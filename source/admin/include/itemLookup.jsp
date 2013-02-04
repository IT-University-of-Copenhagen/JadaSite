<script type="text/javascript">
var jc_itemSearchCallBack = null;
var jc_item_search_panel = null;
function jc_item_search_init() {
	  jc_item_search_panel = new YAHOO.widget.Panel("jc_item_search_panel", 
	                              { 
	                                width: "500px", 
	                                visible: false, 
	                                constraintoviewport: true ,
	                                fixedcenter : true,
	                                modal: true
	                              } 
	                               );
	  jc_item_search_panel.render();
}
YAHOO.util.Event.onDOMReady(jc_item_search_init);
function jc_item_search_show(callback) {
	jc_itemSearchCallBack = callback;
	jc_item_search_panel.show();
}
function jc_item_search_finish() {
	jc_item_search_panel.hide();
}
	
var handleFailure = function(o) {
  var message = document.getElementById("jc_item_search_message");
  message.innerHTML = "Error retrieving item list";
}

var handleSuccess = function(o) {
  if (o.responseText == undefined) {
    return;
  }
  var table = document.getElementById("jc_item_search_table");
  while (table.rows.length > 0) {
    table.deleteRow(0);
  }
  
  var object = eval('(' + o.responseText + ')');
  var message = document.getElementById("jc_item_search_message");
  if (object.message) {
    message.innerHTML = object.message;
  }
  else {
    message.innerHTML = "";
  }
  var index = 0;
  if (object.items.length == 0) {
	    table.insertRow(table.rows.length);
	    var row = table.rows[table.rows.length - 1];
	    var cell = null;
	    cell = row.insertCell(0);
	    cell.width = "100px";
	    cell.colspan = 3;
	    cell.innerHTML = 'No record found';
  }
  else {
	  for (i = 0; i < object.items.length; i++) {
	    table.insertRow(table.rows.length);
	    var row = table.rows[table.rows.length - 1];
	    var cell = null;
	    var itemId = object.items[index].itemId;
	    var itemNum = object.items[index].itemNum;
	    var itemShortDesc = object.items[index].itemShortDesc;
	    cell = row.insertCell(0);
	    cell.width = "100px";
	    cell.innerHTML = "<a href='javascript:void(0)' onclick='jc_item_search_callback_single(\"" + itemId + "\")'>" + 
	                     object.items[index].itemNum + "</a>";
	    cell = row.insertCell(1);
	    cell.width = "100px";
	    cell.innerHTML = object.items[index].itemUpcCd;
	    cell = row.insertCell(2);
	    cell.innerHTML = object.items[index].itemShortDesc;
	    index++;
	  }
  }
  
  var div = document.getElementById("jc_item_search_div");
  div.style.display = "block";
}

var jc_item_search_callback =
{
  success: handleSuccess,
  failure: handleFailure
};

function jc_item_search_get() {
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/lookup/itemLookup.do";
  var postData = ""
  postData += "itemNum=" + document.jc_item_search_form.itemNum.value + "&";
  postData += "itemUpcCd=" + document.jc_item_search_form.itemUpcCd.value + "&";
  postData += "itemShortDesc=" + document.jc_item_search_form.itemShortDesc.value;
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_item_search_callback, postData);
}

function jc_item_search_callback_single(itemId, itemNum, itemShortDesc) {
  var result = '{"items": [';
  result += '{"itemId": "' + itemId + '", "itemNum": "' + itemNum + '", "itemShortDesc": "' + itemShortDesc + '"}';
  result += ']}';
  if (jc_itemSearchCallBack != null) {
	jc_itemSearchCallBack(result);
  }
  else {
  	jc_item_search_client_callback(result);
  }
}

function jc_item_search_callback_multiple() {
}
</script>
<div class=" yui-skin-sam">
<div>
<div id="jc_item_search_panel">
  <div class="hd">Item search</div>
  <div class="bd"> 
    <form name="jc_item_search_form" method="post" action="javascript:void(0)">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td><span class="jc_input_label">Item number</span></td>
          <td nowrap>
            <a href="javascript:void(0);" onclick="jc_item_search_get()" class="jc_navigation_link">Pick item</a>
          </td>
        </tr>
        <tr> 
          <td> 
            <input type="text" name="itemNum" class="jc_input_control" size="20">
          </td>
          <td></td>
        </tr>
        <tr> 
          <td><span class="jc_input_label">Item upc code</span></td>
          <td></td>
        </tr>
        <tr> 
          <td> 
            <input type="text" name="itemUpcCd" class="jc_input_control" size="20">
          </td>
          <td></td>
        </tr>
        <tr> 
          <td><span class="jc_input_label">Item description</span></td>
          <td></td>
        </tr>
        <tr> 
          <td> 
            <input type="text" name="itemShortDesc" class="jc_input_control" size="40">
          </td>
          <td></td>
        </tr>
      </table>
      <div id="jc_item_search_div">
      <hr>
      <div id="jc_item_search_message" class="jc_input_error"></div>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td width="100px" nowrap><span class="jc_input_label">Item number</span></td>
          <td width="100px" nowrap><span class="jc_input_label">Upc Code</span></td>
          <td><span class="jc_input_label">Description</span></td>
        </tr>
      </table>
      <div style="height: 200px; overflow: auto; vertical-align: top">
      <table id="jc_item_search_table" width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td width="100px"></td>
          <td width="100px"></td>
          <td></td>
        </tr>
      </table>
      </div>
      </div>
    </form>
  </div>
</div>
</div>
</div>
