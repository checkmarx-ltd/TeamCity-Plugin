package com.checkmarx.teamcity.agent;

import com.checkmarx.teamcity.common.CxUtility;
import com.checkmarx.teamcity.common.InvalidParameterException;
import com.cx.restclient.ast.dto.sca.AstScaConfig;
import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.dto.ScannerType;
import com.cx.restclient.sast.utils.LegacyClient;
import com.cx.restclient.exception.CxClientException;
import com.cx.restclient.sca.utils.CxSCAFileSystemUtils;
import jetbrains.buildServer.agent.AgentRunningBuild;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.checkmarx.teamcity.common.CxConstants.*;
import static com.checkmarx.teamcity.common.CxParam.*;
import static com.checkmarx.teamcity.common.CxUtility.decrypt;


/**
 * Created by eyala on 6/12/2018.
 */
public class CxConfigHelper {

    private static final String PARAMETER_PREFIX = "Parameter [";
    private static final String PARAMETER_SUFFIX = "] must be positive integer. Actual value: ";
    private static String teamPath;
    private static LegacyClient commonClient = null;
    public static CxScanConfig resolveConfigurations(Map<String, String> buildParameters, Map<String, String> globalParameters, File checkoutDirectory,
                                                     File reportDirectory,  Map<String,String> otherParameters, AgentRunningBuild agentRunningBuild, CxLoggerAdapter logger) throws InvalidParameterException, UnsupportedEncodingException {


        CxScanConfig ret = new CxScanConfig();
        //to support builds that were configured before this parameter, allow sast scan if parameter is null.
        ret.setSastEnabled(buildParameters.get(SAST_ENABLED) == null || TRUE.equals(buildParameters.get(SAST_ENABLED)));
        
        String originUrl = CxUtility.getCxOriginUrl(agentRunningBuild);
        ret.setCxOriginUrl(originUrl);
        String cxOrigin = CxUtility.getCxOrigin(agentRunningBuild);
        ret.setCxOrigin(cxOrigin);
        logger.info("CxOrigin : "+ cxOrigin);
        logger.info("CxOrigin URL : "+ originUrl);
        
        ret.setSourceDir(checkoutDirectory.getAbsolutePath());
        ret.setReportsDir(reportDirectory);
        String isProxyVar = System.getProperty("cx.isproxy");
        ret.setProxy(StringUtils.isNotEmpty(isProxyVar) && isProxyVar.equalsIgnoreCase("true"));

        if (TRUE.equals(buildParameters.get(USE_DEFAULT_SERVER))) {
            ret.setUrl(validateNotEmpty(globalParameters.get(GLOBAL_SERVER_URL), GLOBAL_SERVER_URL));
            ret.setUsername(validateNotEmpty(globalParameters.get(GLOBAL_USERNAME), GLOBAL_USERNAME));
            ret.setPassword(decrypt(validateNotEmpty(globalParameters.get(GLOBAL_PASSWORD), GLOBAL_PASSWORD)));
        } else {
            ret.setUrl(validateNotEmpty(buildParameters.get(SERVER_URL), SERVER_URL));
            ret.setUsername(validateNotEmpty(buildParameters.get(USERNAME), USERNAME));
            ret.setPassword(decrypt(validateNotEmpty(buildParameters.get(PASSWORD), PASSWORD)));
        }


        ret.setProjectName(validateNotEmpty(buildParameters.get(PROJECT_NAME), PROJECT_NAME));
        ret.setPresetId(convertToIntegerIfNotNull(buildParameters.get(PRESET_ID), PRESET_ID));
        ret.setTeamId(validateNotEmpty(buildParameters.get(TEAM_ID), TEAM_ID));
        try {
        	initializeCommonClient(ret, logger);
        	commonClient.login();
			teamPath = commonClient.getTeamNameById(buildParameters.get(TEAM_ID));
		} catch (Exception e) {
            logger.error("Failed to get team name by team id: " + e.getMessage());
        } finally {
            if (commonClient != null) {
                commonClient.close();
            }
        }
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
            
            String periodicFullScan = (buildParameters.get(PERIODIC_FULL_SCAN) == null)? FALSE: buildParameters.get(PERIODIC_FULL_SCAN);
            int fullScanAfterNumberOfBuilds = -1;
            if(TRUE.equalsIgnoreCase(periodicFullScan))
            	fullScanAfterNumberOfBuilds = convertToIntegerIfNotNull(buildParameters.get(PERIODIC_FULL_SCAN_AFTER), PERIODIC_FULL_SCAN_AFTER);
            
            ret.setIncremental(isThisBuildIncremental(otherParameters.get(CX_BUILD_NUMBER),buildParameters.get(IS_INCREMENTAL),periodicFullScan, fullScanAfterNumberOfBuilds));

            ret.setCustomFields(customFieldFormat(buildParameters.get(CUSTOM_FIELDS)));

            /* Added support for Engine Configuration Id when Engine configuration ID is "Project Default"  i.e. 0
            then Project will get scanned as per SAST set configuration Id.
            */
            Integer engConfigId = convertToIntegerIfNotNull(buildParameters.get(ENGINE_CONFIG_ID), ENGINE_CONFIG_ID);

            ret.setEngineConfigurationId(engConfigId);

            ret.setGeneratePDFReport(TRUE.equals(buildParameters.get(GENERATE_PDF_REPORT)));
        }


