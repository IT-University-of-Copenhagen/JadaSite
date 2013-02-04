<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang"%>
<c:set var="adminBean" scope="session" value="${adminBean}" />
<c:set var="taxRegionMaintForm" scope="request" value="${taxRegionMaintForm}" />
<script language="JavaScript">
var taxRegionId = ${taxRegionMaintForm.taxRegionId};
var mode = '${taxRegionMaintForm.mode}';
function submitForm(type) {
    document.taxRegionMaintForm.process.value = type;
    document.taxRegionMaintForm.submit();
    return false;
}
function submitCancelForm() {
    document.taxRegionMaintForm.action = "/<c:out value='${adminBean.contextPath}'/>/admin/tax/taxRegionListing.do";
    document.taxRegionMaintForm.process.value = "back";
    document.taxRegionMaintForm.submit();
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
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/tax/taxRegionMaint.do";
	var postData = "process=addRegion&taxRegionId=" + taxRegionId;
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
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/tax/taxRegionMaint.do";
	var postData = "process=removeRegion&taxRegionId=" + taxRegionId;
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
                          label: "Options", 
                          name: "menu",
                          menu: buttonMenu, 
                          container: butContainer });
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
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/tax/taxRegionMaint.do?process=getRegionList&taxRegionId=" + taxRegionId;
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
                                          container: 'zipCodeButtonContainer' });
    button.on('click', function() {
		jc_zipcode_client_callback();
        jc_zipcode_panel.hide();
    });
} );

function addZipCode() {
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
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/tax/taxRegionMaint.do";
	var postData = "process=addZipCode&taxRegionId=" + taxRegionId;
	var zipCodeStart = document.getElementById("zipCodeStart");
	var zipCodeEnd = document.getElementById("zipCodeEnd");
	postData += "&zipCodeStart=" + zipCodeStart.value + "&zipCodeEnd=" + zipCodeEnd.value;
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
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/tax/taxRegionMaint.do";
	var postData = "process=removeZipCode&taxRegionId=" + taxRegionId;
	var elements = document.getElementsByName('taxRegionZipId');
	for (var i = 0; i < elements.length; i++) {
		var checkbox = elements[i];
		if (checkbox.checked) {
			postData += "&taxRegionZipIds=" + checkbox.value;
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
        var row = document.createElement('tr');
        body.appendChild(row);
        var col;
        col = document.createElement('td');
        row.appendChild(col);
        var checkbox = document.createElement('input');
    	checkbox.setAttribute('type', 'checkbox');
        checkbox.setAttribute('name', 'taxRegionZipId');
        checkbox.setAttribute('id', 'taxRegionZipId');
        checkbox.setAttribute('value', zipCode.taxRegionZipId);
        col.appendChild(checkbox);
        col = document.createElement('td');
        row.appendChild(col);
        col.appendChild(document.createTextNode(zipCode.zipCodeStart));
        col = document.createElement('td');
        row.appendChild(col);
        col.appendChild(document.createTextNode('- ' + zipCode.zipCodeEnd));
        col = document.createElement('td');
        row.appendChild(col);
    }
  },
  failure: function(o) {
      jc_panel_show_error("Unexcepted Error: Unable to extract zip codes list");
  }
};

function showZipCodes() {
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/tax/taxRegionMaint.do?process=getZipCodeList&taxRegionId=" + taxRegionId;
	var request = YAHOO.util.Connect.asyncRequest('GET', url, showZipCodes_callback);
}


/***********************************************************/
var jsonString = '<c:out value="${taxRegionMaintForm.jsonProductClasses}" escapeXml="false"/>';
jsonString = unescape(jsonString);
var trProductList = '';
if (mode != 'C') {
	trProductList = eval('(' + jsonString + ')');
}

if (mode != 'C') {
	YAHOO.util.Event.onContentReady('product_add_button_container', function() {
		var butContainer = document.getElementById('product_add_button_container');
	    var button = new YAHOO.widget.Button({label: '<div class="buttonFixWidth_6">Add product</div>',
										      id: 'productAddId',
										      container: butContainer });
		button.on('click', function() {
			jc_productClass_search_show();
		});
	} );
}

