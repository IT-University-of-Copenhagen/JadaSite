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

package com.jada.order.cart;

import java.util.Date;

public class ItemEligibleTierPrice {
	int itemTierQty;
	float itemTierPrice;
	Date itemTierPricePublishOn;
	Date itemTierPriceExpireOn;
	public int getItemTierQty() {
		return itemTierQty;
	}
	public void setItemTierQty(int itemTierQty) {
		this.itemTierQty = itemTierQty;
	}
	public float getItemTierPrice() {
		return itemTierPrice;
	}
	public void setItemTierPrice(float itemTierPrice) {
		this.itemTierPrice = itemTierPrice;
	}
	public Date getItemTierPricePublishOn() {
		return itemTierPricePublishOn;
	}
	public void setItemTierPricePublishOn(Date itemTierPricePublishOn) {
		this.itemTierPricePublishOn = itemTierPricePublishOn;
	}
	public Date getItemTierPriceExpireOn() {
		return itemTierPriceExpireOn;
	}
	public void setItemTierPriceExpireOn(Date itemTierPriceExpireOn) {
		this.itemTierPriceExpireOn = itemTierPriceExpireOn;
	}
}
