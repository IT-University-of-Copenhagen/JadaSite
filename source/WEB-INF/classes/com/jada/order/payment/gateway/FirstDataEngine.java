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

import java.io.StringWriter;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import lp.txn.JLinkPointTransaction;

import org.apache.log4j.Logger;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
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
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.PaymentGateway;
import com.jada.jpa.entity.PaymentTran;
import com.jada.jpa.entity.Site;
import com.jada.util.Format;
import com.jada.util.Utility;
import com.jada.xml.firstdata.CreditCard;
import com.jada.xml.firstdata.MerchantInfo;
import com.jada.xml.firstdata.Order;
import com.jada.xml.firstdata.OrderOptions;
import com.jada.xml.firstdata.Payment;
import com.jada.xml.firstdata.TransactionDetails;
import com.jada.xml.paymentGateway.FirstData;

public class FirstDataEngine extends PaymentEngineBase implements PaymentEngine {
	Site site = null;
	static String PAYMENT_TYPE = "CC";
	String firstDataStoreNum = null;
	String firstDataPassword = null;
	String firstDataKeyFile = null;
	String firstDataHostName = null;
	String firstDataHostPort = null;
	
	static Logger logger = Logger.getLogger(FirstDataEngine.class);
	
	public FirstDataEngine(String firstDataStoreNum, String firstDataPassword, String firstDataKeyFile, String firstDataHostName, String firstDataHostPort) {
		this.firstDataStoreNum = firstDataStoreNum;
		this.firstDataPassword = firstDataPassword;
		this.firstDataKeyFile = firstDataKeyFile;
		this.firstDataHostName = firstDataHostName;
		this.firstDataHostPort = firstDataHostPort;
	}
	
	public FirstDataEngine(Site site, Long paymentGatewayId) throws Exception {
		this.site = site;
		PaymentGateway paymentGateway = PaymentGatewayDAO.load(site.getSiteId(), paymentGatewayId);
		FirstData firstData = (FirstData) Utility.joxUnMarshall(FirstData.class, paymentGateway.getPaymentGatewayData());
		firstDataStoreNum = firstData.getFirstDataStoreNum();
		firstDataPassword = firstData.getFirstDataPassword();
		firstDataKeyFile = firstData.getFirstDataKeyFile();
		firstDataHostName = firstData.getFirstDataHostName();
		firstDataHostPort = firstData.getFirstDataHostPort();
	}
	
	public boolean isProvideCustomer() {
		return false;
	}
	
	public boolean isAllowAuthorizeOnly() {
		return true;
	}
	
	public void callBack(HttpServletRequest request, ShoppingCart shoppingCart, ContentBean contentBean) throws PaymentException, Exception {
		return;
	}

	public void cancelPayment(InvoiceHeader invoiceHeader) throws PaymentException, Exception {

	}

