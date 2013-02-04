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

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import javax.persistence.EntityManager;

import com.jada.admin.AdminBean;
import com.jada.dao.OrderItemDetailDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.CreditDetail;
import com.jada.jpa.entity.CreditDetailTax;
import com.jada.jpa.entity.CreditHeader;
import com.jada.jpa.entity.InvoiceHeader;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.OrderItemDetail;
import com.jada.jpa.entity.OrderTracking;
import com.jada.jpa.entity.User;
import com.jada.order.document.CreditEngine;
import com.jada.order.document.OrderStateException;
import com.jada.order.payment.AuthorizationException;
import com.jada.order.payment.NotImplementedException;
import com.jada.util.Constants;
import com.jada.util.Format;

public class CreditMaintAction extends OrderMaintBaseAction {
    public ActionForward create(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	CreditMaintActionForm form = (CreditMaintActionForm) actionForm;
    	AdminBean adminBean = getAdminBean(request);
    	User user = adminBean.getUser();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		InvoiceHeader invoiceHeader = (InvoiceHeader) em.find(InvoiceHeader.class, Format.getLong(form.getInvoiceHeaderId()));
		
		CreditEngine creditEngine = new CreditEngine(invoiceHeader, user);
		creditEngine.creditAll();
    	initOrder(form, invoiceHeader.getOrderHeader(), null, null, null, request);
    	form.setInvoiceHeaderId(invoiceHeader.getInvoiceHeaderId().toString());
		CreditHeader creditHeader = creditEngine.getCreditHeader();
    	CreditHeaderDisplayForm creditHeaderDisplayForm = new CreditHeaderDisplayForm();
    	form.setCreditHeader(creditHeaderDisplayForm);
    	creditHeaderDisplayForm.setShippingTotal(Format.getFloat(creditHeader.getShippingTotal()));
    	creditHeaderDisplayForm.setCreditTotal(Format.getFloat(creditHeader.getCreditTotal()));

		Iterator<?> iterator = form.getOrderItemDetails().iterator();
		Vector<CreditDetailDisplayForm> creditDetailVector = new Vector<CreditDetailDisplayForm>();
		while (iterator.hasNext()) {
			OrderItemDetailDisplayForm itemDisplay = (OrderItemDetailDisplayForm) iterator.next();
			CreditDetailDisplayForm creditDisplay = new CreditDetailDisplayForm();
			creditDisplay.setOrderItemDetailId(itemDisplay.getOrderItemDetailId());
			creditDisplay.setItemId(itemDisplay.getItemId());
			creditDisplay.setItemNum(itemDisplay.getItemNum());
			creditDisplay.setItemSkuCd(itemDisplay.getItemSkuCd());
			creditDisplay.setItemShortDesc(itemDisplay.getItemShortDesc());
			creditDisplay.setItemTierQty(itemDisplay.getItemTierQty());
			creditDisplay.setItemTierPrice(itemDisplay.getItemTierPrice());
			creditDisplay.setItemOrderQty(itemDisplay.getItemOrderQty());
			creditDisplay.setItemInvoiceQty(itemDisplay.getItemInvoiceQty());
			creditDisplay.setItemCreditQty(itemDisplay.getItemCreditQty());
			creditDisplay.setItemShipQty(itemDisplay.getItemShipQty());
			creditDisplay.setInputCreditQty("");
			creditDisplay.setItemCreditAmount(Format.getFloat(0));
			creditDisplay.setOrderItemAttributes(itemDisplay.getOrderItemAttributes());
			
			Iterator<?> creditDetailIterator = creditHeader.getCreditDetails().iterator();
			while (creditDetailIterator.hasNext()) {
				CreditDetail creditDetail = (CreditDetail) creditDetailIterator.next();
				if (itemDisplay.getOrderItemDetailId().equals(creditDetail.getOrderItemDetail().getOrderItemDetailId().toString())) {
					creditDisplay.setInputCreditQty(Format.getInt(creditDetail.getItemCreditQty()));
					creditDisplay.setItemCreditAmount(Format.getFloat(creditDetail.getItemCreditAmount()));
				}
			}
			
			creditDetailVector.add(creditDisplay);
		}
		CreditDetailDisplayForm creditDetails[] = new CreditDetailDisplayForm[creditDetailVector.size()];
		creditDetailVector.copyInto(creditDetails);
		form.setCreditDetails(creditDetails);
		form.setInputShippingTotal(Format.getFloat(creditEngine.getCreditHeader().getShippingTotal()));
		form.setEditable(true);
		form.setNewCredit(true);
		form.setAllowCapture(false);
		form.setAllowVoid(false);
		calcTotal(form, null, creditEngine);
		em.getTransaction().rollback();
		
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
    }
    
