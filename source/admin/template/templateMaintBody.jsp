<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="templateMaintForm" scope="session" value="${templateMaintForm}"/>

<script language="JavaScript">
function submitForm(type) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/template/templateMaint.do';
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}
function submitBackForm(type) {
    document.forms[0].action = "/<c:out value='${adminBean.contextPath}'/>/admin/template/templateListing.do";
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}
function sizeImage(id, path) {
  var image = new Image();
  image.src = path;
  var width = image.width;
  var object = document.getElementById(id);
  object.src = image.src;
  if (width > 100) {
    object.width = 100;
  }
}
</script>
<html:form action="/admin/template/templateMaint.do" method="post" enctype="multipart/form-data">
<html:hidden property="process" value="list"/>
<html:hidden property="path" />
<html:hidden property="filename" />
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - <a href="/<c:out value='${adminBean.contextPath}'/>/admin/template/templateListing.do?process=back">Template 
      Listing</a> - Template Create</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr> 
    <td width="100%" valign="top"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td align="right" class="jc_panel_table_header"> 
            <table width="0%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td>
                <layout:mode value="create"> 
                <html:submit property="submitButton" value="Create" styleClass="jc_submit_button" onclick="return submitForm('upload')"/>&nbsp;
                </layout:mode>
                </td>
                <c:if test="${!templateMaintForm.servletResource}">
                <layout:mode value="edit"> 
                <td><html:submit property="submitButton" value="Save" styleClass="jc_submit_button" onclick="return submitForm('save')"/>&nbsp;</td>
                <td><html:submit property="submitButton" value="Remove" styleClass="jc_submit_button" onclick="return submitForm('remove')"/>&nbsp;</td>
                </layout:mode>
                </c:if>
                <td>
                <html:submit property="submitButton" value="Cancel" styleClass="jc_submit_button" onclick="return submitBackForm('back')"/>&nbsp;
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <br>
    </td>
  </tr>
</table>
<br>
<layout:mode value="create">
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr> 
    <td width="100%" class="jc_input_label" colspan="2">
      <logic:messagesPresent property="extract" message="true"> 
      <html:messages property="extract" id="errorText" message="true">
        <span class="jc_input_error">
          <bean:write name="errorText"/>
        </span>
      </html:messages>
      </logic:messagesPresent> 
      <logic:messagesPresent property="info" message="true"> 
      <html:messages property="info" id="infoText" message="true">
        <span class="jc_input_comment">
          <bean:write name="infoText"/>
        </span>
      </html:messages>
      </logic:messagesPresent> 
    </td>
  </tr>
  <layout:mode value="create">
  <tr> 
    <td>
    You can create template either by uploading a predefined template jar file or by creating a empty
    template directory on server.
    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td>
    To upload a prefined template jar file, please browse and
    select from your local directories.
    </td>
  </tr>
  <tr> 
    <td class="jc_input_label">
    Template jar file
    </td>
  </tr>
  <tr>
    <td> 
      <input type="file" name="file" size="20" onkeydown="return false;" class="jc_input_object">
    </td>
  </tr>
  <tr> 
    <td>
    To create a empty template directory on server, please enter the template name you wish to create.
    </td>
  </tr>
  <tr> 
    <td class="jc_input_label">
    Template name
    </td>
  </tr>
  <tr>
    <td>
      <html:text size="20" name="templateMaintForm" property="templateName" styleClass="jc_input_control"/>
    </td>
  </tr>
  <tr> 
    <td class="jc_input_label">
    </td>
  </tr>
  <tr>
    <td> 
      <logic:messagesPresent property="file" message="true"> 
      <html:messages property="file" id="errorText" message="true">
        <span class="jc_input_error">
          <bean:write name="errorText"/>
        </span>
      </html:messages>
      </logic:messagesPresent> 
    </td>
  </tr>
  </layout:mode>
  <tr> 
    <td class="jc_input_label">
    Description
    </td>
  </tr>
  <tr>
    <td> 
      <layout:text layout="false" property="templateDesc" size="40" mode="E,E,E" styleClass="tableContent"/>
    </td>
  </tr>
