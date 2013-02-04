<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="countryMaintForm" scope="request" value="${countryMaintForm}"/>

<script language="JavaScript">
function submitForm(type) {
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}
function submitBackForm(type) {
    document.forms[0].action = "/<c:out value='${adminBean.contextPath}'/>/admin/country/countryListing.do";
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}
</script>
<html:form method="post" action="/admin/country/countryMaint">
<html:hidden property="mode"/> 
<html:hidden property="process" value=""/>
<html:hidden property="countryId"/> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - <a href="/<c:out value='${adminBean.contextPath}'/>/admin/country/countryListing.do?process=back">Country 
      Listing</a> - Country Maintenance</td>
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
                <td><html:submit property="submitButton" value="Save" styleClass="jc_submit_button" onclick="return submitForm('save')"/>&nbsp;</td>
                <layout:mode value="edit"> 
                <td><html:submit property="submitButton" value="Remove" styleClass="jc_submit_button" onclick="return submitForm('remove')"/>&nbsp;</td>
                </layout:mode> 
                <td><html:submit property="submitButton" value="Cancel" styleClass="jc_submit_button" onclick="return submitBackForm('back')"/>&nbsp;</td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <br>
      <span class="jc_input_error">
      <logic:messagesPresent property="error" message="true"> 
      	<html:messages property="error" id="errorText" message="true"> 
        <bean:write name="errorText"/><br>
        </html:messages> 
      </logic:messagesPresent>
      </span>
      <table width="705" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td width="300" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="5px" class="borderTable">
              <tr> 
                <td class="jc_input_label">Country Code</td>
              </tr>
              <tr>
                <td class="jc_input_control"> <layout:text layout="false" property="countryCode" size="2" maxlength="2" mode="E,E,E" styleClass="tableContent"/> 
                  <span class="jc_input_error"> <logic:messagesPresent property="countryCode" message="true"> 
                  <html:messages property="countryCode" id="errorText" message="true"> 
                  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
                  </span> </td>
              </tr>
              <tr> 
                <td class="jc_input_label">Country Name</td>
              </tr>
              <tr>
                <td class="jc_input_control"> <layout:text layout="false" property="countryName" size="40" maxlength="40" mode="E,E,E" styleClass="tableContent"/> 
                  <span class="jc_input_error"> <logic:messagesPresent property="countryName" message="true"> 
                  <html:messages property="countryName" id="errorText" message="true"> 
                  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
                  </span> </td>
              </tr>
            </table>
          </td>
          <layout:mode value="edit">
          <td width="5px" height="100%">
            <div style="border-left: 1px solid #dcdcdc; height: 100%"></div>
          </td>
          <td width="400" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0px" class="borderTable">
              <tr> 
                <td class="jc_input_label" colspan="3">
                  <a href="javascript:void(0);" onclick="addState();" class="jc_navigation_link">Add State</a>
                </td>
              </tr>
              <tr> 
                <td class="jc_input_label" colspan="3">&nbsp;</td>
              </tr>
              <tr> 
                <td class="jc_input_label">
                  <div align="center">
                  <html:link href="javascript:submitForm('removeState')" styleClass="jc_submit_link">Remove</html:link>
                  </div>
                </td>
                <td class="jc_input_label"><div align="center">State Code</div></td>
                <td class="jc_input_label">Name</td>
              </tr>
              <c:forEach var="state" items="${countryMaintForm.states}"> 
              <tr> 
                <td class="jc_input_control">
                  <div align="center">
                    <html:hidden indexed="true" name="state" property="stateId"/>
                    <html:hidden indexed="true" name="state" property="stateCode"/>
                    <html:hidden indexed="true" name="state" property="stateName"/>
                    <html:checkbox indexed="true" name="state" property="remove" value="Y"/>
                  </div>
                </td>
                <td class="jc_input_control">
                  <div align="center">
                  <a href='javascript:modifyState(<c:out value="${state.stateId}"/>)'><c:out value="${state.stateCode}"/></a>
                  </div>
                </td>
                <td class="jc_input_control"><c:out value="${state.stateName}"/></td>
              </tr>
              </c:forEach>
            </table>
          </td>
          </layout:mode>
        </tr>
      </table>
    </td>
  </tr>
