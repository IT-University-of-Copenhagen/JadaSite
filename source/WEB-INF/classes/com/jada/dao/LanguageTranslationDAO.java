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
import com.jada.jpa.entity.LanguageTranslation;

public class LanguageTranslationDAO extends LanguageTranslation {
	private static final long serialVersionUID = -372047037929738493L;
	public static LanguageTranslation load(String siteId, Long langId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	LanguageTranslation languageTranslation = (LanguageTranslation) em.find(LanguageTranslation.class, langId);
		return languageTranslation;
	}
	public static LanguageTranslation loadByKey(Long langId, String langTranKey) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String sql = "select translation " +
    				 "from   LanguageTranslation translation " +
    				 "inner  join translation.language language " +
    				 "where  language.langId = :langId " +
    				 "and    translation.langTranKey = :langTranKey ";
    	Query query = em.createQuery(sql);
    	query.setParameter("langId", langId);
    	query.setParameter("langTranKey", langTranKey);
    	Iterator<?> iterator = query.getResultList().iterator();
    	if (iterator.hasNext()) {
    		LanguageTranslation languageTranslation = (LanguageTranslation) iterator.next();
    		return languageTranslation;
    	}
    	return null;
	}

}
