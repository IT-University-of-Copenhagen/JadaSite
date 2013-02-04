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

package com.jada.content.template;

import java.io.File;
import java.io.StringWriter;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;

import com.jada.api.DataApi;
import com.jada.content.ContentBean;
import com.jada.content.ContentFilterBean;
import com.jada.content.ContentLookupDispatchAction;
import com.jada.content.ContentSessionBean;
import com.jada.content.data.CategoryInfo;
import com.jada.content.data.CommentInfo;
import com.jada.content.data.ContactUsInfo;
import com.jada.content.data.ContactUsInfoList;
import com.jada.content.data.ContentApi;
import com.jada.content.data.ContentInfo;
import com.jada.content.data.DataInfo;
import com.jada.content.data.EmptyTemplateInfo;
import com.jada.content.data.ItemComparePageInfo;
import com.jada.content.data.ItemInfo;
import com.jada.content.data.MenuComponentInfo;
import com.jada.content.data.MenuInfo;
import com.jada.content.data.OrderInfo;
import com.jada.content.data.PageHeaderInfo;
import com.jada.content.data.SearchInfo;
import com.jada.content.data.ShoppingCartSummaryInfo;
import com.jada.content.data.SiteInfo;
import com.jada.content.data.SyndicationInfo;
import com.jada.content.frontend.ContentUtility;
import com.jada.dao.ContentDAO;
import com.jada.dao.CustomAttributeDAO;
import com.jada.dao.ItemDAO;
import com.jada.dao.PollHeaderDAO;
import com.jada.dao.SiteDomainDAO;
import com.jada.dao.SyndicationDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Category;
import com.jada.jpa.entity.CategoryLanguage;
import com.jada.jpa.entity.Comment;
import com.jada.jpa.entity.Content;
import com.jada.jpa.entity.CustomAttribute;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.HomePage;
import com.jada.jpa.entity.HomePageLanguage;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.SiteCurrency;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.SiteDomainLanguage;
import com.jada.jpa.entity.SiteProfile;
import com.jada.system.ApplicationGlobal;
import com.jada.system.Languages;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;
import com.jada.xml.site.SiteDomainParamBean;

public class TemplateEngine {
	protected VelocityEngine engine = null;
	protected HttpServletRequest request = null;
	protected ServletContext servletContext = null;
	protected boolean emptyTemplate = false;
	protected SiteDomain siteDomain = null;
	protected SiteDomainLanguage siteDomainLanguage = null;
	protected SiteDomainParamBean siteDomainParamBean = null;
	protected SiteProfile siteProfile = null;
	protected SiteCurrency siteCurrency = null;
	protected String siteName = null;
	protected int pageSize = Constants.DEFAULT_LISTING_PAGE_SIZE;
	protected ContentApi api = null;
	protected SiteInfo siteInfo = null;
	protected Hashtable<String, Object> parameters = new Hashtable<String, Object>();
	protected DataApi dataApi = null;
	protected ActionForm strutForm = null;
	protected String customPage = null;
	
	static String emptyPlaceHolder = "<-!-!-!EMPTYPLACEHOLDER!-!-!->";
    static String REPOST_URLS[][] = {{"/content/checkout/shoppingCart.do.*", "/content/checkout/shoppingCart.do?process=start&prefix=${siteDomainPrefix}"},
		 							 {"/myaccount/login/myAccountLogin.do.*", "/myaccount/login/myAccountLogin.do?process=start&prefix=${siteDomainPrefix}"},
		 							 {"/web/fe/.*/contentCommentUpdate/.*", null},
		 							 {"/web/fe/.*", ""}
		 							 
									};
    
