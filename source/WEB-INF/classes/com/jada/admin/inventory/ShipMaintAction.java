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
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.OrderItemDetail;
import com.jada.jpa.entity.OrderTracking;
import com.jada.jpa.entity.ShipDetail;
import com.jada.jpa.entity.ShipHeader;
import com.jada.jpa.entity.User;
import com.jada.order.document.OrderEngine;
import com.jada.order.document.OrderStateException;
import com.jada.order.document.ShipEngine;
import com.jada.order.payment.AuthorizationException;
import com.jada.util.Constants;
import com.jada.util.Format;

public class ShipMaintAction extends OrderMaintBaseAction {
    public ActionForward create(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	ShipMaintActionForm form = (ShipMaintActionForm) actionForm;
    	AdminBean adminBean = getAdminBean(request);
    	User user = adminBean.getUser();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
		
		ShipEngine shipEngine = new ShipEngine(orderHeader, user);
		shipEngine.shipAll();
    	initOrder(form, orderHeader, null, null, null, request);
		ShipHeader shipHeader = shipEngine.getShipHeader();
    	ShipHeaderDisplayForm shipHeaderDisplayForm = new ShipHeaderDisplayForm();
    	shipHeaderDisplayForm.setUpdateInventory(true);
    	form.setShipHeader(shipHeaderDisplayForm);

		Iterator<?> iterator = form.getOrderItemDetails().iterator();
		Vector<ShipDetailDisplayForm> shipDetailVector = new Vector<ShipDetailDisplayForm>();
		while (iterator.hasNext()) {
			OrderItemDetailDisplayForm itemDisplay = (OrderItemDetailDisplayForm) iterator.next();
			ShipDetailDisplayForm shipDisplay = new ShipDetailDisplayForm();
			shipDisplay.setOrderItemDetailId(itemDisplay.getOrderItemDetailId());
			shipDisplay.setItemId(itemDisplay.getItemId());
			shipDisplay.setItemNum(itemDisplay.getItemNum());
			shipDisplay.setItemSkuCd(itemDisplay.getItemSkuCd());
			shipDisplay.setItemShortDesc(itemDisplay.getItemShortDesc());
			shipDisplay.setItemTierQty(itemDisplay.getItemTierQty());
			shipDisplay.setItemTierPrice(itemDisplay.getItemTierPrice());
			shipDisplay.setItemOrderQty(itemDisplay.getItemOrderQty());
			shipDisplay.setItemInvoiceQty(itemDisplay.getItemInvoiceQty());
			shipDisplay.setItemCreditQty(itemDisplay.getItemCreditQty());
			shipDisplay.setItemShipQty(itemDisplay.getItemShipQty());
			shipDisplay.setInputShipQty("");
			shipDisplay.setOrderItemAttributes(itemDisplay.getOrderItemAttributes());

			Iterator<?> shipDetailIterator = shipHeader.getShipDetails().iterator();
			while (shipDetailIterator.hasNext()) {
				ShipDetail shipDetail = (ShipDetail) shipDetailIterator.next();
				if (itemDisplay.getOrderItemDetailId().equals(shipDetail.getOrderItemDetail().getOrderItemDetailId().toString())) {
					shipDisplay.setInputShipQty(Format.getInt(shipDetail.getItemShipQty()));
				}
			}
			
			shipDetailVector.add(shipDisplay);
		}
		ShipDetailDisplayForm shipDetails[] = new ShipDetailDisplayForm[shipDetailVector.size()];
		shipDetailVector.copyInto(shipDetails);
		form.setShipDetails(shipDetails);
		form.setEditable(true);
		form.setNewShip(true);
		form.setAllowCapture(false);
		form.setUpdateInventory(true);
		calcTotal(form, null, shipEngine);
		em.getTransaction().rollback();
		
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
    }
    
