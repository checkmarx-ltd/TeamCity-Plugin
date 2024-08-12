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
    private final String projectNameDelimiter = "___";
    //private static boolean errorShown = false;
    
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
        	System.out.println("FillModel method:"+conf+":"+cxAdminConfig.getConfiguration(conf));
            properties.put(conf, cxAdminConfig.getConfiguration(conf));
        }
        
        System.out.println("form.getExternalId()" + form.getExternalId());
        String enableCriticalSeverityFromFile = fetchEnableCriticalSeverity(form.getExternalId());
        System.out.println("enableCriticalSeverityFromFile: " + enableCriticalSeverityFromFile);
//        model.put("enableCriticalSeverityFromFile",enableCriticalSeverityFromFile);
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
    	System.out.println("######################### In updateState");
    	System.out.println("form.getExternalId()" + form.getExternalId());
        String enableCriticalSeverityFromFile = fetchEnableCriticalSeverity(form.getExternalId());
        System.out.println("enableCriticalSeverityFromFile: " + enableCriticalSeverityFromFile);
    }

    @Nullable
    public StatefulObject getState(@NotNull final HttpServletRequest request, @NotNull final BuildTypeForm form) {
    	System.out.println("######################### In getState");
    	System.out.println("form.getExternalId()" + form.getExternalId());
        String enableCriticalSeverityFromFile = fetchEnableCriticalSeverity(form.getExternalId());
        System.out.println("enableCriticalSeverityFromFile: " + enableCriticalSeverityFromFile);
        return null;
        
    }

    public void updateBuildType(@NotNull final HttpServletRequest request,
                                @NotNull final BuildTypeForm form,
                                @NotNull final BuildTypeSettings buildTypeSettings,
                                @NotNull final ActionErrors errors) {
        //Empty implementation - currently not in use .
    }
    
    private String fetchEnableCriticalSeverity(String projectName) {
    	System.out.println("project Name useDefaultServer projectName2" + projectName);
        String something = this.cxAdminConfig.getConfiguration(CxParam.ENABLE_CRITICAL_SEVERITY);
        String[] keys = something.split("\\|");
        String enableCriticalSeverity = null;
        String someKey = null;

        for (String key : keys) {
            if (key.startsWith(projectName)) {
                someKey = key;
                enableCriticalSeverity = key.replace(projectName + projectNameDelimiter, "");
                break;
            }
        }
        System.out.println("enable Critical Severity below for loop : " + enableCriticalSeverity);
        System.out.println("some Key below for loop : " + someKey);
        if((enableCriticalSeverity == null || enableCriticalSeverity.isEmpty())) {
        	enableCriticalSeverity = "noChange_9.0_9.0_http://localhost";
        }
        return enableCriticalSeverity;
    }

    @NotNull
    public ActionErrors validate(@NotNull final HttpServletRequest request, @NotNull final BuildTypeForm form) {
        Map<String, String> properties = null;
        final BuildRunnerBean buildRunnerBean = form.getBuildRunnerBean();
        System.out.println("form.getExternalId()" + form.getExternalId());
        System.out.println("form.getName()" + form.getName());
        System.out.println("form.getPublicKey()" + form.getPublicKey());
        BasePropertiesBean basePropertiesBean = null;
        try {
            Method propertiesBeanMethod = BuildRunnerBean.class.getDeclaredMethod("getPropertiesBean");
            basePropertiesBean = (BasePropertiesBean) propertiesBeanMethod.invoke(buildRunnerBean);
            properties = basePropertiesBean.getProperties();
            
//            if (properties != null) {
//                System.out.println("Properties:");
//                for (Map.Entry<String, String> entry : properties.entrySet()) {
//                    System.out.println(entry.getKey() + " = " + entry.getExternalId());
//                    System.out.println(entry.getKey() + " = " + entry.getExternalId());
//                    System.out.println(entry.getKey() + " = " + entry.getExternalId());
//                }
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
        String projectName = form.getExternalId();
        System.out.println("project Name useDefaultServer projectName2" + projectName);
        String something = this.cxAdminConfig.getConfiguration(CxParam.ENABLE_CRITICAL_SEVERITY);
        String[] keys = something.split("\\|");
        String enableCriticalSeverity = null;
        String someKey = null;

        for (String key : keys) {
            if (key.startsWith(projectName)) {
                someKey = key;
                //Removing project name and three underscores 
                enableCriticalSeverity = key.replace(projectName + projectNameDelimiter, "");
                break;
            }
        }
        System.out.println("enable Critical Severity below for loop : " + enableCriticalSeverity);
        System.out.println("some Key below for loop : " + someKey);
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
			if (someKey != null) {
				System.out.println("something before something : " + something);
				if(someKey.equalsIgnoreCase(something)) {
					something="";
				}else {
	            something = something.replace("|"+someKey, "");
				}
	            System.out.println("something after something : " + something);
	        }
			//The string in file will be appended with project name and three underscores
			if(something==null || something.isEmpty() || "|".equalsIgnoreCase(something.trim())) {
				something = projectName + projectNameDelimiter + enableCriticalSeverity;
			}else {
				something = something + "|" + projectName + projectNameDelimiter + enableCriticalSeverity;
			}
	        System.out.println("something after something below if condidtion : " + something);
			properties.put(CxParam.CRITICAL_THRESHOLD, criticalThreshold);
			properties.put(CxParam.ENABLE_CRITICAL_SEVERITY, something);
			try {
				
			       this.cxAdminConfig.setConfiguration(CxParam.ENABLE_CRITICAL_SEVERITY, something);
			       this.cxAdminConfig.persistConfiguration();
			   } catch (IOException e) {
			       e.printStackTrace();
			   }
        
        
        return actionErrors;
        
    }
}
