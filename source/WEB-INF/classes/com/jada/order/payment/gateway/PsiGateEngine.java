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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLContext;
import org.xml.sax.InputSource;

import com.jada.order.cart.ShoppingCart;
import com.jada.order.document.OrderEngine;
import com.jada.order.payment.AuthorizationException;
import com.jada.order.payment.CreditCardInfo;
import com.jada.order.payment.PaymentEngine;
import com.jada.order.payment.PaymentEngineBase;
import com.jada.order.payment.PaymentException;
import com.jada.content.ContentBean;
import com.jada.dao.CreditHeaderDAO;
import com.jada.dao.PaymentGatewayDAO;
import com.jada.dao.SiteDomainDAO;
import com.jada.jpa.entity.CreditHeader;
import com.jada.jpa.entity.InvoiceHeader;
import com.jada.jpa.entity.OrderAddress;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.PaymentGateway;
import com.jada.jpa.entity.PaymentTran;
import com.jada.jpa.entity.Site;
import com.jada.system.ApplicationGlobal;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;
import com.jada.xml.paymentGateway.PSIGate;
import com.jada.xml.psigate.Order;
import com.jada.xml.psigate.Result;
import com.jada.xml.site.SiteDomainParamBean;

public class PsiGateEngine extends PaymentEngineBase implements PaymentEngine {
	static String PSIGATE_ENVIRONMENT_PRODUCTION = "live";
	static String SERVICE_URL_PRODUCTION = "https://secure.psigate.com:7934/Messenger/XMLMessenger";
	static String SERVICE_URL_SANDBOX = "https://dev.psigate.com:7989/Messenger/XMLMessenger";
	
	static String RESULT_APPROVED = "APPROVED";
	
	static String PAYMENT_TYPE = "CC";
	static String CARDACTION_SALE = "0";
	static String CARDACTION_PREAUTH = "1";
	static String CARDACTION_POSTAUTH = "2";
	static String CARDACTION_CREDIT = "3";
	static String CARDACTION_FORCED_POSTAUTH = "4";
	static String CARDACTION_VOID = "9";
	
	Site site = null;
	String storeId;
	String passphrase;
	String bname;
	String bcompany;
	String baddress1;
	String baddress2;
	String bcity;
	String bprovince;
	String bcountry;
	String bpostalcode;
	String phone;
	String fax;
	String email;
	String environment;
	
	XMLContext xmlContext = null;
	
	static Logger logger = Logger.getLogger(PsiGateEngine.class);
	
	public PsiGateEngine(Site site, Long paymentGatewayId) throws Exception {
		this.site = site;
		PaymentGateway paymentGateway = PaymentGatewayDAO.load(site.getSiteId(), paymentGatewayId);
		PSIGate psiGate = (PSIGate) Utility.joxUnMarshall(PSIGate.class, paymentGateway.getPaymentGatewayData());

		String value = "";
		SiteDomainParamBean siteDomainParamBean = SiteDomainDAO.getSiteDomainParamBean(site.getSiteDomainDefault().getSiteDomainLanguage(), 
																					   site.getSiteDomainDefault().getSiteDomainLanguage());
		
		storeId = psiGate.getStoreId();
		passphrase = psiGate.getPassPhrase();
		environment = psiGate.getEnvironment();

		bname = siteDomainParamBean.getBusinessContactName();
		bcompany = siteDomainParamBean.getBusinessCompany();
		baddress1 = siteDomainParamBean.getBusinessAddress1();
		baddress2 = siteDomainParamBean.getBusinessAddress2();
		bcity = siteDomainParamBean.getBusinessCity();
		value = siteDomainParamBean.getBusinessStateCode();
		bprovince = Utility.getStateName(site.getSiteId(), value);
		value = siteDomainParamBean.getBusinessCountryCode();
		bcountry = Utility.getCountryName(site.getSiteId(), value);
		bpostalcode = siteDomainParamBean.getBusinessPostalCode();
		phone = siteDomainParamBean.getBusinessPhone();
		fax = siteDomainParamBean.getBusinessFax();
		email = siteDomainParamBean.getBusinessEmail();
		
		Mapping mapping = new Mapping();
		
		InputSource input = new InputSource(getClass().getResourceAsStream("/com/jada/xml/psigate/OrderMapping.xml"));
		mapping.loadMapping(input);
		xmlContext = new XMLContext();
		xmlContext.addMapping(mapping);
		
		input = new InputSource(getClass().getResourceAsStream("/com/jada/xml/psigate/ResultMapping.xml"));
		mapping.loadMapping(input);
		xmlContext = new XMLContext();
		xmlContext.addMapping(mapping);		
	}
	
