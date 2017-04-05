package com.checkmarx.teamcity.common;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.ws.WebServiceException;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.io.IOUtils;

import jetbrains.buildServer.agent.BuildProgressLogger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.checkmarx.jenkins.xmlresponseparser.*;
import com.checkmarx.CxWsResolver.CxWSResolver;
import com.checkmarx.CxWsResolver.CxWSResolverSoap;
import com.checkmarx.CxWsResolver.CxWSResponseDiscovery;
import com.checkmarx.CxWsResolver.CxClientType;
import com.checkmarx.CxJenkinsWebService.*;


/**
 * CxWebService encapsulates the web service communication
 *
 * It is largely copied from the Jenkins plugin
 */

public class CxWebService {
    private static final int LCID = 1033; // English
    private static final int SERVER_CALL_RETRY_NUMBER = 5;
    private static final int MILISEOUNDS_IN_HOUR = 1000 * 60 * 60;
    private static final String RESOLVER_PATH = "/cxwebinterface/CxWsResolver.asmx";
    private static final int WEBSERVICE_API_VERSION = 1;

    private final CxJenkinsWebServiceSoap cxJenkinsWebServiceSoap;
    private final URL webServiceUrl;
    private String sessionId;

    public CxWebService(@NotNull final String url) throws MalformedURLException, CxAbortException {
        CxUrlValidation.validate(url);

        final URL resolverUrl = new URL(url + RESOLVER_PATH);
        final CxWSResolver cxWSResolver = new CxWSResolver(resolverUrl);
        final CxWSResolverSoap cxWSResolverSoap = cxWSResolver.getCxWSResolverSoap();

        final CxWSResponseDiscovery cxWSResponseDiscovery = cxWSResolverSoap.getWebServiceUrl(CxClientType.JENKINS, WEBSERVICE_API_VERSION);
        if (!cxWSResponseDiscovery.isIsSuccesfull()) {
            throw new CxAbortException("Failed to resolve Checkmarx webservice url: " + cxWSResponseDiscovery.getErrorMessage());
        }

        this.webServiceUrl = new URL(cxWSResponseDiscovery.getServiceURL());
        CxJenkinsWebService cxJenkinsWebService = new CxJenkinsWebService(this.webServiceUrl);
        this.cxJenkinsWebServiceSoap = cxJenkinsWebService.getCxJenkinsWebServiceSoap();
    }

    public void login(@Nullable final String user, @Nullable final String pass) throws CxAbortException, ConnectException {
        this.sessionId = null;

        Credentials credentials = new Credentials();
        credentials.setUser(user);
        credentials.setPass(pass);

        CxWSResponseLoginData cxWSResponseLoginData = this.cxJenkinsWebServiceSoap.login(credentials, LCID);
        if (!cxWSResponseLoginData.isIsSuccesfull()) {
            throw new CxAbortException(cxWSResponseLoginData.getErrorMessage());
        }

        this.sessionId = cxWSResponseLoginData.getSessionId();
    }

    private CxWSResponseScanStatus getScanStatus(final CxWSResponseRunID cxWSResponseRunID) throws CxAbortException {
        assert this.sessionId != null : "Trying to get scan status before login";
        CxWSResponseScanStatus cxWSResponseScanStatus = this.cxJenkinsWebServiceSoap.getStatusOfSingleScan(this.sessionId,
                cxWSResponseRunID.getRunId());
        if (!cxWSResponseScanStatus.isIsSuccesfull()) {
            String message = "Error received from Checkmarx server: " + cxWSResponseScanStatus.getErrorMessage();
            throw new CxAbortException(message);
        }
        return cxWSResponseScanStatus;
    }

    /**
     * Simple convenience method to avoid spamming the console with duplicate messages.
     * This method will only log a new message if it does not equal the previous message.
     * This method is not responsible for tracking the messages, and expects them to
     * be passed in as parameters.
     *
     * @param prevMsg - The message to compare the new message against.
     * @param newMsg - The message to log, if new
     * @return - When new, the message that was just logged.  Otherwise, the single message is returned (new=old)
     *
     */

    private String cleanLogger(final String prevMsg, final String newMsg, final BuildProgressLogger logger) {
        //only log if new != old
        if (!newMsg.equals(prevMsg)) {
            logger.message(newMsg);
        }

        //if new, return the message logged
        //otherwise, just returns the msg (because new=old)
        return newMsg;
    }

