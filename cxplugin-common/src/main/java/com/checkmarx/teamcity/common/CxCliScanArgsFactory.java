package com.checkmarx.teamcity.common;

import java.util.Map;

import jetbrains.buildServer.agent.BuildProgressLogger;

import com.checkmarx.CxJenkinsWebService.*;


/**
 * CxCliScanArgsFactory is used to create and initialize a CliScanArgs structure, used by the webservice
 */

public class CxCliScanArgsFactory {
    static public CliScanArgs create(final BuildProgressLogger logger, final Map<String, String> runnerParameters, final long projectId, final byte[] compressedSources) {
        ProjectSettings projectSettings = new ProjectSettings();

        final String cxProject = runnerParameters.get(CxConstants.CXPROJECT);
        final String cxPreset = runnerParameters.get(CxConstants.CXPRESET);
        final String cxConfiguration = runnerParameters.get(CxConstants.CXCONFIGURATION);
        final String cxTeam = runnerParameters.get(CxConstants.CXTEAM);
        final String cxComment = runnerParameters.get(CxConstants.CXCOMMENT);

        long presetLong = 0; // default value to use in case of exception
        try {
            presetLong = Long.parseLong(cxPreset);
        } catch (Exception e) {
            logger.warning("Encountered illegal preset value: " + cxPreset + ". Using default preset.");
        }
        projectSettings.setPresetID(presetLong);

        projectSettings.setProjectName(cxProject);
        projectSettings.setAssociatedGroupID(cxTeam);
        projectSettings.setProjectID(projectId);

        long configurationLong = 0; // default value to use in case of exception
        try {
            configurationLong = Long.parseLong(cxConfiguration);
        } catch (Exception e) {
            logger.warning("Encountered illegal configuration value: " + cxConfiguration + ". Using default configuration.");
        }
        projectSettings.setScanConfigurationID(configurationLong);

        LocalCodeContainer localCodeContainer = new LocalCodeContainer();
        localCodeContainer.setFileName("src.zip");
        localCodeContainer.setZippedFile(compressedSources);

        SourceCodeSettings sourceCodeSettings = new SourceCodeSettings();
        sourceCodeSettings.setSourceOrigin(SourceLocationType.LOCAL);
        sourceCodeSettings.setPackagedCode(localCodeContainer);

        CliScanArgs args = new CliScanArgs();
        args.setIsIncremental(false);
        args.setIsPrivateScan(false);
        args.setPrjSettings(projectSettings);
        args.setSrcCodeSettings(sourceCodeSettings);
        args.setComment(cxComment);

        return args;
    }
}
