package com.checkmarx.teamcity.server;


import jetbrains.buildServer.controllers.ActionErrors;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.controllers.BaseFormXmlController;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.crypt.RSACipher;
import jetbrains.buildServer.util.StringUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import static com.checkmarx.teamcity.common.CxConstants.*;
import static com.checkmarx.teamcity.common.CxParam.*;
import static com.checkmarx.teamcity.common.CxUtility.encrypt;
import static com.checkmarx.teamcity.common.CxUtility.decrypt;
import com.checkmarx.teamcity.common.SASTUtils;


public class CxAdminPageController extends BaseFormXmlController {
	
	private static final Logger log = LoggerFactory.getLogger(CxAdminPageController.class);

    public static final String INVALID = "invalid_";
    private final CxAdminConfig cxAdminConfig;


    public CxAdminPageController(@NotNull final CxAdminConfig cxAdminConfig) {
        this.cxAdminConfig = cxAdminConfig;
    }

    @Override
    protected ModelAndView doGet(@NotNull final HttpServletRequest request, @NotNull final HttpServletResponse response) {
        return null;
    }

    @Override
    protected void doPost(@NotNull final HttpServletRequest request, @NotNull final HttpServletResponse response, @NotNull final Element xmlResponse) {

        ActionErrors actionErrors = validateForm(request);
        if(actionErrors.hasErrors()) {
            actionErrors.serialize(xmlResponse);
            return;
        }
        
        Loggers.SERVER.info("Received request parameters: " + request.getParameterMap());
        System.out.println("Received request parameters: " + request.getParameterMap());

        for (String config : GLOBAL_CONFIGS) {
            cxAdminConfig.setConfiguration(config, StringUtil.emptyIfNull(request.getParameter(config)));
            Loggers.SERVER.info("Set configuration for: " + config);
        }


        String sastAndOsaPassword = ensurePasswordEncryption(request, "encryptedCxGlobalPassword");
        Loggers.SERVER.info("sast And OsaPassword: " + sastAndOsaPassword);
        cxAdminConfig.setConfiguration(GLOBAL_PASSWORD, sastAndOsaPassword);
        System.out.println("Set SAST and OSA Password");
        Loggers.SERVER.info("Set SAST and OSA Password");

        String scaPassword = ensurePasswordEncryption(request, "encryptedCxGlobalSCAPassword");
        cxAdminConfig.setConfiguration(GLOBAL_SCA_PASSWORD, scaPassword);
        System.out.println("Set SCA Password");
        Loggers.SERVER.info("Set SCA Password");
        
        String sastPassword = ensurePasswordEncryption(request, "encryptedCxGlobalSastPassword");
        Loggers.SERVER.info("sast Password: " + sastPassword);
        cxAdminConfig.setConfiguration(GLOBAL_SAST_SERVER_PASSWORD, sastPassword);

//        try {
//            //cxAdminConfig.persistConfiguration();
//            Loggers.SERVER.info("Successfully persisted global configurations");
//            System.out.println("Successfully persisted global configurations");
//        } catch (IOException e) {
//            Loggers.SERVER.error("Failed to persist global configurations", e);
//            System.out.println("Failed to persist global configurations: " + e.getMessage());
//        }
        getOrCreateMessages(request).addMessage("settingsSaved", SUCCESSFUL_SAVE_MESSAGE);

        String globalIsSynchronous = request.getParameter(GLOBAL_IS_SYNCHRONOUS);
        String globalThresholdsEnabled = request.getParameter(GLOBAL_THRESHOLD_ENABLED);
        String globalServerUrl = request.getParameter(GLOBAL_SERVER_URL);
        String globalUsername = request.getParameter(GLOBAL_USERNAME);
        String globalPss = request.getParameter(GLOBAL_PASSWORD);
        Loggers.SERVER.info("Password after: " + globalPss);
        String globalEnableCriticalSeverity = request.getParameter(GLOBAL_ENABLE_CRITICAL_SEVERITY);
        String globalCriticalThreshold = request.getParameter(GLOBAL_CRITICAL_THRESHOLD);
        //String password = ensurePasswordEncryption(request, "encryptedCxGlobalPassword");
        Loggers.SERVER.info("global Enable Critical Severity: " + globalEnableCriticalSeverity +"******");
        if (globalCriticalThreshold == null) {
        	   Loggers.SERVER.warn("globalCriticalThreshold is null");
        	}
        	if (globalEnableCriticalSeverity == null) {
        	   Loggers.SERVER.warn("globalEnableCriticalSeverity is null");
        	}
        	
        Loggers.SERVER.info("Global is synchronous: " + globalIsSynchronous);
        Loggers.SERVER.info("Global thresholds enabled: " + globalThresholdsEnabled);
        
        if ("true".equals(globalIsSynchronous)) {
            if ("true".equals(globalThresholdsEnabled)) {
                Double version = 9.0;
                // Invoke API to fetch SAST version
                try {
                    String sastVersion = SASTUtils.loginToServer(new URL(globalServerUrl), globalUsername, decrypt(sastAndOsaPassword));
                    Loggers.SERVER.info("Fetched SAST version: " + sastVersion);
                    System.out.println("Fetched SAST version: " + sastVersion);
                    String[] sastVersionSplit = sastVersion.split("\\.");
                    Loggers.SERVER.info("SAST version split: " + Arrays.toString(sastVersionSplit));
                    if (sastVersionSplit.length > 1) {
                        Loggers.SERVER.info("SAST version split[0]: " + sastVersionSplit[0]);
                        Loggers.SERVER.info("SAST version split[1]: " + sastVersionSplit[1]);
                        version = Double.parseDouble(sastVersionSplit[0] + "." + sastVersionSplit[1]);
                        Loggers.SERVER.info("Parsed SAST version: " + version);
                        
                }else {
                    Loggers.SERVER.error("Unexpected SAST version format: " + sastVersion);
                }
            } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error fetching SAST version: " + e.getMessage());
                    Loggers.SERVER.error("Error fetching SAST version: " + e.getMessage());
                }
                if (version >= 9.7) {
                	Loggers.SERVER.info("version greater or equal to 9.7:"+globalEnableCriticalSeverity);
                    if (globalEnableCriticalSeverity == null || OPTION_FALSE.equalsIgnoreCase(globalEnableCriticalSeverity)) {
                        globalEnableCriticalSeverity = OPTION_TRUE;
                        globalCriticalThreshold = "";
                        getOrCreateMessages(request).addMessage("criticalSeverityMessage", CRITICAL_SEVERITY_MESSAGE);
                        //actionErrors.addError(new InvalidProperty(GLOBAL_ENABLE_CRITICAL_SEVERITY, "The configured SAST version supports Critical severity. Critical threshold can also be configured."));
                        //getOrCreateMessages(request).addMessage("settingsSaved", SUCCESSFUL_SAVE_MESSAGE);
                        System.out.println("SAST version supports Critical severity. Critical threshold can also be configured.");
                        Loggers.SERVER.info("SAST version supports Critical severity. Critical threshold can also be configured.");
                    }
                } else {
                	Loggers.SERVER.info("version less than 9.7:"+globalEnableCriticalSeverity);
                    if (OPTION_TRUE.equalsIgnoreCase(globalEnableCriticalSeverity)) {
                        globalEnableCriticalSeverity = OPTION_FALSE;
                        globalCriticalThreshold = "";
                        getOrCreateMessages(request).addMessage("criticalSeverityErrorMessage", CRITICAL_SEVERITY_ERROR_MESSAGE);
                        //actionErrors.addError(new InvalidProperty(GLOBAL_ENABLE_CRITICAL_SEVERITY, "The configured SAST version does not support Critical severity. Critical threshold will not be applicable."));
                        System.out.println("SAST version does not support Critical severity. Critical threshold will not be applicable.");
                        Loggers.SERVER.info("SAST version does not support Critical severity. Critical threshold will not be applicable.");
                    }
                }
                
            }
        }
        cxAdminConfig.setConfiguration(GLOBAL_CRITICAL_THRESHOLD, globalCriticalThreshold);
        Loggers.SERVER.info("Set global critical threshold: " + globalCriticalThreshold);
        cxAdminConfig.setConfiguration(GLOBAL_ENABLE_CRITICAL_SEVERITY, globalEnableCriticalSeverity);
        Loggers.SERVER.info("Set global enable critical severity: " + globalEnableCriticalSeverity);
        