function jc_productClass_search_client_callback(result) {
	var productClassId = result.productClassId;
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/tax/taxRegionMaint.do";
	var postData = "process=addProductClass&taxRegionId=" + taxRegionId + "&productClassId=" + productClassId;
	var callback = {
		success: function(o) {
		    if (o.responseText == undefined) {
		        return;
		    }
	        var jsonObject = eval('(' + o.responseText + ')');
	        if (jsonObject.status == 'failed') {
	        	jc_panel_show_message(jsonObject.message);
	        	return;
	        }
	    	createProductHeader(jsonObject.taxRegionProductId, jsonObject.productClassId, jsonObject.productClassName);
	    	createProductBody(jsonObject.taxRegionProductId, jsonObject.productClassId, jsonObject.productClassName);
	    	paintProductBody(jsonObject.taxRegionProductId);
	    	jc_productClass_search_finish();
		},
		failure: function(o) {
			jc_panel_show_message('Error showing product class search');
		}
	};
	var request = YAHOO.util.Connect.asyncRequest('POST', url, callback, postData);
}

if (mode != 'C') {
	YAHOO.util.Event.onAvailable('products_container', function() {
		for (var i = 0; i < trProductList.taxRegionProductClasses.length; i++) {
			var trProductClass = trProductList.taxRegionProductClasses[i];
			createProductHeader(trProductClass.taxRegionProductId, trProductClass.productClassId, trProductClass.productClassName);
			createProductBody(trProductClass.taxRegionProductId, trProductClass.productClassId, trProductClass.productClassName);
			paintProductBody(trProductClass.taxRegionProductId);
		}
	});
}

function createProductHeader(taxRegionProductId, productClassId, productClassName) {
	var productsContainer = document.getElementById('products_container');
	var headerDiv = document.createElement('div');
	headerDiv.setAttribute('id', 'product_header_' + taxRegionProductId);
    headerDiv.className = 'jc_detail_panel_header';
    var table = document.createElement('table');
    table.setAttribute('width', '100%');
    headerDiv.appendChild(table);
    var tableBody = document.createElement("tbody");
    table.appendChild(tableBody);
	var row = document.createElement('tr');
    tableBody.appendChild(row);
    var col = document.createElement('td');
    row.appendChild(col);
   	var span = document.createElement('span');
   	span.className = 'jc_input_label';
//   	span.setAttribute('class', 'jc_input_label');
   	col.appendChild(span);
   	span.appendChild(document.createTextNode('Product class - ' + productClassName));
   	col = document.createElement('td');
   	row.appendChild(col);
   	var butContainer = document.createElement('div');
   	col.appendChild(butContainer);
   	butContainer.setAttribute('align', 'right');
    var button = new YAHOO.widget.Button({label: '<div class="buttonFixWidth_6">Remove</div>',
									      id: 'but_product_remove' + taxRegionProductId,
									      container: butContainer });
	button.on('click', function() {
		var url = "<c:out value='/${adminBean.contextPath}'/>/admin/tax/taxRegionMaint.do";
		var postData = "process=removeProductClass&taxRegionId=" + taxRegionId;
		postData += "&taxRegionProductId=" + taxRegionProductId;
		var callback = {
			success: function(o) {
		        if (o.responseText == undefined) {
		            return;
		        }
		        var jsonObject = eval('(' + o.responseText + ')');
		        var header = document.getElementById('product_header_' + taxRegionProductId);
		        var body = document.getElementById('product_body_' + taxRegionProductId);
		        productsContainer.removeChild(header);
		        productsContainer.removeChild(body);
		    },
		    failure: function(o) {
		    	jc_panel_show_error("Unexcepted Error: " + jsonObject.message);
		    }
		};
		var request = YAHOO.util.Connect.asyncRequest('POST', url, callback, postData);
	});
	productsContainer.appendChild(headerDiv);
}

