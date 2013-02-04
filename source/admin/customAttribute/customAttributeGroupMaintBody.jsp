<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="customAttributeGroupMaintForm" scope="request" value="${customAttributeGroupMaintForm}"/>
<script language="JavaScript">
var customAttribGroupId = "${customAttributeGroupMaintForm.customAttribGroupId}";
var mode = "${customAttributeGroupMaintForm.mode}";

function submitForm(type) {
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}
function submitBackForm(type) {
    document.forms[0].action = "/<c:out value='${adminBean.contextPath}'/>/admin/customAttribute/customAttributeGroupListing.do";
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}

var jc_customAttributeDetails_remove_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to remove custom attributes");
      return false;
    }
    if (!isJsonResponseSuccess(o.responseText)) {
 	    var jsonObject = getJsonObject(o.responseText);
 	    if (jsonObject.reason == 'inuse') {
 	 	    var container = document.getElementById('customAttrbuteMsgContainer');
 	 	    container.innerHTML = "Attributes already in use and cannot be removed.";
 	    }
 	    return false;
    }
    
    var jsonObject = eval('(' + o.responseText + ')');
    paintAttribute();
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to remove custom attributes");
  }
};

function removeCustomAttributeDetails() {
	var url = "/${adminBean.contextPath}/admin/customAttribute/customAttributeGroupMaint.do";
	var data = "process=removeCustomAttributeDetails&customAttribGroupId=" + customAttribGroupId;
	
	var e = document.getElementById("customAttributeDetailsContainer");
	var checkboxes = new Array();
	jc_traverse_element(e, checkboxes, 'input', 'customAttribDetailIds');

	var count = 0;
	for (var i = 0; i < checkboxes.length; i++) {
	  if (!checkboxes[i].checked) {
	    continue;
	  }
	  data += "&customAttribDetailIds=" + checkboxes[i].value;
	}
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_customAttributeDetails_remove_callback, data);
	return false;
}

var jc_customAttributeDetails_resequence_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to resequence custom attributes");
      return false;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    var validations = jsonObject.validations;
    if (jsonObject.validations) {
        for (i = 0; i < validations.length; i++) {
        	var object = document.getElementById("seqNum_msg_" + validations[i].customAttribDetailId);
        	object.appendChild(document.createTextNode(validations[i].error));
        }
        return;
    }
    paintAttribute();
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to resequence custom attributes");
  }
};

function resequence() {
	var url = "/${adminBean.contextPath}/admin/customAttribute/customAttributeGroupMaint.do";
	var data = "process=resequence&customAttribGroupId=" + customAttribGroupId;
	
	var e = document.getElementById("customAttributeDetailsContainer");
	var checkboxes = new Array();
	jc_traverse_element(e, checkboxes, 'input', 'customAttribDetailIds');
	var seqNums = new Array();
	jc_traverse_element(e, seqNums, 'input', 'seqNums');
	

	var count = 0;
	for (var i = 0; i < checkboxes.length; i++) {
	  data += "&customAttribDetailIds=" + checkboxes[i].value;
	  data += "&seqNums=" + seqNums[i].value;
	}
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_customAttributeDetails_resequence_callback, data);
	return false;
}

var addCustomAttributeDetail_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to add custom attribute");
      return false;
    }
    paintAttribute();
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to add custom attribute");
  }
};
function jc_customAttributeDetails_search_client_callback(object) {
	jc_customAttribute_search_panel.hide();
   
	var url = "/${adminBean.contextPath}/admin/customAttribute/customAttributeGroupMaint.do";
	var data = "process=addCustomAttributeDetail&customAttribGroupId=" + customAttribGroupId + "&customAttribId=" + object.customAttribId;
	var request = YAHOO.util.Connect.asyncRequest('POST', url, addCustomAttributeDetail_callback, data);
	return false;
}

function addCustomAttributeDetail() {
	jc_customAttribute_search_show(jc_customAttributeDetails_search_client_callback, 'customAttributeGroup');
}

