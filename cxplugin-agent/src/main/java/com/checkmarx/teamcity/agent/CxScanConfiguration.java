package com.checkmarx.teamcity.agent;


import com.checkmarx.teamcity.common.InvalidParameterException;
import jetbrains.buildServer.serverSide.crypt.EncryptUtil;

import java.util.Map;

import static com.checkmarx.teamcity.common.CxConstants.TRUE;
import static com.checkmarx.teamcity.common.CxParam.*;

/**
 * Created by galn on 21/12/2016.
 */
public class CxScanConfiguration {

    private String username;
    /**
     * The password of the user running the scan.
     */
    private String password;
    /**
     * Host name of the CheckmarxTask application.
     */
    private String url;
    /**
     * The name of the project being scanned.
     */
    private String projectName;
    private long presetId;
    private String teamId;
    private String folderExclusions;
    private String filterPattern;
    /**
     * Define a timeout (in minutes) for the scan. If the specified time has passed, the build fails.
     * Set to 0 to run the scan with no time limit.
     */
    private Integer scanTimeoutInMinutes;
    private String scanComment;
    private boolean isIncremental = false;
    private boolean isSynchronous = false;
    private boolean thresholdsEnabled = false;
    private Integer highThreshold;
    private Integer mediumThreshold;
    private Integer lowThreshold;
    private boolean generatePDFReport = false;
    private boolean osaEnabled = false;
    private String osaFilterPattern;
    private String osaArchiveIncludePatterns;
    private boolean osaInstallBeforeScan;

    private boolean osaThresholdsEnabled = false;
    /**
     * Configure a threshold for the CxOSA High Severity Vulnerabilities.
     * The build will fail if the sum of High Severity Vulnerabilities is larger than the threshold.
     * Leave empty to ignore threshold.
     */
    private Integer osaHighThreshold;
    /**
     * Configure a threshold for the CxOSA Medium Severity Vulnerabilities.
     * The build will fail if the sum of Medium Severity Vulnerabilities is larger than the threshold.
     * Leave empty to ignore threshold.
     */
    private Integer osaMediumThreshold;
    /**
     * Configure a threshold for the CxOSA Low Severity Vulnerabilities.
     * The build will fail if the sum of Low Severity Vulnerabilities is larger than the threshold.
     * Leave empty to ignore threshold.
     */
    private Integer osaLowThreshold;


