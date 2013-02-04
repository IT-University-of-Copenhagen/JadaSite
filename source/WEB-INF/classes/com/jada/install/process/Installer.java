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

package com.jada.install.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.jada.system.ApplicationGlobal;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;
import com.jada.xml.databaseUpgrade.DatabaseUpgradeQuery;
import com.jada.xml.databaseUpgrade.DatabaseUpgradeRecord;

public class Installer {
	static Installer instance = null;
	Connection connection = null;
	InstallActionForm form;
	String servletLocation = null;
	ServletContext servletContext = null;
    Logger logger = Logger.getLogger(Installer.class);
	
	static public Installer getInstance() {
		if (instance != null) {
			return instance;
		}
		instance = new Installer();
		return instance;
	}
	
	public void init(ServletContext servletContext) {
		this.servletContext = servletContext;
		servletLocation = Utility.getServletLocation(servletContext);
	}
	
	public void setInstallActionForm(InstallActionForm form) {
		this.form = form;
	}
	
	public Connection getConnection() throws SQLException {
		if (connection != null) {
			return connection;
		}
		connection = DriverManager.getConnection(form.getUrl(), form.getUsername(), form.getPassword());
		return connection;
	}
	
	public void testDatabaseConnectivity() throws Exception {
		String url = null;
		if (Format.isNullOrEmpty(form.getUrl())) {
			url = "jdbc:mysql://" + 
			form.getDbHost() + 
				  ":" + 
				  form.getDbPort() + 
				  "/" +
				  form.getDbName() +
				  "?relaxAutoCommit=true&emulateLocators=true&autoReconnect=true&characterEncoding=UTF-8";
			form.setUrl(url);
		}
		else {
			url = form.getUrl();
		}
		Class.forName(form.getDriver());
		getConnection();
	}
	
	public Properties getCurrentProperties() throws IOException {
		Properties properties = new Properties();
		FileInputStream in = new FileInputStream(new File(servletLocation + Constants.CONFIG_PROPERTIES_FILENAME));
		properties.load(in);
		return properties;
	}
	
	public void writeConfig() throws IOException {
		Properties properties = new Properties();
		properties.setProperty(Constants.CONFIG_PROPERTY_DRIVER, form.getDriver());
		properties.setProperty(Constants.CONFIG_PROPERTY_URL, form.getUrl());
		properties.setProperty(Constants.CONFIG_PROPERTY_USER, form.getUsername());
		properties.setProperty(Constants.CONFIG_PROPERTY_PASS, form.getPassword());
		properties.setProperty(Constants.CONFIG_PROPERTY_ENCRYPTIONKEY, form.getEncryptionKey());
		properties.setProperty(Constants.CONFIG_PROPERTY_WORKINGDIRECTORY, form.getWorkingDirectory());
		properties.setProperty(Constants.CONFIG_PROPERTY_LOGDIRECTORY, form.getLog4jDirectory());

		FileOutputStream out = new FileOutputStream(new File(servletLocation + Constants.CONFIG_PROPERTIES_FILENAME));
		properties.store(out, null);
	}
	
	public boolean isDatabaseCreated() throws Exception {
		String sql = "";
		ResultSet result = null;
		
		try {
			int count = 0;
			Connection connection = getConnection();
			sql = "select   count(*) " +
				  "from     control ";
			result = connection.createStatement().executeQuery(sql);
			if (result.next()) {
				count = result.getInt(1);
			}
			if (count > 0) {
				return true;
			}
			
			sql = "select   count(*) " +
			      "from     site ";
			result = connection.createStatement().executeQuery(sql);
			if (result.next()) {
				count = result.getInt(1);
			}
			if (count > 0) {
				return true;
			}
		}
		catch (SQLException e) {
    		// Object does not exist error
    		if (e.getSQLState().equals("42S02")) {
    			return false;
    		}
    		throw e;
		}
		return false;
	}
	
	public void setDatabaseCreated() throws Exception {
    	String sql = "insert " +
			  	     "into   control " +
			         "values ('dbVersion', '" + ApplicationGlobal.getDbVersion() + "')";
    	connection.createStatement().executeUpdate(sql);
	}
	
