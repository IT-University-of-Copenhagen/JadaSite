<jsp:directive.page import="com.jada.ui.dropdown.DropDownMenuGenerator"/>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="pollListingForm" scope="session" value="${pollListingForm}"/>
<jsp:useBean id="pollListingForm"  type="com.jada.admin.poll.PollListingActionForm"  scope="session" />

<script language="JavaScript">
function submitNewForm(page) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/poll/pollMaint.do';
    document.forms[0].process.value = "create";
    document.forms[0].submit();
    return false;
}

function submitRemoveForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/poll/pollListing.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function submitListingForm(page) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/poll/pollListing.do';
    document.forms[0].process.value = "list";
    document.forms[0].srPageNo.value = page;
    document.forms[0].submit();
    return false;
}

function submitMaintForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/poll/pollMaint.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function handlePollPublishOnStart(type, args, obj) { 
    document.forms[0].srPollPublishOnStart.value = jc_calendar_callback(type, args, obj);
}

function handlePollPublishOnEnd(type, args, obj) { 
    document.forms[0].srPollPublishOnEnd.value = jc_calendar_callback(type, args, obj);
} 

function handlePollExpireOnStart(type, args, obj) { 
    document.forms[0].srPollExpireOnStart.value = jc_calendar_callback(type, args, obj);
}

function handlePollExpireOnEnd(type, args, obj) { 
    document.forms[0].srPollExpireOnEnd.value = jc_calendar_callback(type, args, obj);
} 

