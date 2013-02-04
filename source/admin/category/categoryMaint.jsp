<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="pageTitle" scope="request" value="Category Maintenance"/>
<jsp:setProperty name="adminBean" property="contentPage" value="/admin/category/categoryMaintBody.jsp"/>
<jsp:include page="/admin/web/index.jsp" />