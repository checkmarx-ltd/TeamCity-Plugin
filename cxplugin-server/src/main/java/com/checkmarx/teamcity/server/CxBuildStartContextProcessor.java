package com.checkmarx.teamcity.server;

import com.checkmarx.teamcity.common.CxConstants;
import com.checkmarx.teamcity.common.CxParam;
import jetbrains.buildServer.serverSide.BuildStartContext;
import jetbrains.buildServer.serverSide.BuildStartContextProcessor;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;


public class CxBuildStartContextProcessor implements BuildStartContextProcessor {
    private final CxAdminConfig cxAdminConfig;
    private final PluginDescriptor pluginDescriptor;

    public CxBuildStartContextProcessor(@NotNull final CxAdminConfig cxAdminConfig,
                                        final PluginDescriptor pluginDescriptor) {
        this.cxAdminConfig = cxAdminConfig;
        this.pluginDescriptor = pluginDescriptor;
    }

    @Override
    public void updateParameters(@NotNull final BuildStartContext buildStartContext) {

        for (String config : CxParam.GLOBAL_CONFIGS) {
            buildStartContext.addSharedParameter(config, this.cxAdminConfig.getConfiguration(config));
        }

        buildStartContext.addSharedParameter(CxConstants.TEAMCITY_PLUGIN_VERSION, pluginDescriptor.getPluginVersion());
    }
}
