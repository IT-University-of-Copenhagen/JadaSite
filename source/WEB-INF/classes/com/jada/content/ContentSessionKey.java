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

package com.jada.content;

public class ContentSessionKey {
	String siteId;
	Long siteDomainId;
	Long siteProfileId;
	Long siteProfileClassId;
	String siteProfileClassName;
	Long siteCurrencyId;
	Long siteCurrencyClassId;
	String siteCurrencyClassName;
	Long langId;
	String langName;
	boolean siteProfileClassDefault;
	boolean siteCurrencyClassDefault;
	public Long getSiteDomainId() {
		return siteDomainId;
	}
	public void setSiteDomainId(Long siteDomainId) {
		this.siteDomainId = siteDomainId;
	}
	public Long getLangId() {
		return langId;
	}
	public void setLangId(Long langId) {
		this.langId = langId;
	}
	public boolean isSiteProfileClassDefault() {
		return siteProfileClassDefault;
	}
	public void setSiteProfileClassDefault(boolean siteProfileClassDefault) {
		this.siteProfileClassDefault = siteProfileClassDefault;
	}
	public Long getSiteProfileId() {
		return siteProfileId;
	}
	public void setSiteProfileId(Long siteProfileId) {
		this.siteProfileId = siteProfileId;
	}
	public Long getSiteCurrencyId() {
		return siteCurrencyId;
	}
	public void setSiteCurrencyId(Long siteCurrencyId) {
		this.siteCurrencyId = siteCurrencyId;
	}
	public boolean isSiteCurrencyClassDefault() {
		return siteCurrencyClassDefault;
	}
	public void setSiteCurrencyClassDefault(boolean siteCurrencyClassDefault) {
		this.siteCurrencyClassDefault = siteCurrencyClassDefault;
	}
	public Long getSiteProfileClassId() {
		return siteProfileClassId;
	}
	public void setSiteProfileClassId(Long siteProfileClassId) {
		this.siteProfileClassId = siteProfileClassId;
	}
	public Long getSiteCurrencyClassId() {
		return siteCurrencyClassId;
	}
	public void setSiteCurrencyClassId(Long siteCurrencyClassId) {
		this.siteCurrencyClassId = siteCurrencyClassId;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
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
	public String getLangName() {
		return langName;
	}
	public void setLangName(String langName) {
		this.langName = langName;
	}
}