</table>
</layout:mode>
<layout:mode value="edit">
<html:messages property="msg" id="errorText" message="true">
  <span class="jc_input_error">
    <bean:write name="errorText"/>
  </span>
</html:messages>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr> 
    <td class="jc_input_label">
    Template Name
    </td>
  </tr>
  <tr>
    <td> 
      <html:text size="20" name="templateMaintForm" property="templateName" styleClass="jc_input_control" readonly="true"/>
    </td>
  </tr>
  <tr> 
    <td > 
      <logic:messagesPresent property="file" message="true"> 
      <html:messages property="file" id="errorText" message="true">
        <span class="jc_input_error">
          <bean:write name="errorText"/>
        </span>
      </html:messages>
      </logic:messagesPresent> 
    </td>
  </tr>
  <tr> 
    <td class="jc_input_label">
    Description
    </td>
  </tr>
  <tr>
    <td>
    <c:choose>
      <c:when test="${templateMaintForm.servletResource}">
      <layout:text layout="false" property="templateDesc" size="40" mode="E,E,E" styleClass="tableContent" readonly="true"/>
      </c:when>
      <c:otherwise>
      <layout:text layout="false" property="templateDesc" size="40" mode="E,E,E" styleClass="tableContent"/>
      </c:otherwise>
    </c:choose>   
    </td>
  </tr>
</table>
<br><br>
<div class="yui-content" style="width: 700px">
  <div id="directory" style="padding: 5px; width: 700px">
    <div class="jc_shaded_box">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td>
            <table border="0" cellspacing="0" cellpadding="5" width="100%">
              <tr>
                <td width="30">&nbsp;</td>
                <td>
                  <c:if test="${!templateMaintForm.root}">
                    <a href="/<c:out value='${adminBean.contextPath}'/>/admin/template/templateMaint.do?process=edit&templateName=<c:out value='${templateMaintForm.templateName}'/>&path=<c:out value='${templateMaintForm.parentFullPath}'/>" class="jc_submit_link">parent</a>
                    &nbsp;&nbsp;&nbsp;
                  </c:if>
                  <c:forEach var="entry" items="${templateMaintForm.pathEntries}">
                    /&nbsp;<c:out value="${entry.label}"/>&nbsp;
                  </c:forEach>
                  <c:if test="${templateMaintForm.root}">
                  /
                  </c:if>
                </td>
                <td width="100">&nbsp;</td>
                <td align="right" nowrap>
                  <c:if test="${!templateMaintForm.servletResource}">
                  <a class="jc_navigation_link" href="javascript:submitForm('removeFile');">Remove selected</a>
                  <a class="jc_navigation_link" onclick="jc_file_upload_show()" href="javascript:void(0);">Upload new file</a>
                  <a class="jc_navigation_link" onclick="jc_create_directory_show()" href="javascript:void(0);">Create directory</a>
                  </c:if>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td>
            <table border="0" cellspacing="0" cellpadding="5">
              <tr>
                <td>&nbsp;</td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
              </tr>
              <tr>
                <td colspan="6">
                  <logic:messagesPresent property="remove" message="true"> 
                    <html:messages property="remove" id="errorText" message="true">
                      <span class="jc_input_error">
                        <bean:write name="errorText"/>
                      </span>
                    </html:messages>
                  </logic:messagesPresent> 
                </td>
              </tr>
              <c:set var="index" value="0" />
              <c:forEach var="file" items="${templateMaintForm.files}">
              <c:set var="index" value="${index + 1}" />
              <tr>
                <td width="30" nowrap><input type="checkbox" name="removeFiles" value="<c:out value="${file.filename}"/>">&nbsp;</td>
                <td width="200" nowrap>
                  <c:choose>
                    <c:when test="${file.directory}">
                      <a href="/<c:out value='${adminBean.contextPath}'/>/admin/template/templateMaint.do?process=edit&templateName=<c:out value='${templateMaintForm.templateName}'/>&path=<c:out value='${file.fullPath}'/>"><c:out value="${file.filename}"/></a>
                    </c:when>
                    <c:otherwise>
                      <a href="javascript:void(0);" onclick="jc_edit_file_show('${file.filename}');"><c:out value='${file.filename}'/></a>
                    </c:otherwise>
                  </c:choose>
                </td>
                <td width="100">
                  <c:if test="${file.image}">
                    <c:choose>
                      <c:when test="${templateMaintForm.servletResource}">
                        <img id="img_${index}" src="" border="0" >
                        <script language="JavaScript">
                          sizeImage('img_${index}', '/${adminBean.contextPath}/content/template/${templateMaintForm.templateName}/${file.fullPath}');
                        </script>
                      </c:when>
                      <c:otherwise>
                        <img id="img_${index}" src="" border="0" >
                        <script language="JavaScript">
                          sizeImage('img_${index}', '/${adminBean.contextPath}/web/proxy/${adminBean.siteId}/template/${templateMaintForm.templateName}/${file.fullPath}');
                        </script>
                       </c:otherwise>
                    </c:choose>
                 </c:if>
                </td>
                <td width="100" nowrap>
                  <div align="right">
                  <c:choose>
                    <c:when test="${file.directory}">
                      &lt;dir&gt;
                    </c:when>
                    <c:otherwise>
                      <c:if test="${!templateMaintForm.servletResource}">
                        <c:out value="${file.size}"/>&nbsp;bytes
                      </c:if>
                    </c:otherwise>
                  </c:choose>
                  </div>
                </td>
                <td width="20"></td>
                <td width="200" nowrap><c:out value="${file.modificationDate}"/></td>
              </tr>
              </c:forEach>
            </table>
          </td>
        </tr>
      </table>
    </div>
  </div>

