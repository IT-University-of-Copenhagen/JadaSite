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

package com.jada.dao;

import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.admin.site.SiteLoader;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Category;
import com.jada.jpa.entity.CategoryLanguage;
import com.jada.jpa.entity.Currency;
import com.jada.jpa.entity.CustomAttribute;
import com.jada.jpa.entity.CustomAttributeLanguage;
import com.jada.jpa.entity.CustomerClass;
import com.jada.jpa.entity.IeProfileDetail;
import com.jada.jpa.entity.IeProfileHeader;
import com.jada.jpa.entity.Language;
import com.jada.jpa.entity.ProductClass;
import com.jada.jpa.entity.Report;
import com.jada.jpa.entity.ShippingType;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteCurrencyClass;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.SiteDomainLanguage;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.jpa.entity.Template;
import com.jada.jpa.entity.User;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;
import com.jada.xml.site.SiteDomainParamBean;
import com.jada.xml.site.SiteParamBean;

public class SiteDAO extends Site {
	private static final long serialVersionUID = 3177676445170572528L;

	public static Site load(String siteId, User signinUser) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String userType = signinUser.getUserType();
    	if (!userType.equals(Constants.USERTYPE_ADMIN) && !userType.equals(Constants.USERTYPE_SUPER)) {
    		throw new SecurityException();
    	}
    	
		Site site = (Site) em.find(Site.class, siteId);
		return site;
	}
	
	public static Site load(String siteId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Site site = (Site) em.find(Site.class, siteId);
		return site;
	}
	
	
	public static void remove(Site site, User user) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		SiteLoader siteLoader = new SiteLoader(site, user.getUserId());
