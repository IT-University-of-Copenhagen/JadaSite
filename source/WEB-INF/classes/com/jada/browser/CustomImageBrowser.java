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

package com.jada.browser;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;

public class CustomImageBrowser extends YuiImageBrowser {
	private static final long serialVersionUID = 4949352085595404286L;

	protected String getBaseDir(HttpServletRequest request) {
		String baseDir = super.getBaseDir(request);
		AdminBean adminBean = AdminLookupDispatchAction.getAdminBean(request);
		baseDir += "/" + adminBean.getSiteId() + "/rte";
		File file = new File(baseDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		return baseDir;
	}
}
