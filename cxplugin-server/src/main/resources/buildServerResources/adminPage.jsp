<%@ page import="com.checkmarx.teamcity.common.CxParam" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@include file="/include.jsp" %>

<style>
  .scanControlSectionTable {
    margin-left: -10px;
  }

  .cxTitle {
    text-align: center;
    font-weight: bold;
    font-size: medium;
  }

</style>

<script type="text/javascript">
    function sanitizeJS(str) {
        var temp = document.createElement('div');
        temp.textContent = str;
        return temp.innerHTML;
    }

 function updateGlobalDependencyScanSectionVisibility() {
        var depScanEnabled = jQuery('#globalDependencyScanEnabled').prop('checked'),
            cxGlobalOsaEnabled = jQuery('#cxGlobalOsaEnabled').prop('checked'),
            cxGlobalScaEnabled = jQuery('#cxGlobalScaEnabled').prop('checked'),
            isOverriding = depScanEnabled;

        jQuery('.globalDependencyScanRow')[isOverriding ? 'show' : 'hide']();

        jQuery('.osaInput')[isOverriding && cxGlobalOsaEnabled ? 'show' : 'hide']();
        jQuery('.scaInput')[isOverriding && cxGlobalScaEnabled ? 'show' : 'hide']();
    }

    jQuery(updateGlobalDependencyScanSectionVisibility);


  var SettingsForm = OO.extend(BS.AbstractPasswordForm, {
    formElement: function () {
      return $("globalSettingsForm")
    },
    save: function () {

      BS.PasswordFormSaver.save(this, this.formElement().action, OO.extend(BS.ErrorsAwareListener, {
        onInvalid_cxGlobalServerUrlError: function (elem) {
          $("invalid_cxGlobalServerUrl").innerHTML = sanitizeJS(elem.firstChild.nodeValue);
          SettingsForm.highlightErrorField($("cxGlobalServerUrl"));
        },
        onInvalid_cxGlobalUsernameError: function (elem) {
          $("invalid_cxGlobalUsername").innerHTML = sanitizeJS(elem.firstChild.nodeValue);
          SettingsForm.highlightErrorField($("cxGlobalUsername"));
        },
        onInvalid_cxGlobalPasswordError: function (elem) {
          $("invalid_cxGlobalPassword").innerHTML = sanitizeJS(elem.firstChild.nodeValue);
          SettingsForm.highlightErrorField($("cxGlobalPassword"));
        },

        onInvalid_cxGlobalScanTimeoutInMinutesError: function (elem) {
          $("invalid_cxGlobalScanTimeoutInMinutes").innerHTML = sanitizeJS(elem.firstChild.nodeValue);
          SettingsForm.highlightErrorField($("cxGlobalScanTimeoutInMinutes"));
        },

        onInvalid_cxGlobalHighThresholdError: function (elem) {
          $("invalid_cxGlobalHighThreshold").innerHTML = sanitizeJS(elem.firstChild.nodeValue);
          SettingsForm.highlightErrorField($("cxGlobalHighThreshold"));
        },

        onInvalid_cxGlobalMediumThresholdError: function (elem) {
          $("invalid_cxGlobalMediumThreshold").innerHTML = sanitizeJS(elem.firstChild.nodeValue);
          SettingsForm.highlightErrorField($("cxGlobalMediumThreshold"));
        },

        onInvalid_cxGlobalLowThresholdError: function (elem) {
          $("invalid_cxGlobalLowThreshold").innerHTML = sanitizeJS(elem.firstChild.nodeValue);
          SettingsForm.highlightErrorField($("cxGlobalLowThreshold"));
        },

        onInvalid_cxGlobalOsaHighThresholdError: function (elem) {
          $("invalid_cxGlobalOsaHighThreshold").innerHTML = sanitizeJS(elem.firstChild.nodeValue);
          SettingsForm.highlightErrorField($("cxGlobalOsaHighThreshold"));
        },

        onInvalid_cxGlobalOsaMediumThresholdError: function (elem) {
          $("invalid_cxGlobalOsaMediumThreshold").innerHTML = sanitizeJS(elem.firstChild.nodeValue);
          SettingsForm.highlightErrorField($("cxGlobalOsaMediumThreshold"));
        },

        onInvalid_cxGlobalOsaLowThresholdError: function (elem) {
          $("invalid_cxGlobalOsaLowThreshold").innerHTML = sanitizeJS(elem.firstChild.nodeValue);
          SettingsForm.highlightErrorField($("cxGlobalOsaLowThreshold"));
        },

        onInvalid_cxGlobalSCAServerUrlError: function (elem) {
          $("invalid_cxGlobalSCAServerUrl").innerHTML = sanitizeJS(elem.firstChild.nodeValue);
          SettingsForm.highlightErrorField($("cxGlobalSCAServerUrl"));
        },
        onInvalid_cxGlobalSCAWebAppURLError: function (elem) {
          $("invalid_cxGlobalSCAWebAppURL").innerHTML = sanitizeJS(elem.firstChild.nodeValue);
          SettingsForm.highlightErrorField($("cxGlobalSCAWebAppURL"));
        },
        onInvalid_cxGlobalSCAAccessControlServerURLError: function (elem) {
          $("invalid_cxGlobalSCAAccessControlServerURL").innerHTML = sanitizeJS(elem.firstChild.nodeValue);
          SettingsForm.highlightErrorField($("cxGlobalSCAAccessControlServerURL"));
        },
        onInvalid_cxGlobalSCAUserNameError: function (elem) {
          $("invalid_cxGlobalSCAUserName").innerHTML = sanitizeJS(elem.firstChild.nodeValue);
          SettingsForm.highlightErrorField($("cxGlobalSCAUserName"));
        },
        onInvalid_cxGlobalSCAPasswordError: function (elem) {
          $("invalid_cxGlobalSCAPassword").innerHTML = sanitizeJS(elem.firstChild.nodeValue);
          SettingsForm.highlightErrorField($("cxGlobalSCAPassword"));
        },
         onInvalid_cxGlobalSCATenantError: function (elem) {
                  $("invalid_cxGlobalSCATenant").innerHTML = sanitizeJS(elem.firstChild.nodeValue);
                  SettingsForm.highlightErrorField($("cxGlobalSCATenant"));
        },
        
        onInvalid_cxGlobalSastServerUrlError: function (elem) {
            $("invalid_cxGlobalSastServerUrl").innerHTML = sanitizeJS(elem.firstChild.nodeValue);
            SettingsForm.highlightErrorField($("cxGlobalSastServerUrl"));
          },
          onInvalid_cxGlobalSastUsernameError: function (elem) {
            $("invalid_cxGlobalSastUsername").innerHTML = sanitizeJS(elem.firstChild.nodeValue);
            SettingsForm.highlightErrorField($("cxGlobalSastUsername"));
          },
          onInvalid_cxGlobalSastPasswordError: function (elem) {
            $("invalid_cxGlobalSastPassword").innerHTML = sanitizeJS(elem.firstChild.nodeValue);
            SettingsForm.highlightErrorField($("cxGlobalSastPassword"));
          },

          
        onSuccessfulSave: function () {
          SettingsForm.enable();
        },
        onCompleteSave: function (form, responseXml, wereErrors) {
          BS.ErrorsAwareListener.onCompleteSave(form, responseXml, wereErrors);
          if (!wereErrors) {
            $('generalSettings').refresh();
            window.scrollTo(0, 0);
          }
        }
      }));
      return false;
    }
  });