</layout:mode>

<script type="text/javascript">
var jc_file_upload_panel;
function jc_file_upload_init() {
  jc_file_upload_panel = new YAHOO.widget.Panel("jc_file_upload_panel", 
                              { 
                                width: "300px", 
                                visible: false, 
                                constraintoviewport: true ,
                                fixedcenter : true,
                                modal: true
                              } 
                               );
  jc_file_upload_panel.render();
}
function jc_file_upload_show(siteId, key) {
  document.jc_file_upload_form.templateName.value = "<c:out value='${templateMaintForm.templateName}'/>";
  document.jc_file_upload_form.path.value = "<c:out value='${templateMaintForm.path}'/>";
  document.jc_file_upload_form.file.value = '';
  setIdValue('jc_file_upload_message', '');
  jc_file_upload_panel.show();
}
function jc_file_upload_finish() {
  jc_file_upload_panel.hide();
}
YAHOO.util.Event.onDOMReady(jc_file_upload_init);

function jc_file_upload_callback() {
  YAHOO.util.Connect.setForm('jc_file_upload_form', true, true);
  var url = "/<c:out value='${adminBean.contextPath}'/>/admin/template/templateMaint.do";
  var response = YAHOO.util.Connect.asyncRequest('GET', url, jc_upload_callback);
}

var jc_upload_callback = {
  upload: function(o) {
    var value = o.responseText.replace(/<\/?pre>/ig, '');
    if (!isJsonResponseValid(value)) {
      setIdValue('jc_file_upload_message', 'Unable to upload file');
      return false;
    }
    var jsonObject = getJsonObject(value);
    if (jsonObject.status == 'failed') {
      setIdValue('jc_file_upload_message', jsonObject.message);
    }
    else {
      jc_file_upload_finish();
      window.location = "<c:out value='/${adminBean.contextPath}'/>/admin/template/templateMaint.do?process=edit&templateName=" + "<c:out value='${templateMaintForm.templateName}'/>" + "&path=" + "<c:out value='${templateMaintForm.path}'/>";
    }
  }
}
</script>
</html:form>

