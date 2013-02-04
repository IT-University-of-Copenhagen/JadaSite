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

public class PayPalWebsitePaymentPro {
	String paypalApiUsername;
	String paypalApiPassword;
	String paypalSignature;
	String paypalEnvironment;
	public String getPaypalApiUsername() {
		return paypalApiUsername;
	}
	public void setPaypalApiUsername(String paypalApiUsername) {
		this.paypalApiUsername = paypalApiUsername;
	}
	public String getPaypalApiPassword() {
		return paypalApiPassword;
	}
	public void setPaypalApiPassword(String paypalApiPassword) {
		this.paypalApiPassword = paypalApiPassword;
	}
	public String getPaypalSignature() {
		return paypalSignature;
	}
	public void setPaypalSignature(String paypalSignature) {
		this.paypalSignature = paypalSignature;
	}
	public String getPaypalEnvironment() {
		return paypalEnvironment;
	}
	public void setPaypalEnvironment(String paypalEnvironment) {
		this.paypalEnvironment = paypalEnvironment;
	}
}
