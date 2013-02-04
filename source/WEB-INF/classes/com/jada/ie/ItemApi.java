package com.jada.ie;


import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.jada.dao.ItemDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Category;
import com.jada.jpa.entity.CustomAttribute;
import com.jada.jpa.entity.CustomAttributeGroup;
import com.jada.jpa.entity.CustomAttributeOption;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.ItemAttributeDetail;
import com.jada.jpa.entity.ItemAttributeDetailLanguage;
import com.jada.jpa.entity.ItemImage;
import com.jada.jpa.entity.ItemLanguage;
import com.jada.jpa.entity.ItemPriceCurrency;
import com.jada.jpa.entity.ItemTierPrice;
import com.jada.jpa.entity.ItemTierPriceCurrency;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteCurrencyClass;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.util.Constants;

public class ItemApi {
	Site site = null;
	Item item = null;
	private SiteProfileClass siteProfileClasses[];
	private SiteCurrencyClass siteCurrencyClasses[];
	
	@SuppressWarnings("unchecked")
	public ItemApi(Site site) throws Exception {
		this.site = site;
		
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		
		String sql = "from SiteProfileClass siteProfileClass where siteProfileClass.site.siteId = :siteId";
		Query query = em.createQuery(sql);
		query.setParameter("siteId", site.getSiteId());
		siteProfileClasses = new SiteProfileClass[query.getResultList().size()];
		query.getResultList().toArray(siteProfileClasses);
		
		sql = "from SiteCurrencyClass siteCurrencyClass where siteCurrencyClass.site.siteId = :siteId";
		query = em.createQuery(sql);
		query.setParameter("siteId", site.getSiteId());
		siteCurrencyClasses = new SiteCurrencyClass[query.getResultList().size()];
		query.getResultList().toArray(siteCurrencyClasses);
	}
	
	public com.jada.xml.ie.Item export(Long itemId) throws SecurityException, Exception {
		Item item = ItemDAO.load(site.getSiteId(), itemId);
		return export(item);
	}
	
