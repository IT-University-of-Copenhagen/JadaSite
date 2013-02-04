<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<c:set var="adminBean" scope="request" value="${adminBean}"/>
<c:set var="creditMaintForm" scope="request" value="${creditMaintForm}"/>
<c:set var="orderMaintForm" scope="request" value="${creditMaintForm}"/>

<script language="JavaScript">
function submitForm(methodType) {
    document.creditMaintForm.action = '/<c:out value='${adminBean.contextPath}'/>/admin/inventory/creditMaint.do';
    document.creditMaintForm.process.value = methodType;
    document.creditMaintForm.submit();
    return false;
}
function submitBackForm(type) {
    document.creditMaintForm.action = "/<c:out value='${adminBean.contextPath}'/>/admin/inventory/orderListing.do";
    document.creditMaintForm.process.value = type;
    document.creditMaintForm.submit();
    return false;
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
<html:form action="/admin/inventory/creditMaint.do" method="post">
<html:hidden property="process" value="list"/>
<html:hidden property="orderHeaderId"/>
<html:hidden property="invoiceHeaderId"/>
<html:hidden property="creditHeaderId"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>
      <a href="/<c:out value='${adminBean.contextPath}'/>/admin/inventory/orderListing.do?process=back">Order 
      Listing</a> - Credit Maintenance
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
          <td>
	        <c:choose>
	          <c:when test="${creditMaintForm.newCredit}">
	            New Credit
	          </c:when>
	          <c:otherwise> 
	            Credit Order - ${creditMaintForm.creditHeader.creditNum}
	          </c:otherwise>
	        </c:choose>
          </td>
          <td align="right"> 
            <table width="0%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td>
                  <c:if test="${creditMaintForm.allowVoid}">
                  <html:submit property="submitButton" value="Void Credit" styleClass="jc_submit_button" onclick="return submitForm('voidOrder');"/>&nbsp;
                  </c:if>
                  <c:if test="${creditMaintForm.allowCapture}">
                  <html:submit property="submitButton" value="Credit Payment" styleClass="jc_submit_button" onclick="return submitForm('capture');"/>&nbsp;
                  </c:if>
                  <c:if test="${creditMaintForm.editable}">
                  <html:submit property="submitButton" value="Save" styleClass="jc_submit_button" onclick="return submitForm('save');"/>&nbsp;
                  </c:if>
                  <html:submit property="submitButton" value="Cancel" styleClass="jc_submit_button" onclick="return submitBackForm('back');"/>&nbsp;
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <span class="jc_input_error">
      <logic:messagesPresent property="credit" message="true">
        <html:messages property="credit" id="errorText" message="true"> 
        <bean:write name="errorText"/>
        </html:messages> 
      </logic:messagesPresent>
      </span>
      <table width="100%" border="0" cellpadding="3">
        <tr>
          <td width="50%">
            <div class="jc_detail_panel_header">
		      <table width="100%" border="0" cellspacing="0" cellpadding="0">
		        <tr>
		          <td><span class="jc_input_label">Billing Address</span></td>
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
                <td><div align="center">Quantity</div></td>
                <td><div align="right">Amount</div></td>
              </tr>
              <c:set var="single">true</c:set>
	          <c:forEach var="creditDetail" items="${creditMaintForm.creditDetails}">
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
                <td valign="top">
                  <html:hidden indexed="true" name="creditDetail" property="orderItemDetailId"/>
                  <html:hidden indexed="true" name="creditDetail" property="creditDetailId"/>
                  <html:hidden indexed="true" name="creditDetail" property="itemId"/>
                  <html:hidden indexed="true" name="creditDetail" property="itemSkuCd"/>
                  <html:hidden indexed="true" name="creditDetail" property="itemShortDesc"/>
                  <html:hidden indexed="true" name="creditDetail" property="itemTierQty"/>
                  <html:hidden indexed="true" name="creditDetail" property="itemTierPrice"/>
                  <html:hidden indexed="true" name="creditDetail" property="itemOrderQty"/>
                  <html:hidden indexed="true" name="creditDetail" property="itemInvoiceQty"/>
                  <html:hidden indexed="true" name="creditDetail" property="itemCreditQty"/>
                  <html:hidden indexed="true" name="creditDetail" property="itemShipQty"/>
                  <html:hidden indexed="true" name="creditDetail" property="itemCreditAmount"/>
                  ${creditDetail.itemSkuCd}
                </td>
                <td valign="top">
                  ${creditDetail.itemShortDesc}<br>
                  <c:forEach var="orderItemAttribute" items="${creditDetail.orderItemAttributes}">
                  ${orderItemAttribute.customAttribDesc} - ${orderItemAttribute.customAttribValue}<br>
                  </c:forEach>
                </td>
                <td valign="top">${creditDetail.itemTierPrice}</td>
                <td valign="top"><div align="center">${creditDetail.itemOrderQty}</div></td>
                <td valign="top"><div align="center">${creditDetail.itemInvoiceQty}</div></td>
                <td valign="top"><div align="center">${creditDetail.itemCreditQty}</div></td>
                <td valign="top"><div align="center">${creditDetail.itemShipQty}</div></td>
                <td valign="top">
                  <div align="center">
                  <c:choose>
	                  <c:when test="${creditMaintForm.editable}">
	                  <html:text indexed="true" name="creditDetail" styleClass="jc_input_control" size="5" property="inputCreditQty"/>
	                  </c:when>
	                  <c:otherwise>
	                  <html:hidden indexed="true" name="creditDetail" property="inputCreditQty"/>
	                  ${creditDetail.inputCreditQty}
	                  </c:otherwise>
                  </c:choose>
                  <c:if test="${!empty creditDetail.inputCreditQtyError}">
                  <br>
                  <span class="jc_input_error">
                  ${creditDetail.inputCreditQtyError}
                  </span>
                  </c:if>
                  </div>
                </td>
                <td valign="top"><div align="right">${creditDetail.itemCreditAmount}</div></td>
              </tr>
	          </c:forEach>
	          <tr class="order">
                <td></td>
                <td><span class="jc_input_label">Sub-total</span></td>
                <td></td>
                <td><div align="center">${creditMaintForm.creditHeader.itemOrderQty}</div></td>
                <td><div align="center">${creditMaintForm.creditHeader.itemInvoiceQty}</div></td>
                <td><div align="center">${creditMaintForm.creditHeader.itemCreditQty}</div></td>
                <td><div align="center">${creditMaintForm.creditHeader.itemShipQty}</div></td>
                <td><div align="center">${creditMaintForm.creditHeader.inputCreditQty}</div></td>
                <td><div align="right">${creditMaintForm.creditHeader.itemCreditAmount}</div></td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <br>
	  <table width="100%" border="0" cellpadding="3">
        <tr>
          <td width="100%" valign="top">
            <c:if test="${!creditMaintForm.newCredit}">
	        <table width="100%" border="0" cellpadding="3" class="order">
              <tr class="orderHeader">
                <td colspan="2" nowrap>Comments</td>
               </tr>
              <tr>
                <td width="100%">
                  <html:textarea name="creditMaintForm" property="orderTrackingMessage" styleClass="tableContent"/><br>
                  <html:checkbox name="creditMaintForm" property="orderTrackingInternal" styleClass="tableContent">Internal view only</html:checkbox>
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
	          <c:forEach var="orderTracking" items="${creditMaintForm.creditHeader.orderTrackings}">
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
	        </c:if>
	      </td>
	      <td valign="top">
	      <c:choose>
	        <c:when test="${creditMaintForm.editable}">
	        <div class="jc_detail_panel_header">
            <table width="300" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><span class="jc_input_label">Shipping</span></td>
              </tr>
            </table>
            </div>
            <div class="jc_detail_panel">
            <table width="300" border="0">
              <tr>
                <td><span class="jc_input_label">Shipping Amount</span></td>
                <td align="right">
                  <html:text name="creditMaintForm" styleClass="jc_input_control" size="5" property="inputShippingTotal"/>
                  <span class="jc_input_error">
			      <logic:messagesPresent property="inputShippingTotal" message="true">
			        <html:messages property="inputShippingTotal" id="errorText" message="true"> 
			        <br><bean:write name="errorText"/>
			        </html:messages> 
			      </logic:messagesPresent>
			      </span>
                </td>
              </tr>
            </table>
            </div>
            <br>
            </c:when>
            <c:otherwise>
            <html:hidden name="creditMaintForm" property="inputShippingTotal"/>
            </c:otherwise>
          </c:choose>
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
                <td align="right">${creditMaintForm.creditHeader.itemCreditAmount}</td>
              </tr>
              <tr>
                <td><span class="jc_input_label">Shipping</span></td>
                <td align="right">${creditMaintForm.creditHeader.shippingTotal}</td>
              </tr>
              <c:forEach var="creditTax" items="${creditMaintForm.creditTaxes}">
              <tr>
                <td><span class="jc_input_label">${creditTax.taxName}</span></td>
                <td align="right">${creditTax.taxAmount}</td>
              </tr>
              </c:forEach>
              <tr>
                <td><span class="jc_input_label">Total</span></td>
                <td align="right">${creditMaintForm.creditHeader.creditTotal}</td>
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
