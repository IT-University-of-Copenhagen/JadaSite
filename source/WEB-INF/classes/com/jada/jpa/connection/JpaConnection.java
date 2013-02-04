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

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletConfig;

import org.hibernate.cfg.Environment;

import com.jada.util.Constants;

public class JpaConnection {
	static JpaConnection self = null;
	static private ThreadLocal<EntityManager> entityMangaerLocal = new ThreadLocal<EntityManager>();
	static EntityManagerFactory factory = null;
	String driver = null;
	String url = null;
	String user = null;
	String password = null;
	
    public synchronized static JpaConnection getInstance() throws Exception {
        if (self != null) {
            return self;
        }
        self = new JpaConnection();
        return self;
    }
    
    public void init() {
		factory = Persistence.createEntityManagerFactory("jadaSite");
    }
    
    public void init(ServletConfig config) {
    	this.driver = config.getInitParameter(Constants.CONFIG_PROPERTY_DRIVER);
    	this.url = config.getInitParameter(Constants.CONFIG_PROPERTY_URL);
    	this.user = config.getInitParameter(Constants.CONFIG_PROPERTY_USER);
    	this.password = config.getInitParameter(Constants.CONFIG_PROPERTY_PASS);
    	
    	HashMap<String, String> map = new HashMap<String, String>();
    	map.put(Environment.DRIVER, driver);
        map.put(Environment.URL, url);
        map.put(Environment.USER, user);
        map.put(Environment.PASS, password);
		factory = Persistence.createEntityManagerFactory("jadaSite", map);
    }
    
    public void init(String driver, String url, String user, String password) throws Exception {
    	this.driver = driver;
    	this.url = url;
    	this.user = user;
    	this.password = password;
    	
    	HashMap<String, String> map = new HashMap<String, String>();
    	map.put(Environment.DRIVER, driver);
        map.put(Environment.URL, url);
        map.put(Environment.USER, user);
        map.put(Environment.PASS, password);
        factory = Persistence.createEntityManagerFactory("jadaSite", map);
    }
    
    public EntityManager getCurrentEntityManager() throws Exception {
    	EntityManager em = (EntityManager) entityMangaerLocal.get();
    	if (em == null || !em.isOpen()) {
    		em = factory.createEntityManager();
    		entityMangaerLocal.set(em);
    	}
    	return em;
    }
    
    public EntityManager getEntityManager() throws Exception {
    	EntityManager em = factory.createEntityManager();
    	entityMangaerLocal.set(em);
    	return em;
    }

	public String getDriver() {
		return driver;
	}

	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

}
