package com.checkmarx.teamcity.server;

import com.checkmarx.teamcity.common.CxConstants;
import com.checkmarx.teamcity.common.CxParam;
import jetbrains.buildServer.controllers.ActionMessages;
import jetbrains.buildServer.controllers.ActionErrors;
import jetbrains.buildServer.controllers.BasePropertiesBean;
import jetbrains.buildServer.controllers.StatefulObject;
import jetbrains.buildServer.controllers.admin.projects.BuildRunnerBean;
import jetbrains.buildServer.controllers.admin.projects.BuildTypeForm;
import jetbrains.buildServer.controllers.admin.projects.EditRunTypeControllerExtension;
import jetbrains.buildServer.serverSide.BuildTypeSettings;
import jetbrains.buildServer.serverSide.SBuildServer;
import static com.checkmarx.teamcity.common.CxUtility.encrypt;
import static com.checkmarx.teamcity.common.CxUtility.decrypt;
import com.checkmarx.teamcity.common.SASTUtils;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import jetbrains.buildServer.serverSide.InvalidProperty;
import java.util.Vector;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.net.URL;
import java.util.Arrays;


import static com.checkmarx.teamcity.common.CxConstants.TRUE;


public class CxEditRunTypeControllerExtension implements EditRunTypeControllerExtension {
    private final CxAdminConfig cxAdminConfig;
    private final String projectNameDelimiter = ",";
    
    List<InvalidProperty> result = new Vector<>();

    public CxEditRunTypeControllerExtension(@NotNull final SBuildServer server,
                                            @NotNull final CxAdminConfig cxAdminConfig) {

        server.registerExtension(EditRunTypeControllerExtension.class, CxConstants.RUNNER_TYPE, this);

        this.cxAdminConfig = cxAdminConfig;
    }

