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

public class CustomAttributeOptionDisplayForm {
	boolean remove;
	String customAttribOptionId;
	String customAttribValue;
	String customAttribValueError;
	boolean customAttribValueLangFlag;
	String customAttribValueLang;
	boolean customAttribValueCurrFlag;
	String customAttribValueCurr;
	String customAttribSkuCode;
	String customAttribSkuCodeError;
	public String getCustomAttribOptionId() {
		return customAttribOptionId;
	}
	public void setCustomAttribOptionId(String customAttribOptionId) {
		this.customAttribOptionId = customAttribOptionId;
	}
	public String getCustomAttribValue() {
		return customAttribValue;
	}
	public void setCustomAttribValue(String customAttribValue) {
		this.customAttribValue = customAttribValue;
	}
	public String getCustomAttribSkuCode() {
		return customAttribSkuCode;
	}
	public void setCustomAttribSkuCode(String customAttribSkuCode) {
		this.customAttribSkuCode = customAttribSkuCode;
	}
	public boolean isCustomAttribValueLangFlag() {
		return customAttribValueLangFlag;
	}
	public void setCustomAttribValueLangFlag(boolean customAttribValueLangFlag) {
		this.customAttribValueLangFlag = customAttribValueLangFlag;
	}
	public String getCustomAttribValueLang() {
		return customAttribValueLang;
	}
	public void setCustomAttribValueLang(String customAttribValueLang) {
		this.customAttribValueLang = customAttribValueLang;
	}
	public boolean isRemove() {
		return remove;
	}
	public void setRemove(boolean remove) {
		this.remove = remove;
	}
	public String getCustomAttribValueError() {
		return customAttribValueError;
	}
	public void setCustomAttribValueError(String customAttribValueError) {
		this.customAttribValueError = customAttribValueError;
	}
	public String getCustomAttribSkuCodeError() {
		return customAttribSkuCodeError;
	}
	public void setCustomAttribSkuCodeError(String customAttribSkuCodeError) {
		this.customAttribSkuCodeError = customAttribSkuCodeError;
	}
	public boolean isCustomAttribValueCurrFlag() {
		return customAttribValueCurrFlag;
	}
	public void setCustomAttribValueCurrFlag(boolean customAttribValueCurrFlag) {
		this.customAttribValueCurrFlag = customAttribValueCurrFlag;
	}
	public String getCustomAttribValueCurr() {
		return customAttribValueCurr;
	}
	public void setCustomAttribValueCurr(String customAttribValueCurr) {
		this.customAttribValueCurr = customAttribValueCurr;
	}
}
