<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="customAttributeMaintForm" scope="request" value="${customAttributeMaintForm}"/>
<script language="JavaScript">
var mode = "${customAttributeMaintForm.mode}";
var siteProfileClassDefault = ${customAttributeMaintForm.siteProfileClassDefault};
var siteCurrencyClassDefault = ${customAttributeMaintForm.siteCurrencyClassDefault};
var translationEnable = ${customAttributeMaintForm.translationEnable};
var customAttribId = '${customAttributeMaintForm.customAttribId}';
var siteProfileClassId = "${customAttributeMaintForm.siteProfileClassId}";

function submitForm(type) {
	var inputBoxes = new Array();
    document.forms[0].process.value = type;
    if (!siteProfileClassDefault) {
        document.customAttributeMaintForm.customAttribDescLang.value = document.customAttributeMaintForm.customAttribDescLang_tmp.value;
        var e = document.getElementById("customAttributeOptionsContainer");
        
		jc_traverse_element(e, inputBoxes, 'input', 'customAttributeOption.*customAttribValueLang$');
		for (var i = 0; i < inputBoxes.length; i++) {
			var masterName = inputBoxes[i].name;
			var masterObject = document.customAttributeMaintForm.elements[masterName];
			var sourceName = masterName + '_tmp';
			var sourceObject = document.customAttributeMaintForm.elements[sourceName];
			masterObject.value = sourceObject.value;
		}
    }
    if (!siteCurrencyClassDefault) {
        var e = document.getElementById("customAttributeOptionsContainer");
        
		jc_traverse_element(e, inputBoxes, 'input', 'customAttributeOption.*customAttribValueCurr$');
		for (var i = 0; i < inputBoxes.length; i++) {
			var masterName = inputBoxes[i].name;
			var masterObject = document.customAttributeMaintForm.elements[masterName];
			var sourceName = masterName + '_tmp';
			var sourceObject = document.customAttributeMaintForm.elements[sourceName];
			masterObject.value = sourceObject.value;
		}
    }
    document.forms[0].submit();
    return false;
}
function submitBackForm(type) {
    document.forms[0].action = "/${adminBean.contextPath}/admin/customAttribute/customAttributeListing.do";
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}

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

<script language="JavaScript">

function addCustomAttributeOptionRow(index, customAttribOptionId, customAttribValue, customAttribSkuCode) {
	var table = document.getElementById('optionTable');
	var row = table.insertRow(-1);
	var cell = null;

	var prefix = 'customAttributeOption[' + index + '].';
	
	cell = row.insertCell(-1);
	cell.setAttribute('width', '20%');
	
	var input = document.createElement('input');
	input.setAttribute('type', 'hidden');
	input.setAttribute('name', prefix + 'customAttribOptionId');
	input.setAttribute('value', customAttribOptionId);
	cell.appendChild(input);

	input = document.createElement('input');
	input.setAttribute('type', 'checkbox');
	input.setAttribute('name', 'remove');
	input.setAttribute('value', customAttribOptionId);
	input.className = 'jc_input_control';
	cell.appendChild(input);
	
	cell = row.insertCell(-1);
	cell.setAttribute('width', '40%');
	
	input = document.createElement('input');
	input.setAttribute('type', 'text');
	input.setAttribute('name', prefix +  'customAttribValue');
	input.setAttribute('maxlength', '40');
	input.setAttribute('size', '20');
	input.setAttribute('value', customAttribValue);
	input.className = 'jc_input_control';
	cell.appendChild(input);
	
	cell = row.insertCell(-1);
	cell.setAttribute('width', '40%');
	
	input = document.createElement('input');
	input.setAttribute('type', 'text');
	input.setAttribute('name', prefix + 'customAttribSkuCode');
	input.setAttribute('maxlength', '3');
	input.setAttribute('size', '3');
	input.setAttribute('value', customAttribSkuCode);
	input.className = 'jc_input_control';
	cell.appendChild(input);
}

function addCustomAttributeOption() {
	var table = document.getElementById('optionTable');
	var index = table.rows.length - 2;
	addCustomAttributeOptionRow(index, '', '', '');
}

var showOptions_callback =
{
 	success: function(o) {
 	    if (!isJsonResponseValid(o.responseText)) {
 			jc_panel_show_error("Unexcepted Error: Unable to show attribute options");
 			return false;
 	    }
 	    var jsonObject = getJsonObject(o.responseText);
 		var table = document.getElementById('optionTable');
 		while (table.rows.length > 2) {
 	 		table.deleteRow(2);
 		}

 		for (var i = 0; i < jsonObject.customAttributeOptions.length; i++) {
 	 		var customAttributeOption = jsonObject.customAttributeOptions[i];
 	 		addCustomAttributeOptionRow(i, 
 	 	 	 							customAttributeOption.customAttribOptionId,
 	 	 	 							customAttributeOption.customAttribValue,
 	 	 	 							customAttributeOption.customAttribSkuCode);
 		}	 		
   },
   failure: function(o) {
       jc_panel_show_error("Unexcepted Error: Unable to show attribute options");
   }
};

