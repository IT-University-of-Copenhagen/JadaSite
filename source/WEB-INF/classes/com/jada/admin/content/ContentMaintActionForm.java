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

package com.jada.admin.content;

import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;

import com.jada.admin.AdminMaintActionForm;
import com.jada.ui.dropdown.DropDownMenu;

public class ContentMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -4059929765478694408L;
	public String encodeFields[] = {"contentTitle", "contentTitleLang", "contentShortDesc", "contentShortDescLang", "contentDesc", "contentDescLang"};
	String mode;
	String contentId;
	String contentTitle;
	boolean contentTitleLangFlag;
	String contentTitleLang;
	String contentShortDesc;
	boolean contentShortDescLangFlag;
	String contentShortDescLang;
	String contentDesc;
	boolean contentDescLangFlag;
	String contentDescLang;
	String pageTitle;
	boolean pageTitleLangFlag;
	String pageTitleLang;
	String contentHitCounter;
	String contentPublishOn;
	String contentExpireOn;
	String contentRating;
	String contentRatingCount;
	boolean contentImageOverride;
	boolean published;
	String recUpdateBy;
	String recUpdateDatetime;
	String recCreateBy;
	String recCreateDatetime;
	String key;
	DropDownMenu menuList[];
	DropDownMenu categoryTree;
	String imagesOverride;
	String metaKeywords;
	String metaDescription;
	boolean metaKeywordsLangFlag;
	String metaKeywordsLang;
	boolean metaDescriptionLangFlag;
	String metaDescriptionLang;
	
	String menuUrl;
	String menuWindowTarget;
	String menuWindowMode;
	
	ContentMenuDisplayForm selectedMenus[];
	int selectedMenusCount;
	String removeMenus[];
	String addMenus[];
	String selectedCategory;
	String addCategory;
	String addCategories[];
	String removeCategories[];
	LabelValueBean defaultImageId;
	String createDefaultImageId;
	LabelValueBean imageIds[];
	String removeImages[];
	LabelValueBean siteDomains[];
	
	String contentRelatedId;
	String contentRelatedIds[];
	
	FormFile file;
	
	public DropDownMenu[] getMenuList() {
		return menuList;
	}
	public void setMenuList(DropDownMenu[] menuList) {
		this.menuList = menuList;
	}
	public String getRecCreateBy() {
		return recCreateBy;
	}
	public void setRecCreateBy(String recCreateBy) {
		this.recCreateBy = recCreateBy;
	}
	public String getRecCreateDatetime() {
		return recCreateDatetime;
	}
	public void setRecCreateDatetime(String recCreateDatetime) {
		this.recCreateDatetime = recCreateDatetime;
	}
	public String getRecUpdateBy() {
		return recUpdateBy;
	}
	public void setRecUpdateBy(String recUpdateBy) {
		this.recUpdateBy = recUpdateBy;
	}
	public String getRecUpdateDatetime() {
		return recUpdateDatetime;
	}
	public void setRecUpdateDatetime(String recUpdateDatetime) {
		this.recUpdateDatetime = recUpdateDatetime;
	}
	public String getContentDesc() {
		return contentDesc;
	}
	public void setContentDesc(String contentDesc) {
		this.contentDesc = contentDesc;
	}
	public String getContentExpireOn() {
		return contentExpireOn;
	}
	public void setContentExpireOn(String contentExpireOn) {
		this.contentExpireOn = contentExpireOn;
	}
	public String getContentHitCounter() {
		return contentHitCounter;
	}
	public void setContentHitCounter(String contentHitCounter) {
		this.contentHitCounter = contentHitCounter;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getContentPublishOn() {
		return contentPublishOn;
	}
	public void setContentPublishOn(String contentPublishOn) {
		this.contentPublishOn = contentPublishOn;
	}
	public String getContentShortDesc() {
		return contentShortDesc;
	}
	public void setContentShortDesc(String contentShortDesc) {
		this.contentShortDesc = contentShortDesc;
	}
	public String getContentTitle() {
		return contentTitle;
	}
	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
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
	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
	}
	public String[] getRemoveMenus() {
		return removeMenus;
	}
	public void setRemoveMenus(String[] removeMenus) {
		this.removeMenus = removeMenus;
	}
	public String[] getAddMenus() {
		return addMenus;
	}
	public void setAddMenus(String[] addMenus) {
		this.addMenus = addMenus;
	}
	public FormFile getFile() {
		return file;
	}
	public void setFile(FormFile file) {
		this.file = file;
	}
	public String[] getRemoveImages() {
		return removeImages;
	}
	public void setRemoveImages(String[] removeImages) {
		this.removeImages = removeImages;
	}
	public LabelValueBean[] getImageIds() {
		return imageIds;
	}
	public void setImageIds(LabelValueBean[] imageIds) {
		this.imageIds = imageIds;
	}
	public String getAddCategory() {
		return addCategory;
	}
	public void setAddCategory(String addCategory) {
		this.addCategory = addCategory;
	}
	public String getSelectedCategory() {
		return selectedCategory;
	}
	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}
	public String getCreateDefaultImageId() {
		return createDefaultImageId;
	}
	public void setCreateDefaultImageId(String createDefaultImageId) {
		this.createDefaultImageId = createDefaultImageId;
	}
	public LabelValueBean getDefaultImageId() {
		return defaultImageId;
	}
	public void setDefaultImageId(LabelValueBean defaultImageId) {
		this.defaultImageId = defaultImageId;
	}
	public int getSelectedMenusCount() {
		return selectedMenusCount;
	}
	public void setSelectedMenusCount(int selectedMenusCount) {
		this.selectedMenusCount = selectedMenusCount;
	}
	public DropDownMenu getCategoryTree() {
		return categoryTree;
	}
	public void setCategoryTree(DropDownMenu categoryTree) {
		this.categoryTree = categoryTree;
	}
	public ContentMenuDisplayForm[] getSelectedMenus() {
		return selectedMenus;
	}
	public void setSelectedMenus(ContentMenuDisplayForm[] selectedMenus) {
		this.selectedMenus = selectedMenus;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getContentRating() {
		return contentRating;
	}
	public void setContentRating(String contentRating) {
		this.contentRating = contentRating;
	}
	public String getContentRatingCount() {
		return contentRatingCount;
	}
	public void setContentRatingCount(String contentRatingCount) {
		this.contentRatingCount = contentRatingCount;
	}
	public String getContentDescLang() {
		return contentDescLang;
	}
	public void setContentDescLang(String contentDescLang) {
		this.contentDescLang = contentDescLang;
	}
	public boolean isContentDescLangFlag() {
		return contentDescLangFlag;
	}
	public void setContentDescLangFlag(boolean contentDescLangFlag) {
		this.contentDescLangFlag = contentDescLangFlag;
	}
	public String getContentShortDescLang() {
		return contentShortDescLang;
	}
	public void setContentShortDescLang(String contentShortDescLang) {
		this.contentShortDescLang = contentShortDescLang;
	}
	public boolean isContentShortDescLangFlag() {
		return contentShortDescLangFlag;
	}
	public void setContentShortDescLangFlag(boolean contentShortDescLangFlag) {
		this.contentShortDescLangFlag = contentShortDescLangFlag;
	}
	public String getContentTitleLang() {
		return contentTitleLang;
	}
	public void setContentTitleLang(String contentTitleLang) {
		this.contentTitleLang = contentTitleLang;
	}
	public boolean isContentTitleLangFlag() {
		return contentTitleLangFlag;
	}
	public void setContentTitleLangFlag(boolean contentTitleLangFlag) {
		this.contentTitleLangFlag = contentTitleLangFlag;
	}
	public String getPageTitleLang() {
		return pageTitleLang;
	}
	public void setPageTitleLang(String pageTitleLang) {
		this.pageTitleLang = pageTitleLang;
	}
	public boolean isPageTitleLangFlag() {
		return pageTitleLangFlag;
	}
	public void setPageTitleLangFlag(boolean pageTitleLangFlag) {
		this.pageTitleLangFlag = pageTitleLangFlag;
	}
	public String getImagesOverride() {
		return imagesOverride;
	}
	public void setImagesOverride(String imagesOverride) {
		this.imagesOverride = imagesOverride;
	}
	public boolean isContentImageOverride() {
		return contentImageOverride;
	}
	public void setContentImageOverride(boolean contentImageOverride) {
		this.contentImageOverride = contentImageOverride;
	}
	public String getContentRelatedId() {
		return contentRelatedId;
	}
	public void setContentRelatedId(String contentRelatedId) {
		this.contentRelatedId = contentRelatedId;
	}
	public String[] getContentRelatedIds() {
		return contentRelatedIds;
	}
	public void setContentRelatedIds(String[] contentRelatedIds) {
		this.contentRelatedIds = contentRelatedIds;
	}
	public String[] getAddCategories() {
		return addCategories;
	}
	public void setAddCategories(String[] addCategories) {
		this.addCategories = addCategories;
	}
	public String[] getRemoveCategories() {
		return removeCategories;
	}
	public void setRemoveCategories(String[] removeCategories) {
		this.removeCategories = removeCategories;
	}
	public LabelValueBean[] getSiteDomains() {
		return siteDomains;
	}
	public void setSiteDomains(LabelValueBean[] siteDomains) {
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
