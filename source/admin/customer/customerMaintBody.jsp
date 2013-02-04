<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="customerMaintForm" scope="request" value="${customerMaintForm}"/>
<script language="JavaScript">
function submitForm(type) {
    document.customerMaintForm.process.value = type;
    document.customerMaintForm.submit();
    return false;
}

function submitCancelForm() {
    document.customerMaintForm.action = "/${adminBean.contextPath}/admin/customer/customerListing.do";
    document.customerMaintForm.process.value = "back";
    document.customerMaintForm.submit();
    return false;
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

YAHOO.util.Event.onContentReady('billingContainer', function() {
	var value = document.customerMaintForm.billingCustUseAddress.value;
	if (value == 'O') {
		document.getElementById('billingContainer').style.display = 'block';
	}
} );

YAHOO.util.Event.onContentReady('shippingContainer', function() {
	var value = document.customerMaintForm.shippingCustUseAddress.value;
	if (value == 'O') {
		document.getElementById('shippingContainer').style.display = 'block';
	}
} );

</script>
<html:form method="post" action="/admin/customer/customerMaint"> 
<html:hidden property="process" value=""/> 
<html:hidden property="custId"/> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - <a href="/<c:out value='${adminBean.contextPath}'/>/admin/customer/customerListing.do?process=back">Customer 
      Listing</a> - Customer Maintenance</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr>
  	<td width="300" valign="top">
	<div class="jc_detail_panel_header">
	  <table width="300" border="0" cellspacing="0" cellpadding="0">
	    <tr>
	      <td><span class="jc_input_label">Customer Address</span></td>
	    </tr>
	  </table>
	</div>
	<div class="jc_detail_panel">
      <table width="300" border="0" cellspacing="0" cellpadding="5">
        <tr>
          <td class="jc_input_label">First Name</td>
        </tr>
        <tr>
          <td class="jc_input_control">
          <layout:text layout="false" property="custFirstName" size="40"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Last Name</td>
        </tr>
        <tr>
          <td class="jc_input_control">
          <layout:text layout="false" property="custLastName" size="40"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Address</td>
        </tr>
        <tr>
          <td class="jc_input_control">
          <layout:text layout="false" property="custAddressLine1" size="40"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_control">
          <layout:text layout="false" property="custAddressLine2" size="40"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">City</td>
        </tr>
        <tr>
          <td class="jc_input_control">
          <layout:text layout="false" property="custCityName" size="25"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">State / Province</td>
        </tr>
        <tr>
          <td class="jc_input_control">
			<html:select property="custStateCode" styleClass="tableContent"> 
			  <html:optionsCollection property="states" label="label" value="value"/> 
			</html:select> 
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Country</td>
        </tr>
        <tr>
          <td class="jc_input_control">
			<html:select property="custCountryCode" styleClass="tableContent"> 
			  <html:optionsCollection property="countries" label="label" value="value"/> 
			</html:select> 
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Zip / Postal Code</td>
        </tr>
        <tr>
          <td class="jc_input_control">
			<layout:text layout="false" property="custZipCode" size="10"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Phone</td>
        </tr>
        <tr>
          <td class="jc_input_control">
			<layout:text layout="false" property="custPhoneNum" size="15"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Fax</td>
        </tr>
        <tr>
          <td class="jc_input_control">
			<layout:text layout="false" property="custFaxNum" size="15"/>
          </td>
        </tr>
      </table>
	</div>
	<div class="jc_detail_panel_header">
	  <table width="300" border="0" cellspacing="0" cellpadding="0">
	    <tr>
	      <td><span class="jc_input_label">Billing Address</span></td>
	      <td>
	      <div align="right">
		    <html:select property="billingCustUseAddress" onchange="selectBilling(this.value)"> 
		      <html:option value="C">Same as customer address</html:option>
		      <html:option value="O">Choose a different address</html:option>
		    </html:select>
		  </div>
	      </td>
	    </tr>
	  </table>
	</div>
	<div class="jc_detail_panel">
      <div id="billingContainer" style="display: none">
      <table width="300" border="0" cellspacing="0" cellpadding="5">
        <tr>
          <td class="jc_input_label">First Name</td>
        </tr>
        <tr>
          <td class="jc_input_control">
          <layout:text layout="false" property="billingCustFirstName" size="40"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Last Name</td>
        </tr>
        <tr>
          <td class="jc_input_control">
          <layout:text layout="false" property="billingCustLastName" size="40"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Address</td>
        </tr>
        <tr>
          <td class="jc_input_control">
          <layout:text layout="false" property="billingCustAddressLine1" size="40"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_control">
          <layout:text layout="false" property="billingCustAddressLine2" size="40"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">City</td>
        </tr>
        <tr>
          <td class="jc_input_control">
          <layout:text layout="false" property="billingCustCityName" size="25"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">State / Province</td>
        </tr>
        <tr>
          <td class="jc_input_control">
			<html:select property="billingCustStateCode" styleClass="tableContent"> 
			  <html:optionsCollection property="states" label="label" value="value"/> 
			</html:select> 
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Country</td>
        </tr>
        <tr>
          <td class="jc_input_control">
			<html:select property="billingCustCountryCode" styleClass="tableContent"> 
			  <html:optionsCollection property="countries" label="label" value="value"/> 
			</html:select> 
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Zip / Postal Code</td>
        </tr>
        <tr>
          <td class="jc_input_control">
			<layout:text layout="false" property="billingCustZipCode" size="10"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Phone</td>
        </tr>
        <tr>
          <td class="jc_input_control">
			<layout:text layout="false" property="billingCustPhoneNum" size="15"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Fax</td>
        </tr>
        <tr>
          <td class="jc_input_control">
			<layout:text layout="false" property="billingCustFaxNum" size="15"/>
          </td>
        </tr>
      </table>
      </div>
	</div>
	<div class="jc_detail_panel_header">
	  <table width="300" border="0" cellspacing="0" cellpadding="0">
	    <tr>
	      <td><span class="jc_input_label">Shipping Address</span></td>
	      <td>
	      <div align="right">
	        <html:select property="shippingCustUseAddress" onchange="selectShipping(this.value)"> 
	          <html:option value="C">Same as customer address</html:option>
	          <html:option value="B">Same as billing address</html:option>
	          <html:option value="O">Choose a different address</html:option>
	        </html:select>
		  </div>
	      </td>
	    </tr>
	  </table>
	</div>
	<div class="jc_detail_panel">
      <div id="shippingContainer" style="display: none">
      <table width="300" border="0" cellspacing="0" cellpadding="5">
        <tr>
          <td class="jc_input_label">First Name</td>
        </tr>
        <tr>
          <td class="jc_input_control">
          <layout:text layout="false" property="shippingCustFirstName" size="40"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Last Name</td>
        </tr>
        <tr>
          <td class="jc_input_control">
          <layout:text layout="false" property="shippingCustLastName" size="40"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Address</td>
        </tr>
        <tr>
          <td class="jc_input_control">
          <layout:text layout="false" property="shippingCustAddressLine1" size="40"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_control">
          <layout:text layout="false" property="shippingCustAddressLine2" size="40"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">City</td>
        </tr>
        <tr>
          <td class="jc_input_control">
          <layout:text layout="false" property="shippingCustCityName" size="25"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">State / Province</td>
        </tr>
        <tr>
          <td class="jc_input_control">
			<html:select property="shippingCustStateCode" styleClass="tableContent"> 
			  <html:optionsCollection property="states" label="label" value="value"/> 
			</html:select> 
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Country</td>
        </tr>
        <tr>
          <td class="jc_input_control">
			<html:select property="shippingCustCountryCode" styleClass="tableContent"> 
			  <html:optionsCollection property="countries" label="label" value="value"/> 
			</html:select> 
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Zip / Postal Code</td>
        </tr>
        <tr>
          <td class="jc_input_control">
			<layout:text layout="false" property="shippingCustZipCode" size="10"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Phone</td>
        </tr>
        <tr>
          <td class="jc_input_control">
			<layout:text layout="false" property="shippingCustPhoneNum" size="15"/>
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Fax</td>
        </tr>
        <tr>
          <td class="jc_input_control">
			<layout:text layout="false" property="shippingCustFaxNum" size="15"/>
          </td>
        </tr>
      </table>
      </div>
	</div>
	<div class="jc_detail_panel_header">
	  <table width="300" border="0" cellspacing="0" cellpadding="0">
	    <tr>
	      <td><span class="jc_input_label">Credit Card information</span></td>
	    </tr>
	  </table>
	</div>
	<div class="jc_detail_panel">
      <table width="300" border="0" cellspacing="0" cellpadding="5">
        <tr>
          <td class="jc_input_label">Credit card</td>
        </tr>
        <tr>
          <td class="jc_input_control">
            <html:select property="creditCardId" styleClass="tableContent"> 
              <html:optionsCollection property="creditCards" label="label" value="value"/> 
            </html:select> 
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Full name</td>
        </tr>
        <tr>
          <td class="jc_input_control">
            <layout:text layout="false" property="custCreditCardFullName" size="40" mode="E,E,E" styleClass="tableContent"/> 
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Card number</td>
        </tr>
        <tr>
          <td class="jc_input_control">
            <layout:text layout="false" property="custCreditCardNum" size="20" mode="E,E,E" styleClass="tableContent"/> 
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Expiry Month</td>
        </tr>
        <tr>
          <td class="jc_input_control">
            <html:select property="custCreditCardExpiryMonth" styleClass="tableContent" style="width: 50px"> 
              <html:optionsCollection property="expiryMonths" label="label" value="value"/> 
            </html:select> 
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Expiry Year</td>
        </tr>
        <tr>
          <td class="jc_input_control">
            <html:select property="custCreditCardExpiryYear" styleClass="tableContent" style="width: 50px"> 
              <html:optionsCollection property="expiryYears" label="label" value="value"/> 
            </html:select> 
          </td>
        </tr>
        <tr>
          <td class="jc_input_label">Verification Number</td>
        </tr>
        <tr>
          <td class="jc_input_control">
            <layout:text layout="false" property="custCreditCardVerNum" size="10" mode="E,E,E" styleClass="tableContent"/> 
          </td>
        </tr>
      </table>
	</div>
    </td>
    <td width="100%" valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td align="right" class="jc_panel_table_header"> 
            <table width="0%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><html:submit property="submitButton" value="Save" styleClass="jc_submit_button" onclick="return submitForm('save');"/>&nbsp;</td>
                <td><html:submit property="submitButton" value="Cancel" styleClass="jc_submit_button" onclick="return submitCancelForm();"/>&nbsp;</td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <table width="400" border="0" cellspacing="0" cellpadding="5">
        <tr>
          <td>
		  <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td class="jc_input_label">Sub-site</td>
              </tr>
              <tr>
                <td class="jc_input_control">
                  <html:hidden property="siteName" value=""/> 
                  ${customerMaintForm.siteName}
                </td>
              </tr>
              <tr>
                <td class="jc_input_label">Email</td>
              </tr>
              <tr>
                <td class="jc_input_control">
                  <layout:text layout="false" property="custEmail" size="50" mode="R,R,R" styleClass="tableContent"/>
                </td>
              </tr>
			  <tr>
				<td class="jc_input_label">Public Name</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
				  <layout:text layout="false" property="custPublicName" size="20" mode="E,E,E" styleClass="tableContent"/> 
				</td>
			  </tr>
			  <tr>
				<td class="jc_input_label">Password</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
		          <layout:password layout="false" property="custPassword" size="20" mode="E,E,E" styleClass="tableContent"/>
				  <span class="jc_input_error">
				  <logic:messagesPresent property="custPassword" message="true"> 
				  <html:messages property="custPassword" id="errorText" message="true"> 
				  <br><bean:write name="errorText"/></html:messages> </logic:messagesPresent>
				  </span>
				</td>
			  </tr>
			  <tr>
				<td class="jc_input_label">Verify password</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
				  <layout:password layout="false" property="custPassword1" size="20" mode="E,E,E" styleClass="tableContent"/> 
          		</td>
			  </tr>
	          <tr class="jc_input_table_row"> 
	            <td class="jc_input_label">Customer Class</td>
	          </tr>
	          <tr>
	            <td class="jc_input_label">
	              <html:select property="custClassId"> 
	                <html:optionsCollection property="custClasses" label="label"/>
	              </html:select>
	            </td>
	          </tr>
			  <tr>
				<td class="jc_input_label">Source</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
				  <html:hidden property="custSource"/>
				  ${customerMaintForm.custSource}
				</td>
			  </tr>
              <tr>
                <td class="jc_input_label">Source Reference</td>
              </tr>
              <tr>
                <td class="jc_input_control">
                  <html:hidden property="custSourceRef"/>
                  ${customerMaintForm.custSourceRef}
                </td>
              </tr>
              <tr>
                <td class="jc_input_label">Comments</td>
              </tr>
              <tr>
                <td class="jc_input_control">
                  <layout:textarea layout="false" property="custComments" size="30" rows="5" mode="E,E,E" styleClass="tableContent"/>
                </td>
              </tr>
              <tr>
                <td class="jc_input_label">Active</td>
              </tr>
              <tr>
                <td class="jc_input_control"><layout:checkbox layout="false" property="active" mode="E,E,E" value="Y"/></td>
              </tr>
		    </table>
		  </td>
        </tr>
      </table>
      <table width="400" border="0" cellspacing="0" cellpadding="3">
        <tr>
          <td nowrap class="jc_input_label">Order number</td>
          <td nowrap class="jc_input_label">Order date</td>
          <td nowrap class="jc_input_label" width="80px"><div align="right">Price total</div></td>
          <td nowrap class="jc_input_label" width="80px"><div align="right">Tax</div></td>
          <td nowrap class="jc_input_label" width="80px"><div align="right">Shipping</div></td>
          <td nowrap class="jc_input_label" width="80px"><div align="right">Order total</div></td>
          <td nowrap class="jc_input_label" width="80px"><div align="center">Status</div></td>
        </tr>
        <c:forEach var="order" items="${customerMaintForm.orders}">
        <tr>
          <td nowrap width="100px">
            <a href="/${adminBean.contextPath}/admin/inventory/orderMaint.do?process=edit&custId=${customerMaintForm.custId}&orderHeaderId=${order.orderHeaderId}" styleClass="jc_submit_link">${order.orderNum}</a> 
          </td>
          <td nowrap width="180px">${order.orderDate}</td>
          <td nowrap><div align="right" width="80px">${order.orderSubTotal}</div></td>
          <td nowrap><div align="right" width="80px">${order.orderTaxTotal}</div></td>
          <td nowrap><div align="right" width="80px">${order.shippingTotal}</div></td>
          <td nowrap><div align="right" width="80px">${order.orderTotal}</div></td>
          <td nowrap><div align="center">${order.orderStatusDesc}</div></td>
        </tr>
        </c:forEach>
      </table>
    </td>
  </tr>
</table></html:form>