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

package com.jada.admin.customAttribute;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.CustomAttributeDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.CustomAttribute;
import com.jada.jpa.entity.CustomAttributeLanguage;
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

public class CustomAttributeListingAction
    extends AdminLookupDispatchAction {
	
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        CustomAttributeListingActionForm form = (CustomAttributeListingActionForm) actionForm;
        form.setCustomAttributes(null);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward list(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	CustomAttributeListingActionForm form = (CustomAttributeListingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        initSiteProfiles(form, site);
		MessageResources resources = this.getResources(request);

        Query query = null;
        String sql = "select customAttribute " +
        			 "from   CustomAttribute customAttribute " + 
        			 "left   join customAttribute.site site " +
        			 "left   join customAttribute.customAttributeLanguage customAttributeLanguage " +
        			 "where  site.siteId = :siteId " +
        			 "and    customAttributeLanguage.siteProfileClass.siteProfileClassId = :siteProfileClassId ";
        if (!Format.isNullOrEmpty(form.getSrCustomAttribName())) {
        	sql += "and customAttribute.customAttribName like :customAttribName ";
        }
        sql += "order by customAttribute.customAttribName";
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        query.setParameter("siteProfileClassId", form.getSiteProfileClassDefaultId());
        if (form.getSrCustomAttribName() != null && form.getSrCustomAttribName().length() > 0) {
        	query.setParameter("customAttribName", "%" + form.getSrCustomAttribName() + "%");
        }
        Iterator<?> iterator = query.getResultList().iterator();
        Vector<CustomAttributeDisplayForm> vector = new Vector<CustomAttributeDisplayForm>();
        while (iterator.hasNext()) {
        	CustomAttribute customAttribute = (CustomAttribute) iterator.next();
    		CustomAttributeDisplayForm customAttributeDisplay = new CustomAttributeDisplayForm();
    		customAttributeDisplay.setCustomAttribId(Format.getLong(customAttribute.getCustomAttribId()));
    		customAttributeDisplay.setCustomAttribName(customAttribute.getCustomAttribName());
    		customAttributeDisplay.setCustomAttribTypeDesc(resources.getMessage("customAttrib.typeCode." + customAttribute.getCustomAttribTypeCode()));
    		customAttributeDisplay.setSystemRecord(String.valueOf(customAttribute.getSystemRecord()));
    		vector.add(customAttributeDisplay);
        }
        form.setCustomAttributes(vector);
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward back(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	CustomAttributeListingActionForm form = (CustomAttributeListingActionForm) actionForm;
        if (form.getCustomAttributes() != null) {
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
    	CustomAttributeListingActionForm form = (CustomAttributeListingActionForm) actionForm;
        String customAttribIds[] = form.getCustomAttribIds();
        
        try {
	        if (customAttribIds != null) {
		        for (int i = 0; i < customAttribIds.length; i++) {
		            CustomAttribute customAttribute = new CustomAttribute();
		            customAttribute = CustomAttributeDAO.load(getAdminBean(request).getSite().getSiteId(), Format.getLong(customAttribIds[i]));
		            if (customAttribute.getSystemRecord() == Constants.VALUE_YES) {
		            	continue;
		            }
		            em.remove(customAttribute);
					for (CustomAttributeLanguage customAttributeLanguage : customAttribute.getCustomAttributeLanguages()) {
						em.remove(customAttributeLanguage);
					}
		        }
		        em.getTransaction().commit();
	        }
        }
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.customAttribute.constraint"));
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