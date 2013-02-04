<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Install</title>
<c:set var="installForm" scope="request" value="${installForm}"/>
<link rel="stylesheet" type="text/css" href="/<c:out value='${installForm.contextPath}'/>/admin/web/styles.css">
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
a:link, a:visited, a:active, a:hover {
  color: blue;
}
</style>
</head>
<body>
<br>
<br>
<br>
<br>
<html:form method="post" action="/install/process/databaseLoadAction?process=complete">
<table width="600" border="0" cellspacing="0" cellpadding="10" align="center">
  <tr>
    <td width="100%" valign="top">
      <img src="/${installForm.contextPath}/admin/images/jadasite-logo.gif"/>
      <br>
      <br>
      <div class="panel">
      <table width="600" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td valign="top">
            <br>
            <span class="header">Installation</span>
            <br><br>
            <c:choose>
              <c:when test="${installForm.databaseExist}">
                <table border="0" cellspacing="5" cellpadding="5" align="center">
                  <tr> 
                    <td>
                        Database already exist and will not be overridden.
                        This is a normal behavior when performing upgrade to JadaSite.<br>
                        You can simply mark the installation process complete.<br>
                        <input type="submit" name="complete" value="Complete" class="jc_submit_button">
                    </td>
                  </tr>
                </table>
              </c:when>
              <c:when test="${installForm.error}">
                <table border="0" cellspacing="5" cellpadding="5" align="center">
                  <tr> 
                    <td>
                      <b>
                      <font color="red">Database loaded with errors.  Due to the errors, setup may not have completed successfully.
                      </font>
                      </b>
                    </td>
                  </tr>
                  <tr>
                    <td>
                    <hr>
                      <font color="red">
                      <logic:messagesPresent property="dbcreated" message="true">
                        <html:messages property="dbcreated" id="errorText" message="true">
                        <span class="jc_tran_error">
                          <bean:write name="errorText"/>
                        </span>
                        </html:messages>
                      </logic:messagesPresent>
                      </font>
                    <span style="font-size: 0.8em">
                    <c:out value='${installForm.detailLog}' escapeXml="false"/>
                    </span>
                    </td>
                  </tr>
                </table>
              </c:when>
              <c:otherwise>
                <table width="600" height="300" border="0" cellspacing="5" cellpadding="5">
                  <tr> 
                    <td valign="top">
                      <b>Congratulation</b><br><br>
                      <c:choose>
                		<c:when test='${installForm.upgrade}'>
                		Database upgraded successfully.<br><br>
                        </c:when>
                        <c:otherwise>
                        Database initialized successfully.<br><br>
                        </c:otherwise>
                      </c:choose>
                      
                      Before you start, please make sure JadaSite is restarted on the application server.
                      <br>
                      <br>
                    </td>
                  </tr>
                </table>
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
      </table>
      </div>
    </td>
  </tr>
</table>
</html:form>
</body>
</html>