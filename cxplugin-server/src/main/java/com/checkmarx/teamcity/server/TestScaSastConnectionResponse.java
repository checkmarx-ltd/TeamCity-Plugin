package com.checkmarx.teamcity.server;

import com.cx.restclient.dto.Team;
import com.cx.restclient.sast.dto.Preset;

import java.util.List;

/**
 * Created by galn on 12/02/2017.
 */
public class TestScaSastConnectionResponse {

    public boolean success = false;
    public String message;
    public List<Preset> presetList;
    public List<Team> teamPathList;


    public TestScaSastConnectionResponse(boolean success, String message, List<Preset> presetList, List<Team> teamPathList) {
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

    public TestScaSastConnectionResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Preset> getPresetList() {
        return presetList;
    }

    public void setPresetList(List<Preset> presetList) {
        this.presetList = presetList;
    }

    public List<Team> getTeamPathList() {
        return teamPathList;
    }

    public void setTeamPathList(List<Team> teamPathList) {
        this.teamPathList = teamPathList;
    }
}



