<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="couponMaintForm" scope="request" value="${couponMaintForm}"/>
<script language="JavaScript">

var siteId = '${siteMaintForm.siteId}';
var mode = '${siteMaintForm.mode}';
function submitForm(type) {
    document.siteMaintForm.process.value = type;
    document.siteMaintForm.submit();
    return false;
}
function submitCancelForm() {
    document.siteMaintForm.action = "/${adminBean.contextPath}/admin/site/siteListing.do";
    document.siteMaintForm.process.value = "back";
    document.siteMaintForm.submit();
    return false;
}

/*
 * Add and remove items
 */

function addDomain() {
	window.location = '/${adminBean.contextPath}/admin/site/siteDomainMaint.do?process=create&siteId=' + siteId;
}

var jc_removeDomains_callback = {
	success: function(o) {
		if (!isJsonResponseValid(o.responseText)) {
		    jc_panel_show_error("Unexcepted Error: Unable to remove domains");
		    return false;
		}
		var jsonObject = getJsonObject(o.responseText);
		if (!isJsonResponseSuccess(o.responseText)) {
			jc_panel_show_error("Unexcepted Error: " + jsonObject.message);
			return false;
		}
		showSiteDomains();
	},
	failure: function(o) {
		jc_panel_show_error("Unexcepted Error: Unable to remove domains");
	}	
};

function removeDomains() {
	var url = "/${adminBean.contextPath}/admin/site/siteMaint.do";
	var postData = "process=removeSiteDomains&siteId=" + siteId;
	var objects = YAHOO.util.Dom.getElementsByClassName('siteDomainId');
	for (var i = 0; i < objects.length; i++) {
		if (objects[i].checked) {
			postData += "&siteDomainIds=" + objects[i].value;
		}
	}
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_removeDomains_callback, postData);
}

YAHOO.util.Event.onAvailable('siteDomainsButtons', function() {
	var buttonMenu = [
			{ text: "Add sub-site", value: 'Add sub-site', onclick: { fn: addDomain } }, 
			{ text: "Remove sub-sites", value: 'Remove sub-sites', onclick: { fn: removeDomains } }
	];
	var menu = new YAHOO.widget.Button({ 
	      				  disabled: true,
                          type: "menu", 
                          label: "Options", 
                          name: "menu",
                          menu: buttonMenu, 
                          container: 'siteDomainsButtons' });
	if (mode != 'C') {
		  menu.set('disabled', false);
	}
} );

