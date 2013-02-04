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

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Vector;

import com.jada.jpa.entity.Coupon;
import com.jada.jpa.entity.CreditDetail;
import com.jada.jpa.entity.CreditDetailTax;
import com.jada.jpa.entity.CreditHeader;
import com.jada.jpa.entity.InvoiceDetail;
import com.jada.jpa.entity.InvoiceDetailTax;
import com.jada.jpa.entity.InvoiceHeader;
import com.jada.jpa.entity.OrderDetailTax;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.OrderItemDetail;
import com.jada.jpa.entity.ShipDetail;
import com.jada.jpa.entity.ShipHeader;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.Tax;
import com.jada.order.payment.CreditCardInfo;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

public class OrderEngineBase {
	Vector<String> itemSkuCds = new Vector<String>();
	Vector<Coupon> coupons = new Vector<Coupon>();
	CreditCardInfo creditCardInfo = null;
	DecimalFormat format = new DecimalFormat("0000000000");
	
	public void setDirty(String itemSkuCd) {
		itemSkuCds.add(itemSkuCd);
	}
	
	public void setDirty(Coupon coupon) {
		coupons.add(coupon);
	}
	
	public boolean isDirty(String itemSkuCd) {
		if (itemSkuCd == null) {
			return true;
		}
		
		Iterator<?> iterator = itemSkuCds.iterator();
		while (iterator.hasNext()) {
			String i = (String) iterator.next();
			if (i.equals(itemSkuCd)) {
				return true;
			}
		}
		return false;
	}

	public boolean isDirty(Coupon value) {
		Iterator<?> iterator = coupons.iterator();
		while (iterator.hasNext()) {
			Coupon coupon = (Coupon) iterator.next();
			if (coupon.equals(value)) {
				return true;
			}
		}
		return false;
	}
	
	public void clean() {
		itemSkuCds.clear();
		coupons.clear();
	}
	
	public boolean isOpen(OrderHeader orderHeader) {
		return orderHeader.getOrderStatus().equals(Constants.ORDERSTATUS_OPEN);
	}
	
	public boolean isProcessing(OrderHeader orderHeader) {
		return orderHeader.getOrderStatus().equals(Constants.ORDERSTATUS_PROCESSING);
	}
	
	public boolean isVoided(OrderHeader orderHeader) {
		return orderHeader.getOrderStatus().equals(Constants.ORDERSTATUS_VOIDED);
	}
	
	public boolean isCancelled(OrderHeader orderHeader) {
		return orderHeader.getOrderStatus().equals(Constants.ORDERSTATUS_CANCELLED);
	}
	
	public boolean isCompleted(OrderHeader orderHeader) {
		return orderHeader.getOrderStatus().equals(Constants.ORDERSTATUS_COMPLETED);
	}

	public boolean isClosed(OrderHeader orderHeader) {
		return orderHeader.getOrderStatus().equals(Constants.ORDERSTATUS_CLOSED);
	}
	
	public boolean isOnHold(OrderHeader orderHeader) {
		return orderHeader.getOrderStatus().equals(Constants.ORDERSTATUS_ONHOLD);
	}
	
	public boolean isAuthorized(OrderHeader orderHeader) {
		return orderHeader.getPaymentTran() != null;
	}
	
	public boolean isOpen(InvoiceHeader invoiceHeader) {
		return invoiceHeader.getInvoiceStatus().equals(Constants.ORDERSTATUS_OPEN);
	}

	public boolean isProcessing(InvoiceHeader invoiceHeader) {
		return invoiceHeader.getInvoiceStatus().equals(Constants.ORDERSTATUS_PROCESSING);
	}
	
	public boolean isCancelled(InvoiceHeader invoiceHeader) {
		return invoiceHeader.getInvoiceStatus().equals(Constants.ORDERSTATUS_CANCELLED);
	}
	
	public boolean isCompleted(InvoiceHeader invoiceHeader) {
		return invoiceHeader.getInvoiceStatus().equals(Constants.ORDERSTATUS_COMPLETED);
	}
	
	public boolean isClosed(InvoiceHeader invoiceHeader) {
		return invoiceHeader.getInvoiceStatus().equals(Constants.ORDERSTATUS_CLOSED);
	}
	
