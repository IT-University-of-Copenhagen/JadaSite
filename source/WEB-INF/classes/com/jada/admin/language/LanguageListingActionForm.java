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

package com.jada.admin.language;

import java.util.Vector;

import com.jada.admin.AdminListingActionForm;

public class LanguageListingActionForm extends AdminListingActionForm {
	private static final long serialVersionUID = 7919384136944330264L;
	String srLangName;
	String srCodePage;
	Vector<?> languages;
	String langIds[];
	public String[] getLangIds() {
		return langIds;
	}
	public void setLangIds(String[] langIds) {
		this.langIds = langIds;
	}
	public Vector<?> getLanguages() {
		return languages;
	}
	public void setLanguages(Vector<?> languages) {
		this.languages = languages;
	}
	public String getSrCodePage() {
		return srCodePage;
	}
	public void setSrCodePage(String srCodePage) {
		this.srCodePage = srCodePage;
	}
	public String getSrLangName() {
		return srLangName;
	}
	public void setSrLangName(String srLangName) {
		this.srLangName = srLangName;
	}
}
