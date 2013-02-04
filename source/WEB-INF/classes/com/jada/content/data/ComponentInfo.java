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

package com.jada.content.data;

import javax.servlet.http.HttpServletRequest;

/*
 * Used to store information for component that make-up a page.
 */
public class ComponentInfo extends DataInfo {
	String requestURL;
	String contextPath;
	String resourcePrefix;
	String servletResourcePrefix;
	String templateResourcePrefix;
	String templateName;
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getTemplateResourcePrefix() {
		return templateResourcePrefix;
	}
	public void setTemplateResourcePrefix(String templateResourcePrefix) {
		this.templateResourcePrefix = templateResourcePrefix;
	}
	public String getServletResourcePrefix() {
		return servletResourcePrefix;
	}
	public void setServletResourcePrefix(String servletResourcePrefix) {
		this.servletResourcePrefix = servletResourcePrefix;
	}
	public ComponentInfo(HttpServletRequest request) {
		contextPath = request.getContextPath();
	}
	public String getContextPath() {
		return contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	public String getResourcePrefix() {
		return resourcePrefix;
	}
	public void setResourcePrefix(String resourcePrefix) {
		this.resourcePrefix = resourcePrefix;
	}
	public String getRequestURL() {
		return requestURL;
	}
	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}
}
