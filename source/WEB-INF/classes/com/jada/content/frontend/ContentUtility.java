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

package com.jada.content.frontend;

import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.jada.content.data.ContentApi;
import com.jada.content.data.MenuInfo;
import com.jada.util.Constants;

public class ContentUtility {
	static int sequence = 0;
    Logger logger = Logger.getLogger(ContentUtility.class);

	static synchronized public String getNextMenuDivId(String menuName) {
		if (sequence > 50000) {
			sequence = 0;
		}
		return menuName + sequence++;
	}
	
	static public String generateMenu(ContentApi api, String menuSetName, String menuId, boolean vertical, boolean customerSession, String styleClassSuffix) throws Exception {
		Vector<?> vector = null;
		vector = api.getMenu(menuSetName, customerSession);
		return generateMenuItem(vector, menuId, vertical, 0, styleClassSuffix);
	}
	
	static private String generateMenuItem(Vector<?> vector, String id, boolean vertical, int level, String styleClassSuffix) {
		String ulstyle = "yuimenubar yuimenubarnav";
		String listyle = "yuimenubaritem";
		String anchorstyle ="yuimenubaritemlabel";
		if (vertical) {
			ulstyle = "yuimenu";
			listyle = "yuimenuitem";
			anchorstyle = "yuimenuitemlabel";
		}
		if (styleClassSuffix != null && styleClassSuffix.length() > 0) {
			ulstyle += " " + ulstyle + "-" + styleClassSuffix;
			listyle += " " + listyle + "-" + styleClassSuffix;
			anchorstyle += " " + anchorstyle + "-" + styleClassSuffix;
		}
		String data = "";
		String align = "";
		if (!vertical) {
			align = "align=\"left\" style=\"white-space: nowrap;\"";
		}
		data += "<div id=\"" + id + "\" " + align + " class=\"" + ulstyle + "\">";
		data += "<div class=\"bd\" " + align + " >";
		data += "<ul class=\"first-of-type\">";
		int counter = 0;
		Iterator<?> iterator = vector.iterator();
		while (iterator.hasNext()) {
			MenuInfo menuInfo = (MenuInfo) iterator.next();
			String anchor = "";
			if (menuInfo.getMenuType().equals(Constants.MENU_NOOPERATION)) {
				anchor = "<span class=\"" + anchorstyle + "\">" + menuInfo.getMenuName() + "</span>";
			}
			else {
				anchor = "<a href=\"" + menuInfo.getMenuUrl() + "\" " + 
						 "class=\"" + anchorstyle + "\" " + 
	            		 "onclick=\"javascrpt:window.open('" + menuInfo.getMenuUrl() + "', " + "'" + menuInfo.getMenuWindowTarget() + "' ";
				if (menuInfo.getMenuWindowMode().trim().length() != 0) {
					anchor += ", '" + menuInfo.getMenuWindowMode() + "'";
				}
				anchor += ");return false;\">";
				anchor += menuInfo.getMenuName();
				anchor += "</a>";
			}

			if (level == 0) {
				data += "<li class=\"" + listyle + "\" class=\"first-of-type\">" + anchor + "";
			}
			else {
				data += "<li class=\"" + listyle + "\">" + anchor + "";
			}
			if (menuInfo.getMenus().size() > 0) {
				vertical = true;
				data += generateMenuItem(menuInfo.getMenus(), id + counter, vertical, level + 1, styleClassSuffix) + "";
			}
			data += "</li>";
			counter++;
		}
		data += "</ul>";
		data += "</div>";
		data += "</div>";
		return data;
	}
}