    static public TemplateEngine getInstance() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    	String classname = ApplicationGlobal.getTemmplateEngineClassName();
    	Class<?> c = Class.forName(classname);
    	return (TemplateEngine) c.newInstance();
    }

	public void init(SiteDomain siteDomain, SiteProfile siteProfile, SiteCurrency siteCurrency, ServletContext servletContext) throws Exception {
		emptyTemplate = false;
		this.siteDomain = siteDomain;
		this.servletContext = servletContext;
		initBackground(siteProfile, siteCurrency);
	}
	
	public void init(HttpServletRequest request, ServletContext servletContext) throws Exception {
		init(request, servletContext, false);
	}

	public void init(HttpServletRequest request, ServletContext servletContext, boolean emptyTemplate) throws Exception {
		this.request = request;
		this.servletContext = servletContext;
		this.emptyTemplate = emptyTemplate;
		init();
	}
	
	public void initBackground(SiteProfile siteProfile, SiteCurrency siteCurrency) throws Exception {
		siteDomainLanguage = siteDomain.getSiteDomainLanguage();
		siteDomainParamBean = SiteDomainDAO.getSiteDomainParamBean(siteDomain.getSiteDomainLanguage(), siteDomainLanguage);
		this.siteProfile = siteProfile;
		this.siteCurrency = siteCurrency;
		String value = siteDomainParamBean.getCategoryPageSize();
		if (!Format.isNullOrEmpty(value)) {
			pageSize = Format.getInt(value);
		}
		siteName = siteDomainLanguage.getSiteName();
		if (!siteDomainLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfile.getSiteProfileClass().getSiteProfileClassId())) {
			for (SiteDomainLanguage language : siteDomain.getSiteDomainLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfile.getSiteProfileClass().getSiteProfileClassId())) {
					siteDomainLanguage = language;
				}
			}
			if (siteDomainLanguage.getSiteName() != null) {
				siteName = siteDomainLanguage.getSiteName();
			}
		}
		engine = new VelocityEngine();
		engine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute" );

	    engine.setProperty("runtime.log.logsystem.log4j.logger", "loggername");

		initResourceLoader();
		dataApi = DataApi.getInstance();
		api = new ContentApi(siteDomain, siteProfile, siteCurrency);
		siteInfo = api.getSite();
	}
	
	public void init() throws Exception {
		ContentBean contentBean = ContentLookupDispatchAction.getContentBean(request);
		siteDomain = contentBean.getContentSessionBean().getSiteDomain();
		siteDomainLanguage = siteDomain.getSiteDomainLanguage();
		siteDomainParamBean = SiteDomainDAO.getSiteDomainParamBean(siteDomain.getSiteDomainLanguage(), siteDomainLanguage);
		this.siteProfile = contentBean.getContentSessionBean().getSiteProfile();
		this.siteCurrency = contentBean.getContentSessionBean().getSiteCurrency();
		String value = siteDomainParamBean.getCategoryPageSize();
		if (!Format.isNullOrEmpty(value)) {
			pageSize = Format.getInt(value);
		}
		siteName = siteDomainLanguage.getSiteName();
		if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
			for (SiteDomainLanguage language : siteDomain.getSiteDomainLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
					siteDomainLanguage = language;
				}
			}
			if (siteDomainLanguage.getSiteName() != null) {
				siteName = siteDomainLanguage.getSiteName();
			}
		}
		engine = new VelocityEngine();
		initResourceLoader();
		dataApi = DataApi.getInstance();
		api = new ContentApi(request);
		siteInfo = api.getSite();
	}
	
	public String getTemplateName() {
		String templateName = null;
		if (request != null) {
			templateName = request.getParameter("templateName");
		}
		if (templateName == null) {
			if (siteDomain.getTemplate() != null) {
				templateName = siteDomain.getTemplate().getTemplateName();
			}
		}
		return templateName;
	}
	
	public void initResourceLoader() throws Exception {
		String resourceLoader = "";
    	engine.setApplicationAttribute(ServletContext.class.getName(), servletContext);
    	String templateName = getTemplateName();
		if (templateName != null && templateName.trim().length() > 0  && !templateName.trim().equals(Constants.TEMPLATE_BASIC)) {
			String templateUrlPrefix = Utility.getTemplatePrefix(siteDomain.getSite(), templateName);
	    	resourceLoader += "file";
	    	engine.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
	    	engine.setProperty("file.resource.loader.path", templateUrlPrefix);
		}
		if (resourceLoader.length() > 0) {
			resourceLoader += ", ";
		}
		resourceLoader += "webapp";
    	engine.setProperty("webapp.resource.loader.class", "org.apache.velocity.tools.view.servlet.WebappLoader");
    	engine.setProperty("webapp.resource.loader.path", "/content/template/" + Constants.TEMPLATE_BASIC + "");

    	engine.setProperty("resource.loader", resourceLoader);
		engine.init();
	}
	
	public VelocityContext getDefaultContext() {
		VelocityContext context = new VelocityContext();
		context.put("template", this);
		context.put("siteInfo", siteInfo);
		if (strutForm != null) {
			context.put("form", strutForm);
		}
		if (!Format.isNullOrEmpty(customPage)) {
			context.put("customPage", customPage);
		}
		return context;
	}
	
	public Template getTemplate(String templateName) throws ResourceNotFoundException, ParseErrorException, Exception {
		return engine.getTemplate(templateName);
	}
	
	public String mergeData(String vmName) throws ResourceNotFoundException, ParseErrorException, Exception{
		Template template = engine.getTemplate(vmName);
		VelocityContext context = getDefaultContext();
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		return writer.toString();
	}
	
	public String mergeData(String vmName, String variableName, Object info) throws ResourceNotFoundException, ParseErrorException, Exception {
		Template template = engine.getTemplate(vmName);
		VelocityContext context = getDefaultContext();
		context.put("template", this);
		context.put(variableName, info);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		return writer.toString();
	}
	
	public EmptyTemplateInfo getEmptyTemplateInfo() throws ResourceNotFoundException, ParseErrorException, Exception {
		return getEmptyTemplateInfo("template.vm");
	}
	
	public EmptyTemplateInfo getEmptyPrintTemplateInfo() throws ResourceNotFoundException, ParseErrorException, Exception {
		return getEmptyTemplateInfo("print.vm");
	}
	
	public EmptyTemplateInfo getEmptySecureTemplateInfo() throws ResourceNotFoundException, ParseErrorException, Exception {
		return getEmptyTemplateInfo("templateSecure.vm");
	}
	
	public EmptyTemplateInfo getEmptyTemplateInfo(String vmfile) throws ResourceNotFoundException, ParseErrorException, Exception {
		String data = mergeData(vmfile);
		EmptyTemplateInfo emptyTemplateInfo = new EmptyTemplateInfo();
		emptyTemplateInfo.setTemplatePrefix("");
		emptyTemplateInfo.setTemplateSuffix("");
		int pos = data.indexOf(emptyPlaceHolder);
		if (pos > 0) {
			emptyTemplateInfo.setTemplatePrefix(data.substring(0, pos));
		}
		if (data.length() > pos + emptyPlaceHolder.length()) {
			emptyTemplateInfo.setTemplateSuffix(data.substring(pos + emptyPlaceHolder.length()));
		}
		return emptyTemplateInfo;
	}
	
	public String getRepostURL() throws Exception {
		String url = request.getRequestURI().substring(request.getContextPath().length());
		for (int i = 0; i < REPOST_URLS.length; i++) {
			if (url.matches(REPOST_URLS[i][0])) {
				if (REPOST_URLS[i][1] == null) {
					break;
				}
				if (REPOST_URLS[i][1].length() > 0) {
					return "/" + ApplicationGlobal.getContextPath() + parseRepostURL(REPOST_URLS[i][1]);
				}
				else {
					return "/" + ApplicationGlobal.getContextPath() + url;
				}
			}
		}
		ContentSessionBean sessionBean = ContentLookupDispatchAction.getContentBean(request).getContentSessionBean();
		String result = "/" + ApplicationGlobal.getContextPath() + 
						Constants.FRONTEND_URL_PREFIX + 
						"/" + sessionBean.getSiteDomain().getSiteDomainPrefix() + 
						"/" + sessionBean.getSiteProfile().getSiteProfileClass().getSiteProfileClassName() + 
						"/home";
		return result;
	}
	
	public String parseRepostURL(String url) throws Exception {
		ContentBean contentBean = ContentLookupDispatchAction.getContentBean(request);
		ContentSessionBean sessionBean = contentBean.getContentSessionBean();
		url = url.replace("${siteDomainPrefix}", sessionBean.getSiteDomain().getSiteDomainPrefix());
		return url;
	}
	
	public PageHeaderInfo getPageHeader() throws Exception {
		if (isPageHome()) {
			return getPageHomeHeaderInfo();
		}
		else if (isPageContactUs()) {
			return getPageContactUsInfo();
		}
		else if (isPageCategory()) {
			return getPageCategoryInfo();
		}
		else if (isPageItem()) {
			return getPageItemInfo();
		}
		else if (isPageItemComment()) {
			return getPageItemCommentInfo();
		}
		else if (isPageContent()) {
			return getPageContentInfo();
		}
		else if (isPageContentComment()) {
			return getPageContentCommentInfo();
		}
		else if (isPageItemCompare()) {
			return getPageItemCompareInfo();
		}
		else if (isPageSearch()) {
			return getPageSearchInfo();
		}
		
		PageHeaderInfo pageHeaderInfo = new PageHeaderInfo();
		pageHeaderInfo.setPageTitle(siteName);
		pageHeaderInfo.setMetaKeywords(siteName);
		pageHeaderInfo.setMetaDescription(siteName);
		return pageHeaderInfo;
	}
	
	public PageHeaderInfo getPageHomeHeaderInfo() {
		PageHeaderInfo pageHeaderInfo = new PageHeaderInfo();
		ContentBean contentBean = ContentLookupDispatchAction.getContentBean(request);
		HomePage homePage = siteDomain.getHomePage();
		pageHeaderInfo.setPageTitle(siteName);
		pageHeaderInfo.setMetaKeywords(siteName);
		pageHeaderInfo.setMetaDescription(siteName);
		if (!Format.isNullOrEmpty(homePage.getHomePageLanguage().getHomePageTitle())) {
			pageHeaderInfo.setPageTitle(homePage.getHomePageLanguage().getHomePageTitle());
		}
		if (!Format.isNullOrEmpty(homePage.getHomePageLanguage().getMetaKeywords())) {
			pageHeaderInfo.setMetaKeywords(homePage.getHomePageLanguage().getMetaKeywords());
		}
		if (!Format.isNullOrEmpty(homePage.getHomePageLanguage().getMetaDescription())) {
			pageHeaderInfo.setMetaDescription(homePage.getHomePageLanguage().getMetaDescription());
		}
		if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
			for (HomePageLanguage language : homePage.getHomePageLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
					if (language.getHomePageTitle() != null) {
						pageHeaderInfo.setPageTitle(language.getHomePageTitle());
					}
					if (language.getMetaKeywords() != null) {
						pageHeaderInfo.setMetaKeywords(language.getMetaKeywords());
					}
					if (language.getMetaDescription() != null) {
						pageHeaderInfo.setMetaDescription(language.getMetaDescription());
					}
					break;
				}
			}
		}
		return pageHeaderInfo;
	}
	
	public PageHeaderInfo getPageCategoryInfo() throws Exception {
		PageHeaderInfo pageHeaderInfo = new PageHeaderInfo();
		String catNaturalKey = getCategoryParameter(request, 3);
		Category category = dataApi.getCategory(siteDomain.getSite().getSiteId(), catNaturalKey);
        if (category == null) {
        	pageHeaderInfo.setPageTitle(siteName + " - " + getLanguageByValue("Page not found"));
        	pageHeaderInfo.setMetaKeywords("");
        	pageHeaderInfo.setMetaDescription("");
        	return pageHeaderInfo;
        }
        pageHeaderInfo.setPageTitle(category.getCategoryLanguage().getCatShortTitle());
        if (Format.isNullOrEmpty(category.getCategoryLanguage().getMetaKeywords())) {
        	pageHeaderInfo.setMetaKeywords(category.getCategoryLanguage().getCatShortTitle());
        }
        else {
        	pageHeaderInfo.setMetaKeywords(category.getCategoryLanguage().getMetaKeywords());
        }
        if (Format.isNullOrEmpty(category.getCategoryLanguage().getMetaDescription())) {
        	pageHeaderInfo.setMetaDescription(category.getCategoryLanguage().getCatShortTitle());
        }
        else {
        	pageHeaderInfo.setMetaDescription(category.getCategoryLanguage().getMetaDescription());
        }
        ContentBean contentBean = ContentLookupDispatchAction.getContentBean(request);
        if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
        	for (CategoryLanguage language : category.getCategoryLanguages()) {
        		if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
        			if (language.getCatShortTitle() != null) {
        				pageHeaderInfo.setPageTitle(language.getCatShortTitle());
        			}
        			if (!Format.isNullOrEmpty(language.getMetaKeywords())) {
        				pageHeaderInfo.setMetaKeywords(language.getMetaKeywords());
        			}
        			if (!Format.isNullOrEmpty(language.getMetaDescription())) {
        				pageHeaderInfo.setMetaDescription(language.getMetaDescription());
        			}
        			break;
        		}
        	}
        }
		return pageHeaderInfo;
	}
	
	public PageHeaderInfo getPageItemInfo() throws Exception {
		PageHeaderInfo pageHeaderInfo = new PageHeaderInfo();
		
		String itemNaturalKey = getItemKey();
		boolean updateStatistics = false;
		ItemInfo itemInfo = api.getItem(itemNaturalKey, updateStatistics);
		if (itemInfo == null) {
			pageHeaderInfo.setPageTitle(siteName + " - " + getLanguageByValue("Page not found"));
        	pageHeaderInfo.setMetaKeywords("");
        	pageHeaderInfo.setMetaDescription("");
        	return pageHeaderInfo;
		}
		pageHeaderInfo.setPageTitle(itemInfo.getPageTitle());
		pageHeaderInfo.setMetaKeywords(itemInfo.getMetaKeywords());
		pageHeaderInfo.setMetaDescription(itemInfo.getMetaDescription());

		return pageHeaderInfo;
	}
	
	public PageHeaderInfo getPageItemCommentInfo() throws Exception {
		return getPageItemInfo();
	}
	
	public PageHeaderInfo getPageContentInfo() throws Exception {
		PageHeaderInfo pageHeaderInfo = new PageHeaderInfo();
		String contentNaturalKey = getContentKey();
		boolean updateStatistics = false;
		ContentInfo contentInfo = api.getContent(contentNaturalKey, updateStatistics);
		if (contentInfo == null) {
			pageHeaderInfo.setPageTitle(siteName + " - " + getLanguageByValue("Page not found"));
        	pageHeaderInfo.setMetaKeywords("");
        	pageHeaderInfo.setMetaDescription("");
        	return pageHeaderInfo;
		}
		
		pageHeaderInfo.setPageTitle(contentInfo.getPageTitle());
		pageHeaderInfo.setMetaKeywords(contentInfo.getMetaKeywords());
		pageHeaderInfo.setMetaDescription(contentInfo.getMetaDescription());

		return pageHeaderInfo;
	}
	
	public PageHeaderInfo getPageContentCommentInfo() throws Exception {
		return getPageContentInfo();
	}
	
	public PageHeaderInfo getPageContactUsInfo() throws Exception {
		PageHeaderInfo pageHeaderInfo = new PageHeaderInfo();
		String value = getLanguageByValue("Contact Us");
		pageHeaderInfo.setPageTitle(value);
		pageHeaderInfo.setMetaKeywords(value);
		pageHeaderInfo.setMetaDescription(value);
		return pageHeaderInfo;
	}
	
	public PageHeaderInfo getPageItemCompareInfo() throws Exception {
		PageHeaderInfo pageHeaderInfo = new PageHeaderInfo();
		String value = getLanguageByValue("Item Compare");
		pageHeaderInfo.setPageTitle(value);
		pageHeaderInfo.setMetaKeywords(value);
		pageHeaderInfo.setMetaDescription(value);
		return pageHeaderInfo;
	}
	
	public PageHeaderInfo getPageSearchInfo() throws Exception {
		PageHeaderInfo pageHeaderInfo = new PageHeaderInfo();
		String value = getLanguageByValue("Search");
		pageHeaderInfo.setPageTitle(value);
		pageHeaderInfo.setMetaKeywords(value);
		pageHeaderInfo.setMetaDescription(value);
		return pageHeaderInfo;
	}
	
	/*
	 * To be deprecated.
	 */
	
	/**
     * @deprecated
     * Replace by getPageHeader()
     */
    @Deprecated
	public String getPageTitle() throws Exception {
		if (isPageHome()) {
			return getPageHomeTitle();
		}
		else if (isPageContactUs()) {
			return getPageContactUsTitle();
		}
		else if (isPageContent()) {
			return getPageContentTitle();
		}
		else if (isPageContentComment()) {
			return getPageContentCommentTitle();
		}
		else if (isPageItem()) {
			return getPageItemTitle();
		}
		else if (isPageItemComment()) {
			return getPageItemCommentTitle();
		}
		else if (isPageCategory()) {
			return getPageCategoryTitle();
		}
		else if (isPageItemCompare()) {
			return getPageItemCompareTitle();
		}
		else if (isPageSearch()) {
			return getPageSearchTitle();
		}
		return siteName;
	}
	
	/**
     * @deprecated
     * Calling method getPageTitle() is deprecated.
     */
    @Deprecated
    public String getPageHomeTitle() {
		ContentBean contentBean = ContentLookupDispatchAction.getContentBean(request);
		HomePage homePage = siteDomain.getHomePage();
		String homePageTitle = siteName;
		if (!Format.isNullOrEmpty(homePage.getHomePageLanguage().getHomePageTitle())) {
			homePageTitle = homePage.getHomePageLanguage().getHomePageTitle();
		}
		if (!contentBean.getContentSessionBean().isSiteProfileClassDefault()) {
			for (HomePageLanguage language : homePage.getHomePageLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
					if (language.getHomePageTitle() != null) {
						homePageTitle = language.getHomePageTitle();
						break;
					}
				}
			}
		}
		return homePageTitle;
	}
	
	/**
     * @deprecated
     * Calling method getPageTitle() is deprecated.
     */
    @Deprecated
	public String getPageCategoryTitle() throws Exception {
		String catNaturalKey = getCategoryParameter(request, 3);
		Category category = dataApi.getCategory(siteDomain.getSite().getSiteId(), catNaturalKey);
        if (category == null) {
        	return siteName + " - " + getLanguageByValue("Page not found");
        }
        String catShortTitle = category.getCategoryLanguage().getCatShortTitle();
        ContentBean contentBean = ContentLookupDispatchAction.getContentBean(request);
        if (!contentBean.getContentSessionKey().isSiteProfileClassDefault()) {
        	for (CategoryLanguage language : category.getCategoryLanguages()) {
        		if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
        			if (language.getCatShortTitle() != null) {
        				catShortTitle = language.getCatShortTitle();
        			}
        			break;
        		}
        	}
        }
        return siteName + " - " + catShortTitle;
	}
	
	/**
     * @deprecated
     * Calling method getPageTitle() is deprecated.
     */
    @Deprecated
	public String getPageItemTitle() throws Exception {
		String itemNaturalKey = getItemKey();
		boolean updateStatistics = false;
		ItemInfo itemInfo = api.getItem(itemNaturalKey, updateStatistics);
		if (itemInfo == null) {
			return siteName + " - " + getLanguageByValue("Page not found");
		}
		return siteName + " - " + itemInfo.getPageTitle();
	}
	
	/**
     * @deprecated
     * Calling method getPageTitle() is deprecated.
     */
    @Deprecated
	public String getPageItemCommentTitle() throws Exception {
		String itemNaturalKey = getItemKey();
		boolean updateStatistics = false;
		ItemInfo itemInfo = api.getItem(itemNaturalKey, updateStatistics);
		return siteName + " - " + itemInfo.getPageTitle();
	}
	
	/**
     * @deprecated
     * Calling method getPageTitle() is deprecated.
     */
    @Deprecated
	public String getPageItemCommentUpdateTitle() throws Exception {
		return getPageItemCommentTitle();
	}
	
	/**
     * @deprecated
     * Calling method getPageTitle() is deprecated.
     */
    @Deprecated
	public String getPageContentTitle() throws Exception {
		String contentNaturalKey = getContentKey();
		boolean updateStatistics = false;
		ContentInfo contentInfo = api.getContent(contentNaturalKey, updateStatistics);
		return siteName + " - " + contentInfo.getPageTitle();
	}
	
	/**
     * @deprecated
     * Calling method getPageTitle() is deprecated.
     */
    @Deprecated
	public String getPageContentCommentTitle() throws Exception {
		return getPageContentTitle();
	}
	
	/**
     * @deprecated
     * Calling method getPageTitle() is deprecated.
     */
    @Deprecated
	public String getPageContentCommentUpdateTitle() {
		return null;
	}
	
	/**
     * @deprecated
     * Calling method getPageTitle() is deprecated.
     */
    @Deprecated
	public String getPageContactUsTitle() throws Exception {
		return siteName + " - " + getLanguageByValue("Contact Us");
	}
	
	/**
     * @deprecated
     * Calling method getPageTitle() is deprecated.
     */
    @Deprecated
	public String getPageSearchTitle() throws Exception {
		return siteName + " - " + getLanguageByValue("Search");
	}
	
	/**
     * @deprecated
     * Calling method getPageTitle() is deprecated.
     */
    @Deprecated
	public String getPageItemCompareTitle() throws Exception {
		return siteName + " - " + getLanguageByValue("Item Compare");
	}
    
    public MenuInfo[] getMenu(String menuSetName) throws Exception {
		boolean customerSession = ContentLookupDispatchAction.isCustomerSession(request);
		Vector<?> vector = api.getMenu(menuSetName, customerSession);
		MenuInfo menuInfo[] = new MenuInfo[vector.size()];
		vector.copyInto(menuInfo);
    	return menuInfo;
    }
	
	public MenuComponentInfo getHorizontalMenu(String menuSetName, String styleClassSuffix) throws ResourceNotFoundException, ParseErrorException, Exception {
		MenuComponentInfo menuComponentInfo = new MenuComponentInfo();
		/* Generate unique id for yui's div.  Important when rendering 2 menus with the same menuSetName. */
		String menuDivId = ContentUtility.getNextMenuDivId(menuSetName);
		menuComponentInfo.setMenuDivId(menuDivId);
		boolean vertical = false;
		boolean customerSession = ContentLookupDispatchAction.isCustomerSession(request);
		String horizontalMenuCode = ContentUtility.generateMenu(api, menuSetName, menuDivId, vertical, customerSession, styleClassSuffix);
		menuComponentInfo.setMenuCode(horizontalMenuCode);
		return menuComponentInfo;
	}
	
	public MenuComponentInfo getVerticalMenu(String menuSetName, String styleClassSuffix) throws ResourceNotFoundException, ParseErrorException, Exception  {
		MenuComponentInfo menuComponentInfo = new MenuComponentInfo();
		String menuDivId = ContentUtility.getNextMenuDivId(menuSetName);
		menuComponentInfo.setMenuDivId(menuDivId);
		boolean vertical = true;
		boolean customerSession = ContentLookupDispatchAction.isCustomerSession(request);
		String verticalMenuCode = ContentUtility.generateMenu(api, menuSetName, menuDivId, vertical, customerSession, styleClassSuffix);
		menuComponentInfo.setMenuCode(verticalMenuCode);
		return menuComponentInfo;
	}
	
	public DataInfo getHomeFeatureData() throws Exception {
		return api.getHomeFeatureData();
	}
	
	public DataInfo[] getHomeDataInfos() throws Exception {
		return api.getHomeDataInfos();
	}
	
	public ItemInfo[] getMostPopularItems() throws Exception {
		return api.getMostPopularItems(getModuleDisplaySize());
	}
	
	public ContentInfo[] getTopRatedContent() throws Exception {
		return api.getTopRatedContent(getModuleDisplaySize());
	}
	
	public SyndicationInfo[] getSyndications() throws ResourceNotFoundException, ParseErrorException, Exception  {
		return api.getSyndication();
	}
	
	public ContactUsInfoList getContactUs() throws ResourceNotFoundException, ParseErrorException, Exception  {
		ContactUsInfoList contactUsInfoList = new ContactUsInfoList();
		ContactUsInfo[] contactUsInfo = api.getContactUs();
		contactUsInfoList.setContactUsInfos(contactUsInfo);
		String message = request.getParameter("message");
		String messageText = "";
		if (message != null) {
			messageText = this.getLanguage(message);
		}
		contactUsInfoList.setMessageText(messageText);
		return contactUsInfoList;
	}
	
	public ShoppingCartSummaryInfo getShoppingCartSummary() throws ResourceNotFoundException, ParseErrorException, Exception  {
		return api.getShoppingCartSummary(request);
	}
	
	public String getContentKey() throws Exception {
		String contentNaturalKey = Utility.reEncode(getCategoryParameter(request, 2));
		return contentNaturalKey;
	}
	
	public ContentInfo getContent() throws Exception {
		String contentNaturalKey = getContentKey();
		boolean updateStatistics = true;
		ContentInfo contentInfo = api.getContent(contentNaturalKey, updateStatistics);
		return contentInfo;
	}
	
	public ContentInfo[] getRelatedContents() throws Exception {
		return api.getRelatedContent(getContentKey());
	}
	
	public CommentInfo[] getContentComment() throws Exception {
		return api.getContentComment(getContentKey());
	}
	
	public Hashtable<String, String> updateContentComment() throws Exception {
		Customer customer = ContentLookupDispatchAction.getCustomer(request);
		if (customer == null) {
			return null;
		}
		
		String contentNaturalKey = Utility.reEncode(getCategoryParameter(request, 2));
		String commentTitle = request.getParameter("commentTitle");
		commentTitle = Utility.escapeStrictHTML(commentTitle);
		String commentLine = request.getParameter("comment");
		commentLine = Utility.escapeStrictHTML(commentLine);

		Hashtable<String, String> attributes = new Hashtable<String, String>();
		if (Format.isNullOrEmpty(commentTitle)) {
			attributes.put("commentTitleMessage", getLanguage("content.error.string.required"));
		}
		if (Format.isNullOrEmpty(commentLine)) {
			attributes.put("commentMessage", getLanguage("content.error.string.required"));
		}
		
		if (attributes.size() == 0) {
			EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
			Content content = (Content) ContentDAO.loadNatural(siteDomain.getSite().getSiteId(), contentNaturalKey);
			Comment comment = new Comment();
			comment.setCommentTitle(commentTitle);
			comment.setComment(commentLine);
			comment.setCommentRating(0);
			comment.setActive(Constants.VALUE_YES);
			String custName = customer.getCustEmail();
			if (custName.length() > 20) {
				custName = custName.substring(0, 19);
			}
			comment.setRecCreateBy(custName);
			comment.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			comment.setRecUpdateBy(custName);
			comment.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			comment.setCustomer(customer);
			comment.setContent(content);
			content.getComments().add(comment);
			em.persist(comment);
			
			attributes.put("commentTitle", "");
			attributes.put("comment", "");
		}
		else {
			attributes.put("commentTitle", commentTitle);
			attributes.put("comment", commentLine);
		}
		return attributes;
	}
	
	public String getItemKey() throws Exception {
		String itemNaturalKey = Utility.reEncode(getCategoryParameter(request, 2));
		return itemNaturalKey;
	}
	
	public ItemInfo getItem() throws Exception {
		String itemNaturalKey = getItemKey();
		boolean updateStatistics = true;
		ItemInfo itemInfo = api.getItem(itemNaturalKey, updateStatistics);
		return itemInfo;
	}
	
	public CategoryInfo getItemsInCategory(String catNaturalKey, int pageNum, String sortBy) throws Exception {
		String topCatNaturalKey = "";
		ContentFilterBean contentFilterBeans[] = {};
		CategoryInfo categoryInfo = api.getCategory(catNaturalKey, topCatNaturalKey, pageSize, Constants.PAGE_NAV_COUNT, pageNum, sortBy, contentFilterBeans);
		return categoryInfo;
	}

	public Hashtable<String, String> updateItemComment() throws Exception {
		Customer customer = ContentLookupDispatchAction.getCustomer(request);
		if (customer == null) {
			return null;
		}
		
		String itemNaturalKey = Utility.reEncode(getCategoryParameter(request, 2));
		String commentTitle = request.getParameter("commentTitle");
		commentTitle = Utility.escapeStrictHTML(commentTitle);
		String commentLine = request.getParameter("comment");
		commentLine = Utility.escapeStrictHTML(commentLine);
		String commentRating = request.getParameter("commentRating");

		Hashtable<String, String> attributes = new Hashtable<String, String>();
		if (Format.isNullOrEmpty(commentTitle)) {
			attributes.put("commentTitleMessage", getLanguage("content.error.string.required"));
		}
		if (Format.isNullOrEmpty(commentLine)) {
			attributes.put("commentMessage", getLanguage("content.error.string.required"));
		}
		if (Format.isNullOrEmpty(commentRating) || commentRating.equals("0")) {
			attributes.put("commentRatingMessage", getLanguage("content.error.string.required"));
		}

		if (attributes.size() == 0) {
			EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
			Item item = (Item) ItemDAO.loadNatural(siteDomain.getSite().getSiteId(), itemNaturalKey);
			Comment comment = new Comment();
			comment.setCommentTitle(commentTitle);
			comment.setComment(commentLine);
			comment.setCommentRating(Integer.valueOf(commentRating));
			comment.setActive(Constants.VALUE_YES);
			String custName = customer.getCustEmail();
			if (custName.length() > 20) {
				custName = custName.substring(0, 19);
			}
			comment.setRecCreateBy(custName);
			comment.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			comment.setRecUpdateBy(custName);
			comment.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			comment.setCustomer(customer);
			comment.setItem(item);
			item.getComments().add(comment);
			em.persist(comment);
			
			attributes.put("commentTitle", "");
			attributes.put("comment", "");
			attributes.put("commentRating", "");
		}
		else {
			attributes.put("commentTitle", commentTitle);
			attributes.put("comment", commentLine);
			attributes.put("commentRating", commentRating);
		}
		return attributes;
	}
	
	public CommentInfo[] getItemComment() throws Exception {
		return api.getItemComment(getItemKey());
	}
	
	public ItemInfo[] getRelatedItems() throws Exception {
		return api.getRelatedItems(getItemKey());
	}

	public ItemInfo[] getUpSellItems() throws Exception {
		return api.getUpSellItems(getItemKey());
	}

	public ItemInfo[] getCrossSellItems() throws Exception {
		return api.getCrossSellItems(getItemKey());
	}

	public ItemInfo[] getBundleItems() throws Exception {
		return api.getBundleItems(getItemKey());
	}

	public ItemInfo[] getSkuItems() throws Exception {
		return api.getSkuItems(getItemKey());
	}
	
	public CategoryInfo getCategory() throws Exception {
		String topCatNaturalKey = getCategoryParameter(request, 2);
		String catNaturalKey = getCategoryParameter(request, 3);
		
		String value = request.getParameter("pageNum");
		if (value == null) {
			value = "1";
		}
		int pageNum = Format.getInt(value);
		String sortBy = request.getParameter("sortBy");
		if (sortBy == null) {
			sortBy = "";
		}
		ContentFilterBean contentFilterBeans[] = getContentFilterBeans(request);

		CategoryInfo categoryInfo = api.getCategory(catNaturalKey, topCatNaturalKey, pageSize, Constants.PAGE_NAV_COUNT, pageNum, sortBy, contentFilterBeans);
		return categoryInfo;
	}
	
	public CategoryInfo[] getCategoryTitles() throws Exception {
		String topCatNaturalKey = getCategoryParameter(request, 2);
		String catNaturalKey = getCategoryParameter(request, 3);
		
		CategoryInfo categoryInfos[] = api.getCategoryTitles(catNaturalKey, topCatNaturalKey);
		return categoryInfos;
	}
	
	public CategoryInfo[] getCategoryChildren() throws Exception {
		String topCatNaturalKey = getCategoryParameter(request, 2);
		String catNaturalKey = getCategoryParameter(request, 3);
		
		CategoryInfo categoryInfos[] = api.getCategoryChildren(catNaturalKey, topCatNaturalKey);
		return categoryInfos;
	}
	
	protected ContentFilterBean[] getContentFilterBeans(HttpServletRequest request) throws Exception {
		Vector<ContentFilterBean> vector = new Vector<ContentFilterBean>();
		String filters[] = request.getParameterValues("filter");
		if (filters != null) {
			for (String filter : filters) {
				int pos = filter.indexOf(',');
				String customAttribId = filter.substring(0, pos);
				String customAttribOptionId = filter.substring(pos + 1);
				ContentFilterBean bean = new ContentFilterBean();
				bean.setCustomAttribId(Long.valueOf(customAttribId));
				bean.setCustomAttribOptionId(Long.valueOf(customAttribOptionId));
				CustomAttribute  customAttribute = CustomAttributeDAO.load(siteDomain.getSite().getSiteId(), Long.valueOf(customAttribId));
				bean.setSystemRecord(customAttribute.getSystemRecord());
				vector.add(bean);
			}
		}
		ContentFilterBean contentFilterBeans[] = new ContentFilterBean[vector.size()];
		vector.copyInto(contentFilterBeans);
		return contentFilterBeans;
	}
	
	public ItemComparePageInfo getItemCompare() throws Exception {
		return api.getItemComparePage();
	}
	
	public SearchInfo getSearch() throws Exception {
		String value = (String) request.getParameter("pageNum");
		if (value == null) {
			value = "1";
		}
		int pageNum = Format.getInt(value);
		String query = (String) request.getParameter("query");
		SearchInfo searchInfo = api.getSearch(siteDomain.getSite().getSiteId(), query, pageSize, Constants.PAGE_NAV_COUNT, pageNum);
		return searchInfo;
	}
	
	public OrderInfo getOrder() throws Exception {
		Long orderHeaderId = (Long) parameters.get("orderHeaderId");
		return api.getOrder(orderHeaderId);
	}

	/****************************************************************************/
	
	public String getResourcePrefix(String resource) {
		String prefix = "";
		String resourcePath = Utility.getTemplatePrefix(siteDomain.getSite(), getTemplateName()) + resource;
		File file = new File(resourcePath);
		if (file.exists()) {
			prefix = Utility.getTemplateUrlPrefix(siteDomain.getSite(), getTemplateName()) + resource;
		}
		else {
			prefix = "/" + ApplicationGlobal.getContextPath() + "/content/template/basic/" + resource;
		}
		return prefix;
	}
	
	public String getServletResourcePrefix() {
		return "/" + ApplicationGlobal.getContextPath() + "/content/template";
	}
	
	public String getTemplateResourcePrefix() {
		return Utility.getTemplateUrlPrefix(siteDomain.getSite(), getTemplateName());
	}
	
	public String getContextPath() {
		return ApplicationGlobal.getContextPath();
	}
	
	public String getRequestURL() {
		return request.getRequestURL().toString();
	}
	
	private int getModuleDisplaySize() {
		if (Format.isNullOrEmpty(siteDomainParamBean.getModuleDisplaySize())) {
			return Constants.TEMPLATE_MODULE_DISPLAY_SIZE;
		}
		return Format.getInt(siteDomainParamBean.getModuleDisplaySize());
	}
	
	public String getLanguage(String langTranKey) throws Exception {
		return Languages.getLangTranValue(siteProfile.getSiteProfileClass().getLanguage().getLangId(), langTranKey);
	}
	
	public String getLanguageByValue(String langTranValue) throws Exception {
		return Languages.getLangTranValueByEnglishValue(siteProfile.getSiteProfileClass().getLanguage().getLangId(), langTranValue);
	}
	
	public String getLanguageEscape(String langTranKey) throws Exception {
		String value = Languages.getLangTranValue(siteProfile.getSiteProfileClass().getLanguage().getLangId(), langTranKey);
		return StringEscapeUtils.escapeHtml(value).replaceAll("'", "&#rsquo;");
	}
	
	public String getLanguageByValueEscape(String langTranValue) throws Exception {
		String value = Languages.getLangTranValueByEnglishValue(siteProfile.getSiteProfileClass().getLanguage().getLangId(), langTranValue);
		return StringEscapeUtils.escapeHtml(value).replaceAll("'", "&#rsquo;");
	}
	
	public String nn(String input) {
		if (input == null) {
			return "";
		}
		return input;
	}
	
	public boolean isNullOrEmpty(String value) {
		return Format.isNullOrEmpty(value);
	}
	
	public boolean isPoll() throws Exception {
		return PollHeaderDAO.hasActivePoll(siteDomain.getSite().getSiteId());
	}
	
	public boolean isSyndication() throws Exception {
		return SyndicationDAO.hasSyndication(siteDomain.getSite().getSiteId());
	}
	
	public boolean isCustomerSession() throws Exception {
		return ContentLookupDispatchAction.isCustomerSession(request);
	}
	
	public boolean isShoppingCart() throws Exception {
		ContentBean contentBean = ContentLookupDispatchAction.getContentBean(request);
		SiteCurrency siteCurrency = contentBean.getContentSessionBean().getSiteCurrency();
		if (siteCurrency.getPayPalPaymentGateway() != null) {
			return true;
		}
		if (siteCurrency.getPaymentGateway() != null) {
			return true;
		}
		if (siteCurrency.getCashPayment() == Constants.VALUE_YES) {
			return true;
		}
		return false;
	}
	
	public boolean inCategory(String contentId, String catShortTitle) throws Exception {
		Content content = ContentDAO.load(siteDomain.getSite().getSiteId(), Long.valueOf(contentId));
		for (Category category : content.getCategories()) {
			if (category.getCategoryLanguage().getCatShortTitle().equals(catShortTitle)) {
				return true;
			}
		}
		return false;
	}
	
	public String getCategoryParameter(HttpServletRequest request, int pos) {
		String category = ContentLookupDispatchAction.getCategory(request);
		String tokens[] = category.split("/");
		if (pos > tokens.length - 1) {
			return null;
		}
		return tokens[pos];
	}
	
	public int getCount(Vector<?> vector) {
		if (vector == null) {
			return 0;
		}
		return vector.size();
	}
	public int getCount(Object objects[]) {
		if (objects == null) {
			return 0;
		}
		return objects.length;
	}
	public boolean isEmpty(Vector<?> vector) {
		if (vector == null) {
			return true;
		}
		if (vector.size() > 0) {
			return false;
		}
		return true;
	}
	public boolean isEmpty(Object objects[]) {
		if (objects == null) {
			return true;
		}
		if (objects.length > 0) {
			return false;
		}
		return true;
	}
	public Object getArrayEntry(Object object[], int index) {
		return object[index];
	}
	public Object getParameter(String name) {
		return parameters.get(name);
	}
	public void setParameter(String name, Object value) {
		parameters.put(name, value);
	}
	
	/****************************************************************************/

	public boolean isPageStatic() {
		String categoryName = ContentLookupDispatchAction.getCategoryName(request);
		if (categoryName != null && categoryName.equals(Constants.FRONTEND_URL_STATIC)) {
			return true;
		}
		return false;
	}
	public boolean isPageHome() {
		String categoryName = ContentLookupDispatchAction.getCategoryName(request);
		if (categoryName != null && categoryName.equals(Constants.FRONTEND_URL_HOME)) {
			return true;
		}
		return false;
	}
	public boolean isPageCategory() {
		String categoryName = ContentLookupDispatchAction.getCategoryName(request);
		if (categoryName != null && categoryName.equals(Constants.FRONTEND_URL_SECTION)) {
			return true;
		}
		return false;
	}
	public boolean isPageItem() {
		String categoryName = ContentLookupDispatchAction.getCategoryName(request);
		if (categoryName != null && categoryName.equals(Constants.FRONTEND_URL_ITEM)) {
			return true;
		}
		return false;
	}
	public boolean isPageItemComment() {
		String categoryName = ContentLookupDispatchAction.getCategoryName(request);
		if (categoryName != null && categoryName.equals(Constants.FRONTEND_URL_ITEMCOMMENT)) {
			return true;
		}
		return false;
	}
	public boolean isPageItemCommentUpdate() {
		String categoryName = ContentLookupDispatchAction.getCategoryName(request);
		if (categoryName != null && categoryName.equals(Constants.FRONTEND_URL_ITEMCOMMENTUPDATE)) {
			return true;
		}
		return false;
	}
	public boolean isPageContent() {
		String categoryName = ContentLookupDispatchAction.getCategoryName(request);
		if (categoryName != null && categoryName.equals(Constants.FRONTEND_URL_CONTENT)) {
			return true;
		}
		return false;
	}
	public boolean isPageContentComment() {
		String categoryName = ContentLookupDispatchAction.getCategoryName(request);
		if (categoryName != null && categoryName.equals(Constants.FRONTEND_URL_CONTENTCOMMENT)) {
			return true;
		}
		return false;
	}
	public boolean isPageContentCommentUpdate() {
		String categoryName = ContentLookupDispatchAction.getCategoryName(request);
		if (categoryName != null && categoryName.equals(Constants.FRONTEND_URL_CONTENTCOMMENTUPDATE)) {
			return true;
		}
		return false;
	}
	public boolean isPageContactUs() {
		String categoryName = ContentLookupDispatchAction.getCategoryName(request);
		if (categoryName != null && categoryName.equals(Constants.FRONTEND_URL_CONTACTUS)) {
			return true;
		}
		return false;
	}
	public boolean isPageSearch() {
		String categoryName = ContentLookupDispatchAction.getCategoryName(request);
		if (categoryName != null && categoryName.equals(Constants.FRONTEND_URL_SEARCH)) {
			return true;
		}
		return false;
	}
	public boolean isPageItemCompare() {
		String categoryName = ContentLookupDispatchAction.getCategoryName(request);
		if (categoryName != null && categoryName.equals(Constants.FRONTEND_URL_ITEMCOMPARE)) {
			return true;
		}
		return false;
	}
	public boolean isPageCustom() {
		if (!Format.isNullOrEmpty(customPage)) {
			return true;
		}
		return false;
	}
	public boolean isPageEmpty() {
		return emptyTemplate;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}
	
	public ActionForm getStrutForm() {
		return strutForm;
	}

	public void setStrutForm(ActionForm strutForm) {
		this.strutForm = strutForm;
	}

	public String getCustomPage() {
		return customPage;
	}

	public void setCustomPage(String customPage) {
		this.customPage = customPage;
	}



	/****************************************************************************/

}
