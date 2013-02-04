<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<jsp:useBean id="paymentGatewayMaintForm"  type="com.jada.admin.paymentGateway.PaymentGatewayMaintActionForm"  scope="request" />
<script language="JavaScript">
var paymentGatewayId = "<c:out value='${paymentGatewayMaintForm.paymentGatewayId}'/>";
var mode = '<c:out value='${paymentGatewayMaintForm.mode}'/>';

function paymentGatewaySelect() {
    var selection = document.getElementById('paymentGatewaySelection');
    var gateway = '';
    if (selection.options) {
      gateway = selection.options[selection.selectedIndex].value;
    }
    else {
      gateway = selection.value;
    }
    var gatewayList = new Array('PsiGateEngine', 'AuthorizeNetEngine', 'EWayCVNAustraliaEngine', 'FirstDataEngine', 'PayPalEngine', 'PayPalWebsitePaymentProEngine', 'PayPalWebsitePaymentProHostedEngine', 'PaymentExpressEngine', 'PayPalPayFlowEngine');
    for (var i = 0; i < gatewayList.length; i++) {
      var obj = document.getElementById(gatewayList[i]);
      if (gateway == gatewayList[i]) {
        obj.style.display = 'block';
      }
      else {
        obj.style.display = 'none';
      }
    }
}

<c:if test="${paymentGatewayMaintForm.paymentGatewayProvider != ''}">
	YAHOO.util.Event.onDOMReady(function() {
		paymentGatewaySelect();
	} );
</c:if>