</script>
<script type="text/javascript" src="<c:url value='${teamcityPluginResourcesPath}testConnection.js'/>"></script>


<c:if test="${cxGlobalIsSynchronous != 'true'}">
  <c:set var="hideScanControlSection" value="style='display:none'"/>
</c:if>
<c:if test="${cxGlobalThresholdEnabled != 'true'}">
  <c:set var="hideThresholdSection" value="style='display:none'"/>
</c:if>
<c:if test="${cxGlobalIsExploitablePath != 'true'}">
  <c:set var="hideExpPathSection" value="style='display:none'"/>
</c:if>

<c:if test="${cxGlobalOsaThresholdEnabled != 'true'}">
  <c:set var="hideOsaThresholdSection" value="style='display:none'"/>
</c:if>
<c:if test="${cxGlobalSCAThresholdEnabled != 'true'}">
  <c:set var="hideSCAThresholdSection" value="style='display:none'"/>
</c:if>

<div>
  <bs:refreshable containerId="generalSettings" pageUrl="${pageUrl}">

    <bs:messages key="settingsSaved"/>

    <form id="globalSettingsForm" action="<c:url value='/admin/checkmarxSettings.html'/>" method="post"
          onsubmit="{return SettingsForm.save()}">

      <table class="runnerFormTable">
        <tr class="groupingTitle">
          <td colspan="2" class="cxTitle">Checkmarx Plugin Default Configuration</td>
        </tr>
        <tr class="groupingTitle">
          <td colspan="2">Checkmarx Server</td>
        </tr>

        <tr>
          <th><label for="cxGlobalServerUrl">Server URL<l:star/></label></th>
          <td>
            <forms:textField name="cxGlobalServerUrl" value="${cxGlobalServerUrl}" className="longField"/>
            <span class="error" id="invalid_cxGlobalServerUrl"></span>
          </td>
        </tr>

        <tr>
          <th><label for="cxGlobalUsername">Username<l:star/></label></th>
          <td>
            <forms:textField name="cxGlobalUsername" value="${cxGlobalUsername}" className="longField"/>
            <span class="error" id="invalid_cxGlobalUsername"></span>

          </td>

        </tr>

        <tr>
          <th><label for="cxGlobalPassword">Password<l:star/></label></th>
          <td>
            <input type="password" id="cxGlobalPassword" name="cxGlobalPassword" value="${cxGlobalPassword}" class="longField"/>
            <span class="error" id="invalid_cxGlobalPassword"></span>
          </td>
        </tr>

        <tr>
          <td>
            <form>
              <input id="testConnection" type="button" name="TestConnection" value="Connect to Server"
                     onclick="Checkmarx.testConnection(Checkmarx.extractGlobalCredentials())"/>
              <span id="testConnectionMsg"></span>
            </form>
          </td>

        </tr>

        <tr class="groupingTitle">
          <td colspan="2">Checkmarx Scan CxSAST</td>
        </tr>

        <tr>
          <th><label for="cxGlobalExcludeFolders">Folder Exclusion
            <bs:helpIcon iconTitle="Comma separated list of folders to exclude from scan.</br>
                                    Entries in this list are automatically converted to exclude wildcard patterns and appended to the full pattern list provided in the advanced section"/></label></th>
          <td><forms:textField name="cxGlobalExcludeFolders" value="${cxGlobalExcludeFolders}" className="longField"/></td>
        </tr>

        <tr>
          <th><label for="cxGlobalFilterPatterns">Include/Exclude Wildcard Patterns
            <bs:helpIcon iconTitle="Comma separated list of include or exclude wildcard patterns. Exclude patterns start with exclamation mark \"!\". Example: **/*.java, **/*.html, !**/test/**/XYZ*"/></label></th>
          <td><textarea rows="5" cols="50" name="cxGlobalFilterPatterns" wrap="off">${cxGlobalFilterPatterns}</textarea>
          </td>
        </tr>

        <tr>
          <th><label for="cxGlobalScanTimeoutInMinutes">Scan Timeout in Minutes
            <bs:helpIcon iconTitle="Abort the scan if exceeds specified timeout in minutes"/></label></th>
          <td>
            <forms:textField name="cxGlobalScanTimeoutInMinutes" value="${cxGlobalScanTimeoutInMinutes}" className="longField"/>
            <span class="error" id="invalid_cxGlobalScanTimeoutInMinutes"></span>
          </td>

        </tr>

        <tr class="groupingTitle">
          <td colspan="2">Control Checkmarx Scan</td>
        </tr>

        <tr>
          <th><label for="cxGlobalIsSynchronous">Enable Synchronous Mode
            <bs:helpIcon iconTitle="In synchronous mode, Checkmarx build step will wait for Checkmarx scan to complete, then retrieve scan results and optionally check vulnerability thresholds.</br>
            When disabled, the build step finishes after scan job submissions to Checkmarx server."/></label></th>
          <td><forms:checkbox name="cxGlobalIsSynchronous" checked="${cxGlobalIsSynchronous}"
                              onclick=" jQuery('#scanControlSection').toggle();"/></td>
        </tr>

        <tr id="scanControlSection" ${hideScanControlSection}>
          <td colspan="2">
            <table class="scanControlSectionTable" width="101%">

              <tr>
                <th><label for="cxGlobalProjectPolicyViolation">Enable Project's policy enforcement
                  <bs:helpIcon iconTitle="Mark the build as failed or unstable if the projects policy is violated.</br> Note: Assigning a policy to a project is done from within CxSAST."/></label>
                </th>
                <td><forms:checkbox name="cxGlobalProjectPolicyViolation" value="${cxGlobalProjectPolicyViolation}"
                                    checked="${cxGlobalProjectPolicyViolation}"/></td>
              </tr>

              <tr>
                <th><label for="cxGlobalThresholdEnabled">Enable CxSAST Vulnerability Thresholds
                  <bs:helpIcon iconTitle="Severity vulnerability threshold. If the number of vulnerabilities exceeds the threshold, build will break.</br>
                        Leave blank for no thresholds."/></label>
                </th>
                <td><forms:checkbox name="cxGlobalThresholdEnabled" value="${cxGlobalThresholdEnabled}"
                                    checked="${cxGlobalThresholdEnabled}"
                                    onclick="$('thresholdSection').toggle()"/></td>
              </tr>

              <tbody id="thresholdSection" ${hideThresholdSection}>

              <tr>
                <th><label for="cxGlobalHighThreshold">High</label></th>
                <td>
                  <forms:textField name="cxGlobalHighThreshold" value="${cxGlobalHighThreshold}" className="longField"/>
                  <span class="error" id="invalid_cxGlobalHighThreshold"></span>
                </td>

              </tr>

              <tr>
                <th><label for="cxGlobalMediumThreshold">Medium</label></th>
                <td>
                  <forms:textField name="cxGlobalMediumThreshold" value="${cxGlobalMediumThreshold}" className="longField"/>
                  <span class="error" id="invalid_cxGlobalMediumThreshold"></span>
                </td>

              </tr>

              <tr>
                <th><label for="cxGlobalLowThreshold">Low</label></th>
                <td>
                  <forms:textField name="cxGlobalLowThreshold" value="${cxGlobalLowThreshold}" className="longField"/>
                  <span class="error" id="invalid_cxGlobalLowThreshold"></span>
                </td>

              </tr>
              </tbody>


            </table>


                <l:settingsGroup className="cx-title" title="Checkmarx Dependency Scan">
                    <tr>
                        <th><label for="globalDependencyScanEnabled">Globally define dependency scan settings
                            <bs:helpIcon iconTitle="Enable dependency scan to choose between CxOSA and CxSCA"/>
                        </label>
                        </th>
                        <td>
                            <forms:checkbox name="globalDependencyScanEnabled"  checked="${globalDependencyScanEnabled}" value="${globalDependencyScanEnabled}" onclick="updateGlobalDependencyScanSectionVisibility()"/>
                        </td>
                    </tr>
                  <tr class="globalDependencyScanRow ">
                    <th><label for="CxGlobalDependencyScanFilterPatterns">Include/Exclude Wildcard Patterns
                      <bs:helpIcon iconTitle="Comma separated list of include or exclude wildcard patterns. Exclude patterns start with exclamation mark \"!\". Example: **/*.java, **/*.html, !**/test/**/XYZ*"/>
                    </label>
                    </th>
                    <td><textarea rows="5" cols="50" name="CxGlobalDependencyScanFilterPatterns" wrap="off">${CxGlobalDependencyScanFilterPatterns}</textarea>
                    </td>
                  </tr>
                  <tr class="globalDependencyScanRow">
                    <th><label for="cxGlobalOsaEnabled"> Enable CxOsa scan
                      <bs:helpIcon iconTitle="Select CxOSA to perform dependency scan using CxOSA"/>
                    <td>
                          <forms:radioButton id="cxGlobalOsaEnabled" checked="${cxGlobalDependencyScanType == 'OSA'}"
                                             name="cxGlobalDependencyScanType" value="OSA"
                                             onclick="updateGlobalDependencyScanSectionVisibility()" />
                    </td>
                  </tr>
                  <tbody id="globalOsaFilterPatterns" >
                  <tr class="globalDependencyScanRow osaInput">
                    <th><label for="cxGlobalOsaArchiveIncludePatterns">Archive extract patterns
                      <bs:helpIcon
                              iconTitle="Comma separated list of archive wildcard patterns to include their extracted content for the scan. eg. *.zip, *.jar, *.ear.
                                        Supported archive types are: jar, war, ear, sca, gem, whl, egg, tar, tar.gz, tgz, zip, rar
                                        Leave empty to extract all archives"/>
                    </label></th>
                    <td>
                      <forms:textField name="cxGlobalOsaArchiveIncludePatterns" value="${cxGlobalOsaArchiveIncludePatterns}" className="longField"/>
                    </td>
                  </tr>
                  <tr class="globalDependencyScanRow osaInput">
                    <th><label for="cxGlobalExecuteDependencyManager">Execute dependency managers 'install packages' command before Scan
                    </label>
                    </th>
                    <td>
                      <forms:checkbox name="cxGlobalExecuteDependencyManager" checked="${cxGlobalExecuteDependencyManager}" value="${cxGlobalExecuteDependencyManager}"/>
                    </td>
                  </tr>



