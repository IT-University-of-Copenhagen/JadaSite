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

package com.jada.admin.item;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import org.json.JSONException;
import com.jada.util.JSONEscapeObject;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.CacheDAO;
import com.jada.dao.CustomAttributeDAO;
import com.jada.dao.CustomAttributeGroupDAO;
import com.jada.dao.CustomAttributeOptionDAO;
import com.jada.dao.CustomerClassDAO;
import com.jada.dao.ItemDAO;
import com.jada.dao.ItemImageDAO;
import com.jada.dao.ItemTierPriceDAO;
import com.jada.dao.MenuDAO;
import com.jada.dao.CategoryDAO;
import com.jada.dao.SiteCurrencyClassDAO;
import com.jada.dao.SiteDomainDAO;
import com.jada.dao.SiteProfileClassDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Category;
import com.jada.jpa.entity.Comment;
import com.jada.jpa.entity.CustomAttribute;
import com.jada.jpa.entity.CustomAttributeDetail;
import com.jada.jpa.entity.CustomAttributeGroup;
import com.jada.jpa.entity.CustomAttributeOption;
import com.jada.jpa.entity.CustomAttributeOptionCurrency;
import com.jada.jpa.entity.CustomAttributeOptionLanguage;
import com.jada.jpa.entity.CustomerClass;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.ItemAttributeDetail;
import com.jada.jpa.entity.ItemAttributeDetailLanguage;
import com.jada.jpa.entity.ItemImage;
import com.jada.jpa.entity.ItemLanguage;
import com.jada.jpa.entity.ItemPriceCurrency;
import com.jada.jpa.entity.ItemTierPrice;
import com.jada.jpa.entity.ItemTierPriceCurrency;
import com.jada.jpa.entity.Menu;
import com.jada.jpa.entity.ProductClass;
import com.jada.jpa.entity.ShippingType;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteCurrencyClass;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.jpa.entity.User;
import com.jada.inventory.InventoryEngine;
import com.jada.search.Indexer;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.translator.BingTranslate;
import com.jada.util.ImageScaler;
import com.jada.util.CategorySearchUtil;
import com.jada.util.Utility;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

