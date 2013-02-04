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

package com.jada.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.admin.AdminBean;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Content;
import com.jada.jpa.entity.ContentDescSearch;
import com.jada.jpa.entity.ContentLanguage;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.ItemDescSearch;
import com.jada.jpa.entity.ItemLanguage;
import com.jada.jpa.entity.ItemPriceCurrency;
import com.jada.jpa.entity.ItemPriceSearch;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteCurrencyClass;
import com.jada.jpa.entity.SiteProfileClass;

public class CategorySearchUtil {
	static Date MINDATE = new Date(java.sql.Date.valueOf("1970-01-01").getTime());
	static Date MAXDATE = new Date(java.sql.Date.valueOf("2999-12-31").getTime());

	static public void itemPriceSearchUpdate(Item item, Site site, AdminBean adminBean) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		for (ItemPriceSearch itemPriceSearch : item.getItemPriceSearches()) {
			em.remove(itemPriceSearch);
		}
		item.getItemPriceSearches().clear();
		
		if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_RECOMMAND_BUNDLE)) {
			for (Item child : item.getChildren()) {
				createItemPriceSearch(item, child, site, adminBean);
			}
		}
		else {
			createItemPriceSearch(item, item, site, adminBean);
			String sql = "select item " +
						 "from   Item item " +
			 			 "inner  join item.children child " +
			 			 "where  child.itemId = :itemId";
			Query query = em.createQuery(sql);
			query.setParameter("itemId", item.getItemId());
			Iterator<?> iterator = query.getResultList().iterator();
			while (iterator.hasNext()) {
				Item i = (Item) iterator.next();
				if (!i.getItemTypeCd().equals(Constants.ITEM_TYPE_RECOMMAND_BUNDLE)) {
					continue;
				}
				itemPriceSearchUpdate(i, site, adminBean);
			}
		}
	}
	
	static void createItemPriceSearch(Item master, Item item, Site site, AdminBean adminBean) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ItemPriceCurrency regularPrice = item.getItemPrice();
		ItemPriceCurrency specialPrice = item.getItemSpecPrice();

		Query query = em.createQuery("from SiteCurrencyClass siteCurrencyClass where siteCurrencyClass.site.siteId = :siteId");
		query.setParameter("siteId", site.getSiteId());
		Iterator<?> iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			SiteCurrencyClass siteCurrencyClass = (SiteCurrencyClass) iterator.next();
			if (specialPrice != null) {
				ItemPriceSearch itemPriceSearch = new ItemPriceSearch();
				ItemPriceCurrency itemPriceCurrency = null;
				for (ItemPriceCurrency currency : item.getItemPriceCurrencies()) {
					if (currency.getItemPriceTypeCode() != Constants.ITEM_PRICE_TYPE_CODE_SPECIAL) {
						continue;
					}
					if (currency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteCurrencyClass.getSiteCurrencyClassId())) {
						itemPriceCurrency = currency;
						break;
					}
				}
				if (itemPriceCurrency == null || itemPriceCurrency.getItemPrice() == null) {
					itemPriceSearch.setItemPrice(specialPrice.getItemPrice());
					itemPriceSearch.setExchangeFactor(Integer.valueOf(1));
				}
				else {
					itemPriceSearch.setItemPrice(itemPriceCurrency.getItemPrice());
					itemPriceSearch.setExchangeFactor(Integer.valueOf(0));	
				}
				itemPriceSearch.setItemPricePublishOn(specialPrice.getItemPricePublishOn());
				itemPriceSearch.setItemPriceExpireOn(specialPrice.getItemPriceExpireOn());
				itemPriceSearch.setRecUpdateBy(adminBean.getUser().getUserId());
				itemPriceSearch.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
				itemPriceSearch.setRecCreateBy(adminBean.getUser().getUserId());
				itemPriceSearch.setRecCreateDatetime(new Date(System.currentTimeMillis()));
				itemPriceSearch.setSiteCurrencyClass(siteCurrencyClass);
				itemPriceSearch.setItem(master);
				em.persist(itemPriceSearch);
				master.getItemPriceSearches().add(itemPriceSearch);
			}
			ItemPriceCurrency itemPriceCurrency = null;
			for (ItemPriceCurrency currency : item.getItemPriceCurrencies()) {
				if (currency.getItemPriceTypeCode() != Constants.ITEM_PRICE_TYPE_CODE_REGULAR) {
					continue;
				}
				if (currency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteCurrencyClass.getSiteCurrencyClassId())) {
					itemPriceCurrency = currency;
					break;
				}
			}
			ItemPriceSearch itemPriceSearch = new ItemPriceSearch();
			if (itemPriceCurrency == null || itemPriceCurrency.getItemPrice() == null) {
				itemPriceSearch.setItemPrice(regularPrice.getItemPrice());
				itemPriceSearch.setExchangeFactor(Integer.valueOf(1));
			}
			else {
				itemPriceSearch.setItemPrice(itemPriceCurrency.getItemPrice());
				itemPriceSearch.setExchangeFactor(Integer.valueOf(0));	
			}
			itemPriceSearch.setRecUpdateBy(adminBean.getUser().getUserId());
			itemPriceSearch.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			itemPriceSearch.setRecCreateBy(adminBean.getUser().getUserId());
			itemPriceSearch.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			itemPriceSearch.setSiteCurrencyClass(siteCurrencyClass);
			if (specialPrice != null) {
				itemPriceSearch.setItemPricePublishOn(MINDATE);
				itemPriceSearch.setItemPriceExpireOn(addDay(specialPrice.getItemPricePublishOn(), -1));
			}
			else {
				itemPriceSearch.setItemPricePublishOn(MINDATE);
				itemPriceSearch.setItemPriceExpireOn(MAXDATE);	
			}
			itemPriceSearch.setItem(master);
			em.persist(itemPriceSearch);
			master.getItemPriceSearches().add(itemPriceSearch);
			
			if (specialPrice != null) {
				ItemPriceSearch itemPriceSearchEnd = new ItemPriceSearch();
				itemPriceSearchEnd.setItemPrice(itemPriceSearch.getItemPrice());
				itemPriceSearchEnd.setExchangeFactor(itemPriceSearch.getExchangeFactor());
				itemPriceSearchEnd.setRecUpdateBy(adminBean.getUser().getUserId());
				itemPriceSearchEnd.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
				itemPriceSearchEnd.setRecCreateBy(adminBean.getUser().getUserId());
				itemPriceSearchEnd.setRecCreateDatetime(new Date(System.currentTimeMillis()));
				itemPriceSearchEnd.setSiteCurrencyClass(siteCurrencyClass);
				itemPriceSearchEnd.setItemPricePublishOn(addDay(specialPrice.getItemPriceExpireOn(), 1));
				itemPriceSearchEnd.setItemPriceExpireOn(MAXDATE);
				itemPriceSearchEnd.setItem(master);
				em.persist(itemPriceSearchEnd);
				master.getItemPriceSearches().add(itemPriceSearchEnd);
			}
		}
	}
	
	static public void itemDescSearchUpdate(Item item, Site site, AdminBean adminBean) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		for (ItemDescSearch itemDescSearch : item.getItemDescSearches()) {
			em.remove(itemDescSearch);
		}
		item.getItemDescSearches().clear();
		ItemLanguage itemLanguageDefault = item.getItemLanguage();
		Query query = em.createQuery("from SiteProfileClass siteProfileClass where siteProfileClass.site.siteId = :siteId");
		query.setParameter("siteId", site.getSiteId());
		Iterator<?> iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			SiteProfileClass siteProfileClass = (SiteProfileClass) iterator.next();
			ItemLanguage itemLanguage = null;
			for (ItemLanguage language : item.getItemLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClass.getSiteProfileClassId())) {
					itemLanguage = language;
					break;
				}
			}
			ItemDescSearch itemDescSearch = new ItemDescSearch();
			if (itemLanguage == null || itemLanguage.getItemShortDesc() == null) {
				itemDescSearch.setItemShortDesc(itemLanguageDefault.getItemShortDesc());
			}
			else {
				itemDescSearch.setItemShortDesc(itemLanguage.getItemShortDesc());
			}
			itemDescSearch.setRecUpdateBy(adminBean.getUser().getUserId());
			itemDescSearch.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			itemDescSearch.setRecCreateBy(adminBean.getUser().getUserId());
			itemDescSearch.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			itemDescSearch.setSiteProfileClass(siteProfileClass);
			itemDescSearch.setItem(item);
			em.persist(itemDescSearch);
			item.getItemDescSearches().add(itemDescSearch);
		}
	}
	
	static public void contentDescSearchUpdate(Content content, Site site, AdminBean adminBean) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		for (ContentDescSearch contentDescSearch : content.getContentDescSearches()) {
			em.remove(contentDescSearch);
		}
		content.getContentDescSearches().clear();
		ContentLanguage contentLanguageDefault = content.getContentLanguage();
		Query query = em.createQuery("from SiteProfileClass siteProfileClass where siteProfileClass.site.siteId = :siteId");
		query.setParameter("siteId", site.getSiteId());
		Iterator<?> iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			SiteProfileClass siteProfileClass = (SiteProfileClass) iterator.next();
			ContentLanguage contentLanguage = null;
			for (ContentLanguage language : content.getContentLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClass.getSiteProfileClassId())) {
					contentLanguage = language;
					break;
				}
			}
			ContentDescSearch contentDescSearch = new ContentDescSearch();
			if (contentLanguage == null || contentLanguage.getContentTitle() == null) {
				contentDescSearch.setContentTitle(contentLanguageDefault.getContentTitle());
			}
			else {
				contentDescSearch.setContentTitle(contentLanguage.getContentTitle());
			}
			contentDescSearch.setRecUpdateBy(adminBean.getUser().getUserId());
			contentDescSearch.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			contentDescSearch.setRecCreateBy(adminBean.getUser().getUserId());
			contentDescSearch.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			contentDescSearch.setSiteProfileClass(siteProfileClass);
			contentDescSearch.setContent(content);
			em.persist(contentDescSearch);
			content.getContentDescSearches().add(contentDescSearch);
		}
	}
	
	static public void createSiteProfileClass(SiteProfileClass defaultSiteProfileClass, SiteProfileClass siteProfileClass, String siteId, String userId) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String sql = null;
		
		sql = NamedQuery.getInstance().getQuery("category.item.language.create");
		Query query = em.createQuery(sql);
		query.setParameter("siteId", siteId);
		query.setParameter("siteProfileClass", defaultSiteProfileClass);
		query.executeUpdate();
	
		sql = NamedQuery.getInstance().getQuery("category.item.language.create1");
		query = em.createQuery(sql);
		query.setParameter("siteProfileClassId", siteProfileClass.getSiteProfileClassId());
		query.executeUpdate();
		
		sql = NamedQuery.getInstance().getQuery("category.content.language.create");
		query = em.createQuery(sql);
		query.setParameter("siteId", siteId);
		query.setParameter("siteProfileClass", defaultSiteProfileClass);
		query.executeUpdate();
	
		sql = NamedQuery.getInstance().getQuery("category.content.language.create1");
		query = em.createQuery(sql);
		query.setParameter("siteProfileClassId", siteProfileClass.getSiteProfileClassId());
		query.executeUpdate();
	}
	
	static public void removeSiteProfileClass(String siteId, SiteProfileClass siteProfileClass) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String sql = NamedQuery.getInstance().getQuery("category.item.language.remove");
		Query query = em.createQuery(sql);
		
		query.setParameter("siteId", siteId);
		query.setParameter("siteProfileClass", siteProfileClass);
		query.executeUpdate();
		
		sql = NamedQuery.getInstance().getQuery("category.content.language.remove");
		query = em.createQuery(sql);
		query.setParameter("siteId", siteId);
		query.setParameter("siteProfileClass", siteProfileClass);
		query.executeUpdate();
	}
	
	static public void createSiteCurrencyClass(SiteCurrencyClass defaultSiteCurrencyClass, SiteCurrencyClass siteCurrencyClass, String siteId, String userId) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String sql = null;
		Query query = null;
		
		sql = NamedQuery.getInstance().getQuery("category.item.price.create4");
		query = em.createQuery(sql);
		query.setParameter("siteId", siteId);
		query.setParameter("siteCurrencyClassId", defaultSiteCurrencyClass.getSiteCurrencyClassId());
		query.executeUpdate();

		sql = NamedQuery.getInstance().getQuery("category.item.price.create5");
		query = em.createQuery(sql);
		query.setParameter("siteId", siteId);
		query.setParameter("siteCurrencyClassId1", defaultSiteCurrencyClass.getSiteCurrencyClassId());
		query.executeUpdate();

		sql = NamedQuery.getInstance().getQuery("category.item.price.create");
		query = em.createQuery(sql);
		query.setParameter("siteId", siteId);
		query.setParameter("siteCurrencyClassId1", defaultSiteCurrencyClass.getSiteCurrencyClassId());
		query.setParameter("siteCurrencyClassId2", defaultSiteCurrencyClass.getSiteCurrencyClassId());
		query.executeUpdate();
	
		sql = NamedQuery.getInstance().getQuery("category.item.price.create2");
		query = em.createQuery(sql);
		query.setParameter("siteId", siteId);
		query.setParameter("siteCurrencyClassId1", defaultSiteCurrencyClass.getSiteCurrencyClassId());
		query.setParameter("siteCurrencyClassId2", defaultSiteCurrencyClass.getSiteCurrencyClassId());
		query.executeUpdate();

		sql = NamedQuery.getInstance().getQuery("category.item.price.create3");
		query = em.createQuery(sql);
		query.setParameter("siteId", siteId);
		query.setParameter("siteCurrencyClassId1", defaultSiteCurrencyClass.getSiteCurrencyClassId());
		query.setParameter("siteCurrencyClassId2", defaultSiteCurrencyClass.getSiteCurrencyClassId());
		query.executeUpdate();

		sql = NamedQuery.getInstance().getQuery("category.item.price.create6");
		query = em.createQuery(sql);
		query.setParameter("siteId", siteId);
		query.executeUpdate();

		sql = NamedQuery.getInstance().getQuery("category.item.price.create1");
		query = em.createQuery(sql);
		query.setParameter("siteCurrencyClassId", siteCurrencyClass.getSiteCurrencyClassId());
		query.executeUpdate();

	}
	
	static public void removeSiteCurrencyClass(String siteId, SiteCurrencyClass siteCurrencyClass) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String sql = NamedQuery.getInstance().getQuery("category.item.price.remove");
		Query query = em.createQuery(sql);
		query.setParameter("siteId", siteId);
		query.setParameter("siteCurrencyClass", siteCurrencyClass);
		query.executeUpdate();
	}
	
	static Date addDay(Date input, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(input);
		calendar.add(Calendar.DAY_OF_MONTH,day);
		return calendar.getTime();
	}
}
