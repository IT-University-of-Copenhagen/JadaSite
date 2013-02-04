<jsp:directive.page import="com.jada.ui.dropdown.DropDownMenuGenerator"/>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<script type="text/javascript" src="/${adminBean.contextPath}/admin/web/utility/taskNode.js"></script>

<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="itemListingForm" scope="session" value="${itemListingForm}"/>
<jsp:useBean id="itemListingForm"  type="com.jada.admin.item.ItemListingActionForm"  scope="session" />
<script language="JavaScript">
var jsonCategoryTree = '${itemListingForm.jsonCategoryTree}';
var jsonObject = getJsonObject(jsonCategoryTree);
var categoryTree = null;

function submitNewForm(page) {
    document.forms[0].action = '/${adminBean.contextPath}/admin/item/itemMaint.do';
    document.forms[0].process.value = "create";
    document.forms[0].submit();
    return false;
}

function submitRemoveForm(methodType) {
    document.forms[0].action = '/${adminBean.contextPath}/admin/item/itemListing.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function submitMaintForm(methodType) {
    document.forms[0].action = '/${adminBean.contextPath}/admin/item/itemMaint.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function submitForm() {
	var container = document.getElementById('catIdContainer');
	var checkedNodes = getCheckedNodes(categoryTree.getRoot().children);
	for (var j = 0; j < checkedNodes.length; j++) {
		var hidden = document.createElement('input');
		hidden.setAttribute('type', 'hidden');
		hidden.setAttribute('name', 'srSelectedCategories');
		hidden.setAttribute('value', checkedNodes[j]);
		container.appendChild(hidden);
	}
}

function handleItemPublishOnStart(type, args, obj) { 
    document.forms[0].srItemPublishOnStart.value = jc_calendar_callback(type, args, obj);
}

function handleItemPublishOnEnd(type, args, obj) { 
    document.forms[0].srItemPublishOnEnd.value = jc_calendar_callback(type, args, obj);
} 

function handleItemExpireOnStart(type, args, obj) { 
    document.forms[0].srItemExpireOnStart.value = jc_calendar_callback(type, args, obj);
}

function handleItemExpireOnEnd(type, args, obj) { 
    document.forms[0].srItemExpireOnEnd.value = jc_calendar_callback(type, args, obj);
} 