function submitForm(type) {
    document.paymentGatewayMaintForm.process.value = type;
    document.paymentGatewayMaintForm.submit();
    return false;
}
function submitCancelForm() {
    document.paymentGatewayMaintForm.action = "/${adminBean.contextPath}/admin/paymentGateway/paymentGatewayListing.do";
    document.paymentGatewayMaintForm.process.value = "back";
    document.paymentGatewayMaintForm.submit();
    return false;
}
</script>
<div class=" yui-skin-sam">
<html:form method="post" action="/admin/paymentGateway/paymentGatewayMaint"> 
<html:hidden property="process" value=""/> 
<html:hidden property="paymentGatewayId"/> 
<html:hidden property="mode"/> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Store - <a href="/<c:out value='${adminBean.contextPath}'/>/admin/paymentGateway/paymentGatewayListing.do?process=back">Payment Gateway 
      Listing</a> - Payment Gateway Maintenance</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr> 
    <td width="100%" valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
        <tr> 
          <td>&nbsp;</td>
          <td align="right"> 
            <table width="0%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><html:submit property="submitButton" value="Save" styleClass="jc_submit_button" onclick="return submitForm('save');"/>&nbsp;</td>
                <td><html:submit property="submitButton" value="Cancel" styleClass="jc_submit_button" onclick="return submitCancelForm();"/>&nbsp;</td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <table width="500" border="0" cellspacing="0" cellpadding="5">
        <tr>
          <td valign="top">
		    <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td class="jc_input_label">Payment Gateway Name</td>
                </tr>
                <tr>
                <td class="jc_input_control">
                  <html:text property="paymentGatewayName" size="40" styleClass="tableContent"/>
                  <logic:messagesPresent property="paymentGatewayName" message="true"> 
                    <br>
                    <html:messages property="paymentGatewayName" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                  </logic:messagesPresent>
                </td>
              </tr>
              <tr> 
                <td class="jc_input_label">Payment gateway provider</td>
              </tr>
              <tr>
                <td>
                  <html:select styleId="paymentGatewaySelection" property="paymentGatewayProvider" styleClass="tableContent" onchange="paymentGatewaySelect();"> 
                    <html:optionsCollection property="paymentGateways" label="label"/> 
                  </html:select>
                </td>
              </tr>
   		    </table>
   		    <br>
            <div id="PsiGateEngine" style="display:none">
              <table width="500" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td>
                  <hr>
                  </td>
                </tr>
                <tr>
                  <td class="jc_input_label">
                  PSIGate configuration
                  </td>
                </tr>
                <tr>
                  <td>
                  Store Id and PassPhrase should be obtained from PSIGate once an account is created with them.
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Store Id</td>
                </tr>
                <tr>
                  <td>
                    <html:text property="psiGateStoreId" maxlength="60" size="60" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="psiGateStoreId" message="true"> 
                    <br>
                    <html:messages property="psiGateStoreId" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">PassPhrase</td>
                </tr>
                <tr>
                  <td>
                  This field contains sensitive information and is not displayed.  Enter a value if change is required.
                  Enter a space if this field is to be cleared.  Otherwise, leave this field as blank and existing value will not be altered.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:password property="psiGatePassPhrase" maxlength="60" size="60" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="psiGatePassPhrase" message="true"> 
                    <br>
                    <html:messages property="psiGatePassPhrase" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Environment</td>
                </tr>
                <tr>
                  <td>
                  Specify live mode or sandbox mode for testing.
                  </td>
                </tr>
                <tr>
                  <td><html:select property="psiGateEnvironment" styleClass="tableContent"> 
                    <html:option value="sandbox">Sandbox</html:option>
                    <html:option value="live">Live</html:option> 
                    </html:select>
                  </td>
                </tr>
              </table>
            </div>
            <div id="PayPalWebsitePaymentProEngine" style="display:none">
              <table width="500" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td>
                  <hr>
                  </td>
                </tr>
                <tr>
                  <td class="jc_input_label">
                  PayPal Website Payment Pro Configuration
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Api Username</td>
                </tr>
                <tr>
                  <td>
                  Api username, api password and signature can be obtained from PayPal once account is setup with them.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:text property="paypalWebsitePaymentProApiUsername" maxlength="60" size="60" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paypalWebsitePaymentProApiUsername" message="true"> 
                    <br>
                    <html:messages property="paypalWebsitePaymentProApiUsername" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Api Password</td>
                </tr>
                <tr> 
                  <td>
                  This field contains sensitive information and is not displayed.  Enter a value if change is required.
                  Enter a space if this field is to be cleared.  Otherwise, leave this field as blank and existing value will not be altered.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:password property="paypalWebsitePaymentProApiPassword" maxlength="60" size="60" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paypalWebsitePaymentProApiPassword" message="true"> 
                    <br>
                    <html:messages property="paypalWebsitePaymentProApiPassword" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Signature</td>
                </tr>
                <tr>
                  <td>
                  This field contains sensitive information and is not displayed.  Enter a value if change is required.
                  Enter a space if this field is to be cleared.  Otherwise, leave this field as blank and existing value will not be altered.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:password property="paypalWebsitePaymentProSignature" maxlength="60" size="80" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paypalWebsitePaymentProSignature" message="true"> 
                    <br>
                    <html:messages property="paypalWebsitePaymentProSignature" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Environment</td>
                </tr>
                <tr>
                  <td>
                  Specify live mode or sandbox mode for testing.
                  </td>
                </tr>
                <tr>
                  <td><html:select property="paypalWebsitePaymentProEnvironment" styleClass="tableContent"> 
                    <html:option value="sandbox">Sandbox</html:option>
                    <html:option value="live">Live</html:option> 
                    </html:select>
                  </td>
                </tr>
              </table>
            </div>
            <div id="PayPalWebsitePaymentProHostedEngine" style="display:none">
              <table width="500" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td>
                  <hr>
                  </td>
                </tr>
                <tr>
                  <td class="jc_input_label">
                  PayPal Website Payment Pro Hosted Configuration
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Body background color</td>
                </tr>
                <tr>
                  <td>
                  Color of the surrounding background of the page.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:text property="paypalWebsitePaymentProHostedBodyBgColor" size="80" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paypalWebsitePaymentProHostedBodyBgColor" message="true"> 
                    <br>
                    <html:messages property="paypalWebsitePaymentProHostedBodyBgColor" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Body background image</td>
                </tr>
                <tr>
                  <td>
                  Image of the surrounding background of the payment page. The file extension can be .gif, .jpg, .jpeg, or .png format.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:text property="paypalWebsitePaymentProHostedBodyBgImg" size="80" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paypalWebsitePaymentProHostedBodyBgImg" message="true"> 
                    <br>
                    <html:messages property="paypalWebsitePaymentProHostedBodyBgImg" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Footer text color</td>
                </tr>
                <tr>
                  <td>
                  Color of the footer text.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:text property="paypalWebsitePaymentProHostedFooterTextColor" size="80" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paypalWebsitePaymentProHostedFooterTextColor" message="true"> 
                    <br>
                    <html:messages property="paypalWebsitePaymentProHostedFooterTextColor" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Header background color</td>
                </tr>
                <tr>
                  <td>
                  Color of the header background.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:text property="paypalWebsitePaymentProHostedHeaderBgColor" size="80" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paypalWebsitePaymentProHostedHeaderBgColor" message="true"> 
                    <br>
                    <html:messages property="paypalWebsitePaymentProHostedHeaderBgColor" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Header height</td>
                </tr>
                <tr>
                  <td>
                  Height of the header banner. It can be from 50 to 140 pixels. The