	public boolean isProvideCustomer() {
		return false;
	}
	
	private boolean isProduction() {
		return PSIGATE_ENVIRONMENT_PRODUCTION.equals(environment);
	}
	
	public boolean isAllowAuthorizeOnly() {
		return true;
	}
	
	public void callBack(HttpServletRequest request, ShoppingCart shoppingCart, ContentBean contentBean) throws PaymentException, Exception {
		return;
	}

	public void cancelPayment() throws PaymentException, Exception {
		return;
	}
	
	public void voidCredit(Long creditHeaderId) throws AuthorizationException, PaymentException, Exception {
    	CreditHeader creditHeader = CreditHeaderDAO.load(site.getSiteId(), creditHeaderId);

		Order order = new Order();
		order.setStoreID(storeId);
		order.setPassphrase(passphrase);
		order.setBname(bname);
		PaymentTran payment = creditHeader.getPaymentTran();
		order.setTransRefNumber(payment.getPaymentReference2());
		order.setOrderID(payment.getPaymentReference3());
		order.setPaymentType(PAYMENT_TYPE);
		order.setCardAction(CARDACTION_VOID);
		
		String url = SERVICE_URL_SANDBOX;
		if (isProduction()) {
			url = SERVICE_URL_PRODUCTION;
		}
	    Result result = null;
		try {
			result = callWebService(url, order);
		}
		catch (Exception e) {
			logger.error(e);;
			throw new PaymentException(e.getMessage());
		}
		String approved = result.getApproved();
		if (!approved.equals(RESULT_APPROVED)) {
			paymentMessage = result.getErrMsg();
			throw new AuthorizationException(approved + " - " + result.getErrMsg());
		}
		String transactionId = result.getReturnCode();
		String ids[] = transactionId.split(":");
		authCode = ids[1];
		paymentReference1 = transactionId;
		paymentReference2 = result.getTransRefNumber();
		paymentReference3 = result.getOrderID();
		paymentType = result.getPaymentType();
	}
	
	public boolean isBookQty() {
		return false;
	}

	public boolean isExtendedTransaction() {
		return true;
	}

