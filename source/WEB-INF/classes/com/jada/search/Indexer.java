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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.compass.core.Compass;
import org.compass.core.CompassHits;
import org.compass.core.CompassSession;
import org.compass.core.CompassTransaction;
import org.compass.core.config.CompassConfiguration;
import org.compass.core.config.CompassEnvironment;
import org.compass.core.impl.DefaultCompassHit;
import org.compass.core.lucene.LuceneEnvironment;
import org.compass.core.lucene.engine.store.jdbc.C3P0DataSourceProvider;
import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.dao.CacheDAO;
import com.jada.dao.SiteDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Content;
import com.jada.jpa.entity.ContentLanguage;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.ItemLanguage;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.system.ApplicationGlobal;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;
import com.jada.xml.info.IndexerInfo;

public class Indexer {
	static Indexer instance = null;
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	Compass compass = null;
	Site site = null;
	String siteId = null;
    static Hashtable<String, Indexer> table = new Hashtable<String, Indexer>();
    IndexerInfo indexerInfo = null;
    int itemUpdateCount = 0;
    int contentUpdateCount = 0;
    static Logger logger = Logger.getLogger(Indexer.class);
    boolean isShareInventory = false;

	static public synchronized Indexer getInstance(String siteId) throws Exception {
		Indexer instance = (Indexer) table.get(siteId);
		if (instance != null) {
			return instance;
		}
		instance = new Indexer(siteId);
		table.put(siteId, instance);
		
		return instance;
	}
	
	public Indexer(String siteId) throws Exception {
		this.siteId = siteId;
		site = SiteDAO.load(siteId);
		if (site.getShareInventory() == Constants.VALUE_YES) {
			isShareInventory = true;
		}
		init();
	}
	
	public void remove() {
		table.remove(siteId);
	}
	
	public void init() throws Exception {
		CompassConfiguration compassConfiguration = new CompassConfiguration();
		JpaConnection jpaConnection = JpaConnection.getInstance();
		if (ApplicationGlobal.isCompassDatabaseStore()) {
			compassConfiguration.setSetting(CompassEnvironment.CONNECTION, "jdbc://" + jpaConnection.getUrl());
			compassConfiguration.setSetting(LuceneEnvironment.JdbcStore.DataSourceProvider.CLASS, C3P0DataSourceProvider.class.getName());
			compassConfiguration.setSetting(LuceneEnvironment.JdbcStore.Connection.DRIVER_CLASS, jpaConnection.getDriver());
			compassConfiguration.setSetting(LuceneEnvironment.JdbcStore.Connection.USERNAME, jpaConnection.getUser());
			compassConfiguration.setSetting(LuceneEnvironment.JdbcStore.Connection.PASSWORD, jpaConnection.getPassword());
		}
		else {
			String directory = ApplicationGlobal.getWorkingDirectory();
			if (!directory.endsWith("/") && !directory.endsWith("\\")) {
				directory += "/";
			}
			directory += "compass";
			File file = new File(directory);
			if (!file.exists()) {
				if (!file.mkdir()) {
					logger.error("Unable to create index directory: " + directory);
				}
			}
			compassConfiguration.setSetting(CompassEnvironment.CONNECTION, directory);
		}
		compassConfiguration.addResource("compass/Content.cpm.xml");
		compassConfiguration.addResource("compass/ContentLanguage.cpm.xml");
		compassConfiguration.addResource("compass/Item.cpm.xml");
		compassConfiguration.addResource("compass/ItemLanguage.cpm.xml");
		
		compass = compassConfiguration.buildCompass();
	}
	
	public void dropAllIndexes(Site site) throws Exception {
		compass.close();
        indexerInfo.setRemoveStatus(Constants.INDEXER_PROCESSING);
        updateIndexerInfo(indexerInfo);
        
        if (ApplicationGlobal.isCompassDatabaseStore()) {
	        executeQuery("drop table index_compasscontent", true);
	        executeQuery("drop table index_compasscontentlanguage", true);
	        executeQuery("drop table index_compassitem", true);
	        executeQuery("drop table index_compassitemlanguage", true);
        }
        else {
        	compass.getSearchEngineIndexManager().cleanIndex();
        }
		
		init();
        indexerInfo.setRemoveCount(0);
        indexerInfo.setRemoveStatus(Constants.INDEXER_FINISHED);
        updateIndexerInfo(indexerInfo);
	}
	
