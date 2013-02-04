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

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import javax.persistence.EntityManager;

import com.jada.order.payment.AuthorizationException;
import com.jada.order.payment.CreditCardInfo;
import com.jada.order.payment.PaymentEngine;
import com.jada.order.payment.PaymentException;
import com.jada.content.ContentBean;
import com.jada.content.ContentLookupDispatchAction;
import com.jada.content.ContentSessionKey;
import com.jada.content.template.TemplateEngine;
import com.jada.dao.CountryDAO;
import com.jada.dao.OrderHeaderDAO;
import com.jada.dao.SiteDomainDAO;
import com.jada.dao.StateDAO;
import com.jada.inventory.InventoryEngine;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.Coupon;
import com.jada.jpa.entity.CouponLanguage;
import com.jada.jpa.entity.CustomAttributeOption;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.CustomerAddress;
import com.jada.jpa.entity.CustomerCreditCard;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.ItemAttributeDetail;
import com.jada.jpa.entity.ItemLanguage;
import com.jada.jpa.entity.OrderAddress;
import com.jada.jpa.entity.OrderAttributeDetail;
import com.jada.jpa.entity.OrderDetailTax;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.OrderItemDetail;
import com.jada.jpa.entity.OrderOtherDetail;
import com.jada.jpa.entity.PaymentTran;
import com.jada.jpa.entity.ShippingMethodLanguage;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.SiteDomainLanguage;
import com.jada.jpa.entity.State;
import com.jada.jpa.entity.TaxLanguage;
import com.jada.jpa.entity.User;
import com.jada.order.cart.ItemAttributeInfo;
import com.jada.order.cart.ItemTax;
import com.jada.order.cart.ShoppingCart;
import com.jada.order.cart.ShoppingCartCoupon;
import com.jada.order.cart.ShoppingCartItem;
import com.jada.system.Languages;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Mailer;
import com.jada.xml.site.SiteDomainParamBean;

public class OrderEngine extends OrderEngineBase {
    Logger logger = Logger.getLogger(OrderEngine.class);
	static String DIRTYCHECK_ADDRESS = "ADDRESS";
	PaymentTran paymentTran = null;
	OrderHeader orderHeader = null;
	ShoppingCart shoppingCart = null;
	boolean isNew = false;
	User user = null;
	
	public OrderEngine(OrderHeader orderHeader, User user) {
		this.orderHeader = orderHeader;
	}
	
