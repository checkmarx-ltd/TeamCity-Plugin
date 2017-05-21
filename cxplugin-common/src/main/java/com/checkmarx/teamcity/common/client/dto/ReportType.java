package com.checkmarx.teamcity.common.client.dto;

/**
 * Created by: Dorg.
 * Date: 15/09/2016.
 */
public enum ReportType {

        PDF,
        RTF,
        CSV,
        XML;

    public String value() {
        return name();
    }

    public static ReportType fromValue(String v) {
            return valueOf(v);
        }

}
