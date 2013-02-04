<jsp:directive.page import="com.jada.ui.dropdown.DropDownMenuGenerator"/>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="paymentGatewayListingForm" scope="session" value="${paymentGatewayListingForm}"/>
<jsp:useBean id="paymentGatewayListingForm"  type="com.jada.admin.paymentGateway.PaymentGatewayListingActionForm"  scope="session" />
<script language="JavaScript">
function submitNewForm(page) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/paymentGateway/paymentGatewayMaint.do';
    document.forms[0].process.value = "create";
    document.forms[0].submit();
    return false;
}

function submitRemoveForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/paymentGateway/paymentGatewayListing.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function submitMaintForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/paymentGateway/paymentGatewayMaint.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

</script>
<html:form action="/admin/paymentGateway/paymentGatewayListing.do" method="post">
<html:hidden property="process" value="search"/>
<html:hidden property="srPageNo" value=""/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Store - Payment Gateway Listing</td>
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
            <td class="jc_input_label">Name</td>
          </tr>
          <tr> 
            <td class="jc_input_control"> <layout:text layout="false" property="srPaymentGatewayName" mode="E,E,E" styleClass="jc_input_control"/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
        </table>
      </div>
    </td>
    <td width="100%" valign="top" class="jc_list_panel_open"> 
      <c:if test="${paymentGatewayListingForm.paymentGateways != null}">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td>Payment Gateway Listing 
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
            <c:if test="${paymentGatewayListingForm.pageNo > 1}">
            <td>
              <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/paymentGateway/paymentGatewayListing.do?process=list&srPageNo=<c:out value="${paymentGatewayListingForm.pageNo - 1}"/>">&lt;</a>
            </td>
            </c:if>
            <td>
              <c:forEach begin="${paymentGatewayListingForm.startPage}" end="${paymentGatewayListingForm.endPage}" var="index">
                <c:choose>
                  <c:when test="${index == paymentGatewayListingForm.pageNo}">
                    <span class="jc_navigation_line"><c:out value="${index}"/></span>
                  </c:when>
                  <c:otherwise>
                    <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/paymentGateway/paymentGatewayListing.do?process=list&srPageNo=<c:out value='${index}'/>"><c:out value="${index}"/></a>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
              &nbsp;
            </td>
            <c:if test="${paymentGatewayListingForm.pageNo < paymentGatewayListingForm.pageCount}">
            <td>
              <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/paymentGateway/paymentGatewayListing.do?process=list&srPageNo=<c:out value="${paymentGatewayListingForm.pageNo + 1}"/>">&gt;</a>
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
            <td class="jc_list_table_header">Name</td>
          </tr>
          <c:forEach var="paymentGateway" items="${paymentGatewayListingForm.paymentGateways}">
          <tr> 
            <td width="50" class="jc_list_table_paymentGateway">
              <div align="center">
                <html:hidden name="paymentGateway" property="paymentGatewayId" indexed="true"/>
                <html:hidden name="paymentGateway" property="paymentGatewayName" indexed="true"/>
                <html:checkbox indexed="true" name="paymentGateway" property="remove" styleClass="tableContent"/>
              </div>
            </td>
            <td width="100%" class="jc_list_table_paymentGateway">
              <html:link page="/admin/paymentGateway/paymentGatewayMaint.do?process=edit" paramId="paymentGatewayId" paramName="paymentGateway" paramProperty="paymentGatewayId">
                <c:out value="${paymentGateway.paymentGatewayName}"/>
              </html:link>
            </td>
          </tr>
          </c:forEach>
        </table>
      </c:if>
    </td>
  </tr>
</table>
</html:form>