    public long trackScanProgress(final CxWSResponseRunID cxWSResponseRunID, final String username,
                                  final String password, final boolean scanTimeOutEnabled,
                                  final long scanTimeoutDuration, final BuildProgressLogger logger)
            throws CxAbortException, InterruptedException, ConnectException {
        assert this.sessionId != null : "Trying to track scan progress before login";

        final long jobStartTime = System.currentTimeMillis();
        int retryAttempts = SERVER_CALL_RETRY_NUMBER;

        boolean locReported = false;
        String previousMessage = "";
        while (true) {
            String newMessage = "";
            try {
                Thread.sleep(10L * 1000);

                if (scanTimeOutEnabled
                        && jobStartTime + scanTimeoutDuration * MILISEOUNDS_IN_HOUR < System.currentTimeMillis()) {
                    logger.message("Scan duration exceeded timeout threshold");
                    return 0;
                }

                CxWSResponseScanStatus status = this.getScanStatus(cxWSResponseRunID);

                switch (status.getCurrentStatus()) {
                    // In progress states
                    case WAITING_TO_PROCESS:
                        newMessage = "Scan job waiting for processing";
                        previousMessage = cleanLogger(previousMessage, newMessage, logger);
                        break;

                    case QUEUED:
                        if (!locReported) {
                            logger.message("Source contains: " + status.getLOC() + " lines of code.");
                            locReported = true;
                        }
                        newMessage = "Scan job queued at position: " + status.getQueuePosition();
                        previousMessage = cleanLogger(previousMessage, newMessage, logger);
                        break;

                    case UNZIPPING:
                        logger.message("Unzipping: " + status.getCurrentStagePercent() + "% finished");
                        logger.message("LOC: " + status.getLOC());
                        logger.message("StageMessage: " + status.getStageMessage());
                        logger.message("StepMessage: " + status.getStepMessage());
                        logger.message("StepDetails: " + status.getStepDetails());
                        break;

                    case WORKING:
                        newMessage = "Scanning: " + status.getStageMessage() + " " + status.getStepDetails()
                                + " (Current stage progress: " + status.getCurrentStagePercent() + "%, Total progress: "
                                + status.getTotalPercent() + "%)";
                        previousMessage = cleanLogger(previousMessage, newMessage, logger);
                        break;

                    // End of progress states
                    case FINISHED:
                        logger.message("Scan Finished Successfully -  RunID: " + status.getRunId() + " ScanID:"
                                + status.getScanId());
                        return status.getScanId();

                    case CANCELED:
                        throw new InterruptedException("Scan canceled on server");

                    case FAILED:
                    case DELETED:
                    case UNKNOWN:
                        String message = "Scan " + status.getStageName() + " -  RunID: " + status.getRunId() + " ScanID: "
                                + status.getScanId() + " Server scan status: " + status.getStageMessage();
                        logger.message(message);
                        throw new CxAbortException(message);
                }
            } catch (CxAbortException | WebServiceException e) {
                if (e.getMessage().contains("Unauthorized") || e.getMessage().contains("ReConnect")) {
                    RestoreSession(username, password, logger);
                } else if (retryAttempts > 0) {
                    retryAttempts--;
                } else {
                    throw e;
                }
            }
        }
    }

    private void RestoreSession(final String username, final String password, final BuildProgressLogger logger)
            throws CxAbortException, ConnectException {
        logger.message("Session was rejected by the Checkmarx server, trying to re-login");
        this.login(username, password);
    }

