<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="taxRegionListingForm" scope="session" value="${taxRegionListingForm}"/>

<script language="JavaScript">
function submitNewForm(page) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/tax/taxRegionMaint.do';
    document.forms[0].process.value = "create";
    document.forms[0].submit();
    return false;
}

function submitRemoveForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/tax/taxRegionListing.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function submitListingForm(page) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/tax/taxRegionListing.do';
    document.forms[0].process.value = "list";
    document.forms[0].submit();
    return false;
}

function submitMaintForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/tax/taxRegionMaint.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}
</script>
<html:form action="/admin/tax/taxRegionListing.do" method="post">
<html:hidden property="process" value="list"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - Tax Region Listing</td>
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
            <td class="jc_input_label">Tax Region</td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srTaxRegionDesc" mode="E,E,E" styleClass="jc_input_object"/> 
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
      <c:if test="${taxRegionListingForm.taxRegions != null}">
        <table class="jc_panel_table_header" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td width="100%">Tax Region Listing 
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
            <td class="jc_list_table_header">Tax Region</td>
            <td class="jc_list_table_header"><div align="center">Published</div></td>
          </tr>
          <c:forEach var="taxRegion" items="${taxRegionListingForm.taxRegions}">
          <tr class="jc_list_table_row"> 
            <td width="10%" class="jc_list_table_content">
              <div align="center">
                <input type="checkbox" name="taxRegionIds" value="<c:out value="${taxRegion.taxRegionId}"/>">
              </div>
            </td>
            <td width="20%" class="jc_list_table_content">
              <html:link page="/admin/tax/taxRegionMaint.do?process=edit" paramId="taxRegionId" paramName="taxRegion" paramProperty="taxRegionId">
                <c:out value="${taxRegion.taxRegionDesc}"/>
              </html:link>
            </td>
            <td width="20%" class="jc_list_table_content" nowrap>
              <div align="center">
                <c:out value="${taxRegion.published}"/>
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
