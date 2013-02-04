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
import com.jada.jpa.entity.InvoiceDetail;
import com.jada.jpa.entity.InvoiceDetailTax;
import com.jada.jpa.entity.InvoiceHeader;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.OrderItemDetail;
import com.jada.jpa.entity.OrderTracking;
import com.jada.jpa.entity.User;
import com.jada.order.document.InvoiceEngine;
import com.jada.order.document.OrderEngine;
import com.jada.order.document.OrderStateException;
import com.jada.order.payment.AuthorizationException;
import com.jada.order.payment.NotImplementedException;
import com.jada.order.payment.PaymentException;
import com.jada.util.Constants;
import com.jada.util.Format;

public class InvoiceMaintAction extends OrderMaintBaseAction {
    public ActionForward create(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	InvoiceMaintActionForm form = (InvoiceMaintActionForm) actionForm;
    	AdminBean adminBean = getAdminBean(request);
    	User user = adminBean.getUser();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
		
		InvoiceEngine invoiceEngine = new InvoiceEngine(orderHeader, user);
		invoiceEngine.invoiceAll();
    	initOrder(form, orderHeader, null, null, null, request);
		InvoiceHeader invoiceHeader = invoiceEngine.getInvoiceHeader();
    	InvoiceHeaderDisplayForm invoiceHeaderDisplayForm = new InvoiceHeaderDisplayForm();
    	form.setInvoiceHeader(invoiceHeaderDisplayForm);
    	invoiceHeaderDisplayForm.setShippingTotal(Format.getFloat(invoiceHeader.getShippingTotal()));
    	invoiceHeaderDisplayForm.setInvoiceTotal(Format.getFloat(invoiceHeader.getInvoiceTotal()));

		Iterator<?> iterator = form.getOrderItemDetails().iterator();
		Vector<InvoiceDetailDisplayForm> invoiceDetailVector = new Vector<InvoiceDetailDisplayForm>();
		while (iterator.hasNext()) {
			OrderItemDetailDisplayForm itemDisplay = (OrderItemDetailDisplayForm) iterator.next();
			InvoiceDetailDisplayForm invoiceDisplay = new InvoiceDetailDisplayForm();
			invoiceDisplay.setOrderItemDetailId(itemDisplay.getOrderItemDetailId());
			invoiceDisplay.setItemId(itemDisplay.getItemId());
			invoiceDisplay.setItemNum(itemDisplay.getItemNum());
			invoiceDisplay.setItemSkuCd(itemDisplay.getItemSkuCd());
			invoiceDisplay.setItemShortDesc(itemDisplay.getItemShortDesc());
			invoiceDisplay.setItemTierQty(itemDisplay.getItemTierQty());
			invoiceDisplay.setItemTierPrice(itemDisplay.getItemTierPrice());
			invoiceDisplay.setItemOrderQty(itemDisplay.getItemOrderQty());
			invoiceDisplay.setItemInvoiceQty(itemDisplay.getItemInvoiceQty());
			invoiceDisplay.setItemCreditQty(itemDisplay.getItemCreditQty());
			invoiceDisplay.setItemShipQty(itemDisplay.getItemShipQty());
			invoiceDisplay.setInputInvoiceQty("");
			invoiceDisplay.setItemInvoiceAmount(Format.getFloat(0));
			invoiceDisplay.setOrderItemAttributes(itemDisplay.getOrderItemAttributes());

			Iterator<?> invoiceDetailIterator = invoiceHeader.getInvoiceDetails().iterator();
			while (invoiceDetailIterator.hasNext()) {
				InvoiceDetail invoiceDetail = (InvoiceDetail) invoiceDetailIterator.next();
				if (itemDisplay.getOrderItemDetailId().equals(invoiceDetail.getOrderItemDetail().getOrderItemDetailId().toString())) {
					invoiceDisplay.setInputInvoiceQty(Format.getInt(invoiceDetail.getItemInvoiceQty()));
					invoiceDisplay.setItemInvoiceAmount(Format.getFloat(invoiceDetail.getItemInvoiceAmount()));
				}
			}
			
			invoiceDetailVector.add(invoiceDisplay);
		}
		InvoiceDetailDisplayForm invoiceDetails[] = new InvoiceDetailDisplayForm[invoiceDetailVector.size()];
		invoiceDetailVector.copyInto(invoiceDetails);
		form.setInvoiceDetails(invoiceDetails);
		form.setInputShippingTotal(Format.getFloat(invoiceEngine.getInvoiceHeader().getShippingTotal()));
		form.setEditable(true);
		form.setNewInvoice(true);
		form.setAllowCapture(false);
		form.setAllowVoid(false);
		form.setAllowCredit(false);
		calcTotal(form, null, invoiceEngine);
		em.getTransaction().rollback();
		
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
    }
    