        if (TRUE.equals(buildParameters.get(DEPENDENCY_SCAN_ENABLED)))
        {
            ScannerType scannerType = null;
            if (TRUE.equals(buildParameters.get(OVERRIDE_GLOBAL_CONFIGURATIONS)))
            {
            	ret.setOsaFilterPattern(buildParameters.get(OSA_FILTER_PATTERNS));
                if("SCA".equalsIgnoreCase(buildParameters.get(DEPENDENCY_SCANNER_TYPE))) {
                	scannerType = ScannerType.AST_SCA;
                	ret.setAstScaConfig(getScaConfig(buildParameters,globalParameters, false));
                }
                else {
                	scannerType = ScannerType.OSA;
                	ret.setOsaArchiveIncludePatterns(buildParameters.get(OSA_ARCHIVE_INCLUDE_PATTERNS));
                    ret.setOsaRunInstall(TRUE.equals(buildParameters.get(OSA_INSTALL_BEFORE_SCAN)));
                }
                
            } else {
            	
            	if(TRUE.equals(globalParameters.get(GLOBAL_DEFINE_DEPENDENCY_SCAN_SETTINGS))){
            	ret.setOsaFilterPattern(globalParameters.get(GLOBAL_DEPENDENCY_SCAN_FILTER_PATTERNS));
            	if("SCA".equalsIgnoreCase(globalParameters.get(GLOBAL_DEPENDENCY_SCANNER_TYPE)) ) {
            		scannerType = ScannerType.AST_SCA;
            		ret.setAstScaConfig(getScaConfig(buildParameters,globalParameters, true));
            	} 
            	else {
            		scannerType = ScannerType.OSA;
            		ret.setOsaArchiveIncludePatterns(globalParameters.get(GLOBAL_OSA_ARCHIVE_INCLUDE_PATTERNS));
                    ret.setOsaRunInstall(TRUE.equals(globalParameters.get(GLOBAL_EXECUTE_DEPENDENCY_MANAGER)));
            	}
            }
            }
            if (scannerType != null) {
                ret.addScannerType(scannerType);
            }
        }



        String thresholdEnabled = THRESHOLD_ENABLED;
        String enableCriticalSeverity = ENABLE_CRITICAL_SEVERITY;
        String criticalThreshold = CRITICAL_THRESHOLD;
        String highThreshold = HIGH_THRESHOLD;
        String mediumThreshold = MEDIUM_THRESHOLD;
        String lowThreshold = LOW_THRESHOLD;

