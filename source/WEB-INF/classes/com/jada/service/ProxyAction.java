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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.jada.system.ApplicationGlobal;
import com.jada.util.Constants;

public class ProxyAction extends Action {
    Logger logger = Logger.getLogger(ImageProvider.class);
    static Properties mimeProperties = null;
    
    private synchronized Properties getMimeProperties() throws IOException {
    	if (mimeProperties == null) {
    		mimeProperties = new Properties();
    		InputStream inputStream = this.getClass().getResourceAsStream("/mime.properties");
    		mimeProperties.load(inputStream);
    	}
    	return mimeProperties;
    }
    
    public ActionForward execute(ActionMapping actionMapping,
            					 ActionForm actionForm,
            					 HttpServletRequest request,
            					 HttpServletResponse response) {
		String uri = request.getRequestURI();
		String prefix = "/" + ApplicationGlobal.getContextPath() + Constants.URL_PREFIX_PROXY;
		if (!uri.matches(prefix + ".*")) {
        	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		String path = uri.substring(prefix.length());
		String workingDirectory = ApplicationGlobal.getWorkingDirectory();
		String filename = workingDirectory + "/" + path;

        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Expires", "0");
        response.setHeader("Pragma", "no-cache");

		try {
	        OutputStream outputStream = response.getOutputStream();
	        File file = new File(filename);
	        if (!file.exists()) {
	        	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	        	return null;
	        }
	        
            response.setContentLength((int) file.length());
            
            String fileExtension = "";
	        String tokens[] = file.getName().split("\\.");
	        if (tokens.length > 1) {
	        	fileExtension = tokens[1];
	        }
            Properties properties = getMimeProperties();
            String contentType = (String) properties.get(fileExtension);
            response.setContentType(contentType);

	        
	        InputStream is = new BufferedInputStream(new FileInputStream(file));
	        byte data[] = new byte[1024];
	        while (true) {
	        	int len = is.read(data);
	        	if (len == -1) {
	        		break;
	        	}
		        outputStream.write(data, 0, len);
	        }
	        outputStream.flush();
		}
		catch (Exception e) {
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
    	return null;
    }
}
