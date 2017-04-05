package com.checkmarx.teamcity.agent;

import java.util.concurrent.*;

import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.*;
import org.jetbrains.annotations.NotNull;


abstract class CallableBuildProcess implements BuildProcess, Callable<BuildFinishedStatus> {
    @NotNull
    protected final BuildProgressLogger logger;
    private Future<BuildFinishedStatus> future;

    public CallableBuildProcess(@NotNull final AgentRunningBuild runningBuild) {
        this.logger = runningBuild.getBuildLogger();
    }

    public void start() throws RunBuildException {
        try {
            this.future = Executors.newSingleThreadExecutor().submit(this);
        } catch (final RejectedExecutionException e) {
            this.logger.error("Failed to start build!");
            this.logger.exception(e);
            throw new RunBuildException(e);
        }
    }

    public boolean isInterrupted() {
        return this.future.isCancelled() && isFinished();
    }

    public boolean isFinished() {
        return this.future.isDone();
    }

    protected abstract void cancelBuild();

    public void interrupt() {
        this.logger.message("Attempt to interrupt build process");
        cancelBuild();
        this.future.cancel(true);
    }

    @NotNull
    public BuildFinishedStatus waitFor() throws RunBuildException {
        try {
            return this.future.get();
        } catch (final InterruptedException e) {
            throw new RunBuildException(e);
        } catch (final ExecutionException e) {
            throw new RunBuildException(e);
        } catch (final CancellationException e) {
            this.logger.exception(e);
            return BuildFinishedStatus.INTERRUPTED;
        }
    }
}
