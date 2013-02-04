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

package com.jada.jpa.util;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import com.jada.xml.persistence.Persistence;
import com.jada.xml.persistence.PersistenceLoader;

public class JpaSchemaExport {
	String dialectName = null;
	String fileName = null;
	
	public JpaSchemaExport(String dialectName, String fileName) {
		this.dialectName = dialectName;
		this.fileName = fileName;
	}

	public void export() throws Exception {
		AnnotationConfiguration configuration = new AnnotationConfiguration();
		configuration.setProperty("hibernate.hbm2ddl.auto","create");
		PersistenceLoader persistenceLoader = PersistenceLoader.getInstance();
		Persistence persistence = persistenceLoader.getPersistence();
		for (String className : persistence.getPersistenceUnit().getClassNames()) {
			Class<?> c = Class.forName(className);
			configuration.addAnnotatedClass(c);
		}
		
		configuration.setProperty("hibernate.dialect", dialectName);
		SchemaExport exporter = new SchemaExport(configuration);
		exporter.setDelimiter(";");
		exporter.setOutputFile(fileName);
		
		boolean script = true;
		boolean export = false;
		boolean justDrop = false;
		boolean justCreate = false;
		exporter.execute(script, export, justDrop, justCreate);
	}

	public static void main(String[] args) {
		try {
			String dialectName = args[0];
			String fileName = args[1];
			JpaSchemaExport jpaExport = new JpaSchemaExport(dialectName, fileName);
			jpaExport.export();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
