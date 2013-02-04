<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="taxMaintForm" scope="request" value="${taxMaintForm}"/>

<script language="JavaScript">
var siteProfileClassDefault = false;
var translationEnable = false;
var siteProfileClassId = "${taxMaintForm.siteProfileClassId}";
var mode = "${itemMaintForm.mode}";
var imageCounter = 0;
<c:if test="${taxMaintForm.siteProfileClassDefault}">
siteProfileClassDefault = true;
</c:if>
<c:if test="${taxMaintForm.translationEnable}">
translationEnable = true;
</c:if>

function submitForm(type) {
    if (!siteProfileClassDefault) {
      document.taxMaintForm.taxCodeLang.value = document.taxMaintForm.taxCodeLang_tmp.value;
      document.taxMaintForm.taxNameLang.value = document.taxMaintForm.taxNameLang_tmp.value;
    }
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}
function submitBackForm(type) {
    document.forms[0].action = "/<c:out value='${adminBean.contextPath}'/>/admin/tax/taxListing.do";
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}
</script>
<html:form method="post" action="/admin/tax/taxMaint">
<html:hidden property="mode"/> 
<html:hidden property="process" value=""/>
<html:hidden property="taxId"/> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - <a href="/<c:out value='${adminBean.contextPath}'/>/admin/tax/taxListing.do?process=back">Tax 
      Listing</a> - Tax Maintenance</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr> 
    <td width="100%" valign="top"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
        <tr>
          <layout:mode value="edit">
          <td>
            Profile
            <html:select property="siteProfileClassId" onchange="javascript:submitForm('language')"> 
              <html:optionsCollection property="siteProfileClassBeans" value="value" label="label"/> 
            </html:select>
            <button type="button" id="butTranslate" name="butTranslate" value="Translate">Translate</button>
          </td>
          </layout:mode>
          <td align="right"> 
            <table width="0%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td><html:submit property="submitButton" value="Save" styleClass="jc_submit_button" onclick="return submitForm('save')"/>&nbsp;</td>
                <td><html:submit property="submitButton" value="Cancel" styleClass="jc_submit_button" onclick="return submitBackForm('back')"/>&nbsp;</td>
                <layout:mode value="edit"> 
                <td><html:submit property="submitButton" value="Remove" styleClass="jc_submit_button" onclick="return submitForm('remove')"/>&nbsp;</td>
                </layout:mode> 
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <br>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td width="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="5px" class="borderTable">
              <tr> 
                <td class="jc_input_label">Tax Code
                  <lang:checkboxSwitch name="taxMaintForm" property="taxCodeLangFlag"> - Override language</lang:checkboxSwitch>
                </td>
              </tr>
              <tr>
                <td class="jc_input_control"> <lang:text property="taxCode" size="10" styleClass="tableContent"/> 
                  <span class="jc_input_error"> <logic:messagesPresent property="taxCode" message="true"> 
                  <html:messages property="taxCode" id="errorText" message="true"> 
                  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
                  </span> </td>
              </tr>
              <tr> 
                <td class="jc_input_label">
                  Tax Name
                  <lang:checkboxSwitch name="taxMaintForm" property="taxNameLangFlag"> - Override language</lang:checkboxSwitch>
                </td>
              </tr>
              <tr>
                <td class="jc_input_control">
                  <lang:text property="taxName" size="40" styleClass="tableContent"/> 
                  <span class="jc_input_error"> <logic:messagesPresent property="taxName" message="true"> 
                  <html:messages property="taxName" id="errorText" message="true"> 
                  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
                  </span> </td>
              </tr>
              <tr> 
                <td class="jc_input_label">Tax Rate (Percentage)</td>
              </tr>
              <tr>
                <td class="jc_input_control"> <lang:text property="taxRate" size="10" styleClass="tableContent"/> 
                  <span class="jc_input_error"> <logic:messagesPresent property="taxRate" message="true"> 
                  <html:messages property="taxRate" id="errorText" message="true"> 
                  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
                  </span> </td>
              </tr>
              <tr> 
                <td class="jc_input_label">Published</td>
              </tr>
              <tr>
                <td class="jc_input_control"> <lang:checkbox property="published" value="Y"/> 
                </td>
              </tr>
              <tr> 
                <td> </td>
                <td> </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<c:if test="${taxMaintForm.taxId != null && taxMaintForm.taxId != ''}"> 
<script type="text/javascript">
function submitTranslateForm() {
	submitForm('translate');
}
var butTranslate = new YAHOO.widget.Button("butTranslate", { disabled: true });
if (translationEnable) {
	butTranslate.set('disabled', false);
}
butTranslate.on("click", submitTranslateForm);
</script>
</c:if> 
</html:form> 
<%@ include file="/admin/include/confirm.jsp" %>
