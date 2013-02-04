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

package com.jada.admin.coupon;

import com.jada.admin.AdminMaintActionForm;

public class CouponMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -702239451479916264L;
	String mode;
	String couponId;
	String couponCode;
	String couponName;
	boolean couponNameLangFlag;
	String couponNameLang;
	String couponStartDate;
	String couponEndDate;
	boolean couponAutoApply;
	boolean couponApplyAll;
	String couponType;
	String couponDiscountPercent;
	String couponDiscountAmount;
	boolean couponDiscountAmountCurrFlag;
	String couponDiscountAmountCurr;
	String couponOrderAmount;
	boolean couponOrderAmountCurrFlag;
	String couponOrderAmountCurr;
	String couponTotalUsed;
	String couponMaxUse;
	String couponMaxCustUse;
	String couponPriority;
	String couponScope;
	boolean published;
	String recUpdateBy;
	String recUpdateDatetime;
	String recCreateBy;
	String recCreateDatetime;
	String itemId;
	String itemIds[];
	String catId;
	String catIds[];
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public String getCouponStartDate() {
		return couponStartDate;
	}
	public void setCouponStartDate(String couponStartDate) {
		this.couponStartDate = couponStartDate;
	}
	public String getCouponEndDate() {
		return couponEndDate;
	}
	public void setCouponEndDate(String couponEndDate) {
		this.couponEndDate = couponEndDate;
	}
	public boolean isCouponAutoApply() {
		return couponAutoApply;
	}
	public void setCouponAutoApply(boolean couponAutoApply) {
		this.couponAutoApply = couponAutoApply;
	}
	public boolean isCouponApplyAll() {
		return couponApplyAll;
	}
	public void setCouponApplyAll(boolean couponApplyAll) {
		this.couponApplyAll = couponApplyAll;
	}
	public String getCouponDiscountPercent() {
		return couponDiscountPercent;
	}
	public void setCouponDiscountPercent(String couponDiscountPercent) {
		this.couponDiscountPercent = couponDiscountPercent;
	}
	public String getCouponDiscountAmount() {
		return couponDiscountAmount;
	}
	public void setCouponDiscountAmount(String couponDiscountAmount) {
		this.couponDiscountAmount = couponDiscountAmount;
	}
	public String getCouponOrderAmount() {
		return couponOrderAmount;
	}
	public void setCouponOrderAmount(String couponOrderAmount) {
		this.couponOrderAmount = couponOrderAmount;
	}
	public String getCouponTotalUsed() {
		return couponTotalUsed;
	}
	public void setCouponTotalUsed(String couponTotalUsed) {
		this.couponTotalUsed = couponTotalUsed;
	}
	public String getCouponMaxUse() {
		return couponMaxUse;
	}
	public void setCouponMaxUse(String couponMaxUse) {
		this.couponMaxUse = couponMaxUse;
	}
	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
	}
	public String getCouponPriority() {
		return couponPriority;
	}
	public void setCouponPriority(String couponPriority) {
		this.couponPriority = couponPriority;
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
	public String getCouponType() {
		return couponType;
	}
	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}
	public String getCouponMaxCustUse() {
		return couponMaxCustUse;
	}
	public void setCouponMaxCustUse(String couponMaxCustUse) {
		this.couponMaxCustUse = couponMaxCustUse;
	}
	public String getCouponScope() {
		return couponScope;
	}
	public void setCouponScope(String couponScope) {
		this.couponScope = couponScope;
	}
	public boolean isCouponNameLangFlag() {
		return couponNameLangFlag;
	}
	public void setCouponNameLangFlag(boolean couponNameLangFlag) {
		this.couponNameLangFlag = couponNameLangFlag;
	}
	public String getCouponNameLang() {
		return couponNameLang;
	}
	public void setCouponNameLang(String couponNameLang) {
		this.couponNameLang = couponNameLang;
	}
	public boolean isCouponDiscountAmountCurrFlag() {
		return couponDiscountAmountCurrFlag;
	}
	public void setCouponDiscountAmountCurrFlag(boolean couponDiscountAmountCurrFlag) {
		this.couponDiscountAmountCurrFlag = couponDiscountAmountCurrFlag;
	}
	public String getCouponDiscountAmountCurr() {
		return couponDiscountAmountCurr;
	}
	public void setCouponDiscountAmountCurr(String couponDiscountAmountCurr) {
		this.couponDiscountAmountCurr = couponDiscountAmountCurr;
	}
	public boolean isCouponOrderAmountCurrFlag() {
		return couponOrderAmountCurrFlag;
	}
	public void setCouponOrderAmountCurrFlag(boolean couponOrderAmountCurrFlag) {
		this.couponOrderAmountCurrFlag = couponOrderAmountCurrFlag;
	}
	public String getCouponOrderAmountCurr() {
		return couponOrderAmountCurr;
	}
	public void setCouponOrderAmountCurr(String couponOrderAmountCurr) {
		this.couponOrderAmountCurr = couponOrderAmountCurr;
	}
	public String[] getCatIds() {
		return catIds;
	}
	public void setCatIds(String[] catIds) {
		this.catIds = catIds;
	}
	public String[] getItemIds() {
		return itemIds;
	}
	public void setItemIds(String[] itemIds) {
		this.itemIds = itemIds;
	}
}
