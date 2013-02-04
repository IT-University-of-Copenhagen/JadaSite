<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/lang.tld" prefix="lang" %>
<c:choose>
  <c:when test='${shoppingCartActionForm.cashPaymentOrder == true}'>
    <div class="shopping-cart-steps-header">
      <div align="center" class="shopping-cart-steps-inactive" id="step1">
	    <table width="120" border="0" cellpadding="10">
          <tr>
            <td class="shopping-cart-steps-num">1&nbsp;</td>
            <td class="shopping-cart-steps-text"><lang:contentMessage value="Sign-in / New User"/></td>
          </tr>
          <tr>
            <td colspan="2" class="shopping-cart-steps-line-inactive" id="step1_line"><hr></td>
          </tr>
        </table>
      </div>
      <div align="center" class="shopping-cart-steps-inactive" id="step2">
	    <table width="120" border="0" cellpadding="10"> 
          <tr>
            <td class="shopping-cart-steps-num">2&nbsp;</td>
            <td class="shopping-cart-steps-text"><lang:contentMessage value="Review User Information"/></td>
          </tr>
          <tr>
            <td colspan="2" class="shopping-cart-steps-line-inactive" id="step2_line"><hr></td>
          </tr>
        </table>
      </div>
      <div align="center" class="shopping-cart-steps-inactive" id="step3">
	    <table width="120" border="0" cellpadding="10">
          <tr>
            <td class="shopping-cart-steps-num">3&nbsp;</td>
            <td class="shopping-cart-steps-text"><lang:contentMessage value="Review Purchase"/></td>
          </tr>
          <tr>
            <td colspan="2" class="shopping-cart-steps-line-inactive" id="step3_line"><hr></td>
          </tr>
        </table>
      </div>
      <div align="center" class="shopping-cart-steps-inactive" id="step5">
	    <table width="120" border="0" cellpadding="10">
          <tr>
            <td class="shopping-cart-steps-num">4&nbsp;</td>
            <td class="shopping-cart-steps-text"><lang:contentMessage value="Transaction Complete"/></td>
          </tr>
          <tr>
            <td colspan="2" class="shopping-cart-steps-line-inactive" id="step5_line"><hr></td>
          </tr>
        </table>
      </div>
	</div>
	<div style="clear: both;"> </div>
  </c:when>
  <c:when test='${shoppingCartActionForm.paymentGatewayProvider == "PayPalEngine"}'>
    <div class="shopping-cart-steps-header">
      <div align="center" class="shopping-cart-steps-inactive" id="step1">
	    <table width="120" border="0" cellpadding="10">
          <tr>
            <td class="shopping-cart-steps-num">1&nbsp;</td>
            <td class="shopping-cart-steps-text" width="100%"><lang:contentMessage value="PayPal"/></td>
          </tr>
          <tr>
            <td id="step1_line" colspan="2" class="shopping-cart-steps-line-inactive"><hr></td>
          </tr>
        </table>
      </div>
      <div align="center" class="shopping-cart-steps-inactive" id="step2">
	    <table width="120" border="0" cellpadding="10">
          <tr>
            <td class="shopping-cart-steps-num">2&nbsp;</td>
            <td class="shopping-cart-steps-text"><lang:contentMessage value="Review Purchase"/></td>
          </tr>
          <tr>
            <td id="step2_line" colspan="2" class="shopping-cart-steps-line-inactive"><hr></td>
          </tr>
        </table>
      </div>
      <div align="center" class="shopping-cart-steps-inactive" id="step3">
	    <table width="120" border="0" cellpadding="10">
          <tr>
            <td class="shopping-cart-steps-num">3&nbsp;</td>
            <td class="shopping-cart-steps-text"><lang:contentMessage value="Transaction Complete"/></td>
          </tr>
          <tr>
            <td id="step3_line" colspan="2" class="shopping-cart-steps-line-inactive"><hr></td>
          </tr>
        </table>
      </div>
    </div>
  </c:when>
  <c:otherwise>
    <div class="shopping-cart-steps-header">
      <div align="center" class="shopping-cart-steps-inactive" id="step1">
	      <table width="120" border="0" cellpadding="10">
            <tr>
              <td class="shopping-cart-steps-num">1&nbsp;</td>
              <td class="shopping-cart-steps-text"><lang:contentMessage value="Sign-in / New User"/></td>
            </tr>
            <tr>
              <td id="step1_line" colspan="2" class="shopping-cart-steps-line-inactive"><hr></td>
            </tr>
          </table>
      </div>
      <div align="center" class="shopping-cart-steps-inactive" id="step2">
          <table width="120" border="0" cellpadding="10">
            <tr>
              <td class="shopping-cart-steps-num">2&nbsp;</td>
              <td class="shopping-cart-steps-text"><lang:contentMessage value="Review User Information"/></td>
            </tr>
            <tr>
              <td id="step2_line" colspan="2" class="shopping-cart-steps-line-inactive"><hr></td>
            </tr>
          </table>
      </div>
      <div align="center" class="shopping-cart-steps-inactive" id="step3">
          <table width="120" border="0" cellpadding="10">
            <tr>
              <td class="shopping-cart-steps-num">3&nbsp;</td>
              <td class="shopping-cart-steps-text"><lang:contentMessage value="Review Purchase"/></td>
            </tr>
            <tr>
              <td id="step3_line" colspan="2" class="shopping-cart-steps-line-inactive"><hr></td>
            </tr>
          </table>
      </div>
      <div align="center" class="shopping-cart-steps-inactive" id="step4">
	      <table width="120" border="0" cellpadding="10">
            <tr>
              <td class="shopping-cart-steps-num">4&nbsp;</td>
              <td class="shopping-cart-steps-text"><lang:contentMessage value="Credit Card Information"/></td>
            </tr>
            <tr>
              <td id="step4_line" colspan="2" class="shopping-cart-steps-line-inactive"><hr></td>
            </tr>
          </table>
      </div>
      <div align="center" class="shopping-cart-steps-inactive" id="step5">
	      <table width="120" border="0" cellpadding="10">
            <tr>
              <td class="shopping-cart-steps-num">5&nbsp;</td>
              <td class="shopping-cart-steps-text"><lang:contentMessage value="Transaction Complete"/></td>
            </tr>
            <tr>
              <td id="step5_line" colspan="2" class="shopping-cart-steps-line-inactive"><hr></td>
            </tr>
          </table>
      </div>
    </div>
  </c:otherwise>
</c:choose>
<div class="container-reset"></div>