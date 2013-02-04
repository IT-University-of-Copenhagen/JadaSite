<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<jsp:useBean id="myAccountOrderStatusActionForm"  type="com.jada.myaccount.order.MyAccountOrderStatusActionForm"  scope="request" />
<c:set var="form" scope="request" value="${myAccountOrderStatusActionForm}"/>
<div id="my-account-container">
  <div id="my-account-nav-container">
  	<jsp:include page="/myaccount/myAccountNav.jsp" />
  </div>
  <div id="my-account-body-container">
    <html:form method="post" action="/myaccount/order/myAccountOrderStatus.do?process=update">
    <html:hidden property="process" value=""/>
    <div id="my-account-header-container"><lang:contentMessage value="My orders"/></div>
    <div id="my-account-order-body-container">
	<table border="0" cellpadding="3" class="my-account-table">
	  <tr>
	    <td width="150"><span class="my-account-form-label"><lang:contentMessage value="Order number"/></span></td>
	    <td>
	    <span class="my-account-form-value">${form.orderNum}</span>
	    </td>
	  </tr>
	  <tr>
	    <td width="150"><span class="my-account-form-label"><lang:contentMessage value="Date"/></span></td>
	    <td>
	    <span class="my-account-form-value">${form.orderDatetime}</span>
	    </td>
	  </tr>
	  <c:choose>
	  <c:when test="${shoppingCartActionForm.paymentGatewayEngine != 'PayPalEngine'}">
	  <tr>
	    <td width="150"><span class="my-account-form-label"><lang:contentMessage value="Credit card type"/></span></td>
	    <td>
	    <span class="my-account-form-value">${form.creditCardDesc}</span>
	    </td>
	  </tr>
	  <c:if test="${form.payPal != true}">
	  <tr>
	    <td width="150"><span class="my-account-form-label"><lang:contentMessage value="Credit card"/></span></td>
	    <td>
	    <span class="my-account-form-value">${form.custCreditCardNum}</span>
	    </td>
	  </tr>
	  </c:if>
	  <tr>
	    <td width="150"><span class="my-account-form-label"><lang:contentMessage value="Authorization number"/></span></td>
	    <td>
	    <span class="my-account-form-value">${form.authCode}</span>
	    </td>
	  </tr>
	  </c:when>
	  <c:otherwise>
	  <tr>
	    <td width="150"><span class="my-account-form-label"><lang:contentMessage value="Payment"/></span></td>
	    <td>
	    <span class="my-account-form-value">PayPal</span>
	    </td>
	  </tr>
	  <tr>
	    <td width="150"><span class="my-account-form-label"><lang:contentMessage value="Credit card type"/></span></td>
	    <td>
	    <span class="my-account-form-value">${form.paymentReference1}</span>
	    </td>
	  </tr>
	  </c:otherwise>
	  </c:choose>
	</table>
	<br>
	<table width="600" border="0" cellpadding="3" class="my-account-table">
	  <tr>
	    <td colspan="2"><span class="my-account-form-title"><lang:contentMessage value="My information"/></span></td>
	  </tr>
	  <tr>
	    <td width="150"><span class="my-account-form-label"><lang:contentMessage value="Email address"/></span></td>
	    <td>
	    <span class="my-account-form-value">${form.custEmail}</span>
	    </td>
	  </tr>
	  <tr>
	    <td width="150" valign="top"><span class="my-account-form-label"><lang:contentMessage value="Address"/></span></td>
	    <td valign="top">
	    <span class="my-account-form-value">
	    ${form.custFirstName} ${form.custLastName}<br>
	    ${form.custAddressLine1}<br>
	    <c:if test="${not empty (form.custAddressLine2)}">
	      ${form.custAddressLine2}<br>
	    </c:if>
	    ${form.custCityName} ${form.custStateName}<br>
	    ${form.custCountryName}<br>
	    ${form.custZipCode}
	    </span>
	    </td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td></td>
	  </tr>
	  <tr>
	    <td colspan="2">
	      <span class="my-account-form-title"><lang:contentMessage value="Billing information"/></span>
	    </td>
	  </tr>
	  <tr>
	    <td colspan="2">
	      <c:choose>
	        <c:when test="${form.billingCustUseAddress == 'C'}">
	        <span class="my-account-form-value"><lang:contentMessage value="Same as my information"/></span>
	        </c:when>
	      </c:choose>
	    </td>
	  </tr>
	  <c:if test="${form.billingCustUseAddress == 'O'}">
	  <tr>
	    <td width="150" valign="top"><span class="my-account-form-label"><lang:contentMessage value="Address"/></span></td>
	    <td valign="top">
	    <span class="my-account-form-value">
	    ${form.billingCustFirstName} ${form.billingCustLastName}<br>
	    ${form.billingCustAddressLine1}<br>
	    <c:if test="${not empty (form.billingCustAddressLine2)}">
	      ${form.billingCustAddressLine2}<br>
	    </c:if>
	    ${form.billingCustCityName} ${form.billingCustStateName}<br>
	    ${form.billingCustCountryName}<br>
	    ${form.billingCustZipCode}
	    </span>
	    </td>
	  </tr>
	  </c:if>
	  <tr>
	    <td>&nbsp;</td>
	    <td></td>
	  </tr>
	  <tr>
	    <td colspan="2">
	      <span class="my-account-form-title"><lang:contentMessage value="Shipping information"/></span>
	    </td>
	  </tr>
	  <tr>
	    <td colspan="2">
	      <c:choose>
	        <c:when test="${form.shippingCustUseAddress == 'C'}">
	        <span class="my-account-form-value"><lang:contentMessage value="Same as my information"/></span>
	        </c:when>
	        <c:when test="${form.shippingCustUseAddress == 'B'}">
	        <span class="my-account-form-value"><lang:contentMessage value="Same as billing information"/></span>
	        </c:when>
	      </c:choose>
	    </td>
	  </tr>
	  <c:if test="${form.shippingCustUseAddress == 'O'}">
	  <tr>
	    <td width="150" valign="top"><span class="my-account-form-label"><lang:contentMessage value="Address"/></span></td>
	    <td valign="top">
	    <span class="my-account-form-value">
	    ${form.shippingCustFirstName} ${form.shippingCustLastName}<br>
	    ${form.shippingCustAddressLine1}<br>
	    <c:if test="${not empty (form.shippingCustAddressLine2)}">
	      ${form.shippingCustAddressLine2}<br>
	    </c:if>
	    ${form.shippingCustCityName} ${form.shippingCustStateName}<br>
	    ${form.shippingCustCountryName}<br>
	    ${form.shippingCustZipCode}
	    </span>
	    </td>
	  </tr>
	  </c:if>
	  <tr>
	    <td>&nbsp;</td>
	    <td></td>
	  </tr>
	  <tr>
		<td colspan="2"><span class="my-account-form-title"><lang:contentMessage value="Shipping method"/></span></td>
	  </tr>
	  <tr>
		<td colspan="2">
		  <span class="my-account-form-value">${form.shippingMethodName}</span>
		</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td></td>
	  </tr>
	  <tr>
		<td colspan="2"><span class="my-account-form-title"><lang:contentMessage value="More information"/></span></td>
	  </tr>
	  <c:forEach var="orderTracking" items="${form.orderTrackings}">
	  <tr>
		<td>
		  <span class="my-account-form-value">${orderTracking.orderTrackingMessage}</span>
		</td>
		<td>
		  <span class="my-account-form-value">${orderTracking.orderTrackingDatetime}</span>
		</td>
	  </tr>
	  </c:forEach>
	</table>
	<br>
	
	
	
	
	
	<table width="700" border="0" cellpadding="0" id="my-account-order-table">
	  <tr>
	    <td id="my-account-order-table-header"><span id="my-account-order-table-title-header"><lang:contentMessage value="Item details"/></span></td>
	  </tr>
	  <c:forEach var="orderDetail" items="${form.orderDetails}">
	  <tr>
	    <td><hr></td>
	  </tr>
	  <tr>
	    <td>
	      <table width="100%" border="0" cellpadding="0" id="my-account-order-item-table">
	        <tr>
	          <td>
	            <div align="center" id="my-account-order-item-image-container">
	              <c:if test="${orderDetail.imageId != null}">
	                <img src="/${contentBean.contextPath}/services/ImageProvider.do?type=I&imageId=${orderDetail.imageId}&maxsize=75">
	              </c:if>
	            </div>
	          </td>
	          <td valign="top" width="100%">
	            <table width="100%" border="0" cellpadding="0" id="my-account-order-item-desc-table">
	              <tr>
	                <td>
	                  <span id="my-account-order-item-desc">${orderDetail.itemShortDesc}</span><br>
	                  <c:forEach var="orderItemAttribute" items="${orderDetail.orderItemAttributes}">
	                  <span class="my-account-order-item-attrib">${orderItemAttribute.customAttribDesc} - ${orderItemAttribute.customAttribValue}</span><br>
	                  </c:forEach>
	                </td>
	                <td width="80" align="right"><span id="my-account-order-item-price">${orderDetail.itemTierPrice}</span></td>
	                <td width="80" align="center" id="my-account-order-item-qty">
	                  ${orderDetail.itemOrderQty}
	                </td>
	                <td width="80" align="right"><span id="my-account-order-item-subtotal">${orderDetail.itemSubTotal}</span></td>
	              </tr>
	            </table>
	          </td>
	        </tr>
	      </table>
	    </td>
	  </tr>
	  </c:forEach>
	  <c:forEach var="orderOtherDetail" items="${form.orderOtherDetails}">
	  <tr>
	    <td><hr></td>
	  </tr>
	  <tr>
	    <td>
	      <table width="100%" border="0" cellpadding="0" id="my-account-order-other-table">
	        <tr>
	          <td>
	            <div id="my-account-order-item-image-container">
	            </div>
	          </td>
	          <td valign="top" width="100%">
	            <table width="100%" border="0" cellpadding="0" id="my-account-order-other-table">
	              <tr>
	                <td><span class="my-account-order-item-desc">${orderOtherDetail.itemShortDesc}</span></td>
	                <td width="80"></td>
	                <td width="80"></td>
	                <td width="80" align="right"><span id="my-account-order-item-subtotal">${orderOtherDetail.itemSubTotal}</span></td>
	              </tr>
	            </table>
	          </td>
	        </tr>
	
	      </table>
	    </td>
	  </tr>
	  </c:forEach>
	  <tr>
	    <td bgcolor="#F2F2F2">
	      <table width="300" border="0" align="right" cellpadding="0" id="my-account-order-total-table">
	        <tr>
	          <td align="right"><span id="my-account-order-subtotal-label"><lang:contentMessage value="Cart sub-total"/></span></td>
	          <td align="right"><span id="my-account-order-subtotal-value">${form.priceTotal}</span></td>
	        </tr>
	        <tr>
	          <td align="right"><span id="my-account-order-subtotal-label"><lang:contentMessage value="Shipping"/></span></td>
	          <td align="right"><span id="my-account-order-subtotal-value">${form.shippingTotal}</span></td>
	        </tr>
	        <c:forEach var="orderTax" items="${form.orderTaxes}">
	        <tr>
	          <td align="right"><span id="my-account-order-subtotal-label">${orderTax.taxName}</span></td>
	          <td align="right"><span id="my-account-order-subtotal-value">${orderTax.taxAmount}</span></td>
	        </tr>
	        </c:forEach>
	        <tr>
	          <td colspan="2"><hr></td>
	        </tr>
	        <tr>
	          <td align="right"><span id="my-account-order-total-label"><lang:contentMessage value="Order total"/></span></td>
	          <td align="right"><span id="my-account-order-total-value">${form.orderTotal}</span></td>
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
    </html:form>
  </div>
</div>