<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<script type="text/javascript" src="<c:url value='${teamcityPluginResourcesPath}testConnection.js'/>"></script>

    <script type="text/javascript">
    window.updateDependencyScanSectionVisibility = function() {
        var depScanEnabled = jQuery('#dependencyScanEnabled').prop('checked'),
            overrideChecked = jQuery('#OverrideGlobalConfigurations').prop('checked'),
            overrideSASTChecked = jQuery('#useSASTDefaultServer').prop('checked'),
            osaEnabled = jQuery('#OsaEnabled').prop('checked'),
            scaEnabled = jQuery('#enableSca').prop('checked'),
            isEnableExpPath = jQuery('#enableExpPath').prop('checked'),
            isEnableSCAResolver = jQuery('#ScaResolverEnabled').prop('checked'),
            isManifestFileEnabled = jQuery('#ManifestFileEnabled').prop('checked'),
            isOverriding = depScanEnabled && overrideChecked;
            
        	
        	isSASTOverridingForSCA = isOverriding && isEnableExpPath && (!overrideSASTChecked);
        jQuery('#overrideGlobalDSSettings')[depScanEnabled ? 'show' : 'hide']();
        jQuery('#overrideGlobalSASTSettings')[isEnableExpPath ? 'show' : 'hide']();
        jQuery('.dependencyScanRow')[isOverriding ? 'show' : 'hide']();

        jQuery('.osaInput')[isOverriding && osaEnabled ? 'show' : 'hide']();
        jQuery('.scaInput')[isOverriding && scaEnabled ? 'show' : 'hide']();
        
        jQuery('.expPath')[isOverriding && scaEnabled && isEnableExpPath ? 'show' : 'hide']();
        jQuery('.sastDetailsRow')[isSASTOverridingForSCA ? 'show' : 'hide']();
        
        jQuery('.enableSCAResolver')[isOverriding && scaEnabled && isEnableSCAResolver? 'show' : 'hide']();
        jQuery('.enableManifestFile')[isOverriding && scaEnabled && isManifestFileEnabled? 'show' : 'hide']();
        
    }

    console.log('updateDependencyScanSectionVisibility');
    jQuery(updateDependencyScanSectionVisibility);

    window.updatePeriodicScanSectionVisibility = function() {
        var incrementalScanEnabled = jQuery('#cxIsIncremental').prop('checked'),
            isPeriodicFullScanEnabled = jQuery('#cxIsPeriodicFullScan').prop('checked'),
            periodindFullScanAfter = incrementalScanEnabled && isPeriodicFullScanEnabled;

        jQuery('#cxIsPeriodicFullScan')[incrementalScanEnabled ? 'show' : 'hide']();
        jQuery('#incrementalSection')[incrementalScanEnabled ? 'show' : 'hide']();
        jQuery('#cxPeriodicFullScanAfter')[periodindFullScanAfter ? 'show' : 'hide']();
        jQuery('#periodicScanSection')[periodindFullScanAfter ? 'show' : 'hide']();

    }

    console.log('updatePeriodicScanSectionVisibility');
    jQuery(updatePeriodicScanSectionVisibility);

    window.Checkmarx = {
    		extractSASTCredentials: function () {
        return {
            sastServerUrl: $('scaSASTServerUrl').value,
            sastUsername: $('scaSASTUserName').value,
            sastPssd: $('prop:encrypted:scaSASTPassword').value ? $('prop:encrypted:scaSASTPassword').value : $('scaSASTPassword').value
        };
    },
    
    extractGlobalSASTCredentials: function () {
        return {
        	sastServerUrl: $('cxGlobalSastServerUrl').value,
        	sastUsername: $('cxGlobalSastUsername').value,
        	sastPssd: $('cxGlobalSastPassword').value,
            global: true
        }
    },
    extractCredentials: function () {
        return {
            serverUrl: $('cxServerUrl').value,
            username: $('cxUsername').value,
            pssd: $('prop:encrypted:cxPassword').value ? $('prop:encrypted:cxPassword').value : $('cxPassword').value
        };
    },
    
    extractGlobalCredentials: function () {
        return {
            serverUrl: $('cxGlobalServerUrl').value,
            username: $('cxGlobalUsername').value,
            pssd: $('cxGlobalPassword').value,
            global: true
        }
    },
       extractGlobalSCAparameters: function () {
        return {
            serverUrl: $('cxGlobalSCAServerUrl').value,
            accessControlServerUrl: $('cxGlobalSCAAccessControlServerURL').value,
            webAppURL: $('cxGlobalSCAWebAppURL').value,
            scaUserName: $('cxGlobalSCAUserName').value,
            scaPassword: $('cxGlobalSCAPassword').value,
            scaTenant: $('cxGlobalSCATenant').value,
            global: true
        }
    },
    extractSCAparameters: function () {
            return {
                serverUrl: $('scaApiUrl').value,
                accessControlServerUrl: $('scaAccessControlUrl').value,
                webAppURL: $('scaWebAppUrl').value,
                scaUserName: $('scaUserName').value,
                scaPassword: $('prop:encrypted:scaPass').value ? $('prop:encrypted:scaPass').value : $('scaPass').value,
                scaTenant: $('scaTenant').value,
                global: false
            }
        },
    testConnection: function (credentials) {
        if (Checkmarx.validateCredentials(credentials)) {
            var messageElm = jQuery('#testConnectionMsg');
            var buttonElm = jQuery('#testConnection');
			console.log('testConnection');
            messageElm.removeAttr("style");
            messageElm.text('');
            buttonElm.attr("disabled", true);
            buttonElm.css('cursor','wait');
            jQuery.ajax({
                type: 'POST',
                url: window['base_uri'] + '/checkmarx/testConnection/',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(credentials),
                success: function (data) {
                    buttonElm.attr("disabled", false);
                    buttonElm.removeAttr("style");

                    messageElm.text( data.message);
                    if(data.success) {
                        messageElm.css('color','green');
                    } else {
                        messageElm.css('color','red');
                    }

                    if(!credentials.global) {
                        Checkmarx.populateDropdownList(data.presetList, '#cxPresetId', 'id', 'name');
                        Checkmarx.populateDropdownList(data.teamPathList, '#cxTeamId', 'id', 'fullName');
                        Checkmarx.populateDropdownList(data.engineConfigList, '#cxEngineConfigId', 'id', 'name');
                    }

                },
                error: function (data) {
                }
            });
        }
    },
    testScaSASTConnection: function (credentials) {
        if (Checkmarx.validateSASTCredentials(credentials)) {
            var messageElm = jQuery('#testScaSASTConnectionMsg');
            var buttonElm = jQuery('#testScaSASTConnection');
			console.log('testScaSASTConnection');
            messageElm.removeAttr("style");
            messageElm.text('');
            buttonElm.attr("disabled", true);
            buttonElm.css('cursor','wait');
            jQuery.ajax({
                type: 'POST',
                url: window['base_uri'] + '/checkmarx/testScaSastConnection/',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(credentials),
                success: function (data) {
                    buttonElm.attr("disabled", false);
                    buttonElm.removeAttr("style");

                    messageElm.text( data.message);
                    if(data.success) {
                        messageElm.css('color','green');
                    } else {
                        messageElm.css('color','red');
                    }

                    if(!credentials.global) {
                        Checkmarx.populateDropdownList(data.presetList, '#cxPresetId', 'id', 'name');
                        Checkmarx.populateDropdownList(data.teamPathList, '#cxTeamId', 'id', 'fullName');
                        Checkmarx.populateDropdownList(data.engineConfigList, '#cxEngineConfigId', 'id', 'name');
                    }

                },
                error: function (data) {
                }
            });
        }
    },
    
 testSCAConnection: function (credentials) {
        if (Checkmarx.validateSCAParameters(credentials)) {
            var messageElm = jQuery('#testSCAConnectionMsg');
            var buttonElm = jQuery('#testConnectionSCA');

            messageElm.removeAttr("style");
            messageElm.text('');
            buttonElm.attr("disabled", true);
            buttonElm.css('cursor','wait');
            jQuery.ajax({
                type: 'POST',
                url: window['base_uri'] + '/checkmarx/testSCAConnection/',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(credentials),
                success: function (data) {
                    buttonElm.attr("disabled", false);
                    buttonElm.removeAttr("style");

                    messageElm.text( data.message);
                    if(data.success) {
                        messageElm.css('color','green');
                    } else {
                        messageElm.css('color','red');
                    }

//                    if(!credentials.global) {
//                        Checkmarx.populateDropdownList(data.presetList, '#cxPresetId', 'id', 'name');
//                        Checkmarx.populateDropdownList(data.teamPathList, '#cxTeamId', 'id', 'fullName');
//                    }

                },
                error: function (data) {
                }
            });
        }
    },


    validateCredentials: function (credentials) {
        var messageElm = jQuery('#testConnectionMsg');
        if (!credentials.serverUrl) {
            messageElm.text('URL must not be empty');
            messageElm.css('color','red');
            return false;
        }

        if (!credentials.username) {
            messageElm.text('Username must not be empty');
            messageElm.css('color','red');
            return false;
        }

        if (!credentials.pssd) {
            messageElm.text('Password must not be empty');
            messageElm.css('color','red');
            return false;
        }

        return true;

    },
    
    validateSASTCredentials: function (credentials) {
        var messageElm = jQuery('#testScaSASTConnectionMsg');
        if (!credentials.sastServerUrl) {
            messageElm.text('URL must not be empty');
            messageElm.css('color','red');
            return false;
        }

        if (!credentials.sastUsername) {
            messageElm.text('Username must not be empty');
            messageElm.css('color','red');
            return false;
        }

        if (!credentials.sastPssd) {
            messageElm.text('Password must not be empty');
            messageElm.css('color','red');
            return false;
        }

        return true;

    },

validateSCAParameters: function (credentials) {
        var messageElm = jQuery('#testSCAConnectionMsg');
        if (!credentials.serverUrl) {
            messageElm.text('URL must not be empty');
            messageElm.css('color','red');
            return false;
        }
        if (!credentials.scaUserName) {
            messageElm.text('User name must not be empty');
            messageElm.css('color','red');
            return false;
        }
        if (!credentials.scaPassword) {
            messageElm.text('Password must not be empty');
            messageElm.css('color','red');
            return false;
        }
        if (!credentials.accessControlServerUrl) {
            messageElm.text('Access control URL must not be empty');
            messageElm.css('color','red');
            return false;
        }
        if (!credentials.scaTenant) {
            messageElm.text('tenant must not be empty');
            messageElm.css('color','red');
            return false;
        }
        return true;

    },

    populateDropdownList: function(data, selector, key, name) {
        jQuery(selector).empty();
        var l = data.length;
         for (var i = 0; i < l; ++i) {
            jQuery(selector).append('<option value="' + data[i][key] + '">' + data[i][name] + '</option>');
        }
}


};


    </script>



