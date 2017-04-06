<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>
<jsp:useBean id="optionsBean" class="com.checkmarx.teamcity.server.CxOptions"/>

<jsp:setProperty name="optionsBean" property="serverUrlValue" value="${actualServerUrl}"/>
<jsp:setProperty name="optionsBean" property="userValue" value="${actualUser}"/>
<jsp:setProperty name="optionsBean" property="passValue" value="${actualPass}"/>

<c:if test="${propertiesBean.properties[optionsBean.defaultServer] == 'true'}">
    <c:set var="hideServerOverrideSection" value="${optionsBean.noDisplay}"/>
</c:if>
<c:if test="${propertiesBean.properties[optionsBean.incremental] != 'true'}">
    <c:set var="hideIncrementalSection" value="${optionsBean.noDisplay}"/>
</c:if>
<c:if test="${propertiesBean.properties[optionsBean.periodicFullScans] != 'true'}">
    <c:set var="hideNumberIncrementalSection" value="${optionsBean.noDisplay}"/>
</c:if>
<c:if test="${propertiesBean.properties[optionsBean.thresholdEnable] != 'true'}">
    <c:set var="hideThresholdSection" value="${optionsBean.noDisplay}"/>
</c:if>

<script>

    function refresh()
    {
        window.location.reload();

    }

</script>

<l:settingsGroup title="Checkmarx Server">
    <tr>
        <th><label for="${optionsBean.defaultServer}">Use default Checkmarx server credentials:</label></th>
        <td>
            <c:set var="onclick">
                $('serverOverrideSection').toggle();
                refresh()
            </c:set>
            <props:checkboxProperty name="${optionsBean.defaultServer}" onclick="${onclick}"/>
        </td>
    </tr>
    <tbody id="serverOverrideSection" ${hideServerOverrideSection}>
    <tr>
        <th><label for="${optionsBean.serverUrl}">Server URL: <l:star/></label></th>
        <td>
            <props:textProperty name="${optionsBean.serverUrl}" className="longField"/>
            <span class="error" id="error_${optionsBean.serverUrl}"></span>
        </td>
    </tr>
    <tr>
        <th><label for="${optionsBean.user}">Username: <l:star/></label></th>
        <td>
            <props:textProperty name="${optionsBean.user}" className="longField"/>
            <span class="error" id="error_${optionsBean.user}">
        </td>
    </tr>
    <tr>
        <th><label for="${optionsBean.pass}">Password: <l:star/></label></th>
        <td>
            <props:passwordProperty name="${optionsBean.pass}" className="longField"/>
            <span class="error" id="error_${optionsBean.pass}">
        </td>
    </tr>
      <td>
         <form>
             <c:set var="onclick1">
                ${optionsBean.testConnection()}
                 refresh()
             </c:set>
             <input type="button" name ="TestConnection" value="Test Connection" onclick="refresh()"/>
        <!--  <button onclick="{optionsBean.testConnection(optionsBean.pass)}">Test Connection!</button>-->
                   <span id="testConnectionMsg">"${optionsBean.testConnectionMsg}"</span>
         </form>
      </td>
    <!--<td>
            <input class="button" onclick="{optionsBean.testConnection()}" name="try" value="tryyy"/>
    </td>-->
  <!-- BAD, like the enter button, it immediatly save and leaves page-->

    </tbody>
</l:settingsGroup>

