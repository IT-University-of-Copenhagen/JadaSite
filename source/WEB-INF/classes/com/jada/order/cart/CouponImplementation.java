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

import java.util.Iterator;

import org.apache.log4j.Logger;
import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Category;
import com.jada.jpa.entity.Coupon;
import com.jada.jpa.entity.Item;
import com.jada.util.Constants;
import com.jada.util.Utility;

public abstract class CouponImplementation {
	ShoppingCart shoppingCart = null;
	ShoppingCartCoupon shoppingCartCoupon = null;
	CurrencyConverter currencyConverter = null;
	Coupon coupon = null;
    Logger logger = Logger.getLogger(CouponImplementation.class);
	
	public CouponImplementation(ShoppingCart shoppingCart, ShoppingCartCoupon shoppingCartCoupon, CurrencyConverter currencyConverter) {
		this.shoppingCart = shoppingCart;
		this.shoppingCartCoupon = shoppingCartCoupon;
		this.coupon = shoppingCartCoupon.getCoupon();
		this.currencyConverter = currencyConverter;
	}

	public boolean isApplicable() throws Exception {
		if (isCouponInCart()) {
			return false;
		}
		if (!Utility.isDateBetween(coupon.getCouponStartDate(), coupon.getCouponEndDate())) {
			logger.debug("Coupon (" + coupon.getCouponId() + "," + coupon.getCouponLanguage().getCouponName() + ") - CouponNotActive");
			return false;
		}
		if (coupon.getPublished() != Constants.VALUE_YES) {
			logger.debug("Coupon (" + coupon.getCouponId() + "," + coupon.getCouponLanguage().getCouponName() + ") - CouponNotPublished");
			return false;
		}
		if (isCouponMaxUsageExceeded()) {
			logger.debug("Coupon (" + coupon.getCouponId() + "," + coupon.getCouponLanguage().getCouponName() + ") - CouponMaxUsageExceeded");
			return false;
		}
		if (shoppingCart.isActiveCart()) {
			if (isCouponMaxCustUsageExceeded()) {
				logger.debug("Coupon (" + coupon.getCouponId() + "," + coupon.getCouponLanguage().getCouponName() + ") - CouponMaxCustUsageExceeded");
				return false;
			}
		}
		if (coupon.getCouponApplyAll() != Constants.VALUE_YES) {
			if (!isCartApplicable()) {
				logger.debug("Coupon (" + coupon.getCouponId() + "," + coupon.getCouponLanguage().getCouponName() + ") - not CartApplicable");
				return false;
			}
		}
		return true;
	}
	
