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

package com.jada.myaccount.order;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.jada.admin.inventory.OrderItemAttributeBean;
import com.jada.content.ContentBean;
import com.jada.content.Formatter;
import com.jada.dao.OrderHeaderDAO;
import com.jada.jpa.entity.Coupon;
import com.jada.jpa.entity.CouponLanguage;
import com.jada.jpa.entity.CreditDetail;
import com.jada.jpa.entity.CreditHeader;
import com.jada.jpa.entity.CustomAttribute;
import com.jada.jpa.entity.CustomAttributeLanguage;
import com.jada.jpa.entity.CustomAttributeOption;
import com.jada.jpa.entity.CustomAttributeOptionLanguage;
import com.jada.jpa.entity.InvoiceDetail;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.ItemAttributeDetail;
import com.jada.jpa.entity.ItemImage;
import com.jada.jpa.entity.ItemLanguage;
import com.jada.jpa.entity.OrderAddress;
import com.jada.jpa.entity.OrderAttributeDetail;
import com.jada.jpa.entity.OrderDetailTax;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.OrderItemDetail;
import com.jada.jpa.entity.OrderOtherDetail;
import com.jada.jpa.entity.OrderTracking;
import com.jada.jpa.entity.PaymentTran;
import com.jada.jpa.entity.ShipHeader;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteCurrency;
import com.jada.jpa.entity.SiteProfile;
import com.jada.myaccount.portal.MyAccountPortalAction;
import com.jada.order.document.OrderEngine;
import com.jada.order.payment.gateway.PayPalEngine;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

