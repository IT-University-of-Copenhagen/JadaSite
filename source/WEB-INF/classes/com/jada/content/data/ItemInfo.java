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

import java.util.Vector;

import com.jada.util.Constants;

public class ItemInfo extends DataInfo {
	String CUSTOM_ATTRIBUTE_TYPE_CUST_SELECT_DROPDOWN = String.valueOf(Constants.CUSTOM_ATTRIBUTE_TYPE_CUST_SELECT_DROPDOWN);
	String CUSTOM_ATTRIBUTE_TYPE_USER_SELECT_DROPDOWN = String.valueOf(Constants.CUSTOM_ATTRIBUTE_TYPE_USER_SELECT_DROPDOWN);
	String CUSTOM_ATTRIBUTE_TYPE_USER_INPUT = String.valueOf(Constants.CUSTOM_ATTRIBUTE_TYPE_USER_INPUT);
	String CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP = String.valueOf(Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP);
	String CUSTOM_ATTRIBUTE_TYPE_CUST_INPUT = String.valueOf(Constants.CUSTOM_ATTRIBUTE_TYPE_CUST_INPUT);
	
	String infoType = "item";
	boolean special;
	boolean outOfStock;
	String itemNaturalKey;
	String itemId;
	String itemNum;
	String itemUpcCd;
	String itemSkuCd;
	String itemTypeCd;
	String itemShortDesc;
	String itemDesc;
	String pageTitle;
	String metaKeywords;
	String metaDescription;
	boolean itemPriceRange;
	String itemPriceFrom;
	String itemPriceTo;
	String itemPrice;
	String itemSpecPrice;
	String itemSpecPublishOn;
	String itemSpecExpireOn;
	String itemHitCounter;
	String commentRating;
	String commentRatingPercentage;
	String commentRatingPercentageNumber;
	String itemQty;
	String itemBookedQty;
	String itemPublishOn;
	String itemExpireOn;
	String itemUrl;
	String itemCommentUrl;
	String itemCommentUpdateUrl;
	String itemDefaultImageUrl;
	String itemUpdateName;
	String recUpdateBy;
	String recUpdateDatetime;
	String recCreateBy;
	String recCreateDatetime;
	Vector<?> itemImageUrls;
	int commentCount;
	Vector<?> comments;
	Vector<?> itemTierPrices;
	String itemOrderedQty;
	Vector<?> relatedItems;
	Vector<?> upSellItems;
	Vector<?> bundleItems;
	Vector<?> skuItems;
	Vector<?> itemAttribDetailInfos;
	Object custom;
	public String getCUSTOM_ATTRIBUTE_TYPE_CUST_SELECT_DROPDOWN() {
		return CUSTOM_ATTRIBUTE_TYPE_CUST_SELECT_DROPDOWN;
	}
	public void setCUSTOM_ATTRIBUTE_TYPE_CUST_SELECT_DROPDOWN(
			String custom_attribute_type_cust_select_dropdown) {
		CUSTOM_ATTRIBUTE_TYPE_CUST_SELECT_DROPDOWN = custom_attribute_type_cust_select_dropdown;
	}
	public String getCUSTOM_ATTRIBUTE_TYPE_USER_SELECT_DROPDOWN() {
		return CUSTOM_ATTRIBUTE_TYPE_USER_SELECT_DROPDOWN;
	}
	public void setCUSTOM_ATTRIBUTE_TYPE_USER_SELECT_DROPDOWN(
			String custom_attribute_type_user_select_dropdown) {
		CUSTOM_ATTRIBUTE_TYPE_USER_SELECT_DROPDOWN = custom_attribute_type_user_select_dropdown;
	}
	public String getCUSTOM_ATTRIBUTE_TYPE_USER_INPUT() {
		return CUSTOM_ATTRIBUTE_TYPE_USER_INPUT;
	}
	public void setCUSTOM_ATTRIBUTE_TYPE_USER_INPUT(
			String custom_attribute_type_user_input) {
		CUSTOM_ATTRIBUTE_TYPE_USER_INPUT = custom_attribute_type_user_input;
	}
	public String getCUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP() {
		return CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP;
	}
	public void setCUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP(
			String custom_attribute_type_sku_makeup) {
		CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP = custom_attribute_type_sku_makeup;
	}
	public String getCUSTOM_ATTRIBUTE_TYPE_CUST_INPUT() {
		return CUSTOM_ATTRIBUTE_TYPE_CUST_INPUT;
	}
	public void setCUSTOM_ATTRIBUTE_TYPE_CUST_INPUT(
			String custom_attribute_type_cust_input) {
		CUSTOM_ATTRIBUTE_TYPE_CUST_INPUT = custom_attribute_type_cust_input;
	}
	public String getInfoType() {
		return infoType;
	}
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	public boolean isSpecial() {
		return special;
	}
	public void setSpecial(boolean special) {
		this.special = special;
	}
	public boolean isOutOfStock() {
		return outOfStock;
	}
	public void setOutOfStock(boolean outOfStock) {
		this.outOfStock = outOfStock;
	}
	public String getItemNaturalKey() {
		return itemNaturalKey;
	}
	public void setItemNaturalKey(String itemNaturalKey) {
		this.itemNaturalKey = itemNaturalKey;
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
	public String getItemUpcCd() {
		return itemUpcCd;
	}
	public void setItemUpcCd(String itemUpcCd) {
		this.itemUpcCd = itemUpcCd;
	}
	public String getItemShortDesc() {
		return itemShortDesc;
	}
	public void setItemShortDesc(String itemShortDesc) {
		this.itemShortDesc = itemShortDesc;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
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
	public String getItemSpecExpireOn() {
		return itemSpecExpireOn;
	}
	public void setItemSpecExpireOn(String itemSpecExpireOn) {
		this.itemSpecExpireOn = itemSpecExpireOn;
	}
	public String getItemHitCounter() {
		return itemHitCounter;
	}
	public void setItemHitCounter(String itemHitCounter) {
		this.itemHitCounter = itemHitCounter;
	}
	public String getCommentRating() {
		return commentRating;
	}
	public void setCommentRating(String commentRating) {
		this.commentRating = commentRating;
	}
	public String getCommentRatingPercentage() {
		return commentRatingPercentage;
	}
	public void setCommentRatingPercentage(String commentRatingPercentage) {
		this.commentRatingPercentage = commentRatingPercentage;
	}
	public String getItemQty() {
		return itemQty;
	}
	public void setItemQty(String itemQty) {
		this.itemQty = itemQty;
	}
	public String getItemBookedQty() {
		return itemBookedQty;
	}
	public void setItemBookedQty(String itemBookedQty) {
		this.itemBookedQty = itemBookedQty;
	}
	public String getItemPublishOn() {
		return itemPublishOn;
	}
	public void setItemPublishOn(String itemPublishOn) {
		this.itemPublishOn = itemPublishOn;
	}
	public String getItemExpireOn() {
		return itemExpireOn;
	}
	public void setItemExpireOn(String itemExpireOn) {
		this.itemExpireOn = itemExpireOn;
	}
	public String getItemUrl() {
		return itemUrl;
	}
	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}
	public String getItemCommentUrl() {
		return itemCommentUrl;
	}
	public void setItemCommentUrl(String itemCommentUrl) {
		this.itemCommentUrl = itemCommentUrl;
	}
	public String getItemDefaultImageUrl() {
		return itemDefaultImageUrl;
	}
	public void setItemDefaultImageUrl(String itemDefaultImageUrl) {
		this.itemDefaultImageUrl = itemDefaultImageUrl;
	}
	public String getItemUpdateName() {
		return itemUpdateName;
	}
	public void setItemUpdateName(String itemUpdateName) {
		this.itemUpdateName = itemUpdateName;
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
	public Vector<?> getItemImageUrls() {
		return itemImageUrls;
	}
	public void setItemImageUrls(Vector<?> itemImageUrls) {
		this.itemImageUrls = itemImageUrls;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public Vector<?> getComments() {
		return comments;
	}
	public void setComments(Vector<?> comments) {
		this.comments = comments;
	}
	public Vector<?> getItemTierPrices() {
		return itemTierPrices;
	}
	public void setItemTierPrices(Vector<?> itemTierPrices) {
		this.itemTierPrices = itemTierPrices;
	}
	public String getItemOrderedQty() {
		return itemOrderedQty;
	}
	public void setItemOrderedQty(String itemOrderedQty) {
		this.itemOrderedQty = itemOrderedQty;
	}
	public Vector<?> getRelatedItems() {
		return relatedItems;
	}
	public void setRelatedItems(Vector<?> relatedItems) {
		this.relatedItems = relatedItems;
	}
	public Vector<?> getUpSellItems() {
		return upSellItems;
	}
	public void setUpSellItems(Vector<?> upSellItems) {
		this.upSellItems = upSellItems;
	}
	public Vector<?> getBundleItems() {
		return bundleItems;
	}
	public void setBundleItems(Vector<?> bundleItems) {
		this.bundleItems = bundleItems;
	}
	public Vector<?> getItemAttribDetailInfos() {
		return itemAttribDetailInfos;
	}
	public void setItemAttribDetailInfos(Vector<?> itemAttribDetailInfos) {
		this.itemAttribDetailInfos = itemAttribDetailInfos;
	}
	public String getItemCommentUpdateUrl() {
		return itemCommentUpdateUrl;
	}
	public void setItemCommentUpdateUrl(String itemCommentUpdateUrl) {
		this.itemCommentUpdateUrl = itemCommentUpdateUrl;
	}
	public String getItemTypeCd() {
		return itemTypeCd;
	}
	public void setItemTypeCd(String itemTypeCd) {
		this.itemTypeCd = itemTypeCd;
	}
	public Vector<?> getSkuItems() {
		return skuItems;
	}
	public void setSkuItems(Vector<?> skuItems) {
		this.skuItems = skuItems;
	}
	public String getItemSkuCd() {
		return itemSkuCd;
	}
	public void setItemSkuCd(String itemSkuCd) {
		this.itemSkuCd = itemSkuCd;
	}
	public String getItemPriceFrom() {
		return itemPriceFrom;
	}
	public void setItemPriceFrom(String itemPriceFrom) {
		this.itemPriceFrom = itemPriceFrom;
	}
	public String getItemPriceTo() {
		return itemPriceTo;
	}
	public void setItemPriceTo(String itemPriceTo) {
		this.itemPriceTo = itemPriceTo;
	}
	public boolean isItemPriceRange() {
		return itemPriceRange;
	}
	public void setItemPriceRange(boolean itemPriceRange) {
		this.itemPriceRange = itemPriceRange;
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
	public String getCommentRatingPercentageNumber() {
		return commentRatingPercentageNumber;
	}
	public void setCommentRatingPercentageNumber(
			String commentRatingPercentageNumber) {
		this.commentRatingPercentageNumber = commentRatingPercentageNumber;
	}
	public Object getCustom() {
		return custom;
	}
	public void setCustom(Object custom) {
		this.custom = custom;
	}
}
