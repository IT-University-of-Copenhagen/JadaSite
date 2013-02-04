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

package com.jada.order.payment;

public class CreditCardInfo {
	String creditCardFullName;
	String creditCardNum;
	String creditCardExpiryMonth;
	String creditCardExpiryYear;
	String creditCardVerNum;
	public String getCreditCardFullName() {
		return creditCardFullName;
	}
	public void setCreditCardFullName(String creditCardFullName) {
		this.creditCardFullName = creditCardFullName;
	}
	public String getCreditCardNum() {
		return creditCardNum;
	}
	public void setCreditCardNum(String creditCardNum) {
		this.creditCardNum = creditCardNum;
	}
	public String getCreditCardExpiryYear() {
		return creditCardExpiryYear;
	}
	public void setCreditCardExpiryYear(String creditCardExpiryYear) {
		this.creditCardExpiryYear = creditCardExpiryYear;
	}
	public String getCreditCardVerNum() {
		return creditCardVerNum;
	}
	public void setCreditCardVerNum(String creditCardVerNum) {
		this.creditCardVerNum = creditCardVerNum;
	}
	public String getCreditCardExpiryMonth() {
		return creditCardExpiryMonth;
	}
	public void setCreditCardExpiryMonth(String creditCardExpiryMonth) {
		this.creditCardExpiryMonth = creditCardExpiryMonth;
	}

}