public class ItemMaintAction
    extends AdminLookupDispatchAction {
    Logger logger = Logger.getLogger(ItemMaintAction.class);
	
	static String TABINDEX_STATISTICS = "0";
	static String TABINDEX_PRICING = "1";
	static String TABINDEX_ADJUSTMENT = "2";
	static String TABINDEX_IMAGES = "3";
	static String TABINDEX_MENU = "4";
	static String TABINDEX_SECTION = "5";
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse)
        throws Throwable {

        ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
        if (form == null) {
            form = new ItemMaintActionForm();
        }
        Item item = new Item();
		AdminBean adminBean = getAdminBean(httpServletRequest);
		Site site = adminBean.getSite();
        initSiteProfiles(form, site);
        
        form.setShippingTypeId("");
        form.setItemHitCounter("0");
        form.setItemRating(Format.getFloat(0));
        form.setItemRatingCount("0");
        form.setItemQty("0");
        form.setItemBookedQty("0");
        form.setItemSellable(true);
        form.setPublished(true);
        form.setItemPublishOn(Format.getDate(new Date(System.currentTimeMillis())));
        form.setItemExpireOn(Format.getDate(Format.HIGHDATE));
        form.setItemId("-1");
        form.setShippingTypeId("");
        form.setMode("C");
        createAdditionalInfo(adminBean, form, item);

        FormUtils.setFormDisplayMode(httpServletRequest, form, FormUtils.CREATE_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    
    public ActionForward language(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
	
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		if (form == null) {
			form = new ItemMaintActionForm();
		}
		
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
		Item item = (Item) em.find(Item.class, Format.getLong(form.getItemId()));
		
		copyProperties(request, form, item);
        createAdditionalInfo(getAdminBean(request), form, item);
	
		FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}
    
    public ActionForward translate(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
	
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
		Item item = (Item) em.find(Item.class, Format.getLong(form.getItemId()));
        createAdditionalInfo(getAdminBean(request), form, item);        

        BingTranslate translator = new BingTranslate(form.getFromLocale(), form.getToLocale(), site);
		
		if (!form.isSiteProfileClassDefault()) {
			copyProperties(request, form, item);
		}
		
		try {
			form.setItemShortDescLangFlag(true);
			form.setItemDescLangFlag(true);
			form.setPageTitleLangFlag(true);
			form.setMetaKeywordsLangFlag(true);
			form.setMetaDescriptionLangFlag(true);
			form.setItemShortDescLang(translator.translate(item.getItemLanguage().getItemShortDesc()));
			form.setItemDescLang(translator.translate(item.getItemLanguage().getItemDesc()));
			form.setPageTitleLang(translator.translate(item.getItemLanguage().getPageTitle()));
			form.setMetaKeywordsLang(translator.translate(item.getItemLanguage().getMetaKeywords()));
			form.setMetaDescriptionLang(translator.translate(item.getItemLanguage().getMetaDescription()));
			
			form.setItemAttribDetailValueLangFlag(true);
			ItemAttributeDetailDisplayForm attributeDetailDisplayForm[] = form.getItemAttributeDetails();
			for (int i = 0; i < attributeDetailDisplayForm.length; i++) {
				attributeDetailDisplayForm[i].setItemAttribDetailValueLangFlag(true);
				CustomAttribute customAttribute = CustomAttributeDAO.load(site.getSiteId(), Format.getLong(attributeDetailDisplayForm[i].getCustomAttribId()));
				if (customAttribute.getCustomAttribTypeCode() != Constants.CUSTOM_ATTRIBUTE_TYPE_USER_INPUT) {
					continue;
				}
				attributeDetailDisplayForm[i].setItemAttribDetailValueLang(translator.translate(attributeDetailDisplayForm[i].getItemAttribDetailValue()));
			}
		}
		catch (Exception e) {
			logger.error(e);
	    	ActionMessages errors = new ActionMessages();
	    	errors.add("error", new ActionMessage("error.google.translate"));
	    	saveMessages(request, errors);
			return actionMapping.findForward("error");
		}
		
        createAdditionalInfo(getAdminBean(request), form, item);
		FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}

    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

        ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
        if (form == null) {
            form = new ItemMaintActionForm();
        }
		AdminBean adminBean = getAdminBean(request);
		initSiteProfiles(form, adminBean.getSite());
		String id = request.getParameter("itemId");
		Long itemId = null;
		if (id != null) {
			itemId = Format.getLong(id);
		}
		else {
			itemId = Format.getLong(form.getItemId());
		}

        Item item = new Item();
        item = ItemDAO.load(adminBean.getSite().getSiteId(), itemId);
        copyProperties(request, form, item);
        form.setMode("U");
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        
		createAdditionalInfo(adminBean, form, item);

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward cancel(ActionMapping actionMapping,
          	ActionForm actionForm,
          	HttpServletRequest httpServletRequest,
          	HttpServletResponse httpServletResponse) {
    	ActionForward actionForward = actionMapping.findForward("cancel");
		return actionForward;
	}
    
	public ActionForward save(ActionMapping mapping, 
							  ActionForm actionForm,
							  HttpServletRequest request,
							  HttpServletResponse response) 
		throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		MessageResources resources = this.getResources(request);
		boolean insertMode = false;
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}
		form.setItemShortDesc(form.getItemShortDesc());
		form.setItemShortDescLang(form.getItemShortDescLang());

		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);

		Item item = new Item();
		if (!insertMode) {
			item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
		}

		ActionMessages errors = validate(request, form, site.getSiteId(), insertMode, item);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			createAdditionalInfo(adminBean, form, item);
			return mapping.findForward("error");
		}

		if (insertMode) {
			item.setSite(site);
			item.setItemTypeCd(form.getItemTypeCd());
			item.setItemHitCounter(new Integer(0));
			item.setItemRating(new Float(0));
			item.setItemRatingCount(new Integer(0));
			item.setItemQty(new Integer(0));
			item.setItemBookedQty(new Integer(0));
			item.setRecCreateBy(adminBean.getUser().getUserId());
			item.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			
			if (!Format.isNullOrEmpty(form.getCustomAttribGroupId())) {
				Long customAttribGroupId = Format.getLong(form.getCustomAttribGroupId());
				CustomAttributeGroup customAttributeGroup = CustomAttributeGroupDAO.load(site.getSiteId(), customAttribGroupId);
				item.setCustomAttributeGroup(customAttributeGroup);
				form.setCustomAttribGroupName(customAttributeGroup.getCustomAttribGroupName());

				String sql = "select   customAttributeDetail " +
				      		 "from     CustomAttributeGroup customAttributeGroup " +
				      		 "join 	customAttributeGroup.customAttributeDetails customAttributeDetail  " +
				      		 "where    customAttributeGroup.customAttribGroupId = :customAttribGroupId " +
				      		 "order    by customAttributeDetail.seqNum ";
				Query query = em.createQuery(sql);
				query.setParameter("customAttribGroupId", Format.getLong(form.getCustomAttribGroupId()));
				Iterator<?> iterator = query.getResultList().iterator();
				Vector<ItemAttributeDetailDisplayForm> itemAttributeDetails = new Vector<ItemAttributeDetailDisplayForm>();
				boolean itemAttributeLangFlag = false;
				while (iterator.hasNext()) {
					CustomAttributeDetail customAttributeDetail = (CustomAttributeDetail) iterator.next();
/*
					ItemAttributeDetail itemAttributeDetail = new ItemAttributeDetail();
					itemAttributeDetail.setItem(item);
					itemAttributeDetail.setCustomAttributeDetail(customAttributeDetail);
					itemAttributeDetail.setRecUpdateBy(adminBean.getUser().getUserId());
					itemAttributeDetail.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
					itemAttributeDetail.setRecCreateBy(adminBean.getUser().getUserId());
					itemAttributeDetail.setRecCreateDatetime(new Date(System.currentTimeMillis()));
					
					ItemAttributeDetailLanguage itemAttributeDetailLanguage = new ItemAttributeDetailLanguage();
					itemAttributeDetailLanguage.setItemAttribDetailValue("");
					itemAttributeDetailLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
					itemAttributeDetailLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
					itemAttributeDetailLanguage.setRecCreateBy(adminBean.getUser().getUserId());
					itemAttributeDetailLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
					SiteProfileClass siteProfileClass = SiteProfileClassDAO.load(form.getSiteProfileClassDefaultId());
					itemAttributeDetailLanguage.setSiteProfileClass(siteProfileClass);
					if (customAttributeDetail.getCustomAttribute().getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_USER_SELECT_DROPDOWN) {
						CustomAttributeOption customAttributeOption = null;
						for (CustomAttributeOption option : customAttributeDetail.getCustomAttribute().getCustomAttributeOptions()) {
							customAttributeOption = option;
							break;
						}
						itemAttributeDetail.setCustomAttributeOption(customAttributeOption);
					}
					em.persist(itemAttributeDetailLanguage);
					
					itemAttributeDetail.setItemAttributeDetailLanguage(itemAttributeDetailLanguage);
					em.persist(itemAttributeDetail);
*/
					ItemAttributeDetailDisplayForm displayForm = new ItemAttributeDetailDisplayForm();
					displayForm.setCustomAttribId(customAttributeDetail.getCustomAttribute().getCustomAttribId().toString());
					displayForm.setCustomAttribTypeCode(String.valueOf(customAttributeDetail.getCustomAttribute().getCustomAttribTypeCode()));
					displayForm.setCustomAttribDetailId(customAttributeDetail.getCustomAttribDetailId().toString());
					displayForm.setCustomAttribName(customAttributeDetail.getCustomAttribute().getCustomAttribName());
					displayForm.setCustomAttribTypeCode(String.valueOf(customAttributeDetail.getCustomAttribute().getCustomAttribTypeCode()));
					displayForm.setItemAttribDetailValue("");
					itemAttributeDetails.add(displayForm);
				}
				form.setItemAttribDetailValueLangFlag(itemAttributeLangFlag);
				ItemAttributeDetailDisplayForm[] itemAttributeDetailDisplayForms = new ItemAttributeDetailDisplayForm[itemAttributeDetails.size()];
				itemAttributeDetails.copyInto(itemAttributeDetailDisplayForms);
				form.setItemAttributeDetails(itemAttributeDetailDisplayForms);
			}
			form.setItemTypeDesc(resources.getMessage("item.typeCode." + form.getItemTypeCd()));
		}
		
		if (form.isSiteProfileClassDefault() && form.isSiteCurrencyClassDefault()) {
			saveDefault(item, form, adminBean, insertMode);
			form.setRecUpdateBy(item.getRecUpdateBy());
			form.setRecUpdateDatetime(Format.getFullDatetime(item.getRecUpdateDatetime()));
			User user = adminBean.getUser();
			item.setUser(user);
			if (insertMode) {
				em.persist(item);
				form.setItemId(Format.getLong(item.getItemId()));
			}
			else {
				// em.update(item);
			}
		}
		else {
			if (!form.isSiteProfileClassDefault()) {
				saveLanguage(item, form, adminBean);
			}
			if (!form.isSiteCurrencyClassDefault()) {
				saveCurrency(item, form, adminBean);
			}			
		}
		
		if (!Format.isNullOrEmpty(form.getShippingTypeId())) {
			ShippingType shippingType = (ShippingType) em.find(ShippingType.class, Format.getLong(form.getShippingTypeId()));
			if (shippingType != null) {
				item.setShippingType(shippingType);
			}
		}
		else {
			item.setShippingType(null);
		}
		
		if (!Format.isNullOrEmpty(form.getProductClassId())) {
			ProductClass productClass = (ProductClass) em.find(ProductClass.class, Format.getLong(form.getProductClassId()));
			if (productClass != null) {
				item.setProductClass(productClass);
			}
		}
		else {
			item.setShippingType(null);
		}
		
		CategorySearchUtil.itemPriceSearchUpdate(item, site, adminBean);
		CategorySearchUtil.itemDescSearchUpdate(item, site, adminBean);
		if (insertMode) {
			em.persist(item);
			form.setItemId(Format.getLong(item.getItemId()));
		}
		else {
			// em.update(item);
		}
		
		Iterator<?> iterator = item.getMenus().iterator();
		while (iterator.hasNext()) {
			Menu menu = (Menu) iterator.next();
			CacheDAO.removeByKeyPrefix(site.getSiteId(), Constants.CACHE_MENU + "." + menu.getMenuSetName());
		}
		
		Indexer.getInstance(site.getSiteId()).updateItem(item);

		createAdditionalInfo(adminBean, form, item);

        form.setMode("U");
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	public void saveLanguage(Item item, ItemMaintActionForm form, AdminBean adminBean) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Long siteProfileClassId = form.getSiteProfileClassId();
		SiteProfileClass siteProfileClass = (SiteProfileClass) em.find(SiteProfileClass.class, siteProfileClassId);
    	User user = adminBean.getUser();
    	Iterator<?> iterator = item.getItemLanguages().iterator();
    	boolean found = false;
    	ItemLanguage itemLanguage = null;
    	while (iterator.hasNext()) {
    		itemLanguage = (ItemLanguage) iterator.next();
    		if (itemLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
    			found = true;
    			break;
    		}
    	}
    	if (!found) {
    		itemLanguage = new ItemLanguage();
    		itemLanguage.setRecCreateBy(user.getUserId());
    		itemLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		itemLanguage.setItemImageOverride(String.valueOf(Constants.VALUE_NO));
    		itemLanguage.setSiteProfileClass(siteProfileClass);
    		itemLanguage.setItem(item);
    	}
    	if (form.isItemShortDescLangFlag()) {
    		itemLanguage.setItemShortDesc(form.getItemShortDescLang());
    	}
    	else {
    		itemLanguage.setItemShortDesc(null);
    	}
    	if (form.isItemDescLangFlag()) {
    		itemLanguage.setItemDesc(form.getItemDescLang());
    	}
    	else {
    		itemLanguage.setItemDesc(null);
    	}
    	if (form.isPageTitleLangFlag()) {
    		itemLanguage.setPageTitle(form.getPageTitleLang());
    	}
    	else {
    		itemLanguage.setPageTitle(null);
    	}
    	if (form.isMetaKeywordsLangFlag()) {
    		itemLanguage.setMetaKeywords(form.getMetaKeywordsLang());
    	}
    	else {
    		itemLanguage.setMetaKeywords(null);
    	}
    	if (form.isMetaDescriptionLangFlag()) {
    		itemLanguage.setMetaDescription(form.getMetaDescriptionLang());
    	}
    	else {
    		itemLanguage.setMetaDescription(null);
    	}
    	
    	itemLanguage.setRecUpdateBy(user.getUserId());
    	itemLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    	em.persist(itemLanguage);

		Iterator<?> attributeDetailIterator = item.getItemAttributeDetails().iterator();
		while (attributeDetailIterator.hasNext()) {
			ItemAttributeDetail itemAttributeDetail = (ItemAttributeDetail) attributeDetailIterator.next();
			Iterator<?> attributeDetailLangIterator = itemAttributeDetail.getItemAttributeDetailLanguages().iterator();
			while (attributeDetailLangIterator.hasNext()) {
				ItemAttributeDetailLanguage itemAttributeDetailLanguage = (ItemAttributeDetailLanguage) attributeDetailLangIterator.next();
				if (itemAttributeDetailLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
					em.remove(itemAttributeDetailLanguage);
				}
			}
		}
		if (form.isItemAttribDetailValueLangFlag()) {
			ItemAttributeDetailDisplayForm itemAttributeDetails[] = form.getItemAttributeDetails();
			for (int i = 0; i < itemAttributeDetails.length; i++) {
				ItemAttributeDetailDisplayForm displayForm = itemAttributeDetails[i];
				CustomAttribute customAttribute = CustomAttributeDAO.load(adminBean.getSite().getSiteId(), Format.getLong(itemAttributeDetails[i].getCustomAttribId()));
				if (customAttribute.getCustomAttribTypeCode() != Constants.CUSTOM_ATTRIBUTE_TYPE_USER_INPUT) {
					continue;
				}
				ItemAttributeDetail itemAttributeDetail = null;
				if (Format.isNullOrEmpty(displayForm.getItemAttribDetailId())) {
					itemAttributeDetail = new ItemAttributeDetail();
					itemAttributeDetail.setRecUpdateBy(user.getUserId());
					itemAttributeDetail.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
					itemAttributeDetail.setRecCreateBy(user.getUserId());
					itemAttributeDetail.setRecCreateDatetime(new Date(System.currentTimeMillis()));
					em.persist(itemAttributeDetail);
				}
				else {
					itemAttributeDetail = (ItemAttributeDetail) em.find(ItemAttributeDetail.class, Format.getLong(displayForm.getItemAttribDetailId()));
				}
				ItemAttributeDetailLanguage itemAttributeDetailLanguage = new ItemAttributeDetailLanguage();
				itemAttributeDetailLanguage.setItemAttribDetailValue(displayForm.getItemAttribDetailValueLang());
				itemAttributeDetailLanguage.setRecUpdateBy(user.getUserId());
				itemAttributeDetailLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
				itemAttributeDetailLanguage.setRecCreateBy(user.getUserId());
				itemAttributeDetailLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
				itemAttributeDetailLanguage.setItemAttributeDetail(itemAttributeDetail);
				itemAttributeDetailLanguage.setSiteProfileClass(siteProfileClass);
				em.persist(itemAttributeDetailLanguage);
			}
		}
	}
	
	public void saveCurrency(Item item, ItemMaintActionForm form, AdminBean adminBean) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Long siteCurrencyClassId = form.getSiteCurrencyClassId();
    	SiteCurrencyClass siteCurrencyClass = (SiteCurrencyClass) em.find(SiteCurrencyClass.class, siteCurrencyClassId);
    	User user = adminBean.getUser();
    	ItemPriceCurrency itemPriceCurrency = null;
    	for (ItemPriceCurrency priceCurrency : item.getItemPriceCurrencies()) {
    		if (priceCurrency.getItemPriceTypeCode() != Constants.ITEM_PRICE_TYPE_CODE_REGULAR) {
    			continue;
    		}
    		if (priceCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteCurrencyClassId)) {
    			itemPriceCurrency = priceCurrency;
    			break;
    		}
    	}
    	if (form.isItemPriceCurrFlag()) {
	    	if (itemPriceCurrency == null) {
	    		itemPriceCurrency = new ItemPriceCurrency();
	    		itemPriceCurrency.setItemPriceTypeCode(Constants.ITEM_PRICE_TYPE_CODE_REGULAR);
	    		itemPriceCurrency.setItemPricePublishOn(Format.LOWDATE);
	    		itemPriceCurrency.setItemPriceExpireOn(Format.HIGHDATE);
	    		itemPriceCurrency.setRecCreateBy(user.getUserId());
	    		itemPriceCurrency.setRecCreateDatetime(new Date(System.currentTimeMillis()));
	    		itemPriceCurrency.setSiteCurrencyClass(siteCurrencyClass);
	    		itemPriceCurrency.setItem(item);
	    		if (form.isSiteCurrencyClassDefault()) {
	    			item.setItemPrice(itemPriceCurrency);
	    		}
	    	}
    		itemPriceCurrency.setItemPrice(Format.getFloat(form.getItemPriceCurr()));
    		itemPriceCurrency.setRecUpdateBy(user.getUserId());
    		itemPriceCurrency.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    		em.persist(itemPriceCurrency);
    	}
    	else {
    		if (itemPriceCurrency != null) {
    			item.getItemPriceCurrencies().remove(itemPriceCurrency);
    			em.remove(itemPriceCurrency);
    		}
    	}
    	
    	itemPriceCurrency = null;
    	for (ItemPriceCurrency priceCurrency : item.getItemPriceCurrencies()) {
    		if (priceCurrency.getItemPriceTypeCode() != Constants.ITEM_PRICE_TYPE_CODE_SPECIAL) {
    			continue;
    		}
    		if (priceCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteCurrencyClassId)) {
    			itemPriceCurrency = priceCurrency;
    			break;
    		}
    	}
    	if (form.isItemSpecPriceCurrFlag()) {
	    	if (itemPriceCurrency == null) {
	    		itemPriceCurrency = new ItemPriceCurrency();
	    		itemPriceCurrency.setItemPriceTypeCode(Constants.ITEM_PRICE_TYPE_CODE_SPECIAL);
	    		itemPriceCurrency.setItemPricePublishOn(Format.getDate(form.getItemSpecPublishOn()));
	    		itemPriceCurrency.setItemPriceExpireOn(Format.getDate(form.getItemSpecExpireOn()));
	    		itemPriceCurrency.setRecCreateBy(user.getUserId());
	    		itemPriceCurrency.setRecCreateDatetime(new Date(System.currentTimeMillis()));
	    		itemPriceCurrency.setSiteCurrencyClass(siteCurrencyClass);
	    		itemPriceCurrency.setItem(item);
	    		if (form.isSiteCurrencyClassDefault()) {
	    			item.setItemSpecPrice(itemPriceCurrency);
	    		}
	    	}
    		itemPriceCurrency.setItemPrice(Format.getFloat(form.getItemSpecPriceCurr()));
    		itemPriceCurrency.setRecUpdateBy(user.getUserId());
    		itemPriceCurrency.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    		em.persist(itemPriceCurrency);
    	}
    	else {
    		if (itemPriceCurrency != null) {
    			item.getItemPriceCurrencies().remove(itemPriceCurrency);
    			em.remove(itemPriceCurrency);
    		}
    	}
	}
	
	public void saveDefault(Item item, ItemMaintActionForm form, AdminBean adminBean, boolean insertMode) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	User user = adminBean.getUser();
    	Site site = adminBean.getSite();
    	Long siteProfileClassId = form.getSiteProfileClassId();
		SiteProfileClass siteProfileClass = SiteProfileClassDAO.load(siteProfileClassId);
    	ItemLanguage itemLanguage = item.getItemLanguage();
    	
    	boolean exist = true;
    	if (itemLanguage == null) {
    		itemLanguage = new ItemLanguage();
    		exist = false;
    		itemLanguage.setItemImageOverride(String.valueOf(Constants.VALUE_NO));
    		itemLanguage.setRecCreateBy(adminBean.getUser().getUserId());
    		itemLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		itemLanguage.setSiteProfileClass(siteProfileClass);
    		item.getItemLanguages().add(itemLanguage);
    		item.setItemLanguage(itemLanguage);
    	}
    	itemLanguage.setItemShortDesc(form.getItemShortDesc());
    	itemLanguage.setItemDesc(form.getItemDesc());
    	itemLanguage.setPageTitle(form.getPageTitle());
    	itemLanguage.setMetaKeywords(form.getMetaKeywords());
    	itemLanguage.setMetaDescription(form.getMetaDescription());
		itemLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
		itemLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		if (!exist) {
			em.persist(itemLanguage);
		}
    	
		item.setItemNaturalKey(Utility.encode(form.getItemSkuCd()));
		item.setItemNum(form.getItemNum());
		item.setItemUpcCd(form.getItemUpcCd());
		item.setItemSkuCd(form.getItemSkuCd());
		item.setSeqNum(new Integer(0));
		item.setItemCost(Format.getFloatObj(form.getItemCost()));
		
    	Long siteCurrencyClassId = form.getSiteCurrencyClassId();
		SiteCurrencyClass siteCurrencyClass = SiteCurrencyClassDAO.load(siteCurrencyClassId);
    	ItemPriceCurrency regularPriceCurrency = null;
    	ItemPriceCurrency specialPriceCurrency = null;
    	for (ItemPriceCurrency priceCurrency : item.getItemPriceCurrencies()) {
    		if (!priceCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteCurrencyClassId)) {
    			continue;
    		}
    		if (priceCurrency.getItemPriceTypeCode() == Constants.ITEM_PRICE_TYPE_CODE_REGULAR) {
    			regularPriceCurrency = priceCurrency;
    		}
    		if (priceCurrency.getItemPriceTypeCode() == Constants.ITEM_PRICE_TYPE_CODE_SPECIAL) {
    			specialPriceCurrency = priceCurrency;
    		}
    	}
    	if (regularPriceCurrency == null) {
    		regularPriceCurrency = new ItemPriceCurrency();
    		regularPriceCurrency.setItemPriceTypeCode(Constants.ITEM_PRICE_TYPE_CODE_REGULAR);
    		regularPriceCurrency.setItemPricePublishOn(Format.LOWDATE);
    		regularPriceCurrency.setItemPriceExpireOn(Format.HIGHDATE);
    		regularPriceCurrency.setRecCreateBy(user.getUserId());
    		regularPriceCurrency.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		regularPriceCurrency.setSiteCurrencyClass(siteCurrencyClass);
    		regularPriceCurrency.setItem(item);
    		item.setItemPrice(regularPriceCurrency);
    	}
    	regularPriceCurrency.setItemPrice(Format.getFloat(form.getItemPrice()));
    	regularPriceCurrency.setRecUpdateBy(user.getUserId());
		regularPriceCurrency.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(regularPriceCurrency);
		
    	if (!Format.isNullOrEmpty(form.getItemSpecPrice())) {
	    	if (specialPriceCurrency == null) {
	    		specialPriceCurrency = new ItemPriceCurrency();
	    		specialPriceCurrency.setItemPriceTypeCode(Constants.ITEM_PRICE_TYPE_CODE_SPECIAL);
	    		specialPriceCurrency.setRecCreateBy(user.getUserId());
	    		specialPriceCurrency.setRecCreateDatetime(new Date(System.currentTimeMillis()));
	    		specialPriceCurrency.setSiteCurrencyClass(siteCurrencyClass);
	    		specialPriceCurrency.setItem(item);
	    		item.setItemSpecPrice(specialPriceCurrency);
	    	}
	    	specialPriceCurrency.setItemPrice(Format.getFloat(form.getItemSpecPrice()));
    		specialPriceCurrency.setItemPricePublishOn(Format.getDate(form.getItemSpecPublishOn()));
    		specialPriceCurrency.setItemPriceExpireOn(Format.getDate(form.getItemSpecExpireOn()));
	    	specialPriceCurrency.setRecUpdateBy(user.getUserId());
	    	specialPriceCurrency.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    		em.persist(specialPriceCurrency);
    	}
    	else {
    		if (specialPriceCurrency != null) {
    			item.setItemSpecPrice(null);
    			for (ItemPriceCurrency itemPriceCurrency : item.getItemPriceCurrencies()) {
    				if (itemPriceCurrency.getItemPriceTypeCode() != Constants.ITEM_PRICE_TYPE_CODE_SPECIAL) {
    					continue;
    				}
    				em.remove(itemPriceCurrency);
    			}
    		}
    	}
		item.setItemPublishOn(Format.getDate(form.getItemPublishOn()));
		item.setItemExpireOn(Format.getDate(form.getItemExpireOn()));
		item.setItemSellable(form.isItemSellable() ? 'Y' : 'N');
		item.setPublished(form.isPublished() ? 'Y' : 'N');
		item.setRecUpdateBy(adminBean.getUser().getUserId());
		item.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		
		ItemTierPriceDisplayForm prices[] = form.getItemTierPrices();
		for (int i = 0; i < prices.length; i++) {
			ItemTierPriceDisplayForm price = prices[i];
			Iterator<?> iterator = item.getItemTierPrices().iterator();
			boolean found = false;
			ItemTierPrice itemTierPrice = null;
			ItemTierPriceCurrency itemTierPriceCurrency = null;
			if (!Format.isNullOrEmpty(price.getItemTierPriceId())) {
				while (iterator.hasNext()) {
					itemTierPrice = (ItemTierPrice) iterator.next();
					if (itemTierPrice.getItemTierPriceId().equals(Format.getLong(price.getItemTierPriceId()))) {
						found = true;
						itemTierPriceCurrency = itemTierPrice.getItemTierPriceCurrency();
						break;
					}
				}
			}
			if (!found) {
				itemTierPrice = new ItemTierPrice();
				itemTierPrice.setRecCreateBy(adminBean.getUser().getUserId());
				itemTierPrice.setRecCreateDatetime(new Date(System.currentTimeMillis()));
				item.getItemTierPrices().add(itemTierPrice);
				
				itemTierPriceCurrency = new ItemTierPriceCurrency();
				itemTierPriceCurrency.setRecCreateBy(adminBean.getUser().getUserId());
				itemTierPriceCurrency.setRecCreateDatetime(new Date(System.currentTimeMillis()));
				itemTierPrice.setItemTierPriceCurrency(itemTierPriceCurrency);
				itemTierPrice.getItemTierPriceCurrencies().add(itemTierPriceCurrency);
				itemTierPriceCurrency.setSiteCurrencyClass(siteCurrencyClass);
			}
			itemTierPriceCurrency.setItemPrice(Format.getFloatObj(price.getItemTierPrice()));
			itemTierPriceCurrency.setRecUpdateBy(adminBean.getUser().getUserId());
			itemTierPriceCurrency.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			em.persist(itemTierPriceCurrency);

			itemTierPrice.setItemTierQty(Format.getIntObj(price.getItemTierQty()));
			itemTierPrice.setRecUpdateBy(adminBean.getUser().getUserId());
			itemTierPrice.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			if (Format.isNullOrEmpty(price.getItemTierPricePublishOn())) {
				itemTierPrice.setItemTierPricePublishOn(Format.LOWDATE);
			}
			else {
				itemTierPrice.setItemTierPricePublishOn(Format.getDate(price.getItemTierPricePublishOn()));
			}
			if (Format.isNullOrEmpty(price.getItemTierPriceExpireOn())) {
				itemTierPrice.setItemTierPricePublishOn(Format.HIGHDATE);
			}
			else {
				itemTierPrice.setItemTierPricePublishOn(Format.getDate(price.getItemTierPriceExpireOn()));
			}
		}
		
		if (!Format.isNullOrEmpty(form.getCustomAttribGroupId())) {
			Long customAttribGroupId = Format.getLong(form.getCustomAttribGroupId());
			CustomAttributeGroup customAttributeGroup = CustomAttributeGroupDAO.load(adminBean.getSite().getSiteId(), customAttribGroupId);
			item.setCustomAttributeGroup(customAttributeGroup);
		}
		
		for (ItemAttributeDetail itemAttributeDetail : item.getItemAttributeDetails()) {
			for (ItemAttributeDetailLanguage itemAttributeDetailLanguage : itemAttributeDetail.getItemAttributeDetailLanguages()) {
				em.remove(itemAttributeDetailLanguage);
			}
			em.remove(itemAttributeDetail);
		}
		item.getItemAttributeDetails().clear();
		ItemAttributeDetailDisplayForm itemAttributeDetails[] = form.getItemAttributeDetails();
		for (int i = 0; i < itemAttributeDetails.length; i++) {
			CustomAttribute customAttribute = CustomAttributeDAO.load(site.getSiteId(), Format.getLong(itemAttributeDetails[i].getCustomAttribId()));
			ItemAttributeDetail itemAttributeDetail = new ItemAttributeDetail();
			Long customAttribDetailId = Format.getLong(itemAttributeDetails[i].getCustomAttribDetailId());
			CustomAttributeDetail customAttributeDetail = (CustomAttributeDetail) em.find(CustomAttributeDetail.class, customAttribDetailId);
			itemAttributeDetail.setCustomAttributeDetail(customAttributeDetail);
			itemAttributeDetail.setRecUpdateBy(adminBean.getUser().getUserId());
			itemAttributeDetail.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			itemAttributeDetail.setRecCreateBy(adminBean.getUser().getUserId());
			itemAttributeDetail.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			itemAttributeDetail.setItem(item);
			
			switch (customAttribute.getCustomAttribTypeCode()) {
			case Constants.CUSTOM_ATTRIBUTE_TYPE_USER_INPUT:
				ItemAttributeDetailLanguage itemAttributeDetailLanguage = new ItemAttributeDetailLanguage();
				itemAttributeDetailLanguage.setItemAttribDetailValue(itemAttributeDetails[i].getItemAttribDetailValue());
				itemAttributeDetailLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
				itemAttributeDetailLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
				itemAttributeDetailLanguage.setRecCreateBy(adminBean.getUser().getUserId());
				itemAttributeDetailLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
				itemAttributeDetail.setItemAttributeDetailLanguage(itemAttributeDetailLanguage);
				itemAttributeDetail.getItemAttributeDetailLanguages().add(itemAttributeDetailLanguage);
				itemAttributeDetailLanguage.setSiteProfileClass(siteProfileClass);
				em.persist(itemAttributeDetailLanguage);
				break;
			case Constants.CUSTOM_ATTRIBUTE_TYPE_USER_SELECT_DROPDOWN:
				if (!insertMode) {
					CustomAttributeOption customAttributeOption = null;
					if (itemAttributeDetails[i].getCustomAttribOptionId() != null) {
						customAttributeOption = CustomAttributeOptionDAO.load(site.getSiteId(), Format.getLong(itemAttributeDetails[i].getCustomAttribOptionId()));
					}
					itemAttributeDetail.setCustomAttributeOption(customAttributeOption);
				}
				else {
					CustomAttributeOption customAttributeOption = null;
					for (CustomAttributeOption option : customAttribute.getCustomAttributeOptions()) {
						customAttributeOption = option;
					}
					if (customAttributeOption != null) {
						itemAttributeDetail.setCustomAttributeOption(customAttributeOption);
					}
				}
				break;
			case Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP:
				if (!insertMode && !item.getItemTypeCd().equals(Constants.ITEM_TYPE_TEMPLATE)) {
					CustomAttributeOption customAttributeOption = CustomAttributeOptionDAO.load(site.getSiteId(), Format.getLong(itemAttributeDetails[i].getCustomAttribOptionId()));
					itemAttributeDetail.setCustomAttributeOption(customAttributeOption);
				}
				break;
			}
			item.getItemAttributeDetails().add(itemAttributeDetail);
			em.persist(itemAttributeDetail);
		}
		
		item.getSiteDomains().clear();
		for (ItemSiteDomainDisplayForm itemSiteDomainDisplayForm : form.getItemSiteDomains()) {
			if (itemSiteDomainDisplayForm.getSelected() == null) {
				continue;
			}
			if (itemSiteDomainDisplayForm.getSelected().equals(String.valueOf(Constants.VALUE_YES))) {
				SiteDomain siteDomain = SiteDomainDAO.load(Format.getLong(itemSiteDomainDisplayForm.getSiteDomainId()));
				item.getSiteDomains().add(siteDomain);
			}
		}
	}
	
	private Vector<?> getJSONCustomerClassesSegment(Site site) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String sql = "select   customerClass " +
				 	 "from     CustomerClass customerClass " +
				 	 "left     join customerClass.site site " +
				 	 "where    site.siteId = :siteId " +
				 	 "order    by customerClass.custClassName ";	 
		Query query = em.createQuery(sql);
		query.setParameter("siteId", site.getSiteId());
		Iterator<?> iterator = query.getResultList().iterator();
		Vector<JSONEscapeObject> custClasses = new Vector<JSONEscapeObject>();
		JSONEscapeObject object = new JSONEscapeObject();
		object.put("custClassId", "");
		object.put("custClassName", "All");
		custClasses.add(object);
		while (iterator.hasNext()) {
			CustomerClass customerClass = (CustomerClass) iterator.next();
			object = new JSONEscapeObject();
			object.put("custClassId", customerClass.getCustClassId());
			object.put("custClassName", customerClass.getCustClassName());
			custClasses.add(object);
		}
		return custClasses;
	}
	
	private String getJSONItemTierPrice(ItemTierPrice itemTierPrice, ItemMaintActionForm form) throws Exception {
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("itemTierPriceId", itemTierPrice.getItemTierPriceId());
		jsonResult.put("customerClasses", getJSONCustomerClassesSegment(itemTierPrice.getItem().getSite()));
		if (itemTierPrice.getCustomerClass() != null) {
			jsonResult.put("custClassId", itemTierPrice.getCustomerClass().getCustClassId());
		}
		else {
			jsonResult.put("custClassId", "");
		}
		jsonResult.put("itemTierQty", itemTierPrice.getItemTierQty());
		jsonResult.put("itemTierPrice", Format.getFloat(itemTierPrice.getItemTierPriceCurrency().getItemPrice()));
		if (itemTierPrice.getItemTierPricePublishOn() != null) {
			jsonResult.put("itemTierPricePublishOn", Format.getDate(itemTierPrice.getItemTierPricePublishOn()));
		}
		else {
			jsonResult.put("itemTierPricePublishOn", "");
		}
		if (itemTierPrice.getItemTierPriceExpireOn() != null) {
			jsonResult.put("itemTierPriceExpireOn", Format.getDate(itemTierPrice.getItemTierPriceExpireOn()));
		}
		else {
			jsonResult.put("itemTierPriceExpireOn", "");
		}
		if (!form.isSiteCurrencyClassDefault()) {
			Long siteCurrencyClassId = form.getSiteCurrencyClassId();
			ItemTierPriceCurrency itemTierPriceCurrency = null;
			for (ItemTierPriceCurrency priceCurrency : itemTierPrice.getItemTierPriceCurrencies()) {
				if (priceCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteCurrencyClassId)) {
					itemTierPriceCurrency = priceCurrency;
					break;
				}
			}
			if (itemTierPriceCurrency != null) {
				if (itemTierPriceCurrency.getItemPrice() == null) {
					jsonResult.put("itemTierPriceCurrFlag", false);
				}
				else {
					jsonResult.put("itemTierPriceCurrFlag", true);
					jsonResult.put("itemTierPriceCurr", itemTierPriceCurrency.getItemPrice());
				}
			}
		}
		return jsonResult.toHtmlString();
	}
	
	public ActionForward getItemTierPrice(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
        initSiteProfiles(form, site);
	
		ItemTierPrice itemTierPrice = ItemTierPriceDAO.load(site.getSiteId(), Format.getLong(form.getItemTierPriceId()));
		streamWebService(response, getJSONItemTierPrice(itemTierPrice, form));
        return null;

	}
	
	public ActionForward addTierPrice(ActionMapping mapping, 
									  ActionForm actionForm,
									  HttpServletRequest request,
									  HttpServletResponse response) 
								throws Throwable {
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("customerClasses", getJSONCustomerClassesSegment(site));
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
		return null;
	}	
	
	public ActionForward removeTierPrice(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
        initSiteProfiles(form, site);
	
	   	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
	   	String itemTierPriceId = form.getItemTierPriceId();
		Item item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
		
		for (ItemTierPrice itemTierPrice : item.getItemTierPrices()) {
			if (itemTierPrice.getItemTierPriceId().equals(Format.getLong(itemTierPriceId))) {
				em.remove(itemTierPrice);
				for (ItemTierPriceCurrency itemTierPriceCurrency : itemTierPrice.getItemTierPriceCurrencies()) {
					itemTierPrice.getItemTierPriceCurrencies().remove(itemTierPriceCurrency);
					em.remove(itemTierPriceCurrency);
				}
			}
		}
		item.setRecUpdateBy(adminBean.getUser().getUserId());
		item.setRecUpdateDatetime(new Date(System.currentTimeMillis()));

    	JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
		em.getTransaction().commit();
    	return null;
	}
	
	public ActionForward saveTierPrice(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
	   	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
	   	ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		
		MessageResources resources = this.getResources(request);
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		if (form.isSiteCurrencyClassDefault()) {
			if (Format.isNullOrEmpty(form.getItemTierQty())) {
				jsonResult.put("itemTierQty", resources.getMessage("error.string.required"));
			}
			else {
				if (!Format.isInt(form.getItemTierQty())) {
					jsonResult.put("itemTierQty", resources.getMessage("error.int.invalid"));
				}
			}
			if (Format.isNullOrEmpty(form.getItemTierPricePublishOn())) {
				jsonResult.put("itemTierPricePublishOn", resources.getMessage("error.string.required"));
			}
			else {
				if (!Format.isDate(form.getItemTierPricePublishOn())) {
					jsonResult.put("itemTierPricePublishOn", resources.getMessage("error.date.invalid"));
				}
			}
			if (Format.isNullOrEmpty(form.getItemTierPriceExpireOn())) {
				jsonResult.put("itemTierPriceExpireOn", resources.getMessage("error.string.required"));
			}
			else {
				if (!Format.isDate(form.getItemTierPriceExpireOn())) {
					jsonResult.put("itemTierPriceExpireOn", resources.getMessage("error.date.invalid"));
				}
			}
			if (jsonResult.length() > 0) {
				jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
				streamWebService(response, jsonResult.toHtmlString());
				return null;
			}
		}
		if (Format.isNullOrEmpty(form.getItemTierPrice())) {
			jsonResult.put("itemTierPrice", resources.getMessage("error.string.required"));
		}
		else {
			if (!Format.isInt(form.getItemTierPrice())) {
				jsonResult.put("itemTierPrice", resources.getMessage("error.float.invalid"));
			}
		}

	   	String itemTierPriceId = form.getItemTierPriceId();
		Item item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
		ItemTierPrice itemTierPrice = null;
		ItemTierPriceCurrency itemTierPriceCurrency = null;
		boolean exist = true;
		if (!Format.isNullOrEmpty(itemTierPriceId)) {
			itemTierPrice = ItemTierPriceDAO.load(site.getSiteId(), Format.getLong(itemTierPriceId));
			itemTierPriceCurrency = itemTierPrice.getItemTierPriceCurrency();
		}
		else {
			itemTierPrice = new ItemTierPrice();
			itemTierPrice.setRecCreateBy(adminBean.getUser().getUserId());
			itemTierPrice.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			itemTierPrice.setItem(item);
			
			itemTierPriceCurrency = new ItemTierPriceCurrency();
			exist = false;
			itemTierPriceCurrency.setRecCreateBy(adminBean.getUser().getUserId());
			itemTierPriceCurrency.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			itemTierPrice.setItemTierPriceCurrency(itemTierPriceCurrency);
			itemTierPrice.getItemTierPriceCurrencies().add(itemTierPriceCurrency);
			SiteCurrencyClass siteCurrencyClass = SiteCurrencyClassDAO.load(form.getSiteCurrencyClassDefaultId());
			itemTierPriceCurrency.setSiteCurrencyClass(siteCurrencyClass);
		}
		if (form.isSiteCurrencyClassDefault()) {
			itemTierPriceCurrency.setItemPrice(Format.getFloat(form.getItemTierPrice()));
			itemTierPriceCurrency.setRecUpdateBy(adminBean.getUser().getUserId());
			itemTierPriceCurrency.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			if (!exist) {
				em.persist(itemTierPriceCurrency);
			}

			itemTierPrice.setItemTierQty(Format.getIntObj(form.getItemTierQty()));
			if (!Format.isNullOrEmpty(form.getItemTierPricePublishOn())) {
				itemTierPrice.setItemTierPricePublishOn(Format.getDate(form.getItemTierPricePublishOn()));
			}
			else {
				itemTierPrice.setItemTierPricePublishOn(null);
			}
			if (!Format.isNullOrEmpty(form.getItemTierPriceExpireOn())) {
				itemTierPrice.setItemTierPriceExpireOn(Format.getDate(form.getItemTierPriceExpireOn()));
			}
			else {
				itemTierPrice.setItemTierPriceExpireOn(null);
			}
			itemTierPrice.setRecUpdateBy(adminBean.getUser().getUserId());
			itemTierPrice.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			if (Format.isNullOrEmpty(form.getCustClassId())) {
				itemTierPrice.setCustomerClass(null);
			}
			else {
				CustomerClass customerClass = CustomerClassDAO.load(site.getSiteId(), Format.getLong(form.getCustClassId()));
				itemTierPrice.setCustomerClass(customerClass);
			}
			em.persist(itemTierPrice);
		}
		else {
			itemTierPriceCurrency = null;
			SiteCurrencyClass siteCurrencyClass = SiteCurrencyClassDAO.load(form.getSiteCurrencyClassId());
			for (ItemTierPriceCurrency priceCurrency : itemTierPrice.getItemTierPriceCurrencies()) {
				if (priceCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteCurrencyClass.getSiteCurrencyClassId())) {
					itemTierPriceCurrency = priceCurrency;
					break;
				}
			}
			exist = true;
			if (itemTierPriceCurrency == null) {
				itemTierPriceCurrency = new ItemTierPriceCurrency();
				exist = false;
				itemTierPriceCurrency.setRecCreateBy(adminBean.getUser().getUserId());
				itemTierPriceCurrency.setRecCreateDatetime(new Date(System.currentTimeMillis()));
				itemTierPriceCurrency.setItemTierPrice(itemTierPrice);
				itemTierPriceCurrency.setSiteCurrencyClass(siteCurrencyClass);
			}
			if (form.isItemTierPriceOverride()) {
				itemTierPriceCurrency.setItemPrice(Format.getFloat(form.getItemTierPrice()));
			}
			else {
				itemTierPriceCurrency.setItemPrice(null);
			}
			itemTierPriceCurrency.setRecUpdateBy(adminBean.getUser().getUserId());
			itemTierPriceCurrency.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			if (!exist) {
				em.persist(itemTierPriceCurrency);
			}
		}

		item.setRecUpdateBy(adminBean.getUser().getUserId());
		item.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(item);

		jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("itemTierPriceId", itemTierPrice.getItemTierPriceId());
		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		streamWebService(response, jsonResult.toHtmlString());
		return null;
	}
	
	public ActionForward getCustomAttributes(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		CustomAttributeGroup customAttributeGroup = CustomAttributeGroupDAO.load(site.getSiteId(), Format.getLong(form.getCustomAttribGroupId()));
		Vector<JSONEscapeObject> customAttributeDetails = new Vector<JSONEscapeObject>();
		for (CustomAttributeDetail customAttributeDetail : customAttributeGroup.getCustomAttributeDetails()) {
			JSONEscapeObject result = new JSONEscapeObject();
			result.put("customAttribDetailId", customAttributeDetail.getCustomAttribDetailId());
			result.put("customAttribDesc", customAttributeDetail.getCustomAttribute().getCustomAttributeLanguage().getCustomAttribDesc());
			customAttributeDetails.add(result);
		}
		jsonResult.put("customAttributeDetails", customAttributeDetails);
	
		streamWebService(response, jsonResult.toHtmlString());
      return null;

	}

	public ActionForward remove(ActionMapping mapping, 
				  ActionForm actionForm,
				  HttpServletRequest httpServletRequest,
				  HttpServletResponse httpServletResponse) 
		throws Throwable {

		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(httpServletRequest);
		Site site = adminBean.getSite();
        initSiteProfiles(form, site);
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		
		Item item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
		
		try {
			ItemDAO.remove(site.getSiteId(), item);
			em.getTransaction().commit();
		}
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.item.constraint"));
				saveMessages(httpServletRequest, errors);
				createAdditionalInfo(adminBean, form, item);
				return mapping.findForward("error");
			}
			throw e;
		}
		
		Indexer.getInstance(site.getSiteId()).removeItem(item);
		return mapping.findForward("removeConfirm");
	}
	
	public ActionForward resetCounter(ActionMapping mapping, 
									  ActionForm actionForm,
									  HttpServletRequest request,
									  HttpServletResponse response) 
		throws Throwable {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);

		Item item = new Item();
		item = ItemDAO.load(adminBean.getSite().getSiteId(), Format.getLong(form.getItemId()));
		item.setItemHitCounter(new Integer(0));
		item.setRecUpdateBy(adminBean.getUser().getUserId());
		item.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		// em.update(item);
		
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
		em.getTransaction().commit();
    	return null;
	}
	
	public ActionForward comments(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		
		Item item = new Item();
		item = ItemDAO.load(adminBean.getSite().getSiteId(), Format.getLong(form.getItemId()));
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		Iterator<?> iterator = item.getComments().iterator();
		Vector<JSONEscapeObject> comments = new Vector<JSONEscapeObject>();
		while (iterator.hasNext()) {
			Comment comment = (Comment) iterator.next();
			JSONEscapeObject jsonComment = new JSONEscapeObject();
			jsonComment.put("commentId", comment.getCommentId());
			jsonComment.put("commentTitle", comment.getCommentTitle());
			jsonComment.put("comment", comment.getComment());
			jsonComment.put("moderation", comment.getModeration());
			jsonComment.put("commentApproved", comment.getCommentApproved());
			jsonComment.put("custEmail", comment.getCustomer().getCustEmail());
			jsonComment.put("custPublicName", comment.getCustomer().getCustPublicName());
			jsonComment.put("recCreateDatetime", Format.getFullDatetime(comment.getRecCreateDatetime()));
			jsonComment.put("agreeCount", comment.getAgreeCustomers().size());
			jsonComment.put("disagreeCount", comment.getDisagreeCustomers().size());
			comments.add(jsonComment);
		}
		jsonResult.put("comments", comments);
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
		em.getTransaction().commit();
		return null;
	}

	public ActionForward adjustQty(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
		MessageResources resources = this.getResources(request);
		
		Item item = new Item();
		item = ItemDAO.load(adminBean.getSite().getSiteId(), Format.getLong(form.getItemId()), LockModeType.WRITE);
		if (!Format.isInt(form.getItemAdjQty())) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			jsonResult.put("message", resources.getMessage("error.int.invalid"));
			String jsonString = jsonResult.toHtmlString();
			streamWebService(response, jsonString);
			em.getTransaction().commit();
	    	return null;
	    }

		int itemQty = Format.getInt(form.getItemAdjQty());
		InventoryEngine engine = new InventoryEngine(item.getItemId());
		engine.adjustQty(itemQty);
		form.setItemAdjQty("");
		item.setRecUpdateBy(adminBean.getUser().getUserId());
		item.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		// em.update(item);
		
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("itemQty", Format.getInt(item.getItemQty()));
		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
		em.getTransaction().commit();
    	return null;
	}

	public ActionForward adjustBookedQty(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
        initSiteProfiles(form, adminBean.getSite());
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
		MessageResources resources = this.getResources(request);
	
		Item item = new Item();
		item = ItemDAO.load(adminBean.getSite().getSiteId(), Format.getLong(form.getItemId()), LockModeType.WRITE);
		if (!Format.isInt(form.getItemAdjBookedQty())) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			jsonResult.put("message", resources.getMessage("error.int.invalid"));
			String jsonString = jsonResult.toHtmlString();
			streamWebService(response, jsonString);
			em.getTransaction().commit();
	    	return null;
	    }

		int itemBookedQty = Format.getInt(form.getItemAdjBookedQty());
		InventoryEngine engine = new InventoryEngine(item.getItemId());
		engine.adjustBookedQty(itemBookedQty);
		item.setRecUpdateBy(adminBean.getUser().getUserId());
		item.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		form.setItemAdjBookedQty("");
		// em.update(item);
		
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("itemBookedQty", Format.getInt(item.getItemBookedQty()));
		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
		em.getTransaction().commit();
    	return null;
	}
	
	public ActionForward showCategories(ActionMapping mapping, 
				 				   ActionForm actionForm,
				 				   HttpServletRequest request,
				 				   HttpServletResponse response) 
		throws Throwable {
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		initSiteProfiles(form, adminBean.getSite());
		Item item = new Item();
		item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
		JSONEscapeObject jsonResult = createJsonSelectedCategories(site.getSiteId(), item);
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
        return null;
	}

	public ActionForward removeCategories(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
        initSiteProfiles(form, adminBean.getSite());
		
	   	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Item item = ItemDAO.load(adminBean.getSite().getSiteId(), Format.getLong(form.getItemId()));
		String catIds[] = form.getRemoveCategories();
		if (catIds != null) {
			for (String catId : catIds) {
				Category category = CategoryDAO.load(site.getSiteId(), Format.getLong(catId));
				item.getCategories().remove(category);
			}
		}

		item.setRecUpdateBy(adminBean.getUser().getUserId());
		item.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		
    	JSONEscapeObject jsonResult = createJsonSelectedCategories(site.getSiteId(), item);
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
		em.getTransaction().commit();
		return null;
	}
	
	public ActionForward addCategories(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
        initSiteProfiles(form, site);
		String itemId = request.getParameter("itemId");
	
	   	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Item item = new Item();
		item = ItemDAO.load(site.getSiteId(), Format.getLong(itemId));
		String catIds[] = form.getAddCategories();
		if (catIds != null) {
			for (String catId : catIds) {
				Category category = CategoryDAO.load(site.getSiteId(), Format.getLong(catId));
				item.getCategories().add(category);
				item.setRecUpdateBy(adminBean.getUser().getUserId());
				item.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			}
		}
		
		JSONEscapeObject jsonResult = createJsonSelectedCategories(site.getSiteId(), item);
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
		em.getTransaction().commit();
    	return null;
	}
	
	public JSONEscapeObject createJsonSelectedCategories(String siteId, Item item) throws Exception {
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		Vector<JSONEscapeObject> categories = new Vector<JSONEscapeObject>();
		for (Category category : item.getCategories()) {
			JSONEscapeObject categoryObject = new JSONEscapeObject();
			categoryObject.put("catId", category.getCatId());
			categoryObject.put("catTitle", category.getCategoryLanguage().getCatTitle());
			categoryObject.put("catShortTitle", category.getCategoryLanguage().getCatShortTitle());
			categories.add(categoryObject);
		}
		jsonResult.put("categories", categories);
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);

		return jsonResult;
	}
	
	public ActionForward showMenus(ActionMapping mapping, 
				 				   ActionForm actionForm,
				 				   HttpServletRequest request,
				 				   HttpServletResponse response) 
		throws Throwable {
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		initSiteProfiles(form, adminBean.getSite());
		Item item = new Item();
		item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
		JSONEscapeObject jsonResult = createJsonSelectedMenus(site.getSiteId(), item, form.getSiteProfileClassId());
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
        return null;
	}
	
	public ActionForward removeMenus(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
        initSiteProfiles(form, site);
		
		Item item = new Item();
		item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));

		String menuIds[] = form.getRemoveMenus();
		if (menuIds != null) {
			for (int i = 0; i < menuIds.length; i++) {
				Menu menu = new Menu();
				menu = MenuDAO.load(site.getSiteId(), Format.getLong(menuIds[i]));
				menu.setItem(null);
				menu.setMenuUrl("");
				menu.setMenuType("");
				CacheDAO.removeByKeyPrefix(site.getSiteId(), Constants.CACHE_MENU + "." + menu.getMenuSetName());
			}
		}
		item.setRecUpdateBy(adminBean.getUser().getUserId());
		item.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.flush();
		
		JSONEscapeObject jsonResult = createJsonSelectedMenus(site.getSiteId(), item, form.getSiteProfileClassId());
		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
		em.getTransaction().commit();
		return null;
	}
	
	public ActionForward addMenus(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
        initSiteProfiles(form, adminBean.getSite());
		
		Item item = new Item();
		item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));

		String menuIds[] = form.getAddMenus();
		if (menuIds != null) {
			for (int i = 0; i < menuIds.length; i++) {
				Menu menu = new Menu();
				menu = MenuDAO.load(site.getSiteId(), Format.getLong(menuIds[i]));
				menu.setItem(item);
				menu.setContent(null);
				menu.setCategory(null);
				menu.setMenuUrl("");
				menu.setMenuType(Constants.MENU_ITEM);
				menu.setMenuWindowMode(form.getMenuWindowMode());
				menu.setMenuWindowTarget(form.getMenuWindowTarget());
				CacheDAO.removeByKeyPrefix(site.getSiteId(), Constants.CACHE_MENU + "." + menu.getMenuSetName());
			}
		}
		item.setRecUpdateBy(adminBean.getUser().getUserId());
		item.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.flush();
		
		JSONEscapeObject jsonResult = createJsonSelectedMenus(site.getSiteId(), item, form.getSiteProfileClassId());
		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
		em.getTransaction().commit();
    	return null;
	}
	
	public JSONEscapeObject createJsonSelectedMenus(String siteId, Item item, Long siteProfileClassId) throws Exception {
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		Iterator<?> iterator = item.getMenus().iterator();
		Vector<JSONEscapeObject> menus = new Vector<JSONEscapeObject>();
		while (iterator.hasNext()) {
			Menu menu = (Menu) iterator.next();
			JSONEscapeObject menuObject = new JSONEscapeObject();
			menuObject.put("menuId", menu.getMenuId());
			menuObject.put("menuLongDesc", Utility.formatMenuName(siteId, menu.getMenuId(), siteProfileClassId));
			menuObject.put("siteDomainName", menu.getSiteDomain().getSiteDomainLanguage().getSiteName());
			menus.add(menuObject);
		}
		jsonResult.put("menus", menus);
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);

		return jsonResult;
	}
	
	public ActionForward showImages(ActionMapping mapping, 
				 				   ActionForm actionForm,
				 				   HttpServletRequest request,
				 				   HttpServletResponse response) 
		throws Throwable {
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		initSiteProfiles(form, adminBean.getSite());
		Item item = new Item();
		item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
		JSONEscapeObject jsonResult = createJsonImages(site.getSiteId(), item, form);
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
        return null;
	}
	
	public ActionForward uploadImage(ActionMapping mapping, 
			  						 ActionForm actionForm,
			  						 HttpServletRequest request,
			  						 HttpServletResponse response) 
		throws Throwable {
		
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		
		MessageResources resources = this.getResources(request);
		JSONEscapeObject jsonResult = new JSONEscapeObject();

		Item item = new Item();
		item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getKey()));

		FormFile file = form.getFile();
		byte fileData[] = file.getFileData();
		ActionMessages errors = validateUploadImage(form, file);
		if (errors.size() != 0) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			jsonResult.put("message", resources.getMessage("error.string.required")); 
			streamWebService(response, jsonResult.toHtmlString());
		}
		
		ImageScaler scaler = null;
		try {
			scaler = new ImageScaler(fileData, file.getContentType());
			scaler.resize(1000);
		}
		catch (OutOfMemoryError outOfMemoryError) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			jsonResult.put("message", resources.getMessage("content.error.image.size"));
			streamWebService(response, jsonResult.toHtmlString());
	        return null;
		}
		catch (Throwable e) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			jsonResult.put("message", resources.getMessage("error.image.invalid"));
			streamWebService(response, jsonResult.toHtmlString());
	        return null;
		}
		
		ItemLanguage itemLanguage = null;
		for (ItemLanguage language : item.getItemLanguages()) {
			if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassId())) {
				itemLanguage = language;
				break;
			}
		}
		
		if (form.isSiteProfileClassDefault()) {
			ItemImage itemImage = new ItemImage();
			itemImage.setImageName(file.getFileName());
			itemImage.setContentType("image/jpeg");
			itemImage.setImageValue(scaler.getBytes());
			itemImage.setImageHeight(scaler.getHeight());
			itemImage.setImageWidth(scaler.getWidth());
			itemImage.setRecUpdateBy(adminBean.getUser().getUserId());
			itemImage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			itemImage.setRecCreateBy(adminBean.getUser().getUserId());
			itemImage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			em.persist(itemImage);
			if (itemLanguage.getImage() == null) {
				itemLanguage.setImage(itemImage);
			}
			else {
				itemImage.setItemLanguage(itemLanguage);
				itemLanguage.getImages().add(itemImage);
			}
			itemLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
			itemLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			em.persist(itemLanguage);
		}
		else {
			if (itemLanguage == null) {
				itemLanguage = new ItemLanguage();
				itemLanguage.setItem(item);
				itemLanguage.setRecCreateBy(adminBean.getUser().getUserId());
				itemLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
				SiteProfileClass siteProfileClass = SiteProfileClassDAO.load(form.getSiteProfileClassId());
				itemLanguage.setSiteProfileClass(siteProfileClass);
			}
			ItemImage itemImage = new ItemImage();
			itemImage.setImageName(file.getFileName());
			itemImage.setContentType("image/jpeg");
			itemImage.setImageValue(scaler.getBytes());
			itemImage.setImageHeight(scaler.getHeight());
			itemImage.setImageWidth(scaler.getWidth());
			itemImage.setRecUpdateBy(adminBean.getUser().getUserId());
			itemImage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			itemImage.setRecCreateBy(adminBean.getUser().getUserId());
			itemImage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			em.persist(itemImage);
			if (itemLanguage.getImage() == null) {
				itemLanguage.setImage(itemImage);
			}
			else {
				itemImage.setItemLanguage(itemLanguage);
				itemLanguage.getImages().add(itemImage);
			}
			itemLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
			itemLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			em.persist(itemImage);
		}
		
		em.flush();
		em.refresh(item);
		jsonResult = createJsonImages(site.getSiteId(), item, form);
		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
        return null;
	}
	
	public JSONEscapeObject createJsonImages(String siteId, Item item, ItemMaintActionForm form) throws Exception {
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("isSiteProfileClassDefault", form.isSiteProfileClassDefault());
		
		Vector<JSONEscapeObject> vector = new Vector<JSONEscapeObject>();
		ItemLanguage itemLanguageDefault = null;
		ItemLanguage itemLanguage = null;
		for (ItemLanguage language : item.getItemLanguages()) {
			if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassDefaultId())) {
				itemLanguageDefault = language;
			}
			if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassId())) {
				itemLanguage = language;
			}
		}
		
		ItemLanguage effectiveLanguage = itemLanguageDefault;
		if (!form.isSiteProfileClassDefault() && itemLanguage != null) {
			if (itemLanguage.getItemImageOverride().equals(String.valueOf(Constants.VALUE_YES))) {
				effectiveLanguage = itemLanguage;
			}
		}
		
		ItemImage defaultImage = effectiveLanguage.getImage();
		if (defaultImage != null) {
			JSONEscapeObject jsonDefaultImage = new JSONEscapeObject();
			jsonDefaultImage.put("imageId", defaultImage.getImageId());
			jsonDefaultImage.put("imageName", defaultImage.getImageName());
			jsonDefaultImage.put("isLanguageDefault", true);
			jsonResult.put("defaultImage", jsonDefaultImage);
		}
		
		Iterator<?> iterator = effectiveLanguage.getImages().iterator();
		while (iterator.hasNext()) {
			ItemImage image = (ItemImage) iterator.next();
			JSONEscapeObject jsonImage = new JSONEscapeObject();
			jsonImage.put("imageId", image.getImageId());
			jsonImage.put("imageName", image.getImageName());
			jsonImage.put("isLanguageDefault", true);
			vector.add(jsonImage);
		}
		jsonResult.put("images", vector);
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		return jsonResult;
	}
	
	public ActionForward overrideImages(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		initSiteProfiles(form, adminBean.getSite());
		
		Item item = ItemDAO.load(adminBean.getSite().getSiteId(), Format.getLong(form.getItemId()));
	  	boolean found = false;
	  	Iterator<?> iterator = item.getItemLanguages().iterator();
	  	ItemLanguage itemLanguage = null;
		while (iterator.hasNext()) {
			itemLanguage = (ItemLanguage) iterator.next();
			if (itemLanguage.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassId())) {
				found = true;
				break;
			}
		}
		if (!found) {
			SiteProfileClass siteProfileClass = (SiteProfileClass) em.find(SiteProfileClass.class, form.getSiteProfileClassId());
			itemLanguage = new ItemLanguage();
			itemLanguage.setItem(item);
			itemLanguage.setRecCreateBy(adminBean.getUser().getUserId());
			itemLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			itemLanguage.setSiteProfileClass(siteProfileClass);
		}
		if (form.getImagesOverride().equalsIgnoreCase("true")) {
			itemLanguage.setItemImageOverride(String.valueOf(Constants.VALUE_YES));
		}
		else {
			ItemImage itemImage = null;
			if (itemLanguage.getImage() != null) {
				itemImage = itemLanguage.getImage();
				em.remove(itemImage);
				itemLanguage.setImage(null);
			}
			iterator = itemLanguage.getImages().iterator();
			while (iterator.hasNext()) {
				itemImage = (ItemImage) iterator.next();
				em.remove(itemImage);
			}
			itemLanguage.setItemImageOverride(String.valueOf(Constants.VALUE_NO));
		}
		itemLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
		itemLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(itemLanguage);
		
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
		return null;
	}
	
	public ActionForward removeImages(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, adminBean.getSite());
		
		Item item = new Item();
		item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
		String imageIds[] = form.getRemoveImages();
		
		ItemLanguage itemLanguage = null;
		for (ItemLanguage language : item.getItemLanguages()) {
			if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassId())) {
				itemLanguage = language;
				break;
			}
		}
		if (itemLanguage != null) {
			ItemImage defaultImage = itemLanguage.getImage();
			if (imageIds != null) {
				for (int i = 0; i < imageIds.length; i++) {
					if (defaultImage != null && defaultImage.getImageId().equals(Format.getLong(imageIds[i]))) {
						itemLanguage.setImage(null);
						em.persist(itemLanguage);
						em.remove(defaultImage);
						defaultImage = null;
					}
					else {
						ItemImage itemImage = ItemImageDAO.load(site.getSiteId(), Format.getLong(imageIds[i]));
						em.remove(itemImage);
					}
				}
			}
			itemLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
			itemLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			em.flush();
			em.refresh(itemLanguage);
			
			if (itemLanguage.getImage() == null) {
				Set<?> images = itemLanguage.getImages();
				if (!images.isEmpty()) {
					ItemImage itemImage = (ItemImage) images.iterator().next();
					itemLanguage.setImage(itemImage);
					images.remove(itemImage);
				}
			}
		}
		em.flush();
		em.refresh(item);
		
		JSONEscapeObject jsonResult = createJsonImages(site.getSiteId(), item, form);
		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
        return null;
	}
	
	public ActionForward defaultImage(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		
		Item item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
		
		ItemLanguage itemLanguage = null;
		for (ItemLanguage language : item.getItemLanguages()) {
			if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassId())) {
				itemLanguage = language;
				break;
			}
		}
		String defaultImageId = form.getCreateDefaultImageId();
		ItemImage itemImage = ItemImageDAO.load(site.getSiteId(), Format.getLong(defaultImageId));
		
		ItemImage currentImage = itemLanguage.getImage();
		currentImage.setItemLanguage(itemLanguage);
		itemLanguage.getImages().add(currentImage);
		
		itemImage.setItemLanguage(null);
		itemLanguage.setImage(itemImage);
		itemLanguage.getImages().remove(itemImage);
		
		itemLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
		itemLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(itemLanguage);
		
		JSONEscapeObject jsonResult = createJsonImages(site.getSiteId(), item, form);
		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
        return null;
	}

	public void createAdditionalInfo(AdminBean adminBean, ItemMaintActionForm form, Item item) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Site site = adminBean.getSite();
    	Long siteProfileClassId = form.getSiteProfileClassId();
    	Long siteCurrencyClassId = form.getSiteCurrencyClassId();
		
		form.setItemHitCounter(Format.getIntObj(item.getItemHitCounter()));
		form.setItemRating(Format.getFloatObj(item.getItemRating()));
		form.setItemRatingCount(Format.getIntObj(item.getItemRatingCount()));
		form.setItemQty(Format.getIntObj(item.getItemQty()));
		form.setItemBookedQty(Format.getIntObj(item.getItemBookedQty()));
		
	 	Iterator<?> iterator = item.getMenus().iterator();
	 	Vector<ItemMenuDisplayForm> selectedMenuVector = new Vector<ItemMenuDisplayForm>();
		while (iterator.hasNext()) {
			Menu menu = (Menu) iterator.next();
			ItemMenuDisplayForm menuDisplayForm = new ItemMenuDisplayForm();
			menuDisplayForm.setMenuId(Format.getLong(menu.getMenuId()));
			menuDisplayForm.setMenuLongDesc(Utility.formatMenuName(site.getSiteId(), menu.getMenuId(), form.getSiteProfileClassId()));
			menuDisplayForm.setSiteDomainName(menu.getSiteDomain().getSiteDomainLanguage().getSiteName());
			selectedMenuVector.add(menuDisplayForm);
		}
		ItemMenuDisplayForm selectedMenuList[] = new ItemMenuDisplayForm[selectedMenuVector.size()];
		selectedMenuVector.copyInto(selectedMenuList);
		form.setSelectedMenus(selectedMenuList);
		form.setSelectedMenusCount(selectedMenuList.length);
