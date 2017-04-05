package com.checkmarx.teamcity.agent;

import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.*;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;

import org.jetbrains.annotations.NotNull;

import com.checkmarx.teamcity.common.CxConstants;


public class CxAgentBuildRunner implements AgentBuildRunner, AgentBuildRunnerInfo {
    private final ArtifactsWatcher artifactsWatcher;

    public CxAgentBuildRunner(@NotNull final ArtifactsWatcher artifactsWatcher) {
        this.artifactsWatcher = artifactsWatcher;
    }

    @NotNull
    public BuildProcess createBuildProcess(@NotNull final AgentRunningBuild runningBuild,
                                           @NotNull final BuildRunnerContext context) throws RunBuildException {
        return new CxBuildProcess(runningBuild, context, this.artifactsWatcher);
    }

    @NotNull
    public AgentBuildRunnerInfo getRunnerInfo() {
        return this;
    }

    @NotNull
    public String getType() {
        return CxConstants.RUNNER_TYPE;
    }

    public boolean canRun(@NotNull final BuildAgentConfiguration agentConfiguration) {
        return true;
    }
}
