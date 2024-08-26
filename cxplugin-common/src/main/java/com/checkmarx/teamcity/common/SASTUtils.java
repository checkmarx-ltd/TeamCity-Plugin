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
import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.dto.ProxyConfig;
import com.cx.restclient.sast.utils.LegacyClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SASTUtils {

	public static LegacyClient getInstance(CxScanConfig config, Logger log)
			throws MalformedURLException, CxClientException {
		return new LegacyClient(config, log) {
		};
	}

	private static final Logger log = LoggerFactory.getLogger(SASTUtils.class);
	private CxClientDelegator clientDelegator;

	public static String loginToServer(URL url, String username, String pssd) {
		String version = null;
		String result = "";
		try {
			CxScanConfig scanConfig = new CxScanConfig(url.toString().trim(), username, pssd,
					CxConstants.ORIGIN_TEAMCITY, true);
			scanConfig.addScannerType(ScannerType.SAST);
			LegacyClient clientCommon = getInstance(scanConfig, log);
			version = clientCommon.login(true);
			return version;
		} catch (Exception ex) {
			result = ex.getMessage();
			return version;
		}
	}
}
