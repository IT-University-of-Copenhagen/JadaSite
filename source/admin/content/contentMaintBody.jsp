<%@ page language="java" import="com.jada.util.Utility" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<jsp:useBean id="adminBean"  type="com.jada.admin.AdminBean"  scope="session" />
<jsp:useBean id="contentMaintForm"  type="com.jada.admin.content.ContentMaintActionForm"  scope="request" />
<script language="javascript" type="text/javascript">
</script>
<script type="text/javascript" language="JavaScript">
var siteProfileClassDefault = false;
var translationEnable = false; 
var siteProfileClassId = "${contentMaintForm.siteProfileClassId}";
var mode = "${contentMaintForm.mode}";
var contentId = "${contentMaintForm.contentId}";
var imageCounter = 0;
<c:if test="${contentMaintForm.siteProfileClassDefault}">
siteProfileClassDefault = true;
</c:if>
<c:if test="${contentMaintForm.translationEnable}">
translationEnable = true;
</c:if>

var commentLoaded = false;
function showComment() {
  if (!commentLoaded) {
    var text = document.getElementById("tabComment");
    text.innerHTML = 'loading ...';
    showId('tabComment');
    var contentId = <c:out value='${contentMaintForm.contentId}'/>
    var url = "/${adminBean.contextPath}/admin/content/contentMaint.do";
    var data = "process=comments&contentId=" + contentId ;
    var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_comments_callback, data);
    commentLoaded = true;
  }
  showId('tabComment');
  hideId('tabComment_show');
  showId('tabComment_hide');
}

function hideComment() {
  hideId('tabComment');
  showId('tabComment_show');
  hideId('tabComment_hide');
}
var jc_comments_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: Unable to retrieve comments");
      return false;
    }
    var text = document.getElementById("tabComment");
    // empty table for formatting only.
    text.innerHTML = '<table border="0" width="100%" cellspacing="0" cellpadding="0"><tr><td>&nbsp;</td></tr></table>';
    var jsonObject = getJsonObject(o.responseText);
    for (var i = 0; i < jsonObject.comments.length; i++) {
      var comment = jsonObject.comments[i];
      text.innerHTML += '<b>' + comment.commentTitle + '</b><br>' +
                        comment.comment + '<br><br>' +
                        'Posted by ' + comment.custPublicName + ' (' + comment.custEmail + ') on ' + comment.recCreateDatetime + '<br><br>';
      text.innerHTML += 'Agree (' + comment.agreeCount + ')';
      text.innerHTML += ', Disagree (' + comment.disagreeCount + ')';
      if (comment.moderation == 'Y') {
        text.innerHTML += ', Alerted by visitor';
      }
      if (comment.commentApproved == 'Y') {
        text.innerHTML += ', Approved';
      }
      else if (comment.commentApproved == 'N') {
        text.innerHTML += ', Disapproved';
      }
      else {
        text.innerHTML += ', Not moderated';
      }
      text.innerHTML += '<br><br>';
    }
  },
  failure: function(o) {
      jc_panel_show_error("Unexcepted Error: Unable to retrieve comments");
  }
};


function submitDefaultImage(input) {
    document.contentMaintForm.process.value = "defaultImage";
    document.contentMaintForm.createDefaultImageId.value = input;
}

function submitForm(type) {
    if (!siteProfileClassDefault) {
      document.contentMaintForm.contentTitleLang.value = document.contentMaintForm.contentTitleLang_tmp.value;
      document.contentMaintForm.pageTitleLang.value = document.contentMaintForm.pageTitleLang_tmp.value;
      document.contentMaintForm.metaKeywordsLang.value = document.contentMaintForm.metaKeywordsLang_tmp.value;
      document.contentMaintForm.metaDescriptionLang.value = document.contentMaintForm.metaDescriptionLang_tmp.value;
      jc_editor_contentShortDescLang.saveHTML();
      jc_editor_contentDescLang.saveHTML();
    }
    else {
      jc_editor_contentShortDesc.saveHTML();
      jc_editor_contentDesc.saveHTML();
    }
    document.contentMaintForm.process.value = type;
    document.contentMaintForm.submit();
    return false;
}

function submitBackForm(type) {
    document.contentMaintForm.action = "/<c:out value='${adminBean.contextPath}'/>/admin/content/contentListing.do";
    document.contentMaintForm.process.value = type;
    document.contentMaintForm.submit();
    return false;
}

/*
 * Content related processing
 */
var jc_content_search_panel = null;
var addContentRelated_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to add related content");
      return false;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    paintContentsRelated();
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to aadd related content");
  }
};
function jc_content_related_search_client_callback(value) {
	jc_content_search_panel.hide();
   
	var jsonObject = eval('(' + value + ')');
   	for (var i = 0; i < jsonObject.contents.length; i++) {
		var content = jsonObject.contents[i];
		var url = "/${adminBean.contextPath}/admin/content/contentMaint.do";
		var data = "process=addContentRelated&contentId=" + contentId + "&contentRelatedId=" + content.contentId;
		var request = YAHOO.util.Connect.asyncRequest('POST', url, addContentRelated_callback, data);
   	}
	return false;
}

function addContentRelated() {
	jc_content_search_show(jc_content_related_search_client_callback);
}

var jc_contentsRelated_remove_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to remove related contents");
      return false;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    paintContentsRelated();
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to remove related contents");
  }
};

