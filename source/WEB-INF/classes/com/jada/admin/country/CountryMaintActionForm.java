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

import com.jada.admin.AdminMaintActionForm;

public class CountryMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -5353945649532211896L;
	String mode;
	String countryId;
	String countryCode;
	String countryName;
	StateDisplayForm states[];
	
    public StateDisplayForm[] getStates() {
		return states;
	}
	public void setStates(StateDisplayForm[] states) {
		this.states = states;
	}
	public StateDisplayForm getState(int index) {
    	return states[index];
    }
	public void reset(ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
		String STATEDETAIL = "state.*stateId";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(STATEDETAIL)) {
				count++;
			}
		}
		states = new StateDisplayForm[count];
		for (int i = 0; i < states.length; i++) {
			states[i] = new StateDisplayForm();
		}
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
}
