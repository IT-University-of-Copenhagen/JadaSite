<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="customerClassMaintForm" scope="request" value="${customerClassMaintForm}"/>
<script language="JavaScript">
function submitForm(type) {
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}
function submitBackForm(type) {
    document.forms[0].action = "/<c:out value='${adminBean.contextPath}'/>/admin/customerClass/customerClassListing.do";
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}
</script>
<html:form method="post" action="/admin/customerClass/customerClassMaint"> 
<html:hidden property="mode"/> 
<html:hidden property="process" value=""/> 
<html:hidden property="custClassId"/> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - <a href="/<c:out value='${adminBean.contextPath}'/>/admin/customerClass/customerClassListing.do?process=back">Customer Class 
      Listing</a> - Customer Class Maintenance</td>
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
                <c:if test="${customerClassMaintForm.systemRecord != 'Y'}">
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
          <td class="jc_input_label">Customer class</td>
        </tr>
        <tr>
          <td class="jc_input_control">
		  <layout:text layout="false" property="custClassName" size="40" mode="E,E,E" styleClass="tableContent"/>
		  <span class="jc_input_error">
          <logic:messagesPresent property="custClassName" message="true"> 
          <html:messages property="custClassName" id="errorText" message="true"> 
          <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
          </span> 
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">System</td>
        </tr>
        <tr>
          <td class="jc_input_control">
			${customerClassMaintForm.systemRecord}
            <html:hidden property="systemRecord"/>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table></html:form>