</table>
</html:form> 

<!------------------------------------------------------------------------>

<script>
var jc_state_update_panel;

function addState() {
  var message = document.getElementById("jc_state_message");
  message.innerHTML = "";
  jc_state_update_panel.show();
}

<!-- -->

function modifyState(stateId) {
  var message = document.getElementById("jc_state_message");
  message.innerHTML = "";
  jc_state_update_panel.show();
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/country/countryMaint.do?process=getState";
  var data = "stateId=" + stateId;
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_getState_callback, data);
}

var getStateSuccessHandler = function(o) {
  if (o.responseText == undefined) {
    return;
  }
  var jsonObject = eval('(' + o.responseText + ')');
  document.stateForm.stateId.value = jsonObject.stateId;
  document.stateForm.stateCode.value = jsonObject.stateCode;
  document.stateForm.stateName.value = jsonObject.stateName;
}

var failureHandler = function(o) {
  var message = document.getElementById("jc_state_message");
  message.innerHTML = "Error updating state information";
}

var jc_getState_callback =
{
  success: getStateSuccessHandler,
  failure: failureHandler
}

<!-- -->

function updateState() {
  var url = "<c:out value='/${adminBean.contextPath}'/>/admin/country/countryMaint.do?process=updateState";
  var data = "stateId=" + document.stateForm.stateId.value;
  data += "&" + "countryId=" + encodeURI(document.countryMaintForm.countryId.value);
  data += "&" + "stateCode=" + encodeURI(document.stateForm.stateCode.value);
  data += "&" + "stateName=" + encodeURI(document.stateForm.stateName.value);
  var request = YAHOO.util.Connect.asyncRequest('POST', url, jc_updateState_callback, data);
}

var updateStateSuccessHandler = function(o) {
  if (o.responseText == undefined) {
    return;
  }
  var jsonObject = eval('(' + o.responseText + ')');
  var message = document.getElementById("jc_state_message");
  if (jsonObject.status == 'success') {
    window.location = "<c:out value='/${adminBean.contextPath}'/>/admin/country/countryMaint.do?process=edit&countryId=" + "<c:out value='${countryMaintForm.countryId}'/>";
  }
  var msgText = "";
  for (i = 0; i < jsonObject.messages.length; i++) {
    msgText += jsonObject.messages[i].message + '<br>';
  }
  message.innerHTML = msgText;
}

var jc_updateState_callback =
{
  success: updateStateSuccessHandler,
  failure: failureHandler
}

</script>

<!------------------------------------------------------------------------>

<script>
function jc_state_init() {
  jc_state_update_panel = new YAHOO.widget.Panel("jc_state_update_panel_container", 
                              { 
                                width: "300px", 
                                visible: false, 
                                constraintoviewport: true ,
                                fixedcenter : true,
                                modal: true
                              } 
                               );
  jc_state_update_panel.render();
}
YAHOO.util.Event.onDOMReady(jc_state_init);
</script>
<div class="yui-skin-sam">
<div>
<div id="jc_state_update_panel_container">
  <div class="hd">State maintenance</div>
  <div class="bd"> 
    <div id="jc_state_message" class="jc_input_error"></div>
    <form name="stateForm" method="post" action="">
    <table border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>State Code</td>
      </tr>
      <tr>
        <td>
        <input type="hidden" name="stateId">
        <input type="text" name="stateCode">
        </td>
      </tr>
      <tr>
        <td>State Name</td>
      </tr>
      <tr>
        <td><input type="text" name="stateName"></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td><a href="javascript:updateState()" class="jc_navigation_link">save</a></td>
      </tr>
    </table>
    </form>
  </div>
</div>
</div>
</div>

<!------------------------------------------------------------------------>


