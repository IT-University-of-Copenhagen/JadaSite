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

package com.jada.admin.system;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jada.admin.AdminLookupDispatchAction;
import com.jada.util.Utility;
import com.jada.util.Constants;

import java.io.File;
import java.util.Map;
import java.util.HashMap;

public class SystemMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward start(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse)
        throws Throwable {
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward reset(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

    	String filename = Utility.getServletLocation(this.getServlet().getServletContext()) + Constants.CONFIG_PROPERTIES_FILENAME;
    	File file = new File(filename);
    	file.delete();
        ActionForward actionForward = actionMapping.findForward("resetSuccess");
        return actionForward;
    }

    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("reset", "reset");
        map.put("start", "start");
        return map;
    }
}