	private void executeQuery(String sql, boolean ignoreException) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		try {
			Query query = em.createNativeQuery(sql);
			query.executeUpdate();
		}
		catch (Exception e) {
			logger.error(e);
			if (!ignoreException) {
				throw e;
			}
		}

	}
	
	public void removeItem(Item item) {
		CompassSession compassSession = compass.openSession();
		CompassTransaction compassTransaction = compassSession.beginTransaction();
		String query = "+itemId:" + item.getItemId().toString();
		CompassHits hits = compassSession.find(query);
		Iterator<?> iterator = hits.detach().iterator();
		while (iterator.hasNext()) {
			DefaultCompassHit defaultCompassHit = (DefaultCompassHit) iterator.next();
			compassSession.delete(defaultCompassHit.getData());
		}
		
		compassTransaction.commit();
		compassSession.close();
	}
	
	private CompassItemLanguage formatItemLanguage(Item item, ItemLanguage master, Long siteDomainId) {
		CompassItemLanguage compassItemLanguage = new CompassItemLanguage();
		compassItemLanguage.setSiteDomainId(siteDomainId);
		compassItemLanguage.setSiteProfileClassId(master.getSiteProfileClass().getSiteProfileClassId());
		compassItemLanguage.setItemNaturalKey(item.getItemNaturalKey());
		compassItemLanguage.setSiteId(item.getSite().getSiteId());
		compassItemLanguage.setItemId(item.getItemId());
		compassItemLanguage.setItemLangId(master.getItemLangId());
		compassItemLanguage.setItemNum(item.getItemNum());
		compassItemLanguage.setItemUpcCd(item.getItemUpcCd());
		compassItemLanguage.setItemSkuCd(item.getItemSkuCd());
		
		compassItemLanguage.setItemShortDesc(master.getItemShortDesc());
		compassItemLanguage.setItemDesc(master.getItemDesc());
		
		compassItemLanguage.setInfoPublishOn(Format.getSortDate(item.getItemPublishOn()));
		compassItemLanguage.setInfoExpireOn(Format.getSortDate(item.getItemExpireOn()));
		compassItemLanguage.setPublished(item.getPublished());
		
		compassItemLanguage.setRecUpdateBy(master.getRecUpdateBy());
		compassItemLanguage.setRecUpdateDatetime(Format.getSortDate(master.getRecUpdateDatetime()));
		compassItemLanguage.setRecCreateBy(master.getRecCreateBy());
		compassItemLanguage.setRecCreateDatetime(Format.getSortDate(master.getRecCreateDatetime()));
		if (item.getUser() != null) {
			compassItemLanguage.setUserId(item.getUser().getUserId());
		}
		return compassItemLanguage;
	}
	
	public void updateItem(Item master) throws Exception {
		updateItem(master, null);
	}
	
	public void updateItem(Item master, SiteProfileClass siteProfileClasses[]) throws Exception {
		if (siteProfileClasses == null) {
			siteProfileClasses = getSiteProfileClasses();
		}
		
		CompassSession compassSession = compass.openSession();
		CompassTransaction compassTransaction = compassSession.beginTransaction();

		String query = "+itemId:" + master.getItemId().toString();
		CompassHits hits = compassSession.find(query);
		Iterator<?> iterator = hits.detach().iterator();
		while (iterator.hasNext()) {
			DefaultCompassHit defaultCompassHit = (DefaultCompassHit) iterator.next();
			compassSession.delete(defaultCompassHit.getData());
		}
		
		if (!master.getItemTypeCd().equals(Constants.ITEM_TYPE_SKU)) {
			if (isShareInventory) {
				for (SiteProfileClass siteProfileClass : siteProfileClasses) {
					boolean found = false;
					for (ItemLanguage language : master.getItemLanguages()) {
						if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClass.getSiteProfileClassId())) {
							found = true;
							CompassItemLanguage compassItemLanguage = formatItemLanguage(master, language, -1L);
							compassSession.save(compassItemLanguage);
						}
					}
					if (!found) {
						CompassItemLanguage compassItemLanguage = formatItemLanguage(master, master.getItemLanguage(), -1L);
						compassItemLanguage.setSiteProfileClassId(siteProfileClass.getSiteProfileClassId());
						compassSession.save(compassItemLanguage);
					}
				}
			}
			else {
				for (SiteDomain siteDomain : master.getSiteDomains()) {
					for (SiteProfileClass siteProfileClass : siteProfileClasses) {
						boolean found = false;
						for (ItemLanguage language : master.getItemLanguages()) {
							if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClass.getSiteProfileClassId())) {
								found = true;
								CompassItemLanguage compassItemLanguage = formatItemLanguage(master, language, siteDomain.getSiteDomainId());
								compassSession.save(compassItemLanguage);
							}
						}
						if (!found) {
							CompassItemLanguage compassItemLanguage = formatItemLanguage(master, master.getItemLanguage(), siteDomain.getSiteDomainId());
							compassItemLanguage.setSiteProfileClassId(siteProfileClass.getSiteProfileClassId());
							compassSession.save(compassItemLanguage);
						}
					}
				}
			}
		}
		
		compassTransaction.commit();
		compassSession.close();
	}
	
	public void removeContent(Content content) {
		CompassSession compassSession = compass.openSession();
		CompassTransaction compassTransaction = compassSession.beginTransaction();
		String query = "+contentId:" + content.getContentId().toString();
		CompassHits hits = compassSession.find(query);
		Iterator<?> iterator = hits.detach().iterator();
		while (iterator.hasNext()) {
			DefaultCompassHit defaultCompassHit = (DefaultCompassHit) iterator.next();
			compassSession.delete(defaultCompassHit.getData());
		}
		
		compassTransaction.commit();
		compassSession.close();
	}
	
	private CompassContentLanguage formatContentLanguage(Content content, ContentLanguage master) {
		CompassContentLanguage compassContentLanguage = new CompassContentLanguage();
		compassContentLanguage.setSiteProfileClassId(master.getSiteProfileClass().getSiteProfileClassId());
		compassContentLanguage.setContentNaturalKey(content.getContentNaturalKey());
		compassContentLanguage.setSiteId(content.getSite().getSiteId());
		compassContentLanguage.setContentId(content.getContentId());
		compassContentLanguage.setContentLangId(master.getContentLangId());
		compassContentLanguage.setContentShortDesc(master.getContentShortDesc());	
		compassContentLanguage.setContentTitle(master.getContentTitle());
		compassContentLanguage.setContentDesc(master.getContentDesc());
		compassContentLanguage.setInfoPublishOn(Format.getSortDate(content.getContentPublishOn()));
		compassContentLanguage.setInfoExpireOn(Format.getSortDate(content.getContentExpireOn()));
		compassContentLanguage.setPublished(content.getPublished());
		compassContentLanguage.setRecUpdateBy(master.getRecUpdateBy());
		compassContentLanguage.setRecUpdateDatetime(master.getRecUpdateDatetime());
		compassContentLanguage.setRecCreateBy(master.getRecCreateBy());
		compassContentLanguage.setRecCreateDatetime(master.getRecCreateDatetime());
		if (content.getUser() != null) {
			compassContentLanguage.setUserId(content.getUser().getUserId());
		}
		return compassContentLanguage;
	}
	
	public void updateContent(Content master) throws Exception {
		updateContent(master, null);
	}
	
	public void updateContent(Content master, SiteProfileClass siteProfileClasses[]) throws Exception {
		if (siteProfileClasses == null) {
			siteProfileClasses = getSiteProfileClasses();
		}
		CompassSession compassSession = compass.openSession();
		CompassTransaction compassTransaction = compassSession.beginTransaction();

		String query = "+contentId:" + master.getContentId().toString();
		CompassHits hits = compassSession.find(query);
		Iterator<?> iterator = hits.detach().iterator();
		while (iterator.hasNext()) {
			DefaultCompassHit defaultCompassHit = (DefaultCompassHit) iterator.next();
			compassSession.delete(defaultCompassHit.getData());
		}
		
		for (SiteProfileClass siteProfileClass : siteProfileClasses) {
			boolean found = false;
			for (ContentLanguage language : master.getContentLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClass.getSiteProfileClassId())) {
					found = true;
					CompassContentLanguage compassContentLanguage = formatContentLanguage(master, language);
					compassSession.save(compassContentLanguage);
				}
			}
			if (!found) {
				CompassContentLanguage compassContentLanguage = formatContentLanguage(master, master.getContentLanguage());
				compassContentLanguage.setSiteProfileClassId(siteProfileClass.getSiteProfileClassId());
				compassSession.save(compassContentLanguage);
			}
		}
		
		compassTransaction.commit();
		compassSession.close();
	}
	
	public void indexAll() throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Site site = (Site) SiteDAO.load(siteId);
		indexerInfo = new IndexerInfo();
		indexerInfo.setIndexerStatus(Constants.INDEXER_PROCESSING);
		indexerInfo.setRemoveCount(0);
		indexerInfo.setRemoveStatus(Constants.INDEXER_PENDING);
		indexerInfo.setItemUpdateCount(0);
		indexerInfo.setItemUpdateStatus(Constants.INDEXER_PENDING);
		indexerInfo.setContentUpdateCount(0);
		indexerInfo.setContentUpdateStatus(Constants.INDEXER_PENDING);
		indexerInfo.setIndexerStartTime(new Date());
		itemUpdateCount = 0;
		contentUpdateCount = 0;
		String sql = "from Site";
		Query query = em.createQuery(sql);
		Iterator<?> iterator = query.getResultList().iterator();
		int contentCount = 0;
		int itemCount = 0;
		while (iterator.hasNext()) {
			Site s = (Site) iterator.next();
			
			Long result;
			Query itemQuery = em.createQuery("select count(*) from Item item where item.site.siteId = :siteId");
			itemQuery.setParameter("siteId", s.getSiteId());
			result = (Long) itemQuery.getSingleResult();
			itemCount += result;
			
			Query contentQuery = em.createQuery("select count(*) from Content content where content.site.siteId = :siteId");
			contentQuery.setParameter("siteId", s.getSiteId());
			result = (Long) contentQuery.getSingleResult();
			contentCount += result;
		}
		indexerInfo.setItemTotalCount(itemCount);
		indexerInfo.setContentTotalCount(contentCount);
		updateIndexerInfo(indexerInfo);
		
		dropAllIndexes(site);
		
		sql = "from Site";
		query = em.createQuery(sql);
		iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			Site s = (Site) iterator.next();
			indexAllContent(s);
			indexAllItem(s);
		}
		
		indexerInfo.setIndexerStatus(Constants.INDEXER_FINISHED);
		updateIndexerInfo(indexerInfo);
	}
	
	public IndexerInfo getIndexerInfo() throws Exception {
		String cacheKey = Constants.CACHE_INDEXER_INFO;
		String cacheValue = CacheDAO.getCacheText(siteId, cacheKey);
		if (cacheValue == null) {
			return null;
		}
		IndexerInfo indexerInfo = (IndexerInfo) Utility.joxUnMarshall(IndexerInfo.class, String.valueOf(cacheValue));
		return indexerInfo;
	}
	
	public void updateIndexerInfo(IndexerInfo indexerInfo) throws Exception {
        IndexerInfoWriter writer = new IndexerInfoWriter(site, indexerInfo);
        writer.start();
	}
	
	public void indexAllContent(Site site) throws Exception {
		indexerInfo.setContentUpdateStatus(Constants.INDEXER_PROCESSING);
		updateIndexerInfo(indexerInfo);
		
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	SiteProfileClass siteProfileClasses[] = getSiteProfileClasses();
        Query query = null;
        String sql = "from Content content where content.site.siteId = :siteId ";
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        Iterator<?> iterator = query.getResultList().iterator();
        int counter = 0;
        while (iterator.hasNext()) {
        	Content content = (Content) iterator.next();
        	updateContent(content, siteProfileClasses);
        	contentUpdateCount++;
        	if (contentUpdateCount % 10 == 0) {
        		indexerInfo.setContentUpdateCount(contentUpdateCount);
        		updateIndexerInfo(indexerInfo);
        	}
        	counter++;
        	if (counter % 100 == 0) {
        		System.gc();
        	}
        }
        System.gc();
        indexerInfo.setContentUpdateStatus(Constants.INDEXER_FINISHED);
        indexerInfo.setContentUpdateCount(contentUpdateCount);
        updateIndexerInfo(indexerInfo);
	}
	
	public QueryResult search(String criteria, Long siteProfileClassId, Long siteDomainId, int pageNum, int pageSize) throws Exception {
		CompassSession compassSession = compass.openSession();
		CompassTransaction compassTransaction = compassSession.beginTransaction();
		
		Calendar calendar = Calendar.getInstance();
		String infoPublishTo = dateFormat.format(calendar.getTime());
		calendar.add(Calendar.DATE, 1);
		String infoExpireFrom = dateFormat.format(calendar.getTime());
		
		QueryResult result = new QueryResult();
		String query = "+siteId:" + siteId + 
					   " +siteProfileClassId:" + siteProfileClassId.toString() + 
					   " +published:" + "Y" + 
					   " +infoPublishOn:[19000101 TO " + infoPublishTo + "]" +
					   " +infoExpireOn:[" + infoExpireFrom + " TO 29991231]";
		if (!isShareInventory) {
			query += " +siteDomainId:" + siteDomainId;
		}
		query += " +(" + criteria + ")";
//query = criteria;
		CompassHits hits = compassSession.find(query);
		result.setHitCount(hits.getLength());
		
		int start = (pageNum - 1) * pageSize;
		int end = start + pageSize - 1;
		Vector<Object> queryHits = new Vector<Object>();
		Iterator<?> iterator = hits.detach().iterator();
		int pos = -1;
		while (iterator.hasNext()) {
			pos++;
			DefaultCompassHit defaultCompassHit = (DefaultCompassHit) iterator.next();
			if (pos < start) {
				continue;
			}
			if (pos > end) {
				break;
			}
			queryHits.add(defaultCompassHit.getData());
		}
		result.setQueryHits(queryHits);
		compassTransaction.commit();
		compassSession.close();
		return result;
	}
	
	public SiteProfileClass[] getSiteProfileClasses() throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Query query = em.createQuery("from  SiteProfileClass siteProfileClass where siteProfileClass.site.siteId = :siteId");
		query.setParameter("siteId", siteId);
		Iterator<?> iterator = query.getResultList().iterator();
		Vector<SiteProfileClass> vector = new Vector<SiteProfileClass>();
		while (iterator.hasNext()) {
			SiteProfileClass siteProfileClass = (SiteProfileClass) iterator.next(); 
			vector.add(siteProfileClass);
		}
		SiteProfileClass siteProfileClasses[] = new SiteProfileClass[vector.size()];
		vector.copyInto(siteProfileClasses);
		return siteProfileClasses;
	}

	public void indexAllItem(Site site) throws Exception {
		indexerInfo.setItemUpdateStatus(Constants.INDEXER_PROCESSING);
		updateIndexerInfo(indexerInfo);
		
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	SiteProfileClass siteProfileClasses[] = getSiteProfileClasses();
        Query query = null;
        String sql = "from Item where siteId = :siteId ";
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        Iterator<?> iterator = query.getResultList().iterator();
        int counter = 0;
        while (iterator.hasNext()) {
        	Item item = (Item) iterator.next();
        	updateItem(item, siteProfileClasses);
        	itemUpdateCount++;
        	if (itemUpdateCount % 10 == 0) {
        		indexerInfo.setItemUpdateCount(itemUpdateCount);
        		updateIndexerInfo(indexerInfo);
        	}
        	counter++;
        	if (counter % 100 == 0) {
        		System.gc();
        	}
        }
        indexerInfo.setItemUpdateCount(itemUpdateCount);
        indexerInfo.setItemUpdateStatus(Constants.INDEXER_FINISHED);
        updateIndexerInfo(indexerInfo);
        System.gc();
	}

	public Compass getCompass() {
		return compass;
	}

	public void setCompass(Compass compass) {
		this.compass = compass;
	}
}