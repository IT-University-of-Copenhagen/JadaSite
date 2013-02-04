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

public class InvoiceDetailDisplayForm {
	String orderItemDetailId;
	String invoiceDetailId;
	String itemId;
	String itemNum;
	String itemSkuCd;
	String itemShortDesc;
	String itemTierQty;
	String itemTierPrice;
	String itemOrderQty;
	String itemInvoiceQty;
	String itemCreditQty;
	String itemShipQty;
	String inputInvoiceQty;
	String inputInvoiceQtyError;
	String itemInvoiceAmount;
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
	public String getInvoiceDetailId() {
		return invoiceDetailId;
	}
	public void setInvoiceDetailId(String invoiceDetailId) {
		this.invoiceDetailId = invoiceDetailId;
	}
	public String getOrderItemDetailId() {
		return orderItemDetailId;
	}
	public void setOrderItemDetailId(String orderItemDetailId) {
		this.orderItemDetailId = orderItemDetailId;
	}
	public String getInputInvoiceQty() {
		return inputInvoiceQty;
	}
	public void setInputInvoiceQty(String inputInvoiceQty) {
		this.inputInvoiceQty = inputInvoiceQty;
	}
	public String getItemInvoiceAmount() {
		return itemInvoiceAmount;
	}
	public void setItemInvoiceAmount(String itemInvoiceAmount) {
		this.itemInvoiceAmount = itemInvoiceAmount;
	}
	public String getInputInvoiceQtyError() {
		return inputInvoiceQtyError;
	}
	public void setInputInvoiceQtyError(String inputInvoiceQtyError) {
		this.inputInvoiceQtyError = inputInvoiceQtyError;
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
