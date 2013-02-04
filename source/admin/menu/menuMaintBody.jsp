<jsp:directive.page import="com.jada.ui.dropdown.DropDownMenuGenerator"/>
<jsp:directive.page import="com.jada.ui.dropdown.DropDownMenu"/>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<jsp:useBean id="adminBean"  type="com.jada.admin.AdminBean" scope="session" />
<jsp:useBean id="menuMaintForm"  type="com.jada.admin.menu.MenuMaintActionForm" scope="request" />
<script type="text/javascript">
var siteProfileClassDefault = ${menuMaintForm.siteProfileClassDefault};
var siteProfileClassId = ${menuMaintForm.siteProfileClassId};
var translationEnable = ${menuMaintForm.translationEnable};
//var jsonMenuList = '${menuMaintForm.jsonMenuList}';
//var jsonObject = getJsonObject(jsonMenuList);
var jsonMenuList = ${menuMaintForm.jsonMenuList};
var menuId = '${menuMaintForm.menuId}';

function submitForm(methodType) {
    if (!siteProfileClassDefault && menuId != '') {
      document.menuMaintForm.menuNameLang.value = document.menuMaintForm.menuNameLang_tmp.value
    }
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}
</script>

<script language="JavaScript">
var id = null;

function addChildNode() {
	document.menuMaintForm.createMenuId.value = id;
	document.menuMaintForm.createMode.value = 'C';
	document.menuMaintForm.process.value = 'create';
	document.menuMaintForm.submit();
}

function createBeforeNode() {
  document.forms[0].createMenuId.value = id;
  document.forms[0].createMode.value = "B";
  document.forms[0].process.value = "create";
  document.forms[0].submit();
}

function createAfterNode() {
  document.forms[0].createMenuId.value = id;
  document.forms[0].createMode.value = "A";
  document.forms[0].process.value = "create";
  document.forms[0].submit();
}

function sequenceNode() {
  document.forms[0].menuId.value = id;
  document.forms[0].createMode.value = "A";
  document.forms[0].process.value = "showSequence";
  document.forms[0].submit();
}

function removeNode() {
  document.forms[0].removeMenuId.value = id;
  document.forms[0].process.value = "removeMenuSet";
  document.forms[0].submit();
}

var catId = null;
function sequenceHomeNode() {
  document.forms[0].menuId.value = id;
  document.forms[0].createMode.value = "A";
  document.forms[0].process.value = "showSequence";
  document.forms[0].submit();
}

