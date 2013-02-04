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

package com.jada.order.document;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.OrderItemDetail;
import com.jada.jpa.entity.PaymentTran;
import com.jada.jpa.entity.ShipDetail;
import com.jada.jpa.entity.ShipHeader;
import com.jada.jpa.entity.User;
import com.jada.inventory.InventoryEngine;
import com.jada.util.Constants;

public class ShipEngine extends OrderEngineBase {
	OrderHeader orderHeader = null;
	ShipHeader shipHeader = null;
	ShipHeader masterHeader = null;
	ShipDetail lastShipDetail = null;
	boolean updateInventory;
	User user = null;
	boolean shipShipping = false;
	
	public ShipEngine(OrderHeader orderHeader, User user) {
		this.orderHeader = orderHeader;
		this.user = user;
		shipHeader = new ShipHeader();
		shipHeader.setOrderHeader(orderHeader);
		shipHeader.setShipStatus(Constants.ORDERSTATUS_OPEN);
		shipHeader.setShipDate(new Date());
		shipHeader.setRecUpdateBy(user.getUserId());
		shipHeader.setRecUpdateDatetime(new Date());
		shipHeader.setRecCreateBy(user.getUserId());
		shipHeader.setRecCreateDatetime(new Date());
	}
	
	public ShipEngine(ShipHeader header, User user) throws IllegalAccessException, InvocationTargetException {
		this.shipHeader = clone(header);
		this.user = user;
		this.orderHeader = shipHeader.getOrderHeader();
		this.masterHeader = header;
	}
	
	static public ShipHeader clone(ShipHeader header) throws IllegalAccessException, InvocationTargetException {
		ShipHeader shipHeader = new ShipHeader();
		shipHeader.setShipHeaderId(header.getShipHeaderId());
		shipHeader.setShipNum(header.getShipNum());
		shipHeader.setShipStatus(header.getShipStatus());
		shipHeader.setShipDate(header.getShipDate());
		shipHeader.setUpdateInventory(header.getUpdateInventory());
		shipHeader.setRecUpdateBy(header.getRecUpdateBy());
		shipHeader.setRecUpdateDatetime(header.getRecUpdateDatetime());
		shipHeader.setRecCreateBy(header.getRecCreateBy());
		shipHeader.setRecCreateDatetime(header.getRecCreateDatetime());
		shipHeader.setOrderHeader(header.getOrderHeader());
		
		Iterator<?> iterator = header.getShipDetails().iterator();
		while (iterator.hasNext()) {
			ShipDetail detail = (ShipDetail) iterator.next();
			ShipDetail shipDetail = new ShipDetail();
			shipDetail.setShipHeader(shipHeader);
			shipHeader.getShipDetails().add(shipDetail);
			shipDetail.setShipDetailId(detail.getShipDetailId());
			shipDetail.setSeqNum(detail.getSeqNum());
			shipDetail.setItemShipQty(detail.getItemShipQty());
			shipDetail.setRecUpdateBy(header.getRecUpdateBy());
			shipDetail.setRecUpdateDatetime(header.getRecUpdateDatetime());
			shipDetail.setRecCreateBy(header.getRecCreateBy());
			shipDetail.setRecCreateDatetime(header.getRecCreateDatetime());
			shipDetail.setOrderItemDetail(detail.getOrderItemDetail());
		}		
		return shipHeader;
	}
	
	public void shipAll() throws Exception {
		Iterator<?> iterator = orderHeader.getOrderItemDetails().iterator();
		while (iterator.hasNext()) {
			OrderItemDetail orderItemDetail = (OrderItemDetail) iterator.next();
			ItemBalance itemBalance = getItemBalance(orderHeader, orderItemDetail.getItemSkuCd(), null, null, shipHeader);
			int shipQty = itemBalance.getOrderQty() - itemBalance.getShipQty();
			setQty(orderItemDetail.getItemSkuCd(), shipQty);
		}
		calculateHeader();
	}
	