    public CxWSCreateReportResponse generateScanReport(final long scanId, final CxWSReportType reportType,
                                                       final BuildProgressLogger logger) throws CxAbortException {
        assert this.sessionId != null : "Trying to retrieve scan report before login";

        CxWSReportRequest cxWSReportRequest = new CxWSReportRequest();
        cxWSReportRequest.setScanID(scanId);
        cxWSReportRequest.setType(reportType);
        logger.message("Requesting " + reportType.toString().toUpperCase() + " Scan Report Generation");

        int retryAttempts = SERVER_CALL_RETRY_NUMBER;
        CxWSCreateReportResponse cxWSCreateReportResponse;
        do {
            cxWSCreateReportResponse = this.cxJenkinsWebServiceSoap.createScanReport(sessionId, cxWSReportRequest);
            if (!cxWSCreateReportResponse.isIsSuccesfull()) {
                retryAttempts--;
                logger.warning("Error requesting scan report generation: " + cxWSCreateReportResponse.getErrorMessage());
            }
        } while (!cxWSCreateReportResponse.isIsSuccesfull() && retryAttempts > 0);

        if (!cxWSCreateReportResponse.isIsSuccesfull()) {
            String message = "Error requesting scan report generation: " + cxWSCreateReportResponse.getErrorMessage();
            logger.error(message);
            throw new CxAbortException(message);
        }

        return cxWSCreateReportResponse;
    }

    public void retrieveScanReport(final long reportId, File reportFile, final CxWSReportType reportType,
                                   final BuildProgressLogger logger) throws CxAbortException, InterruptedException {
        // Wait for the report to become ready
        String previousMessage = "";
        while (true) {
            CxWSReportStatusResponse cxWSReportStatusResponse = this.cxJenkinsWebServiceSoap.getScanReportStatus(
                    this.sessionId, reportId);
            if (!cxWSReportStatusResponse.isIsSuccesfull()) {
                String message = "Error retrieving scan report status: " + cxWSReportStatusResponse.getErrorMessage();
                logger.error(message);
                throw new CxAbortException(message);
            }
            if (cxWSReportStatusResponse.isIsFailed()) {
                String message = "Failed to create scan report";
                logger.error("Web method getScanReportStatus returned status response with isFailed field set to true");
                logger.error(message);
                throw new CxAbortException(message);
            }

            if (cxWSReportStatusResponse.isIsReady()) {
                logger.message("Scan report generated on Checkmarx server");
                break;
            }

            previousMessage = cleanLogger(previousMessage, reportType.toString().toUpperCase() + " Report generation in progress", logger);

            Thread.sleep(5L * 1000);
        }

        CxWSResponseScanResults cxWSResponseScanResults = this.cxJenkinsWebServiceSoap.getScanReport(this.sessionId, reportId);
        if (!cxWSResponseScanResults.isIsSuccesfull()) {
            String message = "Error retrieving scan report: " + cxWSResponseScanResults.getErrorMessage();
            logger.error(message);
            throw new CxAbortException(message);
        }

        // Save results on disk
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(reportFile);
            IOUtils.write(cxWSResponseScanResults.getScanResults(), fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            String message = "Can't create report file: " + reportFile.getAbsolutePath();
            logger.error(message);
            throw new CxAbortException(message);
        }
        logger.message("Scan report written to: " + reportFile.getAbsolutePath());
    }

    public List<ProjectDisplayData> getProjectsDisplayData() throws CxAbortException {
        assert this.sessionId != null : "Trying to retrieve projects display data before login";

        CxWSResponseProjectsDisplayData cxWSResponseProjectsDisplayData = this.cxJenkinsWebServiceSoap
                .getProjectsDisplayData(this.sessionId);
        if (!cxWSResponseProjectsDisplayData.isIsSuccesfull()) {
            String message = "Error retrieving projects display data from server: "
                    + cxWSResponseProjectsDisplayData.getErrorMessage();
            throw new CxAbortException(message);
        }
        return cxWSResponseProjectsDisplayData.getProjectList().getProjectDisplayData();
    }

    public List<Preset> getPresets() throws CxAbortException {
        assert this.sessionId != null : "Trying to retrieve presets before login";

        CxWSResponsePresetList cxWSResponsePresetList = this.cxJenkinsWebServiceSoap.getPresetList(this.sessionId);
        if (!cxWSResponsePresetList.isIsSuccesfull()) {
            String message = "Error retrieving presets from server: " + cxWSResponsePresetList.getErrorMessage();
            throw new CxAbortException(message);
        }
        return cxWSResponsePresetList.getPresetList().getPreset();
    }

    public List<CxSelectOption> getPresetsSelectList() throws CxAbortException {
        List<CxSelectOption> selectList = new ArrayList<CxSelectOption>();
        for (Preset preset : getPresets()) {
            selectList.add(new CxSelectOption(Long.toString(preset.getID()), preset.getPresetName()));
        }
        return selectList;
    }

