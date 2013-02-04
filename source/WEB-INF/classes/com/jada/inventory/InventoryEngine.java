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

package com.jada.inventory;

import javax.persistence.EntityManager;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.Site;
import com.jada.system.ApplicationGlobal;
import com.jada.util.Constants;

public class InventoryEngine {
	Item item = null;
	boolean manageInventory = false;
	
	public InventoryEngine(Item item) throws Exception {
		this.item = item;
		Site site = item.getSite();
		manageInventory = true;
		if (ApplicationGlobal.isProductExternal()) {
			manageInventory = false;
			return;
		}
		if (site.getManageInventory() != Constants.VALUE_YES) {
			manageInventory = false;
			return;
		}
	}
	
	public InventoryEngine(Long itemId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	item = (Item) em.find(Item.class, itemId);
    	Site site = item.getSite();
		manageInventory = true;
		if (ApplicationGlobal.isProductExternal()) {
			manageInventory = false;
		}
		if (site.getManageInventory() != Constants.VALUE_YES) {
			manageInventory = false;
		}
	}
	
	public boolean isAvailable(int qty) throws Exception {
		if (!manageInventory) {
			return true;
		}
		if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_TEMPLATE)) {
			return true;
		}
		if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_RECOMMAND_BUNDLE) || 
			item.getItemTypeCd().equals(Constants.ITEM_TYPE_STATIC_BUNDLE)) {
			for (Item child : item.getChildren()) {
				int availableQty = child.getItemQty() - child.getItemBookedQty();
				if (availableQty < qty) {
					return false;
				}
			}
		}
		else {
			int availableQty = item.getItemQty() - item.getItemBookedQty();
			if (availableQty < qty) {
				return false;
			}
		}
		return true;
	}
	
	public void adjustBookedQty(int qty) throws Exception {
		if (!manageInventory) {
			return;
		}
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		
		if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_RECOMMAND_BUNDLE) || 
			item.getItemTypeCd().equals(Constants.ITEM_TYPE_STATIC_BUNDLE)) {
			for (Item child : item.getChildren()) {
				int itemBookedQty = child.getItemBookedQty();
				itemBookedQty += qty;
				child.setItemBookedQty(itemBookedQty);
				em.persist(child);	
			}
		}
		else {
			int itemBookedQty = item.getItemBookedQty();
			itemBookedQty += qty;
			item.setItemBookedQty(itemBookedQty);
			em.persist(item);
		}
	}
	
	public void adjustQty(int qty) throws Exception {
		if (!manageInventory) {
			return;
		}
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_RECOMMAND_BUNDLE) || 
			item.getItemTypeCd().equals(Constants.ITEM_TYPE_STATIC_BUNDLE)) {
			for (Item child : item.getChildren()) {
				int itemQty = child.getItemQty();
				itemQty += qty;
				child.setItemQty(itemQty);
				em.persist(child);	
			}
		}
		else {
			int itemQty = item.getItemQty();
			itemQty += qty;
			item.setItemQty(itemQty);
			em.persist(item);		
		}
	}

}
