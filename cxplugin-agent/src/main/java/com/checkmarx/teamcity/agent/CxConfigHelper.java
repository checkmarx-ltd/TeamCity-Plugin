package com.checkmarx.teamcity.agent;

import com.checkmarx.teamcity.common.CxConstants;
import com.checkmarx.teamcity.common.InvalidParameterException;
import com.cx.restclient.ast.dto.sca.AstScaConfig;
import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.dto.ScannerType;
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

    private static final String PARAMETER_PREFIX = "Parameter [";
    private static final String PARAMETER_SUFFIX = "] must be positive integer. Actual value: ";

    public static CxScanConfig resolveConfigurations(Map<String, String> buildParameters, Map<String, String> globalParameters, File checkoutDirectory,
                                                     File reportDirectory) throws InvalidParameterException {


        CxScanConfig ret = new CxScanConfig();
        //to support builds that were configured before this parameter, allow sast scan if parameter is null.
        ret.setSastEnabled(buildParameters.get(SAST_ENABLED) == null || TRUE.equals(buildParameters.get(SAST_ENABLED)));
        ret.setCxOrigin(CxConstants.ORIGIN_TEAMCITY);
        ret.setSourceDir(checkoutDirectory.getAbsolutePath());
        ret.setReportsDir(reportDirectory);
        String isProxyVar = System.getProperty("cx.isproxy");
        ret.setProxy(StringUtils.isNotEmpty(isProxyVar) && isProxyVar.equalsIgnoreCase("true"));

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
        // Parameter name is TEAM_ID, But actual value is TEAM_NAME
        ret.setTeamPath(validateNotEmpty(buildParameters.get(TEAM_ID), TEAM_ID));

        if(ret.isSastEnabled()){
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


        if (TRUE.equals(buildParameters.get(DEPENDENCY_SCAN_ENABLED)))
        {
            ScannerType scannerType;
            if (TRUE.equals(buildParameters.get(OVERRIDE_GLOBAL_CONFIGURATIONS)))
            {
            	ret.setOsaFilterPattern(buildParameters.get(OSA_FILTER_PATTERNS));
                if("SCA".equalsIgnoreCase(buildParameters.get(DEPENDENCY_SCANNER_TYPE))) {
                	scannerType = ScannerType.AST_SCA;
                	ret.setAstScaConfig(getScaConfig(buildParameters, false));
                }
                else {
                	scannerType = ScannerType.OSA;
                	ret.setOsaArchiveIncludePatterns(buildParameters.get(OSA_ARCHIVE_INCLUDE_PATTERNS));
                    ret.setOsaRunInstall(TRUE.equals(buildParameters.get(OSA_INSTALL_BEFORE_SCAN)));
                }
                
            } else {
            	ret.setOsaFilterPattern(globalParameters.get(GLOBAL_DEPENDENCY_SCAN_FILTER_PATTERNS));
            	if("SCA".equalsIgnoreCase(globalParameters.get(GLOBAL_DEPENDENCY_SCANNER_TYPE)) ) {
            		scannerType = ScannerType.AST_SCA;
            		ret.setAstScaConfig(getScaConfig(globalParameters, true));
            	} 
            	else {
            		scannerType = ScannerType.OSA;
            		ret.setOsaArchiveIncludePatterns(buildParameters.get(GLOBAL_OSA_ARCHIVE_INCLUDE_PATTERNS));
                    ret.setOsaRunInstall(TRUE.equals(buildParameters.get(GLOBAL_EXECUTE_DEPENDENCY_MANAGER)));
            	}
            }
            if (scannerType != null) {
                ret.addScannerType(scannerType);
            }
        }



        String thresholdEnabled = THRESHOLD_ENABLED;
        String highThreshold = HIGH_THRESHOLD;
        String mediumThreshold = MEDIUM_THRESHOLD;
        String lowThreshold = LOW_THRESHOLD;

        String osaThresholdEnabled = OSA_THRESHOLD_ENABLED;
        String osaHighThreshold = OSA_HIGH_THRESHOLD;
        String osaMediumThreshold = OSA_MEDIUM_THRESHOLD;
        String osaLowThreshold = OSA_LOW_THRESHOLD;

        String isSynchronous = IS_SYNCHRONOUS;
        String enablePolicyViolation = PROJECT_POLICY_VIOLATION;
        Map<String, String> parameters = buildParameters;

        if (TRUE.equals(buildParameters.get(USE_DEFAULT_SCAN_CONTROL))) {
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


        if (ret.isSastEnabled()) {
            ret.setSastThresholdsEnabled(TRUE.equals(parameters.get(thresholdEnabled)));
            if (ret.getSastThresholdsEnabled()) {
                ret.setSastHighThreshold(convertToIntegerIfNotNull(parameters.get(highThreshold), highThreshold));
                ret.setSastMediumThreshold(convertToIntegerIfNotNull(parameters.get(mediumThreshold), mediumThreshold));
                ret.setSastLowThreshold(convertToIntegerIfNotNull(parameters.get(lowThreshold), lowThreshold));
            }
        }

        if (ret.isAstScaEnabled() || ret.isOsaEnabled()) {
            ret.setOsaThresholdsEnabled(TRUE.equals(parameters.get(osaThresholdEnabled)));
            if (ret.getOsaThresholdsEnabled()) {
                ret.setOsaHighThreshold(convertToIntegerIfNotNull(parameters.get(osaHighThreshold), osaHighThreshold));
                ret.setOsaMediumThreshold(convertToIntegerIfNotNull(parameters.get(osaMediumThreshold), osaMediumThreshold));
                ret.setOsaLowThreshold(convertToIntegerIfNotNull(parameters.get(osaLowThreshold), osaLowThreshold));
            }
        }
        return ret;
    }
    private static AstScaConfig getScaConfig(Map<String, String> parameters, boolean fromGlobal) {
		AstScaConfig scaConfig = new AstScaConfig();
		
		if(fromGlobal) {
			scaConfig.setAccessControlUrl(parameters.get(GLOBAL_SCA_ACCESS_CONTROL_URL));
            scaConfig.setWebAppUrl(parameters.get(GLOBAL_SCA_WEB_APP_URL));
            scaConfig.setApiUrl(parameters.get(GLOBAL_SCA_API_URL));
            scaConfig.setPassword(EncryptUtil.isScrambled(parameters.get(GLOBAL_SCA_PASSWORD)) ? EncryptUtil.unscramble(parameters.get(GLOBAL_SCA_PASSWORD)) : parameters.get(GLOBAL_SCA_PASSWORD));
            scaConfig.setUsername(parameters.get(GLOBAL_SCA_USERNAME));
            scaConfig.setTenant(parameters.get(GLOBAL_SCA_TENANT));
            // As we dont have include source option at global level, the flag can be set to false
            scaConfig.setIncludeSources(false);

		}else {
			scaConfig.setAccessControlUrl(parameters.get(SCA_ACCESS_CONTROL_URL));
            scaConfig.setWebAppUrl(parameters.get(SCA_WEB_APP_URL));
            scaConfig.setApiUrl(parameters.get(SCA_API_URL));
            scaConfig.setPassword(EncryptUtil.isScrambled(parameters.get(SCA_PASSWORD)) ? EncryptUtil.unscramble(parameters.get(SCA_PASSWORD)) : parameters.get(SCA_PASSWORD));
            scaConfig.setUsername(parameters.get(SCA_USERNAME));
            scaConfig.setTenant(parameters.get(SCA_TENANT));
            scaConfig.setIncludeSources(TRUE.equals(parameters.get(IS_INCLUDE_SOURCES)));
		}
		return scaConfig;
    }
    

    private static Integer convertToIntegerIfNotNull(String param, String paramName) throws InvalidParameterException {

        if (param != null && param.length() > 0) {
            try {
                int i = Integer.parseInt(param);
                if (i < 0) {
                    throw new InvalidParameterException(PARAMETER_PREFIX + paramName + PARAMETER_SUFFIX + param);
                }
                return i;


            } catch (NumberFormatException e) {
                throw new InvalidParameterException(PARAMETER_PREFIX + paramName + PARAMETER_SUFFIX + param);
            }
        }
        return null;
    }

    private static String validateNotEmpty(String param, String paramName) throws InvalidParameterException {
        if (param == null || param.length() == 0) {
            throw new InvalidParameterException(PARAMETER_PREFIX + paramName + "] must not be empty");
        }
        return param;
    }

}
