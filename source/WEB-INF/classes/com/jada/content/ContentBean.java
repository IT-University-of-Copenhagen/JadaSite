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

import java.util.Iterator;
import java.util.Vector;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.SiteCurrency;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.SiteProfile;
import com.jada.system.ApplicationGlobal;
import com.jada.util.Constants;

import javax.servlet.http.HttpServletRequest;

public class ContentBean {
	String contextPath = null;
	SiteDomain siteDomain;
	ContentSessionKey contentSessionKey;
	Vector<ContentSessionKey> contentSessionKeyList = new Vector<ContentSessionKey>();
	Vector<String> itemCompareList = new Vector<String>();
	Long custId;
	boolean shoppingCartEnabled;
	Formatter formatter = null;
	
	public void init(HttpServletRequest request) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	
		String sql = "from  SiteDomain " +
		 			 "where siteDomainPrefix = :siteDomainPrefix ";
		Query query = em.createQuery(sql);
		query.setParameter("siteDomainPrefix", ContentLookupDispatchAction.getSiteDomainPrefix(request));
		
		Iterator<?> iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			siteDomain = (SiteDomain) iterator.next();
			break;
		}
		if (siteDomain == null) {
			throw new ContentSiteNotFoundException();
		}
		ContentSessionKey lastContentSessionKey = ContentLookupDispatchAction.getContentSessionKey(siteDomain.getSiteDomainId(), request);
		boolean singleCheckout = siteDomain.getSite().getSingleCheckout() == Constants.VALUE_YES;
		
		String currentSiteProfileClassName = ContentLookupDispatchAction.getSiteProfileClassName(request);
		String value = request.getParameter("langName");
		if (value != null) {
			currentSiteProfileClassName = value;
		}
		if (currentSiteProfileClassName == null) {
			if (lastContentSessionKey != null) {
				currentSiteProfileClassName = lastContentSessionKey.getSiteProfileClassName();
			}
			else {
				currentSiteProfileClassName = siteDomain.getSite().getSiteProfileClassDefault().getSiteProfileClassName();
			}
		}
		
		String currentSiteCurrencyClassName = ContentLookupDispatchAction.getSiteCurrencyClassName(request);
		value = request.getParameter("currencyCode");
		if (value != null) {
			currentSiteCurrencyClassName = value;
		}
		if (currentSiteCurrencyClassName == null) {
			if (lastContentSessionKey != null) {
				currentSiteCurrencyClassName = lastContentSessionKey.getSiteCurrencyClassName();
			}
			else {
				currentSiteCurrencyClassName = siteDomain.getSite().getSiteCurrencyClassDefault().getSiteCurrencyClassName();
			}
		}
		
		SiteDomain effectiveDomain = siteDomain;
		if (singleCheckout) {
			effectiveDomain = siteDomain.getSite().getSiteDomainDefault();
		}
		SiteProfile siteProfile = null;
		for (SiteProfile s : effectiveDomain.getSiteProfiles()) {
			if (s.getSiteProfileClass().getSiteProfileClassName().equals(currentSiteProfileClassName)) {
				siteProfile = s;
				break;
			}
		}
		SiteCurrency siteCurrency = null;
		for (SiteCurrency s : effectiveDomain.getSiteCurrencies()) {
			if (s.getSiteCurrencyClass().getSiteCurrencyClassName().equals(currentSiteCurrencyClassName)) {
				siteCurrency = s;
				break;
			}
		}
		
		/*
		 * Handle case when currency name is not in the url and 
		 * the default currency for the site is not part of the list in the site domain definition.
		 */
		if (siteCurrency == null) {
			for (SiteCurrency s : effectiveDomain.getSiteCurrencies()) {
				siteCurrency = s;
				break;
			}
		}
		
		contentSessionKey = new ContentSessionKey();
		contentSessionKey.setSiteId(siteDomain.getSite().getSiteId());
		contentSessionKey.setSiteDomainId(siteDomain.getSiteDomainId());
		contentSessionKey.setSiteProfileClassDefault(siteProfile.getSiteProfileClass().getSiteProfileClassId().equals(siteDomain.getSite().getSiteProfileClassDefault().getSiteProfileClassId()));
		contentSessionKey.setSiteProfileId(siteProfile.getSiteProfileId());
		contentSessionKey.setSiteProfileClassId(siteProfile.getSiteProfileClass().getSiteProfileClassId());
		contentSessionKey.setSiteProfileClassName(siteProfile.getSiteProfileClass().getSiteProfileClassName());
		contentSessionKey.setLangId(siteProfile.getSiteProfileClass().getLanguage().getLangId());
		contentSessionKey.setLangName(siteProfile.getSiteProfileClass().getSiteProfileClassName());
		contentSessionKey.setSiteCurrencyId(siteCurrency.getSiteCurrencyId());
		contentSessionKey.setSiteCurrencyClassDefault(siteCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteDomain.getSite().getSiteCurrencyClassDefault().getSiteCurrencyClassId()));
		contentSessionKey.setSiteCurrencyClassId(siteCurrency.getSiteCurrencyClass().getSiteCurrencyClassId());
		contentSessionKey.setSiteCurrencyClassName(siteCurrency.getSiteCurrencyClass().getSiteCurrencyClassName());
		ContentLookupDispatchAction.putContentSessionkey(contentSessionKey.getSiteDomainId(), contentSessionKey, request);
		
		shoppingCartEnabled = false;
		if (siteCurrency.getPayPalPaymentGateway() != null) {
			shoppingCartEnabled = true;
		}
		if (siteCurrency.getPaymentGateway() != null) {
			shoppingCartEnabled = true;
		}
		if (siteCurrency.getCashPayment() == Constants.VALUE_YES) {
			shoppingCartEnabled = true;
		}
		formatter = new Formatter(siteProfile, siteCurrency);
		contextPath = ApplicationGlobal.getContextPath();
	}

	public ContentSessionBean getContentSessionBean() {
		ContentSessionBean contentSessionBean = new ContentSessionBean(contentSessionKey);
		return contentSessionBean;
	}

	public ContentSessionKey getContentSessionKey() {
		return contentSessionKey;
	}

	public void setContentSessionKey(ContentSessionKey contentSessionKey) {
		this.contentSessionKey = contentSessionKey;
	}

	public boolean isShoppingCartEnabled() {
		return shoppingCartEnabled;
	}

	public void setShoppingCartEnabled(boolean shoppingCartEnabled) {
		this.shoppingCartEnabled = shoppingCartEnabled;
	}

	public SiteDomain getSiteDomain() {
		return siteDomain;
	}

	public void setSiteDomain(SiteDomain siteDomain) {
		this.siteDomain = siteDomain;
	}
	
	public Formatter getFormatter() {
		return formatter;
	}
	public void setFormatter(Formatter formatter) {
		this.formatter = formatter;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

}
