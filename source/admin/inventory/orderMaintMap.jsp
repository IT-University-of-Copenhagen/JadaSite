<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<div class="jc_detail_panel_header">
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td><span class="jc_input_label">Purchase Order - <a class="jc_simple_link" href="/${adminBean.contextPath}/admin/inventory/orderMaint.do?process=edit&orderHeaderId=${orderMaintForm.orderHeader.orderHeaderId}">${orderMaintForm.orderHeader.orderNum}</a></span></td>
    </tr>
  </table>
</div>
<div class="jc_detail_panel">
<table border="0" cellpadding="3">
  <tr>
    <td width="150"><span class="jc_input_label">Order Number</span></td>
    <td>${orderMaintForm.orderHeader.orderNum}</td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Order Date</span></td>
    <td>${orderMaintForm.orderHeader.orderDate}</td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Language</span></td>
    <td>${orderMaintForm.orderHeader.langName}</td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Currency</span></td>
    <td>${orderMaintForm.orderHeader.currencyCode}</td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Payment Gateway</span></td>
    <td>
    <c:if test="${orderMaintForm.orderHeader.paymentGatewayProvider == null}">
    Cash On Delivery
    </c:if>
    <c:if test="${orderMaintForm.orderHeader.paymentGatewayProvider != null}">
    <c:choose>
    <c:when test="${orderMaintForm.orderHeader.paymentGatewayProvider == 'PayPalEngine'}">
    PayPal
    </c:when>
    <c:otherwise>
    Credit card
    </c:otherwise>
    </c:choose>
    </c:if>
    </td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Item Total</span></td>
    <td>${orderMaintForm.orderHeader.orderPriceTotal}</td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Item Discount Total</span></td>
    <td>${orderMaintForm.orderHeader.orderPriceDiscountTotal}</td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Sub-total</span></td>
    <td>${orderMaintForm.orderHeader.orderSubTotal}</td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Shipping Method</span></td>
    <td>${orderMaintForm.orderHeader.shippingMethodName}</td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Shipping</span></td>
    <td>${orderMaintForm.orderHeader.shippingTotal}</td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Shipping Discount</span></td>
    <td>${orderMaintForm.orderHeader.shippingDiscountTotal}</td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Tax Total</span></td>
    <td>${orderMaintForm.orderHeader.orderTaxTotal}</td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Total</span></td>
    <td>${orderMaintForm.orderHeader.orderTotal}</td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Status</span></td>
    <td>${orderMaintForm.orderHeader.orderStatus}</td>
  </tr>
</table>
<c:if test="${orderMaintForm.orderHeader.paymentTran != null}">
  <hr>
  <table border="0" cellpadding="3">
    <tr>
      <td width="150"><span class="jc_input_label">Authorization Code</span></td>
      <td>${orderMaintForm.orderHeader.paymentTran.authCode}</td>
    </tr>
    <c:if test="${orderMaintForm.orderHeader.paymentTran.paymentReference1 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label">Reference</span></td>
        <td>${orderMaintForm.orderHeader.paymentTran.paymentReference1}</td>
      </tr>
    </c:if>
    <c:if test="${orderMaintForm.orderHeader.paymentTran.paymentReference2 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${orderMaintForm.orderHeader.paymentTran.paymentReference2}</td>
      </tr>
    </c:if>
    <c:if test="${orderMaintForm.orderHeader.paymentTran.paymentReference3 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${orderMaintForm.orderHeader.paymentTran.paymentReference3}</td>
      </tr>
    </c:if>
    <c:if test="${orderMaintForm.orderHeader.paymentTran.paymentReference4 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${orderMaintForm.orderHeader.paymentTran.paymentReference4}</td>
      </tr>
    </c:if>
    <c:if test="${orderMaintForm.orderHeader.paymentTran.paymentReference5 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${orderMaintForm.orderHeader.paymentTran.paymentReference5}</td>
      </tr>
    </c:if>
  </table>