public class MyAccountOrderAction
    extends MyAccountPortalAction {
	
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        MyAccountOrderStatusActionForm form = (MyAccountOrderStatusActionForm) actionForm;
        Site site = getContentBean(request).getContentSessionBean().getSiteDomain().getSite();
		String orderHeaderId = request.getParameter("orderHeaderId");
		OrderHeader header = new OrderHeader();
		header = OrderHeaderDAO.load(site.getSiteId(), Format.getLong(orderHeaderId));
		OrderEngine orderEngine = new OrderEngine(header, null);
		SiteProfile siteProfile = getContentBean(request).getContentSessionBean().getSiteProfile();
		SiteCurrency siteCurrency = header.getSiteCurrency();
		Formatter numberFormatter = new Formatter(siteProfile, siteCurrency);
		ContentBean contentBean = getContentBean(request);

		MessageResources resources = this.getResources(request);
		form.setOrderNum(header.getOrderNum());
		
		OrderAddress address = header.getCustAddress();
		String name = Format.formatCustomerName("", address.getCustFirstName(), address.getCustMiddleName(), address.getCustLastName(), "");
		form.setCustName(name);
		form.setCustEmail(header.getCustEmail());
		form.setCustPrefix(address.getCustPrefix());
		form.setCustFirstName(address.getCustFirstName());
		form.setCustMiddleName(address.getCustMiddleName());
		form.setCustLastName(address.getCustLastName());
		form.setCustSuffix(address.getCustSuffix());
		form.setCustAddressLine1(address.getCustAddressLine1());
		form.setCustAddressLine2(address.getCustAddressLine2());
		form.setCustCityName(address.getCustCityName());
		form.setCustStateName(address.getCustStateName());
		form.setCustStateCode(address.getCustStateCode());
		form.setCustCountryName(address.getCustCountryName());
		form.setCustCountryCode(address.getCustCountryCode());
		form.setCustZipCode(address.getCustZipCode());
		form.setCustPhoneNum(address.getCustPhoneNum());
		form.setCustFaxNum(address.getCustFaxNum());

		address = header.getBillingAddress();
		form.setBillingCustUseAddress(address.getCustUseAddress());
		if (address.getCustUseAddress().equals(Constants.CUST_ADDRESS_USE_OWN)) {
			name = Format.formatCustomerName("", address.getCustFirstName(), address.getCustMiddleName(), address.getCustLastName(), "");
			form.setBillingCustName(name);
			form.setBillingCustPrefix(address.getCustPrefix());
			form.setBillingCustFirstName(address.getCustFirstName());
			form.setBillingCustMiddleName(address.getCustMiddleName());
			form.setBillingCustLastName(address.getCustLastName());
			form.setBillingCustSuffix(address.getCustSuffix());
			form.setBillingCustAddressLine1(address.getCustAddressLine1());
			form.setBillingCustAddressLine2(address.getCustAddressLine2());
			form.setBillingCustCityName(address.getCustCityName());
			form.setBillingCustStateName(address.getCustStateName());
			form.setBillingCustStateCode(address.getCustStateCode());
			form.setBillingCustCountryName(address.getCustCountryName());
			form.setBillingCustCountryCode(address.getCustCountryCode());
			form.setBillingCustZipCode(address.getCustZipCode());
			form.setBillingCustPhoneNum(address.getCustPhoneNum());
			form.setBillingCustFaxNum(address.getCustFaxNum());
		}
		
		address = header.getShippingAddress();
		form.setShippingCustUseAddress(address.getCustUseAddress());
		if (address.getCustUseAddress().equals(Constants.CUST_ADDRESS_USE_OWN)) {
			name = Format.formatCustomerName("", address.getCustFirstName(), address.getCustMiddleName(), address.getCustLastName(), "");
			form.setShippingCustName(name);
			form.setShippingCustPrefix(address.getCustPrefix());
			form.setShippingCustFirstName(address.getCustFirstName());
			form.setShippingCustMiddleName(address.getCustMiddleName());
			form.setShippingCustLastName(address.getCustLastName());
			form.setShippingCustSuffix(address.getCustSuffix());
			form.setShippingCustAddressLine1(address.getCustAddressLine1());
			form.setShippingCustAddressLine2(address.getCustAddressLine2());
			form.setShippingCustCityName(address.getCustCityName());
			form.setShippingCustStateName(address.getCustStateName());
			form.setShippingCustStateCode(address.getCustStateCode());
			form.setShippingCustCountryName(address.getCustCountryName());
			form.setShippingCustCountryCode(address.getCustCountryCode());
			form.setShippingCustZipCode(address.getCustZipCode());
			form.setShippingCustPhoneNum(address.getCustPhoneNum());
			form.setShippingCustFaxNum(address.getCustFaxNum());
		}
		
		form.setPriceTotal(numberFormatter.formatCurrency(orderEngine.getOrderSubTotal()));
		form.setShippingTotal(numberFormatter.formatCurrency(orderEngine.getOrderShippingTotal()));
		form.setTaxTotal(numberFormatter.formatCurrency(orderEngine.getOrderTaxTotal()));
		form.setOrderTotal(numberFormatter.formatCurrency(header.getOrderTotal()));
		if (header.getPaymentGatewayProvider() != null) {
			form.setPaymentGateway(header.getPaymentGatewayProvider());
			if (header.getPaymentGatewayProvider().equals(PayPalEngine.class.getSimpleName())) {
				form.setPayPal(true);
			}
		}
		
		form.setCreditCardDesc(header.getCreditCardDesc());
		if (header.getCustCreditCardNum() != null) {
			String custCreditCardNum = AESEncoder.getInstance().decode(header.getCustCreditCardNum());
			custCreditCardNum = Utility.maskCreditCardNumber(custCreditCardNum);
			form.setCustCreditCardNum(custCreditCardNum);
		}
		form.setShippingMethodName(header.getShippingMethodName());
		
		Date shipDate = null;
		for (ShipHeader shipHeader : header.getShipHeaders()) {
			if (shipDate == null || shipDate.before(shipHeader.getShipDate())) {
				shipDate = shipHeader.getShipDate();
			}
		}
		if (shipDate != null) {
			form.setShipDate(numberFormatter.formatDate(shipDate));
		}
		form.setOrderStatus(header.getOrderStatus());
		form.setOrderStatusDesc(resources.getMessage("order.status." + header.getOrderStatus()));
		form.setOrderDatetime(numberFormatter.formatDate(header.getOrderDate()));
		
		PaymentTran paymentTran = header.getPaymentTran();
		if (paymentTran != null) {
			form.setAuthCode(paymentTran.getAuthCode());
			form.setPaymentReference1(paymentTran.getPaymentReference1());
		}
		
		Iterator<?> iterator = orderEngine.getOrderTaxes().iterator();
		Vector<OrderTaxForm> orderTaxes = new Vector<OrderTaxForm>();
		while (iterator.hasNext()) {
			OrderDetailTax orderDetailTax = (OrderDetailTax) iterator.next();
			OrderTaxForm orderTaxForm = new OrderTaxForm();
			orderTaxForm.setTaxName(orderDetailTax.getTaxName());
			orderTaxForm.setTaxAmount(numberFormatter.formatCurrency(orderDetailTax.getTaxAmount()));
			orderTaxes.add(orderTaxForm);
		}
		form.setOrderTaxes(orderTaxes);
		
		
		iterator = header.getOrderItemDetails().iterator();
		Vector<OrderDetailDisplayForm> details = new Vector<OrderDetailDisplayForm>();
		while (iterator.hasNext()) {
			OrderItemDetail detail = (OrderItemDetail) iterator.next();
			OrderDetailDisplayForm dform = new OrderDetailDisplayForm();
			dform.setItemShortDesc(detail.getItemShortDesc());
			
			Item item = detail.getItem();
			if (item != null) {
				Item master = detail.getItem();
				if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_SKU)) {
					master = item.getItemSkuParent();
				}
					
				ItemImage itemImage = master.getItemLanguage().getImage();
				if (itemImage != null) {
					dform.setImageId(itemImage.getImageId().toString());
				}
				dform.setItemShortDesc(master.getItemLanguage().getItemShortDesc());
				if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
					for (ItemLanguage itemLanguage : master.getItemLanguages()) {
						if (itemLanguage.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
							if (itemLanguage.getImage() != null) {
								itemImage = itemLanguage.getImage();
							}
							if (itemLanguage.getItemShortDesc() != null) {
								dform.setItemShortDesc(itemLanguage.getItemShortDesc());
							}
							break;
						}
					}
				}
			}
			
			dform.setItemNum(detail.getItemNum());
			dform.setItemUpcCd(detail.getItemUpcCd());
			dform.setItemTierQty(numberFormatter.formatNumber(detail.getItemTierQty()));
			dform.setItemTierPrice(numberFormatter.formatCurrency(detail.getItemTierPrice()));

			String priceDisplay = numberFormatter.formatCurrency(detail.getItemTierPrice());
			if (detail.getItemTierQty() > 1) {
				priceDisplay = numberFormatter.formatNumber(detail.getItemTierQty()) + " / " + numberFormatter.formatCurrency(detail.getItemTierPrice());
			}
			dform.setPriceDisplay(priceDisplay);
			
			dform.setItemOrderQty(numberFormatter.formatNumber(detail.getItemOrderQty()));
			dform.setItemSubTotal(numberFormatter.formatCurrency(detail.getItemDetailAmount()));
			
			int itemInvoiceQty = 0;
			for (InvoiceDetail invoiceDetail : detail.getInvoiceDetails()) {
				itemInvoiceQty += invoiceDetail.getItemInvoiceQty();
			}
			dform.setItemInvoiceQty(numberFormatter.formatNumber(itemInvoiceQty));
			int itemCreditQty = 0;
			for (CreditDetail creditDetail : detail.getCreditDetails()) {
				itemCreditQty += creditDetail.getItemCreditQty();
			}
			dform.setItemCreditQty(numberFormatter.formatNumber(itemCreditQty));
			
			Vector<OrderItemAttributeBean> orderItemAttributes = new Vector<OrderItemAttributeBean>();
    		if (item != null && item.getItemTypeCd().equals(Constants.ITEM_TYPE_SKU)) {
    			for (ItemAttributeDetail itemAttributeDetail : item.getItemAttributeDetails()) {
    				CustomAttribute customAttribute = itemAttributeDetail.getCustomAttributeDetail().getCustomAttribute();
    				if (customAttribute.getCustomAttribTypeCode() != Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP) {
    					continue;
    				}
	    			OrderItemAttributeBean orderItemAttributeBean = new OrderItemAttributeBean();
	    			
	    			orderItemAttributeBean.setCustomAttribDesc(customAttribute.getCustomAttributeLanguage().getCustomAttribDesc());
	        		if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
	        			for (CustomAttributeLanguage language : customAttribute.getCustomAttributeLanguages()) {
	        				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
	    	    				if (language.getCustomAttribDesc() != null) {
	    	    					orderItemAttributeBean.setCustomAttribDesc(language.getCustomAttribDesc());
	    	    				}
	        					break;
	        				}
	        			}
	        		}

	        		CustomAttributeOption customAttributeOption = itemAttributeDetail.getCustomAttributeOption();
	        		orderItemAttributeBean.setCustomAttribValue(customAttributeOption.getCustomAttributeOptionLanguage().getCustomAttribValue());
	        		if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
	        			for (CustomAttributeOptionLanguage language : customAttributeOption.getCustomAttributeOptionLanguages()) {
	        				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
	    	    				if (language.getCustomAttribValue() != null) {
	    	    					orderItemAttributeBean.setCustomAttribValue(language.getCustomAttribValue());
	    	    				}
	        					break;
	        				}
	        			}
	        		}
	        		orderItemAttributes.add(orderItemAttributeBean);
    			}
    		}
    		
    		for (OrderAttributeDetail orderAttributeDetail : detail.getOrderAttributeDetails()) {
    			OrderItemAttributeBean orderItemAttributeBean = new OrderItemAttributeBean();
    			CustomAttribute customAttribute = orderAttributeDetail.getCustomAttributeDetail().getCustomAttribute();
    			orderItemAttributeBean.setCustomAttribDesc(customAttribute.getCustomAttributeLanguage().getCustomAttribDesc());
        		if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
        			for (CustomAttributeLanguage language : customAttribute.getCustomAttributeLanguages()) {
        				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
    	    				if (language.getCustomAttribDesc() != null) {
    	    					orderItemAttributeBean.setCustomAttribDesc(language.getCustomAttribDesc());
    	    				}
        					break;
        				}
        			}
        		}
        		
        		if (customAttribute.getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_CUST_INPUT) {
        			orderItemAttributeBean.setCustomAttribValue(orderAttributeDetail.getOrderAttribValue());
        		}
        		else {
	        		CustomAttributeOption customAttributeOption = orderAttributeDetail.getCustomAttributeOption();
	        		orderItemAttributeBean.setCustomAttribValue(customAttributeOption.getCustomAttributeOptionLanguage().getCustomAttribValue());
	        		if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
	        			for (CustomAttributeOptionLanguage language : customAttributeOption.getCustomAttributeOptionLanguages()) {
	        				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
	    	    				if (language.getCustomAttribValue() != null) {
	    	    					orderItemAttributeBean.setCustomAttribValue(language.getCustomAttribValue());
	    	    				}
	        					break;
	        				}
	        			}
	        		}
        		}
        		orderItemAttributes.add(orderItemAttributeBean);
    		}
    		dform.setOrderItemAttributes(orderItemAttributes);
			
			details.add(dform);
		}
		form.setOrderDetails(details);
		
    	Vector<OrderDetailDisplayForm> otherVector = new Vector<OrderDetailDisplayForm>();
    	iterator = header.getOrderOtherDetails().iterator();
     	while (iterator.hasNext()) {
    		OrderOtherDetail orderOtherDetail = (OrderOtherDetail) iterator.next();
    		Coupon coupon = orderOtherDetail.getCoupon();
    		OrderDetailDisplayForm bean = new OrderDetailDisplayForm();
    		bean.setItemShortDesc(coupon.getCouponCode() + " " + coupon.getCouponLanguage().getCouponName());
    		if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
    			for (CouponLanguage language : coupon.getCouponLanguages()) {
    				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
    					if (language.getCouponName() != null) {
    						bean.setItemShortDesc(coupon.getCouponCode() + " " + language.getCouponName());
    					}
    					break;
    				}
    			}
    		}
    		bean.setItemSubTotal(Format.getFloat(orderOtherDetail.getOrderOtherDetailAmount()));
    		otherVector.add(bean);
    	}
    	form.setOrderOtherDetails(otherVector);
		
		Vector<TrackingDisplayForm> vector = new Vector<TrackingDisplayForm>();
		iterator = header.getOrderTrackings().iterator();
		while (iterator.hasNext()) {
			OrderTracking orderTracking = (OrderTracking) iterator.next();
			TrackingDisplayForm displayForm = new TrackingDisplayForm();
			displayForm.setOrderTrackingId(Format.getLong(orderTracking.getOrderTrackingId()));
			displayForm.setOrderTrackingCode(resources.getMessage("order.tracking." + orderTracking.getOrderTrackingCode()));
			displayForm.setOrderTrackingMessage(orderTracking.getOrderTrackingMessage());
			displayForm.setOrderTrackingDatetime(numberFormatter.formatDate(orderTracking.getRecUpdateDatetime()));
			displayForm.setOrderTrackingInternal(orderTracking.getOrderTrackingInternal());
			vector.add(displayForm);
		}
		TrackingDisplayForm orderTrackings[] = new TrackingDisplayForm[vector.size()];
		vector.copyInto(orderTrackings);
		form.setOrderTrackings(orderTrackings);
		
		Vector<CreditDetailDisplayForm> creditVector = new Vector<CreditDetailDisplayForm>();
		iterator = header.getCreditHeaders().iterator();
		while (iterator.hasNext()) {
			CreditHeader creditHeader = (CreditHeader) iterator.next();
			CreditDetailDisplayForm creditDisplayForm = new CreditDetailDisplayForm();
			creditDisplayForm.setCreditNum(creditHeader.getCreditNum());
			creditDisplayForm.setCreditTotal(numberFormatter.formatCurrency(creditHeader.getCreditTotal()));
			creditDisplayForm.setCreditStatusDesc(resources.getMessage("order.status." + creditHeader.getCreditStatus()));
			creditDisplayForm.setCreditDatetime(numberFormatter.formatDate(creditHeader.getCreditDate()));
			creditVector.add(creditDisplayForm);
		}
		form.setCreditDetails(creditVector);
		
		createEmptySecureTemplateInfo(request);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("list", "list");
        map.put("start", "start");
        return map;
    }
}