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

public class OrderMaintActionForm extends OrderMaintBaseForm {
	private static final long serialVersionUID = -1515260200312153795L;
	String orderTrackingMessage;
	boolean orderTrackingInternal;
	OrderTrackingDisplayForm orderTrackings[];
	public OrderTrackingDisplayForm getOrderTracking(int index) {
		return orderTrackings[index];
	}
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		String ORDERTRACKING = "orderTracking.*orderTrackingId";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(ORDERTRACKING)) {
				count++;
			}
		}
		orderTrackings = new OrderTrackingDisplayForm[count];
		for (int i = 0; i < orderTrackings.length; i++) {
			orderTrackings[i] = new OrderTrackingDisplayForm();
		}
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
	public OrderTrackingDisplayForm[] getOrderTrackings() {
		return orderTrackings;
	}
	public void setOrderTrackings(OrderTrackingDisplayForm[] orderTrackings) {
		this.orderTrackings = orderTrackings;
	}
}
