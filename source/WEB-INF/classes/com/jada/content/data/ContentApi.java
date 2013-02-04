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

package com.jada.content.data;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.api.DataApi;
import com.jada.content.ContentBean;
import com.jada.content.ContentFilterBean;
import com.jada.content.ContentLookupDispatchAction;
import com.jada.content.ContentSessionBean;
import com.jada.content.Formatter;
import com.jada.order.cart.ShoppingCart;
import com.jada.order.cart.ShoppingCartItem;
import com.jada.content.syndication.SyndReader;
import com.jada.content.syndication.SyndicationEntryInfo;
import com.jada.content.syndication.SyndicationList;
import com.jada.dao.CacheDAO;
import com.jada.dao.ContentDAO;
import com.jada.dao.CustomAttributeDAO;
import com.jada.dao.CustomAttributeOptionDAO;
import com.jada.dao.ItemDAO;
import com.jada.dao.ItemTierPriceDAO;
import com.jada.dao.MenuDAO;
import com.jada.dao.CategoryDAO;
import com.jada.dao.SiteDomainDAO;
import com.jada.inventory.InventoryEngine;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Category;
import com.jada.jpa.entity.CategoryLanguage;
import com.jada.jpa.entity.Comment;
import com.jada.jpa.entity.ContactUs;
import com.jada.jpa.entity.ContactUsLanguage;
import com.jada.jpa.entity.Content;
import com.jada.jpa.entity.ContentImage;
import com.jada.jpa.entity.ContentLanguage;
import com.jada.jpa.entity.CustomAttribute;
import com.jada.jpa.entity.CustomAttributeLanguage;
import com.jada.jpa.entity.CustomAttributeOption;
import com.jada.jpa.entity.CustomAttributeOptionCurrency;
import com.jada.jpa.entity.CustomAttributeOptionLanguage;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.HomePage;
import com.jada.jpa.entity.HomePageDetail;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.ItemAttributeDetail;
import com.jada.jpa.entity.ItemAttributeDetailLanguage;
import com.jada.jpa.entity.ItemImage;
import com.jada.jpa.entity.ItemLanguage;
import com.jada.jpa.entity.ItemPriceCurrency;
import com.jada.jpa.entity.ItemTierPrice;
import com.jada.jpa.entity.ItemTierPriceCurrency;
import com.jada.jpa.entity.Menu;
import com.jada.jpa.entity.MenuLanguage;
import com.jada.jpa.entity.OrderAddress;
import com.jada.jpa.entity.OrderAttributeDetail;
import com.jada.jpa.entity.OrderDetailTax;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.OrderItemDetail;
import com.jada.jpa.entity.OrderOtherDetail;
import com.jada.jpa.entity.SiteCurrency;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.SiteDomainLanguage;
import com.jada.jpa.entity.SiteProfile;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.search.CompassContentLanguage;
import com.jada.search.CompassItemLanguage;
import com.jada.search.Indexer;
import com.jada.search.QueryResult;
import com.jada.system.ApplicationGlobal;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.NamedQuery;
import com.jada.util.Utility;
import com.jada.xml.site.SiteDomainParamBean;
import com.jada.order.cart.CurrencyConverter;
import com.jada.order.document.OrderEngine;

public class ContentApi {
	static int MAX_RESULT = 10;
	protected String imageUrlPrefix = null;
	protected String currencyCode = null;
	protected SiteDomain siteDomain = null;
	protected SiteDomainLanguage siteDomainLanguage = null;
	protected SiteDomainParamBean siteDomainParamBean = null;
	protected SiteProfile siteProfile = null;
	protected SiteCurrency siteCurrency = null;
	boolean siteProfileClassDefault;
	protected HttpServletRequest request = null;
	protected ContentBean contentBean = null;
	protected Locale locale = null;
	protected Formatter formatter = null;
	protected CurrencyConverter currencyConverter = null;
	protected Long custId = null;
	// customer is late initialized.
	protected Customer customer = null; 
	protected DataApi dataApi = null;
	
    Logger logger = Logger.getLogger(ContentApi.class);
    