    public ActionForward edit(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	InvoiceMaintActionForm form = (InvoiceMaintActionForm) actionForm;
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		InvoiceHeader invoiceHeader = (InvoiceHeader) em.find(InvoiceHeader.class, Format.getLong(form.getInvoiceHeaderId()));
		OrderHeader orderHeader = invoiceHeader.getOrderHeader();
    	initOrder(form, orderHeader, invoiceHeader, null, null, request);

		Iterator<?> iterator = form.getOrderItemDetails().iterator();
		Vector<InvoiceDetailDisplayForm> invoiceDetailVector = new Vector<InvoiceDetailDisplayForm>();
		while (iterator.hasNext()) {
			OrderItemDetailDisplayForm itemDisplay = (OrderItemDetailDisplayForm) iterator.next();
			InvoiceDetailDisplayForm invoiceDisplay = new InvoiceDetailDisplayForm();
			invoiceDisplay.setOrderItemDetailId(itemDisplay.getOrderItemDetailId());
			invoiceDisplay.setItemId(itemDisplay.getItemId());
			invoiceDisplay.setItemSkuCd(itemDisplay.getItemSkuCd());
			invoiceDisplay.setItemNum(itemDisplay.getItemNum());
			invoiceDisplay.setItemShortDesc(itemDisplay.getItemShortDesc());
			invoiceDisplay.setItemTierQty(itemDisplay.getItemTierQty());
			invoiceDisplay.setItemTierPrice(itemDisplay.getItemTierPrice());
			invoiceDisplay.setItemOrderQty(itemDisplay.getItemOrderQty());
			invoiceDisplay.setItemCreditQty(itemDisplay.getItemCreditQty());
			invoiceDisplay.setItemShipQty(itemDisplay.getItemShipQty());
			invoiceDisplay.setInputInvoiceQty("");
			invoiceDisplay.setItemInvoiceAmount(Format.getFloat(0));
			invoiceDisplay.setItemInvoiceQty(itemDisplay.getItemInvoiceQty());
			invoiceDisplay.setOrderItemAttributes(itemDisplay.getOrderItemAttributes());

			Iterator<?> invoiceDetailIterator = invoiceHeader.getInvoiceDetails().iterator();
			while (invoiceDetailIterator.hasNext()) {
				InvoiceDetail invoiceDetail = (InvoiceDetail) invoiceDetailIterator.next();
				if (itemDisplay.getOrderItemDetailId().equals(invoiceDetail.getOrderItemDetail().getOrderItemDetailId().toString())) {
					invoiceDisplay.setInputInvoiceQty(Format.getInt(invoiceDetail.getItemInvoiceQty()));
					invoiceDisplay.setItemInvoiceAmount(Format.getFloat(invoiceDetail.getItemInvoiceAmount()));
				}
			}
			
			invoiceDetailVector.add(invoiceDisplay);
		}
		InvoiceDetailDisplayForm invoiceDetails[] = new InvoiceDetailDisplayForm[invoiceDetailVector.size()];
		invoiceDetailVector.copyInto(invoiceDetails);
		form.setInvoiceDetails(invoiceDetails);
		form.setEditable(false);
		form.setAllowCredit(false);
		InvoiceEngine invoiceEngine = new InvoiceEngine(invoiceHeader, null);
		//em.evict(invoiceHeader);
		form.setInputShippingTotal(Format.getFloat(invoiceEngine.getInvoiceHeader().getShippingTotal()));
		if (invoiceEngine.isOpen(invoiceHeader)) {
			form.setEditable(true);
		}
		if (invoiceEngine.isCompleted(invoiceHeader)) {
			form.setAllowCredit(true);
		}
		form.setNewInvoice(false);
		if (orderHeader.getPaymentGatewayProvider() == null) {
			if (invoiceHeader.getInvoiceStatus().equals(Constants.ORDERSTATUS_OPEN)) {
				form.setAllowCapture(true);
			}
			if (invoiceHeader.getInvoiceStatus().equals(Constants.ORDERSTATUS_COMPLETED)) {
				form.setAllowVoid(true);
			}
		}
		else {
			form.setAllowCapture(true);
			if (invoiceHeader.getPaymentTran() != null) {
				form.setAllowCapture(false);
			}
			form.setAllowVoid(false);
			if (invoiceHeader.getPaymentTran() != null && invoiceHeader.getVoidPaymentTran() == null) {
				form.setAllowVoid(true);
			}
		}
    	calcTotal(form, invoiceHeader.getInvoiceHeaderId(), invoiceEngine);
    	
		iterator = invoiceHeader.getInvoiceTrackings().iterator();
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
		form.getInvoiceHeader().setOrderTrackings(orderTrackings);
	
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}