    public ActionForward edit(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	CreditMaintActionForm form = (CreditMaintActionForm) actionForm;
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		CreditHeader creditHeader = (CreditHeader) em.find(CreditHeader.class, Format.getLong(form.getCreditHeaderId()));
		OrderHeader orderHeader = creditHeader.getOrderHeader();
    	initOrder(form, orderHeader, null, creditHeader, null, request);
    	form.setInvoiceHeaderId(creditHeader.getInvoiceHeader().getInvoiceHeaderId().toString());

		Iterator<?> iterator = form.getOrderItemDetails().iterator();
		Vector<CreditDetailDisplayForm> creditDetailVector = new Vector<CreditDetailDisplayForm>();
		while (iterator.hasNext()) {
			OrderItemDetailDisplayForm itemDisplay = (OrderItemDetailDisplayForm) iterator.next();
			CreditDetailDisplayForm creditDisplay = new CreditDetailDisplayForm();
			creditDisplay.setOrderItemDetailId(itemDisplay.getOrderItemDetailId());
			creditDisplay.setItemId(itemDisplay.getItemId());
			creditDisplay.setItemNum(itemDisplay.getItemNum());
			creditDisplay.setItemSkuCd(itemDisplay.getItemSkuCd());
			creditDisplay.setItemShortDesc(itemDisplay.getItemShortDesc());
			creditDisplay.setItemTierQty(itemDisplay.getItemTierQty());
			creditDisplay.setItemTierPrice(itemDisplay.getItemTierPrice());
			creditDisplay.setItemOrderQty(itemDisplay.getItemOrderQty());
			creditDisplay.setItemInvoiceQty(itemDisplay.getItemInvoiceQty());
			creditDisplay.setItemCreditQty(itemDisplay.getItemCreditQty());
			creditDisplay.setItemShipQty(itemDisplay.getItemShipQty());
			creditDisplay.setInputCreditQty("0");
			creditDisplay.setItemCreditAmount(Format.getFloat(0));
			creditDisplay.setItemCreditQty(itemDisplay.getItemCreditQty());
			creditDisplay.setOrderItemAttributes(itemDisplay.getOrderItemAttributes());

			Iterator<?> creditDetailIterator = creditHeader.getCreditDetails().iterator();
			while (creditDetailIterator.hasNext()) {
				CreditDetail creditDetail = (CreditDetail) creditDetailIterator.next();
				if (itemDisplay.getOrderItemDetailId().equals(creditDetail.getOrderItemDetail().getOrderItemDetailId().toString())) {
					creditDisplay.setInputCreditQty(Format.getInt(creditDetail.getItemCreditQty()));
					creditDisplay.setItemCreditAmount(Format.getFloat(creditDetail.getItemCreditAmount()));
				}
			}
			
			creditDetailVector.add(creditDisplay);
		}
		CreditDetailDisplayForm creditDetails[] = new CreditDetailDisplayForm[creditDetailVector.size()];
		creditDetailVector.copyInto(creditDetails);
		form.setCreditDetails(creditDetails);

		CreditEngine creditEngine = new CreditEngine(creditHeader, null);
		setOptions(form, creditEngine);
    	calcTotal(form, creditHeader.getCreditHeaderId(), creditEngine);
    	
		iterator = creditHeader.getCreditTrackings().iterator();
		Vector<OrderTrackingDisplayForm> orderTrackings = new Vector<OrderTrackingDisplayForm>();
		while (iterator.hasNext()) {
			OrderTracking orderTracking = (OrderTracking) iterator.next();
			OrderTrackingDisplayForm orderTrackingDisplayForm = new OrderTrackingDisplayForm();
			orderTrackingDisplayForm.setOrderTrackingId(orderTracking.getOrderTrackingId().toString());
			orderTrackingDisplayForm.setOrderTrackingMessage(orderTracking.getOrderTrackingMessage());
			orderTrackingDisplayForm.setOrderTrackingInternal(false);
			if (orderTracking.getOrderTrackingInternal().equals(String.valueOf(Constants.VALUE_YES))) {
				orderTrackingDisplayForm.setOrderTrackingInternal(true);
			}
			orderTrackingDisplayForm.setRecUpdateDatetime(Format.getFullDatetime(orderTracking.getRecUpdateDatetime()));
			orderTrackings.add(orderTrackingDisplayForm);
		}
		form.getCreditHeader().setOrderTrackings(orderTrackings);
	
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}

