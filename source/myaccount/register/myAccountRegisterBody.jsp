<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<script type="text/javascript" src="http://www.google.com/recaptcha/api/js/recaptcha_ajax.js"></script>
<script type="text/javascript" language="JavaScript"><!--
function submitForm(type) {
    document.myAccountRegisterActionForm.process.value = type;
    document.myAccountRegisterActionForm.submit();
}

YAHOO.util.Event.onContentReady('recaptcha_div', function() {
    if ("${myAccountRegisterActionForm.enableCaptcha}" == "Y") {
    	var options = {
    	    		theme: "clean",
    	    		callback: Recaptcha.focus_response_field,
    			   	custom_translations : { instructions_visual : 'unknown',
								            instructions_audio : 'unknown',
								            play_again : '<lang:contentMessage value="Play sound again"/>',
								            cant_hear_this : '<lang:contentMessage value="Download sound as MP3"/>',
								            visual_challenge : '<lang:contentMessage value="Get a visual challenge"/>',
								            audio_challenge : '<lang:contentMessage value="Get an audio challenge"/>',
								            refresh_btn : '<lang:contentMessage value="Get a new challenge"/>',
								            help_btn : '<lang:contentMessage value="Help"/>',
								            incorrect_try_again : "unknown" }
    			};
		Recaptcha.create("${myAccountRegisterActionForm.captchaPublicKey}", "recaptcha_div", options);
    }
} );
//--></script>
<c:set var="myAccountRegisterActionForm" scope="request" value="${myAccountRegisterActionForm}"/>
<div id="my-account-container">
<div id="my-account-public-header-container">
  <span id="my-account-public-header"><lang:contentMessage value="Account registration"/></span>
</div>
<br>
<html:form method="post" action="/myaccount/register/myAccountRegister.do?process=update&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}">
<html:hidden property="process" value=""/>
<html:hidden property="url"/>
<table width="500" cellspacing="0" cellpadding="3" id="my-account-public-table">
  <tr id="my-account-public-table-header">
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td>
      <logic:messagesPresent property="message" message="true">
        <html:messages property="message" id="m" message="true">
        <span class="jc_text_normal jc_blue"><bean:write name="m"/></span>
        </html:messages>
      </logic:messagesPresent>
    </td>
  </tr>
  <tr> 
    <td nowrap><span class="my-account-public-label"><lang:contentMessage value="Email address (login name)"/></span>
    </td>
  </tr>
  <tr>
    <td>
      <html:text property="custEmail1" size="40" maxlength="40" styleClass="my-account-public-value"/>
      <span class="my-account-public-error"><lang:contentError field="custEmail1"/></span>
    </td>
  </tr>
  <tr> 
    <td nowrap><span class="my-account-public-label"><lang:contentMessage value="Re-enter Email address"/></span>
    </td>
  </tr>
  <tr>
    <td>
      <html:text property="custEmail2" size="40" maxlength="40" styleClass="my-account-public-value"/>
      <span class="my-account-public-error"><lang:contentError field="custEmail2"/></span>
      <span class="my-account-public-error"><lang:contentError field="emailMatch"/></span>
    </td>
  </tr>
  <tr> 
    <td><span class="my-account-public-label"><lang:contentMessage value="Password"/></span><br>
    <span class="jc_text_normal"><lang:contentMessage key="content.text.passwordMessage"/></span>
    </td>
  </tr>
  <tr>
    <td>
      <html:password property="custPassword" size="20" maxlength="20" styleClass="my-account-public-value"/>
      <span class="my-account-public-error"><lang:contentError field="custPassword"/></span>
    </td>
  </tr>
  <tr> 
    <td nowrap><span class="my-account-public-label"><lang:contentMessage value="Re-enter password"/></span></td>
  </tr>
  <tr>
    <td>
      <html:password property="custPassword1" size="20" maxlength="20" styleClass="my-account-public-value"/>
      <span class="my-account-public-error"><lang:contentError field="custPassword1"/></span>
    </td>
  </tr>
  <tr> 
    <td>
      <span class="my-account-public-error"><lang:contentError field="custPasswordNoMatch"/></span>
    </td>
  </tr>
  <tr> 
    <td nowrap><span class="my-account-public-label"><lang:contentMessage value="Public name"/></span><br>
    <span class="jc_text_normal"><lang:contentMessage value="Name that will be known to public"/></span>
    </td>
  </tr>
  <tr>
    <td>
      <div class="my-account-public-label"><html:text property="custPublicName" size="20" maxlength="20"/></div>
      <span class="my-account-public-error"><lang:contentError field="custPublicName"/></span>
    </td>
  </tr>
  <tr> 
    <td>
      <!-- empty div element in which the reCAPTCHA object will be placed -->
	  <div id="recaptcha_div"></div>
	  <span class="my-account-public-error"><lang:contentError field="recaptchaUserResponse"/></span>
	</td>
  </tr>
  <tr> 
    <td class="jc_tran_reverse">
      <html:link href="javascript:submitForm('update');" styleClass="jc_shopping_cart_button">
        <lang:contentMessage value="Register"/>
      </html:link>
    </td>
  </tr>
</table>
</html:form>
</div>