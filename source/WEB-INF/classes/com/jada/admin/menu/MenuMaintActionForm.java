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

package com.jada.admin.menu;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminMaintActionForm;

public class MenuMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -1391921620077763902L;
	String siteDomainId;
	String jsonMenuList;
	String menuId;
	String menuParentId;
	String menuName;
	boolean menuNameLangFlag;
	String menuNameLang;
	String seqNum;
	String menuUrl;
	String menuType;
	String menuWindowTarget;
	String menuWindowMode;
	String menuPosition;
	boolean published;
	String mode;
	String createMode;
	MenuDisplayForm childrenMenus[];
	String menuSetName;
	String createMenuSetName;
	String removeMenuId;
	String createMenuId;
	boolean isSequence;
	String itemId;
	String itemNum;
	String itemShortDesc;
	String contentId;
	String contentTitle;
	String catId;
	String catShortTitle;
	public String getCatShortTitle() {
		return catShortTitle;
	}
	public void setCatShortTitle(String catShortTitle) {
		this.catShortTitle = catShortTitle;
	}
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getContentTitle() {
		return contentTitle;
	}
	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemNum() {
		return itemNum;
	}
	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}
	public String getItemShortDesc() {
		return itemShortDesc;
	}
	public void setItemShortDesc(String itemShortDesc) {
		this.itemShortDesc = itemShortDesc;
	}
	public MenuDisplayForm getChildrenMenu(int index) {
		return childrenMenus[index];
	}
	public String getCreateMenuId() {
		return createMenuId;
	}
	public void setCreateMenuId(String createMenuId) {
		this.createMenuId = createMenuId;
	}
	public String getCreateMode() {
		return createMode;
	}
	public void setCreateMode(String createMode) {
		this.createMode = createMode;
	}
	public boolean isSequence() {
		return isSequence;
	}
	public void setSequence(boolean isSequence) {
		this.isSequence = isSequence;
	}
	public String getRemoveMenuId() {
		return removeMenuId;
	}
	public void setRemoveMenuId(String removeMenuId) {
		this.removeMenuId = removeMenuId;
	}
	public String getMenuSetName() {
		return menuSetName;
	}
	public void setMenuSetName(String menuSetName) {
		this.menuSetName = menuSetName;
	}
	public MenuDisplayForm[] getChildrenMenus() {
		return childrenMenus;
	}
	public void setChildrenMenus(MenuDisplayForm[] childrenMenus) {
		this.childrenMenus = childrenMenus;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuPosition() {
		return menuPosition;
	}
	public void setMenuPosition(String menuPosition) {
		this.menuPosition = menuPosition;
	}
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
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
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		String CHILDMENUS = "childrenMenu.*menuId";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(CHILDMENUS)) {
				count++;
			}
		}
		childrenMenus = new MenuDisplayForm[count];
		for (int i = 0; i < childrenMenus.length; i++) {
			childrenMenus[i] = new MenuDisplayForm();
		}
	}
	public String getMenuParentId() {
		return menuParentId;
	}
	public void setMenuParentId(String menuParentId) {
		this.menuParentId = menuParentId;
	}
	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
	}
	public String getCreateMenuSetName() {
		return createMenuSetName;
	}
	public void setCreateMenuSetName(String createMenuSetName) {
		this.createMenuSetName = createMenuSetName;
	}
	public String getMenuNameLang() {
		return menuNameLang;
	}
	public void setMenuNameLang(String menuNameLang) {
		this.menuNameLang = menuNameLang;
	}
	public boolean isMenuNameLangFlag() {
		return menuNameLangFlag;
	}
	public void setMenuNameLangFlag(boolean menuNameLangFlag) {
		this.menuNameLangFlag = menuNameLangFlag;
	}
	public String getJsonMenuList() {
		return jsonMenuList;
	}
	public void setJsonMenuList(String jsonMenuList) {
		this.jsonMenuList = jsonMenuList;
	}
	public String getSiteDomainId() {
		return siteDomainId;
	}
	public void setSiteDomainId(String siteDomainId) {
		this.siteDomainId = siteDomainId;
	}
}
