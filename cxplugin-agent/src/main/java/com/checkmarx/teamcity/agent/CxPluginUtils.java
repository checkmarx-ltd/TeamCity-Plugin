package com.checkmarx.teamcity.agent;


import com.cx.restclient.dto.ScanResults;

public abstract class CxPluginUtils {
    public static void printScanBuildFailure(String thDescription, ScanResults ret, CxLoggerAdapter logger) {
        logger.error("********************************************");
        logger.error(" The Build Failed for the Following Reasons: ");
        logger.error("********************************************");

        logError(ret.getSastCreateException(), logger);
        logError(ret.getSastWaitException(), logger);
        logError(ret.getOsaCreateException(), logger);
        logError(ret.getOsaWaitException(), logger);

        if (thDescription != null) {
            String[] lines = thDescription.split("\\n");
            for (String s : lines) {
                logger.error(s);
            }
        }

        logger.error("-----------------------------------------------------------------------------------------\n");
        logger.error("");
    }

    private static void logError(Exception ex, org.slf4j.Logger log) {
        if (ex != null) {
            log.error(ex.getMessage());
        }
    }
}

