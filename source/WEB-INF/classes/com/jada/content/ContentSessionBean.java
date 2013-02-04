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

import javax.persistence.EntityManager;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.SiteCurrency;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.SiteProfile;

public class ContentSessionBean {
	Long siteDomainId;
	Long siteProfileId;
	Long siteCurrencyId;
	boolean siteProfileClassDefault;
	SiteDomain siteDomain;
	SiteProfile siteProfile;
	SiteCurrency siteCurrency;
	
	public ContentSessionBean(ContentSessionKey bean) {
		this.siteDomainId = bean.getSiteDomainId();
		this.siteProfileId = bean.getSiteProfileId();
		this.siteCurrencyId = bean.getSiteCurrencyId();
		this.siteProfileClassDefault = bean.isSiteProfileClassDefault();
	}
	public SiteDomain getSiteDomain() throws Exception {
		if (siteDomain != null) {
			return siteDomain;
		}
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		siteDomain = (SiteDomain) em.find(SiteDomain.class, siteDomainId);
		return siteDomain;
	}
	public void setSiteDomain(SiteDomain siteDomain) {
		this.siteDomain = siteDomain;
	}
	public SiteProfile getSiteProfile() throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		siteProfile = (SiteProfile) em.find(SiteProfile.class, siteProfileId);
		return siteProfile;
	}
	public void setSiteProfile(SiteProfile siteProfile) {
		this.siteProfile = siteProfile;
	}
	public SiteCurrency getSiteCurrency() throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		siteCurrency = (SiteCurrency) em.find(SiteCurrency.class, siteCurrencyId);
		return siteCurrency;
	}
	public void setSiteCurrency(SiteCurrency siteCurrency) {
		this.siteCurrency = siteCurrency;
	}
	public Long getSiteDomainId() {
		return siteDomainId;
	}
	public void setSiteDomainId(Long siteDomainId) {
		this.siteDomainId = siteDomainId;
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
	public boolean isSiteProfileClassDefault() {
		return siteProfileClassDefault;
	}
	public void setSiteProfileClassDefault(boolean siteProfileClassDefault) {
		this.siteProfileClassDefault = siteProfileClassDefault;
	}
}
