<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="ieProfileListingForm" scope="request" value="${ieProfileListingForm}"/>

<script language="JavaScript">
function submitNewForm(page) {
    document.forms[0].action = '/${adminBean.contextPath}/admin/ie/ieProfileMaint.do';
    document.forms[0].process.value = "create";
    document.forms[0].submit();
    return false;
}

function submitRemoveForm(methodType) {
    document.forms[0].action = '/${adminBean.contextPath}/admin/ie/ieProfileListing.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function submitListingForm(methodType) {
    document.forms[0].action = '/${adminBean.contextPath}/admin/ie/ieProfileListing.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}
</script>
<html:form action="/admin/ie/ieProfileListing.do" method="post">
<html:hidden property="process" value="list"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - Import/Export Profile Listing</td>
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
                      <html:submit property="submitButtom" value="Search" styleClass="jc_submit_button" onclick="return submitListingForm('list');"/>
                      <html:submit property="submitButtom" value="New" styleClass="jc_submit_button" onclick="return submitNewForm('');"/>
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
            <td class="jc_input_label">Import/Export Profile</td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srIeProfileHeaderName" mode="E,E,E" styleClass="jc_input_object"/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
        </table>
      </div>
    </td>
    <td width="100%" valign="top" class="jc_list_panel_open"> 
      <c:if test="${ieProfileListingForm.ieProfileHeaders != null}">
        <table class="jc_panel_table_header" width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td>Import/Export Profile Listing 
              Result</td>
            <td> 
              <div align="right">
                <html:submit property="submitButtom" value="Remove" styleClass="jc_submit_button" onclick="return submitRemoveForm('remove');"/>
                <html:submit property="submitButtom" value="New" styleClass="jc_submit_button" onclick="return submitNewForm('');"/>
              </div>
            </td>
          </tr>
		</table>
		<br>
        <table width="100%" border="0" cellspacing="0" cellpadding="1">
          <tr>
            <td width="100" class="jc_list_table_header"></td>
            <td class="jc_list_table_header">Profile Name</td>
            <td class="jc_list_table_header"><div align="center">Type</div></td>
            <td class="jc_list_table_header"><div align="center">System</div></td>
          </tr>
          <c:forEach var="ieProfileHeader" items="${ieProfileListingForm.ieProfileHeaders}" varStatus="xstatus">
          <tr class="jc_list_table_row"> 
            <td class="jc_list_table_content">
              <div align="center">
                <c:choose>
                  <c:when test='${ieProfileHeader.modify == true}'>
                    <html:checkbox indexed="true" name="ieProfileHeader" property="selected"/>
                  </c:when>
                  <c:otherwise>
                    <html:checkbox indexed="true" name="ieProfileHeader" property="selected" disabled="true"/>
                  </c:otherwise>
                </c:choose>
                <html:hidden indexed="true" name="ieProfileHeader" property="ieProfileHeaderId"/>
              </div>
            </td>
            <td class="jc_list_table_content">
              <html:link page="/admin/ie/ieProfileMaint.do?process=edit" paramId="ieProfileHeaderId" paramName="ieProfileHeader" paramProperty="ieProfileHeaderId">
                ${ieProfileHeader.ieProfileHeaderName}
              </html:link>
            </td>
            <td class="jc_list_table_content">
              <div align="center">${ieProfileHeader.ieProfileTypeValue}</div>
            </td>
            <td class="jc_list_table_content">
              <div align="center">${ieProfileHeader.systemRecord}</div>
            </td>
          </tr>
          </c:forEach>
        </table>
      </c:if>
    </td>
  </tr>
</table>
</html:form>
