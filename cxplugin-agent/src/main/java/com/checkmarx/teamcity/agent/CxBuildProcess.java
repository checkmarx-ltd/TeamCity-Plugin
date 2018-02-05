package com.checkmarx.teamcity.agent;

import com.checkmarx.teamcity.common.CxPluginUtils;
import com.checkmarx.teamcity.common.CxConstants;
import com.checkmarx.teamcity.common.InvalidParameterException;
import com.checkmarx.teamcity.common.client.*;
import com.checkmarx.teamcity.common.client.dto.*;
import com.checkmarx.teamcity.common.client.exception.CxClientException;
import com.checkmarx.teamcity.common.client.rest.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.whitesource.fs.ComponentScan;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.checkmarx.teamcity.common.CxConstants.*;
import static com.checkmarx.teamcity.common.CxResultsConst.*;

/**
 * Created by: Dorg.
 * Date: 18/04/2017.
 */
public class CxBuildProcess extends CallableBuildProcess {

    private static final long MAX_ZIP_SIZE_BYTES = 209715200;
    private static final String TEMP_FILE_NAME_TO_ZIP = "CxZippedSource";
    private static final String REPORT_NAME = "CxSASTReport";
    public static final String OSA_LIBRARIES_NAME = "CxOSALibraries";
    public static final String OSA_VULNERABILITIES_NAME = "CxOSAVulnerabilities";
    public static final String OSA_SUMMARY_NAME = "CxOSASummary";
    private static final String CX_REPORT_LOCATION = "/Checkmarx/Reports";


    private final BuildRunnerContext buildRunnerContext;
    private final AgentRunningBuild agentRunningBuild;
    private final ArtifactsWatcher artifactsWatcher;
    private final CxBuildLoggerAdapter logger;
    private String pluginVersion;

    private CxScanConfiguration config;
    private CxClientService client;

    private File checkoutDirectory;
    private File tempDirectory;
    private File buildDirectory;
    private String teamFullPath;
    private String projectStateLink;
    private URL url;
    private Exception osaException;
    private CreateScanResponse createScanResponse;
    private CreateOSAScanResponse osaScan;
    private String osaProjectSummaryLink;
    private String scanResultsUrl;
    private String sastPDFLink = "";
    private Exception sastException;
    private OSAScanStatus osaScanStatus;
    private OSASummaryResults osaSummaryResults;
    private ObjectMapper objectMapper = new ObjectMapper();
    private ScanResults scanResults;
    private String osaCVEJson;
    private String osaLibrariesJson;
    private boolean sastResultsReady = false;
    private boolean osaResultsReady = false;


    public CxBuildProcess(AgentRunningBuild agentRunningBuild, BuildRunnerContext buildRunnerContext, ArtifactsWatcher artifactsWatcher) {
        this.agentRunningBuild = agentRunningBuild;
        this.buildRunnerContext = buildRunnerContext;
        this.artifactsWatcher = artifactsWatcher;
        logger = new CxBuildLoggerAdapter(agentRunningBuild.getBuildLogger());
    }

