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

package com.jada.myaccount.identity;

import com.jada.content.ContentBean;
import com.jada.content.ContentLookupDispatchAction;
import com.jada.dao.CustomerDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.Language;
import com.jada.myaccount.portal.MyAccountPortalAction;
import com.jada.system.Languages;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class MyAccountIdentityAction extends MyAccountPortalAction {
    Logger logger = Logger.getLogger(MyAccountIdentityAction.class);

    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	MyAccountIdentityActionForm form = (MyAccountIdentityActionForm) actionForm;
    	init(request, form);
    	ActionForward actionForward = actionMapping.findForward("success");
    	Customer customer = getCustomer(request);
		String siteId = getContentBean(request).getContentSessionKey().getSiteId();
    	customer = CustomerDAO.load(siteId, customer.getCustId());
    	form.setCustEmail(customer.getCustEmail());
    	form.setCustPublicName(customer.getCustPublicName());
		createEmptySecureTemplateInfo(request);
        return actionForward;
    }

    public ActionForward update(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ContentBean contentBean = getContentBean(request);
		String siteId = contentBean.getContentSessionKey().getSiteId();
    	Language language = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getLanguage();
		createEmptySecureTemplateInfo(request);
		MyAccountIdentityActionForm form = (MyAccountIdentityActionForm) actionForm;
    	init(request, form);
    	validate(form, siteId, request, language.getLangId());
    	if (form.hasMessage()) {
    		form.setCustPassword("");
    		form.setCustPassword1("");
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
    	}
    	
		Customer customer = CustomerDAO.load(siteId, getCustomer(request).getCustId());
		
        if (form.getCustPassword().trim().length() > 0) {
        	customer.setCustPassword(AESEncoder.getInstance().encode(form.getCustPassword()));
        }
        customer.setCustPublicName(form.getCustPublicName());
        customer.setRecUpdateBy(Constants.USERNAME_CUSTOMER);
        customer.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        em.persist(customer);
        ContentLookupDispatchAction.setCustId(request, customer.getCustId());
		form.setCustPassword("");
		form.setCustPassword1("");
		form.addMessage("message", Languages.getLangTranValue(language.getLangId(), "content.text.information.updated"));

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public void validate(MyAccountIdentityActionForm form, String siteId, HttpServletRequest request, Long langId) throws Exception { 
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	if (Format.isNullOrEmpty(form.getCustEmail())) {
    		form.addMessage("custEmail", Languages.getLangTranValue(langId, "content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustPublicName())) {
    		form.addMessage("custPublicName", Languages.getLangTranValue(langId, "content.error.string.required"));
    	}
    	else {
    		String sql = "select  count(*) " + 
    					 "from    Customer " + 
    					 "where   siteId = :siteId " + 
    					 "and     custPublicName = :custPublicName " +
    					 "and     custId != :custId";
    		Query query = em.createQuery(sql);
    		query.setParameter("siteId", siteId);
    		query.setParameter("custPublicName", form.getCustPublicName());
    		query.setParameter("custId", getCustomer(request).getCustId());
    		Long count = (Long) query.getSingleResult();
    		if (count > 0) {
    			form.addMessage("custPublicName", Languages.getLangTranValue(langId, "content.error.publicName.duplicate"));
    		}
    	}
    	if (form.getCustPassword().trim().length() > 0 || form.getCustPassword1().trim().length() > 0) {
        	if (Format.isNullOrEmpty(form.getCustPassword())) {
        		form.addMessage("custPassword", Languages.getLangTranValue(langId, "content.error.string.required"));
        		return;
        	}
        	else {
        		if (!Utility.isValidPassword(form.getCustPassword())) {
        			form.addMessage("custPassword", Languages.getLangTranValue(langId, "content.error.password.invalidRule"));
            		return;
        		}
        	}
        	if (Format.isNullOrEmpty(form.getCustPassword1())) {
        		form.addMessage("custPassword1", Languages.getLangTranValue(langId, "content.error.string.required"));
        		return;
        	}
        	else {
        		if (!Utility.isValidPassword(form.getCustPassword1())) {
        			form.addMessage("custPassword1", Languages.getLangTranValue(langId, "content.error.password.invalidRule"));
            		return;
        		}
        	}
	    	if (!form.getCustPassword().equals(form.getCustPassword1())) {
	    		form.addMessage("custPasswordNoMatch", Languages.getLangTranValue(langId, "content.error.password.nomatch"));
	    		return;
	    	}
    	}
    	return;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        map.put("update", "update");
        return map;
    }
}