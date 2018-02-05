package com.checkmarx.teamcity.common;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class CxPluginUtils {


    public static boolean isEmptyString(String s) {
        return s != null && "".equals(s);
    }


    public static Properties generateOSAScanConfiguration(String filterPatterns, String archiveIncludes, String scanFolder, boolean installBeforeScan) {
        Properties ret = new Properties();
        filterPatterns = StringUtils.defaultString(filterPatterns);
        archiveIncludes = StringUtils.defaultString(archiveIncludes);

        List<String> inclusions = new ArrayList<String>();
        List<String> exclusions = new ArrayList<String>();
        String[] filters = filterPatterns.split("\\s*,\\s*"); //split by comma and trim (spaces + newline)
        for (String filter : filters) {
            if(StringUtils.isNotEmpty(filter)) {
                if (!filter.startsWith("!") ) {
                    inclusions.add(filter);
                } else if(filter.length() > 1){
                    filter = filter.substring(1); // Trim the "!"
                    exclusions.add(filter);
                }
            }
        }

        String includesString = String.join(",", inclusions);
        String excludesString = String.join(",", exclusions);

        if(StringUtils.isNotEmpty(includesString)) {
            ret.put("includes",includesString);
        }

        if(StringUtils.isNotEmpty(excludesString)) {
            ret.put("excludes",excludesString);
        }

        if(StringUtils.isNotEmpty(archiveIncludes)) {
            ret.put("archiveIncludes", archiveIncludes);
        }

        ret.put("archiveExtractionDepth", "4");

        if(installBeforeScan) {
            ret.put("npm.runPreStep", "true");
            ret.put("bower.runPreStep", "true");
        }

        ret.put("d", scanFolder);

        return ret;
    }
}

