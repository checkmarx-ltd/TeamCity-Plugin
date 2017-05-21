package com.checkmarx.teamcity.server;


import jetbrains.buildServer.controllers.ActionErrors;
import jetbrains.buildServer.controllers.BaseFormXmlController;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.crypt.EncryptUtil;
import jetbrains.buildServer.serverSide.crypt.RSACipher;
import jetbrains.buildServer.util.StringUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.checkmarx.teamcity.common.CxConstants.*;
import static com.checkmarx.teamcity.common.CxParam.*;


public class CxAdminPageController extends BaseFormXmlController {

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

        String cxPass = RSACipher.decryptWebRequestData(request.getParameter("encryptedCxGlobalPassword"));
        if(!EncryptUtil.isScrambled(cxPass)) {
            try {
                cxPass = EncryptUtil.scramble(cxPass);
            } catch (RuntimeException e) {
                cxPass = "";
            }
        }
        cxAdminConfig.setConfiguration(GLOBAL_PASSWORD, cxPass);

        try {
            cxAdminConfig.persistConfiguration();
        } catch (IOException e) {
            Loggers.SERVER.error("Failed to persist global configurations", e);
        }
        getOrCreateMessages(request).addMessage("settingsSaved", SUCCESSFUL_SAVE_MESSAGE);

    }

    private ActionErrors validateForm(final HttpServletRequest request) {

        ActionErrors ret = new ActionErrors();

        String cxGlobalServerUrl = request.getParameter(GLOBAL_SERVER_URL);
        if (StringUtil.isEmptyOrSpaces(cxGlobalServerUrl)) {
            ret.addError(INVALID + GLOBAL_SERVER_URL, URL_NOT_EMPTY_MESSAGE);
        } else {
            try {
                new URL(cxGlobalServerUrl);
            } catch (MalformedURLException e) {
                ret.addError(INVALID + GLOBAL_SERVER_URL, URL_NOT_VALID_MESSAGE);
            }
        }

        if (StringUtil.isEmptyOrSpaces(request.getParameter(GLOBAL_USERNAME))) {
            ret.addError(INVALID + GLOBAL_USERNAME, USERNAME_NOT_EMPTY_MESSAGE);
        }

        if (StringUtil.isEmptyOrSpaces(request.getParameter("encryptedCxGlobalPassword"))) {
            ret.addError(INVALID + GLOBAL_PASSWORD, PASSWORD_NOT_EMPTY_MESSAGE);
        }

        validateNumeric(GLOBAL_SCAN_TIMEOUT_IN_MINUTES, SCAN_TIMEOUT_POSITIVE_INTEGER_MESSAGE, request, ret);


        if(TRUE.equals(request.getParameter(GLOBAL_IS_SYNCHRONOUS))) {

            if(TRUE.equals(request.getParameter(GLOBAL_THRESHOLD_ENABLED))) {
                validateNumeric(GLOBAL_HIGH_THRESHOLD, HIGH_THRESHOLD_POSITIVE_INTEGER_MESSAGE, request, ret);
                validateNumeric(GLOBAL_MEDIUM_THRESHOLD, MEDIUM_THRESHOLD_POSITIVE_INTEGER_MESSAGE, request, ret);
                validateNumeric(GLOBAL_LOW_THRESHOLD, LOW_THRESHOLD_POSITIVE_INTEGER_MESSAGE, request, ret);
            }

            if(TRUE.equals(request.getParameter(GLOBAL_OSA_THRESHOLD_ENABLED))) {
                validateNumeric(GLOBAL_OSA_HIGH_THRESHOLD, HIGH_THRESHOLD_POSITIVE_INTEGER_MESSAGE, request, ret);
                validateNumeric(GLOBAL_OSA_MEDIUM_THRESHOLD,MEDIUM_THRESHOLD_POSITIVE_INTEGER_MESSAGE, request, ret);
                validateNumeric(GLOBAL_OSA_LOW_THRESHOLD, LOW_THRESHOLD_POSITIVE_INTEGER_MESSAGE, request, ret);
            }
        }

        return ret;
    }

    private void validateNumeric(String parameterName, String errorMessage, HttpServletRequest request, ActionErrors errors) {
        String num = request.getParameter(parameterName);
        if (!StringUtil.isEmptyOrSpaces(num) && (!StringUtil.isNumber(num) || (Integer.parseInt(num) < 0))) {
                errors.addError(INVALID + parameterName, errorMessage);
        }
    }
}