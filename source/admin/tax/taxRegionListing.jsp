<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="pageTitle" scope="request" value="Tax Region Listing"/>
<jsp:setProperty name="adminBean" property="contentPage" value="/admin/tax/taxRegionListingBody.jsp"/>
<jsp:include page="/admin/web/index.jsp" />