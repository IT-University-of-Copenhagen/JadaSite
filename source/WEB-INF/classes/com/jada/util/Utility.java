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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.EntityManager;

import au.com.bytecode.opencsv.CSVWriter;

import com.jada.util.JSONEscapeObject;

import com.jada.admin.AdminAction;
import com.jada.dao.ContentImageDAO;
import com.jada.dao.ItemImageDAO;
import com.jada.dao.MenuDAO;
import com.jada.dao.CategoryDAO;
import com.jada.dao.SiteDomainDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Category;
import com.jada.jpa.entity.CategoryLanguage;
import com.jada.jpa.entity.ContentImage;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.Currency;
import com.jada.jpa.entity.ItemImage;
import com.jada.jpa.entity.Language;
import com.jada.jpa.entity.Menu;
import com.jada.jpa.entity.MenuLanguage;
import com.jada.jpa.entity.Sequence;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.SiteDomainLanguage;
import com.jada.jpa.entity.SiteProfile;
import com.jada.jpa.entity.State;
import com.jada.jpa.entity.User;
import com.jada.order.cart.ItemEligibleTierPrice;
import com.jada.order.payment.gateway.PayPalEngine;
import com.jada.system.ApplicationGlobal;
import com.jada.ui.dropdown.DropDownMenu;
import com.jada.util.Constants;

public class Utility {
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	static String lastOrderNum = "-";
	static String lastCreditNum = "-";
	static String lastVoidOrderNum = "-";
	static int HTTP_TIMEOUT = 30000;
    final static byte transparent[] = {0x47, 0x49, 0x46, 0x38, 0x39, 0x61, 0x01, 0x00, 0x01, 0x00, (byte) 0x80, 0x00, 0x00, (byte) 0xff, (byte) 0xff, (byte) 0xff,
    	0x00, 0x00, 0x00, 0x21, (byte) 0xf9, 0x04, 0x01, 0x00, 0x00, 0x00, 0x00, 0x2c, 0x00, 0x00, 0x00, 0x00,
    	0x01, 0x00, 0x01, 0x00, 0x00, 0x02, 0x02, 0x44, 0x01, 0x00, 0x3b};


/*
	static public boolean isSiteProfileClassDefault(SiteProfileClass siteProfileClass) {
		SiteDomain siteDomain = siteProfile.getSiteDomain();
		if (!siteDomain.getSiteProfileDefault().getSiteProfileId().equals(siteProfile.getSiteProfileId())) {
			return false;
		}
		Site site = siteDomain.getSite();
		if (!site.getSiteDomainDefault().getSiteDomainId().equals(siteDomain.getSiteDomainId())) {
			return false;
		}
		return true;
	}
*/
	static public SiteProfile getSiteProfileDefault(Site site) {
		return site.getSiteDomainDefault().getSiteProfileDefault();
	}

    static public String formatCategoryName(String siteId, Long catId, Long siteProfileClassId, Long siteProfileClassDefaultId) throws Exception {
    	String categoryString = "";
        while (true) {
            Category category = CategoryDAO.load(siteId, catId);
            catId = category.getCategoryParent().getCatId();
            if (catId == null) {
            	break;
            }
			CategoryLanguage categoryLanguage = null;
			for (CategoryLanguage language : category.getCategoryLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassDefaultId)) {
					categoryLanguage = language;
				}
			}
			
