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
import com.jada.dao.ShippingMethodDAO;
import com.jada.dao.SiteCurrencyClassDAO;
import com.jada.dao.SiteProfileClassDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.ShippingMethod;
import com.jada.jpa.entity.ShippingMethodLanguage;
import com.jada.jpa.entity.ShippingMethodRegion;
import com.jada.jpa.entity.ShippingMethodRegionType;
import com.jada.jpa.entity.ShippingRate;
import com.jada.jpa.entity.ShippingRateCurrency;
import com.jada.jpa.entity.ShippingRegion;
import com.jada.jpa.entity.ShippingType;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteCurrencyClass;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.jpa.entity.User;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.translator.BingTranslate;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

public class ShippingMethodMaintAction
    extends AdminLookupDispatchAction {
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest request,
                             HttpServletResponse response)
        throws Throwable {

        ShippingMethodMaintActionForm form = (ShippingMethodMaintActionForm) actionForm;
		initSiteProfiles(form, getAdminBean(request).getSite());
        form.setShippingMethodName(null);
        form.setJsonShippingTypes(null);
        form.setShippingTypeIds(null);
        form.setPublished(true);
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
	
    	AdminBean adminBean = getAdminBean(request);
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ShippingMethodMaintActionForm form = (ShippingMethodMaintActionForm) actionForm;
		if (form == null) {
			form = new ShippingMethodMaintActionForm();
		}
		initSiteProfiles(form, getAdminBean(request).getSite());
		
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
		ShippingMethod shippingMethod = (ShippingMethod) em.find(ShippingMethod.class, Format.getLong(form.getShippingMethodId()));
		
        BingTranslate translator = new BingTranslate(form.getFromLocale(), form.getToLocale(), site);
		form.setShippingMethodNameLangFlag(true);
		form.setShippingMethodNameLang(translator.translate(shippingMethod.getShippingMethodLanguage().getShippingMethodName()));
		
		initJsonSmRegions(form, shippingMethod, site.getSiteId(), adminBean.getUser());
		
		FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}
    
    public ActionForward language(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
    	AdminBean adminBean = getAdminBean(request);
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ShippingMethodMaintActionForm form = (ShippingMethodMaintActionForm) actionForm;
		if (form == null) {
			form = new ShippingMethodMaintActionForm();
		}
		initSiteProfiles(form, getAdminBean(request).getSite());
		
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
		ShippingMethod shippingMethod = (ShippingMethod) em.find(ShippingMethod.class, Format.getLong(form.getShippingMethodId()));
		
		if (!form.isSiteProfileClassDefault()) {
			form.setShippingMethodNameLangFlag(false);
			form.setShippingMethodNameLang(shippingMethod.getShippingMethodLanguage().getShippingMethodName());
	    	boolean found = false;
	    	ShippingMethodLanguage shippingMethodLanguage = null;
	    	Long siteProfileClassId = form.getSiteProfileClassId();
	    	Iterator<?> iterator = shippingMethod.getShippingMethodLanguages().iterator();
	    	while (iterator.hasNext()) {
	    		shippingMethodLanguage = (ShippingMethodLanguage) iterator.next();
	    		if (shippingMethodLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
	    			found = true;
	    			break;
	    		}
	    	}
	    	if (found) {
	    		if (shippingMethodLanguage.getShippingMethodName() != null) {
		    		form.setShippingMethodNameLangFlag(true);
		    		form.setShippingMethodNameLang(shippingMethodLanguage.getShippingMethodName());
	    		}
	    	}
		}
		initJsonSmRegions(form, shippingMethod, site.getSiteId(), adminBean.getUser());
		
		FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}

    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        ShippingMethodMaintActionForm form = (ShippingMethodMaintActionForm) actionForm;
        if (form == null) {
            form = new ShippingMethodMaintActionForm();
        }
		initSiteProfiles(form, site);
		String shippingMethodId = request.getParameter("shippingMethodId");
        ShippingMethod shippingMethod = new ShippingMethod();
        shippingMethod = ShippingMethodDAO.load(site.getSiteId(), Format.getLong(shippingMethodId));
        form.setMode("U");
		form.setShippingMethodId(Format.getLong(shippingMethod.getShippingMethodId()));
		form.setShippingMethodName(shippingMethod.getShippingMethodLanguage().getShippingMethodName());
		form.setPublished(shippingMethod.getPublished() == Constants.PUBLISHED_YES);
		
		if (!form.isSiteProfileClassDefault()) {
			form.setShippingMethodNameLang(shippingMethod.getShippingMethodLanguage().getShippingMethodName());
	    	boolean found = false;
	    	ShippingMethodLanguage shippingMethodLanguage = null;
	    	Long siteProfileClassId = form.getSiteProfileClassId();
	    	Iterator<?> iterator = shippingMethod.getShippingMethodLanguages().iterator();
	    	while (iterator.hasNext()) {
	    		shippingMethodLanguage = (ShippingMethodLanguage) iterator.next();
	    		if (shippingMethodLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
	    			found = true;
	    			break;
	    		}
	    	}
	    	if (found) {
	    		if (shippingMethodLanguage.getShippingMethodName() != null) {
		    		form.setShippingMethodNameLangFlag(true);
		    		form.setShippingMethodNameLang(shippingMethodLanguage.getShippingMethodName());
	    		}
	    	}
		}

		initJsonSmRegions(form, shippingMethod, site.getSiteId(), adminBean.getUser());

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
		ShippingMethodMaintActionForm form = (ShippingMethodMaintActionForm) actionForm;
		ShippingMethod shippingMethod = ShippingMethodDAO.load(site.getSiteId(), Format.getLong(form.getShippingMethodId()));
		em.remove(shippingMethod);
		for (ShippingMethodLanguage shippingMethodLanguage : shippingMethod.getShippingMethodLanguages())  {
			em.remove(shippingMethodLanguage);
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
    	AdminBean adminBean = getAdminBean(request);
    	Site site = adminBean.getSite();
		boolean insertMode = false;
		ShippingMethodMaintActionForm form = (ShippingMethodMaintActionForm) actionForm;
		initSiteProfiles(form, site);
		ActionMessages errors = validate(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return mapping.findForward("error");
		}
		if (form.getMode().equals("C")) {
			insertMode = true;
		}
		
		ShippingMethod shippingMethod = new ShippingMethod();
		ShippingMethodLanguage shippingMethodLanguage = new ShippingMethodLanguage();
		if (!insertMode) {
			shippingMethod = ShippingMethodDAO.load(site.getSiteId(), Format.getLong(form.getShippingMethodId()));
			shippingMethodLanguage = shippingMethod.getShippingMethodLanguage();
		}
		if (insertMode) {
        	String sql = "select max(seqNum) " +
            			 "from   ShippingMethod " +
            			 "where  siteId = :siteId ";
        	int seqNum = 0;
			Query query = em.createQuery(sql);
			query.setParameter("siteId", site.getSiteId());
			Integer i = (Integer) query.getResultList().iterator().next();
			if (i != null) {
				seqNum = i.intValue() + 1;
			}
			shippingMethod.setSeqNum(seqNum);
			shippingMethod.setSite(site);
			shippingMethod.setRecCreateBy(adminBean.getUser().getUserId());
			shippingMethod.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			
			shippingMethodLanguage.setRecCreateBy(adminBean.getUser().getUserId());
			shippingMethodLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			shippingMethodLanguage.setShippingMethod(shippingMethod);
			SiteProfileClass siteProfileClass = SiteProfileClassDAO.load(form.getSiteProfileClassId());
			shippingMethodLanguage.setSiteProfileClass(siteProfileClass);
			shippingMethod.getShippingMethodLanguages().add(shippingMethodLanguage);
		}

		for (ShippingMethodRegion shippingMethodRegion : shippingMethod.getShippingMethodRegions()) {
			shippingMethodRegion.setPublished(Constants.PUBLISHED_NO);
			String shippingRegionId = shippingMethodRegion.getShippingRegion().getShippingRegionId().toString();
			if (form.getShippingRegionIds() != null) {
				for (int i = 0; i < form.getShippingRegionIds().length; i++) {
					if (shippingRegionId.equals(form.getShippingRegionIds()[i])) {
						shippingMethodRegion.setPublished(Constants.PUBLISHED_YES);
						break;
					}
				}
			}
			shippingMethodRegion.setRecUpdateBy(adminBean.getUser().getUserId());
			shippingMethodRegion.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			em.persist(shippingMethodRegion);
		}

		if (form.isSiteProfileClassDefault()) {
			saveDefault(shippingMethod, shippingMethodLanguage, form, adminBean);
			shippingMethod.setShippingMethodLanguage(shippingMethodLanguage);
			if (insertMode) {
				em.persist(shippingMethodLanguage);
				em.persist(shippingMethod);
			}
		}
		else {
			saveLanguage(shippingMethod, form, adminBean);			
		}
		initJsonSmRegions(form, shippingMethod, site.getSiteId(), adminBean.getUser());

		form.setMode("U");
		form.setShippingMethodId(shippingMethod.getShippingMethodId().toString());
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	public void saveDefault(ShippingMethod shippingMethod, ShippingMethodLanguage shippingMethodLanguage, ShippingMethodMaintActionForm form, AdminBean adminBean) throws Exception {
		shippingMethodLanguage.setShippingMethodName(form.getShippingMethodName());
		shippingMethodLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
		shippingMethodLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		
		shippingMethod.setPublished(form.isPublished() ? Constants.PUBLISHED_YES : Constants.PUBLISHED_NO);
		shippingMethod.setRecUpdateBy(adminBean.getUser().getUserId());
		shippingMethod.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
	}
	
	public void saveLanguage(ShippingMethod shippingMethod, ShippingMethodMaintActionForm form, AdminBean adminBean) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Long siteProfileClassId = form.getSiteProfileClassId();
    	User user = adminBean.getUser();
    	Iterator<?> iterator = shippingMethod.getShippingMethodLanguages().iterator();
    	boolean found = false;
    	ShippingMethodLanguage shippingMethodLanguage = null;
    	while (iterator.hasNext()) {
    		shippingMethodLanguage = (ShippingMethodLanguage) iterator.next();
    		if (shippingMethodLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
    			found = true;
    			break;
    		}
    	}
    	if (!found) {
    		shippingMethodLanguage = new ShippingMethodLanguage();
    		shippingMethodLanguage.setRecCreateBy(user.getUserId());
    		shippingMethodLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		SiteProfileClass siteProfileClass = (SiteProfileClass) em.find(SiteProfileClass.class, siteProfileClassId);
    		shippingMethodLanguage.setSiteProfileClass(siteProfileClass);
    		shippingMethodLanguage.setShippingMethod(shippingMethod);
    	}
    	if (form.isShippingMethodNameLangFlag()) {
    		shippingMethodLanguage.setShippingMethodName(form.getShippingMethodNameLang());
    	}
    	else {
    		shippingMethodLanguage.setShippingMethodName(null);
    	}

    	shippingMethodLanguage.setRecUpdateBy(user.getUserId());
    	shippingMethodLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    	em.persist(shippingMethodLanguage);
	}
	
	private void initJsonSmRegions(ShippingMethodMaintActionForm form, ShippingMethod shippingMethod, String siteId, User user) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String sql = "from ShippingType shippingType where shippingType.site.siteId = :siteId order by shippingTypeName";
		Query query = em.createQuery(sql);
		query.setParameter("siteId", siteId);
		Iterator<?> iterator = query.getResultList().iterator();
		Vector<ShippingType> stVector = new Vector<ShippingType>();
		while (iterator.hasNext()) {
			ShippingType shippingType = (ShippingType) iterator.next();
			stVector.add(shippingType);
		}
		ShippingType shippingTypes[] = new ShippingType[stVector.size()];
		stVector.toArray(shippingTypes);
		
		Vector<JSONEscapeObject> jsonShippingRegions = new Vector<JSONEscapeObject>();
		sql = "from ShippingRegion shippingRegion where shippingRegion.site.siteId = :siteId and shippingRegionName = :shippingRegionName order by shippingRegionName";
		query = em.createQuery(sql);
		query.setParameter("siteId", siteId);
		query.setParameter("shippingRegionName", Constants.SHIPPINGREGION_OTHERS);
		iterator = query.getResultList().iterator();
		Vector<ShippingRegion> srVector = new Vector<ShippingRegion>();
		while (iterator.hasNext()) {
			ShippingRegion shippingRegion = (ShippingRegion) iterator.next();
			srVector.add(shippingRegion);
			
			JSONEscapeObject JSONEscapeObject = new JSONEscapeObject();
			JSONEscapeObject.put("shippingRegionId", shippingRegion.getShippingRegionId().toString());
			JSONEscapeObject.put("shippingRegionName", shippingRegion.getShippingRegionName());
			jsonShippingRegions.add(JSONEscapeObject);
		}
		sql = "from ShippingRegion shippingRegion where shippingRegion.site.siteId = :siteId and shippingRegionName != :shippingRegionName order by shippingRegionName";
		query = em.createQuery(sql);
		query.setParameter("siteId", siteId);
		query.setParameter("shippingRegionName", Constants.SHIPPINGREGION_OTHERS);
		iterator = query.getResultList().iterator();
		srVector = new Vector<ShippingRegion>();
		while (iterator.hasNext()) {
			ShippingRegion shippingRegion = (ShippingRegion) iterator.next();
			srVector.add(shippingRegion);

			JSONEscapeObject JSONEscapeObject = new JSONEscapeObject();
			JSONEscapeObject.put("shippingRegionId", shippingRegion.getShippingRegionId().toString());
			JSONEscapeObject.put("shippingRegionName", shippingRegion.getShippingRegionName());
			
			boolean published = false;
			for (ShippingMethodRegion shippingMethodRegion : shippingMethod.getShippingMethodRegions()) {
				if (shippingMethodRegion.getShippingRegion().getShippingRegionId().equals(shippingRegion.getShippingRegionId())) {
					if (shippingMethodRegion.getPublished() == Constants.VALUE_YES) {
						published = true;
						break;
					}
				}
			}
			JSONEscapeObject.put("published", published);

			jsonShippingRegions.add(JSONEscapeObject);
		}
		ShippingRegion shippingRegions[] = new ShippingRegion[srVector.size()];
		srVector.toArray(shippingRegions);
		
		Vector<JSONEscapeObject> jsonShippingTypes = new Vector<JSONEscapeObject>();
		for (int i = 0; i < shippingTypes.length; i++) {
			JSONEscapeObject JSONEscapeObject = new JSONEscapeObject();
			JSONEscapeObject.put("shippingTypeId", shippingTypes[i].getShippingTypeId());
			JSONEscapeObject.put("shippingTypeName", shippingTypes[i].getShippingTypeName());
			for (int j = 0; j < shippingRegions.length; j++) {
				ShippingRate shippingRate = findRate(shippingMethod, shippingRegions[j], shippingTypes[i], user);
				JSONEscapeObject jsonShippingRate = new JSONEscapeObject();
				jsonShippingRate.put("shippingMethodId", shippingMethod.getShippingMethodId().toString());
				jsonShippingRate.put("shippingRegionId", shippingRegions[j].getShippingRegionId().toString());
				jsonShippingRate.put("shippingTypeId", shippingTypes[i].getShippingTypeId().toString());
				if (shippingRate != null) {
					JSONEscapeObject.put("exist", true);
					if (shippingRate.getShippingRateCurrency() == null || shippingRate.getShippingRateCurrency().getShippingRateFee() == null) {
						jsonShippingRate.put("shippingRateFee", "");
					}
					else {
						jsonShippingRate.put("shippingRateFee", Format.getFloat(shippingRate.getShippingRateCurrency().getShippingRateFee()));
					}
					if (shippingRate.getShippingRatePercentage() == null) {
						jsonShippingRate.put("shippingRatePercentage", "");
					}
					else {
						jsonShippingRate.put("shippingRatePercentage", Format.getFloat(shippingRate.getShippingRatePercentage()));
					}
					if (shippingRate.getShippingRateCurrency() == null || shippingRate.getShippingRateCurrency().getShippingAdditionalRateFee() == null) {
						jsonShippingRate.put("shippingAdditionalRateFee", "");
					}
					else {
						jsonShippingRate.put("shippingAdditionalRateFee", Format.getFloat(shippingRate.getShippingRateCurrency().getShippingAdditionalRateFee()));
					}
					if (shippingRate.getShippingAdditionalRatePercentage() == null) {
						jsonShippingRate.put("shippingAdditionalRatePercentage", "");
					}
					else {
						jsonShippingRate.put("shippingAdditionalRatePercentage", Format.getFloat(shippingRate.getShippingAdditionalRatePercentage()));
					}
					JSONEscapeObject.put(shippingRegions[j].getShippingRegionId().toString(), jsonShippingRate);
				}
				else {
					JSONEscapeObject.put("exist", false);
					jsonShippingRate.put("shippingRateFee", "");
					jsonShippingRate.put("shippingRatePercentage", "");
					jsonShippingRate.put("shippingAdditionalRateFee", "");
					jsonShippingRate.put("shippingAdditionalRatePercentage", "");
					JSONEscapeObject.put(shippingRegions[j].getShippingRegionId().toString(), jsonShippingRate);
				}
				if (!form.isSiteCurrencyClassDefault()) {
					if (shippingRate != null) {
						ShippingRateCurrency shippingRateCurrency = new ShippingRateCurrency();
						for (ShippingRateCurrency srCurrency : shippingRate.getShippingRateCurrencies()) {
							if (srCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(form.getSiteCurrencyClassId())) {
								shippingRateCurrency = srCurrency;
								break;
							}
						}
						if (shippingRateCurrency.getShippingRateFee() != null) {
							jsonShippingRate.put("shippingRateFeeCurr", Format.getFloat(shippingRateCurrency.getShippingRateFee()));
							jsonShippingRate.put("shippingRateFeeCurrFlag", true);
						}
						else {
							jsonShippingRate.put("shippingRateFeeCurr", "");
							jsonShippingRate.put("shippingRateFeeCurrFlag", false);
						}
						if (shippingRateCurrency.getShippingAdditionalRateFee() != null) {
							jsonShippingRate.put("shippingAdditionalRateFeeCurr", Format.getFloat(shippingRateCurrency.getShippingAdditionalRateFee()));
							jsonShippingRate.put("shippingAdditionalRateFeeCurrFlag", true);
						}
						else {
							jsonShippingRate.put("shippingAdditionalRateFeeCurr", "");
							jsonShippingRate.put("shippingAdditionalRateFeeCurrFlag", false);
						}
					}
					else {
						jsonShippingRate.put("shippingRateFeeCurr", "");
						jsonShippingRate.put("shippingRateFeeCurrFlag", false);
						jsonShippingRate.put("shippingAdditionalRateFeeCurr", "");
						jsonShippingRate.put("shippingAdditionalRateFeeCurrFlag", false);
					}
				}
			}
			jsonShippingTypes.add(JSONEscapeObject);
		}
		JSONEscapeObject JSONEscapeObject = new JSONEscapeObject();
		JSONEscapeObject.put("shippingRegions", jsonShippingRegions);
		JSONEscapeObject.put("shippingTypes", jsonShippingTypes);
		form.setJsonShippingTypes(JSONEscapeObject.toHtmlString());
	}
	
	private ShippingRate findRate(ShippingMethod shippingMethod, ShippingRegion shippingRegion, ShippingType shippingType, User user) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ShippingRate shippingRate = null;
		boolean foundRegion = false;
		boolean foundRegionType = false;
		ShippingMethodRegion shippingMethodRegion = null;
		for (ShippingMethodRegion smr : shippingMethod.getShippingMethodRegions()) {
			if (smr.getShippingRegion().getShippingRegionId() != shippingRegion.getShippingRegionId()) {
				continue;
			}
			shippingMethodRegion = smr;
			foundRegion = true;
			for (ShippingMethodRegionType shippingMethodRegionType : shippingMethodRegion.getShippingMethodRegionTypes()) {
				if (!shippingMethodRegionType.getShippingType().getShippingTypeId().equals(shippingType.getShippingTypeId())) {
					continue;
				}
				foundRegionType = true;
				shippingRate = shippingMethodRegionType.getShippingRate();
				return shippingRate;
			}
		}
		if (!foundRegion) {
			shippingMethodRegion = new ShippingMethodRegion();
			shippingMethodRegion.setPublished(Constants.VALUE_NO);
			shippingMethodRegion.setRecUpdateBy(user.getUserId());
			shippingMethodRegion.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			shippingMethodRegion.setRecCreateBy(user.getUserId());
			shippingMethodRegion.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			shippingMethodRegion.setShippingRegion(shippingRegion);
			shippingMethodRegion.setShippingMethod(shippingMethod);
	    	em.persist(shippingMethodRegion);
	    	shippingMethod.getShippingMethodRegions().add(shippingMethodRegion);
		}
		if (!foundRegionType) {
	    	ShippingMethodRegionType shippingMethodRegionType = new ShippingMethodRegionType();
	    	shippingMethodRegionType.setPublished(Constants.VALUE_NO);
	    	shippingMethodRegionType.setRecUpdateBy(user.getUserId());
	    	shippingMethodRegionType.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
	    	shippingMethodRegionType.setRecCreateBy(user.getUserId());
	    	shippingMethodRegionType.setRecCreateDatetime(new Date(System.currentTimeMillis()));
	    	shippingMethodRegionType.setShippingMethod(shippingMethod);
	    	shippingMethodRegionType.setShippingMethodRegion(shippingMethodRegion);
	    	shippingMethodRegionType.setShippingRegion(shippingRegion);
	    	shippingMethodRegionType.setShippingType(shippingType);
	    	em.persist(shippingMethodRegionType);
	    	shippingMethodRegion.getShippingMethodRegionTypes().add(shippingMethodRegionType);
		}
		return null;
	}

    public ActionMessages validate(ShippingMethodMaintActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	
    	if (Format.isNullOrEmpty(form.getShippingMethodName())) {
    		errors.add("shippingMethodName", new ActionMessage("error.string.required"));
    	}
    	return errors;
    }
    
    public ActionForward getJsonRate(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	AdminBean adminBean = getAdminBean(request);
    	ShippingMethodMaintActionForm form = (ShippingMethodMaintActionForm) actionForm;
    	this.initSiteProfiles(form, adminBean.getSite());

		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Long shippingRegionId = Format.getLong(form.getShippingRegionId());
    	Long shippingMethodId = Format.getLong(form.getShippingMethodId());
    	Long shippingTypeId = Format.getLong(form.getShippingTypeId());
		ShippingMethod shippingMethod = (ShippingMethod) em.find(ShippingMethod.class, Format.getLong(form.getShippingMethodId()));
		
		JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		JSONEscapeObject jsonRate = new JSONEscapeObject();
    	jsonRate.put("siteCurrencyClassId", form.getSiteCurrencyClassId());
    	jsonRate.put("siteCurrencyClassDefault", form.isSiteCurrencyClassDefault());
    	jsonRate.put("shippingRegionId", shippingRegionId.toString());
    	jsonRate.put("shippingTypeId", shippingTypeId.toString());
    	jsonRate.put("shippingMethodId", shippingMethodId.toString());
		ShippingRate shippingRate = new ShippingRate();
		for (ShippingMethodRegion shippingMethodRegion : shippingMethod.getShippingMethodRegions()) {
			if (shippingMethodRegion.getShippingRegion().getShippingRegionId().equals(shippingRegionId)) {
				for (ShippingMethodRegionType shippingMethodRegionType : shippingMethodRegion.getShippingMethodRegionTypes()) {
					if (shippingMethodRegionType.getShippingType().getShippingTypeId().equals(shippingTypeId)) {
						if (shippingMethodRegionType.getShippingRate() != null) {
							shippingRate = shippingMethodRegionType.getShippingRate();
						}
					}
				}
			}
		}
		ShippingRateCurrency shippingRateCurrency = new ShippingRateCurrency();
		if (shippingRate.getShippingRateCurrency() != null) {
			shippingRateCurrency = shippingRate.getShippingRateCurrency();
		}
		if (shippingRateCurrency.getShippingRateFee() != null) {
			jsonRate.put("shippingRateFee", Format.getFloat(shippingRate.getShippingRateCurrency().getShippingRateFee()));
		}
		else {
			jsonRate.put("shippingRateFee", "");
		}
		if (shippingRate.getShippingRatePercentage() != null) {
			jsonRate.put("shippingRatePercentage", Format.getFloat(shippingRate.getShippingRatePercentage()));
		}
		else {
			jsonRate.put("shippingRatePercentage", "");
		}
		if (shippingRateCurrency.getShippingAdditionalRateFee() != null) {
			jsonRate.put("shippingAdditionalRateFee", Format.getFloat(shippingRate.getShippingRateCurrency().getShippingAdditionalRateFee()));
		}
		else {
			jsonRate.put("shippingAdditionalRateFee", "");
		}
		if (shippingRate.getShippingAdditionalRatePercentage() != null) {
			jsonRate.put("shippingAdditionalRatePercentage", Format.getFloat(shippingRate.getShippingAdditionalRatePercentage()));
		}
		else {
			jsonRate.put("shippingAdditionalRatePercentage", "");
		}
		
		if (!form.isSiteCurrencyClassDefault()) {
			shippingRateCurrency = new ShippingRateCurrency();
			for (ShippingRateCurrency srCurrency : shippingRate.getShippingRateCurrencies()) {
				if (srCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(form.getSiteCurrencyClassId())) {
					shippingRateCurrency = srCurrency;
					break;
				}
			}
			if (shippingRateCurrency.getShippingRateFee() != null) {
				jsonRate.put("shippingRateFeeCurr", Format.getFloat(shippingRateCurrency.getShippingRateFee()));
				jsonRate.put("shippingRateFeeCurrFlag", true);
			}
			else {
				jsonRate.put("shippingRateFeeCurr", "");
				jsonRate.put("shippingRateFeeCurrFlag", false);
			}
			if (shippingRateCurrency.getShippingAdditionalRateFee() != null) {
				jsonRate.put("shippingAdditionalRateFeeCurr", Format.getFloat(shippingRateCurrency.getShippingAdditionalRateFee()));
				jsonRate.put("shippingAdditionalRateFeeCurrFlag", true);
			}
			else {
				jsonRate.put("shippingAdditionalRateFeeCurr", "");
				jsonRate.put("shippingAdditionalRateFeeCurrFlag", false);
			}
		}
		jsonResult.put("shippingRate", jsonRate);
		
		this.streamWebService(response, jsonResult.toHtmlString());
       	return null;
   }

    public ActionForward updateJsonRate(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	AdminBean adminBean = getAdminBean(request);
    	ShippingMethodMaintActionForm form = (ShippingMethodMaintActionForm) actionForm;
    	this.initSiteProfiles(form, adminBean.getSite());

		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		JSONEscapeObject jsonResult = new JSONEscapeObject();
	
    	boolean error = false;
		MessageResources resources = this.getResources(request);
		if (form.isSiteCurrencyClassDefault()) {
			if (!Format.isNullOrEmpty(form.getShippingRateFee())) {
				if (!Format.isFloat(form.getShippingRateFee())) {
					jsonResult.put("shippingRateFeeError", resources.getMessage("error.float.invalid"));
					error = true;
				}
			}
			if (!Format.isNullOrEmpty(form.getShippingRatePercentage())) {
				if (!Format.isFloat(form.getShippingRatePercentage())) {
					jsonResult.put("shippingRatePercentageError", resources.getMessage("error.float.invalid"));
					error = true;
				}
			}
			if (!Format.isNullOrEmpty(form.getShippingAdditionalRateFee())) {
				if (!Format.isFloat(form.getShippingAdditionalRateFee())) {
					jsonResult.put("shippingRateAdditionalFeeError", resources.getMessage("error.float.invalid"));
					error = true;
				}
			}
			if (!Format.isNullOrEmpty(form.getShippingAdditionalRatePercentage())) {
				if (!Format.isFloat(form.getShippingAdditionalRatePercentage())) {
					jsonResult.put("shippingAdditionalRatePercentageError", resources.getMessage("error.float.invalid"));
					error = true;
				}
			}
		}
		else {
			if (!Format.isNullOrEmpty(form.getShippingRateFeeCurr())) {
				if (!Format.isFloat(form.getShippingRateFeeCurr())) {
					jsonResult.put("shippingRateFeeCurrError", resources.getMessage("error.float.invalid"));
					error = true;
				}
			}
			if (!Format.isNullOrEmpty(form.getShippingAdditionalRateFeeCurr())) {
				if (!Format.isFloat(form.getShippingAdditionalRateFeeCurr())) {
					jsonResult.put("shippingRateAdditionalFeeCurrError", resources.getMessage("error.float.invalid"));
					error = true;
				}
			}
		}
		if (error) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			this.streamWebService(response, jsonResult.toHtmlString());
			return null;
		}
		
    	Long shippingRegionId = Format.getLong(form.getShippingRegionId());
    	Long shippingMethodId = Format.getLong(form.getShippingMethodId());
    	Long shippingTypeId = Format.getLong(form.getShippingTypeId());
		ShippingMethod shippingMethod = (ShippingMethod) em.find(ShippingMethod.class, shippingMethodId);

		ShippingRate shippingRate = null;
		ShippingRateCurrency shippingRateCurrency = null;
		ShippingMethodRegionType shippingMethodRegionType = null;
		for (ShippingMethodRegion shippingMethodRegion : shippingMethod.getShippingMethodRegions()) {
			if (shippingMethodRegion.getShippingRegion().getShippingRegionId().equals(shippingRegionId)) {
				for (ShippingMethodRegionType smrt : shippingMethodRegion.getShippingMethodRegionTypes()) {
					if (smrt.getShippingType().getShippingTypeId().equals(shippingTypeId)) {
						shippingMethodRegionType = smrt;
						shippingRate = shippingMethodRegionType.getShippingRate();
						break;
					}
				}
			}
		}
		if (shippingRate == null) {
			shippingRate = new ShippingRate();
			shippingRate.setRecUpdateBy(adminBean.getUser().getUserId());
			shippingRate.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			shippingRate.setRecCreateBy(adminBean.getUser().getUserId());
			shippingRate.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			shippingRate.setPublished(Constants.VALUE_YES);
			shippingMethodRegionType.setShippingRate(shippingRate);
		}
		if (form.isSiteCurrencyClassDefault()) {
			shippingRateCurrency = shippingRate.getShippingRateCurrency();
			if (shippingRateCurrency == null) {
				shippingRateCurrency = new ShippingRateCurrency();
				shippingRateCurrency.setShippingRate(shippingRate);
				shippingRateCurrency.setRecCreateBy(adminBean.getUser().getUserId());
				shippingRateCurrency.setRecCreateDatetime(new Date(System.currentTimeMillis()));
	    		SiteCurrencyClass siteCurrencyClass = SiteCurrencyClassDAO.load(form.getSiteCurrencyClassDefaultId());
	    		shippingRateCurrency.setSiteCurrencyClass(siteCurrencyClass);
				shippingRate.setShippingRateCurrency(shippingRateCurrency);
				shippingRate.getShippingRateCurrencies().add(shippingRateCurrency);
			}
			if (!Format.isNullOrEmpty(form.getShippingRateFee())) {
				shippingRateCurrency.setShippingRateFee(Format.getFloatObj(form.getShippingRateFee()));
			}
			if (!Format.isNullOrEmpty(form.getShippingRatePercentage())) {
				shippingRate.setShippingRatePercentage(Format.getFloatObj(form.getShippingRatePercentage()));
			}
			if (!Format.isNullOrEmpty(form.getShippingAdditionalRateFee())) {
				shippingRateCurrency.setShippingAdditionalRateFee(Format.getFloatObj(form.getShippingAdditionalRateFee()));
			}
			if (!Format.isNullOrEmpty(form.getShippingAdditionalRatePercentage())) {
				shippingRate.setShippingAdditionalRatePercentage(Format.getFloatObj(form.getShippingAdditionalRatePercentage()));
			}
			shippingRate.setRecUpdateBy(adminBean.getUser().getUserId());
			shippingRate.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			
			shippingRateCurrency.setRecUpdateBy(adminBean.getUser().getUserId());
			shippingRateCurrency.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			if (shippingRateCurrency.getShippingRateCurrId() == null) {
				em.persist(shippingRateCurrency);
			}
			if (shippingRate.getShippingRateId() == null) {
				em.persist(shippingRate);
			}
	
			if (shippingRate.getShippingRateCurrency().getShippingRateFee() != null) {
				jsonResult.put("shippingRateFee", Format.getFloat(shippingRate.getShippingRateCurrency().getShippingRateFee()));
			}
			else {
				jsonResult.put("shippingRateFee", "");
			}
			if (shippingRate.getShippingRatePercentage() != null) {
				jsonResult.put("shippingRatePercentage", Format.getFloat(shippingRate.getShippingRatePercentage()));
			}
			else {
				jsonResult.put("shippingRatePercentage", "");
			}
			if (shippingRate.getShippingRateCurrency().getShippingAdditionalRateFee() != null) {
				jsonResult.put("shippingAdditionalRateFee", Format.getFloat(shippingRate.getShippingRateCurrency().getShippingAdditionalRateFee()));
			}
			else {
				jsonResult.put("shippingAdditionalRateFee", "");
			}
			if (shippingRate.getShippingAdditionalRatePercentage() != null) {
				jsonResult.put("shippingAdditionalRatePercentage", Format.getFloat(shippingRate.getShippingAdditionalRatePercentage()));
			}
			else {
				jsonResult.put("shippingAdditionalRatePercentage", "");
			}
		}
		else {
			shippingRateCurrency = null;
			for (ShippingRateCurrency srCurrency : shippingRate.getShippingRateCurrencies()) {
				if (srCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(form.getSiteCurrencyClassId())) {
					shippingRateCurrency = srCurrency;
				}
			}
			if (shippingRateCurrency == null) {
				shippingRateCurrency = new ShippingRateCurrency();
				SiteCurrencyClass siteCurrencyClass = SiteCurrencyClassDAO.load(form.getSiteCurrencyClassId());
				shippingRateCurrency.setRecCreateBy(adminBean.getUser().getUserId());
				shippingRateCurrency.setRecCreateDatetime(new Date(System.currentTimeMillis()));
				shippingRateCurrency.setSiteCurrencyClass(siteCurrencyClass);
				shippingRateCurrency.setShippingRate(shippingRate);
				shippingRate.getShippingRateCurrencies().add(shippingRateCurrency);
			}
			shippingRate.setRecUpdateBy(adminBean.getUser().getUserId());
			shippingRate.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			
			if (form.isShippingRateFeeCurrFlag()) {
				shippingRateCurrency.setShippingRateFee(Format.getFloat(form.getShippingRateFeeCurr()));
			}
			else {
				shippingRateCurrency.setShippingRateFee(null);
			}
			if (form.isShippingAdditionalRateFeeCurrFlag()) {
				shippingRateCurrency.setShippingAdditionalRateFee(Format.getFloat(form.getShippingAdditionalRateFeeCurr()));
			}
			else {
				shippingRateCurrency.setShippingAdditionalRateFee(null);
			}
			shippingRateCurrency.setRecUpdateBy(adminBean.getUser().getUserId());
			shippingRateCurrency.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			if (shippingRate.getShippingRateId() == null) {
				em.persist(shippingRate);
			}
			if (shippingRateCurrency.getShippingRateCurrId() == null) {
				em.persist(shippingRateCurrency);
			}
			
			if (shippingRate.getShippingRateCurrency() != null && shippingRate.getShippingRateCurrency().getShippingRateFee() != null) {
				jsonResult.put("shippingRateFee", Format.getFloat(shippingRate.getShippingRateCurrency().getShippingRateFee()));
			}
			else {
				jsonResult.put("shippingRateFee", "");
			}
			if (shippingRate.getShippingRatePercentage() != null) {
				jsonResult.put("shippingRatePercentage", Format.getFloat(shippingRate.getShippingRatePercentage()));
			}
			else {
				jsonResult.put("shippingRatePercentage", "");
			}
			if (shippingRate.getShippingRateCurrency() != null && shippingRate.getShippingRateCurrency().getShippingAdditionalRateFee() != null) {
				jsonResult.put("shippingAdditionalRateFee", Format.getFloat(shippingRate.getShippingRateCurrency().getShippingAdditionalRateFee()));
			}
			else {
				jsonResult.put("shippingAdditionalRateFee", "");
			}
			if (shippingRate.getShippingAdditionalRatePercentage() != null) {
				jsonResult.put("shippingAdditionalRatePercentage", Format.getFloat(shippingRate.getShippingAdditionalRatePercentage()));
			}
			else {
				jsonResult.put("shippingAdditionalRatePercentage", "");
			}
			if (shippingRateCurrency.getShippingRateFee() != null) {
				jsonResult.put("shippingRateFeeCurr", Format.getFloat(shippingRateCurrency.getShippingRateFee()));
				jsonResult.put("shippingRateFeeCurrFlag", true);
			}
			else {
				jsonResult.put("shippingRateFeeCurr", "");
				jsonResult.put("shippingRateFeeCurrFlag", false);
			}
			if (shippingRateCurrency.getShippingAdditionalRateFee() != null) {
				jsonResult.put("shippingAdditionalRateFeeCurr", Format.getFloat(shippingRateCurrency.getShippingAdditionalRateFee()));
				jsonResult.put("shippingAdditionalRateFeeCurrFlag", true);
			}
			else {
				jsonResult.put("shippingAdditionalRateFeeCurr", "");
				jsonResult.put("shippingAdditionalRateFeeCurrFlag", false);
			}
		}
		
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		this.streamWebService(response, jsonResult.toHtmlString());
		return null;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("save", "save");
        map.put("edit", "edit");
        map.put("create", "create");
        map.put("remove", "remove"); 
        map.put("language", "language");
        map.put("translate", "translate");
        map.put("getJsonRate", "getJsonRate");
        map.put("updateJsonRate", "updateJsonRate");
        return map;
    }
}
