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
import com.jada.jpa.entity.Coupon;
import com.jada.jpa.entity.Currency;

public class CouponDAO extends Currency {
	private static final long serialVersionUID = -7835776219193942800L;
	public static Coupon load(String siteId, Long couponId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Coupon coupon = (Coupon) em.find(Coupon.class, couponId);
		if (coupon == null) {
			return null;
		}
		if (!coupon.getSite().getSiteId().equals(siteId)) {
			throw new SecurityException();
		}
		return coupon;
	}
	public static Coupon loadByCouponCode(String siteId, String couponCode) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from Coupon where siteId = :siteId and couponCode = :couponCode");
    	query.setParameter("siteId", siteId);
    	query.setParameter("couponCode", couponCode);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		return (Coupon) iterator.next();
    	}
    	return null;
	}
}
