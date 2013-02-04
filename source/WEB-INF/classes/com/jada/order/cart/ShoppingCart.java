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

package com.jada.order.cart;

import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.content.ContentBean;
import com.jada.content.ContentLookupDispatchAction;
import com.jada.content.ContentSessionKey;
import com.jada.content.checkout.ItemNotAvailiableException;
import com.jada.dao.CountryDAO;
import com.jada.dao.OrderHeaderDAO;
import com.jada.dao.StateDAO;
import com.jada.order.payment.PaymentEngine;
import com.jada.order.payment.PaymentManager;
import com.jada.order.payment.gateway.PayPalEngine;
import com.jada.order.payment.gateway.PayPalWebsitePaymentProHostedEngine;
import com.jada.inventory.InventoryEngine;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.Coupon;
import com.jada.jpa.entity.CustomAttribute;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.CustomerAddress;
import com.jada.jpa.entity.CustomerClass;
import com.jada.jpa.entity.CustomerCreditCard;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.ItemAttributeDetail;
import com.jada.jpa.entity.OrderAttributeDetail;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.OrderItemDetail;
import com.jada.jpa.entity.PaymentGateway;
import com.jada.jpa.entity.ShippingMethod;
import com.jada.jpa.entity.ShippingMethodRegion;
import com.jada.jpa.entity.ShippingMethodRegionType;
import com.jada.jpa.entity.ShippingRate;
import com.jada.jpa.entity.ShippingRateCurrency;
import com.jada.jpa.entity.ShippingRegion;
import com.jada.jpa.entity.ShippingRegionZip;
import com.jada.jpa.entity.ShippingType;
import com.jada.jpa.entity.SiteCurrency;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.SiteProfile;
import com.jada.jpa.entity.State;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

public class ShoppingCart {
	boolean recalcRequired;
	String requestId;
	PaymentEngine paymentEngine = null;
	boolean payPalWebsitePaymentProHosted;
	boolean payPalWebsitePaymentProHostedOrder;
	boolean payPal;
	boolean payPalOrder;
	boolean creditCard;
	boolean creditCardOrder;
	boolean cashPayment;
	boolean cashPaymentOrder;
	SiteDomain siteDomain;
	SiteCurrency siteCurrency;
	SiteProfile siteProfile;
	boolean newCustomer;
	Customer customer;
	CustomerClass customerClass;
	CustomerAddress custAddress;
	CustomerAddress shippingAddress;
	CustomerAddress billingAddress;
	boolean useBillingAddress;
	CustomerAddress estimateAddress;
	CustomerCreditCard customerCreditCard;
	ShippingMethod shippingMethod;
	boolean activeCart;
	float shippingTotal;
	float shippingDiscountTotal;
	float shippingOrderTotal;
	ItemTax shippingTaxes[];
	float taxTotal;
	int itemCount;
	float priceTotal;
	float orderTotal;
	Vector<ShoppingCartItem> shoppingCartItems = new Vector<ShoppingCartItem>();
	Vector<ShoppingCartCoupon> shoppingCartCoupons = new Vector<ShoppingCartCoupon>();
	Vector<ShoppingCartCoupon> removedCoupons = new Vector<ShoppingCartCoupon>();
	PriceCalculator priceCalculator = null;
	TaxCalculator taxCalculator = null;
	CurrencyConverter currencyConverter = null;
    Logger logger = Logger.getLogger(ShoppingCart.class);
    ContentSessionKey contentSessionKey = null;
    String orderNum;
    boolean shippingPickUp;
    boolean includeShippingPickUp;
    boolean shippingQuoteLock;
    boolean estimatePickUp;
    int checkoutSteps;
/*
	static public ShoppingCart getSessionInstance(HttpServletRequest request) throws Exception {
		HttpSession em = request.getSession();
		ShoppingCart shoppingCart = (ShoppingCart) em.getAttribute("shoppingCart");
		if (shoppingCart == null) {
			ContentBean contentBean = ContentLookupDispatchAction.getContentBean(request);
			shoppingCart = new ShoppingCart(contentBean);
			Customer customer = ContentLookupDispatchAction.getCustomer(request);
			if (customer != null) {
				shoppingCart.initCustomer(customer, contentBean);
			}
			em.setAttribute("shoppingCart", shoppingCart);
		}
		else {
			String requestId = ContentLookupDispatchAction.getRequestId(request);
			if (!requestId.equals(shoppingCart.getRequestId())) {
				shoppingCart.attachHibernateSession(request);
			}
		}
		return shoppingCart;
	}
*/
	static public ShoppingCart getSessionInstance(HttpServletRequest request, boolean create) throws Exception {
		HttpSession em = request.getSession();
		ShoppingCart shoppingCart = (ShoppingCart) em.getAttribute("shoppingCart");
		ContentBean contentBean = ContentLookupDispatchAction.getContentBean(request);
		if (shoppingCart == null) {
			if (!create) {
				return null;
			}
			
			shoppingCart = new ShoppingCart(contentBean);
			Customer customer = ContentLookupDispatchAction.getCustomer(request);
			if (customer != null) {
				shoppingCart.initCustomer(customer, contentBean);
			}
			
			PaymentEngine engine = PaymentManager.getPaymentEngine(contentBean.getContentSessionBean().getSiteCurrency(), request);
			if (engine != null) {
				shoppingCart.setPaymentEngine(engine);
			}
			else {
				shoppingCart.setCashPayment(true);
			}
			em.setAttribute("shoppingCart", shoppingCart);
		}
		else {
			String requestId = ContentLookupDispatchAction.getRequestId(request);
			if (!requestId.equals(shoppingCart.getRequestId())) {
				shoppingCart.init(contentBean);
				shoppingCart.attachHibernateSession(request);
			}
		}
		return shoppingCart;
	}

	static public void remove(HttpServletRequest request) {
		HttpSession em = request.getSession();
		em.removeAttribute("shoppingCart");
	}
	
    public ShoppingCart(ContentBean contentBean) throws Exception {
    	init(contentBean);
    }
    
    public void init(ContentBean contentBean) throws Exception {
    	this.requestId = "";
    	this.contentSessionKey = contentBean.getContentSessionKey();
    	this.siteDomain = contentBean.getSiteDomain();
    	this.siteCurrency = contentBean.getContentSessionBean().getSiteCurrency();
    	this.siteProfile = contentBean.getContentSessionBean().getSiteProfile();
    	
		payPal = false;
		payPalWebsitePaymentProHosted = false;
		creditCard = false;
		if (siteCurrency.getPayPalPaymentGateway() != null) {
			PaymentGateway gateway = siteCurrency.getPayPalPaymentGateway();
			if (gateway.getPaymentGatewayProvider().equals(PayPalEngine.class.getSimpleName())) {
				payPal = true;
			}
		}
		if (siteCurrency.getPaymentGateway() != null) {
			PaymentGateway gateway = siteCurrency.getPaymentGateway();
			if (gateway.getPaymentGatewayProvider().equals(PayPalWebsitePaymentProHostedEngine.class.getSimpleName())) {
				payPalWebsitePaymentProHosted = true;
			}
		}
		if (siteCurrency.getPaymentGateway() != null) {
			creditCard = true;
		}
		if (siteCurrency.getCashPayment() == Constants.VALUE_YES) {
			cashPayment = true;
		}
		
		priceCalculator = new PriceCalculator(contentBean, null);
		taxCalculator = new TaxCalculator(null, null, contentBean);
		currencyConverter = new CurrencyConverter(siteCurrency);
    }

