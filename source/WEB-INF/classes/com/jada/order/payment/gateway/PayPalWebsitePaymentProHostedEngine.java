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
import com.jada.content.ContentLookupDispatchAction;
import com.jada.order.cart.ShoppingCart;
import com.jada.order.document.OrderEngine;
import com.jada.order.payment.AuthorizationException;
import com.jada.order.payment.PaymentEngine;
import com.jada.order.payment.PaymentEngineBase;
import com.jada.order.payment.PaymentException;
import com.jada.dao.PaymentGatewayDAO;
import com.jada.jpa.entity.CreditHeader;
import com.jada.jpa.entity.InvoiceHeader;
import com.jada.jpa.entity.OrderAddress;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.PaymentGateway;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.SiteDomainLanguage;
import com.jada.system.ApplicationGlobal;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;
import com.jada.xml.paymentGateway.PayPalWebsitePaymentProHosted;
import com.paypal.soap.api.DoExpressCheckoutPaymentResponseType;
import com.paypal.soap.api.ErrorType;
import com.paypal.soap.api.GetExpressCheckoutDetailsResponseType;

public class PayPalWebsitePaymentProHostedEngine extends PaymentEngineBase implements PaymentEngine {
	static String PAYPAL_ENVIRONMENT_PRODUCTION = "live";
	static public String PAYMENT_TYPE = "PayPal";
	
	Site site = null;
	String secureURLPrefix;
	String bodyBgColor;
	String bodyBgImg;
	String footerTextColor;
	String headerBgColor;
	String headerHeight;
	String logoFont;
	String logoFontColor;
	String logoFontSize;
	String logoImagePosition;
	String logoImage;
	String apiUserName;
	String apiPassword;
	String signature;
	String environment;
	String siteContext;
	String endPoint;
	
	String token = null;
	String payerId = null;
	SiteDomain siteDomain = null;
	
	PaymentGateway paymentGateway = null;
	PayPalWebsitePaymentProHosted payPalWebsitePaymentProHosted = null;
	
	static Logger logger = Logger.getLogger(PayPalWebsitePaymentProEngine.class);
	
	static String SERVICE_URL_PRODUCTION = "https://api-3t.paypal.com/nvp";
	static String SERVICE_URL_SANDBOX = "https://api-3t.sandbox.paypal.com/nvp";
	
	String emailLink;

