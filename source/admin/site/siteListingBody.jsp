<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="siteListingForm" scope="session" value="${siteListingForm}"/>

<script type="text/javascript">
function submitNewForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/site/siteMaint.do';
    document.forms[0].process.value = "create";
    document.forms[0].submit();
    return false;
}

function submitRemoveForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/site/siteListing.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function submitListingForm(page) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/site/siteListing.do';
    document.forms[0].process.value = "list";
    document.forms[0].submit();
    return false;
}

function submitMaintForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/site/siteMaint.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}
</script>

<html:form action="/admin/site/siteListing.do" method="post">
<html:hidden property="process" value="list"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - Site Listing</td>
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
            <td class="jc_input_label">Site</td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srSiteId" mode="E,E,E" styleClass="jc_input_object"/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Description</td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srSiteDesc" mode="E,E,E" styleClass="jc_input_object"/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Active</td>
          </tr>
          <tr> 
            <td class="jc_input_object">
              <layout:radio layout="false" property="srActive" mode="E,E,E" value="Y"/>Active 
              <br>
              <layout:radio layout="false" property="srActive" mode="E,E,E" value="N"/>In-active 
              <br>
              <layout:radio layout="false" property="srActive" mode="E,E,E" value="*"/>All 
              <br>
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
        </table>
      </div>
    </td>
    <td width="100%" valign="top" class="jc_list_panel_open"> 
      <c:if test="${siteListingForm.sites != null}">
        <table class="jc_panel_table_header" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td width="100%">Site Listing 
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
            <td class="jc_list_table_header">Site</td>
            <td class="jc_list_table_header">Description</td>
            <td width="100" class="jc_list_table_header"><div align="center" style="width: 100%">Active</div></td>
            <td width="100" class="jc_list_table_header"><div align="center" style="width: 100%">System</div></td>
          </tr>
          <c:forEach var="site" items="${siteListingForm.sites}">
          <tr class="jc_list_table_row"> 
            <td width="50" class="jc_list_table_content">
              <div align="center">
                <c:choose>
                  <c:when test="${site.systemRecord != 'Y'}">
				    <input type="checkbox" name="siteIds" value="${site.siteId}">
				  </c:when>
				  <c:otherwise>
				    <input type="checkbox" name="siteIds" value="${site.siteId}" disabled>
				  </c:otherwise>
				</c:choose>
              </div>
            </td>
            <td width="30%" class="jc_list_table_content">
              <html:link page="/admin/site/siteMaint.do?process=edit" paramId="siteId" paramName="site" paramProperty="siteId">
                <c:out value="${site.siteId}"/>
              </html:link>
            </td>
            <td width="30%" class="jc_list_table_content">
              <c:out value="${site.siteDesc}"/>
            </td>
            <td width="100" class="jc_list_table_content">
              <div align="center" style="width: 100%"><c:out value="${site.active}"/></div>
            </td>
            <td width="100" class="jc_list_table_content">
              <div align="center" style="width: 100%"><c:out value="${site.systemRecord}"/></div>
            </td>
          </tr>
          </c:forEach>
        </table>
      </c:if>
    </td>
  </tr>
</table>
</html:form>
