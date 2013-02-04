<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<c:set var="shoppingCartForm" scope="request" value="${shoppingCartActionForm}"/>
<div id="shopping-cart-container">
<c:if test="${shoppingCartForm.print != true}">
<%@ include file="/content/shoppingCart/shoppingCartSteps.jsp" %>
<br>
</c:if>
<div id="shopping-cart-confirm-header-container">
  <span id="shopping-cart-confirm-header"><lang:contentMessage value="My Cart"/></span>&nbsp;&nbsp;
  <c:if test="${shoppingCartForm.print != true}">
  <html:link page="/content/checkout/shoppingCartConfirmation.do?process=confirm&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}&print=Y" paramId="shoppingCart.orderNum" paramName="shoppingCartForm" paramProperty="orderNum" styleClass="shopping-cart-print-button" target="_blank">
    <lang:contentMessage value="print view"/>
  </html:link>
  </c:if>
</div>
<br>
<div id="shopping-cart-info-container">
<c:if test="${shoppingCartForm.print != true}">
<br>
<span id="shopping-cart-confirm-message"><lang:contentMessage value="Thank You.  Transaction has been successfully completed."/></span><br>
<br>
</c:if>
<table width="800" border="0" cellpadding="0" class="shopping-cart-info-table">
  <tr>
    <td><span class="shopping-cart-info-title"><lang:contentMessage value="My order"/></span></td>
  </tr>
</table>
<table width="800" border="0" cellpadding="3" class="shopping-cart-info-table">
  <tr>
    <td width="150"><span class="shopping-cart-info-label"><lang:contentMessage value="Order number"/></span></td>
    <td>
      <span class="shopping-cart-info-value">${shoppingCartForm.orderNum}</span>
    </td>
  </tr>
  <tr>
    <td width="150"><span class="shopping-cart-info-label"><lang:contentMessage value="Date"/></span></td>
    <td>
      <span class="shopping-cart-info-value">${shoppingCartForm.orderDatetime}</span>
    </td>
  </tr>
  <tr>
    <td width="150"><span class="shopping-cart-info-label"><lang:contentMessage value="Currency"/></span></td>
    <td>
      <span class="shopping-cart-info-value">${shoppingCartForm.currencyCode}</span>
    </td>
  </tr>
  <c:if test="${shoppingCartActionForm.paymentGatewayProvider == null}">
  <tr>
    <td width="150"><span class="shopping-cart-info-label"><lang:contentMessage value="Payment type"/></span></td>
    <td>
      <span class="shopping-cart-info-value"><lang:contentMessage value="Cash On Delivery"/></span>
    </td>
  </tr>
  </c:if>
  <c:if test="${shoppingCartActionForm.paymentGatewayProvider != null}">
  <c:choose>
  <c:when test="${shoppingCartActionForm.paymentGatewayProvider != 'PayPalEngine'}">
  <tr>
    <td width="150"><span class="shopping-cart-info-label"><lang:contentMessage value="Payment type"/></span></td>
    <td>
      <span class="shopping-cart-info-value"><lang:contentMessage value="Credit card"/></span>
    </td>
  </tr>
  <tr>
    <td width="150"><span class="shopping-cart-info-label"><lang:contentMessage value="Credit card"/></span></td>
    <td>
      <span class="shopping-cart-info-value">${shoppingCartForm.custCreditCardNum}</span>
    </td>
  </tr>
  <tr>
    <td width="150"><span class="shopping-cart-info-label"><lang:contentMessage value="Authorization number"/></span></td>
    <td>
      <span class="shopping-cart-info-value">${shoppingCartForm.authCode}</span>
    </td>
  </tr>
  </c:when>
  <c:otherwise>
  <tr>
    <td width="150"><span class="shopping-cart-info-label"><lang:contentMessage value="Payment type"/></span></td>
    <td>
      <span class="shopping-cart-info-label">PayPal</span>
    </td>
  </tr>
  <tr>
    <td width="150"><span class="shopping-cart-info-label"><lang:contentMessage value="Payment reference"/></span></td>
    <td>
      <span class="shopping-cart-info-value">${shoppingCartForm.authCode}</span>
    </td>
  </tr>
  </c:otherwise>
  </c:choose>
  </c:if>
