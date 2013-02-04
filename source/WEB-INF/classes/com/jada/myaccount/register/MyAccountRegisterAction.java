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

package com.jada.myaccount.register;

import com.jada.content.ContentBean;
import com.jada.content.ContentLookupDispatchAction;
import com.jada.content.frontend.FrontendBaseAction;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.CustomerAddress;
import com.jada.jpa.entity.CustomerClass;
import com.jada.jpa.entity.Language;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomain;
import com.jada.system.Languages;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;
import com.jada.xml.site.SiteParamBean;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.persistence.Query;
import javax.persistence.EntityManager;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

public class MyAccountRegisterAction extends FrontendBaseAction {
    Logger logger = Logger.getLogger(MyAccountRegisterAction.class);

    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
		MyAccountRegisterActionForm form = (MyAccountRegisterActionForm) actionForm;
		form.setUrl(request.getParameter("url"));
		setCaptcha(form, request);			
		ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward update(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
		ContentBean contentBean = getContentBean(request);
    	Language language = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getLanguage();
		MyAccountRegisterActionForm form = (MyAccountRegisterActionForm) actionForm;
		
		setCaptcha(form, request);			
		validate(form, request, language.getLangId());
    	if (form.hasMessage()) {
			form.setCustPassword("");
			form.setCustPassword1("");
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
    	}
    	
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	Long defaultSiteDomainId = contentBean.getSiteDomain().getSite().getSiteDomainDefault().getSiteDomainId();
    	char singleCheckout = contentBean.getSiteDomain().getSite().getSingleCheckout();
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	
    	String sql = "from   Customer customer " + 
    				 "where  customer.siteDomain.siteDomainId = :siteDomainId " +
    				 "and    custEmail = :custEmail";
        Query query = em.createQuery(sql);
        if (singleCheckout == Constants.VALUE_YES) {
            query.setParameter("siteDomainId", defaultSiteDomainId);
        }
        else {
        	query.setParameter("siteDomainId", contentBean.getSiteDomain().getSiteDomainId());
        }
        query.setParameter("custEmail", form.getCustEmail1());
        if (query.getResultList().size() > 0) {
        	form.addMessage("custEmail1", Languages.getLangTranValue(language.getLangId(), "content.error.email.duplicate"));
        }
        
    	sql = "from   Customer customer " + 
    		  "where  customer.siteDomain.siteDomainId = :siteDomainId " +
    		  "and    custPublicName = :custPublicName";
        query = em.createQuery(sql);
        if (singleCheckout == Constants.VALUE_YES) {
            query.setParameter("siteDomainId", defaultSiteDomainId);
        }
        else {
        	query.setParameter("siteDomainId", contentBean.getSiteDomain().getSiteDomainId());
        }
        query.setParameter("custPublicName", form.getCustPublicName());
        if (query.getResultList().size() > 0) {
        	form.addMessage("custPublicName", Languages.getLangTranValue(language.getLangId(), "content.error.publicName.duplicate"));
        }
    	if (form.hasMessage()) {
            ActionForward actionForward = actionMapping.findForward("error");
			form.setCustPassword("");
			form.setCustPassword1("");
            return actionForward;
    	}
    	
    	sql = "from CustomerClass customerClass where siteId = :siteId and customerClass.systemRecord = 'Y'";
    	query = em.createQuery(sql);
    	query.setParameter("siteId", site.getSiteId());
    	CustomerClass customerClass = (CustomerClass) query.getSingleResult();

    	
    	Customer customer = new Customer();
    	customer.setSite(site);
		if (singleCheckout == Constants.VALUE_YES) {
			SiteDomain siteDomain = (SiteDomain) em.find(SiteDomain.class, defaultSiteDomainId);
			customer.setSiteDomain(siteDomain);
		}
		else {
			customer.setSiteDomain(contentBean.getSiteDomain());
		}
    	customer.setCustPublicName(form.getCustPublicName());
    	customer.setCustEmail(form.getCustEmail1());
    	customer.setCustPassword(AESEncoder.getInstance().encode(form.getCustPassword()));
    	customer.setCustSource(Constants.CUSTOMER_SOURCE_REGISTER);
    	customer.setCustSourceRef("");
    	customer.setActive(Constants.ACTIVE_YES);
        customer.setRecUpdateBy(Constants.USERNAME_CUSTOMER);
        customer.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        customer.setRecCreateBy(Constants.USERNAME_CUSTOMER);
        customer.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        customer.setCustomerClass(customerClass);
        
        CustomerAddress customerAddress = new CustomerAddress();
        customer.setCustAddress(customerAddress);
        customer.getCustAddresses().add(customerAddress);
        customerAddress.setCustUseAddress(Constants.CUST_ADDRESS_USE_OWN);
        customerAddress.setCustAddressType(Constants.CUSTOMER_ADDRESS_CUST);
        customerAddress.setRecUpdateBy(Constants.USERNAME_SYSTEM);
        customerAddress.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        customerAddress.setRecCreateBy(Constants.USERNAME_SYSTEM);
        customerAddress.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        em.persist(customerAddress);
             
        em.persist(customer);

        ContentLookupDispatchAction.setCustId(request, customer.getCustId());
		form.setCustPassword("");
		form.setCustPassword1("");
		form.addMessage("message", Languages.getLangTranValue(language.getLangId(), "content.text.information.updated")); 

        if (!Format.isNullOrEmpty(form.getUrl())) {
        	response.sendRedirect(form.getUrl());
        	return null;
        }
        ActionForward forward = actionMapping.findForward("registerSuccess") ;
        return forward;
    }