YAHOO.namespace("example.calendar");
function init() {
  YAHOO.example.calendar.calPollPublishOnStart = new YAHOO.widget.Calendar("calPollPublishOnStart", "calPollPublishOnStartContainer", { title:"Choose a date:", close:true } );
  YAHOO.example.calendar.calPollPublishOnStart.render();
  YAHOO.example.calendar.calPollPublishOnStart.hide();
  YAHOO.util.Event.addListener("calPollPublishOnStartSwitch", "click", YAHOO.example.calendar.calPollPublishOnStart.show, YAHOO.example.calendar.calPollPublishOnStart, true);
  YAHOO.example.calendar.calPollPublishOnStart.selectEvent.subscribe(handlePollPublishOnStart, YAHOO.example.calendar.calPollPublishOnStart, true); 

  YAHOO.example.calendar.calPollPublishOnEnd = new YAHOO.widget.Calendar("calPollPublishOnEnd", "calPollPublishOnEndContainer", { title:"Choose a date:", close:true } );
  YAHOO.example.calendar.calPollPublishOnEnd.render();
  YAHOO.example.calendar.calPollPublishOnEnd.hide();
  YAHOO.util.Event.addListener("calPollPublishOnEndSwitch", "click", YAHOO.example.calendar.calPollPublishOnEnd.show, YAHOO.example.calendar.calPollPublishOnEnd, true);
  YAHOO.example.calendar.calPollPublishOnEnd.selectEvent.subscribe(handlePollPublishOnEnd, YAHOO.example.calendar.calPollPublishOnEnd, true); 

  YAHOO.example.calendar.calPollExpireOnStart = new YAHOO.widget.Calendar("calPollExpireOnStart", "calPollExpireOnStartContainer", { title:"Choose a date:", close:true } );
  YAHOO.example.calendar.calPollExpireOnStart.render();
  YAHOO.example.calendar.calPollExpireOnStart.hide();
  YAHOO.util.Event.addListener("calPollExpireOnStartSwitch", "click", YAHOO.example.calendar.calPollExpireOnStart.show, YAHOO.example.calendar.calPollExpireOnStart, true);
  YAHOO.example.calendar.calPollExpireOnStart.selectEvent.subscribe(handlePollExpireOnStart, YAHOO.example.calendar.calPollExpireOnStart, true); 

  YAHOO.example.calendar.calPollExpireOnEnd = new YAHOO.widget.Calendar("calPollExpireOnEnd", "calPollExpireOnEndContainer", { title:"Choose a date:", close:true } );
  YAHOO.example.calendar.calPollExpireOnEnd.render();
  YAHOO.example.calendar.calPollExpireOnEnd.hide();
  YAHOO.util.Event.addListener("calPollExpireOnEndSwitch", "click", YAHOO.example.calendar.calPollExpireOnEnd.show, YAHOO.example.calendar.calPollExpireOnEnd, true);
  YAHOO.example.calendar.calPollExpireOnEnd.selectEvent.subscribe(handlePollExpireOnEnd, YAHOO.example.calendar.calPollExpireOnEnd, true); 
}
YAHOO.util.Event.addListener(window, "load", init);
</script>
<html:form action="/admin/poll/pollListing.do" method="post">
<html:hidden property="process" value="search"/>
<html:hidden property="srPageNo" value=""/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - Poll Listing</td>
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
                      <input type="submit" name="submitButton" value="Search" class="jc_submit_button">
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
            <td class="jc_input_label">Topic </td>
          </tr>
          <tr> 
            <td class="jc_input_control"> <layout:text layout="false" property="srPollTopic" mode="E,E,E" styleClass="jc_input_control"/> 
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
            <td class="jc_input_control" nowrap> <layout:text layout="false" property="srPollPublishOnStart" mode="E,E,E" styleClass="jc_input_control"/> 
            <img id="calPollPublishOnStartSwitch" src="../images/icon/image_plus.gif" border="0">
            <div id="calPollPublishOnStartContainer" style="position: absolute;"></div> 
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control" nowrap>
            <span class="jc_input_error"> 
              <logic:messagesPresent property="srPollPublishOnStart" message="true"> 
                <html:messages property="srPollPublishOnStart" id="errorText" message="true"> 
                  <bean:write name="errorText"/>
                </html:messages> 
              </logic:messagesPresent> 
              </span>
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control"> <layout:text layout="false" property="srPollPublishOnEnd" mode="E,E,E" styleClass="jc_input_control"/> 
            <img id="calPollPublishOnEndSwitch" src="../images/icon/image_plus.gif" border="0">
            <div id="calPollPublishOnEndContainer" style="position: absolute;"></div> 
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control" nowrap>
            <span class="jc_input_error"> 
              <logic:messagesPresent property="srPollPublishOnEnd" message="true"> 
                <html:messages property="srPollPublishOnEnd" id="errorText" message="true"> 
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
            <td class="jc_input_control"> <layout:text layout="false" property="srPollExpireOnStart" mode="E,E,E" styleClass="jc_input_control"/> 
            <img id="calPollExpireOnStartSwitch" src="../images/icon/image_plus.gif" border="0">
            <div id="calPollExpireOnStartContainer" style="position: absolute;"></div> 
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control" nowrap>
            <span class="jc_input_error"> 
              <logic:messagesPresent property="srPollExpireOnStart" message="true"> 
                <html:messages property="srPollExpireOnStart" id="errorText" message="true"> 
                  <bean:write name="errorText"/>
                </html:messages> 
              </logic:messagesPresent> 
              </span>
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control"> <layout:text layout="false" property="srPollExpireOnEnd" mode="E,E,E" styleClass="jc_input_control"/> 
            <img id="calPollExpireOnEndSwitch" src="../images/icon/image_plus.gif" border="0">
            <div id="calPollExpireOnEndContainer" style="position: absolute;"></div> 
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control" nowrap>
            <span class="jc_input_error"> 
              <logic:messagesPresent property="srPollExpireOnEnd" message="true"> 
                <html:messages property="srPollExpireOnEnd" id="errorText" message="true"> 
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
        </table>
      </div>
    </td>
    <td width="100%" valign="top" class="jc_list_panel_open"> 
      <c:if test="${pollListingForm.polls != null}">
        <table class="jc_panel_table_header" width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td>Poll Listing 
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
            <c:if test="${pollListingForm.pageNo > 1}">
            <td>
              <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/poll/pollListing.do?process=list&srPageNo=<c:out value="${pollListingForm.pageNo - 1}"/>">&lt;</a>
            </td>
            </c:if>
            <td>
              <c:forEach begin="${pollListingForm.startPage}" end="${pollListingForm.endPage}" var="index">
                <c:choose>
                  <c:when test="${index == pollListingForm.pageNo}">
                    <span class="jc_navigation_line"><c:out value="${index}"/></span>
                  </c:when>
                  <c:otherwise>
                    <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/poll/pollListing.do?process=list&srPageNo=<c:out value='${index}'/>"><c:out value="${index}"/></a>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
              &nbsp;
            </td>
            <c:if test="${pollListingForm.pageNo < pollListingForm.pageCount}">
            <td>
              <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/poll/pollListing.do?process=list&srPageNo=<c:out value="${pollListingForm.pageNo + 1}"/>">&gt;</a>
            </td>
            </c:if>
            <td>&nbsp;&nbsp;</td>
          </tr>
        </table>
        <br>
        </div>
        <table width="100%" border="0" cellspacing="0" cellpadding="1">
           <tr> 
            <td class="jc_list_table_header"></td>
            <td class="jc_list_table_header">Topic</td>
            <td class="jc_list_table_header"><div align="center">Publish</div></td>
            <td class="jc_list_table_header">Publish On</td>
            <td class="jc_list_table_header">Expire On</td>
          </tr>
          <c:forEach var="poll" items="${pollListingForm.polls}">
          <tr> 
            <td width="50" class="jc_list_table_poll">
              <div align="center">
                <input type="checkbox" name="pollHeaderIds" value="<c:out value="${poll.pollHeaderId}"/>">
              </div>
            </td>
            <td width="100%" class="jc_list_table_poll">
              <html:link page="/admin/poll/pollMaint.do?process=edit" paramId="pollHeaderId" paramName="poll" paramProperty="pollHeaderId">
                <c:out value="${poll.pollTopic}"/>
              </html:link>
            <td width="60" class="jc_list_table_poll" nowrap><div align="center"><c:out value="${poll.published}"/></div></td>
            <td width="120" class="jc_list_table_poll" nowrap><c:out value="${poll.pollPublishOn}"/></td>
            <td width="120" class="jc_list_table_poll" nowrap><c:out value="${poll.pollExpireOn}"/></td>
          </tr>
          </c:forEach>
        </table>
      </c:if>
    </td>
  </tr>
</table>
</html:form>
