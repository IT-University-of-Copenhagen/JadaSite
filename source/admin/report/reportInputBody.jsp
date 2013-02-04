<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="reportGeneratorForm" scope="request" value="${reportGeneratorForm}"/>

<script language="JavaScript">
function submitForm() {
    document.reportGeneratorForm.action = '/${adminBean.contextPath}/admin/report/reportGenerator.do';
    document.reportGeneratorForm.submit();
    return false;
}
function submitCancelForm() {
    document.reportGeneratorForm.action = "/${adminBean.contextPath}/admin/report/reportListing.do";
    document.reportGeneratorForm.process.value = "back";
    document.reportGeneratorForm.submit();
    return false;
}
<c:if test="${reportGeneratorForm.generate}">
YAHOO.util.Event.onDOMReady(function() {
	document.reportGeneratorForm.action = '/${adminBean.contextPath}/admin/report/reportGenerator.do';
	document.reportGeneratorForm.target = 'jada_report';
	document.reportGeneratorForm.process.value = "generate";
	document.reportGeneratorForm.submit();
});
</c:if>
</script>

<html:form action="/admin/report/reportGenerator.do" method="post">
<html:hidden property="process" value="validateInput"/>
<html:hidden property="reportId"/>
<html:hidden property="reportName"/>
<html:hidden property="reportDesc"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td><a href="/${adminBean.contextPath}/admin/report/reportGeneratorListing.do?process=back">Report Listing</a> - Report</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" valign="top" class="jc_list_panel_open"> 
        <table class="jc_panel_table_header" width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_panel_table_header">
          <tr> 
            <td>Report - ${reportGeneratorForm.reportName}</td>
            <td> 
            </td>
            <td align="right"> 
            <table width="0%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><html:submit property="submitButton" value="Generate" styleClass="jc_submit_button" onclick="return submitForm();"/>&nbsp;</td>
                <td><html:submit property="submitButton" value="Cancel" styleClass="jc_submit_button" onclick="return submitCancelForm();"/>&nbsp;</td>
              </tr>
            </table>
            </td>
          </tr>
		</table>
        <table width="100%" border="0" cellspacing="0" cellpadding="5">
          <tr class="jc_list_table_row"> 
            <td width="300" class="jc_input_label">
              Description
            </td>
          </tr>
          <tr class="jc_list_table_row"> 
            <td width="300" class="jc_list_table_content">
              ${reportGeneratorForm.reportDesc}
            </td>
          </tr>
          <c:forEach var="reportParameter" items="${reportGeneratorForm.reportParameters}">
          <tr class="jc_list_table_row"> 
            <td width="300" class="jc_input_label">
              <html:hidden indexed="true" name="reportParameter" property="displayName"/>
              <html:hidden indexed="true" name="reportParameter" property="name"/>
              <html:hidden indexed="true" name="reportParameter" property="type"/>
              <html:hidden indexed="true" name="reportParameter" property="required"/>
              ${reportParameter.displayName}
            </td>
          </tr>
          <tr class="jc_list_table_row"> 
            <td width="300" class="jc_list_table_content">
              <c:choose>
                <c:when test="${reportParameter.values == null}">
	              <html:text indexed="true" styleId="id_${reportParameter.name}" name="reportParameter" property="value" styleClass="jc_input_control"/>
	              <c:if test="${reportParameter.type == 'date'}">
	                <img id="calParamSwitch_${reportParameter.name}" src="../images/icon/image_plus.gif" border="0">
	                <div id="calParamContainer_${reportParameter.name}" style="position: absolute; display:none"></div>
	                <script language="JavaScript">
	                function handleCalParam_${reportParameter.name}(type, args, obj) {
	                    var object = document.getElementById('id_${reportParameter.name}');  
	                    object.value = jc_calendar_callback(type, args, obj);
	                }
	                var calParam_${reportParameter.name} = new YAHOO.widget.Calendar("calParam_${reportParameter.name}", "calParamContainer_${reportParameter.name}", { title:"Choose a date:", close:true } );
	                calParam_${reportParameter.name}.render();
	                YAHOO.util.Event.addListener("calParamSwitch_${reportParameter.name}", "click", calParam_${reportParameter.name}.show, calParam_${reportParameter.name}, true);
	                calParam_${reportParameter.name}.selectEvent.subscribe(handleCalParam_${reportParameter.name}, calParam_${reportParameter.name}, true); 
	                </script>
	              </c:if>
                </c:when>
                <c:otherwise>
                  <html:select indexed="true" name="reportParameter" property="value">
                    <c:forEach var="option" items="${reportParameter.values}">
                      <html:option value="${option.value}">${option.label}</html:option> 
                    </c:forEach>
                  </html:select>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
          <c:if test="${reportParameter.message != ''}">
          <tr class="jc_list_table_row"> 
            <td width="300" class="jc_list_table_content">
              <span class="jc_input_error">${reportParameter.message}</span>
            </td>
          </tr>
          </c:if>
          </c:forEach>
          <tr class="jc_list_table_row"> 
            <td width="300" class="jc_input_label">
              Output format
            </td>
          </tr>
          <tr class="jc_list_table_row"> 
            <td width="300" class="jc_list_table_content">
			<html:select property="reportOutputMode" styleClass="tableContent"> 
              <html:option value="H">html</html:option> 
              <html:option value="P">pdf</html:option>  
		    </html:select>
            </td>
          </tr>
        </table>
    </td>
  </tr>
</table>
</html:form>
