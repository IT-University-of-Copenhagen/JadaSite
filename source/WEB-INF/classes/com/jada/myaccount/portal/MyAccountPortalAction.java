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

package com.jada.myaccount.portal;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.jada.content.frontend.FrontendBaseAction;
import com.jada.dao.SiteDAO;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.Site;
import com.jada.myaccount.login.MyAccountLoginAction;

public class MyAccountPortalAction extends FrontendBaseAction {
    Logger logger = Logger.getLogger(MyAccountLoginAction.class);
    
    public void init(HttpServletRequest request, MyAccountPortalActionForm form) throws Exception {
    	Customer customer = getCustomer(request);
    	form.setDisplayFirstName(customer.getCustAddress().getCustFirstName());
    	form.setDisplayLastName(customer.getCustAddress().getCustLastName());
    	
    	Site site = getContentBean(request).getContentSessionBean().getSiteDomain().getSite();
    	form.setStoreCreditCard(true);
    	if (!SiteDAO.isStoreCreditCard(site)) {
    		form.setStoreCreditCard(false);
    	}
    }

    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	ActionForward actionForward = null;
    	init(request, (MyAccountPortalActionForm) actionForm);
		actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        return map;
    }
}