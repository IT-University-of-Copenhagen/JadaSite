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
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.jada.content.ContentBean;
import com.jada.order.cart.ShoppingCart;
import com.jada.order.payment.AuthorizationException;
import com.jada.order.payment.NotImplementedException;
import com.jada.order.payment.PaymentEngine;
import com.jada.order.payment.PaymentEngineBase;
import com.jada.order.payment.PaymentException;
import com.jada.dao.PaymentGatewayDAO;
import com.jada.jpa.entity.CreditHeader;
import com.jada.jpa.entity.InvoiceHeader;
import com.jada.jpa.entity.OrderAddress;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.PaymentGateway;
import com.jada.jpa.entity.PaymentTran;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomain;
import com.jada.system.ApplicationGlobal;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;
import com.jada.xml.paymentGateway.PayPalWebsitePaymentPro;

public class PayPalWebsitePaymentProEngine extends PaymentEngineBase implements PaymentEngine {
	/*
	 * Observations
	 * 1. When doing a transaction, a PayPal fee is applied.
	 * 2. When doing a refund, a PayPal fee is also taken out.  Customer will be short of payment.
	 */
	
	static String PAYPAL_ENVIRONMENT_PRODUCTION = "live";
	static public String PAYMENT_TYPE = "CC";
	
	static String SERVICE_URL_PRODUCTION = "https://api-3t.paypal.com/nvp";
	static String SERVICE_URL_SANDBOX = "https://api-3t.sandbox.paypal.com/nvp";
	
	Site site = null;
	String apiUserName;
	String apiPassword;
	String signature;
	String environment;
	String siteContext;
	String endPoint;
	
	SiteDomain siteDomain = null;
	
	PaymentGateway paymentGateway = null;
	PayPalWebsitePaymentPro paypalWebsitePaymentPro = null;
	
	static Logger logger = Logger.getLogger(PayPalWebsitePaymentProEngine.class);
	
	public PayPalWebsitePaymentProEngine(Site site, Long paymentGatewayId) throws Exception {
		this.site = site;
		paymentGateway = PaymentGatewayDAO.load(site.getSiteId(), paymentGatewayId);
		paypalWebsitePaymentPro = (PayPalWebsitePaymentPro) Utility.joxUnMarshall(PayPalWebsitePaymentPro.class, paymentGateway.getPaymentGatewayData());

		apiUserName = paypalWebsitePaymentPro.getPaypalApiUsername();
		apiPassword = AESEncoder.getInstance().decode(paypalWebsitePaymentPro.getPaypalApiPassword());
		signature = AESEncoder.getInstance().decode(paypalWebsitePaymentPro.getPaypalSignature());
		environment = paypalWebsitePaymentPro.getPaypalEnvironment();
		paymentType = PAYMENT_TYPE;
		
		if (isProduction()) {
			endPoint = SERVICE_URL_PRODUCTION;
		}
		else {
			endPoint = SERVICE_URL_SANDBOX;
		}
	}
	
	public boolean isProvideCustomer() {
		return true;
	}
	
	public boolean isProduction() {
		return PAYPAL_ENVIRONMENT_PRODUCTION.equals(environment);
	}
	
	public boolean isAllowAuthorizeOnly() {
		return true;
	}
	
