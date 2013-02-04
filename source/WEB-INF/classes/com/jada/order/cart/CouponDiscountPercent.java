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
import com.jada.util.Constants;

public class CouponDiscountPercent extends CouponImplementation {

	public CouponDiscountPercent(ShoppingCart shoppingCart,
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
		float discountTotal = 0;
		float remainingAmountTotal = 0;
		boolean isItemScope = String.valueOf(coupon.getCouponScope()).equals(Constants.COUPONSCOPE_ITEM);
		Iterator<?> iterator = shoppingCart.getShoppingCartItems().iterator();
		while (iterator.hasNext()) {
			ShoppingCartItem shoppingCartItem = (ShoppingCartItem) iterator.next();
			if (isItemScope) {
				if (!isItemApplicable(shoppingCartItem.getItem())) {
					continue;
				}
				float discount = (float) Math.round(shoppingCartItem.getItemPriceTotal() * coupon.getCouponDiscountPercent().floatValue()) / 100;
				float discountAmount = shoppingCartItem.getItemDiscountAmount();
				float remainingAmount = shoppingCartItem.getItemPriceTotal() - shoppingCartItem.getItemDiscountAmount();
				if (remainingAmount < discount) {
					discount = remainingAmount;
				}
				discountAmount += discount;
				if (!validateOnly ) {
					shoppingCartItem.setItemDiscountAmount(discountAmount);
				}
				discountTotal += discount;
			}
			else {
				remainingAmountTotal += shoppingCartItem.getItemPriceTotal() - shoppingCartItem.getItemDiscountAmount();
				float discount = (float) Math.round(shoppingCartItem.getItemPriceTotal() * coupon.getCouponDiscountPercent().floatValue()) / 100;
				discountTotal += discount;
				remainingAmountTotal -= discount;
			}
		}
		if (!isItemScope) {
			if (discountTotal > remainingAmountTotal) {
				discountTotal = remainingAmountTotal;
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