function getKey(oTextNode) {
	return oTextNode.id;
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
    	id = getKey(oTextNode);
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

function expand(pos) {
  for (i = 1; i < 9; i++) {
    var id = "location-" + i;
    var object = document.getElementById(id);
    if (pos == i) {
      object.style.display = 'block';
    }
    else {
      object.style.display = 'none';
    }
  }
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

/************************************************/

YAHOO.util.Event.onContentReady('menuLocation', function() {
	var container = document.getElementById('menuLocation');
	for (var i = 0; i < jsonMenuList.menuSets.length; i++) {
		container.appendChild(document.createElement('br'));
		var menuSet = jsonMenuList.menuSets[i];
		var menuSetDiv = document.createElement('div');
		menuSetDiv.setAttribute('id', 'menuSetName_' + menuSet.menuSetName);
		menuSetDiv.style.cursor = 'pointer';
		menuSetDiv.appendChild(document.createTextNode(menuSet.menuSetName));
		var idDiv = document.createElement('div');
		idDiv.style.display = 'none';
		idDiv.innerHTML = menuSet.menuId;
		menuSetDiv.appendChild(idDiv);
		container.appendChild(menuSetDiv);
		
		var menuDiv = document.createElement('div');
		container.appendChild(menuDiv);

		var menuTree = new YAHOO.widget.TreeView(menuDiv);
		for (var j = 0; j < menuSet.menus.length; j++) {
			var node = menuAddNode(menuTree.getRoot(), menuSet.menus[j]);
		}
		menuTree.draw();

        if (siteProfileClassDefault) {
            var homeItemData = [{ text: "Append Child Menu", onclick: { fn: addChildNode } },
                                { text: "Resequence/remove children", onclick: { fn: sequenceNode } }
                            	];
        	if (menuSet.menuSetName != 'MAIN' && menuSet.menuSetName != 'SECONDARY') {
            	homeItemData = [{ text: "Remove menu set", onclick: { fn: removeNode } },
            	            	{ text: "Append Child Menu", onclick: { fn: addChildNode } },
                                { text: "Resequence/remove children", onclick: { fn: sequenceNode } }
                            	];
        	};

        	var contextHomeMenu = new YAHOO.widget.ContextMenu('contextHomeMenu_' + menuSet.menuSetName, {
                trigger: menuSetDiv,
                lazyload: true, 
                itemdata: homeItemData
             });
        	contextHomeMenu.subscribe("triggerContextMenu", onTriggerHomeMenu);
            
            var contextMenu = new YAHOO.widget.ContextMenu('contextMenu_' + menuSet.menuSetName, {
                trigger: menuDiv,
                lazyload: true, 
                itemdata: [
                    { text: "Append Child Menu", onclick: { fn: addChildNode } },
                    { text: "Create menu before", onclick: { fn: createBeforeNode } },
                    { text: "Create menu after", onclick: { fn: createAfterNode } }, 
                    { text: "Resequence/remove children", onclick: { fn: sequenceNode } }
                ] });
            contextMenu.subscribe("triggerContextMenu", onTriggerContextMenu);
        }
	}
} );

function menuAddNode(tree, menu) {
	var node = new YAHOO.widget.TextNode(menu.menuName, tree, false);
	if (menuId == menu.menuId) {
		var parent = node.parent;
		while (parent != null) {
			parent.expanded = true;
			parent = parent.parent;
		}
	}
	node.labelElId = menu.menuId;
	node.labelStyle = 'ygtvlabel_white';
	var url = '/${adminBean.contextPath}/admin/menu/menuMaint.do?process=update&siteProfileClassId=' + siteProfileClassId + '&siteDomainId=' + ${menuMaintForm.siteDomainId} + '&menuId=' + menu.menuId;
	node.href = url;

	var i = 0;
	for (i = 0; i < menu.menus.length; i++) {
		menuAddNode(node, menu.menus[i]);
	}
}

function createMenuSet() {
	submitForm('newMenuSet');
}

YAHOO.util.Event.onAvailable('butMenuSet', function() {
	var butMenuSet = new YAHOO.widget.Button("butMenuSet", { disabled: false });
	butMenuSet.on("click", createMenuSet);
});

</script>

<html:form action="/admin/menu/menuMaint.do" method="post">
<html:hidden property="process" value="list"/>
<html:hidden property="removeMenuId"/> 
<html:hidden property="createMenuId"/> 
<html:hidden property="createMode"/>
<html:hidden property="menuId"/>
<html:hidden property="mode"/>
<html:hidden property="siteDomainId"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>
    Administration - <a href="/${adminBean.contextPath}/admin/menu/menuListing.do?process=back">Menu 
      Listing</a> - Menu Maintenance
    </td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr>
    <td valign="top" width="400">
      <div class="jc_detail_panel_header">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">General Information</span></td>
            <td>
              <div align="right">
              &nbsp;
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div class="jc_detail_panel">
      
      <c:if test="${menuMaintForm.mode != ''}">
      <table width="100%" border="0" cellspacing="0" cellpadding="2">
        <tr>
          <td>
            <table width="100%" border="0" cellspacing="0" cellpadding="2">
              <tr>
                <c:if test="${menuMaintForm.sequence == false}">
                <td valign="top">
                  <table width="100%" border="0" cellspacing="0" cellpadding="2">
                    <tr>
                      <td>
                        <table width="100%" border="0" cellspacing="0" cellpadding="2">
                          <tr> 
                            <td width="0" class="jc_input_label">
                              Menu Name
                              <lang:checkboxSwitch name="menuMaintForm" property="menuNameLangFlag"> - Override language</lang:checkboxSwitch>
                            </td>
                          </tr>
                          <tr> 
                            <td>
                            <lang:text property="menuName" size="40" maxlength="40" styleClass="tableContent"/>
                            </td>
                          </tr>
                          <tr>
                          <tr> 
                            <td width="0" class="jc_input_label">&nbsp;</td>
                          </tr>
                          <tr> 
                            <td width="0" class="jc_input_label">Menu Location</td>
                          </tr>
                          <tr>
                          <td>
                            <table width="350" border="0" cellspacing="0" cellpadding="3" class="jc_bordered_table">
                              <tr>
                                <td>
                                <lang:radio onclick="expand(1)" property="menuType" value="SECT" start="true"/>Category
                                <div id="location-1" style="display:none; padding: 10px 20px 10px 20px">
                                    <html:hidden property="catId"/>
                                    <html:hidden property="catShortTitle"/>
                                    <div style="padding: 5px; border: 1px solid #dcdcdc;">
                                    <table width="100%" border="0" cellspacing="0" cellpadding="3">
                                      <tr>
                                        <td class="jc_input_label" width="100px">Category</td>
                                        <td><div id="catShortTitle_container"><c:out value='${menuMaintForm.catShortTitle}'/></div></td>
                                      </tr>
                                      <c:if test="${menuMaintForm.siteProfileClassDefault}">
                                      <tr>
                                        <td nowrap>
                                          <a href="javascript:void(0);" onclick="jc_category_search_show();" class="jc_navigation_link">Pick Category</a>
                                        </td>
                                        <td></td>
                                      </tr>
                                      </c:if>
                                    </table>
                                    </div>
                                </div>
                                </td>
                              </tr>
                              <tr>
                                <td>
                                  <lang:radio onclick="expand(2)" property="menuType" value="CONT"/>Content
                                  <div id="location-2" style="display:none; padding: 10px 20px 10px 20px">
                                    <html:hidden property="contentId"/>
                                    <html:hidden property="contentTitle"/>
                                    <div style="padding: 5px; border: 1px solid #dcdcdc;">
                                    <table width="100%" border="0" cellspacing="0" cellpadding="3">
                                      <tr>
                                        <td class="jc_input_label" width="100px">Content</td>
                                        <td><div id="contentTitle_container"><c:out value='${menuMaintForm.contentTitle}'/></div></td>
                                      </tr>
                                      <c:if test="${menuMaintForm.siteProfileClassDefault}">
                                      <tr>
                                        <td nowrap>
                                          <a href="javascript:void(0);" onclick="showContentPickWindow();" class="jc_navigation_link">Pick Content</a>
                                        </td>
                                        <td></td>
                                      </tr>
                                      </c:if>
                                    </table>
                                    </div>
                                  </div>
                                </td>
                              </tr>
                              <tr>
                                <td>
                                  <lang:radio onclick="expand(3)" property="menuType" value="ITEM"/>Item
                                  <div id="location-3" style="display:none; padding: 10px 20px 10px 20px">
                                    <html:hidden property="itemId"/>
                                    <html:hidden property="itemNum"/>
                                    <html:hidden property="itemShortDesc"/>
                                    <div style="padding: 5px; border: 1px solid #dcdcdc;">
                                      <table width="100%" border="0" cellspacing="0" cellpadding="3">
                                        <tr>
                                          <td class="jc_input_label" width="100px" nowrap>Item Number</td>
                                          <td width="100%"><div id="itemNum_container"><c:out value='${menuMaintForm.itemNum}'/></div></td>
                                        </tr>
                                        <tr>
                                          <td class="jc_input_label">Description</td>
                                          <td><div id="itemShortDesc_container"><c:out value='${menuMaintForm.itemShortDesc}'/></div></td>
                                        </tr>
                                        <c:if test="${menuMaintForm.siteProfileClassDefault}">
                                        <tr>
                                          <td nowrap>
                                            <a href="javascript:void(0);" onclick="showItemPickWindow();" class="jc_navigation_link">Pick item</a>
                                          </td>
                                          <td></td>
                                        </tr>
                                        </c:if>
                                      </table>
                                    </div>
                                  </div>
                                </td>
                              </tr>
                              <tr>
                                <td>
                                <lang:radio onclick="expand(4)" property="menuType" value="SURL"/>Static URL
                                <div id="location-4" style="display:none; padding: 10px 20px 10px 20px">
                                <div style="padding: 5px; border: 1px solid #dcdcdc;">
                                  <table width="100%" border="0" cellspacing="0" cellpadding="3">
                                    <tr>
                                      <td class="jc_input_label" nowrap>URL</td>
                                      <td width="100%"><layout:text layout="false" property="menuUrl" mode="E,E,E" size="40" maxlength="255" styleClass="tableContent"/></td>
                                    </tr>
                                  </table>
                                </div>
                                </div>
                                </td>
                              </tr>
                              <tr>
                                <td>
                                <lang:radio onclick="expand(5)" property="menuType" value="HOME"/>Home
                                <div id="location-5" style="display:none;">
                                </div>
                                </td>
                              </tr>
                              <tr>
                                <td>
                                <lang:radio onclick="expand(6)" property="menuType" value="CTUS"/>Contact us
                                <div id="location-6" style="display:none;">
                                </div>
                                </td>
                              </tr>
                              <tr>
                                <td><lang:radio onclick="expand(7)" property="menuType" value="SIGI"/>Sign in / customer portal
                                <div id="location-7" style="display:none;">
                                </div>
                                </td>
                              </tr>
                              <tr>
                                <td><lang:radio onclick="expand(8)" property="menuType" value="SIGO"/>Sign out
                                <div id="location-8" style="display:none;">
                                </div>
                                </td>
                              </tr>
                              <tr>
                                <td>
                                <lang:radio onclick="expand(9)" property="menuType" value="NOOP"/>No operation
                                <div id="location-9" style="display:none;">
                                </div>
                                </td>
                              </tr>
                            </table>
                          </td>
                          </tr>
                          <tr> 
                            <td width="0" class="jc_input_label">&nbsp;</td>
                          </tr>
                          <tr> 
                            <td class="jc_input_label">Window Mode</td>
                          </tr>
                          <tr> 
                            <td class="jc_input_control"> <lang:text property="menuWindowMode" size="60" styleClass="tableContent"/> 
                            </td>
                          </tr>
                          <tr> 
                            <td class="jc_input_label">Window Target</td>
                          </tr>
                          <tr> 
                            <td class="jc_input_control" nowrap> <lang:select property="menuWindowTarget" styleClass="tableContent"> 
                              <lang:option value="_self">Current window (_self)</lang:option> 
                              <lang:option value="_blank">New window (_blank)</lang:option> 
                              <lang:option value="_parent">Parent Window (_parent)</lang:option> 
                              <lang:option value="_top">Top of the frameset (_top)</lang:option> 
                              </lang:select> </td>
                          </tr>
                          <tr> 
                            <td class="jc_input_label" width="0">Published</td>
                          </tr>
                          <tr> 
                            <td class="jc_input_label" width="0"><lang:checkbox property="published" value="Y"/></td>
                          </tr>
                        </table>
                      </td>
                      <td>
                      </td>
                    </tr>
                  </table>
                </td>
                </c:if>
                <c:if test="${menuMaintForm.sequence == true}">
                <td valign="top" width="100%">
                  <span class="jc_input_label">Menu - <c:out value="${menuMaintForm.menuName}"/></span>
                  <br>
                  <br>
                  <table width="100%" border="0" cellspacing="0" cellpadding="1">
                    <tr> 
                      <td class="jc_list_table_header" width="100">
                        <div align="center">
                        <html:link href="javascript:submitForm('removeSelected')" styleClass="jc_submit_link">Remove</html:link>
                        </div>
                      </td>
                      <td class="jc_list_table_header">Name</td>
                      <td class="jc_list_table_header" width="100">
                      <div align="center"><html:link href="javascript:submitForm('resequence')" styleClass="jc_submit_link">Resequence</html:link></div> </td>
                      <td class="jc_list_table_header" width="100">Published</td>
                    </tr>
                    <% int index = 0; %>
                    <c:forEach var="childrenMenu" items="${menuMaintForm.childrenMenus}"> 
                    <tr class="jc_list_table_row"> 
                      <td width="100" class="jc_list_table_content"> 
                        <div align="center">
                          <html:hidden indexed="true" name="childrenMenu" property="menuId"/>
                          <html:hidden indexed="true" name="childrenMenu" property="menuName"/>
                          <html:hidden indexed="true" name="childrenMenu" property="menuUrlOrContent"/>
                          <html:hidden indexed="true" name="childrenMenu" property="menuWindowTarget"/>
                          <html:hidden indexed="true" name="childrenMenu" property="menuWindowMode"/>
                          <html:hidden indexed="true" name="childrenMenu" property="published"/>
                          <html:checkbox indexed="true" name="childrenMenu" property="remove"/>
                      </td>
                      <td class="jc_list_table_content" nowrap>
                        <c:out value="${childrenMenu.menuName}"/>
                      </td>
                      <td width="100" class="jc_list_table_content" nowrap> 
                        <div align="center">
                        <html:text indexed="true" name="childrenMenu" property="seqNum" size="2"/>
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
                        <div align="center"><c:out value="${childrenMenu.published}"/></div>
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
      <c:if test="${menuMaintForm.siteProfileClassDefault}">
      <div class="jc_detail_panel_header">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><span class="jc_input_label">Create New Menu Set</span></td>
            <td>
              <button type="button" id="butMenuSet" name="butMenuSet" value="createMenuSet">Create</button>
            </td>
          </tr>
        </table>
      </div>
      <div class="jc_detail_panel">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td class="jc_input_label" nowrap>Menu Set Name</td>
          </tr>
          <tr>
			<td class="jc_input_object"><layout:text layout="false" property="createMenuSetName" mode="E,E,E" styleClass="jc_input_object"/></td>
          </tr>
          <tr>
			<td class="jc_input_object">
			  <span class="jc_input_error"> <logic:messagesPresent property="createMenuSetName" message="true"> 
              <html:messages property="createMenuSetName" id="errorText" message="true"> 
              <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
              </span>
			</td>
          </tr>
        </table>
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
		    <c:if test="${menuMaintForm.mode != ''}">
		      <c:if test="${menuMaintForm.sequence == false}">
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
          <td class="jc_input_error">
            <logic:messagesPresent property="menuId" message="true"> 
            <html:messages property="menuId" id="errorText" message="true"> 
            <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
          </td>
        </tr>
        <tr> 
          <td>
            <div id="home" style="cursor: hand; cursor: pointer">
            <b>Menu Sets</b>
            </div>
          </td>
        </tr>
        <tr> 
          <td> 
            <div id="menuLocation"></div>
          </td>
        </tr>
        <tr> 
          <td>&nbsp;</td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<br>
</html:form>

<!------------------------------------------------------------------------>

<script>
var jc_item_search_panel = null;
function showItemPickWindow() {
  jc_item_search_panel.show();
}
function jc_item_search_client_callback(value) {
  var response = eval('(' + value + ')');
  var itemShortDesc = unescape(response.items[0].itemShortDesc);
  document.getElementById('itemNum_container').innerHTML = response.items[0].itemNum;
  document.getElementById('itemShortDesc_container').innerHTML = itemShortDesc;
  document.menuMaintForm.itemId.value = response.items[0].itemId;
  document.menuMaintForm.itemNum.value = response.items[0].itemNum;
  document.menuMaintForm.itemShortDesc.value = itemShortDesc;
  
  jc_item_search_panel.hide();
  return false;
}

var jc_content_search_panel = null;
function showContentPickWindow() {
  jc_content_search_panel.show();
}
function jc_content_search_client_callback(value) {
  var response = eval('(' + value + ')');
  var contentTitle = unescape(response.contents[0].contentTitle);
  document.getElementById('contentTitle_container').innerHTML = contentTitle;
  document.menuMaintForm.contentId.value = response.contents[0].contentId;
  document.menuMaintForm.contentTitle.value = contentTitle;
  jc_content_search_panel.hide();
  return false;
}


var jc_category_search_panel = null;
function showCategoryPickWindow() {
  jc_category_search_panel.show();
}
function jc_category_search_client_callback(value) {
  var response = eval('(' + value + ')');
  if (response.categories[0].catShortTitle == 'Home') {
    return false;
  }
  document.getElementById('catShortTitle_container').innerHTML = response.categories[0].catShortTitle;
  document.menuMaintForm.catId.value = response.categories[0].catId;
  document.menuMaintForm.catShortTitle.value = response.categories[0].catShortTitle;
  jc_category_search_finish();
  return false;
}

function init_expand() {
  var menuType = document.menuMaintForm.menuType;
  var pos = 0;
  for (i = 0; i < menuType.length; i++) {
    if (menuType[i].type != 'radio') {
      continue;
    }
    if (menuType[i].checked) {
      expand(pos + 1);
    }
    pos++;
  }
}
YAHOO.util.Event.onDOMReady(init_expand);
</script>

<!------------------------------------------------------------------------>

<script>
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
  for (i = 0; i < object.items.length; i++) {
    table.insertRow(table.rows.length);
    var row = table.rows[table.rows.length - 1];
    var cell = null;
    var itemId = object.items[index].itemId;
    var itemNum = object.items[index].itemNum;
    var itemShortDesc = object.items[index].itemShortDesc;
    cell = row.insertCell(0);
    cell.width = "100px";
    cell.innerHTML = "<a href='javascript:void(0)' onclick='jc_item_search_callback_single(\"" + itemId + "\", \"" + itemNum + "\", \"" + escape(itemShortDesc) + "\")'>" + 
                     object.items[index].itemNum + "</a>";
    cell = row.insertCell(1);
    cell.width = "100px";
    cell.innerHTML = object.items[index].itemUpcCd;
    cell = row.insertCell(2);
    cell.innerHTML = object.items[index].itemShortDesc;
    index++;
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
  jc_item_search_client_callback(result);
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
<!------------------------------------------------------------------------>

<script>
function jc_content_search_init() {
  jc_content_search_panel = new YAHOO.widget.Panel("jc_content_search_panel", 
                              { 
                                width: "500px", 
                                visible: false, 
                                constraintoviewport: true ,
                                fixedcenter : true,
                                modal: true
                              } 
                               );
  jc_content_search_panel.render();
}
YAHOO.util.Event.onDOMReady(jc_content_search_init);

var handleFailure = function(o) {
  var message = document.getElementById("jc_content_search_message");
  message.innerHTML = "Error retrieving content list";
}

var handleSuccess = function(o) {
  if (o.responseText == undefined) {
    return;
  }
  var table = document.getElementById("jc_content_search_table");
  while (table.rows.length > 0) {
    table.deleteRow(0);
  }
  
  var object = eval('(' + o.responseText + ')');
  var message = document.getElementById("jc_content_search_message");
  if (object.message) {
    message.innerHTML = object.message;
  }
  else {
    message.innerHTML = "";
  }
  var index = 0;
  for (i = 0; i < object.contents.length; i++) {
    table.insertRow(table.rows.length);
    var row = table.rows[table.rows.length - 1];
    var cell = null;
    var contentId = object.contents[index].contentId;
    var contentTitle = object.contents[index].contentTitle;
    cell = row.insertCell(0);
    cell.width = "100%";
    cell.innerHTML = "<a href='javascript:void(0)' onclick='jc_content_search_callback_single(\"" + contentId + "\", \"" + escape(contentTitle) + "\")'>" + 
                     object.contents[index].contentTitle + "</a>";
    index++;
  }
  
  var div = document.getElementById("jc_content_search_div");
  div.style.display = "block";
}

var jc_content_search_callback =
{
  success: handleSuccess,
  failure: handleFailure
};

function jc_content_search_get() {
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/lookup/contentLookup.do";
  var postData = ""
  postData += "contentTitle=" + document.jc_content_search_form.contentTitle.value;
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_content_search_callback, postData);
}

function jc_content_search_callback_single(contentId, contentTitle) {
  var result = '{"contents": [';
  result += '{"contentId": "' + contentId + '", "contentTitle": "' + contentTitle + '"}';
  result += ']}';
  jc_content_search_client_callback(result);
}

function jc_content_search_callback_multiple() {
}
</script>
<div class=" yui-skin-sam">
<div>
<div id="jc_content_search_panel">
  <div class="hd">Content search</div>
  <div class="bd"> 
    <form name="jc_content_search_form" method="post" action="javascript:void(0)">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td><span class="jc_input_label">Content Title</span></td>
          <td nowrap>
            <a href="javascript:void(0);" onclick="jc_content_search_get()" class="jc_navigation_link">Pick content</a>
          </td>
        </tr>
        <tr> 
          <td> 
            <input type="text" name="contentTitle" class="jc_input_control" size="40">
          </td>
          <td></td>
        </tr>
      </table>
      <div id="jc_content_search_div">
      <hr>
      <div id="jc_content_search_message" class="jc_input_error"></div>
      <div style="height: 200px; overflow: auto; vertical-align: top">
      <table id="jc_content_search_table" width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td width="100%"></td>
        </tr>
      </table>
      </div>
      </div>
    </form>
  </div>
</div>
</div>
</div>


<!------------------------------------------------------------------------>

<%@ include file="/admin/include/categoryLookup.jsp" %>
<%@ include file="/admin/include/confirm.jsp" %>

<!------------------------------------------------------------------------>


