<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<script type="text/javascript" language="JavaScript"><!--
function submitForm(type) {
    document.shoppingCartActionForm.process.value = type;
    document.shoppingCartActionForm.submit();
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

function populateStateCodes(countryId, stateCodeId, stateNameId) {
	var countryObject = document.getElementById(countryId);
	var stateCodeObject = document.getElementById(stateCodeId);
	var stateNameObject = document.getElementById(stateNameId);
	
	var countryCode = countryObject.value;
	if (countryCode != '') {
		var url = "/${contentBean.contextPath}/content/content/update.do?process=getStates&prefix=" + "${contentBean.siteDomain.siteDomainPrefix}" + "&countryCode=" + countryCode;
		var get_callback =
		{
			success: function(o) {
			    if (!isJsonResponseValid(o.responseText)) {
					jc_panel_show_error("Unexcepted Error: Unable to extract ");
					return false;
			    }
			    var jsonObject = getJsonObject(o.responseText);
			    var has = false;
			    if (jsonObject.states.length > 0) {
				    has = true;
			    }

			    while (stateCodeObject.options.length > 0) {
				    stateCodeObject.remove(0);
			    }
				if (has) {
					var selection = document.createElement('option');
					selection.text = '<lang:contentMessage value="Please select"/>';
					selection.value = '';
					stateCodeObject.options[stateCodeObject.length] = selection;	
					for (i = 0; i < jsonObject.states.length; i++) {
						selection = document.createElement('option');
						selection.text = jsonObject.states[i].stateName;
						selection.value = jsonObject.states[i].stateCode;
						stateCodeObject.options[stateCodeObject.length] = selection;
					}
					stateCodeObject.style.display = 'block';
					stateCodeObject.disabled = false;
					stateNameObject.style.display = 'none';
					stateNameObject.disabled = true;
					stateNameObject.value = '';
				}
				else {
					stateNameObject.style.display = 'block';
					stateNameObject.disabled = false;
					stateCodeObject.style.display = 'none';
					stateCodeObject.disabled = true;
				}
		  },
		  failure: function(o) {
		      jc_panel_show_error("Unexcepted Error: Unable to extract item tier price");
		  }
		};
		var request = YAHOO.util.Connect.asyncRequest('GET', url, get_callback);
	}
}
//--></script>
<div id="shopping-cart-container">
<c:set var="shoppingCartActionForm" scope="request" value="${shoppingCartActionForm}"/>
<div id="shopping-cart-header">
  <lang:contentMessage value="My Cart"/>
</div>
<c:if test="${shoppingCartActionForm.sequenceInterrupt}">
<span class="tran-error-normal">
You have interrupted the checkout process.  This check is to ensure transaction is not submitted more than once.
It is important to follow the process in order to maintain a smooth and efficient shopping experience.
<br><br>
In particular, you should avoid performing the following.
<br>
<ol>
<li>Use of browser navigation button, also known as back button.</li>
<li>Linking into any checkout pages via previously saved bookmark.</li>
</ol>
</span>
<br>
</c:if>
<span class="tran-error-normal">
  <lang:contentError field="message"/>
</span>
<html:form method="post" action="/content/checkout/shoppingCart.do?prefix=${contentBean.siteDomain.siteDomainPrefix}">
<html:hidden property="process" value=""/> 
<html:hidden property="itemNaturalKey" value=""/> 
<html:hidden property="couponId" value=""/> 
<c:if test="${shoppingCartActionForm.shippingMethodName != null}">
  <span id="shopping-cart-shipping-method-title"><lang:contentMessage value="Shipping method"/></span><br>
  <span id="shopping-cart-shipping-method-value">${shoppingCartActionForm.shippingMethodName}</span>
  <br><br>
</c:if>
<c:if test="${!shoppingCartActionForm.shippingQuoteLock}">
<div id="shopping-cart-coupon-container">
  <table border="0" id="shopping-cart-coupon-table">
	<tr>
	  <td><span id="shopping-cart-coupon-title"><lang:contentMessage value="Coupon"/></span></td>
	</tr>
	<tr>
	  <td>
	    <html:text property="couponCode" size="20" style="shopping-cart-coupon-value"/>
	    <html:link href="javascript:submitForm('applyCoupon')" styleClass="shopping-cart-coupon-button">
		  <lang:contentMessage value="Apply Coupon"/>
		</html:link></td>
	</tr>
	<tr>
	  <td>
	    <span class="tran-error-normal"><lang:contentError field="couponCode"/></span>
	  </td>
	 </tr>
  </table>
  <div class="container-reset"></div>
</div>
<br>
</c:if>
<c:if test="${shoppingCartActionForm.shippingQuoteLock}">
<html:link styleClass="shopping-cart-shipping-quote-button" href="javascript:submitForm('cancelShippingQuote', '${shoppingCartItemInfo.itemNaturalKey}')">
  <lang:contentMessage value="Prefer to pick up order"/>
</html:link>
<br>
</c:if>
<div id="shopping-cart-detail-container">
<div id="shopping-cart-detail-header-container"><lang:contentMessage value="Item details"/></div>
<table border="0" cellpadding="0" id="shopping-cart-detail-table">
  <tr>
    <td align="right">
      <html:link page="/web/fe/${contentBean.siteDomain.siteDomainPrefix}/${contentBean.contentSessionKey.langName}/home" styleClass="shopping-cart-continue-button">
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
          <td>
            <div id="shopping-cart-detail-image-container" style="float: left">
              <c:if test="${shoppingCartItemInfo.imageId != null}">
                <img src="/${contentBean.contextPath}/services/ImageProvider.do?type=I&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}&imageId=${shoppingCartItemInfo.imageId}&maxsize=75">
              </c:if>
            </div>
            <div id="shopping-cart-detail-desc-container">
              <span id="shopping-cart-detail-item-desc">${shoppingCartItemInfo.itemShortDesc}</span><br>
              <c:forEach var="shoppingCartItemAttribute" items="${shoppingCartItemInfo.shoppingCartItemAttributes}">
              	<span id="shopping-cart-detail-item-attrib">${shoppingCartItemAttribute.customAttribDesc} - ${shoppingCartItemAttribute.customAttribValue}</span><br>
              </c:forEach>
			</div>
			<div id="shopping-cart-detail-info-container">
            <table border="0" cellpadding="0" id="shopping-cart-detail-item-desc-table">
              <tr>
                <td width="80" valign="top" align="right"><span id="shopping-cart-detail-item-price">${shoppingCartItemInfo.itemPrice}</span></td>
                <td width="80" valign="top" align="right">
                  <span id="shopping-cart-detail-item-qty">
                    <input type="hidden" name="itemNaturalKeys" value="${shoppingCartItemInfo.itemNaturalKey}">
                    <input name="itemQtys" type="text" value="${shoppingCartItemInfo.itemQty}" size="2">
                  </span>
                  <span class="tran-error-normal">${shoppingCartItemInfo.itemQtyError}</span>
                </td>
                <td width="80" valign="top" align="right"><span id="shopping-cart-detail-item-subtotal">${shoppingCartItemInfo.itemSubTotal}</span></td>
              </tr>
              <tr>
                <td colspan="4" align="right">
                  <html:link styleClass="shopping-cart-detail-item-remove" href="javascript:submitRemoveItemForm('removeItem', '${shoppingCartItemInfo.itemNaturalKey}')">
                    <lang:contentMessage value="Remove"/>
                  </html:link>
                </td>
              </tr>
            </table>
			</div>
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
      <table border="0" cellpadding="0" id="shopping-cart-detail-other-table">
        <tr>
          <td><div id="shopping-cart-detail-image-container"></div></td>
          <td valign="top" width="100%">
            <table border="0" cellpadding="0" id="shopping-cart-detail-other-desc-table">
              <tr>
                <td align="right"><span id="shopping-cart-detail-other-desc">${shoppingCartCouponInfo.couponCode}&nbsp;${shoppingCartCouponInfo.couponName}</span></td>
                <td align="right"><span id="shopping-cart-detail-other-price">${shoppingCartCouponInfo.couponAmount}</span></td>
              </tr>
              <tr>
                <td colspan="2" align="right">
                  <html:link styleClass="shopping-cart-detail-other-remove" href="javascript:submitRemoveCouponForm('removeCoupon', '${shoppingCartCouponInfo.couponId}')">
                    <lang:contentMessage value="Remove"/>
                  </html:link>
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
      <c:if test="${!shoppingCartActionForm.customerSignin}">
      <div id="shopping-cart-estimate-container">
      <table border="0" cellpadding="0" id="shopping-cart-estimate-table">
        <tr>
          <td><span id="shopping-cart-estimate-header">Shipping and tax estimate</span></td>
        </tr>
        <tr>
          <td>
          <span class="tran-error-normal">
            <lang:contentError field="estimateShippingLocation"/>
          </span>
          </td>
        </tr>
        <c:if test="${shoppingCartActionForm.includeShippingPickUp}">
        <tr>
          <td><span class="shopping-cart-estimate-label"><lang:contentMessage value="Pick up"/></span></td>
        </tr>
        <tr>
          <td>
            <span class="shopping-cart-estimate-input">
            <html:checkbox property="estimatePickUp"/>
            </span>
          </td>
        </tr>
        </c:if>
        <tr>
          <td><span class="shopping-cart-estimate-label"><lang:contentMessage value="Country"/></span></td>
        </tr>
        <tr>
          <td>
      	    <html:select styleId="estimateCountryCode" styleClass="shopping-cart-estimate-input" property="estimateCountryCode" onchange="populateStateCodes('estimateCountryCode', 'estimateStateCode', 'estimateStateName')"> 
              <c:if test="${fn:length(shoppingCartForm.countries) > 1}">
              <html:option value=""><lang:contentMessage value="Please select"/></html:option>
              </c:if>
              <html:optionsCollection property="countries" label="label" value="value"/> 
            </html:select>
          </td>
        </tr>
        <tr>
          <td><span class="shopping-cart-estimate-label"><lang:contentMessage value="State/Province"/></span></td>
        </tr>
        <tr>
          <td>
            <c:choose>
      		<c:when test="${fn:length(shoppingCartActionForm.estimateStates) > 0}">
                <html:select styleId="estimateStateCode" property="estimateStateCode" styleClass="shopping-cart-estimate-input"> 
                  <html:option value=""><lang:contentMessage value="Please select"/></html:option>
                  <html:optionsCollection property="estimateStates" label="label" value="value"/> 
                </html:select>
                <html:text styleId="estimateStateName" property="estimateStateName" styleClass="shopping-cart-estimate-input" style="display: none; maxlength=40" disabled="true"/>
      		</c:when>
      		<c:otherwise>
                <html:select styleId="estimateStateCode" property="estimateStateCode" style="display: none;" disabled="true" styleClass="shopping-cart-estimate-input"> 
                  <html:option value=""><lang:contentMessage value="Please select"/></html:option>
                  <html:optionsCollection property="estimateStates" label="label" value="value"/> 
                </html:select>
                <html:text styleId="estimateStateName" property="estimateStateName" maxlength="40" styleClass="shopping-cart-estimate-input"/>
      		</c:otherwise>
            </c:choose>
          </td>
        </tr>
        <tr>
          <td><span class="shopping-cart-estimate-label"><lang:contentMessage value="Zip/Postal"/></span></td>
        </tr>
        <tr>
          <td>
             <html:text property="estimateZipCode" maxlength="10" styleClass="shopping-cart-estimate-input"/>
          </td>
        </tr>
        <tr>
          <td><span class="shopping-cart-estimate-label"><lang:contentMessage value="Shipping method"/></span></td>
        </tr>
        <tr>
          <td>
      	    <html:select styleId="estimateShippingMethod" styleClass="shopping-cart-estimate-input" property="shippingMethodId"> 
              <html:option value=""><lang:contentMessage value="Please select"/></html:option>
              <html:optionsCollection property="shippingMethods" label="label" value="value"/> 
            </html:select>
          </td>
        </tr>
        <tr>
          <td>
            <html:link href="javascript:submitForm('estimate')" styleClass="shopping-cart-estimate-button">
      	    Estimate
      	    </html:link>
      	  </td>
        </tr>
      </table>
      </div>
      </c:if>
      <div id="shopping-cart-total-container">
      <table width="300px" border="0" cellpadding="0" id="shopping-cart-total-table">
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
        <tr>
          <td colspan="2" align="right">
 	        <html:link href="javascript:submitForm('updateQty')" styleClass="shopping-cart-update-button">
	          <lang:contentMessage value="Update Shopping Cart"/>
	        </html:link>
	        <br>
          </td>
        </tr>
      </table>
      </div>
    </td>
  </tr>
</table>
</div>
<div id="shopping-cart-checkout-container">
  <c:if test="${fn:length(shoppingCartActionForm.shoppingCartItemInfos) > 0}">
  <c:if test="${shoppingCartActionForm.payPal}">
    <div class="shopping-cart-mobile-view">
      <html:link page="/content/checkout/shoppingCartProcess.do?process=payPalAuthorize&prefix=${contentBean.siteDomain.siteDomainPrefix}&mobile=Y">
        <html:img src="https://www.paypal.com/en_US/i/btn/btn_xpressCheckout.gif" border="0"/>
      </html:link>
    </div>
    <div class="shopping-cart-normal-view">
      <html:link page="/content/checkout/shoppingCartProcess.do?process=payPalAuthorize&prefix=${contentBean.siteDomain.siteDomainPrefix}">
        <html:img src="https://www.paypal.com/en_US/i/btn/btn_xpressCheckout.gif" border="0"/>
      </html:link>
    </div>
    &nbsp;
  </c:if>
  <c:if test="${shoppingCartActionForm.creditCard}">
    <html:link page="/content/checkout/shoppingCartProcess.do?process=start&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}" styleClass="shopping-cart-checkout-button">
      <lang:contentMessage value="Check-out with Credit Card"/>
    </html:link>
  </c:if>
  <c:if test="${shoppingCartActionForm.cashPayment}">
    <html:link page="/content/checkout/shoppingCartProcess.do?process=start&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}&cash=Y" styleClass="shopping-cart-checkout-button">
      <lang:contentMessage value="Cash On Delivery"/>
    </html:link>
  </c:if>
  </c:if>
</div>
<br>
<c:if test="${fn:length(shoppingCartActionForm.crossSellItems) > 0}">
<div id="shopping-cart-crosssell-header"><lang:contentMessage value="You may also be interested in"/></div>
<div id="shopping-cart-crosssell-container">
  <div id="shopping-cart-crosssell-left-arrow-container">
    <a id="crossSellPrevious" href="javascript:void(0);"><img src="/${contentBean.contextPath}/content/template/basic/images/left-1.gif" border="0"></a>
  </div>
  <div id="jc_crossSellItem" class="shopping-cart-crosssell-content-container">
    <ol class="shopping-cart-crosssell-element">
      <c:forEach var="crossSellItem" items="${shoppingCartActionForm.crossSellItems}">
         <li>
           <div class="shopping-cart-crosssell-item-container">
             <c:if test="${crossSellItem.itemDefaultImageUrl != null}">
               <a href="${crossSellItem.itemUrl}"><img src="${crossSellItem.itemDefaultImageUrl}&maxsize=150" border="0"></a><br>
             </c:if>
             <span id="shopping-cart-crosssell-item-desc"><a href="${crossSellItem.itemUrl}">${crossSellItem.itemShortDesc}</a></span><br>
             <div class="jc_rating_outer">
               <div class="jc_rating_inner" style="width: ${crossSellItem.commentRatingPercentage}%">&nbsp;</div>
             </div>
             <lang:contentMessage value="Rating"/> ${crossSellItem.commentRating}
             <br>
             <c:choose>
               <c:when test="${crossSellItem.itemPriceRange}">
                 <span id="shopping-cart-crosssell-item-price-range">
                   <lang:contentMessage value="From"/> ${crossSellItem.itemPriceFrom} <lang:contentMessage value="to"/> ${crossSellItem.itemPriceTo}<br>
                 </span>
               </c:when>
               <c:otherwise>
               <c:choose>
                 <c:when test="${crossSellItem.special}">
                   <span id="shopping-cart-crosssell-item-regular-price">${crossSellItem.itemPrice}</span> <span id="shopping-cart-crosssell-item-spec-price">${crossSellItem.itemSpecPrice}</span><br>
                 </c:when>
                 <c:otherwise>
                   <span id="shopping-cart-crosssell-item-price">${crossSellItem.itemPrice}</span><br>
                 </c:otherwise>
               </c:choose>
               </c:otherwise>
             </c:choose>
             <c:forEach var="itemTierPrice" items="${crossSellItem.itemTierPrices}">
               <span id="shopping-cart-crosssell-item-tier-price">
                 ${itemTierPrice.itemTierQty} / ${itemTierPrice.itemTierPrice}<br>
               </span>
             </c:forEach>
             <c:if test="${crossSellItem.outOfStock}">
               <span id="shopping-cart-crosssell-outofstock"><lang:contentMessage value="Out of Stock"/></span><br>
             </c:if>
             <a href="${crossSellItem.itemUrl}" id="shopping-cart-button"><lang:contentMessage value="View more"/></a>
          </div>
	    </li>
      </c:forEach>
    </ol>
  </div>
  <div id="shopping-cart-crosssell-right-arrow-container">
    <a id="crossSellNext" href="javascript:void(0);"><img src="/${contentBean.contextPath}/content/template/basic/images/right-1.gif" border="0"></a>
  </div>
  <div class="container-reset"></div>
</div>
</c:if>
<div id="shopping-cart-footer-container">
  <c:out value='${shoppingCartActionForm.shoppingCartMessage}' escapeXml='false'/>
</div>
</html:form>
</div>

<script language="JavaScript">
<c:if test="${fn:length(shoppingCartActionForm.crossSellItems) > 0}">
	YAHOO.util.Event.onContentReady('jc_crossSellItem', function() {
		var crossSellCarousel = new Carousel('jc_crossSellItem', {'isVertical': false, 'itemWidth' : 200, 'margin' : 6, 'numVisible' : 4});
		YAHOO.util.Event.addListener('crossSellPrevious', "click", function() {
			crossSellCarousel.previous();
		}); 
		YAHOO.util.Event.addListener('crossSellNext', "click", function() {
			crossSellCarousel.next();
		});
	} );
</c:if>
</script>