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

package com.jada.content.checkout.paypal;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.jada.order.payment.PaymentException;
import com.jada.order.payment.gateway.PayPalEngine;
import com.jada.content.ContentBean;
import com.jada.content.checkout.ShoppingCartActionForm;
import com.jada.content.checkout.ShoppingCartBaseAction;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.Site;
import com.jada.order.cart.ShoppingCart;
import com.jada.order.document.InvoiceEngine;
import com.jada.order.document.OrderEngine;
import com.jada.util.AESEncoder;
import com.jada.util.Format;
import com.jada.util.Utility;

public class ShoppingCartPayPalFinalizeAction extends ShoppingCartBaseAction  {
    Logger logger = Logger.getLogger(ShoppingCartPayPalFinalizeAction.class);

    public ActionForward finalize(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	init(request);
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getSiteDomain().getSite();
		ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
    	EntityManager em = null;
		em = JpaConnection.getInstance().getCurrentEntityManager();
		
		Customer customer = getCustomer(request);
		if (customer.getCustPassword() == null) {
	    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
			ActionMessages errors = validate(form, site.getSiteId());
			if (errors.size() != 0) {
				saveMessages(request, errors);
				form.setCustPassword(null);
				form.setCustVerifyPassword(null);
				this.initAddressInfo(form, site, shoppingCart, request, errors);
				this.initSearchInfo(form, site.getSiteId(), errors);
				this.initCartInfo(form, site, shoppingCart, request, errors);
		        createEmptySecureTemplateInfo(request);
				return actionMapping.findForward("error");
			}
			Customer c = (Customer) em.find(Customer.class, customer.getCustId());
			c.setCustPassword(AESEncoder.getInstance().encode(form.getCustPassword()));
			c.setCustPublicName(form.getCustPublicName());
   		}
		
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
    	}
    	catch (PaymentException e) {
    		logger.error(e);
    		paymentEngine.cancelPayment();
    		shoppingCart.cancelTransaction();
    		em.getTransaction().setRollbackOnly();
    		return actionMapping.findForward("paymentError");
    	}
    	
    	try {
    		orderEngine.sendCustSaleConfirmEmail(request, this.getServlet().getServletContext());
    	}
    	catch (Exception e) {
    		// Unable to send email.  Still consider to be a successful transaction.
    		logger.error(e);
    	}
    	ShoppingCart.remove(request);
    	request.setAttribute("shoppingCart.orderNum", orderNum);
    	ActionForward forward = actionMapping.findForward("success");
        forward = new ActionForward(forward.getPath() + 
        							"&shoppingCart.orderNum=" + orderNum, 
        							forward.getRedirect());
    	return forward;
    }
    
    public ActionMessages validate(ShoppingCartActionForm form, String siteId) throws Exception { 
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
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("finalize", "finalize");
        return map;
    }
}
