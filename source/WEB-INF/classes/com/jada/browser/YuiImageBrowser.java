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

package com.jada.browser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.jada.util.Format;
import com.jada.util.JSONEscapeObject;

public class YuiImageBrowser extends HttpServlet {
	private static final long serialVersionUID = 3816477876067328338L;
	static final String ACTION_LISTCURRENT = "listCurrent";
	static final String ACTION_LISTPREVIOUS = "listPrevious";
	static final String ACTION_LISTNEXT = "listNext";
	static final String ACTION_REMOVEFILE = "removeFile";
	static final String ACTION_UPLOADFILE = "uploadFile";
	static final String ACTION_CREATEFOLDER = "createFolder";
	static String baseDir;
	static SecurityManager securityManager;
	static int maxsize = 0;
	static boolean externalInit = false;
	static String imageExtensions[] = {"jpg", "jpeg", "gif"};
    Logger logger = Logger.getLogger(YuiImageBrowser.class);
	
	public void init() throws ServletException {
		try {
			if (!externalInit) {
				YuiImageBrowser.baseDir = getInitParameter("baseDir");
				String smClassName = getInitParameter("securityMangerClassName");
				if (smClassName != null && smClassName.trim().length() > 0) {
					YuiImageBrowser.securityManager = (SecurityManager) Class.forName(smClassName).newInstance();
				}
				String s = getInitParameter("maxsize");
				if (s != null && s.length() > 0) {
					maxsize = Integer.parseInt(s);
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new ServletException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new ServletException(e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}
	
	static public void customInit(String baseDir, SecurityManager securityManager, int maxsize) throws Exception {
		Logger.getLogger(YuiImageBrowser.class).info("customInit");
		YuiImageBrowser.baseDir = baseDir;
		YuiImageBrowser.securityManager = securityManager;
		YuiImageBrowser.maxsize = maxsize;
		externalInit = true;
		
		File file = new File(baseDir);
		if (!file.exists()) {
			if (!file.mkdir()) {
				throw new Exception("Unable to create working directory");
			}
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setContentType("text/html; charset=utf-8");
			response.addHeader("Pragma", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
			response.addHeader("Cache-Control", "must-revalidate");
			response.addHeader("Expires", "Mon, 8 Aug 2006 10:00:00 GMT"); 
	        PrintWriter out = response.getWriter();
			if (securityManager != null) {
				if (!securityManager.isAllowAccess(request)) {
					out.println("Access denied.  You are not allowed to access Image Browser service.");
					out.flush();
					out.close();
					return;
				}
			}
		
			String action = request.getParameter("action");
			String currentFolder = request.getParameter("currentFolder");
			String targetFolder = request.getParameter("targetFolder");
			String result = null;
			if (action.equals(ACTION_LISTCURRENT) || action.equals(ACTION_LISTNEXT) || action.equals(ACTION_LISTPREVIOUS)) {
				result = performActionList(action, currentFolder, targetFolder, request);
			}
			if (action.equals(ACTION_UPLOADFILE)) {
				result = performUpload(request, currentFolder);
			}
			if (action.equals(ACTION_REMOVEFILE)) {
				result = performRemove(request, currentFolder);
			}
			if (action.equals(ACTION_CREATEFOLDER)) {
				result = performCreate(request, currentFolder);
			}
			response.setContentLength(result.length());
	        out.print(result);
	        out.flush();
	        out.close();
		}
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String performCreate(HttpServletRequest request, String currentFolder) throws Exception {
		String result = null;
		JSONEscapeObject JSONEscapeObject = new JSONEscapeObject();
		String folder = request.getParameter("folder");
		String filename = getBaseDir(request);
		if (!currentFolder.equals("/")) {
			filename += currentFolder;
		}
		filename += "/" + folder;
		File file = new File(filename);
		if (file.exists()) {
			JSONEscapeObject.put("status", "failed");
			JSONEscapeObject.put("message", "Folder already exist");
			return JSONEscapeObject.toHtmlString();
		}
		if (!file.mkdir()) {
			JSONEscapeObject.put("status", "failed");
			JSONEscapeObject.put("message", "Unable to create directory");
			return JSONEscapeObject.toHtmlString();
		}
		JSONEscapeObject.put("status", "success");
		result = JSONEscapeObject.toHtmlString();
		return result;
	}
	
	public String performRemove(HttpServletRequest request, String currentFolder) throws Exception {
		String result = null;
		JSONEscapeObject JSONEscapeObject = new JSONEscapeObject();
		String filenames[] = request.getParameterValues("filenames");
		if (filenames != null) {
			for (int i = 0; i < filenames.length; i++) {
				String filename = getBaseDir(request);
				if (currentFolder.equals("/")) {
					filename += currentFolder;
				}
				filename += filenames[i];
				File file = new File(filename);
				if (!file.exists()) {
					JSONEscapeObject.put("status", "failed");
					JSONEscapeObject.put("message", "File " + filename + " does not exist");
					return JSONEscapeObject.toHtmlString();
				}
				removeFile(filename);
			}
		}
		JSONEscapeObject.put("status", "success");
		result = JSONEscapeObject.toHtmlString();
		return result;
	}
	
	public String performUpload(HttpServletRequest request, String currentFolder) throws Exception {
		String result = null;
		JSONEscapeObject JSONEscapeObject = new JSONEscapeObject();
		try {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			if (maxsize > 0) {
				upload.setSizeMax(maxsize);
			}
			List<?> items = upload.parseRequest(request);
			Iterator<?> iterator = items.iterator();
			while (iterator.hasNext()) {
				FileItem item = (FileItem) iterator.next();
				if (!item.isFormField()) {
					String fileName = getBaseDir(request);
					fileName += currentFolder;
					fileName += "/" + trimFileName(item.getName());
					File file = new File(fileName);
					item.write(file);
				}
			}
			JSONEscapeObject.put("status", "success");
		}
		catch (Exception e) {
			e.printStackTrace();
			JSONEscapeObject.put("status", "failed");
			JSONEscapeObject.put("message", e.getMessage());
		}
		result = JSONEscapeObject.toHtmlString();
		return result;
	}
	
	private String trimFileName(String input) {
		String result = "";
		for (int i = input.length() - 1; i >= 0; i--) {
			char c = input.charAt(i);
			if (c == '/' || c == '\\' || c == ':') {
				break;
			}
			result = String.valueOf(c) + result;
		}
		if (result.length() == 0) {
			result = "new";
		}
		return result;
	}
	
	public String performActionList(String action, String currentFolder, String targetFolder, HttpServletRequest request) throws Exception {
		String result = null;
		String target = currentFolder;
		if (action.equals(ACTION_LISTPREVIOUS)) {
			int index = currentFolder.lastIndexOf('/');
	    	target = currentFolder;
	    	if (index >= 0) {
	    		target = currentFolder.substring(0, index);
	    	}
	    	if (target.trim().length() == 0) {
	    		target = "/";
	    	}
		}
		if (action.equals(ACTION_LISTNEXT)) {
			if (!currentFolder.endsWith("/")) {
				currentFolder += "/";
			}
			target = currentFolder + targetFolder;
		}
		String relativeTarget = target;
		target = getBaseDir(request) + target;
		
    	JSONEscapeObject JSONEscapeObject = new JSONEscapeObject();
    	JSONEscapeObject.put("folder", relativeTarget);
    	Vector<JSONEscapeObject> folderList = new Vector<JSONEscapeObject>();
    	String tokens[] = relativeTarget.split("/");
    	String folder = "/";
    	for (int i = 0; i < tokens.length; i++) {
    		if (tokens[i].length() == 0) {
    			continue;
    		}
    		JSONEscapeObject jsonLevel = new JSONEscapeObject();
    		jsonLevel.put("name", tokens[i]);
    		if (folder.length() > 1) {
    			folder += "/";
    		}
    		folder += tokens[i];
    		jsonLevel.put("folder", folder);
    		folderList.add(jsonLevel);
    	}
    	JSONEscapeObject.put("breadcrumb", folderList);
    	
    	File file = new File(target);
    	File entries[] = file.listFiles();
    	Vector<JSONEscapeObject> files = new Vector<JSONEscapeObject>();
    	for (int i = 0; i < entries.length; i++) {
    		File entry = entries[i];
    		if (!entry.isDirectory()) {
    			continue;
    		}
    		JSONEscapeObject jsonFile = new JSONEscapeObject();
    		jsonFile.put("name", entry.getName());
    		jsonFile.put("isDirectory", entry.isDirectory());
    		jsonFile.put("size", entry.length());
    		jsonFile.put("isImage", false);
    		Date lastUpdateOn = new Date(entry.lastModified());
    		jsonFile.put("lastUpdateOn", Format.getFullDatetime(lastUpdateOn));
    		files.add(jsonFile);
    	}
    	for (int i = 0; i < entries.length; i++) {
    		File entry = entries[i];
    		if (entry.isDirectory()) {
    			continue;
    		}
    		JSONEscapeObject jsonFile = new JSONEscapeObject();
    		jsonFile.put("name", entry.getName());
    		jsonFile.put("isDirectory", entry.isDirectory());
    		jsonFile.put("size", entry.length());
    		if (isImage(entry.getName())) {
    			jsonFile.put("isImage", true);
    		}
    		else {
    			jsonFile.put("isImage", false);
    		}
    		Date lastUpdateOn = new Date(entry.lastModified());
    		jsonFile.put("lastUpdateOn", Format.getFullDatetime(lastUpdateOn));
    		files.add(jsonFile);
    	}
    	JSONEscapeObject.put("files", files);
    	
    	result = JSONEscapeObject.toHtmlString();
		return result;
	}
	
	private boolean isImage(String filename) {
		String tokens[] = filename.split("\\.");
		if (tokens.length > 1) {
			for (int i = 0; i < imageExtensions.length; i++) {
				if (imageExtensions[i].equalsIgnoreCase(tokens[1])) {
					return true;
				}
			}
		}
		return false;
	}
	
	static private boolean removeFile(String directory) throws Exception {
		File file = new File(directory);
		File files[] = file.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					removeFile(files[i].getAbsolutePath());
				}
				else {
					if (!files[i].delete()) {
						return false;
					}
				}
			}
		}
		if (!file.delete()) {
			return false;
		}
		return true;
	}
	
	protected String getBaseDir(HttpServletRequest request) {
		return baseDir;
	}
}