	public PayPalWebsitePaymentProHostedEngine(Site site, Long paymentGatewayId) throws Exception {
		this.site = site;
		paymentGateway = PaymentGatewayDAO.load(site.getSiteId(), paymentGatewayId);
		payPalWebsitePaymentProHosted = (PayPalWebsitePaymentProHosted) Utility.joxUnMarshall(PayPalWebsitePaymentProHosted.class, paymentGateway.getPaymentGatewayData());

		bodyBgColor = payPalWebsitePaymentProHosted.getBodyBgColor();
		bodyBgImg = payPalWebsitePaymentProHosted.getBodyBgImg();
		footerTextColor = payPalWebsitePaymentProHosted.getFooterTextColor();
		headerBgColor = payPalWebsitePaymentProHosted.getHeaderBgColor();
		headerHeight = payPalWebsitePaymentProHosted.getHeaderHeight();
		logoFont = payPalWebsitePaymentProHosted.getLogoFont();
		logoFontColor = payPalWebsitePaymentProHosted.getLogoFontColor();
		logoFontSize = payPalWebsitePaymentProHosted.getLogoFontSize();
		logoImagePosition = payPalWebsitePaymentProHosted.getLogoImagePosition();

		logoImage = payPalWebsitePaymentProHosted.getLogoImage();
		apiUserName = payPalWebsitePaymentProHosted.getPaypalApiUsername();
		apiPassword = AESEncoder.getInstance().decode(payPalWebsitePaymentProHosted.getPaypalApiPassword());
		signature = AESEncoder.getInstance().decode(payPalWebsitePaymentProHosted.getPaypalSignature());
		environment = payPalWebsitePaymentProHosted.getPaypalEnvironment();
		paymentType = "PayPal";
		
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
	
	public void voidPayment(Long orderHeaderId) throws AuthorizationException, PaymentException, Exception {
	}
	
	public void creditPayment(Long creditHeaderId) throws AuthorizationException, PaymentException, Exception {
	}
	
	public boolean isProduction() {
		return PAYPAL_ENVIRONMENT_PRODUCTION.equals(environment);
	}
	
	public boolean isAllowAuthorizeOnly() {
		return false;
	}
	
	public void payPalAuthorizeAndCapturePayment(OrderHeader orderHeader, HttpServletRequest request) throws AuthorizationException, PaymentException, Exception {
		String nvp = "";
		nvp += "VERSION=" + URLEncoder.encode("65.2", Constants.SYSTEM_ENCODING);
		nvp += "&METHOD=BMCreateButton";
		nvp += "&USER=" + URLEncoder.encode(apiUserName, Constants.SYSTEM_ENCODING);
		nvp += "&PWD=" + URLEncoder.encode(apiPassword, Constants.SYSTEM_ENCODING);
		nvp += "&SIGNATURE=" + URLEncoder.encode(signature, Constants.SYSTEM_ENCODING);
		
		nvp += "&buttoncode=TOKEN";
		nvp += "&buttontype=PAYMENT";
		
		OrderEngine orderEngine = new OrderEngine(orderHeader, null);
		
		nvp += "&amnount=" + URLEncoder.encode(Format.getSimpleFloat(orderHeader.getOrderTotal()), Constants.SYSTEM_ENCODING);
		nvp += "&L_BUTTONVAR0=subtotal=" + URLEncoder.encode(Format.getSimpleFloat(orderHeader.getOrderTotal()), Constants.SYSTEM_ENCODING);
		nvp += "&L_BUTTONVAR1=tax=" + URLEncoder.encode(Format.getSimpleFloat(orderEngine.getOrderTaxTotal()), Constants.SYSTEM_ENCODING);
		nvp += "&L_BUTTONVAR2=shipping=" + URLEncoder.encode(Format.getSimpleFloat(orderEngine.getOrderShippingTotal()), Constants.SYSTEM_ENCODING);
		nvp += "&L_BUTTONVAR4=first_name=" + URLEncoder.encode(orderHeader.getCustAddress().getCustFirstName(), Constants.SYSTEM_ENCODING);
		nvp += "&L_BUTTONVAR5=last_name=" + URLEncoder.encode(orderHeader.getCustAddress().getCustLastName(), Constants.SYSTEM_ENCODING);
		nvp += "&L_BUTTONVAR6=address1=" + URLEncoder.encode(orderHeader.getCustAddress().getCustAddressLine1(), Constants.SYSTEM_ENCODING);
		nvp += "&L_BUTTONVAR7=address2=" + URLEncoder.encode(orderHeader.getCustAddress().getCustAddressLine2(), Constants.SYSTEM_ENCODING);
		nvp += "&L_BUTTONVAR8=city=" + URLEncoder.encode(orderHeader.getCustAddress().getCustCityName(), Constants.SYSTEM_ENCODING);
		nvp += "&L_BUTTONVAR9=state=" + URLEncoder.encode(orderHeader.getCustAddress().getCustStateName(), Constants.SYSTEM_ENCODING);
		nvp += "&L_BUTTONVAR10=zip=" + URLEncoder.encode(orderHeader.getCustAddress().getCustZipCode(), Constants.SYSTEM_ENCODING);
		nvp += "&L_BUTTONVAR11=country=" + URLEncoder.encode(orderHeader.getCustAddress().getCustCountryCode(), Constants.SYSTEM_ENCODING);

		OrderAddress billingAddress = orderHeader.getBillingAddress();
		if (orderHeader.getBillingAddress().getCustUseAddress().equals(Constants.CUST_ADDRESS_USE_CUST)) {
			billingAddress = orderHeader.getCustAddress();
		}
		nvp += "&L_BUTTONVAR12=billing_first_name=" + URLEncoder.encode(billingAddress.getCustFirstName(), Constants.SYSTEM_ENCODING);
		nvp += "&L_BUTTONVAR13=billing_last_name=" + URLEncoder.encode(billingAddress.getCustLastName(), Constants.SYSTEM_ENCODING);
		nvp += "&L_BUTTONVAR14=billing_address1=" + URLEncoder.encode(billingAddress.getCustAddressLine1(), Constants.SYSTEM_ENCODING);
		nvp += "&L_BUTTONVAR15=billing_address2=" + URLEncoder.encode(billingAddress.getCustAddressLine2(), Constants.SYSTEM_ENCODING);
		nvp += "&L_BUTTONVAR16=billing_city=" + URLEncoder.encode(billingAddress.getCustCityName(), Constants.SYSTEM_ENCODING);
		nvp += "&L_BUTTONVAR17=billing_state=" + URLEncoder.encode(billingAddress.getCustStateName(), Constants.SYSTEM_ENCODING);
		nvp += "&L_BUTTONVAR18=billing_zip=" + URLEncoder.encode(billingAddress.getCustZipCode(), Constants.SYSTEM_ENCODING);
		nvp += "&L_BUTTONVAR19=billing_country=" + URLEncoder.encode(billingAddress.getCustCountryCode(), Constants.SYSTEM_ENCODING);
		
		nvp += "&L_BUTTONVAR25=cancel_return=" + "";
		nvp += "&L_BUTTONVAR27=currency_code=" + orderHeader.getSiteCurrency().getSiteCurrencyClass().getCurrency().getCurrencyCode();
		nvp += "&L_BUTTONVAR36=showHostedThankyouPage=false";
//		nvp += "&L_BUTTONVAR39=address_override=true";
		
	
		ContentBean contentBean = ContentLookupDispatchAction.getContentBean(request);
		SiteDomain siteDomain = contentBean.getSiteDomain();
		SiteDomainLanguage siteDomainLanguage = siteDomain.getSiteDomainLanguage();
		String siteName = siteDomainLanguage.getSiteName();
		if (!siteDomainLanguage.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
			for (SiteDomainLanguage language : siteDomain.getSiteDomainLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
					siteDomainLanguage = language;
				}
			}
			if (siteDomainLanguage.getSiteName() != null) {
				siteName = siteDomainLanguage.getSiteName();
			}
		}
		nvp += "&L_BUTTONVAR41=logoText=" + siteName;
		if (!Format.isNullOrEmpty(logoImage)) {
			nvp += "&L_BUTTONVAR42=logoImage=" + logoImage;
		}
		if (!Format.isNullOrEmpty(logoImagePosition)) {
			nvp += "&L_BUTTONVAR43=logoImagePosition=" + logoImagePosition;
		}
		if (!Format.isNullOrEmpty(logoFont)) {
			nvp += "&L_BUTTONVAR44=logoFont=" + logoFont;
		}
		if (!Format.isNullOrEmpty(logoFontSize)) {
			nvp += "&L_BUTTONVAR45=logoFontSize=" + logoFontSize;
		}
		if (!Format.isNullOrEmpty(logoFontColor)) {
			nvp += "&L_BUTTONVAR46=logoFontColor=" + logoFontColor;
		}
		if (!Format.isNullOrEmpty(bodyBgImg)) {
			nvp += "&L_BUTTONVAR47=bodyBgImg=" + bodyBgImg;
		}
		if (!Format.isNullOrEmpty(bodyBgColor)) {
			nvp += "&L_BUTTONVAR48=bodyBgColor=" + bodyBgColor;
		}
		if (!Format.isNullOrEmpty(headerHeight)) {
			nvp += "&L_BUTTONVAR49=headerHeight=" + headerHeight;
		}
		if (!Format.isNullOrEmpty(headerBgColor)) {
			nvp += "&L_BUTTONVAR50=headerBgColor=" + headerBgColor;
		}
		nvp += "&L_BUTTONVAR61=return=" + URLEncoder.encode(Utility.getSecureURLPrefix(siteDomain) + 
			   "/" + ApplicationGlobal.getContextPath() + 
			   "/content/checkout/shoppingCartProcess.do?process=finalizeOrder&prefix=" + siteDomain.getSiteDomainPrefix(), Constants.SYSTEM_ENCODING);

System.out.println(nvp);
		if (ApplicationGlobal.isLocalTesting()) {
			logger.error(nvp);
		}
		String response = sendTransmission(nvp);
System.out.print(response);
		if (ApplicationGlobal.isLocalTesting()) {
			logger.error(response);
		}
		
		NVPResponseParser parser = new NVPResponseParser(response);
		String ack = parser.getValue("ACK");
		if (!ack.equals("Success")) {
			paymentMessage = parser.getValue("L_ERRORCODE0") + ": " + parser.getValue("L_SHORTMESSAGE0") + " - " + parser.getValue("L_LONGMESSAGE0");
			throw new AuthorizationException(paymentMessage);
		}
		
		emailLink = parser.getValue("EMAILLINK");
		paymentReference1 = parser.getValue("CORRELATIONID");
	}
	
