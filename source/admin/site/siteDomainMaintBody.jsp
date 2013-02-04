<%@ page language="java" import="com.jada.util.Utility" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="siteDomainMaintForm" scope="request" value="${siteDomainMaintForm}"/>
<jsp:useBean id="adminBean"  type="com.jada.admin.AdminBean"  scope="session" />
<jsp:useBean id="siteDomainMaintForm"  type="com.jada.admin.site.SiteDomainMaintActionForm"  scope="request" />
<script type="text/javascript">
var siteProfileClassDefault = false;
var translationEnable = false;
var imageCounter = 0;
var siteDomainId = "${siteDomainMaintForm.siteDomainId}";
<c:if test="${siteDomainMaintForm.siteProfileClassDefault}">
siteProfileClassDefault = true;
</c:if>
<c:if test="${siteDomainMaintForm.translationEnable}">
translationEnable = true;
</c:if>
var master = ${siteDomainMaintForm.master};
var singleCheckout = ${siteDomainMaintForm.singleCheckout};

function submitForm(type) {
    <c:if test="${siteDomainMaintForm.mode != 'C'}"> 
    document.siteDomainMaintForm.tabIndex.value = tabView.get('activeIndex');
    if (siteProfileClassDefault) {
      jc_editor_templateFooter.saveHTML();
      jc_editor_checkoutShoppingCartMessage.saveHTML();
    }
    else {
      jc_editor_templateFooterLang.saveHTML();
      jc_editor_checkoutShoppingCartMessageLang.saveHTML();
	  document.siteDomainMaintForm.subjectCustSalesLang.value = document.siteDomainMaintForm.subjectCustSalesLang_tmp.value;
	  document.siteDomainMaintForm.subjectPwdResetLang.value = document.siteDomainMaintForm.subjectPwdResetLang_tmp.value;
	  document.siteDomainMaintForm.subjectShippingQuoteLang.value = document.siteDomainMaintForm.subjectShippingQuoteLang_tmp.value;
	  document.siteDomainMaintForm.subjectNotificationLang.value = document.siteDomainMaintForm.subjectNotificationLang_tmp.value;	      
    }
    <c:if test="${!siteDomainMaintForm.siteProfileClassDefault}"> 
    document.siteDomainMaintForm.siteNameLang.value = document.siteDomainMaintForm.siteNameLang_tmp.value;
    </c:if>
    </c:if>
    document.siteDomainMaintForm.process.value = type;
    document.siteDomainMaintForm.submit();
    return false;
}
function submitCancelForm() {
    document.siteDomainMaintForm.action = "/${adminBean.contextPath}/admin/site/siteMaint.do?process=edit&siteId=${siteDomainMaintForm.siteId}";
    document.siteDomainMaintForm.process.value = "cancel";
    document.siteDomainMaintForm.submit();
    return false;
}
function preview(selection) {
    var templateName = selection.options[selection.selectedIndex].value;
    if (templateName == "basic") {
      document.images.templateImage.src = "/${adminBean.contextPath}/services/SecureImageProvider.do?type=U&maxsize=240&imageId=" + "/content/template/basic/preview.jpg";
    }
    else {
      document.images.templateImage.src = "/${adminBean.contextPath}/services/SecureImageProvider.do?type=T&siteDomainId=<c:out value='${siteDomainMaintForm.siteDomainId}'/>&maxsize=240&imageId=" + templateName;
    }
}

function showLogoWindow() {
  jc_logo_panel.show();
}

function removeImage() {
  jc_removeImage();
}

function addProfile() {
  submitForm('addProfile');
}

function removeProfile() {
  submitForm('removeProfile');
}

function addCurrency() {
  submitForm('addCurrency');
}

function removeCurrency() {
  submitForm('removeCurrency');
}

function overrideLogoLanguage(control) { 
  var location = document.getElementById('divLogo');
  if (!control.checked) {
    jc_panel_confirm('Confirm to override language?', function(confirm) {
      if (confirm) {
        var activate = 'false';
        jc_resetLanguageImage(activate);
        location.innerHTML = '';
        if (document.siteDomainMaintForm.siteLogoContentType.value != '') {
          var context = "${adminBean.contextPath}";
          var image = document.createElement('img');
          image.src = '/' + context + '/services/SecureImageProvider.do?type=S&maxsize=100&imageId=' + '${siteDomainMaintForm.defaultSiteDomainLangId}';
          location.appendChild(image);
        }
        return true;
      }
      else {
        control.checked = true;
        return false;
      }
    });
  }
  else {
    var activate = 'true';
    jc_resetLanguageImage(activate);
    location.innerHTML = '';
    butLogo.set('disabled', false);
  }
}

YAHOO.util.Event.onAvailable('profileContainer', function() {
	var profileOptionsMenu = [
	                          { text: "Add", value: 1, onclick: { fn: addProfile } },
	                          { text: "Remove", value: 2, onclick: { fn: removeProfile } }
	                      ];
	var butProfile = new YAHOO.widget.Button({
	                            disabled: true,
	                            type: "menu", 
	                            label: "Option", 
	                            name: "profileOptions",
	                            menu: profileOptionsMenu, 
	                            container: "profileContainer" });
		if (siteProfileClassDefault) {
			if (master || !singleCheckout) {
				butProfile.set('disabled', false);
			}
		}
	}
);

YAHOO.util.Event.onAvailable('currencyContainer', function() {
	var currencyOptionsMenu = [
	                          { text: "Add", value: 1, onclick: { fn: addCurrency } },
	                          { text: "Remove", value: 2, onclick: { fn: removeCurrency } }
	                      ];
	var butCurrency = new YAHOO.widget.Button({
	                            disabled: true,
	                            type: "menu", 
	                            label: "Option", 
	                            name: "currencyOptions",
	                            menu: currencyOptionsMenu, 
	                            container: "currencyContainer" });
		if (siteProfileClassDefault) {
			if (master || !singleCheckout) {
				butCurrency.set('disabled', false);
			}
		}
	}
);

