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

package com.jada.sitemap;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.XMLContext;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import org.xml.sax.InputSource;

import com.jada.dao.CategoryDAO;
import com.jada.dao.ContentDAO;
import com.jada.dao.ItemDAO;
import com.jada.dao.SiteDomainDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Category;
import com.jada.jpa.entity.Content;
import com.jada.jpa.entity.HomePage;
import com.jada.jpa.entity.HomePageDetail;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.Menu;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.SiteProfile;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.NamedQuery;
import com.jada.xml.site.SiteDomainParamBean;
import com.jada.xml.sitemap.Url;
import com.jada.xml.sitemap.UrlSet;

import java.util.Hashtable;

public class SiteMap {
	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	String domainName = null;
	String port = null;
	Vector<Url> urlSet = new Vector<Url>();
	Hashtable<String, String> keys = new Hashtable<String, String>();
	String topCatNaturalKey = "";
	
	public SiteMap(String domainName, String port) {
		this.domainName = domainName;
		this.port = port;
	}

	public String generate() throws Exception {
		String result = null;
    	for (SiteDomain siteDomain : getSiteDomains(domainName, port)) {
    		Menu menus[] = getMenuItems(siteDomain);
    		for (Menu menu : menus) {
    			String menuType = menu.getMenuType();
    			if (menuType.equals(Constants.MENU_CONTACTUS)) {
    				generateContactus(siteDomain);
    			}
    			else if (menuType.equals(Constants.MENU_CONTENT)) {
    				if (menu.getContent() != null) {
    					generateContent(menu.getContent(), siteDomain);
    				}
    			}
    			else if (menuType.equals(Constants.MENU_HOME)) {
    				generateHome(siteDomain);
    			}
    			else if (menuType.equals(Constants.MENU_ITEM)) {
    				if (menu.getItem() != null) {
    					generateItem(menu.getItem(), siteDomain);
    				}
    			}
    			else if (menuType.equals(Constants.MENU_SECTION)) {
    				if (menu.getCategory() != null) {
	    				topCatNaturalKey = menu.getCategory().getCatNaturalKey();
	    				generateCategory(menu.getCategory(), siteDomain);
    				}
    			}
    			else if (menuType.equals(Constants.MENU_SIGNIN)) {
    				generateSignin(siteDomain);
    			}
    			else if (menuType.equals(Constants.MENU_SIGNOUT)) {
    				
    			}
    			else if (menuType.equals(Constants.MENU_STATIC_URL)) {
    				if (menu.getMenuUrl() != null) {
    					generateStaticURL(menu.getMenuUrl(), siteDomain);
    				}
    			}
    		}
    	}
    	
    	Url urls[] = new Url[urlSet.size()];
    	urlSet.copyInto(urls);
    	UrlSet urlSet = new UrlSet();
    	urlSet.setUrl(urls);
    	
		Mapping mapping = new Mapping();
		InputSource input = new InputSource(getClass().getResourceAsStream("/com/jada/xml/sitemap/UrlSetMapping.xml"));
		mapping.loadMapping(input);
		
		XMLContext context = new XMLContext();
		context.addMapping(mapping);
		
		StringWriter writer = new StringWriter();
    	Marshaller marshaller = context.createMarshaller();
    	marshaller.setWriter(writer);
    	marshaller.marshal(urlSet);
    	result = writer.toString();
    	
    	return result;
	}
	
	private void generateContactus(SiteDomain siteDomain) throws Exception {
		for (SiteProfile siteProfile : siteDomain.getSiteProfiles()) {
			String urlLoc = getPrefix() + "/web/fe/" + 
							siteDomain.getSiteDomainPrefix() + "/" +
							siteProfile.getSiteProfileClass().getSiteProfileClassName() + "/" + 
							"contactus";
			Url url = generateUrl(urlLoc, null);
			urlSet.add(url);
		}
	}
	
