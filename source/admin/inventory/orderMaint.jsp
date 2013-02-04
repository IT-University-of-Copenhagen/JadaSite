<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="pageTitle" scope="request" value="Order Maintenance"/>
<jsp:setProperty name="adminBean" property="contentPage" value="/admin/inventory/orderMaintBody.jsp"/>
<jsp:include page="/admin/web/index.jsp" />