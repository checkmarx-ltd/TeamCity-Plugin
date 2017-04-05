package com.checkmarx.teamcity.common;


public class CxConstants {
    public static final String RUNNER_TYPE = "checkmarx";
    public static final String RUNNER_DESCRIPTION = "Checkmarx SAST Scan";
    public static final String RUNNER_DISPLAY_NAME = "Checkmarx";

    public final static String CHECKMARX_REPORT_XML = "ScanReport.xml";
    public final static String CHECKMARX_REPORT_PDF = "ScanReport.pdf";
    public final static String CHECKMARX_REPORT_XML_PATH = RUNNER_DISPLAY_NAME + "/" + CHECKMARX_REPORT_XML;

    public static final String TRUE = "true";
    public static final String SAVE = "save";

    public static final String DEFAULTSERVERLURL = "http://localhost";

    public static final String CXGLOBALSERVER = "cxDefaultServer";
    public static final String CXSERVERURL = "cxServerUrl";
    public static final String CXUSER = "cxUser";
    public static final String CXPASS = "cxPass";
    public static final String CXPROJECT = "cxProject";
    public static final String CXPRESET = "cxPreset";
    public static final String CXCONFIGURATION = "cxConfiguration";
    public static final String CXTEAM = "cxTeam";
    public static final String CXEXCLUDEFOLDERS = "cxExcludeFolders";
    public static final String CXFILTERPATTERNS = "cxFilterPatterns";
    public static final String CXINCREMENTAL = "cxIncremental";
    public static final String CXPERIODICFULLSCANS = "cxPeriodicFullScans";
    public static final String CXNUMBERINCREMENTAL = "cxNumberIncremental";
    public static final String CXCOMMENT = "cxComment";
    public static final String CXTHRESHOLDENABLE = "cxThresholdEnable";
    public static final String CXTHRESHOLDHIGH = "cxThresholdHigh";
    public static final String CXTHRESHOLDMEDIUM = "cxThresholdMedium";
    public static final String CXTHRESHOLDLOW = "cxThresholdLow";
    public static final String CXGENERATEPDF = "cxGeneratePdf";
}
