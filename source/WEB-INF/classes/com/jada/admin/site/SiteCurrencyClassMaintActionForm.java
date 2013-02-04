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

package com.jada.admin.site;

import org.apache.struts.util.LabelValueBean;

import com.jada.admin.AdminMaintActionForm;

public class SiteCurrencyClassMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = 8631833194928976445L;
	String mode;
	String siteCurrencyClassName;
	String locale;
	String currencyId;
	String systemRecord;
	LabelValueBean locales[];
	LabelValueBean currencies[];
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getSiteCurrencyClassName() {
		return siteCurrencyClassName;
	}
	public void setSiteCurrencyClassName(String siteCurrencyClassName) {
		this.siteCurrencyClassName = siteCurrencyClassName;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public LabelValueBean[] getLocales() {
		return locales;
	}
	public void setLocales(LabelValueBean[] locales) {
		this.locales = locales;
	}
	public String getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}
	public LabelValueBean[] getCurrencies() {
		return currencies;
	}
	public void setCurrencies(LabelValueBean[] currencies) {
		this.currencies = currencies;
	}
	public String getSystemRecord() {
		return systemRecord;
	}
	public void setSystemRecord(String systemRecord) {
		this.systemRecord = systemRecord;
	}
}
