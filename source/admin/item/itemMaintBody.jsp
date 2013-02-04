<%@ page language="java" import="com.jada.util.Utility" %>
<jsp:directive.page import="com.jada.ui.dropdown.DropDownMenuGenerator"/>
<jsp:directive.page import="com.jada.ui.dropdown.DropDownMenu"/>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<jsp:useBean id="adminBean"  type="com.jada.admin.AdminBean"  scope="session" />
<jsp:useBean id="itemMaintForm"  type="com.jada.admin.item.ItemMaintActionForm"  scope="request" />

<script type="text/javascript" language="JavaScript">
var siteProfileClassDefault = ${itemMaintForm.siteProfileClassDefault};
var siteCurrencyClassDefault = ${itemMaintForm.siteCurrencyClassDefault};
var translationEnable = ${itemMaintForm.translationEnable};
var siteProfileClassId = "${itemMaintForm.siteProfileClassId}";
var siteCurrencyClassId = "${itemMaintForm.siteCurrencyClassId}";
var itemTypeCd = "${itemMaintForm.itemTypeCd}";
var itemSkusExist = ${itemMaintForm.itemSkusExist};
var mode = "${itemMaintForm.mode}";
var itemId = "${itemMaintForm.itemId}";
var imageCounter = 0;

var commentLoaded = false;
function showComment() {
  if (!commentLoaded) {
    var text = document.getElementById("tabComment");
    text.innerHTML = 'loading ...';
    showId('tabComment');
    var itemId = <c:out value='${itemMaintForm.itemId}'/>
    var url = "<c:out value='/${adminBean.contextPath}'/>/admin/item/itemMaint.do";
    var data = "process=comments&itemId=" + itemId ;
    var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_comments_callback, data);
    commentLoaded = true;
  }
  showId('tabComment');
  hideId('tabComment_show');
  showId('tabComment_hide');
}
function hideComment() {
  hideId('tabComment');
  showId('tabComment_show');
  hideId('tabComment_hide');
}
var jc_comments_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: Unable to retrieve comments");
      return false;
    }
    var text = document.getElementById("tabComment");
    // empty table for formatting only.
    text.innerHTML = '<table width="100%" border="0" cellspacing="0" cellpadding="0"><tr><td>&nbsp;</td></tr></table>';
    var jsonObject = getJsonObject(o.responseText);
    for (var i = 0; i < jsonObject.comments.length; i++) {
      var comment = jsonObject.comments[i];
      text.innerHTML += '<b>' + comment.commentTitle + '</b><br>' +
                        comment.comment + '<br><br>' +
                        'Posted by ' + comment.custPublicName + ' (' + comment.custEmail + ') <br>on ' + comment.recCreateDatetime + '<br><br>';
      text.innerHTML += 'Agree (' + comment.agreeCount + ')';
      text.innerHTML += ', Disagree (' + comment.disagreeCount + ')';
      if (comment.moderation == 'Y') {
        text.innerHTML += ', Alerted by visitor';
      }
      if (comment.commentApproved == 'Y') {
        text.innerHTML += ', Approved';
      }
      else if (comment.commentApproved == 'N') {
        text.innerHTML += ', Disapproved';
      }
      else {
        text.innerHTML += ', Not moderated';
      }
      text.innerHTML += '<br><br>';
    }
  },
  failure: function(o) {
      jc_panel_show_error("Unexcepted Error: Unable to retrieve comments");
  }
};

function submitDefaultImage(input) {
    document.forms[0].process.value = "defaultImage";
    document.forms[0].createDefaultImageId.value = input;
}

function submitForm(type) {
    if (!siteProfileClassDefault || !siteCurrencyClassDefault) {
        if (!siteProfileClassDefault) {
			document.itemMaintForm.itemShortDescLang.value = document.itemMaintForm.itemShortDescLang_tmp.value;
			document.itemMaintForm.pageTitleLang.value = document.itemMaintForm.pageTitleLang_tmp.value;
			document.itemMaintForm.metaKeywordsLang.value = document.itemMaintForm.metaKeywordsLang_tmp.value;
			document.itemMaintForm.metaDescriptionLang.value = document.itemMaintForm.metaDescriptionLang_tmp.value;
			jc_editor_itemDescLang.saveHTML();
			var inputBoxes = new Array();
			var e = document.getElementById("tabCustomAttributes");
			jc_traverse_element(e, inputBoxes, 'input', 'itemAttributeDetail.*');
			for (var i = 0; i < inputBoxes.length; i++) {
				var masterName = inputBoxes[i].name;
				var pos = masterName.search('itemAttributeDetail.*itemAttribDetailValueLang$');
				if (pos == -1) {
					continue;
				}
				var masterObject = document.itemMaintForm.elements[masterName];
				var sourceName = masterName + '_tmp';
				var sourceObject = document.itemMaintForm.elements[sourceName];
				masterObject.value = sourceObject.value;
			}
        }
        if (!siteCurrencyClassDefault) {
			document.itemMaintForm.itemPriceCurr.value = document.itemMaintForm.itemPriceCurr_tmp.value;
			document.itemMaintForm.itemSpecPriceCurr.value = document.itemMaintForm.itemSpecPriceCurr_tmp.value;
        }
    }
    else {
      jc_editor_itemDesc.saveHTML();
    }
    document.itemMaintForm.process.value = type;
    document.itemMaintForm.submit();
    return false;
}

function submitBackForm(type) {
    document.forms[0].action = "/<c:out value='${adminBean.contextPath}'/>/admin/item/itemListing.do";
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}


/*
 * Tier price processing 
 */
 
YAHOO.util.Event.onContentReady('butItemTierPriceAddContainer', function() {
    var button = new YAHOO.widget.Button({label: 'Add tier price',
                                          id: 'butAddTierPrice',
                                          disabled: true,
                                          container: 'butItemTierPriceAddContainer' });
    if (siteCurrencyClassDefault && siteProfileClassDefault) {
        button.set("disabled", false);
    }
    button.on('click', function() {
		addTierPrice();
    });
} );

function createTierPriceTable(itemTierPriceId, itemTierQty, itemTierPrice, itemTierPriceCurr, itemTierPriceCurrFlag, itemTierPricePublishOn, itemTierPriceExpireOn, custClassId, customerClasses) {
    var table = document.createElement('table');
    table.setAttribute('cellpadding', '5');
    table.className = 'jc_nobordered_table';
    table.setAttribute('width', '400');
    var body = document.createElement('tbody');
    table.appendChild(body);
    var div;
    var input;
    var row = document.createElement('tr');
    body.appendChild(row);
    var col;
    col = document.createElement('td');
    col.setAttribute('width', '40%');
    row.appendChild(col);
    col = document.createElement('td');
    col.setAttribute('width', '60%');
    row.appendChild(col);
    div = document.createElement('div');
    div.setAttribute('align', 'right');
    col.appendChild(div);
    var buttonMenu = null;
    if (siteCurrencyClassDefault) {
		buttonMenu = [ { text: "Save tier price", value: itemTierPriceId, onclick: { fn: saveTierPrice } }, 
	      			   { text: "Remove tier price", value: itemTierPriceId, onclick: { fn: removeTierPrice } }
	      	         ];
    }
    else {
		buttonMenu = [ { text: "Save tier price", value: itemTierPriceId, onclick: { fn: saveTierPrice } }
	      	         ];
    }
    var menu = new YAHOO.widget.Button({ type: "menu", 
                                		 label: "Options", 
                                		 name: "menu",
                                		 menu: buttonMenu, 
                                		 container: div });
	if (!siteProfileClassDefault && siteCurrencyClassDefault) {
		menu.set('disabled', true);
	}

    row = document.createElement('tr');
    body.appendChild(row);
    col = document.createElement('td');
    row.appendChild(col);
    col.className = 'jc_input_label';
    col.setAttribute('width', '40%');
    col.appendChild(document.createTextNode("Customer Class"));
    col = document.createElement('td');
    row.appendChild(col);

    if (siteCurrencyClassDefault && siteProfileClassDefault) {
	    var select = document.createElement('select');
	    var option;
	    col.appendChild(select);
	    select.setAttribute('name', 'custClassId_' + itemTierPriceId);
	    for (var i = 0; i < customerClasses.length; i++) {
	        var customerClass = customerClasses[i];
	        option = document.createElement('option');
	        option.value = customerClass.custClassId;
	        option.appendChild(document.createTextNode(customerClass.custClassName));
	        if (customerClass.custClassId == custClassId) {
		        option.selected = true;
	        }
	        select.appendChild(option);
	    }
    }
    else {
	    for (var i = 0; i < customerClasses.length; i++) {
	        customerClass = customerClasses[i];
	        if (customerClass.custClassId == custClassId) {
		        col.appendChild(document.createTextNode(customerClass.custClassName));
	        }
	    }
    }

    row = document.createElement('tr');
    body.appendChild(row);
    col = document.createElement('td');
    row.appendChild(col);
    col.appendChild(document.createTextNode("Order Quantity"));
    col.className = 'jc_input_label';
    col = document.createElement('td');
    row.appendChild(col);
    input = document.createElement('input');
    YAHOO.util.Dom.addClass(input, 'itemTierQty_' + itemTierPriceId); 
    YAHOO.util.Dom.addClass(input, 'jc_input_control'); 
    input.setAttribute('type', 'text');
    input.setAttribute('name', 'itemTierQty_' + itemTierPriceId);
    input.setAttribute('value', itemTierQty);
    if (!siteCurrencyClassDefault || !siteProfileClassDefault) {
    	input.disabled = true;
    }
    col.appendChild(input);
    div = document.createElement('div');
    div.setAttribute('id', 'itemTierQtyError_' + itemTierPriceId);
    div.className = 'jc_input_error';
    col.appendChild(div);

    row = document.createElement('tr');
    body.appendChild(row);
    col = document.createElement('td');
    row.appendChild(col);
    if (!siteCurrencyClassDefault) {
        var checkbox = document.createElement('input');
        checkbox.setAttribute('type', 'checkbox');
        checkbox.setAttribute('name', 'itemTierPriceOverride_' + itemTierPriceId);
        if (itemTierPriceCurrFlag) {
            checkbox.checked = true;
        }
        YAHOO.util.Event.addListener(checkbox, "click", function() {
            var object = jc_getElementByName('itemTierPrice_' + itemTierPriceId, 'input', 'itemTierPriceContainer_' + itemTierPriceId);
            if (checkbox.checked) {
                object.disabled = false;
             }
            else {
            	jc_panel_confirm('Confirm to use default currency?', function(confirm) {
          	      if (confirm) {
          	        object.value = itemTierPrice;
          	      	object.disabled = true;
          	      }
          	      else {
          	        object.checked = true;
          	      }
          	    });
            }
        });
        col.appendChild(document.createTextNode('Override Currency'));
        col.appendChild(checkbox);
        col.appendChild(document.createElement('br'));
    }
    col.appendChild(document.createTextNode("Price"));
    col.className = 'jc_input_label';
    col = document.createElement('td');
    row.appendChild(col);
    
    input = document.createElement('input');
    YAHOO.util.Dom.addClass(input, 'itemTierPrice_' + itemTierPriceId); 
    YAHOO.util.Dom.addClass(input, 'jc_input_control'); 
    input.setAttribute('type', 'text');
    input.setAttribute('name', 'itemTierPrice_' + itemTierPriceId);
    if (siteCurrencyClassDefault) {
    	input.setAttribute('value', itemTierPrice);
        if (!siteCurrencyClassDefault || !siteProfileClassDefault) {
        	input.disabled = true;
        }
    }
    else {
	    if (itemTierPriceCurrFlag) {
	    	input.setAttribute('value', itemTierPriceCurr);
	    }
	    else {
	        input.setAttribute('value', itemTierPrice);
	        input.disabled = true;
	    }
    }
    
    col.appendChild(input);
    div = document.createElement('div');
    div.setAttribute('id', 'itemTierPriceError_' + itemTierPriceId);
    div.className = 'jc_input_error';
    col.appendChild(div);

    var img;
    row = document.createElement('tr');
    body.appendChild(row);
    col = document.createElement('td');
    row.appendChild(col);
    col.appendChild(document.createTextNode("Publish On"));
    col.className = 'jc_input_label';
    col = document.createElement('td');
    row.appendChild(col);
    input = document.createElement('input');
    YAHOO.util.Dom.addClass(input, 'itemTierPricePublishOn_' + itemTierPriceId); 
    YAHOO.util.Dom.addClass(input, 'jc_input_control'); 
    input.setAttribute('type', 'text');
    input.setAttribute('id', 'itemTierPricePublishOn_' + itemTierPriceId);
    input.setAttribute('name', 'itemTierPricePublishOn_' + itemTierPriceId);
    input.setAttribute('value', itemTierPricePublishOn);
    if (!siteCurrencyClassDefault || !siteProfileClassDefault) {
    	input.disabled = true;
    }
    col.appendChild(input);
    col.appendChild(document.createTextNode(' '));
    img = document.createElement('img');
    img.setAttribute('id', 'calItemTierPricePublishOn_' + itemTierPriceId);
    img.setAttribute('src', '../images/icon/image_plus.gif');
    img.setAttribute('border', '0');
    col.appendChild(img);
	div = document.createElement('div');
    div.setAttribute('id', 'calItemTierPricePublishOnContainer_' + itemTierPriceId);
    div.style.position = 'absolute';
    div.style.display = 'none';
    div.style.zIndex = '99999';
    col.appendChild(div);
    div = document.createElement('div');
    div.setAttribute('id', 'itemTierPricePublishOnError_' + itemTierPriceId);
    div.className = 'jc_input_error';
    col.appendChild(div);
	initCalendar('calItemTierPricePublishOnContainer_' + itemTierPriceId, 'itemTierPricePublishOn_' + itemTierPriceId, 'calItemTierPricePublishOn_' + itemTierPriceId);

    row = document.createElement('tr');
    body.appendChild(row);
    col = document.createElement('td');
    row.appendChild(col);
    col.appendChild(document.createTextNode("Expire On"));
    col.className = 'jc_input_label';
    col = document.createElement('td');
    row.appendChild(col);
    input = document.createElement('input');
    input.setAttribute('type', 'text');
    YAHOO.util.Dom.addClass(input, 'itemTierPriceExpireOn_' + itemTierPriceId); 
    YAHOO.util.Dom.addClass(input, 'jc_input_control'); 
    input.setAttribute('id', 'itemTierPriceExpireOn_' + itemTierPriceId);
    input.setAttribute('name', 'itemTierPriceExpireOn_' + itemTierPriceId);
    input.setAttribute('value', itemTierPriceExpireOn);
    if (!siteCurrencyClassDefault || !siteProfileClassDefault) {
    	input.disabled = true;
    }
    col.appendChild(input);
    col.appendChild(document.createTextNode(' '));
    img = document.createElement('img');
    img.setAttribute('id', 'calItemTierPriceExpireOn_' + itemTierPriceId);
    img.setAttribute('src', '../images/icon/image_plus.gif');
    img.setAttribute('border', '0');
    col.appendChild(img);
	div = document.createElement('div');
    div.setAttribute('id', 'calItemTierPriceExpireOnContainer_' + itemTierPriceId);
    div.style.zIndex = '99999';
    div.style.position = 'absolute';
    div.style.display = 'none';
    col.appendChild(div);
	initCalendar('calItemTierPriceExpireOnContainer_' + itemTierPriceId, 'itemTierPriceExpireOn_' + itemTierPriceId, 'calItemTierPriceExpireOn_' + itemTierPriceId);
    div = document.createElement('div');
    div.className = 'jc_input_error';
    div.setAttribute('id', 'itemTierPriceExpireOnError_' + itemTierPriceId);
    col.appendChild(div);

    row = document.createElement('tr');
    body.appendChild(row);
    col = document.createElement('td');
    col.colSpan = '2';
    col.appendChild(document.createElement('hr'));
    row.appendChild(col);

    return table;
}