    public ActionForward save(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	InvoiceMaintActionForm form = (InvoiceMaintActionForm) actionForm;
    	AdminBean adminBean = getAdminBean(request);
    	User user = adminBean.getUser();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
		getMissingFormInformation(form, adminBean.getSiteId());

    	String invoiceHeaderId = form.getInvoiceHeaderId();
    	InvoiceHeader invoiceHeader = null;
    	if (!Format.isNullOrEmpty(invoiceHeaderId)) {
    		invoiceHeader = (InvoiceHeader) em.find(InvoiceHeader.class, Format.getLong(form.getInvoiceHeaderId()));
    	}
   	
    	InvoiceEngine invoiceEngine = null;
    	if (invoiceHeader == null) {
    		invoiceEngine = new InvoiceEngine(orderHeader, user);
    	}
    	else {
    		invoiceEngine = new InvoiceEngine(invoiceHeader, user);
    	}
    	invoiceEngine.setShippingTotal(0);
    	
    	initOrder(form, orderHeader, invoiceHeader, null, null, request);
    	ActionMessages messages = validateOther(form);
    	if (messages.size() == 0) {
    		invoiceEngine.setShippingTotal(Format.getFloat(form.getInputShippingTotal()));
    	}
    	boolean isValid = validateInput(form, request);
    	if (!isValid || messages.size() > 0) {
        	initOrder(form, orderHeader, null, null, null, request);
        	invoiceEngine.calculateHeader();
    		saveMessages(request, messages);
    		Long id = null;
    		if (!Format.isNullOrEmpty(invoiceHeaderId)) {
    			id = Format.getLong(invoiceHeaderId);
    		}
        	calcTotal(form, id, invoiceEngine);
    		form.setEditable(true);
    		form.setNewInvoice(true);
        	if (!Format.isNullOrEmpty(invoiceHeaderId)) {
        		form.setNewInvoice(false);
        	}
    		em.getTransaction().rollback();
    		ActionForward actionForward = actionMapping.findForward("error");
    		return actionForward;
    	}
    	
    	saveInput(form, invoiceEngine);
    	invoiceEngine.saveOrder();
    	orderHeader = invoiceEngine.getOrderHeader();
    	OrderEngine orderEngine = new OrderEngine(orderHeader, user);
    	orderHeader.setOrderStatus(orderEngine.calcStatus(orderHeader));
    	invoiceHeader = invoiceEngine.getInvoiceHeader();
    	form.setInvoiceHeaderId(invoiceHeader.getInvoiceHeaderId().toString());
    	// refresh form
    	initOrder(form, orderHeader, invoiceHeader, null, null, request);
    	calcTotal(form, invoiceHeader.getInvoiceHeaderId(), invoiceEngine);
		form.setEditable(true);
		form.setNewInvoice(false);
		form.setAllowCapture(true);
		form.setAllowVoid(false);
		form.setAllowCredit(false);
		
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
    }
    
