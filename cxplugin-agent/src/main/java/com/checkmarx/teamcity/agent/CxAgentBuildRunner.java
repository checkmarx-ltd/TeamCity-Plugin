package com.checkmarx.teamcity.agent;

import com.checkmarx.teamcity.common.CxConstants;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.*;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import org.jetbrains.annotations.NotNull;

/**
 * Created by: Dorg.
 * Date: 18/04/2017.
 */
public class CxAgentBuildRunner implements AgentBuildRunner, AgentBuildRunnerInfo {

    private final ArtifactsWatcher artifactsWatcher;

    public CxAgentBuildRunner(@NotNull final ArtifactsWatcher artifactsWatcher) {
        this.artifactsWatcher = artifactsWatcher;
    }

    @NotNull
    @Override
    public BuildProcess createBuildProcess(@NotNull AgentRunningBuild agentRunningBuild, @NotNull BuildRunnerContext buildRunnerContext) throws RunBuildException {
        return (BuildProcess) new CxBuildProcess(agentRunningBuild, buildRunnerContext, artifactsWatcher);
    }

    @NotNull
    @Override
    public AgentBuildRunnerInfo getRunnerInfo() {
        return this;
    }

    @NotNull
    @Override
    public String getType() {
        return CxConstants.RUNNER_TYPE;
    }

    @Override
    public boolean canRun(@NotNull BuildAgentConfiguration buildAgentConfiguration) {
        return true;
    }
}
