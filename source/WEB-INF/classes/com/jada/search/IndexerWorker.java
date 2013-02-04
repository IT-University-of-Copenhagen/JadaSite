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

import org.apache.log4j.Logger;
import javax.persistence.EntityManager;

import com.jada.jpa.connection.JpaConnection;

public class IndexerWorker extends Thread {
	Logger logger = Logger.getLogger(IndexerWorker.class);
	String siteId = null;
	
	public IndexerWorker(String siteId) {
		this.siteId = siteId;
	}
	
	public void run() {
		EntityManager em = null;
		try {
            em = JpaConnection.getInstance().getCurrentEntityManager();
            em.getTransaction().begin();
			Indexer.getInstance(siteId).indexAll();
			em = JpaConnection.getInstance().getCurrentEntityManager();
			em.getTransaction().commit();
		} catch (Exception e) {
			logger.error("Unable to index site", e);
		}
		finally {
            if (em.getTransaction().isActive()) {
            	em.getTransaction().rollback();
            }
		}
	}
}
