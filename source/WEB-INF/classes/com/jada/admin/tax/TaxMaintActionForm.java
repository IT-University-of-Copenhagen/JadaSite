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

package com.jada.admin.tax;

import com.jada.admin.AdminMaintActionForm;

public class TaxMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -5448197993960966696L;
	String mode;
	CountryStateTableBean countryStateTable[];
	CountryStateTableBean regionCountryStateTable[];
	String removeCountries[];
	String removeStates[];
	String countries[];
	String states[];
	String taxId;
	String taxCode;
	boolean taxCodeLangFlag;
	String taxCodeLang;
	String taxName;
	boolean taxNameLangFlag;
	String taxNameLang;
	String taxRate;
	String published;
	public boolean isTaxCodeLangFlag() {
		return taxCodeLangFlag;
	}
	public void setTaxCodeLangFlag(boolean taxCodeLangFlag) {
		this.taxCodeLangFlag = taxCodeLangFlag;
	}
	public String getTaxCodeLang() {
		return taxCodeLang;
	}
	public void setTaxCodeLang(String taxCodeLang) {
		this.taxCodeLang = taxCodeLang;
	}
	public String[] getCountries() {
		return countries;
	}
	public void setCountries(String[] countries) {
		this.countries = countries;
	}
	public CountryStateTableBean[] getCountryStateTable() {
		return countryStateTable;
	}
	public void setCountryStateTable(CountryStateTableBean[] countryStateTable) {
		this.countryStateTable = countryStateTable;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getPublished() {
		return published;
	}
	public void setPublished(String published) {
		this.published = published;
	}
	public CountryStateTableBean[] getRegionCountryStateTable() {
		return regionCountryStateTable;
	}
	public void setRegionCountryStateTable(
			CountryStateTableBean[] regionCountryStateTable) {
		this.regionCountryStateTable = regionCountryStateTable;
	}
	public String[] getRemoveCountries() {
		return removeCountries;
	}
	public void setRemoveCountries(String[] removeCountries) {
		this.removeCountries = removeCountries;
	}
	public String[] getRemoveStates() {
		return removeStates;
	}
	public void setRemoveStates(String[] removeStates) {
		this.removeStates = removeStates;
	}
	public String[] getStates() {
		return states;
	}
	public void setStates(String[] states) {
		this.states = states;
	}
	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	public String getTaxName() {
		return taxName;
	}
	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}
	public String getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}
	public String getTaxCode() {
		return taxCode;
	}
	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
	public String getTaxNameLang() {
		return taxNameLang;
	}
	public void setTaxNameLang(String taxNameLang) {
		this.taxNameLang = taxNameLang;
	}
	public boolean isTaxNameLangFlag() {
		return taxNameLangFlag;
	}
	public void setTaxNameLangFlag(boolean taxNameLangFlag) {
		this.taxNameLangFlag = taxNameLangFlag;
	}
}
