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

package com.jada.util;

public class ItemPriceInfo {
	String itemNum;
	int itemOrderQty;
	Float itemPrice;
	Float itemPriceTotal;
	Float itemTaxTotal;
	ShoppingCartTax itemTaxes[];
	public ShoppingCartTax[] getItemTaxes() {
		return itemTaxes;
	}
	public void setItemTaxes(ShoppingCartTax[] itemTaxes) {
		this.itemTaxes = itemTaxes;
	}
	public String getItemNum() {
		return itemNum;
	}
	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}
	public int getItemOrderQty() {
		return itemOrderQty;
	}
	public void setItemOrderQty(int itemOrderQty) {
		this.itemOrderQty = itemOrderQty;
	}
	public Float getItemPriceTotal() {
		return itemPriceTotal;
	}
	public void setItemPriceTotal(Float itemPriceTotal) {
		this.itemPriceTotal = itemPriceTotal;
	}
	public Float getItemTaxTotal() {
		return itemTaxTotal;
	}
	public void setItemTaxTotal(Float itemTaxTotal) {
		this.itemTaxTotal = itemTaxTotal;
	}
	public Float getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Float itemPrice) {
		this.itemPrice = itemPrice;
	}
}
