package com.checkmarx.teamcity.server;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.controllers.admin.AdminPage;
import jetbrains.buildServer.serverSide.auth.Permission;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import jetbrains.buildServer.serverSide.crypt.EncryptUtil;

import org.jetbrains.annotations.NotNull;

import com.checkmarx.teamcity.common.CxConstants;


public class CxAdminPage extends AdminPage {
    private final CxAdminConfig cxAdminConfig;

    public CxAdminPage(@NotNull final PagePlaces pagePlaces,
                       @NotNull final WebControllerManager controllerManager,
                       @NotNull final PluginDescriptor descriptor,
                       @NotNull final CxAdminConfig cxAdminConfig) {
        super(pagePlaces);
        this.cxAdminConfig = cxAdminConfig;
        setPluginName(CxConstants.RUNNER_TYPE);
        setIncludeUrl(descriptor.getPluginResourcesPath("adminPage.jsp"));
        setTabTitle(CxConstants.RUNNER_DISPLAY_NAME);
        register();

        controllerManager.registerController("/admin/checkmarxSettings.html", new BaseController() {
            @Override
            protected ModelAndView doHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response)
                    throws Exception {
            if (request.getParameter("save") != null) {
                cxAdminConfig.setServerUrl(request.getParameter(CxConstants.CXSERVERURL));
                cxAdminConfig.setUser(request.getParameter(CxConstants.CXUSER));
                String cxPass = request.getParameter(CxConstants.CXPASS);
                try {
                    cxPass = EncryptUtil.scramble(cxPass);
                } catch (RuntimeException e) {
                    cxPass = "";
                }
                cxAdminConfig.setPass(cxPass);
                cxAdminConfig.persistConfiguration();
            }

            return new ModelAndView(new RedirectView(request.getContextPath() + "/admin/admin.html?item=" +
                    CxConstants.RUNNER_TYPE + "&save"));
            }
        });
    }

    @Override
    public boolean isAvailable(@NotNull HttpServletRequest request) {
        return super.isAvailable(request) && checkHasGlobalPermission(request, Permission.CHANGE_SERVER_SETTINGS);
    }

    @NotNull
    public String getGroup() {
        return INTEGRATIONS_GROUP;
    }

    @Override
    public void fillModel(@NotNull Map<String, Object> model, @NotNull HttpServletRequest request) {
        super.fillModel(model, request);

        model.put(CxConstants.CXSERVERURL, cxAdminConfig.getServerUrl());
        model.put(CxConstants.CXUSER, cxAdminConfig.getUser());

        String cxPass = cxAdminConfig.getPass();
        try {
            if (cxPass != null) {
                cxPass = EncryptUtil.unscramble(cxPass);
            }
        } catch (IllegalArgumentException e) {
            cxPass = "";
        }
        model.put(CxConstants.CXPASS, cxPass);

        model.put(CxConstants.SAVE, request.getParameter("save") != null);
    }
}