function addTierPrice() {
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/item/itemMaint.do";
	var postData = "process=addTierPrice&itemId=" + itemId;
	var callback = {
		success: function(o) {
		    if (o.responseText == undefined) {
		        return;
		    }
	        var jsonObject = eval('(' + o.responseText + ')');
	        if (jsonObject.status == 'success') {
	            var mainContainer = document.getElementById("tabTierPricing");
	            var node = document.createElement("div");
	            var id = 'new' + getRandom();
	            node.setAttribute("id", "itemTierPriceContainer_" + id);
	            node.appendChild(createTierPriceTable(id, '', '', '', '', '', '', '', jsonObject.customerClasses));
	            var butAddContainer = document.getElementById('butItemTierPriceAddContainer');
	            mainContainer.insertBefore(node, butAddContainer);
	        }
		},
		failure: function(o) {
			jc_panel_show_message('Error adding tier price search');
		}
	};
	var request = YAHOO.util.Connect.asyncRequest('POST', url, callback, postData);
}

function saveTierPrice(type, args, menuItem) {
	var itemTierPriceId = menuItem.value.toString();
	var url = "/${adminBean.contextPath}/admin/item/itemMaint.do";
	var postData = "process=saveTierPrice&itemId=" + itemId;
	if (itemTierPriceId.match('new') != null) {
		postData += "&itemTierPriceId=";
	}
	else {
		postData += "&itemTierPriceId=" + itemTierPriceId;
	}
	postData += "&siteCurrencyClassId=" + siteCurrencyClassId;
	if (siteCurrencyClassDefault) {
		postData += "&custClassId=" + jc_getElementByName('custClassId_' + itemTierPriceId, 'select', 'itemTierPriceContainer_' + itemTierPriceId).value;
		postData += "&itemTierQty=" + jc_getElementByName('itemTierQty_' + itemTierPriceId, 'input', 'itemTierPriceContainer_' + itemTierPriceId).value;
		postData += "&itemTierPricePublishOn=" + jc_getElementByName('itemTierPricePublishOn_' + itemTierPriceId, 'input', 'itemTierPriceContainer_' + itemTierPriceId).value;
		postData += "&itemTierPriceExpireOn=" + jc_getElementByName('itemTierPriceExpireOn_' + itemTierPriceId, 'input', 'itemTierPriceContainer_' + itemTierPriceId).value;
	}
	else {
		postData += "&itemTierPriceOverride=" + jc_getElementByName('itemTierPriceOverride_' + itemTierPriceId, 'input', 'itemTierPriceContainer_' + itemTierPriceId).checked;
	}
	postData += "&itemTierPrice=" + jc_getElementByName('itemTierPrice_' + itemTierPriceId, 'input', 'itemTierPriceContainer_' + itemTierPriceId).value;
	var callback = {
		success: function(o) {
		    if (o.responseText == undefined) {
		        return;
		    }
	        var jsonObject = eval('(' + o.responseText + ')');
	        if (jsonObject.status == 'success') {
		        if (itemTierPriceId.match('new')) {
			        var priceContainer = document.getElementById('itemTierPriceContainer_' + itemTierPriceId);
			        itemTierPriceId = jsonObject.itemTierPriceId;
			        priceContainer.setAttribute('id', 'itemTierPriceContainer_' + itemTierPriceId);
		        }
	        	paintItemTierPrice(itemTierPriceId);
	        }
	        else {
		        var object = null;
		        if (jsonObject.itemTierQty != null) {
			        object = document.getElementById('itemTierQtyError_' + itemTierPriceId);
			        object.innerHTML = jsonObject.itemTierQty;
		        }
		        if (jsonObject.itemTierPrice != null) {
			        object = document.getElementById('itemTierPriceError_' + itemTierPriceId);
			        object.innerHTML = jsonObject.itemTierPrice;
		        }
		        if (jsonObject.itemTierPricePublishOn != null) {
			        object = document.getElementById('itemTierPricePublishOnError_' + itemTierPriceId);
			        object.innerHTML = jsonObject.itemTierPricePublishOn;
		        }
		        if (jsonObject.itemTierPriceExpireOn != null) {
			        object = document.getElementById('itemTierPriceExpireOnError_' + itemTierPriceId);
			        object.innerHTML = jsonObject.itemTierPriceExpireOn;
		        }
	        }
		},
		failure: function(o) {
			jc_panel_show_message('Error saving tier price');
		}
	};
	var request = YAHOO.util.Connect.asyncRequest('POST', url, callback, postData);
}

function removeTierPrice(type, args, menuItem) {
	var itemTierPriceId = menuItem.value;
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/item/itemMaint.do";
	var postData = "process=removeTierPrice&itemTierPriceId=" + itemTierPriceId + "&itemId=" + itemId;
	var callback = {
		success: function(o) {
		    if (o.responseText == undefined) {
		        return;
		    }
	        var jsonObject = eval('(' + o.responseText + ')');
	        var mainContainer = document.getElementById('tabTierPricing');
	        var childContainer = document.getElementById('itemTierPriceContainer_' + itemTierPriceId);
	        mainContainer.removeChild(childContainer);
		},
		failure: function(o) {
			jc_panel_show_message('Error showing product class search');
		}
	};
	var request = YAHOO.util.Connect.asyncRequest('POST', url, callback, postData);
}

var showItemTierPrice_callback =
{
	success: function(o) {
	    if (!isJsonResponseValid(o.responseText)) {
			jc_panel_show_error("Unexcepted Error: Unable to extract item tier price");
			return false;
	    }
	    var jsonObject = getJsonObject(o.responseText);
	    var node = document.getElementById("itemTierPriceContainer_" + jsonObject.itemTierPriceId);
	    node.innerHTML = '';
		node.appendChild(createTierPriceTable(jsonObject.itemTierPriceId, 
											  jsonObject.itemTierQty,
											  jsonObject.itemTierPrice,
											  jsonObject.itemTierPriceCurr,
											  jsonObject.itemTierPriceCurrFlag,
											  jsonObject.itemTierPricePublishOn,
											  jsonObject.itemTierPriceExpireOn,
											  jsonObject.custClassId,
											  jsonObject.customerClasses));
  },
  failure: function(o) {
      jc_panel_show_error("Unexcepted Error: Unable to extract item tier price");
  }
};

function paintItemTierPrice(itemTierPriceId) {
	var url = "/${adminBean.contextPath}/admin/item/itemMaint.do?process=getItemTierPrice&siteCurrencyClassId=" + siteCurrencyClassId + "&itemTierPriceId=" + itemTierPriceId;
	var request = YAHOO.util.Connect.asyncRequest('GET', url, showItemTierPrice_callback);
}

/*
 * Item Skus processing
 */
var generateItemSkus_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to generate item skus");
      return false;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    itemSkusExist = true;
    paintItemSkuButton();
    paintItemSkus();
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to aadd related item");
  }
};

function generateItemSkus() {
	var url = "/${adminBean.contextPath}/admin/item/itemMaint.do";
	var itemNum = document.itemMaintForm.itemNum.value;
	var data = "process=generateItemSkus&itemId=" + itemId + "&itemNum=" + itemNum;
	var request = YAHOO.util.Connect.asyncRequest('POST', url, generateItemSkus_callback, data);
}
 
var showItemSkus_callback =
{
 	success: function(o) {
 	    if (!isJsonResponseValid(o.responseText)) {
 			jc_panel_show_error("Unexcepted Error: Unable to extract skus");
 			return false;
 	    }
 	    var jsonObject = getJsonObject(o.responseText);
 	    var node = document.getElementById("itemSkusContainer");
 	    node.innerHTML = '';
 	    var table = document.createElement('table');
 	    table.setAttribute('id', 'itemSkus');
 	    table.setAttribute('cellpadding', '5');
 	    table.className = 'jc_nobordered_table';
 	    table.setAttribute('width', '400');
 	    var body = document.createElement('tbody');
 	    table.appendChild(body);
 	    if (jsonObject.items.length > 0) {
 		    var row = document.createElement('tr');
 		    body.appendChild(row);
 		    var col = document.createElement('td');
 		    col.setAttribute('width', '100');
 		    col.className = 'jc_input_label';
 		    col.appendChild(document.createTextNode('Item Sku Code'));
			row.appendChild(col);
 	    }
 	   	for (var i = 0; i < jsonObject.items.length; i++) {
 		    var item = jsonObject.items[i];
 		    var row = document.createElement('tr');
 		    body.appendChild(row);
 		    col = document.createElement('td');
 		    var anchor = document.createElement('a');
 		    anchor.setAttribute('href', '/${adminBean.contextPath}/admin/item/itemMaint.do?process=edit&itemId=' + item.itemId);
 		    anchor.appendChild(document.createTextNode(item.itemSkuCd));
 		    col.appendChild(anchor);
			row.appendChild(col);
 		}
 		node.appendChild(table);
   },
   failure: function(o) {
       jc_panel_show_error("Unexcepted Error: Unable to extract item sku code");
   }
};
 
function paintItemSkus() {
	var url = "/${adminBean.contextPath}/admin/item/itemMaint.do";
	var itemNum = document.itemMaintForm.itemNum.value;
	var data = "process=getItemSkus&itemId=" + itemId + "&itemNum=" + itemNum;
	var request = YAHOO.util.Connect.asyncRequest('POST', url, showItemSkus_callback, data);
}

function paintItemSkuButton() {
	var container = document.getElementById('butItemSkusContainer');
	container.innerHTML = '';
	var buttonMenu = [];
	if (!itemSkusExist) {
		buttonMenu.push({ text: "Generate SKU Codes", onclick: { fn: generateItemSkus } });
	}
    buttonMenu.push({ text: "Add SKU Code", onclick: { fn: generateItemSkus } });

    var menu = new YAHOO.widget.Button({ type: "menu", 
                                		 label: "Options", 
                                		 name: "itemSkus",
                                		 menu: buttonMenu, 
                                		 disabled: true,
                                		 container: "butItemSkusContainer" });
	if (siteProfileClassDefault && siteCurrencyClassDefault) {
		menu.set('disabled', false);
	}
}

YAHOO.util.Event.onContentReady('itemSkusContainer', function() {
	paintItemSkuButton();
} );

if (itemTypeCd == '02') {
	YAHOO.util.Event.onContentReady('itemSkusContainer', function() {
		paintItemSkus();
	} );
}

/*
 * Items bundle processing
 */
