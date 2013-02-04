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
import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.jada.dao.MenuDAO;
import com.jada.dao.SiteDomainDAO;
import com.jada.dao.SiteProfileClassDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Menu;
import com.jada.jpa.entity.MenuLanguage;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.util.Constants;

public class Tester {
	public void checkfile() throws Exception {
		File file = new File("D:\\tmp\\BlurbsAndImages.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		int pos = 1;
		while (true) {
			String line = reader.readLine();
			System.out.println(pos++ + ": " + line);
		}
	}
	
	public void scan(File file) throws Exception {
		for (File children : file.listFiles()) {
			if (children.isDirectory()) {
				scan(children);
			}
			else {
				if (!children.getPath().toLowerCase().endsWith("remove.java")) {
					continue;
				}
				System.out.println(children.getPath());
/*
				BufferedReader reader = new BufferedReader(new FileReader(children));
				String line = reader.readLine();
				if (!line.startsWith("/*")) {
					System.out.println(children.getPath());
				}
				reader.close();
*/
			}
		}
	}
	
	public void velocity() throws Exception {
		VelocityEngine engine = new VelocityEngine();
    	engine.setProperty("resource.loader", "file, file1");
    	engine.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
    	engine.setProperty("file.resource.loader.path", "d:/tmp/template");
    	engine.setProperty("file1.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
    	engine.setProperty("file1.resource.loader.path", "d:/tmp/template1");
    	engine.init();
    	
    	Template template = engine.getTemplate("sample.vm");
    	VelocityContext context = new VelocityContext();
    	StringWriter writer = new StringWriter();
    	template.merge(context, writer);
    	System.out.println(writer.toString());
	}
	
	public void test() {
		try {
			JpaConnection connection = JpaConnection.getInstance();
			connection.init();
			EntityManager em = connection.getCurrentEntityManager();
			em.getTransaction().begin();
			
			SiteDomain siteDomain = SiteDomainDAO.load(Long.valueOf(5));
			
			Menu parent = MenuDAO.load("Electronics", Long.valueOf(11));
			
			for (Menu menu : parent.getMenuChildren()) {
				System.out.println(menu.getMenuId() + " " + menu.getMenuLanguage().getMenuName());
			}

	        Menu menu = new Menu();
	        menu.setSiteDomain(siteDomain);
	        menu.setMenuParent(parent);
	        menu.setSeqNum(10);
	        menu.setMenuSetName("MAIN");
	        menu.setMenuType(Constants.MENU_HOME);
	        menu.setMenuUrl("");
	        menu.setMenuWindowTarget("");
	        menu.setMenuWindowMode("");
	        menu.setPublished(Constants.PUBLISHED_YES);
	        menu.setRecUpdateBy("admin");
	        menu.setRecCreateBy("admin");
	        menu.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
	        menu.setRecCreateDatetime(new Date(System.currentTimeMillis()));
	        
	        MenuLanguage menuLanguage = new MenuLanguage();
	        menuLanguage.setMenuName("New Menu 6");
	        menuLanguage.setMenu(menu);
	        menuLanguage.setRecUpdateBy("admin");
	        menuLanguage.setRecCreateBy("admin");
	        menuLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
	        menuLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
	        SiteProfileClass siteProfileClass = SiteProfileClassDAO.load(Long.valueOf(6));
	        menuLanguage.setSiteProfileClass(siteProfileClass);
	        em.persist(menuLanguage);
	        menu.getMenuLanguages().add(menuLanguage);
	        menu.setMenuLanguage(menuLanguage);
	        em.persist(menu);
	        
	        em = connection.getCurrentEntityManager();	        

	        String sql = "from Menu menu where menu.menuParent.menuId = 11";
	        Query query = em.createQuery(sql);
	        Iterator<?> iterator = query.getResultList().iterator();
	        while (iterator.hasNext()) {
	        	Menu m = (Menu) iterator.next();
	        	System.out.println(m.getMenuId() + " " + m.getMenuLanguage().getMenuName());
	        }
			
			em.getTransaction().rollback();
			em.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static public void main(String[] argv) {
		Tester j = new Tester();
		try {
			j.checkfile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
