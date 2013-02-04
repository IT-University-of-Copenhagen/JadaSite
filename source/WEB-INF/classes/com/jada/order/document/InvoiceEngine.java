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
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import javax.persistence.EntityManager;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.InvoiceDetail;
import com.jada.jpa.entity.InvoiceDetailTax;
import com.jada.jpa.entity.InvoiceHeader;
import com.jada.jpa.entity.OrderDetailTax;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.OrderItemDetail;
import com.jada.jpa.entity.PaymentTran;
import com.jada.jpa.entity.User;
import com.jada.order.payment.PaymentEngine;
import com.jada.order.payment.PaymentException;
import com.jada.order.payment.PaymentManager;
import com.jada.util.Constants;
import com.jada.util.Utility;

public class InvoiceEngine extends OrderEngineBase {
	OrderHeader orderHeader = null;
	InvoiceHeader invoiceHeader = null;
	InvoiceHeader masterHeader = null;
	User user = null;
	String userId = null;
	boolean isNew = false;
	boolean invoiceShipping = false;
	InvoiceDetail lastInvoiceDetail = null;
	
	public InvoiceEngine(OrderHeader orderHeader, User user) throws Exception {
		isNew = true;
		this.user = user;
		this.orderHeader = orderHeader;
		userId = user == null ? Constants.USERNAME_SYSTEM : user.getUserId();
		invoiceHeader = new InvoiceHeader();
		invoiceHeader.setOrderHeader(orderHeader);
		invoiceHeader.setInvoiceDate(new Date());
		invoiceHeader.setInvoiceStatus(Constants.ORDERSTATUS_OPEN);
		invoiceHeader.setInvoiceTotal((float) 0);
		invoiceHeader.setRecUpdateBy(userId);
		invoiceHeader.setRecUpdateDatetime(new Date());
		invoiceHeader.setRecCreateBy(userId);
		invoiceHeader.setRecCreateDatetime(new Date());
//		orderHeader.getInvoiceHeaders().add(invoiceHeader);
	}
	
