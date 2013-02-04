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

package com.jada.content.checkout;

import com.jada.content.ContentBean;
import com.jada.content.ContentLookupDispatchAction;
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
import com.jada.util.JSONEscapeObject;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import javax.persistence.Query;
import javax.persistence.EntityManager;

public class ShoppingCartLoginAction extends ShoppingCartBaseAction {
    Logger logger = Logger.getLogger(ShoppingCartLoginAction.class);

    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
    	ActionForward actionForward = null;
    	if (getCustomer(request) != null) {
    		actionForward = actionMapping.findForward("loginSuccess");
    	}
    	else {
    		actionForward = actionMapping.findForward("success");
    	}
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
		createEmptySecureTemplateInfo(request);
		ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
		shoppingCart.setPayPalOrder(false);
		if (form.getCash() != null && form.getCash().equals(String.valueOf(Constants.VALUE_YES))) {
			shoppingCart.setCashPaymentOrder(true);
			shoppingCart.setCreditCardOrder(false);
		}
		else {
			shoppingCart.setCashPaymentOrder(false);
			shoppingCart.setCreditCardOrder(true);
		}
		ActionMessages messages = new ActionMessages();
		this.initCartInfo(form, site, shoppingCart, request, messages);
        return actionForward;
    }
    
    public ActionForward login(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
    	ActionMessages messages = new ActionMessages();
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
        ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
    	char singleCheckout = contentBean.getSiteDomain().getSite().getSingleCheckout();
		createEmptySecureTemplateInfo(request);
		
		JSONEscapeObject jsonResult = new JSONEscapeObject();
    	Language language = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getLanguage();
		validateLogin(jsonResult, form, contentBean);
		if (jsonResult.length() > 0) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			this.streamWebService(response, jsonResult.toString());
			return null;
		}
		
    	IdSecurity idSecurity = new IdSecurity(site, form.getCustEmail(), true);
    	if (idSecurity.isSuspened()) {
    		idSecurity.fail();
        	form.setCustPassword("");
    		this.initCartInfo(form, site, shoppingCart, request, messages);
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
    		jsonResult.addKeyValue("login", Languages.getLangTranValue(language.getLangId(), "content.error.login.suspended.temporary", String.valueOf(Constants.ID_SUSPEND_TIME / 1000 / 60)));
 			this.streamWebService(response, jsonResult.toString());
			return null;
    	}

		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String sql = "from 		Customer customer " +
					 "where 	customer.siteDomain.siteDomainId = :siteDomainId " +
					 "and 		custEmail = :custEmail " +
					 "and		custPassword = :custPassword ";
        Query query = em.createQuery(sql);
        if (singleCheckout == Constants.VALUE_YES) {
        	Long defaultSiteDomainId = contentBean.getSiteDomain().getSite().getSiteDomainDefault().getSiteDomainId();
        	query.setParameter("siteDomainId", defaultSiteDomainId);
        }
        else {
        	query.setParameter("siteDomainId", contentBean.getSiteDomain().getSiteDomainId());
        }
        query.setParameter("custEmail", form.getCustEmail());
        query.setParameter("custPassword", AESEncoder.getInstance().encode(form.getCustPassword()));
        
        List<?> list = query.getResultList();
        if (list.size() == 0) {
        	idSecurity.fail();
        	form.setCustPassword("");
    		this.initCartInfo(form, site, shoppingCart, request, messages);
    		jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
    		jsonResult.addKeyValue("login", Languages.getLangTranValue(language.getLangId(), "content.error.login.invalid"));
			this.streamWebService(response, jsonResult.toString());
			return null;
        }
        idSecurity.reset();
        
        Customer customer = (Customer) list.get(0);
        if (!customer.getActive().equals(Constants.VALUE_YES)) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			jsonResult.addKeyValue("login", Languages.getLangTranValue(language.getLangId(), "content.error.login.suspended"));
			// TODO - Handle quote lock
			/*
            if (shoppingCart.isShippingQuoteLock()) {
            	actionForward = actionMapping.findForward("quote");
                return actionForward;
            }
            */
			this.streamWebService(response, jsonResult.toString());
			return null;
        }
        ContentLookupDispatchAction.setCustId(request, customer.getCustId());
        shoppingCart.initCustomer(customer, contentBean);
        shoppingCart.setShippingPickUp(shoppingCart.isEstimatePickUp());
        shoppingCart.setShippingMethod(null);
        boolean isCash = false;
        if (!Format.isNullOrEmpty(form.getCash())) {
        	if (form.getCash().equals(String.valueOf(Constants.VALUE_YES))) {
        		isCash = true;
        	}
        }
        shoppingCart.setCashPaymentOrder(isCash);
        
        OrderHeader orderHeader = shoppingCart.locateAbundentOrder();
        if (orderHeader != null) {
        	shoppingCart.mergeOrder(orderHeader.getOrderHeaderId(), contentBean);
        }
        
        // TODO - handle quote lock
        /*
        ActionForward actionForward = new ActionForward();
        if (shoppingCart.isShippingQuoteLock()) {
        	actionForward = actionMapping.findForward("quote");
            return actionForward;
        }
        */
        
		this.initCartInfo(form, site, shoppingCart, request, messages);
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		this.streamWebService(response, jsonResult.toString());
		return null;
    }


    public void validateLogin(JSONEscapeObject jsonResult, ShoppingCartActionForm form, ContentBean contentBean) throws Exception {
    	Language language = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getLanguage();
    	if (Format.isNullOrEmpty(form.getCustEmail())) {
    		jsonResult.put("custEmail", Languages.getLangTranValue(language.getLangId(), "content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustPassword())) {
    		jsonResult.put("custPassword", Languages.getLangTranValue(language.getLangId(), "content.error.string.required"));
    	}
    }

/*
    public ActionForward login(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
        ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
    	char singleCheckout = contentBean.getSiteDomain().getSite().getSingleCheckout();
		createEmptySecureTemplateInfo(request);
    	ActionMessages messages = validate((ShoppingCartActionForm) actionForm);
    	if (messages.size() > 0) {
    		this.initCartInfo(form, site, shoppingCart, request, messages);
			saveMessages(request, messages);
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
    	}
    	
    	IdSecurity idSecurity = new IdSecurity(site, form.getCustEmail(), true);
    	if (idSecurity.isSuspened()) {
    		idSecurity.fail();
        	form.setCustPassword("");
    		this.initCartInfo(form, site, shoppingCart, request, messages);
        	messages.add("login", new ActionMessage("content.error.login.suspended.temporary", String.valueOf(Constants.ID_SUSPEND_TIME / 1000 / 60)));
			saveMessages(request, messages);
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
    	}

		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String sql = "from 		Customer customer " +
					 "where 	customer.siteDomain.siteDomainId = :siteDomainId " +
					 "and 		custEmail = :custEmail " +
					 "and		custPassword = :custPassword ";
        Query query = em.createQuery(sql);
        if (singleCheckout == Constants.VALUE_YES) {
        	Long defaultSiteDomainId = contentBean.getSiteDomain().getSite().getSiteDomainDefault().getSiteDomainId();
        	query.setParameter("siteDomainId", defaultSiteDomainId);
        }
        else {
        	query.setParameter("siteDomainId", contentBean.getSiteDomain().getSiteDomainId());
        }
        query.setParameter("custEmail", form.getCustEmail());
        query.setParameter("custPassword", AESEncoder.getInstance().encode(form.getCustPassword()));
        
        List<?> list = query.getResultList();
        if (list.size() == 0) {
        	idSecurity.fail();
        	form.setCustPassword("");
    		this.initCartInfo(form, site, shoppingCart, request, messages);
        	messages.add("login", new ActionMessage("content.error.login.invalid"));
			saveMessages(request, messages);
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
        }
        idSecurity.reset();
        
        Customer customer = (Customer) list.get(0);
        if (!customer.getActive().equals(Constants.VALUE_YES)) {
        	messages.add("login", new ActionMessage("content.error.login.suspended"));
			saveMessages(request, messages);
            ActionForward actionForward = null;
            if (shoppingCart.isShippingQuoteLock()) {
            	actionForward = actionMapping.findForward("quote");
                return actionForward;
            }
            actionForward = actionMapping.findForward("error");
            return actionForward;
        }
        ContentLookupDispatchAction.setCustId(request, customer.getCustId());
        shoppingCart.initCustomer(customer, contentBean);
        shoppingCart.setShippingPickUp(shoppingCart.isEstimatePickUp());
        shoppingCart.setShippingMethod(null);
        
        OrderHeader orderHeader = shoppingCart.locateAbundentOrder();
        if (orderHeader != null) {
        	shoppingCart.mergeOrder(orderHeader.getOrderHeaderId(), contentBean);
        }
        
        ActionForward actionForward = new ActionForward();
        if (shoppingCart.isShippingQuoteLock()) {
        	actionForward = actionMapping.findForward("quote");
            return actionForward;
        }
        
		this.initCartInfo(form, site, shoppingCart, request, messages);
        actionForward = actionMapping.findForward("loginSuccess");
        return actionForward;
    }

    public ActionMessages validate(ShoppingCartActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	if (Format.isNullOrEmpty(form.getCustEmail())) {
    		errors.add("custEmail", new ActionMessage("content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustPassword())) {
    		errors.add("custPassword", new ActionMessage("content.error.string.required"));
    	}
    	return errors;
    }
*/
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        map.put("login", "login");
        return map;
    }
}