    public ActionForward edit(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	ShipMaintActionForm form = (ShipMaintActionForm) actionForm;
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ShipHeader shipHeader = (ShipHeader) em.find(ShipHeader.class, Format.getLong(form.getShipHeaderId()));
		OrderHeader orderHeader = shipHeader.getOrderHeader();
    	initOrder(form, orderHeader, null, null, shipHeader, request);

		Iterator<?> iterator = form.getOrderItemDetails().iterator();
		Vector<ShipDetailDisplayForm> shipDetailVector = new Vector<ShipDetailDisplayForm>();
		while (iterator.hasNext()) {
			OrderItemDetailDisplayForm itemDisplay = (OrderItemDetailDisplayForm) iterator.next();
			ShipDetailDisplayForm shipDisplay = new ShipDetailDisplayForm();
			shipDisplay.setOrderItemDetailId(itemDisplay.getOrderItemDetailId());
			shipDisplay.setItemId(itemDisplay.getItemId());
			shipDisplay.setItemNum(itemDisplay.getItemNum());
			shipDisplay.setItemSkuCd(itemDisplay.getItemSkuCd());
			shipDisplay.setItemShortDesc(itemDisplay.getItemShortDesc());
			shipDisplay.setItemTierQty(itemDisplay.getItemTierQty());
			shipDisplay.setItemTierPrice(itemDisplay.getItemTierPrice());
			shipDisplay.setItemOrderQty(itemDisplay.getItemOrderQty());
			shipDisplay.setItemInvoiceQty(itemDisplay.getItemInvoiceQty());
			shipDisplay.setItemCreditQty(itemDisplay.getItemCreditQty());
			shipDisplay.setItemShipQty(itemDisplay.getItemShipQty());
			shipDisplay.setInputShipQty("0");
			shipDisplay.setOrderItemAttributes(itemDisplay.getOrderItemAttributes());
	
			Iterator<?> shipDetailIterator = shipHeader.getShipDetails().iterator();
			while (shipDetailIterator.hasNext()) {
				ShipDetail shipDetail = (ShipDetail) shipDetailIterator.next();
				if (itemDisplay.getOrderItemDetailId().equals(shipDetail.getOrderItemDetail().getOrderItemDetailId().toString())) {
					shipDisplay.setInputShipQty(Format.getInt(shipDetail.getItemShipQty()));
				}
			}
			
			shipDetailVector.add(shipDisplay);
		}
		ShipDetailDisplayForm shipDetails[] = new ShipDetailDisplayForm[shipDetailVector.size()];
		shipDetailVector.copyInto(shipDetails);
		form.setShipDetails(shipDetails);

		form.setEditable(false);
		ShipEngine shipEngine = new ShipEngine(shipHeader, null);
		//em.evict(shipHeader);
		form.setUpdateInventory(false);
		if (shipHeader.getUpdateInventory().equals(String.valueOf(Constants.VALUE_YES))) {
			form.setUpdateInventory(true);
		}
		if (shipEngine.isOpen(shipHeader)) {
			form.setEditable(true);
		}
		form.setNewShip(false);
		form.setAllowCapture(false);
		if (shipHeader.getShipStatus().equals(Constants.ORDERSTATUS_OPEN)) {
			form.setAllowCapture(true);
		}
		form.setAllowVoid(false);
		if (shipHeader.getShipStatus().equals(Constants.ORDERSTATUS_COMPLETED)) {
			form.setAllowVoid(true);
		}
    	calcTotal(form, shipHeader.getShipHeaderId(), shipEngine);
    	
		iterator = shipHeader.getShipTrackings().iterator();
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
		form.getShipHeader().setOrderTrackings(orderTrackings);
	
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}

