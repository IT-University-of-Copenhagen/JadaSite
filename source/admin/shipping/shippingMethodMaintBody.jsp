<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="shippingMethodMaintForm" scope="request" value="${shippingMethodMaintForm}"/>
<script type="text/javascript">
var siteProfileClassDefault = ${shippingMethodMaintForm.siteProfileClassDefault};
var siteCurrencyClassDefault = ${shippingMethodMaintForm.siteCurrencyClassDefault};
var translationEnable = ${shippingMethodMaintForm.translationEnable};
var siteProfileClassId = "${shippingMethodMaintForm.siteProfileClassId}";
var siteCurrencyClassId = "${shippingMethodMaintForm.siteCurrencyClassId}";
var mode = "${shippingMethodMaintForm.mode}";
var jsonShippings = '';
if (mode != 'C') {
	jsonShippings = getJsonObject('${shippingMethodMaintForm.jsonShippingTypes}');
}

function submitForm(type) {
    if (!siteProfileClassDefault) {
      document.shippingMethodMaintForm.shippingMethodNameLang.value = document.shippingMethodMaintForm.shippingMethodNameLang_tmp.value;
    }
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}
function submitBackForm(type) {
    document.forms[0].action = "/${adminBean.contextPath}/admin/shipping/shippingMethodListing.do";
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}

function ShippingRate() {
	var id = null;
	var shippingMethidId = null;
	var shippingRegionId = null;
	var shippingTypeId = null;
	var shippingRateFee = null;
	var shippingRatePercentage = null;
	var shippingAdditionalRateFee = null;
	var shippingAdditionalRatePercentage = null;
}

function paintRate(container, rate) {
	container.innerHTML = '';
    var table = document.createElement('table');
    container.appendChild(table);
    table.className = 'jc_nobordered_table';
//    table.setAttribute('cellpadding', '5px');
    table.setAttribute('width', '120');
    table.setAttribute('border', '0');
    table.style.border = 0;
    var body = document.createElement('tbody');
    table.appendChild(body);

    var row = document.createElement('tr');
    row.style.border = 0;
    body.appendChild(row);
    var col = document.createElement('td');
    col.style.border = 0;
    col.style.padding = '3px';
    col.colSpan = '2';
    var div = document.createElement('div');
    div.setAttribute('align', 'right');
    var anchor = document.createElement('a');
    anchor.href = 'javascript:void(0);';
    YAHOO.util.Event.addListener(anchor, "click", function() {
        rateModify(rate, container.id);
    });
    anchor.appendChild(document.createTextNode('Update'));
    div.appendChild(anchor);
    col.appendChild(div);
    row.appendChild(col);
    
    var row = document.createElement('tr');
    row.style.border = 0;
    body.appendChild(row);
    var col = document.createElement('td');
    col.className = "jc_input_label";
    col.style.border = 0;
    col.style.padding = '3px';
    col.colSpan = '2';
    col.appendChild(document.createTextNode("First item"));
    row.appendChild(col);

    row = document.createElement('tr');
    row.style.border = 0;
    body.appendChild(row);
    var col = document.createElement('td');
    col.style.border = 0;
    col.style.padding = '3px';
    col.appendChild(document.createTextNode("Rate"));
    if (!siteCurrencyClassDefault) {
        if (rate.shippingRateFeeCurrFlag) {
            col.appendChild(document.createTextNode(' *'));
        }
    }
    row.appendChild(col);
    col = document.createElement('td');
    col.style.border = 0;
    col.style.padding = '3px';
    var div = document.createElement('div');
    div.setAttribute('align', 'right');
    if (siteCurrencyClassDefault || !rate.shippingRateFeeCurrFlag) {
    	div.appendChild(document.createTextNode(rate.shippingRateFee));
    }
    else {
    	div.appendChild(document.createTextNode(rate.shippingRateFeeCurr));
    }
    col.appendChild(div);
    row.appendChild(col);

    row = document.createElement('tr');
    row.style.border = 0;
    body.appendChild(row);
    col = document.createElement('td');
    col.style.border = 0;
    col.style.padding = '3px';
    col.appendChild(document.createTextNode("Percentage"));
    row.appendChild(col);
    col = document.createElement('td');
    col.style.border = 0;
    col.style.padding = '3px';
    div = document.createElement('div');
    div.setAttribute('align', 'right');
    div.appendChild(document.createTextNode(rate.shippingRatePercentage));
    col.appendChild(div);
    row.appendChild(col);

    row = document.createElement('tr');
    row.style.border = 0;
    body.appendChild(row);
    col = document.createElement('td');
    col.className = "jc_input_label";
    col.style.border = 0;
    col.style.padding = '3px';
    col.colSpan = '2';
    col.appendChild(document.createTextNode("Additional item"));
    row.appendChild(col);

    row = document.createElement('tr');
    row.style.border = 0;
    body.appendChild(row);
    var col = document.createElement('td');
    col.style.border = 0;
    col.style.padding = '3px';
    col.appendChild(document.createTextNode("Rate"));
    if (!siteCurrencyClassDefault) {
        if (rate.shippingAdditionalRateFeeCurrFlag) {
            col.appendChild(document.createTextNode(' *'));
        }
    }
    row.appendChild(col);
    col = document.createElement('td');
    col.style.border = 0;
    col.style.padding = '3px';
    div = document.createElement('div');
    div.setAttribute('align', 'right');
    if (siteCurrencyClassDefault || !rate.shippingAdditionalRateFeeCurrFlag) {
    	div.appendChild(document.createTextNode(rate.shippingAdditionalRateFee));
    }
    else {
    	div.appendChild(document.createTextNode(rate.shippingAdditionalRateFeeCurr));
    }
    col.appendChild(div);
    row.appendChild(col);

    row = document.createElement('tr');
    row.style.border = 0;
    body.appendChild(row);
    col = document.createElement('td');
    col.style.border = 0;
    col.style.padding = '3px';
    col.appendChild(document.createTextNode("Percentage"));
    row.appendChild(col);
    col = document.createElement('td');
    col.style.border = 0;
    col.style.padding = '3px';
    div = document.createElement('div');
    div.setAttribute('align', 'right');
    div.appendChild(document.createTextNode(rate.shippingAdditionalRatePercentage));
    col.appendChild(div);
    row.appendChild(col);
    
	container.appendChild(table);
}

