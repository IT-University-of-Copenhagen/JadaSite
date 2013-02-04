<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="taxListingForm" scope="session" value="${taxListingForm}"/>

<script language="JavaScript">
function submitNewForm(page) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/tax/taxMaint.do';
    document.forms[0].process.value = "create";
    document.forms[0].submit();
    return false;
}

function submitRemoveForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/tax/taxListing.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function submitListingForm(page) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/tax/taxListing.do';
    document.forms[0].process.value = "list";
    document.forms[0].submit();
    return false;
}

function submitMaintForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/tax/taxMaint.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}
</script>
<html:form action="/admin/tax/taxListing.do" method="post">
<html:hidden property="process" value="list"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - Tax Rate Listing</td>
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
            <td class="jc_input_label">Tax Code</td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srTaxCode" mode="E,E,E" styleClass="jc_input_object"/> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr> 
            <td class="jc_input_label">Published</td>
          </tr>
          <tr> 
            <td class="jc_input_object">
              <layout:radio layout="false" property="srPublished" mode="E,E,E" value="Y"/>Published 
              <br>
              <layout:radio layout="false" property="srPublished" mode="E,E,E" value="N"/>Not-Published 
              <br>
              <layout:radio layout="false" property="srPublished" mode="E,E,E" value="*"/>All 
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
      <c:if test="${taxListingForm.taxes != null}">
        <table class="jc_panel_table_header" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td width="100%">Tax Rate Listing 
              Result</td>
            <td nowrap> 
              <div align="right">
                <html:submit property="submitButton" value="Remove" styleClass="jc_submit_button" onclick="return submitRemoveForm('remove');"/>
                <html:submit property="submitButton" value="New" styleClass="jc_submit_button" onclick="return submitMaintForm('create');"/>
              </div>
            </td>
          </tr>
		</table>
		<br>
        <table width="100%" border="0" cellspacing="0" cellpadding="1">
           <tr> 
            <td class="jc_list_table_header"></td>
            <td class="jc_list_table_header">Tax Code</td>
            <td class="jc_list_table_header">Tax Name</td>
            <td class="jc_list_table_header"><div align="right">Tax Rate</div></td>
            <td class="jc_list_table_header"><div align="center">Published</div></td>
          </tr>
          <c:forEach var="tax" items="${taxListingForm.taxes}">
          <tr class="jc_list_table_row"> 
            <td width="10%" class="jc_list_table_content">
              <div align="center">
                <input type="checkbox" name="taxIds" value="<c:out value="${tax.taxId}"/>">
              </div>
            </td>
            <td width="20%" class="jc_list_table_content">
              <html:link page="/admin/tax/taxMaint.do?process=edit" paramId="taxId" paramName="tax" paramProperty="taxId">
                <c:out value="${tax.taxCode}"/>
              </html:link>
            </td>
            <td width="25%" class="jc_list_table_content">
              <c:out value="${tax.taxName}"/>
            </td>
            <td width="25%" class="jc_list_table_content">
              <div align="right">
              	<c:out value="${tax.taxRate}"/>
              </div>
            </td>
            <td width="20%" class="jc_list_table_content" nowrap>
              <div align="center">
                <c:out value="${tax.published}"/>
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
