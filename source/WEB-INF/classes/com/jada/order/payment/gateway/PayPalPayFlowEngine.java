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
import com.jada.xml.paymentGateway.PayPalPayFlow;

public class PayPalPayFlowEngine extends PaymentEngineBase implements PaymentEngine {
	static String PAYPAL_ENVIRONMENT_PRODUCTION = "live";
	static public String PAYMENT_TYPE = "CC";
	
	static String SERVICE_URL_PRODUCTION = "https://payflowpro.paypal.com";
	static String SERVICE_URL_SANDBOX = "https://pilot-payflowpro.paypal.com";

	Site site = null;
	String secureURLPrefix;
	String user;
	String password;
	String vendor;
	String partner;
	String environment;
	String siteContext;
	String endPoint;
	SiteDomain siteDomain = null;
	
	PaymentGateway paymentGateway = null;
	PayPalPayFlow paypalPayFlow = null;
	
	static Logger logger = Logger.getLogger(PayPalPayFlowEngine.class);
	
	public PayPalPayFlowEngine(Site site, Long paymentGatewayId) throws Exception {
		this.site = site;
		paymentGateway = PaymentGatewayDAO.load(site.getSiteId(), paymentGatewayId);
		paypalPayFlow = (PayPalPayFlow) Utility.joxUnMarshall(PayPalPayFlow.class, paymentGateway.getPaymentGatewayData());

		user = paypalPayFlow.getUser();
		vendor = paypalPayFlow.getVendor();
		partner = paypalPayFlow.getPartner();
		password = AESEncoder.getInstance().decode(paypalPayFlow.getPassword());

		environment = paypalPayFlow.getEnvironment();
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
	
	private void makePayment(OrderHeader orderHeader, String tranType, HttpServletRequest request) throws AuthorizationException, PaymentException, Exception {
		String nvp = "";
		nvp += "USER=" + URLEncoder.encode(user, Constants.SYSTEM_ENCODING);
		nvp += "&PWD=" + URLEncoder.encode(password, Constants.SYSTEM_ENCODING);
		nvp += "&VENDOR=" + URLEncoder.encode(vendor, Constants.SYSTEM_ENCODING);
		nvp += "&PARTNER=" + URLEncoder.encode(partner, Constants.SYSTEM_ENCODING);
		
		nvp += "&TRXTYPE=" + tranType;
		nvp += "&TENDER=C";
		nvp += "&ACCT=" + creditCardInfo.getCreditCardNum();
		nvp += "&EXPDATE=" + creditCardInfo.getCreditCardExpiryMonth() + creditCardInfo.getCreditCardExpiryYear();
		nvp += "&CVV2=" + creditCardInfo.getCreditCardVerNum();
		nvp += "&AMT=" + URLEncoder.encode(Format.getSimpleFloat(orderHeader.getOrderTotal()), Constants.SYSTEM_ENCODING);

		OrderAddress billingAddress = orderHeader.getBillingAddress();
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
		nvp += "&VERBOSITY=MEDIUM";

		if (ApplicationGlobal.isLocalTesting()) {
			logger.error(nvp);
		}
		String response = sendTransmission(nvp);
		if (ApplicationGlobal.isLocalTesting()) {
			logger.error(response);
		}
		
		NVPResponseParser parser = new NVPResponseParser(response);
		String result = parser.getValue("RESULT");
		if (!result.equals("0")) {
			paymentMessage = result + ": " + parser.getValue("RESPMSG");
			throw new AuthorizationException(paymentMessage);
		}
		
		authCode = parser.getValue("AUTHCODE");
		paymentReference1 = parser.getValue("PNREF");
	}
	
	public void authorizePayment(OrderHeader orderHeader, HttpServletRequest request) throws AuthorizationException, PaymentException, Exception {
		makePayment(orderHeader, "A", request);
	}
	
	public void authorizeAndCapturePayment(InvoiceHeader invoiceHeader, HttpServletRequest request) throws AuthorizationException, PaymentException, Exception {
		OrderHeader orderHeader = invoiceHeader.getOrderHeader();
		makePayment(orderHeader, "S", request);
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
		String nvp = "";
		nvp += "USER=" + URLEncoder.encode(user, Constants.SYSTEM_ENCODING);
		nvp += "&PWD=" + URLEncoder.encode(password, Constants.SYSTEM_ENCODING);
		nvp += "&VENDOR=" + URLEncoder.encode(vendor, Constants.SYSTEM_ENCODING);
		nvp += "&PARTNER=" + URLEncoder.encode(partner, Constants.SYSTEM_ENCODING);
		
		nvp += "&TRXTYPE=V";
		PaymentTran payment = creditHeader.getPaymentTran();
		nvp += "&ORIGID=" + payment.getPaymentReference1();

		if (ApplicationGlobal.isLocalTesting()) {
			logger.error(nvp);
		}
		String response = sendTransmission(nvp);
		if (ApplicationGlobal.isLocalTesting()) {
			logger.error(response);
		}
		
		NVPResponseParser parser = new NVPResponseParser(response);
		String result = parser.getValue("RESULT");
		if (!result.equals("0")) {
			paymentMessage = parser.getValue("RESULT") + " - " + parser.getValue("RESPMSG");
			throw new AuthorizationException(paymentMessage);
		}
		
		authCode = "";
		paymentReference1 = parser.getValue("PNREF");
	}

	public void voidPayment(InvoiceHeader invoiceHeader) throws AuthorizationException, PaymentException, Exception {
		String nvp = "";
		nvp += "USER=" + URLEncoder.encode(user, Constants.SYSTEM_ENCODING);
		nvp += "&PWD=" + URLEncoder.encode(password, Constants.SYSTEM_ENCODING);
		nvp += "&VENDOR=" + URLEncoder.encode(vendor, Constants.SYSTEM_ENCODING);
		nvp += "&PARTNER=" + URLEncoder.encode(partner, Constants.SYSTEM_ENCODING);
		
		nvp += "&TRXTYPE=V";
		PaymentTran payment = invoiceHeader.getPaymentTran();
		nvp += "&ORIGID=" + payment.getPaymentReference1();

		if (ApplicationGlobal.isLocalTesting()) {
			logger.error(nvp);
		}
		String response = sendTransmission(nvp);
		if (ApplicationGlobal.isLocalTesting()) {
			logger.error(response);
		}
		
		NVPResponseParser parser = new NVPResponseParser(response);
		String result = parser.getValue("RESULT");
		if (!result.equals("0")) {
			paymentMessage = parser.getValue("RESULT") + " - " + parser.getValue("RESPMSG");
			throw new AuthorizationException(paymentMessage);
		}
		
		authCode = "";
		paymentReference1 = parser.getValue("PNREF");
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
		nvp += "USER=" + URLEncoder.encode(user, Constants.SYSTEM_ENCODING);
		nvp += "&PWD=" + URLEncoder.encode(password, Constants.SYSTEM_ENCODING);
		nvp += "&VENDOR=" + URLEncoder.encode(vendor, Constants.SYSTEM_ENCODING);
		nvp += "&PARTNER=" + URLEncoder.encode(partner, Constants.SYSTEM_ENCODING);
		
		nvp += "&TRXTYPE=D";
		OrderHeader orderHeader = invoiceHeader.getOrderHeader();
		PaymentTran payment = orderHeader.getPaymentTran();
		nvp += "&ORIGID=" + payment.getPaymentReference1();
		nvp += "&AMT=" + URLEncoder.encode(Format.getSimpleFloat(invoiceHeader.getInvoiceTotal()), Constants.SYSTEM_ENCODING);
		nvp += "&CAPTURECOMPLETE=N";

		if (ApplicationGlobal.isLocalTesting()) {
			logger.error(nvp);
		}
		String response = sendTransmission(nvp);
		if (ApplicationGlobal.isLocalTesting()) {
			logger.error(response);
		}
		
		NVPResponseParser parser = new NVPResponseParser(response);
		String result = parser.getValue("RESULT");
		if (!result.equals("0")) {
			paymentMessage = parser.getValue("RESULT") + " - " + parser.getValue("RESPMSG");
			throw new AuthorizationException(paymentMessage);
		}
		
		authCode = parser.getValue("AUTHCODE");
		paymentReference1 = parser.getValue("PNREF");
	}
	
	public void creditPayment(CreditHeader creditHeader) throws PaymentException, Exception {
		String nvp = "";
		nvp += "USER=" + URLEncoder.encode(user, Constants.SYSTEM_ENCODING);
		nvp += "&PWD=" + URLEncoder.encode(password, Constants.SYSTEM_ENCODING);
		nvp += "&VENDOR=" + URLEncoder.encode(vendor, Constants.SYSTEM_ENCODING);
		nvp += "&PARTNER=" + URLEncoder.encode(partner, Constants.SYSTEM_ENCODING);
		
		nvp += "&TRXTYPE=C";
		InvoiceHeader invoiceHeader = creditHeader.getInvoiceHeader();
		PaymentTran payment = invoiceHeader.getPaymentTran();
		nvp += "&ORIGID=" + payment.getPaymentReference1();
		nvp += "&AMT=" + URLEncoder.encode(Format.getSimpleFloat(invoiceHeader.getInvoiceTotal()), Constants.SYSTEM_ENCODING);

		if (ApplicationGlobal.isLocalTesting()) {
			logger.error(nvp);
		}
		String response = sendTransmission(nvp);
		if (ApplicationGlobal.isLocalTesting()) {
			logger.error(response);
		}
		
		NVPResponseParser parser = new NVPResponseParser(response);
		String result = parser.getValue("RESULT");
		if (!result.equals("0")) {
			paymentMessage = parser.getValue("RESULT") + " - " + parser.getValue("RESPMSG");
			throw new AuthorizationException(paymentMessage);
		}
		
		authCode = "";
		paymentReference1 = parser.getValue("PNREF");
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
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
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
