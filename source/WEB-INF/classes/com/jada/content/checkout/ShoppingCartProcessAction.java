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
import com.jada.dao.ShippingMethodDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.CustomerAddress;
import com.jada.jpa.entity.CustomerClass;
import com.jada.jpa.entity.Language;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.PaymentGateway;
import com.jada.jpa.entity.ShippingMethod;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomain;
import com.jada.order.cart.ShoppingCart;
import com.jada.order.document.InvoiceEngine;
import com.jada.order.document.OrderEngine;
import com.jada.order.payment.PaymentCustomerException;
import com.jada.order.payment.PaymentEngine;
import com.jada.order.payment.PaymentException;
import com.jada.order.payment.gateway.PayPalEngine;
import com.jada.order.payment.gateway.PayPalWebsitePaymentProHostedEngine;
import com.jada.system.ApplicationGlobal;
import com.jada.system.Languages;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.IdSecurity;
import com.jada.util.JSONEscapeObject;
import com.jada.util.Utility;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ShoppingCartProcessAction extends ShoppingCartBaseAction {
    Logger logger = Logger.getLogger(ShoppingCartProcessAction.class);

    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
		createEmptySecureTemplateInfo(request);
		ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
		shoppingCart.setCheckoutSteps(Constants.CHECKOUT_STEP_NONE);
		shoppingCart.setCreditCardOrder(false);
		shoppingCart.setCashPaymentOrder(false);
		shoppingCart.setPayPalWebsitePaymentProHostedOrder(false);
		shoppingCart.setPayPalOrder(false);
		if (form.getCash() != null && form.getCash().equals(String.valueOf(Constants.VALUE_YES))) {
			shoppingCart.setCashPaymentOrder(true);
		}
		else {
			if (shoppingCart.isPayPalWebsitePaymentProHosted()) {
				shoppingCart.setPayPalWebsitePaymentProHostedOrder(true);
			}
			else {
				shoppingCart.setCreditCardOrder(true);
			}
		}
		ActionMessages messages = new ActionMessages();
		this.initCartInfo(form, site, shoppingCart, request, messages);
		if (isCustomerSession(request)) {
			this.initAddressInfo(form, site, shoppingCart, request, messages);
		}
		this.initSearchInfo(form, site.getSiteId(), messages);

    	ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    /****************************************************************************************************/
    
    public ActionForward info(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
    	form.setUseTemplate(false);
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
		createEmptySecureTemplateInfo(request);
		ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
		if (form.getResetStep() != null) {
			shoppingCart.setCheckoutSteps(Integer.valueOf(form.getResetStep()));
		}
		ActionMessages messages = new ActionMessages();
		this.initCartInfo(form, site, shoppingCart, request, messages);
		if (isCustomerSession(request)) {
			this.initAddressInfo(form, site, shoppingCart, request, messages);
			this.initCreditCardInfo(form, site, shoppingCart, request, messages);
		}
    	ActionForward actionForward = actionMapping.findForward("info");
        return actionForward;
    }
    
    /****************************************************************************************************/
    
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
        
		shoppingCart.setCheckoutSteps(Constants.CHECKOUT_STEP_MYINFORMATION);
		this.initCartInfo(form, site, shoppingCart, request, messages);
		jsonResult.put("address", this.getJsonAddressInfo(site, shoppingCart, request));
		jsonResult.put("cartInfo", this.getJsonCartInfo(site, shoppingCart, request));
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		this.streamWebService(response, jsonResult.toString());
		return null;
    }

    public ActionForward newUser(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
		ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
		
    	ActionMessages messages = validateNewUser(form);
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("cartInfo", this.getJsonCartInfo(site, shoppingCart, request));
    	if (messages.size() > 0) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			toJson(jsonResult, messages, contentBean);
			this.streamWebService(response, jsonResult.toString());
			return null;
    	}
    	
    	Long defaultSiteDomainId = contentBean.getSiteDomain().getSite().getSiteDomainDefault().getSiteDomainId();
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
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			toJson(jsonResult, messages, contentBean);
			this.streamWebService(response, jsonResult.toString());
			return null;
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
		shoppingCart.setCustomer(customer);
		shoppingCart.setCustAddress(customerAddress);

		shoppingCart.setCheckoutSteps(Constants.CHECKOUT_STEP_MYINFORMATION);
		this.initCartInfo(form, site, shoppingCart, request, messages);
		jsonResult.put("address", this.getJsonAddressInfo(site, shoppingCart, request));
		jsonResult.put("cartInfo", this.getJsonCartInfo(site, shoppingCart, request));
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
    
    /****************************************************************************************************/

    public ActionForward updateUser(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
		ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
		// TODO shipping quote
		/*
		if (shoppingCart.isShippingQuoteLock()) {
        	actionForward = actionMapping.findForward("quote");
            return actionForward;
		}
		*/

		this.init(request);
		JSONEscapeObject jsonResult = new JSONEscapeObject();
    	ActionMessages messages = this.validateAddress(site.getSiteId(), form);
    	if (messages.size() > 0) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			toJson(jsonResult, messages, contentBean);
			this.streamWebService(response, jsonResult.toString());
			return null;
    	}
    	this.saveAddress(form, site, getCustomer(request), shoppingCart, contentBean);

		shoppingCart.setCheckoutSteps(Constants.CHECKOUT_STEP_SHIPPING);
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("cartInfo", this.getJsonCartInfo(site, shoppingCart, request));
		this.streamWebService(response, jsonResult.toString());
		return null;
    }
    
    /****************************************************************************************************/
    
    public ActionForward updateShipping(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
		ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
		
		JSONEscapeObject jsonResult = new JSONEscapeObject();
    	ActionMessages messages = validateShipping(contentBean, form, shoppingCart);
    	if (messages.size() > 0) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			toJson(jsonResult, messages, contentBean);
			this.streamWebService(response, jsonResult.toString());
			return null;
    	}
    	
    	String shippingMethodId = form.getShippingMethodId();
    	if (shippingMethodId.equals(Constants.SHOPPING_CART_SHIPPING_PICKUP)) {
    		shoppingCart.setShippingPickUp(true);
    		shoppingCart.setShippingMethod(null);
    	}
    	else {
    		shoppingCart.setShippingPickUp(false);
        	ShippingMethod shippingMethod = null;
        	if (shippingMethodId != null) {
        		shippingMethod = ShippingMethodDAO.load(site.getSiteId(), Format.getLong(shippingMethodId));
        	}
        	shoppingCart.setShippingMethod(shippingMethod);
    	}
    	shoppingCart.recalculate(contentBean);
    	
		if (shoppingCart.isCreditCardOrder()) {
			shoppingCart.setCheckoutSteps(Constants.CHECKOUT_STEP_CREDITCARD);
		}
		else {
			shoppingCart.setCheckoutSteps(Constants.CHECKOUT_STEP_DONE);
		}
		jsonResult.put("cartInfo", this.getJsonCartInfo(site, shoppingCart, request));
		if (!shoppingCart.isCashPaymentOrder()) {
			jsonResult.put("custCreditCard", getJsonCreditCardInfo(site, shoppingCart, request));
		}
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		this.streamWebService(response, jsonResult.toString());
		return null;
    }
    
    /****************************************************************************************************/
    
    public ActionForward payPalProHostedPayment(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
    	ContentBean contentBean = getContentBean(request);
    	PaymentGateway paymentGateway = contentBean.getContentSessionBean().getSiteCurrency().getPaymentGateway();
    	
    	PayPalWebsitePaymentProHostedEngine payPalEngine = new PayPalWebsitePaymentProHostedEngine(contentBean.getContentSessionBean().getSiteDomain().getSite(), paymentGateway.getPaymentGatewayId());
    	payPalEngine.setSiteDomain(contentBean.getContentSessionBean().getSiteDomain());
    	shoppingCart.setPaymentEngine(payPalEngine);
    	
		OrderEngine orderEngine = new OrderEngine(shoppingCart, null);
		OrderHeader orderHeader = orderEngine.getOrderHeader();
    	payPalEngine.payPalAuthorizeAndCapturePayment(orderHeader, request);
    	
		String emailLink = payPalEngine.getEmailLink();
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("url", emailLink);
		this.streamWebService(response, jsonResult.toString());
		return null;
    }
    
    /****************************************************************************************************/
    
    public ActionForward payPalAuthorize(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
    	ContentBean contentBean = getContentBean(request);
    	PaymentGateway paymentGateway = contentBean.getContentSessionBean().getSiteCurrency().getPayPalPaymentGateway();
    	PayPalEngine payPalEngine = new PayPalEngine(contentBean.getContentSessionBean().getSiteDomain().getSite(), paymentGateway.getPaymentGatewayId());
    	payPalEngine.setSiteDomain(contentBean.getContentSessionBean().getSiteDomain());
    	shoppingCart.setPaymentEngine(payPalEngine);
    	shoppingCart.setPayPalOrder(true);
    	
		OrderEngine orderEngine = new OrderEngine(shoppingCart, null);
		OrderHeader orderHeader = orderEngine.getOrderHeader();
		payPalEngine.payPalAuthorizePayment(orderHeader, request);
		String token = payPalEngine.getToken();
		
		String mobile = request.getParameter("mobile");

		ActionForward templateForward = null;
    	
		if (Format.isNullOrEmpty(mobile)) {
	    	if (payPalEngine.isProduction()) {
	    		templateForward = actionMapping.findForward("paypal.production");
	    	}
	    	else {
	    		templateForward = actionMapping.findForward("paypal.sandbox");
	    	}
		}
		else {
	    	if (payPalEngine.isProduction()) {
	    		templateForward = actionMapping.findForward("paypal.mobile.production");
	    	}
	    	else {
	    		templateForward = actionMapping.findForward("paypal.mobile.sandbox");
	    	}
		}
    	
    	String path = templateForward.getPath() + token;
    	ActionForward actionForward = new ActionForward(templateForward);
    	actionForward.setPath(path);
        return actionForward;
    }
    
    /****************************************************************************************************/

    public ActionForward payPalCallBack(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	ContentBean contentBean = getContentBean(request);
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
    	PaymentEngine paymentEngine = shoppingCart.getPaymentEngine();
    	try {
	    	if (paymentEngine != null) {
		    	paymentEngine.callBack(request, shoppingCart, contentBean);
		    	if (paymentEngine.isProvideCustomer()) {
		    	}
	    	}
	    	shoppingCart.recalculate(contentBean);
    	}
    	catch (PaymentException e) {
    		logger.error(e);
    		paymentEngine.abort();
    		shoppingCart.cancelTransaction();
    		createEmptySecureTemplateInfo(request);
    		return actionMapping.findForward("paymentError");
    	}
    	catch (PaymentCustomerException e) {
    		paymentEngine.abort();
    		shoppingCart.cancelTransaction();
    		createEmptySecureTemplateInfo(request);
    		return actionMapping.findForward("paymentCustomerError");
    	}
		this.saveToken(request);
		
		if (shoppingCart.isNewCustomer()) {
			shoppingCart.setCheckoutSteps(Constants.CHECKOUT_STEP_PAYPALACTIVATE);
		}
		else {
			shoppingCart.setCheckoutSteps(Constants.CHECKOUT_STEP_DONE);
		}
		
		shoppingCart.setCheckoutSteps(Constants.CHECKOUT_STEP_DONE);
		ActionMessages messages = new ActionMessages();
		this.initAddressInfo(form, site, shoppingCart, request, messages);
		this.initSearchInfo(form, site.getSiteId(), messages);
		this.initCartInfo(form, site, shoppingCart, request, messages);

        createEmptySecureTemplateInfo(request);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    /****************************************************************************************************/
    
    public ActionForward activatePayPalUser(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
		ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
		
    	ActionMessages messages = validatePayPalUser(form, site.getSiteId());
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("cartInfo", this.getJsonCartInfo(site, shoppingCart, request));
    	if (messages.size() > 0) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			toJson(jsonResult, messages, contentBean);
			this.streamWebService(response, jsonResult.toString());
			return null;
    	}
    	
		Customer customer = getCustomer(request);
		customer.setCustPassword(AESEncoder.getInstance().encode(form.getCustPassword()));
		customer.setCustPublicName(form.getCustPublicName());

		this.initCartInfo(form, site, shoppingCart, request, messages);
		jsonResult.put("address", this.getJsonAddressInfo(site, shoppingCart, request));
		jsonResult.put("cartInfo", this.getJsonCartInfo(site, shoppingCart, request));
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		this.streamWebService(response, jsonResult.toString());
        return null;
    }
    
    /****************************************************************************************************/
    
    public ActionForward finalizeOrder(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
		ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
    	shoppingCart.recalculate(contentBean);
    	
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("cartInfo", this.getJsonCartInfo(site, shoppingCart, request));
		ActionMessages messages = new ActionMessages();
		try {
			// TODO remove form 
			this.finalizeOrder(form, site, shoppingCart, request, messages);
    	}
		catch (Exception e) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			jsonResult.put("paymentMessage", shoppingCart.getPaymentEngine().getPaymentMessage());
			toJson(jsonResult, messages, contentBean);
			this.streamWebService(response, jsonResult.toString());
			return null;
		}
    	ShoppingCart.remove(request);

    	ActionForward forward = actionMapping.findForward("finalize");
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("orderNum", shoppingCart.getOrderNum());
		jsonResult.put("url", "/" + ApplicationGlobal.getContextPath() + forward.getPath() + 
    							"&prefix=" + contentBean.getSiteDomain().getSiteDomainPrefix() + 
    							"&langName=" + contentBean.getContentSessionKey().getLangName() + 
    							"&shoppingCart.orderNum=" + shoppingCart.getOrderNum());
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		this.streamWebService(response, jsonResult.toString());
		return null;
    }
    
    /****************************************************************************************************/

    public ActionForward finalizePayPalOrder(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	init(request);
    	ContentBean contentBean = getContentBean(request);
		ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		
		JSONEscapeObject jsonResult = new JSONEscapeObject();
    	PayPalEngine paymentEngine = (PayPalEngine) shoppingCart.getPaymentEngine();
    	String orderNum = null;
    	createEmptySecureTemplateInfo(request);
		OrderEngine orderEngine = new OrderEngine(shoppingCart, null);
    	try {
    		OrderHeader orderHeader = orderEngine.getOrderHeader();
			InvoiceEngine invoiceEngine = new InvoiceEngine(orderHeader, null);
			invoiceEngine.invoiceAll();
			invoiceEngine.setCreditCardInfo(orderEngine.getCreditCardInfo());
			invoiceEngine.payOrder(shoppingCart.getPaymentEngine(), request);
			orderEngine.saveOrder();
			orderNum = orderEngine.getOrderHeader().getOrderNum();
			invoiceEngine.saveOrder();
			orderEngine.processOrder();
    	}
    	catch (PaymentException e) {
    		paymentEngine.cancelPayment();
    		shoppingCart.cancelTransaction();
    		em.getTransaction().setRollbackOnly();
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			if (Format.isNullOrEmpty(shoppingCart.getPaymentEngine().getPaymentMessage())) {
	    		jsonResult.addKeyValue("paymentMessage", e.getMessage());
			}
			else {
	    		jsonResult.addKeyValue("paymentMessage", e.getMessage());
			}
			this.streamWebService(response, jsonResult.toString());
			return null;
    	}

    	try {
    		orderEngine.sendCustSaleConfirmEmail(request, this.getServlet().getServletContext());
    	}
    	catch (Exception e) {
    		// Unable to send email.  Still consider to be a successful transaction.
    		logger.error(e);
    	}
    	ShoppingCart.remove(request);
    	ActionForward forward = actionMapping.findForward("finalize");
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("orderNum", orderNum);
		jsonResult.put("url", "/" + ApplicationGlobal.getContextPath() + forward.getPath() + 
        							"&prefix=" + contentBean.getSiteDomain().getSiteDomainPrefix() + 
        							"&langName=" + contentBean.getContentSessionKey().getLangName() + 
        							"&shoppingCart.orderNum=" + shoppingCart.getOrderNum());
		this.streamWebService(response, jsonResult.toString());
		return null;
    }
    
    /****************************************************************************************************/

    public ActionForward updatePayment(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
		ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
		ActionMessages messages = new ActionMessages();

		JSONEscapeObject jsonResult = new JSONEscapeObject();
    	messages = this.validateCreditCard(form);
    	if (messages.size() > 0) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			toJson(jsonResult, messages, contentBean);
			this.streamWebService(response, jsonResult.toString());
			return null;
    	}
       	this.saveCreditCard(form, site, shoppingCart, contentBean);

       	shoppingCart.setCheckoutSteps(Constants.CHECKOUT_STEP_DONE);
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("cartInfo", this.getJsonCartInfo(site, shoppingCart, request));
		this.streamWebService(response, jsonResult.toString());
		return null;
    }

    /****************************************************************************************************/

    public ActionMessages validateNewUser(ShoppingCartActionForm form) { 
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
    	if (Format.isNullOrEmpty(form.getCustVerifyPassword())) {
    		errors.add("custVerifyPassword", new ActionMessage("content.error.string.required"));
    	}
    	if (!form.getCustPassword().equals(form.getCustVerifyPassword())) {
    		errors.add("custPasswordNoMatch", new ActionMessage("content.error.password.nomatch"));
    	}
    	else {
	    	if (!Utility.isValidPassword(form.getCustPassword())) {
	    		errors.add("custPassword", new ActionMessage("content.error.password.invalidRule"));
	    	}
    	}
    	return errors;
    }
    
    /***************************************************************************************************/

    public ActionMessages validatePayPalUser(ShoppingCartActionForm form, String siteId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	ActionMessages errors = new ActionMessages();
    	
    	if (Format.isNullOrEmpty(form.getCustPassword()) || Format.isNullOrEmpty(form.getCustVerifyPassword())) {
    		errors.add("custPassword", new ActionMessage("content.error.string.required"));
    	}
    	else {
    		if (!Utility.isValidPassword(form.getCustPassword())) {
    			errors.add("custPassword", new ActionMessage("content.error.password.invalidRule"));
    		}
    	}
    	if (!form.getCustPassword().equals(form.getCustVerifyPassword())) {
    		errors.add("custPassword", new ActionMessage("content.error.password.nomatch"));
    	}
    	if (Format.isNullOrEmpty(form.getCustPublicName())) {
    		errors.add("custPublicName", new ActionMessage("content.error.string.required"));
    	}
    	else {
    		String sql = "select count(*) " + 
    					 "from   Customer customer " +
    					 "where  customer.siteId = :siteId " +
    					 "and    customer.custPublicName = :custPublicName";
    		Query query = em.createQuery(sql);
    		query.setParameter("siteId", siteId);
    		query.setParameter("custPublicName", form.getCustPublicName());
    		Long counter = (Long) query.getSingleResult();
    		if (counter > 0) {
    			errors.add("custPublicName", new ActionMessage("content.error.publicName.duplicate"));
    		}
    	}
    	return errors;
    }

    /****************************************************************************************************/
    
    protected void toJson(JSONEscapeObject jsonResult, ActionMessages messages, ContentBean contentBean) throws Exception {
    	Language language = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getLanguage();
    	Iterator<?> iterator = messages.properties();
    	while (iterator.hasNext()) {
    		String name = (String) iterator.next();
    		Iterator<?> values = messages.get(name);
    		if (values.hasNext()) {
    			ActionMessage message = (ActionMessage) values.next();
    			jsonResult.addKeyValue(name, Languages.getLangTranValue(language.getLangId(), message.getKey()));
    		}
    	}
    }

    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        map.put("login", "login");
        map.put("newUser", "newUser");
        map.put("updateUser", "updateUser");
        map.put("updateShipping", "updateShipping");
        map.put("updatePayment", "updatePayment");
        map.put("payPalAuthorize", "payPalAuthorize");
        map.put("payPalProHostedPayment", "payPalProHostedPayment");
        map.put("payPalCallBack", "payPalCallBack");
        map.put("activatePayPalUser", "activatePayPalUser");
        map.put("info", "info");
        map.put("finalizeOrder", "finalizeOrder");
        map.put("finalizePayPalOrder", "finalizePayPalOrder");
        return map;
    }
}