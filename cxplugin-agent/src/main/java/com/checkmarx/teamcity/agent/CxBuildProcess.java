package com.checkmarx.teamcity.agent;

import com.checkmarx.teamcity.common.CxConstants;
import com.cx.restclient.CxShragaClient;
import com.cx.restclient.common.CxPARAM;
import com.cx.restclient.common.ShragaUtils;
import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.dto.ScanResults;
import com.cx.restclient.exception.CxClientException;
import com.cx.restclient.osa.dto.OSAResults;
import com.cx.restclient.sast.dto.SASTResults;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.checkmarx.teamcity.agent.CxPluginUtils.printScanBuildFailure;
import static com.checkmarx.teamcity.common.CxConstants.REPORT_HTML_NAME;
import static com.checkmarx.teamcity.common.CxParam.CONNECTION_FAILED_COMPATIBILITY;

/**
 * Created by: Dorg.
 * Date: 18/04/2017.
 */
public class CxBuildProcess extends CallableBuildProcess {

    //    private static final String CX_REPORT_LOCATION = "/Checkmarx/Reports";
    private static final String REPORT_NAME = "CxSASTReport";

    private final BuildRunnerContext buildRunnerContext;
    private final AgentRunningBuild agentRunningBuild;
    private final ArtifactsWatcher artifactsWatcher;
    private final CxLoggerAdapter logger;
    private String pluginVersion;
    private String sastPDFLink = "";

    private CxScanConfig config;

    private File checkoutDirectory;
    private File buildDirectory;
    CxShragaClient shraga = null;

    private Exception sastException;
    private Exception osaException;


    public CxBuildProcess(AgentRunningBuild agentRunningBuild, BuildRunnerContext buildRunnerContext, ArtifactsWatcher artifactsWatcher) {
        this.agentRunningBuild = agentRunningBuild;
        this.buildRunnerContext = buildRunnerContext;
        this.artifactsWatcher = artifactsWatcher;
        logger = new CxLoggerAdapter(agentRunningBuild.getBuildLogger());
    }

