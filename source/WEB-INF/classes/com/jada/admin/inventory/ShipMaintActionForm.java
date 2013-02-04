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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class ShipMaintActionForm extends OrderMaintBaseForm {
	private static final long serialVersionUID = 6690461489326901818L;
	String shipHeaderId;
	String orderTrackingMessage;
	boolean orderTrackingInternal;
	ShipHeaderDisplayForm shipHeader;
	OrderTrackingDisplayForm orderTrackings[];
	ShipDetailDisplayForm shipDetails[];
	boolean updateInventory;
	boolean newShip;
	boolean editable;
	boolean allowCapture;
	boolean allowVoid;
	public OrderTrackingDisplayForm getOrderTracking(int index) {
		return orderTrackings[index];
	}
	public ShipDetailDisplayForm getShipDetail(int index) {
		return shipDetails[index];
	}
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		String ORDERTRACKING = "orderTracking.*orderTrackingId";
		String SHIPDETAIL = "shipDetail.*orderItemDetailId";
		int orderTrackingCount = 0;
		int shipDetailCount = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(ORDERTRACKING)) {
				orderTrackingCount++;
			}
			if (name.matches(SHIPDETAIL)) {
				shipDetailCount++;
			}
		}
		orderTrackings = new OrderTrackingDisplayForm[orderTrackingCount];
		for (int i = 0; i < orderTrackings.length; i++) {
			orderTrackings[i] = new OrderTrackingDisplayForm();
		}
		shipDetails = new ShipDetailDisplayForm[shipDetailCount];
		for (int i = 0; i < shipDetails.length; i++) {
			shipDetails[i] = new ShipDetailDisplayForm();
		}
	}
	public String getShipHeaderId() {
		return shipHeaderId;
	}
	public void setShipHeaderId(String shipHeaderId) {
		this.shipHeaderId = shipHeaderId;
	}
	public String getOrderTrackingMessage() {
		return orderTrackingMessage;
	}
	public void setOrderTrackingMessage(String orderTrackingMessage) {
		this.orderTrackingMessage = orderTrackingMessage;
	}
	public boolean isOrderTrackingInternal() {
		return orderTrackingInternal;
	}
	public void setOrderTrackingInternal(boolean orderTrackingInternal) {
		this.orderTrackingInternal = orderTrackingInternal;
	}
	public ShipHeaderDisplayForm getShipHeader() {
		return shipHeader;
	}
	public void setShipHeader(ShipHeaderDisplayForm shipHeader) {
		this.shipHeader = shipHeader;
	}
	public OrderTrackingDisplayForm[] getOrderTrackings() {
		return orderTrackings;
	}
	public void setOrderTrackings(OrderTrackingDisplayForm[] orderTrackings) {
		this.orderTrackings = orderTrackings;
	}
	public ShipDetailDisplayForm[] getShipDetails() {
		return shipDetails;
	}
	public void setShipDetails(ShipDetailDisplayForm[] shipDetails) {
		this.shipDetails = shipDetails;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public boolean isNewShip() {
		return newShip;
	}
	public void setNewShip(boolean newShip) {
		this.newShip = newShip;
	}
	public boolean isUpdateInventory() {
		return updateInventory;
	}
	public void setUpdateInventory(boolean updateInventory) {
		this.updateInventory = updateInventory;
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
}