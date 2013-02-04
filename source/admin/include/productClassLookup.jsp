<script type="text/javascript">
var jc_productClass_search_panel = null;
function jc_productClass_search_init() {
	jc_productClass_search_panel = new YAHOO.widget.Panel("jc_productClass_search_panel", 
	                             { 
	                               width: "300px", 
	                               visible: false, 
	                               constraintoviewport: true ,
	                               fixedcenter : true,
	                               modal: true
	                             } 
	);
	jc_productClass_search_panel.render();
}

YAHOO.util.Event.onDOMReady(jc_productClass_search_init);

function jc_productClass_search_show() {
    var productClassContainer = document.getElementById('productClass_container');
    productClassContainer.innerHTML = '';
	jc_productClass_search_panel.show();
	jc_productClass_search_get();
}

function jc_productClass_search_finish() {
	jc_productClass_search_panel.hide();
}

var jc_productClass_search_callback = {
	success: function(o) {
	    if (o.responseText == undefined) {
	        return;
	    }
	    var productClassContainer = document.getElementById('productClass_container');
	    productClassContainer.innerHTML = '';
	    var jsonObject = eval('(' + o.responseText + ')');
	    if (jsonObject.productClasses.length == 0) {
		    productClassContainer.appendChild(document.createTextNode('No record found'));
	    }
	    else {
		    for (var i = 0; i < jsonObject.productClasses.length; i++) {
			    var productClass = jsonObject.productClasses[i];
			    var anchor = document.createElement('a');
			    var href = 'javascript:jc_productClass_search_callback_single(' + productClass.productClassId + ')';
			    anchor.setAttribute('href', href);
			    anchor.appendChild(document.createTextNode(productClass.productClassName));
			    productClassContainer.appendChild(anchor);
			    productClassContainer.appendChild(document.createElement('br'));
		    }
	    }
	},
	failure: function(o) {
		jc_panel_show_message('Error showing product class search');
	}
};

function jc_productClass_search_get() {
	var url = "<c:out value='/${adminBean.contextPath}'/>/admin/lookup/productClassLookup.do";
	var postData = ""
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_productClass_search_callback, postData);
}

function jc_productClass_search_callback_single(productClassId) {
	var jsonResult = '{"productClassId": ' + productClassId + '}';
	var result = eval('(' + jsonResult + ')');
	jc_productClass_search_client_callback(result);
}

</script>
<div class=" yui-skin-sam">
<div>
<div id="jc_productClass_search_panel">
  <div class="hd">Product Class search</div>
  <div class="bd"> 
    <div id="jc_productClass_search_message" class="jc_input_error"></div>
	<div id="productClass_container"></div>
  </div>
</div>
</div>
</div>
