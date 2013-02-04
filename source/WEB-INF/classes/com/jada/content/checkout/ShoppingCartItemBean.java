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

package com.jada.content.checkout;

import java.util.Vector;

public class ShoppingCartItemBean {
	String itemId;
	String itemNum;
	String itemNaturalKey;
	String itemShortDesc;
	String itemQty;
	String itemPrice;
	String itemSubTotal;
	String itemQtyError;
	String imageId;
	String itemUrl;
	String commentRatingPercentage;
	String commentRating;
	boolean special;
	Vector<?> shoppingCartItemAttributes;
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getItemQtyError() {
		return itemQtyError;
	}
	public void setItemQtyError(String itemQtyError) {
		this.itemQtyError = itemQtyError;
	}
	public String getItemSubTotal() {
		return itemSubTotal;
	}
	public void setItemSubTotal(String itemSubTotal) {
		this.itemSubTotal = itemSubTotal;
	}
	public String getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getItemQty() {
		return itemQty;
	}
	public void setItemQty(String itemQty) {
		this.itemQty = itemQty;
	}
	public String getItemShortDesc() {
		return itemShortDesc;
	}
	public void setItemShortDesc(String itemShortDesc) {
		this.itemShortDesc = itemShortDesc;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemNum() {
		return itemNum;
	}
	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}
	public String getItemUrl() {
		return itemUrl;
	}
	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}
	public Vector<?> getShoppingCartItemAttributes() {
		return shoppingCartItemAttributes;
	}
	public void setShoppingCartItemAttributes(Vector<?> shoppingCartItemAttributes) {
		this.shoppingCartItemAttributes = shoppingCartItemAttributes;
	}
	public String getItemNaturalKey() {
		return itemNaturalKey;
	}
	public void setItemNaturalKey(String itemNaturalKey) {
		this.itemNaturalKey = itemNaturalKey;
	}
}
