<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<c:set var="adminBean" scope="request" value="${adminBean}"/>
<c:set var="orderMaintForm" scope="request" value="${orderMaintForm}"/>

<script language="JavaScript">
function submitForm(methodType) {
    document.orderMaintForm.action = '/<c:out value='${adminBean.contextPath}'/>/admin/inventory/orderMaint.do';
    document.orderMaintForm.process.value = methodType;
    document.orderMaintForm.submit();
    return false;
}
function submitInvoiceForm(methodType) {
    document.orderMaintForm.action = '/<c:out value='${adminBean.contextPath}'/>/admin/inventory/invoiceMaint.do';
    document.orderMaintForm.process.value = methodType;
    document.orderMaintForm.submit();
    return false;
}
function submitShipForm(methodType) {
    document.orderMaintForm.action = '/<c:out value='${adminBean.contextPath}'/>/admin/inventory/shipMaint.do';
    document.orderMaintForm.process.value = methodType;
    document.orderMaintForm.submit();
    return false;
}
function submitBackForm(type) {
    document.orderMaintForm.action = "/<c:out value='${adminBean.contextPath}'/>/admin/inventory/orderListing.do";
    document.orderMaintForm.process.value = type;
    document.orderMaintForm.submit();
    return false;
}

if (${orderMaintForm.orderHeader.shippingQuote == 'true' && orderMaintForm.orderHeader.orderOpen}) {
	YAHOO.util.Event.onContentReady('butShippingQuoteDiv', function() {
		var button = new YAHOO.widget.Button({label: 'Update quote',
		    id: "updateShipping",
		    name: 'updateShipping',
		    container: 'butShippingQuoteDiv' });
		button.on('click', function() {
		  submitForm('updateShipping');
		});
	} );

	function handleShippingValidUntil(type, args, obj) { 
	    var dates = args[0]; 
	    var date = dates[0]; 
	    var year = date[0], month = date[1] + "", day = date[2] + "";
	    if (month.length == 1) month = "0" + month;
	    if (day.length == 1) day = "0" + day;
	    obj.hide();
	    document.orderMaintForm.shippingValidUntil.value =  month + "/" + day + "/" + year;
	}
		
	YAHOO.util.Event.onContentReady('shippingValidUntilContainer', function() {
	  var shippingValidUntil = new YAHOO.widget.Calendar("shippingValidUntil", "shippingValidUntilContainer", { title:"Choose a date:", close:true } );
	  shippingValidUntil.render();
	  YAHOO.util.Event.addListener("shippingValidUntilSwitch", "click", shippingValidUntil.show, shippingValidUntil, true);
	  shippingValidUntil.selectEvent.subscribe(handleShippingValidUntil, shippingValidUntil, true); 
	} );

	YAHOO.util.Event.onContentReady('butShippingQuoteDiv', function() {
		var button = new YAHOO.widget.Button({label: 'Send quote',
		    id: "sendQuote",
		    name: 'sendQuote',
		    container: 'butShippingQuoteDiv' });
		button.on('click', function() {
		  submitForm('sendShippingQuote');
		});
	} );
	
}

YAHOO.util.Event.onContentReady('butOrderTrackingDiv', function() {
	var button = new YAHOO.widget.Button({label: 'Add commment',
	    id: "orderTracking",
	    name: 'butOrderTracking',
	    container: 'butOrderTrackingDiv' });
	button.on('click', function() {
	  submitForm('comment');
	});
} );

YAHOO.util.Event.onContentReady('butUpdateCommentDiv', function() {
	var buttonMenu = [
	      			{ text: "Update", value: 'Update', onclick: { fn: function() {submitForm('updateInternal')} } }
   	];
   	var butContainer = document.getElementById('butUpdateCommentDiv');
   	var menu = new YAHOO.widget.Button({ 
                             type: "menu", 
                             label: "Internal", 
                             name: "menu",
                             menu: buttonMenu, 
                             container: butContainer });
} );


