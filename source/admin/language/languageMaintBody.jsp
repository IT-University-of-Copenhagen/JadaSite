<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="languageMaintForm" scope="request" value="${languageMaintForm}"/>

<script language="JavaScript">
function submitForm(type) {
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}
function submitBackForm(type) {
    document.forms[0].action = "/<c:out value='${adminBean.contextPath}'/>/admin/language/languageListing.do";
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}
function submitTranslateForm() {
	submitForm('translate');
}
YAHOO.util.Event.onAvailable('butTranslate', function() {
	var butTranslate = new YAHOO.widget.Button("butTranslate", { disabled: false });
	butTranslate.on("click", submitTranslateForm);
});
</script>

<html:form method="post" action="/admin/language/languageMaint">
<html:hidden property="mode"/> 
<html:hidden property="process" value=""/>
<html:hidden property="langId"/> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - <a href="/<c:out value='${adminBean.contextPath}'/>/admin/language/languageListing.do?process=back">Language 
      Listing</a> - Language Maintenance</td>
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
                <c:if test="${languageMaintForm.systemRecord != 'Y'}"> 
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
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td width="100%" valign="top">
            <table border="0" cellspacing="0" cellpadding="5px" class="borderTable">
              <tr> 
                <td class="jc_input_label">Language Name</td>
                <td class="jc_input_control">
                  <layout:text layout="false" property="langName" size="20" mode="E,E,E" styleClass="tableContent"/> 
                  <span class="jc_input_error">
                    <logic:messagesPresent property="langName" message="true"> 
                    <html:messages property="langName" id="errorText" message="true"> 
                      <bean:write name="errorText"/>
                    </html:messages>
                    </logic:messagesPresent> 
                  </span>
                  <span class="jc_input_error">
			      <logic:messagesPresent property="error" message="true"> 
			      	<html:messages property="error" id="errorText" message="true"> 
			        <bean:write name="errorText"/>
			        </html:messages> 
			      </logic:messagesPresent>
			      </span>
                </td>
              </tr>
              <tr> 
                <td class="jc_input_label">Locale</td>
                <td class="jc_input_control">
                  <html:select property="locale"> 
                    <html:optionsCollection property="locales" value="value" label="label"/> 
                  </html:select>
                </td>
              </tr>
              <tr> 
                <td class="jc_input_label">Translation Language</td>
                <td class="jc_input_control">
                  <html:select property="googleTranslateLocale"> 
                    <html:option value=""></html:option>
                    <html:optionsCollection property="languages" value="value" label="label"/> 
                  </html:select>
                  <c:if test="${languageMaintForm.mode == 'U'}">
                    <button type="button" id="butTranslate" name="butTranslate" value="Translate">Translate</button>
                  </c:if>
                  <br>
                  <span class="jc_input_error"> 
                    <logic:messagesPresent property="googleTranslateLocale" message="true"> 
                    <html:messages property="googleTranslateLocale" id="errorText" message="true"> 
                      <bean:write name="errorText"/>
                    </html:messages> 
                    </logic:messagesPresent> 
                  </span>
                </td>
              </tr>
              <tr> 
                <td class="jc_input_label">System record</td>
                <td class="jc_input_control">
                  <html:hidden property="systemRecord"/>
				  ${languageMaintForm.systemRecord}
                </td>
              </tr>
              <tr>
                <td> </td>
                <td> </td>
              </tr>
            </table>
			<br>
            <c:if test="${languageMaintForm.mode == 'U'}"> 
            <div class="hr">
              <hr/>
            </div>
            <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td valign="top">
                  <table border="0" cellspacing="0" cellpadding="0">
                    <tr> 
                      <td class="jc_panel_table_sub_header" width="100%">Translation</td>
                    </tr>
                  </table>
                  <br>
                  <table width="100%" border="0" cellspacing="0" cellpadding="3">
                    <tr>
                      <td valign="top" width="200" class="jc_input_label">Key</td>
                      <td valign="top" width="50%" class="jc_input_label">English</td>
                      <td valign="top" width="50%" class="jc_input_label">Translation</td>
                    </tr>
                    <c:forEach var="langTran" items="${languageMaintForm.langTrans}">
                    <tr>
                      <td valign="top" width="200" class="jc_input_control">
                        <html:hidden indexed="true" name="langTran" property="langTranKey" styleClass="tableContent"/>
                        ${langTran.langTranKey}
                      </td>
                      <td valign="top" width="50%" class="jc_input_control">
                        <html:hidden indexed="true" name="langTran" property="langTranEnglish" styleClass="tableContent"/>
                        ${langTran.langTranEnglish}
                      </td>
                      <td valign="top" width="50%" class="jc_input_control">
                        <html:text indexed="true" name="langTran" property="langTranValue" size="80" maxlength="255" styleClass="tableContent"/>
                        <c:if test="${langTran.langTranValueError != ''}">
                          <br>
                          <span class="jc_input_error">${langTran.langTranValueError}</span>
                        </c:if>
                      </td>
                    </tr>
                    </c:forEach>
                  </table>
                </td>
              </tr>
            </table>
            </c:if>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</html:form> 