    public ActionForward save(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	ShipMaintActionForm form = (ShipMaintActionForm) actionForm;
    	AdminBean adminBean = getAdminBean(request);
    	User user = adminBean.getUser();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
		getMissingFormInformation(form, adminBean.getSiteId());

    	String shipHeaderId = form.getShipHeaderId();
    	ShipHeader shipHeader = null;
    	if (!Format.isNullOrEmpty(shipHeaderId)) {
    		shipHeader = (ShipHeader) em.find(ShipHeader.class, Format.getLong(form.getShipHeaderId()));
    	}
   	
    	ShipEngine shipEngine = null;
    	if (shipHeader == null) {
    		shipEngine = new ShipEngine(orderHeader, user);
    	}
    	else {
    		shipEngine = new ShipEngine(shipHeader, user);
    	}
    	shipEngine.setUpdateInventory(form.isUpdateInventory());
    	
    	initOrder(form, orderHeader, null, null, shipHeader, request);
    	boolean isValid = validateInput(form, request);
    	ShipDetailDisplayForm shipDisplays[] = form.getShipDetails();
    	for (int i = 0; i < shipDisplays.length; i++) {
    		ShipDetailDisplayForm shipDisplay = shipDisplays[i];
    		if (!Format.isNullOrEmpty(shipDisplay.getInputShipQtyError())) {
    			continue;
    		}
    		shipEngine.setQty(shipDisplay.getItemSkuCd(),  Format.getInt(shipDisplay.getInputShipQty()));
    	}
    	shipEngine.calculateHeader();
    	initOrder(form, orderHeader, null, null, null, request);
    	if (!isValid) {
    		Long id = null;
    		if (!Format.isNullOrEmpty(shipHeaderId)) {
    			id = Format.getLong(shipHeaderId);
    		}
        	calcTotal(form, id, shipEngine);
    		form.setNewShip(true);
        	if (!Format.isNullOrEmpty(shipHeaderId)) {
        		form.setNewShip(false);
        	}
        	form.setEditable(true);
    		em.getTransaction().rollback();
    		ActionForward actionForward = actionMapping.findForward("error");
    		return actionForward;
    	}
    	
    	saveInput(form, shipEngine);
    	orderHeader = shipEngine.getOrderHeader();
    	OrderEngine orderEngine = new OrderEngine(orderHeader, user);
    	orderHeader.setOrderStatus(orderEngine.calcStatus(orderHeader));
    	shipHeader = shipEngine.getShipHeader();
    	form.setShipHeaderId(shipHeader.getShipHeaderId().toString());
    	// refresh form
		form.setAllowCapture(false);
		if (shipHeader.getShipStatus().equals(Constants.ORDERSTATUS_OPEN)) {
			form.setAllowCapture(true);
		}
		form.setAllowVoid(false);
		if (shipHeader.getShipStatus().equals(Constants.ORDERSTATUS_COMPLETED)) {
			form.setAllowVoid(true);
		}
		initOrder(form, orderHeader, null,  null, shipHeader, request);
    	calcTotal(form, shipHeader.getShipHeaderId(), shipEngine);
		form.setEditable(true);
		form.setNewShip(false);

		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
    }
    
    public void calcTotal(ShipMaintActionForm form, Long shipHeaderId, ShipEngine shipEngine) {
    	ShipHeaderDisplayForm shipHeaderDisplayForm = form.getShipHeader();
    	if (shipHeaderDisplayForm == null) {
	    	Iterator<?> iterator = form.getShipHeaders().iterator();
	    	while (iterator.hasNext()) {
	    		shipHeaderDisplayForm = (ShipHeaderDisplayForm) iterator.next();
	    		if (shipHeaderId != null) {
	        		if (shipHeaderDisplayForm.getShipHeaderId().equals(shipHeaderId)) {
	        			break;
	        		}
	    		}
	    	}
    	}
    	form.setShipHeader(shipHeaderDisplayForm);
    	form.setShipHeaderId(shipHeaderDisplayForm.getShipHeaderId());
    	
    	int itemOrderTotal = 0;
    	int itemInvoiceTotal = 0;
    	int itemCreditTotal = 0;
    	int itemShipTotal = 0;
    	int inputShipTotal = 0;
    	ShipDetailDisplayForm shipDisplays[] = form.getShipDetails();
    	for (int i = 0; i < shipDisplays.length; i++) {
    		ShipDetailDisplayForm shipDisplay = shipDisplays[i];
			itemOrderTotal += Format.getInt(shipDisplay.getItemOrderQty());
			itemInvoiceTotal += Format.getInt(shipDisplay.getItemInvoiceQty());
			itemCreditTotal += Format.getInt(shipDisplay.getItemCreditQty());
			itemShipTotal += Format.getInt(shipDisplay.getItemShipQty());
			if (Format.isInt(shipDisplay.getInputShipQty())) {
				inputShipTotal += Format.getInt(shipDisplay.getInputShipQty());
			}
    	}
		shipHeaderDisplayForm.setItemOrderQty(Format.getInt(itemOrderTotal));
		shipHeaderDisplayForm.setItemInvoiceQty(Format.getInt(itemInvoiceTotal));
		shipHeaderDisplayForm.setItemCreditQty(Format.getInt(itemCreditTotal));
		shipHeaderDisplayForm.setItemShipQty(Format.getInt(itemShipTotal));
		shipHeaderDisplayForm.setInputShipQty(Format.getInt(inputShipTotal));
    }
    