function removeContentsRelated() {
	var contentId = '${contentMaintForm.contentId}';
	var url = "/${adminBean.contextPath}/admin/content/contentMaint.do";
	var data = "process=removeContentsRelated&contentId=" + contentId;
	
	var e = document.getElementById("contentsRelated");
	var checkboxes = new Array();
	jc_traverse_element(e, checkboxes, 'input', 'contentRelatedIds.*');

	var count = 0;
	for (var i = 0; i < checkboxes.length; i++) {
	  if (!checkboxes[i].checked) {
	    continue;
	  }
	  data += "&contentRelatedIds=" + checkboxes[i].value;
	}
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_contentsRelated_remove_callback, data);
	return false;
}
 
var showContentsRelated_callback =
{
 	success: function(o) {
 	    if (!isJsonResponseValid(o.responseText)) {
 			jc_panel_show_error("Unexcepted Error: Unable to extract related contents");
 			return false;
 	    }
 	    var jsonObject = getJsonObject(o.responseText);
 	    var node = document.getElementById("contentsRelatedContainer");
 	    node.innerHTML = '';
 	    var table = document.createElement('table');
 	    table.setAttribute('id', 'contentsRelated');
 	    table.setAttribute('cellpadding', '5');
 	    table.className = 'jc_nobordered_table';
 	    table.setAttribute('width', '400');
 	    var body = document.createElement('tbody');
 	    table.appendChild(body);
 	    if (jsonObject.contents.length > 0) {
 		    var row = document.createElement('tr');
 		    body.appendChild(row);
 		    var col = document.createElement('td');
 		    col.setAttribute('width', '50');
 		    col.appendChild(document.createTextNode(''));
			row.appendChild(col);
 		    col = document.createElement('td');
 		    col.setAttribute('width', '100%');
 		    col.className = 'jc_input_label';
 		    col.appendChild(document.createTextNode('Description'));
			row.appendChild(col);
 	    }
 	   	for (var i = 0; i < jsonObject.contents.length; i++) {
 		    var content = jsonObject.contents[i];
 		    var row = document.createElement('tr');
 		    body.appendChild(row);
 		    col = document.createElement('td');
 		    var input = document.createElement('input');
 		    input.setAttribute('type', 'checkbox');
 		    input.setAttribute('name', 'contentRelatedIds');
 		    input.setAttribute('value', content.contentId);
 		    col.appendChild(input);
 		 	row.appendChild(col);
 		    col = document.createElement('td');
 		    col.appendChild(document.createTextNode(content.contentTitle));
			row.appendChild(col);
 		}
 		node.appendChild(table);
   },
   failure: function(o) {
       jc_panel_show_error("Unexcepted Error: Unable to extract related contents");
   }
};
 
function paintContentsRelated() {
	var url = "/${adminBean.contextPath}/admin/content/contentMaint.do";
	var data = "process=getContentsRelated&contentId=" + contentId;
	var request = YAHOO.util.Connect.asyncRequest('POST', url, showContentsRelated_callback, data);
}

YAHOO.util.Event.onContentReady('butContentsRelatedContainer', function() {
	var buttonMenu = [ { text: "Add Related Content", onclick: { fn: addContentRelated } }, 
	      			   { text: "Remove Related Content", onclick: { fn: removeContentsRelated } }
	      	         ];
    var menu = new YAHOO.widget.Button({ type: "menu", 
        								 disabled: true,
                                		 label: "Options", 
                                		 name: "contentsRelated",
                                		 menu: buttonMenu, 
                                		 container: "butContentsRelatedContainer" });
    if (siteProfileClassDefault) {
    	  menu.set('disabled', false);
    }
} );
	
YAHOO.util.Event.onContentReady('contentsRelatedContainer', function() {
	paintContentsRelated();
} );


function handleContentPublishOn(type, args, obj) { 
    document.contentMaintForm.contentPublishOn.value = jc_calendar_callback(type, args, obj);
}

function handleContentExpireOn(type, args, obj) { 
    document.contentMaintForm.contentExpireOn.value = jc_calendar_callback(type, args, obj);
}

YAHOO.namespace("example.calendar");

function init() {
  if (siteProfileClassDefault) {
    YAHOO.example.calendar.calContentPublishOn = new YAHOO.widget.Calendar("calContentPublishOn", "calContentPublishOnContainer", { title:"Choose a date:", close:true } );
    YAHOO.example.calendar.calContentPublishOn.render();
    YAHOO.example.calendar.calContentPublishOn.hide();
    YAHOO.util.Event.addListener("calContentPublishOn", "click", YAHOO.example.calendar.calContentPublishOn.show, YAHOO.example.calendar.calContentPublishOn, true);
    YAHOO.example.calendar.calContentPublishOn.selectEvent.subscribe(handleContentPublishOn, YAHOO.example.calendar.calContentPublishOn, true); 
  
    YAHOO.example.calendar.calContentExpireOn = new YAHOO.widget.Calendar("calContentExpireOn", "calContentExpireOnContainer", { title:"Choose a date:", close:true } );
    YAHOO.example.calendar.calContentExpireOn.render();
    YAHOO.example.calendar.calContentExpireOn.hide();
    YAHOO.util.Event.addListener("calContentExpireOn", "click", YAHOO.example.calendar.calContentExpireOn.show, YAHOO.example.calendar.calContentExpireOn, true);
    YAHOO.example.calendar.calContentExpireOn.selectEvent.subscribe(handleContentExpireOn, YAHOO.example.calendar.calContentExpireOn, true);
  }
  
  if (mode != 'C') {
	  jc_images_paint();
  }
}
YAHOO.util.Event.addListener(window, "load", init);
var tabView = new YAHOO.widget.TabView('tabPanel');

