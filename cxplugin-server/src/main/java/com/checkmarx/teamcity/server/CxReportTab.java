package com.checkmarx.teamcity.server;

import java.io.*;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.artifacts.BuildArtifact;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.ViewLogTab;
import jetbrains.buildServer.web.reportTabs.ReportTabUtil;

import org.jetbrains.annotations.NotNull;

import com.checkmarx.teamcity.common.CxConstants;
import com.checkmarx.teamcity.common.CxAbortException;
import com.checkmarx.teamcity.common.CxScanResult;


public class CxReportTab extends ViewLogTab {
    public CxReportTab(@NotNull final PagePlaces pagePlaces,
                       @NotNull final SBuildServer server,
                       @NotNull final PluginDescriptor pluginDescriptor) {
        super(CxConstants.RUNNER_DISPLAY_NAME, CxConstants.RUNNER_TYPE, pagePlaces, server);
        addCssFile(pluginDescriptor.getPluginResourcesPath("ScanReport.css"));
        setIncludeUrl(pluginDescriptor.getPluginResourcesPath("reportTab.jsp"));
    }

    @Override
    protected void fillModel(@NotNull final Map<String, Object> model,
                             @NotNull final HttpServletRequest request,
                             @NotNull final SBuild build) {
        try {
            model.put("scanResult", addScanResultAction(build));
        } catch (IOException e) {
            model.put("errorMessage", e.getMessage());
        }
    }

    @Override
    protected boolean isAvailable(@NotNull final HttpServletRequest request, @NotNull final SBuild build) {
        return super.isAvailable(request, build) && ReportTabUtil.isAvailable(build, CxConstants.CHECKMARX_REPORT_XML_PATH);
    }

    @NotNull
    private CxScanResult addScanResultAction(@NotNull final SBuild build) throws IOException {
        final BuildArtifact buildArtifact = ReportTabUtil.getArtifact(build, CxConstants.CHECKMARX_REPORT_XML_PATH);
        if (buildArtifact == null || !buildArtifact.isFile()) {
            throw new CxAbortException("Report file does not exist");
        }

        final CxScanResult cxScanResult = new CxScanResult();
        final File xmlReportFile = File.createTempFile("cxscantmp", ".xml");

        try {
            FileOutputStream out = new FileOutputStream(xmlReportFile);
            IOUtils.copy(buildArtifact.getInputStream(), out);

            if (xmlReportFile != null) {
                cxScanResult.readScanXMLReport(xmlReportFile);
                if (!cxScanResult.isResultValid()) {
                    throw new CxAbortException("Invalid scan result");
                }
            }
        } finally {
            xmlReportFile.delete();
        }

        return cxScanResult;
    }
}
