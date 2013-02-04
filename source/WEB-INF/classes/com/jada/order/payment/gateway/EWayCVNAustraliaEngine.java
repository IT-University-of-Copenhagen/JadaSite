/*
 * Copyright 2007-2010 JadaSite.

 * This file is part of JadaSite.
 
 * JadaSite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * JadaSite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JadaSite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jada.order.payment.gateway;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.eway.GatewayConnector;
import com.eway.GatewayRequest;
import com.eway.GatewayResponse;
import com.jada.content.ContentBean;
import com.jada.order.cart.ShoppingCart;
import com.jada.order.payment.AuthorizationException;
import com.jada.order.payment.NotImplementedException;
import com.jada.order.payment.PaymentEngine;
import com.jada.order.payment.PaymentEngineBase;
import com.jada.order.payment.PaymentException;
import com.jada.dao.PaymentGatewayDAO;
import com.jada.jpa.entity.CreditHeader;
import com.jada.jpa.entity.CustomerCreditCard;
import com.jada.jpa.entity.InvoiceHeader;
import com.jada.jpa.entity.OrderAddress;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.PaymentGateway;
import com.jada.jpa.entity.PaymentTran;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomain;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;
import com.jada.xml.paymentGateway.EWayCVNAustralia;
import com.paypal.soap.api.DoExpressCheckoutPaymentResponseType;
import com.paypal.soap.api.ErrorType;

public class EWayCVNAustraliaEngine extends PaymentEngineBase implements PaymentEngine {
	static String ENVIRONMENT_PRODUCTION = "live";
	static String SERVICE_URL_PRODUCTION = "https://www.eway.com.au/gateway_cvn/xmlpayment.asp";
	static String SERVICE_URL_SANDBOX = "https://www.eway.com.au/gateway_cvn/xmltest/testpage.asp";
	static public String PAYMENT_TYPE = "CC";
	
	Site site = null;
	String secureURLPrefix;
	String customerId;
	String environment;
	String siteContext;
	
	String token = null;
	String payerId = null;
	SiteDomain siteDomain = null;
	
	PaymentGateway paymentGateway = null;
	EWayCVNAustralia eWayCVNAustralia = null;
	
	static Logger logger = Logger.getLogger(EWayCVNAustraliaEngine.class);
	
	public EWayCVNAustraliaEngine(Site site, Long paymentGatewayId) throws Exception {
		this.site = site;
		paymentGateway = PaymentGatewayDAO.load(site.getSiteId(), paymentGatewayId);
		eWayCVNAustralia = (EWayCVNAustralia) Utility.joxUnMarshall(EWayCVNAustralia.class, paymentGateway.getPaymentGatewayData());

		customerId = eWayCVNAustralia.getCustomerId();
		environment = eWayCVNAustralia.getEnvironment();
	}
	
	public boolean isProvideCustomer() {
		return true;
	}
	
	public void voidPayment(Long orderHeaderId) throws AuthorizationException, PaymentException, Exception {
		paymentMessage = MESSAGE_NOTIMPLEMENTED;
		throw new NotImplementedException("");
	}
	
	public void creditPayment(Long creditHeaderId) throws AuthorizationException, PaymentException, Exception {

	}
	
	public boolean isProduction() {
		return ENVIRONMENT_PRODUCTION.equals(environment);
	}
	
	public boolean isAllowAuthorizeOnly() {
		return false;
	}
	
	public void authorizePayment(OrderHeader orderHeader, HttpServletRequest request) throws AuthorizationException, PaymentException, Exception {
		paymentMessage = MESSAGE_NOTIMPLEMENTED;
		throw new NotImplementedException("");
	}
	
	public void authorizeAndCapturePayment(InvoiceHeader invoiceHeader, HttpServletRequest request) throws AuthorizationException, PaymentException, Exception {
		GatewayConnector connector = new GatewayConnector();
		int mode = GatewayRequest.REQUEST_METHOD_CVN;
		if (isProduction()) {
			connector.setGatewayUrlCVN(SERVICE_URL_PRODUCTION);
		}
		else {
			connector.setGatewayUrlCVN(SERVICE_URL_SANDBOX);
		}
		
		OrderHeader orderHeader = invoiceHeader.getOrderHeader();
		OrderAddress billingAddress = orderHeader.getBillingAddress();
		if (billingAddress.getCustUseAddress().equals(Constants.CUST_ADDRESS_USE_CUST)) {
			billingAddress = orderHeader.getCustAddress();
		}
		OrderAddress shippingAddress = orderHeader.getBillingAddress();
		if (shippingAddress.getCustUseAddress().equals(Constants.CUST_ADDRESS_USE_BILL)) {
			shippingAddress = billingAddress;
		}
		if (shippingAddress.getCustUseAddress().equals(Constants.CUST_ADDRESS_USE_CUST)) {
			shippingAddress = orderHeader.getCustAddress();
		}
		
		GatewayRequest gwr = new GatewayRequest(mode);
	    gwr.setCustomerID(customerId);
		
	    gwr.setCardExpiryMonth(creditCardInfo.getCreditCardExpiryMonth());
	    gwr.setCardExpiryYear(creditCardInfo.getCreditCardExpiryYear());
	    gwr.setCardHoldersName(creditCardInfo.getCreditCardNum());
	    gwr.setCardNumber(creditCardInfo.getCreditCardNum());
	    String customerAddress = billingAddress.getCustAddressLine1();
	    if (Format.isNullOrEmpty(billingAddress.getCustAddressLine2())) {
	    	customerAddress += billingAddress.getCustAddressLine2();
	    }
	    gwr.setCustomerAddress(customerAddress);
	    gwr.setCustomerBillingCountry(billingAddress.getCustCountryCode());
	    gwr.setCustomerEmailAddress(orderHeader.getCustEmail());
	    gwr.setCustomerFirstName(billingAddress.getCustFirstName());
	    gwr.setCustomerInvoiceRef(orderHeader.getOrderNum());
	    
		String remoteAddress = request.getRemoteAddr();
		if (remoteAddress.split(":").length > 4) {
			logger.error("Remote address " + remoteAddress + " seems to be a IPv6 address.");
		}
		
	    gwr.setCustomerIPAddress(remoteAddress);
	    gwr.setCustomerLastName(billingAddress.getCustLastName());
	    gwr.setCustomerPostcode(billingAddress.getCustZipCode());
	    gwr.setCVN(creditCardInfo.getCreditCardVerNum());
	    gwr.setTotalAmount((int) (orderHeader.getOrderTotal() * 100));
	    //gwr.setTotalAmount(1000); 
	    GatewayResponse response = connector.sendRequest(gwr);
		if (!response.getTrxnStatus()) {
			logger.error("request = Not able to process credit card authorization for " + orderHeader.getCustAddress().getCustFirstName() + " " + orderHeader.getCustAddress().getCustLastName());
			logger.error("response = " + response.getTrxnError());
			paymentMessage = response.getTrxnError();
			throw new AuthorizationException(response.getTrxnError());
		}
		authCode = response.getAuthCode();
		paymentReference1 = response.getTrxnNumber();
		paymentReference2 = response.getTrxnReference();
	}
	
	public void cancelPayment() throws PaymentException, Exception {
		token = null;
		payerId = null;
	}
	
	public boolean isBookQty() {
		return true;
	}

	public String getSecureURLPrefix() {
		return secureURLPrefix;
	}

	public void setSecureURLPrefix(String secureURLPrefix) {
		this.secureURLPrefix = secureURLPrefix;
	}

	public void voidCredit(Long creditHeaderId) throws AuthorizationException, PaymentException, Exception {
		paymentMessage = MESSAGE_NOTIMPLEMENTED;
		throw new NotImplementedException("");
	}

	public boolean isExtendedTransaction() {
		return false;
	}

	public void voidCredit(CreditHeader creditHeader) throws AuthorizationException, PaymentException, Exception {
		paymentMessage = MESSAGE_NOTIMPLEMENTED;
		throw new NotImplementedException("");
	}

	public void voidPayment(InvoiceHeader invoiceHeader) throws AuthorizationException, PaymentException, Exception {
		paymentMessage = MESSAGE_NOTIMPLEMENTED;
		throw new NotImplementedException("");
	}

	public SiteDomain getSiteDomain() {
		return siteDomain;
	}

	public void setSiteDomain(SiteDomain siteDomain) {
		this.siteDomain = siteDomain;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void abort() {
		
	}

	public void cancelPayment(InvoiceHeader invoiceHeader)
			throws PaymentException, Exception {
	}

	public void capturePayment(InvoiceHeader invoiceHeader)
			throws PaymentException, Exception {
		paymentMessage = MESSAGE_NOTIMPLEMENTED;
		throw new NotImplementedException("");
	}
	
	public String formatErrorMessage(DoExpressCheckoutPaymentResponseType response) {
		String result = "";
		result += response.getAck().getValue().toString();
		for (ErrorType error : response.getErrors()) {
			result += " - " + error.getShortMessage() + " - " + error.getLongMessage();
		}
		return result;
	}
	
	public void creditPayment(CreditHeader creditHeader)
			throws PaymentException, Exception {
    	OrderHeader orderHeader = creditHeader.getOrderHeader();
		GatewayConnector connector = new GatewayConnector();
		int mode = GatewayRequest.REQUEST_METHOD_CVN;
		if (isProduction()) {
			connector.setGatewayUrlCVN(SERVICE_URL_PRODUCTION);
		}
		else {
			connector.setGatewayUrlCVN(SERVICE_URL_SANDBOX);
		}
		
		GatewayRequest gwr = new GatewayRequest(mode);
	    gwr.setCustomerID(customerId);
	    
	    CustomerCreditCard custCreditCard = null;
	    Iterator<?> iterator = orderHeader.getCustomer().getCustCreditCards().iterator();
	    while (iterator.hasNext()) {
	    	custCreditCard = (CustomerCreditCard) iterator.next();
	    	break;
	    }
	    if (custCreditCard == null) {
	    	paymentMessage = "Unable to locate customer credit card to refund";
	    	throw new AuthorizationException("Unable to locate customer credit card to refund");
	    }

	    gwr.setCardExpiryMonth(custCreditCard.getCustCreditCardExpiryMonth());
	    gwr.setCardExpiryYear(custCreditCard.getCustCreditCardExpiryYear());
	    gwr.setCardHoldersName(custCreditCard.getCustCreditCardNum());
	    gwr.setCardNumber(AESEncoder.getInstance().decode(custCreditCard.getCustCreditCardNum()));
	    gwr.setCustomerInvoiceRef(orderHeader.getOrderNum());
	    
	    PaymentTran payment = orderHeader.getPaymentTran();
		if (payment == null) {
			for (InvoiceHeader invoiceHeader : orderHeader.getInvoiceHeaders()) {
				payment = invoiceHeader.getPaymentTran();
			}
		}
	    gwr.setTrxnNumber(payment.getPaymentReference1());

	    gwr.setCVN(custCreditCard.getCustCreditCardVerNum());
	    gwr.setTotalAmount((int)(creditHeader.getCreditTotal() * 100));
	    //gwr.setTotalAmount(1000); 
    
	    GatewayResponse response = connector.sendRequest(gwr);
		if (!response.getTrxnStatus()) {
			logger.error("request = Not able to process credit card authorization for " + orderHeader.getCustAddress().getCustFirstName() + " " + orderHeader.getCustAddress().getCustLastName());
			logger.error("response = " + response.getTrxnError());
			paymentMessage = response.getTrxnError();
			throw new AuthorizationException(response.getTrxnError());
		}
		authCode = response.getAuthCode();
		paymentReference1 = response.getTrxnNumber();
		paymentReference2 = response.getTrxnReference();
	}

	public void callBack(HttpServletRequest request, ShoppingCart shoppingCart,
			ContentBean contentBean) throws PaymentException, Exception {
		
	}

}
