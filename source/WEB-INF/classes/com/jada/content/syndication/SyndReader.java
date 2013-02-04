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

package com.jada.content.syndication;

import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Syndication;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


public class SyndReader {
    Logger logger = Logger.getLogger(SyndReader.class);
	static Hashtable<String, SyndReader> instance = new Hashtable<String, SyndReader>();
	static long expiry = 30000;
	String siteId;
	long lastUpdateAt = 0;
	SyndicationList syndicationList = new SyndicationList();
	
	public SyndReader(String siteId) {
		this.siteId = siteId;
	}
	
	public static boolean hasInstance(String siteId) {
		SyndReader reader = (SyndReader) instance.get(siteId);
		if (reader != null) {
			return true;
		}
		return false;
	}
	
	public static SyndReader getInstance(String siteId) throws FeedException, IOException, Exception {
		SyndReader reader = (SyndReader) instance.get(siteId);
		if (reader != null) {
			return reader;
		}
		SyndReader syndReader = new SyndReader(siteId);
		syndReader.read();
		instance.put(siteId, syndReader);
		return syndReader;
	}
	
	public synchronized void read() throws FeedException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        Query query = em.createQuery("from Syndication syndication where siteId = :siteId and active = :active order by seqNum");
		query.setParameter("siteId", siteId);
		query.setParameter("active", String.valueOf(Constants.VALUE_YES));
		Iterator<?> it = query.getResultList().iterator();
		Vector<String> urls = new Vector<String>();
		while (it.hasNext()) {
			Syndication syndication = (Syndication) it.next();
			urls.add(syndication.getSynUrl());
		}

		// TODO read proxy information from database
		String proxyHost = null;
		String proxyPort = "8080";
		if (proxyHost != null && proxyHost.length() > 0) {
			System.getProperties().put("proxySet", "true");
			System.getProperties().put("proxyHost", proxyHost);
			System.getProperties().put("proxyPort", proxyPort);
		}
		
		Vector<SyndicationEntryInfo> vector = new Vector<SyndicationEntryInfo>();
		it = urls.iterator();
		while (it.hasNext()) {
			String url = (String) it.next();
			try {
	            SyndFeedInput input = new SyndFeedInput();
	            SyndFeed feed = input.build(new XmlReader(new URL(url)));
	            List<?> list = feed.getEntries();
	            Iterator<?> iterator = list.iterator();
	            while (iterator.hasNext()) {
	            	SyndEntry entry = (SyndEntry) iterator.next();
	            	SyndicationEntryInfo syndicationEntryInfo = new SyndicationEntryInfo();
	            	if (entry.getLink() == null) {
	            		continue;
	            	}
	            	syndicationEntryInfo.setLink(entry.getLink());
	            	if (entry.getTitle() == null) {
	            		continue;
	            	}
	            	syndicationEntryInfo.setTitle(entry.getTitle());
	            	if (entry.getDescription() != null) {
	            		syndicationEntryInfo.setDescription(entry.getDescription().getValue());
	            	}
	            	syndicationEntryInfo.setPublishDate("");
	            	syndicationEntryInfo.setUpdatedDate("");
	            	// TODO format the date
	            	if (entry.getPublishedDate() != null) {
	                	syndicationEntryInfo.setPublishDate(Format.getDate(entry.getPublishedDate()));
	            	}
	            	if (entry.getUpdatedDate() != null) {
	                	syndicationEntryInfo.setUpdatedDate(Format.getDate(entry.getUpdatedDate()));
	            	}
	              	vector.add(syndicationEntryInfo);
	            }
			}
			catch (IOException e) {
				logger.error("Error fetching " + url);
				logger.error(e);
			}
			catch (Exception e) {
				logger.error("Exception caught while fetching " + url);
				logger.error(e);
			}
		}
		SyndicationEntryInfo syndicationEntryInfos[] = new SyndicationEntryInfo[vector.size()];
		vector.copyInto(syndicationEntryInfos);
		syndicationList.setSyncdicationEntryInfos(syndicationEntryInfos);
		lastUpdateAt = System.currentTimeMillis();
	}

	public synchronized SyndicationList getSyndicationList() throws FeedException, Exception {
		long current = System.currentTimeMillis();
		if (current > lastUpdateAt + expiry) {
			logger.info("Dropping cache ....");
			read();
		}
		return syndicationList;
	}
	
	public void reset() {
		// To force the access to perform a read
		lastUpdateAt = 0;
	}

	public void setSyndicationList(SyndicationList syndicationList) {
		this.syndicationList = syndicationList;
	}
}
