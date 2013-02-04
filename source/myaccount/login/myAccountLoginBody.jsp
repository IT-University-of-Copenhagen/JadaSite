<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<script type="text/javascript" language="JavaScript"><!--
function submitForm(type) {
    document.myAccountLoginActionForm.process.value = type;
    document.myAccountLoginActionForm.submit();
}

//--></script>
<c:set var="myAccountLoginActionForm" scope="request" value="${myAccountLoginActionForm}"/>
<div id="my-account-container">
<div id="my-account-public-header-container">
  <span id="my-account-public-header"><lang:contentMessage value="Sign-in"/></span>
</div>
<br>
<html:form method="post" action="/myaccount/login/myAccountLogin.do?process=login&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}">
<span class="my-account-public-message"><lang:contentError field="msg"/></span>
<span class="my-account-public-message">
  <lang:contentMessage value="To sign in, please enter your email address and password below."/>
</span>
<br>
<br>
<html:hidden property="process" value=""/>
<table border="0" cellspacing="0" cellpadding="0" id="my-account-public-table" width="400">
  <tr id="my-account-public-table-header">
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td nowrap>&nbsp;</td>
  </tr>
  <tr> 
    <td nowrap><span class="my-account-public-label"><lang:contentMessage value="Email address"/></span></td>
  </tr>
  <tr> 
    <td nowrap>
      <html:hidden property="target"/>
      <html:hidden property="url"/>
      <html:hidden property="orderNum"/>
      <html:hidden property="others"/>
      <html:text property="custEmail" size="30" styleClass="my-account-public-input"/>
      <span class="my-account-public-error"><lang:contentError field="custEmail"/></span>
    </td>
  </tr>
  <tr> 
    <td nowrap><span class="my-account-public-value"><lang:contentMessage value="Password"/></span></td>
  </tr>
  <tr> 
    <td>
      <html:password property="custPassword" size="30" styleClass="my-account-public-input"/>
      <span class="my-account-public-error"><lang:contentError field="custPassword"/></span>
    </td>
  </tr>
  <tr> 
    <td nowrap>
    </td>
  </tr>
  <tr> 
    <td>
      <span class="my-account-public-error"><lang:contentError field="login"/></span>
    </td>
  </tr>
  <tr> 
    <td nowrap>
    </td>
  </tr>
  <tr> 
    <td nowrap id="my-account-public-table-footer">
      <html:link href="javascript:submitForm('login');" styleClass="my-account-public-button">
        <lang:contentMessage value="Sign-in"/>
      </html:link>
      <br>
      <br>
      <html:link page="/myaccount/forgot/myAccountForgot.do?process=start&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}" styleClass="my-account-public-button"><lang:contentMessage value="Forgot your password?"/></html:link>
      &nbsp;
      <html:link page="/myaccount/register/myAccountRegister.do?process=start&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}" styleClass="my-account-public-button">
        <lang:contentMessage key="content.text.newVisitorAccount"/>
      </html:link>
    </td>
  </tr>
</table>
</html:form>
<br>
</div>