	private void generateStaticURL(String staticURL, SiteDomain siteDomain) throws Exception {
		String urlLoc = staticURL;
		Url url = generateUrl(urlLoc, null);
		urlSet.add(url);
	}
	
	private void generateSignin(SiteDomain siteDomain) throws Exception {
		for (SiteProfile siteProfile : siteDomain.getSiteProfiles()) {
			String urlLoc = getSecurePrefix(siteDomain) + "/myaccount/login/myAccountLogin.do?process=start" + 
							"&prefix=" + siteDomain.getSiteDomainPrefix() +
							"&langName=" + siteProfile.getSiteProfileClass().getSiteProfileClassName();
			Url url = generateUrl(urlLoc, null);
			urlSet.add(url);
		}
	}
	
	private void generateHome(SiteDomain siteDomain) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String key = "home:" + siteDomain.getSiteDomainId();
		if (!isExist(key)) {		
			for (SiteProfile siteProfile : siteDomain.getSiteProfiles()) {
				String urlLoc = getPrefix() + "/web/fe/" + 
								siteDomain.getSiteDomainPrefix() + "/" +
								siteProfile.getSiteProfileClass().getSiteProfileClassName() + "/" + 
								"home";
				Url url = generateUrl(urlLoc, null);
				urlSet.add(url);
			}
			keys.put(key, "");
		}

		HomePage homePage = siteDomain.getHomePage();
		for (HomePageDetail homePageDetail : homePage.getHomePageDetails()) {
			if (homePageDetail.getContent() != null) {
				generateContent(homePageDetail.getContent(), siteDomain);
			}
			if (homePageDetail.getItem() != null) {
				generateItem(homePageDetail.getItem(), siteDomain);
			}
		}
		
		int recordCount = Constants.TEMPLATE_MODULE_DISPLAY_SIZE;
		SiteDomainParamBean siteDomainParamBean = SiteDomainDAO.getSiteDomainParamBean(siteDomain.getSiteDomainLanguage(), null);
		if (!Format.isNullOrEmpty(siteDomainParamBean.getModuleDisplaySize())) {
			recordCount = Format.getInt(siteDomainParamBean.getModuleDisplaySize());
		}
		
