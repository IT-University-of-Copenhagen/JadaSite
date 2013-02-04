package com.jada.xml.ie;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Vector;

import javax.persistence.EntityManager;

import org.apache.struts.util.MessageResources;

import com.jada.admin.AdminBean;
import com.jada.dao.CategoryDAO;
import com.jada.dao.CustomAttributeGroupDAO;
import com.jada.dao.ItemDAO;
import com.jada.dao.ProductClassDAO;
import com.jada.dao.ShippingTypeDAO;
import com.jada.dao.SiteCurrencyClassDAO;
import com.jada.dao.SiteDAO;
import com.jada.dao.SiteProfileClassDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.CustomAttribute;
import com.jada.jpa.entity.CustomAttributeDetail;
import com.jada.jpa.entity.CustomAttributeGroup;
import com.jada.jpa.entity.CustomAttributeOption;
import com.jada.jpa.entity.ItemTierPrice;
import com.jada.jpa.entity.ProductClass;
import com.jada.jpa.entity.ShippingType;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteCurrencyClass;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.search.Indexer;
import com.jada.util.CategorySearchUtil;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.ImageScaler;
import com.jada.util.Utility;

public class Item {
	private boolean simple;
	private String siteId;
	private Long itemId;
	private String itemNum;
	private String itemUpcCd;
	private String itemSkuCd;
	private String itemTypeCd;
	private char itemSellable;
	private Float itemCost;
	private Integer itemHitCounter;
	private Float itemRating;
	private Integer itemRatingCount;
	private Integer itemQty;
	private Integer itemBookedQty;
	private Date itemPublishOn;
	private Date itemExpireOn;
	private char published;
	private String recUpdateBy;
	private Date recUpdateDatetime;
	private String recCreateBy;
	private Date recCreateDatetime;
	private Vector<ItemLanguage> itemLanguages = new Vector<ItemLanguage>(0);
	private Vector<ItemPriceCurrency> itemPriceCurrencies = new Vector<ItemPriceCurrency>(
			0);
	private Long shippingTypeId;
	private String shippingTypeName;
	private Long productClassId;
	private String productClassName;
	private Long itemParentId;
	private String itemParentSkuCd;
	private Long customAttributeGroupId;
	private String customAttributeGroupName;
	private Vector<Category> categories = new Vector<Category>(0);
	private Vector<Item> itemsRelated = new Vector<Item>(0);
	private Vector<Item> itemsUpSell = new Vector<Item>(0);
	private Vector<Item> itemsCrossSell = new Vector<Item>(0);
	private Vector<com.jada.xml.ie.ItemTierPrice> itemTierPrices = new Vector<com.jada.xml.ie.ItemTierPrice>(0);
	private Vector<ItemAttributeDetail> itemAttributeDetails = new Vector<ItemAttributeDetail>(
			0);
	private Vector<ItemImage> itemImages = new Vector<ItemImage>(
			0);
	private Long siteCurrencyClassId;
	private Long siteProfileClassId;
	private boolean defaultCurrency;
	private boolean defaultProfile;
	// TODO assigns items to sub-sites
	public Item() {
	}
	
