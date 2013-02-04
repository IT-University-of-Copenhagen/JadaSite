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

public class ApplicationGlobal {
	static String contextPath;
	static String workingDirectory;
	static String version;
	static String dbVersion;
	static boolean localTesting;
	static boolean requireInstall;
	static boolean installCompleted;
	static boolean compassDatabaseStore;
	static String temmplateEngineClassName;
	static String dataApiClassName;
	static boolean productExternal;
	public static String getWorkingDirectory() {
		return workingDirectory;
	}
	public static void setWorkingDirectory(String workingDirectory) {
		ApplicationGlobal.workingDirectory = workingDirectory;
	}
	public static String getContextPath() {
		return contextPath;
	}
	public static void setContextPath(String contextPath) {
		ApplicationGlobal.contextPath = contextPath;
	}
	public static String getVersion() {
		return version;
	}
	public static void setVersion(String version) {
		ApplicationGlobal.version = version;
	}
	public static String getDbVersion() {
		return dbVersion;
	}
	public static void setDbVersion(String dbVersion) {
		ApplicationGlobal.dbVersion = dbVersion;
	}
	public static boolean isLocalTesting() {
		return localTesting;
	}
	public static void setLocalTesting(boolean localTesting) {
		ApplicationGlobal.localTesting = localTesting;
	}
	public static boolean isRequireInstall() {
		return requireInstall;
	}
	public static void setRequireInstall(boolean requireInstall) {
		ApplicationGlobal.requireInstall = requireInstall;
	}
	public static boolean isInstallCompleted() {
		return installCompleted;
	}
	public static void setInstallCompleted(boolean installCompleted) {
		ApplicationGlobal.installCompleted = installCompleted;
	}
	public static boolean isCompassDatabaseStore() {
		return compassDatabaseStore;
	}
	public static void setCompassDatabaseStore(boolean compassDatabaseStore) {
		ApplicationGlobal.compassDatabaseStore = compassDatabaseStore;
	}
	public static String getTemmplateEngineClassName() {
		return temmplateEngineClassName;
	}
	public static void setTemmplateEngineClassName(String temmplateEngineClassName) {
		ApplicationGlobal.temmplateEngineClassName = temmplateEngineClassName;
	}
	public static boolean isProductExternal() {
		return productExternal;
	}
	public static void setProductExternal(boolean productExternal) {
		ApplicationGlobal.productExternal = productExternal;
	}
	public static String getDataApiClassName() {
		return dataApiClassName;
	}
	public static void setDataApiClassName(String dataApiClassName) {
		ApplicationGlobal.dataApiClassName = dataApiClassName;
	}
}
