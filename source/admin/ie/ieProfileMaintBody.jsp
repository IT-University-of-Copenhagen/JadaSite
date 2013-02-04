<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="ieProfileMaintForm" scope="request" value="${ieProfileMaintForm}"/>
<script language="JavaScript">
var ieProfileHeaderId = '${ieProfileMaintForm.ieProfileHeaderId}';
var mode = '${ieProfileMaintForm.mode}';

function submitForm(type) {
    document.ieProfileMaintForm.process.value = type;
    document.ieProfileMaintForm.submit();
    return false;
}
function submitBackForm(type) {
    document.ieProfileMaintForm.action = "/${adminBean.contextPath}/admin/ie/ieProfileListing.do";
    document.ieProfileMaintForm.process.value = "back";
    document.ieProfileMaintForm.submit();
    return false;
}

function addCategoryMenu() {
	document.ieProfileMaintForm.ieProfileGroupName.value = "categories";
	submitForm('addGroup');
}

function removeCategoryMenu() {
	document.ieProfileMaintForm.ieProfileGroupName.value = "categories";
	submitForm('removeGroup');
}

YAHOO.util.Event.onContentReady('categoryContainer', function() {
	var categoryMenu = [ { text: "Add", onclick: { fn: addCategoryMenu } }, 
	  			   		 { text: "Remove", onclick: { fn: removeCategoryMenu } }
	  	         		];
    var button = new YAHOO.widget.Button({type: "menu",
        								  label: 'Options',
        								  name: 'menu',
        								  menu: categoryMenu,
                                          container: 'categoryContainer' });
} );

function addItemRelatedMenu() {
	document.ieProfileMaintForm.ieProfileGroupName.value = "itemsRelated";
	submitForm('addGroup');
}

function removeItemRelatedMenu() {
	document.ieProfileMaintForm.ieProfileGroupName.value = "itemsRelated";
	submitForm('removeGroup');
}

YAHOO.util.Event.onContentReady('itemRelatedContainer', function() {
	var itemRelatedMenu = [ { text: "Add", onclick: { fn: addItemRelatedMenu } }, 
	  			   		 { text: "Remove", onclick: { fn: removeItemRelatedMenu } }
	  	         		];
    var button = new YAHOO.widget.Button({type: "menu",
        								  label: 'Options',
        								  name: 'menu',
        								  menu: itemRelatedMenu,
                                          container: 'itemRelatedContainer' });
} );


function addItemCrossSellMenu() {
	document.ieProfileMaintForm.ieProfileGroupName.value = "itemsCrossSell";
	submitForm('addGroup');
}

function removeItemCrossSellMenu() {
	document.ieProfileMaintForm.ieProfileGroupName.value = "itemsCrossSell";
	submitForm('removeGroup');
}

YAHOO.util.Event.onContentReady('itemCrossSellContainer', function() {
	var itemCrossSellMenu = [ { text: "Add", onclick: { fn: addItemCrossSellMenu } }, 
	  			   		 { text: "Remove", onclick: { fn: removeItemCrossSellMenu } }
	  	         		];
    var button = new YAHOO.widget.Button({type: "menu",
        								  label: 'Options',
        								  name: 'menu',
        								  menu: itemCrossSellMenu,
                                          container: 'itemCrossSellContainer' });
} );


function addItemUpSellMenu() {
	document.ieProfileMaintForm.ieProfileGroupName.value = "itemsUpSell";
	submitForm('addGroup');
}

function removeItemUpSellMenu() {
	document.ieProfileMaintForm.ieProfileGroupName.value = "itemsUpSell";
	submitForm('removeGroup');
}

YAHOO.util.Event.onContentReady('itemUpSellContainer', function() {
	var itemUpSellMenu = [ { text: "Add", onclick: { fn: addItemUpSellMenu } }, 
	  			   		 { text: "Remove", onclick: { fn: removeItemUpSellMenu } }
	  	         		];
    var button = new YAHOO.widget.Button({type: "menu",
        								  label: 'Options',
        								  name: 'menu',
        								  menu: itemUpSellMenu,
                                          container: 'itemUpSellContainer' });
} );

function addItemTierPriceMenu() {
	document.ieProfileMaintForm.ieProfileGroupName.value = "itemTierPrices";
	submitForm('addGroup');
}

function removeItemTierPriceMenu() {
	document.ieProfileMaintForm.ieProfileGroupName.value = "itemTierPrices";
	submitForm('removeGroup');
}

