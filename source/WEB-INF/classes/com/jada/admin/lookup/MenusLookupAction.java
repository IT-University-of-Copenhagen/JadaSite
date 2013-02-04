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

package com.jada.admin.lookup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.jada.util.JSONEscapeObject;

import com.jada.admin.AdminAction;
import com.jada.util.Format;
import com.jada.util.Utility;

public class MenusLookupAction extends AdminAction {
    public ActionForward performAction(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
        String siteDomainId = request.getParameter("siteDomainId");
        JSONEscapeObject jsonCategory = Utility.makeJSONMenuTree(Format.getLong(siteDomainId), null, true);
        String jsonString = jsonCategory.toHtmlString();
        this.streamWebService(response, jsonString);
        return null;
    }
}
