<jsp:directive.page import="com.jada.ui.dropdown.DropDownMenuGenerator"/>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<script type="text/javascript" src="/${adminBean.contextPath}/admin/web/utility/dataNode.js"></script>
<jsp:useBean id="adminBean"  type="com.jada.admin.AdminBean"  scope="session" />
<jsp:useBean id="categoryMaintForm"  type="com.jada.admin.category.CategoryMaintActionForm" scope="request" />
<script type="text/javascript">
var siteProfileClassDefault = ${categoryMaintForm.siteProfileClassDefault};
var siteProfileClassId = ${categoryMaintForm.siteProfileClassId};
var translationEnable = ${categoryMaintForm.translationEnable};
var jsonCategoryTree = '${categoryMaintForm.jsonCategoryTree}';
var jsonObject = getJsonObject(jsonCategoryTree);
var categoryTree = null;
var catId = '${categoryMaintForm.catId}';

function submitForm(methodType) {
	if (document.categoryMaintForm.mode.value != '') {
	    if (!siteProfileClassDefault) {
	      document.categoryMaintForm.catShortTitleLang.value = document.categoryMaintForm.catShortTitleLang_tmp.value;
	      document.categoryMaintForm.catTitleLang.value = document.categoryMaintForm.catTitleLang_tmp.value;
	      document.categoryMaintForm.metaKeywordsLang.value = document.categoryMaintForm.metaKeywordsLang_tmp.value;
	      document.categoryMaintForm.metaDescriptionLang.value = document.categoryMaintForm.metaDescriptionLang_tmp.value;
	      jc_editor_catDescLang.saveHTML();
	    }
	    else {
	      jc_editor_catDesc.saveHTML();
	    }
	}
    document.forms[0].action = '/${adminBean.contextPath}/admin/category/categoryMaint.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function submitSimpleForm(methodType) {
    document.forms[0].action = '/${adminBean.contextPath}/admin/category/categoryMaint.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}
YAHOO.util.Event.onContentReady('categoryLocation', function() {
	categoryTree = new YAHOO.widget.TreeView("categoryLocation");
	var homeDiv = document.getElementById('home');
	homeDiv.appendChild(document.createTextNode(jsonObject.catShortTitle));
	var homeIdDiv = document.createElement('div');
	homeIdDiv.style.display = 'none';
	homeIdDiv.appendChild(document.createTextNode(jsonObject.catId));
	homeDiv.appendChild(homeIdDiv);
	for (var i = 0; i < jsonObject.categories.length; i++) {
		var node = categoryAddNode(categoryTree.getRoot(), jsonObject.categories[i]);
	}
	categoryTree.draw();
} );

function categoryAddNode(tree, category) {
	var node = new YAHOO.widget.TextNode(category.catShortTitle, tree, false);
	if (catId == category.catId) {
		var parent = node.parent;
		while (parent != null) {
			parent.expanded = true;
			parent = parent.parent;
		}
	}
	node.labelElId = category.catId;
	node.labelStyle = 'ygtvlabel_white';
	var url = '/${adminBean.contextPath}/admin/category/categoryMaint.do?process=update&siteProfileClassId=' + siteProfileClassId + '&catId=' + category.catId;
	node.href = url;
	var i = 0;
	for (i = 0; i < category.categories.length; i++) {
		categoryAddNode(node, category.categories[i]);
	}
}

var jc_customAttributes_remove_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to remove custom attributes");
      return false;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    paintAttribute();
    jc_panel_show_message("Custom attributes removed successfully");
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to remove custom attributes");
  }
};

function removeCustomAttributes() {
	var url = "/${adminBean.contextPath}/admin/category/categoryMaint.do";
	var data = "process=removeCustomAttributes&catId=" + catId;
	
	var e = document.getElementById("customAttributesContainer");
	var checkboxes = new Array();
	jc_traverse_element(e, checkboxes, 'input', 'customAttribIds');

	for (var i = 0; i < checkboxes.length; i++) {
	  if (!checkboxes[i].checked) {
	    continue;
	  }
	  data += "&customAttribIds=" + checkboxes[i].value;
	}
	var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_customAttributes_remove_callback, data);
	return false;
}

