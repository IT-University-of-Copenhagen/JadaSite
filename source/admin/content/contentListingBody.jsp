<jsp:directive.page import="com.jada.ui.dropdown.DropDownMenuGenerator"/>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<script type="text/javascript" src="/${adminBean.contextPath}/admin/web/utility/taskNode.js"></script>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="contentListingForm" scope="session" value="${contentListingForm}"/>
<jsp:useBean id="contentListingForm"  type="com.jada.admin.content.ContentListingActionForm"  scope="session" />
<script language="JavaScript">
var jsonCategoryTree = '${contentListingForm.jsonCategoryTree}';
var jsonObject = getJsonObject(jsonCategoryTree);
var categoryTree = null;

function submitListingForm(methodType) {
    document.contentListingForm.action = '/${adminBean.contextPath}/admin/content/contentListing.do';
    document.contentListingForm.process.value = methodType;

	var container = document.getElementById('catIdContainer');
	var checkedNodes = getCheckedNodes(categoryTree.getRoot().children);
	for (var j = 0; j < checkedNodes.length; j++) {
		var hidden = document.createElement('input');
		hidden.setAttribute('type', 'hidden');
		hidden.setAttribute('name', 'srSelectedCategories');
		hidden.setAttribute('value', checkedNodes[j]);
		container.appendChild(hidden);
	}

    document.contentListingForm.submit();
    return false;
}

function submitMaintForm(methodType) {
    document.contentListingForm.action = '/${adminBean.contextPath}/admin/content/contentMaint.do';
    document.contentListingForm.process.value = methodType;
    document.contentListingForm.submit();
    return false;
}

function handleContentPublishOnStart(type, args, obj) {
    document.contentListingForm.srContentPublishOnStart.value = jc_calendar_callback(type, args, obj);
}

function handleContentPublishOnEnd(type, args, obj) { 
    document.contentListingForm.srContentPublishOnEnd.value = jc_calendar_callback(type, args, obj);
} 

function handleContentExpireOnStart(type, args, obj) { 
    document.contentListingForm.srContentExpireOnStart.value = jc_calendar_callback(type, args, obj);
}

function handleContentExpireOnEnd(type, args, obj) { 
    document.contentListingForm.srContentExpireOnEnd.value = jc_calendar_callback(type, args, obj);
}

YAHOO.namespace("example.calendar");
function init() {
  YAHOO.example.calendar.calContentPublishOnStart = new YAHOO.widget.Calendar("calContentPublishOnStart", "calContentPublishOnStartContainer", { title:"Choose a date:", close:true } );
  YAHOO.example.calendar.calContentPublishOnStart.render();
  YAHOO.example.calendar.calContentPublishOnStart.hide();
  YAHOO.util.Event.addListener("calContentPublishOnStartSwitch", "click", YAHOO.example.calendar.calContentPublishOnStart.show, YAHOO.example.calendar.calContentPublishOnStart, true);
  YAHOO.example.calendar.calContentPublishOnStart.selectEvent.subscribe(handleContentPublishOnStart, YAHOO.example.calendar.calContentPublishOnStart, true); 
  
  YAHOO.example.calendar.calContentPublishOnEnd = new YAHOO.widget.Calendar("calContentPublishOnEnd", "calContentPublishOnEndContainer", { title:"Choose a date:", close:true } );
  YAHOO.example.calendar.calContentPublishOnEnd.render();
  YAHOO.example.calendar.calContentPublishOnEnd.hide();
  YAHOO.util.Event.addListener("calContentPublishOnEndSwitch", "click", YAHOO.example.calendar.calContentPublishOnEnd.show, YAHOO.example.calendar.calContentPublishOnEnd, true);
  YAHOO.example.calendar.calContentPublishOnEnd.selectEvent.subscribe(handleContentPublishOnEnd, YAHOO.example.calendar.calContentPublishOnEnd, true); 

  YAHOO.example.calendar.calContentExpireOnStart = new YAHOO.widget.Calendar("calContentExpireOnStart", "calContentExpireOnStartContainer", { title:"Choose a date:", close:true } );
  YAHOO.example.calendar.calContentExpireOnStart.render();
  YAHOO.example.calendar.calContentExpireOnStart.hide();
  YAHOO.util.Event.addListener("calContentExpireOnStartSwitch", "click", YAHOO.example.calendar.calContentExpireOnStart.show, YAHOO.example.calendar.calContentExpireOnStart, true);
  YAHOO.example.calendar.calContentExpireOnStart.selectEvent.subscribe(handleContentExpireOnStart, YAHOO.example.calendar.calContentExpireOnStart, true); 

  YAHOO.example.calendar.calContentExpireOnEnd = new YAHOO.widget.Calendar("calContentExpireOnEnd", "calContentExpireOnEndContainer", { title:"Choose a date:", close:true } );
  YAHOO.example.calendar.calContentExpireOnEnd.render();
  YAHOO.example.calendar.calContentExpireOnEnd.hide();
  YAHOO.util.Event.addListener("calContentExpireOnEndSwitch", "click", YAHOO.example.calendar.calContentExpireOnEnd.show, YAHOO.example.calendar.calContentExpireOnEnd, true);
  YAHOO.example.calendar.calContentExpireOnEnd.selectEvent.subscribe(handleContentExpireOnEnd, YAHOO.example.calendar.calContentExpireOnEnd, true); 
}
YAHOO.util.Event.addListener(window, "load", init);

