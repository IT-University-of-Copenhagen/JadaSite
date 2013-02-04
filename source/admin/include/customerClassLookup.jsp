<script type="text/javascript">
var jc_customerClass_search_panel = null;
function jc_customerClass_search_init() {
	jc_customerClass_search_panel = new YAHOO.widget.Panel("jc_customerClass_search_panel", 
	                             { 
	                               width: "300px", 
	                               visible: false, 
	                               constraintoviewport: true ,
	                               fixedcenter : true,
	                               modal: true
	                             } 
	);
	jc_customerClass_search_panel.render();
}

YAHOO.util.Event.onDOMReady(jc_customerClass_search_init);

function jc_customerClass_search_show() {
    var customerClassContainer = document.getElementById('customerClass_container');
    customerClassContainer.innerHTML = '';
	jc_customerClass_search_panel.show();
	jc_customerClass_search_get();
}

function jc_customerClass_search_finish() {
	jc_customerClass_search_panel.hide();
}

var jc_customerClass_search_callback = {
	success: function(o) {
	    if (o.responseText == undefined) {
	        return;
	    }
	    var customerClassContainer = document.getElementById('customerClass_container');
	    customerClassContainer.innerHTML = '';
	    var jsonObject = eval('(' + o.responseText + ')');
	    if (jsonObject.customerClasses.length == 0) {
		    customerClassContainer.appendChild(document.createTextNode('No record found'));
	    }
	    else {
		    for (var i = 0; i < jsonObject.customerClasses.length; i++) {
			    var customerClass = jsonObject.customerClasses[i];
			    var anchor = document.createElement('a');
			    var href = 'javascript:jc_customerClass_search_callback_single(' + customerClass.custClassId + ')';
			    anchor.setAttribute('href', href);
			    anchor.appendChild(document.createTextNode(customerClass.custClassName));
			    customerClassContainer.appendChild(anchor);
			    customerClassContainer.appendChild(document.createElement('br'));
		    }
	    }
	},
	failure: function(o) {
		jc_panel_show_message('Error showing product class search');
	}
};

function jc_customerClass_search_get() {
  var url = "/${adminBean.contextPath}/admin/lookup/customerClassLookup.do";
  var postData = ""
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_customerClass_search_callback, postData);
}

function jc_customerClass_search_callback_single(custClassId) {
  var jsonResult = '{"custClassId": ' + custClassId + '}';
  var result = eval('(' + jsonResult + ')');
  jc_customerClass_search_client_callback(result);
}

</script>
<div class=" yui-skin-sam">
<div>
<div id="jc_customerClass_search_panel">
  <div class="hd">Customer Class search</div>
  <div class="bd"> 
    <div id="jc_customerClass_search_message" class="jc_input_error"></div>
	<div id="customerClass_container"></div>
  </div>
</div>
</div>
</div>
