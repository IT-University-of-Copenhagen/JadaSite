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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;

import com.jada.admin.AdminMaintActionForm;
import com.jada.ui.dropdown.DropDownMenu;

public class ItemMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -1956493469823900742L;
	public String encodeFields[] = {"itemShortDesc", "itemShortDescLang", "itemDesc", "itemDescLang"};
	String mode;
	String key;
	String itemId;
	String siteId;
	String productClassId;
	String itemNum;
	String itemUpcCd;
	String itemTypeCd;
	String itemTypeDesc;
	String itemSkuCd;
	boolean itemSellable;
	String itemShortDesc;
	boolean itemShortDescLangFlag;
	String itemShortDescLang;
	String itemDesc;
	boolean itemDescLangFlag;
	String itemDescLang;
	String pageTitle;
	boolean pageTitleLangFlag;
	String pageTitleLang;
	String itemCost;
	String itemPrice;
	boolean itemPriceCurrFlag;
	String itemPriceCurr;
	String itemSpecPrice;
	boolean itemSpecPriceCurrFlag;
	String itemSpecPriceCurr;
	String itemSpecPublishOn;
	String itemSpecExpireOn;
	String itemHitCounter;
	String itemRating;
	String itemRatingCount;
	String itemQty;
	String itemBookedQty;
	String itemPublishOn;
	String itemExpireOn;
	boolean itemImageOverride;
	String metaKeywords;
	String metaDescription;
	boolean metaKeywordsLangFlag;
	String metaKeywordsLang;
	boolean metaDescriptionLangFlag;
	String metaDescriptionLang;
	boolean published;
	String recUpdateBy;
	String recUpdateDatetime;
	String recCreateBy;
	String recCreateDatetime;
	String imagesOverride;
	
	String itemAdjQty;
	String itemAdjBookedQty;
	
	String itemTierQty;
	String itemTierPrice;
	String itemTierPricePublishOn;
	String itemTierPriceExpireOn;
	boolean itemTierPriceOverride;
	String custClassId;
	
	String shippingTypeId;
	LabelValueBean shippingTypes[];
	LabelValueBean productClasses[];
	DropDownMenu menuList[];
	String menuUrl;
	String menuWindowTarget;
	String menuWindowMode;
	ItemMenuDisplayForm selectedMenus[];
	int selectedMenusCount;
	String removeMenus[];
	String addMenus[];
	ItemTierPriceDisplayForm itemTierPrices[];
	String itemTierPriceId;
	String customAttribGroupId;
	String customAttribGroupName;
	LabelValueBean customAttributeGroups[];
	ItemAttributeDetailDisplayForm itemAttributeDetails[];
	boolean itemAttribDetailValueLangFlag;

	DropDownMenu categoryTree;
	String selectedCategories[];
	String addCategories[];
	String removeCategories[];
	LabelValueBean defaultImageId;
	String createDefaultImageId;
	LabelValueBean imageIds[];
	String removeImages[];
	FormFile file;
	LabelValueBean custClasses[];
	LabelValueBean siteDomains[];
	ItemSiteDomainDisplayForm itemSiteDomains[];
	
	String itemRelatedId;
	String itemRelatedIds[];
	String itemUpSellId;
	String itemUpSellIds[];
	String itemCrossSellId;
	String itemCrossSellIds[];
	String itemChildId;
	String itemChildIds[];
	
	boolean itemSkusExist;
	boolean shareInventory;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		String id = "itemTierPrice.*itemTierPriceId";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(id)) {
				count++;
			}
		}
		itemTierPrices = new ItemTierPriceDisplayForm[count];
		for (int i = 0; i < itemTierPrices.length; i++) {
			itemTierPrices[i] = new ItemTierPriceDisplayForm();
		}
		
		id = "itemAttributeDetail.*itemAttribDetailId";
		count = 0;
		enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(id)) {
				count++;
			}
		}
		itemAttributeDetails = new ItemAttributeDetailDisplayForm[count];
		for (int i = 0; i < itemAttributeDetails.length; i++) {
			itemAttributeDetails[i] = new ItemAttributeDetailDisplayForm();
		}
		
		id = "itemSiteDomain.*siteDomainId";
		count = 0;
		enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(id)) {
				count++;
			}
		}
		itemSiteDomains = new ItemSiteDomainDisplayForm[count];
		for (int i = 0; i < itemSiteDomains.length; i++) {
			itemSiteDomains[i] = new ItemSiteDomainDisplayForm();
		}
	}
	public ItemAttributeDetailDisplayForm getItemAttributeDetail(int index) {
		return itemAttributeDetails[index];
	}
	public ItemSiteDomainDisplayForm getItemSiteDomain(int index) {
		return itemSiteDomains[index];
	}
	public ItemMenuDisplayForm[] getSelectedMenus() {
		return selectedMenus;
	}
	public void setSelectedMenus(ItemMenuDisplayForm[] selectedMenus) {
		this.selectedMenus = selectedMenus;
	}
	public FormFile getFile() {
		return file;
	}
	public void setFile(FormFile file) {
		this.file = file;
	}
	public LabelValueBean[] getImageIds() {
		return imageIds;
	}
	public void setImageIds(LabelValueBean[] imageIds) {
		this.imageIds = imageIds;
	}
	public String getItemBookedQty() {
		return itemBookedQty;
	}
	public void setItemBookedQty(String itemBookedQty) {
		this.itemBookedQty = itemBookedQty;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public String getItemExpireOn() {
		return itemExpireOn;
	}
	public void setItemExpireOn(String itemExpireOn) {
		this.itemExpireOn = itemExpireOn;
	}
	public String getItemHitCounter() {
		return itemHitCounter;
	}
	public void setItemHitCounter(String itemHitCounter) {
		this.itemHitCounter = itemHitCounter;
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
	public String getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getItemPublishOn() {
		return itemPublishOn;
	}
	public void setItemPublishOn(String itemPublishOn) {
		this.itemPublishOn = itemPublishOn;
	}
	public String getItemQty() {
		return itemQty;
	}
	public void setItemQty(String itemQty) {
		this.itemQty = itemQty;
	}
	public String getItemRating() {
		return itemRating;
	}
	public void setItemRating(String itemRating) {
		this.itemRating = itemRating;
	}
	public String getItemRatingCount() {
		return itemRatingCount;
	}
	public void setItemRatingCount(String itemRatingCount) {
		this.itemRatingCount = itemRatingCount;
	}
	public String getItemShortDesc() {
		return itemShortDesc;
	}
	public void setItemShortDesc(String itemShortDesc) {
		this.itemShortDesc = itemShortDesc;
	}
	public String getItemSpecExpireOn() {
		return itemSpecExpireOn;
	}
	public void setItemSpecExpireOn(String itemSpecExpireOn) {
		this.itemSpecExpireOn = itemSpecExpireOn;
	}
	public String getItemSpecPrice() {
		return itemSpecPrice;
	}
	public void setItemSpecPrice(String itemSpecPrice) {
		this.itemSpecPrice = itemSpecPrice;
	}
	public String getItemSpecPublishOn() {
		return itemSpecPublishOn;
	}
	public void setItemSpecPublishOn(String itemSpecPublishOn) {
		this.itemSpecPublishOn = itemSpecPublishOn;
	}
	public String getItemUpcCd() {
		return itemUpcCd;
	}
	public void setItemUpcCd(String itemUpcCd) {
		this.itemUpcCd = itemUpcCd;
	}
	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
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
	public String[] getRemoveImages() {
		return removeImages;
	}
	public void setRemoveImages(String[] removeImages) {
		this.removeImages = removeImages;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public LabelValueBean getDefaultImageId() {
		return defaultImageId;
	}
	public void setDefaultImageId(LabelValueBean defaultImageId) {
		this.defaultImageId = defaultImageId;
	}
	public String getCreateDefaultImageId() {
		return createDefaultImageId;
	}
	public void setCreateDefaultImageId(String createDefaultImageId) {
		this.createDefaultImageId = createDefaultImageId;
	}
	public String[] getAddMenus() {
		return addMenus;
	}
	public void setAddMenus(String[] addMenus) {
		this.addMenus = addMenus;
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
	public String[] getRemoveMenus() {
		return removeMenus;
	}
	public void setRemoveMenus(String[] removeMenus) {
		this.removeMenus = removeMenus;
	}
	public int getSelectedMenusCount() {
		return selectedMenusCount;
	}
	public void setSelectedMenusCount(int selectedMenusCount) {
		this.selectedMenusCount = selectedMenusCount;
	}
	public String getItemAdjBookedQty() {
		return itemAdjBookedQty;
	}
	public void setItemAdjBookedQty(String itemAdjBookedQty) {
		this.itemAdjBookedQty = itemAdjBookedQty;
	}
	public String getItemAdjQty() {
		return itemAdjQty;
	}
	public void setItemAdjQty(String itemAdjQty) {
		this.itemAdjQty = itemAdjQty;
	}
	public DropDownMenu[] getMenuList() {
		return menuList;
	}
	public void setMenuList(DropDownMenu[] menuList) {
		this.menuList = menuList;
	}
	public DropDownMenu getCategoryTree() {
		return categoryTree;
	}
	public void setCategoryTree(DropDownMenu categoryTree) {
		this.categoryTree = categoryTree;
	}
	public LabelValueBean[] getShippingTypes() {
		return shippingTypes;
	}
	public void setShippingTypes(LabelValueBean[] shippingTypes) {
		this.shippingTypes = shippingTypes;
	}
	public String getShippingTypeId() {
		return shippingTypeId;
	}
	public void setShippingTypeId(String shippingTypeId) {
		this.shippingTypeId = shippingTypeId;
	}
	public String getItemCost() {
		return itemCost;
	}
	public void setItemCost(String itemCost) {
		this.itemCost = itemCost;
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
	public String getItemDescLang() {
		return itemDescLang;
	}
	public void setItemDescLang(String itemDescLang) {
		this.itemDescLang = itemDescLang;
	}
	public boolean isItemDescLangFlag() {
		return itemDescLangFlag;
	}
	public void setItemDescLangFlag(boolean itemDescLangFlag) {
		this.itemDescLangFlag = itemDescLangFlag;
	}
	public String getItemShortDescLang() {
		return itemShortDescLang;
	}
	public void setItemShortDescLang(String itemShortDescLang) {
		this.itemShortDescLang = itemShortDescLang;
	}
	public boolean isItemShortDescLangFlag() {
		return itemShortDescLangFlag;
	}
	public void setItemShortDescLangFlag(boolean itemShortDescLangFlag) {
		this.itemShortDescLangFlag = itemShortDescLangFlag;
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
	public boolean isItemImageOverride() {
		return itemImageOverride;
	}
	public void setItemImageOverride(boolean itemImageOverride) {
		this.itemImageOverride = itemImageOverride;
	}
	public String getProductClassId() {
		return productClassId;
	}
	public void setProductClassId(String productClassId) {
		this.productClassId = productClassId;
	}
	public LabelValueBean[] getProductClasses() {
		return productClasses;
	}
	public void setProductClasses(LabelValueBean[] productClasses) {
		this.productClasses = productClasses;
	}
	public ItemTierPriceDisplayForm[] getItemTierPrices() {
		return itemTierPrices;
	}
	public void setItemTierPrices(ItemTierPriceDisplayForm[] itemTierPrices) {
		this.itemTierPrices = itemTierPrices;
	}
	public LabelValueBean[] getCustClasses() {
		return custClasses;
	}
	public void setCustClasses(LabelValueBean[] custClasses) {
		this.custClasses = custClasses;
	}
	public String getItemTierPriceId() {
		return itemTierPriceId;
	}
	public void setItemTierPriceId(String itemTierPriceId) {
		this.itemTierPriceId = itemTierPriceId;
	}
	public String getItemTierQty() {
		return itemTierQty;
	}
	public void setItemTierQty(String itemTierQty) {
		this.itemTierQty = itemTierQty;
	}
	public String getItemTierPrice() {
		return itemTierPrice;
	}
	public void setItemTierPrice(String itemTierPrice) {
		this.itemTierPrice = itemTierPrice;
	}
	public String getItemTierPricePublishOn() {
		return itemTierPricePublishOn;
	}
	public void setItemTierPricePublishOn(String itemTierPricePublishOn) {
		this.itemTierPricePublishOn = itemTierPricePublishOn;
	}
	public String getItemTierPriceExpireOn() {
		return itemTierPriceExpireOn;
	}
	public void setItemTierPriceExpireOn(String itemTierPriceExpireOn) {
		this.itemTierPriceExpireOn = itemTierPriceExpireOn;
	}
	public String getCustClassId() {
		return custClassId;
	}
	public void setCustClassId(String custClassId) {
		this.custClassId = custClassId;
	}
	public String getItemUpSellId() {
		return itemUpSellId;
	}
	public void setItemUpSellId(String itemUpSellId) {
		this.itemUpSellId = itemUpSellId;
	}
	public String[] getItemUpSellIds() {
		return itemUpSellIds;
	}
	public void setItemUpSellIds(String[] itemUpSellIds) {
		this.itemUpSellIds = itemUpSellIds;
	}
	public String[] getItemRelatedIds() {
		return itemRelatedIds;
	}
	public void setItemRelatedIds(String[] itemRelatedIds) {
		this.itemRelatedIds = itemRelatedIds;
	}
	public String getItemRelatedId() {
		return itemRelatedId;
	}
	public void setItemRelatedId(String itemRelatedId) {
		this.itemRelatedId = itemRelatedId;
	}
	public String getItemCrossSellId() {
		return itemCrossSellId;
	}
	public void setItemCrossSellId(String itemCrossSellId) {
		this.itemCrossSellId = itemCrossSellId;
	}
	public String[] getItemCrossSellIds() {
		return itemCrossSellIds;
	}
	public void setItemCrossSellIds(String[] itemCrossSellIds) {
		this.itemCrossSellIds = itemCrossSellIds;
	}
	public String getCustomAttribGroupId() {
		return customAttribGroupId;
	}
	public void setCustomAttribGroupId(String customAttribGroupId) {
		this.customAttribGroupId = customAttribGroupId;
	}
	public LabelValueBean[] getCustomAttributeGroups() {
		return customAttributeGroups;
	}
	public void setCustomAttributeGroups(LabelValueBean[] customAttributeGroups) {
		this.customAttributeGroups = customAttributeGroups;
	}
	public ItemAttributeDetailDisplayForm[] getItemAttributeDetails() {
		return itemAttributeDetails;
	}
	public void setItemAttributeDetails(
			ItemAttributeDetailDisplayForm[] itemAttributeDetails) {
		this.itemAttributeDetails = itemAttributeDetails;
	}
	public boolean isItemPriceCurrFlag() {
		return itemPriceCurrFlag;
	}
	public void setItemPriceCurrFlag(boolean itemPriceCurrFlag) {
		this.itemPriceCurrFlag = itemPriceCurrFlag;
	}
	public String getItemPriceCurr() {
		return itemPriceCurr;
	}
	public void setItemPriceCurr(String itemPriceCurr) {
		this.itemPriceCurr = itemPriceCurr;
	}
	public boolean isItemTierPriceOverride() {
		return itemTierPriceOverride;
	}
	public void setItemTierPriceOverride(boolean itemTierPriceOverride) {
		this.itemTierPriceOverride = itemTierPriceOverride;
	}
	public boolean isItemSpecPriceCurrFlag() {
		return itemSpecPriceCurrFlag;
	}
	public void setItemSpecPriceCurrFlag(boolean itemSpecPriceCurrFlag) {
		this.itemSpecPriceCurrFlag = itemSpecPriceCurrFlag;
	}
	public String getItemSpecPriceCurr() {
		return itemSpecPriceCurr;
	}
	public void setItemSpecPriceCurr(String itemSpecPriceCurr) {
		this.itemSpecPriceCurr = itemSpecPriceCurr;
	}
	public boolean isItemAttribDetailValueLangFlag() {
		return itemAttribDetailValueLangFlag;
	}
	public void setItemAttribDetailValueLangFlag(
			boolean itemAttribDetailValueLangFlag) {
		this.itemAttribDetailValueLangFlag = itemAttribDetailValueLangFlag;
	}
	public String getItemTypeCd() {
		return itemTypeCd;
	}
	public void setItemTypeCd(String itemTypeCd) {
		this.itemTypeCd = itemTypeCd;
	}
	public String getItemSkuCd() {
		return itemSkuCd;
	}
	public void setItemSkuCd(String itemSkuCd) {
		this.itemSkuCd = itemSkuCd;
	}
	public String getItemTypeDesc() {
		return itemTypeDesc;
	}
	public void setItemTypeDesc(String itemTypeDesc) {
		this.itemTypeDesc = itemTypeDesc;
	}
	public boolean isItemSellable() {
		return itemSellable;
	}
	public void setItemSellable(boolean itemSellable) {
		this.itemSellable = itemSellable;
	}
	public String getCustomAttribGroupName() {
		return customAttribGroupName;
	}
	public void setCustomAttribGroupName(String customAttribGroupName) {
		this.customAttribGroupName = customAttribGroupName;
	}
	public boolean isItemSkusExist() {
		return itemSkusExist;
	}
	public void setItemSkusExist(boolean itemSkusExist) {
		this.itemSkusExist = itemSkusExist;
	}
	public String getItemChildId() {
		return itemChildId;
	}
	public void setItemChildId(String itemChildId) {
		this.itemChildId = itemChildId;
	}
	public String[] getItemChildIds() {
		return itemChildIds;
	}
	public void setItemChildIds(String[] itemChildIds) {
		this.itemChildIds = itemChildIds;
	}
	public String[] getSelectedCategories() {
		return selectedCategories;
	}
	public void setSelectedCategories(String[] selectedCategories) {
		this.selectedCategories = selectedCategories;
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
	public ItemSiteDomainDisplayForm[] getItemSiteDomains() {
		return itemSiteDomains;
	}
	public void setItemSiteDomains(ItemSiteDomainDisplayForm[] itemSiteDomains) {
		this.itemSiteDomains = itemSiteDomains;
	}
	public boolean isShareInventory() {
		return shareInventory;
	}
	public void setShareInventory(boolean shareInventory) {
		this.shareInventory = shareInventory;
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
