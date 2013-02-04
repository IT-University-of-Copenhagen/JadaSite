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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.jada.order.cart.ShoppingCart;
import com.jada.order.payment.AuthorizationException;
import com.jada.order.payment.CreditCardInfo;
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
import com.jada.system.ApplicationGlobal;
import com.jada.util.AESEncoder;
import com.jada.util.Format;
import com.jada.util.Utility;
import com.jada.xml.paymentGateway.AuthorizeNet;

public class AuthorizeNetEngine extends PaymentEngineBase implements PaymentEngine {
	static String AUTHORIZENET_ENVIRONMENT_PRODUCTION = "live";
	static String SERVICE_URL_PRODUCTION = "https://secure.authorize.net/gateway/transact.dll";
	static String SERVICE_URL_SANDBOX = "https://test.authorize.net/gateway/transact.dll";
	
	static String RESULT_APPROVED = "1";
	static String RESULT_DECLINED = "2";
	static String RESULT_ERROR = "3";
	static String RESULT_HELDFORREVIEW = "4";
	
	
	static String PAYMENT_TYPE = "CC";
	static String CARDACTION_SALE = "AUTH_CAPTURE";
	static String CARDACTION_PREAUTH = "AUTH_ONLY";
	static String CARDACTION_POSTAUTH = "PRIOR_AUTH_CAPTURE";
	static String CARDACTION_CREDIT = "CREDIT";
	static String CARDACTION_FORCED_POSTAUTH = "";
	static String CARDACTION_UNLINKED_CREDIT = "CREDIT";
	static String CARDACTION_VOID = "VOID";
	
	Site site = null;
	String loginId;
	String tranKey;
	String environment;
	
	static Logger logger = Logger.getLogger(AuthorizeNetEngine.class);
	
	public AuthorizeNetEngine(Site site, Long paymentGatewayId) throws Exception {
		this.site = site;
		PaymentGateway paymentGateway = PaymentGatewayDAO.load(site.getSiteId(), paymentGatewayId);
		AuthorizeNet authorizeNet = (AuthorizeNet) Utility.joxUnMarshall(AuthorizeNet.class, paymentGateway.getPaymentGatewayData());
		loginId = authorizeNet.getLoginId();
		tranKey = authorizeNet.getTranKey();
		environment = authorizeNet.getEnvironment();
	}
	
	public boolean isProvideCustomer() {
		return false;
	}
	
	private boolean isProduction() {
		return AUTHORIZENET_ENVIRONMENT_PRODUCTION.equals(environment);
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
		
		StringBuffer sb = new StringBuffer();
		sb.append("x_login=" + loginId + "&");
		sb.append("x_tran_key=" + tranKey + "&");
		sb.append("x_version=3.1&");
		sb.append("x_method=CC&");
		sb.append("x_type=" + CARDACTION_PREAUTH + "&");
		sb.append("x_delim_data=TRUE&");
		sb.append("x_delim_char=|&");
		sb.append("x_relay_response=FALSE&");
		
	    sb.append("x_card_num=" + creditCardInfo.getCreditCardNum() + "&");
	    sb.append("x_card_code=" + creditCardInfo.getCreditCardVerNum() + "&");
	    String expDate = creditCardInfo.getCreditCardExpiryMonth() + "-" + creditCardInfo.getCreditCardExpiryYear();
	    sb.append("x_exp_date=" + expDate + "&");
	    sb.append("x_amount=" + Format.getSimpleFloat(orderHeader.getOrderTotal()) + "&");
	    sb.append("x_currency_code=" + orderHeader.getSiteCurrency().getSiteCurrencyClass().getCurrency().getCurrencyCode() + "&");

	    String line = sendTransmission(sb);
		Vector<?> response = split("|", line);
		String responseCode = (String) response.elementAt(0);
		if (!responseCode.equals(RESULT_APPROVED)) {
			logger.error("request = " + sb.toString());
			logger.error("response = " + line);
			paymentMessage = (String) response.elementAt(3);
			throw new AuthorizationException(responseCode);
		}
		authCode = (String) response.elementAt(4);
		paymentReference1 = (String) response.elementAt(6);
		paymentReference2 = (String) response.elementAt(1);
		paymentReference3 = "";
		paymentReference4 = "";
		paymentType = PAYMENT_TYPE;
		
		return;
	}
	
