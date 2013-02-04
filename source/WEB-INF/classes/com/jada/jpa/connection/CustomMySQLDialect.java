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

package com.jada.jpa.connection;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;

public class CustomMySQLDialect extends MySQL5InnoDBDialect {
	public CustomMySQLDialect() {
		super();
		registerFunction("date_add_interval", new SQLFunctionTemplate( Hibernate.DATE, "date_add(?1, INTERVAL ?2 ?3)" ) );
		registerFunction("to_date", new SQLFunctionTemplate( Hibernate.DATE, "str_to_date(?1, ?2)" ) );
	}
	
	public String openBlobSelectQuote() {
		return "`";
	}
		 
	public String closeBlobSelectQuote() {
		return "`";
	}
}
