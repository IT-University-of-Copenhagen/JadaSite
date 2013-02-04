<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>  
<html:form method="post" styleId="shoppingCartUserForm" action="/content/checkout/shoppingCartProcess.do">
<html:hidden property="custId"/> 
<html:hidden property="prefix" value="${contentBean.siteDomain.siteDomainPrefix}"/>
<html:hidden property="newUser"/> 
<html:hidden property="process" value="updateUser"/> 

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
	var form = document.getElementById("shoppingCartUserForm");
	var value = form.billingUseAddress.value;
	if (value == 'O') {
		document.getElementById('billingContainer').style.display = 'block';
	}
} );

YAHOO.util.Event.onContentReady('shippingContainer', function() {
	var form = document.getElementById("shoppingCartUserForm");
	var value = form.shippingUseAddress.value;
	if (value == 'O') {
		document.getElementById('shippingContainer').style.display = 'block';
	}
} );

function populateStateCodes(countryId, stateCodeId, stateNameId, stateCode) {
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
                        if (stateCode) {
                            if (stateCode == jsonObject.states[i].stateCode) {
                                selection.selected = 'selected';
                            }
                        }
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

<div id="myinfoContainer">
<div class="shopping-cart-col1">
  <div class="shopping-cart-col11">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Email address"/>*</span>
  </div>
  <div class="shopping-cart-col11" id="custEmail">
    ${shoppingCartForm.custEmail}
  </div>
</div>
<div class="shopping-cart-col2">
  <div class="shopping-cart-col21">
  </div>
  <div class="shopping-cart-col22">
  </div>
</div>
<div class="container-reset"></div>

<div class="shopping-cart-col1">
  <div class="shopping-cart-col11">
    <span class="shopping-cart-input-label"><lang:contentMessage value="First Name"/>*</span>
  </div>
  <div class="shopping-cart-col11">
    <html:text property="custFirstName" styleClass="shopping-cart-input" style="width: 240px" maxlength="40"/>
    <span class="shopping-cart-error" id="msg-custFirstName"></span>
  </div>
</div>
<div class="shopping-cart-col2">
  <div class="shopping-cart-col21">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Last Name"/>*</span>
  </div>
  <div class="shopping-cart-col22">
    <html:text property="custLastName" styleClass="shopping-cart-input" style="width: 240px" maxlength="40"/>
    <span class="shopping-cart-error" id="msg-custLastName"></span>
  </div>
</div>
<div class="container-reset"></div>

<div class="shopping-cart-col1">
  <div class="shopping-cart-col11">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Address"/> 1*</span>
  </div>
  <div class="shopping-cart-col11">
    <html:text property="custAddressLine1" styleClass="shopping-cart-input" style="width: 240px" maxlength="30"/>
    <span class="shopping-cart-error" id="msg-custAddressLine1"></span>
  </div>
</div>
<div class="shopping-cart-col2">
  <div class="shopping-cart-col21">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Address"/> 2*</span>
  </div>
  <div class="shopping-cart-col22">
    <html:text property="custAddressLine2" styleClass="shopping-cart-input" style="width: 240px" maxlength="30"/>
    <span class="shopping-cart-error" id="msg-custAddressLine2"></span>
  </div>
</div>
<div class="container-reset"></div>
<div class="shopping-cart-col1">
  <div class="shopping-cart-col11">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Country"/>*</span>
  </div>
  <div class="shopping-cart-col11">
    <html:select styleId="custCountryCode" property="custCountryCode" styleClass="shopping-cart-input" style="width: 240px" onchange="populateStateCodes('custCountryCode', 'custStateCode', 'custStateName')"> 
      <c:if test="${fn:length(shoppingCartForm.countries) > 1}">
      <html:option value=""><lang:contentMessage value="Please select"/></html:option>
      </c:if>
      <html:optionsCollection property="countries" label="label" value="value"/> 
    </html:select>
    <span class="shopping-cart-error" id="msg-custCountryCode"></span>
  </div>
</div>
<div class="shopping-cart-col2">
  <div class="shopping-cart-col21">
    <span class="shopping-cart-input-label"><lang:contentMessage value="State/Province"/></span>
  </div>
  <div class="shopping-cart-col22">
    <c:choose>
      <c:when test="${fn:length(shoppingCartForm.custStates) > 0}">
        <html:select styleId="custStateCode" property="custStateCode" styleClass="shopping-cart-input" style="width: 120px; display: block"> 
          <html:option value=""><lang:contentMessage value="Please select"/></html:option>
          <html:optionsCollection property="custStates" label="label" value="value"/> 
        </html:select>
        <html:text styleId="custStateName" property="custStateName" styleClass="shopping-cart-input" style="width: 240px; display: none" maxlength="40" disabled="true"/>
        <span class="shopping-cart-error" id="msg-custStateCode"></span><span class="shopping-cart-error" id="msg-custStateName"></span>
      </c:when>
      <c:otherwise>
	    <html:select styleId="custStateCode" property="custStateCode" styleClass="shopping-cart-input" style="width: 120px; display: none" disabled="true"> 
          <html:option value=""><lang:contentMessage value="Please select"/></html:option>
          <html:optionsCollection property="custStates" label="label" value="value"/> 
        </html:select>
        <html:text styleId="custStateName" property="custStateName" styleClass="shopping-cart-input" style="width: 240px; display: block" maxlength="40"/>
        <span class="shopping-cart-error" id="msg-custStateCode"></span><span class="shopping-cart-error" id="msg-custStateName"></span>
      </c:otherwise>
    </c:choose>
  </div>
</div>
<div class="container-reset"></div>

<div class="shopping-cart-col1">
  <div class="shopping-cart-col11">
    <span class="shopping-cart-input-label"><lang:contentMessage value="City"/>*</span>
  </div>
  <div class="shopping-cart-col11">
    <html:text property="custCityName" styleClass="shopping-cart-input" style="width: 240px" maxlength="30"/>
    <span class="shopping-cart-error"><lang:contentError field="custCityName"/></span>
  </div>
</div>
<div class="shopping-cart-col2">
  <div class="shopping-cart-col21">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Zip/Postal"/>*</span>
  </div>
  <div class="shopping-cart-col22">
    <html:text property="custZipCode" styleClass="shopping-cart-input" style="width: 50px" maxlength="10"/>
    <span class="shopping-cart-error"><lang:contentError field="custZipCode"/></span>
  </div>
</div>
<div class="container-reset"></div>

<div class="shopping-cart-col1">
  <div class="shopping-cart-col11">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Phone"/>*</span>
  </div>
  <div class="shopping-cart-col11">
    <html:text property="custPhoneNum" styleClass="shopping-cart-input" style="width: 240px" maxlength="15"/>
    <span class="shopping-cart-error"><lang:contentError field="custPhoneNum"/></span>
  </div>
</div>
<div class="shopping-cart-col2">
  <div class="shopping-cart-col21">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Fax"/></span>
  </div>
  <div class="shopping-cart-col22">
    <html:text property="custFaxNum" styleClass="shopping-cart-input" style="width: 240px" maxlength="15"/>
    <span class="shopping-cart-error"><lang:contentError field="custFaxNum"/></span>
  </div>
</div>
<div class="container-reset"></div>
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
<div class="shopping-cart-col1">
  <div class="shopping-cart-col11">
    <span class="shopping-cart-input-label"><lang:contentMessage value="First Name"/>*</span>
  </div>
  <div class="shopping-cart-col11">
    <html:text property="billingCustFirstName" styleClass="shopping-cart-input" style="width: 240px" maxlength="40"/>
    <span class="shopping-cart-error" id="msg-billingCustFirstName"></span>
  </div>
</div>
<div class="shopping-cart-col2">
  <div class="shopping-cart-col21">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Last Name"/>*</span>
  </div>
  <div class="shopping-cart-col22">
    <html:text property="billingCustLastName" styleClass="shopping-cart-input" style="width: 240px" maxlength="40"/>
    <span class="shopping-cart-error" id="msg-billingCustLastName"></span>
  </div>
</div>
<div class="container-reset"></div>

<div class="shopping-cart-col1">
  <div class="shopping-cart-col11">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Address"/> 1*</span>
  </div>
  <div class="shopping-cart-col11">
    <html:text property="billingCustAddressLine1" styleClass="shopping-cart-input" style="width: 240px" maxlength="30"/>
    <span class="shopping-cart-error" id="msg-billingCustAddressLine1"></span>
  </div>
</div>
<div class="shopping-cart-col2">
  <div class="shopping-cart-col21">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Address"/> 2*</span>
  </div>
  <div class="shopping-cart-col22">
    <html:text property="billingCustAddressLine2" styleClass="shopping-cart-input" style="width: 240px" maxlength="30"/>
    <span class="shopping-cart-error" id="msg-billingCustAddressLine2"></span>
  </div>
</div>
<div class="container-reset"></div>
<div class="shopping-cart-col1">
  <div class="shopping-cart-col11">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Country"/>*</span>
  </div>
  <div class="shopping-cart-col11">
    <html:select styleId="billingCustCountryCode" property="billingCustCountryCode" styleClass="shopping-cart-input" style="width: 240px" onchange="populateStateCodes('billingCustCountryCode', 'billingCustStateCode', 'billingCustStateName')"> 
      <c:if test="${fn:length(shoppingCartForm.countries) > 1}">
      <html:option value=""><lang:contentMessage value="Please select"/></html:option>
      </c:if>
      <html:optionsCollection property="countries" label="label" value="value"/> 
    </html:select>
    <span class="shopping-cart-error" id="msg-billingCustCountryCode"></span>
  </div>
</div>
<div class="shopping-cart-col2">
  <div class="shopping-cart-col21">
    <span class="shopping-cart-input-label"><lang:contentMessage value="State/Province"/></span>
  </div>
  <div class="shopping-cart-col22">
    <c:choose>
      <c:when test="${fn:length(shoppingCartForm.billingCustStates) > 0}">
        <html:select styleId="billingCustStateCode" property="billingCustStateCode" styleClass="shopping-cart-input" style="width: 120px; display: block"> 
          <html:option value=""><lang:contentMessage value="Please select"/></html:option>
          <html:optionsCollection property="billingCustStates" label="label" value="value"/> 
        </html:select>
        <html:text styleId="billingCustStateName" property="billingCustStateName" styleClass="shopping-cart-input" style="width: 240px; display: none" maxlength="40" disabled="true"/>
	</c:when>
	<c:otherwise>
          <html:select styleId="billingCustStateCode" property="billingCustStateCode" styleClass="shopping-cart-input" style="width: 120px; display: none" disabled="true"> 
            <html:option value=""><lang:contentMessage value="Please select"/></html:option>
            <html:optionsCollection property="billingCustStates" label="label" value="value"/> 
          </html:select>
          <html:text styleId="billingCustStateName" property="billingCustStateName" styleClass="shopping-cart-input" style="width: 240px; display: block" maxlength="40"/>
	</c:otherwise>
      </c:choose>
      <span class="shopping-cart-error" id="msg-billingCustStateName"></span>
  </div>
</div>
<div class="container-reset"></div>

<div class="shopping-cart-col1">
  <div class="shopping-cart-col11">
    <span class="shopping-cart-input-label"><lang:contentMessage value="City"/>*</span>
  </div>
  <div class="shopping-cart-col11">
    <html:text property="billingCustCityName" styleClass="shopping-cart-input" style="width: 240px" maxlength="30"/>
    <span class="shopping-cart-error"><lang:contentError field="billingCustCityName"/></span>
  </div>
</div>
<div class="shopping-cart-col2">
  <div class="shopping-cart-col21">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Zip/Postal"/>*</span>
  </div>
  <div class="shopping-cart-col22">
    <html:text property="billingCustZipCode" styleClass="shopping-cart-input" style="width: 50px" maxlength="10"/>
    <span class="shopping-cart-error"><lang:contentError field="billingCustZipCode"/></span>
  </div>
</div>
<div class="container-reset"></div>

<div class="shopping-cart-col1">
  <div class="shopping-cart-col11">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Phone"/>*</span>
  </div>
  <div class="shopping-cart-col11">
    <html:text property="billingCustPhoneNum" styleClass="shopping-cart-input" style="width: 240px" maxlength="15"/>
    <span class="shopping-cart-error"><lang:contentError field="billingCustPhoneNum"/></span>
  </div>
</div>
<div class="shopping-cart-col2">
  <div class="shopping-cart-col21">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Fax"/></span>
  </div>
  <div class="shopping-cart-col22">
    <html:text property="billingCustFaxNum" styleClass="shopping-cart-input" style="width: 240px" maxlength="15"/>
    <span class="shopping-cart-error"><lang:contentError field="billingCustFaxNum"/></span>
  </div>
</div>
<div class="container-reset"></div>
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
<div class="shopping-cart-col1">
  <div class="shopping-cart-col11">
    <span class="shopping-cart-input-label"><lang:contentMessage value="First Name"/>*</span>
  </div>
  <div class="shopping-cart-col11">
    <html:text property="shippingCustFirstName" styleClass="shopping-cart-input" style="width: 240px" maxlength="40"/>
    <span class="shopping-cart-error" id="msg-shippingCustFirstName"></span>
  </div>
</div>
<div class="shopping-cart-col2">
  <div class="shopping-cart-col21">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Last Name"/>*</span>
  </div>
  <div class="shopping-cart-col22">
    <html:text property="shippingCustLastName" styleClass="shopping-cart-input" style="width: 240px" maxlength="40"/>
    <span class="shopping-cart-error" id="msg-shippingCustLastName"></span>
  </div>
</div>
<div class="container-reset"></div>

<div class="shopping-cart-col1">
  <div class="shopping-cart-col11">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Address"/> 1*</span>
  </div>
  <div class="shopping-cart-col11">
    <html:text property="shippingCustAddressLine1" styleClass="shopping-cart-input" style="width: 240px" maxlength="30"/>
    <span class="shopping-cart-error" id="msg-shippingCustAddressLine1"></span>
  </div>
</div>
<div class="shopping-cart-col2">
  <div class="shopping-cart-col21">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Address"/> 2*</span>
  </div>
  <div class="shopping-cart-col22">
    <html:text property="shippingCustAddressLine2" styleClass="shopping-cart-input" style="width: 240px" maxlength="30"/>
    <span class="shopping-cart-error" id="msg-shippingCustAddressLine2"></span>
  </div>
</div>
<div class="container-reset"></div>
<div class="shopping-cart-col1">
  <div class="shopping-cart-col11">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Country"/>*</span>
  </div>
  <div class="shopping-cart-col11">
    <html:select styleId="shippingCustCountryCode" property="shippingCustCountryCode" styleClass="shopping-cart-input" style="width: 240px" onchange="populateStateCodes('shippingCustCountryCode', 'shippingCustStateCode', 'shippingCustStateName')"> 
      <c:if test="${fn:length(shoppingCartForm.countries) > 1}">
      <html:option value=""><lang:contentMessage value="Please select"/></html:option>
      </c:if>
      <html:optionsCollection property="countries" label="label" value="value"/> 
    </html:select>
    <span class="shopping-cart-error" id="msg-shippingCustCountryCode"></span>
  </div>
</div>
<div class="shopping-cart-col2">
  <div class="shopping-cart-col21">
    <span class="shopping-cart-input-label"><lang:contentMessage value="State/Province"/></span>
  </div>
  <div class="shopping-cart-col22">
    <c:choose>
      <c:when test="${fn:length(shoppingCartForm.shippingCustStates) > 0}">
        <html:select styleId="shippingCustStateCode" property="shippingCustStateCode" styleClass="shopping-cart-input" style="width: 120px; display: block"> 
          <html:option value=""><lang:contentMessage value="Please select"/></html:option>
          <html:optionsCollection property="shippingCustStates" label="label" value="value"/> 
        </html:select>
        <html:text styleId="shippingCustStateName" property="shippingCustStateName" styleClass="shopping-cart-input" style="width: 240px; display: none" maxlength="40" disabled="true"/>
	</c:when>
	<c:otherwise>
          <html:select styleId="shippingCustStateCode" property="shippingCustStateCode" styleClass="shopping-cart-input" style="width: 120px; display: none" disabled="true"> 
            <html:option value=""><lang:contentMessage value="Please select"/></html:option>
            <html:optionsCollection property="shippingCustStates" label="label" value="value"/> 
          </html:select>
          <html:text styleId="shippingCustStateName" property="shippingCustStateName" styleClass="shopping-cart-input" style="width: 240px; display: block" maxlength="40"/>
	</c:otherwise>
      </c:choose>
      <span class="shopping-cart-error" id="msg-shippingCustStateName"></span>
  </div>
</div>
<div class="container-reset"></div>

<div class="shopping-cart-col1">
  <div class="shopping-cart-col11">
    <span class="shopping-cart-input-label"><lang:contentMessage value="City"/>*</span>
  </div>
  <div class="shopping-cart-col11">
    <html:text property="shippingCustCityName" styleClass="shopping-cart-input" style="width: 240px" maxlength="30"/>
    <span class="shopping-cart-error"><lang:contentError field="shippingCustCityName"/></span>
  </div>
</div>
<div class="shopping-cart-col2">
  <div class="shopping-cart-col21">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Zip/Postal"/>*</span>
  </div>
  <div class="shopping-cart-col22">
    <html:text property="shippingCustZipCode" styleClass="shopping-cart-input" style="width: 50px" maxlength="10"/>
    <span class="shopping-cart-error"><lang:contentError field="shippingCustZipCode"/></span>
  </div>
</div>
<div class="container-reset"></div>

<div class="shopping-cart-col1">
  <div class="shopping-cart-col11">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Phone"/>*</span>
  </div>
  <div class="shopping-cart-col11">
    <html:text property="shippingCustPhoneNum" styleClass="shopping-cart-input" style="width: 240px" maxlength="15"/>
    <span class="shopping-cart-error"><lang:contentError field="shippingCustPhoneNum"/></span>
  </div>
</div>
<div class="shopping-cart-col2">
  <div class="shopping-cart-col21">
    <span class="shopping-cart-input-label"><lang:contentMessage value="Fax"/></span>
  </div>
  <div class="shopping-cart-col22">
    <html:text property="shippingCustFaxNum" styleClass="shopping-cart-input" style="width: 240px" maxlength="15"/>
    <span class="shopping-cart-error"><lang:contentError field="shippingCustFaxNum"/></span>
  </div>
</div>
<div class="container-reset"></div>
</div>
<div class="shopping-cart-col-large">
  <html:link href="" onclick="return submitUser();" styleClass="shopping-cart-button">
    <lang:contentMessage value="Update and Continue"/>
  </html:link>
</div>
</html:form>