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

public class InvoiceHeaderDisplayForm {
	private String invoiceHeaderId;
	private String invoiceNum;
	private String shippingTotal;
	private String invoiceTotal;
	private String invoiceStatus;
	private String invoiceDate;
	private String itemOrderQty;
	private String itemInvoiceQty;
	private String itemCreditQty;
	private String itemShipQty;
	private String itemInvoiceAmount;
	private String inputInvoiceQty;
	private PaymentTranDisplayForm paymentTran;
	private PaymentTranDisplayForm voidPaymentTran;
	private Vector<?> orderTrackings;
	public String getInvoiceHeaderId() {
		return invoiceHeaderId;
	}
	public void setInvoiceHeaderId(String invoiceHeaderId) {
		this.invoiceHeaderId = invoiceHeaderId;
	}
	public String getInvoiceNum() {
		return invoiceNum;
	}
	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}
	public String getShippingTotal() {
		return shippingTotal;
	}
	public void setShippingTotal(String shippingTotal) {
		this.shippingTotal = shippingTotal;
	}
	public String getInvoiceTotal() {
		return invoiceTotal;
	}
	public void setInvoiceTotal(String invoiceTotal) {
		this.invoiceTotal = invoiceTotal;
	}
	public String getInvoiceStatus() {
		return invoiceStatus;
	}
	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
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
	public String getItemInvoiceAmount() {
		return itemInvoiceAmount;
	}
	public void setItemInvoiceAmount(String itemInvoiceAmount) {
		this.itemInvoiceAmount = itemInvoiceAmount;
	}
	public String getInputInvoiceQty() {
		return inputInvoiceQty;
	}
	public void setInputInvoiceQty(String inputInvoiceQty) {
		this.inputInvoiceQty = inputInvoiceQty;
	}
}