var showCustomAttributeDetails_callback =
{
 	success: function(o) {
 	    if (!isJsonResponseValid(o.responseText)) {
 			jc_panel_show_error("Unexcepted Error: Unable to extract custom attributes");
 			return false;
 	    }
 	    var jsonObject = getJsonObject(o.responseText);
 	    var node = document.getElementById("customAttributeDetailsContainer");
 	    node.innerHTML = '';
 	    var table = document.createElement('table');
 	    table.setAttribute('id', 'customAttrbuteDetails');
 	    table.setAttribute('cellpadding', '5');
 	    table.className = 'jc_nobordered_table';
 	    table.setAttribute('width', '400');
 	    var body = document.createElement('tbody');
 	    table.appendChild(body);
 	    if (jsonObject.customAttributeDetails.length > 0) {
 		    var row = document.createElement('tr');
 		    body.appendChild(row);
 		    var col = document.createElement('td');
 		    col.setAttribute('width', '50');
 		    col.appendChild(document.createTextNode(''));
			row.appendChild(col);
 		    col = document.createElement('td');
 		    col.setAttribute('width', '50');
 		    col.className = 'jc_input_label';
 		    col.appendChild(document.createTextNode('Sequence'));
			row.appendChild(col);
 		    col = document.createElement('td');
 		    col.setAttribute('width', '0');
			row.appendChild(col);
 		    col = document.createElement('td');
 		    col.setAttribute('width', '100%');
 		    col.className = 'jc_input_label';
 		    col.appendChild(document.createTextNode('Custom Attribute'));
			row.appendChild(col);
			row.appendChild(col);
			
			row = document.createElement('tr');
 		    body.appendChild(row);
 		    col = document.createElement('td');
 		    col.className = 'jc_input_error';
 		    col.colSpan = '4';
 		    var div = document.createElement('div');
 		    div.setAttribute('id', 'customAttrbuteMsgContainer');
 		    col.appendChild(div);
			row.appendChild(col);
 	    }
 	   	for (var i = 0; i < jsonObject.customAttributeDetails.length; i++) {
 		    var customAttributeDetail = jsonObject.customAttributeDetails[i];
 		    var row = document.createElement('tr');
 		    body.appendChild(row);
 		    col = document.createElement('td');
 		    var input = document.createElement('input');
 		    input.setAttribute('type', 'checkbox');
 		    input.setAttribute('name', 'customAttribDetailIds');
 		    input.setAttribute('value', customAttributeDetail.customAttribDetailId);
 		 	col.appendChild(input);
 		 	row.appendChild(col);
 		 	col = document.createElement('td');
 		    input = document.createElement('input');
 		    input.style.width="50";
 		    input.setAttribute('type', 'text');
 		    input.setAttribute('name', 'seqNums');
 		    input.setAttribute('value', customAttributeDetail.seqNum);
 		    col.appendChild(input);
 		 	row.appendChild(col);

 		 	col = document.createElement('td');
 		 	col.className = 'jc_input_error';
 		 	col.setAttribute('id', 'seqNum_msg_' + customAttributeDetail.customAttribDetailId);
 		 	row.appendChild(col);
 		 	
 		    col = document.createElement('td');
 		    col.appendChild(document.createTextNode(customAttributeDetail.customAttribName));
			row.appendChild(col);
 		}
 		node.appendChild(table);
   },
   failure: function(o) {
       jc_panel_show_error("Unexcepted Error: Unable to extract up-sell items");
   }
};
 
function paintAttribute() {
	var url = "/${adminBean.contextPath}/admin/customAttribute/customAttributeGroupMaint.do";
	var data = "process=getCustomAttributeDetails&customAttribGroupId=" + customAttribGroupId;
	var request = YAHOO.util.Connect.asyncRequest('POST', url, showCustomAttributeDetails_callback, data);
}
	
YAHOO.util.Event.onContentReady('customAttributeDetailsContainer', function() {
	if (mode != 'C') {
		paintAttribute();
	}
} );

YAHOO.util.Event.onContentReady('butCustomAttributeDetailsContainer', function() {
	var buttonMenu = [
			{ text: "Add Custom Attribute", value: 'Add Custom Attribute', onclick: { fn: addCustomAttributeDetail } }, 
			{ text: "Remove Custom Attributes", value: 'Remove Custom Attributes', onclick: { fn: removeCustomAttributeDetails } },
			{ text: "Resequence", value: 'Resequence', onclick: { fn: resequence } }
	];
	var butContainer = document.getElementById('butCustomAttributeDetailsContainer');
	var menu = new YAHOO.widget.Button({ 
                          type: "menu", 
                          disabled: true,
                          label: "Options", 
                          name: "menu",
                          menu: buttonMenu, 
                          container: butContainer });
    if (mode != 'C') {
    	  menu.set('disabled', false);
    }
} );
</script>
<html:form method="post" action="/admin/customAttribute/customAttributeGroupMaint"> 
<html:hidden property="mode"/> 
<html:hidden property="process" value=""/> 
<html:hidden property="customAttribGroupId"/> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Item catalog - <a href="/<c:out value='${adminBean.contextPath}'/>/admin/customAttribute/customAttributeGroupListing.do?process=back">Custom Attribute Group
      Listing</a> - Custom Attribute Group Maintenance</td>
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
		        <div align="right" id="butCustomAttributeDetailsContainer">
		        </div>
		      </td>
		    </tr>
		  </table>
		</div>
		<div class="jc_detail_panel">
		  <div id="customAttributeDetailsContainer">
		  </div>
		  <br>
		  <br>
		  <br>
		</div>
    </td>
    <td width="100%" valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
        <tr>
          <td align="right"> 
            <table width="0%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><html:submit property="submitButton" value="Save" styleClass="jc_submit_button" onclick="return submitForm('save')"/>&nbsp;</td>
                <layout:mode value="edit"> 
                <td><html:submit property="submitButton" value="Remove" styleClass="jc_submit_button" onclick="return submitForm('remove')"/>&nbsp;</td>
                </layout:mode> 
                <td><html:submit property="submitButton" value="Cancel" styleClass="jc_submit_button" onclick="return submitBackForm('back')"/>&nbsp;</td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <br>
	  <table width="100%" border="0" cellspacing="0" cellpadding="5px" class="borderTable">
		<tr> 
		  <td class="jc_input_label">
		    Custom Attribute Group Name
		  </td>
		</tr>
		<tr>
		  <td class="jc_input_control">
		    <html:text property="customAttribGroupName" size="40" styleClass="tableContent"/>
		   <span class="jc_input_error">
		   <logic:messagesPresent property="customAttribGroupName" message="true"> 
		   <html:messages property="customAttribGroupName" id="errorText" message="true"> 
		   <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
		      </span> 
		    </td>
		  </tr>
	  </table>

    </td>
  </tr>
</table></html:form>
<%@ include file="/admin/include/customAttributeLookup.jsp" %>
<%@ include file="/admin/include/panel.jsp" %>
<%@ include file="/admin/include/confirm.jsp" %>
