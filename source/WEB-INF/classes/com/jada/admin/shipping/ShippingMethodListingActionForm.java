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

package com.jada.admin.shipping;

import com.jada.admin.AdminMaintActionForm;

public class ShippingMethodListingActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = 1027558928854272186L;
	String mode;
	String srShippingMethodName;
    String srPublished;
    ShippingMethodDisplayForm shippingMethods[];
    String shippingMethodIds[];
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String[] getShippingMethodIds() {
		return shippingMethodIds;
	}
	public void setShippingMethodIds(String[] shippingMethodIds) {
		this.shippingMethodIds = shippingMethodIds;
	}
	public ShippingMethodDisplayForm[] getShippingMethods() {
		return shippingMethods;
	}
	public void setShippingMethods(ShippingMethodDisplayForm[] shippingMethods) {
		this.shippingMethods = shippingMethods;
	}
	public String getSrPublished() {
		return srPublished;
	}
	public void setSrPublished(String srPublished) {
		this.srPublished = srPublished;
	}
	public String getSrShippingMethodName() {
		return srShippingMethodName;
	}
	public void setSrShippingMethodName(String srShippingMethodName) {
		this.srShippingMethodName = srShippingMethodName;
	}
}
