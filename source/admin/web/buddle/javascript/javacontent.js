function jc_tab_toggle(control_id, id) {
	var obj = document.getElementById(id);
	var control = document.getElementById(control_id);
	if (obj.className == "jc_tab_content_show") {
		obj.className = "jc_tab_content_hide";
		control.innerHTML = "+";
	}
	else {
		obj.className = "jc_tab_content_show";
		control.innerHTML = "-";
	}
}

function jc_tab_expand(control_id, id) {
	var obj = document.getElementById(id);
	var control = document.getElementById(control_id);
	obj.className = "jc_tab_content_show";
	control.innerHTML = "-";
}

function jc_tab_collapse_all() {
	var i = 0;
	while (true) {
		var control = document.getElementById("click_" + i);
		if (control == null) {
			break;
		}
		var obj = document.getElementById("tab_" + i);
		obj.className = "jc_tab_content_hide";
		control.innerHTML = "+";
		i = i + 1;
	}
}

function jc_tab_expand_all() {
	var i = 0;
	while (true) {
		var control = document.getElementById("click_" + i);
		if (control == null) {
			break;
		}
		var obj = document.getElementById("tab_" + i);
		obj.className = "jc_tab_content_show";
		control.innerHTML = "-";
		i = i + 1;
	}
}

function jc_create_yui_treemenu(id, location) {
      var obj = document.getElementById(id);
      tree = new YAHOO.widget.TreeView(location);
//      var parent = new YAHOO.widget.TextNode(label, tree.getRoot(), true);
      jc_create_yui_treemenu_items(tree.getRoot(), obj);
      tree.draw();
}

function jc_create_yui_treemenu_items(parent, source) {
       var li_nodes = source.childNodes;
       for (var i = 0; i < li_nodes.length; i++) {
            var li_node = li_nodes[i];
			var child_nodes = li_node.childNodes;
			for (var j = 0; j < child_nodes.length; j++) {
				var child_node = child_nodes[j];
				var tree_node;
				if (child_node.tagName == 'DIV') {
					tree_node = new YAHOO.widget.TextNode(child_node.innerHTML, parent, false);
					child_node.innerHTML = "";
				}
				else if (child_node.tagName == 'UL') {
					jc_create_yui_treemenu_items(tree_node, child_node);
				}
			}
      }
}

function jc_calendar_callback(type, args, obj) {
    var dates = args[0]; 
    var date = dates[0]; 
    var year = date[0], month = date[1] + "", day = date[2] + "";
    if (month.length == 1) month = "0" + month;
    if (day.length == 1) day = "0" + day;
    obj.hide();
    return day + "-" + month + "-" + year;
}
