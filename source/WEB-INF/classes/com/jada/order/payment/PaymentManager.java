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

import java.lang.reflect.Constructor;

import javax.servlet.http.HttpServletRequest;

import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteCurrency;

public class PaymentManager {
	static public PaymentEngine getPaymentEngine(String paymentGatewayProvider, SiteCurrency siteCurrency) throws Exception {
		if (paymentGatewayProvider == null) {
			return null;
		}
		Site site = siteCurrency.getSiteDomain().getSite();
		Class<?> c = Class.forName("com.jada.order.payment.gateway." + paymentGatewayProvider);
		Constructor<?> constructor = c.getConstructor(Site.class, Long.class);
		PaymentEngine paymentEngine = (PaymentEngine) constructor.newInstance(site, siteCurrency.getPaymentGateway().getPaymentGatewayId());
		
		return paymentEngine;
	}
	
	static public PaymentEngine getPaymentEngine(SiteCurrency siteCurrency, HttpServletRequest request) throws Exception {
		if (siteCurrency.getPaymentGateway() == null) {
			return null;
		}
		String paymentGatewayProvider = siteCurrency.getPaymentGateway().getPaymentGatewayProvider();
		Class<?> c = Class.forName("com.jada.order.payment.gateway." + paymentGatewayProvider);
		Constructor<?> constructor = c.getConstructor(Site.class, Long.class);
		PaymentEngine paymentEngine = (PaymentEngine) constructor.newInstance(siteCurrency.getSiteDomain().getSite(), siteCurrency.getPaymentGateway().getPaymentGatewayId());
		return paymentEngine;
	}

}
