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
a:link { 
	color: #000000;
	text-decoration: underline;
}
a:visited { 
	color: #000000;
	text-decoration: underline;
}
a:hover{
	color: #ff0000;
	text-decoration: underline;
}
</style>
<script type="text/javascript" language="JavaScript">
function submitGenerateKey() {
	document.installForm.process.value = "generate";
	document.installForm.submit();
}
</script>
</head>
<body>
<br>
<br>
<br>
<br>
<table width="600" border="0" cellspacing="0" cellpadding="10" align="center">
  <tr>
    <td valign="top">
      <img src="/${installForm.contextPath}/admin/images/jadasite-logo.gif"/>
      <br>
      <br>
      <div class="panel">
      <br>
      <span class="header">Installation</span>
      <br><br>
      <logic:messagesPresent property="message" message="true"> 
        <html:messages property="message" id="errorText" message="true"> 
        <span class="error"><bean:write name="errorText"/></span>
        </html:messages> 
      </logic:messagesPresent>
      <br>
      <c:if test="${installForm.installCompleted == false}"> 
      <html:form method="post" action="/install/process/databaseTestAction">
      <html:hidden property="process" value="update"/>
      <table width="600" border="0" cellspacing="0" cellpadding="3">
        <tr>
          <td>
          <span class="label">Driver</span><br>
          Database driver class name to be used.  If you are using MySql as the database, please enter 
          com.mysql.jdbc.Driver.
          </td>
        </tr>
        <tr>
          <td><html:text property="driver" size="50"/></td>
        </tr>
        <tr>
          <td>
            <logic:messagesPresent property="driver" message="true"> 
              <html:messages property="driver" id="errorText" message="true"> 
                <span class="error"><bean:write name="errorText"/></span>
              </html:messages> 
            </logic:messagesPresent>
          </td>
        </tr>
        <tr>
          <td>
          <span class="label">Database host name</span><br>
          The host name of the machine where the database is located.  If the application is installed
          together on the same machine of the database, the host name of the database is usually 'localhost'.
          </td>
        </tr>
        <tr>
          <td><html:text property="dbHost" size="20"/></td>
        </tr>
        <tr>
          <td>
            <logic:messagesPresent property="dbHost" message="true"> 
              <html:messages property="dbHost" id="errorText" message="true"> 
                <span class="error"><bean:write name="errorText"/></span>
              </html:messages> 
            </logic:messagesPresent>
          </td>
        </tr>
        <tr>
          <td>
          <span class="label">Database port number</span><br>
          Listener port of the database.  If you are using MySql default installation, the listener port is 3306.
          </td>
        </tr>
        <tr>
          <td><html:text property="dbPort" size="10"/></td>
        </tr>
        <tr>
          <td>
            <logic:messagesPresent property="dbPort" message="true"> 
              <html:messages property="dbPort" id="errorText" message="true"> 
                <span class="error"><bean:write name="errorText"/></span>
              </html:messages> 
            </logic:messagesPresent>
          </td>
        </tr>
        <tr>
          <td>
          <span class="label">Name of the database</span><br>
          Name of the empty database.  Please make sure the database is already created and is empty.
          </td>
        </tr>
        <tr>
          <td><html:text property="dbName" size="20"/></td>
        </tr>
        <tr>
          <td>
            <logic:messagesPresent property="dbName" message="true"> 
              <html:messages property="dbName" id="errorText" message="true"> 
                <span class="error"><bean:write name="errorText"/></span>
              </html:messages> 
            </logic:messagesPresent>
          </td>
        </tr>
        <tr>
          <td>
          <span class="label">User id</span><br>
          User id to be used to connect to the database.
          </td>
        </tr>
        <tr>
          <td><html:text property="username" size="30"/></td>
        </tr>
        <tr>
          <td>
            <logic:messagesPresent property="username" message="true"> 
              <html:messages property="username" id="errorText" message="true"> 
                <span class="error"><bean:write name="errorText"/></span>
              </html:messages> 
            </logic:messagesPresent>
          </td>
        </tr>
        <tr>
          <td>
          <span class="label">User password</span><br>
          Password to be used to connect to the database.
          </td>
         </tr>
        <tr>
          <td><html:password property="password" size="30"/></td>
        </tr>
        <tr>
          <td>
            <logic:messagesPresent property="password" message="true"> 
              <html:messages property="password" id="errorText" message="true"> 
                <span class="error"><bean:write name="errorText"/></span>
              </html:messages> 
            </logic:messagesPresent>
          </td>
        </tr>
        <tr>
          <td><hr></td>
        </tr>
        <tr>
          <td>
            <span class="label">Working Directory</span><br>
            Directory on the local machine.  This directory will contains all the uploaded templates, 
            site search indexes, user uploaded images, etc.  Directory entered here has to be writable 
            by Jada Site.
          </td>
        </tr>
        <tr>
          <td><html:text property="workingDirectory" size="50"/></td>
        </tr>
        <tr>
          <td>
            <logic:messagesPresent property="workingDirectory" message="true"> 
              <html:messages property="workingDirectory" id="errorText" message="true"> 
                <span class="error"><bean:write name="errorText"/></span>
              </html:messages> 
            </logic:messagesPresent>
          </td>
        </tr>
        <tr>
          <td><hr></td>
        </tr>
        <tr>
          <td>
            <span class="label">Log Directory</span><br>
            Directory on the local machine where log files should be located.
            Directory entered here has to be writable by Jada Site.
          </td>
        </tr>
        <tr>
          <td><html:text property="log4jDirectory" size="50"/></td>
        </tr>
        <tr>
          <td>
            <logic:messagesPresent property="log4jDirectory" message="true"> 
              <html:messages property="log4jDirectory" id="errorText" message="true"> 
                <span class="error"><bean:write name="errorText"/></span>
              </html:messages> 
            </logic:messagesPresent>
          </td>
        </tr>
        <tr>
          <td>
            <span class="label">Encryption Key</span><br>
            Key to be used for encryption and decryption.
            You can simply clicks generate key to create a key for new installation.  Please
            make sure the key is written down for future reference.<br><br>
            If you are performing an upgrade, please enter your existing key.  This is 
            important in order to be able to use the existing password in already in the database.
          </td>
        </tr>
        <tr>
          <td>
            <html:text property="encryptionKey" size="50"/>
            <a href="javascript:submitGenerateKey();">Generate key</a>
          </td>
        </tr>
        <tr>
          <td>
            <logic:messagesPresent property="encryptionKey" message="true"> 
              <html:messages property="encryptionKey" id="errorText" message="true"> 
                <span class="error"><bean:write name="errorText"/></span>
              </html:messages> 
            </logic:messagesPresent>
          </td>
        </tr>
        <tr>
          <td>
            <span class="message">
            <c:out value='${installForm.detailLog}' escapeXml="false"/>
            </span>
          </td>
        </tr>
        <tr>
          <td><input type="submit" name="Next" value="Next" class="jc_submit_button"></td>
        </tr>
      </table>
      </html:form>
      </c:if>
      </div>
    </td>
  </tr>
</table>
</body>
</html>