	public Item(String siteId, ItemSimple itemSimple) throws IOException, ParseException {
		simple = true;
		siteCurrencyClassId = itemSimple.getSiteCurrencyClassId();
		siteProfileClassId = itemSimple.getSiteProfileClassId();
		defaultCurrency = itemSimple.isDefaultCurrency();
		defaultProfile = itemSimple.isDefaultProfile();
		
		this.siteId = siteId;
		this.itemId = itemSimple.getItemId();
		this.itemNum = itemSimple.getItemNum();
		this.itemUpcCd = itemSimple.getItemUpcCd();
		this.itemSkuCd = itemSimple.getItemSkuCd();
		this.itemTypeCd = itemSimple.getItemTypeCd();
		this.itemSellable = itemSimple.getItemSellable();
		this.itemCost = itemSimple.getItemCost();
		this.itemHitCounter = itemSimple.getItemHitCounter();
		this.itemRating = itemSimple.getItemRating();
		this.itemRatingCount = itemSimple.getItemRatingCount();
		this.itemQty = itemSimple.getItemQty();
		this.itemBookedQty = itemSimple.getItemBookedQty();
		this.itemPublishOn = itemSimple.getItemPublishOn();
		this.itemExpireOn = itemSimple.getItemExpireOn();
		this.published = itemSimple.getPublished();
		this.recUpdateBy = itemSimple.getRecUpdateBy();
		this.recUpdateDatetime = itemSimple.getRecUpdateDatetime();
		this.recCreateBy = itemSimple.getRecCreateBy();
		this.recCreateDatetime = itemSimple.getRecCreateDatetime();
	
		ItemLanguage itemLanguage = new ItemLanguage();
		itemLanguage.setItemShortDesc(itemSimple.getItemShortDesc());
		itemLanguage.setItemDesc(itemSimple.getItemDesc());
		itemLanguage.setPageTitle(itemSimple.getPageTitle());
		itemLanguage.setMetaKeywords(itemSimple.getMetaKeywords());
		itemLanguage.setMetaDescription(itemSimple.getMetaDescription());
		itemLanguage.setRecUpdateBy(itemSimple.getRecUpdateBy());
		itemLanguage.setRecUpdateDatetime(itemSimple.getRecUpdateDatetime());
		itemLanguage.setRecCreateBy(itemSimple.getRecCreateBy());
		itemLanguage.setRecCreateDatetime(itemSimple.getRecCreateDatetime());
		itemLanguage.setSiteProfileClassId(itemSimple.getSiteProfileClassId());
		itemLanguage.setSiteProfileClassName(itemSimple.getSiteProfileClassName());
		this.itemLanguages.add(itemLanguage);
		
		itemLanguage.setItemImageOverride(itemSimple.getItemImageOverride());
		if (itemSimple.getItemImages() != null) {
			for (ItemSimpleImage itemSimpleImage : itemSimple.getItemImages()) {
				if (Format.isNullOrEmpty(itemSimpleImage.getItemImageLocation())) {
					continue;
				}
				ItemImage itemImage = new ItemImage();
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				FileInputStream inputStream = new FileInputStream(new File(itemSimpleImage.getItemImageLocation()));
				while (true) {
					byte buffer[] = new byte[1024];
					int i = inputStream.read(buffer);
					if (i == -1) {
						break;
					}
					outputStream.write(buffer, 0, i);
				}
				ImageScaler scaler = new ImageScaler(outputStream.toByteArray(), "image/jpeg");
				scaler.resize(600);
				itemImage.setImageValue(scaler.getBytes());
				itemImage.setDefaultImage(itemSimpleImage.getDefaultImage());
				itemLanguage.getImages().add(itemImage);
			}
		}
		
		if (itemSimple.getItemPrice() != null) {
			ItemPriceCurrency itemPriceCurrency = new ItemPriceCurrency();
			itemPriceCurrency.setItemPrice(itemSimple.getItemPrice());
			itemPriceCurrency.setItemPriceTypeCode(Constants.ITEM_PRICE_TYPE_CODE_REGULAR);
			itemPriceCurrency.setRecUpdateBy(itemSimple.getRecUpdateBy());
			itemPriceCurrency.setRecUpdateDatetime(itemSimple.getRecUpdateDatetime());
			itemPriceCurrency.setRecCreateBy(itemSimple.getRecCreateBy());
			itemPriceCurrency.setRecCreateDatetime(itemSimple.getRecCreateDatetime());
			itemPriceCurrency.setSiteCurrencyClassId(itemSimple.getSiteCurrencyClassId());
			itemPriceCurrency.setSiteCurrencyClassName(itemSimple.getSiteCurrencyClassName());
			this.getItemPriceCurrencies().add(itemPriceCurrency);
		}
		if (itemSimple.getItemSpecPrice() != null) {
			ItemPriceCurrency itemPriceCurrency = new ItemPriceCurrency();
			itemPriceCurrency.setItemPrice(itemSimple.getItemSpecPrice());
			itemPriceCurrency.setItemPriceTypeCode(Constants.ITEM_PRICE_TYPE_CODE_SPECIAL);
			itemPriceCurrency.setItemPricePublishOn(itemSimple.getItemSpecPricePublishOn());
			itemPriceCurrency.setItemPriceExpireOn(itemSimple.getItemSpecPriceExpireOn());
			itemPriceCurrency.setRecUpdateBy(itemSimple.getRecUpdateBy());
			itemPriceCurrency.setRecUpdateDatetime(itemSimple.getRecUpdateDatetime());
			itemPriceCurrency.setRecCreateBy(itemSimple.getRecCreateBy());
			itemPriceCurrency.setRecCreateDatetime(itemSimple.getRecCreateDatetime());
			itemPriceCurrency.setSiteCurrencyClassId(itemSimple.getSiteCurrencyClassId());
			itemPriceCurrency.setSiteCurrencyClassName(itemSimple.getSiteCurrencyClassName());
			this.getItemPriceCurrencies().add(itemPriceCurrency);
		}
		
		this.shippingTypeId = itemSimple.getShippingTypeId();
		this.shippingTypeName = itemSimple.getShippingTypeName();
		this.productClassId = itemSimple.getProductClassId();
		this.productClassName = itemSimple.getProductClassName();
		this.itemParentId = itemSimple.getItemParentId();
		this.itemParentSkuCd = itemSimple.getItemParentSkuCd();
		
		if (itemSimple.getCategories() == null) {
			this.categories = null;
		}
		else {
			for (ItemSimpleCategory itemSimpleCategory : itemSimple.getCategories()) {
				if (itemSimpleCategory.getCatId() == null && Format.isNullOrEmpty(itemSimpleCategory.getCatShortTitle())) {
					continue;
				}
				Category category = new Category();
				if (itemSimpleCategory.getCatId() != null) {
					category.setCatId(itemSimpleCategory.getCatId());
				}
				category.setCatShortTitle(itemSimpleCategory.getCatShortTitle());
				this.categories.add(category);
			}
		}
		
		if (itemSimple.getItemsRelated() == null) {
			this.itemsRelated = null;
		}
		else {
			for (ItemSimple i : itemSimple.getItemsRelated()) {
				if (i.getItemId() == null && Format.isNullOrEmpty(i.getItemSkuCd())) {
					continue;
				}
				Item item = new Item();
				if (i.getItemId() != null) {
					item.setItemId(item.getItemId());
				}
				item.setItemSkuCd(i.getItemSkuCd());
				this.itemsRelated.add(item);
			}
		}
		
		if (itemSimple.getItemsUpSell() == null) {
			this.itemsUpSell = null;
		}
		else {
			for (ItemSimple i : itemSimple.getItemsUpSell()) {
				if (i.getItemId() == null && Format.isNullOrEmpty(i.getItemSkuCd())) {
					continue;
				}
				Item item = new Item();
				if (i.getItemId() != null) {
					item.setItemId(item.getItemId());
				}
				item.setItemSkuCd(i.getItemSkuCd());
				this.itemsUpSell.add(item);
			}
		}
		
		if (itemSimple.getItemsCrossSell() == null) {
			this.itemsCrossSell = null;
		}
		else {
			for (ItemSimple i : itemSimple.getItemsCrossSell()) {
				if (i.getItemId() == null && Format.isNullOrEmpty(i.getItemSkuCd())) {
					continue;
				}
				Item item = new Item();
				if (i.getItemId() != null) {
					item.setItemId(item.getItemId());
				}
				item.setItemSkuCd(i.getItemSkuCd());
				this.itemsCrossSell.add(item);
			}
		}
		
		if (itemSimple.getItemTierPrices() == null) {
			this.itemTierPrices = null;
		}
		else {
			for (ItemSimpleItemTierPrice i : itemSimple.getItemTierPrices()) {
				if (i.getItemTierQty() == null) {
					continue;
				}
				com.jada.xml.ie.ItemTierPrice itemTierPrice = new com.jada.xml.ie.ItemTierPrice();
				itemTierPrice.setCustClassId(i.getCustClassId());
				itemTierPrice.setCustClassName(i.getCustClassName());
				itemTierPrice.setItemTierQty(i.getItemTierQty());
				itemTierPrice.setItemTierPricePublishOn(i.getItemTierPricePublishOn());
				itemTierPrice.setItemTierPriceExpireOn(i.getItemTierPriceExpireOn());
				itemTierPrice.setRecUpdateBy(itemSimple.getRecUpdateBy());
				itemTierPrice.setRecUpdateDatetime(itemSimple.getRecUpdateDatetime());
				itemTierPrice.setRecCreateBy(itemSimple.getRecCreateBy());
				itemTierPrice.setRecCreateDatetime(itemSimple.getRecCreateDatetime());
				
				ItemTierPriceCurrency itemTierPriceCurrency = new ItemTierPriceCurrency();
				itemTierPriceCurrency.setItemPrice(i.getItemPrice());
				itemTierPriceCurrency.setRecUpdateBy(itemSimple.getRecUpdateBy());
				itemTierPriceCurrency.setRecUpdateDatetime(itemSimple.getRecUpdateDatetime());
				itemTierPriceCurrency.setRecCreateBy(itemSimple.getRecCreateBy());
				itemTierPriceCurrency.setRecCreateDatetime(itemSimple.getRecCreateDatetime());
				itemTierPriceCurrency.setSiteCurrencyClassId(itemSimple.getSiteCurrencyClassId());
				itemTierPriceCurrency.setSiteCurrencyClassName(itemSimple.getSiteCurrencyClassName());
				itemTierPrice.getItemTierPriceCurrencies().add(itemTierPriceCurrency);
				
				this.itemTierPrices.add(itemTierPrice);
			}
		}

		this.customAttributeGroupId = itemSimple.getCustomAttributeGroupId();
		this.customAttributeGroupName = itemSimple.getCustomAttributeGroupName();
		if (itemSimple.getItemAttributeDetails() == null) {
			this.itemAttributeDetails = null;
		}
		else {
			for (ItemSimpleItemAttributeDetail i : itemSimple.getItemAttributeDetails()) {
				if (i.getCustomAttribId() == null && Format.isNullOrEmpty(i.getCustomAttribName())) {
					continue;
				}
				ItemAttributeDetail itemAttributeDetail = new ItemAttributeDetail();
				itemAttributeDetail.setCustomAttribId(i.getCustomAttribId());
				itemAttributeDetail.setCustomAttribName(i.getCustomAttribName());
				itemAttributeDetail.setCustomAttribOptionId(i.getCustomAttribOptionId());
				itemAttributeDetail.setCustomAttribValue(i.getCustomAttribValue());
				
				if (!Format.isNullOrEmpty(i.getItemAttribDetailValue())) {
					ItemAttributeDetailLanguage itemAttributeDetailLanuage = new ItemAttributeDetailLanguage();
					itemAttributeDetailLanuage.setItemAttribDetailValue(i.getItemAttribDetailValue());
					itemAttributeDetailLanuage.setRecUpdateBy(itemSimple.getRecUpdateBy());
					itemAttributeDetailLanuage.setRecUpdateDatetime(itemSimple.getRecUpdateDatetime());
					itemAttributeDetailLanuage.setRecCreateBy(itemSimple.getRecCreateBy());
					itemAttributeDetailLanuage.setRecCreateDatetime(itemSimple.getRecCreateDatetime());
					itemAttributeDetailLanuage.setSiteProfileClassId(itemSimple.getSiteProfileClassId());
					itemAttributeDetailLanuage.setSiteProfileClassName(itemSimple.getSiteProfileClassName());
					itemAttributeDetail.getItemAttributeDetailLanguages().add(itemAttributeDetailLanuage);					
				}
				
				this.itemAttributeDetails.add(itemAttributeDetail);
			}
		}
	}
	