    public List<ConfigurationSet> getConfigurationSets() throws CxAbortException {
        assert this.sessionId != null : "Trying to retrieve configurations before login";

        CxWSResponseConfigSetList cxWSResponseConfigSetList = this.cxJenkinsWebServiceSoap.getConfigurationSetList(this.sessionId);
        if (!cxWSResponseConfigSetList.isIsSuccesfull()) {
            String message = "Error retrieving configurations from server: " + cxWSResponseConfigSetList.getErrorMessage();
            throw new CxAbortException(message);
        }
        return cxWSResponseConfigSetList.getConfigSetList().getConfigurationSet();
    }

    public List<CxSelectOption> getConfigurationSetsSelectList() throws CxAbortException {
        List<CxSelectOption> selectList = new ArrayList<CxSelectOption>();
        for (ConfigurationSet configSet : getConfigurationSets()) {
            selectList.add(new CxSelectOption(Long.toString(configSet.getID()), configSet.getConfigSetName()));
        }
        return selectList;
    }

    public CxWSBasicRepsonse validateProjectName(final String cxProjectName, final String groupId) {
        assert this.sessionId != null : "Trying to validate project name before login";
        return this.cxJenkinsWebServiceSoap.isValidProjectName(this.sessionId, cxProjectName, groupId);
    }

    private Pair<byte[], byte[]> createScanSoapMessage(final Object request, final Class inputType,
                                                       final ProjectSettings projectSettings,
                                                       final LocalCodeContainer localCodeContainer,
                                                       final boolean visibleToOtherUsers, final boolean isPublicScan,
                                                       final BuildProgressLogger logger) throws RuntimeException {
        final String soapMessageHead = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
                + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" + "  <soap:Body>";

        final String soapMessageTail = "</soap:Body></soap:Envelope>";
        final String zippedFileOpenTag = "<ZippedFile>";
        final String zippedFileCloseTag = "</ZippedFile>";

        try {
            final JAXBContext context = JAXBContext.newInstance(inputType);
            final Marshaller marshaller = context.createMarshaller();

            StringWriter scanMessage = new StringWriter();
            scanMessage.write(soapMessageHead);

            // Nullify the zippedFile field, and save its old value for
            // restoring later
            final byte[] oldZippedFileValue = localCodeContainer.getZippedFile();
            localCodeContainer.setZippedFile(new byte[] {});

            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            marshaller.marshal(request, scanMessage);
            localCodeContainer.setZippedFile(oldZippedFileValue); // Restore the old value

            scanMessage.write(soapMessageTail);
            // Here we split the message around <ZippedFile></ZippedFile>
            // substring. We know that the opening
            // and closing tag are adjacent because the zippedFile property was
            // set to empty byte array
            final String[] parts = scanMessage.toString().split(zippedFileOpenTag + zippedFileCloseTag);
            assert parts.length == 2;
            final String startPart = parts[0] + zippedFileOpenTag;
            final String endPart = zippedFileCloseTag + parts[1];

            return Pair.of(startPart.getBytes("UTF-8"), endPart.getBytes("UTF-8"));
        } catch (JAXBException | UnsupportedEncodingException e) {
            // Getting here indicates a bug
            logger.error(e.getMessage());
            throw new RuntimeException("Eror creating SOAP message", e);
        }
    }

    public List<Group> getAssociatedGroups() throws CxAbortException {
        assert this.sessionId != null : "Trying to retrieve teams before login";

        CxWSResponseGroupList associatedGroupsList = this.cxJenkinsWebServiceSoap.getAssociatedGroupsList(this.sessionId);
        if (!associatedGroupsList.isIsSuccesfull()) {
            String message = "Error retrieving associated groups (teams) from server: "
                    + associatedGroupsList.getErrorMessage();
            throw new CxAbortException(message);
        }
        return associatedGroupsList.getGroupList().getGroup();
    }

    public List<CxSelectOption> getAssociatedGroupsSelectList() throws CxAbortException {
        List<CxSelectOption> selectList = new ArrayList<CxSelectOption>();
        for (Group group : getAssociatedGroups()) {
            selectList.add(new CxSelectOption(group.getID(), group.getGroupName()));
        }
        return selectList;
    }

