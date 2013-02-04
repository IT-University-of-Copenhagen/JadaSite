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
import java.util.Vector;

import com.jada.content.ContentBean;
import com.jada.dao.ItemDAO;
import com.jada.jpa.entity.Currency;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.ItemPriceCurrency;
import com.jada.jpa.entity.ItemTierPrice;
import com.jada.jpa.entity.ItemTierPriceCurrency;
import com.jada.util.Constants;
import com.jada.util.Utility;

public class PriceCalculator {
	Customer customer = null;
	ContentBean contentBean = null;
	Currency currency = null;
	float exchangeRate = 1;
	CurrencyConverter currencyConverter = null;
	Long siteCurrencyClassId;
	
	public PriceCalculator(ContentBean contentBean, Customer customer) throws Exception {
		this.contentBean = contentBean;
		this.customer = customer;
		this.currencyConverter = new CurrencyConverter(contentBean.getContentSessionBean().getSiteCurrency());
		this.siteCurrencyClassId = contentBean.getContentSessionBean().getSiteCurrency().getSiteCurrencyClass().getSiteCurrencyClassId();
	}
	
	public float getItemPrice(Item item) throws Exception {
		float itemPrice = 0;
		itemPrice = item.getItemPrice().getItemPrice();
		Long defaultSiteCurrencyClassId = contentBean.getSiteDomain().getSite().getSiteCurrencyClassDefault().getSiteCurrencyClassId();
		if (ItemDAO.isSpecialOn(item, defaultSiteCurrencyClassId)) {
			itemPrice = item.getItemSpecPrice().getItemPrice();
		}
		if (!defaultSiteCurrencyClassId.equals((siteCurrencyClassId))) {
			boolean found = false;
			for (ItemPriceCurrency currency : item.getItemPriceCurrencies()) {
				if (!currency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteCurrencyClassId)) {
					continue;
				}
				found = true;
				if (ItemDAO.isSpecialOn(item, defaultSiteCurrencyClassId)) {
					if (currency.getItemPriceTypeCode() == Constants.ITEM_PRICE_TYPE_CODE_SPECIAL) {
						if (currency.getItemPrice() == null) {
							itemPrice = currencyConverter.convert(itemPrice);
						}
						else {
							itemPrice = currency.getItemPrice();
						}
						break;
					}
				}
				else {
					if (currency.getItemPriceTypeCode() == Constants.ITEM_PRICE_TYPE_CODE_REGULAR) {
						if (currency.getItemPrice() == null) {
							itemPrice = currencyConverter.convert(itemPrice);
						}
						else {
							itemPrice = currency.getItemPrice();
						}
						break;
					}
				}
			}
			if (!found) {
				itemPrice = currencyConverter.convert(itemPrice);
			}
		}
		return itemPrice;
	}
	
	public boolean isItemSpecPrice(Item item) {
		for (ItemPriceCurrency currency : item.getItemPriceCurrencies()) {
			if (currency.getItemPriceTypeCode() != Constants.ITEM_PRICE_TYPE_CODE_SPECIAL) {
				continue;
			}
			if (!Utility.isDateBetween(currency.getItemPricePublishOn(), currency.getItemPriceExpireOn())) {
				continue;
			}
			return true;
		}
		return false;
	}
	
	public float getItemSpecPrice(Item item) throws Exception {
		float itemSpecPrice = 0;
		ItemPriceCurrency itemPriceCurrDefault = null;
		ItemPriceCurrency itemPriceCurr = null;
		Long defaultSiteCurrencyClassId = contentBean.getSiteDomain().getSite().getSiteCurrencyClassDefault().getSiteCurrencyClassId();
		for (ItemPriceCurrency currency : item.getItemPriceCurrencies()) {
			if (currency.getItemPriceTypeCode() != Constants.ITEM_PRICE_TYPE_CODE_SPECIAL) {
				continue;
			}
			if (currency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(defaultSiteCurrencyClassId)) {
				itemPriceCurrDefault = currency;
			}
			if (currency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteCurrencyClassId)) {
				itemPriceCurr = currency;
			}
		}
		itemSpecPrice = itemPriceCurrDefault.getItemPrice().floatValue();
		if (!defaultSiteCurrencyClassId.equals(siteCurrencyClassId)) {
			if (itemPriceCurr != null && itemPriceCurr.getItemPrice() != null) {
				itemSpecPrice = itemPriceCurr.getItemPrice();
			}
			else {
				itemSpecPrice = currencyConverter.convert(itemSpecPrice);
			}
		}
		return itemSpecPrice;
	}

	public ItemEligibleTierPrice[] getItemEligibleTierPrice(Item item) {
		Vector<ItemEligibleTierPrice> prices = new Vector<ItemEligibleTierPrice>();
		Iterator<?> iterator = item.getItemTierPrices().iterator();
		while (iterator.hasNext()) {
			ItemTierPrice itemTierPrice = (ItemTierPrice) iterator.next();
			if (itemTierPrice.getCustomerClass() != null) {
				if (customer == null) {
					continue;
				}
				if (!itemTierPrice.getCustomerClass().getCustClassId().equals(customer.getCustomerClass().getCustClassId())) {
					continue;
				}
			}
			if (!Utility.isDateBetween(itemTierPrice.getItemTierPricePublishOn(), itemTierPrice.getItemTierPriceExpireOn())) {
				continue;
			}
			ItemEligibleTierPrice price = new ItemEligibleTierPrice();
			price.setItemTierQty(itemTierPrice.getItemTierQty());
			float p = itemTierPrice.getItemTierPriceCurrency().getItemPrice();
			Long defaultSiteCurrencyClassId = contentBean.getSiteDomain().getSite().getSiteDomainDefault().getSiteCurrencyDefault().getSiteCurrencyClass().getSiteCurrencyClassId();
			if (!defaultSiteCurrencyClassId.equals(siteCurrencyClassId)) {
				boolean found = false;
				for (ItemTierPriceCurrency currency : itemTierPrice.getItemTierPriceCurrencies()) {
					if (currency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteCurrencyClassId)) {
						if (currency.getItemPrice() != null) {
							p = currency.getItemPrice();
							found = true;
						}
						break;
					}
				}
				if (!found) {
					p = currencyConverter.convert(p);
				}
			}
			price.setItemTierPrice(p);
			price.setItemTierPricePublishOn(itemTierPrice.getItemTierPricePublishOn());
			price.setItemTierPriceExpireOn(itemTierPrice.getItemTierPriceExpireOn());
			prices.add(price);
		}
		ItemEligibleTierPrice itemEligibleTierPrice[] = new ItemEligibleTierPrice[prices.size()];
		prices.copyInto(itemEligibleTierPrice);
		return itemEligibleTierPrice;
	}
	
	public ItemEligibleTierPrice getEffectivePrice(Item item, int quantity) throws Exception {
		ItemEligibleTierPrice tierPrice = new ItemEligibleTierPrice();
		tierPrice.setItemTierQty(1);
		tierPrice.setItemTierPrice(getItemPrice(item));
		if (isItemSpecPrice(item)) {
			tierPrice.setItemTierPrice(getItemSpecPrice(item));
		}
		if (quantity == 1) {
			return tierPrice;
		}
		
		ItemEligibleTierPrice itemEligibleTierPrice[] = getItemEligibleTierPrice(item);
		for (int i = 0; i < itemEligibleTierPrice.length; i++) {
			ItemEligibleTierPrice potentialPrice = itemEligibleTierPrice[i];
			if (quantity < potentialPrice.itemTierQty) {
				continue;
			}
			if (tierPrice.getItemTierPrice() / tierPrice.getItemTierQty() < potentialPrice.getItemTierPrice() / potentialPrice.getItemTierQty()){
				continue;
			}
			tierPrice = itemEligibleTierPrice[i];
		}
		return tierPrice;
	}
	
	public ItemEligibleTierPrice getEffectivePrice(Item item) throws Exception {
		return getEffectivePrice(item, 1);
	}
	
}