var jc_item_search_panel = null;
var addItemBundle_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to add bundle item");
      return false;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    paintItemsBundle();
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to add bundle item");
  }
};
function jc_item_bundle_search_client_callback(value) {
	jc_item_search_panel.hide();
   
	var jsonObject = eval('(' + value + ')');
   	for (var i = 0; i < jsonObject.items.length; i++) {
		var item = jsonObject.items[i];
		var url = "/${adminBean.contextPath}/admin/item/itemMaint.do";
		var data = "process=addItemBundle&itemId=" + itemId + "&itemChildId=" + item.itemId;
		var request = YAHOO.util.Connect.asyncRequest('POST', url, addItemBundle_callback, data);
   	}
	return false;
}

function addItemBundle() {
	jc_item_search_show(jc_item_bundle_search_client_callback);
}

var jc_itemsBundle_remove_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to remove bundle items");
      return false;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    paintItemsBundle();
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to remove bundle items");
  }
};

function removeItemsBundle() {
	var itemId = '${itemMaintForm.itemId}';
	var url = "/${adminBean.contextPath}/admin/item/itemMaint.do";
	var data = "process=removeItemsBundle&itemId=" + itemId;
	
	var e = document.getElementById("itemsBundle");
	var checkboxes = new Array();
	jc_traverse_element(e, checkboxes, 'input', 'itemChildIds.*');

	var count = 0;
	for (var i = 0; i < checkboxes.length; i++) {
	  if (!checkboxes[i].checked) {
	    continue;
	  }
	  data += "&itemChildIds=" + checkboxes[i].value;
	}
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_itemsBundle_remove_callback, data);
	return false;
}
 
var showItemsBundle_callback =
{
 	success: function(o) {
 	    if (!isJsonResponseValid(o.responseText)) {
 			jc_panel_show_error("Unexcepted Error: Unable to extract bundle items");
 			return false;
 	    }
 	    var jsonObject = getJsonObject(o.responseText);
 	    var node = document.getElementById("itemsBundleContainer");
 	    node.innerHTML = '';
 	    var table = document.createElement('table');
 	    table.setAttribute('id', 'itemsBundle');
 	    table.setAttribute('cellpadding', '5');
 	    table.className = 'jc_nobordered_table';
 	    table.setAttribute('width', '400');
 	    var body = document.createElement('tbody');
 	    table.appendChild(body);
 	    if (jsonObject.items.length > 0) {
 		    var row = document.createElement('tr');
 		    body.appendChild(row);
 		    var col = document.createElement('td');
 		    col.setAttribute('width', '50');
 		    col.appendChild(document.createTextNode(''));
			row.appendChild(col);
 		    col = document.createElement('td');
 		    col.setAttribute('width', '100');
 		    col.className = 'jc_input_label';
 		    col.appendChild(document.createTextNode('Item Number'));
			row.appendChild(col);
 		    col = document.createElement('td');
 		    col.className = 'jc_input_label';
 		    col.appendChild(document.createTextNode('Short Description'));
			row.appendChild(col);
 	    }
 	   	for (var i = 0; i < jsonObject.items.length; i++) {
 		    var item = jsonObject.items[i];
 		    var row = document.createElement('tr');
 		    body.appendChild(row);
 		    col = document.createElement('td');
 		    var input = document.createElement('input');
 		    input.setAttribute('type', 'checkbox');
 		    input.setAttribute('name', 'itemChildIds');
 		    input.setAttribute('value', item.itemId);
 		 	row.appendChild(input);
 		    col = document.createElement('td');
 		    col.appendChild(document.createTextNode(item.itemNum));
			row.appendChild(col);
 		    col = document.createElement('td');
 		    col.appendChild(document.createTextNode(item.itemShortDesc));
			row.appendChild(col);
 		}
 		node.appendChild(table);
   },
   failure: function(o) {
       jc_panel_show_error("Unexcepted Error: Unable to extract related items");
   }
};
 
function paintItemsBundle() {
	var url = "/${adminBean.contextPath}/admin/item/itemMaint.do";
	var data = "process=getItemsBundle&itemId=" + itemId;
	var request = YAHOO.util.Connect.asyncRequest('POST', url, showItemsBundle_callback, data);
}

YAHOO.util.Event.onContentReady('butItemsBundleContainer', function() {
	var buttonMenu = [ { text: "Add Bundle Item", onclick: { fn: addItemBundle } }, 
	      			   { text: "Remove Bundle Item", onclick: { fn: removeItemsBundle } }
	      	         ];
    var menu = new YAHOO.widget.Button({ type: "menu", 
                                		 label: "Options", 
                                		 name: "itemsBundle",
                                		 menu: buttonMenu, 
                                		 disabled: true,
                                		 container: "butItemsBundleContainer" });
	if (siteProfileClassDefault && siteCurrencyClassDefault) {
		menu.set('disabled', false);
	}
} );
	
YAHOO.util.Event.onContentReady('itemsBundleContainer', function() {
	paintItemsBundle();
} );


/*
 * Items related processing
 */
var jc_item_search_panel = null;
var addItemRelated_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to add related item");
      return false;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    paintItemsRelated();
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to aadd related item");
  }
};
function jc_item_related_search_client_callback(value) {
	jc_item_search_panel.hide();
   
	var jsonObject = eval('(' + value + ')');
   	for (var i = 0; i < jsonObject.items.length; i++) {
		var item = jsonObject.items[i];
		var url = "/${adminBean.contextPath}/admin/item/itemMaint.do";
		var data = "process=addItemRelated&itemId=" + itemId + "&itemRelatedId=" + item.itemId;
		var request = YAHOO.util.Connect.asyncRequest('POST', url, addItemRelated_callback, data);
   	}
	return false;
}

function addItemRelated() {
	jc_item_search_show(jc_item_related_search_client_callback);
}

var jc_itemsRelated_remove_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to remove related items");
      return false;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    paintItemsRelated();
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to remove related items");
  }
};

function removeItemsRelated() {
	var itemId = '${itemMaintForm.itemId}';
	var url = "/${adminBean.contextPath}/admin/item/itemMaint.do";
	var data = "process=removeItemsRelated&itemId=" + itemId;
	
	var e = document.getElementById("itemsRelated");
	var checkboxes = new Array();
	jc_traverse_element(e, checkboxes, 'input', 'itemRelatedIds.*');

	var count = 0;
	for (var i = 0; i < checkboxes.length; i++) {
	  if (!checkboxes[i].checked) {
	    continue;
	  }
	  data += "&itemRelatedIds=" + checkboxes[i].value;
	}
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_itemsRelated_remove_callback, data);
	return false;
}
 
var showItemsRelated_callback =
{
 	success: function(o) {
 	    if (!isJsonResponseValid(o.responseText)) {
 			jc_panel_show_error("Unexcepted Error: Unable to extract related items");
 			return false;
 	    }
 	    var jsonObject = getJsonObject(o.responseText);
 	    var node = document.getElementById("itemsRelatedContainer");
 	    node.innerHTML = '';
 	    var table = document.createElement('table');
 	    table.setAttribute('id', 'itemsRelated');
 	    table.setAttribute('cellpadding', '5');
 	    table.className = 'jc_nobordered_table';
 	    table.setAttribute('width', '400');
 	    var body = document.createElement('tbody');
 	    table.appendChild(body);
 	    if (jsonObject.items.length > 0) {
 		    var row = document.createElement('tr');
 		    body.appendChild(row);
 		    var col = document.createElement('td');
 		    col.setAttribute('width', '50');
 		    col.appendChild(document.createTextNode(''));
			row.appendChild(col);
 		    col = document.createElement('td');
 		    col.setAttribute('width', '100');
 		    col.className = 'jc_input_label';
 		    col.appendChild(document.createTextNode('Item Number'));
			row.appendChild(col);
 		    col = document.createElement('td');
 		    col.className = 'jc_input_label';
 		    col.appendChild(document.createTextNode('Short Description'));
			row.appendChild(col);
 	    }
 	   	for (var i = 0; i < jsonObject.items.length; i++) {
 		    var item = jsonObject.items[i];
 		    var row = document.createElement('tr');
 		    body.appendChild(row);
 		    col = document.createElement('td');
 		    row.appendChild(col);
 		    var input = document.createElement('input');
 		    input.setAttribute('type', 'checkbox');
 		    input.setAttribute('name', 'itemRelatedIds');
 		    input.setAttribute('value', item.itemId);
 		 	col.appendChild(input);
 		    col = document.createElement('td');
 		    col.appendChild(document.createTextNode(item.itemNum));
			row.appendChild(col);
 		    col = document.createElement('td');
 		    col.appendChild(document.createTextNode(item.itemShortDesc));
			row.appendChild(col);
 		}
 		node.appendChild(table);
   },
   failure: function(o) {
       jc_panel_show_error("Unexcepted Error: Unable to extract related items");
   }
};
 
function paintItemsRelated() {
	var url = "/${adminBean.contextPath}/admin/item/itemMaint.do";
	var data = "process=getItemsRelated&itemId=" + itemId;
	var request = YAHOO.util.Connect.asyncRequest('POST', url, showItemsRelated_callback, data);
}

YAHOO.util.Event.onContentReady('butItemsRelatedContainer', function() {
	var buttonMenu = [ { text: "Add Related Item", onclick: { fn: addItemRelated } }, 
	      			   { text: "Remove Related Item", onclick: { fn: removeItemsRelated } }
	      	         ];
    var menu = new YAHOO.widget.Button({ type: "menu", 
                                		 label: "Options", 
                                		 name: "itemsRelated",
                                		 menu: buttonMenu, 
                                		 disabled: true,
                                		 container: "butItemsRelatedContainer" });
	if (siteProfileClassDefault && siteCurrencyClassDefault) {
		menu.set('disabled', false);
	}
		
} );
	
YAHOO.util.Event.onContentReady('itemsRelatedContainer', function() {
	paintItemsRelated();
} );

/*
 * Items upsell processing
 */
var jc_item_search_panel = null;
var addItemUpSell_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to add up-sell item");
      return false;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    paintItemsUpSell();
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to add up-sell item");
  }
};
function jc_item_upsell_search_client_callback(value) {
	jc_item_search_panel.hide();
   
	var jsonObject = eval('(' + value + ')');
   	for (var i = 0; i < jsonObject.items.length; i++) {
		var item = jsonObject.items[i];
		var url = "/${adminBean.contextPath}/admin/item/itemMaint.do";
		var data = "process=addItemUpSell&itemId=" + itemId + "&itemUpSellId=" + item.itemId;
		var request = YAHOO.util.Connect.asyncRequest('POST', url, addItemUpSell_callback, data);
   	}
	return false;
}

function addItemUpSell() {
	jc_item_search_show(jc_item_upsell_search_client_callback);
}

var jc_itemsUpSell_remove_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to remove up-sell items");
      return false;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    paintItemsUpSell();
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to remove up-sell items");
  }
};

function removeItemsUpSell() {
	var itemId = '${itemMaintForm.itemId}';
	var url = "/${adminBean.contextPath}/admin/item/itemMaint.do";
	var data = "process=removeItemsUpSell&itemId=" + itemId;
	
	var e = document.getElementById("itemsUpSell");
	var checkboxes = new Array();
	jc_traverse_element(e, checkboxes, 'input', 'itemUpSellIds.*');

	var count = 0;
	for (var i = 0; i < checkboxes.length; i++) {
	  if (!checkboxes[i].checked) {
	    continue;
	  }
	  data += "&itemUpSellIds=" + checkboxes[i].value;
	}
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_itemsUpSell_remove_callback, data);
	return false;
}
 
var showItemsUpSell_callback =
{
 	success: function(o) {
 	    if (!isJsonResponseValid(o.responseText)) {
 			jc_panel_show_error("Unexcepted Error: Unable to extract up-sell items");
 			return false;
 	    }
 	    var jsonObject = getJsonObject(o.responseText);
 	    var node = document.getElementById("itemsUpSellContainer");
 	    node.innerHTML = '';
 	    var table = document.createElement('table');
 	    table.setAttribute('id', 'itemsUpSell');
 	    table.setAttribute('cellpadding', '5');
 	    table.className = 'jc_nobordered_table';
 	    table.setAttribute('width', '400');
 	    var body = document.createElement('tbody');
 	    table.appendChild(body);
 	    if (jsonObject.items.length > 0) {
 		    var row = document.createElement('tr');
 		    body.appendChild(row);
 		    var col = document.createElement('td');
 		    col.setAttribute('width', '50');
 		    col.appendChild(document.createTextNode(''));
			row.appendChild(col);
 		    col = document.createElement('td');
 		    col.setAttribute('width', '100');
 		    col.className = 'jc_input_label';
 		    col.appendChild(document.createTextNode('Item Number'));
			row.appendChild(col);
 		    col = document.createElement('td');
 		    col.className = 'jc_input_label';
 		    col.appendChild(document.createTextNode('Short Description'));
			row.appendChild(col);
 	    }
 	   	for (var i = 0; i < jsonObject.items.length; i++) {
 		    var item = jsonObject.items[i];
 		    var row = document.createElement('tr');
 		    body.appendChild(row);
 		    col = document.createElement('td');
 		    row.appendChild(col);
 		    var input = document.createElement('input');
 		    input.setAttribute('type', 'checkbox');
 		    input.setAttribute('name', 'itemUpSellIds');
 		    input.setAttribute('value', item.itemId);
 		 	col.appendChild(input);
 		    col = document.createElement('td');
 		    col.appendChild(document.createTextNode(item.itemNum));
			row.appendChild(col);
 		    col = document.createElement('td');
 		    col.appendChild(document.createTextNode(item.itemShortDesc));
			row.appendChild(col);
 		}
 		node.appendChild(table);
   },
   failure: function(o) {
       jc_panel_show_error("Unexcepted Error: Unable to extract up-sell items");
   }
};
 