YAHOO.util.Event.onContentReady('itemTierPriceContainer', function() {
	var itemTierPriceMenu = [ { text: "Add", onclick: { fn: addItemTierPriceMenu } }, 
	  			   		 { text: "Remove", onclick: { fn: removeItemTierPriceMenu } }
	  	         		];
    var button = new YAHOO.widget.Button({type: "menu",
        								  label: 'Options',
        								  name: 'menu',
        								  menu: itemTierPriceMenu,
                                          container: 'itemTierPriceContainer' });
} );

function addItemAttributeMenu() {
	document.ieProfileMaintForm.ieProfileGroupName.value = "itemAttributeDetails";
	submitForm('addGroup');
}

function removeItemAttributeMenu() {
	document.ieProfileMaintForm.ieProfileGroupName.value = "itemAttributeDetails";
	submitForm('removeGroup');
}

YAHOO.util.Event.onContentReady('itemAttributesContainer', function() {
	var itemAttributeMenu = [ { text: "Add", onclick: { fn: addItemAttributeMenu } }, 
	  			   		 { text: "Remove", onclick: { fn: removeItemAttributeMenu } }
	  	         		];
    var button = new YAHOO.widget.Button({type: "menu",
        								  label: 'Options',
        								  name: 'menu',
        								  menu: itemAttributeMenu,
                                          container: 'itemAttributesContainer' });
} );

function addItemImageMenu() {
	document.ieProfileMaintForm.ieProfileGroupName.value = "itemImages";
	submitForm('addGroup');
}

function removeItemImageMenu() {
	document.ieProfileMaintForm.ieProfileGroupName.value = "itemImages";
	submitForm('removeGroup');
}

YAHOO.util.Event.onContentReady('itemImageContainer', function() {
	var itemImageMenu = [ { text: "Add", onclick: { fn: addItemImageMenu } }, 
	  			   		 { text: "Remove", onclick: { fn: removeItemImageMenu } }
	  	         		];
    var button = new YAHOO.widget.Button({type: "menu",
        								  label: 'Options',
        								  name: 'menu',
        								  menu: itemImageMenu,
                                          container: 'itemImageContainer' });
} );

function addOtherMenu() {
	document.ieProfileMaintForm.ieProfileGroupName.value = "others";
	submitForm('addGroup');
}

function removeOtherMenu() {
	document.ieProfileMaintForm.ieProfileGroupName.value = "others";
	submitForm('removeGroup');
}

YAHOO.util.Event.onContentReady('othersContainer', function() {
	var otherMenu = [ { text: "Add", onclick: { fn: addOtherMenu } }, 
	  			   		 { text: "Remove", onclick: { fn: removeOtherMenu } }
	  	         		];
    var button = new YAHOO.widget.Button({type: "menu",
        								  label: 'Options',
        								  name: 'menu',
        								  menu: otherMenu,
                                          container: 'othersContainer' });
} );

var jc_value_panel = null;

YAHOO.util.Event.onContentReady('jc_value_panel', function() {
	jc_value_panel = new YAHOO.widget.Panel("jc_value_panel", 
	        { 
	          width: "400px", 
	          visible: false, 
	          constraintoviewport: true ,
	          fixedcenter : true,
	          modal: true
	        } 
	);
	jc_value_panel.render();
} );

var activeObject = null;

function editValue(object) {
	var field = document.getElementById('areaValue');
	field.value = object.value;
	activeObject = object;

	jc_value_panel.show();
}

YAHOO.util.Event.onContentReady('jc_value_button_container', function() {
    var button = new YAHOO.widget.Button({label: 'Save',
                                          id: 'jc_value_button',
                                          disabled: false,
                                          container: 'jc_value_button_container' });
    button.on('click', function() {
    	var field = document.getElementById('areaValue');
    	activeObject.value = field.value;
    	jc_value_panel.hide();
    });
} );


</script>
<html:form method="post" action="/admin/ie/ieProfileMaint"> <html:hidden property="mode"/> 
<html:hidden property="process" value=""/>
<html:hidden property="ieProfileHeaderId"/> 
<html:hidden property="ieProfileGroupName" value=""/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - <a href="/${adminBean.contextPath}/admin/ie/ieProfileListing.do?process=back">Import/Export Profile 
      Listing</a> - Import/Export Profile Maintenance</td>
  </tr>