</script>

<html:form method="post" action="/admin/content/contentMaint" enctype="multipart/form-data"> 
<html:hidden property="contentId"/>
<html:hidden property="mode"/>
<html:hidden property="process" value=""/> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr>
    <td>Administration - <a href="/<c:out value='${adminBean.contextPath}'/>/admin/content/contentListing.do?process=back">Content 
      Listing</a> - Content Maintenance</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr>
    <td valign="top" width="400">
      <div class="jc_detail_panel_header" style="width: 400px">
        <table width="400" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">General</span></td>
            <td>
              <div align="right">
              &nbsp;
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div class="jc_detail_panel">
        <table width="400" border="0" cellspacing="0" cellpadding="5" class="jc_nobordered_table">
          <layout:mode value="edit"> 
          <tr> 
            <td class="jc_input_label">Hit Counter</td>
            <td class="jc_input_label">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="100">
                    <span id="hitCounter">
                      <c:out value='${contentMaintForm.contentHitCounter}'/>
                    </span>
                  </td>
                  <td>
                    <layout:mode value="edit">
                    <div align="left" nowrap>
                    <button type="button" id="butResetCounter" name="butResetCounter" value="Reset Counter">Reset Counter</button>
                    </div>
                    </layout:mode>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          </layout:mode>
          <tr> 
            <td class="jc_input_label">Publish On</td>
            <td class="jc_input_label">
              <lang:text property="contentPublishOn" styleClass="jc_input_control"/> 
              <c:if test="${contentMaintForm.siteProfileClassDefault}">
              <img id="calContentPublishOn" src="../images/icon/image_plus.gif" border="0">
              <div id="calContentPublishOnContainer" style="position: absolute; display:none;"></div>
              </c:if>
              <span class="jc_input_error"> 
              <logic:messagesPresent property="contentPublishOn" message="true"> 
              <br><html:messages property="contentPublishOn" id="errorText" message="true"> 
              <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
              </span> </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Expire On</td>
            <td class="jc_input_label"> 
            <span class="jc_input_error"> 
              <lang:text property="contentExpireOn" styleClass="jc_input_control"/>
              <c:if test="${contentMaintForm.siteProfileClassDefault}">
              <img id="calContentExpireOn" src="../images/icon/image_plus.gif" border="0">
              <div id="calContentExpireOnContainer" style="position: absolute; display:none;"></div>
              </c:if>
              <logic:messagesPresent property="contentExpireOn" message="true"> 
              <br><html:messages property="contentExpireOn" id="errorText" message="true"> 
              <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
              </span> </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Published</td>
            <td class="jc_input_label"> <lang:checkbox property="published" value="Y" styleClass="jc_input_control"/> 
            </td>
          </tr>
        </table>
      </div>
      <layout:mode value="edit">
	  <div class="jc_detail_panel_header" style="width: 400px">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td><span class="jc_input_label">Related content</span></td>
            <td>
              <div align="right">
                <table width="0" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                      <div id="tabContentsRelated_show">
                        <a href="javascript:void(0);" onclick="showId('tabContentsRelated');hideId('tabContentsRelated_show');showId('tabContentsRelated_hide')">
                          <img src="../images/icon/image_plus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                    <td>
                      <div id="tabContentsRelated_hide" style="display:none;">
                        <a href="javascript:void(0);" onclick="hideId('tabContentsRelated');showId('tabContentsRelated_show');hideId('tabContentsRelated_hide')">
                          <img src="../images/icon/image_minus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
	    </table>
	  </div>
	  <div id="tabContentsRelated" class="jc_detail_panel" style="display:none;">
        <table width="400" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td> 
              <div align="right" id="butContentsRelatedContainer"></div>
            </td>
          </tr>
          <tr> 
            <td> &nbsp;</td>
          </tr>
        </table>
        <div id="contentsRelatedContainer">
	    </div>
	  </div>
      <div class="jc_detail_panel_header" style="width: 400px">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">Images</span></td>
            <td>
              <div align="right">
                <table width="0" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                      <div id="tabImage_show">
                        <a href="javascript:void(0);" onclick="showId('tabImage');hideId('tabImage_show');showId('tabImage_hide')">
                          <img src="../images/icon/image_plus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                    <td>
                      <div id="tabImage_hide" style="display:none;">
                        <a href="javascript:void(0);" onclick="hideId('tabImage');showId('tabImage_show');hideId('tabImage_hide')">
                          <img src="../images/icon/image_minus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div id="tabImage" class="jc_detail_panel" style="display:none;">
        <table width="400" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td>
                    <c:if test="${!contentMaintForm.siteProfileClassDefault}">
                      Override default
                      <html:checkbox onclick="return overrideImageLanguage(this);" property="contentImageOverride"/>
                    </c:if>
                  </td>
                  <td>
                    <div id="imageOptionContainer" align="right"></div>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr> 
            <td> &nbsp;</td>
          </tr>
        </table>
        <table width="400" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td> 
              <div class="jc_image_scroll" style="width: 400px"> 
                <table id="jc_images_table" width="100%" border="0" cellspacing="0" cellpadding="5">

                </table>
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div class="jc_detail_panel_header" style="width: 400px">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">Menus</span></td>
            <td>
              <div align="right">
                <table width="0" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                      <div id="tabMenus_show">
                        <a href="javascript:void(0);" onclick="showId('tabMenus');hideId('tabMenus_show');showId('tabMenus_hide')">
                          <img src="../images/icon/image_plus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                    <td>
                      <div id="tabMenus_hide" style="display:none;">
                        <a href="javascript:void(0);" onclick="hideId('tabMenus');showId('tabMenus_show');hideId('tabMenus_hide')">
                          <img src="../images/icon/image_minus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div id="tabMenus" class="jc_detail_panel" style="display:none;">
        <table width="400" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td> 
              <div align="right" id="butMenusContainer"></div>
            </td>
          </tr>
          <tr> 
            <td> &nbsp;</td>
          </tr>
        </table>
        <table id="menuTable" width="400" border="0" cellspacing="0" cellpadding="2" class="jc_unbordered_table">
          <tr>
            <td></td>
            <td class="jc_input_label">Menu</td>
            <td class="jc_input_label">Sub-site</td>
          </tr>
          <logic:iterate name="contentMaintForm" property="selectedMenus" id="selectedMenu">
          <tr> 
            <td class="jc_input_control">  
              <c:choose>
                <c:when test='${contentMaintForm.siteProfileClassDefault}'>
                  <html:multibox property="removeMenus"> <bean:write name="selectedMenu" property="menuId"/></html:multibox>
                </c:when>
                <c:otherwise>
                  <html:multibox property="removeMenus" disabled="true"><bean:write name="selectedMenu" property="menuId"/></html:multibox>
                </c:otherwise>
              </c:choose>
            </td>
            <td><bean:write name="selectedMenu" property="menuLongDesc"/></td>
            <td><bean:write name="selectedMenu" property="siteDomainName"/></td>
          </tr>
          </logic:iterate>
        </table>
      </div>
      <div class="jc_detail_panel_header" style="width: 400px">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">Category</span></td>
            <td>
              <div align="right">
                <table width="0" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                      <div id="tabCategory_show">
                        <a href="javascript:void(0);" onclick="showId('tabCategory');hideId('tabCategory_show');showId('tabCategory_hide')">
                       	  <img src="../images/icon/image_plus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                    <td>
                      <div id="tabCategory_hide" style="display:none;">
                        <a href="javascript:void(0);" onclick="hideId('tabCategory');showId('tabCategory_show');hideId('tabCategory_hide')">
                       	  <img src="../images/icon/image_minus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div id="tabCategory" class="jc_detail_panel" style="display:none;">
        <table width="400" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td> 
              <div align="right" id="butCategoryContainer"></div>
            </td>
          </tr>
          <tr> 
            <td> &nbsp;</td>
          </tr>
        </table>
        <table id="categoryTable" width="400" border="0" cellspacing="0" cellpadding="2" class="jc_unbordered_table">
          <tr>
            <td></td>
            <td class="jc_input_label">Category</td>
          </tr>
        </table>
      </div>
      <div class="jc_detail_panel_header" style="width: 400px">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">Comments</span></td>
            <td>
              <div align="right">
                <table width="0" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                      <div id="tabComment_show">
                        <a href="javascript:void(0);" onclick="showComment();">
                          <img src="../images/icon/image_plus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                    <td>
                      <div id="tabComment_hide" style="display:none;">
                        <a href="javascript:void(0);" onclick="hideComment();">
                          <img src="../images/icon/image_minus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div id="tabComment" class="jc_detail_panel" style="display:none; width: 400px">
      </div>
      <div class="jc_detail_panel_header" style="width: 400px">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">Timestamp</span></td>
            <td>
              <div align="right">
                <table width="0" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>
                      <div id="tabTimestamp_show">
                        <a href="javascript:void(0);" onclick="showId('tabTimestamp');hideId('tabTimestamp_show');showId('tabTimestamp_hide')">
                        	<img src="../images/icon/image_plus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                    <td>
                      <div id="tabTimestamp_hide" style="display:none;">
                        <a href="javascript:void(0);" onclick="hideId('tabTimestamp');showId('tabTimestamp_show');hideId('tabTimestamp_hide')">
                        	<img src="../images/icon/image_minus.gif" width="11" height="11" border="0">
                        </a>
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div id="tabTimestamp" class="jc_detail_panel" style="display:none;">
        <table width="400" border="0" cellspacing="0" cellpadding="5" bordercolor="#CCCCCC" class="jc_unbordered_table">
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label" width="150">Updated By</td>
            <td class="jc_input_control" width="0"><div id="recUpdateBy"><layout:text layout="false" property="recUpdateBy" mode="I,I,I" styleClass="jc_input_control"/></div>
            </td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">Updated On</td>
            <td class="jc_input_control"><div id="recUpdateDatetime"><layout:text layout="false" property="recUpdateDatetime" mode="I,I,I" styleClass="jc_input_control"/></div>
            </td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">Created By</td>
            <td class="jc_input_label"> <layout:text layout="false" property="recCreateBy" mode="I,I,I" styleClass="jc_input_control"/> 
            </td>
          </tr>
          <tr class="jc_input_table_row"> 
            <td class="jc_input_label">Created On</td>
            <td class="jc_input_label"> <layout:text layout="false" property="recCreateDatetime" mode="I,I,I" styleClass="jc_input_control"/> 
            </td>
          </tr>
        </table>
      </div>
      </layout:mode>
    </td>
    <td width="100%" valign="top"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
        <tr>
          <td>
            <layout:mode value="edit">
            Profile
            <html:select property="siteProfileClassId" onchange="javascript:submitForm('language')"> 
              <html:optionsCollection property="siteProfileClassBeans" value="value" label="label"/> 
            </html:select>
            <button type="button" id="butTranslate" name="butTranslate" value="Translate">Translate</button>
            </layout:mode>
          </td>
          <td align="right"> 
            <table width="0%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td><html:submit property="submitButton" value="Save" styleClass="jc_submit_button" onclick="return submitForm('save');"/>&nbsp;</td>
                <layout:mode value="edit"> 
                <td><html:submit property="submitButton" value="Remove" styleClass="jc_submit_button" onclick="return submitForm('remove');"/>&nbsp;</td>
                </layout:mode> 
                <td><html:submit property="submitButton" value="Cancel" styleClass="jc_submit_button" onclick="return submitBackForm('back');"/>&nbsp;</td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <br>
      <span class="jc_input_error">
      <logic:messagesPresent property="error" message="true"> 
        <html:messages property="error" id="errorText" message="true"> 
        <bean:write name="errorText"/>
        </html:messages> 
      </logic:messagesPresent>
      </span>
      <table width="100%" border="0" cellspacing="0" cellpadding="4" class="borderTable">
        <tr> 
          <td class="jc_input_label">
          Title
          <lang:checkboxSwitch name="contentMaintForm" property="contentTitleLangFlag"> - Override language</lang:checkboxSwitch>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_control">
            <lang:text property="contentTitle" size="80" maxlength="128" styleClass="tableContent"/>
            <span class="jc_input_error">
            <logic:messagesPresent property="contentTitle" message="true"> 
            <br><html:messages property="contentTitle" id="errorText" message="true"> 
            <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
            </span>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">
            Description
            <lang:checkboxEditorSwitch name="contentMaintForm" property="contentShortDescLangFlag"> - Override language</lang:checkboxEditorSwitch>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_control">
            <lang:editorText name="contentMaintForm" property="contentShortDesc" height="200" width="100%" toolBarSet="Simple"/>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">
            Content
            <lang:checkboxEditorSwitch name="contentMaintForm" property="contentDescLangFlag"> - Override language</lang:checkboxEditorSwitch>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_control">
            <lang:editorText name="contentMaintForm" property="contentDesc" height="300" width="100%" toolBarSet="Simple"/>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">
          HTML Title Tag
          <lang:checkboxSwitch name="contentMaintForm" property="pageTitleLangFlag"> - Override language</lang:checkboxSwitch>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_control">
            <lang:text maxlength="255" size="120" property="pageTitle" styleClass="tableContent"/> 
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">
          Meta keywords
          <lang:checkboxSwitch name="contentMaintForm" property="metaKeywordsLangFlag"> - Override language</lang:checkboxSwitch>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_control">
            <lang:textarea rows="5" cols="51" property="metaKeywords" styleClass="tableContent"/> 
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">
          Meta description
          <lang:checkboxSwitch name="contentMaintForm" property="metaDescriptionLangFlag"> - Override language</lang:checkboxSwitch>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_control">
            <lang:textarea rows="5" cols="51" property="metaDescription" styleClass="tableContent"/> 
          </td>
        </tr>
        <tr> 
          <td> </td>
          <td> </td>
        </tr>
      </table>
    </td>
  </tr>
