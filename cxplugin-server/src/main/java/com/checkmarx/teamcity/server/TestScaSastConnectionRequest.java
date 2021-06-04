package com.checkmarx.teamcity.server;

public class TestScaSastConnectionRequest {

    private String sastServerUrl;
    private String sastUsername;
    private String sastPssd;
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
	public boolean isGlobal() {
		return global;
	}
	public void setGlobal(boolean global) {
		this.global = global;
	}
	private boolean global;

    
}
