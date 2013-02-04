<script type="text/javascript">
var jc_tax_search_panel = null;
function jc_tax_search_init() {
	jc_tax_search_panel = new YAHOO.widget.Panel("jc_tax_search_panel", 
	                             { 
	                               width: "500px", 
	                               visible: false, 
	                               constraintoviewport: true ,
	                               fixedcenter : true,
	                               modal: true
	                             } 
	);
	jc_tax_search_panel.render();
}

YAHOO.util.Event.onDOMReady(jc_tax_search_init);

function jc_tax_search_show() {
    var taxContainer = document.getElementById('tax_container');
    taxContainer.innerHTML = '';
	jc_tax_search_panel.show();
	jc_tax_search_get();
}

function jc_tax_search_finish() {
	jc_tax_search_panel.hide();
}

var jc_tax_search_callback = {
	success: function(o) {
	    if (o.responseText == undefined) {
	        return;
	    }
	    var taxContainer = document.getElementById('tax_container');
	    taxContainer.innerHTML = '';
	    var jsonObject = eval('(' + o.responseText + ')');

	    var table = document.createElement('table');
//	    table.setAttribute('width', '100%');
	    taxContainer.appendChild(table);
	    var tableBody = document.createElement("tbody");
	    table.appendChild(tableBody);
	    if (jsonObject.taxes.length == 0) {
	    	var row = document.createElement('tr');
		    tableBody.appendChild(row);
		    var col = document.createElement('td');
		    col.colspan = 3;
		    row.appendChild(col);
		    col.appendChild(document.createTextNode('No record found'));
	    }
	    else {
		    for (var i = 0; i < jsonObject.taxes.length; i++) {
			    var tax = jsonObject.taxes[i];
			    var anchor = document.createElement('a');
			    var href = 'javascript:jc_tax_search_callback_single(' + tax.taxId + ')';
			    anchor.setAttribute('href', href);
			    anchor.appendChild(document.createTextNode(tax.taxName));
				var row = document.createElement('tr');
			    tableBody.appendChild(row);
			    var col = document.createElement('td');
			    col.setAttribute('width', '200');
			    row.appendChild(col);
			    col.appendChild(anchor);
			    col = document.createElement('td');
			    row.appendChild(col);
			    col.setAttribute('width', '200');
			    col.appendChild(document.createTextNode(tax.taxName));
			    col = document.createElement('td');
			    col.setAttribute('width', '100');
			    row.appendChild(col);
			    col.appendChild(document.createTextNode(tax.taxRate));
		    }
	    }
	},
	failure: function(o) {
		jc_panel_show_message('Error showing tax search');
	}
};

function jc_tax_search_get() {
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/lookup/taxLookup.do";
	var postData = ""
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_tax_search_callback, postData);
}

function jc_tax_search_callback_single(taxId) {
	var jsonResult = '{"taxId": ' + taxId + '}';
	var result = eval('(' + jsonResult + ')');
	jc_tax_search_client_callback(result);
}

</script>
<div class=" yui-skin-sam">
	<div>
		<div id="jc_tax_search_panel">
		  <div class="hd">Tax search</div>
		  <div class="bd"> 
		    <div id="jc_tax_search_message" class="jc_input_error"></div>
			<div id="tax_container"></div>
		  </div>
		</div>
	</div>
</div>