<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>
<jsp:useBean id="optionsBean" class="com.checkmarx.teamcity.server.CxOptions"/>

<style>
    #cxPresetId, #cxTeamId {
        width: 535px;
    }

    .scanControlSectionTable {
        margin-left: -10px;
    }

    .runnerFormTable .cx-title.groupingTitle td {
        padding: 6px 0 6px 8px;
        background-color: #edeff5;
        font-weight: bold;
        font-size: 16px;
    }

</style>

${'true'.equals(cxUseDefaultServer) ?
optionsBean.testConnection(cxGlobalServerUrl, cxGlobalUsername, cxGlobalPassword) :
optionsBean.testConnection(cxServerUrl, cxUsername, cxPassword)}

${'true'.equals(useSASTDefaultServer) ?
optionsBean.testSASTConnection(cxGlobalSastServerUrl, cxGlobalSastUsername, cxGlobalSastPassword) :
optionsBean.testSASTConnection(scaSASTServerUrl, scaSASTUserName, scaSASTPassword)}

<c:if test="${propertiesBean.properties[optionsBean.useDefaultServer] == 'true'}">
    <c:set var="hideServerOverrideSection" value="${optionsBean.noDisplay}"/>
</c:if>

<c:if test="${propertiesBean.properties[optionsBean.useDefaultSastConfig] == 'true'}">
    <c:set var="hideSastConfigSection" value="${optionsBean.noDisplay}"/>