    public void attachHibernateSession(HttpServletRequest request) throws Exception {
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        synchronized (this) {
	        ContentBean contentBean = ContentLookupDispatchAction.getContentBean(request);
	    	if (customer != null) {
	    		customer = (Customer) em.find(Customer.class, customer.getCustId());
	    		customerClass = customer.getCustomerClass();
	    	}
	    	if (customerCreditCard != null) {
	    		customerCreditCard = (CustomerCreditCard) em.find(CustomerCreditCard.class, customerCreditCard.getCustCreditCardId());
	    	}
	    	if (shippingMethod != null) {
	    		shippingMethod = (ShippingMethod) em.find(ShippingMethod.class, shippingMethod.getShippingMethodId());
	    	}
	    	if (siteCurrency != null) {
	    		siteCurrency = (SiteCurrency) em.find(SiteCurrency.class, siteCurrency.getSiteCurrencyId());
	    	}
	    	Iterator<?> iterator = null;
	    	iterator = shoppingCartItems.iterator();
	    	while (iterator.hasNext()) {
	    		ShoppingCartItem shoppingCartItem = (ShoppingCartItem) iterator.next();
	    		Item item = shoppingCartItem.getItem();
	    		if (item.getItemId() < 0) {
	    			continue;
	    		}
	    		item = (Item) em.merge(shoppingCartItem.getItem());
	    		shoppingCartItem.setItem(item);
	    	}
	    	iterator = shoppingCartCoupons.iterator();
	    	while (iterator.hasNext()) {
	    		ShoppingCartCoupon shoppingCartCoupon = (ShoppingCartCoupon) iterator.next();
	    		Coupon coupon = (Coupon) em.find(Coupon.class, shoppingCartCoupon.getCoupon().getCouponId());
	    		shoppingCartCoupon.setCoupon(coupon);
	    	}
	
	    	requestId = ContentLookupDispatchAction.getRequestId(request);
	
			priceCalculator = new PriceCalculator(ContentLookupDispatchAction.getContentBean(request), null);
			taxCalculator = new TaxCalculator(getEffectiveShippingAddress(), customerClass, contentBean);
        }
    }

	public void initCustomer(Customer customer, ContentBean contentBean) throws Exception {
		this.customer = customer;
		customer.getCustAddress().getCustPrefix();
		this.customerClass = customer.getCustomerClass();
        Iterator<?> custAddresses = customer.getCustAddresses().iterator();
        while (custAddresses.hasNext()) {
        	CustomerAddress customerAddress = (CustomerAddress) custAddresses.next();
			if (customerAddress.getCustAddressType().equals(Constants.CUSTOMER_ADDRESS_CUST)) {
				this.custAddress = customerAddress;
				this.custAddress.getCustAddressLine1();
			}
			if (customerAddress.getCustAddressType().equals(Constants.CUSTOMER_ADDRESS_SHIPPING)) {
				this.shippingAddress = customerAddress;
				this.shippingAddress.getCustAddressLine1();
			}
			if (customerAddress.getCustAddressType().equals(Constants.CUSTOMER_ADDRESS_BILLING)) {
				this.billingAddress = customerAddress;
				this.billingAddress.getCustAddressLine1();
			}
        }
        if (cashPaymentOrder) {
        	customerCreditCard = null;
        }
        else {
	        Iterator<?> custCreditCards = customer.getCustCreditCards().iterator();
	        if (custCreditCards.hasNext()) {
	        	customerCreditCard = (CustomerCreditCard) custCreditCards.next();
	        	customerCreditCard.getCreditCard().getCreditCardDesc();
	        }
        }
		priceCalculator = new PriceCalculator(contentBean, customer);
		taxCalculator = new TaxCalculator(getEffectiveShippingAddress(), customerClass, contentBean);
		this.activeCart = true;
	}
	
