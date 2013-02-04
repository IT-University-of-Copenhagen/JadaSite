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

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.EntityManager;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.IeProfileHeaderDAO;
import com.jada.ie.ItemSimpleCsvMapping;
import com.jada.ie.ItemSimpleCsvTransformation;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.IeProfileDetail;
import com.jada.jpa.entity.IeProfileHeader;
import com.jada.jpa.entity.Site;
import com.jada.util.Constants;
import com.jada.util.Format;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

public class IeProfileMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest request,
                             HttpServletResponse response)
        throws Throwable {

        IeProfileMaintActionForm form = (IeProfileMaintActionForm) actionForm;
        form.setSystemRecord(String.valueOf(Constants.VALUE_NO));
        form.setMode("C");
        FormUtils.setFormDisplayMode(request, form, FormUtils.CREATE_MODE);
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

        Site site = getAdminBean(request).getSite();
    	IeProfileMaintActionForm form = (IeProfileMaintActionForm) actionForm;
        Vector<IeProfileDetailDisplayForm> generalVector = new Vector<IeProfileDetailDisplayForm>();
        Vector<IeProfileDetailDisplayForm> categoryVector = new Vector<IeProfileDetailDisplayForm>();
        
        Vector<IeProfileDetailDisplayForm> itemRelatedVector = new Vector<IeProfileDetailDisplayForm>();
        Vector<IeProfileDetailDisplayForm> itemCrossSellVector = new Vector<IeProfileDetailDisplayForm>();
        Vector<IeProfileDetailDisplayForm> itemUpSellVector = new Vector<IeProfileDetailDisplayForm>();

        Vector<IeProfileDetailDisplayForm> itemTierPriceVector = new Vector<IeProfileDetailDisplayForm>();
        Vector<IeProfileDetailDisplayForm> itemAttributeVector = new Vector<IeProfileDetailDisplayForm>();
        
        Vector<IeProfileDetailDisplayForm> itemImageVector = new Vector<IeProfileDetailDisplayForm>();
        
        Vector<IeProfileDetailDisplayForm> otherVector = new Vector<IeProfileDetailDisplayForm>();

        IeProfileHeader header = IeProfileHeaderDAO.load(site.getSiteId(), Format.getLong(form.getIeProfileHeaderId()));
        form.setIeProfileHeaderName(header.getIeProfileHeaderName());
        form.setIeProfileType(String.valueOf(header.getIeProfileType()));
        form.setSystemRecord(String.valueOf(header.getSystemRecord()));
        
        for (IeProfileDetail detail : header.getIeProfileDetails()) {
        	IeProfileDetailDisplayForm detailDisplayForm = new IeProfileDetailDisplayForm();
        	detailDisplayForm.setIeProfileGroupName(detail.getIeProfileGroupName());
        	if (detail.getIeProfileGroupIndex() != null) {
        		detailDisplayForm.setIeProfileGroupIndex(detail.getIeProfileGroupIndex().toString());
        	}
        	detailDisplayForm.setIeProfileFieldName(detail.getIeProfileFieldName());
        	if (detail.getIeProfilePosition() != null) {
        		detailDisplayForm.setIeProfilePosition(detail.getIeProfilePosition().toString());
        	}
        	detailDisplayForm.setIeProfileFieldValue(detail.getIeProfileFieldValue());
        	if (detail.getIeProfileGroupIndex() == null) {
        		generalVector.add(detailDisplayForm);
        	}
        	else {
        		if (detail.getIeProfileGroupName().equals("categories")) {
        			categoryVector.add(detailDisplayForm);
        		}
        		if (detail.getIeProfileGroupName().equals("itemsRelated")) {
        			itemRelatedVector.add(detailDisplayForm);
        		}
        		if (detail.getIeProfileGroupName().equals("itemsCrossSell")) {
        			itemCrossSellVector.add(detailDisplayForm);
        		}
        		if (detail.getIeProfileGroupName().equals("itemsUpSell")) {
        			itemUpSellVector.add(detailDisplayForm);
        		}
        		if (detail.getIeProfileGroupName().equals("itemTierPrices")) {
        			itemTierPriceVector.add(detailDisplayForm);
        		}
        		if (detail.getIeProfileGroupName().equals("itemAttributeDetails")) {
        			itemAttributeVector.add(detailDisplayForm);
        		}
        		if (detail.getIeProfileGroupName().equals("itemImages")) {
        			itemImageVector.add(detailDisplayForm);
        		}
        		if (detail.getIeProfileGroupName().equals("others")) {
        			otherVector.add(detailDisplayForm);
        		}
        	}
        }
        
        for (ItemSimpleCsvMapping mapping : ItemSimpleCsvTransformation.FIELDS_GENERAL) {
        	boolean found = false;
        	for (int i = 0; i < generalVector.size(); i++) {
        		IeProfileDetailDisplayForm displayForm = generalVector.elementAt(i);
        		if (displayForm.getIeProfileFieldName().equals(mapping.getIeProfileFieldName())) {
        			found = true;
        			break;
        		}
        	}
        	if (!found) {
        		IeProfileDetailDisplayForm displayForm = new IeProfileDetailDisplayForm();
        		displayForm.setIeProfileGroupName(mapping.getIeProfileGroupName());
        		displayForm.setIeProfileGroupIndex(null);
        		displayForm.setIeProfileFieldName(mapping.getIeProfileFieldName());
        		generalVector.add(displayForm);
        	}
        }
        
        IeProfileDetailDisplayForm[] generalDisplayForms = new IeProfileDetailDisplayForm[generalVector.size()];
        generalVector.copyInto(generalDisplayForms);
        form.setIeProfileGeneralDetails(generalDisplayForms);

        IeProfileDetailDisplayForm[] categoryForms = new IeProfileDetailDisplayForm[categoryVector.size()];
        categoryVector.copyInto(categoryForms);
        form.setIeProfileCategoryDetails(categoryForms);

        IeProfileDetailDisplayForm[] itemRelatedForms = new IeProfileDetailDisplayForm[itemRelatedVector.size()];
        itemRelatedVector.copyInto(itemRelatedForms);
        form.setIeProfileItemRelatedDetails(itemRelatedForms);

        IeProfileDetailDisplayForm[] itemCrossSellForms = new IeProfileDetailDisplayForm[itemCrossSellVector.size()];
        itemCrossSellVector.copyInto(itemCrossSellForms);
        form.setIeProfileItemCrossSellDetails(itemCrossSellForms);

        IeProfileDetailDisplayForm[] itemUpSellForms = new IeProfileDetailDisplayForm[itemUpSellVector.size()];
        itemUpSellVector.copyInto(itemUpSellForms);
        form.setIeProfileItemUpSellDetails(itemUpSellForms);

        IeProfileDetailDisplayForm[] itemTierPriceForms = new IeProfileDetailDisplayForm[itemTierPriceVector.size()];
        itemTierPriceVector.copyInto(itemTierPriceForms);
        form.setIeProfileItemTierPriceDetails(itemTierPriceForms);

        IeProfileDetailDisplayForm[] itemAttributeForms = new IeProfileDetailDisplayForm[itemAttributeVector.size()];
        itemAttributeVector.copyInto(itemAttributeForms);
        form.setIeProfileItemAttributeDetails(itemAttributeForms);

        IeProfileDetailDisplayForm[] itemImageForms = new IeProfileDetailDisplayForm[itemImageVector.size()];
        itemImageVector.copyInto(itemImageForms);
        form.setIeProfileItemImageDetails(itemImageForms);

        IeProfileDetailDisplayForm[] otherForms = new IeProfileDetailDisplayForm[otherVector.size()];
        otherVector.copyInto(otherForms);
        form.setIeProfileOtherDetails(otherForms);

        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        Site site = getAdminBean(request).getSite();
		IeProfileMaintActionForm form = (IeProfileMaintActionForm) actionForm;
		IeProfileHeader header = IeProfileHeaderDAO.load(site.getSiteId(), Format.getLong(form.getIeProfileHeaderId()));
		for (IeProfileDetail detail : header.getIeProfileDetails()) {
			em.remove(detail);
		}
		em.remove(header);
		ActionForward actionForward = actionMapping.findForward("removeSuccess");
		return actionForward;
	}
    
	public ActionForward addGroup(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		IeProfileMaintActionForm form = (IeProfileMaintActionForm) actionForm;
		
		IeProfileDetailDisplayForm profileDetails[] = null;
		ItemSimpleCsvMapping csvMappings[] = null;
		if (form.getIeProfileGroupName().equals("categories")) {
			profileDetails = form.getIeProfileCategoryDetails();
			csvMappings = ItemSimpleCsvTransformation.FIELDS_CATEGORY;
		}
		else if (form.getIeProfileGroupName().equals("itemsRelated")) {
			profileDetails = form.getIeProfileItemRelatedDetails();
			csvMappings = ItemSimpleCsvTransformation.FIELDS_ITEM_RELATED;
		}
		else if (form.getIeProfileGroupName().equals("itemsUpSell")) {
			profileDetails = form.getIeProfileItemUpSellDetails();
			csvMappings = ItemSimpleCsvTransformation.FIELDS_ITEM_UPSELL;
		}
		else if (form.getIeProfileGroupName().equals("itemsCrossSell")) {
			profileDetails = form.getIeProfileItemCrossSellDetails();
			csvMappings = ItemSimpleCsvTransformation.FIELDS_ITEM_CROSSSELL;
		}
		else if (form.getIeProfileGroupName().equals("itemTierPrices")) {
			profileDetails = form.getIeProfileItemTierPriceDetails();
			csvMappings = ItemSimpleCsvTransformation.FIELDS_ITEM_TIERPRICE;
		}
		else if (form.getIeProfileGroupName().equals("itemAttributeDetails")) {
			profileDetails = form.getIeProfileItemAttributeDetails();
			csvMappings = ItemSimpleCsvTransformation.FIELDS_ITEM_ATTRIBUTEDETAIL;
		}
		else if (form.getIeProfileGroupName().equals("itemImages")) {
			profileDetails = form.getIeProfileItemImageDetails();
			csvMappings = ItemSimpleCsvTransformation.FIELDS_ITEM_IMAGE;
		}
		else if (form.getIeProfileGroupName().equals("others")) {
			profileDetails = form.getIeProfileOtherDetails();
			csvMappings = ItemSimpleCsvTransformation.FIELDS_OTHERS;
		}
	
		Vector<IeProfileDetailDisplayForm> vector = new Vector<IeProfileDetailDisplayForm>();
		int current = 0;
		int groupIndex = -1;
		for (IeProfileDetailDisplayForm i : profileDetails) {
			groupIndex = Format.getInt(i.getIeProfileGroupIndex());
			if (groupIndex > current) {
				current = groupIndex;
			}
			vector.add(i);
		}
		for (ItemSimpleCsvMapping csvMapping : csvMappings) {
			IeProfileDetailDisplayForm ieProfileDetailDisplayForm = new IeProfileDetailDisplayForm();
			ieProfileDetailDisplayForm.setIeProfileGroupName(csvMapping.getIeProfileGroupName());
			ieProfileDetailDisplayForm.setIeProfileGroupIndex(String.valueOf(groupIndex + 1));
			ieProfileDetailDisplayForm.setIeProfileFieldName(csvMapping.getIeProfileFieldName());
			vector.add(ieProfileDetailDisplayForm);
		}
		profileDetails = new IeProfileDetailDisplayForm[vector.size()];
		vector.copyInto(profileDetails);
		
		if (form.getIeProfileGroupName().equals("categories")) {
			form.setIeProfileCategoryDetails(profileDetails);
		}
		else if (form.getIeProfileGroupName().equals("itemsRelated")) {
			form.setIeProfileItemRelatedDetails(profileDetails);
		}
		else if (form.getIeProfileGroupName().equals("itemsUpSell")) {
			form.setIeProfileItemUpSellDetails(profileDetails);
		}
		else if (form.getIeProfileGroupName().equals("itemsCrossSell")) {
			form.setIeProfileItemCrossSellDetails(profileDetails);
		}
		else if (form.getIeProfileGroupName().equals("itemTierPrices")) {
			form.setIeProfileItemTierPriceDetails(profileDetails);
		}
		else if (form.getIeProfileGroupName().equals("itemAttributeDetails")) {
			form.setIeProfileItemAttributeDetails(profileDetails);
		}
		else if (form.getIeProfileGroupName().equals("itemImages")) {
			form.setIeProfileItemImageDetails(profileDetails);
		}
		else if (form.getIeProfileGroupName().equals("others")) {
			form.setIeProfileOtherDetails(profileDetails);
		}
		
		return mapping.findForward("success");
	}
	
	public ActionForward removeGroup(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		IeProfileMaintActionForm form = (IeProfileMaintActionForm) actionForm;
		
		Vector<IeProfileDetailDisplayForm> vector = new Vector<IeProfileDetailDisplayForm>();
		int removeIndex = -1;
		
		IeProfileDetailDisplayForm profileDetails[] = null;
		if (form.getIeProfileGroupName().equals("categories")) {
			profileDetails = form.getIeProfileCategoryDetails();
		}
		else if (form.getIeProfileGroupName().equals("itemsRelated")) {
			profileDetails = form.getIeProfileItemRelatedDetails();
		}
		else if (form.getIeProfileGroupName().equals("itemsUpSell")) {
			profileDetails = form.getIeProfileItemUpSellDetails();
		}
		else if (form.getIeProfileGroupName().equals("itemsCrossSell")) {
			profileDetails = form.getIeProfileItemCrossSellDetails();
		}
		else if (form.getIeProfileGroupName().equals("itemTierPrices")) {
			profileDetails = form.getIeProfileItemTierPriceDetails();
		}
		else if (form.getIeProfileGroupName().equals("itemAttributeDetails")) {
			profileDetails = form.getIeProfileItemAttributeDetails();
		}
		else if (form.getIeProfileGroupName().equals("itemImages")) {
			profileDetails = form.getIeProfileItemImageDetails();
		}
		else if (form.getIeProfileGroupName().equals("otherDetails")) {
			profileDetails = form.getIeProfileOtherDetails();
		}
	
		for (IeProfileDetailDisplayForm i : profileDetails) {
			if (i.isSelected()) {
				removeIndex = Format.getInt(i.getIeProfileGroupIndex());
			}
			int groupIndex = Format.getInt(i.getIeProfileGroupIndex());
			if (groupIndex == removeIndex) {
				continue;
			}
			vector.add(i);
		}
		profileDetails = new IeProfileDetailDisplayForm[vector.size()];
		vector.copyInto(profileDetails);
		
		if (form.getIeProfileGroupName().equals("categories")) {
			form.setIeProfileCategoryDetails(profileDetails);
		}
		else if (form.getIeProfileGroupName().equals("itemsRelated")) {
			form.setIeProfileItemRelatedDetails(profileDetails);
		}
		else if (form.getIeProfileGroupName().equals("itemsUpSell")) {
			form.setIeProfileItemUpSellDetails(profileDetails);
		}
		else if (form.getIeProfileGroupName().equals("itemsCrossSell")) {
			form.setIeProfileItemCrossSellDetails(profileDetails);
		}
		else if (form.getIeProfileGroupName().equals("itemTierPrices")) {
			form.setIeProfileItemTierPriceDetails(profileDetails);
		}
		else if (form.getIeProfileGroupName().equals("itemAttributeDetails")) {
			form.setIeProfileItemAttributeDetails(profileDetails);
		}
		else if (form.getIeProfileGroupName().equals("itemImages")) {
			form.setIeProfileItemImageDetails(profileDetails);
		}
		else if (form.getIeProfileGroupName().equals("otherDetails")) {
			form.setIeProfileOtherDetails(profileDetails);
		}
		
		return mapping.findForward("success");
	}

	public ActionForward save(ActionMapping mapping, 
							  ActionForm actionForm,
							  HttpServletRequest request,
							  HttpServletResponse response) 
		throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		boolean insertMode = false;
		IeProfileMaintActionForm form = (IeProfileMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}

		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		String siteId = site.getSiteId();

		IeProfileHeader ieProfileHeader = new IeProfileHeader();
		if (insertMode) {
	        Vector<IeProfileDetailDisplayForm> vector = new Vector<IeProfileDetailDisplayForm>();
	        for (ItemSimpleCsvMapping csvMapping : ItemSimpleCsvTransformation.FIELDS_GENERAL) {
	        	IeProfileDetailDisplayForm ieProfileDetailDisplayForm = new IeProfileDetailDisplayForm();
	        	ieProfileDetailDisplayForm.setIeProfileFieldName(csvMapping.getIeProfileFieldName());
	        	vector.add(ieProfileDetailDisplayForm);
	        }
	        IeProfileDetailDisplayForm[] generalDisplayForms = new IeProfileDetailDisplayForm[vector.size()];
	        vector.copyInto(generalDisplayForms);
	        form.setIeProfileGeneralDetails(generalDisplayForms);
		}
		else {
			ieProfileHeader = IeProfileHeaderDAO.load(siteId, Format.getLong(form.getIeProfileHeaderId()));
		}

		ActionMessages errors = validate(form, request);
		if (errors.size() != 0) {
			saveMessages(request, errors);
	        if (insertMode) {
	            FormUtils.setFormDisplayMode(request, form, FormUtils.CREATE_MODE);
	        }
	        else {
	            FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
	        }
			return mapping.findForward("error");
		}

		if (insertMode) {
			ieProfileHeader.setSystemRecord(Constants.VALUE_NO);
			ieProfileHeader.setRecCreateBy(adminBean.getUser().getUserId());
			ieProfileHeader.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		ieProfileHeader.setSite(site);
		ieProfileHeader.setIeProfileType(form.getIeProfileType().charAt(0));
		ieProfileHeader.setIeProfileHeaderName(form.getIeProfileHeaderName());
		ieProfileHeader.setRecUpdateBy(adminBean.getUser().getUserId());
		ieProfileHeader.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		if (insertMode) {
			em.persist(ieProfileHeader);
		}
		
		for (IeProfileDetail ieProfileDetail : ieProfileHeader.getIeProfileDetails()) {	
			em.remove(ieProfileDetail);
		}
		
		int seqNum = 0;
		for (IeProfileDetailDisplayForm detailForm : form.getIeProfileGeneralDetails()) {
			IeProfileDetail ieProfileDetail = new IeProfileDetail();
			ieProfileDetail.setIeProfileGroupName(detailForm.getIeProfileGroupName());
			if (!Format.isNullOrEmpty(detailForm.getIeProfileGroupIndex())) {
				ieProfileDetail.setIeProfileGroupIndex(Integer.valueOf(detailForm.getIeProfileGroupIndex()));
			}
			ieProfileDetail.setIeProfileFieldName(detailForm.getIeProfileFieldName());
			if (!Format.isNullOrEmpty(detailForm.getIeProfileFieldValue())) {
				ieProfileDetail.setIeProfileFieldValue(detailForm.getIeProfileFieldValue());
			}
			ieProfileDetail.setSeqNum(Integer.valueOf(seqNum++));
			if (!Format.isNullOrEmpty(detailForm.getIeProfilePosition())) {
				ieProfileDetail.setIeProfilePosition(Integer.valueOf(detailForm.getIeProfilePosition()));
			}
			ieProfileDetail.setRecUpdateBy(adminBean.getUser().getUserId());
			ieProfileDetail.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			ieProfileDetail.setRecCreateBy(adminBean.getUser().getUserId());
			ieProfileDetail.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			em.persist(ieProfileDetail);
			ieProfileDetail.setIeProfileHeader(ieProfileHeader);
		}
		
		int groupIndex = -1;
		for (IeProfileDetailDisplayForm detailForm : form.getIeProfileCategoryDetails()) {
			if (detailForm.getIeProfileFieldName().equals("catId")) {
				groupIndex++;
			}
			
			IeProfileDetail ieProfileDetail = new IeProfileDetail();
			ieProfileDetail.setIeProfileGroupName(detailForm.getIeProfileGroupName());
			if (!Format.isNullOrEmpty(detailForm.getIeProfileGroupIndex())) {
				ieProfileDetail.setIeProfileGroupIndex(groupIndex);
			}
			ieProfileDetail.setIeProfileFieldName(detailForm.getIeProfileFieldName());
			if (!Format.isNullOrEmpty(detailForm.getIeProfileFieldValue())) {
				ieProfileDetail.setIeProfileFieldValue(detailForm.getIeProfileFieldValue());
			}
			ieProfileDetail.setSeqNum(Integer.valueOf(seqNum++));
			if (!Format.isNullOrEmpty(detailForm.getIeProfilePosition())) {
				ieProfileDetail.setIeProfilePosition(Integer.valueOf(detailForm.getIeProfilePosition()));
			}
			ieProfileDetail.setRecUpdateBy(adminBean.getUser().getUserId());
			ieProfileDetail.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			ieProfileDetail.setRecCreateBy(adminBean.getUser().getUserId());
			ieProfileDetail.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			em.persist(ieProfileDetail);
			ieProfileDetail.setIeProfileHeader(ieProfileHeader);
		}
		
		groupIndex = -1;
		for (IeProfileDetailDisplayForm detailForm : form.getIeProfileItemRelatedDetails()) {
			if (detailForm.getIeProfileFieldName().equals("itemId")) {
				groupIndex++;
			}
			
			IeProfileDetail ieProfileDetail = new IeProfileDetail();
			ieProfileDetail.setIeProfileGroupName(detailForm.getIeProfileGroupName());
			if (!Format.isNullOrEmpty(detailForm.getIeProfileGroupIndex())) {
				ieProfileDetail.setIeProfileGroupIndex(groupIndex);
			}
			ieProfileDetail.setIeProfileFieldName(detailForm.getIeProfileFieldName());
			if (!Format.isNullOrEmpty(detailForm.getIeProfileFieldValue())) {
				ieProfileDetail.setIeProfileFieldValue(detailForm.getIeProfileFieldValue());
			}
			ieProfileDetail.setSeqNum(Integer.valueOf(seqNum++));
			if (!Format.isNullOrEmpty(detailForm.getIeProfilePosition())) {
				ieProfileDetail.setIeProfilePosition(Integer.valueOf(detailForm.getIeProfilePosition()));
			}
			ieProfileDetail.setRecUpdateBy(adminBean.getUser().getUserId());
			ieProfileDetail.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			ieProfileDetail.setRecCreateBy(adminBean.getUser().getUserId());
			ieProfileDetail.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			em.persist(ieProfileDetail);
			ieProfileDetail.setIeProfileHeader(ieProfileHeader);
		}
		
		groupIndex = -1;
		for (IeProfileDetailDisplayForm detailForm : form.getIeProfileItemUpSellDetails()) {
			if (detailForm.getIeProfileFieldName().equals("itemId")) {
				groupIndex++;
			}
			
			IeProfileDetail ieProfileDetail = new IeProfileDetail();
			ieProfileDetail.setIeProfileGroupName(detailForm.getIeProfileGroupName());
			if (!Format.isNullOrEmpty(detailForm.getIeProfileGroupIndex())) {
				ieProfileDetail.setIeProfileGroupIndex(groupIndex);
			}
			ieProfileDetail.setIeProfileFieldName(detailForm.getIeProfileFieldName());
			if (!Format.isNullOrEmpty(detailForm.getIeProfileFieldValue())) {
				ieProfileDetail.setIeProfileFieldValue(detailForm.getIeProfileFieldValue());
			}
			ieProfileDetail.setSeqNum(Integer.valueOf(seqNum++));
			if (!Format.isNullOrEmpty(detailForm.getIeProfilePosition())) {
				ieProfileDetail.setIeProfilePosition(Integer.valueOf(detailForm.getIeProfilePosition()));
			}
			ieProfileDetail.setRecUpdateBy(adminBean.getUser().getUserId());
			ieProfileDetail.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			ieProfileDetail.setRecCreateBy(adminBean.getUser().getUserId());
			ieProfileDetail.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			em.persist(ieProfileDetail);
			ieProfileDetail.setIeProfileHeader(ieProfileHeader);
		}
		
		groupIndex = -1;
		for (IeProfileDetailDisplayForm detailForm : form.getIeProfileItemCrossSellDetails()) {
			if (detailForm.getIeProfileFieldName().equals("itemId")) {
				groupIndex++;
			}
			
			IeProfileDetail ieProfileDetail = new IeProfileDetail();
			ieProfileDetail.setIeProfileGroupName(detailForm.getIeProfileGroupName());
			if (!Format.isNullOrEmpty(detailForm.getIeProfileGroupIndex())) {
				ieProfileDetail.setIeProfileGroupIndex(groupIndex);
			}
			ieProfileDetail.setIeProfileFieldName(detailForm.getIeProfileFieldName());
			if (!Format.isNullOrEmpty(detailForm.getIeProfileFieldValue())) {
				ieProfileDetail.setIeProfileFieldValue(detailForm.getIeProfileFieldValue());
			}
			ieProfileDetail.setSeqNum(Integer.valueOf(seqNum++));
			if (!Format.isNullOrEmpty(detailForm.getIeProfilePosition())) {
				ieProfileDetail.setIeProfilePosition(Integer.valueOf(detailForm.getIeProfilePosition()));
			}
			ieProfileDetail.setRecUpdateBy(adminBean.getUser().getUserId());
			ieProfileDetail.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			ieProfileDetail.setRecCreateBy(adminBean.getUser().getUserId());
			ieProfileDetail.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			em.persist(ieProfileDetail);
			ieProfileDetail.setIeProfileHeader(ieProfileHeader);
		}
		
		groupIndex = -1;
		for (IeProfileDetailDisplayForm detailForm : form.getIeProfileItemTierPriceDetails()) {
			if (detailForm.getIeProfileFieldName().equals("custClassId")) {
				groupIndex++;
			}
			
			IeProfileDetail ieProfileDetail = new IeProfileDetail();
			ieProfileDetail.setIeProfileGroupName(detailForm.getIeProfileGroupName());
			if (!Format.isNullOrEmpty(detailForm.getIeProfileGroupIndex())) {
				ieProfileDetail.setIeProfileGroupIndex(groupIndex);
			}
			ieProfileDetail.setIeProfileFieldName(detailForm.getIeProfileFieldName());
			if (!Format.isNullOrEmpty(detailForm.getIeProfileFieldValue())) {
				ieProfileDetail.setIeProfileFieldValue(detailForm.getIeProfileFieldValue());
			}
			ieProfileDetail.setSeqNum(Integer.valueOf(seqNum++));
			if (!Format.isNullOrEmpty(detailForm.getIeProfilePosition())) {
				ieProfileDetail.setIeProfilePosition(Integer.valueOf(detailForm.getIeProfilePosition()));
			}
			ieProfileDetail.setRecUpdateBy(adminBean.getUser().getUserId());
			ieProfileDetail.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			ieProfileDetail.setRecCreateBy(adminBean.getUser().getUserId());
			ieProfileDetail.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			em.persist(ieProfileDetail);
			ieProfileDetail.setIeProfileHeader(ieProfileHeader);
		}

		groupIndex = -1;
		for (IeProfileDetailDisplayForm detailForm : form.getIeProfileItemAttributeDetails()) {
			if (detailForm.getIeProfileFieldName().equals("customAttribId")) {
				groupIndex++;
			}
			
			IeProfileDetail ieProfileDetail = new IeProfileDetail();
			ieProfileDetail.setIeProfileGroupName(detailForm.getIeProfileGroupName());
			if (!Format.isNullOrEmpty(detailForm.getIeProfileGroupIndex())) {
				ieProfileDetail.setIeProfileGroupIndex(groupIndex);
			}
			ieProfileDetail.setIeProfileFieldName(detailForm.getIeProfileFieldName());
			if (!Format.isNullOrEmpty(detailForm.getIeProfileFieldValue())) {
				ieProfileDetail.setIeProfileFieldValue(detailForm.getIeProfileFieldValue());
			}
			ieProfileDetail.setSeqNum(Integer.valueOf(seqNum++));
			if (!Format.isNullOrEmpty(detailForm.getIeProfilePosition())) {
				ieProfileDetail.setIeProfilePosition(Integer.valueOf(detailForm.getIeProfilePosition()));
			}
			ieProfileDetail.setRecUpdateBy(adminBean.getUser().getUserId());
			ieProfileDetail.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			ieProfileDetail.setRecCreateBy(adminBean.getUser().getUserId());
			ieProfileDetail.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			em.persist(ieProfileDetail);
			ieProfileDetail.setIeProfileHeader(ieProfileHeader);
		}

		groupIndex = -1;
		for (IeProfileDetailDisplayForm detailForm : form.getIeProfileItemImageDetails()) {
			if (detailForm.getIeProfileFieldName().equals("itemImageLocation")) {
				groupIndex++;
			}
			
			IeProfileDetail ieProfileDetail = new IeProfileDetail();
			ieProfileDetail.setIeProfileGroupName(detailForm.getIeProfileGroupName());
			if (!Format.isNullOrEmpty(detailForm.getIeProfileGroupIndex())) {
				ieProfileDetail.setIeProfileGroupIndex(groupIndex);
			}
			ieProfileDetail.setIeProfileFieldName(detailForm.getIeProfileFieldName());
			if (!Format.isNullOrEmpty(detailForm.getIeProfileFieldValue())) {
				ieProfileDetail.setIeProfileFieldValue(detailForm.getIeProfileFieldValue());
			}
			ieProfileDetail.setSeqNum(Integer.valueOf(seqNum++));
			if (!Format.isNullOrEmpty(detailForm.getIeProfilePosition())) {
				ieProfileDetail.setIeProfilePosition(Integer.valueOf(detailForm.getIeProfilePosition()));
			}
			ieProfileDetail.setRecUpdateBy(adminBean.getUser().getUserId());
			ieProfileDetail.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			ieProfileDetail.setRecCreateBy(adminBean.getUser().getUserId());
			ieProfileDetail.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			em.persist(ieProfileDetail);
			ieProfileDetail.setIeProfileHeader(ieProfileHeader);
		}

		groupIndex = -1;
		for (IeProfileDetailDisplayForm detailForm : form.getIeProfileOtherDetails()) {
			if (detailForm.getIeProfileFieldName().equals("default")) {
				groupIndex++;
			}
			
			IeProfileDetail ieProfileDetail = new IeProfileDetail();
			ieProfileDetail.setIeProfileGroupName(detailForm.getIeProfileGroupName());
			if (!Format.isNullOrEmpty(detailForm.getIeProfileGroupIndex())) {
				ieProfileDetail.setIeProfileGroupIndex(groupIndex);
			}
			ieProfileDetail.setIeProfileFieldName(detailForm.getIeProfileFieldName());
			if (!Format.isNullOrEmpty(detailForm.getIeProfileFieldValue())) {
				ieProfileDetail.setIeProfileFieldValue(detailForm.getIeProfileFieldValue());
			}
			ieProfileDetail.setSeqNum(Integer.valueOf(seqNum++));
			if (!Format.isNullOrEmpty(detailForm.getIeProfilePosition())) {
				ieProfileDetail.setIeProfilePosition(Integer.valueOf(detailForm.getIeProfilePosition()));
			}
			ieProfileDetail.setRecUpdateBy(adminBean.getUser().getUserId());
			ieProfileDetail.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			ieProfileDetail.setRecCreateBy(adminBean.getUser().getUserId());
			ieProfileDetail.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			em.persist(ieProfileDetail);
			ieProfileDetail.setIeProfileHeader(ieProfileHeader);
		}
		
        form.setIeProfileHeaderId(Format.getLong(ieProfileHeader.getIeProfileHeaderId()));
		form.setMode("U");
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	private ActionMessages validate(IeProfileMaintActionForm form, HttpServletRequest request) {
		ActionMessages errors = new ActionMessages();
		if (Format.isNullOrEmpty(form.getIeProfileHeaderName())) {
			errors.add("ieProfileHeaderName", new ActionMessage("error.string.required"));
		}
		MessageResources resources = this.getResources(request);
		for (IeProfileDetailDisplayForm detailForm : form.getIeProfileGeneralDetails()) {
			if (!Format.isNullOrEmpty(detailForm.getIeProfilePosition())) {
				if (!Format.isInt(detailForm.getIeProfilePosition())) {
					detailForm.setIeProfilePositionError(resources.getMessage("error.int.invalid"));
					errors.add("dummy", new ActionMessage("error.int.invalid"));
				}
			}
		}
		return errors;
	}

    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("save", "save");
        map.put("edit", "edit");
        map.put("addGroup", "addGroup");
        map.put("removeGroup", "removeGroup");
        map.put("create", "create");
        map.put("remove", "remove");
       return map;
    }
}