</tbody>




              <tr class="globalDependencyScanRow">
                  <th><label for="cxGlobalScaEnabled"> Enable CxSca scan
                    <bs:helpIcon iconTitle="Select SCA to perform dependency scan using CxSCA"/>
                      <td>
                          <forms:radioButton id="cxGlobalScaEnabled" checked="${cxGlobalDependencyScanType == 'SCA'}"
                                             name="cxGlobalDependencyScanType" value="SCA"
                                             onclick="updateGlobalDependencyScanSectionVisibility()" />
                       </td>
                </tr>

                            <tbody id="SCAThresholdSection" >

                            <tr class="globalDependencyScanRow scaInput">
                              <th><label for="cxGlobalSCAServerUrl">CxSCA server URL
                                <bs:helpIcon iconTitle="fill this with the SCA server URL"/>
                                <l:star/></label></th>
                              <td>
                                <forms:textField name="cxGlobalSCAServerUrl" value="${cxGlobalSCAServerUrl}" className="longField"/>
                                <span class="error" id="invalid_cxGlobalSCAServerUrl"></span>
                              </td>
                            </tr>
                            <tr class="globalDependencyScanRow scaInput">
                              <th><label for="cxGlobalSCAAccessControlServerURL">Access control server URL
                                <bs:helpIcon iconTitle="fill this with the SCA Access Control URL"/>
                                <l:star/></label></th>
                              <td>
                                <forms:textField name="cxGlobalSCAAccessControlServerURL" value="${cxGlobalSCAAccessControlServerURL}" className="longField"/>
                                <span class="error" id="invalid_cxGlobalSCAAccessControlServerURL"></span>
                              </td>
                            </tr>
                            <tr class="globalDependencyScanRow scaInput">
                              <th><label for="cxGlobalSCAWebAppURL">CxSCA web app URL
                                <bs:helpIcon iconTitle="fill this with the SCA web app URL"/>
                                <l:star/></label></th>
                              <td>
                                <forms:textField name="cxGlobalSCAWebAppURL" value="${cxGlobalSCAWebAppURL}" className="longField"/>
                                <span class="error" id="invalid_cxGlobalSCAWebAppURL"></span>
                              </td>
                            </tr>

                             <tr class="globalDependencyScanRow scaInput">
                              <th><label for="cxGlobalSCAUserName">CxSCA User Name
                                <bs:helpIcon iconTitle="fill this with the SCA username"/>
                                <l:star/></label></th>
                              <td>
                                <forms:textField name="cxGlobalSCAUserName" value="${cxGlobalSCAUserName}" className="longField"/>
                                <span class="error" id="invalid_cxGlobalSCAUserName"></span>
                              </td>
                            </tr>
                                <tr class="globalDependencyScanRow scaInput">
                              <th><label for="cxGlobalSCAPassword">CxSCA Password
                                <bs:helpIcon iconTitle="fill this with the SCA password"/>
                                <l:star/></label></th>
                              <td>
                                <input type="password" id="cxGlobalSCAPassword" name="cxGlobalSCAPassword" value="${cxGlobalSCAPassword}" class="longField"/>
                                <span class="error" id="invalid_cxGlobalSCAPassword"></span>
                              </td>
                            </tr>

                             <tr class="globalDependencyScanRow scaInput">
                              <th><label for="cxGlobalSCATenant">CxSCA Account
                                <bs:helpIcon iconTitle="fill this with the SCA Account"/>
                                <l:star/></label></th>
                              <td>
                                <forms:textField name="cxGlobalSCATenant" value="${cxGlobalSCATenant}" className="longField"/>
                                <span class="error" id="invalid_cxGlobalSCATenant"></span>
                              </td>
                            </tr>
                          <tr class="globalDependencyScanRow scaInput">
                                    <td>
                                      <form>
                                        <input id="testConnectionSCA" type="button" name="TestConnectionSCA" value="Test Connection"
                                               onclick="Checkmarx.testSCAConnection(Checkmarx.extractGlobalSCAparameters())"/>
                                        <span id="testSCAConnectionMsg"></span>
                                      </form>
                                    </td>

                                  </tr>
                                   </tbody>
         <!-- SCA FEATURES  -->
     <tr class="globalDependencyScanRow scaInput">
             <th><label for="cxGlobalScaConfigFile">Package Manager's Config File(s) Path
                 <bs:helpIcon iconTitle="This parameter is to provide configuration files of the package managers used in the project. For ex. Settings.xml for maven, Nuget.config for Nuget, .npmrc for npm etc.
				This option is relevant for projects that use private artifactory. Use CxSCA agent to perfom the scan. CxSCA agent will try to perform dependency resolution using the package manager's configuration files provided.
			Multiple comma character separated file path can be provided.  
		
			Example: c:\user\.m2\settings.xml, c:\user\npm\.npmrc"/>
                 </label></th>
              <td>
                 <textarea rows="5" cols="50" name="cxGlobalScaConfigFile" wrap="off">${cxGlobalScaConfigFile}</textarea>
              </td>
      </tr>
    <tr class="globalDependencyScanRow scaInput">
             <th><label for="cxGlobalScaEnvVariable">Private Registry Environment Variable
                 <bs:helpIcon iconTitle="This option is relevant only if Package Manager's config files are provided.
	In many cases, package manager's configuration files reference environment variables, often to provide credentials without storing them in a file. Pass all such variables using this option.
	<p>
	Example: param1:value1,param2:value2"/>
                 </label></th>
              <td>
                 <textarea rows="5" cols="50" name="cxGlobalScaEnvVariable" wrap="off">${cxGlobalScaEnvVariable}</textarea>
              </td>
      </tr>
      
      <tr class="globalDependencyScanRow scaInput">
                <th><label for="cxGlobalIsExploitablePath">Enable Exploitable Path
                  <bs:helpIcon iconTitle="Exploitable Path feature will attempt to co-relate CxSCA scan with the available CxSAST scan results. In this section, provide details like CxSAST server url, credentials. At the job level, two more parameters need to be configured. These project full path name and/or project id from CxSAST.
