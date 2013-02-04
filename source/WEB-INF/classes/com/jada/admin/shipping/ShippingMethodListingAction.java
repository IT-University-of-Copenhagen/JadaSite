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
import org.apache.struts.util.MessageResources;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.ShippingMethodDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.ShippingMethod;
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

public class ShippingMethodListingAction
    extends AdminLookupDispatchAction {
	
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        ShippingMethodListingActionForm form = (ShippingMethodListingActionForm) actionForm;
        form.setShippingMethods(null);
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

    	ShippingMethodListingActionForm form = (ShippingMethodListingActionForm) actionForm;
        initListInfo(form, request);

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward back(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        ShippingMethodListingActionForm form = (ShippingMethodListingActionForm) actionForm;
        if (form.getShippingMethods() != null) {
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
    	
    	Site site = getAdminBean(request).getSite();
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        ShippingMethodListingActionForm form = (ShippingMethodListingActionForm) actionForm;
        ShippingMethodDisplayForm shippingMethods[] = form.getShippingMethods();
        
        for (int i = 0; i < shippingMethods.length; i++) {
        	if (shippingMethods[i].isSelected()) {
	            ShippingMethod shippingMethod = new ShippingMethod();
	            shippingMethod = ShippingMethodDAO.load(site.getSiteId(), Format.getLong(shippingMethods[i].getShippingMethodId()));
	            em.remove(shippingMethod);
        	}
        }

        ActionForward forward = new ActionForward();
        forward = new ActionForward(request.getServletPath() + "?process=list", true);
        return forward;
    }
    
    public ActionForward resequence(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	ShippingMethodListingActionForm form = (ShippingMethodListingActionForm) actionForm;
        
		ActionMessages errors = validateResequence(request, form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
	        // TODO do not override seqnum when there is an error.
	        em.flush();
			return actionMapping.findForward("error");
		}
		
		ShippingMethodDisplayForm shippingMethods[] = form.getShippingMethods();
		for (int i = 0; i < shippingMethods.length; i++) {
            Long shippingMethodId = Format.getLong(shippingMethods[i].getShippingMethodId());
            int seqNum = Format.getInt(shippingMethods[i].getSeqNum());
            ShippingMethod shippingMethod = ShippingMethodDAO.load(getAdminBean(request).getSite().getSiteId(), shippingMethodId);
            shippingMethod.setSeqNum(seqNum);
            em.persist(shippingMethod);
		}
        initListInfo(form, request);

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public void initListInfo(ShippingMethodListingActionForm form, HttpServletRequest request) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();

        Query query = null;
        String sql = "from  ShippingMethod shippingMethod " + 
        			 "where siteId = :siteId ";
        if (form.getSrShippingMethodName().length() > 0) {
        	sql += "and shippingMethod.shippingMethodLanguage.shippingMethodName like :shippingMethodName ";
        }
        if (!form.getSrPublished().equals("*")) {
        	sql += "and published = :published ";
        }
        sql += "order by seqNum";
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        if (form.getSrShippingMethodName().length() > 0) {
        	query.setParameter("shippingMethodName", "%" + form.getSrShippingMethodName() + "%");
        }
        if (!form.getSrPublished().equals("*")) {
        	query.setParameter("published", form.getSrPublished());
        }
        Iterator<?> iterator = query.getResultList().iterator();
        Vector<ShippingMethodDisplayForm> vector = new Vector<ShippingMethodDisplayForm>();
        while (iterator.hasNext()) {
        	ShippingMethod shippingMethod = (ShippingMethod) iterator.next();
    		ShippingMethodDisplayForm shippingMethodDisplay = new ShippingMethodDisplayForm();
    		shippingMethodDisplay.setShippingMethodId(Format.getLong(shippingMethod.getShippingMethodId()));
    		shippingMethodDisplay.setShippingMethodName(shippingMethod.getShippingMethodLanguage().getShippingMethodName());
    		shippingMethodDisplay.setSeqNum(Format.getInt(shippingMethod.getSeqNum()));
    		shippingMethodDisplay.setPublished(String.valueOf(shippingMethod.getPublished()));
    		vector.add(shippingMethodDisplay);
        }
        ShippingMethodDisplayForm shippingMethods[] = new ShippingMethodDisplayForm[vector.size()];
        vector.copyInto(shippingMethods);
        form.setShippingMethods(shippingMethods);
    }

    public ActionMessages validateResequence(HttpServletRequest request, ShippingMethodListingActionForm form) {
    	MessageResources resources = this.getResources(request);
    	
    	ActionMessages errors = new ActionMessages();
    	ShippingMethodDisplayForm shippingMethods[] = form.getShippingMethods();
    	for (int i = 0; i < shippingMethods.length; i++) {
    		if (!Format.isInt(shippingMethods[i].getSeqNum())) {
        		errors.add("seqNums_" + i, new ActionMessage("error.int.invalid"));
        		// Hack - not able to retrieve index properties from using html:messages in the jsp
        		shippingMethods[i].setSeqNumError(resources.getMessage("error.int.invalid"));
        		break;
    		}
    		else {
        		shippingMethods[i].setSeqNumError(null);
    		}
    	}
    	return errors;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("remove", "remove");
        map.put("list", "list");
        map.put("start", "start");
        map.put("resequence", "resequence");
        map.put("back", "back");
        return map;
    }
}