var showSiteDomains_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: Unable to extract domain list");
      return false;
    }
    var node = document.getElementById("siteDomainsContainer");
    node.innerHTML = '';
    var table = document.createElement('table');
    table.setAttribute('width', '100%');
    node.appendChild(table);
    var body = document.createElement('tbody');
    table.appendChild(body);
    
    var jsonObject = getJsonObject(o.responseText);
    if (jsonObject.siteDomains.length > 0) {
        var row = document.createElement('tr');
        body.appendChild(row);
        var col;
        col = document.createElement('td');
        col.appendChild(document.createTextNode(''));
        col.className = 'jc_input_label';
        row.appendChild(col);
        col = document.createElement('td');
        col.appendChild(document.createTextNode('Sub-site'));
        col.className = 'jc_input_label';
        row.appendChild(col);
        col = document.createElement('td');
        col.appendChild(document.createTextNode('Port'));
        col.className = 'jc_input_label';
        row.appendChild(col);
        col = document.createElement('td');
        col.className = 'jc_input_label';
        col.appendChild(document.createTextNode('Prefix'));
        row.appendChild(col);
        col = document.createElement('td');
        col.className = 'jc_input_label';
        var div = document.createElement('div');
        div.setAttribute('align', 'center');
        div.appendChild(document.createTextNode('Master'));
        col.appendChild(div);
        row.appendChild(col);
    }
    for (var i = 0; i < jsonObject.siteDomains.length; i++) {
        var siteDomain = jsonObject.siteDomains[i];
        var row = document.createElement('tr');
        body.appendChild(row);
        var col;
        col = document.createElement('td');
        row.appendChild(col);
        var checkbox = document.createElement('input');
        checkbox.className='siteDomainId';
        checkbox.setAttribute('type', 'checkbox');
        checkbox.setAttribute('name', 'siteDomains');
        checkbox.setAttribute('value', siteDomain.siteDomainId);
        col.appendChild(checkbox);
        if (siteDomain.master) {
            checkbox.disabled = true;
        }
        col = document.createElement('td');
        row.appendChild(col);
		var link = document.createElement('a');
		link.setAttribute('href', '/${adminBean.contextPath}/admin/site/siteDomainMaint.do?process=edit&siteDomainId=' + siteDomain.siteDomainId);
		link.appendChild(document.createTextNode(siteDomain.siteName));
        col.appendChild(link);
        
        col = document.createElement('td');
        row.appendChild(col);
        col.appendChild(document.createTextNode(siteDomain.sitePublicPortNum));
        col = document.createElement('td');
        row.appendChild(col);
        col.appendChild(document.createTextNode(siteDomain.siteDomainPrefix));
        col = document.createElement('td');
        row.appendChild(col);
        var div = document.createElement('div');
        div.setAttribute('align', 'center');
        var value = 'N';
        if (siteDomain.master) {
            value = 'Y';
        }
        div.appendChild(document.createTextNode(value));
        col.appendChild(div);
    }
  },
  failure: function(o) {
      jc_panel_show_error("Unexcepted Error: Unable to extract domain list");
  }
};

function showSiteDomains() {
	var url = "/${adminBean.contextPath}/admin/site/siteMaint.do?process=listSiteDomains&siteId=" + siteId;
	var request = YAHOO.util.Connect.asyncRequest('GET', url, showSiteDomains_callback);
}

/*************************************************************/

