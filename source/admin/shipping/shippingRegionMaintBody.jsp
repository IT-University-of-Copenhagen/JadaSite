<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="shippingRegionMaintForm" scope="request" value="${shippingRegionMaintForm}"/>
<script language="JavaScript">
var shippingRegionId = '${shippingRegionMaintForm.shippingRegionId}';
var mode = '${shippingRegionMaintForm.mode}';

function submitForm(type) {
    document.shippingRegionMaintForm.process.value = type;
    document.shippingRegionMaintForm.submit();
    return false;
}
function submitBackForm(type) {
    document.shippingRegionMaintForm.action = "/${adminBean.contextPath}/admin/shipping/shippingRegionListing.do";
    document.shippingRegionMaintForm.process.value = "back";
    document.shippingRegionMaintForm.submit();
    return false;
}

/*
 * Add and remove regions
 */
function addRegion() {
	jc_region_search_show();
}

var jc_region_add_callback = {
	success: function(o) {
		if (!isJsonResponseValid(o.responseText)) {
		    jc_panel_show_error("Unexcepted Error: Unable to add region");
			jc_region_search_finish();
		    return false;
		}
		var jsonObject = getJsonObject(o.responseText);
		if (!isJsonResponseSuccess(o.responseText)) {
			jc_panel_show_error("Unexcepted Error: " + jsonObject.message);
			jc_region_search_finish();
			return false;
		}
		jc_region_search_finish();
		showRegions();
	},
	failure: function(o) {
		jc_panel_show_error("Unexcepted Error: Unable to add region");
	}	
};

function jc_region_search_client_callback(result) {
	var response = eval('(' + result + ')');
	var url = "/${adminBean.contextPath}/admin/shipping/shippingRegionMaint.do";
	var postData = "process=addRegion&shippingRegionId=" + shippingRegionId;
	var countries = response.countries;
	for (var i = 0; i < countries.length; i++) {
		postData += "&countryIds=" + countries[i].countryId;
	}
	var states = response.states;
	for (var i = 0; i < states.length; i++) {
		postData += "&stateIds=" + states[i].stateId;
	}
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_region_add_callback, postData);
}

var jc_regions_remove_callback = {
	success: function(o) {
		if (!isJsonResponseValid(o.responseText)) {
		    jc_panel_show_error("Unexcepted Error: Unable to remove regions");
		    return false;
		}
		var jsonObject = getJsonObject(o.responseText);
		if (!isJsonResponseSuccess(o.responseText)) {
			jc_panel_show_error("Unexcepted Error: " + jsonObject.message);
			return false;
		}
		showRegions();
	},
	failure: function(o) {
		jc_panel_show_error("Unexcepted Error: Unable to remove regions");
	}	
};

function removeRegions() {
	var url = "/${adminBean.contextPath}/admin/shipping/shippingRegionMaint.do";
	var postData = "process=removeRegion&shippingRegionId=" + shippingRegionId;
	var elements = jc_getElementsByName('countryId');
	for (var i = 0; i < elements.length; i++) {
		var checkbox = elements[i];
		if (checkbox.checked) {
			postData += "&countryIds=" + checkbox.value;
		}
	}
	elements = jc_getElementsByName('stateId');
	for (var i = 0; i < elements.length; i++) {
		var checkbox = elements[i];
		if (checkbox.checked) {
			postData += "&stateIds=" + checkbox.value;
		}
	}
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_regions_remove_callback, postData);
}

YAHOO.util.Event.onContentReady('regionButtonOptionContainer', function() {
	var buttonMenu = [
			{ text: "Add region", value: 'Add region', onclick: { fn: addRegion } }, 
			{ text: "Remove regions", value: 'Remove regions', onclick: { fn: removeRegions } }
	];
	var butContainer = document.getElementById('regionButtonOptionContainer');
	var menu = new YAHOO.widget.Button({ 
                          type: "menu", 
                          disabled: true,
                          label: "Options", 
                          name: "menu",
                          menu: buttonMenu, 
                          container: butContainer });
	if (mode != 'C') {
		menu.set("disabled", false);
	}
} );

