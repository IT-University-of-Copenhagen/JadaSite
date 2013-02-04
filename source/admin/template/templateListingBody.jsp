<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="templateListingForm" scope="session" value="${templateListingForm}"/>

<script language="JavaScript">
function submitRemoveForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/template/templateListing.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function submitListingForm(page) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/template/templateListing.do';
    document.forms[0].process.value = "list";
    document.forms[0].submit();
    return false;
}

function submitForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/template/templateMaint.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}
</script>
<html:form action="/admin/template/templateListing.do" method="post">
<html:hidden property="process" value="list"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - Template Listing</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="0%" valign="top"> 
      <div class="jc_search_panel_open"> 
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td> 
              <table class="jc_panel_table_header" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td>Search</td>
                  <td> 
                    <div align="right"> 
                      <html:submit property="submitButton" value="Search" styleClass="jc_submit_button" onclick="return submitListingForm('');"/>
                      <html:submit property="submitButton" value="New" styleClass="jc_submit_button" onclick="return submitForm('start');"/>
                    </div>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Template Name</td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srTemplateName" mode="E,E,E" styleClass="jc_input_object"/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Description</td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srTemplateDesc" mode="E,E,E" styleClass="jc_input_object"/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
        </table>
      </div>
    </td>
    <td width="100%" valign="top" class="jc_list_panel_open"> 
      <c:if test="${templateListingForm.templates != null}">
        <table border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td width="100%">Template Listing 
              Result</td>
            <td nowrap> 
              <div align="right">
                <html:submit property="submitButton" value="Remove" styleClass="jc_submit_button" onclick="return submitRemoveForm('remove');"/>
                <html:submit property="submitButton" value="New" styleClass="jc_submit_button" onclick="return submitForm('start');"/>
              </div>
            </td>
          </tr>
		</table>
		<br>
        <logic:messagesPresent property="msg" message="true"> 
        <html:messages property="msg" id="errorText" message="true">
          <span class="jc_input_error">
            <bean:write name="errorText"/>
            <br><br>
          </span>
        </html:messages>
        </logic:messagesPresent> 
        <table width="100%" border="0" cellspacing="0" cellpadding="1">
           <tr> 
            <td class="jc_list_table_header"></td>
            <td class="jc_list_table_header"></td>
            <td class="jc_list_table_header">Template</td>
            <td class="jc_list_table_header">Name</td>
            <td class="jc_list_table_header">Description</td>
          </tr>
          <c:forEach var="template" items="${templateListingForm.templates}">
          <tr class="jc_list_table_row"> 
            <td width="5%" class="jc_list_table_content">
              <div align="center">
                <c:choose>
                  <c:when test="${template.templateName == 'basic'}">
                    <input type="checkbox" name="templateNames" value="<c:out value="${template.templateName}"/>" disabled>
                  </c:when>
                  <c:otherwise>
                    <input type="checkbox" name="templateNames" value="<c:out value="${template.templateName}"/>">
                  </c:otherwise>
                </c:choose>
              </div>
            </td>
            <td nowrap>
              &nbsp;
              <html:link href="${template.previewURL}" target="_blank">
                preview 
              </html:link>
              &nbsp;
            </td>
            <td width="20%" class="jc_list_table_content" nowrap>
              <html:link page="/admin/template/templateMaint.do?process=edit" paramId="templateName" paramName="template" paramProperty="templateName">
              <c:choose>
                <c:when test="${template.servletResource}">
                  <html:img border="0" width="120" src="/${adminBean.contextPath}/content/template/${template.templateName}/preview.jpg"/>
                </c:when>
                <c:otherwise>
                  <img src="/${adminBean.contextPath}/services/ImageProvider.do?type=T&siteId=${adminBean.siteId}&imageId=${template.templateName}&maxsize=120" border="0">
                </c:otherwise>
              </c:choose>
              </html:link>
            </td>
            <td width="20%" class="jc_list_table_content">
              <html:link page="/admin/template/templateMaint.do?process=edit" paramId="templateName" paramName="template" paramProperty="templateName">
                <c:out value="${template.templateName}"/>
              </html:link>
            </td>
            <td width="55%" class="jc_list_table_content">
              	<c:out value="${template.templateDesc}"/>
            </td>
          </tr>
          </c:forEach>
        </table>
      </c:if>
    </td>
  </tr>
</table>
</html:form>
