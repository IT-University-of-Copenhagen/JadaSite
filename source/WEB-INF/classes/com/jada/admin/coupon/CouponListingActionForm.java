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

package com.jada.admin.coupon;

import java.util.Enumeration;

import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminListingActionForm;

public class CouponListingActionForm extends AdminListingActionForm {
	private static final long serialVersionUID = 9077765489591742489L;
	String srCouponCode;
	String srCouponName;
	String srCouponPublishDate;
	String srPublished;
	CouponDisplayForm coupons[];
	
    public CouponDisplayForm getCoupon(int index) {
    	return coupons[index];
    }
	public void reset(ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
		String COUPONDETAIL = "coupon.*couponId";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(COUPONDETAIL)) {
				count++;
			}
		}
		coupons = new CouponDisplayForm[count];
		for (int i = 0; i < coupons.length; i++) {
			coupons[i] = new CouponDisplayForm();
		}
	}
	public String getSrCouponCode() {
		return srCouponCode;
	}
	public void setSrCouponCode(String srCouponCode) {
		this.srCouponCode = srCouponCode;
	}
	public String getSrCouponName() {
		return srCouponName;
	}
	public void setSrCouponName(String srCouponName) {
		this.srCouponName = srCouponName;
	}
	public String getSrPublished() {
		return srPublished;
	}
	public void setSrPublished(String srPublished) {
		this.srPublished = srPublished;
	}
	public CouponDisplayForm[] getCoupons() {
		return coupons;
	}
	public void setCoupons(CouponDisplayForm[] coupons) {
		this.coupons = coupons;
	}
	public String getSrCouponPublishDate() {
		return srCouponPublishDate;
	}
	public void setSrCouponPublishDate(String srCouponPublishDate) {
		this.srCouponPublishDate = srCouponPublishDate;
	}
}
