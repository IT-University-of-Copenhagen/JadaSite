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

package com.jada.admin.site;

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

import com.jada.search.Indexer;
import com.jada.util.JSONEscapeObject;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.SiteDAO;
import com.jada.dao.SiteDomainDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomain;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;
import com.jada.xml.site.SiteParamBean;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

public class SiteMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest request,
                             HttpServletResponse response)
        throws Throwable {

		SiteMaintActionForm form = (SiteMaintActionForm) actionForm;
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
        form.setSiteId("");
        form.setActive(true);
        form.setShareInventory(false);
        form.setManageInventory(true);
        form.setSingleCheckout(true);
        form.setStoreCreditCard(true);
        form.setListingPageSize(String.valueOf(Constants.DEFAULT_LISTING_PAGE_SIZE));
        form.setMode("C");
        form.setSystemRecord(String.valueOf(Constants.VALUE_NO));
        initListInfo(form);
        FormUtils.setFormDisplayMode(request, form, FormUtils.CREATE_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
 
    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

        SiteMaintActionForm form = (SiteMaintActionForm) actionForm;
        Site site = SiteDAO.load(form.getSiteId());
        form.setMode("U");
        copyProperties(form, site);
        initListInfo(form);
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
		SiteMaintActionForm form = (SiteMaintActionForm) actionForm;
        Site site = SiteDAO.load(form.getSiteId());

		try {
			em.remove(site);
			em.getTransaction().commit();
		}
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.site.constraint"));
				saveMessages(request, errors);
				return actionMapping.findForward("error");
			}
			throw e;
		}
		
        initListInfo(form);
		ActionForward actionForward = actionMapping.findForward("removeSuccess");
		return actionForward;
	}
    
    public ActionForward removeSiteDomains(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		SiteMaintActionForm form = (SiteMaintActionForm) actionForm;
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);

		try {
			String siteDomainIds[] = form.getSiteDomainIds();
			for (int i = 0; i < siteDomainIds.length; i++) {
				Long siteDomainId = Format.getLong(siteDomainIds[i]);
				SiteDomainDAO.remove(siteDomainId);
			}
			em.getTransaction().commit();
		}
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				MessageResources resources = this.getResources(request);
				jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
				jsonResult.put("message", resources.getMessage("error.remove.site.constraint"));
			}
			throw e;
		}
		
		streamWebService(response, jsonResult.toHtmlString());
		return null;
	}
    
	public ActionForward listSiteDomains(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
			throws Throwable {
		AdminBean adminBean = getAdminBean(request);
		SiteMaintActionForm form = (SiteMaintActionForm) actionForm;
        Site site = SiteDAO.load(form.getSiteId());
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
    	Vector<JSONEscapeObject> siteDomains = new Vector<JSONEscapeObject>();
    	SiteDomain siteDomainDefault = site.getSiteDomainDefault();
		JSONEscapeObject object = new JSONEscapeObject();
		Long siteDomainDefaultId = siteDomainDefault.getSiteDomainId();
		object.put("siteDomainId", siteDomainDefault.getSiteDomainId());
		object.put("siteName", siteDomainDefault.getSiteDomainLanguage().getSiteName());
		object.put("siteDomainPrefix", siteDomainDefault.getSiteDomainPrefix());
		object.put("sitePublicPortNum", siteDomainDefault.getSitePublicPortNum());
		object.put("siteDomainPrefix", siteDomainDefault.getSiteDomainPrefix());
		object.put("siteDomainUrl", Utility.formatDomainURL(siteDomainDefault, adminBean.getContextPath()));
		object.put("master", true);
		object.put("active", siteDomainDefault.getActive());
		siteDomains.add(object);

    	java.util.Iterator<SiteDomain> iterator = site.getSiteDomains().iterator();
    	while (iterator.hasNext()) {
    		SiteDomain siteDomain = (SiteDomain) iterator.next();
    		if (siteDomain.getSiteDomainId().equals(siteDomainDefaultId)) {
    			continue;
    		}
    		object = new JSONEscapeObject();
    		object.put("siteDomainId", siteDomain.getSiteDomainId());
    		object.put("siteName", siteDomain.getSiteDomainLanguage().getSiteName());
    		object.put("siteDomainPrefix", siteDomain.getSiteDomainPrefix());
    		object.put("sitePublicPortNum", siteDomain.getSitePublicPortNum());
    		object.put("siteDomainPrefix", siteDomain.getSiteDomainPrefix());
    		object.put("siteDomainUrl", Utility.formatDomainURL(siteDomain, adminBean.getContextPath()));
    		object.put("master", false);
    		object.put("active", siteDomain.getActive());
    		siteDomains.add(object);
    	}
    	jsonResult.put("siteDomains", siteDomains);
    	String result = jsonResult.toHtmlString();
		streamWebService(response, result);
		return null;
	}

	public ActionForward save(ActionMapping mapping, 
							  ActionForm actionForm,
							  HttpServletRequest request,
							  HttpServletResponse response) 
		throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		boolean insertMode = false;
		SiteMaintActionForm form = (SiteMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}

		AdminBean adminBean = getAdminBean(request);

		Site site = new Site();
		if (!insertMode) {
	        site = SiteDAO.load(form.getSiteId());
		}
		SiteParamBean siteParamBean = new SiteParamBean();
		if (!Format.isNullOrEmpty(site.getSiteParam())) {
			siteParamBean = (SiteParamBean) Utility.joxUnMarshall(SiteParamBean.class, site.getSiteParam());
		}

		form.setMailSmtpPort(form.getMailSmtpPort().trim());
		ActionMessages errors = validate(form, siteParamBean);
		if (errors.size() != 0) {
			form.setMailSmtpPassword("");
			form.setCaptchaPrivateKey("");
			form.setCaptchaPublicKey("");
			form.setBingClientSecert("");
	        initListInfo(form);
			saveMessages(request, errors);
			return mapping.findForward("error");
		}

		site.setSiteDesc(form.getSiteDesc());
		if (!insertMode) {
			site.setShareInventory(form.isShareInventory() ? Constants.VALUE_YES : Constants.VALUE_NO);
			site.setManageInventory(form.isManageInventory() ? Constants.VALUE_YES : Constants.VALUE_NO);
			site.setSingleCheckout(form.isSingleCheckout() ? Constants.VALUE_YES : Constants.VALUE_NO);
			site.setListingPageSize(Format.getInt(form.getListingPageSize()));
			site.setMailSmtpHost(form.getMailSmtpHost());
			site.setMailSmtpPort(form.getMailSmtpPort());
			site.setMailSmtpAccount(form.getMailSmtpAccount());
			if (!Format.isNullOrEmpty(form.getMailSmtpPassword())) {
				site.setMailSmtpPassword(AESEncoder.getInstance().encode(form.getMailSmtpPassword()));
			}
			if (form.getMailSmtpPassword().equals(" ")) {
				site.setMailSmtpPassword(AESEncoder.getInstance().encode(""));
			}
		}
		site.setActive(form.isActive() ? Constants.VALUE_YES : Constants.VALUE_NO);
		siteParamBean.setEnableCaptcha(form.isEnableCaptcha() ? String.valueOf(Constants.VALUE_YES) : String.valueOf(Constants.VALUE_NO));
		if (!Format.isNullOrEmpty(form.getCaptchaPrivateKey())) {
			siteParamBean.setCaptchaPrivateKey(AESEncoder.getInstance().encode(form.getCaptchaPrivateKey()));
		}
		if (!Format.isNullOrEmpty(form.getCaptchaPublicKey())) {
			siteParamBean.setCaptchaPublicKey(AESEncoder.getInstance().encode(form.getCaptchaPublicKey()));
		}
		if (!form.isEnableCaptcha()) {
			siteParamBean.setCaptchaPrivateKey("");
			siteParamBean.setCaptchaPublicKey("");
		}
		siteParamBean.setBingClientId(form.getBingClientId());
		if (!Format.isNullOrEmpty(form.getBingClientSecert())) {
			siteParamBean.setBingClientSecert(form.getBingClientSecert());
		}
		siteParamBean.setStoreCreditCard(form.isStoreCreditCard() ? String.valueOf(Constants.VALUE_YES) : String.valueOf(Constants.VALUE_NO));
		site.setSiteParam(Utility.joxMarshall("SiteParamBean", siteParamBean));
		site.setRecUpdateBy(adminBean.getUser().getUserId());
		site.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		if (insertMode) {
			boolean checked = false;
			String siteDomainPrefix = "localhost";
			int count = 0;
			while (!checked) {
				String sql = "from  Site";
				Query query = em.createQuery(sql);
				Iterator<?> iterator = query.getResultList().iterator();
				boolean found = false;
				if (count > 0) {
					siteDomainPrefix = "localhost" + count;
				}
				while (iterator.hasNext()) {
					Site otherSite = (Site) iterator.next();
					for (SiteDomain otherSiteDomain : otherSite.getSiteDomains()) {
						if (otherSiteDomain.getSiteDomainPrefix().equals(siteDomainPrefix)) {
							found = true;
							break;
						}
					}
				}
				if (!found) {
					checked = true;
				}
				count++;
			}
			
			site.setSiteId(form.getSiteId());
			site.setListingPageSize(Constants.DEFAULT_LISTING_PAGE_SIZE);
			site.setMailSmtpHost(form.getMailSmtpHost());
			site.setMailSmtpPort(form.getMailSmtpPort());
			site.setMailSmtpAccount(form.getMailSmtpAccount());
			site.setMailSmtpPassword("");
			site.setShareInventory(Constants.VALUE_YES);
			site.setManageInventory(Constants.VALUE_YES);
			site.setSingleCheckout(Constants.VALUE_YES);
			site.setSystemRecord(Constants.VALUE_NO);
			site.setRecCreateBy(adminBean.getUser().getUserId());
			site.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			em.persist(site);
			SiteDAO.add(site, adminBean.getUserId(), request);
		}
		else {
			// em.update(site);
		}
		Indexer.getInstance(site.getSiteId()).remove();
		form.setMailSmtpPassword("");
		form.setCaptchaPrivateKey("");
		form.setCaptchaPublicKey("");
		form.setBingClientSecert("");
		form.setMode("U");
		form.setSiteId(site.getSiteId());
        initListInfo(form);
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	private void initListInfo(SiteMaintActionForm form) throws Exception {
	}
	
	private void copyProperties(SiteMaintActionForm form, Site site) throws Exception {
		form.setSiteId(site.getSiteId());
		form.setSiteDesc(site.getSiteDesc());
		form.setShareInventory(false);
		form.setManageInventory(false);
		form.setSingleCheckout(false);
		form.setSystemRecord(String.valueOf(site.getSystemRecord()));
		form.setActive(false);
		if (site.getShareInventory() == Constants.VALUE_YES) {
			form.setShareInventory(true);
		}
		if (site.getManageInventory() == Constants.VALUE_YES) {
			form.setManageInventory(true);
		}
		if (site.getSingleCheckout() == Constants.VALUE_YES) {
			form.setSingleCheckout(true);
		}
	
		if (site.getActive() == Constants.VALUE_YES) {
			form.setActive(true);
		}
		form.setListingPageSize(site.getListingPageSize().toString());
		form.setMailSmtpHost(site.getMailSmtpHost());
		form.setMailSmtpPort(site.getMailSmtpPort());
		form.setMailSmtpAccount(site.getMailSmtpAccount());
		form.setMailSmtpPassword("");
		//form.setMailSmtpPassword(AESEncoder.getInstance().decode(site.getMailSmtpPassword()));
		Vector<SiteDomainDisplayForm> vector = new Vector<SiteDomainDisplayForm>();
		for (SiteDomain siteDomain : site.getSiteDomains()) {
			SiteDomainDisplayForm domainForm = new SiteDomainDisplayForm();
			domainForm.setRemove(false);
			domainForm.setSiteName(siteDomain.getSiteDomainLanguage().getSiteName());
			domainForm.setSiteDomainPrefix(siteDomain.getSiteDomainPrefix());
			domainForm.setSitePublicPortNum(siteDomain.getSitePublicPortNum());
			vector.add(domainForm);
		}
		SiteDomainDisplayForm siteDomains[] = new SiteDomainDisplayForm[vector.size()];
		vector.copyInto(siteDomains);
		form.setSiteDomains(siteDomains);
		
		form.setEnableCaptcha(false);
		form.setCaptchaPrivateKey("");
		form.setCaptchaPublicKey("");
		form.setStoreCreditCard(true);
		if (!Format.isNullOrEmpty(site.getSiteParam())) {
			SiteParamBean siteParamBean = (SiteParamBean) Utility.joxUnMarshall(SiteParamBean.class, site.getSiteParam());
			if (siteParamBean.getEnableCaptcha().equals(String.valueOf(Constants.VALUE_YES))) {
				form.setEnableCaptcha(true);
			}
			if (siteParamBean.getStoreCreditCard() != null) {
				if (siteParamBean.getStoreCreditCard().equals(String.valueOf(Constants.VALUE_NO))) {
					form.setStoreCreditCard(false);
				}
			}
			form.setBingClientId(siteParamBean.getBingClientId());
		}
	}

    public ActionMessages validate(SiteMaintActionForm form, SiteParamBean siteParamBean) { 
    	ActionMessages errors = new ActionMessages();
    	
    	if (Format.isNullOrEmpty(form.getSiteId())) {
    		errors.add("siteId", new ActionMessage("error.string.required"));
    	}
    	else {
    		if (!form.getSiteId().matches("[0-9A-Za-z_\\-]+")) {
    			errors.add("siteId", new ActionMessage("error.siteId.invalidCharacter"));
    		}
    	}
    	if (Format.isNullOrEmpty(form.getSiteDesc())) {
    		errors.add("siteDesc", new ActionMessage("error.string.required"));
    	}
    	if (!Format.isInt(form.getListingPageSize())) {
    		errors.add("listingPageSize", new ActionMessage("error.int.invalid"));
    	}
    	else {
    		int listingPageSize = Format.getInt(form.getListingPageSize());
    		if (listingPageSize < 1) {
    			errors.add("listingPageSize", new ActionMessage("error.int.invalid"));
    		}
    	}
    	if (form.isEnableCaptcha()) {
    		if (Format.isNullOrEmpty(siteParamBean.getCaptchaPrivateKey())) {
	    	   	if (Format.isNullOrEmpty(form.getCaptchaPrivateKey())) {
	        		errors.add("captchaPrivateKey", new ActionMessage("error.string.required"));
	        	}
    		}
    		if (Format.isNullOrEmpty(siteParamBean.getCaptchaPublicKey())) {
	    	   	if (Format.isNullOrEmpty(form.getCaptchaPublicKey())) {
	        		errors.add("captchaPublicKey", new ActionMessage("error.string.required"));
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
        map.put("removeSiteDomains", "removeSiteDomains");
        map.put("listSiteDomains", "listSiteDomains");
        return map;
    }
}
