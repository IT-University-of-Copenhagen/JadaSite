<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:form method="post" styleId="shoppingCartCreditCardForm" action="/content/checkout/shoppingCartProcess.do?prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}">
<html:hidden property="process" value="updatePayment"/> 
<span class="shopping-cart-info-value" id="msg-paymentMessage">Here is the message</span>
<div class="shopping-cart-review-address-col1">
  <div class="shopping-cart-review-address-col11">
    <span class="shopping-cart-info-label"><lang:contentMessage value="Name as it appears on the card"/>*</span>
  </div>
  <div class="shopping-cart-review-address-col11">
    <html:text property="custCreditCardFullName" size="40" styleClass="shopping-cart-input"/>
    <span class="shopping-cart-error"><br><lang:contentError field="custCreditCardFullName"/></span>
  </div>
</div>
<br>
<div class="shopping-cart-review-address-col1">
  <div class="shopping-cart-review-address-col11">
    <span class="shopping-cart-info-label"><lang:contentMessage value="Credit card type"/>*</span>
  </div>
  <div class="shopping-cart-review-address-col11">
    <html:select property="creditCardId" styleClass="shopping-cart-input"> 
      <html:optionsCollection property="creditCards" label="label" value="value"/> 
    </html:select>
  </div>
</div>
<div class="shopping-cart-review-address-col1">
  <div class="shopping-cart-review-address-col11">
    <span class="shopping-cart-info-label"><lang:contentMessage value="Card number (no spaces)"/>*</span>
  </div>
  <div class="shopping-cart-review-address-col11">
    <html:text property="custCreditCardNum" size="20" styleClass="shopping-cart-input"/>
    <span class="shopping-cart-error"><lang:contentError field="custCreditCardNum"/></span>
  </div>
</div>
<div class="shopping-cart-review-address-col1">
  <div class="shopping-cart-review-address-col11">
    <span class="shopping-cart-info-label"><lang:contentMessage value="Expiration date"/>*</span>       
  </div>
  <div class="shopping-cart-review-address-col11">
    <html:select property="custCreditCardExpiryMonth" styleClass="shopping-cart-input" style="width: 50px"> 
      <html:optionsCollection property="expiryMonths" label="label" value="value"/> 
    </html:select>
    <html:select property="custCreditCardExpiryYear" styleClass="shopping-cart-input" style="width: 70px"> 
      <html:optionsCollection property="expiryYears" label="label" value="value"/> 
    </html:select>   
  </div>
</div>
<div class="shopping-cart-review-address-col1">
  <div class="shopping-cart-review-address-col11">
    <span class="shopping-cart-info-label"><lang:contentMessage value="Card code verification number (CCV)"/>*</span>
  </div>
  <div class="shopping-cart-review-address-col11">
    <html:text property="custCreditCardVerNum" size="10" styleClass="shopping-cart-input"/>  
  </div>
</div>
<div class="shopping-cart-col-large">
  <html:link href="" onclick="return submitPayment();" styleClass="shopping-cart-button">
    <lang:contentMessage value="Make Payment"/>
  </html:link>
</div>
</html:form>