function paintItemsUpSell() {
	var url = "/${adminBean.contextPath}/admin/item/itemMaint.do";
	var data = "process=getItemsUpSell&itemId=" + itemId;
	var request = YAHOO.util.Connect.asyncRequest('POST', url, showItemsUpSell_callback, data);
}

YAHOO.util.Event.onContentReady('butItemsUpSellContainer', function() {
	var buttonMenu = [ { text: "Add Up-sell Item", onclick: { fn: addItemUpSell } }, 
	      			   { text: "Remove Up-sell Item", onclick: { fn: removeItemsUpSell } }
	      	         ];
    var menu = new YAHOO.widget.Button({ type: "menu", 
                                		 label: "Options", 
                                		 name: "itemsUpSell",
                                		 menu: buttonMenu,
                                		 disabled: true,
                                		 container: "butItemsUpSellContainer" });
	if (siteProfileClassDefault && siteCurrencyClassDefault) {
		menu.set('disabled', false);
	}
} );
	
YAHOO.util.Event.onContentReady('itemsUpSellContainer', function() {
	paintItemsUpSell();
} );

/*
 * Items cross-sell processing
 */
var jc_item_search_panel = null;
var addItemCrossSell_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to add cross-sell item");
      return false;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    paintItemsCrossSell();
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to add cross-sell item");
  }
};
function jc_item_crossSell_search_client_callback(value) {
	jc_item_search_panel.hide();
   
	var jsonObject = eval('(' + value + ')');
   	for (var i = 0; i < jsonObject.items.length; i++) {
		var item = jsonObject.items[i];
		var url = "/${adminBean.contextPath}/admin/item/itemMaint.do";
		var data = "process=addItemCrossSell&itemId=" + itemId + "&itemCrossSellId=" + item.itemId;
		var request = YAHOO.util.Connect.asyncRequest('POST', url, addItemCrossSell_callback, data);
   	}
	return false;
}

function addItemCrossSell() {
	jc_item_search_show(jc_item_crossSell_search_client_callback);
}

var jc_itemsCrossSell_remove_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to remove cross-sell items");
      return false;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    paintItemsCrossSell();
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to remove cross-sell items");
  }
};

function removeItemsCrossSell() {
	var itemId = '${itemMaintForm.itemId}';
	var url = "/${adminBean.contextPath}/admin/item/itemMaint.do";
	var data = "process=removeItemsCrossSell&itemId=" + itemId;
	
	var e = document.getElementById("itemsCrossSell");
	var checkboxes = new Array();
	jc_traverse_element(e, checkboxes, 'input', 'itemCrossSellIds.*');

	var count = 0;
	for (var i = 0; i < checkboxes.length; i++) {
	  if (!checkboxes[i].checked) {
	    continue;
	  }
	  data += "&itemCrossSellIds=" + checkboxes[i].value;
	}
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_itemsCrossSell_remove_callback, data);
	return false;
}
 
var showItemsCrossSell_callback =
{
 	success: function(o) {
 	    if (!isJsonResponseValid(o.responseText)) {
 			jc_panel_show_error("Unexcepted Error: Unable to extract cross-sell items");
 			return false;
 	    }
 	    var jsonObject = getJsonObject(o.responseText);
 	    var node = document.getElementById("itemsCrossSellContainer");
 	    node.innerHTML = '';
 	    var table = document.createElement('table');
 	    table.setAttribute('id', 'itemsCrossSell');
 	    table.setAttribute('cellpadding', '5');
 	    table.className = 'jc_nobordered_table';
 	    table.setAttribute('width', '400');
 	    var body = document.createElement('tbody');
 	    table.appendChild(body);
 	    if (jsonObject.items.length > 0) {
 		    var row = document.createElement('tr');
 		    body.appendChild(row);
 		    var col = document.createElement('td');
 		    col.setAttribute('width', '50');
 		    col.appendChild(document.createTextNode(''));
			row.appendChild(col);
 		    col = document.createElement('td');
 		    col.setAttribute('width', '100');
 		    col.className = 'jc_input_label';
 		    col.appendChild(document.createTextNode('Item Number'));
			row.appendChild(col);
 		    col = document.createElement('td');
 		    col.className = 'jc_input_label';
 		    col.appendChild(document.createTextNode('Short Description'));
			row.appendChild(col);
 	    }
 	   	for (var i = 0; i < jsonObject.items.length; i++) {
 		    var item = jsonObject.items[i];
 		    var row = document.createElement('tr');
 		    body.appendChild(row);
 		    col = document.createElement('td');
 		    row.appendChild(col);
 		    var input = document.createElement('input');
 		    input.setAttribute('type', 'checkbox');
 		    input.setAttribute('name', 'itemCrossSellIds');
 		    input.setAttribute('value', item.itemId);
 		 	col.appendChild(input);
 		    col = document.createElement('td');
 		    col.appendChild(document.createTextNode(item.itemNum));
			row.appendChild(col);
 		    col = document.createElement('td');
 		    col.appendChild(document.createTextNode(item.itemShortDesc));
			row.appendChild(col);
 		}
 		node.appendChild(table);
   },
   failure: function(o) {
       jc_panel_show_error("Unexcepted Error: Unable to extract cross-sell items");
   }
};
 
function paintItemsCrossSell() {
	var url = "/${adminBean.contextPath}/admin/item/itemMaint.do";
	var data = "process=getItemsCrossSell&itemId=" + itemId;
	var request = YAHOO.util.Connect.asyncRequest('POST', url, showItemsCrossSell_callback, data);
}

YAHOO.util.Event.onContentReady('butItemsCrossSellContainer', function() {
	var buttonMenu = [ { text: "Add Cross-sell Item", onclick: { fn: addItemCrossSell } }, 
	      			   { text: "Remove Cross-sell Item", onclick: { fn: removeItemsCrossSell } }
	      	         ];
    var menu = new YAHOO.widget.Button({ type: "menu", 
                                		 label: "Options", 
                                		 name: "itemsCrossSell",
                                		 menu: buttonMenu, 
                                		 disabled: true,
                                		 container: "butItemsCrossSellContainer" });
	if (siteProfileClassDefault && siteCurrencyClassDefault) {
		menu.set('disabled', false);
	}
} );
	
