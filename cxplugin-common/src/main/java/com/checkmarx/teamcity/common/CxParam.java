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
    public static final String ENGINE_CONFIG_ID = "cxEngineConfigId";
    public static final String EXCLUDE_FOLDERS = "cxExcludeFolders";
    public static final String FILTER_PATTERNS = "cxFilterPatterns";
    public static final String SCAN_TIMEOUT_IN_MINUTES = "cxScanTimeoutInMinutes";
    public static final String SCAN_COMMENT = "cxScanComment";
    public static final String IS_INCREMENTAL = "cxIsIncremental";
    public static final String CUSTOM_FIELDS = "cxCustomFields";
    public static final String PERIODIC_FULL_SCAN = "cxIsPeriodicFullScan";
    public static final String PERIODIC_FULL_SCAN_AFTER = "cxPeriodicFullScanAfter";
    public static final String GENERATE_PDF_REPORT = "cxGeneratePDFReport";
    public static final String OSA_ENABLED = "cxOsaEnabled";
    public static final String DEPENDENCY_SCANNER_TYPE = "dependencyScannerType";
    public static final String DEPENDENCY_SCA_SCAN_TYPE = "dependencyScaScanType";
    public static final String SAST_ENABLED = "cxSastEnabled";
    public static final String OSA_FILTER_PATTERNS = "cxOsaFilterPatterns";
    public static final String OSA_ARCHIVE_INCLUDE_PATTERNS = "cxOsaArchiveIncludePatterns";
    public static final String OSA_INSTALL_BEFORE_SCAN = "cxOsaInstallBeforeScan";
    public static final String IS_SYNCHRONOUS = "cxIsSynchronous";
    public static final String THRESHOLD_ENABLED = "cxThresholdEnabled";
    public static final String CRITICAL_THRESHOLD = "cxCriticalThreshold";
    public static final String HIGH_THRESHOLD = "cxHighThreshold";
    public static final String MEDIUM_THRESHOLD = "cxMediumThreshold";
    public static final String LOW_THRESHOLD = "cxLowThreshold";
    public static final String OSA_THRESHOLD_ENABLED = "cxOsaThresholdEnabled";
    public static final String OSA_CRITICAL_THRESHOLD = "cxOsaCriticalThreshold";
    public static final String OSA_HIGH_THRESHOLD = "cxOsaHighThreshold";
    public static final String OSA_MEDIUM_THRESHOLD = "cxOsaMediumThreshold";
    public static final String OSA_LOW_THRESHOLD = "cxOsaLowThreshold";
    public static final String PROJECT_POLICY_VIOLATION = "cxProjectPolicyViolation";
    public static final String PROJECT_SCA_POLICY_VIOLATION = "cxProjectSCAPolicyViolation";

    public static final String NO_TEAM_PATH = "noTeamPath";
    public static final int NO_PRESET_ID = -1;
    public static final int PROJECT_DEFAULT_CONFIG_ID = 0;
    public static final String PROJECT_DEFAULT = "Project Default (Only for CxSAST 9.3.0+)";

    public static final String GLOBAL_SERVER_URL = "cxGlobalServerUrl";
    public static final String GLOBAL_USERNAME = "cxGlobalUsername";
    public static final String GLOBAL_PASSWORD = "cxGlobalPassword";
    public static final String GLOBAL_EXCLUDE_FOLDERS = "cxGlobalExcludeFolders";
    public static final String GLOBAL_FILTER_PATTERNS = "cxGlobalFilterPatterns";
    public static final String GLOBAL_SCAN_TIMEOUT_IN_MINUTES = "cxGlobalScanTimeoutInMinutes";
    public static final String GLOBAL_IS_SYNCHRONOUS = "cxGlobalIsSynchronous";
    public static final String GLOBAL_THRESHOLD_ENABLED = "cxGlobalThresholdEnabled";
    public static final String GLOBAL_CRITICAL_THRESHOLD = "cxGlobalCriticalThreshold";
    public static final String GLOBAL_HIGH_THRESHOLD = "cxGlobalHighThreshold";
    public static final String GLOBAL_MEDIUM_THRESHOLD = "cxGlobalMediumThreshold";
    public static final String GLOBAL_LOW_THRESHOLD = "cxGlobalLowThreshold";
    public static final String GLOBAL_OSA_THRESHOLD_ENABLED = "cxGlobalOsaThresholdEnabled";
    public static final String GLOBAL_OSA_CRITICAL_THRESHOLD = "cxGlobalOsaCriticalThreshold";
    public static final String GLOBAL_OSA_HIGH_THRESHOLD = "cxGlobalOsaHighThreshold";
    public static final String GLOBAL_OSA_MEDIUM_THRESHOLD = "cxGlobalOsaMediumThreshold";
    public static final String GLOBAL_OSA_LOW_THRESHOLD = "cxGlobalOsaLowThreshold";
    public static final String GLOBAL_PROJECT_POLICY_VIOLATION = "cxGlobalProjectPolicyViolation";
    public static final String GLOBAL_PROJECT_SCA_POLICY_VIOLATION = "cxGlobalProjectSCAPolicyViolation";
    public static final String GLOBAL_OSA_ARCHIVE_INCLUDE_PATTERNS = "cxGlobalOsaArchiveIncludePatterns";
    public static final String GLOBAL_DEPENDENCY_SCAN_FILTER_PATTERNS="CxGlobalDependencyScanFilterPatterns";
    public static final String GLOBAL_EXECUTE_DEPENDENCY_MANAGER = "cxGlobalExecuteDependencyManager";
    public static final String GLOBAL_SCA_ENABLED = "cxGlobalScaEnabled";
    public static final String GLOBAL_OSA_ENABLED = "cxGlobalOsaEnabled";
    public static final String GLOBAL_DEPENDENCY_SCANNER_TYPE = "cxGlobalDependencyScanType";
    public static final String GLOBAL_SCA_API_URL = "cxGlobalSCAServerUrl";
    public static final String GLOBAL_SCA_ACCESS_CONTROL_URL = "cxGlobalSCAAccessControlServerURL";
    public static final String GLOBAL_SCA_WEB_APP_URL = "cxGlobalSCAWebAppURL";
    public static final String GLOBAL_SCA_USERNAME = "cxGlobalSCAUserName";
    public static final String GLOBAL_SCA_PASSWORD = "cxGlobalSCAPassword";
    public static final String GLOBAL_SCA_TENANT = "cxGlobalSCATenant";
    
    public static final String GLOBAL_SCA_CONFIGFILE = "cxGlobalScaConfigFile";
    public static final String GLOBAL_SCA_ENV_VARIABLE = "cxGlobalScaEnvVariable";
    public static final String GLOBAL_IS_EXPLOITABLE_PATH = "cxGlobalIsExploitablePath";
    public static final String GLOBAL_SAST_SERVER_URL = "cxGlobalSastServerUrl";
    public static final String GLOBAL_SAST_SERVER_USERNAME = "cxGlobalSastUsername";
    public static final String GLOBAL_SAST_SERVER_PASSWORD = "cxGlobalSastPassword";

    public static final String GLOBAL_DEFINE_DEPENDENCY_SCAN_SETTINGS = "globalDependencyScanEnabled";
    public static final String GLOBAL_SCA_THRESHOLD_ENABLED = "cxGlobalSCAThresholdEnabled";

    //SCA Params
    public static final String SCA_API_URL = "scaApiUrl";
    public static final String SCA_ACCESS_CONTROL_URL = "scaAccessControlUrl";
    public static final String SCA_RESOLVER_ADD_PARAMETERS = "scaResolverAddParameters";
    public static final String SCA_RESOLVER_PATH = "scaResolverPath";
    public static final String SCA_WEB_APP_URL = "scaWebAppUrl";
    public static final String SCA_USERNAME = "scaUserName";
    public static final String SCA_PASSWORD = "scaPass";
    public static final String SCA_TENANT = "scaTenant";
    public static final String SCA_TEAMPATH = "scaTeampath";
    public static final String SCA_CONFIGFILE = "scaConfigFile";
    public static final String SCA_ENV_VARIABLE = "scaEnvVariable";
    public static final String IS_INCLUDE_SOURCES = "isIncludeSources";
    public static final String IS_EXPLOITABLE_PATH = "isExploitablePath";
    public static final String SCA_SAST_PROJECT_FULLPATH = "scaSASTProjectFullPath";
    public static final String SCA_SAST_PROJECT_ID = "scaSASTProjectID";
    public static final String USE_SAST_DEFAULT_SERVER = "useSASTDefaultServer";
    public static final String SCA_SAST_SERVER_URL = "scaSASTServerUrl";
    public static final String SCA_SAST_SERVER_USERNAME = "scaSASTUserName";
    public static final String SCA_SAST_SERVER_PASSWORD = "scaSASTPassword";
    
    public static final String SCA_CRITICAL = "ScaCritical";
    public static final String SCA_HIGH = "ScaHigh";
    public static final String SCA_MEDIUM = "ScaMedium";
    public static final String SCA_LOW = "ScaLow";
    public static final String SCA_FILES_INCLUDE = "ScaFileInclude";
    public static final String SCA_FILES_EXCLUDE = "ScaFilesExclude";
    public static final String SCA_FOLDER_EXCLUDE = "ScaFolderExclude";
    public static final String SCA_LOCATION_PATH = "ScaLocationPath";
    public static final String OVERRIDE_GLOBAL_CONFIGURATIONS = "OverrideGlobalConfigurations";
    public static final String DEPENDENCY_SCAN_ENABLED = "dependencyScanEnabled";


    public static final String CONNECTION_FAILED_COMPATIBILITY = "Connection Failed.\n" +
            "Validate the provided login credentials and server URL are correct.\n" +
            "In addition, make sure the installed plugin version is compatible with the CxSAST version according to CxSAST release notes.";
    public static final String SCA_CONNECTION_FAILED_COMPATIBILITY = "Connection Failed.\n" +
            "Validate the provided login credentials and server URL are correct.";

    public static final String[] GLOBAL_CONFIGS = {
            GLOBAL_SERVER_URL, GLOBAL_USERNAME, GLOBAL_PASSWORD, GLOBAL_EXCLUDE_FOLDERS, GLOBAL_FILTER_PATTERNS,
            GLOBAL_SCAN_TIMEOUT_IN_MINUTES, GLOBAL_IS_SYNCHRONOUS, GLOBAL_THRESHOLD_ENABLED, GLOBAL_CRITICAL_THRESHOLD, GLOBAL_HIGH_THRESHOLD,
            GLOBAL_MEDIUM_THRESHOLD, GLOBAL_LOW_THRESHOLD, GLOBAL_OSA_THRESHOLD_ENABLED,GLOBAL_OSA_CRITICAL_THRESHOLD, GLOBAL_OSA_HIGH_THRESHOLD,
            GLOBAL_OSA_MEDIUM_THRESHOLD, GLOBAL_OSA_LOW_THRESHOLD,GLOBAL_DEFINE_DEPENDENCY_SCAN_SETTINGS,GLOBAL_DEPENDENCY_SCANNER_TYPE, GLOBAL_PROJECT_POLICY_VIOLATION,GLOBAL_PROJECT_SCA_POLICY_VIOLATION,
            GLOBAL_OSA_ARCHIVE_INCLUDE_PATTERNS,GLOBAL_DEPENDENCY_SCAN_FILTER_PATTERNS, GLOBAL_EXECUTE_DEPENDENCY_MANAGER,
            GLOBAL_SCA_ENABLED, GLOBAL_OSA_ENABLED, GLOBAL_SCA_API_URL, GLOBAL_SCA_ACCESS_CONTROL_URL,
            GLOBAL_SCA_WEB_APP_URL, GLOBAL_SCA_USERNAME, GLOBAL_SCA_PASSWORD, GLOBAL_SCA_TENANT, GLOBAL_SCA_CONFIGFILE, GLOBAL_SCA_ENV_VARIABLE, GLOBAL_IS_EXPLOITABLE_PATH
            , GLOBAL_SAST_SERVER_URL, GLOBAL_SAST_SERVER_USERNAME, GLOBAL_SAST_SERVER_PASSWORD
    };


}