function submitTranslateForm() {
	submitForm('translate');
}
YAHOO.util.Event.onAvailable('butTranslate', function() {
	var butTranslate = new YAHOO.widget.Button("butTranslate", { disabled: true });
	if (translationEnable) {
		butTranslate.set('disabled', false);
	}
	butTranslate.on("click", submitTranslateForm);
});
</script>

<html:form method="post" action="/admin/site/siteDomainMaint">
<html:hidden property="mode"/>
<html:hidden property="process" value=""/>
<html:hidden property="tabIndex"/> 
<html:hidden property="siteDomainId"/>
<html:hidden property="siteId"/>
<html:hidden property="master"/>
<html:hidden property="singleCheckout"/>
<html:hidden property="siteDomainLangId"/>
<html:hidden property="defaultSiteDomainLangId"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>
      Administration - 
      <a href="/${adminBean.contextPath}/admin/site/siteListing.do?process=list">Site Listing</a> - 
      <a href="/${adminBean.contextPath}/admin/site/siteMaint.do?process=edit&siteId=${siteDomainMaintForm.siteId}">Site Maintenance</a> - 
      Sub-site Maintenance
    </td>
  </tr>
</table>
<br>
<table width="98%" border="0" cellspacing="0" cellpadding="5">
  <tr> 
    <td width="100%" valign="top"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td class="jc_panel_table_header">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td>
                  <layout:mode value="edit">
                  <table border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td>
                        Profile
                        <html:select property="siteProfileClassId" onchange="javascript:submitForm('language')"> 
                          <html:optionsCollection property="siteProfileClassBeans" value="value" label="label"/> 
                        </html:select>
            			<button type="button" id="butTranslate" name="butTranslate" value="Translate">Translate</button>
                        &nbsp;
                      </td>
                    </tr>
                  </table>
                  </layout:mode>
                </td>
                <td align="right"> 
                  <table width="0%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td><html:submit property="submitButton" value="Save" styleClass="jc_submit_button" onclick="return submitForm('save')"/>&nbsp;</td>
                      <layout:mode value="edit">
                      <c:if test="${!siteDomainMaintForm.siteDomainDefault}"> 
                      <td><html:submit property="submitButton" value="Remove" styleClass="jc_submit_button" onclick="return submitForm('remove')"/>&nbsp;</td>
                      </c:if>
                      </layout:mode> 
                      <td><html:submit property="submitButton" value="Cancel" styleClass="jc_submit_button" onclick="return submitCancelForm('cancel')"/>&nbsp;</td>
                    </tr>
                  </table>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <br>
      <span class="jc_input_error">
      <logic:messagesPresent property="error" message="true"> 
      	<html:messages property="error" id="errorText" message="true"> 
        <bean:write name="errorText"/>
        </html:messages> 
      </logic:messagesPresent>
      </span>
      <br>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td width="400px" valign="top"> 
            <table width="100%" class="borderTable" border="0" cellspacing="0" cellpadding="3">
              <tr>
            	<td class="jc_input_error">
            	  <logic:messagesPresent property="siteDomainId" message="true"> 
                  <html:messages property="siteDomainId" id="errorText" message="true"> <bean:write name="errorText"/> 
                  </html:messages> </logic:messagesPresent>
            	</td>
              </tr>
              <tr>
                <td class="jc_input_label">
                  Sub-site Description <lang:checkboxSwitch name="siteDomainMaintForm" property="siteNameLangFlag"> - Override language</lang:checkboxSwitch>
                </td>
              </tr>
              <tr>
                <td>
                The name of the site and is feed to the template.  
                </td>
              </tr>
              <tr>
            	<td>
            	  <lang:text name="siteDomainMaintForm" property="siteName" size="50" maxlength="50" styleClass="jc_input_object"/>
                </td>
              </tr>
              <tr>
            	<td class="jc_input_error">
            	  <logic:messagesPresent property="siteName" message="true"> 
                  <html:messages property="siteName" id="errorText" message="true"> <bean:write name="errorText"/> 
                  </html:messages> </logic:messagesPresent>
            	</td>
              </tr>
              <tr>
                <td class="jc_input_label">
                  Sub-site URL
                </td>
              </tr>
              <tr>
                <td>
                URL to access the home page of sub site defined in here.  
                </td>
              </tr>
              <tr>
            	<td>
            	  <html:hidden name="siteDomainMaintForm" property="siteDomainUrl"/>
            	  <html:link href="${siteDomainMaintForm.siteDomainUrl}" target="_blank" styleClass="jc_simple_link">${siteDomainMaintForm.siteDomainUrl}</html:link>
                </td>
              </tr>
              <tr>
                <td class="jc_input_label">Site Public Domain Name</td>
              </tr>
              <tr>
                <td>
                Domain name for the public site and is important for determining if the incoming
                request is for this site.  It should be in the format of www.jadasite.com.
                </td>
              </tr>
              <tr>
            	<td>
            	  <lang:text name="siteDomainMaintForm" property="siteDomainName" maxlength="50" size="50" styleClass="jc_input_object"/>
                </td>
              </tr>
              <tr>
                <td class="jc_input_label">Site Public URI Prefix</td>
              </tr>
              <tr>
                <td>
                Prefix to be used in the URI to identify the domain especially when the site is configured with multiple domains.
                </td>
              </tr>
              <tr>
            	<td>
            	  <lang:text name="siteDomainMaintForm" property="siteDomainPrefix" maxlength="50" size="50" styleClass="jc_input_object"/>
                </td>
              </tr>
              <tr>
            	<td class="jc_input_error">
            	  <logic:messagesPresent property="siteDomainPrefix" message="true"> 
                  <html:messages property="siteDomainPrefix" id="errorText" message="true"> <bean:write name="errorText"/> 
                  </html:messages> </logic:messagesPresent>
            	</td>
              </tr>
              <tr>
                <td class="jc_input_label">Site Public Port Number (default to 80)</td>
              </tr>
              <tr>
                <td>
                If not specified, port number will be defaulted to standard port 80.  
                </td>
              </tr>
              <tr>
            	<td>
            	  <lang:text name="siteDomainMaintForm" property="sitePublicPortNum" maxlength="4" size="4" styleClass="jc_input_object"/>
                </td>
              </tr>
              <tr>
            	<td class="jc_input_error">
            	  <logic:messagesPresent property="sitePublicPortNum" message="true"> 
                  <html:messages property="sitePublicPortNum" id="errorText" message="true"> <bean:write name="errorText"/> 
                  </html:messages> </logic:messagesPresent>
            	</td>
              </tr>
              <tr>
                <td class="jc_input_label">Enable SSL secure connection</td>
              </tr>
              <tr>
                <td>
                Select this option when SSL is required when performing secure transactions in the public site.
                Once selected, SSL will be used when user perform checkout transaction and viewing their account
                information.
                </td>
              </tr>
              <tr>
            	<td>
            	  <lang:checkbox name="siteDomainMaintForm" property="siteSecureConnectionEnabled" styleClass="jc_input_object" value="Y"/>
                </td>
              </tr>
              <tr>
            	<td class="jc_input_error">
            	  <logic:messagesPresent property="siteSecureConnectionEnabled" message="true"> 
                  <html:messages property="siteSecureConnectionEnabled" id="errorText" message="true"> <bean:write name="errorText"/> 
                  </html:messages> </logic:messagesPresent>
            	</td>
              </tr>
              <tr>
                <td class="jc_input_label">Site Secure Port Number (default to 443)</td>
              </tr>
              <tr>
                <td>
                If not specified, port number will be defaulted to standard port 443.  
                </td>
              </tr>
               <tr>
            	<td>
            	  <lang:text name="siteDomainMaintForm" property="siteSecurePortNum" maxlength="4" size="4" styleClass="jc_input_object"/>
            	</td>
              </tr>
             <tr>
            	<td class="jc_input_error">
            	  <logic:messagesPresent property="siteSecurePortNum" message="true"> 
                  <html:messages property="siteSecurePortNum" id="errorText" message="true"> <bean:write name="errorText"/> 
                  </html:messages> </logic:messagesPresent>
            	</td>
              </tr>
              <tr>
                <td class="jc_input_label">Active</td>
              </tr>
              <tr>
            	<td>
            	  <lang:checkbox name="siteDomainMaintForm" property="active" styleClass="jc_input_object" value="Y"/>	<td class="jc_input_error">
            	</td>
              </tr>
            </table>
          </td>
          <layout:mode value="edit,inspect">
          <td width="50px">
          &nbsp;
          </td>
          <td width="100%" valign="top">
            <div class=" yui-skin-sam">
            <div id="tabPanel" class="yui-navset"> 
              <ul class="yui-nav">
                <li class="selected"><a href="#site"><em>Site</em></a></li>
                <li><a href="#general"><em>General</em></a></li>
                <li><a href="#logo"><em>Site logo</em></a></li>
                <li><a href="#mail"><em>Mail</em></a></li>
                <li><a href="#business"><em>Business</em></a></li>
                <li><a href="#checkout"><em>Checkout</em></a></li>
                <li><a href="#template"><em>Template</em></a></li>
              </ul>
              
              <div class="yui-content">
                <div id="site" style="padding: 5px; min-height:300px"> 
                  <table border="0" cellspacing="0" cellpadding="3" width="100%">
                    <tr> 
                      <td class="jc_input_label">
						<table border="0" cellspacing="0" cellpadding="0">
						  <tr>
						    <td width="100" class="jc_input_label">Profile</td>
						    <td width="200">
		                      <div id='profileContainer'></div>
						    </td>
						  </tr>
						</table>
                      </td>
                    </tr>
                    <tr> 
                      <td class="jc_input_label">
                        <table border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td width="100">Remove</td>
                            <td width="100">Profile</td>
                            <td width="100">Sequence</td>
                            <td width="50"><div align="center">Active</div></td>
                          </tr>
                          <c:forEach var="siteProfile" items="${siteDomainMaintForm.siteProfiles}" varStatus="status">
                          <tr>
                            <td>
                              <html:hidden indexed="true" name="siteProfile" property="siteProfileId"/>
                              <c:choose>
                  				<c:when test="${siteProfile.locked || (!siteDomainMaintForm.master && siteDomainMaintForm.singleCheckout)}">
                              	  <lang:checkbox indexed="true" name="siteProfile" disabled="true" property="remove" value="Y" styleClass="tableContent"/>
                  				</c:when>
                  				<c:otherwise>
                              	  <lang:checkbox indexed="true" name="siteProfile" property="remove" value="Y" styleClass="tableContent"/>
                  				</c:otherwise>
                  		      </c:choose>
                            </td>
                            <td>
                              <span class="jc_input_control">
                              <c:choose>
                                <c:when test="${!siteDomainMaintForm.master && siteDomainMaintForm.singleCheckout}">
                                  <lang:select indexed="true" name="siteProfile" property="siteProfileClassId" disabled="true"> 
                                    <lang:optionsCollection property="siteProfileClasses" label="label"/> 
                                  </lang:select>
                                </c:when>
                                <c:otherwise>
                                  <lang:select indexed="true" name="siteProfile" property="siteProfileClassId"> 
                                    <lang:optionsCollection property="siteProfileClasses" label="label"/> 
                                  </lang:select>
                                </c:otherwise>
                              </c:choose>
                              </span>
                            </td>
                            <td>
                              <c:choose>
                                <c:when test="${!siteDomainMaintForm.master && siteDomainMaintForm.singleCheckout}">
                                  <lang:text indexed="true" name="siteProfile" size="5" property="seqNum" styleClass="tableContent" disabled="true"/>
                                </c:when>
                                <c:otherwise>
                                  <lang:text indexed="true" name="siteProfile" size="5" property="seqNum" styleClass="tableContent"/>
                                </c:otherwise>
                              </c:choose>
                            </td>
                            <td>
                              <div align="center">
                                <c:choose>
                                  <c:when test="${!siteDomainMaintForm.master && siteDomainMaintForm.singleCheckout}">
                                    <lang:checkbox indexed="true" name="siteProfile" property="active" value="Y" styleClass="tableContent" disabled="true"/>
                                  </c:when>
                                  <c:otherwise>
                                    <lang:checkbox indexed="true" name="siteProfile" property="active" styleClass="tableContent"/>
                                  </c:otherwise>
                                </c:choose>
                              </div>
                            </td>
                          </tr>
                          <tr>
                            <td></td>
                            <td>
                              <span class="jc_input_error">${siteProfile.siteProfileClassIdError}</span>
                            </td>
                            <td>
                              <span class="jc_input_error">${siteProfile.seqNumError}</span>
                            </td>
                            <td></td>
                          </tr>
                          </c:forEach>
                        </table>
                      </td>
                    </tr>
                    <tr>
                      <td><br></td>
                    </tr>
                    <tr> 
                      <td class="jc_input_label">
						<table border="0" cellspacing="0" cellpadding="0">
						  <tr>
						    <td width="100" class="jc_input_label">Currency</td>
						    <td width="200">
		                      <div id='currencyContainer'></div>
						    </td>
						  </tr>
						</table>
                      </td>
                    </tr>
                    <tr> 
                      <td class="jc_input_label">
                        <table border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td width="100">Remove</td>
                            <td width="150">Currency</td>
                            <td width="100">Sequence</td>
                            <td width="150">Exchange</td>
                            <td width="150" nowrap>Payment Gateway</td>
                            <td width="100">PayPal</td>
                            <td width="100">Cash Payment</td>
                            <td width="100"><div align="center">Active</div></td>
                          </tr>
                          <c:forEach var="siteCurrency" items="${siteDomainMaintForm.siteCurrencies}" varStatus="status">
                          <tr>
                            <td>
                              <html:hidden indexed="true" name="siteCurrency" property="siteCurrencyId"/>
                              <c:choose>
                  				<c:when test="${siteCurrency.locked || (!siteDomainMaintForm.master && siteDomainMaintForm.singleCheckout)}">
                                  <lang:checkbox indexed="true" disabled="true" name="siteCurrency" property="remove" value="Y" styleClass="tableContent"/>
                  				</c:when>
                  				<c:otherwise>
                                  <lang:checkbox indexed="true" name="siteCurrency" property="remove" value="Y" styleClass="tableContent"/>
                  				</c:otherwise>
                  		      </c:choose>
                            </td>
                            <td nowrap>
                              <span class="jc_input_control">
                              <c:choose>
                  				<c:when test="${!siteDomainMaintForm.master && siteDomainMaintForm.singleCheckout}">
                                  <lang:select indexed="true" name="siteCurrency" property="siteCurrencyClassId" disabled="true"> 
                                    <lang:optionsCollection property="siteCurrencyClasses" label="label"/> 
                                  </lang:select>
                                </c:when>
                                <c:otherwise>
                                  <lang:select indexed="true" name="siteCurrency" property="siteCurrencyClassId"> 
                                    <lang:optionsCollection property="siteCurrencyClasses" label="label"/> 
                                  </lang:select>
                                </c:otherwise>
							  </c:choose>
							  </span>
                            </td>
                            <td>
                              <c:choose>
                  				<c:when test="${!siteDomainMaintForm.master && siteDomainMaintForm.singleCheckout}">
                                  <lang:text indexed="true" name="siteCurrency" size="5" property="seqNum" styleClass="tableContent" disabled="true"/>
                                </c:when>
                                <c:otherwise>
                                  <lang:text indexed="true" name="siteCurrency" size="5" property="seqNum" styleClass="tableContent"/>
                                </c:otherwise>
							  </c:choose>
                            </td>
                            <td>
                              <c:choose>
                  				<c:when test="${!siteDomainMaintForm.master && siteDomainMaintForm.singleCheckout}">
                                  <lang:text indexed="true" name="siteCurrency" property="exchangeRate" styleClass="tableContent" disabled="true"/>
                                </c:when>
                                <c:otherwise>
                                  <lang:text indexed="true" name="siteCurrency" property="exchangeRate" styleClass="tableContent"/>
                                </c:otherwise>
							  </c:choose>
                            </td>
                            <td nowrap>
                              <span class="jc_input_control">
                              <c:choose>
                  				<c:when test="${!siteDomainMaintForm.master && siteDomainMaintForm.singleCheckout}">
	                              <lang:select indexed="true" name="siteCurrency" property="paymentGatewayId" disabled="true"> 
	                                <lang:optionsCollection property="paymentGateways" label="label"/> 
	                              </lang:select>
                                </c:when>
                                <c:otherwise>
	                              <lang:select indexed="true" name="siteCurrency" property="paymentGatewayId"> 
	                                <lang:optionsCollection property="paymentGateways" label="label"/> 
	                              </lang:select>
                                </c:otherwise>
							  </c:choose>
							  </span>
                            </td>
                            <td nowrap>
                              <span class="jc_input_control">
                              <c:choose>
                  				<c:when test="${!siteDomainMaintForm.master && siteDomainMaintForm.singleCheckout}">
	                              <lang:select indexed="true" name="siteCurrency" property="payPalPaymentGatewayId" disabled="true"> 
	                                <lang:optionsCollection property="payPalPaymentGateways" label="label"/> 
	                              </lang:select>
                                </c:when>
                                <c:otherwise>
	                              <lang:select indexed="true" name="siteCurrency" property="payPalPaymentGatewayId"> 
	                                <lang:optionsCollection property="payPalPaymentGateways" label="label"/> 
	                              </lang:select>
                                </c:otherwise>
							  </c:choose>
							  </span>
                            </td>
                            <td>
                              <div align="center">
                              <c:choose>
                  				<c:when test="${!siteDomainMaintForm.master && siteDomainMaintForm.singleCheckout}">
                                  <lang:checkbox indexed="true" name="siteCurrency" property="cashPayment" value="Y" styleClass="tableContent" disabled="true"/>
                                </c:when>
                                <c:otherwise>
                                  <lang:checkbox indexed="true" name="siteCurrency" property="cashPayment" styleClass="tableContent"/>
                                </c:otherwise>
							  </c:choose>
                              </div>
                            </td>
                            <td>
                              <div align="center">
                              <c:choose>
                  				<c:when test="${!siteDomainMaintForm.master && siteDomainMaintForm.singleCheckout}">
                                  <lang:checkbox indexed="true" name="siteCurrency" property="active" value="Y" styleClass="tableContent" disabled="true"/>
                                </c:when>
                                <c:otherwise>
                                  <lang:checkbox indexed="true" name="siteCurrency" property="active" styleClass="tableContent"/>
                                </c:otherwise>
							  </c:choose>
                              </div>
                            </td>
                          </tr>
                          <tr>
                            <td></td>
                            <td>
                              <span class="jc_input_error">${siteCurrency.siteCurrencyClassIdError}</span>
                            </td>
                            <td>
                              <span class="jc_input_error">${siteCurrency.seqNumError}</span>
                            </td>
                            <td>
                              <span class="jc_input_error">${siteCurrency.exchangeRateError}</span>
                            </td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                          </tr>
                          </c:forEach>
                        </table>
                      </td>
                    </tr>
                    <tr>
                      <td>
                      <br>
                      </td>
                    </tr>
                    <tr> 
                      <td class="jc_input_label">
						<table border="0" cellspacing="0" cellpadding="0">
						  <tr>
						    <td width="100" class="jc_input_label">Base currency</td>
						  </tr>
						  <tr>
						    <td width="100" class="jc_input_label">
						      <c:choose>
                  				<c:when test="${!siteDomainMaintForm.master && siteDomainMaintForm.singleCheckout}">
	                              <lang:select name="siteDomainMaintForm" property="baseSiteCurrencyClassId" disabled="true"> 
	                                <lang:optionsCollection property="siteCurrencyClasses" label="label"/> 
	                              </lang:select>
						    	</c:when>
						    	<c:otherwise>
	                              <lang:select name="siteDomainMaintForm" property="baseSiteCurrencyClassId"> 
	                                <lang:optionsCollection property="siteCurrencyClasses" label="label"/> 
	                              </lang:select>
						    	</c:otherwise>
						      </c:choose>
						    </td>
						  </tr>
						</table>
                      </td>
                    </tr>

                  </table>
                </div>
                <div id="general" style="padding: 5px; min-height:300px"> 
                  <table border="0" cellspacing="0" cellpadding="3" width="100%">
                   <tr> 
                      <td class="jc_input_label">Category page size</td>
                    </tr>
                    <tr>
                      <td>
                      Number of entries to be shown per category page.
                      </td>
                    </tr>
                    <tr>
                      <td><lang:text name="siteDomainMaintForm" property="categoryPageSize" styleClass="jc_input_object"/> 
                        <span class="jc_input_error"> <logic:messagesPresent property="categoryPageSize" message="true"> 
                        <html:messages property="categoryPageSize" id="errorText" message="true"> 
                        <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
                        </span> </td>
                    </tr>
                    <tr> 
                      <td class="jc_input_label">Public site footer
                      <lang:checkboxEditorSwitch name="siteDomainMaintForm" property="templateFooterLangFlag"> - Override language</lang:checkboxEditorSwitch>
                      </td>
                    </tr>
                    <tr>
                      <td>
                      Footer to be used on public site.
                      </td>
                    </tr>
                    <tr>
                      <td>
                        <lang:editorText name="siteDomainMaintForm" property="templateFooter" height="200" width="100%" toolBarSet="Simple"/>
                      </td>
                    </tr>
                  </table>
                </div>
                <div id="logo" style="padding: 5px; min-height:300px">
                  <html:hidden property="siteLogoContentType"/>
                  <table border="0" cellspacing="0" cellpadding="3" width="100%">
                    <tr> 
                      <td>
                      <span id="logoOptionsId">
                        <span class="jc_input_label">
                        Site Logo
                        <c:if test="${!siteDomainMaintForm.siteProfileClassDefault}">
                         - Override default
                         <html:checkbox onclick="return overrideLogoLanguage(this);" value="on" property="siteLogoFlag"/>
                        </c:if>
                        &nbsp;&nbsp;
                        </span>
                      </span>
                      
                      <script>
                      var logoOptionsMenu = [
                          { text: "Upload", value: 1, onclick: { fn: showLogoWindow } },
                          { text: "Remove", value: 2, onclick: { fn: removeImage } }
                      ];
                      var butLogo = new YAHOO.widget.Button({
                            disabled: true,
                            type: "menu", 
                            label: "Options", 
                            name: "logoOptions",
                            menu: logoOptionsMenu, 
                            container: "logoOptionsId" });
                      <c:if test="${siteDomainMaintForm.siteProfileClassDefault || siteDomainMaintForm.siteLogoFlag}">
                        butLogo.set('disabled', false);
                      </c:if>
                      </script>
                      </td>
                    </tr>
                    <tr>
                      <td>
                      Logo to be shown on the public site.  The acual placement is determined by the template selected.
                      </td>
                    </tr>
                    <tr>
                      <td width="100%">&nbsp;</td>
                    </tr>
                    <tr>
                      <td width="100%">
                        <div id='divLogo'>
                        <c:choose>
                          <c:when test="${siteDomainMaintForm.siteProfileClassDefault}">
                            <c:if test="${siteDomainMaintForm.siteLogoContentType != null}"> 
                              <img id="imgLogo" src="/${adminBean.contextPath}/services/SecureImageProvider.do?type=S&maxsize=100&imageId=${siteDomainMaintForm.defaultSiteDomainLangId}&random=${siteDomainMaintForm.random}"><br>
                            </c:if>
                          </c:when>
                          <c:otherwise>
                            <c:choose>
                              <c:when test="${siteDomainMaintForm.siteLogoFlag}">
                                <c:if test="${siteDomainMaintForm.siteLogoContentTypeLang != null}"> 
                                  <img id="imgLogo" src="/${adminBean.contextPath}/services/SecureImageProvider.do?type=S&maxsize=100&imageId=${siteDomainMaintForm.siteDomainLangId}&random=${siteDomainMaintForm.random}"><br>
                                </c:if>
                              </c:when>
                              <c:otherwise>
                                <c:if test="${siteDomainMaintForm.siteLogoContentType != null}"> 
                                  <img id="imgLogo" src="/${adminBean.contextPath}/services/SecureImageProvider.do?type=S&maxsize=100&imageId=${siteDomainMaintForm.defaultSiteDomainLangId}&random=${siteDomainMaintForm.random}"><br>
                                </c:if>
                              </c:otherwise>
                            </c:choose>
                          </c:otherwise>
                        </c:choose>
                        </div>
                      </td>
                    </tr>
                  </table>
                  <br>
                </div>
                <div id="mail" style="padding: 5px; min-height:300px">
                  <table border="0" cellspacing="0" cellpadding="3" width="100%">
                    <tr> 
                      <td class="jc_input_label">Password Reset email from</td>
                    </tr>
                    <tr>
                      <td>
                      Specify the 'mail from' address when delivery password to user during password reset. 
                      </td>
                    </tr>
                    <tr>
                      <td class="jc_input_label"> <lang:text name="siteDomainMaintForm" property="mailFromPwdReset" size="40" styleClass="jc_input_object"/> 
                      </td>
                    </tr>
                    <tr> 
                      <td width="200" class="jc_input_label">Password Reset email subject line</td>
                    </tr>
                    <tr>
                      <td>
                      Specify the subject line of the email to be sent
                      <lang:checkboxSwitch name="siteDomainMaintForm" property="subjectPwdResetLangFlag"> - Override language</lang:checkboxSwitch>
                      </td>
                    </tr>
                    <tr>
                      <td width="200" class="jc_input_label">
                        <lang:text name="siteDomainMaintForm" property="subjectPwdReset" size="80" styleClass="jc_input_object"/> 
                      </td>
                    </tr>
                    <tr>
                      <td><hr></td>
                    </tr>
                    <tr> 
                      <td width="200" class="jc_input_label">Customer sales confirmation email from</td>
                    </tr>
                    <tr>
                      <td>
                      Specify the 'mail from' address when sending the sales confirmation email
                      </td>
                    </tr>
                    <tr>
                      <td width="200" class="jc_input_label">
                        <lang:text name="siteDomainMaintForm" property="mailFromCustSales" size="40" styleClass="jc_input_object"/> 
                      </td>
                    </tr>
                    <tr> 
                      <td width="200" class="jc_input_label">Customer sales confirmation email subject line</td>
                    </tr>
                    <tr>
                      <td>
                      Specify the subject line of the email to be sent. 
                      <lang:checkboxSwitch name="siteDomainMaintForm" property="subjectCustSalesLangFlag"> - Override language</lang:checkboxSwitch>
                      </td>
                    </tr>
                    <tr>
                      <td width="200" class="jc_input_label">
                        <lang:text name="siteDomainMaintForm" property="subjectCustSales" size="80" styleClass="jc_input_object"/> 
                      </td>
                    </tr>
                    <tr>
                      <td><hr></td>
                    </tr>
                    <tr>
                      <td class="jc_input_label">
                      Sales notification email
                      </td>
                    </tr>
                    <tr>
                      <td>
                      Automatic generate email to this address once a sale is completed.<br>
                      Leave it blank if no automatic email is required.
                      </td>
                    </tr>
                    <tr>
                      <td width="100%"><lang:text name="siteDomainMaintForm" property="checkoutNotificationEmail" styleClass="jc_input_object" size="40"/></td>
                    </tr>
                    <tr> 
                      <td width="200" class="jc_input_label">Sales notification internal email from</td>
                    </tr>
                    <tr>
                      <td>
                      Specify the 'mail from' address when sending the sales notification internal email
                      </td>
                    </tr>
                    <tr>
                      <td width="200" class="jc_input_label">
                        <lang:text name="siteDomainMaintForm" property="mailFromNotification" size="40" styleClass="jc_input_object"/> 
                      </td>
                    </tr>
                    <tr> 
                      <td width="200" class="jc_input_label">Customer sales notification internal email subject line</td>
                    </tr>
                    <tr>
                      <td>
                      Specify the subject line of the email to be sent. 
                      <lang:checkboxSwitch name="siteDomainMaintForm" property="subjectNotificationLangFlag"> - Override language</lang:checkboxSwitch>
                      </td>
                    </tr>
                    <tr>
                      <td width="200" class="jc_input_label">
                        <lang:text name="siteDomainMaintForm" property="subjectNotification" size="80" styleClass="jc_input_object"/> 
                      </td>
                    </tr>
                    <tr>
                      <td><hr></td>
                    </tr>
                    <tr> 
                      <td width="200" class="jc_input_label">Shipping quotation email from</td>
                    </tr>
                    <tr>
                      <td>
                      Specify the 'mail from' address when sending shipping quotation email
                      </td>
                    </tr>
                    <tr>
                      <td width="200" class="jc_input_label">
                        <lang:text name="siteDomainMaintForm" property="mailFromShippingQuote" size="40" styleClass="jc_input_object"/> 
                      </td>
                    </tr>
                    <tr> 
                      <td width="200" class="jc_input_label">Shipping quotation email subject line</td>
                    </tr>
                    <tr>
                      <td>
                      Specify the subject line of the email to be sent. 
                      <lang:checkboxSwitch name="siteDomainMaintForm" property="subjectShippingQuoteLangFlag"> - Override language</lang:checkboxSwitch>
                      </td>
                    </tr>
                    <tr>
                      <td width="200" class="jc_input_label">
                        <lang:text name="siteDomainMaintForm" property="subjectShippingQuote" size="80" styleClass="jc_input_object"/> 
                      </td>
                    </tr>
                    <tr>
                      <td><hr></td>
                    </tr>
                    <tr> 
                      <td width="200" class="jc_input_label">Contact us email from</td>
                    </tr>
                    <tr>
                      <td>
                      Specify the 'mail from' address when sending the contact us email
                      </td>
                    </tr>
                    <tr>
                      <td width="200" class="jc_input_label">
                        <lang:text name="siteDomainMaintForm" property="mailFromContactUs" size="40" styleClass="jc_input_object"/> 
                      </td>
                    </tr>
                  </table>
                  <br>
                </div>
                <div id="business" style="padding: 5px; min-height:300px">
                  <table border="0" cellspacing="0" cellpadding="3">
                    <tr>
                      <td>
                      Business contact information when performing monetary transaction.  Please make sure
                      the following information matches your business's bank account to avoid any confusion
                      in the future.
                      </td>
                    </tr>
                    <tr>
                      <td>
                      &nbsp;
                      </td>
                    </tr>
                    <tr> 
                      <td width="200" class="jc_input_label">Contact Name</td>
                    </tr>
                    <tr>
                      <td><lang:text name="siteDomainMaintForm" property="businessContactName" maxlength="60" size="60" styleClass="jc_input_object"/></td>
                    </tr>
                    <tr> 
                      <td class="jc_input_label">Company</td>
                    </tr>
                    <tr>
                      <td><lang:text name="siteDomainMaintForm" property="businessCompany" maxlength="60" size="60" styleClass="jc_input_object"/></td>
                    </tr>
                    <tr> 
                      <td class="jc_input_label">Address Line 1</td>
                    </tr>
                    <tr>
                      <td><lang:text name="siteDomainMaintForm" property="businessAddress1" maxlength="60" size="60" styleClass="jc_input_object"/></td>
                    </tr>
                    <tr> 
                      <td class="jc_input_label">Address Line 2</td>
                    </tr>
                    <tr>
                      <td><lang:text name="siteDomainMaintForm" property="businessAddress2" maxlength="60" size="60" styleClass="jc_input_object"/></td>
                    </tr>
                    <tr> 
                      <td class="jc_input_label">City</td>
                    </tr>
                    <tr>
                      <td><lang:text name="siteDomainMaintForm" property="businessCity" maxlength="30" size="30" styleClass="jc_input_object"/></td>
                    </tr>
                    <tr> 
                      <td width="200" class="jc_input_label">State/Province</td>
                    </tr>
                    <tr>
                      <td width="200" class="jc_input_label"> <lang:select name="siteDomainMaintForm" property="businessStateCode"> 
                        <lang:optionsCollection property="states" label="label"/> 
                        </lang:select> </td>
                    </tr>
                    <tr> 
                      <td width="200" class="jc_input_label">Country</td>
                    </tr>
                    <tr>
                      <td width="200" class="jc_input_label"> 
                        <lang:select name="siteDomainMaintForm" property="businessCountryCode"> 
                          <lang:optionsCollection property="countries" label="label"/> 
                        </lang:select>
                      </td>
                    </tr>                    
                    <tr> 
                      <td class="jc_input_label">Postal / Zip Code</td>
                    </tr>
                    <tr>
                      <td><lang:text name="siteDomainMaintForm" property="businessPostalCode" maxlength="30" size="30" styleClass="jc_input_object"/></td>
                    </tr>
                    <tr> 
                      <td class="jc_input_label">Phone</td>
                    </tr>
                    <tr>
                      <td><lang:text name="siteDomainMaintForm" property="businessPhone" maxlength="30" size="30" styleClass="jc_input_object"/></td>
                    </tr>
                    <tr> 
                      <td class="jc_input_label">Fax</td>
                    </tr>
                    <tr>
                      <td><lang:text name="siteDomainMaintForm" property="businessFax" maxlength="30" size="30" styleClass="jc_input_object"/></td>
                    </tr>
                    <tr> 
                      <td class="jc_input_label">Email</td>
                    </tr>
                    <tr>
                      <td><lang:text name="siteDomainMaintForm" property="businessEmail" maxlength="40" size="40" styleClass="jc_input_object"/></td>
                    </tr>
                  </table>
                  <br>
                </div>
                <div id="checkout" style="padding: 5px; min-height:300px">               
                  <table border="0" cellspacing="0" cellpadding="3" width="100%">
                    <tr>
                      <td class="jc_input_label">
                      Authorization Mode
                      </td>
                    </tr>
                    <tr>
                      <td>
						<html:select property="paymentProcessType"> 
						  <html:option value="A">Authorize only</html:option>
						  <html:option value="C">Authorize and Capture</html:option>
						</html:select>
                      </td>
                    </tr>
                    <tr>
                      <td class="jc_input_label">
                      Automatically includes 'pick-up' shipping option during checkout
                      </td>
                    </tr>
                    <tr>
                      <td>
						<lang:checkbox name="siteDomainMaintForm" property="checkoutIncludePickup" value="Y" styleClass="tableContent"/>
                      </td>
                    </tr>
                    <tr>
                      <td class="jc_input_label">
                      Allows customer to request for shipping quote during checkout if no appropriate shipping method can be found.
                      </td>
                    </tr>
                    <tr>
                      <td>
						<lang:checkbox name="siteDomainMaintForm" property="checkoutAllowsShippingQuote" value="Y" styleClass="tableContent"/>
                      </td>
                    </tr>
                    <tr> 
                      <td class="jc_input_label">
                        Shopping Cart Message
                        <lang:checkboxEditorSwitch name="siteDomainMaintForm" property="checkoutShoppingCartMessageLangFlag"> - Override language</lang:checkboxEditorSwitch>
                      </td>
                    </tr>
                    <tr>
                      <td>
                      The following message will be shown at the bottom of every page during check-out process.
                      </td>
                    </tr>
                    <tr>
                      <td width="100%">
                        <lang:editorText name="siteDomainMaintForm" property="checkoutShoppingCartMessage" height="200" width="100%" toolBarSet="Simple"/>
                    </tr>
                  </table>
                </div>
                <div id="template" style="padding: 5px; min-height:300px">
                  <table border="0" cellspacing="0" cellpadding="3" width="100%">
                    <tr> 
                      <td class="jc_input_label">Template</td>
                    </tr>
                    <tr>
					  <td class="jc_input_control">
			            <html:select property="templateId"> 
			              <html:optionsCollection property="templates" value="value" label="label"/> 
			            </html:select>
					  </td>
                    </tr>
                    <tr> 
                      <td class="jc_input_label">Module display size</td>
                    </tr>
                    <tr> 
                      <td>
                      Number of records to be shown in the module.  These modules include top rated content, 
                      most viewed content and most popular items.
                      </td>
                    </tr>
                    <tr> 
                      <td>
                        <lang:text name="siteDomainMaintForm" property="moduleDisplaySize" maxlength="10" size="10" styleClass="jc_input_object"/>
                      </td>
                    </tr>
                    <tr>
                      <td class="jc_input_error">
		            	<logic:messagesPresent property="moduleRecordNum" message="true"> 
		                <html:messages property="moduleRecordNum" id="errorText" message="true"> <bean:write name="errorText"/> 
		                </html:messages> </logic:messagesPresent>
                      </td>
                    </tr>
                  </table>
                  <br>
                </div>
              </div>
            </div>
            </div>
          </td>
          </layout:mode>
        </tr>
      </table>
    </td>
  </tr>