    private void getMissingFormInformation(InvoiceMaintActionForm form, String siteId) throws NumberFormatException, SecurityException, Exception {
    	for (InvoiceDetailDisplayForm detailForm : form.getInvoiceDetails()) {
    		OrderItemDetail orderItemDetail = OrderItemDetailDAO.load(siteId, Long.valueOf(detailForm.getOrderItemDetailId()));
    		Vector<OrderItemAttributeBean> orderItemAttributes = getOrderItemAttributes(orderItemDetail);
    		detailForm.setOrderItemAttributes(orderItemAttributes);
    	}
    }
    
    public void calcTotal(InvoiceMaintActionForm form, Long invoiceHeaderId, InvoiceEngine invoiceEngine) {
    	InvoiceHeaderDisplayForm invoiceHeaderDisplayForm = form.getInvoiceHeader();
    	if (invoiceHeaderDisplayForm == null) {
	    	Iterator<?> iterator = form.getInvoiceHeaders().iterator();
	    	while (iterator.hasNext()) {
	    		invoiceHeaderDisplayForm = (InvoiceHeaderDisplayForm) iterator.next();
	    		if (invoiceHeaderId != null) {
	        		if (invoiceHeaderDisplayForm.getInvoiceHeaderId().equals(invoiceHeaderId.toString())) {
	        			break;
	        		}
	    		}
	    	}
    	}
    	
    	if (invoiceHeaderDisplayForm == null) {
    		invoiceHeaderDisplayForm = new InvoiceHeaderDisplayForm();
    	}
		form.setInvoiceHeader(invoiceHeaderDisplayForm);
		form.setInvoiceHeaderId(invoiceHeaderDisplayForm.getInvoiceHeaderId());
    	
    	int itemOrderTotal = 0;
    	int itemInvoiceTotal = 0;
    	int itemCreditTotal = 0;
    	int itemShipTotal = 0;
    	int inputInvoiceTotal = 0;
    	float itemInvoiceAmountTotal = 0;
    	InvoiceDetailDisplayForm invoiceDisplays[] = form.getInvoiceDetails();
    	for (int i = 0; i < invoiceDisplays.length; i++) {
    		InvoiceDetailDisplayForm invoiceDisplay = invoiceDisplays[i];
			itemOrderTotal += Format.getInt(invoiceDisplay.getItemOrderQty());
			itemInvoiceTotal += Format.getInt(invoiceDisplay.getItemInvoiceQty());
			itemCreditTotal += Format.getInt(invoiceDisplay.getItemCreditQty());
			itemShipTotal += Format.getInt(invoiceDisplay.getItemShipQty());
			if (Format.isInt(invoiceDisplay.getInputInvoiceQty())) {
				inputInvoiceTotal += Format.getInt(invoiceDisplay.getInputInvoiceQty());
			}
			itemInvoiceAmountTotal += Format.getFloat(invoiceDisplay.getItemInvoiceAmount());
    	}
		invoiceHeaderDisplayForm.setItemOrderQty(Format.getInt(itemOrderTotal));
		invoiceHeaderDisplayForm.setItemInvoiceQty(Format.getInt(itemInvoiceTotal));
		invoiceHeaderDisplayForm.setItemCreditQty(Format.getInt(itemCreditTotal));
		invoiceHeaderDisplayForm.setItemShipQty(Format.getInt(itemShipTotal));
		invoiceHeaderDisplayForm.setItemInvoiceAmount(Format.getFloat(itemInvoiceTotal));
		invoiceHeaderDisplayForm.setInputInvoiceQty(Format.getInt(inputInvoiceTotal));
		invoiceHeaderDisplayForm.setItemInvoiceAmount(Format.getFloat(itemInvoiceAmountTotal));
		
		invoiceHeaderDisplayForm.setShippingTotal(Format.getFloat(invoiceEngine.getInvoiceHeader().getShippingTotal()));
		
		Iterator<?> iterator = invoiceEngine.getInvoiceTaxes().iterator();
		Vector<OrderTax> orderTaxes = new Vector<OrderTax>();
		while (iterator.hasNext()) {
			InvoiceDetailTax invoiceDetailTax = (InvoiceDetailTax) iterator.next();
			OrderTax orderTax = new OrderTax();
			orderTax.setTaxName(invoiceDetailTax.getTax().getTaxLanguage().getTaxName());
			orderTax.setTaxAmount(Format.getFloat(invoiceDetailTax.getTaxAmount()));
			orderTaxes.add(orderTax);
		}
		form.setInvoiceTaxes(orderTaxes);
    }
    
