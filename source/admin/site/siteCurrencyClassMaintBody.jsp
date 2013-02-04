<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="couponMaintForm" scope="request" value="${couponMaintForm}"/>
<script language="JavaScript">

var siteCurrencyClassId = '${siteCurrencyClassMaintForm.siteCurrencyClassId}';
var mode = '${siteCurrencyClassMaintForm.mode}';
function submitForm(type) {
    document.siteCurrencyClassMaintForm.process.value = type;
    document.siteCurrencyClassMaintForm.submit();
    return false;
}
function submitCancelForm() {
    document.siteCurrencyClassMaintForm.action = "/${adminBean.contextPath}/admin/site/siteCurrencyClassListing.do";
    document.siteCurrencyClassMaintForm.process.value = "back";
    document.siteCurrencyClassMaintForm.submit();
    return false;
}

</script>
<div class=" yui-skin-sam">
<html:form method="post" action="/admin/site/siteCurrencyClassMaint"> 
<html:hidden property="process" value=""/> 
<html:hidden property="siteCurrencyClassId"/> 
<html:hidden property="mode"/> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - <a href="/<c:out value='${adminBean.contextPath}'/>/admin/site/siteCurrencyClassListing.do?process=back">Site 
      Currency Class Listing</a> - Site Currency Class Maintenance</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr> 
    <td width="100%" valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
        <tr> 
          <td>&nbsp;
          </td>
          <td align="right"> 
            <table width="0%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><html:submit property="submitButton" value="Save" styleClass="jc_submit_button" onclick="return submitForm('save');"/>&nbsp;</td>
                <td><html:submit property="submitButton" value="Cancel" styleClass="jc_submit_button" onclick="return submitCancelForm();"/>&nbsp;</td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr>
          <td valign="top">
		    <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td class="jc_input_label">Site currency class</td>
              </tr>
              <tr>
                <td class="jc_input_control">
            	  <html:text name="siteCurrencyClassMaintForm" property="siteCurrencyClassName" size="40" maxlength="40" styleClass="jc_input_object"/>
                  <logic:messagesPresent property="siteCurrencyClassName" message="true"> 
                    <br>
                    <html:messages property="siteCurrencyClassName" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                  </logic:messagesPresent>
                </td>
              </tr>
              <tr>
                <td class="jc_input_label">Locale</td>
              </tr>
			  <tr>
				<td class="jc_input_control">
                  <html:select name="siteCurrencyClassMaintForm" property="locale">
                    <html:optionsCollection property="locales" label="label"/> 
                  </html:select>
				</td>
			  </tr>
              <tr>
                <td class="jc_input_label">Currency</td>
              </tr>
			  <tr>
				<td class="jc_input_control">
                  <html:select name="siteCurrencyClassMaintForm" property="currencyId">
                    <html:optionsCollection property="currencies" label="label"/> 
                  </html:select>
				</td>
			  </tr>
              <tr>
                <td class="jc_input_label">System</td>
              </tr>
			  <tr>
				<td class="jc_input_control">
	              <html:hidden property="systemRecord"/> 
	              ${siteCurrencyClassMaintForm.systemRecord}
				</td>
			  </tr>
		    </table>
		  </td>
        </tr>
      </table>
    </td>
  </tr>
</table></html:form>
</div>
<%@ include file="/admin/include/panel.jsp" %>
<%@ include file="/admin/include/confirm.jsp" %>
