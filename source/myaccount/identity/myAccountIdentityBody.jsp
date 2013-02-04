<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<script type="text/javascript" language="JavaScript"><!--
function submitForm(type) {
    document.myAccountIdentityActionForm.process.value = type;
    document.myAccountIdentityActionForm.submit();
}

function selectShipping(value) {
	var container = document.getElementById('shippingContainer');
	if (value == 'O') {
		container.style.display = 'block';
	}
	else {
		container.style.display = 'none';
	}
}

function selectBilling(value) {
	var container = document.getElementById('billingContainer');
	if (value == 'O') {
		container.style.display = 'block';
	}
	else {
		container.style.display = 'none';
	}
}

YAHOO.util.Event.onContentReady('billingContainer', function() {
	var value = document.shoppingCartActionForm.billingUseAddress.value;
	if (value == 'O') {
		document.getElementById('billingContainer').style.display = 'block';
	}
} );

YAHOO.util.Event.onContentReady('shippingContainer', function() {
	var value = document.shoppingCartActionForm.shippingUseAddress.value;
	if (value == 'O') {
		document.getElementById('shippingContainer').style.display = 'block';
	}
} );

//--></script>
<jsp:useBean id="myAccountIdentityActionForm"  type="com.jada.myaccount.identity.MyAccountIdentityActionForm"  scope="request" />
<c:set var="myAccountBean" scope="request" value="${myAccountIdentityActionForm}"/>
<div id="my-account-container">
  <div id="my-account-nav-container">
  	<jsp:include page="/myaccount/myAccountNav.jsp" />
  </div>
  <div id="my-account-body-container">
    <html:form method="post" action="/myaccount/identity/myAccountIdentity.do?process=update&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}">
    <html:hidden property="process" value=""/>
    <div id="my-account-header-container"><lang:contentMessage value="Email and password"/></div>
    <span id="my-account-message"><lang:contentError field="message"/></span>
    <div id="my-account-body-inner-container">
      <table cellspacing="0" cellpadding="0" class="my-account-table">
        <tr> 
          <td nowrap><span class="my-account-form-title"><lang:contentMessage value="Email address (login name)"/></span><br>
          </td>
        </tr>
        <tr>
          <td>
            <span class="my-account-form-value">
            ${myAccountIdentityActionForm.custEmail}
            <html:hidden property="custEmail"/>
            </span>
          </td>
        </tr>
        <tr> 
          <td nowrap><span class="my-account-form-title"><lang:contentMessage value="Public name"/></span><br>
          <span class="my-account-form-label"><lang:contentMessage value="Name that will be known to public"/></span>
          </td>
        </tr>
        <tr>
          <td>
            <html:text property="custPublicName" size="20" maxlength="20" styleClass="my-account-form-input"/>
            <span class=my-account-error>
              <lang:contentError field="custPublicName"/>
            </span>
          </td>
        </tr>
        <tr> 
          <td><span class="my-account-form-title"><lang:contentMessage value="Password"/></span><br>
          <span class="my-account-form-label"><lang:contentMessage key="content.text.myaccount.identity.password.desc"/></span>
          </td>
        </tr>
        <tr>
          <td>
            <html:password property="custPassword" size="20" maxlength="20" styleClass="my-account-form-input"/>
            <span class=my-account-error>
              <lang:contentError field="custPassword"/>
            </span>
          </td>
        </tr>
        <tr> 
          <td nowrap><span class="my-account-form-title"><lang:contentMessage value="Re-enter password"/></span></td>
        </tr>
        <tr>
          <td>
            <html:password property="custPassword1" size="20" maxlength="20" styleClass="my-account-form-input"/>
            <span class="my-account-error">
              <lang:contentError field="custPassword1"/>
            </span>
          </td>
        </tr>
        <tr> 
          <td>
            <span class="my-account-error">
              <lang:contentError field="custPasswordNoMatch"/>
            </span>
          </td>
        </tr>
      </table>
      <table width="200" cellspacing="0" cellpadding="0" class="my-account-table">
        <tr>
          <td>
            <html:link href="javascript:submitForm('update');" styleClass="my-account-submit">
              <lang:contentMessage value="Update"/>
            </html:link>
          </td>
        </tr>
      </table>
    </div>
    </html:form>
  </div>
</div>