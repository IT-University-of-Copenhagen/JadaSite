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

package com.jada.myaccount.login;

import com.jada.content.ContentBean;
import com.jada.content.ContentLookupDispatchAction;

import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class MyAccountLogoutAction extends ContentLookupDispatchAction {
    Logger logger = Logger.getLogger(MyAccountLogoutAction.class);

    public ActionForward logout(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	ContentBean contentBean = getContentBean(request);
		createEmptyTemplateInfo(request);
    	this.removeSession(request);
        ActionForward actionForward = actionMapping.findForward("logoutSuccess");
        String path = actionForward.getPath();
        path += "&prefix=" + contentBean.getContentSessionBean().getSiteDomain().getSiteDomainPrefix();
        path += "&langName=" + contentBean.getContentSessionKey().getSiteProfileClassName();
        ActionForward forward = new ActionForward();
        forward.setPath(path);
        forward.setRedirect(true);
        return forward;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("logout", "logout");
        return map;
    }
}