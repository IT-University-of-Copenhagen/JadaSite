<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="orderListingForm" scope="session" value="${orderListingForm}"/>

<script language="JavaScript">
function submitNewForm(page) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/inventory/orderMaint.do';
    document.forms[0].process.value = "create";
    document.forms[0].submit();
    return false;
}

function submitRemoveForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/inventory/orderListing.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function submitSearchForm(page) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/inventory/orderListing.do';
    document.forms[0].process.value = "search";
    document.forms[0].srPageNo.value = page;
    document.forms[0].submit();
    return false;
}

function submitMaintForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/inventory/orderMaint.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function handleSrOrderCreatedOnStart(type, args, obj) { 
    document.forms[0].srOrderCreatedOnStart.value = jc_calendar_callback(type, args, obj);
}

function handleSrOrderCreatedOnEnd(type, args, obj) { 
    document.forms[0].srOrderCreatedOnEnd.value = jc_calendar_callback(type, args, obj);
} 

YAHOO.namespace("example.calendar");
function init() {
  YAHOO.example.calendar.calSrOrderCreatedOnStart = new YAHOO.widget.Calendar("calSrOrderCreatedOnStart", "calSrOrderCreatedOnStartContainer", { title:"Choose a date:", close:true } );
  YAHOO.example.calendar.calSrOrderCreatedOnStart.render();
  YAHOO.example.calendar.calSrOrderCreatedOnStart.hide();
  YAHOO.util.Event.addListener("calSrOrderCreatedOnStartSwitch", "click", YAHOO.example.calendar.calSrOrderCreatedOnStart.show, YAHOO.example.calendar.calSrOrderCreatedOnStart, true);
  YAHOO.example.calendar.calSrOrderCreatedOnStart.selectEvent.subscribe(handleSrOrderCreatedOnStart, YAHOO.example.calendar.calSrOrderCreatedOnStart, true); 

  YAHOO.example.calendar.calSrOrderCreatedOnEnd = new YAHOO.widget.Calendar("calSrOrderCreatedOnEnd", "calSrOrderCreatedOnEndContainer", { title:"Choose a date:", close:true } );
  YAHOO.example.calendar.calSrOrderCreatedOnEnd.render();
  YAHOO.example.calendar.calSrOrderCreatedOnEnd.hide();
  YAHOO.util.Event.addListener("calSrOrderCreatedOnEndSwitch", "click", YAHOO.example.calendar.calSrOrderCreatedOnEnd.show, YAHOO.example.calendar.calSrOrderCreatedOnEnd, true);
  YAHOO.example.calendar.calSrOrderCreatedOnEnd.selectEvent.subscribe(handleSrOrderCreatedOnEnd, YAHOO.example.calendar.calSrOrderCreatedOnEnd, true); 
}
YAHOO.util.Event.addListener(window, "load", init);