YAHOO.util.Event.onContentReady('itemsCrossSellContainer', function() {
	paintItemsCrossSell();
} );
function overrideAttributeLanguage(container, control)  {
	var e = document.getElementById("tabCustomAttributes");
	var inputBoxes = new Array();
	if (!control.checked) {
	  jc_panel_confirm('Confirm to use default language?', function(confirm) {
	    if (confirm) {
			jc_traverse_element(e, inputBoxes, 'input', 'itemAttributeDetail.*itemAttribDetailValue$');
			for (var i = 0; i < inputBoxes.length; i++) {
				var masterName = inputBoxes[i].name;
				var sourceName = masterName + 'Lang_tmp';
				var sourceObject = document.itemMaintForm.elements[sourceName];
				if (sourceObject == null) {
					continue;
				}
				var masterObject = document.itemMaintForm.elements[masterName];
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
		jc_traverse_element(e, inputBoxes, 'input', 'itemAttributeDetail.*Lang_tmp');
		for (var i = 0; i < inputBoxes.length; i++) {
			inputBoxes[i].disabled = false;
		}
	}
}

var getNewCustomAttribuites_callback = 
{
	success: function(o) {
		if (!isJsonResponseValid(o.responseText)) {
		    jc_panel_show_error("Unexcepted Error: Unable to get custom attributes");
		    return false;
		}
		var container = document.getElementById('tabCustomAttributes');
		container.innerHTML = '';

        var table = document.createElement('table');
    	table.setAttribute('width', '400');
        table.setAttribute('cellpadding', '5');
        container.appendChild(table);
        var body = document.createElement('tbody');
        table.appendChild(body);

        var jsonObject = getJsonObject(o.responseText);
	    for (var i = 0; i < jsonObject.customAttributeDetails.length; i++) {
	        var customAttributeDetail = jsonObject.customAttributeDetails[i];
	        
	        var row = document.createElement('tr');
	        body.appendChild(row);

	        col = document.createElement('td');
	        row.appendChild(col);
			col.setAttribute('width', '40%');
			col.className = 'jc_input_label';
			col.appendChild(document.createTextNode(customAttributeDetail.customAttribName));

	        var hidden = document.createElement('input');
	        hidden.setAttribute('type', 'hidden');
	        hidden.setAttribute('name', 'itemAttributeDetail[' + i + '].itemAttribDetailId');
	        hidden.setAttribute('value', '');
	        col.appendChild(hidden);

	        hidden = document.createElement('input');
	        hidden.setAttribute('type', 'hidden');
	        hidden.setAttribute('name', 'itemAttributeDetail[' + i + '].customAttribDetailId');
	        hidden.setAttribute('value', customAttributeDetail.customAttribDetailId);
	        col.appendChild(hidden);
	        
	        hidden = document.createElement('input');
	        hidden.setAttribute('type', 'hidden');
	        hidden.setAttribute('name', 'itemAttributeDetail[' + i + '].customAttribName');
	        hidden.setAttribute('value', customAttributeDetail.customAttribName);
	        col.appendChild(hidden);

	        col = document.createElement('td');
	        row.appendChild(col);
	        
	        var input = document.createElement('input');
	        input.className = 'jc_input_control';
	        input.setAttribute('type', 'text');
	        input.setAttribute('name', 'itemAttributeDetail[' + i + '].itemAttribDetailValue');
	        input.setAttribute('value', '');
	        col.appendChild(input);
	    }
	},
	failure: function(o) {
	}
};

function getNewCustomAttributes(customAttribGroupId) {
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/item/itemMaint.do?process=getCustomAttributes&customAttribGroupId=" + customAttribGroupId;
	var request = YAHOO.util.Connect.asyncRequest('GET', url, getNewCustomAttribuites_callback);
}

YAHOO.util.Event.onContentReady('customAttribGroupId', function() {
	var object = document.getElementById('customAttribGroupId');
	object.onchange = function() {
		if (mode != 'C') {
			var index = document.itemMaintForm.customAttribGroupId.selectedIndex;
			var customAttriGroupId = document.itemMaintForm.customAttribGroupId.options[index].value;
			if (customAttriGroupId != '') {
				getNewCustomAttributes(customAttriGroupId);
			}
			else {
				var container = document.getElementById('tabCustomAttributes');
				container.innerHTML = '';
			}
		}
	};
} );

/*
 * Calendar processing
 */
if (siteProfileClassDefault) {
	initCalendar('calItemPublishOnContainer', 'itemPublishOn', 'calItemPublishOn');
	initCalendar('calItemExpireOnContainer', 'itemExpireOn', 'calItemExpireOn');
	initCalendar('calItemSpecPublishOnContainer', 'itemSpecPublishOn', 'calItemSpecPublishOn');
	initCalendar('calItemSpecExpireOnContainer', 'itemSpecExpireOn', 'calItemSpecExpireOn');
}

</script>
<div id='container' class="yui-skin-sam"></div>
<html:form method="post" action="/admin/item/itemMaint" styleClass="yui-skin-sam"> 
<html:hidden property="itemId"/>
<html:hidden property="process" value=""/>
<html:hidden property="mode"/>
<html:hidden property="shareInventory"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - <a href="/<c:out value='${adminBean.contextPath}'/>/admin/item/itemListing.do?process=back">Item 
      Listing</a> - Item Maintenance</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr>
    <td valign="top" width="400">
      <div class="jc_detail_panel_header" style="width: 400px">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">General</span></td>
            <td>
              <div align="right">
              &nbsp;
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div class="jc_detail_panel">
        <table width="400" border="0" cellspacing="0" cellpadding="5" bordercolor="#CCCCCC" class="jc_nobordered_table">
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label" width="40%">Item Type</td>
            <td class="jc_input_control" width="60%">
                <c:choose>
                  <c:when test="${itemMaintForm.mode == 'C'}">
					<lang:select property="itemTypeCd" styleClass="tableContent"> 
		              <lang:option value="01">Regular</lang:option> 
		              <lang:option value="02">Template</lang:option> 
		              <lang:option value="04">Static Bundle</lang:option> 
		              <lang:option value="05">Recommended Bundle</lang:option> 
				    </lang:select>
				  </c:when>
				  <c:otherwise>
				    <html:hidden property="itemTypeCd"/>
				    <html:hidden property="itemTypeDesc"/>
				    ${itemMaintForm.itemTypeDesc}
				  </c:otherwise>
			    </c:choose>
            </td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">Custom Attribute Group</td>
            <td class="jc_input_control">
			  <c:choose>
                <c:when test="${itemMaintForm.mode == 'C'}">
	              <lang:select styleId="customAttribGroupId" property="customAttribGroupId"> 
	                <lang:optionsCollection property="customAttributeGroups" label="label"/>
	              </lang:select>
			    </c:when>
			    <c:otherwise>
			      <html:hidden property="customAttribGroupId"/>
			      <html:hidden property="customAttribGroupName"/>
			      ${itemMaintForm.customAttribGroupName}
			    </c:otherwise>
		      </c:choose>
              <span class="jc_input_error">
                <logic:messagesPresent property="customAttribGroupId" message="true"> 
              	  <br><html:messages property="customAttribGroupId" id="errorText" message="true"> 
              	  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
              </span>
            </td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">Sellable</td>
            <td class="jc_input_label">
              <lang:checkbox property="itemSellable" styleClass="jc_input_control"/> 
            </td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label" width="40%">Publish On</td>
            <td class="jc_input_label" width="60%"> 
              <lang:text property="itemPublishOn" styleClass="jc_input_control"/>
              <c:if test="${itemMaintForm.siteProfileClassDefault}">
              <img id="calItemPublishOn" src="../images/icon/image_plus.gif" border="0">
              <div id="calItemPublishOnContainer" style="position: absolute; display:none; z-index: 9999"></div> 
              </c:if>
              <span class="jc_input_error"> <logic:messagesPresent property="itemPublishOn" message="true"> 
              <br><html:messages property="itemPublishOn" id="errorText" message="true"> 
              <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
              </span>
            </td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">Expire On</td>
            <td>
              <lang:text property="itemExpireOn" styleClass="jc_input_control"/> 
              <c:if test="${itemMaintForm.siteProfileClassDefault}">
              <img id="calItemExpireOn" src="../images/icon/image_plus.gif" border="0">
              <div id="calItemExpireOnContainer" style="position: absolute; display:none; z-index: 9999"></div> 
              </c:if>
              <span class="jc_input_error"> <logic:messagesPresent property="itemExpireOn" message="true"> 
              <br><html:messages property="itemExpireOn" id="errorText" message="true"> 
              <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
              </span>
            </td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">Shipping Type</td>
            <td class="jc_input_label">
              <lang:select property="shippingTypeId"> 
                <lang:optionsCollection property="shippingTypes" label="label"/>
              </lang:select>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">Product Class</td>
            <td class="jc_input_label">
              <lang:select property="productClassId"> 
                <lang:optionsCollection property="productClasses" label="label"/>
              </lang:select>
          </tr>
          <layout:mode value="edit">
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">Inventory</td>
            <td>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="100">
                    <div id="itemQty">
                    ${itemMaintForm.itemQty}
                    </div>
                  </td>
                  <td>
                    <layout:mode value="edit">
                    <div align="left">
                    <button type="button" id="butAdjInv" name="butAdjInv" value="Adjust">Adjust</button>
                    </div>
                    </layout:mode>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">Booked Quantity</td>
            <td>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="100">
                    <div id="itemBookedQty">
                    ${itemMaintForm.itemBookedQty}
                    </div>
                  </td>
                  <td>
                    <layout:mode value="edit">
                    <div align="left">
                    <button type="button" id="butAdjBookInv" name="butAdjBookInv" value="Adjust">Adjust</button>
                    </div>
                    </layout:mode>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">Hit Counter</td>
            <td>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="100">
                    <span id="hitCounter">
                      <c:out value='${itemMaintForm.itemHitCounter}'/>
                    </span>
                  </td>
                  <td>
                    <layout:mode value="edit">
                    <div align="left" nowrap>
                    <button type="button" id="butResetCounter" name="butResetCounter" value="Reset Counter">Reset Counter</button>
                    </div>
                    </layout:mode>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">Rating</td>
            <td>
            <c:out value='${itemMaintForm.itemRating}'/>
            </td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">Number of Rating</td>
            <td>
            <c:out value='${itemMaintForm.itemRatingCount}'/>
            </td>
          </tr>
          </layout:mode>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">Published</td>
            <td class="jc_input_label"> <lang:checkbox property="published" value="Y" styleClass="jc_input_control"/> 
            </td>
          </tr>
        </table>
      </div>
      <div class="jc_detail_panel_header" style="width: 400px">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">Pricing</span></td>
            <td>
              <div align="right">
              &nbsp;
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div class="jc_detail_panel">
        <table width="400" border="0" cellspacing="0" cellpadding="5" bordercolor="#CCCCCC" class="jc_nobordered_table">
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label" width="40%">Cost</td>
            <td class="jc_input_label" width="60%"> <lang:text property="itemCost" size="8" styleClass="jc_input_control"/> 
              <span class="jc_input_error"> <logic:messagesPresent property="itemCost" message="true"> 
              <html:messages property="itemCost" id="errorText" message="true"> 
              <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
              </span> </td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label"><lang:checkboxSwitch name="itemMaintForm" property="itemPriceCurrFlag">Override currency</lang:checkboxSwitch></td>
            <td class="jc_input_label"></td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">
              Price
            </td>
            <td class="jc_input_label"> <lang:text property="itemPrice" size="8" styleClass="jc_input_control"/> 
              <span class="jc_input_error"> <logic:messagesPresent property="itemPrice" message="true"> 
              <html:messages property="itemPrice" id="errorText" message="true"> 
              <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
              </span> </td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label"><lang:checkboxSwitch name="itemMaintForm" property="itemSpecPriceCurrFlag" ignoreEmpty="true">Override currency</lang:checkboxSwitch></td>
            <td class="jc_input_label"></td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">
              Special Price</td>
            <td class="jc_input_label"> <lang:text property="itemSpecPrice" size="8" styleClass="jc_input_control"/> 
              <span class="jc_input_error"> <logic:messagesPresent property="itemSpecPrice" message="true"> 
              <html:messages property="itemSpecPrice" id="errorText" message="true"> 
              <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
              </span> </td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">Special Publish On</td>
            <td class="jc_input_label">
              <lang:text property="itemSpecPublishOn" styleClass="jc_input_control"/>
              <c:if test="${itemMaintForm.siteProfileClassDefault}">
              <img id="calItemSpecPublishOn" src="../images/icon/image_plus.gif" border="0">
              <div id="calItemSpecPublishOnContainer" style="position: absolute; display:none; z-index: 99999"></div> 
              </c:if>
              <span class="jc_input_error"> <logic:messagesPresent property="itemSpecPublishOn" message="true"> 
              <html:messages property="itemSpecPublishOn" id="errorText" message="true"> 
              <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
              </span> </td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">Special Expire On</td>
            <td class="jc_input_label">
              <lang:text property="itemSpecExpireOn" styleClass="jc_input_control"/>
              <c:if test="${itemMaintForm.siteProfileClassDefault}">
              <img id="calItemSpecExpireOn" src="../images/icon/image_plus.gif" border="0">
              <div id="calItemSpecExpireOnContainer" style="position: absolute; display:none; z-index: 99999"></div>
              </c:if>
              <span class="jc_input_error"> <logic:messagesPresent property="itemSpecExpireOn" message="true"> 
              <html:messages property="itemSpecExpireOn" id="errorText" message="true"> 
              <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
              </span> </td>
          </tr>
        </table>
      </div>
      <layout:mode value="edit">
	  <div class="jc_detail_panel_header" style="width: 400px">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td><span class="jc_input_label">Tier Pricing</span></td>
            <td>
              <div align="right">
                <table width="0" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                      <div id="tabTierPricing_show">
                        <a href="javascript:void(0);" onclick="showId('tabTierPricing');hideId('tabTierPricing_show');showId('tabTierPricing_hide')">
                          <img src="../images/icon/image_plus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                    <td>
                      <div id="tabTierPricing_hide" style="display:none;">
                        <a href="javascript:void(0);" onclick="hideId('tabTierPricing');showId('tabTierPricing_show');hideId('tabTierPricing_hide')">
                          <img src="../images/icon/image_minus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
	    </table>
	  </div>
	  <div id="tabTierPricing" class="jc_detail_panel" style="display:none;">
	    <c:forEach var="itemTierPrice" items="${itemMaintForm.itemTierPrices}">
	    <div id="itemTierPriceContainer_${itemTierPrice.itemTierPriceId}">
	    </div>
	    <script>
	    paintItemTierPrice(${itemTierPrice.itemTierPriceId});
	    </script>
	    </c:forEach>
	    <div align="right" id="butItemTierPriceAddContainer">
	    </div>
	  </div>
	  
	  <div class="jc_detail_panel_header" style="width: 400px">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td><span class="jc_input_label">Custom Attributes</span></td>
            <td>
              <div align="right">
                <table width="0" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                      <div id="tabCustomAttributes_show">
                        <a href="javascript:void(0);" onclick="showId('tabCustomAttributes');hideId('tabCustomAttributes_show');showId('tabCustomAttributes_hide')">
                          <img src="../images/icon/image_plus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                    <td>
                      <div id="tabCustomAttributes_hide" style="display:none;">
                        <a href="javascript:void(0);" onclick="hideId('tabCustomAttributes');showId('tabCustomAttributes_show');hideId('tabCustomAttributes_hide')">
                          <img src="../images/icon/image_minus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
	    </table>
	  </div>
	  <div id="tabCustomAttributes" class="jc_detail_panel" style="display:none;">
        <table width="400" border="0" cellspacing="0" cellpadding="5">
          <c:if test="${!itemMaintForm.siteProfileClassDefault}">
          <tr>
            <td colspan="2" class="jc_input_label">
              Attributes - Override language
              <html:checkbox property="itemAttribDetailValueLangFlag" onclick="return overrideAttributeLanguage('tabCustomAttributes', this)"/>
            </td>
          </tr>
          </c:if>
          <c:forEach var="itemAttributeDetail" items="${itemMaintForm.itemAttributeDetails}">
          <tr> 
            <td class="jc_input_label" width="40%">
              ${itemAttributeDetail.customAttribName}
              <html:hidden indexed="true" name="itemAttributeDetail" property="itemAttribDetailId" value="${itemAttributeDetail.itemAttribDetailId}"/>
              <html:hidden indexed="true" name="itemAttributeDetail" property="customAttribDetailId" value="${itemAttributeDetail.customAttribDetailId}"/>
              <html:hidden indexed="true" name="itemAttributeDetail" property="customAttribId" value="${itemAttributeDetail.customAttribId}"/>
              <html:hidden indexed="true" name="itemAttributeDetail" property="customAttribName" value="${itemAttributeDetail.customAttribName}"/>
              <html:hidden indexed="true" name="itemAttributeDetail" property="customAttribTypeCode" value="${itemAttributeDetail.customAttribTypeCode}"/>
            </td>
            <td width="60%">
              <c:choose>
				<c:when test="${itemAttributeDetail.customAttribTypeCode == '1'}">
                  <html:hidden indexed="true" name="itemAttributeDetail" property="itemAttribDetailValue" value="${itemAttributeDetail.itemAttribDetailValue}"/>
                  ${itemAttributeDetail.itemAttribDetailValue}
				</c:when>
				<c:when test="${itemAttributeDetail.customAttribTypeCode == '2'}">
	              <lang:select indexed="true" name="itemAttributeDetail" property="customAttribOptionId"> 
	                <lang:optionsCollection name="itemAttributeDetail" property="customAttribOptions" label="label"/>
	              </lang:select>
				</c:when>
				<c:when test="${itemAttributeDetail.customAttribTypeCode == '3'}">
                  <lang:text indexed="true" name="itemAttributeDetail" property="itemAttribDetailValue" styleClass="jc_input_control"/>
				</c:when>
				<c:when test="${itemAttributeDetail.customAttribTypeCode == '4'}">
				  <c:choose>
					<c:when test="${itemMaintForm.itemTypeCd == '02'}">
	                  <html:hidden indexed="true" name="itemAttributeDetail" property="itemAttribDetailValue" value="${itemAttributeDetail.itemAttribDetailValue}"/>
	                  ${itemAttributeDetail.itemAttribDetailValue}
				    </c:when>
				    <c:otherwise>
		              <lang:select indexed="true" name="itemAttributeDetail" property="customAttribOptionId"> 
		                <lang:optionsCollection name="itemAttributeDetail" property="customAttribOptions" label="label"/>
		              </lang:select>
				    </c:otherwise>
				  </c:choose>
				</c:when>
			  </c:choose>
            </td>
          </tr>
          </c:forEach>
        </table>
	  </div>
	  
	  <c:if test="${!itemMaintForm.shareInventory}">
	  <div class="jc_detail_panel_header" style="width: 400px">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td><span class="jc_input_label">Sub-sites</span></td>
            <td>
              <div align="right">
                <table width="0" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                      <div id="tabSubSites_show">
                        <a href="javascript:void(0);" onclick="showId('tabSubSites');hideId('tabSubSites_show');showId('tabSubSites_hide')">
                          <img src="../images/icon/image_plus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                    <td>
                      <div id="tabSubSites_hide" style="display:none;">
                        <a href="javascript:void(0);" onclick="hideId('tabSubSites');showId('tabSubSites_show');hideId('tabSubSites_hide')">
                          <img src="../images/icon/image_minus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
	    </table>
	  </div>
	  <div id="tabSubSites" class="jc_detail_panel" style="display:none;">
        <table width="400" border="0" cellspacing="0" cellpadding="5">
          <tr>
            <td class="jc_input_label">
            </td>
            <td class="jc_input_label">
            	Sub-site
            </td>
          </tr>
          <c:forEach var="itemSiteDomain" items="${itemMaintForm.itemSiteDomains}">
          <tr> 
            <td class="jc_input_label" width="100">
              <html:hidden indexed="true" name="itemSiteDomain" property="siteDomainId" value="${itemSiteDomain.siteDomainId}"/>
              <lang:checkbox indexed="true" name="itemSiteDomain" property="selected" value="Y" styleClass="jc_input_control"/> 
            </td>
            <td>
              <html:hidden indexed="true" name="itemSiteDomain" property="siteName" value="${itemSiteDomain.siteName}"/>
			  ${itemSiteDomain.siteName}
            </td>
          </tr>
          </c:forEach>
        </table>
	  </div>
	  </c:if>
	  
	  <c:if test="${itemMaintForm.itemTypeCd == '02'}">
	  <div class="jc_detail_panel_header" style="width: 400px">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td><span class="jc_input_label">Skus</span></td>
            <td>
              <div align="right">
                <table width="0" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                      <div id="tabItemSkus_show">
                        <a href="javascript:void(0);" onclick="showId('tabItemSkus');hideId('tabItemSkus_show');showId('tabItemSkus_hide')">
                          <img src="../images/icon/image_plus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                    <td>
                      <div id="tabItemSkus_hide" style="display:none;">
                        <a href="javascript:void(0);" onclick="hideId('tabItemSkus');showId('tabItemSkus_show');hideId('tabItemSkus_hide')">
                          <img src="../images/icon/image_minus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
	    </table>
	  </div>
	  <div id="tabItemSkus" class="jc_detail_panel" style="display:none;">
        <table width="400" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td> 
              <div align="right" id="butItemSkusContainer"></div>
            </td>
          </tr>
          <tr> 
            <td> &nbsp;</td>
          </tr>
        </table>
        <div id="itemSkusContainer">
	    </div>
	  </div>
	  </c:if>
	  
	  <c:if test="${itemMaintForm.itemTypeCd == '04' || itemMaintForm.itemTypeCd == '05'}">
	  <div class="jc_detail_panel_header" style="width: 400px">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td><span class="jc_input_label">Bundle</span></td>
            <td>
              <div align="right">
                <table width="0" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                      <div id="tabItemsBundle_show">
                        <a href="javascript:void(0);" onclick="showId('tabItemsBundle');hideId('tabItemsBundle_show');showId('tabItemsBundle_hide')">
                          <img src="../images/icon/image_plus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                    <td>
                      <div id="tabItemsBundle_hide" style="display:none;">
                        <a href="javascript:void(0);" onclick="hideId('tabItemsBundle');showId('tabItemsBundle_show');hideId('tabItemsBundle_hide')">
                          <img src="../images/icon/image_minus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
	    </table>
	  </div>
	  <div id="tabItemsBundle" class="jc_detail_panel" style="display:none;">
        <table width="400" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td> 
              <div align="right" id="butItemsBundleContainer"></div>
            </td>
          </tr>
          <tr> 
            <td> &nbsp;</td>
          </tr>
        </table>
        <div align="right" id="itemsBundleContainer">
	    </div>
	  </div>
	  </c:if>
	  
	  <div class="jc_detail_panel_header" style="width: 400px">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td><span class="jc_input_label">Related Items</span></td>
            <td>
              <div align="right">
                <table width="0" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                      <div id="tabItemsRelated_show">
                        <a href="javascript:void(0);" onclick="showId('tabItemsRelated');hideId('tabItemsRelated_show');showId('tabItemsRelated_hide')">
                          <img src="../images/icon/image_plus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                    <td>
                      <div id="tabItemsRelated_hide" style="display:none;">
                        <a href="javascript:void(0);" onclick="hideId('tabItemsRelated');showId('tabItemsRelated_show');hideId('tabItemsRelated_hide')">
                          <img src="../images/icon/image_minus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
	    </table>
	  </div>
	  <div id="tabItemsRelated" class="jc_detail_panel" style="display:none;">
        <table width="400" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td> 
              <div align="right" id="butItemsRelatedContainer"></div>
            </td>
          </tr>
          <tr> 
            <td> &nbsp;</td>
          </tr>
        </table>
        <div align="left" id="itemsRelatedContainer">
	    </div>
	  </div>
	  
	  <div class="jc_detail_panel_header" style="width: 400px">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td><span class="jc_input_label">Up-sell Items</span></td>
            <td>
              <div align="right">
                <table width="0" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                      <div id="tabItemsUpSell_show">
                        <a href="javascript:void(0);" onclick="showId('tabItemsUpSell');hideId('tabItemsUpSell_show');showId('tabItemsUpSell_hide')">
                          <img src="../images/icon/image_plus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                    <td>
                      <div id="tabItemsUpSell_hide" style="display:none;">
                        <a href="javascript:void(0);" onclick="hideId('tabItemsUpSell');showId('tabItemsUpSell_show');hideId('tabItemsUpSell_hide')">
                          <img src="../images/icon/image_minus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
	    </table>
	  </div>
	  <div id="tabItemsUpSell" class="jc_detail_panel" style="display:none;">
        <table width="400" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td> 
              <div align="right" id="butItemsUpSellContainer"></div>
            </td>
          </tr>
          <tr> 
            <td> &nbsp;</td>
          </tr>
        </table>
        <div align="left" id="itemsUpSellContainer">
	    </div>
	  </div>

	  <div class="jc_detail_panel_header" style="width: 400px">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td><span class="jc_input_label">Cross-sell Items</span></td>
            <td>
              <div align="right">
                <table width="0" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                      <div id="tabItemsCrossSell_show">
                        <a href="javascript:void(0);" onclick="showId('tabItemsCrossSell');hideId('tabItemsCrossSell_show');showId('tabItemsCrossSell_hide')">
                          <img src="../images/icon/image_plus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                    <td>
                      <div id="tabItemsCrossSell_hide" style="display:none;">
                        <a href="javascript:void(0);" onclick="hideId('tabItemsCrossSell');showId('tabItemsCrossSell_show');hideId('tabItemsCrossSell_hide')">
                          <img src="../images/icon/image_minus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
	    </table>
	  </div>
	  <div id="tabItemsCrossSell" class="jc_detail_panel" style="display:none;">
        <table width="400" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td> 
              <div align=right id="butItemsCrossSellContainer"></div>
            </td>
          </tr>
          <tr> 
            <td> &nbsp;</td>
          </tr>
        </table>
        <div align="left" id="itemsCrossSellContainer">
	    </div>
	  </div>
	  
      <div class="jc_detail_panel_header" style="width: 400px">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">Images</span></td>
            <td>
              <div align="right">
                <table width="0" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                      <div id="tabImage_show">
                        <a href="javascript:void(0);" onclick="showId('tabImage');hideId('tabImage_show');showId('tabImage_hide')">
                          <img src="../images/icon/image_plus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                    <td>
                      <div id="tabImage_hide" style="display:none;">
                        <a href="javascript:void(0);" onclick="hideId('tabImage');showId('tabImage_show');hideId('tabImage_hide')">
                          <img src="../images/icon/image_minus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div id="tabImage" class="jc_detail_panel" style="display:none;">
        <table width="400" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td>
                    <c:if test="${!itemMaintForm.siteProfileClassDefault}">
                      Override default
                      <html:checkbox onclick="return overrideImageLanguage(this);" property="itemImageOverride"/>
                    </c:if>
                  </td>
                  <td>
                    <div id="imageOptionContainer" align="right"></div>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr> 
            <td> &nbsp;</td>
          </tr>
        </table>
        <table width="400" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td> 
              <div class="jc_image_scroll" style="width: 396px"> 
                <table id="jc_images_table" width="100%" border="0" cellspacing="0" cellpadding="5">

                </table>
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div class="jc_detail_panel_header" style="width: 400px">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">Menus</span></td>
            <td>
              <div align="right">
                <table width="0" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                      <div id="tabMenus_show">
                        <a href="javascript:void(0);" onclick="showId('tabMenus');hideId('tabMenus_show');showId('tabMenus_hide')">
                          <img src="../images/icon/image_plus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                    <td>
                      <div id="tabMenus_hide" style="display:none;">
                        <a href="javascript:void(0);" onclick="hideId('tabMenus');showId('tabMenus_show');hideId('tabMenus_hide')">
                          <img src="../images/icon/image_minus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div id="tabMenus" class="jc_detail_panel" style="display:none;">
        <table width="400" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td> 
              <div align="right" id="butMenusContainer"></div>
            </td>
          </tr>
          <tr> 
            <td> &nbsp;</td>
          </tr>
        </table>
        <table id="menuTable" width="400" border="0" cellspacing="0" cellpadding="2" class="jc_unbordered_table">
          <tr>
            <td></td>
            <td class="jc_input_label">Menu</td>
            <td class="jc_input_label">Sub-site</td>
          </tr>
        </table>
      </div>
      <div class="jc_detail_panel_header" style="width: 400px">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">Categories</span></td>
            <td>
              <div align="right">
                <table width="0" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                      <div id="tabCategory_show">
                        <a href="javascript:void(0);" onclick="showId('tabCategory');hideId('tabCategory_show');showId('tabCategory_hide')">
                       	  <img src="../images/icon/image_plus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                    <td>
                      <div id="tabCategory_hide" style="display:none;">
                        <a href="javascript:void(0);" onclick="hideId('tabCategory');showId('tabCategory_show');hideId('tabCategory_hide')">
                       	  <img src="../images/icon/image_minus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div id="tabCategory" class="jc_detail_panel" style="display:none;">
        <table width="400" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td> 
              <div align="right" id="butCategoryContainer"></div>
            </td>
          </tr>
          <tr> 
            <td> &nbsp;</td>
          </tr>
        </table>
        <table id="categoryTable" width="400" border="0" cellspacing="0" cellpadding="2" class="jc_unbordered_table">
          <tr>
            <td></td>
            <td class="jc_input_label">Category</td>
          </tr>
        </table>
      </div>
      <div class="jc_detail_panel_header" style="width: 400px">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">Comments</span></td>
            <td>
              <div align="right">
                <table width="0" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                      <div id="tabComment_show">
                        <a href="javascript:void(0);" onclick="showComment();">
                          <img src="../images/icon/image_plus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                    <td>
                      <div id="tabComment_hide" style="display:none;">
                        <a href="javascript:void(0);" onclick="hideComment();">
                          <img src="../images/icon/image_minus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div id="tabComment" class="jc_detail_panel" style="display:none; width: 400px">
      </div>
      <div class="jc_detail_panel_header" style="width: 400px">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">Timestamp</span></td>
            <td>
              <div align="right">
                <table width="0" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                      <div id="tabTimestamp_show">
                        <a href="javascript:void(0);" onclick="showId('tabTimestamp');hideId('tabTimestamp_show');showId('tabTimestamp_hide')">
                        	<img src="../images/icon/image_plus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                    <td>
                      <div id="tabTimestamp_hide" style="display:none;">
                        <a href="javascript:void(0);" onclick="hideId('tabTimestamp');showId('tabTimestamp_show');hideId('tabTimestamp_hide')">
                        	<img src="../images/icon/image_minus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div id="tabTimestamp" class="jc_detail_panel" style="display:none;">
        <table width="400" border="0" cellspacing="0" cellpadding="5" bordercolor="#CCCCCC" class="jc_unbordered_table">
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label" width="150">Updated By</td>
            <td class="jc_input_control" width="0"><div id="recUpdateBy"><layout:text layout="false" property="recUpdateBy" mode="I,I,I" styleClass="jc_input_control"/></div>
            </td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">Updated On</td>
            <td class="jc_input_control"><div id="recUpdateDatetime"><layout:text layout="false" property="recUpdateDatetime" mode="I,I,I" styleClass="jc_input_control"/></div>
            </td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">Created By</td>
            <td class="jc_input_label"> <layout:text layout="false" property="recCreateBy" mode="I,I,I" styleClass="jc_input_control"/> 
            </td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">Created On</td>
            <td class="jc_input_label"> <layout:text layout="false" property="recCreateDatetime" mode="I,I,I" styleClass="jc_input_control"/> 
            </td>
          </tr>
        </table>
      </div>
      </layout:mode>
    </td>
    <td width="100%" valign="top"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
        <tr>
          <layout:mode value="edit">
          <td>
            Profile
            <html:select property="siteProfileClassId" onchange="javascript:submitForm('language')"> 
              <html:optionsCollection property="siteProfileClassBeans" value="value" label="label"/> 
            </html:select>
            <button type="button" id="butTranslate" name="butTranslate" value="Translate">Translate</button>
            Currency
            <html:select property="siteCurrencyClassId" onchange="javascript:submitForm('language')"> 
              <html:optionsCollection property="siteCurrencyClassBeans" value="value" label="label"/> 
            </html:select>
          </td>
          </layout:mode>
          <td align="right"> 
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
      <br>
      <span class="jc_input_error">
      <logic:messagesPresent property="error" message="true"> 
      	<html:messages property="error" id="errorText" message="true"> 
        <bean:write name="errorText"/>
        </html:messages> 
      </logic:messagesPresent>
      </span>
      <table width="100%" border="0" cellspacing="0" cellpadding="4" class="borderTable">
        <tr>
          <td colspan="2">
            <span class="jc_input_error">
              <logic:messagesPresent property="inputSkuAttrbute" message="true"> 
            	  <br><html:messages property="inputSkuAttrbute" id="errorText" message="true"> 
            	  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
            </span>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">Item Number</td>
        </tr>
        <tr> 
          <td class="jc_input_control">
            <lang:text property="itemNum" size="20" maxlength="20" styleClass="tableContent"/>
            <span class="jc_input_error">
            <logic:messagesPresent property="itemNum" message="true"> 
            <br><html:messages property="itemNum" id="errorText" message="true"> 
            <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
            </span>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">Item UPC Code</td>
        </tr>
        <tr> 
          <td class="jc_input_control"> <lang:text property="itemUpcCd" size="20" maxlength="20" styleClass="tableContent"/> 
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">Item SKU Code</td>
        </tr>
        <tr> 
          <td class="jc_input_control">
            <lang:text property="itemSkuCd" size="40" maxlength="40" styleClass="tableContent"/>
            <span class="jc_input_error">
            <logic:messagesPresent property="itemSkuCd" message="true"> 
            <br><html:messages property="itemSkuCd" id="errorText" message="true"> 
            <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
            </span>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">
          Short Description
          <lang:checkboxSwitch name="itemMaintForm" property="itemShortDescLangFlag"> - Override language</lang:checkboxSwitch>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_control"> <lang:text property="itemShortDesc" size="80" maxlength="80" styleClass="tableContent"/> 
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">
          Description
          <lang:checkboxEditorSwitch name="itemMaintForm" property="itemDescLangFlag"> - Override language</lang:checkboxEditorSwitch>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_control">
          <lang:editorText name="itemMaintForm" property="itemDesc" height="300" width="100%" toolBarSet="Simple"/>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">
          HTML Title Tag
          <lang:checkboxSwitch name="itemMaintForm" property="pageTitleLangFlag"> - Override language</lang:checkboxSwitch>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_control"> <lang:text maxlength="255" size="120" property="pageTitle" styleClass="tableContent"/> 
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">
          Meta keywords
          <lang:checkboxSwitch name="itemMaintForm" property="metaKeywordsLangFlag"> - Override language</lang:checkboxSwitch>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_control"> <lang:textarea rows="5" cols="51" property="metaKeywords" styleClass="tableContent"/> 
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">
          Meta description
          <lang:checkboxSwitch name="itemMaintForm" property="metaDescriptionLangFlag"> - Override language</lang:checkboxSwitch>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_control"> <lang:textarea rows="5" cols="51" property="metaDescription" styleClass="tableContent"/> 
          </td>
        </tr>
        <tr> 
          <td> </td>
          <td> </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<br>

<%@ include file="/admin/include/menusLookup.jsp" %>
<script type="text/javascript">
YAHOO.util.Event.onContentReady('tabMenus', function() {
	var showMenu_callback =
	{
	  success: function(o) {
	    if (!isJsonResponseValid(o.responseText)) {
	      jc_panel_show_error("Unexcepted Error: unable to show selected menu");
	      return false;
	    }
	    var jsonObject = getJsonObject(o.responseText);
	    jc_menus_show_menus(jsonObject);
	  },
	  failure: function(o) {
	      jc_panel_show_error("Unexcepted Error: unable to show selected menu");
	  }
	};
	var url = "/${adminBean.contextPath}/admin/item/itemMaint.do";
	var data = "process=showMenus&itemId=" + itemId ;
	var request = YAHOO.util.Connect.asyncRequest('POST', url, showMenu_callback, data);
} );

function jc_menus_show_menus(jsonObject) {
  var table = document.getElementById("menuTable");
  var tbody = table.getElementsByTagName("tbody")[0]
  if (!tbody) {
    tbody = document.createElement('tbody');
    table.appendChild(tbody);
  }
  var rows = tbody.childNodes;
  for (var i = table.rows.length - 1; i > 0; i--) {
    table.deleteRow(i);
  }
  
  for (var i = 0; i < jsonObject.menus.length; i++) {
    var row = document.createElement('tr');
    var column;
    column = document.createElement('td');
    var checkbox = document.createElement('input');
    checkbox.setAttribute('type', 'checkbox');
    checkbox.setAttribute('value', jsonObject.menus[i].menuId);
    checkbox.setAttribute('name', 'removeMenus');
    column.appendChild(checkbox);
    if (!siteProfileClassDefault) {
      checkbox.disabled = true;
    }
    row.appendChild(column);
    column = document.createElement('td');
    column.innerHTML = jsonObject.menus[i].menuLongDesc;
    row.appendChild(column);
    column = document.createElement('td');
    column.innerHTML = jsonObject.menus[i].siteDomainName;
    row.appendChild(column);
    tbody.appendChild(row);
  }
}

var jc_menus_add_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      var message = document.getElementById("jc_menus_search_message");
      message.innerHTML = "Unexcepted Error: unable to add menus";
      return false;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    jc_menus_show_menus(jsonObject);
    jc_menus_search_finish();
  },
  failure: function(o) {
    jc_panel_show_error("Unable to add menus");
  }
};

function jc_menus_search_client_callback(value) {
  var response = eval('(' + value + ')');
  var itemId = <c:out value='${itemMaintForm.itemId}'/>

  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/item/itemMaint.do";
  var data = "process=addMenus&itemId=" + itemId;
  data += "&menuWindowTarget=" + response.menuWindowTarget;
  data += "&menuWindowMode=" + response.menuWindowMode;
  for (var i = 0; i < response.menus.length; i++) {
    data += "&addMenus=" + response.menus[i].menuId;
  }
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_menus_add_callback, data);
  return false;
}
</script>

<script type="text/javascript">
var jc_menus_remove_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to remove menus");
      return false;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    jc_menus_show_menus(jsonObject);
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to add to menus");
  }
};

