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

package com.jada.admin.item;

import org.apache.struts.util.LabelValueBean;

public class ItemAttributeDetailDisplayForm {
	String itemAttribDetailId;
	String itemAttribDetailValue;
	String itemAttribDetailValueLang;
	boolean itemAttribDetailValueLangFlag;
	String customAttribId;
	String customAttribName;
	String customAttribDetailId;
	String customAttribTypeCode;
	LabelValueBean customAttribOptions[];
	String customAttribOptionId;
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
	public String getCustomAttribDetailId() {
		return customAttribDetailId;
	}
	public void setCustomAttribDetailId(String customAttribDetailId) {
		this.customAttribDetailId = customAttribDetailId;
	}
	public String getItemAttribDetailValueLang() {
		return itemAttribDetailValueLang;
	}
	public void setItemAttribDetailValueLang(String itemAttribDetailValueLang) {
		this.itemAttribDetailValueLang = itemAttribDetailValueLang;
	}
	public boolean isItemAttribDetailValueLangFlag() {
		return itemAttribDetailValueLangFlag;
	}
	public void setItemAttribDetailValueLangFlag(
			boolean itemAttribDetailValueLangFlag) {
		this.itemAttribDetailValueLangFlag = itemAttribDetailValueLangFlag;
	}
	public String getCustomAttribTypeCode() {
		return customAttribTypeCode;
	}
	public void setCustomAttribTypeCode(String customAttribTypeCode) {
		this.customAttribTypeCode = customAttribTypeCode;
	}
	public String getCustomAttribId() {
		return customAttribId;
	}
	public void setCustomAttribId(String customAttribId) {
		this.customAttribId = customAttribId;
	}
	public String getCustomAttribOptionId() {
		return customAttribOptionId;
	}
	public void setCustomAttribOptionId(String customAttribOptionId) {
		this.customAttribOptionId = customAttribOptionId;
	}
	public LabelValueBean[] getCustomAttribOptions() {
		return customAttribOptions;
	}
	public void setCustomAttribOptions(LabelValueBean[] customAttribOptions) {
		this.customAttribOptions = customAttribOptions;
	}
	public String getCustomAttribName() {
		return customAttribName;
	}
	public void setCustomAttribName(String customAttribName) {
		this.customAttribName = customAttribName;
	}
}