</script>
<html:form action="/admin/inventory/orderMaint.do" method="post">
<html:hidden property="process" value="list"/>
<html:hidden property="orderHeaderId"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>
      <a href="/<c:out value='${adminBean.contextPath}'/>/admin/inventory/orderListing.do?process=back">Order 
      Listing</a> - Order Maintenance
    </td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="400" valign="top">
      <%@ include file="/admin/inventory/orderMaintMap.jsp" %>
    </td>
    <td valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
        <tr>
          <td align="right"> 
            <table width="0%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td>
                  <c:if test="${orderMaintForm.allowCancel}">
                  <html:submit property="submitButton" value="Cancel Order" styleClass="jc_submit_button" onclick="return submitForm('cancel');"/>&nbsp;
                  </c:if>
                  <c:if test="${orderMaintForm.allowInvoice}">
                  <html:submit property="submitButton" value="Invoice" styleClass="jc_submit_button" onclick="return submitInvoiceForm('create');"/>&nbsp;
                  </c:if>
                  <c:if test="${orderMaintForm.allowShip}">
                  <html:submit property="submitButton" value="Ship" styleClass="jc_submit_button" onclick="return submitShipForm('create');"/>&nbsp;
                  </c:if>
                  <html:submit property="submitButton" value="Cancel" styleClass="jc_submit_button" onclick="return submitBackForm('back');"/>&nbsp;
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <table width="100%" border="0" cellpadding="3">
        <tr>
          <td>
            <div class="jc_detail_panel_header">
		      <table width="100%" border="0" cellspacing="0" cellpadding="0">
		        <tr>
		          <td><span class="jc_input_label">Customer contact information</span></td>
		        </tr>
		      </table>
		    </div>
		    <div class="jc_detail_panel">
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td><span class="jc_input_label">Address</span></td>
                <td width="100%"></td>
              </tr>
              <tr>
                <td>Email address</td>
                <td><a href="mailto:${orderMaintForm.orderHeader.custEmail}">${orderMaintForm.orderHeader.custEmail}</a></td>
              </tr>
              <tr>
                <td valign="top">Address</td>
                <td>
				    ${orderMaintForm.custAddress.custFirstName} ${orderMaintForm.custAddress.custLastName}<br>
				    ${orderMaintForm.custAddress.custAddressLine1}<br>
				    <c:if test="${not empty (orderMaintForm.custAddress.custAddressLine2)}">
				      ${orderMaintForm.custAddress.custAddressLine2}<br>
				    </c:if>
				    ${orderMaintForm.custAddress.custCityName} ${orderMaintForm.custAddress.custStateName}<br>
				    ${orderMaintForm.custAddress.custCountryName}<br>
				    ${orderMaintForm.custAddress.custZipCode}
                </td>
              </tr>
              <tr>
                <td valign="top">Phone</td>
                <td>
                  ${orderMaintForm.custAddress.custPhoneNum}
                </td>
              </tr>
              <tr>
                <td valign="top">Fax</td>
                <td>
                  ${orderMaintForm.custAddress.custFaxNum}
                </td>
              </tr>
              <tr>
                <td><span class="jc_input_label">Billing information</span></td>
                <td></td>
              </tr>
              <tr>
                <td>
			      <c:choose>
			        <c:when test="${orderMaintForm.billingAddress.custUseAddress == 'C'}">
			        Same as address
			        </c:when>
			      </c:choose>
                </td>
                <td></td>
              </tr>
              <c:if test="${orderMaintForm.billingAddress.custUseAddress == 'O'}">
              <tr>
                <td valign="top">Address</td>
                <td>
				    ${orderMaintForm.billingAddress.custFirstName} ${orderMaintForm.billingAddress.custLastName}<br>
				    ${orderMaintForm.billingAddress.custAddressLine1}<br>
				    <c:if test="${not empty (orderMaintForm.billingAddress.custAddressLine2)}">
				      ${orderMaintForm.billingAddress.custAddressLine2}<br>
				    </c:if>
				    ${orderMaintForm.billingAddress.custCityName} ${orderMaintForm.billingAddress.custStateName}<br>
				    ${orderMaintForm.billingAddress.custCountryName}<br>
				    ${orderMaintForm.billingAddress.custZipCode}
                </td>
              </tr>
              <tr>
                <td valign="top">Phone</td>
                <td>
                  ${orderMaintForm.billingAddress.custPhoneNum}
                </td>
              </tr>
              <tr>
                <td valign="top">Fax</td>
                <td>
                  ${orderMaintForm.billingAddress.custFaxNum}
                </td>
              </tr>
              </c:if>
              
              <tr>
                <td><span class="jc_input_label">Shipping information</span></td>
                <td></td>
              </tr>
              <tr>
                <td>
			      <c:choose>
			        <c:when test="${orderMaintForm.shippingAddress.custUseAddress == 'C'}">
			        Same as address
			        </c:when>
			        <c:when test="${orderMaintForm.shippingAddress.custUseAddress == 'B'}">
			        Same as billing information
			        </c:when>
			      </c:choose>
                </td>
                <td></td>
              </tr>
              <c:if test="${orderMaintForm.shippingAddress.custUseAddress == 'O'}">
              <tr>
                <td valign="top">Address</td>
                <td>
				    ${orderMaintForm.shippingAddress.custFirstName} ${orderMaintForm.shippingAddress.custLastName}<br>
				    ${orderMaintForm.shippingAddress.custAddressLine1}<br>
				    <c:if test="${not empty (orderMaintForm.shippingAddress.custAddressLine2)}">
				      ${orderMaintForm.shippingAddress.custAddressLine2}<br>
				    </c:if>
				    ${orderMaintForm.shippingAddress.custCityName} ${orderMaintForm.shippingAddress.custStateName}<br>
				    ${orderMaintForm.shippingAddress.custCountryName}<br>
				    ${orderMaintForm.shippingAddress.custZipCode}
                </td>
              </tr>
              <tr>
                <td valign="top">Phone</td>
                <td>
                  ${orderMaintForm.shippingAddress.custPhoneNum}
                </td>
              </tr>
              <tr>
                <td valign="top">Fax</td>
                <td>
                  ${orderMaintForm.shippingAddress.custFaxNum}
                </td>
              </tr>
              </c:if>
              
            </table>
            </div>
          </td>
        </tr>
      </table>
      <br>
      <table width="100%" border="0" cellpadding="3">
        <tr>
          <td>
            <table width="100%" border="0" cellpadding="3" class="order">
              <tr class="orderHeader">
                <td>Item Sku Code</td>
                <td>Description</td>
                <td>Price</td>
                <td><div align="center">Ordered</div></td>
                <td><div align="center">Invoiced</div></td>
                <td><div align="center">Credit</div></td>
                <td><div align="center">Shipped</div></td>
                <td><div align="right">Amount</div></td>
                <td><div align="right">Discount</div></td>
                <td><div align="right">Total</div></td>
              </tr>
              <c:set var="single">true</c:set>
	          <c:forEach var="orderItemDetail" items="${orderMaintForm.orderItemDetails}">
	          <c:choose> 
	            <c:when test="${single == 'true'}">
	              <c:set var="single">false</c:set>
	              <c:set var="className">order</c:set>
	            </c:when>
	            <c:otherwise> 
	              <c:set var="single">true</c:set>
	              <c:set var="className">order1</c:set>
	            </c:otherwise>
	          </c:choose>
	          <tr class="${className}">
                <td valign="top">${orderItemDetail.itemSkuCd}</td>
                <td>
                  ${orderItemDetail.itemShortDesc}<br>
                  <c:forEach var="orderItemAttribute" items="${orderItemDetail.orderItemAttributes}">
                  ${orderItemAttribute.customAttribDesc} - ${orderItemAttribute.customAttribValue}<br>
                  </c:forEach>
                </td>
                <td valign="top">${orderItemDetail.itemTierPrice}</td>
                <td valign="top"><div align="center">${orderItemDetail.itemOrderQty}</div></td>
                <td valign="top"><div align="center">${orderItemDetail.itemInvoiceQty}</div></td>
                <td valign="top"><div align="center">${orderItemDetail.itemCreditQty}</div></td>
                <td valign="top"><div align="center">${orderItemDetail.itemShipQty}</div></td>
                <td valign="top"><div align="right">${orderItemDetail.itemDetailAmount}</div></td>
                <td valign="top"><div align="right">${orderItemDetail.itemDetailDiscountAmount}</div></td>
                <td valign="top"><div align="right">${orderItemDetail.itemDetailSubTotal}</div></td>
              </tr>
	          </c:forEach>
	          <tr class="order">
                <td></td>
                <td><span class="jc_input_label">Sub-total</span></td>
                <td></td>
                <td><div align="center">${orderMaintForm.orderHeader.itemOrderQty}</div></td>
                <td><div align="center">${orderMaintForm.orderHeader.itemInvoiceQty}</div></td>
                <td><div align="center">${orderMaintForm.orderHeader.itemCreditQty}</div></td>
                <td><div align="center">${orderMaintForm.orderHeader.itemShipQty}</div></td>
                <td><div align="right">${orderMaintForm.orderHeader.itemDetailAmount}</div></td>
                <td><div align="right">${orderMaintForm.orderHeader.itemDetailDiscountAmount}</div></td>
                <td><div align="right">${orderMaintForm.orderHeader.itemDetailSubTotal}</div></td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <br>
	  <table width="100%" border="0" cellpadding="3">
        <tr>
          <td width="100%" valign="top">
            <c:if test="${fn:length(orderMaintForm.orderOtherDetails) > 0}">
            <table width="100%" border="0" cellpadding="3" class="order">
              <tr class="orderHeader">
                <td width="20%" nowrap>Coupon Code</td>
                <td width="60%">Description</td>
                <td width="20%"><div align="right">Amount</div></td>
              </tr>
              <c:set var="single">true</c:set>
	          <c:forEach var="orderOtherDetail" items="${orderMaintForm.orderOtherDetails}">
	          <c:choose> 
	            <c:when test="${single == 'true'}">
	              <c:set var="single">false</c:set>
	              <c:set var="className">order</c:set>
	            </c:when>
	            <c:otherwise> 
	              <c:set var="single">true</c:set>
	              <c:set var="className">order1</c:set>
	            </c:otherwise>
	          </c:choose>
	          <tr class="${className}">
                <td>${orderOtherDetail.orderOtherDetailNum}</td>
                <td>${orderOtherDetail.orderOtherDetailDesc}</td>
                <td><div align="right">${orderOtherDetail.orderOtherDetailAmount}</div></td>
			  </tr>
	          </c:forEach>
	          <tr class="order">
                <td></td>
                <td><span class="jc_input_label">Sub-total</span></td>
                <td><div align="right">${orderMaintForm.orderHeader.orderOtherDetailAmount}</div></td>
			  </tr>
	        </table>
	        <br>
	        <br>
	        </c:if>
	        <table width="100%" border="0" cellpadding="3" class="order">
              <tr class="orderHeader">
                <td colspan="2" nowrap>Comments</td>
               </tr>
              <tr>
                <td width="100%">
                  <html:textarea name="orderMaintForm" property="orderTrackingMessage" styleClass="tableContent"/><br>
                  <html:checkbox name="orderMaintForm" property="orderTrackingInternal" styleClass="tableContent">Internal view only</html:checkbox>
                </td>
                <td width="0" align="right" valign="top"><div id="butOrderTrackingDiv"></div></td>
               </tr>
            </table>
            <br>
            <table width="100%" border="0" cellpadding="3" class="order">
              <tr class="orderHeader">
                <td width="60%" nowrap>Comments</td>
                <td width="20%">
                  <div id="butUpdateCommentDiv" align="center"></div>
                </td>
                <td width="20%">Date</td>
              </tr>
              <c:set var="single">true</c:set>
	          <c:forEach var="orderTracking" items="${orderMaintForm.orderHeader.orderTrackings}">
	          <c:choose> 
	            <c:when test="${single == 'true'}">
	              <c:set var="single">false</c:set>
	              <c:set var="className">order</c:set>
	            </c:when>
	            <c:otherwise> 
	              <c:set var="single">true</c:set>
	              <c:set var="className">order1</c:set>
	            </c:otherwise>
	          </c:choose>
	          <tr class="${className}">
                <td>
                  <html:hidden indexed="true" name="orderTracking" property="orderTrackingId"/>
                  <html:hidden indexed="true" name="orderTracking" property="recUpdateDatetime"/>
                  ${orderTracking.orderTrackingMessage}
                </td>
                <td>
                  <div align="center">
                  <html:checkbox indexed="true" name="orderTracking" property="orderTrackingInternal" value="Y" styleClass="tableContent"/>
                  </div>
                </td>
                <td class="${className}" nowrap>${orderTracking.recUpdateDatetime}</td>
			  </tr>
	          </c:forEach>
	        </table>
	      </td>
	      <td valign="top">
            <div class="jc_detail_panel_header">
            <table width="300" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><span class="jc_input_label">Summary</span></td>
              </tr>
            </table>
            </div>
            <div class="jc_detail_panel">
            <table width="300" border="0">
              <tr>
                <td><span class="jc_input_label">Item Total</span></td>
                <td align="right">${orderMaintForm.orderHeader.orderPriceTotal}</td>
              </tr>
              <tr>
                <td><span class="jc_input_label">Item Discount Total</span></td>
                <td align="right">${orderMaintForm.orderHeader.orderPriceDiscountTotal}</td>
              </tr>
              <tr>
                <td><span class="jc_input_label">Sub-total</span></td>
                <td align="right">${orderMaintForm.orderHeader.orderSubTotal}</td>
              </tr>
              <tr>
                <td><span class="jc_input_label">Shipping</span></td>
                <td align="right">${orderMaintForm.orderHeader.shippingTotal}</td>
              </tr>
              <tr>
                <td><span class="jc_input_label">Shipping Discount</span></td>
                <td align="right">${orderMaintForm.orderHeader.shippingDiscountTotal}</td>
              </tr>
              <c:forEach var="orderTax" items="${orderMaintForm.orderTaxes}">
              <tr>
                <td><span class="jc_input_label">${orderTax.taxName}</span></td>
                <td align="right">${orderTax.taxAmount}</td>
              </tr>
              </c:forEach>
              <tr>
                <td><span class="jc_input_label">Total</span></td>
                <td align="right">${orderMaintForm.orderHeader.orderTotal}</td>
              </tr>
            </table>
            </div>
	      </td>
	    </tr>
	  </table>
      <br>
      <br>
     
    </td>
  </tr>
</table>
</html:form>
