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
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.CustomerClass;
import com.jada.jpa.entity.ItemTierPrice;
import com.jada.util.Utility;

public class ItemTierPriceDAO extends ItemTierPrice {
	private static final long serialVersionUID = 2927973275751752923L;

	public static ItemTierPrice load(String siteId, Long itemTierPriceId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	ItemTierPrice itemTierPrice = (ItemTierPrice) em.find(ItemTierPrice.class, itemTierPriceId);
		if (!itemTierPrice.getItem().getSite().getSiteId().equals(siteId)) {
			throw new SecurityException();
		}
		return itemTierPrice;
	}
	
    static public boolean isPublished(ItemTierPrice itemTierPrice) {
    	if (itemTierPrice.getItemTierPricePublishOn() != null) {
        	if (!Utility.isDateBetween(itemTierPrice.getItemTierPricePublishOn(), itemTierPrice.getItemTierPriceExpireOn())) {
        		return false;
        	}
    	}
    	return true;
    }
    
    static public boolean isApplicable(ItemTierPrice itemTierPrice, Customer customer) {
    	if (!isPublished(itemTierPrice)) {
    		return false;
    	}
    	CustomerClass customerClass = itemTierPrice.getCustomerClass();
    	if (customerClass == null) {
    		return true;
    	}
    	if (customer == null) {
    		return false;
    	}
    	if (itemTierPrice.getCustomerClass().getCustClassId().equals(customer.getCustomerClass().getCustClassId())) {
    		return true;
    	}
    	return false;
    }
    
}
