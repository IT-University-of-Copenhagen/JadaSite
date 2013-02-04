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

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.content.ContentAction;
import com.jada.content.ContentBean;
import com.jada.content.ContentLookupDispatchAction;
import com.jada.order.cart.ShoppingCart;
import com.jada.order.document.OrderEngine;
import com.jada.order.payment.AuthorizationException;
import com.jada.order.payment.PaymentCustomerException;
import com.jada.order.payment.PaymentEngine;
import com.jada.order.payment.PaymentEngineBase;
import com.jada.order.payment.PaymentException;
import com.jada.dao.PaymentGatewayDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.CreditHeader;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.CustomerAddress;
import com.jada.jpa.entity.CustomerClass;
import com.jada.jpa.entity.InvoiceHeader;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.PaymentGateway;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.State;
import com.jada.system.ApplicationGlobal;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;
import com.jada.xml.paymentGateway.PayPalExpressCheckOut;
import com.paypal.sdk.exceptions.PayPalException;
import com.paypal.sdk.profiles.APIProfile;
import com.paypal.sdk.profiles.ProfileFactory;
import com.paypal.sdk.services.CallerServices;
import com.paypal.soap.api.AckCodeType;
import com.paypal.soap.api.BasicAmountType;
import com.paypal.soap.api.CurrencyCodeType;
import com.paypal.soap.api.DoExpressCheckoutPaymentRequestDetailsType;
import com.paypal.soap.api.DoExpressCheckoutPaymentRequestType;
import com.paypal.soap.api.DoExpressCheckoutPaymentResponseType;
import com.paypal.soap.api.ErrorType;
import com.paypal.soap.api.GetExpressCheckoutDetailsRequestType;
import com.paypal.soap.api.GetExpressCheckoutDetailsResponseDetailsType;
import com.paypal.soap.api.GetExpressCheckoutDetailsResponseType;
import com.paypal.soap.api.PayerInfoType;
import com.paypal.soap.api.PaymentActionCodeType;
import com.paypal.soap.api.PaymentDetailsType;
import com.paypal.soap.api.SetExpressCheckoutRequestDetailsType;
import com.paypal.soap.api.SetExpressCheckoutRequestType;
import com.paypal.soap.api.SetExpressCheckoutResponseType;

public class PayPalEngine extends PaymentEngineBase implements PaymentEngine {
	static String PAYPAL_ENVIRONMENT_PRODUCTION = "live";
	static public String PAYMENT_TYPE = "PayPal";
	
	Site site = null;
	String secureURLPrefix;
	String apiUserName;
	String apiPassword;
	String signature;
	String environment;
	String siteContext;
	float extraVerificationPercent;
	float extraVerificationAmount;
	
	String token = null;
	String payerId = null;
	SiteDomain siteDomain = null;
	
	PaymentGateway paymentGateway = null;
	PayPalExpressCheckOut payPalExpressCheckOut = null;
	
	// TODO trap all com.paypal.sdk.exceptions.PayPalException and handle it.
	public PayPalEngine(Site site, Long paymentGatewayId) throws Exception {
		this.site = site;
		paymentGateway = PaymentGatewayDAO.load(site.getSiteId(), paymentGatewayId);
		payPalExpressCheckOut = (PayPalExpressCheckOut) Utility.joxUnMarshall(PayPalExpressCheckOut.class, paymentGateway.getPaymentGatewayData());

		apiUserName = payPalExpressCheckOut.getPaymentPaypalApiUsername();
		apiPassword = AESEncoder.getInstance().decode(payPalExpressCheckOut.getPaymentPaypalApiPassword());
		signature = AESEncoder.getInstance().decode(payPalExpressCheckOut.getPaymentPaypalSignature());
		environment = payPalExpressCheckOut.getPaymentPaypalEnvironment();
		extraVerificationAmount = (float) payPalExpressCheckOut.getPaymentPaypalExtraAmount();
		paymentType = "PayPal";
	}
	
	public boolean isProvideCustomer() {
		return true;
	}
	
	public void voidPayment(Long orderHeaderId) throws AuthorizationException, PaymentException, Exception {
	}
	
