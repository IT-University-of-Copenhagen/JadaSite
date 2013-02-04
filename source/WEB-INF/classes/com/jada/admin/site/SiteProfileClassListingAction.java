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
import com.jada.dao.SiteProfileClassDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.CategorySearchUtil;
import com.jada.util.Utility;

import javax.persistence.Query;

import java.util.Iterator;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class SiteProfileClassListingAction
    extends AdminListingAction {
	
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        SiteProfileClassListingActionForm form = (SiteProfileClassListingActionForm) actionForm;
        form.setSiteProfileClasses(null);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

	public void extract(AdminListingActionForm actionForm, HttpServletRequest request) throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        SiteProfileClassListingActionForm form = (SiteProfileClassListingActionForm) actionForm;
        Site site = getAdminBean(request).getSite();

        Query query = null;
        String sql = "select	siteProfileClass " + 
        			 "from 		SiteProfileClass siteProfileClass " + 
        			 "where 	siteProfileClass.site.siteId = :siteId ";
        if (!Format.isNullOrEmpty(form.getSrSiteProfileClassName())) {
        	sql += "and siteProfileClassName like :siteProfileClassName ";
        }
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        if (!Format.isNullOrEmpty(form.getSrSiteProfileClassName())) {
        	query.setParameter("siteProfileClassName", "%" + form.getSrSiteProfileClassName() + "%");
        }
        Iterator<?> iterator = query.getResultList().iterator();
        Vector<SiteProfileClassDisplayForm> vector = new Vector<SiteProfileClassDisplayForm>();
        while (iterator.hasNext()) {
        	SiteProfileClass siteProfileClass = (SiteProfileClass) iterator.next();
    		SiteProfileClassDisplayForm siteProfileClassDisplay = new SiteProfileClassDisplayForm();
    		siteProfileClassDisplay.setSiteProfileClassId(siteProfileClass.getSiteProfileClassId().toString());
    		siteProfileClassDisplay.setSiteProfileClassName(siteProfileClass.getSiteProfileClassName());
    		siteProfileClassDisplay.setLangName(siteProfileClass.getLanguage().getLangName());
    		siteProfileClassDisplay.setSystemRecord(String.valueOf(siteProfileClass.getSystemRecord()));
    		vector.add(siteProfileClassDisplay);
        }
        SiteProfileClassDisplayForm siteProfileClasses[] = new SiteProfileClassDisplayForm[vector.size()];
        vector.copyInto(siteProfileClasses);
        form.setSiteProfileClasses(siteProfileClasses);
    }
    
    public ActionForward cancel(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        SiteProfileClassListingActionForm form = (SiteProfileClassListingActionForm) actionForm;
        if (form.getSiteProfileClasses() != null) {
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
        SiteProfileClassListingActionForm form = (SiteProfileClassListingActionForm) actionForm;
        
        try {
        	SiteProfileClassDisplayForm siteProfileClasses[] = form.getSiteProfileClasses();
	        if (siteProfileClasses != null) {
		        for (int i = 0; i < siteProfileClasses.length; i++) {
		        	if (!siteProfileClasses[i].isRemove()) {
		        		continue;
		        	}
		            SiteProfileClass siteProfileClass = new SiteProfileClass();
		            siteProfileClass = SiteProfileClassDAO.load(Format.getLong(siteProfileClasses[i].getSiteProfileClassId()));
		            if (siteProfileClass.getSystemRecord() == Constants.VALUE_YES) {
		            	continue;
		            }
					CategorySearchUtil.removeSiteProfileClass(getAdminBean(request).getSite().getSiteId(), siteProfileClass);
					
		            em.remove(siteProfileClass);
		        }
		        em.getTransaction().commit();
	        }
        }
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.siteProfileClasss.constraint"));
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
		SiteProfileClassListingActionForm form = (SiteProfileClassListingActionForm) actionForm;
		form.setSiteProfileClasses(null);
	}
	
	public void initSearchInfo(AdminListingActionForm actionForm, String siteProfileClassId) throws Exception {
		SiteProfileClassListingActionForm form = (SiteProfileClassListingActionForm) actionForm;
		form.setSiteProfileClasses(null);
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