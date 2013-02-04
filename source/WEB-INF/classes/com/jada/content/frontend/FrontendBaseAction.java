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

package com.jada.content.frontend;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.jada.content.ContentLookupDispatchAction;
import com.jada.content.template.TemplateEngine;

public abstract class FrontendBaseAction extends ContentLookupDispatchAction {
    Logger logger = Logger.getLogger(FrontendBaseAction.class);

    /*
     * Landing method for Action
     */
/*
    public ActionForward execute(ActionMapping actionMapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        return process(actionMapping, actionForm, request, response, null);
    }
*/
    /*
     * Final dispatch routine
     */
    protected ActionForward customProcess(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, String name) throws Throwable {
		if (actionForm instanceof FrontendBaseForm) {
			FrontendBaseForm form = (FrontendBaseForm) actionForm;
			form.setUseTemplate(true);
		}
		ActionForward actionForward = super.customProcess(actionMapping, actionForm, request, response, name);
    	
    	TemplateEngine engine = TemplateEngine.getInstance();
    	engine.init(request, this.getServlet().getServletConfig().getServletContext());
    	
    	if (actionForward == null) {
    		return null;
    	}
    	
    	if (actionForward.getRedirect()) {
    		return actionForward;
    	}
    	
    	String data = "";
    	
		engine.setStrutForm(actionForm);
		boolean useTemplate = true;
		boolean isPrintTemplate = false;
		if (actionForm instanceof FrontendBaseForm) {
			FrontendBaseForm form = (FrontendBaseForm) actionForm;
			if (!form.isUseTemplate()) {
				useTemplate = false;
			}
			isPrintTemplate = form.isPrintTemplate();
		}
    	if (useTemplate) {
			engine.setCustomPage(actionForward.getPath());
			if (isPrintTemplate) {
				data = engine.mergeData("print.vm");
			}
			else {
				data = engine.mergeData("template.vm");
			}
    	}
    	else {
    		data = engine.mergeData(actionForward.getPath());
    	}
		
        OutputStream outputStream;
		outputStream = response.getOutputStream();
        outputStream.write(data.getBytes("UTF-8"));
        outputStream.flush();

        return null;
    }
    
    public ActionForward performAction(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws Throwable {
    	throw new java.lang.UnsupportedOperationException("Method perform() not yet implemented.");
	}

}