	public void setQty(String itemSkuCd, int qty) throws Exception {
		ShipDetail shipDetail = null;
		boolean found = false;
		Iterator<?> iterator = shipHeader.getShipDetails().iterator();
		while (iterator.hasNext()) {
			shipDetail = (ShipDetail) iterator.next();
			if (shipDetail.getOrderItemDetail().getItemSkuCd().equals(itemSkuCd)) {
				found = true;
				break;
			}
		}
		if (!found) {
			shipDetail = new ShipDetail();
			shipDetail.setRecCreateBy(user.getUserId());
			shipDetail.setRecCreateDatetime(new Date());
			iterator = orderHeader.getOrderItemDetails().iterator();
			found = false;
			while (iterator.hasNext()) {
				OrderItemDetail orderItemDetail = (OrderItemDetail) iterator.next();
				if (orderItemDetail.getItemSkuCd().equals(itemSkuCd)) {
					shipDetail.setOrderItemDetail(orderItemDetail);
//					orderItemDetail.getShipDetails().add(shipDetail);
					found = true;
					break;
				}
			}
			if (!found) {
				throw new OrderItemNotFoundException("");
			}
			shipDetail.setShipHeader(shipHeader);
			
			iterator = shipHeader.getShipDetails().iterator();
			int seqNum = 0;
			while (iterator.hasNext()) {
				ShipDetail s = (ShipDetail) iterator.next();
				if (s.getSeqNum() > seqNum) {
					seqNum = s.getSeqNum();
				}
			}
			shipDetail.setSeqNum(seqNum);
			shipHeader.getShipDetails().add(shipDetail);
		}
		
		ItemBalance itemBalance = getItemBalance(orderHeader, itemSkuCd, null, null, shipHeader);
		int balanceQty = itemBalance.getOrderQty() - itemBalance.getShipQty();
		
		if (balanceQty < qty) {
			throw new OrderQuantityException("");
		}
		shipDetail.setItemShipQty(qty);
		shipDetail.setRecUpdateBy(user.getUserId());
		shipDetail.setRecUpdateDatetime(new Date());
		shipHeader.setRecUpdateBy(user.getUserId());
		shipHeader.setRecUpdateDatetime(new Date());
		
		lastShipDetail = shipDetail;
	}
	
	public void calculateHeader() throws Exception {
		shipHeader.setRecUpdateBy(user.getUserId());
		shipHeader.setRecUpdateDatetime(new Date());
	}
	
	public void shipOrder(HttpServletRequest request) throws Exception {
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Iterator<?> iterator = shipHeader.getShipDetails().iterator();
		while (iterator.hasNext()) {
			ShipDetail shipDetail = (ShipDetail) iterator.next();
			int shipQty = shipDetail.getItemShipQty();
			
			Item item = shipDetail.getOrderItemDetail().getItem();
			if (item != null) {
				item = (Item) em.find(Item.class, item.getItemId());
				em.lock(item, LockModeType.WRITE);
			
				InventoryEngine engine = new InventoryEngine(item);
				engine.adjustQty(shipQty * -1);
				engine.adjustBookedQty(shipQty * -1);
			}
		}
		shipHeader.setShipStatus(Constants.ORDERSTATUS_COMPLETED);
		shipHeader.setRecUpdateBy(user.getUserId());
		shipHeader.setRecUpdateDatetime(new Date());
	}
	
	public void voidShip() throws Exception {
		Iterator<?> iterator = shipHeader.getShipDetails().iterator();
		while (iterator.hasNext()) {
			ShipDetail shipDetail = (ShipDetail) iterator.next();
			int shipQty = shipDetail.getItemShipQty();
			InventoryEngine engine = new InventoryEngine(shipDetail.getOrderItemDetail().getItem());
			engine.adjustQty(shipQty);
			engine.adjustBookedQty(shipQty);
		}
		shipHeader.setShipStatus(Constants.ORDERSTATUS_VOIDED);
		shipHeader.setRecUpdateBy(user.getUserId());
		shipHeader.setRecUpdateDatetime(new Date());
	}
	
	public void cancelOrder() throws OrderStateException {
		if (!isOpen(shipHeader)) {
			throw new OrderStateException("Order cannot be cancelled");
		}
		shipHeader.setShipStatus(Constants.ORDERSTATUS_CANCELLED);
		shipHeader.setRecUpdateBy(user.getUserId());
		shipHeader.setRecUpdateDatetime(new Date());
	}
	
	public void saveHeader() throws Exception {
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String userId = user == null ? Constants.USERNAME_SYSTEM : user.getUserId();
		Date current = new Date();
		if (shipHeader.getShipNum() == null) {
			shipHeader.setShipNum(generateNumber(orderHeader.getSiteDomain().getSite(), Constants.SEQUENCE_INVOICE));
			shipHeader.setShipDate(new Date());
			shipHeader.setRecCreateBy(userId);
			shipHeader.setRecCreateDatetime(current);
		}
		shipHeader.setRecUpdateBy(userId);
		shipHeader.setRecUpdateDatetime(current);
		if (shipHeader.getShipHeaderId() == null) {
			em.persist(shipHeader);
		}
	}
	
