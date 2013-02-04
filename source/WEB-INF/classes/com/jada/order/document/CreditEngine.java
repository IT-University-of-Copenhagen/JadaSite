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
import com.jada.jpa.entity.CreditDetail;
import com.jada.jpa.entity.CreditDetailTax;
import com.jada.jpa.entity.CreditHeader;
import com.jada.jpa.entity.InvoiceDetail;
import com.jada.jpa.entity.InvoiceHeader;
import com.jada.jpa.entity.OrderDetailTax;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.OrderItemDetail;
import com.jada.jpa.entity.PaymentTran;
import com.jada.jpa.entity.User;
import com.jada.order.payment.PaymentEngine;
import com.jada.order.payment.PaymentManager;
import com.jada.util.Constants;
import com.jada.util.Utility;

public class CreditEngine extends OrderEngineBase {
	InvoiceHeader invoiceHeader = null;
	OrderHeader orderHeader = null;
	CreditHeader creditHeader = null;
	CreditHeader masterHeader = null;
	CreditDetail lastCreditDetail = null;
	User user = null;
	boolean creditShipping = false;
	
	public CreditEngine(InvoiceHeader invoiceHeader, User user) {
		this.invoiceHeader = invoiceHeader;
		this.orderHeader = invoiceHeader.getOrderHeader();;
		this.user = user;
		creditHeader = new CreditHeader();
		creditHeader.setOrderHeader(orderHeader);
		creditHeader.setInvoiceHeader(invoiceHeader);
		creditHeader.setUpdateInventory(String.valueOf(Constants.VALUE_YES));
		creditHeader.setShippingTotal((float) 0);
		creditHeader.setCreditTotal((float) 0);
		creditHeader.setCreditStatus(Constants.ORDERSTATUS_OPEN);
		creditHeader.setCreditDate(new Date());
		creditHeader.setRecUpdateBy(user.getUserId());
		creditHeader.setRecUpdateDatetime(new Date());
		creditHeader.setRecCreateBy(user.getUserId());
		creditHeader.setRecCreateDatetime(new Date());
//		orderHeader.getCreditHeaders().add(creditHeader);
	}
	
