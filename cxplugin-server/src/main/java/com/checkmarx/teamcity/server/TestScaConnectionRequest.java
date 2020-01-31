package com.checkmarx.teamcity.server;

import java.io.Serializable;

/**
 * Created by: Dorg.
 * Date: 13/04/2017.
 */
public class TestScaConnectionRequest implements Serializable {

    private String serverUrl;
    private String accessControlServerUrl;
    private String webAppURL;
    private String scaUserName;
    private String scaPassword;
    private String scaTenant;
    private boolean global;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getAccessControlServerUrl() {
        return accessControlServerUrl;
    }

    public void setAccessControlServerUrl(String accessControlServerUrl) {
        this.accessControlServerUrl = accessControlServerUrl;
    }

    public String getWebAppURL() {
        return webAppURL;
    }

    public void setWebAppURL(String webAppURL) {
        this.webAppURL = webAppURL;
    }

    public String getScaUserName() {
        return scaUserName;
    }

    public void setScaUserName(String scaUserName) {
        this.scaUserName = scaUserName;
    }

    public String getScaPassword() {
        return scaPassword;
    }

    public void setScaPassword(String scaPassword) {
        this.scaPassword = scaPassword;
    }

    public String getScaTenant() {
        return scaTenant;
    }

    public void setScaTenant(String scaTenant) {
        this.scaTenant = scaTenant;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }
}
