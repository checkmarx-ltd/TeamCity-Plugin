package com.checkmarx.teamcity.common.client.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by: Dorg.
 * Date: 06/10/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OSAScanStatus {

    private String id;
    private String startAnalyzeTime;
    private String endAnalyzeTime;
    private String origin;
    private OSAScanState state;
    private List<String> sharedSourceLocationPaths;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartAnalyzeTime() {
        return startAnalyzeTime;
    }

    public void setStartAnalyzeTime(String startAnalyzeTime) {
        this.startAnalyzeTime = startAnalyzeTime;
    }

    public String getEndAnalyzeTime() {
        return endAnalyzeTime;
    }

    public void setEndAnalyzeTime(String endAnalyzeTime) {
        this.endAnalyzeTime = endAnalyzeTime;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public OSAScanState getState() {
        return state;
    }

    public void setState(OSAScanState state) {
        this.state = state;
    }

    public List<String> getSharedSourceLocationPaths() {
        return sharedSourceLocationPaths;
    }

    public void setSharedSourceLocationPaths(List<String> sharedSourceLocationPaths) {
        this.sharedSourceLocationPaths = sharedSourceLocationPaths;
    }
}