	public CreditEngine(CreditHeader header, User user) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		this.creditHeader = clone(header);
		this.user = user;
		this.orderHeader = creditHeader.getOrderHeader();
		this.invoiceHeader = creditHeader.getInvoiceHeader();
		this.masterHeader = header;
	}
	
	static public CreditHeader clone(CreditHeader header) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		CreditHeader creditHeader = new CreditHeader();
		creditHeader.setCreditHeaderId(header.getCreditHeaderId());
		creditHeader.setCreditNum(header.getCreditNum());
		creditHeader.setUpdateInventory(header.getUpdateInventory());
		creditHeader.setShippingTotal(header.getShippingTotal());
		creditHeader.setCreditTotal(header.getCreditTotal());
		creditHeader.setCreditStatus(header.getCreditStatus());
		creditHeader.setCreditDate(header.getCreditDate());
		creditHeader.setRecUpdateBy(header.getRecUpdateBy());
		creditHeader.setRecUpdateDatetime(header.getRecUpdateDatetime());
		creditHeader.setRecCreateBy(header.getRecCreateBy());
		creditHeader.setRecCreateDatetime(header.getRecCreateDatetime());
		creditHeader.setOrderHeader(header.getOrderHeader());
		creditHeader.setInvoiceHeader(header.getInvoiceHeader());
		
		if (header.getPaymentTran() != null) {
			PaymentTran pt = header.getPaymentTran();
			PaymentTran paymentTran = new PaymentTran();
			PropertyUtils.copyProperties(paymentTran, pt);
			creditHeader.setPaymentTran(paymentTran);
		}
		if (header.getVoidPaymentTran() != null) {
			PaymentTran pt = header.getVoidPaymentTran();
			PaymentTran paymentTran = new PaymentTran();
			PropertyUtils.copyProperties(paymentTran, pt);
			creditHeader.setVoidPaymentTran(paymentTran);
		}
		
		Iterator<?> iterator = header.getCreditDetails().iterator();
		while (iterator.hasNext()) {
			CreditDetail detail = (CreditDetail) iterator.next();
			CreditDetail creditDetail = new CreditDetail();
			creditDetail.setCreditHeader(creditHeader);
			creditHeader.getCreditDetails().add(creditDetail);
			creditDetail.setCreditDetailId(detail.getCreditDetailId());
			creditDetail.setSeqNum(detail.getSeqNum());
			creditDetail.setItemCreditQty(detail.getItemCreditQty());
			creditDetail.setItemCreditAmount(detail.getItemCreditAmount());
			creditDetail.setRecUpdateBy(header.getRecUpdateBy());
			creditDetail.setRecUpdateDatetime(header.getRecUpdateDatetime());
			creditDetail.setRecCreateBy(header.getRecCreateBy());
			creditDetail.setRecCreateDatetime(header.getRecCreateDatetime());
			creditDetail.setOrderItemDetail(detail.getOrderItemDetail());
			
			Iterator<?> taxIterator = detail.getCreditDetailTaxes().iterator();
			while (taxIterator.hasNext()) {
				CreditDetailTax detailTax = (CreditDetailTax) taxIterator.next();
				CreditDetailTax creditDetailTax = new CreditDetailTax();
				creditDetailTax.setCreditDetailTaxId(detailTax.getCreditDetailTaxId());
				creditDetailTax.setTaxName(detailTax.getTaxName());
				creditDetailTax.setTaxAmount(detailTax.getTaxAmount());
				creditDetailTax.setRecUpdateBy(detailTax.getRecUpdateBy());
				creditDetailTax.setRecUpdateDatetime(detailTax.getRecUpdateDatetime());
				creditDetailTax.setRecCreateBy(detailTax.getRecCreateBy());
				creditDetailTax.setRecCreateDatetime(detailTax.getRecCreateDatetime());
				creditDetailTax.setTax(detailTax.getTax());
				creditDetailTax.setCreditDetail(creditDetail);
				creditDetail.getCreditDetailTaxes().add(creditDetailTax);
				creditDetailTax.setCreditHeader(creditHeader);
				creditHeader.getCreditTaxes().add(creditDetailTax);
			}
		}
		
		Iterator<?> taxIterator = header.getCreditTaxes().iterator();
		while (taxIterator.hasNext()) {
			CreditDetailTax detailTax = (CreditDetailTax) taxIterator.next();
			if (detailTax.getCreditDetail() != null) {
				continue;
			}
			CreditDetailTax creditDetailTax = new CreditDetailTax();
			creditDetailTax.setCreditDetailTaxId(detailTax.getCreditDetailTaxId());
			creditDetailTax.setTaxName(detailTax.getTaxName());
			creditDetailTax.setTaxAmount(detailTax.getTaxAmount());
			creditDetailTax.setRecUpdateBy(detailTax.getRecUpdateBy());
			creditDetailTax.setRecUpdateDatetime(detailTax.getRecUpdateDatetime());
			creditDetailTax.setRecCreateBy(detailTax.getRecCreateBy());
			creditDetailTax.setRecCreateDatetime(detailTax.getRecCreateDatetime());
			creditDetailTax.setCreditHeader(creditHeader);
			creditHeader.getCreditTaxes().add(creditDetailTax);
		}
		
		return creditHeader;
	}
	
	public void creditAll() throws Exception {
		float shippingTotal = invoiceHeader.getShippingTotal();
		Iterator<?> iterator = invoiceHeader.getCreditHeaders().iterator();
		while (iterator.hasNext()) {
			CreditHeader creditHeader = (CreditHeader) iterator.next();
			if (creditHeader.getCreditHeaderId() == null) {
				continue;
			}
			shippingTotal -= creditHeader.getShippingTotal();
		}
		this.setShippingTotal(shippingTotal);
		
		iterator = invoiceHeader.getInvoiceDetails().iterator();
		while (iterator.hasNext()) {
			InvoiceDetail invoiceDetail = (InvoiceDetail) iterator.next();
			int itemCreditQty = invoiceDetail.getItemInvoiceQty();
			float itemCreditAmount = invoiceDetail.getItemInvoiceAmount();
			
			for (CreditHeader cheader : invoiceHeader.getCreditHeaders()) {
				if (cheader.getCreditStatus().equals(Constants.ORDERSTATUS_CANCELLED) || cheader.getCreditStatus().equals(Constants.ORDERSTATUS_VOIDED)) {
					continue;
				}
				if (cheader != null) {
					if (cheader.getCreditHeaderId().equals(creditHeader.getCreditHeaderId())) {
						continue;
					}
				}
				for (CreditDetail creditDetail : cheader.getCreditDetails()) {
					if (!creditDetail.getOrderItemDetail().getOrderItemDetailId().equals(invoiceDetail.getOrderItemDetail().getOrderItemDetailId())) {
						continue;
					}
					itemCreditQty -= creditDetail.getItemCreditQty();
					itemCreditAmount -= creditDetail.getItemCreditAmount();
				} 
			}
			setQty(invoiceDetail.getOrderItemDetail().getItemSkuCd(), itemCreditQty);
		}
		calculateHeader();
	}
	
	public void setQty(String itemSkuCd, int qty) throws Exception {
		CreditDetail creditDetail = null;
		boolean found = false;
		Iterator<?> iterator = creditHeader.getCreditDetails().iterator();
		while (iterator.hasNext()) {
			creditDetail = (CreditDetail) iterator.next();
			if (creditDetail.getOrderItemDetail().getItemSkuCd().equals(itemSkuCd)) {
				found = true;
				break;
			}
		}
		if (!found) {
			creditDetail = new CreditDetail();
			creditDetail.setRecCreateBy(user.getUserId());
			creditDetail.setRecCreateDatetime(new Date());
			iterator = orderHeader.getOrderItemDetails().iterator();
			found = false;
			while (iterator.hasNext()) {
				OrderItemDetail orderItemDetail = (OrderItemDetail) iterator.next();
				if (orderItemDetail.getItemSkuCd().equals(itemSkuCd)) {
					creditDetail.setOrderItemDetail(orderItemDetail);
//					orderItemDetail.getCreditDetails().add(creditDetail);
					found = true;
					break;
				}
			}
			if (!found) {
				throw new OrderItemNotFoundException("");
			}
			creditDetail.setCreditHeader(creditHeader);
			
			iterator = creditHeader.getCreditDetails().iterator();
			int seqNum = 0;
			while (iterator.hasNext()) {
				CreditDetail c = (CreditDetail) iterator.next();
				if (c.getSeqNum() > seqNum) {
					seqNum = c.getSeqNum();
				}
			}
			creditDetail.setSeqNum(seqNum);
			creditHeader.getCreditDetails().add(creditDetail);
		}
		
		ItemBalance itemBalance = getItemBalance(orderHeader, itemSkuCd, null, creditHeader, null);
		int balanceQty = itemBalance.getInvoiceQty() - itemBalance.getCreditQty();
		float balanceAmount = itemBalance.getInvoiceAmount() - itemBalance.getCreditAmount();
		
		if (balanceQty < qty) {
			throw new OrderQuantityException("");
		}
		creditDetail.setItemCreditQty(qty);
		if (balanceQty == qty) {
			creditDetail.setItemCreditAmount(balanceAmount);
		}
		else {
			float itemCreditAmount = Utility.round(balanceAmount * qty / balanceQty, 2);
			creditDetail.setItemCreditAmount(itemCreditAmount);
		}
		
		creditDetail.getCreditDetailTaxes().clear();
		ItemTaxBalance itemTaxBalances[] = itemBalance.getItemTaxBalances();
		for (int i = 0; i < itemTaxBalances.length; i++) {
			ItemTaxBalance itemTaxBalance = itemTaxBalances[i];
			float taxBalanceAmount = itemTaxBalance.getInvoiceTaxAmount() - itemTaxBalance.getCreditTaxAmount();
			if (taxBalanceAmount <= 0) {
				continue;
			}
			CreditDetailTax creditDetailTax = new CreditDetailTax();
			creditDetailTax.setCreditHeader(creditHeader);
			creditDetailTax.setCreditDetail(creditDetail);
			creditDetail.getCreditDetailTaxes().add(creditDetailTax);
			creditDetailTax.setTax(itemTaxBalance.getTax());
			creditDetailTax.setTaxName(itemTaxBalance.getTaxName());
			if (balanceQty == qty) {
				creditDetailTax.setTaxAmount(Float.valueOf(taxBalanceAmount));
			}
			else {
				float taxInvoiceAmount = Utility.round(taxBalanceAmount * qty / balanceQty, 2);
				creditDetailTax.setTaxAmount(Float.valueOf(taxInvoiceAmount));
			}
			creditDetailTax.setRecUpdateBy(user.getUserId());
			creditDetailTax.setRecUpdateDatetime(new Date());
			creditDetailTax.setRecCreateBy(user.getUserId());
			creditDetailTax.setRecCreateDatetime(new Date());
			creditHeader.getCreditTaxes().add(creditDetailTax);
		}
		creditDetail.setRecUpdateBy(user.getUserId());
		creditDetail.setRecUpdateDatetime(new Date());
		creditHeader.setRecUpdateBy(user.getUserId());
		creditHeader.setRecUpdateDatetime(new Date());
		
		lastCreditDetail = creditDetail;
	}
	
	public void calculateHeader() throws Exception {
		float creditTotal = 0;
		Iterator<?> iterator = creditHeader.getCreditDetails().iterator();
		while (iterator.hasNext()) {
			CreditDetail creditDetail = (CreditDetail) iterator.next();
			creditTotal += creditDetail.getItemCreditAmount().floatValue();
			Iterator<?> taxIterator = creditDetail.getCreditDetailTaxes().iterator();
			while (taxIterator.hasNext()) {
				CreditDetailTax creditDetailTax = (CreditDetailTax) taxIterator.next();
				creditTotal += creditDetailTax.getTaxAmount();
			}
		}	
		creditTotal += creditHeader.getShippingTotal();
		iterator = creditHeader.getCreditTaxes().iterator();
		while (iterator.hasNext()) {
			CreditDetailTax creditDetailTax = (CreditDetailTax) iterator.next();
			if (creditDetailTax.getCreditDetail() != null) {
				continue;
			}
			creditTotal += creditDetailTax.getTaxAmount();
		}
		
		creditHeader.setCreditTotal(Float.valueOf(creditTotal));
		creditHeader.setRecUpdateBy(user.getUserId());
		creditHeader.setRecUpdateDatetime(new Date());
	}
	
	public void setShippingTotal(float shippingTotal) throws Exception {
		float shippingBalance = orderHeader.getShippingTotal() - orderHeader.getShippingDiscountTotal();
		Iterator<?> iterator = orderHeader.getCreditHeaders().iterator();
		while (iterator.hasNext()) {
			CreditHeader cHeader = (CreditHeader) iterator.next();
			if (cHeader.getCreditHeaderId() == null) {
				continue;
			}
			if (cHeader.getCreditHeaderId().equals(creditHeader.getCreditHeaderId())) {
				continue;
			}
			shippingBalance -= cHeader.getShippingTotal();
		}
		float ratio = 1;
		if (shippingTotal == 0) {
			ratio = 0;
		}
		if (shippingBalance != shippingTotal) {
			ratio = shippingTotal / shippingBalance;
		}
		
		creditHeader.setShippingTotal(shippingTotal);
		
		creditHeader.getCreditTaxes().clear();
		Iterator<?> shippingIterator = orderHeader.getOrderTaxes().iterator();
		while (shippingIterator.hasNext()) {
			OrderDetailTax orderDetailTax = (OrderDetailTax) shippingIterator.next();
			if (orderDetailTax.getOrderItemDetail() != null) {
				continue;
			}
			CreditDetailTax creditDetailTax = new CreditDetailTax();
			creditDetailTax.setCreditHeader(creditHeader);
			creditDetailTax.setTax(orderDetailTax.getTax());
			creditDetailTax.setTaxName(orderDetailTax.getTaxName());
			float taxAmount = orderDetailTax.getTaxAmount() * ratio;
			if (taxAmount <= 0) {
				continue;
			}
			creditDetailTax.setTaxAmount(taxAmount);
			creditDetailTax.setRecUpdateBy(user.getUserId());
			creditDetailTax.setRecUpdateDatetime(new Date());
			creditDetailTax.setRecCreateBy(user.getUserId());
			creditDetailTax.setRecCreateDatetime(new Date());
			creditHeader.getCreditTaxes().add(creditDetailTax);
		}
		creditHeader.setRecUpdateBy(user.getUserId());
		creditHeader.setRecUpdateDatetime(new Date());
	}
	
	public Vector<?> getCreditTaxes() {
		Vector<CreditDetailTax> creditDetailTaxes = new Vector<CreditDetailTax>();
		Iterator<?> iterator = creditHeader.getCreditTaxes().iterator();
		while (iterator.hasNext()) {
			CreditDetailTax creditDetailTax = (CreditDetailTax) iterator.next();
			boolean found = false;
			Iterator<?> sumIterator = creditDetailTaxes.iterator();
			CreditDetailTax sumTax = null;
			while (sumIterator.hasNext()) {
				sumTax = (CreditDetailTax) sumIterator.next();
				if (sumTax.getTaxName().equals(creditDetailTax.getTaxName())) {
					found = true;
					break;
				}	
			}
			if (!found) {
				sumTax = new CreditDetailTax();
				sumTax.setTaxName(creditDetailTax.getTaxName());
				sumTax.setTaxAmount((float) 0);
				sumTax.setTax(creditDetailTax.getTax());
				creditDetailTaxes.add(sumTax);
			}
			float taxAmount = sumTax.getTaxAmount();
			taxAmount += creditDetailTax.getTaxAmount();
			sumTax.setTaxAmount(taxAmount);
		}
		return creditDetailTaxes;
	}
	
	public void creditOrder(HttpServletRequest request) throws Exception {
		PaymentEngine paymentEngine = PaymentManager.getPaymentEngine(orderHeader.getPaymentGatewayProvider(), orderHeader.getSiteCurrency());
		if (isVoided(orderHeader)) {
			throw new OrderStateException("Order is already voided");
		}
		if (paymentEngine != null) {
			paymentEngine.creditPayment(creditHeader);
			
			PaymentTran paymentTran = new PaymentTran();
			paymentTran.setAuthCode(paymentEngine.getAuthCode());
			paymentTran.setPaymentReference1(paymentEngine.getPaymentReference1());
			paymentTran.setPaymentReference2(paymentEngine.getPaymentReference2());
			paymentTran.setPaymentReference3(paymentEngine.getPaymentReference3());
			paymentTran.setPaymentReference4(paymentEngine.getPaymentReference4());
			paymentTran.setPaymentReference5(paymentEngine.getPaymentReference5());
			paymentTran.setTranDatetime(new Date());
			paymentTran.setRecUpdateBy(user.getUserId());
			paymentTran.setRecUpdateDatetime(new Date());
			paymentTran.setRecCreateBy(user.getUserId());
			paymentTran.setRecCreateDatetime(new Date());
			creditHeader.setPaymentTran(paymentTran);
		}
		creditHeader.setCreditStatus(Constants.ORDERSTATUS_COMPLETED);
		creditHeader.setRecUpdateBy(user.getUserId());
		creditHeader.setRecUpdateDatetime(new Date());
	}
	
	public void voidCredit() throws Exception {
		if (!isCompleted(creditHeader)) {
			throw new OrderStateException("Order has not been completed");
		}
		
		String paymentGatewayProvider = orderHeader.getPaymentGatewayProvider();
		if (paymentGatewayProvider != null) {
			PaymentEngine paymentEngine = PaymentManager.getPaymentEngine(paymentGatewayProvider, orderHeader.getSiteCurrency());
			paymentEngine.voidCredit(creditHeader);
			PaymentTran paymentTran = new PaymentTran();
			paymentTran.setAuthCode(paymentEngine.getAuthCode());
			paymentTran.setPaymentReference1(paymentEngine.getPaymentReference1());
			paymentTran.setPaymentReference2(paymentEngine.getPaymentReference2());
			paymentTran.setPaymentReference3(paymentEngine.getPaymentReference3());
			paymentTran.setPaymentReference4(paymentEngine.getPaymentReference4());
			paymentTran.setPaymentReference5(paymentEngine.getPaymentReference5());
			paymentTran.setTranDatetime(new Date());
			paymentTran.setRecUpdateBy(user.getUserId());
			paymentTran.setRecUpdateDatetime(new Date());
			paymentTran.setRecCreateBy(user.getUserId());
			paymentTran.setRecCreateDatetime(new Date());
			creditHeader.setVoidPaymentTran(paymentTran);
		}
		creditHeader.setCreditStatus(Constants.ORDERSTATUS_VOIDED);
		creditHeader.setRecUpdateBy(user.getUserId());
		creditHeader.setRecUpdateDatetime(new Date());
	}
	
	public void cancelOrder() throws OrderStateException {
		if (!isOpen(creditHeader)) {
			throw new OrderStateException("Order cannot be cancelled");
		}
		creditHeader.setCreditStatus(Constants.ORDERSTATUS_CANCELLED);
		creditHeader.setRecUpdateBy(user.getUserId());
		creditHeader.setRecUpdateDatetime(new Date());
	}
	
	public void saveHeader() throws Exception {
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String userId = user == null ? Constants.USERNAME_SYSTEM : user.getUserId();
		Date current = new Date();
		if (creditHeader.getCreditNum() == null) {
			creditHeader.setCreditNum(generateNumber(orderHeader.getSiteDomain().getSite(), Constants.SEQUENCE_CREDIT));
			creditHeader.setCreditDate(new Date());
			creditHeader.setRecCreateBy(userId);
			creditHeader.setRecCreateDatetime(current);
		}
		creditHeader.setRecUpdateBy(userId);
		creditHeader.setRecUpdateDatetime(current);
		if (creditHeader.getCreditHeaderId() == null) {
			em.persist(creditHeader);
		}
	}
	
	public void saveOrder() throws Exception {
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		if (masterHeader == null || !masterHeader.getRecUpdateDatetime().equals(creditHeader.getRecUpdateDatetime())) {
			if (creditHeader.getCreditNum() == null) {
				creditHeader.setCreditNum(generateNumber(orderHeader.getSiteDomain().getSite(), Constants.SEQUENCE_CREDIT));
			}
			if (masterHeader == null) {
				masterHeader = new CreditHeader();
			}
			masterHeader.setCreditNum(creditHeader.getCreditNum());
			masterHeader.setShippingTotal(creditHeader.getShippingTotal());
			masterHeader.setCreditTotal(creditHeader.getCreditTotal());
			masterHeader.setCreditStatus(creditHeader.getCreditStatus());
			masterHeader.setCreditDate(creditHeader.getCreditDate());
			masterHeader.setUpdateInventory(creditHeader.getUpdateInventory());
			masterHeader.setRecUpdateBy(creditHeader.getRecUpdateBy());
			masterHeader.setRecUpdateDatetime(creditHeader.getRecUpdateDatetime());
			masterHeader.setRecCreateBy(creditHeader.getRecCreateBy());
			masterHeader.setRecCreateDatetime(creditHeader.getRecCreateDatetime());
			masterHeader.setOrderHeader(orderHeader);
			masterHeader.setInvoiceHeader(invoiceHeader);
			orderHeader.getCreditHeaders().add(masterHeader);

			// Copy data from creditHeader to masterHeader
			if (masterHeader.getCreditHeaderId() == null) {
				em.persist(masterHeader);
			}
			creditHeader.setCreditHeaderId(masterHeader.getCreditHeaderId());

			if (masterHeader != null) {
				Iterator<?> taxIterator = masterHeader.getCreditTaxes().iterator();
				while (taxIterator.hasNext()) {
					CreditDetailTax creditDetailTax = (CreditDetailTax) taxIterator.next();
					if (creditDetailTax.getCreditDetail() != null) {
						continue;
					}
					em.remove(creditDetailTax);
					taxIterator.remove();
				}
			}
			
			Iterator<?> taxIterator = creditHeader.getCreditTaxes().iterator();
			while (taxIterator.hasNext()) {
				CreditDetailTax creditDetailTax = (CreditDetailTax) taxIterator.next();
				if (creditDetailTax.getCreditDetail() != null) {
					continue;
				}
				CreditDetailTax masterTax = new CreditDetailTax();
				masterTax.setCreditDetailTaxId(creditDetailTax.getCreditDetailTaxId());
				masterTax.setTaxName(creditDetailTax.getTaxName());
				masterTax.setTaxAmount(creditDetailTax.getTaxAmount());
				masterTax.setRecUpdateBy(creditDetailTax.getRecUpdateBy());
				masterTax.setRecUpdateDatetime(creditDetailTax.getRecUpdateDatetime());
				masterTax.setRecCreateBy(creditDetailTax.getRecCreateBy());
				masterTax.setRecCreateDatetime(creditDetailTax.getRecCreateDatetime());
				masterTax.setCreditDetail(null);
				masterTax.setCreditHeader(masterHeader);
				masterTax.setTax(creditDetailTax.getTax());
				masterTax.setCreditDetailTaxId(null);
				em.persist(masterTax);
			}
		}
		
		Iterator<?> iterator = masterHeader.getCreditDetails().iterator();
		while (iterator.hasNext()) {
			CreditDetail masterDetail = (CreditDetail) iterator.next();
			Iterator<?> taxIterator = masterDetail.getCreditDetailTaxes().iterator();
			while (taxIterator.hasNext()) {
				CreditDetailTax masterDetailTax = (CreditDetailTax) taxIterator.next();
				em.remove(masterDetailTax);
				taxIterator.remove();
			}
			em.remove(masterDetail);
			iterator.remove();
		}
		
		iterator = creditHeader.getCreditDetails().iterator();
		while (iterator.hasNext()) {
			CreditDetail creditDetail = (CreditDetail) iterator.next();
			CreditDetail masterDetail = new CreditDetail();
			masterDetail.setCreditHeader(masterHeader);
			masterDetail.setSeqNum(creditDetail.getSeqNum());
			masterDetail.setItemCreditQty(creditDetail.getItemCreditQty());
			masterDetail.setItemCreditAmount(creditDetail.getItemCreditAmount());
			masterDetail.setRecUpdateBy(creditDetail.getRecUpdateBy());
			masterDetail.setRecUpdateDatetime(creditDetail.getRecUpdateDatetime());
			masterDetail.setRecCreateBy(creditDetail.getRecCreateBy());
			masterDetail.setRecCreateDatetime(creditDetail.getRecCreateDatetime());
			masterDetail.setOrderItemDetail(creditDetail.getOrderItemDetail());
			em.persist(masterDetail);
			masterHeader.getCreditDetails().add(masterDetail);
	
			Iterator<?> taxIterator = creditDetail.getCreditDetailTaxes().iterator();
			while (taxIterator.hasNext()) {
				CreditDetailTax creditDetailTax = (CreditDetailTax) taxIterator.next();
				CreditDetailTax masterTax = new CreditDetailTax();
				masterTax.setTaxName(creditDetailTax.getTaxName());
				masterTax.setTaxAmount(creditDetailTax.getTaxAmount());
				masterTax.setRecUpdateBy(creditDetailTax.getRecUpdateBy());
				masterTax.setRecUpdateDatetime(creditDetailTax.getRecUpdateDatetime());
				masterTax.setRecCreateBy(creditDetailTax.getRecCreateBy());
				masterTax.setRecCreateDatetime(creditDetailTax.getRecCreateDatetime());
				masterTax.setCreditDetail(masterDetail);
				masterDetail.getCreditDetailTaxes().add(masterTax);
				masterTax.setCreditHeader(masterHeader);
				masterTax.setTax(creditDetailTax.getTax());
				em.persist(masterTax);
			}
		}

		PaymentTran paymentTran = creditHeader.getPaymentTran();
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
		
		PaymentTran voidPaymentTran = creditHeader.getVoidPaymentTran();
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
	
	public CreditDetail getMasterDetail(CreditDetail creditDetail) {
		if (masterHeader == null) {
			return null;
		}
		if (creditDetail.getCreditDetailId() == null) {
			return null;
		}
		Iterator<?> iterator = masterHeader.getCreditDetails().iterator();
		while (iterator.hasNext()) {
			CreditDetail detail = (CreditDetail) iterator.next();
			if (creditDetail.getCreditDetailId().equals(detail.getCreditDetailId())) {
				return creditDetail;
			}
		}
		return null;
	}
	
	public boolean isModified(CreditDetail creditDetail) {
		CreditDetail masterDetail = getMasterDetail(creditDetail);
		if (masterDetail == null) {
			return true;
		}
		if (creditDetail.getRecUpdateDatetime().equals(masterDetail.getRecUpdateDatetime())) {
			return false;
		}
		return true;
	}

	public CreditHeader getCreditHeader() {
		return creditHeader;
	}

	public void setCreditHeader(CreditHeader creditHeader) {
		this.creditHeader = creditHeader;
	}

	public CreditDetail getLastCreditDetail() {
		return lastCreditDetail;
	}

	public void setLastCreditDetail(CreditDetail lastCreditDetail) {
		this.lastCreditDetail = lastCreditDetail;
	}

	public OrderHeader getOrderHeader() {
		return orderHeader;
	}

	public void setOrderHeader(OrderHeader orderHeader) {
		this.orderHeader = orderHeader;
	}
	
}