	public boolean isVoided(InvoiceHeader invoiceHeader) {
		return invoiceHeader.getInvoiceStatus().equals(Constants.ORDERSTATUS_VOIDED);
	}
	
	public boolean isOpen(ShipHeader shipHeader) {
		return shipHeader.getShipStatus().equals(Constants.ORDERSTATUS_OPEN);
	}
	
	public boolean isProcessing(ShipHeader shipHeader) {
		return shipHeader.getShipStatus().equals(Constants.ORDERSTATUS_PROCESSING);
	}
	
	public boolean isCancelled(ShipHeader shipHeader) {
		return shipHeader.getShipStatus().equals(Constants.ORDERSTATUS_CANCELLED);
	}
	
	public boolean isCompleted(ShipHeader shipHeader) {
		return shipHeader.getShipStatus().equals(Constants.ORDERSTATUS_COMPLETED);
	}
	
	public boolean isClosed(ShipHeader shipHeader) {
		return shipHeader.getShipStatus().equals(Constants.ORDERSTATUS_CLOSED);
	}
	
	public boolean isVoided(ShipHeader shipHeader) {
		return shipHeader.getShipStatus().equals(Constants.ORDERSTATUS_VOIDED);
	}
	
	
	public boolean isOpen(CreditHeader creditHeader) {
		return creditHeader.getCreditStatus().equals(Constants.ORDERSTATUS_OPEN);
	}
	
	public boolean isProcessing(CreditHeader creditHeader) {
		return creditHeader.getCreditStatus().equals(Constants.ORDERSTATUS_PROCESSING);
	}
	
	public boolean isCancelled(CreditHeader creditHeader) {
		return creditHeader.getCreditStatus().equals(Constants.ORDERSTATUS_CANCELLED);
	}
	
	public boolean isCompleted(CreditHeader creditHeader) {
		return creditHeader.getCreditStatus().equals(Constants.ORDERSTATUS_COMPLETED);
	}
	
	public boolean isClosed(CreditHeader creditHeader) {
		return creditHeader.getCreditStatus().equals(Constants.ORDERSTATUS_CLOSED);
	}
	
	public boolean isVoided(CreditHeader creditHeader) {
		return creditHeader.getCreditStatus().equals(Constants.ORDERSTATUS_VOIDED);
	}

	public String generateNumber(Site site, String sequenceId) throws Exception {
		Long sequenceNum = Utility.getNextSequenceNum(site, sequenceId);
		return format.format(sequenceNum.longValue());
	}
	
	public CreditCardInfo getCreditCardInfo() {
		return creditCardInfo;
	}

	public void setCreditCardInfo(CreditCardInfo creditCardInfo) {
		this.creditCardInfo = creditCardInfo;
	}
	
