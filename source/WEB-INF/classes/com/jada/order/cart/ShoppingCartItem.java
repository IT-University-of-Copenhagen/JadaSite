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

import java.util.Vector;

import com.jada.jpa.entity.Item;

public class ShoppingCartItem {
	Item item;
	ItemEligibleTierPrice tierPrice;
	int itemQty;
	int suggestQty;
	float itemPriceTotal;
	float taxTotal;
	ItemTax taxes[];
	float itemShippingFee;
	float itemAdditionalShippingFee;
	float itemShippingDiscountFee;
	float itemAdditionalShippingDiscountFee;
	float itemDiscountAmount;
	Vector<?> itemAttributeInfos;
	public ItemTax[] getTaxes() {
		return taxes;
	}
	public void setTaxes(ItemTax[] taxes) {
		this.taxes = taxes;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public int getItemQty() {
		return itemQty;
	}
	public void setItemQty(int itemQty) {
		this.itemQty = itemQty;
	}
	public float getItemPriceTotal() {
		return itemPriceTotal;
	}
	public void setItemPriceTotal(float itemPriceTotal) {
		this.itemPriceTotal = itemPriceTotal;
	}
	public float getTaxTotal() {
		return taxTotal;
	}
	public void setTaxTotal(float taxTotal) {
		this.taxTotal = taxTotal;
	}
	public float getItemShippingFee() {
		return itemShippingFee;
	}
	public void setItemShippingFee(float itemShippingFee) {
		this.itemShippingFee = itemShippingFee;
	}
	public float getItemAdditionalShippingFee() {
		return itemAdditionalShippingFee;
	}
	public void setItemAdditionalShippingFee(float itemAdditionalShippingFee) {
		this.itemAdditionalShippingFee = itemAdditionalShippingFee;
	}
	public float getItemShippingDiscountFee() {
		return itemShippingDiscountFee;
	}
	public void setItemShippingDiscountFee(float itemShippingDiscountFee) {
		this.itemShippingDiscountFee = itemShippingDiscountFee;
	}
	public float getItemAdditionalShippingDiscountFee() {
		return itemAdditionalShippingDiscountFee;
	}
	public void setItemAdditionalShippingDiscountFee(
			float itemAdditionalShippingDiscountFee) {
		this.itemAdditionalShippingDiscountFee = itemAdditionalShippingDiscountFee;
	}
	public float getItemDiscountAmount() {
		return itemDiscountAmount;
	}
	public void setItemDiscountAmount(float itemDiscountAmount) {
		this.itemDiscountAmount = itemDiscountAmount;
	}
	public int getSuggestQty() {
		return suggestQty;
	}
	public void setSuggestQty(int suggestQty) {
		this.suggestQty = suggestQty;
	}
	public ItemEligibleTierPrice getTierPrice() {
		return tierPrice;
	}
	public void setTierPrice(ItemEligibleTierPrice tierPrice) {
		this.tierPrice = tierPrice;
	}
	public Vector<?> getItemAttributeInfos() {
		return itemAttributeInfos;
	}
	public void setItemAttributeInfos(Vector<?> itemAttributeInfos) {
		this.itemAttributeInfos = itemAttributeInfos;
	}
	public String toString() {
		String s = "";
		s += "item: " + item.getItemId() + ", itemDesc: " + item.getItemLanguage().getItemDesc() + "\n";
		s += "itemQty: " + itemQty + "\n";
		s += "itemPriceTotal: " + itemPriceTotal + "\n";
		s += "taxTotal: " + taxTotal + "\n";
		s += "taxes:" + "\n";
		for (int i = 0; i < taxes.length; i++) {
			s += "  " + taxes[i].toString();
		}
		s += "itemShippingFee: " + itemShippingFee + "\n";
		s += "itemAdditionalShippingFee: " + itemAdditionalShippingFee + "\n";
		s += "itemShippingDiscountFee: " + itemShippingDiscountFee + "\n";
		s += "itemAdditionalShippingDiscountFee: " + itemAdditionalShippingDiscountFee + "\n";
		s += "itemDiscountAmount: " + itemDiscountAmount + "\n";
		return s;
	}
}
