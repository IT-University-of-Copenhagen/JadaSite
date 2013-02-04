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

import java.util.Iterator;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Currency;
import com.jada.jpa.entity.PaymentGateway;

public class PaymentGatewayDAO extends Currency {
	private static final long serialVersionUID = -8086202210621920693L;
	public static PaymentGateway load(String siteId, Long paymentGatewayId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		PaymentGateway paymentGateway = (PaymentGateway) em.find(PaymentGateway.class, paymentGatewayId);
		if (paymentGateway == null) {
			return null;
		}
		if (!paymentGateway.getSite().getSiteId().equals(siteId)) {
			throw new SecurityException();
		}
		return paymentGateway;
	}
	public static PaymentGateway loadByPaymentGatewayCode(String siteId, String paymentGatewayCode) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from PaymentGateway where siteId = :siteId and paymentGatewayCode = :paymentGatewayCode");
    	query.setParameter("siteId", siteId);
    	query.setParameter("paymentGatewayCode", paymentGatewayCode);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		return (PaymentGateway) iterator.next();
    	}
    	return null;
	}
}