//		siteLoader.preRemove1();
//		siteLoader.remove();
		siteLoader.remove();
		em.remove(site);
	}
	
	public static Site getDefaultSite(User user) throws Exception {
		if (user.getUserType().equals(Constants.USERTYPE_ADMIN) || user.getUserType().equals(Constants.USERTYPE_SUPER)) {
	        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
	        Query query = em.createQuery("from Site where siteId = :siteId");
	        query.setParameter("siteId", Constants.SITE_DEFAULT);
	        Iterator<?> iterator = query.getResultList().iterator();
	        while (iterator.hasNext()) {
				Site site = (Site) iterator.next();
				return site;
	        }
		}
		else {
			Iterator<?> iterator = user.getSites().iterator();
			while (iterator.hasNext()) {
				Site site = (Site) iterator.next();
				return site;
			}
		}
		return null;
	}
	
	public static void add(Site site, String userId, HttpServletRequest request) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		SiteLoader loader = new SiteLoader(site, userId);
		loader.load();
		em.flush();
		
		SiteProfileClass siteProfileClass = new SiteProfileClass();
		siteProfileClass.setSiteProfileClassName("English");
		siteProfileClass.setSiteProfileClassNativeName("English");
		siteProfileClass.setSite(site);
		Language language = LanguageDAO.loadByLanguageName("English");
		siteProfileClass.setLanguage(language);
		siteProfileClass.setSystemRecord(Constants.VALUE_YES);
		siteProfileClass.setRecUpdateBy(userId);
		siteProfileClass.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		siteProfileClass.setRecCreateBy(userId);
		siteProfileClass.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		em.persist(siteProfileClass);
		site.setSiteProfileClassDefault(siteProfileClass);
		
		SiteCurrencyClass siteCurrencyClass = new SiteCurrencyClass();
		Currency currency = CurrencyDAO.loadByCurrencyCode(site.getSiteId(), "USD");
		siteCurrencyClass.setCurrency(currency);
		siteCurrencyClass.setCurrencyLocaleCountry("US");
		siteCurrencyClass.setCurrencyLocaleLanguage("en");
		siteCurrencyClass.setSite(site);
		siteCurrencyClass.setSiteCurrencyClassName("USD");
		siteCurrencyClass.setSystemRecord(Constants.VALUE_YES);
		siteCurrencyClass.setRecUpdateBy(userId);
		siteCurrencyClass.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		siteCurrencyClass.setRecCreateBy(userId);
		siteCurrencyClass.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		em.persist(siteCurrencyClass);
		site.setSiteCurrencyClassDefault(siteCurrencyClass);
		
		Category category = new Category();
    	category.setCatNaturalKey("");
    	category.setSeqNum(0);
    	category.setPublished(Constants.VALUE_YES);
    	category.setSite(site);
    	category.setCatId(null);
    	category.setRecUpdateBy(userId);
    	category.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    	category.setRecCreateBy(userId);
    	category.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    	category.setMenus(null);
    	CategoryLanguage categoryLanguage = new CategoryLanguage();
    	categoryLanguage.setCatTitle("Home");
    	categoryLanguage.setCatShortTitle("Home");
    	categoryLanguage.setCatDesc("");
    	categoryLanguage.setRecUpdateBy(userId);
    	categoryLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    	categoryLanguage.setRecCreateBy(userId);
    	categoryLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    	categoryLanguage.setSiteProfileClass(siteProfileClass);
    	categoryLanguage.setCategory(category);
    	category.setCategoryLanguage(categoryLanguage);
    	em.persist(categoryLanguage);

    	category.setCategoryLanguages(null);
    	em.persist(category);
    	
    	CustomerClass customerClass = new CustomerClass();
    	customerClass.setCustClassName(Constants.CUSTOMER_CLASS_REGULAR);
    	customerClass.setSystemRecord(Constants.VALUE_YES);
    	customerClass.setRecUpdateBy(userId);
    	customerClass.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    	customerClass.setRecCreateBy(userId);
    	customerClass.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    	customerClass.setSite(site);
    	em.persist(customerClass);

    	ProductClass productClass = new ProductClass();
    	productClass.setProductClassName(Constants.PRODUCT_CLASS_REGULAR);
    	productClass.setSystemRecord(Constants.VALUE_YES);
    	productClass.setRecUpdateBy(userId);
    	productClass.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    	productClass.setRecCreateBy(userId);
    	productClass.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    	productClass.setSite(site);
    	em.persist(productClass);

    	ShippingType shippingType = new ShippingType();
    	shippingType.setShippingTypeName(Constants.SHIPPING_TYPE_REGULAR);
    	shippingType.setSystemRecord(Constants.VALUE_YES);
    	shippingType.setRecUpdateBy(userId);
    	shippingType.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    	shippingType.setRecCreateBy(userId);
    	shippingType.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    	shippingType.setSite(site);
    	em.persist(shippingType);
    	
    	CustomAttribute customAttribute = new CustomAttribute();
    	customAttribute.setCustomAttribDataTypeCode(Constants.CUSTOM_ATTRIBUTE_DATA_TYPE_CURRENCY);
    	customAttribute.setCustomAttribTypeCode(Constants.CUSTOM_ATTRIBUTE_TYPE_USER_SELECT_DROPDOWN);
    	customAttribute.setCustomAttribName("Price");
    	customAttribute.setItemCompare(Constants.VALUE_NO);
    	customAttribute.setSystemRecord(Constants.VALUE_YES);
    	customAttribute.setRecUpdateBy(userId);
    	customAttribute.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    	customAttribute.setRecCreateBy(userId);
    	customAttribute.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    	customAttribute.setSite(site);
    	
    	CustomAttributeLanguage customAttributeLanguage = new CustomAttributeLanguage();
    	customAttributeLanguage.setCustomAttribDesc("Price");
    	customAttributeLanguage.setRecUpdateBy(userId);
    	customAttributeLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    	customAttributeLanguage.setRecCreateBy(userId);
    	customAttributeLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    	customAttributeLanguage.setCustomAttribute(customAttribute);
    	customAttributeLanguage.setSiteProfileClass(siteProfileClass);
    	customAttribute.setCustomAttributeLanguage(customAttributeLanguage);
    	em.persist(customAttributeLanguage);
    	em.persist(customAttribute);

		String prefix = getNextPrefix();
		SiteDomain siteDomain = new SiteDomain();
		SiteDomainLanguage siteDomainLanguage = new SiteDomainLanguage();
		siteDomainLanguage.setSiteName("localhost");
		siteDomainLanguage.setRecUpdateBy(userId);
		siteDomainLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		siteDomainLanguage.setRecCreateBy(userId);
		siteDomainLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		SiteDomainParamBean siteDomainParamBean = new SiteDomainParamBean();
		siteDomainLanguage.setSiteDomainParam(Utility.joxMarshall("SiteDomainParamBean", siteDomainParamBean));
		siteDomainLanguage.setSiteProfileClass(siteProfileClass);
		em.persist(siteDomainLanguage);
		siteDomain.getSiteDomainLanguages().add(siteDomainLanguage);
		siteDomain.setSiteDomainLanguage(siteDomainLanguage);
		siteProfileClass.setSiteDomain(siteDomain);

		siteDomain.setSiteDomainName("localhost");
		siteDomain.setSiteDomainPrefix(prefix);
		siteDomain.setSitePublicPortNum("");
		if (request.getServerPort() != 80) {
			siteDomain.setSitePublicPortNum(String.valueOf(request.getServerPort()));
		}
		siteDomain.setSiteSslEnabled(Constants.VALUE_NO);
		siteDomain.setSiteSecurePortNum("");
		siteDomain.setActive(Constants.ACTIVE_YES);
		siteDomain.setRecUpdateBy(userId);
		siteDomain.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		siteDomain.setRecCreateBy(userId);
		siteDomain.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		siteDomain.setBaseCurrency(siteCurrencyClass);
		siteDomain.setSite(site);
		
		Template template = new Template();
    	template.setSite(site);
    	template.setTemplateName(Constants.TEMPLATE_BASIC);
    	template.setTemplateDesc("Basic template");
     	template.setRecUpdateBy(userId);
    	template.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    	template.setRecCreateBy(userId);
    	template.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    	em.persist(template);

		siteDomain.setTemplate(template);

		em.persist(siteDomain);
		site.setSiteDomainDefault(siteDomain);
		
		String sql = "from Report report where report.site.siteId = 'default' and systemRecord = 'Y'";
		Query query = em.createQuery(sql);
		Iterator<?> iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			Report masterReport = (Report) iterator.next();
			Report report = new Report();
			report.setSite(site);
			report.setReportName(masterReport.getReportName());
			report.setReportDesc(masterReport.getReportDesc());
			report.setReportText(masterReport.getReportText());
			report.setSystemRecord(Constants.VALUE_YES);
			report.setRecUpdateBy(userId);
			report.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			report.setRecCreateBy(userId);
			report.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			em.persist(report);
		}
		
		sql = "from IeProfileHeader ieProfileHeader where ieProfileHeader.site.siteId = 'default' and systemRecord = 'Y'";
		query = em.createQuery(sql);
		iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			IeProfileHeader masterProfileHeader = (IeProfileHeader) iterator.next();
			IeProfileHeader ieProfileHeader = new IeProfileHeader();
			ieProfileHeader.setSite(site);
			ieProfileHeader.setIeProfileHeaderName(masterProfileHeader.getIeProfileHeaderName());
			ieProfileHeader.setIeProfileType(masterProfileHeader.getIeProfileType());
			ieProfileHeader.setSystemRecord(Constants.VALUE_YES);
			ieProfileHeader.setRecUpdateBy(userId);
			ieProfileHeader.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			ieProfileHeader.setRecCreateBy(userId);
			ieProfileHeader.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			
			Iterator<?> detailIterator = masterProfileHeader.getIeProfileDetails().iterator();
			while (detailIterator.hasNext()) {
				IeProfileDetail masterProfileDetail = (IeProfileDetail) detailIterator.next();
				IeProfileDetail ieProfileDetail = new IeProfileDetail();
				ieProfileDetail.setIeProfileHeader(ieProfileHeader);
				ieProfileDetail.setIeProfileFieldName(masterProfileDetail.getIeProfileFieldName());
				ieProfileDetail.setIeProfileFieldValue(masterProfileDetail.getIeProfileFieldValue());
				ieProfileDetail.setIeProfileGroupIndex(masterProfileDetail.getIeProfileGroupIndex());
				ieProfileDetail.setIeProfileGroupName(masterProfileDetail.getIeProfileGroupName());
				ieProfileDetail.setIeProfilePosition(masterProfileDetail.getIeProfilePosition());
				ieProfileDetail.setSeqNum(masterProfileDetail.getSeqNum());
				ieProfileDetail.setRecUpdateBy(userId);
				ieProfileDetail.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
				ieProfileDetail.setRecCreateBy(userId);
				ieProfileDetail.setRecCreateDatetime(new Date(System.currentTimeMillis()));
				em.persist(ieProfileDetail);
			}
			em.persist(ieProfileHeader);
		}
		
		SiteDomainDAO.add(siteDomain, userId, site.getSiteProfileClassDefault(), site.getSiteCurrencyClassDefault());
	}
	
	public static String getNextPrefix() throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String sql = "";
    	int index = 0;
    	String prefix = "";
    	while (true) {
	    	sql = "select  count(*) " + 
	    		  "from  SiteDomain siteDomain " +
	    		  "where siteDomain.siteDomainPrefix = :siteDomainPrefix";
	    	Query query = em.createQuery(sql);
	    	prefix = "prefix";
	    	if (index > 0) {
	    		prefix += index;
	    	}
	    	query.setParameter("siteDomainPrefix", prefix);
	    	Long count = (Long) query.getSingleResult();
	    	if (count == 0) {
	    		break;
	    	}
	    	index++;
	    }
    	return prefix;
	}
	
	static public boolean isStoreCreditCard(Site site) throws Exception {
		if (Format.isNullOrEmpty(site.getSiteParam())) {
			return true;
		}
		SiteParamBean siteParamBean = (SiteParamBean) Utility.joxUnMarshall(SiteParamBean.class, site.getSiteParam());
		if (siteParamBean.getStoreCreditCard() == null) {
			return true;
		}
		if (siteParamBean.getStoreCreditCard().equals(String.valueOf(Constants.VALUE_NO))) {
			return false;
		}
		return true;
	}
	
	public static void initialize(Site site) throws Exception {
		// to init in order to avoid lazy initialization.
		site.getRecCreateDatetime();
	}
}
