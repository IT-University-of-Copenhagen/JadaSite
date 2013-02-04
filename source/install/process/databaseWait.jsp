<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="refresh" content="0;url=/<c:out value='${installForm.contextPath}'/>/install/process/databaseLoadAction.do?process=create" />
<title>Install</title>
<link rel="stylesheet" type="text/css" href="/${installForm.contextPath}/admin/web/styles.css">
<style type="text/css">
.header {
  font-size: 2em;
  font-weight: bold;
}
.label {
  font-weight: bold;
}
.panel {
  border: 1px solid #6E89DD;
  padding: 10px;
  font-size: 1em;
  font-weight: normal;
}
.error {
  font-size: 1em;
  color: red;
}
.message {
  font-size: 0.8em;
  color: blue;
}
</style>
</head>
<body>
<br>
<br>
<br>
<br>
<table width="600" border="0" cellspacing="0" cellpadding="10" align="center">
  <tr>
    <td width="100%" valign="top">
      <img src="/${installForm.contextPath}/admin/images/jadasite-logo.gif"/>
      <br>
      <br>
      <div class="panel">
      <br>
      <table width="100%" width="600" height="300" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td valign="top">
            <br>
            <span class="header">Installation</span>
            <br><br>
            Database initializing, please wait ....<br>
            Depending on the speed of your host, it may takes up to a few minutes.
          </td>
        </tr>
      </table>
      </div>
    </td>
  </tr>
</table>
</body>
</html>