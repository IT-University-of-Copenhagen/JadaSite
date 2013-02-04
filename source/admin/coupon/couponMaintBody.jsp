<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="couponMaintForm" scope="request" value="${couponMaintForm}"/>
<script language="JavaScript">
var siteProfileClassDefault = ${couponMaintForm.siteProfileClassDefault};
var siteCurrencyClassDefault = ${couponMaintForm.siteCurrencyClassDefault};
var translationEnable = ${couponMaintForm.translationEnable};
var siteProfileClassId = '${couponMaintForm.siteProfileClassId}';

var couponId = '${couponMaintForm.couponId}';
var mode = '${couponMaintForm.mode}';
function submitForm(type) {
    if (!siteProfileClassDefault) {
        document.couponMaintForm.couponNameLang.value = document.couponMaintForm.couponNameLang_tmp.value;
	}
	if (!siteCurrencyClassDefault) {
		document.couponMaintForm.couponDiscountAmountCurr.value = document.couponMaintForm.couponDiscountAmountCurr_tmp.value;
		document.couponMaintForm.couponOrderAmountCurr.value = document.couponMaintForm.couponOrderAmountCurr_tmp.value;
	}
    document.couponMaintForm.process.value = type;
    document.couponMaintForm.submit();
    return false;
}
function submitCancelForm() {
    document.couponMaintForm.action = "/${adminBean.contextPath}/admin/coupon/couponListing.do";
    document.couponMaintForm.process.value = "back";
    document.couponMaintForm.submit();
    return false;
}

function initDiscountField(couponType) {
	document.couponMaintForm.couponDiscountPercent.disabled = true;
	if (siteCurrencyClassDefault) {
		document.couponMaintForm.couponDiscountAmount.disabled = true;
		document.couponMaintForm.couponOrderAmount.disabled = true;
	}
	else {
		document.couponMaintForm.couponDiscountAmountCurrFlag.disabled = true;
		document.couponMaintForm.couponOrderAmountCurrFlag.disabled = true;
		document.couponMaintForm.couponDiscountAmountCurr_tmp.disabled = true;
		document.couponMaintForm.couponOrderAmountCurr_tmp.disabled = true;
	}
	if (couponType == '1') {
		document.couponMaintForm.couponDiscountPercent.disabled = false;
		if (siteCurrencyClassDefault) {
			document.couponMaintForm.couponDiscountAmount.value = '';
			document.couponMaintForm.couponOrderAmount.value = '';
		}
		else {
			document.couponMaintForm.couponDiscountAmountCurr_tmp.value = '';
			document.couponMaintForm.couponOrderAmountCurr_tmp.value = '';
		}
	}
	if (couponType == '2') {
		document.couponMaintForm.couponDiscountPercent.value = '';
		if (siteCurrencyClassDefault) {
			document.couponMaintForm.couponDiscountAmount.disabled = false;
			document.couponMaintForm.couponOrderAmount.value = '';
		}
		else {
			if (document.couponMaintForm.couponDiscountAmountCurrFlag.checked) {
				document.couponMaintForm.couponDiscountAmountCurr_tmp.disabled = false;
			}
			document.couponMaintForm.couponDiscountAmountCurrFlag.disabled = false;
			document.couponMaintForm.couponOrderAmountCurr_tmp.value = '';
		}
	}
	if (couponType == '3') {
		// perform no action.
	}
	if (couponType == '4') {
		document.couponMaintForm.couponDiscountPercent.value = '';
		if (siteCurrencyClassDefault) {
			document.couponMaintForm.couponDiscountAmount.disabled = false;
			document.couponMaintForm.couponOrderAmount.disabled = false;
		}
		else {
			document.couponMaintForm.couponDiscountAmountCurrFlag.disabled = false;
			document.couponMaintForm.couponOrderAmountCurrFlag.disabled = false;			
			if (document.couponMaintForm.couponDiscountAmountCurrFlag.checked) {
				document.couponMaintForm.couponDiscountAmountCurr_tmp.disabled = false;
			}
			if (document.couponMaintForm.couponOrderAmountCurrFlag.checked) {
				document.couponMaintForm.couponOrderAmountCurr_tmp.disabled = false;
			}
		}
	}
}

function resetDiscountField() {
	var couponType = document.couponMaintForm.couponType.value;
	if (couponType == '') {
		couponType = '1';
	}
	initDiscountField(couponType);
}

/*
 * Add and remove items
 */
