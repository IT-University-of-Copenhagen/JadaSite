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

import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import com.jada.admin.AdminMaintActionForm;

public class TaxRegionMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -1770049277261348126L;
	String mode;
	String taxRegionId;
	String taxRegionDesc;
	String shippingProductClassId;
	LabelValueBean productClassList[];
	String jsonProductClasses;
	String taxRegionProductId;
	String productClassId;
	String custClassId;
	String taxId;
	String taxRegionProductCustId;
	String taxRegionProductCustTaxIds[];
	String[] countryIds;
	String[] stateIds;
	String taxRegionZipId;
	String[] taxRegionZipIds;
	String zipCodeStart;
	String zipCodeEnd;
	boolean published;
	public void reset(ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
		published = false;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getTaxRegionDesc() {
		return taxRegionDesc;
	}
	public void setTaxRegionDesc(String taxRegionDesc) {
		this.taxRegionDesc = taxRegionDesc;
	}
	public String getTaxRegionId() {
		return taxRegionId;
	}
	public void setTaxRegionId(String taxRegionId) {
		this.taxRegionId = taxRegionId;
	}
	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
	}
	public String getTaxRegionProductId() {
		return taxRegionProductId;
	}
	public void setTaxRegionProductId(String taxRegionProductId) {
		this.taxRegionProductId = taxRegionProductId;
	}
	public String getTaxRegionProductCustId() {
		return taxRegionProductCustId;
	}
	public void setTaxRegionProductCustId(String taxRegionProductCustId) {
		this.taxRegionProductCustId = taxRegionProductCustId;
	}
	public String getProductClassId() {
		return productClassId;
	}
	public void setProductClassId(String productClassId) {
		this.productClassId = productClassId;
	}
	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	public String[] getTaxRegionProductCustTaxIds() {
		return taxRegionProductCustTaxIds;
	}
	public void setTaxRegionProductCustTaxIds(String[] taxRegionProductCustTaxIds) {
		this.taxRegionProductCustTaxIds = taxRegionProductCustTaxIds;
	}
	public String getTaxRegionZipId() {
		return taxRegionZipId;
	}
	public void setTaxRegionZipId(String taxRegionZipId) {
		this.taxRegionZipId = taxRegionZipId;
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
	public String[] getTaxRegionZipIds() {
		return taxRegionZipIds;
	}
	public void setTaxRegionZipIds(String[] taxRegionZipIds) {
		this.taxRegionZipIds = taxRegionZipIds;
	}
	public String getJsonProductClasses() {
		return jsonProductClasses;
	}
	public void setJsonProductClasses(String jsonProductClasses) {
		this.jsonProductClasses = jsonProductClasses;
	}
	public String getCustClassId() {
		return custClassId;
	}
	public void setCustClassId(String custClassId) {
		this.custClassId = custClassId;
	}
	public String getShippingProductClassId() {
		return shippingProductClassId;
	}
	public void setShippingProductClassId(String shippingProductClassId) {
		this.shippingProductClassId = shippingProductClassId;
	}
	public LabelValueBean[] getProductClassList() {
		return productClassList;
	}
	public void setProductClassList(LabelValueBean[] productClassList) {
		this.productClassList = productClassList;
	}
}