	public void authorizeAndCapturePayment(InvoiceHeader invoiceHeader, HttpServletRequest request) throws AuthorizationException, PaymentException, Exception {
		CreditCardInfo creditCardInfo = this.getCreditCardInfo();
		
		StringBuffer sb = new StringBuffer();
		sb.append("x_login=" + loginId + "&");
		sb.append("x_tran_key=" + tranKey + "&");
		sb.append("x_version=3.1&");
		sb.append("x_method=CC&");
		sb.append("x_type=" + CARDACTION_SALE + "&");
		sb.append("x_delim_data=TRUE&");
		sb.append("x_delim_char=|&");
		sb.append("x_relay_response=FALSE&");
		
	    sb.append("x_card_num=" + creditCardInfo.getCreditCardNum() + "&");
	    sb.append("x_card_code=" + creditCardInfo.getCreditCardVerNum() + "&");
	    String expDate = creditCardInfo.getCreditCardExpiryMonth() + creditCardInfo.getCreditCardExpiryYear();
	    sb.append("x_exp_date=" + expDate + "&");
	    sb.append("x_amount=" + Format.getSimpleFloat(invoiceHeader.getInvoiceTotal()) + "&");
	    sb.append("x_currency_code=" + invoiceHeader.getOrderHeader().getSiteCurrency().getSiteCurrencyClass().getCurrency().getCurrencyCode() + "&");

	    String line = sendTransmission(sb);
		
		Vector<?> response = split("|", line);
		String responseCode = (String) response.elementAt(0);
		if (!responseCode.equals(RESULT_APPROVED)) {
			logger.error("request = " + sb.toString());
			logger.error("response = " + line);
			paymentMessage = (String) response.elementAt(3);
			throw new AuthorizationException(responseCode);
		}
		authCode = (String) response.elementAt(4);
		paymentReference1 = (String) response.elementAt(6);
		paymentReference2 = (String) response.elementAt(1);
		paymentReference3 = "";
		paymentReference4 = "";
		paymentType = PAYMENT_TYPE;
		
		return;
	}
	
	public void capturePayment(InvoiceHeader invoiceHeader) throws AuthorizationException, PaymentException, Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("x_login=" + loginId + "&");
		sb.append("x_tran_key=" + tranKey + "&");
		sb.append("x_version=3.1&");
		sb.append("x_method=CC&");
		sb.append("x_type=" + CARDACTION_POSTAUTH + "&");
		sb.append("x_delim_data=TRUE&");
		sb.append("x_delim_char=|&");
		sb.append("x_relay_response=FALSE&");
		
		PaymentTran payment = invoiceHeader.getOrderHeader().getPaymentTran();
	    sb.append("x_trans_id=" + payment.getPaymentReference1() + "&");
	    sb.append("x_amount=" + Format.getSimpleFloat(invoiceHeader.getInvoiceTotal()) + "&");

	    String line = sendTransmission(sb);
		