	public void creditPayment(Long creditHeaderId) throws AuthorizationException, PaymentException, Exception {
	}

	private APIProfile createProfile() throws PayPalException {
		APIProfile profile = ProfileFactory.createSignatureAPIProfile();
    	profile.setAPIUsername(apiUserName);
    	profile.setAPIPassword(apiPassword);
    	profile.setSignature(signature);
    	profile.setEnvironment(environment);
    	return profile;
	}
	
	public boolean isProduction() {
		return PAYPAL_ENVIRONMENT_PRODUCTION.equals(environment);
	}
	
	public boolean isAllowAuthorizeOnly() {
		return false;
	}
	
	public void payPalAuthorizePayment(OrderHeader orderHeader, HttpServletRequest request) throws AuthorizationException, PaymentException, Exception {
		CallerServices caller = new CallerServices();
    	caller.setAPIProfile(createProfile());
		
		SetExpressCheckoutRequestType requestHeader = new SetExpressCheckoutRequestType();
		SetExpressCheckoutRequestDetailsType requestDetail = new SetExpressCheckoutRequestDetailsType();
		BasicAmountType ppAmount = new BasicAmountType();
		ppAmount.setCurrencyID(CurrencyCodeType.fromString(orderHeader.getSiteCurrency().getSiteCurrencyClass().getCurrency().getCurrencyCode()));
		float verificationTotal = orderHeader.getOrderTotal() * (extraVerificationPercent + 100) / 100 + extraVerificationAmount;
		ppAmount.set_value(Format.getSimpleFloat(verificationTotal));
		requestDetail.setOrderTotal(ppAmount);
		requestDetail.setPaymentAction(PaymentActionCodeType.fromString("Authorization"));

		ContentBean contentBean = ContentLookupDispatchAction.getContentBean(request);
		requestDetail.setReturnURL(Utility.getSecureURLPrefix(siteDomain) + 
								   "/" + ApplicationGlobal.getContextPath() + 
								   "/content/checkout/shoppingCartProcess.do" + 
								   "?process=payPalCallBack" + 
								   "&prefix=" + contentBean.getSiteDomain().getSiteDomainPrefix()
								   );
		requestDetail.setCancelURL(Utility.getSecureURLPrefix(siteDomain) + 
								   "/" + ApplicationGlobal.getContextPath() + 
								   "/content/checkout/shoppingCartCancelCheckout.do?" +
								   "process=cancel" +
								   "&prefix=" + contentBean.getSiteDomain().getSiteDomainPrefix()
								   );
		
		// TODO Park - more customer information
		requestDetail.setOrderDescription("");
		requestDetail.setCustom("");
		
		requestHeader.setSetExpressCheckoutRequestDetails(requestDetail);

		SetExpressCheckoutResponseType response = (SetExpressCheckoutResponseType) caller.call("SetExpressCheckout", requestHeader);
		if (!response.getAck().equals(AckCodeType.Success)) {
			throw new PaymentException(response.getAck().getValue().toString());
		}
		token = response.getToken();
	}
	
	public void authorizePayment(OrderHeader orderHeader, HttpServletRequest request) throws AuthorizationException, PaymentException, Exception {
		authCode = "";
		paymentReference1 = payerId;
		paymentReference2 = token;
		return;
	}
	
	public void authorizeAndCapturePayment(InvoiceHeader invoiceHeader, HttpServletRequest request) throws AuthorizationException, PaymentException, Exception {
		capturePayment(invoiceHeader);
	}
	