        try {
            cxAdminConfig.persistConfiguration();
            Loggers.SERVER.info("Successfully persisted configurations after updates");
        } catch (IOException e) {
            Loggers.SERVER.error("Failed to persist configurations after updates", e);
        }
        
        if (actionErrors.hasErrors()) {
        	Loggers.SERVER.error("Action errors found: " + actionErrors.getErrors());
            System.out.println("Action errors found: " + actionErrors.getErrors());
            actionErrors.serialize(xmlResponse);
        }else {
            Loggers.SERVER.info("Configuration settings saved successfully");
            System.out.println("Configuration settings saved successfully");
        }
    }

    private String ensurePasswordEncryption(HttpServletRequest request, String requestParamName) {
        String password = RSACipher.decryptWebRequestData(request.getParameter(requestParamName));
        return encrypt(password);
    }

    private ActionErrors validateForm(final HttpServletRequest request) {

        ActionErrors ret = new ActionErrors();

        String cxGlobalServerUrl = request.getParameter(GLOBAL_SERVER_URL);
        if (com.intellij.openapi.util.text.StringUtil.isEmptyOrSpaces(cxGlobalServerUrl)) {
            ret.addError(INVALID + GLOBAL_SERVER_URL, URL_NOT_EMPTY_MESSAGE);
        } else {
            try {
                new URL(cxGlobalServerUrl);
            } catch (MalformedURLException e) {
                ret.addError(INVALID + GLOBAL_SERVER_URL, URL_NOT_VALID_MESSAGE);
            }
        }

        if (com.intellij.openapi.util.text.StringUtil.isEmptyOrSpaces(request.getParameter(GLOBAL_USERNAME))) {
            ret.addError(INVALID + GLOBAL_USERNAME, USERNAME_NOT_EMPTY_MESSAGE);
        }

        if (com.intellij.openapi.util.text.StringUtil.isEmptyOrSpaces(request.getParameter("encryptedCxGlobalPassword"))) {
            ret.addError(INVALID + GLOBAL_PASSWORD, PASSWORD_NOT_EMPTY_MESSAGE);
        }

        validateNumericLargerThanZero(GLOBAL_SCAN_TIMEOUT_IN_MINUTES, SCAN_TIMEOUT_POSITIVE_INTEGER_MESSAGE, request, ret);

        if(TRUE.equals(request.getParameter(GLOBAL_IS_EXPLOITABLE_PATH))){
        	
        	String cxGlobalSastServerUrl = request.getParameter(GLOBAL_SAST_SERVER_URL);
            if (com.intellij.openapi.util.text.StringUtil.isEmptyOrSpaces(cxGlobalSastServerUrl)) {
                ret.addError(INVALID + GLOBAL_SAST_SERVER_URL, SAST_URL_NOT_EMPTY_MESSAGE);
            } else {
                try {
                    new URL(cxGlobalSastServerUrl);
                } catch (MalformedURLException e) {
                    ret.addError(INVALID + GLOBAL_SAST_SERVER_URL, SAST_URL_NOT_VALID_MESSAGE);
                }
            }

            if (com.intellij.openapi.util.text.StringUtil.isEmptyOrSpaces(request.getParameter(GLOBAL_SAST_SERVER_USERNAME))) {
                ret.addError(INVALID + GLOBAL_SAST_SERVER_USERNAME, SAST_USERNAME_NOT_EMPTY_MESSAGE);
            }
        	
        	if (com.intellij.openapi.util.text.StringUtil.isEmptyOrSpaces(request.getParameter("encryptedCxGlobalSastPassword"))) {
                ret.addError(INVALID + GLOBAL_SAST_SERVER_PASSWORD, SAST_PASSWORD_NOT_EMPTY_MESSAGE);
            }
        }

        if(TRUE.equals(request.getParameter(GLOBAL_IS_SYNCHRONOUS))) {

            if(TRUE.equals(request.getParameter(GLOBAL_THRESHOLD_ENABLED))) {
            	validateNumeric(GLOBAL_CRITICAL_THRESHOLD, THRESHOLD_POSITIVE_INTEGER_MESSAGE, request, ret);
                validateNumeric(GLOBAL_HIGH_THRESHOLD, THRESHOLD_POSITIVE_INTEGER_MESSAGE, request, ret);
                validateNumeric(GLOBAL_MEDIUM_THRESHOLD, THRESHOLD_POSITIVE_INTEGER_MESSAGE, request, ret);
                validateNumeric(GLOBAL_LOW_THRESHOLD, THRESHOLD_POSITIVE_INTEGER_MESSAGE, request, ret);
            }

            if(TRUE.equals(request.getParameter(GLOBAL_OSA_THRESHOLD_ENABLED))) {
            	validateNumeric(GLOBAL_OSA_CRITICAL_THRESHOLD,THRESHOLD_POSITIVE_INTEGER_MESSAGE, request, ret);
                validateNumeric(GLOBAL_OSA_HIGH_THRESHOLD, THRESHOLD_POSITIVE_INTEGER_MESSAGE, request, ret);
                validateNumeric(GLOBAL_OSA_MEDIUM_THRESHOLD,THRESHOLD_POSITIVE_INTEGER_MESSAGE, request, ret);
                validateNumeric(GLOBAL_OSA_LOW_THRESHOLD, THRESHOLD_POSITIVE_INTEGER_MESSAGE, request, ret);
            }
        }
        
        log.info("Validation completed with errors: {}", ret.hasErrors());

        return ret;
    }

    private void validateNumeric(String parameterName, String errorMessage, HttpServletRequest request, ActionErrors errors) {
        String num = request.getParameter(parameterName);
        if (!com.intellij.openapi.util.text.StringUtil.isEmptyOrSpaces(num) && (!StringUtil.isNumber(num) || (Integer.parseInt(num) < 0))) {
                errors.addError(INVALID + parameterName, errorMessage);
        }
    }

    private void validateNumericLargerThanZero(String parameterName, String errorMessage, HttpServletRequest request, ActionErrors errors) {
        String num = request.getParameter(parameterName);
        if (!com.intellij.openapi.util.text.StringUtil.isEmptyOrSpaces(num) && (!StringUtil.isNumber(num) || (Integer.parseInt(num) <= 0))) {
            errors.addError(INVALID + parameterName, errorMessage);
        }
    }
}