    public CxWSResponseRunID runScanAndAddToProject(final ProjectSettings projectSettings,
                                                    final LocalCodeContainer localCodeContainer,
                                                    final boolean visibleToOtherUsers, final boolean isPublicScan,
                                                    final String base64ZipFile, final String comment,
                                                    final BuildProgressLogger logger) throws CxAbortException {
        assert this.sessionId != null;

        RunScanAndAddToProject scan = new RunScanAndAddToProject();
        scan.setLocalCodeContainer(localCodeContainer);
        scan.setSessionId(this.sessionId);
        scan.setProjectSettings(projectSettings);
        scan.setVisibleToUtherUsers(visibleToOtherUsers);
        scan.setIsPublicScan(isPublicScan);
        scan.setComment(comment);

        Pair<byte[], byte[]> soapMeassage = createScanSoapMessage(scan, RunScanAndAddToProject.class, projectSettings,
                localCodeContainer, visibleToOtherUsers, isPublicScan, logger);

        return scan(localCodeContainer, visibleToOtherUsers, isPublicScan, base64ZipFile, "RunScanAndAddToProject",
                soapMeassage, new RunScanAndAddToProjectXmlResponseParser(), logger);
    }

    public CxWSResponseRunID runIncrementalScan(final ProjectSettings projectSettings,
                                                final LocalCodeContainer localCodeContainer,
                                                final boolean visibleToOtherUsers, final boolean isPublicScan,
                                                final String base64ZipFile, final String comment,
                                                final BuildProgressLogger logger) throws CxAbortException {
        assert this.sessionId != null;

        RunIncrementalScan scan = new RunIncrementalScan();
        scan.setLocalCodeContainer(localCodeContainer);
        scan.setSessionId(this.sessionId);
        scan.setProjectSettings(projectSettings);
        scan.setVisibleToUtherUsers(visibleToOtherUsers);
        scan.setIsPublicScan(isPublicScan);
        scan.setComment(comment);

        Pair<byte[], byte[]> soapMeassage = createScanSoapMessage(scan, RunIncrementalScan.class, projectSettings,
                localCodeContainer, visibleToOtherUsers, isPublicScan, logger);

        return scan(localCodeContainer, visibleToOtherUsers, isPublicScan, base64ZipFile, "RunIncrementalScan",
                soapMeassage, new RunIncrementalScanXmlResponseParser(), logger);
    }

    public CxWSResponseRunID createAndRunProject(final ProjectSettings projectSettings,
                                                 final LocalCodeContainer localCodeContainer, final boolean visibleToOtherUsers, final boolean isPublicScan,
                                                 final String base64ZipFile, final String comment,
                                                 final BuildProgressLogger logger) throws CxAbortException {
        assert this.sessionId != null;

        CreateAndRunProject scan = new CreateAndRunProject();
        scan.setLocalCodeContainer(localCodeContainer);
        scan.setSessionID(this.sessionId);
        scan.setProjectSettings(projectSettings);
        scan.setVisibleToOtherUsers(visibleToOtherUsers);
        scan.setIsPublicScan(isPublicScan);
        scan.setComment(comment);

        Pair<byte[], byte[]> soapMessage = createScanSoapMessage(scan, CreateAndRunProject.class, projectSettings,
                localCodeContainer, visibleToOtherUsers, isPublicScan, logger);

        return scan(localCodeContainer, visibleToOtherUsers, isPublicScan, base64ZipFile, "CreateAndRunProject",
                soapMessage, new CreateAndRunProjectXmlResponseParser(), logger);
    }