    public static CxScanConfiguration resolveConfigurations(Map<String, String> buildParameters, Map<String, String> globalParameters) throws InvalidParameterException {

        CxScanConfiguration ret = new CxScanConfiguration();

        if(TRUE.equals(buildParameters.get(USE_DEFAULT_SERVER))) {
            ret.setUrl(validateNotEmpty(globalParameters.get(GLOBAL_SERVER_URL), GLOBAL_SERVER_URL));
            ret.setUsername(validateNotEmpty(globalParameters.get(GLOBAL_USERNAME), GLOBAL_USERNAME ));
            ret.setPassword(EncryptUtil.unscramble(validateNotEmpty(globalParameters.get(GLOBAL_PASSWORD), GLOBAL_PASSWORD)));
        } else {
            ret.setUrl(validateNotEmpty(buildParameters.get(SERVER_URL),SERVER_URL));
            ret.setUsername(validateNotEmpty(buildParameters.get(USERNAME), USERNAME));
            ret.setPassword(EncryptUtil.unscramble(validateNotEmpty(buildParameters.get(PASSWORD), PASSWORD)));
        }


        ret.setProjectName(validateNotEmpty(buildParameters.get(PROJECT_NAME), PROJECT_NAME));
        ret.setPresetId(convertToLong(buildParameters.get(PRESET_ID), PRESET_ID));
        ret.setTeamId(validateNotEmpty(buildParameters.get(TEAM_ID), TEAM_ID));

        if(TRUE.equals(buildParameters.get(USE_DEFAULT_SAST_CONFIG))) {
            ret.setFolderExclusions(globalParameters.get(GLOBAL_EXCLUDE_FOLDERS));
            ret.setFilterPattern(globalParameters.get(GLOBAL_FILTER_PATTERNS));
            ret.setScanTimeoutInMinutes(convertToIntegerIfNotNull(globalParameters.get(GLOBAL_SCAN_TIMEOUT_IN_MINUTES), GLOBAL_SCAN_TIMEOUT_IN_MINUTES));

        } else {
            ret.setFolderExclusions(buildParameters.get(EXCLUDE_FOLDERS));
            ret.setFilterPattern(buildParameters.get(FILTER_PATTERNS));
            ret.setScanTimeoutInMinutes(convertToIntegerIfNotNull(buildParameters.get(SCAN_TIMEOUT_IN_MINUTES), SCAN_TIMEOUT_IN_MINUTES));
        }

        ret.setScanComment(buildParameters.get(SCAN_COMMENT));
        ret.setIncremental(TRUE.equals(buildParameters.get(IS_INCREMENTAL)));
        ret.setGeneratePDFReport(TRUE.equals(buildParameters.get(GENERATE_PDF_REPORT)));
        ret.setOsaEnabled(TRUE.equals(buildParameters.get(OSA_ENABLED)));
        ret.setOsaFilterPattern(buildParameters.get(OSA_FILTER_PATTERNS));
        ret.setOsaArchiveIncludePatterns(buildParameters.get(OSA_ARCHIVE_INCLUDE_PATTERNS));
        ret.setOsaInstallBeforeScan(TRUE.equals(buildParameters.get(OSA_INSTALL_BEFORE_SCAN)));

        if(TRUE.equals(buildParameters.get(USE_DEFAULT_SCAN_CONTROL))) {

            ret.setSynchronous(TRUE.equals(globalParameters.get(GLOBAL_IS_SYNCHRONOUS)));

            if(ret.isSynchronous()) {

                ret.setThresholdsEnabled(TRUE.equals(globalParameters.get(GLOBAL_THRESHOLD_ENABLED)));
                if(ret.isThresholdsEnabled()) {
                    ret.setHighThreshold(convertToIntegerIfNotNull(globalParameters.get(GLOBAL_HIGH_THRESHOLD),GLOBAL_HIGH_THRESHOLD));
                    ret.setMediumThreshold(convertToIntegerIfNotNull(globalParameters.get(GLOBAL_MEDIUM_THRESHOLD),GLOBAL_MEDIUM_THRESHOLD));
                    ret.setLowThreshold(convertToIntegerIfNotNull(globalParameters.get(GLOBAL_LOW_THRESHOLD),GLOBAL_LOW_THRESHOLD));
                }

                if(ret.isOsaEnabled()) {
                    ret.setOsaThresholdsEnabled(TRUE.equals(globalParameters.get(GLOBAL_OSA_THRESHOLD_ENABLED)));
                    if(ret.isOsaThresholdsEnabled()) {
                        ret.setOsaHighThreshold(convertToIntegerIfNotNull(globalParameters.get(GLOBAL_OSA_HIGH_THRESHOLD),GLOBAL_OSA_HIGH_THRESHOLD));
                        ret.setOsaMediumThreshold(convertToIntegerIfNotNull(globalParameters.get(GLOBAL_OSA_MEDIUM_THRESHOLD),GLOBAL_OSA_MEDIUM_THRESHOLD));
                        ret.setOsaLowThreshold(convertToIntegerIfNotNull(globalParameters.get(GLOBAL_OSA_LOW_THRESHOLD),GLOBAL_OSA_LOW_THRESHOLD));
                    }
                }

            }


        } else {

            ret.setSynchronous(TRUE.equals(buildParameters.get(IS_SYNCHRONOUS)));
            if (ret.isSynchronous()) {

                ret.setThresholdsEnabled(TRUE.equals(buildParameters.get(THRESHOLD_ENABLED)));
                if (ret.isThresholdsEnabled()) {
                    ret.setHighThreshold(convertToIntegerIfNotNull(buildParameters.get(HIGH_THRESHOLD), HIGH_THRESHOLD));
                    ret.setMediumThreshold(convertToIntegerIfNotNull(buildParameters.get(MEDIUM_THRESHOLD), MEDIUM_THRESHOLD));
                    ret.setLowThreshold(convertToIntegerIfNotNull(buildParameters.get(LOW_THRESHOLD), LOW_THRESHOLD));
                }

                if (ret.isOsaEnabled()) {
                    ret.setOsaThresholdsEnabled(TRUE.equals(buildParameters.get(OSA_THRESHOLD_ENABLED)));
                    if (ret.isOsaThresholdsEnabled()) {
                        ret.setOsaHighThreshold(convertToIntegerIfNotNull(buildParameters.get(OSA_HIGH_THRESHOLD), OSA_HIGH_THRESHOLD));
                        ret.setOsaMediumThreshold(convertToIntegerIfNotNull(buildParameters.get(OSA_MEDIUM_THRESHOLD), OSA_MEDIUM_THRESHOLD));
                        ret.setOsaLowThreshold(convertToIntegerIfNotNull(buildParameters.get(OSA_LOW_THRESHOLD), OSA_LOW_THRESHOLD));
                    }
                }
            }
        }

        return ret;
    }

