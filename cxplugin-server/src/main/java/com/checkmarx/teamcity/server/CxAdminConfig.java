package com.checkmarx.teamcity.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.checkmarx.teamcity.common.CxConstants;
import com.checkmarx.teamcity.common.CxParam;
import jetbrains.buildServer.serverSide.ServerPaths;
import jetbrains.buildServer.util.PropertiesUtil;
import jetbrains.buildServer.util.FileUtil;

import org.jetbrains.annotations.NotNull;


public class CxAdminConfig {
    private final ServerPaths serverPaths;
    private final Properties properties = new Properties();

    public CxAdminConfig(@NotNull final ServerPaths serverPaths) throws IOException {
        this.serverPaths = serverPaths;

        File configFile = getConfigFile();
        if (!configFile.isFile()) {
            initConfigFile(configFile);
        }
        loadConfiguration(configFile);
    }

    private void initConfigFile(@NotNull final File configFile) throws IOException {

        for (String conf : CxParam.GLOBAL_CONFIGS) {
            this.properties.put(conf, "");
        }

        this.properties.put(CxParam.GLOBAL_SERVER_URL, CxConstants.DEFAULT_SERVER_URL);
        this.properties.put(CxParam.GLOBAL_FILTER_PATTERNS, CxConstants.DEFAULT_FILTER_PATTERN);
        this.properties.put(CxParam.GLOBAL_IS_SYNCHRONOUS, CxConstants.TRUE);
        this.properties.put(CxParam.GLOBAL_DEPENDENCY_SCAN_FILTER_PATTERNS, CxConstants.DEFAULT_FILTER_PATTERN);
        this.properties.put(CxParam.GLOBAL_OSA_ARCHIVE_INCLUDE_PATTERNS, CxConstants.DEFAULT_OSA_ARCHIVE_INCLUDE_PATTERNS);
        configFile.getParentFile().mkdirs();
        PropertiesUtil.storeProperties(this.properties, configFile, "");
    }

    private void loadConfiguration(@NotNull final File configFile) throws IOException {
        //FileReader fileReader = null;
        try(FileReader fileReader = new FileReader(configFile)) {
            this.properties.load(fileReader);
            for (String conf : CxParam.GLOBAL_CONFIGS) {
                if (this.properties.get(conf) == null){
                    this.properties.put(conf, "");
                }
            }
        }
        catch(FileNotFoundException fnfe)
        {
            fnfe.printStackTrace();
        }
    }

    private File getConfigFile() {
        return new File(this.serverPaths.getConfigDir(), "checkmarx-plugin.properties");
    }

    public void persistConfiguration() throws IOException {
        PropertiesUtil.storeProperties(this.properties, getConfigFile(), "");
    }

    public String getConfiguration(String key) {
        return this.properties.get(key).toString();
    }

    public void setConfiguration(String key, String val) {
        this.properties.put(key, val);
    }
}
