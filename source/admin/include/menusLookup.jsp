<script type="text/javascript" src="/${adminBean.contextPath}/admin/web/utility/taskNode.js"></script>
<script type="text/javascript">
/*
Category selection pop-up window
*/
var jc_menus_search_panel;
var jc_menu_trees = new Array(0);
function jc_menus_search_init() {
  jc_menus_search_panel = new YAHOO.widget.Panel("jc_menus_search_panel", 
                              { 
                                width: "500px", 
                                visible: false, 
                                constraintoviewport: true ,
                                fixedcenter : true,
                                modal: true
                              } 
                               );
  jc_menus_search_panel.render();
}
function jc_menus_search_show(siteDomainId) {
  var container = document.getElementById("jc_menus_search_tree");
  container.innerHTML = "";
  jc_menus_search_get(siteDomainId);
  jc_menus_search_panel.show();
}
function jc_menus_search_finish() {
  jc_menus_search_panel.hide();
}
function jc_menus_search_message(message) {
  var message = document.getElementById("jc_menus_search_message");
    message.innerHTML = message;
}
YAHOO.util.Event.onDOMReady(jc_menus_search_init);

function jc_menus_search_addnode(tree, jsonObject) {
  var url = "javascript:void(0);";
  var node;
  if (jsonObject.menuSetName) {
	node = new YAHOO.widget.TextNode(jsonObject.menuSetName, tree, false);
  }
  else {
	node = new YAHOO.widget.TaskNode(jsonObject.menuName, jsonObject.menuId, tree, false);
  }
/*
  if (jsonObject.menuId) {
alert(jsonObject.menuId);
alert(jsonObject.menuName);
	node = new YAHOO.widget.TaskNode(jsonObject.menuName, jsonObject.menuId, tree, false);
alert('added');
  }
  else {
alert(jsonObject.menuSetName);
	node = new YAHOO.widget.TextNode(jsonObject.menuSetName, '', tree, false);
  }
*/
  for (var i = 0; i < jsonObject.menus.length; i++) {
    jc_menus_search_addnode(node, jsonObject.menus[i]);
  }
}

var jc_menus_search_callback =
{
  success: function(o) {
    if (o.responseText == undefined) {
      return;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    var container = document.getElementById("jc_menus_search_tree");
    for (var i = 0; i < jsonObject.menuSets.length; i++) {
      var ndiv = document.createElement('div');
      var id = 'menuSet' + i;
      ndiv.setAttribute('id', id);
      container.appendChild(ndiv);
      var tree = new YAHOO.widget.TreeView(id);
      var root = tree.getRoot();
      jc_menus_search_addnode(root, jsonObject.menuSets[i]);
      tree.draw();
      jc_menu_trees.push(tree);
    }
  },
  failure: function(o) {
    var message = document.getElementById("jc_menus_search_message");
    message.innerHTML = "Error retrieving menu list";
  }
};

function jc_menus_search_get(siteDomainId) {
  var url = "/${adminBean.contextPath}/admin/lookup/menusLookup.do?siteDomainId=" + siteDomainId;
  var request = YAHOO.util.Connect.asyncRequest('GET', url, jc_menus_search_callback);
}

function jc_menus_search_callback_multiple() {
  var e = document.getElementById("jc_menus_search_tree");
  var list = new Array();
  jc_traverse_element(e, list, 'INPUT', 'menuIds.*');

  var menuWindowMode = document.getElementById("menuWindowMode").value;
  var menuWindowTarget = document.getElementById("menuWindowTarget").value;
  var result = '{"menus": [';

  var count = 0;
  for (var i = 0; i < jc_menu_trees.length; i++) {
	  var checkedNodes = jc_menus_get_checked_nodes(jc_menu_trees[i].getRoot().children);
	  for (var j = 0; j < checkedNodes.length; j++) {
	      	if (count > 0) {
	          result += ', ';
			}
			result += '{"menuId": "' + checkedNodes[j] + '"}';
			count++;
	  }
  }
/*
  var count = 0;
  for (var i = 0; i < list.length; i++) {
    if (list[i].checked) {
      if (count > 0) {
        result += ', ';
      }
      result += '{"menuId": "' + list[i].value + '"}';
      count++;
    }
  }
*/
  result += ']';
  result += ', "menuWindowMode": "' + menuWindowMode + '", "menuWindowTarget": "' + menuWindowTarget + '"';
  result += '}';
  jc_menus_search_client_callback(result);
}

function jc_menus_get_checked_nodes(nodes) {
	checkedNodes = [];
    for(var i=0, l=nodes.length; i<l; i=i+1) {
        var n = nodes[i];
        if (n.checkState === 2) {
            checkedNodes.push(n.key); 
        }
        if (n.hasChildren()) {
			checkedNodes = checkedNodes.concat(jc_menus_get_checked_nodes(n.children));
        }
    }
    return checkedNodes;
}

</script>
<div class=" yui-skin-sam">
<div>
<div id="jc_menus_search_panel">
  <div class="hd">Menus search</div>
  <div class="bd"> 
    <div id="jc_menus_search_message" class="jc_input_error"></div>
    <div id="jc_menus_search_tree" style="height: 200px; overflow: auto; vertical-align: top">
    All menus
    </div>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr> 
        <td class="jc_input_label" width="100%">&nbsp;</td>
      </tr>
      <tr> 
        <td class="jc_input_label" width="100%">Window Mode</td>
      </tr>
      <tr> 
        <td class="jc_input_control"> 
          <input id="menuWindowMode" class="tableContent" type="text" value="" size="40" name="menuWindowMode" style="tableContent"/>
        </td>
      </tr>
      <tr> 
        <td class="jc_input_label">Window Target</td>
      </tr>
      <tr> 
        <td class="jc_input_control" > 
          <select id="menuWindowTarget" class="tableContent" name="menuWindowTarget">
            <option value="_self">Current window (_self)</option>
            <option value="_blank">New window (_blank)</option>
            <option value="_parent">Parent Window (_parent)</option>
            <option value="_top">Top of the frameset (_top)</option>
          </select>
        </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
      <tr> 
        <td class="jc_input_control" > 
          <a href="javascript:void(0);" onclick="jc_menus_search_callback_multiple();" class="jc_navigation_link">Confirm</a>
          <a href="javascript:void(0);" onclick="jc_menus_search_panel.hide()" class="jc_navigation_link">Cancel</a>&nbsp;
        </td>
      </tr>
    </table>
  </div>
</div>
</div>
</div>
