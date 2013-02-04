package com.jada.ie;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.CustomerClass;
import com.jada.jpa.entity.ProductClass;
import com.jada.jpa.entity.ShippingType;
import com.jada.jpa.entity.Site;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.xml.ie.ItemSimple;
import com.jada.xml.ie.ItemSimpleImage;
import com.jada.xml.ie.ItemSimpleItemTierPrice;

public class ItemSimpleTransformationBase {
	Site site = null;
	ShippingType shippingType = null;
	ProductClass productClass = null;
	CustomerClass customerClass = null;
	
	public void init(Site site) throws Exception {
		this.site = site;
		
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	
		Query query = em.createQuery("from ShippingType shippingType where siteId = :siteId and systemRecord = 'Y'");
		query.setParameter("siteId", site.getSiteId());
		shippingType = (ShippingType) query.getSingleResult();
		
		query = em.createQuery("from ProductClass productClass where productClass.site.siteId = :siteId and systemRecord = 'Y'");
		query.setParameter("siteId", site.getSiteId());
		productClass = (ProductClass) query.getSingleResult();	
		
		query = em.createQuery("from CustomerClass customerClass where customerClass.site.siteId = :siteId and systemRecord = 'Y'");
		query.setParameter("siteId", site.getSiteId());
		customerClass = (CustomerClass) query.getSingleResult();	
	}
	
	public void populateDefaultValue(ItemSimple itemSimple) {
		if (itemSimple.getSiteProfileClassId() == null && itemSimple.getSiteProfileClassName() == null) {
			itemSimple.setSiteProfileClassId(site.getSiteProfileClassDefault().getSiteProfileClassId());
		}
		if (itemSimple.getSiteCurrencyClassId() == null && itemSimple.getSiteCurrencyClassName() == null) {
			itemSimple.setSiteCurrencyClassId(site.getSiteCurrencyClassDefault().getSiteCurrencyClassId());
		}
		if (Format.isNullOrEmpty(itemSimple.getItemTypeCd())) {
			itemSimple.setItemTypeCd(Constants.ITEM_TYPE_REGULAR);
		}
		if (itemSimple.getItemSellable() == ' ') {
			itemSimple.setItemSellable(Constants.VALUE_YES);
		}
		if (itemSimple.getItemPublishOn() == null) {
			itemSimple.setItemPublishOn(Format.LOWDATE);
		}
		if (itemSimple.getItemExpireOn() == null) {
			itemSimple.setItemExpireOn(Format.HIGHDATE);
		}
		if (itemSimple.getPublished() == ' ') {
			itemSimple.setPublished(Constants.VALUE_YES);
		}
		if (itemSimple.getShippingTypeId() == null && itemSimple.getShippingTypeName() == null) {
			itemSimple.setShippingTypeId(shippingType.getShippingTypeId());
		}
		if (itemSimple.getProductClassId() == null && itemSimple.getProductClassName() == null) {
			itemSimple.setProductClassId(productClass.getProductClassId());
		}
		
		for (ItemSimpleItemTierPrice itemTierPrice : itemSimple.getItemTierPrices()) {
			/*
			if (itemTierPrice.getCustClassId() == null && itemTierPrice.getCustClassName() == null) {
				itemTierPrice.setCustClassId(customerClass.getCustClassId());
			}
			*/
			if (itemTierPrice.getItemTierPricePublishOn() == null) {
				itemTierPrice.setItemTierPricePublishOn(Format.LOWDATE);
			}
			if (itemTierPrice.getItemTierPriceExpireOn() == null) {
				itemTierPrice.setItemTierPriceExpireOn(Format.HIGHDATE);
			}
		}
		
		boolean defaultImageFound = false;
		for (ItemSimpleImage itemImage : itemSimple.getItemImages()) {
			if (Format.isNullOrEmpty(itemImage.getDefaultImage())) {
				itemImage.setDefaultImage(String.valueOf(Constants.VALUE_NO));
			}
			if (itemImage.getDefaultImage().equals(String.valueOf(Constants.VALUE_YES))) {
				defaultImageFound = true;
			}
		}
		if (!defaultImageFound && itemSimple.getItemImages().length > 0) {
			itemSimple.getItemImages()[0].setDefaultImage(String.valueOf(Constants.VALUE_YES));
		}
	}
}
