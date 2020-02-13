package com.checkmarx.teamcity.server;

import com.checkmarx.teamcity.common.CxConstants;
import com.cx.restclient.CxShragaClient;
import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.sca.dto.SCAConfig;
import com.google.gson.Gson;
import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.util.StringUtil;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.checkmarx.teamcity.common.CxParam.CONNECTION_FAILED_COMPATIBILITY;

public class TestScaConnectionController extends BaseController {

    public static final Logger log = LoggerFactory.getLogger(TestScaConnectionController.class);

    private Gson gson = new Gson();

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

        CxScanConfig config = new CxScanConfig();
        config.setCxOrigin("TeamCity");
        //  config.setDisableCertificateValidation(!isEnableCertificateValidation());

        SCAConfig scaConfig = new SCAConfig();
        scaConfig.setAccessControlUrl(credi.getAccessControlServerUrl());
        scaConfig.setApiUrl(credi.getServerUrl());
        scaConfig.setTenant(credi.getScaTenant());
        scaConfig.setUsername(credi.getScaUserName());
        scaConfig.setPassword(credi.getScaPassword());
        scaConfig.setWebAppUrl(credi.getWebAppURL());
        config.setScaConfig(scaConfig);
        try {
            CxShragaClient.testScaConnection(config, log);
            res.setSuccess(true);
            res.setMessage(CxConstants.CONNECTION_SUCCESSFUL_MESSAGE);
            writeHttpServletResponse(httpServletResponse, res);
            return null;

        } catch (Exception e) {
            log.error("connection failed", e);
            res.setMessage(CONNECTION_FAILED_COMPATIBILITY);
            writeHttpServletResponse(httpServletResponse, res);
            return null;
        }
        //create client and perform login
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
        ret.setScaPassword(CxOptions.decryptPassword(ret.getScaPassword(), ret.isGlobal()));
        ret.setScaTenant(StringUtil.trim(ret.getScaTenant()));
        ret.setWebAppURL(StringUtil.trim(ret.getWebAppURL()));
        //
        return ret;
    }
}
