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

public class ShippingRegionDisplayForm {
	String shippingRegionId;
	String shippingRegionName;
	String systemRecord;
	String published;
	boolean modify;
	public boolean isModify() {
		return modify;
	}
	public void setModify(boolean modify) {
		this.modify = modify;
	}
	public String getPublished() {
		return published;
	}
	public void setPublished(String published) {
		this.published = published;
	}
	public String getShippingRegionId() {
		return shippingRegionId;
	}
	public void setShippingRegionId(String shippingRegionId) {
		this.shippingRegionId = shippingRegionId;
	}
	public String getShippingRegionName() {
		return shippingRegionName;
	}
	public void setShippingRegionName(String shippingRegionName) {
		this.shippingRegionName = shippingRegionName;
	}
	public String getSystemRecord() {
		return systemRecord;
	}
	public void setSystemRecord(String systemRecord) {
		this.systemRecord = systemRecord;
	}
}
