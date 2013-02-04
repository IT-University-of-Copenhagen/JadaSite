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

package com.jada.admin.homePage;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.ContentDAO;
import com.jada.dao.HomePageDAO;
import com.jada.dao.ItemDAO;
import com.jada.dao.SiteDomainDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Content;
import com.jada.jpa.entity.ContentLanguage;
import com.jada.jpa.entity.HomePage;
import com.jada.jpa.entity.HomePageDetail;
import com.jada.jpa.entity.HomePageLanguage;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.ItemLanguage;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.jpa.entity.User;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.translator.BingTranslate;

import fr.improve.struts.taglib.layout.util.FormUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.EntityManager;

import com.jada.util.JSONEscapeObject;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class HomePageMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        HomePageMaintActionForm form = (HomePageMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		
		SiteDomain siteDomain = SiteDomainDAO.load(Format.getLong(form.getSiteDomainId()));
		HomePage homePage = siteDomain.getHomePage();
		if (homePage == null) {
			homePage = new HomePage();
			homePage.setRecUpdateBy(adminBean.getUser().getUserId());
	    	homePage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			homePage.setRecCreateBy(adminBean.getUser().getUserId());
	    	homePage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
	    	siteDomain.setHomePage(homePage);
	    	em.persist(homePage);
	    	
	    	HomePageLanguage homePageLanguage = new HomePageLanguage();
	    	homePageLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
	    	homePageLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
	    	homePageLanguage.setRecCreateBy(adminBean.getUser().getUserId());
	    	homePageLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
	    	homePage.setHomePageLanguage(homePageLanguage);
	    	homePage.getHomePageLanguages().add(homePageLanguage);
	    	em.persist(homePageLanguage);
		}
		form.setHomePageId(Format.getLong(homePage.getHomePageId()));
		form.setPageTitle(homePage.getHomePageLanguage().getHomePageTitle());
		form.setMetaKeywords(homePage.getHomePageLanguage().getMetaKeywords());
		form.setMetaDescription(homePage.getHomePageLanguage().getMetaDescription());
        initListInfo(form, homePage);

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward translate(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
	
		HomePageMaintActionForm form = (HomePageMaintActionForm) actionForm;
		
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
		HomePage homePage = HomePageDAO.load(Format.getLong(form.getHomePageId()));
		
        BingTranslate translator = new BingTranslate(form.getFromLocale(), form.getToLocale(), site);
        
        form.setPageTitleLangFlag(true);
        form.setPageTitleLang(translator.translate(homePage.getHomePageLanguage().getHomePageTitle()));
        form.setMetaKeywordsLangFlag(true);
        form.setMetaKeywordsLang(translator.translate(homePage.getHomePageLanguage().getMetaKeywords()));
        form.setMetaDescriptionLangFlag(true);
        form.setMetaDescriptionLang(translator.translate(homePage.getHomePageLanguage().getMetaDescription()));
          
        initListInfo(form, homePage);

		FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}

    public ActionForward language(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
	
		HomePageMaintActionForm form = (HomePageMaintActionForm) actionForm;
		
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
		
		HomePage homePage = HomePageDAO.load(Format.getLong(form.getHomePageId()));
		form.setPageTitle(homePage.getHomePageLanguage().getHomePageTitle());
		form.setMetaKeywords(homePage.getHomePageLanguage().getMetaKeywords());
		form.setMetaDescription(homePage.getHomePageLanguage().getMetaDescription());
		
		if (!form.isSiteProfileClassDefault()) {
			form.setPageTitleLang(homePage.getHomePageLanguage().getHomePageTitle());
	    	boolean found = false;
	    	HomePageLanguage homePageLanguage = null;
	    	Long siteProfileClassId = form.getSiteProfileClassId();
	    	Iterator<?> iterator = homePage.getHomePageLanguages().iterator();
	    	while (iterator.hasNext()) {
	    		homePageLanguage = (HomePageLanguage) iterator.next();
	    		if (homePageLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
	    			found = true;
	    			break;
	    		}
	    	}
	    	if (found) {
	    		if (homePageLanguage.getHomePageTitle() != null) {
		    		form.setPageTitleLangFlag(true);
		    		form.setPageTitleLang(homePageLanguage.getHomePageTitle());
	    		}
	    		if (homePageLanguage.getMetaKeywords() != null) {
		    		form.setMetaKeywordsLangFlag(true);
		    		form.setMetaKeywordsLang(homePageLanguage.getMetaKeywords());
	    		}
	    		if (homePageLanguage.getMetaDescription() != null) {
		    		form.setMetaDescriptionLangFlag(true);
		    		form.setMetaDescriptionLang(homePageLanguage.getMetaDescription());
	    		}
	    	}
		}
        initListInfo(form, homePage);		
		FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}

	public ActionForward save(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
        HomePageMaintActionForm form = (HomePageMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		
		ActionMessages errors = validate(request, form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return mapping.findForward("error");
		}
		
		HomePage homePage = HomePageDAO.load(Format.getLong(form.getHomePageId()));
		if (form.isSiteProfileClassDefault()) {
			saveDefault(form, adminBean, site);
		}
		else {
			saveLanguage(form, adminBean, site);
		}
        initListInfo(form, homePage);
		return mapping.findForward("success");
	}
	
	public void saveDefault(HomePageMaintActionForm form, AdminBean adminBean, Site site) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		HomePage homePage = HomePageDAO.load(Format.getLong(form.getHomePageId()));
		homePage.getHomePageLanguage().setHomePageTitle(form.getPageTitle());
		homePage.getHomePageLanguage().setMetaKeywords(form.getMetaKeywords());
		homePage.getHomePageLanguage().setMetaDescription(form.getMetaDescription());
		homePage.setRecUpdateBy(adminBean.getUser().getUserId());
    	homePage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
   	
    	for (HomePageDetailDisplayForm homePageDetailForm : form.getHomePageDetails()) {
    		HomePageDetail homePageDetail = (HomePageDetail) em.find(HomePageDetail.class, Format.getLong(homePageDetailForm.getHomePageDetailId()));
    		homePageDetail.setSeqNum(Format.getInt(homePageDetailForm.getSeqNum()));
    		homePageDetail.setRecUpdateBy(adminBean.getUser().getUserId());
    		homePageDetail.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    		em.persist(homePageDetail);
    	}
    	
    	if (!Format.isNullOrEmpty(form.getFeatureHomePageDetailId())) {
    		HomePageDetail featureHomePageDetail = (HomePageDetail) em.find(HomePageDetail.class, Format.getLong(form.getFeatureHomePageDetailId()));
    		homePage.setFeatureData(featureHomePageDetail);
    	}
	}
	
	public void saveLanguage(HomePageMaintActionForm form, AdminBean adminBean, Site site) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		HomePage homePage = HomePageDAO.load(Format.getLong(form.getHomePageId()));

    	Long siteProfileClassId = form.getSiteProfileClassId();
    	User user = adminBean.getUser();
    	Iterator<?> iterator = homePage.getHomePageLanguages().iterator();
    	boolean found = false;
    	HomePageLanguage homePageLanguage = null;
    	while (iterator.hasNext()) {
    		homePageLanguage = (HomePageLanguage) iterator.next();
    		if (homePageLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
    			found = true;
    			break;
    		}
    	}
    	if (!found) {
    		homePageLanguage = new HomePageLanguage();
    		homePageLanguage.setRecCreateBy(user.getUserId());
    		homePageLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		SiteProfileClass siteProfileClass = (SiteProfileClass) em.find(SiteProfileClass.class, siteProfileClassId);
    		homePageLanguage.setSiteProfileClass(siteProfileClass);
    		homePageLanguage.setHomePage(homePage);
    	}
    	if (form.isPageTitleLangFlag()) {
    		homePageLanguage.setHomePageTitle(form.getPageTitleLang());
    	}
    	else {
    		homePageLanguage.setHomePageTitle(null);
    	}
    	if (form.isMetaKeywordsLangFlag()) {
    		homePageLanguage.setMetaKeywords(form.getMetaKeywords());
    	}
    	else {
    		homePageLanguage.setMetaKeywords(null);
    	}
    	if (form.isMetaDescriptionLangFlag()) {
    		homePageLanguage.setMetaDescription(form.getMetaDescriptionLang());
    	}
    	else {
    		homePageLanguage.setMetaDescription(null);
    	}
		homePageLanguage.setRecUpdateBy(user.getUserId());
		homePageLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		if (!found) {
			em.persist(homePageLanguage);
		}
	}
	
	public ActionForward getHomePageDetails(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
		HomePageMaintActionForm form = (HomePageMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		
		HomePage homePage = HomePageDAO.load(Format.getLong(form.getHomePageId()));
		HomePageDetail feature = homePage.getFeatureData();
		JSONEscapeObject JSONEscapeObject = new JSONEscapeObject();
		JSONEscapeObject.put("homePageId", homePage.getHomePageId());
		Vector<JSONEscapeObject> vector = new Vector<JSONEscapeObject>();
		for (HomePageDetail homePageDetail : homePage.getHomePageDetails()) {
			JSONEscapeObject jsonDetail = new JSONEscapeObject();
			jsonDetail.put("homePageDetailId", homePageDetail.getHomePageDetailId());
			jsonDetail.put("seqNum", homePageDetail.getSeqNum());
			Item item = homePageDetail.getItem();
			Content content = homePageDetail.getContent();
			if (item != null) {
				ItemLanguage itemLanguage = null;
				for (ItemLanguage language : item.getItemLanguages()) {
					if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassDefaultId())) {
						itemLanguage = language;
					}
				}
				jsonDetail.put("type", "Item");
				jsonDetail.put("homePageDesc", itemLanguage.getItemShortDesc() + " - " + item.getItemSkuCd());
			}
			if (content != null) {
				ContentLanguage contentLanguage = null;
				for (ContentLanguage language : content.getContentLanguages()) {
					if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassDefaultId())) {
						contentLanguage = language;
					}
				}
				jsonDetail.put("type", "Content");
				jsonDetail.put("homePageDesc", contentLanguage.getContentTitle());
			}
			if (feature != null) {
				if (feature.getHomePageDetailId().equals(homePageDetail.getHomePageDetailId())) {
					jsonDetail.put("feature", true);
				}
			}
			else {
				jsonDetail.put("feature", false);
			}
			vector.add(jsonDetail);
		}
		JSONEscapeObject.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		JSONEscapeObject.put("homePageDetails", vector);
		streamWebService(response, JSONEscapeObject.toHtmlString());
      return null;
	}
	
	public ActionForward addItem(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		HomePageMaintActionForm form = (HomePageMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		HomePage homePage = HomePageDAO.load(Format.getLong(form.getHomePageId()));
		
		Item item = ItemDAO.load(site.getSiteId(), Format.getLong(form.getItemId()));
		int seqNum = -1;
		boolean exist = false;
		boolean hasFeature = false;
		for (HomePageDetail homePageDetail : homePage.getHomePageDetails()) {
			if (homePageDetail.getSeqNum() > seqNum) {
				seqNum = homePageDetail.getSeqNum();
			}
			if (homePageDetail.getFeatureData() == Constants.VALUE_YES) {
				hasFeature = true;
			}
			Item currentItem = homePageDetail.getItem();
			if (currentItem == null) {
				continue;
			}
			if (currentItem.getItemId().equals(item.getItemId())) {
				exist = true;
				break;
			}
		}
		if (!exist) {
			HomePageDetail homePageDetail = new HomePageDetail();
			homePageDetail.setRecUpdateBy(adminBean.getUser().getUserId());
			homePageDetail.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			homePageDetail.setRecCreateBy(adminBean.getUser().getUserId());
			homePageDetail.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			homePageDetail.setItem(item);
			homePageDetail.setSeqNum(seqNum + 1);
			homePageDetail.setFeatureData(hasFeature ? Constants.ACTIVE_NO : Constants.VALUE_YES);
			homePage.getHomePageDetails().add(homePageDetail);
			em.persist(homePageDetail);
		}
		
		JSONEscapeObject JSONEscapeObject = new JSONEscapeObject();
		JSONEscapeObject.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		form.setStream(true);
		form.setStreamData(JSONEscapeObject.toHtmlString());
		return null;
	}

	public ActionForward addContent(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		HomePageMaintActionForm form = (HomePageMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		HomePage homePage = HomePageDAO.load(Format.getLong(form.getHomePageId()));
		
		Content content = ContentDAO.load(site.getSiteId(), Format.getLong(form.getContentId()));
		int seqNum = -1;
		boolean exist = false;
		boolean hasFeature = false;
		for (HomePageDetail homePageDetail : homePage.getHomePageDetails()) {
			if (homePageDetail.getSeqNum() > seqNum) {
				seqNum = homePageDetail.getSeqNum();
			}
			if (homePageDetail.getFeatureData() == Constants.VALUE_YES) {
				hasFeature = true;
			}
			Content currentContent = homePageDetail.getContent();
			if (currentContent == null) {
				continue;
			}
			if (currentContent.getContentId().equals(content.getContentId())) {
				exist = true;
				break;
			}
		}
		if (!exist) {
			HomePageDetail homePageDetail = new HomePageDetail();
			homePageDetail.setRecUpdateBy(adminBean.getUser().getUserId());
			homePageDetail.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			homePageDetail.setRecCreateBy(adminBean.getUser().getUserId());
			homePageDetail.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			homePageDetail.setContent(content);
			homePageDetail.setSeqNum(seqNum + 1);
			homePageDetail.setFeatureData(hasFeature ? Constants.ACTIVE_NO : Constants.VALUE_YES);
			homePage.getHomePageDetails().add(homePageDetail);
			em.persist(homePageDetail);
		}
		
		JSONEscapeObject JSONEscapeObject = new JSONEscapeObject();
		JSONEscapeObject.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		form.setStream(true);
		form.setStreamData(JSONEscapeObject.toHtmlString());
		return null;
	}
	
	public ActionForward removeDetails(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		HomePageMaintActionForm form = (HomePageMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		HomePage homePage = HomePageDAO.load(Format.getLong(form.getHomePageId()));
		
		if (form.getHomePageDetailIds() != null) {
			for (String homePageDetailId : form.getHomePageDetailIds()) {
				HomePageDetail homePageDetail = (HomePageDetail) em.find(HomePageDetail.class, Format.getLong(homePageDetailId));
				HomePageDetail featureData = homePage.getFeatureData();
				if (featureData != null) {
					if (featureData.getHomePageDetailId().equals(homePageDetail.getHomePageDetailId())) {
						homePage.setFeatureData(null);
						em.persist(homePage);
					}
				}
				homePage.getHomePageDetails().remove(homePageDetail);
				em.remove(homePageDetail);
			}
		}
		JSONEscapeObject JSONEscapeObject = new JSONEscapeObject();
		JSONEscapeObject.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		form.setStream(true);
		form.setStreamData(JSONEscapeObject.toHtmlString());
		return null;
	}
	
	
    public ActionForward resequence(ActionMapping mapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	HomePageMaintActionForm form = (HomePageMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		MessageResources resources = this.getResources(request);
		
		HomePage homePage = HomePageDAO.load(Format.getLong(form.getHomePageId()));
		HomePageDetailDisplayForm displayForms[] = form.getHomePageDetails();
		boolean isError = false;
		for (HomePageDetailDisplayForm displayForm : displayForms) {
			if (!Format.isInt(displayForm.getSeqNum())) {
				displayForm.setSeqNumError(resources.getMessage("error.int.invalid"));
				isError = true;
			}
		}
		if (isError) {
			return mapping.findForward("error");
		}
		
		for (HomePageDetailDisplayForm displayForm : displayForms) {
			HomePageDetail homePageDetail = (HomePageDetail) em.find(HomePageDetail.class, Format.getLong(displayForm.getHomePageDetailId()));
			homePageDetail.setSeqNum(Format.getInt(displayForm.getSeqNum()));
			em.persist(homePageDetail);
		}
        initListInfo(form, homePage);
        
        ActionForward actionForward = mapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	HomePageMaintActionForm form = (HomePageMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
		initSiteProfiles(form, site);
    
		HomePage homePage = HomePageDAO.load(Format.getLong(form.getHomePageId()));
		for (HomePageDetailDisplayForm displayForm : form.getHomePageDetails()) {
			if (displayForm.isRemove()) {
	            HomePageDetail homePageDetail = (HomePageDetail) em.find(HomePageDetail.class, Format.getLong(displayForm.getHomePageDetailId()));
	            em.remove(homePageDetail);
			}
		}
        initListInfo(form, homePage);
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    private void initListInfo(HomePageMaintActionForm form, HomePage homePage) throws Exception {
    	HomePageDetail featureData = homePage.getFeatureData();
    	if (featureData != null) {
    		form.setFeatureHomePageDetailId(featureData.getHomePageDetailId().toString());
    	}
    	Vector<HomePageDetailDisplayForm> vector = new Vector<HomePageDetailDisplayForm>();
    	for (HomePageDetail homePageDetail : homePage.getHomePageDetails()) {
    		HomePageDetailDisplayForm displayForm = new HomePageDetailDisplayForm();
    		displayForm.setHomePageDetailId(homePageDetail.getHomePageDetailId().toString());
    		displayForm.setSeqNum(Format.getInt(homePageDetail.getSeqNum()));
    		displayForm.setFeatureData("N");
    		if (homePageDetail.getContent() != null) {
        		Content content = homePageDetail.getContent();
				ContentLanguage contentLanguage = null;
				for (ContentLanguage language : content.getContentLanguages()) {
					if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassDefaultId())) {
						contentLanguage = language;
					}
				}
        		displayForm.setDataType("Content");
        		displayForm.setDescription(contentLanguage.getContentTitle());
            	displayForm.setPublished(String.valueOf(content.getPublished()));
            	displayForm.setDataPublishOn(Format.getFullDate(content.getContentPublishOn()));
            	displayForm.setDataExpireOn(Format.getFullDate(content.getContentExpireOn()));
    		}
    		if (homePageDetail.getItem() != null) {
        		Item item = homePageDetail.getItem();
				ItemLanguage itemLanguage = null;
				for (ItemLanguage language : item.getItemLanguages()) {
					if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassDefaultId())) {
						itemLanguage = language;
					}
				}
        		displayForm.setDataType("Item");
        		displayForm.setDescription(itemLanguage.getItemShortDesc());
            	displayForm.setPublished(String.valueOf(item.getPublished()));
            	displayForm.setDataPublishOn(Format.getFullDate(item.getItemPublishOn()));
            	displayForm.setDataExpireOn(Format.getFullDate(item.getItemExpireOn()));
    		}
    		vector.add(displayForm);
    	}
    	HomePageDetailDisplayForm homePageDetails[] = new HomePageDetailDisplayForm[vector.size()];
    	vector.copyInto(homePageDetails);
    	form.setHomePageDetails(homePageDetails);
    }
    
    public ActionMessages validate(HttpServletRequest request, HomePageMaintActionForm form) throws Exception { 
		MessageResources resources = this.getResources(request);
    	ActionMessages errors = new ActionMessages();
    	
    	if (!form.isSiteProfileClassDefault()) {
    		return errors;
    	}
    	
    	if (form.getHomePageDetails() == null) {
    		return errors;
    	}
    	
    	for (HomePageDetailDisplayForm homePageDetailForm : form.getHomePageDetails()) {
    		if (Format.isNullOrEmpty(homePageDetailForm.getSeqNum())) {
    			homePageDetailForm.setSeqNumError(resources.getMessage("error.string.required"));
    			errors.add("dummy", new ActionMessage("error.string.required"));
    		} else if (!Format.isInt(homePageDetailForm.getSeqNum())) {
    			homePageDetailForm.setSeqNumError(resources.getMessage("error.int.invalid"));
    			errors.add("dummy", new ActionMessage("error.int.invalid"));
    		}
    	}
    	return errors;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("edit", "edit");
        map.put("save", "save");
        map.put("resequence", "resequence");
        map.put("makeFeature", "makeFeature");
        map.put("language", "language");
        map.put("translate", "translate");
        map.put("remove", "remove");
        map.put("addItem", "addItem");
        map.put("addContent", "addContent");
        map.put("getHomePageDetails", "getHomePageDetails");
        map.put("removeDetails", "removeDetails");
       return map;
    }
}