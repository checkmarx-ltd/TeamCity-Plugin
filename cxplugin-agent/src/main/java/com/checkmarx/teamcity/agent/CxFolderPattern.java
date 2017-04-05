package com.checkmarx.teamcity.agent;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import org.jetbrains.annotations.NotNull;
import jetbrains.buildServer.agent.BuildProgressLogger;

import com.checkmarx.teamcity.common.CxConstants;


/**
 * CxFolderPattern generates the patterns used for zipping the workspace folder
 */

public class CxFolderPattern {
    public String generatePattern(final Map<String, String> runnerParameters, final BuildProgressLogger logger) throws IOException, InterruptedException {
        final String cxExclude = runnerParameters.get(CxConstants.CXEXCLUDEFOLDERS);
        final String cxPattern = runnerParameters.get(CxConstants.CXFILTERPATTERNS);
        return cxPattern + "," + processExcludeFolders(cxExclude, logger);
    }

    @NotNull
    private String processExcludeFolders(final String excludeFolders, final BuildProgressLogger logger) {
        if (excludeFolders == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        String[] patterns = StringUtils.split(excludeFolders, ",\n");
        for (String p : patterns) {
            p = p.trim();
            if (p.length() > 0) {
                result.append("!**/");
                result.append(p);
                result.append("/**/*, ");
            }
        }
        logger.progressMessage("Exclude folders converted to: '" + result.toString() + "'");
        return result.toString();
    }
}
