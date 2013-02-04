<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="siteProfileClassListingForm" scope="session" value="${siteProfileClassListingForm}"/>

<script type="text/javascript">
function submitNewForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/site/siteProfileClassMaint.do';
    document.forms[0].process.value = "create";
    document.forms[0].submit();
    return false;
}

function submitRemoveForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/site/siteProfileClassListing.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function submitListingForm(page) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/site/siteProfileClassListing.do';
    document.forms[0].process.value = "list";
    document.forms[0].submit();
    return false;
}

function submitMaintForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/site/siteProfileClassMaint.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}
</script>

<html:form action="/admin/site/siteProfileClassListing.do" method="post">
<html:hidden property="process" value="list"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - Site Profile Class Listing</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="0%"> 
      <div class="jc_search_panel_open"> 
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td> 
              <table class="jc_panel_table_header" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td>Search</td>
                  <td> 
                    <div align="right"> 
                      <html:submit property="submitButton" value="Search" styleClass="jc_submit_button" onclick="javascript:return submitListingForm('');"/>
                      <html:submit property="submitButton" value="New" styleClass="jc_submit_button" onclick="return submitNewForm('');"/>
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
            <td class="jc_input_label">Site Profile Class</td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srSiteProfileClassName" mode="E,E,E" styleClass="jc_input_object"/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
        </table>
      </div>
    </td>
    <td width="100%" valign="top" class="jc_list_panel_open"> 
      <c:if test="${siteProfileClassListingForm.siteProfileClasses != null}">
        <table class="jc_panel_table_header" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td width="100%">Site Profile Class Listing 
              Result</td>
            <td nowrap> 
              <div align="right">
                <html:submit property="submitButton" value="Remove" styleClass="jc_submit_button" onclick="return submitRemoveForm('remove');"/>
                <html:submit property="submitButton" value="New" styleClass="jc_submit_button" onclick="return submitMaintForm('create');"/>
              </div>
            </td>
          </tr>
		</table>
		<br>
        <span class="jc_input_error">
        <logic:messagesPresent property="error" message="true"> 
          <html:messages property="error" id="errorText" message="true"> 
          <bean:write name="errorText"/><br><br>
          </html:messages> 
        </logic:messagesPresent>
        </span>
        <table width="100%" border="0" cellspacing="0" cellpadding="1">
           <tr> 
            <td class="jc_list_table_header"></td>
            <td class="jc_list_table_header">Site Profile Class</td>
            <td class="jc_list_table_header">Language</td>
            <td class="jc_list_table_header">
              <div align="center">
              System
              </div>
            </td>
          </tr>
          <c:forEach var="siteProfileClass" items="${siteProfileClassListingForm.siteProfileClasses}">
          <tr class="jc_list_table_row"> 
            <td width="50" class="jc_list_table_content">
              <html:hidden name="siteProfileClass" property="siteProfileClassId" indexed="true"/>
              <html:hidden name="siteProfileClass" property="langName" indexed="true"/>
              <div align="center">
              <c:choose>
				<c:when test="${siteProfileClass.systemRecord == 'Y'}">
				  <html:checkbox indexed="true" name="siteProfileClass" property="remove" value="Y" styleClass="tableContent" disabled="true"/>
				</c:when>
				<c:otherwise>
				  <html:checkbox indexed="true" name="siteProfileClass" property="remove" value="Y" styleClass="tableContent"/>
				</c:otherwise>
			  </c:choose>
              </div>
            </td>
            <td width="50%" class="jc_list_table_content">
              <html:link page="/admin/site/siteProfileClassMaint.do?process=edit" paramId="siteProfileClassId" paramName="siteProfileClass" paramProperty="siteProfileClassId">
                ${siteProfileClass.siteProfileClassName}
              </html:link>
            </td>
            <td width="25%" class="jc_list_table_content">
              ${siteProfileClass.langName}
            </td>
            <td width="5%" class="jc_list_table_content">
              <div align="center">
              ${siteProfileClass.systemRecord}
              </div>
            </td>
          </tr>
          </c:forEach>
        </table>
      </c:if>
    </td>
  </tr>
</table>
</html:form>
