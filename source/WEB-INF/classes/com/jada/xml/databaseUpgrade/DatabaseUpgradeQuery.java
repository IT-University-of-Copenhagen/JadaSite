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

package com.jada.xml.databaseUpgrade;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.XMLContext;
import org.xml.sax.InputSource;

public class DatabaseUpgradeQuery {
	static DatabaseUpgradeQuery instance = null;
	DatabaseUpgradeQueries databaseUpgradeQueries = null;
	
	static public DatabaseUpgradeQuery getInstance() throws Exception {
		if (instance == null) {
			synchronized(DatabaseUpgradeQuery.class) {
				instance = new DatabaseUpgradeQuery();
			}
		}
		return instance;
	}
	
	public DatabaseUpgradeQuery() throws Exception {
		Mapping mapping = new Mapping();
		InputSource input = new InputSource(getClass().getResourceAsStream("/com/jada/xml/databaseUpgrade/DatabaseUpgradeQueriesMapping.xml"));
		mapping.loadMapping(input);
		
		XMLContext context = new XMLContext();
		context.addMapping(mapping);
		
		InputStream stream = getClass().getResourceAsStream("/com/jada/xml/databaseUpgrade/DatabaseUpgradeQuery.xml");
    	BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

    	Unmarshaller unmarshaller = context.createUnmarshaller();
    	unmarshaller.setClass(DatabaseUpgradeQueries.class);
    	
    	databaseUpgradeQueries = (DatabaseUpgradeQueries) unmarshaller.unmarshal(reader);
	}
	
	public DatabaseUpgradeRecord[] getDatabaseUpgradeRecords() {
		return databaseUpgradeQueries.getDatabaseUpgradeRecord();
	}
}
