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

package com.jada.myaccount.payment;

import org.apache.struts.util.LabelValueBean;

import com.jada.myaccount.portal.MyAccountPortalActionForm;

public class MyAccountPaymentActionForm extends MyAccountPortalActionForm {
	private static final long serialVersionUID = 5150123319047356667L;
	String custCreditCardFullName;
	String creditCardId;
	String custCreditCardNum;
	String custCreditCardExpiryMonth;
	String custCreditCardExpiryYear;
	String custCreditCardVerNum;
	LabelValueBean expiryMonths[];
	LabelValueBean expiryYears[];
	LabelValueBean creditCards[];
	public String getCreditCardId() {
		return creditCardId;
	}
	public void setCreditCardId(String creditCardId) {
		this.creditCardId = creditCardId;
	}
	public LabelValueBean[] getCreditCards() {
		return creditCards;
	}
	public void setCreditCards(LabelValueBean[] creditCards) {
		this.creditCards = creditCards;
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
}
