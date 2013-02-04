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

package com.jada.content.checkout;

import java.util.Vector;

import org.apache.struts.util.LabelValueBean;

import com.jada.content.frontend.FrontendBaseForm;

public class ShoppingCartActionForm extends FrontendBaseForm {
	private static final long serialVersionUID = -196650110576263074L;
	boolean newUser;
	String cash;
	String currencyCode;
	String custId;
	String custAddressId;
	String custEmail;
	String custEmail1;
	String custFirstName;
	String custMiddleName;
	String custLastName;
	String custSuffix;
	String custAddressLine1;
	String custAddressLine2;
	String custCityName;
	String custStateCode;
	String custStateName;
	String custCountryCode;
	String custCountryName;
	String custZipCode;
	String custPhoneNum;
	String custFaxNum;
	String billingUseAddress;
	String billingCustAddressId;
	String billingCustPrefix;
	String billingCustFirstName;
	String billingCustMiddleName;
	String billingCustLastName;
	String billingCustSuffix;
	String billingCustAddressLine1;
	String billingCustAddressLine2;
	String billingCustCityName;
	String billingCustStateCode;
	String billingCustStateName;
	String billingCustCountryCode;
	String billingCustCountryName;
	String billingCustZipCode;
	String billingCustPhoneNum;
	String billingCustFaxNum;
	String shippingUseAddress;
	String shippingCustAddressId;
	String shippingCustPrefix;
	String shippingCustFirstName;
	String shippingCustMiddleName;
	String shippingCustLastName;
	String shippingCustSuffix;
	String shippingCustAddressLine1;
	String shippingCustAddressLine2;
	String shippingCustCityName;
	String shippingCustStateCode;
	String shippingCustStateName;
	String shippingCustCountryCode;
	String shippingCustCountryName;
	String shippingCustZipCode;
	String shippingCustPhoneNum;
	String shippingCustFaxNum;
	
	String estimateCountryCode;
	String estimateStateCode;
	String estimateStateName;
	String estimateZipCode;
	boolean estimatePickUp;
	
	Vector<?> shoppingCartCouponInfos;
	Vector<?> shoppingCartItemInfos;
	Vector<?> shoppingCartTaxInfos;
	
	String custCreditCardFullName;
	String creditCardId;
	String custCreditCardNum;
	String custCreditCardExpiryMonth;
	String custCreditCardExpiryYear;
	String custCreditCardVerNum;
	String paymentMessage;
	
	String paymentGatewayProvider;
	boolean cashPaymentOrder;
	boolean creditCardOrder;
	boolean payPalOrder;
	boolean payPalHostedOrder;
	String creditCardDesc;
	String authCode;
	String paymentReference1;
	
	LabelValueBean expiryMonths[];
	LabelValueBean expiryYears[];
	LabelValueBean creditCards[];
	LabelValueBean custStates[];
	LabelValueBean estimateStates[];
	LabelValueBean shippingCustStates[];
	LabelValueBean billingCustStates[];
	LabelValueBean countries[];
	
	String orderNum;
	String orderDatetime;
	String priceTotal;
	String shippingTotal;
	String shippingDiscountTotal;
	String shippingOrderTotal;
	String taxTotal;
	String orderTotal;
	String itemQtys[];
	String itemNaturalKeys[];
	String itemIds[];