var id = 0;
var customFormatter = function(cell, record, column, data) {
	var container = document.createElement('div');
	container.setAttribute('id', id++);
	paintRate(container, data);
	cell.appendChild(container);
	id++;
};

function TableColumn() {
	key = null;
	label = null;
	formatter = null;
}

if (mode != 'C') {
	YAHOO.util.Event.onContentReady("shippingTypesContainer", function() {
	    createTable = function() {       
	    	YAHOO.widget.DataTable.Formatter.myCustom = customFormatter;
	        var columns = [{key:"shippingTypeName", label:"", sortable:false, resizeable:true}];
	        var column = null;
	        for (var i = 0; i < jsonShippings.shippingRegions.length; i++) {
	            var shippingRegion = jsonShippings.shippingRegions[i];
	            var checked = '';
	            if (shippingRegion.published) {
	                checked = 'checked';
	            }
	            var disabled = "";
	            if (!siteProfileClassDefault || !siteCurrencyClassDefault) {
		            disabled = 'disabled="disabled"';
	            }
	            column = new TableColumn();
	            column.key = shippingRegion.shippingRegionId;
	            var label = '';
	            if (!siteProfileClassDefault || !siteCurrencyClassDefault) {
	            	label = shippingRegion.shippingRegionName +
 			   				"<input type='checkbox' name='shippingRegionIds' value='" + shippingRegion.shippingRegionId + "' " + checked + " " + disabled + ">";
		   			if (shippingRegion.published) {
			   			label += "<input type='hidden' name='shippingRegionIds' value='" + shippingRegion.shippingRegionId + "'" + ">"
		   			}
	            }
	            else {
	            	label = shippingRegion.shippingRegionName +
		   					"<input type='checkbox' name='shippingRegionIds' value='" + shippingRegion.shippingRegionId + "' " + checked + " " + disabled + ">";
	            }
	            column.label = label;
	            column.formatter = "myCustom";
	            columns.push(column);
	        }      
	
	        var myDataSource = new YAHOO.util.DataSource(jsonShippings.shippingTypes);
	        myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
	        var myDataTable = new YAHOO.widget.DataTable("shippingTypesContainer",
	                columns, myDataSource, {width:"1200px"});
	                
	        return {
	            oDS: myDataSource,
	            oDT: myDataTable
	        };
	    }();
	});
}
</script>
<html:form method="post" action="/admin/shipping/shippingMethodMaint"> <html:hidden property="mode"/> 
<html:hidden property="process" value=""/> <html:hidden property="shippingMethodId"/> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - <a href="/<c:out value='${adminBean.contextPath}'/>/admin/shipping/shippingMethodListing.do?process=back">Shipping 
      Method Listing</a> - Shipping Method Maintenance</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr> 
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
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td width="300px" valign="top"> 
            <table width="100%" border="0" cellspacing="0" cellpadding="5px" class="borderTable">
              <tr> 
                <td class="jc_input_label">
                Shipping Method
                <lang:checkboxSwitch name="shippingMethodMaintForm" property="shippingMethodNameLangFlag"> - Override language</lang:checkboxSwitch>
                </td>
              </tr>
              <tr>
                <td class="jc_input_control"> <lang:text property="shippingMethodName" size="40" styleClass="tableContent"/> 
                  <span class="jc_input_error"> <logic:messagesPresent property="shippingMethodName" message="true"> 
                  <html:messages property="shippingMethodName" id="errorText" message="true"> 
                  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
                  </span> </td>
              </tr>
              <tr> 
                <td class="jc_input_label">Published</td>
              </tr>
              <tr>
                <td class="jc_input_control"> <lang:checkbox property="published" value="Y"/> 
                </td>
              </tr>
              <tr> 
                <td> </td>
                <td> </td>
              </tr>
            </table>
            <br>
          </td>
        </tr>
      </table>
      <layout:mode value="edit">
      <div class="hr">
        <hr>
      </div>
      <br>
      <br>
	  <div id="shippingTypesContainer">
	  </div>
      </layout:mode>
    </td>
  </tr>
