<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="exportForm" scope="request" value="${exportForm}"/>

<script language="JavaScript">
function submitForm() {
    document.exportForm.action = '/${adminBean.contextPath}/admin/ie/export.do';
    if (document.exportForm.exportType.value == 'H') {
        document.exportForm.process.value = 'generate';
    }
    else {
    	document.exportForm.process.value = 'validateInput';
    }
    document.exportForm.submit();
    return false;
}

function changeExportLocation() {
	var location = document.exportForm.exportLocation.value;
	if (location == 'L') {
		document.getElementById('hostFileNameLabel').style.display = 'none';
		document.getElementById('hostFileNameValue').style.display = 'none';
	}
	else {
		document.getElementById('hostFileNameLabel').style.display = 'block';
		document.getElementById('hostFileNameValue').style.display = 'block';
	}
}

YAHOO.util.Event.onContentReady('hostFileNameValue', function() {
	changeExportLocation();
} );

<c:if test="${exportForm.generate}">
YAHOO.util.Event.onDOMReady(function() {
	document.exportForm.action = '/${adminBean.contextPath}/admin/ie/export.do';
//	document.exportForm.target = 'jada_export';
	document.exportForm.process.value = "generate";
	document.exportForm.submit();
});
</c:if>
</script>

<html:form action="/admin/ie/export.do" method="post">
<html:hidden property="process" value="validateInput"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Export</td>
  </tr>
</table>
<br>     
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" valign="top" class="jc_list_panel_open"> 
        <table class="jc_panel_table_header" width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td>Export</td>
            <td> 
            </td>
            <td align="right"> 
            <table width="0%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><html:submit property="submitButton" value="Generate" styleClass="jc_submit_button" onclick="return submitForm();"/>&nbsp;</td>
              </tr>
            </table>
            </td>
          </tr>
		</table>
		<br>
		<span class="jc_input_error"> 
		  <logic:messagesPresent property="fileError" message="true"> 
		  <html:messages property="fileError" id="errorText" message="true"> 
		    <bean:write name="errorText"/>
		  </html:messages>
		  </logic:messagesPresent> 
		</span>
        <table width="100%" border="0" cellspacing="0" cellpadding="5">
          <tr class="jc_list_table_row"> 
            <td width="300" class="jc_input_label">
              Export to
            </td>
          </tr>
          <tr class="jc_list_table_row"> 
            <td width="300" class="jc_input_control">
              <html:select property="exportLocation" onchange="changeExportLocation()"> 
                <html:option value="L">Local</html:option> 
                <html:option value="S">Server</html:option> 
              </html:select> 
            </td>
          </tr>
          <tr class="jc_list_table_row"> 
            <td width="300" class="jc_input_label">
              Export type
            </td>
          </tr>
          <tr class="jc_list_table_row"> 
            <td width="300" class="jc_input_control">
              <html:select property="exportType"> 
                <html:option value="C">CSV</html:option> 
                <html:option value="X">XML</html:option> 
              </html:select> 
            </td>
          </tr>
          <tr class="jc_list_table_row"> 
            <td width="300" class="jc_input_label">
              Profile
            </td>
          </tr>
          <tr class="jc_list_table_row"> 
            <td width="300" class="jc_input_control">
		      <html:select property="ieProfileHeaderId"> 
		        <html:optionsCollection property="ieProfileHeaderList" value="value" label="label"/> 
		      </html:select>
            </td>
          </tr>
          <tr class="jc_list_table_row" id="hostFileNameLabel" style="display: none"> 
            <td width="300" class="jc_input_label">
              Host file name
            </td>
          </tr>
          <tr class="jc_input_control" id="hostFileNameValue" style="display: none"> 
            <td width="300" class="jc_list_table_content">
			<html:text size="100" property="hostFileName"/>
            </td>
          </tr>
        </table>
    </td>
  </tr>
</table>
</html:form>