	String custPassword;
	String custVerifyPassword;
	String custPublicName;
	String custPasswordError;
	String shippingMethodId;
	String shippingMethodName;
	String itemNaturalKey;
	LabelValueBean shippingMethods[];
	String shoppingCartMessage;
	String couponCode;
	boolean shippingValid;
	boolean print;
	boolean payPal;
	boolean payPalHosted;
	boolean creditCard;
	boolean cashPayment;
	boolean passwordEmpty;
	boolean sequenceInterrupt;
	String couponId;
	ShoppingCartAddressActionForm custAddress;
	ShoppingCartAddressActionForm billingAddress;
	ShoppingCartAddressActionForm shippingAddress;
	Vector<?> crossSellItems;
	boolean useBillingAddress;
	boolean includeShippingPickUp;
	boolean allowShippingQuote;
	boolean shippingQuoteLock;
	String customAttribTypeCodes[];
	String itemAttribDetailIds[];
	String customAttribValues[];
	boolean customerSignin;
	String paymentError;
	String resetStep;
	int checkoutSteps;
	public boolean isNewUser() {
		return newUser;
	}
	public void setNewUser(boolean newUser) {
		this.newUser = newUser;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getCustAddressId() {
		return custAddressId;
	}
	public void setCustAddressId(String custAddressId) {
		this.custAddressId = custAddressId;
	}
	public String getCustFirstName() {
		return custFirstName;
	}
	public void setCustFirstName(String custFirstName) {
		this.custFirstName = custFirstName;
	}
	public String getCustMiddleName() {
		return custMiddleName;
	}
	public void setCustMiddleName(String custMiddleName) {
		this.custMiddleName = custMiddleName;
	}
	public String getCustLastName() {
		return custLastName;
	}
	public void setCustLastName(String custLastName) {
		this.custLastName = custLastName;
	}
	public String getCustSuffix() {
		return custSuffix;
	}
	public void setCustSuffix(String custSuffix) {
		this.custSuffix = custSuffix;
	}
	public String getCustAddressLine1() {
		return custAddressLine1;
	}
	public void setCustAddressLine1(String custAddressLine1) {
		this.custAddressLine1 = custAddressLine1;
	}
	public String getCustAddressLine2() {
		return custAddressLine2;
	}
	public void setCustAddressLine2(String custAddressLine2) {
		this.custAddressLine2 = custAddressLine2;
	}
	public String getCustCityName() {
		return custCityName;
	}
	public void setCustCityName(String custCityName) {
		this.custCityName = custCityName;
	}
	public String getCustStateCode() {
		return custStateCode;
	}
	public void setCustStateCode(String custStateCode) {
		this.custStateCode = custStateCode;
	}
	public String getCustCountryCode() {
		return custCountryCode;
	}
	public void setCustCountryCode(String custCountryCode) {
		this.custCountryCode = custCountryCode;
	}
	public String getCustZipCode() {
		return custZipCode;
	}
	public void setCustZipCode(String custZipCode) {
		this.custZipCode = custZipCode;
	}
	public String getCustPhoneNum() {
		return custPhoneNum;
	}
	public void setCustPhoneNum(String custPhoneNum) {
		this.custPhoneNum = custPhoneNum;
	}
	public String getCustFaxNum() {
		return custFaxNum;
	}
	public void setCustFaxNum(String custFaxNum) {
		this.custFaxNum = custFaxNum;
	}
	public String getBillingUseAddress() {
		return billingUseAddress;
	}
	public void setBillingUseAddress(String billingUseAddress) {
		this.billingUseAddress = billingUseAddress;
	}
	public String getBillingCustAddressId() {
		return billingCustAddressId;
	}
	public void setBillingCustAddressId(String billingCustAddressId) {
		this.billingCustAddressId = billingCustAddressId;
	}
	public String getBillingCustPrefix() {
		return billingCustPrefix;
	}
	public void setBillingCustPrefix(String billingCustPrefix) {
		this.billingCustPrefix = billingCustPrefix;
	}
	public String getBillingCustFirstName() {
		return billingCustFirstName;
	}
	public void setBillingCustFirstName(String billingCustFirstName) {
		this.billingCustFirstName = billingCustFirstName;
	}
	public String getBillingCustMiddleName() {
		return billingCustMiddleName;
	}
	public void setBillingCustMiddleName(String billingCustMiddleName) {
		this.billingCustMiddleName = billingCustMiddleName;
	}
	public String getBillingCustLastName() {
		return billingCustLastName;
	}
	public void setBillingCustLastName(String billingCustLastName) {
		this.billingCustLastName = billingCustLastName;
	}
	public String getBillingCustSuffix() {
		return billingCustSuffix;
	}
	public void setBillingCustSuffix(String billingCustSuffix) {
		this.billingCustSuffix = billingCustSuffix;
	}
	public String getBillingCustAddressLine1() {
		return billingCustAddressLine1;
	}
	public void setBillingCustAddressLine1(String billingCustAddressLine1) {
		this.billingCustAddressLine1 = billingCustAddressLine1;
	}
	public String getBillingCustAddressLine2() {
		return billingCustAddressLine2;
	}
	public void setBillingCustAddressLine2(String billingCustAddressLine2) {
		this.billingCustAddressLine2 = billingCustAddressLine2;
	}
	public String getBillingCustCityName() {
		return billingCustCityName;
	}
	public void setBillingCustCityName(String billingCustCityName) {
		this.billingCustCityName = billingCustCityName;
	}
	public String getBillingCustStateCode() {
		return billingCustStateCode;
	}
	public void setBillingCustStateCode(String billingCustStateCode) {
		this.billingCustStateCode = billingCustStateCode;
	}
	public String getBillingCustCountryCode() {
		return billingCustCountryCode;
	}
	public void setBillingCustCountryCode(String billingCustCountryCode) {
		this.billingCustCountryCode = billingCustCountryCode;
	}
	public String getBillingCustZipCode() {
		return billingCustZipCode;
	}
	public void setBillingCustZipCode(String billingCustZipCode) {
		this.billingCustZipCode = billingCustZipCode;
	}
	public String getBillingCustPhoneNum() {
		return billingCustPhoneNum;
	}
	public void setBillingCustPhoneNum(String billingCustPhoneNum) {
		this.billingCustPhoneNum = billingCustPhoneNum;
	}
	public String getBillingCustFaxNum() {
		return billingCustFaxNum;
	}
	public void setBillingCustFaxNum(String billingCustFaxNum) {
		this.billingCustFaxNum = billingCustFaxNum;
	}
	public String getShippingUseAddress() {
		return shippingUseAddress;
	}
	public void setShippingUseAddress(String shippingUseAddress) {
		this.shippingUseAddress = shippingUseAddress;
	}
	public String getShippingCustAddressId() {
		return shippingCustAddressId;
	}
	public void setShippingCustAddressId(String shippingCustAddressId) {
		this.shippingCustAddressId = shippingCustAddressId;
	}
	public String getShippingCustPrefix() {
		return shippingCustPrefix;
	}
	public void setShippingCustPrefix(String shippingCustPrefix) {
		this.shippingCustPrefix = shippingCustPrefix;
	}
	public String getShippingCustFirstName() {
		return shippingCustFirstName;
	}
	public void setShippingCustFirstName(String shippingCustFirstName) {
		this.shippingCustFirstName = shippingCustFirstName;
	}
	public String getShippingCustMiddleName() {
		return shippingCustMiddleName;
	}
	public void setShippingCustMiddleName(String shippingCustMiddleName) {
		this.shippingCustMiddleName = shippingCustMiddleName;
	}
	public String getShippingCustLastName() {
		return shippingCustLastName;
	}
	public void setShippingCustLastName(String shippingCustLastName) {
		this.shippingCustLastName = shippingCustLastName;
	}
	public String getShippingCustSuffix() {
		return shippingCustSuffix;
	}
	public void setShippingCustSuffix(String shippingCustSuffix) {
		this.shippingCustSuffix = shippingCustSuffix;
	}
	public String getShippingCustAddressLine1() {
		return shippingCustAddressLine1;
	}
	public void setShippingCustAddressLine1(String shippingCustAddressLine1) {
		this.shippingCustAddressLine1 = shippingCustAddressLine1;
	}
	public String getShippingCustAddressLine2() {
		return shippingCustAddressLine2;
	}
	public void setShippingCustAddressLine2(String shippingCustAddressLine2) {
		this.shippingCustAddressLine2 = shippingCustAddressLine2;
	}
	public String getShippingCustCityName() {
		return shippingCustCityName;
	}
	public void setShippingCustCityName(String shippingCustCityName) {
		this.shippingCustCityName = shippingCustCityName;
	}
	public String getShippingCustStateCode() {
		return shippingCustStateCode;
	}
	public void setShippingCustStateCode(String shippingCustStateCode) {
		this.shippingCustStateCode = shippingCustStateCode;
	}
	public String getShippingCustCountryCode() {
		return shippingCustCountryCode;
	}
	public void setShippingCustCountryCode(String shippingCustCountryCode) {
		this.shippingCustCountryCode = shippingCustCountryCode;
	}
	public String getShippingCustZipCode() {
		return shippingCustZipCode;
	}
	public void setShippingCustZipCode(String shippingCustZipCode) {
		this.shippingCustZipCode = shippingCustZipCode;
	}
	public String getShippingCustPhoneNum() {
		return shippingCustPhoneNum;
	}
	public void setShippingCustPhoneNum(String shippingCustPhoneNum) {
		this.shippingCustPhoneNum = shippingCustPhoneNum;
	}
	public String getShippingCustFaxNum() {
		return shippingCustFaxNum;
	}
	public void setShippingCustFaxNum(String shippingCustFaxNum) {
		this.shippingCustFaxNum = shippingCustFaxNum;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getOrderDatetime() {
		return orderDatetime;
	}
	public void setOrderDatetime(String orderDatetime) {
		this.orderDatetime = orderDatetime;
	}
	public Vector<?> getShoppingCartCouponInfos() {
		return shoppingCartCouponInfos;
	}
	public void setShoppingCartCouponInfos(Vector<?> shoppingCartCouponInfos) {
		this.shoppingCartCouponInfos = shoppingCartCouponInfos;
	}
	public Vector<?> getShoppingCartItemInfos() {
		return shoppingCartItemInfos;
	}
	public void setShoppingCartItemInfos(Vector<?> shoppingCartItemInfos) {
		this.shoppingCartItemInfos = shoppingCartItemInfos;
	}
	public Vector<?> getShoppingCartTaxInfos() {
		return shoppingCartTaxInfos;
	}
	public void setShoppingCartTaxInfos(Vector<?> shoppingCartTaxInfos) {
		this.shoppingCartTaxInfos = shoppingCartTaxInfos;
	}
	public String getPriceTotal() {
		return priceTotal;
	}
	public void setPriceTotal(String priceTotal) {
		this.priceTotal = priceTotal;
	}
	public String getShippingTotal() {
		return shippingTotal;
	}
	public void setShippingTotal(String shippingTotal) {
		this.shippingTotal = shippingTotal;
	}
	public String getShippingDiscountTotal() {
		return shippingDiscountTotal;
	}
	public void setShippingDiscountTotal(String shippingDiscountTotal) {
		this.shippingDiscountTotal = shippingDiscountTotal;
	}
	public String getShippingOrderTotal() {
		return shippingOrderTotal;
	}
	public void setShippingOrderTotal(String shippingOrderTotal) {
		this.shippingOrderTotal = shippingOrderTotal;
	}
	public String getTaxTotal() {
		return taxTotal;
	}
	public void setTaxTotal(String taxTotal) {
		this.taxTotal = taxTotal;
	}
	public String getOrderTotal() {
		return orderTotal;
	}
	public void setOrderTotal(String orderTotal) {
		this.orderTotal = orderTotal;
	}
	public String[] getItemQtys() {
		return itemQtys;
	}
	public void setItemQtys(String[] itemQtys) {
		this.itemQtys = itemQtys;
	}
	public String getCustPassword() {
		return custPassword;
	}
	public void setCustPassword(String custPassword) {
		this.custPassword = custPassword;
	}
	public String getCustVerifyPassword() {
		return custVerifyPassword;
	}
	public void setCustVerifyPassword(String custVerifyPassword) {
		this.custVerifyPassword = custVerifyPassword;
	}
	public String getCustPasswordError() {
		return custPasswordError;
	}
	public void setCustPasswordError(String custPasswordError) {
		this.custPasswordError = custPasswordError;
	}
	public String getShippingMethodId() {
		return shippingMethodId;
	}
	public void setShippingMethodId(String shippingMethodId) {
		this.shippingMethodId = shippingMethodId;
	}
	public String getShippingMethodName() {
		return shippingMethodName;
	}
	public void setShippingMethodName(String shippingMethodName) {
		this.shippingMethodName = shippingMethodName;
	}
	public LabelValueBean[] getShippingMethods() {
		return shippingMethods;
	}
	public void setShippingMethods(LabelValueBean[] shippingMethods) {
		this.shippingMethods = shippingMethods;
	}
	public String getPaymentGatewayProvider() {
		return paymentGatewayProvider;
	}
	public void setPaymentGatewayProvider(String paymentGatewayProvider) {
		this.paymentGatewayProvider = paymentGatewayProvider;
	}
	public String getCreditCardDesc() {
		return creditCardDesc;
	}
	public void setCreditCardDesc(String creditCardDesc) {
		this.creditCardDesc = creditCardDesc;
	}
	public String getCustCreditCardNum() {
		return custCreditCardNum;
	}
	public void setCustCreditCardNum(String custCreditCardNum) {
		this.custCreditCardNum = custCreditCardNum;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getShoppingCartMessage() {
		return shoppingCartMessage;
	}
	public void setShoppingCartMessage(String shoppingCartMessage) {
		this.shoppingCartMessage = shoppingCartMessage;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public boolean isShippingValid() {
		return shippingValid;
	}
	public void setShippingValid(boolean shippingValid) {
		this.shippingValid = shippingValid;
	}
	public boolean isPrint() {
		return print;
	}
	public void setPrint(boolean print) {
		this.print = print;
	}
	public boolean isPayPal() {
		return payPal;
	}
	public void setPayPal(boolean payPal) {
		this.payPal = payPal;
	}
	public boolean isCreditCard() {
		return creditCard;
	}
	public void setCreditCard(boolean creditCard) {
		this.creditCard = creditCard;
	}
	public boolean isSequenceInterrupt() {
		return sequenceInterrupt;
	}
	public void setSequenceInterrupt(boolean sequenceInterrupt) {
		this.sequenceInterrupt = sequenceInterrupt;
	}
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	public ShoppingCartAddressActionForm getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(ShoppingCartAddressActionForm billingAddress) {
		this.billingAddress = billingAddress;
	}
	public ShoppingCartAddressActionForm getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(ShoppingCartAddressActionForm shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	public Vector<?> getCrossSellItems() {
		return crossSellItems;
	}
	public void setCrossSellItems(Vector<?> crossSellItems) {
		this.crossSellItems = crossSellItems;
	}
	public boolean isUseBillingAddress() {
		return useBillingAddress;
	}
	public void setUseBillingAddress(boolean useBillingAddress) {
		this.useBillingAddress = useBillingAddress;
	}
	public String[] getItemAttribDetailIds() {
		return itemAttribDetailIds;
	}
	public void setItemAttribDetailIds(String[] itemAttribDetailIds) {
		this.itemAttribDetailIds = itemAttribDetailIds;
	}
	public String getCustEmail() {
		return custEmail;
	}
	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}
	public LabelValueBean[] getCountries() {
		return countries;
	}
	public void setCountries(LabelValueBean[] countries) {
		this.countries = countries;
	}
	public String getCustStateName() {
		return custStateName;
	}
	public void setCustStateName(String custStateName) {
		this.custStateName = custStateName;
	}
	public String getCustCountryName() {
		return custCountryName;
	}
	public void setCustCountryName(String custCountryName) {
		this.custCountryName = custCountryName;
	}
	public String getBillingCustStateName() {
		return billingCustStateName;
	}
	public void setBillingCustStateName(String billingCustStateName) {
		this.billingCustStateName = billingCustStateName;
	}
	public String getBillingCustCountryName() {
		return billingCustCountryName;
	}
	public void setBillingCustCountryName(String billingCustCountryName) {
		this.billingCustCountryName = billingCustCountryName;
	}
	public String getShippingCustStateName() {
		return shippingCustStateName;
	}
	public void setShippingCustStateName(String shippingCustStateName) {
		this.shippingCustStateName = shippingCustStateName;
	}
	public String getShippingCustCountryName() {
		return shippingCustCountryName;
	}
	public void setShippingCustCountryName(String shippingCustCountryName) {
		this.shippingCustCountryName = shippingCustCountryName;
	}
	public String getCustCreditCardFullName() {
		return custCreditCardFullName;
	}
	public void setCustCreditCardFullName(String custCreditCardFullName) {
		this.custCreditCardFullName = custCreditCardFullName;
	}
	public String getCreditCardId() {
		return creditCardId;
	}
	public void setCreditCardId(String creditCardId) {
		this.creditCardId = creditCardId;
	}
	public String getCustCreditCardExpiryMonth() {
		return custCreditCardExpiryMonth;
	}
	public void setCustCreditCardExpiryMonth(String custCreditCardExpiryMonth) {
		this.custCreditCardExpiryMonth = custCreditCardExpiryMonth;
	}
	public String getCustCreditCardExpiryYear() {
		return custCreditCardExpiryYear;
	}
	public void setCustCreditCardExpiryYear(String custCreditCardExpiryYear) {
		this.custCreditCardExpiryYear = custCreditCardExpiryYear;
	}
	public String getCustCreditCardVerNum() {
		return custCreditCardVerNum;
	}
	public void setCustCreditCardVerNum(String custCreditCardVerNum) {
		this.custCreditCardVerNum = custCreditCardVerNum;
	}
	public String getPaymentMessage() {
		return paymentMessage;
	}
	public void setPaymentMessage(String paymentMessage) {
		this.paymentMessage = paymentMessage;
	}
	public LabelValueBean[] getExpiryMonths() {
		return expiryMonths;
	}
	public void setExpiryMonths(LabelValueBean[] expiryMonths) {
		this.expiryMonths = expiryMonths;
	}
	public LabelValueBean[] getExpiryYears() {
		return expiryYears;
	}
	public void setExpiryYears(LabelValueBean[] expiryYears) {
		this.expiryYears = expiryYears;
	}
	public LabelValueBean[] getCreditCards() {
		return creditCards;
	}
	public void setCreditCards(LabelValueBean[] creditCards) {
		this.creditCards = creditCards;
	}
	public ShoppingCartAddressActionForm getCustAddress() {
		return custAddress;
	}
	public void setCustAddress(ShoppingCartAddressActionForm custAddress) {
		this.custAddress = custAddress;
	}
	public boolean isPasswordEmpty() {
		return passwordEmpty;
	}
	public void setPasswordEmpty(boolean passwordEmpty) {
		this.passwordEmpty = passwordEmpty;
	}
	public String getPaymentReference1() {
		return paymentReference1;
	}
	public void setPaymentReference1(String paymentReference1) {
		this.paymentReference1 = paymentReference1;
	}
	public String[] getCustomAttribValues() {
		return customAttribValues;
	}
	public void setCustomAttribValues(String[] customAttribValues) {
		this.customAttribValues = customAttribValues;
	}
	public String[] getCustomAttribTypeCodes() {
		return customAttribTypeCodes;
	}
	public void setCustomAttribTypeCodes(String[] customAttribTypeCodes) {
		this.customAttribTypeCodes = customAttribTypeCodes;
	}
	public boolean isCashPayment() {
		return cashPayment;
	}
	public void setCashPayment(boolean cashPayment) {
		this.cashPayment = cashPayment;
	}
	public String getCash() {
		return cash;
	}
	public void setCash(String cash) {
		this.cash = cash;
	}
	public boolean isCashPaymentOrder() {
		return cashPaymentOrder;
	}
	public void setCashPaymentOrder(boolean cashPaymentOrder) {
		this.cashPaymentOrder = cashPaymentOrder;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCustPublicName() {
		return custPublicName;
	}
	public void setCustPublicName(String custPublicName) {
		this.custPublicName = custPublicName;
	}
	public boolean isAllowShippingQuote() {
		return allowShippingQuote;
	}
	public void setAllowShippingQuote(boolean allowShippingQuote) {
		this.allowShippingQuote = allowShippingQuote;
	}
	public boolean isIncludeShippingPickUp() {
		return includeShippingPickUp;
	}
	public void setIncludeShippingPickUp(boolean includeShippingPickUp) {
		this.includeShippingPickUp = includeShippingPickUp;
	}
	public boolean isShippingQuoteLock() {
		return shippingQuoteLock;
	}
	public void setShippingQuoteLock(boolean shippingQuoteLock) {
		this.shippingQuoteLock = shippingQuoteLock;
	}
	public LabelValueBean[] getCustStates() {
		return custStates;
	}
	public void setCustStates(LabelValueBean[] custStates) {
		this.custStates = custStates;
	}
	public LabelValueBean[] getShippingCustStates() {
		return shippingCustStates;
	}
	public void setShippingCustStates(LabelValueBean[] shippingCustStates) {
		this.shippingCustStates = shippingCustStates;
	}
	public LabelValueBean[] getBillingCustStates() {
		return billingCustStates;
	}
	public void setBillingCustStates(LabelValueBean[] billingCustStates) {
		this.billingCustStates = billingCustStates;
	}
	public String getEstimateCountryCode() {
		return estimateCountryCode;
	}
	public void setEstimateCountryCode(String estimateCountryCode) {
		this.estimateCountryCode = estimateCountryCode;
	}
	public String getEstimateStateCode() {
		return estimateStateCode;
	}
	public void setEstimateStateCode(String estimateStateCode) {
		this.estimateStateCode = estimateStateCode;
	}
	public String getEstimateZipCode() {
		return estimateZipCode;
	}
	public void setEstimateZipCode(String estimateZipCode) {
		this.estimateZipCode = estimateZipCode;
	}
	public LabelValueBean[] getEstimateStates() {
		return estimateStates;
	}
	public void setEstimateStates(LabelValueBean[] estimateStates) {
		this.estimateStates = estimateStates;
	}
	public String getEstimateStateName() {
		return estimateStateName;
	}
	public void setEstimateStateName(String estimateStateName) {
		this.estimateStateName = estimateStateName;
	}
	public boolean isEstimatePickUp() {
		return estimatePickUp;
	}
	public void setEstimatePickUp(boolean estimatePickUp) {
		this.estimatePickUp = estimatePickUp;
	}
	public boolean isCustomerSignin() {
		return customerSignin;
	}
	public void setCustomerSignin(boolean customerSignin) {
		this.customerSignin = customerSignin;
	}
	public String getPaymentError() {
		return paymentError;
	}
	public void setPaymentError(String paymentError) {
		this.paymentError = paymentError;
	}
	public String[] getItemNaturalKeys() {
		return itemNaturalKeys;
	}
	public void setItemNaturalKeys(String[] itemNaturalKeys) {
		this.itemNaturalKeys = itemNaturalKeys;
	}
	public String getItemNaturalKey() {
		return itemNaturalKey;
	}
	public void setItemNaturalKey(String itemNaturalKey) {
		this.itemNaturalKey = itemNaturalKey;
	}
	public String[] getItemIds() {
		return itemIds;
	}
	public void setItemIds(String[] itemIds) {
		this.itemIds = itemIds;
	}
	public String getCustEmail1() {
		return custEmail1;
	}
	public void setCustEmail1(String custEmail1) {
		this.custEmail1 = custEmail1;
	}
	public boolean isCreditCardOrder() {
		return creditCardOrder;
	}
	public void setCreditCardOrder(boolean creditCardOrder) {
		this.creditCardOrder = creditCardOrder;
	}
	public boolean isPayPalOrder() {
		return payPalOrder;
	}
	public void setPayPalOrder(boolean payPalOrder) {
		this.payPalOrder = payPalOrder;
	}
	public int getCheckoutSteps() {
		return checkoutSteps;
	}
	public void setCheckoutSteps(int checkoutSteps) {
		this.checkoutSteps = checkoutSteps;
	}
	public String getResetStep() {
		return resetStep;
	}
	public void setResetStep(String resetStep) {
		this.resetStep = resetStep;
	}
	public boolean isPayPalHosted() {
		return payPalHosted;
	}
	public void setPayPalHosted(boolean payPalHosted) {
		this.payPalHosted = payPalHosted;
	}
	public boolean isPayPalHostedOrder() {
		return payPalHostedOrder;
	}
	public void setPayPalHostedOrder(boolean payPalHostedOrder) {
		this.payPalHostedOrder = payPalHostedOrder;
	}
}
