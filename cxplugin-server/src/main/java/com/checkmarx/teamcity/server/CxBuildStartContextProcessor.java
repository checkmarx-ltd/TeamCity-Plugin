package com.checkmarx.teamcity.server;

import com.checkmarx.teamcity.common.CxConstants;
import com.checkmarx.teamcity.common.CxParam;
import jetbrains.buildServer.serverSide.BuildStartContext;
import jetbrains.buildServer.serverSide.BuildStartContextProcessor;
import jetbrains.buildServer.serverSide.SBuildServer;
import org.jetbrains.annotations.NotNull;


public class CxBuildStartContextProcessor implements BuildStartContextProcessor {
    private final CxAdminConfig cxAdminConfig;
    private final SBuildServer server;

    public CxBuildStartContextProcessor(@NotNull final CxAdminConfig cxAdminConfig, final SBuildServer server) {
        this.cxAdminConfig = cxAdminConfig;
        this.server = server;
    }

    @Override
    public void updateParameters(@NotNull final BuildStartContext buildStartContext) {

        for (String config : CxParam.GLOBAL_CONFIGS) {
            buildStartContext.addSharedParameter(config, this.cxAdminConfig.getConfiguration(config));
        }
        buildStartContext.addSharedParameter(CxConstants.TEAMCITY_SERVER_URL ,server.getRootUrl());
    }
}
