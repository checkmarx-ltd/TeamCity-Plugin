package com.checkmarx.teamcity.agent;

import com.checkmarx.CxJenkinsWebService.CliScanArgs;
import com.checkmarx.CxJenkinsWebService.CxWSCreateReportResponse;
import com.checkmarx.CxJenkinsWebService.CxWSReportType;
import com.checkmarx.CxJenkinsWebService.CxWSResponseRunID;
import com.checkmarx.teamcity.common.*;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import jetbrains.buildServer.serverSide.crypt.EncryptUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.ws.WebServiceException;
import java.io.File;
import java.io.IOException;
import java.util.Map;


public class CxBuildProcess extends CallableBuildProcess {
    private final AgentRunningBuild runningBuild;
    private final ArtifactsWatcher artifactsWatcher;
    private final BuildProgressLogger logger;
    private final Map<String, String> runnerParameters;
    private CxWebService cxWebService;
    CxWSResponseRunID cxWSResponseRunID;
    CxWSCreateReportResponse reportResponse;
    @Nullable
    private long projectId = 0;

    //todo: take out configuration manager that will create a final dto with all configurations


    public CxBuildProcess(@NotNull final AgentRunningBuild runningBuild,
                          @NotNull final BuildRunnerContext context,
                          @NotNull final ArtifactsWatcher artifactsWatcher) throws RunBuildException {
        super(runningBuild);
        this.runningBuild = runningBuild;
        this.artifactsWatcher = artifactsWatcher;
        this.logger = runningBuild.getBuildLogger();
        this.runnerParameters = context.getRunnerParameters();
    }

    public BuildFinishedStatus call() throws Exception {
        final String cxGlobalServer = this.runnerParameters.get(CxConstants.CXGLOBALSERVER);
        String cxServerUrl, cxUser, cxPass;
        if (cxGlobalServer != null && cxGlobalServer.equals(CxConstants.TRUE)) {
            this.logger.message("Using global Cx Server settings");
            final Map<String, String> sharedConfigParameters = this.runningBuild.getSharedConfigParameters();
            cxServerUrl = sharedConfigParameters.get(CxConstants.CXSERVERURL);
            cxUser = sharedConfigParameters.get(CxConstants.CXUSER);
            cxPass = sharedConfigParameters.get(CxConstants.CXPASS);
        } else {
            this.logger.message("Using build step Cx Server settings");
            cxServerUrl = this.runnerParameters.get(CxConstants.CXSERVERURL);
            cxUser = this.runnerParameters.get(CxConstants.CXUSER);
            cxPass = this.runnerParameters.get(CxConstants.CXPASS);
        }
        try {
            if (cxPass != null) {
                cxPass = EncryptUtil.unscramble(cxPass);
            }
        } catch (IllegalArgumentException e) {
            throw new RunBuildException("Failed to retrieve password");
        }
        this.logger.message("Cx server configured as '" + cxServerUrl + "'");

        try {
            this.cxWebService = new CxWebService(cxServerUrl);
            this.cxWebService.login(cxUser, cxPass);
        } catch (Exception e) {
            throw new RunBuildException("Login to Cx server failed: '" + e.getMessage()  + "'");
        }
        this.logger.message("Login to Cx server successful");

        try {
            setProjectId();
            this.cxWSResponseRunID = submitScan();

            final long scanId = this.cxWebService.trackScanProgress(this.cxWSResponseRunID, cxUser, cxPass, false, 0,
                    this.logger);
            if (scanId == 0) {
                throw new RunBuildException("trackScanProgress failed");
            }

            this.reportResponse = this.cxWebService.generateScanReport(scanId, CxWSReportType.XML, this.logger);
            final File xmlReportFile = CxScanArtifacts.getXmlFile(this.runningBuild);
            this.cxWebService.retrieveScanReport(reportResponse.getID(), xmlReportFile, CxWSReportType.XML,
                    this.logger);
            CxScanArtifacts.publishArtifact(this.artifactsWatcher, xmlReportFile);

            final String cxGeneratePdf = this.runnerParameters.get(CxConstants.CXGENERATEPDF);
            if (cxGeneratePdf != null && cxGeneratePdf.equals(CxConstants.TRUE)) {
                this.reportResponse = this.cxWebService.generateScanReport(scanId, CxWSReportType.PDF, this.logger);
                final File pdfReportFile = CxScanArtifacts.getPdfFile(this.runningBuild);
                this.cxWebService.retrieveScanReport(this.reportResponse.getID(), pdfReportFile, CxWSReportType.PDF,
                        this.logger);
                CxScanArtifacts.publishArtifact(this.artifactsWatcher, pdfReportFile);
            }

            final CxScanResult cxScanResult = addScanResultAction(xmlReportFile);
            if (!cxScanResult.isResultValid()) {
                throw new RunBuildException("Invalid scan result");
            }

            final String thresholdEnable = this.runnerParameters.get(CxConstants.CXTHRESHOLDENABLE);
            if (thresholdEnable != null && thresholdEnable.equals(CxConstants.TRUE)) {
                if (isThresholdCrossed(cxScanResult)) {
                    this.logger.buildFailureDescription("Vulnerabilities above threshold");
                    return BuildFinishedStatus.FINISHED_FAILED;
                }
            }
        } catch (WebServiceException e) {
            throw new RunBuildException("WebService error: " + e.getMessage());
        } catch (IOException e) {
            throw new RunBuildException("IO error: " + e.getMessage());
        } catch (InterruptedException e) {
            this.logger.message(e.getMessage());
            return BuildFinishedStatus.INTERRUPTED;
        }

        return BuildFinishedStatus.FINISHED_SUCCESS;
    }

