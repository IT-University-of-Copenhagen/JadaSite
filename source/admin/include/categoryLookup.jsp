<script type="text/javascript">
/*
Category selection pop-up window
*/
var jc_category_search_panel;
function jc_category_search_init() {
  jc_category_search_panel = new YAHOO.widget.Panel("jc_category_search_panel", 
                              { 
                                width: "500px", 
                                visible: false, 
                                constraintoviewport: true ,
                                fixedcenter : true,
                                modal: true
                              } 
                               );
  jc_category_search_panel.render();
}
function jc_category_search_show() {
  var container = document.getElementById("jc_category_search_tree");
  container.innerHTML = "";
  jc_category_search_get();
  jc_category_search_panel.show();
}
function jc_category_search_finish() {
  jc_category_search_panel.hide();
}
function jc_category_search_message(message) {
  var message = document.getElementById("jc_category_search_message");
    message.innerHTML = message;
}
YAHOO.util.Event.onDOMReady(jc_category_search_init);

function jc_category_search_addnode(tree, jsonObject) {
  var node = new YAHOO.widget.TextNode(jsonObject.catShortTitle, tree, false);
  node.labelElId = jsonObject.catId;
  var i = 0;
  for (i = 0; i < jsonObject.categories.length; i++) {
    jc_category_search_addnode(node, jsonObject.categories[i]);
  }
}

var jc_category_search_callback =
{
  success: function(o) {
    if (o.responseText == undefined) {
      return;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    var tree = new YAHOO.widget.TreeView("jc_category_search_tree");
    var root = tree.getRoot();
  
    var i = 0;
    for (i = 0; i < jsonObject.categories.length; i++) {
      jc_category_search_addnode(root, jsonObject.categories[i]);
    }
    tree.draw();
    tree.subscribe("clickEvent", function(oArgs) {
		node = oArgs.node;
        jc_category_search_callback_single(node.labelElId, node.label);
        return false;
	});
  },
  failure: function(o) {
    var message = document.getElementById("jc_category_search_message");
    message.innerHTML = "Error retrieving category list";
  }
};

function jc_category_search_get() {
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/lookup/categoryLookup.do";
  var request = YAHOO.util.Connect.asyncRequest('GET', url, jc_category_search_callback);
}

function jc_category_search_callback_single(catId, catShortTitle) {
  var result = '{"categories": [';
  result += '{"catId": "' + catId + '", "catShortTitle": "' + catShortTitle + '"}';
  result += ']}';
  jc_category_search_client_callback(result);
}
</script>
<div class=" yui-skin-sam">
<div>
<div id="jc_category_search_panel">
  <div class="hd">Category search</div>
  <div class="bd"> 
    <div id="jc_category_search_message" class="jc_input_error"></div>
    <div id="jc_category_search_tree" style="height: 200px; overflow: auto; vertical-align: top">
    
    </div>
  </div>
</div>
</div>
</div>
