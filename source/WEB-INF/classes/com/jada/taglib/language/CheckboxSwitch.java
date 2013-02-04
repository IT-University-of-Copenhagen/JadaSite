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

package com.jada.taglib.language;

import javax.servlet.jsp.JspException;

import com.jada.util.Format;

public class CheckboxSwitch extends Checkbox {
	private static final long serialVersionUID = 3789544183496502871L;
	String ignoreEmpty;

	public int doStartTag() throws JspException {
		try {
			if (isLanguage()) {
				if (this.isSiteProfileDefault()) {
					return SKIP_BODY;
				}
			}
			else {
				if (this.isSiteCurrencyDefault()) {
					return SKIP_BODY;
				}
			}
		}
		catch (Exception e) {
			logger.error(e);
			throw new JspException();
		}
		return EVAL_BODY_AGAIN;
	}
	public int doEndTag() throws JspException {
		String fieldname = property.substring(0, property.length() - 8);
		try {
			if (isLanguage()) {
				if (this.isSiteProfileDefault()) {
					return SKIP_BODY;
				}
			}
			else {
				if (this.isSiteCurrencyDefault()) {
					return SKIP_BODY;
				}
			}
			if (ignoreEmpty != null && ignoreEmpty.toLowerCase().equals("true")) {
				String fieldvalue = (String) this.findValue(fieldname);
				if (Format.isNullOrEmpty(fieldvalue)) {
					return SKIP_BODY;
				}
			}
		}
		catch (Exception e) {
			logger.error(e);
			throw new JspException();
		}
		String s = "";
		if (isLanguage()) {
			s = "return overrideLanguage(" + name + "." + fieldname + "Lang_tmp," + name + "." + fieldname + ", this);";
		}
		else {
			s = "return overrideCurrency(" + name + "." + fieldname + "Curr_tmp," + name + "." + fieldname + ", this);";
		}
		super.setOnclick(s);
		return super.doEndTag();
	}
	
	public boolean isLanguage() {
		if (property.endsWith("LangFlag")) {
			return true;
		}
		return false;
	}
	
	public boolean isCurrency() {
		if (property.endsWith("CurrFlag")) {
			return true;
		}
		return false;
	}
	public String getIgnoreEmpty() {
		return ignoreEmpty;
	}
	public void setIgnoreEmpty(String ignoreEmpty) {
		this.ignoreEmpty = ignoreEmpty;
	}
}