        String osaThresholdEnabled = OSA_THRESHOLD_ENABLED;
        String osaCriticalThreshold = OSA_CRITICAL_THRESHOLD;
        String osaHighThreshold = OSA_HIGH_THRESHOLD;
        String osaMediumThreshold = OSA_MEDIUM_THRESHOLD;
        String osaLowThreshold = OSA_LOW_THRESHOLD;

        String isSynchronous = IS_SYNCHRONOUS;
        String enablePolicyViolation = PROJECT_POLICY_VIOLATION;
        String enableSCAPolicyViolation = PROJECT_SCA_POLICY_VIOLATION;
        Map<String, String> parameters = buildParameters;

        if (TRUE.equals(buildParameters.get(USE_DEFAULT_SCAN_CONTROL))) {
            thresholdEnabled = GLOBAL_THRESHOLD_ENABLED;
            enableCriticalSeverity = GLOBAL_ENABLE_CRITICAL_SEVERITY;
            criticalThreshold = GLOBAL_CRITICAL_THRESHOLD;
            highThreshold = GLOBAL_HIGH_THRESHOLD;
            mediumThreshold = GLOBAL_MEDIUM_THRESHOLD;
            lowThreshold = GLOBAL_LOW_THRESHOLD;

            osaThresholdEnabled = GLOBAL_OSA_THRESHOLD_ENABLED;
            osaCriticalThreshold = GLOBAL_OSA_CRITICAL_THRESHOLD;
            osaHighThreshold = GLOBAL_OSA_HIGH_THRESHOLD;
            osaMediumThreshold = GLOBAL_OSA_MEDIUM_THRESHOLD;
            osaLowThreshold = GLOBAL_OSA_LOW_THRESHOLD;

            isSynchronous = GLOBAL_IS_SYNCHRONOUS;
            enablePolicyViolation = GLOBAL_PROJECT_POLICY_VIOLATION;
            enableSCAPolicyViolation = GLOBAL_PROJECT_SCA_POLICY_VIOLATION;


            parameters = globalParameters;
        }

        ret.setSynchronous(TRUE.equals(parameters.get(isSynchronous)));
        ret.setEnablePolicyViolations(TRUE.equals(parameters.get(enablePolicyViolation)));
        ret.setEnablePolicyViolationsSCA(TRUE.equals(parameters.get(enableSCAPolicyViolation)));


        if (ret.isSastEnabled()) {
            ret.setSastThresholdsEnabled(TRUE.equals(parameters.get(thresholdEnabled)));
            if (ret.getSastThresholdsEnabled()) {
            	ret.setSastEnableCriticalSeverity(TRUE.equals(parameters.get(enableCriticalSeverity)));
            	ret.setSastCriticalThreshold(convertToIntegerIfNotNull(parameters.get(criticalThreshold), criticalThreshold));
                ret.setSastHighThreshold(convertToIntegerIfNotNull(parameters.get(highThreshold), highThreshold));
                ret.setSastMediumThreshold(convertToIntegerIfNotNull(parameters.get(mediumThreshold), mediumThreshold));
                ret.setSastLowThreshold(convertToIntegerIfNotNull(parameters.get(lowThreshold), lowThreshold));
            }
        }

