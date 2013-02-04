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
import com.jada.jpa.entity.Language;

public class LanguageDAO extends Language {
	private static final long serialVersionUID = 8230455424732766612L;

	public static Language load(Long langId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Language language = (Language) em.find(Language.class, langId);
		return language;
	}
	
	public static Language loadByLanguageName(String langName) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from Language where langName = :langName");
    	query.setParameter("langName", langName);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		return (Language) iterator.next();
    	}
    	return null;
	}
}