</table>
<layout:mode value="edit">
<script type="text/javascript">
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
</layout:mode>
</html:form>
<!-- ------------------------------------------------ -->

<script type="text/javascript">
var ratePanel = null;
YAHOO.util.Event.onContentReady('ratePanel', function() {
	var butRateSave = new YAHOO.widget.Button("butRateSave");
	butRateSave.on("click", rateSave);
    ratePanel = new YAHOO.widget.Panel("ratePanel", 
             { 
               width: "300px", 
               visible: false, 
               constraintoviewport: true ,
               fixedcenter : true,
               modal: true
             } 
              );
	ratePanel.render();
} );

var ratePanelCurr = null;
YAHOO.util.Event.onContentReady('ratePanelCurr', function() {
	var butRateSaveCurr = new YAHOO.widget.Button("butRateSaveCurr");
	butRateSaveCurr.on("click", rateSave);
	ratePanelCurr = new YAHOO.widget.Panel("ratePanelCurr", 
             { 
               width: "300px", 
               visible: false, 
               constraintoviewport: true ,
               fixedcenter : true,
               modal: true
             } 
              );
	ratePanelCurr.render();
} );

function rateModify(rate, id){
	var form = document.rateForm;
	if (!siteCurrencyClassDefault) {
		form = document.rateFormCurr;
	}
	form.containerId.value = id;
	document.getElementById('shippingRateFeeError').innerHTML = '';
	document.getElementById('shippingRatePercentageError').innerHTML = '';
	document.getElementById('shippingAdditionalRateFeeError').innerHTML = '';
	document.getElementById('shippingAdditionalRatePercentageError').innerHTML = '';
	form.shippingRateFee.value = '';
	form.shippingRatePercentage.value = '';
	form.shippingAdditionalRateFee.value = '';
	form.shippingAdditionalRatePercentage.value = '';
	if (!siteCurrencyClassDefault) {
		form.shippingRateFeeCurrFlag.checked = false;
		form.shippingRateFeeCurr.value = '';
		form.shippingRateFeeCurr_tmp.disabled = true;
		form.shippingRateFeeCurr.value = '';
		form.shippingAdditionalRateFeeCurrFlag.checked = false;
		form.shippingAdditionalRateFeeCurr.value = '';
		form.shippingAdditionalRateFeeCurr_tmp.disabled = true;
		form.shippingAdditionalRateFeeCurr_tmp.value = '';
	}

	if (siteCurrencyClassDefault) {
		ratePanel.show();
	}
	else {
		ratePanelCurr.show();
	}
	var url = '/${adminBean.contextPath}/admin/shipping/shippingMethodMaint.do';
	var postData = 'process=getJsonRate&';
	postData += 'shippingMethodId=' + rate.shippingMethodId + '&';
	postData += 'shippingRegionId=' + rate.shippingRegionId + '&';
	postData += 'shippingTypeId=' + rate.shippingTypeId + '&';
	postData += 'siteCurrencyClassId=' + siteCurrencyClassId;
/*
	postData += 'shippingRateFee=' + rate.shippingRateFee + '&';
	postData += 'shippingRatePercentage=' + rate.shippingRatePercentage + '&';
	postData += 'shippingAdditionalRateFee=' + rate.shippingAdditionalRateFee + '&';
	postData += 'shippingAdditionalRatePercentage=' + rate.shippingAdditionalRatePercentage;
*/
	
	var callback = {
		success: function(o) {
	        if (o.responseText == undefined) {
	            return;
	        }
	        var jsonObject = eval('(' + o.responseText + ')');
			form.jsonShippingRate.value = o.responseText;
			form.shippingRegionId.value = jsonObject.shippingRate.shippingRegionId;
			form.shippingMethodId.value = jsonObject.shippingRate.shippingMethodId;
			form.shippingTypeId.value = jsonObject.shippingRate.shippingTypeId;
			form.shippingRateFee.value = jsonObject.shippingRate.shippingRateFee;
			form.shippingRatePercentage.value = jsonObject.shippingRate.shippingRatePercentage;
			form.shippingAdditionalRateFee.value = jsonObject.shippingRate.shippingAdditionalRateFee;
			form.shippingAdditionalRatePercentage.value = jsonObject.shippingRate.shippingAdditionalRatePercentage;
			if (!siteCurrencyClassDefault) {
				form.shippingRateFeeCurr.value = jsonObject.shippingRate.shippingRateFeeCurr;
				if (jsonObject.shippingRate.shippingRateFeeCurrFlag) {
					form.shippingRateFeeCurrFlag.checked = true;
					form.shippingRateFeeCurr_tmp.value = jsonObject.shippingRate.shippingRateFeeCurr;
					form.shippingRateFeeCurr_tmp.disabled = false;
				}
				else {
					form.shippingRateFeeCurr_tmp.value = jsonObject.shippingRate.shippingRateFee;
				}
				form.shippingAdditionalRateFeeCurr.value = jsonObject.shippingRate.shippingAdditionalRateFeeCurr;
				if (jsonObject.shippingRate.shippingAdditionalRateFeeCurrFlag) {
					form.shippingAdditionalRateFeeCurrFlag.checked = true;
					form.shippingAdditionalRateFeeCurr_tmp.value = jsonObject.shippingRate.shippingAdditionalRateFeeCurr;
					form.shippingAdditionalRateFeeCurr_tmp.disabled = false;
				}
				else {
					form.shippingAdditionalRateFeeCurr_tmp.value = jsonObject.shippingRate.shippingAdditionalRateFee;
				}
				form.shippingAdditionalRateFeeCurr.value = jsonObject.shippingRate.shippingAdditionalRateFeeCurr;
			}
		},
		failure: function(o) {
	    	jc_panel_show_error("Unexcepted Error: " + jsonObject.message);
		}
	};
	var request = YAHOO.util.Connect.asyncRequest('POST', url, callback, postData);
}