function jc_menus_remove() {
  var itemId = ${itemMaintForm.itemId};
  var url = "/${adminBean.contextPath}/admin/item/itemMaint.do";
  var data = "process=removeMenus&itemId=" + itemId;
  
  var e = document.getElementById("menuTable");
  var checkboxes = new Array();
  jc_traverse_element(e, checkboxes, 'input', 'removeMenus.*');
  
  var removeMenus = new Array();
  var count = 0;
  for (var i = 0; i < checkboxes.length; i++) {
    if (!checkboxes[i].checked) {
      continue;
    }
    data += "&removeMenus=" + checkboxes[i].value;
    removeMenus[count++] = checkboxes[i].value;
  }
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_menus_remove_callback, data);
  return false;
}

function jc_menu_search(p_type, p_args, p_value) {
	jc_menus_search_show(p_value.value);
}

if (mode != 'C') {
	var butMenusOption = [
		<c:forEach var="siteDomain" items="${itemMaintForm.siteDomains}">
	    { text: "Select Menus from ${siteDomain.label}", value: ${siteDomain.value}, onclick: { fn: jc_menu_search } },
	    </c:forEach>
	    { text: "Remove Selected Menus", value: 2, onclick: { fn: jc_menus_remove } }
	];
	var butMenus = new YAHOO.widget.Button({
	      disabled: true,
	      type: "menu", 
	      label: "Options", 
	      name: "butMenus",
	      menu: butMenusOption, 
	      container: "butMenusContainer" });
	if (siteProfileClassDefault) {
	  butMenus.set('disabled', false);
	}
}
</script>

