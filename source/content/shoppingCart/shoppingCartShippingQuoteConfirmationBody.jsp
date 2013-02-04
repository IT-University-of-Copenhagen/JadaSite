<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<c:set var="shoppingCartForm" scope="request" value="${shoppingCartActionForm}"/>
<div id="shopping-cart-container">
<span id="shopping-cart-shippingquote-title">Shipping Quote Confirmation</span>
<br>
<br>
We will provide you with your shipping rate shortly.
<br>
<div id="shopping-cart-footer-container">
  <c:out value='${shoppingCartActionForm.shoppingCartMessage}' escapeXml='false'/>
</div>
</div>
