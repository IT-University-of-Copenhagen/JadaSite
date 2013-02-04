<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<script type="text/javascript" language="JavaScript"><!--
function submitForm(type) {
    document.myAccountShipInfoActionForm.process.value = type;
    document.myAccountShipInfoActionForm.submit();
}

function toggleBilling() {
	var form = document.myAccountShipInfoActionForm;
	var disabled = false;
	if (form.useBilling.checked) {
		disabled = true;
	}

	form.shippingCustPrefix.disabled = disabled;
	form.shippingCustFirstName.disabled = disabled;
	form.shippingCustMiddleName.disabled = disabled;
	form.shippingCustLastName.disabled = disabled;
	form.shippingCustSuffix.disabled = disabled;
	form.shippingCustAddressLine1.disabled = disabled;
	form.shippingCustAddressLine2.disabled = disabled;
	form.shippingCustCityName.disabled = disabled;
	form.shippingCustStateCode.disabled = disabled;
	form.shippingCustCountryCode.disabled = disabled;
	form.shippingCustZipCode.disabled = disabled;
	form.shippingCustPhoneNum.disabled = disabled;
	form.shippingCustFaxNum.disabled = disabled;
}

//--></script>
<div class="jc_page_body_container">
<html:form method="post" action="/myaccount/shipinfo/myAccountShipInfo.do?process=update">
<html:hidden property="process" value=""/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_table_normal">
  <tr>
    <td valign="top" width="0">
      <jsp:include page="/myaccount/myAccountNav.jsp" />
    </td>
    <td valign="top" width="100%">
      <div class="jc_panel_container">
      <table width="300" cellspacing="0" cellpadding="3">
        <tr>
          <td valign="top">
		      <table width="300" cellspacing="0" cellpadding="3" class="jc_table_normal">
		        <tr>
		          <td>
		          <span class="jc_text_large_title">Billing information</span>
		          </td>
		        </tr>
		        <tr>
		          <td>
		            <logic:messagesPresent property="message" message="true">
		              <html:messages property="message" id="m" message="true">
		              <span class="jc_text_normal jc_blue">
		                <bean:write name="m"/>
		              </span>
		              </html:messages>
		            </logic:messagesPresent>
		          </td>
		        </tr>
		        <tr>
		          <td>
		          <span class="jc_text_normal">To speed up your checkout process.  Please complete your billing information.
		          You should also ensure all information is consistent with information on file at your financial institution.<br><br>
		          </span>
		          </td>
		        </tr>
		      </table>
           </td>
          <td valign="top">
		      <table width="300" cellspacing="0" cellpadding="0" class="jc_table_normal">
		        <tr>
		          <td>
		          <span class="jc_text_large_title">Shipping Information</span>
		          </td>
		        </tr>
		        <tr>
		          <td>
		          </td>
		        </tr>
		        <tr>
		          <td>
		          <div class="jc_input_normal">
		          <html:checkbox property="useBilling" onchange="toggleBilling();"></html:checkbox>
		          Same as Billing Address
		          </div>
		          <br>
		          <span class="jc_text_normal">
		          If checking out from PayPal, its shipping address will be used.
		          </span>
		          </td>
		        </tr>
		      </table>
           </td>
        </tr>
        <tr>
          <td valign="top">
		      <table width="300" cellspacing="0" cellpadding="3" class="jc_table_normal">
		        <tr>
		          <td><span class="jc_text_normal_title">Prefix</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="billingCustPrefix" size="20"/></div>
		          </td>
		        </tr>
		        <tr>
		          <td><span class="jc_text_normal_title">First name</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="billingCustFirstName" size="40"/></div>
		          </td>
		        </tr>
		        <tr>
		          <td><span class="jc_text_normal_title">Middle name</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="billingCustMiddleName" size="40"/></div>
		          </td>
		        </tr>
		        <tr>
		          <td><span class="jc_text_normal_title">Last name</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="billingCustLastName" size="40"/></div>
		          </td>
		        </tr>
		        <tr>
		          <td><span class="jc_text_normal_title">Suffix</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="billingCustSuffix" size="20"/></div>
		          </td>
		        </tr>
		
		        <tr>
		          <td><span class="jc_text_normal_title">Address</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="billingCustAddressLine1" size="30"/></div>
		          </td>
		        </tr>
		        <tr>
		          <td>
		            <logic:messagesPresent property="billingCustAddressLine1" message="true">
		              <html:messages property="billingCustAddressLine1" id="errorText" message="true">
		              <span class="jc_alert">
		                <bean:write name="errorText"/>
		              </span>
		              </html:messages>
		            </logic:messagesPresent>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="billingCustAddressLine2" size="30"/></div>
		          </td>
		        </tr>
		        <tr>
		          <td>
		            <logic:messagesPresent property="billingCustAddressLine2" message="true">
		              <html:messages property="billingCustAddressLine1" id="errorText" message="true">
		              <span class="jc_alert">
		                <bean:write name="errorText"/>
		              </span>
		              </html:messages>
		            </logic:messagesPresent>
		          </td>
		        </tr>
		        <tr> 
		          <td><span class="jc_text_normal_title">City</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="billingCustCityName" size="20" maxlength="40"/></div>
		          </td>
		        </tr>
		        <tr>
		          <td><span class="jc_text_normal_title">State/Province</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <html:select property="billingCustStateCode" styleClass="jc_input_normal" style="width: 200px"> 
		              <html:optionsCollection property="states" label="label" value="value"/> 
		            </html:select> 
		          </td>
		        </tr>
		        <tr> 
		          <td><span class="jc_text_normal_title">Country</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <html:select property="billingCustCountryCode" styleClass="jc_input_normal" style="width: 200px"> 
		              <html:optionsCollection property="countries" label="label" value="value"/> 
		            </html:select> 
		          </td>
		        </tr>
		        <tr> 
		          <td><span class="jc_text_normal_title">Zip/Postal</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="billingCustZipCode" size="10"/></div>
		          </td>
		        </tr>
		        <tr>
		          <td>
		            <logic:messagesPresent property="billingCustZipCode" message="true">
		              <html:messages property="billingCustZipCode" id="errorText" message="true">
		              <span class="jc_alert">
		                <bean:write name="errorText"/>
		              </span>
		              </html:messages>
		            </logic:messagesPresent>
		          </td>
		        </tr>
		        <tr> 
		          <td><span class="jc_text_normal_title">Phone Number</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="billingCustPhoneNum" size="15"/></div>
		          </td>
		        </tr>
		        <tr>
		          <td>
		            <logic:messagesPresent property="billingCustPhoneNum" message="true">
		              <html:messages property="billingCustPhoneNum" id="errorText" message="true">
		              <span class="jc_alert">
		                <bean:write name="errorText"/>
		              </span>
		              </html:messages>
		            </logic:messagesPresent>
		          </td>
		        </tr>
		        <tr> 
		          <td><span class="jc_text_normal_title">Fax Number</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="billingCustFaxNum" size="15"/></div>
		          </td>
		        </tr>
		        <tr>
		          <td>
		            <logic:messagesPresent property="billingCustFaxNum" message="true">
		              <html:messages property="billingCustFaxNum" id="errorText" message="true">
		              <span class="jc_alert">
		                <bean:write name="errorText"/>
		              </span>
		              </html:messages>
		            </logic:messagesPresent>
		          </td>
		        </tr>
		      </table>
           </td>
          <td valign="top">
		      <table width="300" cellspacing="0" cellpadding="3" class="jc_table_normal">
		        <tr>
		          <td><span class="jc_text_normal_title">Prefix</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="shippingCustPrefix" size="20" disabled="${myAccountShipInfoActionForm.useBilling}"/></div>
		          </td>
		        </tr>
		        <tr>
		          <td><span class="jc_text_normal_title">First name</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="shippingCustFirstName" size="40" disabled="${myAccountShipInfoActionForm.useBilling}"/></div>
		          </td>
		        </tr>
		        <tr>
		          <td><span class="jc_text_normal_title">Middle name</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="shippingCustMiddleName" size="40" disabled="${myAccountShipInfoActionForm.useBilling}"/></div>
		          </td>
		        </tr>
		        <tr>
		          <td><span class="jc_text_normal_title">Last name</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="shippingCustLastName" size="40" disabled="${myAccountShipInfoActionForm.useBilling}"/></div>
		          </td>
		        </tr>
		        <tr>
		          <td><span class="jc_text_normal_title">Suffix</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="shippingCustSuffix" size="20" disabled="${myAccountShipInfoActionForm.useBilling}"/></div>
		          </td>
		        </tr>
		
		        <tr>
		          <td><span class="jc_text_normal_title">Address</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="shippingCustAddressLine1" size="30" disabled="${myAccountShipInfoActionForm.useBilling}"/></div>
		          </td>
		        </tr>
		        <tr>
		          <td>
		            <logic:messagesPresent property="shippingCustAddressLine1" message="true">
		              <html:messages property="shippingCustAddressLine1" id="errorText" message="true">
		              <span class="jc_alert">
		                <bean:write name="errorText"/>
		              </span>
		              </html:messages>
		            </logic:messagesPresent>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="shippingCustAddressLine2" size="30" disabled="${myAccountShipInfoActionForm.useBilling}"/></div>
		          </td>
		        </tr>
		        <tr>
		          <td>
		            <logic:messagesPresent property="shippingCustAddressLine2" message="true">
		              <html:messages property="shippingCustAddressLine1" id="errorText" message="true">
		              <span class="jc_alert">
		                <bean:write name="errorText"/>
		              </span>
		              </html:messages>
		            </logic:messagesPresent>
		          </td>
		        </tr>
		        <tr> 
		          <td><span class="jc_text_normal_title">City</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="shippingCustCityName" size="20" maxlength="40"/></div>
		          </td>
		        </tr>
		        <tr>
		          <td><span class="jc_text_normal_title">State/Province</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <html:select property="shippingCustStateCode" styleClass="jc_input_normal" style="width: 200px"> 
		              <html:optionsCollection property="states" label="label" value="value"/> 
		            </html:select> 
		          </td>
		        </tr>
		        <tr> 
		          <td><span class="jc_text_normal_title">Country</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <html:select property="shippingCustCountryCode" styleClass="jc_input_normal" style="width: 200px" disabled="${myAccountShipInfoActionForm.useBilling}"> 
		              <html:optionsCollection property="countries" label="label" value="value"/> 
		            </html:select> 
		          </td>
		        </tr>
		        <tr> 
		          <td><span class="jc_text_normal_title">Zip/Postal</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="shippingCustZipCode" size="10"/></div>
		          </td>
		        </tr>
		        <tr>
		          <td>
		            <logic:messagesPresent property="shippingCustZipCode" message="true">
		              <html:messages property="shippingCustZipCode" id="errorText" message="true">
		              <span class="jc_alert">
		                <bean:write name="errorText"/>
		              </span>
		              </html:messages>
		            </logic:messagesPresent>
		          </td>
		        </tr>
		        <tr> 
		          <td><span class="jc_text_normal_title">Phone Number</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="shippingCustPhoneNum" size="15" disabled="${myAccountShipInfoActionForm.useBilling}"/></div>
		          </td>
		        </tr>
		        <tr>
		          <td>
		            <logic:messagesPresent property="shippingCustPhoneNum" message="true">
		              <html:messages property="shippingCustPhoneNum" id="errorText" message="true">
		              <span class="jc_alert">
		                <bean:write name="errorText"/>
		              </span>
		              </html:messages>
		            </logic:messagesPresent>
		          </td>
		        </tr>
		        <tr> 
		          <td><span class="jc_text_normal_title">Fax Number</span><br>
		          </td>
		        </tr>
		        <tr>
		          <td nowrap>
		            <div class="jc_input_normal"><html:text property="shippingCustFaxNum" size="15" disabled="${myAccountShipInfoActionForm.useBilling}"/></div>
		          </td>
		        </tr>
		        <tr>
		          <td>
		            <logic:messagesPresent property="shippingCustFaxNum" message="true">
		              <html:messages property="shippingCustFaxNum" id="errorText" message="true">
		              <span class="jc_alert">
		                <bean:write name="errorText"/>
		              </span>
		              </html:messages>
		            </logic:messagesPresent>
		          </td>
		        </tr>
		      </table>
          </td>
        </tr>
      </table>
      <br>
      <table width="200" cellspacing="0" cellpadding="0">
        <tr>
          <td>
            <html:link href="javascript:submitForm('update');" styleClass="jc_button_small">
              Update
            </html:link>
          </td>
        </tr>
      </table>
      <br>
      </div>
    </td>
  </tr>
</table>
</html:form>
</div>