<script type="text/javascript">
YAHOO.util.Event.onContentReady('tabCategory', function() {
	var showCategory_callback =
	{
	  success: function(o) {
	    if (!isJsonResponseValid(o.responseText)) {
	      jc_panel_show_error("Unexcepted Error: unable to show selected category");
	      return false;
	    }
	    var jsonObject = getJsonObject(o.responseText);
	    jc_category_show_categories(jsonObject);
	  },
	  failure: function(o) {
	      jc_panel_show_error("Unexcepted Error: unable to show selected menu");
	  }
	};
	var url = "/${adminBean.contextPath}/admin/item/itemMaint.do";
	var data = "process=showCategories&itemId=" + itemId ;
	var request = YAHOO.util.Connect.asyncRequest('POST', url, showCategory_callback, data);
} );

function jc_category_show_categories(jsonObject) {
	  var table = document.getElementById("categoryTable");
	  var tbody = table.getElementsByTagName("tbody")[0]
	  if (!tbody) {
	    tbody = document.createElement('tbody');
	    table.appendChild(tbody);
	  }
	  var rows = tbody.childNodes;
	  for (var i = table.rows.length - 1; i > 0; i--) {
	    table.deleteRow(i);
	  }
	  for (var i = 0; i < jsonObject.categories.length; i++) {
	    var row = document.createElement('tr');
	    var column;
	    column = document.createElement('td');
	    var inputCheck = document.createElement('input');
	    inputCheck.setAttribute('type', 'checkbox');
		inputCheck.setAttribute('value', jsonObject.categories[i].catId);
		inputCheck.setAttribute('name', 'removeCategories');
	    column.appendChild(inputCheck);
	    if (!siteProfileClassDefault) {
	    	inputCheck.disabled = true;
	    }
	    row.appendChild(column);
	    column = document.createElement('td');
	    column.innerHTML = jsonObject.categories[i].catShortTitle;
	    row.appendChild(column);
	    tbody.appendChild(row);
	  }
}

var jc_category_add_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_category_search_messge("Unexcepted Error: unable to assign category");
      return false;
    }
    var jsonObject = getJsonObject(o.responseText);
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    jc_category_show_categories(jsonObject);
    jc_category_search_finish();
  },
  failure: function(o) {
    jc_category_search_messge("Unable to add category");
  }
};

function jc_category_search_client_callback(value) {
  var response = eval('(' + value + ')');
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/item/itemMaint.do";
  var data = "process=addCategories&itemId=" + itemId;
  for (var i = 0; i < response.categories.length; i++) {
	data += "&addCategories=" + response.categories[i].catId;
  }
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_category_add_callback, data);
  return false;
}
</script>
<%@ include file="/admin/include/categoriesLookup.jsp" %>

<script type="text/javascript">
var jc_category_remove_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to remove categories");
      return false;
    }
    var jsonObject = getJsonObject(o.responseText);
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    jc_category_show_categories(jsonObject);
  },
  failure: function(o) {
    jc_panel_show_error("Unable to remove item from category");
  }
};

function jc_category_remove() {
	var url = "/${adminBean.contextPath}/admin/item/itemMaint.do";
	var data = "process=removeCategories&itemId=" + itemId;
	  
	var e = document.getElementById("categoryTable");
	var checkboxes = new Array();
	jc_traverse_element(e, checkboxes, 'input', 'removeCategories.*');
	  
	var removeCategories = new Array();
	var count = 0;
	for (var i = 0; i < checkboxes.length; i++) {
	    if (!checkboxes[i].checked) {
	      continue;
	    }
	    data += "&removeCategories=" + checkboxes[i].value;
	    removeCategories[count++] = checkboxes[i].value;
	}
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_category_remove_callback, data);
	return false;
}

var butCategoryOption = [
    { text: "Pick Category", value: 1, onclick: { fn: jc_category_search_show } },
    { text: "Remove From Category", value: 2, onclick: { fn: jc_category_remove } }
];
var butCategory = new YAHOO.widget.Button({
      disabled: true,
      type: "menu", 
      label: "Options", 
      name: "butCategory",
      menu: butCategoryOption, 
      container: "butCategoryContainer" });
if (siteProfileClassDefault) {
  butCategory.set('disabled', false);
}
</script>

<script type="text/javascript">
/*
Adjust inventory pop-up window
*/
var jc_adjust_booked_inventory_panel;
function jc_adjust_booked_inventory_init() {
  jc_adjust_booked_inventory_panel = new YAHOO.widget.Panel("jc_adjust_booked_inventory_panel", 
                              { 
                                width: "300px", 
                                visible: false, 
                                constraintoviewport: true ,
                                fixedcenter : true,
                                modal: true
                              } 
                               );
  jc_adjust_booked_inventory_panel.render();
}
function jc_adjust_booked_inventory_show(event) {
  document.itemMaintForm.itemAdjBookedQty.value = '';
  jc_adjust_booked_inventory_panel.show();
}
function jc_adjust_booked_inventory_finish() {
  jc_adjust_booked_inventory_panel.hide();
}
function jc_adjust_booked_inventory_message(message) {
  var msg = document.getElementById("jc_adjust_booked_inventory_message");
  msg.innerHTML = message;
}
YAHOO.util.Event.onDOMReady(jc_adjust_booked_inventory_init);

var jc_adjust_booked_inventory_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_adjust_booked_inventory_message("Unexcepted Error: unable to adjust inventory");
      return false;
    }
    var jsonObject = getJsonObject(o.responseText);
    if (!isJsonResponseSuccess(o.responseText)) {
      jc_adjust_booked_inventory_message(jsonObject.message);
      return;
    }
    setIdValue("itemBookedQty", jsonObject.itemBookedQty);
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    jc_adjust_booked_inventory_finish();
  },
  failure: function(o) {
    jc_adjust_inventory_message("Unexcepted Error: unable to adjust booked inventory");
  }
};

function jc_adjust_booked_inventory() {
  var itemId = <c:out value='${itemMaintForm.itemId}'/>
  var url = "/${adminBean.contextPath}/admin/item/itemMaint.do";
  var data = "process=adjustBookedQty&itemId=" + itemId + "&itemAdjBookedQty=" + document.itemMaintForm.itemAdjBookedQty.value;
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_adjust_booked_inventory_callback, data);
}