var showRegions_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: Unable to extract region list");
      return false;
    }
    var node = document.getElementById("regionContainer");
    node.innerHTML = '';
    var table = document.createElement('table');
    node.appendChild(table);
    var body = document.createElement('tbody');
    table.appendChild(body);
    
    var jsonObject = getJsonObject(o.responseText);
    for (var i = 0; i < jsonObject.countries.length; i++) {
        var country = jsonObject.countries[i];
        var row = document.createElement('tr');
        body.appendChild(row);
        var col;
        col = document.createElement('td');
        row.appendChild(col);
        var checkbox = document.createElement('input');
    	checkbox.setAttribute('type', 'checkbox');
        checkbox.setAttribute('name', 'countryId');
        checkbox.setAttribute('value', country.countryId);
        col.appendChild(checkbox);
        col = document.createElement('td');
        row.appendChild(col);
        col.appendChild(document.createTextNode(country.countryCode));
        col = document.createElement('td');
        row.appendChild(col);
        col.appendChild(document.createTextNode(country.countryName));
        col = document.createElement('td');
        row.appendChild(col);
    }

    var jsonObject = getJsonObject(o.responseText);
    for (var i = 0; i < jsonObject.states.length; i++) {
        var state = jsonObject.states[i];
        var row = document.createElement('tr');
        body.appendChild(row);
        var col;
        col = document.createElement('td');
        row.appendChild(col);
        var checkbox = document.createElement('input');
    	checkbox.setAttribute('type', 'checkbox');
        checkbox.setAttribute('name', 'stateId');
        checkbox.setAttribute('value', state.stateId);
        col.appendChild(checkbox);
        col = document.createElement('td');
        row.appendChild(col);
        col.appendChild(document.createTextNode(state.stateCode));
        col = document.createElement('td');
        row.appendChild(col);
        col.appendChild(document.createTextNode(state.stateName));
        col = document.createElement('td');
        row.appendChild(col);
    }
  },
  failure: function(o) {
      jc_panel_show_error("Unexcepted Error: Unable to extract countries and states list");
  }
};

function showRegions() {
	var url = "/${adminBean.contextPath}/admin/shipping/shippingRegionMaint.do?process=getRegionList&shippingRegionId=" + shippingRegionId;
	var request = YAHOO.util.Connect.asyncRequest('GET', url, showRegions_callback);
}

/***********************************************************/

var jc_zipcode_panel = null;

YAHOO.util.Event.onDOMReady(function() {
    jc_zipcode_panel = new YAHOO.widget.Panel("jc_zipcode_panel",
	    {
			width: "300px",
			visible: false,
			constraintoviewport: true ,
			fixedcenter : true,
			modal: true
		}
	);
	jc_zipcode_panel.render();
} );

YAHOO.util.Event.onDOMReady(function() {
    var button = new YAHOO.widget.Button({label: 'Update',
                                          id: 'zipCodeButton',
                                          disabled: true,
                                          container: 'zipCodeButtonContainer' });
    button.on('click', function() {
		jc_zipcode_client_callback();
        jc_zipcode_panel.hide();
    });
	if (mode != 'C') {
		button.set("disabled", false);
	}
} );

function addZipCode() {
	var zipCodeExpression = document.getElementById("zipCodeStart");
	zipCodeExpression.checked = false;
	var zipCodeStart = document.getElementById("zipCodeStart");
	zipCodeStart.value = '';
	var zipCodeEnd = document.getElementById("zipCodeEnd");
	zipCodeEnd.value = '';
	jc_zipcode_panel.show();
}

var jc_zipcode_add_callback = {
	success: function(o) {
		if (!isJsonResponseValid(o.responseText)) {
		    jc_panel_show_error("Unexcepted Error: Unable to add zip code");
			jc_region_search_finish();
		    return false;
		}
		var jsonObject = getJsonObject(o.responseText);
		if (!isJsonResponseSuccess(o.responseText)) {
			jc_panel_show_error("Unexcepted Error: " + jsonObject.message);
			jc_zipcode_panel.hide();
			return false;
		}
		jc_zipcode_panel.hide();
		showZipCodes();
	},
	failure: function(o) {
		jc_panel_show_error("Unexcepted Error: Unable to add zip code");
	}	
};

