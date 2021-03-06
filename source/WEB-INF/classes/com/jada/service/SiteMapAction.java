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

package com.jada.service;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.jada.sitemap.SiteMap;
import com.jada.util.Constants;

public class SiteMapAction extends SimpleAction {
    Logger logger = Logger.getLogger(SiteMapAction.class);

    public ActionForward process(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");
        try {
        	String domainName = request.getServerName();
        	int port = request.getServerPort();
        	SiteMap siteMap = new SiteMap(domainName, String.valueOf(port));
        	String result = siteMap.generate();
        	
        	response.setContentType("text/xml; charset=UTF-8");
            response.setContentLength(result.getBytes(Constants.SYSTEM_ENCODING).length);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(result.getBytes(Constants.SYSTEM_ENCODING));
        }
        catch (Exception e) {
            logger.error("Unable to service request", e);
        }
        return null;
    }
}
