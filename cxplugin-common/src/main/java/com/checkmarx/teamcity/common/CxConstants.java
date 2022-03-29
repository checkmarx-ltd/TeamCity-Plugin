package com.checkmarx.teamcity.common;


public abstract class CxConstants {

    public static final String RUNNER_TYPE = "checkmarx";
    public static final String RUNNER_DESCRIPTION = "Checkmarx SAST Scan";
    public static final String RUNNER_DISPLAY_NAME = "Checkmarx";

    public final static String REPORT_HTML_NAME = "report.html";

    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public static final String DEFAULT_SERVER_URL = "http://localhost";
    public static final String DEFAULT_FILTER_PATTERN =
                    "!**/_cvs/**/*, !**/.svn/**/*,   !**/.hg/**/*,   !**/.git/**/*,  !**/.bzr/**/*, !**/bin/**/*,\n" +
                    "!**/obj/**/*,  !**/backup/**/*, !**/.idea/**/*, !**/*.DS_Store, !**/*.ipr,     !**/*.iws,\n" +
                    "!**/*.bak,     !**/*.tmp,       !**/*.aac,      !**/*.aif,      !**/*.iff,     !**/*.m3u,   !**/*.mid, !**/*.mp3,\n" +
                    "!**/*.mpa,     !**/*.ra,        !**/*.wav,      !**/*.wma,      !**/*.3g2,     !**/*.3gp,   !**/*.asf, !**/*.asx,\n" +
                    "!**/*.avi,     !**/*.flv,       !**/*.mov,      !**/*.mp4,      !**/*.mpg,     !**/*.rm,    !**/*.swf, !**/*.vob,\n" +
                    "!**/*.wmv,     !**/*.bmp,       !**/*.gif,      !**/*.jpg,      !**/*.png,     !**/*.psd,   !**/*.tif, !**/*.swf,\n" +
                    "!**/*.jar,     !**/*.zip,       !**/*.rar,      !**/*.exe,      !**/*.dll,     !**/*.pdb,   !**/*.7z,  !**/*.gz,\n" +
                    "!**/*.tar.gz,  !**/*.tar,       !**/*.gz,       !**/*.ahtm,     !**/*.ahtml,   !**/*.fhtml, !**/*.hdm,\n" +
                    "!**/*.hdml,    !**/*.hsql,      !**/*.ht,       !**/*.hta,      !**/*.htc,     !**/*.htd,   !**/*.war, !**/*.ear,\n" +
                    "!**/*.htmls,   !**/*.ihtml,     !**/*.mht,      !**/*.mhtm,     !**/*.mhtml,   !**/*.ssi,   !**/*.stm,\n" +
                    "!**/*.stml,    !**/*.ttml,      !**/*.txn,      !**/*.xhtm,     !**/*.xhtml,   !**/*.class, !**/node_modules/**/*, !**/*.iml\n";

    public static final String DEFAULT_OSA_ARCHIVE_INCLUDE_PATTERNS = "*.zip, *.tgz, *.war, *.ear";
    
    public static final String DEFAULT_SCA_API_URL = "https://api-sca.checkmarx.net";
    public static final String DEFAULT_SCA_ACCESS_CONTROL_URL = "https://platform.checkmarx.net";
    public static final String DEFAULT_SCA_WEB_APP_URL = "https://sca.checkmarx.net";

    public static final String CONNECTION_SUCCESSFUL_MESSAGE = "Connection successful";
    public static final String SUCCESSFUL_SAVE_MESSAGE = "Settings Saved Successfully";
    public final static String NO_PRESET_MESSAGE = "Unable to connect to server. Make sure URL and credentials are valid to see presets list";
    public final static String NO_TEAM_MESSAGE = "Unable to connect to server. Make sure URL and credentials are valid to see teams list";
    public final static String NO_ENGINE_CONFIG_MESSAGE = "Unable to connect to server. Make sure URL and credentials are valid to see engine configuration list";
    public final static String UNABLE_TO_CONNECT_MESSAGE = "Unable to connect to the server. Please check the server logs for further information";

    public static final String URL_NOT_EMPTY_MESSAGE = "Server URL must not be empty";
    public static final String URL_NOT_VALID_MESSAGE = "Server URL is not valid";
    public static final String USERNAME_NOT_EMPTY_MESSAGE = "Username must not be empty";
    public static final String PASSWORD_NOT_EMPTY_MESSAGE = "Password must not be empty";
    public static final String SAST_URL_NOT_EMPTY_MESSAGE = "Server URL for exploitable path feature must not be empty";
    public static final String SAST_URL_NOT_VALID_MESSAGE = "Server URL for exploitable path feature is not valid";
    public static final String SAST_USERNAME_NOT_EMPTY_MESSAGE = "Username for exploitable path feature must not be empty";
    public static final String SAST_PASSWORD_NOT_EMPTY_MESSAGE = "Password for exploitable path feature must not be empty";
    public static final String PROJECT_FULLPATH_PROJECT_ID_NOT_EMPTY_MESSAGE = "Must provide value for either 'Project Full Path' or 'Project Id'.";
    public static final String PROJECT_NAME_NOT_EMPTY_MESSAGE = "Project name must not be empty";
    public static final String PROJECT_NAME_ILLEGAL_CHARS_MESSAGE = "Project name should not contain the following characters: / ? < > : * |";
    public static final String PROJECT_NAME_MAX_CHARS_MESSAGE = "Project name length must not exceed 200 characters";
    public static final String PRESET_NOT_EMPTY_MESSAGE = "Preset must not be empty";
    public static final String TEAM_NOT_EMPTY_MESSAGE = "Team must not be empty";
    public static final String SCAN_TIMEOUT_POSITIVE_INTEGER_MESSAGE = "Scan timeout must be a number greater than zero";
    public static final String THRESHOLD_POSITIVE_INTEGER_MESSAGE = "Threshold must be 0 or greater, or leave blank for no thresholds";
    public static final String WRONG_PERIODIC_FULL_SCAN_INTERVAL = "The value must be a number between 1 and 99.";
    public static final String CUSTOM_FIELDS_FORMAT_ERROR_MESSAGE = "Custom Fields must to have next format: key1:val1,key2:val2";

    public static final String TEAMCITY_PLUGIN_VERSION = "cx.teamcity.plugin.version";

    public static final String ORIGIN_TEAMCITY = "TeamCity";
    public static final int FULL_SCAN_CYCLE_MIN = 1;
    public static final int FULL_SCAN_CYCLE_MAX = 99;
    public static final String CX_BUILD_NUMBER = "BUILD_NUMBER";

    public static final String SCA_RESOLVER_PATH_NOT_EMPTY_MESSAGE = "SCA resolver path must not be empty";
    public static final String SCA_RESOLVER_ADD_PARAMETERS_NOT_EMPTY_MESSAGE = "SCA resolver additional parameters must not be empty";

}
