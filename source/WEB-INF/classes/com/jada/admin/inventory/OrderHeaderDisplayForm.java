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

public class OrderHeaderDisplayForm {
	private String orderHeaderId;
	private String langName;
	private String currencyCode;
	private String orderNum;
	private String custEmail;
	private String shippingTotal;
	private String shippingDiscountTotal;
	private String orderPriceTotal;
	private String orderPriceDiscountTotal;
	private String orderSubTotal;
	private String orderTaxTotal;
	private String orderTotal;
	private String paymentGatewayProvider;
	private String creditCardDesc;
	private String custCreditCardNum;
	private String shippingMethodName;
	private String orderAbundantLoc;
	private String shippingValidUntil;
	private String shippingPickUp;
	private String orderStatus;
	private String orderDate;
	private PaymentTranDisplayForm paymentTran;
	private PaymentTranDisplayForm voidPaymentTran;
	private String itemOrderQty;
	private String itemInvoiceQty;
	private String itemCreditQty;
	private String itemShipQty;
	private String itemDetailAmount;
	private String itemDetailDiscountAmount;
	private String itemDetailSubTotal;
	private String orderOtherDetailAmount;
	private Vector<?> orderTrackings;
	private boolean shippingQuote;
	private boolean orderOpen;
	private boolean orderLocked;
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getCustEmail() {
		return custEmail;
	}
	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}
	public String getShippingTotal() {
		return shippingTotal;
	}
	public void setShippingTotal(String shippingTotal) {
		this.shippingTotal = shippingTotal;
	}
	public String getShippingDiscountTotal() {
		return shippingDiscountTotal;
	}
	public void setShippingDiscountTotal(String shippingDiscountTotal) {
		this.shippingDiscountTotal = shippingDiscountTotal;
	}
	public String getOrderTotal() {
		return orderTotal;
	}
	public void setOrderTotal(String orderTotal) {
		this.orderTotal = orderTotal;
	}
	public String getPaymentGatewayProvider() {
		return paymentGatewayProvider;
	}
	public void setPaymentGatewayProvider(String paymentGatewayProvider) {
		this.paymentGatewayProvider = paymentGatewayProvider;
	}
	public String getCreditCardDesc() {
		return creditCardDesc;
	}
	public void setCreditCardDesc(String creditCardDesc) {
		this.creditCardDesc = creditCardDesc;
	}
	public String getCustCreditCardNum() {
		return custCreditCardNum;
	}
	public void setCustCreditCardNum(String custCreditCardNum) {
		this.custCreditCardNum = custCreditCardNum;
	}
	public String getShippingMethodName() {
		return shippingMethodName;
	}
	public void setShippingMethodName(String shippingMethodName) {
		this.shippingMethodName = shippingMethodName;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
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
	public String getOrderHeaderId() {
		return orderHeaderId;
	}
	public void setOrderHeaderId(String orderHeaderId) {
		this.orderHeaderId = orderHeaderId;
	}
	public String getOrderTaxTotal() {
		return orderTaxTotal;
	}
	public void setOrderTaxTotal(String orderTaxTotal) {
		this.orderTaxTotal = orderTaxTotal;
	}
	public String getOrderPriceTotal() {
		return orderPriceTotal;
	}
	public void setOrderPriceTotal(String orderPriceTotal) {
		this.orderPriceTotal = orderPriceTotal;
	}
	public String getOrderPriceDiscountTotal() {
		return orderPriceDiscountTotal;
	}
	public void setOrderPriceDiscountTotal(String orderPriceDiscountTotal) {
		this.orderPriceDiscountTotal = orderPriceDiscountTotal;
	}
	public String getOrderSubTotal() {
		return orderSubTotal;
	}
	public void setOrderSubTotal(String orderSubTotal) {
		this.orderSubTotal = orderSubTotal;
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
	public String getOrderOtherDetailAmount() {
		return orderOtherDetailAmount;
	}
	public void setOrderOtherDetailAmount(String orderOtherDetailAmount) {
		this.orderOtherDetailAmount = orderOtherDetailAmount;
	}
	public Vector<?> getOrderTrackings() {
		return orderTrackings;
	}
	public void setOrderTrackings(Vector<?> orderTrackings) {
		this.orderTrackings = orderTrackings;
	}
	public String getLangName() {
		return langName;
	}
	public void setLangName(String langName) {
		this.langName = langName;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getOrderAbundantLoc() {
		return orderAbundantLoc;
	}
	public void setOrderAbundantLoc(String orderAbundantLoc) {
		this.orderAbundantLoc = orderAbundantLoc;
	}
	public String getShippingValidUntil() {
		return shippingValidUntil;
	}
	public void setShippingValidUntil(String shippingValidUntil) {
		this.shippingValidUntil = shippingValidUntil;
	}
	public String getShippingPickUp() {
		return shippingPickUp;
	}
	public void setShippingPickUp(String shippingPickUp) {
		this.shippingPickUp = shippingPickUp;
	}
	public boolean isShippingQuote() {
		return shippingQuote;
	}
	public void setShippingQuote(boolean shippingQuote) {
		this.shippingQuote = shippingQuote;
	}
	public boolean isOrderOpen() {
		return orderOpen;
	}
	public void setOrderOpen(boolean orderOpen) {
		this.orderOpen = orderOpen;
	}
	public boolean isOrderLocked() {
		return orderLocked;
	}
	public void setOrderLocked(boolean orderLocked) {
		this.orderLocked = orderLocked;
	}
}