width cannot be changed. It is always 940 pixels.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:text property="paypalWebsitePaymentProHostedHeaderHeight" size="80" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paypalWebsitePaymentProHostedHeaderHeight" message="true"> 
                    <br>
                    <html:messages property="paypalWebsitePaymentProHostedHeaderHeight" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Logo font</td>
                </tr>
                <tr>
                  <td>
                  Font type of the logo text.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:text property="paypalWebsitePaymentProHostedLogoFont" size="80" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paypalWebsitePaymentProHostedLogoFont" message="true"> 
                    <br>
                    <html:messages property="paypalWebsitePaymentProHostedLogoFont" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Logo font Color</td>
                </tr>
                <tr>
                  <td>
                  Color of the logo text.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:text property="paypalWebsitePaymentProHostedLogoFontColor" size="80" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paypalWebsitePaymentProHostedLogoFont" message="true"> 
                    <br>
                    <html:messages property="paypalWebsitePaymentProHostedLogoFontColor" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Logo font Size</td>
                </tr>
                <tr>
                  <td>
                  Color of the logo text.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:text property="paypalWebsitePaymentProHostedLogoFontSize" size="80" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paypalWebsitePaymentProHostedLogoFontSize" message="true"> 
                    <br>
                    <html:messages property="paypalWebsitePaymentProHostedLogoFontSize" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Logo image position</td>
                </tr>
                <tr>
                  <td>
                  Position of the image in the logo.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:text property="paypalWebsitePaymentProHostedLogoImagePosition" size="80" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paypalWebsitePaymentProHostedLogoImagePosition" message="true"> 
                    <br>
                    <html:messages property="paypalWebsitePaymentProHostedLogoImagePosition" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Logo Image</td>
                </tr>
                <tr>
                  <td>
                  Image displayed in the logo. The acceptable file extension formats
are .gif, .jpg, .jpeg, or .png. The width of the image cannot be more
than 940 pixels.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:text property="paypalWebsitePaymentProHostedLogoImage" size="80" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paypalWebsitePaymentProHostedLogoImage" message="true"> 
                    <br>
                    <html:messages property="paypalWebsitePaymentProHostedLogoImage" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Api Username</td>
                </tr>
                <tr>
                  <td>
                  Api username, api password and signature can be obtained from PayPal once account is setup with them.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:text property="paypalWebsitePaymentProHostedApiUsername" maxlength="60" size="60" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paypalWebsitePaymentProHostedApiUsername" message="true"> 
                    <br>
                    <html:messages property="paypalWebsitePaymentProHostedApiUsername" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Api Password</td>
                </tr>
                <tr> 
                  <td>
                  This field contains sensitive information and is not displayed.  Enter a value if change is required.
                  Enter a space if this field is to be cleared.  Otherwise, leave this field as blank and existing value will not be altered.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:password property="paypalWebsitePaymentProHostedApiPassword" maxlength="60" size="60" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paypalWebsitePaymentProHostedApiPassword" message="true"> 
                    <br>
                    <html:messages property="paypalWebsitePaymentProHostedApiPassword" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Signature</td>
                </tr>
                <tr>
                  <td>
                  This field contains sensitive information and is not displayed.  Enter a value if change is required.
                  Enter a space if this field is to be cleared.  Otherwise, leave this field as blank and existing value will not be altered.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:password property="paypalWebsitePaymentProHostedSignature" maxlength="60" size="80" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paypalWebsitePaymentProHostedSignature" message="true"> 
                    <br>
                    <html:messages property="paypalWebsitePaymentProHostedSignature" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Environment</td>
                </tr>
                <tr>
                  <td>
                  Specify live mode or sandbox mode for testing.
                  </td>
                </tr>
                <tr>
                  <td><html:select property="paypalWebsitePaymentProHostedEnvironment" styleClass="tableContent"> 
                    <html:option value="sandbox">Sandbox</html:option>
                    <html:option value="live">Live</html:option> 
                    </html:select>
                  </td>
                </tr>
              </table>
            </div>
            <div id="PayPalPayFlowEngine" style="display:none">
              <table width="500" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td>
                  <hr>
                  </td>
                </tr>
                <tr>
                  <td class="jc_input_label">
                  PayPal Payflow Pro configuration
                  </td>
                </tr>
                <tr>
                  <td>
                  User, vendor, partner and password should be obtained from PayPal once an account is created with them.
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">User</td>
                </tr>
                <tr>
                  <td>
                  If you set up one or more additional users on the account, this value is the ID of
