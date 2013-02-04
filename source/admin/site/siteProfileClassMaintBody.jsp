<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="couponMaintForm" scope="request" value="${couponMaintForm}"/>
<script language="JavaScript">

var siteProfileClassId = '${siteProfileClassMaintForm.siteProfileClassId}';
var mode = '${siteProfileClassMaintForm.mode}';
function submitForm(type) {
    document.siteProfileClassMaintForm.process.value = type;
    document.siteProfileClassMaintForm.submit();
    return false;
}
function submitCancelForm() {
    document.siteProfileClassMaintForm.action = "/${adminBean.contextPath}/admin/site/siteProfileClassListing.do";
    document.siteProfileClassMaintForm.process.value = "back";
    document.siteProfileClassMaintForm.submit();
    return false;
}

</script>
<div class=" yui-skin-sam">
<html:form method="post" action="/admin/site/siteProfileClassMaint"> 
<html:hidden property="process" value=""/> 
<html:hidden property="siteProfileClassId"/> 
<html:hidden property="mode"/> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - <a href="/<c:out value='${adminBean.contextPath}'/>/admin/site/siteProfileClassListing.do?process=back">Site 
      Profile Class Listing</a> - Site Profile Class Maintenance</td>
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
                <td class="jc_input_label">Site profile Class Name</td>
              </tr>
              <tr>
                <td class="jc_input_control">
            	  <html:text name="siteProfileClassMaintForm" property="siteProfileClassName" size="40" maxlength="40" styleClass="jc_input_object"/>
                  <logic:messagesPresent property="siteProfileClassName" message="true"> 
                    <br>
                    <html:messages property="siteProfileClassName" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                  </logic:messagesPresent>
                </td>
              </tr>
              <tr>
                <td class="jc_input_label">Site profile Class Native Name</td>
              </tr>
              <tr>
                <td class="jc_input_control">
            	  <html:text name="siteProfileClassMaintForm" property="siteProfileClassNativeName" size="40" maxlength="40" styleClass="jc_input_object"/>
                  <logic:messagesPresent property="siteProfileClassNativeName" message="true"> 
                    <br>
                    <html:messages property="siteProfileClassNativeName" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                  </logic:messagesPresent>
                </td>
              </tr>
              <tr>
                <td class="jc_input_label">Language</td>
              </tr>
			  <tr>
				<td class="jc_input_control">
	              <html:select property="langId"> 
	                <html:optionsCollection property="languages" value="value" label="label"/> 
	              </html:select>
                  <logic:messagesPresent property="langId" message="true"> 
                    <br>
                    <html:messages property="langId" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                  </logic:messagesPresent>
				</td>
			  </tr>
              <tr>
                <td class="jc_input_label">System</td>
              </tr>
			  <tr>
				<td class="jc_input_control">
	              <html:hidden property="systemRecord"/> 
	              ${siteProfileClassMaintForm.systemRecord}
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
