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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

import com.jada.system.ApplicationGlobal;
import com.jada.util.AESEncoder;
import com.jada.util.Format;
import com.jada.util.Utility;

public class DatabaseTestAction extends LookupDispatchAction {
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)throws Throwable {
    	
    	InstallActionForm form = (InstallActionForm) actionForm;
    	form.setInstallCompleted(false);
    	form.setContextPath(ApplicationGlobal.getContextPath());
    	Installer installer = Installer.getInstance();
    	installer.init(this.getServlet().getServletContext());
    	
    	ActionMessages errors = new ActionMessages();
    	if (installer.isInstallCompleted()) {
    		form.setInstallCompleted(true);
    		errors.add("message", new ActionMessage("error.install.completed"));
    	}
    	saveMessages(request, errors);
    	form.setDriver("com.mysql.jdbc.Driver");
    	form.setDbHost("localhost");
    	form.setDbPort("3306");
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}
    
    public ActionForward update(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)throws Throwable {
    	
    	InstallActionForm form = (InstallActionForm) actionForm;
    	form.setContextPath(ApplicationGlobal.getContextPath());
    	Installer installer = Installer.getInstance();
    	installer.setInstallActionForm(form);
    	
    	ActionMessages errors = new ActionMessages();
    	if (installer.isInstallCompleted()) {
    		form.setInstallCompleted(true);
    		errors.add("message", new ActionMessage("error.install.completed"));
    	}
    	if (Format.isNullOrEmpty(form.getDriver())) {
    		errors.add("driver", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getDbHost())) {
    		errors.add("dbHost", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getDbPort())) {
    		errors.add("dbPort", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getUsername())) {
    		errors.add("username", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getPassword())) {
    		errors.add("password", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getDbName())) {
    		errors.add("dbName", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getEncryptionKey())) {
    		errors.add("encryptionKey", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getWorkingDirectory())) {
    		errors.add("workingDirectory", new ActionMessage("error.string.required"));
    	}
    	else {
    		if (!installer.isValidDirectory(form.getWorkingDirectory())) {
    			errors.add("workingDirectory", new ActionMessage("error.directory.notwritable"));
    		}
    	}
    	if (Format.isNullOrEmpty(form.getLog4jDirectory())) {
    		errors.add("log4jDirectory", new ActionMessage("error.string.required"));
    	}
    	else {
    		if (!installer.isValidDirectory(form.getLog4jDirectory())) {
    			errors.add("log4jDirectory", new ActionMessage("error.directory.notwritable"));
    		}
    	}
    	if (errors.size() > 0) {
	    	saveMessages(request, errors);
			ActionForward actionForward = actionMapping.findForward("error");
			return actionForward;
    	}

    	try {
    		installer.setInstallActionForm(form);
    		installer.testDatabaseConnectivity();
    		
		} catch (Exception e) {
			form.setError(true);
			String errorInfo = Utility.getStackTrace(e);
			errorInfo = errorInfo.replaceAll("\n", "<br>");
			form.setDetailLog(errorInfo);
    		return actionMapping.findForward("error");
		}
		finally {
//			installer.writeConfig();
		}

		ActionForward actionForward = actionMapping.findForward("next");
		return actionForward;
	}
    
    public ActionForward generate(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)throws Throwable {
    	
    	InstallActionForm form = (InstallActionForm) actionForm;
    	form.setEncryptionKey(AESEncoder.generateKey());
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
    }

	protected Map<String, String> getKeyMethodMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        map.put("update", "update");
        map.put("generate", "generate");
        return map;
	}
}
