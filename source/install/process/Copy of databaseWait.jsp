<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="installForm" scope="request" value="${installForm}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="refresh" content="0;url=/<c:out value='${installForm.contextPath}'/>/install/process/databaseLoadAction.do?process=create" />
<title>Install</title>
<link rel="stylesheet" type="text/css" href="/<c:out value='${installForm.contextPath}'/>/admin/web/yui/fonts/fonts.css">
<link rel="stylesheet" type="text/css" href="/<c:out value='${installForm.contextPath}'/>/admin/web/styles.css">
<style type="text/css">
#panelHeader1 {
  background: url(/<c:out value='${installForm.contextPath}'/>/admin/images/background/blue_left.gif) no-repeat scroll left top; 
  padding-left: 15px;
}
#panelHeader2 {
  background: url(/<c:out value='${installForm.contextPath}'/>/admin/images/background/blue_right.gif) no-repeat scroll right top;
  font-size: 1.2em;
  font-weight: bold;
  color: #ffffff;
  overflow: hidden;
  padding: 3px;
}
#panelBody {
  border: 1px solid #6E89DD;
  background-color: #dbdbdb;
  font-size: 1em;
  font-weight: normal;
}
</style>
</head>
<body>
<br>
<br>
<br>
<br>
<br>
<table width="80%" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr> 
    <td width="100%"> 
    <div>
      <div id="panelHeader1"> 
        <div id="panelHeader2">Database initialization</div>
      </div>
      <div id="panelBody">
      <br><br><br>
      <div align="center">Initializing, please wait ....</div>
      <br><br><br>
      </div>
    </div>
    </td>
  </tr>
</table>
</body>
</html>