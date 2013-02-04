<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="reportGeneratorForm" scope="request" value="${reportGeneratorForm}"/>
<c:set var="pageTitle" scope="request" value="Report - ${reportGeneratorForm.reportName}"/>
<jsp:setProperty name="adminBean" property="contentPage" value="/admin/report/reportInputBody.jsp"/>
<jsp:include page="/admin/web/index.jsp" />