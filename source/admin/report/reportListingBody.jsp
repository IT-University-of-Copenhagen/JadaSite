<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="reportListingForm" scope="request" value="${reportListingForm}"/>

<script language="JavaScript">
function submitForm(methodType) {
    document.forms[0].action = '/${adminBean.contextPath}/admin/report/reportListing.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}
function submitNewForm(page) {
    document.forms[0].action = '/${adminBean.contextPath}/admin/report/reportMaint.do';
    document.forms[0].process.value = "create";
    document.forms[0].submit();
    return false;
}
</script>

<html:form action="/admin/report/reportListing.do" method="post">
<html:hidden property="process" value="list"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Report Listing</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="0%" valign="top"> 
      <div class="jc_search_panel_open"> 
        <table width="100%" border="0" cellspacing="0" cellpadding="5">
          <tr> 
            <td> 
              <table class="jc_panel_table_header" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td>Search</td>
                  <td> 
                    <div align="right"> 
                      <html:submit property="submitButton" value="Search" styleClass="jc_submit_button" onclick="return submitForm('list');"/>
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
            <td class="jc_input_label">Report</td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srReportName" mode="E,E,E" styleClass="jc_input_object"/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
        </table>
      </div>
    </td>
    <td width="100%" valign="top" class="jc_list_panel_open"> 
      <c:if test="${reportListingForm.reports != null}">
        <table class="jc_panel_table_header" width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td>Reports</td>
            <td> 
              <div align="right">
                <html:submit property="submitButton" value="Remove" styleClass="jc_submit_button" onclick="return submitForm('remove');"/>
                <html:submit property="submitButton" value="New" styleClass="jc_submit_button" onclick="return submitNewForm('create');"/>
              </div>
            </td>
          </tr>
		</table>
        <span class="jc_input_error">
        <br>
        <logic:messagesPresent property="error" message="true"> 
          <html:messages property="error" id="errorText" message="true"> 
          <bean:write name="errorText"/><br><br>
          </html:messages> 
        </logic:messagesPresent>
        </span>
        <table width="100%" border="0" cellspacing="0" cellpadding="3">
           <tr> 
            <td class="jc_list_table_header"></td>
            <td class="jc_list_table_header">Report Name</td>
            <td class="jc_list_table_header">Description</td>
            <td class="jc_list_table_header"><div align="center">System</div></td>
          </tr>
          <c:forEach var="report" items="${reportListingForm.reports}">
          <tr class="jc_list_table_row"> 
            <td width="50" class="jc_list_table_content">
              <div align="center">
	            <c:choose>
				  <c:when test="${report.systemRecord == 'Y'}">
				    <input type="checkbox" name="reportIds" value="${report.reportId}" disabled>
				  </c:when>
				  <c:otherwise>
					<input type="checkbox" name="reportIds" value="${report.reportId}">
				  </c:otherwise>
				</c:choose>
              </div>
            </td>
            <td width="300" class="jc_list_table_content">
              <html:link page="/admin/report/reportMaint.do?process=edit" paramId="reportId" paramName="report" paramProperty="reportId">
                ${report.reportName}
              </html:link>
            </td>
            <td width="300" class="jc_list_table_content">
              ${report.reportDesc}
            </td>
            <td width="300" class="jc_list_table_content">
              <div align="center">${report.systemRecord}</div>
            </td>
          </tr>
          </c:forEach>
        </table>
      </c:if>
    </td>
  </tr>
</table>
</html:form>
