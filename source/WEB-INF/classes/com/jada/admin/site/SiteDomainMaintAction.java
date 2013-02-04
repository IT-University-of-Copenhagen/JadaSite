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

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import com.jada.util.JSONEscapeObject;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.CacheDAO;
import com.jada.dao.SiteCurrencyClassDAO;
import com.jada.dao.SiteDAO;
import com.jada.dao.SiteDomainDAO;
import com.jada.dao.TemplateDAO;
import com.jada.xml.site.SiteDomainParamBean;
import com.jada.dao.SiteProfileClassDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.CustomerClass;
import com.jada.jpa.entity.PaymentGateway;
import com.jada.jpa.entity.ShippingType;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteCurrency;
import com.jada.jpa.entity.SiteCurrencyClass;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.SiteDomainLanguage;
import com.jada.jpa.entity.SiteProfile;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.jpa.entity.State;
import com.jada.jpa.entity.Template;
import com.jada.jpa.entity.User;
import com.jada.order.payment.gateway.PayPalEngine;
import com.jada.order.payment.gateway.PayPalWebsitePaymentProHostedEngine;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.translator.BingTranslate;
import com.jada.util.ImageScaler;
import com.jada.util.Utility;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

public class SiteDomainMaintAction
    extends AdminLookupDispatchAction {
	
	static String SITEPARAM_LANG_PATTERN = "\\.lang\\..*";
	static String TABINDEX_SITE = "0";
	static String TABINDEX_GENERAL = "1";
	static String TABINDEX_SITELOGO = "2";
	static String TABINDEX_MAIL = "3";
	static String TABINDEX_BUSINESS = "4";
	static String TABINDEX_CHECKOUT = "5";
	static String TABINDEX_TEMPLATE = "6";
    Logger logger = Logger.getLogger(SiteDomainMaintAction.class);

    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest request,
                             HttpServletResponse response)
        throws Throwable {

    	Site site = getAdminBean(request).getSite();
        SiteDomainMaintActionForm form = (SiteDomainMaintActionForm) actionForm;
        initSiteProfiles(form, site);
        form.setSiteDomainId("");
        form.setSiteDomainUrl("");
        form.setSiteName("");
        form.setSiteDomainName("");
        form.setSiteDomainPrefix("");
        form.setSitePublicPortNum("");
        form.setSiteSecurePortNum("");
        form.setActive(false);
        form.setMode("C");
        form.setSiteProfileClassDefault(true);
        
		initListInfo(form);
        FormUtils.setFormDisplayMode(request, form, FormUtils.CREATE_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
 
    public ActionForward translate(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
		
		SiteDomainMaintActionForm form = (SiteDomainMaintActionForm) actionForm;
		if (form == null) {
			form = new SiteDomainMaintActionForm();
		}
		String siteId = form.getSiteId();
		Site site = SiteDAO.load(siteId);
		String siteDomainId = form.getSiteDomainId();
		SiteDomain siteDomain = SiteDomainDAO.load(Format.getLong(siteDomainId));
		form.setMode("U");
		initSiteProfiles(form, site);
		copyProperties(form, siteDomain, getAdminBean(request));
		
        BingTranslate translator = new BingTranslate(form.getFromLocale(), form.getToLocale(), site);
        form.setSiteNameLangFlag(true);
        form.setTemplateFooterLangFlag(true);
        form.setSubjectPwdResetLangFlag(true);
        form.setSubjectCustSalesLangFlag(true);
        form.setSubjectShippingQuoteLangFlag(true);
        form.setSubjectNotificationLangFlag(true);
        form.setCheckoutShoppingCartMessageLangFlag(true);
        form.setSiteNameLang(translator.translate(form.getSiteName()));
        form.setTemplateFooterLang(translator.translate(Format.getNonNullString(form.getTemplateFooter())));
        form.setSubjectPwdResetLang(translator.translate(Format.getNonNullString(form.getSubjectPwdReset())));
        form.setSubjectCustSalesLang(translator.translate(Format.getNonNullString(form.getSubjectCustSales())));
        form.setSubjectShippingQuoteLang(translator.translate(Format.getNonNullString(form.getSubjectShippingQuote())));
        form.setSubjectNotificationLang(translator.translate(Format.getNonNullString(form.getSubjectNotification())));
        form.setCheckoutShoppingCartMessageLang(translator.translate(Format.getNonNullString(form.getCheckoutShoppingCartMessage())));

		initListInfo(form);
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
    	Site site = getAdminBean(request).getSite();
        SiteDomainMaintActionForm form = (SiteDomainMaintActionForm) actionForm;
        form.setMode("U");
        initSiteProfiles(form, site);
        SiteDomain siteDomain = SiteDomainDAO.load(Format.getLong(form.getSiteDomainId()));   
        SiteDomainLanguage siteDomainLanguage = null;
        for (SiteDomainLanguage language : siteDomain.getSiteDomainLanguages()) {
			if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassId())) {
				siteDomainLanguage = language;
				break;
			}
        }
        if (siteDomainLanguage == null) {
        	User user = getAdminBean(request).getUser();
    		siteDomainLanguage = new SiteDomainLanguage();
    		SiteProfileClass siteProfileClass = SiteProfileClassDAO.load(form.getSiteProfileClassId());
    		SiteDomainParamBean siteDomainParamBean = new SiteDomainParamBean();
    		siteDomainLanguage.setSiteDomainParam(Utility.joxMarshall("SiteDomainParamBean", siteDomainParamBean));
    		siteDomainLanguage.setSiteProfileClass(siteProfileClass);
    		siteDomain.getSiteDomainLanguages().add(siteDomainLanguage);
    		siteDomainLanguage.setRecUpdateBy(user.getUserId());
    		siteDomainLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    		siteDomainLanguage.setRecCreateBy(user.getUserId());
    		siteDomainLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		em.persist(siteDomainLanguage);
        }
        copyProperties(form, siteDomain, getAdminBean(request));
        initListInfo(form);
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

        SiteDomainMaintActionForm form = (SiteDomainMaintActionForm) actionForm;
        form.setMode("U");
        SiteDomain siteDomain = SiteDomainDAO.load(Format.getLong(form.getSiteDomainId()));
        initSiteProfiles(form, siteDomain.getSite());
        copyProperties(form, siteDomain, getAdminBean(request));
        initListInfo(form);
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward remove(ActionMapping mapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
		Site site = getAdminBean(request).getSite();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		SiteDomainMaintActionForm form = (SiteDomainMaintActionForm) actionForm;
        initSiteProfiles(form, site);
        SiteDomain siteDomain = SiteDomainDAO.load(Format.getLong(form.getSiteDomainId()));
		try {
			SiteDomainDAO.remove(siteDomain.getSiteDomainId());
	        em.getTransaction().commit();
		}
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.site.constraint"));
				saveMessages(request, errors);
		        copyProperties(form, siteDomain, getAdminBean(request));
		        initListInfo(form);			
				return mapping.findForward("error");
			}
			throw e;
		}
		ActionForward forward = mapping.findForward("removeSuccess");
		ActionForward actionForward = new ActionForward();
		actionForward.setPath(forward.getPath() + "&siteId=" + form.getSiteId());
		actionForward.setRedirect(forward.getRedirect());
		return actionForward;
	}
    
    public void saveDefault(SiteDomainMaintActionForm form, SiteDomain siteDomain, Site site, User user, AdminBean adminBean, boolean insertMode) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		SiteDomainLanguage siteDomainLanguage = siteDomain.getSiteDomainLanguage();
		SiteDomainParamBean siteDomainParamBean = null;
		
		if (insertMode) {
			siteDomainParamBean = new SiteDomainParamBean();
			
			siteDomain.setRecCreateBy(user.getUserId());
			siteDomain.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			siteDomain.setSite(site);
			
			Template template = TemplateDAO.load(site.getSiteId(), Constants.TEMPLATE_BASIC);
			siteDomain.setTemplate(template);
			form.setTemplateId(template.getTemplateId().toString());
			form.setModuleDisplaySize(String.valueOf(Constants.TEMPLATE_MODULE_DISPLAY_SIZE));
			siteDomainParamBean.setModuleDisplaySize(String.valueOf(Constants.TEMPLATE_MODULE_DISPLAY_SIZE));

			siteDomainLanguage = new SiteDomainLanguage();
			siteDomainLanguage.setSiteLogoContentType("");
			siteDomainLanguage.setRecCreateBy(user.getUserId());
			siteDomainLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			siteDomain.setSiteDomainLanguage(siteDomainLanguage);
			siteDomain.getSiteDomainLanguages().add(siteDomainLanguage);
			siteDomain.setBaseCurrency(site.getSiteCurrencyClassDefault());
			SiteProfileClass siteProfileClass = SiteProfileClassDAO.load(form.getSiteProfileClassId());
			siteDomainLanguage.setSiteProfileClass(siteProfileClass);
		}
		else {
	        Template template = TemplateDAO.load(site.getSiteId(), Format.getLong(form.getTemplateId()));
	        siteDomain.setTemplate(template);
	        if (!Format.isNullOrEmpty(siteDomainLanguage.getSiteDomainParam())) {
	        	siteDomainParamBean = (SiteDomainParamBean) Utility.joxUnMarshall(SiteDomainParamBean.class, siteDomainLanguage.getSiteDomainParam());
	        }
	        siteDomainParamBean.setMailFromContactUs(form.getMailFromContactUs());
	        siteDomainParamBean.setMailFromPwdReset(form.getMailFromPwdReset());
	        siteDomainParamBean.setSubjectNotification(form.getSubjectNotification());
	        siteDomainParamBean.setMailFromNotification(form.getMailFromNotification());
	        siteDomainParamBean.setSubjectPwdReset(form.getSubjectPwdReset());
	        siteDomainParamBean.setMailFromCustSales(form.getMailFromCustSales());
	        siteDomainParamBean.setSubjectCustSales(form.getSubjectCustSales());
	        siteDomainParamBean.setMailFromShippingQuote(form.getMailFromShippingQuote());
	        siteDomainParamBean.setSubjectShippingQuote(form.getSubjectShippingQuote());
	        siteDomainParamBean.setTemplateFooter(form.getTemplateFooter());
	        siteDomainParamBean.setCheckoutNotificationEmail(form.getCheckoutNotificationEmail());
	        siteDomainParamBean.setCheckoutShoppingCartMessage(form.getCheckoutShoppingCartMessage());
	        siteDomainParamBean.setCategoryPageSize(form.getCategoryPageSize());
	        siteDomainParamBean.setPaymentProcessType(form.getPaymentProcessType());
	        siteDomainParamBean.setBusinessContactName(form.getBusinessContactName());
	        siteDomainParamBean.setBusinessCompany(form.getBusinessCompany());
	        siteDomainParamBean.setBusinessAddress1(form.getBusinessAddress1());
	        siteDomainParamBean.setBusinessAddress2(form.getBusinessAddress2());
	        siteDomainParamBean.setBusinessCity(form.getBusinessCity());
	        siteDomainParamBean.setBusinessStateCode(form.getBusinessStateCode());
	        siteDomainParamBean.setBusinessCountryCode(form.getBusinessCountryCode());
	        siteDomainParamBean.setBusinessPostalCode(form.getBusinessPostalCode());
	        siteDomainParamBean.setBusinessPhone(form.getBusinessPhone());
	        siteDomainParamBean.setBusinessFax(form.getBusinessFax());
	        siteDomainParamBean.setBusinessEmail(form.getBusinessEmail());
	        siteDomainParamBean.setModuleDisplaySize(form.getModuleDisplaySize());
	        siteDomainParamBean.setCheckoutIncludeShippingPickup(String.valueOf(Constants.VALUE_NO));
	        siteDomainParamBean.setCheckoutAllowsShippingQuote(String.valueOf(Constants.VALUE_NO));
	        if (form.isCheckoutIncludePickup()) {
	        	siteDomainParamBean.setCheckoutIncludeShippingPickup(String.valueOf(Constants.VALUE_YES));
	        }
	        if (form.isCheckoutAllowsShippingQuote()) {
	        	siteDomainParamBean.setCheckoutAllowsShippingQuote(String.valueOf(Constants.VALUE_YES));
	        }
		}
        siteDomainLanguage.setSiteName(form.getSiteName());
		siteDomainLanguage.setRecUpdateBy(user.getUserId());
		siteDomainLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));

		siteDomain.setSiteDomainName(form.getSiteDomainName());
		siteDomain.setSiteDomainPrefix(form.getSiteDomainPrefix());
        siteDomain.setSitePublicPortNum(form.getSitePublicPortNum());
        siteDomain.setSiteSslEnabled(form.isSiteSecureConnectionEnabled() ? Constants.VALUE_YES : Constants.VALUE_NO);
        siteDomain.setSiteSecurePortNum(form.getSiteSecurePortNum());
        if (form.getBaseSiteCurrencyClassId() != null) {
        	SiteCurrencyClass siteCurrencyClass = (SiteCurrencyClass) em.find(SiteCurrencyClass.class, Format.getLong(form.getBaseSiteCurrencyClassId()));
    		siteDomain.setBaseCurrency(siteCurrencyClass);
        }
        else {
        	siteDomain.setBaseCurrency(null);

        }
		siteDomain.setActive(form.isActive() ? Constants.ACTIVE_YES : Constants.ACTIVE_NO);
		siteDomain.setRecUpdateBy(user.getUserId());
		siteDomain.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    	siteDomainLanguage.setSiteDomainParam(Utility.joxMarshall("SiteDomainParamBean", siteDomainParamBean));

        if (insertMode) {
        	em.persist(siteDomainLanguage);
        	em.persist(siteDomain);
        	form.setSiteDomainId(siteDomain.getSiteDomainId().toString());
        	form.setSiteDomainLangId(siteDomainLanguage.getSiteDomainLangId().toString());
        	form.setDefaultSiteDomainLangId(siteDomainLanguage.getSiteDomainLangId().toString());
        }
        SiteProfileForm siteProfileForms[] = form.getSiteProfiles();
        for (int i = 0; i < siteProfileForms.length; i++) {
        	SiteProfileForm siteProfileForm = siteProfileForms[i];
        	SiteProfile siteProfile = null;
        	if (!Format.isNullOrEmpty(siteProfileForm.getSiteProfileId())) {
        		siteProfile = (SiteProfile) em.find(SiteProfile.class, Format.getLong(siteProfileForm.getSiteProfileId()));
        	}
        	else {
        		siteProfile = new SiteProfile();
                siteProfile.setRecCreateBy(user.getUserId());
                siteProfile.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        		siteDomain.getSiteProfiles().add(siteProfile);
        	}
        	SiteProfileClass siteProfileClass = SiteProfileClassDAO.load(Format.getLong(siteProfileForm.getSiteProfileClassId()));
    		siteProfile.setSiteProfileClass(siteProfileClass);
    		siteProfile.setSeqNum(Format.getInt(siteProfileForm.getSeqNum()));
    		siteProfile.setActive(Constants.VALUE_YES);
    		if (!siteProfileForm.isActive()) {
    			siteProfile.setActive(Constants.VALUE_NO);
    		}
    		siteProfile.setRecUpdateBy(user.getUserId());
            siteProfile.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    		em.persist(siteProfile);
            siteProfileForm.setSiteProfileId(Format.getLong(siteProfile.getSiteProfileId()));
        }
        
        SiteCurrencyForm siteCurrencyForms[] = form.getSiteCurrencies();
        for (int i = 0; i < siteCurrencyForms.length; i++) {
        	SiteCurrencyForm siteCurrencyForm = siteCurrencyForms[i];
        	SiteCurrency siteCurrency = null;
        	if (!Format.isNullOrEmpty(siteCurrencyForm.getSiteCurrencyId())) {
        		siteCurrency = (SiteCurrency) em.find(SiteCurrency.class, Format.getLong(siteCurrencyForm.getSiteCurrencyId()));
        	}
        	else {
        		siteCurrency = new SiteCurrency();
        		siteCurrency.setRecCreateBy(user.getUserId());
        		siteCurrency.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        		siteDomain.getSiteCurrencies().add(siteCurrency);
        		if (i == 0) {
        			siteDomain.setSiteCurrencyDefault(siteCurrency);
        		}
        	}
        	SiteCurrencyClass siteCurrencyClass = SiteCurrencyClassDAO.load(Format.getLong(siteCurrencyForm.getSiteCurrencyClassId()));
        	siteCurrency.setSiteCurrencyClass(siteCurrencyClass);
        	siteCurrency.setSeqNum(Format.getInt(siteCurrencyForm.getSeqNum()));
        	siteCurrency.setExchangeRate(Format.getFloat(siteCurrencyForm.getExchangeRate()));
    		siteCurrency.setActive(Constants.VALUE_YES);
    		if (!siteCurrencyForm.isActive()) {
    			siteCurrency.setActive(Constants.VALUE_NO);
    		}
    		siteCurrency.setRecUpdateBy(user.getUserId());
    		siteCurrency.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
            if (!Format.isNullOrEmpty(siteCurrencyForm.getPaymentGatewayId())) {
            	PaymentGateway paymentGateway = (PaymentGateway) em.find(PaymentGateway.class, Format.getLong(siteCurrencyForm.getPaymentGatewayId()));
            	siteCurrency.setPaymentGateway(paymentGateway);
            }
            else {
            	siteCurrency.setPaymentGateway(null);
            }
            if (!Format.isNullOrEmpty(siteCurrencyForm.getPayPalPaymentGatewayId())) {
            	PaymentGateway paymentGateway = (PaymentGateway) em.find(PaymentGateway.class, Format.getLong(siteCurrencyForm.getPayPalPaymentGatewayId()));
            	siteCurrency.setPayPalPaymentGateway(paymentGateway);
            }
            else {
            	siteCurrency.setPayPalPaymentGateway(null);
            }
            if (siteCurrencyForm.isCashPayment()) {
            	siteCurrency.setCashPayment(Constants.VALUE_YES);
            }
            else {
            	siteCurrency.setCashPayment(Constants.VALUE_NO);
            }
    		em.persist(siteCurrency);
    		siteCurrencyForm.setSiteCurrencyId(siteCurrency.getSiteCurrencyId().toString());
        }
        
        if (insertMode) {
        	SiteDomainDAO.add(siteDomain, user.getUserId(), site.getSiteProfileClassDefault(), site.getSiteCurrencyClassDefault());

        	SiteProfileForm siteProfileForm = new SiteProfileForm();
        	SiteProfile siteProfileDefault = siteDomain.getSiteProfileDefault();
	    	siteProfileForm.setSiteProfileId(Format.getLong(siteProfileDefault.getSiteProfileId()));
	    	siteProfileForm.setSiteProfileClassId(siteProfileDefault.getSiteProfileClass().getSiteProfileClassId().toString());
	    	siteProfileForm.setSeqNum(siteProfileDefault.getSeqNum().toString());
	    	siteProfileForm.setActive(true);
	    	siteProfileForm.setLocked(true);
	    	siteProfileForms = new SiteProfileForm[1];
	    	siteProfileForms[0] = siteProfileForm;
	    	form.setSiteProfiles(siteProfileForms);
            
    		SiteCurrencyForm siteCurrencyForm = new SiteCurrencyForm();
    		SiteCurrency siteCurrencyDefault = siteDomain.getSiteCurrencyDefault();
    		siteCurrencyForm.setSiteCurrencyId(siteCurrencyDefault.getSiteCurrencyId().toString());
    		siteCurrencyForm.setSiteCurrencyClassId(siteCurrencyDefault.getSiteCurrencyClass().getSiteCurrencyClassId().toString());
    		siteCurrencyForm.setExchangeRate("1");
    		siteCurrencyForm.setExchangeRateError("");
    		siteCurrencyForm.setPaymentGatewayId("");
    		siteCurrencyForm.setSeqNum("0");
    		siteCurrencyForm.setCashPayment(true);
    		siteCurrencyForm.setActive(true);
    		siteCurrencyForms = new SiteCurrencyForm[1];
    		siteCurrencyForms[0] = siteCurrencyForm;
    		form.setSiteCurrencies(siteCurrencyForms);
        }
		form.setSiteDomainUrl(Utility.formatDomainURL(siteDomain, adminBean.getContextPath()));
    }
    
    public void saveLanguage(SiteDomainMaintActionForm form, SiteDomain siteDomain, Site site, User user) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	
    	Long siteProfileClassId = form.getSiteProfileClassId();
    	SiteDomainLanguage siteDomainLanguage = null;
        SiteDomainParamBean siteDomainParamBean = null;
    	for (SiteDomainLanguage language : siteDomain.getSiteDomainLanguages()) {
    		if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
    			siteDomainLanguage = language;
    			siteDomainParamBean = (SiteDomainParamBean) Utility.joxUnMarshall(SiteDomainParamBean.class, language.getSiteDomainParam());
    			break;
    		}
    	}

    	if (siteDomainLanguage == null) {
    		siteDomainLanguage = new SiteDomainLanguage();
    		siteDomainLanguage.setRecCreateBy(user.getUserId());
    		siteDomainLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		siteDomain.getSiteDomainLanguages().add(siteDomainLanguage);
			SiteProfileClass siteProfileClass = (SiteProfileClass) em.find(SiteProfileClass.class, form.getSiteProfileClassId());
			siteDomainLanguage.setSiteProfileClass(siteProfileClass);
			siteDomainParamBean = new SiteDomainParamBean();
    	}
    	siteDomainLanguage.setSiteName(null);
    	if (form.isSiteNameLangFlag()) {
    		siteDomainLanguage.setSiteName(form.getSiteNameLang());
    	}
    	
    	if (form.isTemplateFooterLangFlag()) {
    		siteDomainParamBean.setTemplateFooter(form.getTemplateFooterLang());
    	}
    	else {
    		siteDomainParamBean.setTemplateFooter(null);
    	}
    	if (form.isSubjectPwdResetLangFlag()) {
    		siteDomainParamBean.setSubjectPwdReset(form.getSubjectPwdResetLang());
    	}
    	if (form.isSubjectNotificationLangFlag()) {
    		siteDomainParamBean.setSubjectNotification(form.getSubjectNotificationLang());
    	}
    	if (form.isSubjectCustSalesLangFlag()) {
    		siteDomainParamBean.setSubjectCustSales(form.getSubjectCustSalesLang());
    	}
    	if (form.isSubjectShippingQuoteLangFlag()) {
    		siteDomainParamBean.setSubjectShippingQuote(form.getSubjectShippingQuoteLang());
    	}
    	if (form.isCheckoutShoppingCartMessageLangFlag()) {
    		siteDomainParamBean.setCheckoutShoppingCartMessage(form.getCheckoutShoppingCartMessageLang());
    	}
    	siteDomainLanguage.setRecUpdateBy(user.getUserId());
    	siteDomainLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    	siteDomainLanguage.setSiteDomainParam(Utility.joxMarshall("SiteDomainParamBean", siteDomainParamBean));

    	em.persist(siteDomainLanguage);
        CacheDAO.removeByKeyPrefix(site.getSiteId(), Constants.CACHE_MENU);
    }

	public ActionForward save(ActionMapping mapping, 
							  ActionForm actionForm,
							  HttpServletRequest request,
							  HttpServletResponse response) 
		throws Throwable {

        SiteDomainMaintActionForm form = (SiteDomainMaintActionForm) actionForm;
    	AdminBean adminBean = getAdminBean(request);
    	User user = adminBean.getUser();
    	Site site = SiteDAO.load(form.getSiteId());
		boolean insertMode = false;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}
        initSiteProfiles(form, site);

		SiteDomain siteDomain = null;
		if (!insertMode) {
			siteDomain = SiteDomainDAO.load(Format.getLong(form.getSiteDomainId()));
		}
		else {
			siteDomain = new SiteDomain();
		}

		ActionMessages errors = validate(request, form, site, insertMode);
		if (errors.size() != 0) {
			saveMessages(request, errors);
	        initListInfo(form);
			return mapping.findForward("error");
		}
		
		if (insertMode || form.isSiteProfileClassDefault()) {
			saveDefault(form, siteDomain, site, user, adminBean, insertMode);
		}
		else {
			saveLanguage(form, siteDomain, site, user);
		}
		
		// In case siteProfileName is updated
		initSiteProfiles(form, site);
        form.setSingleCheckout(false);
        if (site.getSingleCheckout() == Constants.VALUE_YES) {
        	form.setSingleCheckout(true);
        }
	    adminBean.init(adminBean.getUser().getUserId(), adminBean.getSite().getSiteId());
        initListInfo(form);
        form.setMode("U");
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        CacheDAO.removeByKeyPrefix(site.getSiteId(), Constants.CACHE_MENU);
		return mapping.findForward("success");
	}

	public ActionForward uploadImage(ActionMapping mapping, 
				 ActionForm actionForm,
				 HttpServletRequest request,
				 HttpServletResponse response) 
		throws Throwable {
	
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		SiteDomainMaintActionForm form = (SiteDomainMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		MessageResources resources = this.getResources(request);
		
		String siteDomainId = form.getSiteDomainId();
        SiteDomain siteDomain = SiteDomainDAO.load(Format.getLong(siteDomainId));

        JSONEscapeObject json = new JSONEscapeObject();
		FormFile file = form.getFile();
		byte fileData[] = file.getFileData();
		if (file.getFileName().length() == 0) {
			json.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			json.put("filename", resources.getMessage("error.string.required"));
			streamWebService(response, json.toHtmlString());
			return null;
		}
		ImageScaler scaler = new ImageScaler(fileData, file.getContentType());
		scaler.resize(600);

		initSiteProfiles(form, site);
        if (form.isSiteProfileClassDefault()) {
    		siteDomain.getSiteDomainLanguage().setSiteLogoValue(scaler.getBytes());
    		siteDomain.getSiteDomainLanguage().setSiteLogoContentType("image/jpeg");
    		siteDomain.setRecUpdateBy(adminBean.getUser().getUserId());
    		siteDomain.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    		siteDomain.setRecCreateBy(adminBean.getUser().getUserId());
    		siteDomain.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		form.setSiteLogoContentType(siteDomain.getSiteDomainLanguage().getSiteLogoContentType());
    		em.persist(siteDomain);
        }
        else {
        	SiteDomainLanguage siteDomainLanguage = null;
        	for (SiteDomainLanguage language : siteDomain.getSiteDomainLanguages()) {
        		Long siteProfileClassId = form.getSiteProfileClassId();
        		if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
        			siteDomainLanguage = language;
        			break;
        		}
        	}
        	siteDomainLanguage.setSiteLogoValue(scaler.getBytes());
        	siteDomainLanguage.setSiteLogoContentType("image/jpeg");
        	siteDomainLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
        	siteDomainLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        	siteDomainLanguage.setRecCreateBy(adminBean.getUser().getUserId());
        	siteDomainLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        	form.setSiteLogoContentType(siteDomainLanguage.getSiteLogoContentType());
    		em.persist(siteDomainLanguage);
    		form.setSiteLogoContentTypeLang(siteDomainLanguage.getSiteLogoContentType());
        }
	   
		json.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		streamWebService(response, json.toHtmlString());
		
		return null;
	}

	public ActionForward removeImage(ActionMapping mapping, 
			 ActionForm actionForm,
			 HttpServletRequest httpServletRequest,
			 HttpServletResponse httpServletResponse) 
		throws Throwable {
	
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		SiteDomainMaintActionForm form = (SiteDomainMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(httpServletRequest);
		Site site = adminBean.getSite();
        initSiteProfiles(form, site);

		String siteDomainId = form.getSiteDomainId();
        SiteDomain siteDomain = SiteDomainDAO.load(Format.getLong(siteDomainId));

        if (form.isSiteProfileClassDefault()) {
        	siteDomain.getSiteDomainLanguage().setSiteLogoValue(null);
        	siteDomain.getSiteDomainLanguage().setSiteLogoContentType("");
        	siteDomain.setRecUpdateBy(adminBean.getUser().getUserId());
        	siteDomain.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        	siteDomain.setRecCreateBy(adminBean.getUser().getUserId());
        	siteDomain.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		em.persist(siteDomain);
        }
        else {
        	SiteDomainLanguage siteDomainLanguage = null;
        	for (SiteDomainLanguage language : siteDomain.getSiteDomainLanguages()) {
        		Long siteProfileClassId = form.getSiteProfileClassId();
        		if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
        			siteDomainLanguage = language;
        			break;
        		}
        	}
        	siteDomainLanguage.setSiteLogoValue(null);
        	siteDomainLanguage.setSiteLogoContentType("");
        	siteDomainLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
        	siteDomainLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        	siteDomainLanguage.setRecCreateBy(adminBean.getUser().getUserId());
        	siteDomainLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		em.persist(siteDomainLanguage);
    		form.setSiteLogoContentTypeLang(siteDomainLanguage.getSiteLogoContentType());
        }
		form.setSiteLogoContentType("");
		
		FormUtils.setFormDisplayMode(httpServletRequest, form, FormUtils.EDIT_MODE);
		return null;
	}
	
	public ActionForward resetLanguageImage(ActionMapping mapping, 
			 ActionForm actionForm,
			 HttpServletRequest request,
			 HttpServletResponse response) 
		throws Throwable {
	
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		SiteDomainMaintActionForm form = (SiteDomainMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		String activate = request.getParameter("activate");
		
		String siteDomainId = form.getSiteDomainId();
        SiteDomain siteDomain = SiteDomainDAO.load(Format.getLong(siteDomainId));

    	SiteDomainLanguage siteDomainLanguage = null;
		Long siteProfileClassId = form.getSiteProfileClassId();
    	for (SiteDomainLanguage language : siteDomain.getSiteDomainLanguages()) {
    		if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
    			siteDomainLanguage = language;
    			break;
    		}
    	}
    	if (siteDomainLanguage == null) {
    		siteDomainLanguage = new SiteDomainLanguage();
    		SiteProfileClass siteProfileClass = SiteProfileClassDAO.load(siteProfileClassId);
    		SiteDomainParamBean siteDomainParamBean = new SiteDomainParamBean();
    		siteDomainLanguage.setSiteDomainParam(Utility.joxMarshall("SiteDomainParamBean", siteDomainParamBean));
    		siteDomainLanguage.setSiteProfileClass(siteProfileClass);
    		siteDomain.getSiteDomainLanguages().add(siteDomainLanguage);
    	}
       	if (activate.equalsIgnoreCase("true")) {
       		siteDomainLanguage.setSiteLogoContentType("");       		
       	}
       	else {
       		siteDomainLanguage.setSiteLogoContentType(null);
       	}
       	siteDomainLanguage.setSiteLogoValue(null);
       	siteDomainLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
       	siteDomainLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
       	siteDomainLanguage.setRecCreateBy(adminBean.getUser().getUserId());
       	siteDomainLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
   		em.persist(siteDomainLanguage);
   		form.setSiteLogoContentTypeLang(siteDomainLanguage.getSiteLogoContentType());
		form.setSiteLogoContentType(siteDomainLanguage.getSiteLogoContentType());
		FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		
		JSONEscapeObject json = new JSONEscapeObject();
		json.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		streamWebService(response, json.toHtmlString());
		return null;
	}
    
    public void copyProperties(SiteDomainMaintActionForm form, SiteDomain siteDomain, AdminBean adminBean) throws Exception {
    	form.setSiteDomainDefault(false);
    	Long siteDomainDefaultId = siteDomain.getSite().getSiteDomainDefault().getSiteDomainId();
    	if (siteDomain.getSiteDomainId().equals(siteDomainDefaultId)) {
    		form.setSiteDomainDefault(true);
    	}
    	
    	form.setSiteDomainUrl(Utility.formatDomainURL(siteDomain, adminBean.getContextPath()));
    	form.setSiteId(siteDomain.getSite().getSiteId());
        form.setSiteDomainId(siteDomain.getSiteDomainId().toString());
        form.setSiteName(siteDomain.getSiteDomainLanguage().getSiteName());
        form.setSiteDomainName(siteDomain.getSiteDomainName());
        form.setSiteDomainPrefix(siteDomain.getSiteDomainPrefix());
        form.setSitePublicPortNum(siteDomain.getSitePublicPortNum());
    	form.setSiteSecureConnectionEnabled(siteDomain.getSiteSslEnabled() == Constants.VALUE_YES ? true : false);
        form.setSiteSecurePortNum(siteDomain.getSiteSecurePortNum());
        form.setTemplateId(siteDomain.getTemplate().getTemplateId().toString());
        if (siteDomain.getBaseCurrency() != null) {
        	form.setBaseSiteCurrencyClassId(siteDomain.getBaseCurrency().getSiteCurrencyClassId().toString());
        }

        Vector<SiteProfileForm> vector = new Vector<SiteProfileForm>();
        SiteProfile siteProfileDefault = siteDomain.getSiteProfileDefault();
        if (siteProfileDefault != null) {
	    	SiteProfileForm siteProfileForm = new SiteProfileForm();
	    	siteProfileForm.setSiteProfileId(Format.getLong(siteProfileDefault.getSiteProfileId()));
	    	siteProfileForm.setSiteProfileClassId(siteProfileDefault.getSiteProfileClass().getSiteProfileClassId().toString());
	    	siteProfileForm.setSeqNum(siteProfileDefault.getSeqNum().toString());
	    	if (siteProfileDefault.getActive() == Constants.ACTIVE_YES) {
	    		siteProfileForm.setActive(true);
	    	}
	    	else {
	    		siteProfileForm.setActive(false);
	    	}
	    	siteProfileForm.setLocked(true);
	    	vector.add(siteProfileForm);
        }

        Iterator<?> iterator = siteDomain.getSiteProfiles().iterator();
        while (iterator.hasNext()) {
        	SiteProfile siteProfile = (SiteProfile) iterator.next();
        	if (siteProfileDefault != null) {
        		if (siteProfile.getSiteProfileId().equals(siteProfileDefault.getSiteProfileId())) {
        			continue;
        		}
        	}
        	SiteProfileForm siteProfileForm = new SiteProfileForm();
        	siteProfileForm.setSiteProfileId(Format.getLong(siteProfile.getSiteProfileId()));
        	siteProfileForm.setSiteProfileClassId(siteProfile.getSiteProfileClass().getSiteProfileClassId().toString());
	    	siteProfileForm.setSeqNum(siteProfile.getSeqNum().toString());
        	if (siteProfile.getActive() == Constants.ACTIVE_YES) {
        		siteProfileForm.setActive(true);
        	}
        	else {
        		siteProfileForm.setActive(false);
        	}
        	vector.add(siteProfileForm);
        }
    
        SiteProfileForm siteProfiles[] = new SiteProfileForm[vector.size()];
        vector.copyInto(siteProfiles);
        form.setSiteProfiles(siteProfiles);
        
        Vector<SiteCurrencyForm> siteCurrenciesVector = new Vector<SiteCurrencyForm>();
        SiteCurrency siteCurrencyDefault = siteDomain.getSiteCurrencyDefault();
        if (siteCurrencyDefault != null) {
	    	SiteCurrencyForm siteCurrencyForm = new SiteCurrencyForm();
	    	siteCurrencyForm.setSiteCurrencyId(siteCurrencyDefault.getSiteCurrencyId().toString());
	    	siteCurrencyForm.setSeqNum(siteCurrencyDefault.getSeqNum().toString());
	    	siteCurrencyForm.setSiteCurrencyClassId(siteCurrencyDefault.getSiteCurrencyClass().getSiteCurrencyClassId().toString());
	    	siteCurrencyForm.setExchangeRate(Format.getFloat4(siteCurrencyDefault.getExchangeRate()));
	    	if (siteCurrencyDefault.getCashPayment() == Constants.VALUE_YES) {
	    		siteCurrencyForm.setCashPayment(true);
	    	}
	    	else {
	    		siteCurrencyForm.setCashPayment(false);
	    	}
			if (siteCurrencyDefault.getPaymentGateway() != null) {
				siteCurrencyForm.setPaymentGatewayId(siteCurrencyDefault.getPaymentGateway().getPaymentGatewayId().toString());
			}
			if (siteCurrencyDefault.getPayPalPaymentGateway() != null) {
				siteCurrencyForm.setPayPalPaymentGatewayId(siteCurrencyDefault.getPayPalPaymentGateway().getPaymentGatewayId().toString());
			}
	    	if (siteCurrencyDefault.getActive() == Constants.ACTIVE_YES) {
	    		siteCurrencyForm.setActive(true);
	    	}
	    	else {
	    		siteCurrencyForm.setActive(false);
	    	}
	    	siteCurrencyForm.setLocked(true);
	    	siteCurrenciesVector.add(siteCurrencyForm);
        }

        iterator = siteDomain.getSiteCurrencies().iterator();
        while (iterator.hasNext()) {
        	SiteCurrency siteCurrency = (SiteCurrency) iterator.next();
        	if (siteCurrency.getSiteCurrencyId().equals(siteCurrencyDefault.getSiteCurrencyId())) {
        		continue;
        	}
        	SiteCurrencyForm siteCurrencyForm = new SiteCurrencyForm();
        	siteCurrencyForm.setSiteCurrencyId(siteCurrency.getSiteCurrencyId().toString());
	    	siteCurrencyForm.setSeqNum(siteCurrency.getSeqNum().toString());
        	siteCurrencyForm.setSiteCurrencyClassId(siteCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().toString());
        	siteCurrencyForm.setExchangeRate(Format.getFloat4(siteCurrency.getExchangeRate()));
	    	if (siteCurrency.getCashPayment() == Constants.VALUE_YES) {
	    		siteCurrencyForm.setCashPayment(true);
	    	}
	    	else {
	    		siteCurrencyForm.setCashPayment(false);
	    	}
    		if (siteCurrency.getPaymentGateway() != null) {
    			siteCurrencyForm.setPaymentGatewayId(siteCurrency.getPaymentGateway().getPaymentGatewayId().toString());
    		}
    		if (siteCurrency.getPayPalPaymentGateway() != null) {
    			siteCurrencyForm.setPayPalPaymentGatewayId(siteCurrency.getPayPalPaymentGateway().getPaymentGatewayId().toString());
    		}
        	if (siteCurrency.getActive() == Constants.ACTIVE_YES) {
        		siteCurrencyForm.setActive(true);
        	}
        	else {
        		siteCurrencyForm.setActive(false);
        	}

        	siteCurrenciesVector.add(siteCurrencyForm);
        }
    
        SiteCurrencyForm siteCurrencies[] = new SiteCurrencyForm[siteCurrenciesVector.size()];
        siteCurrenciesVector.copyInto(siteCurrencies);
        form.setSiteCurrencies(siteCurrencies);
        form.setActive(false);
        if (siteDomain.getActive() == Constants.VALUE_YES) {
        	form.setActive(true);
        }
        form.setMaster(false);
        Site site = siteDomain.getSite();
        if (site.getSiteDomainDefault().getSiteDomainId().equals(siteDomain.getSiteDomainId())) {
        	form.setMaster(true);
        }
        form.setSingleCheckout(false);
        if (site.getSingleCheckout() == Constants.VALUE_YES) {
        	form.setSingleCheckout(true);
        }
    
        SiteDomainParamBean siteDomainParamBean = (SiteDomainParamBean) Utility.joxUnMarshall(SiteDomainParamBean.class, siteDomain.getSiteDomainLanguage().getSiteDomainParam());
        form.setModuleDisplaySize(siteDomainParamBean.getModuleDisplaySize());
        form.setMailFromContactUs(siteDomainParamBean.getMailFromContactUs());
    	form.setMailFromNotification(siteDomainParamBean.getMailFromNotification());
    	form.setSubjectNotification(siteDomainParamBean.getSubjectNotification());
    	form.setMailFromPwdReset(siteDomainParamBean.getMailFromPwdReset());
    	form.setSubjectPwdReset(siteDomainParamBean.getSubjectPwdReset());
    	form.setMailFromCustSales(siteDomainParamBean.getMailFromCustSales());
    	form.setSubjectCustSales(siteDomainParamBean.getSubjectCustSales());
    	form.setMailFromShippingQuote(siteDomainParamBean.getMailFromShippingQuote());
    	form.setSubjectShippingQuote(siteDomainParamBean.getSubjectShippingQuote());
    	form.setTemplateFooter(siteDomainParamBean.getTemplateFooter());
    	form.setCheckoutNotificationEmail(siteDomainParamBean.getCheckoutNotificationEmail());;
    	form.setCheckoutIncludePickup(false);
    	form.setCheckoutAllowsShippingQuote(false);
    	if (siteDomainParamBean.getCheckoutIncludeShippingPickup() != null) {
	    	if (siteDomainParamBean.getCheckoutIncludeShippingPickup().equals(String.valueOf(Constants.VALUE_YES))) {
	    		form.setCheckoutIncludePickup(true);
	    	}
    	}
    	if (siteDomainParamBean.getCheckoutAllowsShippingQuote() != null) {
	    	if (siteDomainParamBean.getCheckoutAllowsShippingQuote().equals(String.valueOf(Constants.VALUE_YES))) {
	    		form.setCheckoutAllowsShippingQuote(true);
	    	}
    	}
    	form.setCategoryPageSize(siteDomainParamBean.getCategoryPageSize());
    	form.setPaymentProcessType(siteDomainParamBean.getPaymentProcessType()); 
    	form.setBusinessContactName(siteDomainParamBean.getBusinessContactName()); 
    	form.setBusinessCompany(siteDomainParamBean.getBusinessCompany()); 
    	form.setBusinessAddress1(siteDomainParamBean.getBusinessAddress1()); 
    	form.setBusinessAddress2(siteDomainParamBean.getBusinessAddress2()); 
    	form.setBusinessCity(siteDomainParamBean.getBusinessCity()); 
    	form.setBusinessStateCode(siteDomainParamBean.getBusinessStateCode()); 
    	form.setBusinessCountryCode(siteDomainParamBean.getBusinessCountryCode()); 
    	form.setBusinessPostalCode(siteDomainParamBean.getBusinessPostalCode()); 
    	form.setBusinessPhone(siteDomainParamBean.getBusinessPhone()); 
    	form.setBusinessFax(siteDomainParamBean.getBusinessFax()); 
    	form.setBusinessEmail(siteDomainParamBean.getBusinessEmail()); 
    	form.setCheckoutShoppingCartMessage(siteDomainParamBean.getCheckoutShoppingCartMessage());
    	if (Format.isNullOrEmpty(form.getCheckoutShoppingCartMessage())) {
    		form.setCheckoutShoppingCartMessage("");
    	}
    	form.setSiteLogoContentType(siteDomain.getSiteDomainLanguage().getSiteLogoContentType());  	
    	form.setSiteNameLang(siteDomain.getSiteDomainLanguage().getSiteName());
    	form.setSiteNameLangFlag(false);
    	form.setTemplateFooterLang(Format.getNonNullString(siteDomainParamBean.getTemplateFooter()));
    	form.setTemplateFooterLangFlag(false);
    	form.setCheckoutShoppingCartMessageLang(Format.getNonNullString(siteDomainParamBean.getCheckoutShoppingCartMessage()));
    	form.setCheckoutShoppingCartMessageLangFlag(false);
    	form.setSubjectPwdResetLang(Format.getNonNullString(siteDomainParamBean.getSubjectPwdReset()));
    	form.setSubjectPwdResetLangFlag(false);
    	form.setSubjectCustSalesLang(Format.getNonNullString(siteDomainParamBean.getSubjectCustSales()));
    	form.setSubjectCustSalesLangFlag(false);
    	form.setSubjectShippingQuoteLang(Format.getNonNullString(siteDomainParamBean.getSubjectShippingQuote()));
    	form.setSubjectShippingQuoteLangFlag(false);
    	form.setSubjectNotificationLang(Format.getNonNullString(siteDomainParamBean.getSubjectNotification()));
    	form.setSubjectNotificationLangFlag(false);
    	form.setSiteLogoContentTypeLang(Format.getNonNullString(siteDomain.getSiteDomainLanguage().getSiteLogoContentType()));
    	form.setSiteLogoFlag(false);
    	form.setDefaultSiteDomainLangId(siteDomain.getSiteDomainLanguage().getSiteDomainLangId().toString());
    	if (!form.isSiteProfileClassDefault()) {
            SiteDomainLanguage siteDomainLanguage = null;
            for (SiteDomainLanguage language : siteDomain.getSiteDomainLanguages()) {
    			if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassId())) {
    				siteDomainLanguage = language;
    				break;
    			}
            }
            if (siteDomainLanguage == null) {
        		siteDomainLanguage = new SiteDomainLanguage();
        		SiteProfileClass siteProfileClass = SiteProfileClassDAO.load(form.getSiteProfileClassId());
        		siteDomainParamBean = new SiteDomainParamBean();
        		siteDomainLanguage.setSiteDomainParam(Utility.joxMarshall("SiteDomainParamBean", siteDomainParamBean));
        		siteDomainLanguage.setSiteProfileClass(siteProfileClass);
        		siteDomain.getSiteDomainLanguages().add(siteDomainLanguage);
            }
			form.setSiteDomainLangId(siteDomainLanguage.getSiteDomainLangId().toString());
			if (siteDomainLanguage.getSiteName() != null) {
				form.setSiteNameLang(siteDomainLanguage.getSiteName());
				form.setSiteNameLangFlag(true);
			}
			if (siteDomainLanguage.getSiteLogoContentType() != null) {
				form.setSiteLogoContentTypeLang(siteDomainLanguage.getSiteLogoContentType());
				form.setSiteLogoFlag(true);
			}
			SiteDomainParamBean profileParamBean = (SiteDomainParamBean) Utility.joxUnMarshall(SiteDomainParamBean.class, siteDomainLanguage.getSiteDomainParam());
			String value = null;
			value = profileParamBean.getTemplateFooter();
			if (value != null) {
				form.setTemplateFooterLang(value);
				form.setTemplateFooterLangFlag(true);
			}
			value = profileParamBean.getCheckoutShoppingCartMessage();
			if (value != null) {
				form.setCheckoutShoppingCartMessageLang(value);
				form.setCheckoutShoppingCartMessageLangFlag(true);
			}
			value = profileParamBean.getSubjectPwdReset();
			if (value != null) {
				form.setSubjectPwdResetLang(value);
				form.setSubjectPwdResetLangFlag(true);
			}
			value = profileParamBean.getSubjectCustSales();
			if (value != null) {
				form.setSubjectCustSalesLang(value);
				form.setSubjectCustSalesLangFlag(true);
			}
			value = profileParamBean.getSubjectShippingQuote();
			if (value != null) {
				form.setSubjectShippingQuoteLang(value);
				form.setSubjectShippingQuoteLangFlag(true);
			}
    	}
    }
    
    public ActionForward addProfile(ActionMapping mapping,
						            ActionForm actionForm,
						            HttpServletRequest request,
						            HttpServletResponse response)
						    		throws Throwable {
		SiteDomainMaintActionForm form = (SiteDomainMaintActionForm) actionForm;
		Site site = getAdminBean(request).getSite();
		SiteDomain siteDomain = SiteDomainDAO.load(Format.getLong(form.getSiteDomainId()));
		int seqNum = 0;
		for (SiteProfile siteProfile : siteDomain.getSiteProfiles()) {
			if (siteProfile.getSeqNum() >= seqNum) {
				seqNum = siteProfile.getSeqNum() + 1;
			}
		}
		SiteProfileForm siteProfileForms[] = form.getSiteProfiles();
		Vector<SiteProfileForm> vector = new Vector<SiteProfileForm>();
		for (int i = 0; i < siteProfileForms.length; i++) {
			vector.add(siteProfileForms[i]);
		}
		SiteProfileForm siteProfileForm = new SiteProfileForm();
		siteProfileForm.setSiteProfileId("");
		siteProfileForm.setSiteProfileClassId("");
		siteProfileForm.setSeqNum(Format.getInt(seqNum));
		vector.add(siteProfileForm);
		siteProfileForms = new SiteProfileForm[vector.size()];
		vector.copyInto(siteProfileForms);
		form.setSiteProfiles(siteProfileForms);

        initSiteProfiles(form, site);
		initListInfo(form);

		ActionForward actionForward = mapping.findForward("success");
		return actionForward;
    }
    
    public ActionForward removeProfile(ActionMapping mapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	Site site = getAdminBean(request).getSite();
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		SiteDomainMaintActionForm form = (SiteDomainMaintActionForm) actionForm;
		SiteProfileForm siteProfileForms[] = form.getSiteProfiles();
		Vector<SiteProfileForm> vector = new Vector<SiteProfileForm>();
		for (int i = 0; i < siteProfileForms.length; i++) {
			if (siteProfileForms[i].remove) {
/*
				String sql = "";
				Query query = null;
				sql = "delete   " +
				      "from    ContactUsLanguage contactUsLanguage " +
				      "where   contactUsLanguage.siteProfile.siteProfileId = :siteProfileId";
				query = em.createQuery(sql);
				query.setParameter("siteProfileId", siteProfileForms[i].getSiteProfileId());
				query.executeUpdate();

				sql = "delete   " +
			      	  "from    ContentImageLanguage contentImageLanguage " +
			      	  "where   contentImageLanguage.siteProfile.siteProfileId = :siteProfileId";
				query = em.createQuery(sql);
				query.setParameter("siteProfileId", siteProfileForms[i].getSiteProfileId());
				query.executeUpdate();

				sql = "delete   " +
			      	  "from    ContentLanguage contentLanguage " +
			      	  "where   contentLanguage.siteProfile.siteProfileId = :siteProfileId";
				query = em.createQuery(sql);
				query.setParameter("siteProfileId", siteProfileForms[i].getSiteProfileId());
				query.executeUpdate();

				sql = "delete   " +
			      	  "from    CouponLanguage couponLanguage " +
			      	  "where   couponLanguage.siteProfile.siteProfileId = :siteProfileId";
				query = em.createQuery(sql);
				query.setParameter("siteProfileId", siteProfileForms[i].getSiteProfileId());
				query.executeUpdate();

				sql = "delete   " +
			      	  "from    CustomAttributeLanguage customAttributeLanguage " +
			      	  "where   customAttributeLanguage.siteProfile.siteProfileId = :siteProfileId";
				query = em.createQuery(sql);
				query.setParameter("siteProfileId", siteProfileForms[i].getSiteProfileId());
				query.executeUpdate();

				sql = "delete   " +
			      	  "from    ItemAttributeDetailLanguage itemAttributeDetailLanguage " +
			      	  "where   itemAttributeDetailLanguage.siteProfile.siteProfileId = :siteProfileId";
				query = em.createQuery(sql);
				query.setParameter("siteProfileId", siteProfileForms[i].getSiteProfileId());
				query.executeUpdate();

				sql = "delete   " +
			      	  "from    ItemImageLanguage itemImageLanguage " +
			      	  "where   itemImageLanguage.siteProfile.siteProfileId = :siteProfileId";
				query = em.createQuery(sql);
				query.setParameter("siteProfileId", siteProfileForms[i].getSiteProfileId());
				query.executeUpdate();
		
				sql = "delete   " +
			      	  "from    ItemLanguage itemLanguage " +
			      	  "where   itemLanguage.siteProfile.siteProfileId = :siteProfileId";
				query = em.createQuery(sql);
				query.setParameter("siteProfileId", siteProfileForms[i].getSiteProfileId());
				query.executeUpdate();
				
				sql = "delete   " +
			      	  "from    MenuLanguage menuLanguage " +
			      	  "where   menuLanguage.siteProfile.siteProfileId = :siteProfileId";
				query = em.createQuery(sql);
				query.setParameter("siteProfileId", siteProfileForms[i].getSiteProfileId());
				query.executeUpdate();
				
				sql = "delete   " +
			      	  "from    CategoryLanguage categoryLanguage " +
			      	  "where   categoryLanguage.siteProfile.siteProfileId = :siteProfileId";
				query = em.createQuery(sql);
				query.setParameter("siteProfileId", siteProfileForms[i].getSiteProfileId());
				query.executeUpdate();
			
				sql = "delete   " +
			      	  "from    ShippingMethodLanguage shippingMethodLanguage " +
			      	  "where   shippingMethodLanguage.siteProfile.siteProfileId = :siteProfileId";
				query = em.createQuery(sql);
				query.setParameter("siteProfileId", siteProfileForms[i].getSiteProfileId());
				query.executeUpdate();
			
				sql = "delete   " +
			      	  "from    SiteLanguage siteLanguage " +
			      	  "where   siteLanguage.siteProfile.siteProfileId = :siteProfileId";
				query = em.createQuery(sql);
				query.setParameter("siteProfileId", siteProfileForms[i].getSiteProfileId());
				query.executeUpdate();
*/
				if (!Format.isNullOrEmpty(siteProfileForms[i].getSiteProfileId())) {
					SiteProfile siteProfile = (SiteProfile) em.find(SiteProfile.class, Format.getLong(siteProfileForms[i].getSiteProfileId()));
					siteProfile.getSiteDomain().getSiteProfiles().remove(siteProfile);
					siteProfile.setSiteDomain(null);
					em.remove(siteProfile);
				}
			}
			else {
				vector.add(siteProfileForms[i]);
			}
		}
		siteProfileForms = new SiteProfileForm[vector.size()];
		vector.copyInto(siteProfileForms);
		form.setSiteProfiles(siteProfileForms);
        initSiteProfiles(form, site);
		initListInfo(form);
		
		ActionForward actionForward = mapping.findForward("success");
		return actionForward;
	}
    
    public ActionForward addCurrency(ActionMapping mapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	Site site = getAdminBean(request).getSite();
		SiteDomainMaintActionForm form = (SiteDomainMaintActionForm) actionForm;
		SiteDomain siteDomain = SiteDomainDAO.load(Format.getLong(form.getSiteDomainId()));
		int seqNum = 0;
		for (SiteCurrency siteCurrency : siteDomain.getSiteCurrencies()) {
			if (siteCurrency.getSeqNum() >= seqNum) {
				seqNum = siteCurrency.getSeqNum() + 1;
			}
		}
		SiteCurrencyForm siteCurrencyForms[] = form.getSiteCurrencies();
		Vector<SiteCurrencyForm> vector = new Vector<SiteCurrencyForm>();
		for (int i = 0; i < siteCurrencyForms.length; i++) {
			vector.add(siteCurrencyForms[i]);
		}
		SiteCurrencyForm siteCurrencyForm = new SiteCurrencyForm();
		siteCurrencyForm.setSiteCurrencyId("");
		siteCurrencyForm.setExchangeRate("");
		siteCurrencyForm.setExchangeRateError("");
		siteCurrencyForm.setPaymentGatewayId("");
		siteCurrencyForm.setSeqNum(Format.getInt(seqNum));
		vector.add(siteCurrencyForm);
		siteCurrencyForms = new SiteCurrencyForm[vector.size()];
		vector.copyInto(siteCurrencyForms);
		form.setSiteCurrencies(siteCurrencyForms);
		
		initSiteProfiles(form, site);
		initListInfo(form);
		
		ActionForward actionForward = mapping.findForward("success");
		return actionForward;
	}
    
    public ActionForward removeCurrency(ActionMapping mapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	Site site = getAdminBean(request).getSite();
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		SiteDomainMaintActionForm form = (SiteDomainMaintActionForm) actionForm;
		SiteCurrencyForm siteCurrencyForms[] = form.getSiteCurrencies();
		Vector<SiteCurrencyForm> vector = new Vector<SiteCurrencyForm>();
		for (int i = 0; i < siteCurrencyForms.length; i++) {
			if (siteCurrencyForms[i].isRemove()) {
				if (!Format.isNullOrEmpty(siteCurrencyForms[i].getSiteCurrencyId())) {
					SiteCurrency siteCurrency = (SiteCurrency) em.find(SiteCurrency.class, Format.getLong(siteCurrencyForms[i].getSiteCurrencyId()));
					SiteDomain siteDomain = siteCurrency.getSiteDomain();
					siteDomain.getSiteCurrencies().remove(siteCurrency);
					em.remove(siteCurrency);
				}
			}
			else {
				vector.add(siteCurrencyForms[i]);
			}
		}
		siteCurrencyForms = new SiteCurrencyForm[vector.size()];
		vector.copyInto(siteCurrencyForms);
		form.setSiteCurrencies(siteCurrencyForms);
        initSiteProfiles(form, site);
		initListInfo(form);
		
		ActionForward actionForward = mapping.findForward("success");
		return actionForward;
	}
    
    public void  initListInfo(SiteDomainMaintActionForm form) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        Vector<LabelValueBean> vector = new Vector<LabelValueBean>();
        Query query = em.createQuery("from ShippingType where siteId = :siteId order by shippingTypeName");
        query.setParameter("siteId", form.getSiteId());
        Iterator<?> iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	ShippingType shippingType = (ShippingType) iterator.next();
        	LabelValueBean bean = new LabelValueBean();
        	bean.setLabel(shippingType.getShippingTypeName());
        	bean.setValue(shippingType.getShippingTypeId().toString());
        	vector.add(bean);
        }
        LabelValueBean shippingTypes[] = new LabelValueBean[vector.size()];
        vector.copyInto(shippingTypes);
        form.setShippingTypes(shippingTypes);
        
        vector = new Vector<LabelValueBean>();
        query = em.createQuery("from CustomerClass where siteId = :siteId order by custClassId");
        query.setParameter("siteId", form.getSiteId());
        iterator = query.getResultList().iterator();
        vector.add(new LabelValueBean("", ""));
        while (iterator.hasNext()) {
        	CustomerClass customerClass = (CustomerClass) iterator.next();
        	LabelValueBean bean = new LabelValueBean();
        	bean.setLabel(customerClass.getCustClassName());
        	bean.setValue(customerClass.getCustClassId().toString());
        	vector.add(bean);
        }
        LabelValueBean customerClasses[] = new LabelValueBean[vector.size()];
        vector.copyInto(customerClasses);
        form.setCustomerClasses(customerClasses);
        
        vector = new Vector<LabelValueBean>();
        query = em.createQuery("from Template where siteId = :siteId and templateName = :templateName order by templateId");
        query.setParameter("siteId", form.getSiteId());
        query.setParameter("templateName", Constants.TEMPLATE_BASIC);
        iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	Template template = (Template) iterator.next();
        	LabelValueBean bean = new LabelValueBean(template.getTemplateName(),Format.getLong(template.getTemplateId()));
        	vector.add(bean);
        }
        
        query = em.createQuery("from Template where siteId = :siteId and templateName != :templateName order by templateId");
        query.setParameter("siteId", form.getSiteId());
        query.setParameter("templateName", Constants.TEMPLATE_BASIC);
        iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	Template template = (Template) iterator.next();
        	LabelValueBean bean = new LabelValueBean(template.getTemplateName(),Format.getLong(template.getTemplateId()));
        	vector.add(bean);
        }
        LabelValueBean templates[] = new LabelValueBean[vector.size()];
        vector.copyInto(templates);
        form.setTemplates(templates);
        
     	String sql = "";
     	sql = "from		State state " +
              "left	join fetch state.country country " +
              "where	country.siteId = :siteId " +
              "order	by country.countryId, state.stateName";
     	query = em.createQuery(sql);
    	query.setParameter("siteId", form.getSiteId());
     	iterator = query.getResultList().iterator();
     	vector = new Vector<LabelValueBean>();
     	while (iterator.hasNext()) {
     		State state = (State) iterator.next();
     		LabelValueBean bean = new LabelValueBean(state.getStateName(), state.getStateCode());
     		vector.add(bean);
     	}
     	LabelValueBean states[] = new LabelValueBean[vector.size()];
     	vector.copyInto(states);
     	form.setStates(states);

        vector = new Vector<LabelValueBean>();
        query = em.createQuery("from Country where siteId = :siteId order by countryName");
        query.setParameter("siteId", form.getSiteId());
        iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	Country country = (Country) iterator.next();
        	LabelValueBean bean = new LabelValueBean();
        	bean.setLabel(country.getCountryName());
        	bean.setValue(country.getCountryCode());
        	vector.add(bean);
        }
        LabelValueBean countries[] = new LabelValueBean[vector.size()];
        vector.copyInto(countries);
        form.setCountries(countries);

		if (Format.isNullOrEmpty(form.getTabIndex())) {
			form.setTabIndex("0");
		}
		
		vector = new Vector<LabelValueBean>();
		vector.add(new LabelValueBean("", ""));
        query = em.createQuery("from PaymentGateway where siteId = :siteId order by paymentGatewayName");
        query.setParameter("siteId", form.getSiteId());
        iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	PaymentGateway paymentGateway = (PaymentGateway) iterator.next();
        	if (paymentGateway.getPaymentGatewayProvider().equals(PayPalEngine.class.getSimpleName())) {
        		continue;
        	}
        	LabelValueBean bean = new LabelValueBean(paymentGateway.getPaymentGatewayName(), Format.getLong(paymentGateway.getPaymentGatewayId()));
        	vector.add(bean);
        }
        LabelValueBean paymentGateways[] = new LabelValueBean[vector.size()];
        vector.copyInto(paymentGateways);
        form.setPaymentGateways(paymentGateways);
        
		vector = new Vector<LabelValueBean>();
		vector.add(new LabelValueBean("", ""));
        query = em.createQuery("from PaymentGateway where siteId = :siteId order by paymentGatewayName");
        query.setParameter("siteId", form.getSiteId());
        iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	PaymentGateway paymentGateway = (PaymentGateway) iterator.next();
        	if (!paymentGateway.getPaymentGatewayProvider().equals(PayPalEngine.class.getSimpleName()) && !paymentGateway.getPaymentGatewayProvider().equals(PayPalWebsitePaymentProHostedEngine.class.getSimpleName())) {
        		continue;
        	}
        	if (paymentGateway.getPaymentGatewayProvider().equals(PayPalWebsitePaymentProHostedEngine.class.getSimpleName())) {
        		continue;
        	}
        	LabelValueBean bean = new LabelValueBean(paymentGateway.getPaymentGatewayName(), Format.getLong(paymentGateway.getPaymentGatewayId()));
        	vector.add(bean);
        }
        LabelValueBean payPalPaymentGateways[] = new LabelValueBean[vector.size()];
        vector.copyInto(payPalPaymentGateways);
        form.setPayPalPaymentGateways(payPalPaymentGateways);
        
		vector = new Vector<LabelValueBean>();
        query = em.createQuery("from SiteProfileClass siteProfileClass where siteProfileClass.site.siteId = :siteId order by siteProfileClassName");
        query.setParameter("siteId", form.getSiteId());
        iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	SiteProfileClass siteProfileClass = (SiteProfileClass) iterator.next();
        	LabelValueBean bean = new LabelValueBean(siteProfileClass.getSiteProfileClassName(), Format.getLong(siteProfileClass.getSiteProfileClassId()));
        	vector.add(bean);
        }
        LabelValueBean siteProfileClasses[] = new LabelValueBean[vector.size()];
        vector.copyInto(siteProfileClasses);
        form.setSiteProfileClasses(siteProfileClasses);
        
		vector = new Vector<LabelValueBean>();
        query = em.createQuery("from SiteCurrencyClass siteCurrencyClass where siteCurrencyClass.site.siteId = :siteId order by siteCurrencyClassName");
        query.setParameter("siteId", form.getSiteId());
        iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	SiteCurrencyClass siteCurrencyClass = (SiteCurrencyClass) iterator.next();
        	LabelValueBean bean = new LabelValueBean(siteCurrencyClass.getSiteCurrencyClassName(), Format.getLong(siteCurrencyClass.getSiteCurrencyClassId()));
        	vector.add(bean);
        }
        LabelValueBean siteCurrencyClasses[] = new LabelValueBean[vector.size()];
        vector.copyInto(siteCurrencyClasses);
        form.setSiteCurrencyClasses(siteCurrencyClasses);
		
        /*
		form.getSiteProfiles()[0].setActive(true);
		form.getSiteCurrencies()[0].setActive(true);
		*/
		
		long time = (new Date()).getTime();
		form.setRandom(String.valueOf(time));
		
		Locale locales[] = Locale.getAvailableLocales();
		Arrays.sort(locales, new LocaleComparator());
		LabelValueBean beans[] = new LabelValueBean[locales.length];
		for (int i = 0; i < locales.length; i++) {
			String value = locales[i].getLanguage();
			if (!Format.isNullOrEmpty(locales[i].getCountry())) {
				value += "-" + locales[i].getCountry();
			}
			beans[i] = new LabelValueBean(locales[i].getDisplayName(), value);
		}
		form.setLocales(beans);
    }
    
    public ActionMessages validate(HttpServletRequest request, SiteDomainMaintActionForm form, Site site, boolean insertMode) throws Exception { 
    	ActionMessages errors = new ActionMessages();
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	if (!Format.isNullOrEmpty(form.getCategoryPageSize()) && !Format.isInt(form.getCategoryPageSize())) {
       		errors.add("categoryPageSize", new ActionMessage("error.int.invalid"));
       		form.setTabIndex(TABINDEX_GENERAL);
    	}
    	if (Format.isNullOrEmpty(form.getSiteName())) {
    		errors.add("siteName", new ActionMessage("error.string.required"));
    	}
    	if (!insertMode) {
	    	if (Format.isNullOrEmpty(form.getModuleDisplaySize())) {
	    		errors.add("moduleRecordNum", new ActionMessage("error.string.required"));
	    		form.setTabIndex(TABINDEX_TEMPLATE);
	    	}
	    	else {
	        	if (!Format.isInt(form.getModuleDisplaySize())) {
	           		errors.add("moduleRecordNum", new ActionMessage("error.int.invalid"));
	           		form.setTabIndex(TABINDEX_TEMPLATE);
	        	}
	    	}
    	}
    	
    	String sql = "from Site where 1 = 1 ";
    	if (!insertMode) {
    		sql += "and siteId != :siteId ";
    	}
    	Query query = em.createQuery(sql);
    	if (!insertMode) {
    		query.setParameter("siteId", form.getSiteId());
    	}
    	Iterator<?> iterator = query.getResultList().iterator();
    	while (iterator.hasNext()) {
    		Site otherSite = (Site) iterator.next();
    		Iterator<?> siteDomains = otherSite.getSiteDomains().iterator();
    		while (siteDomains.hasNext()) {
    			SiteDomain siteDomain = (SiteDomain) siteDomains.next();
    			if (siteDomain.getSiteDomainId().toString().equals(form.getSiteDomainId())) {
    				continue;
    			}
    			if (siteDomain.getSiteDomainPrefix().equals(form.getSiteDomainPrefix())) {
        			errors.add("siteDomainPrefix", new ActionMessage("error.prefix.duplicate"));
    			}
    		}
    	}
    	
    	MessageResources resources = this.getResources(request);
    	SiteProfileForm siteProfileForms[] = form.getSiteProfiles();
    	for (int i = 0; i < siteProfileForms.length; i++) {
    		SiteProfileForm siteProfileForm = siteProfileForms[i];
    		if (Format.isNullOrEmpty(siteProfileForm.getSiteProfileClassId())) {
    			siteProfileForm.setSiteProfileClassIdError(resources.getMessage("error.string.required"));
    			errors.add("dummy", new ActionMessage("error.string.required"));
    			form.setTabIndex(TABINDEX_SITE);
    		}
    		else {
    			for (int j = 0; j < i; j++) {
    				if (siteProfileForms[j].getSiteProfileClassId().equals(siteProfileForm.getSiteProfileClassId())) {
    					siteProfileForm.setSiteProfileClassIdError(resources.getMessage("error.record.duplicate"));
    					errors.add("dummy", new ActionMessage("error.record.duplicate"));
    	    			form.setTabIndex(TABINDEX_SITE);
    				}
    			}
    		}
        	if (Format.isNullOrEmpty(siteProfileForm.getSeqNum())) {
        		siteProfileForm.setSeqNumError(resources.getMessage("error.string.required"));
        		errors.add("dummy", new ActionMessage("error.string.required"));
    			form.setTabIndex(TABINDEX_SITE);
        	}
        	else {
            	if (!Format.isFloat(siteProfileForm.getSeqNum())) {
            		siteProfileForm.setSeqNumError(resources.getMessage("error.float.invalid"));
            		errors.add("dummy", new ActionMessage("error.float.invalid"));
        			form.setTabIndex(TABINDEX_SITE);
            	}
        	}
    	}
    	SiteCurrencyForm siteCurrencyForms[] = form.getSiteCurrencies();
    	for (int i = 0; i < siteCurrencyForms.length; i++) {
    		SiteCurrencyForm siteCurrencyForm = siteCurrencyForms[i];
    		if (Format.isNullOrEmpty(siteCurrencyForm.getSiteCurrencyClassId())) {
    			siteCurrencyForm.setSiteCurrencyClassIdError(resources.getMessage("error.string.required"));
    			errors.add("dummy", new ActionMessage("error.string.required"));
    			form.setTabIndex(TABINDEX_SITE);
    		}
    		else {
    			for (int j = 0; j < i; j++) {
    				if (siteCurrencyForms[j].getSiteCurrencyClassId().equals(siteCurrencyForm.getSiteCurrencyClassId())) {
    					siteCurrencyForm.setSiteCurrencyClassIdError(resources.getMessage("error.record.duplicate"));
    					errors.add("dummy", new ActionMessage("error.record.duplicate"));
    	    			form.setTabIndex(TABINDEX_SITE);
    				}
    			}
    		}
        	if (Format.isNullOrEmpty(siteCurrencyForm.getSeqNum())) {
        		siteCurrencyForm.setSeqNumError(resources.getMessage("error.string.required"));
        		errors.add("dummy", new ActionMessage("error.string.required"));
    			form.setTabIndex(TABINDEX_SITE);
        	}
        	else {
            	if (!Format.isFloat(siteCurrencyForm.getSeqNum())) {
            		siteCurrencyForm.setSeqNumError(resources.getMessage("error.float.invalid"));
            		errors.add("dummy", new ActionMessage("error.float.invalid"));
        			form.setTabIndex(TABINDEX_SITE);
            	}
        	}
        	if (Format.isNullOrEmpty(siteCurrencyForm.getExchangeRate())) {
        		siteCurrencyForm.setExchangeRateError(resources.getMessage("error.string.required"));
        		errors.add("dummy", new ActionMessage("error.string.required"));
    			form.setTabIndex(TABINDEX_SITE);
        	}
        	else {
            	if (!Format.isFloat(siteCurrencyForm.getExchangeRate())) {
            		siteCurrencyForm.setExchangeRateError(resources.getMessage("error.float.invalid"));
            		errors.add("dummy", new ActionMessage("error.float.invalid"));
        			form.setTabIndex(TABINDEX_SITE);
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
        map.put("addProfile", "addProfile");
        map.put("removeProfile", "removeProfile");
        map.put("addCurrency", "addCurrency");
        map.put("removeCurrency", "removeCurrency");
        map.put("removeImage", "removeImage");
        map.put("uploadImage", "uploadImage");
        map.put("resetLanguageImage", "resetLanguageImage");
        map.put("translate", "translate");
        return map;
    }
    
    class LocaleComparator implements Comparator<Locale> {
		public int compare(Locale arg0, Locale arg1) {
			Locale locale0 = (Locale) arg0;
			Locale locale1 = (Locale) arg1;
			return locale0.getDisplayName().compareTo(locale1.getDisplayName());
		}	
    }
}