    public void validate(MyAccountRegisterActionForm form, HttpServletRequest request, Long langId) throws Throwable { 
    	if (Format.isNullOrEmpty(form.getCustEmail1())) {
    		form.addMessage("custEmail1", Languages.getLangTranValue(langId, "content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustEmail2())) {
    		form.addMessage("custEmail2", Languages.getLangTranValue(langId, "content.error.string.required"));
    	}
     	if (!form.getCustEmail1().equals(form.getCustEmail2())) {
     		form.addMessage("emailMatch", Languages.getLangTranValue(langId, "content.error.email.nomatch"));
    	}
    	if (Format.isNullOrEmpty(form.getCustPublicName())) {
    		form.addMessage("custPublicName", Languages.getLangTranValue(langId, "content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustPassword())) {
    		form.addMessage("custPassword", Languages.getLangTranValue(langId, "content.error.string.required"));
    	}
    	else {
    		if (!Utility.isValidPassword(form.getCustPassword())) {
    			form.addMessage("custPassword", Languages.getLangTranValue(langId, "content.error.password.invalidRule"));
    		}
    	}
    	if (Format.isNullOrEmpty(form.getCustPassword1())) {
    		form.addMessage("custPassword1", Languages.getLangTranValue(langId, "content.error.string.required"));
    	}
    	else {
    		if (!Utility.isValidPassword(form.getCustPassword1())) {
    			form.addMessage("custPassword1", Languages.getLangTranValue(langId, "content.error.password.invalidRule"));
    		}
    	}
    	if (!form.getCustPassword().equals(form.getCustPassword1())) {
    		form.addMessage("custPasswordNoMatch", Languages.getLangTranValue(langId, "content.error.password.nomatch"));
    	}

    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	SiteParamBean siteParamBean = new SiteParamBean();
		if (!Format.isNullOrEmpty(site.getSiteParam())) {
			siteParamBean = (SiteParamBean) Utility.joxUnMarshall(SiteParamBean.class, site.getSiteParam());
		}
		if (!Format.isNullOrEmpty(siteParamBean.getEnableCaptcha()) && "Y".equals(siteParamBean.getEnableCaptcha())) {
			String captchaPrivateKey = AESEncoder.getInstance().decode(siteParamBean.getCaptchaPrivateKey());   	
            String remoteAddr = request.getRemoteAddr();
            String challenge = request.getParameter("recaptcha_challenge_field");
            String uresponse = request.getParameter("recaptcha_response_field");
	    	if (Format.isNullOrEmpty(uresponse)) {
	    		form.addMessage("recaptchaUserResponse", Languages.getLangTranValue(langId, "content.error.captcha.required"));
	    	}
	    	else {
	            ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
	            reCaptcha.setPrivateKey(captchaPrivateKey);
	            ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
	            if (!reCaptchaResponse.isValid()) 
	            	form.addMessage("recaptchaUserResponse", reCaptchaResponse.getErrorMessage());
	    	}
		}
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        map.put("update", "update");
        return map;
    }
    
    public void setCaptcha(MyAccountRegisterActionForm form, HttpServletRequest request)
    		throws Throwable {		
		ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	SiteParamBean siteParamBean = new SiteParamBean();
		if (!Format.isNullOrEmpty(site.getSiteParam())) {
			siteParamBean = (SiteParamBean) Utility.joxUnMarshall(SiteParamBean.class, site.getSiteParam());
		}
		String enableCaptcha= siteParamBean.getEnableCaptcha();
		if (!Format.isNullOrEmpty(enableCaptcha)) {
			form.setEnableCaptcha(enableCaptcha);
			if (!Format.isNullOrEmpty(siteParamBean.getCaptchaPublicKey())) {
				form.setCaptchaPublicKey(AESEncoder.getInstance().decode(siteParamBean.getCaptchaPublicKey()));
			}
			if (!Format.isNullOrEmpty(siteParamBean.getCaptchaPrivateKey())) {		
				form.setCaptchaPrivateKey(AESEncoder.getInstance().decode(siteParamBean.getCaptchaPrivateKey()));
			}	
		}
        return;
    }
    
}