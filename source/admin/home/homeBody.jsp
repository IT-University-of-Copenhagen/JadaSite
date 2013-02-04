<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<script type="text/javascript" language="JavaScript"><!--
function submitForm(type) {
    document.homeActionForm.process.value = type;
    document.homeActionForm.submit();
}
function show(id) {
  obj = document.getElementById(id);
  className = obj.className;
  document.getElementById('password').className = 'jc_simple_hide';
  document.getElementById('profile').className = 'jc_simple_hide';
  if (className == 'jc_simple_show') {
    obj.className = 'jc_simple_hide';
  }
  else {
    obj.className = 'jc_simple_show';
  }
}

//--></script>
<c:set var="homeActionForm" scope="request" value="${homeActionForm}"/>
<html:form method="post" action="/admin/home/home" enctype="multipart/form-data"> 
<html:hidden property="process" value=""/> 
<html:hidden property="userId"/> 
<br>
<br>
<div style="width: 400px">
  <div class="jc_home_table_body">
    <table width="0" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td nowrap>Welcome <c:out value='${homeActionForm.userName}'/></td>
      <td width="100%" align="right">
      <html:link href="javascript:show('password');" styleClass="jc_small_link">
        Password
      </html:link>
      &nbsp;&nbsp;
      <html:link href="javascript:show('profile');" styleClass="jc_small_link">
        Profile
      </html:link>
      </td>
    </tr>
    </table>
    <hr>
    <table width="0" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td class="jc_round_table_label" width="150" nowrap>Last login at</td>
        <td class="jc_round_table_value" width="100%">
          <div align="right"><c:out value="${homeActionForm.userLastLoginDatetime}"/></div>
        </td>
      </tr>
      <tr>
        <td colspan="2">
        <c:set var="style" value="jc_simple_hide"/>
        <c:if test="${homeActionForm.tabName == 'password'}">
          <c:set var="style" value="jc_simple_show"/>
        </c:if>
        <div id="password" class='<c:out value="${style}"/>'>
         <table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr>
            <td><hr></td>
          </tr>
          <tr>
            <td>Password</td>
          </tr>
          <tr>
            <td>
              <html:password property="userPassword" size="20" styleClass="tableContent"/>
              <span class="jc_input_error">
              <logic:messagesPresent property="userPassword" message="true"> 
                <html:messages property="userPassword" id="errorText" message="true"> 
                <bean:write name="errorText"/> </html:messages>
              </logic:messagesPresent>
              </span>
            </td>
          </tr>
          <tr>
            <td>Verify Password</td>
          </tr>
          <tr>
            <td>
              <html:password property="userVerifyPassword" size="20" styleClass="tableContent"/>
              <span class="jc_input_error">
              <logic:messagesPresent property="userVerifyPassword" message="true"> 
                <html:messages property="userVerifyPassword" id="errorText" message="true"> 
                <bean:write name="errorText"/> </html:messages>
              </logic:messagesPresent>
              </span>
            </td>
          </tr>
          <tr>
            <td nowrap>
              <br>
              <html:link href="javascript:submitForm('password');" styleClass="jc_navigation_link">
                Change Password
              </html:link>
            </td>
          </tr>
        </table>
        </div>
        <c:set var="style" value="jc_simple_hide"/>
        <c:if test="${homeActionForm.tabName == 'profile'}">
          <c:set var="style" value="jc_simple_show"/>
        </c:if>
        <div id="profile" class='<c:out value="${style}"/>'>
        <table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr>
            <td><hr></td>
          </tr>
          <tr>
            <td>Name</td>
          </tr>
          <tr>
            <td>
              <html:text property="userName" size="50" maxlength="50" styleClass="tableContent"/>
              <span class="jc_input_error">
              <logic:messagesPresent property="userName" message="true"> 
                <html:messages property="userName" id="errorText" message="true"> 
                <bean:write name="errorText"/> </html:messages>
              </logic:messagesPresent>
              </span>
            </td>
          </tr>
          <tr>
            <td>Email</td>
          </tr>
          <tr>
            <td>
			  <html:text property="userEmail" size="50" maxlength="50" styleClass="tableContent"/> 
			  <span class="jc_input_error">
			  <logic:messagesPresent property="userEmail" message="true"> 
			  <html:messages property="userEmail" id="errorText" message="true"> 
			  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
			  </span>
            </td>
          </tr>
          <tr>
            <td>Phone</td>
          </tr>
          <tr>
            <td>
    		  <html:text property="userPhone" size="20" maxlength="20" styleClass="tableContent"/> 
    		  <span class="jc_input_error">
    		  <logic:messagesPresent property="userPhone" message="true"> 
    		  <html:messages property="userPhone" id="errorText" message="true"> 
    		  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
    		  </span>
            </td>
          </tr>
          <tr>
            <td>Address</td>
          </tr>
          <tr>
            <td>
              <html:text property="userAddressLine1" size="30" maxlength="30" styleClass="tableContent"/> 
              <span class="jc_input_error">
              <logic:messagesPresent property="userAddressLIne1" message="true"> 
              <html:messages property="userAddressLIne1" id="errorText" message="true"> 
              <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
              </span>
            </td>
          </tr>
          <tr>
            <td>
              <html:text property="userAddressLine2" size="30" maxlength="30" styleClass="tableContent"/> 
              <span class="jc_input_error">
              <logic:messagesPresent property="userAddressLIne2" message="true"> 
              <html:messages property="userAddressLIne2" id="errorText" message="true"> 
              <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
              </span>
            </td>
          </tr>
          <tr>
            <td>City</td>
          </tr>
          <tr>
            <td>
              <html:text property="userCityName" size="25" maxlength="25" styleClass="tableContent"/> 
              <span class="jc_input_error">
              <logic:messagesPresent property="userCityName" message="true"> 
              <html:messages property="userCityName" id="errorText" message="true"> 
              <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
              </span>
            </td>
          </tr>
          <tr>
            <td>State</td>
          </tr>
          <tr>
            <td>
              <html:select property="userStateCode" styleClass="tableContent" style="width: 200px"> 
              <html:optionsCollection property="states" label="label" value="value"/> 
              </html:select> 
            </td>
          </tr>
          <tr>
            <td>Country</td>
          </tr>
          <tr>
            <td>
              <html:select property="userCountryCode" styleClass="tableContent" style="width: 200px"> 
              <html:optionsCollection property="countries" label="label" value="value"/> 
              </html:select> 
            </td>
          </tr>
          <tr>
            <td>Zip/Postal Code</td>
          </tr>
          <tr>
            <td>
              <html:text property="userZipCode" size="10" maxlength="10" styleClass="tableContent"/> 
              <span class="jc_input_error">
              <logic:messagesPresent property="userZipCode" message="true"> 
              <html:messages property="userZipCode" id="errorText" message="true"> 
              <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
              </span>
            </td>
          </tr>
          <tr>
            <td>
              <br>
              <html:link href="javascript:submitForm('update');" styleClass="jc_navigation_link">
                Update
              </html:link>
            </td>
          </tr>
        </table>
        </div>
        </td>
      </tr>
    </table>
    <br>
  </div>
