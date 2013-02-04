<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="pollMaintForm" scope="request" value="${pollMaintForm}"/>

<script language="JavaScript">
function submitForm(type) {
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}
function submitBackForm(type) {
    document.forms[0].action = "/<c:out value='${adminBean.contextPath}'/>/admin/poll/pollListing.do";
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}

function handlePollPublishOn(type, args, obj) { 
    document.forms[0].pollPublishOn.value = jc_calendar_callback(type, args, obj);
}
function handlePollExpireOn(type, args, obj) { 
    document.forms[0].pollExpireOn.value = jc_calendar_callback(type, args, obj);
}

YAHOO.namespace("example.calendar");
function init() {
  YAHOO.example.calendar.pollPublishOn = new YAHOO.widget.Calendar("pollPublishOn", "pollPublishOnContainer", { title:"Choose a date:", close:true } );
  YAHOO.example.calendar.pollPublishOn.render();
  YAHOO.example.calendar.pollPublishOn.hide();
  YAHOO.util.Event.addListener("pollPublishOn_img", "click", YAHOO.example.calendar.pollPublishOn.show, YAHOO.example.calendar.pollPublishOn, true);
  YAHOO.example.calendar.pollPublishOn.selectEvent.subscribe(handlePollPublishOn, YAHOO.example.calendar.pollPublishOn, true); 
  
  YAHOO.example.calendar.pollExpireOn = new YAHOO.widget.Calendar("pollExpireOn", "pollExpireOnContainer", { title:"Choose a date:", close:true } );
  YAHOO.example.calendar.pollExpireOn.render();
  YAHOO.example.calendar.pollExpireOn.hide();
  YAHOO.util.Event.addListener("pollExpireOn_img", "click", YAHOO.example.calendar.pollExpireOn.show, YAHOO.example.calendar.pollExpireOn, true);
  YAHOO.example.calendar.pollExpireOn.selectEvent.subscribe(handlePollExpireOn, YAHOO.example.calendar.pollExpireOn, true); 
}
YAHOO.util.Event.addListener(window, "load", init);
</script>
<html:form method="post" action="/admin/poll/pollMaint">
<html:hidden property="mode"/> 
<html:hidden property="process" value=""/>
<html:hidden property="pollHeaderId"/> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - <a href="/<c:out value='${adminBean.contextPath}'/>/admin/poll/pollListing.do?process=back">Poll 
      Listing</a> - Poll Maintenance</td>
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
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td width="100%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="5px" class="borderTable">
              <tr> 
                <td class="jc_input_label">Poll Topic</td>
                <td class="jc_input_control"> <layout:text layout="false" property="pollTopic" size="120" mode="E,E,E" styleClass="tableContent"/> 
                  <span class="jc_input_error"> <logic:messagesPresent property="pollTopic" message="true"> 
                  <html:messages property="pollTopic" id="errorText" message="true"> 
                  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
                  </span> </td>
              </tr>
              <tr> 
                <td class="jc_input_label">Publish On</td>
                <td class="jc_input_control">
                  <layout:text layout="false" property="pollPublishOn" mode="E,E,E" styleClass="jc_input_control"/> 
                  <img id="pollPublishOn_img" src="../images/icon/image_plus.gif" border="0">
                  <div id="pollPublishOnContainer" style="position: absolute;"></div>
                  <span class="jc_input_error"> <logic:messagesPresent property="pollPublishOn" message="true"> 
                  <html:messages property="pollPublishOn" id="errorText" message="true"> 
                  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
                  </span>
                </td>
              </tr>
              <tr> 
                <td class="jc_input_label">Expire On</td>
                <td class="jc_input_control">
                  <layout:text layout="false" property="pollExpireOn" mode="E,E,E" styleClass="jc_input_control"/> 
                  <img id="pollExpireOn_img" src="../images/icon/image_plus.gif" border="0">
                  <div id="pollExpireOnContainer" style="position: absolute;"></div> 
                  <span class="jc_input_error"> <logic:messagesPresent property="pollExpireOn" message="true"> 
                  <html:messages property="pollExpireOn" id="errorText" message="true"> 
                  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
                  </span>
                </td>
              </tr>
              <tr> 
                <td class="jc_input_label">Published</td>
                <td class="jc_input_control"> <layout:checkbox layout="false" property="published" mode="E,E,E" value="Y"/> 
                </td>
              </tr>
              <tr> 
                <td> </td>
                <td> </td>
              </tr>
            </table>
			<br>
            <c:if test="${pollMaintForm.mode == 'U'}"> 
            <div class="hr">
              <hr/>
            </div>
            <br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td valign="top">
                  <table border="0" cellspacing="0" cellpadding="0">
                    <tr> 
                      <td class="jc_panel_table_sub_header" width="100%">Options</td>
                    </tr>
                  </table>
                  <br>
                  <table width="100%" border="0" cellspacing="0" cellpadding="3">
                    <tr>
                      <td valign="top" width="400">
                        <div class="jc_shaded_box" style="width: 400px">
                        <table width="100%" border="0" cellspacing="0" cellpadding="2">
                          <tr>
                            <td>
                            Enter new response - <html:link href="javascript:submitForm('addPollDetail')" styleClass="jc_submit_link">Add</html:link>
                            </td>
                          </tr>
                          <tr>
                            <td>
                            <layout:text layout="false" size="50" property="newPollOption" mode="E,E,E" styleClass="jc_input_control"/>
                            </td>
                          </tr>
                          <tr>
                            <td>
                            <span class="jc_input_error"> <logic:messagesPresent property="newPollOption" message="true"> 
                            <html:messages property="newPollOption" id="errorText" message="true"> 
                            <bean:write name="errorText"/> </html:messages> </logic:messagesPresent> 
                            </span>
                            </td>
                          </tr>
                        </table>
                        </div>
                      </td>
                      <td valign="top" width="100%">
                        <div class="jc_shaded_box">
                        <table border="0" cellspacing="0" cellpadding="2">
                          <tr>
                            <td width="60"><html:link href="javascript:submitForm('removePollDetails')" styleClass="jc_submit_link">Remove</html:link></td>
                            <td width="60"><html:link href="javascript:submitForm('resequence')" styleClass="jc_submit_link">Resequence</html:link></td>
                            <td>Response</td>
                            <td nowrap width="70">Vote Count</td>
                            <td width="50">Percentage</td>
                          </tr>
                          <c:forEach var="pollDetail" items="${pollMaintForm.pollDetails}">
                          <tr>
                            <td width="50" align="center">
                              <html:checkbox indexed="true" name='pollDetail' property="remove"/>
                              <html:hidden indexed="true" name="pollDetail" property="pollVoteCount"/> 
                              <html:hidden indexed="true" name="pollDetail" property="pollDetailId"/> 
                              <html:hidden indexed="true" name="pollDetail" property="pollPercentage"/> 
                            </td>
                            <td width="50" align="center">
                              <html:text size="5" indexed="true" name='pollDetail' property="seqNum"/>
                              <span class="jc_input_error"><c:out value='${pollDetail.seqNumError}'/></span>
                            </td>
                            <td width="100%">
                              <html:text size="50" indexed="true" name='pollDetail' property="pollOption"/>
                              <span class="jc_input_error"><c:out value='${pollDetail.pollOptionError}'/></span>
                            </td>
                            <td width="50" align="center"><c:out value='${pollDetail.pollVoteCount}'/></td>
                            <td width="50" align="center"><c:out value='${pollDetail.pollPercentage}'/></td>
                          </tr>
                          </c:forEach>
                          <tr>
                            <td width="50" align="center">
                              &nbsp;
                            </td>
                            <td width="50" align="center">
                              &nbsp;
                            </td>
                            <td width="100%">
                              &nbsp;
                            </td>
                            <td width="50" align="center">&nbsp;</td>
                            <td width="50" align="center">&nbsp;</td>
                          </tr>
                        </table>
                        </div>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
            </table>
            </c:if>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>

</html:form> 
