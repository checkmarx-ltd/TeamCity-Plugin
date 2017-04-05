package com.checkmarx.teamcity.common;


/**
 * CxResultSeverity is an enum used in CxScanResult
 */

public enum CxResultSeverity {
    HIGH("High","3"),
    MEDIUM("Medium","2"),
    LOW("Low","1"),
    INFO("Info","0");

    private final String displayString; // This value is used for displaying a legend in a graph, and similar display uses
    public final String xmlParseString; // This value is used for detecting result severity while parsing results xml

    private CxResultSeverity(String displayString, String xmlParseString) {
        this.displayString = displayString;
        this.xmlParseString = xmlParseString;
    }

    @Override
    public String toString()
    {
        return displayString;
    }
}
