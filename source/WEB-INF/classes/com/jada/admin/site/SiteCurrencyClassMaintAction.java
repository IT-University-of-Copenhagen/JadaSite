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

package com.jada.admin.site;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.CurrencyDAO;
import com.jada.dao.SiteCurrencyClassDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Currency;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteCurrencyClass;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.CategorySearchUtil;
import com.jada.util.Utility;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

public class SiteCurrencyClassMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest request,
                             HttpServletResponse response)
        throws Throwable {

		Site site = getAdminBean(request).getSite();
		SiteCurrencyClassMaintActionForm form = (SiteCurrencyClassMaintActionForm) actionForm;
        form.setMode("C");
        form.setLocale("");
        form.setSystemRecord(String.valueOf(Constants.VALUE_NO));
        createAdditionalInfo(form, site.getSiteId());
        FormUtils.setFormDisplayMode(request, form, FormUtils.CREATE_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
 
    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

		Site site = getAdminBean(request).getSite();
        SiteCurrencyClassMaintActionForm form = (SiteCurrencyClassMaintActionForm) actionForm;
        SiteCurrencyClass siteCurrencyClass = SiteCurrencyClassDAO.load(form.getSiteCurrencyClassId());
        form.setMode("U");
        copyProperties(form, siteCurrencyClass);
        createAdditionalInfo(form, site.getSiteId());
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		SiteCurrencyClassMaintActionForm form = (SiteCurrencyClassMaintActionForm) actionForm;
        SiteCurrencyClass siteCurrencyClass = SiteCurrencyClassDAO.load(form.getSiteCurrencyClassId());

		try {
			CategorySearchUtil.removeSiteCurrencyClass(getAdminBean(request).getUserId(), siteCurrencyClass);
			em.remove(siteCurrencyClass);
			em.getTransaction().commit();
		}
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.siteCurrencyClass.constraint"));
				saveMessages(request, errors);
				return actionMapping.findForward("error");
			}
			throw e;
		}
		
		ActionForward actionForward = actionMapping.findForward("removeSuccess");
		return actionForward;
	}

	public ActionForward save(ActionMapping mapping, 
							  ActionForm actionForm,
							  HttpServletRequest request,
							  HttpServletResponse response) 
		throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Site site = getAdminBean(request).getSite();
		boolean insertMode = false;
		SiteCurrencyClassMaintActionForm form = (SiteCurrencyClassMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}
        createAdditionalInfo(form, site.getSiteId());

		AdminBean adminBean = getAdminBean(request);

		SiteCurrencyClass siteCurrencyClass = new SiteCurrencyClass();
		if (!insertMode) {
	        siteCurrencyClass = SiteCurrencyClassDAO.load(form.getSiteCurrencyClassId());
		}

		ActionMessages errors = validate(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return mapping.findForward("error");
		}

		if (insertMode) {
			siteCurrencyClass.setSite(site);
			siteCurrencyClass.setSystemRecord(Constants.VALUE_NO);
			siteCurrencyClass.setRecCreateBy(adminBean.getUser().getUserId());
			siteCurrencyClass.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		siteCurrencyClass.setSiteCurrencyClassName(form.getSiteCurrencyClassName());
		siteCurrencyClass.setRecUpdateBy(adminBean.getUser().getUserId());
		siteCurrencyClass.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		
		String localeTokens[] = form.getLocale().split("-");
		siteCurrencyClass.setCurrencyLocaleLanguage(localeTokens[0]);
		siteCurrencyClass.setCurrencyLocaleCountry("");
		if (localeTokens.length > 1) {
			siteCurrencyClass.setCurrencyLocaleCountry(localeTokens[1]);
		}
		
		Currency currency = CurrencyDAO.load(site.getSiteId(), Format.getLong(form.getCurrencyId()));
		siteCurrencyClass.setCurrency(currency);
		
		if (insertMode) {
			em.persist(siteCurrencyClass);
			em.flush();
			CategorySearchUtil.createSiteCurrencyClass(site.getSiteCurrencyClassDefault(), siteCurrencyClass, site.getSiteId(), adminBean.getUserId());
		}
		else {
			// em.update(siteCurrencyClass);
		}
		form.setMode("U");
		form.setSiteCurrencyClassId(siteCurrencyClass.getSiteCurrencyClassId());
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	private void copyProperties(SiteCurrencyClassMaintActionForm form, SiteCurrencyClass siteCurrencyClass) {
		form.setSiteCurrencyClassId(siteCurrencyClass.getSiteCurrencyClassId());
		form.setSiteCurrencyClassName(siteCurrencyClass.getSiteCurrencyClassName());
		String localeString = siteCurrencyClass.getCurrencyLocaleLanguage();
		if (!Format.isNullOrEmpty(siteCurrencyClass.getCurrencyLocaleCountry())) {
			localeString += "-" + siteCurrencyClass.getCurrencyLocaleCountry();
		}
		form.setLocale(localeString);
		form.setSystemRecord(String.valueOf(siteCurrencyClass.getSystemRecord()));
		form.setCurrencyId(siteCurrencyClass.getCurrency().getCurrencyId().toString());
	}
	
	private void createAdditionalInfo(SiteCurrencyClassMaintActionForm form, String siteId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
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
		
		String sql = "from     Currency currency " +
				 	 "where    currency.site.siteId = :siteId " +
				 	 "order    by currency.currencyCode ";	 
		Query query = em.createQuery(sql);
		query.setParameter("siteId", siteId);
		Iterator<?> iterator = query.getResultList().iterator();
		Vector<LabelValueBean> vector = new Vector<LabelValueBean>();
		while (iterator.hasNext()) {
			Currency currency = (Currency) iterator.next();
			vector.add(new LabelValueBean(currency.getCurrencyCode(), currency.getCurrencyId().toString()));
		}
		LabelValueBean currencies[] = new LabelValueBean[vector.size()];
		vector.copyInto(currencies);
		form.setCurrencies(currencies);
	}

    public ActionMessages validate(SiteCurrencyClassMaintActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	
    	if (Format.isNullOrEmpty(form.getSiteCurrencyClassName())) {
    		errors.add("siteCurrencyClassName", new ActionMessage("error.string.required"));
    	}
    	return errors;
    }
	
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("save", "save");
        map.put("edit", "edit");
        map.put("create", "create");
        map.put("remove", "remove");
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