<form name="jc_file_upload_form" method="post" action="o" enctype="multipart/form-data"> 
<input type="hidden" name="process" value="uploadfile">
<input type="hidden" name="templateName" value=""> 
<input type="hidden" name="path" value=""> 
<div class=" yui-skin-sam">
<div>
<div id="jc_file_upload_panel">
  <div class="hd">Upload file</div>
  <div class="bd"> 
    <div id="jc_file_upload_message" class="jc_input_error"></div>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr> 
        <td>
          Upload new file
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
          <a href="javascript:void(0);" onclick="jc_file_upload_callback();" class="jc_navigation_link">Confirm</a>
          <a href="javascript:void(0);" onclick="jc_file_upload_panel.hide()" class="jc_navigation_link">Cancel</a>&nbsp;
        </td>
      </tr>
    </table>
  </div>
</div>
</div>
</div>
</form>

<script type="text/javascript">
var jc_create_directory_panel;
function jc_create_directory_init() {
  jc_create_directory_panel = new YAHOO.widget.Panel("jc_create_directory_panel", 
                              { 
                                width: "300px", 
                                visible: false, 
                                constraintoviewport: true ,
                                fixedcenter : true,
                                modal: true
                              } 
                               );
  jc_create_directory_panel.render();
}
function jc_create_directory_show() {
  jc_create_directory_panel.show();
}
function jc_create_directory_finish() {
  jc_create_directory_panel.hide();
}
function jc_create_directory_message(message) {
  var msg = document.getElementById("jc_create_directory_message");
  msg.innerHTML = message;
}
YAHOO.util.Event.onDOMReady(jc_create_directory_init);

var jc_create_directory_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_create_directory_message("Unexcepted Error: unable to create directory");
      return false;
    }
    var jsonObject = getJsonObject(o.responseText);
    if (!isJsonResponseSuccess(o.responseText)) {
      jc_create_directory_message(jsonObject.message);
      return;
    }
    jc_create_directory_finish();
    window.location = "<c:out value='/${adminBean.contextPath}'/>/admin/template/templateMaint.do?process=edit&templateName=" + "<c:out value='${templateMaintForm.templateName}'/>" + "&path=" + "<c:out value='${templateMaintForm.path}'/>";
  },
  failure: function(o) {
    jc_create_directory_message("Unexcepted Error: unable to adjust inventory");
  }
};

function jc_create_directory() {
  var templateName = "<c:out value='${templateMaintForm.templateName}'/>";
  var directory = document.directoryForm.directory.value;
  var path = "<c:out value='/${templateMaintForm.path}'/>";
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/template/templateMaint.do";
  var data = "process=mkdir&templateName=" + templateName + "&path=" + path + "&directory=" + directory;
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_create_directory_callback, data);
}
</script>

<form name="directoryForm" method="post" action="" enctype="multipart/form-data">
<div class=" yui-skin-sam">
<div>
<div id="jc_create_directory_panel">
  <div class="hd">Create directory</div>
  <div class="bd"> 
    <div id="jc_create_directory_message" class="jc_input_error"></div>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>Enter new directory</td>
      </tr>
      <tr>
        <td>
          <input type="text" name="directory">
        </td>
      </tr>
      <tr>
        <td>
          &nbsp;
        </td>
      </tr>
      <tr>
        <td>
          <a href="javascript:void(0);" onclick="jc_create_directory()" class="jc_navigation_link">Confirm</a>
          <a href="javascript:void(0);" onclick="jc_create_directory_finish()" class="jc_navigation_link">Cancel</a>&nbsp;
        </td>
      </tr>
    </table>
  </div>
</div>
</div>
</div>
</form>

