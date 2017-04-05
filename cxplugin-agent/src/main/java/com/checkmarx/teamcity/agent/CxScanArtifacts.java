package com.checkmarx.teamcity.agent;

import java.io.File;

import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import org.jetbrains.annotations.NotNull;

import com.checkmarx.teamcity.common.CxConstants;


public class CxScanArtifacts {
    public static File getXmlFile(@NotNull final AgentRunningBuild runningBuild) {
        return new File(getReportDirectory(runningBuild), CxConstants.CHECKMARX_REPORT_XML);
    }

    public static File getPdfFile(@NotNull final AgentRunningBuild runningBuild) {
        return new File(getReportDirectory(runningBuild), CxConstants.CHECKMARX_REPORT_PDF);
    }

    public static void publishArtifact(@NotNull final ArtifactsWatcher artifactsWatcher, @NotNull final File file) {
        artifactsWatcher.addNewArtifactsPath(file.getAbsolutePath() + "=>" + CxConstants.RUNNER_DISPLAY_NAME);
    }

    private static File getReportDirectory(@NotNull final AgentRunningBuild runningBuild) {
        final File reportDir = new File(runningBuild.getBuildTempDirectory(), CxConstants.RUNNER_TYPE);
        if (!reportDir.exists()) {
            reportDir.mkdir();
        }
        return reportDir;
    }
}