    protected void cancelBuild() {
        if (this.reportResponse != null) {
            this.cxWebService.cancelScanReport(reportResponse.getID());
        } else if (this.cxWSResponseRunID != null) {
            this.cxWebService.cancelScan(cxWSResponseRunID.getRunId());
        }
    }

    @NotNull
    private CxScanResult addScanResultAction(final File xmlReportFile) throws CxAbortException {
        CxScanResult cxScanResult = new CxScanResult();
        if (xmlReportFile != null){
            cxScanResult.readScanXMLReport(xmlReportFile);
        }
        return cxScanResult;
    }

    private void setProjectId() throws CxAbortException {
        if (this.projectId == 0) {
            CxProjectResolver projectContract = new CxProjectResolver(this.cxWebService, this.logger);
            final String cxProject = this.runnerParameters.get(CxConstants.CXPROJECT);
            final String cxTeam = this.runnerParameters.get(CxConstants.CXTEAM);
            this.projectId = projectContract.resolveProjectId(cxProject, cxTeam);
        }
    }


    private CxWSResponseRunID submitScan() throws IOException {
        try {
            final CliScanArgs cliScanArgs = createCliScanArgs(new byte[]{});
            final boolean incremental = checkIncrementalScanAndWriteResultToLog();
            final String zipPath = zipWorkspaceFolder();

            CxWSResponseRunID cxWSResponseRunId;

            if (projectId == 0){
                cxWSResponseRunId = this.cxWebService.createAndRunProject(cliScanArgs.getPrjSettings(),
                        cliScanArgs.getSrcCodeSettings().getPackagedCode(), true, true, zipPath,
                        cliScanArgs.getComment(), this.logger);
                this.projectId = cxWSResponseRunId.getProjectID();
            } else {
                if (incremental) {
                    cxWSResponseRunId = this.cxWebService.runIncrementalScan(cliScanArgs.getPrjSettings(),
                            cliScanArgs.getSrcCodeSettings().getPackagedCode(), true, true, zipPath,
                            cliScanArgs.getComment(), this.logger);
                } else {
                    cxWSResponseRunId = this.cxWebService.runScanAndAddToProject(cliScanArgs.getPrjSettings(),
                            cliScanArgs.getSrcCodeSettings().getPackagedCode(), true, true, zipPath,
                            cliScanArgs.getComment(), this.logger);
                }
            }

            final File tempFile = new File(zipPath);
            if(tempFile.delete()) {
                this.logger.message("Temporary zip file deleted");
            }else {
                this.logger.warning("Temporary zip file was not deleted");
            }
            this.logger.message("Scan job submitted successfully");

            return cxWSResponseRunId;
        } catch (InterruptedException e) {
            throw new CxAbortException("Remote operation failed on slave node: " + e.getMessage());
        }
    }