		String sql = NamedQuery.getInstance().getQuery("content.topRated");
		Query query = em.createQuery(sql);
		query.setMaxResults(recordCount);
		query.setParameter("siteId", siteDomain.getSite().getSiteId());
		query.setParameter("siteDomainId", siteDomain.getSiteDomainId());
		Iterator<?> iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			Long contentId = (Long) iterator.next();
			Content content = ContentDAO.load(siteDomain.getSite().getSiteId(), contentId);
			generateContent(content, siteDomain);
		}
		
		sql = NamedQuery.getInstance().getQuery("content.mostViewed");
		query = em.createQuery(sql);
		query.setMaxResults(recordCount);
		query.setParameter("siteId", siteDomain.getSite().getSiteId());
		query.setParameter("siteDomainId", siteDomain.getSiteDomainId());
		iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			Long contentId = (Long) iterator.next();
			Content content = ContentDAO.load(siteDomain.getSite().getSiteId(), contentId);
			generateContent(content, siteDomain);
		}
		
		sql = NamedQuery.getInstance().getQuery("item.mostPopular");
		query = em.createQuery(sql);
		query.setMaxResults(recordCount);
		query.setParameter("siteId", siteDomain.getSite().getSiteId());
		query.setParameter("catSiteDomainId", siteDomain.getSiteDomainId());
		iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			Long itemId = (Long) iterator.next();
			Item item = ItemDAO.load(siteDomain.getSite().getSiteId(), itemId);
			generateItem(item, siteDomain);
		}
	}
	
	private void generateContent(Content content, SiteDomain siteDomain) throws Exception {
		String key = "content:" + siteDomain.getSiteDomainId() + "," + content.getContentId();
		if (isExist(key)) {
			return;
		}
		
		if (!ContentDAO.isPublished(content)) {
			return;
		}
		
		for (SiteProfile siteProfile : siteDomain.getSiteProfiles()) {
			String urlLoc = getPrefix() + "/web/fe/" + 
							siteDomain.getSiteDomainPrefix() + "/" +
							siteProfile.getSiteProfileClass().getSiteProfileClassName() + "/" + 
							"content/" + content.getContentNaturalKey();
			Url url = generateUrl(urlLoc, content.getRecUpdateDatetime());
			urlSet.add(url);
			
			urlLoc = getPrefix() + "/web/fe/" + 
			siteDomain.getSiteDomainPrefix() + "/" +
			siteProfile.getSiteProfileClass().getSiteProfileClassName() + "/" + 
			"contentComment/" + content.getContentNaturalKey();
			url = generateUrl(urlLoc, content.getRecUpdateDatetime());
			urlSet.add(url);

			keys.put(key, "");
		}

		for (Content c : content.getContentsRelated()) {
			generateContent(c, siteDomain);
		}
	}
	
	private void generateItem(Item item, SiteDomain siteDomain) {
		String key = "item:" + siteDomain.getSiteDomainId() + "," + item.getItemId();
		if (isExist(key)) {
			return;
		}
		
		if (!ItemDAO.isPublished(item)) {
			return;
		}
		
		for (SiteProfile siteProfile : siteDomain.getSiteProfiles()) {
			String urlLoc = getPrefix() + "/web/fe/" + 
							siteDomain.getSiteDomainPrefix() + "/" +
							siteProfile.getSiteProfileClass().getSiteProfileClassName() + "/" + 
							"item/" + item.getItemId();
			Url url = generateUrl(urlLoc, item.getRecUpdateDatetime());
			urlSet.add(url);
			
			urlLoc = getPrefix() + "/web/fe/" + 
			siteDomain.getSiteDomainPrefix() + "/" +
			siteProfile.getSiteProfileClass().getSiteProfileClassName() + "/" + 
			"itemComment/" + item.getItemId();
			url = generateUrl(urlLoc, null);
			urlSet.add(url);

			keys.put(key, "");
		}
		
		for (Item i : item.getItemsUpSell()) {
			generateItem(i, siteDomain);
		}
		for (Item i : item.getItemsCrossSell()) {
			generateItem(i, siteDomain);
		}
		for (Item i : item.getItemsRelated()) {
			generateItem(i, siteDomain);
		}
	}
	
	private void generateCategory(Category category, SiteDomain siteDomain) throws Exception {
		String key = "category:" + siteDomain.getSiteDomainId() + "," + category.getCatId();
		if (isExist(key)) {
			return;
		}
		
		if (!CategoryDAO.isPublished(category)) {
			return;
		}
		
		for (SiteProfile siteProfile : siteDomain.getSiteProfiles()) {
			String urlLoc = getPrefix() + "/web/fe/" + 
							siteDomain.getSiteDomainPrefix() + "/" +
							siteProfile.getSiteProfileClass().getSiteProfileClassName() + "/" + 
							"category/" + topCatNaturalKey + "/" +
							category.getCatNaturalKey();
			Url url = generateUrl(urlLoc, null);
			urlSet.add(url);
			keys.put(key, "");
		}
		
		
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String sql = "select  item " +
					 "from    Item item " +
					 "left    join item.categories category " +
					 "where   item.site.siteId = :siteId " +
					 "and     item.published = 'Y' " +
					 "and     item.itemSellable = 'Y' " +
					 "and	  item.itemTypeCd in ('01','02','04','05') " +
					 "and	  curdate() between item.itemPublishOn and item.itemExpireOn " +
					 "and     category.catId = :catId";
		Query query = em.createQuery(sql);
		query.setParameter("siteId", siteDomain.getSite().getSiteId());
		query.setParameter("catId", category.getCatId());
		Iterator<?> iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			Item item = (Item) iterator.next();
			generateItem(item, siteDomain);
		}
		
		sql = "select  content " +
			  "from    Content content " +
			  "left    join content.categories category " +
			  "where   content.site.siteId = :siteId " +
			  "and     content.published = 'Y' " +
			  "and	   curdate() between content.contentPublishOn and content.contentExpireOn " +
			  "and     category.catId = :catId";
		query = em.createQuery(sql);
		query.setParameter("siteId", siteDomain.getSite().getSiteId());
		query.setParameter("catId", category.getCatId());
		iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			Content content = (Content) iterator.next();
			generateContent(content, siteDomain);
		}
		
		for (Category c : category.getCategoryChildren()) {
			generateCategory(c, siteDomain);
		}

	}

	private String getPrefix() {
		String value = "http://" + domainName;
		if (port.length() > 0 && !port.equals("80")) {
			value += ":" + port;
		}
		value += "/jada";
		return value;
	}
	
	private String getSecurePrefix(SiteDomain siteDomain) {
		SiteDomain masterDomain = null;
		masterDomain = siteDomain;
		
		if (siteDomain.getSite().getShareInventory() == Constants.VALUE_YES) {
			masterDomain = siteDomain.getSite().getSiteDomainDefault();
		}
		if (masterDomain.getSiteSslEnabled() != Constants.VALUE_YES) {
			return getPrefix();
		}
		String value = "https://" + domainName;
		String port = masterDomain.getSiteSecurePortNum();
		if (port.length() > 0 && !port.equals("443")) {
			value += ":" + port;
		}
		value += "/jada";
		return value;
	}
	
	private Url generateUrl(String value, Date date) {
		Url url = new Url();
		url.setLoc(value);
		
		if (date != null) {
			String text = dateFormatter.format(date);
			String lastMod = text.substring(0, 22) + ":" + text.substring(22);
			url.setLastmod(lastMod);
		}
		
		return url;
	}
	
	private boolean isExist(String key) {
		Object object = keys.get(key);
		if (object == null) {
			return false;
		}
		return true;
	}
	
	private Menu[] getMenuItems(SiteDomain siteDomain) throws Exception {
		Vector<Menu> vector = new Vector<Menu>();
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String sql = "from  Menu menu " +
    				 "where siteDomain = :siteDomain " +
    				 "and   published = 'Y'";
    	Query query = em.createQuery(sql);
    	query.setParameter("siteDomain", siteDomain);
    	Iterator<?> iterator = query.getResultList().iterator();
    	while (iterator.hasNext()) {
    		Menu menu = (Menu) iterator.next();
    		vector.add(menu);
    	}
    	Menu menus[] = new Menu[vector.size()];
    	vector.copyInto(menus);
		return menus;
	}
	
	public SiteDomain[] getSiteDomains(String domainName, String port) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Vector<SiteDomain> vector = new Vector<SiteDomain>();
    	
    	Query query = em.createQuery("from SiteDomain siteDomain");
    	Iterator<?> iterator = query.getResultList().iterator();
    	while (iterator.hasNext()) {
    		SiteDomain siteDomain = (SiteDomain) iterator.next();
    		if (!siteDomain.getSiteDomainName().equals(domainName)) {
    			continue;
    		}
    		String sitePublicPortNum = "80";
    		if (siteDomain.getSitePublicPortNum().trim().length() != 0) {
    			sitePublicPortNum = siteDomain.getSitePublicPortNum();
    		}
    		if (port.trim().length() == 0) {
    			port = "80";
    		}
    		if (!port.equals(sitePublicPortNum)) {
    			continue;
    		}
    		vector.add(siteDomain);
    	}
		SiteDomain siteDomains[] = new SiteDomain[vector.size()];
		vector.copyInto(siteDomains);
		return siteDomains;
	}

/*
	public static void main(String[] args) {
		SiteMap siteMap = new SiteMap("demo1.jadasite.com", "8080");
		try {
			EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
			connection.init();
			connection.getCurrentSession().beginTransaction();
			String result = siteMap.generate();
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/
}