var addCustomAttribute_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to add custom attribute");
      return false;
    }
    paintAttribute();
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to add custom attribute");
  }
};
function jc_customAttribute_search_client_callback(object) {
	jc_customAttribute_search_panel.hide();
   
	var url = "/${adminBean.contextPath}/admin/category/categoryMaint.do";
	var data = "process=addCustomAttribute&catId=" + catId + "&customAttribId=" + object.customAttribId;
	var request = YAHOO.util.Connect.asyncRequest('POST', url, addCustomAttribute_callback, data);
	return false;
}

function addCustomAttribute() {
	jc_customAttribute_search_show(jc_customAttribute_search_client_callback, 'customAttributeCompare');
}

var showCustomAttributes_callback =
{
 	success: function(o) {
 	    if (!isJsonResponseValid(o.responseText)) {
 			jc_panel_show_error("Unexcepted Error: Unable to extract custom attributes");
 			return false;
 	    }
 	    var jsonObject = getJsonObject(o.responseText);
 	    var node = document.getElementById("customAttributesContainer");
 	    node.innerHTML = '';
 	    var table = document.createElement('table');
 	    table.setAttribute('id', 'customAttrbutes');
 	    table.setAttribute('cellpadding', '5');
 	    table.className = 'jc_nobordered_table';
// 	    table.setAttribute('width', '400');
 	    var body = document.createElement('tbody');
 	    table.appendChild(body);
 	    if (jsonObject.customAttributes.length > 0) {
 		    var row = document.createElement('tr');
 		    body.appendChild(row);
 		    var col = document.createElement('td');
 		    col.setAttribute('width', '50');
 		    col.appendChild(document.createTextNode(''));
			row.appendChild(col);
 		    col = document.createElement('td');
 		    col.setAttribute('width', '100%');
 		    col.className = 'jc_input_label';
 		    col.appendChild(document.createTextNode('Custom Attribute'));
			row.appendChild(col);
 	    }
 	   	for (var i = 0; i < jsonObject.customAttributes.length; i++) {
 		    var customAttribute = jsonObject.customAttributes[i];
 		    var row = document.createElement('tr');
 		    body.appendChild(row);
 		    col = document.createElement('td');
 		    var input = document.createElement('input');
 		    input.setAttribute('type', 'checkbox');
 		    input.setAttribute('name', 'customAttribIds');
 		    input.setAttribute('value', customAttribute.customAttribId);
 		 	col.appendChild(input);
 		 	row.appendChild(col);
 		 	
 		    col = document.createElement('td');
 		    col.appendChild(document.createTextNode(customAttribute.customAttribDesc));
			row.appendChild(col);
 		}
 		node.appendChild(table);
   },
   failure: function(o) {
       jc_panel_show_error("Unexcepted Error: Unable to extract custom attribute");
   }
};
 
function paintAttribute() {
	var url = "/${adminBean.contextPath}/admin/category/categoryMaint.do";
	var data = "process=getCustomAttributes&catId=" + catId;
	var request = YAHOO.util.Connect.asyncRequest('POST', url, showCustomAttributes_callback, data);
}
	
