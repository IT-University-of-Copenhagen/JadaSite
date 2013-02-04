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

public class TextArea extends LanguageTagSupport {
	private static final long serialVersionUID = -3888570790212518996L;
	Logger logger = Logger.getLogger(TextArea.class);
    String styleClass;
    String rows;
    String cols;

	public int doEndTag() throws JspException {
		JspWriter writer = pageContext.getOut();
		
		try {
			String value = (String) findValue(property);
			if (value == null) {
				value = "";
			}
			String result = null;
			if (!hasLanguageOverride()) {
				if (isFieldDisabled()) {
					result = buildHidden(property, value);
					writer.println(result);
				}
				result = buildTextArea(property, value);
				writer.println(result);
			}
			else {
				result = buildHidden(property, value);
				writer.println(result);
				String name = property + "Lang";
				value = (String) findValue(name);
				result = buildHidden(name, value);
				writer.println(result);
				result = buildTextArea(name + "_tmp", value);
				writer.println(result);
			}
		}
		catch (Exception e) {
			logger.error(e);
			throw new JspException();
		}
		return EVAL_PAGE;
	}
	
	public String buildTextArea(String name, String value) throws Exception {
		String indexName = findName(name);
		String s = "";
		s += "<textarea ";
		s += "name=\"" + indexName + "\" ";
		if (!Format.isNullOrEmpty(styleClass)) {
			s += "style=\"" + styleClass + "\" ";
		}
		if (!Format.isNullOrEmpty(rows)) {
			s += "rows=\"" + rows + "\" ";
		}
		if (!Format.isNullOrEmpty(cols)) {
			s += "cols=\"" + cols + "\" ";
		}
		if (isFieldDisabled()) {
			s += "disabled=\"disabled\" ";
		}
		s += ">";
		s += value;
		s += "</textarea>";
		return s;
	}

	public String getStyleClass() {
		return styleClass;
	}
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}    
	public String getCols() {
		return cols;
	}
	public void setCols(String cols) {
		this.cols = cols;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
}