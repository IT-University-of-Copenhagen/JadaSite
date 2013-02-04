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

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;

import org.apache.log4j.Logger;

import com.jada.util.Format;

public class Select extends LanguageTagSupport {
	private static final long serialVersionUID = -6571809057095849793L;
	Logger logger = Logger.getLogger(Select.class);
    JspWriter writer = null;
    String onchange;
    String styleClass;

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public int doStartTag() throws JspException {
		writer = pageContext.getOut();
		try {
			if (!isFieldDisabled()) {
				String s = "";
				s += "<select name=\""; 
				s += findName() + "\" ";
				if (!Format.isNullOrEmpty(styleClass)) {
					s += "style=\"" + styleClass + "\" ";
				}
				if (!Format.isNullOrEmpty(styleId)) {
					s += "id=\"" + styleId + "\" ";
				}
				if (!Format.isNullOrEmpty(onchange)) {
					s += "onchange=\"" + onchange + "\" ";
				}
				s += ">";
				writer.print(s);
			}
		}
		catch (Exception e) {
			logger.error(e);
			throw new JspException();
		}
		return EVAL_PAGE;
	}
	
	public int doAfterBody() throws JspException {
		
		try { 
			BodyContent body = getBodyContent();			
			if (body != null) {
				writer.print(body.getString());
				body.clearBody();
			}
		
		} catch (IOException e) {
			logger.error(e);
			throw new JspException();
		} 
		return SKIP_BODY;
		
	}

	public int doEndTag() throws JspException {
		try {
			if (!isFieldDisabled()) {
				String s = "</select>";
				writer.print(s);
			}
		}
		catch (Exception e) {
			logger.error(e);
			throw new JspException();
		}
		return EVAL_PAGE;
	}
	
	public String getOnchange() {
		return onchange;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}
}