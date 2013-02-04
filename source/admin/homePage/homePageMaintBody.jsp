<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>

<script language="JavaScript">
var siteProfileClassDefault = ${homePageMaintForm.siteProfileClassDefault};
var siteProfileClassId = "${homePageMaintForm.siteProfileClassId}";
var translationEnable = ${homePageMaintForm.translationEnable};
var homePageId = "${homePageMaintForm.homePageId}";

function submitForm(methodType) {
    if (!siteProfileClassDefault) {
        document.homePageMaintForm.pageTitleLang.value = document.homePageMaintForm.pageTitleLang_tmp.value;
        document.homePageMaintForm.metaKeywordsLang.value = document.homePageMaintForm.metaKeywordsLang_tmp.value;
        document.homePageMaintForm.metaDescriptionLang.value = document.homePageMaintForm.metaDescriptionLang_tmp.value;
	}
    document.forms[0].action = '/${adminBean.contextPath}/admin/homePage/homePageMaint.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function submitTranslateForm() {
	submitForm('translate');
}

YAHOO.util.Event.onContentReady('butTranslate', function() {
	var butTranslate = new YAHOO.widget.Button("butTranslate", { disabled: true });
	if (translationEnable) {
		butTranslate.set('disabled', false);
	}
	butTranslate.on("click", submitTranslateForm);
} );

function addHomePageDetail(index, homePageDetailId, seqNum, type, homePageDesc, feature) {
	var table = document.getElementById('homePageTable');
	var row = table.insertRow(-1);
	var cell = null;

	var prefix = 'homePageDetail[' + index + '].';
	
	cell = row.insertCell(-1);
	
	var input = document.createElement('input');
	input.setAttribute('type', 'hidden');
	input.setAttribute('name', prefix + 'homePageDetailId');
	input.setAttribute('value', homePageDetailId);
	cell.appendChild(input);
	input = document.createElement('input');
	input.setAttribute('type', 'hidden');
	input.setAttribute('name', prefix + 'description');
	input.setAttribute('value', homePageDesc);
	cell.appendChild(input);
	input = document.createElement('input');
	input.setAttribute('type', 'hidden');
	input.setAttribute('name', prefix + 'dataType');
	input.setAttribute('value', type);
	cell.appendChild(input);

	input = document.createElement('input');
	input.setAttribute('type', 'checkbox');
	input.setAttribute('name', 'remove');
	input.setAttribute('value', homePageId);
	input.className = 'jc_input_control';
	cell.appendChild(input);
	
	cell = row.insertCell(-1);
	cell.appendChild(document.createTextNode(type));
	
	cell = row.insertCell(-1);
	
	input = document.createElement('input');
	input.setAttribute('type', 'text');
	input.setAttribute('name', prefix +  'seqNum');
	input.setAttribute('maxlength', '5');
	input.setAttribute('size', '5');
	input.setAttribute('value', seqNum);
	input.className = 'jc_input_control';
	cell.appendChild(input);

	cell = row.insertCell(-1);

	container = document.createElement('div');
	container.setAttribute('align', 'center');
	input = document.createElement('input');
	input.type = 'radio';
	input.setAttribute('name', 'featureHomePageDetailId');
	input.setAttribute('value', homePageDetailId);
	input.className = 'jc_input_control';
	container.appendChild(input);
	cell.appendChild(container);
	if (feature) {
		input.defaultChecked = true;
	}
	
	cell = row.insertCell(-1);
	cell.appendChild(document.createTextNode(homePageDesc));
}

var showHomePageDetails_callback = {
 	success: function(o) {
	    if (!isJsonResponseValid(o.responseText)) {
			jc_panel_show_error("Unexcepted Error: Unable to show home page details");
			return false;
	    }
	    var jsonObject = getJsonObject(o.responseText);
		var table = document.getElementById('homePageTable');
		while (table.rows.length > 1) {
	 		table.deleteRow(1);
		}

		for (var i = 0; i < jsonObject.homePageDetails.length; i++) {
	 		var homePageDetail = jsonObject.homePageDetails[i];
	 		addHomePageDetail(i, 
	 				homePageDetail.homePageDetailId,
	 				homePageDetail.seqNum,
	 				homePageDetail.type,
	 				homePageDetail.homePageDesc,
	 				homePageDetail.feature);
		}	 		
	},
	failure: function(o) {
	   jc_panel_show_error("Unexcepted Error: Unable to show attribute options");
	}
};

