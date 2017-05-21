package com.checkmarx.teamcity.server;

import com.checkmarx.teamcity.common.CxConstants;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.artifacts.BuildArtifact;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.ViewLogTab;
import jetbrains.buildServer.web.reportTabs.ReportTabUtil;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;


public class CxReportTab extends ViewLogTab {

    public static final String CONTENT = "content";

    public CxReportTab(@NotNull final PagePlaces pagePlaces,
                        @NotNull final SBuildServer server,
                        @NotNull final PluginDescriptor pluginDescriptor) {
        super(CxConstants.RUNNER_DISPLAY_NAME, CxConstants.RUNNER_TYPE, pagePlaces, server);
        setIncludeUrl(pluginDescriptor.getPluginResourcesPath("reportTab.jsp"));
    }

    @Override
    protected void fillModel(@NotNull final Map<String, Object> model,
                             @NotNull final HttpServletRequest request,
                             @NotNull final SBuild build) {

        BuildArtifact artifact = ReportTabUtil.getArtifact(build, CxConstants.RUNNER_DISPLAY_NAME + "/" + CxConstants.REPORT_HTML_NAME);
        if(artifact != null) {
            try {
                String s = IOUtils.toString(artifact.getInputStream());
                model.put(CONTENT, s);

            } catch (IOException e) {
                model.put(CONTENT, "Failed to get the report: " + e.getMessage());

            }
        } else {
            model.put(CONTENT, "Failed to get the report");
        }
    }

    @Override
    protected boolean isAvailable(@NotNull final HttpServletRequest request, @NotNull final SBuild build) {
        return super.isAvailable(request, build) && ReportTabUtil.isAvailable(build, CxConstants.RUNNER_DISPLAY_NAME + "/" + CxConstants.REPORT_HTML_NAME);
    }
}
