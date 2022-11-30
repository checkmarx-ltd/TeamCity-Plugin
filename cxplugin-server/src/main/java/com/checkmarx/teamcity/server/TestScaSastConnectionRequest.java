package com.checkmarx.teamcity.server;

public class TestScaSastConnectionRequest {

    private String sastServerUrl;
    private String sastUsername;
    private String sastPssd;
    private boolean isProxy;
    private String proxyHost;
    private int proxyPort;
    private String proxyUser;
    private String proxyPassword;
    private boolean proxyHttps;

    public String getSastServerUrl() {
        return sastServerUrl;
    }

    public void setSastServerUrl(String sastServerUrl) {
        this.sastServerUrl = sastServerUrl;
    }

    public String getSastUsername() {
        return sastUsername;
    }

    public void setSastUsername(String sastUsername) {
        this.sastUsername = sastUsername;
    }

    public String getSastPssd() {
        return sastPssd;
    }

    public void setSastPssd(String sastPssd) {
        this.sastPssd = sastPssd;
    }

    public boolean isProxy() {
        return isProxy;
    }

    public void setProxy(boolean proxy) {
        isProxy = proxy;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public boolean isProxyHttps() {
        return proxyHttps;
    }

    public void setProxyHttps(boolean proxyHttps) {
        this.proxyHttps = proxyHttps;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    private boolean global;

}