if (catId != '') {
	YAHOO.util.Event.onContentReady('customAttributesContainer', function() {
		if (catId != '') {
			paintAttribute();
		}
	} );

	YAHOO.util.Event.onContentReady('butCustomAttributesContainer', function() {
		var buttonMenu = [
				{ text: "Add Attribute", value: 'Add Custom Attribute', onclick: { fn: addCustomAttribute } }, 
				{ text: "Remove Attributes", value: 'Remove Custom Attributes', onclick: { fn: removeCustomAttributes } }
		];
		var butContainer = document.getElementById('butCustomAttributesContainer');
		var menu = new YAHOO.widget.Button({ 
	                          type: "menu", 
	                          disabled: false,
	                          label: "Options", 
	                          name: "menu",
	                          menu: buttonMenu, 
	                          container: butContainer });
	} );
}
</script>
<html:form action="/admin/category/categoryMaint.do" method="post">
<html:hidden property="process" value="list"/>
<html:hidden property="mode"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - Category Maintenance</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr>
    <td valign="top" width="600">
      <div class="jc_detail_panel_header">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td>
              <span class="jc_input_label">General Information</span>
            </td>
            <td>
              <div align="right">
              &nbsp;
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div class="jc_detail_panel">
      <c:if test="${categoryMaintForm.mode != ''}">
      <table width="100%" border="0" cellspacing="0" cellpadding="2">
        <tr>
          <td>
            <table width="100%" border="0" cellspacing="0" cellpadding="2">
              <tr>
                <c:if test="${categoryMaintForm.sequence == false}">
                <td valign="top">
                  <table width="100%" border="0" cellspacing="0" cellpadding="2">
                    <tr>
                      <td>
                        <table width="100%" border="0" cellspacing="0" cellpadding="2">
                          <tr> 
                            <td width="0" class="jc_input_label">
                              Category short title
                              <lang:checkboxSwitch name="categoryMaintForm" property="catShortTitleLangFlag"> - Override language</lang:checkboxSwitch>
                            </td>
                          </tr>
                          <tr> 
                            <td>
                              <lang:text property="catShortTitle" size="20" maxlength="20" styleClass="tableContent"/>
                            </td>
                          </tr>
                          <tr> 
                            <td class="jc_input_label">
                              <span class="jc_input_error">
                              <logic:messagesPresent property="catShortTitle" message="true"> 
                              <html:messages property="catShortTitle" id="errorText" message="true"> 
                              <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
                              </span>
                            </td>
                          </tr>
                          <tr> 
                            <td class="jc_input_label">
                              Title
                              <lang:checkboxSwitch name="categoryMaintForm" property="catTitleLangFlag"> - Override language</lang:checkboxSwitch>
                            </td>
                          </tr>
                          <tr> 
                            <td class="jc_input_control">
                              <lang:text property="catTitle" size="40" maxlength="40" styleClass="tableContent"/>
                            </td>
                          </tr>
                          <tr> 
                            <td class="jc_input_label">
                              Description
                              <lang:checkboxEditorSwitch name="categoryMaintForm" property="catDescLangFlag"> - Override language</lang:checkboxEditorSwitch>
                            </td>
                          </tr>
                          <tr> 
                            <td class="jc_input_control">
                              <lang:editorText property="catDesc" height="200" width="100%"/> 
                            </td>
                          </tr>
                          <tr> 
                            <td class="jc_input_label">
                              Meta keywords
                              <lang:checkboxSwitch name="categoryMaintForm" property="metaKeywordsLangFlag"> - Override language</lang:checkboxSwitch>
                            </td>
                          </tr>
                          <tr> 
                            <td class="jc_input_control">
                              <lang:textarea property="metaKeywords" cols="80" rows="2" /> 
                            </td>
                          </tr>
                          <tr> 
                            <td class="jc_input_label">
                              Meta description
                              <lang:checkboxSwitch name="categoryMaintForm" property="metaDescriptionLangFlag"> - Override language</lang:checkboxSwitch>
                            </td>
                          </tr>
                          <tr> 
                            <td class="jc_input_control">
                              <lang:textarea property="metaDescription" cols="80" rows="2"/> 
                            </td>
                          </tr>

                          <tr> 
                            <td class="jc_input_label" width="0">Published</td>
                          </tr>
                          <tr> 
                            <td class="jc_input_label" width="0">
                              <lang:checkbox property="published"/>
                            </td>
                          </tr>
                        </table>
                      </td>
                      <td>
                      </td>
                    </tr>
                  </table>
                </td>
                </c:if>
                <c:if test="${categoryMaintForm.sequence == true}">
                <html:hidden name="categoryMaintForm" property="catShortTitle"/>
          		<span class="jc_input_label">Category - ${categoryMaintForm.catShortTitle}</span>
          		<br>
                <td valign="top" width="100%">
	              <span class="jc_input_error">
	                <logic:messagesPresent property="error" message="true"> 
	                  <html:messages property="error" id="errorText" message="true"> 
	                    <bean:write name="errorText"/>
	                  </html:messages>
	                </logic:messagesPresent> 
	              </span>
                  <table width="100%" border="0" cellspacing="0" cellpadding="1">
                    <tr>
                      <td>&nbsp;</td>
                    </tr>
                    <tr> 
                      <td class="jc_list_table_header" width="100">
                        <div align="center"><html:link href="javascript:submitSimpleForm('removeSelected')" styleClass="jc_submit_link">Remove</html:link></div>
                      </td>
                      <td class="jc_list_table_header" nowrap>Short Title </td>
                      <td class="jc_list_table_header" width="100"> 
                        <div align="center"> <html:link href="javascript:submitSimpleForm('resequence')" styleClass="jc_submit_link">Resequence</html:link> 
                        </div>
                      </td>
                      <td class="jc_list_table_header" width="100"><div align="center">Published</div></td>
                    </tr>
                    <% int index = 0; %>
                    <c:forEach var="childrenCategory" items="${categoryMaintForm.childrenCategories}">
                    <tr class="jc_list_table_row"> 
                      <td width="100" class="jc_list_table_content">
                        <html:hidden indexed="true" name="childrenCategory" property="catId"/>
                        <html:hidden indexed="true" name="childrenCategory" property="catShortTitle"/>
                        <html:hidden indexed="true" name="childrenCategory" property="catTitle"/>
                        <html:hidden indexed="true" name="childrenCategory" property="catDesc"/>
                        <html:hidden indexed="true" name="childrenCategory" property="published"/>
                        <div align="center"> 
                          <html:checkbox indexed="true" name="childrenCategory" property="remove"/>
                        </div>
                      </td>
                      <td nowrap class="jc_list_table_content">
                        <c:out value="${childrenCategory.catShortTitle}"/>
                      </td>
                      <td width="100" class="jc_list_table_content" nowrap> 
                        <div align="center"> 
                          <html:text indexed="true" name="childrenCategory" property="seqNum" size="2"/>
                          <span class="jc_input_error">
                            <logic:messagesPresent property="seqNum_<%= index %>" message="true"> 
                              <html:messages property="seqNum_<%= index %>" id="errorText" message="true"> 
                                <bean:write name="errorText"/>
                              </html:messages>
                            </logic:messagesPresent> 
                          </span>
                        </div>
                      </td>
                      <td width="100" class="jc_list_table_content" nowrap> 
                        <div align="center"><c:out value="${childrenCategory.published}"/></div>
                      </td>
                    </tr>
                    <% index++; %>
                    </c:forEach> 
                  </table>
                </td>
                </c:if>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      </c:if>
      </div>
      <c:if test="${categoryMaintForm.catId != null && categoryMaintForm.sequence == false}">
      <div class="jc_detail_panel_header">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">Sub-sites to include</span></td>
          </tr>
        </table>
      </div>
	  <div class="jc_detail_panel">
      <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr> 
        <c:forEach var="siteDomain" items="${categoryMaintForm.siteDomains}">
        <tr class="jc_list_table_row"> 
          <td width="50" class="jc_list_table_content">
            <html:hidden indexed="true" name="siteDomain" property="siteDomainId"/>
            <html:hidden indexed="true" name="siteDomain" property="siteName"/>
            <html:checkbox indexed="true" name="siteDomain" property="checked"/>
          </td>
          <td nowrap width="100%" class="jc_list_table_content">
            ${siteDomain.siteName}
          </td>
        </tr>
        </c:forEach> 
      </table>
	  </div>
      <div class="jc_detail_panel_header">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">Search filtering attributes</span></td>
		      <td>
		        <div align="right" id="butCustomAttributesContainer">
		        </div>
		      </td>
          </tr>
        </table>
      </div>
	  <div class="jc_detail_panel">
		<div id="customAttributesContainer">
		</div>
		<br>
	  </div>
	  </c:if>
    </td>
    <td valign="top">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
		  <tr>
		    <td>
		      Profile
		      <html:select property="siteProfileClassId" onchange="javascript:submitForm('language')"> 
		        <html:optionsCollection property="siteProfileClassBeans" value="value" label="label"/> 
		      </html:select>
		      <button type="button" id="butTranslate" name="butTranslate" value="Translate">Translate</button>
		    </td>
		    <td>
		      <c:if test="${categoryMaintForm.mode != ''}">
		        <c:if test="${categoryMaintForm.sequence == false}">
		          <div align="right">
		            <html:submit property="submitButton" value="Save" styleClass="jc_submit_button" onclick="return submitForm('save');"/>
		            <html:submit property="submitButton" value="Remove" styleClass="jc_submit_button" onclick="return submitForm('remove');"/>
		          </div>
		        </c:if>
		      </c:if>
		    </td>
		  </tr>
		</table>
      <br>
      <table border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td>
            <html:hidden property="createCatId"/>
            <html:hidden property="createMode"/>
            <html:hidden property="catId"/>
            <div id="home" style="cursor: hand; cursor: pointer">
            </div>
          </td>
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
    </td>
 </tr>