	public void callBack(HttpServletRequest request, ShoppingCart shoppingCart, ContentBean contentBean) throws PaymentException, Exception {
		CallerServices caller = new CallerServices();
    	caller.setAPIProfile(createProfile());

    	GetExpressCheckoutDetailsRequestType checkoutRequest = new GetExpressCheckoutDetailsRequestType();
    	checkoutRequest.setToken(token);
		
		GetExpressCheckoutDetailsResponseType responseHeader = (GetExpressCheckoutDetailsResponseType) caller.call("GetExpressCheckoutDetails", checkoutRequest);
		if (!responseHeader.getAck().equals(AckCodeType.Success)) {
			throw new PaymentException(formatErrorMessage(responseHeader));
		}

		GetExpressCheckoutDetailsResponseDetailsType responseDetail = responseHeader.getGetExpressCheckoutDetailsResponseDetails();
		PayerInfoType payer = responseDetail.getPayerInfo();
		
		String emailAddress = payer.getPayer().trim();
		
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();

        Customer customer = null;
        CustomerAddress customerAddress = null;
        CustomerAddress billingAddress = null;
        CustomerAddress shippingAddress = null;

        String sql = "from Customer customer where custEmail = :custEmail";
        Query query = em.createQuery(sql);
        query.setParameter("custEmail", emailAddress);
        List<?> list = query.getResultList();
        if (list.size() > 0) {
        	customer = (Customer) list.iterator().next();
        	if (!customer.getActive().equals(Constants.VALUE_YES)) {
        		throw new PaymentCustomerException("Customer suspended");
        	}
        }
        
        if (customer == null) {
        	customer = new Customer();
    		CustomerClass customerClass = (CustomerClass) em.find(CustomerClass.class, payPalExpressCheckOut.getPaymentPaypalCustClassId());
    		customer.setCustomerClass(customerClass);
        	customer.setActive(Constants.VALUE_YES);
            customer.setRecCreateBy(Constants.USERNAME_CUSTOMER);
            customer.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        }
        else {
        	customerAddress = customer.getCustAddress();
        	for (CustomerAddress address : customer.getCustAddresses()) {
        		if (address.getCustAddressType().equals(Constants.CUSTOMER_ADDRESS_BILLING)) {
        			billingAddress = address;
        			continue;
        		}
        		if (address.getCustAddressType().equals(Constants.CUSTOMER_ADDRESS_SHIPPING)) {
        			shippingAddress = address;
        			continue;
        		}
        	}
        }
        
        if (customerAddress == null) {
        	customerAddress = new CustomerAddress();
        	customerAddress.setCustUseAddress(Constants.CUST_ADDRESS_USE_OWN);
        	customerAddress.setCustAddressType(Constants.CUSTOMER_ADDRESS_CUST);
        	customerAddress.setRecCreateBy(Constants.USERNAME_CUSTOMER);
        	customerAddress.setRecCreateDatetime(new Date(System.currentTimeMillis()));
            customer.getCustAddresses().add(customerAddress);
            customer.setCustAddress(customerAddress);
        }
        if (billingAddress == null) {
            billingAddress = new CustomerAddress();
            billingAddress.setCustUseAddress(Constants.CUST_ADDRESS_USE_CUST);
            billingAddress.setCustAddressType(Constants.CUSTOMER_ADDRESS_BILLING);
            billingAddress.setRecCreateBy(Constants.USERNAME_CUSTOMER);
            billingAddress.setRecCreateDatetime(new Date(System.currentTimeMillis()));
            customer.getCustAddresses().add(billingAddress);
        }
        if (shippingAddress == null) {
            shippingAddress = new CustomerAddress();
            shippingAddress.setCustAddressType(Constants.CUSTOMER_ADDRESS_SHIPPING);
            shippingAddress.setCustUseAddress(Constants.CUST_ADDRESS_USE_CUST);
            shippingAddress.setRecCreateBy(Constants.USERNAME_CUSTOMER);
            shippingAddress.setRecCreateDatetime(new Date(System.currentTimeMillis()));
            customer.getCustAddresses().add(shippingAddress);
        }
        
        billingAddress.setCustUseAddress(Constants.CUST_ADDRESS_USE_CUST);
        billingAddress.setCustAddressType(Constants.CUSTOMER_ADDRESS_BILLING);
        billingAddress.setRecUpdateBy(Constants.USERNAME_CUSTOMER);
        billingAddress.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        shoppingCart.setBillingAddress(billingAddress);
        if (billingAddress.getCustAddressId() == null) {
        	em.persist(billingAddress);
        }
        shippingAddress.setCustUseAddress(Constants.CUST_ADDRESS_USE_CUST);
        shippingAddress.setCustAddressType(Constants.CUSTOMER_ADDRESS_SHIPPING);
        shippingAddress.setRecUpdateBy(Constants.USERNAME_CUSTOMER);
        shippingAddress.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        shoppingCart.setShippingAddress(shippingAddress);
        if (shippingAddress.getCustAddressId() == null) {
        	em.persist(shippingAddress);
        }
        
        String stateName = Format.getString(payer.getAddress().getStateOrProvince());
        State state = Utility.getStateByNameOrCode(site.getSiteId(), stateName);
        String countryCode = Format.getString(payer.getAddress().getCountry().toString());
        Country country = Utility.getCountryByCode(site.getSiteId(), countryCode);

        customer.setSite(site);
        customer.setCustPublicName("");
        customer.setCustEmail(emailAddress);
        customer.setCustSource(Constants.CUSTOMER_SOURCE_PAYPAL);
        customer.setCustSourceRef(Format.getString(payer.getPayerID()));
        customer.setRecUpdateBy(Constants.USERNAME_CUSTOMER);
        customer.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
//        customer.setCustPublicName(customer.getCustEmail());
        char singleCheckout = contentBean.getSiteDomain().getSite().getSingleCheckout();
        if (singleCheckout == Constants.VALUE_YES) {
        	customer.setSiteDomain(contentBean.getSiteDomain().getSite().getSiteDomainDefault());
        }
        else {
        	customer.setSiteDomain(contentBean.getSiteDomain());
        }
        
        customerAddress.setCustUseAddress(Constants.CUST_ADDRESS_USE_OWN);
        customerAddress.setCustAddressType(Constants.CUSTOMER_ADDRESS_CUST);
        customerAddress.setCustPrefix(Format.getString(payer.getPayerName().getSalutation()));
        customerAddress.setCustFirstName(Format.getString(payer.getPayerName().getFirstName()));
        customerAddress.setCustMiddleName(Format.getString(payer.getPayerName().getMiddleName()));
        customerAddress.setCustLastName(Format.getString(payer.getPayerName().getLastName()));
        customerAddress.setCustSuffix(Format.getString(payer.getPayerName().getSuffix()));
        customerAddress.setCustAddressLine1(payer.getAddress().getStreet1());
        customerAddress.setCustAddressLine2(payer.getAddress().getStreet2());
        customerAddress.setCustCityName(payer.getAddress().getCityName());
        customerAddress.setCustStateCode(state.getStateCode());
        customerAddress.setCustStateName(state.getStateName());
        customerAddress.setCustCountryName(country.getCountryName());
        customerAddress.setCustCountryCode(country.getCountryCode());
        customerAddress.setCustZipCode("");
        customerAddress.setCustPhoneNum(Format.getString(payer.getContactPhone()));
        customerAddress.setCustFaxNum("");
        // TODO what if state and country and null
        customerAddress.setState(state);
        customerAddress.setCountry(country);
        customerAddress.setRecUpdateBy(Constants.USERNAME_CUSTOMER);
        customerAddress.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        em.persist(customerAddress);
        
        em.persist(customer);
        shoppingCart.initCustomer(customer, contentBean);
        
        // Automatically sign-in after Pay Pal confirm
        ContentAction.setCustId(request, customer.getCustId());
        
        payerId = Format.getString(payer.getPayerID());
        return;
	}
/*
	private void formatCustomerAddress(CustomerAddress customerAddress, PayerInfoType payer) throws Exception {
        String stateName = Format.getString(payer.getAddress().getStateOrProvince());
        State state = Utility.getStateByName(site.getSiteId(), stateName);

        String countryCode = Format.getString(payer.getAddress().getCountry().toString());
        Country country = Utility.getCountryByCode(site.getSiteId(), countryCode);
        
        String emailAddress = payer.getPayer().trim();

        customerAddress.setCustPrefix(Format.getString(payer.getPayerName().getSalutation()));
        customerAddress.setCustFirstName(Format.getString(payer.getPayerName().getFirstName()));
        customerAddress.setCustMiddleName(Format.getString(payer.getPayerName().getMiddleName()));
        customerAddress.setCustLastName(Format.getString(payer.getPayerName().getLastName()));
        customerAddress.setCustSuffix(Format.getString(payer.getPayerName().getSuffix()));
        customerAddress.setCustAddressLine1(Format.getString(payer.getAddress().getStreet1()));
        customerAddress.setCustAddressLine2(Format.getString(payer.getAddress().getStreet2()));
        customerAddress.setCustCityName(Format.getString(payer.getAddress().getCityName()));
        customerAddress.setCustStateName(Format.getString(payer.getAddress().getStateOrProvince()));
        customerAddress.setCustStateCode(state.getStateCode());
        customerAddress.setCustCountryName(Format.getString(payer.getAddress().getCountryName()));
        customerAddress.setCustCountryCode(countryCode);
        customerAddress.setCustZipCode(Format.getString(payer.getAddress().getPostalCode()));
        customerAddress.setCustPhoneNum(Format.getString(payer.getContactPhone()));
        customerAddress.setCustFaxNum("");
        customerAddress.setState(state);
        customerAddress.setCountry(country);
        customerAddress.setRecUpdateBy(emailAddress);
        customerAddress.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
	}
*/
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
		// TODO Auto-generated method stub
		
	}

	public void capturePayment(InvoiceHeader invoiceHeader)
			throws PaymentException, Exception {
		OrderHeader orderHeader = invoiceHeader.getOrderHeader();
		CallerServices caller = new CallerServices();
    	caller.setAPIProfile(createProfile());
    	String currencyCode = orderHeader.getSiteCurrency().getSiteCurrencyClass().getCurrency().getCurrencyCode();
    	OrderEngine orderEngine = new OrderEngine(orderHeader, null);

    	DoExpressCheckoutPaymentRequestType request = new DoExpressCheckoutPaymentRequestType();
    	
    	DoExpressCheckoutPaymentRequestDetailsType detail = new DoExpressCheckoutPaymentRequestDetailsType();
    	detail.setPayerID(payerId);
    	detail.setToken(token);
    	detail.setPaymentAction(PaymentActionCodeType.fromString("Sale"));
    
    	PaymentDetailsType paymentDetail = new PaymentDetailsType();
    	BasicAmountType orderTotal = new BasicAmountType();
    	orderTotal.setCurrencyID(CurrencyCodeType.fromString(currencyCode));
    	orderTotal.set_value(Format.getSimpleFloat(orderHeader.getOrderTotal()));
    	paymentDetail.setOrderTotal(orderTotal);

    	BasicAmountType priceTotal = new BasicAmountType();
    	priceTotal.setCurrencyID(CurrencyCodeType.fromString(currencyCode));
    	priceTotal.set_value(Format.getSimpleFloat(orderEngine.getOrderPriceTotal() - orderEngine.getOrderPriceDiscountTotal()));
    	paymentDetail.setItemTotal(priceTotal);

    	BasicAmountType taxTotal = new BasicAmountType();
    	taxTotal.setCurrencyID(CurrencyCodeType.fromString(currencyCode));
    	taxTotal.set_value(Format.getSimpleFloat(orderEngine.getOrderTaxTotal()));
    	paymentDetail.setTaxTotal(taxTotal);

    	float total = orderHeader.getShippingTotal() - orderHeader.getShippingDiscountTotal();
    	BasicAmountType shippingTotal = new BasicAmountType();
    	shippingTotal.setCurrencyID(CurrencyCodeType.fromString(currencyCode));
    	shippingTotal.set_value(Format.getSimpleFloat(total));
    	paymentDetail.setShippingTotal(shippingTotal);
/*
    	Vector<PaymentDetailsItemType> paymentItems = new Vector<PaymentDetailsItemType>();
    	Iterator<?> iterator = orderHeader.getOrderItemDetails().iterator();
    	while (iterator.hasNext()) {
    		OrderItemDetail orderItemDetail = (OrderItemDetail) iterator.next();
	    	PaymentDetailsItemType paymentItem = new PaymentDetailsItemType();
	    	Item item = orderItemDetail.getItem();
	    	String itemDesc = orderItemDetail.getItemOrderQty()  + " x " + item.getItemShortDesc();
	    	if (item.getItemShortDesc1().length() > 0) {
	    		itemDesc += ", " + item.getItemShortDesc1();
	    	}
	    	paymentItem.setName(itemDesc);
	    	paymentItem.setNumber(item.getItemNum());
	    	paymentItem.setQuantity(BigInteger.valueOf((long) 1));
	    	
	    	BasicAmountType itemPriceTotal = new BasicAmountType();
	    	itemPriceTotal.setCurrencyID(CurrencyCodeType.fromString(currencyCode));
	    	itemPriceTotal.set_value(Format.getFloatObj(orderItemDetail.getItemDetailAmount() - orderItemDetail.getItemDetailDiscountAmount()));
	    	paymentItem.setAmount(itemPriceTotal);

	    	Iterator<?> taxIterator<?> = orderItemDetail.getOrderDetailTaxes().iterator();
	    	float taxAmount = 0;
	    	while (taxIterator.hasNext()) {
	    		OrderDetailTax orderItemTax = (OrderDetailTax) taxIterator.next();
	    		taxAmount += orderItemTax.getTaxAmount();
	    	}
	    	BasicAmountType itemTaxTotal = new BasicAmountType();
	    	itemTaxTotal.setCurrencyID(CurrencyCodeType.fromString(currencyCode));
	    	itemTaxTotal.set_value(Format.getFloat(taxAmount));
	    	paymentItem.setTax(itemTaxTotal);
	    	paymentItems.add(paymentItem);
    	}
    	float shippingAmount = orderHeader.getShippingTotal() - orderHeader.getShippingDiscountTotal();
    	if (shippingAmount > 0) {
    		PaymentDetailsItemType paymentItem = new PaymentDetailsItemType();
	    	paymentItem.setName("Shipping");
	    	paymentItem.setNumber("");
	    	paymentItem.setQuantity(BigInteger.valueOf((long) 1));
	    	
	    	BasicAmountType itemPriceTotal = new BasicAmountType();
	    	itemPriceTotal.setCurrencyID(CurrencyCodeType.fromString(currencyCode));
	    	itemPriceTotal.set_value(Format.getFloatObj(shippingAmount));
	    	paymentItem.setAmount(itemPriceTotal);

	    	Iterator<?> taxIterator<?> = orderHeader.getOrderTaxes().iterator();
	    	float taxAmount = 0;
	    	while (taxIterator.hasNext()) {
	    		OrderDetailTax orderItemTax = (OrderDetailTax) taxIterator.next();
	    		if (orderItemTax.getOrderItemDetail() != null) {
	    			continue;
	    		}
	    		taxAmount += orderItemTax.getTaxAmount();
	    	}
	    	BasicAmountType itemTaxTotal = new BasicAmountType();
	    	itemTaxTotal.setCurrencyID(CurrencyCodeType.fromString(currencyCode));
	    	itemTaxTotal.set_value(Format.getFloat(taxAmount));
	    	paymentItem.setTax(itemTaxTotal);
	    	paymentItems.add(paymentItem);
    	}
    	
    	PaymentDetailsItemType paymentDetailsItemTypes[] = new PaymentDetailsItemType[paymentItems.size()];
    	paymentItems.copyInto(paymentDetailsItemTypes);
*/
//    	paymentDetail.setPaymentDetailsItem(paymentDetailsItemTypes);
    	
    	PaymentDetailsType paymentDetailsItemTypes[] = new PaymentDetailsType[1];
    	detail.setPaymentDetails(paymentDetailsItemTypes);
    	detail.setPaymentDetails(0, paymentDetail);
//    	detail.setPaymentDetails(paymentDetail);

    	request.setDoExpressCheckoutPaymentRequestDetails(detail);
    	
    	DoExpressCheckoutPaymentResponseType response = (DoExpressCheckoutPaymentResponseType) caller.call("DoExpressCheckoutPayment", request);
		if (!response.getAck().equals(AckCodeType.Success)) {
			throw new PaymentException(formatErrorMessage(response));
		}
		
//		authCode = response.getDoExpressCheckoutPaymentResponseDetails().getPaymentInfo().getTransactionID();
		authCode = response.getDoExpressCheckoutPaymentResponseDetails().getPaymentInfo(0).getTransactionID();
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

}
