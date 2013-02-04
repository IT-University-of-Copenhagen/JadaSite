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

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminMaintActionForm;

public class HomePageMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -1688434677272433430L;
	String siteDomainId;
	String homePageId;
	String pageTitle;
	String metaKeywords;
	String metaDescription;
	String featureHomePageDetailId;
	boolean pageTitleLangFlag;
	boolean metaKeywordsLangFlag;
	boolean metaDescriptionLangFlag;
	String pageTitleLang;
	String metaKeywordsLang;
	String metaDescriptionLang;
	String itemId;
	String contentId;
	String homePageDetailIds[];
	HomePageDetailDisplayForm homePageDetails[];
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		String id = "homePageDetail.*homePageDetailId";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(id)) {
				count++;
			}
		}
		homePageDetails = new HomePageDetailDisplayForm[count];
		for (int i = 0; i < homePageDetails.length; i++) {
			homePageDetails[i] = new HomePageDetailDisplayForm();
		}
	}
	public HomePageDetailDisplayForm getHomePageDetail(int index) {
		return homePageDetails[index];
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public boolean isPageTitleLangFlag() {
		return pageTitleLangFlag;
	}
	public void setPageTitleLangFlag(boolean pageTitleLangFlag) {
		this.pageTitleLangFlag = pageTitleLangFlag;
	}
	public String getPageTitleLang() {
		return pageTitleLang;
	}
	public void setPageTitleLang(String pageTitleLang) {
		this.pageTitleLang = pageTitleLang;
	}
	public String getSiteDomainId() {
		return siteDomainId;
	}
	public void setSiteDomainId(String siteDomainId) {
		this.siteDomainId = siteDomainId;
	}
	public String getHomePageId() {
		return homePageId;
	}
	public void setHomePageId(String homePageId) {
		this.homePageId = homePageId;
	}
	public HomePageDetailDisplayForm[] getHomePageDetails() {
		return homePageDetails;
	}
	public void setHomePageDetails(HomePageDetailDisplayForm[] homePageDetails) {
		this.homePageDetails = homePageDetails;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String[] getHomePageDetailIds() {
		return homePageDetailIds;
	}
	public void setHomePageDetailIds(String[] homePageDetailIds) {
		this.homePageDetailIds = homePageDetailIds;
	}
	public String getFeatureHomePageDetailId() {
		return featureHomePageDetailId;
	}
	public void setFeatureHomePageDetailId(String featureHomePageDetailId) {
		this.featureHomePageDetailId = featureHomePageDetailId;
	}
	public String getMetaKeywords() {
		return metaKeywords;
	}
	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}
	public String getMetaDescription() {
		return metaDescription;
	}
	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}
	public boolean isMetaKeywordsLangFlag() {
		return metaKeywordsLangFlag;
	}
	public void setMetaKeywordsLangFlag(boolean metaKeywordsLangFlag) {
		this.metaKeywordsLangFlag = metaKeywordsLangFlag;
	}
	public boolean isMetaDescriptionLangFlag() {
		return metaDescriptionLangFlag;
	}
	public void setMetaDescriptionLangFlag(boolean metaDescriptionLangFlag) {
		this.metaDescriptionLangFlag = metaDescriptionLangFlag;
	}
	public String getMetaKeywordsLang() {
		return metaKeywordsLang;
	}
	public void setMetaKeywordsLang(String metaKeywordsLang) {
		this.metaKeywordsLang = metaKeywordsLang;
	}
	public String getMetaDescriptionLang() {
		return metaDescriptionLang;
	}
	public void setMetaDescriptionLang(String metaDescriptionLang) {
		this.metaDescriptionLang = metaDescriptionLang;
	}
}
