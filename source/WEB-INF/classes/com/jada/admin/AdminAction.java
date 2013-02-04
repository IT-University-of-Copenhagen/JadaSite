/*
 * Copyright (C) 2007-2010 JadaSite.

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

import org.apache.struts.action.*;
import javax.servlet.http.*;
import org.apache.log4j.Logger;

public class AdminAction extends AdminLookupDispatchAction {
    Logger logger = Logger.getLogger(AdminAction.class);

    /*
     * Landing method for Action
     */
    public ActionForward execute(ActionMapping actionMapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        return process(actionMapping, actionForm, request, response, null);
    }
    
    /*
     * Final dispatch routine
     */
    protected ActionForward customProcess(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, String name) throws Throwable {
    	return performAction(actionMapping, actionForm, request, response);
    }
    
    public ActionForward performAction(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws Throwable {
    	throw new java.lang.UnsupportedOperationException("Method perform() not yet implemented.");
	}
}