    public boolean validateInput(ShipMaintActionForm form, HttpServletRequest request) {
		MessageResources resources = this.getResources(request);
    	boolean isClean = true;
    	ShipDetailDisplayForm shipDisplays[] = form.getShipDetails();
    	for (int i = 0; i < shipDisplays.length; i++) {
    		ShipDetailDisplayForm shipDisplay = shipDisplays[i];
    		shipDisplay.setInputShipQtyError("");
    		if (!Format.isInt(shipDisplay.getInputShipQty())) {
    			isClean = false;
    			shipDisplay.setInputShipQtyError(resources.getMessage("error.int.invalid"));
    		}
    		else {
	    		int inputShipQty = Format.getInt(shipDisplay.getInputShipQty());
	    		Iterator<?> iterator = form.getOrderItemDetails().iterator();
	    		while (iterator.hasNext()) {
	    			OrderItemDetailDisplayForm itemDisplay = (OrderItemDetailDisplayForm) iterator.next();
	    			if (itemDisplay.getOrderItemDetailId().equals(shipDisplay.getOrderItemDetailId())) {
		    			if (inputShipQty < 0 || inputShipQty > itemDisplay.getItemSuggestShipQty()) {
		    				isClean = false;
		    				shipDisplay.setInputShipQtyError(resources.getMessage("error.qty.invalid"));
		    			}
		    			break;
	    			}
	    		}
    		}
    	}
    	return isClean;
    }
    
    public void saveInput(ShipMaintActionForm form, ShipEngine shipEngine) throws Exception {
    	ShipDetailDisplayForm shipDisplays[] = form.getShipDetails();
    	for (int i = 0; i < shipDisplays.length; i++) {
    		ShipDetailDisplayForm shipDisplay = shipDisplays[i];
    		if (Format.isNullOrEmpty(shipDisplay.getInputShipQty())) {
    			continue;
    		}
    		int qty = Format.getInt(shipDisplay.getInputShipQty());
    		if (qty == 0) {
    			continue;
    		}
    		shipEngine.setQty(shipDisplay.getItemSkuCd(), qty);
    	}
    	shipEngine.saveOrder();
    }
    
    public ActionForward cancel(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	ShipMaintActionForm form = (ShipMaintActionForm) actionForm;
		MessageResources resources = this.getResources(request);
    	AdminBean adminBean = getAdminBean(request);
    	User user = adminBean.getUser();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
    	
    	ShipHeader shipHeader = (ShipHeader) em.find(ShipHeader.class, Format.getLong(form.getShipHeaderId()));
    	ShipEngine shipEngine = new ShipEngine(shipHeader, user);
    	initOrder(form, orderHeader, null, null, shipHeader, request);

    	try {
    		shipEngine.cancelOrder();
    	} catch (OrderStateException exception) {
        	ActionMessages errors = new ActionMessages();
        	errors.add("ship", new ActionMessage("error.ship.cancel"));
    		ActionForward actionForward = actionMapping.findForward("error");
    		return actionForward;
    	}
    	
    	shipEngine.saveHeader();
		form.setEditable(false);
		form.setNewShip(false);
    	
    	ShipHeaderDisplayForm shipHeaderDisplayForm = form.getShipHeader();
		shipHeaderDisplayForm.setShipStatus(resources.getMessage("order.status." + shipHeader.getShipStatus()));
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
    }
    
