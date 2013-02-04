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

package com.jada.admin.content;

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

import javax.persistence.Query;
import javax.persistence.EntityManager;
import com.jada.util.JSONEscapeObject;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.CacheDAO;
import com.jada.dao.ContentDAO;
import com.jada.dao.ContentImageDAO;
import com.jada.dao.MenuDAO;
import com.jada.dao.CategoryDAO;
import com.jada.dao.SiteProfileClassDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Category;
import com.jada.jpa.entity.Comment;
import com.jada.jpa.entity.Content;
import com.jada.jpa.entity.ContentImage;
import com.jada.jpa.entity.ContentLanguage;
import com.jada.jpa.entity.Menu;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.jpa.entity.User;
import com.jada.search.Indexer;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.translator.BingTranslate;
import com.jada.util.ImageScaler;
import com.jada.util.CategorySearchUtil;
import com.jada.util.Utility;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

public class ContentMaintAction
    extends AdminLookupDispatchAction {
    Logger logger = Logger.getLogger(ContentMaintAction.class);

    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse)
        throws Throwable {

        ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
        if (form == null) {
            form = new ContentMaintActionForm();
        }
		AdminBean adminBean = getAdminBean(httpServletRequest);
        initSiteProfiles(form, adminBean.getSite());

        Content content = new Content();
        createAdditionalInfo(adminBean, content, form);
        
        form.setContentRating(Format.getFloat(0));
        form.setContentRatingCount("0");
        form.setPublished(true);
        form.setContentPublishOn(Format.getDate(new Date(System.currentTimeMillis())));
        form.setContentExpireOn(Format.getDate(Format.HIGHDATE));
        form.setContentId("-1");
        form.setMode("C");

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
		ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
		if (form == null) {
			form = new ContentMaintActionForm();
		}
		
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
		Content content = (Content) em.find(Content.class, Format.getLong(form.getContentId()));
        createAdditionalInfo(getAdminBean(request), content, form);
		copyProperties(form, content);
		
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
		ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
		
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
		Content content = (Content) em.find(Content.class, Format.getLong(form.getContentId()));
        createAdditionalInfo(getAdminBean(request), content, form);
        

        BingTranslate translator = new BingTranslate(form.getFromLocale(), form.getToLocale(), site);
		
		if (!form.isSiteProfileClassDefault()) {
			copyProperties(form, content);
		}
		
		try {
			form.setContentTitleLangFlag(true);
			form.setContentShortDescLangFlag(true);
			form.setContentDescLangFlag(true);
			form.setPageTitleLangFlag(true);
			form.setMetaKeywordsLangFlag(true);
			form.setMetaDescriptionLangFlag(true);
			form.setContentTitleLang(translator.translate(content.getContentLanguage().getContentTitle()));
			form.setContentShortDescLang(translator.translate(content.getContentLanguage().getContentShortDesc()));
			form.setContentDescLang(translator.translate(content.getContentLanguage().getContentDesc()));
			form.setPageTitleLang(translator.translate(content.getContentLanguage().getPageTitle()));
			form.setMetaKeywordsLang(translator.translate(content.getContentLanguage().getMetaKeywords()));
			form.setMetaDescriptionLang(translator.translate(content.getContentLanguage().getMetaDescription()));
		}
		catch (Exception e) {
			logger.error(e);
	    	ActionMessages errors = new ActionMessages();
	    	errors.add("error", new ActionMessage("error.google.translate"));
	    	saveMessages(request, errors);
			return actionMapping.findForward("error");
		}
		
		FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}

    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest httpServletRequest,
                              HttpServletResponse httpServletResponse)
        throws Throwable {

        ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
        if (form == null) {
            form = new ContentMaintActionForm();
        }
		AdminBean adminBean = getAdminBean(httpServletRequest);
		Site site = adminBean.getSite();
		initSiteProfiles(form, adminBean.getSite());
		String id = httpServletRequest.getParameter("contentId");
		Long contentId = null;
		if (id != null) {
			contentId = Format.getLong(id);
		}
		else {
			contentId = Format.getLong(form.getContentId());
		}

        Content content = new Content();
        content = ContentDAO.load(site.getSiteId(), contentId);
        copyProperties(form, content);
        form.setMode("U");
        FormUtils.setFormDisplayMode(httpServletRequest, form, FormUtils.EDIT_MODE);
        
		createAdditionalInfo(adminBean, content, form);

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
							  HttpServletRequest httpServletRequest,
							  HttpServletResponse httpServletResponse) 
		throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		boolean insertMode = false;
		ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}
