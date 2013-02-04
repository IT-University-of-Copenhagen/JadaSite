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

package com.jada.admin.shipping;

import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminMaintActionForm;

public class ShippingMethodMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -4665370140369251628L;
	String mode;
	String shippingMethodId;
	String shippingRegionId;
	String shippingTypeId;
	boolean shippingMethodNameLangFlag;
	String shippingMethodNameLang;
	String shippingMethodName;
	String seqNum;
	boolean published;
	String jsonShippingTypes;
	String shippingTypeIds[];
	String shippingRegionIds[];
	String shippingRateFee;
	String shippingRateFeeCurr;
	boolean shippingRateFeeCurrFlag;
	String shippingRatePercentage;
	String shippingAdditionalRateFee;
	String shippingAdditionalRateFeeCurr;
	boolean shippingAdditionalRateFeeCurrFlag;
	String shippingAdditionalRatePercentage;
	
	public void reset(ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
		shippingRegionIds = new String[0];
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getShippingMethodId() {
		return shippingMethodId;
	}
	public void setShippingMethodId(String shippingMethodId) {
		this.shippingMethodId = shippingMethodId;
	}
	public boolean isShippingMethodNameLangFlag() {
		return shippingMethodNameLangFlag;
	}
	public void setShippingMethodNameLangFlag(boolean shippingMethodNameLangFlag) {
		this.shippingMethodNameLangFlag = shippingMethodNameLangFlag;
	}
	public String getShippingMethodNameLang() {
		return shippingMethodNameLang;
	}
	public void setShippingMethodNameLang(String shippingMethodNameLang) {
		this.shippingMethodNameLang = shippingMethodNameLang;
	}
	public String getShippingMethodName() {
		return shippingMethodName;
	}
	public void setShippingMethodName(String shippingMethodName) {
		this.shippingMethodName = shippingMethodName;
	}
	public String getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}
	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
	}
	public String[] getShippingTypeIds() {
		return shippingTypeIds;
	}
	public void setShippingTypeIds(String[] shippingTypeIds) {
		this.shippingTypeIds = shippingTypeIds;
	}
	public String getJsonShippingTypes() {
		return jsonShippingTypes;
	}
	public void setJsonShippingTypes(String jsonShippingTypes) {
		this.jsonShippingTypes = jsonShippingTypes;
	}
	public String[] getShippingRegionIds() {
		return shippingRegionIds;
	}
	public void setShippingRegionIds(String[] shippingRegionIds) {
		this.shippingRegionIds = shippingRegionIds;
	}
	public String getShippingRegionId() {
		return shippingRegionId;
	}
	public void setShippingRegionId(String shippingRegionId) {
		this.shippingRegionId = shippingRegionId;
	}
	public String getShippingTypeId() {
		return shippingTypeId;
	}
	public void setShippingTypeId(String shippingTypeId) {
		this.shippingTypeId = shippingTypeId;
	}
	public String getShippingRateFee() {
		return shippingRateFee;
	}
	public void setShippingRateFee(String shippingRateFee) {
		this.shippingRateFee = shippingRateFee;
	}
	public String getShippingRateFeeCurr() {
		return shippingRateFeeCurr;
	}
	public void setShippingRateFeeCurr(String shippingRateFeeCurr) {
		this.shippingRateFeeCurr = shippingRateFeeCurr;
	}
	public boolean isShippingRateFeeCurrFlag() {
		return shippingRateFeeCurrFlag;
	}
	public void setShippingRateFeeCurrFlag(boolean shippingRateFeeCurrFlag) {
		this.shippingRateFeeCurrFlag = shippingRateFeeCurrFlag;
	}
	public String getShippingRatePercentage() {
		return shippingRatePercentage;
	}
	public void setShippingRatePercentage(String shippingRatePercentage) {
		this.shippingRatePercentage = shippingRatePercentage;
	}
	public String getShippingAdditionalRateFee() {
		return shippingAdditionalRateFee;
	}
	public void setShippingAdditionalRateFee(String shippingAdditionalRateFee) {
		this.shippingAdditionalRateFee = shippingAdditionalRateFee;
	}
	public String getShippingAdditionalRateFeeCurr() {
		return shippingAdditionalRateFeeCurr;
	}
	public void setShippingAdditionalRateFeeCurr(
			String shippingAdditionalRateFeeCurr) {
		this.shippingAdditionalRateFeeCurr = shippingAdditionalRateFeeCurr;
	}
	public boolean isShippingAdditionalRateFeeCurrFlag() {
		return shippingAdditionalRateFeeCurrFlag;
	}
	public void setShippingAdditionalRateFeeCurrFlag(
			boolean shippingAdditionalRateFeeCurrFlag) {
		this.shippingAdditionalRateFeeCurrFlag = shippingAdditionalRateFeeCurrFlag;
	}
	public String getShippingAdditionalRatePercentage() {
		return shippingAdditionalRatePercentage;
	}
	public void setShippingAdditionalRatePercentage(
			String shippingAdditionalRatePercentage) {
		this.shippingAdditionalRatePercentage = shippingAdditionalRatePercentage;
	}
}
