<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<c:set var="adminBean" scope="session" value="${adminBean}"/>
<c:set var="userMaintForm" scope="request" value="${userMaintForm}"/>
<script language="JavaScript">
function submitForm(type) {
    document.forms[0].process.value = type;
    document.forms[0].submit();
    return false;
}
function submitCancelForm() {
    document.forms[0].action = "/<c:out value='${adminBean.contextPath}'/>/admin/user/userListing.do";
    document.forms[0].process.value = "back";
    document.forms[0].submit();
    return false;
}
</script>
<html:form method="post" action="/admin/user/userMaint"> 
<html:hidden property="mode"/> 
<html:hidden property="process" value=""/> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="jc_top_navigation">
  <tr> 
    <td>Administration - <a href="/<c:out value='${adminBean.contextPath}'/>/admin/user/userListing.do?process=back">User 
      Listing</a> - User Maintenance</td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr> 
    <td width="100%" valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td align="right" class="jc_panel_table_header"> 
            <table width="0%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td><html:submit property="submitButton" value="Save" styleClass="jc_submit_button" onclick="return submitForm('save')"/>&nbsp;</td>
                <layout:mode value="edit"> 
                <td><html:submit property="submitButton" value="Remove" styleClass="jc_submit_button" onclick="return submitForm('remove')"/>&nbsp;</td>
                </layout:mode> 
                <td><html:submit property="submitButton" value="Cancel" styleClass="jc_submit_button" onclick="return submitCancelForm()"/>&nbsp;</td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <table width="700" border="0" cellspacing="0" cellpadding="5">
        <tr>
          <td>
		  <table width="100%" border="0" cellspacing="0" cellpadding="3">
			  <tr>
				<td class="jc_input_label">User Id</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
				  <layout:text layout="false" property="userId" size="20" mode="E,R,E" styleClass="tableContent"/> 
				  <span class="jc_input_error">
				  <logic:messagesPresent property="userId" message="true"> 
				  <html:messages property="userId" id="errorText" message="true"> 
				  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
				  </span>
				</td>
			  </tr>
			  <tr>
				<td class="jc_input_label">Name</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
				  <input type="text" class="tableContent" value="${userMaintForm.userName}" size="50" name="userName" autocomplete="off" />
				  <span class="jc_input_error">
				  <logic:messagesPresent property="userName" message="true"> 
				  <html:messages property="userName" id="errorText" message="true"> 
				  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
				  </span> 
				</td>
			  </tr>
			  <tr>
				<td class="jc_input_label">Password</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
				  <input type="password" class="tableContent" value="${userMaintForm.userPassword}" size="20" name="userPassword" autocomplete="off" />
				  <span class="jc_input_error">
				  <logic:messagesPresent property="userPassword" message="true"> 
				  <html:messages property="userPassword" id="errorText" message="true"> 
				  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
				  </span>
				</td>
			  </tr>
			  <tr>
				<td class="jc_input_label">Verify password</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
				  <layout:password layout="false" property="verifyPassword" size="20" mode="E,E,E" styleClass="tableContent"/> 
          		</td>
			  </tr>
			  <tr>
				<td class="jc_input_label">User Type</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
                  <c:if test="${userMaintForm.hasSuperUser}">
				  <layout:radio layout="false" property="userType" mode="E,E,E" value="S"/><bean:message key="user.type.S" /><br>
                  </c:if>
                  <c:if test="${userMaintForm.hasAdministrator}">
                  <layout:radio layout="false" property="userType" mode="E,E,E" value="A"/><bean:message key="user.type.A" /><br>
                  </c:if>
                  <layout:radio layout="false" property="userType" mode="E,E,E" value="R"/><bean:message key="user.type.R" />
          		</td>
			  </tr>
			  <tr>
				<td class="jc_input_label">Active</td>
			  </tr>
			  <tr>
				<td class="jc_input_control"><layout:checkbox layout="false" property="active" mode="E,E,E" value="Y"/></td>
			  </tr>
              <tr>
              <td>&nbsp;</td>
              </tr>
			  <tr>
				<td><hr></td>
			  </tr>
              <tr>
              <td>&nbsp;</td>
              </tr>
			  <tr>
				<td class="jc_input_label">Email</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
				  <layout:text layout="false" property="userEmail" size="50" mode="E,E,E" styleClass="tableContent"/> 
				  <span class="jc_input_error">
				  <logic:messagesPresent property="userEmail" message="true"> 
				  <html:messages property="userEmail" id="errorText" message="true"> 
				  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
				  </span>
		        </td>
			  </tr>
              <tr>
                <td class="jc_input_label">User phone</td>
              </tr>
              <tr>
                <td class="jc_input_control">
                <layout:text layout="false" property="userPhone" size="20" mode="E,E,E" styleClass="tableContent"/> 
                <span class="jc_input_error">
                <logic:messagesPresent property="userPhone" message="true"> 
                <html:messages property="userPhone" id="errorText" message="true"> 
                <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
                </span>
                </td>
              </tr>
			  <tr>
				<td class="jc_input_label">Address</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
				  <layout:text layout="false" property="userAddressLine1" size="30" mode="E,E,E" styleClass="tableContent"/> 
				  <span class="jc_input_error">
				  <logic:messagesPresent property="userAddressLIne1" message="true"> 
				  <html:messages property="userAddressLIne1" id="errorText" message="true"> 
				  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
				  </span>
		        </td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
				  <layout:text layout="false" property="userAddressLine2" size="30" mode="E,E,E" styleClass="tableContent"/> 
				  <span class="jc_input_error">
				  <logic:messagesPresent property="userAddressLIne2" message="true"> 
				  <html:messages property="userAddressLIne1" id="errorText" message="true"> 
				  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
				  </span>
				</td>
			  </tr>
			  <tr>
				<td class="jc_input_label">City</td>
			  </tr>
			  <tr>
				<td class="jc_input_control">
				  <layout:text layout="false" property="userCityName" size="25" mode="E,E,E" styleClass="tableContent"/> 
				  <span class="jc_input_error">
				  <logic:messagesPresent property="userCityName" message="true"> 
				  <html:messages property="userCityName" id="errorText" message="true"> 
				  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
				  </span>
				</td>
			  </tr>
			  <tr>
				<td class="jc_input_label">State/Province</td>
			  </tr>
			  <tr>
			    <td class="jc_input_control">
				  <html:select property="userStateCode" styleClass="tableContent" style="width: 200px"> 
				  <html:optionsCollection property="states" label="label" value="value"/> 
				  </html:select> 
			    </td>
		      </tr>
			  <tr>
			    <td class="jc_input_label">Country</td>
		      </tr>
			  <tr>
			    <td class="jc_input_control">
				  <html:select property="userCountryCode" styleClass="tableContent" style="width: 200px"> 
				  <html:optionsCollection property="countries" label="label" value="value"/> 
				  </html:select> 
				</td>
		      </tr>
			  <tr class="jc_input_label">
			    <td>Zip/Postal code</td>
		      </tr>
			  <tr>
			    <td class="jc_input_control">
				  <layout:text layout="false" property="userZipCode" size="10" mode="E,E,E" styleClass="tableContent"/> 
				  <span class="jc_input_error">
				  <logic:messagesPresent property="userZipCode" message="true"> 
				  <html:messages property="userZipCode" id="errorText" message="true"> 
				  <bean:write name="errorText"/> </html:messages> </logic:messagesPresent>
				  </span>
				</td>
		      </tr>
			  <tr>
			    <td></td>
		      </tr>
		    </table>
		  </td>
          <td width="5px">
            <div style="border-left: 1px solid #dcdcdc; height: 100%"></div>
          </td>
          <td valign="top">
		  <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td class="jc_input_label">User has access to the following sites</td>
              </tr>
              <tr>
                <td class="jc_input_control">
                <logic:iterate name="userMaintForm" property="siteIds" id="siteId">
                  <html:multibox property="selectedSiteIds">
                    <bean:write name="siteId"/>
                  </html:multibox>
                  <bean:write name="siteId"/><br/>
                </logic:iterate>
                </td>
              </tr>
              <tr>
              <td>&nbsp;</td>
              </tr>
			  <tr>
				<td><hr></td>
			  </tr>
			</table>
		  </td>
        </tr>
      </table>
    </td>
  </tr>
</table></html:form>