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

package com.jada.dao;

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import org.json.JSONException;
import com.jada.util.JSONEscapeObject;
import com.jada.util.Utility;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.CustomAttribute;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.ItemAttributeDetail;
import com.jada.jpa.entity.ItemAttributeDetailLanguage;
import com.jada.jpa.entity.ItemDescSearch;
import com.jada.jpa.entity.ItemImage;
import com.jada.jpa.entity.ItemLanguage;
import com.jada.jpa.entity.ItemPriceCurrency;
import com.jada.jpa.entity.ItemPriceSearch;
import com.jada.jpa.entity.Menu;
import com.jada.util.Constants;

public class ItemDAO extends Item {
	private static final long serialVersionUID = 5075471885223815088L;

	public static Item load(String siteId, Long itemId, LockModeType lockModeType) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Item item = (Item) em.find(Item.class, itemId);
    	if (lockModeType != null) {
    		em.lock(item, lockModeType);
    	}
    	if (item != null) {
			if (!item.getSiteId().equals(siteId)) {
				throw new SecurityException();
			}
    	}
		return item;
	}
	
	public static Item load(String siteId, Long itemId) throws SecurityException, Exception {
    	return load(siteId, itemId, (LockModeType) null);
	}
	
	public static Item loadNatural(String siteId, String itemNaturalKey) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from Item where siteId = :siteId and itemNaturalKey = :itemNaturalKey");
    	query.setParameter("siteId", siteId);
    	query.setParameter("itemNaturalKey", itemNaturalKey);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		Item item = (Item) iterator.next();
    		return item;
    	}
    	return null;
	}
	
	public static Item loadBySku(String siteId, String itemSkuCd) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from Item where siteId = :siteId and itemSkuCd = :itemSkuCd");
    	query.setParameter("siteId", siteId);
    	query.setParameter("itemSkuCd", itemSkuCd);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		Item item = (Item) iterator.next();
    		return item;
    	}
    	return null;
	}
	
	public static void remove(String siteId, Item item) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String sql = "delete  " +
    				 "from    HomePageDetail homePageDetail " + 
    				 "where   homePageDetail.item = :item ";
    	Query query = em.createQuery(sql);
    	query.setParameter("item", item);
    	query.executeUpdate();

    	Set<ItemLanguage> itemLanguages = item.getItemLanguages();
    	Set<ItemPriceCurrency> itemPriceCurrencies = item.getItemPriceCurrencies();
		em.remove(item);
		for (ItemLanguage itemLanguage : itemLanguages) {
			if (itemLanguage.getImage() != null) {
				em.remove(itemLanguage.getImage());
			}
			for (ItemImage itemImage : itemLanguage.getImages()) {
				em.remove(itemImage);
			}
			em.remove(itemLanguage);
		}
		for (ItemPriceCurrency itemPriceCurrency : itemPriceCurrencies) {
			em.remove(itemPriceCurrency);
		}
		
		for (ItemAttributeDetail itemAttribDetail : item.getItemAttributeDetails()) {
			for (ItemAttributeDetailLanguage itemAttribDetailLang : itemAttribDetail.getItemAttributeDetailLanguages()) {
				em.remove(itemAttribDetailLang);
			}
			em.remove(itemAttribDetail);
		}
		
		for (ItemDescSearch itemDescSearch : item.getItemDescSearches()) {
			em.remove(itemDescSearch);
		}
		
		for (ItemPriceSearch itemPriceSearch : item.getItemPriceSearches()) {
			em.remove(itemPriceSearch);
		}
		
		for (Menu menu : item.getMenus()) {
			menu.setItem(null);
			CacheDAO.removeByKeyPrefix(siteId, Constants.CACHE_MENU + "." + menu.getMenuSetName());
		}
	}
	
    static public boolean isPublished(Item item) {
    	if (item.getPublished() == Constants.PUBLISHED_NO) {
    		return false;
    	}
    	if (!Utility.isDateBetween(item.getItemPublishOn(), item.getItemExpireOn())) {
    		return false;
    	}
    	return true;
    }
    
    
    static public boolean isSpecialOn(Item item, Long defaultSiteCurrencyClassId) {
    	for (ItemPriceCurrency itemPriceCurrency : item.getItemPriceCurrencies()) {
    		if (itemPriceCurrency.getSiteCurrencyClass().getSiteCurrencyClassId() != defaultSiteCurrencyClassId) {
    			continue;
    		}
    		if (itemPriceCurrency.getItemPriceTypeCode() == Constants.ITEM_PRICE_TYPE_CODE_SPECIAL) {
    	    	if (Utility.isDateBetween(itemPriceCurrency.getItemPricePublishOn(), itemPriceCurrency.getItemPriceExpireOn())) {
    	    		return true;
    	    	}
    		}
    	}
    	return false;
    }
    
    static public String getSkuAttributeKey(Item item) throws JSONException {
    	JSONEscapeObject JSONEscapeObject = new JSONEscapeObject();
    	Vector<JSONEscapeObject> vector = new Vector<JSONEscapeObject>();
    	for (ItemAttributeDetail itemAttributeDetail : item.getItemAttributeDetails()) {
    		CustomAttribute customAttribute = itemAttributeDetail.getCustomAttributeDetail().getCustomAttribute();
    		if (customAttribute.getCustomAttribTypeCode() != Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP) {
    			continue;
    		}
    		JSONEscapeObject attribute = new JSONEscapeObject();
    		attribute.put("customAttribDetailId", itemAttributeDetail.getCustomAttributeDetail().getCustomAttribDetailId().toString());
    		attribute.put("customAttribOptionId", itemAttributeDetail.getCustomAttributeOption().getCustomAttribOptionId().toString());
    		vector.add(attribute);
    	}
    	JSONEscapeObject.put("itemAttributeDetails", vector);
    	return JSONEscapeObject.toHtmlString();
    }
}
