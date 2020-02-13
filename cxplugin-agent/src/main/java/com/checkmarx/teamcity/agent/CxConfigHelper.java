package com.checkmarx.teamcity.agent;

import com.checkmarx.teamcity.common.CxConstants;
import com.checkmarx.teamcity.common.InvalidParameterException;
import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.dto.DependencyScannerType;
import com.cx.restclient.sca.dto.SCAConfig;
import jetbrains.buildServer.serverSide.crypt.EncryptUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Map;

import static com.checkmarx.teamcity.common.CxConstants.TRUE;
import static com.checkmarx.teamcity.common.CxParam.*;

/**
 * Created by eyala on 6/12/2018.
 */
public class CxConfigHelper {

    public static CxScanConfig resolveConfigurations(Map<String, String> buildParameters, Map<String, String> globalParameters, File checkoutDirectory,
             File reportDirectory) throws InvalidParameterException {

        CxScanConfig ret = new CxScanConfig();
        SCAConfig scaConfig= new SCAConfig();
        //to support builds that were configured before this parameter, allow sast scan if parameter is null.
        ret.setSastEnabled(buildParameters.get(SAST_ENABLED) == null || TRUE.equals(buildParameters.get(SAST_ENABLED)));
        ret.setCxOrigin(CxConstants.ORIGIN_TEAMCITY);
        ret.setSourceDir(checkoutDirectory.getAbsolutePath());
        ret.setReportsDir(reportDirectory);



        if (TRUE.equals(buildParameters.get(USE_DEFAULT_SERVER))) {
            ret.setUrl(validateNotEmpty(globalParameters.get(GLOBAL_SERVER_URL), GLOBAL_SERVER_URL));
            ret.setUsername(validateNotEmpty(globalParameters.get(GLOBAL_USERNAME), GLOBAL_USERNAME));
            ret.setPassword(EncryptUtil.unscramble(validateNotEmpty(globalParameters.get(GLOBAL_PASSWORD), GLOBAL_PASSWORD)));
        } else {
            ret.setUrl(validateNotEmpty(buildParameters.get(SERVER_URL), SERVER_URL));
            ret.setUsername(validateNotEmpty(buildParameters.get(USERNAME), USERNAME));
            ret.setPassword(EncryptUtil.unscramble(validateNotEmpty(buildParameters.get(PASSWORD), PASSWORD)));
        }


        ret.setProjectName(validateNotEmpty(buildParameters.get(PROJECT_NAME), PROJECT_NAME));
        ret.setPresetId(convertToIntegerIfNotNull(buildParameters.get(PRESET_ID), PRESET_ID));
        ret.setTeamId(validateNotEmpty(buildParameters.get(TEAM_ID), TEAM_ID));

        if(ret.getSastEnabled()){
            if (TRUE.equals(buildParameters.get(USE_DEFAULT_SAST_CONFIG))) {
                ret.setSastFolderExclusions(globalParameters.get(GLOBAL_EXCLUDE_FOLDERS));
                ret.setSastFilterPattern(globalParameters.get(GLOBAL_FILTER_PATTERNS));
                ret.setSastScanTimeoutInMinutes(convertToIntegerIfNotNull(globalParameters.get(GLOBAL_SCAN_TIMEOUT_IN_MINUTES), GLOBAL_SCAN_TIMEOUT_IN_MINUTES));

            } else {
                ret.setSastFolderExclusions(buildParameters.get(EXCLUDE_FOLDERS));
                ret.setSastFilterPattern(buildParameters.get(FILTER_PATTERNS));
                ret.setSastScanTimeoutInMinutes(convertToIntegerIfNotNull(buildParameters.get(SCAN_TIMEOUT_IN_MINUTES), SCAN_TIMEOUT_IN_MINUTES));
            }

            ret.setScanComment(buildParameters.get(SCAN_COMMENT));
            ret.setIncremental(TRUE.equals(buildParameters.get(IS_INCREMENTAL)));
            ret.setGeneratePDFReport(TRUE.equals(buildParameters.get(GENERATE_PDF_REPORT)));
        }

        String rawScannerType = buildParameters.get(DEPENDENCY_SCANNER_TYPE);
        if (TRUE.equals(buildParameters.get(DEPENDENCY_SCAN_ENABLED)) &&
                StringUtils.isNotEmpty(rawScannerType)) {
            ret.setDependencyScannerType(Enum.valueOf(DependencyScannerType.class, rawScannerType));
        } else {
            ret.setDependencyScannerType(DependencyScannerType.NONE);
        }

        ret.setOsaFilterPattern(buildParameters.get(OSA_FILTER_PATTERNS));
        ret.setOsaArchiveIncludePatterns(buildParameters.get(OSA_ARCHIVE_INCLUDE_PATTERNS));
        ret.setOsaRunInstall(TRUE.equals(buildParameters.get(OSA_INSTALL_BEFORE_SCAN)));


        String thresholdEnabled = THRESHOLD_ENABLED;
        String highThreshold = HIGH_THRESHOLD;
        String mediumThreshold = MEDIUM_THRESHOLD;
        String lowThreshold = LOW_THRESHOLD;

        String osaThresholdEnabled = OSA_THRESHOLD_ENABLED;
        String osaHighThreshold = OSA_HIGH_THRESHOLD;
        String osaMediumThreshold = MEDIUM_THRESHOLD;
        String osaLowThreshold = LOW_THRESHOLD;

        String isSynchronous = IS_SYNCHRONOUS;
        String enablePolicyViolation = PROJECT_POLICY_VIOLATION;
    // TODO: 2/13/2020  add parameters that is common for two pages
        String globalExecuteDependencyManager=GLOBAL_EXECUTE_DEPENDENCY_MANAGER;

        Map<String,String> parameters = buildParameters;

        if(TRUE.equals(buildParameters.get(USE_DEFAULT_SCAN_CONTROL))){
             thresholdEnabled = GLOBAL_THRESHOLD_ENABLED;
             highThreshold = GLOBAL_HIGH_THRESHOLD;
             mediumThreshold = GLOBAL_MEDIUM_THRESHOLD;
             lowThreshold = GLOBAL_LOW_THRESHOLD;

             osaThresholdEnabled = GLOBAL_OSA_THRESHOLD_ENABLED;
             osaHighThreshold = GLOBAL_OSA_HIGH_THRESHOLD;
             osaMediumThreshold = GLOBAL_OSA_MEDIUM_THRESHOLD;
             osaLowThreshold = GLOBAL_OSA_LOW_THRESHOLD;

            isSynchronous = GLOBAL_IS_SYNCHRONOUS;
            enablePolicyViolation = GLOBAL_PROJECT_POLICY_VIOLATION;

            parameters = globalParameters;
        }

        ret.setSynchronous(TRUE.equals(parameters.get(isSynchronous)));
        ret.setEnablePolicyViolations(TRUE.equals(parameters.get(enablePolicyViolation)));


        if (ret.getSastEnabled()) {
            ret.setSastThresholdsEnabled(TRUE.equals(parameters.get(thresholdEnabled)));
            if (ret.getSastThresholdsEnabled()) {
                ret.setSastHighThreshold(convertToIntegerIfNotNull(parameters.get(highThreshold), highThreshold));
                ret.setSastMediumThreshold(convertToIntegerIfNotNull(parameters.get(mediumThreshold), mediumThreshold));
                ret.setSastLowThreshold(convertToIntegerIfNotNull(parameters.get(lowThreshold), lowThreshold));
            }
        }


        if (ret.getDependencyScannerType() != DependencyScannerType.NONE) {
            ret.setOsaThresholdsEnabled(TRUE.equals(parameters.get(osaThresholdEnabled)));
            if (ret.getOsaThresholdsEnabled()) {
                ret.setOsaHighThreshold(convertToIntegerIfNotNull(parameters.get(osaHighThreshold), osaHighThreshold));
                ret.setOsaMediumThreshold(convertToIntegerIfNotNull(parameters.get(osaMediumThreshold), osaMediumThreshold));
                ret.setOsaLowThreshold(convertToIntegerIfNotNull(parameters.get(osaLowThreshold), osaLowThreshold));
            }
            if (ret.getDependencyScannerType().equals(DependencyScannerType.SCA)) {
                scaConfig.setAccessControlUrl(buildParameters.get(SCA_ACCESS_CONTROL_URL));
                scaConfig.setWebAppUrl(buildParameters.get(SCA_WEB_APP_URL));
                scaConfig.setApiUrl(buildParameters.get(SCA_API_URL));
                scaConfig.setPassword(buildParameters.get(SCA_PASSWORD));
                scaConfig.setUsername(buildParameters.get(SCA_USERNAME));
                scaConfig.setTenant(buildParameters.get(SCA_TENANT));
                ret.setScaConfig(scaConfig);
            }
        }


        return ret;
    }

    private static Integer convertToIntegerIfNotNull(String param, String paramName) throws InvalidParameterException {

        if (param != null && param.length() > 0) {
            try {
                int i = Integer.parseInt(param);
                if (i < 0) {
                    throw new InvalidParameterException("Parameter [" + paramName + "] must be positive integer. Actual value: " + param);
                }
                return i;

            } catch (NumberFormatException e) {
                throw new InvalidParameterException("Parameter [" + paramName + "] must be positive integer. Actual value: " + param);
            }
        }
        return null;
    }

    private static String validateNotEmpty(String param, String paramName) throws InvalidParameterException {
        if (param == null || param.length() == 0) {
            throw new InvalidParameterException("Parameter [" + paramName + "] must not be empty");
        }
        return param;
    }

}