	public void authorizePayment(OrderHeader orderHeader, HttpServletRequest request) throws AuthorizationException, PaymentException, Exception {
		CreditCardInfo creditCardInfo = this.getCreditCardInfo();
		
		JLinkPointTransaction transaction = new JLinkPointTransaction();
		transaction.setClientCertificatePath(firstDataKeyFile);
		transaction.setPassword(firstDataPassword);
		transaction.setHost(firstDataHostName);
		transaction.setPort(Integer.parseInt(firstDataHostPort));
		
		Order order = new Order();
		
		MerchantInfo merchantInfo = new MerchantInfo();
		merchantInfo.setConfigFile(firstDataStoreNum);
		order.setMerchantInfo(merchantInfo);
		
		OrderOptions orderOptions = new OrderOptions();
		orderOptions.setOrderType("PREAUTH");
		order.setOrderOptions(orderOptions);
		
		Payment payment = new Payment();
		payment.setChargeTotal(Format.getSimpleFloat(orderHeader.getOrderTotal()));
		order.setPayment(payment);
		
		CreditCard creditCard = new CreditCard();
		creditCard.setCardNumber(creditCardInfo.getCreditCardNum());
		creditCard.setCardExpMonth(creditCardInfo.getCreditCardExpiryMonth());
		creditCard.setCardExpYear(creditCardInfo.getCreditCardExpiryYear().substring(2));
		creditCard.setCvmValue(creditCardInfo.getCreditCardVerNum());
		creditCard.setCvmIndicator("provided");
		order.setCreditCard(creditCard);
		
		Mapping mapping = new Mapping();
		InputSource input = new InputSource(getClass().getResourceAsStream("/com/jada/xml/firstdata/Order.xml"));
		mapping.loadMapping(input);
		
		XMLContext context = new XMLContext();
		context.addMapping(mapping);
		
		StringWriter writer = new StringWriter();
    	Marshaller marshaller = context.createMarshaller();
    	marshaller.setWriter(writer);
    	marshaller.marshal(order);
    	String xmlRequest = writer.toString();
    	String response = null;
    	try {
    		response = transaction.send(xmlRequest);
    	}
    	catch (Exception e) {
    		throw new AuthorizationException(e.getMessage());
    	}
    	
    	String responseCode = getValue(response, "r_message");
    	if (responseCode == null) {
    		throw new AuthorizationException("Invalid response code");
    	}
    	if (!responseCode.equals("APPROVED")) {
    		throw new AuthorizationException(responseCode + " " + getValue(response, "r_authresponse"));
    	}
    	
		authCode = getValue(response, "r_ref");
		paymentReference1 = getValue(response, "r_ordernum");
		paymentReference2 = "";
		paymentReference3 = "";
		paymentReference4 = "";
		paymentType = PAYMENT_TYPE;
		return;
	}
	
	public void authorizeAndCapturePayment(InvoiceHeader invoiceHeader, HttpServletRequest request) throws AuthorizationException, PaymentException, Exception {
		CreditCardInfo creditCardInfo = this.getCreditCardInfo();
		
		JLinkPointTransaction transaction = new JLinkPointTransaction();
		transaction.setClientCertificatePath(firstDataKeyFile);
		transaction.setPassword(firstDataPassword);
		transaction.setHost(firstDataHostName);
		transaction.setPort(Integer.parseInt(firstDataHostPort));
		
		Order order = new Order();
		
		MerchantInfo merchantInfo = new MerchantInfo();
		merchantInfo.setConfigFile(firstDataStoreNum);
		order.setMerchantInfo(merchantInfo);
		
		OrderOptions orderOptions = new OrderOptions();
		orderOptions.setOrderType("SALE");
		order.setOrderOptions(orderOptions);
		
		Payment payment = new Payment();
		payment.setChargeTotal(Format.getSimpleFloat(invoiceHeader.getInvoiceTotal()));
		order.setPayment(payment);
		
		CreditCard creditCard = new CreditCard();
		creditCard.setCardNumber(creditCardInfo.getCreditCardNum());
		creditCard.setCardExpMonth(creditCardInfo.getCreditCardExpiryMonth());
		creditCard.setCardExpYear(creditCardInfo.getCreditCardExpiryYear());
		creditCard.setCvmValue(creditCardInfo.getCreditCardVerNum());
		creditCard.setCvmIndicator("provided");
		order.setCreditCard(creditCard);
		
		Mapping mapping = new Mapping();
		InputSource input = new InputSource(getClass().getResourceAsStream("/com/jada/xml/firstdata/Order.xml"));
		mapping.loadMapping(input);
		
		XMLContext context = new XMLContext();
		context.addMapping(mapping);
		
		StringWriter writer = new StringWriter();
    	Marshaller marshaller = context.createMarshaller();
    	marshaller.setWriter(writer);
    	marshaller.marshal(order);
    	String xmlRequest = writer.toString();
    	String response = null;
    	try {
    		response = transaction.send(xmlRequest);
    	}
    	catch (Exception e) {
    		throw new AuthorizationException(e.getMessage());
    	}
    	
     	String responseCode = getValue(response, "r_message");
    	if (responseCode == null) {
    		throw new AuthorizationException("Invalid response code");
    	}
    	if (!responseCode.equals("APPROVED")) {
    		throw new AuthorizationException(responseCode + " " + getValue(response, "r_authresponse"));
    	}
    	
		authCode = getValue(response, "r_ref");
		paymentReference1 = getValue(response, "r_ordernum");
		paymentReference2 = "";
		paymentReference3 = "";
		paymentReference4 = "";
		paymentType = PAYMENT_TYPE;
		return;
	}
	
