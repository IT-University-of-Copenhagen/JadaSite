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

public class ShippingTypeMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = 6745530743687118022L;
	String mode;
	String shippingTypeId;
	String shippingTypeName;
	String systemRecord;
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getShippingTypeId() {
		return shippingTypeId;
	}
	public void setShippingTypeId(String shippingTypeId) {
		this.shippingTypeId = shippingTypeId;
	}
	public String getShippingTypeName() {
		return shippingTypeName;
	}
	public void setShippingTypeName(String shippingTypeName) {
		this.shippingTypeName = shippingTypeName;
	}
	public String getSystemRecord() {
		return systemRecord;
	}
	public void setSystemRecord(String systemRecord) {
		this.systemRecord = systemRecord;
	}
}
