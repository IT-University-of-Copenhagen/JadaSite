<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<script type="text/javascript" language="JavaScript"><!--
function submitForm(type) {
    document.forms[0].process.value = type;
    document.forms[0].submit();
}

//--></script>
<jsp:useBean id="myAccountPortalActionForm"  type="com.jada.myaccount.portal.MyAccountPortalActionForm"  scope="request" />
<c:set var="myAccountBean" scope="request" value="${myAccountPortalActionForm}"/>
<div id="my-account-container">
  <div id="my-account-nav-container">
  	<jsp:include page="/myaccount/myAccountNav.jsp" />
  </div>
</div>
