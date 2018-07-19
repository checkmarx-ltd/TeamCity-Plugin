package com.checkmarx.teamcity.common;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class CxPluginUtils {

    private static final String[] SUPPORTED_EXTENSIONS = {"jar", "war", "ear", "aar", "dll", "exe", "msi", "nupkg", "egg", "whl", "tar.gz", "gem", "deb", "udeb",
            "dmg", "drpm", "rpm", "pkg.tar.xz", "swf", "swc", "air", "apk", "zip", "gzip", "tar.bz2", "tgz", "c", "cc", "cp", "cpp", "css", "c++", "h", "hh", "hpp",
            "hxx", "h++", "m", "mm", "pch", "java", "c#", "cs", "csharp", "go", "goc", "js", "plx", "pm", "ph", "cgi", "fcgi", "psgi", "al", "perl", "t", "p6m", "p6l", "nqp,6pl", "6pm",
            "p6", "php", "py", "rb", "swift", "clj", "cljx", "cljs", "cljc"};

    private static final String DEFAULT_ARCHIVE_INCLUDES = "**/.*jar,**/*.war,**/*.ear,**/*.sca,**/*.gem,**/*.whl,**/*.egg,**/*.tar,**/*.tar.gz,**/*.tgz,**/*.zip,**/*.rar";

    private static final String INCLUDE_ALL_EXTENSIONS = "**/**";

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
        } else {
            ret.put("includes",INCLUDE_ALL_EXTENSIONS);
        }

        if(StringUtils.isNotEmpty(excludesString)) {
            ret.put("excludes",excludesString);
        }

        ret.put("acceptExtensionsList", SUPPORTED_EXTENSIONS);

        if(StringUtils.isNotEmpty(archiveIncludes)) {
            String[] archivePatterns = archiveIncludes.split("\\s*,\\s*"); //split by comma and trim (spaces + newline)
            for (int i = 0; i < archivePatterns.length; i++) {
                if(StringUtils.isNotEmpty(archivePatterns[i]) && archivePatterns[i].startsWith("*.")) {
                    archivePatterns[i] = "**/" + archivePatterns[i];
                }
            }
            archiveIncludes = String.join(",", archivePatterns);
            ret.put("archiveIncludes", archiveIncludes);
        } else {
            ret.put("archiveIncludes", DEFAULT_ARCHIVE_INCLUDES);
        }

        ret.put("archiveExtractionDepth", "4");

        if(installBeforeScan) {
            ret.put("npm.runPreStep", "true");
            ret.put("npm.ignoreScripts", "true");
            ret.put("bower.runPreStep", "false");
        }

        ret.put("d", scanFolder);

        return ret;
    }
}