	public void capturePayment(InvoiceHeader invoiceHeader) throws AuthorizationException, PaymentException, Exception {
		JLinkPointTransaction transaction = new JLinkPointTransaction();
		transaction.setClientCertificatePath(firstDataKeyFile);
		transaction.setPassword(firstDataPassword);
		transaction.setHost(firstDataHostName);
		transaction.setPort(Integer.parseInt(firstDataHostPort));
		
		Order order = new Order();
		
		MerchantInfo merchantInfo = new MerchantInfo();
		merchantInfo.setConfigFile(firstDataStoreNum);
		order.setMerchantInfo(merchantInfo);
		
		OrderOptions orderOptions = new OrderOptions();
		orderOptions.setOrderType("POSTAUTH");
		order.setOrderOptions(orderOptions);
		
		TransactionDetails transactionDetails = new TransactionDetails();
		PaymentTran paymentTran = invoiceHeader.getOrderHeader().getPaymentTran();
		transactionDetails.setOid(paymentTran.getPaymentReference1());
		order.setTransactionDetails(transactionDetails);
		
		Payment payment = new Payment();
		payment.setChargeTotal(Format.getSimpleFloat(invoiceHeader.getInvoiceTotal()));
		order.setPayment(payment);
		
		Mapping mapping = new Mapping();
		InputSource input = new InputSource(getClass().getResourceAsStream("/com/jada/xml/firstdata/Order.xml"));
		mapping.loadMapping(input);
		
		XMLContext context = new XMLContext();
		context.addMapping(mapping);
		
		StringWriter writer = new StringWriter();
    	Marshaller marshaller = context.createMarshaller();
    	marshaller.setWriter(writer);
    	marshaller.marshal(order);
    	String xmlRequest = writer.toString();
    	String response = null;
    	try {
    		response = transaction.send(xmlRequest);
    	}
    	catch (Exception e) {
    		throw new AuthorizationException(e.getMessage());
    	}
    	
    	String responseCode = getValue(response, "r_approved");
    	if (responseCode == null) {
    		throw new AuthorizationException("Invalid response code");
    	}
    	if (!responseCode.equals("APPROVED")) {
    		throw new AuthorizationException(getValue(response, "r_error"));
    	}
    	
		authCode = getValue(response, "r_ref");
		paymentReference1 = getValue(response, "r_ordernum");
		paymentReference2 = "";
		paymentReference3 = "";
		paymentReference4 = "";
		paymentType = PAYMENT_TYPE;
		return;	
	}
	