    public ActionForward save(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	CreditMaintActionForm form = (CreditMaintActionForm) actionForm;
    	AdminBean adminBean = getAdminBean(request);
    	User user = adminBean.getUser();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		InvoiceHeader invoiceHeader = (InvoiceHeader) em.find(InvoiceHeader.class, Format.getLong(form.getInvoiceHeaderId()));
		getMissingFormInformation(form, adminBean.getSiteId());

    	String creditHeaderId = form.getCreditHeaderId();
    	CreditHeader creditHeader = null;
    	OrderHeader orderHeader = invoiceHeader.getOrderHeader();
    	if (!Format.isNullOrEmpty(creditHeaderId)) {
    		creditHeader = (CreditHeader) em.find(CreditHeader.class, Format.getLong(form.getCreditHeaderId()));
    	}
   	
    	CreditEngine creditEngine = null;
    	if (creditHeader == null) {
    		creditEngine = new CreditEngine(invoiceHeader, user);
    	}
    	else {
    		creditEngine = new CreditEngine(creditHeader, user);
    	}
    	creditEngine.setShippingTotal(0);
    	
    	initOrder(form, orderHeader, null, creditHeader, null, request);
    	ActionMessages messages = validateOther(form);
    	if (messages.size() == 0) {
    		creditEngine.setShippingTotal(Format.getFloat(form.getInputShippingTotal()));
    	}
    	boolean isValid = validateInput(form, request);
    	if (!isValid || messages.size() > 0) {
        	initOrder(form, orderHeader, null, null, null, request);
    		saveMessages(request, messages);
    		Long id = null;
    		if (!Format.isNullOrEmpty(creditHeaderId)) {
    			id = Format.getLong(creditHeaderId);
    		}
        	calcTotal(form, id, creditEngine);
    		form.setEditable(true);
    		form.setNewCredit(true);
        	if (!Format.isNullOrEmpty(creditHeaderId)) {
        		form.setNewCredit(false);
        	}
    		em.getTransaction().rollback();
    		ActionForward actionForward = actionMapping.findForward("error");
    		return actionForward;
    	}
    	
    	saveInput(form, creditEngine);
    	creditEngine.saveOrder();
    	orderHeader = creditEngine.getOrderHeader();
    	creditHeader = creditEngine.getCreditHeader();
    	form.setCreditHeaderId(creditHeader.getCreditHeaderId().toString());
    	// refresh form
    	initOrder(form, orderHeader, null,  creditHeader, null, request);
    	calcTotal(form, creditHeader.getCreditHeaderId(), creditEngine);
		form.setEditable(true);
		form.setNewCredit(false);
		form.setAllowCapture(true);
		form.setAllowVoid(false);

		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
    }
    