    public ActionForward capture(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	ShipMaintActionForm form = (ShipMaintActionForm) actionForm;
    	AdminBean adminBean = getAdminBean(request);
    	User user = adminBean.getUser();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
		getMissingFormInformation(form, adminBean.getSiteId());

    	String shipHeaderId = form.getShipHeaderId();
    	ShipHeader shipHeader = null;
    	if (!Format.isNullOrEmpty(shipHeaderId)) {
    		shipHeader = (ShipHeader) em.find(ShipHeader.class, Format.getLong(form.getShipHeaderId()));
    	}

    	ShipEngine shipEngine = null;
    	if (shipHeader == null) {
    		shipEngine = new ShipEngine(orderHeader, user);
    	}
    	else {
    		shipEngine = new ShipEngine(shipHeader, user);
    	}
    	shipEngine.setUpdateInventory(form.isUpdateInventory());
    	initOrder(form, orderHeader, null, null, shipHeader, request);
    	boolean isValid = validateInput(form, request);
    	ShipDetailDisplayForm shipDisplays[] = form.getShipDetails();
    	for (int i = 0; i < shipDisplays.length; i++) {
    		ShipDetailDisplayForm shipDisplay = shipDisplays[i];
    		if (!Format.isNullOrEmpty(shipDisplay.getInputShipQtyError())) {
    			continue;
    		}
    		shipEngine.setQty(shipDisplay.getItemSkuCd(),  Format.getInt(shipDisplay.getInputShipQty()));
    	}
    	shipEngine.calculateHeader();
    	initOrder(form, orderHeader, null, null, null, request);

    	if (!isValid) {
    		Long id = null;
    		if (!Format.isNullOrEmpty(shipHeaderId)) {
    			id = Format.getLong(shipHeaderId);
    		}
        	calcTotal(form, id, shipEngine);
    		form.setEditable(true);
    		form.setNewShip(true);
        	if (!Format.isNullOrEmpty(shipHeaderId)) {
        		form.setNewShip(false);
        	}
    		em.getTransaction().rollback();
    		ActionForward actionForward = actionMapping.findForward("error");
    		return actionForward;
    	}
    	saveInput(form, shipEngine);
    	
    	try {
    		shipEngine.shipOrder(request);
    		shipEngine.saveOrder();
        	OrderEngine orderEngine = new OrderEngine(orderHeader, user);
        	orderHeader.setOrderStatus(orderEngine.calcStatus(orderHeader));
    	}
    	catch (OrderStateException e) {
        	initOrder(form, orderHeader, null,  null, shipHeader, request);
        	calcTotal(form, shipHeader.getShipHeaderId(), shipEngine);
        	ActionMessages errors = new ActionMessages();
        	errors.add("ship", new ActionMessage("error.ship.capture"));
    		ActionForward actionForward = actionMapping.findForward("error");
    		saveMessages(request, errors);
    		form.setEditable(true);
    		form.setNewShip(false);
    		return actionForward;
    	}
    	catch (AuthorizationException e) {
        	initOrder(form, orderHeader, null, null, shipHeader, request);
        	calcTotal(form, shipHeader.getShipHeaderId(), shipEngine);
        	ActionMessages errors = new ActionMessages();
        	errors.add("ship", new ActionMessage("error.ship.capture", e.getMessage()));
    		ActionForward actionForward = actionMapping.findForward("error");
    		saveMessages(request, errors);
    		form.setEditable(true);
    		form.setNewShip(false);
    		return actionForward;	
    	}
    	initOrder(form, shipEngine.getOrderHeader(), null, null, shipEngine.getShipHeader(), request);
    	calcTotal(form, shipEngine.getShipHeader().getShipHeaderId(), shipEngine);
    	form.setNewShip(false);
		form.setAllowCapture(false);
		form.setAllowVoid(true);
 
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
    }
    