<script type="text/javascript">
var jc_edit_file_panel;
function jc_edit_file_init() {
  jc_edit_file_panel = new YAHOO.widget.Panel("jc_edit_file_panel", 
                              { 
                                width: "600px", 
                                visible: false, 
                                constraintoviewport: true ,
                                fixedcenter : true,
                                modal: true
                              } 
                               );
  jc_edit_file_panel.render();
}
var filename;
function jc_edit_file_show(fname) {
  filename = fname;
  document.editForm.editText.value = '';
  jc_edit_file_message("");
  jc_save_panel_show(true);
  var templateName = "<c:out value='${templateMaintForm.templateName}'/>";
  var directory = document.directoryForm.directory.value;
  var path = "<c:out value='/${templateMaintForm.path}'/>";
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/template/templateMaint.do";
  var data = "process=showFile&templateName=" + templateName + "&path=" + path + "&filename=" + filename;
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_showfile_callback, data);
  jc_edit_file_panel.show();
}
var jc_showfile_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_edit_file_message("Unexcepted Error: unable to display file");
      jc_save_panel_show(false);
      return false;
    }
    var jsonObject = getJsonObject(o.responseText);
    if (!isJsonResponseSuccess(o.responseText)) {
      jc_edit_file_message(jsonObject.message);
      jc_save_panel_show(false);
      return;
    }
    document.editForm.editText.value = jsonObject.editText;
  },
  failure: function(o) {
    jc_save_panel_show(false);
    jc_edit_file_message("Unexcepted Error: unable to display file");
  }
};
function jc_edit_file_finish() {
  jc_edit_file_panel.hide();
}
function jc_edit_file_message(message) {
  var msg = document.getElementById("jc_edit_file_message");
  msg.innerHTML = message;
}
function jc_save_panel_show(show) {
  var object = document.getElementById("jc_save_panel");
  if (object) {
    if (show) {
      object.style.display = "block";
    }
    else {
      object.style.display = "none";
    }
  }
}
YAHOO.util.Event.onDOMReady(jc_edit_file_init);

var jc_save_file_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_edit_file_message("Unexcepted Error: unable to save file");
      return false;
    }
    var jsonObject = getJsonObject(o.responseText);
    if (!isJsonResponseSuccess(o.responseText)) {
      jc_edit_file_message(jsonObject.message);
      return;
    }
    jc_edit_file_finish();
  },
  failure: function(o) {
    jc_edit_file_message("Unexcepted Error: unable to save file");
  }
};
function jc_save_file() {
  var templateName = "<c:out value='${templateMaintForm.templateName}'/>";
  var directory = document.directoryForm.directory.value;
  var path = "<c:out value='/${templateMaintForm.path}'/>";
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/template/templateMaint.do";
  var data = "process=savefile&templateName=" + templateName + "&path=" + path + "&directory=" + directory + "&filename=" + filename;
  data += "&editText=" + encodeURIComponent(document.editForm.editText.value);
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_save_file_callback, data);
}
</script>

<form name="editForm" method="post" action="" enctype="multipart/form-data">
<div class=" yui-skin-sam">
<div>
<div id="jc_edit_file_panel">
  <div class="hd">Edit file</div>
  <div class="bd"> 
    <div id="jc_edit_file_message" class="jc_input_error"></div>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>
          <textarea name="editText" cols="200" rows="20"></textarea>
        </td>
      </tr>
      <tr>
        <td>
          &nbsp;
        </td>
      </tr>
      <tr>
        <td nowrap>
          <table border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td>
                <c:if test="${!templateMaintForm.servletResource}">
                <div id="jc_save_panel">
                <a href="javascript:void(0);" onclick="jc_save_file()" class="jc_navigation_link">Save</a>
                </div>
                </c:if>
              </td>
              <td>
              	<a href="javascript:void(0);" onclick="jc_edit_file_finish()" class="jc_navigation_link">Cancel</a>&nbsp;
              </td>
            </tr>
          </table>

        </td>
      </tr>
    </table>
  </div>
</div>
</div>
</div>
</form>