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

import com.jada.admin.AdminMaintActionForm;

public class TaxListingActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -6419103861845315648L;
	String mode;
	String srTaxCode;
    String srPublished;
    Vector<?> taxes;
    String taxIds[];
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
	public String getSrTaxCode() {
		return srTaxCode;
	}
	public void setSrTaxCode(String srTaxCode) {
		this.srTaxCode = srTaxCode;
	}
	public Vector<?> getTaxes() {
		return taxes;
	}
	public void setTaxes(Vector<?> taxes) {
		this.taxes = taxes;
	}
	public String[] getTaxIds() {
		return taxIds;
	}
	public void setTaxIds(String[] taxIds) {
		this.taxIds = taxIds;
	}
}