    private CliScanArgs createCliScanArgs(final byte[] compressedSources) {
        return CxCliScanArgsFactory.create(this.logger, this.runnerParameters, this.projectId, compressedSources);
    }

    private String zipWorkspaceFolder() throws IOException, InterruptedException {
        CxFolderPattern folderPattern = new CxFolderPattern();
        final String combinedFilterPattern = folderPattern.generatePattern(this.runnerParameters, this.logger);

        CxZip cxZip = new CxZip();
        return cxZip.ZipWorkspaceFolder(this.runningBuild, combinedFilterPattern, this.logger);
    }

    private boolean checkIncrementalScanAndWriteResultToLog() {
        final boolean incremental = isThisBuildIncremental();
        if (incremental){
            this.logger.message("Scan job started in incremental scan mode");
        } else {
            this.logger.message("Scan job started in full scan mode");
        }
        return incremental;
    }


    private boolean isThisBuildIncremental() {
        final String cxIncremental = this.runnerParameters.get(CxConstants.CXINCREMENTAL);
        if (cxIncremental == null || !cxIncremental.equals(CxConstants.TRUE)) {
            return false;
        }

        final String cxPeriodicFullScans = this.runnerParameters.get(CxConstants.CXPERIODICFULLSCANS);
        if (cxPeriodicFullScans == null || !cxPeriodicFullScans.equals(CxConstants.TRUE)) {
            return true;
        }

        final String cxNumberIncremental = this.runnerParameters.get(CxConstants.CXNUMBERINCREMENTAL);
        if (cxNumberIncremental == null || cxNumberIncremental.isEmpty()) {
            this.logger.warning("cxNumberIncremental is empty");
            return true;
        }

        // If user asked to perform full scan after every 9 incremental scans -
        // it means that every 10th scan should be full,
        // that is the ordinal numbers of full scans will be "1", "11", "21" and so on...
        final String buildNumber = this.runningBuild.getBuildNumber();
        if (buildNumber == null || buildNumber.isEmpty()) {
            this.logger.error("buildNumber is empty");
            return true;
        }
        final int buildNumberInt = Integer.parseInt(buildNumber);
        final int fullScanCycle = Integer.parseInt(cxNumberIncremental);
        final boolean shouldBeFullScan = buildNumberInt % (fullScanCycle + 1) == 1;
        return !shouldBeFullScan;
    }

    private boolean isThresholdCrossed(final CxScanResult cxScanResult) {
        final String cxThresholdHigh = this.runnerParameters.get(CxConstants.CXTHRESHOLDHIGH);
        final String cxThresholdMedium = this.runnerParameters.get(CxConstants.CXTHRESHOLDMEDIUM);
        final String cxThresholdLow = this.runnerParameters.get(CxConstants.CXTHRESHOLDLOW);

        final int cxThresholdHighInt = cxThresholdHigh != null ? Integer.parseInt(cxThresholdHigh) : 0;
        final int cxThresholdMediumInt = cxThresholdMedium != null ? Integer.parseInt(cxThresholdMedium) : 0;
        final int cxThresholdLowInt = cxThresholdLow != null ? Integer.parseInt(cxThresholdLow) : 0;

        logFoundVulnerabilities("high", cxScanResult.getHighCount(), cxThresholdHighInt);
        logFoundVulnerabilities("medium", cxScanResult.getMediumCount(), cxThresholdMediumInt);
        logFoundVulnerabilities("low", cxScanResult.getLowCount(), cxThresholdLowInt);

        return cxScanResult.getHighCount() > cxThresholdHighInt
                || cxScanResult.getMediumCount() > cxThresholdMediumInt
                || cxScanResult.getLowCount() > cxThresholdLowInt;
    }

    private void logFoundVulnerabilities(final String severity, final int actualNumber, final int configuredHighThreshold) {
        this.logger.message("Number of " + severity + " severity vulnerabilities: " + actualNumber +
                " stability threshold: " + configuredHighThreshold);
    }

}
