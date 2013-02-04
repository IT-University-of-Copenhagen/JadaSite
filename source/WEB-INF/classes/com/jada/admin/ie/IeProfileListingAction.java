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

package com.jada.admin.ie;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.IeProfileHeaderDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.IeProfileDetail;
import com.jada.jpa.entity.IeProfileHeader;
import com.jada.jpa.entity.Site;
import com.jada.util.Constants;
import com.jada.util.Format;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;


import javax.persistence.Query;

import java.util.Iterator;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class IeProfileListingAction
    extends AdminLookupDispatchAction {
	
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        IeProfileListingActionForm form = (IeProfileListingActionForm) actionForm;
        form.setIeProfileHeaders(null);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward list(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        IeProfileListingActionForm form = (IeProfileListingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();

        Query query = null;
        String sql = "from 		IeProfileHeader ieProfileHeader " +
        			 "where 	ieProfileHeader.site.siteId = :siteId ";
        if (!Format.isNullOrEmpty(form.getSrIeProfileHeaderName())) {
        	sql += "and ieProfileHeaderName like :ieProfileHeaderName ";
        }
        sql += "order by ieProfileHeaderName";
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        if (!Format.isNullOrEmpty(form.getSrIeProfileHeaderName())) {
        	query.setParameter("ieProfileHeaderName", "%" + form.getSrIeProfileHeaderName() + "%");
        }
        Iterator<?> iterator = query.getResultList().iterator();
        Vector<IeProfileHeaderDisplayForm> vector = new Vector<IeProfileHeaderDisplayForm>();
        while (iterator.hasNext()) {
        	IeProfileHeader ieProfileHeader = (IeProfileHeader) iterator.next();
    		IeProfileHeaderDisplayForm ieProfileHeaderDisplay = new IeProfileHeaderDisplayForm();
    		ieProfileHeaderDisplay.setIeProfileHeaderId(Format.getLong(ieProfileHeader.getIeProfileHeaderId()));
    		ieProfileHeaderDisplay.setIeProfileHeaderName(ieProfileHeader.getIeProfileHeaderName());
    		if (ieProfileHeader.getIeProfileType() == 'I') {
    			ieProfileHeaderDisplay.setIeProfileTypeValue("Import");
    		}
    		else {
    			ieProfileHeaderDisplay.setIeProfileTypeValue("Export");
    		}
    		ieProfileHeaderDisplay.setIeProfileType(String.valueOf(ieProfileHeader.getIeProfileType()));
    		ieProfileHeaderDisplay.setSystemRecord(String.valueOf(ieProfileHeader.getSystemRecord()));
    		ieProfileHeaderDisplay.setModify(true);
    		if (ieProfileHeader.getSystemRecord() == Constants.VALUE_YES) {
    			ieProfileHeaderDisplay.setModify(false);
    		}
    		vector.add(ieProfileHeaderDisplay);
        }
        IeProfileHeaderDisplayForm ieProfileHeaders[] = new IeProfileHeaderDisplayForm[vector.size()];
        vector.copyInto(ieProfileHeaders);
        form.setIeProfileHeaders(ieProfileHeaders);
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward back(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        IeProfileListingActionForm form = (IeProfileListingActionForm) actionForm;
        if (form.getIeProfileHeaders() != null) {
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
        IeProfileListingActionForm form = (IeProfileListingActionForm) actionForm;
        for (IeProfileHeaderDisplayForm headerForm : form.getIeProfileHeaders()) {
	        if (headerForm.isSelected()) {
	        	IeProfileHeader ieProfileHeader = IeProfileHeaderDAO.load(site.getSiteId(), Format.getLong(headerForm.getIeProfileHeaderId()));
	            Iterator<?> iterator = ieProfileHeader.getIeProfileDetails().iterator();
	            while (iterator.hasNext()) {
	            	IeProfileDetail ieProfileDetail = (IeProfileDetail) iterator.next();
	            	em.remove(ieProfileDetail);
	            }
	            em.remove(ieProfileHeader);
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