	private void makePayment(OrderHeader orderHeader, String paymentAction, HttpServletRequest request) throws AuthorizationException, PaymentException, Exception {
		String nvp = "";
		nvp += "VERSION=" + URLEncoder.encode("56.0", Constants.SYSTEM_ENCODING);
		nvp += "&METHOD=DoDirectPayment";
		nvp += "&USER=" + URLEncoder.encode(apiUserName, Constants.SYSTEM_ENCODING);
		nvp += "&PWD=" + URLEncoder.encode(apiPassword, Constants.SYSTEM_ENCODING);
		nvp += "&SIGNATURE=" + URLEncoder.encode(signature, Constants.SYSTEM_ENCODING);
		String remoteAddress = request.getRemoteAddr();
		if (remoteAddress.split(":").length > 4) {
			logger.error("Remote address " + remoteAddress + " seems to be a IPv6 address.  It will likely be rejected by PayPal.");
		}
		nvp += "&IPADDRESS=" + request.getRemoteAddr();
		
		nvp += "&PAYMENTACTION=" + paymentAction;
		nvp += "&CURRENCYCODE=" + orderHeader.getSiteCurrency().getSiteCurrencyClass().getCurrency().getCurrencyCode();
		nvp += "&AMT=" + URLEncoder.encode(Format.getSimpleFloat(orderHeader.getOrderTotal()), Constants.SYSTEM_ENCODING);
		nvp += "&CREDITCARDTYPE=" + getCreditCardType(orderHeader.getCreditCardDesc());
		nvp += "&ACCT=" + creditCardInfo.getCreditCardNum();
		nvp += "&EXPDATE=" + creditCardInfo.getCreditCardExpiryMonth() + creditCardInfo.getCreditCardExpiryYear();
		nvp += "&CVV2=" + creditCardInfo.getCreditCardVerNum();

		OrderAddress billingAddress = orderHeader.getBillingAddress();
		if (billingAddress.getCustUseAddress().equals(Constants.CUST_ADDRESS_USE_CUST)) {
			billingAddress = orderHeader.getCustAddress();
		}
		nvp += "&FIRSTNAME=" + URLEncoder.encode(billingAddress.getCustFirstName(), Constants.SYSTEM_ENCODING);
		nvp += "&LASTNAME=" + URLEncoder.encode(billingAddress.getCustLastName(), Constants.SYSTEM_ENCODING);
		String street = URLEncoder.encode(billingAddress.getCustAddressLine1(), Constants.SYSTEM_ENCODING);
		if (!Format.isNullOrEmpty(billingAddress.getCustAddressLine2())) {
			street += " " + billingAddress.getCustAddressLine2();
		}
		nvp += "&STREET=" + street;
		nvp += "&CITY=" + URLEncoder.encode(billingAddress.getCustCityName(), Constants.SYSTEM_ENCODING);
		nvp += "&STATE=" + URLEncoder.encode(billingAddress.getCustStateCode(), Constants.SYSTEM_ENCODING);
		nvp += "&ZIP=" + URLEncoder.encode(billingAddress.getCustZipCode(), Constants.SYSTEM_ENCODING);
		nvp += "&COUNTRYCODE=" + billingAddress.getCustCountryCode();

		if (ApplicationGlobal.isLocalTesting()) {
			logger.error(nvp);
		}
		String response = sendTransmission(nvp);
		if (ApplicationGlobal.isLocalTesting()) {
			logger.error(response);
		}
		
		NVPResponseParser parser = new NVPResponseParser(response);
		String ack = parser.getValue("ACK");
		if (!ack.equals("Success")) {
			paymentMessage = parser.getValue("L_ERRORCODE0") + ": " + parser.getValue("L_SHORTMESSAGE0") + " - " + parser.getValue("L_LONGMESSAGE0");
			throw new AuthorizationException(paymentMessage);
		}
		
		authCode = parser.getValue("TRANSACTIONID");
		paymentReference1 = parser.getValue("CORRELATIONID");
//		paymentReference2 = "Fee: " + parser.getValue("FEEAMT");
	}
	
	public void authorizePayment(OrderHeader orderHeader, HttpServletRequest request) throws AuthorizationException, PaymentException, Exception {
		makePayment(orderHeader, "Authorization", request);
	}
	
	public void authorizeAndCapturePayment(InvoiceHeader invoiceHeader, HttpServletRequest request) throws AuthorizationException, PaymentException, Exception {
		OrderHeader orderHeader = invoiceHeader.getOrderHeader();
		makePayment(orderHeader, "Sale", request);
	}
	
