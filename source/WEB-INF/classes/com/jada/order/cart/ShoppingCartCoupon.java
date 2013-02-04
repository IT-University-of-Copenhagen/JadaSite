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

import com.jada.jpa.entity.Coupon;

public class ShoppingCartCoupon {
	Coupon coupon;
	boolean userAdded;
	float couponAmount;
	boolean bookMark;
	public Coupon getCoupon() {
		return coupon;
	}
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}
	public boolean isUserAdded() {
		return userAdded;
	}
	public void setUserAdded(boolean userAdded) {
		this.userAdded = userAdded;
	}
	public float getCouponAmount() {
		return couponAmount;
	}
	public void setCouponAmount(float couponAmount) {
		this.couponAmount = couponAmount;
	}
	public boolean isBookMark() {
		return bookMark;
	}
	public void setBookMark(boolean bookMark) {
		this.bookMark = bookMark;
	}
	public String toString() {
		String s = "";
		s += "couponCode: " + coupon.getCouponCode() + ", ";
		s += "couponName: " + coupon.getCouponLanguage().getCouponName() + ", ";
		s += "couponAmount: " + couponAmount + ", ";
		s += "userAdded: " + userAdded + ", ";
		s += "bookmark: " + bookMark + "\n";
		return s;
	}
}
