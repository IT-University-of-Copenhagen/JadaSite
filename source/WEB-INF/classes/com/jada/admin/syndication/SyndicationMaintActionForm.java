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

package com.jada.admin.syndication;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminMaintActionForm;

public class SyndicationMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -7427271887077155524L;
	SyndicationDisplayForm syns[];
	public SyndicationDisplayForm[] getSyns() {
		return syns;
	}
	public void setSyns(SyndicationDisplayForm[] syns) {
		this.syns = syns;
	}
	public SyndicationDisplayForm getSyn(int index) {
		return syns[index];
	}
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		String SYN = "syn.*Url";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(SYN)) {
				count++;
			}
		}
		syns = new SyndicationDisplayForm[count];
		for (int i = 0; i < syns.length; i++) {
			syns[i] = new SyndicationDisplayForm();
		}
	}
}
