package com.checkmarx.teamcity.server;

import java.util.HashMap;
import java.util.Map;

import com.checkmarx.teamcity.common.CxParam;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.web.openapi.PluginDescriptor;

import org.jetbrains.annotations.NotNull;

import com.checkmarx.teamcity.common.CxConstants;


public class CxRunType extends RunType {

    private final PluginDescriptor pluginDescriptor;
    private CxAdminConfig adminConfig;


    public CxRunType(final RunTypeRegistry runTypeRegistry, final PluginDescriptor pluginDescriptor, CxAdminConfig adminConfig) {
        this.pluginDescriptor = pluginDescriptor;
        this.adminConfig = adminConfig;
        runTypeRegistry.registerRunType(this);
    }

    @Override
    public PropertiesProcessor getRunnerPropertiesProcessor() {
        return new CxRunTypePropertiesProcessor();
    }

    @NotNull
    @Override
    public String getDescription() {
        return CxConstants.RUNNER_DESCRIPTION;
    }

    @Override
    public String getEditRunnerParamsJspFilePath() {
        return this.pluginDescriptor.getPluginResourcesPath("editRunParams.jsp");
    }

    @Override
    public String getViewRunnerParamsJspFilePath() {
        return this.pluginDescriptor.getPluginResourcesPath("viewRunParams.jsp");
    }

    @Override
    public Map<String, String> getDefaultRunnerProperties() {

        Map<String, String> parameters = new HashMap<>();
        parameters.put(CxParam.USE_DEFAULT_SERVER, CxConstants.TRUE);
        parameters.put(CxParam.USE_DEFAULT_SAST_CONFIG, CxConstants.TRUE);
        parameters.put(CxParam.USE_DEFAULT_SCAN_CONTROL, CxConstants.TRUE);
        parameters.put(CxParam.SERVER_URL, "");
        parameters.put(CxParam.USERNAME, "");
        parameters.put(CxParam.PASSWORD, "");
        parameters.put(CxParam.IS_PROXY, CxConstants.FALSE);
        parameters.put(CxParam.PROJECT_NAME, "");
        parameters.put(CxParam.FILTER_PATTERNS, CxConstants.DEFAULT_FILTER_PATTERN);
        parameters.put(CxParam.OSA_ARCHIVE_INCLUDE_PATTERNS, CxConstants.DEFAULT_OSA_ARCHIVE_INCLUDE_PATTERNS);
        parameters.put(CxParam.IS_SYNCHRONOUS, CxConstants.TRUE);
        parameters.put(CxParam.SAST_ENABLED, CxConstants.TRUE);
        parameters.put(CxParam.GLOBAL_DEPENDENCY_SCAN_FILTER_PATTERNS,adminConfig.getConfiguration(CxParam.GLOBAL_DEPENDENCY_SCAN_FILTER_PATTERNS));
        parameters.put(CxParam.GLOBAL_OSA_ARCHIVE_INCLUDE_PATTERNS,adminConfig.getConfiguration(CxParam.GLOBAL_OSA_ARCHIVE_INCLUDE_PATTERNS));
        parameters.put(CxParam.SCA_API_URL,adminConfig.getConfiguration(CxParam.GLOBAL_SCA_API_URL));
        parameters.put(CxParam.SCA_USERNAME,adminConfig.getConfiguration(CxParam.GLOBAL_SCA_USERNAME));
        parameters.put(CxParam.SCA_PASSWORD,adminConfig.getConfiguration(CxParam.GLOBAL_SCA_PASSWORD));
        parameters.put(CxParam.SCA_TENANT,adminConfig.getConfiguration(CxParam.GLOBAL_SCA_TENANT));
        parameters.put(CxParam.SCA_WEB_APP_URL,adminConfig.getConfiguration(CxParam.GLOBAL_SCA_WEB_APP_URL));
        parameters.put(CxParam.SCA_ACCESS_CONTROL_URL,adminConfig.getConfiguration(CxParam.GLOBAL_SCA_ACCESS_CONTROL_URL));


        return parameters;
    }

    @Override
    @NotNull
    public String getType() {
        return CxConstants.RUNNER_TYPE;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return CxConstants.RUNNER_DISPLAY_NAME;
    }
}