</table>
<br>
<table width="800" border="0" cellpadding="3" class="shopping-cart-info-table">
  <tr>
    <td><span class="shopping-cart-info-title"><lang:contentMessage value="My information"/></span></td>
    <td></td>
  </tr>
  <tr>
    <td width="150"><span class="shopping-cart-info-label"><lang:contentMessage value="Email address"/></span></td>
    <td>
      <span class="shopping-cart-info-value">${shoppingCartForm.custEmail}</span>
    </td>
  </tr>
  <tr>
    <td width="150" valign="top"><span class="shopping-cart-info-label"><lang:contentMessage value="Address"/></span></td>
    <td valign="top">
    <span class="shopping-cart-info-value">
    ${shoppingCartForm.custAddress.custFirstName} ${shoppingCartForm.custAddress.custLastName}<br>
    ${shoppingCartForm.custAddress.custAddressLine1}<br>
    <c:if test="${not empty (shoppingCartForm.custAddress.custAddressLine2)}">
      ${shoppingCartForm.custAddress.custAddressLine2}<br>
    </c:if>
    ${shoppingCartForm.custAddress.custCityName} ${shoppingCartForm.custAddress.custStateName}<br>
    ${shoppingCartForm.custAddress.custCountryName}<br>
    ${shoppingCartForm.custAddress.custZipCode}
    </span>
    </td>
  </tr>
</table>
<br>
<table width="800" border="0" cellpadding="3" class="shopping-cart-info-table">
  <tr>
    <td><span class="shopping-cart-info-title"><lang:contentMessage value="Billing information"/></span></td>
  </tr>
  <tr>
    <td>
      <c:choose>
        <c:when test="${shoppingCartForm.billingAddress.custUseAddress == 'C'}">
        <span class="shopping-cart-info-value"><lang:contentMessage value="Same as my information"/></span>
        </c:when>
      </c:choose>
    </td>
  </tr>
  <c:if test="${shoppingCartForm.billingAddress.custUseAddress == 'O'}">
  <tr>
    <td width="150" valign="top"><span class="shopping-cart-info-label"><lang:contentMessage value="Address"/></span></td>
    <td valign="top">
    <span class="shopping-cart-info-value">
    ${shoppingCartForm.billingAddress.custFirstName} ${shoppingCartForm.billingAddress.custLastName}<br>
    ${shoppingCartForm.billingAddress.custAddressLine1}<br>
    <c:if test="${not empty (shoppingCartForm.billingAddress.custAddressLine2)}">
      ${shoppingCartForm.billingAddress.custAddressLine2}<br>
    </c:if>
    ${shoppingCartForm.billingAddress.custCityName} ${shoppingCartForm.billingAddress.custStateName}<br>
    ${shoppingCartForm.billingAddress.custCountryName}<br>
    ${shoppingCartForm.billingAddress.custZipCode}
    </span>
    </td>
  </tr>
  </c:if>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>
      <span class="shopping-cart-info-title"><lang:contentMessage value="Shipping information"/></span>
    </td>
  </tr>
  <tr>
    <td>
      <c:choose>
        <c:when test="${shoppingCartForm.shippingAddress.custUseAddress == 'C'}">
          <span class="shopping-cart-info-value"><lang:contentMessage value="Same as my information"/></span>
        </c:when>
        <c:when test="${shoppingCartForm.shippingAddress.custUseAddress == 'B'}">
          <span class="shopping-cart-info-value"><lang:contentMessage value="Same as billing information"/></span>
        </c:when>
      </c:choose>
    </td>
  </tr>
  <c:if test="${shoppingCartForm.shippingAddress.custUseAddress == 'O'}">
  <tr>
    <td width="150" valign="top"><span class="shopping-cart-info-label"><lang:contentMessage value="Address"/></span></td>
    <td valign="top">
    <span class="shopping-cart-info-value">
    ${shoppingCartForm.shippingAddress.custFirstName} ${shoppingCartForm.shippingAddress.custLastName}<br>
    ${shoppingCartForm.shippingAddress.custAddressLine1}<br>
    <c:if test="${not empty (shoppingCartForm.shippingAddress.custAddressLine2)}">
      ${shoppingCartForm.shippingAddress.custAddressLine2}<br>
    </c:if>
    ${shoppingCartForm.shippingAddress.custCityName} ${shoppingCartForm.shippingAddress.custStateName}<br>
    ${shoppingCartForm.shippingAddress.custCountryName}<br>
    ${shoppingCartForm.shippingAddress.custZipCode}
    </span>
    </td>
  </tr>
  </c:if>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td><span class="shopping-cart-info-title"><lang:contentMessage value="Shipping method"/></span></td>
  </tr>
  <tr>
    <td>
      <span class="shopping-cart-info-value">${shoppingCartForm.shippingMethodName}</span>
	</td>
  </tr>
