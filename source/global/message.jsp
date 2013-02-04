<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html>
<head>
<title>JadaSite content management and e-commerce system</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body bgcolor="#FFFFFF" text="#000000">
<p><font face="Arial, Helvetica, sans-serif"><b><font size="+2">JadaSite content management and e-commerce
  system</font></b><br>
  </font></p>
<hr>
<p><font size="+2" face="Arial, Helvetica, sans-serif">Our apologies </font><font face="Arial, Helvetica, sans-serif"><br>
  The page you requested cannot be displayed <br>
  </font></p>
<font face="Arial, Helvetica, sans-serif" size="3"><b>Explanation</b></font><br>
<font face="Arial, Helvetica, sans-serif" size="2">
<html:messages property="message" id="messageText" message="true"> 
<bean:write name="messageText"/>
</html:messages>
</font>
</body>
</html>
