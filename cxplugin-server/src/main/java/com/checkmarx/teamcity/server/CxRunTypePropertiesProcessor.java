package com.checkmarx.teamcity.server;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.util.PropertiesUtil;

import com.checkmarx.teamcity.common.CxConstants;
import com.checkmarx.teamcity.common.CxUrlValidation;


public class CxRunTypePropertiesProcessor implements PropertiesProcessor {
    private final static String CANNOT_BE_EMPTY = " cannot be empty";

    public Collection<InvalidProperty> process(Map<String, String> properties) {
        List<InvalidProperty> result = new Vector<>();

        if (PropertiesUtil.isEmptyOrNull(properties.get(CxConstants.CXGLOBALSERVER))) {
            final String cxServerUrl = properties.get(CxConstants.CXSERVERURL);
            if (PropertiesUtil.isEmptyOrNull(cxServerUrl)) {
                result.add(new InvalidProperty(CxConstants.CXSERVERURL, "Server URL" + CANNOT_BE_EMPTY));
            } else {
                try {
                    CxUrlValidation.validate(cxServerUrl);
                } catch (MalformedURLException e) {
                    result.add(new InvalidProperty(CxConstants.CXSERVERURL, "Server URL malformed or has path"));
                }
            }
            if (PropertiesUtil.isEmptyOrNull(properties.get(CxConstants.CXUSER))) {
                result.add(new InvalidProperty(CxConstants.CXUSER, "User" + CANNOT_BE_EMPTY));
            }
            if (PropertiesUtil.isEmptyOrNull(properties.get(CxConstants.CXPASS))) {
                result.add(new InvalidProperty(CxConstants.CXPASS, "Password" + CANNOT_BE_EMPTY));
            }
        }

        if (PropertiesUtil.isEmptyOrNull(properties.get(CxConstants.CXPROJECT))) {
            result.add(new InvalidProperty(CxConstants.CXPROJECT, "Project" + CANNOT_BE_EMPTY));
        }
        if (PropertiesUtil.isEmptyOrNull(properties.get(CxConstants.CXPRESET))) {
            result.add(new InvalidProperty(CxConstants.CXPRESET, "Preset" + CANNOT_BE_EMPTY));
        }
        if (PropertiesUtil.isEmptyOrNull(properties.get(CxConstants.CXCONFIGURATION))) {
            result.add(new InvalidProperty(CxConstants.CXCONFIGURATION, "Configuration" + CANNOT_BE_EMPTY));
        }
        if (PropertiesUtil.isEmptyOrNull(properties.get(CxConstants.CXTEAM))) {
            result.add(new InvalidProperty(CxConstants.CXTEAM, "Team" + CANNOT_BE_EMPTY));
        }
        if (!PropertiesUtil.isEmptyOrNull(properties.get(CxConstants.CXINCREMENTAL))) {
            if (!PropertiesUtil.isEmptyOrNull(properties.get(CxConstants.CXPERIODICFULLSCANS))) {
                final String cxNumberIncremental = properties.get(CxConstants.CXNUMBERINCREMENTAL);
                if (PropertiesUtil.isEmptyOrNull(cxNumberIncremental)) {
                    result.add(new InvalidProperty(CxConstants.CXNUMBERINCREMENTAL, "Number of incremental scans" + CANNOT_BE_EMPTY));
                } else {
                    final int cxNumberIncrementalInt = Integer.parseInt(cxNumberIncremental);
                    if (cxNumberIncrementalInt < 1 || cxNumberIncrementalInt > 99) {
                        result.add(new InvalidProperty(CxConstants.CXNUMBERINCREMENTAL, "Number of incremental scans must be between 1-99"));
                    }
                }
            }
        }
        if (!PropertiesUtil.isEmptyOrNull(properties.get(CxConstants.CXTHRESHOLDENABLE))) {
            if (PropertiesUtil.isEmptyOrNull(properties.get(CxConstants.CXTHRESHOLDHIGH))) {
                result.add(new InvalidProperty(CxConstants.CXTHRESHOLDHIGH, "Threshold" + CANNOT_BE_EMPTY));
            }
            if (PropertiesUtil.isEmptyOrNull(properties.get(CxConstants.CXTHRESHOLDMEDIUM))) {
                result.add(new InvalidProperty(CxConstants.CXTHRESHOLDMEDIUM, "Threshold" + CANNOT_BE_EMPTY));
            }
            if (PropertiesUtil.isEmptyOrNull(properties.get(CxConstants.CXTHRESHOLDLOW))) {
                result.add(new InvalidProperty(CxConstants.CXTHRESHOLDLOW, "Threshold" + CANNOT_BE_EMPTY));
            }
        }

        return result;
    }
}
