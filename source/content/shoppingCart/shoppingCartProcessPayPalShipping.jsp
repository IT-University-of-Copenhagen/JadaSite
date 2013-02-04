<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<html:form method="post" styleId="shoppingCartShippingForm" action="/content/checkout/shoppingCartProcess.do">
<html:hidden property="process" value="updatePayPalShipping"/> 
<html:hidden property="prefix" value="${contentBean.siteDomain.siteDomainPrefix}"/>
<c:if test="${shoppingCartForm.passwordEmpty}">
  <div class="shopping-cart-col1">
    <div class="shopping-cart-col11">
      <span class="shopping-cart-input-label"><lang:contentMessage value="Password"/>*</span>
    </div>
    <div class="shopping-cart-col11">
      <html:password property="custPassword" size="20" styleClass="shopping-cart-info-label"/>
      <span class="shopping-cart-error" id="msg-custPassword"></span>
    </div>
  </div>
  <div class="shopping-cart-col1">
    <div class="shopping-cart-col11">
      <span class="shopping-cart-input-label"><lang:contentMessage value="Verification Password"/>*</span>
    </div>
    <div class="shopping-cart-col11">
      <html:password property="custVerifyPassword" size="20" styleClass="shopping-cart-info-label"/>
      <span class="shopping-cart-error" id="msg-custVerifyPassword"></span>
    </div>
  </div>
  <div class="shopping-cart-col1">
    <div class="shopping-cart-col11">
      <span class="shopping-cart-input-label"><lang:contentMessage value="Public name"/>*</span>
    </div>
    <div class="shopping-cart-col11">
      <html:password property="custPublicName" size="20" styleClass="shopping-cart-info-label"/>
      <span class="shopping-cart-error" id="msg-custPublicName"></span>
    </div>
  </div>
</c:if>
<div class="shopping-cart-col1">
  <div class="shopping-cart-col11">
    <span class="shopping-cart-info-title"><lang:contentMessage value="Shipping method"/></span>
  </div>
  <div class="shopping-cart-col11">
    <select id="shippingMethodId" name="shippingMethodId" class="shopping-cart-input" onchange="javascript:submitForm('recalculate')">
    </select>
  </div>
</div>
<div class="shopping-cart-col-large">
  <html:link href="" onclick="return submitPayPalShipping();" styleClass="shopping-cart-button">
    <lang:contentMessage value="Update and Continue"/>
  </html:link>
</div>
</html:form>
