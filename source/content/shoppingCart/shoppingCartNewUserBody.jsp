<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<script type="text/javascript" language="JavaScript"><!--
function submitForm(type) {
    document.shoppingCartNewUserActionForm.process.value = type;
    document.shoppingCartNewUserActionForm.submit();
}

//--></script>
<c:set var="shoppingCartNewUserActionForm" scope="request" value="${shoppingCartNewUserActionForm}"/>
<%@ include file="/content/shoppingCart/shoppingCartSteps.jsp" %>
<div id="shopping-cart-container">
<br>
<span id="shopping-cart-newuser-title"><lang:contentMessage value="New User"/></span>
<br>
<br>
<html:form method="post" action="/content/checkout/shoppingCartNewUser.do?prefix=${contentBean.siteDomain.siteDomainPrefix}">
<html:hidden property="process" value=""/> 
<table width="500" cellspacing="0" cellpadding="0" id="shopping-cart-newuser-table">
  <tr id="shopping-cart-newuser-header">
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td nowrap>&nbsp;</td>
  </tr>
  <tr> 
    <td nowrap><span class="shopping-cart-newuser-input-label"><lang:contentMessage value="Email address (login name)"/></span></td>
  </tr>
  <tr>
    <td>
      <html:text property="custEmail" size="40" styleClass="shopping-cart-newuser-input"/>
    </td>
  </tr>
  <tr>
    <td>
      <span class="tran-error-normal">
        <lang:contentError field="custEmail"/>
      </span>
    </td>
  </tr>
  <tr> 
    <td nowrap><span class="shopping-cart-newuser-input-label"><lang:contentMessage value="Re-enter Email address"/></span></td>
  </tr>
  <tr>
    <td>
      <html:text property="custEmail1" size="40" styleClass="shopping-cart-newuser-input"/>
    </td>
  </tr>
  <tr>
    <td>
      <span class="tran-error-normal">
        <lang:contentError field="custEmail1"/>
      </span>
    </td>
  </tr>
  <tr> 
    <td nowrap>
      <span class="tran-error-normal">
        <lang:contentError field="custEmailNoMatch"/>
      </span>
    </td>
  </tr>
  <tr> 
    <td>
      <span class="shopping-cart-newuser-input-label">Public Name</span><br>
      <span class="shopping-cart-newuser-text">
      Name that will be known to the public
	  </span>
    </td>
  </tr>
  <tr>
    <td>
      <html:text property="custPublicName" size="20" styleClass="shopping-cart-newuser-input"/>
    </td>
  </tr>
  <tr>
    <td>
      <span class="tran-error-normal">
        <lang:contentError field="custPublicName"/>
      </span>
    </td>
  </tr>
  <tr> 
    <td>
      <span class="shopping-cart-newuser-input-label"><lang:contentMessage value="Password"/></span><br>
      <span class="shopping-cart-newuser-text">
      <lang:contentMessage key="content.text.passwordMessage"/>
	  </span>
    </td>
  </tr>
  <tr>
    <td>
      <html:password property="custPassword" size="30" styleClass="shopping-cart-newuser-input"/>
    </td>
  </tr>
  <tr>
    <td>
      <span class="tran-error-normal">
        <lang:contentError field="custPassword"/>
      </span>
    </td>
  </tr>
  <tr> 
    <td nowrap><div class="shopping-cart-newuser-input-label"><lang:contentMessage value="Re-enter Password"/></div></td>
  </tr>
  <tr>
    <td>
      <html:password property="custPassword1" size="30" styleClass="shopping-cart-newuser-input"/>
    </td>
  </tr>
  <tr>
    <td>
      <span class="tran-error-normal">
        <lang:contentError field="custPassword1"/>
      </span>
    </td>
  </tr>
  <tr> 
    <td>
      <span class="tran-error-normal">
        <lang:contentError field="login"/>
      </span>
    </td>
  </tr>
  <tr> 
    <td>
      <span class="tran-error-normal">
        <lang:contentError field="custPasswordNoMatch"/>
      </span>
    </td>
  </tr>
  <tr> 
    <td>
      <span class="tran-error-normal">
        <lang:contentError field="emailDuplicate"/>
      </span>
    </td>
  </tr>
  <tr> 
    <td id="shopping-cart-newuser-footer">
      <a href="javascript:submitForm('create');" class="shopping-cart-button"><lang:contentMessage value="Register New User"/></a>&nbsp;&nbsp;
      <html:link page="/myaccount/forgot/myAccountForgot.do?process=start&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}" styleClass="shopping-cart-button"><lang:contentMessage value="Forgot your password?"/></html:link>
      <br><br>
      <html:link page="/content/checkout/shoppingCartCancelCheckout.do?process=cancel&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}" styleClass="shopping-cart-button">
        <lang:contentMessage value="Cancel Checkout"/>
      </html:link>
    </td>
  </tr>
</table>
<br>
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