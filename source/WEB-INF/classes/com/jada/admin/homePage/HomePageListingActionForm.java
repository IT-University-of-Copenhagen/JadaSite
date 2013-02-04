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

package com.jada.admin.homePage;

import java.util.Vector;

import com.jada.admin.AdminMaintActionForm;

public class HomePageListingActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = 6793373172130667750L;
	String mode;
	String srSiteName;
	String srSiteDomainName;
    String srActive;
    Vector<?> SiteDomains;
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getSrSiteName() {
		return srSiteName;
	}
	public void setSrSiteName(String srSiteName) {
		this.srSiteName = srSiteName;
	}
	public String getSrSiteDomainName() {
		return srSiteDomainName;
	}
	public void setSrSiteDomainName(String srSiteDomainName) {
		this.srSiteDomainName = srSiteDomainName;
	}
	public Vector<?> getSiteDomains() {
		return SiteDomains;
	}
	public void setSiteDomains(Vector<?> siteDomains) {
		SiteDomains = siteDomains;
	}
	public String getSrActive() {
		return srActive;
	}
	public void setSrActive(String srActive) {
		this.srActive = srActive;
	}
}