	public OrderEngine(ShoppingCart shoppingCart, User user) throws Exception {
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		this.shoppingCart = shoppingCart;
		this.user = user;
		isNew = true;
		String siteId = shoppingCart.getContentSessionKey().getSiteId();
		ContentSessionKey contentSessionKey = shoppingCart.getContentSessionKey();
		
		if (shoppingCart.getOrderNum() != null) {
			orderHeader = OrderHeaderDAO.load(siteId, shoppingCart.getOrderNum());
			isNew = false;
		}
		else {
			orderHeader = new OrderHeader();
			orderHeader.setOrderStatus(Constants.ORDERSTATUS_OPEN);
		}
		
		Customer customer = shoppingCart.getCustomer();
		if (customer != null) {
			OrderAddress custAddress = null;
			if (orderHeader.getCustAddress() != null) {
				custAddress = orderHeader.getCustAddress();
			}
			else {
				custAddress = new OrderAddress();
				custAddress.setRecCreateBy(Constants.USERNAME_SYSTEM);
				custAddress.setRecCreateDatetime(new Date());
			}
			CustomerAddress adddress = customer.getCustAddress();
			custAddress.setCustUseAddress(Constants.CUST_ADDRESS_USE_OWN);
			custAddress.setCustPrefix(adddress.getCustPrefix());
			custAddress.setCustFirstName(adddress.getCustFirstName());
			custAddress.setCustMiddleName(adddress.getCustMiddleName());
			custAddress.setCustLastName(adddress.getCustLastName());
			custAddress.setCustSuffix(adddress.getCustSuffix());
			custAddress.setCustAddressLine1(adddress.getCustAddressLine1());
			custAddress.setCustAddressLine2(adddress.getCustAddressLine2());
			custAddress.setCustCityName(adddress.getCustCityName());
			custAddress.setCustStateName(adddress.getCustStateName());
			custAddress.setCustStateCode(adddress.getCustStateCode());
			custAddress.setCustCountryName(adddress.getCustCountryName());
			custAddress.setCustCountryCode(adddress.getCustCountryCode());
			custAddress.setCustStateName(adddress.getCustStateName());
			custAddress.setCustZipCode(adddress.getCustZipCode());
			custAddress.setCustPhoneNum(adddress.getCustPhoneNum());
			custAddress.setCustFaxNum(adddress.getCustFaxNum());
			custAddress.setRecUpdateBy(Constants.USERNAME_SYSTEM);
			custAddress.setRecUpdateDatetime(new Date());
			Country country = CountryDAO.loadByCountryCode(siteId, adddress.getCustCountryCode());
			custAddress.setCountry(country);
			if (adddress.getCustStateCode() != null) {
				State state = StateDAO.loadByStateCode(siteId, adddress.getCustStateCode());
				custAddress.setState(state);
			}
			em.persist(custAddress);
			orderHeader.setCustAddress(custAddress);
		}
		
		CustomerAddress billingAddress = shoppingCart.getBillingAddress();
		if (billingAddress != null) {
			OrderAddress address = null;
			if (orderHeader.getBillingAddress() != null) {
				address = orderHeader.getBillingAddress();
			}
			else {
				address = new OrderAddress();
				address.setRecCreateBy(Constants.USERNAME_SYSTEM);
				address.setRecCreateDatetime(new Date());
			}
			address.setCustUseAddress(billingAddress.getCustUseAddress());
			address.setCustPrefix(billingAddress.getCustPrefix());
			address.setCustFirstName(billingAddress.getCustFirstName());
			address.setCustMiddleName(billingAddress.getCustMiddleName());
			address.setCustLastName(billingAddress.getCustLastName());
			address.setCustSuffix(billingAddress.getCustSuffix());
			address.setCustAddressLine1(billingAddress.getCustAddressLine1());
			address.setCustAddressLine2(billingAddress.getCustAddressLine2());
			address.setCustCityName(billingAddress.getCustCityName());
			address.setCustStateName(billingAddress.getCustStateName());
			address.setCustStateCode(billingAddress.getCustStateCode());
			address.setCustCountryName(billingAddress.getCustCountryName());
			address.setCustCountryCode(billingAddress.getCustCountryCode());
			address.setCustStateName(billingAddress.getCustStateName());
			address.setCustZipCode(billingAddress.getCustZipCode());
			address.setCustPhoneNum(billingAddress.getCustPhoneNum());
			address.setCustFaxNum(billingAddress.getCustFaxNum());
			address.setRecUpdateBy(Constants.USERNAME_SYSTEM);
			address.setRecUpdateDatetime(new Date());
			Country country = CountryDAO.loadByCountryCode(siteId, billingAddress.getCustCountryCode());
			address.setCountry(country);
			if (billingAddress.getCustStateCode() != null) {
				State state = StateDAO.loadByStateCode(siteId, billingAddress.getCustStateCode());
				address.setState(state);
			}
			em.persist(address);
			orderHeader.setBillingAddress(address);
		}
		CustomerAddress shippingAddress = shoppingCart.getShippingAddress();
		if (shippingAddress != null) {
			OrderAddress address = null;
			if (orderHeader.getShippingAddress() != null) {
				address = orderHeader.getShippingAddress();
			}
			else {
				address = new OrderAddress();
				address.setRecCreateBy(Constants.USERNAME_SYSTEM);
				address.setRecCreateDatetime(new Date());
			}
			address.setCustUseAddress(shippingAddress.getCustUseAddress());
			address.setCustPrefix(shippingAddress.getCustPrefix());
			address.setCustFirstName(shippingAddress.getCustFirstName());
			address.setCustMiddleName(shippingAddress.getCustMiddleName());
			address.setCustLastName(shippingAddress.getCustLastName());
			address.setCustSuffix(shippingAddress.getCustSuffix());
			address.setCustAddressLine1(shippingAddress.getCustAddressLine1());
			address.setCustAddressLine2(shippingAddress.getCustAddressLine2());
			address.setCustCityName(shippingAddress.getCustCityName());
			address.setCustStateName(shippingAddress.getCustStateName());
			address.setCustStateCode(shippingAddress.getCustStateCode());
			address.setCustCountryName(shippingAddress.getCustCountryName());
			address.setCustCountryCode(shippingAddress.getCustCountryCode());
			address.setCustStateName(shippingAddress.getCustStateName());
			address.setCustZipCode(shippingAddress.getCustZipCode());
			address.setCustPhoneNum(shippingAddress.getCustPhoneNum());
			address.setCustFaxNum(shippingAddress.getCustFaxNum());
			address.setRecUpdateBy(Constants.USERNAME_SYSTEM);
			address.setRecUpdateDatetime(new Date());
			Country country = CountryDAO.loadByCountryCode(siteId, shippingAddress.getCustCountryCode());
			address.setCountry(country);
			if (shippingAddress.getCustStateCode() != null) {
				State state = StateDAO.loadByStateCode(siteId, shippingAddress.getCustStateCode());
				address.setState(state);
			}
			em.persist(address);
			orderHeader.setShippingAddress(address);
		}
		if (customer != null) {
			orderHeader.setCustEmail(customer.getCustEmail());
		}
		orderHeader.setShippingTotal(Float.valueOf(shoppingCart.getShippingOrderTotal()));
		orderHeader.setShippingDiscountTotal(shoppingCart.getShippingDiscountTotal());
		orderHeader.setOrderTotal(Float.valueOf(shoppingCart.getOrderTotal()));
		orderHeader.setOrderDate(new Date());
		
		orderHeader.setPaymentGatewayProvider(null);
		orderHeader.setCreditCardDesc(null);
		orderHeader.setCustCreditCardNum(null);
		if (!shoppingCart.isCashPaymentOrder()) {
			if (shoppingCart.getPaymentEngine() != null) {
				orderHeader.setPaymentGatewayProvider(shoppingCart.getPaymentEngine().getClass().getSimpleName());
			}
		}
		CustomerCreditCard custCreditCard = shoppingCart.getCustCreditCard();
		if (!shoppingCart.isCashPaymentOrder() && custCreditCard != null) {
			orderHeader.setCreditCardDesc(custCreditCard.getCreditCard().getCreditCardDesc());
			orderHeader.setCustCreditCardNum(custCreditCard.getCustCreditCardNum());
			creditCardInfo = new CreditCardInfo();
			creditCardInfo.setCreditCardNum(AESEncoder.getInstance().decode(custCreditCard.getCustCreditCardNum()));
			creditCardInfo.setCreditCardFullName(custCreditCard.getCustCreditCardFullName());
			creditCardInfo.setCreditCardVerNum(custCreditCard.getCustCreditCardVerNum());
			creditCardInfo.setCreditCardExpiryMonth(custCreditCard.getCustCreditCardExpiryMonth());
			creditCardInfo.setCreditCardExpiryYear(custCreditCard.getCustCreditCardExpiryYear());
		}
		
		if (shoppingCart.getShippingMethod() != null) {
			orderHeader.setShippingMethodName(shoppingCart.getShippingMethod().getShippingMethodLanguage().getShippingMethodName());
			if (!contentSessionKey.isSiteProfileClassDefault()) {
				for (ShippingMethodLanguage language : shoppingCart.getShippingMethod().getShippingMethodLanguages()) {
					if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentSessionKey.getSiteProfileClassId())) {
						if (language.getShippingMethodName() != null) {
							orderHeader.setShippingMethodName(language.getShippingMethodName());
						}
						break;
					}
				}
			}
		}
		orderHeader.setShippingPickUp(Constants.VALUE_NO);
		if (shoppingCart.isShippingPickUp()) {
			orderHeader.setShippingPickUp(Constants.VALUE_YES);
			String shippingMethodName = Languages.getLangTranValue(contentSessionKey.getLangId(), "content.text.shipping.pickUp");
			orderHeader.setShippingMethodName(shippingMethodName);
		}
		
