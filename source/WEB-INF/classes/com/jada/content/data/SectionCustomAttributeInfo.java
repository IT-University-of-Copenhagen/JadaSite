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

public class SectionCustomAttributeInfo {
	String customAttribId;
	String customAttribDesc;
	boolean selected;
	SectionCustomAttributeOptionInfo selectedCustomAttribOptionInfo;
	SectionCustomAttributeOptionInfo customAttribOptionInfos[];
	public String getCustomAttribId() {
		return customAttribId;
	}
	public void setCustomAttribId(String customAttribId) {
		this.customAttribId = customAttribId;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public SectionCustomAttributeOptionInfo getSelectedCustomAttribOptionInfo() {
		return selectedCustomAttribOptionInfo;
	}
	public void setSelectedCustomAttribOptionInfo(
			SectionCustomAttributeOptionInfo selectedCustomAttribOptionInfo) {
		this.selectedCustomAttribOptionInfo = selectedCustomAttribOptionInfo;
	}
	public SectionCustomAttributeOptionInfo[] getCustomAttribOptionInfos() {
		return customAttribOptionInfos;
	}
	public void setCustomAttribOptionInfos(
			SectionCustomAttributeOptionInfo[] customAttribOptionInfos) {
		this.customAttribOptionInfos = customAttribOptionInfos;
	}
	public String getCustomAttribDesc() {
		return customAttribDesc;
	}
	public void setCustomAttribDesc(String customAttribDesc) {
		this.customAttribDesc = customAttribDesc;
	}
}