	public void save(boolean isSimple, AdminBean adminBean) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		MessageResources resources = MessageResources.getMessageResources("application");
		boolean isNew = false;
		Site site = SiteDAO.load(siteId);
		if (site == null) {
			throw new ItemSaveException(resources.getMessage("IE.ERROR.E109", "site", "siteId", siteId));
		}
		SiteProfileClass siteProfileClassDefault = site.getSiteProfileClassDefault();
		SiteCurrencyClass siteCurrencyClassDefault = site.getSiteCurrencyClassDefault();
		
		
		
		com.jada.jpa.entity.Item item = null;
		if (itemId != null) {
			item = ItemDAO.load(siteId, itemId);
			if (item == null) {
				throw new ItemSaveException(resources.getMessage("IE.ERROR.E109", "item", "itemId", itemId));
			}
			
		}
		else if (itemSkuCd != null) {
			item = ItemDAO.loadBySku(siteId, itemSkuCd);
		}
		if (item == null) {
			isNew = true;
			item = new com.jada.jpa.entity.Item();
			item.setItemUpcCd(itemSkuCd);
			item.setItemQty(0);
			item.setItemTypeCd(Constants.ITEM_TYPE_REGULAR);
			item.setItemBookedQty(0);
			item.setItemExpireOn(Format.HIGHDATE);
			item.setItemPublishOn(new Date());
			item.setItemHitCounter(0);
			item.setItemRating(Float.valueOf(0));
			item.setItemRatingCount(0);
			item.setSeqNum(0);
			item.setRecCreateBy(Constants.USERNAME_IMPORT);
			item.setRecCreateDatetime(new Date());
			item.setSite(site);
		}

		if (isNew) {
			if (isSimple) {
				boolean found = false;
				for (ItemLanguage itemLanguage : this.getItemLanguages()) {
					if (itemLanguage.getSiteProfileClassId() != null) {
						if (itemLanguage.getSiteProfileClassId().equals(siteProfileClassDefault.getSiteProfileClassId())) {
							found = true;
							break;
						}
					}
					else {
						if (itemLanguage.getSiteProfileClassName().equals(siteProfileClassDefault.getSiteProfileClassName())) {
							found = true;
							break;
						}
					}
				}
				if (!found) {
					throw new ItemSaveException(resources.getMessage("IE.ERROR.E111", siteProfileClassDefault.getSiteProfileClassName()));
				}
				found = false;
				for (ItemPriceCurrency itemPriceCurrency : this.getItemPriceCurrencies()) {
					if (itemPriceCurrency.getSiteCurrencyClassId() != null) {
						if (itemPriceCurrency.getSiteCurrencyClassId().equals(siteCurrencyClassDefault.getSiteCurrencyClassId())) {
							found = true;
							break;
						}
					}
					else {
						if (itemPriceCurrency.getSiteCurrencyClassName().equals(siteCurrencyClassDefault.getSiteCurrencyClassName())) {
							found = true;
							break;
						}
					}
				}
				if (!found) {
					throw new ItemSaveException(resources.getMessage("IE.ERROR.E112", siteCurrencyClassDefault.getSiteCurrencyClassName()));
				}
			}
		}
		
		item.setItemNum(itemNum);
		item.setItemSkuCd(itemSkuCd);
		item.setItemNaturalKey(Utility.encode(itemSkuCd));
		item.setItemSellable(itemSellable);
		item.setItemCost(itemCost);
		if (itemUpcCd != null) {
			item.setItemUpcCd(itemUpcCd);
		}
		if (itemTypeCd != null) {
			item.setItemTypeCd(itemTypeCd);
		}
		if (itemPublishOn != null) {
			item.setItemPublishOn(itemPublishOn);
		}
		if (itemExpireOn != null) {
			item.setItemExpireOn(itemExpireOn);
		}
		if (itemHitCounter != null) {
			item.setItemHitCounter(itemHitCounter);
		}
		if (itemRating != null) {
			item.setItemRating(itemRating);
		}
		if (itemRatingCount != null) {
			item.setItemRatingCount(itemRatingCount);
		}
		if (itemQty != null) {
			item.setItemQty(itemQty);
		}
		if (itemBookedQty != null) {
			item.setItemBookedQty(itemBookedQty);
		}
		item.setPublished(published);
		item.setRecUpdateBy(Constants.USERNAME_IMPORT);
		item.setRecUpdateDatetime(new Date());
		
		for (ItemLanguage itemLanguageImport : this.getItemLanguages()) {
			com.jada.jpa.entity.ItemLanguage itemLanguage = null;
			boolean found = false;
			for (com.jada.jpa.entity.ItemLanguage language : item.getItemLanguages()) {
				if (itemLanguageImport.getSiteProfileClassId() != null) {
					if (language.getSiteProfileClass().getSiteProfileClassId().equals(itemLanguageImport.getSiteProfileClassId())) {
						itemLanguage = language;
						found = true;
						break;
					}
				}
				else {
					if (language.getSiteProfileClass().getSiteProfileClassName().equals(itemLanguageImport.getSiteProfileClassName())) {
						itemLanguage = language;
						found = true;
						break;
					}
				}
			}
			if (!found) {
				itemLanguage = new com.jada.jpa.entity.ItemLanguage();
				itemLanguage.setItemImageOverride(String.valueOf(Constants.VALUE_NO));
				itemLanguage.setRecCreateBy(Constants.USERNAME_IMPORT);
				itemLanguage.setRecCreateDatetime(new Date());
				SiteProfileClass siteProfileClass = getSiteProfileClass(siteId, itemLanguageImport.getSiteProfileClassId(), itemLanguageImport.getSiteProfileClassName());
				if (siteProfileClass == null) {
					throw new ItemSaveException(resources.getMessage("IE.ERROR.E109", 
																	 "siteProfileClass", 
																	 "siteProfileClassId and siteProfileClassName", 
																	 itemLanguageImport.getSiteProfileClassId().toString() + " and " + itemLanguageImport.getSiteProfileClassName()));
				}
				itemLanguage.setSiteProfileClass(siteProfileClass);
				itemLanguage.setItem(item);
				
				if (itemLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassDefault.getSiteProfileClassId())) {
					item.setItemLanguage(itemLanguage);
				}
			}
			itemLanguage.setItemShortDesc(itemLanguageImport.getItemShortDesc());
			itemLanguage.setItemDesc(itemLanguageImport.getItemDesc());
			itemLanguage.setPageTitle(itemLanguageImport.getPageTitle());
			itemLanguage.setMetaKeywords(itemLanguageImport.getMetaKeywords());
			itemLanguage.setMetaDescription(itemLanguageImport.getMetaDescription());
			
			itemLanguage.setItemImageOverride(itemLanguageImport.getItemImageOverride());
			for (com.jada.jpa.entity.ItemImage image : itemLanguage.getImages()) {
				em.remove(image);
			}
			itemLanguage.getImages().clear();
			for (ItemImage imageImport : itemLanguageImport.getImages()) {
				com.jada.jpa.entity.ItemImage itemImage = new com.jada.jpa.entity.ItemImage();
				itemImage = new com.jada.jpa.entity.ItemImage();
				itemImage.setContentType("image/jpeg");
				itemImage.setImageName("");
				itemImage.setImageHeight(-1);
				itemImage.setImageWidth(-1);
				itemImage.setRecCreateBy(Constants.USERNAME_IMPORT);
				itemImage.setRecCreateDatetime(new Date());
				itemLanguage.setImage(itemImage);
				itemImage.setRecUpdateBy(Constants.USERNAME_IMPORT);
				itemImage.setRecUpdateDatetime(new Date());
				itemImage.setImageValue(imageImport.getImageValue());
				em.persist(itemImage);
				if (imageImport.getDefaultImage().equals(String.valueOf(Constants.VALUE_YES))) {
					itemLanguage.setImage(itemImage);
				}
			}
			
