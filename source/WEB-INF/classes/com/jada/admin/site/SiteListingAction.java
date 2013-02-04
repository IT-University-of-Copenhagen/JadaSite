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

import com.jada.admin.AdminBean;
import com.jada.admin.AdminListingAction;
import com.jada.admin.AdminListingActionForm;
import com.jada.dao.SiteDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.User;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

import javax.persistence.Query;

import java.util.Iterator;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class SiteListingAction
    extends AdminListingAction {
	
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        SiteListingActionForm form = (SiteListingActionForm) actionForm;
        form.setSites(null);
    	if (form.getSrActive() == null) {
    		form.setSrActive("*");
    	}
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

	public void extract(AdminListingActionForm actionForm, HttpServletRequest request) throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        SiteListingActionForm form = (SiteListingActionForm) actionForm;

        Query query = null;
        String sql = "select	site " + 
        			 "from 		Site site " + 
        			 "where 	siteId != '_system' ";
        if (!Format.isNullOrEmpty(form.getSrSiteId())) {
        	sql += "and siteId like :siteId ";
        }
        if (!Format.isNullOrEmpty(form.getSrSiteDesc())) {
        	sql += "and siteDesc like :siteDesc ";
        }
        if (!form.getSrActive().equals("*")) {
        	sql += "and active = :active ";
        }
        query = em.createQuery(sql);
        if (!Format.isNullOrEmpty(form.getSrSiteId())) {
        	query.setParameter("siteId", "%" + form.getSrSiteId() + "%");
        }
        if (!Format.isNullOrEmpty(form.getSrSiteDesc())) {
        	query.setParameter("siteDesc", "%" + form.getSrSiteDesc() + "%");
        }
        if (!form.getSrActive().equals("*")) {
        	query.setParameter("active", form.getSrActive());
        }
        Iterator<?> iterator = query.getResultList().iterator();
        Vector<SiteDisplayForm> vector = new Vector<SiteDisplayForm>();
        while (iterator.hasNext()) {
        	Site site = (Site) iterator.next();
    		SiteDisplayForm siteDisplay = new SiteDisplayForm();
    		siteDisplay.setSiteId(site.getSiteId());
    		siteDisplay.setSiteDesc(site.getSiteDesc());
    		siteDisplay.setActive(String.valueOf(site.getActive()));
    		siteDisplay.setSystemRecord(String.valueOf(site.getSystemRecord()));
    		vector.add(siteDisplay);
        }
        form.setSites(vector);
    }
    
    public ActionForward cancel(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        SiteListingActionForm form = (SiteListingActionForm) actionForm;
        if (form.getSites() != null) {
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
        SiteListingActionForm form = (SiteListingActionForm) actionForm;
        String siteIds[] = form.getSiteIds();
        User user = getAdminBean(request).getUser();
        
        try {
	        if (siteIds != null) {
		        for (int i = 0; i < siteIds.length; i++) {
		        	Site site = SiteDAO.load(siteIds[i], user);
		        	SiteDAO.remove(site, user);
		        }
		        em.getTransaction().commit();
		        em = JpaConnection.getInstance().getCurrentEntityManager();
		        em.getTransaction().begin();
	        }
        }
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.sites.constraint"));
				saveMessages(request, errors);
		        ActionForward forward = actionMapping.findForward("removeError");
		        em.getTransaction().rollback();
		        return forward;
			}
			throw e;
        }
        
        AdminBean adminBean = getAdminBean(request);
        if (adminBean.getSite() == null) {
        	Iterator<?> iterator = null;
        	if (user.getUserType().equals(Constants.USERTYPE_SUPER) || user.getUserType().equals(Constants.USERTYPE_ADMIN)) {
    	     	String sql = "from Site where siteId not like '\\_%' order by siteId";
    	     	Query query = em.createQuery(sql);
    	     	iterator = query.getResultList().iterator();
        	}
        	else {
    	    	iterator = user.getSites().iterator();
        	}
        	Site site = (Site) iterator.next();
        	adminBean.setSiteId(site.getSiteId());
        	user.setUserLastVisitSiteId(site.getSiteId());
        	// em.update(user);
        }

        ActionForward forward = new ActionForward();
        forward = new ActionForward(request.getServletPath() + "?process=list", true);
        return forward;
    }
    
	public void initForm(AdminListingActionForm actionForm) {
		SiteListingActionForm form = (SiteListingActionForm) actionForm;
		form.setSites(null);
		form.setSrActive("*");
	}
	
	public void initSearchInfo(AdminListingActionForm actionForm, String siteId) throws Exception {
		SiteListingActionForm form = (SiteListingActionForm) actionForm;
		form.setSites(null);
		form.setSrActive("*");
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