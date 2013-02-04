<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<script type="text/javascript" language="JavaScript"><!--
function submitForm(type) {
    document.shoppingCartActionForm.process.value = type;
    document.shoppingCartActionForm.submit();
}

//--></script>
<c:set var="contentBean" scope="request" value="${contentBean}"/>
<c:set var="shoppingCartActionForm" scope="request" value="${shoppingCartActionForm}"/>
<div id="shopping-cart-container">
<%@ include file="/content/shoppingCart/shoppingCartSteps.jsp" %>
<br>
<div id="shopping-cart-login-title-container">
  <span id="shopping-cart-login-title"><lang:contentMessage value="Sign-in"/></span>
</div>
<br>
<html:form method="post" action="/content/checkout/shoppingCartLogin.do?process=login&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}">
<html:hidden property="process" value=""/>
<html:hidden property="cash"/>

<div id="shopping-cart-login-container">
<table border="0" cellspacing="0" cellpadding="0" id="shopping-cart-login-table">
  <tr id="shopping-cart-login-header">
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td nowrap>&nbsp;</td>
  </tr>
  <tr> 
    <td nowrap><span class="shopping-cart-login-input-label"><lang:contentMessage value="E-mail address"/></span></td>
  </tr>
  <tr>
    <td nowrap>
      <html:text property="custEmail" size="30" styleClass="shopping-cart-login-input"/>
    </td>
  </tr>
  <tr> 
    <td nowrap>
      <span class="tran-error-normal">
        <lang:contentError field="custEmail"/>
      </span>
    </td>
  </tr>
  
  <tr> 
    <td nowrap><span class="shopping-cart-login-input-label"><lang:contentMessage value="Password"/></span></td>
  </tr>
  <tr>
    <td nowrap>
      <html:password property="custPassword" size="30" styleClass="shopping-cart-login-input"/>
    </td>
  </tr>
  <tr> 
    <td nowrap>
      <span class="tran-error-normal">
        <lang:contentError field="custPassword"/>
      </span>
    </td>
  </tr>
  <tr>
    <td nowrap>
      <span class="tran-error-normal">
        <lang:contentError field="login"/>
      </span>
    </td>
  </tr>
  <tr>
    <td id="shopping-cart-login-footer">
    <html:link href="javascript:submitForm('login');" styleClass="shopping-cart-button">
      <lang:contentMessage value="Sign-in"/>
    </html:link>
    &nbsp;
    <html:link page="/content/checkout/shoppingCartCancelCheckout.do?process=cancel&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}" styleClass="shopping-cart-button">
      <lang:contentMessage value="Cancel Checkout"/>
    </html:link>
    <br>
    <br>
    <html:link page="/myaccount/forgot/myAccountForgot.do?process=start&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}" styleClass="shopping-cart-button">
      <lang:contentMessage value="Forgot your password?"/>
    </html:link>
    &nbsp;
    <html:link page="/content/checkout/shoppingCartNewUser.do?process=start&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}" styleClass="shopping-cart-button">
      <lang:contentMessage key="content.text.newVisitorAccount"/>
    </html:link>
    </td>
  </tr>
</table>
</div>
</html:form>
</div>
<script type="text/javascript" language="JavaScript">
YAHOO.util.Event.onAvailable('step1', function() {
	var object = document.getElementById('step1');
	object.className = 'shopping-cart-steps-active';
	object = document.getElementById('step1_line');
	object.className = 'shopping-cart-steps-line-active';
} );
</script>