</c:if>
<c:if test="${orderMaintForm.orderHeader.voidPaymentTran != null}">
  <hr>
  <table border="0" cellpadding="3">
    <tr>
      <td width="150"><span class="jc_input_label">Void transaction</span></td>
      <td></td>
    </tr>
    <tr>
      <td width="150"><span class="jc_input_label">Authorization Code</span></td>
      <td>${orderMaintForm.orderHeader.voidPaymentTran.authCode}</td>
    </tr>
    <c:if test="${orderMaintForm.orderHeader.voidPaymentTran.paymentReference1 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label">Reference</span></td>
        <td>${orderMaintForm.orderHeader.voidPaymentTran.paymentReference1}</td>
      </tr>
    </c:if>
    <c:if test="${orderMaintForm.orderHeader.voidPaymentTran.paymentReference2 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${orderMaintForm.orderHeader.voidPaymentTran.paymentReference2}</td>
      </tr>
    </c:if>
    <c:if test="${orderMaintForm.orderHeader.voidPaymentTran.paymentReference3 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${orderMaintForm.orderHeader.voidPaymentTran.paymentReference3}</td>
      </tr>
    </c:if>
    <c:if test="${orderMaintForm.orderHeader.voidPaymentTran.paymentReference4 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${orderMaintForm.orderHeader.voidPaymentTran.paymentReference4}</td>
      </tr>
    </c:if>
    <c:if test="${orderMaintForm.orderHeader.voidPaymentTran.paymentReference5 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${orderMaintForm.orderHeader.voidPaymentTran.paymentReference5}</td>
      </tr>
    </c:if>
  </table>
</c:if>
</div>
<c:if test="${orderMaintForm.orderHeader.shippingQuote}">
    <div class="jc_detail_panel_header">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">Shipping quote</span></td>
          </tr>
        </table>
    </div>
	<div class="jc_detail_panel">
	<table border="0" cellpadding="3">
	  <tr>
	    <td width="150"><span class="jc_input_label">Shipping</span></td>
	    <td>
          <c:choose>
            <c:when test="${orderMaintForm.orderHeader.orderOpen}">
              <html:text name="orderMaintForm" property="shippingQuoteTotal" styleClass="tableContent"/>
            </c:when>
            <c:otherwise>
              ${orderMaintForm.shippingQuoteTotal}
            </c:otherwise>
          </c:choose>
	    </td>
	  </tr>
	  <tr>
	    <td width="150"></td>
	    <td class="jc_input_error">
		  <logic:messagesPresent property="shippingQuoteTotal" message="true"> 
			<html:messages property="shippingQuoteTotal" id="errorText" message="true"> 
			  <bean:write name="errorText"/> 
			</html:messages>
		  </logic:messagesPresent> 
	    </td>
	  </tr>
	  <tr>
	    <td><span class="jc_input_label">Shipping rate valid until</span></td>
	    <td>
	      <c:choose>
            <c:when test="${orderMaintForm.orderHeader.orderOpen}">
		      <html:text name="orderMaintForm" property="shippingValidUntil" styleClass="tableContent"/>
		      <img id="shippingValidUntilSwitch" src="../images/icon/image_plus.gif" border="0">
		      <div id="shippingValidUntilContainer" style="position: absolute; display:none"></div>
            </c:when>
            <c:otherwise>
              ${orderMaintForm.shippingValidUntil}
            </c:otherwise>
          </c:choose>
	    </td>
	  </tr>
	  <tr>
	    <td width="150"></td>
	    <td class="jc_input_error">
		  <logic:messagesPresent property="shippingValidUntil" message="true"> 
			<html:messages property="shippingValidUntil" id="errorText" message="true"> 
			  <bean:write name="errorText"/> 
			</html:messages>
		  </logic:messagesPresent> 
	    </td>
	  </tr>
	  <tr>
	    <td></td>
	    <td>
	      <div id="butShippingQuoteDiv"></div>
	    </td>
	  </tr>
	  <tr>
	    <td width="150"></td>
	    <td class="jc_input_error">
		  <logic:messagesPresent property="sendShippingQuote" message="true"> 
			<html:messages property="sendShippingQuote" id="errorText" message="true"> 
			  <bean:write name="errorText"/> 
			</html:messages>
		  </logic:messagesPresent> 
	    </td>
	  </tr>
	</table>
	</div>