function paintHomePageTable() {
	var url = "/${adminBean.contextPath}/admin/homePage/homePageMaint.do";
	var postData = "process=getHomePageDetails&homePageId=" + homePageId;
	var request = YAHOO.util.Connect.asyncRequest('POST', url, showHomePageDetails_callback, postData);
}


var addContent_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to add content");
      return false;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    paintHomePageTable();
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to add content");
  }
};

function jc_content_search_client_callback(value) {
	jc_content_search_panel.hide();
   
	var jsonObject = eval('(' + value + ')');
   	for (var i = 0; i < jsonObject.contents.length; i++) {
		var content = jsonObject.contents[i];
		var url = "/${adminBean.contextPath}/admin/homePage/homePageMaint.do";
		var data = "process=addContent&homePageId=" + homePageId + "&contentId=" + content.contentId;
		var request = YAHOO.util.Connect.asyncRequest('POST', url, addContent_callback, data);
   	}
	return false;
}

function addContent() {
	jc_content_search_show(jc_content_search_client_callback);
}

var addItem_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to add item");
      return false;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    paintHomePageTable();
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to add item");
  }
};

function jc_item_search_client_callback(value) {
	jc_item_search_panel.hide();
   
	var jsonObject = eval('(' + value + ')');
   	for (var i = 0; i < jsonObject.items.length; i++) {
		var item = jsonObject.items[i];
		var url = "/${adminBean.contextPath}/admin/homePage/homePageMaint.do";
		var data = "process=addItem&homePageId=" + homePageId + "&itemId=" + item.itemId;
		var request = YAHOO.util.Connect.asyncRequest('POST', url, addItem_callback, data);
   	}
	return false;
}

function addItem() {
	jc_item_search_show(jc_item_search_client_callback);
}

var removeDetails_callback =
{
  success: function(o) {
    if (!isJsonResponseValid(o.responseText)) {
      jc_panel_show_error("Unexcepted Error: unable to remove details");
      return false;
    }
    var jsonObject = eval('(' + o.responseText + ')');
    paintHomePageTable();
  },
  failure: function(o) {
    jc_panel_show_error("Unexcepted Error: unable to remove details");
  }
};

function removeDetails() {
	var removes = jc_getElementsByName('remove');
	var url = "/${adminBean.contextPath}/admin/homePage/homePageMaint.do";
	var postData = "process=removeDetails&homePageId=" + homePageId;
	for (var i = 0; i < removes.length; i++) {
		if (removes[i].checked) {
			var homePageDetailId = YAHOO.util.Dom.getFirstChild(removes[i].parentNode).value;
			postData += '&homePageDetailIds=' + homePageDetailId;
		}
	}
	var request = YAHOO.util.Connect.asyncRequest('POST', url, removeDetails_callback, postData);
}

YAHOO.util.Event.onContentReady('butHomePageContainer', function() {
	var buttonMenu = [
			{ text: "Add item to home page", value: 'Add item to home page', onclick: { fn: addItem } }, 
			{ text: "Add content to home page", value: 'Add content to home page', onclick: { fn: addContent } },
			{ text: "Remove details from home page", value: 'Remove details from home page', onclick: { fn: removeDetails } }
	];
	var butContainer = document.getElementById('butHomePageContainer');
	var menu = new YAHOO.widget.Button({ 
                          type: "menu", 
                          disabled: true,
                          label: "Options", 
                          name: "menu",
                          menu: buttonMenu, 
                          container: butContainer });
    if (siteProfileClassDefault) {
    	  menu.set('disabled', false);
    }
} );