function createProductBody(taxRegionProductId, productClassId, productClassName) {
	var productsContainer = document.getElementById('products_container');
	var bodyDiv = document.createElement('div');
	bodyDiv.className = 'jc_detail_panel';
	bodyDiv.setAttribute('id', 'product_body_' + taxRegionProductId);
	productsContainer.appendChild(bodyDiv);
	productsContainer.appendChild(document.createElement('br'));
}

var taxRegionProductId = "";
function addCustomerClass(trProductId) {
	taxRegionProductId = trProductId;
	jc_customerClass_search_show();
}

function jc_customerClass_search_client_callback(result) {
	var custClassId = result.custClassId;
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/tax/taxRegionMaint.do";
	var postData = "process=addCustomerClass&taxRegionProductId=" + taxRegionProductId + "&custClassId=" + custClassId;
	var callback = {
		success: function(o) {
		    if (o.responseText == undefined) {
		        return;
		    }
	        var jsonObject = eval('(' + o.responseText + ')');
	        if (jsonObject.status == 'failed') {
	        	jc_panel_show_message(jsonObject.message);
	        	return;
	        }
	    	paintProductBody(jsonObject.taxRegionProductId);
	    	jc_customerClass_search_finish();
		},
		failure: function(o) {
			jc_panel_show_message('Error showing product class search');
		}
	};
	var request = YAHOO.util.Connect.asyncRequest('POST', url, callback, postData);
}

function removeCustomerClass(type, args, menuItem) {
	var taxRegionProductCustId = menuItem.value;
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/tax/taxRegionMaint.do";
	var postData = "process=removeCustomerClass&taxRegionProductCustId=" + taxRegionProductCustId;
	var callback = {
		success: function(o) {
		    if (o.responseText == undefined) {
		        return;
		    }
	        var jsonObject = eval('(' + o.responseText + ')');
	    	paintProductBody(jsonObject.taxRegionProductId);
//	    	jc_productClass_search_finish();
		},
		failure: function(o) {
			jc_panel_show_message('Error showing product class search');
		}
	};
	var request = YAHOO.util.Connect.asyncRequest('POST', url, callback, postData);
}

var taxRegionProductCustId = null;
function addTax(type, args, menuItem) {
	taxRegionProductCustId = menuItem.value;
	jc_tax_search_show();
}

function jc_tax_search_client_callback(result) {
	var taxId = result.taxId;
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/tax/taxRegionMaint.do";
	var postData = "process=addTax&taxRegionProductCustId=" + taxRegionProductCustId + "&taxId=" + taxId;
	var callback = {
		success: function(o) {
		    if (o.responseText == undefined) {
		        return;
		    }
	        var jsonObject = eval('(' + o.responseText + ')');
	        if (jsonObject.status == 'failed') {
	        	jc_panel_show_message(jsonObject.message);
	        	return;
	        }
	        var jsonObject = eval('(' + o.responseText + ')');
	    	paintProductBody(jsonObject.taxRegionProductId);
	    	jc_tax_search_finish();
		},
		failure: function(o) {
			jc_panel_show_message('Error showing product class');
		}
	};
	var request = YAHOO.util.Connect.asyncRequest('POST', url, callback, postData);
}

function removeTaxes(type, args, menuItem) {
    var taxRegionProductCustId = menuItem.value;
    var name = 'checkbox_taxRegionProductCustTax_from_' + taxRegionProductCustId;
	var elements = jc_getElementsByName(name);
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/tax/taxRegionMaint.do";
	var postData = "process=removeTaxes&taxRegionProductCustId=" + taxRegionProductCustId;
    for (var i = 0; i < elements.length; i++) {
        var checkbox = elements[i];
        if (!checkbox.checked) {
            continue;
        }
        postData += "&taxRegionProductCustTaxIds=" + checkbox.id;
    }
 	var callback = {
		success: function(o) {
		    if (o.responseText == undefined) {
		        return;
		    }
	        var jsonObject = eval('(' + o.responseText + ')');
	    	paintProductBody(jsonObject.taxRegionProductId);
	    	jc_productClass_search_finish();
		},
		failure: function(o) {
			jc_panel_show_message('Error removing taxes');
		}
	};
	var request = YAHOO.util.Connect.asyncRequest('POST', url, callback, postData);
}

