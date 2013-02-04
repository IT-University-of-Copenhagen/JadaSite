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

package com.jada.admin.customAttribute;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminMaintActionForm;

public class CustomAttributeMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = 6670991744132031868L;
	String mode;
	String systemRecord;
	String customAttribId;
	String customAttribName;
	String customAttribDesc;
	String customAttribDescLang;
	boolean customAttribDescLangFlag;
	boolean itemCompare;
	String customAttribDataTypeCode;
	String customAttribDataTypeCodeDesc;
	String customAttribTypeCode;
	String customAttribTypeCodeDesc;
	boolean customAttribOptionLangFlag;
	boolean customAttribOptionCurrFlag;
	CustomAttributeOptionDisplayForm customAttribOptions[];
	String customAttribOptionIds[];
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		String id = "customAttributeOption.*customAttribOptionId";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(id)) {
				count++;
			}
		}
		customAttribOptions = new CustomAttributeOptionDisplayForm[count];
		for (int i = 0; i < customAttribOptions.length; i++) {
			customAttribOptions[i] = new CustomAttributeOptionDisplayForm();
		}
	}
	public CustomAttributeOptionDisplayForm getCustomAttributeOption(int index) {
		return customAttribOptions[index];
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getCustomAttribId() {
		return customAttribId;
	}
	public void setCustomAttribId(String customAttribId) {
		this.customAttribId = customAttribId;
	}
	public boolean isItemCompare() {
		return itemCompare;
	}
	public void setItemCompare(boolean itemCompare) {
		this.itemCompare = itemCompare;
	}
	public CustomAttributeOptionDisplayForm[] getCustomAttribOptions() {
		return customAttribOptions;
	}
	public void setCustomAttribOptions(
			CustomAttributeOptionDisplayForm[] customAttribOptions) {
		this.customAttribOptions = customAttribOptions;
	}
	public String[] getCustomAttribOptionIds() {
		return customAttribOptionIds;
	}
	public void setCustomAttribOptionIds(String[] customAttribOptionIds) {
		this.customAttribOptionIds = customAttribOptionIds;
	}
	public boolean isCustomAttribOptionLangFlag() {
		return customAttribOptionLangFlag;
	}
	public void setCustomAttribOptionLangFlag(boolean customAttribOptionLangFlag) {
		this.customAttribOptionLangFlag = customAttribOptionLangFlag;
	}
	public String getCustomAttribTypeCode() {
		return customAttribTypeCode;
	}
	public void setCustomAttribTypeCode(String customAttribTypeCode) {
		this.customAttribTypeCode = customAttribTypeCode;
	}
	public String getCustomAttribDataTypeCode() {
		return customAttribDataTypeCode;
	}
	public void setCustomAttribDataTypeCode(String customAttribDataTypeCode) {
		this.customAttribDataTypeCode = customAttribDataTypeCode;
	}
	public String getCustomAttribDataTypeCodeDesc() {
		return customAttribDataTypeCodeDesc;
	}
	public void setCustomAttribDataTypeCodeDesc(String customAttribDataTypeCodeDesc) {
		this.customAttribDataTypeCodeDesc = customAttribDataTypeCodeDesc;
	}
	public String getCustomAttribTypeCodeDesc() {
		return customAttribTypeCodeDesc;
	}
	public void setCustomAttribTypeCodeDesc(String customAttribTypeCodeDesc) {
		this.customAttribTypeCodeDesc = customAttribTypeCodeDesc;
	}
	public boolean isCustomAttribOptionCurrFlag() {
		return customAttribOptionCurrFlag;
	}
	public void setCustomAttribOptionCurrFlag(boolean customAttribOptionCurrFlag) {
		this.customAttribOptionCurrFlag = customAttribOptionCurrFlag;
	}
	public String getSystemRecord() {
		return systemRecord;
	}
	public void setSystemRecord(String systemRecord) {
		this.systemRecord = systemRecord;
	}
	public String getCustomAttribDesc() {
		return customAttribDesc;
	}
	public void setCustomAttribDesc(String customAttribDesc) {
		this.customAttribDesc = customAttribDesc;
	}
	public String getCustomAttribDescLang() {
		return customAttribDescLang;
	}
	public void setCustomAttribDescLang(String customAttribDescLang) {
		this.customAttribDescLang = customAttribDescLang;
	}
	public boolean isCustomAttribDescLangFlag() {
		return customAttribDescLangFlag;
	}
	public void setCustomAttribDescLangFlag(boolean customAttribDescLangFlag) {
		this.customAttribDescLangFlag = customAttribDescLangFlag;
	}
	public String getCustomAttribName() {
		return customAttribName;
	}
	public void setCustomAttribName(String customAttribName) {
		this.customAttribName = customAttribName;
	}
}
