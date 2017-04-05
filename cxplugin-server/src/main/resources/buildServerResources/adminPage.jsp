<%@ include file="/include.jsp"%>

<c:if test="${save}">
<div class="successMessage">Checkmarx configuration updated.</div>
</c:if>

<form action="/admin/checkmarxSettings.html" method="post">
  <table class="runnerFormTable">
    <tr class="groupingTitle">
      <td colspan="2">Checkmarx Plugin Default Configuration</td>
    </tr>
    <tr>
      <th><label for="cxServerUrl">Server URL: <l:star/></label></th>
      <td><forms:textField name="cxServerUrl" value="${cxServerUrl}" style="width:300px;"/></td>
    </tr>
    <tr>
      <th><label for="cxUser">Username: <l:star/></label></th>
      <td><forms:textField name="cxUser" value="${cxUser}" style="width:300px;"/></td>
    </tr>
    <tr>
      <th><label for="cxPass">Password: <l:star/></label></th>
      <td>
          <input style="display:none" type="text" value=" "/>
          <input style="display:none" type="password" value=" "/>
          <input type="password" name="cxPass" value="${cxPass}" class="textField" style="margin:0;padding:0;width:300px;" onfocus="this.select()" onchange="" onkeyup="" autocomplete="false"/>
      </td>
    </tr>
    <tr>
      <th></th>
      <td>
        <forms:submit label="Save" name="save"/>
      </td>
    </tr>
  </table>
</form>
