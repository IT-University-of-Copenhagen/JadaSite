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

import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteProfileClass;

public class SiteProfileClassDAO extends Site {
	private static final long serialVersionUID = 585449048013896454L;

	public static SiteProfileClass load(Long siteProfileClassId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	SiteProfileClass siteProfileClass = (SiteProfileClass) em.find(SiteProfileClass.class, siteProfileClassId);
		return siteProfileClass;
	}
	
	public static SiteProfileClass loadByName(String siteId, String siteProfileClassName) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from SiteProfileClass siteProfileClass where siteProfileClass.site.siteId = :siteId and siteProfileClassName = :siteProfileClassName");
    	query.setParameter("siteId", siteId);
    	query.setParameter("siteProfileClassName", siteProfileClassName);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		SiteProfileClass siteProfileClass = (SiteProfileClass) iterator.next();
    		return siteProfileClass;
    	}
    	return null;
	}
}