	public void voidPayment(InvoiceHeader invoiceHeader) throws AuthorizationException, PaymentException, Exception {
		JLinkPointTransaction transaction = new JLinkPointTransaction();
		transaction.setClientCertificatePath(firstDataKeyFile);
		transaction.setPassword(firstDataPassword);
		transaction.setHost(firstDataHostName);
		transaction.setPort(Integer.parseInt(firstDataHostPort));
		
		Order order = new Order();
		
		MerchantInfo merchantInfo = new MerchantInfo();
		merchantInfo.setConfigFile(firstDataStoreNum);
		order.setMerchantInfo(merchantInfo);
		
		OrderOptions orderOptions = new OrderOptions();
		orderOptions.setOrderType("VOID");
		order.setOrderOptions(orderOptions);
		
		TransactionDetails transactionDetails = new TransactionDetails();
		PaymentTran paymentTran = invoiceHeader.getOrderHeader().getPaymentTran();
		transactionDetails.setOid(paymentTran.getPaymentReference1());
		order.setTransactionDetails(transactionDetails);
		
		Mapping mapping = new Mapping();
		InputSource input = new InputSource(getClass().getResourceAsStream("/com/jada/xml/firstdata/Order.xml"));
		mapping.loadMapping(input);
		
		XMLContext context = new XMLContext();
		context.addMapping(mapping);
		
		StringWriter writer = new StringWriter();
    	Marshaller marshaller = context.createMarshaller();
    	marshaller.setWriter(writer);
    	marshaller.marshal(order);
    	String xmlRequest = writer.toString();
    	String response = null;
    	try {
    		response = transaction.send(xmlRequest);
    	}
    	catch (Exception e) {
    		throw new AuthorizationException(e.getMessage());
    	}
    	
    	String responseCode = getValue(response, "r_approved");
    	if (responseCode == null) {
    		throw new AuthorizationException("Invalid response code");
    	}
    	if (!responseCode.equals("APPROVED")) {
    		throw new AuthorizationException(getValue(response, "r_error"));
    	}
    	
		authCode = getValue(response, "r_ref");
		paymentReference1 = getValue(response, "r_ordernum");
		paymentReference2 = "";
		paymentReference3 = "";
		paymentReference4 = "";
		paymentType = PAYMENT_TYPE;
		return;
	}
	
	public void creditPayment(CreditHeader creditHeader) throws PaymentException, Exception {
		JLinkPointTransaction transaction = new JLinkPointTransaction();
		transaction.setClientCertificatePath(firstDataKeyFile);
		transaction.setPassword(firstDataPassword);
		transaction.setHost(firstDataHostName);
		transaction.setPort(Integer.parseInt(firstDataHostPort));
		
		Order order = new Order();
		
		MerchantInfo merchantInfo = new MerchantInfo();
		merchantInfo.setConfigFile(firstDataStoreNum);
		order.setMerchantInfo(merchantInfo);
		
		OrderOptions orderOptions = new OrderOptions();
		orderOptions.setOrderType("CREDIT");
		order.setOrderOptions(orderOptions);
		
		Payment payment = new Payment();
		payment.setChargeTotal(Format.getSimpleFloat(creditHeader.getCreditTotal()));
		order.setPayment(payment);
		
		TransactionDetails transactionDetails = new TransactionDetails();
		OrderHeader orderHeader = creditHeader.getOrderHeader();
		PaymentTran paymentTran = orderHeader.getPaymentTran();
		if (paymentTran == null) {
			for (InvoiceHeader invoiceHeader : orderHeader.getInvoiceHeaders()) {
				paymentTran = invoiceHeader.getPaymentTran();
			}
		}
		transactionDetails.setOid(paymentTran.getPaymentReference1());
		order.setTransactionDetails(transactionDetails);
		
		Mapping mapping = new Mapping();
		InputSource input = new InputSource(getClass().getResourceAsStream("/com/jada/xml/firstdata/Order.xml"));
		mapping.loadMapping(input);
		
		XMLContext context = new XMLContext();
		context.addMapping(mapping);
		
		StringWriter writer = new StringWriter();
    	Marshaller marshaller = context.createMarshaller();
    	marshaller.setWriter(writer);
    	marshaller.marshal(order);
    	String xmlRequest = writer.toString();
    	String response = null;
    	try {
    		response = transaction.send(xmlRequest);
    	}
    	catch (Exception e) {
    		throw new AuthorizationException(e.getMessage());
    	}
    	
    	String responseCode = getValue(response, "r_approved");
    	if (responseCode == null) {
    		throw new AuthorizationException("Invalid response code");
    	}
    	if (!responseCode.equals("APPROVED")) {
    		throw new AuthorizationException(getValue(response, "r_error"));
    	}
    	
		authCode = getValue(response, "r_ref");
		paymentReference1 = getValue(response, "r_ordernum");
		paymentReference2 = "";
		paymentReference3 = "";
		paymentReference4 = "";
		paymentType = PAYMENT_TYPE;
		return;
	}
	
