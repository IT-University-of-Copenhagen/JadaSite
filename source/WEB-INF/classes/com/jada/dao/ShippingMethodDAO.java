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

import java.util.Set;

import javax.persistence.EntityManager;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.ShippingMethod;
import com.jada.jpa.entity.ShippingMethodLanguage;

public class ShippingMethodDAO extends ShippingMethod {
	private static final long serialVersionUID = 4497338014468025543L;

	public static ShippingMethod load(String siteId, Long shippingMethodId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ShippingMethod shippingmethod = (ShippingMethod) em.find(ShippingMethod.class, shippingMethodId);
		if (!shippingmethod.getSiteId().equals(siteId)) {
			throw new SecurityException();
		}
		return shippingmethod;
	}
	
	public static void remove(String siteId, ShippingMethod shippingMethod) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Set<ShippingMethodLanguage> shippingMethodLanguages = shippingMethod.getShippingMethodLanguages();
		em.remove(shippingMethod);
		for (ShippingMethodLanguage shippingMethodLanguage : shippingMethodLanguages) {
			em.remove(shippingMethodLanguage);
		}
	}
}