    public void calcTotal(CreditMaintActionForm form, Long creditHeaderId, CreditEngine creditEngine) {
    	CreditHeaderDisplayForm creditHeaderDisplayForm = form.getCreditHeader();
    	if (creditHeaderDisplayForm == null) {
	    	Iterator<?> iterator = form.getCreditHeaders().iterator();
	    	while (iterator.hasNext()) {
	    		creditHeaderDisplayForm = (CreditHeaderDisplayForm) iterator.next();
	    		if (creditHeaderId != null) {
	        		if (creditHeaderDisplayForm.getCreditHeaderId().equals(creditHeaderId.toString())) {
	        			break;
	        		}
	    		}
	    	}
    	}
    	form.setCreditHeader(creditHeaderDisplayForm);
    	form.setCreditHeaderId(creditHeaderDisplayForm.getCreditHeaderId());
    	
    	int itemOrderTotal = 0;
    	int itemInvoiceTotal = 0;
    	int itemCreditTotal = 0;
    	int itemShipTotal = 0;
    	int inputCreditTotal = 0;
    	float itemCreditAmountTotal = 0;
    	CreditDetailDisplayForm creditDisplays[] = form.getCreditDetails();
    	for (int i = 0; i < creditDisplays.length; i++) {
    		CreditDetailDisplayForm creditDisplay = creditDisplays[i];
			itemOrderTotal += Format.getInt(creditDisplay.getItemOrderQty());
			itemInvoiceTotal += Format.getInt(creditDisplay.getItemInvoiceQty());
			itemCreditTotal += Format.getInt(creditDisplay.getItemCreditQty());
			itemShipTotal += Format.getInt(creditDisplay.getItemShipQty());
			if (Format.isInt(creditDisplay.getInputCreditQty())) {
				inputCreditTotal += Format.getInt(creditDisplay.getInputCreditQty());
			}
			itemCreditAmountTotal += Format.getFloat(creditDisplay.getItemCreditAmount());
    	}
		creditHeaderDisplayForm.setItemOrderQty(Format.getInt(itemOrderTotal));
		creditHeaderDisplayForm.setItemInvoiceQty(Format.getInt(itemInvoiceTotal));
		creditHeaderDisplayForm.setItemCreditQty(Format.getInt(itemCreditTotal));
		creditHeaderDisplayForm.setItemShipQty(Format.getInt(itemShipTotal));
		creditHeaderDisplayForm.setItemCreditAmount(Format.getFloat(itemCreditTotal));
		creditHeaderDisplayForm.setInputCreditQty(Format.getInt(inputCreditTotal));
		creditHeaderDisplayForm.setItemCreditAmount(Format.getFloat(itemCreditAmountTotal));
		
		creditHeaderDisplayForm.setShippingTotal(Format.getFloat(creditEngine.getCreditHeader().getShippingTotal()));
		
		Iterator<?> iterator = creditEngine.getCreditTaxes().iterator();
		Vector<OrderTax> orderTaxes = new Vector<OrderTax>();
		while (iterator.hasNext()) {
			CreditDetailTax creditDetailTax = (CreditDetailTax) iterator.next();
			OrderTax orderTax = new OrderTax();
			orderTax.setTaxName(creditDetailTax.getTaxName());
			orderTax.setTaxAmount(Format.getFloat(creditDetailTax.getTaxAmount()));
			orderTaxes.add(orderTax);
		}
		form.setCreditTaxes(orderTaxes);
    }
    
    public boolean validateInput(CreditMaintActionForm form, HttpServletRequest request) {
		MessageResources resources = this.getResources(request);
    	boolean isClean = true;
    	CreditDetailDisplayForm creditDisplays[] = form.getCreditDetails();
    	for (int i = 0; i < creditDisplays.length; i++) {
    		CreditDetailDisplayForm creditDisplay = creditDisplays[i];
    		creditDisplay.setInputCreditQtyError("");
    		if (!Format.isInt(creditDisplay.getInputCreditQty())) {
    			isClean = false;
    			creditDisplay.setInputCreditQtyError(resources.getMessage("error.int.invalid"));
    		}
    		else {
	    		int inputCreditQty = Format.getInt(creditDisplay.getInputCreditQty());
	    		Iterator<?> iterator = form.getOrderItemDetails().iterator();
	    		while (iterator.hasNext()) {
	    			OrderItemDetailDisplayForm itemDisplay = (OrderItemDetailDisplayForm) iterator.next();
	    			if (itemDisplay.getOrderItemDetailId().equals(creditDisplay.getOrderItemDetailId())) {
		    			if (inputCreditQty < 0 || inputCreditQty > itemDisplay.getItemSuggestCreditQty()) {
		    				isClean = false;
		    				creditDisplay.setInputCreditQtyError(resources.getMessage("error.qty.invalid"));
		    			}
		    			break;
	    			}
	    		}
    		}
    	}
    	return isClean;
    }
    
    public ActionMessages validateOther(CreditMaintActionForm form) throws Exception {
    	ActionMessages errors = new ActionMessages();
    	if (!Format.isFloat(form.getInputShippingTotal())) {
    		errors.add("inputShippingTotal", new ActionMessage("error.float.required"));
    	}
    	return errors;
    }
    
