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

package com.jada.install.process;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

import com.jada.system.ApplicationGlobal;
import com.jada.util.Utility;

public class DatabaseLoadAction extends LookupDispatchAction {
    public ActionForward wait(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) {
    	InstallActionForm form = (InstallActionForm) actionForm;
    	form.setContextPath(ApplicationGlobal.getContextPath());
    	form.setError(false);
    	form.setDetailLog("");
    	return actionMapping.findForward("wait");
    }
    
    public ActionForward create(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) {
    	
    	InstallActionForm form = (InstallActionForm) actionForm;
    	Installer installer = Installer.getInstance();
    	form.setError(false);
    	form.setDetailLog("");
    	try {
    		if (installer.isDatabaseCreated()) {
    			installer.upgradeDatabase();
    			form.setUpgrade(true);
    		}
    		else {
	    		installer.installDatabase();
    		}
    		installer.writeConfig();
    		installer.setDatabaseCreated();
		} catch (Exception e) {
			form.setError(true);			
			String errorInfo = Utility.getStackTrace(e);
			errorInfo = errorInfo.replaceAll("\n", "<br>");
			form.setDetailLog(form.getDetailLog() + "\n" + errorInfo);
    		return actionMapping.findForward("error");
		}
		finally {
		}
		
    	return actionMapping.findForward("success");
    }
    
    public ActionForward complete(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	InstallActionForm form = (InstallActionForm) actionForm;
    	form.setContextPath(ApplicationGlobal.getContextPath());
    	form.setError(false);
    	form.setDetailLog("");
    	return actionMapping.findForward("success");
    }

   protected java.util.Map<String, String> getKeyMethodMap()  {
       Map<String, String> map = new HashMap<String, String>();
       map.put("wait", "wait");
       map.put("create", "create");
       map.put("complete", "complete");
       return map;
   }
}