	private Order formatOrder(OrderHeader orderHeader, HttpServletRequest request) { 
		String remoteAddr = request.getRemoteAddr();
		OrderEngine orderEngine = new OrderEngine(orderHeader, null);
		CreditCardInfo creditCardInfo = this.getCreditCardInfo();
		
		Order order = new Order();
		order.setStoreID(storeId);
		order.setPassphrase(passphrase);
		
		OrderAddress orderAddress = orderHeader.getCustAddress();
		if (orderHeader.getBillingAddress().getCustUseAddress().equals(Constants.CUST_ADDRESS_USE_OWN)) {
			orderAddress = orderHeader.getBillingAddress();
		}
		order.setBname(orderAddress.getCustFirstName() + " " + orderAddress.getCustLastName());
		order.setBcompany("");
		order.setBaddress1(orderAddress.getCustAddressLine1());
		order.setBaddress2(orderAddress.getCustAddressLine2());
		order.setBcity(orderAddress.getCustCityName());
		order.setBprovince(orderAddress.getCustStateName());
		order.setBcountry(orderAddress.getCustCountryName());
		order.setBpostalcode(orderAddress.getCustZipCode());
		
		OrderAddress shipAddress = orderHeader.getCustAddress();
		if (orderHeader.getShippingAddress().getCustUseAddress().equals(Constants.CUST_ADDRESS_USE_OWN)) {
			shipAddress = orderHeader.getShippingAddress();
		}
		else if (orderHeader.getShippingAddress().getCustUseAddress().equals(Constants.CUST_ADDRESS_USE_BILL)) {
			if (orderHeader.getBillingAddress().getCustUseAddress().equals(Constants.CUST_ADDRESS_USE_OWN)) {
				shipAddress = orderHeader.getBillingAddress();
			}
			else {
				shipAddress = orderHeader.getCustAddress();
			}
		}
		order.setSname(shipAddress.getCustFirstName() + " " + shipAddress.getCustLastName());
		order.setScompany("");
		order.setSaddress1(shipAddress.getCustAddressLine1());
		order.setSaddress2(shipAddress.getCustAddressLine2());
		order.setScity(shipAddress.getCustCityName());
		order.setSprovince(shipAddress.getCustStateName());
		order.setScountry(shipAddress.getCustCountryName());
		order.setSpostalcode(shipAddress.getCustZipCode());
		
		order.setPhone(orderAddress.getCustPhoneNum());
		order.setFax(orderAddress.getCustFaxNum());
		order.setEmail(orderHeader.getCustEmail());
		order.setTax1(Format.getSimpleFloat(orderEngine.getOrderTaxTotal()));
		order.setShippingTotal(Format.getSimpleFloat(orderEngine.getOrderShippingTotal()));
		order.setSubtotal(Format.getSimpleFloat(orderEngine.getOrderSubTotal()));
		order.setCardAction(CARDACTION_PREAUTH);
		order.setPaymentType(PAYMENT_TYPE);
		order.setCardNumber(creditCardInfo.getCreditCardNum());
		order.setCardExpMonth(creditCardInfo.getCreditCardExpiryMonth());
		if (creditCardInfo.getCreditCardExpiryYear().length() > 2) {
			order.setCardExpYear(creditCardInfo.getCreditCardExpiryYear().substring(2));
		}
		else {
			order.setCardExpYear(creditCardInfo.getCreditCardExpiryYear());
		}
		if (creditCardInfo.getCreditCardVerNum() != null) {
			order.setCardIDNumber(creditCardInfo.getCreditCardVerNum());
		}
		order.setCustomerIP(remoteAddr);
		
		return order;
	}

	public void authorizePayment(OrderHeader orderHeader, HttpServletRequest request)
			throws PaymentException, Exception {
		
		Order order = formatOrder(orderHeader, request);
		order.setCardAction(CARDACTION_PREAUTH);

		String url = SERVICE_URL_SANDBOX;
		if (isProduction()) {
			url = SERVICE_URL_PRODUCTION;
		}
	    Result result = null;
		try {
			result = callWebService(url, order);
		}
		catch (Exception e) {
			logger.error(e);;
			throw new PaymentException(e.getMessage());
		}
		String approved = result.getApproved();
		if (!approved.equals(RESULT_APPROVED)) {
			paymentMessage = result.getErrMsg();
			throw new AuthorizationException(approved);
		}
		String transactionId = result.getReturnCode();
		String ids[] = transactionId.split(":");
		authCode = ids[1];
		paymentReference1 = transactionId;
		paymentReference2 = result.getTransRefNumber();
		paymentReference3 = result.getOrderID();
		paymentReference4 = "";
		
		paymentType = PAYMENT_TYPE;
		
		return;
	}
	
