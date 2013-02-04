<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<div id="shopping-cart-login-option-container" style="float: left; margin-right: 10px; width: 250px; padding: 10px">
<html:link href="" onclick="$('#shopping-cart-new-user-container').toggle(); return false;" styleClass="shopping-cart-button">
  <lang:contentMessage key="content.text.newVisitorAccount"/>
</html:link>
<br></br>
<div id="shopping-cart-new-user-container" class="shopping-cart-panel-disable">
<html:form method="post" styleId="shoppingCartNewUserForm" action="/content/checkout/shoppingCartProcess.do?prefix=${contentBean.siteDomain.siteDomainPrefix}">
  <html:hidden property="process" value="newUser"/>
  <div class="shopping-cart-col11"><span class="shopping-cart-newuser-input-label"><lang:contentMessage value="Email address (login name)"/></span></div>
  <div class="shopping-cart-col11"><html:text property="custEmail" size="40" styleClass="shopping-cart-newuser-input"/></div>
  <span class="shopping-cart-error" id="msg-custEmail"></span>
  <span class="shopping-cart-error" id="msg-emailDuplicate"></span>
  <div class="shopping-cart-col11"><span class="shopping-cart-newuser-input-label"><lang:contentMessage value="Re-enter Email address"/></span></div>
  <div class="shopping-cart-col11"><html:text property="custEmail1" size="40" styleClass="shopping-cart-newuser-input"/></div>
  <span class="shopping-cart-error" id="msg-custEmail1"></span>
  <span class="shopping-cart-error" id="msg-custEmailNoMatch"></span>
  <div class="shopping-cart-col11"><span class="shopping-cart-newuser-input-label">Public Name</span><br></div>
  <div class="shopping-cart-col11">
  <span class="shopping-cart-newuser-text">
    Name that will be known to the public
  </span>
  </div>
  <div class="shopping-cart-col11"><html:text property="custPublicName" size="20" styleClass="shopping-cart-newuser-input"/></div>
  <span class="shopping-cart-error" id="msg-custPublicName"></span>
  <div class="shopping-cart-col11">
  <span class="shopping-cart-newuser-input-label"><lang:contentMessage value="Password"/></span><br>
  <span class="shopping-cart-newuser-text">
    <lang:contentMessage key="content.text.passwordMessage"/>
  </span>
  </div>
  <div class="shopping-cart-col11">
    <html:password property="custPassword" size="30" styleClass="shopping-cart-newuser-input"/>
    <span class="shopping-cart-error" id="msg-custPassword"></span>
  </div>
  <div class="shopping-cart-col11"><div class="shopping-cart-newuser-input-label"><lang:contentMessage value="Re-enter Password"/></div></div>
  <div class="shopping-cart-col11">
    <html:password property="custVerifyPassword" size="30" styleClass="shopping-cart-newuser-input"/>
    <span class="shopping-cart-error" id="msg-custVerifyPassword"></span>
    <span class="shopping-cart-error" id="msg-custPasswordNoMatch"></span>
  </div>
  <div class="shopping-cart-col11">
  <a href="" onclick="return submitNewUser();" class="shopping-cart-button"><lang:contentMessage value="Register New User"/></a>&nbsp;&nbsp;
  </div>
  <br><br>
</html:form>
</div>
<html:link page="/content/checkout/shoppingCartCancelCheckout.do?process=cancel&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}" styleClass="shopping-cart-button">
  <lang:contentMessage value="Cancel Checkout"/>
</html:link>
</div>
<html:form method="post" styleId="shoppingCartLoginForm" action="/content/checkout/shoppingCartProcess.do?process=login&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}">
<html:hidden property="process" value="login"/>
<html:hidden property="cash"/>
<div id="shopping-cart-login-container" style="float: left; width: 200px">
	<div class="shopping-cart-col11"><span class="shopping-cart-login-input-label"><lang:contentMessage value="E-mail address"/></span></div>
	<div class="shopping-cart-col11"><html:text property="custEmail" size="30" styleClass="shopping-cart-login-input"/></div>
	<div class="shopping-cart-col11"><span class="tran-error-normal"><lang:contentError field="msg-custEmail"/></span></div>
	<div class="shopping-cart-col11"><span class="shopping-cart-login-input-label"><lang:contentMessage value="Password"/></span></div>
	<div class="shopping-cart-col11"><html:password property="custPassword" size="30" styleClass="shopping-cart-login-input"/></div><br>
	<div class="shopping-cart-col11"><span class="tran-error-normal" id="msg-custPassword"></span></div>
	<div class="shopping-cart-col11"><span class="tran-error-normal" id="msg-login"></span></div>
	<div class="shopping-cart-col11">
	<html:link href="javascript:login();" styleClass="shopping-cart-button">
	  <lang:contentMessage value="Sign-in"/>
	</html:link>
	<html:link page="/myaccount/forgot/myAccountForgot.do?process=start&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}" styleClass="shopping-cart-button">
	  <lang:contentMessage value="Forgot your password?"/>
	</html:link>
	</div>
</div>
</html:form>
<div class="container-reset"></div>
<br></br>
