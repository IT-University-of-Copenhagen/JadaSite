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

import java.util.Vector;

public class DropDownMenu {
	String menuKey;
	String menuName;
	DropDownMenu menuItems[] = new DropDownMenu[0];
	
	public void addMenuItems(DropDownMenu menu) {
		Vector<DropDownMenu> vector = new Vector<DropDownMenu>();
		for (int i = 0; i < menuItems.length; i++) {
			vector.add(menuItems[i]);
		}
		vector.add(menu);
		menuItems = new DropDownMenu[vector.size()];
		vector.copyInto(menuItems);
	}
	public DropDownMenu[] getMenuItems() {
		return menuItems;
	}
	public void setMenuItems(DropDownMenu[] menuItems) {
		this.menuItems = menuItems;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuKey() {
		return menuKey;
	}
	public void setMenuKey(String menuKey) {
		this.menuKey = menuKey;
	}
}
