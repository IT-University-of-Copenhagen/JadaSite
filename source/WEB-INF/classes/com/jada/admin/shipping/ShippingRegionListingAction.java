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

package com.jada.admin.shipping;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.ShippingRegionDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.ShippingMethodRegion;
import com.jada.jpa.entity.ShippingRegion;
import com.jada.jpa.entity.Site;
import com.jada.util.Format;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;


import javax.persistence.Query;

import java.util.Iterator;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class ShippingRegionListingAction
    extends AdminLookupDispatchAction {
	
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        ShippingRegionListingActionForm form = (ShippingRegionListingActionForm) actionForm;
        form.setShippingRegions(null);
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
        ShippingRegionListingActionForm form = (ShippingRegionListingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();

        Query query = null;
        String sql = "select	shippingRegion " + 
        			 "from 		ShippingRegion shippingRegion " +
        			 "where 	siteId = :siteId " + 
        			 "and 		systemRecord = 'Y'";
        if (form.getSrShippingRegionName().length() > 0) {
        	sql += "and shippingRegionName like :shippingRegionName ";
        }
        if (!form.getSrPublished().equals("*")) {
        	sql += "and published = :published ";
        }
        sql += "order by shippingRegionName";
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        if (form.getSrShippingRegionName().length() > 0) {
        	query.setParameter("shippingRegionName", "%" + form.getSrShippingRegionName() + "%");
        }
        if (!form.getSrPublished().equals("*")) {
        	query.setParameter("published", form.getSrPublished());
        }
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        if (form.getSrShippingRegionName().length() > 0) {
        	query.setParameter("shippingRegionName", "%" + form.getSrShippingRegionName() + "%");
        }
        if (!form.getSrPublished().equals("*")) {
        	query.setParameter("published", form.getSrPublished());
        }
        Iterator<?> iterator = query.getResultList().iterator();
        Vector<ShippingRegionDisplayForm> vector = new Vector<ShippingRegionDisplayForm>();
        while (iterator.hasNext()) {
        	ShippingRegion shippingRegion = (ShippingRegion) iterator.next();
    		ShippingRegionDisplayForm shippingRegionDisplay = new ShippingRegionDisplayForm();
    		shippingRegionDisplay.setShippingRegionId(Format.getLong(shippingRegion.getShippingRegionId()));
    		shippingRegionDisplay.setShippingRegionName(shippingRegion.getShippingRegionName());
    		shippingRegionDisplay.setSystemRecord(String.valueOf(shippingRegion.getSystemRecord()));
    		shippingRegionDisplay.setPublished("Y");
    		shippingRegionDisplay.setModify(false);
    		vector.add(shippingRegionDisplay);
        }
        
        sql = "select	shippingRegion " +
        	  "from 	ShippingRegion shippingRegion " +
        	  "where 	siteId = :siteId " +
        	  "and 		systemRecord = 'N' ";
        if (form.getSrShippingRegionName().length() > 0) {
        	sql += "and shippingRegionName like :shippingRegionName ";
        }
        if (!form.getSrPublished().equals("*")) {
        	sql += "and published = :published ";
        }
        sql += "order by shippingRegionName";
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        if (form.getSrShippingRegionName().length() > 0) {
        	query.setParameter("shippingRegionName", "%" + form.getSrShippingRegionName() + "%");
        }
        if (!form.getSrPublished().equals("*")) {
        	query.setParameter("published", form.getSrPublished());
        }
        iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	ShippingRegion shippingRegion = (ShippingRegion) iterator.next();
    		ShippingRegionDisplayForm shippingRegionDisplay = new ShippingRegionDisplayForm();
    		shippingRegionDisplay.setShippingRegionId(Format.getLong(shippingRegion.getShippingRegionId()));
    		shippingRegionDisplay.setShippingRegionName(shippingRegion.getShippingRegionName());
    		shippingRegionDisplay.setPublished(String.valueOf(shippingRegion.getPublished()));
    		shippingRegionDisplay.setSystemRecord(String.valueOf(shippingRegion.getSystemRecord()));
    		shippingRegionDisplay.setModify(true);
    		vector.add(shippingRegionDisplay);
        }
        form.setShippingRegions(vector);
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward back(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        ShippingRegionListingActionForm form = (ShippingRegionListingActionForm) actionForm;
        if (form.getShippingRegions() != null) {
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
        ShippingRegionListingActionForm form = (ShippingRegionListingActionForm) actionForm;
        String shippingRegionIds[] = form.getShippingRegionIds();
        
        if (shippingRegionIds != null) {
	        for (int i = 0; i < shippingRegionIds.length; i++) {
	            ShippingRegion shippingRegion = new ShippingRegion();
	            shippingRegion = ShippingRegionDAO.load(site.getSiteId(), Format.getLong(shippingRegionIds[i]));
	            Iterator<?> iterator = shippingRegion.getShippingMethodRegions().iterator();
	            while (iterator.hasNext()) {
	            	ShippingMethodRegion shippingMethodRegion = (ShippingMethodRegion) iterator.next();
	            	em.remove(shippingMethodRegion);
	            }
	            em.remove(shippingRegion);
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