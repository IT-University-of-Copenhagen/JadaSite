<script type="text/javascript">
/*
Message window
*/
var jc_panel_message;
var jc_panel_callback;
var jc_panel_confirm_panel;

YAHOO.util.Event.onContentReady('jc_panel_confirm_panel', function() {
	jc_panel_confirm_panel = new YAHOO.widget.Panel("jc_panel_confirm_panel", 
              { 
              width: "300px", 
              visible: false, 
              constraintoviewport: true ,
              fixedcenter : true,
              modal: true
              } 
            );
	jc_panel_confirm_panel.render();
} );

function jc_panel_confirm_finish(value) {
  jc_panel_confirm_panel.hide();
  jc_panel_callback(value);
}

function jc_panel_confirm(msg, c) {
  var confirm = document.getElementById("jc_panel_confirm_text");
  confirm.innerHTML = msg;
  jc_panel_callback = c;
  jc_panel_confirm_panel.show();
}

function overrideLanguage(target, source, control)  {
  if (!control.checked) {
    jc_panel_confirm('Confirm to use default language?', function(confirm) {
      if (confirm) {
        target.value = source.value;
        target.disabled = true;
        return true;
      }
      else {
        control.checked = true;
        return false;
      }
    });
  }
  else {
    target.disabled = false;
  }
}

function overrideEditorLanguage(target, source, control) {
  if (!control.checked) {
    jc_panel_confirm('Confirm to use default language?', function(confirm) {
      if (confirm) {
        target.setEditorHTML(source.value);
        target.set('disabled', true);
        return true;
      }
      else {
        control.checked = true;
        return false;
      }
    });
  }
  else {
    target.set('disabled', false)
  }
}

function overrideCurrency(target, source, control)  {
	  if (!control.checked) {
	    jc_panel_confirm('Confirm to use default currency?', function(confirm) {
	      if (confirm) {
	        target.value = source.value;
	        target.disabled = true;
	        return true;
	      }
	      else {
	        control.checked = true;
	        return false;
	      }
	    });
	  }
	  else {
	    target.disabled = false;
	  }
	}

</script>

<div class=" yui-skin-sam">
<div>
<div id="jc_panel_confirm_panel">
  <div class="hd">Confirm</div>
  <div class="bd"> 
    <div id="jc_panel_confirm_text" class="jc_input_label"></div>
    &nbsp;
    <div>
      <a href="javascript:void(0);" onclick="jc_panel_confirm_finish(true)" class="jc_navigation_link">Yes</a>&nbsp;
      <a href="javascript:void(0);" onclick="jc_panel_confirm_finish(false)" class="jc_navigation_link">No</a>&nbsp;
    </div>
  </div>
</div>
</div>
</div>