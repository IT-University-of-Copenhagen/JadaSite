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

package com.jada.myaccount.order;

import java.util.Vector;

public class OrderDetailDisplayForm {
	String orderDetailId;
	String itemNum;
	String itemUpcCd;
	String itemShortDesc;
	String itemShortDesc1;
	String shortDescDisplay;
	String itemPrice;
	String itemTierQty;
	String itemTierPrice;
	String priceDisplay;
	String itemOrderQty;
	String itemCreditTotal;
	String itemPriceTotal;
	String itemTaxTotal;
	String itemSubTotal;
	String itemInvoiceQty;
	String itemCreditQty;
	String imageId;
	Vector<?> orderItemAttributes;
	public String getItemCreditQty() {
		return itemCreditQty;
	}
	public void setItemCreditQty(String itemCreditQty) {
		this.itemCreditQty = itemCreditQty;
	}
	public String getItemCreditTotal() {
		return itemCreditTotal;
	}
	public void setItemCreditTotal(String itemCreditTotal) {
		this.itemCreditTotal = itemCreditTotal;
	}
	public String getItemTierQty() {
		return itemTierQty;
	}
	public void setItemTierQty(String itemTierQty) {
		this.itemTierQty = itemTierQty;
	}
	public String getItemTierPrice() {
		return itemTierPrice;
	}
	public void setItemTierPrice(String itemTierPrice) {
		this.itemTierPrice = itemTierPrice;
	}
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
	public String getItemSubTotal() {
		return itemSubTotal;
	}
	public void setItemSubTotal(String itemSubTotal) {
		this.itemSubTotal = itemSubTotal;
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
	public String getOrderDetailId() {
		return orderDetailId;
	}
	public void setOrderDetailId(String orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
	public String getPriceDisplay() {
		return priceDisplay;
	}
	public void setPriceDisplay(String priceDisplay) {
		this.priceDisplay = priceDisplay;
	}
	public String getShortDescDisplay() {
		return shortDescDisplay;
	}
	public void setShortDescDisplay(String shortDescDisplay) {
		this.shortDescDisplay = shortDescDisplay;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getItemInvoiceQty() {
		return itemInvoiceQty;
	}
	public void setItemInvoiceQty(String itemInvoiceQty) {
		this.itemInvoiceQty = itemInvoiceQty;
	}
	public Vector<?> getOrderItemAttributes() {
		return orderItemAttributes;
	}
	public void setOrderItemAttributes(Vector<?> orderItemAttributes) {
		this.orderItemAttributes = orderItemAttributes;
	}
}