var jc_item_add_callback = {
	success: function(o) {
		if (!isJsonResponseValid(o.responseText)) {
		    jc_panel_show_error("Unexcepted Error: Unable to add item");
			jc_item_search_finish();
		    return false;
		}
		var jsonObject = getJsonObject(o.responseText);
		if (!isJsonResponseSuccess(o.responseText)) {
			jc_panel_show_error("Error: " + jsonObject.message);
			jc_item_search_finish();
			return false;
		}
		jc_item_search_finish();
		showItems();
	},
	failure: function(o) {
		jc_panel_show_error("Unexcepted Error: Unable to add item");
	}	
};

function jc_item_search_client_callback(result) {
	var response = eval('(' + result + ')');
	var url = "/${adminBean.contextPath}/admin/coupon/couponMaint.do";
	var postData = ""
	postData += "process=addItem&couponId=" + couponId + "&itemId=" + response.items[0].itemId;
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_item_add_callback, postData);
}

var jc_item_remove_callback = {
	success: function(o) {
		if (!isJsonResponseValid(o.responseText)) {
		    jc_panel_show_error("Unexcepted Error: Unable to remove item");
		    return false;
		}
		var jsonObject = getJsonObject(o.responseText);
		if (!isJsonResponseSuccess(o.responseText)) {
			jc_panel_show_error("Unexcepted Error: " + jsonObject.message);
			return false;
		}
		showItems();
	},
	failure: function(o) {
		jc_panel_show_error("Unexcepted Error: Unable to remove item");
	}	
};

function addItem() {
	jc_item_search_show(jc_item_search_client_callback);
}

function removeItems() {
	var elements = document.getElementsByName('items');
	var url = "/${adminBean.contextPath}/admin/coupon/couponMaint.do";
	var postData = "process=removeItems&couponId=" + couponId;
	for (var i = 0; i < elements.length; i++) {
		var checkbox = elements[i];
		if (checkbox.checked) {
			postData += "&itemIds=" + checkbox.value;
		}
	}
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_item_remove_callback, postData);
}

if (mode != 'C') {
	YAHOO.util.Event.onAvailable('itemsButtons', function() {
		var buttonMenu = [
				{ text: "Add item", value: 'Add item', onclick: { fn: addItem } }, 
				{ text: "Remove items", value: 'Remove items', onclick: { fn: removeItems } }
		];
		var menu = new YAHOO.widget.Button({ 
		      				  disabled: true,
	                          type: "menu", 
	                          label: "Options", 
	                          name: "menu",
	                          menu: buttonMenu, 
	                          container: 'itemsButtons' });
		if (siteProfileClassDefault) {
			  menu.set('disabled', false);
		}
	} );
}

var showItems_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: Unable to extract item list");
      return false;
    }
    var node = document.getElementById("itemsContainer");
    node.innerHTML = '';
    var table = document.createElement('table');
    node.appendChild(table);
    var body = document.createElement('tbody');
    table.appendChild(body);
    
    var jsonObject = getJsonObject(o.responseText);
    for (var i = 0; i < jsonObject.items.length; i++) {
        var item = jsonObject.items[i];
        var row = document.createElement('tr');
        body.appendChild(row);
        var col;
        col = document.createElement('td');
        row.appendChild(col);
        var checkbox = document.createElement('input');
        checkbox.setAttribute('type', 'checkbox');
        checkbox.setAttribute('name', 'items');
        checkbox.setAttribute('id', 'items');
        checkbox.setAttribute('value', item.itemId);
        col.appendChild(checkbox);
        col = document.createElement('td');
        row.appendChild(col);
        col.appendChild(document.createTextNode(item.itemNum));
        col = document.createElement('td');
        row.appendChild(col);
        col.appendChild(document.createTextNode(item.itemNum));
        col = document.createElement('td');
        row.appendChild(col);
        col.appendChild(document.createTextNode(item.itemShortDesc));
    }
  },
  failure: function(o) {
      jc_panel_show_error("Unexcepted Error: Unable to extract item list");
  }
};

function showItems() {
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/coupon/couponMaint.do?process=listItem&couponId=" + couponId;
	var request = YAHOO.util.Connect.asyncRequest('GET', url, showItems_callback);
}

/*************************************************************/

function addCategory() {
	jc_category_search_show();
}

var jc_category_add_callback = {
	success: function(o) {
		if (!isJsonResponseValid(o.responseText)) {
		    jc_panel_show_error("Unexcepted Error: Unable to add category");
			jc_category_search_finish();
		    return false;
		}
		var jsonObject = getJsonObject(o.responseText);
		if (!isJsonResponseSuccess(o.responseText)) {
			jc_panel_show_error("Unexcepted Error: " + jsonObject.message);
			jc_category_search_finish();
			return false;
		}
		jc_category_search_finish();
		showCategories();
	},
	failure: function(o) {
		jc_panel_show_error("Unexcepted Error: Unable to add category");
	}	
};

