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

package com.jada.admin.contactus;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.ContactUsDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.ContactUs;
import com.jada.jpa.entity.ContactUsLanguage;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.jpa.entity.State;
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

public class ContactUsMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest request,
                             HttpServletResponse response)
        throws Throwable {

        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        ContactUsMaintActionForm form = (ContactUsMaintActionForm) actionForm;
		initSiteProfiles(form, site);
        ContactUs contactUs = new ContactUs();
        PropertyUtils.copyProperties(form, contactUs);
        form.setMode("C");
        form.setActive(String.valueOf(Constants.ACTIVE_YES));
        initSearchInfo(form, site.getSiteId());
        
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
		ContactUsMaintActionForm form = (ContactUsMaintActionForm) actionForm;
		if (form == null) {
			form = new ContactUsMaintActionForm();
		}
		
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
		
		ContactUs contactUs = em.find(ContactUs.class, Format.getLong(form.getContactUsId()));
	
	    BingTranslate translator = new BingTranslate(form.getFromLocale(), form.getToLocale(), site);
		form.setContactUsNameLangFlag(true);
		form.setContactUsDescLangFlag(true);
		form.setContactUsNameLang(translator.translate(contactUs.getContactUsLanguage().getContactUsName()));
		form.setContactUsDescLang(translator.translate(contactUs.getContactUsLanguage().getContactUsDesc()));
		
        initSearchInfo(form, site.getSiteId()); 
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
		ContactUsMaintActionForm form = (ContactUsMaintActionForm) actionForm;
		if (form == null) {
			form = new ContactUsMaintActionForm();
		}
		
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
		
		ContactUs contactUs = em.find(ContactUs.class, Format.getLong(form.getContactUsId()));
		
		form.setContactUsNameLangFlag(false);
		form.setContactUsDescLangFlag(false);
		form.setContactUsNameLang(contactUs.getContactUsLanguage().getContactUsName());
		form.setContactUsDescLang(contactUs.getContactUsLanguage().getContactUsDesc());
		if (!form.isSiteProfileClassDefault()) {
	    	Iterator<?> iterator = contactUs.getContactUsLanguages().iterator();
	    	Long siteProfileClassId = form.getSiteProfileClassId();
	    	while (iterator.hasNext()) {
	    		ContactUsLanguage contactUsLanguage = (ContactUsLanguage) iterator.next();
	    		if (contactUsLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
	    			if (contactUsLanguage.getContactUsName() != null) {
	    				form.setContactUsNameLangFlag(true);
	    				form.setContactUsNameLang(contactUsLanguage.getContactUsName());
	    			}
	    			if (contactUsLanguage.getContactUsDesc() != null) {
	    				form.setContactUsDescLangFlag(true);
	    				form.setContactUsDescLang(contactUsLanguage.getContactUsDesc());
	    			}
	    			break;
	    		}
	    	}
		}
        initSearchInfo(form, site.getSiteId()); 
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
        ContactUsMaintActionForm form = (ContactUsMaintActionForm) actionForm;
		initSiteProfiles(form, site);
        if (form == null) {
            form = new ContactUsMaintActionForm();
        }
		String contactUsId = request.getParameter("contactUsId");
        ContactUs contactUs = new ContactUs();
        contactUs = ContactUsDAO.load(site.getSiteId(), Format.getLong(contactUsId));
        
        form.setMode("U");
        copyProperties(form, contactUs);
        initSearchInfo(form, site.getSiteId());

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
		ContactUsMaintActionForm form = (ContactUsMaintActionForm) actionForm;
		ContactUs contactUs = ContactUsDAO.load(site.getSiteId(), Format.getLong(form.getContactUsId()));
		em.remove(contactUs);
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
		ContactUsMaintActionForm form = (ContactUsMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}

		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);

		ContactUs contactUs = new ContactUs();
		if (!insertMode) {
			contactUs = ContactUsDAO.load(site.getSiteId(), Format.getLong(form.getContactUsId()));
		}

		ActionMessages errors = validate(form, site.getSiteId());
		if (errors.size() != 0) {
			saveMessages(request, errors);
	        initSearchInfo(form, site.getSiteId());
			return mapping.findForward("error");
		}

		if (insertMode) {
			contactUs.setSite(site);
			contactUs.setRecCreateBy(adminBean.getUser().getUserId());
			contactUs.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		contactUs.setActive(new Character(Constants.ACTIVE_NO));
		if (form.getActive() != null && form.getActive().equals(String.valueOf(Constants.ACTIVE_YES))) {
			contactUs.setActive(new Character(Constants.ACTIVE_YES));
		}
		contactUs.setContactUsEmail(form.getContactUsEmail());
		contactUs.setContactUsPhone(form.getContactUsPhone());
		contactUs.setContactUsAddressLine1(form.getContactUsAddressLine1());
		contactUs.setContactUsAddressLine2(form.getContactUsAddressLine2());
		contactUs.setContactUsCityName(form.getContactUsCityName());
		contactUs.setContactUsStateCode(form.getContactUsStateCode());
		String stateName = Utility.getStateName(site.getSiteId(), form.getContactUsStateCode());
		if (stateName == null) {
			stateName = "";
		}
		contactUs.setContactUsStateName(stateName);
		contactUs.setContactUsCountryCode(form.getContactUsCountryCode());
		contactUs.setContactUsCountryName(Utility.getCountryName(site.getSiteId(), form.getContactUsCountryCode()));
		contactUs.setContactUsZipCode(form.getContactUsZipCode());
		if (!Format.isNullOrEmpty(form.getSeqNum()))  {
			contactUs.setSeqNum(Format.getIntObj(form.getSeqNum()));
		}
		else {
			contactUs.setSeqNum(new Integer(0));
		}
		contactUs.setRecUpdateBy(adminBean.getUser().getUserId());
		contactUs.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		
		if (form.isSiteProfileClassDefault()) {
			saveDefault(contactUs, form, adminBean);
		}
		else {
			saveLanguage(contactUs, form, adminBean);
		}
		
		if (insertMode) {
			em.persist(contactUs);
		}
		else {
			// em.update(contactUs);
		}
		form.setContactUsId(Format.getLong(contactUs.getContactUsId()));
		form.setMode("U");
        initSearchInfo(form, site.getSiteId());
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	private void saveDefault(ContactUs contactUs, ContactUsMaintActionForm form, AdminBean adminBean) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ContactUsLanguage contactUsLanguage = contactUs.getContactUsLanguage();
		if (contactUsLanguage == null) {
			contactUsLanguage = new ContactUsLanguage();
			contactUsLanguage.setContactUs(contactUs);
			contactUs.getContactUsLanguages().add(contactUsLanguage);
    		SiteProfileClass siteProfileClass = em.find(SiteProfileClass.class, form.getSiteProfileClassDefaultId());
			contactUsLanguage.setSiteProfileClass(siteProfileClass);
			contactUsLanguage.setRecCreateBy(adminBean.getUser().getUserId());
			contactUsLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			contactUs.setContactUsLanguage(contactUsLanguage);
		}
		contactUsLanguage.setContactUsName(form.getContactUsName());
		contactUsLanguage.setContactUsDesc(form.getContactUsDesc());
		contactUsLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
		contactUsLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(contactUsLanguage);
	}
	
	private void saveLanguage(ContactUs contactUs, ContactUsMaintActionForm form, AdminBean adminBean) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ContactUsLanguage contactUsLanguage = null;
		User user = adminBean.getUser();
    	Long siteProfileClassId = form.getSiteProfileClassId();
		boolean found = false;
		Iterator<?> iterator = contactUs.getContactUsLanguages().iterator();
		while (iterator.hasNext()) {
			contactUsLanguage = (ContactUsLanguage) iterator.next();
			if (contactUsLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
				found = true;
				break;
			}
		}
		if (!found) {
			contactUsLanguage = new ContactUsLanguage();
			contactUsLanguage.setRecCreateBy(user.getUserId());
			contactUsLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		SiteProfileClass siteProfileClass = em.find(SiteProfileClass.class, siteProfileClassId);
    		contactUsLanguage.setSiteProfileClass(siteProfileClass);
    		contactUs.getContactUsLanguages().add(contactUsLanguage);
		}
		if (form.isContactUsNameLangFlag()) {
			contactUsLanguage.setContactUsName(form.getContactUsNameLang());
		}
		else {
			contactUsLanguage.setContactUsName(null);
		}
		if (form.isContactUsDescLangFlag()) {
			contactUsLanguage.setContactUsDesc(form.getContactUsDescLang());
		}
		else {
			contactUsLanguage.setContactUsDesc(null);
		}
		contactUsLanguage.setRecUpdateBy(user.getUserId());
		contactUsLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(contactUsLanguage);
	}
	
	private void copyProperties(ContactUsMaintActionForm form, ContactUs contactUs) {
		form.setContactUsId(Format.getLong(contactUs.getContactUsId()));
		form.setContactUsName(contactUs.getContactUsLanguage().getContactUsName());
		form.setContactUsEmail(contactUs.getContactUsEmail());
		form.setContactUsAddressLine1(contactUs.getContactUsAddressLine1());
		form.setContactUsAddressLine2(contactUs.getContactUsAddressLine2());
		form.setContactUsCityName(contactUs.getContactUsCityName());
		form.setContactUsStateCode(contactUs.getContactUsStateCode());
		form.setContactUsCountryCode(contactUs.getContactUsCountryCode());
		form.setContactUsZipCode(contactUs.getContactUsZipCode());
		form.setContactUsPhone(contactUs.getContactUsPhone());
		form.setContactUsDesc(contactUs.getContactUsLanguage().getContactUsDesc());
		form.setActive(String.valueOf(contactUs.getActive()));
		if (!form.isSiteProfileClassDefault()) {
			form.setContactUsNameLangFlag(false);
			form.setContactUsNameLang(contactUs.getContactUsLanguage().getContactUsName());
			form.setContactUsDescLangFlag(false);
			form.setContactUsDescLang(contactUs.getContactUsLanguage().getContactUsDesc());
	    	ContactUsLanguage contactUsLanguage = null;
	    	Long siteProfileClassId = form.getSiteProfileClassId();
	    	boolean found = false;
	    	Iterator<?> iterator = contactUs.getContactUsLanguages().iterator();
	    	while (iterator.hasNext()) {
	    		contactUsLanguage = (ContactUsLanguage) iterator.next();
	    		if (contactUsLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
	    			found = true;
	    			break;
	    		}
	    	}
	    	if (found) {
	    		if (contactUsLanguage.getContactUsName() != null) {
	    			form.setContactUsNameLang(form.getContactUsName());
	    			form.setContactUsNameLangFlag(true);
	    		}
	    		if (contactUsLanguage.getContactUsDesc() != null) {
	    			form.setContactUsDescLang(form.getContactUsDesc());
	    			form.setContactUsDescLangFlag(true);
	    		}
	    	}
		}
	}
	
    public void initSearchInfo(ContactUsMaintActionForm form, String siteId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from country in class Country where country.siteId = :siteId order by countryName");
    	query.setParameter("siteId", siteId);
     	Iterator<?> iterator = query.getResultList().iterator();
     	Vector<LabelValueBean> vector = new Vector<LabelValueBean>();
     	while (iterator.hasNext()) {
     		Country country = (Country) iterator.next();
     		LabelValueBean bean = new LabelValueBean(country.getCountryName(), country.getCountryCode());
     		vector.add(bean);
     	}
     	LabelValueBean countries[] = new LabelValueBean[vector.size()];
     	vector.copyInto(countries);
     	form.setCountries(countries);

     	String sql = "";
     	sql = "from		State state " +
              "left	join fetch state.country country " +
              "where	country.siteId = :siteId " +
              "order	by country.countryId, state.stateName";
     	query = em.createQuery(sql);
    	query.setParameter("siteId", siteId);
     	iterator = query.getResultList().iterator();
     	vector = new Vector<LabelValueBean>();
     	vector.add(new LabelValueBean("", ""));
     	while (iterator.hasNext()) {
     		State state = (State) iterator.next();
     		LabelValueBean bean = new LabelValueBean(state.getStateName(), state.getStateCode());
     		vector.add(bean);
     	}
     	LabelValueBean states[] = new LabelValueBean[vector.size()];
     	vector.copyInto(states);
     	form.setStates(states);
    }
    
    public ActionMessages validate(ContactUsMaintActionForm form, String siteId) throws Exception { 
    	ActionMessages errors = new ActionMessages();
    	
    	if (Format.isNullOrEmpty(form.getContactUsName())) {
    		errors.add("contactUsName", new ActionMessage("error.string.required"));
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
        return map;
    }
}