var removeOptions_callback =
{
 	success: function(o) {
 	    if (!isJsonResponseValid(o.responseText)) {
 			jc_panel_show_error("Unexcepted Error: Unable to remove attribute options");
 			return false;
 	    }
 	    if (!isJsonResponseSuccess(o.responseText)) {
 	 	    var jsonObject = getJsonObject(o.responseText);
 	 	    if (jsonObject.reason == 'inuse') {
 	 	 	    var container = document.getElementById('customAttributeOptionMsgContainer');
 	 	 	    container.innerHTML = "Option already in use and cannot be removed.";
 	 	    }
 	 	    return false;
 	    }
 	    var container = document.getElementById('customAttributeOptionMsgContainer');
	 	container.innerHTML = "";
 		var url = "/${adminBean.contextPath}/admin/customAttribute/customAttributeMaint.do";
 		var postData = "process=getCustomAttributeOptions&customAttribId=" + customAttribId;
 		var request = YAHOO.util.Connect.asyncRequest('POST', url, showOptions_callback, postData);
   },
   failure: function(o) {
       jc_panel_show_error("Unexcepted Error: Unable to remove attribute options");
   }
};

function removeCustomAttributeOptions() {
	var removes = jc_getElementsByName('remove');
	var url = "/${adminBean.contextPath}/admin/customAttribute/customAttributeMaint.do";
	var postData = "process=removeCustomAttributeOption&customAttribId=" + customAttribId;
	for (var i = 0; i < removes.length; i++) {
		if (removes[i].checked) {
			var customAttribOptionId = YAHOO.util.Dom.getFirstChild(removes[i].parentNode).value;
			if (customAttribOptionId != '') {
				postData += '&customAttribOptionIds=' + customAttribOptionId;
			}
		}
	}
	var request = YAHOO.util.Connect.asyncRequest('POST', url, removeOptions_callback, postData);
}

YAHOO.util.Event.onContentReady('butCustomAttributeOptionsContainer', function() {
	var buttonMenu = [
			{ text: "Add custom attribute option", value: 'Add custom attribute option', onclick: { fn: addCustomAttributeOption } }, 
			{ text: "Remove custom attribute options", value: 'Remove custom attribute options', onclick: { fn: removeCustomAttributeOptions } }
	];
	var butContainer = document.getElementById('butCustomAttributeOptionsContainer');
	var menu = new YAHOO.widget.Button({ 
                          type: "menu", 
                          disabled: true,
                          label: "Options", 
                          name: "menu",
                          menu: buttonMenu, 
                          container: butContainer });
    if (mode != 'C' && siteProfileClassDefault && siteCurrencyClassDefault) {
        if ('${customAttributeMaintForm.customAttribTypeCode}' != '3' &&
        	'${customAttributeMaintForm.customAttribTypeCode}' != '5') {
    	  menu.set('disabled', false);
        }
    }
} );