</div>
<br>
<br>
<div style="width: 400px">
  <div class=jc_home_table_body>
    Site - <c:out value='${homeActionForm.siteId}'/>
    <hr>
    Switch to another site<br>
    <html:select property="siteId" onchange="javascript:submitForm('toggle')"> 
    <html:optionsCollection property="siteIds" value="value" label="label"/> 
    </html:select>
    <br>
  </div>
</div>
<br>
<br>
<div style="width: 400px">
  <!--  
  <b class="jc_round_table_rtop"> <b class="jc_round_table_r1"></b> <b class="jc_round_table_r2"></b> <b class="jc_round_table_r3"></b> 
    <b class="jc_round_table_r4"></b>
  </b>
  <div class="jc_round_table_header">Statistics</div>
  -->
  <div class=jc_home_table_body>
    Statistics
    <hr>
    <table width="0" border="0" cellspacing="0" cellpadding="3">
      <c:forEach var="serverStats" items="${homeActionForm.serverStats}">
      <tr>
        <td class="jc_round_table_label" width="150"><bean:message key="${serverStats.label}" /></td>
        <td class="jc_round_table_value">
          <c:out value="${serverStats.value}"/>
        </td>
      </tr>
      </c:forEach>
    </table>
  </div>
</div>
</html:form>
