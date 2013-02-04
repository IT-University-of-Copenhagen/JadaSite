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

package com.jada.admin.tax;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.TaxRegionDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.TaxRegion;
import com.jada.util.Format;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;


import javax.persistence.Query;

import java.util.Iterator;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class TaxRegionListingAction
    extends AdminLookupDispatchAction {
	
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        TaxRegionListingActionForm form = (TaxRegionListingActionForm) actionForm;
        form.setTaxRegions(null);
    	if (form.getSrPublished() == null) {
    		form.setSrPublished("*");
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
        TaxRegionListingActionForm form = (TaxRegionListingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        String siteId = adminBean.getSite().getSiteId();
        
        if (form.getSrTaxRegionDesc() == null) {
        	return start(actionMapping, actionForm, request, response);
        }

        Query query = null;
        String sql = "select taxRegion " + 
        			 "from   TaxRegion taxRegion " +
        			 "left   join taxRegion.site site " +
        			 "where  site.siteId = :siteId ";
        if (form.getSrTaxRegionDesc().length() > 0) {
        	sql += "and taxRegionDesc like :taxRegionDesc ";
        }
        if (!form.getSrPublished().equals("*")) {
        	sql += "and published = :published ";
        }
        sql += "order by taxRegionDesc";
        query = em.createQuery(sql);
        query.setParameter("siteId", siteId);
        if (form.getSrTaxRegionDesc().length() > 0) {
        	query.setParameter("taxRegionDesc", "%" + form.getSrTaxRegionDesc() + "%");
        }
        if (!form.getSrPublished().equals("*")) {
        	query.setParameter("published", form.getSrPublished());
        }
        Iterator<?> iterator = query.getResultList().iterator();
        Vector<TaxRegionDisplayForm> vector = new Vector<TaxRegionDisplayForm>();
        while (iterator.hasNext()) {
        	TaxRegion taxRegion = (TaxRegion) iterator.next();
    		TaxRegionDisplayForm taxRegionDisplay = new TaxRegionDisplayForm();
    		taxRegionDisplay.setTaxRegionId(Format.getLong(taxRegion.getTaxRegionId()));
    		taxRegionDisplay.setTaxRegionDesc(taxRegion.getTaxRegionDesc());
    		taxRegionDisplay.setPublished(String.valueOf(taxRegion.getPublished()));
    		vector.add(taxRegionDisplay);
        }
        form.setTaxRegions(vector);
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward back(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        TaxRegionListingActionForm form = (TaxRegionListingActionForm) actionForm;
        if (form.getTaxRegions() != null) {
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
    	Site site = getAdminBean(request).getSite();
        TaxRegionListingActionForm form = (TaxRegionListingActionForm) actionForm;
        String taxRegionIds[] = form.getTaxRegionIds();
        
        if (taxRegionIds != null) {
	        for (int i = 0; i < taxRegionIds.length; i++) {
	            TaxRegion taxRegion = new TaxRegion();
	            taxRegion = TaxRegionDAO.load(site.getSiteId(), Format.getLong(taxRegionIds[i]));
	            em.remove(taxRegion);
	        }
        }

        ActionForward forward = new ActionForward();
        forward = new ActionForward(request.getServletPath() + "?process=list", true);
        return forward;
    }

    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("remove", "remove");
        map.put("list", "list");
        map.put("start", "start");
        map.put("back", "back");
        return map;
    }
}