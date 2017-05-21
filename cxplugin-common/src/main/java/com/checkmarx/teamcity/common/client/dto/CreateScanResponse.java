package com.checkmarx.teamcity.common.client.dto;

/**
 * Created by: Dorg.
 * Date: 15/09/2016.
 */
public class CreateScanResponse {

    private long projectId;
    private String runId;

    public CreateScanResponse(long projectId, String runId) {
        this.projectId = projectId;
        this.runId = runId;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }
}