	public String calcStatus(OrderHeader orderHeader) throws Exception {
		if (orderHeader.getOrderStatus() != null) {
			if (orderHeader.getOrderStatus().equals(Constants.ORDERSTATUS_CANCELLED)) {
				return Constants.ORDERSTATUS_CANCELLED;
			}
			if (orderHeader.getOrderStatus().equals(Constants.ORDERSTATUS_ONHOLD)) {
				return Constants.ORDERSTATUS_ONHOLD;
			}
		}
		
		if (!Format.isNullOrEmpty(orderHeader.getOrderAbundantLoc())) {
			return Constants.ORDERSTATUS_OPEN;
		}
		
		boolean hasInvoice = false;
		boolean hasShip = false;
		boolean isAuthorized = false;
		int voidInvoiceQty = 0;
		if (orderHeader.getInvoiceHeaders().size() > 0) {
			hasInvoice = true;
		}
		for (InvoiceHeader invoiceHeader : orderHeader.getInvoiceHeaders()) {
			if (isVoided(invoiceHeader)) {
				for (InvoiceDetail invoiceDetail : invoiceHeader.getInvoiceDetails()) {
					voidInvoiceQty += invoiceDetail.getItemInvoiceQty();
				}
			}
		}

		if (orderHeader.getShipHeaders().size() > 0) {
			hasShip = true;
		}
		if (orderHeader.getPaymentTran() != null) {
			isAuthorized = true;
		}
		if (orderHeader.getPaymentGatewayProvider() == null) {
			isAuthorized = true;
		}
		if (!hasInvoice && !hasShip && !isAuthorized) {
			return Constants.ORDERSTATUS_OPEN;
		}
		
		int itemOrderQty = 0;
		for (OrderItemDetail orderItemDetail : orderHeader.getOrderItemDetails()) {
			itemOrderQty += orderItemDetail.getItemOrderQty();
		}
		
		int itemInvoiceQty = 0;
		for (InvoiceHeader invoiceHeader : orderHeader.getInvoiceHeaders()) {
			if (!isCompleted(invoiceHeader) && !isClosed(invoiceHeader) && !isVoided(invoiceHeader)) {
				continue;
			}
			for (InvoiceDetail invoiceDetail : invoiceHeader.getInvoiceDetails()) {
				itemInvoiceQty += invoiceDetail.getItemInvoiceQty();
			}
		}
		
		if (itemInvoiceQty != itemOrderQty) {
			return Constants.ORDERSTATUS_PROCESSING;
		}
		else {
			boolean allClosed = true;
			for (InvoiceHeader invoiceHeader : orderHeader.getInvoiceHeaders()) {
				if (isCompleted(invoiceHeader)) {
					allClosed = false;
					break;
				}
			}
			if (allClosed) {
				return Constants.ORDERSTATUS_CLOSED;
			}
			else {
				int itemShipQty = 0;
				for (ShipHeader shipHeader : orderHeader.getShipHeaders()) {
					if (!isCompleted(shipHeader) && !isClosed(shipHeader) && !isVoided(shipHeader)) {
						continue;
					}
					for (ShipDetail shipDetail : shipHeader.getShipDetails()) {
						itemShipQty += shipDetail.getItemShipQty();
					}
				}
				if (itemShipQty != itemOrderQty) {
					return Constants.ORDERSTATUS_PROCESSING;
				}
				else {
					return Constants.ORDERSTATUS_COMPLETED;
				}
			}
		}
	}
	
	public String calcStatus(InvoiceHeader invoiceHeader) throws Exception {
		if (Format.isNullOrEmpty(invoiceHeader.getInvoiceStatus())) {
			return Constants.ORDERSTATUS_PROCESSING;
		}
		
		int itemInvoiceQty = 0;
		for (InvoiceDetail invoiceDetail : invoiceHeader.getInvoiceDetails()) {
			itemInvoiceQty += invoiceDetail.getItemInvoiceQty();
		}
		
		int itemCreditQty = 0;
		for (CreditHeader creditHeader : invoiceHeader.getCreditHeaders()) {
			if (isVoided(creditHeader)) {
				continue;
			}
			if (isCancelled(creditHeader)) {
				continue;
			}
			for (CreditDetail creditDetail : creditHeader.getCreditDetails()) {
				itemCreditQty += creditDetail.getItemCreditQty();
			}
		}
		
		if (itemInvoiceQty == itemCreditQty) {
			return Constants.ORDERSTATUS_CLOSED;
		}
		return invoiceHeader.getInvoiceStatus();
	}
	
	public String calcStatus(CreditHeader creditHeader) throws Exception {
		if (Format.isNullOrEmpty(creditHeader.getCreditStatus())) {
			return Constants.ORDERSTATUS_PROCESSING;
		}
		return creditHeader.getCreditStatus();
	}
	
	public String calcStatus(ShipHeader shipHeader) throws Exception {
		if (Format.isNullOrEmpty(shipHeader.getShipStatus())) {
			return Constants.ORDERSTATUS_PROCESSING;
		}
		return shipHeader.getShipStatus();
	}

