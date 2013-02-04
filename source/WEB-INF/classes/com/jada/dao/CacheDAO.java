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

package com.jada.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Cache;
import com.jada.jpa.entity.Site;
import com.jada.util.Constants;

public class CacheDAO extends Cache {
	private static final long serialVersionUID = -4039630714705867371L;
	static Logger logger = Logger.getLogger(CacheDAO.class);
    
	public static boolean isCacheEnabled() {
		return true;
	}
	
	public static void removeAllTransient() throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String sql = "delete from Cache where cacheTypeCode = '" + Constants.CACHE_TYPE_CODE_TRANSIENT + "'";
		int count = em.createQuery(sql).executeUpdate();
		logger.info("Total number of transient cache entries removed = " + count);
	}
	
	public static void removeAll() throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String sql = "delete from Cache";
		int count = em.createQuery(sql).executeUpdate();
		logger.info("Total number of cache entries removed = " + count);
	}
	
	public static Cache load(String siteId, Long cacheId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Cache cache = (Cache) em.find(Cache.class, cacheId);
		if (!cache.getSiteId().equals(siteId)) {
			throw new SecurityException("");
		}
		return cache;
	}
	
	public static void removeByKey(String siteId, String cacheKey) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String sql = "from     Cache " +
				 	 "where    siteId = :siteId " +
				 	 "and      cacheKey = :cacheKey";
		Query query = em.createQuery(sql);
		query.setParameter("siteId", siteId);
		query.setParameter("cacheKey", cacheKey + "%");
		Iterator<?> iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			Cache cache = (Cache) iterator.next();
			em.remove(cache);
		}
	}
	
	public static void removeByKeyPrefix(String siteId, String cacheKey) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String sql = "from     Cache " +
    				 "where    siteId = :siteId " +
    				 "and      cacheKey like :cacheKey";
    	Query query = em.createQuery(sql);
    	query.setParameter("siteId", siteId);
    	query.setParameter("cacheKey", cacheKey + "%");
    	Iterator<?> iterator = query.getResultList().iterator();
    	while (iterator.hasNext()) {
    		Cache cache = (Cache) iterator.next();
    		em.remove(cache);
    	}
	}
	
	public static Cache loadByKey(String siteId, String cacheKey) throws SecurityException, Exception {
		Cache cache = null;
		try {
	    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
	    	String sql = "select cache " + 
						 "from   Cache cache " +
						 "inner  join cache.site site " + 
						 "where  site.siteId = :siteId " + 
						 "and    cache.cacheKey = :cacheKey";
	    	Query query = em.createQuery(sql);
	    	query.setMaxResults(1);
	    	query.setParameter("siteId", siteId);
	    	query.setParameter("cacheKey", cacheKey);
	    	cache = (Cache) query.getSingleResult();
		}
		catch (javax.persistence.NoResultException e) {
			
		}
    	return cache;
	}

	public static Object getCacheValue(String siteId, String cacheKey) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String sql = "select cache " + 
    				 "from   Cache cache " +
        			 "inner  join cache.site site " + 
        			 "where  site.siteId = :siteId " + 
        			 "and    cache.cacheKey = :cacheKey";
    	Query query = em.createQuery(sql);
    	query.setMaxResults(1);
    	query.setParameter("siteId", siteId);
    	query.setParameter("cacheKey", cacheKey);
		Cache cache = null;
		try {
			cache = (Cache) query.getSingleResult();
		}
		catch (javax.persistence.NoResultException e) {}
    	if (cache == null) {
    		return null;
    	}
    	ByteArrayInputStream stream = new ByteArrayInputStream(cache.getCacheValue());
    	ObjectInput in = new ObjectInputStream(stream);
    	Object object = in.readObject();
    	return object;
    	
	}
	
	public static String getCacheText(String siteId, String cacheKey) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String sql = "select cache " + 
    				 "from   Cache cache " +
        			 "inner  join cache.site site " + 
        			 "where  site.siteId = :siteId " + 
        			 "and    cache.cacheKey = :cacheKey";
    	Query query = em.createQuery(sql);
    	query.setMaxResults(1);
    	query.setParameter("siteId", siteId);
    	query.setParameter("cacheKey", cacheKey);
		Cache cache = null;
		try {
			cache = (Cache) query.getSingleResult();
		}
		catch (javax.persistence.NoResultException e) {}
    	if (cache == null) {
    		return null;
    	}
    	return cache.getCacheText();
    	
	}
	
	public static void setCacheText(Site site, String cacheKey, char cacheTypeCode, String cacheValue) throws Exception {
		removeByKey(site.getSiteId(), cacheKey);
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Cache cache = new Cache();
		cache.setCacheTypeCode(cacheTypeCode);
		cache.setRecCreateBy(Constants.USERNAME_SYSTEM);
		cache.setRecCreateDatetime(new Date());
    	cache.setSite(site);
    	cache.setCacheKey(cacheKey);
    	cache.setCacheText(cacheValue);
		cache.setRecUpdateBy(Constants.USERNAME_SYSTEM);
		cache.setRecUpdateDatetime(new Date());
		em.persist(cache);
	}
	
	public static void setCacheValue(Site site, String cacheKey, char cacheTypeCode, Object object) throws Exception {
		removeByKey(site.getSiteId(), cacheKey);
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	byte cacheValue[] = null;
    	ByteArrayOutputStream stream = new ByteArrayOutputStream();
    	ObjectOutput out = new ObjectOutputStream(stream);
    	out.writeObject(object);
    	out.close();
    	cacheValue = stream.toByteArray();
    	
    	Cache cache = new Cache();
		cache.setCacheTypeCode(cacheTypeCode);
		cache.setRecCreateBy(Constants.USERNAME_SYSTEM);
		cache.setRecCreateDatetime(new Date());
    	cache.setSite(site);
    	cache.setCacheKey(cacheKey);
    	cache.setCacheValue(cacheValue);
		cache.setRecUpdateBy(Constants.USERNAME_SYSTEM);
		cache.setRecUpdateDatetime(new Date());
		em.persist(cache);
	}
}