</script>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="homePageMaintForm" scope="session" value="${homePageMaintForm}"/>
<html:form action="/admin/homePage/homePageMaint.do" method="post">
<html:hidden property="process" value="new"/>
<html:hidden property="siteDomainId"/>
<html:hidden property="homePageId"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>
    Administration - <a href="/${adminBean.contextPath}/admin/homePage/homePageListing.do?process=back">Home Page 
      Listing</a> - Home Page Maintenance</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="3">
  <tr> 
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
                <td><html:submit property="submitButton" value="Save" styleClass="jc_submit_button" onclick="return submitForm('save')"/>&nbsp;</td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <br><br>
      <table width="50%" border="0" cellspacing="0" cellpadding="5">
        <tr> 
          <td class="jc_input_label">
          HTML title text for home page
          <lang:checkboxSwitch name="homePageMaintForm" property="pageTitleLangFlag"> - Override language</lang:checkboxSwitch>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_control">
          <lang:text property="pageTitle" size="80" maxlength="255" styleClass="tableContent"/> 
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">
          Meta keywords
          <lang:checkboxSwitch name="homePageMaintForm" property="metaKeywordsLangFlag"> - Override language</lang:checkboxSwitch>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_control">
          <lang:textarea property="metaKeywords" cols="80" rows="2" styleClass="tableContent"/> 
          </td>
        </tr>
        <tr> 
          <td class="jc_input_label">
          Meta description
          <lang:checkboxSwitch name="homePageMaintForm" property="metaDescriptionLangFlag"> - Override language</lang:checkboxSwitch>
          </td>
        </tr>
        <tr> 
          <td class="jc_input_control">
          <lang:textarea property="metaDescription" cols="80" rows="3" styleClass="tableContent"/> 
          </td>
        </tr>
      </table>
    </td>
    <td valign="top">
    <div class="jc_detail_panel_header">
	  <table width="400" border="0" cellspacing="0" cellpadding="0">
	    <tr>
	      <td><span class="jc_input_label">General</span></td>
	      <td>
	        <div align="right" id="butHomePageContainer">
	        </div>
	      </td>
	    </tr>
	  </table>
	</div>
	<div class="jc_detail_panel">
	  <table width="400" border="0" cellspacing="0" cellpadding="0">
	    <tr>
	      <td>
	        <div id="butHomePageContainer">
	        <table id="homePageTable" width="100%" border="0" cellspacing="0" cellpadding="3">
	          <tr>
	            <td width="15%" class="jc_input_label">
	            Remove
	            </td>
	            <td width="15%" class="jc_input_label">
	            Type
	            </td>
	            <td width="15%" class="jc_input_label">
	            Sequence
	            </td>
	            <td width="15%" class="jc_input_label">
	            <div align="center">Feature</div>
	            </td>
	            <td width="40%" class="jc_input_label">
	            Description
	            </td>
	          </tr>
	          <c:forEach var="homePageDetail" items="${homePageMaintForm.homePageDetails}">
	          <tr>
	            <td class="jc_input_label">
	              <html:hidden indexed="true" name="homePageDetail" property="homePageDetailId"/>
	              <html:hidden indexed="true" name="homePageDetail" property="dataType"/>
	              <html:hidden indexed="true" name="homePageDetail" property="description"/>
	              <html:hidden indexed="true" name="homePageDetail" property="featureData"/>
	              <lang:checkbox name="homePageDetail" value="" property="remove" styleClass="jc_input_control"/>
	            </td>
	            <td class="jc_input_object">
	              ${homePageDetail.dataType}
	            </td>
	            <td class="jc_input_object">
	              <lang:text indexed="true" name="homePageDetail" property="seqNum" size="5" maxlength="5" styleClass="jc_input_control"/>
		          <span class="jc_input_error">
	                <c:if test="${homePageDetail.seqNumError != null}">
	                  <br>
	                  ${homePageDetail.seqNumError}
	                </c:if>
	              </span>
	            </td>
	            <td class="jc_input_object">
	              <div align="center">
	              <lang:radio name="homePageMaintForm" property="featureHomePageDetailId" value="${homePageDetail.homePageDetailId}"/>
	              </div>
	            </td>
	            <td class="jc_input_object">
	              ${homePageDetail.description}
	            </td>
	          </tr>
	          </c:forEach>
	        </table>
	        </div>
	      </td>
	    </tr>
	  </table>
	</div>
    </td>
  </tr>
</table>
</html:form>
<%@ include file="/admin/include/panel.jsp" %>
<!------------------------------------------------------------------------>
<%@ include file="/admin/include/itemLookup.jsp" %>
<%@ include file="/admin/include/contentLookup.jsp" %>
<%@ include file="/admin/include/confirm.jsp" %>
<!------------------------------------------------------------------------>