function jc_zipcode_client_callback(result) {
	var response = eval('(' + result + ')');
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/shipping/shippingRegionMaint.do";
	var postData = "process=addZipCode&shippingRegionId=" + shippingRegionId;
	var element = document.getElementById("zipCodeExpression");
	var zipCodeExpression = 'N';
	if (element.checked) {
		zipCodeExpression = 'Y';
	}
	var zipCodeStart = document.getElementById("zipCodeStart");
	var zipCodeEnd = document.getElementById("zipCodeEnd");
	postData += "&zipCodeStart=" + zipCodeStart.value + "&zipCodeEnd=" + zipCodeEnd.value + "&zipCodeExpression=" + zipCodeExpression;
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_zipcode_add_callback, postData);
}

var jc_zipcodes_remove_callback = {
	success: function(o) {
		if (!isJsonResponseValid(o.responseText)) {
		    jc_panel_show_error("Unexcepted Error: Unable to remove zip codes");
		    return false;
		}
		var jsonObject = getJsonObject(o.responseText);
		if (!isJsonResponseSuccess(o.responseText)) {
			jc_panel_show_error("Unexcepted Error: " + jsonObject.message);
			return false;
		}
		showZipCodes();
	},
	failure: function(o) {
		jc_panel_show_error("Unexcepted Error: Unable to remove regions");
	}	
};

function removeZipCodes() {
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/shipping/shippingRegionMaint.do";
	var postData = "process=removeZipCode&shippingRegionId=" + shippingRegionId;
	var elements = document.getElementsByName('shippingRegionZipId');
	for (var i = 0; i < elements.length; i++) {
		var checkbox = elements[i];
		if (checkbox.checked) {
			postData += "&shippingRegionZipIds=" + checkbox.value;
		}
	}
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_zipcodes_remove_callback, postData);
}

YAHOO.util.Event.onDOMReady(function() {
	var zipCodeButtonMenu = [
			{ text: "Add zip code", value: 'Add zip code', onclick: { fn: addZipCode } }, 
			{ text: "Remove zip codes", value: 'Remove zip codes', onclick: { fn: removeZipCodes } }
	];
	var butContainer = document.getElementById('zipCodeButtonOptionContainer');
	var menu = new YAHOO.widget.Button({ 
                          type: "menu", 
                          label: "Options", 
                          name: "menu",
                          menu: zipCodeButtonMenu, 
                          container: butContainer });
} );

var showZipCodes_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: Unable to extract zip code list");
      return false;
    }
    var node = document.getElementById("zipCodeContainer");
    node.innerHTML = '';
    var table = document.createElement('table');
    node.appendChild(table);
    var body = document.createElement('tbody');
    table.appendChild(body);
    
    var jsonObject = getJsonObject(o.responseText);
    for (var i = 0; i < jsonObject.zipCodes.length; i++) {
        var zipCode = jsonObject.zipCodes[i];

        var isExpression = false;
        if (zipCode.zipCodeExpression == "Y") {
            isExpression = true;
        }
        
        var row = document.createElement('tr');
        body.appendChild(row);
        var col;
        col = document.createElement('td');
        row.appendChild(col);
        var checkbox = document.createElement('input');
    	checkbox.setAttribute('type', 'checkbox');
        checkbox.setAttribute('name', 'shippingRegionZipId');
        checkbox.setAttribute('id', 'shippingRegionZipId');
        checkbox.setAttribute('value', zipCode.shippingRegionZipId);
        col.appendChild(checkbox);
        col = document.createElement('td');
        row.appendChild(col);
        col.appendChild(document.createTextNode(zipCode.zipCodeStart));
        col = document.createElement('td');
        row.appendChild(col);
        if (isExpression) {
        	col.appendChild(document.createTextNode(''));
        }
        else {
       		col.appendChild(document.createTextNode('- ' + zipCode.zipCodeEnd));
        }
        col = document.createElement('td');
        row.appendChild(col);

        if (isExpression) {
        	col.appendChild(document.createTextNode(' Expression'));
        }
        else {
        	col.appendChild(document.createTextNode(''));
        }
        col = document.createElement('td');
        row.appendChild(col);
    }
  },
  failure: function(o) {
      jc_panel_show_error("Unexcepted Error: Unable to extract zip codes list");
  }
};