</table>
</html:form>

<!------------------------------------------------------------------------>

<script>
var jc_logo_panel = null;

function jc_logo_init() {
  jc_logo_panel = new YAHOO.widget.Panel("jc_logo_panel", 
                              { 
                                width: "300px", 
                                visible: false, 
                                constraintoviewport: true ,
                                fixedcenter : true,
                                modal: true
                              } 
                               );
  jc_logo_panel.render();
}
YAHOO.util.Event.onDOMReady(jc_logo_init);

var jc_upload_callback = {
  upload: function(o) {
    var value = o.responseText.replace(/<\/?pre>/ig, '');
    var object = eval('(' + value + ')');
    if (object.status == 'failed') {
      var fn = document.getElementById('filename');
      fn.innerHTML = object.filename;
    }
    else {
      var location = document.getElementById('divLogo');
      var context = "<c:out value='${adminBean.contextPath}'/>";
      var siteDomainId = "<c:out value='${siteDomainMaintForm.siteDomainId}'/>";
      var siteProfileClassId = "<c:out value='${siteDomainMaintForm.siteProfileClassId}'/>";
      location.innerHTML = '';
      var image = document.createElement('img');
      location.appendChild(image);
      if (siteProfileClassDefault) {
        image.src = '/' + context + '/services/SecureImageProvider.do?type=S&maxsize=100&imageId=' + ${siteDomainMaintForm.defaultSiteDomainLangId} + '&imageCounter=' + imageCounter++;
      }
      else {
        image.src = '/' + context + '/services/SecureImageProvider.do?type=S&maxsize=100&imageId=' + ${siteDomainMaintForm.siteDomainLangId} + '&imageCounter=' + imageCounter++;
      }
      jc_logo_panel.hide();
    }
  }
}
function jc_uploadImage() {
  YAHOO.util.Connect.setForm('siteDomainMaintForm1', true, true);

  var url = "/<c:out value='${adminBean.contextPath}'/>/admin/site/siteDomainMaint.do";
  var response = YAHOO.util.Connect.asyncRequest('POST', url, jc_upload_callback);
  
}