</table>
</html:form>
<script language="JavaScript">
//jc_create_yui_treemenu('categoryTree', 'categoryLocation');

var id = null;

function addChildNode() {
  document.forms[0].createCatId.value = id;
  document.forms[0].createMode.value = "C";
  document.forms[0].process.value = "create";
  document.forms[0].submit();
}

function createBeforeNode() {
  document.forms[0].createCatId.value = id;
  document.forms[0].createMode.value = "B";
  document.forms[0].process.value = "create";
  document.forms[0].submit();
}

function createAfterNode() {
  document.forms[0].createCatId.value = id;
  document.forms[0].createMode.value = "A";
  document.forms[0].process.value = "create";
  document.forms[0].submit();
}

function sequenceNode() {
  document.forms[0].catId.value = id;
  document.forms[0].createMode.value = "A";
  document.forms[0].process.value = "showSequence";
  document.forms[0].submit();
}

function sequenceHomeNode() {
  document.forms[0].catId.value = id;
  document.forms[0].createMode.value = "A";
  document.forms[0].process.value = "showSequence";
  document.forms[0].submit();
}

function getKey(oTextNode) {
  var key = "";
  var nodes = oTextNode.childNodes;
  for (var i = 0; i < nodes.length; i++) {
    if (nodes[i].tagName && nodes[i].tagName.toUpperCase() == 'DIV') {
      key = nodes[i].innerHTML;
    }
  }
  return key;
}