if (mode != 'C') {
	YAHOO.util.Event.onAvailable('siteDomainsContainer', function() {
		showSiteDomains();
	} );
}
</script>
<div class=" yui-skin-sam">
<html:form method="post" action="/admin/site/siteMaint"> 
<html:hidden property="process" value=""/> 
<html:hidden property="mode"/> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - <a href="/<c:out value='${adminBean.contextPath}'/>/admin/site/siteListing.do?process=back">Site 
      Listing</a> - Site Maintenance</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr> 
    <td width="100%" valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
        <tr> 
          <td>&nbsp;
          </td>
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
      <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr>
          <td valign="top" width="400">
	        <div class="jc_detail_panel_header">
	          <table width="100%" border="0" cellspacing="0" cellpadding="0">
	            <tr>
	              <td><span class="jc_input_label">Domains</span></td>
	              <td><div align="right" id="siteDomainsButtons"></div></td>
	            </tr>
	          </table>
	        </div>
	        <div>
	          <div id="siteDomainsContainer" class="jc_detail_panel"></div>
	        </div>
	        <br>
	        <div class="jc_detail_panel_header">
	          <table width="100%" border="0" cellspacing="0" cellpadding="0">
	            <tr>
	              <td><span class="jc_input_label">General</span></td>
	            </tr>
	          </table>
	        </div>
	        <div class="jc_detail_panel">
	          <table width="100%" border="0" cellspacing="0" cellpadding="5">
	            <tr>
	              <td><span class="jc_input_label">Manage Inventory</span></td>
	            </tr>
	            <tr>
	              <td>
	                <html:checkbox property="manageInventory" value="Y" styleClass="jc_input_control"/> 
	              </td>
	            </tr>
	            <tr>
	              <td><span class="jc_input_label">Share Inventory</span></td>
	            </tr>
	            <tr>
	              <td>
	                <html:checkbox property="shareInventory" value="Y" styleClass="jc_input_control"/> 
	              </td>
	            </tr>
	            <tr>
	              <td><span class="jc_input_label">Single Check-out</span></td>
	            </tr>
	            <tr>
	              <td>
	                <html:checkbox property="singleCheckout" value="Y" styleClass="jc_input_control"/> 
	              </td>
	            </tr>
	            <tr>
	              <td><span class="jc_input_label">Listing page size</span></td>
	            </tr>
	            <tr>
	              <td>
	              Number of entries to be shown per page during search.
	              </td>
	            </tr>
	            <tr>
	              <td>
	                <html:text property="listingPageSize" size="10" styleClass="jc_input_control"/> 
	              </td>
	            </tr>
	            <tr>
	              <td><span class="jc_input_label">Store credit card on file</span></td>
	            </tr>
	            <tr>
	              <td>
	                <html:checkbox property="storeCreditCard" value="Y" styleClass="jc_input_control"/> 
	              </td>
	            </tr>
	          </table>
	        </div>
	        <br>
	        <div class="jc_detail_panel_header">
	          <table width="100%" border="0" cellspacing="0" cellpadding="0">
	            <tr>
	              <td><span class="jc_input_label">Mail server configuration</span></td>
	            </tr>
	          </table>
	        </div>
	        <div class="jc_detail_panel">
	          <table width="100%" border="0" cellspacing="0" cellpadding="5">
                <tr> 
                  <td class="jc_input_label">Mail outgoing (SMTP) host</td>
                </tr>
                <tr>
                  <td>
                  SMTP server address
                  </td>
                </tr>
                <tr>
                  <td class="jc_input_label"> <html:text name="siteMaintForm" property="mailSmtpHost" size="40" styleClass="jc_input_object"/> 
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Mail outgoing (SMTP) port</td>
                </tr>
                <tr>
                  <td>
                  SMTP server port number. If not specified, port number will be defaulted to 25.
                  </td>
                </tr>
                <tr>
                  <td class="jc_input_label"> <html:text name="siteMaintForm" property="mailSmtpPort"  size="10" styleClass="jc_input_object"/> 
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Mail outgoing (SMTP) account</td>
                </tr>
                <tr>
                  <td>
                  Account name to be used when connecting to SMTP server.
                  Leave it empty is authentication is not required.
                  </td>
                </tr>
                <tr>
                  <td class="jc_input_label"> <html:text name="siteMaintForm" property="mailSmtpAccount" size="40" styleClass="jc_input_object"/> 
                  </td>
                </tr>
                <tr> 
                  <td class="jc_input_label">Mail outgoing (SMTP) password</td>
                </tr>
                <tr>
                  <td>
                  Password to be used when connecting to SMTP server if authentication is required.<br>
                  This field contains sensitive information and is not displayed.  Enter a value if change is required.
                  Enter a space if this field is to be cleared.  Otherwise, leave this field as blank and existing value will not be altered.
                  </td>
                </tr>
                <tr>
                  <td class="jc_input_label"> <html:password name="siteMaintForm" property="mailSmtpPassword"/> 
                  </td>
                </tr>
	          </table>
	        </div>
	        <br>
	        <div class="jc_detail_panel_header">
	          <table width="100%" border="0" cellspacing="0" cellpadding="0">
	            <tr>
	              <td><span class="jc_input_label">reCAPTCHA</span></td>
	            </tr>
	          </table>
	        </div>
	        <div class="jc_detail_panel">
	          <table width="100%" border="0" cellspacing="0" cellpadding="5">
	            <tr>
	              <td><span class="jc_input_label">Enable Captcha</span></td>
	            </tr>
	            <tr>
	              <td>
	                <html:checkbox property="enableCaptcha" value="Y" styleClass="jc_input_control"/> 
	              </td>
	            </tr>
	            <tr>
	              <td><span class="jc_input_label">Captcha private key</span></td>
	            </tr>
	            <tr>
	              <td>
	                <html:password property="captchaPrivateKey" size="40" styleClass="jc_input_control"/>
	                <logic:messagesPresent property="captchaPrivateKey" message="true"> 
	                  <br>
	                  <html:messages property="captchaPrivateKey" id="errorText" message="true"> 
	                  <span class="jc_input_error"><bean:write name="errorText"/></span>
	                  </html:messages>
	                </logic:messagesPresent>
	              </td>
	            </tr>
	            <tr>
	              <td><span class="jc_input_label">Captcha public key</span></td>
	            </tr>
	            <tr>
	              <td>
	                <html:password property="captchaPublicKey" size="40" styleClass="jc_input_control"/>
	                <logic:messagesPresent property="captchaPublicKey" message="true"> 
	                  <br>
	                  <html:messages property="captchaPublicKey" id="errorText" message="true"> 
	                  <span class="jc_input_error"><bean:write name="errorText"/></span>
	                  </html:messages>
	                </logic:messagesPresent>
	              </td>
	            </tr>
	          </table>
	        </div>
	        <br>
	        <div class="jc_detail_panel_header">
	          <table width="100%" border="0" cellspacing="0" cellpadding="0">
	            <tr>
	              <td><span class="jc_input_label">Bing translation</span></td>
	            </tr>
	          </table>
	        </div>
	        <div class="jc_detail_panel">
	          <table width="100%" border="0" cellspacing="0" cellpadding="5">
	            <tr>
	              <td><span class="jc_input_label">Client id</span></td>
	            </tr>
	            <tr>
	              <td>
	                <html:text property="bingClientId" size="40" styleClass="jc_input_control"/>
	                <logic:messagesPresent property="bingClientId" message="true"> 
	                  <br>
	                  <html:messages property="bingClientId" id="errorText" message="true"> 
	                  <span class="jc_input_error"><bean:write name="errorText"/></span>
	                  </html:messages>
	                </logic:messagesPresent>
	              </td>
	            </tr>
	            <tr>
	              <td><span class="jc_input_label">Client secert</span></td>
	            </tr>
	            <tr>
	              <td>
	                <html:password property="bingClientSecert" size="40" styleClass="jc_input_control"/>
	                <logic:messagesPresent property="bingClientSecert" message="true"> 
	                  <br>
	                  <html:messages property="bingClientSecert" id="errorText" message="true"> 
	                  <span class="jc_input_error"><bean:write name="errorText"/></span>
	                  </html:messages>
	                </logic:messagesPresent>
	              </td>
	            </tr>
	          </table>
	        </div>
          </td>
          <td valign="top">
		    <table width="100%" border="0" cellspacing="0" cellpadding="5">
              <tr>
                <td class="jc_input_label">Site id</td>
              </tr>
              <tr>
                <td class="jc_input_control">
            	  <layout:text layout="false" name="siteMaintForm" property="siteId" size="20" maxlength="20" mode="E,I,I" styleClass="jc_input_object"/>
                  <logic:messagesPresent property="siteId" message="true"> 
                    <br>
                    <html:messages property="siteId" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                  </logic:messagesPresent>
                </td>
              </tr>
              <tr>
                <td class="jc_input_label">Description</td>
              </tr>
			  <tr>
				<td class="jc_input_control">
				  <html:text property="siteDesc" size="40" styleClass="tableContent"/> 
                  <logic:messagesPresent property="siteDesc" message="true"> 
                    <br>
                    <html:messages property="siteDesc" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                  </logic:messagesPresent>
				</td>
			  </tr>
              <tr>
                <td class="jc_input_label">Active</td>
              </tr>
              <tr>
                <td class="jc_input_control"><html:checkbox property="active" value="Y"/></td>
              </tr>
              <tr>
                <td class="jc_input_label">System Record</td>
              </tr>
              <tr>
                <td class="jc_input_control">
                  ${siteMaintForm.systemRecord}
                  <html:hidden property="systemRecord"/> 
                </td>
              </tr>
		    </table>
		  </td>
        </tr>
      </table>
    </td>
  </tr>
</table></html:form>
</div>
<%@ include file="/admin/include/panel.jsp" %>
<%@ include file="/admin/include/confirm.jsp" %>
