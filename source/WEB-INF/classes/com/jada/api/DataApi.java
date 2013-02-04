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

package com.jada.api;

import java.util.Iterator;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.dao.ContentDAO;
import com.jada.dao.ItemDAO;
import com.jada.dao.CategoryDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Category;
import com.jada.jpa.entity.Content;
import com.jada.jpa.entity.Item;
import com.jada.system.ApplicationGlobal;
/*
 * TODO Remove this file and replace with the DAO classes
 */
public class DataApi {
	static public DataApi getInstance() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String classname = ApplicationGlobal.getDataApiClassName();
		return (DataApi) Class.forName(classname).newInstance();
	}
	
	public Item getItem(String siteId, Long itemId) throws Exception {
    	Item item = ItemDAO.load(siteId, itemId);
    	return item;
	}
	public Item getItem(String siteId, String itemNaturalKey) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from Item where siteId = :siteId and itemNaturalKey = :itemNaturalKey");
    	query.setParameter("siteId", siteId);
    	query.setParameter("itemNaturalKey", itemNaturalKey);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		Item item = (Item) iterator.next();
    		return item;
    	}
    	return null;
	}
	
	public Content getContent(String siteId, Long contentId) throws Exception {
    	Content content = ContentDAO.load(siteId, contentId);
    	return content;
	}
	public Content getContent(String siteId, String contentNaturalKey) throws Exception {
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
	
	public Category getCategory(String siteId, Long catId) throws Exception {
    	Category category = CategoryDAO.load(siteId, catId);
    	return category;
	}
	public Category getCategory(String siteId, String catNaturalKey) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from Category where siteId = :siteId and catNaturalKey = :catNaturalKey");
    	query.setParameter("siteId", siteId);
    	query.setParameter("catNaturalKey", catNaturalKey);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		Category category = (Category) iterator.next();
    		return category;
    	}
    	return null;
	}

}