function onTriggerContextMenu(p_oEvent) {
    function getTextNodeFromEventTarget(p_oTarget) {
//        if (p_oTarget.tagName.toUpperCase() == "A" && p_oTarget.className == "ygtvlabel") {
        if (p_oTarget.tagName.toUpperCase() == "A") {
            return p_oTarget;
        }
        else {
            if (p_oTarget.parentNode && 
                    p_oTarget.parentNode.nodeType == 1) {
                return getTextNodeFromEventTarget(p_oTarget.parentNode);
            }
        }
    }
    var oTextNode = getTextNodeFromEventTarget(this.contextEventTarget);
    if (oTextNode) {
        id = oTextNode.id;
    }
    else {
        this.cancel();
    }
}

function onTriggerHomeMenu(p_oEvent) {
  var object = this.contextEventTarget;
  var nodes = object.childNodes;
  for (var i = 0; i < nodes.length; i++) {
    if (nodes[i].tagName && nodes[i].tagName.toUpperCase() == 'DIV') {
      id = nodes[i].innerHTML;
      return;
    }
  }
}

if (siteProfileClassDefault) {
var oContextMenu = new YAHOO.widget.ContextMenu("contextMenu", {
                                                trigger: "categoryLocation",
                                                lazyload: true, 
                                                itemdata: [
                                                    { text: "Append Child Category", onclick: { fn: addChildNode } },
                                                    { text: "Create category before", onclick: { fn: createBeforeNode } },
                                                    { text: "Create category after", onclick: { fn: createAfterNode } }, 
                                                    { text: "Resequence/remove children", onclick: { fn: sequenceNode } }
                                                ] });
oContextMenu.subscribe("triggerContextMenu", onTriggerContextMenu);

var homeContextMenu = new YAHOO.widget.ContextMenu("contextMenu1", {
                                                trigger: "home",
                                                lazyload: true, 
                                                itemdata: [
                                                    { text: "Append Child Category", onclick: { fn: addChildNode } },
                                                    { text: "Resequence/remove children", onclick: { fn: sequenceHomeNode } }
                                                ] });
homeContextMenu.subscribe("triggerContextMenu", onTriggerHomeMenu);
}

function submitTranslateForm() {
	submitForm('translate');
}
YAHOO.util.Event.onAvailable('butTranslate', function() {
	var butTranslate = new YAHOO.widget.Button("butTranslate", { disabled: true });
	if (translationEnable) {
		butTranslate.set('disabled', false);
	}
	butTranslate.on("click", submitTranslateForm);
});
</script>

<!------------------------------------------------------------------------>

<%@ include file="/admin/include/confirm.jsp" %>
<%@ include file="/admin/include/customAttributeLookup.jsp" %>

<!------------------------------------------------------------------------>

