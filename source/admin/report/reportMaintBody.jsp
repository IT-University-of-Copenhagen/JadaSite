<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="reportMaintForm" scope="request" value="${reportMaintForm}"/>
<script language="JavaScript">
function submitForm(type) {
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}
function submitBackForm(type) {
    document.forms[0].action = "/<c:out value='${adminBean.contextPath}'/>/admin/report/reportListing.do";
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}
</script>
<html:form method="post" action="/admin/report/reportMaint"> 
<html:hidden property="mode"/> 
<html:hidden property="process" value=""/> 
<html:hidden property="reportId"/> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - <a href="/<c:out value='${adminBean.contextPath}'/>/admin/report/reportListing.do?process=back">Report 
      Listing</a> - Report Maintenance</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr> 
    <td width="100%" valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td align="right" class="jc_panel_table_header"> 
            <table width="0%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><html:submit property="submitButton" value="Save" styleClass="jc_submit_button" onclick="return submitForm('save')"/>&nbsp;</td>
                <layout:mode value="edit">
                <c:if test="${reportMaintForm.systemRecord != 'Y'}">
                <td><html:submit property="submitButton" value="Remove" styleClass="jc_submit_button" onclick="return submitForm('remove')"/>&nbsp;</td>
                </c:if>
                </layout:mode> 
                <td><html:submit property="submitButton" value="Cancel" styleClass="jc_submit_button" onclick="return submitBackForm('back')"/>&nbsp;</td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <br>
      <span class="jc_input_error">
      <logic:messagesPresent property="error" message="true"> 
        <html:messages property="error" id="errorText" message="true"> 
        <bean:write name="errorText"/>
        </html:messages> 
      </logic:messagesPresent>
      </span>
      <table width="100%" border="0" cellspacing="0" cellpadding="5px" class="borderTable">
        <tr> 
          <td class="jc_input_label">Report name</td>
        </tr>
        <tr>
          <td class="jc_input_control">
		  <layout:text layout="false" property="reportName" size="40" mode="E,E,E" styleClass="tableContent"/>
		  <span class="jc_input_error">
          <logic:messagesPresent property="reportName" message="true"> 
          <html:messages property="reportName" id="errorText" message="true"> 
          <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
          </span> 
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">Report description</td>
        </tr>
        <tr>
          <td class="jc_input_control">
		  <layout:text layout="false" property="reportDesc" size="40" maxlength="255" mode="E,E,E" styleClass="tableContent"/>
		  <span class="jc_input_error">
          <logic:messagesPresent property="reportDesc" message="true"> 
          <html:messages property="reportDesc" id="errorText" message="true"> 
          <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
          </span> 
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">Report text</td>
        </tr>
        <tr>
          <td class="jc_input_control">
		  <layout:textarea layout="false" property="reportText" rows="30" cols="80" mode="E,E,E" styleClass="tableContent"/>
		  <span class="jc_input_error">
          <logic:messagesPresent property="reportText" message="true"> 
          <html:messages property="reportText" id="errorText" message="true"> 
          <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
          </span> 
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">System</td>
        </tr>
        <tr>
          <td class="jc_input_control">
			${reportMaintForm.systemRecord}
            <html:hidden property="systemRecord"/>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table></html:form>