	public void saveOrder() throws Exception {
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		if (masterHeader == null || !masterHeader.getRecUpdateDatetime().equals(shipHeader.getRecUpdateDatetime())) {
			if (shipHeader.getShipNum() == null) {
				shipHeader.setShipNum(generateNumber(orderHeader.getSiteDomain().getSite(), Constants.SEQUENCE_SHIP));
			}
			if (masterHeader == null) {
				masterHeader = new ShipHeader();
			}
			masterHeader.setShipNum(shipHeader.getShipNum());
			masterHeader.setShipStatus(shipHeader.getShipStatus());
			masterHeader.setShipDate(shipHeader.getShipDate());
			masterHeader.setUpdateInventory(shipHeader.getUpdateInventory());
			masterHeader.setRecUpdateBy(shipHeader.getRecUpdateBy());
			masterHeader.setRecUpdateDatetime(shipHeader.getRecUpdateDatetime());
			masterHeader.setRecCreateBy(shipHeader.getRecCreateBy());
			masterHeader.setRecCreateDatetime(shipHeader.getRecCreateDatetime());
			masterHeader.setOrderHeader(orderHeader);
			orderHeader.getShipHeaders().add(masterHeader);

			// Copy data from shipHeader to masterHeader
			if (masterHeader.getShipHeaderId() == null) {
				em.persist(masterHeader);
			}
			shipHeader.setShipHeaderId(masterHeader.getShipHeaderId());
		}
		
		Iterator<?> iterator = masterHeader.getShipDetails().iterator();
		while (iterator.hasNext()) {
			ShipDetail masterDetail = (ShipDetail) iterator.next();
			em.remove(masterDetail);
			iterator.remove();
		}
		
		iterator = shipHeader.getShipDetails().iterator();
		while (iterator.hasNext()) {
			ShipDetail shipDetail = (ShipDetail) iterator.next();
			if (shipDetail.getItemShipQty() <= 0) {
				continue;
			}
			ShipDetail masterDetail = new ShipDetail();
			masterDetail.setShipHeader(masterHeader);
			masterDetail.setSeqNum(shipDetail.getSeqNum());
			masterDetail.setItemShipQty(shipDetail.getItemShipQty());
			masterDetail.setRecUpdateBy(shipDetail.getRecUpdateBy());
			masterDetail.setRecUpdateDatetime(shipDetail.getRecUpdateDatetime());
			masterDetail.setRecCreateBy(shipDetail.getRecCreateBy());
			masterDetail.setRecCreateDatetime(shipDetail.getRecCreateDatetime());
			masterDetail.setOrderItemDetail(shipDetail.getOrderItemDetail());
			em.persist(masterDetail);
			masterHeader.getShipDetails().add(masterDetail);
		}
	}
	
	public boolean isSavePaymentTran(PaymentTran paymentTran, PaymentTran masterPaymentTran) {
		if (paymentTran == null) {
			return false;
		}
		if (masterPaymentTran == null) {
			return true;
		}
		if (!paymentTran.getRecUpdateDatetime().equals(masterPaymentTran.getRecUpdateDatetime())) {
			return true;
		}
		return false;
	}
	
	public ShipDetail getMasterDetail(ShipDetail shipDetail) {
		if (masterHeader == null) {
			return null;
		}
		if (shipDetail.getShipDetailId() == null) {
			return null;
		}
		Iterator<?> iterator = masterHeader.getShipDetails().iterator();
		while (iterator.hasNext()) {
			ShipDetail detail = (ShipDetail) iterator.next();
			if (shipDetail.getShipDetailId().equals(detail.getShipDetailId())) {
				return shipDetail;
			}
		}
		return null;
	}
	
	public boolean isModified(ShipDetail shipDetail) {
		ShipDetail masterDetail = getMasterDetail(shipDetail);
		if (masterDetail == null) {
			return true;
		}
		if (shipDetail.getRecUpdateDatetime().equals(masterDetail.getRecUpdateDatetime())) {
			return false;
		}
		return true;
	}

	public ShipHeader getShipHeader() {
		return shipHeader;
	}

	public void setShipHeader(ShipHeader shipHeader) {
		this.shipHeader = shipHeader;
	}

	public ShipDetail getLastShipDetail() {
		return lastShipDetail;
	}

	public void setLastShipDetail(ShipDetail lastShipDetail) {
		this.lastShipDetail = lastShipDetail;
	}

	public OrderHeader getOrderHeader() {
		return orderHeader;
	}

	public void setOrderHeader(OrderHeader orderHeader) {
		this.orderHeader = orderHeader;
	}

	public boolean isUpdateInventory() {
		return updateInventory;
	}

	public void setUpdateInventory(boolean updateInventory) {
		this.updateInventory = updateInventory;
		shipHeader.setUpdateInventory(String.valueOf(Constants.VALUE_NO));
		if (updateInventory) {
			shipHeader.setUpdateInventory(String.valueOf(Constants.VALUE_YES));
		}
	}
	
}
