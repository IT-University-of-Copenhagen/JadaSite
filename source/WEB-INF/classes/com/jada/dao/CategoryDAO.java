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
import com.jada.jpa.entity.Category;
import com.jada.util.Constants;

public class CategoryDAO extends Category {
	private static final long serialVersionUID = 3631233268397121579L;

	public static Category load(String siteId, Long catId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Category category = (Category) em.find(Category.class, catId);
		if (!category.getSite().getSiteId().equals(siteId)) {
			throw new SecurityException();
		}
		return category;
	}

	public static Category loadByShortTitle(String siteId, String catShortTitle) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("select category " + 
    								 "from   Category category " +
    								 "left   join category.categoryLanguage categoryLanguage " +
    								 "where  category.site.siteId = :siteId " +
    								 "and    categoryLanguage.catShortTitle = :catShortTitle");
    	query.setParameter("siteId", siteId);
    	query.setParameter("catShortTitle", catShortTitle);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		Category category = (Category) iterator.next();
    		return category;
    	}
    	return null;
	}
	
    static public boolean isPublished(Category category) {
    	if (category.getPublished() == Constants.PUBLISHED_NO) {
    		return false;
    	}
    	return true;
    }
}