</table>
<br>
</div>
<br>



<div id="shopping-cart-detail-container">
<div id="shopping-cart-detail-header-container"><lang:contentMessage value="Item details"/></div>
<table width="100%" border="0" cellpadding="0" id="shopping-cart-detail-table">
  <c:forEach var="shoppingCartItemInfo" items="${shoppingCartActionForm.shoppingCartItemInfos}">
  <tr>
    <td><hr id="shopping-cart-detail-seperator"></td>
  </tr>
  <tr>
    <td>
      <table width="100%" border="0" cellpadding="0" id="shopping-cart-detail-item-table">
        <tr>
          <td width="80px">
            <div id="shopping-cart-detail-image-container">
              <c:if test="${shoppingCartItemInfo.imageId != null}">
                <img src="/${contentBean.contextPath}/services/ImageProvider.do?type=I&imageId=${shoppingCartItemInfo.imageId}&maxsize=75">
              </c:if>
            </div>
          </td>
          <td valign="top">
            <table width="100%" border="0" cellpadding="0" id="shopping-cart-detail-item-desc-table">
              <tr>
                <td>
                  <span class="shopping-cart-detail-item-desc">${shoppingCartItemInfo.itemShortDesc}</span><br>
                  <c:forEach var="shoppingCartItemAttribute" items="${shoppingCartItemInfo.shoppingCartItemAttributes}">
                  <span class="shopping-cart-detail-item-attrib">${shoppingCartItemAttribute.customAttribDesc} - ${shoppingCartItemAttribute.customAttribValue}</span><br>
                  </c:forEach>
                </td>
                <td width="80" valign="top" align="right"><span id="shopping-cart-detail-item-price">${shoppingCartItemInfo.itemPrice}</span></td>
                <td width="80" valign="top" align="right">
                  <span id="shopping-cart-detail-item-qty">
                    ${shoppingCartItemInfo.itemQty}
                  </span>
                </td>
                <td width="80" valign="top" align="right"><span id="shopping-cart-detail-item-subtotal">${shoppingCartItemInfo.itemSubTotal}</span></td>
              </tr>
            </table>
          </td>
        </tr>

      </table>
    </td>
  </tr>
  </c:forEach>
  <c:forEach var="shoppingCartCouponInfo" items="${shoppingCartActionForm.shoppingCartCouponInfos}">
  <tr>
    <td><hr class="shopping-cart-detail-seperator"></td>
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
          <td align="right"><span class="shopping-cart-total-label">${shoppingCartActionForm.orderTotal}</span></td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</div>
<br>
<div id="shopping-cart-footer-container">
  <c:out value='${shoppingCartActionForm.shoppingCartMessage}' escapeXml='false'/>
</div>
</div>
<script type="text/javascript" language="JavaScript">
var paymentGatewayProvider = '${shoppingCartForm.paymentGatewayProvider}';
var elementId = '';
if (paymentGatewayProvider == 'PayPalEngine') {
	elementId = 'step3';
}
else {
	elementId = 'step5';
}
YAHOO.util.Event.onAvailable(elementId, function() {
	var object = document.getElementById(elementId);
	object.className = 'shopping-cart-steps-active';
	object = document.getElementById(elementId + '_line');
	object.className = 'shopping-cart-steps-line-active';
} );
</script>