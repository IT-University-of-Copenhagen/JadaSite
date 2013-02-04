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

public class ShippingMethodRegionDisplayForm {
	String shippingMethodRegionId;
	String shippingMethodId;
	String shippingRegionId;
	String shippingRegionName;
	boolean shippingMethodRegionIsPublisheds;
	boolean modify;
	public boolean isModify() {
		return modify;
	}
	public void setModify(boolean modify) {
		this.modify = modify;
	}
	public boolean isShippingMethodRegionIsPublisheds() {
		return shippingMethodRegionIsPublisheds;
	}
	public void setShippingMethodRegionIsPublisheds(
			boolean shippingMethodRegionIsPublisheds) {
		this.shippingMethodRegionIsPublisheds = shippingMethodRegionIsPublisheds;
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
	public String getShippingMethodRegionId() {
		return shippingMethodRegionId;
	}
	public void setShippingMethodRegionId(String shippingMethodRegionId) {
		this.shippingMethodRegionId = shippingMethodRegionId;
	}
	public String getShippingMethodId() {
		return shippingMethodId;
	}
	public void setShippingMethodId(String shippingMethodId) {
		this.shippingMethodId = shippingMethodId;
	}
}