function showZipCodes() {
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/shipping/shippingRegionMaint.do?process=getZipCodeList&shippingRegionId=" + shippingRegionId;
	var request = YAHOO.util.Connect.asyncRequest('GET', url, showZipCodes_callback);
}


/***********************************************************/

if (mode != 'C') {
	YAHOO.util.Event.onDOMReady(function() {
		showRegions();
		showZipCodes();
	} );
}

</script>
<html:form method="post" action="/admin/shipping/shippingRegionMaint"> <html:hidden property="mode"/> 
<html:hidden property="process" value=""/> <html:hidden property="shippingRegionId"/> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - <a href="/${adminBean.contextPath}/admin/shipping/shippingRegionListing.do?process=back">Shipping 
      Region Listing</a> - Shipping Region Maintenance</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr> 
    <td valign="top"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td align="right" class="jc_panel_table_header"> 
            <table width="0%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><html:submit property="submitButton" value="Save" styleClass="jc_submit_button" onclick="return submitForm('save');"/>&nbsp;</td>
                <layout:mode value="edit"> 
                <td><html:submit property="submitButton" value="Remove" styleClass="jc_submit_button" onclick="return submitForm('remove');"/>&nbsp;</td>
                </layout:mode> 
                <td><html:submit property="submitButton" value="Cancel" styleClass="jc_submit_button" onclick="return submitBackForm('back');"/>&nbsp;</td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <table width="100%" border="0" cellspacing="0" cellpadding="5px" class="borderTable">
        <tr> 
          <td class="jc_input_label">Shipping Region</td>
          <td class="jc_input_control"> <layout:text layout="false" property="shippingRegionName" size="40" mode="E,E,E" styleClass="tableContent"/> 
            <span class="jc_input_error"> <logic:messagesPresent property="shippingRegionName" message="true"> 
            <html:messages property="shippingRegionName" id="errorText" message="true"> 
            <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
            </span> </td>
        </tr>
        <tr> 
          <td class="jc_input_label">Published</td>
          <td class="jc_input_control"> <layout:checkbox layout="false" property="published" mode="E,E,E" value="Y"/> 
          </td>
        </tr>
        <tr> 
          <td> </td>
          <td> </td>
        </tr>
      </table>
    </td>
    <td width="400" valign="top">
      <div class="jc_detail_panel_header">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td>
              <span class="jc_input_label">Country and States/Province</span>
            </td>
            <td>
              <div align="right" id="regionButtonOptionContainer"></div>
            </td>
          </tr>
        </table>
      </div>
      <div id="regionContainer" class="jc_detail_panel"></div>
	  <div class="jc_detail_panel_header">
	    <table border="0" cellspacing="0" cellpadding="0">
	      <tr>
		    <td><span class="jc_input_label">Regions - Zip codes</span></td>
		    <td width="100%">
		    <div align="right" id="zipCodeButtonOptionContainer"></div>
		    </td>
		  </tr>
	    </table>
	  </div>
	  <div>
	    <div id="zipCodeContainer" class="jc_detail_panel"></div>
	  </div>
    </td>
  </tr>
</table>

</html:form> 

<div class=" yui-skin-sam">
	<div id="jc_zipcode_panel">
		<div class="hd">Zip code</div>
		<div class="bd">
			<div align="right" id="zipCodeButtonContainer"></div>
			<div id="jc_zipcode_message" class="jc_input_error"></div>
			<table border="0" cellspacing="0" cellpadding="5">
				<tr>
					<td>Regular expression</td>
					<td><input type="checkbox" name="zipCodeExpression" id="zipCodeExpression"></td>
				</tr>
				<tr>
					<td>Zip code start</td>
					<td><input type="text" name="zipCodeStart" id="zipCodeStart"></td>
				</tr>
				<tr>
					<td>Zip code end</td>
					<td><input type="text" name="zipCodeEnd" id="zipCodeEnd"></td>
				</tr>
			</table>
		</div>
	</div>
</div>

<%@ include file="/admin/include/panel.jsp"%>
<%@ include file="/admin/include/regionLookup.jsp"%>