//		orderHeader.setOrderStatus(Constants.ORDERSTATUS_OPEN);
		orderHeader.setSiteDomain(shoppingCart.getSiteDomain());
		orderHeader.setCustomer(customer);
		orderHeader.setSiteCurrency(shoppingCart.getSiteCurrency());
		orderHeader.setSiteProfile(shoppingCart.getSiteProfile());
		orderHeader.setShppingMethod(shoppingCart.getShippingMethod());
		
		for (OrderItemDetail orderItemDetail : orderHeader.getOrderItemDetails()) {
			for (OrderDetailTax orderDetailTax : orderItemDetail.getOrderDetailTaxes()) {
				em.remove(orderDetailTax);
			}
			orderItemDetail.getOrderDetailTaxes().clear();
			for (OrderAttributeDetail orderAttributeDetail : orderItemDetail.getOrderAttributeDetails()) {
				em.remove(orderAttributeDetail);
			}
			orderItemDetail.getOrderAttributeDetails().clear();
			em.remove(orderItemDetail);
		}
		orderHeader.getOrderItemDetails().clear();
		
		for (OrderOtherDetail orderOtherDetail : orderHeader.getOrderOtherDetails()) {
			em.remove(orderOtherDetail);
		}
		orderHeader.getOrderOtherDetails().clear();
		
		for (OrderDetailTax orderTax : orderHeader.getOrderTaxes()) {
			em.remove(orderTax);
		}
		orderHeader.getOrderTaxes().clear();
		
		Iterator<?> shoppingCartItems = shoppingCart.getShoppingCartItems().iterator();
		int seqNum = 0;
		while (shoppingCartItems.hasNext()) {
			ShoppingCartItem shoppingCartItem = (ShoppingCartItem) shoppingCartItems.next();
			OrderItemDetail orderItemDetail = new OrderItemDetail();
			Item item = shoppingCartItem.getItem();
			Item master = item;
			if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_SKU)) {
				master = item.getItemSkuParent();
			}
			orderItemDetail.setSeqNum(Integer.valueOf(seqNum++));
			orderItemDetail.setItemNum(item.getItemNum());
			orderItemDetail.setItemUpcCd(item.getItemUpcCd());