</table>

<layout:mode value="edit">
<script type="text/javascript">
YAHOO.util.Event.onContentReady('tabCategory', function() {
	var showCategory_callback =
	{
	  success: function(o) {
	    if (!isJsonResponseValid(o.responseText)) {
	      jc_panel_show_error("Unexcepted Error: unable to show selected category");
	      return false;
	    }
	    var jsonObject = getJsonObject(o.responseText);
	    jc_category_show_categories(jsonObject);
	  },
	  failure: function(o) {
	      jc_panel_show_error("Unexcepted Error: unable to show selected menu");
	  }
	};
	var url = "/${adminBean.contextPath}/admin/content/contentMaint.do";
	var data = "process=showCategories&contentId=" + contentId ;
	var request = YAHOO.util.Connect.asyncRequest('POST', url, showCategory_callback, data);
} );

function jc_category_show_categories(jsonObject) {
	  var table = document.getElementById("categoryTable");
	  var tbody = table.getElementsByTagName("tbody")[0]
	  if (!tbody) {
	    tbody = document.createElement('tbody');
	    table.appendChild(tbody);
	  }
	  var rows = tbody.childNodes;
	  for (var i = table.rows.length - 1; i > 0; i--) {
	    table.deleteRow(i);
	  }
	  
	  for (var i = 0; i < jsonObject.categories.length; i++) {
	    var row = document.createElement('tr');
	    var column;
	    column = document.createElement('td');
	    var checkbox = document.createElement('input');
	    checkbox.setAttribute('type', 'checkbox');
	    checkbox.setAttribute('value', jsonObject.categories[i].catId);
	    checkbox.setAttribute('name', 'removeCategories');
	    if (!siteProfileClassDefault) {
	      checkbox.disabled = true;
	    }
	    column.appendChild(checkbox);
	    row.appendChild(column);
	    column = document.createElement('td');
	    column.innerHTML = jsonObject.categories[i].catShortTitle;
	    row.appendChild(column);
	    tbody.appendChild(row);
	  }
}

