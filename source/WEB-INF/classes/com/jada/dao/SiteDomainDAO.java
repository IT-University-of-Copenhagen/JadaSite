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

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.HomePage;
import com.jada.jpa.entity.HomePageLanguage;
import com.jada.jpa.entity.Menu;
import com.jada.jpa.entity.MenuLanguage;
import com.jada.jpa.entity.SiteCurrency;
import com.jada.jpa.entity.SiteCurrencyClass;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.SiteDomainLanguage;
import com.jada.jpa.entity.SiteProfile;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;
import com.jada.xml.site.SiteDomainParamBean;

public class SiteDomainDAO extends SiteDomain {
	private static final long serialVersionUID = 5685286772398362205L;

	public static SiteDomain load(Long siteDomainId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	SiteDomain siteDomain = (SiteDomain) em.find(SiteDomain.class, siteDomainId);
		return siteDomain;
	}
	
	public static void add(SiteDomain siteDomain, String userId, SiteProfileClass siteProfileClassDefault, SiteCurrencyClass siteCurrencyClassDefault) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Menu menu = new Menu();
		menu.setMenuSetName(Constants.MENUSET_MAIN);
		menu.setSeqNum(0);
		menu.setMenuType("");
		menu.setMenuUrl("");
		menu.setMenuWindowTarget("");
		menu.setMenuWindowMode("");
		menu.setPublished(Constants.VALUE_YES);
		menu.setRecUpdateBy(userId);
		menu.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		menu.setRecCreateBy(userId);
		menu.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		menu.setSiteDomain(siteDomain);
		MenuLanguage menuLanguage = new MenuLanguage();
		menu.setMenuLanguage(menuLanguage);
		menuLanguage.setMenuName("Home");
		menuLanguage.setRecUpdateBy(userId);
		menuLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		menuLanguage.setRecCreateBy(userId);
		menuLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		menuLanguage.setSiteProfileClass(siteProfileClassDefault);
		menuLanguage.setMenu(menu);
		em.persist(menuLanguage);
		em.persist(menu);
		
		menu = new Menu();
		menu.setMenuSetName(Constants.MENUSET_SECONDARY);
		menu.setSeqNum(0);
		menu.setMenuType("");
		menu.setMenuUrl("");
		menu.setMenuWindowTarget("");
		menu.setMenuWindowMode("");
		menu.setPublished(Constants.VALUE_YES);
		menu.setRecUpdateBy(userId);
		menu.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		menu.setRecCreateBy(userId);
		menu.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		menu.setSiteDomain(siteDomain);
		menuLanguage = new MenuLanguage();
		menu.setMenuLanguage(menuLanguage);
		menuLanguage.setMenuName("Home");
		menuLanguage.setRecUpdateBy(userId);
		menuLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		menuLanguage.setRecCreateBy(userId);
		menuLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		menuLanguage.setSiteProfileClass(siteProfileClassDefault);
		menuLanguage.setMenu(menu);
		em.persist(menuLanguage);
		em.persist(menu);

		SiteProfile siteProfileDefault = new SiteProfile();
		siteProfileDefault.setSiteDomain(siteDomain);
		siteProfileDefault.setSeqNum(new Integer(0));
		siteProfileDefault.setSiteProfileClass(siteProfileClassDefault);
		siteProfileDefault.setActive(Constants.VALUE_YES);
		siteProfileDefault.setRecUpdateBy(userId);
		siteProfileDefault.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		siteProfileDefault.setRecCreateBy(userId);
		siteProfileDefault.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		em.persist(siteProfileDefault);
		siteDomain.setSiteProfileDefault(siteProfileDefault);

		SiteCurrency siteCurrencyDefault = new SiteCurrency();
		siteCurrencyDefault.setSiteDomain(siteDomain);
		siteCurrencyDefault.setSeqNum(new Integer(0));
		siteCurrencyDefault.setExchangeRate(new Float(1));
		siteCurrencyDefault.setCashPayment(Constants.VALUE_YES);
		siteCurrencyDefault.setActive(Constants.VALUE_YES);
		siteCurrencyDefault.setSiteCurrencyClass(siteCurrencyClassDefault);
		siteCurrencyDefault.setRecUpdateBy(userId);
		siteCurrencyDefault.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		siteCurrencyDefault.setRecCreateBy(userId);
		siteCurrencyDefault.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		em.persist(siteCurrencyDefault);
		siteDomain.setSiteCurrencyDefault(siteCurrencyDefault);
		siteDomain.setBaseCurrency(siteCurrencyClassDefault);
		
