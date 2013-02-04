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

package com.jada.admin.menu;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.CacheDAO;
import com.jada.dao.ContentDAO;
import com.jada.dao.ItemDAO;
import com.jada.dao.MenuDAO;
import com.jada.dao.CategoryDAO;
import com.jada.dao.SiteDomainDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Category;
import com.jada.jpa.entity.Content;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.Menu;
import com.jada.jpa.entity.MenuLanguage;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.jpa.entity.User;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.translator.BingTranslate;
import com.jada.util.Utility;

import fr.improve.struts.taglib.layout.util.FormUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

public class MenuMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	MenuMaintActionForm form = (MenuMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        initSiteProfiles(form, adminBean.getSite());
        Long siteProfileClassId = form.getSiteProfileClassId();
        form.setJsonMenuList(Utility.makeJSONMenuTree(Format.getLong(form.getSiteDomainId()), siteProfileClassId, form.isSiteProfileClassDefault()).toHtmlString());
        form.setMode("");

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward translate(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        MenuMaintActionForm form = (MenuMaintActionForm) actionForm;
        if (form == null) {
            form = new MenuMaintActionForm();
        }

        Site site = getAdminBean(request).getSite();
        initSiteProfiles(form, site);
        Long siteProfileClassId = form.getSiteProfileClassId();
        form.setJsonMenuList(Utility.makeJSONMenuTree(Format.getLong(form.getSiteDomainId()), siteProfileClassId, form.isSiteProfileClassDefault()).toHtmlString());
        
        if (!form.isSiteProfileClassDefault() && !Format.isNullOrEmpty(form.getMenuId())) {
        	Menu menu = (Menu) em.find(Menu.class, Format.getLong(form.getMenuId()));
        	initLanguageInfo(menu, form);
        	
            BingTranslate translator = new BingTranslate(form.getFromLocale(), form.getToLocale(), site);
            form.setMenuNameLangFlag(true);
            form.setMenuNameLang(translator.translate(menu.getMenuLanguage().getMenuName()));
        }
        
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward language(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        MenuMaintActionForm form = (MenuMaintActionForm) actionForm;
        if (form == null) {
            form = new MenuMaintActionForm();
        }

        Site site = getAdminBean(request).getSite();
        initSiteProfiles(form, site);
        Long siteProfileClassId = form.getSiteProfileClassId();
        form.setJsonMenuList(Utility.makeJSONMenuTree(Format.getLong(form.getSiteDomainId()), siteProfileClassId, form.isSiteProfileClassDefault()).toHtmlString());
        
        if (!form.isSiteProfileClassDefault() && !Format.isNullOrEmpty(form.getMenuId())) {
        	Menu menu = (Menu) em.find(Menu.class, Format.getLong(form.getMenuId()));
        	initLanguageInfo(menu, form);
        }
        
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public void initLanguageInfo(Menu menu, MenuMaintActionForm form) {
		MenuLanguage menuLanguage = null;
		for (MenuLanguage language : menu.getMenuLanguages()) {
			if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassDefaultId())) {
				menuLanguage = language;
				break;
			}
		}
		
    	form.setMenuNameLang(menuLanguage.getMenuName());
    	Iterator<?> iterator = menu.getMenuLanguages().iterator();
    	boolean found = false;
    	Long siteProfileClassId = form.getSiteProfileClassId();
    	while (iterator.hasNext()) {
    		menuLanguage = (MenuLanguage) iterator.next();
    		if (menuLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
    			found = true;
    			break;
    		}
    	}
    	if (found) {
    		if (menuLanguage.getMenuName() != null) {
	    		form.setMenuNameLangFlag(true);
	    		form.setMenuNameLang(menuLanguage.getMenuName());
    		}
    	}
    }
    
    public ActionForward newMenuSet(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	MenuMaintActionForm form = (MenuMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        SiteDomain siteDomain = SiteDomainDAO.load(Format.getLong(form.getSiteDomainId()));
        initSiteProfiles(form, site);
        
		ActionMessages errors = validateNewMenuSet(form, siteDomain.getSiteDomainId());
		if (errors.size() != 0) {
			saveMessages(request, errors);
	        Long siteProfileClassId = form.getSiteProfileClassId();
	        form.setJsonMenuList(Utility.makeJSONMenuTree(Format.getLong(form.getSiteDomainId()), siteProfileClassId, form.isSiteProfileClassDefault()).toHtmlString());
			return actionMapping.findForward("error");
		}

        Menu menuSet = new Menu();
        menuSet.setSiteDomain(siteDomain);
        menuSet.setMenuSetName(form.getCreateMenuSetName());
        menuSet.setSeqNum(0);
        menuSet.setPublished('Y');
        menuSet.setMenuType("");
        menuSet.setMenuUrl("");
        menuSet.setMenuWindowTarget("");
        menuSet.setMenuWindowMode("");
        menuSet.setRecUpdateBy(adminBean.getUser().getUserId());
        menuSet.setRecCreateBy(adminBean.getUser().getUserId());
        menuSet.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        menuSet.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        
        MenuLanguage menuLanguage = new MenuLanguage();
        menuLanguage.setMenuName(form.getCreateMenuSetName());
        menuLanguage.setMenu(menuSet);
        menuLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
        menuLanguage.setRecCreateBy(adminBean.getUser().getUserId());
        menuLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        menuLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        menuLanguage.setSiteProfileClass(site.getSiteProfileClassDefault());
        menuSet.getMenuLanguages().add(menuLanguage);
        menuSet.setMenuLanguage(menuLanguage);
        
        em.persist(menuLanguage);
        em.persist(menuSet);

        form.setCreateMenuSetName("");
        Long siteProfileClassId = form.getSiteProfileClassId();
        form.setJsonMenuList(Utility.makeJSONMenuTree(Format.getLong(form.getSiteDomainId()), siteProfileClassId, form.isSiteProfileClassDefault()).toHtmlString());
        form.setMenuId("");
        form.setMode("");
        
        CacheDAO.removeByKeyPrefix(site.getSiteId(), Constants.CACHE_MENU + "." + form.getMenuSetName());
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward removeMenuSet(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	MenuMaintActionForm form = (MenuMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        initSiteProfiles(form, site);
       
        Menu menu = MenuDAO.load(site.getSiteId(), Format.getLong(form.getRemoveMenuId()));
        
    	Query query = em.createQuery("from	  Menu " +
				  "where  siteDomain.site.siteId = :siteId " +
				  "and    menuSetName = :menuSetName");
		query.setParameter("siteId", adminBean.getSite().getSiteId());
		query.setParameter("menuSetName", menu.getMenuSetName());
		Iterator<?> iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			Menu child = (Menu) iterator.next();
			em.remove(child);
			for (MenuLanguage menuLanguage : child.getMenuLanguages()) {
				em.remove(menuLanguage);
			}
		}

        em.flush();
        Long siteProfileClassId = form.getSiteProfileClassId();
        form.setJsonMenuList(Utility.makeJSONMenuTree(Format.getLong(form.getSiteDomainId()), siteProfileClassId, form.isSiteProfileClassDefault()).toHtmlString());
        form.setMenuId(null);
        form.setMode("");
     
        CacheDAO.removeByKeyPrefix(site.getSiteId(), Constants.CACHE_MENU + "." + menu.getMenuSetName());

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward create(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        MenuMaintActionForm form = (MenuMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        SiteDomain siteDomain = SiteDomainDAO.load(Format.getLong(form.getSiteDomainId()));
        initSiteProfiles(form, site);
   
        Menu referenceMenu = MenuDAO.load(site.getSiteId(), Format.getLong(form.getCreateMenuId()));
        Menu parent = null;
        int seqNum = 0;
        if (form.getCreateMode().equals("C")) {	// append child node
        	for (Menu child : referenceMenu.getMenuChildren()) {
        		if (child.getSeqNum() >= seqNum) {
        			seqNum = child.getSeqNum() + 1;
        		}
        	}
        	parent = referenceMenu;
        } else if (form.getCreateMode().equals("B")) {  // before current
        	parent = referenceMenu.getMenuParent();
        	seqNum = referenceMenu.getSeqNum();
        	for (Menu child : parent.getMenuChildren()) {
        		if (child.getSeqNum() >= seqNum) {
        			child.setSeqNum(child.getSeqNum() + 1);
        			em.persist(child);
        		}
        	}        
        } else if (form.getCreateMode().equals("A")) {  // after current
        	parent = referenceMenu.getMenuParent();
        	seqNum = referenceMenu.getSeqNum() + 1;
        	for (Menu child : parent.getMenuChildren()) {
        		if (child.getSeqNum() >= seqNum) {
        			child.setSeqNum(child.getSeqNum() + 1);
        			em.persist(child);
        		}
        	}
        }
        Menu menu = new Menu();
        menu.setSiteDomain(siteDomain);
        menu.setMenuParent(parent);
        menu.setSeqNum(seqNum);
        menu.setMenuSetName(referenceMenu.getMenuSetName());
        menu.setMenuType(Constants.MENU_HOME);
        menu.setMenuUrl("");
        menu.setMenuWindowTarget("");
        menu.setMenuWindowMode("");
        menu.setPublished(Constants.PUBLISHED_YES);
        menu.setRecUpdateBy(adminBean.getUser().getUserId());
        menu.setRecCreateBy(adminBean.getUser().getUserId());
        menu.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        menu.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        parent.getMenuChildren().add(menu);

        MenuLanguage menuLanguage = new MenuLanguage();
        menuLanguage.setMenuName("New Menu");
        menuLanguage.setMenu(menu);
        menuLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
        menuLanguage.setRecCreateBy(adminBean.getUser().getUserId());
        menuLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        menuLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        menuLanguage.setSiteProfileClass(site.getSiteProfileClassDefault());
        em.persist(menuLanguage);
        menu.getMenuLanguages().add(menuLanguage);
        menu.setMenuLanguage(menuLanguage);
        
        em.persist(menu);

        form.setMenuId(Format.getLong(menu.getMenuId()));
        form.setMenuParentId(parent.getMenuId().toString());
        form.setMenuSetName(menu.getMenuSetName());
        form.setMenuName(menuLanguage.getMenuName());
        form.setMenuType(Constants.MENU_HOME);
        form.setMenuUrl("");
        form.setMenuWindowTarget("");
        form.setMenuWindowMode("");
        form.setPublished(menu.getPublished() == Constants.PUBLISHED_YES ? true : false);
        form.setMode(Constants.MODE_UPDATE);
        Long siteProfileClassId = form.getSiteProfileClassId();
        form.setJsonMenuList(Utility.makeJSONMenuTree(Format.getLong(form.getSiteDomainId()), siteProfileClassId, form.isSiteProfileClassDefault()).toHtmlString());
        form.setPublished(true);
        form.setSequence(false);
        form.setMode(Constants.MODE_CREATE);
        
        initListInfo(form, site.getSiteId());
        
        CacheDAO.removeByKeyPrefix(site.getSiteId(), Constants.CACHE_MENU);

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward update(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        MenuMaintActionForm form = (MenuMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        initSiteProfiles(form, site);
        String siteId = site.getSiteId();
        
        Long siteProfileClassId = form.getSiteProfileClassId();
        form.setJsonMenuList(Utility.makeJSONMenuTree(Format.getLong(form.getSiteDomainId()), siteProfileClassId, form.isSiteProfileClassDefault()).toHtmlString());
        String menuId = (String) request.getParameter("menuId");
        Menu menu = MenuDAO.load(siteId, Format.getLong(menuId));
        form.setMenuId(menuId);
        if (menu.getMenuParent() != null) {
        	form.setMenuParentId(menu.getMenuParent().getMenuId().toString());;
        }
        
        form.setMenuId(Format.getLong(menu.getMenuId()));
        form.setMenuSetName(menu.getMenuSetName());
        form.setMenuName(menu.getMenuLanguage().getMenuName());
        form.setMenuUrl(menu.getMenuUrl());
        form.setMenuWindowTarget(menu.getMenuWindowTarget());
        form.setMenuWindowMode(menu.getMenuWindowMode());
        form.setPublished(menu.getPublished() == Constants.PUBLISHED_YES ? true : false);
        form.setMode(Constants.MODE_UPDATE);
        form.setSequence(false);
        form.setMode(Constants.MODE_UPDATE);
        
        form.setMenuType(menu.getMenuType());
        if (menu.getMenuType().equals(Constants.MENU_CONTENT) && menu.getContent() != null) {
        	Content content = menu.getContent();
        	form.setContentId(content.getContentId().toString());
        	form.setContentTitle(content.getContentLanguage().getContentTitle());
        }
        else if (menu.getMenuType().equals(Constants.MENU_ITEM) && menu.getItem() != null) {
        	Item item = menu.getItem();
        	form.setItemId(item.getItemId().toString());
        	form.setItemNum(item.getItemNum());
        	form.setItemShortDesc(item.getItemLanguage().getItemShortDesc());
        }
        else if (menu.getMenuType().equals(Constants.MENU_SECTION) && menu.getCategory() != null) {
        	Category category = menu.getCategory();
        	form.setCatId(category.getCatId().toString());
        	form.setCatShortTitle(category.getCategoryLanguage().getCatShortTitle());
        }
        if (!form.isSiteProfileClassDefault()) {
        	initLanguageInfo(menu, form);
        }
        initListInfo(form, siteId);

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward save(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        String siteId = site.getSiteId();
        MenuMaintActionForm form = (MenuMaintActionForm) actionForm;
        initSiteProfiles(form, site);
       
		ActionMessages errors = validate(form, siteId);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return actionMapping.findForward("error");
		}
        Menu menu = MenuDAO.load(siteId, Format.getLong(form.getMenuId()));
        if (form.isSiteProfileClassDefault()) {
        	saveDefault(menu, form, adminBean);
        }
        else {
        	saveLanguage(menu, form, adminBean);
        }   
        em.flush();
  
        Long siteProfileClassId = form.getSiteProfileClassId();
        form.setJsonMenuList(Utility.makeJSONMenuTree(Format.getLong(form.getSiteDomainId()), siteProfileClassId, form.isSiteProfileClassDefault()).toHtmlString());
        
        initListInfo(form, siteId);
        
        CacheDAO.removeByKeyPrefix(siteId, Constants.CACHE_MENU);

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public void saveLanguage(Menu menu, MenuMaintActionForm form, AdminBean adminBean) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Long siteProfileClassId = form.getSiteProfileClassId();
    	User user = adminBean.getUser();
    	Iterator<?> iterator = menu.getMenuLanguages().iterator();
    	boolean found = false;
    	MenuLanguage menuLanguage = null;
    	while (iterator.hasNext()) {
    		menuLanguage = (MenuLanguage) iterator.next();
    		if (menuLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
    			found = true;
    			break;
    		}
    	}
    	if (!found) {
    		menuLanguage = new MenuLanguage();
    		menuLanguage.setRecCreateBy(user.getUserId());
    		menuLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		SiteProfileClass siteProfileClass = (SiteProfileClass) em.find(SiteProfileClass.class, siteProfileClassId);
    		menuLanguage.setSiteProfileClass(siteProfileClass);
    		menuLanguage.setMenu(menu);
    	}
    	if (form.isMenuNameLangFlag()) {
    		menuLanguage.setMenuName(form.getMenuNameLang());
    	}
    	else {
    		menuLanguage.setMenuName(null);
    	}
    	menuLanguage.setRecUpdateBy(user.getUserId());
    	menuLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    	em.persist(menuLanguage);
    }
    
    public void saveDefault(Menu menu, MenuMaintActionForm form, AdminBean adminBean) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();

    	Site site = adminBean.getSite();
        menu.getMenuLanguage().setMenuName(form.getMenuName());
        menu.setMenuUrl(form.getMenuUrl());
        menu.setMenuWindowTarget(form.getMenuWindowTarget());
        menu.setMenuWindowMode(form.getMenuWindowMode());
        menu.setPublished(form.isPublished() ? Constants.PUBLISHED_YES : Constants.PUBLISHED_NO);
        menu.setRecUpdateBy(adminBean.getUser().getUserId());
        menu.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        
        menu.setContent(null);
        menu.setItem(null);
        menu.setCategory(null);
        
        menu.setMenuType(form.getMenuType());
        if (form.getMenuType().equals(Constants.MENU_CONTENT)) {
        	if (!Format.isNullOrEmpty(form.getContentId())) {
	        	Content content = ContentDAO.load(site.getSiteId(), Format.getLong(form.getContentId()));
	        	menu.setContent(content);
        	}
        }
        if (form.getMenuType().equals(Constants.MENU_ITEM)) {
        	if (!Format.isNullOrEmpty(form.getItemId())) {
	        	Item item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
	        	menu.setItem(item);
        	}
        }
        if (form.getMenuType().equals(Constants.MENU_SECTION)) {
        	if (!Format.isNullOrEmpty(form.getCatId())) {
	        	Category category = CategoryDAO.load(site.getSiteId(), Format.getLong(form.getCatId()));
	        	menu.setCategory(category);
        	}
        }
        em.persist(menu);
    }
    
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        MenuMaintActionForm form = (MenuMaintActionForm) actionForm;
        initSiteProfiles(form, site);
        Menu menu = (Menu) em.find(Menu.class, Format.getLong(form.getMenuId()));
		
		cascadeRemoveMenu(Format.getLong(form.getMenuId()), site.getSiteId());
        
        Long siteProfileClassId = form.getSiteProfileClassId();
        form.setJsonMenuList(Utility.makeJSONMenuTree(Format.getLong(form.getSiteDomainId()), siteProfileClassId, form.isSiteProfileClassDefault()).toHtmlString());
        form.setMode("");
        
        CacheDAO.removeByKeyPrefix(site.getSiteId(), Constants.CACHE_MENU + "." + menu.getMenuSetName());

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward showSequence(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        MenuMaintActionForm form = (MenuMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        initSiteProfiles(form, site);
        
        Long siteProfileClassId = form.getSiteProfileClassId();
        form.setJsonMenuList(Utility.makeJSONMenuTree(Format.getLong(form.getSiteDomainId()), siteProfileClassId, form.isSiteProfileClassDefault()).toHtmlString());
        String menuId = (String) request.getParameter("menuId");
        Menu menu = MenuDAO.load(site.getSiteId(), Format.getLong(menuId));

        form.setMenuId(menuId);
        if (menu.getMenuParent() != null) {
        	form.setMenuParentId(menu.getMenuParent().getMenuId().toString());
        }
        
        form.setMenuId(Format.getLong(menu.getMenuId()));
        form.setMenuSetName(menu.getMenuSetName());
        form.setMenuName(menu.getMenuLanguage().getMenuName());
        form.setMenuType(menu.getMenuType());
        form.setMenuUrl(menu.getMenuUrl());
        form.setMenuWindowTarget(menu.getMenuWindowTarget());
        form.setMenuWindowMode(menu.getMenuWindowMode());
        form.setPublished(menu.getPublished() == Constants.PUBLISHED_YES ? true : false);
        form.setMode(Constants.MODE_UPDATE);
        form.setPublished(true);
        form.setMode(Constants.MODE_UPDATE);
        form.setSequence(true);
        
        initListInfo(form, site.getSiteId());
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward resequence(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	MenuMaintActionForm form = (MenuMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        initSiteProfiles(form, site);
        
		ActionMessages errors = validateSequenceNum(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
	        Long siteProfileClassId = form.getSiteProfileClassId();
	        form.setJsonMenuList(Utility.makeJSONMenuTree(Format.getLong(form.getSiteDomainId()), siteProfileClassId, form.isSiteProfileClassDefault()).toHtmlString());
	        form.setSequence(true);
			return actionMapping.findForward("error");
		}
        
		MenuDisplayForm childMenus[] = form.getChildrenMenus();
		String menuSetName = "";
        for (int i = 0; i < childMenus.length; i++) {
        	int seqNum = Format.getInt(childMenus[i].getSeqNum());
        	Long menuId = Format.getLong(childMenus[i].getMenuId());
    		Menu menu = new Menu();
    		menu = MenuDAO.load(site.getSiteId(), menuId);
    		menu.setSeqNum(seqNum);
    		menuSetName = menu.getMenuSetName();
    		// em.update(menu);
        }
        Long siteProfileClassId = form.getSiteProfileClassId();
        form.setJsonMenuList(Utility.makeJSONMenuTree(Format.getLong(form.getSiteDomainId()), siteProfileClassId, form.isSiteProfileClassDefault()).toHtmlString());
        form.setSequence(true);
     
        CacheDAO.removeByKeyPrefix(site.getSiteId(), Constants.CACHE_MENU + "." + menuSetName);
 
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionMessages validateSequenceNum(MenuMaintActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	MenuDisplayForm childrenMenus[] = form.getChildrenMenus();
    	for (int i = 0; i < childrenMenus.length; i++) {
    		if (!Format.isInt(childrenMenus[i].getSeqNum())) {
    			errors.add("seqNum_" + i, new ActionMessage("error.int.invalid"));
    		}
    	}
    	return errors;
    }
    
    public ActionForward removeSelected(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	MenuMaintActionForm form = (MenuMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        initSiteProfiles(form, site);
        String siteId = site.getSiteId();
        
        MenuDisplayForm childrenMenus[] = form.getChildrenMenus();
        for (int i = 0; i < childrenMenus.length; i++) {
        	if (childrenMenus[i].isRemove()) {
	        	Long menuId = Format.getLong(childrenMenus[i].getMenuId());
	        	cascadeRemoveMenu(menuId, siteId);
	        }
        }
        em.flush();
        
        Long siteProfileClassId = form.getSiteProfileClassId();
        form.setJsonMenuList(Utility.makeJSONMenuTree(Format.getLong(form.getSiteDomainId()), siteProfileClassId, form.isSiteProfileClassDefault()).toHtmlString());
        initListInfo(form, siteId);
        form.setSequence(true);
      
        CacheDAO.removeByKeyPrefix(siteId, Constants.CACHE_MENU);

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    protected void initListInfo(MenuMaintActionForm form, String siteId) throws Exception {
		Menu menu = MenuDAO.load(siteId, Format.getLong(form.getMenuId()));
		
		Vector<MenuDisplayForm> vector = new Vector<MenuDisplayForm>();
		for (Menu childMenu : menu.getMenuChildren()) {
			MenuDisplayForm display = new MenuDisplayForm();
			display.setMenuId(Format.getLong(childMenu.getMenuId()));
			display.setMenuName(childMenu.getMenuLanguage().getMenuName());
			display.setSeqNum(Format.getInt(childMenu.getSeqNum()));
			display.setMenuWindowTarget(childMenu.getMenuWindowTarget());
			display.setMenuWindowMode(childMenu.getMenuWindowMode());
			display.setPublished(String.valueOf(childMenu.getPublished()));
			vector.add(display);
		}
		MenuDisplayForm childrenMenus[] = new MenuDisplayForm[vector.size()];
		vector.copyInto(childrenMenus);
		form.setChildrenMenus(childrenMenus);
    }

    public ActionMessages validateNewMenuSet(MenuMaintActionForm form, Long siteDomainId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();

    	ActionMessages errors = new ActionMessages();
    	if (Format.isNullOrEmpty(form.getCreateMenuSetName())) {
    		errors.add("createMenuSetName", new ActionMessage("error.string.required"));    		
    	}
    	
    	Query query = em.createQuery("select count(*) from Menu menu where menuSetName = :menuSetName and menu.siteDomain.siteDomainId = :siteDomainId");
    	query.setParameter("menuSetName", form.getCreateMenuSetName());
    	query.setParameter("siteDomainId", siteDomainId);
    	long count = (Long) query.getSingleResult();
    	if (count > 0) {
    		errors.add("createMenuSetName", new ActionMessage("error.record.duplicate"));
    	}
    	
    	return errors;
    }
    
    public ActionMessages validate(MenuMaintActionForm form, String siteId) throws Exception { 
    	ActionMessages errors = new ActionMessages();
    	if (Format.isNullOrEmpty(form.getMenuName())) {
    		errors.add("menuName", new ActionMessage("error.string.required"));    		
    	}
    	return errors;
    }
    
    public void cascadeRemoveMenu(Long menuId, String siteId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Menu parent = MenuDAO.load(siteId, menuId);
    	for (Menu childMenu : parent.getMenuChildren()) {
    		cascadeRemoveMenu(childMenu.getMenuId(), siteId);
    	}
    	parent.setMenuLanguage(null);
    	for (MenuLanguage menuLanguage: parent.getMenuLanguages()) {
    		em.remove(menuLanguage);
    	}
    	em.remove(parent);
    	return;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        map.put("newMenuSet", "newMenuSet");
        map.put("removeMenuSet", "removeMenuSet");
        map.put("create", "create");
        map.put("update", "update");
        map.put("save", "save");
        map.put("remove", "remove");
        map.put("resequence", "resequence");
        map.put("removeSelected", "removeSelected");
        map.put("showSequence", "showSequence");
        map.put("language", "language");
        map.put("translate", "translate");
        return map;
    }
}