	public void installDatabase() throws Exception {
		/*
		 * Prepare schema for new table.
		 */
    	Vector<String> ddl = new Vector<String>();
    	InputStream stream = getClass().getResourceAsStream("/schema/schema.ddl");
    	BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuffer buffer = new StringBuffer();
    	while (true) {
    		String line = reader.readLine();
    		if (line == null) {
    			break;
    		}
    		line = line.trim();
    		if (line.length() == 0) {
    			continue;
    		}
    		buffer.append(line + " ");
    		if (line.endsWith(";")) {
    			if (buffer.toString().indexOf("drop") == -1) {
    				ddl.add(buffer.toString());
    			}
    			buffer = new StringBuffer();
    		}
    	}
    	/*
    	 * Only load create statements.
    	 */
    	Enumeration<?> enumeration = ddl.elements();
    	while (enumeration.hasMoreElements()) {
    		String sql = (String) enumeration.nextElement();
			if (!ltrim(sql).toLowerCase().startsWith("create")) {
				continue;
			}
			connection.createStatement().executeUpdate(sql);
    	}
    	/*
    	 * Load data
    	 */
    	stream = getClass().getResourceAsStream("/schema/load.sql");
    	reader = new BufferedReader(new InputStreamReader(stream));
    	String sql = "";
    	String line = null;
    	while (true) {
    		line = reader.readLine();
    		if (line == null) {
    			break;
    		}
    		if (line.startsWith("--")) {
    			continue;
    		}
    		if (line.startsWith("/*")) {
    			continue;
    		}
    		if (line.startsWith("#")) {
    			continue;
    		}  		
    		if (line.trim().length() == 0) {
    			continue;
    		}
    		sql += line;
/*
    		if (sql.startsWith("INSERT") || sql.startsWith("LOCK")) {
    			sql = sql.replaceAll("\"", "`");
    		}
 */
    		if (sql.trim().endsWith(";")) {
    			sql = sql.replaceAll("\t", " ");
	    		System.out.println("[" + sql + "]");
    			connection.createStatement().executeUpdate(sql);
    			sql = "";
    		}
    	}
    	/*
    	 * Load indexes and constraints.
    	 */
    	enumeration = ddl.elements();
    	while (enumeration.hasMoreElements()) {
    		sql = (String) enumeration.nextElement();
			if (ltrim(sql).toLowerCase().startsWith("create")) {
				continue;
			}
			System.out.println(sql);
			connection.createStatement().executeUpdate(sql);
    	}

		/*
		 * Update user password
		 */
    	AESEncoder.init(form.getEncryptionKey());
    	System.out.println("updating user password");
    	String userPassword = AESEncoder.getInstance().encode("admin");
    	sql = "update  user " +
    		  "set     user_password = '" + userPassword + "'";
    	connection.createStatement().executeUpdate(sql);
	}
	
	public void upgradeDatabase() throws Exception {
    	String sql = null;
    	Statement statement = null;
    	Connection connection = getConnection();
    	
 		float dbVersion = 0;
		sql = "select * " + 
			  "from   control " + 
			  "where  control_key = 'dbVersion'";
		statement = connection.createStatement();
		ResultSet result = statement.executeQuery(sql);
		while (result.next()) {
			String value = result.getString("control_value");
			dbVersion = Float.valueOf(value);
		}
		logger.info("Upgrading from version - " + dbVersion);
		
		DatabaseUpgradeRecord databaseUpgradeRecords[] = DatabaseUpgradeQuery.getInstance().getDatabaseUpgradeRecords();
		for (DatabaseUpgradeRecord databaseUpgradeRecord : databaseUpgradeRecords) {
			float upgradeVersion = Float.valueOf(databaseUpgradeRecord.getDbVersion());
			if (upgradeVersion <= dbVersion) {
				continue;
			}
			try {
				if (databaseUpgradeRecord.getDbQuery() != null) {
					logger.info("Query version " + upgradeVersion + " [" + databaseUpgradeRecord.getDbQuery() + "]");
					connection.createStatement().executeUpdate(databaseUpgradeRecord.getDbQuery());
				}
				else {
					logger.info("Query version " + upgradeVersion + " [" + databaseUpgradeRecord.getClassName() + "." + databaseUpgradeRecord.getMethod() + "]");
					Class<?> c = Class.forName(databaseUpgradeRecord.getClassName());
					Class<?> types[] = {Connection.class};
					Method method = c.getMethod(databaseUpgradeRecord.getMethod(), types);
					
					Object object = Class.forName(databaseUpgradeRecord.getClassName()).newInstance();
					Object arguments[] = {connection};
					method.invoke(object, arguments);
				}
			}
			catch (Exception e) {
				boolean ignore = false;
				if (databaseUpgradeRecord.getIgnoreError() != null) {
					if (databaseUpgradeRecord.getIgnoreError().equalsIgnoreCase("Y")) {
						ignore = true;
					}
				}
				if (!ignore) {
					throw e;
				}
				else {
					logger.error("The following error has been ignored", e);
				}
			}
		}
		
		sql = "delete " +
		  	  "from   control " +
		  	  "where  control_key = 'dbVersion'";
		connection.createStatement().executeUpdate(sql);
	}
	
	public boolean isInstallRequired() {
		return ApplicationGlobal.isRequireInstall();
	}
	
	public boolean isInstallCompleted() {
		try {
			String filename = servletLocation + Constants.CONFIG_PROPERTIES_FILENAME;
			File file = new File(filename);
			if (file.exists()) {
				return true;
			}
		}
		catch (Exception e) {
			return false;
		}
		return false;
	}
	
	public Properties getConfigProperties() throws IOException {
		String filename = servletLocation + Constants.CONFIG_PROPERTIES_FILENAME;
		File file = new File(filename);
		FileInputStream input = new FileInputStream(file);
		Properties properties = new Properties();
		properties.load(input);
		return properties;
	}
	
	public boolean isValidDirectory(String value) {
		String filename = "_00000";
		File file = new File(value + "/" + filename);
		try {
			FileWriter writer = new FileWriter(file);
			writer.append(' ');
			writer.close();
			file.delete();
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}
	
    private String ltrim(String input) {
 	   if (input == null) {
 		   return null;
 	   }
 	   for (int i = 0; i < input.length(); i++) {
 		   if (input.charAt(i) != ' ') {
 			   return input.substring(i);
 		   }
 	   }
 	   return "";
	}
}
