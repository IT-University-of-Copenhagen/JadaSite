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

package com.jada.myaccount.order;

public class TrackingDisplayForm {
	String orderTrackingId;
	String orderTrackingCode;
	String orderTrackingMessage;
	String orderTrackingInternal;
	String orderTrackingDatetime;
	public String getOrderTrackingCode() {
		return orderTrackingCode;
	}
	public void setOrderTrackingCode(String orderTrackingCode) {
		this.orderTrackingCode = orderTrackingCode;
	}
	public String getOrderTrackingDatetime() {
		return orderTrackingDatetime;
	}
	public void setOrderTrackingDatetime(String orderTrackingDatetime) {
		this.orderTrackingDatetime = orderTrackingDatetime;
	}
	public String getOrderTrackingMessage() {
		return orderTrackingMessage;
	}
	public void setOrderTrackingMessage(String orderTrackingMessage) {
		this.orderTrackingMessage = orderTrackingMessage;
	}
	public String getOrderTrackingInternal() {
		return orderTrackingInternal;
	}
	public void setOrderTrackingInternal(String orderTrackingInternal) {
		this.orderTrackingInternal = orderTrackingInternal;
	}
	public String getOrderTrackingId() {
		return orderTrackingId;
	}
	public void setOrderTrackingId(String orderTrackingId) {
		this.orderTrackingId = orderTrackingId;
	}
}