Example of Project Full Path: CxServer/team1/projectname"/></label>
                </th>
                <td><forms:checkbox name="cxGlobalIsExploitablePath" value="${cxGlobalIsExploitablePath}"
                                    checked="${cxGlobalIsExploitablePath}"
                                    onclick="$('expPathSection').toggle()"/></td>
              </tr>

              <tbody id="expPathSection" ${hideExpPathSection}>

              <tr>
          <th><label for="cxGlobalSastServerUrl">Server URL<l:star/></label></th>
          <td>
            <forms:textField name="cxGlobalSastServerUrl" value="${cxGlobalSastServerUrl}" className="longField"/>
            <span class="error" id="invalid_cxGlobalSastServerUrl"></span>
          </td>
        </tr>

        <tr>
          <th><label for="cxGlobalSastUsername">Username<l:star/></label></th>
          <td>
            <forms:textField name="cxGlobalSastUsername" value="${cxGlobalSastUsername}" className="longField"/>
            <span class="error" id="invalid_cxGlobalSastUsername"></span>

          </td>

        </tr>

        <tr>
          <th><label for="cxGlobalSastPassword">Password<l:star/></label></th>
          <td>
            <input type="password" id="cxGlobalSastPassword" name="cxGlobalSastPassword" value="${cxGlobalSastPassword}" class="longField"/>
            <span class="error" id="invalid_cxGlobalSastPassword"></span>
          </td>
        </tr>

        <tr>
          <td>
            <form>
              <input id="testScaSastConnection" type="button" name="TestScaSastConnection" value="Connect to Server"
                     onclick="Checkmarx.testScaSASTConnection(Checkmarx.extractGlobalSASTCredentials())"/>
              <span id="testScaSASTConnectionMsg"></span>
            </form>
          </td>

        </tr>
              </tbody>
      <!-- END OF SCA FEATURES -->
                           


                  <tr class="globalDependencyScanRow">
                    <th><label for="cxGlobalOsaThresholdEnabled">Enable Dependency Scan Vulnerability Thresholds
                      <bs:helpIcon iconTitle="Severity vulnerability threshold. If the number of vulnerabilities exceeds the threshold, build will break.</br>
                        Leave blank for no thresholds."/></label></th>
                    <td><forms:checkbox name="cxGlobalOsaThresholdEnabled"
                                        value="${cxGlobalOsaThresholdEnabled}"
                                        checked="${cxGlobalOsaThresholdEnabled}"
                                        onclick="$('osaThresholdSection').toggle()"/></td>
                  </tr>

                  <tbody id="osaThresholdSection" ${hideOsaThresholdSection}>
                  <tr>
                    <th><label for="cxGlobalOsaHighThreshold">High</label></th>
                    <td>
                      <forms:textField name="cxGlobalOsaHighThreshold" value="${cxGlobalOsaHighThreshold}" className="longField"/>
                      <span class="error" id="invalid_cxGlobalOsaHighThreshold"></span>
                    </td>

                  </tr>

                  <tr>
                    <th><label for="cxGlobalOsaMediumThreshold">Medium</label></th>
                    <td>
                      <forms:textField name="cxGlobalOsaMediumThreshold" value="${cxGlobalOsaMediumThreshold}" className="longField"/>
                      <span class="error" id="invalid_cxGlobalOsaMediumThreshold"></span>
                    </td>

                  </tr>

                  <tr>
                    <th><label for="cxGlobalOsaLowThreshold">Low</label></th>
                    <td>
                      <forms:textField name="cxGlobalOsaLowThreshold" value="${cxGlobalOsaLowThreshold}" className="longField"/>
                      <span class="error" id="invalid_cxGlobalOsaLowThreshold"></span>
                    </td>
                  </tr>
                  </tbody>
                </l:settingsGroup>


          </td>
        </tr>
      </table>



      <div class="saveButtonsBlock">
        <input class="submitButton" type="submit" value="Save">
        <input type="hidden" id="publicKey" name="publicKey"
               value="<c:out value='${hexEncodedPublicKey}'/>"/>
        <forms:saving/>
      </div>
    </form>
  </bs:refreshable>
</div>