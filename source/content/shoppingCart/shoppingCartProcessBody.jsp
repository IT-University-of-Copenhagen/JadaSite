<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>  
<c:set var="contentBean" scope="request" value="${contentBean}"/>
<c:set var="shoppingCartForm" scope="request" value="${shoppingCartActionForm}"/>

<style>
.shopping-cart-panel-header {
  border: 1px solid #999999;
  background-color: #cccccc;
}

.shopping-cart-panel-disable {
  display: none;
}
</style>

<script type="text/javascript" language="JavaScript">
var DISABLECLASS = "shopping-cart-panel-disable";
$(function() {
	$("#shopping-cart-panels > h3").addClass("panel-header");
	$("#shopping-cart-panels > div").addClass("panel-hide");
	$("#shopping-cart-panels > h3").click(function(event) {
	  if ($(this).hasClass(DISABLECLASS)) {
		return;
	  }
	  if ($(this).children("div").css("display") == "none") {
	    $("#shopping-cart-panels > div > div").css("display","none");
	    $(this).children("div").slideToggle("slow", "linear");
	  }
	});
});

function removeMsg(parent) {
  $(parent).find('span[id^="msg-"]').each(function(index, e) {
	  $(e).html('');
  });
}

function showMsg(jsonObject) {
  for (var i = 0; i < jsonObject.messages.length; i++) {
    var message = jsonObject.messages[i];
    var object = $("#msg-" + message.key);
    if (object.length > 0) {
      object.text(message.value);
    }
  }
}

function login() {
  $('#shoppingCartLoginForm').submit(function() {
    removeMsg('#shoppingCartForm');
    $.ajax( {
        timeout: 30000,
        url: $(this).attr('action'),
        type: $(this).attr('method'),
        dataType: 'json',
        data: $(this).serialize(),
        success: function(response) {
          if (response.status != 'success') {
            showMsg(response);
            return;
          }
          else {
            var address = response.address;
            $('#custEmail').text(address.custEmail);
            $('#shoppingCartUserForm').find('[name="custId"]').val(address.custId);
            $('#shoppingCartUserForm').find('[name="custFirstName"]').val(address.custFirstName);
            $('#shoppingCartUserForm').find('[name="custLastName"]').val(address.custLastName);
            $('#shoppingCartUserForm').find('[name="custAddressLine1"]').val(address.custAddressLine1);
            $('#shoppingCartUserForm').find('[name="custAddressLine2"]').val(address.custAddressLine2);
            $('#shoppingCartUserForm').find('[name="custCityName"]').val(address.custCityName);
            $('#shoppingCartUserForm').find('[name="custStateCode"]').val(address.custStateCode);
            $('#shoppingCartUserForm').find('[name="custStateName"]').val(address.custStateName);
            $('#shoppingCartUserForm').find('[name="custCountryCode"]').val(address.custCountryCode);
            $('#shoppingCartUserForm').find('[name="custCountryName"]').val(address.custCountryName);
            $('#shoppingCartUserForm').find('[name="custZipCode"]').val(address.custZipCode);
            $('#shoppingCartUserForm').find('[name="custPhoneNum"]').val(address.custPhoneNum);
            $('#shoppingCartUserForm').find('[name="custFaxNum"]').val(address.custFaxNum);
            $('#shoppingCartUserForm').find('[name="custStateName"]').val(address.custStateName);
            $('#shoppingCartUserForm').find('[name="custCountryName"]').val(address.custCountryName);
            populateStateCodes('custCountryCode', 'custStateCode', 'custStateName', address.custStateCode);
           
            $('#shoppingCartUserForm').find('[name="billingUseAddress"]').val(response.billingUseAddress);
            if (response.billingCustAddressId) {
              $('#shoppingCartUserForm').find('[name="billingCustFirstName"]').val(response.billingCustFirstName);
      	      $('#shoppingCartUserForm').find('[name="billingCustLastName"]').val(response.billingCustLastName);
          	  $('#shoppingCartUserForm').find('[name="billingCustAddressLine1"]').val(response.billingCustAddressLine1);
              $('#shoppingCartUserForm').find('[name="billingCustAddressLine2"]').val(response.billingCustAddressLine2);
              $('#shoppingCartUserForm').find('[name="billingCustCityName"]').val(response.billingCustCityName);
              $('#shoppingCartUserForm').find('[name="billingCustStateCode"]').val(response.billingCustStateCode);
              $('#shoppingCartUserForm').find('[name="billingCustStateName"]').val(response.billingCustStateName);
              $('#shoppingCartUserForm').find('[name="billingCustCountryCode"]').val(response.billingCustCountryCode);
              $('#shoppingCartUserForm').find('[name="billingCustCountryName"]').val(response.billingCustCountryName);
              $('#shoppingCartUserForm').find('[name="billingCustZipCode"]').val(response.billingCustZipCode);
              $('#shoppingCartUserForm').find('[name="billingCustPhoneNum"]').val(response.billingCustPhoneNum);
              $('#shoppingCartUserForm').find('[name="billingCustFaxNum"]').val(response.billingCustFaxNum);
              $('#shoppingCartUserForm').find('[name="billingCustStateName"]').val(response.billingCustStateName);
              $('#shoppingCartUserForm').find('[name="billingCustCountryName"]').val(response.billingCustCountryName);
              populateStateCodes('billingCustCountryCode', 'billingCustStateCode', 'billingCustStateName', response.billingCustStateCode);
            }
            
            $('#shoppingCartUserForm').find('[name="shippingUseAddress').val(response.shippingUseAddress);
            if (response.shippingCustAddressId) {
              $('#shoppingCartUserForm').find('[name="shippingCustFirstName"]').val(response.shippingCustFirstName);
              $('#shoppingCartUserForm').find('[name="shippingCustLastName"]').val(response.shippingCustLastName);
              $('#shoppingCartUserForm').find('[name="shippingCustAddressLine1"]').val(response.shippingCustAddressLine1);
              $('#shoppingCartUserForm').find('[name="shippingCustAddressLine2"]').val(response.shippingCustAddressLine2);
              $('#shoppingCartUserForm').find('[name="shippingCustCityName"]').val(response.shippingCustCityName);
              $('#shoppingCartUserForm').find('[name="shippingCustStateCode"]').val(response.shippingCustStateCode);
              $('#shoppingCartUserForm').find('[name="shippingCustStateName"]').val(response.shippingCustStateName);
              $('#shoppingCartUserForm').find('[name="shippingCustCountryCode"]').val(response.shippingCustCountryCode);
              $('#shoppingCartUserForm').find('[name="shippingCustCountryName"]').val(response.shippingCustCountryName);
              $('#shoppingCartUserForm').find('[name="shippingCustZipCode"]').val(response.shippingCustZipCode);
              $('#shoppingCartUserForm').find('[name="shippingCustPhoneNum"]').val(response.shippingCustPhoneNum);
              $('#shoppingCartUserForm').find('[name="shippingCustFaxNum"]').val(response.shippingCustFaxNum);
              $('#shoppingCartUserForm').find('[name="shippingCustStateName"]').val(response.shippingCustStateName);
              $('#shoppingCartUserForm').find('[name="shippingCustCountryName"]').val(response.shippingCustCountryName);
              populateStateCodes('shippingCustCountryCode', 'shippingCustStateCode', 'shippingCustStateName', response.shippingCustStateCode);
            }
          }
          $("#shopping-cart-signin").addClass(DISABLECLASS);       
          $("#shopping-cart-user").removeClass(DISABLECLASS);
        }
      }
    );
    return false;
  });
  $('#shoppingCartLoginForm').submit();
}