<l:settingsGroup title="Checkmarx Scan Options">
    <tr>
        <th><label for="${optionsBean.project}">Project name: <l:star/></label></th>
        <td>
            <props:textProperty name="${optionsBean.project}" className="longField"/>
            <span class="error" id="error_${optionsBean.project}">
        </td>
    </tr>
    <tr>
        <th><label for="${optionsBean.preset}">Preset: <l:star/></label></th>
        <td>
            <props:selectProperty name="${optionsBean.preset}" className="longField">
                <c:forEach items="${optionsBean.presetList}" var="item">
                    <props:option value="${item.id}">${item.name}</props:option>
                </c:forEach>
            </props:selectProperty>
            <span class="error" id="error_${optionsBean.preset}">
        </td>
    </tr>
    <tr>
        <th><label for="${optionsBean.configuration}">Configuration: <l:star/></label></th>
        <td>
            <props:selectProperty name="${optionsBean.configuration}" className="longField">
                <c:forEach items="${optionsBean.configurationList}" var="item">
                    <props:option value="${item.id}">${item.name}</props:option>
                </c:forEach>
            </props:selectProperty>
            <span class="error" id="error_${optionsBean.configuration}">
        </td>
    </tr>
    <tr>
        <th><label for="${optionsBean.team}">Team: <l:star/></label></th>
        <td>
            <props:selectProperty name="${optionsBean.team}" className="longField">
                <c:forEach items="${optionsBean.teamList}" var="item">
                    <props:option value="${item.id}">${item.name}</props:option>
                </c:forEach>
            </props:selectProperty>
            <span class="error" id="error_${optionsBean.team}">
        </td>
    </tr>
    <tr>
        <th><label for="${optionsBean.excludeFolders}">Exclude folders:</label></th>
        <td><props:textProperty name="${optionsBean.excludeFolders}" className="longField"/></td>
    </tr>
    <tr>
        <th><label for="${optionsBean.filterPatterns}">Include/Exclude wildcard patterns:</label></th>
        <td><props:multilineProperty name="${optionsBean.filterPatterns}" linkTitle="" expanded="true" rows="5" cols="50" className="longField"/></td>
    </tr>
    <tr>
        <th><label for="${optionsBean.incremental}">Incremental:</label></th>
        <td>
            <c:set var="onclick">
                $('incrementalSection').toggle();
            </c:set>
            <props:checkboxProperty name="${optionsBean.incremental}" onclick="${onclick}"/>
        </td>
    </tr>
    <tr id="incrementalSection" ${hideIncrementalSection}>
        <th><label for="${optionsBean.periodicFullScans}">Schedule periodic full scans:</label></th>
        <td>
            <c:set var="onclick">
                $('numberIncrementalSection').toggle();
            </c:set>
            <props:checkboxProperty name="${optionsBean.periodicFullScans}" onclick="${onclick}"/>
        </td>
    </tr>
    <tr id="numberIncrementalSection" ${hideNumberIncrementalSection}>
        <th><label for="${optionsBean.numberIncremental}">Number of incremental scans between full scans (1-99):</label></th>
        <td>
            <props:textProperty name="${optionsBean.numberIncremental}" className="longField"/>
            <span class="error" id="error_${optionsBean.numberIncremental}">
        </td>
    </tr>
    <tr>
        <th><label for="${optionsBean.comment}">Comment:</label></th>
        <td><props:multilineProperty name="${optionsBean.comment}" linkTitle="" expanded="true" rows="5" cols="50" className="longField"/></td>
    </tr>
    <tr>
        <th><label for="${optionsBean.thresholdEnable}">Enable vulnerability threshold:</label></th>
        <td>
            <c:set var="onclick">
                $('thresholdSection').toggle();
            </c:set>
            <props:checkboxProperty name="${optionsBean.thresholdEnable}" onclick="${onclick}"/>
        </td>
    </tr>
    <tbody id="thresholdSection" ${hideThresholdSection}>
    <tr>
        <th><label for="${optionsBean.thresholdHigh}">High severity vulnerabilities threshold:</label></th>
        <td>
            <props:textProperty name="${optionsBean.thresholdHigh}" className="longField"/>
            <span class="error" id="error_${optionsBean.thresholdHigh}">
        </td>
    </tr>
    <tr>
        <th><label for="${optionsBean.thresholdMedium}">Medium severity vulnerabilities threshold:</label></th>
        <td>
            <props:textProperty name="${optionsBean.thresholdMedium}" className="longField"/>
            <span class="error" id="error_${optionsBean.thresholdMedium}">
        </td>
    </tr>
    <tr>
        <th><label for="${optionsBean.thresholdLow}">Low severity vulnerabilities threshold:</label></th>
        <td>
            <props:textProperty name="${optionsBean.thresholdLow}" className="longField"/>
            <span class="error" id="error_${optionsBean.thresholdLow}">
        </td>
    </tr>
    </tbody>
    <tr>
        <th><label for="${optionsBean.generatePdf}">Generate PDF report:</label></th>
        <td><props:checkboxProperty name="${optionsBean.generatePdf}"/></td>
    </tr>
</l:settingsGroup>
