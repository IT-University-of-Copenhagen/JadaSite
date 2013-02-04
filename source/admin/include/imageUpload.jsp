<script type="text/javascript">
/*
Menu upload pop-up window.  Make sure this file is included outside of any FORM tag.
*/
var jc_image_upload_panel;
function jc_image_upload_init() {
  jc_image_upload_panel = new YAHOO.widget.Panel("jc_image_upload_panel", 
                              { 
                                width: "300px", 
                                visible: false, 
                                constraintoviewport: true ,
                                fixedcenter : true,
                                modal: true
                              } 
                               );
  jc_image_upload_panel.render();
}
function jc_image_upload_show(key) {
  document.jc_image_upload_form.reset();
  document.jc_image_upload_form.key.value = key;
  document.jc_image_upload_form.file.value = '';
  jc_image_upload_panel.show();
}
function jc_image_upload_finish() {
  jc_image_upload_panel.hide();
}

function jc_image_upload_message(message) {
  var container = document.getElementById("jc_image_upload_message");
  container.innerHTML = message;
}
YAHOO.util.Event.onDOMReady(jc_image_upload_init);

function jc_image_upload_callback() {
  var result = '{"formName": "jc_image_upload_form"}';
  jc_image_upload_client_callback(result);
}
</script>
<form name="jc_image_upload_form" method="post" action="o" enctype="multipart/form-data"> 
<input type="hidden" name="process" value="uploadImage">
<input type="hidden" name="key" value=""> 
<div class=" yui-skin-sam">
<div>
<div id="jc_image_upload_panel">
  <div class="hd">Upload image</div>
  <div class="bd"> 
    <div id="jc_image_upload_message" class="jc_input_error"></div>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr> 
        <td>
          Upload new image
        </td>
      </tr>
      <tr>
         <td>
         <input type="file" name="file" class="jc_input_object" width="300px">
         </td>
      </tr>
      <tr>
         <td class="jc_input_error"><div id="filename"></div></td>
      </tr>
      <tr>
         <td>&nbsp;</td>
      </tr>
      <tr>
        <td>
          <a href="javascript:void(0);" onclick="jc_image_upload_callback();" class="jc_navigation_link">Confirm</a>
          <a href="javascript:void(0);" onclick="jc_image_upload_panel.hide()" class="jc_navigation_link">Cancel</a>&nbsp;
        </td>
      </tr>
    </table>
  </div>
</div>
</div>
</div>
</form>