function categoryAddNode(tree, category) {
	var node = new YAHOO.widget.TaskNode(category.catShortTitle, category.catId, tree, false);
	node.labelElId = category.catId;
	node.href = '/' + category.catId;
	var i = 0;
	for (i = 0; i < category.categories.length; i++) {
		categoryAddNode(node, category.categories[i]);
	}
}

YAHOO.util.Event.onContentReady('categoryLocation', function() {
	categoryTree = new YAHOO.widget.TreeView("categoryLocation");
	for (var i = 0; i < jsonObject.categories.length; i++) {
		var node = categoryAddNode(categoryTree.getRoot(), jsonObject.categories[i]);
	}
	categoryTree.draw();
} );

</script>
<html:form action="/admin/content/contentListing.do" method="post">
<html:hidden property="process" value="search"/>
<html:hidden property="srPageNo" value=""/>
<div id="catIdContainer"></div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - Content Listing</td>
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
                      <html:submit property="submitButton" value="Search" styleClass="jc_submit_button" onclick="return submitListingForm('search');"/>
                      <html:submit property="submitButton" value="New" styleClass="jc_submit_button" onclick="return submitMaintForm('create');"/>
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
            <td class="jc_input_label">Title </td>
          </tr>
          <tr> 
            <td class="jc_input_control"> <layout:text layout="false" property="srContentTitle" mode="E,E,E" styleClass="jc_input_control"/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label"> <layout:radio layout="false" property="srPublished" mode="E,E,E" value="Y"/>Published 
              <br>
              <layout:radio layout="false" property="srPublished" mode="E,E,E" value="N"/>Not-Published 
              <br>
              <layout:radio layout="false" property="srPublished" mode="E,E,E" value="*"/>All 
              <br>
            </td>
          </tr>
          <tr> 
            <td class="jc_input_label">&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Published between </td>
          </tr>
          <tr> 
            <td class="jc_input_control" nowrap> <layout:text layout="false" property="srContentPublishOnStart" mode="E,E,E" styleClass="jc_input_control"/> 
            <img id="calContentPublishOnStartSwitch" src="../images/icon/image_plus.gif" border="0">
            <div id="calContentPublishOnStartContainer" style="position: absolute; display: none;"></div> 
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control" nowrap>
            <span class="jc_input_error"> 
              <logic:messagesPresent property="srContentPublishOnStart" message="true"> 
                <html:messages property="srContentPublishOnStart" id="errorText" message="true"> 
                  <bean:write name="errorText"/>
                </html:messages> 
              </logic:messagesPresent> 
              </span>
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control"> <layout:text layout="false" property="srContentPublishOnEnd" mode="E,E,E" styleClass="jc_input_control"/> 
            <img id="calContentPublishOnEndSwitch" src="../images/icon/image_plus.gif" border="0">
            <div id="calContentPublishOnEndContainer" style="position: absolute; display: none;"></div> 
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control" nowrap>
            <span class="jc_input_error"> 
              <logic:messagesPresent property="srContentPublishOnEnd" message="true"> 
                <html:messages property="srContentPublishOnEnd" id="errorText" message="true"> 
                  <bean:write name="errorText"/>
                </html:messages> 
              </logic:messagesPresent> 
              </span>
            </td>
          </tr>
          <tr> 
            <td class="jc_input_label">&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Expired between </td>
          </tr>
          <tr> 
            <td class="jc_input_control"> <layout:text layout="false" property="srContentExpireOnStart" mode="E,E,E" styleClass="jc_input_control"/> 
            <img id="calContentExpireOnStartSwitch" src="../images/icon/image_plus.gif" border="0">
            <div id="calContentExpireOnStartContainer" style="position: absolute; display: none;"></div> 
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control" nowrap>
            <span class="jc_input_error"> 
              <logic:messagesPresent property="srContentExpireOnStart" message="true"> 
                <html:messages property="srContentExpireOnStart" id="errorText" message="true"> 
                  <bean:write name="errorText"/>
                </html:messages> 
              </logic:messagesPresent> 
              </span>
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control"> <layout:text layout="false" property="srContentExpireOnEnd" mode="E,E,E" styleClass="jc_input_control"/> 
            <img id="calContentExpireOnEndSwitch" src="../images/icon/image_plus.gif" border="0">
            <div id="calContentExpireOnEndContainer" style="position: absolute; display: none;"></div> 
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control" nowrap>
            <span class="jc_input_error"> 
              <logic:messagesPresent property="srContentExpireOnEnd" message="true"> 
                <html:messages property="srContentExpireOnEnd" id="errorText" message="true"> 
                  <bean:write name="errorText"/>
                </html:messages> 
              </logic:messagesPresent> 
              </span>
            </td>
          </tr>
          <tr> 
            <td class="jc_input_label">&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Updated by </td>
          </tr>
          <tr> 
            <td class="jc_input_control">
              <html:select property="srUpdateBy"> 
                <html:option value="All"/> 
                <html:options property="srSelectUsers"/> 
              </html:select> 
            </td>
          </tr>
          <tr> 
            <td class="jc_input_label">&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Created by </td>
          </tr>
          <tr> 
            <td class="jc_input_control"> 
              <html:select property="srCreateBy"> 
                <html:option value="All"/> 
                <html:options property="srSelectUsers"/> 
              </html:select> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
          </tr>
          <tr> 
            <td class="jc_input_label">Select categories to show</td>
          </tr>
          <tr> 
            <td>
            <div id="categoryLocation"></div>
            </td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
          </tr>
        </table>
      </div>
    </td>
    <td width="100%" valign="top" class="jc_list_panel_open"> 
      <c:if test="${contentListingForm.contents != null}">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td>Content Listing 
              Result</td>
            <td> 
              <div align="right">
                <html:submit property="submitButton" value="Remove" styleClass="jc_submit_button" onclick="return submitListingForm('remove');"/>
                <html:submit property="submitButton" value="New" styleClass="jc_submit_button" onclick="return submitMaintForm('create');"/>
              </div>
            </td>
          </tr>
		</table>
		<div align="right">
        <br>
        <table width="0%" border="0" cellspacing="0" cellpadding="0" class="jc_input_label">
          <tr>
            <c:if test="${contentListingForm.pageNo > 1}">
            <td>
              <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/content/contentListing.do?process=list&srPageNo=<c:out value="${contentListingForm.pageNo - 1}"/>">&lt;</a>
            </td>
            </c:if>
            <td>
              <c:forEach begin="${contentListingForm.startPage}" end="${contentListingForm.endPage}" var="index">
                <c:choose>
                  <c:when test="${index == contentListingForm.pageNo}">
                    <span class="jc_navigation_line"><c:out value="${index}"/></span>
                  </c:when>
                  <c:otherwise>
                    <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/content/contentListing.do?process=list&srPageNo=<c:out value='${index}'/>"><c:out value="${index}"/></a>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
              &nbsp;
            </td>
            <c:if test="${contentListingForm.pageNo < contentListingForm.pageCount}">
            <td>
              <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/content/contentListing.do?process=list&srPageNo=<c:out value="${contentListingForm.pageNo + 1}"/>">&gt;</a>
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
        <table width="100%" border="0" cellspacing="0" cellpadding="1">
           <tr> 
            <td class="jc_list_table_header"></td>
            <td class="jc_list_table_header">Title</td>
            <td class="jc_list_table_header"><div align="center">Publish</div></td>
            <td class="jc_list_table_header">Publish On</td>
            <td class="jc_list_table_header">Expire On</td>
          </tr>
          <c:forEach var="content" items="${contentListingForm.contents}">
          <tr> 
            <td width="50" class="jc_list_table_content">
              <div align="center">
                <html:hidden name="content" property="contentId" indexed="true"/>
                <html:hidden name="content" property="contentTitle" indexed="true"/>
                <html:hidden name="content" property="contentTitle" indexed="true"/>
                <html:hidden name="content" property="published" indexed="true"/>
                <html:hidden name="content" property="contentPublishOn" indexed="true"/>
                <html:hidden name="content" property="contentExpireOn" indexed="true"/>
                <html:checkbox indexed="true" name="content" property="remove" value="Y" styleClass="tableContent"/>
              </div>
            </td>
            <td width="60%" class="jc_list_table_content">
              <a href="/${adminBean.contextPath}/admin/content/contentMaint.do?process=edit&contentId=${content.contentId}">${content.contentTitle}</a>
            </td>
            <td width="60" class="jc_list_table_content" nowrap><div align="center"><c:out value="${content.published}"/></div></td>
            <td width="110" class="jc_list_table_content" nowrap><c:out value="${content.contentPublishOn}"/></td>
            <td width="110" class="jc_list_table_content" nowrap><c:out value="${content.contentExpireOn}"/></td>
          </tr>
          </c:forEach>
        </table>
      </c:if>
    </td>
  </tr>
</table>
</html:form>