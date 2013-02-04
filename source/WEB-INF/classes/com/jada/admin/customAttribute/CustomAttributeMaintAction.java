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

package com.jada.admin.customAttribute;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import com.jada.util.JSONEscapeObject;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.admin.customAttribute.CustomAttributeMaintActionForm;
import com.jada.dao.CustomAttributeDAO;
import com.jada.dao.CustomAttributeOptionDAO;
import com.jada.dao.SiteProfileClassDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.CustomAttribute;
import com.jada.jpa.entity.CustomAttributeLanguage;
import com.jada.jpa.entity.CustomAttributeOption;
import com.jada.jpa.entity.CustomAttributeOptionCurrency;
import com.jada.jpa.entity.CustomAttributeOptionLanguage;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteCurrencyClass;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.jpa.entity.User;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.translator.BingTranslate;
import com.jada.util.Utility;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

public class CustomAttributeMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest request,
                             HttpServletResponse response)
        throws Throwable {

        CustomAttributeMaintActionForm form = (CustomAttributeMaintActionForm) actionForm;
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
        form.setMode("C");
        
        FormUtils.setFormDisplayMode(request, form, FormUtils.CREATE_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward translate(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
	
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		CustomAttributeMaintActionForm form = (CustomAttributeMaintActionForm) actionForm;
		if (form == null) {
			form = new CustomAttributeMaintActionForm();
		}
		
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
		
		CustomAttribute customAttribute = (CustomAttribute) em.find(CustomAttribute.class, Format.getLong(form.getCustomAttribId()));
		
	    BingTranslate translator = new BingTranslate(form.getFromLocale(), form.getToLocale(), site);
		form.setCustomAttribDescLangFlag(true);
		form.setCustomAttribDescLang(translator.translate(customAttribute.getCustomAttributeLanguage().getCustomAttribDesc()));
		
		if (customAttribute.getCustomAttribDataTypeCode() == Constants.CUSTOM_ATTRIBUTE_DATA_TYPE_STRING) {
			form.setCustomAttribOptionLangFlag(true);
			CustomAttributeOptionDisplayForm optionForms[] = form.getCustomAttribOptions();
			for (int i = 0; i < optionForms.length; i++) {
				CustomAttributeOptionDisplayForm optionForm = optionForms[i];
				optionForm.setCustomAttribValueLang(translator.translate(optionForm.getCustomAttribValue()));
				optionForm.setCustomAttribValueLangFlag(true);
			}
		}
		
		FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}
    
    public ActionForward language(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
	
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		CustomAttributeMaintActionForm form = (CustomAttributeMaintActionForm) actionForm;
		
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
		
		CustomAttribute customAttribute = (CustomAttribute) em.find(CustomAttribute.class, Format.getLong(form.getCustomAttribId()));
        copyProperties(form, customAttribute, request);
		FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}
    
    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

    	Site site = getAdminBean(request).getSite();
        CustomAttributeMaintActionForm form = (CustomAttributeMaintActionForm) actionForm;
		initSiteProfiles(form, site);
		String customAttribId = request.getParameter("customAttribId");
        CustomAttribute customAttribute = new CustomAttribute();
        customAttribute = CustomAttributeDAO.load(site.getSiteId(), Format.getLong(customAttribId));
        form.setMode("U");
        copyProperties(form, customAttribute, request);
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
		
		CustomAttributeMaintActionForm form = (CustomAttributeMaintActionForm) actionForm;
    	Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		try {
			CustomAttribute customAttribute = CustomAttributeDAO.load(site.getSiteId(), Format.getLong(form.getCustomAttribId()));
	        copyProperties(form, customAttribute, request);
			em.remove(customAttribute);
			for (CustomAttributeLanguage customAttributeLanguage : customAttribute.getCustomAttributeLanguages()) {
				em.remove(customAttributeLanguage);
			}
			em.getTransaction().commit();
		}
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.customAttribute.constraint"));
				saveMessages(request, errors);
				return actionMapping.findForward("error");
			}
			throw e;
		}
		
		ActionForward actionForward = actionMapping.findForward("removeSuccess");
		return actionForward;
	}

	public ActionForward save(ActionMapping mapping, 
							  ActionForm actionForm,
							  HttpServletRequest request,
							  HttpServletResponse response) 
		throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		MessageResources resources = this.getResources(request);
		boolean insertMode = false;
		CustomAttributeMaintActionForm form = (CustomAttributeMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}

		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		CustomAttribute customAttribute = new CustomAttribute();
		if (!insertMode) {
			customAttribute = CustomAttributeDAO.load(site.getSiteId(), Format.getLong(form.getCustomAttribId()));
		}

		ActionMessages errors = validate(form, request);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return mapping.findForward("error");
		}

		if (insertMode) {
			customAttribute.setRecCreateBy(adminBean.getUser().getUserId());
			customAttribute.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			form.setCustomAttribDataTypeCodeDesc(resources.getMessage("customAttrib.dataTypeCode." + form.getCustomAttribDataTypeCode()));
			form.setCustomAttribTypeCodeDesc(resources.getMessage("customAttrib.typeCode." + form.getCustomAttribTypeCode()));
			
			CustomAttributeLanguage customAttribLanguage = new CustomAttributeLanguage();
			customAttribute.setSystemRecord(Constants.VALUE_NO);
			customAttribLanguage.setRecCreateBy(adminBean.getUser().getUserId());
			customAttribLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		SiteProfileClass siteProfileClass = (SiteProfileClass) em.find(SiteProfileClass.class, form.getSiteProfileClassDefaultId());
    		customAttribLanguage.setSiteProfileClass(siteProfileClass);
    		customAttribLanguage.setCustomAttribute(customAttribute);
    		customAttribute.setCustomAttributeLanguage(customAttribLanguage);
    		customAttribute.getCustomAttributeLanguages().add(customAttribLanguage);
		}
		customAttribute.setSite(site);
		customAttribute.setCustomAttribName(form.getCustomAttribName());
		customAttribute.setRecUpdateBy(adminBean.getUser().getUserId());
		customAttribute.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		
		if (form.isSiteProfileClassDefault() && form.isSiteCurrencyClassDefault()) {
			saveDefault(customAttribute, form, adminBean);
		}
		else {
			if (!form.isSiteProfileClassDefault()) {
				saveLanguage(customAttribute, form, adminBean);
			}
			if (!form.isSiteCurrencyClassDefault()) {
				saveCurrency(customAttribute, form, adminBean);
			}
		}
		
		if (insertMode) {
			em.persist(customAttribute);
		}
		else {
			// em.update(customAttribute);
		}
        form.setMode("U");
        form.setCustomAttribId(customAttribute.getCustomAttribId().toString());
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	private void saveDefault(CustomAttribute customAttribute, CustomAttributeMaintActionForm form, AdminBean adminBean) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	CustomAttributeLanguage customAttribLanguage = customAttribute.getCustomAttributeLanguage();
    	customAttribLanguage.setCustomAttribDesc(form.getCustomAttribDesc());
    	customAttribLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
    	customAttribLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    	em.persist(customAttribLanguage);
		
		customAttribute.setItemCompare(Constants.VALUE_NO);
		if (form.isItemCompare()) {
			customAttribute.setItemCompare(Constants.VALUE_YES);
		}
		customAttribute.setCustomAttribDataTypeCode(form.getCustomAttribDataTypeCode().charAt(0));
		customAttribute.setCustomAttribTypeCode(form.getCustomAttribTypeCode().charAt(0));
		
		SiteCurrencyClass siteCurrencyClass = (SiteCurrencyClass) em.find(SiteCurrencyClass.class, form.getSiteCurrencyClassId());		
		for (int i = 0; i < form.getCustomAttribOptions().length; i++) {
			CustomAttributeOptionDisplayForm optionForm = form.getCustomAttribOptions()[i];
			CustomAttributeOption customAttributeOption = null;
			boolean exist = false;
			if (Format.isNullOrEmpty(optionForm.getCustomAttribOptionId())) {
				customAttributeOption = new CustomAttributeOption();
				customAttributeOption.setCustomAttribute(customAttribute);
				customAttributeOption.setRecCreateBy(adminBean.getUser().getUserId());
				customAttributeOption.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			}
			else {
				Long customAttribOptionId = Format.getLong(optionForm.getCustomAttribOptionId());
				customAttributeOption = CustomAttributeOptionDAO.load(adminBean.getSite().getSiteId(), customAttribOptionId);
				exist = true;
			}
			customAttributeOption.setCustomAttribSkuCode(optionForm.getCustomAttribSkuCode());
			customAttributeOption.setRecUpdateBy(adminBean.getUser().getUserId());
			customAttributeOption.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			if (!exist) {
				em.persist(customAttributeOption);
			}
			
			if (customAttribute.getCustomAttribDataTypeCode() == Constants.CUSTOM_ATTRIBUTE_DATA_TYPE_CURRENCY) {
				exist = true;
				CustomAttributeOptionCurrency customAttribOptionCurrency = customAttributeOption.getCustomAttributeOptionCurrency();
				if (customAttribOptionCurrency == null) {
					customAttribOptionCurrency = new CustomAttributeOptionCurrency();
					exist = false;
					customAttribOptionCurrency.setSiteCurrencyClass(siteCurrencyClass);
					customAttribOptionCurrency.setCustomAttributeOption(customAttributeOption);
					customAttribOptionCurrency.setRecCreateBy(adminBean.getUser().getUserId());
					customAttribOptionCurrency.setRecCreateDatetime(new Date(System.currentTimeMillis()));
					customAttributeOption.setCustomAttributeOptionCurrency(customAttribOptionCurrency);
					customAttributeOption.getCustomAttributeOptionCurrencies().add(customAttribOptionCurrency);
				}
				customAttribOptionCurrency.setCustomAttribValue(optionForm.getCustomAttribValue());
				customAttribOptionCurrency.setRecUpdateBy(adminBean.getUser().getUserId());
				customAttribOptionCurrency.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
				if (!exist) {
					em.persist(customAttribOptionCurrency);
				}
			}
			else {
				exist = true;
				CustomAttributeOptionLanguage customAttribOptionLanguage = customAttributeOption.getCustomAttributeOptionLanguage();
				if (customAttribOptionLanguage == null) {
					SiteProfileClass siteProfileClass = SiteProfileClassDAO.load(form.getSiteProfileClassId());
					customAttribOptionLanguage = new CustomAttributeOptionLanguage();
					exist = false;
					customAttribOptionLanguage.setCustomAttributeOption(customAttributeOption);
					customAttribOptionLanguage.setSiteProfileClass(siteProfileClass);
					customAttribOptionLanguage.setRecCreateBy(adminBean.getUser().getUserId());
					customAttribOptionLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
					customAttributeOption.setCustomAttributeOptionLanguage(customAttribOptionLanguage);
					customAttributeOption.getCustomAttributeOptionLanguages().add(customAttribOptionLanguage);
				}
				customAttribOptionLanguage.setCustomAttribValue(optionForm.getCustomAttribValue());
				customAttribOptionLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
				customAttribOptionLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));	
				if (!exist) {
					em.persist(customAttribOptionLanguage);
				}
			}
			optionForm.setCustomAttribOptionId(customAttributeOption.getCustomAttribOptionId().toString());
		}
	}
	
	private void saveLanguage(CustomAttribute customAttribute, CustomAttributeMaintActionForm form, AdminBean adminBean) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		CustomAttributeLanguage customAttribLanguage = null;
		User user = adminBean.getUser();
    	Long siteProfileClassId = form.getSiteProfileClassId();
		boolean found = false;
		Iterator<?> iterator = customAttribute.getCustomAttributeLanguages().iterator();
		while (iterator.hasNext()) {
			customAttribLanguage = (CustomAttributeLanguage) iterator.next();
			if (customAttribLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
				found = true;
				break;
			}
		}
		if (!found) {
			customAttribLanguage = new CustomAttributeLanguage();
			customAttribLanguage.setRecCreateBy(user.getUserId());
			customAttribLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		SiteProfileClass siteProfileClass = (SiteProfileClass) em.find(SiteProfileClass.class, siteProfileClassId);
    		customAttribLanguage.setSiteProfileClass(siteProfileClass);
    		customAttribLanguage.setCustomAttribute(customAttribute);
    		customAttribute.getCustomAttributeLanguages().add(customAttribLanguage);
		}
		if (form.isCustomAttribDescLangFlag()) {
			customAttribLanguage.setCustomAttribDesc(form.getCustomAttribDescLang());
		}
		else {
			customAttribLanguage.setCustomAttribDesc(null);
		}
		customAttribLanguage.setRecUpdateBy(user.getUserId());
		customAttribLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(customAttribLanguage);
		
		for (int i = 0; i < form.getCustomAttribOptions().length; i++) {
			CustomAttributeOptionDisplayForm optionForm = form.getCustomAttribOptions()[i];
			CustomAttributeOption customAttributeOption = null;
			Long customAttribOptionId = Format.getLong(optionForm.getCustomAttribOptionId());
			customAttributeOption = CustomAttributeOptionDAO.load(adminBean.getSite().getSiteId(), customAttribOptionId);
			boolean exist = true;
			CustomAttributeOptionLanguage customAttributeOptionLanguage = null;
			for (CustomAttributeOptionLanguage optionLang : customAttributeOption.getCustomAttributeOptionLanguages()) {
				if (optionLang.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
					customAttributeOptionLanguage = optionLang;
				}
			}
			if (customAttributeOptionLanguage == null) {
				customAttributeOptionLanguage = new CustomAttributeOptionLanguage();
				exist = false;
				customAttributeOptionLanguage.setRecCreateBy(user.getUserId());
				customAttributeOptionLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
	    		SiteProfileClass siteProfileClass = (SiteProfileClass) em.find(SiteProfileClass.class, siteProfileClassId);
	    		customAttributeOptionLanguage.setSiteProfileClass(siteProfileClass);
	    		customAttributeOptionLanguage.setCustomAttributeOption(customAttributeOption);
	    		customAttributeOption.getCustomAttributeOptionLanguages().add(customAttributeOptionLanguage);
			}
			
			if (form.isCustomAttribOptionLangFlag()) {
				customAttributeOptionLanguage.setCustomAttribValue(optionForm.getCustomAttribValueLang());
			}
			else {
				customAttributeOptionLanguage.setCustomAttribValue(null);
			}
			customAttributeOptionLanguage.setRecUpdateBy(user.getUserId());
			customAttributeOptionLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			if (!exist) {
				em.persist(customAttributeOptionLanguage);
			}
			optionForm.setCustomAttribValueLangFlag(form.isCustomAttribOptionLangFlag());
		}
	}
	
	private void saveCurrency(CustomAttribute customAttribute, CustomAttributeMaintActionForm form, AdminBean adminBean) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Long siteCurrencyClassId = form.getSiteCurrencyClassId();
		User user = adminBean.getUser();
		for (int i = 0; i < form.getCustomAttribOptions().length; i++) {
			CustomAttributeOptionDisplayForm optionForm = form.getCustomAttribOptions()[i];
			CustomAttributeOption customAttributeOption = null;
			Long customAttribOptionId = Format.getLong(optionForm.getCustomAttribOptionId());
			customAttributeOption = CustomAttributeOptionDAO.load(adminBean.getSite().getSiteId(), customAttribOptionId);				
			boolean exist = true;
			CustomAttributeOptionCurrency customAttributeOptionCurrency = null;
			for (CustomAttributeOptionCurrency optionCurr : customAttributeOption.getCustomAttributeOptionCurrencies()) {
				if (optionCurr.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteCurrencyClassId)) {
					customAttributeOptionCurrency = optionCurr;
				}
			}
			if (customAttributeOptionCurrency == null) {
				customAttributeOptionCurrency = new CustomAttributeOptionCurrency();
				exist = false;
				customAttributeOptionCurrency.setRecCreateBy(user.getUserId());
				customAttributeOptionCurrency.setRecCreateDatetime(new Date(System.currentTimeMillis()));
	    		SiteCurrencyClass siteCurrencyClass = (SiteCurrencyClass) em.find(SiteCurrencyClass.class, siteCurrencyClassId);
	    		customAttributeOptionCurrency.setSiteCurrencyClass(siteCurrencyClass);
	    		customAttributeOptionCurrency.setCustomAttributeOption(customAttributeOption);
	    		customAttributeOption.getCustomAttributeOptionCurrencies().add(customAttributeOptionCurrency);
			}
			
			if (form.isCustomAttribOptionCurrFlag()) {
				customAttributeOptionCurrency.setCustomAttribValue(optionForm.getCustomAttribValueCurr());
			}
			else {
				customAttributeOptionCurrency.setCustomAttribValue(null);
			}
			customAttributeOptionCurrency.setRecUpdateBy(user.getUserId());
			customAttributeOptionCurrency.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			if (!exist) {
				em.persist(customAttributeOptionCurrency);
			}
			optionForm.setCustomAttribValueCurrFlag(form.isCustomAttribOptionCurrFlag());
		}
	}
	
	private void copyProperties(CustomAttributeMaintActionForm form, CustomAttribute customAttribute, HttpServletRequest request) {
		MessageResources resources = this.getResources(request);
		Long siteProfileClassId = form.getSiteProfileClassId();
		Long siteCurrencyClassId = form.getSiteCurrencyClassId();
		form.setCustomAttribId(Format.getLong(customAttribute.getCustomAttribId()));
		form.setCustomAttribName(customAttribute.getCustomAttribName());
		form.setCustomAttribDesc(customAttribute.getCustomAttributeLanguage().getCustomAttribDesc());
		form.setItemCompare(false);
		if (customAttribute.getItemCompare() == Constants.VALUE_YES) {
			form.setItemCompare(true);
		}
		form.setSystemRecord(String.valueOf(customAttribute.getSystemRecord()));
		form.setCustomAttribDataTypeCode(String.valueOf(customAttribute.getCustomAttribDataTypeCode()));
		form.setCustomAttribDataTypeCodeDesc(resources.getMessage("customAttrib.dataTypeCode." + customAttribute.getCustomAttribDataTypeCode()));
		form.setCustomAttribTypeCode(String.valueOf(customAttribute.getCustomAttribTypeCode()));
		form.setCustomAttribTypeCodeDesc(resources.getMessage("customAttrib.typeCode." + customAttribute.getCustomAttribTypeCode()));
		if (!form.isSiteProfileClassDefault()) {
			form.setCustomAttribDescLang(form.getCustomAttribDesc());
			form.setCustomAttribDescLangFlag(false);
			for (CustomAttributeLanguage customAttributeLang : customAttribute.getCustomAttributeLanguages()) {
				if (customAttributeLang.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
					if (customAttributeLang.getCustomAttribDesc() != null) {
						form.setCustomAttribDescLang(customAttributeLang.getCustomAttribDesc());
						form.setCustomAttribDescLangFlag(true);
					}
				}
			}
		}
		
		boolean customAttribOptionLangFlag = false;
		boolean customAttribOptionCurrFlag = false;
		Vector<CustomAttributeOptionDisplayForm> vector = new Vector<CustomAttributeOptionDisplayForm>();
		for (CustomAttributeOption customAttributeOption : customAttribute.getCustomAttributeOptions()) {
			CustomAttributeOptionDisplayForm optionForm = new CustomAttributeOptionDisplayForm();
			optionForm.setCustomAttribOptionId(Format.getLong(customAttributeOption.getCustomAttribOptionId()));
			optionForm.setCustomAttribSkuCode(customAttributeOption.getCustomAttribSkuCode());
			if (customAttribute.getCustomAttribDataTypeCode() == Constants.CUSTOM_ATTRIBUTE_DATA_TYPE_CURRENCY) {
				optionForm.setCustomAttribValue(customAttributeOption.getCustomAttributeOptionCurrency().getCustomAttribValue());
			}
			else {
				optionForm.setCustomAttribValue(customAttributeOption.getCustomAttributeOptionLanguage().getCustomAttribValue());
			}
			optionForm.setCustomAttribValueLang(optionForm.getCustomAttribValue());
			optionForm.setCustomAttribValueLangFlag(false);
			if (!form.isSiteProfileClassDefault()) {
				for (CustomAttributeOptionLanguage customAttributeOptionLang : customAttributeOption.getCustomAttributeOptionLanguages()) {
					if (customAttributeOptionLang.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
						if (customAttributeOptionLang.getCustomAttribValue() != null) {
							optionForm.setCustomAttribValueLang(customAttributeOptionLang.getCustomAttribValue());
							optionForm.setCustomAttribValueLangFlag(true);
							customAttribOptionLangFlag = true;
						}
						break;
					}
				}
			}
			optionForm.setCustomAttribValueCurr(optionForm.getCustomAttribValue());
			optionForm.setCustomAttribValueCurrFlag(false);
			if (!form.isSiteCurrencyClassDefault()) {
				for (CustomAttributeOptionCurrency customAttributeOptionCurr : customAttributeOption.getCustomAttributeOptionCurrencies()) {
					if (customAttributeOptionCurr.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteCurrencyClassId)) {
						if (customAttributeOptionCurr.getCustomAttribValue() != null) {
							optionForm.setCustomAttribValueCurr(customAttributeOptionCurr.getCustomAttribValue());
							optionForm.setCustomAttribValueCurrFlag(true);
							customAttribOptionCurrFlag = true;
						}
						break;
					}
				}
			}
			vector.add(optionForm);
		}
		form.setCustomAttribOptionLangFlag(customAttribOptionLangFlag);
		form.setCustomAttribOptionCurrFlag(customAttribOptionCurrFlag);
		CustomAttributeOptionDisplayForm customAttribOptions[] = new CustomAttributeOptionDisplayForm[vector.size()];
		vector.copyInto(customAttribOptions);
		form.setCustomAttribOptions(customAttribOptions);
	}
	
	public ActionForward removeCustomAttributeOption(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		CustomAttributeMaintActionForm form = (CustomAttributeMaintActionForm) actionForm;
		
		String customAttribOptionIds[] = form.getCustomAttribOptionIds();
		if (customAttribOptionIds != null) {
			for (int i = 0; i < customAttribOptionIds.length; i++) {
				CustomAttributeOption customAttribOption = CustomAttributeOptionDAO.load(site.getSiteId(), Format.getLong(customAttribOptionIds[i]));
				String sql = "select count(*) " +
							 "from   ItemAttributeDetail itemAttributeDetail " +
							 "where  itemAttributeDetail.customAttributeOption = :customAttributeOption ";
				Query query = em.createQuery(sql);
				query.setParameter("customAttributeOption", customAttribOption);
				Long count = (Long) query.getSingleResult();
				if (count.intValue() > 0) {	
					jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
					jsonResult.put("reason", Constants.WEBSERVICE_REASON_INUSE);
					streamWebService(response, jsonResult.toHtmlString());
					return null;
				}
				
				for (CustomAttributeOptionLanguage customAttributeOptionLanguage : customAttribOption.getCustomAttributeOptionLanguages()) {
					em.remove(customAttributeOptionLanguage);
				}
				for (CustomAttributeOptionCurrency customAttributeOptionCurrency : customAttribOption.getCustomAttributeOptionCurrencies()) {
					em.remove(customAttributeOptionCurrency);
				}
				em.remove(customAttribOption);
			}
		}
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		streamWebService(response, jsonResult.toHtmlString());
		return null;
	}
	
	public ActionForward getCustomAttributeOptions(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		CustomAttributeMaintActionForm form = (CustomAttributeMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
	
		Long customAttribId = Format.getLong(form.getCustomAttribId());
		CustomAttribute customAttribute = CustomAttributeDAO.load(site.getSiteId(), customAttribId);
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		Iterator<?> iterator = customAttribute.getCustomAttributeOptions().iterator();
		Vector<JSONEscapeObject> customAttributeOptions = new Vector<JSONEscapeObject>();
		while (iterator.hasNext()) {
			CustomAttributeOption option = (CustomAttributeOption) iterator.next();
			JSONEscapeObject optionObject = new JSONEscapeObject();
			optionObject.put("customAttribOptionId", option.getCustomAttribOptionId());
			if (customAttribute.getCustomAttribDataTypeCode() == Constants.CUSTOM_ATTRIBUTE_DATA_TYPE_CURRENCY) {
				optionObject.put("customAttribValue", option.getCustomAttributeOptionCurrency().getCustomAttribValue());	
			}
			else {
				optionObject.put("customAttribValue", option.getCustomAttributeOptionLanguage().getCustomAttribValue());	
			}
			optionObject.put("customAttribSkuCode", option.getCustomAttribSkuCode());
			customAttributeOptions.add(optionObject);
		}
		jsonResult.put("customAttributeOptions", customAttributeOptions);
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);

		streamWebService(response, jsonResult.toHtmlString());
		return null;
	}

    public ActionMessages validate(CustomAttributeMaintActionForm form, HttpServletRequest request) { 
    	ActionMessages errors = new ActionMessages();
		MessageResources resources = this.getResources(request);
		char customAttribDataType = form.getCustomAttribDataTypeCode().charAt(0);
  	
    	if (Format.isNullOrEmpty(form.getCustomAttribName())) {
    		errors.add("customAttribName", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustomAttribDesc())) {
    		errors.add("customAttribDesc", new ActionMessage("error.string.required"));
    	}
    	CustomAttributeOptionDisplayForm customAttribOptions[] = form.getCustomAttribOptions();
    	if (customAttribOptions != null) {
    		for (int i = 0; i < customAttribOptions.length; i++) {
    			CustomAttributeOptionDisplayForm customAttribOptionForm = customAttribOptions[i];
    			if (Format.isNullOrEmpty(customAttribOptionForm.getCustomAttribValue())) {
    				customAttribOptionForm.setCustomAttribValueError(resources.getMessage("error.string.required"));
    				errors.add("dummy", new ActionMessage("error.string.required"));
    			}
    			else {
					switch (customAttribDataType) {
						case Constants.CUSTOM_ATTRIBUTE_DATA_TYPE_INTEGER:
							if (!Format.isInt(customAttribOptionForm.getCustomAttribValue())) {
								customAttribOptionForm.setCustomAttribValueError(resources.getMessage("error.int.invalid"));
								errors.add("dummy", new ActionMessage("error.string.required"));
							}
							else {
								customAttribOptionForm.setCustomAttribValue(Format.getSimpleInt(Format.getInt(customAttribOptionForm.getCustomAttribValue())));
							}
							break;
						case Constants.CUSTOM_ATTRIBUTE_DATA_TYPE_DECIMAL:
						case Constants.CUSTOM_ATTRIBUTE_DATA_TYPE_CURRENCY:
							if (!Format.isFloat(customAttribOptionForm.getCustomAttribValue())) {
								customAttribOptionForm.setCustomAttribValueError(resources.getMessage("error.float.invalid"));
								errors.add("dummy", new ActionMessage("error.string.required"));
							}
							else {
								customAttribOptionForm.setCustomAttribValue(Format.getSimpleFloat(Format.getFloat(customAttribOptionForm.getCustomAttribValue())));
							}
							break;
					}
    				for (int j = 0; j < i; j++) {
    					if (customAttribOptionForm.getCustomAttribValue().equals(customAttribOptions[j].getCustomAttribValue())) {
    	    				customAttribOptionForm.setCustomAttribValueError(resources.getMessage("error.record.duplicate"));
    	    				errors.add("dummy", new ActionMessage("error.string.required"));
    					}
    				}
    			}
    			if (form.getCustomAttribTypeCode().equals(String.valueOf(Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP))) {
	    			if (Format.isNullOrEmpty(customAttribOptionForm.getCustomAttribSkuCode())) {
	    				customAttribOptionForm.setCustomAttribSkuCodeError(resources.getMessage("error.string.required"));
	    				errors.add("dummy", new ActionMessage("error.string.required"));
	    			}
	    			else {
	    				for (int j = 0; j < i; j++) {
	    					if (customAttribOptionForm.getCustomAttribSkuCode().equals(customAttribOptions[j].getCustomAttribSkuCode())) {
	    	    				customAttribOptionForm.setCustomAttribSkuCodeError(resources.getMessage("error.record.duplicate"));	
	    	    				errors.add("dummy", new ActionMessage("error.string.required"));
	    					}
	    				}
	    			}
    			}
    			customAttribOptionForm.setCustomAttribValueLangFlag(form.isCustomAttribOptionLangFlag());
    		}
    	}
    	
    	if (form.isCustomAttribOptionCurrFlag()) {
        	if (customAttribOptions != null && customAttribDataType == Constants.CUSTOM_ATTRIBUTE_DATA_TYPE_CURRENCY) {
        		for (int i = 0; i < customAttribOptions.length; i++) {
        			CustomAttributeOptionDisplayForm customAttribOptionForm = customAttribOptions[i];
        			if (Format.isNullOrEmpty(customAttribOptionForm.getCustomAttribValueCurr())) {
        				customAttribOptionForm.setCustomAttribValueError(resources.getMessage("error.string.required"));
        				errors.add("dummy", new ActionMessage("error.string.required"));
        			}
        			else {
						if (!Format.isInt(customAttribOptionForm.getCustomAttribValueCurr())) {
							customAttribOptionForm.setCustomAttribValueError(resources.getMessage("error.int.invalid"));
							errors.add("dummy", new ActionMessage("error.string.required"));
						}
						else {
							customAttribOptionForm.setCustomAttribValueCurr(Format.getSimpleFloat(Format.getFloat(customAttribOptionForm.getCustomAttribValueCurr())));
						}
        				for (int j = 0; j < i; j++) {
        					if (customAttribOptionForm.getCustomAttribValueCurr().equals(customAttribOptions[j].getCustomAttribValue())) {
        	    				customAttribOptionForm.setCustomAttribValueError(resources.getMessage("error.record.duplicate"));
        	    				errors.add("dummy", new ActionMessage("error.string.required"));
        					}
        				}
        			}
        			customAttribOptionForm.setCustomAttribValueCurrFlag(form.isCustomAttribOptionCurrFlag());
        		}
        	}
    	}
    	return errors;
    }
	
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("save", "save");
        map.put("edit", "edit");
        map.put("create", "create");
        map.put("remove", "remove");
        map.put("language", "language");
        map.put("translate", "translate");
        map.put("getCustomAttributeOptions", "getCustomAttributeOptions");
        map.put("removeCustomAttributeOption", "removeCustomAttributeOption");
        return map;
    }
}
