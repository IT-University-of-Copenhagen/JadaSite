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

package com.jada.admin.category;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import javax.persistence.EntityManager;
import com.jada.util.JSONEscapeObject;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.CacheDAO;
import com.jada.dao.CategoryDAO;
import com.jada.dao.SiteDomainDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Category;
import com.jada.jpa.entity.CategoryLanguage;
import com.jada.jpa.entity.Content;
import com.jada.jpa.entity.CustomAttribute;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.Menu;
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

public class CategoryMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	CategoryMaintActionForm form = (CategoryMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        initSiteProfiles(form, adminBean.getSite());
        String jsonCategoryTree = Utility.makeJSONCategoryTree(site.getSiteId(), form.getSiteProfileClassId(), form.isSiteProfileClassDefault()).toHtmlString();
        form.setJsonCategoryTree(jsonCategoryTree);
    
        String error = request.getParameter("error");
        if (error != null && error.equals("E01")) {
	    	ActionMessages errors = new ActionMessages();
			errors.add("catId", new ActionMessage("error.catId.required"));
			saveMessages(request, errors);
        }
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
		CategoryMaintActionForm form = (CategoryMaintActionForm) actionForm;
		if (form == null) {
			form = new CategoryMaintActionForm();
		}
		
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
        String jsonCategoryTree = Utility.makeJSONCategoryTree(site.getSiteId(), form.getSiteProfileClassId(), form.isSiteProfileClassDefault()).toHtmlString();
        form.setJsonCategoryTree(jsonCategoryTree);
		
		if (!form.isSiteProfileClassDefault() && !Format.isNullOrEmpty(form.getCatId())) {
			Category category = em.find(Category.class, Format.getLong(form.getCatId()));
			initLanguageInfo(category, form);
			
            BingTranslate translator = new BingTranslate(form.getFromLocale(), form.getToLocale(), site);
            form.setCatDescLangFlag(true);
            form.setCatShortTitleLangFlag(true);
            form.setCatTitleLangFlag(true);
            form.setCatDescLang(translator.translate(category.getCategoryLanguage().getCatDesc()));
            form.setCatShortTitleLang(translator.translate(category.getCategoryLanguage().getCatShortTitle()));
            form.setMetaDescriptionLangFlag(true);
            form.setMetaDescriptionLang(translator.translate(category.getCategoryLanguage().getMetaDescription()));
            form.setMetaKeywordsLangFlag(true);
            form.setMetaKeywordsLang(translator.translate(category.getCategoryLanguage().getMetaKeywords()));
            if (form.getCatShortTitleLang().length() > 20) {
            	form.setCatShortTitleLang(form.getCatShortTitleLang().substring(0, 20));
            }
            form.setCatTitleLang(translator.translate(category.getCategoryLanguage().getCatTitle()));
            if (form.getCatTitleLang().length() > 40) {
            	form.setCatTitleLang(form.getCatTitleLang().substring(0, 40));
            }
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
		CategoryMaintActionForm form = (CategoryMaintActionForm) actionForm;
		if (form == null) {
			form = new CategoryMaintActionForm();
		}
		
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
        String jsonCategoryTree = Utility.makeJSONCategoryTree(site.getSiteId(), form.getSiteProfileClassId(), form.isSiteProfileClassDefault()).toHtmlString();
        form.setJsonCategoryTree(jsonCategoryTree);
		
		if (!form.isSiteProfileClassDefault() && !Format.isNullOrEmpty(form.getCatId())) {
			Category category = em.find(Category.class, Format.getLong(form.getCatId()));
			initLanguageInfo(category, form);
		}
		
		FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}
    
    public void initLanguageInfo(Category category, CategoryMaintActionForm form) {
    	form.setCatTitleLang(category.getCategoryLanguage().getCatTitle());
    	form.setCatShortTitleLang(category.getCategoryLanguage().getCatShortTitle());
    	form.setCatDescLang(category.getCategoryLanguage().getCatDesc());
    	form.setMetaDescriptionLang(category.getCategoryLanguage().getMetaDescription());
    	form.setMetaKeywordsLang(category.getCategoryLanguage().getMetaKeywords());
  	
    	Iterator<?> iterator = category.getCategoryLanguages().iterator();
    	Long siteProfileClassId = form.getSiteProfileClassId();
    	CategoryLanguage categoryLanguage = null;
    	while (iterator.hasNext()) {
    		categoryLanguage = (CategoryLanguage) iterator.next();
    		if (categoryLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
        		if (categoryLanguage.getCatTitle() != null) {
    	    		form.setCatTitleLangFlag(true);
    	    		form.setCatTitleLang(categoryLanguage.getCatTitle());
        		}
        		if (categoryLanguage.getCatShortTitle() != null) {
    	    		form.setCatShortTitleLangFlag(true);
    	    		form.setCatShortTitleLang(categoryLanguage.getCatShortTitle());
        		}
        		if (categoryLanguage.getCatDesc() != null) {
    	    		form.setCatDescLangFlag(true);
    	    		form.setCatDescLang(categoryLanguage.getCatDesc());
        		}
        		if (categoryLanguage.getMetaDescription() != null) {
    	    		form.setMetaDescriptionLangFlag(true);
    	    		form.setMetaDescriptionLang(categoryLanguage.getMetaDescription());
        		}
        		if (categoryLanguage.getMetaKeywords() != null) {
    	    		form.setMetaKeywordsLangFlag(true);
    	    		form.setMetaKeywordsLang(categoryLanguage.getMetaKeywords());
        		}
        		break;
    		}
    	}
    }
    
    public ActionForward create(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        CategoryMaintActionForm form = (CategoryMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        initSiteProfiles(form, site);
        
        Category referenceCategory = CategoryDAO.load(site.getSiteId(), Format.getLong(form.getCreateCatId()));
        Category parent = null;
        int seqNum = 0;
        if (form.getCreateMode().equals("C")) {	// append child node
        	for (Category child : referenceCategory.getCategoryChildren()) {
        		if (child.getSeqNum() >= seqNum) {
        			seqNum = child.getSeqNum() + 1;
        		}
        	}
        	parent = referenceCategory;
        } else if (form.getCreateMode().equals("B")) {  // before current
        	parent = referenceCategory.getCategoryParent();
        	seqNum = referenceCategory.getSeqNum();
        	for (Category child : parent.getCategoryChildren()) {
        		if (child.getSeqNum() >= seqNum) {
        			child.setSeqNum(child.getSeqNum() + 1);
        			em.persist(child);
        		}
        	}
        
        } else if (form.getCreateMode().equals("A")) {  // after current
        	parent = referenceCategory.getCategoryParent();
        	seqNum = referenceCategory.getSeqNum() + 1;
        	for (Category child : parent.getCategoryChildren()) {
        		if (child.getSeqNum() >= seqNum) {
        			child.setSeqNum(child.getSeqNum() + 1);
        			em.persist(child);
        		}
        	}
        }
        
        int count = 0;
        String catShortTitle = "New category";
        boolean exist = true;
        while (exist) {
        	exist = false;
        	for (Category child : parent.getCategoryChildren()) {
        		if (child.getCategoryLanguage().getCatShortTitle().equals(catShortTitle)) {
	        		exist = true;
	    	        count++;
	    	        catShortTitle = "New category " + count;
	        		break;
	        	}
        	}
        }
        
        Category category = new Category();
        category.setSite(site);
        category.setCategoryParent(parent);
        category.setSeqNum(seqNum);
        category.setPublished(Constants.PUBLISHED_YES);
        category.setRecUpdateBy(adminBean.getUser().getUserId());
        category.setRecCreateBy(adminBean.getUser().getUserId());
        category.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        category.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        parent.getCategoryChildren().add(category);
        
        CategoryLanguage categoryLanguage = new CategoryLanguage();
        categoryLanguage.setCatShortTitle(catShortTitle);
        categoryLanguage.setCatTitle("");
        categoryLanguage.setCatDesc("");
        categoryLanguage.setMetaKeywords("");
        categoryLanguage.setMetaDescription("");
        categoryLanguage.setCategory(category);
        categoryLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
        categoryLanguage.setRecCreateBy(adminBean.getUser().getUserId());
        categoryLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        categoryLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        categoryLanguage.setSiteProfileClass(site.getSiteProfileClassDefault());
        category.getCategoryLanguages().add(categoryLanguage);
        category.setCategoryLanguage(categoryLanguage);
        em.persist(categoryLanguage);

        String fullCategoryPath = getFullCategoryPath(category, site.getSiteId(), form.getSiteProfileClassDefaultId());
        category.setCatNaturalKey(Utility.encode(fullCategoryPath));
        em.persist(category);

        form.setCatId(Format.getLong(category.getCatId()));
        form.setCategoryParentId(category.getCategoryParent().getCatId().toString());
        form.setCatTitle("");
        form.setMetaKeywords("");
        form.setMetaDescription("");
        form.setCatShortTitle(categoryLanguage.getCatShortTitle());
        form.setCatDesc("");
        form.setPublished(category.getPublished() == Constants.PUBLISHED_YES ? true : false);
        form.setMode(Constants.MODE_UPDATE);
        String jsonCategoryTree = Utility.makeJSONCategoryTree(site.getSiteId(), form.getSiteProfileClassId(), form.isSiteProfileClassDefault()).toHtmlString();
        form.setJsonCategoryTree(jsonCategoryTree);
        form.setPublished(true);
        form.setSequence(false);
        form.setMode(Constants.MODE_CREATE);
        
        Vector<SiteDomainDisplayForm> vector = new Vector<SiteDomainDisplayForm>();
        for (SiteDomain siteDomain : site.getSiteDomains()) {
        	SiteDomainDisplayForm siteDomainDisplayForm = new SiteDomainDisplayForm();
        	siteDomainDisplayForm.setSiteDomainId(siteDomain.getSiteDomainId().toString());
        	siteDomainDisplayForm.setSiteName(siteDomain.getSiteDomainLanguage().getSiteName());
        	siteDomainDisplayForm.setChecked(false);
        	vector.add(siteDomainDisplayForm);
        }
        SiteDomainDisplayForm siteDomains[] = new SiteDomainDisplayForm[vector.size()];
        vector.copyInto(siteDomains);
        form.setSiteDomains(siteDomains);
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward update(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        CategoryMaintActionForm form = (CategoryMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        initSiteProfiles(form, site);
        
        String jsonCategoryTree = Utility.makeJSONCategoryTree(site.getSiteId(), form.getSiteProfileClassId(), form.isSiteProfileClassDefault()).toHtmlString();
        form.setJsonCategoryTree(jsonCategoryTree);
        String catId = (String) request.getParameter("catId");
        Category category = CategoryDAO.load(site.getSiteId(), Format.getLong(catId));

        form.setCatId(catId);
        if (category.getCategoryParent() != null) {
        	form.setCategoryParentId(Format.getLong(category.getCategoryParent().getCatId()));;
        }
        form.setCatTitle(category.getCategoryLanguage().getCatTitle());
        form.setCatShortTitle(category.getCategoryLanguage().getCatShortTitle());
        form.setCatDesc(category.getCategoryLanguage().getCatDesc());
        form.setMetaKeywords(category.getCategoryLanguage().getMetaKeywords());
        form.setMetaDescription(category.getCategoryLanguage().getMetaDescription());
        form.setPublished(category.getPublished() == Constants.PUBLISHED_YES ? true : false);
        form.setMode(Constants.MODE_UPDATE);
        form.setSequence(false);
        
        Vector<SiteDomainDisplayForm> vector = new Vector<SiteDomainDisplayForm>();
        for (SiteDomain siteDomain : site.getSiteDomains()) {
        	SiteDomainDisplayForm siteDomainDisplayForm = new SiteDomainDisplayForm();
        	siteDomainDisplayForm.setSiteDomainId(siteDomain.getSiteDomainId().toString());
        	siteDomainDisplayForm.setSiteName(siteDomain.getSiteDomainLanguage().getSiteName());
        	boolean found = false;
        	for (SiteDomain s : category.getSiteDomains()) {
        		if (s.getSiteDomainId().equals(siteDomain.getSiteDomainId())) {
        			found = true;
        			break;
        		}
        	}
        	siteDomainDisplayForm.setChecked(found);
        	vector.add(siteDomainDisplayForm);
        }
        SiteDomainDisplayForm siteDomains[] = new SiteDomainDisplayForm[vector.size()];
        vector.copyInto(siteDomains);
        form.setSiteDomains(siteDomains);
    
        if (!form.isSiteProfileClassDefault()) {
        	initLanguageInfo(category, form);
        }
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    } 
    
    public ActionForward save(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        CategoryMaintActionForm form = (CategoryMaintActionForm) actionForm;
        initSiteProfiles(form, site);
  
		ActionMessages errors = validate(form, site.getSiteId());
		if (errors.size() != 0) {
			saveMessages(request, errors);
	        String jsonCategoryTree = Utility.makeJSONCategoryTree(site.getSiteId(), form.getSiteProfileClassId(), form.isSiteProfileClassDefault()).toHtmlString();
	        form.setJsonCategoryTree(jsonCategoryTree);
			return actionMapping.findForward("error");
		}
		
        Category category = CategoryDAO.load(site.getSiteId(), Format.getLong(form.getCatId()));
        boolean update = false;

        if (form.isSiteProfileClassDefault()) {
        	category.getCategoryLanguage().setCatShortTitle(form.getCatShortTitle());
        	category.getCategoryLanguage().setMetaKeywords(form.getMetaKeywords());
        	category.getCategoryLanguage().setMetaDescription(form.getMetaDescription());
	        String fullCategoryPath = getFullCategoryPath(category, site.getSiteId(), form.getSiteProfileClassDefaultId());
	        fullCategoryPath = Utility.encode(fullCategoryPath);
	        if (fullCategoryPath.length() > 255) {
	       		errors.add("catShortTitle", new ActionMessage("error.category.naturalkey.toolong"));    		
				saveMessages(request, errors);
		        String jsonCategoryTree = Utility.makeJSONCategoryTree(site.getSiteId(), form.getSiteProfileClassId(), form.isSiteProfileClassDefault()).toHtmlString();
		        form.setJsonCategoryTree(jsonCategoryTree);
				return actionMapping.findForward("error");
	        }
			int length = updateChildrenCategoryPath(category, site.getSiteId(), form.getSiteProfileClassDefaultId(), update);
			if (length > 255) {
	       		errors.add("catShortTitle", new ActionMessage("error.category.naturalkey.toolong"));    		
				saveMessages(request, errors);
		        String jsonCategoryTree = Utility.makeJSONCategoryTree(site.getSiteId(), form.getSiteProfileClassId(), form.isSiteProfileClassDefault()).toHtmlString();
		        form.setJsonCategoryTree(jsonCategoryTree);
				return actionMapping.findForward("error");
			}
			saveDefault(category, form, adminBean, fullCategoryPath);
        }
        else {
        	saveLanguage(category, form, adminBean);
        }
        
        category.getSiteDomains().clear();
        for (SiteDomainDisplayForm siteDomainDisplayForm : form.getSiteDomains()) {
        	if (siteDomainDisplayForm.isChecked()) {
        		SiteDomain siteDomain = SiteDomainDAO.load(Format.getLong(siteDomainDisplayForm.getSiteDomainId()));
        		category.getSiteDomains().add(siteDomain);
        	}
        }
        
		for (Menu menu : category.getMenus()) {
			menu.setItem(null);
			CacheDAO.removeByKeyPrefix(site.getSiteId(), Constants.CACHE_MENU + "." + menu.getMenuSetName());
		}

        String jsonCategoryTree = Utility.makeJSONCategoryTree(site.getSiteId(), form.getSiteProfileClassId(), form.isSiteProfileClassDefault()).toHtmlString();
        form.setJsonCategoryTree(jsonCategoryTree);
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public void saveLanguage(Category category, CategoryMaintActionForm form, AdminBean adminBean) throws Exception {
       	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Long siteProfileClassId = form.getSiteProfileClassId();
    	User user = adminBean.getUser();
    	Iterator<?> iterator = category.getCategoryLanguages().iterator();
    	boolean found = false;
    	CategoryLanguage categoryLanguage = null;
    	while (iterator.hasNext()) {
    		categoryLanguage = (CategoryLanguage) iterator.next();
    		if (categoryLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
    			found = true;
    			break;
    		}
    	}
    	if (!found) {
    		categoryLanguage = new CategoryLanguage();
    		categoryLanguage.setRecCreateBy(user.getUserId());
    		categoryLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		SiteProfileClass siteProfileClass = em.find(SiteProfileClass.class, siteProfileClassId);
    		categoryLanguage.setSiteProfileClass(siteProfileClass);
    		categoryLanguage.setCategory(category);
    	}
    	if (form.isCatTitleLangFlag()) {
    		categoryLanguage.setCatTitle(form.getCatTitleLang());
    	}
    	else {
    		categoryLanguage.setCatTitle(null);
    	}
    	if (form.isCatShortTitleLangFlag()) {
    		categoryLanguage.setCatShortTitle(form.getCatShortTitleLang());
    	}
    	else {
    		categoryLanguage.setCatShortTitle(null);
    	}
    	if (form.isCatDescLangFlag()) {
    		categoryLanguage.setCatDesc(form.getCatDescLang());
    	}
    	else {
    		categoryLanguage.setCatDesc(null);
    	}
    	if (form.isMetaKeywordsLangFlag()) {
    		categoryLanguage.setMetaKeywords(form.getMetaKeywordsLang());
    	}
    	else {
    		categoryLanguage.setMetaKeywords(null);
    	}
    	if (form.isMetaDescriptionLangFlag()) {
    		categoryLanguage.setMetaDescription(form.getMetaDescriptionLang());
    	}
    	else {
    		categoryLanguage.setMetaDescription(null);
    	}
    	categoryLanguage.setRecUpdateBy(user.getUserId());
    	categoryLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    	em.persist(categoryLanguage);
    }
    
    public void saveDefault(Category category, CategoryMaintActionForm form, AdminBean adminBean, String fullCategoryPath) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		
    	CategoryLanguage categoryLanguage = category.getCategoryLanguage();
    	categoryLanguage.setCatTitle(form.getCatTitle());
    	categoryLanguage.setCatDesc(form.getCatDesc());
    	categoryLanguage.setMetaKeywords(form.getMetaKeywords());
    	categoryLanguage.setMetaDescription(form.getMetaDescription());
    	categoryLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
    	categoryLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        
        category.setPublished(form.isPublished() ? Constants.PUBLISHED_YES : Constants.PUBLISHED_NO);
        category.setRecUpdateBy(adminBean.getUser().getUserId());
        category.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        
        category.setCatNaturalKey(fullCategoryPath);
        boolean update = true;
        updateChildrenCategoryPath(category, adminBean.getSite().getSiteId(), form.getSiteProfileClassDefaultId(), update);
        em.persist(categoryLanguage);
        em.persist(category);
    }
    
    public String getFullCategoryPath(Category category, String siteId, Long siteProfileClassDefaultId) throws Exception {
    	String path = category.getCategoryLanguage().getCatShortTitle();
    	Category parent = category.getCategoryParent();
    	while (parent != null) {
    		path = parent.getCategoryLanguage().getCatShortTitle() + " " + path;
    		parent = parent.getCategoryParent();
    	}
    	return path;
    }
    
    public int updateChildrenCategoryPath(Category category, String siteId, Long siteProfileClassDefaultId, boolean update) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	int max = 0;
    	for (Category child : category.getCategoryChildren()) {
    		String fullCategoryPath = Utility.encode(getFullCategoryPath(child, siteId, siteProfileClassDefaultId));
    		if (fullCategoryPath.length() > max) {
    			max = fullCategoryPath.length();
    		}
    		if (update) {
	    		child.setCatNaturalKey(fullCategoryPath);
	    		em.persist(child);
    		}
    		int length = updateChildrenCategoryPath(child, siteId, siteProfileClassDefaultId, update);
    		if (length > max) {
    			max = length;
    		}
    	}
    	return max;
    }
    
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        String siteId = site.getSiteId();
        CategoryMaintActionForm form = (CategoryMaintActionForm) actionForm;
        initSiteProfiles(form, site);
		
        try {
			cascadeRemoveCategory(Format.getLong(form.getCatId()), siteId);
	        em.flush();
//			em.getTransaction().commit();
        }
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.categories.constraint"));
				saveMessages(request, errors);
		        String jsonCategoryTree = Utility.makeJSONCategoryTree(site.getSiteId(), form.getSiteProfileClassId(), form.isSiteProfileClassDefault()).toHtmlString();
		        form.setJsonCategoryTree(jsonCategoryTree);
				return actionMapping.findForward("error");
			}
			throw e;
		}
		
        String jsonCategoryTree = Utility.makeJSONCategoryTree(site.getSiteId(), form.getSiteProfileClassId(), form.isSiteProfileClassDefault()).toHtmlString();
        form.setJsonCategoryTree(jsonCategoryTree);
        form.setMode("");
        form.setCatId("");
        form.setSiteDomains(new SiteDomainDisplayForm[0]);
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public void cascadeRemoveCategory(Long catId, String siteId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Category parent = CategoryDAO.load(siteId, catId);
    	for (Category childCategory : parent.getCategoryChildren()) {
    		cascadeRemoveCategory(childCategory.getCatId(), siteId);
    	}
    	Iterator<?> iterator = parent.getContents().iterator();
    	while (iterator.hasNext()) {
    		Content content = (Content) iterator.next();
    		content.getCategories().remove(parent);
    	}
    	
    	iterator = parent.getItems().iterator();
    	while (iterator.hasNext()) {
    		Item item = (Item) iterator.next();
    		item.getCategories().remove(parent);
    	}
    	
		for (Menu menu : parent.getMenus()) {
			menu.setItem(null);
			CacheDAO.removeByKeyPrefix(siteId, Constants.CACHE_MENU + "." + menu.getMenuSetName());
		}
    	
    	iterator = parent.getMenus().iterator();
    	while (iterator.hasNext()) {
    		Menu menu = (Menu) iterator.next();
    		menu.setCategory(null);
    	}
    	em.flush();
    	
    	for (CategoryLanguage categoryLanguage : parent.getCategoryLanguages()) {
    		em.remove(categoryLanguage);
    	}
    	em.remove(parent);
    	return;
    }
    
    public ActionForward showSequence(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        CategoryMaintActionForm form = (CategoryMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        String siteId = site.getSiteId();
        initSiteProfiles(form, adminBean.getSite());
        
        String jsonCategoryTree = Utility.makeJSONCategoryTree(site.getSiteId(), form.getSiteProfileClassId(), form.isSiteProfileClassDefault()).toHtmlString();
        form.setJsonCategoryTree(jsonCategoryTree);
        String catId = (String) request.getParameter("catId"); 
        Category category = CategoryDAO.load(siteId, Format.getLong(catId));
        form.setCatId(catId);
        if (category.getCategoryParent() != null) {
        	form.setCategoryParentId(category.getCategoryParent().getCatId().toString());
        }
        form.setCatTitle(category.getCategoryLanguage().getCatTitle());
        form.setCatShortTitle(category.getCategoryLanguage().getCatShortTitle());
        form.setCatDesc(category.getCategoryLanguage().getCatDesc());
        form.setMetaKeywords(category.getCategoryLanguage().getMetaKeywords());
        form.setMetaDescription(category.getCategoryLanguage().getMetaDescription());
        form.setPublished(category.getPublished() == Constants.PUBLISHED_YES ? true : false);
        form.setMode(Constants.MODE_UPDATE);
        form.setSequence(true);

        
        initListInfo(form, siteId);
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    } 
    

    public ActionForward resequence(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	CategoryMaintActionForm form = (CategoryMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        String siteId = site.getSiteId();
        initSiteProfiles(form, adminBean.getSite());
       
		ActionMessages errors = validateSequenceNum(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
	        String jsonCategoryTree = Utility.makeJSONCategoryTree(site.getSiteId(), form.getSiteProfileClassId(), form.isSiteProfileClassDefault()).toHtmlString();
	        form.setJsonCategoryTree(jsonCategoryTree);
		    form.setSequence(true);
			return actionMapping.findForward("error");
		}
        
		CategoryDisplayForm childCategories[] = form.getChildrenCategories();
        for (int i = 0; i < childCategories.length; i++) {
        	int seqNum = Format.getInt(childCategories[i].getSeqNum());
        	Long catId = Format.getLong(childCategories[i].getCatId());
    		Category category = new Category();
    		category = CategoryDAO.load(siteId, catId);
    		category.setSeqNum(seqNum);
    		// em.update(category);
        }
        String jsonCategoryTree = Utility.makeJSONCategoryTree(site.getSiteId(), form.getSiteProfileClassId(), form.isSiteProfileClassDefault()).toHtmlString();
        form.setJsonCategoryTree(jsonCategoryTree);
	    form.setSequence(true);
     
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward removeSelected(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	CategoryMaintActionForm form = (CategoryMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        initSiteProfiles(form, site);
        
        try {
	        CategoryDisplayForm childrenCategories[] = form.getChildrenCategories();
	        for (int i = 0; i < childrenCategories.length; i++) {
	        	if (childrenCategories[i].isRemove()) {
		        	Long catId = Format.getLong(childrenCategories[i].getCatId());
		        	cascadeRemoveCategory(catId, site.getSiteId());
		        }
	        }
			em.getTransaction().commit();
	        em = JpaConnection.getInstance().getCurrentEntityManager();
			em.getTransaction().begin();
        }
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.categories.constraint"));
				saveMessages(request, errors);
		        String jsonCategoryTree = Utility.makeJSONCategoryTree(site.getSiteId(), form.getSiteProfileClassId(), form.isSiteProfileClassDefault()).toHtmlString();
		        form.setJsonCategoryTree(jsonCategoryTree);
		        form.setSequence(true);
				return actionMapping.findForward("error");
			}
			throw e;
		}
        
        String jsonCategoryTree = Utility.makeJSONCategoryTree(site.getSiteId(), form.getSiteProfileClassId(), form.isSiteProfileClassDefault()).toHtmlString();
        form.setJsonCategoryTree(jsonCategoryTree);
        initListInfo(form, site.getSiteId());
        form.setSequence(true);
      
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionMessages validateSequenceNum(CategoryMaintActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	CategoryDisplayForm childrenCategories[] = form.getChildrenCategories();
    	for (int i = 0; i < childrenCategories.length; i++) {
    		if (!Format.isInt(childrenCategories[i].getSeqNum())) {
    			errors.add("seqNum_" + i, new ActionMessage("error.int.invalid"));
    		}
    	}
    	return errors;
    }

    protected void initListInfo(CategoryMaintActionForm form, String siteId) throws Exception {
    	Category parent = CategoryDAO.load(siteId, Format.getLong(form.getCatId()));
		Vector<CategoryDisplayForm> vector = new Vector<CategoryDisplayForm>();
    	for (Category childCategory : parent.getCategoryChildren()) {			
			CategoryDisplayForm display = new CategoryDisplayForm();
			display.setCatId(Format.getLong(childCategory.getCatId()));
			display.setSeqNum(Format.getInt(childCategory.getSeqNum()));
			display.setCatShortTitle(childCategory.getCategoryLanguage().getCatShortTitle());
			display.setCatTitle(childCategory.getCategoryLanguage().getCatTitle());
			display.setCatDesc(childCategory.getCategoryLanguage().getCatDesc());
			display.setPublished(String.valueOf(childCategory.getPublished()));
			vector.add(display);
    	}
		CategoryDisplayForm childrenCategories[] = new CategoryDisplayForm[vector.size()];
		vector.copyInto(childrenCategories);
		form.setChildrenCategories(childrenCategories);
    }
    
	private String getJSONCustomAttributes(Category category) throws Exception {
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		
		Vector<JSONEscapeObject> vector = new Vector<JSONEscapeObject>();
		for (CustomAttribute customAttribute: category.getCustomAttributes()) {
			JSONEscapeObject detail = new JSONEscapeObject();
			detail.put("customAttribId", customAttribute.getCustomAttribId());
			detail.put("customAttribDesc", customAttribute.getCustomAttributeLanguage().getCustomAttribDesc());
			vector.add(detail);
		}
		jsonResult.put("customAttributes", vector);
		return jsonResult.toHtmlString();
	}
	
	public ActionForward getCustomAttributes(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Throwable {
		CategoryMaintActionForm form = (CategoryMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		Long catId = Format.getLong(form.getCatId());
		Category category = CategoryDAO.load(site.getSiteId(), catId);
		streamWebService(response, getJSONCustomAttributes(category));
		return null;
	}
	
	public ActionForward removeCustomAttributes(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Throwable {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	CategoryMaintActionForm form = (CategoryMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);

		Long catId = Format.getLong(form.getCatId());
		Category category = CategoryDAO.load(site.getSiteId(), catId);
		String customAttribIds[] = form.getCustomAttribIds();
		if (customAttribIds != null) {
			for (int i = 0; i < customAttribIds.length; i++) {
				CustomAttribute customAttribute = em.find(CustomAttribute.class, Format.getLong(customAttribIds[i]));
				category.getCustomAttributes().remove(customAttribute);
			}
		}
		streamWebService(response, jsonResult.toHtmlString());
		return null;
	}
	
	public ActionForward addCustomAttribute(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Throwable {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		CategoryMaintActionForm form = (CategoryMaintActionForm) actionForm;
	
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		
		Long customAttribId = Format.getLong(form.getCustomAttribId());
		CustomAttribute customAttribute = em.find(CustomAttribute.class, customAttribId);
		Long catId = Format.getLong(form.getCatId());
		Category category = em.find(Category.class, catId);
		Iterator<?> iterator = category.getCustomAttributes().iterator();
		boolean found = false;
		while (iterator.hasNext()) {
			CustomAttribute attribute = (CustomAttribute) iterator.next();
			if (attribute.getCustomAttribId().equals(customAttribute.getCustomAttribId())) {
				found = true;
			}
		}
		
		if (!found) {
			category.getCustomAttributes().add(customAttribute);
			em.persist(category);
		}

		streamWebService(response, jsonResult.toHtmlString());
		return null;
	}

    public ActionMessages validate(CategoryMaintActionForm form, String siteId) throws Exception { 
    	ActionMessages errors = new ActionMessages();
    	if (Format.isNullOrEmpty(form.getCatShortTitle())) {
    		errors.add("catShortTitle", new ActionMessage("error.string.required"));    		
    	}
    	
    	if (form.isSiteProfileClassDefault()) {
	    	Category master = CategoryDAO.load(siteId, Format.getLong(form.getCatId()));
	    	for (Category category : master.getCategoryChildren()) {
				CategoryLanguage categoryLanguage = null;
				for (CategoryLanguage language : category.getCategoryLanguages()) {
					if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassDefaultId())) {
						categoryLanguage = language;
					}
				}
				
	    		if (categoryLanguage.getCatShortTitle().equals(form.getCatShortTitle())) {
	        		errors.add("catShortTitle", new ActionMessage("error.record.duplicate"));    		
	    		}	
	    	}
    	}
    	
    	return errors;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        map.put("create", "create");
        map.put("update", "update");
        map.put("save", "save");
        map.put("remove", "remove");
        map.put("resequence", "resequence");
        map.put("removeSelected", "removeSelected");
        map.put("showSequence", "showSequence");
        map.put("language", "language");
        map.put("translate", "translate");
        map.put("getCustomAttributes", "getCustomAttributes");
        map.put("removeCustomAttributes", "removeCustomAttributes");
        map.put("addCustomAttribute", "addCustomAttribute");
        return map;
    }
}