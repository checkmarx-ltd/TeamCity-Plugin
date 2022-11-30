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
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.checkmarx.teamcity.agent.CxPluginUtils.printScanBuildFailure;
import static com.checkmarx.teamcity.common.CxConstants.CX_BUILD_NUMBER;
import static com.checkmarx.teamcity.common.CxConstants.REPORT_HTML_NAME;
import static com.checkmarx.teamcity.common.CxParam.CONNECTION_FAILED_COMPATIBILITY;

/**
 * Created by: Dorg.
 * Date: 18/04/2017.
 */
public class CxBuildProcess extends CallableBuildProcess {

    //    private static final String CX_REPORT_LOCATION = "/Checkmarx/Reports";
    private static final String REPORT_NAME = "CxSASTReport";
    private static final String NO_THRESHOLD = "[No Threshold]";

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


    public CxBuildProcess(AgentRunningBuild agentRunningBuild, BuildRunnerContext buildRunnerContext, ArtifactsWatcher artifactsWatcher) {
        this.agentRunningBuild = agentRunningBuild;
        this.buildRunnerContext = buildRunnerContext;
        this.artifactsWatcher = artifactsWatcher;
        logger = new CxLoggerAdapter(agentRunningBuild.getBuildLogger());
    }

    @Override
    public BuildFinishedStatus call() throws Exception {
        String appenderName = "cxAppender_" + agentRunningBuild.getBuildId();

        ScanResults ret = new ScanResults();

        try {
            logger.info("Retrieving config parameters :");
            Map<String, String> allParameters = buildRunnerContext.getBuildParameters().getAllParameters();
            for (Map.Entry<String, String> entry : allParameters.entrySet()) {
                if (entry.getKey().contains(".CX_")) {
                    if (StringUtils.isNotEmpty(entry.getValue())) {
                        System.setProperty(entry.getKey().substring(entry.getKey().indexOf(".") + 1), entry.getValue());
                    }
                }
            }

            Map<String, String> runnerParameters = buildRunnerContext.getRunnerParameters();
            logger.info("-->Runner parameters");
            Map<String, String> sharedConfigParameters = agentRunningBuild.getSharedConfigParameters();
            logger.info("-->Shared Config parameters");
            checkoutDirectory = agentRunningBuild.getCheckoutDirectory();
            buildDirectory = new File(agentRunningBuild.getBuildTempDirectory() + "/" + agentRunningBuild.getProjectName() + "/" + agentRunningBuild.getBuildTypeName() + "/" + agentRunningBuild.getBuildNumber());
            Map<String, String> otherParameters = new HashMap<>();
            otherParameters.put(CX_BUILD_NUMBER, agentRunningBuild.getBuildNumber());

            logger.info("Resolving Configurations");
            config = CxConfigHelper.resolveConfigurations(runnerParameters, sharedConfigParameters, checkoutDirectory, buildDirectory, otherParameters, agentRunningBuild, logger);
            pluginVersion = sharedConfigParameters.get(CxConstants.TEAMCITY_PLUGIN_VERSION);

            printConfiguration();
            if (!config.isSastEnabled() && !(config.isOsaEnabled() || config.isAstScaEnabled())) {
                logger.error("Both SAST and OSA are disabled. exiting");
                return null;
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
            if (config.isOsaEnabled() || config.isAstScaEnabled()) {
                Logger.getRootLogger().addAppender(new CxAppender(agentRunningBuild.getBuildLogger(), appenderName));
            }
            ret = clientDelegator.initiateScan();
            if (config.isOsaEnabled() || config.isAstScaEnabled()) {
                Logger.getRootLogger().removeAppender(appenderName);
            }

            ret = config.getSynchronous() ? clientDelegator.waitForScanResults() : clientDelegator.getLatestScanResults();

            if (config.getEnablePolicyViolations()) {
                clientDelegator.printIsProjectViolated(ret);
            }


            //assert if expected exception is thrown  OR when vulnerabilities under threshold OR when policy violated
            ScanSummary scanSummary = new ScanSummary(config, ret.getSastResults(), ret.getOsaResults(), ret.getScaResults());
            if (scanSummary.hasErrors() || ret.getGeneralException() != null ||
                    (config.isSastEnabled() && (ret.getSastResults() == null || ret.getSastResults().getException() != null)) ||
                    (config.isOsaEnabled() && (ret.getOsaResults() == null || ret.getOsaResults().getException() != null)) ||
                    (config.isAstScaEnabled() && (ret.getScaResults() == null || ret.getScaResults().getException() != null))) {

                StringBuilder scanFailedAtServer = new StringBuilder();
                if (config.isSastEnabled() && (ret.getSastResults() == null || !ret.getSastResults().isSastResultsReady()))
                    scanFailedAtServer.append("CxSAST scan results are not found. Scan might have failed at the server or aborted by the server.\n");
                if (config.isOsaEnabled() && (ret.getOsaResults() == null || !ret.getOsaResults().isOsaResultsReady()))
                    scanFailedAtServer.append("CxSAST OSA scan results are not found. Scan might have failed at the server or aborted by the server.\n");
                if (config.isAstScaEnabled() && (ret.getScaResults() == null || !ret.getScaResults().isScaResultReady()))
                    scanFailedAtServer.append("CxAST SCA scan results are not found. Scan might have failed at the server or aborted by the server.\n");

                if (scanSummary.hasErrors() && scanFailedAtServer.toString().isEmpty())
                    scanFailedAtServer.append(scanSummary.toString());
                else if (scanSummary.hasErrors())
                    scanFailedAtServer.append("\n").append(scanSummary.toString());

                printScanBuildFailure(scanFailedAtServer.toString(), ret, logger);

                //handle hard failures. In case of threshold or policy failure, we still need to generate report before returning.
                //Hence, cannot return yet
                if (!scanSummary.hasErrors())
                    return BuildFinishedStatus.FINISHED_FAILED;
            }
            //Asynchronous MODE
            if (!config.getSynchronous()) {
                logger.info("Running in Asynchronous mode. Not waiting for scan to finish");
                if (ret.getException() != null || ret.getGeneralException() != null) {
                    printScanBuildFailure(null, ret, logger);
                    return BuildFinishedStatus.FINISHED_FAILED;
                }

                return BuildFinishedStatus.FINISHED_SUCCESS;
            }

            if (config.getSynchronous() && config.isSastEnabled() && ((ret.getSastResults() != null
                    && ret.getSastResults().getException() != null
                    && ret.getSastResults().getScanId() > 0))) {
                cancelScan(clientDelegator);
            }
            if (ret.getSastResults() != null) {
                publishXMLReport(ret.getSastResults());
            }
            if (config.getGeneratePDFReport()) {
                publishPDFReport(ret.getSastResults());
            }

            String summaryStr = clientDelegator.generateHTMLSummary(ret);
            File htmlFile = new File(buildDirectory, REPORT_HTML_NAME);
            try {
                FileUtils.writeStringToFile(htmlFile, summaryStr, StandardCharsets.UTF_8);
            } catch (IOException e) {
                logger.error("Failed to generate full html report: " + e.getMessage());
            }
            publishArtifact(htmlFile.getAbsolutePath());
            if (scanSummary.hasErrors()) {
                return BuildFinishedStatus.FINISHED_FAILED;
            }

            return BuildFinishedStatus.FINISHED_SUCCESS;
        } catch (InterruptedException e) {
            logger.error("Interrupted exception: " + e.getMessage());
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

    private void cancelScan(CxClientDelegator delegator) {
        try {
            delegator.getSastClient().cancelSASTScan();
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
        logger.info("CxSAST enabled: " + config.isSastEnabled());
        logger.info("Proxy Enabled: " + config.isProxy());
        if (config.isSastEnabled()) {
            logger.info("Preset ID: " + config.getPresetId());
            logger.info("Engine Configuration ID : " + config.getEngineConfigurationId());
            logger.info("Folder exclusions: " + config.getSastFolderExclusions());
            logger.info("Filter pattern: " + config.getSastFilterPattern());
            logger.info("Scan timeout in minutes: " + config.getSastScanTimeoutInMinutes());
            logger.info("Scan comment: " + config.getScanComment());
            logger.info("Is incremental scan: " + config.getIncremental());
            logger.info("Custom Fields: " + config.getCustomFields());
            logger.info("Generate PDF report: " + config.getGeneratePDFReport());
            logger.info("CxSAST thresholds enabled: " + config.getSastThresholdsEnabled());
            if (config.getSastThresholdsEnabled()) {
                logger.info("CxSAST high threshold: " + (config.getSastHighThreshold() == null ? NO_THRESHOLD : config.getSastHighThreshold()));
                logger.info("CxSAST medium threshold: " + (config.getSastMediumThreshold() == null ? NO_THRESHOLD : config.getSastMediumThreshold()));
                logger.info("CxSAST low threshold: " + (config.getSastLowThreshold() == null ? NO_THRESHOLD : config.getSastLowThreshold()));
            }
        }
        logger.info("Policy violations enabled: " + config.getEnablePolicyViolations());
        logger.info("Dependency Scan enabled : " + (config.isOsaEnabled() || config.isAstScaEnabled()));
        if (config.isOsaEnabled() || config.isAstScaEnabled()) {
            String scannerType = config.isOsaEnabled() ? ScannerType.OSA.getDisplayName() : ScannerType.AST_SCA.getDisplayName();
            logger.info("Dependency Scan type : " + scannerType);
            logger.info("Dependency scan configuration:");
            logger.info(" Include/Exclude Filter patterns: " + config.getOsaFilterPattern());
            logger.info(" Dependency Scan thresholds enabled: " + config.getOsaThresholdsEnabled());
            if (config.getOsaThresholdsEnabled()) {
                logger.info(" Dependency Scan high threshold: " + (config.getOsaHighThreshold() == null ? "[No Threshold]" : config.getOsaHighThreshold()));
                logger.info(" Dependency Scan medium threshold: " + (config.getOsaMediumThreshold() == null ? "[No Threshold]" : config.getOsaMediumThreshold()));
                logger.info(" Dependency Scan low threshold: " + (config.getOsaLowThreshold() == null ? "[No Threshold]" : config.getOsaLowThreshold()));
            }
            if (config.isOsaEnabled()) {
                logger.info(" CxOSA archive extract patterns: " + config.getOsaArchiveIncludePatterns());
                logger.info(" Execute dependency managers 'install packages' command before CxOSA Scan: " + config.getOsaRunInstall());
            } else if (config.isAstScaEnabled()) {
                logger.info(" CxSCA Tenant: " + config.getAstScaConfig().getTenant());
                logger.info(" CxSCA TeamPath: " + config.getAstScaConfig().getTeamPath());
            }
        }
        logger.info("------------------------------------------------------------------------");
    }


    private void publishPDFReport(SASTResults sastResults) {
        try {
            publishArtifact(buildDirectory + CxPARAM.CX_REPORT_LOCATION + File.separator + sastResults.getPdfFileName());
            sastPDFLink = compileLinkToArtifact(sastResults.getPdfFileName());
            sastResults.setSastPDFLink(sastPDFLink);
        } catch (Exception e) {
            logger.error("Fail to publish PDF report");
        }
    }

    private void publishXMLReport(SASTResults sastResults) {
        try {
            String xmlFileName = REPORT_NAME + ".xml";
            File xmlFile = new File(buildDirectory + CxPARAM.CX_REPORT_LOCATION, xmlFileName);
            FileUtils.writeByteArrayToFile(xmlFile, sastResults.getRawXMLReport());
            publishArtifact(xmlFile.getAbsolutePath());
        } catch (Exception e) {
            boolean isExist = (sastResults != null && sastResults.getRawXMLReport() != null && sastResults.getRawXMLReport().length > 0);
            logger.error("Fail to publish XML report, file: " + buildDirectory + CxPARAM.CX_REPORT_LOCATION + REPORT_NAME + ".xml, is xml file exist in SAST result: " + isExist);
        }
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