    @Override
    public BuildFinishedStatus call() throws Exception {

        boolean sastCreated = false;
        boolean osaCreated = false;

        ScanResults ret = new ScanResults();
        ret.setSastResults(new SASTResults());
        ret.setOsaResults(new OSAResults());
        try {

            Map<String, String> runnerParameters = buildRunnerContext.getRunnerParameters();
            Map<String, String> sharedConfigParameters = agentRunningBuild.getSharedConfigParameters();
            checkoutDirectory = agentRunningBuild.getCheckoutDirectory();
            buildDirectory = new File(agentRunningBuild.getBuildTempDirectory() + "/" + agentRunningBuild.getProjectName() + "/" + agentRunningBuild.getBuildTypeName() + "/" + agentRunningBuild.getBuildNumber());
            config = CxConfigHelper.resolveConfigurations(runnerParameters, sharedConfigParameters, checkoutDirectory, buildDirectory);
            pluginVersion = sharedConfigParameters.get(CxConstants.TEAMCITY_PLUGIN_VERSION);

            printConfiguration();
            if (!config.getSastEnabled() && !config.getOsaEnabled()) {
                logger.error("Both SAST and OSA are disabled. exiting");
                return null;
                //TODO run.setResult(Result.FAILURE);
            }
            try {
                shraga = new CxShragaClient(config, logger);
                shraga.init();
            } catch (Exception ex) {
                if (ex.getMessage().contains("Server is unavailable")) {
                    try {
                        shraga.login();
                    } catch (CxClientException e) {
                        throw new IOException(e);
                    }
                    throw new RunBuildException(CONNECTION_FAILED_COMPATIBILITY);
                }

                throw new RunBuildException("Failed to init CxClient: " + ex.getMessage(), ex);
            }
            if (config.getSastEnabled()) {
                try {
                    shraga.createSASTScan();
                    sastCreated = true;
                } catch (IOException | CxClientException e) {
                    ret.setSastCreateException(e);
                    logger.error(e.getMessage());
                }
            }
            if (config.getOsaEnabled()) {
                //---------------------------
                //we do this in order to redirect the logs from the filesystem agent component to the build console
                String appenderName = "cxAppender_" + agentRunningBuild.getBuildId();
                Logger.getRootLogger().addAppender(new CxAppender(agentRunningBuild.getBuildLogger(), appenderName));
                //---------------------------
                try {
                    shraga.createOSAScan();
                    osaCreated = true;
                } catch (CxClientException | IOException e) {
                    ret.setOsaCreateException(e);
                    logger.error(e.getMessage());
                } finally {
                    Logger.getRootLogger().removeAppender(appenderName);
                }
            }
            //Asynchronous MODE
            if (!config.getSynchronous()) {
                logger.info("Running in Asynchronous mode. Not waiting for scan to finish");
                if (ret.getSastCreateException() != null || ret.getOsaCreateException() != null) {
                    printScanBuildFailure(null, ret, logger);
                    return BuildFinishedStatus.FINISHED_FAILED;
                }

                return BuildFinishedStatus.FINISHED_SUCCESS;
            }
            if (sastCreated) {
                try {
                    SASTResults sastResults = shraga.waitForSASTResults();
                    ret.setSastResults(sastResults);
                    publishXMLReport(sastResults);
                    if (config.getGeneratePDFReport()) {
                        publishPDFReport(sastResults);
                    }

                } catch (CxClientException | IOException e) {
                    ret.setSastWaitException(e);
                    logger.error(e.getMessage());
                }
            }
            if (osaCreated) {
                try {
                    OSAResults osaResults = shraga.waitForOSAResults();
                    ret.setOsaResults(osaResults);
                } catch (CxClientException | IOException e) {
                    ret.setOsaWaitException(e);
                    logger.error(e.getMessage());
                }
            }

            if (config.getEnablePolicyViolations()) {
                shraga.printIsProjectViolated();
            }

            String summaryStr = shraga.generateHTMLSummary();
            File htmlFile = new File(buildDirectory, REPORT_HTML_NAME);
            try {
                FileUtils.writeStringToFile(htmlFile, summaryStr);
            } catch (IOException e) {
                logger.error("Failed to generate full html report: " + e.getMessage());
            }
            publishArtifact(htmlFile.getAbsolutePath());

            //assert if expected exception is thrown  OR when vulnerabilities under threshold OR when policy violated
            String buildFailureResult = ShragaUtils.getBuildFailureResult(config, ret.getSastResults(), ret.getOsaResults());

            if (!StringUtils.isEmpty(buildFailureResult) || ret.getSastWaitException() != null || ret.getSastCreateException() != null ||
                    ret.getOsaCreateException() != null || ret.getOsaWaitException() != null) {
                printScanBuildFailure(buildFailureResult, ret, logger);
                return BuildFinishedStatus.FINISHED_FAILED;
            }
            return BuildFinishedStatus.FINISHED_SUCCESS;
        } catch (InterruptedException e) {
            logger.error("Interrupted exception: " + e.getMessage());

            if (shraga != null && sastCreated) {
                logger.error("Canceling scan on the Checkmarx server...");
                cancelScan(shraga);
            }
            throw new RunBuildException(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected exception: " + e.getMessage(), e);
            throw new RunBuildException(e);
        } finally {
            if (shraga != null) {
                shraga.close();
            }
        }

    }

    private void cancelScan(CxShragaClient shraga) {
        try {
            shraga.cancelSASTScan();
        } catch (Exception ignored) {
        }
    }


    private void printConfiguration() {
        logger.info("----------------------------Configurations:-----------------------------");
        logger.info("Plugin version: " + pluginVersion);
        logger.info("URL: " + config.getUrl());
        logger.info("Username: " + config.getUsername());
        logger.info("Project name: " + config.getProjectName());
        logger.info("Team ID: " + config.getTeamId());
        logger.info("Is synchronous scan: " + config.getSynchronous());
        logger.info("CxSAST enabled: " + config.getSastEnabled());
        if (config.getSastEnabled()) {
            logger.info("Preset ID: " + config.getPresetId());
            logger.info("Folder exclusions: " + config.getSastFolderExclusions());
            logger.info("Filter pattern: " + config.getSastFilterPattern());
            logger.info("Scan timeout in minutes: " + config.getSastScanTimeoutInMinutes());
            logger.info("Scan comment: " + config.getScanComment());
            logger.info("Is incremental scan: " + config.getIncremental());
            logger.info("Generate PDF report: " + config.getGeneratePDFReport());
            logger.info("CxSAST thresholds enabled: " + config.getSastThresholdsEnabled());
            if (config.getSastThresholdsEnabled()) {
                logger.info("CxSAST high threshold: " + (config.getSastHighThreshold() == null ? "[No Threshold]" : config.getSastHighThreshold()));
                logger.info("CxSAST medium threshold: " + (config.getSastMediumThreshold() == null ? "[No Threshold]" : config.getSastMediumThreshold()));
                logger.info("CxSAST low threshold: " + (config.getSastLowThreshold() == null ? "[No Threshold]" : config.getSastLowThreshold()));
            }
        }
        logger.info("Policy violations enabled: " + config.getEnablePolicyViolations());
        logger.info("CxOSA enabled: " + config.getOsaEnabled());
        if (config.getOsaEnabled()) {
            logger.info("CxOSA filter patterns: " + config.getOsaFilterPattern());
            logger.info("CxOSA archive extract patterns: " + config.getOsaArchiveIncludePatterns());
            logger.info("CxOSA Execute dependency managers 'install packages' command before Scan: " + config.getOsaRunInstall());
            logger.info("CxOSA thresholds enabled: " + config.getOsaThresholdsEnabled());
            if (config.getOsaThresholdsEnabled()) {
                logger.info("CxOSA high threshold: " + (config.getOsaHighThreshold() == null ? "[No Threshold]" : config.getOsaHighThreshold()));
                logger.info("CxOSA medium threshold: " + (config.getOsaMediumThreshold() == null ? "[No Threshold]" : config.getOsaMediumThreshold()));
                logger.info("CxOSA low threshold: " + (config.getOsaLowThreshold() == null ? "[No Threshold]" : config.getOsaLowThreshold()));
            }
        }
        logger.info("------------------------------------------------------------------------");
    }


    private void publishPDFReport(SASTResults sastResults) throws IOException {
        publishArtifact(buildDirectory + CxPARAM.CX_REPORT_LOCATION + File.separator + sastResults.getPdfFileName());
        sastPDFLink = compileLinkToArtifact(sastResults.getPdfFileName());
        sastResults.setSastPDFLink(sastPDFLink);
    }

    private void publishXMLReport(SASTResults sastResults) throws IOException {
        String xmlFileName = REPORT_NAME + ".xml";
        File xmlFile = new File(buildDirectory + CxPARAM.CX_REPORT_LOCATION, xmlFileName);
        FileUtils.writeByteArrayToFile(xmlFile, sastResults.getRawXMLReport());
        publishArtifact(xmlFile.getAbsolutePath());
    }

    private void publishArtifact(String filePath) {
        artifactsWatcher.addNewArtifactsPath(filePath + "=>" + CxConstants.RUNNER_DISPLAY_NAME);
    }

    private String compileLinkToArtifact(String artifactName) {
        String rootServerUrl = agentRunningBuild.getSharedConfigParameters().get(CxConstants.TEAMCITY_SERVER_URL);
        long buildId = buildRunnerContext.getBuild().getBuildId();
        String buildTypeId = buildRunnerContext.getBuild().getBuildTypeExternalId();
        return rootServerUrl + "/repository/download/" + buildTypeId + "/" + buildId + ":id/" + CxConstants.RUNNER_DISPLAY_NAME + "/" + artifactName;
    }

}
