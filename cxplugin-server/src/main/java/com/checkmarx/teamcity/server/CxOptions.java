package com.checkmarx.teamcity.server;

import com.checkmarx.teamcity.common.CxConstants;
import com.checkmarx.teamcity.common.CxSelectOption;
import com.checkmarx.teamcity.common.client.CxClientService;
import com.checkmarx.teamcity.common.client.CxClientServiceImpl;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.crypt.EncryptUtil;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Collections;
import java.util.List;

import static com.checkmarx.teamcity.common.CxParam.*;


public class CxOptions {

    private List<CxSelectOption> teamList = Collections.singletonList(new CxSelectOption("0", CxConstants.NO_TEAM_MESSAGE));
    private List<CxSelectOption> presetList = Collections.singletonList(new CxSelectOption("0", CxConstants.NO_PRESET_MESSAGE));


    @NotNull
    public String getUseDefaultServer() {
        return USE_DEFAULT_SERVER;
    }

    @NotNull
    public String getUseDefaultSastConfig() {
        return USE_DEFAULT_SAST_CONFIG;
    }

    @NotNull
    public String getUseDefaultScanControl() {
        return USE_DEFAULT_SCAN_CONTROL;
    }

    @NotNull
    public String getServerUrl() {
        return SERVER_URL;
    }

    @NotNull
    public String getUsername() {
        return USERNAME;
    }

    @NotNull
    public String getPassword() {
        return PASSWORD;
    }

    @NotNull
    public String getProjectName() {
        return PROJECT_NAME;
    }

    @NotNull
    public String getPresetId() {
        return PRESET_ID;
    }

    @NotNull
    public List<CxSelectOption> getPresetList() {
        return presetList;
    }


    @NotNull
    public String getTeamId() {
        return TEAM_ID;
    }

    @NotNull
    public List<CxSelectOption> getTeamList() {
        return teamList;
    }

    @NotNull
    public String getExcludeFolders() {
        return EXCLUDE_FOLDERS;
    }

    @NotNull
    public String getFilterPatterns() {
        return FILTER_PATTERNS;
    }

    @NotNull
    public String getScanComment() {
        return SCAN_COMMENT;
    }

    @NotNull
    public String getScanTimeoutInMinutes() {
        return SCAN_TIMEOUT_IN_MINUTES;
    }


    @NotNull
    public String getIsIncremental() {
        return IS_INCREMENTAL;
    }

    @NotNull
    public String getGeneratePDFReport() {
        return GENERATE_PDF_REPORT;
    }

    @NotNull
    public String getOsaEnabled() {
        return OSA_ENABLED;
    }

    @NotNull
    public String getOsaFilterPatterns() {
        return OSA_FILTER_PATTERNS;
    }

    @NotNull
    public String getOsaArchiveIncludePatterns() {
        return OSA_ARCHIVE_INCLUDE_PATTERNS;
    }

    @NotNull
    public String getOsaInstallBeforeScan() {
        return OSA_INSTALL_BEFORE_SCAN;
    }

    @NotNull
    public String getIsSynchronous() {
        return IS_SYNCHRONOUS;
    }

    @NotNull
    public String getThresholdEnabled() {
        return THRESHOLD_ENABLED;
    }

    @NotNull
    public String getHighThreshold() {
        return HIGH_THRESHOLD;
    }

    @NotNull
    public String getMediumThreshold() {
        return MEDIUM_THRESHOLD;
    }

    @NotNull
    public String getLowThreshold() {
        return LOW_THRESHOLD;
    }

    @NotNull
    public String getOsaThresholdEnabled() {
        return OSA_THRESHOLD_ENABLED;
    }

    @NotNull
    public String getOsaHighThreshold() {
        return OSA_HIGH_THRESHOLD;
    }

    @NotNull
    public String getOsaMediumThreshold() {
        return OSA_MEDIUM_THRESHOLD;
    }

    @NotNull
    public String getOsaLowThreshold() {
        return OSA_LOW_THRESHOLD;
    }

    @NotNull
    public String getGlobalServerUrl() {
        return GLOBAL_SERVER_URL;
    }

    @NotNull
    public String getGlobalUsername() {
        return GLOBAL_USERNAME;
    }

    @NotNull
    public String getGlobalExcludeFolders() {
        return GLOBAL_EXCLUDE_FOLDERS;
    }

    @NotNull
    public String getGlobalFilterPatterns() {
        return GLOBAL_FILTER_PATTERNS;
    }

    @NotNull
    public String getGlobalScanTimeoutInMinutes() {
        return GLOBAL_SCAN_TIMEOUT_IN_MINUTES;
    }

    @NotNull
    public String getGlobalIsSynchronous() {
        return GLOBAL_IS_SYNCHRONOUS;
    }

    @NotNull
    public String getGlobalThresholdEnabled() {
        return GLOBAL_THRESHOLD_ENABLED;
    }

    @NotNull
    public String getGlobalHighThreshold() {
        return GLOBAL_HIGH_THRESHOLD;
    }

    @NotNull
    public String getGlobalMediumThreshold() {
        return GLOBAL_MEDIUM_THRESHOLD;
    }

    @NotNull
    public String getGlobalLowThreshold() {
        return GLOBAL_LOW_THRESHOLD;
    }

    @NotNull
    public String getGlobalOsaThresholdEnabled() {
        return GLOBAL_OSA_THRESHOLD_ENABLED;
    }

    @NotNull
    public String getGlobalOsaHighThreshold() {
        return GLOBAL_OSA_HIGH_THRESHOLD;
    }

    @NotNull
    public String getGlobalOsaMediumThreshold() {
        return GLOBAL_OSA_MEDIUM_THRESHOLD;
    }

    @NotNull
    public String getGlobalOsaLowThreshold() {
        return GLOBAL_OSA_LOW_THRESHOLD;
    }


    public void testConnection(String serverUrl, String username, String password) {

        try {
            if (EncryptUtil.isScrambled(password)) {
                password = EncryptUtil.unscramble(password);
            }
            CxClientService client = new CxClientServiceImpl(new URL(serverUrl), username, password);
            client.loginToServer();
            presetList = client.getPresetListForSelect();
            teamList = client.getTeamListForSelect();

        } catch (Exception e) {
            Loggers.SERVER.error("Failed to retrieve preset and teams from server: " + e.getMessage());

        }
    }

    @NotNull
    public String getNoDisplay() {
        return "style='display:none'";
    }
}
