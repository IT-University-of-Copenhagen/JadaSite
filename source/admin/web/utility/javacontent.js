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
    return month + "/" + day + "/" + year;
}

function jc_traverse_element(element, list, tagName, re) {
  var size = list.length;
  if (element.tagName) {
	  if (element.tagName.toLowerCase() == tagName.toLowerCase()) {
	    if (re) {
	    	if (element.name.match(re)) {
	    		list[size++] = element;
	    	}
/*
	      var index = element.name.toLowerCase().indexOf(name.toLowerCase());
	      if (index == 0) {
	        list[size++] = element;
	      }
*/
	    }
	  }
  }
  var children = element.childNodes;
  for (var j = 0; j < children.length; j++) {
    jc_traverse_element(children[j], list, tagName, re);
  }
  return list;
}

function isJsonResponseValid(responseText) {
  try {
    if (responseText == undefined) {
      return false;
    }
    var jsonObject = eval('(' + responseText + ')');
    return true;
  }
  catch (e) {
    return false;
  }
  return true;
}

function isJsonResponseSuccess(responseText) {
  try {
    if (responseText == undefined) {
      return false;
    }
    var jsonObject = eval('(' + responseText + ')');
    if (jsonObject.status != 'success') {
      return false;
    }
  }
  catch (e) {
    return false;
  }
  return true;
}

function getJsonObject(responseText) {
    var jsonObject = eval('(' + responseText + ')');
	return jsonObject;
}

function showId(id) {
  var tab = document.getElementById(id);
  tab.style.display = 'block';
}

function hideId(id) {
  var tab = document.getElementById(id);
  tab.style.display = 'none';
}

function setIdValue(id, value) {
	var element = document.getElementById(id);
	element.innerHTML = value;
}

function isSet(variable)
{
	return(typeof(variable) != 'undefined');
}

/*
 *  Helper method to render Yahoo text editor
 */
function jc_render_texteditor(context, myHeight, myWidth, editorId, urlPrefix, disabledFlag) {
	// Ignore the disable flag until problem with YUI Is resolved.
	disabledFlag = false;
	
	var myConfig = {
	    height: myHeight + 'px',
//	    width: myWidth,
	    disabled: disabledFlag
	};
	
	var objectName = 'jc_editor_' + editorId;
	var cmd = objectName + ' = new YAHOO.widget.Editor(\'' + editorId + '\', myConfig);';
	eval(cmd);
	eval('editor = ' + objectName);
	editor.render();
//	editor.set('disabled', disabledFlag);
/*
    editor.addListener('editorContentLoaded', function(master) {
    	alert("done");
    	editor.set('disabled', false);
    	editor.setEditorHTML('testing');
    	editor.set('disabled', true);
    });
*/    
	var heightValue = 600 + 'px';
	var jsiu_config = {
	  height: heightValue,
	  width: '600px',
	  linkStyle: 'yui_image_browser_a',
	  textStyle: 'yui_image_browser_text',
	  urlPrefix: urlPrefix
	}
	jsiu_getInstance(context, editor, jsiu_config);
}

function jc_getElementsByName(name) {
	if (YAHOO.env.ua.ie > 0) {
		var list = document.all;
		var matches = new Array();
		for (var i = 0; i < list.length; i++) {
			if (list[i].name == name) {
				matches.push(list[i]);
			}
		}
		return matches;
	}
	else {
		return document.getElementsByName(name);
	}
}

function jc_getElementByName(name, tagName, startContainerId) {
	var objects = YAHOO.util.Dom.getElementsBy(function(element) {
		if (element.name == name) {
			return true;
		}
		return false;
	}, tagName, startContainerId);
	if (objects.length == 0) {
		return null;
	}
	return objects[0];
}

function initCalendar(containerId, inputName, linkName) {
	YAHOO.util.Event.onContentReady(containerId, function() {
		var options = { title:"Choose a date:", close:true };
		var calendar = new YAHOO.widget.Calendar(containerId, options);
		calendar.render();
		YAHOO.util.Event.addListener(linkName, 'click', calendar.show, calendar, true);
		calendar.selectEvent.subscribe(function(type, args, obj) {
			var objects = document.getElementsByName(inputName);
			var object = null;
			if (objects.length > 0) {
				object = objects[0];
			}
			object.value = jc_calendar_callback(type, args, obj);
		}, calendar, true);
	} );
}

var _random = 0;
function getRandom() {
	_random = _random + 1;
	return _random;
}

function getCheckedNodes(nodes) {
	checkedNodes = [];
    for(var i=0, l=nodes.length; i<l; i=i+1) {
        var n = nodes[i];
        if (n.checkState === 2) {
            checkedNodes.push(n.key); 
        }
        if (n.hasChildren()) {
			checkedNodes = checkedNodes.concat(getCheckedNodes(n.children));
        }
    }
    return checkedNodes;
}

function unescapeHTML(html) {
	var htmlNode = document.createElement("div");
	htmlNode.innerHTML = html;
	if(htmlNode.innerText)
	return htmlNode.innerText;
	return htmlNode.textContent;
}