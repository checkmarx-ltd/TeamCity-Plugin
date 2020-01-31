package com.checkmarx.teamcity.server;

import com.cx.restclient.dto.Team;
import com.cx.restclient.sast.dto.Preset;

import java.util.List;

/**
 * Created by galn on 12/02/2017.
 */
public class TestScaConnectionResponse {

    public boolean success = false;
    public String message;




    public TestScaConnectionResponse(boolean success, String message) {
        this.success = success;
        this.message = message;

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public TestScaConnectionResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}