</c:if>
<c:if test="${propertiesBean.properties[optionsBean.sastEnabled] != 'true'}">
    <c:set var="hideCxSast" value="${optionsBean.noDisplay}"/>
</c:if>
<c:if test="${propertiesBean.properties[optionsBean.useDefaultSastConfig] != 'true'}">
    <c:set var="hideDefaultSastConfigSection" value="${optionsBean.noDisplay}"/>
</c:if>

<c:if test="${propertiesBean.properties[optionsBean.globalIsSynchronous] == 'true'}">
    <c:set var="globalIsSynchronus" value="true"/>
</c:if>
<c:if test="${propertiesBean.properties[optionsBean.globalIsSynchronous] != 'true'}">
    <c:set var="globalIsSynchronus" value="false"/>
</c:if>

<c:if test="${propertiesBean.properties[optionsBean.globalThresholdEnabled] == 'true'}">
    <c:set var="globalThresholdEnabled" value="true"/>
</c:if>
<c:if test="${propertiesBean.properties[optionsBean.globalThresholdEnabled] != 'true'}">
    <c:set var="globalThresholdEnabled" value="false"/>
</c:if>

<c:if test="${propertiesBean.properties[optionsBean.globalProjectPolicyViolation] == 'true'}">
    <c:set var="globalProjectPolicydEnabled" value="true"/>
</c:if>
<c:if test="${propertiesBean.properties[optionsBean.globalProjectPolicyViolation] != 'true'}">
    <c:set var="globalProjectPolicydEnabled" value="false"/>
</c:if>

<c:if test="${propertiesBean.properties[optionsBean.globalOsaThresholdEnabled] == 'true'}">
    <c:set var="globalOsaThresholdEnabled" value="true"/>
</c:if>
<c:if test="${propertiesBean.properties[optionsBean.globalOsaThresholdEnabled] != 'true'}">
    <c:set var="globalOsaThresholdEnabled" value="false"/>
</c:if>

<c:if test="${propertiesBean.properties[optionsBean.useDefaultScanControl] == 'true'}">
    <c:set var="hideSpecificScanControlSection" value="${optionsBean.noDisplay}"/>
</c:if>
<c:if test="${propertiesBean.properties[optionsBean.useDefaultScanControl] != 'true'}">
    <c:set var="hideDefaultScanControlSection" value="${optionsBean.noDisplay}"/>
</c:if>

<c:if test="${propertiesBean.properties[optionsBean.isSynchronous] != 'true'}">
    <c:set var="hideScanControlSection" value="${optionsBean.noDisplay}"/>
</c:if>

<c:if test="${propertiesBean.properties[optionsBean.thresholdEnabled] != 'true' }">
    <c:set var="hideThresholdSection" value="${optionsBean.noDisplay}"/>
</c:if>
<c:if test="${propertiesBean.properties[optionsBean.osaThresholdEnabled] != 'true'}">
    <c:set var="hideOsaThresholdSection" value="${optionsBean.noDisplay}"/>
</c:if>
<c:if test="${propertiesBean.properties[optionsBean.isIncremental] != 'true'}">
    <c:set var="hideIncrementalSection" value="${optionsBean.noDisplay}"/>
</c:if>
<c:if test="${propertiesBean.properties[optionsBean.isPeriodicFullScan] != 'true'}">
    <c:set var="hidePeriodicScanSection" value="${optionsBean.noDisplay}"/>
</c:if>


