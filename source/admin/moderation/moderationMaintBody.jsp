<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="moderationMaintForm" scope="session" value="${moderationMaintForm}"/>
<jsp:useBean id="moderationMaintForm"  type="com.jada.admin.moderation.ModerationMaintActionForm" scope="session"/>
<script language="JavaScript">
function submitMaintForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/moderation/moderationMaint.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}
function showPage(srPageNo) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/moderation/moderationMaint.do';
    document.forms[0].srPageNo.value = srPageNo;
    document.forms[0].process.value = 'list';
    document.forms[0].submit();
    return false;
}

function handleRecCreateDatetimeStart(type, args, obj) { 
    document.forms[0].srRecCreateDatetimeStart.value = jc_calendar_callback(type, args, obj);
}

function handleRecCreateDatetimeEnd(type, args, obj) { 
    document.forms[0].srRecCreateDatetimeEnd.value = jc_calendar_callback(type, args, obj);
} 


YAHOO.namespace("example.calendar");
function init() {
  YAHOO.example.calendar.calRecCreateDatetimeStart = new YAHOO.widget.Calendar("calRecCreateDatetimeStart", "calRecCreateDatetimeStartContainer", { title:"Choose a date:", close:true } );
  YAHOO.example.calendar.calRecCreateDatetimeStart.render();
  YAHOO.example.calendar.calRecCreateDatetimeStart.hide();
  YAHOO.util.Event.addListener("calRecCreateDatetimeStartSwitch", "click", YAHOO.example.calendar.calRecCreateDatetimeStart.show, YAHOO.example.calendar.calRecCreateDatetimeStart, true);
  YAHOO.example.calendar.calRecCreateDatetimeStart.selectEvent.subscribe(handleRecCreateDatetimeStart, YAHOO.example.calendar.calRecCreateDatetimeStart, true); 

  YAHOO.example.calendar.calRecCreateDatetimeEnd = new YAHOO.widget.Calendar("calRecCreateDatetimeEnd", "calRecCreateDatetimeEndContainer", { title:"Choose a date:", close:true } );
  YAHOO.example.calendar.calRecCreateDatetimeEnd.render();
  YAHOO.example.calendar.calRecCreateDatetimeEnd.hide();
  YAHOO.util.Event.addListener("calRecCreateDatetimeEndSwitch", "click", YAHOO.example.calendar.calRecCreateDatetimeEnd.show, YAHOO.example.calendar.calRecCreateDatetimeEnd, true);
  YAHOO.example.calendar.calRecCreateDatetimeEnd.selectEvent.subscribe(handleRecCreateDatetimeEnd, YAHOO.example.calendar.calRecCreateDatetimeEnd, true); 
}
YAHOO.util.Event.addListener(window, "load", init);
</script>
<html:form action="/admin/moderation/moderationMaint.do" method="post">
<html:hidden property="process" value="search"/>
<html:hidden property="srPageNo"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - Comments moderation</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td valign="top" width="0%"> 
      <div class="jc_search_panel_open"> 
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td> 
              <table class="jc_panel_table_header" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td>Search</td>
                  <td> 
                    <div align="right"> 
                      <html:submit property="submitButton" value="Search" styleClass="jc_submit_button"/>
                    </div>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label"><layout:checkbox layout="false" property="srModerated" mode="E,E,E" value="on" styleClass="jc_input_control"/> Moderated</td>
          </tr>
          <tr> 
            <td class="jc_input_label"><layout:checkbox layout="false" property="srNotModerated" mode="E,E,E" value="on" styleClass="jc_input_control"/> Not moderated</td>
          </tr>
          <tr> 
            <td class="jc_input_label"><div style="padding-left: 25px">and</div></td>
          </tr>
          <tr> 
            <td class="jc_input_label"><layout:checkbox layout="false" property="srFlagged" mode="E,E,E" value="on" styleClass="jc_input_control"/> Flagged by visitor</td>
          </tr>
          <tr> 
            <td class="jc_input_label"><layout:checkbox layout="false" property="srNotFlagged" mode="E,E,E" value="on" styleClass="jc_input_control"/> Not Flagged by visitor</td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Entered between </td>
          </tr>
          <tr> 
            <td class="jc_input_control" nowrap> <layout:text layout="false" property="srRecCreateDatetimeStart" mode="E,E,E" styleClass="jc_input_control"/> 
            <img id="calRecCreateDatetimeStartSwitch" src="../images/icon/image_plus.gif" border="0">
            <div id="calRecCreateDatetimeStartContainer" style="position: absolute; display: none;"></div> 
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control" nowrap>
            <span class="jc_input_error"> 
              <logic:messagesPresent property="srRecCreateDatetimeStart" message="true"> 
                <html:messages property="srRecCreateDatetimeStart" id="errorText" message="true"> 
                  <bean:write name="errorText"/>
                </html:messages> 
              </logic:messagesPresent> 
              </span>
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control"> <layout:text layout="false" property="srRecCreateDatetimeEnd" mode="E,E,E" styleClass="jc_input_control"/> 
            <img id="calRecCreateDatetimeEndSwitch" src="../images/icon/image_plus.gif" border="0">
            <div id="calRecCreateDatetimeEndContainer" style="position: absolute; display: none;"></div> 
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control" nowrap>
            <span class="jc_input_error"> 
              <logic:messagesPresent property="srRecCreateDatetimeEnd" message="true"> 
                <html:messages property="srRecCreateDatetimeEnd" id="errorText" message="true"> 
                  <bean:write name="errorText"/>
                </html:messages> 
              </logic:messagesPresent> 
              </span>
            </td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
          </tr>
        </table>
      </div>
    </td>
    <td width="100%" valign="top" class="jc_list_panel_open"> 
      <c:if test="${moderationMaintForm.comments != null}">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td>Content Listing 
              Result</td>
            <td> 
              <div align="right">
                <html:submit property="submitButton" value="Reject selected" styleClass="jc_submit_button" onclick="return submitMaintForm('reject');"/>
                <html:submit property="submitButton" value="Approve selected" styleClass="jc_submit_button" onclick="return submitMaintForm('approve');"/>
              </div>
            </td>
          </tr>
		</table>
		<div align="right">
        <br>
        <table width="0%" border="0" cellspacing="0" cellpadding="0" class="jc_input_label">
          <tr>
            <c:if test="${moderationMaintForm.pageNo > 1}">
            <td>
              <a class="jc_navigation_link" href="javascript:showPage(${moderationMaintForm.pageNo - 1})">&lt;</a>
            </td>
            </c:if>
            <td>
              <c:forEach begin="${moderationMaintForm.startPage}" end="${moderationMaintForm.endPage}" var="index">
                <c:choose>
                  <c:when test="${index == moderationMaintForm.pageNo}">
                    <span class="jc_navigation_line">${index}</span>
                  </c:when>
                  <c:otherwise>
                    <a class="jc_navigation_link" href="javascript:showPage(${index})">${index}</a>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
              &nbsp;
            </td>
            <c:if test="${moderationMaintForm.pageNo < moderationMaintForm.pageCount}">
            <td>
              <a class="jc_navigation_link" href="javascript:showPage(${moderationMaintForm.pageNo + 1})">&gt;</a>
            </td>
            </c:if>
            <td>&nbsp;&nbsp;</td>
          </tr>
        </table>
        <br>
        </div>
        <span class="jc_input_error">
        <logic:messagesPresent property="error" message="true"> 
          <html:messages property="error" id="errorText" message="true"> 
          <bean:write name="errorText"/><br><br>
          </html:messages> 
        </logic:messagesPresent>
        </span>
        <table width="100%" border="0" cellspacing="0" cellpadding="5">
          <tr>
            <td class="jc_list_table_header"></td>
            <td width="50px" class="jc_list_table_header"><div align="center">Status</div></td>
            <td width="50px" class="jc_list_table_header"><div align="center">Flagged</div></td>
            <td class="jc_list_table_header">Comments</td>
          </tr>
          <c:forEach var="comments" items="${moderationMaintForm.comments}">
          <tr> 
            <td width="50" class="jc_list_table_content" valign="top">
              <div align="center">
                <html:hidden name="comments" property="commentId" indexed="true"/>
                <html:hidden name="comments" property="commentTitle" indexed="true"/>
                <html:hidden name="comments" property="comment" indexed="true"/>
                <html:hidden name="comments" property="custPublicName" indexed="true"/>
                <html:hidden name="comments" property="custEmail" indexed="true"/>
                <html:hidden name="comments" property="recUpdatedDatetime" indexed="true"/>
                <html:hidden name="comments" property="commentSource" indexed="true"/>
                <html:hidden name="comments" property="commentSourceTitle" indexed="true"/>
                <html:checkbox indexed="true" name="comments" property="select" value="Y" styleClass="tableContent"/>
              </div>
            </td>
            <td width="50px" valign="top"><div align="center"><c:out value='${comments.commentApproved}'/></div></td>
            <td width="50px" valign="top"><div align="center"><c:out value='${comments.commentModeration}'/></div></td>
            <td width="100%">
              <b><c:out value='${comments.commentTitle}'/></b><br>
              <c:out value='${comments.comment}'/><br><br>
              Referring to 
              <c:choose>
                <c:when test="${comments.commentSource eq 'C'}">
                  content - <html:link styleClass="jc_simple_link" href="javascript:jc_content_show('${comments.commentSource}', '${comments.commentSourceId}')"> <c:out value='${comments.commentSourceTitle}'/></html:link>
                </c:when>
                <c:otherwise>
                  item - <html:link styleClass="jc_simple_link" href="javascript:jc_item_show('${comments.commentSource}', '${comments.commentSourceId}')"> <c:out value='${comments.commentSourceTitle}'/></html:link>
                </c:otherwise>
              </c:choose>
              <br>
              Posted by <c:out value='${comments.custPublicName}'/> (<c:out value='${comments.custEmail}'/>) on 
              <c:out value='${comments.recUpdatedDatetime}'/>
              <br>
              Agree (<c:out value='${comments.agreeCount}'/>) Disagree (<c:out value='${comments.disagreeCount}'/>)
              <br><br>
            </td>
          </c:forEach>
        </table>
      </c:if>
    </td>
  </tr>
