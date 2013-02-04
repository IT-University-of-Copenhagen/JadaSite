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

import javax.persistence.EntityManager;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.ShippingMethodRegionType;

public class ShippingMethodRegionTypeDAO extends ShippingMethodRegionType {
	private static final long serialVersionUID = 7472414945481322221L;

	public static ShippingMethodRegionType load(String siteId, Long shippingMethodRegionTypeId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ShippingMethodRegionType shippingmethodregiontype = (ShippingMethodRegionType) em.find(ShippingMethodRegionType.class, shippingMethodRegionTypeId);
		if (!shippingmethodregiontype.getShippingMethod().getSiteId().equals(siteId)) {
			throw new SecurityException();
		}
		return shippingmethodregiontype;
	}
}
