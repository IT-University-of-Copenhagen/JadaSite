package com.jada.xml.ie;

import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import org.apache.struts.util.MessageResources;

import com.jada.admin.AdminBean;
import com.jada.util.Constants;
import com.jada.util.Format;

public class ItemSimple {
	private Long siteProfileClassId;
	private String siteProfileClassName;
	private Long siteCurrencyClassId;
	private String siteCurrencyClassName;
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
	private String itemShortDesc;
	private String itemDesc;
	private String pageTitle;
	private String metaKeywords;
	private String metaDescription;
	private Float itemPrice;
	private Float itemSpecPrice;
	private Date itemSpecPricePublishOn;
	private Date itemSpecPriceExpireOn;
	private Long shippingTypeId;
	private String shippingTypeName;
	private Long productClassId;
	private String productClassName;
	private Long itemParentId;
	private String itemParentSkuCd;
	private ItemSimpleCategory categories[];
	private ItemSimple itemsRelated[];
	private ItemSimple itemsUpSell[];
	private ItemSimple itemsCrossSell[];
	private ItemSimpleItemTierPrice itemTierPrices[];
	private Long customAttributeGroupId;
	private String customAttributeGroupName;
	private ItemSimpleItemAttributeDetail itemAttributeDetails[];
	private String itemImageOverride;
	private ItemSimpleImage itemImages[];
	
	boolean defaultCurrency;
	boolean defaultProfile;

