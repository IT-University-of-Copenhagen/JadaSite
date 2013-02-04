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

package com.jada.dao;

import javax.persistence.EntityManager;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.CustomerAddress;

public class CustomerAddressDAO extends CustomerAddress {
	private static final long serialVersionUID = 1982269244513214829L;
	Long custAddressId;
	String custAddressType;
	String custAddressLine1;
	String custAddressLine2;
	String custCityName;
	String custStateName;
	String custStateCode;
	Long stateId;
	String custCountryName;
	String custCountryCode;
	Long countryId;
	String custZipCode;
	String custPhoneNum;
	String custFaxNum;
	public static CustomerAddress load(String siteId, Long custAddressId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		CustomerAddress customeraddress = (CustomerAddress) em.find(CustomerAddress.class, custAddressId);
/*
		if (!customeraddress.getgetSiteId().equals(siteId)) {
			throw new SecurityException();
		}
*/
		return customeraddress;
	}
	public CustomerAddressDAO(CustomerAddress customerAddress) {
		this.custAddressId = customerAddress.getCustAddressId();
		this.custAddressType = customerAddress.getCustAddressType();
		this.custAddressLine1 = customerAddress.getCustAddressLine1();
		this.custAddressLine2 = customerAddress.getCustAddressLine2();
		this.custCityName = customerAddress.getCustCityName();
		this.custStateName = customerAddress.getCustStateName();
		this.custStateCode = customerAddress.getCustStateCode();
		this.custCountryName = customerAddress.getCustCountryName();
		this.custCountryCode = customerAddress.getCustCountryCode();
		this.custZipCode = customerAddress.getCustZipCode();
		this.custPhoneNum = customerAddress.getCustPhoneNum();
		this.custFaxNum = customerAddress.getCustFaxNum();
		if (customerAddress.getCountry() != null) {
			this.countryId = customerAddress.getCountry().getCountryId();
		}
		if (customerAddress.getState() != null) {
			this.stateId = customerAddress.getState().getStateId();
		}
	}
	public Long getCustAddressId() {
		return custAddressId;
	}
	public void setCustAddressId(Long custAddressId) {
		this.custAddressId = custAddressId;
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
	public String getCustAddressType() {
		return custAddressType;
	}
	public void setCustAddressType(String custAddressType) {
		this.custAddressType = custAddressType;
	}
	public String getCustCityName() {
		return custCityName;
	}
	public void setCustCityName(String custCityName) {
		this.custCityName = custCityName;
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
	public String getCustFaxNum() {
		return custFaxNum;
	}
	public void setCustFaxNum(String custFaxNum) {
		this.custFaxNum = custFaxNum;
	}
	public String getCustPhoneNum() {
		return custPhoneNum;
	}
	public void setCustPhoneNum(String custPhoneNum) {
		this.custPhoneNum = custPhoneNum;
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
	public String getCustZipCode() {
		return custZipCode;
	}
	public void setCustZipCode(String custZipCode) {
		this.custZipCode = custZipCode;
	}
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	public Long getStateId() {
		return stateId;
	}
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}
}