    public void saveInput(CreditMaintActionForm form, CreditEngine creditEngine) throws Exception {
    	CreditDetailDisplayForm creditDisplays[] = form.getCreditDetails();
    	for (int i = 0; i < creditDisplays.length; i++) {
    		CreditDetailDisplayForm creditDisplay = creditDisplays[i];
    		int qty = 0;
    		if (!Format.isNullOrEmpty(creditDisplay.getInputCreditQty())) {
    			qty = Format.getInt(creditDisplay.getInputCreditQty());
    		}
    		if (qty == 0) {
    			continue;
    		}
    		creditEngine.setQty(creditDisplay.getItemSkuCd(), qty);
    		CreditDetail creditDetail = creditEngine.getLastCreditDetail();
    		creditDisplay.setItemCreditAmount(Format.getFloat(creditDetail.getItemCreditAmount()));
    	}
    	creditEngine.calculateHeader();
    }
    
    public ActionForward cancel(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	CreditMaintActionForm form = (CreditMaintActionForm) actionForm;
		MessageResources resources = this.getResources(request);
    	AdminBean adminBean = getAdminBean(request);
    	User user = adminBean.getUser();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
    	
    	CreditHeader creditHeader = (CreditHeader) em.find(CreditHeader.class, Format.getLong(form.getCreditHeaderId()));
    	CreditEngine creditEngine = new CreditEngine(creditHeader, user);
    	initOrder(form, orderHeader, null, creditHeader, null, request);

    	try {
    		creditEngine.cancelOrder();
    	} catch (OrderStateException exception) {
        	ActionMessages errors = new ActionMessages();
        	errors.add("credit", new ActionMessage("error.credit.cancel"));
    		ActionForward actionForward = actionMapping.findForward("error");
    		return actionForward;
    	}
    	
    	creditEngine.saveHeader();
		form.setEditable(false);
		form.setNewCredit(false);
		form.setAllowCapture(false);
		form.setAllowVoid(false);
    	
    	CreditHeaderDisplayForm creditHeaderDisplayForm = form.getCreditHeader();
		creditHeaderDisplayForm.setCreditStatus(resources.getMessage("order.status." + creditHeader.getCreditStatus()));
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
    }
    
    public ActionForward capture(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	CreditMaintActionForm form = (CreditMaintActionForm) actionForm;
    	AdminBean adminBean = getAdminBean(request);
    	User user = adminBean.getUser();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		InvoiceHeader invoiceHeader = (InvoiceHeader) em.find(InvoiceHeader.class, Format.getLong(form.getInvoiceHeaderId()));
		OrderHeader orderHeader = invoiceHeader.getOrderHeader();
		getMissingFormInformation(form, adminBean.getSiteId());

    	String creditHeaderId = form.getCreditHeaderId();
    	CreditHeader creditHeader = null;
    	if (!Format.isNullOrEmpty(creditHeaderId)) {
    		creditHeader = (CreditHeader) em.find(CreditHeader.class, Format.getLong(form.getCreditHeaderId()));
    	}

    	CreditEngine creditEngine = null;
    	if (creditHeader == null) {
    		creditEngine = new CreditEngine(invoiceHeader, user);
    	}
    	else {
    		creditEngine = new CreditEngine(creditHeader, user);
    	}
    	creditEngine.setShippingTotal(0);
    	ActionMessages messages = validateOther(form);
    	if (messages.size() == 0) {
    		creditEngine.setShippingTotal(Format.getFloat(form.getInputShippingTotal()));
    	}
    	
    	initOrder(form, orderHeader, null, creditHeader, null, request);
    	boolean isValid = validateInput(form, request);
    	if (!isValid) {
        	initOrder(form, orderHeader, null, null, null, request);
    		Long id = null;
    		if (!Format.isNullOrEmpty(creditHeaderId)) {
    			id = Format.getLong(creditHeaderId);
    		}
        	calcTotal(form, id, creditEngine);
    		form.setEditable(true);
    		form.setNewCredit(true);
        	if (!Format.isNullOrEmpty(creditHeaderId)) {
        		form.setNewCredit(false);
        	}
    		em.getTransaction().rollback();
    		ActionForward actionForward = actionMapping.findForward("error");
    		return actionForward;
    	}
    	saveInput(form, creditEngine);
    	
    	try {
    		creditEngine.creditOrder(request);
    		creditEngine.saveOrder();
    		invoiceHeader.setInvoiceStatus(creditEngine.calcStatus(invoiceHeader));
    		orderHeader.setOrderStatus(creditEngine.calcStatus(orderHeader));
    	}
    	catch (OrderStateException e) {
        	initOrder(form, orderHeader, null,  creditHeader, null, request);
        	calcTotal(form, creditHeader.getCreditHeaderId(), creditEngine);
        	ActionMessages errors = new ActionMessages();
        	errors.add("credit", new ActionMessage("error.credit.capture"));
    		ActionForward actionForward = actionMapping.findForward("error");
    		saveMessages(request, errors);
    		form.setAllowCapture(true);
    		form.setEditable(true);
    		form.setNewCredit(false);
    		return actionForward;
    	}
    	catch (AuthorizationException e) {
        	initOrder(form, orderHeader, null, creditHeader, null, request);
        	calcTotal(form, creditHeader.getCreditHeaderId(), creditEngine);
        	ActionMessages errors = new ActionMessages();
        	errors.add("credit", new ActionMessage("error.credit.capture", e.getMessage()));
    		ActionForward actionForward = actionMapping.findForward("error");
    		saveMessages(request, errors);
    		form.setAllowCapture(true);
    		form.setEditable(true);
    		form.setNewCredit(false);
    		return actionForward;	
    	}
    	initOrder(form, creditEngine.getOrderHeader(), null, creditEngine.getCreditHeader(), null, request);
    	calcTotal(form, creditEngine.getCreditHeader().getCreditHeaderId(), creditEngine);
		form.setNewCredit(false);
		form.setAllowCapture(false);
		form.setAllowVoid(true);
    	
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
    }
    
