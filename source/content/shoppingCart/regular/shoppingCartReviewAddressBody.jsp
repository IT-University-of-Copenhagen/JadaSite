<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>

<div id="shopping-cart-container">
<c:set var="shoppingCartForm" scope="request" value="${shoppingCartActionForm}"/>
<html:form method="post" action="/content/checkout/regular/shoppingCartReviewAddress.do">
<html:hidden property="custId"/> 
<html:hidden property="prefix" value="${contentBean.siteDomain.siteDomainPrefix}"/>
<html:hidden property="newUser"/> 
<html:hidden property="process" value=""/> 
<%@ include file="/content/shoppingCart/shoppingCartSteps.jsp" %>

<script type="text/javascript" language="JavaScript"><!--
function submitForm(type) {
    document.shoppingCartActionForm.process.value = type;
    document.shoppingCartActionForm.submit();
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

function selectShipping(value) {
	var container = document.getElementById('shippingContainer');
	if (value == 'O') {
		container.style.display = 'block';
	}
	else {
		container.style.display = 'none';
	}
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

<br>
<div id="shopping-cart-reviewaddress-title-container">
	<span id="shopping-cart-reviewaddress-title">
	  <c:choose>
	  <c:when test="${shoppingCartForm.newUser}">
	    <lang:contentMessage value="New User"/>
	  </c:when>
	  <c:otherwise>
	    <lang:contentMessage value="Review User Information"/>
	  </c:otherwise>
	  </c:choose>
	</span>
</div>
<br>
<div id="shopping-cart-reviewaddress-container">
<span class="shopping-cart-reviewaddress-text">
<lang:contentMessage key="content.text.userInfoText"/>
</span>
<br><br>
<div id="myinfoContainer">
<table border="0" cellpadding="3" width="800px" class="shopping-cart-reviewaddress-table">
  <tr>
    <td><span class="shopping-cart-reviewaddress-subtitle"><lang:contentMessage value="My information"/></span></td>
  </tr>
  <tr>
    <td width="100"><span class="shopping-cart-input-label"><lang:contentMessage value="Email address"/>*</span></td>
    <td>
    <html:hidden property="custEmail"/>
    ${shoppingCartForm.custEmail}
    </td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="First Name"/>*</span></td>
    <td valign="top">
      <html:text property="custFirstName" styleClass="shopping-cart-input" style="width: 240px" maxlength="40"/>
      <logic:messagesPresent property="custFirstName" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="custFirstName"/></span>
      </logic:messagesPresent>
    </td>
    <td valign="top" width="100"><span class="shopping-cart-input-label"><lang:contentMessage value="Last Name"/>*</span></td>
    <td valign="top">
      <html:text property="custLastName" styleClass="shopping-cart-input" style="width: 240px" maxlength="40"/>
      <logic:messagesPresent property="custLastName" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="custLastName"/></span>
      </logic:messagesPresent>
    </td>
  </tr>
  <tr>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="Address"/> 1*</span></td>
    <td valign="top">
      <html:text property="custAddressLine1" styleClass="shopping-cart-input" style="width: 240px" maxlength="30"/>
      <logic:messagesPresent property="custAddressLine1" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="custAddressLine1"/></span>
      </logic:messagesPresent>
    </td>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="Address"/> 2</span></td>
    <td valign="top"><html:text property="custAddressLine2" styleClass="shopping-cart-input" style="width: 240px" maxlength="30"/></td>
  </tr>
  <tr>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="Country"/>*</span></td>
    <td valign="top">
      <html:select styleId="custCountryCode" property="custCountryCode" styleClass="shopping-cart-input" style="width: 240px" onchange="populateStateCodes('custCountryCode', 'custStateCode', 'custStateName')"> 
        <c:if test="${fn:length(shoppingCartForm.countries) > 1}">
        <html:option value=""><lang:contentMessage value="Please select"/></html:option>
        </c:if>
        <html:optionsCollection property="countries" label="label" value="value"/> 
      </html:select>
      <logic:messagesPresent property="custCountryCode" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="custCountryCode"/></span>
      </logic:messagesPresent>
    </td>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="State/Province"/></span></td>
    <td valign="top">
      <c:choose>
		<c:when test="${fn:length(shoppingCartForm.custStates) > 0}">
          <html:select styleId="custStateCode" property="custStateCode" styleClass="shopping-cart-input" style="width: 120px; display: block"> 
            <html:option value=""><lang:contentMessage value="Please select"/></html:option>
            <html:optionsCollection property="custStates" label="label" value="value"/> 
          </html:select>
          <html:text styleId="custStateName" property="custStateName" styleClass="shopping-cart-input" style="width: 240px; display: none" maxlength="40" disabled="true"/>
		</c:when>
		<c:otherwise>
          <html:select styleId="custStateCode" property="custStateCode" styleClass="shopping-cart-input" style="width: 120px; display: none" disabled="true"> 
            <html:option value=""><lang:contentMessage value="Please select"/></html:option>
            <html:optionsCollection property="custStates" label="label" value="value"/> 
          </html:select>
          <html:text styleId="custStateName" property="custStateName" styleClass="shopping-cart-input" style="width: 240px; display: block" maxlength="40"/>
		</c:otherwise>
      </c:choose>
      <logic:messagesPresent property="custStateCode" message="true">
        <span class="shopping-cart-error"><lang:contentError field="custStateCode"/></span>
      </logic:messagesPresent>
    </td>
  </tr>
  <tr>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="Phone"/>*</span></td>
    <td valign="top">
      <html:text property="custPhoneNum" styleClass="shopping-cart-input" style="width: 240px" maxlength="15"/>
      <logic:messagesPresent property="custPhoneNum" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="custPhoneNum"/></span>
      </logic:messagesPresent>
    </td>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="City"/>*</span></td>
    <td valign="top">
      <html:text property="custCityName" styleClass="shopping-cart-input" style="width: 240px" maxlength="30"/>
      <logic:messagesPresent property="custCityName" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="custCityName"/></span>
      </logic:messagesPresent>
    </td>
  </tr>
  <tr>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="Fax"/></span></td>
    <td valign="top">
      <html:text property="custFaxNum" styleClass="shopping-cart-input" style="width: 240px" maxlength="15"/>
      <logic:messagesPresent property="custFaxNum" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="custFaxNum"/></span>
      </logic:messagesPresent>
    </td>
    <td valign="top">
      <span class="shopping-cart-input-label"><lang:contentMessage value="Zip/Postal"/>*</span>
    </td>
    <td valign="top">
      <html:text property="custZipCode" styleClass="shopping-cart-input" style="width: 50px" maxlength="10"/>
      <logic:messagesPresent property="custZipCode" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="custZipCode"/></span>
      </logic:messagesPresent>
    </td>
  </tr>
</table>
</div>
<br>
<table width="800" border="0" cellpadding="3" class="shopping-cart-reviewaddress-table">
  <tr>
    <td><span class="shopping-cart-reviewaddress-subtitle"><lang:contentMessage value="Billing information"/></span></td>
  </tr>
  <tr>
    <td>
      <html:select property="billingUseAddress" styleClass="shopping-cart-input" onchange="selectBilling(this.value)"> 
        <html:option value="C"><lang:contentMessage value="Same as my information"/></html:option>
        <html:option value="O"><lang:contentMessage value="Choose a different address"/></html:option>
      </html:select>
    </td>
  </tr>
</table>
<div id="billingContainer" style="display:none"> <br>
  <table width="800" border="0" cellpadding="3" class="shopping-cart-reviewaddress-table">
  <tr>
    <td valign="top" width="100"><span class="shopping-cart-input-label"><lang:contentMessage value="First Name"/>*</span></td>
    <td valign="top">
      <html:text property="billingCustFirstName" styleClass="shopping-cart-input" style="width: 240px" maxlength="40"/>
      <logic:messagesPresent property="billingCustFirstName" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="billingCustFirstName"/></span>
      </logic:messagesPresent>
    </td>
    <td valign="top" width="100"><span class="shopping-cart-input-label"><lang:contentMessage value="Last Name"/>*</span></td>
    <td valign="top">
      <html:text property="billingCustLastName" styleClass="shopping-cart-input" style="width: 240px" maxlength="40"/>
      <logic:messagesPresent property="billingCustLastName" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="billingCustLastName"/></span>
      </logic:messagesPresent>
    </td>
  </tr>
  <tr>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="Address"/> 1*</span></td>
    <td valign="top">
      <html:text property="billingCustAddressLine1" styleClass="shopping-cart-input" style="width: 240px" maxlength="30"/>
      <logic:messagesPresent property="billingCustAddressLine1" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="billingCustAddressLine1"/></span>
      </logic:messagesPresent>
    </td>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="Address"/> 2</span></td>
    <td valign="top">
      <html:text property="billingCustAddressLine2" styleClass="shopping-cart-input" style="width: 240px" maxlength="30"/>
    </td>
  </tr>
  <tr>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="Country"/>*</span></td>
    <td valign="top">
      <html:select styleId="billingCustCountryCode" property="billingCustCountryCode" styleClass="shopping-cart-input" style="width: 240px" onchange="populateStateCodes('billingCustCountryCode', 'billingCustStateCode', 'billingCustStateName')"> 
        <c:if test="${fn:length(shoppingCartForm.countries) > 1}">
        <html:option value=""><lang:contentMessage value="Please select"/></html:option>
        </c:if>
        <html:optionsCollection property="countries" label="label" value="value"/> 
      </html:select>
      <logic:messagesPresent property="billingCustCountryCode" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="billingCustCountryCode"/></span>
      </logic:messagesPresent>
    </td>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="State/Province"/></span></td>
    <td valign="top">
      <c:choose>
		<c:when test="${fn:length(shoppingCartForm.billingCustStates) > 0}">
          <html:select styleId="billingCustStateCode" property="billingCustStateCode" styleClass="shopping-cart-input" style="width: 120px; display: block"> 
            <html:option value=""><lang:contentMessage value="Please select"/></html:option>
            <html:optionsCollection property="billingCustStates" label="label" value="value"/> 
          </html:select>
          <html:text styleId="billingCustStateName" property="billingCustStateName" styleClass="shopping-cart-input" style="width: 240px; display: none" maxlength="40"/>
		</c:when>
		<c:otherwise>
          <html:select styleId="billingCustStateCode" property="billingCustStateCode" styleClass="shopping-cart-input" style="width: 120px; display: none"> 
            <html:option value=""><lang:contentMessage value="Please select"/></html:option>
            <html:optionsCollection property="billingCustStates" label="label" value="value"/> 
          </html:select>
          <html:text styleId="billingCustStateName" property="billingCustStateName" styleClass="shopping-cart-input" style="width: 240px; display: block" maxlength="40"/>
		</c:otherwise>
      </c:choose>
      <logic:messagesPresent property="billingCustStateCode" message="true">
        <span class="shopping-cart-error"><lang:contentError field="billingCustStateCode"/></span>
      </logic:messagesPresent>
    </td>
  </tr>
  <tr>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="Phone"/>*</span></td>
    <td valign="top">
      <html:text property="billingCustPhoneNum" styleClass="shopping-cart-input" style="width: 240px" maxlength="15"/>
      <logic:messagesPresent property="billingCustPhoneNum" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="billingCustPhoneNum"/></span>
      </logic:messagesPresent>
    </td>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="City"/>*</span></td>
    <td valign="top">
      <html:text property="billingCustCityName" styleClass="shopping-cart-input" style="width: 240px" maxlength="30"/>
      <logic:messagesPresent property="billingCustCityName" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="billingCustCityName"/></span>
      </logic:messagesPresent>
    </td>
  </tr>
  <tr>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="Fax"/></span></td>
    <td valign="top">
      <html:text property="billingCustFaxNum" styleClass="shopping-cart-input" style="width: 240px" maxlength="15"/>
      <logic:messagesPresent property="billingCustFaxNum" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="billingCustFaxNum"/></span>
      </logic:messagesPresent>
    </td>
    <td valign="top">
      <span class="shopping-cart-input-label"><lang:contentMessage value="Zip/Postal"/>*</span>
    </td>
    <td valign="top">
      <html:text property="billingCustZipCode" styleClass="shopping-cart-input" style="width: 50px" maxlength="10"/>
      <logic:messagesPresent property="billingCustZipCode" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="billingCustZipCode"/></span>
      </logic:messagesPresent>
    </td>
  </tr>
  </table>
</div>
<br>
<table width="800" border="0" cellpadding="0" class="shopping-cart-reviewaddress-table">
  <tr>
    <td><span class="shopping-cart-reviewaddress-subtitle"><lang:contentMessage value="Shipping information"/></span></td>
  </tr>
  <tr>
    <td>
      <html:select property="shippingUseAddress" styleClass="shopping-cart-input" onchange="selectShipping(this.value)"> 
        <html:option value="C"><lang:contentMessage value="Same as my information"/></html:option>
        <html:option value="B"><lang:contentMessage value="Same as billing information"/></html:option>
        <html:option value="O"><lang:contentMessage value="Choose a different address"/></html:option>
      </html:select>
    </td>
  </tr>
</table>
<div id="shippingContainer" style="display:none"> <br>
  <table width="800" border="0" cellpadding="3" class="shopping-cart-reviewaddress-table">
  <tr>
    <td valign="top" width="100"><span class="shopping-cart-input-label"><lang:contentMessage value="First Name"/>*</span></td>
    <td valign="top">
      <html:text property="shippingCustFirstName" styleClass="shopping-cart-input" style="width: 240px" maxlength="40"/>
      <logic:messagesPresent property="shippingCustFirstName" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="shippingCustFirstName"/></span>
      </logic:messagesPresent>
    </td>
    <td valign="top" width="100"><span class="shopping-cart-input-label"><lang:contentMessage value="Last Name"/>*</span></td>
    <td valign="top">
      <html:text property="shippingCustLastName" styleClass="shopping-cart-input" style="width: 240px" maxlength="40"/>
      <logic:messagesPresent property="shippingCustLastName" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="shippingCustLastName"/></span>
      </logic:messagesPresent>
    </td>
  </tr>
  <tr>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="Address"/> 1*</span></td>
    <td valign="top">
      <html:text property="shippingCustAddressLine1" styleClass="shopping-cart-input" style="width: 240px" maxlength="30"/>
      <logic:messagesPresent property="shippingCustAddressLine1" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="shippingCustAddressLine1"/></span>
      </logic:messagesPresent>
    </td>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="Address"/> 2</span></td>
    <td valign="top">
      <html:text property="shippingCustAddressLine2" styleClass="shopping-cart-input" style="width: 240px" maxlength="30"/>
    </td>
  </tr>
  <tr>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="Country"/>*</span></td>
    <td valign="top">
      <html:select styleId="shippingCustCountryCode" property="shippingCustCountryCode" styleClass="shopping-cart-input" style="width: 240px" onchange="populateStateCodes('shippingCustCountryCode', 'shippingCustStateCode', 'shippingCustStateName')"> 
        <c:if test="${fn:length(shoppingCartForm.countries) > 1}">
        <html:option value=""><lang:contentMessage value="Please select"/></html:option>
        </c:if>
        <html:optionsCollection property="countries" label="label" value="value"/> 
      </html:select>
      <logic:messagesPresent property="shippingCustCountryCode" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="shippingCustCountryCode"/></span>
      </logic:messagesPresent>
    </td>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="State/Province"/></span></td>
    <td valign="top">
      <c:choose>
		<c:when test="${fn:length(shoppingCartForm.shippingCustStates) > 0}">
          <html:select styleId="shippingCustStateCode" property="shippingCustStateCode" styleClass="shopping-cart-input" style="width: 120px; display: block"> 
            <html:option value=""><lang:contentMessage value="Please select"/></html:option>
            <html:optionsCollection property="shippingCustStates" label="label" value="value"/> 
          </html:select>
          <html:text styleId="shippingCustStateName" property="shippingCustStateName" styleClass="shopping-cart-input" style="width: 240px; display: none" maxlength="40"/>
		</c:when>
		<c:otherwise>
          <html:select styleId="shippingCustStateCode" property="shippingCustStateCode" styleClass="shopping-cart-input" style="width: 120px; display: none"> 
            <html:option value=""><lang:contentMessage value="Please select"/></html:option>
            <html:optionsCollection property="shippingCustStates" label="label" value="value"/> 
          </html:select>
          <html:text styleId="shippingCustStateName" property="shippingCustStateName" styleClass="shopping-cart-input" style="width: 240px; display: block" maxlength="40"/>
		</c:otherwise>
      </c:choose>
      <logic:messagesPresent property="shippingCustStateCode" message="true">
        <span class="shopping-cart-error"><lang:contentError field="shippingCustStateCode"/></span>
      </logic:messagesPresent>
    </td>
  </tr>
  <tr>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="Phone"/>*</span></td>
    <td valign="top">
      <html:text property="shippingCustPhoneNum" styleClass="shopping-cart-input" style="width: 240px" maxlength="15"/>
      <logic:messagesPresent property="shippingCustPhoneNum" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="shippingCustPhoneNum"/></span>
      </logic:messagesPresent>
    </td>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="City"/>*</span></td>
    <td valign="top">
      <html:text property="shippingCustCityName" styleClass="shopping-cart-input" style="width: 240px" maxlength="30"/>
      <logic:messagesPresent property="shippingCustCityName" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="shippingCustCityName"/></span>
      </logic:messagesPresent>
    </td>
  </tr>
  <tr>
    <td valign="top"><span class="shopping-cart-input-label"><lang:contentMessage value="Fax"/></span></td>
    <td valign="top">
      <html:text property="shippingCustFaxNum" styleClass="shopping-cart-input" style="width: 240px" maxlength="15"/>
      <logic:messagesPresent property="shippingCustFaxNum" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="shippingCustFaxNum"/></span>
      </logic:messagesPresent>
    </td>
    <td valign="top">
      <span class="shopping-cart-input-label"><lang:contentMessage value="Zip/Postal"/>*</span>
    </td>
    <td valign="top">
      <html:text property="shippingCustZipCode" styleClass="shopping-cart-input" style="width: 50px" maxlength="10"/>
      <logic:messagesPresent property="shippingCustZipCode" message="true">
        <br><span class="shopping-cart-error"><lang:contentError field="shippingCustZipCode"/></span>
      </logic:messagesPresent>
    </td>
  </tr>
  </table>
</div>
<br>
<table width="800" cellspacing="0" cellpadding="2" class="shopping-cart-reviewaddress-table">
  <tr> 
    <td>
      <html:link page="/content/checkout/shoppingCartCancelCheckout.do?process=cancel&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}" styleClass="shopping-cart-button">
        <lang:contentMessage value="Continue Shopping"/>
      </html:link>
      &nbsp;
      <html:link href="javascript:submitForm('update');" styleClass="shopping-cart-button">
        <lang:contentMessage value="Update and Continue"/>
      </html:link>
    </td>
  </tr>
</table>
</div>
<br>
<div class="shopping-cart-footer">
  <c:out value='${shoppingCartActionForm.shoppingCartMessage}' escapeXml='false'/>
</div>
</html:form>
</div>
<script type="text/javascript" language="JavaScript">
YAHOO.util.Event.onAvailable('step1', function() {
	var object = document.getElementById('step2');
	object.className = 'shopping-cart-steps-active';
	object = document.getElementById('step2_line');
	object.className = 'shopping-cart-steps-line-active';
} );
</script>