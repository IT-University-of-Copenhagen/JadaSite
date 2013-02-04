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

public class CheckboxEditorSwitch extends Checkbox {
	private static final long serialVersionUID = -8888912980722346453L;
	public int doStartTag() throws JspException {
		try {
			if (this.isSiteProfileDefault()) {
				return SKIP_BODY;
			}
		}
		catch (Exception e) {
			logger.error(e);
			throw new JspException();
		}
		return EVAL_BODY_AGAIN;
	}
	public int doEndTag() throws JspException {
		try {
			if (this.isSiteProfileDefault()) {
				return EVAL_PAGE;
			}
		}
		catch (Exception e) {
			logger.error(e);
			throw new JspException();
		}
		
		if (!property.endsWith("LangFlag")) {
			throw new JspException("Property " + property + " does not ends with LangFlag");
		}
		String fieldname = property.substring(0, property.length() - 8);
		String s = "return overrideEditorLanguage(jc_editor_"+ fieldname + "Lang," + name + "." + fieldname + ", this);";
		super.setOnclick(s);
		return super.doEndTag();
	}
}