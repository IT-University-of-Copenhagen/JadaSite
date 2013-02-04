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

import java.util.Enumeration;

import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminListingActionForm;

public class SiteCurrencyClassListingActionForm extends AdminListingActionForm {
	private static final long serialVersionUID = 3853163036712058850L;
	String mode;
	String srSiteCurrencyClassName;
    SiteCurrencyClassDisplayForm siteCurrencyClasses[];
    public SiteCurrencyClassDisplayForm getSiteCurrencyClass(int index) {
    	return siteCurrencyClasses[index];
    }
	public void reset(ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
		String ITEMDETAIL = "siteCurrencyClass.*siteCurrencyClassId";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(ITEMDETAIL)) {
				count++;
			}
		}
		siteCurrencyClasses = new SiteCurrencyClassDisplayForm[count];
		for (int i = 0; i < siteCurrencyClasses.length; i++) {
			siteCurrencyClasses[i] = new SiteCurrencyClassDisplayForm();
		}
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getSrSiteCurrencyClassName() {
		return srSiteCurrencyClassName;
	}
	public void setSrSiteCurrencyClassName(String srSiteCurrencyClassName) {
		this.srSiteCurrencyClassName = srSiteCurrencyClassName;
	}
	public SiteCurrencyClassDisplayForm[] getSiteCurrencyClasses() {
		return siteCurrencyClasses;
	}
	public void setSiteCurrencyClasses(
			SiteCurrencyClassDisplayForm[] siteCurrencyClasses) {
		this.siteCurrencyClasses = siteCurrencyClasses;
	}
}