function jc_category_search_client_callback(result) {
	var response = eval('(' + result + ')');
	var url = "/${adminBean.contextPath}/admin/coupon/couponMaint.do";
	var postData = "";
	postData += "process=addCategory&couponId=" + couponId + "&catId=" + response.categories[0].catId;
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_category_add_callback, postData);
}

var jc_category_remove_callback = {
	success: function(o) {
		if (!isJsonResponseValid(o.responseText)) {
		    jc_panel_show_error("Unexcepted Error: Unable to remove category");
		    return false;
		}
		var jsonObject = getJsonObject(o.responseText);
		if (!isJsonResponseSuccess(o.responseText)) {
			jc_panel_show_error("Unexcepted Error: " + jsonObject.message);
			return false;
		}
		showCategories();
	},
	failure: function(o) {
		jc_panel_show_error("Unexcepted Error: Unable to remove category");
	}	
};

function removeCategories() {
	var elements = document.getElementsByName('categories');
	var url = "/${adminBean.contextPath}/admin/coupon/couponMaint.do";
	var postData = "process=removeCategories&couponId=" + couponId;
	for (var i = 0; i < elements.length; i++) {
		var checkbox = elements[i];
		if (checkbox.checked) {
			postData += "&catIds=" + checkbox.value;
		}
	}
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_category_remove_callback, postData);
}

if (mode != 'C') {
	YAHOO.util.Event.onAvailable('categoriesButtons', function() {
		var buttonMenu = [
				{ text: "Add category", value: 'Add category', onclick: { fn: addCategory } }, 
				{ text: "Remove categories", value: 'Remove categories', onclick: { fn: removeCategories } }
		];
		var menu = new YAHOO.widget.Button({ 
							  disabled: true,
	                          type: "menu", 
	                          label: "Options", 
	                          name: "menu",
	                          menu: buttonMenu, 
	                          container: 'categoriesButtons' });      
		if (siteProfileClassDefault) {
			  menu.set('disabled', false);
		}
	} );
}

var showCategories_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: Unable to extract category list");
      return false;
    }
    var node = document.getElementById("categoriesContainer");
    node.innerHTML = '';
    var table = document.createElement('table');
    node.appendChild(table);
    var body = document.createElement('tbody');
    table.appendChild(body);
    
    var jsonObject = getJsonObject(o.responseText);
    for (var i = 0; i < jsonObject.categories.length; i++) {
        var category = jsonObject.categories[i];
        var row = document.createElement('tr');
        body.appendChild(row);
        var col;
        col = document.createElement('td');
        row.appendChild(col);
        var checkbox = document.createElement('input');
        checkbox.setAttribute('type', 'checkbox');
        checkbox.setAttribute('id', 'categories');
        checkbox.setAttribute('name', 'categories');
        checkbox.setAttribute('value', category.catId);
        col.appendChild(checkbox);
        col = document.createElement('td');
        row.appendChild(col);
        col.appendChild(document.createTextNode(category.catShortTitle));
        col = document.createElement('td');
        row.appendChild(col);
    }
  },
  failure: function(o) {
      jc_panel_show_error("Unexcepted Error: Unable to extract category list");
  }
};

function showCategories() {
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/coupon/couponMaint.do?process=listCategory&couponId=" + couponId;
	var request = YAHOO.util.Connect.asyncRequest('GET', url, showCategories_callback);
}

/***********************************************************/

if (mode != 'C') {
	YAHOO.util.Event.onAvailable('itemsContainer', function() {
		showItems();
	} );
	YAHOO.util.Event.onAvailable('categoriesContainer', function() {
		showCategories();
	} );
}

function handleCouponStartDate(type, args, obj) {
alert('here 1');
    document.couponMaintForm.couponStartDate.value = jc_calendar_callback(type, args, obj);
}
function handleCouponEndDate(type, args, obj) { 
    document.couponMaintForm.couponEndDate.value = jc_calendar_callback(type, args, obj);
}
YAHOO.util.Event.onAvailable('calCouponStartDateContainer', function() {
	initCalendar("calCouponStartDateContainer", "couponStartDate", "calCouponStartDate");
});
YAHOO.util.Event.onAvailable('calCouponEndDateContainer', function() {
	initCalendar("calCouponEndDateContainer", "couponEndDate", "calCouponEndDate");
});