    public boolean validateInput(InvoiceMaintActionForm form, HttpServletRequest request) {
		MessageResources resources = this.getResources(request);
    	boolean isClean = true;
    	InvoiceDetailDisplayForm invoiceDisplays[] = form.getInvoiceDetails();
    	for (int i = 0; i < invoiceDisplays.length; i++) {
    		InvoiceDetailDisplayForm invoiceDisplay = invoiceDisplays[i];
    		invoiceDisplay.setInputInvoiceQtyError("");
    		if (!Format.isInt(invoiceDisplay.getInputInvoiceQty())) {
    			isClean = false;
    			invoiceDisplay.setInputInvoiceQtyError(resources.getMessage("error.int.invalid"));
    		}
    		else {
	    		int inputInvoiceQty = Format.getInt(invoiceDisplay.getInputInvoiceQty());
	    		Iterator<?> iterator = form.getOrderItemDetails().iterator();
	    		while (iterator.hasNext()) {
	    			OrderItemDetailDisplayForm itemDisplay = (OrderItemDetailDisplayForm) iterator.next();
	    			if (itemDisplay.getOrderItemDetailId().equals(invoiceDisplay.getOrderItemDetailId())) {
		    			if (inputInvoiceQty < 0 || inputInvoiceQty > itemDisplay.getItemSuggestInvoiceQty()) {
		    				isClean = false;
		    				invoiceDisplay.setInputInvoiceQtyError(resources.getMessage("error.qty.invalid"));
		    			}
		    			break;
	    			}
	    		}
    		}
    	}
    	return isClean;
    }
    
    public ActionMessages validateOther(InvoiceMaintActionForm form) throws Exception {
    	ActionMessages errors = new ActionMessages();
    	if (!Format.isFloat(form.getInputShippingTotal())) {
    		errors.add("inputShippingTotal", new ActionMessage("error.float.required"));
    	}
    	return errors;
    }
    
    public void saveInput(InvoiceMaintActionForm form, InvoiceEngine invoiceEngine) throws Exception {
    	InvoiceDetailDisplayForm invoiceDisplays[] = form.getInvoiceDetails();
    	for (int i = 0; i < invoiceDisplays.length; i++) {
    		InvoiceDetailDisplayForm invoiceDisplay = invoiceDisplays[i];
    		int qty = 0;
    		if (Format.isNullOrEmpty(invoiceDisplay.getInputInvoiceQty())) {
    			qty = 0;
    		}
    		else {
    			qty = Format.getInt(invoiceDisplay.getInputInvoiceQty());
    		}
    		qty = Format.getInt(invoiceDisplay.getInputInvoiceQty());
    		invoiceEngine.setQty(invoiceDisplay.getItemSkuCd(), qty);
    		InvoiceDetail invoiceDetail = invoiceEngine.getLastInvoiceDetail();
    		invoiceDisplay.setItemInvoiceAmount(Format.getFloat(invoiceDetail.getItemInvoiceAmount()));
    	}
    	invoiceEngine.calculateHeader();
    }
    
