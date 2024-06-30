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
import jetbrains.buildServer.log.Loggers;
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
    
    List<InvalidProperty> result = new Vector<>();

    public CxEditRunTypeControllerExtension(@NotNull final SBuildServer server,
                                            @NotNull final CxAdminConfig cxAdminConfig) {
    	Loggers.SERVER.info("CxEditRunTypeControllerExtension method start");

        server.registerExtension(EditRunTypeControllerExtension.class, CxConstants.RUNNER_TYPE, this);

        this.cxAdminConfig = cxAdminConfig;
    }

    public void fillModel(@NotNull final HttpServletRequest request,
                          @NotNull final BuildTypeForm form,
                          @NotNull final Map model) {
    	
    	Loggers.SERVER.info("fillModel method start");

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

        //put default project name as the build name
        if(StringUtils.isEmpty(properties.get(CxParam.PROJECT_NAME))) {
            properties.put(CxParam.PROJECT_NAME, form.getName());
        }
        //Default to true in case its an old job configuration.
        if(properties.get(CxParam.SAST_ENABLED) == null){
            properties.put(CxParam.SAST_ENABLED,CxConstants.TRUE);
        }

        //put all global properties to the config page
        for (String conf : CxParam.GLOBAL_CONFIGS) {
            properties.put(conf, cxAdminConfig.getConfiguration(conf));
        }

        model.put(CxParam.USE_DEFAULT_SERVER, properties.get(CxParam.USE_DEFAULT_SERVER));
        model.put(CxParam.SERVER_URL, properties.get(CxParam.SERVER_URL));
        model.put(CxParam.USERNAME, properties.get(CxParam.USERNAME));
        model.put(CxParam.PASSWORD, properties.get(CxParam.PASSWORD));
        model.put(CxParam.GLOBAL_SERVER_URL, cxAdminConfig.getConfiguration(CxParam.GLOBAL_SERVER_URL));
        model.put(CxParam.GLOBAL_USERNAME, cxAdminConfig.getConfiguration(CxParam.GLOBAL_USERNAME));
        model.put(CxParam.GLOBAL_PASSWORD, cxAdminConfig.getConfiguration(CxParam.GLOBAL_PASSWORD));

        Loggers.SERVER.info("fillModel method end");
    }

    public void updateState(@NotNull final HttpServletRequest request, @NotNull final BuildTypeForm form) {
    	Loggers.SERVER.info("updateState method start");
        //Empty implementation - currently not in use .
    	Loggers.SERVER.info("updateState method end");
    }

    @Nullable
    public StatefulObject getState(@NotNull final HttpServletRequest request, @NotNull final BuildTypeForm form) {
    	Loggers.SERVER.info("getState method start");
    	Loggers.SERVER.info("getState method end");
        return null;
        
    }

    public void updateBuildType(@NotNull final HttpServletRequest request,
                                @NotNull final BuildTypeForm form,
                                @NotNull final BuildTypeSettings buildTypeSettings,
                                @NotNull final ActionErrors errors) {
    	Loggers.SERVER.info("updateBuildType method start");
        //Empty implementation - currently not in use .
    	Loggers.SERVER.info("updateBuildType method start");
    }

    @NotNull
    public ActionErrors validate(@NotNull final HttpServletRequest request, @NotNull final BuildTypeForm form) {
    	
    	Loggers.SERVER.info("Validate method start");
        Map<String, String> properties = null;
        final BuildRunnerBean buildRunnerBean = form.getBuildRunnerBean();
        BasePropertiesBean basePropertiesBean = null;
        try {
            Method propertiesBeanMethod = BuildRunnerBean.class.getDeclaredMethod("getPropertiesBean");
            basePropertiesBean = (BasePropertiesBean) propertiesBeanMethod.invoke(buildRunnerBean);
            properties = basePropertiesBean.getProperties();
            
            Map<String, String> optionValues = form.getOptionValues();
            Loggers.SERVER.info("Actual option values: " + optionValues);
            
//            String[] fieldNames = form.getFieldNames();
//            for(String fieldName : fieldNames) {
//            	String fieldValue = form.getFieldValues(fieldName);
//            	Loggers.SERVER.info("Field Name: " + fieldName + ", Field values:" +fieldValue);
//            }
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
        
        //the jsp page dosent pass false value, so we need to check if it isnt true, null in this case, set it as false
        //this way we can distinguish in the build process between an old job (sast enabled == null) and a job where user specified not to run sast (sast_enabled == false)
        if(!TRUE.equals(properties.get(CxParam.SAST_ENABLED))) {
            properties.put(CxParam.SAST_ENABLED, CxConstants.FALSE);
        }
        
        String isSynchronous = properties.get(CxParam.IS_SYNCHRONOUS);
        String thresholdsEnabled = properties.get(CxParam.THRESHOLD_ENABLED);
        String useDefaultServer = properties.get(CxParam.USE_DEFAULT_SERVER);
        String enableCriticalSeverity = properties.get(CxParam.ENABLE_CRITICAL_SEVERITY);
        if((enableCriticalSeverity == null || enableCriticalSeverity.isEmpty())) {
        	enableCriticalSeverity = "noChange_9.0";
        }
        String criticalThreshold = properties.get(CxParam.CRITICAL_THRESHOLD);
//        
        Loggers.SERVER.info("is synchronous: " + isSynchronous);
        Loggers.SERVER.info("thresholds enabled: " + thresholdsEnabled);
        Loggers.SERVER.info("Use default server: " + useDefaultServer);
        Loggers.SERVER.info("Enable critical severity: " + enableCriticalSeverity);
        Loggers.SERVER.info("Critical threshold: " + criticalThreshold);
        
        Loggers.SERVER.info("Enable Critical Severity: " + enableCriticalSeverity +"******");
//        if (criticalThreshold == null) {
//        	   Loggers.SERVER.warn("criticalThreshold is null");
//        	}
        	if (enableCriticalSeverity == null) {
        	   Loggers.SERVER.warn("enableCriticalSeverity is null");
//        	   enableCriticalSeverity = CxParam.OPTION_FALSE;
        	}
        	ActionErrors actionErrors = new ActionErrors();
			if ("true".equals(isSynchronous)) {
				Loggers.SERVER.info("synchronous is true");
				if ("true".equals(thresholdsEnabled)) {
					Loggers.SERVER.info("thresholds enabled is true");
					Double version = 9.0;
	                String cxServerUrl = "";
	                String cxUser = "";
	                String cxPassword = "";
	                String proxyEnable = "";
					if("true".equals(useDefaultServer)) {//For global configuration case  
	                	Loggers.SERVER.info("Using default server configuration");
	                	cxServerUrl = cxAdminConfig.getConfiguration(CxParam.GLOBAL_SERVER_URL);
	                    cxUser = cxAdminConfig.getConfiguration(CxParam.GLOBAL_USERNAME);
	                    cxPassword = cxAdminConfig.getConfiguration(CxParam.GLOBAL_PASSWORD);       			
	        		}
	        		else { // For use specific case
	        			Loggers.SERVER.info("Using specific server configuration");
	        			cxServerUrl = properties.get(CxParam.SERVER_URL);
	        	        cxUser = properties.get(CxParam.USERNAME);
	        	        cxPassword = properties.get(CxParam.PASSWORD);      			
	        		}
					String sastVersion;
					try {
						sastVersion = SASTUtils.loginToServer(new URL(cxServerUrl), cxUser, decrypt(cxPassword));
						Loggers.SERVER.info("Fetched SAST version: " + sastVersion);
						System.out.println("Fetched SAST version: " + sastVersion);
						String[] sastVersionSplit = sastVersion.split("\\.");
						Loggers.SERVER.info("SAST version split: " + Arrays.toString(sastVersionSplit));
						if (sastVersionSplit.length > 1) {
							Loggers.SERVER.info("SAST version split[0]: " + sastVersionSplit[0]);
							Loggers.SERVER.info("SAST version split[1]: " + sastVersionSplit[1]);
							version = Double.parseDouble(sastVersionSplit[0] + "." + sastVersionSplit[1]);
							Loggers.SERVER.info("Parsed SAST version: " + version);
						} else {
							Loggers.SERVER.error("Unexpected SAST version format: " + sastVersion);
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Error fetching SAST version: " + e.getMessage());
						Loggers.SERVER.error("Error fetching SAST version: " + e.getMessage());
					}
					Double previousVersion = 9.0;
					Loggers.SERVER.info("enable Critical Severity *******: " + enableCriticalSeverity);
					if (enableCriticalSeverity != null) {
						String[] arr = enableCriticalSeverity.split("_");
						if (arr.length > 1) {
							previousVersion = Double.parseDouble(arr[1]);
						}
					}
					
					Loggers.SERVER.info("previous Version *******: " + previousVersion);
					if (!((previousVersion >= 9.7 && version >= 9.7) || (previousVersion < 9.7 && version < 9.7))) {
						if (version >= 9.7) {
							if(enableCriticalSeverity.startsWith("criticalSupported")) {
								enableCriticalSeverity = "noChange_" + version;
							}else {
								
							Loggers.SERVER.info("version greater or equal to 9.7:" + enableCriticalSeverity);
							enableCriticalSeverity = "criticalSupported_" + version;
							criticalThreshold = "";
							actionErrors.addError(new InvalidProperty(CxParam.CRITICAL_THRESHOLD, "*****The configured SAST version supports Critical severity. Critical threshold can also be configured."));
							System.out.println(
									"SAST version supports Critical severity. Critical threshold can also be configured.");
							Loggers.SERVER.info(
									"SAST version supports Critical severity. Critical threshold can also be configured.");
							}
						} else {
							if(enableCriticalSeverity.startsWith("criticalNotSupported")) {
								enableCriticalSeverity = "noChange_" + version;
							}else {
							Loggers.SERVER.info("version less than 9.7:" + enableCriticalSeverity);
							enableCriticalSeverity = "criticalNotSupported_" + version;
							criticalThreshold = "";
							actionErrors.addError(new InvalidProperty(CxParam.HIGH_THRESHOLD,"*******The configured SAST version does not supports Critical severity. Critical threshold can not be configured."));
							System.out.println(
									"SAST version does not support Critical severity. Critical threshold will not be applicable.");
							Loggers.SERVER.info(
									"SAST version does not support Critical severity. Critical threshold will not be applicable.");
							}
						}
					} else {
						enableCriticalSeverity = "noChange_" + version;
					}
				}
			}
			//form.setEnableCriticalSeverity(enableCriticalSeverity); // Set updated value in form
		    //form.setCriticalThreshold(criticalThreshold); // Set updated value in form
			properties.put(CxParam.CRITICAL_THRESHOLD, criticalThreshold);
			properties.put(CxParam.ENABLE_CRITICAL_SEVERITY, enableCriticalSeverity);
			basePropertiesBean.setProperties(properties);

        Loggers.SERVER.info("Validate method end");
        
        
        return actionErrors;
        
    }
}