function rateSave() {
	var form = document.rateForm;
	if (!siteCurrencyClassDefault) {
		form = document.rateFormCurr;
	}
    var jsonShippingRate = eval('(' + form.jsonShippingRate.value + ')');
	var url = '/${adminBean.contextPath}/admin/shipping/shippingMethodMaint.do';
	var postData = 'process=updateJsonRate&';
	postData += 'shippingMethodId=' + form.shippingMethodId.value + '&';
	postData += 'shippingRegionId=' + form.shippingRegionId.value + '&';
	postData += 'shippingTypeId=' + form.shippingTypeId.value + '&';
	postData += 'shippingRateFee=' + form.shippingRateFee.value + '&';
	postData += 'shippingRatePercentage=' + form.shippingRatePercentage.value + '&';
	postData += 'shippingAdditionalRateFee=' + form.shippingAdditionalRateFee.value + '&';
	postData += 'shippingAdditionalRatePercentage=' + form.shippingAdditionalRatePercentage.value;
	if (!siteCurrencyClassDefault) {
		postData += '&';
		postData += 'siteCurrencyClassId=' + siteCurrencyClassId + '&';
		postData += 'shippingRateFeeCurr=' + form.shippingRateFeeCurr_tmp.value + '&';
		postData += 'shippingRateFeeCurrFlag=' + form.shippingRateFeeCurrFlag.checked + '&';
		postData += 'shippingAdditionalRateFeeCurr=' + form.shippingAdditionalRateFeeCurr_tmp.value + '&';
		postData += 'shippingAdditionalRateFeeCurrFlag=' + form.shippingAdditionalRateFeeCurrFlag.checked;
	}
	var callback = {
		success: function(o) {
        	var jsonObject = eval('(' + o.responseText + ')');
	        if (jsonObject.status == 'failed') {
		        var element = null;
		        if (jsonObject.shippingRateFeeError) {
			        element = document.getElementById('shippingRateFeeError');
		        	element.appendChild(document.createElement('br'));
		        	element.appendChild(document.createTextNode(jsonObject.shippingRateFeeError));
		        }
		        if (jsonObject.shippingRatePercentageError) {
		        	element = document.getElementById('shippingRatePercentageError');
		        	element.appendChild(document.createElement('br'));
		        	element.appendChild(document.createTextNode(jsonObject.shippingRatePercentageError));
		        }
		        if (jsonObject.shippingAdditionalRateFeeError) {
		        	element = document.getElementById('shippingAdditionalRateFeeError');
		        	element.appendChild(document.createElement('br'));
		        	element.appendChild(document.createTextNode(jsonObject.shippingAdditionalRateFeeError));
		        }
		        if (jsonObject.shippingAdditionalRatePercentageError) {
		        	element = document.getElementById('shippingAdditionalRatePercentageError');
		        	element.appendChild(document.createElement('br'));
		        	element.appendChild(document.createTextNode(jsonObject.shippingAdditionalRatePercentageError));
		        }
	        }
	        else {
		        var containerId = form.containerId.value;
		        var container = document.getElementById(containerId);
		        var rate = new ShippingRate();
		        rate.shippingMethodId = form.shippingMethodId.value;
		        rate.shippingTypeId = form.shippingTypeId.value;
		        rate.shippingRegionId = form.shippingRegionId.value;
		        rate.shippingRateFee = jsonObject.shippingRateFee;
		        rate.shippingRatePercentage = jsonObject.shippingRatePercentage;
		        rate.shippingAdditionalRateFee = jsonObject.shippingAdditionalRateFee;
		        rate.shippingAdditionalRatePercentage = jsonObject.shippingAdditionalRatePercentage;
		        if (!siteCurrencyClassDefault) {
		        	rate.shippingRateFeeCurr = jsonObject.shippingRateFeeCurr;
		        	rate.shippingRateFeeCurrFlag = jsonObject.shippingRateFeeCurrFlag;
		        	rate.shippingAdditionalRateFeeCurr = jsonObject.shippingAdditionalRateFeeCurr;
		        	rate.shippingAdditionalRateFeeCurrFlag = jsonObject.shippingAdditionalRateFeeCurrFlag;
		        }
		        paintRate(container, rate);
		    	if (siteCurrencyClassDefault) {
			    	ratePanel.hide();
		    	}
		    	else {
		    		ratePanelCurr.hide();
		    	}
	        }
		},
		failure: function(o) {
	    	jc_panel_show_error("Unexcepted Error: " + jsonObject.message);
		}
	};
	var request = YAHOO.util.Connect.asyncRequest('POST', url, callback, postData);
}

