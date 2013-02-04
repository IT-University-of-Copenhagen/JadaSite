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

package com.jada.ui.dropdown;

import javax.servlet.http.HttpServletRequest;

public class DropDownMenuGenerator {
	// TODO replace with request.getContext()
	static String imagePrefix = "/jada/admin/templates/images/";
	static public int SELECTION_NONE = 0;
	static public int SELECTION_SINGLE = 1;
	static public int SELECTION_MULTIPLE = 2;
	
	static public DropDownMenuContainer construct() {
		DropDownMenuContainer container = new DropDownMenuContainer();
		
		DropDownMenu menu = new DropDownMenu();
		menu.setMenuName("Line 1");
		container.addMenuItems(menu);
		
		DropDownMenu menu1 = new DropDownMenu();
		menu1.setMenuName("Line 1 1");
		menu.addMenuItems(menu1);
		
		menu1 = new DropDownMenu();
		menu1.setMenuName("Line 1 2");
		menu.addMenuItems(menu1);
		
		
		menu = new DropDownMenu();
		menu.setMenuName("Line 2");
		container.addMenuItems(menu);
		
		menu1 = new DropDownMenu();
		menu1.setMenuName("Line 2 1");
		menu.addMenuItems(menu1);
		
		menu1 = new DropDownMenu();
		menu1.setMenuName("Line 2 2");
		menu.addMenuItems(menu1);
	
		return container;
	}
/*	
	static public String generate1(HttpServletRequest request, String menu, int selection, String selectionName) {
		DropDownMenu container = (DropDownMenu) request.getAttribute(menu);
		return generate1(container, selection, selectionName, null, null);
	}
*/	
	static public String generate1(DropDownMenu ddmList[], String selections[], int selection, String selectionName, String anchorPrefix, String paramName) {
		String code = "";
		for (int i = 0; i < ddmList.length; i++) {
			code += generate1(ddmList[i], selections, String.valueOf(i), selection, selectionName, anchorPrefix, paramName);
		}
		return code;
	}
	
	static public String generate1(DropDownMenu ddm, String selections[], String id, int selection, String selectionName, String anchorPrefix, String paramName) {
		String formType = "";
		if (selection == SELECTION_SINGLE) {
			formType = "radio";
		}
		else {
			formType = "checkbox";
		}
		
		String code = "";
		code += "<ul style=\"display:none\" id=\"" + id + "\">\n";
		DropDownMenu menuList[] = ddm.getMenuItems();
		for (int i = 0; i < menuList.length; i++) {
			DropDownMenu menuItem = menuList[i];
			
			code += "<li>";
			code += "<div>";
			if (selection != SELECTION_NONE) {
				String check = "";
				if (selections != null) {
					for (int j = 0; j < selections.length; j++) {
						if (menuItem.getMenuKey().equals(selections[j])) {
							check = "checked=\"checked\"";
							break;
						}
					}
				}
				code += "<input type=\"" + formType + "\" name=\"" + selectionName + "\" value=\"" + menuItem.getMenuKey() + "\" " + check + ">";
			}
			if (anchorPrefix == null) {
				code += menuList[i].getMenuName() + "\n";
			}
			else {
				String href = anchorPrefix;
				if (anchorPrefix.indexOf("?") > 0) {
					href += "&" + paramName + "=" + menuList[i].getMenuKey();
				}
				else {
					href += "?" + paramName + "=" + menuList[i].getMenuKey();	
				}
				code += "<a href='" + href + "'>" + menuList[i].getMenuName() + "<div style='display:none'>" + menuList[i].getMenuKey() + "</div></a>\n";
			}
			code += "</div>";
			if (menuList[i].getMenuItems().length > 0) {
				code += generate1(menuList[i], selections, id, selection, selectionName, anchorPrefix, paramName);
			}
			code += "</li>";
		}
		
		code += "</ul>\n";
		return code;
	}
	
	static public String generateMenuItem1(DropDownMenu menuItem, String id, int selection, String selectionName, String anchorPrefix, String paramName) {
		String code = "";
		DropDownMenu menuList[] = menuItem.getMenuItems();
		
		String formType = "";
		if (selection == SELECTION_SINGLE) {
			formType = "radio";
		}
		else {
			formType = "checkbox";
		}

		code += "<ul id=\"" + id + "\">\n";
		if (menuList.length > 0) {
			for (int i = 0; i < menuList.length; i++) {
				code += "<li>";
				code += "<div>";
				if (selection != SELECTION_NONE) {
					code += "<input type=\"" + formType + "\" name=\"" + selectionName + "\" value=\"" + menuItem.getMenuKey() + "\" unchecked>";
				}
				if (anchorPrefix == null) {
					code += menuList[i].getMenuName() + "\n";
				}
				else {
					String href = anchorPrefix;
					if (anchorPrefix.indexOf("?") > 0) {
						href += "&" + paramName + "=" + menuList[i].getMenuKey();
					}
					else {
						href += "?" + paramName + "=" + menuList[i].getMenuKey();	
					}
					code += "<a href='" + href + "'>" + menuList[i].getMenuName() + "</a>\n";
				}
				code += "</div>";
				if (menuList[i].getMenuItems().length > 0) {
					code += generateMenuItem1(menuList[i], id, selection, selectionName, anchorPrefix, paramName);
				}
				code += "</li>";
			}
		}
		return code;
	}
	/***************************************************************************************************************/
	/***************************************************************************************************************/
	