</c:if>
<c:forEach var="invoiceHeader" items="${orderMaintForm.invoiceHeaders}">
   <div class="jc_detail_panel_header">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">Invoice Order - <a class="jc_simple_link" href="/${adminBean.contextPath}/admin/inventory/invoiceMaint.do?process=edit&orderHeaderId=${orderMaintForm.orderHeader.orderHeaderId}&invoiceHeaderId=${invoiceHeader.invoiceHeaderId}"/>${invoiceHeader.invoiceNum}</a></span></td>
          </tr>
        </table>
      </div>
   <div class="jc_detail_panel">
<table border="0" cellpadding="3">
  <tr>
    <td width="150"><span class="jc_input_label">Invoice Number</span></td>
    <td>
      ${invoiceHeader.invoiceNum}
    </td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Invoice Date</span></td>
    <td>${invoiceHeader.invoiceDate}</td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Shipping Total</span></td>
    <td>${invoiceHeader.shippingTotal}</td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Invoice Total</span></td>
    <td>${invoiceHeader.invoiceTotal}</td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Status</span></td>
    <td>${invoiceHeader.invoiceStatus}</td>
  </tr>
</table>
<c:if test="${invoiceHeader.paymentTran != null}">
  <hr>
  <table border="0" cellpadding="3">
    <tr>
      <td width="150"><span class="jc_input_label">Authorization Code</span></td>
      <td>${invoiceHeader.paymentTran.authCode}</td>
    </tr>
    <c:if test="${invoiceHeader.paymentTran.paymentReference1 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label">Reference</span></td>
        <td>${invoiceHeader.paymentTran.paymentReference1}</td>
      </tr>
    </c:if>
    <c:if test="${invoiceHeader.paymentTran.paymentReference2 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${invoiceHeader.paymentTran.paymentReference2}</td>
      </tr>
    </c:if>
    <c:if test="${invoiceHeader.paymentTran.paymentReference3 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${invoiceHeader.paymentTran.paymentReference3}</td>
      </tr>
    </c:if>
    <c:if test="${invoiceHeader.paymentTran.paymentReference4 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${invoiceHeader.paymentTran.paymentReference4}</td>
      </tr>
    </c:if>
    <c:if test="${invoiceHeader.paymentTran.paymentReference5 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${invoiceHeader.paymentTran.paymentReference5}</td>
      </tr>
    </c:if>
  </table>
</c:if>
<c:if test="${invoiceHeader.voidPaymentTran != null}">
  <hr>
  <table border="0" cellpadding="3">
    <tr>
      <td width="150"><span class="jc_input_label">Void transaction</span></td>
      <td></td>
    </tr>
    <tr>
      <td width="150"><span class="jc_input_label">Authorization Code</span></td>
      <td>${invoiceHeader.voidPaymentTran.authCode}</td>
    </tr>
    <c:if test="${invoiceHeader.voidPaymentTran.paymentReference1 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label">Reference</span></td>
        <td>${invoiceHeader.voidPaymentTran.paymentReference1}</td>
      </tr>
    </c:if>
    <c:if test="${invoiceHeader.voidPaymentTran.paymentReference2 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${invoiceHeader.voidPaymentTran.paymentReference2}</td>
      </tr>
    </c:if>
    <c:if test="${invoiceHeader.voidPaymentTran.paymentReference3 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${invoiceHeader.voidPaymentTran.paymentReference3}</td>
      </tr>
    </c:if>
    <c:if test="${invoiceHeader.voidPaymentTran.paymentReference4 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${invoiceHeader.voidPaymentTran.paymentReference4}</td>
      </tr>
    </c:if>
    <c:if test="${invoiceHeader.voidPaymentTran.paymentReference5 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${invoiceHeader.voidPaymentTran.paymentReference5}</td>
      </tr>
    </c:if>
  </table>
</c:if>
</div>
</c:forEach>
<c:forEach var="creditHeader" items="${orderMaintForm.creditHeaders}">
   <div class="jc_detail_panel_header">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">Credit Order - <a class="jc_simple_link" href="/${adminBean.contextPath}/admin/inventory/creditMaint.do?process=edit&orderHeaderId=${orderMaintForm.orderHeader.orderHeaderId}&creditHeaderId=${creditHeader.creditHeaderId}"/>${creditHeader.creditNum}</a></span></td>
          </tr>
        </table>
      </div>
   <div class="jc_detail_panel">
