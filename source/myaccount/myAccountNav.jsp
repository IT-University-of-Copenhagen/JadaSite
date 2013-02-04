<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<c:set var="customer" scope="session" value="${customer}"/>
<div id="my-account-nav-inner-container">
  <div class="my-account-nav-header-container">
    <lang:contentMessage value="My account"/><br>
  </div>
  <div class="my-account-nav-header-container">
    <span id="my-account-nav-title">${myAccountBean.displayFirstName}&nbsp;${myAccountBean.displayLastName}</span>
  </div>
  <ul class="my-account-nav-container">
    <li><html:link page="/content/checkout/shoppingCart.do?process=start&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}"><lang:contentMessage value="My Cart - view my current shopping cart order"/></html:link></li><li><html:link page="/myaccount/login/myAccountLogout.do?process=logout&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}"><lang:contentMessage value="Sign-out"/></html:link></li>
  </ul>
  <div class="my-account-nav-header-container">
    <span id="my-account-nav-title"><lang:contentMessage value="My account settings"/></span>
  </div>
  <ul>
    <li><html:link page="/myaccount/identity/myAccountIdentity.do?process=start&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}"><lang:contentMessage value="Email and password"/></html:link></li>
    <li><html:link page="/myaccount/address/myAccountAddress.do?process=start&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}"><lang:contentMessage value="Address information"/></html:link></li>
    <c:if test="${myAccountBean.storeCreditCard}">
    <li><html:link page="/myaccount/payment/myAccountPayment.do?process=start&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}"><lang:contentMessage value="Payment information"/></html:link></li>
    </c:if>
  </ul>
  <div class="my-account-nav-header-container">
    <span id="my-account-nav-title"><lang:contentMessage value="My orders"/></span>
  </div>
  <ul>
    <li><html:link page="/myaccount/order/myAccountOrderStatusListing.do?process=list&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}&srPageNo=1"><lang:contentMessage value="Order Status - track and view status of current orders"/></html:link></li><li><html:link page="/myaccount/order/myAccountOrderHistoryListing.do?process=list&prefix=${contentBean.siteDomain.siteDomainPrefix}&langName=${contentBean.contentSessionKey.langName}&history=Y&srPageNo=1"><lang:contentMessage value="Order History - view history of past orders"/></html:link></li>
  </ul>
</div>