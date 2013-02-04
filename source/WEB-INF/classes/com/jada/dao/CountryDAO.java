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
import com.jada.jpa.entity.Country;

public class CountryDAO extends Country {
	private static final long serialVersionUID = -8827430679330867335L;
	public static Country load(String siteId, Long countryId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Country country = (Country) em.find(Country.class, countryId);
		if (!country.getSiteId().equals(siteId)) {
			throw new SecurityException();
		}
		return country;
	}
	public static Country loadByCountryName(String siteId, String countryName) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from Country where siteId = :siteId and countryName = :countryName");
    	query.setParameter("siteId", siteId);
    	query.setParameter("countryName", countryName);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		return (Country) iterator.next();
    	}
    	return null;
	}
	public static Country loadByCountryCode(String siteId, String countryCode) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from Country where siteId = :siteId and countryCode = :countryCode");
    	query.setParameter("siteId", siteId);
    	query.setParameter("countryCode", countryCode);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		return (Country) iterator.next();
    	}
    	return null;
	}
}
