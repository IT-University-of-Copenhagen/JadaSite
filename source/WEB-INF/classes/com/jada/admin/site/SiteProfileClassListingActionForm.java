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

public class SiteProfileClassListingActionForm extends AdminListingActionForm {
	private static final long serialVersionUID = -108209139379409519L;
	String mode;
	String srSiteProfileClassName;
    SiteProfileClassDisplayForm siteProfileClasses[];
    public SiteProfileClassDisplayForm getSiteProfileClass(int index) {
    	return siteProfileClasses[index];
    }
	public void reset(ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
		String ITEMDETAIL = "siteProfileClass.*siteProfileClassId";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(ITEMDETAIL)) {
				count++;
			}
		}
		siteProfileClasses = new SiteProfileClassDisplayForm[count];
		for (int i = 0; i < siteProfileClasses.length; i++) {
			siteProfileClasses[i] = new SiteProfileClassDisplayForm();
		}
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getSrSiteProfileClassName() {
		return srSiteProfileClassName;
	}
	public void setSrSiteProfileClassName(String srSiteProfileClassName) {
		this.srSiteProfileClassName = srSiteProfileClassName;
	}
	public SiteProfileClassDisplayForm[] getSiteProfileClasses() {
		return siteProfileClasses;
	}
	public void setSiteProfileClasses(
			SiteProfileClassDisplayForm[] siteProfileClasses) {
		this.siteProfileClasses = siteProfileClasses;
	}
}
