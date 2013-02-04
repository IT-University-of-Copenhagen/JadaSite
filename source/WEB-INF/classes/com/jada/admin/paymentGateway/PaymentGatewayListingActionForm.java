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

package com.jada.admin.paymentGateway;

import java.util.Enumeration;

import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminListingActionForm;

public class PaymentGatewayListingActionForm extends AdminListingActionForm {
	private static final long serialVersionUID = 3597182967833979411L;
	String srPaymentGatewayName;
	PaymentGatewayDisplayForm paymentGateways[];
    public PaymentGatewayDisplayForm getPaymentGateway(int index) {
    	return paymentGateways[index];
    }
	public void reset(ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
		String COUPONDETAIL = "paymentGateway.*paymentGatewayId";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(COUPONDETAIL)) {
				count++;
			}
		}
		paymentGateways = new PaymentGatewayDisplayForm[count];
		for (int i = 0; i < paymentGateways.length; i++) {
			paymentGateways[i] = new PaymentGatewayDisplayForm();
		}
	}
	public String getSrPaymentGatewayName() {
		return srPaymentGatewayName;
	}
	public void setSrPaymentGatewayName(String srPaymentGatewayName) {
		this.srPaymentGatewayName = srPaymentGatewayName;
	}
	public PaymentGatewayDisplayForm[] getPaymentGateways() {
		return paymentGateways;
	}
	public void setPaymentGateways(PaymentGatewayDisplayForm[] paymentGateways) {
		this.paymentGateways = paymentGateways;
	}
}
