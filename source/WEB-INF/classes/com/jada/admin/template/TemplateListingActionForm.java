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

package com.jada.admin.template;

import java.util.Vector;

import com.jada.admin.AdminMaintActionForm;

public class TemplateListingActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = 4132003682421675465L;
	String mode;
	String srTemplateName;
	String srTemplateDesc;
	String domainAndPort;
    Vector<?> templates;
    String templateNames[];
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getSrTemplateName() {
		return srTemplateName;
	}
	public void setSrTemplateName(String srTemplateName) {
		this.srTemplateName = srTemplateName;
	}
	public String[] getTemplateNames() {
		return templateNames;
	}
	public void setTemplateNames(String[] templateNames) {
		this.templateNames = templateNames;
	}
	public Vector<?> getTemplates() {
		return templates;
	}
	public void setTemplates(Vector<?> templates) {
		this.templates = templates;
	}
	public String getSrTemplateDesc() {
		return srTemplateDesc;
	}
	public void setSrTemplateDesc(String srTemplateDesc) {
		this.srTemplateDesc = srTemplateDesc;
	}
	public String getDomainAndPort() {
		return domainAndPort;
	}
	public void setDomainAndPort(String domainAndPort) {
		this.domainAndPort = domainAndPort;
	}
}
