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

        for (String config : GLOBAL_CONFIGS) {
            cxAdminConfig.setConfiguration(config, StringUtil.emptyIfNull(request.getParameter(config)));
        }

        String sastAndOsaPassword = ensurePasswordEncryption(request, "encryptedCxGlobalPassword");
        cxAdminConfig.setConfiguration(GLOBAL_PASSWORD, sastAndOsaPassword);
        String scaPassword = ensurePasswordEncryption(request, "encryptedCxGlobalSCAPassword");
        cxAdminConfig.setConfiguration(GLOBAL_SCA_PASSWORD, scaPassword);
        getOrCreateMessages(request).addMessage("settingsSaved", SUCCESSFUL_SAVE_MESSAGE);

        String globalIsSynchronous = request.getParameter(GLOBAL_IS_SYNCHRONOUS);
        String globalThresholdsEnabled = request.getParameter(GLOBAL_THRESHOLD_ENABLED);
        String globalServerUrl = request.getParameter(GLOBAL_SERVER_URL);
        String globalUsername = request.getParameter(GLOBAL_USERNAME);
        String globalPss = request.getParameter(GLOBAL_PASSWORD);
        String globalEnableCriticalSeverity = request.getParameter(GLOBAL_ENABLE_CRITICAL_SEVERITY);
        String globalCriticalThreshold = request.getParameter(GLOBAL_CRITICAL_THRESHOLD);
        	if (globalEnableCriticalSeverity == null) {
        	   globalEnableCriticalSeverity = OPTION_FALSE;
        	}
        if ("true".equals(globalIsSynchronous)) {
            if ("true".equals(globalThresholdsEnabled)) {
                Double version = 9.0;
                try {
                    String sastVersion = SASTUtils.loginToServer(new URL(globalServerUrl), globalUsername, decrypt(sastAndOsaPassword));
                    String[] sastVersionSplit = sastVersion.split("\\.");
                    if (sastVersionSplit.length > 1) {
                        version = Double.parseDouble(sastVersionSplit[0] + "." + sastVersionSplit[1]);
                }
            } catch (Exception e) {
                    e.printStackTrace();
                }
                if (version >= 9.7) {
                    if (globalEnableCriticalSeverity == null || OPTION_FALSE.equalsIgnoreCase(globalEnableCriticalSeverity)) {
                        globalEnableCriticalSeverity = OPTION_TRUE;
                        globalCriticalThreshold = "";
                        getOrCreateMessages(request).addMessage("criticalSeverityMessage", CRITICAL_SEVERITY_MESSAGE);
                    }
                } else {
                    if (OPTION_TRUE.equalsIgnoreCase(globalEnableCriticalSeverity)) {
                        globalEnableCriticalSeverity = OPTION_FALSE;
                        globalCriticalThreshold = "";
                        getOrCreateMessages(request).addMessage("criticalSeverityErrorMessage", CRITICAL_SEVERITY_ERROR_MESSAGE);
                    }
                }
                
            }
        }
        cxAdminConfig.setConfiguration(GLOBAL_CRITICAL_THRESHOLD, globalCriticalThreshold);
        cxAdminConfig.setConfiguration(GLOBAL_ENABLE_CRITICAL_SEVERITY, globalEnableCriticalSeverity);
        try {
            cxAdminConfig.persistConfiguration();
        } catch (IOException e) {
        	e.printStackTrace();
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