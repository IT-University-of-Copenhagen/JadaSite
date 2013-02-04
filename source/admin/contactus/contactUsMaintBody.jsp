<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="contactUsMaintForm" scope="request" value="${contactUsMaintForm}"/>
<script language="JavaScript">
var siteProfileClassDefault = false;
var translationEnable = false;
var siteProfileClassId = "<c:out value='${contactUsMaintForm.siteProfileClassId}'/>";
<c:if test="${contactUsMaintForm.siteProfileClassDefault}">
siteProfileClassDefault = true;
</c:if>
<c:if test="${contactUsMaintForm.translationEnable}">
translationEnable = true;
</c:if>

function submitForm(type) {
    if (!siteProfileClassDefault) {
    	document.contactUsMaintForm.contactUsNameLang.value = document.contactUsMaintForm.contactUsNameLang_tmp.value;
        jc_editor_contactUsDescLang.saveHTML();
    }
    else {
        jc_editor_contactUsDesc.saveHTML();
    }
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}
function submitCancelForm() {
    document.forms[0].action = "/<c:out value='${adminBean.contextPath}'/>/admin/contactus/contactUsListing.do";
    document.forms[0].process.value = "back";
    document.forms[0].submit();
    return false;
}
function submitTranslateForm() {
	submitForm('translate');
}
YAHOO.util.Event.onAvailable('butTranslate', function() {
	var butTranslate = new YAHOO.widget.Button("butTranslate", { disabled: true });
	if (translationEnable) {
		butTranslate.set('disabled', false);
	}
	butTranslate.on("click", submitTranslateForm);
});
</script>
<html:form method="post" action="/admin/contactus/contactUsMaint"> 
<html:hidden property="mode"/> 
<html:hidden property="process" value=""/> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - <a href="/<c:out value='${adminBean.contextPath}'/>/admin/contactus/contactUsListing.do?process=back">Contact Us 
      Listing</a> - Contact Us Maintenance</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr> 
    <td width="100%" valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
        <tr>
          <td>
            <layout:mode value="edit">
            Profile
            <html:select property="siteProfileClassId" onchange="javascript:submitForm('language')"> 
              <html:optionsCollection property="siteProfileClassBeans" value="value" label="label"/> 
            </html:select>
            <button type="button" id="butTranslate" name="butTranslate" value="Translate">Translate</button>
            </layout:mode>
          </td>
          <td align="right"> 
            <table width="0%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><html:submit property="submitButton" value="Save" styleClass="jc_submit_button" onclick="return submitForm('save')"/>&nbsp;</td>
                <layout:mode value="edit"> 
                <td><html:submit property="submitButton" value="Remove" styleClass="jc_submit_button" onclick="return submitForm('remove')"/>&nbsp;</td>
                </layout:mode> 
                <td><html:submit property="submitButton" value="Cancel" styleClass="jc_submit_button" onclick="return submitCancelForm()"/>&nbsp;</td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <html:hidden property="contactUsId"/>
      <table width="900" border="0" cellspacing="0" cellpadding="5">
        <tr>
          <td width="300">
		  <table width="100%" border="0" cellspacing="0" cellpadding="3">
			  <tr>
				<td class="jc_input_label">
				Contact Us Name
				<lang:checkboxSwitch name="contactUsMaintForm" property="contactUsNameLangFlag"> - Override language</lang:checkboxSwitch>
				</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
				  <lang:text property="contactUsName" size="100" styleClass="tableContent" style="width: 250px"/>
				  <span class="jc_input_error">
				  <logic:messagesPresent property="contactUsName" message="true"> 
				  <html:messages property="contactUsName" id="errorText" message="true"> 
				  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
				  </span> 
				</td>
			  </tr>
			  <tr>
				<td class="jc_input_label">Active</td>
			  </tr>
			  <tr>
				<td class="jc_input_control"><lang:checkbox property="active" value="Y"/></td>
			  </tr>
              <tr>
              <td>&nbsp;</td>
              </tr>
			  <tr>
				<td><hr></td>
			  </tr>
              <tr>
              <td>&nbsp;</td>
              </tr>
			  <tr>
				<td class="jc_input_label">Email</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
				  <lang:text property="contactUsEmail" size="50" styleClass="tableContent"/> 
				  <span class="jc_input_error">
				  <logic:messagesPresent property="contactUsEmail" message="true"> 
				  <html:messages property="contactUsEmail" id="errorText" message="true"> 
				  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
				  </span>
		        </td>
			  </tr>
              <tr>
                <td class="jc_input_label">Contact Us phone</td>
              </tr>
              <tr>
                <td class="jc_input_control">
                <lang:text property="contactUsPhone" size="20" styleClass="tableContent"/> 
                <span class="jc_input_error">
                <logic:messagesPresent property="contactUsPhone" message="true"> 
                <html:messages property="contactUsPhone" id="errorText" message="true"> 
                <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
                </span>
                </td>
              </tr>
			  <tr>
				<td class="jc_input_label">Address</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
				  <lang:text property="contactUsAddressLine1" size="30" styleClass="tableContent"/> 
				  <span class="jc_input_error">
				  <logic:messagesPresent property="contactUsAddressLIne1" message="true"> 
				  <html:messages property="contactUsAddressLIne1" id="errorText" message="true"> 
				  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
				  </span>
		        </td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
				  <lang:text property="contactUsAddressLine2" size="30" styleClass="tableContent"/> 
				  <span class="jc_input_error">
				  <logic:messagesPresent property="contactUsAddressLIne2" message="true"> 
				  <html:messages property="contactUsAddressLIne1" id="errorText" message="true"> 
				  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
				  </span>
				</td>
			  </tr>
			  <tr>
				<td class="jc_input_label">City</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
				  <lang:text property="contactUsCityName" size="25" styleClass="tableContent"/> 
				  <span class="jc_input_error">
				  <logic:messagesPresent property="contactUsCityName" message="true"> 
				  <html:messages property="contactUsCityName" id="errorText" message="true"> 
				  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
				  </span>
				</td>
			  </tr>
			  <tr>
				<td class="jc_input_label">State/Province</td>
			  </tr>
			  <tr>
			    <td class="jc_input_control">
				  <lang:select property="contactUsStateCode" styleId="tableContent" styleClass="width: 200px"> 
				  <lang:optionsCollection property="states" label="label"/> 
				  </lang:select> 
			    </td>
		      </tr>
			  <tr>
			    <td class="jc_input_label">Country</td>
		      </tr>
			  <tr>
			    <td class="jc_input_control">
				  <lang:select property="contactUsCountryCode" styleId="tableContent" styleClass="width: 200px"> 
				  <lang:optionsCollection property="countries" label="label"/> 
				  </lang:select> 
				</td>
		      </tr>
			  <tr class="jc_input_label">
			    <td>Zip/Postal code</td>
		      </tr>
			  <tr>
			    <td class="jc_input_control">
				  <lang:text property="contactUsZipCode" size="10" styleClass="tableContent"/> 
				  <span class="jc_input_error">
				  <logic:messagesPresent property="contactUsZipCode" message="true"> 
				  <html:messages property="contactUsZipCode" id="errorText" message="true"> 
				  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
				  </span>
				</td>
		      </tr>
			  <tr>
			    <td></td>
		      </tr>
		    </table>
		  </td>
          <td width="5px" height="100%">
            <div style="border-left: 1px solid #dcdcdc; height: 100%"></div>
          </td>
          <td valign="top" width="100%">
		  <table width="100%" border="0" cellspacing="0" cellpadding="3">
			  <tr>
				<td class="jc_input_label">
				Contact us Description
				<lang:checkboxEditorSwitch name="contactUsMaintForm" property="contactUsDescLangFlag"> - Override language</lang:checkboxEditorSwitch>
				</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
				  <lang:editorText name="contactUsMaintForm" property="contactUsDesc" height="300" width="100%" toolBarSet="Simple"/>
				</td>
			  </tr>
			</table>
		  </td>
        </tr>
      </table>
    </td>
  </tr>
</table></html:form>
<%@ include file="/admin/include/confirm.jsp" %>
<!------------------------------------------------------------------------>