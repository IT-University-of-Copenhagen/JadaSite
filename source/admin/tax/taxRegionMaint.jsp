<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="pageTitle" scope="request" value="Tax Region Maintenance"/>
<jsp:setProperty name="adminBean" property="contentPage" value="/admin/tax/taxRegionMaintBody.jsp"/>
<jsp:include page="/admin/web/index.jsp" />