</script>
<html:form action="/admin/inventory/orderListing.do" method="post">
<html:hidden property="process" value="list"/>
<html:hidden property="srPageNo"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Order Listing</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="0%" valign="top"> 
      <div class="jc_search_panel_open"> 
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td> 
              <table class="jc_panel_table_header" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td>Search</td>
                  <td> 
                    <div align="right"> 
                      <html:submit property="submitButton" value="Search" styleClass="jc_submit_button" onclick="return submitSearchForm('');"/>
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
            <td class="jc_input_label">Order Num </td>
          </tr>
          <tr> 
            <td class=""> <layout:text layout="false" size="20" property="srOrderNum" mode="E,E,E" styleClass=""/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Order Created Between </td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srOrderCreatedOnStart" mode="E,E,E" styleClass="jc_input_object"/> 
            <img id="calSrOrderCreatedOnStartSwitch" src="../images/icon/image_plus.gif" border="0">
            <div id="calSrOrderCreatedOnStartContainer" style="position: absolute; display:none;"></div> 
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control" nowrap>
            <span class="jc_input_error"> 
              <logic:messagesPresent property="srOrderCreatedOnStart" message="true"> 
                <html:messages property="srOrderCreatedOnStart" id="errorText" message="true"> 
                  <bean:write name="errorText"/>
                </html:messages> 
              </logic:messagesPresent> 
              </span>
            </td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srOrderCreatedOnEnd" mode="E,E,E" styleClass="jc_input_object"/> 
            <img id="calSrOrderCreatedOnEndSwitch" src="../images/icon/image_plus.gif" border="0">
            <div id="calSrOrderCreatedOnEndContainer" style="position: absolute; display:none;"></div> 
            </td>
          </tr>
          <tr> 
            <td class="jc_input_control" nowrap>
            <span class="jc_input_error"> 
              <logic:messagesPresent property="srOrderCreatedOnEnd" message="true"> 
                <html:messages property="srOrderCreatedOnEnd" id="errorText" message="true"> 
                  <bean:write name="errorText"/>
                </html:messages> 
              </logic:messagesPresent> 
              </span>
            </td>
          </tr>
          <tr> 
            <td class="jc_input_label">&nbsp;</td>
          </tr>
          <tr> 
            <td class="jc_input_label">First Name</td>
          </tr>
          <tr> 
            <td class=""> <layout:text layout="false" property="srCustFirstName" mode="E,E,E" styleClass=""/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Last Name</td>
          </tr>
          <tr> 
            <td class=""> <layout:text layout="false" property="srCustLastName" mode="E,E,E" styleClass=""/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Email</td>
          </tr>
          <tr> 
            <td class=""> <layout:text layout="false" size="25" property="srCustEmail" mode="E,E,E" styleClass=""/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">City Name</td>
          </tr>
          <tr> 
            <td class=""> <layout:text layout="false" size="25" property="srCustCityName" mode="E,E,E" styleClass=""/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">State</td>
          </tr>
          <tr> 
            <td class="">
              <html:select property="srCustStateCode" styleClass="jc_input_control" style="width: 150px"> 
                <html:option value="All"/> 
                <html:optionsCollection property="states" label="label" value="value"/> 
              </html:select> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Country</td>
          </tr>
          <tr> 
            <td class="">
              <html:select property="srCustCountryCode" styleClass="jc_input_control" style="width: 150px"> 
                <html:option value="All"/> 
                <html:optionsCollection property="countries" label="label" value="value"/> 
              </html:select> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Order Abundant Location</td>
          </tr>
          <tr> 
            <td class="jc_input_control">
              <html:select property="orderAbundantLoc" styleClass="jc_input_control"> 
                <html:option value="All"/> 
                <html:optionsCollection property="orderAbundantLocs" label="label" value="value"/> 
              </html:select> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Order Status</td>
          </tr>
          <tr> 
            <td class="jc_input_control">
              <html:select property="srOrderStatus" styleClass="jc_input_control"> 
                <html:option value="All"/> 
                <html:optionsCollection property="orderStatuses" label="label" value="value"/> 
              </html:select> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
        </table>
      </div>
    </td>
    <td width="100%" valign="top" class="jc_list_panel_open"> 
      <c:if test="${orderListingForm.orders != null}">
        <table class="jc_panel_table_header" width="100%">
          <tr> 
            <td>Order Listing 
              Result</td>
            <td> 
            </td>
          </tr>
		</table>
		<div align="right">
        <br>
        <table width="0" border="0" cellspacing="0" cellpadding="1" class="jc_input_label">
          <tr>
            <c:if test="${orderListingForm.pageNo > 1}">
            <td>
              <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/inventory/orderListing.do?process=list&srPageNo=<c:out value="${orderListingForm.pageNo - 1}"/>">&lt;</a>
            </td>
            </c:if>
            <td nowrap>
              <c:forEach begin="${orderListingForm.startPage}" end="${orderListingForm.endPage}" var="index">
                <c:choose>
                  <c:when test="${index == orderListingForm.pageNo}">
                    <span class="jc_navigation_line"><c:out value="${index}"/></span>
                  </c:when>
                  <c:otherwise>
                    <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/inventory/orderListing.do?process=list&srPageNo=<c:out value='${index}'/>"><c:out value="${index}"/></a>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
            </td>
            <c:if test="${orderListingForm.pageNo < orderListingForm.pageCount}">
            <td>
              <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/inventory/orderListing.do?process=list&srPageNo=<c:out value="${orderListingForm.pageNo + 1}"/>">&gt;</a>
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
            <td class="jc_list_table_header" nowrap>Order Num</td>
            <td class="jc_list_table_header" nowrap>Date</td>
            <td class="jc_list_table_header" nowrap>First Name</td>
            <td class="jc_list_table_header" nowrap>Last Name</td>
            <td class="jc_list_table_header" nowrap>Email</td>
            <td class="jc_list_table_header" nowrap>City</td>
            <td class="jc_list_table_header" nowrap>State</td>
            <td class="jc_list_table_header" nowrap>Country</td>
            <td class="jc_list_table_header" nowrap><div align="right">Order Total</div></td>
            <td class="jc_list_table_header" nowrap>Status</td>
          </tr>
          <c:forEach var="order" items="${orderListingForm.orders}">
          <tr class="jc_list_table_row"> 
            <td width="50" class="jc_list_table_content">
              <div align="center">
                <input type="checkbox" name="orderHeaderIds" value="<c:out value="${order.orderHeaderId}"/>">
              </div>
            </td>
            <td width="200px" class="jc_list_table_content">
              <html:link page="/admin/inventory/orderMaint.do?process=edit" paramId="orderHeaderId" paramName="order" paramProperty="orderHeaderId">
                <c:out value="${order.orderNum}"/>
              </html:link>
            </td>
            <td width="20%" class="jc_list_table_content" nowrap>
              <c:out value="${order.orderDate}"/>
            </td>
            <td width="20%" class="jc_list_table_content" nowrap>
              <c:out value="${order.custFirstName}"/>
            </td>
            <td width="20%" class="jc_list_table_content" nowrap>
              <c:out value="${order.custLastName}"/>
            </td>
            <td width="40%" class="jc_list_table_content" nowrap>
              <c:out value="${order.custEmail}"/>
            </td>
            <td width="100px" class="jc_list_table_content" nowrap>
              <c:out value="${order.custCityName}"/>
            </td>
            <td width="50px" class="jc_list_table_content" nowrap>
              <c:out value="${order.custStateCode}"/>
            </td>
            <td width="50px" class="jc_list_table_content" nowrap>
              <c:out value="${order.custCountryCode}"/>
            </td>
            <td width="100px" class="jc_list_table_content" nowrap>
              <div align="right"><c:out value="${order.orderTotal}"/></div>
            </td>
            <td width="30px" class="jc_list_table_content" nowrap>
              <div align="center"><c:out value="${order.orderStatus}"/></div>
            </td>
          </tr>
          </c:forEach>
        </table>
      </c:if>
    </td>
  </tr>
</table>
</html:form>
