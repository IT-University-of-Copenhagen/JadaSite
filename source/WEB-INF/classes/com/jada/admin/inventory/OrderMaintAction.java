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

package com.jada.admin.inventory;

import java.util.Date;
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

import com.jada.admin.AdminBean;
import com.jada.admin.inventory.OrderMaintActionForm;
import com.jada.dao.OrderHeaderDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.OrderTracking;
import com.jada.jpa.entity.User;
import com.jada.order.document.OrderEngine;
import com.jada.util.Constants;
import com.jada.util.Format;

public class OrderMaintAction extends OrderMaintBaseAction {
    Logger logger = Logger.getLogger(OrderMaintAction.class);
    
    public ActionForward edit(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	OrderMaintActionForm form = (OrderMaintActionForm) actionForm;
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
    	initOrder(form, orderHeader, null, null, null, request);
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}
    
    public ActionForward cancel(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	OrderMaintActionForm form = (OrderMaintActionForm) actionForm;
    	String siteId = getAdminBean(request).getSiteId();
    	OrderHeader orderHeader = OrderHeaderDAO.load(siteId, Format.getLong(form.getOrderHeaderId()));
    	orderHeader.setOrderStatus(Constants.ORDERSTATUS_CANCELLED);
    	initOrder(form, orderHeader, null, null, null, request);
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}
    
    public ActionForward comment(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	OrderMaintActionForm form = (OrderMaintActionForm) actionForm;
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
    	String orderTrackingMessage = form.getOrderTrackingMessage();
    	if (!Format.isNullOrEmpty(orderTrackingMessage)) {
    		AdminBean adminBean = getAdminBean(request);
    		User user = adminBean.getUser();
    		OrderTracking orderTracking = new OrderTracking();
    		orderTracking.setOrderTrackingCode("");
    		orderTracking.setOrderTrackingMessage(orderTrackingMessage);
    		if (form.isOrderTrackingInternal()) {
    			orderTracking.setOrderTrackingInternal(String.valueOf(Constants.VALUE_YES));
    		}
    		else {
    			orderTracking.setOrderTrackingInternal(String.valueOf(Constants.VALUE_NO));
    		}
    		orderTracking.setRecUpdateBy(user.getUserId());
    		orderTracking.setRecUpdateDatetime(new Date());
    		orderTracking.setRecCreateBy(user.getUserId());
    		orderTracking.setRecCreateDatetime(new Date());
    		orderTracking.setOrderHeader(orderHeader);
    		em.persist(orderTracking);
    		orderHeader.getOrderTrackings().add(orderTracking);
    		
    		/*
    		Vector<OrderTracking> temp = new Vector<OrderTracking>();
    		Iterator<?> trackingIterator<?> = orderHeader.getOrderTrackings().iterator();
    		while (trackingIterator.hasNext()) {
    			OrderTracking ot = (OrderTracking) trackingIterator.next();
    			temp.add(ot);
    		}
    		orderHeader.getOrderTrackings().removeAll(temp);
    		orderHeader.getOrderTrackings().add(orderTracking);
    		trackingIterator<?> = temp.iterator();
    		while (trackingIterator.hasNext()) {
    			OrderTracking ot = (OrderTracking) trackingIterator.next();
    			orderHeader.getOrderTrackings().add(ot);
    		}
    		*/
    	}
    	
    	form.setOrderTrackingMessage("");
    	form.setOrderTrackingInternal(false);
    	initOrder(form, orderHeader, null, null, null, request);
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}
    
    public ActionForward updateShipping(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	OrderMaintActionForm form = (OrderMaintActionForm) actionForm;
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
    	
		ActionMessages errors = validateShipping(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
	    	initOrder(form, orderHeader, null, null, null, request);
			return actionMapping.findForward("error");
		}
		
		orderHeader.setShippingTotal(Format.getFloatObj(form.getShippingQuoteTotal()));
		orderHeader.setShippingValidUntil(Format.getDate(form.getShippingValidUntil()));

    	initOrder(form, orderHeader, null, null, null, request);
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}
    
