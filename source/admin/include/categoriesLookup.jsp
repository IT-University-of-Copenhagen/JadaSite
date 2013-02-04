<script type="text/javascript" src="/${adminBean.contextPath}/admin/web/utility/taskNode.js"></script>
<script type="text/javascript">
/*
Category selection pop-up window
*/
var jc_category_search_panel;
var jc_category_trees = new Array();
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
  var node = new YAHOO.widget.TaskNode(jsonObject.catShortTitle, jsonObject.catId, tree, false);
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
    jc_category_trees.push(tree);
  },
  failure: function(o) {
    var message = document.getElementById("jc_category_search_message");
    message.innerHTML = "Error retrieving category list";
  }
};

function jc_category_search_get() {
  var url = "/${adminBean.contextPath}/admin/lookup/categoryLookup.do";
  var request = YAHOO.util.Connect.asyncRequest('GET', url, jc_category_search_callback);
}

function jc_category_search_callback_multiple() {
  var count = 0;
  var result = '{"categories": [';
  for (var i = 0; i < jc_category_trees.length; i++) {
	  var checkedNodes = jc_categories_get_checked_nodes(jc_category_trees[i].getRoot().children);
	  for (var j = 0; j < checkedNodes.length; j++) {
	      	if (count > 0) {
	          result += ', ';
			}
			result += '{"catId": "' + checkedNodes[j] + '"}';
			count++;
	  }
  }
  result += ']';
  result += '}';
  jc_category_search_client_callback(result);
}

function jc_categories_get_checked_nodes(nodes) {
	checkedNodes = [];
    for(var i=0, l=nodes.length; i<l; i=i+1) {
        var n = nodes[i];
        if (n.checkState === 2) {
            checkedNodes.push(n.key); 
        }
        if (n.hasChildren()) {
			checkedNodes = checkedNodes.concat(jc_categories_get_checked_nodes(n.children));
        }
    }
    return checkedNodes;
}

/*
function jc_category_search_callback_single(catId, catShortTitle) {
  var result = '{"categories": [';
  result += '{"catId": "' + catId + '", "catShortTitle": "' + catShortTitle + '"}';
  result += ']}';
  jc_category_search_client_callback(result);
}
*/

YAHOO.util.Event.onContentReady('jc_category_search_button', function() {
    var butMenuConfirm = new YAHOO.widget.Button({label: 'Confirm',
        id: 'menuConfirm',
        disabled: false,
        container: 'jc_category_search_button' });
	butMenuConfirm.on('click', function() {
		jc_category_search_callback_multiple();
	} );
    
    var butMenuCancel = new YAHOO.widget.Button({label: 'Cancel',
        id: 'butMenuCancel',
        disabled: false,
        container: 'jc_category_search_button' });
    butMenuCancel.on('click', function() {
    	jc_category_search_finish();
    } ); 
} );
</script>
<div class=" yui-skin-sam">
<div>
<div id="jc_category_search_panel">
  <div class="hd">Category search</div>
  <div class="bd"> 
    <div id="jc_category_search_button" align="right"></div>
    <div id="jc_category_search_message" class="jc_input_error"></div>
    <div id="jc_category_search_tree" style="height: 200px; overflow: auto; vertical-align: top">
    
    </div>
  </div>
  <div id="jc_category_search_panel_footer" class="hd">
  </div>
</div>
</div>
</div>
