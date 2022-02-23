package com.checkmarx.teamcity.server;

import com.checkmarx.teamcity.common.CxConstants;
import com.checkmarx.teamcity.common.CxParam;
import com.cx.restclient.dto.ScannerType;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.util.PropertiesUtil;
import jetbrains.buildServer.util.StringUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.checkmarx.teamcity.common.CxConstants.*;
import static com.checkmarx.teamcity.common.CxParam.CUSTOM_FIELDS;
import static com.checkmarx.teamcity.common.CxParam.DEPENDENCY_SCANNER_TYPE;


public class CxRunTypePropertiesProcessor implements PropertiesProcessor {

    public Collection<InvalidProperty> process(Map<String, String> properties) {
        if (CxConstants.TRUE.equals(properties.get(CxParam.OSA_ENABLED))) {
            properties.put(CxParam.DEPENDENCY_SCANNER_TYPE, ScannerType.OSA.getDisplayName());
            properties.put(CxParam.DEPENDENCY_SCAN_ENABLED, CxConstants.TRUE);
            properties.put(CxParam.OVERRIDE_GLOBAL_CONFIGURATIONS, CxConstants.TRUE);
         //   properties.remove(CxParam.OSA_ENABLED);
        }
        List<InvalidProperty> result = new Vector<>();
        if (!TRUE.equals(properties.get(CxParam.USE_DEFAULT_SERVER))) {
            final String cxServerUrl = properties.get(CxParam.SERVER_URL);
            if (PropertiesUtil.isEmptyOrNull(cxServerUrl)) {
                result.add(new InvalidProperty(CxParam.SERVER_URL, URL_NOT_EMPTY_MESSAGE));
            } else {
                try {
                    new URL(cxServerUrl);
                } catch (MalformedURLException e) {
                    result.add(new InvalidProperty(CxParam.SERVER_URL, URL_NOT_VALID_MESSAGE));
                }
            }

            if (PropertiesUtil.isEmptyOrNull(properties.get(CxParam.USERNAME))) {
                result.add(new InvalidProperty(CxParam.USERNAME, USERNAME_NOT_EMPTY_MESSAGE));
            }
            if (PropertiesUtil.isEmptyOrNull(properties.get(CxParam.PASSWORD))) {
                result.add(new InvalidProperty(CxParam.PASSWORD, PASSWORD_NOT_EMPTY_MESSAGE));
            }
        }

        validateExpPathProjectDetails(properties, result);
        validateProjectName(properties.get(CxParam.PROJECT_NAME), result);
        validIncrementalSettings(properties, result);

        if (!com.intellij.openapi.util.text.StringUtil.isEmptyOrSpaces(properties.get(CUSTOM_FIELDS))) {
            validateCustomFieldsFormat(CUSTOM_FIELDS, CUSTOM_FIELDS_FORMAT_ERROR_MESSAGE, properties, result);
        }


        if (PropertiesUtil.isEmptyOrNull(properties.get(CxParam.PRESET_ID))) {
            result.add(new InvalidProperty(CxParam.PRESET_ID, PRESET_NOT_EMPTY_MESSAGE));
        }

        if (PropertiesUtil.isEmptyOrNull(properties.get(CxParam.TEAM_ID))) {
            result.add(new InvalidProperty(CxParam.TEAM_ID, TEAM_NOT_EMPTY_MESSAGE));
        }

        if(!TRUE.equals(properties.get(CxParam.USE_DEFAULT_SAST_CONFIG))) {
            validateNumericLargerThanZero(CxParam.SCAN_TIMEOUT_IN_MINUTES, properties, SCAN_TIMEOUT_POSITIVE_INTEGER_MESSAGE, result);
        }

        if(!TRUE.equals(properties.get(CxParam.USE_DEFAULT_SCAN_CONTROL))) {


            if (TRUE.equals(properties.get(CxParam.IS_SYNCHRONOUS))) {
                if (TRUE.equals(properties.get(CxParam.THRESHOLD_ENABLED))) {
                    validateNumeric(CxParam.HIGH_THRESHOLD, properties, THRESHOLD_POSITIVE_INTEGER_MESSAGE, result);
                    validateNumeric(CxParam.MEDIUM_THRESHOLD, properties, THRESHOLD_POSITIVE_INTEGER_MESSAGE, result);
                    validateNumeric(CxParam.LOW_THRESHOLD, properties, THRESHOLD_POSITIVE_INTEGER_MESSAGE, result);
                }

                boolean dependencyScanEnabled = "OSA".equals(properties.get(CxParam.DEPENDENCY_SCANNER_TYPE)) || "SCA".equals(properties.get(CxParam.DEPENDENCY_SCANNER_TYPE));
                if (dependencyScanEnabled && TRUE.equals(properties.get(CxParam.OSA_THRESHOLD_ENABLED))) {
                    validateNumeric(CxParam.OSA_HIGH_THRESHOLD, properties, THRESHOLD_POSITIVE_INTEGER_MESSAGE, result);
                    validateNumeric(CxParam.OSA_MEDIUM_THRESHOLD, properties, THRESHOLD_POSITIVE_INTEGER_MESSAGE, result);
                    validateNumeric(CxParam.OSA_LOW_THRESHOLD, properties, THRESHOLD_POSITIVE_INTEGER_MESSAGE, result);
                }
            }
        }

        return result;
    }

