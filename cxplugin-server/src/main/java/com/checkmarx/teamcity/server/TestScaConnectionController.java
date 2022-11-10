package com.checkmarx.teamcity.server;

import com.checkmarx.teamcity.common.CxConstants;
import com.checkmarx.teamcity.common.CxParam;
import com.checkmarx.teamcity.common.EmptyStringToNumberTypeAdapter;
import com.cx.restclient.ast.AstScaClient;
import com.cx.restclient.ast.dto.sca.AstScaConfig;
import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.dto.ProxyConfig;
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

public class TestScaConnectionController extends BaseController {

    public static final Logger log = LoggerFactory.getLogger(TestScaConnectionController.class);

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(int.class, new EmptyStringToNumberTypeAdapter())
            .registerTypeAdapter(Integer.class, new EmptyStringToNumberTypeAdapter())
            .registerTypeAdapter(double.class, new EmptyStringToNumberTypeAdapter())
            .registerTypeAdapter(Double.class, new EmptyStringToNumberTypeAdapter())
            .create();

    public TestScaConnectionController(@NotNull SBuildServer server,
                                       @NotNull WebControllerManager webControllerManager) {
        super(server);
        webControllerManager.registerController("/checkmarx/testSCAConnection/", this);
    }

    @Nullable
    @Override
    protected ModelAndView doHandle(@NotNull HttpServletRequest httpServletRequest, @NotNull HttpServletResponse httpServletResponse) throws Exception {

        if (!"POST".equals(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return null;
        }

        TestScaConnectionResponse res = new TestScaConnectionResponse();

        TestScaConnectionRequest credi = extractRequestBody(httpServletRequest);
        ProxyConfig proxyConfig = null;
        if (credi.isProxy() && StringUtils.isNotEmpty(credi.getProxyHost()) && credi.getProxyPort() > 0) {
            proxyConfig = new ProxyConfig(credi.getProxyHost(), credi.getProxyPort(), credi.getProxyUser(),
                    credi.getProxyPassword(), credi.isProxyHttps());
        }

        CxScanConfig config = new CxScanConfig();
        config.setCxOrigin("TeamCity");
        config.setDisableCertificateValidation(true);
        AstScaConfig scaConfig = new AstScaConfig();
        scaConfig.setAccessControlUrl(credi.getAccessControlServerUrl());
        scaConfig.setApiUrl(credi.getServerUrl());
        scaConfig.setTenant(credi.getScaTenant());
        scaConfig.setUsername(credi.getScaUserName());
        scaConfig.setPassword(credi.getScaPassword());
        scaConfig.setWebAppUrl(credi.getWebAppURL());
        config.setDisableCertificateValidation(true);
        config.setAstScaConfig(scaConfig);
        String isProxyVar = System.getProperty("cx.isproxy");
        config.setProxy(StringUtils.isNotEmpty(isProxyVar) && isProxyVar.equalsIgnoreCase("true"));
        if (proxyConfig != null) {
            config.setProxy(true);
            config.setProxyConfig(proxyConfig);
        }
        AstScaClient scaClient = new AstScaClient(config, log);
        try {
            scaClient.testScaConnection();
            res.setSuccess(true);
            res.setMessage(CxConstants.CONNECTION_SUCCESSFUL_MESSAGE);
            writeHttpServletResponse(httpServletResponse, res);
            return null;

        } catch (Exception e) {
            log.error("connection failed", e);
            res.setMessage(CxParam.CONNECTION_FAILED_COMPATIBILITY);
            writeHttpServletResponse(httpServletResponse, res);
            return null;
        }
    }


    private void writeHttpServletResponse(HttpServletResponse httpServletResponse, TestScaConnectionResponse res) throws IOException {
        httpServletResponse.getWriter().write(gson.toJson(res));
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(200);
    }

    private TestScaConnectionRequest extractRequestBody(HttpServletRequest request) throws IOException {
        String jsonString = IOUtils.toString(request.getReader());
        TestScaConnectionRequest ret = gson.fromJson(jsonString, TestScaConnectionRequest.class);
        ret.setServerUrl(StringUtil.trim(ret.getServerUrl()));
        ret.setAccessControlServerUrl(StringUtil.trim(ret.getAccessControlServerUrl()));
        ret.setScaUserName(StringUtil.trim(ret.getScaUserName()));
        ret.setScaPassword(CxOptions.decryptPasswordPlainText(ret.getScaPassword(), ret.isGlobal()));
        ret.setScaTenant(StringUtil.trim(ret.getScaTenant()));
        ret.setWebAppURL(StringUtil.trim(ret.getWebAppURL()));
        if (StringUtils.isNotEmpty(ret.getProxyPassword())) {
            ret.setProxyPassword(CxOptions.decryptPasswordPlainText(ret.getProxyPassword(), ret.isGlobal()));
        }
        return ret;
    }
}
