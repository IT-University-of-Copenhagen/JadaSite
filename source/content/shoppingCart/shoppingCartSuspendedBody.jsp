<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<span class="shopping-cart-error">
Sorry, your account has been suspended.<br><br>
Please contact us for more information.
</span>
<br><br>
<html:link page="/content/checkout/shoppingCartCancelCheckout.do?process=cancel" styleClass="shopping-cart-cancel-button">
  Cancel Checkout
</html:link>
<br><br>