            if (categoryString.length() > 0) {
            	categoryString = " - " + categoryString;
            }
            String catShortTitle = categoryLanguage.getCatShortTitle();
        	boolean found = false;
        	Iterator<?> iterator = category.getCategoryLanguages().iterator();
        	while (iterator.hasNext()) {
        		categoryLanguage = (CategoryLanguage) iterator.next();
        		if (categoryLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
        			found = true;
        		}
        	}
        	if (found) {
        		if (categoryLanguage.getCatShortTitle() != null) {
        			catShortTitle = categoryLanguage.getCatShortTitle();
        		}
        	}
            categoryString = catShortTitle + categoryString;
        }
     	return categoryString;
    }
    
    /*****/

    static public DropDownMenu[] makeMenuTreeList(Long siteDomainId, Long siteProfileClassId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String sql = "from   Menu menu " +
    				 "where  menu.siteDomain.siteDomainId = :siteDomainId " +
    				 "and    menu.menuParent is null " + 
    				 "order  by menu.seqNum";
    	Query query = em.createQuery(sql);
    	query.setParameter("siteDomainId", siteDomainId);
     	Iterator<?> iterator = query.getResultList().iterator();
     	Vector<DropDownMenu> menuVector = new Vector<DropDownMenu>();
     	menuVector.add(makeMenuTree(siteDomainId, Constants.MENUSET_MAIN, siteProfileClassId));
     	menuVector.add(makeMenuTree(siteDomainId, Constants.MENUSET_SECONDARY, siteProfileClassId));
    	while (iterator.hasNext()) {
    		Menu menu = (Menu) iterator.next();
    		if (menu.getMenuSetName().equals(Constants.MENUSET_MAIN)) {
    			continue;
    		}
    		if (menu.getMenuSetName().equals(Constants.MENUSET_SECONDARY)) {
    			continue;
    		}
    		menuVector.add(makeMenuTree(siteDomainId, menu.getMenuSetName(), siteProfileClassId));
    	}
    	DropDownMenu ddmList[] = new DropDownMenu[menuVector.size()];
    	menuVector.copyInto(ddmList);
    	return ddmList;
    }
    
    static public DropDownMenu makeMenuTree(Long siteDomainId, String menuSetName, Long siteProfileClassId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String sql = "select  menu " +
    				 "from    Menu menu " +
    				 "where   menu.siteDomain.siteDomainId = :siteDomainId " +
    				 "and     menu.menuSetName = :menuSetName " +
    				 "and     menu.menuParent is null " +
    				 "order   by menu.seqNum ";
    	Query query = em.createQuery(sql);
    	query.setParameter("siteDomainId", siteDomainId);
    	query.setParameter("menuSetName", menuSetName);
    	Iterator<?> iterator = query.getResultList().iterator();
    	Menu menu = null;
    	if (iterator.hasNext()) {
    		menu = (Menu) iterator.next();
     	}
        DropDownMenu menus[] = makeMenu(siteDomainId, menuSetName, menu.getMenuId(), siteProfileClassId, menu.getSiteDomain().getSite().getSiteProfileClassDefault().getSiteProfileClassId());
        DropDownMenu ddm = new DropDownMenu();
        ddm.setMenuName(menu.getMenuSetName());
        ddm.setMenuKey(Format.getLong(menu.getMenuId()));
        ddm.setMenuItems(menus);
        
    	return ddm;
    }
    

    static public DropDownMenu[] makeMenu(Long siteDomainId, String menuSetName, Long menuParentId, Long siteProfileClassId, Long siteProfileClassDefaultId) throws Exception {
    	DropDownMenu menus[] = null;
    	Vector<DropDownMenu> menuList = new Vector<DropDownMenu>();
    	
    	SiteDomain siteDomain = SiteDomainDAO.load(siteDomainId);
    	Menu parent = MenuDAO.load(siteDomain.getSite().getSiteId(), menuParentId);
    	for (Menu menu : parent.getMenuChildren()) {
    		MenuLanguage defaultMenuLanguage = null;
    		for (MenuLanguage language : menu.getMenuLanguages()) {
    			if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassDefaultId)) {
    				defaultMenuLanguage = language;
    			}
    		}
    		
       		DropDownMenu ddm = new DropDownMenu();
       		ddm.setMenuKey(Format.getLong(menu.getMenuId()));
   			ddm.setMenuName(defaultMenuLanguage.getMenuName());
       		if (siteProfileClassId != siteProfileClassDefaultId) {
       			Iterator<?> menuLanguages = menu.getMenuLanguages().iterator();
       			boolean found = false;
       			MenuLanguage menuLanguage = null;
       			while (menuLanguages.hasNext()) {
       		        menuLanguage = (MenuLanguage) menuLanguages.next();
       		        if (menuLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
       		        	found = true;
       		        	break;
       		        }
       			}
       			if (found) {
       				if (menuLanguage.getMenuName() != null) {
       					ddm.setMenuName(menuLanguage.getMenuName());
       				}
       			}
       		}
    		if (menu.getMenuId() != null) {
    			DropDownMenu childMenus[] = makeDdmMenu(siteDomainId, menuSetName, menu.getMenuId(), siteProfileClassId, siteProfileClassDefaultId);
           		ddm.setMenuItems(childMenus);
    		}
    		menuList.add(ddm);	
    	}
    	menus = new DropDownMenu[menuList.size()];
    	menuList.copyInto(menus);
    	return menus;
    }
    
    /*****/
    
    static public JSONEscapeObject makeJSONMenuTree(Long siteDomainId, Long siteProfileClassId, boolean siteProfileClassDefault) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	JSONEscapeObject object = new JSONEscapeObject();
    	String sql = "select  menu " +
		 			 "from    Menu menu " +
		 			 "where   menu.siteDomain.siteDomainId = :siteDomainId " +
		 			 "and     menu.menuParent is null " +
		 			 "order   by menu.menuSetName ";    	
    	Query query = em.createQuery(sql);
    	query.setParameter("siteDomainId", siteDomainId);
    	Iterator<?> iterator = query.getResultList().iterator();
    	Vector<JSONEscapeObject> menuSetVector = new Vector<JSONEscapeObject>();
    	while (iterator.hasNext()) {
    		Menu menu = (Menu) iterator.next();
    		JSONEscapeObject menuSetObject = makeJSONMenuTreeNode(siteDomainId, menu.getMenuId(), siteProfileClassId, siteProfileClassDefault);
    		menuSetVector.add(menuSetObject);
    	}
    	object.put("menuSets", menuSetVector);
    	
    	return object;
    }

    static public JSONEscapeObject makeJSONMenuTreeNode(Long siteDomainId, Long menuId, Long siteProfileClassId, boolean siteProfileClassDefault) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	JSONEscapeObject JSONEscapeObject = new JSONEscapeObject();
    	SiteDomain siteDomain = SiteDomainDAO.load(siteDomainId);
    	Site site = siteDomain.getSite();
    	Menu menu = MenuDAO.load(site.getSiteId(), menuId);
		MenuLanguage defaultMenuLanguage = null;
		for (MenuLanguage language : menu.getMenuLanguages()) {
			if (language.getSiteProfileClass().getSiteProfileClassId().equals(site.getSiteProfileClassDefault().getSiteProfileClassId())) {
				defaultMenuLanguage = language;
				break;
			}
		}
		
    	if (menu.getMenuParent() == null) {
	    	JSONEscapeObject.put("menuId", menu.getMenuId());
	    	JSONEscapeObject.put("menuSetName", menu.getMenuSetName());
    	}
    	else{
	    	JSONEscapeObject.put("menuId", menu.getMenuId());
	    	String menuName = defaultMenuLanguage.getMenuName();
	    	if (!siteProfileClassDefault) {
	    		for (MenuLanguage menuLanguage : menu.getMenuLanguages()) {
	    			if (menuLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
	    				if (menuLanguage.getMenuName() != null) {
	    					menuName = menuLanguage.getMenuName();
	    				}
	    				break;
	    			}
	    		}
	    	}
	    	JSONEscapeObject.put("menuName", menuName);
    	}
     	
    	Vector<JSONEscapeObject> vector = new Vector<JSONEscapeObject>();
    	String sql = "from   Menu menu " + 
    				 "where  menu.menuParent.menuId = :menuId " +
    				 "order	 by menu.seqNum";
    	Query query = em.createQuery(sql);
    	query.setParameter("menuId", menu.getMenuId());
    	Iterator<?> iterator = query.getResultList().iterator();
    	while (iterator.hasNext()) {
    		Menu child = (Menu) iterator.next();
    		JSONEscapeObject object = makeJSONMenuTreeNode(siteDomainId, child.getMenuId(), siteProfileClassId, siteProfileClassDefault);
    		vector.add(object);
    	}
/*
    	for (Menu child : menu.getMenuChildren()) {
    		JSONEscapeObject object = makeJSONMenuTreeNode(siteDomainId, child.getMenuId(), siteProfileClassId, siteProfileClassDefault);
    		vector.add(object);
    	}
 */
    	JSONEscapeObject.put("menus", vector);
    	return JSONEscapeObject;
    }
    
    static public JSONEscapeObject makeJSONCategoryTree(String siteId, Long siteProfileClassId, boolean siteProfileClassDefault) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	JSONEscapeObject object = new JSONEscapeObject();
    	String sql = "select   category " +
    				 "from     Category category " +
    				 "where	   category.site.siteId = :siteId " +
    				 "and      category.categoryParent is null";
    	Query query = em.createQuery(sql);
    	query.setParameter("siteId", siteId);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		Category category = (Category) iterator.next();
    		return makeJSONCategoryTreeNode(siteId, category.getCatId(), siteProfileClassId, siteProfileClassDefault);
    	}
    	
    	return object;
    }
    
    static public JSONEscapeObject makeJSONCategoryTreeNode(String siteId, Long catId, Long siteProfileClassId, boolean siteProfileClassDefault) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	JSONEscapeObject JSONEscapeObject = new JSONEscapeObject();
    	Category category = CategoryDAO.load(siteId, catId);
    	JSONEscapeObject.put("catId", category.getCatId());
    	String catShortTitle = category.getCategoryLanguage().getCatShortTitle();
    	if (!siteProfileClassDefault) {
    		for (CategoryLanguage language : category.getCategoryLanguages()) {
    			if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
    				if (language.getCatShortTitle() != null) {
    					catShortTitle = language.getCatShortTitle();
    				}
    			}
    		}
    	}
    	JSONEscapeObject.put("catShortTitle", catShortTitle);
    	
    	Vector<JSONEscapeObject> vector = new Vector<JSONEscapeObject>();
    	String sql = "from   Category category " +
    				 "where  category.categoryParent.catId = :catId " +
    				 "order  by category.seqNum";
    	Query query = em.createQuery(sql);
    	query.setParameter("catId", category.getCatId());
    	Iterator<?> iterator = query.getResultList().iterator();
    	while (iterator.hasNext()) {
    		Category child = (Category) iterator.next();
    		JSONEscapeObject object = makeJSONCategoryTreeNode(siteId, child.getCatId(), siteProfileClassId, siteProfileClassDefault);
    		vector.add(object);
    	}
