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

package com.jada.system;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.PropertyConfigurator;

import com.jada.browser.YuiImageBrowser;
import com.jada.dao.CacheDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Utility;
import com.jada.install.process.Installer;

public class Initializer extends HttpServlet {
	private static final long serialVersionUID = -5724816778527823325L;
	ServletContext context = null;
	
	public void init(ServletConfig config) {
		String contextPath = null;
		Installer installer = null;
		context = config.getServletContext();

		try {
			URL rootURL = context.getResource ("/");
			File rootFile = new File(rootURL.toString());
			contextPath = rootFile.getName ();
			ApplicationGlobal.setContextPath(contextPath);
		}
		catch (Exception e) {
			log("Problem initializing JadaSite.", e);
			log("JadaSite startup aborted.");
			return;
		}
		
    	String filename = Utility.getServletLocation(config.getServletContext()) + "jada.properties";
    	File file = new File(filename);
    	if (file.exists()) {
    		try {
    			if (!file.delete()) {
    				log("Unable to remove " + filename);
        			log("This file can be removed manually");
        			return;
    			}
    		}
    		catch (SecurityException e) {
    			log("Unable to remove " + filename, e);
    			log("This file can be removed manually");
    			return;
    		}
    	}

		
    	installer = Installer.getInstance();
    	installer.init(config.getServletContext());
    	
		ApplicationGlobal.setVersion(config.getInitParameter("version"));
		ApplicationGlobal.setDbVersion(config.getInitParameter("dbVersion"));
		
		String debug = config.getInitParameter(Constants.CONFIG_PROPERTY_LOCALTESTING);
		if (debug != null && debug.toLowerCase().equals("true")) {
			ApplicationGlobal.setLocalTesting(true);
		}

		String driver = config.getInitParameter(Constants.CONFIG_PROPERTY_DRIVER);
    	String url = config.getInitParameter(Constants.CONFIG_PROPERTY_URL);
    	String user = config.getInitParameter(Constants.CONFIG_PROPERTY_USER);
    	String pass = config.getInitParameter(Constants.CONFIG_PROPERTY_PASS);
		String encryptionKey = config.getInitParameter(Constants.CONFIG_PROPERTY_ENCRYPTIONKEY);
		String workingDirectory = config.getInitParameter(Constants.CONFIG_PROPERTY_WORKINGDIRECTORY);
		String log4jDirectory = config.getInitParameter(Constants.CONFIG_PROPERTY_LOGDIRECTORY);
		
		boolean requireInstall = false;
		boolean installCompleted = installer.isInstallCompleted();
		ApplicationGlobal.setInstallCompleted(installCompleted);
		String value = config.getInitParameter(Constants.CONFIG_PROPERTY_REQUIREINSTALL);
		if (value != null && value.toLowerCase().equals("true")) {
			requireInstall = true;
			ApplicationGlobal.setRequireInstall(true);
		}
		
		if (requireInstall) {
			if (!installCompleted) {
				log("Installation has not been done.  Skipping custom initialization.");
				log("Please proceed to installation and remmember to restart before continue.");
				log("Awaiting installation....");
				return;
			}
			try {
				Properties installProperties = installer.getConfigProperties();
				driver = installProperties.getProperty(Constants.CONFIG_PROPERTY_DRIVER);
				url = installProperties.getProperty(Constants.CONFIG_PROPERTY_URL);
				user = installProperties.getProperty(Constants.CONFIG_PROPERTY_USER);
				pass = installProperties.getProperty(Constants.CONFIG_PROPERTY_PASS);
				encryptionKey = installProperties.getProperty(Constants.CONFIG_PROPERTY_ENCRYPTIONKEY);
				workingDirectory = installProperties.getProperty(Constants.CONFIG_PROPERTY_WORKINGDIRECTORY);
				log4jDirectory = installProperties.getProperty(Constants.CONFIG_PROPERTY_LOGDIRECTORY);
			}
			catch (IOException e) {
				log("Unable to read installation properties.", e);
				log("JadaSite startup aborted.");
				return;	
			}
		}
		
		ApplicationGlobal.setWorkingDirectory(workingDirectory);
		
		/*
		 * Configure log4j configuration information
		 */
		try {
			InputStream input = Initializer.class.getResourceAsStream("/jadasite.log4j.properties");
			Properties log4jProperties = new Properties();
			log4jProperties.load(input);
			if (requireInstall) {
				log4jProperties.put(Constants.CONFIG_LOG4J_PROPERTY, 
									log4jDirectory + "/" + Constants.CONFIG_LOG4J_LOGFILENAME);
				log("Picking up log directory from installation setup - " + log4jDirectory);
			}
			PropertyConfigurator.configure(log4jProperties);
		}
		catch (Exception e) {
			log("Problem loading log4j.properties file.", e);
			log("JadaSite startup aborted.");
			return;
		}

		/*
		 * Initializing database connection.
		 */
		try {
			if (requireInstall) {
				JpaConnection.getInstance().init(driver, url, user, pass);
			}
			else {
				JpaConnection.getInstance().init(config);
			}
		}
		catch (Exception e) {
			log("Unable to initialize JPA connection", e);
			log("JadaSite startup aborted.");
			return;
		}
		
		/*
		 * Initializing encryption key
		 */
		try {
            AESEncoder.init(encryptionKey);
		}
		catch (Exception e) {
			log("Unable to instantiate aes encoder.", e);
			log("JadaSite startup aborted.");
			return;
		}
		
		/*
		 * Initialize custom image browser.
		 */
		try {
			YuiImageBrowser.customInit(workingDirectory, null, 0);
		} catch (Exception e) {
			log("Unable to initialize YuiImageBrowser", e);
			log("JadaSite startup aborted.");
			return;
		}
		
		EntityManager em = null;
		try {
			em = JpaConnection.getInstance().getCurrentEntityManager();
			em.getTransaction().begin();
			
			try {
				Languages.init();
			} catch (Exception e) {
				log("Unable to initialize Languages", e);
				log("JadaSite startup aborted.");
				e.printStackTrace();
			}
			em.getTransaction().commit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
            if (em.isOpen()) {
            	if (em.getTransaction().isActive()) {
            		em.getTransaction().rollback();
            	}
            }
            em.close();
		}
		
		try {
			em = JpaConnection.getInstance().getCurrentEntityManager();
			em.getTransaction().begin();
			CacheDAO.removeAllTransient();
			em.getTransaction().commit();
		}
		catch (Exception e) {
			log("Unable to remove all transient information from cache", e);
			log("JadaSite startup aborted.");
			e.printStackTrace();
		}
		finally {
            if (em.isOpen()) {
            	if (em.getTransaction().isActive()) {
            		em.getTransaction().rollback();
            	}
            }
            em.close();
		}
		
		String compassStoreType = config.getInitParameter(Constants.CONFIG_COMPASS_STORE_TYPE);
		if (compassStoreType == null || compassStoreType.equalsIgnoreCase("file")) {
			ApplicationGlobal.setCompassDatabaseStore(false);
		}
		else {
			ApplicationGlobal.setCompassDatabaseStore(true);
		}
		
		String templateEngineClassName = config.getInitParameter(Constants.CONFIG_TEMPLATE_ENGINE_CLASSNAME);
		if (templateEngineClassName == null) {
			templateEngineClassName = com.jada.content.template.TemplateEngine.class.getName();
		}
		ApplicationGlobal.setTemmplateEngineClassName(templateEngineClassName);
		
		String dataApiClassName = config.getInitParameter(Constants.CONFIG_DATA_API_CLASSNAME);
		if (dataApiClassName == null) {
			dataApiClassName = com.jada.api.DataApi.class.getName();
		}
		ApplicationGlobal.setDataApiClassName(dataApiClassName);
		
		String productCatalogSource = config.getInitParameter(Constants.CONFIG_PRODUCT_CATALOG_SOURCE);
		if (productCatalogSource != null && productCatalogSource.equalsIgnoreCase("external")) {
			ApplicationGlobal.setProductExternal(true);
		}
	}
	
	public void log(String input) {
		context.log("JadaSite: " + input);
	}
	
	public void log(String input, Exception e) {
		context.log("JadaSite: " + input, e);
	}
}
