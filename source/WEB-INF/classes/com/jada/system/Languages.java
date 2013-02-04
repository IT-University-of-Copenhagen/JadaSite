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

package com.jada.system;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

import com.jada.admin.language.LanguageMaintAction;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Language;
import com.jada.jpa.entity.LanguageTranslation;
import com.jada.util.Constants;

public class Languages {
	private static Properties englishValues = null;
	private static Hashtable<Long, Language> table = null;
    static Logger logger = Logger.getLogger(LanguageMaintAction.class);
	
	public static synchronized void init() throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		
		Hashtable<Long, Language> langTable = new Hashtable<Long, Language>();
		Vector<Language> languageVector = new Vector<Language>();
		Query query = em.createQuery("from Language");
		for (Iterator<?> iterator = query.getResultList().iterator(); iterator.hasNext();) {
			Language language = (Language) iterator.next();
			language.getTranslations().size();
			languageVector.add(language);
			langTable.put(language.getLangId(), language);
		}
		Language languages[] = new Language[languageVector.size()];
		languageVector.copyInto(languages);

		LabelValueBean beans[] = loadKeyFromProperties();
		englishValues = new Properties();
		for (LabelValueBean bean : beans) {
			englishValues.put(bean.getValue(), bean.getLabel());
		}

		if (!ApplicationGlobal.isLocalTesting()) {
			for (LabelValueBean bean : beans) {
				for (Language language : languages) {
					String sql = "select count(languageTranslation) " +
								 "from   LanguageTranslation languageTranslation " +
								 "where  languageTranslation.language.langId = :langId " +
								 "and    langTranKey = :langTranKey ";
					query = em.createQuery(sql);
					query.setParameter("langId", language.getLangId());
					query.setParameter("langTranKey", bean.getLabel());
					Long count = (Long) query.getSingleResult();
					if (count == 0) {
						logger.info("Adding new language key - " + bean.getLabel());
						LanguageTranslation languageTranslation = new LanguageTranslation();
						languageTranslation.setLangSource(Constants.LANGUAGETRANSLATION_SOURCE_SYSTEM);
						languageTranslation.setLangTranKey(bean.getLabel());
						languageTranslation.setLangTranValue(bean.getValue());
						languageTranslation.setRecCreateBy(Constants.USERNAME_SYSTEM);
						languageTranslation.setRecCreateDatetime(new Date());
						languageTranslation.setRecUpdateBy(Constants.USERNAME_SYSTEM);
						languageTranslation.setRecUpdateDatetime(new Date());
						languageTranslation.setLanguage(language);
						language.getTranslations().add(languageTranslation);
						em.persist(languageTranslation);
					}
				}
			}
		}
		table = langTable;
	}
	
	public static String getLangTranValue(Long langId, String langTranKey) {
		Language language = (Language) table.get(langId);
		Iterator<?> iterator = language.getTranslations().iterator();
		while (iterator.hasNext()) {
			LanguageTranslation translation = (LanguageTranslation) iterator.next();
			if (translation.getLangTranKey().equals(langTranKey)) {
				return translation.getLangTranValue();
			}
		}
		return null;
	}
	
	public static String getLangTranValue(Long langId, String langTranKey, String value0) {
		String result = getLangTranValue(langId, langTranKey);
		return result.replace("{0}", value0);
	}
	
	public static String getLangTranValueByEnglishValue(Long langId, String langTranValue) throws Exception {
		Language language = (Language) table.get(langId);
		String langTranKey = (String) englishValues.get(langTranValue);
		Iterator<?> iterator = language.getTranslations().iterator();
		
		iterator = language.getTranslations().iterator();
		while (iterator.hasNext()) {
			LanguageTranslation translation = (LanguageTranslation) iterator.next();
			if (translation.getLangTranKey().equals(langTranKey)) {
				return translation.getLangTranValue();
			}
		}
		return null;
	}
	
	public static String getLangTranValueByEnglishValue(Long langId, String langTranValue, String value0) throws Exception {
		String result =  getLangTranValueByEnglishValue(langId, langTranValue);
		return result.replace("{0}", value0);
	}
	
	static public LabelValueBean[] loadKeyFromProperties() throws Exception {
		Vector<LabelValueBean> vector = new Vector<LabelValueBean>();
    	InputStream inputStream = Languages.class.getResourceAsStream("/application.properties");
    	BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    	while (true) {
    		String line = reader.readLine();
    		if (line == null) {
    			break;
    		}
    		if (line.startsWith("content.")) {
    			String label = "";
    			String value = "";
    			int pos = line.indexOf("=");
    			if (pos > 0) {
    				label = line.substring(0, pos);
    				value = line.substring(pos + 1);
    				
    				Iterator<?> iterator = vector.iterator();
    				boolean exist = false;
    				while (iterator.hasNext()) {
    					LabelValueBean b = (LabelValueBean) iterator.next();
    					if (b.getLabel().equals(label)) {
    						logger.info("Duplicate language key ignored - " + label);
    						exist = true;
    						break;
    					}
    				}
    				if (exist) {
    					continue;
    				}
    				
    	    		LabelValueBean bean = new LabelValueBean(label, value);
	    			vector.add(bean);
    			}
    		}
    	}
		LabelValueBean beans[] = new LabelValueBean[vector.size()];
		vector.copyInto(beans);
		return beans;
	}
}