/*
    	for (Category child : category.getCategoryChildren()) {
    		JSONEscapeObject object = makeJSONCategoryTreeNode(siteId, child.getCatId(), siteProfileClassId, siteProfileClassDefault);
    		vector.add(object);
    	}
*/
    	JSONEscapeObject.put("categories", vector);
    	return JSONEscapeObject;
    }
    
    static public DropDownMenu makeCategoryTree(String siteId, Long siteProfileClassId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
   	
    	String sql = "select category " +
    				 "from   Category category " + 
    	             "left   join category.site site " +
    	             "where  site.siteId = :siteId " +
    	             "and    category.categoryParentId is null " +
    	             "order  by category.seqNum";
    	Query query = em.createQuery(sql);
    	query.setParameter("siteId", siteId);
    	Iterator<?> iterator = query.getResultList().iterator();
    	Category category = null;
    	if (iterator.hasNext()) {
    		category = (Category) iterator.next();
    	}
    	Long siteProfileClassDefaultId = category.getSite().getSiteProfileClassDefault().getSiteProfileClassId();
		CategoryLanguage categoryLanguage = null;
		for (CategoryLanguage language : category.getCategoryLanguages()) {
			if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassDefaultId)) {
				categoryLanguage = language;
			}
		}
		
    	DropDownMenu categories[] = null;
		categories = makeCategoryTreeItem(siteId, category.getCatId(), siteProfileClassId, siteProfileClassDefaultId);
		
    	DropDownMenu ddm = new DropDownMenu();
    	ddm.setMenuKey(category.getCatId().toString());
    	ddm.setMenuName(categoryLanguage.getCatShortTitle());
    	ddm.setMenuItems(categories);

    	return ddm;
    }
    
    static public DropDownMenu[] makeCategoryTreeItem(String siteId, Long categoryParentId, Long siteProfileClassId, Long siteProfileClassDefaultId) throws Exception {
    	DropDownMenu categories[] = null;
    	Vector<DropDownMenu> categoryList = new Vector<DropDownMenu>();
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String sql = "select category " +
		 			 "from   Category category " +
    				 "left   join category.site site " +
    	             "where  site.siteId = :siteId " +
    	             "and    category.categoryParentId = :categoryParentId " +
    				 "order  by category.seqNum";
    	Query query = em.createQuery(sql);
    	query.setParameter("siteId", siteId);
    	query.setParameter("categoryParentId", categoryParentId.longValue());
    	Iterator<?> iterator = query.getResultList().iterator();
    	while (iterator.hasNext()) {
    		Category category = (Category) iterator.next();
			CategoryLanguage categoryLanguage = null;
			for (CategoryLanguage language : category.getCategoryLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassDefaultId)) {
					categoryLanguage = language;
				}
			}
			
       		DropDownMenu childMenus[] = makeDdmCategory(siteId, category.getCatId(), siteProfileClassId, siteProfileClassDefaultId);
       		DropDownMenu ddm = new DropDownMenu();
       		ddm.setMenuKey(Format.getLong(category.getCatId()));
       		ddm.setMenuName(categoryLanguage.getCatShortTitle());
       		if (siteProfileClassId != siteProfileClassDefaultId) {
       			Iterator<?> categoryLanguages = category.getCategoryLanguages().iterator();
       			boolean found = false;
       			while (categoryLanguages.hasNext()) {
       				categoryLanguage = (CategoryLanguage) categoryLanguages.next();
       		        if (categoryLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
       		        	found = true;
       		        	break;
       		        }
       			}
       			if (found) {
       				if (categoryLanguage.getCatShortTitle() != null) {
       					ddm.setMenuName(categoryLanguage.getCatShortTitle());
       				}
       			}
       		}
       		ddm.setMenuItems(childMenus);
       		categoryList.add(ddm);
    	}
    	categories = new DropDownMenu[categoryList.size()];
    	categoryList.copyInto(categories);
    	return categories;
    }
    
    static public Long[] getCatIdTreeList(String siteId, Long parentId) throws Exception {
    	// TODO performance - cache
    	Vector<Long> list = new Vector<Long>();
    	Category parent = CategoryDAO.load(siteId, parentId);
    	getCatIdTreeListWorker(siteId, list, parent);
    	Long catIdList[] = new Long[list.size()];
    	list.copyInto(catIdList);
    	return catIdList;
    }
    
    static public void getCatIdTreeListWorker(String siteId, Vector<Long> list, Category parent) throws Exception {
    	list.add(parent.getCatId());
    	for (Category category : parent.getCategoryChildren()) {
//    		list.add(category.getCatId());
    		getCatIdTreeListWorker(siteId, list, category);
    	}
    	return;
    }
    
    /*****/
 /*
    static public DropDownMenuContainer[] makeDdmCategoryContainerList(String siteId) throws Exception {
    	DropDownMenuContainer list[] = new DropDownMenuContainer[1];
    	DropDownMenuContainer container = makeDdmCategoryContainer(siteId);
    	container.setMenuSetName("Home");
    	list[0] = container;
    	return list;
    }

    
    static public DropDownMenuContainer makeDdmCategoryContainer(String siteId) throws Exception {
    	DropDownMenuContainer container = new DropDownMenuContainer();
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from category in class Category where siteId = :siteId and category.categoryParentId is null order by seqNum");
    	query.setParameter("siteId", siteId);
    	Iterator<?> iterator = query.getResultList().iterator();
    	Category category = null;
    	if (iterator.hasNext()) {
    		category = (Category) iterator.next();
    	}
    	
    	DropDownMenu categories[] = null;
		categories = makeDdmCategory(siteId, category.getCatId());
        container.setMenuSetKey(category.getCatId().toString());
        container.setMenuSetName(category.getCatTitle());
        container.setMenuItems(categories);
    	return container;
    }
*/
    static public DropDownMenu[] makeDdmCategory(String siteId, Long categoryParentId, Long siteProfileClassId, Long siteProfileClassDefaultId) throws Exception {
    	DropDownMenu categories[] = null;
    	Vector<DropDownMenu> categoryList = new Vector<DropDownMenu>();
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String sql = "select category " +
		 			 "from   Category category " +
		 			 "left   join category.site site " +
		 			 "where  site.siteId = :siteId " +
		 			 "and    category.categoryParentId = :categoryParentId " +
		 			 "order  by category.seqNum";
    	Query query = em.createQuery(sql);
    	query.setParameter("siteId", siteId);
    	query.setParameter("categoryParentId", categoryParentId.longValue());
    	Iterator<?> iterator = query.getResultList().iterator();
    	while (iterator.hasNext()) {
    		Category category = (Category) iterator.next();
			CategoryLanguage categoryLanguage = null;
			for (CategoryLanguage language : category.getCategoryLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassDefaultId)) {
					categoryLanguage = language;
				}
			}
       		DropDownMenu childMenus[] = makeDdmCategory(siteId, category.getCatId(), siteProfileClassId, siteProfileClassDefaultId);
       		DropDownMenu ddm = new DropDownMenu();
       		ddm.setMenuKey(Format.getLong(category.getCatId()));
       		ddm.setMenuName(categoryLanguage.getCatShortTitle());
       		if (siteProfileClassId != siteProfileClassDefaultId) {
       			Iterator<?> categoryLanguages = category.getCategoryLanguages().iterator();
       			boolean found = false;
       			while (categoryLanguages.hasNext()) {
       				categoryLanguage = (CategoryLanguage) categoryLanguages.next();
       		        if (categoryLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
       		        	found = true;
       		        	break;
       		        }
       			}
       			if (found) {
       				if (categoryLanguage.getCatShortTitle() != null) {
       					ddm.setMenuName(categoryLanguage.getCatShortTitle());
       				}
       			}
       		}
       		ddm.setMenuItems(childMenus);
       		categoryList.add(ddm);
    	}
    	categories = new DropDownMenu[categoryList.size()];
    	categoryList.copyInto(categories);
    	return categories;
    }
