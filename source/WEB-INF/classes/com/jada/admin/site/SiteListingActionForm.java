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

import java.util.Vector;

import com.jada.admin.AdminListingActionForm;

public class SiteListingActionForm extends AdminListingActionForm {
	private static final long serialVersionUID = 6054675267229134515L;
	String mode;
	String srSiteId;
	String srSiteDesc;
    String srActive;
    Vector<?> sites;
    String siteIds[];
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String[] getSiteIds() {
		return siteIds;
	}
	public void setSiteIds(String[] siteIds) {
		this.siteIds = siteIds;
	}
	public Vector<?> getSites() {
		return sites;
	}
	public void setSites(Vector<?> sites) {
		this.sites = sites;
	}
	public String getSrActive() {
		return srActive;
	}
	public void setSrActive(String srActive) {
		this.srActive = srActive;
	}
	public String getSrSiteId() {
		return srSiteId;
	}
	public void setSrSiteId(String srSiteId) {
		this.srSiteId = srSiteId;
	}
	public String getSrSiteDesc() {
		return srSiteDesc;
	}
	public void setSrSiteDesc(String srSiteDesc) {
		this.srSiteDesc = srSiteDesc;
	}
}
