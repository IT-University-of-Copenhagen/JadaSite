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

package com.jada.admin.inventory;

import java.util.Vector;

public class OrderItemDetailDisplayForm {
	String orderItemDetailId;
	String itemId;
	String itemSkuCd;
	String itemNum;
	String itemShortDesc;
	String itemTierQty;
	String itemTierPrice;
	String itemOrderQty;
	String itemInvoiceQty;
	String itemCreditQty;
	String itemShipQty;
	int itemSuggestInvoiceQty;
	int itemSuggestCreditQty;
	int itemSuggestShipQty;
	String itemDetailAmount;
	String itemDetailDiscountAmount;
	String itemDetailSubTotal;
	Vector<?> orderItemAttributes;
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemNum() {
		return itemNum;
	}
	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}
	public String getItemShortDesc() {
		return itemShortDesc;
	}
	public void setItemShortDesc(String itemShortDesc) {
		this.itemShortDesc = itemShortDesc;
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
	public String getItemOrderQty() {
		return itemOrderQty;
	}
	public void setItemOrderQty(String itemOrderQty) {
		this.itemOrderQty = itemOrderQty;
	}
	public String getItemInvoiceQty() {
		return itemInvoiceQty;
	}
	public void setItemInvoiceQty(String itemInvoiceQty) {
		this.itemInvoiceQty = itemInvoiceQty;
	}
	public String getItemCreditQty() {
		return itemCreditQty;
	}
	public void setItemCreditQty(String itemCreditQty) {
		this.itemCreditQty = itemCreditQty;
	}
	public String getItemShipQty() {
		return itemShipQty;
	}
	public void setItemShipQty(String itemShipQty) {
		this.itemShipQty = itemShipQty;
	}
	public String getItemDetailAmount() {
		return itemDetailAmount;
	}
	public void setItemDetailAmount(String itemDetailAmount) {
		this.itemDetailAmount = itemDetailAmount;
	}
	public String getItemDetailDiscountAmount() {
		return itemDetailDiscountAmount;
	}
	public void setItemDetailDiscountAmount(String itemDetailDiscountAmount) {
		this.itemDetailDiscountAmount = itemDetailDiscountAmount;
	}
	public String getItemDetailSubTotal() {
		return itemDetailSubTotal;
	}
	public void setItemDetailSubTotal(String itemDetailSubTotal) {
		this.itemDetailSubTotal = itemDetailSubTotal;
	}
	public int getItemSuggestInvoiceQty() {
		return itemSuggestInvoiceQty;
	}
	public void setItemSuggestInvoiceQty(int itemSuggestInvoiceQty) {
		this.itemSuggestInvoiceQty = itemSuggestInvoiceQty;
	}
	public int getItemSuggestCreditQty() {
		return itemSuggestCreditQty;
	}
	public void setItemSuggestCreditQty(int itemSuggestCreditQty) {
		this.itemSuggestCreditQty = itemSuggestCreditQty;
	}
	public int getItemSuggestShipQty() {
		return itemSuggestShipQty;
	}
	public void setItemSuggestShipQty(int itemSuggestShipQty) {
		this.itemSuggestShipQty = itemSuggestShipQty;
	}
	public String getOrderItemDetailId() {
		return orderItemDetailId;
	}
	public void setOrderItemDetailId(String orderItemDetailId) {
		this.orderItemDetailId = orderItemDetailId;
	}
	public Vector<?> getOrderItemAttributes() {
		return orderItemAttributes;
	}
	public void setOrderItemAttributes(Vector<?> orderItemAttributes) {
		this.orderItemAttributes = orderItemAttributes;
	}
	public String getItemSkuCd() {
		return itemSkuCd;
	}
	public void setItemSkuCd(String itemSkuCd) {
		this.itemSkuCd = itemSkuCd;
	}
}