/*  
    static public DropDownMenuContainer[] makeDdmMenuContainerList(String siteId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from menu in class Menu where menu.siteId = :siteId and menuParentId is null order by seqNum");
    	query.setParameter("siteId", siteId);
     	Iterator<?> iterator = query.getResultList().iterator();
     	Vector<DropDownMenuContainer> menuVector = new Vector<DropDownMenuContainer>();
    	while (iterator.hasNext()) {
    		Menu menu = (Menu) iterator.next();
    		DropDownMenuContainer container = makeDdmMenuContainer(siteId, menu.getMenuSetName());
    		container.setMenuSetName(menu.getMenuSetName());
    		container.setMenuSetKey(menu.getMenuId().toString());
    		menuVector.add(container);
    	}
    	DropDownMenuContainer ddmMenuContainerList[] = new DropDownMenuContainer[menuVector.size()];
    	menuVector.copyInto(ddmMenuContainerList);
    	return ddmMenuContainerList;
    }
    
    static public DropDownMenuContainer makeDdmMenuContainer(String siteId, String menuSetName) throws Exception {
    	DropDownMenuContainer container = new DropDownMenuContainer();
     
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from menu in class Menu where siteId = :siteId and menu.menuSetName = :menuSetName and menu.menuParentId is null order by seqNum ");
    	query.setParameter("siteId", siteId);
    	query.setParameter("menuSetName", menuSetName);
    	Iterator<?> iterator = query.getResultList().iterator();
    	Menu menu = null;
    	if (iterator.hasNext()) {
    		menu = (Menu) iterator.next();
     	}
    		
        DropDownMenu menus[] = makeDdmMenu(siteId, menuSetName, menu.getMenuId());
        container.setMenuItems(menus);
        
        container.setMenuItems(menus);
    	return container;
    }
*/

    static public DropDownMenu[] makeDdmMenu(Long siteDomainId, String menuSetName, Long menuParentId, Long siteProfileClassId, Long siteProfileClassDefaultId) throws Exception {
    	DropDownMenu menus[] = null;
    	Vector<DropDownMenu> menuList = new Vector<DropDownMenu>();
    	
    	SiteDomain siteDomain = SiteDomainDAO.load(siteDomainId);
    	Menu parent = MenuDAO.load(siteDomain.getSite().getSiteId(), menuParentId);
    	for (Menu menu : parent.getMenuChildren()) {
    		MenuLanguage defaultMenuLanguage = null;
    		for (MenuLanguage language : menu.getMenuLanguages()) {
    			if (language.getSiteProfileClass().getSiteProfileClassId().equals(menu.getSiteDomain().getSite().getSiteProfileClassDefault().getSiteProfileClassId())) {
    				defaultMenuLanguage = language;
    			}
    		}
    		
       		DropDownMenu ddm = new DropDownMenu();
       		ddm.setMenuKey(Format.getLong(menu.getMenuId()));
       		ddm.setMenuName(defaultMenuLanguage.getMenuName());
       		if (siteProfileClassId != siteProfileClassDefaultId) {
       			Iterator<?> menuLanguages = menu.getMenuLanguages().iterator();
       			boolean found = false;
       			MenuLanguage menuLanguage = null;
       			while (menuLanguages.hasNext()) {
       		        menuLanguage = (MenuLanguage) menuLanguages.next();
       		        if (menuLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
       		        	found = true;
       		        	break;
       		        }
       			}
       			if (found) {
       				if (menuLanguage.getMenuName() != null) {
       					ddm.setMenuName(menuLanguage.getMenuName());
       				}
       			}
       		}
       		if (menu.getMenuId() != null) {
    			DropDownMenu childMenus[] = makeDdmMenu(siteDomainId, menuSetName, menu.getMenuId(), siteProfileClassId, siteProfileClassDefaultId);
           		ddm.setMenuItems(childMenus);
    		}
    		menuList.add(ddm);
    	}
    	menus = new DropDownMenu[menuList.size()];
    	menuList.copyInto(menus);
    	return menus;
    }
    
    static public String formatMenuName(String siteId, Long menuId, Long siteProfileClassId) throws Exception {
    	String menuString = "";
        while (true) {
            Menu menu = MenuDAO.load(siteId, menuId);
            Long siteProfileClassDefaultId = menu.getSiteDomain().getSite().getSiteProfileClassDefault().getSiteProfileClassId();
    		MenuLanguage defaultMenuLanguage = null;
    		for (MenuLanguage language : menu.getMenuLanguages()) {
    			if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassDefaultId)) {
    				defaultMenuLanguage = language;
    			}
    		}
    		
            if (menuString.length() > 0) {
            	menuString = " - " + menuString;
            }
            if (menu.getMenuParent() == null) {
            	menuString = menu.getMenuSetName() + menuString;
            	break;
            }
            String menuName = defaultMenuLanguage.getMenuName();
            if (siteProfileClassId != siteProfileClassDefaultId) {
            	boolean found = false;
            	MenuLanguage menuLanguage = null;
            	Iterator<?> iterator = menu.getMenuLanguages().iterator();
            	while (iterator.hasNext()) {
            		menuLanguage = (MenuLanguage) iterator.next();
            		if (menuLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
            			found = true;
            		}
            	}
            	if (found) {
            		if (menuLanguage.getMenuName() != null) {
            			menuName = menuLanguage.getMenuName();
            		}
            	}
            }
            menuString = menuName + menuString;
            menuId = menu.getMenuParent().getMenuId();
        }
    	return menuString;
    }
    
    static public String getPriceDisplay(Float itemPrice, Integer itemMultQty, Float itemMultPrice) {
    	String display = Format.getFloatObj(itemPrice);
    	if (itemMultQty != null && itemMultQty.intValue() != 0) {
    		display += ", " + Format.getIntObj(itemMultQty) + "/" + Format.getFloatObj(itemMultPrice);
    	}
    	return display;
    }
    
    static public String getItemPriceDisplay(ItemEligibleTierPrice tierPrice) {
    	return "deprecated";
/*
    	Float itemPrice = item.getItemPrice();
    	Integer itemMultQty = new Integer(0);
    	Float itemMultPrice = new Float(0);
    	if (item.getItemMultQty() != null) {
	    	itemMultQty = item.getItemMultQty();
	    	itemMultPrice = item.getItemMultPrice();
	    }
    	if (isSpecialOn(item)) {
			itemPrice = item.getItemSpecPrice();
			itemMultQty = new Integer(0);
			itemMultPrice = new Float(0);
	    	if (item.getItemSpecMultQty() != null) {
		    	itemMultQty = item.getItemSpecMultQty();
		    	itemMultPrice = item.getItemSpecMultPrice();
		    }
    	}
    	return getPriceDisplay(itemPrice, itemMultQty, itemMultPrice);
*/
    }

