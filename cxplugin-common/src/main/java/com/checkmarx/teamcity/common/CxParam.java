package com.checkmarx.teamcity.common;

/**
 * Created by: Dorg.
 * Date: 27/04/2017.
 */
public abstract class CxParam {

    public static final String USE_DEFAULT_SERVER = "cxUseDefaultServer";
    public static final String USE_DEFAULT_SAST_CONFIG = "cxUseDefaultSastConfig";
    public static final String USE_DEFAULT_SCAN_CONTROL = "cxUseDefaultScanControl";

    public static final String SERVER_URL = "cxServerUrl";
    public static final String USERNAME = "cxUsername";
    public static final String PASSWORD = "cxPassword";
    public static final String PROJECT_NAME = "cxProjectName";
    public static final String PRESET_ID = "cxPresetId";
    public static final String TEAM_ID = "cxTeamId";
    public static final String EXCLUDE_FOLDERS = "cxExcludeFolders";
    public static final String FILTER_PATTERNS = "cxFilterPatterns";
    public static final String SCAN_TIMEOUT_IN_MINUTES = "cxScanTimeoutInMinutes";
    public static final String SCAN_COMMENT = "cxScanComment";
    public static final String IS_INCREMENTAL = "cxIsIncremental";
    public static final String GENERATE_PDF_REPORT = "cxGeneratePDFReport";
    public static final String OSA_ENABLED = "cxOsaEnabled";
    public static final String OSA_FILTER_PATTERNS = "cxOsaFilterPatterns";
    public static final String OSA_ARCHIVE_INCLUDE_PATTERNS = "cxOsaArchiveIncludePatterns";
    public static final String OSA_INSTALL_BEFORE_SCAN = "cxOsaInstallBeforeScan";
    public static final String IS_SYNCHRONOUS = "cxIsSynchronous";
    public static final String THRESHOLD_ENABLED = "cxThresholdEnabled";
    public static final String HIGH_THRESHOLD = "cxHighThreshold";
    public static final String MEDIUM_THRESHOLD = "cxMediumThreshold";
    public static final String LOW_THRESHOLD = "cxLowThreshold";
    public static final String OSA_THRESHOLD_ENABLED = "cxOsaThresholdEnabled";
    public static final String OSA_HIGH_THRESHOLD = "cxOsaHighThreshold";
    public static final String OSA_MEDIUM_THRESHOLD = "cxOsaMediumThreshold";
    public static final String OSA_LOW_THRESHOLD = "cxOsaLowThreshold";

    public static final String GLOBAL_SERVER_URL = "cxGlobalServerUrl";
    public static final String GLOBAL_USERNAME = "cxGlobalUsername";
    public static final String GLOBAL_PASSWORD = "cxGlobalPassword";
    public static final String GLOBAL_EXCLUDE_FOLDERS = "cxGlobalExcludeFolders";
    public static final String GLOBAL_FILTER_PATTERNS = "cxGlobalFilterPatterns";
    public static final String GLOBAL_SCAN_TIMEOUT_IN_MINUTES = "cxGlobalScanTimeoutInMinutes";
    public static final String GLOBAL_IS_SYNCHRONOUS = "cxGlobalIsSynchronous";
    public static final String GLOBAL_THRESHOLD_ENABLED = "cxGlobalThresholdEnabled";
    public static final String GLOBAL_HIGH_THRESHOLD = "cxGlobalHighThreshold";
    public static final String GLOBAL_MEDIUM_THRESHOLD = "cxGlobalMediumThreshold";
    public static final String GLOBAL_LOW_THRESHOLD = "cxGlobalLowThreshold";
    public static final String GLOBAL_OSA_THRESHOLD_ENABLED = "cxGlobalOsaThresholdEnabled";
    public static final String GLOBAL_OSA_HIGH_THRESHOLD = "cxGlobalOsaHighThreshold";
    public static final String GLOBAL_OSA_MEDIUM_THRESHOLD = "cxGlobalOsaMediumThreshold";
    public static final String GLOBAL_OSA_LOW_THRESHOLD = "cxGlobalOsaLowThreshold";


    public static final String[] GLOBAL_CONFIGS = {
            GLOBAL_SERVER_URL, GLOBAL_USERNAME, GLOBAL_PASSWORD, GLOBAL_EXCLUDE_FOLDERS, GLOBAL_FILTER_PATTERNS,
            GLOBAL_SCAN_TIMEOUT_IN_MINUTES, GLOBAL_IS_SYNCHRONOUS, GLOBAL_THRESHOLD_ENABLED, GLOBAL_HIGH_THRESHOLD,
            GLOBAL_MEDIUM_THRESHOLD, GLOBAL_LOW_THRESHOLD, GLOBAL_OSA_THRESHOLD_ENABLED, GLOBAL_OSA_HIGH_THRESHOLD,
            GLOBAL_OSA_MEDIUM_THRESHOLD, GLOBAL_OSA_LOW_THRESHOLD
    };


}
