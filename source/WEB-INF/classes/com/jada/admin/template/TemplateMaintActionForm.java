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

import org.apache.struts.upload.FormFile;

import com.jada.admin.AdminMaintActionForm;

public class TemplateMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -5476353376106770916L;
	boolean root;
	boolean servletResource;
	FormFile file;
	String parentFullPath;
	String templateId;
	String templateName;
	String templateDesc;
	String[] templateIds;
	String path;
	String filename;
	Vector<?> pathEntries;
	Vector<?> files;
	String editText;
	String directory;
	String removeFiles[];
	public String[] getRemoveFiles() {
		return removeFiles;
	}
	public void setRemoveFiles(String[] removeFiles) {
		this.removeFiles = removeFiles;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	public String getEditText() {
		return editText;
	}
	public void setEditText(String editText) {
		this.editText = editText;
	}
	public String getTemplateDesc() {
		return templateDesc;
	}
	public void setTemplateDesc(String templateDesc) {
		this.templateDesc = templateDesc;
	}
	public FormFile getFile() {
		return file;
	}
	public Vector<?> getFiles() {
		return files;
	}
	public void setFiles(Vector<?> files) {
		this.files = files;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Vector<?> getPathEntries() {
		return pathEntries;
	}
	public void setPathEntries(Vector<?> pathEntries) {
		this.pathEntries = pathEntries;
	}
	public void setFile(FormFile file) {
		this.file = file;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String[] getTemplateIds() {
		return templateIds;
	}
	public void setTemplateIds(String[] templateIds) {
		this.templateIds = templateIds;
	}
	public boolean isRoot() {
		return root;
	}
	public void setRoot(boolean root) {
		this.root = root;
	}
	public String getParentFullPath() {
		return parentFullPath;
	}
	public void setParentFullPath(String parentFullPath) {
		this.parentFullPath = parentFullPath;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public boolean isServletResource() {
		return servletResource;
	}
	public void setServletResource(boolean servletResource) {
		this.servletResource = servletResource;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
}