	public com.jada.xml.ie.Item export(String itemSkuCd) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String sql = "from  item " +
    				 "where item.site.siteId = :siteId " +
    				 "and   item.itemSkuCd = :itemSkuCd ";
    	Query query = em.createQuery(sql);
    	item = (Item) query.getSingleResult();
    	return export(item);
	}
	
	public com.jada.xml.ie.Item export(Item item) {
		com.jada.xml.ie.Item itemXml = new com.jada.xml.ie.Item();
		itemXml.setSiteId(item.getSiteId());
		itemXml.setItemId(item.getItemId());
		itemXml.setItemNum(item.getItemNum());
		itemXml.setItemUpcCd(item.getItemUpcCd());
		itemXml.setItemSkuCd(item.getItemSkuCd());
		itemXml.setItemTypeCd(item.getItemTypeCd());
		itemXml.setItemSellable(item.getItemSellable());
		itemXml.setItemCost(item.getItemCost());
		itemXml.setItemHitCounter(item.getItemHitCounter());
		itemXml.setItemRating(item.getItemRating());
		itemXml.setItemRatingCount(item.getItemRatingCount());
		itemXml.setItemQty(item.getItemQty());
		itemXml.setItemBookedQty(item.getItemBookedQty());
		itemXml.setItemPublishOn(item.getItemPublishOn());
		itemXml.setItemExpireOn(item.getItemExpireOn());
		itemXml.setPublished(item.getPublished());
		itemXml.setRecUpdateBy(item.getRecUpdateBy());
		itemXml.setRecUpdateDatetime(item.getRecUpdateDatetime());
		itemXml.setRecCreateBy(item.getRecCreateBy());
		itemXml.setRecCreateDatetime(item.getRecCreateDatetime());
		
		for (ItemLanguage itemLanguage : item.getItemLanguages()) {
			com.jada.xml.ie.ItemLanguage itemLanguageXml = new com.jada.xml.ie.ItemLanguage();
			itemLanguageXml.setItemShortDesc(itemLanguage.getItemShortDesc());
			itemLanguageXml.setItemDesc(itemLanguage.getItemDesc());
			itemLanguageXml.setPageTitle(itemLanguage.getPageTitle());
			itemLanguageXml.setMetaKeywords(itemLanguage.getMetaKeywords());
			itemLanguageXml.setMetaDescription(itemLanguage.getMetaDescription());
			itemLanguageXml.setItemImageOverride(itemLanguage.getItemImageOverride());
			itemLanguageXml.setRecUpdateBy(itemLanguage.getRecUpdateBy());
			itemLanguageXml.setRecUpdateDatetime(itemLanguage.getRecUpdateDatetime());
			itemLanguageXml.setRecCreateBy(itemLanguage.getRecCreateBy());
			itemLanguageXml.setRecCreateDatetime(itemLanguage.getRecCreateDatetime());
			itemLanguageXml.setSiteProfileClassId(itemLanguage.getSiteProfileClass().getSiteProfileClassId());
			itemLanguageXml.setSiteProfileClassName(itemLanguage.getSiteProfileClass().getSiteProfileClassName());
			
			if (itemLanguage.getImage() != null) {
				com.jada.xml.ie.ItemImage itemImageXml = new com.jada.xml.ie.ItemImage();
				itemImageXml.setImageValue(itemLanguage.getImage().getImageValue());
				itemImageXml.setDefaultImage(String.valueOf(Constants.VALUE_YES));
				itemLanguageXml.getImages().add(itemImageXml);
				for (ItemImage itemImage : itemLanguage.getImages()) {
					itemImageXml = new com.jada.xml.ie.ItemImage();
					itemImageXml.setImageValue(itemImage.getImageValue());
					itemImageXml.setDefaultImage(String.valueOf(Constants.VALUE_NO));
					itemLanguageXml.getImages().add(itemImageXml);
				}
			}
			itemXml.getItemLanguages().add(itemLanguageXml);
		}
		
		for (ItemPriceCurrency itemPriceCurrency : item.getItemPriceCurrencies()) {
			com.jada.xml.ie.ItemPriceCurrency itemPriceCurrencyXml = new com.jada.xml.ie.ItemPriceCurrency();
			itemPriceCurrencyXml.setItemPrice(itemPriceCurrency.getItemPrice());
			itemPriceCurrencyXml.setItemPriceTypeCode(itemPriceCurrency.getItemPriceTypeCode());
			itemPriceCurrencyXml.setItemPricePublishOn(itemPriceCurrency.getItemPricePublishOn());
			itemPriceCurrencyXml.setItemPriceExpireOn(itemPriceCurrency.getItemPriceExpireOn());
			itemPriceCurrencyXml.setRecUpdateBy(itemPriceCurrency.getRecUpdateBy());
			itemPriceCurrencyXml.setRecUpdateDatetime(itemPriceCurrency.getRecUpdateDatetime());
			itemPriceCurrencyXml.setRecCreateBy(itemPriceCurrency.getRecCreateBy());
			itemPriceCurrencyXml.setRecCreateDatetime(itemPriceCurrency.getRecCreateDatetime());
			itemPriceCurrencyXml.setSiteCurrencyClassId(itemPriceCurrency.getSiteCurrencyClass().getSiteCurrencyClassId());
			itemPriceCurrencyXml.setSiteCurrencyClassName(itemPriceCurrency.getSiteCurrencyClass().getSiteCurrencyClassName());			
			itemXml.getItemPriceCurrencies().add(itemPriceCurrencyXml);
		}
		
		itemXml.setShippingTypeId(item.getShippingType().getShippingTypeId());
		itemXml.setShippingTypeName(item.getShippingType().getShippingTypeName());
		itemXml.setProductClassId(item.getProductClass().getProductClassId());
		itemXml.setProductClassName(item.getProductClass().getProductClassName());
		
		Item parent = item.getItemSkuParent();
		if (parent != null) {
			itemXml.setItemParentId(parent.getItemId());
			itemXml.setItemParentSkuCd(parent.getItemSkuCd());
		}
		
		for (Category category : item.getCategories()) {
			com.jada.xml.ie.Category categoryXml = new com.jada.xml.ie.Category();
			categoryXml.setCatId(category.getCatId());
			categoryXml.setCatShortTitle(category.getCategoryLanguage().getCatShortTitle());
			itemXml.getCategories().add(categoryXml);
		}
		
		for (Item i : item.getItemsRelated()) {
			com.jada.xml.ie.Item iXml = new com.jada.xml.ie.Item();
			iXml.setItemId(i.getItemId());
			iXml.setItemSkuCd(i.getItemSkuCd());
			itemXml.getItemsRelated().add(iXml);
		}
		
		for (Item i : item.getItemsUpSell()) {
			com.jada.xml.ie.Item iXml = new com.jada.xml.ie.Item();
			iXml.setItemId(i.getItemId());
			iXml.setItemSkuCd(i.getItemSkuCd());
			itemXml.getItemsUpSell().add(iXml);
		}
		
		for (Item i : item.getItemsCrossSell()) {
			com.jada.xml.ie.Item iXml = new com.jada.xml.ie.Item();
			iXml.setItemId(i.getItemId());
			iXml.setItemSkuCd(i.getItemSkuCd());
			itemXml.getItemsCrossSell().add(iXml);
		}
		
		for (ItemTierPrice itemTierPrice : item.getItemTierPrices()) {
			com.jada.xml.ie.ItemTierPrice itemTierPriceXml = new com.jada.xml.ie.ItemTierPrice();
			itemTierPriceXml.setItemTierQty(itemTierPrice.getItemTierQty());
			itemTierPriceXml.setItemTierPricePublishOn(itemTierPrice.getItemTierPricePublishOn());
			itemTierPriceXml.setItemTierPriceExpireOn(itemTierPrice.getItemTierPriceExpireOn());
			itemTierPriceXml.setRecUpdateBy(itemTierPrice.getRecUpdateBy());
			itemTierPriceXml.setRecUpdateDatetime(itemTierPrice.getRecUpdateDatetime());
			itemTierPriceXml.setRecCreateBy(itemTierPrice.getRecCreateBy());
			itemTierPriceXml.setRecCreateDatetime(itemTierPrice.getRecCreateDatetime());
			for (ItemTierPriceCurrency itemTierPriceCurrency : itemTierPrice.getItemTierPriceCurrencies()) {
				com.jada.xml.ie.ItemTierPriceCurrency itemTierPriceCurrencyXml = new com.jada.xml.ie.ItemTierPriceCurrency();
				itemTierPriceCurrencyXml.setItemPrice(itemTierPriceCurrency.getItemPrice());
				itemTierPriceCurrencyXml.setRecUpdateBy(itemTierPriceCurrency.getRecUpdateBy());
				itemTierPriceCurrencyXml.setRecUpdateDatetime(itemTierPriceCurrency.getRecUpdateDatetime());
				itemTierPriceCurrencyXml.setRecCreateBy(itemTierPriceCurrency.getRecCreateBy());
				itemTierPriceCurrencyXml.setRecCreateDatetime(itemTierPriceCurrency.getRecCreateDatetime());
				itemTierPriceCurrencyXml.setSiteCurrencyClassId(itemTierPriceCurrency.getSiteCurrencyClass().getSiteCurrencyClassId());
				itemTierPriceCurrencyXml.setSiteCurrencyClassName(itemTierPriceCurrency.getSiteCurrencyClass().getSiteCurrencyClassName());
				itemTierPriceXml.getItemTierPriceCurrencies().add(itemTierPriceCurrencyXml);
			}
			itemXml.getItemTierPrices().add(itemTierPriceXml);
		}
		
		CustomAttributeGroup customAttributeGroup = item.getCustomAttributeGroup();
		if (customAttributeGroup != null) {
			itemXml.setCustomAttributeGroupId(customAttributeGroup.getCustomAttribGroupId());
			itemXml.setCustomAttributeGroupName(customAttributeGroup.getCustomAttribGroupName());
			
			for (ItemAttributeDetail itemAttributeDetail : item.getItemAttributeDetails()) {
				com.jada.xml.ie.ItemAttributeDetail itemAttributeDetailXml = new com.jada.xml.ie.ItemAttributeDetail();
				CustomAttribute customAttribute = itemAttributeDetail.getCustomAttributeDetail().getCustomAttribute();
				itemAttributeDetailXml.setCustomAttribId(customAttribute.getCustomAttribId());
				itemAttributeDetailXml.setCustomAttribName(customAttribute.getCustomAttribName());
				CustomAttributeOption customAttributeOption = itemAttributeDetail.getCustomAttributeOption();
				if (customAttributeOption != null) {
					itemAttributeDetailXml.setCustomAttribOptionId(customAttributeOption.getCustomAttribOptionId());
					if (customAttributeOption.getCustomAttributeOptionLanguage() != null) {
						itemAttributeDetailXml.setCustomAttribValue(customAttributeOption.getCustomAttributeOptionLanguage().getCustomAttribValue());
					}
					else {
						itemAttributeDetailXml.setCustomAttribValue(customAttributeOption.getCustomAttributeOptionCurrency().getCustomAttribValue());
					}
					itemAttributeDetailXml.setRecUpdateBy(customAttributeOption.getRecCreateBy());
					itemAttributeDetailXml.setRecUpdateDatetime(customAttributeOption.getRecCreateDatetime());
					itemAttributeDetailXml.setRecCreateBy(customAttributeOption.getRecCreateBy());
					itemAttributeDetailXml.setRecCreateDatetime(customAttributeOption.getRecCreateDatetime());
				}
				for (ItemAttributeDetailLanguage itemAttributeDetailLanguage : itemAttributeDetail.getItemAttributeDetailLanguages()) {
					com.jada.xml.ie.ItemAttributeDetailLanguage itemAttributeDetailLanguageXml = new com.jada.xml.ie.ItemAttributeDetailLanguage();
					itemAttributeDetailLanguageXml.setItemAttribDetailValue(itemAttributeDetailLanguage.getItemAttribDetailValue());
					itemAttributeDetailLanguageXml.setRecUpdateBy(itemAttributeDetailLanguage.getRecCreateBy());
					itemAttributeDetailLanguageXml.setRecUpdateDatetime(itemAttributeDetailLanguage.getRecCreateDatetime());
					itemAttributeDetailLanguageXml.setRecCreateBy(itemAttributeDetailLanguage.getRecCreateBy());
					itemAttributeDetailLanguageXml.setRecCreateDatetime(itemAttributeDetailLanguage.getRecCreateDatetime());
					SiteProfileClass siteProfileClass = itemAttributeDetailLanguage.getSiteProfileClass();
					itemAttributeDetailLanguageXml.setSiteProfileClassId(siteProfileClass.getSiteProfileClassId());
					itemAttributeDetailLanguageXml.setSiteProfileClassName(siteProfileClass.getSiteProfileClassName());
					itemAttributeDetailXml.getItemAttributeDetailLanguages().add(itemAttributeDetailLanguageXml);
				}
				itemXml.getItemAttributeDetails().add(itemAttributeDetailXml);
			}
		}

		return itemXml;
	}
}
