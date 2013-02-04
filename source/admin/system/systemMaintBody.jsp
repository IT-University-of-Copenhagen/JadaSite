<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html:form method="post" action="/admin/system/systemMaint?process=reset"> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - Reset Installation</td>
  </tr>
</table>
<br>
<table width="600" border="0" cellspacing="0" cellpadding="5">
  <tr> 
    <td width="100%" valign="top"> 
    <p>Once installation is performed, the web interface for installation configuration will be automatically 
    disabled to protect the fundamental configuration from further changes.</p>
    <p>
    In case changes is required again, you can reset the installation configuration to allow for changes via the web
    interface.  Once reset is performed, please proceed as soon as possible to perform the changes and to
    complete the installation.  This is very important to ensure no illegal access to the installation
    configuration steps.
    </p>
    Proceed to reset installation configuration <br><br>
    <input type="submit" name="Proceed" value="Reset" class="jc_submit_button">
    </td>
  </tr>
</table>
</html:form>
