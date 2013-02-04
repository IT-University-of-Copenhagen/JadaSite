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

public class CreditHeaderDisplayForm {
	private String creditHeaderId;
	private String creditNum;
	private boolean updateInventory;
	private String shippingTotal;
	private String creditTotal;
	private String creditStatus;
	private String creditDate;
	private String itemOrderQty;
	private String itemInvoiceQty;
	private String itemCreditQty;
	private String itemShipQty;
	private String itemCreditAmount;
	private String inputCreditQty;
	private PaymentTranDisplayForm paymentTran;
	private PaymentTranDisplayForm voidPaymentTran;
	private Vector<?> orderTrackings;
	public String getCreditHeaderId() {
		return creditHeaderId;
	}
	public void setCreditHeaderId(String creditHeaderId) {
		this.creditHeaderId = creditHeaderId;
	}
	public String getCreditNum() {
		return creditNum;
	}
	public void setCreditNum(String creditNum) {
		this.creditNum = creditNum;
	}
	public boolean isUpdateInventory() {
		return updateInventory;
	}
	public void setUpdateInventory(boolean updateInventory) {
		this.updateInventory = updateInventory;
	}
	public String getShippingTotal() {
		return shippingTotal;
	}
	public void setShippingTotal(String shippingTotal) {
		this.shippingTotal = shippingTotal;
	}
	public String getCreditTotal() {
		return creditTotal;
	}
	public void setCreditTotal(String creditTotal) {
		this.creditTotal = creditTotal;
	}
	public String getCreditStatus() {
		return creditStatus;
	}
	public void setCreditStatus(String creditStatus) {
		this.creditStatus = creditStatus;
	}
	public String getCreditDate() {
		return creditDate;
	}
	public void setCreditDate(String creditDate) {
		this.creditDate = creditDate;
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
	public String getItemCreditAmount() {
		return itemCreditAmount;
	}
	public void setItemCreditAmount(String itemCreditAmount) {
		this.itemCreditAmount = itemCreditAmount;
	}
	public String getInputCreditQty() {
		return inputCreditQty;
	}
	public void setInputCreditQty(String inputCreditQty) {
		this.inputCreditQty = inputCreditQty;
	}
	public PaymentTranDisplayForm getPaymentTran() {
		return paymentTran;
	}
	public void setPaymentTran(PaymentTranDisplayForm paymentTran) {
		this.paymentTran = paymentTran;
	}
	public PaymentTranDisplayForm getVoidPaymentTran() {
		return voidPaymentTran;
	}
	public void setVoidPaymentTran(PaymentTranDisplayForm voidPaymentTran) {
		this.voidPaymentTran = voidPaymentTran;
	}
	public Vector<?> getOrderTrackings() {
		return orderTrackings;
	}
	public void setOrderTrackings(Vector<?> orderTrackings) {
		this.orderTrackings = orderTrackings;
	}
}