	public ItemSimple() {
		
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

	public Long getSiteProfileClassId() {
		return siteProfileClassId;
	}

	public void setSiteProfileClassId(Long siteProfileClassId) {
		this.siteProfileClassId = siteProfileClassId;
	}

	public String getSiteProfileClassName() {
		return siteProfileClassName;
	}

	public void setSiteProfileClassName(String siteProfileClassName) {
		this.siteProfileClassName = siteProfileClassName;
	}

	public Long getSiteCurrencyClassId() {
		return siteCurrencyClassId;
	}

	public void setSiteCurrencyClassId(Long siteCurrencyClassId) {
		this.siteCurrencyClassId = siteCurrencyClassId;
	}

	public String getSiteCurrencyClassName() {
		return siteCurrencyClassName;
	}

	public void setSiteCurrencyClassName(String siteCurrencyClassName) {
		this.siteCurrencyClassName = siteCurrencyClassName;
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

	public String getItemShortDesc() {
		return itemShortDesc;
	}

	public void setItemShortDesc(String itemShortDesc) {
		this.itemShortDesc = itemShortDesc;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public Float getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Float itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Float getItemSpecPrice() {
		return itemSpecPrice;
	}

	public void setItemSpecPrice(Float itemSpecPrice) {
		this.itemSpecPrice = itemSpecPrice;
	}

	public Date getItemSpecPricePublishOn() {
		return itemSpecPricePublishOn;
	}

	public void setItemSpecPricePublishOn(Date itemSpecPricePublishOn) {
		this.itemSpecPricePublishOn = itemSpecPricePublishOn;
	}

	public Date getItemSpecPriceExpireOn() {
		return itemSpecPriceExpireOn;
	}

	public void setItemSpecPriceExpireOn(Date itemSpecPriceExpireOn) {
		this.itemSpecPriceExpireOn = itemSpecPriceExpireOn;
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

	public ItemSimpleCategory[] getCategories() {
		return categories;
	}

	public void setCategories(ItemSimpleCategory[] categories) {
		this.categories = categories;
	}

	public ItemSimple[] getItemsRelated() {
		return itemsRelated;
	}

	public void setItemsRelated(ItemSimple[] itemsRelated) {
		this.itemsRelated = itemsRelated;
	}

	public ItemSimple[] getItemsUpSell() {
		return itemsUpSell;
	}

	public void setItemsUpSell(ItemSimple[] itemsUpSell) {
		this.itemsUpSell = itemsUpSell;
	}

	public ItemSimple[] getItemsCrossSell() {
		return itemsCrossSell;
	}

	public void setItemsCrossSell(ItemSimple[] itemsCrossSell) {
		this.itemsCrossSell = itemsCrossSell;
	}

	public ItemSimpleItemTierPrice[] getItemTierPrices() {
		return itemTierPrices;
	}

	public void setItemTierPrices(ItemSimpleItemTierPrice[] itemTierPrices) {
		this.itemTierPrices = itemTierPrices;
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

	public ItemSimpleItemAttributeDetail[] getItemAttributeDetails() {
		return itemAttributeDetails;
	}

	public void setItemAttributeDetails(
			ItemSimpleItemAttributeDetail[] itemAttributeDetails) {
		this.itemAttributeDetails = itemAttributeDetails;
	}

	public ItemSimpleImage[] getItemImages() {
		return itemImages;
	}

	public void setItemImages(ItemSimpleImage[] itemImages) {
		this.itemImages = itemImages;
	}

	public String getItemImageOverride() {
		return itemImageOverride;
	}

	public void setItemImageOverride(String itemImageOverride) {
		this.itemImageOverride = itemImageOverride;
	}
	
	public String getMetaKeywords() {
		return metaKeywords;
	}

	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	public String getMetaDescription() {
		return metaDescription;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	public ItemSimple(Item item, Long siteProfileClassId, String siteProfileClassName, Long siteCurrencyClassId, String siteCurrencyClassName) {
		setSiteProfileClassId(siteProfileClassId);
		setSiteProfileClassName(siteProfileClassName);
		setSiteCurrencyClassId(siteCurrencyClassId);
		setSiteCurrencyClassName(siteCurrencyClassName);
		setItemId(item.getItemId());
		setItemNum(item.getItemNum());
		setItemUpcCd(item.getItemUpcCd());
		setItemSkuCd(item.getItemSkuCd());
		setItemTypeCd(item.getItemTypeCd());
		setItemSellable(item.getItemSellable());
		setItemRating(item.getItemRating());
		setItemRatingCount(item.getItemRatingCount());
		setItemQty(item.getItemQty());
		setItemBookedQty(item.getItemBookedQty());
		setItemCost(item.getItemCost());
		setItemHitCounter(item.getItemHitCounter());
		setItemPublishOn(item.getItemPublishOn());
		setItemExpireOn(item.getItemExpireOn());
		setPublished(item.getPublished());
		setRecUpdateBy(item.getRecUpdateBy());
		setRecUpdateDatetime(item.getRecUpdateDatetime());
		setRecCreateBy(item.getRecCreateBy());
		setRecCreateDatetime(item.getRecCreateDatetime());
		
		setItemImageOverride(String.valueOf(Constants.VALUE_NO));
		
		for (ItemLanguage itemLanguage : item.getItemLanguages()) {
			if (itemLanguage.getSiteProfileClassId().equals(siteProfileClassId)) {
				setItemShortDesc(itemLanguage.getItemShortDesc());
				setItemDesc(itemLanguage.getItemDesc());
				setPageTitle(itemLanguage.getPageTitle());
				setMetaKeywords(itemLanguage.getMetaKeywords());
				setMetaDescription(itemLanguage.getMetaDescription());
				setItemImageOverride(itemLanguage.getItemImageOverride());
				
				Vector<ItemSimpleImage> images = new Vector<ItemSimpleImage>();
				for (ItemImage itemImage : itemLanguage.getImages()) {
					ItemSimpleImage itemSimpleImage = new ItemSimpleImage();
					itemSimpleImage.setDefaultImage(itemImage.getDefaultImage());
					itemSimpleImage.setImageValue(itemImage.getImageValue());
					images.add(itemSimpleImage);
				}
				itemImages = new ItemSimpleImage[images.size()];
				images.copyInto(itemImages);
				break;
			}
		}
		
		for (ItemPriceCurrency itemPriceCurrency : item.getItemPriceCurrencies()) {
			if (itemPriceCurrency.getSiteCurrencyClassId().equals(siteCurrencyClassId)) {
				if (itemPriceCurrency.getItemPriceTypeCode() == Constants.ITEM_PRICE_TYPE_CODE_REGULAR) {
					setItemPrice(itemPriceCurrency.getItemPrice());
				}
				if (itemPriceCurrency.getItemPriceTypeCode() == Constants.ITEM_PRICE_TYPE_CODE_SPECIAL) {
					setItemSpecPrice(itemPriceCurrency.getItemPrice());
					setItemSpecPricePublishOn(itemPriceCurrency.getItemPricePublishOn());
					setItemSpecPriceExpireOn(itemPriceCurrency.getItemPriceExpireOn());
				}
			}
		}
		
		setShippingTypeId(item.getShippingTypeId());
		setShippingTypeName(item.getShippingTypeName());
		setProductClassId(item.getProductClassId());
		setProductClassName(item.getProductClassName());
		setItemParentId(item.getItemParentId());
		setItemParentSkuCd(item.getItemParentSkuCd());

		Vector<ItemSimpleCategory> categoryVector = new Vector<ItemSimpleCategory>();
		for (Category category : item.getCategories()) {
			ItemSimpleCategory itemSimpleCategory = new ItemSimpleCategory();
			itemSimpleCategory.setCatId(category.getCatId());
			itemSimpleCategory.setCatShortTitle(category.getCatShortTitle());
			categoryVector.add(itemSimpleCategory);
		}
		ItemSimpleCategory categories[] = new ItemSimpleCategory[categoryVector.size()];
		categoryVector.copyInto(categories);
		this.setCategories(categories);
		
		Vector<ItemSimple> itemsRelatedVector = new Vector<ItemSimple>();
		for (Item itemRelated : item.getItemsRelated()) {
			ItemSimple itemSimpleItem = new ItemSimple();
			itemSimpleItem.setItemId(itemRelated.getItemId());
			itemSimpleItem.setItemSkuCd(itemRelated.getItemSkuCd());
			itemSimpleItem.setPublished(' ');
			itemSimpleItem.setItemSellable(' ');
			itemsRelatedVector.add(itemSimpleItem);
		}
		ItemSimple itemsRelated[] = new ItemSimple[itemsRelatedVector.size()];
		itemsRelatedVector.copyInto(itemsRelated);
		this.setItemsRelated(itemsRelated);
		
		Vector<ItemSimple> itemsUpSellVector = new Vector<ItemSimple>();
		for (Item itemUpSell : item.getItemsUpSell()) {
			ItemSimple itemSimpleItem = new ItemSimple();
			itemSimpleItem.setItemId(itemUpSell.getItemId());
			itemSimpleItem.setItemSkuCd(itemUpSell.getItemSkuCd());
			itemSimpleItem.setPublished(' ');
			itemSimpleItem.setItemSellable(' ');
			itemsUpSellVector.add(itemSimpleItem);
		}
		ItemSimple itemsUpSell[] = new ItemSimple[itemsUpSellVector.size()];
		itemsUpSellVector.copyInto(itemsUpSell);
		this.setItemsUpSell(itemsUpSell);

		Vector<ItemSimple> itemsCrossSellVector = new Vector<ItemSimple>();
		for (Item itemCrossSell : item.getItemsCrossSell()) {
			ItemSimple itemSimpleItem = new ItemSimple();
			itemSimpleItem.setItemId(itemCrossSell.getItemId());
			itemSimpleItem.setItemSkuCd(itemCrossSell.getItemSkuCd());
			itemSimpleItem.setPublished(' ');
			itemSimpleItem.setItemSellable(' ');
			itemsCrossSellVector.add(itemSimpleItem);
		}
		ItemSimple itemsCrossSell[] = new ItemSimple[itemsCrossSellVector.size()];
		itemsCrossSellVector.copyInto(itemsCrossSell);
		this.setItemsCrossSell(itemsCrossSell);

		Vector<ItemSimpleItemTierPrice> itemTierPricesVector = new Vector<ItemSimpleItemTierPrice>();
		for (ItemTierPrice itemTierPrice : item.getItemTierPrices()) {
			ItemSimpleItemTierPrice itemSimpleItemTierPrice = new ItemSimpleItemTierPrice();
			itemSimpleItemTierPrice.setCustClassId(itemTierPrice.getCustClassId());
			itemSimpleItemTierPrice.setCustClassName(itemTierPrice.getCustClassName());
			itemSimpleItemTierPrice.setItemTierQty(itemTierPrice.getItemTierQty());
			Float itemPrice = null;
			for (ItemTierPriceCurrency itemTierPriceCurrency : itemTierPrice.getItemTierPriceCurrencies()) {
				if (itemTierPriceCurrency.getSiteCurrencyClassId().equals(siteCurrencyClassId)) {
					itemPrice = itemTierPriceCurrency.getItemPrice();
					break;
				}
			}
			itemSimpleItemTierPrice.setItemPrice(itemPrice);
			itemSimpleItemTierPrice.setItemTierPricePublishOn(itemTierPrice.getItemTierPricePublishOn());
			itemSimpleItemTierPrice.setItemTierPriceExpireOn(itemTierPrice.getItemTierPriceExpireOn());
			itemTierPricesVector.add(itemSimpleItemTierPrice);
		}
		ItemSimpleItemTierPrice itemTierPrices[] = new ItemSimpleItemTierPrice[itemTierPricesVector.size()];
		itemTierPricesVector.copyInto(itemTierPrices);
		this.setItemTierPrices(itemTierPrices);
		
		setCustomAttributeGroupId(item.getCustomAttributeGroupId());
		setCustomAttributeGroupName(item.getCustomAttributeGroupName());
		
		Vector<ItemSimpleItemAttributeDetail> itemAttributeDetailsVector = new Vector<ItemSimpleItemAttributeDetail>();
		for (ItemAttributeDetail itemAttributeDetail : item.getItemAttributeDetails()) {
			ItemSimpleItemAttributeDetail itemSimpleItemAttribiuteDetail = new ItemSimpleItemAttributeDetail();
			itemSimpleItemAttribiuteDetail.setCustomAttribId(itemAttributeDetail.getCustomAttribId());
			itemSimpleItemAttribiuteDetail.setCustomAttribName(itemAttributeDetail.getCustomAttribName());
			Long customAttribOptionId = null;
			if (itemAttributeDetail.getCustomAttribOptionId() != null) {
				customAttribOptionId = itemAttributeDetail.getCustomAttribOptionId();
			}
			itemSimpleItemAttribiuteDetail.setCustomAttribOptionId(customAttribOptionId);
			itemSimpleItemAttribiuteDetail.setCustomAttribValue(itemAttributeDetail.getCustomAttribValue());
			String itemAttributeDetailValue = null;
			for (ItemAttributeDetailLanguage itemAttribuetDetailLanguage : itemAttributeDetail.getItemAttributeDetailLanguages()) {
				if (itemAttribuetDetailLanguage.getSiteProfileClassId().equals(siteProfileClassId)) {
					itemAttributeDetailValue = itemAttribuetDetailLanguage.getItemAttribDetailValue();
					break;
				}
			}
			itemSimpleItemAttribiuteDetail.setItemAttribDetailValue(itemAttributeDetailValue);
			itemAttributeDetailsVector.add(itemSimpleItemAttribiuteDetail);
		}
		ItemSimpleItemAttributeDetail itemAttributeDetails[] = new ItemSimpleItemAttributeDetail[itemAttributeDetailsVector.size()];
		itemAttributeDetailsVector.copyInto(itemAttributeDetails);
		this.setItemAttributeDetails(itemAttributeDetails);
	}

	public String[] validate() throws IOException {
		MessageResources resources = MessageResources.getMessageResources("application");
		Vector<String> m = new Vector<String>();
		if (siteProfileClassId == null && Format.isNullOrEmpty(siteProfileClassName)) {
			m.add(resources.getMessage("IE.ERROR.E102", "siteProfileClassId", "siteProfileClassName"));
		}
		if (siteCurrencyClassId == null && Format.isNullOrEmpty(siteCurrencyClassName)) {
			m.add(resources.getMessage("IE.ERROR.E102", "siteCurrencyClassId", "siteCurrencyClassName"));
		}
		if (!Format.isNullOrEmpty(itemNum)) {
			if (itemNum.length() > 20) {
				m.add(resources.getMessage("IE.ERROR.E105", "itemNum", 20));
			}
		}
		if (Format.isNullOrEmpty(itemUpcCd)) {
			m.add(resources.getMessage("IE.ERROR.E101", "itemUpcCd"));
		}
		else {
			if (itemUpcCd.length() > 20) {
				m.add(resources.getMessage("IE.ERROR.E105", "itemUpcCd", 20));
			}
		}
		if (Format.isNullOrEmpty(itemSkuCd)) {
			m.add(resources.getMessage("IE.ERROR.E101", "itemSkuCd"));
		}
		else {
			if (itemSkuCd.length() > 40) {
				m.add(resources.getMessage("IE.ERROR.E105", "itemSkuCd", 20));
			}
		}
		if (!Format.isNullOrEmpty(itemTypeCd)) {
			if (!itemTypeCd.equals(Constants.ITEM_TYPE_REGULAR) &&
				!itemTypeCd.equals(Constants.ITEM_TYPE_RECOMMAND_BUNDLE) && 
				!itemTypeCd.equals(Constants.ITEM_TYPE_SKU) &&
				!itemTypeCd.equals(Constants.ITEM_TYPE_STATIC_BUNDLE) &&
				!itemTypeCd.equals(Constants.ITEM_TYPE_TEMPLATE)) {
				m.add(resources.getMessage("IE.ERROR.E110", "itemTypeCd"));
			}
		}
		if (itemSellable != ' ') {
			if (itemSellable != 'Y' && itemSellable != 'N') {
				m.add(resources.getMessage("IE.ERROR.E103", "itemSellable"));
			}
		}
		if (itemCost != null) {
			if (itemCost.floatValue() < 0) {
				m.add(resources.getMessage("IE.ERROR.E104", "itemCost"));
			}
		}
		if (itemHitCounter != null) {
			if (itemHitCounter.intValue() < 0) {
				m.add(resources.getMessage("IE.ERROR.E104", "itemHitCounter"));
			}
		}
		if (itemRating != null) {
			if (itemRating.floatValue() < 0) {
				m.add(resources.getMessage("IE.ERROR.E104", "itemRating"));
			}
		}
		if (itemRatingCount != null) {
			if (itemRatingCount.intValue() < 0) {
				m.add(resources.getMessage("IE.ERROR.E104", "itemRatingCount"));
			}
		}
		if (itemQty != null) {
			if (itemQty.intValue() < 0) {
				m.add(resources.getMessage("IE.ERROR.E104", "itemQty"));
			}
		}
		if (itemBookedQty != null) {
			if (itemBookedQty.intValue() < 0) {
				m.add(resources.getMessage("IE.ERROR.E104", "itemBookedQty"));
			}
		}
		if (published != 'Y' && published != 'N') {
			m.add(resources.getMessage("IE.ERROR.E103", "published"));
		}
		if (this.isDefaultProfile()) {
			if (Format.isNullOrEmpty(itemShortDesc)) {
				m.add(resources.getMessage("IE.ERROR.E101", "itemShortDesc"));
			}
		}
		if (!Format.isNullOrEmpty(itemShortDesc)) {
			if (itemShortDesc.length() > 128) {
				m.add(resources.getMessage("IE.ERROR.E105", "itemShortDesc", 128));
			}
		}
		if (this.isDefaultProfile()) {
			if (Format.isNullOrEmpty(itemDesc)) {
				m.add(resources.getMessage("IE.ERROR.E101", "itemDesc"));
			}
		}
		if (!Format.isNullOrEmpty(itemDesc)) {
			if (itemDesc.length() > 1000) {
				m.add(resources.getMessage("IE.ERROR.E105", "itemDesc", 1000));
			}
		}
		if (!Format.isNullOrEmpty(pageTitle)) {
			if (pageTitle.length() > 255) {
				m.add(resources.getMessage("IE.ERROR.E105", "pageTitle", 255));
			}
		}
		if (!Format.isNullOrEmpty(metaKeywords)) {
			if (metaKeywords.length() > 1000) {
				m.add(resources.getMessage("IE.ERROR.E105", "metaKeywords", 1000));
			}
		}
		if (!Format.isNullOrEmpty(metaDescription)) {
			if (metaDescription.length() > 1000) {
				m.add(resources.getMessage("IE.ERROR.E105", "metaDescription", 1000));
			}
		}
		if (isDefaultCurrency()) {
			if (itemPrice == null) {
				m.add(resources.getMessage("IE.ERROR.E101", "itemPrice"));
			}
		}
		if (itemSpecPrice != null) {
			if (itemSpecPrice.floatValue() < 0) {
				m.add(resources.getMessage("IE.ERROR.E104", "itemSpecPrice"));
			}
			if (itemSpecPricePublishOn == null) {
				m.add(resources.getMessage("IE.ERROR.E106", "itemSpecPricePublishOn"));
			}
			if (itemSpecPriceExpireOn == null) {
				m.add(resources.getMessage("IE.ERROR.E106", "itemSpecPriceExpireOn"));
			}
		}
		if (shippingTypeId == null && Format.isNullOrEmpty(shippingTypeName)) {
			m.add(resources.getMessage("IE.ERROR.E102", "shippingTypeId", "shippingTypeName"));
		}
		if (productClassId == null && productClassName == null) {
			m.add(resources.getMessage("IE.ERROR.E102", "productClassId", "productClassName"));
		}
		
		/*
		for (ItemSimpleCategory category : categories) {
			if (category.getCatId() == null && Format.isNullOrEmpty(category.getCatShortTitle())) {
				m.add(resources.getMessage("IE.ERROR.E102", "catId", "catShortTitle"));
			}
		}
		
		for (ItemSimple itemRelated : itemsRelated) {
			if (itemRelated.getItemId() == null && Format.isNullOrEmpty(itemRelated.getItemSkuCd())) {
				m.add(resources.getMessage("IE.ERROR.E102", "itemId", "itemSkuCd"));
			}
		}
		
		for (ItemSimple itemUpSell : itemsUpSell) {
			if (itemUpSell.getItemId() == null && Format.isNullOrEmpty(itemUpSell.getItemSkuCd())) {
				m.add(resources.getMessage("IE.ERROR.E102", "itemId", "itemSkuCd"));
			}
		}

		for (ItemSimple itemCrossSell : itemsCrossSell) {
			if (itemCrossSell.getItemId() == null && Format.isNullOrEmpty(itemCrossSell.getItemSkuCd())) {
				m.add(resources.getMessage("IE.ERROR.E102", "itemId", "itemSkuCd"));
			}
		}
		*/
		if (itemTierPrices != null) {
			for (ItemSimpleItemTierPrice itemTierPrice : itemTierPrices) {
				if (itemTierPrice.getItemPrice() == null && 
					itemTierPrice.getItemTierQty() == null) {
					continue;
				}
				if (isDefaultCurrency()) {
					if (itemTierPrice.getItemPrice() == null) {
						m.add(resources.getMessage("IE.ERROR.E101", "itemTierPrice.itemPrice"));
					}
				}
				/*
				if (itemTierPrice.getCustClassId() == null && itemTierPrice.getCustClassName() == null) {
					m.add(resources.getMessage("IE.ERROR.E102", "itemTierPrice.custClassId", "itemTierPrice.custClassName"));
				}
				*/
				if (itemTierPrice.getItemTierQty() == null) {
					m.add(resources.getMessage("IE.ERROR.E101", "itemTierPrice.itemTierQty"));
				}
				if (itemTierPrice.getItemTierPricePublishOn() == null) {
					m.add(resources.getMessage("IE.ERROR.E101", "itemTierPrice.itemTierPricePublishOn"));
				}
				if (itemTierPrice.getItemTierPriceExpireOn() == null) {
					m.add(resources.getMessage("IE.ERROR.E101", "itemTierPrice.itemTierPriceExpireOn"));
				}
			}
		}
		if (itemImages != null) {
			for (ItemSimpleImage itemImage : itemImages) {
				if (Format.isNullOrEmpty(itemImage.getItemImageLocation())) {
					continue;
				}
				String defaultImage = itemImage.getDefaultImage();
				if (Format.isNullOrEmpty(defaultImage)) {
					m.add(resources.getMessage("IE.ERROR.E101", "defaultImage"));
				}
				else {
					if (!defaultImage.equals("Y") && !defaultImage.equals("N")) {
						m.add(resources.getMessage("IE.ERROR.E103", "defaultImage"));
					}
				}
			}
		}

		String messages[] = new String[m.size()];
		m.copyInto(messages);
		return messages;
	}
	public void save(AdminBean adminBean) throws Exception {
		Item item = new Item(adminBean.getSiteId(), this);
		boolean isSimple = true;
		item.save(isSimple, adminBean);
	}
}
