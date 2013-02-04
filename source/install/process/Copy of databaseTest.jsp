<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Install</title>
<c:set var="installForm" scope="request" value="${installForm}"/>
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
        <div id="panelHeader2">Database connection test</div>
      </div>
      <div id="panelBody">
        <c:choose>
          <c:when test="${installForm.error}">
            <table width="100%" border="0" cellspacing="5" cellpadding="5" align="center">
              <tr> 
                <td>
                  <b><font color="red">Unable to connect to database based on information defined in hibernate.cfg.xml.</font></b>
                  <br>
                  <br>
                  hibernate.connection.driver_class=<font color="blue"><c:out value='${installForm.driver}'/></font><br>
                  hibernate.connection.url=<font color="blue"><c:out value='${installForm.url}'/></font><br>
                  hibernate.connection.username=<font color="blue"><c:out value='${installForm.username}'/></font><br>
                  hibernate.connection.password=<font color="blue">xxxxxxxy</font><br>
                  <br> 
                  <span style="font-size: 0.8em">Note: this is only a simple connectivity test.</span>
                </td>
              </tr>
              <tr>
                <td>
                <hr>
                <span style="font-size: 0.8em">
                <c:out value='${installForm.detailLog}' escapeXml="false"/>
                </span>
                </td>
              </tr>
            </table>
          </c:when>
          <c:otherwise>
            <table width="100%" border="0" cellspacing="5" cellpadding="5" align="center">
              <tr> 
                <td>
                  <b>Database successfully connnected based on information defined in hibernate.cfg.xml.</b>
                  <br>
                  <br>
                  hibernate.connection.driver_class=<font color="blue"><c:out value='${installForm.driver}'/></font><br>
                  hibernate.connection.url=<font color="blue"><c:out value='${installForm.url}'/></font><br>
                  hibernate.connection.username=<font color="blue"><c:out value='${installForm.username}'/></font><br>
                  hibernate.connection.password=<font color="blue">xxxxxxx</font><br>
                  <br>
                  <span style="font-size: 0.8em">Note: this is only a simple connectivity test.</span>
                </td>
              </tr>
              <tr>
                <td>
                <div align="right">
                <form name="form1" method="post" action="/<c:out value='${installForm.contextPath}'/>/install/process/databaseLoadAction.do?process=wait">
                <input type="submit" name="Next" value="Next" class="jc_submit_button">
                </form>
                </div>
                </td>
              </tr>
            </table>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
    </td>
  </tr>
</table>
</body>
</html>