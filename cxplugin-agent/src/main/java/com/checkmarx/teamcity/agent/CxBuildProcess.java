package com.checkmarx.teamcity.agent;

import com.checkmarx.teamcity.common.CxConstants;
import com.cx.restclient.CxClientDelegator;
import com.cx.restclient.common.CxPARAM;
import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.dto.ScanResults;
import com.cx.restclient.dto.ScannerType;
import com.cx.restclient.dto.scansummary.ScanSummary;
import com.cx.restclient.exception.CxClientException;
import com.cx.restclient.sast.dto.SASTResults;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import org.apache.commons.io.FileUtils;
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
    private CxClientDelegator clientDelegator;

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
        String appenderName = "cxAppender_" + agentRunningBuild.getBuildId();
        boolean sastCreated = false;
        boolean osaCreated = false;

        ScanResults ret = new ScanResults();

        try {

            Map<String, String> runnerParameters = buildRunnerContext.getRunnerParameters();
            Map<String, String> sharedConfigParameters = agentRunningBuild.getSharedConfigParameters();
            checkoutDirectory = agentRunningBuild.getCheckoutDirectory();
            buildDirectory = new File(agentRunningBuild.getBuildTempDirectory() + "/" + agentRunningBuild.getProjectName() + "/" + agentRunningBuild.getBuildTypeName() + "/" + agentRunningBuild.getBuildNumber());
            config = CxConfigHelper.resolveConfigurations(runnerParameters, sharedConfigParameters, checkoutDirectory, buildDirectory);
            pluginVersion = sharedConfigParameters.get(CxConstants.TEAMCITY_PLUGIN_VERSION);

            printConfiguration();
            if (!config.isSastEnabled() && !(config.isOsaEnabled() || config.isAstScaEnabled())) {
                logger.error("Both SAST and OSA are disabled. exiting");
                return null;
                //TODO run.setResult(Result.FAILURE);
            }
            try {
                clientDelegator = new CxClientDelegator(config, logger);
                clientDelegator.init();
            } catch (Exception ex) {
                if (ex.getMessage().contains("Server is unavailable") && config.isSastEnabled()) {
                    try {
                        clientDelegator.getSastClient().login();
                    } catch (CxClientException e) {
                        throw new IOException(e);
                    }
                    throw new RunBuildException(CONNECTION_FAILED_COMPATIBILITY);
                }

                throw new RunBuildException("Failed to init CxClient: " + ex.getMessage(), ex);
            }
            if(config.isOsaEnabled() || config.isAstScaEnabled()){
                Logger.getRootLogger().addAppender(new CxAppender(agentRunningBuild.getBuildLogger(), appenderName));
                ret = clientDelegator.initiateScan();
                Logger.getRootLogger().removeAppender(appenderName);

            }
            else
                ret = clientDelegator.initiateScan();


            //Asynchronous MODE
            if (!config.getSynchronous()) {
                logger.info("Running in Asynchronous mode. Not waiting for scan to finish");
                if (ret.getSastCreateException() != null || ret.getOsaCreateException() != null) {
                    printScanBuildFailure(null, ret, logger);
                    return BuildFinishedStatus.FINISHED_FAILED;
                }

                return BuildFinishedStatus.FINISHED_SUCCESS;
            }

            ret = clientDelegator.waitForScanResults();

            if (config.getGeneratePDFReport()) {
                publishPDFReport(ret.getSastResults());
            }



            if (config.getEnablePolicyViolations()) {
                clientDelegator.printIsProjectViolated(ret);
            }

            String summaryStr = clientDelegator.generateHTMLSummary(ret);
            File htmlFile = new File(buildDirectory, REPORT_HTML_NAME);
            try {
                FileUtils.writeStringToFile(htmlFile, summaryStr);
            } catch (IOException e) {
                logger.error("Failed to generate full html report: " + e.getMessage());
            }
            publishArtifact(htmlFile.getAbsolutePath());

            //assert if expected exception is thrown  OR when vulnerabilities under threshold OR when policy violated
            //String buildFailureResult = ShragaUtils.getBuildFailureResult(config, ret.getSastResults(), ret.getOsaResults());
            ScanSummary scanSummary = new ScanSummary(config,ret.getSastResults(),ret.getOsaResults(),ret.getScaResults());


            if (scanSummary.hasErrors() || ret.getSastWaitException() != null || ret.getSastCreateException() != null ||
                    ret.getOsaCreateException() != null || ret.getOsaWaitException() != null) {
                printScanBuildFailure(scanSummary, ret, logger);
                return BuildFinishedStatus.FINISHED_FAILED;
            }
            return BuildFinishedStatus.FINISHED_SUCCESS;
        } catch (InterruptedException e) {
            logger.error("Interrupted exception: " + e.getMessage());

            //TODO CHECK HOW TO CANCLE SCAN IN NEW COMMON
           /* if (clientDelegator != null && sastCreated) {
                logger.error("Canceling scan on the Checkmarx server...");
                cancelScan(clientDelegator);
            }*/
            throw new RunBuildException(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected exception: " + e.getMessage(), e);
            throw new RunBuildException(e);
        } finally {
            if (clientDelegator != null) {
                clientDelegator.close();
            }
        }

    }

/*    private void cancelScan(CxShragaClient shraga) {
        try {
            shraga.cancelSASTScan();
        } catch (Exception ignored) {
        }
    }*/


    private void printConfiguration() {
        logger.info("----------------------------Configurations:-----------------------------");
        logger.info("Plugin version: " + pluginVersion);
        logger.info("URL: " + config.getUrl());
        logger.info("Username: " + config.getUsername());
        logger.info("Project name: " + config.getProjectName());
        logger.info("Team ID: " + config.getTeamId());
        logger.info("Is synchronous scan: " + config.getSynchronous());
        logger.info("CxSAST enabled: " + config.isSastEnabled());
        if (config.isSastEnabled()) {
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
        if (config.isOsaEnabled() || config.isAstScaEnabled()) {
            String scannerType = config.isOsaEnabled() ? ScannerType.OSA.getDisplayName() : ScannerType.AST_SCA.getDisplayName();
            logger.info("Dependency scanner type: " + scannerType);
            logger.info(scannerType+" dependency Scan filter patterns: " + config.getOsaFilterPattern());
            logger.info(scannerType +" dependency Scan archive extract patterns: " + config.getOsaArchiveIncludePatterns());
            logger.info(scannerType +" dependency Scan Execute dependency managers 'install packages' command before Scan: " + config.getOsaRunInstall());
            logger.info(scannerType +" dependency Scan thresholds enabled: " + config.getOsaThresholdsEnabled());
            if (config.getOsaThresholdsEnabled()) {
                logger.info(scannerType +" dependency Scan high threshold: " + (config.getOsaHighThreshold() == null ? "[No Threshold]" : config.getOsaHighThreshold()));
                logger.info(scannerType+" dependency Scan medium threshold: " + (config.getOsaMediumThreshold() == null ? "[No Threshold]" : config.getOsaMediumThreshold()));
                logger.info(scannerType +" dependency Scan low threshold: " + (config.getOsaLowThreshold() == null ? "[No Threshold]" : config.getOsaLowThreshold()));
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
        long buildId = buildRunnerContext.getBuild().getBuildId();
        String buildTypeId = buildRunnerContext.getBuild().getBuildTypeExternalId();
        return "/repository/download/" + buildTypeId + "/" + buildId + ":id/" + CxConstants.RUNNER_DISPLAY_NAME + "/" + artifactName;
    }

}
