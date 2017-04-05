package com.checkmarx.teamcity.server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.checkmarx.teamcity.common.CxConstants;
import jetbrains.buildServer.serverSide.ServerPaths;
import jetbrains.buildServer.util.PropertiesUtil;
import jetbrains.buildServer.util.FileUtil;

import org.jetbrains.annotations.NotNull;


public class CxAdminConfig {
    private final ServerPaths serverPaths;
    private final Properties properties = new Properties();

    public CxAdminConfig(@NotNull final ServerPaths serverPaths) {
        this.serverPaths = serverPaths;

        File configFile = getConfigFile();
        if (!configFile.isFile()) {
            initConfigFile(configFile);
        }
        loadConfiguration(configFile);
    }

    private void initConfigFile(@NotNull final File configFile) {
        try {
            this.properties.put(CxConstants.CXSERVERURL, "http://localhost");
            this.properties.put(CxConstants.CXUSER, "");
            this.properties.put(CxConstants.CXPASS, "");
            configFile.getParentFile().mkdirs();
            PropertiesUtil.storeProperties(this.properties, configFile, "");
        } catch (IOException e) {
        }
    }

    private void loadConfiguration(@NotNull final File configFile) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(configFile);
            this.properties.load(fileReader);
            if (this.properties.get(CxConstants.CXSERVERURL) == null){
                this.properties.put(CxConstants.CXSERVERURL, "");
            }
            if (this.properties.get(CxConstants.CXUSER) == null){
                this.properties.put(CxConstants.CXUSER, "");
            }
            if (this.properties.get(CxConstants.CXPASS) == null){
                this.properties.put(CxConstants.CXPASS, "");
            }
        } catch (IOException e) {
        } finally {
            FileUtil.close(fileReader);
        }
    }

    private File getConfigFile() {
        return new File(this.serverPaths.getConfigDir(), "checkmarx-plugin.properties");
    }

    public void persistConfiguration() throws IOException {
        PropertiesUtil.storeProperties(this.properties, getConfigFile(), "");
    }

    public String getServerUrl() {
        return this.properties.get(CxConstants.CXSERVERURL).toString();
    }
    public void setServerUrl(final String serverUrl) {
        this.properties.put(CxConstants.CXSERVERURL, serverUrl);
    }

    public String getUser() {
        return this.properties.get(CxConstants.CXUSER).toString();
    }
    public void setUser(final String user) {
        this.properties.put(CxConstants.CXUSER, user);
    }

    public String getPass() {
        return this.properties.get(CxConstants.CXPASS).toString();
    }
    public void setPass(final String pass) {
        this.properties.put(CxConstants.CXPASS, pass);
    }
}
