package com.checkmarx.teamcity.server;

import com.checkmarx.teamcity.common.CxParam;
import jetbrains.buildServer.serverSide.BuildStartContext;
import jetbrains.buildServer.serverSide.BuildStartContextProcessor;
import org.jetbrains.annotations.NotNull;


public class CxBuildStartContextProcessor implements BuildStartContextProcessor {
    private final CxAdminConfig cxAdminConfig;

    public CxBuildStartContextProcessor(@NotNull final CxAdminConfig cxAdminConfig) {
        this.cxAdminConfig = cxAdminConfig;
    }

    @Override
    public void updateParameters(@NotNull final BuildStartContext buildStartContext) {

        for (String config : CxParam.GLOBAL_CONFIGS) {
            buildStartContext.addSharedParameter(config, this.cxAdminConfig.getConfiguration(config));
        }
    }
}