    @Override
    public BuildFinishedStatus call() throws Exception {

        try {

            Map<String, String> runnerParameters = buildRunnerContext.getRunnerParameters();
            Map<String, String> sharedConfigParameters = agentRunningBuild.getSharedConfigParameters();
            checkoutDirectory = agentRunningBuild.getCheckoutDirectory();
            tempDirectory = agentRunningBuild.getAgentTempDirectory();
            buildDirectory = new File(agentRunningBuild.getBuildTempDirectory() + "/" + agentRunningBuild.getProjectName() + "/" + agentRunningBuild.getBuildTypeName() + "/" + agentRunningBuild.getBuildNumber() + CX_REPORT_LOCATION);

            config = CxScanConfiguration.resolveConfigurations(runnerParameters, sharedConfigParameters);
            pluginVersion = sharedConfigParameters.get(CxConstants.TEAMCITY_PLUGIN_VERSION);
            printConfiguration();
            url = new URL(config.getUrl());
            client = new CxClientServiceImpl(url, config.getUsername(), config.getPassword());
            client.setLogger(logger);
            client.checkServerConnectivity();
            client.loginToServer();

            teamFullPath = client.resolveTeamNameFromTeamId(config.getTeamId());

            createScanResponse = createScan();


            if (config.isOsaEnabled()) {
                try {
                    osaScan = createOSAScan();

                } catch (InterruptedException e) {
                    throw e;

                } catch (Exception e) {
                    logger.error("Failed to create OSA Scan: " + e.getMessage());
                    osaException = e;
                }
            }

            //Asynchronous MODE
            if (!config.isSynchronous()) {

                if (osaException != null) {
                    throw osaException;
                }
                logger.info("Running in Asynchronous mode. Not waiting for scan to finish");
                return BuildFinishedStatus.FINISHED_SUCCESS;
            }

            try {

                retrieveScanResults();

            } catch (InterruptedException e) {
                throw e;

            } catch (CxClientException e) {
                logger.error("Failed to perform CxSAST scan: " + e.getMessage());
                sastException = new CxClientException("Failed to perform CxSAST scan: ", e);

            } catch (Exception e) {
                logger.error("Failed to perform CxSAST scan: " + e.getMessage());
                sastException = new Exception("Failed to perform CxSAST scan: ", e);
            }

            if (config.isOsaEnabled()) {
                try {
                    if (osaException != null) {
                        throw osaException;
                    }
                    retrieveOSAScanResults();

                } catch (Exception e) {
                    osaException = e;
                    throw osaException;
                }
            }

            logger.info("Generating full html report");
            generateCxHTMLReport();

            if (sastException != null) {
                throw sastException;
            }


        } catch (InterruptedException e) {
            logger.error("Interrupted exception: " + e.getMessage());

            if (client != null && createScanResponse != null) {
                logger.info("Canceling scan on the Checkmarx server...");
                client.cancelScan(createScanResponse.getRunId());
            }
            throw new RunBuildException(e.getMessage());

        } catch (CxClientException e) {
            logger.error(e.getMessage());

            if (osaException == null && sastException == null) {
                sastException = e;
            }

        } catch (InvalidParameterException e) {
            logger.error(e.getMessage());
            throw new RunBuildException(e);
        } catch (MalformedURLException e) {
            throw new RunBuildException("Invalid URL: " + config.getUrl());
        } catch (Exception e) {
            logger.error("Unexpected exception: " + e.getMessage(), e);
            throw new RunBuildException(e);
        } finally {
            deleteTempFiles();
            closeClient(client);
        }

        //assert if expected exception is thrown  OR when vulnerabilities under threshold
        StringBuilder res = new StringBuilder("");
        boolean thresholdExceeded = assertVulnerabilities(scanResults, osaSummaryResults, res);
        if (thresholdExceeded || sastException != null || osaException != null) {
            printBuildFailure(res, thresholdExceeded, sastException, osaException);
            return BuildFinishedStatus.FINISHED_FAILED;
        }

        return BuildFinishedStatus.FINISHED_SUCCESS;
    }

    private CreateScanResponse createScan() throws IOException, InterruptedException, RunBuildException, CxClientException {

        //prepare sources to scan (zip them)
        logger.info("Zipping sources");
        File zipTempFile = zipWorkspaceFolder(config.getFolderExclusions(), config.getFilterPattern(), MAX_ZIP_SIZE_BYTES, true);

        //send sources to scan
        byte[] zippedSources = getBytesFromZippedSources(zipTempFile);
        LocalScanConfiguration conf = generateScanConfiguration(zippedSources);
        CreateScanResponse createScanResponse = client.createLocalScan(conf);
        projectStateLink = CxPluginHelper.composeProjectStateLink(url.toString(), createScanResponse.getProjectId());
        logger.info("Scan created successfully. Link to project state: " + projectStateLink);

        if (zipTempFile.exists() && !zipTempFile.delete()) {
            logger.warn("Failed to delete temporary zip file: " + zipTempFile.getAbsolutePath());
        } else {
            logger.info("Temporary file deleted");
        }
        return createScanResponse;
    }


    private File zipWorkspaceFolder(String folderExclusions, String filterPattern, long maxZipSizeInBytes, boolean writeToLog) throws IOException, InterruptedException {
        final String combinedFilterPattern = CxFolderPattern.generatePattern(folderExclusions, filterPattern, logger);
        CxZip cxZip = new CxZip(TEMP_FILE_NAME_TO_ZIP).setMaxZipSizeInBytes(maxZipSizeInBytes);
        return cxZip.zipWorkspaceFolder(checkoutDirectory, tempDirectory, combinedFilterPattern, logger, writeToLog);
    }

