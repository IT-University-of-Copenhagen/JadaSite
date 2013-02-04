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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.LoopTagSupport;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.taglib.TagUtils;

import com.jada.admin.AdminMaintActionForm;

public class LanguageTagSupport extends BodyTagSupport {
	private static final long serialVersionUID = -7826059101103835902L;
	protected String indexed;
	protected String name = "org.apache.struts.taglib.html.BEAN";
	protected String property;
	protected String disabled;
	protected String styleId;
	protected String start;
	protected String onclick;
	protected String value;
	protected String style;
	protected String language;
	protected String currency;
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getOnclick() {
		return onclick;
	}
	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getStyleId() {
		return styleId;
	}
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}
	public String getDisabled() {
		return disabled;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getIndexed() {
		return indexed;
	}
	public void setIndexed(String indexed) {
		this.indexed = indexed;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String findName() {
		if (indexed == null || !indexed.equals("true")) {
			return property;
		}
		LoopTagSupport loopTag = (LoopTagSupport) TagSupport.findAncestorWithClass(this, LoopTagSupport.class);

		String result = "";
		result += name + "[" + loopTag.getLoopStatus().getIndex() + "]." + property;
		return result;
	}
	public String findName(String prop) {
		if (indexed == null || !indexed.equals("true")) {
			return prop;
		}
		LoopTagSupport loopTag = (LoopTagSupport) TagSupport.findAncestorWithClass(this, LoopTagSupport.class);

		String result = "";
		result += name + "[" + loopTag.getLoopStatus().getIndex() + "]." + prop;
		return result;
	}

	public Object findValue(String property) {
		try {
			Object result = TagUtils.getInstance().lookup(super.pageContext, name, property, null);
			return result;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public boolean isSiteProfileDefault() throws Exception {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		ActionForm form = (ActionForm) request.getAttribute("org.apache.struts.taglib.html.BEAN");
		if (form == null) {
			return true;
		}
		if (form instanceof AdminMaintActionForm) {
			AdminMaintActionForm actionForm = (AdminMaintActionForm) form;
			return actionForm.isSiteProfileClassDefault();
		}
		return true;
	}
	
	public boolean isSiteCurrencyDefault() throws Exception {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		ActionForm form = (ActionForm) request.getAttribute("org.apache.struts.taglib.html.BEAN");
		if (form == null) {
			return true;
		}
		if (form instanceof AdminMaintActionForm) {
			AdminMaintActionForm actionForm = (AdminMaintActionForm) form;
			return actionForm.isSiteCurrencyClassDefault();
		}
		return true;
	}
	
	public boolean hasLanguageOverride() throws Exception {
		if (isSiteProfileDefault()) {
			return false;
		}
		if (currency != null) {
			if (currency.toLowerCase().equals("true")) {
				return false;
			}
		}
		if (language != null) {
			if (language.toLowerCase().equals("true")) {
				return true;
			}
		}
		String fieldname = property + "LangFlag";
		Object result = null;
		try {
			result = TagUtils.getInstance().lookup(super.pageContext, name, fieldname, null);
		}
		catch (Exception e) {};
		if (result != null) {
			return true;
		}
		return false;
	}
	
	
	public boolean hasCurrencyOverride() throws Exception {
		if (isSiteCurrencyDefault()) {
			return false;
		}
		if (language != null) {
			if (language.toLowerCase().equals("true")) {
				return false;
			}
		}
		if (currency != null) {
			if (currency.toLowerCase().equals("true")) {
				return true;
			}
		}
		String fieldname = property + "CurrFlag";
		Object result = null;
		try {
			result = TagUtils.getInstance().lookup(super.pageContext, name, fieldname, null);
		}
		catch (Exception e) {};
		if (result != null) {
			return true;
		}
		return false;
	}
	
	public boolean isFieldDisabled() throws Exception {
		if (disabled != null) {
			if (disabled.equalsIgnoreCase("true")) {
				return true;
			}
			if (disabled.equalsIgnoreCase("false")) {
				return false;
			}
		}
		if (isSiteProfileDefault() && isSiteCurrencyDefault()) {
			return false;
		}
		String langFieldName = property + "LangFlag";
		String currFieldName = property + "CurrFlag";
		Object result = null;
		
		if (currency != null && currency.toLowerCase().equals("true")) {
			try {
				result = TagUtils.getInstance().lookup(super.pageContext, name, currFieldName, null);
			}
			catch (Exception e) {};
		}
		else if (language != null && language.toLowerCase().equals("true")) {
			try {
				result = TagUtils.getInstance().lookup(super.pageContext, name, langFieldName, null);
			}
			catch (Exception e) {};
		}
		else {
			try {
				result = TagUtils.getInstance().lookup(super.pageContext, name, langFieldName, null);
			}
			catch (Exception e) {};
			try {
				if (result == null) {
					result = TagUtils.getInstance().lookup(super.pageContext, name, currFieldName, null);
				}
			}
			catch (Exception e) {};
		}

		if (result == null) {
			return true;
		}
		Boolean b = (Boolean) result;
		if (b.booleanValue()) {
			return false;
		}
		return true;
	}
	
	public String buildHidden(String name, String value) {
		String indexName = findName(name);
		if (value == null) {
			value = "";
		}
		String s = "";
		s = "<input type=\"hidden\" ";
		s += "name=\"" + indexName + "\" ";
		s += "value=\"" + StringEscapeUtils.escapeHtml(value) + "\" ";
		s += ">";
		return s;
	}
}
