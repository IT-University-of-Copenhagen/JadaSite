<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>

<script type="text/javascript" language="JavaScript"><!--
function submitForm(type) {
    document.myAccountAddressActionForm.process.value = type;
    document.myAccountAddressActionForm.submit();
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
	var value = document.myAccountAddressActionForm.billingUseAddress.value;
	if (value == 'O') {
		document.getElementById('billingContainer').style.display = 'block';
	}
} );

YAHOO.util.Event.onContentReady('shippingContainer', function() {
	var value = document.myAccountAddressActionForm.shippingUseAddress.value;
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
<jsp:useBean id="myAccountAddressActionForm"  type="com.jada.myaccount.address.MyAccountAddressActionForm"  scope="request" />
<c:set var="myAccountBean" scope="request" value="${myAccountAddressActionForm}"/>
<html:form method="post" action="/myaccount/address/myAccountAddress.do?process=update&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}">
<html:hidden property="process" value=""/>
<div id="my-account-container">
  <div id="my-account-nav-container">
  	<jsp:include page="/myaccount/myAccountNav.jsp" />
  </div>
  <div id="my-account-body-container">
    <div id="my-account-header-container"><lang:contentMessage value="Address information"/></div>
    <span id="my-account-message"><lang:contentError field="message"/></span>
    <div id="my-account-body-inner-container">
      <span class="my-account-title"><lang:contentMessage value="My information"/></span><br>
      <br>
		<table border="0" cellpadding="3" class="my-account-table">
		  <tr>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="First Name"/>*</span></td>
		    <td valign="top">
		      <html:text property="custFirstName" style="width: 240px" maxlength="40" styleClass="my-account-form-input"/>
	          <span class="my-account-error">
	            <lang:contentError field="custFirstName"/>
	          </span>
		    </td>
		    <td valign="top" width="100"><span class="my-account-form-label"><lang:contentMessage value="Last Name"/>*</span></td>
		    <td valign="top">
		      <html:text property="custLastName" style="width: 240px" maxlength="40" styleClass="my-account-form-input"/>
	          <span class="my-account-error">
	            <lang:contentError field="custLastName"/>
	          </span>
		    </td>
		  </tr>
		  <tr>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="Address"/> 1*</span></td>
		    <td valign="top">
		      <html:text property="custAddressLine1" style="width: 240px" maxlength="30" styleClass="my-account-form-input"/>
	          <span class="my-account-error">
	            <lang:contentError field="custAddressLine1"/>
	          </span>
		    </td>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="Address"/> 2</span></td>
		    <td valign="top"><html:text property="custAddressLine2" style="width: 240px" maxlength="30" styleClass="my-account-form-input"/></td>
		  </tr>
		  <tr>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="Country"/>*</span></td>
		    <td valign="top">
		      <html:select styleId="custCountryCode" property="custCountryCode" styleClass="my-account-form-input" style="width: 240px" onchange="populateStateCodes('custCountryCode', 'custStateCode', 'custStateName')">
		        <c:if test="${fn:length(myAccountAddressActionForm.countries) > 1}">
		        <html:option value=""><lang:contentMessage value="Please select"/></html:option>
		        </c:if>
		        <html:optionsCollection property="countries" label="label" value="value"/> 
		      </html:select>
	          <span class="my-account-error">
	            <lang:contentError field="custCountryCode"/>
	          </span>
		    </td>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="State/Province"/>*</span></td>
		    <td valign="top">
		      <c:choose>
				<c:when test="${fn:length(myAccountAddressActionForm.custStates) > 0}">
		          <html:select styleId="custStateCode" property="custStateCode" styleClass="my-account-form-input" style="width: 120px; display: block"> 
		            <html:option value=""><lang:contentMessage value="Please select"/></html:option>
		            <html:optionsCollection property="custStates" label="label" value="value"/> 
		          </html:select>
		          <html:text styleId="custStateName" property="custStateName" styleClass="my-account-form-input" style="width: 240px; display: none" maxlength="40" disabled="true"/>
				</c:when>
				<c:otherwise>
		          <html:select styleId="custStateCode" property="custStateCode" styleClass="my-account-form-input" style="width: 120px; display: none" disabled="true"> 
		            <html:option value=""><lang:contentMessage value="Please select"/></html:option>
		            <html:optionsCollection property="custStates" label="label" value="value"/> 
		          </html:select>
		          <html:text styleId="custStateName" property="custStateName" styleClass="my-account-form-input" style="width: 240px; display: block" maxlength="40"/>
				</c:otherwise>
		      </c:choose>
	          <span class="my-account-error">
	            <lang:contentError field="custStateCode"/>
	          </span>
	           </td>
		  </tr>
		  <tr>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="Phone"/>*</span></td>
		    <td valign="top">
		      <html:text property="custPhoneNum" styleClass="my-account-form-input" style="width: 240px" maxlength="15"/>
	          <span class="my-account-error">
	            <lang:contentError field="custPhoneNum"/>
	          </span>
		    </td>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="City"/>*</span></td>
		    <td valign="top">
		      <html:text property="custCityName" styleClass="my-account-form-input" style="width: 240px" maxlength="30"/>
	          <span class="my-account-error">
	            <lang:contentError field="custCityName"/>
	          </span>
		    </td>
		  </tr>
		  <tr>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="Fax"/></span></td>
		    <td valign="top">
		      <html:text property="custFaxNum" styleClass="my-account-form-input" style="width: 240px" maxlength="15"/>
	          <span class="my-account-error">
	            <lang:contentError field="custFaxNum"/>
	          </span>
		    </td>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="Zip/Postal"/>*</span></td>
		    <td valign="top">
	          <html:text property="custZipCode" styleClass="my-account-form-input" style="width: 50px" maxlength="10"/>
	          <span class="my-account-error">
	            <lang:contentError field="custZipCode"/>
	          </span>
			</td>
		  </tr>
		</table>
		<br>
		<table width="700" border="0" cellpadding="3" class="my-account-table">
		  <tr>
		    <td><span class="my-account-title"><lang:contentMessage value="Billing information"/></span></td>
		  </tr>
		  <tr>
		    <td>
		      <html:select property="billingUseAddress" styleClass="my-account-form-input" onchange="selectBilling(this.value)"> 
		        <html:option value="C"><lang:contentMessage value="Same as my information"/></html:option>
		        <html:option value="O"><lang:contentMessage value="Choose a different address"/></html:option>
		      </html:select>
		    </td>
		  </tr>
		</table>
		<div id="billingContainer" style="display:none"> <br>
		  <table width="700" border="0" cellpadding="3" class="my-account-table">
		  <tr>
		    <td valign="top" width="100"><span class="my-account-form-label"><lang:contentMessage value="First Name"/>*</span></td>
		    <td valign="top">
		      <html:text property="billingCustFirstName" styleClass="my-account-form-input" style="width: 240px" maxlength="40"/>
	          <span class="my-account-error">
	            <lang:contentError field="billingCustFirstName"/>
	          </span>
		    </td>
		    <td valign="top" width="100"><span class="my-account-form-label"><lang:contentMessage value="Last Name"/>*</span></td>
		    <td valign="top">
		      <html:text property="billingCustLastName" styleClass="my-account-form-input" style="width: 240px" maxlength="40"/>
	          <span class="my-account-error">
	            <lang:contentError field="billingCustLastName"/>
	          </span>
		    </td>
		  </tr>
		  <tr>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="Address"/> 1*</span></td>
		    <td valign="top">
		      <html:text property="billingCustAddressLine1" styleClass="my-account-form-input" style="width: 240px" maxlength="30"/>
	          <span class="my-account-error">
	            <lang:contentError field="billingCustAddressLine1"/>
	          </span>
		    </td>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="Address"/> 2</span></td>
		    <td valign="top"><html:text property="billingCustAddressLine2" styleClass="my-account-form-input" style="width: 240px" maxlength="30"/></td>
		  </tr>
		  <tr>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="Country"/>*</span></td>
		    <td valign="top">
		      <html:select styleId="billingCustCountryCode" property="billingCustCountryCode" styleClass="my-account-form-input" style="width: 240px" onchange="populateStateCodes('billingCustCountryCode', 'billingCustStateCode', 'billingCustStateName')">
		        <c:if test="${fn:length(myAccountAddressActionForm.countries) > 1}">
		        <html:option value=""><lang:contentMessage value="Please select"/></html:option>
		        </c:if>
		        <html:optionsCollection property="countries" label="label" value="value"/> 
		      </html:select>
	          <span class="my-account-error">
	            <lang:contentError field="billingCustCountryCode"/>
	          </span>
		    </td>
	        <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="State/Province"/>*</span></td>
		    <td valign="top">
		      <c:choose>
				<c:when test="${fn:length(myAccountAddressActionForm.billingCustStates) > 0}">
		          <html:select styleId="billingCustStateCode" property="billingCustStateCode" styleClass="my-account-form-input" style="width: 120px; display: block"> 
		            <html:option value=""><lang:contentMessage value="Please select"/></html:option>
		            <html:optionsCollection property="billingCustStates" label="label" value="value"/> 
		          </html:select>
		          <html:text styleId="billingCustStateName" property="billingCustStateName" style="width: 240px; display: none" maxlength="40"/>
				</c:when>
				<c:otherwise>
		          <html:select styleId="billingCustStateCode" property="billingCustStateCode" styleClass="my-account-form-input" style="width: 120px; display: none"> 
		            <html:option value=""><lang:contentMessage value="Please select"/></html:option>
		            <html:optionsCollection property="billingCustStates" label="label" value="value"/> 
		          </html:select>
		          <html:text styleId="billingCustStateName" property="billingCustStateName" styleClass="my-account-form-input" style="width: 240px; display: block" maxlength="40"/>
				</c:otherwise>
		      </c:choose>
	          <span class="my-account-error">
	            <lang:contentError field="billingCustStateCode"/>
	          </span>
	           </td>
	  		  </tr>
		  <tr>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="Phone"/>*</span></td>
		    <td valign="top">
		      <html:text property="billingCustPhoneNum" styleClass="my-account-form-input" style="width: 240px" maxlength="15"/>
	          <span class="my-account-error">
	            <lang:contentError field="billingCustPhoneNum"/>
	          </span>
		    </td>
	  		<td valign="top"><span class="my-account-form-label"><lang:contentMessage value="City"/>*</span></td>
		    <td valign="top">
		      <html:text property="billingCustCityName" styleClass="my-account-form-input" style="width: 240px" maxlength="30"/>
	          <span class="my-account-error">
	            <lang:contentError field="billingCustCityName"/>
	          </span>
		    </td>
		  </tr>
		  <tr>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="Fax"/></span></td>
		    <td valign="top">
		      <html:text property="billingCustFaxNum" styleClass="my-account-form-input" style="width: 240px" maxlength="15"/>
	          <span class="my-account-error">
	            <lang:contentError field="billingCustFaxNum"/>
	          </span>
		    </td>
		    <td valign="top">
		      <span class="my-account-form-label"><lang:contentMessage value="Zip/Postal"/>*</span>
	        </td>
		    <td valign="top">
		      <html:text property="billingCustZipCode" styleClass="my-account-form-input" style="width: 50px" maxlength="10"/>
	          <span class="my-account-error">
	            <lang:contentError field="billingCustZipCode"/>
	          </span>
	           </td>
		  </tr>
		  </table>
		</div>
		<br>
		<table width="700" border="0" cellpadding="0" class="my-account-table">
		  <tr>
		    <td><span class="my-account-title"><lang:contentMessage value="Shipping information"/></span></td>
		  </tr>
		  <tr>
		    <td>
		      <html:select property="shippingUseAddress" styleClass="my-account-form-input" onchange="selectShipping(this.value)"> 
		        <html:option value="C"><lang:contentMessage value="Same as my information"/></html:option>
		        <html:option value="B"><lang:contentMessage value="Same as billing information"/></html:option>
		        <html:option value="O"><lang:contentMessage value="Choose a different address"/></html:option>
		      </html:select>
		    </td>
		  </tr>
		</table>
		<div id="shippingContainer" style="display:none"> <br>
		  <table width="700" border="0" cellpadding="3" class="my-account-table">
		  <tr>
		    <td valign="top" width="100"><span class="my-account-form-label"><lang:contentMessage value="First Name"/>*</span></td>
		    <td valign="top">
		      <html:text property="shippingCustFirstName" styleClass="my-account-form-input" style="width: 240px" maxlength="40"/>
	          <span class="my-account-error">
	            <lang:contentError field="shippingCustFirstName"/>
	          </span>
		    </td>
		    <td valign="top" width="100"><span class="my-account-form-label"><lang:contentMessage value="Last Name"/>*</span></td>
		    <td valign="top">
		      <html:text property="shippingCustLastName" styleClass="my-account-form-input" style="width: 240px" maxlength="40"/>
	          <span class="my-account-error">
	            <lang:contentError field="shippingCustLastName"/>
	          </span>
		    </td>
		  </tr>
		  <tr>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="Address"/> 1*</span></td>
		    <td valign="top">
		      <html:text property="shippingCustAddressLine1" styleClass="my-account-form-input" style="width: 240px" maxlength="30"/>
	          <span class="my-account-error">
	            <lang:contentError field="shippingCustAddressLine1"/>
	          </span>
		    </td>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="Address"/> 2</span></td>
		    <td valign="top"><html:text property="shippingCustAddressLine2" styleClass="my-account-form-input" style="width: 240px" maxlength="30"/></td>
		  </tr>
		  <tr>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="Country"/>*</span></td>
		    <td valign="top">
		      <html:select styleId="shippingCustCountryCode" property="shippingCustCountryCode" styleClass="my-account-form-input" style="width: 240px" onchange="populateStateCodes('shippingCustCountryCode', 'shippingCustStateCode', 'shippingCustStateName')"> 
		        <c:if test="${fn:length(myAccountAddressActionForm.countries) > 1}">
		        <html:option value=""><lang:contentMessage value="Please select"/></html:option>
		        </c:if>
		        <html:optionsCollection property="countries" label="label" value="value"/> 
		      </html:select>
	          <span class="my-account-error">
	            <lang:contentError field="shippingCustCountryCode"/>
	          </span>
		    </td>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="State/Province"/>*</span></td>
		    <td valign="top">
		      <c:choose>
				<c:when test="${fn:length(myAccountAddressActionForm.shippingCustStates) > 0}">
		          <html:select styleId="shippingCustStateCode" property="shippingCustStateCode" styleClass="my-account-form-input" style="width: 120px; display: block"> 
		            <html:option value=""><lang:contentMessage value="Please select"/></html:option>
		            <html:optionsCollection property="shippingCustStates" label="label" value="value"/> 
		          </html:select>
		          <html:text styleId="shippingCustStateName" property="shippingCustStateName" styleClass="my-account-form-input" style="width: 240px; display: none" maxlength="40"/>
				</c:when>
				<c:otherwise>
		          <html:select styleId="shippingCustStateCode" property="shippingCustStateCode" styleClass="my-account-form-input" style="width: 120px; display: none"> 
		            <html:option value=""><lang:contentMessage value="Please select"/></html:option>
		            <html:optionsCollection property="shippingCustStates" label="label" value="value"/> 
		          </html:select>
		          <html:text styleId="shippingCustStateName" property="shippingCustStateName" styleClass="my-account-form-input" style="width: 240px; display: block" maxlength="40"/>
				</c:otherwise>
		      </c:choose>
	          <span class="my-account-error">
	            <lang:contentError field="shippingCustStateCode"/>
	          </span>
		  </tr>
		  <tr>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="Phone"/>*</span></td>
		    <td valign="top">
		      <html:text property="shippingCustPhoneNum" styleClass="my-account-form-input" style="width: 240px" maxlength="15"/>
	          <span class="my-account-error">
	            <lang:contentError field="shippingCustPhoneNum"/>
	          </span>
		    </td>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="City"/>* </span></td>
		    <td valign="top">
		      <html:text property="shippingCustCityName" styleClass="my-account-form-input" style="width: 240px" maxlength="30"/>
	          <span class="my-account-error">
	            <lang:contentError field="shippingCustCityName"/>
	          </span>
		    </td>
		  </tr>
		  <tr>
		    <td valign="top"><span class="my-account-form-label"><lang:contentMessage value="Fax"/></span></td>
		    <td valign="top">
		      <html:text property="shippingCustFaxNum" styleClass="my-account-form-input" style="width: 240px" maxlength="15"/>
	          <span class="my-account-error">
	            <lang:contentError field="shippingCustFaxNum"/>
	          </span>
		    </td>
		    <td valign="top">
		      <span class="my-account-form-label"><lang:contentMessage value="Zip/Postal"/>*</span>
	        </td>
		    <td valign="top">
		      <html:text property="shippingCustZipCode" styleClass="my-account-form-input" style="width: 50px" maxlength="10"/>
	          <span class="my-account-error">
	            <lang:contentError field="shippingCustZipCode"/>
	          </span>
	           </td>
		  </tr>
		  </table>
		</div>
		<br>
		<br>
	    <table width="200" cellspacing="0" cellpadding="0" class="my-account-table">
	      <tr>
	        <td>
	          <html:link href="javascript:submitForm('update');" styleClass="my-account-submit">
	            <lang:contentMessage value="Update"/>
	          </html:link>
	        </td>
	      </tr>
	    </table>
    </div>
  </div>
  <div class="container-reset"></div>
</div>
</html:form>

