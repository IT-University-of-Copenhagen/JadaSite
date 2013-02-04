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

package com.jada.search;

import java.util.Date;

import org.apache.log4j.Logger;
import javax.persistence.EntityManager;

import com.jada.dao.CacheDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Site;
import com.jada.util.Constants;
import com.jada.util.Utility;
import com.jada.xml.info.IndexerInfo;

public class IndexerInfoWriter extends Thread {
	Logger logger = Logger.getLogger(IndexerWorker.class);
	Site site = null;
	IndexerInfo indexerInfo = null;
	
	public IndexerInfoWriter(Site site, IndexerInfo indexerInfo) {
		this.site = site;
		this.indexerInfo = indexerInfo;
	}
	
	public synchronized void run() {
		EntityManager em = null;
		try {
            em = JpaConnection.getInstance().getCurrentEntityManager();
            em.getTransaction().begin();
			indexerInfo.setIndexerLastUpdateTime(new Date());
			String cacheKey = Constants.CACHE_INDEXER_INFO;
	 		CacheDAO.setCacheText(site, cacheKey, Constants.CACHE_TYPE_CODE_STATIC, Utility.joxMarshall("IndexerInfo", indexerInfo));
	        em.getTransaction().commit();
		} catch (Exception e) {
			logger.error("Unable to update IndexerInfo", e);
		}
		finally {
        	if (em.getTransaction().isActive()) {
        		em.getTransaction().rollback();
        	}
		}
	}
}
