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

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Syndication;
import com.jada.util.Constants;

public class SyndicationDAO extends Syndication {
	private static final long serialVersionUID = -4489010877809947109L;
	public static Syndication load(String siteId, Long syndicationId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Syndication syndication = (Syndication) em.find(Syndication.class, syndicationId);
		if (!syndication.getSiteId().equals(siteId)) {
			throw new SecurityException();
		}
		return syndication;
	}
	public static boolean hasSyndication(String siteId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        Query query = em.createQuery("from Syndication syndication where siteId = :siteId and active = :active order by seqNum");
		query.setParameter("siteId", siteId);
		query.setParameter("active", String.valueOf(Constants.VALUE_YES));
		Iterator<?> iterator = query.getResultList().iterator();
		if (iterator.hasNext()) {
			return true;
		}
		return false;
	}
}