</table>

<script type="text/javascript">
/*
Category selection pop-up window
*/
var jc_content_panel;
var resize;
function jc_content_init() {
  jc_content_panel_obj = new YAHOO.widget.Panel("jc_content_panel", 
                              { 
                                draggable: true,
                                width: "600px",
                                visible: false, 
                                constraintoviewport: true ,
                                fixedcenter : true,
                                modal: true
                              } 
                               );
  jc_content_panel_obj.render();
  resize = new YAHOO.util.Resize('jc_content_panel', { 
                                     handles: ['br'], 
                                     autoRatio: false, 
                                     status: false, 
                                     minWidth: 380, 
                                     minHeight: 400 
                                     }); 
}
function jc_content_show(sourceType, contentId) {
  var container = document.getElementById("jc_content_container");
  container.innerHTML = "";
  jc_content_get(contentId);
  jc_content_panel_obj.show();
}
function jc_item_show(sourceType, itemId) {
  var container = document.getElementById("jc_content_container");
  container.innerHTML = "";
  jc_item_get(itemId);
  jc_content_panel_obj.show();
}

function jc_content_finish() {
  jc_content_panel_obj.hide();
}
YAHOO.util.Event.onDOMReady(jc_content_init);

var jc_content_callback =
{
  success: function(o) {
    if (o.responseText == undefined) {
      return;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    var container = document.getElementById("jc_content_container");
    container.innerHTML = '<span class="jc_text_header">' + jsonObject.contentTitle + '</span>' + '<br>' +
                          jsonObject.contentShortDesc + '<br>' +
                          jsonObject.contentDesc + '<br>';
    container.innerHTML += '<hr>';
    if (jsonObject.comments) {
      container.innerHTML += '<b>Comments</b><br><br>';
      for (i = 0; i < jsonObject.comments.length; i++) {
        var comment = jsonObject.comments[i];
        container.innerHTML += '<b>' + comment.commentTitle + '</b>' + '<br>';
        container.innerHTML += comment.comment + '<br><br>';
        container.innerHTML += 'Posted by ' + comment.custPublicName + ' (' + comment.custEmail + ') on ' + comment.recCreateDatetime + '<br>';
        container.innerHTML += 'Agree (' + comment.agreeCount + '), Disagree (' + comment.disagreeCount + ')';
        if (comment.moderation == 'Y') {
          container.innerHTML += ', <font color="red">Alert moderation</font>';
        }
        if (comment.commentApproved == 'Y') {
          container.innerHTML += ', <font color="green">Accepted</font>';
        }
        if (comment.commentApproved == 'N') {
          container.innerHTML += ', <font color="red">Rejected</font>';
        }
        container.innerHTML += '<br><br>';
      }
    }
    
  },
  failure: function(o) {
    var message = document.getElementById("jc_content_message");
    message.innerHTML = "Error retrieving content information";
  }
};

var jc_item_callback =
{
  success: function(o) {
    if (o.responseText == undefined) {
      return;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    var container = document.getElementById("jc_content_container");
    container.innerHTML = '<span class="jc_text_header">' + jsonObject.itemShortDesc + '</span>' + '<br>' +
                          'Item Num: ' + jsonObject.itemNum + '<br>' +
                          'Upc Code: ' + jsonObject.itemUpcCd + '<br>' +
                          jsonObject.itemDesc + '<br>';
    container.innerHTML += '<hr>';
    if (jsonObject.comments) {
      container.innerHTML += '<b>Comments</b><br><br>';
      for (i = 0; i < jsonObject.comments.length; i++) {
        var comment = jsonObject.comments[i];
        container.innerHTML += '<b>' + comment.commentTitle + '</b>' + '<br>';
        container.innerHTML += comment.comment + '<br><br>';
        container.innerHTML += 'Posted by ' + comment.custPublicName + ' (' + comment.custEmail + ') on ' + comment.recCreateDatetime + '<br>';
        container.innerHTML += 'Agree (' + comment.agreeCount + '), Disagree (' + comment.disagreeCount + ')';
        if (comment.moderation == 'Y') {
          container.innerHTML += ', <font color="red">Alert moderation</font>';
        }
        if (comment.commentApproved == 'Y') {
          container.innerHTML += ', <font color="green">Accepted</font>';
        }
        if (comment.commentApproved == 'N') {
          container.innerHTML += ', <font color="red">Rejected</font>';
        }
        container.innerHTML += '<br><br>';
      }
    }
    
  },
  failure: function(o) {
    var message = document.getElementById("jc_content_message");
    message.innerHTML = "Error retrieving content information";
  }
};

function jc_item_get(itemId) {
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/moderation/moderationMaint.do?process=showItem&itemId=" + itemId;
  var request = YAHOO.util.Connect.asyncRequest('GET', url, jc_item_callback);
}

function jc_content_get(contentId) {
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/moderation/moderationMaint.do?process=showContent&contentId=" + contentId;
  var request = YAHOO.util.Connect.asyncRequest('GET', url, jc_content_callback);
}


</script>
<div class=" yui-skin-sam">
<div>
<div id="jc_content_panel">
  <div class="hd">Content</div>
  <div class="bd"> 
    <div id="jc_content_message" class="jc_input_error"></div>
    <div id="jc_content_container" style="height: 500px; overflow: auto;">
    
    </div>
  </div>
</div>
</div>
</div>

</html:form>
