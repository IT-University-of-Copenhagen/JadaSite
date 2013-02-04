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

package com.jada.myaccount.forgot;

import com.jada.content.ContentBean;
import com.jada.content.frontend.FrontendBaseAction;
import com.jada.dao.SiteDomainDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.Language;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.SiteDomainLanguage;
import com.jada.system.Languages;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Mailer;
import com.jada.util.Utility;
import com.jada.xml.site.SiteDomainParamBean;
import com.jada.xml.site.SiteParamBean;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import javax.mail.MessagingException;
import javax.persistence.Query;
import javax.persistence.EntityManager;

public class MyAccountForgotAction extends FrontendBaseAction {
    Logger logger = Logger.getLogger(MyAccountForgotAction.class);

    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	setCaptcha((MyAccountForgotActionForm) actionForm, request);
    	ActionForward actionForward = null;
    	actionForward = actionMapping.findForward("success");
    	createEmptySecureTemplateInfo(request);
        return actionForward;
    }

    public ActionForward reset(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
		ContentBean contentBean = getContentBean(request);
    	Language language = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getLanguage();
    	MyAccountForgotActionForm form = (MyAccountForgotActionForm) actionForm;
    	setCaptcha(form, request);
    	createEmptySecureTemplateInfo(request);
    	validate(form, request, language.getLangId());
    	if (form.hasMessage()) {
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
    	}
    	
    	Long defaultSiteDomainId = contentBean.getSiteDomain().getSite().getSiteDomainDefault().getSiteDomainId();
    	char singleCheckout = contentBean.getSiteDomain().getSite().getSingleCheckout();
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
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
        query.setParameter("custEmail", form.getCustEmail());
        
        List<?> list = query.getResultList();
        if (list.size() == 0) {
        	form.addMessage("forgot", Languages.getLangTranValue(language.getLangId(), "content.text.myaccount.forgot.invalid"));
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
        }
        Customer customer = (Customer) list.get(0);
        
       	MessageResources resources = this.getResources(request);
        Mailer mailer = new Mailer(site);
        SiteDomain siteDomain = contentBean.getContentSessionBean().getSiteDomain();
        SiteDomainLanguage siteDomainLanguage = null;
        for (SiteDomainLanguage l : siteDomain.getSiteDomainLanguages()) {
        	if (l.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
        		siteDomainLanguage = l;
        		break;
        	}	
        }
        SiteDomainParamBean siteDomainParamBean = SiteDomainDAO.getSiteDomainParamBean(siteDomain.getSiteDomainLanguage(), siteDomainLanguage);
        String mailFrom = siteDomainParamBean.getMailFromPwdReset();
        String subject = siteDomainParamBean.getSubjectPwdReset();
        String body = resources.getMessage("message.mail.pwdReset.body", AESEncoder.getInstance().decode(customer.getCustPassword()));
        if (mailFrom == null) {
        	mailFrom = "";
        }
        if (subject == null) {
        	subject = "";
        }
        
        try {
        	mailer.sendMail(mailFrom, form.getCustEmail(), subject, body);
        }
        catch (MessagingException e) {
        	form.addMessage("forgot", Languages.getLangTranValue(language.getLangId(), "content.error.mail.send"));
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
        }

        ActionForward template = actionMapping.findForward("forgotSuccess");
        ActionForward forward = new ActionForward();
        forward.setPath(template.getPath() + 
        				"&prefix=" + siteDomain.getSiteDomainPrefix() +
        				"&langName=" + contentBean.getContentSessionKey().getLangName());
        forward.setRedirect(true);
        return forward;
    }
    
    public void setCaptcha(MyAccountForgotActionForm form, HttpServletRequest request) throws Throwable {		
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

    public void validate(MyAccountForgotActionForm form, HttpServletRequest request, Long langId) throws Exception { 
    	if (Format.isNullOrEmpty(form.getCustEmail())) {
    		form.addMessage("custEmail", Languages.getLangTranValue(langId, "content.error.string.required"));
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
        map.put("reset", "reset");
        return map;
    }
}