        if (ret.isAstScaEnabled() || ret.isOsaEnabled()) {
            ret.setOsaThresholdsEnabled(TRUE.equals(parameters.get(osaThresholdEnabled)));
            if (ret.getOsaThresholdsEnabled()) {
            	ret.setOsaCriticalThreshold(convertToIntegerIfNotNull(parameters.get(osaCriticalThreshold), osaCriticalThreshold));
                ret.setOsaHighThreshold(convertToIntegerIfNotNull(parameters.get(osaHighThreshold), osaHighThreshold));
                ret.setOsaMediumThreshold(convertToIntegerIfNotNull(parameters.get(osaMediumThreshold), osaMediumThreshold));
                ret.setOsaLowThreshold(convertToIntegerIfNotNull(parameters.get(osaLowThreshold), osaLowThreshold));
            }
        }
        return ret;
    }

    private static void initializeCommonClient(CxScanConfig config, CxLoggerAdapter logger) {
    	try {
    	commonClient = CommonClientFactory.getInstance(config, logger);
    } catch (Exception e) {
        logger.debug("Failed to initialize cx client " + e.getMessage(), e);
        commonClient = null;
    }		
	}

    private static String customFieldFormat(String customFields) {
        if(customFields != null && !customFields.isEmpty()) {
            customFields = customFields.replaceAll(":", "\":\"");
            customFields = customFields.replaceAll(",", "\",\"");
            customFields = "{\"".concat(customFields).concat("\"}");
        }
        return customFields;
    }

    private static AstScaConfig getScaConfig(Map<String, String> buildParameters, Map<String, String> globalParameters, boolean fromGlobal) throws InvalidParameterException{
		AstScaConfig scaConfig = new AstScaConfig();
		
		if(fromGlobal) {
			scaConfig.setAccessControlUrl(globalParameters.get(GLOBAL_SCA_ACCESS_CONTROL_URL));
            scaConfig.setWebAppUrl(globalParameters.get(GLOBAL_SCA_WEB_APP_URL));
            scaConfig.setApiUrl(globalParameters.get(GLOBAL_SCA_API_URL));
            scaConfig.setPassword(decrypt(globalParameters.get(GLOBAL_SCA_PASSWORD)));
            scaConfig.setUsername(globalParameters.get(GLOBAL_SCA_USERNAME));
            scaConfig.setTenant(globalParameters.get(GLOBAL_SCA_TENANT));
            // As we dont have include source option at global level, the flag can be set to false
            scaConfig.setIncludeSources(false);
            String scaEnvVars = globalParameters.get(GLOBAL_SCA_ENV_VARIABLE);

            if(StringUtils.isNotEmpty(scaEnvVars))
            {
            	scaConfig.setEnvVariables(CxSCAFileSystemUtils.convertStringToKeyValueMap(scaEnvVars));
            }
            String configFilePaths = globalParameters.get(GLOBAL_SCA_CONFIGFILE);
			if (StringUtils.isNotEmpty(configFilePaths)) {
				String[] strArrayFile = configFilePaths.split(",");
				List<String> trimmedConfigPaths = getTrimmedConfigPaths(strArrayFile);
				scaConfig.setConfigFilePaths(trimmedConfigPaths);
			}
		}else {
			scaConfig.setAccessControlUrl(buildParameters.get(SCA_ACCESS_CONTROL_URL));
            scaConfig.setWebAppUrl(buildParameters.get(SCA_WEB_APP_URL));
            scaConfig.setApiUrl(buildParameters.get(SCA_API_URL));
            scaConfig.setPassword(decrypt(buildParameters.get(SCA_PASSWORD)));
            scaConfig.setUsername(buildParameters.get(SCA_USERNAME));
            scaConfig.setTenant(buildParameters.get(SCA_TENANT));
            
            if(!StringUtils.isEmpty(buildParameters.get(SCA_TEAMPATH))) {
            scaConfig.setTeamPath(buildParameters.get(SCA_TEAMPATH));
            } else {
            	scaConfig.setTeamPath(teamPath);
            }
            scaConfig.setIncludeSources(TRUE.equals(buildParameters.get(IS_INCLUDE_SOURCES)));
            String scaEnvVars = buildParameters.get(SCA_ENV_VARIABLE);

            //add SCA Resolver code here
            if (buildParameters.get(DEPENDENCY_SCA_SCAN_TYPE) != null
                    && "SCAResolver".equalsIgnoreCase(buildParameters.get(DEPENDENCY_SCA_SCAN_TYPE))) {
                validateScaResolverParams(buildParameters.get(SCA_RESOLVER_ADD_PARAMETERS));
                scaConfig.setEnableScaResolver(true);
            }
            else
                scaConfig.setEnableScaResolver(false);

            scaConfig.setPathToScaResolver(buildParameters.get(SCA_RESOLVER_PATH));
            scaConfig.setScaResolverAddParameters(buildParameters.get(SCA_RESOLVER_ADD_PARAMETERS));

            if(StringUtils.isNotEmpty(scaEnvVars))
            {
            	scaConfig.setEnvVariables(CxSCAFileSystemUtils.convertStringToKeyValueMap(scaEnvVars));
            }
            String configFilePaths = buildParameters.get(SCA_CONFIGFILE);
			if (StringUtils.isNotEmpty(configFilePaths)) {
				String[] strArrayFile = configFilePaths.split(",");
				List<String> trimmedConfigPaths = getTrimmedConfigPaths(strArrayFile);
				scaConfig.setConfigFilePaths(trimmedConfigPaths);
			}
		}
		return scaConfig;
    }

    private static boolean scaResolverPathExist(String pathToResolver) {
        pathToResolver = pathToResolver + File.separator + "ScaResolver";
        if(!SystemUtils.IS_OS_UNIX)
            pathToResolver = pathToResolver + ".exe";

        File file = new File(pathToResolver);
        if(!file.exists())
        {
            throw new CxClientException("SCA Resolver path does not exist. Path="+file.getAbsolutePath());
        }
        return true;
    }

    private static void validateScaResolverParams(String additionalParams) {

        String[] arguments = additionalParams.split(" ");
        Map<String, String> params = new HashMap<>();

        for (int i = 0; i <  arguments.length ; i++) {
            if(arguments[i].startsWith("-") && (i+1 != arguments.length && !arguments[i+1].startsWith("-")))
                params.put(arguments[i], arguments[i+1]);
            else
                params.put(arguments[i], "");
        }

        String dirPath = params.get("-s");
        if(StringUtils.isEmpty(dirPath))
            throw new CxClientException("Source code path (-s <source code path>) is not provided.");
//        fileExists(dirPath);

        String projectName = params.get("-n");
        if(StringUtils.isEmpty(projectName))
            throw new CxClientException("Project name parameter (-n <project name>) must be provided to ScaResolver.");

    }

    private static void fileExists(String file) {

        File resultPath = new File(file);
        if (!resultPath.exists()) {
            throw new CxClientException("Path does not exist. Path= " + resultPath.getAbsolutePath());
        }
    }

    private static List<String> getTrimmedConfigPaths(String[] strArrayFile) {
    	List<String> paths = new ArrayList<String>();
    	for (int i = 0; i < strArrayFile.length; i++) {
    		paths.add(strArrayFile[i].trim());
		}
		return paths;
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
    
    private static boolean isThisBuildIncremental(String buildNumber, String isIncremental, String isPeriodicFullScan, int fullScanAfter ) {

        boolean askedForIncremental = TRUE.equalsIgnoreCase(isIncremental);
        if (!askedForIncremental) {
            return false;
        }

        boolean askedForPeriodicFullScans = TRUE.equalsIgnoreCase(isPeriodicFullScan);
        if (!askedForPeriodicFullScans) {
            return true;
        }

        // if user entered invalid value for full scan cycle - all scans will be incremental
        if (fullScanAfter < FULL_SCAN_CYCLE_MIN || fullScanAfter > FULL_SCAN_CYCLE_MAX) {
            return true;
        }

        int currentBuildNumer = -1;
        try {		
        	currentBuildNumer = Integer.parseInt(buildNumber);
        }catch(Exception wrongNumber) {
        	return true;
        }
        // If user asked to perform full scan after every 9 incremental scans -
        // it means that every 10th scan should be full,
        // that is the ordinal numbers of full scans will be "1", "11", "21" and so on...
        boolean shouldBeFullScan = currentBuildNumer % (fullScanAfter + 1) == 1;

        return !shouldBeFullScan;
    }

}