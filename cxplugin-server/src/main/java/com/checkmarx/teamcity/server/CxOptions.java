package com.checkmarx.teamcity.server;

import com.checkmarx.teamcity.common.CxAbortException;
import com.checkmarx.teamcity.common.CxConstants;
import com.checkmarx.teamcity.common.CxSelectOption;
import com.checkmarx.teamcity.common.CxWebService;
import org.jetbrains.annotations.NotNull;

import javax.xml.ws.WebServiceException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class CxOptions {
    private CxWebService cxWebService = null;
    private boolean cxWebServiceInitialized = false;
    private String serverUrlValue;
    private String userValue;
    private String passValue;
    private volatile String testConnectionMsg = "";

    @NotNull
    public String getDefaultServer() {
        return CxConstants.CXGLOBALSERVER;
    }

    @NotNull
    public String getServerUrl() {
        return CxConstants.CXSERVERURL;
    }

    @NotNull
    public String getUser() {
        return CxConstants.CXUSER;
    }

    @NotNull
    public String getPass() {
        return CxConstants.CXPASS;
    }

    @NotNull
    public String getTestConnectionMsg() {
        return testConnectionMsg;
    }

    @NotNull
    public String getProject() {
        return CxConstants.CXPROJECT;
    }

    @NotNull
    public String getPreset() {
        return CxConstants.CXPRESET;
    }

    @NotNull
    public String getConfiguration() {
        return CxConstants.CXCONFIGURATION;
    }

    @NotNull
    public String getTeam() {
        return CxConstants.CXTEAM;
    }

    @NotNull
    public String getExcludeFolders() {
        return CxConstants.CXEXCLUDEFOLDERS;
    }

    @NotNull
    public String getFilterPatterns() {
        return CxConstants.CXFILTERPATTERNS;
    }

    @NotNull
    public String getIncremental() {
        return CxConstants.CXINCREMENTAL;
    }

    @NotNull
    public String getPeriodicFullScans() {
        return CxConstants.CXPERIODICFULLSCANS;
    }

    @NotNull
    public String getNumberIncremental() {
        return CxConstants.CXNUMBERINCREMENTAL;
    }

    @NotNull
    public String getComment() {
        return CxConstants.CXCOMMENT;
    }

    @NotNull
    public String getThresholdEnable() {
        return CxConstants.CXTHRESHOLDENABLE;
    }

    @NotNull
    public String getThresholdHigh() {
        return CxConstants.CXTHRESHOLDHIGH;
    }

    @NotNull
    public String getThresholdMedium() {
        return CxConstants.CXTHRESHOLDMEDIUM;
    }

    @NotNull
    public String getThresholdLow() {
        return CxConstants.CXTHRESHOLDLOW;
    }

    @NotNull
    public String getGeneratePdf() {
        return CxConstants.CXGENERATEPDF;
    }

    public void setServerUrlValue(final String serverUrlValue) {
        this.serverUrlValue = serverUrlValue;
    }
    public void setUserValue(final String userValue) {
        this.userValue = userValue;
    }
    public void setPassValue(final String passValue) {
        this.passValue = passValue;
    }

    private boolean initWebService() {
        if (this.cxWebServiceInitialized) {
            return true;
        }

        if (this.serverUrlValue == null || this.serverUrlValue.isEmpty() ||
                this.userValue == null || this.userValue.isEmpty() ||
                this.passValue == null || this.passValue.isEmpty()) {
            return false;
        }

        try {
            this.cxWebService = new CxWebService(this.serverUrlValue);
            this.cxWebService.login(this.userValue, this.passValue);
            this.cxWebServiceInitialized = true;
        } catch (CxAbortException | MalformedURLException | WebServiceException | ConnectException e) {
            System.out.println("Unable to initialize web service: '" + e.getMessage() + "'");
            return false;
        }

        return true;
    }

    @NotNull
    public String testConnection() {
        testConnectionMsg = "";
        try {
            this.cxWebService = new CxWebService(this.serverUrlValue);
            this.cxWebService.login(this.userValue, this.passValue);
            this.cxWebServiceInitialized = true;
            testConnectionMsg ="Connection successful";
        } catch (CxAbortException | MalformedURLException | WebServiceException | ConnectException e) {
            System.out.println("Unable to initialize web service: '" + e.getMessage() + "'");
            testConnectionMsg = e.getMessage();
        }
        return testConnectionMsg;
    }

    @NotNull
    public List<CxSelectOption> getPresetList() {
        if (initWebService()) {
            try {
                return this.cxWebService.getPresetsSelectList();
            } catch (CxAbortException | WebServiceException e) {
                System.out.println("Unable to retrieve preset list: '" + e.getMessage() + "'");
            }
        }

        List<CxSelectOption> list = new ArrayList<>();
        list.add(new CxSelectOption("0", "Provide Checkmarx server credentials to see preset list"));
        return list;
    }

    @NotNull
    public List<CxSelectOption> getConfigurationList() {
        if (initWebService()) {
            try {
                return this.cxWebService.getConfigurationSetsSelectList();
            } catch (CxAbortException | WebServiceException e) {
                System.out.println("Unable to retrieve configuration list: '" + e.getMessage() + "'");
            }
        }

        List<CxSelectOption> list = new ArrayList<>();
        list.add(new CxSelectOption("0", "Provide Checkmarx server credentials to see configuration list"));
        return list;
    }

    @NotNull
    public List<CxSelectOption> getTeamList() {
        if (initWebService()) {
            try {
                return this.cxWebService.getAssociatedGroupsSelectList();
            } catch (CxAbortException | WebServiceException e) {
                System.out.println("Unable to retrieve team list: '" + e.getMessage() + "'");
            }
        }

        List<CxSelectOption> list = new ArrayList<>();
        list.add(new CxSelectOption("0", "Provide Checkmarx server credentials to see team list"));
        return list;
    }

    @NotNull
    public String getNoDisplay() {
        return "style='display:none'";
    }
}
