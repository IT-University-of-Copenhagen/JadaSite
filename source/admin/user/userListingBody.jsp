<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="userListingForm" scope="session" value="${userListingForm}"/>

<script language="JavaScript">
function submitNewForm(page) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/user/userMaint.do';
    document.forms[0].process.value = "create";
    document.forms[0].submit();
    return false;
}

function submitRemoveForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/user/userListing.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function submitListingForm(page) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/user/userListing.do';
    document.forms[0].process.value = "list";
    document.forms[0].srPageNo.value = page;
    document.forms[0].submit();
    return false;
}

function submitForm(action) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/user/userListing.do';
    document.forms[0].process.value = action;
    document.forms[0].submit();
    return false;
}

function submitMaintForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/user/userMaint.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}
</script>
<html:form action="/admin/user/userListing.do" method="post">
<html:hidden property="process" value="search"/>
<html:hidden property="srPageNo"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - User Listing</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td width="0%"> 
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
            <td class="jc_input_label">User Id </td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srUserId" mode="E,E,E" styleClass="jc_input_object"/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">User Name</td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srUserName" mode="E,E,E" styleClass="jc_input_object"/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">User Type</td>
          </tr>
          <tr> 
            <td class="jc_input_object"> 
              <layout:radio layout="false" property="srUserType" mode="E,E,E" value="S"/><bean:message key="user.type.S" />
              <br>
              <layout:radio layout="false" property="srUserType" mode="E,E,E" value="A"/><bean:message key="user.type.A" />
              <br>
              <layout:radio layout="false" property="srUserType" mode="E,E,E" value="R"/><bean:message key="user.type.R" />
              <br>
              <layout:radio layout="false" property="srUserType" mode="E,E,E" value="*"/><bean:message key="user.type.*" />
              <br>
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Active</td>
          </tr>
          <tr> 
            <td class="jc_input_object">
              <layout:radio layout="false" property="srActive" mode="E,E,E" value="Y"/>Active 
              <br>
              <layout:radio layout="false" property="srActive" mode="E,E,E" value="N"/>In-active 
              <br>
              <layout:radio layout="false" property="srActive" mode="E,E,E" value="*"/>All 
              <br>
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
        </table>
      </div>
    </td>
    <td width="100%" valign="top" class="jc_list_panel_open"> 
      <c:if test="${userListingForm.users != null}">
        <table class="jc_panel_table_header" width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td>User Listing 
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
            <c:if test="${userListingForm.pageNo > 1}">
            <td>
              <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/user/userListing.do?process=list&srPageNo=<c:out value="${userListingForm.pageNo - 1}"/>">&lt;</a>
            </td>
            </c:if>
            <td>
              <c:forEach begin="${userListingForm.startPage}" end="${userListingForm.endPage}" var="index">
                <c:choose>
                  <c:when test="${index == userListingForm.pageNo}">
                    <span class="jc_navigation_line"><c:out value="${index}"/></span>
                  </c:when>
                  <c:otherwise>
                    <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/user/userListing.do?process=list&srPageNo=<c:out value='${index}'/>"><c:out value="${index}"/></a>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
              &nbsp;
            </td>
            <c:if test="${userListingForm.pageNo < userListingForm.pageCount}">
            <td>
              <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/user/userListing.do?process=list&srPageNo=<c:out value="${userListingForm.pageNo + 1}"/>">&gt;</a>
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
            <td class="jc_list_table_header">User Id</td>
            <td class="jc_list_table_header">User Name&nbsp;&nbsp;</td>
            <td class="jc_list_table_header"><div align="center">User Type</div></td>
            <td class="jc_list_table_header"><div align="center">Active</div></td>
          </tr>
          <c:forEach var="user" items="${userListingForm.users}">
          <tr class="jc_list_table_row"> 
            <td width="50" class="jc_list_table_content">
              <div align="center">
                <html:checkbox indexed="true" name="user" property="remove"/>
                <html:hidden indexed="true" name="user" property="userId"/> 
              </div>
            </td>
            <td width="40%" class="jc_list_table_content">
              <html:link page="/admin/user/userMaint.do?process=edit" paramId="userId" paramName="user" paramProperty="userId">
                <c:out value="${user.userId}"/>
              </html:link>
            </td>
            <td width="60%" class="jc_list_table_content" nowrap>
              <html:hidden indexed="true" name="user" property="userName"/> 
              <c:out value="${user.userName}"/><br>
            </td>
            <td width="60" class="jc_list_table_content" nowrap><div align="center"><c:out value="${user.userType}"/></div></td>
            <td width="60" class="jc_list_table_content" nowrap>
              <div align="center">
                <html:hidden indexed="true" name="user" property="active"/> 
                <c:out value="${user.active}"/>
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
