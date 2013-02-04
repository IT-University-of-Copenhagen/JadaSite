package com.jada.util;

import java.util.Date;

import com.jada.dao.CacheDAO;
import com.jada.jpa.entity.Cache;
import com.jada.jpa.entity.Site;

public class IdSecurity {
	String id;
	boolean customerId;
	String cacheKey = null;
	Site site = null;
	
	public IdSecurity(Site site, String id, boolean customerId) {
		this.id = id;
		this.customerId = customerId;
		this.site = site;
		cacheKey = "cache.id";
		if (customerId) {
			cacheKey += ".C.";
		}
		else {
			cacheKey += ".A.";
		}
		cacheKey += id;
	}
	
	public void fail() throws SecurityException, Exception {
		Cache cache = CacheDAO.loadByKey(site.getSiteId(), cacheKey);
		int suspendCount = 0;
		if (cache != null) {
			String tokens[] = cache.getCacheText().split("\\|");
			suspendCount = Integer.parseInt(tokens[1]);
		}
		
		suspendCount++;
		String cacheText = (new Date()).getTime() + "|" + suspendCount;
		CacheDAO.setCacheText(site, cacheKey, Constants.CACHE_TYPE_CODE_TRANSIENT, cacheText);
	}
	
	public void reset() throws Exception {
		CacheDAO.removeByKey(site.getSiteId(), cacheKey);
	}
	
	public boolean isSuspened() throws SecurityException, Exception {
		Cache cache = CacheDAO.loadByKey(site.getSiteId(), cacheKey);
		if (cache == null) {
			return false;
		}
		
		String tokens[] = cache.getCacheText().split("\\|");
		int suspendCount = Integer.parseInt(tokens[1]);
		if (suspendCount < Constants.ID_SUSPEND_COUNT) {
			return false;
		}
		
		long time = Long.parseLong(tokens[0]);
		long current =(new Date()).getTime();
		if ((current - time) > Constants.ID_SUSPEND_TIME) {
			return false;
		}
		
		return true;
	}
}