function overrideAttributeLanguage(container, control)  {
	var e = document.getElementById("customAttributeOptionsContainer");
	var inputBoxes = new Array();
	if (!control.checked) {
	  jc_panel_confirm('Confirm to use default language?', function(confirm) {
	    if (confirm) {
			jc_traverse_element(e, inputBoxes, 'input', 'customAttributeOption.*customAttribValue$');
			for (var i = 0; i < inputBoxes.length; i++) {
				var masterName = inputBoxes[i].name;
				var sourceName = masterName + 'Lang_tmp';
				var sourceObject = document.customAttributeMaintForm.elements[sourceName];
				var masterObject = document.customAttributeMaintForm.elements[masterName];
				sourceObject.value = masterObject.value;
				sourceObject.disabled = true;
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
		jc_traverse_element(e, inputBoxes, 'input', 'customAttributeOption.*customAttribValueLang_tmp');
		for (var i = 0; i < inputBoxes.length; i++) {
			inputBoxes[i].disabled = false;
		}
	}
}

function overrideAttributeCurrency(container, control)  {
	var e = document.getElementById("customAttributeOptionsContainer");
	var inputBoxes = new Array();
	if (!control.checked) {
	  jc_panel_confirm('Confirm to use default currency?', function(confirm) {
	    if (confirm) {
			jc_traverse_element(e, inputBoxes, 'input', 'customAttributeOption.*customAttribValue$');
			for (var i = 0; i < inputBoxes.length; i++) {
				var masterName = inputBoxes[i].name;
				var sourceName = masterName + 'Curr_tmp';
				var sourceObject = document.customAttributeMaintForm.elements[sourceName];
				var masterObject = document.customAttributeMaintForm.elements[masterName];
				sourceObject.value = masterObject.value;
				sourceObject.disabled = true;
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
		jc_traverse_element(e, inputBoxes, 'input', 'customAttributeOption.*customAttribValueCurr_tmp');
		for (var i = 0; i < inputBoxes.length; i++) {
			inputBoxes[i].disabled = false;
		}
	}
}
</script>
<html:form method="post" action="/admin/customAttribute/customAttributeMaint"> 
<html:hidden property="mode"/> 
<html:hidden property="process" value=""/> 
<html:hidden property="customAttribId"/> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Item catalog - <a href="/<c:out value='${adminBean.contextPath}'/>/admin/customAttribute/customAttributeListing.do?process=back">Custom Attribute 
      Listing</a> - Custom Attribute Maintenance</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr>
	<td width="400" valign="top">
		<div class="jc_detail_panel_header">
		  <table width="400" border="0" cellspacing="0" cellpadding="0">
		    <tr>
		      <td><span class="jc_input_label">Custom Attributes</span></td>
		      <td>
		        <div align="right" id="butCustomAttributeOptionsContainer">
		        </div>
		      </td>
		    </tr>
		  </table>
		</div>
		<div class="jc_detail_panel">
		  <div id="customAttributeOptionsContainer">
	        <table id="optionTable" width="400" border="0" cellspacing="0" cellpadding="3">
	          <c:if test="${customAttributeMaintForm.customAttribDataTypeCode == '1'}">
		          <c:if test="${!customAttributeMaintForm.siteProfileClassDefault}">
		          <tr>
		            <td colspan="2" class="jc_input_label">
		              Attributes - Override language
		              <html:checkbox property="customAttribOptionLangFlag" onclick="return overrideAttributeLanguage('tabCustomAttributes', this)"/>
		            </td>
		          </tr>
		          </c:if>
	          </c:if>
	          <c:if test="${customAttributeMaintForm.customAttribDataTypeCode == '4'}">
		          <c:if test="${!customAttributeMaintForm.siteCurrencyClassDefault}">
		          <tr>
		            <td colspan="2" class="jc_input_label">
		              Attributes - Override currency
		              <html:checkbox property="customAttribOptionCurrFlag" onclick="return overrideAttributeCurrency('tabCustomAttributes', this)"/>
		            </td>
		          </tr>
		          </c:if>
	          </c:if>
	          <tr>
	            <td colspan="3"><div id="customAttributeOptionMsgContainer" class="jc_input_error"></div></td>
	          </tr>
	          <tr> 
	            <td width="20%" class="jc_input_label">
	              Remove
	            </td>
	            <td width="40%" class="jc_input_label">
	              Option value
	            </td>
	            <td width="40%" class="jc_input_label">
	              SKU makeup code
	            </td>
	          </tr>
	          <c:forEach var="customAttributeOption" items="${customAttributeMaintForm.customAttribOptions}">
	          <tr>
	            <td width="20%">
	              <html:hidden indexed="true" name="customAttributeOption" property="customAttribOptionId"/>
	              <lang:checkbox name="customAttributeOption" value="" property="remove" styleClass="jc_input_control"/>
	            </td>
	            <td width="40%">
	              <c:choose>
	                <c:when test="${customAttributeMaintForm.customAttribDataTypeCode == '4'}">
	                  <lang:text indexed="true" name="customAttributeOption" property="customAttribValue" currency="true" maxlength="40" size="20" styleClass="jc_input_control"/>
	                </c:when>
	                <c:otherwise>
	                  <lang:text indexed="true" name="customAttributeOption" property="customAttribValue" language="true" maxlength="40" size="20" styleClass="jc_input_control"/>
	                </c:otherwise>
	              </c:choose>
	              <c:if test="${customAttributeOption.customAttribValueError != ''}">
		              <span class="jc_input_error">
						<br>${customAttributeOption.customAttribValueError}
			          </span> 
		          </c:if>
	            </td>
	            <td width="40%">
	              <lang:text indexed="true" name="customAttributeOption" property="customAttribSkuCode" maxlength="3" size="3" styleClass="jc_input_control"/>
	              <c:if test="${customAttributeOption.customAttribSkuCodeError != ''}">
		              <span class="jc_input_error">
						<br>${customAttributeOption.customAttribSkuCodeError}
			          </span> 
		          </c:if>
	            </td>
	          </tr>
	          </c:forEach>
	        </table>
		  </div>
		  <br>
		  <br>
		  <br>
		</div>
    </td>
    <td width="100%" valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
        <tr>
          <td>
            <layout:mode value="edit">
            Profile
            <html:select property="siteProfileClassId" onchange="javascript:submitForm('language')"> 
              <html:optionsCollection property="siteProfileClassBeans" value="value" label="label"/> 
            </html:select>
            <button type="button" id="butTranslate" name="butTranslate" value="Translate">Translate</button>
            <c:if test="${customAttributeMaintForm.customAttribDataTypeCode == '4'}">
            Currency
            <html:select property="siteCurrencyClassId" onchange="javascript:submitForm('language')"> 
              <html:optionsCollection property="siteCurrencyClassBeans" value="value" label="label"/> 
            </html:select>
            </c:if>
            </layout:mode>
          </td>
          <td align="right"> 
            <table width="0%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><html:submit property="submitButton" value="Save" styleClass="jc_submit_button" onclick="return submitForm('save')"/>&nbsp;</td>
                <layout:mode value="edit">
                <c:if test="${customAttributeMaintForm.systemRecord != 'Y'}">
                <td><html:submit property="submitButton" value="Remove" styleClass="jc_submit_button" onclick="return submitForm('remove')"/>&nbsp;</td>
                </c:if>
                </layout:mode> 
                <td><html:submit property="submitButton" value="Cancel" styleClass="jc_submit_button" onclick="return submitBackForm('back')"/>&nbsp;</td>
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
      <table width="100%" border="0" cellspacing="0" cellpadding="5px" class="borderTable">
        <tr> 
          <td class="jc_input_label">
            Custom Attribute Name
          </td>
        </tr>
        <tr>
          <td class="jc_input_control">
		  <lang:text property="customAttribName" size="40" styleClass="tableContent"/>
		  <span class="jc_input_error">
          <logic:messagesPresent property="customAttribName" message="true"> 
          <html:messages property="customAttribName" id="errorText" message="true"> 
          <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
          </span> 
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">
            Custom Attribute Description
            <lang:checkboxSwitch name="customAttributeMaintForm" property="customAttribDescLangFlag"> - Override language</lang:checkboxSwitch>
          </td>
        </tr>
        <tr>
          <td class="jc_input_control">
		  <lang:text property="customAttribDesc" size="40" styleClass="tableContent"/>
		  <span class="jc_input_error">
          <logic:messagesPresent property="customAttribDesc" message="true"> 
          <html:messages property="customAttribDesc" id="errorText" message="true"> 
          <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
          </span> 
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">
            Data type
          </td>
        </tr>
        <tr>
          <td class="jc_input_control">
            <c:choose>
              <c:when test="${customAttributeMaintForm.mode == 'C'}">
				<lang:select property="customAttribDataTypeCode" styleClass="tableContent"> 
	              <lang:option value="1">String</lang:option> 
	              <lang:option value="2">Integer</lang:option> 
	              <lang:option value="3">Decimal</lang:option> 
	              <lang:option value="4">Currency</lang:option> 
			    </lang:select>
			  </c:when>
			  <c:otherwise>
			    <html:hidden property="customAttribDataTypeCode"/> 
			    <html:hidden property="customAttribDataTypeCodeDesc"/> 
			    ${customAttributeMaintForm.customAttribDataTypeCodeDesc}
			  </c:otherwise>
			</c:choose>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">
            Attribute type
          </td>
        </tr>
        <tr>
          <td class="jc_input_control">
            <c:choose>
              <c:when test="${customAttributeMaintForm.mode == 'C'}">
				<lang:select property="customAttribTypeCode" styleClass="tableContent"> 
	              <lang:option value="1">Customer selects from dropdown</lang:option> 
	              <lang:option value="2">User select from dropdown</lang:option> 
	              <lang:option value="5">Customer input</lang:option> 
	              <lang:option value="3">User input</lang:option> 
	              <lang:option value="4">SKU makeup</lang:option> 
			    </lang:select>
			  </c:when>
			  <c:otherwise>
			    <html:hidden property="customAttribTypeCode"/>
			    <html:hidden property="customAttribTypeCodeDesc"/> 
			    ${customAttributeMaintForm.customAttribTypeCodeDesc}
			  </c:otherwise>
			</c:choose>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">
            Item compare
          </td>
        </tr>
        <tr>
          <td class="jc_input_control">
		  <lang:checkbox property="itemCompare" styleClass="tableContent"/>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table></html:form>
<%@ include file="/admin/include/confirm.jsp" %>