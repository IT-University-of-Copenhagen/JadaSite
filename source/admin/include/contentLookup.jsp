<script type="text/javascript">
var jc_contentSearchCallBack = null;
var jc_content_search_panel = null;
function jc_content_search_init() {
  jc_content_search_panel = new YAHOO.widget.Panel("jc_content_search_panel", 
                              { 
                                width: "500px", 
                                visible: false, 
                                constraintoviewport: true ,
                                fixedcenter : true,
                                modal: true
                              } 
                               );
  jc_content_search_panel.render();
}
YAHOO.util.Event.onDOMReady(jc_content_search_init);

function jc_content_search_show(callback) {
	jc_contentSearchCallBack = callback;
	jc_content_search_panel.show();
}

function jc_content_search_finish() {
	jc_content_search_panel.hide();
}

var handleFailure = function(o) {
  var message = document.getElementById("jc_content_search_message");
  message.innerHTML = "Error retrieving content list";
}

var handleSuccess = function(o) {
  if (o.responseText == undefined) {
    return;
  }
  var table = document.getElementById("jc_content_search_table");
  while (table.rows.length > 0) {
    table.deleteRow(0);
  }
  
  var object = eval('(' + o.responseText + ')');
  var message = document.getElementById("jc_content_search_message");
  if (object.message) {
    message.innerHTML = object.message;
  }
  else {
    message.innerHTML = "";
  }
  var index = 0;
  if (object.contents.length == 0) {
	    table.insertRow(table.rows.length);
	    var row = table.rows[table.rows.length - 1];
	    var cell = null;
	    cell = row.insertCell(0);
	    cell.width = "100%";
	    cell.innerHTML = "No record found";
  }
  else {
	  for (i = 0; i < object.contents.length; i++) {
	    table.insertRow(table.rows.length);
	    var row = table.rows[table.rows.length - 1];
	    var cell = null;
	    var contentId = object.contents[index].contentId;
	    var contentTitle = object.contents[index].contentTitle;
	    cell = row.insertCell(0);
	    cell.width = "100%";
	    cell.innerHTML = "<a href='javascript:void(0)' onclick='jc_content_search_callback_single(\"" + contentId + "\", \"" + escape(contentTitle) + "\")'>" + 
	                     object.contents[index].contentTitle + "</a>";
	    index++;
	  }
  }
  
  var div = document.getElementById("jc_content_search_div");
  div.style.display = "block";
}

var jc_content_search_callback =
{
  success: handleSuccess,
  failure: handleFailure
};

function jc_content_search_get() {
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/lookup/contentLookup.do";
  var postData = ""
  postData += "contentTitle=" + document.jc_content_search_form.contentTitle.value;
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_content_search_callback, postData);
}

function jc_content_search_callback_single(contentId, contentTitle) {
	var result = '{"contents": [';
	result += '{"contentId": "' + contentId + '", "contentTitle": "' + contentTitle + '"}';
	result += ']}';
	if (jc_contentSearchCallBack != null) {
		jc_contentSearchCallBack(result);
	}
	else {
	 	jc_content_search_client_callback(result);
	}
}

function jc_content_search_callback_multiple() {
}
</script>
<div class=" yui-skin-sam">
<div>
<div id="jc_content_search_panel">
  <div class="hd">Content search</div>
  <div class="bd"> 
    <form name="jc_content_search_form" method="post" action="javascript:void(0)">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td><span class="jc_input_label">Content Title</span></td>
          <td nowrap>
            <a href="javascript:void(0);" onclick="jc_content_search_get()" class="jc_navigation_link">Pick content</a>
          </td>
        </tr>
        <tr> 
          <td> 
            <input type="text" name="contentTitle" class="jc_input_control" size="40">
          </td>
          <td></td>
        </tr>
      </table>
      <div id="jc_content_search_div">
      <hr>
      <div id="jc_content_search_message" class="jc_input_error"></div>
      <div style="height: 200px; overflow: auto; vertical-align: top">
      <table id="jc_content_search_table" width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td width="100%"></td>
        </tr>
      </table>
      </div>
      </div>
    </form>
  </div>
</div>
</div>
</div>