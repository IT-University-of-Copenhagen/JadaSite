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

package com.jada.admin.language;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.CacheDAO;
import com.jada.dao.LanguageDAO;
import com.jada.dao.LanguageTranslationDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Language;
import com.jada.jpa.entity.LanguageTranslation;
import com.jada.jpa.entity.Site;
import com.jada.system.Languages;
import com.jada.translator.BingTranslate;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

public class LanguageMaintAction
    extends AdminLookupDispatchAction {
    Logger logger = Logger.getLogger(LanguageMaintAction.class);
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse)
        throws Throwable {

        LanguageMaintActionForm form = (LanguageMaintActionForm) actionForm;
        if (form == null) {
            form = new LanguageMaintActionForm();
        }
        form.setMode("C");
        form.setSystemRecord(String.valueOf(Constants.VALUE_NO));
		initSearchInfo(form, getAdminBean(httpServletRequest).getSite());
        FormUtils.setFormDisplayMode(httpServletRequest, form, FormUtils.CREATE_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

        LanguageMaintActionForm form = (LanguageMaintActionForm) actionForm;
        if (form == null) {
            form = new LanguageMaintActionForm();
        }
 		String langId = request.getParameter("langId");
        Language language = new Language();
        language = LanguageDAO.load(Format.getLong(langId));
        form.setMode("U");
		form.setLangId(Format.getLong(language.getLangId()));
		form.setLangName(language.getLangName());
		form.setSystemRecord(String.valueOf(language.getSystemRecord()));
		form.setGoogleTranslateLocale(language.getGoogleTranslateLocale());
		String localeString = language.getLangLocaleLanguage();
		if (!Format.isNullOrEmpty(language.getLangLocaleCountry())) {
			localeString += "-" + language.getLangLocaleCountry();
		}
		form.setLocale(localeString);
		LabelValueBean beans[] = loadKeyFromProperties();
		Vector<LangTranDetailForm> vector = new Vector<LangTranDetailForm>();
		for (int i = 0; i < beans.length; i++) {
			LangTranDetailForm detail = new LangTranDetailForm();
			detail.setLangTranKey(beans[i].getLabel());
			detail.setLangTranEnglish(beans[i].getValue());
        	LanguageTranslation langTran = LanguageTranslationDAO.loadByKey(Format.getLong(langId), beans[i].getLabel());
        	if (langTran != null) {
        		detail.setLangTranValue(langTran.getLangTranValue());
        	}
        	else {
        		detail.setLangTranValue(beans[i].getValue());
        	}
			vector.add(detail);
		}
		LangTranDetailForm langTrans[] = new LangTranDetailForm[vector.size()];
		vector.copyInto(langTrans);
		form.setLangTrans(langTrans);
		initSearchInfo(form, getAdminBean(request).getSite());
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward remove(ActionMapping mapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		LanguageMaintActionForm form = (LanguageMaintActionForm) actionForm;
		Language language = LanguageDAO.load(Format.getLong(form.getLangId()));
		if (language.getSystemRecord() != Constants.VALUE_YES) {
			try {
				em.remove(language);
				em.getTransaction().commit();
			}
			catch (Exception e) {
				if (Utility.isConstraintViolation(e)) {
					ActionMessages errors = new ActionMessages();
					errors.add("error", new ActionMessage("error.remove.language.constraint"));
					saveMessages(request, errors);
					initSearchInfo(form, getAdminBean(request).getSite());
					return mapping.findForward("error");
				}
				throw e;
			}
		}
		
		ActionForward actionForward = mapping.findForward("removeSuccess");
		return actionForward;
	}

	public ActionForward translate(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
		LanguageMaintActionForm form = (LanguageMaintActionForm) actionForm;
		Site site = getAdminBean(request).getSite();

		if (Format.isNullOrEmpty(form.getGoogleTranslateLocale())) {
			ActionMessages errors = new ActionMessages();
			errors.add("googleTranslateLocale", new ActionMessage("error.string.required"));
			saveMessages(request, errors);
			initSearchInfo(form, site);
			return mapping.findForward("error");
		}
		
		BingTranslate translator = new BingTranslate("en", form.getGoogleTranslateLocale(), site);
		Vector<String> vector = new Vector<String>();
		for (LangTranDetailForm detailForm : form.getLangTrans()) {
			vector.add(detailForm.getLangTranEnglish());
		}
		String sources[] = new String[vector.size()];
		vector.copyInto(sources);
		
		String results[] = translator.translateAll(sources);
		
		MessageResources resources = this.getResources(request);
		for (int i = 0; i < form.getLangTrans().length; i++) {
        	LangTranDetailForm langTranDetailForm = form.getLangTrans()[i];
        	langTranDetailForm.setLangTranValue(results[i]);
        	if (results[i].length() > 255) {
        		langTranDetailForm.setLangTranValueError(resources.getMessage("error.value.tooLong"));
        	}
		}
		initSearchInfo(form, site);
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	public ActionForward save(ActionMapping mapping, 
							  ActionForm actionForm,
							  HttpServletRequest request,
							  HttpServletResponse response) 
		throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        Site site = getAdminBean(request).getSite();
		boolean insertMode = false;
		LanguageMaintActionForm form = (LanguageMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}

		AdminBean adminBean = getAdminBean(request);
		Language language = new Language();
		if (!insertMode) {
			language = LanguageDAO.load(Format.getLong(form.getLangId()));
		}

		ActionMessages errors = validate(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			initSearchInfo(form, site);
			return mapping.findForward("error");
		}

		String localeTokens[] = form.getLocale().split("-");
		language.setLangLocaleLanguage(localeTokens[0]);
		language.setLangLocaleCountry("");
		if (localeTokens.length > 1) {
			language.setLangLocaleCountry(localeTokens[1]);
		}
		language.setLangName(form.getLangName());
		language.setGoogleTranslateLocale(form.getGoogleTranslateLocale());
		language.setRecUpdateBy(adminBean.getUser().getUserId());
		language.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		if (insertMode) {
			language.setSystemRecord(Constants.VALUE_NO);
			language.setRecCreateBy(adminBean.getUser().getUserId());
			language.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			em.persist(language);
		}

        form.setLangId(Format.getLong(language.getLangId()));
		form.setMode("U");
		
		if (insertMode) {
			LabelValueBean beans[] = loadKeyFromProperties();
			Vector<LangTranDetailForm> vector = new Vector<LangTranDetailForm>();
			for (int i = 0; i < beans.length; i++) {
				LanguageTranslation langTran = new LanguageTranslation();
        		langTran.setLangTranKey(beans[i].getLabel());
        		langTran.setLangTranValue("");
        		langTran.setLangSource(Constants.LANGUAGETRANSLATION_SOURCE_SYSTEM);
        		langTran.setRecCreateBy(adminBean.getUser().getUserId());
        		langTran.setRecCreateDatetime(new Date(System.currentTimeMillis()));
	    		langTran.setRecUpdateBy(adminBean.getUser().getUserId());
	    		langTran.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
	    		langTran.setLanguage(language);
	    		em.persist(langTran);
	    		
	    		LangTranDetailForm detail = new LangTranDetailForm();
				detail.setLangTranKey(beans[i].getLabel());
				detail.setLangTranEnglish(beans[i].getValue());
				detail.setLangTranValue(beans[i].getValue());
				vector.add(detail);
			}
			LangTranDetailForm langTrans[] = new LangTranDetailForm[vector.size()];
			vector.copyInto(langTrans);
			form.setLangTrans(langTrans);
		}
		else {
			String sql = "delete  " +
						 "from    LanguageTranslation languageTranslation " +
						 "where   languageTranslation.language.langId = :langId";
			Query query = em.createQuery(sql);
			query.setParameter("langId", language.getLangId());
			query.executeUpdate();
			
			for (int i = 0; i < form.getLangTrans().length; i++) {
	        	LangTranDetailForm langTranDetailForm = form.getLangTrans()[i];
	        	LanguageTranslation langTran = new LanguageTranslation();
        		langTran.setLangSource(Constants.LANGUAGETRANSLATION_SOURCE_SYSTEM);
        		langTran.setLangTranKey(langTranDetailForm.getLangTranKey());
        		langTran.setRecCreateBy(adminBean.getUser().getUserId());
        		langTran.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        		langTran.setLanguage(language);

	         	if (langTranDetailForm.getLangTranValue().length() > 255) {
	         		langTranDetailForm.setLangTranValue(langTranDetailForm.getLangTranValue().substring(0, 255 - 1));
	         	}
	        	langTran.setLangTranValue(langTranDetailForm.getLangTranValue());
	    		langTran.setRecUpdateBy(adminBean.getUser().getUserId());
	    		langTran.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
	    		em.persist(langTran);
			}
		}
		Languages.init();
		CacheDAO.removeAll();
		initSearchInfo(form, site);
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	public LabelValueBean[] loadKeyFromProperties() throws Exception {
		Vector<LabelValueBean> vector = new Vector<LabelValueBean>();
    	InputStream inputStream = getClass().getResourceAsStream("/application.properties");
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
	
	public void initSearchInfo(LanguageMaintActionForm form, Site site) throws SecurityException, Exception {
		Locale locales[] = Locale.getAvailableLocales();
		Arrays.sort(locales, new LocaleComparator());
		LabelValueBean beans[] = new LabelValueBean[locales.length];
		for (int i = 0; i < locales.length; i++) {
			String value = locales[i].getLanguage();
			if (!Format.isNullOrEmpty(locales[i].getCountry())) {
				value += "-" + locales[i].getCountry();
			}
			beans[i] = new LabelValueBean(locales[i].getDisplayName(), value);
		}
		form.setLocales(beans);
		
		BingTranslate translate = new BingTranslate(site);
		form.setLanguages(translate.getLanguages());
	}
    
    public ActionMessages validate(LanguageMaintActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	
    	if (Format.isNullOrEmpty(form.getLangName())) {
    		errors.add("langName", new ActionMessage("error.string.required"));
    	}
    	return errors;
    }
	
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("save", "save");
        map.put("edit", "edit");
        map.put("create", "create");
        map.put("remove", "remove");
        map.put("translate", "translate");
        return map;
    }
    
    class LocaleComparator implements Comparator<Locale> {
		public int compare(Locale arg0, Locale arg1) {
			Locale locale0 = (Locale) arg0;
			Locale locale1 = (Locale) arg1;
			return locale0.getDisplayName().compareTo(locale1.getDisplayName());
		}
    	
    }
}
