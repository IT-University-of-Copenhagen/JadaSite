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

package com.jada.myaccount.login;

import com.jada.content.ContentBean;
import com.jada.content.ContentLookupDispatchAction;
import com.jada.content.frontend.FrontendBaseAction;
import com.jada.dao.OrderHeaderDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.Language;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.Site;
import com.jada.order.cart.ShoppingCart;
import com.jada.system.Languages;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.IdSecurity;

import java.util.List;
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

public class MyAccountLoginAction extends FrontendBaseAction {
    Logger logger = Logger.getLogger(MyAccountLoginAction.class);

    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	ContentBean contentBean = getContentBean(request);
    	Language language = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getLanguage();
    	MyAccountLoginActionForm form = (MyAccountLoginActionForm) actionForm;
    	ActionForward actionForward = null;
     	String action = request.getParameter("action");
    	if (action != null) {
    		if (action.equals("logout")) {
    			form.addMessage("msg", Languages.getLangTranValue(language.getLangId(), "content.text.myaccount.logout.successful"));
    		}
    		if (action.equals("forgot")) {
    			form.addMessage("msg", Languages.getLangTranValue(language.getLangId(), "content.text.myaccount.forgot.successful"));
    		}
    	}
    	
    	Customer customer = getCustomer(request);
    	if (customer != null) {
    		mergeOrder(request, form, customer);
    		String target = request.getParameter("target");
    		if (target != null) {
            	ActionForward base = actionMapping.findForward(target);
            	String path = base.getPath();
            	path += "&prefix=" + contentBean.getSiteDomain().getSiteDomainPrefix() + 
    					"&langName=" + contentBean.getContentSessionKey().getLangName() + 
    					"&currencyCode=" + contentBean.getContentSessionKey().getSiteCurrencyClassName();
            	actionForward = new ActionForward();
            	actionForward.setPath(path);
            	actionForward.setRedirect(base.getRedirect());
            	return actionForward;
    		}
    		
    		String url = request.getParameter("url");
    		if (url != null) {
            	actionForward = new ActionForward();
            	actionForward.setPath(url);
            	actionForward.setRedirect(true);
            	return actionForward;
    		}
    		
    		actionForward = actionMapping.findForward("loginSuccess");
    	}
    	else {
    		actionForward = actionMapping.findForward("success");
    	}
    	createEmptySecureTemplateInfo(request);
        return actionForward;
    }

    public ActionForward login(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getSiteDomain().getSite();
    	Language language = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getLanguage();
    	MyAccountLoginActionForm form = (MyAccountLoginActionForm) actionForm;
    	createEmptySecureTemplateInfo(request);
    	validate(form, language.getLangId());
    	if (form.hasMessage()) {
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
    	}
    	
    	IdSecurity idSecurity = new IdSecurity(site, form.getCustEmail(), true);
    	if (idSecurity.isSuspened()) {
    		idSecurity.fail();
    		form.setCustPassword("");
        	form.addMessage("login", Languages.getLangTranValue(language.getLangId(), "content.error.login.suspended.temporary", String.valueOf(Constants.ID_SUSPEND_TIME / 1000 / 60)));
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
    	}
    	
    	char singleCheckout = contentBean.getSiteDomain().getSite().getSingleCheckout();
    	
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String sql = "from   Customer customer " + 
					 "where  customer.siteDomain.siteDomainId = :siteDomainId " +
					 "and    custEmail = :custEmail";
        Query query = em.createQuery(sql);
        if (singleCheckout == Constants.VALUE_YES) {
            query.setParameter("siteDomainId", contentBean.getSiteDomain().getSite().getSiteDomainDefault().getSiteDomainId());
        }
        else {
        	query.setParameter("siteDomainId", contentBean.getSiteDomain().getSiteDomainId());
        }
        query.setParameter("custEmail", form.getCustEmail());
        
        List<?> list = query.getResultList();
        if (list.size() == 0) {
        	idSecurity.fail();
        	form.setCustPassword("");
        	form.addMessage("login", Languages.getLangTranValue(language.getLangId(), "content.error.login.invalid"));
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
        }
        
        Customer customer = (Customer) list.get(0);
        String password = AESEncoder.getInstance().decode(customer.getCustPassword());
        if (!password.equals(form.getCustPassword())) {
        	idSecurity.fail();
        	form.setCustPassword("");
        	form.addMessage("login", Languages.getLangTranValue(language.getLangId(), "content.error.login.invalid"));
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
        }
        idSecurity.reset();
        
        ContentLookupDispatchAction.setCustId(request, customer.getCustId());
        // to init in order to avoid lazy initialization.
        customer.getRecCreateDatetime();
        mergeOrder(request, form, customer);
        
        /*
        if (!Format.isNullOrEmpty(form.getUrl())) {
        	response.sendRedirect(form.getUrl());
        	return null;
        }
        */
        
        ActionForward actionForward = null;
        String target = form.getTarget();
        if (!Format.isNullOrEmpty(target)) {
        	ActionForward base = actionMapping.findForward(target);
        	String path = base.getPath();
        	path += "&prefix=" + contentBean.getSiteDomain().getSiteDomainPrefix() + 
					"&langName=" + contentBean.getContentSessionKey().getLangName() + 
					"&currencyCode=" + contentBean.getContentSessionKey().getSiteCurrencyClassName();
        	actionForward = new ActionForward();
        	actionForward.setPath(path);
        	actionForward.setRedirect(base.getRedirect());
        	return actionForward;
        }
        
        String url = form.getUrl();
        if (!Format.isNullOrEmpty(url)) {
        	response.sendRedirect(url);
        	return null;
        }
        
        actionForward = actionMapping.findForward("loginSuccess");
        return actionForward;
    }
    
    private void mergeOrder(HttpServletRequest request, MyAccountLoginActionForm form, Customer customer) throws Exception {
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getSiteDomain().getSite();
        if (contentBean.isShoppingCartEnabled()) {
    		ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
    		shoppingCart.initCustomer(customer, contentBean);
    		OrderHeader orderHeader = null;
    		String orderNum = form.getOrderNum();
    		if (!Format.isNullOrEmpty(orderNum)) {
    			orderHeader = OrderHeaderDAO.load(site.getSiteId(), orderNum);
    			if (!orderHeader.getCustomer().getCustId().equals(customer.getCustId())) {
    				orderHeader = null;
    			}
    		}
    		else {
	            orderHeader = shoppingCart.locateAbundentOrder();
    		}
            if (orderHeader != null) {
            	shoppingCart.mergeOrder(orderHeader.getOrderHeaderId(), contentBean);
            }
        }
    }

    public void validate(MyAccountLoginActionForm form, Long langId) { 
    	if (Format.isNullOrEmpty(form.getCustEmail())) {
    		form.addMessage("custEmail", Languages.getLangTranValue(langId, "content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustPassword())) {
    		form.addMessage("custPassword", Languages.getLangTranValue(langId, "content.error.string.required"));
    	}
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        map.put("login", "login");
        return map;
    }
}