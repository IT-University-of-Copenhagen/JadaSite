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

package com.jada.admin.item;

import java.util.Enumeration;

import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminListingActionForm;
import com.jada.ui.dropdown.DropDownMenu;

public class ItemListingActionForm extends AdminListingActionForm {
	private static final long serialVersionUID = 5453299448313335930L;
	String srItemShortDesc;
	String srItemSkuCd;
	String srItemNum;
	String srItemUpcCd;
	String srPublished;
	String srItemPublishOnStart;
	String srItemPublishOnEnd;
	String srItemExpireOnStart;
	String srItemExpireOnEnd;
	String srUpdateBy;
	String srCreateBy;
	String srCatIds[];
	String srSelectUsers[];
	boolean srItemTypeRegular;
	boolean srItemTypeTemplate;
	boolean srItemTypeSku;
	boolean srItemTypeStaticBundle;
	boolean srItemTypeRecommandBundle;
	String jsonCategoryTree;
	DropDownMenu srCategoryTree;
    String srSelectedCategories[];
    
    ItemDisplayForm items[];
    String itemIds[];
    
    public ItemDisplayForm getItem(int index) {
    	return items[index];
    }
	public void reset(ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
		if (request.getParameter("srItemNum") != null) {
			srItemTypeRegular = false;
			srItemTypeTemplate = false;
			srItemTypeSku = false;
			srItemTypeStaticBundle = false;
			srItemTypeRecommandBundle = false;
		}
		
		srSelectedCategories = null;
		String ITEMDETAIL = "item.*itemId";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(ITEMDETAIL)) {
				count++;
			}
		}
		items = new ItemDisplayForm[count];
		for (int i = 0; i < items.length; i++) {
			items[i] = new ItemDisplayForm();
		}
	}
	public String getSrCreateBy() {
		return srCreateBy;
	}
	public void setSrCreateBy(String srCreateBy) {
		this.srCreateBy = srCreateBy;
	}
	public DropDownMenu getSrCategoryTree() {
		return srCategoryTree;
	}
	public void setSrCategoryTree(DropDownMenu srCategoryTree) {
		this.srCategoryTree = srCategoryTree;
	}
	public String getSrItemExpireOnEnd() {
		return srItemExpireOnEnd;
	}
	public void setSrItemExpireOnEnd(String srItemExpireOnEnd) {
		this.srItemExpireOnEnd = srItemExpireOnEnd;
	}
	public String getSrItemExpireOnStart() {
		return srItemExpireOnStart;
	}
	public void setSrItemExpireOnStart(String srItemExpireOnStart) {
		this.srItemExpireOnStart = srItemExpireOnStart;
	}
	public String getSrItemNum() {
		return srItemNum;
	}
	public void setSrItemNum(String srItemNum) {
		this.srItemNum = srItemNum;
	}
	public String getSrItemPublishOnEnd() {
		return srItemPublishOnEnd;
	}
	public void setSrItemPublishOnEnd(String srItemPublishOnEnd) {
		this.srItemPublishOnEnd = srItemPublishOnEnd;
	}
	public String getSrItemPublishOnStart() {
		return srItemPublishOnStart;
	}
	public void setSrItemPublishOnStart(String srItemPublishOnStart) {
		this.srItemPublishOnStart = srItemPublishOnStart;
	}
	public String getSrItemShortDesc() {
		return srItemShortDesc;
	}
	public void setSrItemShortDesc(String srItemShortDesc) {
		this.srItemShortDesc = srItemShortDesc;
	}
	public String getSrItemUpcCd() {
		return srItemUpcCd;
	}
	public void setSrItemUpcCd(String srItemUpcCd) {
		this.srItemUpcCd = srItemUpcCd;
	}
	public String[] getSrCatIds() {
		return srCatIds;
	}
	public void setSrCatIds(String[] srCatIds) {
		this.srCatIds = srCatIds;
	}
	public String[] getSrSelectedCategories() {
		return srSelectedCategories;
	}
	public void setSrSelectedCategories(String[] srSelectedCategories) {
		this.srSelectedCategories = srSelectedCategories;
	}
	public String[] getSrSelectUsers() {
		return srSelectUsers;
	}
	public void setSrSelectUsers(String[] srSelectUsers) {
		this.srSelectUsers = srSelectUsers;
	}
	public String getSrUpdateBy() {
		return srUpdateBy;
	}
	public void setSrUpdateBy(String srUpdateBy) {
		this.srUpdateBy = srUpdateBy;
	}
	public String getSrPublished() {
		return srPublished;
	}
	public void setSrPublished(String srPublished) {
		this.srPublished = srPublished;
	}
	public ItemDisplayForm[] getItems() {
		return items;
	}
	public void setItems(ItemDisplayForm[] items) {
		this.items = items;
	}
	public String[] getItemIds() {
		return itemIds;
	}
	public void setItemIds(String[] itemIds) {
		this.itemIds = itemIds;
	}
	public String getJsonCategoryTree() {
		return jsonCategoryTree;
	}
	public void setJsonCategoryTree(String jsonCategoryTree) {
		this.jsonCategoryTree = jsonCategoryTree;
	}
	public String getSrItemSkuCd() {
		return srItemSkuCd;
	}
	public void setSrItemSkuCd(String srItemSkuCd) {
		this.srItemSkuCd = srItemSkuCd;
	}
	public boolean isSrItemTypeRegular() {
		return srItemTypeRegular;
	}
	public void setSrItemTypeRegular(boolean srItemTypeRegular) {
		this.srItemTypeRegular = srItemTypeRegular;
	}
	public boolean isSrItemTypeTemplate() {
		return srItemTypeTemplate;
	}
	public void setSrItemTypeTemplate(boolean srItemTypeTemplate) {
		this.srItemTypeTemplate = srItemTypeTemplate;
	}
	public boolean isSrItemTypeStaticBundle() {
		return srItemTypeStaticBundle;
	}
	public void setSrItemTypeStaticBundle(boolean srItemTypeStaticBundle) {
		this.srItemTypeStaticBundle = srItemTypeStaticBundle;
	}
	public boolean isSrItemTypeRecommandBundle() {
		return srItemTypeRecommandBundle;
	}
	public void setSrItemTypeRecommandBundle(boolean srItemTypeRecommandBundle) {
		this.srItemTypeRecommandBundle = srItemTypeRecommandBundle;
	}
	public boolean isSrItemTypeSku() {
		return srItemTypeSku;
	}
	public void setSrItemTypeSku(boolean srItemTypeSku) {
		this.srItemTypeSku = srItemTypeSku;
	}
}
