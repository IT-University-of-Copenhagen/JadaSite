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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.ShippingTypeDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.ShippingMethodRegionType;
import com.jada.jpa.entity.ShippingType;
import com.jada.jpa.entity.Site;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;


import javax.persistence.Query;

import java.util.Iterator;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class ShippingTypeListingAction
    extends AdminLookupDispatchAction {
	
	public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        ShippingTypeListingActionForm form = (ShippingTypeListingActionForm) actionForm;
        form.setShippingTypes(null);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward list(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        ShippingTypeListingActionForm form = (ShippingTypeListingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();

        Query query = null;
        String sql = "select shippingType from ShippingType shippingType where siteId = :siteId ";
        if (form.getSrShippingTypeName().length() > 0) {
        	sql += "and shippingTypeName like :shippingTypeName ";
        }
        sql += "order by shippingTypeName";
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        if (form.getSrShippingTypeName().length() > 0) {
        	query.setParameter("shippingTypeName", "%" + form.getSrShippingTypeName() + "%");
        }
        Iterator<?> iterator = query.getResultList().iterator();
        Vector<ShippingTypeDisplayForm> vector = new Vector<ShippingTypeDisplayForm>();
        while (iterator.hasNext()) {
        	ShippingType shippingType = (ShippingType) iterator.next();
    		ShippingTypeDisplayForm shippingTypeDisplay = new ShippingTypeDisplayForm();
    		shippingTypeDisplay.setShippingTypeId(Format.getLong(shippingType.getShippingTypeId()));
    		shippingTypeDisplay.setShippingTypeName(shippingType.getShippingTypeName());
    		shippingTypeDisplay.setSystemRecord(String.valueOf(shippingType.getSystemRecord()));
    		vector.add(shippingTypeDisplay);
        }
        form.setShippingTypes(vector);
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward back(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        ShippingTypeListingActionForm form = (ShippingTypeListingActionForm) actionForm;
        if (form.getShippingTypes() != null) {
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
        ShippingTypeListingActionForm form = (ShippingTypeListingActionForm) actionForm;
        String shippingTypeIds[] = form.getShippingTypeIds();
        
        try {
	        if (shippingTypeIds != null) {
		        for (int i = 0; i < shippingTypeIds.length; i++) {
		            ShippingType shippingType = new ShippingType();
		            shippingType = ShippingTypeDAO.load(getAdminBean(request).getSite().getSiteId(), Format.getLong(shippingTypeIds[i]));
		            if (shippingType.getSystemRecord() == Constants.VALUE_YES) {
		            	continue;
		            }
					Iterator<?> iterator = shippingType.getShippingMethodRegionTypes().iterator();
					while (iterator.hasNext()) {
						ShippingMethodRegionType shippingMethodRegionType = (ShippingMethodRegionType) iterator.next();
						em.remove(shippingMethodRegionType.getShippingRate());
						em.remove(shippingMethodRegionType);
					}
		            em.remove(shippingType);
		        }
		        em.getTransaction().commit();
	        }
        }
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.shippingTypes.constraint"));
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

    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("remove", "remove");
        map.put("list", "list");
        map.put("start", "start");
        map.put("back", "back");
        return map;
    }
}