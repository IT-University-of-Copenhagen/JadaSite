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

package com.jada.xml.psigate;

public class Order {
	String storeID;
	String passphrase;
	String orderID;
	String bname;
	String bcompany;
	String baddress1;
	String baddress2;
	String bcity;
	String bprovince;
	String bcountry;
	String bpostalcode;
	String sname;
	String scompany;
	String saddress1;
	String saddress2;
	String scity;
	String sprovince;
	String scountry;
	String spostalcode;
	String phone;
	String fax;
	String email;
	String comments;
	String tax1;
	String tax2;
	String tax3;
	String tax4;
	String tax5;
	String shippingTotal;
	String subtotal;
	String paymentType;
	String cardAction;
	String cardNumber;
	String cardExpMonth;
	String cardExpYear;
	String customerIP;
	String cardIDNumber;
	String transRefNumber;
	Item item[];
	public String getCustomerIP() {
		return customerIP;
	}
	public void setCustomerIP(String customerIP) {
		this.customerIP = customerIP;
	}
	public Item[] getItem() {
		return item;
	}
	public void setItem(Item[] item) {
		this.item = item;
	}
	public String getBaddress1() {
		return baddress1;
	}
	public void setBaddress1(String baddress1) {
		this.baddress1 = baddress1;
	}
	public String getBaddress2() {
		return baddress2;
	}
	public void setBaddress2(String baddress2) {
		this.baddress2 = baddress2;
	}
	public String getBcity() {
		return bcity;
	}
	public void setBcity(String bcity) {
		this.bcity = bcity;
	}
	public String getBcompany() {
		return bcompany;
	}
	public void setBcompany(String bcompany) {
		this.bcompany = bcompany;
	}
	public String getBcountry() {
		return bcountry;
	}
	public void setBcountry(String bcountry) {
		this.bcountry = bcountry;
	}
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	public String getBprovince() {
		return bprovince;
	}
	public void setBprovince(String bprovince) {
		this.bprovince = bprovince;
	}
	public String getCardAction() {
		return cardAction;
	}
	public void setCardAction(String cardAction) {
		this.cardAction = cardAction;
	}
	public String getCardExpMonth() {
		return cardExpMonth;
	}
	public void setCardExpMonth(String cardExpMonth) {
		this.cardExpMonth = cardExpMonth;
	}
	public String getCardExpYear() {
		return cardExpYear;
	}
	public void setCardExpYear(String cardExpYear) {
		this.cardExpYear = cardExpYear;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getPassphrase() {
		return passphrase;
	}
	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getShippingTotal() {
		return shippingTotal;
	}
	public void setShippingTotal(String shippingTotal) {
		this.shippingTotal = shippingTotal;
	}
	public String getStoreID() {
		return storeID;
	}
	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}
	public String getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}
	public String getTax1() {
		return tax1;
	}
	public void setTax1(String tax1) {
		this.tax1 = tax1;
	}
	public String getBpostalcode() {
		return bpostalcode;
	}
	public void setBpostalcode(String bpostalcode) {
		this.bpostalcode = bpostalcode;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getTransRefNumber() {
		return transRefNumber;
	}
	public void setTransRefNumber(String transRefNumber) {
		this.transRefNumber = transRefNumber;
	}
	public String getTax2() {
		return tax2;
	}
	public void setTax2(String tax2) {
		this.tax2 = tax2;
	}
	public String getTax3() {
		return tax3;
	}
	public void setTax3(String tax3) {
		this.tax3 = tax3;
	}
	public String getTax4() {
		return tax4;
	}
	public void setTax4(String tax4) {
		this.tax4 = tax4;
	}
	public String getTax5() {
		return tax5;
	}
	public void setTax5(String tax5) {
		this.tax5 = tax5;
	}
	public String getCardIDNumber() {
		return cardIDNumber;
	}
	public void setCardIDNumber(String cardIDNumber) {
		this.cardIDNumber = cardIDNumber;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getScompany() {
		return scompany;
	}
	public void setScompany(String scompany) {
		this.scompany = scompany;
	}
	public String getSaddress1() {
		return saddress1;
	}
	public void setSaddress1(String saddress1) {
		this.saddress1 = saddress1;
	}
	public String getSaddress2() {
		return saddress2;
	}
	public void setSaddress2(String saddress2) {
		this.saddress2 = saddress2;
	}
	public String getScity() {
		return scity;
	}
	public void setScity(String scity) {
		this.scity = scity;
	}
	public String getSprovince() {
		return sprovince;
	}
	public void setSprovince(String sprovince) {
		this.sprovince = sprovince;
	}
	public String getScountry() {
		return scountry;
	}
	public void setScountry(String scountry) {
		this.scountry = scountry;
	}
	public String getSpostalcode() {
		return spostalcode;
	}
	public void setSpostalcode(String spostalcode) {
		this.spostalcode = spostalcode;
	}
}
