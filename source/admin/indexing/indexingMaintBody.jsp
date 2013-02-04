<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>

<script language="JavaScript">
function submitForm(methodType) {
	<c:if test="${indexingForm.inProgress}">
	var result = confirm('Indexing currenty in progress.  Performing another indexing may negative affect system.  Do you still want to continue?');
	if (!result) {
		return false;
	}
	</c:if>
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}
</script>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="indexingForm" scope="request" value="${indexingForm}"/>
<html:form action="/admin/indexing/indexingMaint.do" method="post">
<html:hidden property="process" value="new"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - Index site for search</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="100%" valign="top" class="jc_list_panel_open"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
        <tr> 
          <td class="jc_panel_table_header"></td>
          <td> 
          </td>
        </tr>
	  </table>
      <br>
        <span class="jc_text_header">Index update status</span>
        <br>
        <br>
		<table width="500" class="grid" cellpadding="2">
		  <tr>
		    <td width="60%"><span class="jc_input_label">Start time</span></td>
		    <td width="40%">${indexingForm.indexerInfo.indexerStartTime}</td>
		  </tr>
		  <tr>
		    <td><span class="jc_input_label">Last update time</span></td>
		    <td>${indexingForm.indexerInfo.indexerLastUpdateTime}</td>
		  </tr>
		  <tr>
		    <td><span class="jc_input_label">Overall status</span></td>
		    <td>${indexingForm.indexerInfo.indexerStatus}</td>
		  </tr>
		  <tr>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		  </tr>
		  <tr>
		    <td><span class="jc_input_label">Removing previous entries</span></td>
		    <td>&nbsp;</td>
		  </tr>
		  <tr>
		    <td><span class="jc_input_label">Status</span></td>
		    <td>${indexingForm.indexerInfo.removeStatus}</td>
		  </tr>
		  <tr>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		  </tr>
		  <tr>
		    <td><span class="jc_input_label">Index content</span></td>
		    <td>&nbsp;</td>
		  </tr>
		  <tr>
		    <td><span class="jc_input_label">Count</span></td>
		    <td>${indexingForm.indexerInfo.contentUpdateCount}/${indexingForm.indexerInfo.contentTotalCount}</td>
		  </tr>
		  <tr>
		    <td><span class="jc_input_label">Status</span></td>
		    <td>${indexingForm.indexerInfo.contentUpdateStatus}</td>
		  </tr>
		  <tr>
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		  </tr>
		  <tr>
		    <td><span class="jc_input_label">Index item</span></td>
		    <td>&nbsp;</td>
		  </tr>
		  <tr>
		    <td><span class="jc_input_label">Count</span></td>
		    <td>${indexingForm.indexerInfo.itemUpdateCount}/${indexingForm.indexerInfo.itemTotalCount}</td>
		  </tr>
		  <tr>
		    <td><span class="jc_input_label">Status</span></td>
		    <td>${indexingForm.indexerInfo.itemUpdateStatus}</td>
		  </tr>
		</table>
      <br><br>
      <html:submit property="submitButton" value="Index" styleClass="jc_submit_button" onclick="return submitForm('index');"/>&nbsp;&nbsp;&nbsp;
      <a class="jc_simple_link" href="/${adminBean.contextPath}/admin/indexing/indexingMaint.do?process=start">Update status</a>
      <br>
    </td>
  </tr>
</table>
</html:form>
