package com.checkmarx.teamcity.server;

import com.checkmarx.teamcity.common.CxConstants;
import com.checkmarx.teamcity.common.CxParam;
import jetbrains.buildServer.controllers.admin.AdminPage;
import jetbrains.buildServer.serverSide.auth.Permission;
import jetbrains.buildServer.serverSide.crypt.RSACipher;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


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
        controllerManager.registerController("/admin/checkmarxSettings.html", new CxAdminPageController(cxAdminConfig));
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

        for (String conf : CxParam.GLOBAL_CONFIGS) {
            model.put(conf, cxAdminConfig.getConfiguration(conf));
        }

        model.put("hexEncodedPublicKey", RSACipher.getHexEncodedPublicKey());
    }
}