    private byte[] getBytesFromZippedSources(File zip) throws RunBuildException {
        logger.info("Converting zipped sources to byte array");
        byte[] zipFileByte;
        InputStream fileStream = null;
        try {
            fileStream = new FileInputStream(zip);
            zipFileByte = IOUtils.toByteArray(fileStream);
        } catch (Exception e) {
            throw new RunBuildException("Fail to set zipped file into project: " + e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(fileStream);
        }
        return zipFileByte;
    }

    private LocalScanConfiguration generateScanConfiguration(byte[] zippedSources) {
        LocalScanConfiguration ret = new LocalScanConfiguration();
        ret.setProjectName(config.getProjectName());
        ret.setClientOrigin(ClientOrigin.TEAMCITY);
        ret.setFolderExclusions(config.getFolderExclusions());
        ret.setFullTeamPath(teamFullPath);
        ret.setComment(config.getScanComment());
        ret.setIncrementalScan(config.isIncremental());
        ret.setPresetId(config.getPresetId());
        ret.setZippedSources(zippedSources);
        ret.setFileName(config.getProjectName());

        return ret;
    }

    private CreateOSAScanResponse createOSAScan() throws IOException, InterruptedException, CxClientException {

        logger.info("Creating OSA scan");
        logger.info("Scanning for CxOSA compatible files");
        Properties scannerProperties = CxPluginUtils.generateOSAScanConfiguration(config.getOsaFilterPattern(),
                                                                                    config.getOsaArchiveIncludePatterns(),
                                                                                    checkoutDirectory.getAbsolutePath(), config.isOsaInstallBeforeScan());
        ComponentScan componentScan = new ComponentScan(scannerProperties);
        String osaDependenciesJson = componentScan.scan();
        writeToOsaListToTemp(osaDependenciesJson);
        logger.info("Sending OSA scan request");
        CreateOSAScanResponse osaScan = client.createOSAScan(createScanResponse.getProjectId(), osaDependenciesJson);
        osaProjectSummaryLink = CxPluginHelper.composeProjectOSASummaryLink(config.getUrl(), createScanResponse.getProjectId());
        logger.info("OSA scan created successfully");

        return osaScan;
    }

    private void writeToOsaListToTemp(String osaDependenciesJson) {
        try {
            File temp = new File(FileUtils.getTempDirectory(), "CxOSAFileList.json");
            FileUtils.writeStringToFile(temp, osaDependenciesJson, Charset.defaultCharset());
            logger.info("OSA file list saved to file: ["+temp.getAbsolutePath()+"]");
        } catch (Exception e) {
            logger.info("Failed to write OSA file list to temp directory: " + e.getMessage());
        }

    }


    private void printConfiguration() {
        logger.info("----------------------------Configurations:-----------------------------");
        logger.info("Plugin version: " + pluginVersion);
        logger.info("URL: " + config.getUrl());
        logger.info("Username: " + config.getUsername());
        logger.info("Project name: " + config.getProjectName());
        logger.info("Preset ID: " + config.getPresetId());
        logger.info("Team ID: " + config.getTeamId());
        logger.info("Folder exclusions: " + config.getFolderExclusions());
        logger.info("Filter pattern: " + config.getFilterPattern());
        logger.info("Scan timeout in minutes: " + config.getScanTimeoutInMinutes());
        logger.info("Scan comment: " + config.getScanComment());
        logger.info("Is incremental scan: " + config.isIncremental());
        logger.info("Generate PDF report: " + config.isGeneratePDFReport());
        logger.info("CxOSA enabled: " + config.isOsaEnabled());

        logger.info("Is synchronous scan: " + config.isSynchronous());
        logger.info("CxSAST thresholds enabled: " + config.isThresholdsEnabled());
        if (config.isThresholdsEnabled()) {
            logger.info("CxSAST high threshold: " + (config.getHighThreshold() == null ? "[No Threshold]" : config.getHighThreshold()));
            logger.info("CxSAST medium threshold: " + (config.getMediumThreshold() == null ? "[No Threshold]" : config.getMediumThreshold()));
            logger.info("CxSAST low threshold: " + (config.getLowThreshold() == null ? "[No Threshold]" : config.getLowThreshold()));
        }
        if (config.isOsaEnabled()) {
            logger.info("CxOSA filter patterns: " + config.getOsaFilterPattern());
            logger.info("CxOSA archive extract patterns: " + config.getOsaArchiveIncludePatterns());
            logger.info("CxOSA install NMP and Bower before scan: " + config.isOsaInstallBeforeScan());

            logger.info("CxOSA thresholds enabled: " + config.isOsaThresholdsEnabled());
            if (config.isOsaThresholdsEnabled()) {
                logger.info("CxOSA high threshold: " + (config.getOsaHighThreshold() == null ? "[No Threshold]" : config.getOsaHighThreshold()));
                logger.info("CxOSA medium threshold: " + (config.getOsaMediumThreshold() == null ? "[No Threshold]" : config.getOsaMediumThreshold()));
                logger.info("CxOSA low threshold: " + (config.getOsaLowThreshold() == null ? "[No Threshold]" : config.getOsaLowThreshold()));
            }
        }
        logger.info("------------------------------------------------------------------------");
    }


    private void retrieveScanResults() throws InterruptedException, CxClientException, IOException, JAXBException {
        //wait for SAST scan to finish
        ConsoleScanWaitHandler consoleScanWaitHandler = new ConsoleScanWaitHandler();
        consoleScanWaitHandler.setLogger(logger);
        logger.info("Waiting for CxSAST scan to finish.");
        long timeout = config.getScanTimeoutInMinutes() == null ? 0 : config.getScanTimeoutInMinutes();
        client.loginToServer();
        client.waitForScanToFinish(createScanResponse.getRunId(), timeout, consoleScanWaitHandler);
        logger.info("Scan finished. Retrieving scan results");

        //retrieve SAST scan results
        scanResults = client.retrieveScanResults(createScanResponse.getProjectId());

        scanResultsUrl = CxPluginHelper.composeScanLink(url.toString(), scanResults);
        printResultsToConsole(scanResults);

        //SAST detailed report
        byte[] cxReport = client.getScanReport(scanResults.getScanID(), ReportType.XML);
        String xmlFileName = REPORT_NAME + ".xml";
        File xmlFile = new File(buildDirectory, xmlFileName);
        FileUtils.writeByteArrayToFile(xmlFile, cxReport);
        publishArtifact(xmlFile.getAbsolutePath());

        CxXMLResults reportObj = convertToXMLResult(cxReport);

        scanResults.setScanDetailedReport(reportObj);

        if (config.isGeneratePDFReport()) {
            createPDFReport(scanResults.getScanID());
        }

        sastResultsReady = true;
    }

    private void printResultsToConsole(ScanResults scanResults) {
        logger.info("----------------------------Checkmarx Scan Results(CxSAST):-------------------------------");
        logger.info("High severity results: " + scanResults.getHighSeverityResults());
        logger.info("Medium severity results: " + scanResults.getMediumSeverityResults());
        logger.info("Low severity results: " + scanResults.getLowSeverityResults());
        logger.info("Info severity results: " + scanResults.getInfoSeverityResults());
        logger.info("");
        logger.info("Scan results location: " + scanResultsUrl);
        logger.info("------------------------------------------------------------------------------------------\n");
    }

    private void createPDFReport(long scanId) throws InterruptedException {
        logger.info("Generating PDF report");
        byte[] scanReport;
        try {
            scanReport = client.getScanReport(scanId, ReportType.PDF);
            String pdfFileName = REPORT_NAME + ".pdf";
            File pdfFile = new File(buildDirectory, pdfFileName);
            FileUtils.writeByteArrayToFile(pdfFile, scanReport);
            publishArtifact(pdfFile.getAbsolutePath());
            sastPDFLink = compileLinkToArtifact(pdfFileName);
            logger.info("PDF report location: " + buildDirectory + "/" + pdfFileName);
        } catch (Exception e) {
            logger.error("Fail to generate PDF report", e);
        }
    }

    private void retrieveOSAScanResults() throws InterruptedException, CxClientException, IOException {

        OSAConsoleScanWaitHandler osaConsoleScanWaitHandler = new OSAConsoleScanWaitHandler();
        osaConsoleScanWaitHandler.setLogger(logger);
        logger.info("Waiting for OSA scan to finish");
        osaScanStatus = client.waitForOSAScanToFinish(osaScan.getScanId(), -1, osaConsoleScanWaitHandler);
        logger.info("OSA scan finished successfully");
        logger.info("Creating OSA reports");
        //retrieve OSA scan results
        osaSummaryResults = client.retrieveOSAScanSummaryResults(osaScan.getScanId());
        printOSAResultsToConsole(osaSummaryResults);

        //OSA json reports
        publishJson(OSA_SUMMARY_NAME, osaSummaryResults);
        List<Library> libraries = client.getOSALibraries(osaScan.getScanId());
        publishJson(OSA_LIBRARIES_NAME, libraries);
        List<CVE> osaVulnerabilities = client.getOSAVulnerabilities(osaScan.getScanId());
        publishJson(OSA_VULNERABILITIES_NAME, osaVulnerabilities);

        osaCVEJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(osaVulnerabilities);
        osaLibrariesJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(libraries);

        osaResultsReady = true;
    }

    private void printOSAResultsToConsole(OSASummaryResults osaSummaryResults) {
        logger.info("----------------------------Checkmarx Scan Results(CxOSA):-------------------------------");
        logger.info("");
        logger.info("------------------------");
        logger.info("Vulnerabilities Summary:");
        logger.info("------------------------");
        logger.info("OSA high severity results: " + osaSummaryResults.getTotalHighVulnerabilities());
        logger.info("OSA medium severity results: " + osaSummaryResults.getTotalMediumVulnerabilities());
        logger.info("OSA low severity results: " + osaSummaryResults.getTotalLowVulnerabilities());
        logger.info("Vulnerability score: " + osaSummaryResults.getVulnerabilityScore());
        logger.info("");
        logger.info("-----------------------");
        logger.info("Libraries Scan Results:");
        logger.info("-----------------------");
        logger.info("Open-source libraries: " + osaSummaryResults.getTotalLibraries());
        logger.info("Vulnerable and outdated: " + osaSummaryResults.getVulnerableAndOutdated());
        logger.info("Vulnerable and updated: " + osaSummaryResults.getVulnerableAndUpdated());
        logger.info("Non-vulnerable libraries: " + osaSummaryResults.getNonVulnerableLibraries());
        logger.info("");
        logger.info("OSA scan results location: " + osaProjectSummaryLink);
        logger.info("-----------------------------------------------------------------------------------------");
    }

    private CxXMLResults convertToXMLResult(byte[] cxReport) throws IOException, JAXBException {

        CxXMLResults reportObj = null;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cxReport);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CxXMLResults.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            reportObj = (CxXMLResults) unmarshaller.unmarshal(byteArrayInputStream);

        } finally {
            IOUtils.closeQuietly(byteArrayInputStream);
        }
        return reportObj;
    }

    private void publishJson(String name, Object jsonObj) throws IOException {
        String fileName = name + ".json";
        File jsonFile = new File(buildDirectory, fileName);
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);
        FileUtils.writeStringToFile(jsonFile, json);
        publishArtifact(jsonFile.getAbsolutePath());
        logger.info(name + " json location: " + buildDirectory + "/" + fileName);
    }

    private boolean assertVulnerabilities(ScanResults scanResults, OSASummaryResults osaSummaryResults, StringBuilder res) {

        boolean failByThreshold = false;
        if (config.isSASTThresholdEffectivelyEnabled() && scanResults != null) {
            failByThreshold = isFail(scanResults.getHighSeverityResults(), config.getHighThreshold(), res, "high", "CxSAST ");
            failByThreshold |= isFail(scanResults.getMediumSeverityResults(), config.getMediumThreshold(), res, "medium", "CxSAST ");
            failByThreshold |= isFail(scanResults.getLowSeverityResults(), config.getLowThreshold(), res, "low", "CxSAST ");
        }
        if (config.isOSAThresholdEffectivelyEnabled() && osaSummaryResults != null) {
            failByThreshold |= isFail(osaSummaryResults.getTotalHighVulnerabilities(), config.getOsaHighThreshold(), res, "high", "CxOSA ");
            failByThreshold |= isFail(osaSummaryResults.getTotalMediumVulnerabilities(), config.getOsaMediumThreshold(), res, "medium", "CxOSA ");
            failByThreshold |= isFail(osaSummaryResults.getTotalLowVulnerabilities(), config.getOsaLowThreshold(), res, "low", "CxOSA ");
        }
        return failByThreshold;
    }

    private boolean isFail(int result, Integer threshold, StringBuilder res, String severity, String severityType) {
        boolean fail = false;
        if (threshold != null && result > threshold) {
            res.append(severityType).append(severity).append(" severity results are above threshold. Results: ").append(result).append(". Threshold: ").append(threshold).append("\n");
            fail = true;
        }
        return fail;
    }

    private void printBuildFailure(StringBuilder res, boolean thresholdExceeded, Exception sastBuildFailException, Exception osaBuildFailException) {
        logger.error("*********************************************");
        logger.error(" The Build Failed for the Following Reasons: ");
        logger.error("*********************************************");

        if (sastBuildFailException != null) {
            agentRunningBuild.getBuildLogger().buildFailureDescription(sastBuildFailException.getMessage() + (sastBuildFailException.getCause() == null ? "" : sastBuildFailException.getCause().getMessage()));
        }
        if (osaBuildFailException != null) {
            agentRunningBuild.getBuildLogger().buildFailureDescription(osaBuildFailException.getMessage() + (osaBuildFailException.getCause() == null ? "" : osaBuildFailException.getCause().getMessage()));
        }
        if(thresholdExceeded) {
            agentRunningBuild.getBuildLogger().buildFailureDescription("Failure: threshold exceeded");
        }

        String[] lines = res.toString().split("\\n");
        for (String s : lines) {
            logger.error(s);
        }
        logger.error("-----------------------------------------------------------------------------------------\n");
        logger.error("");
    }

    private void deleteTempFiles() {

        try {
            String tempDir = System.getProperty("java.io.tmpdir");
            CxFileChecker.deleteFile(tempDir, TEMP_FILE_NAME_TO_ZIP);
        } catch (Exception e) {
            logger.warn("Failed to delete temp files: " + e.getMessage());
        }

    }

    private void closeClient(CxClientService client) {
        if (client != null) {
            try {
                client.close();
            } catch (Exception e) {
            }
        }
    }


    public void generateCxHTMLReport() {
        String html = getResultsTemplate();
        if (html == null) {
            return;
        }

        if (sastResultsReady) {

            //SAST: fill html with results
            html = html
                    .replace(SAST_RESULTS_READY, TRUE)
                    .replaceAll(HIGH_RESULTS, String.valueOf(scanResults.getHighSeverityResults()))
                    .replace(MEDIUM_RESULTS, String.valueOf(scanResults.getMediumSeverityResults()))
                    .replace(LOW_RESULTS, String.valueOf(scanResults.getLowSeverityResults()))
                    .replace(SAST_SUMMARY_RESULTS_LINK, String.valueOf(projectStateLink))
                    .replace(SAST_SCAN_RESULTS_LINK, String.valueOf(scanResultsUrl))
                    .replace(SAST_PDF_LINK, String.valueOf(sastPDFLink))
                    .replace(THRESHOLD_ENABLED, String.valueOf(config.isSASTThresholdEffectivelyEnabled()))
                    .replace(HIGH_THRESHOLD, String.valueOf(config.getHighThreshold()))
                    .replace(MEDIUM_THRESHOLD, String.valueOf(config.getMediumThreshold()))
                    .replace(LOW_THRESHOLD, String.valueOf(config.getLowThreshold()))
                    .replace(SCAN_START_DATE, String.valueOf(scanResults.getScanStart()))
                    .replace(SCAN_TIME, String.valueOf(scanResults.getScanTime()))
                    .replace(SCAN_FILES_SCANNED, String.valueOf(scanResults.getFilesScanned()))
                    .replace(SCAN_LOC_SCANNED, String.valueOf(scanResults.getLinesOfCodeScanned()))
                    .replace(SCAN_QUERY_LIST, String.valueOf(scanResults.getQueryList()));
        } else {

            //SAST: fill html with empty values
            html = html
                    .replace(SAST_RESULTS_READY, FALSE)
                    .replaceAll(HIGH_RESULTS, "0")
                    .replace(MEDIUM_RESULTS, "0")
                    .replace(LOW_RESULTS, "0")
                    .replace(SAST_SUMMARY_RESULTS_LINK, "")
                    .replace(SAST_SCAN_RESULTS_LINK, "")
                    .replace(SAST_PDF_LINK, "")
                    .replace(THRESHOLD_ENABLED, FALSE)
                    .replace(HIGH_THRESHOLD, "0")
                    .replace(MEDIUM_THRESHOLD, "0")
                    .replace(LOW_THRESHOLD, "0")
                    .replace(SCAN_START_DATE, "")
                    .replace(SCAN_TIME, "")
                    .replace(SCAN_FILES_SCANNED, "null")
                    .replace(SCAN_LOC_SCANNED, "null")
                    .replace(SCAN_QUERY_LIST, "null");
        }

        if (osaResultsReady) {
            //OSA: fill html with results
            html = html
                    .replace(OSA_ENABLED, TRUE)
                    .replace(OSA_HIGH_RESULTS, String.valueOf(osaSummaryResults.getTotalHighVulnerabilities()))
                    .replace(OSA_MEDIUM_RESULTS, String.valueOf(osaSummaryResults.getTotalMediumVulnerabilities()))
                    .replace(OSA_LOW_RESULTS, String.valueOf(osaSummaryResults.getTotalLowVulnerabilities()))
                    .replace(OSA_SUMMARY_RESULTS_LINK, String.valueOf(osaProjectSummaryLink))
                    .replace(OSA_THRESHOLD_ENABLED, String.valueOf(config.isOSAThresholdEffectivelyEnabled()))
                    .replace(OSA_HIGH_THRESHOLD, String.valueOf(config.getOsaHighThreshold()))
                    .replace(OSA_MEDIUM_THRESHOLD, String.valueOf(config.getOsaMediumThreshold()))
                    .replace(OSA_LOW_THRESHOLD, String.valueOf(config.getOsaLowThreshold()))
                    .replace(OSA_VULNERABLE_LIBRARIES, String.valueOf(osaSummaryResults.getHighVulnerabilityLibraries() + osaSummaryResults.getMediumVulnerabilityLibraries() + osaSummaryResults.getLowVulnerabilityLibraries()))
                    .replace(OSA_OK_LIBRARIES, String.valueOf(osaSummaryResults.getNonVulnerableLibraries()))
                    .replace(OSA_CVE_LIST, String.valueOf(osaCVEJson))
                    .replace(OSA_LIBRARIES, String.valueOf(osaLibrariesJson))
                    .replace(OSA_START_TIME, String.valueOf(osaScanStatus.getStartAnalyzeTime()))
                    .replace(OSA_END_TIME, String.valueOf(osaScanStatus.getEndAnalyzeTime()));

        } else {

            //SAST: fill html with empty values
            html = html
                    .replace(OSA_ENABLED, FALSE)
                    .replace(OSA_HIGH_RESULTS, "0")
                    .replace(OSA_MEDIUM_RESULTS, "0")
                    .replace(OSA_LOW_RESULTS, "0")
                    .replace(OSA_SUMMARY_RESULTS_LINK, "")
                    .replace(OSA_THRESHOLD_ENABLED, FALSE)
                    .replace(OSA_HIGH_THRESHOLD, "0")
                    .replace(OSA_MEDIUM_THRESHOLD, "0")
                    .replace(OSA_LOW_THRESHOLD, "0")
                    .replace(OSA_VULNERABLE_LIBRARIES, "0")
                    .replace(OSA_OK_LIBRARIES, "0")
                    .replace(OSA_CVE_LIST, "null")
                    .replace(OSA_LIBRARIES, "null")
                    .replace(OSA_START_TIME, "")
                    .replace(OSA_END_TIME, "");
        }


        File htmlFile = new File(buildDirectory, REPORT_HTML_NAME);
        try {
            FileUtils.writeStringToFile(htmlFile, html);
        } catch (IOException e) {
            logger.error("Failed to generate full html report: " + e.getMessage());
            return;
        }
        publishArtifact(htmlFile.getAbsolutePath());
    }

    private String getResultsTemplate() {
        String ret = null;
        InputStream resourceAsStream = CxBuildProcess.class.getResourceAsStream("/com/checkmarx/teamcity/agent/resultsTemplate.html");
        if (resourceAsStream != null) {
            try {
                ret = IOUtils.toString(resourceAsStream, Charset.defaultCharset().name());
            } catch (IOException e) {
                logger.warn("Failed to get results template: " + e.getMessage());
            } finally {
                IOUtils.closeQuietly(resourceAsStream);
            }
        }
        return ret;
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
