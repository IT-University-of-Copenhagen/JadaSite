<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<script type="text/javascript" src="http://www.google.com/recaptcha/api/js/recaptcha_ajax.js"></script>
<script type="text/javascript" language="JavaScript"><!--
function submitForm(type) {
    document.myAccountForgotActionForm.process.value = type;
    document.myAccountForgotActionForm.submit();
}

YAHOO.util.Event.onContentReady('recaptcha_div', function() {
    if ("${myAccountForgotActionForm.enableCaptcha}" == "Y") {
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
		Recaptcha.create("${myAccountForgotActionForm.captchaPublicKey}", "recaptcha_div", options);
    }
} );

//--></script>
<c:set var="myAccountForgotActionForm" scope="request" value="${myAccountForgotActionForm}"/>
<div id="my-account-container">
<div id="my-account-public-header-container">
  <span id="my-account-public-header"><lang:contentMessage value="Forgot your password?"/></span>
</div>
<br>
<html:form method="post" action="/myaccount/forgot/myAccountForgot.do?prefix=${contentBean.siteDomain.siteDomainPrefix}" onsubmit="javascript:submitForm('reset');">
<span class="jc_text_normal">
<lang:contentError field="msg"/>
</span>
<span class="jc_text_normal">
<lang:contentMessage key="content.text.myaccount.forgot.message"/>
</span>
<br><br>
<html:hidden property="process" value=""/>
<table border="0" cellspacing="0" cellpadding="0" class="jc_tran_table" width="400">
  <tr id="my-account-public-table-header">
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td nowrap>&nbsp;</td>
  </tr>
  <tr> 
    <td nowrap><span class="my-account-public-label"><lang:contentMessage value="Email address"/></span></td>
  </tr>
  <tr>
    <td>
      <html:text property="custEmail" size="30" styleClass="my-account-public-value"/>
      <span class="my-account-public-error"><lang:contentError field="custEmail"/></span>
    </td>
  </tr>
  <tr> 
    <td>
      <span class="my-account-public-error"><lang:contentError field="forgot"/></span>
    </td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td>
      <!-- empty div element in which the reCAPTCHA object will be placed -->
	  <div id="recaptcha_div"></div>
	  <span class="jc_alert"><lang:contentError field="recaptchaUserResponse"/></span>
	</td>
  </tr>
  <tr> 
    <td nowrap class="jc_tran_reverse">
      <html:link href="javascript:submitForm('reset');" styleClass="jc_shopping_cart_button">
        <lang:contentMessage value="Continue"/>
      </html:link>
    </td>
  </tr>
</table>
</html:form>
<br>
<br>
</div>