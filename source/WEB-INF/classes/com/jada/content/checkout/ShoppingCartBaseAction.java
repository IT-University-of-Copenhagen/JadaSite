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

package com.jada.content.checkout;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.content.ContentBean;
import com.jada.content.ContentLookupDispatchAction;
import com.jada.content.Formatter;
import com.jada.content.data.ContentApi;
import com.jada.content.data.ItemInfo;
import com.jada.content.frontend.FrontendBaseAction;
import com.jada.dao.CountryDAO;
import com.jada.dao.CustomAttributeOptionDAO;
import com.jada.dao.CustomerCreditCardDAO;
import com.jada.dao.SiteDAO;
import com.jada.dao.SiteDomainDAO;
import com.jada.dao.StateDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.Coupon;
import com.jada.jpa.entity.CouponLanguage;
import com.jada.jpa.entity.CreditCard;
import com.jada.jpa.entity.CustomAttribute;
import com.jada.jpa.entity.CustomAttributeLanguage;
import com.jada.jpa.entity.CustomAttributeOption;
import com.jada.jpa.entity.CustomAttributeOptionLanguage;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.CustomerAddress;
import com.jada.jpa.entity.CustomerCreditCard;
import com.jada.jpa.entity.InvoiceHeader;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.ItemAttributeDetail;
import com.jada.jpa.entity.ItemImage;
import com.jada.jpa.entity.ItemLanguage;
import com.jada.jpa.entity.OrderAddress;
import com.jada.jpa.entity.OrderAttributeDetail;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.OrderItemDetail;
import com.jada.jpa.entity.OrderOtherDetail;
import com.jada.jpa.entity.PaymentGateway;
import com.jada.jpa.entity.PaymentTran;
import com.jada.jpa.entity.ShippingMethod;
import com.jada.jpa.entity.ShippingMethodLanguage;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomainLanguage;
import com.jada.jpa.entity.State;
import com.jada.jpa.entity.Tax;
import com.jada.jpa.entity.TaxLanguage;
import com.jada.order.cart.ItemAttributeInfo;
import com.jada.order.cart.ItemTax;
import com.jada.order.cart.ShoppingCart;
import com.jada.order.cart.ShoppingCartCoupon;
import com.jada.order.cart.ShoppingCartItem;
import com.jada.order.document.InvoiceEngine;
import com.jada.order.document.OrderEngine;
import com.jada.order.payment.AuthorizationException;
import com.jada.order.payment.PaymentEngine;
import com.jada.order.payment.PaymentException;
import com.jada.order.payment.gateway.PayPalEngine;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.JSONEscapeObject;
import com.jada.util.Utility;
import com.jada.xml.site.SiteDomainParamBean;

public class ShoppingCartBaseAction extends FrontendBaseAction {
	Logger logger = Logger.getLogger(ShoppingCartBaseAction.class);
	
	protected void init(HttpServletRequest request) throws Exception {
	}

