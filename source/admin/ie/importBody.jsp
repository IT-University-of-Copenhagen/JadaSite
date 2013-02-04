<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="importForm" scope="request" value="${importForm}"/>

<script language="JavaScript">
function submitForm() {
    document.importForm.action = '/${adminBean.contextPath}/admin/ie/import.do';
    document.importForm.submit();
    return false;
}

function changeImportType() {
	var type = document.importForm.importType.value;
	if (type == 'X') {
		document.getElementById('profileLabel').style.display = 'none';
		document.getElementById('profileValue').style.display = 'none';
	}
	else {
		document.getElementById('profileLabel').style.display = 'block';
		document.getElementById('profileValue').style.display = 'block';
	}
}

function changeImportLocation() {
	var location = document.importForm.importLocation.value;
	if (location == 'L') {
		document.getElementById('hostFileNameLabel').style.display = 'none';
		document.getElementById('hostFileNameValue').style.display = 'none';
		document.getElementById('localFileNameLabel').style.display = 'block';
		document.getElementById('localFileNameValue').style.display = 'block';
	}
	else {
		document.getElementById('hostFileNameLabel').style.display = 'block';
		document.getElementById('hostFileNameValue').style.display = 'block';
		document.getElementById('localFileNameLabel').style.display = 'none';
		document.getElementById('localFileNameValue').style.display = 'none';
	}
}

YAHOO.util.Event.onContentReady('localFileNameValue', function() {
	changeImportLocation();
} );
</script>

<html:form action="/admin/ie/import.do" method="post" enctype="multipart/form-data"> 
<html:hidden property="process" value="importFile"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Import</td>
  </tr>
</table>
<br>
<span class="jc_input_error">
${importForm.messageText}
</span>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" valign="top" class="jc_list_panel_open"> 
        <table class="jc_panel_table_header" width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td>Import</td>
            <td> 
            </td>
            <td align="right"> 
            <table width="0%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><html:submit property="submitButton" value="Import" styleClass="jc_submit_button" onclick="return submitForm();"/>&nbsp;</td>
              </tr>
            </table>
            </td>
          </tr>
		</table>
        <table width="100%" border="0" cellspacing="0" cellpadding="5">
          <tr class="jc_list_table_row"> 
            <td width="300" class="jc_input_label">
              Import from
            </td>
          </tr>
          <tr class="jc_list_table_row"> 
            <td width="300" class="jc_input_control">
              <html:select property="importLocation" onchange="changeImportLocation()"> 
                <html:option value="L">Local</html:option> 
                <html:option value="S">Server</html:option> 
              </html:select> 
            </td>
          </tr>
          <tr class="jc_list_table_row"> 
            <td width="300" class="jc_input_label">
              Import type
            </td>
          </tr>
          <tr class="jc_list_table_row"> 
            <td width="300" class="jc_input_control">
              <html:select property="importType" onchange="changeImportType()"> 
                <html:option value="C">CSV</html:option> 
                <html:option value="X">XML</html:option> 
              </html:select> 
            </td>
          </tr>
          <tr class="jc_list_table_row" id="profileLabel"> 
            <td width="300" class="jc_input_label">
              Profile
            </td>
          </tr>
          <tr class="jc_list_table_row" id="profileValue"> 
            <td width="300" class="jc_input_control">
		      <html:select property="ieProfileHeaderId"> 
		        <html:optionsCollection property="ieProfileHeaderList" value="value" label="label"/> 
		      </html:select>
            </td>
          </tr>
          <tr class="jc_list_table_row"> 
            <td width="300" class="jc_input_label">
              Skip first line in file
            </td>
          </tr>
          <tr class="jc_list_table_row"> 
            <td width="300" class="jc_input_control">
              <html:checkbox property="skipFirstLine" styleClass="jc_input_control"/>
            </td>
          </tr>
          <tr class="jc_list_table_row" id="hostFileNameLabel" style="display:none"> 
            <td width="300" class="jc_input_label">
              Host file name
            </td>
          </tr>
          <tr class="jc_input_control" id="hostFileNameValue" style="display:none"> 
            <td width="300" class="jc_list_table_content">
			<html:text size="100" property="hostFileName"/>
            </td>
          </tr>
          <tr class="jc_list_table_row" id="localFileNameLabel" style="display:none"> 
            <td width="300" class="jc_input_label">
              Local file name
            </td>
          </tr>
          <tr class="jc_input_control" id="localFileNameValue" style="display:none"> 
            <td width="300" class="jc_list_table_content">
			<html:file size="50" property="localFile"/>
            </td>
          </tr>
        </table>
    </td>
  </tr>
</table>
</html:form>
