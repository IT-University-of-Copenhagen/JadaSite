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

package com.jada.admin.menu;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;


import javax.persistence.Query;

import java.util.Iterator;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class MenuListingAction
    extends AdminLookupDispatchAction {
	
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        MenuListingActionForm form = (MenuListingActionForm) actionForm;
        form.setSiteDomains(null);
    	if (form.getSrActive() == null) {
    		form.setSrActive("*");
    	}
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward list(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	MenuListingActionForm form = (MenuListingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();

        Query query = null;
        String sql = "from SiteDomain siteDomain where siteDomain.site.siteId = :siteId ";
        if (form.getSrSiteDomainName().length() > 0) {
        	sql += "and siteDomainName like :siteDomainName ";
        }
        if (!form.getSrActive().equals("*")) {
        	sql += "and active = :active ";
        }
        sql += "order by siteDomainName";
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        if (form.getSrSiteDomainName().length() > 0) {
        	query.setParameter("siteDomainName", "%" + form.getSrSiteDomainName() + "%");
        }
        if (!form.getSrActive().equals("*")) {
        	query.setParameter("active", form.getSrActive());
        }
        Iterator<?> iterator = query.getResultList().iterator();
        Vector<SiteDomainDisplayForm> vector = new Vector<SiteDomainDisplayForm>();
        while (iterator.hasNext()) {
        	SiteDomain siteDomain = (SiteDomain) iterator.next();
        	SiteDomainDisplayForm siteDomainDisplay = new SiteDomainDisplayForm();
        	siteDomainDisplay.setSiteDomainId(siteDomain.getSiteDomainId().toString());
        	siteDomainDisplay.setSiteName(siteDomain.getSiteDomainLanguage().getSiteName());
        	siteDomainDisplay.setSiteDomainName(siteDomain.getSiteDomainName());
        	siteDomainDisplay.setActive(String.valueOf(siteDomain.getActive()));
    		vector.add(siteDomainDisplay);
        }
        form.setSiteDomains(vector);
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward back(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	MenuListingActionForm form = (MenuListingActionForm) actionForm;
        if (form.getSiteDomains() != null) {
        	return list(actionMapping, actionForm, request, response);
        }
        else {
        	return start(actionMapping, actionForm, request, response);
        }
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("list", "list");
        map.put("start", "start");
        map.put("back", "back");
        return map;
    }
}