function submitNewUser() {
  $.ajax( {
      timeout: 30000,
      url: $("#shoppingCartNewUserForm").attr('action'),
      type: $("#shoppingCartNewUserForm").attr('method'),
      dataType: 'json',
      data: $("#shoppingCartNewUserForm").serialize(),
      success: function(response) {
	  	removeMsg("#shoppingCartNewUserForm");
        if (response.status != 'success') {
          showMsg(response);
          return false;
        }
        var address = response.address;
        $('#custEmail').text(address.custEmail);
        $('#shoppingCartUserForm').find('[name="custId"]').val(address.custId);
        $("#shopping-cart-signin").addClass(DISABLECLASS);       
        $("#shopping-cart-user").removeClass(DISABLECLASS);
        return;
      }
    }
  );
  return false;
}

function submitUser() {
  $.ajax( {
      timeout: 30000,
      url: $("#shoppingCartUserForm").attr('action'),
      type: $("#shoppingCartUserForm").attr('method'),
      dataType: 'json',
      data: $("#shoppingCartUserForm").serialize(),
      success: function(response) {
        if (response.status != 'success') {
          showMsg(response);
          return false;
        }
        $("#shoppingCartShippingForm > shippingMethodId").find('option').remove();
        $.each(response.shippingMethods, function(i, item) {
            $("#shoppingCartShippingForm").find('[name="shippingMethodId"]')
              .find('option')
              .end()
              .append('<option value="' + item.shippingMethodId + '">' + item.shippingMethodName + '</option>');
        });
        $("#shopping-cart-signin").addClass(DISABLECLASS);       
        $("#shopping-cart-user").addClass(DISABLECLASS);
        $("#shopping-cart-shipping").removeClass(DISABLECLASS);
      }
    }
  );
  return false;
}

