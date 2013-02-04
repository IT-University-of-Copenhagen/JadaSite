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

import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;

import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.OrderHeaderDAO;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.CreditDetail;
import com.jada.jpa.entity.CreditHeader;
import com.jada.jpa.entity.CustomAttribute;
import com.jada.jpa.entity.CustomAttributeOption;
import com.jada.jpa.entity.InvoiceDetail;
import com.jada.jpa.entity.InvoiceHeader;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.ItemAttributeDetail;
import com.jada.jpa.entity.OrderAddress;
import com.jada.jpa.entity.OrderAttributeDetail;
import com.jada.jpa.entity.OrderDetailTax;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.OrderItemDetail;
import com.jada.jpa.entity.OrderOtherDetail;
import com.jada.jpa.entity.OrderTracking;
import com.jada.jpa.entity.PaymentTran;
import com.jada.jpa.entity.ShipDetail;
import com.jada.jpa.entity.ShipHeader;
import com.jada.jpa.entity.State;
import com.jada.order.document.OrderEngine;
import com.jada.order.payment.gateway.PayPalEngine;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;

public class OrderMaintBaseAction extends AdminLookupDispatchAction {
	protected void initOrder(OrderMaintBaseForm form, OrderHeader orderHeader, InvoiceHeader invoiceHeader, CreditHeader creditHeader, ShipHeader shipHeader, HttpServletRequest request) throws Exception {
		MessageResources resources = this.getResources(request);

		OrderEngine orderEngine = new OrderEngine(orderHeader, null);

		OrderHeaderDisplayForm orderHeaderDisplayForm = new OrderHeaderDisplayForm();
		orderHeaderDisplayForm.setLangName(orderHeader.getSiteProfile().getSiteProfileClass().getLanguage().getLangName());
		orderHeaderDisplayForm.setCurrencyCode(orderHeader.getSiteCurrency().getSiteCurrencyClass().getCurrency().getCurrencyCode());
		orderHeaderDisplayForm.setOrderHeaderId(orderHeader.getOrderHeaderId().toString());
		orderHeaderDisplayForm.setOrderNum(orderHeader.getOrderNum());
		orderHeaderDisplayForm.setCustEmail(orderHeader.getCustEmail());
		orderHeaderDisplayForm.setShippingTotal(Format.getFloat(orderHeader.getShippingTotal()));
		orderHeaderDisplayForm.setShippingDiscountTotal(Format.getFloat(orderHeader.getShippingDiscountTotal()));
		orderHeaderDisplayForm.setOrderPriceTotal(Format.getFloat(orderEngine.getOrderPriceTotal()));
		orderHeaderDisplayForm.setOrderPriceDiscountTotal(Format.getFloat(orderEngine.getOrderPriceDiscountTotal()));
		orderHeaderDisplayForm.setOrderSubTotal(Format.getFloat(orderEngine.getOrderSubTotal()));
		orderHeaderDisplayForm.setOrderTaxTotal(Format.getFloat(orderEngine.getOrderTaxTotal()));
		orderHeaderDisplayForm.setOrderTotal(Format.getFloat(orderHeader.getOrderTotal()));
		orderHeaderDisplayForm.setPaymentGatewayProvider(orderHeader.getPaymentGatewayProvider());
		orderHeaderDisplayForm.setCreditCardDesc(orderHeader.getCreditCardDesc());
		if (orderHeader.getCustCreditCardNum() != null) {
			orderHeaderDisplayForm.setCustCreditCardNum(AESEncoder.getInstance().decode(orderHeader.getCustCreditCardNum()));
		}
		orderHeaderDisplayForm.setShippingMethodName(orderHeader.getShippingMethodName());
		if (orderHeader.getShppingMethod() != null) {
			orderHeaderDisplayForm.setShippingMethodName(orderHeader.getShppingMethod().getShippingMethodLanguage().getShippingMethodName());
		}
		if (!Format.isNullOrEmpty(orderHeader.getOrderAbundantLoc())) {
			orderHeaderDisplayForm.setOrderAbundantLoc(resources.getMessage("order.step." + orderHeader.getOrderAbundantLoc()));
			if (orderHeader.getShippingValidUntil() != null) {
				orderHeaderDisplayForm.setShippingQuote(true);
			}
			if (orderHeader.getOrderAbundantLoc().equals(Constants.ORDER_STEP_SHIPPINGQUOTE)) {
				orderHeaderDisplayForm.setShippingQuote(true);
			}
		}
		form.setShippingQuoteTotal(Format.getFloat(orderHeader.getShippingTotal()));
		if (orderHeader.getShippingValidUntil() != null) {
			orderHeaderDisplayForm.setShippingValidUntil(Format.getFullDate(orderHeader.getShippingValidUntil()));
			form.setShippingValidUntil(Format.getFullDate(orderHeader.getShippingValidUntil()));
		}
		orderHeaderDisplayForm.setShippingPickUp(String.valueOf(orderHeader.getShippingPickUp()));
		if (orderHeader.getOrderStatus().equals(Constants.ORDERSTATUS_OPEN)) {
			orderHeaderDisplayForm.setOrderOpen(true);
		}
		orderHeaderDisplayForm.setOrderStatus(resources.getMessage("order.status." + orderHeader.getOrderStatus()));
		orderHeaderDisplayForm.setOrderDate(Format.getFullDate(orderHeader.getOrderDate()));
		form.setOrderHeader(orderHeaderDisplayForm);
		
		if (orderHeader.getPaymentTran() != null) {
			PaymentTran paymentTran = orderHeader.getPaymentTran();
			PaymentTranDisplayForm paymentForm = new PaymentTranDisplayForm();
			paymentForm.setAuthCode(paymentTran.getAuthCode());
			paymentForm.setPaymentReference1(paymentTran.getPaymentReference1());
			paymentForm.setPaymentReference2(paymentTran.getPaymentReference2());
			paymentForm.setPaymentReference3(paymentTran.getPaymentReference3());
			paymentForm.setPaymentReference4(paymentTran.getPaymentReference4());
			paymentForm.setPaymentReference5(paymentTran.getPaymentReference5());
			orderHeaderDisplayForm.setPaymentTran(paymentForm);
		}
		if (orderHeader.getVoidPaymentTran() != null) {
			PaymentTran paymentTran = orderHeader.getVoidPaymentTran();
			PaymentTranDisplayForm paymentForm = new PaymentTranDisplayForm();
			paymentForm.setAuthCode(paymentTran.getAuthCode());
			paymentForm.setPaymentReference1(paymentTran.getPaymentReference1());
			paymentForm.setPaymentReference2(paymentTran.getPaymentReference2());
			paymentForm.setPaymentReference3(paymentTran.getPaymentReference3());
			paymentForm.setPaymentReference4(paymentTran.getPaymentReference4());
			paymentForm.setPaymentReference5(paymentTran.getPaymentReference5());
			orderHeaderDisplayForm.setVoidPaymentTran(paymentForm);
		}
		
		OrderAddress custAddress = orderHeader.getCustAddress();
		CustomerAddressDisplayForm addressForm = new CustomerAddressDisplayForm();
		addressForm.setCustUseAddress(custAddress.getCustUseAddress());
		addressForm.setOrderAddressId(custAddress.getOrderAddressId().toString());
		addressForm.setCustPrefix(custAddress.getCustPrefix());
		addressForm.setCustFirstName(custAddress.getCustFirstName());
		addressForm.setCustMiddleName(custAddress.getCustMiddleName());
		addressForm.setCustLastName(custAddress.getCustLastName());
		addressForm.setCustSuffix(custAddress.getCustSuffix());
		addressForm.setCustAddressLine1(custAddress.getCustAddressLine1());
		addressForm.setCustAddressLine2(custAddress.getCustAddressLine2());
		addressForm.setCustCityName(custAddress.getCustCityName());
		Country country = custAddress.getCountry(); 
		State state = custAddress.getState();
		if (state != null) {
			addressForm.setCustStateName(state.getStateName());
			addressForm.setCustStateCode(state.getStateCode());
		}
		addressForm.setCustCountryName(country.getCountryName());
		addressForm.setCustCountryCode(country.getCountryCode());
		addressForm.setCustZipCode(custAddress.getCustZipCode());
		addressForm.setCustPhoneNum(custAddress.getCustPhoneNum());
		addressForm.setCustFaxNum(custAddress.getCustFaxNum());
		form.setCustAddress(addressForm);
		
		OrderAddress billingAddress = orderHeader.getBillingAddress();
		addressForm = new CustomerAddressDisplayForm();
		addressForm.setCustUseAddress(billingAddress.getCustUseAddress());
		if (billingAddress.getCustUseAddress().equals(Constants.CUST_ADDRESS_USE_OWN)) {
			addressForm.setOrderAddressId(billingAddress.getOrderAddressId().toString());
			addressForm.setCustPrefix(billingAddress.getCustPrefix());
			addressForm.setCustFirstName(billingAddress.getCustFirstName());
			addressForm.setCustMiddleName(billingAddress.getCustMiddleName());
			addressForm.setCustLastName(billingAddress.getCustLastName());
			addressForm.setCustSuffix(billingAddress.getCustSuffix());
			addressForm.setCustAddressLine1(billingAddress.getCustAddressLine1());
			addressForm.setCustAddressLine2(billingAddress.getCustAddressLine2());
			country = billingAddress.getCountry(); 
			state = billingAddress.getState();
			addressForm.setCustCityName(billingAddress.getCustCityName());
			if (state != null) {
				addressForm.setCustStateName(state.getStateName());
				addressForm.setCustStateCode(state.getStateCode());
			}
			addressForm.setCustCountryName(country.getCountryName());
			addressForm.setCustCountryCode(country.getCountryCode());
			addressForm.setCustZipCode(billingAddress.getCustZipCode());
			addressForm.setCustPhoneNum(billingAddress.getCustPhoneNum());
			addressForm.setCustFaxNum(billingAddress.getCustFaxNum());
		}
		form.setBillingAddress(addressForm);
		
		OrderAddress shippingAddress = orderHeader.getShippingAddress();
		addressForm = new CustomerAddressDisplayForm();
		addressForm.setCustUseAddress(shippingAddress.getCustUseAddress());
		if (shippingAddress.getCustUseAddress().equals(Constants.CUST_ADDRESS_USE_OWN)) {
			addressForm.setOrderAddressId(shippingAddress.getOrderAddressId().toString());
			addressForm.setCustPrefix(shippingAddress.getCustPrefix());
			addressForm.setCustFirstName(shippingAddress.getCustFirstName());
			addressForm.setCustMiddleName(shippingAddress.getCustMiddleName());
			addressForm.setCustLastName(shippingAddress.getCustLastName());
			addressForm.setCustSuffix(shippingAddress.getCustSuffix());
			addressForm.setCustAddressLine1(shippingAddress.getCustAddressLine1());
			addressForm.setCustAddressLine2(shippingAddress.getCustAddressLine2());
			addressForm.setCustCityName(shippingAddress.getCustCityName());
			country = shippingAddress.getCountry(); 
			state = shippingAddress.getState();
			if (state != null) {
				addressForm.setCustStateName(state.getStateName());
				addressForm.setCustStateCode(state.getStateCode());
			}
			addressForm.setCustCountryName(country.getCountryName());
			addressForm.setCustCountryCode(country.getCountryCode());
			addressForm.setCustZipCode(shippingAddress.getCustZipCode());
			addressForm.setCustPhoneNum(shippingAddress.getCustPhoneNum());
			addressForm.setCustFaxNum(shippingAddress.getCustFaxNum());
		}
		form.setShippingAddress(addressForm);
		
		Vector<OrderItemDetailDisplayForm> orderItemDetails = new Vector<OrderItemDetailDisplayForm>();
		Iterator<?> iterator = null;
		iterator = orderHeader.getOrderItemDetails().iterator();
		int itemOrderTotal = 0;
		int itemInvoiceTotal = 0;
		int itemCreditTotal = 0;
		int itemShipTotal = 0;
		float itemDetailAmount = 0;
		float itemDetailDiscountAmount = 0;
		float itemDetailSubTotal = 0;
		while (iterator.hasNext()) {
			OrderItemDetail orderItemDetail = (OrderItemDetail) iterator.next();
			OrderItemDetailDisplayForm itemDisplay = new OrderItemDetailDisplayForm();
			itemDisplay.setOrderItemDetailId(orderItemDetail.getOrderItemDetailId().toString());
			Item item = orderItemDetail.getItem();
			itemDisplay.setItemId("");
			itemDisplay.setItemNum(orderItemDetail.getItemNum());
			itemDisplay.setItemSkuCd(orderItemDetail.getItemSkuCd());
			itemDisplay.setItemShortDesc(orderItemDetail.getItemShortDesc());
			itemDisplay.setItemTierQty(Format.getInt(orderItemDetail.getItemTierQty()));
			itemDisplay.setItemTierPrice(Format.getFloat(orderItemDetail.getItemTierPrice()));
			itemDisplay.setItemOrderQty(Format.getInt(orderItemDetail.getItemOrderQty()));
			itemDisplay.setItemDetailAmount(Format.getFloat(orderItemDetail.getItemDetailAmount()));
			itemDisplay.setItemDetailDiscountAmount(Format.getFloat(orderItemDetail.getItemDetailDiscountAmount()));
			float total = orderItemDetail.getItemDetailAmount() - orderItemDetail.getItemDetailDiscountAmount();
			itemDisplay.setItemDetailSubTotal(Format.getFloat(total));
			
			if (item != null) {
				itemDisplay.setItemId(item.getItemId().toString());
				itemDisplay.setItemShortDesc(orderItemDetail.getItem().getItemLanguage().getItemShortDesc());
			}
			
			Vector<OrderItemAttributeBean> orderItemAttributes = new Vector<OrderItemAttributeBean>();
			if (item != null && item.getItemTypeCd().equals(Constants.ITEM_TYPE_SKU)) {
    			for (ItemAttributeDetail itemAttributeDetail : item.getItemAttributeDetails()) {
    				CustomAttribute customAttribute = itemAttributeDetail.getCustomAttributeDetail().getCustomAttribute();
    				if (customAttribute.getCustomAttribTypeCode() != Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP) {
    					continue;
    				}
	    			OrderItemAttributeBean orderItemAttributeBean = new OrderItemAttributeBean();
	    			orderItemAttributeBean.setCustomAttribDesc(customAttribute.getCustomAttributeLanguage().getCustomAttribDesc());
	        		CustomAttributeOption customAttributeOption = itemAttributeDetail.getCustomAttributeOption();
	        		orderItemAttributeBean.setCustomAttribValue(customAttributeOption.getCustomAttributeOptionLanguage().getCustomAttribValue());
	        		orderItemAttributes.add(orderItemAttributeBean);
    			}
			}
    		for (OrderAttributeDetail orderAttributeDetail : orderItemDetail.getOrderAttributeDetails()) {
    			OrderItemAttributeBean orderItemAttributeBean = new OrderItemAttributeBean();
    			CustomAttribute customAttribute = orderAttributeDetail.getCustomAttributeDetail().getCustomAttribute();
    			orderItemAttributeBean.setCustomAttribDesc(customAttribute.getCustomAttributeLanguage().getCustomAttribDesc());
        		
        		if (customAttribute.getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_CUST_INPUT) {
        			orderItemAttributeBean.setCustomAttribValue(orderAttributeDetail.getOrderAttribValue());
        		}
        		else {
	        		CustomAttributeOption customAttributeOption = orderAttributeDetail.getCustomAttributeOption();
	        		orderItemAttributeBean.setCustomAttribValue(customAttributeOption.getCustomAttributeOptionLanguage().getCustomAttribValue());
        		}
        		orderItemAttributes.add(orderItemAttributeBean);
    		}
			itemDisplay.setOrderItemAttributes(orderItemAttributes);
			
			Iterator<?> invoiceIterator = orderItemDetail.getInvoiceDetails().iterator();
			int itemInvoiceQty = 0;
			while (invoiceIterator.hasNext()) {
				InvoiceDetail invoiceDetail = (InvoiceDetail) invoiceIterator.next();
				InvoiceHeader iHeader = invoiceDetail.getInvoiceHeader();
				if (iHeader.getInvoiceHeaderId() == null) {
					continue;
				}
				if (invoiceHeader != null) {
					if (iHeader.getInvoiceHeaderId().equals(invoiceHeader.getInvoiceHeaderId())) {
						continue;
					}
				}
				String status = iHeader.getInvoiceStatus();
				if (status.equals(Constants.ORDERSTATUS_CANCELLED) || status.equals(Constants.ORDERSTATUS_VOIDED)) {
					continue;
				}
				itemInvoiceQty += invoiceDetail.getItemInvoiceQty();
				itemInvoiceTotal += invoiceDetail.getItemInvoiceQty();
			}
			itemDisplay.setItemInvoiceQty(Format.getInt(itemInvoiceQty));

			Iterator<?> creditIterator = orderItemDetail.getCreditDetails().iterator();
			int itemCreditQty = 0;
			while (creditIterator.hasNext()) {
				CreditDetail creditDetail = (CreditDetail) creditIterator.next();
				CreditHeader cHeader = creditDetail.getCreditHeader();
				if (cHeader.getCreditHeaderId() == null) {
					continue;
				}
				if (creditHeader != null) {
					if (cHeader.getCreditHeaderId().equals(creditHeader.getCreditHeaderId())) {
						continue;
					}
				}
				String status = cHeader.getCreditStatus();
				if (status.equals(Constants.ORDERSTATUS_CANCELLED) || status.equals(Constants.ORDERSTATUS_VOIDED)) {
					continue;
				}

				itemCreditQty += creditDetail.getItemCreditQty();
				itemCreditTotal += creditDetail.getItemCreditQty();
			}
			itemDisplay.setItemCreditQty(Format.getInt(itemCreditQty));

			Iterator<?> shipIterator = orderItemDetail.getShipDetails().iterator();
			int itemShipQty = 0;
			while (shipIterator.hasNext()) {
				ShipDetail shipDetail = (ShipDetail) shipIterator.next();
				ShipHeader sHeader = shipDetail.getShipHeader();
				if (sHeader.getShipHeaderId() == null) {
					continue;
				}
				if (shipHeader != null) {
					if (sHeader.getShipHeaderId().equals(shipHeader.getShipHeaderId())) {
						continue;
					}
				}
				String status = sHeader.getShipStatus();
				if (status.equals(Constants.ORDERSTATUS_CANCELLED) || status.equals(Constants.ORDERSTATUS_VOIDED)) {
					continue;
				}
				itemShipQty += shipDetail.getItemShipQty();
				itemShipTotal += shipDetail.getItemShipQty();
			}
			itemDisplay.setItemShipQty(Format.getInt(itemShipQty));

			itemOrderTotal += orderItemDetail.getItemOrderQty();
			itemDetailAmount += orderItemDetail.getItemDetailAmount();
			itemDetailDiscountAmount += orderItemDetail.getItemDetailDiscountAmount();
			itemDetailSubTotal += total;
			
			int itemSuggestInvoiceQty = orderItemDetail.getItemOrderQty() - itemInvoiceQty + itemCreditQty;
			int itemSuggestShipQty = orderItemDetail.getItemOrderQty() - itemShipQty;
			int itemSuggestCreditQty = itemInvoiceQty - itemCreditQty;
			itemDisplay.setItemSuggestInvoiceQty(itemSuggestInvoiceQty);
			itemDisplay.setItemSuggestShipQty(itemSuggestShipQty);
			itemDisplay.setItemSuggestCreditQty(itemSuggestCreditQty);
			
			orderItemDetails.add(itemDisplay);
		}
		form.setOrderItemDetails(orderItemDetails);

		orderHeaderDisplayForm.setItemOrderQty(Format.getInt(itemOrderTotal));
		orderHeaderDisplayForm.setItemInvoiceQty(Format.getInt(itemInvoiceTotal));
		orderHeaderDisplayForm.setItemCreditQty(Format.getInt(itemCreditTotal));
		orderHeaderDisplayForm.setItemShipQty(Format.getInt(itemShipTotal));
		orderHeaderDisplayForm.setItemDetailAmount(Format.getFloat(itemDetailAmount));
		orderHeaderDisplayForm.setItemDetailDiscountAmount(Format.getFloat(itemDetailDiscountAmount));
		orderHeaderDisplayForm.setItemDetailSubTotal(Format.getFloat(itemDetailSubTotal));
		
		form.setAllowCredit(false);
		form.setAllowInvoice(false);
		form.setAllowShip(false);
		form.setAllowCancel(false);
		// TODO should move the following logic into OrderHeaderDAO
		if (!orderHeader.getOrderStatus().equals(Constants.ORDERSTATUS_CANCELLED) && itemInvoiceTotal < itemOrderTotal) {
			form.setAllowInvoice(true);
		}
		if (!orderHeader.getOrderStatus().equals(Constants.ORDERSTATUS_CANCELLED) && itemShipTotal < itemOrderTotal) {
			form.setAllowShip(true);
		}
		if (OrderHeaderDAO.isAllowCancel(orderHeader)) {
			form.setAllowCancel(true);
		}
		
		if (invoiceHeader != null) {
			float creditTotal = 0;
			for (CreditHeader cHeader : invoiceHeader.getCreditHeaders()) {
				creditTotal += cHeader.getCreditTotal();
			}
			if (!orderHeader.getOrderStatus().equals(Constants.ORDERSTATUS_CANCELLED) && creditTotal < invoiceHeader.getInvoiceTotal()) {
				if (orderHeader.getPaymentGatewayProvider() == null || !orderHeader.getPaymentGatewayProvider().equals(PayPalEngine.class.getSimpleName())) {
					form.setAllowCredit(true);
				}
			}
		}

		Vector<OrderOtherDetailDisplayForm> orderOtherDetails = new Vector<OrderOtherDetailDisplayForm>();
		iterator = orderHeader.getOrderOtherDetails().iterator();
		float orderOtherDetailAmount = 0;
		while (iterator.hasNext()) {
			OrderOtherDetail orderOtherDetail = (OrderOtherDetail) iterator.next();
			OrderOtherDetailDisplayForm otherDisplay = new OrderOtherDetailDisplayForm();
			otherDisplay.setOrderOtherDetailId(orderOtherDetail.getOrderOtherDetailId().toString());
			otherDisplay.setOrderOtherDetailNum(orderOtherDetail.getOrderOtherDetailNum());
			otherDisplay.setOrderOtherDetailDesc(orderOtherDetail.getOrderOtherDetailDesc());
			otherDisplay.setOrderOtherDetailAmount(Format.getFloat(orderOtherDetail.getOrderOtherDetailAmount()));
			orderOtherDetailAmount += orderOtherDetail.getOrderOtherDetailAmount();
			orderOtherDetails.add(otherDisplay);
		}
		form.setOrderOtherDetails(orderOtherDetails);
		orderHeaderDisplayForm.setOrderOtherDetailAmount(Format.getFloat(orderOtherDetailAmount));
		
		iterator = orderEngine.getOrderTaxes().iterator();
		Vector<OrderTax> orderTaxes = new Vector<OrderTax>();
		while (iterator.hasNext()) {
			OrderDetailTax orderDetailTax = (OrderDetailTax) iterator.next();
			OrderTax orderTax = new OrderTax();
			orderTax.setTaxName(orderDetailTax.getTax().getTaxLanguage().getTaxName());
			orderTax.setTaxAmount(Format.getFloat(orderDetailTax.getTaxAmount()));
			orderTaxes.add(orderTax);
		}
		form.setOrderTaxes(orderTaxes);
		
		iterator = orderHeader.getOrderTrackings().iterator();
		Vector<OrderTrackingDisplayForm> orderTrackings = new Vector<OrderTrackingDisplayForm>();
		while (iterator.hasNext()) {
			OrderTracking orderTracking = (OrderTracking) iterator.next();
			if (orderTracking.getCreditHeader() != null) {
				continue;
			}
			if (orderTracking.getInvoiceHeader() != null) {
				continue;
			}
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
		orderHeaderDisplayForm.setOrderTrackings(orderTrackings);
		
		Vector<InvoiceHeaderDisplayForm> invoiceHeaders = new Vector<InvoiceHeaderDisplayForm>();
		iterator = orderHeader.getInvoiceHeaders().iterator();
		while (iterator.hasNext()) {
			InvoiceHeader iHeader = (InvoiceHeader) iterator.next();
			// invoiceHeader might have been updated. eg.capture
			if (invoiceHeader != null) {
				if (iHeader.getInvoiceHeaderId().equals(invoiceHeader.getInvoiceHeaderId())) {
					iHeader = invoiceHeader;
				}
			}
			InvoiceHeaderDisplayForm invoiceHeaderDisplayForm = new InvoiceHeaderDisplayForm();
			if (iHeader.getInvoiceHeaderId() != null) {
				invoiceHeaderDisplayForm.setInvoiceHeaderId(iHeader.getInvoiceHeaderId().toString());
			}
			invoiceHeaderDisplayForm.setInvoiceNum(iHeader.getInvoiceNum());
			invoiceHeaderDisplayForm.setShippingTotal(Format.getFloat(iHeader.getShippingTotal()));
			invoiceHeaderDisplayForm.setInvoiceTotal(Format.getFloat(iHeader.getInvoiceTotal()));
			invoiceHeaderDisplayForm.setInvoiceStatus(resources.getMessage("order.status." + iHeader.getInvoiceStatus()));
			invoiceHeaderDisplayForm.setInvoiceDate(Format.getFullDate(iHeader.getInvoiceDate()));
			
			Iterator<?> trackingIterator = iHeader.getInvoiceTrackings().iterator();
			Vector<OrderTrackingDisplayForm> invoiceTrackings = new Vector<OrderTrackingDisplayForm>();
			while (trackingIterator.hasNext()) {
				OrderTracking orderTracking = (OrderTracking) trackingIterator.next();
				OrderTrackingDisplayForm orderTrackingDisplayForm = new OrderTrackingDisplayForm();
				orderTrackingDisplayForm.setOrderTrackingId(orderTracking.getOrderTrackingId().toString());
				orderTrackingDisplayForm.setOrderTrackingMessage(orderTracking.getOrderTrackingMessage());
				orderTrackingDisplayForm.setOrderTrackingInternal(false);
				if (orderTracking.getOrderTrackingInternal().equals(String.valueOf(Constants.VALUE_YES))) {
					orderTrackingDisplayForm.setOrderTrackingInternal(true);
				}
				orderTrackingDisplayForm.setRecUpdateDatetime(Format.getFullDatetime(orderTracking.getRecUpdateDatetime()));
				invoiceTrackings.add(orderTrackingDisplayForm);
			}
			invoiceHeaderDisplayForm.setOrderTrackings(invoiceTrackings);

			
			if (iHeader.getPaymentTran() != null) {
				PaymentTran paymentTran = iHeader.getPaymentTran();
				PaymentTranDisplayForm paymentForm = new PaymentTranDisplayForm();
				paymentForm.setAuthCode(paymentTran.getAuthCode());
				paymentForm.setPaymentReference1(paymentTran.getPaymentReference1());
				paymentForm.setPaymentReference2(paymentTran.getPaymentReference2());
				paymentForm.setPaymentReference3(paymentTran.getPaymentReference3());
				paymentForm.setPaymentReference4(paymentTran.getPaymentReference4());
				paymentForm.setPaymentReference5(paymentTran.getPaymentReference5());
				invoiceHeaderDisplayForm.setPaymentTran(paymentForm);
			}
			if (iHeader.getVoidPaymentTran() != null) {
				PaymentTran paymentTran = iHeader.getVoidPaymentTran();
				PaymentTranDisplayForm paymentForm = new PaymentTranDisplayForm();
				paymentForm.setAuthCode(paymentTran.getAuthCode());
				paymentForm.setPaymentReference1(paymentTran.getPaymentReference1());
				paymentForm.setPaymentReference2(paymentTran.getPaymentReference2());
				paymentForm.setPaymentReference3(paymentTran.getPaymentReference3());
				paymentForm.setPaymentReference4(paymentTran.getPaymentReference4());
				paymentForm.setPaymentReference5(paymentTran.getPaymentReference5());
				invoiceHeaderDisplayForm.setVoidPaymentTran(paymentForm);
			}
			invoiceHeaders.add(invoiceHeaderDisplayForm);
		}
		form.setInvoiceHeaders(invoiceHeaders);
		
		Vector<CreditHeaderDisplayForm> creditHeaders = new Vector<CreditHeaderDisplayForm>();
		iterator = orderHeader.getCreditHeaders().iterator();
		while (iterator.hasNext()) {
			CreditHeader iHeader = (CreditHeader) iterator.next();
			// creditHeader might have been updated. eg.capture
			if (creditHeader != null) {
				if (iHeader.getCreditHeaderId().equals(creditHeader.getCreditHeaderId())) {
					iHeader = creditHeader;
				}
			}
			CreditHeaderDisplayForm creditHeaderDisplayForm = new CreditHeaderDisplayForm();
			if (iHeader.getCreditHeaderId() != null) {
				creditHeaderDisplayForm.setCreditHeaderId(iHeader.getCreditHeaderId().toString());
			}
			creditHeaderDisplayForm.setCreditNum(iHeader.getCreditNum());
			creditHeaderDisplayForm.setUpdateInventory(false);
			if (iHeader.getUpdateInventory().equals(String.valueOf(Constants.VALUE_YES))) {
				creditHeaderDisplayForm.setUpdateInventory(true);
			}
			creditHeaderDisplayForm.setShippingTotal(Format.getFloat(iHeader.getShippingTotal()));
			creditHeaderDisplayForm.setCreditTotal(Format.getFloat(iHeader.getCreditTotal()));
			creditHeaderDisplayForm.setCreditStatus(resources.getMessage("order.status." + iHeader.getCreditStatus()));
			creditHeaderDisplayForm.setCreditDate(Format.getFullDate(iHeader.getCreditDate()));
			
			Iterator<?> trackingIterator = iHeader.getCreditTrackings().iterator();
			Vector<OrderTrackingDisplayForm> creditTrackings = new Vector<OrderTrackingDisplayForm>();
			while (trackingIterator.hasNext()) {
				OrderTracking orderTracking = (OrderTracking) trackingIterator.next();
				OrderTrackingDisplayForm orderTrackingDisplayForm = new OrderTrackingDisplayForm();
				orderTrackingDisplayForm.setOrderTrackingId(orderTracking.getOrderTrackingId().toString());
				orderTrackingDisplayForm.setOrderTrackingMessage(orderTracking.getOrderTrackingMessage());
				orderTrackingDisplayForm.setOrderTrackingInternal(false);
				if (orderTracking.getOrderTrackingInternal().equals(String.valueOf(Constants.VALUE_YES))) {
					orderTrackingDisplayForm.setOrderTrackingInternal(true);
				}
				orderTrackingDisplayForm.setRecUpdateDatetime(Format.getFullDatetime(orderTracking.getRecUpdateDatetime()));
				creditTrackings.add(orderTrackingDisplayForm);
			}
			creditHeaderDisplayForm.setOrderTrackings(creditTrackings);

			
			if (iHeader.getPaymentTran() != null) {
				PaymentTran paymentTran = iHeader.getPaymentTran();
				PaymentTranDisplayForm paymentForm = new PaymentTranDisplayForm();
				paymentForm.setAuthCode(paymentTran.getAuthCode());
				paymentForm.setPaymentReference1(paymentTran.getPaymentReference1());
				paymentForm.setPaymentReference2(paymentTran.getPaymentReference2());
				paymentForm.setPaymentReference3(paymentTran.getPaymentReference3());
				paymentForm.setPaymentReference4(paymentTran.getPaymentReference4());
				paymentForm.setPaymentReference5(paymentTran.getPaymentReference5());
				creditHeaderDisplayForm.setPaymentTran(paymentForm);
			}
			if (iHeader.getVoidPaymentTran() != null) {
				PaymentTran paymentTran = iHeader.getVoidPaymentTran();
				PaymentTranDisplayForm paymentForm = new PaymentTranDisplayForm();
				paymentForm.setAuthCode(paymentTran.getAuthCode());
				paymentForm.setPaymentReference1(paymentTran.getPaymentReference1());
				paymentForm.setPaymentReference2(paymentTran.getPaymentReference2());
				paymentForm.setPaymentReference3(paymentTran.getPaymentReference3());
				paymentForm.setPaymentReference4(paymentTran.getPaymentReference4());
				paymentForm.setPaymentReference5(paymentTran.getPaymentReference5());
				creditHeaderDisplayForm.setVoidPaymentTran(paymentForm);
			}
			creditHeaders.add(creditHeaderDisplayForm);
		}
		form.setCreditHeaders(creditHeaders);
		
		Vector<ShipHeaderDisplayForm> shipHeaders = new Vector<ShipHeaderDisplayForm>();
		iterator = orderHeader.getShipHeaders().iterator();
		while (iterator.hasNext()) {
			ShipHeader iHeader = (ShipHeader) iterator.next();
			// shipHeader might have been updated. eg.capture
			if (shipHeader != null) {
				if (iHeader.getShipHeaderId().equals(shipHeader.getShipHeaderId())) {
					iHeader = shipHeader;
				}
			}
			ShipHeaderDisplayForm shipHeaderDisplayForm = new ShipHeaderDisplayForm();
			if (iHeader.getShipHeaderId() != null) {
				shipHeaderDisplayForm.setShipHeaderId(iHeader.getShipHeaderId().toString());
			}
			shipHeaderDisplayForm.setShipNum(iHeader.getShipNum());
			shipHeaderDisplayForm.setUpdateInventory(false);
			if (iHeader.getUpdateInventory().equals(String.valueOf(Constants.VALUE_YES))) {
				shipHeaderDisplayForm.setUpdateInventory(true);
			}
			shipHeaderDisplayForm.setShipStatus(resources.getMessage("order.status." + iHeader.getShipStatus()));
			shipHeaderDisplayForm.setShipDate(Format.getFullDate(iHeader.getShipDate()));
			
			Iterator<?> trackingIterator = iHeader.getShipTrackings().iterator();
			Vector<OrderTrackingDisplayForm> shipTrackings = new Vector<OrderTrackingDisplayForm>();
			while (trackingIterator.hasNext()) {
				OrderTracking orderTracking = (OrderTracking) trackingIterator.next();
				OrderTrackingDisplayForm orderTrackingDisplayForm = new OrderTrackingDisplayForm();
				orderTrackingDisplayForm.setOrderTrackingId(orderTracking.getOrderTrackingId().toString());
				orderTrackingDisplayForm.setOrderTrackingMessage(orderTracking.getOrderTrackingMessage());
				orderTrackingDisplayForm.setOrderTrackingInternal(false);
				if (orderTracking.getOrderTrackingInternal().equals(String.valueOf(Constants.VALUE_YES))) {
					orderTrackingDisplayForm.setOrderTrackingInternal(true);
				}
				orderTrackingDisplayForm.setRecUpdateDatetime(Format.getFullDatetime(orderTracking.getRecUpdateDatetime()));
				shipTrackings.add(orderTrackingDisplayForm);
			}
			shipHeaderDisplayForm.setOrderTrackings(shipTrackings);
			shipHeaders.add(shipHeaderDisplayForm);
		}
		form.setShipHeaders(shipHeaders);
	}
	
    public Vector<OrderItemAttributeBean> getOrderItemAttributes(OrderItemDetail orderItemDetail) throws NumberFormatException, SecurityException, Exception {
		Vector<OrderItemAttributeBean> orderItemAttributes = new Vector<OrderItemAttributeBean>();
		Item item = orderItemDetail.getItem();
		if (item != null && item.getItemTypeCd().equals(Constants.ITEM_TYPE_SKU)) {
			for (ItemAttributeDetail itemAttributeDetail : item.getItemAttributeDetails()) {
				CustomAttribute customAttribute = itemAttributeDetail.getCustomAttributeDetail().getCustomAttribute();
				if (customAttribute.getCustomAttribTypeCode() != Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP) {
					continue;
				}
    			OrderItemAttributeBean orderItemAttributeBean = new OrderItemAttributeBean();
    			orderItemAttributeBean.setCustomAttribDesc(customAttribute.getCustomAttributeLanguage().getCustomAttribDesc());
        		CustomAttributeOption customAttributeOption = itemAttributeDetail.getCustomAttributeOption();
        		orderItemAttributeBean.setCustomAttribValue(customAttributeOption.getCustomAttributeOptionLanguage().getCustomAttribValue());
        		orderItemAttributes.add(orderItemAttributeBean);
			}
		}
		for (OrderAttributeDetail orderAttributeDetail : orderItemDetail.getOrderAttributeDetails()) {
			OrderItemAttributeBean orderItemAttributeBean = new OrderItemAttributeBean();
			CustomAttribute customAttribute = orderAttributeDetail.getCustomAttributeDetail().getCustomAttribute();
			orderItemAttributeBean.setCustomAttribDesc(customAttribute.getCustomAttributeLanguage().getCustomAttribDesc());
    		
    		if (customAttribute.getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_CUST_INPUT) {
    			orderItemAttributeBean.setCustomAttribValue(orderAttributeDetail.getOrderAttribValue());
    		}
    		else {
        		CustomAttributeOption customAttributeOption = orderAttributeDetail.getCustomAttributeOption();
        		orderItemAttributeBean.setCustomAttribValue(customAttributeOption.getCustomAttributeOptionLanguage().getCustomAttribValue());
    		}
    		orderItemAttributes.add(orderItemAttributeBean);
		}
    	return orderItemAttributes;
    }
}