			itemLanguage.setRecUpdateBy(Constants.USERNAME_IMPORT);
			itemLanguage.setRecUpdateDatetime(new Date());
			em.persist(itemLanguage);
		}
		
		for (ItemPriceCurrency itemPriceCurrencyImport : this.getItemPriceCurrencies()) {
			com.jada.jpa.entity.ItemPriceCurrency itemPriceCurrency = null;
			boolean found = false;
			for (com.jada.jpa.entity.ItemPriceCurrency currency : item.getItemPriceCurrencies()) {
				if (currency.getItemPriceTypeCode() != itemPriceCurrencyImport.getItemPriceTypeCode()) {
					continue;
				}
				if (itemPriceCurrencyImport.getSiteCurrencyClassId() != null) {
					if (currency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(itemPriceCurrencyImport.getSiteCurrencyClassId())) {
						itemPriceCurrency = currency;
						found = true;
						break;
					}
				}
				else {
					if (currency.getSiteCurrencyClass().getSiteCurrencyClassName().equals(itemPriceCurrencyImport.getSiteCurrencyClassName())) {
						itemPriceCurrency = currency;
						found = true;
						break;
					}
				}
			}
			if (!found) {
				itemPriceCurrency = new com.jada.jpa.entity.ItemPriceCurrency();
				itemPriceCurrency.setItemPriceTypeCode(itemPriceCurrencyImport.getItemPriceTypeCode());
				itemPriceCurrency.setRecCreateBy(Constants.USERNAME_IMPORT);
				itemPriceCurrency.setRecCreateDatetime(new Date());
				SiteCurrencyClass siteCurrencyClass = getSiteCurrencyClass(siteId, itemPriceCurrencyImport.getSiteCurrencyClassId(), itemPriceCurrencyImport.getSiteCurrencyClassName());
				if (siteCurrencyClass == null) {
					throw new ItemSaveException(resources.getMessage("IE.ERROR.E109", 
																	 "siteCurrencyClass", 
																	 "siteCurrencyClassId and siteCurrencyClassName", 
																	 itemPriceCurrencyImport.getSiteCurrencyClassId().toString() + " and " + itemPriceCurrencyImport.getSiteCurrencyClassName()));
				}

				itemPriceCurrency.setSiteCurrencyClass(siteCurrencyClass);
				itemPriceCurrency.setItem(item);
				
				if (itemPriceCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteCurrencyClassDefault.getSiteCurrencyClassId())) {
					if (itemPriceCurrency.getItemPriceTypeCode() == Constants.ITEM_PRICE_TYPE_CODE_REGULAR) {
						item.setItemPrice(itemPriceCurrency);
					}
					else {
						item.setItemSpecPrice(itemPriceCurrency);
					}
				}
			}
			itemPriceCurrency.setItemPrice(itemPriceCurrencyImport.getItemPrice());
			itemPriceCurrency.setItemPricePublishOn(itemPriceCurrencyImport.getItemPricePublishOn());
			itemPriceCurrency.setItemPriceExpireOn(itemPriceCurrencyImport.getItemPriceExpireOn());
			itemPriceCurrency.setRecUpdateBy(Constants.USERNAME_IMPORT);
			itemPriceCurrency.setRecUpdateDatetime(new Date());
			em.persist(itemPriceCurrency);
			
			itemPriceCurrencyImport.setSiteCurrencyClassId(itemPriceCurrency.getSiteCurrencyClass().getSiteCurrencyClassId());
			
			if (isNew) {
				item.setItemPrice(itemPriceCurrency);
			}
			else {
				if (itemPriceCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteCurrencyClassDefault.getSiteCurrencyClassId())) {
					if (itemPriceCurrency.getItemPriceTypeCode() == Constants.ITEM_PRICE_TYPE_CODE_REGULAR) {
						item.setItemPrice(itemPriceCurrency);
					}
					else {
						item.setItemSpecPrice(itemPriceCurrency);
					}
				}
			}
		}
		for (ItemPriceCurrency itemPriceCurrencyImport : this.getItemPriceCurrencies()) {
			if (itemPriceCurrencyImport.getItemPriceTypeCode() != Constants.ITEM_PRICE_TYPE_CODE_REGULAR) {
				continue;
			}
			// look for corresponding special price
			boolean found = false;
			for (ItemPriceCurrency itemSpecPriceCurrencyImport : this.getItemPriceCurrencies()) {
				if (itemSpecPriceCurrencyImport.getItemPriceTypeCode() != Constants.ITEM_PRICE_TYPE_CODE_SPECIAL) {
					continue;
				}
				if (itemPriceCurrencyImport.getSiteCurrencyClassId().equals(itemSpecPriceCurrencyImport.getSiteCurrencyClassId())) {
					found = true;
					break;
				}
			}
			// if not found, ensure special price is removed.
			if (!found) {
				if (itemPriceCurrencyImport.getSiteCurrencyClassId().equals(siteCurrencyClassDefault.getSiteCurrencyClassId())) {
					item.setItemSpecPrice(null);
				}
				for (com.jada.jpa.entity.ItemPriceCurrency itemPriceCurrency : item.getItemPriceCurrencies()) {
					if (itemPriceCurrency.getItemPriceTypeCode() != Constants.ITEM_PRICE_TYPE_CODE_SPECIAL) {
						continue;
					}
					if (!itemPriceCurrencyImport.getSiteCurrencyClassId().equals(siteCurrencyClassDefault.getSiteCurrencyClassId())) {
						if (!itemPriceCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(itemPriceCurrencyImport.getSiteCurrencyClassId())) {
							continue;
						}
					}
					em.remove(itemPriceCurrency);
				}
			}
		}
		
		em.persist(item);
		
		CategorySearchUtil.itemPriceSearchUpdate(item, site, adminBean);
		CategorySearchUtil.itemDescSearchUpdate(item, site, adminBean);
		
		ShippingType shippingType = null;
		if (shippingTypeId != null) {
			shippingType = ShippingTypeDAO.load(siteId, shippingTypeId);
		}
		else {
			shippingType = ShippingTypeDAO.loadByName(siteId, shippingTypeName);
		}
		if (shippingType == null) {
			throw new ItemSaveException(resources.getMessage("IE.ERROR.E109", 
					 "shippingType", 
					 "shippingTypeId and shippingTypeName", 
					 shippingTypeId.toString() + " and " + shippingTypeName));
		}
		item.setShippingType(shippingType);
		
		ProductClass productClass = null;
		if (productClassId != null) {
			productClass = ProductClassDAO.load(siteId, productClassId);
		}
		else {
			productClass = ProductClassDAO.loadByName(siteId, productClassName);
		}
		if (productClass == null) {
			throw new ItemSaveException(resources.getMessage("IE.ERROR.E109", 
					 "productClass", 
					 "productClassId and productClassName", 
					 productClassId.toString() + " and " + productClassName));
		}
		item.setProductClass(productClass);
		
		if (itemParentId != null || itemParentSkuCd != null) {
			com.jada.jpa.entity.Item parent = null;
			if (itemParentId != null) {
				parent = ItemDAO.load(siteId, itemParentId);
			}
			else {
				parent = ItemDAO.loadBySku(siteId, itemParentSkuCd);
			}
			if (parent == null) {
				throw new ItemSaveException(resources.getMessage("IE.ERROR.E109", 
						 "item", 
						 "itemId and itemSkuCd", 
						 itemParentId.toString() + " and " + itemParentSkuCd));
			}
			item.setItemSkuParent(parent);
		}
		
		item.getCategories().clear();
		if (this.getCategories() != null) {
			for (Category categoryImport : this.getCategories()) {
				com.jada.jpa.entity.Category category = null;
				if (categoryImport.getCatId() != null) {
					category = CategoryDAO.load(siteId, categoryImport.getCatId());
				}
				else {
					category = CategoryDAO.loadByShortTitle(siteId, categoryImport.getCatShortTitle());
				}
				if (category == null) {
					throw new ItemSaveException(resources.getMessage("IE.ERROR.E109", 
							 "category", 
							 "catId and catShortTitle", 
							 categoryImport.getCatId().toString() + " and " + categoryImport.getCatShortTitle()));
				}
				item.getCategories().add(category);
			}
		}
		
		item.getItemsRelated().clear();
		if (this.getItemsRelated() != null) {
			for (Item i : this.getItemsRelated()) {
				com.jada.jpa.entity.Item child = null;
				if (i.getItemId() != null) {
					child = ItemDAO.load(siteId, i.getItemId());
				}
				else {
					child = ItemDAO.loadBySku(siteId, i.getItemSkuCd());
				}
				if (child == null) {
					throw new ItemSaveException(resources.getMessage("IE.ERROR.E109", 
							 "item", 
							 "itemId and itemSkuCd", 
							 i.getItemId().toString() + " and " + i.getItemSkuCd()));
				}
				item.getItemsRelated().add(child);
			}
		}
		
		item.getItemsUpSell().clear();
		if (this.getItemsUpSell() != null) {
			for (Item i : this.getItemsUpSell()) {
				com.jada.jpa.entity.Item child = null;
				if (i.getItemId() != null) {
					child = ItemDAO.load(siteId, i.getItemId());
				}
				else {
					child = ItemDAO.loadBySku(siteId, i.getItemSkuCd());
				}
				if (child == null) {
					throw new ItemSaveException(resources.getMessage("IE.ERROR.E109", 
							 "item", 
							 "itemId and itemSkuCd", 
							 i.getItemId().toString() + " and " + i.getItemSkuCd()));
				}
				item.getItemsUpSell().add(child);
			}
		}
		
		item.getItemsCrossSell().clear();
		if (this.getItemsCrossSell() != null) {
			for (Item i : this.getItemsCrossSell()) {
				com.jada.jpa.entity.Item child = null;
				if (i.getItemId() != null) {
					child = ItemDAO.load(siteId, i.getItemId());
				}
				else {
					child = ItemDAO.loadBySku(siteId, i.getItemSkuCd());
				}
				if (child == null) {
					throw new ItemSaveException(resources.getMessage("IE.ERROR.E109", 
							 "item", 
							 "itemId and itemSkuCd", 
							 i.getItemId().toString() + " and " + i.getItemSkuCd()));
				}
				item.getItemsCrossSell().add(child);
			}
		}
		
		if (this.getItemTierPrices() != null) {
			for (com.jada.xml.ie.ItemTierPrice itemTierPriceImport : this.getItemTierPrices()) {
				com.jada.jpa.entity.ItemTierPrice itemTierPrice = null;
				boolean foundTier = false;
				for (com.jada.jpa.entity.ItemTierPrice price : item.getItemTierPrices()) {
					if (!itemTierPriceImport.getItemTierQty().equals(price.getItemTierQty())) {
						continue;
					}
					if (price.getCustomerClass() == null) {
						if (itemTierPriceImport.getCustClassId() == null && itemTierPriceImport.getCustClassName() == null) {
							itemTierPrice = price;
							foundTier = true;
							break;
						}
					}
					else {
						if (itemTierPriceImport.getCustClassId() == null) {
							if (itemTierPriceImport.getCustClassName().equals(price.getCustomerClass().getCustClassName())) {
								itemTierPrice = price;
								foundTier = true;
								break;
							}
						}
						else {
							if (itemTierPriceImport.getCustClassId().equals(price.getCustomerClass().getCustClassId())) {
								itemTierPrice = price;
								foundTier = true;
								break;
							}
						}
					}
				}
				if (!foundTier) {
					if (!defaultCurrency && !defaultProfile) {
						throw new ItemSaveException("Item tier price does not exist in base record");
					}
					itemTierPrice = new com.jada.jpa.entity.ItemTierPrice();
					itemTierPrice.setRecCreateBy(Constants.USERNAME_IMPORT);
					itemTierPrice.setRecCreateDatetime(new Date());
					itemTierPrice.setItem(item);
				}
				itemTierPrice.setItemTierQty(itemTierPriceImport.getItemTierQty());
				itemTierPrice.setItemTierPricePublishOn(itemTierPriceImport.getItemTierPricePublishOn());
				itemTierPrice.setItemTierPriceExpireOn(itemTierPriceImport.getItemTierPriceExpireOn());
				itemTierPrice.setRecUpdateBy(Constants.USERNAME_IMPORT);
				itemTierPrice.setRecUpdateDatetime(new Date());
				
				for (ItemTierPriceCurrency itemTierPriceCurrencyImport : itemTierPriceImport.getItemTierPriceCurrencies()) {
					com.jada.jpa.entity.ItemTierPriceCurrency itemTierPriceCurrency = null;
					boolean found = false;
					for (com.jada.jpa.entity.ItemTierPriceCurrency currency : itemTierPrice.getItemTierPriceCurrencies()) {
						if (itemTierPriceCurrencyImport.getSiteCurrencyClassId() != null) {
							if (currency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(itemTierPriceCurrencyImport.getSiteCurrencyClassId())) {
								itemTierPriceCurrency = currency;
								found = true;
								break;
							}
						}
						else {
							if (currency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(itemTierPriceCurrencyImport.getSiteCurrencyClassName())) {
								itemTierPriceCurrency = currency;
								found = true;
								break;
							}
						}
					}
					if (itemTierPriceCurrencyImport.getItemPrice() == null) {
						if (found) {
							em.remove(itemTierPriceCurrency);
							itemTierPrice.getItemTierPriceCurrencies().remove(itemTierPriceCurrency);
						}
					}
					else {
						if (!found) {
							itemTierPriceCurrency = new com.jada.jpa.entity.ItemTierPriceCurrency();
							itemTierPriceCurrency.setRecCreateBy(Constants.USERNAME_IMPORT);
							itemTierPriceCurrency.setRecCreateDatetime(new Date());
							SiteCurrencyClass siteCurrencyClass = getSiteCurrencyClass(siteId, itemTierPriceCurrencyImport.getSiteCurrencyClassId(), 
																					   itemTierPriceCurrencyImport.getSiteCurrencyClassName());
							itemTierPriceCurrency.setSiteCurrencyClass(siteCurrencyClass);
							itemTierPriceCurrency.setItemTierPrice(itemTierPrice);
							if (siteCurrencyClass.getSiteCurrencyClassId().equals(siteCurrencyClassDefault.getSiteCurrencyClassId())) {
								itemTierPrice.setItemTierPriceCurrency(itemTierPriceCurrency);
							}
						}
						itemTierPriceCurrency.setItemPrice(itemTierPriceCurrencyImport.getItemPrice());
						itemTierPriceCurrency.setRecUpdateBy(Constants.USERNAME_IMPORT);
						itemTierPriceCurrency.setRecUpdateDatetime(new Date());
						em.persist(itemTierPriceCurrency);
					}
				}
				em.persist(itemTierPrice);
			}
		}
		
		if (simple) {
			Vector<ItemTierPrice> removeTierPriceList = new Vector<ItemTierPrice>();
			for (com.jada.jpa.entity.ItemTierPrice itemTierPrice : item.getItemTierPrices()) {
				boolean found = false;
				for (com.jada.xml.ie.ItemTierPrice itemTierPriceImport : this.getItemTierPrices()) {
					if (!itemTierPrice.getItemTierQty().equals(itemTierPriceImport.getItemTierQty())) {
						continue;
					}
					if (itemTierPrice.getCustomerClass() == null) {
						if (itemTierPriceImport.getCustClassId() == null) {
							found = true;
							break;
						}
						else {
							continue;
						}
					}
					else {
						if (itemTierPrice.getCustomerClass().getCustClassId().equals(itemTierPriceImport.getCustClassId())) {
							found = true;
							break;
						}
					}
				}
				if (!found) {
					removeTierPriceList.add(itemTierPrice);
					em.remove(itemTierPrice);
					for (com.jada.jpa.entity.ItemTierPriceCurrency itemTierPriceCurrency : itemTierPrice.getItemTierPriceCurrencies()) {
						em.remove(itemTierPriceCurrency);
					}
				}
			}
			// Remove from the collection in case it is accessed in the next line.
			for (ItemTierPrice p : removeTierPriceList) {
				item.getItemTierPrices().remove(p);
			}
		}

		CustomAttributeGroup customAttributeGroup = null;
		if (customAttributeGroupId != null || !Format.isNullOrEmpty(customAttributeGroupName)) {
			if (customAttributeGroupId != null) {
				customAttributeGroup = CustomAttributeGroupDAO.load(siteId, customAttributeGroupId);
			}
			else {
				customAttributeGroup = CustomAttributeGroupDAO.loadByName(siteId, customAttributeGroupName);
			}
			if (customAttributeGroup == null) {
				throw new ItemSaveException(resources.getMessage("IE.ERROR.E109", 
						 "customAttributeGroup", 
						 "customAttributeGroupId and customAttributeGroupName", 
						 customAttributeGroupId + " and " + customAttributeGroupName));
			}
			if (item.getCustomAttributeGroup() != null) {
				if (!item.getCustomAttributeGroup().getCustomAttribGroupId().equals(customAttributeGroup.getCustomAttribGroupId())) {
					throw new ItemSaveException(resources.getMessage("IE.ERROR.E123", customAttributeGroup.getCustomAttribGroupId(), customAttributeGroup.getCustomAttribGroupName()));
				}
			}
			else {
				item.setCustomAttributeGroup(customAttributeGroup);
			}
			
			for (CustomAttributeDetail customAttributeDetail : customAttributeGroup.getCustomAttributeDetails()) {
				CustomAttribute customAttribute = customAttributeDetail.getCustomAttribute();
				ItemAttributeDetail itemAttributeDetailImport = null;
				boolean found = false;
				for (ItemAttributeDetail detail : this.getItemAttributeDetails()) {
					if (detail.getCustomAttribId() != null) {
						if (detail.getCustomAttribId().equals(customAttribute.getCustomAttribId())) {
							itemAttributeDetailImport = detail;
							found = true;
							break;
						}
					}
					else {
						if (detail.getCustomAttribName().equals(customAttribute.getCustomAttribName())) {
							itemAttributeDetailImport = detail;
							found = true;
							break;
						}
					}
				}
				if (!found) {
					throw new ItemSaveException(resources.getMessage("IE.ERROR.E113", 
																	 customAttribute.getCustomAttribId(), 
																	 customAttribute.getCustomAttribName()));
				}
				if (customAttribute.getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_CUST_SELECT_DROPDOWN) {
					if (itemAttributeDetailImport.getItemAttributeDetailLanguages().size() > 0) {
						throw new ItemSaveException(resources.getMessage("IE.ERROR.E121", customAttribute.getCustomAttribName()));
					}
				}
				else if (customAttribute.getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_CUST_INPUT) {
					if (itemAttributeDetailImport.getCustomAttribId() != null ||
						Format.isNullOrEmpty(itemAttributeDetailImport.getCustomAttribName())) {
						throw new ItemSaveException(resources.getMessage("IE.ERROR.E117", customAttribute.getCustomAttribName()));
					}
					if (itemAttributeDetailImport.getItemAttributeDetailLanguages().size() > 0) {
						throw new ItemSaveException(resources.getMessage("IE.ERROR.E121", customAttribute.getCustomAttribName()));
					}
				}
				else if (customAttribute.getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_USER_SELECT_DROPDOWN) {
					if (itemAttributeDetailImport.getCustomAttribOptionId() == null &&
						Format.isNullOrEmpty(itemAttributeDetailImport.getCustomAttribValue())) {
						throw new ItemSaveException(resources.getMessage("IE.ERROR.E115", customAttribute.getCustomAttribName()));
					}
					if (itemAttributeDetailImport.getItemAttributeDetailLanguages().size() > 0) {
						throw new ItemSaveException(resources.getMessage("IE.ERROR.E121", customAttribute.getCustomAttribName()));
					}
				}
				else if (customAttribute.getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_USER_INPUT) {
					if (itemAttributeDetailImport.getCustomAttribOptionId() != null ||
						!Format.isNullOrEmpty(itemAttributeDetailImport.getCustomAttribValue())) {
						throw new ItemSaveException(resources.getMessage("IE.ERROR.E117", customAttribute.getCustomAttribName()));
					}
					if (this.isDefaultProfile()) {
						boolean valueFound = false;
						for (ItemAttributeDetailLanguage itemAttributeDetailLanguage : itemAttributeDetailImport.getItemAttributeDetailLanguages()) {
							if (itemAttributeDetailLanguage.getSiteProfileClassId().equals(siteProfileClassDefault.getSiteProfileClassId())) {
								if (Format.isNullOrEmpty(itemAttributeDetailLanguage.getItemAttribDetailValue())) {
									throw new ItemSaveException(resources.getMessage("IE.ERROR.E122", customAttribute.getCustomAttribName()));
								}
								valueFound = true;
							}
						}
						if (!valueFound) {
							throw new ItemSaveException(resources.getMessage("IE.ERROR.E122", customAttribute.getCustomAttribName()));
						}
					}
				}
				else if (customAttribute.getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP) {
					if (itemAttributeDetailImport.getCustomAttribOptionId() == null &&
						Format.isNullOrEmpty(itemAttributeDetailImport.getCustomAttribValue())) {
						throw new ItemSaveException(resources.getMessage("IE.ERROR.E115", customAttribute.getCustomAttribName()));
					}
				}
			}
			
			for (ItemAttributeDetail detail : this.getItemAttributeDetails()) {
				boolean found = false;
				for (CustomAttributeDetail customAttributeDetail : customAttributeGroup.getCustomAttributeDetails()) {
					CustomAttribute customAttribute = customAttributeDetail.getCustomAttribute();
					if (detail.getCustomAttribId() != null) {
						if (detail.getCustomAttribId().equals(customAttribute.getCustomAttribId())) {
							found = true;
							break;
						}
					}
					else {
						if (detail.getCustomAttribName().equals(customAttribute.getCustomAttribName())) {
							found = true;
							break;
						}
					}
				}
				if (!found) {
					throw new ItemSaveException(resources.getMessage("IE.ERROR.E119", detail.getCustomAttribId(), detail.getCustomAttribName()));
				}
			}
			
			for (ItemAttributeDetail itemAttributeDetailImport : this.getItemAttributeDetails()) {
				com.jada.jpa.entity.ItemAttributeDetail itemAttributeDetail = null;
				boolean foundDetail = false;
				for (com.jada.jpa.entity.ItemAttributeDetail detail : item.getItemAttributeDetails()) {
					CustomAttribute customAttribute = detail.getCustomAttributeDetail().getCustomAttribute();
					if (itemAttributeDetailImport.getCustomAttribId() != null) {
						if (itemAttributeDetailImport.getCustomAttribId().equals(customAttribute.getCustomAttribId())) {
							itemAttributeDetail = detail;
							foundDetail = true;
						}
					}
					else {
						if (itemAttributeDetailImport.getCustomAttribName().equals(customAttribute.getCustomAttribName())) {
							itemAttributeDetail = detail;
							foundDetail = true;
						}
					}
				}
				if (!foundDetail) {
					itemAttributeDetail = new com.jada.jpa.entity.ItemAttributeDetail();
					CustomAttributeDetail customAttributeDetail = null;
					for (CustomAttributeDetail d : customAttributeGroup.getCustomAttributeDetails()) {
						CustomAttribute customAttribute = d.getCustomAttribute();
						if (itemAttributeDetailImport.getCustomAttribOptionId() != null) {
							if (itemAttributeDetailImport.getCustomAttribId() == customAttribute.getCustomAttribId()) {
								customAttributeDetail = d;
								break;
							}
						}
						else {
							if (itemAttributeDetailImport.getCustomAttribName() == customAttribute.getCustomAttribName()) {
								customAttributeDetail = d;
								break;
							}
						}
					}
					itemAttributeDetail.setCustomAttributeDetail(customAttributeDetail);
					itemAttributeDetail.setRecCreateBy(Constants.USERNAME_IMPORT);
					itemAttributeDetail.setRecCreateDatetime(new Date());
					itemAttributeDetail.setItem(item);
				}
				if (itemAttributeDetailImport.getCustomAttribId() != null) {
					for (CustomAttributeDetail customAttributeDetail : customAttributeGroup.getCustomAttributeDetails()) {
						if (customAttributeDetail.getCustomAttribute().getCustomAttribId().equals(itemAttributeDetailImport.getCustomAttribId())) {
							itemAttributeDetail.setCustomAttributeDetail(customAttributeDetail);
							break;
						}
					}
				}
				if (!Format.isNullOrEmpty(itemAttributeDetailImport.getCustomAttribName())) {
					for (CustomAttributeDetail customAttributeDetail : customAttributeGroup.getCustomAttributeDetails()) {
						if (customAttributeDetail.getCustomAttribute().getCustomAttribName().equals(itemAttributeDetailImport.getCustomAttribName())) {
							itemAttributeDetail.setCustomAttributeDetail(customAttributeDetail);
							break;
						}
					}
				}
				CustomAttribute customAttribute = itemAttributeDetail.getCustomAttributeDetail().getCustomAttribute();
				boolean optionFound = false;
				if (customAttribute.getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_USER_SELECT_DROPDOWN ||
					customAttribute.getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP) {
					if (itemAttributeDetailImport.getCustomAttribId() != null) {
						for (CustomAttributeOption customAttributeOption : customAttribute.getCustomAttributeOptions()) {
							if (customAttributeOption.getCustomAttribOptionId().equals(itemAttributeDetailImport.getCustomAttribOptionId())) {
								itemAttributeDetail.setCustomAttributeOption(customAttributeOption);
								optionFound = true;
								break;
							}
						}
					}
					else {
						if (customAttribute.getCustomAttribDataTypeCode() == Constants.CUSTOM_ATTRIBUTE_DATA_TYPE_CURRENCY) {
							for (CustomAttributeOption customAttributeOption : customAttribute.getCustomAttributeOptions()) {
								if (customAttributeOption.getCustomAttributeOptionCurrency().getCustomAttribValue().equals(itemAttributeDetailImport.getCustomAttribValue())) {
									itemAttributeDetail.setCustomAttributeOption(customAttributeOption);
									optionFound = true;
									break;
								}
							}
						}
						else {
							for (CustomAttributeOption customAttributeOption : customAttribute.getCustomAttributeOptions()) {
								if (customAttributeOption.getCustomAttributeOptionLanguage().getCustomAttribValue().equals(itemAttributeDetailImport.getCustomAttribValue())) {
									itemAttributeDetail.setCustomAttributeOption(customAttributeOption);
									optionFound = true;
									break;
								}
							}
						}
					}
					if (!optionFound) {
						throw new ItemSaveException(resources.getMessage("IE.ERROR.E120", itemAttributeDetailImport.getCustomAttribOptionId(), itemAttributeDetailImport.getCustomAttribValue()));
					}
				}
				itemAttributeDetail.setRecUpdateBy(Constants.USERNAME_IMPORT);
				itemAttributeDetail.setRecUpdateDatetime(new Date());
				em.persist(itemAttributeDetail);
				
				for (ItemAttributeDetailLanguage itemAttributeDetailLanguageImport : itemAttributeDetailImport.getItemAttributeDetailLanguages()) {
					com.jada.jpa.entity.ItemAttributeDetailLanguage itemAttributeDetailLanguage = null;
					boolean found = false;
					for (com.jada.jpa.entity.ItemAttributeDetailLanguage language : itemAttributeDetail.getItemAttributeDetailLanguages()) {
						if (itemAttributeDetailLanguageImport.getSiteProfileClassId() != null) {
							if (language.getSiteProfileClass().getSiteProfileClassId().equals(itemAttributeDetailLanguageImport.getSiteProfileClassId())) {
								itemAttributeDetailLanguage = language;
								found = true;
								break;
							}
						}
						else {
							if (language.getSiteProfileClass().getSiteProfileClassId().equals(itemAttributeDetailLanguageImport.getSiteProfileClassName())) {
								itemAttributeDetailLanguage = language;
								found = true;
								break;
							}
						}
					}
					if (!found) {
						itemAttributeDetailLanguage = new com.jada.jpa.entity.ItemAttributeDetailLanguage();
						itemAttributeDetailLanguage.setRecCreateBy(Constants.USERNAME_IMPORT);
						itemAttributeDetailLanguage.setRecCreateDatetime(new Date());
						itemAttributeDetailLanguage.setSiteProfileClass(getSiteProfileClass(siteId, 
								itemAttributeDetailLanguageImport.getSiteProfileClassId(), 
								itemAttributeDetailLanguageImport.getSiteProfileClassName()));
						itemAttributeDetailLanguage.setItemAttributeDetail(itemAttributeDetail);
						if (itemAttributeDetailLanguageImport.getSiteProfileClassId().equals(siteProfileClassDefault.getSiteProfileClassId())) {
							itemAttributeDetail.setItemAttributeDetailLanguage(itemAttributeDetailLanguage);
						}
					}
					itemAttributeDetailLanguage.setItemAttribDetailValue(itemAttributeDetailLanguageImport.getItemAttribDetailValue());
					itemAttributeDetailLanguage.setRecUpdateBy(Constants.USERNAME_IMPORT);
					itemAttributeDetailLanguage.setRecUpdateDatetime(new Date());
					em.persist(itemAttributeDetailLanguage);
				}
			}
		}
		else {
			if (item.getCustomAttributeGroup() != null) {
				throw new ItemSaveException(resources.getMessage("IE.ERROR.E124"));
			}
		}

		em.persist(item);
		
		Indexer.getInstance(site.getSiteId()).updateItem(item);
	}
	private SiteProfileClass getSiteProfileClass(String siteId, Long siteProfileClassId, String siteProfileClassName) throws Exception {
		if (siteProfileClassId != null) {
			return SiteProfileClassDAO.load(siteProfileClassId);
		}
		else {
			return SiteProfileClassDAO.loadByName(siteId, siteProfileClassName);
		}
	}
	private SiteCurrencyClass getSiteCurrencyClass(String siteId, Long siteCurrencyClassId, String siteCurrencyClassName) throws Exception {
		if (siteCurrencyClassId != null) {
			return SiteCurrencyClassDAO.load(siteCurrencyClassId);
		}
		else {
			return SiteCurrencyClassDAO.loadByName(siteId, siteCurrencyClassName);
		}
	}
	
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getItemNum() {
		return itemNum;
	}
	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}
	public String getItemUpcCd() {
		return itemUpcCd;
	}
	public void setItemUpcCd(String itemUpcCd) {
		this.itemUpcCd = itemUpcCd;
	}
	public String getItemSkuCd() {
		return itemSkuCd;
	}
	public void setItemSkuCd(String itemSkuCd) {
		this.itemSkuCd = itemSkuCd;
	}
	public String getItemTypeCd() {
		return itemTypeCd;
	}
	public void setItemTypeCd(String itemTypeCd) {
		this.itemTypeCd = itemTypeCd;
	}
	public char getItemSellable() {
		return itemSellable;
	}
	public void setItemSellable(char itemSellable) {
		this.itemSellable = itemSellable;
	}
	public Float getItemCost() {
		return itemCost;
	}
	public void setItemCost(Float itemCost) {
		this.itemCost = itemCost;
	}
	public Integer getItemHitCounter() {
		return itemHitCounter;
	}
	public void setItemHitCounter(Integer itemHitCounter) {
		this.itemHitCounter = itemHitCounter;
	}
	public Float getItemRating() {
		return itemRating;
	}
	public void setItemRating(Float itemRating) {
		this.itemRating = itemRating;
	}
	public Integer getItemRatingCount() {
		return itemRatingCount;
	}
	public void setItemRatingCount(Integer itemRatingCount) {
		this.itemRatingCount = itemRatingCount;
	}
	public Integer getItemQty() {
		return itemQty;
	}
	public void setItemQty(Integer itemQty) {
		this.itemQty = itemQty;
	}
	public Integer getItemBookedQty() {
		return itemBookedQty;
	}
	public void setItemBookedQty(Integer itemBookedQty) {
		this.itemBookedQty = itemBookedQty;
	}
	public Date getItemPublishOn() {
		return itemPublishOn;
	}
	public void setItemPublishOn(Date itemPublishOn) {
		this.itemPublishOn = itemPublishOn;
	}
	public Date getItemExpireOn() {
		return itemExpireOn;
	}
	public void setItemExpireOn(Date itemExpireOn) {
		this.itemExpireOn = itemExpireOn;
	}
	public char getPublished() {
		return published;
	}
	public void setPublished(char published) {
		this.published = published;
	}
	public String getRecUpdateBy() {
		return recUpdateBy;
	}
	public void setRecUpdateBy(String recUpdateBy) {
		this.recUpdateBy = recUpdateBy;
	}
	public Date getRecUpdateDatetime() {
		return recUpdateDatetime;
	}
	public void setRecUpdateDatetime(Date recUpdateDatetime) {
		this.recUpdateDatetime = recUpdateDatetime;
	}
	public String getRecCreateBy() {
		return recCreateBy;
	}
	public void setRecCreateBy(String recCreateBy) {
		this.recCreateBy = recCreateBy;
	}
	public Date getRecCreateDatetime() {
		return recCreateDatetime;
	}
	public void setRecCreateDatetime(Date recCreateDatetime) {
		this.recCreateDatetime = recCreateDatetime;
	}
	public Vector<ItemLanguage> getItemLanguages() {
		return itemLanguages;
	}
	public void setItemLanguages(Vector<ItemLanguage> itemLanguages) {
		this.itemLanguages = itemLanguages;
	}
	public Vector<ItemPriceCurrency> getItemPriceCurrencies() {
		return itemPriceCurrencies;
	}
	public void setItemPriceCurrencies(Vector<ItemPriceCurrency> itemPriceCurrencies) {
		this.itemPriceCurrencies = itemPriceCurrencies;
	}
	public Long getShippingTypeId() {
		return shippingTypeId;
	}
	public void setShippingTypeId(Long shippingTypeId) {
		this.shippingTypeId = shippingTypeId;
	}
	public String getShippingTypeName() {
		return shippingTypeName;
	}
	public void setShippingTypeName(String shippingTypeName) {
		this.shippingTypeName = shippingTypeName;
	}
	public Long getProductClassId() {
		return productClassId;
	}
	public void setProductClassId(Long productClassId) {
		this.productClassId = productClassId;
	}
	public String getProductClassName() {
		return productClassName;
	}
	public void setProductClassName(String productClassName) {
		this.productClassName = productClassName;
	}
	public Long getItemParentId() {
		return itemParentId;
	}
	public void setItemParentId(Long itemParentId) {
		this.itemParentId = itemParentId;
	}
	public String getItemParentSkuCd() {
		return itemParentSkuCd;
	}
	public void setItemParentSkuCd(String itemParentSkuCd) {
		this.itemParentSkuCd = itemParentSkuCd;
	}
	public Long getCustomAttributeGroupId() {
		return customAttributeGroupId;
	}
	public void setCustomAttributeGroupId(Long customAttributeGroupId) {
		this.customAttributeGroupId = customAttributeGroupId;
	}
	public String getCustomAttributeGroupName() {
		return customAttributeGroupName;
	}
	public void setCustomAttributeGroupName(String customAttributeGroupName) {
		this.customAttributeGroupName = customAttributeGroupName;
	}
	public Vector<Category> getCategories() {
		return categories;
	}
	public void setCategories(Vector<Category> categories) {
		this.categories = categories;
	}
	public Vector<Item> getItemsRelated() {
		return itemsRelated;
	}
	public void setItemsRelated(Vector<Item> itemsRelated) {
		this.itemsRelated = itemsRelated;
	}
	public Vector<Item> getItemsUpSell() {
		return itemsUpSell;
	}
	public void setItemsUpSell(Vector<Item> itemsUpSell) {
		this.itemsUpSell = itemsUpSell;
	}
	public Vector<Item> getItemsCrossSell() {
		return itemsCrossSell;
	}
	public void setItemsCrossSell(Vector<Item> itemsCrossSell) {
		this.itemsCrossSell = itemsCrossSell;
	}
	public Vector<com.jada.xml.ie.ItemTierPrice> getItemTierPrices() {
		return itemTierPrices;
	}
	public void setItemTierPrices(Vector<com.jada.xml.ie.ItemTierPrice> itemTierPrices) {
		this.itemTierPrices = itemTierPrices;
	}
	public Vector<ItemAttributeDetail> getItemAttributeDetails() {
		return itemAttributeDetails;
	}
	public void setItemAttributeDetails(
			Vector<ItemAttributeDetail> itemAttributeDetails) {
		this.itemAttributeDetails = itemAttributeDetails;
	}

	public boolean isSimple() {
		return simple;
	}

	public void setSimple(boolean simple) {
		this.simple = simple;
	}

	public Long getSiteCurrencyClassId() {
		return siteCurrencyClassId;
	}

	public void setSiteCurrencyClassId(Long siteCurrencyClassId) {
		this.siteCurrencyClassId = siteCurrencyClassId;
	}

	public Long getSiteProfileClassId() {
		return siteProfileClassId;
	}

	public void setSiteProfileClassId(Long siteProfileClassId) {
		this.siteProfileClassId = siteProfileClassId;
	}

	public boolean isDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(boolean defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	public boolean isDefaultProfile() {
		return defaultProfile;
	}

	public void setDefaultProfile(boolean defaultProfile) {
		this.defaultProfile = defaultProfile;
	}

	public Vector<ItemImage> getItemImages() {
		return itemImages;
	}

	public void setItemImages(Vector<ItemImage> itemImages) {
		this.itemImages = itemImages;
	}
}
