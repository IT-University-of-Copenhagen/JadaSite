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

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import com.jada.content.ContentAction;
import com.jada.content.template.TemplateEngine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FrontendAction extends ContentAction {
	
    Logger logger = Logger.getLogger(FrontendAction.class);
	
    public ActionForward performAction(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	String data = "";
    	TemplateEngine engine = TemplateEngine.getInstance();
    	engine.init(request, this.getServlet().getServletConfig().getServletContext());
		data =  engine.mergeData("template.vm");
        OutputStream outputStream;
		outputStream = response.getOutputStream();
        outputStream.write(data.getBytes("UTF-8"));
        outputStream.flush();

        return null;
    }
}