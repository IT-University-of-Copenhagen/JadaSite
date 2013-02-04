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

import java.util.Vector;

import com.jada.admin.AdminListingActionForm;

public class TaxRegionListingActionForm extends AdminListingActionForm {
	private static final long serialVersionUID = 8385288940431156192L;
	String mode;
	String srTaxRegionDesc;
    String srPublished;
    Vector<?> taxRegions;
    String taxRegionIds[];
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getSrPublished() {
		return srPublished;
	}
	public void setSrPublished(String srPublished) {
		this.srPublished = srPublished;
	}
	public String getSrTaxRegionDesc() {
		return srTaxRegionDesc;
	}
	public void setSrTaxRegionDesc(String srTaxRegionDesc) {
		this.srTaxRegionDesc = srTaxRegionDesc;
	}
	public Vector<?> getTaxRegions() {
		return taxRegions;
	}
	public void setTaxRegions(Vector<?> taxRegions) {
		this.taxRegions = taxRegions;
	}
	public String[] getTaxRegionIds() {
		return taxRegionIds;
	}
	public void setTaxRegionIds(String[] taxRegionIds) {
		this.taxRegionIds = taxRegionIds;
	}
}