	public void voidCredit(CreditHeader creditHeader) throws AuthorizationException, PaymentException, Exception {
		paymentMessage = MESSAGE_NOTIMPLEMENTED;
		throw new NotImplementedException("");
	}
	
	public void abort() {
		
	}
	
	public boolean isBookQty() {
		return false;
	}

	public static Vector<?> split(String pattern, String in){
		int s1=0, s2=-1;
		Vector<String> out = new Vector<String>(30);
		while(true){
			s2 = in.indexOf(pattern, s1);
			if(s2 != -1){
				out.addElement(in.substring(s1, s2));
			}else{
				String _ = in.substring(s1);
				if(_ != null && !_.equals("")){
					out.addElement(_);
				}
				break;
			}
			s1 = s2;
			s1 += pattern.length();
		}
		return out;
	}

	public boolean isExtendedTransaction() {
		return false;
	}
	
	public String getValue(String response, String code) {
		int start = response.indexOf("<" + code + ">");
		if (start < 0) {
			return null;
		}
		start += code.length() + 2;
		
		int end = response.indexOf("</" + code + ">");
		if (end < 0) {
			return null;
		}
		return response.substring(start, end);
	}
	
	public static void main(String argc[]) {
		FirstDataEngine engine = new FirstDataEngine("1909143498", "fd123", "/tmp/1909143498.p12", "staging.linkpt.net", "1129");
/*
		CreditCardInfo creditCardInfo = new CreditCardInfo();
		creditCardInfo.setCreditCardNum("5424180279791765");
		creditCardInfo.setCreditCardFullName("JOHN DOE");
		creditCardInfo.setCreditCardExpiryMonth("05");
		creditCardInfo.setCreditCardExpiryYear("12");
		creditCardInfo.setCreditCardVerNum("123");
		engine.setCreditCardInfo(creditCardInfo);
		InvoiceHeader invoiceHeader = new InvoiceHeader();
		invoiceHeader.setInvoiceTotal(15.00F);
		try {
			engine.authorizeAndCapturePayment(invoiceHeader, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
*/

/*
		OrderHeader orderHeader = new OrderHeader();
		PaymentTran paymentTran = new PaymentTran();
		paymentTran.setPaymentReference1("63F76917-4DB32D88-644-98ABE");
		orderHeader.setPaymentTran(paymentTran);
		InvoiceHeader invoiceHeader = new InvoiceHeader();
		invoiceHeader.setInvoiceTotal(13.00F);
		invoiceHeader.setOrderHeader(orderHeader);
		
		try {
			engine.capturePayment(invoiceHeader);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
*/
		
/*
		OrderHeader orderHeader = new OrderHeader();
		PaymentTran paymentTran = new PaymentTran();
		paymentTran.setPaymentReference1("63F76917-4DB32EAF-214-98ABE");
		orderHeader.setPaymentTran(paymentTran);
		InvoiceHeader invoiceHeader = new InvoiceHeader();
		invoiceHeader.setInvoiceTotal(13.00F);
		invoiceHeader.setOrderHeader(orderHeader);
		
		try {
			engine.voidPayment(invoiceHeader);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
*/
		OrderHeader orderHeader = new OrderHeader();
		PaymentTran paymentTran = new PaymentTran();
		paymentTran.setPaymentReference1("63F76917-4DB3320C-096-98ABE");
		orderHeader.setPaymentTran(paymentTran);
		CreditHeader creditHeader = new CreditHeader();
		creditHeader.setCreditTotal(7F);
		creditHeader.setOrderHeader(orderHeader);
		
		try {
			engine.creditPayment(creditHeader);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