the user authorized to process transactions. If, however, you have not set up
additional users on the account, User has the same value as Vendor.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:text property="paypalPayFlowUser" maxlength="60" size="60" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paypalPayFlowUser" message="true"> 
                    <br>
                    <html:messages property="paypalPayFlowUser" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Vendor</td>
                </tr>
                <tr>
                  <td>
                  Your merchant login name that you created when you registered for the Payflow
Pro or Website Payments Pro Payflow Edition account.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:text property="paypalPayFlowVendor" maxlength="60" size="60" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paypalPayFlowVendor" message="true"> 
                    <br>
                    <html:messages property="paypalPayFlowVendor" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Partner</td>
                </tr>
                <tr>
                  <td>
                  The ID provided to you by the authorized PayPal reseller who registered you for
Payflow Pro or Website Payments Pro Payflow Edition. If you purchased your
account directly from PayPal, use PayPal.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:text property="paypalPayFlowPartner" maxlength="60" size="60" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paypalPayFlowPartner" message="true"> 
                    <br>
                    <html:messages property="paypalPayFlowPartner" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Password</td>
                </tr>
                <tr>
                  <td>
                  The 6- to 32-character case-sensitive password you created while registering for
the account.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:password property="paypalPayFlowPassword" maxlength="60" size="60" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paypalPayFlowPassword" message="true"> 
                    <br>
                    <html:messages property="paypalPayFlowPassword" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Environment</td>
                </tr>
                <tr>
                  <td>
                  Specify live mode or sandbox mode for testing.
                  </td>
                </tr>
                <tr>
                  <td><html:select property="paypalPayFlowEnvironment" styleClass="tableContent"> 
                    <html:option value="sandbox">Sandbox</html:option>
                    <html:option value="live">Live</html:option> 
                    </html:select>
                  </td>
                </tr>
              </table>
            </div>
            <div id="PaymentExpressEngine" style="display:none">
              <table width="500" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td>
                  <hr>
                  </td>
                </tr>
                <tr>
                  <td class="jc_input_label">
                  Payment Express configuration
                  </td>
                </tr>
                <tr>
                  <td>
                  PostUsername and PostPassword should be obtained from Payment Express once an account is created with them.
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">PostUsername</td>
                </tr>
                <tr>
                  <td>
                    <html:text property="paymentExpressPostUsername" maxlength="60" size="60" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paymentExpressPostUsername" message="true"> 
                    <br>
                    <html:messages property="paymentExpressPostUsername" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">PostPassword</td>
                </tr>
                <tr>
                  <td>
                  This field contains sensitive information and is not displayed.  Enter a value if change is required.
                  Enter a space if this field is to be cleared.  Otherwise, leave this field as blank and existing value will not be altered.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:password property="paymentExpressPostPassword" maxlength="60" size="60" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paymentExpressPostPassword" message="true"> 
                    <br>
                    <html:messages property="paymentExpressPostPassword" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Environment</td>
                </tr>
                <tr>
                  <td>
                  Specify live mode or sandbox mode for testing.
                  </td>
                </tr>
                <tr>
                  <td><html:select property="paymentExpressEnvironment" styleClass="tableContent"> 
                    <html:option value="sandbox">Sandbox</html:option>
                    <html:option value="live">Live</html:option> 
                    </html:select>
                  </td>
                </tr>
              </table>
            </div>
            <div id="AuthorizeNetEngine" style="display:none">
              <table width="500" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td>
                  <hr>
                  </td>
                </tr>
                <tr>
                  <td class="jc_input_label">
                  Authorize.Net Configuration
                  </td>
                </tr>
                <tr>
                  <td>
                  Login Id and Tran key should be obtained from Authorize.Net once an account is created with them.
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Login Id</td>
                </tr>
                <tr>
                  <td>
                    <html:text property="authorizeNetLoginId" maxlength="60" size="60" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="authorizeNetLoginId" message="true"> 
                    <br>
                    <html:messages property="authorizeNetLoginId" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Tran key</td>
                </tr>
                <tr>
                  <td>
                  This field contains sensitive information and is not displayed.  Enter a value if change is required.
                  Enter a space if this field is to be cleared.  Otherwise, leave this field as blank and existing value will not be altered.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:password property="authorizeNetTranKey" maxlength="60" size="60" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="authorizeNetTranKey" message="true"> 
                    <br>
                    <html:messages property="authorizeNetTranKey" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Environment</td>
                </tr>
                <tr>
                  <td>
                  Specify live mode or sandbox mode for testing.
                  </td>
                </tr>
                <tr>
                  <td><html:select property="authorizeNetEnvironment" styleClass="tableContent"> 
                    <html:option value="sandbox">Sandbox</html:option>
                    <html:option value="live">Live</html:option> 
                    </html:select>
                  </td>
                </tr>
              </table>
            </div>
            <div id="PayPalEngine" style="display:none">
              <table width="500" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td>
                  <hr>
                  </td>
                </tr>
                <tr>
                  <td class="jc_input_label">
                  PayPal Express Checkout Configuration
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Customer Class</td>
                </tr>
                <tr>
                  <td>
                  User obtained from PayPal will be defaulted to this customer class.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:select property="paymentPaypalCustClassId"> 
                      <html:optionsCollection property="customerClasses" label="label"/> 
                    </html:select>
                    <logic:messagesPresent property="paymentPaypalCustClassId" message="true"> 
                    <br>
                    <html:messages property="paymentPaypalCustClassId" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                 <tr> 
                  <td class="jc_input_label">Api Username</td>
                </tr>
                <tr>
                  <td>
                  Api username, api password and signature can be obtained from PayPal once account is setup with them.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:text property="paymentPaypalApiUsername" maxlength="60" size="60" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paymentPaypalApiUsername" message="true"> 
                    <br>
                    <html:messages property="paymentPaypalApiUsername" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Api Password</td>
                </tr>
                <tr> 
                  <td>
                  This field contains sensitive information and is not displayed.  Enter a value if change is required.
                  Enter a space if this field is to be cleared.  Otherwise, leave this field as blank and existing value will not be altered.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:password property="paymentPaypalApiPassword" maxlength="60" size="60" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paymentPaypalApiPassword" message="true"> 
                    <br>
                    <html:messages property="paymentPaypalApiPassword" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Signature</td>
                </tr>
                <tr>
                  <td>
                  This field contains sensitive information and is not displayed.  Enter a value if change is required.
                  Enter a space if this field is to be cleared.  Otherwise, leave this field as blank and existing value will not be altered.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:password property="paymentPaypalSignature" maxlength="60" size="80" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="paymentPaypalSignature" message="true"> 
                    <br>
                    <html:messages property="paymentPaypalSignature" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Environment</td>
                </tr>
                <tr>
                  <td>
                  Specify live mode or sandbox mode for testing.
                  </td>
                </tr>
                <tr>
                  <td><html:select property="paymentPaypalEnvironment" styleClass="tableContent"> 
                    <html:option value="sandbox">Sandbox</html:option>
                    <html:option value="live">Live</html:option> 
                    </html:select>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Authorize Extra Amount</td>
                </tr>
                <tr>
                  <td>
                  Specify the extra dollar amount to pre-authorize before the transaction is committed.
                  </td>
                </tr>
                <tr>
                  <td><html:text property="paymentPaypalExtraAmount" styleClass="jc_input_object"/> 
                    <span class="jc_input_error"> <logic:messagesPresent property="paymentPaypalExtraAmount" message="true"> 
                    <br><html:messages property="paymentPaypalExtraAmount" id="errorText" message="true"> 
                    <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
                    </span> </td>
                </tr>
               <tr> 
                  <td class="jc_input_label">Authorize Extra Percentage</td>
                </tr>
                <tr>
                  <td>
                  Specify the extra percentage amount to pre-authorize before the transaction is committed.
                  </td>
                </tr>
                 <tr>
                  <td><html:text property="paymentPaypalExtraPercentage" styleClass="jc_input_object"/> 
                    <span class="jc_input_error"> <logic:messagesPresent property="paymentPaypalExtraPercentage" message="true"> 
                    <br><html:messages property="paymentPaypalExtraPercentage" id="errorText" message="true"> 
                    <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
                    </span> </td>
                </tr>
              </table>
            </div>
            <div id="EWayCVNAustraliaEngine" style="display:none">
              <table width="500" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td>
                  <hr>
                  </td>
                </tr>
                <tr>
                  <td class="jc_input_label">
                  eWay CVN Australia configuration
                  </td>
                </tr>
                <tr>
                  <td>
                  eWay customer id should be obtained from eWay once an account is created with them.
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">eWay customer id</td>
                </tr>
                <tr>
                  <td>
                    <html:text property="EWayCVNAustraliaCustomerId" maxlength="60" size="60" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="eWayCVNAustraliaCustomerId" message="true"> 
                    <br>
                    <html:messages property="eWayCVNAustraliaCustomerId" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Environment</td>
                </tr>
                <tr>
                  <td>
                  Specify live mode or sandbox mode for testing.
                  </td>
                </tr>
                <tr>
                  <td><html:select property="EWayCVNAustraliaEnvironment" styleClass="tableContent"> 
                    <html:option value="sandbox">Sandbox</html:option>
                    <html:option value="live">Live</html:option> 
                    </html:select>
                  </td>
                </tr>
              </table>
            </div>
            <div id="FirstDataEngine" style="display:none">
              <table width="500" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td>
                  <hr>
                  </td>
                </tr>
                <tr>
                  <td class="jc_input_label">
                  First Data configuration
                  </td>
                </tr>
                <tr>
                  <td>
                  The following information should be obtained from First Data once an account is created with them.
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">First Data store number</td>
                </tr>
                <tr>
                  <td>
                  This field should contains the merchant store name or store number, which is generally six to ten digit number assigned 
                  when the account is set up. If this is a test account, the store name may be a text value
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:text property="firstDataStoreNum" maxlength="20" size="20" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="firstDataStoreNum" message="true"> 
	                    <br>
	                    <html:messages property="firstDataStoreNum" id="errorText" message="true"> 
	                    <span class="jc_input_error"><bean:write name="errorText"/></span>
	                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">PKCS12 certificate file</td>
                </tr>
                <tr>
                  <td>
                  This field should contains the file path and filename of the PKCS12 certificate generated from the PEM file issued for the given store.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:text property="firstDataKeyFile" maxlength="200" size="60" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="firstDataKeyFile" message="true"> 
	                    <br>
	                    <html:messages property="firstDataKeyFile" id="errorText" message="true"> 
	                    <span class="jc_input_error"><bean:write name="errorText"/></span>
	                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Password</td>
                </tr>
                <tr>
                  <td>
                  This field should contains the password of the PKCS12 certificate.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:password property="firstDataPassword" size="40" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="firstDataPassword" message="true"> 
	                    <br>
	                    <html:messages property="firstDataPassword" id="errorText" message="true"> 
	                    <span class="jc_input_error"><bean:write name="errorText"/></span>
	                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">First Data host name</td>
                </tr>
                <tr>
                  <td>
                  The URL or IP address of the secure payment gateway server.  This information is provided in the merchant's welcome email.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:text property="firstDataHostName" maxlength="200" size="60" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="firstDataHostName" message="true"> 
	                    <br>
	                    <html:messages property="firstDataHostName" id="errorText" message="true"> 
	                    <span class="jc_input_error"><bean:write name="errorText"/></span>
	                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">First Data host port</td>
                </tr>
                <tr>
                  <td>
                  The port of the application used to communicate to the secure payment gateway.
                  </td>
                </tr>
                <tr>
                  <td>
                    <html:text property="firstDataHostPort" maxlength="10" size="10" styleClass="jc_input_object"/>
                    <logic:messagesPresent property="firstDataHostPort" message="true"> 
	                    <br>
	                    <html:messages property="firstDataHostPort" id="errorText" message="true"> 
	                    <span class="jc_input_error"><bean:write name="errorText"/></span>
	                    </html:messages>
                    </logic:messagesPresent>
                  </td>
                </tr>
              </table>
            </div>
		  </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</html:form>
</div>