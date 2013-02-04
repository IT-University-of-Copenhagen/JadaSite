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

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import com.jada.admin.AdminMaintActionForm;

public class LanguageMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = 5257313133437090270L;
	String mode;
	String langId;
	String langName;
	String locale;
	String googleTranslateLocale;
	String systemRecord;
	LabelValueBean locales[];
	LabelValueBean languages[];
	LangTranDetailForm langTrans[];
    public LangTranDetailForm getLangTran(int index) {
    	return langTrans[index];
    }
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		String TRAN = "langTran.*langTranKey";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(TRAN)) {
				count++;
			}
		}
		langTrans = new LangTranDetailForm[count];
		for (int i = 0; i < langTrans.length; i++) {
			langTrans[i] = new LangTranDetailForm();
		}
	}
	public String getLangId() {
		return langId;
	}
	public void setLangId(String langId) {
		this.langId = langId;
	}
	public LangTranDetailForm[] getLangTrans() {
		return langTrans;
	}
	public void setLangTrans(LangTranDetailForm[] langTrans) {
		this.langTrans = langTrans;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getLangName() {
		return langName;
	}
	public void setLangName(String langName) {
		this.langName = langName;
	}
	public LabelValueBean[] getLocales() {
		return locales;
	}
	public void setLocales(LabelValueBean[] locales) {
		this.locales = locales;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getGoogleTranslateLocale() {
		return googleTranslateLocale;
	}
	public void setGoogleTranslateLocale(String googleTranslateLocale) {
		this.googleTranslateLocale = googleTranslateLocale;
	}
	public String getSystemRecord() {
		return systemRecord;
	}
	public void setSystemRecord(String systemRecord) {
		this.systemRecord = systemRecord;
	}
	public LabelValueBean[] getLanguages() {
		return languages;
	}
	public void setLanguages(LabelValueBean[] languages) {
		this.languages = languages;
	}
}
