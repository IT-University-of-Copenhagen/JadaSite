<script type="text/javascript" src="/${adminBean.contextPath}/admin/web/utility/taskNode.js"></script>
<script type="text/javascript">
/*
Section selection pop-up window
*/
var jc_section_search_panel;
var jc_section_trees = new Array();
function jc_section_search_init() {
  jc_section_search_panel = new YAHOO.widget.Panel("jc_section_search_panel", 
                              { 
                                width: "500px", 
                                visible: false, 
                                constraintoviewport: true ,
                                fixedcenter : true,
                                modal: true
                              } 
                               );
  jc_section_search_panel.render();
}
function jc_section_search_show() {
  var container = document.getElementById("jc_section_search_tree");
  container.innerHTML = "";
  jc_section_search_get();
  jc_section_search_panel.show();
}
function jc_section_search_finish() {
  jc_section_search_panel.hide();
}
function jc_section_search_message(message) {
  var message = document.getElementById("jc_section_search_message");
    message.innerHTML = message;
}
YAHOO.util.Event.onDOMReady(jc_section_search_init);

function jc_section_search_addnode(tree, jsonObject) {
  var node = new YAHOO.widget.TaskNode(jsonObject.sectionShortTitle, jsonObject.sectionId, tree, false);
  var i = 0;
  for (i = 0; i < jsonObject.sections.length; i++) {
    jc_section_search_addnode(node, jsonObject.sections[i]);
  }
}

var jc_section_search_callback =
{
  success: function(o) {
    if (o.responseText == undefined) {
      return;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    var tree = new YAHOO.widget.TreeView("jc_section_search_tree");
    var root = tree.getRoot();
  
    var i = 0;
    for (i = 0; i < jsonObject.sections.length; i++) {
      jc_section_search_addnode(root, jsonObject.sections[i]);
    }
    tree.draw();
    jc_section_trees.push(tree);
  },
  failure: function(o) {
    var message = document.getElementById("jc_section_search_message");
    message.innerHTML = "Error retrieving section list";
  }
};

function jc_section_search_get() {
  var url = "/${adminBean.contextPath}/admin/lookup/sectionLookup.do";
  var request = YAHOO.util.Connect.asyncRequest('GET', url, jc_section_search_callback);
}

function jc_section_search_callback_multiple() {
  var count = 0;
  var result = '{"sections": [';
  for (var i = 0; i < jc_section_trees.length; i++) {
	  var checkedNodes = jc_sections_get_checked_nodes(jc_section_trees[i].getRoot().children);
	  for (var j = 0; j < checkedNodes.length; j++) {
	      	if (count > 0) {
	          result += ', ';
			}
			result += '{"sectionId": "' + checkedNodes[j] + '"}';
			count++;
	  }
  }
  result += ']';
  result += '}';
  jc_section_search_client_callback(result);
}

function jc_sections_get_checked_nodes(nodes) {
	checkedNodes = [];
    for(var i=0, l=nodes.length; i<l; i=i+1) {
        var n = nodes[i];
        if (n.checkState === 2) {
            checkedNodes.push(n.key); 
        }
        if (n.hasChildren()) {
			checkedNodes = checkedNodes.concat(jc_sections_get_checked_nodes(n.children));
        }
    }
    return checkedNodes;
}

/*
function jc_section_search_callback_single(sectionId, sectionShortTitle) {
  var result = '{"sections": [';
  result += '{"sectionId": "' + sectionId + '", "sectionShortTitle": "' + sectionShortTitle + '"}';
  result += ']}';
  jc_section_search_client_callback(result);
}
*/

YAHOO.util.Event.onContentReady('jc_section_search_button', function() {
    var butMenuConfirm = new YAHOO.widget.Button({label: 'Confirm',
        id: 'menuConfirm',
        disabled: false,
        container: 'jc_section_search_button' });
	butMenuConfirm.on('click', function() {
		jc_section_search_callback_multiple();
	} );
    
    var butMenuCancel = new YAHOO.widget.Button({label: 'Cancel',
        id: 'butMenuCancel',
        disabled: false,
        container: 'jc_section_search_button' });
    butMenuCancel.on('click', function() {
    	jc_section_search_finish();
    } ); 
} );
</script>
<div class=" yui-skin-sam">
<div>
<div id="jc_section_search_panel">
  <div class="hd">Section search</div>
  <div class="bd"> 
    <div id="jc_section_search_button" align="right"></div>
    <div id="jc_section_search_message" class="jc_input_error"></div>
    <div id="jc_section_search_tree" style="height: 200px; overflow: auto; vertical-align: top">
    
    </div>
  </div>
  <div id="jc_section_search_panel_footer" class="hd">
  </div>
</div>
</div>
</div>