//        form.setMenuList(Utility.makeMenuTreeList(site.getSiteId(), form.getSiteProfileClassId()));

		Query query = em.createQuery("from ShippingType shippingType where siteId = :siteId order by shippingTypeName");
		query.setParameter("siteId", site.getSiteId());
		iterator = query.getResultList().iterator();
		Vector<LabelValueBean> shippingTypeList = new Vector<LabelValueBean>();
		while (iterator.hasNext()) {
			ShippingType shippingType = (ShippingType) iterator.next();
			LabelValueBean bean = new LabelValueBean(shippingType.getShippingTypeName(), shippingType.getShippingTypeId().toString());
			shippingTypeList.add(bean);
		}
		LabelValueBean shippingTypes[] = new LabelValueBean[shippingTypeList.size()];
		shippingTypeList.copyInto(shippingTypes);
		form.setShippingTypes(shippingTypes);
		
		String sql = "select   productClass " +
					 "from     ProductClass productClass " +
					 "left     join productClass.site site " +
					 "where    site.siteId = :siteId " +
					 "order    by productClass.productClassName ";	 
		query = em.createQuery(sql);
		query.setParameter("siteId", site.getSiteId());
		iterator = query.getResultList().iterator();
		Vector<LabelValueBean> productClassList = new Vector<LabelValueBean>();
		while (iterator.hasNext()) {
			ProductClass productClass = (ProductClass) iterator.next();
			LabelValueBean bean = new LabelValueBean(productClass.getProductClassName(), productClass.getProductClassId().toString());;
			productClassList.add(bean);
		}
		LabelValueBean productClasses[] = new LabelValueBean[productClassList.size()];
		productClassList.copyInto(productClasses);
		form.setProductClasses(productClasses);
		
		sql = "select   customAttributeGroup " +
			  "from     CustomAttributeGroup customAttributeGroup " +
		      "left     join customAttributeGroup.site site " +
		      "where    site.siteId = :siteId " +
		      "order    by customAttributeGroup.customAttribGroupName";
		query = em.createQuery(sql);
		query.setParameter("siteId", adminBean.getSite().getSiteId());
		iterator = query.getResultList().iterator();
		Vector<LabelValueBean> customAttributesList = new Vector<LabelValueBean>();
		customAttributesList.add(new LabelValueBean("", ""));
		while (iterator.hasNext()) {
			CustomAttributeGroup customAttributeGroup = (CustomAttributeGroup) iterator.next();
			LabelValueBean bean = new LabelValueBean(customAttributeGroup.getCustomAttribGroupName(),
													 customAttributeGroup.getCustomAttribGroupId().toString());
			customAttributesList.add(bean);
		}
		LabelValueBean customAttributeGroups[] = new LabelValueBean[customAttributesList.size()];
		customAttributesList.copyInto(customAttributeGroups);
		form.setCustomAttributeGroups(customAttributeGroups);
		
		ItemAttributeDetailDisplayForm itemAttributeDetails[] = form.getItemAttributeDetails();
		if (itemAttributeDetails != null) {
			for (int i = 0; i < itemAttributeDetails.length; i++) {
				ItemAttributeDetailDisplayForm displayForm = itemAttributeDetails[i];
				CustomAttribute customAttribute = CustomAttributeDAO.load(site.getSiteId(), Format.getLong(displayForm.getCustomAttribId()));
				if (customAttribute.getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_USER_INPUT) {
					continue;
				}
				if (customAttribute.getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_CUST_SELECT_DROPDOWN) {
					continue;
				}
				Vector<LabelValueBean> options = new Vector<LabelValueBean>();
				for (CustomAttributeOption customAttributeOption : customAttribute.getCustomAttributeOptions()) {
					String value = "";
					if (customAttribute.getCustomAttribDataTypeCode() == Constants.CUSTOM_ATTRIBUTE_DATA_TYPE_CURRENCY) {
						value = customAttributeOption.getCustomAttributeOptionCurrency().getCustomAttribValue();
						if (!form.isSiteCurrencyClassDefault()) {
							for (CustomAttributeOptionCurrency optionCurrency : customAttributeOption.getCustomAttributeOptionCurrencies()) {
								if (optionCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteCurrencyClassId)) {
									if (optionCurrency.getCustomAttribValue() != null) {
										value = optionCurrency.getCustomAttribValue();
									}
									break;
								}
							}
						}
					}
					else {
						value = customAttributeOption.getCustomAttributeOptionLanguage().getCustomAttribValue();
						if (!form.isSiteProfileClassDefault()) {
							for (CustomAttributeOptionLanguage optionLanguage : customAttributeOption.getCustomAttributeOptionLanguages()) {
								if (optionLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
									if (optionLanguage.getCustomAttribValue() != null) {
										value = optionLanguage.getCustomAttribValue();
									}
									break;
								}
							}
						}
					}
					LabelValueBean option = new LabelValueBean(value, customAttributeOption.getCustomAttribOptionId().toString());
					options.add(option);
				}
				LabelValueBean customAttribOptions[] = new LabelValueBean[options.size()];
				options.copyInto(customAttribOptions);
				displayForm.setCustomAttribOptions(customAttribOptions);
			}
		}
		
		if (!form.getMode().equals(Constants.MODE_CREATE)) {
			sql = "select   itemTierPrice " +
				  "from     ItemTierPrice itemTierPrice " +
				  "left     join itemTierPrice.item item " +
				  "left     join itemTierPrice.customerClass customerClass " +
				  "where    item.itemId = :itemId " +
				  "order    by customerClass.custClassName, itemTierPrice.itemTierQty";
			query = em.createQuery(sql);
			query.setParameter("itemId", item.getItemId());
			iterator = query.getResultList().iterator();
			Vector<ItemTierPriceDisplayForm> vector = new Vector<ItemTierPriceDisplayForm>();
			while (iterator.hasNext()) {
				ItemTierPrice itemTierPrice = (ItemTierPrice) iterator.next();
				ItemTierPriceDisplayForm priceForm = new ItemTierPriceDisplayForm();
				priceForm.setItemTierPriceId(itemTierPrice.getItemTierPriceId().toString());
				priceForm.setCustClassId("");
				if (itemTierPrice.getCustomerClass() != null) {
					priceForm.setCustClassId(itemTierPrice.getCustomerClass().getCustClassId().toString());
				}
				priceForm.setItemTierQty(itemTierPrice.getItemTierQty().toString());
				priceForm.setItemTierPrice(Format.getFloat(itemTierPrice.getItemTierPriceCurrency().getItemPrice()));
				priceForm.setItemTierPricePublishOn(Format.getDate(itemTierPrice.getItemTierPricePublishOn()));
				priceForm.setItemTierPriceExpireOn(Format.getDate(itemTierPrice.getItemTierPriceExpireOn()));
				vector.add(priceForm);
			}
			ItemTierPriceDisplayForm itemTierPrices[] = new ItemTierPriceDisplayForm[vector.size()];
			vector.copyInto(itemTierPrices);
			form.setItemTierPrices(itemTierPrices);
		}
		
		Vector<LabelValueBean> siteDomainList = new Vector<LabelValueBean>();
		for (SiteDomain siteDomain : site.getSiteDomains()) {
			siteDomainList.add(new LabelValueBean(siteDomain.getSiteDomainLanguage().getSiteName(), siteDomain.getSiteDomainId().toString()));
		}
		LabelValueBean siteDomains[] = new LabelValueBean[siteDomainList.size()];
		siteDomainList.copyInto(siteDomains);
		form.setSiteDomains(siteDomains);
		
		form.setShareInventory(false);
		if (site.getShareInventory() == Constants.VALUE_YES) {
			form.setShareInventory(true);
		}
	}
	
	private void copyProperties(HttpServletRequest request, ItemMaintActionForm form, Item item) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Long siteCurrencyClassId = form.getSiteCurrencyClassId();
    	ItemLanguage defaultItemLanguage = null;
    	for (ItemLanguage language : item.getItemLanguages()) {
    		if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassDefaultId())) {
    			defaultItemLanguage = language;
    		}
    	}
    	
    	ItemPriceCurrency regularPrice = item.getItemPrice();
    	ItemPriceCurrency specialPrice = item.getItemSpecPrice();
    	
		MessageResources resources = this.getResources(request);
		form.setItemId(Format.getLong(item.getItemId()));
		form.setItemNum(item.getItemNum());
		form.setItemUpcCd(item.getItemUpcCd());
		form.setItemSkuCd(item.getItemSkuCd());
		form.setItemSellable(item.getItemSellable() == 'Y' ? true : false);
		form.setItemTypeCd(item.getItemTypeCd());
		form.setItemTypeDesc(resources.getMessage("item.typeCode." + item.getItemTypeCd()));
		form.setItemShortDesc(defaultItemLanguage.getItemShortDesc());
		form.setItemDesc(defaultItemLanguage.getItemDesc());
		form.setPageTitle(defaultItemLanguage.getPageTitle());
		form.setMetaKeywords(defaultItemLanguage.getMetaKeywords());
		form.setMetaDescription(defaultItemLanguage.getMetaDescription());
		form.setItemCost(Format.getFloatObj(item.getItemCost()));
		form.setItemPrice(Format.getFloatObj(regularPrice.getItemPrice()));
		form.setItemSpecPrice("");
		form.setItemSpecPublishOn("");
		form.setItemSpecExpireOn("");
		if (specialPrice != null) {
			form.setItemSpecPrice(Format.getFloatObj(specialPrice.getItemPrice()));
			form.setItemSpecPublishOn(Format.getDate(specialPrice.getItemPricePublishOn()));
			form.setItemSpecExpireOn(Format.getDate(specialPrice.getItemPriceExpireOn()));	
		}
		form.setItemPublishOn(Format.getDate(item.getItemPublishOn()));
		form.setItemExpireOn(Format.getDate(item.getItemExpireOn()));
		form.setPublished(item.getPublished() == 'Y' ? true : false);
		if (item.getCustomAttributeGroup() != null) {
			form.setCustomAttribGroupId(Format.getLong(item.getCustomAttributeGroup().getCustomAttribGroupId()));
			form.setCustomAttribGroupName(item.getCustomAttributeGroup().getCustomAttribGroupName());
		}
		form.setRemoveImages(null);
		form.setRemoveMenus(null);
		form.setMenuWindowMode("");
		ShippingType shippingType = item.getShippingType();
		if (shippingType != null) {
			form.setShippingTypeId(shippingType.getShippingTypeId().toString());
		}
		ProductClass productClass = item.getProductClass();
		if (productClass != null) {
			form.setProductClassId(productClass.getProductClassId().toString());
		}
		form.setRecUpdateBy(item.getRecUpdateBy());
		form.setRecUpdateDatetime(Format.getFullDatetime(item.getRecUpdateDatetime()));
		form.setRecCreateBy(item.getRecCreateBy());
		form.setRecCreateDatetime(Format.getFullDatetime(item.getRecCreateDatetime()));
		
		if (!form.isSiteProfileClassDefault()) {
			form.setItemImageOverride(false);
			form.setItemShortDescLangFlag(false);
			form.setItemDescLangFlag(false);
			form.setPageTitleLangFlag(false);
			form.setMetaKeywordsLangFlag(false);
			form.setMetaDescriptionLangFlag(false);
			form.setItemShortDescLang(defaultItemLanguage.getItemShortDesc());
			form.setItemDescLang(defaultItemLanguage.getItemDesc());
			form.setPageTitleLang(defaultItemLanguage.getPageTitle());
			form.setMetaKeywordsLang(defaultItemLanguage.getMetaKeywords());
			form.setMetaDescriptionLang(defaultItemLanguage.getMetaDescription());
	    	Iterator<?> iterator = item.getItemLanguages().iterator();
	    	boolean found = false;
	    	ItemLanguage itemLanguage = null;
	    	Long siteProfileClassId = form.getSiteProfileClassId();
	    	while (iterator.hasNext()) {
	    		itemLanguage = (ItemLanguage) iterator.next();
	    		if (itemLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
	    			found = true;
	    			break;
	    		}
	    	}
	    	if (found) {
	    		if (itemLanguage.getItemShortDesc() != null) {
		    		form.setItemShortDescLangFlag(true);
		    		form.setItemShortDescLang(itemLanguage.getItemShortDesc());
	    		}
	    		if (itemLanguage.getItemDesc() != null) {
		    		form.setItemDescLangFlag(true);
		    		form.setItemDescLang(itemLanguage.getItemDesc());
	    		}
	    		if (itemLanguage.getPageTitle() != null) {
	    			form.setPageTitleLangFlag(true);
	    			form.setPageTitleLang(itemLanguage.getPageTitle());
	    		}
	    		if (itemLanguage.getMetaKeywords() != null) {
	    			form.setMetaKeywordsLangFlag(true);
	    			form.setMetaKeywordsLang(itemLanguage.getMetaKeywords());
	    		}
	    		if (itemLanguage.getMetaDescription() != null) {
	    			form.setMetaDescriptionLangFlag(true);
	    			form.setMetaDescriptionLang(itemLanguage.getMetaDescription());
	    		}
	    		if (itemLanguage.getItemImageOverride().equalsIgnoreCase(String.valueOf(Constants.VALUE_YES))) {
	    			form.setItemImageOverride(true);
	    		}
	    	}
		}
		
		if (!form.isSiteCurrencyClassDefault()) {
	    	ItemPriceCurrency regularPriceCurr = null;
	    	ItemPriceCurrency specialPriceCurr = null;
	    	for (ItemPriceCurrency itemPriceCurrency : item.getItemPriceCurrencies()) {
	    		if (!itemPriceCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(form.getSiteCurrencyClassId())) {
	    			continue;
	    		}
	    		if (itemPriceCurrency.getItemPriceTypeCode() == Constants.ITEM_PRICE_TYPE_CODE_REGULAR) {
	    			regularPriceCurr = itemPriceCurrency;
	    		}
	    		if (itemPriceCurrency.getItemPriceTypeCode() == Constants.ITEM_PRICE_TYPE_CODE_SPECIAL) {
	    			specialPriceCurr = itemPriceCurrency;
	    		}	
	    	}
	    	form.setItemPriceCurrFlag(false);
	    	form.setItemSpecPriceCurrFlag(false);
	    	form.setItemPriceCurr(form.getItemPrice());
	    	form.setItemSpecPriceCurr(form.getItemSpecPrice());
	    	if (regularPriceCurr != null) {
	    		form.setItemPriceCurr(Format.getFloatObj(regularPriceCurr.getItemPrice()));
	    		form.setItemPriceCurrFlag(true);
	    	}
	    	if (specialPriceCurr != null) {
	    		form.setItemSpecPriceCurr(Format.getFloatObj(specialPriceCurr.getItemPrice()));
	    		form.setItemSpecPriceCurrFlag(true);
	    	}
		}
		
		String sql = "select   customerClass " +
		 	  "from     CustomerClass customerClass " +
		 	  "left     join customerClass.site site " +
		 	  "where    site.siteId = :siteId " +
		 	  "order    by customerClass.custClassName ";	 
		Query query = em.createQuery(sql);
		query.setParameter("siteId", item.getSite().getSiteId());
		Iterator<?> iterator = query.getResultList().iterator();
		Vector<LabelValueBean> custClassList = new Vector<LabelValueBean>();
		custClassList.add(new LabelValueBean("", ""));
		while (iterator.hasNext()) {
			CustomerClass customerClass = (CustomerClass) iterator.next();
			LabelValueBean bean = new LabelValueBean(customerClass.getCustClassName(), customerClass.getCustClassId().toString());;
			custClassList.add(bean);
		}
		LabelValueBean custClasses[] = new LabelValueBean[custClassList.size()];
		custClassList.copyInto(custClasses);
		form.setCustClasses(custClasses);

		Vector<ItemAttributeDetailDisplayForm> itemAttributeDetails = new Vector<ItemAttributeDetailDisplayForm>();
		if (item.getCustomAttributeGroup() != null) {
			sql = "select   customAttributeDetail " +
			      "from     CustomAttributeGroup customAttributeGroup " +
			      "join 	customAttributeGroup.customAttributeDetails customAttributeDetail  " +
			      "where    customAttributeGroup.customAttribGroupId = :customAttribGroupId " +
			      "order    by customAttributeDetail.seqNum ";
			query = em.createQuery(sql);
			query.setParameter("customAttribGroupId", item.getCustomAttributeGroup().getCustomAttribGroupId());
			iterator = query.getResultList().iterator();
			boolean itemAttributeLangFlag = false;
			while (iterator.hasNext()) {
				CustomAttributeDetail customAttributeDetail = (CustomAttributeDetail) iterator.next();
				CustomAttribute customAttribute = customAttributeDetail.getCustomAttribute();
				ItemAttributeDetailDisplayForm displayForm = new ItemAttributeDetailDisplayForm();
				displayForm.setCustomAttribId(customAttribute.getCustomAttribId().toString());
				displayForm.setCustomAttribDetailId(customAttributeDetail.getCustomAttribDetailId().toString());
				displayForm.setCustomAttribName(customAttribute.getCustomAttribName());
				displayForm.setCustomAttribTypeCode(String.valueOf(customAttribute.getCustomAttribTypeCode()));
				boolean attributeExist = false;
				Iterator<?> details = item.getItemAttributeDetails().iterator();
				ItemAttributeDetail itemAttributeDetail = null;
				while (details.hasNext()) {
					itemAttributeDetail = (ItemAttributeDetail) details.next();
					if (itemAttributeDetail.getCustomAttributeDetail().getCustomAttribDetailId().equals(customAttributeDetail.getCustomAttribDetailId())) {
						attributeExist = true;
						break;
					}
				}
				if (attributeExist) {
					displayForm.setItemAttribDetailId(itemAttributeDetail.getItemAttribDetailId().toString());
					if (customAttribute.getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_USER_INPUT) {
						displayForm.setItemAttribDetailValue(itemAttributeDetail.getItemAttributeDetailLanguage().getItemAttribDetailValue());
					}
					CustomAttributeOption customAttributeOption = itemAttributeDetail.getCustomAttributeOption();
					if (customAttributeOption != null) {
						displayForm.setCustomAttribOptionId(customAttributeOption.getCustomAttribOptionId().toString());
						if (customAttribute.getCustomAttribDataTypeCode() == Constants.CUSTOM_ATTRIBUTE_DATA_TYPE_CURRENCY) {
							displayForm.setItemAttribDetailValue(customAttributeOption.getCustomAttributeOptionCurrency().getCustomAttribValue());
						}
						else {
							displayForm.setItemAttribDetailValue(customAttributeOption.getCustomAttributeOptionLanguage().getCustomAttribValue());
						}
					}
					if (!form.isSiteProfileClassDefault()) {
						boolean found = false;
				    	Long siteProfileClassId = form.getSiteProfileClassId();
				    	ItemAttributeDetailLanguage itemAttributeDetailLanguage = null;
						Iterator<?> detailLanguages = itemAttributeDetail.getItemAttributeDetailLanguages().iterator();
						while (detailLanguages.hasNext()) {
							itemAttributeDetailLanguage = (ItemAttributeDetailLanguage) detailLanguages.next();
				    		if (itemAttributeDetailLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
				    			found = true;
				    			break;
				    		}
						}
						displayForm.setItemAttribDetailValueLangFlag(false);
						displayForm.setItemAttribDetailValueLang(displayForm.getItemAttribDetailValue());
						if (found) {
							itemAttributeLangFlag = true;
							displayForm.setItemAttribDetailValueLangFlag(false);
							if (itemAttributeDetailLanguage.getItemAttribDetailValue() != null) {
								displayForm.setItemAttribDetailValueLangFlag(true);
								displayForm.setItemAttribDetailValueLang(itemAttributeDetailLanguage.getItemAttribDetailValue());
							}
						}
					}
				}
				else {
					displayForm.setItemAttribDetailId("");
					displayForm.setItemAttribDetailValue("");
					displayForm.setCustomAttribOptionId("");
				}
				itemAttributeDetails.add(displayForm);
			}
			form.setItemAttribDetailValueLangFlag(itemAttributeLangFlag);
		}
		ItemAttributeDetailDisplayForm[] itemAttributeDetailDisplayForms = new ItemAttributeDetailDisplayForm[itemAttributeDetails.size()];
		itemAttributeDetails.copyInto(itemAttributeDetailDisplayForms);
		form.setItemAttributeDetails(itemAttributeDetailDisplayForms);
		
		if (!form.isSiteCurrencyClassDefault()) {
			form.setItemPriceCurrFlag(false);
			form.setItemPriceCurr(Format.getFloat(regularPrice.getItemPrice()));
			form.setItemSpecPriceCurrFlag(false);
			if (specialPrice != null) {
				form.setItemSpecPriceCurr(Format.getFloat(specialPrice.getItemPrice()));
			}
			ItemPriceCurrency regularPriceCurrency = null;
			ItemPriceCurrency specialPriceCurrency = null;
			for (ItemPriceCurrency itemPriceCurrency : item.getItemPriceCurrencies()) {
				if (!itemPriceCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteCurrencyClassId)) {
	    			continue;
	    		}
				if (itemPriceCurrency.getItemPriceTypeCode() == Constants.ITEM_PRICE_TYPE_CODE_REGULAR) {
					regularPriceCurrency = itemPriceCurrency;
				}
				if (itemPriceCurrency.getItemPriceTypeCode() == Constants.ITEM_PRICE_TYPE_CODE_SPECIAL) {
					specialPriceCurrency = itemPriceCurrency;
				}
			}
			if (regularPriceCurrency != null) {
	    		form.setItemPriceCurrFlag(true);
	    		form.setItemPriceCurr(Format.getFloat(regularPriceCurrency.getItemPrice()));
			}
			if (specialPriceCurrency != null) {
	    		form.setItemSpecPriceCurrFlag(true);
	    		form.setItemSpecPriceCurr(Format.getFloat(specialPriceCurrency.getItemPrice()));
			}
		}
		
		form.setItemSkusExist(false);
		if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_TEMPLATE)) {
			if (item.getItemSkus().size() > 0) {
				form.setItemSkusExist(true);
			}
		}
		
		sql = "from  SiteDomain siteDomain " +
			  "where siteDomain.site.siteId = :siteId " +
			  "order by siteDomain.siteDomainId";
		query = em.createQuery(sql);
		query.setParameter("siteId", item.getSite().getSiteId());
		Vector<ItemSiteDomainDisplayForm> vector = new Vector<ItemSiteDomainDisplayForm>();
		iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			SiteDomain siteDomain = (SiteDomain) iterator.next();
			ItemSiteDomainDisplayForm itemSiteDomainDisplayForm = new ItemSiteDomainDisplayForm();
			itemSiteDomainDisplayForm.setSiteDomainId(siteDomain.getSiteDomainId().toString());
			itemSiteDomainDisplayForm.setSiteName(siteDomain.getSiteDomainLanguage().getSiteName());
			boolean found = false;
			for (SiteDomain s : item.getSiteDomains()) {
				if (siteDomain.getSiteDomainId().equals(s.getSiteDomainId())) {
					found = true;
					break;
				}
			}
			if (found) {
				itemSiteDomainDisplayForm.setSelected("Y");
			}
			vector.add(itemSiteDomainDisplayForm);
		}
		ItemSiteDomainDisplayForm itemSiteDomains[] = new ItemSiteDomainDisplayForm[vector.size()];
		vector.copyInto(itemSiteDomains);
		form.setItemSiteDomains(itemSiteDomains);
	}
	
    public ActionMessages validateUploadImage(ItemMaintActionForm form, FormFile file) {
    	ActionMessages errors = new ActionMessages();
    	if (file.getFileName().length() == 0) {
       		errors.add("file", new ActionMessage("error.string.required"));
    	}
    	return errors;
    }
    
    /*******************************************************************************/
    
	public JSONEscapeObject createJsonItemsBundle(Item item, ItemMaintActionForm form) throws Exception {
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		Vector<JSONEscapeObject> items = new Vector<JSONEscapeObject>();
		for (Item child : item.getChildren()) {
			ItemLanguage itemLanguage = null;
			for (ItemLanguage language : child.getItemLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassDefaultId())) {
					itemLanguage = language;
					break;
				}
			}
			JSONEscapeObject itemObject = new JSONEscapeObject();
			itemObject.put("itemId", child.getItemId());
			itemObject.put("itemNum", child.getItemNum());
			itemObject.put("itemSkuCd", child.getItemSkuCd());
			itemObject.put("itemUpcCd", child.getItemUpcCd());
			itemObject.put("itemShortDesc", itemLanguage.getItemShortDesc());
			items.add(itemObject);
		}
		jsonResult.put("items", items);
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);

		return jsonResult;
	}
	
	public ActionForward getItemsBundle(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
	
		Item item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
		streamWebService(response, createJsonItemsBundle(item, form).toHtmlString());
		return null;
	}
	
	public ActionForward addItemBundle(ActionMapping mapping, 
									  ActionForm actionForm,
									  HttpServletRequest request,
									  HttpServletResponse response) 
								throws Throwable {
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		
		Item item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
		Item itemChild = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemChildId()));
		
	  	JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
	   	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Iterator<?> iterator = item.getChildren().iterator();
		while (iterator.hasNext()) {
			Item i = (Item) iterator.next();
			if (i.getItemId().equals(itemChild.getItemId())) {
				MessageResources resources = this.getResources(request);
				jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
				jsonResult.put("message", resources.getMessage("error.item.exist"));
				streamWebService(response, jsonResult.toHtmlString());
				return null;
			}
		}
		item.getChildren().add(itemChild);
		item.setRecUpdateBy(adminBean.getUser().getUserId());
		item.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(item);
		CategorySearchUtil.itemPriceSearchUpdate(item, site, adminBean);

		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		streamWebService(response, jsonResult.toHtmlString());
		return null;
	}	
	
	public ActionForward removeItemsBundle(ActionMapping mapping, 
										   ActionForm actionForm,
										   HttpServletRequest request,
										   HttpServletResponse response) 
										throws Throwable {
		
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
	
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
	   	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Item item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
	   	String itemChildIds[] = form.getItemChildIds();
	   	if (itemChildIds != null) {
		   	for (int i = 0; i < itemChildIds.length; i++) {
				Item itemChild = ItemDAO.load(site.getSiteId(), Format.getLong(itemChildIds[i]));
				item.getChildren().remove(itemChild);
		   	}
			item.setRecUpdateBy(adminBean.getUser().getUserId());
			item.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			em.persist(item);
			CategorySearchUtil.itemPriceSearchUpdate(item, site, adminBean);
	   	}

		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		streamWebService(response, jsonResult.toHtmlString());

		return null;
	}

	/*******************************************************************************/
    
	public JSONEscapeObject createJsonItemsRelated(Item item, ItemMaintActionForm form) throws Exception {
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		Iterator<?> iterator = item.getItemsRelated().iterator();
		Vector<JSONEscapeObject> items = new Vector<JSONEscapeObject>();
		while (iterator.hasNext()) {
			Item itemRelated = (Item) iterator.next();
			ItemLanguage itemLanguage = null;
			for (ItemLanguage language : itemRelated.getItemLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassDefaultId())) {
					itemLanguage = language;
					break;
				}
			}			
			JSONEscapeObject itemObject = new JSONEscapeObject();
			itemObject.put("itemId", itemRelated.getItemId());
			itemObject.put("itemNum", itemRelated.getItemNum());
			itemObject.put("itemUpcCd", itemRelated.getItemUpcCd());
			itemObject.put("itemShortDesc", itemLanguage.getItemShortDesc());
			items.add(itemObject);
		}
		jsonResult.put("items", items);
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);

		return jsonResult;
	}
	
	public ActionForward getItemsRelated(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
	
		Item item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
		streamWebService(response, createJsonItemsRelated(item, form).toHtmlString());
		return null;
	}
	
	public ActionForward addItemRelated(ActionMapping mapping, 
									  ActionForm actionForm,
									  HttpServletRequest request,
									  HttpServletResponse response) 
								throws Throwable {
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		
		Item item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
		Item itemRelated = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemRelatedId()));
		
	  	JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
	   	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Iterator<?> iterator = item.getItemsRelated().iterator();
		while (iterator.hasNext()) {
			Item i = (Item) iterator.next();
			if (i.getItemId().equals(itemRelated.getItemId())) {
				MessageResources resources = this.getResources(request);
				jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
				jsonResult.put("message", resources.getMessage("error.item.exist"));
				streamWebService(response, jsonResult.toHtmlString());
				return null;
			}
		}
		item.getItemsRelated().add(itemRelated);
		item.setRecUpdateBy(adminBean.getUser().getUserId());
		item.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(item);

		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		streamWebService(response, jsonResult.toHtmlString());
		return null;
	}	
	
	public ActionForward removeItemsRelated(ActionMapping mapping, 
										   ActionForm actionForm,
										   HttpServletRequest request,
										   HttpServletResponse response) 
										throws Throwable {
		
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
	
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
	   	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Item item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
	   	String itemRelatedIds[] = form.getItemRelatedIds();
	   	if (itemRelatedIds != null) {
		   	for (int i = 0; i < itemRelatedIds.length; i++) {
				Item itemRelated = ItemDAO.load(site.getSiteId(), Format.getLong(itemRelatedIds[i]));
				item.getItemsRelated().remove(itemRelated);
		   	}
			item.setRecUpdateBy(adminBean.getUser().getUserId());
			item.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			em.persist(item);
	   	}

		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		streamWebService(response, jsonResult.toHtmlString());

		return null;
	}

    /*******************************************************************************/
	
	public JSONEscapeObject createJsonItemsUpSell(Item item, ItemMaintActionForm form) throws Exception {
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		Iterator<?> iterator = item.getItemsUpSell().iterator();
		Vector<JSONEscapeObject> items = new Vector<JSONEscapeObject>();
		while (iterator.hasNext()) {
			Item itemRelated = (Item) iterator.next();
			ItemLanguage itemLanguage = null;
			for (ItemLanguage language : itemRelated.getItemLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassDefaultId())) {
					itemLanguage = language;
					break;
				}
			}			
			JSONEscapeObject itemObject = new JSONEscapeObject();
			itemObject.put("itemId", itemRelated.getItemId());
			itemObject.put("itemNum", itemRelated.getItemNum());
			itemObject.put("itemUpcCd", itemRelated.getItemUpcCd());
			itemObject.put("itemShortDesc", itemLanguage.getItemShortDesc());
			items.add(itemObject);
		}
		jsonResult.put("items", items);
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);

		return jsonResult;
	}
	
	public ActionForward getItemsUpSell(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
	
		Item item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
		streamWebService(response, createJsonItemsUpSell(item, form).toHtmlString());
		return null;
	}
	
	public ActionForward addItemUpSell(ActionMapping mapping, 
									  ActionForm actionForm,
									  HttpServletRequest request,
									  HttpServletResponse response) 
								throws Throwable {
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		
		Item item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
		Item itemUpSell = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemUpSellId()));
		
	  	JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
	   	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Iterator<?> iterator = item.getItemsUpSell().iterator();
		while (iterator.hasNext()) {
			Item i = (Item) iterator.next();
			if (i.getItemId().equals(itemUpSell.getItemId())) {
				MessageResources resources = this.getResources(request);
				jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
				jsonResult.put("message", resources.getMessage("error.item.exist"));
				streamWebService(response, jsonResult.toHtmlString());
				return null;
			}
		}
		item.getItemsUpSell().add(itemUpSell);
		item.setRecUpdateBy(adminBean.getUser().getUserId());
		item.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(item);

		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		streamWebService(response, jsonResult.toHtmlString());
		return null;
	}	
	
	public ActionForward removeItemsUpSell(ActionMapping mapping, 
										   ActionForm actionForm,
										   HttpServletRequest request,
										   HttpServletResponse response) 
										throws Throwable {
		
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
	
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
	   	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Item item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
	   	String itemUpSellIds[] = form.getItemUpSellIds();
	   	if (itemUpSellIds != null) {
		   	for (int i = 0; i < itemUpSellIds.length; i++) {
				Item itemUpSell = ItemDAO.load(site.getSiteId(), Format.getLong(itemUpSellIds[i]));
				item.getItemsUpSell().remove(itemUpSell);
		   	}
			item.setRecUpdateBy(adminBean.getUser().getUserId());
			item.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			em.persist(item);
	   	}

		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		streamWebService(response, jsonResult.toHtmlString());

		return null;
	}
	
    /*******************************************************************************/
	
	public JSONEscapeObject createJsonItemsCrossSell(Item item, ItemMaintActionForm form) throws Exception {
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		Iterator<?> iterator = item.getItemsCrossSell().iterator();
		Vector<JSONEscapeObject> items = new Vector<JSONEscapeObject>();
		while (iterator.hasNext()) {
			Item itemRelated = (Item) iterator.next();
			ItemLanguage itemLanguage = null;
			for (ItemLanguage language : itemRelated.getItemLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassDefaultId())) {
					itemLanguage = language;
					break;
				}
			}
			JSONEscapeObject itemObject = new JSONEscapeObject();
			itemObject.put("itemId", itemRelated.getItemId());
			itemObject.put("itemNum", itemRelated.getItemNum());
			itemObject.put("itemUpcCd", itemRelated.getItemUpcCd());
			itemObject.put("itemShortDesc", itemLanguage.getItemShortDesc());
			items.add(itemObject);
		}
		jsonResult.put("items", items);
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);

		return jsonResult;
	}
	
	public ActionForward getItemsCrossSell(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
	
		Item item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
		streamWebService(response, createJsonItemsCrossSell(item, form).toHtmlString());
		return null;
	}
	
	public ActionForward addItemCrossSell(ActionMapping mapping, 
									  ActionForm actionForm,
									  HttpServletRequest request,
									  HttpServletResponse response) 
								throws Throwable {
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		
		Item item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
		Item itemCrossSell = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemCrossSellId()));
		
	  	JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
	   	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Iterator<?> iterator = item.getItemsCrossSell().iterator();
		while (iterator.hasNext()) {
			Item i = (Item) iterator.next();
			if (i.getItemId().equals(itemCrossSell.getItemId())) {
				MessageResources resources = this.getResources(request);
				jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
				jsonResult.put("message", resources.getMessage("error.item.exist"));
				streamWebService(response, jsonResult.toHtmlString());
				return null;
			}
		}
		item.getItemsCrossSell().add(itemCrossSell);
		item.setRecUpdateBy(adminBean.getUser().getUserId());
		item.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(item);

		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		streamWebService(response, jsonResult.toHtmlString());
		return null;
	}	
	
	public ActionForward removeItemsCrossSell(ActionMapping mapping, 
										   ActionForm actionForm,
										   HttpServletRequest request,
										   HttpServletResponse response) 
										throws Throwable {
		
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
	
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
	   	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Item item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
	   	String itemCrossSellIds[] = form.getItemCrossSellIds();
	   	if (itemCrossSellIds != null) {
		   	for (int i = 0; i < itemCrossSellIds.length; i++) {
				Item itemCrossSell = ItemDAO.load(site.getSiteId(), Format.getLong(itemCrossSellIds[i]));
				item.getItemsCrossSell().remove(itemCrossSell);
		   	}
			item.setRecUpdateBy(adminBean.getUser().getUserId());
			item.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			em.persist(item);
	   	}

		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		streamWebService(response, jsonResult.toHtmlString());

		return null;
	}
	
    /*******************************************************************************/
	
	public JSONEscapeObject createJsonItemSkus(Item item) throws Exception {
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		Vector<JSONEscapeObject> items = new Vector<JSONEscapeObject>();
		for (Item itemChild : item.getItemSkus()) {
			JSONEscapeObject itemObject = new JSONEscapeObject();
			itemObject.put("itemId", itemChild.getItemId());
			itemObject.put("itemSkuCd", itemChild.getItemSkuCd());
			items.add(itemObject);
		}
		jsonResult.put("items", items);
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);

		return jsonResult;
	}
	
	public ActionForward getItemSkus(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
	
		Item item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
		String jsonString = createJsonItemSkus(item).toHtmlString();
		streamWebService(response, jsonString);
		return null;
	}
	
	public ActionForward generateItemSkus(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
       	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		MessageResources resources = this.getResources(request);
		ItemMaintActionForm form = (ItemMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		Item item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
    	String sql = "from  Item " +
					 "where siteId = :siteId " + 
					 "and   itemNum = :itemNum " +
					 "and   itemId != :itemId";

    	Query query = em.createQuery(sql);
    	query.setParameter("siteId", site.getSiteId());
    	query.setParameter("itemNum",  form.getItemNum());
    	query.setParameter("itemId", Format.getLong(form.getItemId()));
		Iterator<?> iterator = query.getResultList().iterator();
		if (iterator.hasNext()) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			jsonResult.put("message", resources.getMessage("error.item.itemNum.duplicate"));
			streamWebService(response, jsonResult.toHtmlString());
			return null;
		}
		
		CustomAttributeGroup customAttributeGroup = item.getCustomAttributeGroup();
		if (customAttributeGroup != null) {
			Vector<AttributeDetailOption[]> attributeVector = new Vector<AttributeDetailOption[]>();
			for (CustomAttributeDetail customAttributeDetail : customAttributeGroup.getCustomAttributeDetails()) {
				CustomAttribute customAttribute = customAttributeDetail.getCustomAttribute();
				if (customAttribute.getCustomAttributeOptions().size() == 0) {
					continue;
				}
				if (customAttribute.getCustomAttribTypeCode() != Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP) {
					continue;
				}
				Vector<AttributeDetailOption> optionVector = new Vector<AttributeDetailOption>();
				for (CustomAttributeOption customAttributeOption : customAttribute.getCustomAttributeOptions()) {
					AttributeDetailOption attributeDetailOption = new AttributeDetailOption();
					attributeDetailOption.setCustomAttributeOption(customAttributeOption);
					attributeDetailOption.setCustomAttributeDetail(customAttributeDetail);
					optionVector.add(attributeDetailOption);
				}
				AttributeDetailOption options[] = new AttributeDetailOption[optionVector.size()];
				optionVector.copyInto(options);
				attributeVector.add(options);
			}
			AttributeDetailOption[] attributes[] = new AttributeDetailOption[attributeVector.size()][];
			attributeVector.copyInto(attributes);

			if (attributes.length > 0) {
				Vector<Item> items = new Vector<Item>();
				generateSkus(items, attributes, item.getItemNum(), 0, new Vector<Object>());
				
				iterator = items.iterator();
				while (iterator.hasNext()) {
					Item itemSku = (Item) iterator.next();
					cloneSku(itemSku, item, adminBean);
				}
			}
		}

		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("recUpdateBy", item.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(item.getRecUpdateDatetime()));
		
		form.setStream(true);
		form.setStreamData(jsonResult.toHtmlString());
		return null;
	}
	
	private void generateSkus(Vector<Item> items, AttributeDetailOption[] attributes[], String itemSkuCd, int index, Vector<?> itemOptions) throws Exception {
       	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		if (index >= attributes.length) {
			Item item = new Item();
			item.setItemSkuCd(itemSkuCd);
			Iterator<?> iterator = itemOptions.iterator();
			while (iterator.hasNext()) {
				AttributeDetailOption option = (AttributeDetailOption) iterator.next();
				ItemAttributeDetail itemAttributeDetail = new ItemAttributeDetail();
				itemAttributeDetail.setRecUpdateBy(option.getCustomAttributeDetail().getRecUpdateBy());
				itemAttributeDetail.setRecUpdateDatetime(new Date());
				itemAttributeDetail.setRecCreateBy(option.getCustomAttributeDetail().getRecUpdateBy());
				itemAttributeDetail.setRecCreateDatetime(new Date());
				itemAttributeDetail.setCustomAttributeDetail(option.getCustomAttributeDetail());
				itemAttributeDetail.setCustomAttributeOption(option.getCustomAttributeOption());
				itemAttributeDetail.setItem(item);
				item.getItemAttributeDetails().add(itemAttributeDetail);
				em.persist(itemAttributeDetail);
			}
			items.add(item);
			return;
		}
		
		AttributeDetailOption attributeDetailOptions[] = attributes[index];
		for (int i = 0; i < attributeDetailOptions.length; i++) {
			Vector<AttributeDetailOption> nextItemOptions = new Vector<AttributeDetailOption>();
			Iterator<?> iterator = itemOptions.iterator();
			while (iterator.hasNext()) {
				AttributeDetailOption attributeDetailOption = (AttributeDetailOption) iterator.next();
				nextItemOptions.add(attributeDetailOption);
			}
			nextItemOptions.add(attributeDetailOptions[i]);
			generateSkus(items, attributes, itemSkuCd + "-" + attributeDetailOptions[i].getCustomAttributeOption().getCustomAttribSkuCode(), index + 1, nextItemOptions);
		}
		return;
	}
	
	private void cloneSku(Item item, Item source, AdminBean adminBean) throws Exception {
       	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		User user = adminBean.getUser();
	
		item.setItemNaturalKey(Utility.encode(item.getItemSkuCd()));
		item.setSeqNum(source.getSeqNum());
		item.setItemNum(source.getItemNum());
		item.setItemUpcCd(source.getItemUpcCd());
		item.setItemTypeCd(Constants.ITEM_TYPE_SKU);
		item.setItemSellable(source.getItemSellable());
		item.setItemCost(source.getItemCost());
		item.setItemHitCounter(0);
		item.setItemRating(Float.valueOf(0));
		item.setItemRatingCount(0);
		item.setItemQty(0);
		item.setItemBookedQty(0);
		item.setItemPublishOn(source.getItemPublishOn());
		item.setItemExpireOn(source.getItemExpireOn());
		item.setPublished(source.getPublished());
		item.setRecUpdateBy(user.getUserId());
		item.setRecUpdateDatetime(new Date());
		item.setRecCreateBy(user.getUserId());
		item.setRecCreateDatetime(new Date());
		item.setSite(source.getSite());
		item.setShippingType(source.getShippingType());
		item.setProductClass(source.getProductClass());
		item.setUser(source.getUser());
		item.setCustomAttributeGroup(source.getCustomAttributeGroup());
		item.setItemSkuParent(source);
		source.getItemSkus().add(item);
		
		for (Category category : source.getCategories()) {
			item.getCategories().add(category);
		}
		
		for (ItemLanguage sourceLanguage : source.getItemLanguages()) {
			ItemLanguage itemLanguage = new ItemLanguage();
			itemLanguage.setItemShortDesc(sourceLanguage.getItemShortDesc());
			itemLanguage.setItemDesc(sourceLanguage.getItemDesc());
			itemLanguage.setPageTitle(sourceLanguage.getPageTitle());
			itemLanguage.setMetaKeywords(sourceLanguage.getMetaKeywords());
			itemLanguage.setMetaDescription(sourceLanguage.getMetaDescription());
			itemLanguage.setItemImageOverride(sourceLanguage.getItemImageOverride());
			itemLanguage.setRecUpdateBy(user.getUserId());
			itemLanguage.setRecUpdateDatetime(new Date());
			itemLanguage.setRecCreateBy(user.getUserId());
			itemLanguage.setRecCreateDatetime(new Date());
			itemLanguage.setSiteProfileClass(sourceLanguage.getSiteProfileClass());
			item.getItemLanguages().add(itemLanguage);
			em.persist(itemLanguage);
			
			if (sourceLanguage.getSiteProfileClass().getSiteProfileClassId().equals(adminBean.getSite().getSiteProfileClassDefault().getSiteProfileClassId())) {
				item.setItemLanguage(itemLanguage);
			}
			
			if (sourceLanguage.getImage() != null) {
				ItemImage sourceImage = sourceLanguage.getImage();
				ItemImage itemImage = new ItemImage();
				itemImage.setSeqNum(sourceImage.getSeqNum());
				itemImage.setContentType(sourceImage.getContentType());
				itemImage.setImageName(sourceImage.getImageName());
				itemImage.setImageValue(sourceImage.getImageValue());
				itemImage.setImageHeight(sourceImage.getImageHeight());
				itemImage.setImageWidth(sourceImage.getImageWidth());
				itemImage.setRecUpdateBy(user.getUserId());
				itemImage.setRecUpdateDatetime(new Date());
				itemImage.setRecCreateBy(user.getUserId());
				itemImage.setRecCreateDatetime(new Date());
				em.persist(itemImage);
				itemLanguage.setImage(itemImage);
			}
			
			for (ItemImage sourceImage : sourceLanguage.getImages()) {
				ItemImage itemImage = new ItemImage();
				itemImage.setSeqNum(sourceImage.getSeqNum());
				itemImage.setContentType(sourceImage.getContentType());
				itemImage.setImageName(sourceImage.getImageName());
				itemImage.setImageValue(sourceImage.getImageValue());
				itemImage.setImageHeight(sourceImage.getImageHeight());
				itemImage.setImageWidth(sourceImage.getImageWidth());
				itemImage.setRecUpdateBy(user.getUserId());
				itemImage.setRecUpdateDatetime(new Date());
				itemImage.setRecCreateBy(user.getUserId());
				itemImage.setRecCreateDatetime(new Date());
				itemLanguage.getImages().add(itemImage);
				em.persist(itemImage);
			}
		}
		
		for (ItemPriceCurrency sourcePriceCurrency : source.getItemPriceCurrencies()) {
			ItemPriceCurrency itemPriceCurrency = new ItemPriceCurrency();
			itemPriceCurrency.setItemPrice(sourcePriceCurrency.getItemPrice());
			itemPriceCurrency.setItemPriceTypeCode(sourcePriceCurrency.getItemPriceTypeCode());
			itemPriceCurrency.setItemPriceExpireOn(sourcePriceCurrency.getItemPriceExpireOn());
			itemPriceCurrency.setItemPricePublishOn(sourcePriceCurrency.getItemPricePublishOn());
			itemPriceCurrency.setRecUpdateBy(user.getUserId());
			itemPriceCurrency.setRecUpdateDatetime(new Date());
			itemPriceCurrency.setRecCreateBy(user.getUserId());
			itemPriceCurrency.setRecCreateDatetime(new Date());
			itemPriceCurrency.setSiteCurrencyClass(sourcePriceCurrency.getSiteCurrencyClass());
			itemPriceCurrency.setItem(item);
			item.getItemPriceCurrencies().add(itemPriceCurrency);
			if (sourcePriceCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(adminBean.getSite().getSiteCurrencyClassDefault().getSiteCurrencyClassId())) {
				if (sourcePriceCurrency.getItemPriceTypeCode() == Constants.ITEM_PRICE_TYPE_CODE_REGULAR) {
					item.setItemPrice(itemPriceCurrency);
				}
				else {
					item.setItemSpecPrice(itemPriceCurrency);
				}
			}
			em.persist(itemPriceCurrency);
		}
		
		for (ItemTierPrice sourceTierPrice : source.getItemTierPrices()) {
			ItemTierPrice itemTierPrice = new ItemTierPrice();
			itemTierPrice.setItemTierQty(sourceTierPrice.getItemTierQty());
			itemTierPrice.setItemTierPricePublishOn(sourceTierPrice.getItemTierPricePublishOn());
			itemTierPrice.setItemTierPriceExpireOn(sourceTierPrice.getItemTierPriceExpireOn());
			itemTierPrice.setRecUpdateBy(user.getUserId());
			itemTierPrice.setRecUpdateDatetime(new Date());
			itemTierPrice.setRecCreateBy(user.getUserId());
			itemTierPrice.setRecCreateDatetime(new Date());
			itemTierPrice.setCustomerClass(sourceTierPrice.getCustomerClass());
			item.getItemTierPrices().add(itemTierPrice);
			
			for (ItemTierPriceCurrency sourceTierPriceCurrency : sourceTierPrice.getItemTierPriceCurrencies()) {
				ItemTierPriceCurrency itemTierPriceCurrency = new ItemTierPriceCurrency();
				itemTierPriceCurrency.setItemPrice(sourceTierPriceCurrency.getItemPrice());
				itemTierPriceCurrency.setRecUpdateBy(user.getUserId());
				itemTierPriceCurrency.setRecUpdateDatetime(new Date());
				itemTierPriceCurrency.setRecCreateBy(user.getUserId());
				itemTierPriceCurrency.setRecCreateDatetime(new Date());
				itemTierPriceCurrency.setSiteCurrencyClass(sourceTierPriceCurrency.getSiteCurrencyClass());
				itemTierPrice.getItemTierPriceCurrencies().add(itemTierPriceCurrency);
				em.persist(itemTierPriceCurrency);
				if (sourceTierPriceCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(adminBean.getSite().getSiteCurrencyClassDefault().getSiteCurrencyClassId())) {
					itemTierPrice.setItemTierPriceCurrency(itemTierPriceCurrency);
				}
			}
			em.persist(itemTierPrice);
		}
		em.persist(item);
		
		for (Item sourceCrossSell : source.getItemsCrossSell()) {
			item.getItemsCrossSell().add(sourceCrossSell);
		}
		for (Item sourceRelated : source.getItemsRelated()) {
			item.getItemsRelated().add(sourceRelated);
		}
		for (Item sourceUpSell: source.getItemsUpSell()) {
			item.getItemsUpSell().add(sourceUpSell);
		}
		
		for (ItemAttributeDetail sourceAttributeDetail : source.getItemAttributeDetails()) {
			if  (sourceAttributeDetail.getCustomAttributeDetail().getCustomAttribute().getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP) {
				continue;
			}
			ItemAttributeDetail itemAttributeDetail = new ItemAttributeDetail();
			itemAttributeDetail.setRecUpdateBy(user.getUserId());
			itemAttributeDetail.setRecUpdateDatetime(new Date());
			itemAttributeDetail.setRecCreateBy(user.getUserId());
			itemAttributeDetail.setRecCreateDatetime(new Date());
			itemAttributeDetail.setCustomAttributeDetail(sourceAttributeDetail.getCustomAttributeDetail());
			itemAttributeDetail.setCustomAttributeOption(sourceAttributeDetail.getCustomAttributeOption());
			itemAttributeDetail.setItem(item);
			item.getItemAttributeDetails().add(itemAttributeDetail);

			for (ItemAttributeDetailLanguage sourceAttributeDetailLanguage : sourceAttributeDetail.getItemAttributeDetailLanguages()) {
				ItemAttributeDetailLanguage itemAttributeDetailLanguage = new ItemAttributeDetailLanguage();
				itemAttributeDetailLanguage.setItemAttribDetailValue(sourceAttributeDetailLanguage.getItemAttribDetailValue());
				itemAttributeDetailLanguage.setRecUpdateBy(user.getUserId());
				itemAttributeDetailLanguage.setRecUpdateDatetime(new Date());
				itemAttributeDetailLanguage.setRecCreateBy(user.getUserId());
				itemAttributeDetailLanguage.setRecCreateDatetime(new Date());
				itemAttributeDetailLanguage.setSiteProfileClass(sourceAttributeDetailLanguage.getSiteProfileClass());
				itemAttributeDetail.getItemAttributeDetailLanguages().add(itemAttributeDetailLanguage);
				em.persist(itemAttributeDetailLanguage);
				
				if (sourceAttributeDetail.getItemAttributeDetailLanguage().getItemAttribDetailLangId().equals(sourceAttributeDetailLanguage.getItemAttribDetailLangId())) {
					itemAttributeDetail.setItemAttributeDetailLanguage(itemAttributeDetailLanguage);
				}
			}
			em.persist(itemAttributeDetail);
		}
		em.persist(item);
	}
	
    /*******************************************************************************/

    public ActionMessages validate(HttpServletRequest request, ItemMaintActionForm form, String siteId, boolean insertMode, Item item) throws Exception { 
       	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		MessageResources resources = this.getResources(request);
    	ActionMessages errors = new ActionMessages();
    	if (Format.isNullOrEmpty(form.getItemPrice())) {
    		errors.add("itemPrice", new ActionMessage("error.string.required"));
    	}
    	else {
	    	if (!Format.isFloat(form.getItemPrice())) {
	    		errors.add("itemPrice", new ActionMessage("error.float.invalid"));
	    	}
    	}

		if (!Format.isNullOrEmpty(form.getItemSpecPrice())) {
	    	if (!Format.isFloat(form.getItemSpecPrice())) {
	    		errors.add("itemSpecPrice", new ActionMessage("error.float.invalid"));
	    	}
    	}
		if (!Format.isNullOrEmpty(form.getItemSpecPrice())) {
	    	if (Format.isNullOrEmpty(form.getItemSpecPublishOn())) {
	    		errors.add("itemSpecPublishOn", new ActionMessage("error.string.required"));
	    	}
	    	else {
		    	if (!Format.isDate(form.getItemSpecPublishOn())) {
		       		errors.add("itemSpecPublishOn", new ActionMessage("error.date.invalid"));
	 	    	}
	    	}
		}
		if (!Format.isNullOrEmpty(form.getItemSpecPrice())) {
	    	if (Format.isNullOrEmpty(form.getItemSpecExpireOn())) {
	    		errors.add("itemSpecExpireOn", new ActionMessage("error.string.required"));
	    	}
	    	else {
		    	if (!Format.isDate(form.getItemSpecExpireOn())) {
		       		errors.add("itemSpecExpireOn", new ActionMessage("error.date.invalid"));
		    	}
	    	}
		}
    	
    	if (!Format.isDate(form.getItemPublishOn())) {
       		errors.add("itemPublishOn", new ActionMessage("error.date.invalid"));
    	}
    	if (!Format.isDate(form.getItemExpireOn())) {
       		errors.add("itemExpireOn", new ActionMessage("error.date.invalid"));
    	}
    	if (!Format.isNullOrEmpty(form.getItemSpecExpireOn())) {
	    	if (!Format.isFloat(form.getItemSpecExpireOn())) {
	    		errors.add("itemSpecExpireOn", new ActionMessage("error.date.invalid"));
	    	}
    	}
/*
    	String sql = "from  Item " +
    				 "where siteId = :siteId " + 
    				 "and   itemNum = :itemNum ";
    	if (!insertMode) {
    		sql += "and  itemId != :itemId";
    	}
    	Query query = em.createQuery(sql);
    	query.setParameter("siteId", siteId);
    	query.setParameter("itemNum",  form.getItemNum());
    	if (!insertMode) {
    		query.setParameter("itemId", Format.getLong(form.getItemId()));
    	}
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		errors.add("itemNum", new ActionMessage("error.item.itemNum.duplicate"));
    	}
*/
    	if (Format.isNullOrEmpty(form.getItemSkuCd())) {
    		errors.add("itemSkuCd", new ActionMessage("error.string.required"));
    	}
    	else {
	        String sql = "from  Item " +
				  "where siteId = :siteId " + 
				  "and   itemSkuCd = :itemSkuCd ";
			if (!insertMode) {
				sql += "and  itemId != :itemId";
			}
			Query query = em.createQuery(sql);
			query.setParameter("siteId", siteId);
			query.setParameter("itemSkuCd",  form.getItemSkuCd());
			if (!insertMode) {
				query.setParameter("itemId", Format.getLong(form.getItemId()));
			}
			Iterator<?> iterator = query.getResultList().iterator();
			if (iterator.hasNext()) {
				errors.add("itemSkuCd", new ActionMessage("error.item.itemSkuCd.duplicate"));
			}
    	}
    	
    	ItemTierPriceDisplayForm prices[] = form.getItemTierPrices();
    	for (int i = 0; i < prices.length; i++) {
    		ItemTierPriceDisplayForm price = prices[i];
    		if (Format.isNullOrEmpty(price.getItemTierQty())) {
    			price.setItemTierQtyError(resources.getMessage("error.int.invalid"));
    		}
    		else {
    	    	if (!Format.isInt(price.getItemTierQty())) {
    	       		errors.add("itemItemTierPriceQty", new ActionMessage("error.int.invalid"));
    	    	}	
    		}
    		if (Format.isNullOrEmpty(price.getItemTierPrice())) {
    			price.setItemTierPriceError(resources.getMessage("error.float.invalid"));
    		}
    		else {
    	    	if (!Format.isInt(price.getItemTierPrice())) {
    	       		errors.add("itemItemTierPricePrice", new ActionMessage("error.float.invalid"));
    	    	}	
    		}
        	if (!Format.isNullOrEmpty(price.getItemTierPricePublishOn())) {
    	    	if (!Format.isDate(price.getItemTierPricePublishOn())) {
    	       		errors.add("itemTierPricePublishOn", new ActionMessage("error.date.invalid"));
    	    	}
        	}
        	if (!Format.isNullOrEmpty(price.getItemTierPriceExpireOn())) {
    	    	if (!Format.isDate(price.getItemTierPriceExpireOn())) {
    	       		errors.add("itemTierPriceExpireOn", new ActionMessage("error.date.invalid"));
    	    	}
        	}
    	}
    	
    	if (!form.isSiteCurrencyClassDefault()) {
        	if (Format.isNullOrEmpty(form.getItemPriceCurr())) {
        		errors.add("itemPrice", new ActionMessage("error.string.required"));
        	}
        	else {
    	    	if (!Format.isFloat(form.getItemPriceCurr())) {
    	    		errors.add("itemPrice", new ActionMessage("error.float.invalid"));
    	    	}
        	}
    	}
    	
    	if (form.getItemTypeCd().equals(Constants.ITEM_TYPE_TEMPLATE)) {
    		if (Format.isNullOrEmpty(form.getCustomAttribGroupId())) {
    			errors.add("customAttribGroupId", new ActionMessage("error.string.required"));
    		}
    	}
    	
    	if (form.getItemTypeCd().equals(Constants.ITEM_TYPE_SKU)) {
    		String inputSkuAttributeKey = getInputSkuAttributeKey(form);
    		for (Item child : item.getItemSkuParent().getItemSkus()) {
    			if (child.getItemId() == null) {
    				continue;
    			}
    			if (child.getItemId().toString().equals(form.getItemId())) {
    				continue;
    			}
    			String skuAttributeKey = ItemDAO.getSkuAttributeKey(child);
    			if (inputSkuAttributeKey.equals(skuAttributeKey)) {
    				errors.add("inputSkuAttrbute", new ActionMessage("error.item.skuAttribute.duplicate"));
    				break;
    			}
    		}
    	}
    	
    	if (insertMode) {
	    	if (form.getItemTypeCd().equals(Constants.ITEM_TYPE_TEMPLATE)) {
	    		if (Format.isNullOrEmpty(form.getCustomAttribGroupId())) {
	    			errors.add("customAttribGroupId", new ActionMessage("error.string.required"));
	    		}
	    	}
    	}
    	
    	return errors;
    }
    
    public String getInputSkuAttributeKey(ItemMaintActionForm form) throws JSONException {
    	JSONEscapeObject JSONEscapeObject = new JSONEscapeObject();
    	Vector<JSONEscapeObject> vector = new Vector<JSONEscapeObject>();
    	
    	ItemAttributeDetailDisplayForm displayForms[] = form.getItemAttributeDetails();
    	/* Ensure it is ordered by itemAttributeDetailId */
    	Vector<Long> idList = new Vector<Long>();
    	for (ItemAttributeDetailDisplayForm displayForm : displayForms) {
    		if (!displayForm.getCustomAttribTypeCode().equals(String.valueOf(Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP))) {
    			continue;
    		}
    		idList.add(Format.getLong(displayForm.getItemAttribDetailId()));
    	}
    	Long ids[] = new Long[idList.size()];
    	idList.copyInto(ids);

    	Arrays.sort(ids);
    	
    	for (long id : ids) {
    		for (ItemAttributeDetailDisplayForm displayForm : displayForms) {
    			long itemAttribDetailId = Format.getLong(displayForm.getItemAttribDetailId());
    			if (id == itemAttribDetailId) {
    	    		JSONEscapeObject attribute = new JSONEscapeObject();
    	    		attribute.put("customAttribDetailId", displayForm.getCustomAttribDetailId());
    	    		attribute.put("customAttribOptionId", displayForm.getCustomAttribOptionId());
    	    		vector.add(attribute);
    				break;
    			}
    		}
    	}
    	JSONEscapeObject.put("itemAttributeDetails", vector);
    	return JSONEscapeObject.toHtmlString();
    }
    
	private class AttributeDetailOption {
		CustomAttributeOption customAttributeOption;
		CustomAttributeDetail customAttributeDetail;
		public CustomAttributeOption getCustomAttributeOption() {
			return customAttributeOption;
		}
		public void setCustomAttributeOption(CustomAttributeOption customAttributeOption) {
			this.customAttributeOption = customAttributeOption;
		}
		public CustomAttributeDetail getCustomAttributeDetail() {
			return customAttributeDetail;
		}
		public void setCustomAttributeDetail(CustomAttributeDetail customAttributeDetail) {
			this.customAttributeDetail = customAttributeDetail;
		}
	}

    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("save", "save");
        map.put("cancel", "cancel");
        map.put("edit", "edit");
        map.put("create", "create");
        map.put("remove", "remove");
        map.put("resetCounter", "resetCounter");
        map.put("removeMenus", "removeMenus");
        map.put("addMenus", "addMenus");
        map.put("removeCategories", "removeCategories");
        map.put("addCategories", "addCategories");
        map.put("uploadImage", "uploadImage");
        map.put("removeImages", "removeImages");
        map.put("showImages", "showImages");
        map.put("defaultImage", "defaultImage");
        map.put("overrideImages", "overrideImages");
        map.put("adjustQty", "adjustQty");
        map.put("adjustBookedQty", "adjustBookedQty");
        map.put("comments", "comments");
        map.put("language", "language");
        map.put("getItemTierPrice", "getItemTierPrice");
        map.put("addTierPrice", "addTierPrice");
        map.put("removeTierPrice", "removeTierPrice");
        map.put("saveTierPrice", "saveTierPrice");
        map.put("translate", "translate");
        map.put("getItemsRelated", "getItemsRelated");
        map.put("removeItemsRelated", "removeItemsRelated");
        map.put("addItemRelated", "addItemRelated");
        map.put("getItemsUpSell", "getItemsUpSell");
        map.put("removeItemsUpSell", "removeItemsUpSell");
        map.put("addItemUpSell", "addItemUpSell");
        map.put("getItemsCrossSell", "getItemsCrossSell");
        map.put("removeItemsCrossSell", "removeItemsCrossSell");
        map.put("addItemCrossSell", "addItemCrossSell");
        map.put("getCustomAttributes", "getCustomAttributes");
        map.put("getItemSkus", "getItemSkus");
        map.put("generateItemSkus", "generateItemSkus");
        map.put("getItemsBundle", "getItemsBundle");
        map.put("removeItemsBundle", "removeItemsBundle");
        map.put("addItemBundle", "addItemBundle");
        map.put("showMenus", "showMenus");
        map.put("showCategories", "showCategories");
        return map;
    }
}
