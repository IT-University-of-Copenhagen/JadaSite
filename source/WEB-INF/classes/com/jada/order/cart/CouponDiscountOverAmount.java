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

import com.jada.jpa.entity.Coupon;
import com.jada.jpa.entity.CouponCurrency;
import com.jada.util.Constants;

public class CouponDiscountOverAmount extends CouponImplementation {

	public CouponDiscountOverAmount(ShoppingCart shoppingCart,
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
		float couponOrderAmount = currencyConverter.convert(coupon.getCouponCurrency().getCouponOrderAmount());
		float couponDiscountAmount = currencyConverter.convert(coupon.getCouponCurrency().getCouponDiscountAmount());
		if (!shoppingCart.getContentSessionKey().isSiteCurrencyClassDefault()) {
			for (CouponCurrency currency : coupon.getCouponCurrencies()) {
				if (currency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(shoppingCart.getContentSessionKey().getSiteCurrencyClassId())) {
					if (currency.getCouponDiscountAmount() != null) {
						couponDiscountAmount = currency.getCouponDiscountAmount();
					}
					if (currency.getCouponOrderAmount() != null) {
						couponOrderAmount = currency.getCouponOrderAmount();
					}
				}
			}
		}
		
		float discountTotal = 0;
		if (String.valueOf(coupon.getCouponScope()).equals(Constants.COUPONSCOPE_ITEM)) {
			Iterator<?> iterator = shoppingCart.getShoppingCartItems().iterator();
			while (iterator.hasNext()) {
				ShoppingCartItem shoppingCartItem = (ShoppingCartItem) iterator.next();
				if (isItemApplicable(shoppingCartItem.getItem())) {
					float discount = shoppingCartItem.getItemQty() * couponDiscountAmount;
					float discountAmount = shoppingCartItem.getItemDiscountAmount();
					float remainingAmount = shoppingCartItem.getItemPriceTotal()/ shoppingCartItem.getItemQty() - shoppingCartItem.getItemDiscountAmount();
					discountAmount += discount;
					if (remainingAmount < couponOrderAmount) {
						continue;
					}
					if (!validateOnly ) {
						shoppingCartItem.setItemDiscountAmount(discountAmount);
					}
					discountTotal += discount;
				}
			}
		}
		else {
			if (shoppingCart.getShoppingCartSubTotal() < couponOrderAmount) {
				throw new CouponNotApplicableException("");
			}
			discountTotal = couponDiscountAmount;
		}
		if (discountTotal == 0) {
			logger.debug("Coupon (" + coupon.getCouponId() + "," + coupon.getCouponLanguage().getCouponName() + 
					 ") - no more discount can be applied");
			throw new CouponNotApplicableException("");
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
