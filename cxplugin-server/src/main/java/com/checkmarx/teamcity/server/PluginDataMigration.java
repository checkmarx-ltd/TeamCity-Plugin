package com.checkmarx.teamcity.server;

import com.checkmarx.teamcity.common.CxConstants;
import com.checkmarx.teamcity.common.CxParam;
import com.cx.restclient.dto.DependencyScannerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

class PluginDataMigration {
    private static final Logger log = LoggerFactory.getLogger(CxOptions.class);

    void migrate(Map<String, String> properties) {
        if (CxConstants.TRUE.equals(properties.get(CxParam.OSA_ENABLED))) {
            log.info("Migrating properties to new format.");
            properties.put(CxParam.DEPENDENCY_SCANNER_TYPE, DependencyScannerType.OSA.toString());
            properties.put(CxParam.DEPENDENCY_SCAN_ENABLED, CxConstants.TRUE);
            properties.put(CxParam.OVERRIDE_GLOBAL_CONFIGURATIONS, CxConstants.TRUE);
            properties.remove(CxParam.OSA_ENABLED);
        }
    }
}