var jc_remove_callback = {
  success: function(o) {
    var location = document.getElementById('divLogo');
    location.innerHTML = '';
  },
  failure: function(o) {
      alert('unable to remove image');
      alert(o);
  }
}
function jc_removeImage() {
  var context = "/${adminBean.contextPath}";
  var siteDomainId = "${siteDomainMaintForm.siteDomainId}";
  var siteProfileClassId = "${siteDomainMaintForm.siteProfileClassId}";
  var url = context + '/admin/site/siteDomainMaint.do?process=removeImage&siteDomainId=' + siteDomainId + '&&siteProfileClassId=' + siteProfileClassId + '&siteDomainId=' + siteDomainId;
  var response = YAHOO.util.Connect.asyncRequest('GET', url, jc_remove_callback);
}

var jc_reset_callback = {
  success: function(o) {
  },
  failure: function(o) {
      alert('unable to reset image');
      alert(o);
  }
}
function jc_resetLanguageImage(activate) {
  var context = "/${adminBean.contextPath}";
  var siteDomainId = "${siteDomainMaintForm.siteDomainId}";
  var siteProfileClassId = "${siteDomainMaintForm.siteProfileClassId}";
  var url = context + '/admin/site/siteDomainMaint.do?process=resetLanguageImage&siteDomainId=' + siteDomainId + '&siteProfileClassId=' + siteProfileClassId + '&activate=' + activate;
  var response = YAHOO.util.Connect.asyncRequest('GET', url, jc_reset_callback);
}
</script>
<form name="siteDomainMaintForm1" method="post" action="/<c:out value='${adminBean.contextPath}'/>/admin/site/siteDomainMaint.do" enctype="multipart/form-data" target="upload_target"> 
<input type="hidden" name="siteDomainId" value="<c:out value='${siteDomainMaintForm.siteDomainId}'/>"> 
<input type="hidden" name="siteProfileClassId" value="<c:out value='${siteDomainMaintForm.siteProfileClassId}'/>"> 
<input type="hidden" name="process" value="uploadImage"> 
<div class=" yui-skin-sam">
<div>
<div id="jc_logo_panel">
  <div class="hd">Site Logo</div>
  <div class="bd"> 
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr> 
        <td>
          Upload new logo for site
        </td>
      </tr>
      <tr>
         <td>
         <input type="file" name="file" class="jc_input_object">
         </td>
      </tr>
      <tr>
         <td class="jc_input_error"><div id="filename"></div></td>
      </tr>
      <tr>
         <td>&nbsp;</td>
      </tr>
      <tr>
        <td>
          <a href="javascript:void(0);" onclick="jc_uploadImage();" class="jc_navigation_link">Confirm</a>
          <a href="javascript:void(0);" onclick="jc_logo_panel.hide()" class="jc_navigation_link">Cancel</a>&nbsp;
        </td>
      </tr>
    </table>
  </div>
</div>
</div>
</div>
</form>

<!------------------------------------------------------------------------>
<%@ include file="/admin/include/confirm.jsp" %>
<!------------------------------------------------------------------------>
<c:if test="${siteDomainMaintForm.mode != 'C'}"> 
<script type="text/javascript">
var tabView = new YAHOO.widget.TabView("tabPanel");
tabView.set("activeIndex", ${siteDomainMaintForm.tabIndex});
var tab1 = tabView.getTab(1);
tab1.addListener('click', function(e) {
	jc_editor_templateFooter.show();
} );
var tab5 = tabView.getTab(5);
tab5.addListener('click', function(e) {
	jc_editor_checkoutShoppingCartMessage.show();
} );
</script>
</c:if>