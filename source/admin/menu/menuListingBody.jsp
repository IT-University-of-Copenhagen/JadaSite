<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="menuListingForm" scope="request" value="${menuListingForm}"/>

<script language="JavaScript">
function submitForm(methodType) {
    document.forms[0].action = '/${adminBean.contextPath}/admin/menu/menuListing.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}
</script>
<html:form action="/admin/menu/menuListing.do" method="post">
<html:hidden property="process" value="list"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - Menu Listing</td>
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
                      <html:submit property="submitButton" value="Search" styleClass="jc_submit_button" onclick="return submitForm('list');"/>
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
            <td class="jc_input_label">Sub-site Name</td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srSiteDomainName" mode="E,E,E" styleClass="jc_input_object"/> 
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
              <layout:radio layout="false" property="srActive" mode="E,E,E" value="N"/>Non-Active
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
      <c:if test="${menuListingForm.siteDomains != null}">
        <table class="jc_panel_table_header" width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td>Menu Listing Result</td>
            <td> 
              <div align="right">
              </div>
            </td>
          </tr>
		</table>
	    <br>
	    <br>
        <table width="100%" border="0" cellspacing="0" cellpadding="3">
           <tr> 
            <td class="jc_list_table_header">Sub-site</td>
            <td class="jc_list_table_header">Sub-site Name</td>
            <td class="jc_list_table_header"><div align="center">Active</div></td>
          </tr>
          <c:forEach var="siteDomain" items="${menuListingForm.siteDomains}">
          <tr class="jc_list_table_row"> 
            <td width="40%" class="jc_list_table_content">
              <html:link page="/admin/menu/menuMaint.do?process=start" paramId="siteDomainId" paramName="siteDomain" paramProperty="siteDomainId">
                ${siteDomain.siteName}
              </html:link>
            </td>
            <td width="40%" class="jc_list_table_content">
				${siteDomain.siteDomainName}
            </td>
            <td width="60" class="jc_list_table_content" nowrap>
              <div align="center">
                ${siteDomain.active}
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
