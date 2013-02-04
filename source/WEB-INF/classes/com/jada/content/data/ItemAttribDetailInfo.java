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

package com.jada.content.data;

import java.util.Vector;

public class ItemAttribDetailInfo {
	String customAttribTypeCode;
	String itemAttribDetailId;
	String customAttribDetailId;
	String customAttribDesc;
	String itemAttribDetailValue;
	String customAttribOptionId;
	Vector<?> customAttribOptions;
	boolean itemQtyTrack;
	public String getItemAttribDetailId() {
		return itemAttribDetailId;
	}
	public void setItemAttribDetailId(String itemAttribDetailId) {
		this.itemAttribDetailId = itemAttribDetailId;
	}
	public String getItemAttribDetailValue() {
		return itemAttribDetailValue;
	}
	public void setItemAttribDetailValue(String itemAttribDetailValue) {
		this.itemAttribDetailValue = itemAttribDetailValue;
	}
	public String getCustomAttribOptionId() {
		return customAttribOptionId;
	}
	public void setCustomAttribOptionId(String customAttribOptionId) {
		this.customAttribOptionId = customAttribOptionId;
	}
	public boolean isItemQtyTrack() {
		return itemQtyTrack;
	}
	public void setItemQtyTrack(boolean itemQtyTrack) {
		this.itemQtyTrack = itemQtyTrack;
	}
	public Vector<?> getCustomAttribOptions() {
		return customAttribOptions;
	}
	public void setCustomAttribOptions(Vector<?> customAttribOptions) {
		this.customAttribOptions = customAttribOptions;
	}
	public String getCustomAttribTypeCode() {
		return customAttribTypeCode;
	}
	public void setCustomAttribTypeCode(String customAttribTypeCode) {
		this.customAttribTypeCode = customAttribTypeCode;
	}
	public String getCustomAttribDesc() {
		return customAttribDesc;
	}
	public void setCustomAttribDesc(String customAttribDesc) {
		this.customAttribDesc = customAttribDesc;
	}
	public String getCustomAttribDetailId() {
		return customAttribDetailId;
	}
	public void setCustomAttribDetailId(String customAttribDetailId) {
		this.customAttribDetailId = customAttribDetailId;
	}
}
