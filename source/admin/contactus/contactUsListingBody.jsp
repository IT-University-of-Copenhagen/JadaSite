<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="contactUsListingForm" scope="session" value="${contactUsListingForm}"/>

<script language="JavaScript">
function submitNewForm(page) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/contactus/contactUsMaint.do';
    document.forms[0].process.value = "create";
    document.forms[0].submit();
    return false;
}

function submitRemoveForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/contactus/contactUsListing.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function submitListingForm(page) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/contactus/contactUsListing.do';
    document.forms[0].process.value = "search";
    document.forms[0].srPageNo.value = page;
    document.forms[0].submit();
    return false;
}

function submitForm(action) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/contactus/contactUsListing.do';
    document.forms[0].process.value = action;
    document.forms[0].submit();
    return false;
}

function submitMaintForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/contactus/contactUsMaint.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}
</script>
<html:form action="/admin/contactus/contactUsListing.do" method="post">
<html:hidden property="process" value="list"/>
<html:hidden property="srPageNo"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - Contact Us Listing</td>
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
                      <html:submit property="submitButton" value="Search" styleClass="jc_submit_button" onclick="return submitListingForm('');"/>
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
            <td class="jc_input_label">Contact Us Name</td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srContactUsName" mode="E,E,E" styleClass="jc_input_object"/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Active</td>
          </tr>
          <tr> 
            <td class="jc_input_object" valign="top">
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
      <c:if test="${contactUsListingForm.contactUsForms != null}">
        <table class="jc_panel_table_header" width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td>Contact Us Listing 
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
            <c:if test="${contactUsListingForm.pageNo > 1}">
            <td>
              <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/contactus/contactUsListing.do?process=list&srPageNo=<c:out value="${contactUsListingForm.pageNo - 1}"/>">&lt;</a>
            </td>
            </c:if>
            <td>
              <c:forEach begin="${contactUsListingForm.startPage}" end="${contactUsListingForm.endPage}" var="index">
                <c:choose>
                  <c:when test="${index == contactUsListingForm.pageNo}">
                    <span class="jc_navigation_line"><c:out value="${index}"/></span>
                  </c:when>
                  <c:otherwise>
                    <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/contactus/contactUsListing.do?process=list&srPageNo=<c:out value='${index}'/>"><c:out value="${index}"/></a>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
              &nbsp;
            </td>
            <c:if test="${contactUsListingForm.pageNo < contactUsListingForm.pageCount}">
            <td>
              <a class="jc_navigation_link" href="<c:out value='/${adminBean.contextPath}'/>/admin/contactus/contactUsListing.do?process=list&srPageNo=<c:out value="${contactUsListingForm.pageNo + 1}"/>">&gt;</a>
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
            <td class="jc_list_table_header">
              <div align="center">
                <html:link href="javascript:submitForm('resequence')" styleClass="jc_submit_link">Resequence</html:link>
              </div>
            </td>
            <td class="jc_list_table_header">Contact Us Name&nbsp;&nbsp;</td>
            <td class="jc_list_table_header"><div align="center">Active</div></td>
          </tr>
          <c:forEach var="contactUs" items="${contactUsListingForm.contactUsForms}">
          <tr class="jc_list_table_row"> 
            <td width="50" class="jc_list_table_content">
              <div align="center">
                <html:checkbox indexed="true" name="contactUs" property="remove"/>
                <html:hidden indexed="true" name="contactUs" property="contactUsId"/> 
              </div>
            </td>
            <td width="100" class="jc_list_table_content" nowrap> 
              <div align="center">
                <html:text indexed="true" name="contactUs" property="seqNum" size="2"/> 
              </div>
            </td>
            <td width="100%" class="jc_list_table_content" nowrap>
              <html:link page="/admin/contactus/contactUsMaint.do?process=edit" paramId="contactUsId" paramName="contactUs" paramProperty="contactUsId">
                <c:out value="${contactUs.contactUsName}"/>
              </html:link>
              <html:hidden indexed="true" name="contactUs" property="contactUsName"/> 
            </td>
            <td width="60" class="jc_list_table_content" nowrap>
              <div align="center">
                <html:hidden indexed="true" name="contactUs" property="active"/> 
                <c:out value="${contactUs.active}"/>
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
