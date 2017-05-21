package com.checkmarx.teamcity.common.client;

import com.checkmarx.v7.CliScanArgs;
import com.checkmarx.v7.CxClientType;
import com.checkmarx.v7.ProjectScannedDisplayData;
import com.checkmarx.v7.ProjectSettings;
import com.checkmarx.teamcity.common.client.dto.BaseScanConfiguration;
import com.checkmarx.teamcity.common.client.dto.ScanResults;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by: Dorg.
 * Date: 15/09/2016.
 */
public abstract class CxPluginHelper {

    private static final Logger log = LoggerFactory.getLogger(CxPluginHelper.class);

    public static ScanResults genScanResponse(ProjectScannedDisplayData scanDisplayData) {
        ScanResults ret = new ScanResults();
        ret.setProjectId(scanDisplayData.getProjectID());
        ret.setScanID(scanDisplayData.getLastScanID());
        ret.setHighSeverityResults(scanDisplayData.getHighVulnerabilities());
        ret.setMediumSeverityResults(scanDisplayData.getMediumVulnerabilities());
        ret.setLowSeverityResults(scanDisplayData.getLowVulnerabilities());
        ret.setInfoSeverityResults(scanDisplayData.getInfoVulnerabilities());
        ret.setRiskLevelScore(scanDisplayData.getRiskLevelScore());

        return ret;
    }

    public static CliScanArgs genCliScanArgs(BaseScanConfiguration conf) {
        CliScanArgs cliScanArgs = new CliScanArgs();

        CxClientType cxClientType = CxClientType.SDK;
        try {
            cxClientType = CxClientType.valueOf(conf.getClientOrigin().name());
        } catch (Exception e) {
            log.debug("Failed to convert client origin enum from value: {}. client origin set to SDK", conf.getClientOrigin().name());
        }
        cliScanArgs.setClientOrigin(cxClientType);
        cliScanArgs.setIsIncremental(conf.isIncrementalScan());
        cliScanArgs.setIsPrivateScan(conf.isPrivateScan());
        cliScanArgs.setIgnoreScanWithUnchangedCode(conf.isIgnoreScanWithUnchangedCode());
        cliScanArgs.setComment(conf.getComment());

        ProjectSettings prjSettings = new ProjectSettings();
        String projectName = StringUtils.isEmpty(conf.getFullTeamPath()) ? conf.getProjectName() : conf.getFullTeamPath() + "\\" +conf.getProjectName();
        prjSettings.setProjectName(projectName);
        prjSettings.setPresetID(conf.getPresetId());
        prjSettings.setAssociatedGroupID(conf.getGroupId());
        prjSettings.setDescription(conf.getDescription());
        prjSettings.setIsPublic(conf.isPublic());
        prjSettings.setOwner(conf.getOwner());
        prjSettings.setProjectID(conf.getProjectId());
        cliScanArgs.setPrjSettings(prjSettings);

        return cliScanArgs;
    }

    public static String composeScanLink(String url, ScanResults scanResults) {
        return String.format( url + "/CxWebClient/ViewerMain.aspx?scanId=%s&ProjectID=%s", scanResults.getScanID(), scanResults.getProjectId());
    }

    public static String composeProjectStateLink(String url, long projectId) {
        return String.format( url + "/CxWebClient/portal#/projectState/%s/Summary", projectId);
    }

    public static String composeProjectOSASummaryLink(String url, long projectId) {
        return String.format( url + "/CxWebClient/portal#/projectState/%s/OSA", projectId);
    }


}
