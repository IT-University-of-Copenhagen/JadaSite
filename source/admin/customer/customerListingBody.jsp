<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="customerListingForm" scope="session" value="${customerListingForm}"/>
<jsp:useBean id="customerListingForm"  type="com.jada.admin.customer.CustomerListingActionForm"  scope="session" />

<script language="JavaScript">
function submitNewForm(page) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/customer/customerMaint.do';
    document.forms[0].process.value = "create";
    document.forms[0].submit();
    return false;
}

function submitRemoveForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/customer/customerListing.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function submitMaintForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/customer/customerMaint.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}
</script>
<html:form action="/admin/customer/customerListing.do" method="post">
<html:hidden property="process" value="search"/>
<html:hidden property="srPageNo" value=""/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - Customer Listing</td>
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
            <td class="jc_input_label">Email address </td>
          </tr>
          <tr> 
            <td class="jc_input_control"> <layout:text layout="false" size="30" property="srCustEmail" mode="E,E,E" styleClass="jc_input_control"/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">First Name</td>
          </tr>
          <tr> 
            <td class="jc_input_control"> <layout:text layout="false" size="20" property="srCustFirstName" mode="E,E,E" styleClass="jc_input_control"/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Last Name</td>
          </tr>
          <tr> 
            <td class="jc_input_control"> <layout:text layout="false" size="20" property="srCustLastName" mode="E,E,E" styleClass="jc_input_control"/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Sub-sites</td>
          </tr>
          <tr> 
            <td class="jc_input_control">
              <c:forEach var="subSite" items="${customerListingForm.subSites}">
                <html:checkbox indexed="true" name="subSite" property="select" value="Y"/>
                <html:hidden name="subSite" property="siteDomainId" indexed="true"/>
                <html:hidden name="subSite" property="siteName" indexed="true"/>
                <c:out value="${subSite.siteName}"/><br>
              </c:forEach>
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label"> <layout:radio layout="false" property="srActive" mode="E,E,E" value="Y"/>Active 
              <br>
              <layout:radio layout="false" property="srActive" mode="E,E,E" value="N"/>InActive 
              <br>
              <layout:radio layout="false" property="srActive" mode="E,E,E" value="*"/>All 
              <br>
            </td>
          </tr>
          <tr> 
            <td class="jc_input_label">&nbsp; </td>
          </tr>
        </table>
      </div>
    </td>
    <td width="100%" valign="top" class="jc_list_panel_open"> 
      <c:if test="${customerListingForm.customers != null}">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td>Customer Listing Result</td>
            <td> 
              <div align="right">
                <html:submit property="submitButton" value="Remove" styleClass="jc_submit_button" onclick="return submitRemoveForm('remove');"/>
              </div>
            </td>
          </tr>
		</table>
		<div align="right">
        <br>
        <table width="0%" border="0" cellspacing="0" cellpadding="0" class="jc_input_label">
          <tr>
            <c:if test="${customerListingForm.pageNo > 1}">
            <td>
              <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/customer/customerListing.do?process=list&srPageNo=<c:out value="${customerListingForm.pageNo - 1}"/>">&lt;</a>
            </td>
            </c:if>
            <td>
              <c:forEach begin="${customerListingForm.startPage}" end="${customerListingForm.endPage}" var="index">
                <c:choose>
                  <c:when test="${index == customerListingForm.pageNo}">
                    <span class="jc_navigation_line"><c:out value="${index}"/></span>
                  </c:when>
                  <c:otherwise>
                    <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/customer/customerListing.do?process=list&srPageNo=<c:out value='${index}'/>"><c:out value="${index}"/></a>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
              &nbsp;
            </td>
            <c:if test="${customerListingForm.pageNo < customerListingForm.pageCount}">
            <td>
              <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/customer/customerListing.do?process=list&srPageNo=<c:out value="${customerListingForm.pageNo + 1}"/>">&gt;</a>
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
            <td class="jc_list_table_header">Email</td>
            <td class="jc_list_table_header">First Name</td>
            <td class="jc_list_table_header">Last Name</td>
            <td class="jc_list_table_header">Sub-site</td>
            <td class="jc_list_table_header"><div align="center">Active</div></td>
          </tr>
          <c:forEach var="customer" items="${customerListingForm.customers}">
          <tr> 
            <td width="50" class="jc_list_table_customer">
              <div align="center">
                <html:hidden name="customer" property="custId" indexed="true"/>
                <html:hidden name="customer" property="siteName" indexed="true"/>
                <html:hidden name="customer" property="custEmail" indexed="true"/>
                <html:hidden name="customer" property="custFirstName" indexed="true"/>
                <html:hidden name="customer" property="custLastName" indexed="true"/>
                <html:hidden name="customer" property="active" indexed="true"/>
                <html:checkbox indexed="true" name="customer" property="remove" value="Y"/>
              </div>
            </td>
            <td width="30%" class="jc_list_table_content">
              <html:link page="/admin/customer/customerMaint.do?process=edit" paramId="custId" paramName="customer" paramProperty="custId">
                <c:out value="${customer.custEmail}"/>
              </html:link>
            </td>
            <td width="30%" class="jc_list_table_content" nowrap>
              <c:out value="${customer.custFirstName}"/><br>
            </td>
            <td width="30%" class="jc_list_table_content" nowrap>
              <c:out value="${customer.custLastName}"/><br>
            </td>
            <td width="20%" class="jc_list_table_content" nowrap>
              <c:out value="${customer.siteName}"/><br>
            </td>
            <td width="100" class="jc_list_table_content" nowrap>
              <div align="center">
              <c:out value="${customer.active}"/><br>
              </div>
            </td>
          </tr>
          </c:forEach>
        </table>
      </c:if>
    </td>
  </tr>
</table>
</html:form>