/*
    static public ItemPriceInfo getItemPriceTotal(Float itemPriceObj, Integer itemMultQtyObj, Float itemMultPriceObj,
    		                              Float itemSpecPriceObj, Integer itemSpecMultQtyObj, Float itemSpecMultPriceObj,
    		                              ShoppingCartTax itemTaxInfo[], int qty) {
    	ItemPriceInfo itemPriceInfo = new ItemPriceInfo();
    	
    	float itemPrice = itemPriceObj.floatValue();
    	itemPriceInfo.setItemPrice(new Float(itemPrice));
    	int itemMultQty = 0;
    	float itemMultPrice = 0;
    	if (itemMultQtyObj != null) {
	    	itemMultQty = itemMultQtyObj.intValue();
	    	itemMultPrice = itemMultPriceObj.floatValue();
	    }
    	if (itemSpecPriceObj != null) {
			itemPrice = itemSpecPriceObj.floatValue();
			itemMultQty = 0;
			itemMultPrice = 0;
	    	if (itemSpecMultQtyObj != null) {
		    	itemMultQty = itemSpecMultQtyObj.intValue();
		    	itemMultPrice = itemSpecMultPriceObj.floatValue();
		    }
    	}
    	
		float taxTotal = 0;
    	if (itemMultQty == 0) {
    		itemPriceInfo.setItemPriceTotal(new Float(itemPrice * qty));
    		for (int i = 0; i < qty; i++) {
    			taxTotal = calculateTaxes(itemPrice, itemTaxInfo);
    		}
    		itemPriceInfo.setItemTaxTotal(taxTotal);
    		return itemPriceInfo;
    	}
    	
    	float price = 0;
    	while (qty >= itemMultQty) {
    		price += itemMultPrice;
    		qty -= itemMultQty;
    		taxTotal += calculateTaxes(itemMultPrice, itemTaxInfo);
    	}
    	price += itemPrice * qty;
    	for (int i = 0; i < qty; i++) {
    		taxTotal += calculateTaxes(itemPrice, itemTaxInfo);
    	}
    	
    	itemPriceInfo.setItemPriceTotal(new Float(price));
		itemPriceInfo.setItemTaxTotal(taxTotal);
    	return itemPriceInfo;	
    }
    
    static public float calculateTaxes(float itemPrice, ShoppingCartTax scTaxes[]) {
    	if (scTaxes == null) {
    		return 0;
    	}
    	float taxTotal = 0;
		for (int j = 0; j < scTaxes.length; j++) {
			float tax = 0;
			if (scTaxes[j].getTax().getTaxRate() != null) {
				tax = scTaxes[j].getTax().getTaxRate().floatValue();
			}
			float taxAmount = round(itemPrice * tax, 0) / 100;
			scTaxes[j].setTaxAmount(taxAmount + scTaxes[j].getTaxAmount().floatValue());
			taxTotal += taxAmount;
		}
		return taxTotal;
    }

    static public ItemPriceInfo getItemPriceTotal(Item item, ShoppingCartTax itemTaxInfo[], int qty) {
    	if (isSpecialOn(item)) {
    		return getItemPriceTotal(item.getItemPrice(),
	  				 item.getItemMultQty(),
	  				 item.getItemMultPrice(),
	  				 item.getItemSpecPrice(),
	  				 item.getItemSpecMultQty(),
	  				 item.getItemSpecMultPrice(),
	  				 itemTaxInfo,
	  				 qty);
    	}
    	else {
    		return getItemPriceTotal(item.getItemPrice(),
			         item.getItemMultQty(),
			         item.getItemMultPrice(),
			         null,
			         null,
			         null,
			         itemTaxInfo,
			         qty);
    	}
    }
    
    static public Float getItemPriceDiscount(Item item, int qty) {
    	float itemPrice = item.getItemPrice().floatValue();
    	if (isSpecialOn(item)) {
    		itemPrice = item.getItemSpecPrice().floatValue();
    	}
    	float itemTotal = itemPrice * qty;
    	ItemPriceInfo itemPriceInfo = getItemPriceTotal(item, null, qty);
    	float itemDiscount = itemTotal - itemPriceInfo.getItemPriceTotal();
    	return new Float(itemDiscount);
    }
*/
    static public Vector<String> getUserIdsForSite(String siteId) throws Exception {
    	Vector<String> vector = new Vector<String>();
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from User order by userId");

     	Iterator<?> iterator = query.getResultList().iterator();
     	while (iterator.hasNext()) {
     		User user = (User) iterator.next();
     		if (!user.getUserType().equals(Constants.USERTYPE_SUPER) && !user.getUserType().equals(Constants.USERTYPE_ADMIN)) {
     			boolean found = false;
     			Iterator<?> iteratorSite = user.getSites().iterator();
     			while (iteratorSite.hasNext()) {
     				Site site = (Site) iteratorSite.next();
     				if (site.getSiteId().equals(siteId)) {
     					found = true;
     				}
     			}
     			if (!found) {
     				continue;
     			}
     		}
     		vector.add(user.getUserId());
     	}
    	return vector;
    }
    
    static public Long getNextSequenceNum(Site site, String sequenceId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Object object = new Object();
    	Sequence sequence = null;
    	synchronized(object) {
	    	sequence = (Sequence) em.find(Sequence.class, sequenceId);
	    	if (sequence == null) {
	    		sequence = new Sequence();
	    		sequence.setSequenceId(sequenceId);
	    		sequence.setNextSequenceNum(Long.valueOf(0));
	    		sequence.setRecCreateBy(Constants.USERNAME_SYSTEM);
	    		sequence.setRecCreateDatetime(new Date());
	    		sequence.setRecUpdateBy(Constants.USERNAME_SYSTEM);
	    		sequence.setRecUpdateDatetime(new Date());
	    		sequence.setSite(site);
	    		em.persist(sequence);
	    	}
	    	else {
		    	em.lock(sequence, LockModeType.WRITE);
	    	}
    	}
    	Long sequenceNum = sequence.getNextSequenceNum();
    	sequenceNum = Long.valueOf(sequenceNum.longValue() + 1);
    	sequence.setNextSequenceNum(sequenceNum);
    	return sequenceNum;
    }
    
    // TODO Park - cannot be done in a clustered environment
    static public synchronized String generateOrderNum() {
    	String orderNum = "";
    	while (orderNum != lastOrderNum) {
	    	orderNum = dateFormat.format(new Date());
	    	lastOrderNum = orderNum;
    	}
    	return orderNum;
    }
    
    // TODO Park - cannot be done in a clustered environment
    static public synchronized String generateVoidOrderNum() {
    	String voidOrderNum = "";
    	while (voidOrderNum != lastVoidOrderNum) {
    		voidOrderNum = dateFormat.format(new Date());
    		lastVoidOrderNum = voidOrderNum;
    	}
    	return lastVoidOrderNum;
    }
    
    // TODO Park - cannot be done in a clustered environment
    static public synchronized String generateCreditNum() {
    	String creditNum = "";
    	while (creditNum != lastCreditNum) {
    		creditNum = dateFormat.format(new Date());
    		lastCreditNum = creditNum;
    	}
    	return creditNum;
    }
