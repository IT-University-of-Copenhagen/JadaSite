<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@page import="com.jada.system.ApplicationGlobal"%><html>
<head>
<title>JadaSite e-commerce solutions sign-in</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%= request.getContextPath() %>/admin/web/styles.css" type="text/css">
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-color: #f1f3f5;
}
-->
</style>
</head>
<body bgcolor="#FFFFFF" text="#000000">
<html:form action="/admin/login.do" method="post">
<html:hidden property="process" value="submit"/>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<div align="center">
<table width="500" border="0" align="center" cellpadding="0">
  <tr>
    <td><div align="left"><img src="<%= request.getContextPath() %>/admin/images/signin-logo.gif"/></div></td>
  </tr>
</table>
<div style="background-image: url(<%= request.getContextPath() %>/admin/images/signin-bg.jpg); height: 300px; width: 500px">
<div style="padding: 50px" align="left">
<table width="100%" height="300" border="0" align="center" cellpadding="0" cellspacing="0" style="height: 30px">
  <tr> 
	<td class="jc_input_label" style="padding: 5px">Version <%= ApplicationGlobal.getVersion() %></td>
	<td width="100%" style="padding: 5px">  </td>
  </tr>
  <tr>
    <td colspan="2" style="padding: 5px">
      <span class="jc_input_error"> <logic:messagesPresent property="install" message="true"> 
      <html:messages property="install" id="errorText" message="true"><br>
      <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
      </span>
      <span class="jc_input_error"> <logic:messagesPresent property="error" message="true"> 
      <html:messages property="error" id="errorText" message="true"><br>
      <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
      </span>
      <span class="jc_input_comment"> <logic:messagesPresent property="message" message="true"> 
      <html:messages property="message" id="messageText" message="true"><br>
      <bean:write name="messageText"/> </html:messages> </logic:messagesPresent> 
      </span>
    </td>
  </tr>
  <tr> 
	<td class="jc_input_label" style="padding: 5px">User Id</td>
	<td width="100%" style="padding: 5px"> <html:text property="userId" size="20" styleClass="jc_input_object"/> </td>
  </tr>
  <tr> 
	<td class="jc_input_label" style="padding: 5px">Password</td>
	<td style="padding: 5px"> <html:password property="userPassword" size="20" styleClass="jc_input_object"/> 
  </tr>
  <tr> 
	<td style="padding: 5px"></td>
	<td style="padding: 5px"><html:submit property="submit" value="Log in" styleClass="jc_submit_button"/></td>
  </tr>
</table>
</div>
</div>
</div>
</html:form>
<script language="JavaScript">
document.loginActionForm.userId.focus();
</script>
</body>
</html>