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

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class InvoiceMaintActionForm extends OrderMaintBaseForm {
	private static final long serialVersionUID = -7636173697710196979L;
	String invoiceHeaderId;
	String orderTrackingMessage;
	boolean orderTrackingInternal;
	InvoiceHeaderDisplayForm invoiceHeader;
	OrderTrackingDisplayForm orderTrackings[];
	InvoiceDetailDisplayForm invoiceDetails[];
	Vector<?> invoiceTaxes;
	String inputShippingTotal;
	float shippingTotal;
	boolean newInvoice;
	boolean editable;
	boolean allowCapture;
	boolean allowVoid;
	public OrderTrackingDisplayForm getOrderTracking(int index) {
		return orderTrackings[index];
	}
	public InvoiceDetailDisplayForm getInvoiceDetail(int index) {
		return invoiceDetails[index];
	}
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		String ORDERTRACKING = "orderTracking.*orderTrackingId";
		String INVOICEDETAIL = "invoiceDetail.*orderItemDetailId";
		int orderTrackingCount = 0;
		int invoiceDetailCount = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(ORDERTRACKING)) {
				orderTrackingCount++;
			}
			if (name.matches(INVOICEDETAIL)) {
				invoiceDetailCount++;
			}
		}
		orderTrackings = new OrderTrackingDisplayForm[orderTrackingCount];
		for (int i = 0; i < orderTrackings.length; i++) {
			orderTrackings[i] = new OrderTrackingDisplayForm();
		}
		invoiceDetails = new InvoiceDetailDisplayForm[invoiceDetailCount];
		for (int i = 0; i < invoiceDetails.length; i++) {
			invoiceDetails[i] = new InvoiceDetailDisplayForm();
		}
	}
	public boolean isOrderTrackingInternal() {
		return orderTrackingInternal;
	}
	public void setOrderTrackingInternal(boolean orderTrackingInternal) {
		this.orderTrackingInternal = orderTrackingInternal;
	}
	public OrderTrackingDisplayForm[] getOrderTrackings() {
		return orderTrackings;
	}
	public void setOrderTrackings(OrderTrackingDisplayForm[] orderTrackings) {
		this.orderTrackings = orderTrackings;
	}
	public String getInvoiceHeaderId() {
		return invoiceHeaderId;
	}
	public void setInvoiceHeaderId(String invoiceHeaderId) {
		this.invoiceHeaderId = invoiceHeaderId;
	}
	public InvoiceDetailDisplayForm[] getInvoiceDetails() {
		return invoiceDetails;
	}
	public void setInvoiceDetails(InvoiceDetailDisplayForm[] invoiceDetails) {
		this.invoiceDetails = invoiceDetails;
	}
	public float getShippingTotal() {
		return shippingTotal;
	}
	public void setShippingTotal(float shippingTotal) {
		this.shippingTotal = shippingTotal;
	}
	public String getOrderTrackingMessage() {
		return orderTrackingMessage;
	}
	public void setOrderTrackingMessage(String orderTrackingMessage) {
		this.orderTrackingMessage = orderTrackingMessage;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public InvoiceHeaderDisplayForm getInvoiceHeader() {
		return invoiceHeader;
	}
	public void setInvoiceHeader(InvoiceHeaderDisplayForm invoiceHeader) {
		this.invoiceHeader = invoiceHeader;
	}
	public boolean isNewInvoice() {
		return newInvoice;
	}
	public void setNewInvoice(boolean newInvoice) {
		this.newInvoice = newInvoice;
	}
	public Vector<?> getInvoiceTaxes() {
		return invoiceTaxes;
	}
	public void setInvoiceTaxes(Vector<?> invoiceTaxes) {
		this.invoiceTaxes = invoiceTaxes;
	}
	public boolean isAllowCapture() {
		return allowCapture;
	}
	public void setAllowCapture(boolean allowCapture) {
		this.allowCapture = allowCapture;
	}
	public boolean isAllowVoid() {
		return allowVoid;
	}
	public void setAllowVoid(boolean allowVoid) {
		this.allowVoid = allowVoid;
	}
	public String getInputShippingTotal() {
		return inputShippingTotal;
	}
	public void setInputShippingTotal(String inputShippingTotal) {
		this.inputShippingTotal = inputShippingTotal;
	}
}