YAHOO.util.Event.onDOMReady(function() {
	initDiscountField('${couponMaintForm.couponType}');
});

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
<div class=" yui-skin-sam">
<html:form method="post" action="/admin/coupon/couponMaint"> 
<html:hidden property="process" value=""/> 
<html:hidden property="couponId"/> 
<html:hidden property="mode"/> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - <a href="/${adminBean.contextPath}/admin/coupon/couponListing.do?process=back">Coupon 
      Listing</a> - Coupon Maintenance</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr> 
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
            Currency
            <html:select property="siteCurrencyClassId" onchange="javascript:submitForm('language')"> 
              <html:optionsCollection property="siteCurrencyClassBeans" value="value" label="label"/> 
            </html:select>
            </layout:mode>
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
          <td valign="top">
		    <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td class="jc_input_label">Coupon Code</td>
                </tr>
                <tr>
                <td class="jc_input_control">
                  <lang:text property="couponCode" size="20" styleClass="tableContent"/>
                  <logic:messagesPresent property="couponCode" message="true"> 
                    <br>
                    <html:messages property="couponCode" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                  </logic:messagesPresent>
                </td>
              </tr>
			  <tr>
				<td class="jc_input_label">
				  Name
				  <lang:checkboxSwitch name="couponMaintForm" property="couponNameLangFlag"> - Override language</lang:checkboxSwitch>
				</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
				  <lang:text property="couponName" size="40" styleClass="tableContent"/> 
                  <logic:messagesPresent property="couponName" message="true"> 
                    <br>
                    <html:messages property="couponName" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                  </logic:messagesPresent>
				</td>
			  </tr>
			  <tr>
				<td class="jc_input_label">Start Date</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
                  <lang:text property="couponStartDate" styleClass="jc_input_control"/> 
                  <img id="calCouponStartDate" src="../images/icon/image_plus.gif" border="0">
                  <div id="calCouponStartDateContainer" style="position: absolute; display:none;"></div>
                  <logic:messagesPresent property="couponStartDate" message="true"> 
                    <br>
                    <html:messages property="couponStartDate" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                  </logic:messagesPresent>
				</td>
			  </tr>
			  <tr>
				<td class="jc_input_label">End Date</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
                  <lang:text property="couponEndDate" styleClass="jc_input_control"/> 
                  <img id="calCouponEndDate" src="../images/icon/image_plus.gif" border="0">
                  <div id="calCouponEndDateContainer" style="position: absolute; display:none;"></div>
                  <logic:messagesPresent property="couponEndDate" message="true"> 
                    <br>
                    <html:messages property="couponEndDate" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                  </logic:messagesPresent>
				</td>
			  </tr>
			  <tr>
				<td class="jc_input_label">Scope</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
                  <lang:select property="couponScope" styleClass="tableContent"> 
                    <lang:option value="O">Per Order</lang:option> 
                    <lang:option value="I">Per Item</lang:option> 
                  </lang:select>
				</td>
			  </tr>
			  <tr>
				<td class="jc_input_label">Automatic apply to all orders</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
		          <lang:checkbox property="couponAutoApply" value="Y" styleClass="jc_input_control"/> 
				</td>
			  </tr>
			  <tr>
				<td class="jc_input_label">Apply to all items and categories</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
		          <lang:checkbox property="couponApplyAll" value="Y" styleClass="jc_input_control"/> 
          		</td>
			  </tr>
              <tr>
                <td class="jc_input_label">Published</td>
              </tr>
              <tr>
                <td class="jc_input_control"><lang:checkbox property="published" value="Y"/></td>
              </tr>
		    </table>
		  </td>
          <td height="100%">
            
		  	<table width="100%" border="0" cellspacing="0" cellpadding="3">
			  <tr>
				<td class="jc_input_label">Coupon Type</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
                  <lang:select property="couponType" onchange="resetDiscountField()" styleClass="tableContent"> 
                    <lang:option value="1">Percentage discount</lang:option> 
                    <lang:option value="2">Dollar amount discount</lang:option> 
                    <lang:option value="3">Free shipping</lang:option> 
                    <lang:option value="4">Discount over order amount</lang:option> 
                  </lang:select>
				</td>
			  </tr>
              <tr>
                <td class="jc_input_label">Discount percentage</td>
              </tr>
              <tr>
                <td class="jc_input_control">
                  <lang:text property="couponDiscountPercent" size="20" styleClass="tableContent"/>
                  <logic:messagesPresent property="couponDiscountPercent" message="true"> 
                    <br>
                    <html:messages property="couponDiscountPercent" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                  </logic:messagesPresent>
                </td>
              </tr>
              <tr>
                <td class="jc_input_label">
                  Discount amount
                  <lang:checkboxSwitch name="couponMaintForm" property="couponDiscountAmountCurrFlag"> - Override currency</lang:checkboxSwitch><br>
                </td>
              </tr>
              <tr>
                <td class="jc_input_control">
                  <lang:text property="couponDiscountAmount" size="20" styleClass="tableContent"/>
                  <logic:messagesPresent property="couponDiscountAmount" message="true"> 
                    <br>
                    <html:messages property="couponDiscountAmount" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                  </logic:messagesPresent>
                </td>
              </tr>
              <tr>
                <td class="jc_input_label">
                  Order amount
                  <lang:checkboxSwitch name="couponMaintForm" property="couponOrderAmountCurrFlag"> - Override currency</lang:checkboxSwitch><br>
                </td>
              </tr>
              <tr>
                <td class="jc_input_control">
                  <lang:text property="couponOrderAmount" size="20" styleClass="tableContent"/>
                  <logic:messagesPresent property="couponOrderAmount" message="true"> 
                    <br>
                    <html:messages property="couponOrderAmount" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                  </logic:messagesPresent>
                </td>
              </tr>
              <layout:mode value="edit">
              <tr>
                <td class="jc_input_label">Total number of coupons used</td>
              </tr>
              <tr>
                <td class="jc_input_control">
                  <c:out value="${couponMaintForm.couponTotalUsed}"/>
                </td>
              </tr>
              </layout:mode>
              <tr>
                <td class="jc_input_label">Maximum number of coupons to be used</td>
              </tr>
              <tr>
                <td class="jc_input_control">
                  <lang:text property="couponMaxUse" size="20" styleClass="tableContent"/>
                  <logic:messagesPresent property="couponMaxUse" message="true"> 
                    <br>
                    <html:messages property="couponMaxUse" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                  </logic:messagesPresent>
                </td>
              </tr>
             <tr>
                <td class="jc_input_label">Maximum number of coupons to be used by a single customer</td>
              </tr>
              <tr>
                <td class="jc_input_control">
                  <lang:text property="couponMaxCustUse" size="20" styleClass="tableContent"/>
                  <logic:messagesPresent property="couponMaxCustUse" message="true"> 
                    <br>
                    <html:messages property="couponMaxCustUse" id="errorText" message="true"> 
                    <span class="jc_input_error"><bean:write name="errorText"/></span>
                    </html:messages>
                  </logic:messagesPresent>
                </td>
              </tr>
			  <tr>
				<td class="jc_input_label">Priority</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
                  <lang:select property="couponPriority" styleClass="tableContent"> 
                    <lang:option value="1">1</lang:option> 
                    <lang:option value="2">2</lang:option> 
                    <lang:option value="3">3</lang:option> 
                    <lang:option value="4">4</lang:option> 
                    <lang:option value="5">5</lang:option> 
                    <lang:option value="6">6</lang:option> 
                    <lang:option value="7">7</lang:option> 
                    <lang:option value="8">8</lang:option> 
                    <lang:option value="9">9</lang:option> 
                  </lang:select>
				</td>
			  </tr>
		    </table>
            
            
          </td>
          <td width="5px" height="100%">
            <div style="border-left: 1px solid #dcdcdc; height: 100%"></div>
          </td>
          <td valign="top" width="400">
	        <div class="jc_detail_panel_header">
	          <table width="100%" border="0" cellspacing="0" cellpadding="0">
	            <tr>
	              <td><span class="jc_input_label">Items</span></td>
	              <td><div align="right" id="itemsButtons"></div></td>
	            </tr>
	          </table>
	        </div>
	        <div>
	          <div id="itemsContainer" class="jc_detail_panel"></div>
	        </div>
	        <div class="jc_detail_panel_header">
	          <table width="100%" border="0" cellspacing="0" cellpadding="0">
	            <tr>
	              <td><span class="jc_input_label">Categories</span></td>
	              <td><div align="right" id="categoriesButtons"></div></td>
	            </tr>
	          </table>
	        </div>
	        <div class="jc_detail_panel">
	          <div id="categoriesContainer"></div>
	        </div>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table></html:form>
</div>
<%@ include file="/admin/include/itemLookup.jsp" %>
<%@ include file="/admin/include/categoryLookup.jsp" %>
<%@ include file="/admin/include/panel.jsp" %>
<%@ include file="/admin/include/confirm.jsp" %>
