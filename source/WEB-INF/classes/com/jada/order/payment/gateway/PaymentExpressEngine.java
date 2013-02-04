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
import com.jada.order.payment.AuthorizationException;
import com.jada.order.payment.CreditCardInfo;
import com.jada.order.payment.NotImplementedException;
import com.jada.order.payment.PaymentEngine;
import com.jada.order.payment.PaymentEngineBase;
import com.jada.order.payment.PaymentException;
import com.jada.content.ContentBean;
import com.jada.dao.PaymentGatewayDAO;
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
import com.jada.xml.paymentGateway.PaymentExpress;
import com.jada.xml.paymentexpress.TxnRequest;
import com.jada.xml.paymentexpress.TxnResult;

public class PaymentExpressEngine extends PaymentEngineBase implements PaymentEngine {
	static String PAYMENTEXPRESS_ENVIRONMENT_PRODUCTION = "live";
	static String SERVICE_URL_PRODUCTION = "https://sec2.paymentexpress.com/pxpost.aspx";
	static String SERVICE_URL_SANDBOX = "https://sec2.paymentexpress.com/pxpost.aspx";
	
	static String RESULT_APPROVED = "APPROVED";
	
	static String PAYMENT_TYPE = "CC";

	static String CARDACTION_SALE = "Purchase";
	static String CARDACTION_PREAUTH = "Auth";
	static String CARDACTION_POSTAUTH = "Complete";
	static String CARDACTION_CREDIT = "Refund";
	
	Site site = null;
	String postUsername;
	String postPassword;
	String environment;
	
	XMLContext xmlContext = null;
	
	static Logger logger = Logger.getLogger(PaymentExpressEngine.class);
	
	public PaymentExpressEngine(Site site, Long paymentGatewayId) throws Exception {
		this.site = site;
		PaymentGateway paymentGateway = PaymentGatewayDAO.load(site.getSiteId(), paymentGatewayId);
		PaymentExpress paymentExpress = (PaymentExpress) Utility.joxUnMarshall(PaymentExpress.class, paymentGateway.getPaymentGatewayData());

		postUsername = paymentExpress.getPostUsername();
		postPassword = paymentExpress.getPostPassword();
		environment = paymentExpress.getEnvironment();
		
		Mapping mapping = new Mapping();
		InputSource input = new InputSource(getClass().getResourceAsStream("/com/jada/xml/paymentexpress/TxnRequestMapping.xml"));
		mapping.loadMapping(input);
		xmlContext = new XMLContext();
		xmlContext.addMapping(mapping);
		
		input = new InputSource(getClass().getResourceAsStream("/com/jada/xml/paymentexpress/TxnResultMapping.xml"));
		mapping.loadMapping(input);
		xmlContext = new XMLContext();
		xmlContext.addMapping(mapping);		
	}
	
	public boolean isProvideCustomer() {
		return false;
	}
	
	private boolean isProduction() {
		return PAYMENTEXPRESS_ENVIRONMENT_PRODUCTION.equals(environment);
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
	}
	
	public boolean isBookQty() {
		return false;
	}

	public boolean isExtendedTransaction() {
		return false;
	}

	private TxnRequest formatOrder(OrderHeader orderHeader) { 
		CreditCardInfo creditCardInfo = this.getCreditCardInfo();
		
		TxnRequest order = new TxnRequest();
		order.setPostUsername(postUsername);
		order.setPostPassword(postPassword);		
		order.setInputCurrency(orderHeader.getSiteCurrency().getSiteCurrencyClass().getSiteCurrencyClassName());
//		order.setTxnId(orderHeader.getOrderNum());
		order.setTxnId(orderHeader.getOrderHeaderId().toString());		
		OrderAddress orderAddress = orderHeader.getCustAddress();
		if (orderHeader.getBillingAddress().getCustUseAddress().equals(Constants.CUST_ADDRESS_USE_OWN)) {
			orderAddress = orderHeader.getBillingAddress();
		}
		order.setCardHolderName(orderAddress.getCustFirstName() + " " + orderAddress.getCustLastName());
//		order.setAmount(Format.getSimpleFloat(orderEngine.getOrderPriceTotal()));
		order.setAmount(Format.getSimpleFloat(orderHeader.getOrderTotal()));
		order.setCardNumber(creditCardInfo.getCreditCardNum());
		String expiryMonth = (creditCardInfo.getCreditCardExpiryMonth());
		String expiryYear = (creditCardInfo.getCreditCardExpiryYear());
		if (expiryYear.length() > 2) {
			expiryYear = expiryYear.substring(2);
		}
		order.setDateExpiry(expiryMonth + expiryYear);
		if (creditCardInfo.getCreditCardVerNum() != null) {
			order.setCvc2(creditCardInfo.getCreditCardVerNum());
		}
		return order;
	}

	public void authorizePayment(OrderHeader orderHeader, HttpServletRequest request) throws PaymentException, Exception {
		paymentMessage = MESSAGE_NOTIMPLEMENTED;
		throw new NotImplementedException("");
	}
	
