package com.checkmarx.teamcity.server;

/**
 * Created by: Dorg.
 * Date: 13/04/2017.
 */
public class TestConnectionRequest {

    private String serverUrl;
    private String username;
    private String password;
    private boolean global;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }
}
