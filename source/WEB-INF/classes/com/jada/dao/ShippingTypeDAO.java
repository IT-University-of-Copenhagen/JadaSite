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
import com.jada.jpa.entity.ShippingType;

public class ShippingTypeDAO extends ShippingType {
	private static final long serialVersionUID = 4041713304293897947L;

	public static ShippingType load(String siteId, Long shippingTypeId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ShippingType shippingtype = (ShippingType) em.find(ShippingType.class, shippingTypeId);
		if (!shippingtype.getSiteId().equals(siteId)) {
			throw new SecurityException();
		}
		return shippingtype;
	}
	
	public static ShippingType loadByName(String siteId, String shippingTypeName) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from  ShippingType shippingType " + 
    								 "where shippingType.site.siteId = :siteId " +
    								 "and   shippingTypeName = :shippingTypeName");
    	query.setParameter("siteId", siteId);
    	query.setParameter("shippingTypeName", shippingTypeName);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		ShippingType shippingType = (ShippingType) iterator.next();
    		return shippingType;
    	}
    	return null;
	}
}