    public ActionForward cancel(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	InvoiceMaintActionForm form = (InvoiceMaintActionForm) actionForm;
		MessageResources resources = this.getResources(request);
    	AdminBean adminBean = getAdminBean(request);
    	User user = adminBean.getUser();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
    	
    	InvoiceHeader invoiceHeader = (InvoiceHeader) em.find(InvoiceHeader.class, Format.getLong(form.getInvoiceHeaderId()));
    	InvoiceEngine invoiceEngine = new InvoiceEngine(invoiceHeader, user);
    	initOrder(form, orderHeader, invoiceHeader, null, null, request);

    	try {
    		invoiceEngine.cancelOrder();
    	} catch (OrderStateException exception) {
        	ActionMessages errors = new ActionMessages();
        	errors.add("invoice", new ActionMessage("error.invoice.cancel"));
    		ActionForward actionForward = actionMapping.findForward("error");
    		return actionForward;
    	}
    	
    	invoiceEngine.saveHeader();
		form.setEditable(false);
		form.setNewInvoice(false);
		form.setAllowCapture(false);
		form.setAllowVoid(false);
    	
    	InvoiceHeaderDisplayForm invoiceHeaderDisplayForm = form.getInvoiceHeader();
		invoiceHeaderDisplayForm.setInvoiceStatus(resources.getMessage("order.status." + invoiceHeader.getInvoiceStatus()));
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
    }
    
    public ActionForward capture(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	InvoiceMaintActionForm form = (InvoiceMaintActionForm) actionForm;
    	AdminBean adminBean = getAdminBean(request);
    	User user = adminBean.getUser();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
		getMissingFormInformation(form, adminBean.getSiteId());

    	String invoiceHeaderId = form.getInvoiceHeaderId();
    	InvoiceHeader invoiceHeader = null;
    	if (!Format.isNullOrEmpty(invoiceHeaderId)) {
    		invoiceHeader = (InvoiceHeader) em.find(InvoiceHeader.class, Format.getLong(form.getInvoiceHeaderId()));
    	}

    	InvoiceEngine invoiceEngine = null;
    	if (invoiceHeader == null) {
    		invoiceEngine = new InvoiceEngine(orderHeader, user);
    	}
    	else {
    		invoiceEngine = new InvoiceEngine(invoiceHeader, user);
    	}
    	invoiceEngine.setShippingTotal(0);
    	ActionMessages messages = validateOther(form);
    	if (messages.size() == 0) {
    		invoiceEngine.setShippingTotal(Format.getFloat(form.getInputShippingTotal()));
    	}
    	
    	initOrder(form, orderHeader, invoiceHeader, null, null, request);
    	boolean isValid = validateInput(form, request);
    	if (!isValid) {
        	initOrder(form, orderHeader, null, null, null, request);
    		Long id = null;
    		if (!Format.isNullOrEmpty(invoiceHeaderId)) {
    			id = Format.getLong(invoiceHeaderId);
    		}
        	calcTotal(form, id, invoiceEngine);
    		form.setEditable(true);
    		form.setNewInvoice(true);
        	if (!Format.isNullOrEmpty(invoiceHeaderId)) {
        		form.setNewInvoice(false);
        	}
    		em.getTransaction().rollback();
    		ActionForward actionForward = actionMapping.findForward("error");
    		return actionForward;
    	}
    	saveInput(form, invoiceEngine);
    	
    	try {
    		invoiceEngine.payOrder(request);
        	invoiceEngine.saveOrder();
        	OrderEngine orderEngine = new OrderEngine(orderHeader, user);
        	orderHeader.setOrderStatus(orderEngine.calcStatus(orderHeader));
    	}
    	catch (OrderStateException e) {
        	initOrder(form, orderHeader, invoiceHeader, null, null, request);
        	calcTotal(form, invoiceHeader.getInvoiceHeaderId(), invoiceEngine);
        	ActionMessages errors = new ActionMessages();
        	errors.add("invoice", new ActionMessage("error.invoice.capture"));
    		ActionForward actionForward = actionMapping.findForward("error");
    		saveMessages(request, errors);
    		form.setAllowCapture(true);
    		form.setEditable(true);
    		form.setNewInvoice(false);
    		form.setAllowCredit(false);
    		em.getTransaction().rollback();
    		return actionForward;
    	}
    	catch (PaymentException e) {
        	initOrder(form, orderHeader, invoiceHeader, null, null, request);
        	calcTotal(form, invoiceHeader.getInvoiceHeaderId(), invoiceEngine);
        	ActionMessages errors = new ActionMessages();
        	errors.add("invoice", new ActionMessage("error.invoice.capture", e.getMessage()));
    		ActionForward actionForward = actionMapping.findForward("error");
    		saveMessages(request, errors);
    		form.setAllowCapture(true);
    		form.setEditable(true);
    		form.setNewInvoice(false);
    		form.setAllowCredit(false);
    		em.getTransaction().rollback();
    		return actionForward;	
    	}
    	catch (AuthorizationException e) {
        	initOrder(form, orderHeader, invoiceHeader, null, null, request);
        	calcTotal(form, invoiceHeader.getInvoiceHeaderId(), invoiceEngine);
        	ActionMessages errors = new ActionMessages();
        	errors.add("invoice", new ActionMessage("error.invoice.capture", e.getMessage()));
    		ActionForward actionForward = actionMapping.findForward("error");
    		saveMessages(request, errors);
    		form.setAllowCapture(true);
    		form.setEditable(true);
    		form.setNewInvoice(false);
    		form.setAllowCredit(false);
    		em.getTransaction().rollback();
    		return actionForward;	
    	}
    	catch (NotImplementedException e) {
        	initOrder(form, orderHeader, invoiceHeader, null, null, request);
        	calcTotal(form, invoiceHeader.getInvoiceHeaderId(), invoiceEngine);
        	ActionMessages errors = new ActionMessages();
        	errors.add("invoice", new ActionMessage("error.payment.notImplemeted", e.getMessage()));
    		ActionForward actionForward = actionMapping.findForward("error");
    		saveMessages(request, errors);
    		form.setAllowCapture(true);
    		form.setEditable(true);
    		form.setNewInvoice(false);
    		form.setAllowCredit(false);
    		em.getTransaction().rollback();
    		return actionForward;	
    	}
    	initOrder(form, invoiceEngine.getOrderHeader(), invoiceEngine.getInvoiceHeader(), null, null, request);
    	calcTotal(form, invoiceEngine.getInvoiceHeader().getInvoiceHeaderId(), invoiceEngine);
		form.setNewInvoice(false);
		form.setAllowCapture(false);
		form.setAllowVoid(true);
		form.setAllowCredit(true);
    	
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
    }
    