//		form.setContentTitle(StringEscapeUtils.escapeHtml(form.getContentTitle()));
//		form.setContentTitleLang(StringEscapeUtils.escapeHtml(form.getContentTitleLang()));

		AdminBean adminBean = getAdminBean(httpServletRequest);
		Site site = adminBean.getSite();
		initSiteProfiles(form, adminBean.getSite());

		Content content = new Content();
		if (!insertMode) {
			content = ContentDAO.load(site.getSiteId(), Format.getLong(form.getContentId()));
		}

		ActionMessages errors = validate(form, site.getSiteId(), insertMode);
		if (errors.size() != 0) {
			saveMessages(httpServletRequest, errors);
			createAdditionalInfo(adminBean, content, form);
			return mapping.findForward("error");
		}

		if (insertMode) {
			content.setSite(site);
			content.setContentRating(new Float(0));
			content.setContentRatingCount(new Integer(0));
			content.setContentHitCounter(new Integer(0));
			content.setRecCreateBy(adminBean.getUser().getUserId());
			content.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		
		if (form.isSiteProfileClassDefault()) {
			saveDefault(content, form, adminBean);
			form.setRecUpdateBy(content.getRecUpdateBy());
			form.setRecUpdateDatetime(Format.getFullDatetime(content.getRecUpdateDatetime()));
			User user = adminBean.getUser();
			content.setUser(user);
			if (insertMode) {
				em.persist(content);
				form.setContentId(Format.getLong(content.getContentId()));
			}
			else {
				// em.update(content);
			}
		}
		else {
			saveLanguage(content, form, adminBean);
		}
		CategorySearchUtil.contentDescSearchUpdate(content, site, adminBean);

		Iterator<?> iterator = content.getMenus().iterator();
		while (iterator.hasNext()) {
			Menu menu = (Menu) iterator.next();
			CacheDAO.removeByKeyPrefix(site.getSiteId(), Constants.CACHE_MENU + "." + menu.getMenuSetName());
		}
		Indexer.getInstance(site.getSiteId()).updateContent(content);
		
        copyProperties(form, content);
		createAdditionalInfo(adminBean, content, form);
		
        form.setMode("U");
        FormUtils.setFormDisplayMode(httpServletRequest, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	public void saveLanguage(Content content, ContentMaintActionForm form, AdminBean adminBean) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Long siteProfileClassId = form.getSiteProfileClassId();
    	User user = adminBean.getUser();
    	Iterator<?> iterator = content.getContentLanguages().iterator();
    	boolean found = false;
    	ContentLanguage contentLanguage = null;
    	while (iterator.hasNext()) {
    		contentLanguage = (ContentLanguage) iterator.next();
    		if (contentLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
    			found = true;
    			break;
    		}
    	}
    	if (!found) {
    		contentLanguage = new ContentLanguage();
    		contentLanguage.setRecCreateBy(user.getUserId());
    		contentLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		contentLanguage.setContentImageOverride(String.valueOf(Constants.VALUE_NO));
    		SiteProfileClass siteProfileClass = (SiteProfileClass) em.find(SiteProfileClass.class, siteProfileClassId);
    		contentLanguage.setSiteProfileClass(siteProfileClass);
    		contentLanguage.setContent(content);
    		content.getContentLanguages().add(contentLanguage);
    	}
    	if (form.isContentTitleLangFlag()) {
    		contentLanguage.setContentTitle(form.getContentTitleLang());
    	}
    	else {
    		contentLanguage.setContentTitle(null);
    	}
    	if (form.isContentShortDescLangFlag()) {
    		contentLanguage.setContentShortDesc(form.getContentShortDescLang());
    	}
    	else {
    		contentLanguage.setContentShortDesc(null);
    	}
    	if (form.isContentDescLangFlag()) {
    		contentLanguage.setContentDesc(form.getContentDescLang());
    	}
    	else {
    		contentLanguage.setContentDesc(null);
    	}
    	if (form.isPageTitleLangFlag()) {
    		contentLanguage.setPageTitle(form.getPageTitleLang());
    	}
    	else {
    		contentLanguage.setPageTitle(null);
    	}
    	if (form.isMetaKeywordsLangFlag()) {
    		contentLanguage.setMetaKeywords(form.getMetaKeywordsLang());
    	}
    	else {
    		contentLanguage.setMetaKeywords(null);
    	}
    	if (form.isMetaDescriptionLangFlag()) {
    		contentLanguage.setMetaDescription(form.getMetaDescriptionLang());
    	}
    	else {
    		contentLanguage.setMetaDescription(null);
    	}
    	contentLanguage.setRecUpdateBy(user.getUserId());
    	contentLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    	em.persist(contentLanguage);
	}
	
	public void saveDefault(Content content, ContentMaintActionForm form, AdminBean adminBean) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	ContentLanguage contentLanguage = content.getContentLanguage();
    	if (contentLanguage == null) {
    		contentLanguage = new ContentLanguage();
    		contentLanguage.setContentImageOverride(String.valueOf(Constants.VALUE_NO));
    		contentLanguage.setRecCreateBy(adminBean.getUser().getUserId());
    		contentLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		SiteProfileClass siteProfileClass = SiteProfileClassDAO.load(form.getSiteProfileClassDefaultId());
    		contentLanguage.setSiteProfileClass(siteProfileClass);
    		content.getContentLanguages().add(contentLanguage);
    		content.setContentLanguage(contentLanguage);
    	}
    	
		contentLanguage.setContentTitle(form.getContentTitle());
		contentLanguage.setContentShortDesc(form.getContentShortDesc());
		contentLanguage.setContentDesc(form.getContentDesc());
		contentLanguage.setPageTitle(form.getPageTitle());
		contentLanguage.setMetaKeywords(form.getMetaKeywords());
		contentLanguage.setMetaDescription(form.getMetaDescription());
		contentLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
		contentLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));

		content.setContentNaturalKey(Utility.encode(form.getContentTitle()));
		content.setContentPublishOn(Format.getDate(form.getContentPublishOn()));
		content.setContentExpireOn(Format.getDate(form.getContentExpireOn()));
		content.setRecUpdateBy(adminBean.getUser().getUserId());
		content.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		content.setPublished(form.isPublished() ? 'Y' : 'N');
		em.persist(contentLanguage);
	}

	public ActionForward remove(ActionMapping mapping, 
				  ActionForm actionForm,
				  HttpServletRequest httpServletRequest,
				  HttpServletResponse httpServletResponse) 
		throws Throwable {

		ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(httpServletRequest);
        initSiteProfiles(form, adminBean.getSite());
		Site site = adminBean.getSite();
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		
		Content content = ContentDAO.load(site.getSiteId(), Format.getLong(form.getContentId()));
		// in case problem is encountered
		createAdditionalInfo(adminBean, content, form);
		
		try {
			em.remove(content);
			for (ContentLanguage contentLanguage : content.getContentLanguages()) {
				if (contentLanguage.getImage() != null) {
					em.remove(contentLanguage.getImage());
				}
				for (ContentImage image : contentLanguage.getImages()) {
					em.remove(image);
				}
				em.remove(contentLanguage);
			}
			Iterator<?> iterator = content.getMenus().iterator();
			while (iterator.hasNext()) {
				Menu menu = (Menu) iterator.next();
				menu.setContent(null);
				CacheDAO.removeByKeyPrefix(site.getSiteId(), Constants.CACHE_MENU + "." + menu.getMenuSetName());
			}
			content.setContentLanguage(null);
			em.getTransaction().commit();
		}
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.content.constraint"));
				saveMessages(httpServletRequest, errors);
				return mapping.findForward("error");
			}
			throw e;
		}
		
		Indexer.getInstance(site.getSiteId()).removeContent(content);
		
		return mapping.findForward("removeConfirm");
	}
	
	public ActionForward resetCounter(ActionMapping mapping, 
									  ActionForm actionForm,
									  HttpServletRequest request,
									  HttpServletResponse response) 
		throws Throwable {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();

		Content content = new Content();
		content = ContentDAO.load(site.getSiteId(), Format.getLong(form.getContentId()));
		content.setContentHitCounter(new Integer(0));
		content.setRecUpdateBy(adminBean.getUser().getUserId());
		content.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("recUpdateBy", content.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(content.getRecUpdateDatetime()));
		String jsonString = jsonResult.toHtmlString();
		this.streamWebService(response, jsonString);
		em.getTransaction().commit();
    	return null;
	}
	
	public ActionForward comments(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();

		Content content = new Content();
		content = ContentDAO.load(site.getSiteId(), Format.getLong(form.getContentId()));
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		Iterator<?> iterator = content.getComments().iterator();
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

	public ActionForward showCategories(ActionMapping mapping, 
			   ActionForm actionForm,
			   HttpServletRequest request,
			   HttpServletResponse response) 
			throws Throwable {
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
		initSiteProfiles(form, adminBean.getSite());
		Content content = new Content();
		content = ContentDAO.load(site.getSiteId(), Format.getLong(form.getContentId()));
		JSONEscapeObject jsonResult = createJsonSelectedCategories(site.getSiteId(), content);
		String jsonString = jsonResult.toHtmlString();
		this.streamWebService(response, jsonString);
		return null;
	}
	
	public ActionForward addCategories(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
        initSiteProfiles(form, adminBean.getSite());
		String contentId = request.getParameter("contentId");
		
	   	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Content content = new Content();
		content = ContentDAO.load(site.getSiteId(), Format.getLong(contentId));
		String catIds[] = form.getAddCategories();
		if (catIds != null) {
			for (String catId : catIds) {
				Category category = CategoryDAO.load(site.getSiteId(), Format.getLong(catId));
				content.getCategories().add(category);
				content.setRecUpdateBy(adminBean.getUser().getUserId());
				content.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			}
		}
    	JSONEscapeObject jsonResult = createJsonSelectedCategories(site.getSiteId(), content);
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("recUpdateBy", content.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(content.getRecUpdateDatetime()));
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
		em.getTransaction().commit();
    	return null;
	}

	
	public JSONEscapeObject createJsonSelectedCategories(String siteId, Content content) throws Exception {
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		Vector<JSONEscapeObject> categories = new Vector<JSONEscapeObject>();
		for (Category category : content.getCategories()) {
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
	
	public ActionForward removeCategories(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
		ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
        initSiteProfiles(form, site);
		
	   	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Content content = new Content();
		content = ContentDAO.load(site.getSiteId(), Format.getLong(form.getContentId()));
		
		String catIds[] = form.getRemoveCategories();
		if (catIds != null) {
			for (String catId : catIds) {
				Category category = CategoryDAO.load(site.getSiteId(), Format.getLong(catId));
				content.getCategories().remove(category);
			}
		}
		
		content.setRecUpdateBy(adminBean.getUser().getUserId());
		content.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
	
    	JSONEscapeObject jsonResult = createJsonSelectedCategories(site.getSiteId(), content);
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("recUpdateBy", content.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(content.getRecUpdateDatetime()));
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
		em.getTransaction().commit();
		return null;
	}
	
	public JSONEscapeObject createJsonSelectedMenus(String siteId, Content content, Long siteProfileClassId) throws Exception {
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		Iterator<?> iterator = content.getMenus().iterator();
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

	public ActionForward removeMenus(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
        initSiteProfiles(form, site);
		
		Content content = new Content();
		content = ContentDAO.load(site.getSiteId(), Format.getLong(form.getContentId()));

		String menuIds[] = form.getRemoveMenus();
		if (menuIds != null) {
			for (int i = 0; i < menuIds.length; i++) {
				Menu menu = new Menu();
				menu = MenuDAO.load(site.getSiteId(), Format.getLong(menuIds[i]));
				menu.setContent(null);
				menu.setMenuUrl("");
				menu.setMenuType("");
				CacheDAO.removeByKeyPrefix(site.getSiteId(), Constants.CACHE_MENU + "." + menu.getMenuSetName());
			}
		}
		content.setRecUpdateBy(adminBean.getUser().getUserId());
		content.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.flush();
		
		JSONEscapeObject jsonResult = createJsonSelectedMenus(site.getSiteId(), content, form.getSiteProfileClassId());
		jsonResult.put("recUpdateBy", content.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(content.getRecUpdateDatetime()));
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
		ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		
		Content content = new Content();
		content = ContentDAO.load(site.getSiteId(), Format.getLong(form.getContentId()));

		String menuIds[] = form.getAddMenus();
		if (menuIds != null) {
			for (int i = 0; i < menuIds.length; i++) {
				Menu menu = new Menu();
				menu = MenuDAO.load(site.getSiteId(), Format.getLong(menuIds[i]));
				menu.setContent(content);
				menu.setItem(null);
				menu.setCategory(null);
				menu.setMenuUrl("");
				menu.setMenuWindowMode(form.getMenuWindowMode());
				menu.setMenuWindowTarget(form.getMenuWindowTarget());
				menu.setMenuType(Constants.MENU_CONTENT);
				CacheDAO.removeByKeyPrefix(site.getSiteId(), Constants.CACHE_MENU + "." + menu.getMenuSetName());
			}
		}
		content.setRecUpdateBy(adminBean.getUser().getUserId());
		content.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.flush();
		
		JSONEscapeObject jsonResult = createJsonSelectedMenus(site.getSiteId(), content, form.getSiteProfileClassId());
		jsonResult.put("recUpdateBy", content.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(content.getRecUpdateDatetime()));
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
		em.getTransaction().commit();
    	return null;
	}
	
	public ActionForward showImages(ActionMapping mapping, 
				 				   ActionForm actionForm,
				 				   HttpServletRequest request,
				 				   HttpServletResponse response) 
		throws Throwable {
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
		initSiteProfiles(form, adminBean.getSite());
		Content content = new Content();
		content = ContentDAO.load(site.getSiteId(), Format.getLong(form.getContentId()));
		JSONEscapeObject jsonResult = createJsonImages(site.getSiteId(), content, form);
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
		ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, adminBean.getSite());
		
		MessageResources resources = this.getResources(request);
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		
		Content content = new Content();
		content = ContentDAO.load(site.getSiteId(), Format.getLong(form.getKey()));

		FormFile file = form.getFile();
		byte fileData[] = file.getFileData();
		if (Format.isNullOrEmpty(file.getFileName())) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			jsonResult.put("message", resources.getMessage("error.string.required")); 
			streamWebService(response, jsonResult.toHtmlString());
	        return null;
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
		
		ContentLanguage contentLanguage = null;
		for (ContentLanguage language : content.getContentLanguages()) {
			if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassId())) {
				contentLanguage = language;
				break;
			}
		}
		
		if (form.isSiteProfileClassDefault()) {
			ContentImage contentImage = new ContentImage();
			contentImage.setImageName(file.getFileName());
			contentImage.setContentType("image/jpeg");
			contentImage.setImageValue(scaler.getBytes());
			contentImage.setImageHeight(scaler.getHeight());
			contentImage.setImageWidth(scaler.getWidth());
			contentImage.setRecUpdateBy(adminBean.getUser().getUserId());
			contentImage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			contentImage.setRecCreateBy(adminBean.getUser().getUserId());
			contentImage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			em.persist(contentImage);
			if (contentLanguage.getImage() == null) {
				contentLanguage.setImage(contentImage);
			}
			else {
				contentImage.setContentLanguage(contentLanguage);
				contentLanguage.getImages().add(contentImage);
			}
			contentLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
			contentLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			em.persist(contentLanguage);
		}
		else {
			if (contentLanguage == null) {
				contentLanguage = new ContentLanguage();
				contentLanguage.setContent(content);
				contentLanguage.setRecCreateBy(adminBean.getUser().getUserId());
				contentLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
				SiteProfileClass siteProfileClass = SiteProfileClassDAO.load(form.getSiteProfileClassId());
				contentLanguage.setSiteProfileClass(siteProfileClass);
				em.persist(contentLanguage);
			}
			ContentImage contentImage = new ContentImage();
			contentImage.setImageName(file.getFileName());
			contentImage.setContentType("image/jpeg");
			contentImage.setImageValue(scaler.getBytes());
			contentImage.setImageHeight(scaler.getHeight());
			contentImage.setImageWidth(scaler.getWidth());
			contentImage.setRecUpdateBy(adminBean.getUser().getUserId());
			contentImage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			contentImage.setRecCreateBy(adminBean.getUser().getUserId());
			contentImage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			em.persist(contentImage);
			if (contentLanguage.getImage() == null) {
				contentLanguage.setImage(contentImage);
			}
			else {
				contentImage.setContentLanguage(contentLanguage);
				contentLanguage.getImages().add(contentImage);
			}
			contentLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
			contentLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			em.persist(contentImage);
		}
		
		jsonResult = createJsonImages(site.getSiteId(), content, form);
		jsonResult.put("recUpdateBy", content.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(content.getRecUpdateDatetime()));
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
        return null;
	}
	
	public JSONEscapeObject createJsonImages(String siteId, Content content, ContentMaintActionForm form) throws Exception {
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("isSiteProfileClassDefault", form.isSiteProfileClassDefault());
		
		Vector<JSONEscapeObject> vector = new Vector<JSONEscapeObject>();
		ContentLanguage contentLanguageDefault = null;
		ContentLanguage contentLanguage = null;
		for (ContentLanguage language : content.getContentLanguages()) {
			if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassDefaultId())) {
				contentLanguageDefault = language;
			}
			if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassId())) {
				contentLanguage = language;
			}
		}
		
		ContentLanguage effectiveLanguage = contentLanguageDefault;
		if (!form.isSiteProfileClassDefault() && contentLanguage != null) {
			if (contentLanguage.getContentImageOverride().equals(String.valueOf(Constants.VALUE_YES))) {
				effectiveLanguage = contentLanguage;
			}
		}
		
		ContentImage defaultImage = effectiveLanguage.getImage();
		if (defaultImage != null) {
			JSONEscapeObject jsonDefaultImage = new JSONEscapeObject();
			jsonDefaultImage.put("imageId", defaultImage.getImageId());
			jsonDefaultImage.put("imageName", defaultImage.getImageName());
			jsonDefaultImage.put("isLanguageDefault", true);
			jsonResult.put("defaultImage", jsonDefaultImage);
		}
		
		Iterator<?> iterator = effectiveLanguage.getImages().iterator();
		while (iterator.hasNext()) {
			ContentImage image = (ContentImage) iterator.next();
			JSONEscapeObject jsonImage = new JSONEscapeObject();
			jsonImage.put("imageId", image.getImageId());
			jsonImage.put("imageName", image.getImageName());
			jsonImage.put("isLanguageDefault", true);
			vector.add(jsonImage);
		}
		jsonResult.put("images", vector);
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("recUpdateBy", content.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(content.getRecUpdateDatetime()));
		return jsonResult;
	}
	
	public ActionForward overrideImages(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		initSiteProfiles(form, adminBean.getSite());
		
		Content content = ContentDAO.load(adminBean.getSite().getSiteId(), Format.getLong(form.getContentId()));
	  	boolean found = false;
	  	Iterator<?> iterator = content.getContentLanguages().iterator();
	  	ContentLanguage contentLanguage = null;
		while (iterator.hasNext()) {
			contentLanguage = (ContentLanguage) iterator.next();
			if (contentLanguage.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassId())) {
				found = true;
				break;
			}
		}
		if (!found) {
			SiteProfileClass siteProfileClass = (SiteProfileClass) em.find(SiteProfileClass.class, form.getSiteProfileClassId());
			contentLanguage = new ContentLanguage();
			contentLanguage.setContent(content);
			contentLanguage.setRecCreateBy(adminBean.getUser().getUserId());
			contentLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			contentLanguage.setSiteProfileClass(siteProfileClass);
		}
		if (form.getImagesOverride().equalsIgnoreCase("true")) {
			contentLanguage.setContentImageOverride(String.valueOf(Constants.VALUE_YES));
		}
		else {
			ContentImage contentImage = null;
			if (contentLanguage.getImage() != null) {
				contentImage = contentLanguage.getImage();
				em.remove(contentImage);
				contentLanguage.setImage(null);
			}
			iterator = contentLanguage.getImages().iterator();
			while (iterator.hasNext()) {
				contentImage = (ContentImage) iterator.next();
				em.remove(contentImage);
			}
			contentLanguage.setContentImageOverride(String.valueOf(Constants.VALUE_NO));
		}
		contentLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
		contentLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(contentLanguage);
		
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
		ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, adminBean.getSite());
		
		Content content = new Content();
		content = ContentDAO.load(site.getSiteId(), Format.getLong(form.getContentId()));
		String imageIds[] = form.getRemoveImages();
		
		ContentLanguage contentLanguage = null;
		for (ContentLanguage language : content.getContentLanguages()) {
			if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassId())) {
				contentLanguage = language;
				break;
			}
		}
		if (contentLanguage != null) {
			ContentImage defaultImage = contentLanguage.getImage();
			if (imageIds != null) {
				for (int i = 0; i < imageIds.length; i++) {
					if (defaultImage != null && defaultImage.getImageId().equals(Format.getLong(imageIds[i]))) {
						contentLanguage.setImage(null);
						em.persist(contentLanguage);
						em.remove(defaultImage);
						defaultImage = null;
					}
					else {
						ContentImage contentImage = ContentImageDAO.load(site.getSiteId(), Format.getLong(imageIds[i]));
						contentLanguage.getImages().remove(contentImage);
						em.remove(contentImage);
					}
				}
			}
			contentLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
			contentLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			
			if (contentLanguage.getImage() == null) {
				Set<?> images = contentLanguage.getImages();
				if (!images.isEmpty()) {
					ContentImage contentImage = (ContentImage) images.iterator().next();
					contentLanguage.setImage(contentImage);
					contentImage.setContentLanguage(null);
					images.remove(contentImage);
				}
			}
		}
		
		JSONEscapeObject jsonResult = createJsonImages(site.getSiteId(), content, form);
		jsonResult.put("recUpdateBy", content.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(content.getRecUpdateDatetime()));
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
		ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		
		Content content = ContentDAO.load(site.getSiteId(), Format.getLong(form.getContentId()));
		
		ContentLanguage contentLanguage = null;
		for (ContentLanguage language : content.getContentLanguages()) {
			if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassId())) {
				contentLanguage = language;
				break;
			}
		}
		String defaultImageId = form.getCreateDefaultImageId();
		ContentImage contentImage = ContentImageDAO.load(site.getSiteId(), Format.getLong(defaultImageId));
		
		ContentImage currentImage = contentLanguage.getImage();
		currentImage.setContentLanguage(contentLanguage);
		contentLanguage.getImages().add(currentImage);
		
		contentImage.setContentLanguage(contentLanguage);
		contentImage.setContentLanguage(null);
		contentLanguage.setImage(contentImage);
		contentLanguage.getImages().remove(contentImage);
		
		contentLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
		contentLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(contentLanguage);
		
		JSONEscapeObject jsonResult = createJsonImages(site.getSiteId(), content, form);
		jsonResult.put("recUpdateBy", content.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(content.getRecUpdateDatetime()));
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
        return null;
	}
	
	public void createAdditionalInfo(AdminBean adminBean, Content content, ContentMaintActionForm form) throws Exception {
		Site site = adminBean.getSite();
        initSiteProfiles(form, site);
		String siteId = site.getSiteId();
		
	 	Iterator<?> iterator = content.getMenus().iterator();
	 	Vector<ContentMenuDisplayForm> selectedMenuVector = new Vector<ContentMenuDisplayForm>();
		while (iterator.hasNext()) {
			Menu menu = (Menu) iterator.next();
			ContentMenuDisplayForm menuDisplayForm = new ContentMenuDisplayForm();
			menuDisplayForm.setMenuId(Format.getLong(menu.getMenuId()));
			menuDisplayForm.setMenuLongDesc(Utility.formatMenuName(siteId, menu.getMenuId(), form.getSiteProfileClassId()));
			menuDisplayForm.setSiteDomainName(menu.getSiteDomain().getSiteDomainLanguage().getSiteName());
			selectedMenuVector.add(menuDisplayForm);
		}
		ContentMenuDisplayForm selectedMenuList[] = new ContentMenuDisplayForm[selectedMenuVector.size()];
		selectedMenuVector.copyInto(selectedMenuList);
		form.setSelectedMenus(selectedMenuList);
		form.setSelectedMenusCount(selectedMenuList.length);
//        form.setMenuList(Utility.makeMenuTreeList(siteId, form.getSiteProfileClassId()));
		
		Vector<LabelValueBean> siteDomainList = new Vector<LabelValueBean>();
		for (SiteDomain siteDomain : site.getSiteDomains()) {
			siteDomainList.add(new LabelValueBean(siteDomain.getSiteDomainLanguage().getSiteName(), siteDomain.getSiteDomainId().toString()));
		}
		LabelValueBean siteDomains[] = new LabelValueBean[siteDomainList.size()];
		siteDomainList.copyInto(siteDomains);
		form.setSiteDomains(siteDomains);
	}
	
    /*******************************************************************************/
    
	public JSONEscapeObject createJsonContentsRelated(Content content, ContentMaintActionForm form) throws Exception {
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		Iterator<?> iterator = content.getContentsRelated().iterator();
		Vector<JSONEscapeObject> contents = new Vector<JSONEscapeObject>();
		while (iterator.hasNext()) {
			Content contentRelated = (Content) iterator.next();
			ContentLanguage contentLanguage = null;
			for (ContentLanguage language : contentRelated.getContentLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassDefaultId())) {
					contentLanguage = language;
					break;
				}
			}
			JSONEscapeObject contentObject = new JSONEscapeObject();
			contentObject.put("contentId", contentRelated.getContentId());
			contentObject.put("contentTitle", contentLanguage.getContentTitle());
			contents.add(contentObject);
		}
		jsonResult.put("contents", contents);
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);

		return jsonResult;
	}
	
	public ActionForward getContentsRelated(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
	
		Content content = ContentDAO.load(site.getSiteId(), Format.getLong(form.getContentId()));
		streamWebService(response, createJsonContentsRelated(content, form).toHtmlString());
		return null;
	}
	
	public ActionForward addContentRelated(ActionMapping mapping, 
									  ActionForm actionForm,
									  HttpServletRequest request,
									  HttpServletResponse response) 
								throws Throwable {
		ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		
		Content content = ContentDAO.load(site.getSiteId(), Format.getLong(form.getContentId()));
		Content contentRelated = ContentDAO.load(site.getSiteId(), Format.getLong(form.getContentRelatedId()));
		
	  	JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
	   	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Iterator<?> iterator = content.getContentsRelated().iterator();
		while (iterator.hasNext()) {
			Content c = (Content) iterator.next();
			if (c.getContentId().equals(contentRelated.getContentId())) {
				MessageResources resources = this.getResources(request);
				jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
				jsonResult.put("message", resources.getMessage("error.content.exist"));
				streamWebService(response, jsonResult.toHtmlString());
				return null;
			}
		}
		content.getContentsRelated().add(contentRelated);
		content.setRecUpdateBy(adminBean.getUser().getUserId());
		content.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(content);

		jsonResult.put("recUpdateBy", content.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(content.getRecUpdateDatetime()));
		streamWebService(response, jsonResult.toHtmlString());
		return null;
	}	
	
	public ActionForward removeContentsRelated(ActionMapping mapping, 
										   ActionForm actionForm,
										   HttpServletRequest request,
										   HttpServletResponse response) 
										throws Throwable {
		
		ContentMaintActionForm form = (ContentMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
	
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
	   	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Content content = ContentDAO.load(site.getSiteId(), Format.getLong(form.getContentId()));
	   	String contentRelatedIds[] = form.getContentRelatedIds();
	   	if (contentRelatedIds != null) {
		   	for (int i = 0; i < contentRelatedIds.length; i++) {
				Content contentRelated = ContentDAO.load(site.getSiteId(), Format.getLong(contentRelatedIds[i]));
				content.getContentsRelated().remove(contentRelated);
		   	}
		   	content.setRecUpdateBy(adminBean.getUser().getUserId());
		   	content.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			em.persist(content);
	   	}

		jsonResult.put("recUpdateBy", content.getRecUpdateBy());
		jsonResult.put("recUpdateDatetime", Format.getFullDatetime(content.getRecUpdateDatetime()));
		streamWebService(response, jsonResult.toHtmlString());

		return null;
	}
	
	private void copyProperties(ContentMaintActionForm form, Content content) throws Exception {
    	ContentLanguage defaultContentLanguage = null;
    	for (ContentLanguage language : content.getContentLanguages()) {
    		if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassDefaultId())) {
    			defaultContentLanguage = language;
    		}
    	}
    	
		form.setContentId(Format.getLong(content.getContentId()));
		form.setContentTitle(defaultContentLanguage.getContentTitle());
		form.setContentShortDesc(defaultContentLanguage.getContentShortDesc());
		form.setContentDesc(defaultContentLanguage.getContentDesc());
		form.setPageTitle(defaultContentLanguage.getPageTitle());
		form.setMetaKeywords(defaultContentLanguage.getMetaKeywords());
		form.setMetaDescription(defaultContentLanguage.getMetaDescription());
		form.setContentHitCounter(Format.getIntObj(content.getContentHitCounter()));
		form.setContentRating(Format.getFloatObj(content.getContentRating()));
		form.setContentRatingCount(Format.getIntObj(content.getContentRatingCount()));
		form.setPublished(content.getPublished() == 'Y' ? true : false);
		form.setContentPublishOn(Format.getDate(content.getContentPublishOn()));
		form.setContentExpireOn(Format.getDate(content.getContentExpireOn()));
		form.setRemoveImages(null);
		form.setRemoveMenus(null);
		form.setMenuWindowMode("");
		
		ContentImage contentImage = defaultContentLanguage.getImage();
		if (contentImage != null) {
			LabelValueBean bean = new LabelValueBean();
			bean.setLabel(contentImage.getImageName());
			bean.setValue(Format.getLong(contentImage.getImageId()));
			form.setDefaultImageId(bean);
		}
		else {
			form.setDefaultImageId(null);
		}
		
		form.setRecUpdateBy(content.getRecUpdateBy());
		form.setRecUpdateDatetime(Format.getFullDatetime(content.getRecUpdateDatetime()));
		form.setRecCreateBy(content.getRecCreateBy());
		form.setRecCreateDatetime(Format.getFullDatetime(content.getRecCreateDatetime()));
		
		if (!form.isSiteProfileClassDefault()) {
			form.setContentImageOverride(false);
			form.setContentTitleLangFlag(false);
			form.setContentDescLangFlag(false);
			form.setContentShortDescLangFlag(false);
			form.setPageTitleLangFlag(false);
			form.setMetaKeywordsLangFlag(false);
			form.setMetaDescriptionLangFlag(false);
			form.setContentTitleLang(defaultContentLanguage.getContentTitle());
			form.setContentDescLang(defaultContentLanguage.getContentDesc());
			form.setContentShortDescLang(defaultContentLanguage.getContentShortDesc());
			form.setPageTitleLang(defaultContentLanguage.getPageTitle());
			form.setMetaKeywordsLang(defaultContentLanguage.getMetaKeywords());
			form.setMetaDescriptionLang(defaultContentLanguage.getMetaDescription());
	    	Iterator<?> iterator = content.getContentLanguages().iterator();
	    	boolean found = false;
	    	ContentLanguage contentLanguage = null;
	    	Long siteProfileClassId = form.getSiteProfileClassId();
	    	while (iterator.hasNext()) {
	    		contentLanguage = (ContentLanguage) iterator.next();
	    		if (contentLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
	    			found = true;
	    			break;
	    		}
	    	}
	    	if (found) {
	    		if (contentLanguage.getContentTitle() != null) {
		    		form.setContentTitleLangFlag(true);
		    		form.setContentTitleLang(contentLanguage.getContentTitle());
	    		}
	    		if (contentLanguage.getContentDesc() != null) {
		    		form.setContentDescLangFlag(true);
		    		form.setContentDescLang(contentLanguage.getContentDesc());
	    		}
	    		if (contentLanguage.getContentShortDesc() != null) {
		    		form.setContentShortDescLangFlag(true);
		    		form.setContentShortDescLang(contentLanguage.getContentShortDesc());
	    		}
	    		if (contentLanguage.getPageTitle() != null) {
	    			form.setPageTitleLangFlag(true);
	    			form.setPageTitleLang(contentLanguage.getPageTitle());
	    		}
	    		if (contentLanguage.getMetaKeywords() != null) {
	    			form.setMetaKeywordsLangFlag(true);
	    			form.setMetaKeywordsLang(contentLanguage.getPageTitle());
	    		}
	    		if (contentLanguage.getMetaDescription() != null) {
	    			form.setMetaDescriptionLangFlag(true);
	    			form.setMetaDescriptionLang(contentLanguage.getMetaDescription());
	    		}
	    		if (contentLanguage.getContentImageOverride().equalsIgnoreCase(String.valueOf(Constants.VALUE_YES))) {
	    			form.setContentImageOverride(true);
	    		}
	    	}
		}
	}

    public ActionMessages validate(ContentMaintActionForm form, String siteId, boolean insertMode) throws Exception {
       	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	ActionMessages errors = new ActionMessages();
    	
    	if (Format.isNullOrEmpty(form.getContentTitle())) {
    		errors.add("contentTitle", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getContentPublishOn())) {
    		errors.add("contentPublishOn", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getContentExpireOn())) {
    		errors.add("contentExpireOn", new ActionMessage("error.string.required"));
    	}
    	
    	if (!Format.isDate(form.getContentPublishOn())) {
       		errors.add("contentPublishOn", new ActionMessage("error.date.invalid"));
    	}
    	if (!Format.isDate(form.getContentExpireOn())) {
       		errors.add("contentExpireOn", new ActionMessage("error.date.invalid"));
    	}
    	
    	String contentNaturalKey = Utility.encode(form.getContentTitle());
    	String sql = "select  content " +
    				 "from    Content content " +
    				 "left    join content.site site " +
    				 "where   site.siteId = :siteId " + 
    				 "and     content.contentNaturalKey = :contentNaturalKey ";
    	if (!insertMode) {
    		sql += "and     content.contentId != :contentId";
    	}
    	Query query = em.createQuery(sql);
    	query.setParameter("siteId", siteId);
    	query.setParameter("contentNaturalKey", contentNaturalKey);
    	if (!insertMode) {
    		query.setParameter("contentId", Format.getLong(form.getContentId()));
    	}
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		errors.add("contentTitle", new ActionMessage("error.content.nkey.duplicate"));
    	}
    	return errors;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("save", "save");
        map.put("cancel", "cancel");
        map.put("edit", "edit");
        map.put("create", "create");
        map.put("resetCounter", "resetCounter");
        map.put("removeCategories", "removeCategories");
        map.put("addCategories", "addCategories");
        map.put("showCategories", "showCategories");
        map.put("removeMenus", "removeMenus");
        map.put("addMenus", "addMenus");
        map.put("showImages", "showImages");
        map.put("uploadImage", "uploadImage");
        map.put("removeImages", "removeImages");
        map.put("defaultImage", "defaultImage");
        map.put("overrideImages", "overrideImages");
        map.put("remove", "remove");
        map.put("comments", "comments");
        map.put("language", "language");
        map.put("translate", "translate");
        map.put("getContentsRelated", "getContentsRelated");
        map.put("removeContentsRelated", "removeContentsRelated");
        map.put("addContentRelated", "addContentRelated");
        return map;
    }
}
