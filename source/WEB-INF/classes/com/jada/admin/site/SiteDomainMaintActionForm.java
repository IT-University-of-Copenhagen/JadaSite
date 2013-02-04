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

package com.jada.admin.site;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;

import com.jada.admin.AdminMaintActionForm;

public class SiteDomainMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -8549561689037582372L;
	String mode;
	String random;
	String siteId;
	String siteDomainId;
	boolean siteDomainDefault;
	String siteName;
	String siteDomainUrl;
	String siteDomainPrefix;
	boolean siteNameLangFlag;
	String siteNameLang;
	String sitePublicPortNum;
	String siteSecurePortNum;
	String siteDomainName;
	boolean siteSecureConnectionEnabled;
	boolean siteLogoFlag;
	String siteLogoContentType;
	String siteLogoContentTypeLang;
	FormFile file;
	boolean active;
	boolean master;
	boolean singleCheckout;
	String checkoutNotificationEmail;
	String checkoutShoppingCartMessage;
	String checkoutShoppingCartMessageLang;
	boolean checkoutShoppingCartMessageLangFlag;
	boolean checkoutIncludePickup;
	boolean checkoutAllowsShippingQuote;
	String templateFooter;
	String templateFooterLang;
	boolean templateFooterLangFlag;
	boolean manageInventory;
	String categoryPageSize;
	LabelValueBean shippingTypes[];
	String templateId;
	LabelValueBean templates[];
	String businessContactName;
	String businessCompany;
	String businessAddress1;
	String businessAddress2;
	String businessCity;
	String businessStateCode;
	String businessPostalCode;
	String businessCountryCode;
	String businessPhone;
	String businessFax;
	String businessEmail;
	String mailFromPwdReset;
	String subjectPwdReset;
	String subjectPwdResetLang;
	boolean subjectPwdResetLangFlag;
	String mailFromCustSales;
	String subjectCustSales;
	String subjectCustSalesLang;
	boolean subjectCustSalesLangFlag;
	String mailFromNotification;
	String subjectNotification;
	String subjectNotificationLang;
	boolean subjectNotificationLangFlag;
	String mailFromShippingQuote;
	String subjectShippingQuote;
	String subjectShippingQuoteLang;
	boolean subjectShippingQuoteLangFlag;
	LabelValueBean siteCurrencyClasses[];
	LabelValueBean siteProfileClasses[];
	LabelValueBean states[];
	LabelValueBean countries[];
	LabelValueBean paymentGateways[];
	LabelValueBean payPalPaymentGateways[];
	String paymentProcessType;
	String paymentGateway;
	String tabIndex;
	SiteProfileForm siteProfiles[];
	SiteCurrencyForm siteCurrencies[];
	LabelValueBean customerClasses[];
	LabelValueBean locales[];
	String baseSiteCurrencyClassId;
	String moduleDisplaySize;
	String siteDomainLangId;
	String defaultSiteDomainLangId;
	String mailFromContactUs;
	public SiteProfileForm getSiteProfile(int index) {
    	return siteProfiles[index];
    }
	public SiteCurrencyForm getSiteCurrency(int index) {
    	return siteCurrencies[index];
    }
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		String TRAN = "siteProfile.*siteProfileClassId";
		String TRAN1 = "siteCurrency.*siteCurrencyClassId";
		int count = 0;
		int count1 = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(TRAN)) {
				count++;
			}
			if (name.matches(TRAN1)) {
				count1++;
			}
		}
		siteProfiles = new SiteProfileForm[count];
		for (int i = 0; i < siteProfiles.length; i++) {
			siteProfiles[i] = new SiteProfileForm();
		}
		if (siteProfiles.length > 0) {
			siteProfiles[0].setActive(true);
		}
		siteCurrencies = new SiteCurrencyForm[count1];
		for (int i = 0; i < siteCurrencies.length; i++) {
			siteCurrencies[i] = new SiteCurrencyForm();
		}
		if (siteCurrencies.length > 0) {
			siteCurrencies[0].setActive(true);
		}
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getRandom() {
		return random;
	}
	public void setRandom(String random) {
		this.random = random;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getSiteDomainId() {
		return siteDomainId;
	}
	public void setSiteDomainId(String siteDomainId) {
		this.siteDomainId = siteDomainId;
	}
	public boolean isSiteDomainDefault() {
		return siteDomainDefault;
	}
	public void setSiteDomainDefault(boolean siteDomainDefault) {
		this.siteDomainDefault = siteDomainDefault;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getSiteDomainPrefix() {
		return siteDomainPrefix;
	}
	public void setSiteDomainPrefix(String siteDomainPrefix) {
		this.siteDomainPrefix = siteDomainPrefix;
	}
	public boolean isSiteNameLangFlag() {
		return siteNameLangFlag;
	}
	public void setSiteNameLangFlag(boolean siteNameLangFlag) {
		this.siteNameLangFlag = siteNameLangFlag;
	}
	public String getSiteNameLang() {
		return siteNameLang;
	}
	public void setSiteNameLang(String siteNameLang) {
		this.siteNameLang = siteNameLang;
	}
	public String getSitePublicPortNum() {
		return sitePublicPortNum;
	}
	public void setSitePublicPortNum(String sitePublicPortNum) {
		this.sitePublicPortNum = sitePublicPortNum;
	}
	public String getSiteSecurePortNum() {
		return siteSecurePortNum;
	}
	public void setSiteSecurePortNum(String siteSecurePortNum) {
		this.siteSecurePortNum = siteSecurePortNum;
	}
	public String getSiteDomainName() {
		return siteDomainName;
	}
	public void setSiteDomainName(String siteDomainName) {
		this.siteDomainName = siteDomainName;
	}
	public boolean isSiteSecureConnectionEnabled() {
		return siteSecureConnectionEnabled;
	}
	public void setSiteSecureConnectionEnabled(boolean siteSecureConnectionEnabled) {
		this.siteSecureConnectionEnabled = siteSecureConnectionEnabled;
	}
	public boolean isSiteLogoFlag() {
		return siteLogoFlag;
	}
	public void setSiteLogoFlag(boolean siteLogoFlag) {
		this.siteLogoFlag = siteLogoFlag;
	}
	public String getSiteLogoContentType() {
		return siteLogoContentType;
	}
	public void setSiteLogoContentType(String siteLogoContentType) {
		this.siteLogoContentType = siteLogoContentType;
	}
	public String getSiteLogoContentTypeLang() {
		return siteLogoContentTypeLang;
	}
	public void setSiteLogoContentTypeLang(String siteLogoContentTypeLang) {
		this.siteLogoContentTypeLang = siteLogoContentTypeLang;
	}
	public FormFile getFile() {
		return file;
	}
	public void setFile(FormFile file) {
		this.file = file;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getCheckoutNotificationEmail() {
		return checkoutNotificationEmail;
	}
	public void setCheckoutNotificationEmail(String checkoutNotificationEmail) {
		this.checkoutNotificationEmail = checkoutNotificationEmail;
	}
	public String getCheckoutShoppingCartMessage() {
		return checkoutShoppingCartMessage;
	}
	public void setCheckoutShoppingCartMessage(String checkoutShoppingCartMessage) {
		this.checkoutShoppingCartMessage = checkoutShoppingCartMessage;
	}
	public String getCheckoutShoppingCartMessageLang() {
		return checkoutShoppingCartMessageLang;
	}
	public void setCheckoutShoppingCartMessageLang(
			String checkoutShoppingCartMessageLang) {
		this.checkoutShoppingCartMessageLang = checkoutShoppingCartMessageLang;
	}
	public boolean isCheckoutShoppingCartMessageLangFlag() {
		return checkoutShoppingCartMessageLangFlag;
	}
	public void setCheckoutShoppingCartMessageLangFlag(
			boolean checkoutShoppingCartMessageLangFlag) {
		this.checkoutShoppingCartMessageLangFlag = checkoutShoppingCartMessageLangFlag;
	}
	public String getTemplateFooter() {
		return templateFooter;
	}
	public void setTemplateFooter(String templateFooter) {
		this.templateFooter = templateFooter;
	}
	public String getTemplateFooterLang() {
		return templateFooterLang;
	}
	public void setTemplateFooterLang(String templateFooterLang) {
		this.templateFooterLang = templateFooterLang;
	}
	public boolean isTemplateFooterLangFlag() {
		return templateFooterLangFlag;
	}
	public void setTemplateFooterLangFlag(boolean templateFooterLangFlag) {
		this.templateFooterLangFlag = templateFooterLangFlag;
	}
	public boolean isManageInventory() {
		return manageInventory;
	}
	public void setManageInventory(boolean manageInventory) {
		this.manageInventory = manageInventory;
	}
	public String getCategoryPageSize() {
		return categoryPageSize;
	}
	public void setCategoryPageSize(String categoryPageSize) {
		this.categoryPageSize = categoryPageSize;
	}
	public LabelValueBean[] getShippingTypes() {
		return shippingTypes;
	}
	public void setShippingTypes(LabelValueBean[] shippingTypes) {
		this.shippingTypes = shippingTypes;
	}
	public LabelValueBean[] getTemplates() {
		return templates;
	}
	public void setTemplates(LabelValueBean[] templates) {
		this.templates = templates;
	}
	public String getBusinessContactName() {
		return businessContactName;
	}
	public void setBusinessContactName(String businessContactName) {
		this.businessContactName = businessContactName;
	}
	public String getBusinessCompany() {
		return businessCompany;
	}
	public void setBusinessCompany(String businessCompany) {
		this.businessCompany = businessCompany;
	}
	public String getBusinessAddress1() {
		return businessAddress1;
	}
	public void setBusinessAddress1(String businessAddress1) {
		this.businessAddress1 = businessAddress1;
	}
	public String getBusinessAddress2() {
		return businessAddress2;
	}
	public void setBusinessAddress2(String businessAddress2) {
		this.businessAddress2 = businessAddress2;
	}
	public String getBusinessCity() {
		return businessCity;
	}
	public void setBusinessCity(String businessCity) {
		this.businessCity = businessCity;
	}
	public String getBusinessStateCode() {
		return businessStateCode;
	}
	public void setBusinessStateCode(String businessStateCode) {
		this.businessStateCode = businessStateCode;
	}
	public String getBusinessPostalCode() {
		return businessPostalCode;
	}
	public void setBusinessPostalCode(String businessPostalCode) {
		this.businessPostalCode = businessPostalCode;
	}
	public String getBusinessCountryCode() {
		return businessCountryCode;
	}
	public void setBusinessCountryCode(String businessCountryCode) {
		this.businessCountryCode = businessCountryCode;
	}
	public String getBusinessPhone() {
		return businessPhone;
	}
	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}
	public String getBusinessFax() {
		return businessFax;
	}
	public void setBusinessFax(String businessFax) {
		this.businessFax = businessFax;
	}
	public String getBusinessEmail() {
		return businessEmail;
	}
	public void setBusinessEmail(String businessEmail) {
		this.businessEmail = businessEmail;
	}
	public String getMailFromPwdReset() {
		return mailFromPwdReset;
	}
	public void setMailFromPwdReset(String mailFromPwdReset) {
		this.mailFromPwdReset = mailFromPwdReset;
	}
	public String getSubjectPwdReset() {
		return subjectPwdReset;
	}
	public void setSubjectPwdReset(String subjectPwdReset) {
		this.subjectPwdReset = subjectPwdReset;
	}
	public String getSubjectPwdResetLang() {
		return subjectPwdResetLang;
	}
	public void setSubjectPwdResetLang(String subjectPwdResetLang) {
		this.subjectPwdResetLang = subjectPwdResetLang;
	}
	public boolean isSubjectPwdResetLangFlag() {
		return subjectPwdResetLangFlag;
	}
	public void setSubjectPwdResetLangFlag(boolean subjectPwdResetLangFlag) {
		this.subjectPwdResetLangFlag = subjectPwdResetLangFlag;
	}
	public String getMailFromCustSales() {
		return mailFromCustSales;
	}
	public void setMailFromCustSales(String mailFromCustSales) {
		this.mailFromCustSales = mailFromCustSales;
	}
	public String getSubjectCustSales() {
		return subjectCustSales;
	}
	public void setSubjectCustSales(String subjectCustSales) {
		this.subjectCustSales = subjectCustSales;
	}
	public String getSubjectCustSalesLang() {
		return subjectCustSalesLang;
	}
	public void setSubjectCustSalesLang(String subjectCustSalesLang) {
		this.subjectCustSalesLang = subjectCustSalesLang;
	}
	public boolean isSubjectCustSalesLangFlag() {
		return subjectCustSalesLangFlag;
	}
	public void setSubjectCustSalesLangFlag(boolean subjectCustSalesLangFlag) {
		this.subjectCustSalesLangFlag = subjectCustSalesLangFlag;
	}
	public String getMailFromNotification() {
		return mailFromNotification;
	}
	public void setMailFromNotification(String mailFromNotification) {
		this.mailFromNotification = mailFromNotification;
	}
	public String getSubjectNotification() {
		return subjectNotification;
	}
	public void setSubjectNotification(String subjectNotification) {
		this.subjectNotification = subjectNotification;
	}
	public String getSubjectNotificationLang() {
		return subjectNotificationLang;
	}
	public void setSubjectNotificationLang(String subjectNotificationLang) {
		this.subjectNotificationLang = subjectNotificationLang;
	}
	public boolean isSubjectNotificationLangFlag() {
		return subjectNotificationLangFlag;
	}
	public void setSubjectNotificationLangFlag(boolean subjectNotificationLangFlag) {
		this.subjectNotificationLangFlag = subjectNotificationLangFlag;
	}
	public LabelValueBean[] getSiteCurrencyClasses() {
		return siteCurrencyClasses;
	}
	public void setSiteCurrencyClasses(LabelValueBean[] siteCurrencyClasses) {
		this.siteCurrencyClasses = siteCurrencyClasses;
	}
	public LabelValueBean[] getSiteProfileClasses() {
		return siteProfileClasses;
	}
	public void setSiteProfileClasses(LabelValueBean[] siteProfileClasses) {
		this.siteProfileClasses = siteProfileClasses;
	}
	public LabelValueBean[] getStates() {
		return states;
	}
	public void setStates(LabelValueBean[] states) {
		this.states = states;
	}
	public LabelValueBean[] getCountries() {
		return countries;
	}
	public void setCountries(LabelValueBean[] countries) {
		this.countries = countries;
	}
	public LabelValueBean[] getPaymentGateways() {
		return paymentGateways;
	}
	public void setPaymentGateways(LabelValueBean[] paymentGateways) {
		this.paymentGateways = paymentGateways;
	}
	public String getPaymentProcessType() {
		return paymentProcessType;
	}
	public void setPaymentProcessType(String paymentProcessType) {
		this.paymentProcessType = paymentProcessType;
	}
	public String getPaymentGateway() {
		return paymentGateway;
	}
	public void setPaymentGateway(String paymentGateway) {
		this.paymentGateway = paymentGateway;
	}
	public String getTabIndex() {
		return tabIndex;
	}
	public void setTabIndex(String tabIndex) {
		this.tabIndex = tabIndex;
	}
	public SiteProfileForm[] getSiteProfiles() {
		return siteProfiles;
	}
	public void setSiteProfiles(SiteProfileForm[] siteProfiles) {
		this.siteProfiles = siteProfiles;
	}
	public SiteCurrencyForm[] getSiteCurrencies() {
		return siteCurrencies;
	}
	public void setSiteCurrencies(SiteCurrencyForm[] siteCurrencies) {
		this.siteCurrencies = siteCurrencies;
	}
	public LabelValueBean[] getCustomerClasses() {
		return customerClasses;
	}
	public void setCustomerClasses(LabelValueBean[] customerClasses) {
		this.customerClasses = customerClasses;
	}
	public LabelValueBean[] getLocales() {
		return locales;
	}
	public void setLocales(LabelValueBean[] locales) {
		this.locales = locales;
	}
	public LabelValueBean[] getPayPalPaymentGateways() {
		return payPalPaymentGateways;
	}
	public void setPayPalPaymentGateways(LabelValueBean[] payPalPaymentGateways) {
		this.payPalPaymentGateways = payPalPaymentGateways;
	}
	public String getBaseSiteCurrencyClassId() {
		return baseSiteCurrencyClassId;
	}
	public void setBaseSiteCurrencyClassId(String baseSiteCurrencyClassId) {
		this.baseSiteCurrencyClassId = baseSiteCurrencyClassId;
	}
	public String getSiteDomainUrl() {
		return siteDomainUrl;
	}
	public void setSiteDomainUrl(String siteDomainUrl) {
		this.siteDomainUrl = siteDomainUrl;
	}
	public boolean isMaster() {
		return master;
	}
	public void setMaster(boolean master) {
		this.master = master;
	}
	public boolean isSingleCheckout() {
		return singleCheckout;
	}
	public void setSingleCheckout(boolean singleCheckout) {
		this.singleCheckout = singleCheckout;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getModuleDisplaySize() {
		return moduleDisplaySize;
	}
	public void setModuleDisplaySize(String moduleDisplaySize) {
		this.moduleDisplaySize = moduleDisplaySize;
	}
	public String getSiteDomainLangId() {
		return siteDomainLangId;
	}
	public void setSiteDomainLangId(String siteDomainLangId) {
		this.siteDomainLangId = siteDomainLangId;
	}
	public String getDefaultSiteDomainLangId() {
		return defaultSiteDomainLangId;
	}
	public void setDefaultSiteDomainLangId(String defaultSiteDomainLangId) {
		this.defaultSiteDomainLangId = defaultSiteDomainLangId;
	}
	public String getMailFromContactUs() {
		return mailFromContactUs;
	}
	public void setMailFromContactUs(String mailFromContactUs) {
		this.mailFromContactUs = mailFromContactUs;
	}
	public boolean isCheckoutIncludePickup() {
		return checkoutIncludePickup;
	}
	public void setCheckoutIncludePickup(boolean checkoutIncludePickup) {
		this.checkoutIncludePickup = checkoutIncludePickup;
	}
	public boolean isCheckoutAllowsShippingQuote() {
		return checkoutAllowsShippingQuote;
	}
	public void setCheckoutAllowsShippingQuote(boolean checkoutAllowsShippingQuote) {
		this.checkoutAllowsShippingQuote = checkoutAllowsShippingQuote;
	}
	public String getMailFromShippingQuote() {
		return mailFromShippingQuote;
	}
	public void setMailFromShippingQuote(String mailFromShippingQuote) {
		this.mailFromShippingQuote = mailFromShippingQuote;
	}
	public String getSubjectShippingQuote() {
		return subjectShippingQuote;
	}
	public void setSubjectShippingQuote(String subjectShippingQuote) {
		this.subjectShippingQuote = subjectShippingQuote;
	}
	public String getSubjectShippingQuoteLang() {
		return subjectShippingQuoteLang;
	}
	public void setSubjectShippingQuoteLang(String subjectShippingQuoteLang) {
		this.subjectShippingQuoteLang = subjectShippingQuoteLang;
	}
	public boolean isSubjectShippingQuoteLangFlag() {
		return subjectShippingQuoteLangFlag;
	}
	public void setSubjectShippingQuoteLangFlag(boolean subjectShippingQuoteLangFlag) {
		this.subjectShippingQuoteLangFlag = subjectShippingQuoteLangFlag;
	}
}
