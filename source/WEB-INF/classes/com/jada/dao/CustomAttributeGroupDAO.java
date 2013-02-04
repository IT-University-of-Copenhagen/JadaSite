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
import com.jada.jpa.entity.CustomAttributeGroup;

public class CustomAttributeGroupDAO extends CustomAttributeGroup {
	private static final long serialVersionUID = 2018608223165684363L;

	public static CustomAttributeGroup load(String siteId, Long customAttribGroupId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	CustomAttributeGroup customAttributeGroup = (CustomAttributeGroup) em.find(CustomAttributeGroup.class, customAttribGroupId);
		if (!customAttributeGroup.getSite().getSiteId().equals(siteId)) {
			throw new SecurityException();
		}
		return customAttributeGroup;
	}
	
	public static CustomAttributeGroup loadByName(String siteId, String customAttributeGroupName) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from CustomAttributeGroup where siteId = :siteId and customAttribGroupName = :customAttribGroupName");
    	query.setParameter("siteId", siteId);
    	query.setParameter("customAttribGroupName", customAttributeGroupName);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		CustomAttributeGroup customAttributeGroup = (CustomAttributeGroup) iterator.next();
    		return customAttributeGroup;
    	}
    	return null;
	}
}