/*
    static public String getParam(Site site, String name) {
    	Iterator<?> iterator = site.getSiteParams().iterator();
    	while (iterator.hasNext()) {
    		SiteParam siteParam = (SiteParam) iterator.next();
    		if (siteParam.getSiteParamName().equals(name)) {
    			return siteParam.getSiteParamValue();
    		}
    	}
    	return null;
    }
    
    static public String getParam(Iterator<?> siteParamDAOs, String name) {
    	while (siteParamDAOs.hasNext()) {
    		SiteParamDAO siteParamDAO = (SiteParamDAO) siteParamDAOs.next();
    		if (siteParamDAO.getSiteParamName().equals(name)) {
    			return siteParamDAO.getSiteParamValue();
    		}
    	}
    	return null;
    }
*/
    static public String getCurrencyCode(String siteId, String currencyId) throws Exception {
    	String currencyCode = null;
    	
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String sql = "select currency " +
		 			 "from   Currency currency " +
		 			 "left   join currency.site site " +
		 			 "where  site.siteId = :siteId " +
		 			 "and    currency.currencyId = :currencyId ";
        Query query = em.createQuery(sql);
        query.setParameter("siteId", siteId);
        query.setParameter("currencyId", currencyId);
        List<?> list = query.getResultList();
        if (list.size() > 0) {
        	Currency currency = (Currency) list.iterator().next();
        	currencyCode = currency.getCurrencyCode();
        }

        return currencyCode;
    }
    
    static public String getLangName(String siteId, String langId) throws Exception {
    	String langName = null;
    	
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        Query query = em.createQuery("from Language where langId = :langId");
        query.setParameter("langId", langId);
        List<?> list = query.getResultList();
        if (list.size() > 0) {
        	Language language = (Language) list.iterator().next();
        	langName = language.getLangName();
        }

        return langName;
    }
    
    static public Country getCountryByCode(String siteId, String countryCode) throws Exception {
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        Country country = null;
    	String sql = "select country " +
		 			 "from   Country country " +
		 			 "left   join country.site site " +
		 			 "where  site.siteId = :siteId " +
		 			 "and    country.countryCode = :countryCode ";
        Query query = em.createQuery(sql);
        query.setParameter("siteId", siteId);
        query.setParameter("countryCode", countryCode);
        List<?> list = query.getResultList();
        if (list.size() > 0) {
        	country = (Country) list.iterator().next();
        }
        return country;
    }
    
    static public String getCountryName(String siteId, String countryCode) throws Exception {
    	Country country = getCountryByCode(siteId, countryCode);
    	return country.getCountryName();
    }
    
    static public State getStateByCode(String siteId, String stateCode) throws Exception {
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        State state = null;
    	String sql = "select state " +
		 			 "from   State state " +
		 			 "left   join state.country country " +
		 			 "left   join country.site site " +
		 			 "where  site.siteId = :siteId " +
		 			 "and    state.stateCode = :stateCode ";
        Query query = em.createQuery(sql);
        query.setParameter("siteId", siteId);
        query.setParameter("stateCode", stateCode);
        List<?> list = query.getResultList();
        if (list.size() > 0) {
        	state = (State) list.iterator().next();
        }
        return state;
    }
    
    static public State getStateByName(String siteId, String stateName) throws Exception {
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        State state = null;
    	String sql = "select state " +
		 			 "from   State state " +
		 			 "left   join state.country country " +
		 			 "left   join country.site site " +
		 			 "where  site.siteId = :siteId " +
		 			 "and    state.stateName = :stateName ";
        Query query = em.createQuery(sql);
        query.setParameter("siteId", siteId);
        query.setParameter("stateName", stateName);
        List<?> list = query.getResultList();
        if (list.size() > 0) {
        	state = (State) list.iterator().next();
        }
        return state;
    }
    
    static public State getStateByNameOrCode(String siteId, String stateNameOrCode) throws Exception {
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        State state = null;
    	String sql = "select state " +
		 			 "from   State state " +
		 			 "left   join state.country country " +
		 			 "left   join country.site site " +
		 			 "where  site.siteId = :siteId " +
		 			 "and    (state.stateName = :stateName or state.stateCode = :stateCode) ";
        Query query = em.createQuery(sql);
        query.setParameter("siteId", siteId);
        query.setParameter("stateName", stateNameOrCode);
        query.setParameter("stateCode", stateNameOrCode);
        List<?> list = query.getResultList();
        if (list.size() > 0) {
        	state = (State) list.iterator().next();
        }
        return state;
    }

    static public String getStateName(String siteId, String stateCode) throws Exception {
    	if (Format.isNullOrEmpty(stateCode)) {
    		return "";
    	}
    	State state = getStateByCode(siteId, stateCode);
    	return state.getStateName();
    }
    
    static public float round(Float value, int digit) {
    	return round(value.floatValue(), digit);
    }
    
    static public float round(float value, int digit) {
    	int power = 1;
    	float result;
    	for (int i = 0; i < digit; i++) {
    		power = power * 10;
    	}
    	result = (float) Math.round(value * power) / power;
    	return result;
    }
    
    static public byte[] httpGet(String url) throws Exception {
    	Logger logger = Logger.getLogger(Utility.class);
    	
    	logger.debug("getting: " + url);
    	byte response[] = null;
        URL u = new URL(url) ; 
        HttpURLConnection connection =  (HttpURLConnection) u.openConnection(); 
        connection.setConnectTimeout(HTTP_TIMEOUT);
        connection.setReadTimeout(HTTP_TIMEOUT);
        connection.setRequestProperty("Content-Type", "text/xml");
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();
        
        if (connection.getResponseCode() != 200) {
        	logger.error("Error calling performing HttpGet: " + connection.getResponseCode());
            throw new Exception("Error calling webservice: " + connection.getResponseCode());
        }
        
        int contentLength = connection.getContentLength();
        InputStream is = connection.getInputStream();

        response = new byte[contentLength];
        int totalread = 0;
        int count = 0;
        while (totalread < contentLength) {
            if (count > 0) {
                logger.debug("Reading chunk: " + count);
            }
            totalread += is.read(response, totalread, contentLength - totalread);
            if (++count > 500) {
                logger.error("Aborting read after 500 tries");
                break;
            }
        }
        logger.debug(response);
        is.close();

    	return response;
    }
    
    static public String callWebService(String serviceURL, String request) throws Exception {
    	Logger logger = Logger.getLogger(Utility.class);
    	
    	byte response[] = null;
        URL u = new URL(serviceURL) ; 
        HttpURLConnection connection =  (HttpURLConnection) u.openConnection(); 
        connection.setConnectTimeout(HTTP_TIMEOUT);
        connection.setReadTimeout(HTTP_TIMEOUT);
        connection.setRequestProperty("Content-Type", "text/xml");
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();
        OutputStream out = connection.getOutputStream();
        logger.debug(request);
        out.write(request.getBytes());
        out.close();
        
        if (connection.getResponseCode() != 200) {
        	logger.error(request);
        	logger.error("Error calling webservice: " + connection.getResponseCode());
            throw new Exception("Error calling webservice: " + connection.getResponseCode());
        }
        
        int contentLength = connection.getContentLength();
        InputStream is = connection.getInputStream();

        response = new byte[contentLength];
        int totalread = 0;
        int count = 0;
        while (totalread < contentLength) {
            if (count > 0) {
                logger.debug("Reading chunk: " + count);
            }
            totalread += is.read(response, totalread, contentLength - totalread);
            if (++count > 500) {
                logger.error("Aborting read after 500 tries");
                break;
            }
        }
        logger.debug(response);
        is.close();

    	return new String(response);
    }
    
    static public boolean isValidPassword(String password) {
    	if (password.length() < 8 || password.length() > 12) {
    		return false;
    	}
    	boolean hasDigit = false;
    	boolean hasAlpha = false;
    	for (int i = 0; i < password.length(); i++) {
    		char c = password.charAt(i);
    		if (Character.isDigit(c)) {
    			hasDigit = true;
    		}
    		if (Character.isLetter(c)) {
    			hasAlpha = true;
    		}
    	}
    	if (!hasDigit || !hasAlpha) {
    		return false;
    	}
    	return true;
    }

    static public boolean isDirectory(String name) {
    	if (name == null) {
    		return false;
    	}
    	if (name.trim().length() == 0) {
    		return false;
    	}
    	File file = new File(name);
    	if (file.isDirectory()) {
    		return true;
    	}
    	return false;
    }
    
    static public boolean isDirectoryEmpty(String name) {
    	if (name.trim().length() == 0) {
    		return true;
    	}
    	File file = new File(name);
    	if (!file.isDirectory()) {
    		return true;
    	}
    	if (file.list().length == 0) {
    		return true;
    	}
    	return false;
    }
    
    static public String getWorkingDirectory() {
    	String name = ApplicationGlobal.getWorkingDirectory();
		if (!name.endsWith("/") && !name.endsWith("\\")) {
			name += "/";
		}
		return name;
    }
    
    static public String getIndexPrefix(Site site) {
    	String prefix = getWorkingDirectory() + site.getSiteId() + "/index/";
    	return prefix;
    }
 
    static public String getTemplatePrefix(Site site, String templateName) {
    	String prefix = getWorkingDirectory() + site.getSiteId() + "/template/" + templateName + "/";
    	return prefix;
    }
    
    static public String getTemplateUrlPrefix(Site site, String templateName) {
    	return "/" + ApplicationGlobal.getContextPath() + "/web/proxy/" + site.getSiteId() + "/template/" + templateName + "/";
    }
    
    static public String getResourcePrefix(Site site) {
    	String prefix = getWorkingDirectory() + site.getSiteId() + "/resource/";
    	return prefix;
    }
    
    static public String getResourceUrlPrefix(Site site) {
    	return site.getSiteId() + "/resource/";
    }
    
    static public boolean isImage(String filename) {
    	int index = filename.lastIndexOf('.');
    	if (index == -1) {
    		return false;
    	}
    	if (filename.length() - 1 == index) {
    		return false;
    	}
    	String extension = filename.substring(index + 1);
    	for (int i = 0; i < Constants.IMAGE_MIME.length; i++) {
    		if (extension.toLowerCase().trim().equals(Constants.IMAGE_MIME[i])) {
    			return true;
    		}
    	}
    	return false;
    }
    
    // TODO - http://www.sitepoint.com/article/card-validation-class-php
    static public boolean isValidCreditCard(int creditCardType, String creditCardNum) {
    	return true;
/*
    	switch (creditCardType) {
	    	case Constants.CREDITCARD_VISA:
	    		break;
	    	case Constants.CREDITCARD_MASTERCARD:
	    		break;
	    	case Constants.CREDITCARD_AMEX:
	    		break;
	    	default:
	    		return false;
	    }
    	return true;
*/
    }
    
    public static String getStackTrace(Throwable t)
    {
        StringWriter string = new StringWriter();
        PrintWriter print = new PrintWriter(string, true);
        t.printStackTrace(print);
        print.flush();
        string.flush();
        return string.toString();
    }
    
    public static String getHtmlStackTrace(Throwable t) {
        StringWriter string = new StringWriter();
        PrintWriter print = new PrintWriter(string, true);
        t.printStackTrace(print);
        print.flush();
        string.flush();
        String response = string.toString();
        response = response.replaceAll("\n", "<br>");
        return response;
    }
    
    