function sequenceTaxes(type, args, menuItem) {
    var taxRegionProductCustId = menuItem.value;
    var name = 'input_taxRegionProductCustTax_from_' + taxRegionProductCustId;
	var inputs = jc_getElementsByName(name, 'input');
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/tax/taxRegionMaint.do";
	var postData = "process=sequenceTaxes&taxRegionProductCustId=" + taxRegionProductCustId;
    for (var i = 0; i < inputs.length; i++) {
        postData += "&taxRegionProductCustTaxId_" + i + "=" + inputs[i].id;
        postData += "&seqNum_" + i + "=" + inputs[i].value;
    }
	var callback = {
		success: function(o) {
		    if (o.responseText == undefined) {
		        return;
		    }
	        var jsonObject = eval('(' + o.responseText + ')');
	        if (jsonObject.status == 'failed') {
	        	jc_panel_show_message(jsonObject.message);
	        	return;
	        }
	    	paintProductBody(jsonObject.taxRegionProductId);
		},
		failure: function(o) {
			jc_panel_show_message('Error sequencing tax rules');
		}
	};
	var request = YAHOO.util.Connect.asyncRequest('POST', url, callback, postData);
}

function paintProductBody(taxRegionProductId) {
	var bodyContainer = document.getElementById('product_detail_container_' + taxRegionProductId);
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/tax/taxRegionMaint.do";
	var postData = "process=getProduct&taxRegionProductId=" + taxRegionProductId;
	var callback = {
		success: function(o) {
	        if (o.responseText == undefined) {
	            return;
	        }
	        var jsonObject = eval('(' + o.responseText + ')');
	        var productContainer = document.getElementById('product_body_' + taxRegionProductId);
	        productContainer.innerHTML = '';
	        for (var i = 0; i < jsonObject.customers.length; i++) {
		        var customer = jsonObject.customers[i];
		        var headerDiv = document.createElement('div');
		        productContainer.appendChild(headerDiv);
		        var table = document.createElement('table');
                table.setAttribute('id', customer.taxRegionProductCustId);
		        table.setAttribute('width', '100%');
		        headerDiv.appendChild(table);
		        var tableBody = document.createElement("tbody");
		        table.appendChild(tableBody);
		    	var row = document.createElement('tr');
		        tableBody.appendChild(row);
		        var col = document.createElement('td');
		        row.appendChild(col);
		        col.appendChild(document.createTextNode('Customer class - ' + customer.custClassName));
		       	col = document.createElement('td');
		       	row.appendChild(col);
		       	var butDiv = document.createElement('div');
		       	butDiv.setAttribute('align', 'right');
		       	col.appendChild(butDiv);
		        var buttonMenu = [
         	        	{ text: "Remove this customer class", value: customer.taxRegionProductCustId, onclick: { fn: removeCustomerClass } },
         	        	{ text: "Add tax", value: customer.taxRegionProductCustId, onclick: { fn: addTax } },
         	        	{ text: "Remove tax", value: customer.taxRegionProductCustId, onclick: { fn: removeTaxes } },
         	        	{ text: "Sequence tax calculation", value: customer.taxRegionProductCustId, onclick: { fn: sequenceTaxes } }
		          	];
       			var menu = new YAHOO.widget.Button({ 
                                   type: "menu",
                                   id: "buttonFixWidth_6",
                                   label: "<em>Options</em>", 
                                   name: "menu",
                                   menu: buttonMenu, 
                                   container: butDiv });           

		        var taxTable = document.createElement('table');
		        productContainer.appendChild(taxTable);
		        taxTable.setAttribute('width', '100%');
		        productContainer.appendChild(taxTable);
		        var taxTableBody = document.createElement("tbody");
		        taxTable.appendChild(taxTableBody);
		        if (customer.taxes.length > 0) {
			    	var taxRow = document.createElement('tr');
			        taxTableBody.appendChild(taxRow);
			       	taxCol = document.createElement('td');
			       	taxRow.appendChild(taxCol);
			       	taxCol = document.createElement('td');
			       	taxRow.appendChild(taxCol);
			       	taxCol.setAttribute('width', '70%');
			       	taxCol.appendChild(document.createTextNode('Tax'));
			       	taxCol = document.createElement('td');
			       	taxCol.setAttribute('width', '30%');
			       	taxRow.appendChild(taxCol);
			       	taxCol.appendChild(document.createTextNode('Rate'));
			       	taxCol = document.createElement('td');
			       	taxRow.appendChild(taxCol);
			       	var centerDiv = document.createElement('div');
			       	centerDiv.setAttribute('align', 'center');
			       	centerDiv.appendChild(document.createTextNode('Sequence'));
			       	taxCol.appendChild(centerDiv);
		        }
		        
		       	for (var j = 0; j < customer.taxes.length; j++) {
			       	var trTax = customer.taxes[j];
			    	taxRow = document.createElement('tr');
			        taxTableBody.appendChild(taxRow);
			        
			        taxCol = document.createElement('td');
			        taxRow.appendChild(taxCol);
			        var checkbox = document.createElement('input');
			        checkbox.setAttribute('type', 'checkbox');
			        checkbox.setAttribute('name', 'checkbox_taxRegionProductCustTax_from_' + customer.taxRegionProductCustId);
			        checkbox.setAttribute('class', 'checkbox_taxRegionProductCustTax_from_' + customer.taxRegionProductCustId);
			        checkbox.setAttribute('id', trTax.taxRegionProductCustTaxId);
			        taxCol.appendChild(checkbox);
			        
			       	taxCol = document.createElement('td');
			       	taxRow.appendChild(taxCol);
			       	taxCol.appendChild(document.createTextNode(trTax.taxName));

			       	taxCol = document.createElement('td');
			       	taxRow.appendChild(taxCol);
			       	taxCol.appendChild(document.createTextNode(trTax.taxRate));

			       	taxCol = document.createElement('td');
			       	taxRow.appendChild(taxCol);
			       	centerDiv = document.createElement('div');
			       	centerDiv.setAttribute('align', 'center');
			       	taxCol.appendChild(centerDiv);
			       	
			        var textbox = document.createElement('input');
			        textbox.setAttribute('type', 'text');
			        textbox.setAttribute('id', trTax.taxRegionProductCustTaxId);
			        textbox.setAttribute('class', 'input_taxRegionProductCustTax_from_' + customer.taxRegionProductCustId);
			        textbox.setAttribute('name', 'input_taxRegionProductCustTax_from_' + customer.taxRegionProductCustId);
			        textbox.setAttribute('value', trTax.seqNum);
			        textbox.setAttribute('size', '2');
			       	centerDiv.appendChild(textbox);
		       	}

		       	if (i < jsonObject.customers.length) {
			       	productContainer.appendChild(document.createElement('hr'));
		       	}
	       	
	        }
	        var addCustomerTable = document.createElement('table');
	        addCustomerTable.setAttribute('width', '100%');
	        productContainer.appendChild(addCustomerTable);
	        var addCustomerTableBody = document.createElement("tbody");
	        addCustomerTable.appendChild(addCustomerTableBody);
	    	row = document.createElement('tr');
	        addCustomerTableBody.appendChild(row);
	        col = document.createElement('td');
	        row.appendChild(col);
	        col.appendChild(document.createTextNode('Add a customer class to this product class.  Items in the same product class can be taxed diffently by customer class'));
	       	col = document.createElement('td');
	       	row.appendChild(col);
	       	butDiv = document.createElement('div');
	       	butDiv.setAttribute('align', 'right');
	       	col.appendChild(butDiv);
	       	
	        var button = new YAHOO.widget.Button({label: '<div class="buttonFixWidth_6">Add customer</div>',
								                  id: "buttonFixWidth_6",
								                  name: 'addCustomerButton',
								                  container: butDiv });
				button.on('click', function() {
					addCustomerClass(taxRegionProductId);
			});
	    },
	    failure: function(o) {
	    	jc_panel_show_error("Unexcepted Error: " + jsonObject.message);
	    }
	};
	var request = YAHOO.util.Connect.asyncRequest('POST', url, callback, postData);
}