	static public String generate(HttpServletRequest request, String menu, int selection, String selectionName) {
		DropDownMenuContainer container = (DropDownMenuContainer) request.getAttribute(menu);
		return generate(container, selection, selectionName, null, null);
	}

	static public String generate(DropDownMenuContainer containerList[], int selection, String selectionName, String anchorPrefix, String paramName) {
		String code = "";
		for (int i = 0; i < containerList.length; i++) {
			if (containerList[i].getMenuSetName() != null) {
				code += "<div class=\"sm_DDM_header\">";
				String href = "";
				if (anchorPrefix != null) {
					href = anchorPrefix;
					if (anchorPrefix.indexOf("?") > 0) {
						href += "&" + paramName + "=" + containerList[i].getMenuSetKey();
					}
					else {
						href += "?" + paramName + "=" + containerList[i].getMenuSetKey();	
					}
					code += "<a href='" + href + "'>";
				}
				code += "Set - " + containerList[i].getMenuSetName();
				if (anchorPrefix != null) {
					code += "</a>";
				}
				code += "</div>\n";
			}
			code += generate(containerList[i], selection, selectionName, anchorPrefix, paramName);
		}
		return code;
	}
	
	static public String generate(DropDownMenuContainer container, int selection, String selectionName, String anchorPrefix, String paramName) {
		String code = "";
		code += "<ul class=\"sm_DDM_menu\">\n";
		DropDownMenu menuList[] = container.getMenuItems();
		for (int i = 0; i < menuList.length; i++) {
			String id = "";
			if (container.getMenuSetName() != null) {
				id += container.getMenuSetName() + "_";
			}
			id += "1" + "_" + String.valueOf(i);
			code += generateMenuItem(menuList[i], id, 1, selection, selectionName, anchorPrefix, paramName);
		}
		code += "</ul>\n";
		return code;
	}
	
	static public String generateMenuItem(DropDownMenu menuItem, String id, int level, int selection, String selectionName, String anchorPrefix, String paramName) {
		String code = "";
		DropDownMenu menuList[] = menuItem.getMenuItems();
		
		String formType = "";
		if (selection == SELECTION_SINGLE) {
			formType = "radio";
		}
		else {
			formType = "checkbox";
		}
		
		String indent = "  ";
		for (int i = 0; i < level - 1; i++) {
			indent += "    ";
		}
		
		code += indent + "<li class=\"sm_DDM_item\">\n";
		for (int i = 0; i < level - 1; i++) {
			code += "<img src=\"" + imagePrefix + "btnBlank.gif\" border=\"0\">";
		}
		if (menuList.length > 0) {
			code += indent + "  <a href=\"\" onclick=\"javascript:sm_DDM_toggle('" + id + "'); return false;\"><img src=\"" + imagePrefix + "btnExpand.gif\" border=\"0\"></a>";
			if (selection != SELECTION_NONE) {
				code += "<input type=\"" + formType + "\" name=\"" + selectionName + "\" value=\"" + menuItem.getMenuKey() + "\" unchecked>";
			}
//			code += menuItem.getMenuName() + "\n";
		}
		else {
			code += "<img src=\"" + imagePrefix + "btnBlank.gif\" border=\"0\">";
			if (selection != SELECTION_NONE) {
				code += "<input type=\"" + formType + "\" name=\"" + selectionName + "\" value=\"" + menuItem.getMenuKey() + "\" unchecked>";
			}
//			code += menuItem.getMenuName() + "\n";
		}
		if (anchorPrefix == null) {
			code += menuItem.getMenuName() + "\n";
		}
		else {
			String href = anchorPrefix;
			if (anchorPrefix.indexOf("?") > 0) {
				href += "&" + paramName + "=" + menuItem.getMenuKey();
			}
			else {
				href += "?" + paramName + "=" + menuItem.getMenuKey();	
			}
			code += "<a href='" + href + "'>" + menuItem.getMenuName() + "</a>\n";
		}
		code += indent + "</li>\n";
		if (menuList.length > 0) {
			code += indent + "<li id=\"" + id + "\" class=\"sm_DDM_hide\">\n";
			code += indent + "  <ul class=\"sm_DDM_container\">\n";
			for (int i = 0; i < menuList.length; i++) {
				id += "-" + String.valueOf(i);
				code += generateMenuItem(menuList[i], id, level + 1, selection, selectionName, anchorPrefix, paramName);
			}
			code += indent + "  </ul>\n";
			code += indent + "</li>\n";
		}
		return code;
	}

	public static void main(String[] argv) {
		DropDownMenuContainer container = construct();
		String result = generate(container, SELECTION_NONE, "", null, "menuId");
		System.out.println(result);
	}
}