</table>
<br>
<table width="950" border="0" cellspacing="0" cellpadding="5">
  <tr> 
    <td valign="top" width="300px"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td align="right" class="jc_panel_table_header"> 
            <table width="0%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <c:if test="${ieProfileMaintForm.systemRecord != 'Y'}">
                <td>
                  <html:submit property="submitButton" value="Save" styleClass="jc_submit_button" onclick="return submitForm('save');"/>&nbsp;
                </td>
                <layout:mode value="edit"> 
                <td><html:submit property="submitButton" value="Remove" styleClass="jc_submit_button" onclick="return submitForm('remove');"/>&nbsp;</td>
                </layout:mode>
                </c:if>
                <td><html:submit property="submitButton" value="Cancel" styleClass="jc_submit_button" onclick="return submitBackForm('back');"/>&nbsp;</td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <table width="100%" border="0" cellspacing="0" cellpadding="5px" class="borderTable">
        <tr> 
          <td class="jc_input_label">Profile Name</td>
          <td class="jc_input_control"> <layout:text layout="false" property="ieProfileHeaderName" size="60" mode="E,E,E" styleClass="tableContent"/> 
            <span class="jc_input_error"> <logic:messagesPresent property="ieProfileHeaderName" message="true"> 
            <html:messages property="ieProfileHeaderName" id="errorText" message="true"> 
            <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
            </span> </td>
        </tr>
        <tr> 
          <td class="jc_input_label">Profile type</td>
          <td>
            <html:select property="ieProfileType"> 
              <html:option value="I">Import</html:option>
              <html:option value="E">Export</html:option>
            </html:select>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">System</td>
          <td>
            ${ieProfileMaintForm.systemRecord}
            <html:hidden property="systemRecord"/> 
          </td>
        </tr>
      </table>
    </td>
    <td width="100%" valign="top">
	<c:if test="${ieProfileMaintForm.mode != 'C'}">
      <div class="jc_detail_panel_header" style="width: 100%">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td>
              <span class="jc_input_label">General Details</span>
            </td>
          </tr>
        </table>
      </div>
      <div class="jc_detail_panel" style="width: 100%">
	  <table width="100%" cellspacing="0" cellpadding="5" id="tableDetails">
	  <tr>
	    <td>Field name</td>
	    <td>Position</td>
	    <td>Value</td>
	  </tr>
	  <c:if test="${ieProfileMaintForm.mode != 'C'}">
	  <c:forEach var="ieProfileGeneralDetail" items="${ieProfileMaintForm.ieProfileGeneralDetails}">
	  <tr>
	    <td>
	    <html:hidden indexed="true" name="ieProfileGeneralDetail" property="ieProfileDetailId"/>
	    <html:hidden indexed="true" name="ieProfileGeneralDetail" property="ieProfileGroupName"/>
	    <html:hidden indexed="true" name="ieProfileGeneralDetail" property="ieProfileGroupIndex"/>
	    <html:hidden indexed="true" name="ieProfileGeneralDetail" property="ieProfileFieldName"/>
	    ${ieProfileGeneralDetail.ieProfileFieldName}
	    </td>
	    <td>
	      <html:text indexed="true" name="ieProfileGeneralDetail" property="ieProfilePosition" size="3"/>
	      <span class="jc_input_error">${ieProfileGeneralDetail.ieProfilePositionError}</span>
	    </td>
	    <td><html:textarea indexed="true" name="ieProfileGeneralDetail" property="ieProfileFieldValue" rows="1" cols="60" style="overflow:hidden;" onfocus="editValue(this);"/></td>
	  </tr>
	  </c:forEach>
	  </c:if>
	  </table>
	  </div>
	  
	  <br></br>
	  <div class="jc_detail_panel_header" style="width: 100%">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td>
              <span class="jc_input_label">Category Details</span>
            </td>
            <td>
              <div id="categoryContainer" align="right"></div>
            </td>
          </tr>
        </table>
      </div>
      <div class="jc_detail_panel" style="width: 100%">
	  <table width="100%" cellspacing="0" cellpadding="5" id="tableDetails">
	  <tr>
	    <td width="50"></td>
	    <td width="50%">Field name</td>
	    <td width="100">Position</td>
	    <td width="50%">Value</td>
	  </tr>
	  <c:if test="${ieProfileMaintForm.mode != 'C'}">
	  <c:forEach var="ieProfileCategoryDetail" items="${ieProfileMaintForm.ieProfileCategoryDetails}">
	  <tr>
	    <td>
	      <c:if test="${ieProfileCategoryDetail.ieProfileFieldName == 'catId'}">
	      <html:checkbox indexed="true" name="ieProfileCategoryDetail" property="selected"/>
	      </c:if>
	    </td>
	    <td>
	    <html:hidden indexed="true" name="ieProfileCategoryDetail" property="ieProfileDetailId"/>
	    <html:hidden indexed="true" name="ieProfileCategoryDetail" property="ieProfileGroupName"/>
	    <html:hidden indexed="true" name="ieProfileCategoryDetail" property="ieProfileGroupIndex"/>
	    <html:hidden indexed="true" name="ieProfileCategoryDetail" property="ieProfileFieldName"/>
	    ${ieProfileCategoryDetail.ieProfileFieldName}
	    </td>
	    <td>
	      <html:text indexed="true" name="ieProfileCategoryDetail" property="ieProfilePosition" size="3"/>
	      <span class="jc_input_error">${ieProfileCategoryDetail.ieProfilePositionError}</span>
	    </td>
	    <td><html:textarea indexed="true" name="ieProfileCategoryDetail" property="ieProfileFieldValue" rows="1" cols="60" style="overflow:hidden;" onfocus="editValue(this);"/></td>
	  </tr>
	  </c:forEach>
	  </c:if>
	  </table>
	  </div>
	  
	  <br></br>
	  <div class="jc_detail_panel_header" style="width: 100%">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td>
              <span class="jc_input_label">Items Related Details</span>
            </td>
            <td>
              <div id="itemRelatedContainer" align="right"></div>
            </td>
          </tr>
        </table>
      </div>
      <div class="jc_detail_panel" style="width: 100%">
	  <table width="100%" cellspacing="0" cellpadding="5" id="tableDetails">
	  <tr>
	    <td width="50"></td>
	    <td width="50%">Field name</td>
	    <td width="100">Position</td>
	    <td width="50%">Value</td>
	  </tr>
	  <c:if test="${ieProfileMaintForm.mode != 'C'}">
	  <c:forEach var="ieProfileItemRelatedDetail" items="${ieProfileMaintForm.ieProfileItemRelatedDetails}">
	  <tr>
	    <td>
	      <c:if test="${ieProfileItemRelatedDetail.ieProfileFieldName == 'itemId'}">
	      <html:checkbox indexed="true" name="ieProfileItemRelatedDetail" property="selected"/>
	      </c:if>
	    </td>
	    <td>
	    <html:hidden indexed="true" name="ieProfileItemRelatedDetail" property="ieProfileDetailId"/>
	    <html:hidden indexed="true" name="ieProfileItemRelatedDetail" property="ieProfileGroupName"/>
	    <html:hidden indexed="true" name="ieProfileItemRelatedDetail" property="ieProfileGroupIndex"/>
	    <html:hidden indexed="true" name="ieProfileItemRelatedDetail" property="ieProfileFieldName"/>
	    ${ieProfileItemRelatedDetail.ieProfileFieldName}
	    </td>
	    <td>
	      <html:text indexed="true" name="ieProfileItemRelatedDetail" property="ieProfilePosition" size="3"/>
	      <span class="jc_input_error">${ieProfileItemRelatedDetail.ieProfilePositionError}</span>
	    </td>
	    <td><html:textarea indexed="true" name="ieProfileItemRelatedDetail" property="ieProfileFieldValue" rows="1" cols="60" style="overflow:hidden;" onfocus="editValue(this);"/></td>
	  </tr>
	  </c:forEach>
	  </c:if>
	  </table>
	  </div>
	  
	  <br></br>
	  <div class="jc_detail_panel_header" style="width: 100%">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td>
              <span class="jc_input_label">Items CrossSell Details</span>
            </td>
            <td>
              <div id="itemCrossSellContainer" align="right"></div>
            </td>
          </tr>
        </table>
      </div>
      <div class="jc_detail_panel" style="width: 100%">
	  <table width="100%" cellspacing="0" cellpadding="5" id="tableDetails">
	  <tr>
	    <td width="50"></td>
	    <td width="50%">Field name</td>
	    <td width="100">Position</td>
	    <td width="50%">Value</td>
	  </tr>
	  <c:if test="${ieProfileMaintForm.mode != 'C'}">
	  <c:forEach var="ieProfileItemCrossSellDetail" items="${ieProfileMaintForm.ieProfileItemCrossSellDetails}">
	  <tr>
	    <td>
	      <c:if test="${ieProfileItemCrossSellDetail.ieProfileFieldName == 'itemId'}">
	      <html:checkbox indexed="true" name="ieProfileItemCrossSellDetail" property="selected"/>
	      </c:if>
	    </td>
	    <td>
	    <html:hidden indexed="true" name="ieProfileItemCrossSellDetail" property="ieProfileDetailId"/>
	    <html:hidden indexed="true" name="ieProfileItemCrossSellDetail" property="ieProfileGroupName"/>
	    <html:hidden indexed="true" name="ieProfileItemCrossSellDetail" property="ieProfileGroupIndex"/>
	    <html:hidden indexed="true" name="ieProfileItemCrossSellDetail" property="ieProfileFieldName"/>
	    ${ieProfileItemCrossSellDetail.ieProfileFieldName}
	    </td>
	    <td>
	      <html:text indexed="true" name="ieProfileItemCrossSellDetail" property="ieProfilePosition" size="3"/>
	      <span class="jc_input_error">${ieProfileItemCrossSellDetail.ieProfilePositionError}</span>
	    </td>
	    <td><html:textarea indexed="true" name="ieProfileItemCrossSellDetail" property="ieProfileFieldValue" rows="1" cols="60" style="overflow:hidden;" onfocus="editValue(this);"/></td>
	  </tr>
	  </c:forEach>
	  </c:if>
	  </table>
	  </div>
	  
	  <br></br>
	  <div class="jc_detail_panel_header" style="width: 100%">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td>
              <span class="jc_input_label">Items UpSell Details</span>
            </td>
            <td>
              <div id="itemUpSellContainer" align="right"></div>
            </td>
          </tr>
        </table>
      </div>
      <div class="jc_detail_panel" style="width: 100%">
	  <table width="100%" cellspacing="0" cellpadding="5" id="tableDetails">
	  <tr>
	    <td width="50"></td>
	    <td width="50%">Field name</td>
	    <td width="100">Position</td>
	    <td width="50%">Value</td>
	  </tr>
	  <c:if test="${ieProfileMaintForm.mode != 'C'}">
	  <c:forEach var="ieProfileItemUpSellDetail" items="${ieProfileMaintForm.ieProfileItemUpSellDetails}">
	  <tr>
	    <td>
	      <c:if test="${ieProfileItemUpSellDetail.ieProfileFieldName == 'itemId'}">
	      <html:checkbox indexed="true" name="ieProfileItemUpSellDetail" property="selected"/>
	      </c:if>
	    </td>
	    <td>
	    <html:hidden indexed="true" name="ieProfileItemUpSellDetail" property="ieProfileDetailId"/>
	    <html:hidden indexed="true" name="ieProfileItemUpSellDetail" property="ieProfileGroupName"/>
	    <html:hidden indexed="true" name="ieProfileItemUpSellDetail" property="ieProfileGroupIndex"/>
	    <html:hidden indexed="true" name="ieProfileItemUpSellDetail" property="ieProfileFieldName"/>
	    ${ieProfileItemUpSellDetail.ieProfileFieldName}
	    </td>
	    <td>
	      <html:text indexed="true" name="ieProfileItemUpSellDetail" property="ieProfilePosition" size="3"/>
	      <span class="jc_input_error">${ieProfileItemUpSellDetail.ieProfilePositionError}</span>
	    </td>
	    <td><html:textarea indexed="true" name="ieProfileItemUpSellDetail" property="ieProfileFieldValue" rows="1" cols="60" style="overflow:hidden;" onfocus="editValue(this);"/></td>
	  </tr>
	  </c:forEach>
	  </c:if>
	  </table>
	  </div>
	  
	  <br></br>
	  <div class="jc_detail_panel_header" style="width: 100%">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td>
              <span class="jc_input_label">Item Tier Price Details</span>
            </td>
            <td>
              <div id="itemTierPriceContainer" align="right"></div>
            </td>
          </tr>
        </table>
      </div>
	  <div class="jc_detail_panel" style="width: 100%">
	  <table width="100%" cellspacing="0" cellpadding="5" id="tableDetails">
	  <tr>
	    <td width="50"></td>
	    <td width="50%">Field name</td>
	    <td width="100">Position</td>
	    <td width="50%">Value</td>
	  </tr>
	  <c:if test="${ieProfileMaintForm.mode != 'C'}">
	  <c:forEach var="ieProfileItemTierPriceDetail" items="${ieProfileMaintForm.ieProfileItemTierPriceDetails}">
	  <tr>
	    <td>
	      <c:if test="${ieProfileItemTierPriceDetail.ieProfileFieldName == 'custClassId'}">
	      <html:checkbox indexed="true" name="ieProfileItemTierPriceDetail" property="selected"/>
	      </c:if>
	    </td>
	    <td>
	    <html:hidden indexed="true" name="ieProfileItemTierPriceDetail" property="ieProfileDetailId"/>
	    <html:hidden indexed="true" name="ieProfileItemTierPriceDetail" property="ieProfileGroupName"/>
	    <html:hidden indexed="true" name="ieProfileItemTierPriceDetail" property="ieProfileGroupIndex"/>
	    <html:hidden indexed="true" name="ieProfileItemTierPriceDetail" property="ieProfileFieldName"/>
	    ${ieProfileItemTierPriceDetail.ieProfileFieldName}
	    </td>
	    <td>
	      <html:text indexed="true" name="ieProfileItemTierPriceDetail" property="ieProfilePosition" size="3"/>
	      <span class="jc_input_error">${ieProfileItemTierPriceDetail.ieProfilePositionError}</span>
	    </td>
	    <td><html:textarea indexed="true" name="ieProfileItemTierPriceDetail" property="ieProfileFieldValue" rows="1" cols="60" style="overflow:hidden;" onfocus="editValue(this);"/></td>
	  </tr>
	  </c:forEach>
	  </c:if>
	  </table>
	  </div>
	  
	  <br></br>
	  <div class="jc_detail_panel_header" style="width: 100%">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td>
              <span class="jc_input_label">Item Attribute Details</span>
            </td>
            <td>
              <div id="itemAttributesContainer" align="right"></div>
            </td>
          </tr>
        </table>
      </div>
  	  <div class="jc_detail_panel" style="width: 100%">
	  <table width="100%" cellspacing="0" cellpadding="5" id="tableDetails">
	  <tr>
	    <td width="50"></td>
	    <td width="50%">Field name</td>
	    <td width="100">Position</td>
	    <td width="50%">Value</td>
	  </tr>
	  <c:if test="${ieProfileMaintForm.mode != 'C'}">
	  <c:forEach var="ieProfileItemAttributeDetail" items="${ieProfileMaintForm.ieProfileItemAttributeDetails}">
	  <tr>
	    <td>
	      <c:if test="${ieProfileItemAttributeDetail.ieProfileFieldName == 'customAttribId'}">
	      <html:checkbox indexed="true" name="ieProfileItemAttributeDetail" property="selected"/>
	      </c:if>
	    </td>
	    <td>
	    <html:hidden indexed="true" name="ieProfileItemAttributeDetail" property="ieProfileDetailId"/>
	    <html:hidden indexed="true" name="ieProfileItemAttributeDetail" property="ieProfileGroupName"/>
	    <html:hidden indexed="true" name="ieProfileItemAttributeDetail" property="ieProfileGroupIndex"/>
	    <html:hidden indexed="true" name="ieProfileItemAttributeDetail" property="ieProfileFieldName"/>
	    ${ieProfileItemAttributeDetail.ieProfileFieldName}
	    </td>
	    <td>
	      <html:text indexed="true" name="ieProfileItemAttributeDetail" property="ieProfilePosition" size="3"/>
	      <span class="jc_input_error">${ieProfileItemAttributeDetail.ieProfilePositionError}</span>
	    </td>
	    <td><html:textarea indexed="true" name="ieProfileItemAttributeDetail" property="ieProfileFieldValue" rows="1" cols="60" style="overflow:hidden;" onfocus="editValue(this);"/></td>
	  </tr>
	  </c:forEach>
	  </c:if>
	  </table>
	  </div>

	  <br></br>
	  <div class="jc_detail_panel_header" style="width: 100%">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td>
              <span class="jc_input_label">Item images</span>
            </td>
            <td>
              <div id="itemImageContainer" align="right"></div>
            </td>
          </tr>
        </table>
      </div>
  	  <div class="jc_detail_panel" style="width: 100%">
	  <table width="100%" cellspacing="0" cellpadding="5" id="tableDetails">
	  <tr>
	    <td width="50"></td>
	    <td width="50%">Field name</td>
	    <td width="100">Position</td>
	    <td width="50%">Value</td>
	  </tr>
	  <c:if test="${ieProfileMaintForm.mode != 'C'}">
	  <c:forEach var="ieProfileItemImageDetail" items="${ieProfileMaintForm.ieProfileItemImageDetails}">
	  <tr>
	    <td>
	      <c:if test="${ieProfileItemImageDetail.ieProfileFieldName == 'itemImageLocation'}">
	      <html:checkbox indexed="true" name="ieProfileItemImageDetail" property="selected"/>
	      </c:if>
	    </td>
	    <td>
	    <html:hidden indexed="true" name="ieProfileItemImageDetail" property="ieProfileDetailId"/>
	    <html:hidden indexed="true" name="ieProfileItemImageDetail" property="ieProfileGroupName"/>
	    <html:hidden indexed="true" name="ieProfileItemImageDetail" property="ieProfileGroupIndex"/>
	    <html:hidden indexed="true" name="ieProfileItemImageDetail" property="ieProfileFieldName"/>
	    ${ieProfileItemImageDetail.ieProfileFieldName}
	    </td>
	    <td>
	      <html:text indexed="true" name="ieProfileItemImageDetail" property="ieProfilePosition" size="3"/>
	      <span class="jc_input_error">${ieProfileItemImageDetail.ieProfilePositionError}</span>
	    </td>
	    <td><html:textarea indexed="true" name="ieProfileItemImageDetail" property="ieProfileFieldValue" rows="1" cols="60" style="overflow:hidden;" onfocus="editValue(this);"/></td>
	  </tr>
	  </c:forEach>
	  </c:if>
	  </table>
	  </div>
	  
	  <br></br>
	  <div class="jc_detail_panel_header" style="width: 100%">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td>
              <span class="jc_input_label">Others</span>
            </td>
            <td>
              <div id="othersContainer" align="right"></div>
            </td>
          </tr>
        </table>
      </div>
  	  <div class="jc_detail_panel" style="width: 100%">
	  <table width="100%" cellspacing="0" cellpadding="5" id="tableDetails">
	  <tr>
	    <td width="50"></td>
	    <td width="50%">Field name</td>
	    <td width="100">Position</td>
	    <td width="50%">Value</td>
	  </tr>
	  <c:if test="${ieProfileMaintForm.mode != 'C'}">
	  <c:forEach var="ieProfileOtherDetail" items="${ieProfileMaintForm.ieProfileOtherDetails}">
	  <tr>
	    <td>
	      <c:if test="${ieProfileOtherDetail.ieProfileFieldName == 'other'}">
	      <html:checkbox indexed="true" name="ieProfileOtherDetail" property="selected"/>
	      </c:if>
	    </td>
	    <td>
	    <html:hidden indexed="true" name="ieProfileOtherDetail" property="ieProfileDetailId"/>
	    <html:hidden indexed="true" name="ieProfileOtherDetail" property="ieProfileGroupName"/>
	    <html:hidden indexed="true" name="ieProfileOtherDetail" property="ieProfileGroupIndex"/>
	    <html:hidden indexed="true" name="ieProfileOtherDetail" property="ieProfileFieldName"/>
	    ${ieProfileOtherDetail.ieProfileFieldName}
	    </td>
	    <td>
	      <html:text indexed="true" name="ieProfileOtherDetail" property="ieProfilePosition" size="3"/>
	      <span class="jc_input_error">${ieProfileOtherDetail.ieProfilePositionError}</span>
	    </td>
	    <td><html:textarea indexed="true" name="ieProfileOtherDetail" property="ieProfileFieldValue" rows="1" cols="60" style="overflow:hidden;" onfocus="editValue(this);"/></td>
	  </tr>
	  </c:forEach>
	  </c:if>
	  </table>
	  </div>
	  
	</c:if>
    </td>
  </tr>
</table>

</html:form>

<div class=" yui-skin-sam">
<div>
<div id="jc_value_panel">
  <div class="hd">Edit value</div>
  <div class="bd">
	<table width="100%" cellspacing="0" cellpadding="5">
	  <tr>
	    <td><div id="jc_value_button_container"></div></td>
	  </tr>
	  <tr>
	    <td><textarea name="areaValue" id="areaValue" cols="45" rows="5"></textarea></td>
	  </tr>
	</table>
	
  </div>
</div>
</div>
</div>

