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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;

import com.jada.admin.AdminListingAction;
import com.jada.admin.AdminListingActionForm;
import com.jada.dao.SiteCurrencyClassDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteCurrencyClass;
import com.jada.util.Format;
import com.jada.util.CategorySearchUtil;
import com.jada.util.Utility;

import javax.persistence.Query;

import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class SiteCurrencyClassListingAction
    extends AdminListingAction {
	
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        SiteCurrencyClassListingActionForm form = (SiteCurrencyClassListingActionForm) actionForm;
        form.setSiteCurrencyClasses(null);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

	public void extract(AdminListingActionForm actionForm, HttpServletRequest request) throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        SiteCurrencyClassListingActionForm form = (SiteCurrencyClassListingActionForm) actionForm;
        Site site = getAdminBean(request).getSite();

        Query query = null;
        String sql = "select	siteCurrencyClass " + 
        			 "from 		SiteCurrencyClass siteCurrencyClass " + 
        			 "where 	siteCurrencyClass.site.siteId = :siteId ";
        if (!Format.isNullOrEmpty(form.getSrSiteCurrencyClassName())) {
        	sql += "and siteCurrencyClassName like :siteCurrencyClassName ";
        }
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        if (!Format.isNullOrEmpty(form.getSrSiteCurrencyClassName())) {
        	query.setParameter("siteCurrencyClassName", form.getSrSiteCurrencyClassName());
        }
        Iterator<?> iterator = query.getResultList().iterator();
        Vector<SiteCurrencyClassDisplayForm> vector = new Vector<SiteCurrencyClassDisplayForm>();
        while (iterator.hasNext()) {
        	SiteCurrencyClass siteCurrencyClass = (SiteCurrencyClass) iterator.next();
    		SiteCurrencyClassDisplayForm siteCurrencyClassDisplay = new SiteCurrencyClassDisplayForm();
    		siteCurrencyClassDisplay.setSiteCurrencyClassId(siteCurrencyClass.getSiteCurrencyClassId().toString());
    		siteCurrencyClassDisplay.setSiteCurrencyClassName(siteCurrencyClass.getSiteCurrencyClassName());
    		String localeName = getLocaleName(siteCurrencyClass.getCurrencyLocaleLanguage(), siteCurrencyClass.getCurrencyLocaleCountry());
    		siteCurrencyClassDisplay.setLocaleName(localeName);
    		siteCurrencyClassDisplay.setSystemRecord(String.valueOf(siteCurrencyClass.getSystemRecord()));
    		vector.add(siteCurrencyClassDisplay);
        }
        SiteCurrencyClassDisplayForm siteCurrencyClasses[] = new SiteCurrencyClassDisplayForm[vector.size()];
        vector.copyInto(siteCurrencyClasses);
        form.setSiteCurrencyClasses(siteCurrencyClasses);
    }
    
    public ActionForward cancel(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        SiteCurrencyClassListingActionForm form = (SiteCurrencyClassListingActionForm) actionForm;
        if (form.getSiteCurrencyClasses() != null) {
        	return list(actionMapping, actionForm, request, response);
        }
        else {
        	return start(actionMapping, actionForm, request, response);
        }
    }

    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        SiteCurrencyClassListingActionForm form = (SiteCurrencyClassListingActionForm) actionForm;
        
        try {
        	SiteCurrencyClassDisplayForm siteCurrencyClasses[] = form.getSiteCurrencyClasses();
	        if (siteCurrencyClasses != null) {
		        for (int i = 0; i < siteCurrencyClasses.length; i++) {
		        	if (!siteCurrencyClasses[i].isRemove()) {
		        		continue;
		        	}
		            SiteCurrencyClass siteCurrencyClass = new SiteCurrencyClass();
		            siteCurrencyClass = SiteCurrencyClassDAO.load(Format.getLong(siteCurrencyClasses[i].getSiteCurrencyClassId()));
					CategorySearchUtil.removeSiteCurrencyClass(getAdminBean(request).getSite().getSiteId(), siteCurrencyClass);
		            em.remove(siteCurrencyClass);
		        }
		        em.getTransaction().commit();
	        }
        }
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.siteCurrencyClasss.constraint"));
				saveMessages(request, errors);
		        ActionForward forward = actionMapping.findForward("removeError") ;
		        return forward;
			}
			throw e;
        }

        ActionForward forward = new ActionForward();
        forward = new ActionForward(request.getServletPath() + "?process=list", true);
        return forward;
    }
    
	public void initForm(AdminListingActionForm actionForm) {
		SiteCurrencyClassListingActionForm form = (SiteCurrencyClassListingActionForm) actionForm;
		form.setSiteCurrencyClasses(null);
	}
	
	public void initSearchInfo(AdminListingActionForm actionForm, String siteCurrencyClassId) throws Exception {
		SiteCurrencyClassListingActionForm form = (SiteCurrencyClassListingActionForm) actionForm;
		form.setSiteCurrencyClasses(null);
	}
	
	public String getLocaleName(String localeLanguage, String localeCountry) {
		String value = localeLanguage;
		if (!Format.isNullOrEmpty(localeCountry)) {
			value += "-" + localeCountry;
		}
		Locale locales[] = Locale.getAvailableLocales();
		for (int i = 0; i < locales.length; i++) {
			Locale locale = locales[i];
			String localeValue = locale.getLanguage();
			if (!Format.isNullOrEmpty(locale.getCountry())) {
				localeValue += "-" + locale.getCountry();
			}
			if (value.equals(localeValue)) {
				return locale.getDisplayName();
			}
		}
		return null;
	}

    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("remove", "remove");
        map.put("list", "list");
        map.put("start", "start");
        map.put("cancel", "cancel");
        map.put("back", "back");
        return map;
    }
}