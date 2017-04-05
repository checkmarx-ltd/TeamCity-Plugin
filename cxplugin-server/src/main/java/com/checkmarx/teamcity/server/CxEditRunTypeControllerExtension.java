package com.checkmarx.teamcity.server;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jetbrains.buildServer.controllers.ActionErrors;
import jetbrains.buildServer.controllers.StatefulObject;
import jetbrains.buildServer.controllers.admin.projects.BuildTypeForm;
import jetbrains.buildServer.controllers.admin.projects.EditRunTypeControllerExtension;
import jetbrains.buildServer.serverSide.BuildTypeSettings;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.crypt.EncryptUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.checkmarx.teamcity.common.CxConstants;


public class CxEditRunTypeControllerExtension implements EditRunTypeControllerExtension {
    private final CxAdminConfig cxAdminConfig;

    private static final String SET = "Set";

    public CxEditRunTypeControllerExtension(@NotNull final SBuildServer server,
                                            @NotNull final CxAdminConfig cxAdminConfig) {
        server.registerExtension(EditRunTypeControllerExtension.class, CxConstants.RUNNER_TYPE, this);
        this.cxAdminConfig = cxAdminConfig;
    }

    public void fillModel(@NotNull final HttpServletRequest request,
                          @NotNull final BuildTypeForm form,
                          @NotNull final Map model) {
        String cxServerUrl, cxUser, cxPass;
        final Map<String, String> properties = form.getBuildRunnerBean().getPropertiesBean().getProperties();
        final String cxGlobalServer = properties.get(CxConstants.CXGLOBALSERVER);
        if (cxGlobalServer != null && cxGlobalServer.equals(CxConstants.TRUE)) {
            System.out.println("Using global Cx Server settings");
            cxServerUrl = this.cxAdminConfig.getServerUrl();
            cxUser = this.cxAdminConfig.getUser();
            cxPass = this.cxAdminConfig.getPass();
        } else {
            System.out.println("Using build step Cx Server settings");
            cxServerUrl = properties.get(CxConstants.CXSERVERURL);
            cxUser = properties.get(CxConstants.CXUSER);
            cxPass = properties.get(CxConstants.CXPASS);
        }
        try {
            if (cxPass != null) {
                cxPass = EncryptUtil.unscramble(cxPass);
            }
        } catch (IllegalArgumentException e) {
        }
        model.put("actualServerUrl", cxServerUrl);
        model.put("actualUser", cxUser);
        model.put("actualPass", cxPass);

        if (properties.get(SET) != null) {
            // properties already set, exiting
            return;
        }
        try {
            cxPass = properties.get(CxConstants.CXPASS);
            if (cxPass != null) {
                cxPass = EncryptUtil.unscramble(cxPass);
                properties.put(CxConstants.CXPASS, cxPass);
            }
            properties.put(SET, CxConstants.TRUE);
        } catch (IllegalArgumentException e) {
        }
    }

    public void updateState(@NotNull final HttpServletRequest request, @NotNull final BuildTypeForm form) {}

    @Nullable
    public StatefulObject getState(@NotNull final HttpServletRequest request, @NotNull final BuildTypeForm form) {
        return null;
    }

    public void updateBuildType(@NotNull final HttpServletRequest request,
                                @NotNull final BuildTypeForm form,
                                @NotNull final BuildTypeSettings buildTypeSettings,
                                @NotNull final ActionErrors errors) {}

    @NotNull
    public ActionErrors validate(@NotNull final HttpServletRequest request, @NotNull final BuildTypeForm form) {
        final Map<String, String> properties = form.getBuildRunnerBean().getPropertiesBean().getProperties();
        String cxPass = properties.get(CxConstants.CXPASS);
        try {
            cxPass = EncryptUtil.scramble(cxPass);
        } catch (RuntimeException e) {
            cxPass = "";
        }
        properties.put(CxConstants.CXPASS, cxPass);

        return new ActionErrors();
    }
}
