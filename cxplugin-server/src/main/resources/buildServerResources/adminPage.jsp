<%@include file="/include.jsp" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>


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
  var SettingsForm = OO.extend(BS.AbstractPasswordForm, {
    formElement: function () {
      return $("globalSettingsForm")
    },
    save: function () {

      BS.PasswordFormSaver.save(this, this.formElement().action, OO.extend(BS.ErrorsAwareListener, {
        onInvalid_cxGlobalServerUrlError: function (elem) {
          $("invalid_cxGlobalServerUrl").innerHTML = elem.firstChild.nodeValue;
          SettingsForm.highlightErrorField($("cxGlobalServerUrl"));
        },

        onInvalid_cxGlobalUsernameError: function (elem) {
          $("invalid_cxGlobalUsername").innerHTML = elem.firstChild.nodeValue;
          SettingsForm.highlightErrorField($("cxGlobalUsername"));
        },

        onInvalid_cxGlobalPasswordError: function (elem) {
          $("invalid_cxGlobalPassword").innerHTML = elem.firstChild.nodeValue;
          SettingsForm.highlightErrorField($("cxGlobalPassword"));
        },

        onInvalid_cxGlobalScanTimeoutInMinutesError: function (elem) {
          $("invalid_cxGlobalScanTimeoutInMinutes").innerHTML = elem.firstChild.nodeValue;
          SettingsForm.highlightErrorField($("cxGlobalScanTimeoutInMinutes"));
        },

        onInvalid_cxGlobalHighThresholdError: function (elem) {
          $("invalid_cxGlobalHighThreshold").innerHTML = elem.firstChild.nodeValue;
          SettingsForm.highlightErrorField($("cxGlobalHighThreshold"));
        },

        onInvalid_cxGlobalMediumThresholdError: function (elem) {
          $("invalid_cxGlobalMediumThreshold").innerHTML = elem.firstChild.nodeValue;
          SettingsForm.highlightErrorField($("cxGlobalMediumThreshold"));
        },

        onInvalid_cxGlobalLowThresholdError: function (elem) {
          $("invalid_cxGlobalLowThreshold").innerHTML = elem.firstChild.nodeValue;
          SettingsForm.highlightErrorField($("cxGlobalLowThreshold"));
        },

        onInvalid_cxGlobalOsaHighThresholdError: function (elem) {
          $("invalid_cxGlobalOsaHighThreshold").innerHTML = elem.firstChild.nodeValue;
          SettingsForm.highlightErrorField($("cxGlobalOsaHighThreshold"));
        },

        onInvalid_cxGlobalOsaMediumThresholdError: function (elem) {
          $("invalid_cxGlobalOsaMediumThreshold").innerHTML = elem.firstChild.nodeValue;
          SettingsForm.highlightErrorField($("cxGlobalOsaMediumThreshold"));
        },

        onInvalid_cxGlobalOsaLowThresholdError: function (elem) {
          $("invalid_cxGlobalOsaLowThreshold").innerHTML = elem.firstChild.nodeValue;
          SettingsForm.highlightErrorField($("cxGlobalOsaLowThreshold"));
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
<c:if test="${cxGlobalOsaThresholdEnabled != 'true'}">
  <c:set var="hideOsaThresholdSection" value="style='display:none'"/>
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
                <th><label for="cxGlobalProjectPolicyViolation">Enable Project's OSA Policy Enforcement
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

              <tr>
                <th><label for="cxGlobalOsaThresholdEnabled">Enable CxOSA Vulnerability Thresholds
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
            </table>
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