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

package com.jada.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.dao.SiteDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.User;

public class SiteCache {
    Logger logger = Logger.getLogger(SiteCache.class);
    	/*
	 * This hashtable can be problematic in load balancing. 
	 * Read data from table once load balance is required.
	 */
	static Vector<Site> cache = new Vector<Site>();
	
	static public Site getSiteById(String siteId) throws Exception {
		Enumeration<?> e = cache.elements();
		Site site = null;
		while (e.hasMoreElements()) {
			site = (Site) e.nextElement();
			if (site.getSiteId().equals(siteId)) {
				return site;
			}
		}
        site = SiteDAO.load(siteId);
        if (site != null) {
        	SiteDAO.initialize(site);
        	cache.add(site);
        }
        return site;
	}
	
	static public Site getDefaultSite(User user) throws Exception {
		if (user.getUserType().equals(Constants.USERTYPE_ADMIN) || user.getUserType().equals(Constants.USERTYPE_SUPER)) {
	        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
	        Query query = em.createQuery("from Site");
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
	
	static public Site getSite(HttpServletRequest request) throws Exception {
/*
		String serverName = request.getServerName();
		String serverPort = Integer.toString(request.getServerPort());
		boolean secure = request.isSecure();
		
		Enumeration<?> e = cache.elements();
		String port = "";
		while (e.hasMoreElements()) {
			Site site = (Site) e.nextElement();
			Iterator<?> iterator = site.getDomains().iterator();
			SiteDomain siteDomain = null;
			String siteDomainName = "";
			if (iterator.hasNext()) {
				siteDomain = (SiteDomain) iterator.next();
			}
			if (siteDomain != null) {
				siteDomainName = siteDomain.getSiteDomainName();
			}
			if (secure) {
				port = Constants.PORTNUM_SECURE;
				if (siteDomain != null && !Format.isNullOrEmpty(siteDomain.getSiteSecurePortNum())) {
					port = siteDomain.getSiteSecurePortNum();
				}
				if (siteDomainName.equals(serverName) && port.equals(serverPort)) {
					return site;
				}
			}
			else {
				port = Constants.PORTNUM_PUBLIC;
				if (siteDomain != null && !Format.isNullOrEmpty(siteDomain.getSitePublicPortNum())) {
					port = siteDomain.getSitePublicPortNum();
				}
				if (siteDomainName.equals(serverName) && port.equals(serverPort)) {
					return site;
				}
			}
		}
		
		Site site = null;
		boolean found = false;
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        String sql = "";
        sql = "from Site";
        Query query = em.createQuery(sql);
        Iterator<?> iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	if (found) {
        		break;
        	}
        	site = (Site) iterator.next();
        	Iterator<?> domains = site.getDomains().iterator();
        	while (domains.hasNext()) {
        		SiteDomain siteDomain = (SiteDomain) domains.next();
				if (secure) {
					port = Constants.PORTNUM_SECURE;
					if (!Format.isNullOrEmpty(siteDomain.getSiteSecurePortNum())) {
						port = siteDomain.getSiteSecurePortNum();
					}
					if (siteDomain.getSiteDomainName().equals(serverName) && port.equals(serverPort)) {
						found = true;
						break;
					}
				}
				else {
					port = Constants.PORTNUM_PUBLIC;
					if (!Format.isNullOrEmpty(siteDomain.getSitePublicPortNum())) {
						port = siteDomain.getSitePublicPortNum();
					}
					Logger.getLogger(SiteCache.class).info("DomainName = " + siteDomain.getSiteDomainName() + " , port = " + serverPort);
					if (siteDomain.getSiteDomainName().equals(serverName) && port.equals(serverPort)) {
						found = true;
						break;
					}
				}
        	}
        }
        if (found) {
        	SiteDAO.initialize(site);
        	cache.add(site);
        	return site;
        }
*/
        return null;
	}
	
	static public void removeSite(String siteId) {
		Enumeration<?> e = cache.elements();
		while (e.hasMoreElements()) {
			Site site = (Site) e.nextElement();
			if (site.getSiteId().equals(siteId)) {
				cache.remove(site);
			}
		}
		cache.remove(siteId);
	}
}
