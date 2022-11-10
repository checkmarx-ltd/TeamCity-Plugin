package com.checkmarx.teamcity.server;

import com.checkmarx.teamcity.common.CxConstants;
import com.checkmarx.teamcity.common.CxParam;
import com.checkmarx.teamcity.common.EmptyStringToNumberTypeAdapter;
import com.cx.restclient.CxClientDelegator;
import com.cx.restclient.CxSASTClient;
import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.dto.ProxyConfig;
import com.cx.restclient.dto.ScannerType;
import com.cx.restclient.dto.Team;
import com.cx.restclient.sast.dto.Preset;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import static com.checkmarx.teamcity.common.CxParam.CONNECTION_FAILED_COMPATIBILITY;

class TestScaSastConnectionController extends BaseController {

    public static final Logger log = LoggerFactory.getLogger(TestScaSastConnectionController.class);
    private static final com.intellij.openapi.diagnostic.Logger LOG = jetbrains.buildServer.log.Loggers.SERVER;

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(int.class, new EmptyStringToNumberTypeAdapter())
            .registerTypeAdapter(Integer.class, new EmptyStringToNumberTypeAdapter())
            .registerTypeAdapter(double.class, new EmptyStringToNumberTypeAdapter())
            .registerTypeAdapter(Double.class, new EmptyStringToNumberTypeAdapter())
            .create();

    private String result = "";
    private List<Preset> presets;
    private List<Team> teams;
    private CxClientDelegator clientDelegator;

    public TestScaSastConnectionController(@NotNull SBuildServer server,
                                           @NotNull WebControllerManager webControllerManager) {
        super(server);
        webControllerManager.registerController("/checkmarx/testScaSastConnection/", this);
    }

    @Nullable
    @Override
    protected ModelAndView doHandle(@NotNull HttpServletRequest httpServletRequest, @NotNull HttpServletResponse httpServletResponse) throws Exception {

        result = "";
        if (!"POST".equals(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return null;
        }

        TestScaSastConnectionResponse res = new TestScaSastConnectionResponse();
        res.setPresetList(Collections.singletonList(new Preset(CxParam.NO_PRESET_ID, NO_PRESET_MESSAGE)));
        res.setTeamPathList(Collections.singletonList(new Team(CxParam.GENERATE_PDF_REPORT, NO_TEAM_MESSAGE)));


        TestScaSastConnectionRequest credi = extractRequestBody(httpServletRequest);
        ProxyConfig proxyConfig = null;
        if (credi.isProxy() && StringUtils.isNotEmpty(credi.getProxyHost()) && credi.getProxyPort() > 0) {
            proxyConfig = new ProxyConfig(credi.getProxyHost(), credi.getProxyPort(), credi.getProxyUser(),
                    credi.getProxyPassword(), credi.isProxyHttps());
        }

        //create client and perform login
        try {
            if (loginToServer(new URL(credi.getSastServerUrl()), credi.getSastUsername(), credi.getSastPssd(), proxyConfig)) {
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

                res = new TestScaSastConnectionResponse(true, CxConstants.CONNECTION_SUCCESSFUL_MESSAGE, presets, teams);
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

    private void writeHttpServletResponse(HttpServletResponse httpServletResponse, TestScaSastConnectionResponse res) throws IOException {
        httpServletResponse.getWriter().write(gson.toJson(res));
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(200);
    }

    private TestScaSastConnectionRequest extractRequestBody(HttpServletRequest request) throws IOException {
        String jsonString = IOUtils.toString(request.getReader());
        TestScaSastConnectionRequest ret = gson.fromJson(jsonString, TestScaSastConnectionRequest.class);
        ret.setSastServerUrl(StringUtil.trim(ret.getSastServerUrl()));
        ret.setSastUsername(StringUtil.trim(ret.getSastUsername()));
        ret.setSastPssd(CxOptions.decryptPasswordPlainText(ret.getSastPssd(), ret.isGlobal()));
        if (StringUtils.isNotEmpty(ret.getProxyPassword())) {
            ret.setProxyPassword(CxOptions.decryptPasswordPlainText(ret.getProxyPassword(), ret.isGlobal()));
        }
        return ret;
    }

    private boolean loginToServer(URL url, String username, String pssd, ProxyConfig proxyConfig) {
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
            if (proxyConfig != null) {
                config.setProxy(true);
                config.setProxyConfig(proxyConfig);
            }
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
