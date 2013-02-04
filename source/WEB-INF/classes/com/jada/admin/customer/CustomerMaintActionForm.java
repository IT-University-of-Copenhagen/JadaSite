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

package com.jada.admin.customer;

import org.apache.struts.util.LabelValueBean;

import com.jada.admin.AdminMaintActionForm;

public class CustomerMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = 405556394733308749L;
	String custId;
	String siteName;
	String custPublicName;
	String custPrefix;
	String custAddressId;
	String custFirstName;
	String custMiddleName;
	String custLastName;
	String custSuffix;
	String custAddressLine1;
	String custAddressLine2;
	String custCityName;
	String custStateCode;
	String custCountryCode;
	String custZipCode;
	String custPhoneNum;
	String custFaxNum;
	String billingCustUseAddress;
	String billingCustPrefix;
	String billingCustAddressId;
	String billingCustFirstName;
	String billingCustMiddleName;
	String billingCustLastName;
	String billingCustSuffix;
	String billingCustAddressLine1;
	String billingCustAddressLine2;
	String billingCustCityName;
	String billingCustStateCode;
	String billingCustCountryCode;
	String billingCustZipCode;
	String billingCustPhoneNum;
	String billingCustFaxNum;
	String shippingCustUseAddress;
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
	String shippingCustCountryCode;
	String shippingCustZipCode;
	String shippingCustPhoneNum;
	String shippingCustFaxNum;
	String custEmail;
	String custPassword;
	String custPassword1;
	String custSource;
	String custSourceRef;
	String custComments;
	String active;
	String custCreditCardFullName;
	String custCreditCardNum;
	String custCreditCardExpiryMonth;
	String custCreditCardExpiryYear;
	String custCreditCardVerNum;
	String custClassId;
	String creditCardId;
	LabelValueBean custClasses[];
	LabelValueBean countries[];
	LabelValueBean states[];
	LabelValueBean expiryMonths[];
	LabelValueBean expiryYears[];
	LabelValueBean creditCards[];
	OrderDisplayForm orders[];
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getCustPrefix() {
		return custPrefix;
	}
	public void setCustPrefix(String custPrefix) {
		this.custPrefix = custPrefix;
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
	public String getBillingCustPrefix() {
		return billingCustPrefix;
	}
	public void setBillingCustPrefix(String billingCustPrefix) {
		this.billingCustPrefix = billingCustPrefix;
	}
	public String getBillingCustAddressId() {
		return billingCustAddressId;
	}
	public void setBillingCustAddressId(String billingCustAddressId) {
		this.billingCustAddressId = billingCustAddressId;
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
	public String getCustEmail() {
		return custEmail;
	}
	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}
	public String getCustPassword() {
		return custPassword;
	}
	public void setCustPassword(String custPassword) {
		this.custPassword = custPassword;
	}
	public String getCustPassword1() {
		return custPassword1;
	}
	public void setCustPassword1(String custPassword1) {
		this.custPassword1 = custPassword1;
	}
	public String getCustSource() {
		return custSource;
	}
	public void setCustSource(String custSource) {
		this.custSource = custSource;
	}
	public String getCustSourceRef() {
		return custSourceRef;
	}
	public void setCustSourceRef(String custSourceRef) {
		this.custSourceRef = custSourceRef;
	}
	public String getCustComments() {
		return custComments;
	}
	public void setCustComments(String custComments) {
		this.custComments = custComments;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getCustCreditCardFullName() {
		return custCreditCardFullName;
	}
	public void setCustCreditCardFullName(String custCreditCardFullName) {
		this.custCreditCardFullName = custCreditCardFullName;
	}
	public String getCustCreditCardNum() {
		return custCreditCardNum;
	}
	public void setCustCreditCardNum(String custCreditCardNum) {
		this.custCreditCardNum = custCreditCardNum;
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
	public String getCustClassId() {
		return custClassId;
	}
	public void setCustClassId(String custClassId) {
		this.custClassId = custClassId;
	}
	public LabelValueBean[] getCustClasses() {
		return custClasses;
	}
	public void setCustClasses(LabelValueBean[] custClasses) {
		this.custClasses = custClasses;
	}
	public LabelValueBean[] getCountries() {
		return countries;
	}
	public void setCountries(LabelValueBean[] countries) {
		this.countries = countries;
	}
	public LabelValueBean[] getStates() {
		return states;
	}
	public void setStates(LabelValueBean[] states) {
		this.states = states;
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
	public OrderDisplayForm[] getOrders() {
		return orders;
	}
	public void setOrders(OrderDisplayForm[] orders) {
		this.orders = orders;
	}
	public String getBillingCustUseAddress() {
		return billingCustUseAddress;
	}
	public void setBillingCustUseAddress(String billingCustUseAddress) {
		this.billingCustUseAddress = billingCustUseAddress;
	}
	public String getShippingCustUseAddress() {
		return shippingCustUseAddress;
	}
	public void setShippingCustUseAddress(String shippingCustUseAddress) {
		this.shippingCustUseAddress = shippingCustUseAddress;
	}
	public String getCustPublicName() {
		return custPublicName;
	}
	public void setCustPublicName(String custPublicName) {
		this.custPublicName = custPublicName;
	}
	public LabelValueBean[] getCreditCards() {
		return creditCards;
	}
	public void setCreditCards(LabelValueBean[] creditCards) {
		this.creditCards = creditCards;
	}
	public String getCreditCardId() {
		return creditCardId;
	}
	public void setCreditCardId(String creditCardId) {
		this.creditCardId = creditCardId;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
}