var jc_category_add_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_category_search_messge("Unexcepted Error: unable to assign category");
      return false;
    }
    var jsonObject = getJsonObject(o.responseText);
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    jc_category_show_categories(jsonObject);
    jc_category_search_finish();
  },
  failure: function(o) {
    jc_category_search_messge("Unable to add category");
  }
};

function jc_category_search_client_callback(value) {
  var response = eval('(' + value + ')');
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/content/contentMaint.do";
  var data = "process=addCategories&contentId=" + contentId;
  for (var i = 0; i < response.categories.length; i++) {
	data += "&addCategories=" + response.categories[i].catId;
  }
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_category_add_callback, data);
  return false;
}
</script>
<%@ include file="/admin/include/categoriesLookup.jsp" %>

<script type="text/javascript">
var jc_category_remove_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to remove categories");
      return false;
    }
    var jsonObject = getJsonObject(o.responseText);
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    jc_category_show_categories(jsonObject);
  },
  failure: function(o) {
    jc_panel_show_error("Unable to remove content from category");
  }
};

function jc_category_remove() {
	var url = "/${adminBean.contextPath}/admin/content/contentMaint.do";
	var data = "process=removeCategories&contentId=" + contentId;
	  
	var e = document.getElementById("categoryTable");
	var checkboxes = new Array();
	jc_traverse_element(e, checkboxes, 'input', 'removeCategories.*');
	  
	var removeCategories = new Array();
	var count = 0;
	for (var i = 0; i < checkboxes.length; i++) {
	    if (!checkboxes[i].checked) {
	      continue;
	    }
	    data += "&removeCategories=" + checkboxes[i].value;
	    removeCategories[count++] = checkboxes[i].value;
	}
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_category_remove_callback, data);
	return false;
}

