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

import javax.persistence.EntityManager;
public class CouponTester {
	EntityManager em = null;
	ShoppingCart shoppingCart = null;
/*
	public CouponTester() {
		
	}
	
	public void init() throws Exception {
		HibernateConnection connection = HibernateConnection.getInstance();
		connection.init("com.mysql.jdbc.Driver", 
						"jdbc:mysql://localhost:3306/jada?autoReconnect=true", 
						"root", 
						"system");

        em = JpaConnection.getInstance().getCurrentEntityManager();
        em.getTransaction().begin();
        
        Site site = (Site) em.find(Site.class, "jadademo");
        SiteDomain siteDomain = site.getSiteDomainDefault();
        shoppingCart = new ShoppingCart(siteDomain.getSiteCurrencyDefault(), siteDomain.getSiteProfileDefault());
        Customer customer = (Customer) em.find(Customer.class, Long.valueOf(1));
        shoppingCart.initCustomer(customer);
        
	}
	
	public void test() throws Exception {
		Item item = (Item) em.find(Item.class, Long.valueOf(1));
		shoppingCart.setItemQty(item, 3);
		System.out.println(shoppingCart.toString());
		Coupon coupon = (Coupon) em.find(Coupon.class, Long.valueOf(1));
		item = (Item) em.find(Item.class, Long.valueOf(2));
		shoppingCart.setItemQty(item, 3);
		try {
			shoppingCart.addCoupon(coupon);
		}
		catch (CouponNotApplicableException e) {
			System.out.println("Coupon (" + coupon.getCouponId() + ", " + coupon.getCouponName() + ") is not applicable");
		}

		System.out.println(shoppingCart.toString());

	}
	
	public static void main(String[] args) {
		try {
			CouponTester tester = new CouponTester();
			tester.init();
			tester.test();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
*/
}
