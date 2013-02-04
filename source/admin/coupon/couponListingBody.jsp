<jsp:directive.page import="com.jada.ui.dropdown.DropDownMenuGenerator"/>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="couponListingForm" scope="session" value="${couponListingForm}"/>
<jsp:useBean id="couponListingForm"  type="com.jada.admin.coupon.CouponListingActionForm"  scope="session" />
<script language="JavaScript">
function submitNewForm(page) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/coupon/couponMaint.do';
    document.forms[0].process.value = "create";
    document.forms[0].submit();
    return false;
}

function submitRemoveForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/coupon/couponListing.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function submitMaintForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/coupon/couponMaint.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function handleCouponPublishDate(type, args, obj) { 
    document.forms[0].srCouponPublishDate.value = jc_calendar_callback(type, args, obj);
} 

YAHOO.namespace("example.calendar");
function init() {
  YAHOO.example.calendar.calCouponPublishDate = new YAHOO.widget.Calendar("calCouponPublishDate", "calCouponPublishDateContainer", { title:"Choose a date:", close:true } );
  YAHOO.example.calendar.calCouponPublishDate.render();
  YAHOO.example.calendar.calCouponPublishDate.hide();
  YAHOO.util.Event.addListener("calCouponPublishDateSwitch", "click", YAHOO.example.calendar.calCouponPublishDate.show, YAHOO.example.calendar.calCouponPublishDate, true);
  YAHOO.example.calendar.calCouponPublishDate.selectEvent.subscribe(handleCouponPublishDate, YAHOO.example.calendar.calCouponPublishDate, true); 
}
YAHOO.util.Event.addListener(window, "load", init);
</script>
<html:form action="/admin/coupon/couponListing.do" method="post">
<html:hidden property="process" value="search"/>
<html:hidden property="srPageNo" value=""/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - Coupon Listing</td>
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
                      <html:submit property="submitButton" value="Search" styleClass="jc_submit_button"/>
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
            <td class="jc_input_label">Code</td>
          </tr>
          <tr> 
            <td class="jc_input_control"> <layout:text layout="false" property="srCouponCode" mode="E,E,E" styleClass="jc_input_control"/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Name</td>
          </tr>
          <tr> 
            <td class="jc_input_control"> <layout:text layout="false" property="srCouponName" mode="E,E,E" styleClass="jc_input_control"/> 
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
            <td class="jc_input_label">Active on </td>
          </tr>
          <tr> 
            <td class="jc_input_control" nowrap> <layout:text layout="false" property="srCouponPublishDate" mode="E,E,E" styleClass="jc_input_control"/> 
            <img id="calCouponPublishDateSwitch" src="../images/icon/image_plus.gif" border="0">
            <div id="calCouponPublishDateContainer" style="position: absolute; display: none;"></div> 
            </td>
          </tr>
          <tr> 
            <td class="jc_input_label">&nbsp; </td>
          </tr>
        </table>
      </div>
    </td>
    <td width="100%" valign="top" class="jc_list_panel_open"> 
      <c:if test="${couponListingForm.coupons != null}">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td>Coupon Listing 
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
            <c:if test="${couponListingForm.pageNo > 1}">
            <td>
              <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/coupon/couponListing.do?process=list&srPageNo=<c:out value="${couponListingForm.pageNo - 1}"/>">&lt;</a>
            </td>
            </c:if>
            <td>
              <c:forEach begin="${couponListingForm.startPage}" end="${couponListingForm.endPage}" var="index">
                <c:choose>
                  <c:when test="${index == couponListingForm.pageNo}">
                    <span class="jc_navigation_line"><c:out value="${index}"/></span>
                  </c:when>
                  <c:otherwise>
                    <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/coupon/couponListing.do?process=list&srPageNo=<c:out value='${index}'/>"><c:out value="${index}"/></a>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
              &nbsp;
            </td>
            <c:if test="${couponListingForm.pageNo < couponListingForm.pageCount}">
            <td>
              <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/coupon/couponListing.do?process=list&srPageNo=<c:out value="${couponListingForm.pageNo + 1}"/>">&gt;</a>
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
            <td class="jc_list_table_header">Code</td>
            <td class="jc_list_table_header">Name</td>
            <td class="jc_list_table_header"><div align="center">Publish</div></td>
            <td class="jc_list_table_header">Starts On</td>
            <td class="jc_list_table_header">Ends On</td>
          </tr>
          <c:forEach var="coupon" items="${couponListingForm.coupons}">
          <tr> 
            <td width="50" class="jc_list_table_coupon">
              <div align="center">
                <html:hidden name="coupon" property="couponId" indexed="true"/>
                <html:hidden name="coupon" property="couponCode" indexed="true"/>
                <html:hidden name="coupon" property="couponName" indexed="true"/>
                <html:hidden name="coupon" property="published" indexed="true"/>
                <html:hidden name="coupon" property="couponStartDate" indexed="true"/>
                <html:hidden name="coupon" property="couponEndDate" indexed="true"/>
                <html:checkbox indexed="true" name="coupon" property="remove" value="Y" styleClass="tableContent"/>
              </div>
            </td>
            <td width="60%" class="jc_list_table_coupon">
              <html:link page="/admin/coupon/couponMaint.do?process=edit" paramId="couponId" paramName="coupon" paramProperty="couponId">
                <c:out value="${coupon.couponCode}"/>
              </html:link>
            </td>
            <td width="40%" class="jc_list_table_coupon" nowrap>
              <c:out value="${coupon.couponName}"/><br>
            </td>
            <td width="60" class="jc_list_table_coupon" nowrap><div align="center"><c:out value="${coupon.published}"/></div></td>
            <td width="110" class="jc_list_table_coupon" nowrap><c:out value="${coupon.couponStartDate}"/></td>
            <td width="110" class="jc_list_table_coupon" nowrap><c:out value="${coupon.couponEndDate}"/></td>
          </tr>
          </c:forEach>
        </table>
      </c:if>
    </td>
  </tr>
</table>
</html:form>
