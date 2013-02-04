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
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

public class Option extends LanguageTagSupport {
	private static final long serialVersionUID = 6795959515948687356L;
	Logger logger = Logger.getLogger(Option.class);
    String value;
	JspWriter writer;
    boolean skip = true;
    String parentName;

    public int doAfterBody() throws JspException {
		try {
			if (!skip) {
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}
			return SKIP_BODY;
		} catch (IOException ex) {
			throw new JspTagException(ex.toString());
		}
	}
	public int doStartTag() throws JspException {
		writer = pageContext.getOut();
		skip = true;
		try {
			Select select = (Select) TagSupport.findAncestorWithClass(this, Select.class);
			String parentValue = (String) select.findValue(select.getProperty());
			if (isFieldDisabled()) {
				if (value.equals(parentValue)) {
					String s = "";
					Select parent = (Select) getParent();
					parentName = parent.findName(parent.getProperty());
					s += "<input type=\"text\" ";
					s += "name=\"" + parentName + "\" ";
					s += "disabled=\"disabled\" ";
					s += "value=\"";
					writer.print(s);
					skip = false;
				}
			}
			else {
				writer.print("<option ");
				if (value.equals(parentValue)) {
					writer.print("selected ");
				}
				writer.print("value=\"" + value + "\">");
				skip = false;
			}
		}
		catch (Exception e) {
			logger.error(e);
			throw new JspException();
		}
		if (skip) {
			return SKIP_BODY;
		}
		return EVAL_PAGE;	
	}
	public int doEndTag() throws JspException {
		try {
			if (isFieldDisabled()) {
				if (!skip) {
					writer.println("\">");
					String s = buildHidden(parentName, value);
					writer.println(s);
				}
			}
			else {
				writer.print("</option>");
				writer.println();
			}
		}
		catch (Exception e) {
			logger.error(e);
			throw new JspException();
		}	
		return EVAL_PAGE;	
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}