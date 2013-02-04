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
import com.jada.jpa.entity.CustomerCreditCard;

public class CustomerCreditCardDAO extends CustomerCreditCard {
	private static final long serialVersionUID = 2362830669787796258L;
	public Long custCreditCardId;
	public String custCreditCardFullName;
	public String custCreditCardNum;
	public String custCreditCardExpiryMonth;
	public String custCreditCardExpiryYear;
	public String custCreditCardVerNum;
	public CreditCardDAO creditCard;
	
	public static CustomerCreditCard load(String siteId, Long custCreditCardId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		CustomerCreditCard customercreditcard = (CustomerCreditCard) em.find(CustomerCreditCard.class, custCreditCardId);
/*
		if (!customercreditcard.getSiteId().equals(siteId)) {
			throw new SecurityException();
		}
*/
		return customercreditcard;
	}
	public CustomerCreditCardDAO(CustomerCreditCard customerCreditCard) {
		this.custCreditCardId = customerCreditCard.getCustCreditCardId();
		this.custCreditCardFullName = customerCreditCard.getCustCreditCardFullName();
		this.custCreditCardNum = customerCreditCard.getCustCreditCardNum();
		this.custCreditCardExpiryMonth = customerCreditCard.getCustCreditCardExpiryMonth();
		this.custCreditCardExpiryYear = customerCreditCard.getCustCreditCardExpiryYear();
		this.custCreditCardVerNum = customerCreditCard.getCustCreditCardVerNum();
		if (customerCreditCard.getCreditCard() != null) {
			this.creditCard = new CreditCardDAO(customerCreditCard.getCreditCard());
		}
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
	public String getCustCreditCardFullName() {
		return custCreditCardFullName;
	}
	public void setCustCreditCardFullName(String custCreditCardFullName) {
		this.custCreditCardFullName = custCreditCardFullName;
	}
	public Long getCustCreditCardId() {
		return custCreditCardId;
	}
	public void setCustCreditCardId(Long custCreditCardId) {
		this.custCreditCardId = custCreditCardId;
	}
	public String getCustCreditCardNum() {
		return custCreditCardNum;
	}
	public void setCustCreditCardNum(String custCreditCardNum) {
		this.custCreditCardNum = custCreditCardNum;
	}
	public String getCustCreditCardVerNum() {
		return custCreditCardVerNum;
	}
	public void setCustCreditCardVerNum(String custCreditCardVerNum) {
		this.custCreditCardVerNum = custCreditCardVerNum;
	}
	public CreditCardDAO getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CreditCardDAO creditCard) {
		this.creditCard = creditCard;
	}
}