	public ItemBalance getItemBalance(OrderHeader orderHeader, String itemSkuCd, InvoiceHeader invoiceHeader, CreditHeader creditHeader, ShipHeader shipHeader) throws Exception {
		Iterator<?> iterator = orderHeader.getOrderItemDetails().iterator();
		boolean found = false;
		OrderItemDetail orderItemDetail = null;
		while (iterator.hasNext()) {
			orderItemDetail = (OrderItemDetail) iterator.next();
			if (orderItemDetail.getItemSkuCd().equals(itemSkuCd)) {
				found = true;
				break;
			}
		}
		if (!found) {
			throw new OrderItemNotFoundException("");
		}
		
		Vector<ItemTaxBalance> taxBalances = new Vector<ItemTaxBalance>();
		ItemBalance itemBalance = new ItemBalance();
		itemBalance.setOrderQty(orderItemDetail.getItemOrderQty().intValue());
		float orderAmount = orderItemDetail.getItemDetailAmount().floatValue() - orderItemDetail.getItemDetailDiscountAmount().floatValue();
		itemBalance.setOrderAmount(orderAmount);
		Iterator<?> taxes = orderItemDetail.getOrderDetailTaxes().iterator();
		while (taxes.hasNext()) {
			OrderDetailTax orderDetailTax = (OrderDetailTax) taxes.next();
			ItemTaxBalance itemTax = locateItemTax(orderDetailTax.getTax(), orderDetailTax.getTaxName(), taxBalances);
			itemTax.setOrderTaxAmount(orderDetailTax.getTaxAmount());
		}
		
		iterator = orderItemDetail.getInvoiceDetails().iterator();
		int invoiceQty = 0;
		float invoiceAmount = 0;
		while (iterator.hasNext()) {
			InvoiceDetail invoiceDetail = (InvoiceDetail) iterator.next();
			InvoiceHeader header = invoiceDetail.getInvoiceHeader();
			if (header.getInvoiceHeaderId() == null) {
				continue;
			}
			if (header.getInvoiceStatus().equals(Constants.ORDERSTATUS_CANCELLED) || header.getInvoiceStatus().equals(Constants.ORDERSTATUS_VOIDED)) {
				continue;
			}
			if (invoiceHeader != null) {
				if (header.getInvoiceHeaderId().equals(invoiceHeader.getInvoiceHeaderId())) {
					continue;
				}
			}
			invoiceQty += invoiceDetail.getItemInvoiceQty().intValue();
			invoiceAmount += invoiceDetail.getItemInvoiceAmount().floatValue();
			
			Iterator<?> invoiceTaxes = invoiceDetail.getInvoiceDetailTaxes().iterator();
			while (invoiceTaxes.hasNext()) {
				InvoiceDetailTax invoiceDetailTax = (InvoiceDetailTax) invoiceTaxes.next();
				ItemTaxBalance itemTax = locateItemTax(invoiceDetailTax.getTax(), invoiceDetailTax.getTaxName(), taxBalances);
				float taxAmount = itemTax.getInvoiceTaxAmount() + invoiceDetailTax.getTaxAmount();
				itemTax.setInvoiceTaxAmount(taxAmount);
			}
		}
		itemBalance.setInvoiceQty(invoiceQty);
		itemBalance.setInvoiceAmount(invoiceAmount);

		iterator = orderItemDetail.getShipDetails().iterator();
		int shipQty = 0;
		while (iterator.hasNext()) {
			ShipDetail shipDetail = (ShipDetail) iterator.next();
			ShipHeader header = shipDetail.getShipHeader();
			if (header.getShipHeaderId() == null) {
				continue;
			}
			if (header.getShipStatus().equals(Constants.ORDERSTATUS_CANCELLED) || header.getShipStatus().equals(Constants.ORDERSTATUS_VOIDED)) {
				continue;
			}
			if (shipHeader != null) {
				if (header.getShipHeaderId().equals(shipHeader.getShipHeaderId())) {
					continue;
				}
			}
			shipQty += shipDetail.getItemShipQty().intValue();
		}
		itemBalance.setShipQty(shipQty);
		
		iterator = orderItemDetail.getCreditDetails().iterator();
		int creditQty = 0;
		float creditAmount = 0;
		while (iterator.hasNext()) {
			CreditDetail creditDetail = (CreditDetail) iterator.next();
			CreditHeader header = creditDetail.getCreditHeader();
			if (header.getCreditHeaderId() == null) {
				continue;
			}
			if (header.getCreditStatus().equals(Constants.ORDERSTATUS_CANCELLED) || header.getCreditStatus().equals(Constants.ORDERSTATUS_VOIDED)) {
				continue;
			}
			if (creditHeader != null) {
				if (header.getCreditHeaderId().equals(creditHeader.getCreditHeaderId())) {
					continue;
				}
			}
			creditQty += creditDetail.getItemCreditQty().intValue();
			creditAmount += creditDetail.getItemCreditAmount().floatValue();
			
			Iterator<?> creditTaxes = creditDetail.getCreditDetailTaxes().iterator();
			while (creditTaxes.hasNext()) {
				CreditDetailTax creditDetailTax = (CreditDetailTax) creditTaxes.next();
				ItemTaxBalance itemTax = locateItemTax(creditDetailTax.getTax(), creditDetailTax.getTaxName(), taxBalances);
				float taxAmount = itemTax.getCreditTaxAmount() + creditDetailTax.getTaxAmount();
				itemTax.setCreditTaxAmount(taxAmount);
			}
		}
		ItemTaxBalance itemTaxBalances[] = new ItemTaxBalance[taxBalances.size()];
		taxBalances.copyInto(itemTaxBalances);
		itemBalance.setItemTaxBalances(itemTaxBalances);
		itemBalance.setCreditQty(creditQty);
		itemBalance.setCreditAmount(creditAmount);

		return itemBalance;
	}
	
