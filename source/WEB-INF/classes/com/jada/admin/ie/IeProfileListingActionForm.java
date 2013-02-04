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

package com.jada.admin.ie;

import java.util.Enumeration;

import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminMaintActionForm;

public class IeProfileListingActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = 102066800516026588L;
	boolean selected;
	String srIeProfileHeaderName;
    IeProfileHeaderDisplayForm ieProfileHeaders[];
    String ieProfileHeaderIds[];
	public void reset(ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
		String IEPROFILEHEADER = "ieProfileHeader.*ieProfileHeaderId";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(IEPROFILEHEADER)) {
				count++;
			}
		}
		ieProfileHeaders = new IeProfileHeaderDisplayForm[count];
		for (int i = 0; i < ieProfileHeaders.length; i++) {
			ieProfileHeaders[i] = new IeProfileHeaderDisplayForm();
		}
	}
	public IeProfileHeaderDisplayForm getIeProfileHeader(int index) {
		return ieProfileHeaders[index];
	}
	public String getSrIeProfileHeaderName() {
		return srIeProfileHeaderName;
	}
	public void setSrIeProfileHeaderName(String srIeProfileHeaderName) {
		this.srIeProfileHeaderName = srIeProfileHeaderName;
	}
	public String[] getIeProfileHeaderIds() {
		return ieProfileHeaderIds;
	}
	public void setIeProfileHeaderIds(String[] ieProfileHeaderIds) {
		this.ieProfileHeaderIds = ieProfileHeaderIds;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public IeProfileHeaderDisplayForm[] getIeProfileHeaders() {
		return ieProfileHeaders;
	}
	public void setIeProfileHeaders(IeProfileHeaderDisplayForm[] ieProfileHeaders) {
		this.ieProfileHeaders = ieProfileHeaders;
	}
}
