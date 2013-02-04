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

package com.jada.content.data;

import java.io.Serializable;
import java.util.Vector;

public class MenuInfo implements Serializable {
	private static final long serialVersionUID = 8152936039824430402L;
	String menuName;
	int seqNo;
	String menuType;
	String menuAnchor;
	String menuWindowTarget;
	String menuWindowMode;
	String menuUrl;
	Vector<?> menus;
	
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public Vector<?> getMenus() {
		return menus;
	}
	public void setMenus(Vector<?> menus) {
		this.menus = menus;
	}
	public String getMenuAnchor() {
		return menuAnchor;
	}
	public void setMenuAnchor(String menuAnchor) {
		this.menuAnchor = menuAnchor;
	}
	public String getMenuWindowMode() {
		return menuWindowMode;
	}
	public void setMenuWindowMode(String menuWindowMode) {
		this.menuWindowMode = menuWindowMode;
	}
	public String getMenuWindowTarget() {
		return menuWindowTarget;
	}
	public void setMenuWindowTarget(String menuWindowTarget) {
		this.menuWindowTarget = menuWindowTarget;
	}
	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

}
