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

public class SiteProfileClassDisplayForm {
	boolean remove;
	public String siteProfileClassId;
	public String siteProfileClassName;
	public String langName;
	public String systemRecord;
	public String getSiteProfileClassId() {
		return siteProfileClassId;
	}
	public void setSiteProfileClassId(String siteProfileClassId) {
		this.siteProfileClassId = siteProfileClassId;
	}
	public String getSiteProfileClassName() {
		return siteProfileClassName;
	}
	public void setSiteProfileClassName(String siteProfileClassName) {
		this.siteProfileClassName = siteProfileClassName;
	}
	public String getLangName() {
		return langName;
	}
	public void setLangName(String langName) {
		this.langName = langName;
	}
	public boolean isRemove() {
		return remove;
	}
	public void setRemove(boolean remove) {
		this.remove = remove;
	}
	public String getSystemRecord() {
		return systemRecord;
	}
	public void setSystemRecord(String systemRecord) {
		this.systemRecord = systemRecord;
	}
}
