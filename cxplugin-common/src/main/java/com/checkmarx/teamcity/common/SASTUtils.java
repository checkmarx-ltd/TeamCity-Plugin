package com.checkmarx.teamcity.common;

import static com.checkmarx.teamcity.common.CxParam.*;
import static com.checkmarx.teamcity.common.CxParam.CX_ORIGIN;

import java.net.MalformedURLException;
import com.cx.restclient.exception.CxClientException;
import java.net.URL;
import com.checkmarx.teamcity.common.CxConstants;
import com.cx.restclient.sast.utils.LegacyClient;
import com.checkmarx.teamcity.common.CxParam;
import com.cx.restclient.CxClientDelegator;
import com.cx.restclient.CxSASTClient;
import jetbrains.buildServer.log.Loggers;
import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.dto.EngineConfiguration;
import com.cx.restclient.dto.ScannerType;
import com.cx.restclient.dto.Team;
import com.cx.restclient.sast.dto.Preset;
import com.google.gson.Gson;
import jetbrains.buildServer.util.StringUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static com.checkmarx.teamcity.common.CxConstants.*;
import static com.checkmarx.teamcity.common.CxParam.*;
//
//import com.checkmarx.teamcity.agent.CommonClientFactory;
//import com.cx.plugin.testConnection.CxRestResource;
import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.dto.ProxyConfig;
import com.cx.restclient.sast.utils.LegacyClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SASTUtils {
	
//	public SASTUtils(CxScanConfig config, Logger log) throws MalformedURLException {
//        super(config, log);
//    }
	
	public static LegacyClient getInstance(CxScanConfig config, Logger log)
            throws MalformedURLException, CxClientException {
        return new LegacyClient(config, log) {
        };
    }
	
	
	private static final Logger log = LoggerFactory.getLogger(SASTUtils.class);
	private CxClientDelegator clientDelegator;

    //private static LegacyClient commonClient = null;
    

	public static String loginToServer(URL url, String username, String pssd) {
		System.out.println(String.format("Attempting to log in to server with URL: %s, Username: %s, Password: %s, Origin: %s", url, username, pssd, CxConstants.ORIGIN_TEAMCITY));
		   Loggers.SERVER.info("Attempting to log in to server with URL: " + url + ", Username: " + username +", Password: " + pssd + ", Origin: " + CxConstants.ORIGIN_TEAMCITY);
		String version = null;
		String result = "";
		//LegacyClient commonClient = null;
	        try {
	            CxScanConfig scanConfig = new CxScanConfig(url.toString().trim(), username, pssd, CxConstants.ORIGIN_TEAMCITY , true);
	            System.out.println(String.format("Scan configuration created with URL: %s, Username: %s, Password: %s, Origin: %s", url, username, pssd, CxConstants.ORIGIN_TEAMCITY));
	            Loggers.SERVER.info("Scan configuration created with URL: " + url + ", Username: " + username +", Password: " + pssd + ", Origin: " + CxConstants.ORIGIN_TEAMCITY);
	            scanConfig.addScannerType(ScannerType.SAST);
//	            scanConfig.setUsername(username);
//	            scanConfig.setPassword(pssd);
//	            scanConfig.setUrl(url.toString().trim());
//	            scanConfig.setCxOrigin(CxConstants.ORIGIN_TEAMCITY);
//	            scanConfig.setDisableCertificateValidation(true);
//	            String isProxyVar = System.getProperty("cx.isproxy");
//	            scanConfig.setProxy(StringUtils.isNotEmpty(isProxyVar) && isProxyVar.equalsIgnoreCase("true"));
	            //commonClient = new LegacyClient(scanConfig, log);
	            //SASTUtils sastUtils = new SASTUtils(scanConfig, log);
	            LegacyClient clientCommon = getInstance(scanConfig, log);
	            //Loggers.SERVER.info("client Common: " + clientCommon.toString());
	            version = clientCommon.login(true);
	            System.out.println(String.format("Logged in successfully, version: %s", version));
	            Loggers.SERVER.info("Logged in successfully, version: " + version);
	            return version;
	        } catch (Exception ex) {
	        	Loggers.SERVER.error("Checkmarx login to server: " + ex.getMessage(), ex);
	            result = ex.getMessage();
	            return version;
	        }
	    }
	
	
	
       
}
