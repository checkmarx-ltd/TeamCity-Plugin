package com.checkmarx.teamcity.common;

public abstract class CxPluginUtils {


    public static boolean isEmptyString(String s) {
        return s != null && "".equals(s);
    }
}