YAHOO.namespace("example.calendar");
function init() {
  YAHOO.example.calendar.calItemPublishOnStart = new YAHOO.widget.Calendar("calItemPublishOnStart", "calItemPublishOnStartContainer", { title:"Choose a date:", close:true } );
  YAHOO.example.calendar.calItemPublishOnStart.render();
//  YAHOO.example.calendar.calItemPublishOnStart.hide();
  YAHOO.util.Event.addListener("calItemPublishOnStartSwitch", "click", YAHOO.example.calendar.calItemPublishOnStart.show, YAHOO.example.calendar.calItemPublishOnStart, true);
  YAHOO.example.calendar.calItemPublishOnStart.selectEvent.subscribe(handleItemPublishOnStart, YAHOO.example.calendar.calItemPublishOnStart, true); 

  YAHOO.example.calendar.calItemPublishOnEnd = new YAHOO.widget.Calendar("calItemPublishOnEnd", "calItemPublishOnEndContainer", { title:"Choose a date:", close:true } );
  YAHOO.example.calendar.calItemPublishOnEnd.render();
//  YAHOO.example.calendar.calItemPublishOnEnd.hide();
  YAHOO.util.Event.addListener("calItemPublishOnEndSwitch", "click", YAHOO.example.calendar.calItemPublishOnEnd.show, YAHOO.example.calendar.calItemPublishOnEnd, true);
  YAHOO.example.calendar.calItemPublishOnEnd.selectEvent.subscribe(handleItemPublishOnEnd, YAHOO.example.calendar.calItemPublishOnEnd, true); 

  YAHOO.example.calendar.calItemExpireOnStart = new YAHOO.widget.Calendar("calItemExpireOnStart", "calItemExpireOnStartContainer", { title:"Choose a date:", close:true } );
  YAHOO.example.calendar.calItemExpireOnStart.render();
//  YAHOO.example.calendar.calItemExpireOnStart.hide();
  YAHOO.util.Event.addListener("calItemExpireOnStartSwitch", "click", YAHOO.example.calendar.calItemExpireOnStart.show, YAHOO.example.calendar.calItemExpireOnStart, true);
  YAHOO.example.calendar.calItemExpireOnStart.selectEvent.subscribe(handleItemExpireOnStart, YAHOO.example.calendar.calItemExpireOnStart, true); 

  YAHOO.example.calendar.calItemExpireOnEnd = new YAHOO.widget.Calendar("calItemExpireOnEnd", "calItemExpireOnEndContainer", { title:"Choose a date:", close:true } );
  YAHOO.example.calendar.calItemExpireOnEnd.render();
//  YAHOO.example.calendar.calItemExpireOnEnd.hide();
  YAHOO.util.Event.addListener("calItemExpireOnEndSwitch", "click", YAHOO.example.calendar.calItemExpireOnEnd.show, YAHOO.example.calendar.calItemExpireOnEnd, true);
  YAHOO.example.calendar.calItemExpireOnEnd.selectEvent.subscribe(handleItemExpireOnEnd, YAHOO.example.calendar.calItemExpireOnEnd, true); 
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
<html:form action="/admin/item/itemListing.do" method="post">
<html:hidden property="process" value="search"/>
<html:hidden property="srPageNo"/>
<div id="catIdContainer"></div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - Item Listing</td>
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
                      <html:submit property="submitButton" value="Search" styleClass="jc_submit_button" onclick="return submitForm();"/>
                      <html:submit property="submitButton" value="New" styleClass="jc_submit_button" onclick="return submitNewForm('');"/>
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
            <td class="jc_input_label">Item SKU Code </td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srItemSkuCd" mode="E,E,E" styleClass="jc_input_object"/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Item Number </td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srItemNum" mode="E,E,E" styleClass="jc_input_object"/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Item UPC Code</td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srItemUpcCd" mode="E,E,E" styleClass="jc_input_object"/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Item Short Description </td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srItemShortDesc" mode="E,E,E" styleClass="jc_input_object"/> 
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
            <td class="jc_input_object"> <layout:text layout="false" property="srItemPublishOnStart" mode="E,E,E" styleClass="jc_input_object"/> 
            <img id="calItemPublishOnStartSwitch" src="../images/icon/image_plus.gif" border="0">
            <div id="calItemPublishOnStartContainer" style="position: absolute; display:none"></div> 
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control" nowrap>
            <span class="jc_input_error"> 
              <logic:messagesPresent property="srItemPublishOnStart" message="true"> 
                <html:messages property="srItemPublishOnStart" id="errorText" message="true"> 
                  <bean:write name="errorText"/>
                </html:messages> 
              </logic:messagesPresent> 
              </span>
            </td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srItemPublishOnEnd" mode="E,E,E" styleClass="jc_input_object"/> 
            <img id="calItemPublishOnEndSwitch" src="../images/icon/image_plus.gif" border="0">
            <div id="calItemPublishOnEndContainer" style="position: absolute; display:none"></div> 
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control" nowrap>
            <span class="jc_input_error"> 
              <logic:messagesPresent property="srItemPublishOnEnd" message="true"> 
                <html:messages property="srItemPublishOnEnd" id="errorText" message="true"> 
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
            <td class="jc_input_object"> <layout:text layout="false" property="srItemExpireOnStart" mode="E,E,E" styleClass="jc_input_object"/> 
            <img id="calItemExpireOnStartSwitch" src="../images/icon/image_plus.gif" border="0">
            <div id="calItemExpireOnStartContainer" style="position: absolute; display:none"></div> 
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control" nowrap>
            <span class="jc_input_error"> 
              <logic:messagesPresent property="srItemExpireOnStart" message="true"> 
                <html:messages property="srItemExpireOnStart" id="errorText" message="true"> 
                  <bean:write name="errorText"/>
                </html:messages> 
              </logic:messagesPresent> 
              </span>
            </td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srItemExpireOnEnd" mode="E,E,E" styleClass="jc_input_object"/> 
            <img id="calItemExpireOnEndSwitch" src="../images/icon/image_plus.gif" border="0">
            <div id="calItemExpireOnEndContainer" style="position: absolute; display:none"></div> 
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control" nowrap>
            <span class="jc_input_error"> 
              <logic:messagesPresent property="srItemExpireOnEnd" message="true"> 
                <html:messages property="srItemExpireOnEnd" id="errorText" message="true"> 
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
            <td class="jc_input_object">
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
            <td class="jc_input_object"> 
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
            <td class="jc_input_label">Item type</td>
          </tr>
          <tr> 
            <td class="jc_input_object">
            <html:checkbox property="srItemTypeRegular">Regular</html:checkbox>
            </td>
          </tr>
          <tr> 
            <td class="jc_input_object">
            <html:checkbox property="srItemTypeTemplate">Template</html:checkbox>
            </td>
          </tr>
          <tr> 
            <td class="jc_input_object">
            <html:checkbox property="srItemTypeSku">SKU</html:checkbox>
            </td>
          </tr>
          <tr> 
            <td class="jc_input_object">
            <html:checkbox property="srItemTypeStaticBundle">Static Bundle</html:checkbox>
            </td>
          </tr>
          <tr> 
            <td class="jc_input_object">
            <html:checkbox property="srItemTypeRecommandBundle">Recommanded Bundle</html:checkbox>
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
      <c:if test="${itemListingForm.items != null}">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td>Item Listing 
              Result</td>
            <td> 
              <div align="right">
                <html:submit property="submitButton" value="Remove" styleClass="jc_submit_button" onclick="return submitRemoveForm('remove');"/>
                <html:submit property="submitButton" value="New" styleClass="jc_submit_button" onclick="return submitMaintForm('create');"/>
              </div>
            </td>
          </tr>
		</table>
		<div align="right">
        <br>
        <table width="0%" border="0" cellspacing="0" cellpadding="0" class="jc_input_label">
          <tr>
            <c:if test="${itemListingForm.pageNo > 1}">
            <td>
              <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/item/itemListing.do?process=list&srPageNo=<c:out value="${itemListingForm.pageNo - 1}"/>">&lt;</a>
            </td>
            </c:if>
            <td>
              <c:forEach begin="${itemListingForm.startPage}" end="${itemListingForm.endPage}" var="index">
                <c:choose>
                  <c:when test="${index == itemListingForm.pageNo}">
                    <span class="jc_navigation_line"><c:out value="${index}"/></span>
                  </c:when>
                  <c:otherwise>
                    <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/item/itemListing.do?process=list&srPageNo=<c:out value='${index}'/>"><c:out value="${index}"/></a>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
              &nbsp;
            </td>
            <c:if test="${itemListingForm.pageNo < itemListingForm.pageCount}">
            <td>
              <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/item/itemListing.do?process=list&srPageNo=<c:out value="${itemListingForm.pageNo + 1}"/>">&gt;</a>
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
            <td class="jc_list_table_header">Description</td>
            <td class="jc_list_table_header">Item Number</td>
            <td class="jc_list_table_header">Item Sku Code</td>
            <td class="jc_list_table_header"><div align="center">Publish</div></td>
            <td class="jc_list_table_header">Publish On</td>
            <td class="jc_list_table_header">Expire On</td>
          </tr>
          <c:forEach var="item" items="${itemListingForm.items}">
          <tr class="jc_list_table_row"> 
            <td width="50" class="jc_list_table_content">
              <div align="center">
                <html:hidden name="item" property="itemId" indexed="true"/>
                <html:hidden name="item" property="itemNum" indexed="true"/>
                <html:hidden name="item" property="itemSkuCd" indexed="true"/>
                <html:hidden name="item" property="itemShortDesc" indexed="true"/>
                <html:hidden name="item" property="published" indexed="true"/>
                <html:hidden name="item" property="itemPublishOn" indexed="true"/>
                <html:hidden name="item" property="itemExpireOn" indexed="true"/>
                <html:checkbox indexed="true" name="item" property="remove" value="Y" styleClass="tableContent"/>
              </div>
            </td>
            <td width="50%" class="jc_list_table_content">
              <html:link page="/admin/item/itemMaint.do?process=edit" paramId="itemId" paramName="item" paramProperty="itemId">
                ${item.itemShortDesc}
              </html:link>
            </td>
            <td width="20%" class="jc_list_table_content" nowrap>
              <c:out value="${item.itemNum}"/><br>
            </td>
            <td width="20%" class="jc_list_table_content" nowrap>
              <c:out value="${item.itemSkuCd}"/><br>
            </td>
            <td width="60" class="jc_list_table_content" nowrap><div align="center"><c:out value="${item.published}"/></div></td>
            <td width="110" class="jc_list_table_content" nowrap><c:out value="${item.itemPublishOn}"/></td>
            <td width="110" class="jc_list_table_content" nowrap><c:out value="${item.itemExpireOn}"/></td>
          </tr>
          </c:forEach>
        </table>
      </c:if>
    </td>
  </tr>
</table>
</html:form>
