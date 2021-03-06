package com.checkmarx.teamcity.agent;


import com.cx.restclient.dto.Results;
import com.cx.restclient.dto.ScanResults;

public abstract class CxPluginUtils {
    //ScanSummary obj instead of String
    public static void printScanBuildFailure(String scanSummary, ScanResults ret, CxLoggerAdapter logger) {
        logger.error("********************************************");
        logger.error(" The Build Failed for the Following Reasons: ");
        logger.error("********************************************");



        logError(ret.getSastResults(), logger);
        logError(ret.getScaResults(), logger);
        logError(ret.getOsaResults(), logger);
        //todo: check null
        if (scanSummary != null) {
            //scanSummary.
            //String[] lines = thDescription.split("\\n");
            //for (String s : lines) {
                logger.error(scanSummary);
            //}
        }

        logger.error("-----------------------------------------------------------------------------------------\n");
        logger.error("");
    }

    private static void logError(Results results, org.slf4j.Logger log) {
       if(results!=null){
           if (results.getException() != null ) {
               log.error(results.getException().getMessage());
           }
       }

    }
    
    
}

