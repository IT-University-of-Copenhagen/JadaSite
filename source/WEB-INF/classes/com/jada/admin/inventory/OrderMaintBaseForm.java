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

import com.jada.admin.AdminMaintActionForm;

public class OrderMaintBaseForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -8536286997923314265L;
	String orderHeaderId;
	String shippingQuoteTotal;
	String shippingValidUntil;
	OrderHeaderDisplayForm orderHeader;
	CustomerAddressDisplayForm custAddress;
	CustomerAddressDisplayForm billingAddress;
	CustomerAddressDisplayForm shippingAddress;
	Vector<?> orderItemDetails;
	Vector<?> orderOtherDetails;
	Vector<?> orderTaxes;
	Vector<?> invoiceHeaders;
	Vector<?> creditHeaders;
	Vector<?> shipHeaders;
	boolean allowInvoice;
	boolean allowCredit;
	boolean allowShip;
	boolean allowCancel;
	public OrderHeaderDisplayForm getOrderHeader() {
		return orderHeader;
	}
	public void setOrderHeader(OrderHeaderDisplayForm orderHeader) {
		this.orderHeader = orderHeader;
	}
	public CustomerAddressDisplayForm getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(CustomerAddressDisplayForm billingAddress) {
		this.billingAddress = billingAddress;
	}
	public CustomerAddressDisplayForm getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(CustomerAddressDisplayForm shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	public Vector<?> getOrderItemDetails() {
		return orderItemDetails;
	}
	public void setOrderItemDetails(Vector<?> orderItemDetails) {
		this.orderItemDetails = orderItemDetails;
	}
	public Vector<?> getOrderTaxes() {
		return orderTaxes;
	}
	public void setOrderTaxes(Vector<?> orderTaxes) {
		this.orderTaxes = orderTaxes;
	}
	public Vector<?> getOrderOtherDetails() {
		return orderOtherDetails;
	}
	public void setOrderOtherDetails(Vector<?> orderOtherDetails) {
		this.orderOtherDetails = orderOtherDetails;
	}
	public String getOrderHeaderId() {
		return orderHeaderId;
	}
	public void setOrderHeaderId(String orderHeaderId) {
		this.orderHeaderId = orderHeaderId;
	}
	public Vector<?> getInvoiceHeaders() {
		return invoiceHeaders;
	}
	public void setInvoiceHeaders(Vector<?> invoiceHeaders) {
		this.invoiceHeaders = invoiceHeaders;
	}
	public boolean isAllowInvoice() {
		return allowInvoice;
	}
	public void setAllowInvoice(boolean allowInvoice) {
		this.allowInvoice = allowInvoice;
	}
	public boolean isAllowCredit() {
		return allowCredit;
	}
	public void setAllowCredit(boolean allowCredit) {
		this.allowCredit = allowCredit;
	}
	public boolean isAllowShip() {
		return allowShip;
	}
	public void setAllowShip(boolean allowShip) {
		this.allowShip = allowShip;
	}
	public Vector<?> getCreditHeaders() {
		return creditHeaders;
	}
	public void setCreditHeaders(Vector<?> creditHeaders) {
		this.creditHeaders = creditHeaders;
	}
	public Vector<?> getShipHeaders() {
		return shipHeaders;
	}
	public void setShipHeaders(Vector<?> shipHeaders) {
		this.shipHeaders = shipHeaders;
	}
	public CustomerAddressDisplayForm getCustAddress() {
		return custAddress;
	}
	public void setCustAddress(CustomerAddressDisplayForm custAddress) {
		this.custAddress = custAddress;
	}
	public boolean isAllowCancel() {
		return allowCancel;
	}
	public void setAllowCancel(boolean allowCancel) {
		this.allowCancel = allowCancel;
	}
	public String getShippingQuoteTotal() {
		return shippingQuoteTotal;
	}
	public void setShippingQuoteTotal(String shippingQuoteTotal) {
		this.shippingQuoteTotal = shippingQuoteTotal;
	}
	public String getShippingValidUntil() {
		return shippingValidUntil;
	}
	public void setShippingValidUntil(String shippingValidUntil) {
		this.shippingValidUntil = shippingValidUntil;
	}
}