    public ActionForward voidOrder(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	CreditMaintActionForm form = (CreditMaintActionForm) actionForm;
    	AdminBean adminBean = getAdminBean(request);
    	User user = adminBean.getUser();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
		CreditHeader creditHeader = (CreditHeader) em.find(CreditHeader.class, Format.getLong(form.getCreditHeaderId()));
		getMissingFormInformation(form, adminBean.getSiteId());

    	CreditEngine creditEngine = new CreditEngine(creditHeader, user);
    	try {
    		creditEngine.voidCredit();
    		creditEngine.saveOrder();
    	}
    	catch (OrderStateException e) {
        	initOrder(form, orderHeader, null, creditHeader, null, request);
        	calcTotal(form, creditHeader.getCreditHeaderId(), creditEngine);
        	ActionMessages errors = new ActionMessages();
        	errors.add("credit", new ActionMessage("error.credit.void"));
    		ActionForward actionForward = actionMapping.findForward("error");
    		saveMessages(request, errors);
    		form.setAllowCapture(false);
    		form.setAllowVoid(true);
    		form.setEditable(false);
    		form.setNewCredit(false);
    		return actionForward;
    	}
    	catch (AuthorizationException e) {
        	initOrder(form, orderHeader, null, creditHeader, null, request);
        	calcTotal(form, creditHeader.getCreditHeaderId(), creditEngine);
        	ActionMessages errors = new ActionMessages();
        	errors.add("credit", new ActionMessage("error.credit.capture", e.getMessage()));
    		ActionForward actionForward = actionMapping.findForward("error");
    		saveMessages(request, errors);
    		form.setAllowCapture(false);
    		form.setAllowVoid(true);
    		form.setEditable(false);
    		form.setNewCredit(false);
    		return actionForward;	
    	}
    	catch (NotImplementedException e) {
        	initOrder(form, orderHeader, null, creditHeader, null, request);
        	calcTotal(form, creditHeader.getCreditHeaderId(), creditEngine);
        	ActionMessages errors = new ActionMessages();
        	errors.add("credit", new ActionMessage("error.payment.notImplemeted", e.getMessage()));
    		ActionForward actionForward = actionMapping.findForward("error");
    		saveMessages(request, errors);
    		form.setAllowCapture(false);
    		form.setAllowVoid(true);
    		form.setEditable(false);
    		form.setNewCredit(false);
    		return actionForward;	
    	}
    	initOrder(form, creditEngine.getOrderHeader(), null, creditEngine.getCreditHeader(), null, request);
    	calcTotal(form, creditHeader.getCreditHeaderId(), creditEngine);
		form.setNewCredit(false);
		form.setAllowCapture(false);
		form.setAllowVoid(false);
    	
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
    }
    