    public long getProjectId(final ProjectSettings projectSettings, final BuildProgressLogger logger)
            throws CxAbortException {
        CxWSResponseProjectsDisplayData projects = this.cxJenkinsWebServiceSoap.getProjectsDisplayData(this.sessionId);

        final String groupId = projectSettings.getAssociatedGroupID();
        List<Group> selected = new ArrayList<>();
        for (final Group group : getAssociatedGroups()) {
            if (group.getID().equals(groupId)) {
                selected.add(group);
            }
        }

        if (selected.isEmpty()) {
            final String message = "Could not translate group (team) id: " + groupId + " to group name. "
                    + "Open the Job configuration page, and select a team.";
            logger.error(message);
            throw new CxAbortException(message);
        } else if (selected.size() > 1) {
            logger.warning("Server returned more than one group with id: " + groupId);
            for (final Group g : selected) {
                logger.warning("Group Id: " + g.getID() + " groupName: " + g.getGroupName());
            }
        }

        long projectId = 0;
        if (projects != null && projects.isIsSuccesfull()) {
            for (ProjectDisplayData projectDisplayData : projects.getProjectList().getProjectDisplayData()) {
                if (projectDisplayData.getProjectName().equals(projectSettings.getProjectName())
                        && projectDisplayData.getGroup().equals(selected.get(0).getGroupName())) {
                    projectId = projectDisplayData.getProjectID();
                    break;
                }
            }
        }

        if (projectId == 0) {
            throw new CxAbortException("Can't find existing project to scan");
        }

        return projectId;
    }

    private CxWSResponseRunID scan(final LocalCodeContainer localCodeContainer, final boolean visibleToOtherUsers,
                                   final boolean isPublicScan, final String base64ZipFile, final String soapActionName,
                                   final Pair<byte[], byte[]> soapMessage, final XmlResponseParser xmlResponseParser,
                                   final BuildProgressLogger logger) throws CxAbortException {
        assert this.sessionId != null;

        int retryAttemptsLeft = SERVER_CALL_RETRY_NUMBER;
        while (true) {
            try {
                return sendScanRequest(base64ZipFile, soapActionName, soapMessage, xmlResponseParser, logger);
            } catch (CxAbortException e) {
                if (retryAttemptsLeft > 0) {
                    retryAttemptsLeft--;
                } else {
                    throw e;
                }
            }
        }
    }

    private CxWSResponseRunID sendScanRequest(final String base64ZipFile, final String soapActionName,
                                              final Pair<byte[], byte[]> soapMessage,
                                              final XmlResponseParser xmlResponseParser,
                                              final BuildProgressLogger logger) throws CxAbortException {
        try {
            // Create HTTP connection
            final HttpURLConnection streamingUrlConnection = (HttpURLConnection) this.webServiceUrl.openConnection();
            streamingUrlConnection.addRequestProperty("Content-Type", "text/xml; charset=utf-8");
            streamingUrlConnection.addRequestProperty("SOAPAction",
                    String.format("\"http://Checkmarx.com/v7/%s\"", soapActionName));
            streamingUrlConnection.setDoOutput(true);

            // Calculate the length of the soap message
            File file = new File(base64ZipFile);
            final long length = soapMessage.getLeft().length + soapMessage.getRight().length + file.length();
            streamingUrlConnection.setFixedLengthStreamingMode((int) length);
            streamingUrlConnection.connect();
            final OutputStream os = streamingUrlConnection.getOutputStream();

            logger.message("Uploading sources to Checkmarx server");
            logger.message("Size = " + length + " bytes");
            os.write(soapMessage.getLeft());

            final InputStream fis = new FileInputStream(file);
            IOUtils.copyLarge(fis, os);

            os.write(soapMessage.getRight());
            os.close();
            fis.close();
            logger.message("Finished uploading sources to Checkmarx server");

            CxWSResponseRunID cxWSResponseRunID = xmlResponseParser.parse(streamingUrlConnection.getInputStream());

            if (!cxWSResponseRunID.isIsSuccesfull()) {
                String message = "Submission of sources for scan failed: " + cxWSResponseRunID.getErrorMessage();
                throw new CxAbortException(message);
            }

            return cxWSResponseRunID;
        } catch (HttpRetryException e) {
            String consoleMessage = "Checkmarx plugin for Bamboo does not support Single sign-on authentication."
                    + "\nPlease, configure Checkmarx server to work in Anonymous authentication mode.";
            logger.error(consoleMessage);
            throw new CxAbortException(e.getMessage());
        } catch (IOException | JAXBException | XMLStreamException e) {
            logger.error(e.getMessage());
            throw new CxAbortException(e.getMessage());
        }
    }

    public CxWSBasicRepsonse cancelScan(final String runId) {
        return this.cxJenkinsWebServiceSoap.cancelScan(this.sessionId, runId);
    }

    public CxWSBasicRepsonse cancelScanReport(final long reportId) {
        return this.cxJenkinsWebServiceSoap.cancelScanReport(this.sessionId, reportId);
    }
}
