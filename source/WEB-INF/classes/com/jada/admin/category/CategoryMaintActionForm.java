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

package com.jada.admin.category;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminMaintActionForm;

public class CategoryMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -331460781772387199L;
	String jsonCategoryTree;
	String catId;
	String categoryParentId;
	String catTitle;
	String metaKeywords;
	String metaDescription;
	boolean catTitleLangFlag;
	String catTitleLang;
	String catShortTitle;
	boolean catShortTitleLangFlag;
	String catShortTitleLang;
	String catDesc;
	boolean catDescLangFlag;
	String catDescLang;
	boolean metaKeywordsLangFlag;
	String metaKeywordsLang;
	boolean metaDescriptionLangFlag;
	String metaDescriptionLang;
	boolean published;
	String mode;
	String createCatId;
	String createMode;
	CategoryDisplayForm childrenCategories[];
	SiteDomainDisplayForm siteDomains[];
	String customAttribIds[];
	String customAttribId;
	boolean sequence;
	public boolean isSequence() {
		return sequence;
	}
	public void setSequence(boolean sequence) {
		this.sequence = sequence;
	}
	public CategoryDisplayForm[] getChildrenCategories() {
		return childrenCategories;
	}
	public void setChildrenCategories(CategoryDisplayForm[] childrenCategories) {
		this.childrenCategories = childrenCategories;
	}
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		published = false;
		String CHILDSECTIONS = "childrenCategory.*catId";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(CHILDSECTIONS)) {
				count++;
			}
		}
		childrenCategories = new CategoryDisplayForm[count];
		for (int i = 0; i < childrenCategories.length; i++) {
			childrenCategories[i] = new CategoryDisplayForm();
		}
		
		String SITEDOMAINS = "siteDomain.*siteDomainId";
		count = 0;
		enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(SITEDOMAINS)) {
				count++;
			}
		}
		siteDomains = new SiteDomainDisplayForm[count];
		for (int i = 0; i < siteDomains.length; i++) {
			siteDomains[i] = new SiteDomainDisplayForm();
		}
	}
	public CategoryDisplayForm getChildrenCategory(int index) {
		return childrenCategories[index];
	}
	public SiteDomainDisplayForm getSiteDomain(int index) {
		return siteDomains[index];
	}
	public String getCreateCatId() {
		return createCatId;
	}
	public void setCreateCatId(String createCatId) {
		this.createCatId = createCatId;
	}
	public String getCreateMode() {
		return createMode;
	}
	public void setCreateMode(String createMode) {
		this.createMode = createMode;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
	}
	public String getCatDesc() {
		return catDesc;
	}
	public void setCatDesc(String catDesc) {
		this.catDesc = catDesc;
	}
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public String getCategoryParentId() {
		return categoryParentId;
	}
	public void setCategoryParentId(String categoryParentId) {
		this.categoryParentId = categoryParentId;
	}
	public String getCatTitle() {
		return catTitle;
	}
	public void setCatTitle(String catTitle) {
		this.catTitle = catTitle;
	}
	public String getCatShortTitle() {
		return catShortTitle;
	}
	public void setCatShortTitle(String catShortTitle) {
		this.catShortTitle = catShortTitle;
	}
	public String getCatDescLang() {
		return catDescLang;
	}
	public void setCatDescLang(String catDescLang) {
		this.catDescLang = catDescLang;
	}
	public boolean isCatDescLangFlag() {
		return catDescLangFlag;
	}
	public void setCatDescLangFlag(boolean catDescLangFlag) {
		this.catDescLangFlag = catDescLangFlag;
	}
	public String getCatShortTitleLang() {
		return catShortTitleLang;
	}
	public void setCatShortTitleLang(String catShortTitleLang) {
		this.catShortTitleLang = catShortTitleLang;
	}
	public boolean isCatShortTitleLangFlag() {
		return catShortTitleLangFlag;
	}
	public void setCatShortTitleLangFlag(boolean catShortTitleLangFlag) {
		this.catShortTitleLangFlag = catShortTitleLangFlag;
	}
	public String getCatTitleLang() {
		return catTitleLang;
	}
	public void setCatTitleLang(String catTitleLang) {
		this.catTitleLang = catTitleLang;
	}
	public boolean isCatTitleLangFlag() {
		return catTitleLangFlag;
	}
	public void setCatTitleLangFlag(boolean catTitleLangFlag) {
		this.catTitleLangFlag = catTitleLangFlag;
	}
	public String getJsonCategoryTree() {
		return jsonCategoryTree;
	}
	public void setJsonCategoryTree(String jsonCategoryTree) {
		this.jsonCategoryTree = jsonCategoryTree;
	}
	public String[] getCustomAttribIds() {
		return customAttribIds;
	}
	public void setCustomAttribIds(String[] customAttribIds) {
		this.customAttribIds = customAttribIds;
	}
	public String getCustomAttribId() {
		return customAttribId;
	}
	public void setCustomAttribId(String customAttribId) {
		this.customAttribId = customAttribId;
	}
	public SiteDomainDisplayForm[] getSiteDomains() {
		return siteDomains;
	}
	public void setSiteDomains(SiteDomainDisplayForm[] siteDomains) {
		this.siteDomains = siteDomains;
	}
	public String getMetaKeywords() {
		return metaKeywords;
	}
	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}
	public String getMetaDescription() {
		return metaDescription;
	}
	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}
	public boolean isMetaKeywordsLangFlag() {
		return metaKeywordsLangFlag;
	}
	public void setMetaKeywordsLangFlag(boolean metaKeywordsLangFlag) {
		this.metaKeywordsLangFlag = metaKeywordsLangFlag;
	}
	public String getMetaKeywordsLang() {
		return metaKeywordsLang;
	}
	public void setMetaKeywordsLang(String metaKeywordsLang) {
		this.metaKeywordsLang = metaKeywordsLang;
	}
	public boolean isMetaDescriptionLangFlag() {
		return metaDescriptionLangFlag;
	}
	public void setMetaDescriptionLangFlag(boolean metaDescriptionLangFlag) {
		this.metaDescriptionLangFlag = metaDescriptionLangFlag;
	}
	public String getMetaDescriptionLang() {
		return metaDescriptionLang;
	}
	public void setMetaDescriptionLang(String metaDescriptionLang) {
		this.metaDescriptionLang = metaDescriptionLang;
	}
}