    public ActionForward voidOrder(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	InvoiceMaintActionForm form = (InvoiceMaintActionForm) actionForm;
    	AdminBean adminBean = getAdminBean(request);
    	User user = adminBean.getUser();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
		InvoiceHeader invoiceHeader = (InvoiceHeader) em.find(InvoiceHeader.class, Format.getLong(form.getInvoiceHeaderId()));
		getMissingFormInformation(form, adminBean.getSiteId());

    	InvoiceEngine invoiceEngine = new InvoiceEngine(invoiceHeader, user);
    	try {
    		invoiceEngine.voidOrder();
    		invoiceEngine.saveOrder();
        	OrderEngine orderEngine = new OrderEngine(orderHeader, user);
        	orderHeader.setOrderStatus(orderEngine.calcStatus(orderHeader));
    	}
    	catch (OrderStateException e) {
        	initOrder(form, orderHeader, invoiceHeader, null, null, request);
        	calcTotal(form, invoiceHeader.getInvoiceHeaderId(), invoiceEngine);
        	ActionMessages errors = new ActionMessages();
        	errors.add("invoice", new ActionMessage("error.invoice.void"));
    		ActionForward actionForward = actionMapping.findForward("error");
    		saveMessages(request, errors);
    		form.setAllowCapture(false);
    		form.setAllowVoid(true);
    		form.setEditable(true);
    		form.setNewInvoice(false);
    		return actionForward;
    	}
    	catch (AuthorizationException e) {
        	initOrder(form, orderHeader, invoiceHeader, null, null, request);
        	calcTotal(form, invoiceHeader.getInvoiceHeaderId(), invoiceEngine);
        	ActionMessages errors = new ActionMessages();
        	errors.add("invoice", new ActionMessage("error.invoice.capture", e.getMessage()));
    		ActionForward actionForward = actionMapping.findForward("error");
    		saveMessages(request, errors);
    		form.setAllowCapture(false);
    		form.setAllowVoid(true);
    		form.setEditable(true);
    		form.setNewInvoice(false);
    		return actionForward;	
    	}
    	catch (NotImplementedException e) {
        	initOrder(form, orderHeader, invoiceHeader, null, null, request);
        	calcTotal(form, invoiceHeader.getInvoiceHeaderId(), invoiceEngine);
        	ActionMessages errors = new ActionMessages();
        	errors.add("invoice", new ActionMessage("error.payment.notImplemeted", e.getMessage()));
    		ActionForward actionForward = actionMapping.findForward("error");
    		saveMessages(request, errors);
    		form.setAllowCapture(false);
    		form.setAllowVoid(true);
    		form.setEditable(true);
    		form.setNewInvoice(false);
    		return actionForward;	
    	}
    	initOrder(form, invoiceEngine.getOrderHeader(), invoiceEngine.getInvoiceHeader(), null, null, request);
    	calcTotal(form, invoiceHeader.getInvoiceHeaderId(), invoiceEngine);
		form.setNewInvoice(false);
		form.setAllowCapture(false);
		form.setAllowVoid(false);
		form.setAllowCredit(false);
    	
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
    }
    
