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
import com.jada.jpa.entity.ProductClass;

public class ProductClassDAO extends ProductClass {
	private static final long serialVersionUID = -5190472179467443342L;

	public static ProductClass load(String siteId, Long productClassId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ProductClass productClass = (ProductClass) em.find(ProductClass.class, productClassId);
		if (!productClass.getSite().getSiteId().equals(siteId)) {
			throw new SecurityException();
		}
		return productClass;
	}
	
	public static ProductClass loadByName(String siteId, String productClassName) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from  ProductClass productClass " + 
    								 "where productClass.site.siteId = :siteId " +
    								 "and   productClassName = :productClassName");
    	query.setParameter("siteId", siteId);
    	query.setParameter("productClassName", productClassName);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		ProductClass productClass = (ProductClass) iterator.next();
    		return productClass;
    	}
    	return null;
	}
}
