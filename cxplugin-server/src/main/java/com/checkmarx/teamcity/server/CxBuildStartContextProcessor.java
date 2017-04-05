package com.checkmarx.teamcity.server;

import jetbrains.buildServer.serverSide.BuildStartContext;
import jetbrains.buildServer.serverSide.BuildStartContextProcessor;
import org.jetbrains.annotations.NotNull;

import com.checkmarx.teamcity.common.CxConstants;


public class CxBuildStartContextProcessor implements BuildStartContextProcessor {
    private final CxAdminConfig cxAdminConfig;

    public CxBuildStartContextProcessor(@NotNull final CxAdminConfig cxAdminConfig) {
        this.cxAdminConfig = cxAdminConfig;
    }

    @Override
    public void updateParameters(@NotNull final BuildStartContext buildStartContext) {
        buildStartContext.addSharedParameter(CxConstants.CXSERVERURL, this.cxAdminConfig.getServerUrl());
        buildStartContext.addSharedParameter(CxConstants.CXUSER, this.cxAdminConfig.getUser());
        buildStartContext.addSharedParameter(CxConstants.CXPASS, this.cxAdminConfig.getPass());
    }
}