orderItemDetail.setItemSkuCd(item.getItemSkuCd());
			orderItemDetail.setItemShortDesc(master.getItemLanguage().getItemShortDesc());
			if (master.getItemLanguage().getItemShortDesc().length() > 128) {
				orderItemDetail.setItemShortDesc(master.getItemLanguage().getItemShortDesc().substring(0, 127));
			}
			if (!contentSessionKey.isSiteProfileClassDefault()) {
				for (ItemLanguage language : master.getItemLanguages()) {
					if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentSessionKey.getSiteProfileClassId())) {
						if (language.getItemShortDesc() != null) {
							orderItemDetail.setItemShortDesc(language.getItemShortDesc());
						}
						break;
					}
				}
			}
			orderItemDetail.setItemTierQty(shoppingCartItem.getTierPrice().getItemTierQty());
			orderItemDetail.setItemTierPrice(shoppingCartItem.getTierPrice().getItemTierPrice());
			orderItemDetail.setItemOrderQty(shoppingCartItem.getItemQty());
			orderItemDetail.setItemDetailAmount(shoppingCartItem.getItemPriceTotal());
			orderItemDetail.setItemDetailDiscountAmount(shoppingCartItem.getItemDiscountAmount());
			setDirty(shoppingCartItem.getItem().getItemSkuCd());
			if (shoppingCartItem.getItem().getItemId() != -1L) {
				orderItemDetail.setItem(item);
			}
			orderItemDetail.setOrderHeader(orderHeader);
			
			Iterator<?> iterator = shoppingCartItem.getItemAttributeInfos().iterator();
			while (iterator.hasNext()) {
				ItemAttributeInfo itemAttributeInfo = (ItemAttributeInfo) iterator.next();
				ItemAttributeDetail itemAttributeDetail = (ItemAttributeDetail) em.find(ItemAttributeDetail.class, itemAttributeInfo.getItemAttribDetailId());
				char customAttribTypeCode = itemAttributeDetail.getCustomAttributeDetail().getCustomAttribute().getCustomAttribTypeCode();
				if (customAttribTypeCode == Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP) {
					continue;
				}
				if (customAttribTypeCode == Constants.CUSTOM_ATTRIBUTE_TYPE_USER_INPUT) {
					continue;
				}
				if (customAttribTypeCode == Constants.CUSTOM_ATTRIBUTE_TYPE_USER_SELECT_DROPDOWN) {
					continue;
				}
				OrderAttributeDetail orderAttributeDetail = new OrderAttributeDetail();
				orderAttributeDetail.setOrderItemDetail(orderItemDetail);
				orderAttributeDetail.setCustomAttributeDetail(itemAttributeDetail.getCustomAttributeDetail());
				if (customAttribTypeCode == Constants.CUSTOM_ATTRIBUTE_TYPE_CUST_SELECT_DROPDOWN) {
					CustomAttributeOption customAttributeOption = (CustomAttributeOption) em.find(CustomAttributeOption.class, itemAttributeInfo.getCustomAttribOptionId());
					orderAttributeDetail.setCustomAttributeOption(customAttributeOption);
				}
				if (customAttribTypeCode == Constants.CUSTOM_ATTRIBUTE_TYPE_CUST_INPUT) {
					orderAttributeDetail.setOrderAttribValue(itemAttributeInfo.getItemAttribDetailValue());
				}
				orderItemDetail.getOrderAttributeDetails().add(orderAttributeDetail);
			}
			
			orderHeader.getOrderItemDetails().add(orderItemDetail);
			
			ItemTax taxes[] = shoppingCartItem.getTaxes();
			for (int i = 0; i < taxes.length; i++) {
				OrderDetailTax orderDetailTax = new OrderDetailTax();
				orderDetailTax.setTaxName(taxes[i].getTax().getTaxLanguage().getTaxName());
				if (!contentSessionKey.isSiteProfileClassDefault()) {
					for (TaxLanguage language : taxes[i].getTax().getTaxLanguages()) {
						if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentSessionKey.getSiteProfileClassId())) {
							if (language.getTaxName() != null) {
								orderDetailTax.setTaxName(language.getTaxName());
							}
							break;
						}
					}
				}
				orderDetailTax.setTaxAmount(taxes[i].getTaxAmount());
				orderDetailTax.setTax(taxes[i].getTax());
				orderDetailTax.setOrderItemDetail(orderItemDetail);
				orderDetailTax.setOrderHeader(orderHeader);
				orderItemDetail.getOrderDetailTaxes().add(orderDetailTax);
				orderHeader.getOrderTaxes().add(orderDetailTax);
			}
		}
		
		Iterator<?> shoppingCartCoupons = shoppingCart.getShoppingCartCoupons().iterator();
		seqNum = 0;
		while (shoppingCartCoupons.hasNext()) {
			ShoppingCartCoupon shoppingCartCoupon = (ShoppingCartCoupon) shoppingCartCoupons.next();
			OrderOtherDetail orderOtherDetail = new OrderOtherDetail();
			Coupon coupon = shoppingCartCoupon.getCoupon();
			orderOtherDetail.setCoupon(coupon);
			orderOtherDetail.setSeqNum(seqNum++);
			orderOtherDetail.setOrderOtherDetailNum(coupon.getCouponCode());
			orderOtherDetail.setOrderOtherDetailDesc(coupon.getCouponLanguage().getCouponName());
			if (!contentSessionKey.isSiteProfileClassDefault()) {
				for (CouponLanguage language : coupon.getCouponLanguages()) {
					if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentSessionKey.getSiteProfileClassId())) {
						if (language.getCouponName() != null) {
							orderOtherDetail.setOrderOtherDetailDesc(language.getCouponName());
						}
						break;
					}
				}
			}
			orderOtherDetail.setOrderOtherDetailAmount(shoppingCartCoupon.getCouponAmount());
			setDirty(coupon);
			orderHeader.getOrderOtherDetails().add(orderOtherDetail);
		}
		
		ItemTax shippingTaxes[] = shoppingCart.getShippingTaxes();
		if (shippingTaxes != null) {
			for (int i = 0; i < shippingTaxes.length; i++) {
				OrderDetailTax orderDetailTax = new OrderDetailTax();
				orderDetailTax.setTaxName(shippingTaxes[i].getTax().getTaxLanguage().getTaxName());
				if (!contentSessionKey.isSiteProfileClassDefault()) {
					for (TaxLanguage language : shippingTaxes[i].getTax().getTaxLanguages()) {
						if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentSessionKey.getSiteProfileClassId())) {
							if (language.getTaxName() != null) {
								orderDetailTax.setTaxName(language.getTaxName());
							}
							break;
						}
					}
				}
				orderDetailTax.setTaxAmount(shippingTaxes[i].getTaxAmount());
				orderDetailTax.setTax(shippingTaxes[i].getTax());
				orderDetailTax.setOrderHeader(orderHeader);
				orderHeader.getOrderTaxes().add(orderDetailTax);
			}
		}
