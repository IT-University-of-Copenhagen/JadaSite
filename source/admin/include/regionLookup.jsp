<script type="text/javascript" src="/${adminBean.contextPath}/admin/web/utility/taskNode.js"></script>
<script type="text/javascript">
var jc_region_search_panel;
var jc_region_tree;
function jc_region_search_init() {
    jc_region_search_panel = new YAHOO.widget.Panel("jc_region_search_panel",
                              {
                                width: "500px",
                                visible: false,
                                constraintoviewport: true ,
                                fixedcenter : true,
                                modal: true
                              }
                               );
    jc_region_search_panel.render();
}

function jc_region_search_show() {
    var container = document.getElementById("jc_region_search_tree");
    container.innerHTML = "";
    jc_region_search_get();
    jc_region_search_panel.show();
}

function jc_region_search_finish() {
    jc_region_search_panel.hide();
}

function jc_region_search_message(message) {
    var message = document.getElementById("jc_region_search_message");
    message.innerHTML = message;
}

function jc_region_search_add_country_node(tree, jsonObject) {
    var node = new YAHOO.widget.TaskNode(jsonObject.countryName, jsonObject.countryId, tree, false);
    return node;
}

function jc_region_search_add_state_node(tree, jsonObject) {
    var node = new YAHOO.widget.TaskNode(jsonObject.stateName, jsonObject.stateId, tree, false);
    return node;
}

var jc_region_search_get_callback = {
    success: function(o) {
        if (o.responseText == undefined) {
            return;
        }
        var jsonObject = eval('(' + o.responseText + ')');
        jc_region_tree = new YAHOO.widget.TreeView("jc_region_search_tree");
        var root = jc_region_tree.getRoot();

        var i = 0;
        for (i = 0; i < jsonObject.countries.length; i++) {
            var country = jsonObject.countries[i];
            var countryNode = jc_region_search_add_country_node(root, country);
            for (j = 0; j < country.states.length; j++) {
                var state = country.states[j];
                jc_region_search_add_state_node(countryNode, state);
            }
        }
        jc_region_tree.draw();
    },
    failure: function(o) {
        var message = document.getElementById("jc_region_search_message");
        message.innerHTML = "Error retrieving region list";
    }
};

function jc_region_search_get() {
    var url = "<c:out value='/${adminBean.contextPath}'/>/admin/lookup/regionLookup.do";
    var request = YAHOO.util.Connect.asyncRequest('GET', url, jc_region_search_get_callback);
}

function jc_region_search_callback() {
	var count = 0;
    var result = '{';
    result += '"countries": [';
	var checkedNodes = jc_region_get_checked_nodes(jc_region_tree.getRoot().children);
	for (var j = 0; j < checkedNodes.length; j++) {
		if (checkedNodes[j].parent != jc_region_tree.getRoot()) {
			continue;
		}
		if (count > 0) {
	    	result += ', ';
		}
		result += '{"countryId": "' + checkedNodes[j].key + '"}';
		count++;
	}
    result += '],';
    result += '"states": [';
    checkedNodes = jc_region_get_checked_nodes(jc_region_tree.getRoot().children);
    counter = 0;
    for (var i = 0; i < checkedNodes.length; i++) {
		if (checkedNodes[i].parent == jc_region_tree.getRoot()) {
			continue;
		}
        if (counter > 0) {
            result += ', ';
        }
        result += '{"stateId": "' + checkedNodes[i].key + '"}';
        counter++;
    }

    result += ']';
    result += '}';
    jc_region_search_client_callback(result);
}

function jc_region_get_checked_nodes(nodes) {
	var checkedNodes = [];
    for(var i=0, l=nodes.length; i<l; i=i+1) {
        var n = nodes[i];
        if (n.checkState === 2) {
            checkedNodes.push(n); 
        }
        if (n.hasChildren()) {
			checkedNodes = checkedNodes.concat(jc_region_get_checked_nodes(n.children));
        }
    }
    return checkedNodes;
}

YAHOO.util.Event.onDOMReady(function() {
    jc_region_search_init();
} );

YAHOO.util.Event.onDOMReady(function() {
    var button = new YAHOO.widget.Button({label: 'Select',
                                          id: 'regionButton',
                                          container: 'regionButtonContainer' });
    button.on('click', function() {
		jc_region_search_callback();
        jc_region_search_finish();
    });
} );
</script>

<div class=" yui-skin-sam">
  <div>
    <div id="jc_region_search_panel">
      <div class="hd">Region search</div>
      <div class="bd">
        <div align="right" id="regionButtonContainer"></div>
        <div id="jc_region_search_message" class="jc_input_error"></div>
        <form name="jc_region_form" method="post" action="">
        <div id="jc_region_search_tree" style="height: 200px; overflow: auto; vertical-align: top">
        </div>
        </form>
      </div>
    </div>
  </div>
</div>

