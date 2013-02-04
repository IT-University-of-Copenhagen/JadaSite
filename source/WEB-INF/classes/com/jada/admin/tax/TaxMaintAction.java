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

package com.jada.admin.tax;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.CountryDAO;
import com.jada.dao.StateDAO;
import com.jada.dao.TaxDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.jpa.entity.State;
import com.jada.jpa.entity.Tax;
import com.jada.jpa.entity.TaxLanguage;
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

public class TaxMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest request,
                             HttpServletResponse response)
        throws Throwable {

        TaxMaintActionForm form = (TaxMaintActionForm) actionForm;
        if (form == null) {
            form = new TaxMaintActionForm();
        }
        Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
        form.setPublished(String.valueOf(Constants.PUBLISHED_YES));
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
		TaxMaintActionForm form = (TaxMaintActionForm) actionForm;
		if (form == null) {
			form = new TaxMaintActionForm();
		}
		
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
		Tax tax = (Tax) em.find(Tax.class, Format.getLong(form.getTaxId()));
		
        BingTranslate translator = new BingTranslate(form.getFromLocale(), form.getToLocale(), site);
        
        form.setTaxCodeLangFlag(true);
        form.setTaxNameLangFlag(true);
        form.setTaxCodeLang(translator.translate(tax.getTaxLanguage().getTaxCode()));
        form.setTaxNameLang(translator.translate(tax.getTaxLanguage().getTaxName()));
        
        initListInfo(form, tax);
        initSearchInfo(form, request);
		
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
		TaxMaintActionForm form = (TaxMaintActionForm) actionForm;
		if (form == null) {
			form = new TaxMaintActionForm();
		}
		
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
		Tax tax = (Tax) em.find(Tax.class, Format.getLong(form.getTaxId()));
		if (!form.isSiteProfileClassDefault()) {
			form.setTaxCodeLang(tax.getTaxLanguage().getTaxCode());
			form.setTaxNameLang(tax.getTaxLanguage().getTaxName());
	    	boolean found = false;
	    	Long siteProfileClassId = form.getSiteProfileClassId();
	    	Iterator<?> iterator = tax.getTaxLanguages().iterator();
	    	TaxLanguage taxLanguage = null;
	    	while (iterator.hasNext()) {
	    		taxLanguage = (TaxLanguage) iterator.next();
	    		if (taxLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
	    			found = true;
	    			break;
	    		}
	    	}
	    	if (found) {
	    		if (taxLanguage.getTaxCode() != null) {
		    		form.setTaxCodeLangFlag(true);
		    		form.setTaxCodeLang(taxLanguage.getTaxCode());
	    		}
	    		if (taxLanguage.getTaxName() != null) {
		    		form.setTaxNameLangFlag(true);
		    		form.setTaxNameLang(taxLanguage.getTaxName());
	    		}
	    	}
		}
        initListInfo(form, tax);
        initSearchInfo(form, request);
		
		FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}

    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

        TaxMaintActionForm form = (TaxMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		initSiteProfiles(form, adminBean.getSite());
        if (form == null) {
            form = new TaxMaintActionForm();
        }
		String taxId = request.getParameter("taxId");
        Tax tax = TaxDAO.load(adminBean.getSite().getSiteId(), Format.getLong(taxId));
        form.setMode("U");
		form.setTaxId(Format.getLong(tax.getTaxId()));
		form.setTaxCode(tax.getTaxLanguage().getTaxCode());
		form.setTaxName(tax.getTaxLanguage().getTaxName());
		form.setTaxRate(Format.getFloatObj4(tax.getTaxRate()));
		form.setPublished(String.valueOf(tax.getPublished()));
		if (!form.isSiteProfileClassDefault()) {
			form.setTaxNameLang(form.getTaxName());
	    	boolean found = false;
	    	Long siteProfileClassId = form.getSiteProfileClassId();
	    	Iterator<?> iterator = tax.getTaxLanguages().iterator();
	    	TaxLanguage taxLanguage = null;
	    	while (iterator.hasNext()) {
	    		taxLanguage = (TaxLanguage) iterator.next();
	    		if (taxLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
	    			found = true;
	    			break;
	    		}
	    	}
	    	if (found) {
	    		if (taxLanguage.getTaxCode() != null) {
		    		form.setTaxCodeLangFlag(true);
	    			form.setTaxCodeLang(taxLanguage.getTaxCode());
	    		}
	    		if (taxLanguage.getTaxName() != null) {
		    		form.setTaxNameLangFlag(true);
		    		form.setTaxNameLang(taxLanguage.getTaxName());
	    		}
	    	}
		}
        
        initListInfo(form, tax);
        initSearchInfo(form, request);

        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    private boolean isRootExist(Vector<?> vector, String value) {
    	Iterator<?> iterator = vector.iterator();
    	while (iterator.hasNext()) {
    		CountryStateTableBean bean = (CountryStateTableBean) iterator.next();
    		if (bean.getType().equals("R")) {
    			if (bean.getValue().equals(value)) {
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		TaxMaintActionForm form = (TaxMaintActionForm) actionForm;
		Tax tax = TaxDAO.load(getAdminBean(request).getSite().getSiteId(), Format.getLong(form.getTaxId()));
		em.remove(tax);
		ActionForward actionForward = actionMapping.findForward("removeSuccess");
		return actionForward;
	}

	public ActionForward save(ActionMapping mapping, 
							  ActionForm actionForm,
							  HttpServletRequest request,
							  HttpServletResponse response) 
		throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		boolean insertMode = false;
		TaxMaintActionForm form = (TaxMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}

		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);

		Tax tax = new Tax();
		if (!insertMode) {
			tax = TaxDAO.load(site.getSiteId(), Format.getLong(form.getTaxId()));
		}

		ActionMessages errors = validate(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
	        initListInfo(form, tax);
	        initSearchInfo(form, request);
	        if (insertMode) {
	            FormUtils.setFormDisplayMode(request, form, FormUtils.CREATE_MODE);
	        }
	        else {
	            FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
	        }
			return mapping.findForward("error");
		}

		if (insertMode) {
			tax.setRecCreateBy(adminBean.getUser().getUserId());
			tax.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			tax.setSite(site);
		}
		
		if (form.isSiteProfileClassDefault()) {
			saveDefault(tax, form, adminBean);
			if (insertMode) {
				em.persist(tax);
			}
		}
		else {
			saveLanguage(tax, form, adminBean);			
		}
        form.setTaxId(Format.getLong(tax.getTaxId()));
		form.setMode("U");
        
        initListInfo(form, tax);
        initSearchInfo(form, request);
        
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	public void saveDefault(Tax tax, TaxMaintActionForm form, AdminBean adminBean) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		TaxLanguage taxLanguage = tax.getTaxLanguage();
		if (taxLanguage == null) {
			taxLanguage = new TaxLanguage();
			taxLanguage.setTax(tax);
			tax.getTaxLanguages().add(taxLanguage);
    		SiteProfileClass siteProfileClass = (SiteProfileClass) em.find(SiteProfileClass.class, form.getSiteProfileClassDefaultId());
    		taxLanguage.setSiteProfileClass(siteProfileClass);
    		taxLanguage.setRecCreateBy(adminBean.getUser().getUserId());
    		taxLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		tax.setTaxLanguage(taxLanguage);
		}
		taxLanguage.setTaxCode(form.getTaxCode());
		taxLanguage.setTaxName(form.getTaxName());
		taxLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
		taxLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(taxLanguage);
		
		tax.setTaxRate(Format.getFloatObj(form.getTaxRate()));
		tax.setPublished(Constants.PUBLISHED_NO);
		if (form.getPublished() != null && form.getPublished().equals(String.valueOf(Constants.PUBLISHED_YES))) {
			tax.setPublished(Constants.PUBLISHED_YES);
		}
		tax.setRecUpdateBy(adminBean.getUser().getUserId());
		tax.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
	}
	
	public void saveLanguage(Tax tax, TaxMaintActionForm form, AdminBean adminBean) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Long siteProfileClassId = form.getSiteProfileClassId();
    	User user = adminBean.getUser();
    	Iterator<?> iterator = tax.getTaxLanguages().iterator();
    	boolean found = false;
    	TaxLanguage taxLanguage = null;
    	while (iterator.hasNext()) {
    		taxLanguage = (TaxLanguage) iterator.next();
    		if (taxLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
    			found = true;
    			break;
    		}
    	}
    	if (!found) {
    		taxLanguage = new TaxLanguage();
    		taxLanguage.setRecCreateBy(user.getUserId());
    		taxLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		SiteProfileClass siteProfileClass = (SiteProfileClass) em.find(SiteProfileClass.class, siteProfileClassId);
    		taxLanguage.setSiteProfileClass(siteProfileClass);
    		taxLanguage.setTax(tax);
    	}
    	if (form.isTaxCodeLangFlag()) {
    		taxLanguage.setTaxCode(form.getTaxCodeLang());
    	}
    	else {
    		taxLanguage.setTaxCode(null);
    	}
    	if (form.isTaxNameLangFlag()) {
    		taxLanguage.setTaxName(form.getTaxNameLang());
    	}
    	else {
    		taxLanguage.setTaxName(null);
    	}

    	taxLanguage.setRecUpdateBy(user.getUserId());
    	taxLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    	em.persist(taxLanguage);
	}
	
    public ActionForward addCountryState(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        TaxMaintActionForm form = (TaxMaintActionForm) actionForm;
		initSiteProfiles(form, getAdminBean(request).getSite());
		Site site = getAdminBean(request).getSite();
   
        Long taxId = Format.getLong(form.getTaxId());
        Tax tax = TaxDAO.load(site.getSiteId(), taxId);

        String countries[] = form.getCountries();
        if (countries != null) {
	        for (int i = 0; i < countries.length; i++) {
	            Country country = CountryDAO.load(site.getSiteId(), Format.getLong(countries[i]));
	        	tax.getCountries().add(country);
	        }
        }

        String states[] = form.getStates();
        if (states != null) {
	        for (int i = 0; i < states.length; i++) {
	            State state = StateDAO.load(site.getSiteId(), Format.getLong(states[i]));
	        	tax.getStates().add(state);
	        }
        }
        em.flush();
        initListInfo(form, tax);
        initSearchInfo(form, request);

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward removeCountryState(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
        TaxMaintActionForm form = (TaxMaintActionForm) actionForm;
		initSiteProfiles(form, getAdminBean(request).getSite());
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        Site site = getAdminBean(request).getSite();
        
        Long taxId = Format.getLong(form.getTaxId());
        Tax tax = TaxDAO.load(site.getSiteId(), taxId);

        String removeCountries[] = form.getRemoveCountries();
        if (removeCountries != null) {
	        for (int i = 0; i < removeCountries.length; i++) {
	            Country country = CountryDAO.load(site.getSiteId(), Format.getLong(removeCountries[i]));
	        	tax.getCountries().remove(country);
	        }
        }

        String removeStates[] = form.getRemoveStates();
        if (removeStates != null) {
	        for (int i = 0; i < removeStates.length; i++) {
	            State state = StateDAO.load(site.getSiteId(), Format.getLong(removeStates[i]));
	        	tax.getStates().remove(state);
	        }
        }
        em.flush();
        
        initListInfo(form, tax);
        initSearchInfo(form, request);

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public void initSearchInfo(TaxMaintActionForm form, HttpServletRequest request) throws Exception {
        AdminBean adminBean = getAdminBean(request);
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        Query query = em.createQuery("from Country country where siteId = :siteId order by countryName");
		query.setParameter("siteId", adminBean.getSite().getSiteId());
		Iterator<?> iterator = query.getResultList().iterator();
		Vector<CountryStateTableBean> vector = new Vector<CountryStateTableBean>();
		while (iterator.hasNext()) {
			Country country = (Country) iterator.next();
			String prefix = "";
			if (country.getCountryName().length() > 0) {
				prefix = country.getCountryName().substring(0,1).toUpperCase();
			}
			if (!isRootExist(vector, prefix)) {
				CountryStateTableBean bean = new CountryStateTableBean();
				bean.setType("R");
				bean.setLabel(prefix);
				bean.setValue(prefix);
				bean.setParentValue("root");
				vector.add(bean);
			}
			
			CountryStateTableBean bean = new CountryStateTableBean();
			bean.setType("C");
			bean.setLabel(country.getCountryName());
			bean.setValue(country.getCountryId().toString());
			bean.setParentValue(prefix);
			vector.add(bean);
			
			Iterator<?> stateIterator = country.getStates().iterator();
			while (stateIterator.hasNext()) {
				State state = (State) stateIterator.next();
				bean = new CountryStateTableBean();
				bean.setType("S");
				bean.setLabel(state.getStateName());
				bean.setValue(state.getStateId().toString());
				bean.setParentValue(country.getCountryId().toString());
				vector.add(bean);
			}
		}
		CountryStateTableBean countryStateTable[] = new CountryStateTableBean[vector.size()];
        vector.copyInto(countryStateTable);
        form.setCountryStateTable(countryStateTable);
    }
    
    public void initListInfo(TaxMaintActionForm form, Tax tax) throws Exception {
    	form.setRemoveCountries(null);
    	form.setRemoveStates(null);
    	
        Iterator<?> iterator = tax.getCountries().iterator();
        Vector<CountryStateTableBean> vector = new Vector<CountryStateTableBean>();
        while (iterator.hasNext()) {
        	Country country = (Country) iterator.next();
        	CountryStateTableBean bean = new CountryStateTableBean();
        	bean.setType("C");
        	bean.setLabel(country.getCountryName());
        	bean.setValue(country.getCountryId().toString());
         	vector.add(bean);

        }
 
        iterator = tax.getStates().iterator();
        while (iterator.hasNext()) {
        	State state = (State) iterator.next();
        	CountryStateTableBean bean = new CountryStateTableBean();
        	bean.setType("S");
        	bean.setLabel(state.getCountry().getCountryName() + " - " + state.getStateName());
        	bean.setValue(state.getStateId().toString());
        	vector.add(bean);
        }
        
        CountryStateTableBean regionCountryStateTable[] = new CountryStateTableBean[vector.size()];
        vector.copyInto(regionCountryStateTable);
        form.setRegionCountryStateTable(regionCountryStateTable);
    }

    public ActionMessages validate(TaxMaintActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	
    	if (Format.isNullOrEmpty(form.getTaxCode())) {
    		errors.add("taxCode", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getTaxName())) {
    		errors.add("taxName", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getTaxRate())) {
    		errors.add("taxRate", new ActionMessage("error.string.required"));
    	}
    	else {
        	if (!Format.isFloat(form.getTaxRate())) {
        		errors.add("taxRate", new ActionMessage("error.float.invalid"));
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
        map.put("addCountryState", "addCountryState");
        map.put("removeCountryState", "removeCountryState");
        map.put("language", "language");
        map.put("translate", "translate");
        return map;
    }
}
