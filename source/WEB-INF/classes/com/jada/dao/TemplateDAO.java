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
import com.jada.jpa.entity.Template;

public class TemplateDAO extends Template {
	private static final long serialVersionUID = -1198725498355587055L;

	public static Template load(String siteId, Long templateId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Template template = (Template) em.find(Template.class, templateId);
		if (!template.getSiteId().equals(siteId)) {
			throw new SecurityException();
		}
		return template;
	}
	
    public static Template load(String siteId, String templateName) throws Exception
	{
	    EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
	    String sql = "from Template where siteId = :siteId and templateName = :templateName";
	    Query query = em.createQuery(sql);
	    query.setParameter("siteId", siteId);
	    query.setParameter("templateName", templateName);
	    Iterator<?> iterator = query.getResultList().iterator();
	    if(iterator == null)
	        return null;
	    if(!iterator.hasNext())
	        return null;
	    else
	        return (Template)iterator.next();
	}
}