var butCategoryOption = [
    { text: "Pick Setion", value: 1, onclick: { fn: jc_category_search_show } },
    { text: "Remove From Category", value: 2, onclick: { fn: jc_category_remove } }
];
var butCategory = new YAHOO.widget.Button({
      disabled: true,
      type: "menu", 
      label: "Options", 
      name: "butCategory",
      menu: butCategoryOption, 
      container: "butCategoryContainer" });
if (siteProfileClassDefault) {
  butCategory.set('disabled', false);
}
</script>

<script type="text/javascript">
function jc_menus_show_menus(jsonObject) {
  var table = document.getElementById("menuTable");
  var tbody = table.getElementsByTagName("tbody")[0]
  if (!tbody) {
    tbody = document.createElement('tbody');
    table.appendChild(tbody);
  }
  var rows = tbody.childNodes;
  for (var i = table.rows.length - 1; i > 0; i--) {
    table.deleteRow(i);
  }
  
  for (var i = 0; i < jsonObject.menus.length; i++) {
    var row = document.createElement('tr');
    var column;
    column = document.createElement('td');
    column.innerHTML = '<input type="checkbox" value="' + jsonObject.menus[i].menuId + '" name="removeMenus"/>';
    row.appendChild(column);
    column = document.createElement('td');
    column.innerHTML = jsonObject.menus[i].menuLongDesc;
    row.appendChild(column);
    column = document.createElement('td');
    column.innerHTML = jsonObject.menus[i].siteDomainName;
    row.appendChild(column);
    tbody.appendChild(row);
  }
}

var jc_menus_add_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      var message = document.getElementById("jc_menus_search_message");
      message.innerHTML = "Unexcepted Error: unable to add menus";
      return false;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime); 
    jc_menus_show_menus(jsonObject);
    jc_menus_search_finish();
  },
  failure: function(o) {
    jc_panel_show_error("Unable to add menus");
  }
};

function jc_menus_search_client_callback(value) {
  var response = eval('(' + value + ')');
  var contentId = <c:out value='${contentMaintForm.contentId}'/>

  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/content/contentMaint.do";
  var data = "process=addMenus&contentId=" + contentId;
  data += "&menuWindowTarget=" + response.menuWindowTarget;
  data += "&menuWindowMode=" + response.menuWindowMode;
  for (var i = 0; i < response.menus.length; i++) {
    data += "&addMenus=" + response.menus[i].menuId;
  }
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_menus_add_callback, data);
  return false;
}
</script>
<%@ include file="/admin/include/menusLookup.jsp" %>

<script type="text/javascript">
var jc_menus_remove_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to remove menus");
      return false;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    jc_menus_show_menus(jsonObject);
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to remove menu");
  }
};