    public ActionForward voidOrder(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	ShipMaintActionForm form = (ShipMaintActionForm) actionForm;
    	AdminBean adminBean = getAdminBean(request);
    	User user = adminBean.getUser();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
		ShipHeader shipHeader = (ShipHeader) em.find(ShipHeader.class, Format.getLong(form.getShipHeaderId()));
		getMissingFormInformation(form, adminBean.getSiteId());

    	ShipEngine shipEngine = new ShipEngine(shipHeader, user);
    	try {
    		shipEngine.voidShip();
    		shipEngine.saveOrder();
        	OrderEngine orderEngine = new OrderEngine(orderHeader, user);
        	orderHeader.setOrderStatus(orderEngine.calcStatus(orderHeader));
    	}
    	catch (OrderStateException e) {
        	initOrder(form, orderHeader, null, null, shipHeader, request);
        	calcTotal(form, shipHeader.getShipHeaderId(), shipEngine);
        	ActionMessages errors = new ActionMessages();
        	errors.add("ship", new ActionMessage("error.ship.void"));
    		ActionForward actionForward = actionMapping.findForward("error");
    		saveMessages(request, errors);
    		form.setEditable(true);
        	form.setNewShip(false);
    		return actionForward;
    	}
    	catch (AuthorizationException e) {
        	initOrder(form, orderHeader, null, null, shipHeader, request);
        	calcTotal(form, shipHeader.getShipHeaderId(), shipEngine);
        	ActionMessages errors = new ActionMessages();
        	errors.add("ship", new ActionMessage("error.ship.capture", e.getMessage()));
    		ActionForward actionForward = actionMapping.findForward("error");
    		saveMessages(request, errors);
    		form.setEditable(true);
        	form.setNewShip(false);
    		return actionForward;	
    	}
    	initOrder(form, shipEngine.getOrderHeader(), null, null, shipEngine.getShipHeader(), request);
    	calcTotal(form, shipHeader.getShipHeaderId(), shipEngine);
    	form.setNewShip(false);
		form.setAllowCapture(false);
		form.setAllowVoid(false);
 
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
    }
    
    public ActionForward comment(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	ShipMaintActionForm form = (ShipMaintActionForm) actionForm;
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
		ShipHeader shipHeader = (ShipHeader) em.find(ShipHeader.class, Format.getLong(form.getShipHeaderId()));
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
    		orderTracking.setShipHeader(shipHeader);
    		em.persist(orderTracking);
    		
    		shipHeader.getShipTrackings().add(orderTracking);
    		
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
    	ShipEngine shipEngine = new ShipEngine(shipHeader, null);
    	initOrder(form, orderHeader, null, null, shipHeader, request);
    	calcTotal(form, shipHeader.getShipHeaderId(), shipEngine);
		if (shipEngine.isOpen(shipHeader)) {
			form.setEditable(true);
		}
    	form.setNewShip(false);
		form.setAllowCapture(false);
		if (shipHeader.getShipStatus().equals(Constants.ORDERSTATUS_OPEN)) {
			form.setAllowCapture(true);
		}
		form.setAllowVoid(false);
		if (shipHeader.getShipStatus().equals(Constants.ORDERSTATUS_COMPLETED)) {
			form.setAllowVoid(true);
		}
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}
    
    public ActionForward updateInternal(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Throwable {
    	ShipMaintActionForm form = (ShipMaintActionForm) actionForm;
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		AdminBean adminBean = getAdminBean(request);
		User user = adminBean.getUser();
    	OrderHeader orderHeader = (OrderHeader) em.find(OrderHeader.class, Format.getLong(form.getOrderHeaderId()));
		ShipHeader shipHeader = (ShipHeader) em.find(ShipHeader.class, Format.getLong(form.getShipHeaderId()));
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
    	ShipEngine shipEngine = new ShipEngine(shipHeader, null);
    	initOrder(form, orderHeader, null, null, shipHeader, request);
    	calcTotal(form, shipHeader.getShipHeaderId(), shipEngine);
		if (shipEngine.isOpen(shipHeader)) {
			form.setEditable(true);
		}
    	form.setNewShip(false);
		form.setAllowCapture(false);
		if (shipHeader.getShipStatus().equals(Constants.ORDERSTATUS_OPEN)) {
			form.setAllowCapture(true);
		}
		form.setAllowVoid(false);
		if (shipHeader.getShipStatus().equals(Constants.ORDERSTATUS_COMPLETED)) {
			form.setAllowVoid(true);
		}
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}
    
    private void getMissingFormInformation(ShipMaintActionForm form, String siteId) throws NumberFormatException, SecurityException, Exception {
    	for (ShipDetailDisplayForm detailForm : form.getShipDetails()) {
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
