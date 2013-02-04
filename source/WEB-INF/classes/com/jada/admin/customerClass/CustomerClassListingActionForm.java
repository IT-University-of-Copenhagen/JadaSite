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

package com.jada.admin.customerClass;

import java.util.Vector;

import com.jada.admin.AdminMaintActionForm;

public class CustomerClassListingActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -954792876542562937L;
	String mode;
	String srCustClassName;
	String custClassIds[];
	Vector<?> custClasses;
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getSrCustClassName() {
		return srCustClassName;
	}
	public void setSrCustClassName(String srCustClassName) {
		this.srCustClassName = srCustClassName;
	}
	public String[] getCustClassIds() {
		return custClassIds;
	}
	public void setCustClassIds(String[] custClassIds) {
		this.custClassIds = custClassIds;
	}
	public Vector<?> getCustClasses() {
		return custClasses;
	}
	public void setCustClasses(Vector<?> custClasses) {
		this.custClasses = custClasses;
	}
}
