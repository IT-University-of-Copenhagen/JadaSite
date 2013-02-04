<script type="text/javascript">
var jc_customAttribute_search_panel = null;
var jc_customAttributeSearchCallBack = null;
var jc_customAttributeSearchType = null;
function jc_customAttribute_search_init() {
	jc_customAttribute_search_panel = new YAHOO.widget.Panel("jc_customAttribute_search_panel", 
	                             { 
	                               width: "300px", 
	                               visible: false, 
	                               constraintoviewport: true ,
	                               fixedcenter : true,
	                               modal: true
	                             } 
	);
	jc_customAttribute_search_panel.render();
}

YAHOO.util.Event.onDOMReady(jc_customAttribute_search_init);

function jc_customAttribute_search_show(callback, type) {
    var customAttributeContainer = document.getElementById('customAttribute_container');
    jc_customAttributeSearchType = type;
    jc_customAttributeSearchCallBack = callback;
    customAttributeContainer.innerHTML = '';
	jc_customAttribute_search_panel.show();
	jc_customAttribute_search_get();
}

function jc_customAttribute_search_finish() {
	jc_customAttribute_search_panel.hide();
}

var jc_customAttribute_search_callback = {
	success: function(o) {
	    if (o.responseText == undefined) {
	        return;
	    }
	    var customAttributeContainer = document.getElementById('customAttribute_container');
	    customAttributeContainer.innerHTML = '';
	    var jsonObject = eval('(' + o.responseText + ')');
	    if (jsonObject.customAttributes.length == 0) {
	    	customAttributeContainer.appendChild(document.createTextNode('No record found'));
	    	return;
	    }
	    for (var i = 0; i < jsonObject.customAttributes.length; i++) {
		    var customAttribute = jsonObject.customAttributes[i];
		    var anchor = document.createElement('a');
		    var href = 'javascript:jc_customAttribute_search_callback_single(' + customAttribute.customAttribId + ')';
		    anchor.setAttribute('href', href);
		    anchor.appendChild(document.createTextNode(customAttribute.customAttribName));
		    customAttributeContainer.appendChild(anchor);
		    customAttributeContainer.appendChild(document.createElement('br'));
	    }
	},
	failure: function(o) {
		jc_panel_show_message('Error showing custom attribute search');
	}
};

function jc_customAttribute_search_get() {
  var url = "/${adminBean.contextPath}/admin/lookup/customAttributeLookup.do?type=" + jc_customAttributeSearchType;
  var postData = ""
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_customAttribute_search_callback, postData);
}

function jc_customAttribute_search_callback_single(customAttribId) {
  var jsonResult = '{"customAttribId": ' + customAttribId + '}';
  var result = eval('(' + jsonResult + ')');
  jc_customAttributeSearchCallBack(result);
}

</script>
<div class=" yui-skin-sam">
<div>
<div id="jc_customAttribute_search_panel">
  <div class="hd">Custom Attribute search</div>
  <div class="bd"> 
    <div id="jc_customAttribute_search_message" class="jc_input_error"></div>
	<div id="customAttribute_container" style="height: 500px; overflow-y: scroll"></div>
  </div>
</div>
</div>
</div>