    public ActionForward sendShippingQuote(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
		AdminBean adminBean = getAdminBean(request);
		User user = adminBean.getUser();

    	OrderMaintActionForm form = (OrderMaintActionForm) actionForm;
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
    	
		ActionMessages errors = validateShipping(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
	    	initOrder(form, orderHeader, null, null, null, request);
			return actionMapping.findForward("error");
		}
		
		orderHeader.setShippingTotal(Format.getFloatObj(form.getShippingQuoteTotal()));
		orderHeader.setShippingValidUntil(Format.getDate(form.getShippingValidUntil()));

		OrderEngine orderEngine = new OrderEngine(orderHeader, null);
		try {
			orderEngine.sendShippingQuoteEmail(this.getServlet().getServletContext());
			
    		OrderTracking orderTracking = new OrderTracking();
    		orderTracking.setOrderTrackingCode("");
    		orderTracking.setOrderTrackingMessage("Shipping qoute sent");
    		orderTracking.setOrderTrackingInternal(String.valueOf(Constants.VALUE_NO));
    		orderTracking.setRecUpdateBy(user.getUserId());
    		orderTracking.setRecUpdateDatetime(new Date());
    		orderTracking.setRecCreateBy(user.getUserId());
    		orderTracking.setRecCreateDatetime(new Date());
    		orderTracking.setOrderHeader(orderHeader);
    		em.persist(orderTracking);
    		orderHeader.getOrderTrackings().add(orderTracking);
		}
		catch (Exception e) {
			logger.error("Unable to send mail", e);
	    	initOrder(form, orderHeader, null, null, null, request);
	    	errors.add("sendShippingQuote", new ActionMessage("content.error.mail.send"));
	    	saveMessages(request, errors);
			ActionForward actionForward = actionMapping.findForward("error");
			return actionForward;
		}
		
    	initOrder(form, orderHeader, null, null, null, request);
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}
    
    public ActionForward updateInternal(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	OrderMaintActionForm form = (OrderMaintActionForm) actionForm;
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		AdminBean adminBean = getAdminBean(request);
		User user = adminBean.getUser();
    	OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
    	OrderTrackingDisplayForm trackingForms[] = form.getOrderTrackings();
    	for (int i = 0 ; i < trackingForms.length; i++) {
    		OrderTracking orderTracking = (OrderTracking) em.find(OrderTracking.class, Format.getLong(trackingForms[i].getOrderTrackingId()));
    		String value = String.valueOf(Constants.VALUE_NO);
    		if (trackingForms[i].isOrderTrackingInternal()) {
    			value = String.valueOf(Constants.VALUE_YES);
    		}
    		if (!orderTracking.getOrderTrackingInternal().equals(value)) {
    			orderTracking.setOrderTrackingInternal(value);
        		orderTracking.setRecUpdateBy(user.getUserId());
        		orderTracking.setRecUpdateDatetime(new Date());
        		em.persist(orderTracking);
    		}
    	}
    	
    	orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
    	form.setOrderTrackingMessage("");
    	form.setOrderTrackingInternal(false);
    	initOrder(form, orderHeader, null, null, null, request);
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}
    
    public ActionMessages validateShipping(OrderMaintActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	if (Format.isNullOrEmpty(form.getShippingQuoteTotal())) {
    		errors.add("shippingQuoteTotal", new ActionMessage("error.string.required"));
    	}
    	else {
    		if (!Format.isFloat(form.getShippingQuoteTotal())) {
    			errors.add("shippingQuoteTotal", new ActionMessage("error.float.invalid"));
    		}
    	}
    	if (Format.isNullOrEmpty(form.getShippingValidUntil())) {
    		errors.add("shippingValidUntil", new ActionMessage("error.string.required"));
    	}
    	else {
    		if (!Format.isDate(form.getShippingValidUntil())) {
    			errors.add("shippingValidUntil", new ActionMessage("error.date.invalid"));
    		}
    	}
    	return errors;
    }

    public ActionMessages validate(OrderMaintActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	return errors;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("hold", "hold");
        map.put("release", "release");
        map.put("cancel", "cancel");
        map.put("edit", "edit");
        map.put("comment", "comment");
        map.put("cancel", "cancel");
        map.put("updateInternal", "updateInternal");
        map.put("updateShipping", "updateShipping");
        map.put("sendShippingQuote", "sendShippingQuote");
        return map;
    }
}
