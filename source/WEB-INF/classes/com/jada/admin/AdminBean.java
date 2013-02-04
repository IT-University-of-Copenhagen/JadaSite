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

package com.jada.admin;

import javax.persistence.EntityManager;

import com.jada.dao.SiteDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.User;
import com.jada.system.ApplicationGlobal;
import com.jada.util.Constants;

public class AdminBean {
	String siteId;
	String userId;
	String userType;
	String contextPath;
	String contentPage;
	
	public void init(String userId, String siteId) throws Exception {
		this.siteId = siteId;
		if (siteId == null) {
			siteId = Constants.SITE_DEFAULT;
		}

		this.contextPath = ApplicationGlobal.getContextPath();
		this.userId = userId;
		User user = this.getUser();
		this.userType = user.getUserType();
	}
	
	public Site getSite() throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Site site = (Site) em.find(Site.class, siteId);
		return site;
	}
	public String getContextPath() {
		return contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	public User getUser() throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		User user = (User) em.find(User.class, userId);
		return user;
	}
	public void setUser(User user) throws Exception {
		userId = user.getUserId();
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getContentPage() {
		return contentPage;
	}
	public void setContentPage(String contentPage) {
		this.contentPage = contentPage;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
/**********************************/
    int listingPageCount = 0;
	public int getListingPageCount() {
		if (listingPageCount == 0) {
			return Constants.DEFAULT_LISTING_PAGE_COUNT;
		}
		return listingPageCount;
	}
	public void setListingPageCount(int listingPageCount) {
		this.listingPageCount = listingPageCount;
	}
	public int getListingPageSize() throws Exception {
		Site site = SiteDAO.load(siteId);
		if (site.getListingPageSize() != null) {
			if (site.getListingPageSize().intValue() > 0) {
				return site.getListingPageSize().intValue();
			}
		}
		return 1;
	}
}