<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="emptyTemplateInfo" scope="request" value="${emptyTemplateInfo}"/>
<c:out escapeXml="false" value="${emptyTemplateInfo.templatePrefix}"/>
<jsp:include page="/content/shoppingCart/shoppingCartShippingQuoteConfirmationBody.jsp" />
<c:out escapeXml="false" value="${emptyTemplateInfo.templateSuffix}"/>