/***********************************************************/

if (mode != 'C') {
	YAHOO.util.Event.onDOMReady(function() {
		showRegions();
	} );
	YAHOO.util.Event.onDOMReady(function() {
		showZipCodes();
	} );
}
</script>
<div class=" yui-skin-sam">
  <html:form method="post" action="/admin/tax/taxRegionMaint">
	<html:hidden property="process" value="" />
	<html:hidden property="taxRegionId" />
	<html:hidden property="mode" />
	<table width="100%" border="0" cellspacing="0" cellpadding="0"
class="jc_top_navigation">
		<tr>
			<td>Administration - <a
				href="/<c:out value='${adminBean.contextPath}'/>/admin/tax/taxRegionListing.do?process=back">Tax
			Region Listing</a> - Tax Region Maintenance</td>
		</tr>
	</table>
	<br>
	<table width="100%" border="0" cellspacing="0" cellpadding="5">
		<tr>
			<td width="100%" valign="top">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				class="jc_panel_table_header">
				<tr>
					<td></td>
					<td align="right">
					<table width="0%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td><html:submit property="submitButton" value="Save"
								styleClass="jc_submit_button"
								onclick="return submitForm('save');" />&nbsp;</td>
							<td><html:submit property="submitButton" value="Cancel"
								styleClass="jc_submit_button"
								onclick="return submitCancelForm();" />&nbsp;</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="0" cellpadding="5">
				<tr>
					<td valign="top">
					<table width="100%" border="0" cellspacing="0" cellpadding="3">
						<tr>
							<td class="jc_input_label">Tax Region Description</td>
						</tr>
						<tr>
							<td class="jc_input_control"><html:text
								property="taxRegionDesc" size="40" styleClass="tableContent" />
							<logic:messagesPresent property="taxRegionDesc" message="true">
								<br>
								<html:messages property="taxRegionDesc" id="errorText"
									message="true">
									<span class="jc_input_error"><bean:write
										name="errorText" /></span>
								</html:messages>
							</logic:messagesPresent></td>
						</tr>
						<tr>
							<td class="jc_input_label">Shipping and Handling Product Class</td>
						</tr>
						<tr>
							<td class="jc_input_control">
							  <html:select property="shippingProductClassId"> 
                				<html:optionsCollection property="productClassList" label="label"/>
              				  </html:select>
							</td>
						</tr>
						<tr>
							<td class="jc_input_label">Published</td>
						</tr>
						<tr>
							<td class="jc_input_control"><html:checkbox
								property="published" value="Y" /></td>
						</tr>
					</table>
					</td>
					<layout:mode value="edit">
					<td id="" height="100%" valign="top">
					<div id="products_container"> </div>
					<div>
						<div class="jc_detail_panel_header">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td>Add a product class for this tax region. In the same region, items can be taxed 
								differently by its product class.
								</td>
								<td width="0">
								  <div align="right" id="product_add_button_container"></div>
								</td>
							</tr>
						</table>
						</div>
					</div>
					</td>
					</layout:mode>
					<layout:mode value="edit">
						<td width="5px" height="100%" valign="top">
						<div style="border-left: 1px solid #dcdcdc; height: 100%"></div>
						</td>
						<td valign="top" width="400">
						<div class="jc_detail_panel_header">
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td><span class="jc_input_label">Regions - Countries
								and States/Province</span></td>
								<td width="100%">
								<div align="right" id="regionButtonOptionContainer"></div>
								</td>
							</tr>
						</table>
						</div>
						<div>
						<div id="regionContainer" class="jc_detail_panel"></div>
						</div>
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
					</layout:mode>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</html:form></div>

<div class=" yui-skin-sam">
	<div id="jc_zipcode_panel">
		<div class="hd">Zip code</div>
		<div class="bd">
			<div align="right" id="zipCodeButtonContainer"></div>
			<div id="jc_zipcode_message" class="jc_input_error"></div>
			<table border="0" cellspacing="0" cellpadding="5">
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
<%@ include file="/admin/include/productClassLookup.jsp"%>
<%@ include file="/admin/include/customerClassLookup.jsp"%>
<%@ include file="/admin/include/taxLookup.jsp"%>

