<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="shippingMethodListingForm" scope="request" value="${shippingMethodListingForm}"/>

<script language="JavaScript">
function submitNewForm(page) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/shipping/shippingMethodMaint.do';
    document.forms[0].process.value = "create";
    document.forms[0].submit();
    return false;
}

function submitRemoveForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/shipping/shippingMethodListing.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}

function submitListingForm(methodType) {
    document.forms[0].action = '/<c:out value='${adminBean.contextPath}'/>/admin/shipping/shippingMethodListing.do';
    document.forms[0].process.value = methodType;
    document.forms[0].submit();
    return false;
}
</script>
<html:form action="/admin/shipping/shippingMethodListing.do" method="post">
<html:hidden property="process" value="list"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - Shipping Method Listing</td>
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
                      <html:submit property="submitButtom" value="Search" styleClass="jc_submit_button" onclick="return submitListingForm('list');"/>
                      <html:submit property="submitButtom" value="New" styleClass="jc_submit_button" onclick="return submitNewForm('');"/>
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
            <td class="jc_input_label">Shipping Method</td>
          </tr>
          <tr> 
            <td class="jc_input_object"> <layout:text layout="false" property="srShippingMethodName" mode="E,E,E" styleClass="jc_input_object"/> 
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
      <c:if test="${shippingMethodListingForm.shippingMethods != null}">
        <table class="jc_panel_table_header" width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td>Shipping Method Listing 
              Result</td>
            <td> 
              <div align="right">
                <html:submit property="submitButtom" value="Remove" styleClass="jc_submit_button" onclick="return submitRemoveForm('remove');"/>
                <html:submit property="submitButtom" value="New" styleClass="jc_submit_button" onclick="return submitNewForm('');"/>
              </div>
            </td>
          </tr>
		</table>
		<br>
        <table width="100%" border="0" cellspacing="0" cellpadding="1">
           <tr> 
            <td width="100" class="jc_list_table_header"></td>
            <td width="20%" class="jc_list_table_header">
              <div align="center"><html:link href="javascript:submitListingForm('resequence')" styleClass="jc_submit_link">Resequence</html:link></div>
            </td>
            <td width="50%" class="jc_list_table_header">Shipping Method</td>
            <td width="20%" class="jc_list_table_header"><div align="center">Published</div></td>
          </tr>
          <c:forEach var="shippingMethods" items="${shippingMethodListingForm.shippingMethods}" varStatus="xstatus">
          <tr class="jc_list_table_row"> 
            <td width="100" class="jc_list_table_content">
              <div align="center">
                <html:checkbox indexed="true" name="shippingMethods" property="selected" />&nbsp;
                <html:hidden indexed="true" name="shippingMethods" property="shippingMethodId"/>
              </div>
            </td>
            <td width="20%" class="jc_list_table_content">
              <div align="center">
                <html:text indexed="true" size="1" name="shippingMethods" property="seqNum" />
              </div>
            </td>
            <td width="50%" class="jc_list_table_content">
              <html:link page="/admin/shipping/shippingMethodMaint.do?process=edit" paramId="shippingMethodId" paramName="shippingMethods" paramProperty="shippingMethodId">
                <c:out value="${shippingMethods.shippingMethodName}"/>
              </html:link>
            </td>
            <td width="20%" class="jc_list_table_content" nowrap>
              <div align="center">
                <c:out value="${shippingMethods.published}"/>
              </div>
            </td>
          </tr>
          <c:if test="${shippingMethods.seqNumError != null}">
           <tr> 
            <td></td>
            <td class="jc_input_error"><div align="center"><c:out value="${shippingMethods.seqNumError}"/></div></td>
            <td></td>
            <td></td>
          </tr>
          </c:if>
          </c:forEach>
        </table>
      </c:if>
    </td>
  </tr>
</table>
</html:form>
