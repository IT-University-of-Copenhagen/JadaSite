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

package com.jada.myaccount.address;

import org.apache.struts.util.LabelValueBean;

import com.jada.myaccount.portal.MyAccountPortalActionForm;

public class MyAccountAddressActionForm extends MyAccountPortalActionForm {
	private static final long serialVersionUID = 619761762370203571L;
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
	LabelValueBean custStates[];
	LabelValueBean billingCustStates[];
	LabelValueBean shippingCustStates[];
	LabelValueBean countries[];
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
	public String getCustStateName() {
		return custStateName;
	}
	public void setCustStateName(String custStateName) {
		this.custStateName = custStateName;
	}
	public String getCustCountryCode() {
		return custCountryCode;
	}
	public void setCustCountryCode(String custCountryCode) {
		this.custCountryCode = custCountryCode;
	}
	public String getCustCountryName() {
		return custCountryName;
	}
	public void setCustCountryName(String custCountryName) {
		this.custCountryName = custCountryName;
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
	public String getBillingCustStateName() {
		return billingCustStateName;
	}
	public void setBillingCustStateName(String billingCustStateName) {
		this.billingCustStateName = billingCustStateName;
	}
	public String getBillingCustCountryCode() {
		return billingCustCountryCode;
	}
	public void setBillingCustCountryCode(String billingCustCountryCode) {
		this.billingCustCountryCode = billingCustCountryCode;
	}
	public String getBillingCustCountryName() {
		return billingCustCountryName;
	}
	public void setBillingCustCountryName(String billingCustCountryName) {
		this.billingCustCountryName = billingCustCountryName;
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
	public String getShippingCustStateName() {
		return shippingCustStateName;
	}
	public void setShippingCustStateName(String shippingCustStateName) {
		this.shippingCustStateName = shippingCustStateName;
	}
	public String getShippingCustCountryCode() {
		return shippingCustCountryCode;
	}
	public void setShippingCustCountryCode(String shippingCustCountryCode) {
		this.shippingCustCountryCode = shippingCustCountryCode;
	}
	public String getShippingCustCountryName() {
		return shippingCustCountryName;
	}
	public void setShippingCustCountryName(String shippingCustCountryName) {
		this.shippingCustCountryName = shippingCustCountryName;
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
	public LabelValueBean[] getCountries() {
		return countries;
	}
	public void setCountries(LabelValueBean[] countries) {
		this.countries = countries;
	}
	public LabelValueBean[] getCustStates() {
		return custStates;
	}
	public void setCustStates(LabelValueBean[] custStates) {
		this.custStates = custStates;
	}
	public LabelValueBean[] getBillingCustStates() {
		return billingCustStates;
	}
	public void setBillingCustStates(LabelValueBean[] billingCustStates) {
		this.billingCustStates = billingCustStates;
	}
	public LabelValueBean[] getShippingCustStates() {
		return shippingCustStates;
	}
	public void setShippingCustStates(LabelValueBean[] shippingCustStates) {
		this.shippingCustStates = shippingCustStates;
	}
}
