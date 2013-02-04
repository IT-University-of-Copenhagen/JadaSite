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

package com.jada.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.jada.jpa.entity.Site;

public abstract class AdminListingAction extends AdminLookupDispatchAction {

	
	/*
	 * First time entry
	 */
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	AdminListingActionForm form = (AdminListingActionForm) actionForm;
    	Site site = getAdminBean(request).getSite();
    	this.initSiteProfiles(form, site);
    	initForm(form);
    	form.setEmpty(true);
        initSearchInfo((AdminListingActionForm) actionForm, site.getSiteId());
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    /*
     * User linking from the detail screen
     */
    public ActionForward back(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        AdminListingActionForm form = (AdminListingActionForm) actionForm;
        if (form.isEmpty()) {
        	return start(actionMapping, actionForm, request, response);
        }
        extract(form, request);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    /*
     * User perform new search
     */
    public ActionForward search(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        AdminListingActionForm form = (AdminListingActionForm) actionForm;
        form.setSrPageNo("");
        extract(form, request);
        initSearchInfo(form, getAdminBean(request).getSite().getSiteId());
        form.setEmpty(false);
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    /*
     * User navigating to another page
     */
    public ActionForward list(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	AdminListingActionForm form = (AdminListingActionForm) actionForm;
        extract(form, request);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    
    /*
     * To be call only when to start a new form
     */
    abstract public void initForm(AdminListingActionForm form);
    
    abstract public void initSearchInfo(AdminListingActionForm form, String siteId) throws Exception;
    
    abstract public void extract(AdminListingActionForm form, HttpServletRequest request) throws Throwable;
}
