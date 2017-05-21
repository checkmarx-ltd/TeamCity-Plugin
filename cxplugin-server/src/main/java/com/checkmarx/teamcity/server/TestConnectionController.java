package com.checkmarx.teamcity.server;

import com.checkmarx.teamcity.common.CxSelectOption;
import com.checkmarx.teamcity.common.client.CxClientService;
import com.checkmarx.teamcity.common.client.CxClientServiceImpl;
import com.checkmarx.teamcity.common.client.exception.CxClientException;
import com.google.gson.Gson;
import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.crypt.EncryptUtil;
import jetbrains.buildServer.serverSide.crypt.RSACipher;
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
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static com.checkmarx.teamcity.common.CxConstants.*;

public class TestConnectionController extends BaseController {

    public static final Logger log = LoggerFactory.getLogger(TestConnectionController.class);

    private static Gson gson = new Gson();

    public TestConnectionController(@NotNull SBuildServer server,
                                    @NotNull WebControllerManager webControllerManager) {
        super(server);
        webControllerManager.registerController("/checkmarx/testConnection/", this);
    }

    @Nullable
    @Override
    protected ModelAndView doHandle(@NotNull HttpServletRequest httpServletRequest, @NotNull HttpServletResponse httpServletResponse) throws Exception {

        if (!"POST".equals(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return null;
        }

        TestConnectionResponse res = new TestConnectionResponse();
        res.setPresetList(Collections.singletonList(new CxSelectOption("0", NO_PRESET_MESSAGE)));
        res.setTeamPathList(Collections.singletonList(new CxSelectOption("0", NO_TEAM_MESSAGE)));

        try {

            TestConnectionRequest credi = extractRequestBody(httpServletRequest);

            //create client and perform login
            try {
                CxClientServiceImpl.testConnection(credi.getServerUrl(), credi.getUsername(), credi.getPassword());
            } catch (CxClientException e) {
                res.setMessage(e.getMessage());
                httpServletResponse.getWriter().write(gson.toJson(res));
                httpServletResponse.setContentType("application/json");
                httpServletResponse.setStatus(200);
                return null;
            }


            CxClientService client = new CxClientServiceImpl(new URL(credi.getServerUrl()), credi.getUsername(), credi.getPassword());
            client.loginToServer();

            List<CxSelectOption> presets = null;
            List<CxSelectOption> teams = null;

            if(!credi.isGlobal()) {
                //get presets from server and convert to id/value
                presets = client.getPresetListForSelect();

                //get teams from server and convert to id/value
                teams = client.getTeamListForSelect();
            }

            //write response
            res = new TestConnectionResponse(true, "Success", presets, teams);
            httpServletResponse.getWriter().write(gson.toJson(res));
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setStatus(200);
            return null;

        } catch (Exception e) {
            log.error("Error occurred in test connection", e);
            res.setMessage(GENERAL_ERROR_MESSAGE);
            httpServletResponse.getWriter().write(gson.toJson(res));
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setStatus(200);
            return null;
        }

    }

    private TestConnectionRequest extractRequestBody(HttpServletRequest request) throws IOException {
        String jsonString = IOUtils.toString(request.getReader());
        TestConnectionRequest ret = gson.fromJson(jsonString, TestConnectionRequest.class);
        ret.setPassword(resolvePasswordPlainText(ret.getPassword(), ret.isGlobal()));
        return ret;
    }

    private String resolvePasswordPlainText(String password, boolean global) {

        try {
            if(!global) {
                password = RSACipher.decryptWebRequestData(password);
            }

            return EncryptUtil.isScrambled(password) ? EncryptUtil.unscramble(password) : password;

        } catch (Exception e) {
            return password;
        }
    }

}
