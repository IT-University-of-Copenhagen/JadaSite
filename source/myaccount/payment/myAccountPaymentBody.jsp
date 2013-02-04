<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<script type="text/javascript" language="JavaScript"><!--
function submitForm(type) {
    document.myAccountPaymentActionForm.process.value = type;
    document.myAccountPaymentActionForm.submit();
}

//--></script>
<jsp:useBean id="myAccountPaymentActionForm"  type="com.jada.myaccount.payment.MyAccountPaymentActionForm"  scope="request" />
<c:set var="myAccountBean" scope="request" value="${myAccountPaymentActionForm}"/>
<div id="my-account-container">
  <div id="my-account-nav-container">
  	<jsp:include page="/myaccount/myAccountNav.jsp" />
  </div>
  <div id="my-account-body-container">
    <html:form method="post" action="/myaccount/payment/myAccountPayment.do?process=update&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}">
    <html:hidden property="process" value=""/>
    <div id="my-account-header-container"><lang:contentMessage value="Payment information"/></div>
    <span id="my-account-message"><lang:contentError field="message"/></span>
    <div id="my-account-body-inner-container">
	<table cellspacing="0" cellpadding="3" class="my-account-table">
	  <tr>
	    <td colspan="2">
	    <span class="my-account-form-label"><lang:contentMessage key="content.text.myaccount.payment.message"/></span>
	    </td>
	  </tr>
	  <tr>
	    <td colspan="2">
	    <span class="my-account-form-title"><lang:contentMessage value="Name as it appears on card"/></span>
	    </td>
	  </tr>
	  <tr>
	    <td colspan="2">
	      <html:text property="custCreditCardFullName" size="20" styleClass="my-account-form-input"/>
	    </td>
	  </tr>
	  <tr>
	    <td colspan="2">
	      <span class="my-account-error">
	        <lang:contentError field="custCreditCardFullName"/>
	      </span>
	    </td>
	  </tr>
	  <tr>
	    <td colspan="2">
	    <span class="my-account-form-title"><lang:contentMessage value="Credit card type"/></span>
	    </td>
	  </tr>
	  <tr>
	    <td colspan="2">
	      <html:select property="creditCardId" styleClass="my-account-form-input"> 
	        <html:optionsCollection property="creditCards" label="label" value="value"/> 
	      </html:select> 
	    </td>
	  </tr>
	  <tr>
	    <td colspan="2">
	    <span class="my-account-form-title"><lang:contentMessage value="Card number (no spaces)"/></span>
	    </td>
	  </tr>
	  <tr>
	    <td colspan="2">
	      <html:text property="custCreditCardNum" size="20" styleClass="my-account-form-input"/>
	    </td>
	  </tr>
	  <tr>
	    <td colspan="2">
	      <span class="my-account-error">
	        <lang:contentError field="custCreditCardNum"/>
	      </span>
	    </td>
	  </tr>
	  <tr>
	    <td colspan="2">
	    <span class="my-account-form-title"><lang:contentMessage value="Expiration date"/></span>
	    </td>
	  </tr>
	  <tr>
	    <td nowrap>
	      <span class="my-account-form-title"><lang:contentMessage value="Month"/></span>
	      <html:select property="custCreditCardExpiryMonth" styleClass="my-account-form-input"> 
	        <html:optionsCollection property="expiryMonths" label="label" value="value"/> 
	      </html:select> 
	      <span class="my-account-form-title"><lang:contentMessage value="Year"/></span>
	      <html:select property="custCreditCardExpiryYear" styleClass="my-account-form-input"> 
	        <html:optionsCollection property="expiryYears" label="label" value="value"/> 
	      </html:select> 
	    </td>
	  </tr>
	  <tr>
	    <td colspan="2">
	    <span class="my-account-form-title"><lang:contentMessage value="Card Code Verification Number (CCV)"/></span>
	    </td>
	  </tr>
	  <tr>
	    <td colspan="2">
	      <html:text property="custCreditCardVerNum" size="10" styleClass="my-account-form-input"/>
	    </td>
	  </tr>
	</table>
	<br>
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