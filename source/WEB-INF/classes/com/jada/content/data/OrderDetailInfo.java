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

package com.jada.content.data;

public class OrderDetailInfo {
	String itemNum;
	String itemUpcCd;
	String itemShortDesc;
	String itemShortDesc1;
	String itemPriceDisplay;
	String itemPrice;
	String itemMultQty;
	String itemMultPrice;
	String itemOrderQty;
	String itemPriceTotal;
	String itemTaxTotal;
	public String getItemNum() {
		return itemNum;
	}
	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}
	public String getItemOrderQty() {
		return itemOrderQty;
	}
	public void setItemOrderQty(String itemOrderQty) {
		this.itemOrderQty = itemOrderQty;
	}
	public String getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getItemPriceTotal() {
		return itemPriceTotal;
	}
	public void setItemPriceTotal(String itemPriceTotal) {
		this.itemPriceTotal = itemPriceTotal;
	}
	public String getItemShortDesc() {
		return itemShortDesc;
	}
	public void setItemShortDesc(String itemShortDesc) {
		this.itemShortDesc = itemShortDesc;
	}
	public String getItemShortDesc1() {
		return itemShortDesc1;
	}
	public void setItemShortDesc1(String itemShortDesc1) {
		this.itemShortDesc1 = itemShortDesc1;
	}
	public String getItemTaxTotal() {
		return itemTaxTotal;
	}
	public void setItemTaxTotal(String itemTaxTotal) {
		this.itemTaxTotal = itemTaxTotal;
	}
	public String getItemUpcCd() {
		return itemUpcCd;
	}
	public void setItemUpcCd(String itemUpcCd) {
		this.itemUpcCd = itemUpcCd;
	}
	public String getItemPriceDisplay() {
		return itemPriceDisplay;
	}
	public void setItemPriceDisplay(String itemPriceDisplay) {
		this.itemPriceDisplay = itemPriceDisplay;
	}
	public String getItemMultPrice() {
		return itemMultPrice;
	}
	public void setItemMultPrice(String itemMultPrice) {
		this.itemMultPrice = itemMultPrice;
	}
	public String getItemMultQty() {
		return itemMultQty;
	}
	public void setItemMultQty(String itemMultQty) {
		this.itemMultQty = itemMultQty;
	}
}