//		orderHeader.setOrderStatus(calcStatus(orderHeader));
	}
	
	public void authorizeOrder(PaymentEngine paymentEngine, HttpServletRequest request) throws AuthorizationException, PaymentException, Exception {
		if (!isOpen(orderHeader)) {
			throw new PaymentException("Order is not open");
		}
		if (!shoppingCart.isCashPaymentOrder()) {
			paymentEngine.setCreditCardInfo(creditCardInfo);
			shoppingCart.getPaymentEngine().authorizePayment(orderHeader, request);
			PaymentTran paymentTran = new PaymentTran();
			paymentTran.setAuthCode(paymentEngine.getAuthCode());
			paymentTran.setPaymentReference1(paymentEngine.getPaymentReference1());
			paymentTran.setPaymentReference2(paymentEngine.getPaymentReference2());
			paymentTran.setPaymentReference3(paymentEngine.getPaymentReference3());
			paymentTran.setPaymentReference4(paymentEngine.getPaymentReference4());
			paymentTran.setPaymentReference5(paymentEngine.getPaymentReference5());
			paymentTran.setTranDatetime(new Date());
			orderHeader.setPaymentTran(paymentTran);
			orderHeader.setPaymentGatewayProvider(paymentEngine.getClass().getSimpleName());
		}
		orderHeader.setOrderStatus(calcStatus(orderHeader));
	}
	
	public void unlockShippingQuote() throws Exception {
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		orderHeader.setShippingValidUntil(null);
		em.persist(orderHeader);
	}
	
	public void saveHeader() throws Exception {
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String userId = user == null ? Constants.USERNAME_SYSTEM : user.getUserId();
		Date current = new Date();
		if (isNew) {
			orderHeader.setOrderNum(generateNumber(orderHeader.getSiteDomain().getSite(), Constants.SEQUENCE_ORDER));
			orderHeader.setRecCreateBy(userId);
			orderHeader.setRecCreateDatetime(current);
			orderHeader.setOrderStatus(Constants.ORDERSTATUS_OPEN);
		}
		orderHeader.setRecUpdateBy(userId);
		orderHeader.setRecUpdateDatetime(current);
		if (orderHeader.getOrderHeaderId() == null) {
			em.persist(orderHeader);
		}
		isNew = false;
	}
	
	public void saveOpenOrder(String step) throws Exception {
		saveOrder();
		orderHeader.setOrderAbundantLoc(step);
		if (step.equals(Constants.ORDER_STEP_SHIPPINGQUOTE)) {
			ContentSessionKey contentSessionKey = shoppingCart.getContentSessionKey();
			String shippingMethodName = Languages.getLangTranValue(contentSessionKey.getLangId(), "content.text.shipping.quote");
			orderHeader.setShippingMethodName(shippingMethodName);
		}
		orderHeader.setOrderStatus(Constants.ORDERSTATUS_OPEN);
	}
	
	public void removeOpenOrder() throws Exception {
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		if (isNew) {
			return;
		}
		
		for (OrderItemDetail orderItemDetail : orderHeader.getOrderItemDetails()) {
			for (OrderAttributeDetail orderAttributeDetail : orderItemDetail.getOrderAttributeDetails()) {
				em.remove(orderAttributeDetail);
			}
			for (OrderDetailTax orderDetailTax : orderItemDetail.getOrderDetailTaxes()) {
				em.remove(orderDetailTax);
			}
			em.remove(orderItemDetail);
		}
		em.remove(orderHeader);
	}
	
	public void saveOrder() throws Exception {
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String userId = user == null ? Constants.USERNAME_SYSTEM : user.getUserId();
		Date current = new Date();
		
		saveHeader();
		orderHeader.setOrderAbundantLoc("");
				
		Iterator<?> iterator = null;
		iterator = orderHeader.getOrderItemDetails().iterator();
		while (iterator.hasNext()) {
			OrderItemDetail orderItemDetail = (OrderItemDetail) iterator.next();
			if (!isDirty(orderItemDetail.getItemSkuCd())) {
				continue;
			}
			if (orderItemDetail.getItem() != null) {
				InventoryEngine engine = new InventoryEngine(orderItemDetail.getItem());
				engine.adjustBookedQty(orderItemDetail.getItemOrderQty());
			}
			if (orderItemDetail.getRecCreateDatetime() == null) {
				orderItemDetail.setRecCreateBy(userId);
				orderItemDetail.setRecCreateDatetime(current);
			}
			orderItemDetail.setRecUpdateBy(userId);
			orderItemDetail.setRecUpdateDatetime(current);
			if (orderItemDetail.getOrderItemDetailId() == null) {
				em.persist(orderItemDetail);
			}
			
			Iterator<?> attributes = orderItemDetail.getOrderAttributeDetails().iterator();
			while (attributes.hasNext()) {
				OrderAttributeDetail orderAttributeDetail = (OrderAttributeDetail) attributes.next();
				if (orderAttributeDetail.getRecCreateDatetime() == null) {
					orderAttributeDetail.setRecCreateBy(userId);
					orderAttributeDetail.setRecCreateDatetime(current);
				}
				orderAttributeDetail.setRecUpdateBy(userId);
				orderAttributeDetail.setRecUpdateDatetime(current);
				if (orderAttributeDetail.getOrderAttribDetailId() == null) {
					em.persist(orderAttributeDetail);
				}
			}
			
			Iterator<?> taxes = orderItemDetail.getOrderDetailTaxes().iterator();
			while (taxes.hasNext()) {
				OrderDetailTax orderDetailTax = (OrderDetailTax) taxes.next();
				if (orderDetailTax.getRecCreateDatetime() == null) {
					orderDetailTax.setRecCreateBy(userId);
					orderDetailTax.setRecCreateDatetime(current);
				}
				orderDetailTax.setRecUpdateBy(userId);
				orderDetailTax.setRecUpdateDatetime(current);
				if (orderDetailTax.getOrderDetailTaxId() == null) {
					em.persist(orderDetailTax);
				}
			}
		}
		
		iterator = orderHeader.getOrderOtherDetails().iterator();
		while (iterator.hasNext()) {
			OrderOtherDetail orderOtherDetail = (OrderOtherDetail) iterator.next();
			if (isDirty(orderOtherDetail.getCoupon())) {
				if (orderOtherDetail.getRecCreateDatetime() == null) {
					orderOtherDetail.setRecCreateBy(userId);
					orderOtherDetail.setRecCreateDatetime(current);
					orderOtherDetail.setOrderHeader(orderHeader);
				}
				orderOtherDetail.setRecUpdateBy(userId);
				orderOtherDetail.setRecUpdateDatetime(current);
				if (orderOtherDetail.getOrderOtherDetailId() == null) {
					em.persist(orderOtherDetail);
				}
			}
		}
		
		PaymentTran paymentTran = orderHeader.getPaymentTran();
		if (paymentTran != null) {
			if (paymentTran.getRecUpdateDatetime() == null) {
				paymentTran.setRecCreateBy(userId);
				paymentTran.setRecCreateDatetime(current);
				paymentTran.setRecUpdateBy(userId);
				paymentTran.setRecUpdateDatetime(current);
				if (paymentTran.getPaymentTranId() == null) {
					em.persist(paymentTran);
				}
			}
		}
		paymentTran = orderHeader.getVoidPaymentTran();
		if (paymentTran != null) {
			if (paymentTran.getRecUpdateDatetime() == null) {
				paymentTran.setRecCreateBy(userId);
				paymentTran.setRecCreateDatetime(current);
				paymentTran.setRecUpdateBy(userId);
				paymentTran.setRecUpdateDatetime(current);
				if (paymentTran.getPaymentTranId() == null) {
					em.persist(paymentTran);
				}
			}
		}
		
		if (orderHeader.getCustAddress() != null) {
			OrderAddress custAddress = orderHeader.getCustAddress();
			if (custAddress.getRecCreateBy() == null) {
				custAddress.setRecCreateBy(userId);
				custAddress.setRecCreateDatetime(current);
			}
			custAddress.setRecUpdateBy(userId);
			custAddress.setRecUpdateDatetime(current);
			if (orderHeader.getCustAddress().getOrderAddressId() == null) {
				em.persist(orderHeader.getCustAddress());
			}
		}
		if (orderHeader.getShippingAddress() != null) {
			OrderAddress shippingAddress = orderHeader.getShippingAddress();
			if (shippingAddress.getRecCreateBy() == null) {
				shippingAddress.setRecCreateBy(userId);
				shippingAddress.setRecCreateDatetime(current);
			}
			shippingAddress.setRecUpdateBy(userId);
			shippingAddress.setRecUpdateDatetime(current);
			if (orderHeader.getShippingAddress().getOrderAddressId() == null) {
				em.persist(orderHeader.getShippingAddress());
			}
		}
		if (orderHeader.getBillingAddress() != null) {
			OrderAddress billingAddress = orderHeader.getBillingAddress();
			if (billingAddress.getRecCreateBy() == null) {
				billingAddress.setRecCreateBy(userId);
				billingAddress.setRecCreateDatetime(current);
			}
			billingAddress.setRecUpdateBy(userId);
			billingAddress.setRecUpdateDatetime(current);
			if (orderHeader.getBillingAddress().getOrderAddressId() == null) {
				em.persist(orderHeader.getBillingAddress());
			}
		}
		
		Iterator<?> taxes = orderHeader.getOrderTaxes().iterator();
		while (taxes.hasNext()) {
			OrderDetailTax orderDetailTax = (OrderDetailTax) taxes.next();
			if (orderDetailTax.getRecCreateDatetime() == null) {
				orderDetailTax.setRecCreateBy(userId);
				orderDetailTax.setRecCreateDatetime(current);
			}
			orderDetailTax.setRecUpdateBy(userId);
			orderDetailTax.setRecUpdateDatetime(current);
			if (orderDetailTax.getOrderDetailTaxId() == null) {
				em.persist(orderDetailTax);
			}
		}
//		orderHeader.setOrderStatus(calcStatus(orderHeader));

		this.clean();
		isNew = false;
	}
	
	public void setQty(Item item, int qty) {
		// To be implemented when order can be modified in the back office.
	}
	
	public void cancelOrder() throws Exception {
		if (isOpen(orderHeader)) {
			throw new PaymentException("Order is not open");
		}
		orderHeader.setOrderStatus(Constants.ORDERSTATUS_CANCELLED);
	}
	
	public void voidOrder() throws Exception {
		if (isCompleted(orderHeader)) {
			throw new PaymentException("Order is not completed");
		}
		orderHeader.setOrderStatus(Constants.ORDERSTATUS_VOIDED);
	}
	
	public void holdOrder() throws Exception {
		orderHeader.setOrderStatus(Constants.ORDERSTATUS_ONHOLD);
	}
	
	public void completeOrder() throws Exception {
		orderHeader.setOrderStatus(Constants.ORDERSTATUS_COMPLETED);
	}

	public void processOrder() throws Exception {
		orderHeader.setOrderStatus(Constants.ORDERSTATUS_PROCESSING);
	}
	
	public OrderHeader getOrderHeader() {
		return orderHeader;
	}

	public void sendCustSaleConfirmEmail(HttpServletRequest request, ServletContext servletContext) throws Exception {
		ContentBean contentBean = ContentLookupDispatchAction.getContentBean(request);
		SiteDomain siteDomain = contentBean.getContentSessionBean().getSiteDomain();
		Long siteProfileClassId = contentBean.getContentSessionKey().getSiteProfileClassId();
		SiteDomainLanguage siteDomainLanguage = contentBean.getContentSessionBean().getSiteDomain().getSiteDomainLanguage();
		if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
			for (SiteDomainLanguage language : contentBean.getContentSessionBean().getSiteDomain().getSiteDomainLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
					siteDomainLanguage = language;
					break;
				}
			}
		}
		SiteDomainParamBean siteDomainParamBean = SiteDomainDAO.getSiteDomainParamBean(contentBean.getContentSessionBean().getSiteDomain().getSiteDomainLanguage(), siteDomainLanguage);
		Site site = siteDomain.getSite();

		TemplateEngine engine = TemplateEngine.getInstance();
		engine.init(request, servletContext, true);
		engine.setParameter("orderHeaderId", orderHeader.getOrderHeaderId());
		String body = engine.mergeData("mail/custSaleConfirmation.vm", "template", engine);
        Mailer mailer = new Mailer(site);

        String mailFrom = siteDomainParamBean.getMailFromCustSales();
        String subject = siteDomainParamBean.getSubjectCustSales();
        String mailTo = orderHeader.getCustEmail();
        if (mailFrom == null) {
        	mailFrom = "";
        }
        if (subject == null) {
        	subject = "";
        }
        if (Format.isNullOrEmpty(mailFrom) || Format.isNullOrEmpty(mailTo)) {
        	logger.error("Unable to send customer sales email");
        	logger.error("mailFrom = " + mailFrom);
        	logger.error("mailTo = " + mailTo);
        	logger.error("subject = " + subject);
        }
        mailer.sendMail(mailFrom, mailTo, subject, body, "text/html");
        
		body = engine.mergeData("mail/adminSaleNotification.vm", "template", engine);
		mailTo = siteDomainParamBean.getCheckoutNotificationEmail();
        mailFrom = siteDomainParamBean.getMailFromNotification();
        subject = siteDomainParamBean.getSubjectNotification();
        if (!Format.isNullOrEmpty(mailFrom) && !Format.isNullOrEmpty(mailTo)) {
        	mailer.sendMail(mailFrom, mailTo, subject, body, "text/html");
        }
	}
	
	public void sendShippingQuoteEmail(ServletContext servletContext) throws Exception {
		SiteDomain siteDomain = orderHeader.getSiteDomain();
		Long siteProfileClassId = orderHeader.getSiteProfile().getSiteProfileClass().getSiteProfileClassId();
		SiteDomainLanguage siteDomainLanguage = siteDomain.getSiteDomainLanguage();
		if (siteProfileClassId.equals(siteDomain.getSiteDomainLanguage().getSiteProfileClass().getSiteProfileClassId())) {
			for (SiteDomainLanguage language : siteDomain.getSiteDomainLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
					siteDomainLanguage = language;
					break;
				}
			}
		}
		SiteDomainParamBean siteDomainParamBean = SiteDomainDAO.getSiteDomainParamBean(siteDomain.getSiteDomainLanguage(), siteDomainLanguage);
		Site site = siteDomain.getSite();
		
		TemplateEngine engine = TemplateEngine.getInstance();
		engine.init(orderHeader.getSiteDomain(), 
					orderHeader.getSiteProfile(), 
					orderHeader.getSiteCurrency(),
					servletContext);
		engine.setParameter("orderHeaderId", orderHeader.getOrderHeaderId());
		String body = engine.mergeData("mail/shippingQuote.vm", "template", engine);
        Mailer mailer = new Mailer(site);

        String mailFrom = siteDomainParamBean.getMailFromShippingQuote();
        String subject = siteDomainParamBean.getSubjectShippingQuote();
        String mailTo = orderHeader.getCustEmail();
        if (mailFrom == null) {
        	mailFrom = "";
        }
        if (subject == null) {
        	subject = "";
        }
        if (Format.isNullOrEmpty(mailFrom) || Format.isNullOrEmpty(mailTo)) {
        	logger.error("Unable to send customer sales email");
        	logger.error("mailFrom = " + mailFrom);
        	logger.error("mailTo = " + mailTo);
        	logger.error("subject = " + subject);
        }
        mailer.sendMail(mailFrom, mailTo, subject, body, "text/html");
	}
	
	public Float getOrderPriceTotal() {
		float total = 0;
		Iterator<?> iterator = orderHeader.getOrderItemDetails().iterator();
		while (iterator.hasNext()) {
			OrderItemDetail orderItemDetail = (OrderItemDetail) iterator.next();
			total += orderItemDetail.getItemDetailAmount().floatValue();
		}
		return total;
	}
	
	public Float getOrderPriceDiscountTotal() {
		float total = 0;
		Iterator<?> iterator = orderHeader.getOrderItemDetails().iterator();
		while (iterator.hasNext()) {
			OrderItemDetail orderItemDetail = (OrderItemDetail) iterator.next();
			total += orderItemDetail.getItemDetailDiscountAmount().floatValue();
		}
		return total;
	}

	public Float getOrderSubTotal() {
		float total = getOrderPriceTotal();
		total -= getOrderPriceDiscountTotal();
		return total;
	}
	
	public Float getOrderShippingTotal() {
		float total = 0;
		total += orderHeader.getShippingTotal();
		total -= orderHeader.getShippingDiscountTotal();
		return total;
	}
	
	public Vector<?> getOrderTaxes() {
/*
		Vector<?> orderDetailTaxes = new Vector();
		Iterator<?> iterator = orderHeader.getOrderItemDetails().iterator();
		while (iterator.hasNext()) {
			OrderItemDetail orderItemDetail = (OrderItemDetail) iterator.next();
			Iterator<?> taxIterator<?> = orderItemDetail.getOrderDetailTaxes().iterator();
			while (taxIterator.hasNext()) {
				OrderDetailTax orderDetailTax = (OrderDetailTax) taxIterator.next();
				boolean found = false;
				Iterator<?> sumIterator<?> = orderDetailTaxes.iterator();
				OrderDetailTax sumTax = null;
				while (sumIterator.hasNext()) {
					sumTax = (OrderDetailTax) sumIterator.next();
					if (sumTax.getTaxName().equals(orderDetailTax.getTaxName())) {
						found = true;
						break;
					}	
				}
				if (!found) {
					sumTax = new OrderDetailTax();
					sumTax.setTaxName(orderDetailTax.getTaxName());
					sumTax.setTaxAmount(Float.valueOf(0));
					orderDetailTaxes.add(sumTax);
				}
				float taxAmount = sumTax.getTaxAmount();
				taxAmount += orderDetailTax.getTaxAmount();
				sumTax.setTaxAmount(taxAmount);
			}
		}
*/
		Vector<OrderDetailTax> orderDetailTaxes = new Vector<OrderDetailTax>();
		Iterator<?> iterator = orderHeader.getOrderTaxes().iterator();
		while (iterator.hasNext()) {
			OrderDetailTax orderDetailTax = (OrderDetailTax) iterator.next();
			boolean found = false;
			Iterator<?> sumIterator = orderDetailTaxes.iterator();
			OrderDetailTax sumTax = null;
			while (sumIterator.hasNext()) {
				sumTax = (OrderDetailTax) sumIterator.next();
				if (sumTax.getTaxName().equals(orderDetailTax.getTaxName())) {
					found = true;
					break;
				}	
			}
			if (!found) {
				sumTax = new OrderDetailTax();
				sumTax.setTaxName(orderDetailTax.getTaxName());
				sumTax.setTaxAmount(Float.valueOf(0));
				sumTax.setTax(orderDetailTax.getTax());
				orderDetailTaxes.add(sumTax);
			}
			float taxAmount = sumTax.getTaxAmount();
			taxAmount += orderDetailTax.getTaxAmount();
			sumTax.setTaxAmount(taxAmount);
		}
		
		return orderDetailTaxes;
	}
	
	public Float getOrderTaxTotal() {
		float total = 0;
		Iterator<?> iterator = orderHeader.getOrderTaxes().iterator();
		while (iterator.hasNext()) {
			OrderDetailTax orderDetailTax = (OrderDetailTax) iterator.next();
			total += orderDetailTax.getTaxAmount();
		}
		return total;
	}
	
	public OrderHeader clone(OrderHeader header) {
		OrderHeader orderHeader = new OrderHeader();
		orderHeader.setOrderHeaderId(header.getOrderHeaderId());
		orderHeader.setOrderNum(header.getOrderNum());
		orderHeader.setCustEmail(header.getCustEmail());
		orderHeader.setShippingTotal(header.getShippingTotal());
		orderHeader.setShippingDiscountTotal(header.getShippingDiscountTotal());
		orderHeader.setOrderTotal(header.getOrderTotal());
		orderHeader.setPaymentGatewayProvider(header.getPaymentGatewayProvider());
		orderHeader.setCreditCardDesc(header.getCreditCardDesc());
		orderHeader.setCustCreditCardNum(header.getCustCreditCardNum());
		orderHeader.setShippingMethodName(header.getShippingMethodName());
		orderHeader.setOrderStatus(header.getOrderStatus());
		orderHeader.setOrderDate(header.getOrderDate());
		orderHeader.setRecUpdateBy(header.getRecUpdateBy());
		orderHeader.setRecUpdateDatetime(header.getRecUpdateDatetime());
		orderHeader.setRecCreateBy(header.getRecCreateBy());
		orderHeader.setRecCreateDatetime(header.getRecCreateDatetime());
		orderHeader.setSiteDomain(header.getSiteDomain());
		orderHeader.setSiteProfile(header.getSiteProfile());
		orderHeader.setSiteCurrency(header.getSiteCurrency());
		
		return orderHeader;
		
	}
}