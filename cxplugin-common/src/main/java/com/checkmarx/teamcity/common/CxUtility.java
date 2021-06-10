package com.checkmarx.teamcity.common;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;

import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.serverSide.crypt.EncryptUtil;

public class CxUtility {

	/**
	 * Encrypts the password if not encrypted
	 * @param password
	 * @return
	 */
	public static String encrypt(String password) throws RuntimeException{
		String encPassword = "";
    	if(!EncryptUtil.isScrambled(password)) {
    		encPassword = EncryptUtil.scramble(password);
        } else {
        	encPassword = password;
        }
        return encPassword;
    }
	
	/**
	 * Decrypts the password if encrypted
	 * @param password
	 * @return
	 */
	public static String decrypt(String password) throws RuntimeException {
		String encStr = "";
		if (StringUtils.isNotEmpty(password)) {
			if (EncryptUtil.isScrambled(password)) {
				encStr = EncryptUtil.unscramble(password);
			} else {
				encStr =  password;
			}
		}
		return encStr;
	}
	
	/**
	 * Returns the origin of the scan : Which project , which build configuration has run the scan
	 * @param buildRunnerContext
	 * @param agentRunningBuild
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getCxOrigin(AgentRunningBuild agentRunningBuild) throws UnsupportedEncodingException {
		
		String projectName = agentRunningBuild.getProjectName();
		projectName = projectName.replaceAll("[^.a-zA-Z0-9\\s]", " ");
		String buildConfName = agentRunningBuild.getBuildTypeName();
		buildConfName = URLDecoder.decode(buildConfName, "UTF-8");
        buildConfName = buildConfName.replaceAll("[^.a-zA-Z0-9\\s]", " ");
        String serverURL = agentRunningBuild.getAgentConfiguration().getServerUrl();
        serverURL = serverURL.substring((serverURL.lastIndexOf("://")) + 3);
        String hostName = "";
        if(serverURL.indexOf(":")!=-1) {
            hostName = serverURL.substring(0, serverURL.lastIndexOf(":"));
        } else {
            hostName = serverURL;
        }
        String origin = "TeamCity " + hostName + " " + projectName + " "+ buildConfName;
        // 50 is the maximum number of characters allowed by SAST server
        if(origin!=null && !origin.isEmpty()){
        if(origin.length()>50)
            origin=origin.substring(0,50);
        }
		return origin;
	}
	public static String getCxOriginUrl(AgentRunningBuild agentRunningBuild) {
		String serverUrl = agentRunningBuild.getAgentConfiguration().getServerUrl();
		String originUrl = serverUrl+"/viewType.html?buildTypeId=" + agentRunningBuild.getBuildTypeExternalId();
		return originUrl;
	}
}
