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
import java.util.Set;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Content;
import com.jada.jpa.entity.ContentImage;
import com.jada.jpa.entity.ContentLanguage;
import com.jada.jpa.entity.Menu;
import com.jada.util.Constants;
import com.jada.util.Utility;

public class ContentDAO extends Content {
	private static final long serialVersionUID = -8910667853869498504L;

	public static Content load(String siteId, Long contentId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Content content = (Content) em.find(Content.class, contentId);
		if (!content.getSiteId().equals(siteId)) {
			throw new SecurityException();
		}
		return content;
	}
	
	public static Content loadNatural(String siteId, String contentNaturalKey) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from Content where siteId = :siteId and contentNaturalKey = :contentNaturalKey");
    	query.setParameter("siteId", siteId);
    	query.setParameter("contentNaturalKey", contentNaturalKey);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		Content content = (Content) iterator.next();
    		return content;
    	}
    	return null;
	}
	
	public static void remove(String siteId, Content content) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Set<ContentLanguage> contentLanguages = content.getContentLanguages();
		em.remove(content);
		for (ContentLanguage contentLanguage : contentLanguages) {
			if (contentLanguage.getImage() != null) {
				em.remove(contentLanguage.getImage());
			}
			for (ContentImage contentImage : contentLanguage.getImages()) {
				em.remove(contentImage);
			}
			em.remove(contentLanguage);
		}
		
		for (Menu menu : content.getMenus()) {
			menu.setContent(null);
			CacheDAO.removeByKeyPrefix(siteId, Constants.CACHE_MENU + "." + menu.getMenuSetName());
		}
	}

    static public boolean isPublished(Content content) {
    	if (content.getPublished() == Constants.PUBLISHED_NO) {
    		return false;
    	}
    	
    	if (!Utility.isDateBetween(content.getContentPublishOn(), content.getContentExpireOn())) {
    		return false;
    	}
    	return true;
    }
}
