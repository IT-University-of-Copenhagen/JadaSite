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

package com.jada.xml.paymentGateway;

public class PayPalExpressCheckOut {
	String paymentPaypalApiUsername;
	String paymentPaypalApiPassword;
	String paymentPaypalSignature;
	String paymentPaypalEnvironment;
	double paymentPaypalExtraAmount;
	double paymentPaypalExtraPercentage;
	Long paymentPaypalCustClassId;
	public String getPaymentPaypalApiUsername() {
		return paymentPaypalApiUsername;
	}
	public void setPaymentPaypalApiUsername(String paymentPaypalApiUsername) {
		this.paymentPaypalApiUsername = paymentPaypalApiUsername;
	}
	public String getPaymentPaypalApiPassword() {
		return paymentPaypalApiPassword;
	}
	public void setPaymentPaypalApiPassword(String paymentPaypalApiPassword) {
		this.paymentPaypalApiPassword = paymentPaypalApiPassword;
	}
	public String getPaymentPaypalSignature() {
		return paymentPaypalSignature;
	}
	public void setPaymentPaypalSignature(String paymentPaypalSignature) {
		this.paymentPaypalSignature = paymentPaypalSignature;
	}
	public String getPaymentPaypalEnvironment() {
		return paymentPaypalEnvironment;
	}
	public void setPaymentPaypalEnvironment(String paymentPaypalEnvironment) {
		this.paymentPaypalEnvironment = paymentPaypalEnvironment;
	}
	public double getPaymentPaypalExtraAmount() {
		return paymentPaypalExtraAmount;
	}
	public void setPaymentPaypalExtraAmount(double paymentPaypalExtraAmount) {
		this.paymentPaypalExtraAmount = paymentPaypalExtraAmount;
	}
	public double getPaymentPaypalExtraPercentage() {
		return paymentPaypalExtraPercentage;
	}
	public void setPaymentPaypalExtraPercentage(double paymentPaypalExtraPercentage) {
		this.paymentPaypalExtraPercentage = paymentPaypalExtraPercentage;
	}
	public Long getPaymentPaypalCustClassId() {
		return paymentPaypalCustClassId;
	}
	public void setPaymentPaypalCustClassId(Long paymentPaypalCustClassId) {
		this.paymentPaypalCustClassId = paymentPaypalCustClassId;
	}
}
