<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<jsp:useBean id="myAccountOrderStatusListingActionForm"  type="com.jada.myaccount.order.MyAccountOrderStatusListingActionForm" scope="session" />
<c:set var="myAccountBean" scope="request" value="${myAccountOrderStatusListingActionForm}"/>
<c:set var="form" scope="session" value="${myAccountOrderStatusListingActionForm}"/>
<div id="my-account-container">
  <div id="my-account-nav-container">
  	<jsp:include page="/myaccount/myAccountNav.jsp" />
  </div>
  <div id="my-account-body-container">
    <html:form method="post" action="/myaccount/order/myAccountOrderHistoryListing.do?process=update">
    <html:hidden property="process" value=""/>
    <div id="my-account-header-container"><lang:contentMessage key="content.text.myaccount.menu.orderHistory"/></div>
    <div id="my-account-body-inner-container">
      <table border="0" cellspacing="0" cellpadding="0" id="my-account-order-listing-table">
        <tr> 
          <td colspan="5" align="right">
            <c:if test="${form.pageNo > 1}">
            <a class="my-account-order-status-nav-link" href="/${contentBean.contextPath}/myaccount/order/myAccountOrderHistoryListing.do?process=list&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}&srPageNo=<c:out value="${form.pageNo - 1}"/>">&lt;</a>
            </c:if>
            <c:forEach begin="${form.startPage}" end="${form.endPage}" var="index">
              <c:choose>
                <c:when test="${index == form.pageNo}">
                  <span id="my-account-order-status-nav">${index}</span>
                </c:when>
                <c:otherwise>
                  <a class="my-account-order-status-nav-link" href="/${contentBean.contextPath}/myaccount/order/myAccountOrderHistoryListing.do?process=list&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}&srPageNo=<c:out value='${index}'/>">${index}</a>
                </c:otherwise>
              </c:choose>
            </c:forEach>
            &nbsp;
            <c:if test="${form.pageNo < form.pageCount}">
              <a class="my-account-order-status-nav-link" href="/${contentBean.contextPath}/myaccount/order/myAccountOrderHistoryListing.do?process=list&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}&srPageNo=<c:out value="${form.pageNo + 1}"/>">&gt;</a>
            </c:if>
          </td>
        </tr>
        <tr> 
          <td nowrap><span class="my-account-order-listing-table-title"><lang:contentMessage value="Order date"/></span></td>
          <td nowrap><span class="my-account-order-listing-table-title"><lang:contentMessage value="Order number"/></span></td>
          <td nowrap><span class="my-account-order-listing-table-title"><lang:contentMessage value="Status"/></span></td>
          <td nowrap><span class="my-account-order-listing-table-title"><lang:contentMessage value="Ship date"/></span></td>
          <td nowrap align="right"><span class="my-account-order-table-title"><lang:contentMessage value="Total"/></span></td>
        </tr>
        <c:forEach var="order" items="${form.orders}">
        <tr> 
          <td nowrap><span class="my-account-order-listing-table-value"><c:out value='${order.orderDatetime}'/></span></td>
          <td nowrap>
            <a class="my-account-order-status-link" href="/<c:out value='${contentBean.contextPath}'/>/myaccount/order/myAccountOrderHistory.do?process=start&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}&orderHeaderId=<c:out value='${order.orderHeaderId}'/>">
              ${order.orderNum}
            </a>
          </td>
          <td nowrap><span class="my-account-order-listing-table-value"><c:out value='${order.orderStatus}'/></span></td>
          <td nowrap><span class="my-account-order-listing-table-value"><c:out value='${order.shipDatetime}'/></span></td>
          <td nowrap align="right"><span class="my-account-order-listing-table-value"><c:out value='${order.orderTotal}'/></span></td>
        </tr>
        </c:forEach>
      </table>
    </div>
    </html:form>
  </div>
</div>