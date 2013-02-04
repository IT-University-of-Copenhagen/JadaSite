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
<div id="shopping-cart-container">
<c:set var="shoppingCartForm" scope="request" value="${shoppingCartActionForm}"/>
<html:form method="post" action="/content/checkout/regular/shoppingCartCreditCard.do?prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}">
<html:hidden property="process" value=""/> 
<%@ include file="/content/shoppingCart/shoppingCartSteps.jsp" %>
<br>
<div id="shopping-cart-creditcard-title-container">
  <span id="shopping-cart-creditcard-title"><lang:contentMessage value="Credit Card Information"/></span>
</div>
<div id="shopping-cart-creditcard-container">
<table width="800" border="0" cellpadding="3" class="shopping-cart-info-table">
  <tr> 
    <td nowrap>
      <span class="shopping-cart-info-value"><c:out value='${shoppingCartForm.paymentMessage}'/></span>
    </td>
  </tr>
  <tr>
    <td valign="top"><span class="shopping-cart-info-label"><lang:contentMessage value="Name as it appears on the card"/>*</span></td>
  </tr>
  <tr>
    <td valign="top">
      <html:text property="custCreditCardFullName" size="40" styleClass="shopping-cart-input"/>
      <logic:messagesPresent property="custCreditCardFullName" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="custCreditCardFullName"/></span>
      </logic:messagesPresent>
    </td>
  </tr>
  <tr>
    <td valign="top"><span class="shopping-cart-info-label"><lang:contentMessage value="Credit card type"/>*</span></td>
  </tr>
  <tr>
    <td valign="top">
      <html:select property="creditCardId" styleClass="shopping-cart-input"> 
        <html:optionsCollection property="creditCards" label="label" value="value"/> 
      </html:select>
    </td>
  </tr>
  <tr>
    <td valign="top"><span class="shopping-cart-info-label"><lang:contentMessage value="Card number (no spaces)"/>*</span></td>
  </tr>
  <tr>
    <td valign="top">
      <html:text property="custCreditCardNum" size="20" styleClass="shopping-cart-input"/>
      <span class="shopping-cart-error">
        <lang:contentError field="custCreditCardNum"/>
      </span>
    </td>
  </tr>
  <tr>
    <td valign="top"><span class="shopping-cart-info-label"><lang:contentMessage value="Expiration date"/>*</span></td>
  </tr>
  <tr>
    <td valign="top">
      <html:select property="custCreditCardExpiryMonth" styleClass="shopping-cart-input" style="width: 50px"> 
        <html:optionsCollection property="expiryMonths" label="label" value="value"/> 
      </html:select>
      <html:select property="custCreditCardExpiryYear" styleClass="shopping-cart-input" style="width: 70px"> 
        <html:optionsCollection property="expiryYears" label="label" value="value"/> 
      </html:select>   
    </td>
  </tr>
  <tr>
    <td valign="top"><span class="shopping-cart-info-label"><lang:contentMessage value="Card code verification number (CCV)"/>*</span></td>
  </tr>
  <tr>
    <td valign="top">
      <html:text property="custCreditCardVerNum" size="10" styleClass="shopping-cart-input"/>
    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td>
      <html:link page="/content/checkout/shoppingCartCancelCheckout.do?process=cancel&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}" styleClass="shopping-cart-continue-button">
        <lang:contentMessage value="Continue Shopping"/>
      </html:link>
      &nbsp;
      <html:link href="javascript:submitForm('update');" styleClass="shopping-cart-continue-button">
        <lang:contentMessage value="Make Payment"/>
      </html:link>
    </td>
  </tr>
</table>
</div>
<br>
<div id="shopping-cart-footer-container">
  <c:out value='${shoppingCartActionForm.shoppingCartMessage}' escapeXml='false'/>
</div>
</html:form>
</div>
<script type="text/javascript" language="JavaScript">
YAHOO.util.Event.onAvailable('step1', function() {
	var object = document.getElementById('step4');
	object.className = 'shopping-cart-steps-active';
	object = document.getElementById('step4_line');
	object.className = 'shopping-cart-steps-line-active';
} );
</script>