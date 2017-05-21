package com.checkmarx.teamcity.server;

import com.checkmarx.teamcity.common.CxSelectOption;

import java.util.List;

/**
 * Created by galn on 12/02/2017.
 */
public class TestConnectionResponse {

    public boolean success = false;
    public String message;
    public List<CxSelectOption> presetList;
    public List<CxSelectOption> teamPathList;


    public TestConnectionResponse(boolean success, String message, List<CxSelectOption> presetList, List<CxSelectOption> teamPathList) {
        this.success = success;
        this.message = message;
        this.presetList = presetList;
        this.teamPathList = teamPathList;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public TestConnectionResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CxSelectOption> getPresetList() {
        return presetList;
    }

    public void setPresetList(List<CxSelectOption> presetList) {
        this.presetList = presetList;
    }

    public List<CxSelectOption> getTeamPathList() {
        return teamPathList;
    }

    public void setTeamPathList(List<CxSelectOption> teamPathList) {
        this.teamPathList = teamPathList;
    }
}



