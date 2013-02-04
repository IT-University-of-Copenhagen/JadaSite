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
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Logger;
import com.jada.util.Format;

public class Radio extends LanguageTagSupport {
	private static final long serialVersionUID = 935435243137745847L;
	Logger logger = Logger.getLogger(Radio.class);
    String size;
    String maxlength;
    String styleClass;
    
	public int doEndTag() throws JspException {
		JspWriter writer = pageContext.getOut();
		
		try {
			String radioValue = getValue();
			String value = (String) findValue(property);
			String result = null;
			if (!hasLanguageOverride()) {
				if (isFieldDisabled()) {
					if (!Format.isNullOrEmpty(start) && start.equalsIgnoreCase("true")) {
						result = buildHidden(property, value);
						writer.println(result);
					}
				}
				result = buildRadio(property, value, radioValue);
				writer.println(result);
			}
			else {
				if (!Format.isNullOrEmpty(start) && start.equalsIgnoreCase("true")) {
					result = buildHidden(property, value);
					writer.println(result);
				}
				result = buildRadio(name + "_tmp", value, radioValue);
				writer.println(result);
			}
		}
		catch (Exception e) {
			logger.error(e);
			throw new JspException();
		}
		return EVAL_PAGE;
	}
	
	public String buildRadio(String name, String value, String radioValue) throws Exception {
		if (value == null) {
			value = "";
		}
		String indexName = findName(name);
		String s = "";
		s += "<input type=\"radio\" ";
		s += "name=\"" + indexName + "\" ";
		s += "value=\"" + radioValue + "\" ";
		if (!Format.isNullOrEmpty(styleClass)) {
			s += "style=\"" + styleClass + "\" ";
		}
		if (isFieldDisabled()) {
			s += "disabled=\"disabled\" ";
		}
		if (!Format.isNullOrEmpty(onclick)) {
			s += "onclick=\"" + onclick + "\" ";
		}
		if (value.equals(radioValue)) {
			s += "checked=\"checked\" ";
		}
		s += ">";
		return s;
	}
	
	public String getMaxlength(String name, String value) {
		return maxlength;
	}

	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getMaxlength() {
		return maxlength;
	}
}