<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>

<script language="JavaScript">
function submitForm(methodType) {
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}
</script>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="syndicationMaintForm" scope="session" value="${syndicationMaintForm}"/>
<html:form action="/admin/syndication/syndicationMaint.do" method="post">
<html:hidden property="process" value="new"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - Syndication Maintenance</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="100%" valign="top" class="jc_list_panel_open"> 
      <table class="jc_panel_table_header" width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
        <tr> 
          <td>Syndication Information</td>
          <td> 
            <div align="right">
              <html:submit property="submitButton" value="Update" styleClass="jc_submit_button" onclick="return submitForm('update');"/>
            </div>
          </td>
        </tr>
	  </table>
      <br>
      <table width="0" border="0" cellspacing="0" cellpadding="1">
         <tr> 
          <td class="jc_list_table_header" width="50">&nbsp;</td>
          <td class="jc_list_table_header" width="300">URL</td>
          <td class="jc_list_table_header" width="50">Active</td>
        </tr>
        <c:forEach var="syn" items="${syndicationMaintForm.syns}">
        <html:hidden indexed="true" name="syn" property="synId"/> 
        <tr class="jc_list_table_row"> 
          <td width="50" class="jc_list_table_content">
            <html:hidden indexed="true" name="syn" property="index"/> 
            <div align="center"><c:out value="${syn.index}"/></div>
          </td>
          <td width="300" class="jc_list_table_content">
            <html:text indexed="true" name="syn" property="synUrl" size="80"/> 
          </td>
          <td width="50" class="jc_list_table_content">
            <html:checkbox indexed="true" name="syn" property="active"/> 
          </td>
        </tr>
        </c:forEach>
      </table>
    </td>
  </tr>
</table>
</html:form>