</script>

<div id="ratePanel">
  <div class="hd">Rate maintenance</div>
  <div class="bd"> 
	<form name="rateForm" method="post" action="javascript:void(0)">
	<input type="hidden" name="containerId">
	<input type="hidden" name="jsonShippingRate">
	<input type="hidden" name="shippingMethodId">
	<input type="hidden" name="shippingTypeId">
	<input type="hidden" name="shippingRegionId">
	<div align="center">
	<table width="100" border="0" cellpadding="5" cellspacing="0">
	  <tr>
	    <td colspan="2">
	    <div align="right">
			<button type="button" id="butRateSave" name="butRateSave" value="Save">Save</button>
	    </div>
	    </td>
	  </tr>
	  <tr>
	    <td colspan="2" class="jc_input_label"><div align="left">First item</div></td>
	  </tr>
	  <tr>
	    <td valign="top"><div align="left">Rate</div></td>
	    <td>
	      <input type="text" name="shippingRateFee">
	      <div id="shippingRateFeeError" class="jc_input_error"></div>
	    </td>
	  </tr>
	  <tr>
	    <td valign="top"><div align="left">Percentage</div></td>
	    <td>
	      <input type="text" name="shippingRatePercentage">
	      <div id="shippingRatePercentageError" class="jc_input_error"></div>
	    </td>
	  </tr>
	  <tr>
	    <td colspan="2" class="jc_input_label"><div align="left">Additional items</div></td>
	  </tr>
	  <tr>
	    <td valign="top"><div align="left">Rate</div></td>
	    <td>
	      <input type="text" name="shippingAdditionalRateFee">
	      <div id="shippingAdditionalRateFeeError" class="jc_input_error"></div>
	    </td>
	  </tr>
	   <tr>
	    <td valign="top"><div align="left">Percentage</div></td>
	    <td>
	      <input type="text" name="shippingAdditionalRatePercentage">
	      <div id="shippingAdditionalRatePercentageError" class="jc_input_error"></div>
	    </td>
	  </tr>
	</table>
	<br>
	<br>
	</div>
	</form>
  </div>