function jc_menus_remove() {
  var contentId = <c:out value='${contentMaintForm.contentId}'/>
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/content/contentMaint.do";
  var data = "process=removeMenus&contentId=" + contentId;
  
  var e = document.getElementById("menuTable");
  var checkboxes = new Array();
  jc_traverse_element(e, checkboxes, 'input', 'removeMenus.*');
  
  var removeMenus = new Array();
  var count = 0;
  for (var i = 0; i < checkboxes.length; i++) {
    if (!checkboxes[i].checked) {
      continue;
    }
    data += "&removeMenus=" + checkboxes[i].value;
    removeMenus[count++] = checkboxes[i].value;
  }
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_menus_remove_callback, data);
  return false;
}

function jc_menu_search(p_type, p_args, p_value) {
	jc_menus_search_show(p_value.value);
}

var butMenusOption = [
  	<c:forEach var="siteDomain" items="${contentMaintForm.siteDomains}">
    { text: "Select Menus from ${siteDomain.label}", value: ${siteDomain.value}, onclick: { fn: jc_menu_search } },
    </c:forEach>
    { text: "Remove Selected Menus", value: 2, onclick: { fn: jc_menus_remove } }
];
var butMenus = new YAHOO.widget.Button({
      disabled: true,
      type: "menu", 
      label: "Options", 
      name: "butMenus",
      menu: butMenusOption, 
      container: "butMenusContainer" });
if (siteProfileClassDefault) {
  butMenus.set('disabled', false);
}
</script>

<script type="text/javascript">
var butResetCounter = new YAHOO.widget.Button("butResetCounter", { disabled: true });
if (siteProfileClassDefault) {
  butResetCounter.set('disabled', false);
}
butResetCounter.on("click", jc_reset_counter);

function jc_reset_counter() {
  var contentId = ${contentMaintForm.contentId};
  var url = "/${adminBean.contextPath}/admin/content/contentMaint.do";
  var data = "process=resetCounter&contentId=" + contentId ;
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_reset_counter_callback, data);
  return false;
}

var jc_reset_counter_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: Unable to reset hit counter");
      return false;
    }
    var text = document.getElementById("hitCounter");
    text.innerHTML = '<span class="jc_input_control">0</span>';
    var jsonObject = getJsonObject(o.responseText);
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
  },
  failure: function(o) {
      jc_panel_show_error("Unexcepted Error: Unable to reset hit counter");
  }
};
</script>
</layout:mode>
</html:form>

<script type="text/javascript">
var jc_images_override_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to override images");
      return false;
    }
    jc_images_paint();
  },
  failure: function(o) {
      jc_panel_show_error("Unexcepted Error: unable to override images");
  }
};
function overrideImageLanguage(object) {
  var contentId = <c:out value='${contentMaintForm.contentId}'/>
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/content/contentMaint.do?process=overrideImages&contentId=" + contentId + "&siteProfileClassId=" + siteProfileClassId;
  if (object.checked) {
    url += '&imagesOverride=true';
  }
  else {
    url += '&imagesOverride=false';
  }
  var request = YAHOO.util.Connect.asyncRequest('GET', url, jc_images_override_callback);
  if (object.checked) {
    butImage.set('disabled', false);
  }
  else {
    butImage.set('disabled', true);
  }
}

function image_upload() {
  jc_image_upload_show(${contentMaintForm.contentId});
}
function image_remove() {
  jc_images_remove();
}
var imageOptionsMenu = [
    { text: "Upload new image", value: 1, onclick: { fn: image_upload } },
    { text: "Remove selected images", value: 2, onclick: { fn: image_remove } }
];
if (mode != 'C') {
	var butImage = new YAHOO.widget.Button({
	      disabled: true,
	      type: "menu", 
	      label: "Options", 
	      name: "logoOptions",
	      menu: imageOptionsMenu, 
	      container: "imageOptionContainer" });
	<c:if test="${contentMaintForm.siteProfileClassDefault || contentMaintForm.contentImageOverride}">
		butImage.set('disabled', false);
	</c:if>
}
               
var jc_images_paint_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to extract content images");
      return false;
    }
    var jsonObject = getJsonObject(o.responseText);
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    jc_image_show_images(jsonObject);
  },
  failure: function(o) {
      jc_panel_show_error("Unexcepted Error: unable to extract content images");
  }
};
function jc_images_paint() {
  var contentId = <c:out value='${contentMaintForm.contentId}'/>
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/content/contentMaint.do?process=showImages&contentId=" + contentId + "&siteProfileClassId=" + siteProfileClassId;
  var request = YAHOO.util.Connect.asyncRequest('GET', url, jc_images_paint_callback);
  return false;
}

function jc_image_upload_client_callback(value) {
  var response = eval('(' + value + ')');
  var formName = response.formName;
  YAHOO.util.Connect.setForm(formName, true, true);

  var url = "/${adminBean.contextPath}/admin/content/contentMaint.do?siteProfileClassId=" + siteProfileClassId;
  var response = YAHOO.util.Connect.asyncRequest('GET', url, jc_upload_callback);
}

var jc_upload_callback = {
  upload: function(o) {
    var value = o.responseText.replace(/<\/?pre>/ig, '');
    if (!isJsonResponseValid(value)) {
      jc_image_upload_message('Unable to upload image');
      return false;
    }
    var jsonObject = getJsonObject(value);
    if (jsonObject.status == 'failed') {
      jc_image_upload_message(jsonObject.message);
    }
    else {
      setIdValue("recUpdateBy", jsonObject.recUpdateBy);
      setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
      jc_image_show_images(jsonObject);
      jc_image_upload_finish();
    }
  }
}