	public void cancelPayment() throws PaymentException, Exception {
	}
	
	public boolean isBookQty() {
		return true;
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
		String nvp = "";
		nvp += "VERSION=" + URLEncoder.encode("56.0", Constants.SYSTEM_ENCODING);
		nvp += "&METHOD=DoVoid";
		nvp += "&USER=" + URLEncoder.encode(apiUserName, Constants.SYSTEM_ENCODING);
		nvp += "&PWD=" + URLEncoder.encode(apiPassword, Constants.SYSTEM_ENCODING);
		nvp += "&SIGNATURE=" + URLEncoder.encode(signature, Constants.SYSTEM_ENCODING);
		
		PaymentTran payment = invoiceHeader.getOrderHeader().getPaymentTran();
		nvp += "&AUTHORIZATIONID=" + payment.getAuthCode();

		if (ApplicationGlobal.isLocalTesting()) {
			logger.error(nvp);
		}
		String response = sendTransmission(nvp);
		if (ApplicationGlobal.isLocalTesting()) {
			logger.error(response);
		}
		
		NVPResponseParser parser = new NVPResponseParser(response);
		String ack = parser.getValue("ACK");
		if (!ack.equals("Success")) {
			paymentMessage = parser.getValue("L_ERRORCODE0") + ": " + parser.getValue("L_SHORTMESSAGE0") + " - " + parser.getValue("L_LONGMESSAGE0");
			throw new AuthorizationException(paymentMessage);
		}
		
		authCode = "";
		paymentReference1 = parser.getValue("CORRELATIONID");
	}

	public SiteDomain getSiteDomain() {
		return siteDomain;
	}

	public void setSiteDomain(SiteDomain siteDomain) {
		this.siteDomain = siteDomain;
	}

	public void abort() {
		
	}

	public void cancelPayment(InvoiceHeader invoiceHeader) throws PaymentException, Exception {
	}

	public void capturePayment(InvoiceHeader invoiceHeader) throws PaymentException, Exception {
		String nvp = "";
		nvp += "VERSION=" + URLEncoder.encode("56.0", Constants.SYSTEM_ENCODING);
		nvp += "&METHOD=DoCapture";
		nvp += "&USER=" + URLEncoder.encode(apiUserName, Constants.SYSTEM_ENCODING);
		nvp += "&PWD=" + URLEncoder.encode(apiPassword, Constants.SYSTEM_ENCODING);
		nvp += "&SIGNATURE=" + URLEncoder.encode(signature, Constants.SYSTEM_ENCODING);
		
		PaymentTran payment = invoiceHeader.getOrderHeader().getPaymentTran();
		nvp += "&COMPLETETYPE=NOTCOMPLETE";
		nvp += "&AUTHORIZATIONID=" + payment.getAuthCode();
		nvp += "&AMT=" + URLEncoder.encode(Format.getSimpleFloat(invoiceHeader.getInvoiceTotal()), Constants.SYSTEM_ENCODING);

		if (ApplicationGlobal.isLocalTesting()) {
			logger.error(nvp);
		}
		String response = sendTransmission(nvp);
		if (ApplicationGlobal.isLocalTesting()) {
			logger.error(response);
		}
		
		NVPResponseParser parser = new NVPResponseParser(response);
		String ack = parser.getValue("ACK");
		if (!ack.equals("Success")) {
			paymentMessage = parser.getValue("L_ERRORCODE0") + ": " + parser.getValue("L_SHORTMESSAGE0") + " - " + parser.getValue("L_LONGMESSAGE0");
			throw new AuthorizationException(paymentMessage);
		}
		
		authCode = parser.getValue("TRANSACTIONID");
		paymentReference1 = parser.getValue("CORRELATIONID");
		paymentReference2 = "Fee: " + parser.getValue("FEEAMT");
	}
	
