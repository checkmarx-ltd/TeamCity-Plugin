package com.checkmarx.teamcity.server;

import com.checkmarx.teamcity.common.CxConstants;
import com.checkmarx.teamcity.common.CxParam;
import com.cx.restclient.CxClientDelegator;
import com.cx.restclient.CxSASTClient;
import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.dto.EngineConfiguration;
import com.cx.restclient.dto.ScannerType;
import com.cx.restclient.dto.Team;
import com.cx.restclient.sast.dto.Preset;
import com.google.gson.Gson;
import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.util.StringUtil;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static com.checkmarx.teamcity.common.CxConstants.*;
import static com.checkmarx.teamcity.common.CxParam.*;

class TestConnectionController extends BaseController {

    public static final Logger log = LoggerFactory.getLogger(TestConnectionController.class);
    private static final com.intellij.openapi.diagnostic.Logger LOG = jetbrains.buildServer.log.Loggers.SERVER;

    private Gson gson = new Gson();

    private String result = "";
    private List<Preset> presets;
    private List<Team> teams;
    private List<EngineConfiguration> engineConfigurations;
    private CxClientDelegator clientDelegator;

    public TestConnectionController(@NotNull SBuildServer server,
                                    @NotNull WebControllerManager webControllerManager) {
        super(server);
        webControllerManager.registerController("/checkmarx/testConnection/", this);
    }

    @Nullable
    @Override
    protected ModelAndView doHandle(@NotNull HttpServletRequest httpServletRequest, @NotNull HttpServletResponse httpServletResponse) throws Exception {

        result = "";
        if (!"POST".equals(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return null;
        }

        TestConnectionResponse res = new TestConnectionResponse();
        res.setPresetList(Collections.singletonList(new Preset(CxParam.NO_PRESET_ID, NO_PRESET_MESSAGE)));
        res.setTeamPathList(Collections.singletonList(new Team(CxParam.GENERATE_PDF_REPORT, NO_TEAM_MESSAGE)));
        res.setEngineConfigList(Collections.singletonList(new EngineConfiguration(NO_ENGINE_CONFIG_MESSAGE)));


        TestConnectionRequest credi = extractRequestBody(httpServletRequest);

        //create client and perform login
        try {
            if (loginToServer(new URL(credi.getServerUrl()), credi.getUsername(), credi.getPssd())) {
                CxSASTClient sastClient = clientDelegator.getSastClient();
                try {
                    teams = sastClient.getTeamList();
                } catch (Exception e) {
                    LOG.error("Error occurred in test connection", e);
                    res.setMessage(CONNECTION_FAILED_COMPATIBILITY);
                    writeHttpServletResponse(httpServletResponse, res);
                    return null;
                }

                presets = sastClient.getPresetList();

                if (presets == null || teams == null) {
                    throw new Exception("invalid preset teamPath");
                }

                engineConfigurations = sastClient.getEngineConfiguration();
                if (engineConfigurations == null) {
                    throw new Exception("Error while getting Engine configurations.");
                }else{
                    EngineConfiguration sastEngineConfig = new EngineConfiguration();
                    sastEngineConfig.setId(PROJECT_DEFAULT_CONFIG_ID);
                    sastEngineConfig.setName(PROJECT_DEFAULT);
                    engineConfigurations.add(sastEngineConfig);
                }
                res = new TestConnectionResponse(true, CxConstants.CONNECTION_SUCCESSFUL_MESSAGE, presets, teams,engineConfigurations);
                writeHttpServletResponse(httpServletResponse, res);
                LOG.info("Checkmarx test connection: Connection successful");
                return null;
            } else {
                result = result.contains("Failed to authenticate") ? "Failed to authenticate" : result;
                result = result.startsWith("Login failed.") ? result : "Login failed. " + result;

                res.setMessage(result);
                writeHttpServletResponse(httpServletResponse, res);
                return null;
            }
        } catch (Exception e) {
            LOG.error("Error occurred in test connection", e);
            res.setMessage(UNABLE_TO_CONNECT_MESSAGE);
            writeHttpServletResponse(httpServletResponse, res);
            return null;
        }
    }

    private void writeHttpServletResponse(HttpServletResponse httpServletResponse, TestConnectionResponse res) throws IOException {
        httpServletResponse.getWriter().write(gson.toJson(res));
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(200);
    }

    private TestConnectionRequest extractRequestBody(HttpServletRequest request) throws IOException {
        String jsonString = IOUtils.toString(request.getReader());
        TestConnectionRequest ret = gson.fromJson(jsonString, TestConnectionRequest.class);
        ret.setServerUrl(StringUtil.trim(ret.getServerUrl()));
        ret.setUsername(StringUtil.trim(ret.getUsername()));
        ret.setPssd(CxOptions.decryptPasswordPlainText(ret.getPssd(), ret.isGlobal()));
        return ret;
    }

    private boolean loginToServer(URL url, String username, String pssd) {
        try {
            CxScanConfig config = new CxScanConfig();
            config.addScannerType(ScannerType.SAST);
            config.setUsername(username);
            config.setPassword(pssd);
            config.setUrl(url.toString().trim());
            config.setCxOrigin(CxConstants.ORIGIN_TEAMCITY);
            config.setDisableCertificateValidation(true);
            String isProxyVar = System.getProperty("cx.isproxy");
            config.setProxy(StringUtils.isNotEmpty(isProxyVar) && isProxyVar.equalsIgnoreCase("true"));
            clientDelegator = new CxClientDelegator(config, log);
            clientDelegator.getSastClient().login();

            return true;
        } catch (Exception ex) {
            LOG.error("Checkmarx test connection: " + ex.getMessage(), ex);
            result = ex.getMessage();
            return false;
        }
    }

    private void printProxyParams() {
        LOG.info("##### JVM Http properties #####");
        final Properties sysProps = System.getProperties();
        final Set<String> keys = sysProps.stringPropertyNames();

        for (final String key : keys) {
            if (key.startsWith("http") || key.startsWith("java.net")) {
                LOG.info(key + " : " + sysProps.getProperty(key));
            }
        }
        LOG.info("###############################");
    }

}
