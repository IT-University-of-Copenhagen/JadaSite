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
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Logger;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.system.ApplicationGlobal;

public class EditorText extends LanguageTagSupport {
	private static final long serialVersionUID = -922414453179353589L;
	Logger logger = Logger.getLogger(EditorText.class);
    String height;
    String width;
    String toolBarSet;

	public int doEndTag() throws JspException {
		JspWriter writer = pageContext.getOut();
		
		try {
			String value = (String) findValue(property);
			if (value == null) {
				value = "";
			}
			String result = null;
			if (!hasLanguageOverride()) {
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
		try {
			s = "<input type=\"hidden\" ";
			s += "name=\"" + indexName + "\" ";
			s += "value='" + value + "' ";
			s += ">";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public String buildText(String name, String value) throws Exception {
		String indexName = findName(name);
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String context = ApplicationGlobal.getContextPath();
		AdminBean adminBean = AdminLookupDispatchAction.getAdminBean(request);
		String prefix = "/" + 
						context + "/" + 
						"web/proxy/" + 
						adminBean.getSiteId() + "/" +
						"rte";
		
		String html = "";
		html += "<textarea id=\"" + indexName + "\" name=\"" + indexName + "\" style=\"display:none\">";
		html += value;
		html += "</textarea>";
		html += "<script>";
		html += "jc_render_texteditor('/" + context + "', '" + height + "', '" + width + "', '" + indexName + "', ";
		html += "'" + prefix + "', ";
		html += isFieldDisabled() ? "true" : "false";
		html += ");";
		html += "</script>";
		return html;
	}
    
	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getToolBarSet() {
		return toolBarSet;
	}

	public void setToolBarSet(String toolBarSet) {
		this.toolBarSet = toolBarSet;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}
}