	protected JSONEscapeObject getJsonCartInfo(Site site, ShoppingCart shoppingCart, HttpServletRequest request) throws Exception {
    	ContentBean contentBean = getContentBean(request);
		Formatter formatter = new Formatter(contentBean.getContentSessionBean().getSiteProfile(), contentBean.getContentSessionBean().getSiteCurrency());
		JSONEscapeObject jsonObject = new JSONEscapeObject();
		jsonObject.put("cashPaymentOrder", shoppingCart.isCashPaymentOrder());
		jsonObject.put("creditCardOrder", shoppingCart.isCreditCardOrder());
		jsonObject.put("payPalWebsitePaymentProHostedOrder", shoppingCart.isPayPalWebsitePaymentProHostedOrder());
		jsonObject.put("creditCard", shoppingCart.isCreditCard());
		jsonObject.put("payPal", shoppingCart.isPayPal());
		
		Vector<?> shippingMethods = shoppingCart.getShippingMethods();
		Vector<JSONEscapeObject> jsonShippingMethods = new Vector<JSONEscapeObject>();
		Iterator<?> iterator = shippingMethods.iterator();
		while (iterator.hasNext()) {
			ShippingMethod shippingMethod = (ShippingMethod) iterator.next();
			JSONEscapeObject jsonShippingMethod = new JSONEscapeObject();
			jsonShippingMethod.put("shippingMethodId", shippingMethod.getShippingMethodId());
			String shippingMethodName = shippingMethod.getShippingMethodLanguage().getShippingMethodName();
            if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
    			for (ShippingMethodLanguage language : shippingMethod.getShippingMethodLanguages()) {
    				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
    					if (language.getShippingMethodName() != null) {
    						shippingMethodName = language.getShippingMethodName();
     					}
    					break;
    				}
    			}
            }
            jsonShippingMethod.put("shippingMethodName", shippingMethodName);
            jsonShippingMethods.add(jsonShippingMethod);
		}
		jsonObject.put("shippingMethods", jsonShippingMethods);
		
		jsonObject.put("priceTotal", formatter.formatCurrency(shoppingCart.getPriceTotal()));
		Vector<JSONEscapeObject> taxes = new Vector<JSONEscapeObject>();
		for (ItemTax tax : shoppingCart.getTaxes()) {
			String taxName = tax.getTax().getTaxLanguage().getTaxName();
			if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
				for (TaxLanguage language: tax.getTax().getTaxLanguages()) {
					if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
						taxName = language.getTaxName();
						break;
					}
				}
			}
			JSONEscapeObject jsonTax = new JSONEscapeObject();
			jsonTax.put("taxName", taxName);
			jsonTax.put("taxAmount", formatter.formatCurrency(tax.getTaxAmount()));
			taxes.add(jsonTax);
		}
		jsonObject.put("taxes", taxes);
		jsonObject.put("shippingTotal", formatter.formatCurrency(shoppingCart.getShippingTotal()));
		jsonObject.put("orderTotal", formatter.formatCurrency(shoppingCart.getOrderTotal()));
		return jsonObject;
	}

	protected JSONEscapeObject getJsonAddressInfo(Site site, ShoppingCart shoppingCart, HttpServletRequest request) throws Exception {
		JSONEscapeObject jsonObject = new JSONEscapeObject();
		Customer customer = shoppingCart.getCustomer();
		jsonObject.put("passwordEmpty", false);
		if (Format.isNullOrEmpty(customer.getCustPassword())) {
			jsonObject.put("passwordEmpty", true);
		}
		
		CustomerAddress billingAddress = shoppingCart.getBillingAddress();
		CustomerAddress shippingAddress = shoppingCart.getShippingAddress();

		jsonObject.put("custId", Format.getLong(customer.getCustId()));
		jsonObject.put("newUser", false);
		if (request.getParameter("new") != null  && request.getParameter("new").equals(String.valueOf(Constants.VALUE_YES))) {
			jsonObject.put("newUser", true);
		}
		jsonObject.put("custEmail", customer.getCustEmail());
		
		CustomerAddress custAddress = shoppingCart.getCustAddress();
		if (custAddress != null) {
			jsonObject.put("custFirstName", custAddress.getCustFirstName());
			jsonObject.put("custMiddleName", custAddress.getCustMiddleName());
			jsonObject.put("custLastName", custAddress.getCustLastName());
			jsonObject.put("custAddressLine1", custAddress.getCustAddressLine1());
			jsonObject.put("custAddressLine2", custAddress.getCustAddressLine2());
			jsonObject.put("custCityName", custAddress.getCustCityName());
			jsonObject.put("custStateCode", custAddress.getCustStateCode());
			jsonObject.put("custStateName", custAddress.getCustStateName());
			jsonObject.put("custCountryCode", custAddress.getCustCountryCode());
			jsonObject.put("custCountryName", custAddress.getCustCountryName());
			jsonObject.put("custZipCode", custAddress.getCustZipCode());
			jsonObject.put("custPhoneNum", custAddress.getCustPhoneNum());
			jsonObject.put("custFaxNum", custAddress.getCustFaxNum());
			if (!Format.isNullOrEmpty(custAddress.getCustStateCode())) {
				jsonObject.put("custStateName", Utility.getStateName(site.getSiteId(), custAddress.getCustStateCode()));
			}
			else {
				jsonObject.put("custStateName", custAddress.getCustStateName());
			}
			if (!Format.isNullOrEmpty(custAddress.getCustCountryCode())) {
				jsonObject.put("custCountryName", Utility.getCountryName(site.getSiteId(), custAddress.getCustCountryCode()));
			}
		}

		if (billingAddress != null) {
			jsonObject.put("billingCustAddressId", Format.getLong(billingAddress.getCustAddressId()));
			jsonObject.put("billingCustFirstName", billingAddress.getCustFirstName());
			jsonObject.put("billingCustMiddleName", billingAddress.getCustMiddleName());
			jsonObject.put("billingCustLastName", billingAddress.getCustLastName());
			jsonObject.put("billingCustAddressLine1", billingAddress.getCustAddressLine1());
			jsonObject.put("billingCustAddressLine2", billingAddress.getCustAddressLine2());
			jsonObject.put("billingCustCityName", billingAddress.getCustCityName());
			jsonObject.put("billingCustStateCode", billingAddress.getCustStateCode());
			jsonObject.put("billingCustStateName", billingAddress.getCustStateName());
			jsonObject.put("billingCustCountryCode", billingAddress.getCustCountryCode());
			jsonObject.put("billingCustCountryName", billingAddress.getCustCountryName());
			jsonObject.put("billingCustZipCode", billingAddress.getCustZipCode());
			jsonObject.put("billingCustPhoneNum", billingAddress.getCustPhoneNum());
			jsonObject.put("billingCustFaxNum", billingAddress.getCustFaxNum());
			if (!Format.isNullOrEmpty(billingAddress.getCustStateCode())) {
				jsonObject.put("billingCustStateName", Utility.getStateName(site.getSiteId(), billingAddress.getCustStateCode()));
			}
			else {
				jsonObject.put("bilingCustStateName", billingAddress.getCustStateName());
			}
			if (!Format.isNullOrEmpty(billingAddress.getCustCountryCode())) {
				jsonObject.put("billingCustCountryName", Utility.getCountryName(site.getSiteId(), billingAddress.getCustCountryCode()));
			}
		}
		else {
			jsonObject.put("billingUseAddress", Constants.CUST_ADDRESS_USE_CUST);
		}
		if (shippingAddress != null) {
			jsonObject.put("shippingCustAddressId", Format.getLong(shippingAddress.getCustAddressId()));
			jsonObject.put("shippingCustFirstName", shippingAddress.getCustFirstName());
			jsonObject.put("shippingCustMiddleName", shippingAddress.getCustMiddleName());
			jsonObject.put("shippingCustLastName", shippingAddress.getCustLastName());
			jsonObject.put("shippingCustAddressLine1", shippingAddress.getCustAddressLine1());
			jsonObject.put("shippingCustAddressLine2", shippingAddress.getCustAddressLine2());
			jsonObject.put("shippingCustCityName", shippingAddress.getCustCityName());
			jsonObject.put("shippingCustStateCode", shippingAddress.getCustStateCode());
			jsonObject.put("shippingCustStateName", shippingAddress.getCustStateName());
			jsonObject.put("shippingCustCountryCode", shippingAddress.getCustCountryCode());
			jsonObject.put("shippingCustCountryName", shippingAddress.getCustCountryName());
			jsonObject.put("shippingCustZipCode", shippingAddress.getCustZipCode());
			jsonObject.put("shippingCustPhoneNum", shippingAddress.getCustPhoneNum());
			jsonObject.put("shippingCustFaxNum", shippingAddress.getCustFaxNum());
			if (!Format.isNullOrEmpty(shippingAddress.getCustStateCode())) {
				jsonObject.put("shippingCustStateName", Utility.getStateName(site.getSiteId(), shippingAddress.getCustStateCode()));
			}
			else {
				jsonObject.put("shippingCustStateName", shippingAddress.getCustStateName());
			}
			if (!Format.isNullOrEmpty(shippingAddress.getCustCountryCode())) {
				jsonObject.put("shippingCustCountryName", Utility.getCountryName(site.getSiteId(), shippingAddress.getCustCountryCode()));
			}
		}
		else {
			jsonObject.put("shippingUseAddress", Constants.CUST_ADDRESS_USE_CUST);
		}
		return jsonObject;
	}
	
    protected JSONEscapeObject getJsonCreditCardInfo(Site site, ShoppingCart shoppingCart, HttpServletRequest request) throws Exception {
		JSONEscapeObject jsonObject = new JSONEscapeObject();
    	CustomerCreditCard customerCreditCard = shoppingCart.getCustomerCreditCard();
    	if (customerCreditCard != null) {
    		jsonObject.put("custCreditCardFullName", customerCreditCard.getCustCreditCardFullName());
    		jsonObject.put("custCreditCardNum", AESEncoder.getInstance().decode(customerCreditCard.getCustCreditCardNum()));
    		jsonObject.put("custCreditCardExpiryMonth", customerCreditCard.getCustCreditCardExpiryMonth());
    		jsonObject.put("custCreditCardExpiryYear", customerCreditCard.getCustCreditCardExpiryYear());
    		jsonObject.put("custCreditCardId", customerCreditCard.getCreditCard().getCreditCardId());
    		jsonObject.put("custCreditCardVerNum", customerCreditCard.getCustCreditCardVerNum());
			CreditCard creditCard = customerCreditCard.getCreditCard();
			if (creditCard != null) {
				jsonObject.put("creditCardId", creditCard.getCreditCardId().toString());
			}
    	}
    	return jsonObject;
    }
    
	protected void initAddressInfo(ShoppingCartActionForm form, Site site, ShoppingCart shoppingCart, HttpServletRequest request, ActionMessages messages) throws Exception {
		Customer customer = shoppingCart.getCustomer();
		form.setPasswordEmpty(false);
		if (Format.isNullOrEmpty(customer.getCustPassword())) {
			form.setPasswordEmpty(true);
		}
		
		CustomerAddress billingAddress = shoppingCart.getBillingAddress();
		CustomerAddress shippingAddress = shoppingCart.getShippingAddress();

		form.setCustId(Format.getLong(customer.getCustId()));
		form.setNewUser(false);
		if (request.getParameter("new") != null  && request.getParameter("new").equals(String.valueOf(Constants.VALUE_YES))) {
			form.setNewUser(true);
		}
		form.setCustEmail(customer.getCustEmail());
		
		CustomerAddress custAddress = shoppingCart.getCustAddress();
		if (custAddress != null) {
			form.setCustFirstName(custAddress.getCustFirstName());
			form.setCustMiddleName(custAddress.getCustMiddleName());
			form.setCustLastName(custAddress.getCustLastName());
			form.setCustAddressLine1(custAddress.getCustAddressLine1());
			form.setCustAddressLine2(custAddress.getCustAddressLine2());
			form.setCustCityName(custAddress.getCustCityName());
			form.setCustStateCode(custAddress.getCustStateCode());
			form.setCustStateName(custAddress.getCustStateName());
			form.setCustCountryCode(custAddress.getCustCountryCode());
			form.setCustCountryName(custAddress.getCustCountryName());
			form.setCustZipCode(custAddress.getCustZipCode());
			form.setCustPhoneNum(custAddress.getCustPhoneNum());
			form.setCustFaxNum(custAddress.getCustFaxNum());
			if (!Format.isNullOrEmpty(custAddress.getCustStateCode())) {
				form.setCustStateName(Utility.getStateName(site.getSiteId(), custAddress.getCustStateCode()));
			}
			else {
				form.setCustStateName(custAddress.getCustStateName());
			}
			if (!Format.isNullOrEmpty(custAddress.getCustCountryCode())) {
				form.setCustCountryName(Utility.getCountryName(site.getSiteId(), custAddress.getCustCountryCode()));
			}
		}

		if (billingAddress != null) {
			form.setBillingCustAddressId(Format.getLong(billingAddress.getCustAddressId()));
			form.setBillingCustFirstName(billingAddress.getCustFirstName());
			form.setBillingCustMiddleName(billingAddress.getCustMiddleName());
			form.setBillingCustLastName(billingAddress.getCustLastName());
			form.setBillingCustAddressLine1(billingAddress.getCustAddressLine1());
			form.setBillingCustAddressLine2(billingAddress.getCustAddressLine2());
			form.setBillingCustCityName(billingAddress.getCustCityName());
			form.setBillingCustStateCode(billingAddress.getCustStateCode());
			form.setBillingCustCountryCode(billingAddress.getCustCountryCode());
			form.setBillingCustZipCode(billingAddress.getCustZipCode());
			form.setBillingCustPhoneNum(billingAddress.getCustPhoneNum());
			form.setBillingCustFaxNum(billingAddress.getCustFaxNum());
			form.setBillingUseAddress(billingAddress.getCustUseAddress());
			if (!Format.isNullOrEmpty(billingAddress.getCustStateCode())) {
				form.setBillingCustStateName(Utility.getStateName(site.getSiteId(), billingAddress.getCustStateCode()));
			}
			else {
				form.setBillingCustStateName(billingAddress.getCustStateName());
			}
			if (!Format.isNullOrEmpty(billingAddress.getCustCountryCode())) {
				form.setBillingCustCountryName(Utility.getCountryName(site.getSiteId(), billingAddress.getCustCountryCode()));
			}
		}
		else {
			form.setBillingUseAddress(Constants.CUST_ADDRESS_USE_CUST);
		}
		if (shippingAddress != null) {
			form.setShippingCustAddressId(Format.getLong(shippingAddress.getCustAddressId()));
			form.setShippingCustFirstName(shippingAddress.getCustFirstName());
			form.setShippingCustMiddleName(shippingAddress.getCustMiddleName());
			form.setShippingCustLastName(shippingAddress.getCustLastName());
			form.setShippingCustAddressLine1(shippingAddress.getCustAddressLine1());
			form.setShippingCustAddressLine2(shippingAddress.getCustAddressLine2());
			form.setShippingCustCityName(shippingAddress.getCustCityName());
			form.setShippingCustStateCode(shippingAddress.getCustStateCode());
			form.setShippingCustCountryCode(shippingAddress.getCustCountryCode());
			form.setShippingCustZipCode(shippingAddress.getCustZipCode());
			form.setShippingCustPhoneNum(shippingAddress.getCustPhoneNum());
			form.setShippingCustFaxNum(shippingAddress.getCustFaxNum());
			form.setShippingUseAddress(shippingAddress.getCustUseAddress());
			if (!Format.isNullOrEmpty(shippingAddress.getCustStateCode())) {
				form.setShippingCustStateName(Utility.getStateName(site.getSiteId(), shippingAddress.getCustStateCode()));
			}
			else {
				form.setShippingCustStateName(shippingAddress.getCustStateName());
			}
			if (!Format.isNullOrEmpty(shippingAddress.getCustCountryCode())) {
				form.setShippingCustCountryName(Utility.getCountryName(site.getSiteId(), shippingAddress.getCustCountryCode()));
			}
		}
		else {
			form.setShippingUseAddress(Constants.CUST_ADDRESS_USE_CUST);
		}
    }
    
    protected void initCartInfo(ShoppingCartActionForm form, Site site, ShoppingCart shoppingCart, HttpServletRequest request, ActionMessages messages) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	ContentBean contentBean = getContentBean(request);
    	Formatter formatter = contentBean.getFormatter();
    	Long siteProfileClassId = contentBean.getContentSessionKey().getSiteProfileClassId();
    	
    	form.setCustomerSignin(isCustomerSession(request));
    	form.setShippingQuoteLock(shoppingCart.isShippingQuoteLock());
    	form.setCheckoutSteps(shoppingCart.getCheckoutSteps());

    	form.setCurrencyCode(contentBean.getContentSessionBean().getSiteCurrency().getSiteCurrencyClass().getCurrency().getCurrencyCode());
    	if (shoppingCart.getShippingMethod() != null) {
    		ShippingMethod shippingMethod = shoppingCart.getShippingMethod();
    		form.setShippingMethodId(shippingMethod.getShippingMethodId().toString());
    		form.setShippingMethodName(shippingMethod.getShippingMethodLanguage().getShippingMethodName());
			for (ShippingMethodLanguage language : shippingMethod.getShippingMethodLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
					if (language.getShippingMethodName() != null) {
						form.setShippingMethodName(language.getShippingMethodName());
					}
					break;
				}
			}
    	}
    	Vector<ShoppingCartItemBean> vector = new Vector<ShoppingCartItemBean>();
    	Iterator<?> iterator = shoppingCart.getShoppingCartItems().iterator();
    	while (iterator.hasNext()) {
    		ShoppingCartItem shoppingCartItem = (ShoppingCartItem) iterator.next();
    		ShoppingCartItemBean bean = new ShoppingCartItemBean();
    		Item item = shoppingCartItem.getItem();
    		Item master = item;
    		if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_SKU)) {
    			master = item.getItemSkuParent();
    		}
    		
    		bean.setItemId(item.getItemId().toString());
    		bean.setItemNum(item.getItemNum());
    		bean.setItemNaturalKey(item.getItemNaturalKey());
    		bean.setItemQty(Format.getInt(shoppingCartItem.getItemQty()));
    		int tierQty = shoppingCartItem.getTierPrice().getItemTierQty();
    		float tierPrice = shoppingCartItem.getTierPrice().getItemTierPrice();
    		bean.setItemPrice(formatItemPrice(contentBean, tierQty, tierPrice));
    		bean.setItemSubTotal(formatter.formatCurrency(shoppingCartItem.getItemPriceTotal()));
    		bean.setItemQtyError("");
    		
    		
    		bean.setItemShortDesc(master.getItemLanguage().getItemShortDesc());
    		ItemImage itemImage = master.getItemLanguage().getImage();
    		if (itemImage != null) {
    			bean.setImageId(itemImage.getImageId().toString());
    		}
            if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()){
            	for (ItemLanguage itemLanguage : master.getItemLanguages()) {
            		if (itemLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
            			if (itemLanguage.getItemShortDesc() != null) {
            				bean.setItemShortDesc(itemLanguage.getItemShortDesc());
            			}
            			if (itemLanguage.getItemImageOverride().equals(String.valueOf(Constants.VALUE_YES))) {
            				bean.setImageId(itemLanguage.getImage().getImageId().toString());
            			}
            			break;
            		}
            	}
            }
            
            Vector<ShoppingCartItemAttributeBean> shoppingCartItemAttributes = new Vector<ShoppingCartItemAttributeBean>();
            Iterator<?> itemAttributeInfoIterator = shoppingCartItem.getItemAttributeInfos().iterator();
            while (itemAttributeInfoIterator.hasNext()) {
            	ItemAttributeInfo itemAttributeInfo = (ItemAttributeInfo) itemAttributeInfoIterator.next();
            	ShoppingCartItemAttributeBean attributeBean = new ShoppingCartItemAttributeBean();
            	
            	ItemAttributeDetail itemAttributeDetail = (ItemAttributeDetail) em.find(ItemAttributeDetail.class, itemAttributeInfo.getItemAttribDetailId());
            	CustomAttribute customAttribute = itemAttributeDetail.getCustomAttributeDetail().getCustomAttribute();
            	if (customAttribute.getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_USER_INPUT) {
            		continue;
            	}
            	if (customAttribute.getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_USER_SELECT_DROPDOWN) {
            		continue;
            	}
            	
            	attributeBean.setCustomAttribDesc(customAttribute.getCustomAttributeLanguage().getCustomAttribDesc());
            	if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
            		for (CustomAttributeLanguage language : customAttribute.getCustomAttributeLanguages()) {
            			if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
            				if (language.getCustomAttribDesc() != null) {
            					attributeBean.setCustomAttribDesc(language.getCustomAttribDesc());
            				}
                			break;
            			}
            		}
            	}
            	
            	if (customAttribute.getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_CUST_INPUT) {
            		attributeBean.setCustomAttribValue(itemAttributeInfo.getItemAttribDetailValue());
            	}
            	else {
	            	CustomAttributeOption customAttribOption = CustomAttributeOptionDAO.load(site.getSiteId(), itemAttributeInfo.getCustomAttribOptionId());
	            	attributeBean.setCustomAttribValue(customAttribOption.getCustomAttributeOptionLanguage().getCustomAttribValue());
	            	if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
	            		for (CustomAttributeOptionLanguage language : customAttribOption.getCustomAttributeOptionLanguages()) {
	            			if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
	            				if (language.getCustomAttribValue() != null) {
	            					attributeBean.setCustomAttribValue(language.getCustomAttribValue());
	            				}
		            			break;
	            			}
	            		}
	            	}
            	}
            	shoppingCartItemAttributes.add(attributeBean);
            }
            bean.setShoppingCartItemAttributes(shoppingCartItemAttributes);
    		vector.add(bean);
    	}
    	form.setShoppingCartItemInfos(vector);
    	
    	Vector<ShoppingCartCouponBean> couponVector = new Vector<ShoppingCartCouponBean>();
    	iterator = shoppingCart.getShoppingCartCoupons().iterator();
    	while (iterator.hasNext()) {
    		ShoppingCartCoupon shoppingCartCoupon = (ShoppingCartCoupon) iterator.next();
    		Coupon coupon = shoppingCartCoupon.getCoupon();
    		ShoppingCartCouponBean bean = new ShoppingCartCouponBean();
    		bean.setCouponId(Format.getLong(coupon.getCouponId()));
    		bean.setCouponCode(coupon.getCouponCode());
    		bean.setCouponName(coupon.getCouponLanguage().getCouponName());
    		if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
    			for (CouponLanguage language : coupon.getCouponLanguages()) {
    				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
    					if (language.getCouponName() != null) {
    						bean.setCouponName(language.getCouponName());
    					}
    					break;
    				}
    			}
    		}
    		bean.setCouponAmount(formatter.formatCurrency(shoppingCartCoupon.getCouponAmount()));
    		couponVector.add(bean);
    	}
    	form.setShoppingCartCouponInfos(couponVector);
    	
    	ItemTax taxes[] = shoppingCart.getTaxes();
     	Vector<ShoppingCartTaxInfo> taxVector = new Vector<ShoppingCartTaxInfo>();
    	if (taxes != null) {
	    	for (int i = 0; i < taxes.length; i++) {
	    		ShoppingCartTaxInfo taxInfo = new ShoppingCartTaxInfo();
	    		Tax tax = taxes[i].getTax();
	    		taxInfo.setTaxName(tax.getTaxLanguage().getTaxName());
	    		if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
	    			for (TaxLanguage language : tax.getTaxLanguages()) {
	    				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
	    					if (language.getTaxName() != null) {
	    						taxInfo.setTaxName(language.getTaxName());
	    					}
	    					break;
	    				}
	    			}
	    		}
	    		taxInfo.setTaxAmount(formatter.formatCurrency(taxes[i].getTaxAmount()));
	    		taxVector.add(taxInfo);
	    	}
    	}
    	Collections.sort(taxVector);
    	form.setShoppingCartTaxInfos(taxVector);
    	
    	form.setPriceTotal(formatter.formatCurrency(shoppingCart.getShoppingCartSubTotal()));
    	form.setTaxTotal(formatter.formatCurrency(shoppingCart.getTaxTotal()));
    	form.setShippingTotal(formatter.formatCurrency(shoppingCart.getShippingTotal()));
    	form.setShippingDiscountTotal(formatter.formatCurrency(shoppingCart.getShippingDiscountTotal()));
    	form.setShippingOrderTotal(formatter.formatCurrency(shoppingCart.getShippingOrderTotal()));
    	form.setOrderTotal(formatter.formatCurrency(shoppingCart.getOrderTotal()));
    	
    	Vector<LabelValueBean> smVector = new Vector<LabelValueBean>();
    	if (isCustomerSession(request)) {
	    	Vector<?> shippingMethodVector = shoppingCart.getShippingMethods();
	    	iterator = shippingMethodVector.iterator();
	    	while (iterator.hasNext()) {
	    		ShippingMethod shippingMethod = (ShippingMethod) iterator.next();
	    		LabelValueBean bean = new LabelValueBean();
	    		bean.setLabel(shippingMethod.getShippingMethodLanguage().getShippingMethodName());
	    		bean.setValue(shippingMethod.getShippingMethodId().toString());
	            if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
	    			for (ShippingMethodLanguage language : shippingMethod.getShippingMethodLanguages()) {
	    				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
	    					if (language.getShippingMethodName() != null) {
	    						bean.setLabel(language.getShippingMethodName());
	    					}
	    					break;
	    				}
	    			}
	            }
	    		smVector.add(bean);
	    	}
    	}
    	else {
    		Query query = em.createQuery("from ShippingMethod where siteId = :siteId");
        	query.setParameter("siteId", site.getSiteId());
        	iterator = query.getResultList().iterator();
        	while (iterator.hasNext()) {
        		ShippingMethod shippingMethod = (ShippingMethod) iterator.next();
        		LabelValueBean bean = new LabelValueBean();
	    		bean.setLabel(shippingMethod.getShippingMethodLanguage().getShippingMethodName());
	    		bean.setValue(shippingMethod.getShippingMethodId().toString());
	            if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
	    			for (ShippingMethodLanguage language : shippingMethod.getShippingMethodLanguages()) {
	    				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
	    					if (language.getShippingMethodName() != null) {
	    						bean.setLabel(language.getShippingMethodName());
	    					}
	    					break;
	    				}
	    			}
	            }
	    		smVector.add(bean);
        	}
    	}
    	LabelValueBean shippingMethods[] = new LabelValueBean[smVector.size()];
    	smVector.copyInto(shippingMethods);
    	form.setShippingMethods(shippingMethods);
    	
    	SiteDomainLanguage siteDomainLanguage = contentBean.getContentSessionBean().getSiteDomain().getSiteDomainLanguage();
    	for (SiteDomainLanguage language : contentBean.getContentSessionBean().getSiteDomain().getSiteDomainLanguages()) {
    		if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
    			siteDomainLanguage = language;
    			break;
    		}
    	}
    	
    	SiteDomainParamBean siteDomainParamBean = SiteDomainDAO.getSiteDomainParamBean(contentBean.getContentSessionBean().getSiteDomain().getSiteDomainLanguage(), siteDomainLanguage);
    	form.setShoppingCartMessage(siteDomainParamBean.getCheckoutShoppingCartMessage());
    	PaymentEngine paymentEngine = shoppingCart.getPaymentEngine();
    	if (paymentEngine != null) {
	    	form.setPaymentGatewayProvider(paymentEngine.getClass().getSimpleName());
    	}
    	form.setCashPaymentOrder(shoppingCart.isCashPaymentOrder());
    	form.setCreditCardOrder(shoppingCart.isCreditCardOrder());
    	form.setPayPalOrder(shoppingCart.isPayPalOrder());
    	form.setPayPal(shoppingCart.isPayPal());
    	form.setPayPalHosted(shoppingCart.isPayPalWebsitePaymentProHosted());
    	form.setPayPalHostedOrder(shoppingCart.isPayPalWebsitePaymentProHostedOrder());
    	form.setCreditCard(shoppingCart.isCreditCard());
    	form.setCashPayment(shoppingCart.isCashPayment());
    	
    	boolean includeShippingPickUp = false;
    	if (siteDomainParamBean.getCheckoutIncludeShippingPickup() != null) {
    		includeShippingPickUp = siteDomainParamBean.getCheckoutIncludeShippingPickup().equals(String.valueOf(Constants.VALUE_YES));
    	}
    	form.setIncludeShippingPickUp(includeShippingPickUp);
    	shoppingCart.setIncludeShippingPickUp(includeShippingPickUp);
    	
        form.setShippingValid(shoppingCart.isShippingValid());
        if (!shoppingCart.isShippingValid()) {
        	form.addMessage("shippingLocation", "content.error.shippingLocation.unsupported");
         	form.setAllowShippingQuote(false);
        	if (siteDomainParamBean.getCheckoutAllowsShippingQuote() != null && siteDomainParamBean.getCheckoutAllowsShippingQuote().equals(String.valueOf(Constants.VALUE_YES))) {
        		form.setAllowShippingQuote(true);
        	}
        }
    	
    	ContentApi contentApi = new ContentApi(request);
    	
    	Vector<ItemInfo> crossSellItems = new Vector<ItemInfo>();
    	iterator = shoppingCart.getShoppingCartItems().iterator();
    	while (iterator.hasNext()) {
    		ShoppingCartItem shoppingCartItem = (ShoppingCartItem) iterator.next();
    		Item item = shoppingCartItem.getItem();
    		if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_SKU)) {
    			item = item.getItemSkuParent();
    		}
    		Iterator<?> itemsCrossSell = item.getItemsCrossSell().iterator();
    		while (itemsCrossSell.hasNext()) {
    			Item upSellItem = (Item) itemsCrossSell.next();
    			if (isExist(crossSellItems, upSellItem)) {
    				continue;
    			}
    			ItemInfo itemInfo = contentApi.formatItem(upSellItem);
        		crossSellItems.add(itemInfo);
    		}
    	}
    	form.setCrossSellItems(crossSellItems);
    }
    
    protected void initCreditCardInfo(ShoppingCartActionForm form, Site site, ShoppingCart shoppingCart, HttpServletRequest request, ActionMessages messages) throws Exception {
    	CustomerCreditCard customerCreditCard = shoppingCart.getCustomerCreditCard();
    	if (customerCreditCard != null) {
			form.setCustCreditCardFullName(customerCreditCard.getCustCreditCardFullName());
			form.setCustCreditCardNum(AESEncoder.getInstance().decode(customerCreditCard.getCustCreditCardNum()));
			form.setCustCreditCardExpiryMonth(customerCreditCard.getCustCreditCardExpiryMonth());
			form.setCustCreditCardExpiryYear(customerCreditCard.getCustCreditCardExpiryYear());
			form.setCustCreditCardVerNum(customerCreditCard.getCustCreditCardVerNum());
			CreditCard creditCard = customerCreditCard.getCreditCard();
			if (creditCard != null) {
				form.setCreditCardId(creditCard.getCreditCardId().toString());
			}
    	}
    }

    protected boolean isStateCodeRequired(String siteId, String countryCode) throws SecurityException, Exception {
    	if (Format.isNullOrEmpty(countryCode)) {
    		return false;
    	}
    	
    	Country country = CountryDAO.loadByCountryCode(siteId, countryCode);
    	if (country.getStates().size() > 0) {
    		return true;
    	}
    	return false;
    }
    
    protected ActionMessages validateShipping(ContentBean contentBean, ShoppingCartActionForm form, ShoppingCart shoppingCart) throws SecurityException, Exception {
    	ActionMessages errors = new ActionMessages();
    	if (Format.isNullOrEmpty(form.getShippingMethodId())) {
    		errors.add("shippingMethodId", new ActionMessage("content.error.string.required"));
    		return errors;
    	}
    	
    	boolean found = false;
    	Long shippingMethodId = Long.valueOf(form.getShippingMethodId());
    	Iterator<?> iterator = shoppingCart.getShippingMethods().iterator();
		while (iterator.hasNext()) {
			ShippingMethod shippingMethod = (ShippingMethod) iterator.next();
			if (shippingMethod.getShippingMethodId().equals(shippingMethodId)) {
				found = true;
				break;
			}
		}
		if (!found) {
			errors.add("shippingMethodId", new ActionMessage("content.error.int.invalid"));
		}
		return errors;
    }
    
    protected ActionMessages validateAddress(String siteId, ShoppingCartActionForm form) throws SecurityException, Exception { 
    	ActionMessages errors = new ActionMessages();
    	if (Format.isNullOrEmpty(form.getCustFirstName())) {
    		errors.add("custFirstName", new ActionMessage("content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustLastName())) {
    		errors.add("custLastName", new ActionMessage("content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustAddressLine1())) {
    		errors.add("custAddressLine1", new ActionMessage("content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustCityName())) {
    		errors.add("custCityName", new ActionMessage("content.error.string.required"));
    	}
    	if (isStateCodeRequired(siteId, form.getCustCountryCode())) {
	    	if (Format.isNullOrEmpty(form.getCustStateCode())) {
	    		errors.add("custStateCode", new ActionMessage("content.error.string.required"));
	    	}
    	}
    	if (Format.isNullOrEmpty(form.getCustCountryCode())) {
    		errors.add("custCountryCode", new ActionMessage("content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustZipCode())) {
    		errors.add("custZipCode", new ActionMessage("content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustPhoneNum())) {
    		errors.add("custPhoneNum", new ActionMessage("content.error.string.required"));
    	}
    	
    	if (form.getBillingUseAddress().equals(Constants.CUST_ADDRESS_USE_OWN)) {
	    	if (Format.isNullOrEmpty(form.getBillingCustFirstName())) {
	    		errors.add("billingCustFirstName", new ActionMessage("content.error.string.required"));
	    	}
	    	if (Format.isNullOrEmpty(form.getBillingCustLastName())) {
	    		errors.add("billingCustLastName", new ActionMessage("content.error.string.required"));
	    	}
	    	if (Format.isNullOrEmpty(form.getBillingCustAddressLine1())) {
	    		errors.add("billingCustAddressLine1", new ActionMessage("content.error.string.required"));
	    	}
	    	if (Format.isNullOrEmpty(form.getBillingCustCityName())) {
	    		errors.add("billingCustCityName", new ActionMessage("content.error.string.required"));
	    	}
	    	if (isStateCodeRequired(siteId, form.getBillingCustCountryCode())) {
		    	if (Format.isNullOrEmpty(form.getBillingCustStateCode())) {
		    		errors.add("billingCustStateCode", new ActionMessage("content.error.string.required"));
		    	}
	    	}
	    	if (Format.isNullOrEmpty(form.getBillingCustCountryCode())) {
	    		errors.add("billingCustCountryCode", new ActionMessage("content.error.string.required"));
	    	}
	    	if (Format.isNullOrEmpty(form.getBillingCustZipCode())) {
	    		errors.add("billingCustZipCode", new ActionMessage("content.error.string.required"));
	    	}
	    	if (Format.isNullOrEmpty(form.getBillingCustPhoneNum())) {
	    		errors.add("billingCustPhoneNum", new ActionMessage("content.error.string.required"));
	    	}
    	}
    	if (form.getShippingUseAddress().equals(Constants.CUST_ADDRESS_USE_OWN)) {
        	if (Format.isNullOrEmpty(form.getShippingCustFirstName())) {
        		errors.add("shippingCustFirstName", new ActionMessage("content.error.string.required"));
        	}
        	if (Format.isNullOrEmpty(form.getShippingCustLastName())) {
        		errors.add("shippingCustLastName", new ActionMessage("content.error.string.required"));
        	}
        	if (Format.isNullOrEmpty(form.getShippingCustAddressLine1())) {
        		errors.add("shippingCustAddressLine1", new ActionMessage("content.error.string.required"));
        	}
        	if (Format.isNullOrEmpty(form.getShippingCustCityName())) {
        		errors.add("shippingCustCityName", new ActionMessage("content.error.string.required"));
        	}
        	if (isStateCodeRequired(siteId, form.getShippingCustCountryCode())) {
	        	if (Format.isNullOrEmpty(form.getShippingCustStateCode())) {
	        		errors.add("shippingCustStateCode", new ActionMessage("content.error.string.required"));
	        	}
        	}
        	if (Format.isNullOrEmpty(form.getShippingCustCountryCode())) {
        		errors.add("shippingCustCountryCode", new ActionMessage("content.error.string.required"));
        	}
        	if (Format.isNullOrEmpty(form.getShippingCustZipCode())) {
        		errors.add("shippingCustZipCode", new ActionMessage("content.error.string.required"));
        	}
        	if (Format.isNullOrEmpty(form.getShippingCustPhoneNum())) {
        		errors.add("shippingCustPhoneNum", new ActionMessage("content.error.string.required"));
        	}
    	}
    	return errors;
    }
    
    protected ActionMessages validateCreditCard(ShoppingCartActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	if (Format.isNullOrEmpty(form.getCustCreditCardFullName())) {
    		errors.add("custCreditCardFullName", new ActionMessage("content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustCreditCardNum())) {
    		errors.add("custCreditCardNum", new ActionMessage("content.error.string.required"));
    	}
    	if (!Utility.isValidCreditCard(Format.getInt(form.getCreditCardId()), form.getCustCreditCardNum())) {
    		errors.add("custCreditCardNum", new ActionMessage("content.error.creditcard.invalid"));
    	}
    	return errors;
    }
    
    protected void saveAddress(ShoppingCartActionForm form, Site site, Customer customer, ShoppingCart shoppingCart, ContentBean contentBean) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		customer.setRecUpdateBy(Constants.USERNAME_SYSTEM);
		customer.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		// em.update(customer);
		
		CustomerAddress shippingAddress = null;
		CustomerAddress billingAddress = null;
		CustomerAddress custAddress = customer.getCustAddress();
		Iterator<?> iterator = customer.getCustAddresses().iterator();
		while (iterator.hasNext()) {
			CustomerAddress address = (CustomerAddress) iterator.next();
			if (address.getCustAddressType().equals(Constants.CUSTOMER_ADDRESS_BILLING)) {
				billingAddress = address;
			}
			if (address.getCustAddressType().equals(Constants.CUSTOMER_ADDRESS_SHIPPING)) {
				shippingAddress = address;
			}
		}
		
		if (custAddress == null) {
			custAddress = new CustomerAddress();
			customer.setCustAddress(custAddress);
			custAddress.setCustAddressType(Constants.CUSTOMER_ADDRESS_CUST);
			customer.getCustAddresses().add(custAddress);
			custAddress.setRecCreateBy(Constants.USERNAME_SYSTEM);
			custAddress.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		custAddress.setCustUseAddress(Constants.CUST_ADDRESS_USE_OWN);
		custAddress.setCustUseAddress(Constants.CUST_ADDRESS_USE_OWN);
		custAddress.setCustFirstName(form.getCustFirstName());
		custAddress.setCustMiddleName(form.getCustMiddleName());
		custAddress.setCustLastName(form.getCustLastName());
		custAddress.setCustSuffix(form.getCustSuffix());
		custAddress.setCustAddressLine1(form.getCustAddressLine1());
		custAddress.setCustAddressLine2(form.getCustAddressLine2());
		custAddress.setCustCityName(form.getCustCityName());
		if (form.getCustStateName() == null) {
			custAddress.setCustStateCode(form.getCustStateCode());
			custAddress.setCustStateName(Utility.getStateName(site.getSiteId(), form.getCustStateCode()));
		}
		else {
			custAddress.setCustStateCode("");
			custAddress.setCustStateName(form.getCustStateName());
		}
		custAddress.setCustCountryCode(form.getCustCountryCode());
		custAddress.setCustCountryName(Utility.getCountryName(site.getSiteId(), form.getCustCountryCode()));
		custAddress.setCustZipCode(form.getCustZipCode());
		custAddress.setCustPhoneNum(form.getCustPhoneNum());
		custAddress.setCustFaxNum(form.getCustFaxNum());
		custAddress.setRecUpdateBy(Constants.USERNAME_SYSTEM);
		custAddress.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		custAddress.setState(StateDAO.loadByStateCode(site.getSiteId(), form.getCustStateCode()));
		custAddress.setCountry(CountryDAO.loadByCountryCode(site.getSiteId(), form.getCustCountryCode()));
		custAddress.setRecCreateBy(Constants.USERNAME_SYSTEM);
		custAddress.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		if (custAddress.getCustAddressId() == null) {
			em.persist(custAddress);
		}
		shoppingCart.setCustAddress(custAddress);
		
		if (billingAddress == null) {
			billingAddress = new CustomerAddress();
			billingAddress.setCustAddressType(Constants.CUSTOMER_ADDRESS_BILLING);
			customer.getCustAddresses().add(billingAddress);
			billingAddress.setRecCreateBy(Constants.USERNAME_SYSTEM);
			billingAddress.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		billingAddress.setCustUseAddress(form.getBillingUseAddress());
		if (form.billingUseAddress.equals(Constants.CUST_ADDRESS_USE_OWN)) {
			billingAddress.setCustUseAddress(Constants.CUST_ADDRESS_USE_OWN);
			billingAddress.setCustPrefix(form.getBillingCustPrefix());
			billingAddress.setCustFirstName(form.getBillingCustFirstName());
			billingAddress.setCustMiddleName(form.getBillingCustMiddleName());
			billingAddress.setCustLastName(form.getBillingCustLastName());
			billingAddress.setCustSuffix(form.getBillingCustSuffix());
			billingAddress.setCustAddressLine1(form.getBillingCustAddressLine1());
			billingAddress.setCustAddressLine2(form.getBillingCustAddressLine2());
			billingAddress.setCustCityName(form.getBillingCustCityName());
			if (form.getBillingCustStateName() == null) {
				billingAddress.setCustStateCode(form.getBillingCustStateCode());
				billingAddress.setCustStateName(Utility.getStateName(site.getSiteId(), form.getBillingCustStateCode()));
			}
			else {
				billingAddress.setCustStateCode("");
				billingAddress.setCustStateName(form.getBillingCustStateName());
			}
			billingAddress.setCustCountryCode(form.getBillingCustCountryCode());
			billingAddress.setCustCountryName(Utility.getCountryName(site.getSiteId(), form.getBillingCustCountryCode()));
			billingAddress.setCustZipCode(form.getBillingCustZipCode());
			billingAddress.setCustPhoneNum(form.getBillingCustPhoneNum());
			billingAddress.setCustFaxNum(form.getBillingCustFaxNum());
			billingAddress.setState(StateDAO.loadByStateCode(site.getSiteId(), form.getBillingCustStateCode()));
			billingAddress.setCountry(CountryDAO.loadByCountryCode(site.getSiteId(), form.getBillingCustCountryCode()));
		}
		billingAddress.setRecUpdateBy(Constants.USERNAME_SYSTEM);
		billingAddress.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		if (billingAddress.getCustAddressId() == null) {
			em.persist(billingAddress);
		}
		shoppingCart.setBillingAddress(billingAddress);
		
		if (shippingAddress == null) {
			shippingAddress = new CustomerAddress();
			shippingAddress.setCustAddressType(Constants.CUSTOMER_ADDRESS_SHIPPING);
			customer.getCustAddresses().add(shippingAddress);
			shippingAddress.setRecCreateBy(Constants.USERNAME_SYSTEM);
			shippingAddress.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		shippingAddress.setCustUseAddress(form.getShippingUseAddress());
		if (form.shippingUseAddress.equals(Constants.CUST_ADDRESS_USE_OWN)) {
			if (shippingAddress == null) {
				shippingAddress = new CustomerAddress();
				customer.getCustAddresses().add(shippingAddress);
				shippingAddress.setCustUseAddress(form.getShippingUseAddress());
				shippingAddress.setRecCreateBy(Constants.USERNAME_SYSTEM);
				shippingAddress.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			}
			shippingAddress.setCustAddressType(Constants.CUSTOMER_ADDRESS_SHIPPING);
			shippingAddress.setCustPrefix(form.getShippingCustPrefix());
			shippingAddress.setCustFirstName(form.getShippingCustFirstName());
			shippingAddress.setCustMiddleName(form.getShippingCustMiddleName());
			shippingAddress.setCustLastName(form.getShippingCustLastName());
			shippingAddress.setCustSuffix(form.getShippingCustSuffix());
			shippingAddress.setCustAddressLine1(form.getShippingCustAddressLine1());
			shippingAddress.setCustAddressLine2(form.getShippingCustAddressLine2());
			shippingAddress.setCustCityName(form.getShippingCustCityName());
			if (form.getShippingCustStateName() == null) {
				shippingAddress.setCustStateCode(form.getShippingCustStateCode());
				shippingAddress.setCustStateName(Utility.getStateName(site.getSiteId(), form.getShippingCustStateCode()));
			}
			else {
				shippingAddress.setCustStateCode("");
				shippingAddress.setCustStateName(form.getShippingCustStateName());
			}
			shippingAddress.setCustCountryCode(form.getShippingCustCountryCode());
			shippingAddress.setCustCountryName(Utility.getCountryName(site.getSiteId(), form.getShippingCustCountryCode()));
			shippingAddress.setCustZipCode(form.getShippingCustZipCode());
			shippingAddress.setCustPhoneNum(form.getShippingCustPhoneNum());
			shippingAddress.setCustFaxNum(form.getShippingCustFaxNum());
			shippingAddress.setState(StateDAO.loadByStateCode(site.getSiteId(), form.getShippingCustStateCode()));
			shippingAddress.setCountry(CountryDAO.loadByCountryCode(site.getSiteId(), form.getShippingCustCountryCode()));
			if (shippingAddress.getCustAddressId() == null) {
				em.persist(shippingAddress);
			}
			shoppingCart.setShippingAddress(shippingAddress);
		}
		shippingAddress.setRecUpdateBy(Constants.USERNAME_SYSTEM);
		shippingAddress.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		if (shippingAddress.getCustAddressId() == null) {
			em.persist(shippingAddress);
		}
		if (customer.getCustId() == null) {
			em.persist(customer);
		}
		shoppingCart.setShippingAddress(shippingAddress);
		shoppingCart.initCustomer(customer, contentBean);
    }
    
    protected void saveCreditCard(ShoppingCartActionForm form, Site site, ShoppingCart shoppingCart, ContentBean contentBean) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Customer customer = shoppingCart.getCustomer();
		CustomerCreditCard customerCreditCard = null;
		Iterator<?> iterator = customer.getCustCreditCards().iterator();
		boolean found = false;
		if (iterator.hasNext()) {
			found = true;
			customerCreditCard = (CustomerCreditCard) iterator.next();
			customerCreditCard.setRecCreateBy(Constants.USERNAME_SYSTEM);
			customerCreditCard.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		else {
			customerCreditCard = new CustomerCreditCard();
			customerCreditCard.setRecUpdateBy(Constants.USERNAME_SYSTEM);
			customerCreditCard.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			customerCreditCard.setRecCreateBy(Constants.USERNAME_SYSTEM);
			customerCreditCard.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		customerCreditCard.setCustCreditCardFullName(form.getCustCreditCardFullName());
		customerCreditCard.setCustCreditCardNum(AESEncoder.getInstance().encode(form.getCustCreditCardNum()));
		customerCreditCard.setCustCreditCardExpiryMonth(form.getCustCreditCardExpiryMonth());
		customerCreditCard.setCustCreditCardExpiryYear(form.getCustCreditCardExpiryYear());
		customerCreditCard.setCustCreditCardVerNum(form.getCustCreditCardVerNum());
		String creditCardId = form.getCreditCardId();
		CreditCard creditCard = (CreditCard) em.find(CreditCard.class, Format.getLong(creditCardId));
		customerCreditCard.setCreditCard(creditCard);
		
		if (SiteDAO.isStoreCreditCard(site)) {
			if (customerCreditCard.getCustCreditCardId() == null) {
				em.persist(customerCreditCard);
			}
			if (!found) {
				customer.getCustCreditCards().add(customerCreditCard);
			}
		}
		shoppingCart.setCustomerCreditCard(new CustomerCreditCardDAO(customerCreditCard));
    }
    
    protected void initSearchInfo(ShoppingCartActionForm form, String siteId, ActionMessages messages) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from country in class Country where country.siteId = :siteId order by country.countryName");
    	query.setParameter("siteId", siteId);
     	Iterator<?> iterator = query.getResultList().iterator();
     	Vector<LabelValueBean> vector = new Vector<LabelValueBean>();
     	while (iterator.hasNext()) {
     		Country country = (Country) iterator.next();
     		LabelValueBean bean = new LabelValueBean(country.getCountryName(), country.getCountryCode());
     		vector.add(bean);
     	}
     	LabelValueBean countries[] = new LabelValueBean[vector.size()];
     	vector.copyInto(countries);
     	form.setCountries(countries);
     	
    	String sql = "from CreditCard credit_card where siteId = :siteId order by seqNum";
     	query = em.createQuery(sql);
    	query.setParameter("siteId", siteId);
     	iterator = query.getResultList().iterator();
     	vector = new Vector<LabelValueBean>();
     	while (iterator.hasNext()) {
     		CreditCard creditCard = (CreditCard) iterator.next();
     		LabelValueBean bean = new LabelValueBean(creditCard.getCreditCardDesc(), creditCard.getCreditCardId().toString());
     		vector.add(bean);
     	}
     	LabelValueBean creditCards[] = new LabelValueBean[vector.size()];
     	vector.copyInto(creditCards);
     	form.setCreditCards(creditCards);
   
    	LabelValueBean expiryMonths[] = {new LabelValueBean("01", "01"),
				 new LabelValueBean("02", "02"),
				 new LabelValueBean("03", "03"),
				 new LabelValueBean("04", "04"),
				 new LabelValueBean("05", "05"),
				 new LabelValueBean("06", "06"),
				 new LabelValueBean("07", "07"),
				 new LabelValueBean("08", "08"),
				 new LabelValueBean("09", "09"),
				 new LabelValueBean("10", "10"),
				 new LabelValueBean("11", "11"),
				 new LabelValueBean("12", "12")};
		LabelValueBean expiryYears[] = {new LabelValueBean("2011", "2011"),
					    new LabelValueBean("2012", "2012"),
					    new LabelValueBean("2013", "2013"),
					    new LabelValueBean("2014", "2014"),
					    new LabelValueBean("2015", "2015"),
					    new LabelValueBean("2016", "2016"),
					    new LabelValueBean("2017", "2017"),
					    new LabelValueBean("2018", "2018"),
					    new LabelValueBean("2019", "2019"),
					    new LabelValueBean("2020", "2020"),
					    new LabelValueBean("2021", "2021"),
					    new LabelValueBean("2022", "2022"),
					    new LabelValueBean("2023", "2023"),
					    new LabelValueBean("2024", "2024"),
					    new LabelValueBean("2025", "2025"),
					    new LabelValueBean("2026", "2026"),
					    new LabelValueBean("2027", "2027"),
					    new LabelValueBean("2028", "2028"),
					    new LabelValueBean("2029", "2029"),
					    };
		form.setExpiryMonths(expiryMonths);
		form.setExpiryYears(expiryYears);
		
		Country estimateCountry = CountryDAO.loadByCountryCode(siteId, form.getEstimateCountryCode());
		Vector<LabelValueBean> estimateStateVector = new Vector<LabelValueBean>();
		if (estimateCountry != null) {
			for (State state : estimateCountry.getStates()) {
				LabelValueBean bean = new LabelValueBean(state.getStateName(), state.getStateCode());
				estimateStateVector.add(bean);
			}
			LabelValueBean states[] = new LabelValueBean[estimateStateVector.size()];
			estimateStateVector.copyInto(states);
	     	form.setEstimateStates(states);
		}
		else {
			LabelValueBean states[] = {};
	     	form.setEstimateStates(states);
		}
		
		Country custCountry = CountryDAO.loadByCountryCode(siteId, form.getCustCountryCode());
		Vector<LabelValueBean> stateVector = new Vector<LabelValueBean>();
		if (custCountry != null) {
			for (State state : custCountry.getStates()) {
				LabelValueBean bean = new LabelValueBean(state.getStateName(), state.getStateCode());
				stateVector.add(bean);
			}
			LabelValueBean states[] = new LabelValueBean[stateVector.size()];
			stateVector.copyInto(states);
	     	form.setCustStates(states);
		}
		else {
			LabelValueBean states[] = {};
	     	form.setCustStates(states);
		}
		
		custCountry = CountryDAO.loadByCountryCode(siteId, form.getBillingCustCountryCode());
		stateVector = new Vector<LabelValueBean>();
		if (custCountry != null) {
			for (State state : custCountry.getStates()) {
				LabelValueBean bean = new LabelValueBean(state.getStateName(), state.getStateCode());
				stateVector.add(bean);
			}
			LabelValueBean states[] = new LabelValueBean[stateVector.size()];
			stateVector.copyInto(states);
	     	form.setBillingCustStates(states);
		}
		else {
			LabelValueBean states[] = {};
	     	form.setBillingCustStates(states);
		}
		
		custCountry = CountryDAO.loadByCountryCode(siteId, form.getShippingCustCountryCode());
		stateVector = new Vector<LabelValueBean>();
		if (custCountry != null) {
			for (State state : custCountry.getStates()) {
				LabelValueBean bean = new LabelValueBean(state.getStateName(), state.getStateCode());
				stateVector.add(bean);
			}
			LabelValueBean states[] = new LabelValueBean[stateVector.size()];
			stateVector.copyInto(states);
	     	form.setShippingCustStates(states);
		}
		else {
			LabelValueBean states[] = {};
	     	form.setShippingCustStates(states);
		}
    }

    protected void uplockShippingQuote(ShoppingCart shoppingCart, String step) throws Exception {
    	OrderEngine orderEngine = new OrderEngine(shoppingCart, null);
    	orderEngine.saveOpenOrder(step);
    	orderEngine.unlockShippingQuote();
    	shoppingCart.setOrderNum(orderEngine.getOrderHeader().getOrderNum());
    }
    
    protected void saveOpenOrder(ShoppingCart shoppingCart, String step) throws Exception {
    	OrderEngine orderEngine = new OrderEngine(shoppingCart, null);
    	orderEngine.saveOpenOrder(step);
    	shoppingCart.setOrderNum(orderEngine.getOrderHeader().getOrderNum());
    }
    
    protected void removeOpenOrder(ShoppingCart shoppingCart) throws Exception {
    	OrderEngine orderEngine = new OrderEngine(shoppingCart, null);
    	orderEngine.removeOpenOrder();
    	shoppingCart.setOrderNum(null);
    }
    
    protected void finalizeOrder(ShoppingCartActionForm form, Site site, ShoppingCart shoppingCart, HttpServletRequest request, ActionMessages messages) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	ContentBean contentBean = getContentBean(request);
    	PaymentEngine paymentEngine = (PaymentEngine) shoppingCart.getPaymentEngine();
    	createEmptySecureTemplateInfo(request);
		OrderEngine orderEngine = null;
		try {
    		SiteDomainParamBean siteDomainParamBean = SiteDomainDAO.getSiteDomainParamBean(contentBean.getContentSessionBean().getSiteDomain().getSiteDomainLanguage(),
    																					   contentBean.getContentSessionBean().getSiteDomain().getSiteDomainLanguage());
    		orderEngine = new OrderEngine(shoppingCart, null);
    		if (shoppingCart.isCashPaymentOrder()) {
    			orderEngine.processOrder();
    			orderEngine.saveOrder();
    		}
    		else {
        		String value = siteDomainParamBean.getPaymentProcessType();
        		boolean authorizeOnly = false;
        		if (value == null || value.equals(Constants.PAYMENT_PROCESS_TYPE_AUTHORIZE_ONLY)) {
        			authorizeOnly = true;
        		}
	    		if (authorizeOnly) {
	    			orderEngine.authorizeOrder(shoppingCart.getPaymentEngine(), request);
	    			orderEngine.processOrder();
	    			orderEngine.saveOrder();
	    		}
	    		else {
	    			OrderHeader orderHeader = orderEngine.getOrderHeader();
	    			InvoiceEngine invoiceEngine = new InvoiceEngine(orderHeader, null);
	    			invoiceEngine.invoiceAll();
	    			invoiceEngine.setCreditCardInfo(orderEngine.getCreditCardInfo());
	    			invoiceEngine.payOrder(shoppingCart.getPaymentEngine(), request);
	    			orderEngine.processOrder();
	    			orderEngine.saveOrder();
	    			invoiceEngine.saveOrder();
	    		}
    		}
    		em.flush();
    		shoppingCart.setOrderNum(orderEngine.getOrderHeader().getOrderNum());
//    		em.getTransaction().commit();
    	}
    	catch (AuthorizationException e) {
    		throw e;
    	}
    	catch (PaymentException e) {
    		logger.error(e);
    		paymentEngine.abort();
    		shoppingCart.cancelTransaction();
    		throw e;
    	}
    	
    	try {
    		orderEngine.sendCustSaleConfirmEmail(request, this.getServlet().getServletContext());
    	}
    	catch (Exception e) {
    		// Unable to send email.  Still consider to be a successful transaction.
    		logger.error(e);
    	}
    }
    
    protected void initFromOrder(String orderNum, ShoppingCartActionForm form, Site site, HttpServletRequest request, ActionMessages messages) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Long custId = ContentLookupDispatchAction.getCustId(request);
		ContentBean contentBean = getContentBean(request);
		Formatter formatter = contentBean.getFormatter();
		OrderHeader orderHeader = null;
		String sql = "select  orderHeader " +
		             "from    OrderHeader orderHeader " +
		             "where   orderHeader.siteDomain.site.siteId = :siteId " +
		             "and     orderHeader.orderNum = :orderNum ";
        Query query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        query.setParameter("orderNum", orderNum);
        List<?> list = query.getResultList();
        if (list.size() > 0) {
        	orderHeader = (OrderHeader) list.get(0);
        }
        if (!orderHeader.getCustomer().getCustId().equals(custId)) {
        	throw new SecurityException("Customer should not have access to other's customer order");
        }
        OrderEngine orderEngine = new OrderEngine(orderHeader, null);
        
        Iterator<?> iterator = orderHeader.getOrderItemDetails().iterator();
    	Vector<ShoppingCartItemBean> vector = new Vector<ShoppingCartItemBean>();
        while (iterator.hasNext()) {
        	OrderItemDetail orderItemDetail = (OrderItemDetail) iterator.next();
        	ShoppingCartItemBean bean = new ShoppingCartItemBean();
        	Item item = orderItemDetail.getItem();
        	Item master = item;
        	if (item != null) {
	        	if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_SKU)) {
	        		master = item.getItemSkuParent();
	        	}
        	}
        	
        	bean.setItemId("");
        	if (item != null) {
        		bean.setItemId(item.getItemId().toString());
        	}
        	
    		bean.setItemNum(orderItemDetail.getItemNum());
    		bean.setItemShortDesc(orderItemDetail.getItemShortDesc());
    		bean.setItemQty(formatter.formatNumber(orderItemDetail.getItemOrderQty()));
    		bean.setItemPrice(formatItemPrice(contentBean, orderItemDetail.getItemTierQty(), orderItemDetail.getItemTierPrice()));
    		bean.setItemSubTotal(formatter.formatCurrency(orderItemDetail.getItemDetailAmount()));
    		bean.setItemQtyError("");
			bean.setImageId(null);
			bean.setItemShortDesc(orderItemDetail.getItemShortDesc());
    		if (item != null) {
	    		ItemImage itemImage = orderItemDetail.getItem().getItemLanguage().getImage();
	    		if (itemImage != null) {
	    			bean.setImageId(itemImage.getImageId().toString());
	    		}
	    		if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
	    			for (ItemLanguage language : master.getItemLanguages()) {
	    				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
		    				if (language.getItemImageOverride().equals(String.valueOf(Constants.VALUE_YES))) {
		    					bean.setImageId("");
		    					if (language.getImage() != null) {
		    						bean.setImageId(language.getImage().getImageId().toString());
		    					}
		    				}
		    				if (language.getItemShortDesc() != null) {
		    					bean.setItemShortDesc(language.getItemShortDesc());
		    				}
	    					break;
	    				}
	    			}
	    		}
    		}
    		
    		Vector<ShoppingCartItemAttributeBean> shoppingCartItemAttributes = new Vector<ShoppingCartItemAttributeBean>();
    		if (item != null && item.getItemTypeCd().equals(Constants.ITEM_TYPE_SKU)) {
    			for (ItemAttributeDetail itemAttributeDetail : item.getItemAttributeDetails()) {
    				CustomAttribute customAttribute = itemAttributeDetail.getCustomAttributeDetail().getCustomAttribute();
    				if (customAttribute.getCustomAttribTypeCode() != Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP) {
    					continue;
    				}
	    			ShoppingCartItemAttributeBean shoppingCartItemAttributeBean = new ShoppingCartItemAttributeBean();
	    			
	    			shoppingCartItemAttributeBean.setCustomAttribDesc(customAttribute.getCustomAttributeLanguage().getCustomAttribDesc());
	        		if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
	        			for (CustomAttributeLanguage language : customAttribute.getCustomAttributeLanguages()) {
	        				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
	    	    				if (language.getCustomAttribDesc() != null) {
	    	    					shoppingCartItemAttributeBean.setCustomAttribDesc(language.getCustomAttribDesc());
	    	    				}
	        					break;
	        				}
	        			}
	        		}

	        		CustomAttributeOption customAttributeOption = itemAttributeDetail.getCustomAttributeOption();
	        		shoppingCartItemAttributeBean.setCustomAttribValue(customAttributeOption.getCustomAttributeOptionLanguage().getCustomAttribValue());
	        		if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
	        			for (CustomAttributeOptionLanguage language : customAttributeOption.getCustomAttributeOptionLanguages()) {
	        				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
	    	    				if (language.getCustomAttribValue() != null) {
	    	    					shoppingCartItemAttributeBean.setCustomAttribValue(language.getCustomAttribValue());
	    	    				}
	        					break;
	        				}
	        			}
	        		}
	        		shoppingCartItemAttributes.add(shoppingCartItemAttributeBean);
    			}
    		}
    		
    		for (OrderAttributeDetail orderAttributeDetail : orderItemDetail.getOrderAttributeDetails()) {
    			ShoppingCartItemAttributeBean shoppingCartItemAttributeBean = new ShoppingCartItemAttributeBean();
    			CustomAttribute customAttribute = orderAttributeDetail.getCustomAttributeDetail().getCustomAttribute();
    			shoppingCartItemAttributeBean.setCustomAttribDesc(customAttribute.getCustomAttributeLanguage().getCustomAttribDesc());
        		if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
        			for (CustomAttributeLanguage language : customAttribute.getCustomAttributeLanguages()) {
        				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
    	    				if (language.getCustomAttribDesc() != null) {
    	    					shoppingCartItemAttributeBean.setCustomAttribDesc(language.getCustomAttribDesc());
    	    				}
        					break;
        				}
        			}
        		}
        		
        		if (customAttribute.getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_CUST_INPUT) {
        			shoppingCartItemAttributeBean.setCustomAttribValue(orderAttributeDetail.getOrderAttribValue());
        		}
        		else {
	        		CustomAttributeOption customAttributeOption = orderAttributeDetail.getCustomAttributeOption();
	        		shoppingCartItemAttributeBean.setCustomAttribValue(customAttributeOption.getCustomAttributeOptionLanguage().getCustomAttribValue());
	        		if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
	        			for (CustomAttributeOptionLanguage language : customAttributeOption.getCustomAttributeOptionLanguages()) {
	        				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
	    	    				if (language.getCustomAttribValue() != null) {
	    	    					shoppingCartItemAttributeBean.setCustomAttribValue(language.getCustomAttribValue());
	    	    				}
	        					break;
	        				}
	        			}
	        		}
        		}
        		shoppingCartItemAttributes.add(shoppingCartItemAttributeBean);
    		}
    		
    		bean.setShoppingCartItemAttributes(shoppingCartItemAttributes);
    		vector.add(bean);
        }
        form.setShoppingCartItemInfos(vector);
        
    	Vector<ShoppingCartCouponBean> couponVector = new Vector<ShoppingCartCouponBean>();
    	iterator = orderHeader.getOrderOtherDetails().iterator();
     	while (iterator.hasNext()) {
    		OrderOtherDetail orderOtherDetail = (OrderOtherDetail) iterator.next();
    		Coupon coupon = orderOtherDetail.getCoupon();
    		ShoppingCartCouponBean bean = new ShoppingCartCouponBean();
    		bean.setCouponCode(coupon.getCouponCode());
    		bean.setCouponName(coupon.getCouponLanguage().getCouponName());
    		if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
    			for (CouponLanguage language : coupon.getCouponLanguages()) {
    				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
    					if (language.getCouponName() != null) {
    						bean.setCouponName(language.getCouponName());
    					}
    					break;
    				}
    			}
    		}
    		bean.setCouponAmount(Format.getFloat(orderOtherDetail.getOrderOtherDetailAmount()));
    		couponVector.add(bean);
    	}
    	form.setShoppingCartCouponInfos(couponVector);

        sql = "select  orderDetailTax.taxName, sum(orderDetailTax.taxAmount) " +
              "from    OrderDetailTax orderDetailTax " +
              "left    join orderDetailTax.orderHeader orderHeader " +
              "where   orderHeader.orderHeaderId = :orderHeaderId " +
              "group   by orderDetailTax.taxName " +
              "order   by orderDetailTax.taxName ";
        query = em.createQuery(sql);
        query.setParameter("orderHeaderId", orderHeader.getOrderHeaderId());
        iterator = query.getResultList().iterator();
    	Vector<ShoppingCartTaxInfo> taxVector = new Vector<ShoppingCartTaxInfo>();
    	while (iterator.hasNext()) {
    		Object object[] = (Object[]) iterator.next();
    		String taxName = (String) object[0];
    		Double taxAmount = (Double) object[1];
    		ShoppingCartTaxInfo taxInfo = new ShoppingCartTaxInfo();
    		taxInfo.setTaxName(taxName);
    		taxInfo.setTaxAmount(formatter.formatCurrency(taxAmount.floatValue()));
    		taxVector.add(taxInfo);
    	}
    	form.setShoppingCartTaxInfos(taxVector);
        
    	form.setPriceTotal(formatter.formatCurrency(orderEngine.getOrderSubTotal()));
    	form.setTaxTotal(formatter.formatCurrency(orderEngine.getOrderTaxTotal()));
    	form.setShippingTotal(formatter.formatCurrency(orderHeader.getShippingTotal()));
    	form.setShippingDiscountTotal(formatter.formatCurrency(orderHeader.getShippingDiscountTotal()));
    	form.setShippingOrderTotal(formatter.formatCurrency(orderHeader.getShippingTotal() - orderHeader.getShippingDiscountTotal()));
    	form.setOrderTotal(formatter.formatCurrency(orderHeader.getOrderTotal()));
    	
    	OrderAddress custAddress = orderHeader.getCustAddress();
    	if (custAddress != null) {
    		ShoppingCartAddressActionForm custAddressForm = new ShoppingCartAddressActionForm();
    		custAddressForm.setCustUseAddress(custAddress.getCustUseAddress());
    		custAddressForm.setCustPrefix(custAddress.getCustPrefix());
    		custAddressForm.setCustFirstName(custAddress.getCustFirstName());
    		custAddressForm.setCustMiddleName(custAddress.getCustMiddleName());
    		custAddressForm.setCustLastName(custAddress.getCustLastName());
    		custAddressForm.setCustSuffix(custAddress.getCustSuffix());
    		custAddressForm.setCustAddressLine1(custAddress.getCustAddressLine1());
    		custAddressForm.setCustAddressLine2(custAddress.getCustAddressLine2());
    		custAddressForm.setCustCityName(custAddress.getCustCityName());
    		custAddressForm.setCustStateName(custAddress.getCustStateName());
    		custAddressForm.setCustStateCode(custAddress.getCustStateCode());
    		custAddressForm.setCustZipCode(custAddress.getCustZipCode());
    		custAddressForm.setCustCountryName(custAddress.getCustCountryName());
    		custAddressForm.setCustCountryCode(custAddress.getCustCountryCode());
    		custAddressForm.setCustPhoneNum(custAddress.getCustPhoneNum());
    		custAddressForm.setCustFaxNum(custAddress.getCustFaxNum());
    		form.setCustAddress(custAddressForm);
    	}
    	
    	OrderAddress billingAddress = orderHeader.getBillingAddress();
    	OrderAddress shippingAddress = orderHeader.getShippingAddress();
		if (billingAddress != null) {
    		ShoppingCartAddressActionForm billingAddressForm = new ShoppingCartAddressActionForm();
    		billingAddressForm.setCustUseAddress(billingAddress.getCustUseAddress());
    		billingAddressForm.setCustPrefix(billingAddress.getCustPrefix());
    		billingAddressForm.setCustFirstName(billingAddress.getCustFirstName());
    		billingAddressForm.setCustMiddleName(billingAddress.getCustMiddleName());
    		billingAddressForm.setCustLastName(billingAddress.getCustLastName());
    		billingAddressForm.setCustSuffix(billingAddress.getCustSuffix());
    		billingAddressForm.setCustAddressLine1(billingAddress.getCustAddressLine1());
    		billingAddressForm.setCustAddressLine2(billingAddress.getCustAddressLine2());
    		billingAddressForm.setCustCityName(billingAddress.getCustCityName());
    		billingAddressForm.setCustStateName(billingAddress.getCustStateName());
    		billingAddressForm.setCustStateCode(billingAddress.getCustStateCode());
    		billingAddressForm.setCustZipCode(billingAddress.getCustZipCode());
    		billingAddressForm.setCustCountryName(billingAddress.getCustCountryName());
    		billingAddressForm.setCustCountryCode(billingAddress.getCustCountryCode());
    		billingAddressForm.setCustPhoneNum(billingAddress.getCustPhoneNum());
    		billingAddressForm.setCustFaxNum(billingAddress.getCustFaxNum());
    		form.setBillingAddress(billingAddressForm);
		}
		if (shippingAddress != null) {
    		ShoppingCartAddressActionForm shippingAddressForm = new ShoppingCartAddressActionForm();
    		shippingAddressForm.setCustUseAddress(shippingAddress.getCustUseAddress());
    		shippingAddressForm.setCustPrefix(shippingAddress.getCustPrefix());
    		shippingAddressForm.setCustFirstName(shippingAddress.getCustFirstName());
    		shippingAddressForm.setCustMiddleName(shippingAddress.getCustMiddleName());
    		shippingAddressForm.setCustLastName(shippingAddress.getCustLastName());
    		shippingAddressForm.setCustSuffix(shippingAddress.getCustSuffix());
    		shippingAddressForm.setCustAddressLine1(shippingAddress.getCustAddressLine1());
    		shippingAddressForm.setCustAddressLine2(shippingAddress.getCustAddressLine2());
    		shippingAddressForm.setCustCityName(shippingAddress.getCustCityName());
    		shippingAddressForm.setCustStateName(shippingAddress.getCustStateName());
    		shippingAddressForm.setCustStateCode(shippingAddress.getCustStateCode());
    		shippingAddressForm.setCustZipCode(shippingAddress.getCustZipCode());
    		shippingAddressForm.setCustCountryName(shippingAddress.getCustCountryName());
    		shippingAddressForm.setCustCountryCode(shippingAddress.getCustCountryCode());
    		shippingAddressForm.setCustPhoneNum(shippingAddress.getCustPhoneNum());
    		shippingAddressForm.setCustFaxNum(shippingAddress.getCustFaxNum());
    		form.setShippingAddress(shippingAddressForm);
		}
    	form.setCustEmail(orderHeader.getCustEmail());
    	form.setShippingMethodName(orderHeader.getShippingMethodName());
    	PaymentTran payment = orderHeader.getPaymentTran();
    	if (payment == null) {
    		for (InvoiceHeader invoiceHeader : orderHeader.getInvoiceHeaders()) {
    			payment = invoiceHeader.getPaymentTran();
    		}
    	}
    	if (payment != null) {
	    	form.setPaymentGatewayProvider(orderHeader.getPaymentGatewayProvider());
	    	if (orderHeader.getPaymentGatewayProvider().equals(PayPalEngine.class.getSimpleName())) {
	        	form.setPayPal(true);
	        	form.setAuthCode(payment.getAuthCode());
	    	}
	    	else {
	    		form.setPayPal(false);
		    	form.setCreditCardDesc(orderHeader.getCreditCardDesc());
		    	String custCreditCardNum = null;
		    	if (orderHeader.getCustCreditCardNum() != null) {
			    	custCreditCardNum = AESEncoder.getInstance().decode(orderHeader.getCustCreditCardNum());
			    	custCreditCardNum = Utility.maskCreditCardNumber(custCreditCardNum);
		    	}
		    	form.setCustCreditCardNum(custCreditCardNum);
		    	form.setAuthCode(payment.getAuthCode());
	    	}
    	}
    	else {
    		form.setCashPaymentOrder(true);
    	}
    	form.setOrderNum(orderNum);
    	form.setOrderDatetime(formatter.formatFullDate(orderHeader.getOrderDate()));
    	PaymentGateway paymentGateway = null;
	    if (paymentGateway != null) {
	    	if (orderHeader.getPaymentGatewayProvider().equals(PayPalEngine.class.getSimpleName())) {
	    		form.setPayPal(true);
	    	}
	    	PaymentTran paymentTran = orderHeader.getPaymentTran();
	    	if (paymentTran == null) {
	    		Iterator<?> invoiceIterator = orderHeader.getInvoiceHeaders().iterator();
	    		if (invoiceIterator.hasNext()) {
	    			InvoiceHeader invoiceHeader = (InvoiceHeader) invoiceIterator.next();
	    			paymentTran = invoiceHeader.getPaymentTran();
	    		}
	    	}
			if (paymentTran != null) {
				form.setAuthCode(paymentTran.getAuthCode());
			}
    	}
    	
    	Long siteProfileId = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileId();
    	SiteDomainLanguage siteDomainLanguage = contentBean.getContentSessionBean().getSiteDomain().getSiteDomainLanguage();
    	if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
	    	for (SiteDomainLanguage language : contentBean.getContentSessionBean().getSiteDomain().getSiteDomainLanguages()) {
	    		if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileId)) {
	    			siteDomainLanguage = language;
	    		}
	    	}
    	}
    	SiteDomainParamBean siteDomainParamBean = SiteDomainDAO.getSiteDomainParamBean(contentBean.getContentSessionBean().getSiteDomain().getSiteDomainLanguage(), siteDomainLanguage);
    	form.setShoppingCartMessage(siteDomainParamBean.getCheckoutShoppingCartMessage());
    	form.setCurrencyCode(orderHeader.getSiteCurrency().getSiteCurrencyClass().getCurrency().getCurrencyCode());
    }
    
    protected String formatItemPrice(ContentBean contentBean, int qty, float price) {
    	Formatter formatter = contentBean.getFormatter();
    	String result = "";
    	if (qty > 1) {
    		result = formatter.formatNumber(qty) + " / " +
    				 formatter.formatCurrency(price);
    	}
    	else {
    		result = formatter.formatCurrency(price);
    	}
    	return result;
    }
    
    private boolean isExist(Vector<?> crossSellItems, Item item) {
    	Iterator<?> iterator = crossSellItems.iterator();
    	while (iterator.hasNext()) {
    		ItemInfo shoppingCartItemBean = (ItemInfo) iterator.next();
    		if (shoppingCartItemBean.getItemId().equals(item.getItemId().toString())) {
    			return true;
    		}
    	}
    	return false;
    }
}