	public void authorizeAndCapturePayment(InvoiceHeader invoiceHeader, HttpServletRequest request) throws PaymentException, Exception {
		OrderHeader orderHeader = invoiceHeader.getOrderHeader();
		Order order = formatOrder(orderHeader, request);
		order.setCardAction(CARDACTION_SALE);

		String url = SERVICE_URL_SANDBOX;
		if (isProduction()) {
			url = SERVICE_URL_PRODUCTION;
		}
	    Result result = null;
		try {
			result = callWebService(url, order);
		}
		catch (Exception e) {
			logger.error(e);;
			throw new PaymentException(e.getMessage());
		}
		String approved = result.getApproved();
		if (!approved.equals(RESULT_APPROVED)) {
			paymentMessage = result.getErrMsg();
			throw new AuthorizationException(approved);
		}
		String transactionId = result.getReturnCode();
		String ids[] = transactionId.split(":");
		authCode = ids[1];
		paymentReference1 = transactionId;
		paymentReference2 = result.getTransRefNumber();
		paymentReference3 = result.getOrderID();
		paymentReference4 = "";
		
		paymentType = PAYMENT_TYPE;
		
		return;
	}

	public void cancelPayment(InvoiceHeader invoiceHeader) throws PaymentException, Exception {

	}

	public void capturePayment(InvoiceHeader invoiceHeader) throws PaymentException, Exception {
		OrderHeader orderHeader = invoiceHeader.getOrderHeader();
		Order order = new Order();
		order.setStoreID(storeId);
		order.setPassphrase(passphrase);
		order.setSubtotal(Format.getSimpleFloat(invoiceHeader.getInvoiceTotal()));
		order.setOrderID(orderHeader.getPaymentTran().getPaymentReference3());
		order.setCardAction(CARDACTION_POSTAUTH);
		order.setPaymentType(PAYMENT_TYPE);

		String url = SERVICE_URL_SANDBOX;
		if (isProduction()) {
			url = SERVICE_URL_PRODUCTION;
		}
	    Result result = null;
		try {
			result = callWebService(url, order);
		}
		catch (Exception e) {
			logger.error(e);;
			throw new PaymentException(e.getMessage());
		}
		String approved = result.getApproved();
		if (!approved.equals(RESULT_APPROVED)) {
			paymentMessage = result.getErrMsg();
			throw new AuthorizationException(approved + " " + result.getErrMsg());
		}
		String transactionId = result.getReturnCode();
		String ids[] = transactionId.split(":");
		authCode = ids[1];
		paymentReference1 = transactionId;
		paymentReference2 = result.getTransRefNumber();
		paymentReference3 = result.getOrderID();
		paymentReference4 = "";
		
		paymentType = PAYMENT_TYPE;
		
		return;
	}
	
	public void voidPayment(InvoiceHeader invoiceHeader) throws AuthorizationException, PaymentException, Exception {
    	OrderHeader orderHeader = invoiceHeader.getOrderHeader();

		Order order = new Order();
		order.setStoreID(storeId);
		order.setPassphrase(passphrase);
		order.setBname(bname);
		PaymentTran payment = orderHeader.getPaymentTran();
		order.setOrderID(payment.getPaymentReference3());
		order.setTransRefNumber(invoiceHeader.getPaymentTran().getPaymentReference2());
		order.setPaymentType(PAYMENT_TYPE);
		order.setCardAction(CARDACTION_VOID);
		
		String url = SERVICE_URL_SANDBOX;
		if (isProduction()) {
			url = SERVICE_URL_PRODUCTION;
		}
	    Result result = null;
		try {
			result = callWebService(url, order);
		}
		catch (Exception e) {
			logger.error(e);;
			throw new PaymentException(e.getMessage());
		}
		String approved = result.getApproved();
		if (!approved.equals(RESULT_APPROVED)) {
			paymentMessage = result.getErrMsg();
			throw new AuthorizationException(approved + " - " + result.getErrMsg());
		}
		String transactionId = result.getReturnCode();
		String ids[] = transactionId.split(":");
		authCode = ids[1];
		paymentReference1 = transactionId;
		paymentReference2 = result.getTransRefNumber();
		paymentReference3 = result.getOrderID();
		paymentType = result.getPaymentType();
	}