    public ActionForward comment(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	CreditMaintActionForm form = (CreditMaintActionForm) actionForm;
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
		CreditHeader creditHeader = (CreditHeader) em.find(CreditHeader.class, Format.getLong(form.getCreditHeaderId()));
		getMissingFormInformation(form, getAdminBean(request).getSiteId());
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
    		orderTracking.setCreditHeader(creditHeader);
    		em.persist(orderTracking);
    		
    		creditHeader.getCreditTrackings().add(orderTracking);
    		
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
    	CreditEngine creditEngine = new CreditEngine(creditHeader, null);
    	initOrder(form, orderHeader, null, creditHeader, null, request);
    	calcTotal(form, creditHeader.getCreditHeaderId(), creditEngine);
		if (creditEngine.isOpen(creditHeader)) {
			form.setEditable(true);
		}
		form.setAllowCapture(true);
		if (creditHeader.getPaymentTran() != null) {
			form.setAllowCapture(false);
		}
		form.setNewCredit(false);
		form.setAllowVoid(false);
		if (creditHeader.getPaymentTran() != null && creditHeader.getVoidPaymentTran() == null) {
			form.setAllowVoid(true);
		}
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}
    
    public void setOptions(CreditMaintActionForm form, CreditEngine creditEngine) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    	CreditHeader creditHeader = creditEngine.getCreditHeader();
		form.setEditable(false);
		form.setInputShippingTotal(Format.getFloat(creditHeader.getShippingTotal()));
		if (creditEngine.isOpen(creditHeader)) {
			form.setEditable(true);
		}
		form.setNewCredit(false);
		form.setAllowCapture(false);
		if (creditHeader.getPaymentTran() == null) {
			if (creditHeader.getCreditStatus().equals(Constants.ORDERSTATUS_OPEN)) {
				form.setAllowCapture(true);
			}
			if (creditHeader.getCreditStatus().equals(Constants.ORDERSTATUS_COMPLETED)) {
				form.setAllowVoid(true);
			}
		}
		else {
			form.setAllowCapture(true);
			if (creditHeader.getPaymentTran() != null) {
				form.setAllowCapture(false);
			}
			form.setAllowVoid(false);
			if (creditHeader.getPaymentTran() != null && creditHeader.getVoidPaymentTran() == null) {
				form.setAllowVoid(true);
			}
		}
	}
    
    public ActionForward updateInternal(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	CreditMaintActionForm form = (CreditMaintActionForm) actionForm;
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		AdminBean adminBean = getAdminBean(request);
		User user = adminBean.getUser();
    	OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
		CreditHeader creditHeader = (CreditHeader) em.find(CreditHeader.class, Format.getLong(form.getCreditHeaderId()));
		getMissingFormInformation(form, adminBean.getSiteId());
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
    	
    	form.setOrderTrackingMessage("");
    	form.setOrderTrackingInternal(false);
    	CreditEngine creditEngine = new CreditEngine(creditHeader, null);
    	initOrder(form, orderHeader, null, creditHeader, null, request);
    	calcTotal(form, creditHeader.getCreditHeaderId(), creditEngine);
		if (creditEngine.isOpen(creditHeader)) {
			form.setEditable(true);
		}
		form.setAllowCapture(true);
		if (creditHeader.getPaymentTran() != null) {
			form.setAllowCapture(false);
		}
		form.setNewCredit(false);
		form.setAllowVoid(false);
		if (creditHeader.getPaymentTran() != null && creditHeader.getVoidPaymentTran() == null) {
			form.setAllowVoid(true);
		}
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}
    
    private void getMissingFormInformation(CreditMaintActionForm form, String siteId) throws NumberFormatException, SecurityException, Exception {
    	for (CreditDetailDisplayForm detailForm : form.getCreditDetails()) {
    		OrderItemDetail orderItemDetail = OrderItemDetailDAO.load(siteId, Long.valueOf(detailForm.getOrderItemDetailId()));
    		Vector<OrderItemAttributeBean> orderItemAttributes = getOrderItemAttributes(orderItemDetail);
    		detailForm.setOrderItemAttributes(orderItemAttributes);
    	}
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("create", "create");
        map.put("edit", "edit");
        map.put("save", "save");
        map.put("cancel", "cancel");
        map.put("capture", "capture");
        map.put("voidOrder", "voidOrder");
        map.put("comment", "comment");
        map.put("updateInternal", "updateInternal");
        return map;
    }

}
