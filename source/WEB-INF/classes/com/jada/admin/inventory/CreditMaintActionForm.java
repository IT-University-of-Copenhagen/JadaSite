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

public class CreditMaintActionForm extends OrderMaintBaseForm {
	private static final long serialVersionUID = 7966250362932589644L;
	String invoiceHeaderId;
	String creditHeaderId;
	String orderTrackingMessage;
	boolean orderTrackingInternal;
	CreditHeaderDisplayForm creditHeader;
	OrderTrackingDisplayForm orderTrackings[];
	CreditDetailDisplayForm creditDetails[];
	Vector<?> creditTaxes;
	String inputShippingTotal;
	float shippingTotal;
	boolean newCredit;
	boolean editable;
	boolean allowCapture;
	boolean allowVoid;
	public OrderTrackingDisplayForm getOrderTracking(int index) {
		return orderTrackings[index];
	}
	public CreditDetailDisplayForm getCreditDetail(int index) {
		return creditDetails[index];
	}
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		String ORDERTRACKING = "orderTracking.*orderTrackingId";
		String INVOICEDETAIL = "creditDetail.*orderItemDetailId";
		int orderTrackingCount = 0;
		int creditDetailCount = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(ORDERTRACKING)) {
				orderTrackingCount++;
			}
			if (name.matches(INVOICEDETAIL)) {
				creditDetailCount++;
			}
		}
		orderTrackings = new OrderTrackingDisplayForm[orderTrackingCount];
		for (int i = 0; i < orderTrackings.length; i++) {
			orderTrackings[i] = new OrderTrackingDisplayForm();
		}
		creditDetails = new CreditDetailDisplayForm[creditDetailCount];
		for (int i = 0; i < creditDetails.length; i++) {
			creditDetails[i] = new CreditDetailDisplayForm();
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
	public String getCreditHeaderId() {
		return creditHeaderId;
	}
	public void setCreditHeaderId(String creditHeaderId) {
		this.creditHeaderId = creditHeaderId;
	}
	public CreditDetailDisplayForm[] getCreditDetails() {
		return creditDetails;
	}
	public void setCreditDetails(CreditDetailDisplayForm[] creditDetails) {
		this.creditDetails = creditDetails;
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
	public CreditHeaderDisplayForm getCreditHeader() {
		return creditHeader;
	}
	public void setCreditHeader(CreditHeaderDisplayForm creditHeader) {
		this.creditHeader = creditHeader;
	}
	public boolean isNewCredit() {
		return newCredit;
	}
	public void setNewCredit(boolean newCredit) {
		this.newCredit = newCredit;
	}
	public Vector<?> getCreditTaxes() {
		return creditTaxes;
	}
	public void setCreditTaxes(Vector<?> creditTaxes) {
		this.creditTaxes = creditTaxes;
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
	public String getInvoiceHeaderId() {
		return invoiceHeaderId;
	}
	public void setInvoiceHeaderId(String invoiceHeaderId) {
		this.invoiceHeaderId = invoiceHeaderId;
	}
}