	public void creditPayment(CreditHeader creditHeader) throws AuthorizationException, PaymentException, Exception {
		OrderHeader orderHeader = creditHeader.getOrderHeader();
	
		Order order = new Order();
		order.setStoreID(storeId);
		order.setPassphrase(passphrase);
		order.setBname(bname);
		PaymentTran payment = orderHeader.getPaymentTran();
		if (payment == null) {
			for (InvoiceHeader invoiceHeader : orderHeader.getInvoiceHeaders()) {
				payment = invoiceHeader.getPaymentTran();
			}
		}
		order.setOrderID(payment.getPaymentReference3());
		order.setPaymentType(PAYMENT_TYPE);
		order.setCardAction(CARDACTION_CREDIT);
		order.setSubtotal(Format.getSimpleFloat(creditHeader.getCreditTotal()));
	
		String url = SERVICE_URL_SANDBOX;
		if (isProduction()) {
			url = SERVICE_URL_PRODUCTION;
		}
	    Result result = null;
		try {
			result = callWebService(url, order);
		}
		catch (Exception e) {
			logger.error(e);;
			throw new PaymentException(e.getMessage());
		}
		String approved = result.getApproved();
		if (!approved.equals(RESULT_APPROVED)) {
			paymentMessage = result.getErrMsg();
			throw new AuthorizationException(approved + " - " + result.getErrMsg());
		}
		String transactionId = result.getReturnCode();
		String ids[] = transactionId.split(":");
		authCode = ids[1];
		paymentReference1 = transactionId;
		paymentReference2 = result.getTransRefNumber();
		paymentReference3 = result.getOrderID();
		paymentType = result.getPaymentType();
	}

	public void voidCredit(CreditHeader creditHeader)
			throws AuthorizationException, PaymentException, Exception {
		Order order = new Order();
		order.setStoreID(storeId);
		order.setPassphrase(passphrase);
		order.setBname(bname);
		PaymentTran payment = creditHeader.getPaymentTran();
		order.setTransRefNumber(payment.getPaymentReference2());
		order.setOrderID(payment.getPaymentReference3());
		order.setPaymentType(PAYMENT_TYPE);
		order.setCardAction(CARDACTION_VOID);
		
		String url = SERVICE_URL_SANDBOX;
		if (isProduction()) {
			url = SERVICE_URL_PRODUCTION;
		}
	    Result result = null;
		try {
			result = callWebService(url, order);
		}
		catch (Exception e) {
			logger.error(e);;
			throw new PaymentException(e.getMessage());
		}
		String approved = result.getApproved();
		if (!approved.equals(RESULT_APPROVED)) {
			paymentMessage = result.getErrMsg();
			throw new AuthorizationException(approved + " - " + result.getErrMsg());
		}
		String transactionId = result.getReturnCode();
		String ids[] = transactionId.split(":");
		authCode = ids[1];
		paymentReference1 = transactionId;
		paymentReference2 = result.getTransRefNumber();
		paymentReference3 = result.getOrderID();
		paymentType = result.getPaymentType();
	}
	
	public void abort() {
		
	}
	
	private Result callWebService(String url, Order order) throws Exception {
		String orderXml = marhsall(order);
	    if (ApplicationGlobal.isLocalTesting()) {
		    logger.debug("url > " + url);
		    logger.debug("send > " + orderXml);
	    }
		String responseXml = Utility.callWebService(url, orderXml);
	    if (ApplicationGlobal.isLocalTesting()) {
		    logger.debug("receive > " + responseXml);
	    }
		Result result = unmarshall(responseXml);
		return result;
	}
	
	private String marhsall(Order order) throws IOException, MarshalException, ValidationException {
		StringWriter writer = new StringWriter();
    	Marshaller marshaller = xmlContext.createMarshaller();
    	marshaller.setNamespaceMapping("","ns1");
    	marshaller.setWriter(writer);
    	marshaller.marshal(order);
    	return writer.toString();
	}
	
	private Result unmarshall(String response) throws IOException, MarshalException, ValidationException {
    	Unmarshaller unmarshaller = xmlContext.createUnmarshaller();
    	unmarshaller.setClass(Result.class);
    	
    	StringReader stream = new StringReader(response);
    	BufferedReader reader = new BufferedReader(stream);
    	Result result = (Result) unmarshaller.unmarshal(reader);
    	return result;
	}

}
