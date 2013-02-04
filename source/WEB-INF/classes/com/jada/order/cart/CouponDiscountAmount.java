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

import com.jada.jpa.entity.Coupon;
import com.jada.jpa.entity.CouponCurrency;
import com.jada.util.Constants;

public class CouponDiscountAmount extends CouponImplementation {
    Logger logger = Logger.getLogger(CouponDiscountAmount.class);

	public CouponDiscountAmount(ShoppingCart shoppingCart,
			ShoppingCartCoupon shoppingCartCoupon, CurrencyConverter currencyConverter) {
		super(shoppingCart, shoppingCartCoupon, currencyConverter);
	}

	public void apply() throws CouponNotApplicableException, Exception {
		if (!isApplicable()) {
			throw new CouponNotApplicableException("");
		}
		
		calculate(true);
		float discountTotal = calculate(false);
		
		shoppingCart.calculateTaxes();
		createCoupon(discountTotal);
	}
	
	public float calculate(boolean validateOnly) throws CouponNotApplicableException, Exception {
		float couponDiscountAmount = coupon.getCouponCurrency().getCouponDiscountAmount();
		if (!shoppingCart.getContentSessionKey().isSiteCurrencyClassDefault()) {
			boolean found = false;
			for (CouponCurrency currency : coupon.getCouponCurrencies()) {
				if (currency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(shoppingCart.getContentSessionKey().getSiteCurrencyClassId())) {
					if (currency.getCouponDiscountAmount() != null) {
						couponDiscountAmount = currency.getCouponDiscountAmount();
						found = true;
					}
					break;
				}
			}
			if (!found) {
				couponDiscountAmount = currencyConverter.convert(couponDiscountAmount);
			}
		}
		
		float discountTotal = 0;
		if (String.valueOf(coupon.getCouponScope()).equals(Constants.COUPONSCOPE_ITEM)) {
			Iterator<?> iterator = shoppingCart.getShoppingCartItems().iterator();
			while (iterator.hasNext()) {
				ShoppingCartItem shoppingCartItem = (ShoppingCartItem) iterator.next();
				if (isItemApplicable(shoppingCartItem.getItem())) {
					float discount = shoppingCartItem.getItemQty() * couponDiscountAmount;
					float itemRemainingAmount = shoppingCartItem.getItemPriceTotal() - shoppingCartItem.getItemDiscountAmount();
					if (itemRemainingAmount < discount) {
						discount = itemRemainingAmount;
					}
					if (!validateOnly ) {
						float discountAmount = shoppingCartItem.getItemDiscountAmount();
						discountAmount += discount;
						shoppingCartItem.setItemDiscountAmount(discountAmount);
					}
					discountTotal += discount;
				}
			}
		}
		else {
			float remainingAmount = shoppingCart.getShoppingCartSubTotal();
			if (couponDiscountAmount > remainingAmount) {
				discountTotal = remainingAmount;
			}
			else {
				discountTotal = couponDiscountAmount;
			}
		}
		return discountTotal;
	}
	
	public void postProcess() throws CouponNotApplicableException, Exception {
		Coupon coupon = shoppingCartCoupon.getCoupon();
		if (String.valueOf(coupon.getCouponScope()).equals(Constants.COUPONSCOPE_ORDER)) {
			applyDiscountToOrder(shoppingCartCoupon.getCouponAmount());
		}
	}
}
