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

import org.apache.log4j.Logger;

import com.jada.util.Format;

public class Checkbox extends LanguageTagSupport {
	private static final long serialVersionUID = 3037842620120714627L;
	Logger logger = Logger.getLogger(Checkbox.class);
    String styleClass;
    String value;
    String onclick;
    String checked;
   
	public int doAfterBody() throws JspException {
		try {
			bodyContent.writeOut(bodyContent.getEnclosingWriter());
			return SKIP_BODY;
		} catch (IOException ex) {
			throw new JspTagException(ex.toString());
		}
	}
	public int doEndTag() throws JspException {
		JspWriter writer = pageContext.getOut();
		String s = "";
		boolean isChecked = false;
		Object object = findValue(property);
		if (object instanceof Boolean) {
			Boolean b = (Boolean) object;
			if (b.booleanValue()) {
				isChecked = true;
			}
		}
		if (object instanceof String) {
			String string = (String) object;
			if (string.equalsIgnoreCase("Y")) {
				isChecked = true;
			}
			if (string.equals("Yes")) {
				isChecked = true;
			}
		}
		
		try {
			String name = findName();
			s += "<input type=\"checkbox\" " +
			 	 "name=\"" + name + "\" ";
			if (isChecked) {
				s += "checked=\"checked\" ";
			}
			if (!Format.isNullOrEmpty(styleClass)) {
				s += "style=\"" + styleClass + "\" ";
			}
			if (!Format.isNullOrEmpty(value)) {
				s += "value=\"" + value + "\" ";	
			}
			else {
				s += "value=\"on\" ";
			}
			if (!Format.isNullOrEmpty(onclick)) {
				s += "onclick=\"" + onclick + "\" ";
			}
			if (!Format.isNullOrEmpty(checked)) {
				s += "checked=\"" + checked + "\" ";
			}
			if (isFieldDisabled() && !property.endsWith("LangFlag") && !property.endsWith("CurrFlag")) {
				s += "disabled=\"disabled\" ";
			}
			s += "/>";
			if (!property.endsWith("LangFlag") && !property.endsWith("CurrFlag")) {
				if (isFieldDisabled()) {
					if (isChecked) {
						s += buildHidden(property, value);
					}
					else {
						s += buildHidden(property, "");
					}
				}
			}
			writer.println(s);
		}
		catch (Exception e) {
			logger.error(e);
			throw new JspException();
		}
		return EVAL_PAGE;
	}
	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	
    public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
}