	public OrderHeader locateAbundentOrder() throws Exception {
		OrderHeader orderHeader = null;
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String sql = "from   OrderHeader orderHeader " +
					 "where  orderHeader.siteDomain.site.siteId = :siteId " +
					 "and    orderHeader.customer.custId = :custId " +
					 "and    orderHeader.orderStatus = '" + Constants.ORDERSTATUS_OPEN + "' " +
					 "and    orderHeader.shippingValidUntil is null " +
					 "order  by orderHeader.orderDate desc";
		Query query = em.createQuery(sql);
		query.setParameter("siteId", siteDomain.getSite().getSiteId());
		query.setParameter("custId", customer.getCustId());
		Iterator<?> iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			orderHeader = (OrderHeader) iterator.next();
			break;
		}
		return orderHeader;
	}
	
	public void mergeOrder(Long orderHeaderId, ContentBean contentBean) throws SecurityException, Exception {
		OrderHeader orderHeader = OrderHeaderDAO.load(siteDomain.getSite().getSiteId(), orderHeaderId);
		
		if (orderHeader.getShippingValidUntil() != null) {
			this.shippingQuoteLock = true;
			this.shippingOrderTotal = orderHeader.getShippingTotal();
			this.getShoppingCartItems().clear();
			this.getShoppingCartCoupons().clear();
		}
		
		for (OrderItemDetail orderItemDetail : orderHeader.getOrderItemDetails()) {
			Item item = orderItemDetail.getItem();
			// Do not merge order when using external data
			if (item == null) {
				continue;
			}
			Vector<ItemAttributeInfo> itemAttributeInfos = new Vector<ItemAttributeInfo>();
			for (OrderAttributeDetail orderAttributeDetail : orderItemDetail.getOrderAttributeDetails()) {
				ItemAttributeInfo itemAttributeInfo = new ItemAttributeInfo();
				itemAttributeInfo.setCustomAttribTypeCode(String.valueOf(orderAttributeDetail.getCustomAttributeDetail().getCustomAttribute().getCustomAttribTypeCode()));
				if (orderAttributeDetail.getCustomAttributeDetail() != null) {
					if (orderAttributeDetail.getCustomAttributeOption() != null) {
						itemAttributeInfo.setCustomAttribOptionId(orderAttributeDetail.getCustomAttributeOption().getCustomAttribOptionId());
					}
					for (ItemAttributeDetail itemAttributeDetail : item.getItemAttributeDetails()) {
						if (itemAttributeDetail.getCustomAttributeDetail().getCustomAttribDetailId().equals(orderAttributeDetail.getCustomAttributeDetail().getCustomAttribDetailId())) {
							itemAttributeInfo.setItemAttribDetailId(itemAttributeDetail.getItemAttribDetailId());
							itemAttributeInfo.setItemAttribDetailValue(orderAttributeDetail.getOrderAttribValue());
							break;
						}
					}
				}
				itemAttributeInfos.add(itemAttributeInfo);
			}
			if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_SKU)) {
				for (ItemAttributeDetail itemAttributeDetail : item.getItemAttributeDetails()) {
    				CustomAttribute customAttribute = itemAttributeDetail.getCustomAttributeDetail().getCustomAttribute();
    				if (customAttribute.getCustomAttribTypeCode() != Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP) {
    					continue;
    				}
    				ItemAttributeInfo itemAttributeInfo = new ItemAttributeInfo();
    				itemAttributeInfo.setCustomAttribTypeCode(String.valueOf(customAttribute.getCustomAttribTypeCode()));
    				itemAttributeInfo.setCustomAttribOptionId(itemAttributeDetail.getCustomAttributeOption().getCustomAttribOptionId());
    				itemAttributeInfo.setItemAttribDetailId(itemAttributeDetail.getItemAttribDetailId());
    				itemAttributeInfos.add(itemAttributeInfo);
				}
			}
			if (!isExist(orderItemDetail.getItem())) {
				try {
					this.setItemQty(orderItemDetail.getItem(), orderItemDetail.getItemOrderQty(), itemAttributeInfos, contentBean, false);
				}
				catch (ItemNotAvailiableException e) {
				}
			}
		}
		this.orderNum = orderHeader.getOrderNum();
		
		if (!cashPaymentOrder && paymentEngine == null) {
			if (orderHeader.getPaymentGatewayProvider() == null) {
				cashPayment = true;
			}
			else {
				this.paymentEngine = PaymentManager.getPaymentEngine(orderHeader.getPaymentGatewayProvider(), orderHeader.getSiteCurrency());
			}
		}
	}
	
	public boolean isExist(Item item) {
		Iterator<?> iterator = shoppingCartItems.iterator();
		while (iterator.hasNext()) {
			ShoppingCartItem scItem = (ShoppingCartItem) iterator.next();
			if (scItem.getItem().getItemNaturalKey().equals(item.getItemNaturalKey())) {
				return true;
			}
		}
		return false;
	}
	
	public void setItemQty(Item item, int quantity, Vector<?> itemAttributeInfos, ContentBean contentBean, boolean isUpdate) throws Exception {
		if (quantity == 0) {
			removeItem(item.getItemNaturalKey(), contentBean);
			return;
		}

		boolean found = false;
		ShoppingCartItem scItem = null;
		int totalQuantity = quantity;
		Iterator<?> iterator = shoppingCartItems.iterator();
		while (iterator.hasNext()) {
			scItem = (ShoppingCartItem) iterator.next();
			if (scItem.getItem().getItemNaturalKey().equals(item.getItemNaturalKey())) {
				found = true;
				if (!isUpdate) {
					totalQuantity += scItem.getItemQty();
				}
				break;
			}
		}
		
		InventoryEngine engine = new InventoryEngine(item);
		if (!engine.isAvailable(totalQuantity)) {
			throw new ItemNotAvailiableException();
		}
		
		if (!found) {
			scItem = new ShoppingCartItem();
			scItem.setItem(item);
			shoppingCartItems.add(scItem);
		}

		if (!isUpdate) {
			scItem.setItemAttributeInfos(itemAttributeInfos);
		}
		scItem.setItemQty(totalQuantity);
		recalculate(contentBean);
	}
	
	public void removeItem(String itemNaturalKey, ContentBean contentBean) throws Exception {
		Iterator<?> iterator = shoppingCartItems.iterator();
		while (iterator.hasNext()) {
			ShoppingCartItem scItem = (ShoppingCartItem) iterator.next();
			if (scItem.getItem().getItemNaturalKey().equals(itemNaturalKey)) {
				iterator.remove();
				break;
			}
		}
		recalculate(contentBean);
	}
	
	public void removeCoupon(Long couponId, ContentBean contentBean) throws Exception {
		Iterator<?> iterator = shoppingCartCoupons.iterator();
		while (iterator.hasNext()) {
			ShoppingCartCoupon scCoupon = (ShoppingCartCoupon) iterator.next();
			if (scCoupon.getCoupon().getCouponId().equals(couponId)) {
				iterator.remove();
				break;
			}
		}
		recalculate(contentBean);
	}
	
	public void setEstimateAddress(HttpServletRequest request, String countryCode, String stateCode, String zipCode, boolean pickUp) throws SecurityException, Exception {
		estimateAddress = new CustomerAddress();
		estimateAddress.setCustCountryCode(countryCode);
		estimateAddress.setCountry(CountryDAO.loadByCountryCode(contentSessionKey.getSiteId(), countryCode));
		estimateAddress.setCustStateCode(stateCode);
		estimateAddress.setState(StateDAO.loadByStateCode(contentSessionKey.getSiteId(), stateCode));
		estimateAddress.setCustZipCode(zipCode);
		setEstimatePickUp(pickUp);
		
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String sql = "from CustomerClass customerClass where siteId = :siteId and customerClass.systemRecord = 'Y'";
    	Query query = em.createQuery(sql);
    	query.setParameter("siteId", contentSessionKey.getSiteId());
    	customerClass = (CustomerClass) query.getSingleResult();
    	
		taxCalculator = new TaxCalculator(getEffectiveShippingAddress(), customerClass, ContentLookupDispatchAction.getContentBean(request));
	}
	
	private CustomerAddress getEffectiveShippingAddress() {
		if (customer == null) {
			return estimateAddress;
		}
		
		if (shippingAddress == null) {
			return null;
		}
		
		CustomerAddress address = shippingAddress;
		if (address.getCustUseAddress().equals(Constants.CUST_ADDRESS_USE_BILL)) {
			address = billingAddress;
		}
		if (address.getCustUseAddress().equals(Constants.CUST_ADDRESS_USE_CUST)) {
			address = custAddress;
		}
		return address;
	}
	
	public Vector<?> getShippingMethods() throws Exception {
		Vector<ShippingMethod> shippingMethods = new Vector<ShippingMethod>();
		CustomerAddress address = getEffectiveShippingAddress();
		if (address == null) {
			return shippingMethods;
		}
		
		Long countryId = null;
		if (address.getCountry() != null) {
			countryId = address.getCountry().getCountryId();
		}
		Long stateId = null;
		if (address.getState() != null) {
			stateId = address.getState().getStateId();
		}
		
		Vector<ShippingRegion> matchedRegion = new Vector<ShippingRegion>();
		
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Query query = em.createQuery("from   ShippingRegion " +
										  "where  siteId = :siteId ");
		query.setParameter("siteId", contentSessionKey.getSiteId());
        Iterator<?> iterator = query.getResultList().iterator();
        ShippingRegion defaultRegion = null;
        while (iterator.hasNext()) {
        	ShippingRegion shippingRegion = (ShippingRegion) iterator.next();
        	if (shippingRegion.getPublished() == Constants.VALUE_NO) {
        		continue;
        	}
        	if (shippingRegion.getSystemRecord() == Constants.VALUE_YES) {
    			if (shippingRegion.getPublished() == Constants.VALUE_YES) {
    				defaultRegion = shippingRegion;
    				continue;
    			}
    		}
        	
        	boolean isFound = false;
    		Iterator<?> countries = shippingRegion.getCountries().iterator();
    		while (countries.hasNext()) {
    			Country country = (Country) countries.next();
    			if (country.getCountryId().equals(countryId)) {
    				isFound = true;
    			}
    		}
    		if (!isFound) {
        		Iterator<?> states = shippingRegion.getStates().iterator();
        		while (states.hasNext()) {
        			State state = (State) states.next();
        			if (state.getStateId().equals(stateId)) {
        				isFound = true;
        			}
        		}
    		}
        	String zipCode = address.getCustZipCode();
        	if (!isFound && !Format.isNullOrEmpty(zipCode)) {
        		for (ShippingRegionZip shippingRegionZip : shippingRegion.getZipCodes()) {
        			if (shippingRegionZip.getZipCodeExpression() == Constants.VALUE_YES) {
        				if (zipCode.matches(shippingRegionZip.getZipCodeStart())) {
        					isFound = true;
        					break;
        				}
        			}
        			else {
            			if (zipCode.compareTo(shippingRegionZip.getZipCodeStart()) >= 0 &&
            				zipCode.compareTo(shippingRegionZip.getZipCodeEnd()) <= 0) {
            				isFound = true;
            				break;
            			}
        			}
        		}
        	}
    		if (isFound) {
    			matchedRegion.add(shippingRegion);
    		}
        }
        if (matchedRegion.size() == 0 && defaultRegion == null) {
            logger.debug("No shipping region (including default) can be found");
        	return shippingMethods;
        }
        if (matchedRegion.size() == 0) {
            logger.debug("Use default shipping region");
            if (defaultRegion == null) {
            	logger.debug("Default shipping region does not exist or turned off.");
            }
            matchedRegion.add(defaultRegion);
        }

        query = em.createQuery("from   ShippingMethod " + 
        		                    "where  siteId = :siteId " +
        		                    "and    published = :published " +
        		                    "order  by seqNum");
        query.setParameter("siteId", contentSessionKey.getSiteId());
        query.setParameter("published", String.valueOf(Constants.PUBLISHED_YES));
        iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	ShippingMethod shippingMethod = (ShippingMethod) iterator.next();
        	
        	Iterator<?> shippingMethodRegions = shippingMethod.getShippingMethodRegions().iterator();
        	boolean found = false;
        	boolean doNotShip = false;
        	while (shippingMethodRegions.hasNext()) {
        		ShippingMethodRegion shippingMethodRegion = (ShippingMethodRegion) shippingMethodRegions.next();
        		Long shippingRegionId = shippingMethodRegion.getShippingRegion().getShippingRegionId();
        		Iterator<ShippingRegion> matchedRegionIterator = matchedRegion.iterator();
        		
        		while (matchedRegionIterator.hasNext()) {
        			ShippingRegion matchedShippingRegion = (ShippingRegion) matchedRegionIterator.next();
        			if (!matchedShippingRegion.getShippingRegionId().equals(shippingRegionId)) {
        				continue;
        			}
        			found = true;
        			if (shippingMethodRegion.getPublished() == Constants.VALUE_NO) {
        				doNotShip = true;
        				break;
        			}
        		}
        		if (doNotShip) {
        			break;
        		}
        	}
        	if (found) {
        		if (!doNotShip) {
        			shippingMethods.add(shippingMethod);
        		}
        	}
        } 
		return shippingMethods;
	}
	
	public void calculateShipping() throws Exception {
		if (shippingQuoteLock) {
			return;
		}
		
		if (isShippingPickUp()) {
			Iterator<?> iterator = shoppingCartItems.iterator();
        	while (iterator.hasNext()) {
        		ShoppingCartItem shoppingCartItem = (ShoppingCartItem) iterator.next();
        		shoppingCartItem.setItemShippingDiscountFee(0);
        		shoppingCartItem.setItemShippingFee(0);
        	}
        	
			shippingTotal = 0;
			shippingDiscountTotal = 0;
			shippingOrderTotal = 0;
			
			return;
		}
		
		Vector<?> shippingMethods = getShippingMethods();
    	if (shippingMethod != null) {
    		Enumeration<?> enumeration = shippingMethods.elements();
    		boolean found = false;
    		while (enumeration.hasMoreElements()) {
    			ShippingMethod sm = (ShippingMethod) enumeration.nextElement();
    			if (sm.getShippingMethodId().equals(shippingMethod.getShippingMethodId())) {
    				found = true;
    				break;
    			}
    		}
    		// This shipping method is no longer valid, customer might have changed address.
    		if (!found) {
    	    	logger.debug("Invalidating shipping method");
    			shippingMethod = null;
    		}
    	}
    	// New order or the shipping method just got invalidated.  Extract from list as default.  Use the cheapest shipping method for better customer experience.
    	if (shippingMethod == null) {
    		if (shippingMethods.size() > 0) {
    			Iterator<?> iterator = shippingMethods.iterator();
    			float lowestTotal = Float.MAX_VALUE;
    			while (iterator.hasNext()) {
    				ShippingMethod sm = (ShippingMethod) iterator.next();
    				float tmpTotal = calculateShippingTotal(sm);
    				if (lowestTotal > tmpTotal) {
    					lowestTotal = tmpTotal;
    					shippingMethod = sm;
    				}
    			}
    		}
    	}
		// Found shipping method that can be used to ship items to this customer.  Recalculate shipping cost.
    	shippingTotal = 0;
    	shippingOrderTotal = 0;
        if (shippingMethod != null) {
        	calculateShippingTotal(shippingMethod);
        }
        else {
//        	this.setShippingPickUp(true);
			Iterator<?> iterator = shoppingCartItems.iterator();
        	while (iterator.hasNext()) {
        		ShoppingCartItem shoppingCartItem = (ShoppingCartItem) iterator.next();
        		shoppingCartItem.setItemShippingDiscountFee(0);
        		shoppingCartItem.setItemShippingFee(0);
        	}
			shippingTotal = 0;
        }
        shippingOrderTotal = shippingTotal - shippingDiscountTotal;
	}
	
	private float calculateShippingTotal(ShippingMethod shippingMethod) {
		CustomerAddress address = getEffectiveShippingAddress();
    	Iterator<?> iterator = shippingMethod.getShippingMethodRegions().iterator();
    	ShippingMethodRegion shippingMethodRegion = null;
    	boolean found = false; 
    	while (iterator.hasNext() && !found) {
    		ShippingMethodRegion smRegion = (ShippingMethodRegion) iterator.next();
    		if (smRegion.getPublished() == Constants.PUBLISHED_NO) {
    			continue;
    		}
    		if (smRegion.getShippingRegion().getPublished() == Constants.PUBLISHED_NO) {
    			continue;
    		}
    		if (smRegion.getShippingRegion().getSystemRecord() == Constants.VALUE_YES) {
    			shippingMethodRegion = smRegion;
    		}
    		Iterator<?> countries = smRegion.getShippingRegion().getCountries().iterator();
    		while (countries.hasNext()) {
    			Country country = (Country) countries.next();
    			if (country.getCountryId().equals(address.getCountry().getCountryId())) {
    				shippingMethodRegion = smRegion;
    				found = true;
    				break;
    			}
    		}
    		if (!found) {
        		Iterator<?> states = smRegion.getShippingRegion().getStates().iterator();
        		while (states.hasNext()) {
        			State state = (State) states.next();
        			if (state.getStateId().equals(address.getState().getStateId())) {
        				shippingMethodRegion = smRegion;
        				found = true;
        				break;
        			}
        		}
    		}
        	String zipCode = address.getCustZipCode();
        	if (!found && !Format.isNullOrEmpty(zipCode)) {
        		for (ShippingRegionZip shippingRegionZip : smRegion.getShippingRegion().getZipCodes()) {
        			if (shippingRegionZip.getZipCodeExpression() == Constants.VALUE_YES) {
        				if (zipCode.matches(shippingRegionZip.getZipCodeStart())) {
            				shippingMethodRegion = smRegion;
        					found = true;
        					break;
        				}
        			}
        			else {
            			if (zipCode.compareTo(shippingRegionZip.getZipCodeStart()) >= 0 &&
            				zipCode.compareTo(shippingRegionZip.getZipCodeEnd()) <= 0) {
            				shippingMethodRegion = smRegion;
            				found = true;
            				break;
            			}
        			}
        		}
        	}
    	}
    	// Calculate shipping amount per line item
    	if (shippingMethodRegion != null) {
        	iterator = shoppingCartItems.iterator();
        	while (iterator.hasNext()) {
        		ShoppingCartItem shoppingCartItem = (ShoppingCartItem) iterator.next();
        		Item item = shoppingCartItem.getItem();
        		logger.debug("Calculating shipping for item " + item.getItemId() + " " + item.getItemNum());
        		ShippingType shippingType = item.getShippingType();
        		Long shippingTypeId = shippingType.getShippingTypeId();
        		if (shippingTypeId == null) {
        			logger.debug("No shipping type can be found");
            		shoppingCartItem.setItemShippingFee(0);
            		shoppingCartItem.setItemAdditionalShippingFee(0);
        			continue;
        		}
        		Iterator<?> shippingMethodRegionTypes = shippingMethodRegion.getShippingMethodRegionTypes().iterator();
        		ShippingRate shippingRate = null;
        		while (shippingMethodRegionTypes.hasNext()) {
        			ShippingMethodRegionType shippingMethodRegionType = (ShippingMethodRegionType) shippingMethodRegionTypes.next();
        			if (shippingMethodRegionType.getShippingType().getShippingTypeId().equals(shippingTypeId)) {
        				shippingRate = shippingMethodRegionType.getShippingRate();
        				break;
        			}
        		}
        		float itemPrice = shoppingCartItem.getItemPriceTotal();
        		
        		float shippingFee = 0;
        		float additionShippingFee = 0;
        		if (shippingRate != null) {
	        		if (shippingRate.getShippingRateCurrency().getShippingRateFee() != null) {
	        			shippingFee = shippingRate.getShippingRateCurrency().getShippingRateFee().floatValue();
	        			if (!contentSessionKey.isSiteCurrencyClassDefault()) {
	        				boolean foundRate = false;
	        				for (ShippingRateCurrency currency : shippingRate.getShippingRateCurrencies()) {
	        					if (!currency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(contentSessionKey.getSiteCurrencyClassId())) {
	        						continue;
	        					}
	        					if (currency.getShippingRateFee() != null) {
	        						shippingFee = currency.getShippingRateFee().floatValue();
	        						foundRate = true;
	        					}
	        					break;
	        				}
	        				if (!foundRate) {
	        					shippingFee = currencyConverter.convert(shippingFee);
	        				}
	        			}
	        		}
	        		if (shippingRate.getShippingRatePercentage() != null) {
	        			float tempRate = (float) Math.round(shippingRate.getShippingRatePercentage().floatValue() * itemPrice) / 100;
	        			if (tempRate > shippingFee) {
	        				shippingFee = tempRate;
	        			}
	        		}
	        		
	        		if (shippingRate.getShippingRateCurrency().getShippingAdditionalRateFee() != null) {
	        			additionShippingFee = shippingRate.getShippingRateCurrency().getShippingAdditionalRateFee().floatValue();
	        			if (!contentSessionKey.isSiteCurrencyClassDefault()) {
	        				boolean foundRate = false;
	        				for (ShippingRateCurrency currency : shippingRate.getShippingRateCurrencies()) {
	        					if (!currency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(contentSessionKey.getSiteCurrencyClassId())) {
	        						continue;
	        					}
	        					if (currency.getShippingAdditionalRateFee() != null) {
	        						additionShippingFee = currency.getShippingAdditionalRateFee().floatValue();
	        						foundRate = true;
	        					}
	        					break;
	        				}
	        				if (!foundRate) {
	        					additionShippingFee = currencyConverter.convert(additionShippingFee);
	        				}
	        			}
	        		}
	        		if (shippingRate.getShippingAdditionalRatePercentage() != null) {
	        			float tempRate = (float) Math.round(shippingRate.getShippingAdditionalRatePercentage().floatValue() * itemPrice) / 100;
	        			if (tempRate > additionShippingFee) {
	        				additionShippingFee = tempRate;
	        			}
	        		}
        		}
        		shoppingCartItem.setItemShippingFee(shippingFee);
        		shoppingCartItem.setItemAdditionalShippingFee(additionShippingFee);
    		}
    	}
    	shippingTotal = sumShippingTotal();
    	return shippingTotal;
	}
	
	public float sumShippingTotal() {
		float total = 0;
		
    	Iterator<?> iterator = shoppingCartItems.iterator();
    	float firstItemShippingFee = 0;
    	int firstItemShippingPos = 0;
    	int pos = 0;
    	while (iterator.hasNext()) {
    		ShoppingCartItem shoppingCartItem = (ShoppingCartItem) iterator.next();
    		/*
    		if (!shoppingCartItem.isAllowShipping()) {
    			continue;
    		}
    		*/
    		if (shoppingCartItem.getItemShippingFee() > firstItemShippingFee) {
    			logger.debug("Using " + shoppingCartItem.getItem().getItemId() + " " + shoppingCartItem.getItem().getItemNum() + " as the first shipping item");
    			firstItemShippingFee = shoppingCartItem.getItemShippingFee();
    			firstItemShippingPos = pos;
    		}
    		pos++;
    	}
    	
    	total = 0;
    	pos = 0;
    	iterator = shoppingCartItems.iterator();
    	while (iterator.hasNext()) {
    		ShoppingCartItem shoppingCartItem = (ShoppingCartItem) iterator.next();
    		/*
    		if (!shoppingCartItem.isAllowShipping()) {
    			continue;
    		}
    		*/
    		if (firstItemShippingPos == pos) {
    			total += shoppingCartItem.getItemShippingFee();
    			if (shoppingCartItem.getItemQty() > 1) {
    				total += (shoppingCartItem.getItemQty() - 1) * shoppingCartItem.getItemAdditionalShippingFee();
    			}
    		}
    		else {
    			total += shoppingCartItem.getItemQty() * shoppingCartItem.getItemAdditionalShippingFee();
    		}
    		pos++;
    	}
    	return total;
//    	shippingTotal = total;
//    	shippingOrderTotal = shippingTotal - shippingDiscountTotal;
	}
	
	public void calculateItems() throws Exception {
		itemCount = 0;
		priceTotal = 0;
		Iterator<?> iterator = shoppingCartItems.iterator();
		while (iterator.hasNext()) {
			ShoppingCartItem shoppingCartItem = (ShoppingCartItem) iterator.next();
			ItemEligibleTierPrice tierPrice = priceCalculator.getEffectivePrice(shoppingCartItem.getItem(), shoppingCartItem.getItemQty());
			shoppingCartItem.setTierPrice(tierPrice);
			float itemAmount = Utility.round(tierPrice.getItemTierPrice() * shoppingCartItem.getItemQty() / tierPrice.getItemTierQty(), 2);
			shoppingCartItem.setItemPriceTotal(itemAmount);
        	itemCount += shoppingCartItem.getItemQty();
        	priceTotal += itemAmount;
		}
	}

	public void calculateTaxes() throws Exception {
		taxTotal = 0;
		Vector<ItemTax> txVector = new Vector<ItemTax>();
		Iterator<?> iterator = shoppingCartItems.iterator();
		while (iterator.hasNext()) {
			ShoppingCartItem shoppingCartItem = (ShoppingCartItem) iterator.next();
			float itemAmount = shoppingCartItem.getItemPriceTotal() - shoppingCartItem.getItemDiscountAmount();
			ItemTax taxes[] = taxCalculator.getTaxes(shoppingCartItem.getItem(), itemAmount);
			shoppingCartItem.setTaxes(taxes);
			for (int i = 0; i < taxes.length; i++) {
				ItemTax itemTax = taxes[i];
				taxTotal += itemTax.getTaxAmount();
				Iterator<?> tv = txVector.iterator();
				boolean found = false;
				ItemTax taxSummary = null;
				while (tv.hasNext()) {
					taxSummary = (ItemTax) tv.next();
					if (taxSummary.getTax().getTaxId().equals(itemTax.getTax().getTaxId())) {
						found = true;
						break;
					}
				}
				if (found) {
					taxSummary.setTaxAmount(taxSummary.getTaxAmount() + itemTax.getTaxAmount());
				}
				else {
					taxSummary = new ItemTax();
					taxSummary.setTax(itemTax.getTax());
					taxSummary.setTaxAmount(itemTax.getTaxAmount());
					taxSummary.setTaxCode(itemTax.getTaxCode());
					taxSummary.setTaxRate(itemTax.getTaxRate());
					txVector.add(taxSummary);
				}
			}
		}
		
		float shippingFinalTotal = shippingTotal - shippingDiscountTotal;
		shippingTaxes =  new ItemTax[0];
		if (shippingFinalTotal > 0) {
			shippingTaxes = taxCalculator.getShippingTaxes(shippingFinalTotal);
			for (int i = 0; i < shippingTaxes.length; i++) {
				taxTotal += shippingTaxes[i].getTaxAmount();
			}
		}
	}
	
	public ItemTax[] getTaxes() {
		Vector<ItemTax> txVector = new Vector<ItemTax>();
		Iterator<?> iterator = shoppingCartItems.iterator();
		while (iterator.hasNext()) {
			ShoppingCartItem shoppingCartItem = (ShoppingCartItem) iterator.next();
			ItemTax taxes[] = shoppingCartItem.getTaxes();
			shoppingCartItem.setTaxes(taxes);
			for (int i = 0; i < taxes.length; i++) {
				ItemTax itemTax = taxes[i];
				Iterator<?> tv = txVector.iterator();
				boolean found = false;
				ItemTax taxSummary = null;
				while (tv.hasNext()) {
					taxSummary = (ItemTax) tv.next();
					if (taxSummary.getTax().getTaxId().equals(itemTax.getTax().getTaxId())) {
						found = true;
						break;
					}
				}
				if (found) {
					taxSummary.setTaxAmount(taxSummary.getTaxAmount() + itemTax.getTaxAmount());
				}
				else {
					taxSummary = new ItemTax();
					taxSummary.setTax(itemTax.getTax());
					taxSummary.setTaxAmount(itemTax.getTaxAmount());
					taxSummary.setTaxCode(itemTax.getTaxCode());
					taxSummary.setTaxRate(itemTax.getTaxRate());
					txVector.add(taxSummary);
				}
			}
		}
		
		if (shippingTaxes != null) {
			for (int i = 0; i < shippingTaxes.length; i++) {
				ItemTax itemTax = shippingTaxes[i];
				Iterator<?> tv = txVector.iterator();
				boolean found = false;
				ItemTax taxSummary = null;
				while (tv.hasNext()) {
					taxSummary = (ItemTax) tv.next();
					if (taxSummary.getTax().getTaxId().equals(itemTax.getTax().getTaxId())) {
						found = true;
						break;
					}
				}
				if (found) {
					taxSummary.setTaxAmount(taxSummary.getTaxAmount() + itemTax.getTaxAmount());
				}
				else {
					taxSummary = new ItemTax();
					taxSummary.setTax(itemTax.getTax());
					taxSummary.setTaxAmount(itemTax.getTaxAmount());
					taxSummary.setTaxCode(itemTax.getTaxCode());
					taxSummary.setTaxRate(itemTax.getTaxRate());
					txVector.add(itemTax);
				}
			}
		}

		ItemTax taxes[] = new ItemTax[txVector.size()];
		txVector.copyInto(taxes);
		return taxes;
	}
	
	public void recalculate(ContentBean contentBean) throws Exception {
		reset();
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();

		priceCalculator = new PriceCalculator(contentBean, customer);
        calculateItems();
		calculateShipping();
		
		// Backup all user added coupons
		Vector<ShoppingCartCoupon> coupons = new Vector<ShoppingCartCoupon>();
		Iterator<?> iterator = shoppingCartCoupons.iterator();
		while (iterator.hasNext()) {
			ShoppingCartCoupon scCoupon = (ShoppingCartCoupon) iterator.next();
			if (scCoupon.isUserAdded()) {
				coupons.add(scCoupon);
			}
			iterator.remove();
		}
		
		// Extracts list of automatic coupons
		String sql = "from  Coupon " +
		 			 "where siteId = :siteId " + 
		 			 "and   curdate() between couponStartDate and couponEndDate " +
		 			 "and   couponAutoApply = 'Y' " +
		 			 "and   published = 'Y' " + 
		 			 "order by couponId";
		Query query = em.createQuery(sql);
		query.setParameter("siteId", contentSessionKey.getSiteId());
		iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			Coupon coupon = (Coupon) iterator.next();
			ShoppingCartCoupon scCoupon = new ShoppingCartCoupon();
			scCoupon.setCoupon(coupon);
			coupons.add(scCoupon);
		}
		// Ensure coupons are applied in the sequence of priority
		Collections.sort(coupons, new CouponComparator());
		
		iterator = coupons.iterator();
		while (iterator.hasNext()) {
			ShoppingCartCoupon scCoupon = (ShoppingCartCoupon) iterator.next();
			CouponImplementation couponImpl = getCouponImplementation(scCoupon);
			try {
				couponImpl.apply();
			}
			catch (CouponNotApplicableException e) {
				removedCoupons.add(scCoupon);
			}
		}
		iterator = coupons.iterator();
		while (iterator.hasNext()) {
			ShoppingCartCoupon scCoupon = (ShoppingCartCoupon) iterator.next();
			CouponImplementation couponImpl = getCouponImplementation(scCoupon);
			couponImpl.postProcess();
		}
		calculateTaxes();
		
		orderTotal = getShoppingCartSubTotal() + taxTotal + shippingOrderTotal;
		recalcRequired = false;
	}
	
	CouponImplementation getCouponImplementation(ShoppingCartCoupon shoppingCartCoupon) {
		String couponType = String.valueOf(shoppingCartCoupon.getCoupon().getCouponType());
		CouponImplementation couponImpl = null;
		if (couponType.equals(Constants.COUPONTYPE_FREESHIPING)) {
			couponImpl = new CouponFreeShipping(this, shoppingCartCoupon, currencyConverter);
		}
		if (couponType.equals(Constants.COUPONTYPE_DISCOUNT_AMOUNT)) {
			couponImpl = new CouponDiscountAmount(this, shoppingCartCoupon, currencyConverter);
		}
		if (couponType.equals(Constants.COUPONTYPE_DISCOUNT_PERCENT)) {
			couponImpl = new CouponDiscountPercent(this, shoppingCartCoupon, currencyConverter);
		}
		if (couponType.equals(Constants.COUPONTYPE_DISCOUNT_OVER_AMOUNT)) {
			couponImpl = new CouponDiscountOverAmount(this, shoppingCartCoupon, currencyConverter);
		}
		return couponImpl;
	}
	
	private void resetBookMark() {
		Iterator<?> iterator = shoppingCartCoupons.iterator();
		while (iterator.hasNext()) {
			ShoppingCartCoupon shoppingCartCoupon = (ShoppingCartCoupon) iterator.next();
			shoppingCartCoupon.setBookMark(false);
		}
	}
	
	private void reset() {
		shippingDiscountTotal = 0;
		Iterator<?> iterator = shoppingCartItems.iterator();
		while (iterator.hasNext()) {
			ShoppingCartItem shoppingCartItem = (ShoppingCartItem) iterator.next();
			shoppingCartItem.setItemDiscountAmount(0);
		}
		removedCoupons.clear();
	}
	
	public void addCoupon(Coupon coupon, ContentBean contentBean) throws CouponNotApplicableException, CouponUserNotRegisterException, Exception {
		resetBookMark();
		ShoppingCartCoupon shoppingCartCoupon = new ShoppingCartCoupon();
		shoppingCartCoupon.setCoupon(coupon);
		shoppingCartCoupon.setCouponAmount(0);
		shoppingCartCoupon.setBookMark(true);
		shoppingCartCoupon.setUserAdded(true);
		shoppingCartCoupons.add(shoppingCartCoupon);
		recalculate(contentBean);
		Iterator<?> iterator = shoppingCartCoupons.iterator();
		boolean found = false;
		while (iterator.hasNext()) {
			shoppingCartCoupon = (ShoppingCartCoupon) iterator.next();
			if (shoppingCartCoupon.isBookMark()) {
				found = true;
			}
		}
		if (!found) {
			throw new CouponNotApplicableException("");
		}
	}
	
	public float getShoppingCartSubTotal() {
		float total = 0;
		Iterator<?> iterator = shoppingCartItems.iterator();
		while (iterator.hasNext()) {
			ShoppingCartItem shoppingCartItem = (ShoppingCartItem) iterator.next();
			total += shoppingCartItem.getItemPriceTotal();
		}

		iterator = shoppingCartCoupons.iterator();
		while (iterator.hasNext()) {
			ShoppingCartCoupon shoppingCartCoupon = (ShoppingCartCoupon) iterator.next();
			total -= shoppingCartCoupon.getCouponAmount();
		}

		return total;
	}

	public void cancelTransaction() {
		
	}
	
	public boolean isShippingValid() throws Exception {
		if (this.getShippingMethods().size() > 0) {
			return true;
		}
		return false;
	}

	public Customer getCustomer() throws Exception {
		if (customer == null) {
			return null;
		}
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        customer = em.merge(customer);
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public boolean isEstimate() throws Exception {
		if (getCustomer() == null) {
			return true;
		}
		return false;
	}

	public CustomerAddress getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(CustomerAddress shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public CustomerAddress getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(CustomerAddress billingAddress) {
		this.billingAddress = billingAddress;
	}

	public CustomerCreditCard getCustCreditCard() {
		return customerCreditCard;
	}

	public void setCustCreditCard(CustomerCreditCard custCreditCard) {
		this.customerCreditCard = custCreditCard;
	}

	public ShippingMethod getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(ShippingMethod shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public boolean isActiveCart() {
		return activeCart;
	}

	public void setActiveCart(boolean activeCart) {
		this.activeCart = activeCart;
	}

	public float getShippingTotal() {
		return shippingTotal;
	}

	public void setShippingTotal(float shippingTotal) {
		this.shippingTotal = shippingTotal;
	}

	public float getShippingDiscountTotal() {
		return shippingDiscountTotal;
	}

	public void setShippingDiscountTotal(float shippingDiscountTotal) {
		this.shippingDiscountTotal = shippingDiscountTotal;
	}

	public float getTaxTotal() {
		return taxTotal;
	}

	public void setTaxTotal(float taxTotal) {
		this.taxTotal = taxTotal;
	}
	
	public Vector<ShoppingCartItem> getShoppingCartItems() {
		return shoppingCartItems;
	}

	public void setShoppingCartItems(Vector<ShoppingCartItem> shoppingCartItems) {
		this.shoppingCartItems = shoppingCartItems;
	}

	public Vector<ShoppingCartCoupon> getShoppingCartCoupons() {
		return shoppingCartCoupons;
	}

	public void setShoppingCartCoupons(Vector<ShoppingCartCoupon> shoppingCartCoupons) {
		this.shoppingCartCoupons = shoppingCartCoupons;
	}

	public Vector<?> getRemovedCoupons() {
		return removedCoupons;
	}

	public void setRemovedCoupons(Vector<ShoppingCartCoupon> removedCoupons) {
		this.removedCoupons = removedCoupons;
	}
	
	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public float getPriceTotal() {
		return priceTotal;
	}

	public void setPriceTotal(float priceTotal) {
		this.priceTotal = priceTotal;
	}

	public float getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(float orderTotal) {
		this.orderTotal = orderTotal;
	}

	public boolean isPayPal() {
		return payPal;
	}

	public void setPayPal(boolean payPal) {
		this.payPal = payPal;
	}

	public boolean isCreditCard() {
		return creditCard;
	}

	public void setCreditCard(boolean creditCard) {
		this.creditCard = creditCard;
	}

	public CustomerCreditCard getCustomerCreditCard() {
		return customerCreditCard;
	}

	public void setCustomerCreditCard(CustomerCreditCard customerCreditCard) {
		this.customerCreditCard = customerCreditCard;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public ItemTax[] getShippingTaxes() {
		return shippingTaxes;
	}

	public void setShippingTaxes(ItemTax[] shippingTaxes) {
		this.shippingTaxes = shippingTaxes;
	}

	public boolean isNewCustomer() {
		return newCustomer;
	}

	public void setNewCustomer(boolean newCustomer) {
		this.newCustomer = newCustomer;
	}

	public boolean isUseBillingAddress() {
		return useBillingAddress;
	}

	public void setUseBillingAddress(boolean useShippingAddress) {
		this.useBillingAddress = useShippingAddress;
	}

	public SiteProfile getSiteProfile() {
		return siteProfile;
	}

	public void setSiteProfile(SiteProfile siteProfile) {
		this.siteProfile = siteProfile;
	}

	public PaymentEngine getPaymentEngine() {
		return paymentEngine;
	}

	public void setPaymentEngine(PaymentEngine paymentEngine) {
		this.paymentEngine = paymentEngine;
	}

	public SiteCurrency getSiteCurrency() {
		return siteCurrency;
	}

	public void setSiteCurrency(SiteCurrency siteCurrency) {
		this.siteCurrency = siteCurrency;
	}

	public float getShippingOrderTotal() {
		return shippingOrderTotal;
	}

	public void setShippingOrderTotal(float shippingOrderTotal) {
		this.shippingOrderTotal = shippingOrderTotal;
	}

	public ContentSessionKey getContentSessionKey() {
		return contentSessionKey;
	}

	public void setContentSessionKey(ContentSessionKey contentSessionKey) {
		this.contentSessionKey = contentSessionKey;
	}

	public boolean isRecalcRequired() {
		return recalcRequired;
	}

	public void setRecalcRequired(boolean recalcRequired) {
		this.recalcRequired = recalcRequired;
	}

	public SiteDomain getSiteDomain() {
		return siteDomain;
	}

	public void setSiteDomain(SiteDomain siteDomain) {
		this.siteDomain = siteDomain;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public CustomerAddress getCustAddress() {
		return custAddress;
	}

	public void setCustAddress(CustomerAddress custAddress) {
		this.custAddress = custAddress;
	}

	public boolean isCashPayment() {
		return cashPayment;
	}

	public void setCashPayment(boolean cashPayment) {
		this.cashPayment = cashPayment;
	}

	public boolean isCashPaymentOrder() {
		return cashPaymentOrder;
	}

	public void setCashPaymentOrder(boolean cashPaymentOrder) {
		this.cashPaymentOrder = cashPaymentOrder;
	}

	public boolean isShippingPickUp() throws Exception {
		if (isEstimate()) {
			return estimatePickUp;
		}
		return shippingPickUp;
	}

	public void setShippingPickUp(boolean shippingPickUp) {
		this.shippingPickUp = shippingPickUp;
	}

	public boolean isIncludeShippingPickUp() {
		return includeShippingPickUp;
	}

	public void setIncludeShippingPickUp(boolean includeShippingPickUp) {
		this.includeShippingPickUp = includeShippingPickUp;
	}

	public boolean isShippingQuoteLock() {
		return shippingQuoteLock;
	}

	public void setShippingQuoteLock(boolean shippingQuoteLock) {
		this.shippingQuoteLock = shippingQuoteLock;
	}

	public CustomerAddress getEstimateAddress() {
		return estimateAddress;
	}

	public void setEstimateAddress(CustomerAddress estimateAddress) {
		this.estimateAddress = estimateAddress;
	}

	public boolean isEstimatePickUp() {
		return estimatePickUp;
	}

	public void setEstimatePickUp(boolean estimatePickUp) {
		this.estimatePickUp = estimatePickUp;
	}

	public boolean isPayPalOrder() {
		return payPalOrder;
	}

	public void setPayPalOrder(boolean payPalOrder) {
		this.payPalOrder = payPalOrder;
	}

	public boolean isCreditCardOrder() {
		return creditCardOrder;
	}

	public void setCreditCardOrder(boolean creditCardOrder) {
		this.creditCardOrder = creditCardOrder;
	}

	public int getCheckoutSteps() {
		return checkoutSteps;
	}

	public void setCheckoutSteps(int checkoutSteps) {
		this.checkoutSteps = checkoutSteps;
	}

	public boolean isPayPalWebsitePaymentProHosted() {
		return payPalWebsitePaymentProHosted;
	}

	public void setPayPalWebsitePaymentProHosted(
			boolean payPalWebsitePaymentProHosted) {
		this.payPalWebsitePaymentProHosted = payPalWebsitePaymentProHosted;
	}

	public boolean isPayPalWebsitePaymentProHostedOrder() {
		return payPalWebsitePaymentProHostedOrder;
	}

	public void setPayPalWebsitePaymentProHostedOrder(
			boolean payPalWebsitePaymentProHostedOrder) {
		this.payPalWebsitePaymentProHostedOrder = payPalWebsitePaymentProHostedOrder;
	}

	public String toString() {
		String s = "";
		s += "Start printing shopping cart information......" + "\n";
		s += "activeCart: " + activeCart + "\n";
		s += "shippingTotal: " + shippingTotal + "\n";
		s += "shippingDiscountTotal: " + shippingDiscountTotal + "\n";
		s += "taxTotal: " + taxTotal + "\n";
		s += "taxes: " + "\n";
		ItemTax taxes[] = getTaxes();
		for (int i = 0; i < taxes.length; i++) {
			s += "  " + taxes[i].toString();
		}

		s += "\n";
		s += "shoppingCartItems: " + "\n";
		Iterator<?> iterator = shoppingCartItems.iterator();
		while (iterator.hasNext()) {
			ShoppingCartItem shoppingCartItem = (ShoppingCartItem) iterator.next();
			s += shoppingCartItem.toString();
		}
		s += "\n";
		s += "shoppingCartCoupons: " + "\n";
		iterator = shoppingCartCoupons.iterator();
		while (iterator.hasNext()) {
			ShoppingCartCoupon shoppingCartCoupon = (ShoppingCartCoupon) iterator.next();
			s += shoppingCartCoupon.toString();
		}
		s += "\n";
		s += "removedCoupons: " + "\n";
		iterator = removedCoupons.iterator();
		while (iterator.hasNext()) {
			ShoppingCartCoupon shoppingCartCoupon = (ShoppingCartCoupon) iterator.next();
			s += shoppingCartCoupon.toString();
		}
		return s;
	}

	class CouponComparator implements Comparator<ShoppingCartCoupon> {
		public int compare(ShoppingCartCoupon o1, ShoppingCartCoupon o2) {
			ShoppingCartCoupon c1 = (ShoppingCartCoupon) o1;
			ShoppingCartCoupon c2 = (ShoppingCartCoupon) o2;
			return c1.getCoupon().getCouponPriority().compareTo(c2.getCoupon().getCouponPriority());
		}
	}
}