    public ActionForward comment(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	InvoiceMaintActionForm form = (InvoiceMaintActionForm) actionForm;
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
		InvoiceHeader invoiceHeader = (InvoiceHeader) em.find(InvoiceHeader.class, Format.getLong(form.getInvoiceHeaderId()));
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
    		orderTracking.setInvoiceHeader(invoiceHeader);
    		em.persist(orderTracking);
    		
    		invoiceHeader.getInvoiceTrackings().add(orderTracking);
    		
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
    	InvoiceEngine invoiceEngine = new InvoiceEngine(invoiceHeader, null);
    	initOrder(form, orderHeader, invoiceHeader, null, null, request);
    	calcTotal(form, invoiceHeader.getInvoiceHeaderId(), invoiceEngine);
    	
		if (invoiceEngine.isOpen(invoiceHeader)) {
			form.setEditable(true);
		}
		form.setAllowCapture(true);
		if (invoiceHeader.getPaymentTran() != null) {
			form.setAllowCapture(false);
		}
		form.setNewInvoice(false);
		form.setAllowVoid(false);
		if (invoiceHeader.getPaymentTran() != null && invoiceHeader.getVoidPaymentTran() == null) {
			form.setAllowVoid(true);
		}
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}
    
    public ActionForward updateInternal(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	InvoiceMaintActionForm form = (InvoiceMaintActionForm) actionForm;
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		AdminBean adminBean = getAdminBean(request);
		User user = adminBean.getUser();
    	OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
		InvoiceHeader invoiceHeader = (InvoiceHeader) em.find(InvoiceHeader.class, Format.getLong(form.getInvoiceHeaderId()));
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
    	InvoiceEngine invoiceEngine = new InvoiceEngine(invoiceHeader, null);
    	initOrder(form, orderHeader, invoiceHeader, null, null, request);
    	calcTotal(form, invoiceHeader.getInvoiceHeaderId(), invoiceEngine);
		if (invoiceEngine.isOpen(invoiceHeader)) {
			form.setEditable(true);
		}
		form.setAllowCapture(true);
		if (invoiceHeader.getPaymentTran() != null) {
			form.setAllowCapture(false);
		}
		form.setNewInvoice(false);
		form.setAllowVoid(false);
		if (invoiceHeader.getPaymentTran() != null && invoiceHeader.getVoidPaymentTran() == null) {
			form.setAllowVoid(true);
		}
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
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