if (mode != 'C') {
	var butAdjBookInv = new YAHOO.widget.Button("butAdjBookInv", { disabled: true });
	if (siteProfileClassDefault) {
	  butAdjBookInv.set('disabled', false);
	}
	butAdjBookInv.on("click", jc_adjust_booked_inventory_show);
}
</script>

<div class=" yui-skin-sam">
<div>
<div id="jc_adjust_booked_inventory_panel">
  <div class="hd">Adjust Inventory</div>
  <div class="bd"> 
    <div id="jc_adjust_booked_inventory_message" class="jc_input_error"></div>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>Enter inventory to adjust</td>
      </tr>
      <tr>
        <td>
          <input type="text" name="itemAdjBookedQty">
        </td>
      </tr>
      <tr>
        <td>
          &nbsp;
        </td>
      </tr>
      <tr>
        <td>
          <a href="javascript:void(0);" onclick="jc_adjust_booked_inventory()" class="jc_navigation_link">Confirm</a>
          <a href="javascript:void(0);" onclick="jc_adjust_booked_inventory_finish()" class="jc_navigation_link">Cancel</a>&nbsp;
        </td>
      </tr>
    </table>
  </div>
</div>
</div>
</div>

<layout:mode value="edit">
<script type="text/javascript">
var butAdjInv = new YAHOO.widget.Button("butAdjInv", { disabled: true });
if (siteProfileClassDefault) {
  butAdjInv.set('disabled', false);
}
butAdjInv.on("click", jc_adjust_inventory_show);
/*
Adjust inventory pop-up window
*/
var jc_adjust_inventory_panel;
function jc_adjust_inventory_init() {
  jc_adjust_inventory_panel = new YAHOO.widget.Panel("jc_adjust_inventory_panel", 
                              { 
                                width: "300px", 
                                visible: false, 
                                constraintoviewport: true ,
                                fixedcenter : true,
                                modal: true
                              } 
                               );
  jc_adjust_inventory_panel.render();
}
function jc_adjust_inventory_show() {
  document.itemMaintForm.itemAdjQty.value = '';
  jc_adjust_inventory_panel.show();
}
function jc_adjust_inventory_finish() {
  jc_adjust_inventory_panel.hide();
}
function jc_adjust_inventory_message(message) {
  var msg = document.getElementById("jc_adjust_inventory_message");
  msg.innerHTML = message;
}
YAHOO.util.Event.onDOMReady(jc_adjust_inventory_init);

var jc_adjust_inventory_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_adjust_inventory_message("Unexcepted Error: unable to adjust inventory");
      return false;
    }
    var jsonObject = getJsonObject(o.responseText);
    if (!isJsonResponseSuccess(o.responseText)) {
      jc_adjust_inventory_message(jsonObject.message);
      return;
    }
    setIdValue("itemQty", jsonObject.itemQty);
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    jc_adjust_inventory_finish();
  },
  failure: function(o) {
    jc_adjust_inventory_message("Unexcepted Error: unable to adjust inventory");
  }
};

function jc_adjust_inventory() {
  var itemId = <c:out value='${itemMaintForm.itemId}'/>
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/item/itemMaint.do";
  var data = "process=adjustQty&itemId=" + itemId + "&itemAdjQty=" + document.itemMaintForm.itemAdjQty.value;
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_adjust_inventory_callback, data);

}
</script>

<div class=" yui-skin-sam">
<div>
<div id="jc_adjust_inventory_panel">
  <div class="hd">Adjust Inventory</div>
  <div class="bd"> 
    <div id="jc_adjust_inventory_message" class="jc_input_error"></div>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>Enter inventory to adjust</td>
      </tr>
      <tr>
        <td>
          <input type="text" name="itemAdjQty">
        </td>
      </tr>
      <tr>
        <td>
          &nbsp;
        </td>
      </tr>
      <tr>
        <td>
          <a href="javascript:void(0);" onclick="jc_adjust_inventory()" class="jc_navigation_link">Confirm</a>
          <a href="javascript:void(0);" onclick="jc_adjust_inventory_finish()" class="jc_navigation_link">Cancel</a>&nbsp;
        </td>
      </tr>
    </table>
  </div>
</div>
</div>
</div>

<script type="text/javascript">
if (mode != 'C') {
	var butResetCounter = new YAHOO.widget.Button("butResetCounter", { disabled: true });
	if (siteProfileClassDefault) {
	  butResetCounter.set('disabled', false);
	}
	butResetCounter.on("click", jc_reset_counter);
}

function jc_reset_counter() {
  var itemId = <c:out value='${itemMaintForm.itemId}'/>
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/item/itemMaint.do";
  var data = "process=resetCounter&itemId=" + itemId ;
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_reset_counter_callback, data);
  return false;
}

var jc_reset_counter_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: Unable to reset hit counter");
      return false;
    }
    setIdValue("hitCounter", '<span class="jc_input_control">0</span>');
    var jsonObject = getJsonObject(o.responseText);
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    jc_panel_show_message("Hit counter reset successfully");
  },
  failure: function(o) {
      jc_panel_show_error("Unexcepted Error: Unable to reset hit counter");
  }
};
</script>
</layout:mode>
</html:form>

<layout:mode value="edit">
<script type="text/javascript">
var jc_images_override_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to override images");
      return false;
    }
    jc_images_paint();
  },
  failure: function(o) {
      jc_panel_show_error("Unexcepted Error: unable to override images");
  }
};
function overrideImageLanguage(object) {
  var itemId = <c:out value='${itemMaintForm.itemId}'/>
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/item/itemMaint.do?process=overrideImages&itemId=" + itemId + "&siteProfileClassId=" + siteProfileClassId;
  if (object.checked) {
    url += '&imagesOverride=true';
  }
  else {
    url += '&imagesOverride=false';
  }
  var request = YAHOO.util.Connect.asyncRequest('GET', url, jc_images_override_callback);
  if (object.checked) {
    butImage.set('disabled', false);
  }
  else {
    butImage.set('disabled', true);
  }
}

function image_upload() {
  jc_image_upload_show(<c:out value='${itemMaintForm.itemId}'/>);
}
function image_remove() {
  jc_images_remove();
}
var imageOptionsMenu = [
    { text: "Upload new image", value: 1, onclick: { fn: image_upload } },
    { text: "Remove selected images", value: 2, onclick: { fn: image_remove } }
];
if (mode != 'C') {
	var butImage = new YAHOO.widget.Button({
	      disabled: true,   
	      type: "menu", 
	      label: "Options", 
	      name: "logoOptions",
	      menu: imageOptionsMenu, 
	      container: "imageOptionContainer" });
	<c:if test="${itemMaintForm.siteProfileClassDefault || itemMaintForm.itemImageOverride}">
	  butImage.set('disabled', false);
	</c:if>
}
               
var jc_images_paint_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to show item images");
      return false;
    }
    var jsonObject = getJsonObject(o.responseText);
    jc_image_show_images(jsonObject);
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to paint images");
  }
};
function jc_images_paint() {
  var itemId = <c:out value='${itemMaintForm.itemId}'/>
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/item/itemMaint.do?process=showImages&itemId=" + itemId + "&siteProfileClassId=" + siteProfileClassId;
  var request = YAHOO.util.Connect.asyncRequest('GET', url, jc_images_paint_callback);
  return false;
}

function jc_image_upload_client_callback(value) {
  var response = eval('(' + value + ')');
  var formName = response.formName;
  YAHOO.util.Connect.setForm(formName, true, true);

  var url = "/<c:out value='${adminBean.contextPath}'/>/admin/item/itemMaint.do?siteProfileClassId=" + siteProfileClassId;
  var response = YAHOO.util.Connect.asyncRequest('GET', url, jc_upload_callback);
}

var jc_upload_callback = {
  upload: function(o) {
    var value = o.responseText.replace(/<\/?pre>/ig, '');
    if (!isJsonResponseValid(value)) {
      jc_image_upload_message('Unable to upload image');
      return false;
    }
    var jsonObject = getJsonObject(value);
    if (jsonObject.status == 'failed') {
      jc_image_upload_message(jsonObject.message);
    }
    else {
      jc_image_show_images(jsonObject);
      setIdValue("recUpdateBy", jsonObject.recUpdateBy);
      setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
      jc_image_upload_finish();
    }
  }
}

function jc_image_show_images(jsonObject) {
  var table = document.getElementById("jc_images_table");
  var tbody = table.getElementsByTagName("tbody")[0]
  if (!tbody) {
    tbody = document.createElement('tbody');
    table.appendChild(tbody);
  }
  var rows = tbody.childNodes;
  for (var i = table.rows.length - 1; i >= 0; i--) {
    table.deleteRow(i);
  }
  var allowEdit = true;
  if (!siteProfileClassDefault) {
    if (!document.itemMaintForm.itemImageOverride.checked) {
      allowEdit = false;
    }
  }
  var row = document.createElement('tr');
  if (jsonObject.defaultImage) {
    var column = document.createElement('td');
    column.className = 'jc_input_label_small';
    column.setAttribute("width", '150');
    column.setAttribute("valign", 'bottom');
    column.setAttribute("align", 'center');
    var html = '';
    html = '<img src="<c:out value="/${adminBean.contextPath}"/>/services/SecureImageProvider.do?type=I&maxsize=100&imageId=' + jsonObject.defaultImage.imageId;
    if (!jsonObject.defaultImage.isLanguageDefault) {
      html += '&siteProfileClassDefault=false';
    }
    html += '"/><br>';
    if (allowEdit) {
      html += '<input type="checkbox" value="' + jsonObject.defaultImage.imageId + '" name="removeImages"/><br>';
    }
    html += jsonObject.defaultImage.imageName + '<br>';
    html += 'Default Image';
    column.innerHTML = html;  
    row.appendChild(column);
  }
  
  for (var i = 0; i < jsonObject.images.length; i++) {
    var image = jsonObject.images[i];
    var column = document.createElement('td');
    column.noWrap = true;
    column.className = 'jc_input_label_small';
    column.setAttribute("width", '150');
    column.setAttribute("valign", 'bottom');
    column.setAttribute("align", 'center');
    var html = '';
    html = '<img src="/${adminBean.contextPath}/services/SecureImageProvider.do?type=I&maxsize=100&imageId=' + image.imageId;
    if (!image.isLanguageDefault) {
      html += '&siteProfileClassDefault=false';
    }
    html += '"/><br>';
    if (allowEdit) {
      html += '<input type="checkbox" value="' + image.imageId + '" name="removeImages"/><br>';
    }
    html += image.imageName + '<br><br>';
    if (allowEdit) {
      html += '<a class="jc_navigation_link" onclick="jc_images_set_default(' + image.imageId + ');" href="javascript:void(0);">Set&nbsp;Default</a>';
    }
    column.innerHTML = html;
    row.appendChild(column);
  }
  var column = document.createElement('td');
  column.setAttribute("width", '100%');
  row.appendChild(column);
  tbody.appendChild(row);
}

function jc_images_remove() {
  var itemId = <c:out value='${itemMaintForm.itemId}'/>
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/item/itemMaint.do";
  var data = "process=removeImages&itemId=" + itemId + "&siteProfileClassId=" + siteProfileClassId;
  
  var e = document.getElementById("jc_images_table");
  var checkboxes = new Array();
  jc_traverse_element(e, checkboxes, 'input', 'removeImages');
  
  var removeImages = new Array();
  var count = 0;
  for (var i = 0; i < checkboxes.length; i++) {
    if (!checkboxes[i].checked) {
      continue;
    }
    data += "&removeImages=" + checkboxes[i].value;
    removeImages[count++] = checkboxes[i].value;
  }
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_images_remove_callback, data);
  return false;
}

var jc_images_remove_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to add to menus");
      return false;
    }
    var jsonObject = getJsonObject(o.responseText);
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    jc_image_show_images(jsonObject);
  },
  failure: function(o) {
      jc_panel_show_error("Unexcepted Error: unable to remove images");
  }
};

function jc_images_set_default(imageId) {
  var itemId = <c:out value='${itemMaintForm.itemId}'/>
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/item/itemMaint.do";
  var data = "process=defaultImage&itemId=" + itemId + "&createDefaultImageId=" + imageId + "&siteProfileClassId=" + siteProfileClassId;
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_images_set_default_callback, data);
  return false;
}

var jc_images_set_default_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_image_upload_message("Unexcepted Error: unable to set default menu");
      return false;
    }
    var jsonObject = getJsonObject(o.responseText);
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    jc_image_show_images(jsonObject);
  },
  failure: function(o) {
      jc_image_upload_message("Unexcepted Error: unable to set default menu");
  }
};

YAHOO.util.Event.onContentReady('jc_images_table', function() {
	jc_images_paint();
} );

function submitTranslateForm() {
	submitForm('translate');
}
if (mode != 'C') {
	var butTranslate = new YAHOO.widget.Button("butTranslate", { disabled: true });
	if (translationEnable) {
		butTranslate.set('disabled', false);
	}
	butTranslate.on("click", submitTranslateForm);
}
</script>
<%@ include file="/admin/include/imageUpload.jsp" %>
</layout:mode>
<%@ include file="/admin/include/itemLookup.jsp" %>
<%@ include file="/admin/include/panel.jsp" %>
<%@ include file="/admin/include/confirm.jsp" %>