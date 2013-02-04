<script type="text/javascript">
/*
Section selection pop-up window
*/
var jc_section_search_panel;
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
  var node = new YAHOO.widget.TextNode(jsonObject.sectionShortTitle, tree, false);
  node.labelElId = jsonObject.sectionId;
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
    tree.subscribe("clickEvent", function(oArgs) {
		node = oArgs.node;
        jc_section_search_callback_single(node.labelElId, node.label);
        return false;
	});
  },
  failure: function(o) {
    var message = document.getElementById("jc_section_search_message");
    message.innerHTML = "Error retrieving section list";
  }
};

function jc_section_search_get() {
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/lookup/sectionLookup.do";
  var request = YAHOO.util.Connect.asyncRequest('GET', url, jc_section_search_callback);
}

function jc_section_search_callback_single(sectionId, sectionShortTitle) {
  var result = '{"sections": [';
  result += '{"sectionId": "' + sectionId + '", "sectionShortTitle": "' + sectionShortTitle + '"}';
  result += ']}';
  jc_section_search_client_callback(result);
}
</script>
<div class=" yui-skin-sam">
<div>
<div id="jc_section_search_panel">
  <div class="hd">Section search</div>
  <div class="bd"> 
    <div id="jc_section_search_message" class="jc_input_error"></div>
    <div id="jc_section_search_tree" style="height: 200px; overflow: auto; vertical-align: top">
    
    </div>
  </div>
</div>
</div>
</div>
