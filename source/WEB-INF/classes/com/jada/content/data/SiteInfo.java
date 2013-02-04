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

package com.jada.content.data;

import java.util.Vector;

public class SiteInfo extends DataInfo {
	String siteId;
	String siteLogoUrl;
	String homeUrl;
	String siteName;
	String siteDomainPrefix;
	String publicURLPrefix;
	String secureURLPrefix;
	String siteFooter;
	String siteProfileClassName;
	String siteCurrencyClassName;
	String langName;
	String contextPath;
	Vector<?> languages;
	Vector<?> currencies;
	boolean singleCheckout;
	Vector<?> siteDomains;
	public Vector<?> getLanguages() {
		return languages;
	}
	public void setLanguages(Vector<?> languages) {
		this.languages = languages;
	}
	public Vector<?> getCurrencies() {
		return currencies;
	}
	public void setCurrencies(Vector<?> currencies) {
		this.currencies = currencies;
	}
	public String getPublicURLPrefix() {
		return publicURLPrefix;
	}
	public void setPublicURLPrefix(String publicURLPrefix) {
		this.publicURLPrefix = publicURLPrefix;
	}
	public String getSecureURLPrefix() {
		return secureURLPrefix;
	}
	public void setSecureURLPrefix(String secureURLPrefix) {
		this.secureURLPrefix = secureURLPrefix;
	}
	public String getSiteFooter() {
		return siteFooter;
	}
	public void setSiteFooter(String siteFooter) {
		this.siteFooter = siteFooter;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getSiteLogoUrl() {
		return siteLogoUrl;
	}
	public void setSiteLogoUrl(String siteLogoUrl) {
		this.siteLogoUrl = siteLogoUrl;
	}
	public String getHomeUrl() {
		return homeUrl;
	}
	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}
	public String getSiteProfileClassName() {
		return siteProfileClassName;
	}
	public void setSiteProfileClassName(String siteProfileClassName) {
		this.siteProfileClassName = siteProfileClassName;
	}
	public String getSiteCurrencyClassName() {
		return siteCurrencyClassName;
	}
	public void setSiteCurrencyClassName(String siteCurrencyClassName) {
		this.siteCurrencyClassName = siteCurrencyClassName;
	}
	public String getSiteDomainPrefix() {
		return siteDomainPrefix;
	}
	public void setSiteDomainPrefix(String siteDomainPrefix) {
		this.siteDomainPrefix = siteDomainPrefix;
	}
	public String getLangName() {
		return langName;
	}
	public void setLangName(String langName) {
		this.langName = langName;
	}
	public boolean isSingleCheckout() {
		return singleCheckout;
	}
	public void setSingleCheckout(boolean singleCheckout) {
		this.singleCheckout = singleCheckout;
	}
	public Vector<?> getSiteDomains() {
		return siteDomains;
	}
	public void setSiteDomains(Vector<?> siteDomains) {
		this.siteDomains = siteDomains;
	}
	public String getContextPath() {
		return contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
}
