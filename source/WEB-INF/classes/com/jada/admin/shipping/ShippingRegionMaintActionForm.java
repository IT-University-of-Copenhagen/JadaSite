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

public class ShippingRegionMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = 2576477799255338697L;
	String mode;
	CountryStateTableBean countryStateTable[];
	CountryStateTableBean regionCountryStateTable[];
	String countryIds[];
	String stateIds[];
	String removeCountries[];
	String removeStates[];
	String shippingRegionId;
	String shippingRegionName;
	String shippingRegionZipIds[];
	String shippingRegionZipId;
	String zipCodeStart;
	String zipCodeEnd;
	String zipCodeExpression;
	boolean published;
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public CountryStateTableBean[] getCountryStateTable() {
		return countryStateTable;
	}
	public void setCountryStateTable(CountryStateTableBean[] countryStateTable) {
		this.countryStateTable = countryStateTable;
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
	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
	}
	public String[] getCountryIds() {
		return countryIds;
	}
	public void setCountryIds(String[] countryIds) {
		this.countryIds = countryIds;
	}
	public String[] getStateIds() {
		return stateIds;
	}
	public void setStateIds(String[] stateIds) {
		this.stateIds = stateIds;
	}
	public String getZipCodeStart() {
		return zipCodeStart;
	}
	public void setZipCodeStart(String zipCodeStart) {
		this.zipCodeStart = zipCodeStart;
	}
	public String getZipCodeEnd() {
		return zipCodeEnd;
	}
	public void setZipCodeEnd(String zipCodeEnd) {
		this.zipCodeEnd = zipCodeEnd;
	}
	public String getZipCodeExpression() {
		return zipCodeExpression;
	}
	public void setZipCodeExpression(String zipCodeExpression) {
		this.zipCodeExpression = zipCodeExpression;
	}
	public String getShippingRegionZipId() {
		return shippingRegionZipId;
	}
	public void setShippingRegionZipId(String shippingRegionZipId) {
		this.shippingRegionZipId = shippingRegionZipId;
	}
	public String[] getShippingRegionZipIds() {
		return shippingRegionZipIds;
	}
	public void setShippingRegionZipIds(String[] shippingRegionZipIds) {
		this.shippingRegionZipIds = shippingRegionZipIds;
	}
}
