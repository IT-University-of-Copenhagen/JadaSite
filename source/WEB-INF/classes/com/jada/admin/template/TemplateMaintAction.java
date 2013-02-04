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

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import javax.persistence.EntityManager;
import com.jada.util.JSONEscapeObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.improve.struts.taglib.layout.util.FormUtils;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.TemplateDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.Template;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class TemplateMaintAction
    extends AdminLookupDispatchAction {
    Logger logger = Logger.getLogger(TemplateMaintAction.class);
    static int FILE_MAX_COUNT = 100;

    public ActionForward start(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest request,
                             HttpServletResponse response)
        throws Throwable {

        TemplateMaintActionForm form = (TemplateMaintActionForm) actionForm;
        if (form == null) {
            form = new TemplateMaintActionForm();
        }
        if (form.getTemplateName() != null) {
	        if (form.getTemplateName().equals(Constants.TEMPLATE_BASIC)) {
	        	showDirectoryContentFromServlet(request, form);
	        }
	        else {
	            showDirectoryContent(request, form);
	        }
        }
        FormUtils.setFormDisplayMode(request, form, FormUtils.CREATE_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward upload(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		
        TemplateMaintActionForm form = (TemplateMaintActionForm) actionForm;
        FormUtils.setFormDisplayMode(request, form, FormUtils.CREATE_MODE);
        ActionMessages errors = validate(form);
        if (errors.size() > 0) {
        	saveMessages(request, errors);
			return actionMapping.findForward("error");
        }
        
        String templateName = form.getTemplateName();
        if (Format.isNullOrEmpty(templateName)) {
	        FormFile file = form.getFile();
	        File f = new File(file.getFileName());
	        String filename = f.getName().toLowerCase();
	        templateName = filename.replaceAll("\\.jar", "");
	    }
        
    	String prefix = Utility.getTemplatePrefix(site, templateName);
    	if (prefix == null || prefix.length() == 0) {
    		errors.add("extract", new ActionMessage("error.template.prefix.empty"));
        	saveMessages(request, errors);
			return actionMapping.findForward("error");
    	}

    	Template template = TemplateDAO.load(site.getSiteId(), templateName);
    	if (template != null) {
    		errors.add("extract", new ActionMessage("error.template.duplicate", templateName));
        	saveMessages(request, errors);
			return actionMapping.findForward("error");
    	}

    	try {
			File entry = new File(prefix);
			if (!entry.exists()) {
				entry.mkdir();
			}
			
    		if (Format.isNullOrEmpty(form.getTemplateName())) {
		        InputStream stream = form.getFile().getInputStream();
		        JarInputStream jarStream = new JarInputStream(stream);
		        while (true) {
		        	JarEntry e = jarStream.getNextJarEntry();
		        	if (e == null) {
		        		break;
		        	}
		        	String name = prefix + e.getName();
		        	if (e.isDirectory()) {
		        		File jarEntry = new File(name);
		        		jarEntry.mkdirs();
		        		continue;
		        	}
		        	FileOutputStream writer = new FileOutputStream(name);
		        	byte[] buffer = new byte[1024];
		        	while (true) {
		        		int size = jarStream.read(buffer);
		        		if (size <= 0) {
		        			break;
		        		}
		        		writer.write(buffer, 0, size);
		        	}
		        	writer.close();
		        }
    		}
    	} catch (SecurityException e) {
    		errors.add("extract", new ActionMessage("error.file.create.invalid"));
        	saveMessages(request, errors);
			return actionMapping.findForward("error");
    	} catch (FileNotFoundException e) {
    		errors.add("extract", new ActionMessage("error.file.create.invalid"));
        	saveMessages(request, errors);
			return actionMapping.findForward("error");
    	} catch (Exception e) {
    		errors.add("extract", new ActionMessage("error.file.create.invalid"));
        	saveMessages(request, errors);
			return actionMapping.findForward("error");
    	}
    	
		template = new Template();
    	template.setTemplateDesc(form.getTemplateDesc());
    	template.setRecCreateBy(adminBean.getUser().getUserId());
    	template.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    	template.setTemplateName(templateName);
    	template.setSite(site);
    	template.setRecUpdateBy(adminBean.getUser().getUserId());
    	template.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    	em.persist(template);
    	
    	errors.add("info", new ActionMessage("message.template.created", templateName));
    	saveMessages(request, errors);
    	
    	form.setTemplateName(template.getTemplateName());
		form.setTemplateDesc(template.getTemplateDesc());
        showDirectoryContent(request, form);

        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward edit(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
	
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		TemplateMaintActionForm form = (TemplateMaintActionForm) actionForm;
		if (form == null) {
			form = new TemplateMaintActionForm();
		}
		String templateName = request.getParameter("templateName");
		Template template = new Template();
		template = TemplateDAO.load(site.getSiteId(), templateName);
		form.setTemplateDesc(template.getTemplateDesc());
        if (form.getTemplateName().equals(Constants.TEMPLATE_BASIC)) {
        	showDirectoryContentFromServlet(request, form);
        }
        else {
            showDirectoryContent(request, form);
        }
		FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}
    
	public ActionForward save(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {

		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		TemplateMaintActionForm form = (TemplateMaintActionForm) actionForm;

		Template template = new Template();
		template = TemplateDAO.load(site.getSiteId(), form.getTemplateName());
		
		template.setTemplateDesc(form.getTemplateDesc());
    	template.setRecUpdateBy(adminBean.getUser().getUserName());
    	template.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    	em.persist(template);
    	showDirectoryContent(request, form);
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	public ActionForward savefile(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {

		TemplateMaintActionForm form = (TemplateMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
    	String prefix = Utility.getTemplatePrefix(adminBean.getSite(), form.getTemplateName());
		String filename = formatFilename(prefix, form.getPath(), form.getFilename());
    	
    	BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
    	writer.write(form.getEditText());
    	writer.close();

    	JSONEscapeObject jsonResult = new JSONEscapeObject();
        jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
		return null;
	}
	
	public ActionForward remove(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {

		Site site = getAdminBean(request).getSite();
		TemplateMaintActionForm form = (TemplateMaintActionForm) actionForm;
    	String prefix = Utility.getTemplatePrefix(site, form.getTemplateName());
    	
    	int fileCount = Utility.fileCount(prefix);
    	if (fileCount > FILE_MAX_COUNT) {
    		ActionMessages errors = new ActionMessages();
       		errors.add("msg", new ActionMessage("error.template.maxcount", FILE_MAX_COUNT));
        	saveMessages(request, errors);
            if (!form.getTemplateName().equals(Constants.TEMPLATE_BASIC)) {
            	showDirectoryContent(request, form);
            }
            else {
            	this.showDirectoryContentFromServlet(request, form);
            }
        	FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
			return mapping.findForward("error");
    	}
    	
    	if (!Utility.removeFile(prefix)) {
            ActionMessages errors = new ActionMessages();
       		errors.add("msg", new ActionMessage("error.template.remove", form.getTemplateName()));
        	saveMessages(request, errors);
            if (!form.getTemplateName().equals(Constants.TEMPLATE_BASIC)) {
            	showDirectoryContent(request, form);
            }
            else {
            	this.showDirectoryContentFromServlet(request, form);
            }
            FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
			return mapping.findForward("error");
    	}
    	
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Template template = TemplateDAO.load(site.getSiteId(), form.getTemplateName());
		em.remove(template);
		return mapping.findForward("removeSuccess");
	}
	
	public ActionForward removeFile(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {

		Site site = getAdminBean(request).getSite();
		TemplateMaintActionForm form = (TemplateMaintActionForm) actionForm;
		
		String prefix = Utility.getTemplatePrefix(site, form.getTemplateName());
		String removeFiles[] = form.getRemoveFiles();
		if (removeFiles != null) {
			for (int i = 0; i < removeFiles.length; i++) {
				String filename = prefix;
				if (form.getPath().length() > 0) {
					filename += form.getPath() + "/";
				}
				filename += removeFiles[i];
		    	if (!Utility.removeFile(filename)) {
		            ActionMessages errors = new ActionMessages();
		       		errors.add("msg", new ActionMessage("error.template.remove", filename));
		        	saveMessages(request, errors);
					return mapping.findForward("error");
		    	}
			}
		}
	  	showDirectoryContent(request, form);
	  	FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	void streamErrorJsonMessage(JSONEscapeObject jsonResult, HttpServletResponse response) throws Exception {
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
	}
	
	public ActionForward uploadfile(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {

		TemplateMaintActionForm form = (TemplateMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
		MessageResources resources = this.getResources(request);
    	if (form.getFile().getFileName().length() == 0) {
    		jsonResult.put("message", resources.getMessage("error.file.emptyFilename"));
    		streamErrorJsonMessage(jsonResult, response);
    		return null;
    	}
    	else if (form.getFile().getFileSize() > Constants.FILESIZE_LIMIT) {
    		jsonResult.put("message", resources.getMessage("error.file.limit", Constants.FILESIZE_LIMIT));
    		streamErrorJsonMessage(jsonResult, response);
    		return null;
    	}
        
    	String prefix = Utility.getTemplatePrefix(adminBean.getSite(), form.getTemplateName());
    	String name = form.getFile().getFileName();
		String filename = formatFilename(prefix, form.getPath(), name);
		
		FileOutputStream stream = new FileOutputStream(new File(filename));
		stream.write(form.getFile().getFileData());
		stream.close();
		
    	jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
    	return null;
	}
	
	public ActionForward showFile(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {

		TemplateMaintActionForm form = (TemplateMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		MessageResources resources = this.getResources(request);
		
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		if (Utility.isImage(form.getFilename())) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
    		jsonResult.put("message", resources.getMessage("error.template.file.nontext"));
			String jsonString = jsonResult.toHtmlString();
			this.streamWebService(response, jsonString);
			return null;
		}
		
		String filename = null;
		BufferedReader reader = null;
        if (form.getTemplateName().equals(Constants.TEMPLATE_BASIC)) {
    		form.setServletResource(true);
        	filename = "/content/template/" + Constants.TEMPLATE_BASIC + form.getPath() + "/" + form.getFilename();
        	ServletContext context = getServlet().getServletContext();
        	InputStream inputStream = context.getResourceAsStream(filename);
        	reader = new BufferedReader(new InputStreamReader(inputStream));
        }
        else {
			String prefix = Utility.getTemplatePrefix(adminBean.getSite(), form.getTemplateName());
			filename = form.getFilename();
			filename = formatFilename(prefix, form.getPath(), filename);
			reader = new BufferedReader(new FileReader(filename));
        }
		
        String editText = "";
        while (true) {
        	String line = reader.readLine();
        	if (line == null) {
        		break;
        	}
        	editText += line + "\n";
        }
        reader.close();

        jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("editText", editText);
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
		return null;
	}

	public ActionForward mkdir(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {

		TemplateMaintActionForm form = (TemplateMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
		MessageResources resources = this.getResources(request);

    	if (form.getDirectory().length() == 0) {
    		jsonResult.put("message", resources.getMessage("error.directory.emptyFilename"));
    		streamErrorJsonMessage(jsonResult, response);
    		return null;
    	}
      
	  	String prefix = Utility.getTemplatePrefix(adminBean.getSite(), form.getTemplateName());
	  	String parentName = formatParentDirectoryName(prefix, form.getPath());
	  	File file = new File(parentName);
	  	String files[] = file.list();
	  	for (int i = 0; i < files.length; i++) {
	  		if (files[i].equals(form.getDirectory().toLowerCase())) {
	    		jsonResult.put("message", resources.getMessage("error.directory.exist"));
	    		streamErrorJsonMessage(jsonResult, response);
	    		return null;
	  		}
	  	}
	  	
	  	String directoryName = formatFilename(prefix, form.getPath(), form.getDirectory());
	  	File directory = new File(directoryName);
	  	if (!directory.mkdir()) {
    		jsonResult.put("message", resources.getMessage("error.directory.create.invalid"));
    		streamErrorJsonMessage(jsonResult, response);
    		return null;
	  	}

	   	jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
    	return null;
	}

	public void showDirectoryContent(HttpServletRequest request, TemplateMaintActionForm form) throws Exception {
		form.setServletResource(false);
		AdminBean adminBean = getAdminBean(request);

        String path = request.getParameter("path");
        if (path == null) {
        	path = form.getPath();
        }
        if (path == null || path.length() == 0) {
        	path = "";
        }
    	form.setPath(path);
    	
    	int index = path.lastIndexOf('/');
    	String previousLevel = path;
    	if (index >= 0) {
    		previousLevel = path.substring(0, index);
    	}
    	else {
    		if (previousLevel.trim().length() > 0) {
    			previousLevel = "";
    		}
    	}
    	form.setParentFullPath(previousLevel);
    	
    	String tokens[] = path.split("/");
    	Vector<LabelValueBean> pathEntries = new Vector<LabelValueBean>();
    	if (path.trim().length() > 0) {
	    	for (int i = 0; i < tokens.length; i++) {
	    		pathEntries.add(new LabelValueBean(tokens[i], tokens[i]));
	    	}
    	}
    	form.setPathEntries(pathEntries);
    	form.setRoot(path.equals("/") || path.trim().length() == 0);

    	String prefix = Utility.getTemplatePrefix(adminBean.getSite(), form.getTemplateName());
    	String fullPath = formatParentDirectoryName(prefix, path);
    	File file = new File(fullPath);
    	if (!file.exists()) {
    		logger.info("Automatic recovering.  Create directory " + file.getAbsoluteFile());
    		file.mkdirs();
    	}
    	File entries[] = file.listFiles();
    	Vector<FileDisplayForm> files = new Vector<FileDisplayForm>();
    	// Make sure directory is sorted to the top
    	for (int i = 0; i < entries.length; i++) {
    		if (!entries[i].isDirectory()) {
    			continue;
    		}
    		FileDisplayForm fileForm = new FileDisplayForm();
    		fileForm.setImage(false);
    		fileForm.setDirectory(entries[i].isDirectory());
    		String fp = path;
    		if (fp.length() > 0) {
    			fp += "/";
    		}
    		fp += entries[i].getName();
    		fileForm.setFullPath(fp);
    		fileForm.setFilename(entries[i].getName());
    		fileForm.setSize(entries[i].length());
    		fileForm.setModificationDate(Format.getFullDatetime(new Date(entries[i].lastModified())));
    		files.add(fileForm);
    	}
    	for (int i = 0; i < entries.length; i++) {
    		if (entries[i].isDirectory()) {
    			continue;
    		}
    		FileDisplayForm fileForm = new FileDisplayForm();
    		fileForm.setImage(Utility.isImage(entries[i].getName()));
    		fileForm.setDirectory(entries[i].isDirectory());
    		String fp = path;
    		if (fp.length() > 0) {
    			fp += "/";
    		}
    		fp += entries[i].getName();
    		fileForm.setFullPath(fp);
    		fileForm.setFilename(entries[i].getName());
    		fileForm.setSize(entries[i].length());
    		fileForm.setModificationDate(Format.getFullDatetime(new Date(entries[i].lastModified())));
    		files.add(fileForm);
    	}
        form.setFiles(files);
	}

	public void showDirectoryContentFromServlet(HttpServletRequest request, TemplateMaintActionForm form) throws Exception {
		form.setServletResource(true);
        String path = request.getParameter("path");
        if (path == null) {
        	path = form.getPath();
        }
        if (path == null || path.length() == 0) {
        	path = "";
        }
    	form.setPath(path);
    	
    	int index = path.lastIndexOf('/');
    	String previousLevel = path;
    	if (index >= 0) {
    		previousLevel = path.substring(0, index);
    	}
    	else {
    		if (previousLevel.trim().length() > 0) {
    			previousLevel = "";
    		}
    	}
    	form.setParentFullPath(previousLevel);
    	
    	String tokens[] = path.split("/");
    	Vector<LabelValueBean> pathEntries = new Vector<LabelValueBean>();
    	if (path.trim().length() > 0) {
	    	for (int i = 0; i < tokens.length; i++) {
	    		pathEntries.add(new LabelValueBean(tokens[i], tokens[i]));
	    	}
    	}
    	form.setPathEntries(pathEntries);
    	form.setRoot(path.equals("/") || path.trim().length() == 0);
    	
    	ServletContext context = getServlet().getServletContext();
    	Object paths[] = context.getResourcePaths("/content/template/basic/" + path).toArray();
    	Vector<FileDisplayForm> files = new Vector<FileDisplayForm>();
    	for (int i = 0; i < paths.length; i++) {
    		String entry = (String) paths[i];
    		if (!entry.endsWith("/")) {
    			continue;
    		}
    		String name = entry.substring(0, entry.length() - 1);
    		index = name.lastIndexOf("/");
    		if (index >= 0) {
    			name = name.substring(index + 1);
    		}
    		
    		FileDisplayForm fileForm = new FileDisplayForm();
    		fileForm.setImage(false);
    		fileForm.setDirectory(true);
    		String fp = path;
    		if (fp.length() > 0) {
    			fp += "/";
    		}
    		fp += name;
    		fileForm.setFullPath(fp);
    		fileForm.setFilename(name);
    		fileForm.setSize(0);
    		fileForm.setModificationDate("");
    		files.add(fileForm);
    	}
    	for (int i = 0; i < paths.length; i++) {
    		String entry = (String) paths[i];
    		if (entry.endsWith("/")) {
    			continue;
    		}
    		String name = entry;
    		index = name.lastIndexOf("/");
    		if (index >= 0) {
    			name = name.substring(index + 1);
    		}
    		
    		FileDisplayForm fileForm = new FileDisplayForm();
    		fileForm.setImage(Utility.isImage(entry));
    		fileForm.setDirectory(false);
    		String fp = path;
    		if (fp.length() > 0) {
    			fp += "/";
    		}
    		fp += name;
    		fileForm.setFullPath(fp);
    		fileForm.setFilename(name);
    		fileForm.setSize(0);
    		fileForm.setModificationDate("");
    		files.add(fileForm);
    	}
        form.setFiles(files);

        String filename = request.getParameter("filename");
        form.setFilename(filename);
        if (filename != null && filename.length() > 0) {
        	String name = "/content/template/basic/";
        	if (path.length() > 0) {
        		name += path + "/";
        	}
        	name += filename;
        	InputStream input = context.getResourceAsStream(name);
        	StringBuffer editText = new StringBuffer();
        	byte buffer[] = new byte[1024];
        	while (true) {
        		int size = input.read(buffer);
        		if (size == -1) {
        			break;
        		}
        		editText.append(new String(buffer, 0, size));
        	}
        	input.close();
	        form.setEditText(editText.toString());
        }
	}
	
	
	public String formatParentDirectoryName(String prefix, String path) {
    	String filename = prefix + path;
    	if (!filename.endsWith("/")) {
    		filename += "/";
    	}
    	return filename;
	}
	
	public String formatFilename(String prefix, String path, String name) {
    	String filename = formatParentDirectoryName(prefix, path);
    	filename += name;
    	return filename;
	}
	
    public ActionMessages validate(TemplateMaintActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	if (form.getFile().getFileName().length() == 0 && form.getTemplateName().length() == 0) {
       		errors.add("file", new ActionMessage("error.file.emptyFilename"));
    	}
    	return errors;
    }
	
    public ActionMessages validateUploadfile(TemplateMaintActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	if (form.getFile().getFileName().length() == 0) {
       		errors.add("uploadfile", new ActionMessage("error.file.emptyFilename"));
    	}
    	if (form.getFile().getFileSize() > Constants.FILESIZE_LIMIT) {
       		errors.add("uploadfile", new ActionMessage("error.file.limit", Constants.FILESIZE_LIMIT));
    	}
    	return errors;
    }
	
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        map.put("upload", "upload");
        map.put("edit", "edit");
        map.put("save", "save");
        map.put("savefile", "savefile");
        map.put("uploadfile", "uploadfile");
        map.put("remove", "remove");
        map.put("removeFile", "removeFile");
        map.put("mkdir", "mkdir");
        map.put("showFile", "showFile");
        return map;
    }
}
