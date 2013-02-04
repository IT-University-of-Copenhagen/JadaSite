<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<span class="jc_detail_title_text">
  <logic:messagesPresent property="message" message="true"> 
    <html:messages property="message" id="messageText" message="true"> 
    <bean:write name="messageText"/>
    </html:messages>
  </logic:messagesPresent>
</span>