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

import org.apache.struts.util.LabelValueBean;

import com.jada.admin.AdminMaintActionForm;

public class SiteProfileClassMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -3296807105674444534L;
	String mode;
	String siteProfileClassName;
	String siteProfileClassNativeName;
	String langId;
	String systemRecord;
	LabelValueBean languages[];
	LabelValueBean templates[];
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getSiteProfileClassName() {
		return siteProfileClassName;
	}
	public void setSiteProfileClassName(String siteProfileClassName) {
		this.siteProfileClassName = siteProfileClassName;
	}
	public String getLangId() {
		return langId;
	}
	public void setLangId(String langId) {
		this.langId = langId;
	}
	public LabelValueBean[] getLanguages() {
		return languages;
	}
	public void setLanguages(LabelValueBean[] languages) {
		this.languages = languages;
	}
	public LabelValueBean[] getTemplates() {
		return templates;
	}
	public void setTemplates(LabelValueBean[] templates) {
		this.templates = templates;
	}
	public String getSiteProfileClassNativeName() {
		return siteProfileClassNativeName;
	}
	public void setSiteProfileClassNativeName(String siteProfileClassNativeName) {
		this.siteProfileClassNativeName = siteProfileClassNativeName;
	}
	public String getSystemRecord() {
		return systemRecord;
	}
	public void setSystemRecord(String systemRecord) {
		this.systemRecord = systemRecord;
	}
}
