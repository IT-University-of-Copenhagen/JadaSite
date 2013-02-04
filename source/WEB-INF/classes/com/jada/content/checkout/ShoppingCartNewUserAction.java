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
import com.jada.dao.CustomerDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.CustomerAddress;
import com.jada.jpa.entity.CustomerClass;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomain;
import com.jada.order.cart.ShoppingCart;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import javax.persistence.Query;
import javax.persistence.EntityManager;

public class ShoppingCartNewUserAction extends ContentLookupDispatchAction {
    Logger logger = Logger.getLogger(ShoppingCartNewUserAction.class);

    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	ActionForward actionForward = actionMapping.findForward("success");
		createEmptySecureTemplateInfo(request);
        return actionForward;
    }

    public ActionForward create(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
		createEmptySecureTemplateInfo(request);
		ShoppingCartNewUserActionForm form = (ShoppingCartNewUserActionForm) actionForm;
		ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
		
    	ActionMessages messages = validate(form);
    	if (messages.size() > 0) {
			saveMessages(request, messages);
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
    	}
    	
    	ContentBean contentBean = getContentBean(request);
    	Long defaultSiteDomainId = contentBean.getSiteDomain().getSite().getSiteDomainDefault().getSiteDomainId();
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	char singleCheckout = contentBean.getSiteDomain().getSite().getSingleCheckout();

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String siteId = site.getSiteId();
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
        if (list.size() != 0) {
        	messages.add("emailDuplicate", new ActionMessage("content.error.email.duplicate"));
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
        list = query.getResultList();
        if (list.size() != 0) {
        	messages.add("custPublicName", new ActionMessage("content.error.publicName.duplicate"));
        }
        if (messages.size() > 0) {
			saveMessages(request, messages);
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
        }
        
		sql = "from     CustomerClass customerClass " +
	  	  	  "where    customerClass.site.siteId = :siteId " +
	  	  	  "and      customerClass.systemRecord = 'Y'";
		query = em.createQuery(sql);
		query.setParameter("siteId", siteId);
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
        customer.setCustEmail(form.getCustEmail());
        customer.setCustPassword(AESEncoder.getInstance().encode(form.getCustPassword()));
        customer.setCustPublicName(form.getCustPublicName());
        customer.setCustSource(Constants.CUSTOMER_SOURCE_REGISTER);
        customer.setCustSourceRef("");
        customer.setActive(Constants.ACTIVE_YES);
        customer.setRecUpdateBy(Constants.USERNAME_CUSTOMER);
        customer.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        customer.setRecCreateBy(Constants.USERNAME_CUSTOMER);
        customer.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        customer.setCustomerClass(customerClass);
        
        CustomerAddress customerAddress = shoppingCart.getEstimateAddress();
        if (customerAddress == null) {
        	customerAddress = new CustomerAddress();
        }
        customer.setCustAddress(customerAddress);
        customer.getCustAddresses().add(customerAddress);
        customerAddress.setCustUseAddress(Constants.CUST_ADDRESS_USE_OWN);
        customerAddress.setCustAddressType(Constants.CUSTOMER_ADDRESS_CUST);
        customerAddress.setRecUpdateBy(Constants.USERNAME_CUSTOMER);
        customerAddress.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        customerAddress.setRecCreateBy(Constants.USERNAME_CUSTOMER);
        customerAddress.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        em.persist(customerAddress);

        em.persist(customer);
        em.flush();
        
        ContentLookupDispatchAction.setCustId(request, customer.getCustId());
		shoppingCart.setCustomer(new CustomerDAO(customer));
		shoppingCart.setCustAddress(customerAddress);

        ActionForward actionForward = actionMapping.findForward("createSuccess");
        return actionForward;
    }

    public ActionMessages validate(ShoppingCartNewUserActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	if (Format.isNullOrEmpty(form.getCustEmail())) {
    		errors.add("custEmail", new ActionMessage("content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustEmail1())) {
    		errors.add("custEmail1", new ActionMessage("content.error.string.required"));
    	}
    	if (!form.getCustEmail().equals(form.getCustEmail1())) {
    		errors.add("custEmailNoMatch", new ActionMessage("content.error.email.nomatch"));
    	}
    	if (Format.isNullOrEmpty(form.getCustPassword())) {
    		errors.add("custPassword", new ActionMessage("content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustPassword1())) {
    		errors.add("custPassword1", new ActionMessage("content.error.string.required"));
    	}
    	if (!form.getCustPassword().equals(form.getCustPassword1())) {
    		errors.add("custPasswordNoMatch", new ActionMessage("content.error.password.nomatch"));
    	}
    	else {
	    	if (!Utility.isValidPassword(form.getCustPassword())) {
	    		errors.add("custPassword", new ActionMessage("content.error.password.invalidRule"));
	    	}
    	}
    	return errors;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        map.put("create", "create");
        return map;
    }
}