function jc_image_show_images(jsonObject) {
  var table = document.getElementById("jc_images_table");
  var tbody = table.getElementsByTagName("tbody")[0]
  if (!tbody) {
    tbody = document.createElement('tbody');
    table.appendChild(tbody);
  }
  var rows = tbody.childNodes;
  for (var i = table.rows.length - 1; i >= 0; i--) {
    table.deleteRow(i);
  }
  var allowEdit = true;
  if (!siteProfileClassDefault) {
    if (!document.contentMaintForm.contentImageOverride.checked) {
      allowEdit = false;
    }
  }
  var row = document.createElement('tr');
  if (jsonObject.defaultImage) {
    var column = document.createElement('td');
    column.className = 'jc_input_label_small';
    column.setAttribute("width", '150');
    column.setAttribute("valign", 'bottom');
    column.setAttribute("align", 'center');
    var html = '';
    html = '<img src="<c:out value="/${adminBean.contextPath}"/>/services/SecureImageProvider.do?type=C&maxsize=100&imageId=' + jsonObject.defaultImage.imageId;
    if (!jsonObject.defaultImage.isLanguageDefault) {
      html += '&siteProfileClassDefault=false';
    }
    html += '"/><br>';
    if (allowEdit) {
      html += '<input type="checkbox" value="' + jsonObject.defaultImage.imageId + '" name="removeImages"/><br>';
    }
    html += jsonObject.defaultImage.imageName + '<br>';
    html += 'Default Image';
    column.innerHTML = html;  
    row.appendChild(column);
  }
  
  for (var i = 0; i < jsonObject.images.length; i++) {
    var image = jsonObject.images[i];
    var column = document.createElement('td');
    column.noWrap = true;
    column.className = 'jc_input_label_small';
    column.setAttribute("width", '150');
    column.setAttribute("valign", 'bottom');
    column.setAttribute("align", 'center');
    var html = '';
    html = '<img src="/${adminBean.contextPath}/services/SecureImageProvider.do?type=C&maxsize=100&imageId=' + image.imageId;
    if (!image.isLanguageDefault) {
      html += '&siteProfileClassDefault=false';
    }
    html += '"/><br>';
    if (allowEdit) {
      html += '<input type="checkbox" value="' + image.imageId + '" name="removeImages"/><br>';
    }
    html += image.imageName + '<br><br>';
    if (allowEdit) {
      html += '<a class="jc_navigation_link" onclick="jc_images_set_default(' + image.imageId + ');" href="javascript:void(0);">Set&nbsp;Default</a>';
    }
    column.innerHTML = html;
    row.appendChild(column);
  }
  var column = document.createElement('td');
  column.setAttribute("width", '100%');
  row.appendChild(column);
  tbody.appendChild(row);
}

function jc_images_remove() {
  var contentId = <c:out value='${contentMaintForm.contentId}'/>
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/content/contentMaint.do";
  var data = "process=removeImages&contentId=" + contentId + "&siteProfileClassId=" + siteProfileClassId;
  
  var e = document.getElementById("jc_images_table");
  var checkboxes = new Array();
  jc_traverse_element(e, checkboxes, 'input', 'removeImages.*');
  
  var removeImages = new Array();
  var count = 0;
  for (var i = 0; i < checkboxes.length; i++) {
    if (!checkboxes[i].checked) {
      continue;
    }
    data += "&removeImages=" + checkboxes[i].value;
    removeImages[count++] = checkboxes[i].value;
  }
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_images_remove_callback, data);
  return false;
}

var jc_images_remove_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to remove image");
      return false;
    }
    var jsonObject = getJsonObject(o.responseText);
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    jc_image_show_images(jsonObject);
  },
  failure: function(o) {
      jc_panel_show_error("Unexcepted Error: unable to remove image");
  }
};

function jc_images_set_default(imageId) {
  var contentId = <c:out value='${contentMaintForm.contentId}'/>
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/content/contentMaint.do";
  var data = "process=defaultImage&contentId=" + contentId + "&createDefaultImageId=" + imageId + "&siteProfileClassId=" + siteProfileClassId;
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_images_set_default_callback, data);
  return false;
}

var jc_images_set_default_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_image_upload_message("Unexcepted Error: unable to set default menu");
      return false;
    }
    var jsonObject = getJsonObject(o.responseText);
    setIdValue("recUpdateBy", jsonObject.recUpdateBy);
    setIdValue("recUpdateDatetime", jsonObject.recUpdateDatetime);
    jc_image_show_images(jsonObject);
  },
  failure: function(o) {
      jc_image_upload_message("Unexcepted Error: unable to set default menu");
  }
};

function submitTranslateForm() {
	submitForm('translate');
}
if (mode != 'C') {
	var butTranslate = new YAHOO.widget.Button("butTranslate", { disabled: true });
	if (translationEnable) {
		butTranslate.set('disabled', false);
	}
	butTranslate.on("click", submitTranslateForm);
}
</script>
<%@ include file="/admin/include/imageUpload.jsp" %>


<%@ include file="/admin/include/contentLookup.jsp" %>
<%@ include file="/admin/include/panel.jsp" %>
<%@ include file="/admin/include/confirm.jsp" %>
<!------------------------------------------------------------------------>