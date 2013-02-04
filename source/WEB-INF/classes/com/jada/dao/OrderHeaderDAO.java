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

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.CreditHeader;
import com.jada.jpa.entity.InvoiceHeader;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.ShipHeader;
import com.jada.util.Constants;

public class OrderHeaderDAO extends OrderHeader {
	private static final long serialVersionUID = 774703970007313602L;

	public static OrderHeader load(String siteId, Long orderHeaderId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		OrderHeader orderheader = (OrderHeader) em.find(OrderHeader.class, orderHeaderId);
		if (!orderheader.getSiteDomain().getSite().getSiteId().equals(siteId)) {
			throw new SecurityException();
		}
		return orderheader;
	}

	public static OrderHeader load(String siteId, String orderNum) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from OrderHeader orderHeader where orderHeader.siteDomain.site.siteId = :siteId and orderNum = :orderNum");
    	query.setParameter("siteId", siteId);
    	query.setParameter("orderNum", orderNum);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		OrderHeader orderHeader = (OrderHeader) iterator.next();
    		return orderHeader;
    	}
    	return null;
	}
	
	public static boolean isAllowCancel(OrderHeader orderHeader) {
		if (orderHeader.getOrderStatus().equals(Constants.ORDERSTATUS_CANCELLED)) {
			return false;
		}
		for (CreditHeader creditHeader : orderHeader.getCreditHeaders()) {
			if (creditHeader.getCreditStatus().equals(Constants.ORDERSTATUS_COMPLETED)) {
				return false;
			}
		}
		for (InvoiceHeader invoiceHeader : orderHeader.getInvoiceHeaders()) {
			if (invoiceHeader.getInvoiceStatus().equals(Constants.ORDERSTATUS_COMPLETED)) {
				return false;
			}
		}
		for (ShipHeader shipHeader : orderHeader.getShipHeaders()) {
			if (shipHeader.getShipStatus().equals(Constants.ORDERSTATUS_COMPLETED)) {
				return false;
			}
		}
		return true;
	}
}