<l:settingsGroup className="cx-title" title="Checkmarx Server">
    <tr>
        <th>
            <label for="${optionsBean.useDefaultServer}">Use Default Credentials<br>
            Server URL: ${propertiesBean.properties[optionsBean.globalServerUrl]}, <br>
            Username: ${propertiesBean.properties[optionsBean.globalUsername]}</label>
        </th>
        <td>
            <c:set var="onclick">
                $('serverOverrideSection').toggle();
            </c:set>
            <props:checkboxProperty name="${optionsBean.useDefaultServer}" onclick="${onclick}"/>
        </td>
    </tr>
    <tbody id="serverOverrideSection" ${hideServerOverrideSection}>
    <tr>
        <th><label for="${optionsBean.serverUrl}">Server URL<l:star/></label></th>
        <td>
            <props:textProperty name="${optionsBean.serverUrl}" className="longField"/>
            <span class="error" id="error_${optionsBean.serverUrl}"></span>
        </td>
    </tr>
    <tr>
        <th><label for="${optionsBean.username}">Username<l:star/></label></th>
        <td>
            <props:textProperty name="${optionsBean.username}" className="longField"/>
            <span class="error" id="error_${optionsBean.username}"></span>
        </td>
    </tr>
    <tr>
        <th><label for="${optionsBean.password}">Password<l:star/></label></th>
        <td>
            <props:passwordProperty name="${optionsBean.password}" className="longField"/>
            <span class="error" id="error_${optionsBean.password}"></span>
        </td>
    </tr>
    <td>
        <form>
            <input id="testConnection" type="button" name="TestConnection" value="Connect to Server"
                   onclick="Checkmarx.testConnection(Checkmarx.extractCredentials())"/>
            <span id="testConnectionMsg"></span>
        </form>
    </td>
    </tbody>


    <tr>
        <th><label for="${optionsBean.projectName}">Checkmarx Project Name<l:star/>
            <bs:helpIcon iconTitle="The project name will be used within the CxSAST Server.</br>
                                    In order to use an existing project, make sure the name is identical to the one in the server and exists under the same team"/>
        </label></th>
        <td>
            <props:textProperty name="${optionsBean.projectName}" className="longField"/>
            <span class="error" id="error_${optionsBean.projectName}"></span>
        </td>
    </tr>
    <tr>
        <th><label for="${optionsBean.presetId}">Preset<l:star/>
            <bs:helpIcon iconTitle="Scan Preset"/></label></th>
        <td>
            <props:selectProperty name="${optionsBean.presetId}" className="longField">
                <c:forEach items="${optionsBean.presetList}" var="item">
                    <props:option value="${item.id}">${item.name}</props:option>
                </c:forEach>
            </props:selectProperty>
            <span class="error" id="error_${optionsBean.presetId}"></span>
        </td>
    </tr>
    <tr>
        <th><label for="${optionsBean.teamId}">Team<l:star/>
            <bs:helpIcon iconTitle="Fully qualified team name for the project"/></label></th>
        <td>
            <props:selectProperty name="${optionsBean.teamId}" className="longField">
                <c:forEach items="${optionsBean.teamList}" var="item">
                    <props:option value="${item.id}">${item.fullName}</props:option>
                </c:forEach>
            </props:selectProperty>
            <span class="error" id="error_${optionsBean.teamId}"></span>
        </td>`
    </tr>

</l:settingsGroup>

<l:settingsGroup className="cx-title" title="Checkmarx Scan CxSAST">
    <tr>
        <th><label for="${optionsBean.sastEnabled}">Enable CxSAST Scan</label>
        </th>
        <td>
            <c:set var="onclick">
                jQuery('#sastContainer').toggle();
                BS.VisibilityHandlers.updateVisibility('sastContainer');
            </c:set>
            <props:checkboxProperty name="${optionsBean.sastEnabled}" onclick="${onclick}"/>
        </td>
    </tr>
    <tr id="sastContainer" ${hideCxSast}>
        <td colspan="2">
            <table width="101%">
                <tr>
                    <th><label for="${optionsBean.useDefaultSastConfig}">Use Default Settings
                        <bs:helpIcon iconTitle="Use default settings. if unchecked can edit the fields below"/>
                    </label></th>
                    <td>
                        <c:set var="onclick">
                            jQuery('#sastConfigSection').toggle();
                            jQuery('#defaultSastConfigSection').toggle();
                            BS.MultilineProperties.updateVisible();
                        </c:set>
                        <props:checkboxProperty name="${optionsBean.useDefaultSastConfig}" onclick="${onclick}"/>
                    </td>
                </tr>


                <tbody id="sastConfigSection" ${hideSastConfigSection}>

                <tr>
                    <th><label for="${optionsBean.excludeFolders}">Folder Exclusion
                        <bs:helpIcon iconTitle="Comma separated list of folders to exclude from scan.</br>
                                                                                                Entries in this list are automatically converted to exclude wildcard patterns and appended to the full pattern list provided in the advanced section"/>
                    </label></th>
                    <td><props:textProperty name="${optionsBean.excludeFolders}" className="longField"/></td>
                </tr>
                <tr>
                    <th><label for="${optionsBean.filterPatterns}">Include/Exclude Wildcard Patterns
                        <bs:helpIcon
                                iconTitle="Comma separated list of include or exclude wildcard patterns. Exclude patterns start with exclamation mark \"!\". Example: **/*.java, **/*.html, !**/test/**/XYZ*"/>
                    </label></th>
                    <td><props:multilineProperty name="${optionsBean.filterPatterns}" linkTitle="" expanded="true" rows="5"
                                                 cols="50" className="longField"/></td>
                </tr>
                <tr>
                    <th><label for="${optionsBean.scanTimeoutInMinutes}">Scan Timeout in Minutes
                        <bs:helpIcon iconTitle="Abort the scan if exceeds specified timeout in minutes"/></label></th>
                    <td>
                        <props:textProperty name="${optionsBean.scanTimeoutInMinutes}" className="longField"/>
                        <span class="error" id="error_${optionsBean.scanTimeoutInMinutes}"></span>
                    </td>
                </tr>

                </tbody>

                <tbody id="defaultSastConfigSection" ${hideDefaultSastConfigSection}>

                <tr>
                    <th>Folder Exclusion
                        <bs:helpIcon iconTitle="Comma separated list of folders to exclude from scan.</br>
                            Entries in this list are automatically converted to exclude wildcard patterns and appended to the full pattern list provided in the advanced section"/>
                    </th>
                    <td><input type="text" class="longField" disabled
                               value="${propertiesBean.properties[optionsBean.globalExcludeFolders]}"></td>
                </tr>

                <tr>
                    <th>Include/Exclude Wildcard Patterns
                        <bs:helpIcon
                                iconTitle="Comma separated list of include or exclude wildcard patterns. Exclude patterns start with exclamation mark \"!\". Example: **/*.java, **/*.html, !**/test/**/XYZ*"/></th>
                    <td>
                        <textarea id="globalFilterPatterns123" wrap="off" type="text" rows="5" cols="50" class="multilineProperty" disabled>${propertiesBean.properties[optionsBean.globalFilterPatterns]}</textarea>
                    </td>
                </tr>

                <tr>
                    <th>Scan Timeout in Minutes
                        <bs:helpIcon iconTitle="Abort the scan if exceeds specified timeout in minutes"/></th>
                    <td><input type="text" class="longField" disabled
                               value="${propertiesBean.properties[optionsBean.globalScanTimeoutInMinutes]}"></td>
                </tr>

                </tbody>

                <tr>
                    <th><label for="${optionsBean.scanComment}">Comment
                        <bs:helpIcon
                                iconTitle="Comment that can be added to the scan result. May reference build parameters like %teamcity.variable.name%"/></label>
                    </th>
                    <td><props:multilineProperty name="${optionsBean.scanComment}" linkTitle="" expanded="true" rows="5"
                                                 cols="50" className="longField"/></td>
                </tr>

                <tr>
                    <th><label for="${optionsBean.isIncremental}">Enable Incremental Scan
                        <bs:helpIcon iconTitle="Run incremental scan instead of full scan"/></label></th>
                    <td>


                    <props:checkboxProperty name="${optionsBean.isIncremental}" onclick="updatePeriodicScanSectionVisibility()" />
                    </td>
                </tr>
                <tbody id="incrementalSection" ${hideIncrementalSection}>
                <tr>
                    <th><label for="${optionsBean.isPeriodicFullScan}">Schedule periodic full scans
                    </label></th>
                    <td>

                    	<props:checkboxProperty name="${optionsBean.isPeriodicFullScan}" onclick="updatePeriodicScanSectionVisibility()" />
                    </td>
                </tr>
                </tbody>
                <tbody id="periodicScanSection" ${hidePeriodicScanSection}>
                <tr>
                    <th><label for="${optionsBean.periodicFullScanAfter}">Schedule periodic full scans (1-99)
                        <bs:helpIcon iconTitle="Number of incremental scans between full scans."/></label></th>
                    <td>
                    	 <props:textProperty name="${optionsBean.periodicFullScanAfter}" className="longField"/>
                    	 <span class="error" id="error_${optionsBean.periodicFullScanAfter}"></span>
                    </td>
                </tr>
                </tbody>
                <tr>
                    <th><label for="${optionsBean.generatePDFReport}">Generate CxSAST PDF Report
                        <bs:helpIcon
                                iconTitle="Downloadable PDF report with scan results from the Checkmarx server. The report is available via \"Artifacts\" tab"/></label>
                    </th>
                    <td><props:checkboxProperty name="${optionsBean.generatePDFReport}"/></td>
                </tr>
                <tr>
                    <th><label for="${optionsBean.engineConfigId}">Source character encoding (Configuration)
                        <bs:helpIcon iconTitle="Source character encoding for the project"/></label>
                    </th>
                    <td>
                        <props:selectProperty name="${optionsBean.engineConfigId}" className="longField">
                            <c:forEach items="${optionsBean.engineConfigList}" var="item">
                                <props:option value="${item.id}" >${item.name}</props:option>
                            </c:forEach>
                        </props:selectProperty>
                        <span class="error" id="error_${optionsBean.engineConfigId}"></span>
                    </td>
                </tr>
            </table>

        </td>
    </tr>
</l:settingsGroup>

<c:if test="${propertiesBean.properties[optionsBean.dependencyScanEnabled] != 'true'}">
    <c:set var="hideEditGlobalDependencyScanSection" value="${optionsBean.noDisplay}"/>
</c:if>
<c:if test="${propertiesBean.properties[optionsBean.overrideGlobalConfigurations] != 'true'}">
    <c:set var="hideGlobalDependencyScanSection" value="${optionsBean.noDisplay}"/>
</c:if>

<l:settingsGroup className="cx-title" title="Checkmarx Dependency Scan">
    <tr>
        <th><label for="${optionsBean.dependencyScanEnabled}">Enable Dependency Scan
            <bs:helpIcon
                    iconTitle="Enable dependency scan to choose between CxOSA and CxSCA"/></label>
        </th>
        <td>


        <props:checkboxProperty name="${optionsBean.dependencyScanEnabled}" onclick="updateDependencyScanSectionVisibility()" /></td>
    </tr>

    <tr id="overrideGlobalDSSettings">
        <th>
            <label for="${optionsBean.overrideGlobalConfigurations}">Override global dependency scan settings
            <bs:helpIcon iconTitle="Override the Global Dependency Scan Configurations"/>
            </label>
        </th>
        <td>
            <props:checkboxProperty name="${optionsBean.overrideGlobalConfigurations}" onclick="updateDependencyScanSectionVisibility()"/>
        </td>
    </tr>

    <tr class="dependencyScanRow">
        <th><label for="${optionsBean.osaFilterPatterns}">Include/Exclude Wildcard Patterns

            <bs:helpIcon
                    iconTitle="Include/Exclude definition will not affect dependencies resolved from package manager manifest files.</br> Comma separated list of include or exclude wildcard patterns. Exclude patterns start with exclamation mark \"!\". Example: **/*.jar, **/*.dll, !**/test/**/XYZ*"/>
        </label></th>
        <td><props:multilineProperty name="${optionsBean.osaFilterPatterns}" linkTitle="" expanded="true" rows="5"
                                     cols="50" className="longField"/></td>
    </tr>

    <tr class="dependencyScanRow">
        <th><label for="OsaEnabled">Use CxOSA dependency Scanner
            <bs:helpIcon iconTitle="Select CxOSA to perform dependency scan using CxOSA"/>
        </label></th>
        <td>
            <props:radioButtonProperty name="${optionsBean.dependencyScannerType}" onclick="updateDependencyScanSectionVisibility()" value="OSA" id="OsaEnabled"/>
        </td>
    </tr>
    <tr class="dependencyScanRow osaInput">
        <th><label for="${optionsBean.osaArchiveIncludePatterns}">Archive Extract Patterns
            <bs:helpIcon
                    iconTitle="Comma separated list of archive wildcard patterns to include their extracted content for the scan. eg. *.zip, *.jar, *.ear.
                                        Supported archive types are: jar, war, ear, sca, gem, whl, egg, tar, tar.gz, tgz, zip, rar
                                        Leave empty to extract all archives"/>
        </label></th>
        <td><props:textProperty name="${optionsBean.osaArchiveIncludePatterns}" className="longField"/></td>
    </tr>

      <tr class="dependencyScanRow osaInput">
        <th><label for="${optionsBean.osaInstallBeforeScan}">Execute dependency managers "install packages" command before Scan

            <bs:helpIcon
                    iconTitle="Select this option in order to be able to scan packages from various dependency managers (NPM, Bower, Nugget, Python and more.) as part of the CxOSA scan"/>
        </label></th>
        <td><props:checkboxProperty name="${optionsBean.osaInstallBeforeScan}"/></td>
    </tr>

    <tr class="dependencyScanRow">
        <th><label for="enableSca">Use CxSCA dependency Scanner
            <bs:helpIcon iconTitle="Select SCA to perform dependency scan using CxSCA"/>
        </label></th>

        <td><props:radioButtonProperty name="${optionsBean.dependencyScannerType}" id="enableSca" value="SCA"
                                       onclick="updateDependencyScanSectionVisibility()"/></td>

    </tr>
    <tr class="dependencyScanRow scaInput">
        <th><label for="${optionsBean.scaApiUrl}">CxSCA server URL
            <bs:helpIcon iconTitle="fill this with the SCA server URL"/>
        </label></th>
        <td><props:textProperty name="${optionsBean.scaApiUrl}" className="longField"/></td>
    </tr>
    <tr class="dependencyScanRow scaInput">
        <th><label for="${optionsBean.scaAccessControlUrl}">CxSCA Access control server URL
            <bs:helpIcon iconTitle="fill this with the SCA Access Control URL"/>
        </label></th>
        <td><props:textProperty name="${optionsBean.scaAccessControlUrl}" className="longField"/></td>
    </tr>
    <tr class="dependencyScanRow scaInput">
        <th><label for="${optionsBean.scaWebAppUrl}">CxSCA web app URL
            <bs:helpIcon iconTitle="fill this with the SCA web app URL"/>
        </label></th>
        <td><props:textProperty name="${optionsBean.scaWebAppUrl}" className="longField"/></td>
    </tr>
    <tr class="dependencyScanRow scaInput">
        <th><label for="${optionsBean.scaUserName}">CxSCA username
            <bs:helpIcon iconTitle="fill this with the SCA username"/>
        </label></th>
        <td><props:textProperty name="${optionsBean.scaUserName}" className="longField"/></td>
    </tr>
    <tr class="dependencyScanRow scaInput">
        <th><label for="${optionsBean.scaPass}">CxSCA password
            <bs:helpIcon iconTitle="fill this with the SCA password"/>
        </label></th>
        <td><props:passwordProperty name="${optionsBean.scaPass}" className="longField"/></td>
    </tr>
    <tr class="dependencyScanRow scaInput">
        <th><label for="${optionsBean.scaTenant}">CxSCA Account
            <bs:helpIcon iconTitle="fill this with the SCA Account"/>
        </label></th>
        <td><props:textProperty name="${optionsBean.scaTenant}" className="longField"/></td>
    </tr>

    <tr class="dependencyScanRow scaInput">
         <td>
             <form>

                <input id="testConnectionSCA" type="button" name="TestConnectionSCA" value="Test Connection"
                       onclick="Checkmarx.testSCAConnection(Checkmarx.extractSCAparameters())"/>
                <span id="testSCAConnectionMsg"></span>
            </form>
        </td>
    </tr>
    
    <tr class="dependencyScanRow scaInput">
        <th><label for="ScaResolverEnabled">Perform SCA scan using dependency resolution by SCA Resolver tool.
            <bs:helpIcon iconTitle="Perform SCA scan using dependency resolution by SCA Resolver tool."/>
        </label></th>
        <td>
            <props:radioButtonProperty name="${optionsBean.dependencyScaScanType}" onclick="updateDependencyScanSectionVisibility()" value="SCAResolver" id="ScaResolverEnabled"/>
        </td>
    </tr>
     <tr class="dependencyScanRow scaInput enableSCAResolver">
        <th><label for="${optionsBean.scaAccessControlUrl}">Path to SCA Resolver
            <bs:helpIcon iconTitle="fill this with the SCA Resolver Path"/>
        </label></th>
        <td><props:textProperty name="${optionsBean.scaAccessControlUrl}" className="longField"/></td>
    </tr>
    
     <tr class="dependencyScanRow scaInput enableSCAResolver">
        <th><label for="${optionsBean.scaAccessControlUrl}">SCA Resolver Additional Parameters
            <bs:helpIcon iconTitle="fill this with the SCA Resolver Additional Parameters"/>
        </label></th>
        <td><props:textProperty name="${optionsBean.scaAccessControlUrl}" className="longField"/></td>
    </tr>
    
    <tr class="dependencyScanRow scaInput">
        <th><label for="ManifestFileEnabled">Perform SCA scan by uploading manifests file(s)/source to SCA Service.
            <bs:helpIcon iconTitle="Perform SCA scan by uploading manifests file(s)/source to SCA Service."/>
        </label></th>
        <td>
            <props:radioButtonProperty name="${optionsBean.dependencyScaScanType}" onclick="updateDependencyScanSectionVisibility()" value="ManifestFile" id="ManifestFileEnabled"/>
        </td>
    </tr>
    
    
    <!-- SCA FEATURES -->
    <tr class="dependencyScanRow scaInput enableManifestFile">
        <th><label for="${optionsBean.scaConfigFile}">Package Manager's Config File(s) Path

            <bs:helpIcon
                    iconTitle="This parameter is to provide configuration files of the package managers used in the project. For ex. Settings.xml for maven, Nuget.config for Nuget, .npmrc for npm etc.</br>
This option is relevant for projects that use private artifactory. Use CxSCA agent to perfom the scan. CxSCA agent will try to perform dependency resolution using the package manager's configuration files provided.</br>
Multiple comma character separated file path can be provided.  </br>
<p>
Example: c:\user\.m2\settings.xml, c:\user\npm\.npmrc"/>
        </label></th>
        <td><props:multilineProperty name="${optionsBean.scaConfigFile}" linkTitle="" expanded="true" rows="5"
                                     cols="50" className="longField"/></td>
    </tr>
    
    <tr class="dependencyScanRow scaInput enableManifestFile">
        <th><label for="${optionsBean.scaEnvVariable}">Private Registry Environment Variable

            <bs:helpIcon
                    iconTitle="This option is relevant only if Package Manager's config files are provided.
In many cases, package manager's configuration files reference environment variables, often to provide credentials without storing them in a file. Pass all such variables using this option.
<p>
Example: param1:value1,param2:value2"/>
        </label></th>
        <td><props:multilineProperty name="${optionsBean.scaEnvVariable}" linkTitle="" expanded="true" rows="5"
                                     cols="50" className="longField"/></td>
    </tr>
    
     <tr class="dependencyScanRow scaInput enableManifestFile">
        <th><label for="${optionsBean.isIncludeSources}">Include Sources
            <bs:helpIcon iconTitle="When this flag is enabled, it will include entire source code in the zip file to be scanned."/>
        </label></th>
        <td> <props:checkboxProperty name="${optionsBean.isIncludeSources}"/>
        </td>
    </tr>
    
    <tr class="dependencyScanRow scaInput enableManifestFile">
        <th><label for="${optionsBean.isExploitablePath}">Enable Exploitable Path
            <bs:helpIcon iconTitle="Exploitable Path feature will attempt to co-relate CxSCA scan with the available CxSAST scan results. 
In this section, provide details like CxSAST server url, credentials.
At the job level, two more parameters need to be configured. These project full path name and/or project id from CxSAST. 
<p>
Example of Project Full Path: CxServer/team1/projectname."/>
        </label></th>
        <td> 
       
        <props:checkboxProperty name="${optionsBean.isExploitablePath}" id="enableExpPath" onclick="updateDependencyScanSectionVisibility()"/>
        </td>
    </tr>
    <tr class="dependencyScanRow scaInput expPath">
        <th>
            <label for="${optionsBean.useSASTDefaultServer}">Use Global Credentials<br>
            Server URL: ${propertiesBean.properties[optionsBean.globalSastServerUrl]}, <br>
            Username: ${propertiesBean.properties[optionsBean.globalSastUsername]}</label>
        </th>
        <td>
            <props:checkboxProperty name="${optionsBean.useSASTDefaultServer}" onclick="updateDependencyScanSectionVisibility()"/>
        </td>
    </tr>
    <tr class="dependencyScanRow scaInput expPath sastDetailsRow">
        <th><label for="${optionsBean.scaSASTServerUrl}">Server URL<l:star/></label></th>
        <td>
            <props:textProperty name="${optionsBean.scaSASTServerUrl}" className="longField"/>
            <span class="error" id="error_${optionsBean.scaSASTServerUrl}"></span>
        </td>
    </tr>
    <tr class="dependencyScanRow scaInput expPath sastDetailsRow">
        <th><label for="${optionsBean.scaSASTUserName}">Username<l:star/></label></th>
        <td>
            <props:textProperty name="${optionsBean.scaSASTUserName}" className="longField"/>
            <span class="error" id="error_${optionsBean.scaSASTUserName}"></span>
        </td>
    </tr>
    <tr class="dependencyScanRow scaInput expPath sastDetailsRow">
        <th><label for="${optionsBean.scaSASTPassword}">Password<l:star/></label></th>
        <td>
            <props:passwordProperty name="${optionsBean.scaSASTPassword}" className="longField"/>
            <span class="error" id="error_${optionsBean.scaSASTPassword}"></span>
        </td>
    </tr>
    <tr class="dependencyScanRow scaInput expPath sastDetailsRow">
    <td>
        <form>
            <input id="testScaSASTConnection" type="button" name="TestSASTConnection" value="Connect to Server"
                   onclick="Checkmarx.testScaSASTConnection(Checkmarx.extractSASTCredentials())"/>
            <span id="testScaSASTConnectionMsg"></span>
        </form>
    </td>
    </tr>
    <tr class="dependencyScanRow scaInput expPath">
        <th><label for="${optionsBean.scaSASTProjectFullPath}">Project Full Path<l:star/></label></th>
        <td>
            <props:textProperty name="${optionsBean.scaSASTProjectFullPath}" className="longField"/>
            <span class="error" id="error_${optionsBean.scaSASTProjectFullPath}"></span>
        </td>
    </tr>
    
    <tr  class="dependencyScanRow scaInput expPath">
        <th><label for="${optionsBean.scaSASTProjectID}">Project ID<l:star/></label></th>
        <td>
            <props:textProperty name="${optionsBean.scaSASTProjectID}" className="longField"/>
            <span class="error" id="error_${optionsBean.scaSASTProjectID}"></span>
        </td>
    </tr>
   
    <!-- END SCA FEATURES -->
    
    </tbody>
    </tbody>

</l:settingsGroup>

<l:settingsGroup className="cx-title" title="Control Checkmarx Scan">

    <tr>
        <th><label for="${optionsBean.useDefaultScanControl}">Use Default Settings
            <bs:helpIcon iconTitle="Use default settings. If unchecked can edit the fields below"/>
        </label></th>
        <td>
            <c:set var="onclick">
                jQuery('#specificScanControlSection').toggle();
                jQuery('#defaultScanControlSection').toggle();
                BS.VisibilityHandlers.updateVisibility('scanControlSection')
            </c:set>
            <props:checkboxProperty name="${optionsBean.useDefaultScanControl}" onclick="${onclick}"/>
        </td>
    </tr>

    <tbody id="specificScanControlSection" ${hideSpecificScanControlSection}>

    <tr>
        <th><label for="${optionsBean.isSynchronous}">Enable Synchronous Mode
            <bs:helpIcon iconTitle="In synchronous mode, Checkmarx build step will wait for Checkmarx scan to complete, then retrieve scan results and optionally check vulnerability thresholds.</br>
            When disabled, the build step finishes after scan job submissions to Checkmarx server."/>
        </label></th>
        <td>
            <c:set var="onclick">
                jQuery('#scanControlSection').toggle();
                BS.VisibilityHandlers.updateVisibility('scanControlSection')
            </c:set>
            <props:checkboxProperty name="${optionsBean.isSynchronous}" onclick="${onclick}"/>
        </td>
    </tr>

    <tr id="scanControlSection" ${hideScanControlSection}>
        <td colspan="2">

            <table class="scanControlSectionTable" width="101%">
                <tr>
                    <th><label for="${optionsBean.projectPolicyViolation}">Enable Project's policy enforcement
                        <bs:helpIcon iconTitle="Mark the build as failed or unstable if the projects policy is violated.</br> Note: Assigning a policy to a project is done from within CxSAST."/></label>
                    </th>
                    <td>
                        <props:checkboxProperty name="${optionsBean.projectPolicyViolation}"/>

                    </td>
                </tr>
                <tr>
                    <th><label for="${optionsBean.thresholdEnabled}">Enable CxSAST Vulnerability Thresholds
                        <bs:helpIcon iconTitle="Severity vulnerability threshold. If the number of vulnerabilities exceeds the threshold, build will break.</br>
                        Leave blank for no thresholds."/></label></th>
                    <td>
                        <c:set var="onclick">
                            $('thresholdSection').toggle();
                            BS.VisibilityHandlers.updateVisibility('scanControlSection')
                        </c:set>
                        <props:checkboxProperty name="${optionsBean.thresholdEnabled}" onclick="${onclick}"/>
                    </td>
                </tr>

                <tbody id="thresholdSection" ${hideThresholdSection}>
                <tr>
                    <th><label for="${optionsBean.highThreshold}">High</label></th>
                    <td>
                        <props:textProperty name="${optionsBean.highThreshold}" className="longField"/>
                        <span class="error" id="error_${optionsBean.highThreshold}"></span>
                    </td>
                </tr>
                <tr>
                    <th><label for="${optionsBean.mediumThreshold}">Medium</label>
                    </th>
                    <td>
                        <props:textProperty name="${optionsBean.mediumThreshold}" className="longField"/>
                        <span class="error" id="error_${optionsBean.mediumThreshold}"></span>
                    </td>
                </tr>
                <tr>
                    <th><label for="${optionsBean.lowThreshold}">Low</label></th>
                    <td>
                        <props:textProperty name="${optionsBean.lowThreshold}" className="longField"/>
                        <span class="error" id="error_${optionsBean.lowThreshold}"></span>
                    </td>
                </tr>
                </tbody>

                <tbody id="osaThresholdSection">
                <tr>
                    <th><label for="${optionsBean.osaThresholdEnabled}">Enable Dependency Scan Vulnerability Thresholds
                        <bs:helpIcon iconTitle="Severity vulnerability threshold. If the number of vulnerabilities exceeds the threshold, build will break.</br>
                        Leave blank for no thresholds."/></label>
                    </th>
                    <td>
                        <c:set var="onclick">
                            jQuery('.osaThresholdRow').toggle();
                            BS.VisibilityHandlers.updateVisibility('scanControlSection')
                        </c:set>
                        <props:checkboxProperty name="${optionsBean.osaThresholdEnabled}" onclick="${onclick}"/>
                    </td>
                </tr>
                <tr class="osaThresholdRow" ${hideOsaThresholdSection}>
                    <th><label for="${optionsBean.osaHighThreshold}">High</label></th>
                    <td>
                        <props:textProperty name="${optionsBean.osaHighThreshold}" className="longField"/>
                        <span class="error" id="error_${optionsBean.osaHighThreshold}"></span>
                    </td>
                </tr>
                <tr class="osaThresholdRow" ${hideOsaThresholdSection}>
                    <th><label for="${optionsBean.osaMediumThreshold}">Medium</label></th>
                    <td>
                        <props:textProperty name="${optionsBean.osaMediumThreshold}" className="longField"/>
                        <span class="error" id="error_${optionsBean.osaMediumThreshold}"></span>
                    </td>
                </tr>
                <tr class="osaThresholdRow" ${hideOsaThresholdSection}>
                    <th><label for="${optionsBean.osaLowThreshold}">Low</label></th>
                    <td>
                        <props:textProperty name="${optionsBean.osaLowThreshold}" className="longField"/>
                        <span class="error" id="error_${optionsBean.osaLowThreshold}"></span>
                    </td>
                </tr>
                </tbody>

            </table>

        </td>
    </tr>

    </tbody>

    <tbody id="defaultScanControlSection" ${hideDefaultScanControlSection}>

    <tr>
        <th>Enable Synchronous Mode
            <bs:helpIcon iconTitle="In synchronous mode, Checkmarx build step will wait for Checkmarx scan to complete, then retrieve scan results and optionally check vulnerability thresholds.</br>
            When disabled, the build step finishes after scan job submissions to Checkmarx server."/></th>
        <td>
            <input type="checkbox" disabled ${globalIsSynchronus ? 'checked' : ''}/>
        </td>
    </tr>

    <tr ${globalIsSynchronus ? '' : optionsBean.noDisplay}>
        <td colspan="2">
            <table class="scanControlSectionTable" width="101%">
                <tr>
                    <th>Enable Project's policy enforcement
                        <bs:helpIcon iconTitle="Mark the build as failed or unstable if the projects policy is violated.</br> Note: Assigning a policy to a project is done from within CxSAST."/></th>
                    <td>
                        <input type="checkbox" disabled ${globalProjectPolicydEnabled ? 'checked' : ''}/>
                    </td>
                </tr>
                <tr>
                    <th>Enable CxSAST Vulnerability Thresholds
                        <bs:helpIcon iconTitle="Severity vulnerability threshold. If the number of vulnerabilities exceeds the threshold, build will break.</br>
                        Leave blank for no thresholds."/></th>
                    <td>
                        <input type="checkbox" disabled ${globalThresholdEnabled ? 'checked' : ''}/>
                    </td>
                </tr>

                <tbody  ${globalThresholdEnabled ? '' : optionsBean.noDisplay}>
                <tr>
                    <th>High</th>
                    <td>
                        <input type="text" class="longField" disabled
                               value="${propertiesBean.properties[optionsBean.globalHighThreshold]}">
                    </td>
                </tr>
                <tr>
                    <th>Medium</th>
                    <td>
                        <input type="text" class="longField" disabled
                               value="${propertiesBean.properties[optionsBean.globalMediumThreshold]}">
                    </td>
                </tr>
                <tr>
                    <th>Low</th>
                    <td>
                        <input type="text" class="longField" disabled
                               value="${propertiesBean.properties[optionsBean.globalLowThreshold]}">
                    </td>
                </tr>
                </tbody>

                <tbody id="globalOsaThresholdSection">
                <tr>
                    <th>Enable Dependency Scan Vulnerability Thresholds
                        <bs:helpIcon iconTitle="Severity vulnerability threshold. If the number of vulnerabilities exceeds the threshold, build will break.</br>
                        Leave blank for no thresholds."/></th>
                    <td>
                        <input type="checkbox" disabled ${globalOsaThresholdEnabled ? 'checked' : ''}/>
                    </td>
                </tr>
                <tr ${globalOsaThresholdEnabled ? '' : optionsBean.noDisplay}>
                    <th>High</th>
                    <td>
                        <input type="text" class="longField" disabled
                               value="${propertiesBean.properties[optionsBean.globalOsaHighThreshold]}">
                    </td>
                </tr>
                <tr ${globalOsaThresholdEnabled ? '' : optionsBean.noDisplay}>
                    <th>Medium</th>
                    <td>
                        <input type="text" class="longField" disabled
                               value="${propertiesBean.properties[optionsBean.globalOsaMediumThreshold]}">
                    </td>
                </tr>

                <tr ${globalOsaThresholdEnabled ? '' : optionsBean.noDisplay}>
                    <th>Low</th>
                    <td>
                        <input type="text" class="longField" disabled
                               value="${propertiesBean.properties[optionsBean.globalOsaLowThreshold]}">
                    </td>
                </tr>
                </tbody>

            </table>
        </td>
    </tr>
    </tbody>

</l:settingsGroup>
