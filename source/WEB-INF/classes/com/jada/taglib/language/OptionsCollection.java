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
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

import com.jada.util.Format;

public class OptionsCollection extends LanguageTagSupport {
	private static final long serialVersionUID = -6116001347289727169L;
	Logger logger = Logger.getLogger(OptionsCollection.class);
    String label;

	public int doEndTag() throws JspException {
		JspWriter writer = pageContext.getOut();
		try {
			Select select = (Select) TagSupport.findAncestorWithClass(this, Select.class);
			String value = (String) select.findValue(select.getProperty());
			if (value == null) {
				value = "";
			}
			
			LabelValueBean bean[] = (LabelValueBean[]) findValue(property);
			if (isFieldDisabled() || select.isFieldDisabled()) {
				for (int i = 0; i < bean.length; i++) {
					if (value.equals(bean[i].getValue())) {
						String s = "";
						Select parent = (Select) getParent();
						String n = parent.findName(parent.getProperty());
						s += buildHidden(n, bean[i].getValue());
						s += "<input type=\"text\" ";
						s += "name=\"" + n + "\" ";
						s += "value=\"" + bean[i].getLabel() + "\" ";
						if (!Format.isNullOrEmpty(parent.getStyleId())) {
							s += "id=\"" + parent.getStyleId() + "\" ";
						}
						s += "disabled=\"disabled\" ";
						s += ">";
						writer.print(s);
					}
				}
			}
			else {
				for (int i = 0; i < bean.length; i++) {
					writer.print("<option ");
					if (value.equals(bean[i].getValue())) {
						writer.print("selected ");
					}
					writer.print("value=\"" + bean[i].getValue() + "\">");
					writer.print(bean[i].getLabel());
					writer.print("</option>");
					writer.println();
				}
			}
		}
		catch (Exception e) {
			logger.error(e);
			throw new JspException();
		}	
		return EVAL_PAGE;	
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}