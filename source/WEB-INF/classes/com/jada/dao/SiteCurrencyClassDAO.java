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
import javax.persistence.FlushModeType;
import javax.persistence.Query;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteCurrencyClass;

public class SiteCurrencyClassDAO extends Site {
	private static final long serialVersionUID = 6324860940285735502L;

	public static SiteCurrencyClass load(Long siteCurrencyClassId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	SiteCurrencyClass siteCurrencyClass = (SiteCurrencyClass) em.find(SiteCurrencyClass.class, siteCurrencyClassId);
		return siteCurrencyClass;
	}
	
	public static SiteCurrencyClass loadByName(String siteId, String siteCurrencyClassName) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from SiteCurrencyClass siteCurrencyClass where siteCurrencyClass.site.siteId = :siteId and siteCurrencyClassName = :siteCurrencyClassName");
    	query.setFlushMode(FlushModeType.COMMIT);
    	query.setParameter("siteId", siteId);
    	query.setParameter("siteCurrencyClassName", siteCurrencyClassName);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		SiteCurrencyClass siteCurrencyClass = (SiteCurrencyClass) iterator.next();
    		return siteCurrencyClass;
    	}
    	return null;
	}
}