	protected boolean isCouponInCart() {
		Iterator<?> iterator = shoppingCart.getShoppingCartCoupons().iterator();
		while (iterator.hasNext()) {
			ShoppingCartCoupon shoppingCartCoupon = (ShoppingCartCoupon) iterator.next();
			if (shoppingCartCoupon.getCoupon().getCouponId().equals(coupon.getCouponId())) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean isCouponMaxUsageExceeded() {
		if (coupon.getCouponMaxUse() == null) {
			return false;
		}
		int couponMaxUse = coupon.getCouponMaxUse().intValue();
		int couponTotalUsed = coupon.getCouponTotalUsed().intValue();
		if (couponMaxUse <= couponTotalUsed) {
			return true;
		}
		return false;
	}
	
	protected boolean isCouponMaxCustUsageExceeded() throws Exception{
		if (coupon.getCouponMaxCustUse() == null) {
			return false;
		}
		int couponMaxCustUse = coupon.getCouponMaxCustUse().intValue();
		if (couponMaxCustUse < 1) {
			return false;
		}
		if (shoppingCart.getCustomer() == null) {
			throw new CouponUserNotRegisterException("");
		}
		
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        String sql = "select	count(*) " +
        			 "from 		OrderHeader as header " +
        			 "left      outer join header.siteDomain siteDomain " +
        			 "			inner join header.orderOtherDetails as detail " +
			 		 "			inner join detail.coupon as coupon " +
			 		 "          inner join header.customer as customer " +
			 		 "where		siteDomain.site.siteId = :siteId " +
			 		 "and       customer.custId = :custId " +
    			 	 "and		coupon.couponCode = :couponCode " +
        			 "and		header.orderStatus in ('P', 'C', 'H') ";
        Query query = em.createQuery(sql);
        query.setParameter("siteId", shoppingCart.getContentSessionKey().getSiteId());
        query.setParameter("custId", shoppingCart.getCustomer().getCustId());
        query.setParameter("couponCode", coupon.getCouponCode());
        Long orderCount = (Long) query.getSingleResult();
        
        Long creditCount = 0L;
        /*
         * Not sure how to handle scenario when customer brought an item with a coupon and later refund the item.
         * 
        sql = "select	count(*) " + 
        	  "from 	CreditHeader as header, OrderHeader as orderHeader  " + 
		 	  "left     outer join orderHeader.siteDomain siteDomain " +
      	  	  "			inner join header.creditOtherDetails as detail " + 
    	  	  "			inner join detail.coupon as coupon " + 
              "where	header.orderHeader.orderHeaderId = orderHeader.orderHeaderId " +
              "and		siteDomain.site.siteId = :siteId " + 
              "and		coupon.couponCode = :couponCode " + 
              "and		header.creditStatus in ('O', 'C', 'H') ";
        query = em.createQuery(sql);
        query.setParameter("siteId", shoppingCart.getContentSessionKey().getSiteId());
        query.setParameter("couponCode", coupon.getCouponCode());
        Long creditCount = (Long) query.getSingleResult();
        */
        
        int totalCouponUsed = orderCount.intValue() - creditCount.intValue();
        if (totalCouponUsed >= couponMaxCustUse) {
        	return true;
        }
        return false;
	}
	
	protected boolean isCartApplicable() {
		Iterator<?> iterator = shoppingCart.getShoppingCartItems().iterator();
		while (iterator.hasNext()) {
			ShoppingCartItem shoppingCartItem = (ShoppingCartItem) iterator.next();
			if (isItemApplicable(shoppingCartItem.getItem())) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean isItemApplicable(Item item) {
		if (coupon.getCouponApplyAll() == Constants.VALUE_YES) {
			return true;
		}
		Iterator<?> iterator = coupon.getItems().iterator();
		while (iterator.hasNext()) {
			Item couponItem = (Item) iterator.next();
			if (couponItem.getItemId().equals(item.getItemId())) {
				return true;
			}
		}
		iterator = coupon.getCategories().iterator();
		while (iterator.hasNext()) {
			Category category = (Category) iterator.next();
			for (Category itemCategory : item.getCategories()) {
				if (itemCategory.getCatId().equals(category.getCatId())) {
					return true;
				}
			}
		}
		return false;
	}
	
	protected void createCoupon(float couponAmount) {
		shoppingCartCoupon.setCouponAmount(new Float(couponAmount));
		shoppingCart.getShoppingCartCoupons().add(shoppingCartCoupon);
		shoppingCart.setPriceTotal(shoppingCart.getPriceTotal() - couponAmount);
	}
	
	protected void applyDiscountToOrder(float discountTotal) {
		float priceGrandTotal = 0;
		int counter = 0;
		for (ShoppingCartItem shoppingCartItem : shoppingCart.getShoppingCartItems()) {
			priceGrandTotal += shoppingCartItem.getItemPriceTotal() - shoppingCartItem.getItemDiscountAmount();
			counter++;
		}
		
		int i = 0;
		float runningTotal = discountTotal;
		for (ShoppingCartItem shoppingCartItem : shoppingCart.getShoppingCartItems()) {
			i++;
			float itemDiscountAmount = shoppingCartItem.getItemDiscountAmount();
			if (i == counter) {
				shoppingCartItem.setItemDiscountAmount(itemDiscountAmount + runningTotal);
			}
			else {
				float priceTotal = shoppingCartItem.getItemPriceTotal() - shoppingCartItem.getItemDiscountAmount();
				float discount = Utility.round((priceTotal / priceGrandTotal) * discountTotal, 2);
				shoppingCartItem.setItemDiscountAmount(itemDiscountAmount + discount);
				runningTotal -= discount;
			}
		}
	}
	
	abstract public void apply() throws CouponNotApplicableException, Exception;
	abstract public void postProcess() throws CouponNotApplicableException, Exception;

}