	private ItemTaxBalance locateItemTax(Tax tax, String taxName, Vector<ItemTaxBalance> vector) {
		Iterator<?> iterator = vector.iterator();
		ItemTaxBalance itemTax = null;
		boolean found = false;
		while (iterator.hasNext()) {
			itemTax = (ItemTaxBalance) iterator.next();
			if (itemTax.getTax().getTaxId().equals(tax.getTaxId())) {
				found = true;
				break;
			}
		}
		if (!found) {
			itemTax = new ItemTaxBalance();
			itemTax.setTax(tax);
			itemTax.setTaxName(taxName);
			vector.add(itemTax);
		}
		return itemTax;
	}
	
	class ItemTaxBalance {
		Tax tax;
		String taxName;
		float orderTaxAmount;
		float invoiceTaxAmount;
		float creditTaxAmount;
		public Tax getTax() {
			return tax;
		}
		public void setTax(Tax tax) {
			this.tax = tax;
		}
		public String getTaxName() {
			return taxName;
		}
		public void setTaxName(String taxName) {
			this.taxName = taxName;
		}
		public float getOrderTaxAmount() {
			return orderTaxAmount;
		}
		public void setOrderTaxAmount(float orderTaxAmount) {
			this.orderTaxAmount = orderTaxAmount;
		}
		public float getInvoiceTaxAmount() {
			return invoiceTaxAmount;
		}
		public void setInvoiceTaxAmount(float invoiceTaxAmount) {
			this.invoiceTaxAmount = invoiceTaxAmount;
		}
		public float getCreditTaxAmount() {
			return creditTaxAmount;
		}
		public void setCreditTaxAmount(float creditTaxAmount) {
			this.creditTaxAmount = creditTaxAmount;
		}
	}
	class ItemBalance {
		public int orderQty;
		public float orderAmount;
		public int invoiceQty;
		public float invoiceAmount;
		public int shipQty;
		public int creditQty;
		public float creditAmount;
		ItemTaxBalance itemTaxBalances[];
		public int getOrderQty() {
			return orderQty;
		}
		public void setOrderQty(int orderQty) {
			this.orderQty = orderQty;
		}
		public int getInvoiceQty() {
			return invoiceQty;
		}
		public void setInvoiceQty(int invoiceQty) {
			this.invoiceQty = invoiceQty;
		}
		public int getShipQty() {
			return shipQty;
		}
		public void setShipQty(int shipQty) {
			this.shipQty = shipQty;
		}
		public int getCreditQty() {
			return creditQty;
		}
		public void setCreditQty(int creditQty) {
			this.creditQty = creditQty;
		}
		public float getOrderAmount() {
			return orderAmount;
		}
		public void setOrderAmount(float orderAmount) {
			this.orderAmount = orderAmount;
		}
		public float getInvoiceAmount() {
			return invoiceAmount;
		}
		public void setInvoiceAmount(float invoiceAmount) {
			this.invoiceAmount = invoiceAmount;
		}
		public float getCreditAmount() {
			return creditAmount;
		}
		public void setCreditAmount(float creditAmount) {
			this.creditAmount = creditAmount;
		}
		public ItemTaxBalance[] getItemTaxBalances() {
			return itemTaxBalances;
		}
		public void setItemTaxBalances(ItemTaxBalance[] itemTaxBalances) {
			this.itemTaxBalances = itemTaxBalances;
		}
	}
}