	public void authorizePayment(OrderHeader orderHeader, HttpServletRequest request) throws AuthorizationException, PaymentException, Exception {
		authCode = "";
		return;
	}
	
	public void authorizeAndCapturePayment(InvoiceHeader invoiceHeader, HttpServletRequest request) throws AuthorizationException, PaymentException, Exception {
		authCode = "";
		return;
	}
	
	public void callBack(HttpServletRequest request, ShoppingCart shoppingCart, ContentBean contentBean) throws PaymentException, Exception {

        return;
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
	}

	public boolean isExtendedTransaction() {
		return false;
	}

	public void voidCredit(CreditHeader creditHeader)
			throws AuthorizationException, PaymentException, Exception {
		// TODO Auto-generated method stub
		
	}

	public void voidPayment(InvoiceHeader invoiceHeader)
			throws AuthorizationException, PaymentException, Exception {
		// TODO Auto-generated method stub
		
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
	}
	
	public String formatErrorMessage(DoExpressCheckoutPaymentResponseType response) {
		String result = "";
		result += response.getAck().getValue().toString();
		for (ErrorType error : response.getErrors()) {
			result += " - " + error.getShortMessage() + " - " + error.getLongMessage();
		}
		return result;
	}

	public String formatErrorMessage(GetExpressCheckoutDetailsResponseType response) {
		String result = "";
		result += response.getAck().getValue().toString();
		for (ErrorType error : response.getErrors()) {
			result += " - " + error.getShortMessage() + " - " + error.getLongMessage();
		}
		return result;
	}
	
	public void creditPayment(CreditHeader creditHeader)
			throws PaymentException, Exception {
		// TODO Auto-generated method stub
		
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

	public String getEmailLink() {
		return emailLink;
	}

	public void setEmailLink(String emailLink) {
		this.emailLink = emailLink;
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