/*
    public static void saveSiteParam(String userId, String siteId, String name, String value) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Site site = (Site) em.find(Site.class, siteId);
    	Iterator<?> iterator = site.getSiteParams().iterator();
    	while (iterator.hasNext()) {
    		SiteParam siteParam = (SiteParam) iterator.next();
    		if (siteParam.getSiteParamName().equals(name)) {
    			em.remove(siteParam);
    		}
    	}
    	
    	SiteParam siteParam = new SiteParam();
    	siteParam.setSiteParamName(name);
    	siteParam.setSiteParamValue(value == null ? "" : value);
    	siteParam.setRecUpdateBy(userId);
    	siteParam.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    	siteParam.setRecCreateBy(userId);
    	siteParam.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    	site.getSiteParams().add(siteParam);
    	em.persist(siteParam);
    }
*/
    static public String encode(String input) throws Exception {
    	input = input.replaceAll("/", "_*_");
    	return URLEncoder.encode(input, "UTF-8");
    }
    
    static public String decode(String input) throws Exception {
    	return URLDecoder.decode(input, "UTF-8");
    }
    
    /*
     * This method is used to reencode already encoded string.
     * Reason being when the string is already encodeed and being passed back and forth the browser, some
     * characters may accidentally decoded.
     * Therefore, this is to decode the string into its original representation and encode again.
     */
    static public String reEncode(String input) throws Exception {
    	String value = URLDecoder.decode(input, "UTF-8");
    	return URLEncoder.encode(value, "UTF-8");
    }
    
    public static final String escapeHTML(String s){
	   StringBuffer sb = new StringBuffer();
	   int n = s.length();
	   for (int i = 0; i < n; i++) {
	      char c = s.charAt(i);
	      switch (c) {
	         case '<': sb.append("&lt;"); break;
	         case '>': sb.append("&gt;"); break;
	         case '&': sb.append("&amp;"); break;
	         case '"': sb.append("&quot;"); break;
	         
	         default:  sb.append(c); break;
	      }
	   }
	   return sb.toString();
	}
    
    public static final String escapeStrictHTML(String s) {
    	String result = escapeHTML(s);
    	result = result.replaceAll("eval\\((.*)\\)", "");
    	result = result.replaceAll("[\\\"\\\'][\\s]*((?i)javascript):(.*)[\\\"\\\']", "\"\"");
    	result = result.replaceAll("((?i)script)", "");
    	return result;
    }

    // This should be removed before production.
	static String safePrefix = "D:\\work\\ajk";
	static public boolean removeFile(String directory) throws Exception {
		File file = new File(directory);
		File files[] = file.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					removeFile(files[i].getAbsolutePath());
				}
				else {
					if (!files[i].delete()) {
						return false;
					}
				}
			}
		}
		if (!file.delete()) {
			return false;
		}
		return true;
	}
	
	static public int fileCount(String directory) throws Exception {
		int count = 0;
		File file = new File(directory);
		File files[] = file.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					count += fileCount(files[i].getAbsolutePath());
				}
				else {
					count += 1;
				}
			}
		}
		count++;
		return count;
	}
	
	static public String trim(String input, int size) {
		if (input == null) {
			return input;
		}
		if (input.length() <= size) {
			return input;
		}
		return input.substring(0, size - 1);
	}
	
	static public boolean isExtendedTransaction(String paymentGatewayProvider) {
		if (paymentGatewayProvider.equals(PayPalEngine.class.getSimpleName())) {
			return false;
		}
		return true;
	}
	
	static public String getServletLocation(ServletContext context) {
		return context.getRealPath("/");
//		return "d:/tmp/jada/";
	}
	
	static public Level getLogLevel(Logger logger) {
		if (logger.getLevel() != null) {
			return logger.getLevel();
		}
		return logger.getParent().getLevel();
	}
	
	public static String getPublicURLPrefix(SiteDomain siteDomain) {
		String defaultPortNum = "";
		String publicURLPrefix = "http://" + siteDomain.getSiteDomainName();
		defaultPortNum = Constants.PORTNUM_PUBLIC;
		if (!Format.isNullOrEmpty(siteDomain.getSitePublicPortNum()) && !siteDomain.getSitePublicPortNum().equals(defaultPortNum)) {
			publicURLPrefix += ":" + siteDomain.getSitePublicPortNum();
		}
		return publicURLPrefix;
	}

	public static String getSecureURLPrefix(SiteDomain siteDomain) throws Exception {
		String secureURLPrefix = getPublicURLPrefix(siteDomain);
		String secureEnabled = String.valueOf(siteDomain.getSiteSslEnabled());
		if (secureEnabled != null && secureEnabled.equals(String.valueOf(Constants.VALUE_YES))) {
			secureURLPrefix = "https://" + siteDomain.getSiteDomainName();
			String defaultPortNum = Constants.PORTNUM_SECURE;
			if (!Format.isNullOrEmpty(siteDomain.getSiteSecurePortNum()) && !siteDomain.getSiteSecurePortNum().equals(defaultPortNum)) {
				secureURLPrefix += ":" + siteDomain.getSiteSecurePortNum();
			}
		}
		return secureURLPrefix;
	}
	
	public static String joxMarshall(String objectName, Object object) throws Exception {
		StringWriter writer = new StringWriter();
		Marshaller.marshal(object, writer);
		return writer.toString();
	}
	
	public static Object joxUnMarshall(Class<?> c, String in) throws Exception {
		StringReader reader = new StringReader(in);
		return Unmarshaller.unmarshal(c, reader);
	}
	
	public static String formatDomainURL(SiteDomain siteDomain, String context) {
		String publicPortNum = "";
		if (!Format.isNullOrEmpty(siteDomain.getSitePublicPortNum())) {
			publicPortNum = ":" + siteDomain.getSitePublicPortNum();
		}
		String url = "http://" + 
					 siteDomain.getSiteDomainName() + 
					 publicPortNum +
					 "/" +
					 context +
					 "/web/fe" +
					 "/" + siteDomain.getSiteDomainPrefix() +
					 "/" + siteDomain.getSiteProfileDefault().getSiteProfileClass().getSiteProfileClassName() +
					 "/home";
		return url;
	}
	
	public static boolean isConstraintViolation(Throwable e) {
		if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
			return true;
		}
		return false;
	}
	
	static public byte[] getImage(HttpServletRequest request, String type, String id, int maxsize) throws Exception {

        String contentType = null;
        byte data[] = null;
        if (type.equals(Constants.IMAGEPROVIDER_CONTENT)) {
        	data = getContentImage(request, id, maxsize);
        }
        else if (type.equals(Constants.IMAGEPROVIDER_SITE)) {
        	data = getSiteImage(request, id, maxsize);
        }
        else if (type.equals(Constants.IMAGEPROVIDER_ITEM)) {          
        	data = getItemImage(request, id, maxsize);
        }
        else if (type.equals(Constants.IMAGEPROVIDER_URL)) {
        	String url = request.getRequestURL().toString();
    		int index = url.indexOf("//");
    		index = url.indexOf("/", index + 2);
    		String prefix = url.substring(0, index) + "/" + ApplicationGlobal.getContextPath();
        	data = Utility.httpGet(prefix + id);
        	contentType = "image/jpeg";
        	if (maxsize != 0) {
        		ImageScaler scaler = new ImageScaler(data, contentType);
        		scaler.resize(maxsize);
        		data = scaler.getBytes();	
        	}
        }
        else if (type.equals(Constants.IMAGEPROVIDER_TEMPLATE)) {
        	// template preview image
        	contentType = "image/jpeg";
        	Site site = AdminAction.getAdminBean(request).getSite();
        	String templatePrefix = Utility.getTemplatePrefix(site, id);
    		if (templatePrefix.trim().length() == 0 || id == null || id.trim().length() == 0) {
    			contentType = "image/gif";
    			data = transparent;
    		}
    		else {
            	String filename = templatePrefix + "preview.jpg";
            	File file = new File(filename);
            	if (file.exists()) {
	            	FileInputStream fr = new FileInputStream(new File(filename));
	            	data = new byte[(int) file.length()];
	        		fr.read(data, 0, (int) file.length());
	            	if (maxsize != 0) {
		        		ImageScaler scaler = new ImageScaler(data, contentType);
		        		scaler.resize(maxsize);
		        		data = scaler.getBytes();
	            	}
	            	fr.close();
            	}
            	else {
            		data = transparent;
            	}
    		}
        }
        return data;
	}
	
	static byte[] getContentImage(HttpServletRequest request, String id, int maxsize) throws Exception {
		byte data[] = null;
		String contentType = null;
		ContentImage image = ContentImageDAO.load(Long.valueOf(id));
		if (image != null) {
			data = image.getImageValue();
			contentType = image.getContentType();
		}
		else {
    		data = transparent;
    		contentType = "jpeg";
		}
        if (maxsize != 0) {
    		ImageScaler scaler = new ImageScaler(data, contentType);
    		scaler.resize(maxsize);
    		data = scaler.getBytes();
        }
        return data;
	}
	
	static byte[] getItemImage(HttpServletRequest request, String id, int maxsize) throws Exception {
		byte data[] = null;
		String contentType = null;
		ItemImage image = ItemImageDAO.load(Long.valueOf(id));
		if (image != null) {
			data = image.getImageValue();
			contentType = image.getContentType();
		}
		else {
    		data = transparent;
    		contentType = "jpeg";
		}
        if (maxsize != 0) {
    		ImageScaler scaler = new ImageScaler(data, contentType);
    		scaler.resize(maxsize);
    		data = scaler.getBytes();
        }
        return data;
	}
	
	static byte[] getSiteImage(HttpServletRequest request, String id, int maxsize) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String contentType = "";
		SiteDomainLanguage siteDomainLanguage = (SiteDomainLanguage) em.find(SiteDomainLanguage.class, Long.valueOf(id));
		byte data[] = null;
		if (siteDomainLanguage != null) {
			data = siteDomainLanguage.getSiteLogoValue();
		}
    	if (data == null) {
    		data = transparent;
    		contentType = siteDomainLanguage.getSiteLogoContentType();
    	}
    	else {
	        if (maxsize != 0) {
	    		ImageScaler scaler = new ImageScaler(data, contentType);
	    		scaler.resize(maxsize);
	    		data = scaler.getBytes();
	        }
    	}
		return data;
	}
	
	static public String toCsvFormat(Object input) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		StringWriter writer = new StringWriter();
		CSVWriter csvPrinter = new CSVWriter(writer);

		Class<? extends Object> c = input.getClass();
		Method methods[] = c.getMethods();
		Vector<String> v = new Vector<String>();
		for (Method method : methods) {
			if (!method.getName().startsWith("get")) {
				continue;
			}
			Object object = method.invoke(input, (Object[]) null);
			if (object == null) {
				v.add("");
			}
			else {
				String value = "Unknown type for " + method.getName();
				if (object instanceof String) {
					value = (String) object;
				}
				if (object instanceof Integer) {
					value = ((Integer) object).toString();
				}
				if (object instanceof Float) {
					value = ((Float) object).toString();
				}
				if (object instanceof Double) {
					value = ((Float) object).toString();
				}
				if (object instanceof Date) {
					value = Format.getDate((Date) object);
				}
				v.add(value);
			}
		}
		String tokens[] = new String[v.size()];
		v.copyInto(tokens);
		csvPrinter.writeNext(tokens);
		return writer.toString();
	}
	
	static public boolean isDateBetween(Date startDate, Date endDate) {
		Calendar current = Calendar.getInstance();
		current.set(Calendar.HOUR, 0);
		current.set(Calendar.MINUTE, 0);
		current.set(Calendar.SECOND, 0);
		current.set(Calendar.MILLISECOND, 0);
		current.set(Calendar.AM_PM, Calendar.AM);
		
		Date now = current.getTime();
		if (now.before(startDate)) {
			return false;
		}
		if (now.after(endDate)) {
			return false;
		}
		return true;
	}
	
	static public String maskCreditCardNumber(String number) {
		int length = number.length();
		String result = "";
		for (int i = 0; i < number.length(); i++) {
			if (i < length - 4) {
				result += "X";
			}
			else {
				result += number.substring(i, i + 1);
			}
		}
		return result;
	}
}