<table border="0" cellpadding="3">
  <tr>
    <td width="150"><span class="jc_input_label">Credit Number</span></td>
    <td>
      ${creditHeader.creditNum}
    </td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Credit Date</span></td>
    <td>${creditHeader.creditDate}</td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Shipping Total</span></td>
    <td>${creditHeader.shippingTotal}</td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Invoice Total</span></td>
    <td>${creditHeader.creditTotal}</td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Status</span></td>
    <td>${creditHeader.creditStatus}</td>
  </tr>
</table>
<c:if test="${creditHeader.paymentTran != null}">
  <hr>
  <table border="0" cellpadding="3">
    <tr>
      <td width="150"><span class="jc_input_label">Authorization Code</span></td>
      <td>${creditHeader.paymentTran.authCode}</td>
    </tr>
    <c:if test="${creditHeader.paymentTran.paymentReference1 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label">Reference</span></td>
        <td>${creditHeader.paymentTran.paymentReference1}</td>
      </tr>
    </c:if>
    <c:if test="${creditHeader.paymentTran.paymentReference2 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${creditHeader.paymentTran.paymentReference2}</td>
      </tr>
    </c:if>
    <c:if test="${creditHeader.paymentTran.paymentReference3 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${creditHeader.paymentTran.paymentReference3}</td>
      </tr>
    </c:if>
    <c:if test="${creditHeader.paymentTran.paymentReference4 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${creditHeader.paymentTran.paymentReference4}</td>
      </tr>
    </c:if>
    <c:if test="${creditHeader.paymentTran.paymentReference5 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${creditHeader.paymentTran.paymentReference5}</td>
      </tr>
    </c:if>
  </table>
</c:if>
<c:if test="${creditHeader.voidPaymentTran != null}">
  <hr>
  <table border="0" cellpadding="3">
    <tr>
      <td width="150"><span class="jc_input_label">Void transaction</span></td>
      <td></td>
    </tr>
    <tr>
      <td width="150"><span class="jc_input_label">Authorization Code</span></td>
      <td>${creditHeader.voidPaymentTran.authCode}</td>
    </tr>
    <c:if test="${creditHeader.voidPaymentTran.paymentReference1 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label">Reference</span></td>
        <td>${creditHeader.voidPaymentTran.paymentReference1}</td>
      </tr>
    </c:if>
    <c:if test="${creditHeader.voidPaymentTran.paymentReference2 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${creditHeader.voidPaymentTran.paymentReference2}</td>
      </tr>
    </c:if>
    <c:if test="${creditHeader.voidPaymentTran.paymentReference3 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${creditHeader.voidPaymentTran.paymentReference3}</td>
      </tr>
    </c:if>
    <c:if test="${creditHeader.voidPaymentTran.paymentReference4 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${creditHeader.voidPaymentTran.paymentReference4}</td>
      </tr>
    </c:if>
    <c:if test="${creditHeader.voidPaymentTran.paymentReference5 != ''}">
      <tr>
        <td width="150"><span class="jc_input_label"></span></td>
        <td>${creditHeader.voidPaymentTran.paymentReference5}</td>
      </tr>
    </c:if>
  </table>
</c:if>
</div>
</c:forEach>
<c:forEach var="shipHeader" items="${orderMaintForm.shipHeaders}">
   <div class="jc_detail_panel_header">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">Ship Order - <a class="jc_simple_link" href="/${adminBean.contextPath}/admin/inventory/shipMaint.do?process=edit&orderHeaderId=${orderMaintForm.orderHeader.orderHeaderId}&shipHeaderId=${shipHeader.shipHeaderId}"/>${shipHeader.shipNum}</a></span></td>
          </tr>
        </table>
   </div>
<div class="jc_detail_panel">
<table border="0" cellpadding="3">
  <tr>
    <td width="150"><span class="jc_input_label">Ship Number</span></td>
    <td>
      ${shipHeader.shipNum}
    </td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Ship Date</span></td>
    <td>${shipHeader.shipDate}</td>
  </tr>
  <tr>
    <td><span class="jc_input_label">Status</span></td>
    <td>${shipHeader.shipStatus}</td>
  </tr>
</table>
</div>
</c:forEach>