    public void fillModel(@NotNull final HttpServletRequest request,
                          @NotNull final BuildTypeForm form,
                          @NotNull final Map model) {

        Map<String, String> properties = null;
        final BuildRunnerBean buildRunnerBean = form.getBuildRunnerBean();
        try {
            Method propertiesBeanMethod = BuildRunnerBean.class.getDeclaredMethod("getPropertiesBean");
            BasePropertiesBean basePropertiesBean = (BasePropertiesBean) propertiesBeanMethod.invoke(buildRunnerBean);
            properties = basePropertiesBean.getProperties();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        PluginDataMigration migration = new PluginDataMigration();
        migration.migrate(properties);

        if(StringUtils.isEmpty(properties.get(CxParam.PROJECT_NAME))) {
            properties.put(CxParam.PROJECT_NAME, form.getName());
        }
        if(properties.get(CxParam.SAST_ENABLED) == null){
            properties.put(CxParam.SAST_ENABLED,CxConstants.TRUE);
        }

        for (String conf : CxParam.GLOBAL_CONFIGS) {
            properties.put(conf, cxAdminConfig.getConfiguration(conf));
        }
        
        String enableCriticalSeverityFromFile = fetchEnableCriticalSeverity(form.getExternalId());
        model.put("buildConfigurationId",form.getExternalId());
        model.put(CxParam.USE_DEFAULT_SERVER, properties.get(CxParam.USE_DEFAULT_SERVER));
        model.put(CxParam.SERVER_URL, properties.get(CxParam.SERVER_URL));
        model.put(CxParam.USERNAME, properties.get(CxParam.USERNAME));
        model.put(CxParam.PASSWORD, properties.get(CxParam.PASSWORD));
        model.put(CxParam.GLOBAL_SERVER_URL, cxAdminConfig.getConfiguration(CxParam.GLOBAL_SERVER_URL));
        model.put(CxParam.GLOBAL_USERNAME, cxAdminConfig.getConfiguration(CxParam.GLOBAL_USERNAME));
        model.put(CxParam.GLOBAL_PASSWORD, cxAdminConfig.getConfiguration(CxParam.GLOBAL_PASSWORD));
    }

    public void updateState(@NotNull final HttpServletRequest request, @NotNull final BuildTypeForm form) {
        //Empty implementation - currently not in use .
    }

    @Nullable
    public StatefulObject getState(@NotNull final HttpServletRequest request, @NotNull final BuildTypeForm form) {
        return null;
        
    }

    public void updateBuildType(@NotNull final HttpServletRequest request,
                                @NotNull final BuildTypeForm form,
                                @NotNull final BuildTypeSettings buildTypeSettings,
                                @NotNull final ActionErrors errors) {
        //Empty implementation - currently not in use .
    }
    
    private String fetchEnableCriticalSeverity(String projectName) {
        String criticalSeverityConfig = this.cxAdminConfig.getConfiguration(CxParam.ENABLE_CRITICAL_SEVERITY);
        String[] keys = criticalSeverityConfig.split("\\|");
        String enableCriticalSeverity = null;
        String currentKey = null;

        for (String key : keys) {
            if (key.startsWith(projectName)) {
            	currentKey = key;
                enableCriticalSeverity = key.replace(projectName + projectNameDelimiter, "");
                break;
            }
        }
        if((enableCriticalSeverity == null || enableCriticalSeverity.isEmpty())) {
        	enableCriticalSeverity = "noChange_9.0_9.0_http://localhost";
        }
        return enableCriticalSeverity;
    }

    @NotNull
    public ActionErrors validate(@NotNull final HttpServletRequest request, @NotNull final BuildTypeForm form) {
        Map<String, String> properties = null;
        final BuildRunnerBean buildRunnerBean = form.getBuildRunnerBean();
        BasePropertiesBean basePropertiesBean = null;
        try {
            Method propertiesBeanMethod = BuildRunnerBean.class.getDeclaredMethod("getPropertiesBean");
            basePropertiesBean = (BasePropertiesBean) propertiesBeanMethod.invoke(buildRunnerBean);
            properties = basePropertiesBean.getProperties();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        String cxPass = properties.get(CxParam.PASSWORD);

        try {
            if(cxPass != null) {
                cxPass = encrypt(cxPass);
            }
        } catch (RuntimeException e) {
            cxPass = "";
        }
        properties.put(CxParam.PASSWORD, cxPass);
        
        String scaPass = properties.get(CxParam.SCA_PASSWORD);

        try {
            if(scaPass != null) {
            	scaPass = encrypt(scaPass);
            }
        } catch (RuntimeException e) {
        	scaPass = "";
        }
        properties.put(CxParam.SCA_PASSWORD, scaPass);
        
        String scaSastPass = properties.get(CxParam.SCA_SAST_SERVER_PASSWORD);

        try {
            if(scaSastPass != null) {
            	scaSastPass = encrypt(scaSastPass);
            }
        } catch (RuntimeException e) {
        	scaSastPass = "";
        }
        properties.put(CxParam.SCA_SAST_SERVER_PASSWORD, scaSastPass);
        
        if(!TRUE.equals(properties.get(CxParam.SAST_ENABLED))) {
            properties.put(CxParam.SAST_ENABLED, CxConstants.FALSE);
        }
        String isSynchronous = properties.get(CxParam.IS_SYNCHRONOUS);
        String thresholdsEnabled = properties.get(CxParam.THRESHOLD_ENABLED);
        String useDefaultServer = properties.get(CxParam.USE_DEFAULT_SERVER);
        String projectName = form.getExternalId();
        String criticalSeverityConfig = this.cxAdminConfig.getConfiguration(CxParam.ENABLE_CRITICAL_SEVERITY);
        String[] keys = criticalSeverityConfig.split("\\|");
        String enableCriticalSeverity = null;
        String currentKey = null;

        for (String key : keys) {
            if (key.startsWith(projectName)) {
            	currentKey = key;
                enableCriticalSeverity = key.replace(projectName + projectNameDelimiter, "");
                break;
            }
        }
        if((enableCriticalSeverity == null || enableCriticalSeverity.isEmpty())) {
        	enableCriticalSeverity = "noChange_9.0_9.0_http://localhost";
        }
        String criticalThreshold = properties.get(CxParam.CRITICAL_THRESHOLD);        
        	if (enableCriticalSeverity == null) {
        	}
        	ActionErrors actionErrors = new ActionErrors();
			if ("true".equals(isSynchronous)) {
				if ("true".equals(thresholdsEnabled)) {
					Double version = 9.0;
	                String cxServerUrl = "";
	                String cxUser = "";
	                String cxPassword = "";
	                String proxyEnable = "";
					if("true".equals(useDefaultServer)) {//For global configuration case  
	                	cxServerUrl = cxAdminConfig.getConfiguration(CxParam.GLOBAL_SERVER_URL);
	                    cxUser = cxAdminConfig.getConfiguration(CxParam.GLOBAL_USERNAME);
	                    cxPassword = cxAdminConfig.getConfiguration(CxParam.GLOBAL_PASSWORD);       			
	        		}
	        		else { // For use specific case
	        			cxServerUrl = properties.get(CxParam.SERVER_URL);
	        	        cxUser = properties.get(CxParam.USERNAME);
	        	        cxPassword = properties.get(CxParam.PASSWORD);      			
	        		}
					String sastVersion;
					try {
						sastVersion = SASTUtils.loginToServer(new URL(cxServerUrl), cxUser, decrypt(cxPassword));
						String[] sastVersionSplit = sastVersion.split("\\.");
						if (sastVersionSplit.length > 1) {
							version = Double.parseDouble(sastVersionSplit[0] + "." + sastVersionSplit[1]);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					Double previousVersion = 9.0;
					if (enableCriticalSeverity != null) {
						String[] arr = enableCriticalSeverity.split("_");
						if (arr.length > 1) {
							previousVersion = Double.parseDouble(arr[1]);
						}
					}
					if (!((previousVersion >= 9.7 && version >= 9.7) || (previousVersion < 9.7 && version < 9.7))) {
						if (version >= 9.7) {
							if(enableCriticalSeverity.startsWith("criticalSupported")) {
								enableCriticalSeverity = "noChange_" + version + "_" + previousVersion + "_" + cxServerUrl;
							}else {
							enableCriticalSeverity = "criticalSupported_" + version + "_" + previousVersion + "_" + cxServerUrl;
							criticalThreshold = "";
							}
						} else {
							if(enableCriticalSeverity.startsWith("criticalNotSupported")) {
								enableCriticalSeverity = "noChange_" + version + "_" + previousVersion + "_" + cxServerUrl;
							}else {
							enableCriticalSeverity = "criticalNotSupported_" + version + "_" + previousVersion + "_" + cxServerUrl;
							criticalThreshold = "";
							}
						}
					} else {
						enableCriticalSeverity = "noChange_" + version + "_" + previousVersion + "_" + cxServerUrl;
					}
				}
			}
			if (currentKey != null) {
				if(currentKey.equalsIgnoreCase(criticalSeverityConfig)) {
					criticalSeverityConfig = "";
				}else {
					if(criticalSeverityConfig.startsWith(currentKey)) {
						criticalSeverityConfig = criticalSeverityConfig.replace(currentKey + "|" , "");
					}else {
						criticalSeverityConfig = criticalSeverityConfig.replace("|"+currentKey, "");
					}
				}
	        }
			if(criticalSeverityConfig == null || criticalSeverityConfig.isEmpty() || "|".equalsIgnoreCase(criticalSeverityConfig.trim())) {
				criticalSeverityConfig = projectName + projectNameDelimiter + enableCriticalSeverity;
			}else {
				criticalSeverityConfig = criticalSeverityConfig + "|" + projectName + projectNameDelimiter + enableCriticalSeverity;
			}
			properties.put(CxParam.CRITICAL_THRESHOLD, criticalThreshold);
			properties.put(CxParam.ENABLE_CRITICAL_SEVERITY, criticalSeverityConfig);
			try {
				
			       this.cxAdminConfig.setConfiguration(CxParam.ENABLE_CRITICAL_SEVERITY, criticalSeverityConfig);
			       this.cxAdminConfig.persistConfiguration();
			   } catch (IOException e) {
			       e.printStackTrace();
			   }
        
        
        return actionErrors;
        
    }
}
