package com.checkmarx.teamcity.server;

import com.checkmarx.teamcity.common.CxConstants;
import com.checkmarx.teamcity.common.CxParam;
import com.cx.restclient.CxShragaClient;
import com.cx.restclient.dto.Team;
import com.cx.restclient.sast.dto.Preset;
import com.google.gson.Gson;
import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.crypt.EncryptUtil;
import jetbrains.buildServer.serverSide.crypt.RSACipher;
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
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static com.checkmarx.teamcity.common.CxConstants.*;

public class TestConnectionController extends BaseController {

    public static final Logger log = LoggerFactory.getLogger(TestConnectionController.class);

    private Gson gson = new Gson();

    private String result = "";
    private List<Preset> presets;
    private List<Team> teams;
    private CxShragaClient shraga;

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


        TestConnectionRequest credi = extractRequestBody(httpServletRequest);

        //create client and perform login
        try {
            if (loginToServer(new URL(credi.getServerUrl()), credi.getUsername(), credi.getPassword())) {
                try {
                    teams = shraga.getTeamList();
                } catch (Exception e) {
                    log.error("Error occurred in test connection", e);
                    res.setMessage("Connection Failed.\n" +
                            "Validate the provided login credentials and server URL are correct.\n" +
                            "In addition, make sure the installed plugin version is compatible with the CxSAST version according to CxSAST release notes.");
                    writeHttpServletResponse(httpServletResponse, res);
                    return null;
                }
                presets = shraga.getPresetList();

                if (presets == null || teams == null) {
                    throw new Exception("invalid preset teamPath");
                }

                res = new TestConnectionResponse(true, CxConstants.CONNECTION_SUCCESSFUL_MESSAGE, presets, teams);
                writeHttpServletResponse(httpServletResponse, res);
                return null;
            } else {
                result = result.contains("Failed to authenticate") ? "Failed to authenticate" : result;
                result = result.startsWith("Login failed.") ? result : "Login failed. " + result;

                res.setMessage(result);
                writeHttpServletResponse(httpServletResponse, res);
                return null;

            }
        } catch (Exception e) {
            log.error("Error occurred in test connection", e);
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

    private boolean loginToServer(URL url, String username, String password) {
        try {
            shraga = new CxShragaClient(url.toString().trim(), username, password, CxConstants.ORIGIN_TEAMCITY, false, log);
            shraga.login();

            return true;
        } catch (Exception ex) {
            result = ex.getMessage();
            return false;
        }
    }

}