</div>

<div id="ratePanelCurr">
  <div class="hd">Rate maintenance</div>
  <div class="bd"> 
	<form name="rateFormCurr" method="post" action="javascript:void(0)">
	<input type="hidden" name="containerId">
	<input type="hidden" name="jsonShippingRate">
	<input type="hidden" name="shippingMethodId">
	<input type="hidden" name="shippingTypeId">
	<input type="hidden" name="shippingRegionId">
	<div align="center">
	<table width="100" border="0" cellpadding="5" cellspacing="0">
	  <tr>
	    <td colspan="2">
	    <div align="right">
			<button type="button" id="butRateSaveCurr" name="butRateSave" value="Save">Save</button>
	    </div>
	    </td>
	  </tr>
	  <tr>
	    <td colspan="2" class="jc_input_label"><div align="left">First item</div></td>
	  </tr>
	  <tr>
	    <td colspan="2" class="jc_input_label">
	      <div align="left">
	      Override currency
	      <input type="checkbox" onclick="return overrideCurrency(rateFormCurr.shippingRateFeeCurr_tmp,rateFormCurr.shippingRateFee, this);" value="on" name="shippingRateFeeCurrFlag"/>
	      </div>
	    </td>
	  </tr>
	  <tr>
	    <td><div align="left">Rate</div></td>
	    <td>
	      <div align="left">
	      <input type="hidden" value="" name="shippingRateFee"/>
	      <input type="hidden" value="" name="shippingRateFeeCurr"/>
	      <input type="text" name="shippingRateFeeCurr_tmp" disabled="disabled">
	      <div id="shippingRateFeeError" class="jc_input_error"></div>
	      </div>
	    </td>
	  </tr>
	  <tr>
	    <td><div align="left">Percentage</div></td>
	    <td>
	      <div align="left">
	      <input type="text" name="shippingRatePercentage" readonly="readonly">
	      <div id="shippingRatePercentageError" class="jc_input_error"></div>
	      </div>
	    </td>
	  </tr>
	  <tr>
	    <td colspan="2" class="jc_input_label"><div align="left">Additional items</div></td>
	  </tr>
	  <tr>
	    <td colspan="2" class="jc_input_label">
	      <div align="left">
	      Override currency
	      <input type="checkbox" onclick="return overrideCurrency(rateFormCurr.shippingAdditionalRateFeeCurr_tmp,rateFormCurr.shippingAdditionalRateFee, this);" value="on" name="shippingAdditionalRateFeeCurrFlag"/>
	      </div>
	    </td>
	  </tr>
	  <tr>
	    <td><div align="left">Rate</div></td>
	    <td>
	      <div align="left">
	      <input type="hidden" value="" name="shippingAdditionalRateFee"/>
	      <input type="hidden" value="" name="shippingAdditionalRateFeeCurr"/>
	      <input type="text" name="shippingAdditionalRateFeeCurr_tmp" disabled="disabled">
	      <div id="shippingAdditionalRateFeeError" class="jc_input_error"></div>
	      </div>
	    </td>
	  </tr>
	   <tr>
	    <td><div align="left">Percentage</div></td>
	    <td>
	      <div align="left">
	      <input type="text" name="shippingAdditionalRatePercentage" readonly="readonly">
	      <div id="shippingAdditionalRatePercentageError" class="jc_input_error"></div>
	      </div>
	    </td>
	  </tr>
	</table>
	<br>
	<br>
	</div>
	</form>
  </div>
</div>
<!-- ------------------------------------------------ -->

<%@ include file="/admin/include/confirm.jsp" %>
<%@ include file="/admin/include/panel.jsp" %>