    private static Long convertToLong(String param, String paramName) throws InvalidParameterException {
        try {
            return Long.parseLong(param);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("Parameter [" + paramName + "] must be positive integer. Actual value: " + param);
        }


    }

    private static Integer convertToIntegerIfNotNull(String param, String paramName) throws InvalidParameterException {

        if(param != null && param.length() > 0) {
            try {
                int i = Integer.parseInt(param);
                if(i < 0) {
                    throw new InvalidParameterException("Parameter [" + paramName + "] must be positive integer. Actual value: " + param);
                }
                return i;

            } catch (NumberFormatException e) {
                throw new InvalidParameterException("Parameter [" + paramName + "] must be positive integer. Actual value: " + param);
            }
        }
       return  null;
    }

    private static String validateNotEmpty(String param, String paramName) throws InvalidParameterException {
        if(param == null || param.length() == 0) {
            throw new InvalidParameterException("Parameter [" +paramName+"] must not be empty");
        }
        return param;
    }

    /********   Setters & Getters ***********/

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public long getPresetId() {
        return presetId;
    }

    public void setPresetId(long presetId) {
        this.presetId = presetId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public Integer getScanTimeoutInMinutes() {
        return scanTimeoutInMinutes;
    }

    public void setScanTimeoutInMinutes(Integer scanTimeoutInMinutes) {
        this.scanTimeoutInMinutes = scanTimeoutInMinutes;
    }

    public String getScanComment() {
        return scanComment;
    }

    public void setScanComment(String scanComment) {
        this.scanComment = scanComment;
    }

    public boolean isIncremental() {
        return isIncremental;
    }

    public void setIncremental(boolean incremental) {
        isIncremental = incremental;
    }

    public boolean isSynchronous() {
        return isSynchronous;
    }

    public void setSynchronous(boolean synchronous) {
        isSynchronous = synchronous;
    }

    public boolean isThresholdsEnabled() {
        return thresholdsEnabled;
    }

    public void setThresholdsEnabled(boolean thresholdsEnabled) {
        this.thresholdsEnabled = thresholdsEnabled;
    }

    public Integer getHighThreshold() {
        return highThreshold;
    }

    public void setHighThreshold(Integer highThreshold) {
        this.highThreshold = highThreshold;
    }

    private void setHighThreshold(String highSeveritiesThreshold) {
        this.highThreshold = getAsInteger(highSeveritiesThreshold);
    }

    public Integer getMediumThreshold() {
        return mediumThreshold;
    }

    public void setMediumThreshold(Integer mediumThreshold) {
        this.mediumThreshold = mediumThreshold;
    }

    private void setMediumThreshold(String mediumSeveritiesThreshold) {
        this.mediumThreshold = getAsInteger(mediumSeveritiesThreshold);
    }

    public Integer getLowThreshold() {
        return lowThreshold;
    }

    public void setLowThreshold(Integer lowThreshold) {
        this.lowThreshold = lowThreshold;
    }

    private void setLowThreshold(String lowSeveritiesThreshold) {
        this.lowThreshold = getAsInteger(lowSeveritiesThreshold);
    }

    public String getFolderExclusions() {
        return folderExclusions;
    }

    public void setFolderExclusions(String folderExclusions) {
        this.folderExclusions = folderExclusions;
    }

    public String getFilterPattern() {
        return filterPattern;
    }

    public void setFilterPattern(String filterPattern) {
        this.filterPattern = filterPattern;
    }

    public boolean isGeneratePDFReport() {
        return generatePDFReport;
    }

    public void setGeneratePDFReport(boolean generatePDFReport) {
        this.generatePDFReport = generatePDFReport;
    }

    public boolean isOsaEnabled() {
        return osaEnabled;
    }

    public void setOsaEnabled(boolean osaEnabled) {
        this.osaEnabled = osaEnabled;
    }

    public String getOsaFilterPattern() {
        return osaFilterPattern;
    }

    public void setOsaFilterPattern(String osaFilterPattern) {
        this.osaFilterPattern = osaFilterPattern;
    }

    public String getOsaArchiveIncludePatterns() {
        return osaArchiveIncludePatterns;
    }

    public void setOsaArchiveIncludePatterns(String osaArchiveIncludePatterns) {
        this.osaArchiveIncludePatterns = osaArchiveIncludePatterns;
    }

    public boolean isOsaInstallBeforeScan() {
        return osaInstallBeforeScan;
    }

    public void setOsaInstallBeforeScan(boolean osaInstallBeforeScan) {
        this.osaInstallBeforeScan = osaInstallBeforeScan;
    }

    public boolean isOsaThresholdsEnabled() {
        return osaThresholdsEnabled;
    }

    public void setOsaThresholdsEnabled(boolean osaThresholdsEnabled) {
        this.osaThresholdsEnabled = osaThresholdsEnabled;
    }

    public Integer getOsaHighThreshold() {
        return osaHighThreshold;
    }

    public void setOsaHighThreshold(Integer osaHighThreshold) {
        this.osaHighThreshold = osaHighThreshold;
    }

    private void setOsaHighSeveritiesThreshold(String osaHighSeveritiesThreshold) {
        this.osaHighThreshold = getAsInteger(osaHighSeveritiesThreshold);
    }

    public Integer getOsaMediumThreshold() {
        return osaMediumThreshold;
    }

    public void setOsaMediumThreshold(Integer osaMediumThreshold) {
        this.osaMediumThreshold = osaMediumThreshold;
    }

    private void setOsaMediumSeveritiesThreshold(String osaMediumSeveritiesThreshold) {
        this.osaMediumThreshold = getAsInteger(osaMediumSeveritiesThreshold);
    }

    public Integer getOsaLowThreshold() {
        return osaLowThreshold;
    }

    public void setOsaLowThreshold(Integer osaLowThreshold) {
        this.osaLowThreshold = osaLowThreshold;
    }

    private void setOsaLowSeveritiesThreshold(String osaLowSeveritiesThreshold) {
        this.osaLowThreshold = getAsInteger(osaLowSeveritiesThreshold);
    }

    private Integer getAsInteger(String number) {
        Integer inti = null;
        try {
            if (number != null && number.length() > 0) {
                inti = Integer.parseInt(number);
            }
        } catch (NumberFormatException e) {
            inti = null;
        }
        return inti;
    }

    public boolean isSASTThresholdEffectivelyEnabled() {
        return isThresholdsEnabled() && (getLowThreshold() != null || getMediumThreshold() != null || getHighThreshold() != null);
    }

    public boolean isOSAThresholdEffectivelyEnabled() {
        return isOsaEnabled() && isOsaThresholdsEnabled() && (getOsaHighThreshold() != null || getOsaMediumThreshold() != null || getOsaLowThreshold() != null);
    }
}
