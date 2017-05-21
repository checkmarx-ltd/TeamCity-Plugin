package com.checkmarx.teamcity.agent;

import java.util.concurrent.*;

import jetbrains.buildServer.*;
import jetbrains.buildServer.agent.*;
import org.jetbrains.annotations.*;


/**
 * Created by: Dorg.
 * Date: 16/05/2017.
 */
public abstract class CallableBuildProcess implements BuildProcess, Callable<BuildFinishedStatus> {

    private Future<BuildFinishedStatus> future;

    public void start() throws RunBuildException {
        try {
            future = Executors.newSingleThreadExecutor().submit(this);
        } catch (final RejectedExecutionException e) {
            throw new RunBuildException(e);
        }
    }

    public boolean isInterrupted() {
        return future.isCancelled() && isFinished();
    }

    public boolean isFinished() {
        return future.isDone();
    }

    public void interrupt() {
        future.cancel(true);
    }

    @NotNull
    public BuildFinishedStatus waitFor() throws RunBuildException {
        try {
            final BuildFinishedStatus status = future.get();
            return status;
        } catch (final InterruptedException e) {
            throw new RunBuildException(e);
        } catch (final ExecutionException e) {
            throw new RunBuildException(e);
        } catch (final CancellationException e) {
            return BuildFinishedStatus.INTERRUPTED;
        }
    }
}