	public InvoiceEngine(InvoiceHeader header, User user) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		this.invoiceHeader = clone(header);
		this.user = user;
		this.orderHeader = invoiceHeader.getOrderHeader();
		this.masterHeader = header;
		userId = user == null ? Constants.USERNAME_SYSTEM : user.getUserId();
	}
	
	public void invoiceAll() throws Exception {
		float shippingTotal = orderHeader.getShippingTotal() - orderHeader.getShippingDiscountTotal();
		Iterator<?> iterator = orderHeader.getInvoiceHeaders().iterator();
		while (iterator.hasNext()) {
			InvoiceHeader invoiceHeader = (InvoiceHeader) iterator.next();
			if (invoiceHeader.getInvoiceHeaderId() == null) {
				continue;
			}
			if (invoiceHeader.getInvoiceStatus().equals(Constants.ORDERSTATUS_CANCELLED) || invoiceHeader.getInvoiceStatus().equals(Constants.ORDERSTATUS_VOIDED)) {
				continue;
			}
			shippingTotal -= invoiceHeader.getShippingTotal();
		}
		this.setShippingTotal(shippingTotal);
		
		iterator = orderHeader.getOrderItemDetails().iterator();
		while (iterator.hasNext()) {
			OrderItemDetail orderItemDetail = (OrderItemDetail) iterator.next();
			ItemBalance itemBalance = getItemBalance(orderHeader, orderItemDetail.getItemSkuCd(), invoiceHeader, null, null);
			int invoiceQty = itemBalance.getOrderQty() + itemBalance.getCreditQty() - itemBalance.getInvoiceQty();
			setQty(orderItemDetail.getItemSkuCd(), invoiceQty);
		}
		calculateHeader();
	}
	
	public void setQty(String itemSkuCd, int qty) throws Exception {
		InvoiceDetail invoiceDetail = null;
		boolean found = false;
		Iterator<?> iterator = invoiceHeader.getInvoiceDetails().iterator();
		while (iterator.hasNext()) {
			invoiceDetail = (InvoiceDetail) iterator.next();
			if (invoiceDetail.getOrderItemDetail().getItemSkuCd().equals(itemSkuCd)) {
				found = true;
				break;
			}
		}
		if (!found) {
			invoiceDetail = new InvoiceDetail();
			invoiceDetail.setRecCreateBy(userId);
			invoiceDetail.setRecCreateDatetime(new Date());
			iterator = orderHeader.getOrderItemDetails().iterator();
			found = false;
			while (iterator.hasNext()) {
				OrderItemDetail orderItemDetail = (OrderItemDetail) iterator.next();
				if (orderItemDetail.getItemSkuCd().equals(itemSkuCd)) {
					invoiceDetail.setOrderItemDetail(orderItemDetail);
//					orderItemDetail.getInvoiceDetails().add(invoiceDetail);
					found = true;
					break;
				}
			}
			if (!found) {
				throw new OrderItemNotFoundException("");
			}
			invoiceDetail.setInvoiceHeader(invoiceHeader);
			
			iterator = invoiceHeader.getInvoiceDetails().iterator();
			int seqNum = 0;
			while (iterator.hasNext()) {
				InvoiceDetail i = (InvoiceDetail) iterator.next();
				if (i.getSeqNum() > seqNum) {
					seqNum = i.getSeqNum();
				}
			}
			invoiceDetail.setSeqNum(seqNum);
			invoiceHeader.getInvoiceDetails().add(invoiceDetail);
		}
		
		ItemBalance itemBalance = getItemBalance(orderHeader, itemSkuCd, invoiceHeader, null, null);
		int balanceQty = itemBalance.getOrderQty() + itemBalance.getCreditQty() - itemBalance.getInvoiceQty();
		float balanceAmount = itemBalance.getOrderAmount() + itemBalance.getCreditAmount() - itemBalance.getInvoiceAmount();
		
		if (balanceQty < qty) {
			throw new OrderQuantityException("");
		}
		invoiceDetail.setItemInvoiceQty(qty);
		if (balanceQty == qty) {
			invoiceDetail.setItemInvoiceAmount(Utility.round(balanceAmount, 2));
		}
		else {
			float itemInvoiceAmount = Utility.round(balanceAmount * qty / balanceQty, 2);
			invoiceDetail.setItemInvoiceAmount(itemInvoiceAmount);
		}
		
		invoiceDetail.getInvoiceDetailTaxes().clear();
		ItemTaxBalance itemTaxBalances[] = itemBalance.getItemTaxBalances();
		for (int i = 0; i < itemTaxBalances.length; i++) {
			ItemTaxBalance itemTaxBalance = itemTaxBalances[i];
			float taxBalanceAmount = itemTaxBalance.getOrderTaxAmount() - itemTaxBalance.getInvoiceTaxAmount();
			if (taxBalanceAmount <= 0) {
				continue;
			}
			
			float taxAmount = taxBalanceAmount;
			if (balanceQty != qty) {
				taxAmount = Utility.round(taxBalanceAmount * qty / balanceQty, 2);
			}
			if (taxAmount > 0) {
				InvoiceDetailTax invoiceDetailTax = new InvoiceDetailTax();
				invoiceDetailTax.setInvoiceHeader(invoiceHeader);
				invoiceDetailTax.setInvoiceDetail(invoiceDetail);
				invoiceDetail.getInvoiceDetailTaxes().add(invoiceDetailTax);
				invoiceDetailTax.setTax(itemTaxBalance.getTax());
				invoiceDetailTax.setTaxName(itemTaxBalance.getTaxName());
				invoiceDetailTax.setTaxAmount(taxAmount);
				invoiceDetailTax.setRecUpdateBy(userId);
				invoiceDetailTax.setRecUpdateDatetime(new Date());
				invoiceDetailTax.setRecCreateBy(userId);
				invoiceDetailTax.setRecCreateDatetime(new Date());
				invoiceHeader.getInvoiceTaxes().add(invoiceDetailTax);
			}
		}
		invoiceDetail.setRecUpdateBy(userId);
		invoiceDetail.setRecUpdateDatetime(new Date());
		invoiceHeader.setRecUpdateBy(userId);
		invoiceHeader.setRecUpdateDatetime(new Date());
		setDirty(itemSkuCd);
		
		lastInvoiceDetail = invoiceDetail;
	}
	
	public void calculateHeader() throws Exception {
		float invoiceTotal = 0;
		Iterator<?> iterator = invoiceHeader.getInvoiceDetails().iterator();
		while (iterator.hasNext()) {
			InvoiceDetail invoiceDetail = (InvoiceDetail) iterator.next();
			invoiceTotal += invoiceDetail.getItemInvoiceAmount().floatValue();
			Iterator<?> taxIterator = invoiceDetail.getInvoiceDetailTaxes().iterator();
			while (taxIterator.hasNext()) {
				InvoiceDetailTax invoiceDetailTax = (InvoiceDetailTax) taxIterator.next();
				invoiceTotal += invoiceDetailTax.getTaxAmount();
			}
		}	
		invoiceTotal += invoiceHeader.getShippingTotal();
		iterator = invoiceHeader.getInvoiceTaxes().iterator();
		while (iterator.hasNext()) {
			InvoiceDetailTax invoiceDetailTax = (InvoiceDetailTax) iterator.next();
			if (invoiceDetailTax.getInvoiceDetail() != null) {
				continue;
			}
			invoiceTotal += invoiceDetailTax.getTaxAmount();
		}
		
		invoiceHeader.setInvoiceTotal(Float.valueOf(invoiceTotal));
		invoiceHeader.setRecUpdateBy(userId);
		invoiceHeader.setRecUpdateDatetime(new Date());
	}
	
	/*
	 * Used only when payment has not been authorized.  To be used with shopping cart.
	 */
	public void payOrder(PaymentEngine paymentEngine, HttpServletRequest request) throws Exception {
		if (isVoided(orderHeader)) {
			throw new PaymentException("Order is already voided");
		}
		if (paymentEngine != null) {
			paymentEngine.setCreditCardInfo(creditCardInfo);
			paymentEngine.authorizeAndCapturePayment(invoiceHeader, request);
	
			PaymentTran paymentTran = new PaymentTran();
			paymentTran.setAuthCode(paymentEngine.getAuthCode());
			paymentTran.setPaymentReference1(paymentEngine.getPaymentReference1());
			paymentTran.setPaymentReference2(paymentEngine.getPaymentReference2());
			paymentTran.setPaymentReference3(paymentEngine.getPaymentReference3());
			paymentTran.setPaymentReference4(paymentEngine.getPaymentReference4());
			paymentTran.setPaymentReference5(paymentEngine.getPaymentReference5());
			paymentTran.setTranDatetime(new Date());
			paymentTran.setRecUpdateBy(userId);
			paymentTran.setRecUpdateDatetime(new Date());
			paymentTran.setRecCreateBy(userId);
			paymentTran.setRecCreateDatetime(new Date());
			invoiceHeader.setPaymentTran(paymentTran);
		}
		invoiceHeader.setInvoiceStatus(Constants.ORDERSTATUS_COMPLETED);
		invoiceHeader.setRecUpdateBy(userId);
		invoiceHeader.setRecUpdateDatetime(new Date());
	}
	
	/*
	 * To be used during back order processing.
	 */
	public void payOrder(HttpServletRequest request) throws Exception {
		PaymentEngine paymentEngine = PaymentManager.getPaymentEngine(orderHeader.getPaymentGatewayProvider(), orderHeader.getSiteCurrency());
		if (isVoided(orderHeader)) {
			throw new OrderStateException("Order is already voided");
		}
		if (paymentEngine != null) {
			paymentEngine.capturePayment(invoiceHeader);
			
			PaymentTran paymentTran = new PaymentTran();
			paymentTran.setAuthCode(paymentEngine.getAuthCode());
			paymentTran.setPaymentReference1(paymentEngine.getPaymentReference1());
			paymentTran.setPaymentReference2(paymentEngine.getPaymentReference2());
			paymentTran.setPaymentReference3(paymentEngine.getPaymentReference3());
			paymentTran.setPaymentReference4(paymentEngine.getPaymentReference4());
			paymentTran.setPaymentReference5(paymentEngine.getPaymentReference5());
			paymentTran.setTranDatetime(new Date());
			paymentTran.setRecUpdateBy(userId);
			paymentTran.setRecUpdateDatetime(new Date());
			paymentTran.setRecCreateBy(userId);
			paymentTran.setRecCreateDatetime(new Date());
			invoiceHeader.setPaymentTran(paymentTran);
		}
		invoiceHeader.setInvoiceStatus(Constants.ORDERSTATUS_COMPLETED);
		invoiceHeader.setRecUpdateBy(userId);
		invoiceHeader.setRecUpdateDatetime(new Date());
	}
	
	public void cancelOrder() throws OrderStateException {
		if (!isOpen(invoiceHeader)) {
			throw new OrderStateException("Order cannot be cancelled");
		}
		invoiceHeader.setInvoiceStatus(Constants.ORDERSTATUS_CANCELLED);
		invoiceHeader.setRecUpdateBy(userId);
		invoiceHeader.setRecUpdateDatetime(new Date());
	}
	
	public void voidOrder() throws Exception {
		if (!isCompleted(invoiceHeader) && !isClosed(invoiceHeader)) {
			throw new OrderStateException("Order has not been completed");
		}
		String paymentGateway = orderHeader.getPaymentGatewayProvider();
		if (paymentGateway != null) {
			PaymentEngine paymentEngine = PaymentManager.getPaymentEngine(paymentGateway, orderHeader.getSiteCurrency());
			paymentEngine.voidPayment(invoiceHeader);
			PaymentTran paymentTran = new PaymentTran();
			paymentTran.setAuthCode(paymentEngine.getAuthCode());
			paymentTran.setPaymentReference1(paymentEngine.getPaymentReference1());
			paymentTran.setPaymentReference2(paymentEngine.getPaymentReference2());
			paymentTran.setPaymentReference3(paymentEngine.getPaymentReference3());
			paymentTran.setPaymentReference4(paymentEngine.getPaymentReference4());
			paymentTran.setPaymentReference5(paymentEngine.getPaymentReference5());
			paymentTran.setTranDatetime(new Date());
			paymentTran.setRecUpdateBy(userId);
			paymentTran.setRecUpdateDatetime(new Date());
			paymentTran.setRecCreateBy(userId);
			paymentTran.setRecCreateDatetime(new Date());
			invoiceHeader.setVoidPaymentTran(paymentTran);
		}
		invoiceHeader.setInvoiceStatus(Constants.ORDERSTATUS_VOIDED);
		invoiceHeader.setRecUpdateBy(userId);
		invoiceHeader.setRecUpdateDatetime(new Date());
	}
	
	public void setShippingTotal(float shippingTotal) throws Exception {
		float shippingBalance = orderHeader.getShippingTotal() - orderHeader.getShippingDiscountTotal();
		Iterator<?> iterator = orderHeader.getInvoiceHeaders().iterator();
		while (iterator.hasNext()) {
			InvoiceHeader iHeader = (InvoiceHeader) iterator.next();
			if (iHeader.getInvoiceStatus().equals(Constants.ORDERSTATUS_VOIDED)) {
				continue;
			}
			if (isNew) {
				if (iHeader.getInvoiceHeaderId() == null) {
					continue;
				}
			}
			else {
				if (iHeader.getInvoiceHeaderId().equals(invoiceHeader.getInvoiceHeaderId())) {
					continue;
				}
			}
			shippingBalance -= iHeader.getShippingTotal();
		}
		float ratio = 1;
		if (shippingTotal == 0) {
			ratio = 0;
		}
		if (shippingBalance != shippingTotal) {
			ratio = shippingTotal / orderHeader.getShippingTotal();
		}
		
		invoiceHeader.setShippingTotal(shippingTotal);
		
		invoiceHeader.getInvoiceTaxes().clear();
		Iterator<?> shippingIterator = orderHeader.getOrderTaxes().iterator();
		while (shippingIterator.hasNext()) {
			OrderDetailTax orderDetailTax = (OrderDetailTax) shippingIterator.next();
			if (orderDetailTax.getOrderItemDetail() != null) {
				continue;
			}
			InvoiceDetailTax invoiceDetailTax = new InvoiceDetailTax();
			invoiceDetailTax.setInvoiceHeader(invoiceHeader);
			invoiceDetailTax.setTax(orderDetailTax.getTax());
			invoiceDetailTax.setTaxName(orderDetailTax.getTaxName());
			float taxAmount = orderDetailTax.getTaxAmount() * ratio;
			if (taxAmount <= 0) {
				continue;
			}
			invoiceDetailTax.setTaxAmount(taxAmount);
			invoiceDetailTax.setRecUpdateBy(userId);
			invoiceDetailTax.setRecUpdateDatetime(new Date());
			invoiceDetailTax.setRecCreateBy(userId);
			invoiceDetailTax.setRecCreateDatetime(new Date());
			invoiceHeader.getInvoiceTaxes().add(invoiceDetailTax);
		}
		invoiceHeader.setRecUpdateBy(userId);
		invoiceHeader.setRecUpdateDatetime(new Date());
	}
	
	public Vector<?> getInvoiceTaxes() {
		Vector<InvoiceDetailTax> invoiceDetailTaxes = new Vector<InvoiceDetailTax>();
		Iterator<?> iterator = invoiceHeader.getInvoiceTaxes().iterator();
		while (iterator.hasNext()) {
			InvoiceDetailTax invoiceDetailTax = (InvoiceDetailTax) iterator.next();
			boolean found = false;
			Iterator<?> sumIterator = invoiceDetailTaxes.iterator();
			InvoiceDetailTax sumTax = null;
			while (sumIterator.hasNext()) {
				sumTax = (InvoiceDetailTax) sumIterator.next();
				if (sumTax.getTaxName().equals(invoiceDetailTax.getTaxName())) {
					found = true;
					break;
				}	
			}
			if (!found) {
				sumTax = new InvoiceDetailTax();
				sumTax.setTaxName(invoiceDetailTax.getTaxName());
				sumTax.setTaxAmount((float) 0);
				sumTax.setTax(invoiceDetailTax.getTax());
				invoiceDetailTaxes.add(sumTax);
			}
			float taxAmount = sumTax.getTaxAmount();
			taxAmount += invoiceDetailTax.getTaxAmount();
			sumTax.setTaxAmount(taxAmount);
		}
		return invoiceDetailTaxes;
	}
	
	public boolean isInvoiceShipping() {
		return invoiceShipping;
	}

	public void setInvoiceShipping(boolean invoiceShipping) {
		this.invoiceShipping = invoiceShipping;
	}

	public InvoiceHeader getInvoiceHeader() {
		return invoiceHeader;
	}

	public void setInvoiceHeader(InvoiceHeader invoiceHeader) {
		this.invoiceHeader = invoiceHeader;
	}

	public OrderHeader getOrderHeader() {
		return orderHeader;
	}

	public void setOrderHeader(OrderHeader orderHeader) {
		this.orderHeader = orderHeader;
	}

	public InvoiceDetail getLastInvoiceDetail() {
		return lastInvoiceDetail;
	}

	public void setLastInvoiceDetail(InvoiceDetail lastInvoiceDetail) {
		this.lastInvoiceDetail = lastInvoiceDetail;
	}
	
	static public InvoiceHeader clone(InvoiceHeader header) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		InvoiceHeader invoiceHeader = new InvoiceHeader();
		invoiceHeader.setInvoiceHeaderId(header.getInvoiceHeaderId());
		invoiceHeader.setInvoiceNum(header.getInvoiceNum());
		invoiceHeader.setShippingTotal(header.getShippingTotal());
		invoiceHeader.setInvoiceTotal(header.getInvoiceTotal());
		invoiceHeader.setInvoiceStatus(header.getInvoiceStatus());
		invoiceHeader.setInvoiceDate(header.getInvoiceDate());
		invoiceHeader.setRecUpdateBy(header.getRecUpdateBy());
		invoiceHeader.setRecUpdateDatetime(header.getRecUpdateDatetime());
		invoiceHeader.setRecCreateBy(header.getRecCreateBy());
		invoiceHeader.setRecCreateDatetime(header.getRecCreateDatetime());
		invoiceHeader.setOrderHeader(header.getOrderHeader());
		
		if (header.getPaymentTran() != null) {
			PaymentTran pt = header.getPaymentTran();
			PaymentTran paymentTran = new PaymentTran();
			PropertyUtils.copyProperties(paymentTran, pt);
			invoiceHeader.setPaymentTran(paymentTran);
		}
		if (header.getVoidPaymentTran() != null) {
			PaymentTran pt = header.getVoidPaymentTran();
			PaymentTran paymentTran = new PaymentTran();
			PropertyUtils.copyProperties(paymentTran, pt);
			invoiceHeader.setVoidPaymentTran(paymentTran);
		}
		
		Iterator<?> iterator = header.getInvoiceDetails().iterator();
		while (iterator.hasNext()) {
			InvoiceDetail detail = (InvoiceDetail) iterator.next();
			InvoiceDetail invoiceDetail = new InvoiceDetail();
			invoiceDetail.setInvoiceHeader(invoiceHeader);
			invoiceHeader.getInvoiceDetails().add(invoiceDetail);
			invoiceDetail.setInvoiceDetailId(detail.getInvoiceDetailId());
			invoiceDetail.setSeqNum(detail.getSeqNum());
			invoiceDetail.setItemInvoiceQty(detail.getItemInvoiceQty());
			invoiceDetail.setItemInvoiceAmount(detail.getItemInvoiceAmount());
			invoiceDetail.setRecUpdateBy(header.getRecUpdateBy());
			invoiceDetail.setRecUpdateDatetime(header.getRecUpdateDatetime());
			invoiceDetail.setRecCreateBy(header.getRecCreateBy());
			invoiceDetail.setRecCreateDatetime(header.getRecCreateDatetime());
			invoiceDetail.setOrderItemDetail(detail.getOrderItemDetail());
			
			Iterator<?> taxIterator = detail.getInvoiceDetailTaxes().iterator();
			while (taxIterator.hasNext()) {
				InvoiceDetailTax detailTax = (InvoiceDetailTax) taxIterator.next();
				InvoiceDetailTax invoiceDetailTax = new InvoiceDetailTax();
				invoiceDetailTax.setInvoiceDetailTaxId(detailTax.getInvoiceDetailTaxId());
				invoiceDetailTax.setTaxName(detailTax.getTaxName());
				invoiceDetailTax.setTaxAmount(detailTax.getTaxAmount());
				invoiceDetailTax.setRecUpdateBy(detailTax.getRecUpdateBy());
				invoiceDetailTax.setRecUpdateDatetime(detailTax.getRecUpdateDatetime());
				invoiceDetailTax.setRecCreateBy(detailTax.getRecCreateBy());
				invoiceDetailTax.setRecCreateDatetime(detailTax.getRecCreateDatetime());
				invoiceDetailTax.setTax(detailTax.getTax());
				invoiceDetailTax.setInvoiceDetail(invoiceDetail);
				invoiceDetail.getInvoiceDetailTaxes().add(invoiceDetailTax);
				invoiceDetailTax.setInvoiceHeader(invoiceHeader);
				invoiceHeader.getInvoiceTaxes().add(invoiceDetailTax);
			}
		}
		
		Iterator<?> taxIterator = header.getInvoiceTaxes().iterator();
		while (taxIterator.hasNext()) {
			InvoiceDetailTax detailTax = (InvoiceDetailTax) taxIterator.next();
			if (detailTax.getInvoiceDetail() != null) {
				continue;
			}
			InvoiceDetailTax invoiceDetailTax = new InvoiceDetailTax();
			invoiceDetailTax.setInvoiceDetailTaxId(detailTax.getInvoiceDetailTaxId());
			invoiceDetailTax.setTaxName(detailTax.getTaxName());
			invoiceDetailTax.setTaxAmount(detailTax.getTaxAmount());
			invoiceDetailTax.setRecUpdateBy(detailTax.getRecUpdateBy());
			invoiceDetailTax.setRecUpdateDatetime(detailTax.getRecUpdateDatetime());
			invoiceDetailTax.setRecCreateBy(detailTax.getRecCreateBy());
			invoiceDetailTax.setRecCreateDatetime(detailTax.getRecCreateDatetime());
			invoiceDetailTax.setTax(detailTax.getTax());
			invoiceDetailTax.setInvoiceHeader(invoiceHeader);
			invoiceHeader.getInvoiceTaxes().add(invoiceDetailTax);
		}
		return invoiceHeader;
	}
	
	public void saveHeader() throws Exception {
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Date current = new Date();
		if (isNew) {
			invoiceHeader.setInvoiceNum(generateNumber(orderHeader.getSiteDomain().getSite(), Constants.SEQUENCE_INVOICE));
			invoiceHeader.setInvoiceDate(new Date());
			invoiceHeader.setRecCreateBy(userId);
			invoiceHeader.setRecCreateDatetime(current);
		}
		invoiceHeader.setRecUpdateBy(userId);
		invoiceHeader.setRecUpdateDatetime(current);
		if (invoiceHeader.getInvoiceHeaderId() == null) {
			em.persist(invoiceHeader);
		}
		isNew = false;
	}
	
	public void saveOrder() throws Exception {
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		if (masterHeader == null || !masterHeader.getRecUpdateDatetime().equals(invoiceHeader.getRecUpdateDatetime())) {
			if (invoiceHeader.getInvoiceNum() == null) {
				invoiceHeader.setInvoiceNum(generateNumber(orderHeader.getSiteDomain().getSite(), Constants.SEQUENCE_INVOICE));
			}
			if (masterHeader == null) {
				masterHeader = new InvoiceHeader();
			}
			masterHeader.setInvoiceNum(invoiceHeader.getInvoiceNum());
			masterHeader.setShippingTotal(invoiceHeader.getShippingTotal());
			masterHeader.setInvoiceTotal(invoiceHeader.getInvoiceTotal());
			masterHeader.setInvoiceStatus(invoiceHeader.getInvoiceStatus());
			masterHeader.setInvoiceDate(invoiceHeader.getInvoiceDate());
			masterHeader.setRecUpdateBy(invoiceHeader.getRecUpdateBy());
			masterHeader.setRecUpdateDatetime(invoiceHeader.getRecUpdateDatetime());
			masterHeader.setRecCreateBy(invoiceHeader.getRecCreateBy());
			masterHeader.setRecCreateDatetime(invoiceHeader.getRecCreateDatetime());
			masterHeader.setOrderHeader(orderHeader);
			orderHeader.getInvoiceHeaders().add(masterHeader);

			// Copy data from invoiceHeader to masterHeader
			if (masterHeader.getInvoiceHeaderId() == null) {
				em.persist(masterHeader);
			}
			invoiceHeader.setInvoiceHeaderId(masterHeader.getInvoiceHeaderId());

			if (masterHeader != null) {
				Iterator<?> taxIterator = masterHeader.getInvoiceTaxes().iterator();
				while (taxIterator.hasNext()) {
					InvoiceDetailTax invoiceDetailTax = (InvoiceDetailTax) taxIterator.next();
					if (invoiceDetailTax.getInvoiceDetail() != null) {
						continue;
					}
					em.remove(invoiceDetailTax);
					taxIterator.remove();
				}
			}
			
			Iterator<?> taxIterator = invoiceHeader.getInvoiceTaxes().iterator();
			while (taxIterator.hasNext()) {
				InvoiceDetailTax invoiceDetailTax = (InvoiceDetailTax) taxIterator.next();
				if (invoiceDetailTax.getInvoiceDetail() != null) {
					continue;
				}
				InvoiceDetailTax masterTax = new InvoiceDetailTax();
//				masterTax.setInvoiceDetailTaxId(invoiceDetailTax.getInvoiceDetailTaxId());
				masterTax.setTaxName(invoiceDetailTax.getTaxName());
				masterTax.setTaxAmount(invoiceDetailTax.getTaxAmount());
				masterTax.setRecUpdateBy(invoiceDetailTax.getRecUpdateBy());
				masterTax.setRecUpdateDatetime(invoiceDetailTax.getRecUpdateDatetime());
				masterTax.setRecCreateBy(invoiceDetailTax.getRecCreateBy());
				masterTax.setRecCreateDatetime(invoiceDetailTax.getRecCreateDatetime());
				masterTax.setTax(invoiceDetailTax.getTax());
				masterTax.setInvoiceDetail(null);
				masterTax.setInvoiceHeader(masterHeader);
				em.persist(masterTax);
			}
		}
		
		Iterator<?> iterator = masterHeader.getInvoiceDetails().iterator();
		while (iterator.hasNext()) {
			InvoiceDetail masterDetail = (InvoiceDetail) iterator.next();
			Iterator<?> taxIterator = masterDetail.getInvoiceDetailTaxes().iterator();
			while (taxIterator.hasNext()) {
				InvoiceDetailTax masterDetailTax = (InvoiceDetailTax) taxIterator.next();
				em.remove(masterDetailTax);
				taxIterator.remove();
			}
			em.remove(masterDetail);
			iterator.remove();
		}
		
		iterator = invoiceHeader.getInvoiceDetails().iterator();
		while (iterator.hasNext()) {
			InvoiceDetail invoiceDetail = (InvoiceDetail) iterator.next();
			InvoiceDetail masterDetail = new InvoiceDetail();
			masterDetail.setInvoiceHeader(masterHeader);
			masterDetail.setSeqNum(invoiceDetail.getSeqNum());
			masterDetail.setItemInvoiceQty(invoiceDetail.getItemInvoiceQty());
			masterDetail.setItemInvoiceAmount(invoiceDetail.getItemInvoiceAmount());
			masterDetail.setRecUpdateBy(invoiceDetail.getRecUpdateBy());
			masterDetail.setRecUpdateDatetime(invoiceDetail.getRecUpdateDatetime());
			masterDetail.setRecCreateBy(invoiceDetail.getRecCreateBy());
			masterDetail.setRecCreateDatetime(invoiceDetail.getRecCreateDatetime());
			masterDetail.setOrderItemDetail(invoiceDetail.getOrderItemDetail());
			em.persist(masterDetail);
			masterHeader.getInvoiceDetails().add(masterDetail);
	
			Iterator<?> taxIterator = invoiceDetail.getInvoiceDetailTaxes().iterator();
			while (taxIterator.hasNext()) {
				InvoiceDetailTax invoiceDetailTax = (InvoiceDetailTax) taxIterator.next();
				InvoiceDetailTax masterTax = new InvoiceDetailTax();
				masterTax.setTaxName(invoiceDetailTax.getTaxName());
				masterTax.setTaxAmount(invoiceDetailTax.getTaxAmount());
				masterTax.setRecUpdateBy(invoiceDetailTax.getRecUpdateBy());
				masterTax.setRecUpdateDatetime(invoiceDetailTax.getRecUpdateDatetime());
				masterTax.setRecCreateBy(invoiceDetailTax.getRecCreateBy());
				masterTax.setRecCreateDatetime(invoiceDetailTax.getRecCreateDatetime());
				masterTax.setInvoiceDetail(masterDetail);
				masterTax.setTax(invoiceDetailTax.getTax());
				masterDetail.getInvoiceDetailTaxes().add(masterTax);
				masterTax.setInvoiceHeader(masterHeader);
				em.persist(masterTax);
			}
		}

		PaymentTran paymentTran = invoiceHeader.getPaymentTran();
		PaymentTran masterPaymentTran = null;
		if (masterHeader != null) {
			masterPaymentTran = masterHeader.getPaymentTran();
		}
		if (isSavePaymentTran(paymentTran, masterPaymentTran)) {
			if (masterPaymentTran == null) {
				masterPaymentTran = new PaymentTran();
			}
			PropertyUtils.copyProperties(masterPaymentTran, paymentTran);
			masterHeader.setPaymentTran(masterPaymentTran);
			if (masterPaymentTran.getPaymentTranId() == null) {
				em.persist(masterPaymentTran);
			}
		}
		
		PaymentTran voidPaymentTran = invoiceHeader.getVoidPaymentTran();
		masterPaymentTran = null;
		if (masterHeader != null) {
			masterPaymentTran = masterHeader.getVoidPaymentTran();
		}
		if (isSavePaymentTran(voidPaymentTran, masterPaymentTran)) {
			if (masterPaymentTran == null) {
				masterPaymentTran = new PaymentTran();
			}
			PropertyUtils.copyProperties(masterPaymentTran, voidPaymentTran);
			masterHeader.setVoidPaymentTran(masterPaymentTran);
			if (masterPaymentTran.getPaymentTranId() == null) {
				em.persist(masterPaymentTran);
			}
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
	
	public InvoiceDetail getMasterDetail(InvoiceDetail invoiceDetail) {
		if (masterHeader == null) {
			return null;
		}
		if (invoiceDetail.getInvoiceDetailId() == null) {
			return null;
		}
		Iterator<?> iterator = masterHeader.getInvoiceDetails().iterator();
		while (iterator.hasNext()) {
			InvoiceDetail detail = (InvoiceDetail) iterator.next();
			if (invoiceDetail.getInvoiceDetailId().equals(detail.getInvoiceDetailId())) {
				return invoiceDetail;
			}
		}
		return null;
	}
	
	public boolean isModified(InvoiceDetail invoiceDetail) {
		InvoiceDetail masterDetail = getMasterDetail(invoiceDetail);
		if (masterDetail == null) {
			return true;
		}
		if (invoiceDetail.getRecUpdateDatetime().equals(masterDetail.getRecUpdateDatetime())) {
			return false;
		}
		return true;
	}
}
