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

public class Password extends LanguageTagSupport {
	private static final long serialVersionUID = 1646231199248327086L;
	Logger logger = Logger.getLogger(Password.class);
    String size;
    String maxlength;
    String styleClass;

	public int doEndTag() throws JspException {
		JspWriter writer = pageContext.getOut();
		
		try {
			String value = (String) findValue(property);
			String result = null;
			if (!hasLanguageOverride()) {
				if (isFieldDisabled()) {
					result = buildHidden(property, value);
					writer.println(result);
				}
				result = buildText(property, value);
				writer.println(result);
			}
			else {
				result = buildHidden(property, value);
				writer.println(result);
				String name = property + "Lang";
				value = (String) findValue(name);
				result = buildText(name, value);
				writer.println(result);
			}
		}
		catch (Exception e) {
			logger.error(e);
			throw new JspException();
		}
		return EVAL_PAGE;
	}
	
	public String buildHidden(String name, String value) {
		String indexName = findName(name);
		String s = "";
		s = "<input type=\"hidden\" ";
		s += "name=\"" + indexName + "\" ";
		s += "value=\"" + value + "\" ";
		s += ">";
		return s;
	}
	
	public String buildText(String name, String value) throws Exception {
		String indexName = findName(name);
		String s = "";
		s += "<input type=\"password\" ";
		s += "name=\"" + indexName + "\" ";
		s += "value=\"" + value + "\" ";
		if (!Format.isNullOrEmpty(size)) {
			s += "size=\"" + size + "\" ";
		}
		if (!Format.isNullOrEmpty(maxlength)) {
			s += "maxlength=\"" + maxlength + "\" ";
		}
		if (!Format.isNullOrEmpty(styleClass)) {
			s += "style=\"" + styleClass + "\" ";
		}
		if (isFieldDisabled()) {
			s += "disabled=\"disabled\" ";
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