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
import com.jada.jpa.entity.PollHeader;

public class PollHeaderDAO extends PollHeader {
	private static final long serialVersionUID = 8955519930189569086L;
	public static PollHeader load(String siteId, Long pollHeaderId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		PollHeader pollheader = (PollHeader) em.find(PollHeader.class, pollHeaderId);
		if (!pollheader.getSiteId().equals(siteId)) {
			throw new SecurityException();
		}
		return pollheader;
	}
	public static PollHeader getActivePoll(String siteId) throws Exception {
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        String sql = "from 		PollHeader " + 
        			 "where 	siteId = :siteId " + 
        			 "and 		published = 'Y' " + 
        			 "and       curdate() between pollPublishOn and pollExpireOn";
        Query query = em.createQuery(sql);
        query.setParameter("siteId", siteId);
        Iterator<?> iterator = query.getResultList().iterator();
        PollHeader pollHeader = null;
        if (iterator.hasNext()) {
        	pollHeader = (PollHeader) iterator.next();
        }
        return pollHeader;
	}
	public static boolean hasActivePoll(String siteId) throws Exception {
		PollHeader pollHeader = getActivePoll(siteId);
		if (pollHeader != null) {
			return true;
		}
		return false;
	}
}
