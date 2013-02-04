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

package com.jada.admin.country;

import java.util.Enumeration;

import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminListingActionForm;


public class CountryListingActionForm extends AdminListingActionForm {
	private static final long serialVersionUID = -3584404282196046455L;
	String srCountryCode;
	String srCountryName;
	CountryDisplayForm countries[];
    public CountryDisplayForm getCountry(int index) {
    	return countries[index];
    }
	public void reset(ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
		String COUNTRYDETAIL = "country.*countryId";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(COUNTRYDETAIL)) {
				count++;
			}
		}
		countries = new CountryDisplayForm[count];
		for (int i = 0; i < countries.length; i++) {
			countries[i] = new CountryDisplayForm();
		}
	}
	public String getSrCountryCode() {
		return srCountryCode;
	}
	public void setSrCountryCode(String srCountryCode) {
		this.srCountryCode = srCountryCode;
	}
	public String getSrCountryName() {
		return srCountryName;
	}
	public void setSrCountryName(String srCountryName) {
		this.srCountryName = srCountryName;
	}
	public CountryDisplayForm[] getCountries() {
		return countries;
	}
	public void setCountries(CountryDisplayForm[] countries) {
		this.countries = countries;
	}
}
