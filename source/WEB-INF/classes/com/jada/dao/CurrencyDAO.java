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

public class CurrencyDAO extends Currency {
	private static final long serialVersionUID = 711047579530965417L;
	public static Currency load(String siteId, Long currencyId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Currency currency = (Currency) em.find(Currency.class, currencyId);
		if (!currency.getSiteId().equals(siteId)) {
			throw new SecurityException();
		}
		return currency;
	}
	public static Currency loadByCurrencyCode(String siteId, String currencyCode) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from Currency where siteId = :siteId and currencyCode = :currencyCode");
    	query.setParameter("siteId", siteId);
    	query.setParameter("currencyCode", currencyCode);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		return (Currency) iterator.next();
    	}
    	return null;
	}
}
