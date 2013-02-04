<script type="text/javascript">
/*
Message window
*/
var jc_panel_message;
function jc_panel_message_init() {
  jc_panel_message = new YAHOO.widget.Panel("jc_panel_message", 
                           { 
                           width: "300px", 
                           visible: false, 
                           constraintoviewport: true ,
                           fixedcenter : true,
                           modal: true
                           } 
                         );
  jc_panel_message.render();
}
function jc_panel_message_finish() {
  jc_panel_message.hide();
}
YAHOO.util.Event.onDOMReady(jc_panel_message_init);

function jc_panel_show_message(msg) {
  var message = document.getElementById("jc_panel_message_text");
  message.innerHTML = msg;
  jc_panel_message.show();
}
</script>

<div class=" yui-skin-sam">
<div>
<div id="jc_panel_message">
  <div class="hd">Message</div>
  <div class="bd"> 
    <div id="jc_panel_message_text" class="jc_input_label"></div>
    &nbsp;
    <div>
      <a href="javascript:void(0);" onclick="jc_panel_message_finish()" class="jc_navigation_link">OK</a>&nbsp;
    </div>
  </div>
</div>
</div>
</div>

<script type="text/javascript">
/*
Error window
*/
var jc_panel_error = null;

YAHOO.util.Event.onContentReady('jc_panel_error_text', function() {
	jc_panel_error = new YAHOO.widget.Panel("jc_panel_error", 
              { 
              width: "300px", 
              visible: false, 
              constraintoviewport: true ,
              fixedcenter : true,
              modal: true
              } 
            );
	jc_panel_error.render();
} );

function jc_panel_error_finish() {
  jc_panel_error.hide();
}

function jc_panel_show_error(msg) {
  var message = document.getElementById("jc_panel_error_text");
  message.innerHTML = msg;
  jc_panel_error.show();
}
</script>

<div class=" yui-skin-sam">
<div>
<div id="jc_panel_error">
  <div class="hd">Error</div>
  <div class="bd"> 
    <div id="jc_panel_error_text" class="jc_input_error"></div>
    &nbsp;
    <div>
      <a href="javascript:void(0);" onclick="jc_panel_error_finish()" class="jc_navigation_link">OK</a>&nbsp;
    </div>
  </div>
</div>
</div>
</div>