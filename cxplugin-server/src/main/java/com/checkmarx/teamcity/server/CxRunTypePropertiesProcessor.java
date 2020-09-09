package com.checkmarx.teamcity.server;

import com.checkmarx.teamcity.common.CxParam;
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


public class CxRunTypePropertiesProcessor implements PropertiesProcessor {

    public Collection<InvalidProperty> process(Map<String, String> properties) {
        PluginDataMigration migration = new PluginDataMigration();
        migration.migrate(properties);
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

        validateProjectName(properties.get(CxParam.PROJECT_NAME), result);


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
}