	public void creditPayment(CreditHeader creditHeader) throws PaymentException, Exception {
		String nvp = "";
		nvp += "VERSION=" + URLEncoder.encode("56.0", Constants.SYSTEM_ENCODING);
		nvp += "&METHOD=RefundTransaction";
		nvp += "&USER=" + URLEncoder.encode(apiUserName, Constants.SYSTEM_ENCODING);
		nvp += "&PWD=" + URLEncoder.encode(apiPassword, Constants.SYSTEM_ENCODING);
		nvp += "&SIGNATURE=" + URLEncoder.encode(signature, Constants.SYSTEM_ENCODING);
		
		InvoiceHeader invoiceHeader = creditHeader.getInvoiceHeader();
		PaymentTran payment = invoiceHeader.getPaymentTran();
		nvp += "&REFUNDTYPE=Partial";
		nvp += "&TRANSACTIONID=" + payment.getAuthCode();
		nvp += "&AMT=" + URLEncoder.encode(Format.getSimpleFloat(creditHeader.getCreditTotal()), Constants.SYSTEM_ENCODING);

		if (ApplicationGlobal.isLocalTesting()) {
			logger.error(nvp);
		}
		String response = sendTransmission(nvp);
		if (ApplicationGlobal.isLocalTesting()) {
			logger.error(response);
		}
		
		NVPResponseParser parser = new NVPResponseParser(response);
		String ack = parser.getValue("ACK");
		if (!ack.equals("Success")) {
			paymentMessage = parser.getValue("L_ERRORCODE0") + ": " + parser.getValue("L_SHORTMESSAGE0") + " - " + parser.getValue("L_LONGMESSAGE0");
			throw new AuthorizationException(paymentMessage);
		}
		
		authCode = parser.getValue("REFUNDTRANSACTIONID");
		paymentReference1 = parser.getValue("CORRELATIONID");
		paymentReference2 = "Refund fee: " + parser.getValue("FEEREFUNDAMT");
	}
	
	private String getCreditCardType(String creditCardDesc) {
		if (creditCardDesc == null) {
			return null;
		}
		if (creditCardDesc.equals("American Express")) {
			return "Amex";
		}
		if (creditCardDesc.equals("Discover Card")) {
			return "Discover";
		}
		if (creditCardDesc.equals("Master Card")) {
			return "MasterCard";
		}
		if (creditCardDesc.equals("Visa")) {
			return "Visa";
		}
		return null;
	}
	
	public String sendTransmission(String input) throws PaymentException, MalformedURLException {
	    URL url = new URL(endPoint);
	    if (ApplicationGlobal.isLocalTesting()) {
		    logger.debug("url > " + url);
		    logger.debug("send > " + input);
	    }
	    
	    String line = "";
	    try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(90000);
			connection.setReadTimeout(90000);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", "" + input.getBytes().length);
			DataOutputStream out = new DataOutputStream( connection.getOutputStream() );
			out.write(input.getBytes());
			out.flush();
			out.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			line = in.readLine();
			in.close();
	    }
	    catch (Exception e) {
			logger.error(e);
			logger.error("request = " + input);
			logger.error("response = " + line);
			throw new PaymentException(e.getMessage());
	    }
	    if (ApplicationGlobal.isLocalTesting()) {
	    	logger.debug("receive > " + line);
	    }
	    return line;
	}

	public void callBack(HttpServletRequest request, ShoppingCart shoppingCart,
			ContentBean contentBean) throws PaymentException, Exception {
		
	}
	
	private class NVPResponseParser {
		Map<String, String> map = new HashMap<String, String>();
		
		public NVPResponseParser(String input) throws UnsupportedEncodingException {
			String tokens[] = input.split("&");
			for (String token : tokens) {
				String pair[] = token.split("=");
				map.put(pair[0], URLDecoder.decode(pair[1], Constants.SYSTEM_ENCODING));
			}
		}
		
		public String getValue(String key) {
			return map.get(key);
		}
	}

}