    public ContentApi(SiteDomain siteDomain, SiteProfile siteProfile, SiteCurrency siteCurrency) throws Exception {
    	this.siteDomain = siteDomain;
    	this.siteProfile = siteProfile;
    	this.siteCurrency = siteCurrency;
    	
		imageUrlPrefix = "/" + ApplicationGlobal.getContextPath() + "/services/ImageProvider.do";
		siteProfileClassDefault = contentBean.getContentSessionKey().isSiteProfileClassDefault();
		
		siteDomainLanguage = siteDomain.getSiteDomainLanguage();
		if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
			for (SiteDomainLanguage language : siteDomain.getSiteDomainLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfile.getSiteProfileClass().getSiteProfileClassId())) {
					siteDomainLanguage = language;
					break;
				}
			}
		}
		siteDomainParamBean = SiteDomainDAO.getSiteDomainParamBean(siteDomain.getSiteDomainLanguage(), siteDomainLanguage);
		formatter = new Formatter(siteProfile, siteCurrency);
		currencyConverter = new CurrencyConverter(siteCurrency);
		if (ContentLookupDispatchAction.isCustomerSession(request)) {
			custId = ContentLookupDispatchAction.getCustId(request);
		}
		dataApi = DataApi.getInstance();
    }
	
	public ContentApi(HttpServletRequest request) throws Exception {
		this.request = request;
		imageUrlPrefix = "/" + ApplicationGlobal.getContextPath() + "/services/ImageProvider.do";
		contentBean = ContentLookupDispatchAction.getContentBean(request);
		siteProfile = contentBean.getContentSessionBean().getSiteProfile();
		siteCurrency = contentBean.getContentSessionBean().getSiteCurrency();
		siteDomain = contentBean.getContentSessionBean().getSiteDomain();
		siteDomainLanguage = siteDomain.getSiteDomainLanguage();
		if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
			for (SiteDomainLanguage language : siteDomain.getSiteDomainLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
					siteDomainLanguage = language;
				}
			}
		}
		siteDomainParamBean = SiteDomainDAO.getSiteDomainParamBean(siteDomain.getSiteDomainLanguage(), siteDomainLanguage);
		formatter = new Formatter(contentBean.getContentSessionBean().getSiteProfile(),
								  contentBean.getContentSessionBean().getSiteCurrency());
		currencyConverter = new CurrencyConverter(contentBean.getContentSessionBean().getSiteCurrency());
		if (ContentLookupDispatchAction.isCustomerSession(request)) {
			custId = ContentLookupDispatchAction.getCustId(request);
		}
		dataApi = DataApi.getInstance();
	}
	
	public SiteInfo getSite() throws Exception {
		SiteInfo siteInfo = new SiteInfo();
		siteInfo.setSiteId(siteDomain.getSite().getSiteId());
		siteInfo.setSiteDomainPrefix(siteDomain.getSiteDomainPrefix());
		siteInfo.setSiteName(siteDomain.getSiteDomainLanguage().getSiteName());
		siteInfo.setContextPath(ApplicationGlobal.getContextPath());
		if (siteDomainParamBean.getTemplateFooter() == null) {
			siteInfo.setSiteFooter("");
		}
		else {
			siteInfo.setSiteFooter(siteDomainParamBean.getTemplateFooter());
		}
		siteInfo.setSingleCheckout(siteDomain.getSite().getSingleCheckout() == Constants.VALUE_YES);
		siteInfo.setLangName(siteProfile.getSiteProfileClass().getSiteProfileClassName());
	    String url = imageUrlPrefix + 
	    			 "?type=S" +
	    			 "&imageId=" + siteDomain.getSiteDomainLanguage().getSiteDomainLangId(); 
		siteInfo.setSiteLogoUrl(url);
		SiteDomain domain = siteDomain;
		String homeUrl = SiteDomainDAO.getPublicURLPrefix(domain) + "/" + 
						 ApplicationGlobal.getContextPath() + 
						 Constants.FRONTEND_URL_PREFIX +
						 "/" + siteDomain.getSiteDomainPrefix() +
						 "/" + siteProfile.getSiteProfileClass().getSiteProfileClassName() +
						 "/" + Constants.FRONTEND_URL_HOME;
		siteInfo.setHomeUrl(homeUrl);
		
        if (!siteProfileClassDefault) {
        	SiteProfileClass siteProfileClass = siteProfile.getSiteProfileClass();
        	for (SiteDomainLanguage language : siteDomain.getSiteDomainLanguages()) {
        		if (!language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClass.getSiteProfileClassId())) {
        			continue;
        		}
        		if (language.getSiteName() != null) {
        			siteInfo.setSiteName(language.getSiteName());
        		}
        		if (language.getSiteLogoValue() != null) {
        			url = imageUrlPrefix + "?type=S&imageId=" + language.getSiteDomainLangId();
        			siteInfo.setSiteLogoUrl(url);
        		}
        		break;
        	}
        }
		
		siteInfo.setSiteProfileClassName(siteProfile.getSiteProfileClass().getSiteProfileClassName());
		siteInfo.setSiteCurrencyClassName(siteCurrency.getSiteCurrencyClass().getSiteCurrencyClassName());
		
		char singleCheckout = siteDomain.getSite().getSingleCheckout();
		SiteDomain singleCheckoutDomain = siteDomain;
		if (singleCheckout == Constants.VALUE_YES) {
			singleCheckoutDomain = siteDomain.getSite().getSiteDomainDefault();
		}
		Vector<SiteLanguageInfo> languages = new Vector<SiteLanguageInfo>();
		for (SiteProfile siteProfile : singleCheckoutDomain.getSiteProfiles()) {
			if (siteProfile.getActive() != Constants.ACTIVE_YES) {
				continue;
			}
			SiteLanguageInfo languageInfo = new SiteLanguageInfo();
			languageInfo.setSiteProfileId(siteProfile.getSiteProfileId().toString());
			languageInfo.setSiteProfileClassName(siteProfile.getSiteProfileClass().getSiteProfileClassName());
			languageInfo.setSiteProfileClassNativeName(siteProfile.getSiteProfileClass().getSiteProfileClassNativeName());
			languages.add(languageInfo);
		}
		siteInfo.setLanguages(languages);
		
		Vector<SiteCurrencyInfo> currencies = new Vector<SiteCurrencyInfo>();
		for (SiteCurrency siteCurrency : singleCheckoutDomain.getSiteCurrencies()) {
			if (siteCurrency.getActive() != Constants.ACTIVE_YES) {
				continue;
			}
			SiteCurrencyInfo currencyInfo = new SiteCurrencyInfo();
			currencyInfo.setSiteCurrencyId(siteCurrency.getSiteCurrencyId().toString());
			currencyInfo.setSiteCurrencyClassName(siteCurrency.getSiteCurrencyClass().getSiteCurrencyClassName());
			currencies.add(currencyInfo);
		}
		siteInfo.setCurrencies(currencies);
		
		siteInfo.setSingleCheckout(singleCheckout == Constants.VALUE_YES);
		if (singleCheckout == Constants.VALUE_YES) {
			Vector<SiteDomainInfo> siteDomains = new Vector<SiteDomainInfo>();
			for (SiteDomain d : siteDomain.getSite().getSiteDomains()) {
				SiteDomainInfo siteDomainInfo = new SiteDomainInfo();
				siteDomainInfo.setSiteDomainId(d.getSiteDomainId().toString());
				siteDomainInfo.setSiteDomainPrefix(d.getSiteDomainPrefix());
				siteDomainInfo.setSiteName(d.getSiteDomainLanguage().getSiteName());
				String siteDomainHomeUrl = SiteDomainDAO.getPublicURLPrefix(d) + "/" + 
				 						   ApplicationGlobal.getContextPath() + 
				 						   Constants.FRONTEND_URL_PREFIX +
				 						   "/" + d.getSiteDomainPrefix() +
				 						   "/" + siteProfile.getSiteProfileClass().getSiteProfileClassName() +
				 						   "/" + Constants.FRONTEND_URL_HOME;
				siteDomainInfo.setHomeUrl(siteDomainHomeUrl);
			    String siteLogoUrl = imageUrlPrefix + 
			   			 			 "?type=S" +
			   			 			 "&imageId=" + d.getSiteDomainLanguage().getSiteDomainLangId(); 
				siteDomainInfo.setSiteLogoUrl(siteLogoUrl);
		        if (!siteProfileClassDefault) {
		        	SiteProfileClass siteProfileClass = siteProfile.getSiteProfileClass();
		        	for (SiteDomainLanguage language : d.getSiteDomainLanguages()) {
		        		if (!language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClass.getSiteProfileClassId())) {
		        			continue;
		        		}
		        		if (language.getSiteName() != null) {
		        			siteDomainInfo.setSiteName(language.getSiteName());
		        		}
		        		if (language.getSiteLogoValue() != null) {
		        			siteLogoUrl = imageUrlPrefix + 
				   			 			  "?type=S" +
				   			 			  "&imageId=" + language.getSiteDomainLangId(); 
		        			siteDomainInfo.setSiteLogoUrl(siteLogoUrl);
		        		}
		        		break;
		        	}
		        }
				siteDomains.add(siteDomainInfo);
			}
			siteInfo.setSiteDomains(siteDomains);
		}
		
		siteInfo.setPublicURLPrefix(SiteDomainDAO.getPublicURLPrefix(siteDomain));
		siteInfo.setSecureURLPrefix(SiteDomainDAO.getSecureURLPrefix(siteDomain));

		return siteInfo;
	}
	
	public ContactUsInfo[] getContactUs() throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from ContactUs where siteId = :siteId and active = :active order by seqNum");
    	query.setParameter("siteId", siteDomain.getSite().getSiteId());
    	query.setParameter("active", String.valueOf(Constants.VALUE_YES));
    	Iterator<?> iterator = query.getResultList().iterator();
    	Vector<ContactUsInfo> vector = new Vector<ContactUsInfo>();
    	while (iterator.hasNext()) {
    		ContactUs contactUs = (ContactUs) iterator.next();
    		ContactUsInfo info = new ContactUsInfo();
    		info.setContactUsName(contactUs.getContactUsLanguage().getContactUsName());
    		info.setContactUsAddressLine1(contactUs.getContactUsAddressLine1());
    		info.setContactUsAddressLine2(contactUs.getContactUsAddressLine2());
    		info.setContactUsCityName(contactUs.getContactUsCityName());
    		info.setContactUsStateCode(contactUs.getContactUsStateCode());
    		info.setContactUsStateName(contactUs.getContactUsStateName());
    		info.setContactUsCountryCode(contactUs.getContactUsCountryCode());
    		info.setContactUsCountryName(contactUs.getContactUsCountryName());
    		info.setContactUsDesc(contactUs.getContactUsLanguage().getContactUsDesc());
    		info.setSeqNum(Format.getInt(contactUs.getSeqNum()));
    		info.setContactUsZipCode(contactUs.getContactUsZipCode());
    		info.setContactUsEmail(contactUs.getContactUsEmail());
    		info.setContactUsPhone(contactUs.getContactUsPhone());
            if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
            	SiteProfile siteProfile = contentBean.getContentSessionBean().getSiteProfile();
            	Iterator<?> iterator1 = contactUs.getContactUsLanguages().iterator();
            	while (iterator1.hasNext()) {
            		ContactUsLanguage contactUsLanguage = (ContactUsLanguage) iterator1.next();
    				if (contactUsLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfile.getSiteProfileClass().getSiteProfileClassId())) {
	            		if (contactUsLanguage.getContactUsName() != null) {
	            			info.setContactUsName(contactUsLanguage.getContactUsName());
	            		}
	            		if (contactUsLanguage.getContactUsDesc() != null) {
	            			info.setContactUsDesc(contactUsLanguage.getContactUsDesc());
	            		}
    				}
            	}
            }
        	vector.add(info);
    	}
    	ContactUsInfo contactUsInfos[] = new ContactUsInfo[vector.size()];
    	vector.copyInto(contactUsInfos);
    	return contactUsInfos;
	}
	
	public SyndicationInfo[] getSyndication() throws Exception {
		SyndReader reader = SyndReader.getInstance(siteDomain.getSite().getSiteId());
		SyndicationList list = reader.getSyndicationList();
		SyndicationEntryInfo entryInfos[] = list.getSyncdicationEntryInfos();
		
		Vector<SyndicationInfo> vector = new Vector<SyndicationInfo>();
		for (int i = 0; i < entryInfos.length; i++) {
			SyndicationInfo info = new SyndicationInfo();
			info.setLink(entryInfos[i].getLink());
			info.setTitle(entryInfos[i].getTitle());
			info.setDescription(entryInfos[i].getDescription());
			info.setUpdatedDate(entryInfos[i].getUpdatedDate());
			info.setPublishDate(entryInfos[i].getPublishDate());
			vector.add(info);
		}
		SyndicationInfo syndicationInfos[] = new SyndicationInfo[vector.size()];
		vector.copyInto(syndicationInfos);
		return syndicationInfos;
	}
	
	public Vector<?> getMenu(String menuSetName, boolean customerSession) throws Exception {
		String siteDomainId = siteDomain.getSiteDomainId().toString();
		String siteProfileId = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileId().toString();
		String cacheKey = Constants.CACHE_MENU + "." + menuSetName + "." + siteDomainId + "." + siteProfileId;
		if (customerSession) {
			cacheKey += ".em";
		}
		if (CacheDAO.isCacheEnabled()) {
			Vector<?> vector = (Vector<?>) CacheDAO.getCacheValue(siteDomain.getSite().getSiteId(), cacheKey);
			if (vector != null) {
				return vector;
			}
		}
		
		Menu parent = null;
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String sql = "from   Menu menu " +
					 "where  menu.siteDomain.siteDomainId = :siteDomainId " +
					 "and    menu.menuSetName = :menuSetName " + 
					 "and    menu.menuParent is null " +
					 "and    menu.published = 'Y' ";
    	Query query = em.createQuery(sql);
    	query.setParameter("siteDomainId", siteDomain.getSiteDomainId());
    	query.setParameter("menuSetName", menuSetName);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		parent = (Menu) iterator.next();
    	}
		Vector<?> vector = getMenus(menuSetName, parent.getMenuId(), customerSession);
		CacheDAO.setCacheValue(siteDomain.getSite(), cacheKey, Constants.CACHE_TYPE_CODE_TRANSIENT, vector);
		return vector;
	}
	
	private Vector<?> getMenus(String menuSetName, Long menuParentId, boolean customerSession) throws Exception  {
		Vector<MenuInfo> vector = new Vector<MenuInfo>();
		Menu parent = MenuDAO.load(siteDomain.getSite().getSiteId(), menuParentId);
    	ContentSessionBean emBean = contentBean.getContentSessionBean();
//		SiteDomain siteDomain = emBean.getSiteDomain();
//		String urlPrefix = SiteDomainDAO.getPublicURLPrefix(siteDomain);
    	for (Menu menu : parent.getMenuChildren()) {
    		if (!customerSession) {
    			if (menu.getMenuType().equals(Constants.MENU_SIGNOUT)) {
    				continue;
    			}	
    		}
    		if (menu.getPublished() != Constants.VALUE_YES) {
    			continue;
    		}
    		
    		MenuInfo menuInfo = new MenuInfo();
    		menuInfo.setMenuName(menu.getMenuLanguage().getMenuName());
    		menuInfo.setSeqNo(menu.getSeqNum());
    		menuInfo.setMenuWindowMode(menu.getMenuWindowMode());
    		menuInfo.setMenuWindowTarget(menu.getMenuWindowTarget());
    		menuInfo.setMenus(getMenus(menuSetName, menu.getMenuId(), customerSession));
    		menuInfo.setMenuType(menu.getMenuType());
    		
    		if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
    			Iterator<?> menuLanguages = menu.getMenuLanguages().iterator();
    			while (menuLanguages.hasNext()) {
    				MenuLanguage menuLanguage = (MenuLanguage) menuLanguages.next();
    				if (menuLanguage.getSiteProfileClass().getSiteProfileClassId().equals(emBean.getSiteProfile().getSiteProfileClass().getSiteProfileClassId())) {
    					if (menuLanguage.getMenuName() != null) {
    						menuInfo.setMenuName(menuLanguage.getMenuName());
    						break;
    					}
    				}
    			}
    		}
    		
    		String menuAnchor = null;
    		String url = null;
    		String menuName = null;
			if (menu.getMenuType().equals(Constants.MENU_STATIC_URL) && menu.getMenuUrl() != null) {
				url = menu.getMenuUrl();
			}
			else if (menu.getMenuType().equals(Constants.MENU_HOME)) {
				url = "/" + ApplicationGlobal.getContextPath() + 
					  Constants.FRONTEND_URL_PREFIX +
					  "/" + contentBean.getContentSessionBean().getSiteDomain().getSiteDomainPrefix() +
  					  "/" + contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getSiteProfileClassName() +
					  "/" + Constants.FRONTEND_URL_HOME;
			}
			else if (menu.getMenuType().equals(Constants.MENU_ITEM) && menu.getItem() != null && ItemDAO.isPublished(menu.getItem())) {
				Item item = menu.getItem();
				url = "/" + ApplicationGlobal.getContextPath() + 
					  Constants.FRONTEND_URL_PREFIX + 
					  "/" + contentBean.getContentSessionBean().getSiteDomain().getSiteDomainPrefix() +
  					  "/" + contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getSiteProfileClassName() +
					  "/" + Constants.FRONTEND_URL_ITEM + 
					  "/" + item.getItemNaturalKey();
			}
			else if (menu.getMenuType().equals(Constants.MENU_CONTENT) && menu.getContent() != null && ContentDAO.isPublished(menu.getContent())) {
				Content content = menu.getContent();
				url = "/" + ApplicationGlobal.getContextPath() + 
					  Constants.FRONTEND_URL_PREFIX + 
					  "/" + contentBean.getContentSessionBean().getSiteDomain().getSiteDomainPrefix() +
  					  "/" + contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getSiteProfileClassName() +
					  "/" + Constants.FRONTEND_URL_CONTENT + 
					  "/" + content.getContentNaturalKey();
			}
			else if (menu.getMenuType().equals(Constants.MENU_SECTION) && menu.getCategory() != null && CategoryDAO.isPublished(menu.getCategory())) {
				Category category = menu.getCategory();
				url = "/" + ApplicationGlobal.getContextPath() + 
					  Constants.FRONTEND_URL_PREFIX +
					  "/" + contentBean.getContentSessionBean().getSiteDomain().getSiteDomainPrefix() +
  					  "/" + contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getSiteProfileClassName() +
					  "/" + Constants.FRONTEND_URL_SECTION + 
				      "/" + category.getCatNaturalKey() +	// topCategory
				      "/" + category.getCatNaturalKey();  // category
			}
			else if (menu.getMenuType().equals(Constants.MENU_CONTACTUS)) {
				url = "/" + ApplicationGlobal.getContextPath() + 
				  	  Constants.FRONTEND_URL_PREFIX +
					  "/" + contentBean.getContentSessionBean().getSiteDomain().getSiteDomainPrefix() +
  					  "/" + contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getSiteProfileClassName() +
					  "/" + Constants.FRONTEND_URL_CONTACTUS;
			}
			else if (menu.getMenuType().equals(Constants.MENU_SIGNIN)) {
				url = "/" + ApplicationGlobal.getContextPath() + "/myaccount/portal/myAccountPortal.do?" + 
					  "process=start&" +
					  "prefix=" + contentBean.getContentSessionBean().getSiteDomain().getSiteDomainPrefix() + "&" +
					  "langName=" + contentBean.getContentSessionKey().getSiteProfileClassName();
			}
			else if (menu.getMenuType().equals(Constants.MENU_SIGNOUT)) {
				url = "/" + ApplicationGlobal.getContextPath() + "/myaccount/login/myAccountLogout.do?" +
					  "process=logout&" +
					  "prefix=" + contentBean.getContentSessionBean().getSiteDomain().getSiteDomainPrefix() + "&" +
					  "langName=" + contentBean.getContentSessionKey().getSiteProfileClassName();
			}
			if (url == null && !menu.getMenuType().equals(Constants.MENU_NOOPERATION)) {
				url = "/" + ApplicationGlobal.getContextPath() + "/content/frontend/messagesAction.do?" + 
					  "messageId=content.menu.nolink&" +
					  "prefix=" + contentBean.getContentSessionBean().getSiteDomain().getSiteDomainPrefix() + "&" +
					  "langName=" + contentBean.getContentSessionKey().getSiteProfileClassName();
			}
			menuInfo.setMenuUrl(url);
			menuName = menu.getMenuLanguage().getMenuName();
			
			menuAnchor = "<a href=\"" + url + "\"" + 
			             "onclick=\"javascrpt:window.open('" + url + "', " + "'" + menu.getMenuWindowTarget() + "' ";
			if (menu.getMenuWindowMode().trim().length() != 0) {
				menuAnchor += ", '" + menu.getMenuWindowMode() + "'";
			}
			menuAnchor += ");return false;\">";
			menuAnchor += menuName;
			menuAnchor += "</a>";
    		menuInfo.setMenuAnchor(menuAnchor);


			vector.add(menuInfo);
    	}
		return vector;
	}
	
	/******************************************************************************************************/
	
	public ItemInfo getItem(String itemNaturalKey, boolean updateStatistics) throws Exception {
		Item item = dataApi.getItem(siteDomain.getSite().getSiteId(), itemNaturalKey);
    	if (!isValidItem(item)) {
    		return null;
    	}
        if (updateStatistics) {
        	item.setItemHitCounter(new Integer(item.getItemHitCounter().intValue() + 1));
        }
        return formatItem(item);
	}
	
	public ItemInfo getItem(Long itemId, boolean updateStatistics) throws Exception {
        Item item = dataApi.getItem(siteDomain.getSite().getSiteId(), itemId);
    	if (!isValidItem(item)) {
    		return null;
    	}
        if (updateStatistics) {
        	item.setItemHitCounter(new Integer(item.getItemHitCounter().intValue() + 1));
        }
        return formatItem(item);
	}
	
	public boolean isValidItem(Item item) throws Exception {
        if (item == null) {
        	return false;
        }
    	if (!ItemDAO.isPublished(item)) {
    		return false;
    	}
        if (!item.getSiteId().equals(siteDomain.getSite().getSiteId())) {
        	return false;
        }
        if (siteDomain.getSite().getShareInventory() != Constants.VALUE_YES) {
            boolean found = false;
        	for (SiteDomain s : item.getSiteDomains()) {
        		if (s.getSiteDomainId().equals(siteDomain.getSiteDomainId())) {
        			found = true;
        		}
        	}
            if (!found) {
            	return false;
            }
        }
        return true;
	}
	
	public ItemInfo formatItem(Item item) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		
		SiteProfile siteProfile = contentBean.getContentSessionBean().getSiteProfile();
		SiteCurrency siteCurrency = contentBean.getContentSessionBean().getSiteCurrency();
		
		Item master = item;
		if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_SKU)) {
			master = item.getItemSkuParent();
		}

        Long defaultSiteCurrencyClassId = contentBean.getContentSessionBean().getSiteDomain().getSite().getSiteCurrencyClassDefault().getSiteCurrencyClassId();
 		ItemInfo itemInfo = new ItemInfo();
		
		ItemLanguage masterLanguageDefault = master.getItemLanguage();
        itemInfo.setItemShortDesc(masterLanguageDefault.getItemShortDesc());
        itemInfo.setItemDesc(masterLanguageDefault.getItemDesc());
        if (Format.isNullOrEmpty(masterLanguageDefault.getPageTitle())) {
        	itemInfo.setPageTitle(masterLanguageDefault.getItemShortDesc());
        }
        else {
        	itemInfo.setPageTitle(masterLanguageDefault.getPageTitle());
        }
        if (Format.isNullOrEmpty(masterLanguageDefault.getMetaKeywords())) {
        	itemInfo.setMetaKeywords(masterLanguageDefault.getItemShortDesc());
        }
        else {
        	itemInfo.setMetaKeywords(masterLanguageDefault.getMetaKeywords());
        }
        if (Format.isNullOrEmpty(masterLanguageDefault.getMetaDescription())) {
        	itemInfo.setMetaDescription(masterLanguageDefault.getItemShortDesc());
        }
        else {
        	itemInfo.setMetaDescription(masterLanguageDefault.getMetaDescription());
        }
        itemInfo.setItemDefaultImageUrl(null);
        if (masterLanguageDefault.getImage() != null) {
        	String imageUrl = imageUrlPrefix + "?type=I&imageId=" + masterLanguageDefault.getImage().getImageId();
        	itemInfo.setItemDefaultImageUrl(imageUrl);
        }
        Vector<String> itemImages = new Vector<String>();
        Iterator<?> iterator = masterLanguageDefault.getImages().iterator();
        while (iterator.hasNext()) {
        	ItemImage image = (ItemImage) iterator.next();
        	String imageUrl = imageUrlPrefix + "?type=I&imageId=" + image.getImageId();
        	itemImages.add(imageUrl);
        }
        itemInfo.setItemImageUrls(itemImages);
        
        if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
    		ItemLanguage masterLanguage = master.getItemLanguage();
    		for (ItemLanguage language : master.getItemLanguages()) {
    			if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfile.getSiteProfileClass().getSiteProfileClassId())) {
    				masterLanguage = language;
    				break;
    			}
    		}
        	if (masterLanguage != null) {
				if (masterLanguage.getItemShortDesc() != null) {
					itemInfo.setItemShortDesc(masterLanguage.getItemShortDesc());
				}
				if (masterLanguage.getItemDesc() != null) {
					itemInfo.setItemDesc(masterLanguage.getItemDesc());
				}
				if (masterLanguage.getPageTitle() != null) {
					itemInfo.setPageTitle(masterLanguage.getPageTitle());
				}
				if (masterLanguage.getMetaKeywords() != null) {
					itemInfo.setMetaKeywords(masterLanguage.getMetaKeywords());
				}
				if (masterLanguage.getMetaDescription() != null) {
					itemInfo.setMetaDescription(masterLanguage.getMetaDescription());
				}
				if (masterLanguage.getItemImageOverride().equals(String.valueOf(Constants.VALUE_YES))) {
					String url = null;
			        itemInfo.setItemDefaultImageUrl(null);
			        if (masterLanguage.getImage() != null) {
			        	url = "?type=I&imageId=" + masterLanguage.getImage().getImageId();
			        	itemInfo.setItemDefaultImageUrl(imageUrlPrefix + url);
			        }
			        itemImages = new Vector<String>();
			        Iterator<?> images = masterLanguage.getImages().iterator();
			        while (images.hasNext()) {
			        	ItemImage image = (ItemImage) images.next();
			        	url = "?type=I&imageId=" + image.getImageId();
			        	itemImages.add(imageUrlPrefix + url);
			        }
			        itemInfo.setItemImageUrls(itemImages);
				}
        	}
        }

		itemInfo.setItemNaturalKey(item.getItemNaturalKey());
        itemInfo.setItemId(item.getItemId().toString());
        itemInfo.setItemNum(item.getItemNum());
        itemInfo.setItemUpcCd(item.getItemUpcCd());
        itemInfo.setItemSkuCd(item.getItemSkuCd());
        itemInfo.setItemTypeCd(item.getItemTypeCd());
        
        itemInfo.setItemPrice(formatter.formatCurrency(getItemPrice(item)));
        itemInfo.setItemSpecPrice("");
        itemInfo.setSpecial(false);
        if (ItemDAO.isSpecialOn(item, defaultSiteCurrencyClassId)) {
        	itemInfo.setSpecial(true);
        	itemInfo.setItemSpecPrice(formatter.formatCurrency(getItemSpecPrice(item)));
        	itemInfo.setItemSpecPublishOn(formatter.formatDatetime(item.getItemSpecPrice().getItemPricePublishOn()));
        	itemInfo.setItemSpecExpireOn(formatter.formatDatetime(item.getItemSpecPrice().getItemPricePublishOn()));
        }
        
        itemInfo.setItemPriceRange(false);
        if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_TEMPLATE)) {
        	float itemPriceFrom = Integer.MAX_VALUE;
        	float itemPriceTo = 0;
        	for (Item child : item.getItemSkus()) {
        		float price = getItemPrice(child);
        		if (ItemDAO.isSpecialOn(child, defaultSiteCurrencyClassId)) {
        			price = getItemSpecPrice(child);
        		}
        		if (price < itemPriceFrom) {
        			itemPriceFrom = price;
        		}
        		if (price > itemPriceTo) {
        			itemPriceTo = price;
        		}
        	}
        	if (itemPriceFrom != Integer.MAX_VALUE) {
        		if (itemPriceTo != itemPriceFrom) {
        			itemInfo.setItemPriceRange(true);
        			itemInfo.setItemPriceFrom(formatter.formatCurrency(itemPriceFrom));
        			itemInfo.setItemPriceTo(formatter.formatCurrency(itemPriceTo));
        		}
        	}
        }
        
        if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_RECOMMAND_BUNDLE)) {
        	float itemPrice = 0;
        	float itemSpecPrice = 0;
        	boolean special = false;
        	for (Item child : item.getChildren()) {
        		float price = getItemPrice(child);
        		float specPrice = price;
        		if (ItemDAO.isSpecialOn(child, defaultSiteCurrencyClassId)) {
        			special = true;
        			specPrice = getItemSpecPrice(child);
        		}
        		itemPrice += price;
        		itemSpecPrice += specPrice;
        	}
        	itemInfo.setItemPrice(formatter.formatCurrency(itemPrice));
        	itemInfo.setItemSpecPrice("");
        	itemInfo.setItemSpecPublishOn("");
        	itemInfo.setItemSpecExpireOn("");
        	if (special) {
        		itemInfo.setItemSpecPrice(formatter.formatCurrency(itemSpecPrice));
        	}
        }
        
        Vector<ItemTierPriceInfo> itemTierPrices = new Vector<ItemTierPriceInfo>();
        if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_TEMPLATE)) {
        	for (Item child : item.getItemSkus()) {
        		for (ItemTierPrice itemTierPrice : child.getItemTierPrices()) {
    	        	if (!ItemTierPriceDAO.isApplicable(itemTierPrice, getCustomer())) {
    	        		continue;
    	        	}
    	        	ItemTierPriceInfo itemTierPriceInfo = new ItemTierPriceInfo();
    	        	itemTierPriceInfo.setItemTierQty(formatter.formatNumber(itemTierPrice.getItemTierQty().intValue()));
    	        	itemTierPriceInfo.setItemTierPrice(formatter.formatCurrency(itemTierPrice.getItemTierPriceCurrency().getItemPrice()));
    	            if (!siteCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(defaultSiteCurrencyClassId)) {
    	            	boolean found = false;
    	            	for (ItemTierPriceCurrency currency : itemTierPrice.getItemTierPriceCurrencies()) {
    	            		if (currency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteCurrency.getSiteCurrencyClass().getSiteCurrencyClassId())) {
    	            			if (currency.getItemPrice() != null) {
    		            			itemTierPriceInfo.setItemTierPrice(formatter.formatCurrency(currency.getItemPrice()));
    		            			found = true;
    	            			}
    	            			break;
    	            		}
    	            	}
    	            	if (!found) {
    	            		itemTierPriceInfo.setItemTierPrice(formatter.formatCurrency(currencyConverter.convert(itemTierPrice.getItemTierPriceCurrency().getItemPrice())));
    	            	}
    	            }
    	            boolean exist = false;
    	            for (int i = 0; i < itemTierPrices.size(); i++) {
    	            	ItemTierPriceInfo price = itemTierPrices.elementAt(i);
    	            	if (price.getItemTierQty().equals(itemTierPriceInfo.getItemTierQty()) && 
    	            		price.getItemTierPrice().equals(itemTierPriceInfo.getItemTierPrice())) {
    	            		exist = true;
    	            		break;
    	            	}
    	            }
    	            if (!exist) {
    	            	int index = -1;
        	            for (int i = 0; i < itemTierPrices.size(); i++) {
        	            	ItemTierPriceInfo price = itemTierPrices.elementAt(i);
        	            	if (price.getItemTierQty().equals(itemTierPriceInfo.getItemTierQty())) {
        	            		index = i;
        	            		break;
        	            	}
        	            	if (Integer.valueOf(price.getItemTierQty()) > Integer.valueOf(itemTierPriceInfo.getItemTierQty())) {
        	            		index = i;
        	            		break;
        	            	}
        	            }
        	            
        	            if (index == -1) {
        	            	itemTierPrices.add(itemTierPriceInfo);
        	            }
        	            else {
        	            	itemTierPrices.add(index, itemTierPriceInfo);
        	            }
    	            }
        		}
        	}
        }
        else {
        	for (ItemTierPrice itemTierPrice : item.getItemTierPrices()) {
	        	if (!ItemTierPriceDAO.isApplicable(itemTierPrice, getCustomer())) {
	        		continue;
	        	}
	        	ItemTierPriceInfo itemTierPriceInfo = new ItemTierPriceInfo();
	        	itemTierPriceInfo.setItemTierQty(formatter.formatNumber(itemTierPrice.getItemTierQty().intValue()));
	        	itemTierPriceInfo.setItemTierPrice(formatter.formatCurrency(itemTierPrice.getItemTierPriceCurrency().getItemPrice()));
	            if (!siteCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(defaultSiteCurrencyClassId)) {
	            	boolean found = false;
	            	for (ItemTierPriceCurrency currency : itemTierPrice.getItemTierPriceCurrencies()) {
	            		if (currency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteCurrency.getSiteCurrencyClass().getSiteCurrencyClassId())) {
	            			if (currency.getItemPrice() != null) {
		            			itemTierPriceInfo.setItemTierPrice(formatter.formatCurrency(currency.getItemPrice()));
		            			found = true;
	            			}
	            			break;
	            		}
	            	}
	            	if (!found) {
	            		itemTierPriceInfo.setItemTierPrice(formatter.formatCurrency(currencyConverter.convert(itemTierPrice.getItemTierPriceCurrency().getItemPrice())));
	            	}
	            }
	        	itemTierPrices.add(itemTierPriceInfo);
	        }
        }
        itemInfo.setItemTierPrices(itemTierPrices);
        
        itemInfo.setItemHitCounter(formatter.formatNumber(item.getItemHitCounter()));
        itemInfo.setItemQty(formatter.formatNumber(item.getItemQty()));
        itemInfo.setItemBookedQty(formatter.formatNumber(item.getItemBookedQty()));
        itemInfo.setItemPublishOn(formatter.formatFullDatetime(item.getItemPublishOn()));
        itemInfo.setItemExpireOn(formatter.formatFullDatetime(item.getItemExpireOn()));
        if (item.getUser() != null) {
        	itemInfo.setItemUpdateName(item.getUser().getUserName());
        }
        else {
        	itemInfo.setItemUpdateName(item.getRecUpdateBy());
        }
        itemInfo.setRecUpdateBy(item.getRecUpdateBy());
        itemInfo.setRecUpdateDatetime(formatter.formatFullDatetime(item.getRecUpdateDatetime()));
        itemInfo.setRecCreateBy(item.getRecCreateBy());
        itemInfo.setRecCreateDatetime(formatter.formatFullDatetime(item.getRecCreateDatetime()));

        String itemUrl = "/" + ApplicationGlobal.getContextPath() + 
						 Constants.FRONTEND_URL_PREFIX +
     					 "/" + contentBean.getContentSessionBean().getSiteDomain().getSiteDomainPrefix() +
    					 "/" + siteProfile.getSiteProfileClass().getSiteProfileClassName() +
						 "/" + Constants.FRONTEND_URL_ITEM +
						 "/" + item.getItemNaturalKey();
        
        itemInfo.setItemUrl(itemUrl);
        itemUrl = "/" + ApplicationGlobal.getContextPath() + 
		  			Constants.FRONTEND_URL_PREFIX +
					"/" + contentBean.getContentSessionBean().getSiteDomain().getSiteDomainPrefix() +
					"/" + siteProfile.getSiteProfileClass().getSiteProfileClassName() +
		  			"/" + Constants.FRONTEND_URL_ITEMCOMMENT +
		  			"/" + item.getItemNaturalKey();
        itemInfo.setItemCommentUrl(itemUrl);       
        itemUrl = "/" + ApplicationGlobal.getContextPath() + 
					Constants.FRONTEND_URL_PREFIX +
					"/" + contentBean.getContentSessionBean().getSiteDomain().getSiteDomainPrefix() +
					"/" + siteProfile.getSiteProfileClass().getSiteProfileClassName() +
					"/" + Constants.FRONTEND_URL_ITEMCOMMENTUPDATE +
					"/" + item.getItemNaturalKey();
		itemInfo.setItemCommentUpdateUrl(itemUrl);       
      
		InventoryEngine inventoryEngine = new InventoryEngine(item);
        if (inventoryEngine.isAvailable(1)) {
        	itemInfo.setOutOfStock(false);
        }
        else {
        	itemInfo.setOutOfStock(true);
        }
        String sql = "select sum(comment.commentRating), count(comment) " +
				 	 "from   Comment comment " +
				 	 "where  comment.item.itemId = :itemId";
		Query query = em.createQuery(sql);
		query.setParameter("itemId", item.getItemId());
		Object objects[] = (Object[]) query.getResultList().iterator().next();
		itemInfo.setCommentRating(formatter.formatDecimal(0));
		itemInfo.setCommentRatingPercentage("0.00");
		itemInfo.setCommentRatingPercentageNumber("0.00");
		Long itemRating = (Long) objects[0];
		Long itemRatingCount = (Long) objects[1];
		if (itemRating != null) {
			itemInfo.setCommentRating(formatter.formatDecimal((float) itemRating / itemRatingCount));
		}
		itemInfo.setCommentCount(itemRatingCount.intValue());
		if (itemRating != null) {
			itemInfo.setCommentRatingPercentage(formatter.formatDecimal((float) (itemRating * 100) / itemRatingCount / 5));
			itemInfo.setCommentRatingPercentageNumber(formatter.formatRawDecimal((float) (itemRating * 100) / itemRatingCount / 5));
		}
        Vector<ItemAttribDetailInfo> itemAttribDetailInfos = new Vector<ItemAttribDetailInfo>();
        
        sql = "from   ItemAttributeDetail itemAttributeDetail " +
        	  "where  itemAttributeDetail.item.itemId = :itemId " +
        	  "order  by itemAttributeDetail.customAttributeDetail.seqNum ";
        query = em.createQuery(sql);
        query.setParameter("itemId", item.getItemId());
        Iterator<?> itemAttributeDetails = query.getResultList().iterator();
        while (itemAttributeDetails.hasNext()) {
        	ItemAttributeDetail itemAttributeDetail = (ItemAttributeDetail) itemAttributeDetails.next();
        	if (itemAttributeDetail.getCustomAttributeDetail().getCustomAttribute().getSystemRecord() == Constants.VALUE_YES) {
        		continue;
        	}
        	ItemAttribDetailInfo itemAttribDetailInfo = new ItemAttribDetailInfo();
        	itemAttribDetailInfo.setItemAttribDetailId(itemAttributeDetail.getItemAttribDetailId().toString());
        	CustomAttribute customAttribute = itemAttributeDetail.getCustomAttributeDetail().getCustomAttribute();
        	itemAttribDetailInfo.setCustomAttribTypeCode(String.valueOf(customAttribute.getCustomAttribTypeCode()));
        	itemAttribDetailInfo.setCustomAttribDetailId(String.valueOf(itemAttributeDetail.getCustomAttributeDetail().getCustomAttribDetailId()));
        	
        	String customAttribDesc = customAttribute.getCustomAttributeLanguage().getCustomAttribDesc();
        	if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
        		for (CustomAttributeLanguage language : customAttribute.getCustomAttributeLanguages()) {
        			if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
        				if (language.getCustomAttribDesc() != null) {
        					customAttribDesc = language.getCustomAttribDesc();
        				}
        				break;
        			}
        		}
        	}
        	itemAttribDetailInfo.setCustomAttribDesc(customAttribDesc);
        	
        	char customAttribTypeCode = itemAttributeDetail.getCustomAttributeDetail().getCustomAttribute().getCustomAttribTypeCode();
        	String itemAttribDetailValue = null;
        	switch (customAttribTypeCode) {
	        	case Constants.CUSTOM_ATTRIBUTE_TYPE_USER_INPUT:
		        	itemAttribDetailValue = itemAttributeDetail.getItemAttributeDetailLanguage().getItemAttribDetailValue();
		        	if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
		        		for (ItemAttributeDetailLanguage language : itemAttributeDetail.getItemAttributeDetailLanguages()) {
		        			if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
		        				if (language.getItemAttribDetailValue() != null) {
		        					itemAttribDetailValue = language.getItemAttribDetailValue();
		        				}
		        				break;
		        			}
		        		}
		        	}
		        	itemAttribDetailInfo.setItemAttribDetailValue(itemAttribDetailValue);
	        		break;
	        	case Constants.CUSTOM_ATTRIBUTE_TYPE_CUST_INPUT:
		        	itemAttribDetailInfo.setItemAttribDetailValue("");
	        		break;
	        	case Constants.CUSTOM_ATTRIBUTE_TYPE_USER_SELECT_DROPDOWN:
	        		CustomAttributeOption customAttributeOption = itemAttributeDetail.getCustomAttributeOption();
	        		itemAttribDetailValue = "";
	        		if (customAttributeOption != null) {
		        		itemAttribDetailValue = customAttributeOption.getCustomAttributeOptionLanguage().getCustomAttribValue();
			        	if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
			        		for (CustomAttributeOptionLanguage language : itemAttributeDetail.getCustomAttributeOption().getCustomAttributeOptionLanguages()) {
			        			if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
			        				if (language.getCustomAttribValue() != null) {
			        					itemAttribDetailValue = language.getCustomAttribValue();
			        				}
			        				break;
			        			}
			        		}
			        	}
	        		}
		        	itemAttribDetailInfo.setItemAttribDetailValue(itemAttribDetailValue);
	        		break;
	        	case Constants.CUSTOM_ATTRIBUTE_TYPE_CUST_SELECT_DROPDOWN:
	        	case Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP:
		        	itemAttribDetailInfo.setCustomAttribOptionId("");
		        	if (itemAttributeDetail.getCustomAttributeOption() != null) {
		        		itemAttribDetailInfo.setCustomAttribOptionId(itemAttributeDetail.getCustomAttributeOption().getCustomAttribOptionId().toString());
		        	}
		        	
		        	Vector<LabelValueBean> customAttribOptions = new Vector<LabelValueBean>();
		        	for (CustomAttributeOption option : customAttribute.getCustomAttributeOptions()) {
		        		String customAttribOption = option.getCustomAttribOptionId().toString();
		        		String customAttribValue = option.getCustomAttributeOptionLanguage().getCustomAttribValue();
		            	if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
			        		for (CustomAttributeOptionLanguage language : option.getCustomAttributeOptionLanguages()) {
			        			if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
			        				if (language.getCustomAttribValue() != null) {
			        					customAttribValue = language.getCustomAttribValue();
			        				}
			        			}
			        		}
		            	}
		            	customAttribOptions.add(new LabelValueBean(customAttribValue, customAttribOption));
		        	}
		        	itemAttribDetailInfo.setCustomAttribOptions(customAttribOptions);
	        		break;
        	}
        	
        	itemAttribDetailInfos.add(itemAttribDetailInfo);
        }
        itemInfo.setItemAttribDetailInfos(itemAttribDetailInfos);
        
        return itemInfo;
	}
	
	public float getItemPrice(Item item) {
		float itemPrice = 0;
		itemPrice = item.getItemPrice().getItemPrice();
		if (!contentBean.getContentSessionKey().isSiteCurrencyClassDefault()) {
			boolean found = false;
			for (ItemPriceCurrency itemPriceCurrency : item.getItemPriceCurrencies()) {
				if (itemPriceCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(contentBean.getContentSessionKey().getSiteCurrencyClassId())) {
					if (itemPriceCurrency.getItemPrice() != null) {
						itemPrice = itemPriceCurrency.getItemPrice();
						found = true;
					}
					break;
				}
			}
			if (!found) {
				itemPrice = currencyConverter.convert(itemPrice);
			}
		}
		return itemPrice;
	}
	
	public float getItemSpecPrice(Item item) {
		float itemSpecPrice = 0;
		itemSpecPrice = item.getItemSpecPrice().getItemPrice();
		if (!contentBean.getContentSessionKey().isSiteCurrencyClassDefault()) {
			boolean found = false;
			for (ItemPriceCurrency itemPriceCurrency : item.getItemPriceCurrencies()) {
				if (itemPriceCurrency.getItemPriceTypeCode() != Constants.ITEM_PRICE_TYPE_CODE_SPECIAL) {
					continue;
				}
				if (itemPriceCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(contentBean.getContentSessionKey().getSiteCurrencyClassId())) {
					if (itemPriceCurrency.getItemPrice() != null) {
						itemSpecPrice = itemPriceCurrency.getItemPrice();
						found = true;
					}
					break;
				}
			}
			if (!found) {
				itemSpecPrice = currencyConverter.convert(itemSpecPrice);
			}
		}
		return itemSpecPrice;
	}
	
	public CommentInfo[] getItemComment(String itemNaturalKey) throws Exception {
        Vector<CommentInfo> comments = new Vector<CommentInfo>();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        String sql = "from   Comment comment " +
	 	 			 "where  comment.item.itemNaturalKey = :itemNaturalKey " + 
	 	 			 "order  by comment.recUpdateDatetime desc";
		Query query = em.createQuery(sql);
		query.setParameter("itemNaturalKey", itemNaturalKey);
        Iterator<?> iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
         	Comment comment = (Comment) iterator.next();
         	if (comment.getCommentApproved() != null && comment.getCommentApproved().charValue() == Constants.VALUE_NO) {
         		continue;
         	}
         	CommentInfo commentInfo = formatComment(comment);
         	comments.add(commentInfo);
        }
        CommentInfo contentInfos[] = new CommentInfo[comments.size()];
        comments.copyInto(contentInfos);
		return contentInfos;
	}
	
	public ItemInfo[] getRelatedItems(String itemNaturalKey) throws Exception {
        Item item = dataApi.getItem(siteDomain.getSite().getSiteId(), itemNaturalKey);
        if (item == null) {
        	return null;
        }
        Iterator<?> itemIterator = item.getItemsRelated().iterator();
        Vector<ItemInfo> relatedItems = new Vector<ItemInfo>();
        while (itemIterator.hasNext()) {
        	Item relatedItem = (Item) itemIterator.next();
        	if (!isValidItem(relatedItem)) {
        		continue;
        	}
        	ItemInfo relatedItemInfo = formatItem(relatedItem);
        	relatedItems.add(relatedItemInfo);
        }
        ItemInfo itemInfos[] = new ItemInfo[relatedItems.size()];
        relatedItems.copyInto(itemInfos);
        return itemInfos;
	}
	
	public ItemInfo[] getUpSellItems(String itemNaturalKey) throws Exception {
        Item item = dataApi.getItem(siteDomain.getSite().getSiteId(), itemNaturalKey);
        if (item == null) {
        	return null;
        }
        Iterator<?> itemIterator = item.getItemsUpSell().iterator();
        Vector<ItemInfo> upsellItems = new Vector<ItemInfo>();
        while (itemIterator.hasNext()) {
        	Item upsellItem = (Item) itemIterator.next();
        	if (!isValidItem(upsellItem)) {
        		continue;
        	}
        	ItemInfo upsellItemInfo = formatItem(upsellItem);
        	upsellItems.add(upsellItemInfo);
        }
        ItemInfo itemInfos[] = new ItemInfo[upsellItems.size()];
        upsellItems.copyInto(itemInfos);
        return itemInfos;
	}
	
	public ItemInfo[] getCrossSellItems(String itemNaturalKey) throws Exception {
        Item item = dataApi.getItem(siteDomain.getSite().getSiteId(), itemNaturalKey);
        if (item == null) {
        	return null;
        }
        Iterator<?> itemIterator = item.getItemsCrossSell().iterator();
        Vector<ItemInfo> crossSellItems = new Vector<ItemInfo>();
        while (itemIterator.hasNext()) {
        	Item crossSellItem = (Item) itemIterator.next();
        	if (!isValidItem(crossSellItem)) {
        		continue;
        	}
        	ItemInfo itemInfo = formatItem(crossSellItem);
        	crossSellItems.add(itemInfo);
        }
        ItemInfo itemInfos[] = new ItemInfo[crossSellItems.size()];
        crossSellItems.copyInto(itemInfos);
        return itemInfos;
	}
	
	public ItemInfo[] getBundleItems(String itemNaturalKey) throws Exception {
        Item item = dataApi.getItem(siteDomain.getSite().getSiteId(), itemNaturalKey);
        if (item == null) {
        	return null;
        }
        Iterator<?> itemIterator = item.getChildren().iterator();
        Vector<ItemInfo> bundleItems = new Vector<ItemInfo>();
        while (itemIterator.hasNext()) {
        	Item child = (Item) itemIterator.next();
        	if (!isValidItem(child)) {
        		continue;
        	}
        	ItemInfo itemInfo = formatItem(child);
        	bundleItems.add(itemInfo);
        }
        ItemInfo itemInfos[] = new ItemInfo[bundleItems.size()];
        bundleItems.copyInto(itemInfos);
        return itemInfos;
	}
	
	public ItemInfo[] getSkuItems(String itemNaturalKey) throws Exception {
        Item item = dataApi.getItem(siteDomain.getSite().getSiteId(), itemNaturalKey);
        if (item == null) {
        	return null;
        }
        Iterator<?> itemIterator = item.getItemSkus().iterator();
        Vector<ItemInfo> bundleItems = new Vector<ItemInfo>();
        while (itemIterator.hasNext()) {
        	Item child = (Item) itemIterator.next();
        	if (!ItemDAO.isPublished(child)) {
        		continue;
        	}
        	ItemInfo itemInfo = formatItem(child);
        	bundleItems.add(itemInfo);
        }
        ItemInfo itemInfos[] = new ItemInfo[bundleItems.size()];
        bundleItems.copyInto(itemInfos);
        return itemInfos;
	}
	
	/******************************************************************************************************/

	public ContentInfo getContent(String contentNaturalKey, boolean updateStatistics) throws Exception {
        Content content = dataApi.getContent(siteDomain.getSite().getSiteId(), contentNaturalKey);
    	if (!isValidContent(content)) {
    		return null;
    	}
        if (updateStatistics) {
        	content.setContentHitCounter(new Integer(content.getContentHitCounter().intValue() + 1));
        }
        return formatContent(content);
	}
	
	public boolean isValidContent(Content content) throws Exception {
        if (content == null) {
        	return false;
        }
    	if (!ContentDAO.isPublished(content)) {
    		return false;
    	}
        if (!content.getSiteId().equals(siteDomain.getSite().getSiteId())) {
        	return false;
        }
        return true;
	}
	
	public ContentInfo[] getRelatedContent(String contentNaturalKey) throws Exception {
        Content content = dataApi.getContent(siteDomain.getSite().getSiteId(), contentNaturalKey);
        if (content == null) {
        	return null;
        }
        Iterator<?> contentIterator = content.getContentsRelated().iterator();
        Vector<ContentInfo> relatedContents = new Vector<ContentInfo>();
        while (contentIterator.hasNext()) {
        	Content relatedContent = (Content) contentIterator.next();
        	if (!ContentDAO.isPublished(relatedContent)) {
        		continue;
        	}
        	ContentInfo relatedContentInfo = formatContent(relatedContent);
        	relatedContents.add(relatedContentInfo);
        }
        ContentInfo contentInfos[] = new ContentInfo[relatedContents.size()];
        relatedContents.copyInto(contentInfos);
        return contentInfos;
	}
	
	public CommentInfo[] getContentComment(String contentNaturalKey) throws Exception {
        Content content = dataApi.getContent(siteDomain.getSite().getSiteId(), contentNaturalKey);
        if (content == null) {
        	return null;
        }
        Vector<CommentInfo> comments = new Vector<CommentInfo>();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        String sql = "from   Comment comment " +
	 	 			 "where  comment.content.contentId = :contentId " + 
	 	 			 "order  by comment.recUpdateDatetime desc";
		Query query = em.createQuery(sql);
		query.setParameter("contentId", content.getContentId());
        Iterator<?> iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
         	Comment comment = (Comment) iterator.next();
         	if (comment.getCommentApproved() != null && comment.getCommentApproved().charValue() == Constants.VALUE_NO) {
         		continue;
         	}
         	CommentInfo commentInfo = formatComment(comment);
         	comments.add(commentInfo);
        }
        CommentInfo contentInfos[] = new CommentInfo[comments.size()];
        comments.copyInto(contentInfos);
		return contentInfos;
	}

	
	public ContentInfo formatContent(Content content) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();

		ContentInfo contentInfo = new ContentInfo();
		ContentLanguage contentLanguageDefault = content.getContentLanguage();
        contentInfo.setContentTitle(contentLanguageDefault.getContentTitle());
        contentInfo.setContentShortDesc(contentLanguageDefault.getContentShortDesc());
        contentInfo.setContentDesc(contentLanguageDefault.getContentDesc());
        if (Format.isNullOrEmpty(contentLanguageDefault.getPageTitle())) {
        	contentInfo.setPageTitle(contentLanguageDefault.getContentTitle());
        }
        else {
        	contentInfo.setPageTitle(contentLanguageDefault.getPageTitle());
        }
        if (Format.isNullOrEmpty(contentLanguageDefault.getMetaKeywords())) {
        	contentInfo.setMetaKeywords(contentLanguageDefault.getContentTitle());
        }
        else {
        	contentInfo.setMetaKeywords(contentLanguageDefault.getMetaKeywords());
        }
        if (Format.isNullOrEmpty(contentLanguageDefault.getMetaDescription())) {
        	contentInfo.setMetaDescription(contentLanguageDefault.getContentTitle());
        }
        else {
        	contentInfo.setMetaDescription(contentLanguageDefault.getMetaDescription());
        }
        contentInfo.setContentDefaultImageUrl(null);
        if (contentLanguageDefault.getImage() != null) {
        	String imageUrl = imageUrlPrefix + 
        					  "?type=C" +
        					  "&imageId=" + contentLanguageDefault.getImage().getImageId();
        	contentInfo.setContentDefaultImageUrl(imageUrl);
        }
        Vector<String> contentImages = new Vector<String>();
        Iterator<?> iterator = contentLanguageDefault.getImages().iterator();
        while (iterator.hasNext()) {
        	ContentImage image = (ContentImage) iterator.next();
        	String imageUrl = imageUrlPrefix + 
        					  "?type=C" + 
        					  "&imageId=" + image.getImageId();
        	contentImages.add(imageUrl);
        }
        contentInfo.setContentImageUrls(contentImages);
        if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
    		ContentLanguage contentLanguage = null;
    		for (ContentLanguage language : content.getContentLanguages()) {
    			if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfile.getSiteProfileClass().getSiteProfileClassId())) {
    				contentLanguage = language;
    				break;
    			}
    		}
    		if (contentLanguage != null) {
	 			if (contentLanguage.getContentTitle() != null) {
					contentInfo.setContentTitle(contentLanguage.getContentTitle());
				}
				if (contentLanguage.getContentShortDesc() != null) {
					contentInfo.setContentShortDesc(contentLanguage.getContentShortDesc());
				}
				if (contentLanguage.getContentDesc() != null) {
					contentInfo.setContentDesc(contentLanguage.getContentDesc());
				}
				if (Format.isNullOrEmpty(contentLanguageDefault.getPageTitle())) {
					if (contentLanguage.getContentTitle() != null) {
						contentInfo.setPageTitle(contentLanguage.getContentTitle());
					}
				}
				else {
					if (contentLanguage.getPageTitle() != null) {
						contentInfo.setPageTitle(contentLanguage.getPageTitle());
					}
				}
				if (Format.isNullOrEmpty(contentLanguageDefault.getMetaKeywords())) {
					if (contentLanguage.getContentTitle() != null) {
						contentInfo.setMetaKeywords(contentLanguage.getContentTitle());
					}
				}
				else {
					if (contentLanguage.getMetaKeywords() != null) {
						contentInfo.setMetaKeywords(contentLanguage.getMetaKeywords());
					}
				}
				if (Format.isNullOrEmpty(contentLanguageDefault.getMetaDescription())) {
					if (contentLanguage.getContentTitle() != null) {
						contentInfo.setMetaDescription(contentLanguage.getContentTitle());
					}
				}
				else {
					if (contentLanguage.getMetaKeywords() != null) {
						contentInfo.setMetaDescription(contentLanguage.getMetaDescription());
					}
				}
				if (contentLanguage.getContentImageOverride().equals(String.valueOf(Constants.VALUE_YES))) {
	    			if (contentLanguage.getContentImageOverride().equals(String.valueOf(Constants.VALUE_YES))) {
	    				String url = null;
	    		        contentInfo.setContentDefaultImageUrl(null);
	    		        if (contentLanguage.getImage() != null) {
	    		        	url = "?type=C&" + 
	    		        		  "&imageId=" + contentLanguage.getImage().getImageId();
	    		        	contentInfo.setContentDefaultImageUrl(imageUrlPrefix + url);
	    		        }
	    		        contentImages = new Vector<String>();
	    		        Iterator<?> images = contentLanguage.getImages().iterator();
	    		        while (images.hasNext()) {
	    		        	ContentImage image = (ContentImage) images.next();
	    		        	url = "?type=C" + 
	    		        		  "&imageId=" + image.getImageId();
	    		        	contentImages.add(imageUrlPrefix + url);
	    		        }
	    		        contentInfo.setContentImageUrls(contentImages);
	    			}
				}
        	}
        }
		
        contentInfo.setContentNaturalKey(content.getContentNaturalKey());
        contentInfo.setContentId(content.getContentId().toString());
        if (content.getUser() != null) {
        	contentInfo.setContentUpdateName(content.getUser().getUserName());
        }
        else {
        	contentInfo.setContentUpdateName(content.getRecUpdateBy());
        }
        contentInfo.setRecUpdateDatetime(formatter.formatFullDatetime(content.getRecUpdateDatetime()));
        contentInfo.setRecCreateBy(content.getRecCreateBy());
        contentInfo.setRecCreateDatetime(formatter.formatFullDatetime(content.getRecCreateDatetime()));

       
        String url = "/" + ApplicationGlobal.getContextPath() + 
        					Constants.FRONTEND_URL_PREFIX +
        					"/" + contentBean.getContentSessionBean().getSiteDomain().getSiteDomainPrefix() +
        					"/" + siteProfile.getSiteProfileClass().getSiteProfileClassName() +
        					"/" + Constants.FRONTEND_URL_CONTENT +
        					"/" + content.getContentNaturalKey();
        contentInfo.setContentUrl(url);
        url = "/" + ApplicationGlobal.getContextPath() + 
			  Constants.FRONTEND_URL_PREFIX +
			  "/" + contentBean.getContentSessionBean().getSiteDomain().getSiteDomainPrefix() +
			  "/" + siteProfile.getSiteProfileClass().getSiteProfileClassName() +
			  "/" + Constants.FRONTEND_URL_CONTENTCOMMENT +
			  "/" + content.getContentNaturalKey();
        contentInfo.setContentCommentUrl(url);
        url = "/" + ApplicationGlobal.getContextPath() + 
			  Constants.FRONTEND_URL_PREFIX +
			  "/" + contentBean.getContentSessionBean().getSiteDomain().getSiteDomainPrefix() +
			  "/" + siteProfile.getSiteProfileClass().getSiteProfileClassName() +
			  "/" + Constants.FRONTEND_URL_CONTENTCOMMENTUPDATE +
			  "/" + content.getContentNaturalKey();
        contentInfo.setContentCommentUpdateUrl(url);
        
        String sql = "select count(comment) " +
				 	 "from   Comment comment " +
				 	 "where  comment.content.contentId = :contentId";
		Query query = em.createQuery(sql);
		query.setParameter("contentId", content.getContentId());
		Long counter = (Long) query.getSingleResult();
		contentInfo.setCommentCount(counter.intValue());
		return contentInfo;
	}
	
	public ContentInfo[] getTopRatedContent(int count) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String sql = NamedQuery.getInstance().getQuery("content.topRated");
		Query query = em.createQuery(sql);
		query.setMaxResults(count);
		query.setParameter("siteId", siteDomain.getSite().getSiteId());
		query.setParameter("siteDomainId", siteDomain.getSiteDomainId());
		Vector<ContentInfo> contents = new Vector<ContentInfo>();
		Iterator<?> iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			Long contentId = (Long) iterator.next();
		 	Content content = ContentDAO.load(siteDomain.getSite().getSiteId(), contentId);
		  	ContentInfo contentInfo = formatContent(content);
		  	contents.add(contentInfo);
		}
		ContentInfo contentInfos[] = new ContentInfo[contents.size()];
		contents.copyInto(contentInfos);
		return contentInfos;
	}
	
	public ContentInfo[] getMostViewedContent(int count) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String sql = NamedQuery.getInstance().getQuery("content.mostViewed");
		Query query = em.createQuery(sql);
		query.setMaxResults(count);
		query.setParameter("siteId", siteDomain.getSite().getSiteId());
		query.setParameter("siteDomainId", siteDomain.getSiteDomainId());
		Vector<ContentInfo> contents = new Vector<ContentInfo>();
		Iterator<?> iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			Long contentId = (Long) iterator.next();
		 	Content content = ContentDAO.load(siteDomain.getSite().getSiteId(), contentId);
		  	ContentInfo contentInfo = formatContent(content);
		  	contents.add(contentInfo);
		}
		ContentInfo contentInfos[] = new ContentInfo[contents.size()];
		contents.copyInto(contentInfos);
		return contentInfos;
	}
	
	public ItemInfo[] getMostPopularItems(int count) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String sql = "";
		if (siteDomain.getSite().getShareInventory() == Constants.VALUE_YES) {
			sql = NamedQuery.getInstance().getQuery("item.mostPopular");
		}
		else {
			sql = NamedQuery.getInstance().getQuery("item.mostPopular.noshare");
		}
		Query query = em.createQuery(sql);
		query.setMaxResults(count);
		query.setParameter("siteId", siteDomain.getSite().getSiteId());
		if (siteDomain.getSite().getShareInventory() != Constants.VALUE_YES) {
			query.setParameter("catSiteDomainId", siteDomain.getSiteDomainId());
			query.setParameter("siteDomainId", siteDomain.getSiteDomainId());
		}
		Vector<ItemInfo> items = new Vector<ItemInfo>();
		Iterator<?> iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			Long itemId = (Long) iterator.next();
		 	Item item = ItemDAO.load(siteDomain.getSite().getSiteId(), itemId);
		  	ItemInfo itemInfo = formatItem(item);
		  	items.add(itemInfo);
		}
		ItemInfo itemInfos[] = new ItemInfo[items.size()];
		items.copyInto(itemInfos);
		return itemInfos;
	}
	
	/******************************************************************************************************/

	public CommentInfo formatComment(Comment comment) {
		CommentInfo commentInfo = new CommentInfo();
		commentInfo.setCommentTitle(comment.getCommentTitle());
     	commentInfo.setComment(comment.getComment());
     	commentInfo.setCommentRating(Format.getInt(comment.getCommentRating()));
     	commentInfo.setCommentRatingPercentage(formatter.formatDecimal((float) (comment.getCommentRating() * 100) / Constants.COMMENT_MAX_VALUE));
     	commentInfo.setCommentRatingPercentageNumber(formatter.formatRawDecimal((float) (comment.getCommentRating() * 100) / Constants.COMMENT_MAX_VALUE));
     	commentInfo.setAgreeCount(comment.getAgreeCustomers().size());
     	commentInfo.setDisagreeCount(comment.getDisagreeCustomers().size());
     	commentInfo.setCommentId(Format.getLong(comment.getCommentId()));
     	commentInfo.setCommentApproved(String.valueOf(comment.getCommentApproved()));
     	commentInfo.setModeration(String.valueOf(comment.getModeration()));
     	commentInfo.setCommentUpdateName(comment.getCustomer().getCustPublicName());
     	commentInfo.setRecUpdateBy(comment.getRecUpdateBy());
     	commentInfo.setRecUpdateDatetime(formatter.formatFullDatetime(comment.getRecUpdateDatetime()));
     	commentInfo.setRecCreateBy(comment.getRecCreateBy());
     	commentInfo.setRecCreateDatetime(formatter.formatFullDatetime(comment.getRecCreateDatetime()));
		return commentInfo;
	}
	
	public DataInfo getHomeFeatureData() throws Exception {
		HomePage homePage = siteDomain.getHomePage();
		HomePageDetail featureData = homePage.getFeatureData();
		DataInfo dataInfo = null;
		if (featureData != null) {
    		if (featureData.getContent() != null) {
    			Content content = featureData.getContent();
    			if (ContentDAO.isPublished(content)) {
	    			ContentInfo contentInfo = formatContent(content);
	    			dataInfo = contentInfo;
    			}
    		}
    		else {
    			Item item = featureData.getItem();
    			if (isValidItem(item)) {
	    			ItemInfo itemInfo = formatItem(item);
	    			dataInfo = itemInfo;
				}
    		}
		}
		return dataInfo;
	}
	
	public DataInfo[] getHomeDataInfos() throws Exception {
		HomePage homePage = siteDomain.getHomePage();
		HomePageDetail feature = homePage.getFeatureData();
    	Vector<DataInfo> vector = new Vector<DataInfo>();
		for (HomePageDetail homePageDetail : homePage.getHomePageDetails()) {
			if (feature != null) {
				if (homePageDetail.getHomePageDetailId().equals(feature.getHomePageDetailId())) {
					continue;
				}
			}
    		if (homePageDetail.getContent() != null) {
    			Content content = homePageDetail.getContent();
    			if (!ContentDAO.isPublished(content)) {
    				continue;
    			}
    			vector.add(formatContent(content));
    		}
    		else {
    			Item item = homePageDetail.getItem();
    			if (!isValidItem(item)) {
    				continue;
    			}
    			vector.add(formatItem(item));
    		}
		}
    	DataInfo dataInfos[] = new DataInfo[vector.size()];
    	vector.copyInto(dataInfos);
    	return dataInfos;
	}

	public PriceRange getAttributePriceRange(CustomAttribute customAttribute, Long customAttribOptionId) throws SecurityException, Exception {
		Double minPrice = Double.valueOf(0);
		Double maxPrice = Double.MAX_VALUE;
		for (CustomAttributeOption customAttributeOption : customAttribute.getCustomAttributeOptions()) {
			Float value = Float.valueOf(customAttributeOption.getCustomAttributeOptionCurrency().getCustomAttribValue());
			if (!contentBean.getContentSessionKey().isSiteCurrencyClassDefault()) {
				CustomAttributeOptionCurrency customAttributeOptionCurrency = null;
				for (CustomAttributeOptionCurrency currency : customAttributeOption.getCustomAttributeOptionCurrencies()) {
					if (currency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(contentBean.getContentSessionKey().getSiteCurrencyClassId())) {
						customAttributeOptionCurrency = currency;
					}
				}
				if (customAttributeOptionCurrency != null) {
					value = Float.valueOf(customAttributeOptionCurrency.getCustomAttribValue());
				}
				else {
					value = contentBean.getContentSessionBean().getSiteCurrency().getExchangeRate() * value;
				}
			}
			if (customAttributeOption.getCustomAttribOptionId().equals(customAttribOptionId)) {
				maxPrice = Double.valueOf(value);
				break;
			}
			else {
				minPrice = Double.valueOf(value);
			}
		}
		PriceRange priceRange = new PriceRange();
		priceRange.setMaxprice(maxPrice);
		priceRange.setMinPrice(minPrice);
		return priceRange;
	}

	public CategoryInfo getCategory(String catNaturalKey, String topCatNaturalKey, int pageSize, int pageNavCount, int pageNum, String sortBy, ContentFilterBean contentFilterBeans[]) throws Exception {
		CategoryInfo categoryInfo = new CategoryInfo();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String key = Utility.reEncode(catNaturalKey);
		String topKey = Utility.reEncode(topCatNaturalKey);
        Category category = dataApi.getCategory(siteDomain.getSite().getSiteId(), key);
        if (category == null) {
        	return null;
        }
        categoryInfo.setCatNaturalKey(catNaturalKey);
        categoryInfo.setCatId(category.getCatId().toString());
        categoryInfo.setTopCatId(null);
        categoryInfo.setTopCatNaturalKey(topCatNaturalKey);
        categoryInfo.setCatShortTitle(category.getCategoryLanguage().getCatShortTitle());
        categoryInfo.setCatTitle(category.getCategoryLanguage().getCatTitle());
        categoryInfo.setCatDesc(category.getCategoryLanguage().getCatDesc());
        categoryInfo.setDataCount(getDataInCategoryCounts(category.getCatId()));
        if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
        	for (CategoryLanguage language : category.getCategoryLanguages()) {
        		if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
        			if (language.getCatShortTitle() != null) {
        				categoryInfo.setCatShortTitle(language.getCatShortTitle());
        			}
        			if (language.getCatTitle() != null) {
        				categoryInfo.setCatTitle(language.getCatTitle());
        			}
        			if (language.getCatDesc() != null) {
        				categoryInfo.setCatDesc(language.getCatDesc());
        			}     			
        		}
        	}
        }

        String categoryUrl = "/" + ApplicationGlobal.getContextPath() + 
					 Constants.FRONTEND_URL_PREFIX +
					 "/" + contentBean.getContentSessionBean().getSiteDomain().getSiteDomainPrefix() +
 					 "/" + contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getSiteProfileClassName() +
					 "/" + Constants.FRONTEND_URL_SECTION +
					 "/" + 
					 topKey +	// topCategory
				  	 "/" + 
				  	 category.getCatNaturalKey();  // category
        String filterUrl = "";
        for (ContentFilterBean contentFilterBean : contentFilterBeans) {
        	filterUrl += "&filter=" + contentFilterBean.getCustomAttribId() + "," + contentFilterBean.getCustomAttribOptionId();
        }
        categoryInfo.setCategoryBasicUrl(categoryUrl);
        categoryInfo.setFilterUrl(filterUrl);
        categoryInfo.setSortBy(sortBy == null ? "" : sortBy);
        categoryInfo.setCategoryUrl(categoryUrl + "?sortBy" + sortBy + filterUrl);
       	
    	int itemStart = (pageNum - 1) * pageSize;
    	int itemEnd = itemStart + pageSize - 1;
    	int index = 0;
    	
    	String value = sortBy;
    	if (value == null || value.length() == 0) {
    		value = String.valueOf(Constants.SECTION_SORT_DESC_ASC);
    	}
    	char sortByValue = value.charAt(0);
    	
		String filterCiteria = "";
		Double minPrice = Double.MIN_VALUE * -1;
		Double maxPrice = Double.MAX_VALUE;
		for (ContentFilterBean contentFilterBean : contentFilterBeans) {
			if (contentFilterBean.getSystemRecord() == Constants.VALUE_YES) {
				CustomAttribute customAttribute = CustomAttributeDAO.load(siteDomain.getSite().getSiteId(), contentFilterBean.getCustomAttribId());
				PriceRange priceRange = getAttributePriceRange(customAttribute, contentFilterBean.getCustomAttribOptionId());
				minPrice = Double.valueOf(priceRange.getMinPrice());
				maxPrice = Double.valueOf(priceRange.getMaxprice());
			}
			else {
				String categoryAttributeSubquery = NamedQuery.getInstance().getQuery("category.attribute.subquery");
				categoryAttributeSubquery = categoryAttributeSubquery.replaceFirst(":customAttribOptionId", contentFilterBean.getCustomAttribOptionId().toString());
				filterCiteria += " " + categoryAttributeSubquery;
			}
		}
		
		String sql = "";
		if (siteDomain.getSite().getShareInventory() == Constants.VALUE_YES) {
			sql = NamedQuery.getInstance().getQuery("category.item.language");
		}
		else {
			sql = NamedQuery.getInstance().getQuery("category.item.language.noshare");
		}
		sql += " and category.catId = :catId ";
		sql += " " + filterCiteria;
		sql += " " + NamedQuery.getInstance().getQuery("category.item.language.suffix");
    	switch (sortByValue) {
    	case Constants.SECTION_SORT_DESC_ASC:
    		sql += " order by itemDescSearch.itemShortDesc";
    		break;
    	case Constants.SECTION_SORT_DESC_DSC:
    		sql += " order by itemDescSearch.itemShortDesc desc";
    		break;
    	case Constants.SECTION_SORT_PRICE_ASC:
    		sql += " order by sum(itemPriceSearch.itemPrice)";
    		break;
    	case Constants.SECTION_SORT_PRICE_DSC:
    		sql += " order by sum(itemPriceSearch.itemPrice) desc";
    		break;
    	}
    	
    	Vector<DataInfo> dataVector = new Vector<DataInfo>();
    	Query query = em.createQuery(sql);
    	query.setParameter("siteId", siteDomain.getSite().getSiteId());
    	if (siteDomain.getSite().getShareInventory() != Constants.VALUE_YES) {
    		query.setParameter("siteDomainId", siteDomain.getSiteDomainId());
    	}
    	query.setParameter("catId", category.getCatId());
    	query.setParameter("siteProfileClassId", contentBean.getContentSessionKey().getSiteProfileClassId());
    	query.setParameter("siteCurrencyClassId", contentBean.getContentSessionKey().getSiteCurrencyClassId());
    	query.setParameter("minPrice", minPrice);
    	query.setParameter("maxPrice", maxPrice);
    	query.setParameter("exchangeRate", contentBean.getContentSessionBean().getSiteCurrency().getExchangeRate() - 1);
    	
    	List<?> list = query.getResultList();
    	int totalCount = 0;
    	int itemSize = list.size();
    	totalCount += itemSize;
    	Iterator<?> sqlIterator = list.iterator();
    	while (sqlIterator.hasNext()) {
    		Long itemId = (Long) sqlIterator.next();
    		if (index >= itemStart && index <= itemEnd) {
	    		Item item = ItemDAO.load(siteDomain.getSite().getSiteId(), itemId);
	    		ItemInfo itemInfo = formatItem(item);
	    		dataVector.add(itemInfo);
    		}
    		index++;
    		if (index > itemEnd) {
    			break;
    		}
    	}
    	sql = NamedQuery.getInstance().getQuery("category.content.language");
    	switch (sortByValue) {
    	case Constants.SECTION_SORT_DESC_ASC:
    		sql += " order by contentDescSearch.contentTitle";
    		break;
    	case Constants.SECTION_SORT_DESC_DSC:
    		sql += " order by contentDescSearch.contentTitle desc";
    		break;
    	case Constants.SECTION_SORT_PRICE_ASC:
    		sql += " order by contentDescSearch.contentTitle";
    		break;
    	case Constants.SECTION_SORT_PRICE_DSC:
    		sql += " order by contentDescSearch.contentTitle";
    		break;
    	}
    	query = em.createQuery(sql);
    	query.setParameter("siteId", siteDomain.getSite().getSiteId());
    	query.setParameter("catId", category.getCatId());
    	query.setParameter("siteProfileClassId", contentBean.getContentSessionKey().getSiteProfileClassId());

    	list = query.getResultList();
    	totalCount += list.size();
    	sqlIterator = list.iterator();
    	while (sqlIterator.hasNext()) {
    		Long contentId = (Long) sqlIterator.next();
    		if (index >= itemStart && index <= itemEnd) {
	    		Content content = ContentDAO.load(siteDomain.getSite().getSiteId(), contentId);
	    		ContentInfo contentInfo = formatContent(content);
	    		dataVector.add(contentInfo);
    		}
    		index++;
    		if (index > itemEnd) {
    			break;
    		}
    	}
    	
    	int midpoint = pageNavCount / 2;
    	int recordCount = totalCount;
    	int pageTotal = recordCount / pageSize;
    	if (recordCount % pageSize > 0) {
    		pageTotal++;
    	}
    	categoryInfo.setPageTotal(pageTotal);
    	categoryInfo.setPageNum(pageNum);
    	int pageStart = pageNum - midpoint;
    	if (pageStart < 1) {
    		pageStart = 1;
    	}
    	int pageEnd = pageStart + pageNavCount - 1;
    	if (pageEnd > pageTotal) {
    		pageEnd = pageTotal;
    		if (pageEnd - pageStart + 1 < pageNavCount) {
    			pageStart = pageEnd - pageNavCount + 1;
    			if (pageStart < 1) {
    				pageStart = 1;
    			}
    		}
    	}

    	categoryInfo.setPageStart(pageStart);
    	categoryInfo.setPageEnd(pageEnd);
    	
    	Object categoryDatas[] = new Object[dataVector.size()];
    	dataVector.copyInto(categoryDatas);
    	categoryInfo.setCategoryDatas(categoryDatas);
        
        /*
         * Extract filtering information
         */
        Vector<CategoryCustomAttributeInfo> categoryCustomAttributeVector = new Vector<CategoryCustomAttributeInfo>();
        for (CustomAttribute customAttribute : category.getCustomAttributes()) {
        	boolean isFilterAttribute = false;
        	ContentFilterBean contentFilterBean = null;
        	for (ContentFilterBean bean : contentFilterBeans) {
        		if (bean.getCustomAttribId().equals(customAttribute.getCustomAttribId())) {
        			contentFilterBean = bean;
        			isFilterAttribute = true;
        			break;
        		}
        	}
        	
        	CategoryCustomAttributeInfo categoryCustomAttributeInfo = new CategoryCustomAttributeInfo();
        	SiteProfileClass siteProfileClass = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass();
        	if (isFilterAttribute) {
        		CategoryCustomAttributeOptionInfo optionInfo = new CategoryCustomAttributeOptionInfo();
        		optionInfo.setCustomAttribOptionId(contentFilterBean.getCustomAttribOptionId().toString());
        		CustomAttributeOption customAttribOption = CustomAttributeOptionDAO.load(siteDomain.getSite().getSiteId(), contentFilterBean.getCustomAttribOptionId());
        		if (customAttribute.getSystemRecord() == Constants.VALUE_YES) {
        			PriceRange priceRange = getAttributePriceRange(customAttribute, customAttribOption.getCustomAttribOptionId());
        			String rangeValue = formatter.formatCurrency(priceRange.getMinPrice().floatValue()) + 
        								" - " + 
        								formatter.formatCurrency(priceRange.getMaxprice().floatValue());
        			optionInfo.setCustomAttribValue(rangeValue);
        		}
        		else {
	        		optionInfo.setCustomAttribValue(customAttribOption.getCustomAttributeOptionLanguage().getCustomAttribValue());
	        		if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
	        			for (CustomAttributeOptionLanguage customAttributeOptionLanguage : customAttribOption.getCustomAttributeOptionLanguages()) {
	        				if (!customAttributeOptionLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClass.getSiteProfileClassId())) {
	                			continue;
	                		}
	        				if (customAttributeOptionLanguage.getCustomAttribValue() != null) {
	        					optionInfo.setCustomAttribValue(customAttributeOptionLanguage.getCustomAttribValue());
	        					break;
	        				}
	        			}
	        		}
        		}
        		String customAttribUrl = categoryUrl + "?sortBy" + sortBy;
                for (ContentFilterBean bean : contentFilterBeans) {
                	if (bean.getCustomAttribId().equals(customAttribute.getCustomAttribId())) {
                		continue;
                	}
                	customAttribUrl += "&filter=" + bean.getCustomAttribId() + "," + bean.getCustomAttribOptionId();
                }
                optionInfo.setCustomAttribUrl(customAttribUrl);
                
        		categoryCustomAttributeInfo.setCustomAttribId(customAttribute.getCustomAttribId().toString());
        		categoryCustomAttributeInfo.setCustomAttribDesc(customAttribute.getCustomAttributeLanguage().getCustomAttribDesc());
        		categoryCustomAttributeInfo.setSelected(true);
        		categoryCustomAttributeInfo.setSelectedCustomAttribOptionInfo(optionInfo);
        		if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
        			for (CustomAttributeLanguage customAttributeLanguage : customAttribute.getCustomAttributeLanguages()) {
        				if (!customAttributeLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClass.getSiteProfileClassId())) {
                			continue;
                		}
        				if (customAttributeLanguage.getCustomAttribDesc() != null) {
        					categoryCustomAttributeInfo.setCustomAttribDesc(customAttributeLanguage.getCustomAttribDesc());
        					break;
        				}
        			}
        		}
        	}
        	else {
        		Vector<CategoryCustomAttributeOptionInfo> categoryCustomAttributeOptionVector = new Vector<CategoryCustomAttributeOptionInfo>();
        		if (customAttribute.getSystemRecord() == Constants.VALUE_YES) {
        			for (CustomAttributeOption customAttributeOption : customAttribute.getCustomAttributeOptions()) {
        				long count = getDataInAttributeOptionCount(category, customAttribute, customAttributeOption, contentFilterBeans);
                		CategoryCustomAttributeOptionInfo optionInfo = new CategoryCustomAttributeOptionInfo();
                		optionInfo.setCustomAttribOptionId(customAttributeOption.getCustomAttribOptionId().toString());
                		PriceRange priceRange = getAttributePriceRange(customAttribute, customAttributeOption.getCustomAttribOptionId());
                		String rangeValue = formatter.formatCurrency(priceRange.getMinPrice().floatValue()) + 
                							" - " + 
                							formatter.formatCurrency(priceRange.getMaxprice().floatValue());
                		optionInfo.setCustomAttribValue(rangeValue);
                		optionInfo.setDataCount((int) count);
                		String customAttribUrl = categoryUrl + "?sortBy=" + sortBy;
                		for (ContentFilterBean bean : contentFilterBeans) {
                			customAttribUrl += "&filter=" + bean.getCustomAttribId() + "," + bean.getCustomAttribOptionId();
                		}
                		customAttribUrl += "&filter=" + customAttribute.getCustomAttribId() + "," + customAttributeOption.getCustomAttribOptionId();
                		optionInfo.setCustomAttribUrl(customAttribUrl);
                		categoryCustomAttributeOptionVector.add(optionInfo);
        			}
        		}
        		else {
        			for (CustomAttributeOption customAttributeOption : customAttribute.getCustomAttributeOptions()) {
        				long count = getDataInAttributeOptionCount(category, customAttribute, customAttributeOption, contentFilterBeans);
                		CategoryCustomAttributeOptionInfo optionInfo = new CategoryCustomAttributeOptionInfo();
                		optionInfo.setCustomAttribOptionId(customAttributeOption.getCustomAttribOptionId().toString());
                		optionInfo.setCustomAttribValue(customAttributeOption.getCustomAttributeOptionLanguage().getCustomAttribValue());
                		optionInfo.setDataCount((int) count);
                		if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
                			for (CustomAttributeOptionLanguage customAttributeOptionLanguage : customAttributeOption.getCustomAttributeOptionLanguages()) {
                				if (!customAttributeOptionLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClass.getSiteProfileClassId())) {
                        			continue;
                        		}
                				if (customAttributeOptionLanguage.getCustomAttribValue() != null) {
                					optionInfo.setCustomAttribValue(customAttributeOptionLanguage.getCustomAttribValue());
                					break;
                				}
                			}
                		}
                		String customAttribUrl = categoryUrl + "?sortBy=" + sortBy;
                		for (ContentFilterBean bean : contentFilterBeans) {
                			customAttribUrl += "&filter=" + bean.getCustomAttribId() + "," + bean.getCustomAttribOptionId();
                		}
                		customAttribUrl += "&filter=" + customAttribute.getCustomAttribId() + "," + customAttributeOption.getCustomAttribOptionId();
                		optionInfo.setCustomAttribUrl(customAttribUrl);
                		categoryCustomAttributeOptionVector.add(optionInfo);
        			}
        		}
        		categoryCustomAttributeInfo.setCustomAttribId(customAttribute.getCustomAttribId().toString());
        		categoryCustomAttributeInfo.setCustomAttribDesc(customAttribute.getCustomAttributeLanguage().getCustomAttribDesc());
        		categoryCustomAttributeInfo.setSelected(false);
        		if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
        			for (CustomAttributeLanguage customAttributeLanguage : customAttribute.getCustomAttributeLanguages()) {
        				if (!customAttributeLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClass.getSiteProfileClassId())) {
                			continue;
                		}
        				if (customAttributeLanguage.getCustomAttribDesc() != null) {
        					categoryCustomAttributeInfo.setCustomAttribDesc(customAttributeLanguage.getCustomAttribDesc());
        					break;
        				}
        			}
        		}
        		CategoryCustomAttributeOptionInfo categoryCustomAttributeOptionInfos[] = new CategoryCustomAttributeOptionInfo[categoryCustomAttributeOptionVector.size()];
        		categoryCustomAttributeOptionVector.copyInto(categoryCustomAttributeOptionInfos);
        		categoryCustomAttributeInfo.setCustomAttribOptionInfos(categoryCustomAttributeOptionInfos);
        	}
        	categoryCustomAttributeVector.add(categoryCustomAttributeInfo);
        }
        CategoryCustomAttributeInfo categoryCustomAttributeInfos[] = new CategoryCustomAttributeInfo[categoryCustomAttributeVector.size()];
        categoryCustomAttributeVector.copyInto(categoryCustomAttributeInfos);
        categoryInfo.setCategoryCustomAttributeInfos(categoryCustomAttributeInfos);

		return categoryInfo;
	}
	
	public CategoryInfo[] getCategoryTitles(String catNaturalKey, String topCatNaturalKey) throws Exception {
		String key = Utility.reEncode(catNaturalKey);
		String topKey = Utility.reEncode(topCatNaturalKey);
        Category category = dataApi.getCategory(siteDomain.getSite().getSiteId(), key);
        if (category == null) {
        	return null;
        }

        Vector<CategoryInfo> titleCategoryVector = new Vector<CategoryInfo>();
    	Category parent = category.getCategoryParent();
        while (true) {
            if (key.equals(topKey)) {
            	break;
            }
            key = Utility.reEncode(parent.getCatNaturalKey());
            CategoryInfo categoryTitleInfo = new CategoryInfo();
            categoryTitleInfo.setCatNaturalKey(parent.getCatNaturalKey());
            categoryTitleInfo.setCatId(Format.getLong(parent.getCatId()));
            categoryTitleInfo.setCatShortTitle(parent.getCategoryLanguage().getCatShortTitle());
            categoryTitleInfo.setCatTitle(parent.getCategoryLanguage().getCatTitle());
            categoryTitleInfo.setCatDesc(parent.getCategoryLanguage().getCatDesc());
    		String url = "/" + ApplicationGlobal.getContextPath() + 
			 	  Constants.FRONTEND_URL_PREFIX +
				  "/" + contentBean.getContentSessionBean().getSiteDomain().getSiteDomainPrefix() +
				  "/" + contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getSiteProfileClassName() +
			      "/" + Constants.FRONTEND_URL_SECTION +
			      "/" + 
			      topCatNaturalKey +	// topCategory
		  	      "/" + 
		  	    parent.getCatNaturalKey();  // category
            categoryTitleInfo.setCategoryUrl(url);
            if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
            	for (CategoryLanguage language : parent.getCategoryLanguages()) {
            		if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
            			if (language.getCatShortTitle() != null) {
            				categoryTitleInfo.setCatShortTitle(language.getCatShortTitle());
            			}
            			if (language.getCatTitle() != null) {
            				categoryTitleInfo.setCatTitle(language.getCatTitle());
            			}
            			if (language.getCatDesc() != null) {
            				categoryTitleInfo.setCatDesc(language.getCatDesc());
            			}     			
            		}
            	}
            }
            titleCategoryVector.add(categoryTitleInfo);
            parent = parent.getCategoryParent();
            if (parent == null) {
            	break;
            }
        }
        // Reverse sequence order
        CategoryInfo categoryTitleInfos[] = new CategoryInfo[titleCategoryVector.size()];
        int pos = 0;
        for (int i = titleCategoryVector.size() - 1; i >= 0; i--) {
        	categoryTitleInfos[pos++] = (CategoryInfo) titleCategoryVector.get(i);
        }
        return categoryTitleInfos;
	}
	
	public CategoryInfo[] getCategoryChildren(String catNaturalKey, String topCatNaturalKey) throws Exception {
		String key = Utility.reEncode(catNaturalKey);
        Category category = dataApi.getCategory(siteDomain.getSite().getSiteId(), key);
        if (category == null) {
        	return null;
        }

    	Vector<Object> vector = new Vector<Object>();
    	for (Category childCategory : category.getCategoryChildren()) {
    		if (childCategory.getPublished() != Constants.VALUE_YES) {
    			continue;
    		}
    		CategoryInfo childCategoryInfo = new CategoryInfo();
    		childCategoryInfo.setCatId(Format.getLong(childCategory.getCatId()));
    		childCategoryInfo.setCatShortTitle(childCategory.getCategoryLanguage().getCatShortTitle());
    		childCategoryInfo.setCatTitle(childCategory.getCategoryLanguage().getCatTitle());
    		childCategoryInfo.setCatDesc(childCategory.getCategoryLanguage().getCatDesc());
    		childCategoryInfo.setDataCount(getDataInCategoryCounts(childCategory.getCatId()));
    		String url = "/" + ApplicationGlobal.getContextPath() + 
			 	  Constants.FRONTEND_URL_PREFIX +
				  "/" + contentBean.getContentSessionBean().getSiteDomain().getSiteDomainPrefix() +
			 	  "/" + contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getSiteProfileClassName() +
			      "/" + Constants.FRONTEND_URL_SECTION +
			      "/" +
			      topCatNaturalKey +	// topCategory
		  	      "/" + 
		  	    childCategory.getCatNaturalKey();  // category
    		childCategoryInfo.setCategoryUrl(url);
            if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
            	for (CategoryLanguage language : childCategory.getCategoryLanguages()) {
            		if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
            			if (language.getCatShortTitle() != null) {
            				childCategoryInfo.setCatShortTitle(language.getCatShortTitle());
            			}
            			if (language.getCatTitle() != null) {
            				childCategoryInfo.setCatTitle(language.getCatTitle());
            			}
            			if (language.getCatTitle() != null) {
            				childCategoryInfo.setCatTitle(language.getCatTitle());
            			}     			
            		}
            	}
            }
    		vector.add(childCategoryInfo);
    	}
    	CategoryInfo categoryChildrenInfos[] = new CategoryInfo[vector.size()];
    	vector.copyInto(categoryChildrenInfos);
    	return categoryChildrenInfos;
	}
	
	private long getDataInAttributeOptionCount(Category category, CustomAttribute customAttribute, CustomAttributeOption customAttributeOption, ContentFilterBean contentFilterBeans[]) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Long count = null;
		
		Double minPrice = Double.MIN_VALUE;
		Double maxPrice = Double.MAX_VALUE;
		String subquery = "";
		if (customAttribute.getSystemRecord() == Constants.VALUE_YES) {
			PriceRange priceRange = getAttributePriceRange(customAttribute, customAttributeOption.getCustomAttribOptionId());
			minPrice = priceRange.getMinPrice();
			maxPrice = priceRange.getMaxprice();
		}
		else {
			subquery = NamedQuery.getInstance().getQuery("category.attribute.subquery");
			subquery = " " + subquery.replace(":customAttribOptionId", customAttributeOption.getCustomAttribOptionId().toString());
		}
		String filterCiteria = subquery;
		for (ContentFilterBean contentFilterBean : contentFilterBeans) {
			if (contentFilterBean.getSystemRecord() == Constants.VALUE_YES) {
				CustomAttribute attribute = CustomAttributeDAO.load(siteDomain.getSite().getSiteId(), contentFilterBean.getCustomAttribId());
				PriceRange priceRange = getAttributePriceRange(attribute, contentFilterBean.getCustomAttribOptionId());
				minPrice = priceRange.getMinPrice();
				maxPrice = priceRange.getMaxprice();
			}
			else {
				subquery = NamedQuery.getInstance().getQuery("category.attribute.subquery");
				subquery = subquery.replace(":customAttribOptionId", contentFilterBean.getCustomAttribOptionId().toString());
				filterCiteria += " " + subquery;
			}
		}

		/*
		Long catIds[] = Utility.getCatIdTreeList(siteDomain.getSite().getSiteId(), category.getCatId());
		String catIdList = "";
		for (int i = 0; i < catIds.length; i++) {
			if (i != 0) {
				catIdList += ",";
			}
			catIdList += catIds[i];
		}
		*/
		String catIdList = category.getCatId().toString();
		
		String sql = "";
		if (siteDomain.getSite().getShareInventory() == Constants.VALUE_YES) {
			sql = NamedQuery.getInstance().getQuery("category.item.language");
		}
		else {
			sql = NamedQuery.getInstance().getQuery("category.item.language.noshare");
		}
		sql += " and  category.catId in (";
		sql += catIdList;
		sql += ")";
		sql += " " + filterCiteria;
		sql += " " + NamedQuery.getInstance().getQuery("category.item.language.suffix");

    	Query query = em.createQuery(sql);
    	query.setParameter("siteId", siteDomain.getSite().getSiteId());
    	if (siteDomain.getSite().getShareInventory() != Constants.VALUE_YES) {
    		query.setParameter("siteDomainId", siteDomain.getSiteDomainId());
    	}
    	query.setParameter("siteProfileClassId", contentBean.getContentSessionKey().getSiteProfileClassId());
    	query.setParameter("siteCurrencyClassId", contentBean.getContentSessionKey().getSiteCurrencyClassId());
    	query.setParameter("minPrice", minPrice);
    	query.setParameter("maxPrice", maxPrice);
    	query.setParameter("exchangeRate", contentBean.getContentSessionBean().getSiteCurrency().getExchangeRate() - 1);

		count = Long.valueOf(query.getResultList().size());
		
		return count;
	}
	
	private long getDataInCategoryCounts(Long catId) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Long count = null;
		Long catIds[] = Utility.getCatIdTreeList(siteDomain.getSite().getSiteId(), catId);
		
		String sql = "";
		if (siteDomain.getSite().getShareInventory() == Constants.VALUE_YES) {
			sql = "select  count(distinct item.itemId) " +
				  "from    Item item " +
				  "left    join item.categories category " +
				  "where   item.site.siteId = :siteId " +
				  "and     item.published = 'Y' " +
				  "and     item.itemSellable = 'Y' " +
				  "and	   item.itemTypeCd in ('01','02','04','05') " +
				  "and	   curdate() between item.itemPublishOn and item.itemExpireOn " +
				  "and     category.catId in (";
		}
		else {
			sql = "select  count(distinct item.itemId) " +
			  "from    Item item " +
			  "left    join item.categories category " +
			  "left    join item.siteDomains siteDomain " +
			  "where   item.site.siteId = :siteId " +
			  "and     siteDomain.siteDomainId = :siteDomainId " +
			  "and     item.published = 'Y' " +
			  "and     item.itemSellable = 'Y' " +
			  "and	   item.itemTypeCd in ('01','02','04','05') " +
			  "and	   curdate() between item.itemPublishOn and item.itemExpireOn " +
			  "and     category.catId in (";
		}
		for (int i = 0; i < catIds.length; i++) {
			if (i != 0) {
				sql += ",";
			}
			sql += catIds[i];
		}
		sql += ")";
		Query query = em.createQuery(sql);
		query.setParameter("siteId", siteDomain.getSite().getSiteId());
		if (siteDomain.getSite().getShareInventory() != Constants.VALUE_YES) {
			query.setParameter("siteDomainId", siteDomain.getSiteDomainId());
		}
		count = (Long) query.getSingleResult();
		
		sql = "select  count(content) " +
			  "from    Content content " +
			  "left    join content.categories category " +
			  "where   content.site.siteId = :siteId " +
			  "and     content.published = 'Y' " +
			  "and	   curdate() between content.contentPublishOn and content.contentExpireOn " +
			  "and     category.catId in (";
		for (int i = 0; i < catIds.length; i++) {
		if (i != 0) {
			sql += ",";
		}
		sql += catIds[i];
		}
		sql += ")";
		query = em.createQuery(sql);
		query.setParameter("siteId", siteDomain.getSite().getSiteId());
		count += (Long) query.getSingleResult();

		return count;
	}

	public SearchInfo getSearch(String siteId, String query, int pageSize, int pageNavCount, int pageNum) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();

		Indexer indexer = Indexer.getInstance(siteId);
    	QueryResult queryResult = indexer.search(query, 
    											 contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getSiteProfileClassId(), 
    											 siteDomain.getSiteDomainId(),
    											 pageNum, 
    											 pageSize);
    	
    	SearchInfo info = new SearchInfo();
    	info.setQuery(query);
    	info.setHitsCount(queryResult.getHitCount());
    	
    	int recordCount = queryResult.getHitCount();
    	int pageTotal = recordCount / pageSize;
    	if (recordCount % pageSize > 0) {
    		pageTotal++;
    	}
    	info.setPageTotal(pageTotal);
    	info.setPageNum(pageNum);
    	int pageStart = pageNum - pageNavCount / 2;
    	if (pageStart < 1) {
    		pageStart = 1;
    	}
    	int pageEnd = pageNum + (pageNavCount + 1) / 2;
    	if (pageEnd > pageTotal) {
    		pageEnd = pageTotal;
    	}
    	info.setPageStart(pageStart);
    	info.setPageEnd(pageEnd);
    	
    	Vector<DataInfo> vector = new Vector<DataInfo>();
    	Vector<?> queryHits = queryResult.getQueryHits();
    	Iterator<?> iterator = queryHits.iterator();
    	while (iterator.hasNext()) {
    		Object object = iterator.next();
    		if (object instanceof CompassContentLanguage) {
    			CompassContentLanguage compassContentLanguage = (CompassContentLanguage) object;
    			Content content = (Content) em.find(Content.class, compassContentLanguage.getContentId());
    			ContentInfo contentInfo = formatContent(content);
    			vector.add(contentInfo);
    		}
    		if (object instanceof CompassItemLanguage) {
    			CompassItemLanguage compassItemLanguage = (CompassItemLanguage) object;
    			Item item = (Item) em.find(Item.class, compassItemLanguage.getItemId());
    			ItemInfo itemInfo = formatItem(item);
    			vector.add(itemInfo);
    		}
    	}
    	Object searchDatas[] = new Object[vector.size()];
    	vector.copyInto(searchDatas);
    	info.setSearchDatas(searchDatas);

    	return info;

	}
	
	public ShoppingCartSummaryInfo getShoppingCartSummary(HttpServletRequest request) throws Exception {
		ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, false);
		ShoppingCartSummaryInfo info = new ShoppingCartSummaryInfo();
		Vector<ItemInfo> itemVector = new Vector<ItemInfo>();
		int itemCount = 0;
		double priceTotal = 0;
		if (shoppingCart != null) {
			Vector<?> vector = shoppingCart.getShoppingCartItems();
			Iterator<?> iterator = vector.iterator();
			while (iterator.hasNext()) {
				ShoppingCartItem shoppingCartItem = (ShoppingCartItem) iterator.next();
				ItemInfo itemInfo = formatItem(shoppingCartItem.getItem());
				itemInfo.setItemOrderedQty(Format.getInt(shoppingCartItem.getItemQty()));
				itemVector.add(itemInfo);
			}
			itemCount = shoppingCart.getItemCount();
			priceTotal = shoppingCart.getPriceTotal();
		}
		ItemInfo cartItems[] = new ItemInfo[itemVector.size()];
		itemVector.copyInto(cartItems);
		info.setCartItems(cartItems);
		info.setItemCount(Format.getInt(itemCount));
		info.setPriceSubTotal(Format.getDouble(priceTotal));
		return info;
	}
	
	public ItemComparePageInfo getItemComparePage() throws SecurityException, Exception {
		ItemComparePageInfo itemComparePageInfo = new ItemComparePageInfo();
		ContentSessionBean contentSessionBean = contentBean.getContentSessionBean();
		SiteProfile siteProfile = contentSessionBean.getSiteProfile();
		Vector<CustomAttributeInfo> attributeVector = new Vector<CustomAttributeInfo>();
		for (String itemId: ContentLookupDispatchAction.getItemCompareList(request)) {
			Item item = ItemDAO.load(siteDomain.getSite().getSiteId(), Format.getLong(itemId));
			for (ItemAttributeDetail itemAttributeDetail: item.getItemAttributeDetails()) {
				CustomAttribute customAttribute = itemAttributeDetail.getCustomAttributeDetail().getCustomAttribute();
				if (customAttribute.getItemCompare() != Constants.VALUE_YES) {
					continue;
				}
				if (customAttribute.getSystemRecord() == Constants.VALUE_YES) {
					continue;
				}
				boolean found = false;
				for (CustomAttributeInfo customAttributeInfo: attributeVector) {
					if (customAttributeInfo.getCustomAttribId().equals(customAttribute.getCustomAttribId().toString())) {
						found = true;
						break;
					}
				}
				if (!found) {
					CustomAttributeInfo customAttributeInfo = new CustomAttributeInfo();
					customAttributeInfo.setCustomAttribId(customAttribute.getCustomAttribId().toString());
					customAttributeInfo.setCustomAttribDesc(customAttribute.getCustomAttributeLanguage().getCustomAttribDesc());
					
					for (CustomAttributeLanguage customAttributeLang: customAttribute.getCustomAttributeLanguages()) {
						if (customAttributeLang.getSiteProfileClass().getSiteProfileClassId().equals(siteProfile.getSiteProfileClass().getSiteProfileClassId())) {
							if (customAttributeLang.getCustomAttribDesc() != null) {
								customAttributeInfo.setCustomAttribDesc(customAttributeLang.getCustomAttribDesc());
							}
							break;
						}
					}
					attributeVector.add(customAttributeInfo);
				}
			}
		}
		CustomAttributeInfo customAttributeInfos[] = new CustomAttributeInfo[attributeVector.size()];
		attributeVector.copyInto(customAttributeInfos);
		itemComparePageInfo.setCustomAttributeInfos(customAttributeInfos);
		
		Vector<ItemCompareItemInfo> itemCompareItemVector = new Vector<ItemCompareItemInfo>();
		for (String itemId: ContentLookupDispatchAction.getItemCompareList(request)) {
			Item item = ItemDAO.load(siteDomain.getSite().getSiteId(), Format.getLong(itemId));
			ItemCompareItemInfo itemCompareItemInfo = new ItemCompareItemInfo();
			ItemInfo itemInfo = formatItem(item);
			itemCompareItemInfo.setItemInfo(itemInfo);
			
			String itemAttribValues[] = new String[customAttributeInfos.length];
			for (int i = 0; i < itemAttribValues.length; i++) {
				itemAttribValues[i] = "";
			}
			itemCompareItemInfo.setItemAttribValues(itemAttribValues);
			
			for (ItemAttributeDetail itemAttributeDetail: item.getItemAttributeDetails()) {
				CustomAttribute customAttribute = itemAttributeDetail.getCustomAttributeDetail().getCustomAttribute();
				int pos = 0;
				boolean found = false;
				for (pos = 0; pos < customAttributeInfos.length; pos++) {
					if (customAttributeInfos[pos].getCustomAttribId().equals(customAttribute.getCustomAttribId().toString())) {
						found = true;
						break;
					}
				}
				if (!found) {
					continue;
				}
				
				String itemAttribValue = "";
				char customAttribTypeCode = itemAttributeDetail.getCustomAttributeDetail().getCustomAttribute().getCustomAttribTypeCode();
				switch (customAttribTypeCode) {
				case Constants.CUSTOM_ATTRIBUTE_TYPE_USER_SELECT_DROPDOWN:
				case Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP:
					if (itemAttributeDetail.getCustomAttributeOption().getCustomAttributeOptionLanguage() != null) {
						itemAttribValue = itemAttributeDetail.getCustomAttributeOption().getCustomAttributeOptionLanguage().getCustomAttribValue();
						if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
							for (CustomAttributeOptionLanguage language : itemAttributeDetail.getCustomAttributeOption().getCustomAttributeOptionLanguages()) {
								if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
									if (language.getCustomAttribValue() != null) {
										itemAttribValue = language.getCustomAttribValue();
									}
									break;
								}
							}
						}
					}
					if (itemAttributeDetail.getCustomAttributeOption().getCustomAttributeOptionCurrency() != null) {
						itemAttribValue = itemAttributeDetail.getCustomAttributeOption().getCustomAttributeOptionCurrency().getCustomAttribValue();
						if (!contentBean.getContentSessionKey().isSiteCurrencyClassDefault()) {
							for (CustomAttributeOptionCurrency currency : itemAttributeDetail.getCustomAttributeOption().getCustomAttributeOptionCurrencies()) {
								if (currency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(contentBean.getContentSessionKey().getSiteCurrencyClassId())) {
									if (currency.getCustomAttribValue() != null) {
										itemAttribValue = currency.getCustomAttribValue();
									}
									break;
								}
							}
						}
					}
					break;
				case Constants.CUSTOM_ATTRIBUTE_TYPE_USER_INPUT:
					itemAttribValue = itemAttributeDetail.getItemAttributeDetailLanguage().getItemAttribDetailValue();
					if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
						for (ItemAttributeDetailLanguage language : itemAttributeDetail.getItemAttributeDetailLanguages()) {
							if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfile.getSiteProfileClass().getSiteProfileClassId())) {
								if (language.getItemAttribDetailValue() != null) {
									itemAttribValue = language.getItemAttribDetailValue();
								}
								break;
							}
						}
					}
					break;
				}
				itemAttribValues[pos] = itemAttribValue;
			}
			itemCompareItemVector.add(itemCompareItemInfo);
		}
		ItemCompareItemInfo itemCompareItemInfos[] = new ItemCompareItemInfo[itemCompareItemVector.size()];
		itemCompareItemVector.copyInto(itemCompareItemInfos);
		itemComparePageInfo.setItemCompareItemInfos(itemCompareItemInfos);
		
		return itemComparePageInfo;
	}
	
	public OrderInfo getOrder(Long orderHeaderId) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		OrderHeader orderHeader = em.find(OrderHeader.class, orderHeaderId);
		OrderEngine orderEngine = new OrderEngine(orderHeader, null);
		Long siteProfileClassId = siteProfile.getSiteProfileClass().getSiteProfileClassId();
		
		OrderInfo header = new OrderInfo();
		header.setOrderNum(orderHeader.getOrderNum());
		header.setOrderDatetime(formatter.formatFullDate(orderHeader.getOrderDate()));
		header.setCustEmail(orderHeader.getCustEmail());
		header.setCurrencyCode(orderHeader.getSiteCurrency().getSiteCurrencyClass().getCurrency().getCurrencyCode());
		
		OrderAddress custAddress = orderHeader.getCustAddress();
		header.setCustUseAddress(custAddress.getCustUseAddress());
		header.setCustPrefix(custAddress.getCustPrefix());
		header.setCustFirstName(custAddress.getCustFirstName());
		header.setCustMiddleName(custAddress.getCustMiddleName());
		header.setCustLastName(custAddress.getCustLastName());
		header.setCustSuffix(custAddress.getCustSuffix());
		header.setCustAddressLine1(custAddress.getCustAddressLine1());
		header.setCustAddressLine2(custAddress.getCustAddressLine2());
		header.setCustCityName(custAddress.getCustCityName());
		header.setCustStateName(custAddress.getCustStateName());
		header.setCustCountryName(custAddress.getCustCountryName());
		header.setCustZipCode(custAddress.getCustZipCode());
		header.setCustPhoneNum(custAddress.getCustPhoneNum());
		header.setCustFaxNum(custAddress.getCustFaxNum());

		OrderAddress billingAddress = orderHeader.getBillingAddress();
		header.setBillingCustUseAddress(billingAddress.getCustUseAddress());
		header.setBillingCustPrefix(billingAddress.getCustPrefix());
		header.setBillingCustFirstName(billingAddress.getCustFirstName());
		header.setBillingCustMiddleName(billingAddress.getCustMiddleName());
		header.setBillingCustLastName(billingAddress.getCustLastName());
		header.setBillingCustSuffix(billingAddress.getCustSuffix());
		header.setBillingCustAddressLine1(billingAddress.getCustAddressLine1());
		header.setBillingCustAddressLine2(billingAddress.getCustAddressLine2());
		header.setBillingCustCityName(billingAddress.getCustCityName());
		header.setBillingCustStateName(billingAddress.getCustStateName());
		header.setBillingCustCountryName(billingAddress.getCustCountryName());
		header.setBillingCustZipCode(billingAddress.getCustZipCode());
		header.setBillingCustPhoneNum(billingAddress.getCustPhoneNum());
		header.setBillingCustFaxNum(billingAddress.getCustFaxNum());

		OrderAddress shippingAddress = orderHeader.getShippingAddress();
		header.setShippingCustUseAddress(shippingAddress.getCustUseAddress());
		header.setShippingCustPrefix(shippingAddress.getCustPrefix());
		header.setShippingCustFirstName(shippingAddress.getCustFirstName());
		header.setShippingCustMiddleName(shippingAddress.getCustMiddleName());
		header.setShippingCustLastName(shippingAddress.getCustLastName());
		header.setShippingCustSuffix(shippingAddress.getCustSuffix());
		header.setShippingCustAddressLine1(shippingAddress.getCustAddressLine1());
		header.setShippingCustAddressLine2(shippingAddress.getCustAddressLine2());
		header.setShippingCustCityName(shippingAddress.getCustCityName());
		header.setShippingCustStateName(shippingAddress.getCustStateName());
		header.setShippingCustCountryName(shippingAddress.getCustCountryName());
		header.setShippingCustZipCode(shippingAddress.getCustZipCode());
		header.setShippingCustPhoneNum(shippingAddress.getCustPhoneNum());
		header.setShippingCustFaxNum(shippingAddress.getCustFaxNum());

		header.setSubTotal(formatter.formatCurrency(orderEngine.getOrderSubTotal()));
		header.setShippingTotal(formatter.formatCurrency(orderHeader.getShippingTotal().floatValue() - orderHeader.getShippingDiscountTotal().floatValue()));
		header.setTaxTotal(formatter.formatCurrency(orderEngine.getOrderTaxTotal()));
		header.setOrderTotal(formatter.formatCurrency(orderHeader.getOrderTotal().floatValue()));
		header.setPaymentGatewayProvider(orderHeader.getPaymentGatewayProvider());
		header.setCreditCardDesc(orderHeader.getCreditCardDesc());
		header.setShippingMethodName(orderHeader.getShippingMethodName());
		
		Vector<ItemDetailInfo> vector = new Vector<ItemDetailInfo>();
		Iterator<?> iterator = orderHeader.getOrderItemDetails().iterator();
		while (iterator.hasNext()) {
			OrderItemDetail orderItemDetail = (OrderItemDetail) iterator.next();
			ItemDetailInfo detail = new ItemDetailInfo();
			detail.setItemNum(orderItemDetail.getItemNum());
			detail.setItemUpcCd(orderItemDetail.getItemUpcCd());
			detail.setItemShortDesc(orderItemDetail.getItemShortDesc());
			detail.setItemQty(orderItemDetail.getItemOrderQty().toString());
			String result = "";
	    	if (orderItemDetail.getItemTierQty() > 1) {
	    		result = formatter.formatNumber(orderItemDetail.getItemTierQty()) + " / " +
	    				 formatter.formatCurrency(orderItemDetail.getItemTierPrice());
	    	}
	    	else {
	    		result = formatter.formatCurrency(orderItemDetail.getItemTierPrice());
	    	}
	    	detail.setItemPrice(result);
	    	detail.setItemSubTotal(formatter.formatCurrency(orderItemDetail.getItemDetailAmount()));
	    	
	    	Item item = orderItemDetail.getItem();
	    	Vector<com.jada.content.data.ItemAttributeInfo> v = new Vector<com.jada.content.data.ItemAttributeInfo>();
	    	if (item != null) {
	    		ItemImage itemImage = item.getItemLanguage().getImage();
	    		if (itemImage != null) {
	    			detail.setImageId(itemImage.getImageId().toString());
	    		}
	            if (!siteProfileClassDefault){
	            	for (ItemLanguage itemLanguage : item.getItemLanguages()) {
	            		if (itemLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
	            			if (itemLanguage.getItemImageOverride().equals(String.valueOf(Constants.VALUE_YES))) {
	            				detail.setImageId(itemLanguage.getImage().getImageId().toString());
	            			}
	            			break;
	            		}
	            	}
	            }
	    		if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_SKU)) {
	    			for (ItemAttributeDetail itemAttributeDetail : item.getItemAttributeDetails()) {
	    				CustomAttribute customAttribute = itemAttributeDetail.getCustomAttributeDetail().getCustomAttribute();
	    				if (customAttribute.getCustomAttribTypeCode() != Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP) {
	    					continue;
	    				}
	    				com.jada.content.data.ItemAttributeInfo itemAttributeInfo = new com.jada.content.data.ItemAttributeInfo();
		    			
	    				itemAttributeInfo.setCustomAttribDesc(customAttribute.getCustomAttributeLanguage().getCustomAttribDesc());
		        		if (!siteProfileClassDefault) {
		        			for (CustomAttributeLanguage language : customAttribute.getCustomAttributeLanguages()) {
		        				if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfile.getSiteProfileClass().getSiteProfileClassId())) {
		    	    				if (language.getCustomAttribDesc() != null) {
		    	    					itemAttributeInfo.setCustomAttribDesc(language.getCustomAttribDesc());
		    	    				}
		        					break;
		        				}
		        			}
		        		}
	
		        		CustomAttributeOption customAttributeOption = itemAttributeDetail.getCustomAttributeOption();
		        		itemAttributeInfo.setCustomAttribValue(customAttributeOption.getCustomAttributeOptionLanguage().getCustomAttribValue());
		        		if (!siteProfileClassDefault) {
		        			for (CustomAttributeOptionLanguage language : customAttributeOption.getCustomAttributeOptionLanguages()) {
		        				if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfile.getSiteProfileClass().getSiteProfileClassId())) {
		    	    				if (language.getCustomAttribValue() != null) {
		    	    					itemAttributeInfo.setCustomAttribValue(language.getCustomAttribValue());
		    	    				}
		        					break;
		        				}
		        			}
		        		}
		        		v.add(itemAttributeInfo);
	    			}
	    		}
	    	}
    		
	    	for (OrderAttributeDetail orderAttributeDetail : orderItemDetail.getOrderAttributeDetails()) {
	    		com.jada.content.data.ItemAttributeInfo itemAttributeInfo = new com.jada.content.data.ItemAttributeInfo();
	    		CustomAttribute customAttribute = orderAttributeDetail.getCustomAttributeDetail().getCustomAttribute();
	    		itemAttributeInfo.setCustomAttribDesc(customAttribute.getCustomAttributeLanguage().getCustomAttribDesc());
            	if (!siteProfileClassDefault) {
            		for (CustomAttributeLanguage language : customAttribute.getCustomAttributeLanguages()) {
            			if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
            				if (language.getCustomAttribDesc() != null) {
            					itemAttributeInfo.setCustomAttribDesc(language.getCustomAttribDesc());
            				}
                			break;
            			}
            		}
            	}
            	if (customAttribute.getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_CUST_INPUT) {
            		itemAttributeInfo.setCustomAttribValue(orderAttributeDetail.getOrderAttribValue());
            	}
            	else {
		    		CustomAttributeOption customAttributeOption = orderAttributeDetail.getCustomAttributeOption();
		    		itemAttributeInfo.setCustomAttribValue(customAttributeOption.getCustomAttributeOptionLanguage().getCustomAttribValue());
	    			if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
	            		for (CustomAttributeOptionLanguage language : customAttributeOption.getCustomAttributeOptionLanguages()) {
	            			if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
	            				if (language.getCustomAttribValue() != null) {
	            					itemAttributeInfo.setCustomAttribValue(language.getCustomAttribValue());
	            				}
		            			break;
	            			}
	            		}
	            	}
            	}
            	v.add(itemAttributeInfo);
	    	}
	    	com.jada.content.data.ItemAttributeInfo itemAttributeInfos[] = new com.jada.content.data.ItemAttributeInfo[v.size()];
	    	v.copyInto(itemAttributeInfos);
	    	detail.setItemAttributes(itemAttributeInfos);
			vector.add(detail);
		}
		ItemDetailInfo orderDetails[] = new ItemDetailInfo[vector.size()];
		vector.copyInto(orderDetails);
		header.setItemDetails(orderDetails);
		
		Vector<CouponDetailInfo> couponVector = new Vector<CouponDetailInfo>();
		Iterator<?> couponIterator = orderHeader.getOrderOtherDetails().iterator();
		while (couponIterator.hasNext()) {
			OrderOtherDetail orderOtherDetail = (OrderOtherDetail) couponIterator.next();
			CouponDetailInfo detail = new CouponDetailInfo();
			detail.setCouponCode(orderOtherDetail.getOrderOtherDetailNum());
			detail.setCouponName(orderOtherDetail.getOrderOtherDetailDesc());
			detail.setCouponAmount(formatter.formatCurrency(orderOtherDetail.getOrderOtherDetailAmount()));
			couponVector.add(detail);
		}
		CouponDetailInfo couponDetails[] = new CouponDetailInfo[couponVector.size()];
		couponVector.copyInto(couponDetails);
		header.setCouponDetails(couponDetails);
		
        Vector<TaxDetailInfo> taxVector = new Vector<TaxDetailInfo>();
		Iterator<?> taxIterator = orderEngine.getOrderTaxes().iterator();
		while (taxIterator.hasNext()) {
			OrderDetailTax orderDetailTax = (OrderDetailTax) taxIterator.next();
			TaxDetailInfo taxInfo = new TaxDetailInfo();
    		taxInfo.setTaxName(orderDetailTax.getTaxName());
    		taxInfo.setTaxAmount(formatter.formatCurrency(orderDetailTax.getTaxAmount()));
    		taxVector.add(taxInfo);
		}
		Collections.sort(taxVector);
		TaxDetailInfo orderTaxes[] = new TaxDetailInfo[taxVector.size()];
		taxVector.copyInto(orderTaxes);

		header.setTaxDetails(orderTaxes);
		
		SiteDomainLanguage siteDomainLanguage = siteDomain.getSiteDomainLanguage();
		if (!siteProfileClassDefault) {
			for (SiteDomainLanguage language : siteDomain.getSiteDomainLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
					siteDomainLanguage = language;
					break;
				}
			}
		}
		
		SiteDomainParamBean siteDomainParamBean = SiteDomainDAO.getSiteDomainParamBean(siteDomain.getSiteDomainLanguage(), siteDomainLanguage);
		String message = siteDomainParamBean.getCheckoutShoppingCartMessage();
		header.setShoppingCartMessage(message);
		header.setSiteName(siteDomain.getSiteDomainLanguage().getSiteName());
		if (!siteProfileClassDefault) {
			if (siteDomainLanguage.getSiteName() != null) {
				header.setSiteName(siteDomainLanguage.getSiteName());
			}
		}
		
		return header;
	}
	
	private Customer getCustomer() throws Exception {
		if (customer != null) {
			return customer;
		}
		if (custId != null) {
			EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
			customer = (Customer) em.find(Customer.class, custId);
		}
		return customer;
	}
	
	class PriceRange {
		Double minPrice;
		Double maxprice;
		public Double getMinPrice() {
			return minPrice;
		}
		public void setMinPrice(Double minPrice) {
			this.minPrice = minPrice;
		}
		public Double getMaxprice() {
			return maxprice;
		}
		public void setMaxprice(Double maxprice) {
			this.maxprice = maxprice;
		}
	}
	
	class Result implements java.io.Serializable {
		private static final long serialVersionUID = 6242770202479397500L;
		String type;
		String id;
		String price;
		String description;
		public Result() {
		}
		public Result(String type, String id, String price, String description) {
			this.type = type;
			this.id = id;
			this.price = price;
			this.description = description;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		
	}
}
