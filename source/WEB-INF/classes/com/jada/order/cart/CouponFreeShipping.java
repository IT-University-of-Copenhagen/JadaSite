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

import com.jada.util.Constants;

public class CouponFreeShipping extends CouponImplementation {

	public CouponFreeShipping(ShoppingCart shoppingCart,
			ShoppingCartCoupon shoppingCartCoupon, CurrencyConverter currencyConverter) {
		super(shoppingCart, shoppingCartCoupon, currencyConverter);
	}

	public void apply() throws CouponNotApplicableException, Exception {
		if (!isApplicable()) {
			throw new CouponNotApplicableException("");
		}
		Iterator<?> iterator = shoppingCart.getShoppingCartItems().iterator();
		while (iterator.hasNext()) {
			ShoppingCartItem shoppingCartItem = (ShoppingCartItem) iterator.next();
			if (String.valueOf(coupon.getCouponScope()).equals(Constants.COUPONSCOPE_ITEM)) {
				if (!isItemApplicable(shoppingCartItem.getItem())) {
					continue;
				}
			}
			shoppingCartItem.setItemShippingFee(0);
			shoppingCartItem.setItemAdditionalShippingFee(0);
		}
		float originalShippingTotal = shoppingCart.getShippingTotal();
		float newTotal = shoppingCart.sumShippingTotal();
		float shippingDiscountTotal = originalShippingTotal - newTotal;
//		createCoupon(shippingDiscountTotal);
		createCoupon(0);
		shoppingCart.setShippingTotal(originalShippingTotal);
		shoppingCart.setShippingDiscountTotal(shippingDiscountTotal);
		shoppingCart.setShippingOrderTotal(newTotal);
	}
	
	public void postProcess() throws CouponNotApplicableException, Exception {
	}
}
