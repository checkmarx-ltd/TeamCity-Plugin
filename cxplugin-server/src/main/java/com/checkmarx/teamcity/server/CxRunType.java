package com.checkmarx.teamcity.server;

import java.util.HashMap;
import java.util.Map;

import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.web.openapi.PluginDescriptor;

import org.jetbrains.annotations.NotNull;

import com.checkmarx.teamcity.common.CxConstants;


public class CxRunType extends RunType {
    private final PluginDescriptor pluginDescriptor;

    private static final String DEFAULTNUMBERINCREMENTAL = "10";
    private static final String DEFAULTTHRESHOLD = "0";
    private static final String DEFAULTFILTERPATTERN =
            "!**/_cvs/**/*, !**/.svn/**/*,   !**/.hg/**/*,   !**/.git/**/*,  !**/.bzr/**/*, !**/bin/**/*,\n" +
            "!**/obj/**/*,  !**/backup/**/*, !**/.idea/**/*, !**/*.DS_Store, !**/*.ipr,     !**/*.iws,\n" +
            "!**/*.bak,     !**/*.tmp,       !**/*.aac,      !**/*.aif,      !**/*.iff,     !**/*.m3u,   !**/*.mid, !**/*.mp3,\n" +
            "!**/*.mpa,     !**/*.ra,        !**/*.wav,      !**/*.wma,      !**/*.3g2,     !**/*.3gp,   !**/*.asf, !**/*.asx,\n" +
            "!**/*.avi,     !**/*.flv,       !**/*.mov,      !**/*.mp4,      !**/*.mpg,     !**/*.rm,    !**/*.swf, !**/*.vob,\n" +
            "!**/*.wmv,     !**/*.bmp,       !**/*.gif,      !**/*.jpg,      !**/*.png,     !**/*.psd,   !**/*.tif, !**/*.swf,\n" +
            "!**/*.jar,     !**/*.zip,       !**/*.rar,      !**/*.exe,      !**/*.dll,     !**/*.pdb,   !**/*.7z,  !**/*.gz,\n" +
            "!**/*.tar.gz,  !**/*.tar,       !**/*.gz,       !**/*.ahtm,     !**/*.ahtml,   !**/*.fhtml, !**/*.hdm,\n" +
            "!**/*.hdml,    !**/*.hsql,      !**/*.ht,       !**/*.hta,      !**/*.htc,     !**/*.htd,   !**/*.war, !**/*.ear,\n" +
            "!**/*.htmls,   !**/*.ihtml,     !**/*.mht,      !**/*.mhtm,     !**/*.mhtml,   !**/*.ssi,   !**/*.stm,\n" +
            "!**/*.stml,    !**/*.ttml,      !**/*.txn,      !**/*.xhtm,     !**/*.xhtml,   !**/*.class, !**/*.iml\n";

    public CxRunType(final RunTypeRegistry runTypeRegistry, final PluginDescriptor pluginDescriptor) {
        this.pluginDescriptor = pluginDescriptor;
        runTypeRegistry.registerRunType(this);
    }

    @Override
    public PropertiesProcessor getRunnerPropertiesProcessor() {
        return new CxRunTypePropertiesProcessor();
    }

    @NotNull
    @Override
    public String getDescription() {
        return CxConstants.RUNNER_DESCRIPTION;
    }

    @Override
    public String getEditRunnerParamsJspFilePath() {
        return this.pluginDescriptor.getPluginResourcesPath("editRunParams.jsp");
    }

    @Override
    public String getViewRunnerParamsJspFilePath() {
        return this.pluginDescriptor.getPluginResourcesPath("viewRunParams.jsp");
    }

    @Override
    public Map<String, String> getDefaultRunnerProperties() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(CxConstants.CXGLOBALSERVER, CxConstants.TRUE);
        parameters.put(CxConstants.CXSERVERURL, CxConstants.DEFAULTSERVERLURL);
        parameters.put(CxConstants.CXFILTERPATTERNS, DEFAULTFILTERPATTERN);
        parameters.put(CxConstants.CXNUMBERINCREMENTAL, DEFAULTNUMBERINCREMENTAL);
        parameters.put(CxConstants.CXTHRESHOLDHIGH, DEFAULTTHRESHOLD);
        parameters.put(CxConstants.CXTHRESHOLDMEDIUM, DEFAULTTHRESHOLD);
        parameters.put(CxConstants.CXTHRESHOLDLOW, DEFAULTTHRESHOLD);
        return parameters;
    }

    @Override
    @NotNull
    public String getType() {
        return CxConstants.RUNNER_TYPE;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return CxConstants.RUNNER_DISPLAY_NAME;
    }
}