		Vector<?> response = split("|", line);
		String responseCode = (String) response.elementAt(0);
		if (!responseCode.equals(RESULT_APPROVED)) {
			logger.error("request = " + sb.toString());
			logger.error("response = " + line);
			paymentMessage = (String) response.elementAt(3);
			throw new AuthorizationException(responseCode + " - " + paymentMessage);
		}
		authCode = (String) response.elementAt(4);
		paymentReference1 = (String) response.elementAt(6);
		paymentReference2 = (String) response.elementAt(1);
		paymentReference3 = "";
		paymentReference4 = "";
		paymentType = PAYMENT_TYPE;
	}
	
	public void voidPayment(InvoiceHeader invoiceHeader) throws AuthorizationException, PaymentException, Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("x_login=" + loginId + "&");
		sb.append("x_tran_key=" + tranKey + "&");
		sb.append("x_version=3.1&");
		sb.append("x_method=CC&");
		sb.append("x_type=" + CARDACTION_VOID + "&");
		sb.append("x_delim_data=TRUE&");
		sb.append("x_delim_char=|&");
		sb.append("x_relay_response=FALSE&");
		
		PaymentTran payment = invoiceHeader.getPaymentTran();
	    sb.append("x_trans_id=" + payment.getPaymentReference1() + "&");

	    String line = sendTransmission(sb);
		
		Vector<?> response = split("|", line);
		String responseCode = (String) response.elementAt(0);
		if (!responseCode.equals(RESULT_APPROVED)) {
			logger.error("request = " + sb.toString());
			logger.error("response = " + line);
			paymentMessage = (String) response.elementAt(3);
			throw new AuthorizationException(paymentMessage);
		}
		authCode = (String) response.elementAt(4);
		paymentReference1 = (String) response.elementAt(6);
		paymentReference2 = (String) response.elementAt(1);
		paymentReference3 = "";
		paymentReference4 = "";
		
		paymentType = PAYMENT_TYPE;
		
		return;
	}
	
	public void creditPayment(CreditHeader creditHeader) throws PaymentException, Exception {
    	OrderHeader orderHeader = creditHeader.getOrderHeader();
		String custCreditCardNum = AESEncoder.getInstance().decode(orderHeader.getCustCreditCardNum());
	
		StringBuffer sb = new StringBuffer();
		sb.append("x_login=" + loginId + "&");
		sb.append("x_tran_key=" + tranKey + "&");
		sb.append("x_version=3.1&");
		sb.append("x_method=CC&");
		sb.append("x_type=" + CARDACTION_CREDIT + "&");
		sb.append("x_delim_data=TRUE&");
		sb.append("x_delim_char=|&");
		sb.append("x_relay_response=FALSE&");
		
		PaymentTran payment = orderHeader.getPaymentTran();
		if (payment == null) {
			for (InvoiceHeader invoiceHeader : orderHeader.getInvoiceHeaders()) {
				payment = invoiceHeader.getPaymentTran();
			}
		}
	    sb.append("x_trans_id=" + payment.getPaymentReference1() + "&");
	    sb.append("x_card_num=" + custCreditCardNum.substring(custCreditCardNum.length() - 4) + "&");
	    sb.append("x_amount=" + Format.getSimpleFloat(creditHeader.getCreditTotal()) + "&");

	    String line = sendTransmission(sb);
		
		Vector<?> response = split("|", line);
		String responseCode = (String) response.elementAt(0);
		if (!responseCode.equals(RESULT_APPROVED)) {
			logger.error("request = " + sb.toString());
			logger.error("response = " + line);
			paymentMessage = (String) response.elementAt(3);
			throw new AuthorizationException(paymentMessage);
		}
		authCode = (String) response.elementAt(4);
		paymentReference1 = (String) response.elementAt(6);
		paymentReference2 = (String) response.elementAt(1);
		paymentReference3 = "";
		paymentReference4 = "";
		
		paymentType = PAYMENT_TYPE;
	}
	
	public void voidCredit(CreditHeader creditHeader) throws AuthorizationException, PaymentException, Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("x_login=" + loginId + "&");
		sb.append("x_tran_key=" + tranKey + "&");
		sb.append("x_version=3.1&");
		sb.append("x_method=CC&");
		sb.append("x_type=" + CARDACTION_VOID + "&");
		sb.append("x_delim_data=TRUE&");
		sb.append("x_delim_char=|&");
		sb.append("x_relay_response=FALSE&");
		
		PaymentTran payment = creditHeader.getPaymentTran();
	    sb.append("x_trans_id=" + payment.getPaymentReference1() + "&");

	    String line = sendTransmission(sb);
		
		Vector<?> response = split("|", line);
		String responseCode = (String) response.elementAt(0);
		if (!responseCode.equals(RESULT_APPROVED)) {
			logger.error("request = " + sb.toString());
			logger.error("response = " + line);
			paymentMessage = (String) response.elementAt(3);
			throw new AuthorizationException(paymentMessage);
		}
		authCode = (String) response.elementAt(4);
		paymentReference1 = (String) response.elementAt(6);
		paymentReference2 = (String) response.elementAt(1);
		paymentReference3 = "";
		paymentReference4 = "";
		
		paymentType = PAYMENT_TYPE;
	}
	
	public void abort() {
		
	}
	
	public String sendTransmission(StringBuffer sb) throws PaymentException, MalformedURLException {
	    URL url = null;
	    if (isProduction()) {
	    	url = new URL(SERVICE_URL_PRODUCTION);
	    }
	    else {
	    	url = new URL(SERVICE_URL_SANDBOX);
	    }
	    if (ApplicationGlobal.isLocalTesting()) {
		    logger.error("url > " + url);
		    logger.error("send > " + sb.toString());
	    }
	    
	    String line = "";
	    try {
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			DataOutputStream out = new DataOutputStream( connection.getOutputStream() );
			out.write(sb.toString().getBytes());
			out.flush();
			out.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			line = in.readLine();
			in.close();
	    }
	    catch (Exception e) {
			logger.error(e);
			logger.error("request = " + sb.toString());
			logger.error("response = " + line);
			paymentMessage = e.getMessage();
			throw new PaymentException(e.getMessage());
	    }
	    if (ApplicationGlobal.isLocalTesting()) {
	    	logger.error("receive > " + line);
	    }
	    return line;
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
}