function submitShipping() {
  $.ajax( {
      timeout: 30000,
      url: $("#shoppingCartShippingForm").attr('action'),
      type: $("#shoppingCartShippingForm").attr('method'),
      dataType: 'json',
      data: $("#shoppingCartShippingForm").serialize(),
      success: function(response) {
        if (response.status != 'success') {
          showMsg(response);
          return false;
        }
        if (response.cartInfo.cashPaymentOrder) {
            window.location.href = response.url;
            return;
        }
        var custCreditCard = response.custCreditCard;
        $('#shoppingCartCreditCardForm').find('[name="custCreditCardFullName"]').val(custCreditCard.custCreditCardFullName);
        $('#shoppingCartCreditCardForm').find('[name="creditCardId"]').find('option[value="' + custCreditCard.creditCardId + '"]').attr("selected","selected");
        $('#shoppingCartCreditCardForm').find('[name="custCreditCardNum"]').val(custCreditCard.custCreditCardNum);
        $('#shoppingCartCreditCardForm').find('[name="custCreditCardExpiryMonth"]').val(custCreditCard.custCreditCardExpiryMonth);
        $('#shoppingCartCreditCardForm').find('[name="custCreditCardExpiryYear"]').val(custCreditCard.custCreditCardExpiryYear);
        $('#shoppingCartCreditCardForm').find('[name="custCreditCardVerNum"]').val(custCreditCard.custCreditCardVerNum);
        $("#shopping-cart-signin").addClass(DISABLECLASS);       
        $("#shopping-cart-user").addClass(DISABLECLASS);
        $("#shopping-cart-shipping").addClass(DISABLECLASS);
        var cartInfo = response.cartInfo;
        if (cartInfo.isCashPaymentOrder) {
            window.location.href = response.url;
            return;
        }
        $("#shopping-cart-creditcard").removeClass(DISABLECLASS);
      }
    }
  );
  return false;
}

function submitPayPalShipping() {
  $.ajax( {
      timeout: 30000,
      url: $("#shoppingCartShippingForm").attr('action'),
      type: $("#shoppingCartShippingForm").attr('method'),
      dataType: 'json',
      data: $("#shoppingCartShippingForm").serialize(),
      success: function(response) {
        if (response.status != 'success') {
          showMsg(response);
          return false;
        }
      }
    }
  );
  return false;
}

function submitPayment() {
  $.ajax( {
      timeout: 30000,
      url: $("#shoppingCartCreditCardForm").attr('action'),
      type: $("#shoppingCartCreditCardForm").attr('method'),
      dataType: 'json',
      data: $("#shoppingCartCreditCardForm").serialize(),
      success: function(response) {
        if (response.status != 'success') {
          showMsg(response);
          return false;
        }
        window.location.href = response.url;
        return;
      }
    }
  );
  return false;
}



</script>
<div style="text-align: left; padding: 10px;">
Home &gt;
<span id="category-breadcrumb-title">Check out</span>
</div>
<div id="body-panel">
  <div id="main-panel">
    <div id="shopping-cart-panels">
      <c:choose>
        <c:when test='${shoppingCartForm.cashPaymentOrder}'>
          <div><h3>1&nbsp;<lang:contentMessage value="Sign-in / New User"/></h3>
          <div id="shopping-cart-signin">
            <jsp:include page="/content/shoppingCart/shoppingCartProcessSignin.jsp" />
          </div>
          </div>
          <div><h3>2&nbsp;<lang:contentMessage value="Review User Information"/></h3>
          <div id="shopping-cart-user" class="shopping-cart-panel-disable">
            <jsp:include page="/content/shoppingCart/shoppingCartProcessUser.jsp" />
          </div>
          </div>
          <div><h3>3&nbsp;<lang:contentMessage value="Shipping method"/></h3>
          <div id="shopping-cart-shipping" class="shopping-cart-panel-disable">
            <jsp:include page="/content/shoppingCart/shoppingCartProcessShipping.jsp" />
          </div>
          </div>
        </c:when>
        <c:when test='${shoppingCartForm.payPalOrder}'>
          <div><h3>1&nbsp;<lang:contentMessage value="Sign-in / New User"/></h3>
          <div id="shopping-cart-signin">
            <jsp:include page="/content/shoppingCart/shoppingCartProcessSignin.jsp" />
          </div>
          </div>
          <div><h3>2&nbsp;<lang:contentMessage value="Shipping method"/></h3>
          <div id="shopping-cart-shipping" class="shopping-cart-panel-disable">
            <jsp:include page="/content/shoppingCart/shoppingCartProcessPayPalShipping.jsp" />
          </div>
          </div>
        </c:when>
        <c:otherwise>
          <div><h3>1&nbsp;<lang:contentMessage value="Sign-in / New User"/></h3>
          <div id="shopping-cart-signin">
            <jsp:include page="/content/shoppingCart/shoppingCartProcessSignin.jsp" />
          </div>
          </div>
          <div><h3>2&nbsp;<lang:contentMessage value="Review User Information"/></h3>
          <div id="shopping-cart-user" class="shopping-cart-panel-disable">
            <jsp:include page="/content/shoppingCart/shoppingCartProcessUser.jsp" />
          </div>
          </div>
          <div><h3>3&nbsp;<lang:contentMessage value="Shipping method"/></h3>
          <div id="shopping-cart-shipping" class="shopping-cart-panel-disable">
            <jsp:include page="/content/shoppingCart/shoppingCartProcessShipping.jsp" />
          </div>
          </div>
          <div><h3>4&nbsp;<lang:contentMessage value="Credit Card Information"/></h3>
          <div id="shopping-cart-creditcard" class="shopping-cart-panel-disable">
            <jsp:include page="/content/shoppingCart/shoppingCartProcessCreditCard.jsp" />
          </div>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
  <div class="right-panel">
    <div class="component-container shopping-cart-summary-text">
    This is a test
    </div>
  </div>
  <div class="container-reset"></div>
</div>
