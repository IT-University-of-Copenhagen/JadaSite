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

import org.apache.struts.action.ActionForm;

public class InstallActionForm extends ActionForm {
	private static final long serialVersionUID = -6610408519228247313L;
	String contextPath;
	boolean error;
	boolean installCompleted;
	boolean databaseExist;
	boolean upgrade;
	String driver;
	String username;
	String password;
	String dbHost;
	String dbPort;
	String dbName;
	String url;
	String workingDirectory;
	String log4jDirectory;
	String detailLog;
	String encryptionKey;
	public String getDbHost() {
		return dbHost;
	}
	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDbPort() {
		return dbPort;
	}
	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}
	public String getDetailLog() {
		return detailLog;
	}
	public void setDetailLog(String detailLog) {
		this.detailLog = detailLog;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getContextPath() {
		return contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getWorkingDirectory() {
		return workingDirectory;
	}
	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}
	public boolean isInstallCompleted() {
		return installCompleted;
	}
	public void setInstallCompleted(boolean installCompleted) {
		this.installCompleted = installCompleted;
	}
	public boolean isDatabaseExist() {
		return databaseExist;
	}
	public void setDatabaseExist(boolean databaseExist) {
		this.databaseExist = databaseExist;
	}
	public String getLog4jDirectory() {
		return log4jDirectory;
	}
	public void setLog4jDirectory(String log4jDirectory) {
		this.log4jDirectory = log4jDirectory;
	}
	public String getEncryptionKey() {
		return encryptionKey;
	}
	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}
	public boolean isUpgrade() {
		return upgrade;
	}
	public void setUpgrade(boolean upgrade) {
		this.upgrade = upgrade;
	}
}
