<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<script type="text/javascript" language="JavaScript"><!--
function submitForm(type) {
    document.shoppingCartActionForm.process.value = type;
    document.shoppingCartActionForm.submit();
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
function submitCashFinalizeForm(methodType) {
    document.shoppingCartActionForm.action = '/${contentBean.contextPath}/content/checkout/shoppingCartCashFinalize.do';
    document.shoppingCartActionForm.process.value = methodType;
    document.shoppingCartActionForm.submit();
    return false;
}
function submitFinalizeForm(methodType) {
    document.shoppingCartActionForm.action = '/${contentBean.contextPath}/content/checkout/paypal/shoppingCartPayPalFinalize.do';
    document.shoppingCartActionForm.process.value = methodType;
    document.shoppingCartActionForm.submit();
    return false;
}
function submitCreditCardForm(methodType) {
    document.shoppingCartActionForm.action = '/${contentBean.contextPath}/content/checkout/regular/shoppingCartCreditCard.do';
    document.shoppingCartActionForm.process.value = methodType;
    document.shoppingCartActionForm.submit();
    return false;
}
function submitPayPalProHostedCreditCardForm(methodType) {
    document.shoppingCartActionForm.action = '/${contentBean.contextPath}/content/checkout/paypalprohosted/shoppingCartPayPalProHostedCreditCard.do';
    document.shoppingCartActionForm.process.value = methodType;
    document.shoppingCartActionForm.submit();
    return false;
}
function submitRemoveItemForm(type, itemNaturalKey) {
    document.shoppingCartActionForm.process.value = type;
    document.shoppingCartActionForm.itemNaturalKey.value = itemNaturalKey;
    document.shoppingCartActionForm.submit();
}
function submitRemoveCouponForm(type, couponId) {
    document.shoppingCartActionForm.process.value = type;
    document.shoppingCartActionForm.couponId.value = couponId;
    document.shoppingCartActionForm.submit();
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
<div id="shopping-cart-container">
<c:set var="shoppingCartForm" scope="request" value="${shoppingCartActionForm}"/>
<html:form method="post" action="/content/checkout/shoppingCartReviewOrder.do">
<html:hidden property="process" value=""/>
<html:hidden property="prefix" value="${contentBean.siteDomain.siteDomainPrefix}"/>
<html:hidden property="couponId" value=""/>
<html:hidden property="itemNaturalKey" value=""/>
<html:hidden property="paymentGatewayProvider" value=""/>
<%@ include file="/content/shoppingCart/shoppingCartSteps.jsp" %>
<br><br>
<div id="shopping-cart-info-container">
<table border="0" cellpadding="0" class="shopping-cart-info-table">
  <tr>
    <td width="150">
      <span class="shopping-cart-info-title"><lang:contentMessage value="My information"/></span>
    </td>
    <td>
      <c:if test="${shoppingCartActionForm.paymentGatewayProvider != 'PayPalEngine'}">
      <c:if test="${!shoppingCartForm.shippingQuoteLock}">
      <html:link href="/${contentBean.contextPath}/content/checkout/regular/shoppingCartReviewAddress.do?process=start&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}" styleClass="shopping-cart-edit-link">
        <lang:contentMessage value="edit"/>
      </html:link>
      </c:if>
      </c:if>
    </td>
  </tr>
  <tr>
    <td width="150"><span class="shopping-cart-info-label"><lang:contentMessage value="Email address"/></span></td>
    <td>
    <html:hidden property="custEmail"/>
    <span class="shopping-cart-info-value">${shoppingCartForm.custEmail}</span>
    </td>
  </tr>
  <c:if test="${shoppingCartForm.passwordEmpty}">
  <tr>
    <td width="150"><span class="shopping-cart-info-label"><lang:contentMessage value="Password"/>*</span></td>
    <td>
    <html:password property="custPassword" size="20" styleClass="shopping-cart-info-label"/>
    </td>
  </tr>
  <tr>
    <td width="150"><span class="shopping-cart-info-label"><lang:contentMessage value="Verification Password"/>*</span></td>
    <td>
    <html:password property="custVerifyPassword" size="20" styleClass="shopping-cart-info-label"/>
    </td>
  </tr>
  <tr>
    <td width="150"></td>
    <td class="tran-error-normal">
      <lang:contentError field="custPassword"/>
    </td>
  </tr>
  <tr>
    <td width="150"><span class="shopping-cart-info-label"><lang:contentMessage value="Public name"/>*</span></td>
    <td>
    <html:text property="custPublicName" size="20" styleClass="shopping-cart-info-value"/>
    </td>
  </tr>
  <tr>
    <td width="150"></td>
    <td class="tran-error-normal">
      <lang:contentError field="custPublicName"/>
    </td>
  </tr>
  </c:if>
  <tr>
    <td width="100" valign="top"><span class="shopping-cart-info-label"><lang:contentMessage value="Address"/></span></td>
    <td valign="top">
    <span class="shopping-cart-info-value">
    ${shoppingCartForm.custFirstName} ${shoppingCartForm.custLastName}<br>
    ${shoppingCartForm.custAddressLine1}<br>
    <c:if test="${not empty (shoppingCartForm.custAddressLine2)}">
      ${shoppingCartForm.custAddressLine2}<br>
    </c:if>
    ${shoppingCartForm.custCityName} ${shoppingCartForm.custStateName}<br>
    ${shoppingCartForm.custCountryName}<br>
    ${shoppingCartForm.custZipCode}
    </span>
    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td></td>
  </tr>
  <tr>
    <td width="150">
      <span class="shopping-cart-info-title"><lang:contentMessage value="Billing information"/></span>
    </td>
    <td>
      <c:if test="${shoppingCartActionForm.paymentGatewayProvider != 'PayPalEngine'}">
      <c:if test="${!shoppingCartForm.shippingQuoteLock}">
      <html:link href="/${contentBean.contextPath}/content/checkout/regular/shoppingCartReviewAddress.do?process=start&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}" styleClass="shopping-cart-edit-link">
        <lang:contentMessage value="edit"/>
      </html:link>
      </c:if>
      </c:if>
    </td>
  </tr>
  <tr>
    <td>
      <c:choose>
        <c:when test="${shoppingCartForm.billingUseAddress == 'C'}">
        <span class="shopping-cart-info-label"><lang:contentMessage value="Same as my information"/></span>
        </c:when>
      </c:choose>
    </td>
    <td>
    </td>
  </tr>
  <c:if test="${shoppingCartForm.billingUseAddress == 'O'}">
  <tr>
    <td width="150" valign="top"><span class="shopping-cart-info-label"><lang:contentMessage value="Address"/></span></td>
    <td valign="top">
    <span class="shopping-cart-info-value">
    ${shoppingCartForm.billingCustFirstName} ${shoppingCartForm.billingCustLastName}<br>
    ${shoppingCartForm.billingCustAddressLine1}<br>
    <c:if test="${not empty (shoppingCartForm.billingCustAddressLine2)}">
      ${shoppingCartForm.billingCustAddressLine2}<br>
    </c:if>
    ${shoppingCartForm.billingCustCityName} ${shoppingCartForm.billingCustStateName}<br>
    ${shoppingCartForm.billingCustCountryName}<br>
    ${shoppingCartForm.billingCustZipCode}
    </span>
    </td>
  </tr>
  </c:if>
  <tr>
    <td width="150">&nbsp;</td>
    <td></td>
  </tr>
  <tr>
    <td width="150">
      <span class="shopping-cart-info-title"><lang:contentMessage value="Shipping information"/></span>
    </td>
    <td>
      <c:if test="${shoppingCartActionForm.paymentGatewayProvider != 'PayPalEngine'}">
      <c:if test="${!shoppingCartForm.shippingQuoteLock}">
      <html:link href="/${contentBean.contextPath}/content/checkout/regular/shoppingCartReviewAddress.do?process=start&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}" styleClass="shopping-cart-edit-link">
        <lang:contentMessage value="edit"/>
      </html:link>
      </c:if>
      </c:if>
    </td>
  </tr>
  <tr>
    <td>
      <c:choose>
        <c:when test="${shoppingCartForm.shippingUseAddress == 'C'}">
          <span class="shopping-cart-info-value"><lang:contentMessage value="Same as my information"/></span>
        </c:when>
        <c:when test="${shoppingCartForm.shippingUseAddress == 'B'}">
          <span class="shopping-cart-info-value"><lang:contentMessage value="Same as billing information"/></span>
        </c:when>
      </c:choose>
    </td>
    <td>
    </td>
  </tr>
  <c:if test="${shoppingCartForm.shippingUseAddress == 'O'}">
  <tr>
    <td width="150" valign="top"><span class="shopping-cart-info-label"><lang:contentMessage value="Address"/></span></td>
    <td valign="top">
    <span class="shopping-cart-info-value">
    ${shoppingCartForm.shippingCustFirstName} ${shoppingCartForm.shippingCustLastName}<br>
    ${shoppingCartForm.shippingCustAddressLine1}<br>
    <c:if test="${not empty (shoppingCartForm.shippingCustAddressLine2)}">
      ${shoppingCartForm.shippingCustAddressLine2}<br>
    </c:if>
    ${shoppingCartForm.shippingCustCityName} ${shoppingCartForm.shippingCustStateName}<br>
    ${shoppingCartForm.shippingCustCountryName}<br>
    ${shoppingCartForm.shippingCustZipCode}
    </span>
    </td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  </c:if>
</table>






<c:if test="${!shoppingCartForm.shippingQuoteLock}">
<br>
<table width="600" border="0" cellpadding="3" class="shopping-cart-info-table">
  <tr>
    <td><span class="shopping-cart-info-title"><lang:contentMessage value="Shipping method"/></span></td>
  </tr>
  <tr>
    <td>
      <html:select property="shippingMethodId" styleClass="shopping-cart-input" onchange="javascript:submitForm('recalculate')"> 
        <html:optionsCollection property="shippingMethods" value="value" label="label"/>
        <c:if test="${shoppingCartForm.includeShippingPickUp}">
        <html:option value="PU"><lang:contentMessage value="Pick up"/></html:option>
        </c:if>
      </html:select>
      <c:if test="${shoppingCartForm.allowShippingQuote}">
      <html:link href="javascript:submitForm('shippingQuote')" styleClass="shopping-cart-quote-button">
        <lang:contentMessage value="Request for shipping quote"/>
      </html:link>
      </c:if>
	</td>
  </tr>
  <tr>
    <td><span class="tran-error-normal"><lang:contentError field="shippingLocation"/></span></td>
  </tr>
</table>
</c:if>
<c:if test="${!shoppingCartForm.shippingQuoteLock}">
<br>
<table width="600" border="0" cellpadding="3" class="shopping-cart-info-table">
  <tr>
    <td><span class="shopping-cart-info-title"><lang:contentMessage value="Coupon"/></span></td>
  </tr>
  <tr>
    <td>
      <span class="shopping-cart-info-label"><html:text property="couponCode" size="20"/></span>
      <html:link href="javascript:submitForm('applyCoupon')" styleClass="shopping-cart-coupon-button">
	    <lang:contentMessage value="Apply Coupon"/>
	  </html:link></td>
  </tr>
  <tr>
    <td>
      <span class="tran-error-normal">
      <lang:contentError field="couponCode"/>
      </span>
    </td>
  </tr>
</table>
</c:if>
</div>
<br>






<div id="shopping-cart-detail-container">
<div id="shopping-cart-detail-header-container"><lang:contentMessage value="Item details"/></div>
<table width="100%" border="0" cellpadding="0" id="shopping-cart-detail-table">
  <tr>
    <td align="right">
  	  <html:link href="/${contentBean.contextPath}/content/checkout/shoppingCartCancelCheckout.do?process=cancel&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}" styleClass="shopping-cart-continue-button">
        <lang:contentMessage value="Continue Shopping"/>
      </html:link>
    </td>
  </tr>
  <c:forEach var="shoppingCartItemInfo" items="${shoppingCartActionForm.shoppingCartItemInfos}">
  <tr>
    <td><hr id="shopping-cart-detail-seperator"></td>
  </tr>
  <tr>
    <td>
      <table width="100%" border="0" cellpadding="0" id="shopping-cart-detail-item-table">
        <tr>
          <td width="100">
            <div id="shopping-cart-detail-image-container">
              <c:if test="${shoppingCartItemInfo.imageId != null}">
                <img src="/${contentBean.contextPath}/services/ImageProvider.do?type=I&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}&imageId=${shoppingCartItemInfo.imageId}&maxsize=75">
              </c:if>
            </div>
          </td>
          <td valign="top">
            <table width="100%" border="0" cellpadding="0" id="shopping-cart-detail-item-desc-table">
              <tr>
                <td>
                  <span id="shopping-cart-detail-item-desc">${shoppingCartItemInfo.itemShortDesc}</span><br>
                  <c:forEach var="shoppingCartItemAttribute" items="${shoppingCartItemInfo.shoppingCartItemAttributes}">
                  <span id="shopping-cart-detail-item-attrib">${shoppingCartItemAttribute.customAttribDesc} - ${shoppingCartItemAttribute.customAttribValue}</span><br>
                  </c:forEach>
                </td>
                <td width="80" valign="top" align="right"><span id="shopping-cart-detail-item-price">${shoppingCartItemInfo.itemPrice}</span></td>
                <td width="80" valign="top" align="right">
                    <input type="hidden" name="itemNaturalKeys" value="${shoppingCartItemInfo.itemNaturalKey}">
                    <input name="itemQtys" type="text" class="shopping-cart-input" value="${shoppingCartItemInfo.itemQty}" size="2">
                    <span class="tran-error-normal">${shoppingCartItemInfo.itemQtyError}</span>
                </td>
                <td width="80" valign="top" align="right"><span id="shopping-cart-detail-item-subtotal">${shoppingCartItemInfo.itemSubTotal}</span></td>
              </tr>
              <c:if test="${!shoppingCartForm.shippingQuoteLock}">
              <tr>
                <td colspan="4">
                  <div align="right">
                  <html:link styleClass="shopping-cart-detail-item-remove" href="javascript:submitRemoveItemForm('removeItem', '${shoppingCartItemInfo.itemNaturalKey}')">
                    <lang:contentMessage value="Remove"/>
                  </html:link>
                  </div>
                </td>
              </tr>
              </c:if>
            </table>
          </td>
        </tr>

      </table>
    </td>
  </tr>
  </c:forEach>
  <c:forEach var="shoppingCartCouponInfo" items="${shoppingCartActionForm.shoppingCartCouponInfos}">
  <tr>
    <td><hr id="shopping-cart-detail-seperator"></td>
  </tr>
  <tr>
    <td>
      <table width="100%" border="0" cellpadding="0" id="shopping-cart-detail-other-table">
        <tr>
          <td><div id="shopping-cart-detail-image-container"></div></td>
          <td valign="top" width="100%">
            <table width="100%" border="0" cellpadding="0" id="shopping-cart-detail-other-desc-table">
              <tr>
                <td><span id="shopping-cart-detail-other-desc">${shoppingCartCouponInfo.couponCode}&nbsp;${shoppingCartCouponInfo.couponName}</span></td>
                <td><span id="shopping-cart-detail-other-price">${shoppingCartCouponInfo.couponAmount}</span></td>
              </tr>
              <tr>
                <td colspan="2">
                  <div align="right">
                  <html:link styleClass="shopping-cart-detail-other-remove" href="javascript:submitRemoveCouponForm('removeCoupon', '${shoppingCartCouponInfo.couponId}')">
                    <lang:contentMessage value="Remove"/>
                  </html:link>
                  </div>
                </td>
              </tr>
            </table>
          </td>
        </tr>

      </table>
    </td>
  </tr>
  </c:forEach>
  <tr>
    <td id="shopping-cart-summary-container">
      <table width="300" border="0" align="right" cellpadding="0" id="shopping-cart-total-table">
        <tr>
          <td align="right"><span class="shopping-cart-subtotal-label"><lang:contentMessage value="Cart sub-total"/></span></td>
          <td align="right"><span class="shopping-cart-subtotal-value">${shoppingCartActionForm.priceTotal}</span></td>
        </tr>
        <tr>
          <td align="right"><span class="shopping-cart-subtotal-label"><lang:contentMessage value="Shipping"/></span></td>
          <td align="right"><span class="shopping-cart-subtotal-value">${shoppingCartActionForm.shippingOrderTotal}</span></td>
        </tr>
        <c:forEach var="shoppingCartTaxInfo" items="${shoppingCartActionForm.shoppingCartTaxInfos}">
        <tr>
          <td align="right"><span class="shopping-cart-subtotal-label">${shoppingCartTaxInfo.taxName}</span></td>
          <td align="right"><span class="shopping-cart-subtotal-value">${shoppingCartTaxInfo.taxAmount}</span></td>
        </tr>
        </c:forEach>
        <tr>
          <td colspan="2"><hr class="shopping-cart-detail-seperator"></td>
        </tr>
        <tr>
          <td align="right"><span class="shopping-cart-total-label"><lang:contentMessage value="Order total"/></span></td>
          <td align="right"><span class="shopping-cart-total-value">${shoppingCartActionForm.orderTotal}</span></td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
        </tr>
        <c:if test="${!shoppingCartForm.shippingQuoteLock}">
        <tr>
          <td colspan="2">
          <div align="right">
             <html:link href="javascript:submitForm('updateQty');" styleClass="shopping-cart-update-button">
             <lang:contentMessage value="Update Shopping Cart"/>
             </html:link>
          </div>
          </td>
        </tr>
        </c:if>
      </table>
    </td>
  </tr>
</table>
<br>
<div id="shopping-cart-checkout-container">
   <c:if test="${fn:length(shoppingCartActionForm.shoppingCartItemInfos) > 0 && (shoppingCartActionForm.shippingValid == true || shoppingCartActionForm.includeShippingPickUp == true)}">
     &nbsp;
     <c:choose>
       <c:when test='${shoppingCartActionForm.cashPaymentOrder == true}'>
         <html:link href="javascript:submitCashFinalizeForm('finalize');" styleClass="shopping-cart-checkout-button">
         <lang:contentMessage value="Confirm Checkout"/>
         </html:link>
       </c:when>
   <c:when test='${shoppingCartActionForm.paymentGatewayProvider == "PayPalEngine"}'>
         <html:link href="javascript:submitFinalizeForm('finalize');" styleClass="shopping-cart-checkout-button">
         <lang:contentMessage value="Confirm Checkout"/>
         </html:link>
   </c:when>
   <c:when test='${shoppingCartActionForm.paymentGatewayProvider == "PayPalWebsitePaymentProHostedEngine"}'>
         <html:link href="javascript:submitPayPalProHostedCreditCardForm('start');" styleClass="shopping-cart-checkout-button">
         <lang:contentMessage value="Confirm Checkout"/>
         </html:link>
   </c:when>
   <c:otherwise>
         <html:link href="javascript:submitCreditCardForm('start');" styleClass="shopping-cart-checkout-button">
         <lang:contentMessage value="Confirm Checkout"/>
         </html:link>
   </c:otherwise>
 </c:choose>
   </c:if>
</div>
</div>
<br>
<div id="shopping-cart-footer-container">
  <c:out value='${shoppingCartActionForm.shoppingCartMessage}' escapeXml='false'/>
</div>
<br>
<br>
</html:form>
</div>
<script type="text/javascript" language="JavaScript">

var paymentGatewayProvider = '${shoppingCartActionForm.paymentGatewayProvider}';
YAHOO.util.Event.onAvailable('step1', function() {
	if (paymentGatewayProvider == 'PayPalEngine') {
		var object = document.getElementById('step2');
		object.className = 'shopping-cart-steps-active';
		object = document.getElementById('step2_line');
		object.className = 'shopping-cart-steps-line-active';
	}
	else {
		var object = document.getElementById('step3');
		object.className = 'shopping-cart-steps-active';
		object = document.getElementById('step3_line');
		object.className = 'shopping-cart-steps-line-active';
	}
} );
</script>