<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<c:set var="adminBean" scope="request" value="${adminBean}"/>
<c:set var="shipMaintForm" scope="request" value="${shipMaintForm}"/>
<c:set var="orderMaintForm" scope="request" value="${shipMaintForm}"/>

<script language="JavaScript">
function submitForm(methodType) {
    document.shipMaintForm.action = '/<c:out value='${adminBean.contextPath}'/>/admin/inventory/shipMaint.do';
    document.shipMaintForm.process.value = methodType;
    document.shipMaintForm.submit();
    return false;
}
function submitBackForm(type) {
    document.shipMaintForm.action = "/<c:out value='${adminBean.contextPath}'/>/admin/inventory/orderListing.do";
    document.shipMaintForm.process.value = type;
    document.shipMaintForm.submit();
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
<html:form action="/admin/inventory/shipMaint.do" method="post">
<html:hidden property="process" value="list"/>
<html:hidden property="orderHeaderId"/>
<html:hidden property="shipHeaderId"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>
      <a href="/<c:out value='${adminBean.contextPath}'/>/admin/inventory/orderListing.do?process=back">Order 
      Listing</a> - Ship Maintenance
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
	          <c:when test="${shipMaintForm.newShip}">
	            New Ship
	          </c:when>
	          <c:otherwise> 
	            Ship Order - ${shipMaintForm.shipHeader.shipNum}
	          </c:otherwise>
	        </c:choose>
          </td>
          <td align="right"> 
            <table width="0%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td>
                  <c:if test="${shipMaintForm.allowCapture}">
                  <html:submit property="submitButton" value="Ship Product" styleClass="jc_submit_button" onclick="return submitForm('capture');"/>&nbsp;
                  </c:if>
                  <c:if test="${shipMaintForm.allowVoid}">
                  <html:submit property="submitButton" value="Void" styleClass="jc_submit_button" onclick="return submitForm('voidOrder');"/>&nbsp;
                  </c:if>
                  <c:if test="${shipMaintForm.editable}">
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
      <logic:messagesPresent property="ship" message="true">
        <html:messages property="ship" id="errorText" message="true"> 
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
              </tr>
              <c:set var="single">true</c:set>
	          <c:forEach var="shipDetail" items="${shipMaintForm.shipDetails}">
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
                  <html:hidden indexed="true" name="shipDetail" property="orderItemDetailId"/>
                  <html:hidden indexed="true" name="shipDetail" property="shipDetailId"/>
                  <html:hidden indexed="true" name="shipDetail" property="itemId"/>
                  <html:hidden indexed="true" name="shipDetail" property="itemSkuCd"/>
                  <html:hidden indexed="true" name="shipDetail" property="itemShortDesc"/>
                  <html:hidden indexed="true" name="shipDetail" property="itemTierQty"/>
                  <html:hidden indexed="true" name="shipDetail" property="itemTierPrice"/>
                  <html:hidden indexed="true" name="shipDetail" property="itemOrderQty"/>
                  <html:hidden indexed="true" name="shipDetail" property="itemInvoiceQty"/>
                  <html:hidden indexed="true" name="shipDetail" property="itemCreditQty"/>
                  <html:hidden indexed="true" name="shipDetail" property="itemShipQty"/>
                  ${shipDetail.itemSkuCd}
                </td>
                <td>
                  ${shipDetail.itemShortDesc}<br>
                  <c:forEach var="orderItemAttribute" items="${shipDetail.orderItemAttributes}">
                  ${orderItemAttribute.customAttribDesc} - ${orderItemAttribute.customAttribValue}<br>
                  </c:forEach>
                </td>
                <td valign="top">${shipDetail.itemTierPrice}</td>
                <td valign="top"><div align="center">${shipDetail.itemOrderQty}</div></td>
                <td valign="top"><div align="center">${shipDetail.itemInvoiceQty}</div></td>
                <td valign="top"><div align="center">${shipDetail.itemCreditQty}</div></td>
                <td valign="top"><div align="center">${shipDetail.itemShipQty}</div></td>
                <td valign="top">
                  <div align="center">
                  <c:choose>
	                  <c:when test="${shipMaintForm.editable}">
	                  <html:text indexed="true" name="shipDetail" styleClass="jc_input_control" size="5" property="inputShipQty"/>
	                  </c:when>
	                  <c:otherwise>
	                  <html:hidden indexed="true" name="shipDetail" property="inputShipQty"/>
	                  ${shipDetail.inputShipQty}
	                  </c:otherwise>
                  </c:choose>
                  <c:if test="${!empty shipDetail.inputShipQtyError}">
                  <br>
                  <span class="jc_input_error">
                  ${shipDetail.inputShipQtyError}
                  </span>
                  </c:if>
                  </div>
                </td>
              </tr>
	          </c:forEach>
	          <tr class="order">
                <td></td>
                <td><span class="jc_input_label">Sub-total</span></td>
                <td></td>
                <td><div align="center">${shipMaintForm.shipHeader.itemOrderQty}</div></td>
                <td><div align="center">${shipMaintForm.shipHeader.itemInvoiceQty}</div></td>
                <td><div align="center">${shipMaintForm.shipHeader.itemCreditQty}</div></td>
                <td><div align="center">${shipMaintForm.shipHeader.itemShipQty}</div></td>
                <td><div align="center">${shipMaintForm.shipHeader.inputShipQty}</div></td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <br>
	  <table width="100%" border="0" cellpadding="3">
        <tr>
          <td width="100%" valign="top">
            <c:if test="${!shipMaintForm.newShip}">
	        <table width="100%" border="0" cellpadding="3" class="order">
              <tr class="orderHeader">
                <td colspan="2" nowrap>Comments</td>
               </tr>
              <tr>
                <td width="100%">
                  <html:textarea name="shipMaintForm" property="orderTrackingMessage" styleClass="tableContent"/><br>
                  <html:checkbox name="shipMaintForm" property="orderTrackingInternal" styleClass="tableContent">Internal view only</html:checkbox>
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
	          <c:forEach var="orderTracking" items="${shipMaintForm.shipHeader.orderTrackings}">
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
            <table width="300" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td>&nbsp;</td>
              </tr>
            </table>
	      </td>
	    </tr>
	  </table>
      <br>
      <br>
    </td>
  </tr>
</table>
</html:form>
