<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html:form method="post" action="/admin/system/systemMaint"> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - Reset Installation</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr> 
    <td width="100%" valign="top"> 
    <p>Installation configuration has been reset.  </p>
    <p>
    Please proceed immediately to 
    <a class="jc_submit_link" href="<c:out value='/${adminBean.contextPath}'/>/install/process/databaseTestAction.do?process=start">installation configuration</a>
    screen to complete the configuration.
    </p>
    </td>
  </tr>
</table>
</html:form>
