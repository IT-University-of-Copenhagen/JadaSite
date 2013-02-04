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

package com.jada.testing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Gen {
	public void create() {
		try {
		    BufferedReader in = new BufferedReader(new FileReader("d://tmp/in.txt"));
		    String input;
		    while ((input = in.readLine()) != null) {
		    	if (input.length() == 0) {
		    		break;
		    	}
		    	String tokens[] = input.split("\t");
		    	PrintWriter out = new PrintWriter(new FileWriter("d://tmp/code/" + tokens[0] + "DAO.java"));
		    	out.println("package com.jada.dao;");
		    	out.println();
		    	out.println("import javax.persistence.EntityManager;");
		    	out.println("import com.jada.jpa.connection.JpaConnection;");
		    	out.println("import com.jada.hibernate." + tokens[0] + ";");
		    	out.println();
		    	out.println("public class " + tokens[0] + "DAO extends " + tokens[0] + " {");
		    	out.println("	public static " + tokens[0] + " load(String siteId, Long " + tokens[1] + ") throws SecurityException, Exception {");
		    	out.println("    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();");
		    	out.println("		" + tokens[0] + " " + tokens[0].toLowerCase() + " = (" + tokens[0] + ") em.find(" + tokens[0] + ".class, " + tokens[1] + ");");
		    	out.println("		if (!" + tokens[0].toLowerCase() + ".getSiteId().equals(siteId)) {");
		    	out.println("			throw new SecurityException();");
		    	out.println("		}");
		    	out.println("		return " + tokens[0].toLowerCase() + ";");
		    	out.println("	}");
		    	out.println("}");
		    	out.close();
		    }
		    in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/*
	 * 
package com.jada.dao;

import javax.persistence.EntityManager;
import com.jada.jpa.connection.JpaConnection;
import com.jada.hibernate.ContactUs;

public class ContactUsDAO extends ContactUs {
	public static ContactUs load(String siteId, Long contactUsId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ContactUs contactUs = (ContactUs) em.find(ContactUs.class, contactUsId);
		if (!contactUs.getContactUsId().equals(contactUsId)) {
			return null;
		}
		return contactUs;
	}
}
	 */
	
	static public void main(String[] argv) {
		Gen gen = new Gen();
		gen.create();
	}
}