		HomePage homePage = new HomePage();
		homePage.setRecUpdateBy(userId);
		homePage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		homePage.setRecCreateBy(userId);
		homePage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		
		HomePageLanguage homePageLanguage = new HomePageLanguage();
		homePageLanguage.setHomePageTitle("");
		homePageLanguage.setRecUpdateBy(userId);
		homePageLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		homePageLanguage.setRecCreateBy(userId);
		homePageLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		homePageLanguage.setSiteProfileClass(siteProfileClassDefault);
		homePageLanguage.setHomePage(homePage);
		em.persist(homePageLanguage);
		
		homePage.setHomePageLanguage(homePageLanguage);
		em.persist(homePage);
		siteDomain.setHomePage(homePage);
		em.persist(siteDomain);
	}
	
	public static void remove(Long siteDomainId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		SiteDomain siteDomain = (SiteDomain) em.find(SiteDomain.class, siteDomainId);
		for (SiteProfile siteProfile : siteDomain.getSiteProfiles()) {
			em.remove(siteProfile);
		}
		for (SiteCurrency siteCurrency : siteDomain.getSiteCurrencies()) {
			em.remove(siteCurrency);
		}
		siteDomain.setSiteProfileDefault(null);
		siteDomain.setSiteCurrencyDefault(null);
		siteDomain.getSiteProfiles().clear();
		siteDomain.getSiteCurrencies().clear();
    	siteDomain.setTemplate(null);
		
		Query query = em.createQuery("from Menu menu where menu.siteDomain = :siteDomain");
		query.setParameter("siteDomain", siteDomain);
		Iterator<?> iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			Menu menu = (Menu) iterator.next();
			for (MenuLanguage menuLanguage : menu.getMenuLanguages()) {
				em.remove(menuLanguage);
			}
			em.remove(menu);
		}
    	
		em.remove(siteDomain);
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

	public static String getSecureURLPrefix(SiteDomain siteDomain) {
		String secureURLPrefix = getPublicURLPrefix(siteDomain);
		boolean secureEnabled = siteDomain.getSiteSslEnabled() == Constants.VALUE_YES;
		if (secureEnabled) {
			secureURLPrefix = "https://" + siteDomain.getSiteDomainName();
			String defaultPortNum = Constants.PORTNUM_SECURE;
			if (!Format.isNullOrEmpty(siteDomain.getSiteSecurePortNum()) && !siteDomain.getSiteSecurePortNum().equals(defaultPortNum)) {
				secureURLPrefix += ":" + siteDomain.getSiteSecurePortNum();
			}
		}
		return secureURLPrefix;
	}
	
	public static SiteDomainParamBean getSiteDomainParamBean(SiteDomainLanguage master, SiteDomainLanguage siteDomainLanguage) throws Exception {
		SiteDomainParamBean masterBean = (SiteDomainParamBean) Utility.joxUnMarshall(SiteDomainParamBean.class, master.getSiteDomainParam());
        SiteDomainParamBean siteDomainParamBean = null;
        if (siteDomainLanguage == null) {
        	siteDomainParamBean = new SiteDomainParamBean();
        }
        else {
        	siteDomainParamBean = (SiteDomainParamBean) Utility.joxUnMarshall(SiteDomainParamBean.class, siteDomainLanguage.getSiteDomainParam());
        }
        
        if (siteDomainParamBean.getCheckoutNotificationEmail() == null) {
        	siteDomainParamBean.setCheckoutNotificationEmail(masterBean.getCheckoutNotificationEmail());
        }
        if (siteDomainParamBean.getCheckoutShoppingCartMessage() == null) {
        	siteDomainParamBean.setCheckoutShoppingCartMessage(masterBean.getCheckoutShoppingCartMessage());
        }
        if (siteDomainParamBean.getTemplateFooter() == null) {
        	siteDomainParamBean.setTemplateFooter(masterBean.getTemplateFooter());
        }
        if (siteDomainParamBean.getPaymentPaypalApiUsername() == null) {
        	siteDomainParamBean.setPaymentPaypalApiUsername(masterBean.getPaymentPaypalApiUsername());
        }
        if (siteDomainParamBean.getPaymentPaypalApiPassword() == null) {
        	siteDomainParamBean.setPaymentPaypalApiPassword(masterBean.getPaymentPaypalApiPassword());
        }
        if (siteDomainParamBean.getPaymentPaypalSignature() == null) {
        	siteDomainParamBean.setPaymentPaypalSignature(masterBean.getPaymentPaypalSignature());
        }
        if (siteDomainParamBean.getPaymentPaypalEnvironment() == null) {
        	siteDomainParamBean.setPaymentPaypalEnvironment(masterBean.getPaymentPaypalEnvironment());
        }
        if (siteDomainParamBean.getPaymentPaypalExtraAmount() == null) {
        	siteDomainParamBean.setPaymentPaypalExtraAmount(masterBean.getPaymentPaypalExtraAmount());
        }
        if (siteDomainParamBean.getPaymentPaypalExtraPercentage() == null) {
        	siteDomainParamBean.setPaymentPaypalExtraPercentage(masterBean.getPaymentPaypalExtraPercentage());
        }
        if (siteDomainParamBean.getPaymentPaypalCustClassId() == null) {
        	siteDomainParamBean.setPaymentPaypalCustClassId(masterBean.getPaymentPaypalCustClassId());
        }
        if (siteDomainParamBean.getPaymentProcessType() == null) {
        	siteDomainParamBean.setPaymentProcessType(masterBean.getPaymentProcessType());
        }
        if (siteDomainParamBean.getCategoryPageSize() == null) {
        	siteDomainParamBean.setCategoryPageSize(masterBean.getCategoryPageSize());
        }
        if (siteDomainParamBean.getBusinessContactName() == null) {
        	siteDomainParamBean.setBusinessContactName(masterBean.getBusinessContactName());
        }
        if (siteDomainParamBean.getBusinessCompany() == null) {
        	siteDomainParamBean.setBusinessCompany(masterBean.getBusinessCompany());
        }
        if (siteDomainParamBean.getBusinessAddress1() == null) {
        	siteDomainParamBean.setBusinessAddress1(masterBean.getBusinessAddress1());
        }
        if (siteDomainParamBean.getBusinessAddress2() == null) {
        	siteDomainParamBean.setBusinessAddress2(masterBean.getBusinessAddress2());
        }
        if (siteDomainParamBean.getBusinessCity() == null) {
        	siteDomainParamBean.setBusinessCity(masterBean.getBusinessCity());
        }
        if (siteDomainParamBean.getBusinessStateCode() == null) {
        	siteDomainParamBean.setBusinessStateCode(masterBean.getBusinessStateCode());
        }
        if (siteDomainParamBean.getBusinessPostalCode() == null) {
        	siteDomainParamBean.setBusinessPostalCode(masterBean.getBusinessPostalCode());
        }
        if (siteDomainParamBean.getBusinessCountryCode() == null) {
        	siteDomainParamBean.setBusinessCountryCode(masterBean.getBusinessCountryCode());
        }
        if (siteDomainParamBean.getBusinessPhone() == null) {
        	siteDomainParamBean.setBusinessPhone(masterBean.getBusinessPhone());
        }
        if (siteDomainParamBean.getBusinessFax() == null) {
        	siteDomainParamBean.setBusinessFax(masterBean.getBusinessFax());
        }
        if (siteDomainParamBean.getBusinessEmail() == null) {
        	siteDomainParamBean.setBusinessEmail(masterBean.getBusinessEmail());
        }
        if (siteDomainParamBean.getMailFromPwdReset() == null) {
        	siteDomainParamBean.setMailFromPwdReset(masterBean.getMailFromPwdReset());
        }
        if (siteDomainParamBean.getSubjectPwdReset() == null) {
        	siteDomainParamBean.setSubjectPwdReset(masterBean.getSubjectPwdReset());
        }
        if (siteDomainParamBean.getMailFromCustSales() == null) {
        	siteDomainParamBean.setMailFromCustSales(masterBean.getMailFromCustSales());
        }
        if (siteDomainParamBean.getSubjectCustSales() == null) {
        	siteDomainParamBean.setSubjectCustSales(masterBean.getSubjectCustSales());
        }
        if (siteDomainParamBean.getMailFromNotification() == null) {
        	siteDomainParamBean.setMailFromNotification(masterBean.getMailFromNotification());
        }
        if (siteDomainParamBean.getSubjectNotification() == null) {
        	siteDomainParamBean.setSubjectNotification(masterBean.getSubjectNotification());
        }
        return siteDomainParamBean;
	}
}
