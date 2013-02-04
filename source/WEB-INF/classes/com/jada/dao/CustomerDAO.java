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
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.CustomerAddress;
import com.jada.util.Constants;

public class CustomerDAO extends Customer {
	private static final long serialVersionUID = -757331669210721043L;
	Long custId;
	String siteId;
	String custFirstName;
	String custMiddleName;
	String custLastName;
	String custEmail;
	String custPassword;
	String custSource;
	String custSourceRef;
	public static Customer load(String siteId, Long custId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Customer customer = (Customer) em.find(Customer.class, custId);
		if (!customer.getSiteId().equals(siteId)) {
			throw new SecurityException();
		}
		return customer;
	}
	public CustomerDAO(Customer customer) {
		this.custId = customer.getCustId();
		this.custEmail = customer.getCustEmail();
		this.custSource = customer.getCustSource();
		this.custSourceRef = customer.getCustSourceRef();
		CustomerAddress customerAddress = null;
    	for (CustomerAddress address : customer.getCustAddresses()) {
    		if (address.getCustAddressType().equals(Constants.CUSTOMER_ADDRESS_CUST)) {
    			customerAddress = address;
    			break;
    		}
    	}
		this.custFirstName = customerAddress.getCustFirstName();
		this.custMiddleName = customerAddress.getCustMiddleName();
		this.custLastName = customerAddress.getCustLastName();
	}
	public String getCustEmail() {
		return custEmail;
	}
	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}
	public String getCustFirstName() {
		return custFirstName;
	}
	public void setCustFirstName(String custFirstName) {
		this.custFirstName = custFirstName;
	}
	public Long getCustId() {
		return custId;
	}
	public void setCustId(Long custId) {
		this.custId = custId;
	}
	public String getCustLastName() {
		return custLastName;
	}
	public void setCustLastName(String custLastName) {
		this.custLastName = custLastName;
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
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getCustMiddleName() {
		return custMiddleName;
	}
	public void setCustMiddleName(String custMiddleName) {
		this.custMiddleName = custMiddleName;
	}
}