	public void capturePayment(InvoiceHeader invoiceHeader) throws PaymentException, Exception {
		paymentMessage = MESSAGE_NOTIMPLEMENTED;
		throw new NotImplementedException("");
	}

	public void authorizeAndCapturePayment(InvoiceHeader invoiceHeader, HttpServletRequest request) throws PaymentException, Exception {
		OrderHeader orderHeader = invoiceHeader.getOrderHeader();
		TxnRequest order = formatOrder(orderHeader);
		order.setTxnType(CARDACTION_SALE);

		String url = SERVICE_URL_SANDBOX;
		if (isProduction()) {
			url = SERVICE_URL_PRODUCTION;
		}
	    TxnResult result = null;
		try {
			result = callWebService(url, order);
		}
		catch (Exception e) {
			logger.error(e);;
			throw new PaymentException(e.getMessage());
		}
		String approved = result.getResponseText();
		if (!approved.equals(RESULT_APPROVED)) {
			paymentMessage = result.getReCo() + ": " + approved;
			throw new AuthorizationException(paymentMessage);
		}
		if (result.getTransaction() != null) authCode = result.getTransaction()[0].getAuthCode();
		paymentReference1 = result.getDpsTxnRef();
		paymentReference2 = result.getTxnRef();		
		paymentType = PAYMENT_TYPE;
		
		return;
	}

	public void cancelPayment(InvoiceHeader invoiceHeader) throws PaymentException, Exception {
	}

	public void voidPayment(InvoiceHeader invoiceHeader) throws AuthorizationException, PaymentException, Exception {
		throw new NotImplementedException("");
	}

	public void creditPayment(CreditHeader creditHeader) throws AuthorizationException, PaymentException, Exception {
		OrderHeader orderHeader = creditHeader.getOrderHeader();

		TxnRequest order = new TxnRequest();
		order.setPostUsername(postUsername);
		order.setPostPassword(postPassword);	
		order.setInputCurrency(orderHeader.getSiteCurrency().getSiteCurrencyClass().getSiteCurrencyClassName());
		order.setAmount(Format.getSimpleFloat(creditHeader.getCreditTotal()));

		PaymentTran payment = orderHeader.getPaymentTran();
		if (payment == null) {
			for (InvoiceHeader invoiceHeader : orderHeader.getInvoiceHeaders()) {
				payment = invoiceHeader.getPaymentTran();
			}
		}
		order.setDpsTxnRef(payment.getPaymentReference1());
		order.setTxnId(payment.getPaymentReference2());
		order.setTxnType(CARDACTION_CREDIT);

		String url = SERVICE_URL_SANDBOX;
		if (isProduction()) {
			url = SERVICE_URL_PRODUCTION;
		}
	    TxnResult result = null;
		try {
			result = callWebService(url, order);
		}
		catch (Exception e) {
			logger.error(e);;
			throw new PaymentException(e.getMessage());
		}
		String approved = result.getResponseText();
		if (!approved.equals(RESULT_APPROVED)) {
			paymentMessage = result.getReCo() + ": " + approved;
			throw new AuthorizationException(paymentMessage);
		}
		if (result.getTransaction() != null) authCode = result.getTransaction()[0].getAuthCode();
		paymentReference1 = result.getDpsTxnRef();
		paymentReference2 = result.getTxnRef();		
		paymentType = PAYMENT_TYPE;
	}

	public void voidCredit(CreditHeader creditHeader) throws AuthorizationException, PaymentException, Exception {
		throw new NotImplementedException("");
	}
	
	public void abort() {
	}
	
	private TxnResult callWebService(String url, TxnRequest order) throws Exception {
		String txnRequestXml = marhsall(order);
	    if (ApplicationGlobal.isLocalTesting()) {
		    logger.debug("url > " + url);
		    logger.debug("send > " + txnRequestXml);
	    }
		String responseXml = Utility.callWebService(url, txnRequestXml);
	    if (ApplicationGlobal.isLocalTesting()) {
		    logger.debug("receive > " + responseXml);
	    }
		TxnResult result = unmarshall(responseXml);
		return result;
	}
	
	private String marhsall(TxnRequest order) throws IOException, MarshalException, ValidationException {
		StringWriter writer = new StringWriter();
    	Marshaller marshaller = xmlContext.createMarshaller();
    	marshaller.setNamespaceMapping("","ns1");
    	marshaller.setWriter(writer);
    	marshaller.marshal(order);
    	return writer.toString();
	}
	
	private TxnResult unmarshall(String response) throws IOException, MarshalException, ValidationException {
    	Unmarshaller unmarshaller = xmlContext.createUnmarshaller();
    	unmarshaller.setClass(TxnResult.class);
    	
    	StringReader stream = new StringReader(response);
    	BufferedReader reader = new BufferedReader(stream);
    	TxnResult result = (TxnResult) unmarshaller.unmarshal(reader);
    	return result;
	}

}