    private void validateCustomFieldsFormat(String parameterName, String errorMessage, Map<String, String> properties, List<InvalidProperty> result) {
        Pattern pattern = Pattern.compile("(^([a-zA-Z0-9]*):([a-zA-Z0-9]*)+(,([a-zA-Z0-9]*):([a-zA-Z0-9]*)+)*$)");
        Matcher match = pattern.matcher(properties.get(parameterName));
        if (!match.find()) {
            result.add(new InvalidProperty(parameterName, errorMessage));
        }
    }

    private void validIncrementalSettings(Map<String, String> properties, List<InvalidProperty> result) {
		if (TRUE.equals(properties.get(CxParam.IS_INCREMENTAL)) && TRUE.equals(properties.get(CxParam.PERIODIC_FULL_SCAN))) {
			if(!validateRange(properties.get(CxParam.PERIODIC_FULL_SCAN_AFTER), FULL_SCAN_CYCLE_MIN, FULL_SCAN_CYCLE_MAX))
	            result.add(new InvalidProperty(CxParam.PERIODIC_FULL_SCAN_AFTER, WRONG_PERIODIC_FULL_SCAN_INTERVAL));
		}
	}

	private void validateExpPathProjectDetails(Map<String, String> properties, List<InvalidProperty> result) {
		if (TRUE.equals(properties.get(CxParam.DEPENDENCY_SCAN_ENABLED))) {
			if (TRUE.equals(properties.get(CxParam.OVERRIDE_GLOBAL_CONFIGURATIONS))) {
				if ("SCA".equalsIgnoreCase(properties.get(DEPENDENCY_SCANNER_TYPE))) {
					if (TRUE.equals(properties.get(CxParam.IS_EXPLOITABLE_PATH))) {
						if ((PropertiesUtil.isEmptyOrNull(properties.get(CxParam.SCA_SAST_PROJECT_FULLPATH))) && 
								(PropertiesUtil.isEmptyOrNull(properties.get(CxParam.SCA_SAST_PROJECT_ID))) ) {
			                result.add(new InvalidProperty(CxParam.SCA_SAST_PROJECT_FULLPATH, PROJECT_FULLPATH_PROJECT_ID_NOT_EMPTY_MESSAGE));
			            }
					}

				}
			}
		}
	}

	private void validateProjectName(String projectName, List<InvalidProperty> result) {
        if (PropertiesUtil.isEmptyOrNull(projectName)) {
            result.add(new InvalidProperty(CxParam.PROJECT_NAME, PROJECT_NAME_NOT_EMPTY_MESSAGE));
            return;
        }

        Pattern pattern = Pattern.compile("[/?<>\\:*|\"]");

        Matcher matcher = pattern.matcher(projectName);
        if (matcher.find()) {
            result.add(new InvalidProperty(CxParam.PROJECT_NAME, PROJECT_NAME_ILLEGAL_CHARS_MESSAGE));
            return;
        }

        if(projectName.length() > 200) {
            result.add(new InvalidProperty(CxParam.PROJECT_NAME, PROJECT_NAME_MAX_CHARS_MESSAGE));
            return;
        }
    }

    private void validateNumeric(String parameterName,  Map<String, String> properties, String errorMessage, List<InvalidProperty> result) {
        String num = properties.get(parameterName);
        if (!com.intellij.openapi.util.text.StringUtil.isEmptyOrSpaces(num) && (!StringUtil.isNumber(num) || (Integer.parseInt(num) < 0))) {
            result.add(new InvalidProperty(parameterName, errorMessage));
        }
    }

    private void validateNumericLargerThanZero(String parameterName,  Map<String, String> properties, String errorMessage, List<InvalidProperty> result) {
        String num = properties.get(parameterName);
        if (!com.intellij.openapi.util.text.StringUtil.isEmptyOrSpaces(num) && (!StringUtil.isNumber(num) || (Integer.parseInt(num) <= 0))) {
            result.add(new InvalidProperty(parameterName, errorMessage));
        }
    }
    
    private boolean validateNumber(String num ) {
    	boolean valid = true;    	
    	try {
    			Integer.parseInt(num);    		
    	}catch(Exception wrongNumber) {
    		valid = false;
    	}
    	return valid;
    }
    
    private boolean validateRange(String num, int min, int max) {
    	boolean withinRange = false;
		if (validateNumber(num)) {

			int tobechecked = Integer.parseInt(num);
			if (tobechecked >= min && tobechecked <= max)
				withinRange = true;
		}
    	return withinRange;	
    }
    
}
