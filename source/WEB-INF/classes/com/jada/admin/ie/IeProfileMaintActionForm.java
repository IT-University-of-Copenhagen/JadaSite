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

package com.jada.admin.ie;

import java.util.Enumeration;

import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminMaintActionForm;

public class IeProfileMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = 6773802613317187911L;
	String mode;
	String ieProfileGroupName;
	String ieProfileHeaderId;
	String ieProfileHeaderName;
	String ieProfileType;
	String systemRecord;
	IeProfileDetailDisplayForm[] ieProfileGeneralDetails;
	IeProfileDetailDisplayForm[] ieProfileCategoryDetails;
	IeProfileDetailDisplayForm[] ieProfileItemRelatedDetails;
	IeProfileDetailDisplayForm[] ieProfileItemUpSellDetails;
	IeProfileDetailDisplayForm[] ieProfileItemCrossSellDetails;
	IeProfileDetailDisplayForm[] ieProfileItemTierPriceDetails;
	IeProfileDetailDisplayForm[] ieProfileItemAttributeDetails;
	IeProfileDetailDisplayForm[] ieProfileItemImageDetails;
	IeProfileDetailDisplayForm[] ieProfileOtherDetails;
	String ieProfileFieldName;
	public void reset(ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
		String IEPROFILEGENERALDETAIL = "ieProfileGeneralDetail.*ieProfileDetailId";
		String IEPROFILECATEGORYDETAIL = "ieProfileCategoryDetail.*ieProfileDetailId";
		String IEPROFILEITEMRELATEDDETAIL = "ieProfileItemRelatedDetail.*ieProfileDetailId";
		String IEPROFILEITEMCROSSSELLDETAIL = "ieProfileItemCrossSellDetail.*ieProfileDetailId";
		String IEPROFILEITEMUPSELLDETAIL = "ieProfileItemUpSellDetail.*ieProfileDetailId";
		String IEPROFILEITEMTIERPRICEDETAIL = "ieProfileItemTierPriceDetail.*ieProfileDetailId";
		String IEPROFILEITEMATTRIBUTEDETAIL = "ieProfileItemAttributeDetail.*ieProfileDetailId";
		String IEPROFILEITEMIMAGEDETAIL = "ieProfileItemImageDetail.*ieProfileDetailId";
		String IEPROFILEOTHERDETAIL = "ieProfileOtherDetail.*ieProfileDetailId";
		int countGeneral = 0;
		int countCategory = 0;
		int countItemUpSell = 0;
		int countItemCrossSell = 0;
		int countItemRelated = 0;
		int countItemTierPrice = 0;
		int countItemAttribute = 0;
		int countItemImage = 0;
		int countOther = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(IEPROFILEGENERALDETAIL)) {
				countGeneral++;
			}
			if (name.matches(IEPROFILECATEGORYDETAIL)) {
				countCategory++;
			}
			if (name.matches(IEPROFILEITEMUPSELLDETAIL)) {
				countItemUpSell++;
			}
			if (name.matches(IEPROFILEITEMCROSSSELLDETAIL)) {
				countItemCrossSell++;
			}
			if (name.matches(IEPROFILEITEMRELATEDDETAIL)) {
				countItemRelated++;
			}
			if (name.matches(IEPROFILEITEMTIERPRICEDETAIL)) {
				countItemTierPrice++;
			}
			if (name.matches(IEPROFILEITEMATTRIBUTEDETAIL)) {
				countItemAttribute++;
			}
			if (name.matches(IEPROFILEITEMIMAGEDETAIL)) {
				countItemImage++;
			}
			if (name.matches(IEPROFILEOTHERDETAIL)) {
				countOther++;
			}
		}
		
		ieProfileGeneralDetails = new IeProfileDetailDisplayForm[countGeneral];
		for (int i = 0; i < ieProfileGeneralDetails.length; i++) {
			ieProfileGeneralDetails[i] = new IeProfileDetailDisplayForm();
		}
		
		ieProfileCategoryDetails = new IeProfileDetailDisplayForm[countCategory];
		for (int i = 0; i < ieProfileCategoryDetails.length; i++) {
			ieProfileCategoryDetails[i] = new IeProfileDetailDisplayForm();
		}
		
		ieProfileItemRelatedDetails = new IeProfileDetailDisplayForm[countItemRelated];
		for (int i = 0; i < ieProfileItemRelatedDetails.length; i++) {
			ieProfileItemRelatedDetails[i] = new IeProfileDetailDisplayForm();
		}
		
		ieProfileItemUpSellDetails = new IeProfileDetailDisplayForm[countItemUpSell];
		for (int i = 0; i < ieProfileItemUpSellDetails.length; i++) {
			ieProfileItemUpSellDetails[i] = new IeProfileDetailDisplayForm();
		}
		
		ieProfileItemCrossSellDetails = new IeProfileDetailDisplayForm[countItemCrossSell];
		for (int i = 0; i < ieProfileItemCrossSellDetails.length; i++) {
			ieProfileItemCrossSellDetails[i] = new IeProfileDetailDisplayForm();
		}
		
		ieProfileItemTierPriceDetails = new IeProfileDetailDisplayForm[countItemTierPrice];
		for (int i = 0; i < ieProfileItemTierPriceDetails.length; i++) {
			ieProfileItemTierPriceDetails[i] = new IeProfileDetailDisplayForm();
		}
		
		ieProfileItemAttributeDetails = new IeProfileDetailDisplayForm[countItemAttribute];
		for (int i = 0; i < ieProfileItemAttributeDetails.length; i++) {
			ieProfileItemAttributeDetails[i] = new IeProfileDetailDisplayForm();
		}
		
		ieProfileItemImageDetails = new IeProfileDetailDisplayForm[countItemImage];
		for (int i = 0; i < ieProfileItemImageDetails.length; i++) {
			ieProfileItemImageDetails[i] = new IeProfileDetailDisplayForm();
		}
		
		ieProfileOtherDetails = new IeProfileDetailDisplayForm[countOther];
		for (int i = 0; i < ieProfileOtherDetails.length; i++) {
			ieProfileOtherDetails[i] = new IeProfileDetailDisplayForm();
		}
	}
	public IeProfileDetailDisplayForm getIeProfileGeneralDetail(int index) {
		return ieProfileGeneralDetails[index];
	}
	public IeProfileDetailDisplayForm getIeProfileCategoryDetail(int index) {
		return ieProfileCategoryDetails[index];
	}
	public IeProfileDetailDisplayForm getIeProfileItemRelatedDetail(int index) {
		return ieProfileItemRelatedDetails[index];
	}
	public IeProfileDetailDisplayForm getIeProfileItemCrossSellDetail(int index) {
		return ieProfileItemCrossSellDetails[index];
	}
	public IeProfileDetailDisplayForm getIeProfileItemUpSellDetail(int index) {
		return ieProfileItemUpSellDetails[index];
	}
	public IeProfileDetailDisplayForm getIeProfileItemTierPriceDetail(int index) {
		return ieProfileItemTierPriceDetails[index];
	}
	public IeProfileDetailDisplayForm getIeProfileItemAttributeDetail(int index) {
		return ieProfileItemAttributeDetails[index];
	}
	public IeProfileDetailDisplayForm getIeProfileItemImageDetail(int index) {
		return ieProfileItemImageDetails[index];
	}
	public IeProfileDetailDisplayForm getIeProfileOtherDetail(int index) {
		return ieProfileOtherDetails[index];
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getIeProfileHeaderId() {
		return ieProfileHeaderId;
	}
	public void setIeProfileHeaderId(String ieProfileHeaderId) {
		this.ieProfileHeaderId = ieProfileHeaderId;
	}
	public String getIeProfileHeaderName() {
		return ieProfileHeaderName;
	}
	public void setIeProfileHeaderName(String ieProfileHeaderName) {
		this.ieProfileHeaderName = ieProfileHeaderName;
	}
	public String getSystemRecord() {
		return systemRecord;
	}
	public void setSystemRecord(String systemRecord) {
		this.systemRecord = systemRecord;
	}
	public String getIeProfileFieldName() {
		return ieProfileFieldName;
	}
	public void setIeProfileFieldName(String ieProfileFieldName) {
		this.ieProfileFieldName = ieProfileFieldName;
	}
	public IeProfileDetailDisplayForm[] getIeProfileGeneralDetails() {
		return ieProfileGeneralDetails;
	}
	public void setIeProfileGeneralDetails(
			IeProfileDetailDisplayForm[] ieProfileGeneralDetails) {
		this.ieProfileGeneralDetails = ieProfileGeneralDetails;
	}
	public IeProfileDetailDisplayForm[] getIeProfileCategoryDetails() {
		return ieProfileCategoryDetails;
	}
	public void setIeProfileCategoryDetails(
			IeProfileDetailDisplayForm[] ieProfileCategoryDetails) {
		this.ieProfileCategoryDetails = ieProfileCategoryDetails;
	}
	public String getIeProfileGroupName() {
		return ieProfileGroupName;
	}
	public void setIeProfileGroupName(String ieProfileGroupName) {
		this.ieProfileGroupName = ieProfileGroupName;
	}
	public IeProfileDetailDisplayForm[] getIeProfileItemRelatedDetails() {
		return ieProfileItemRelatedDetails;
	}
	public void setIeProfileItemRelatedDetails(
			IeProfileDetailDisplayForm[] ieProfileItemRelatedDetails) {
		this.ieProfileItemRelatedDetails = ieProfileItemRelatedDetails;
	}
	public IeProfileDetailDisplayForm[] getIeProfileItemCrossSellDetails() {
		return ieProfileItemCrossSellDetails;
	}
	public void setIeProfileItemCrossSellDetails(
			IeProfileDetailDisplayForm[] ieProfileItemCrossSellDetails) {
		this.ieProfileItemCrossSellDetails = ieProfileItemCrossSellDetails;
	}
	public IeProfileDetailDisplayForm[] getIeProfileItemUpSellDetails() {
		return ieProfileItemUpSellDetails;
	}
	public void setIeProfileItemUpSellDetails(
			IeProfileDetailDisplayForm[] ieProfileItemUpSellDetails) {
		this.ieProfileItemUpSellDetails = ieProfileItemUpSellDetails;
	}
	public IeProfileDetailDisplayForm[] getIeProfileItemTierPriceDetails() {
		return ieProfileItemTierPriceDetails;
	}
	public void setIeProfileItemTierPriceDetails(
			IeProfileDetailDisplayForm[] ieProfileItemTierPriceDetails) {
		this.ieProfileItemTierPriceDetails = ieProfileItemTierPriceDetails;
	}
	public IeProfileDetailDisplayForm[] getIeProfileItemAttributeDetails() {
		return ieProfileItemAttributeDetails;
	}
	public void setIeProfileItemAttributeDetails(
			IeProfileDetailDisplayForm[] ieProfileItemAttributeDetails) {
		this.ieProfileItemAttributeDetails = ieProfileItemAttributeDetails;
	}
	public String getIeProfileType() {
		return ieProfileType;
	}
	public void setIeProfileType(String ieProfileType) {
		this.ieProfileType = ieProfileType;
	}
	public IeProfileDetailDisplayForm[] getIeProfileOtherDetails() {
		return ieProfileOtherDetails;
	}
	public void setIeProfileOtherDetails(
			IeProfileDetailDisplayForm[] ieProfileOtherDetails) {
		this.ieProfileOtherDetails = ieProfileOtherDetails;
	}
	public IeProfileDetailDisplayForm[] getIeProfileItemImageDetails() {
		return ieProfileItemImageDetails;
	}
	public void setIeProfileItemImageDetails(
			IeProfileDetailDisplayForm[] ieProfileItemImageDetails) {
		this.ieProfileItemImageDetails = ieProfileItemImageDetails;
	}
}
