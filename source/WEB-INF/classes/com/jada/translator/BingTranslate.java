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

package com.jada.translator;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.ws.WebServiceRef;
import javax.xml.ws.handler.MessageContext;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.datacontract.schemas._2004._07.microsoft_mt_web_service.ArrayOfTranslateArrayResponse;
import org.json.JSONObject;
import org.tempuri.LanguageService;
import org.tempuri.SoapService;

import com.jada.dao.CacheDAO;
import com.jada.jpa.entity.Site;
import com.jada.util.Format;
import com.jada.util.Utility;
import com.jada.xml.site.SiteParamBean;
import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfstring;
import com.sun.xml.ws.developer.WSBindingProvider;

public class BingTranslate {
	@WebServiceRef(wsdlLocation="http://api.microsofttranslator.com/V2/Soap.svc")
	LanguageService service = new SoapService().getBasicHttpBindingLanguageService();
	String from = null;
	String to = null;
	Site site = null;
	
	String tokenRequestUrl = "https://datamarket.accesscontrol.windows.net/v2/OAuth2-13/";
	int tokenRequestConnectionTimeout = 30000;

	static Logger logger = Logger.getLogger(BingTranslate.class);

	public BingTranslate(Site site) {
		this.site = site;
	}
	
	public BingTranslate(String from, String to, Site site) {
		this.from = from;
		this.to = to;
		this.site = site;
	}
	
	private void initService() throws SecurityException, Exception {
		String token = getAccessToken();
		WSBindingProvider provider = (WSBindingProvider) service;
		
		provider.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, 
										Collections.singletonMap("Authorization", Collections.singletonList("Bearer " + token)));
	}
	
	@SuppressWarnings("unchecked")
	public String[] translateAll(String input[]) throws Exception {
		initService();
		JavaArrayOfString strings = new JavaArrayOfString();
		ArrayList list = new ArrayList();
		for (String s : input) {
			list.add(s);
		}
		strings.setString(list);
	
		Vector vector = new Vector();
		ArrayOfTranslateArrayResponse responses = service.translateArray("", strings, from, to, null);
		for (org.datacontract.schemas._2004._07.microsoft_mt_web_service.TranslateArrayResponse response : responses.getTranslateArrayResponse()) {
			vector.add(response.getTranslatedText().getValue());
		}
		String result[] = new String[vector.size()];
		vector.copyInto(result);
		return result;
	}
	
	public String translate(String input) throws Exception {
		initService();
		return service.translate("", input, from, to, "text/plain", "");
	}
	
	public LabelValueBean[] getLanguages() throws SecurityException, Exception {
		initService();
		ArrayOfstring languageCodes = service.getLanguagesForTranslate("");
		ArrayOfstring languageNames = service.getLanguageNames("", "en", languageCodes);
		
		int counter = 0;
		List<String> names = languageNames.getString();
		Vector<LabelValueBean> vector = new Vector<LabelValueBean>();
		for (String languageCode : languageCodes.getString()) {
			String languageName = names.get(counter++);
			LabelValueBean bean = new LabelValueBean(languageName, languageCode);
			vector.add(bean);
		}
		LabelValueBean beans[] = new LabelValueBean[vector.size()];
		vector.copyInto(beans);
		return beans;
	}
	
	public String[] getLanguagesForTranslate() throws SecurityException, Exception {
		initService();
		ArrayOfstring languageNames = service.getLanguageNames("", "en", service.getLanguagesForTranslate(""));
		
		String languages[] = new String[languageNames.getString().size()];
		languageNames.getString().toArray(languages);
		return null;
	}
	
	public String getAccessToken() throws SecurityException, Exception {
		String accessToken = null;
		long expireOn = 0;

		BingCache cache = (BingCache) CacheDAO.getCacheValue(site.getSiteId(), com.jada.util.Constants.CACHE_BING_TRANSLATE);
		if (cache != null) {
			if (System.currentTimeMillis() < cache.getExpireOn()) {
				return cache.getAccessToken();
			}
		}
		
		String line = "";
    	StringBuffer sb = new StringBuffer();
    	HttpsURLConnection connection = null;
	    try {
			SiteParamBean siteParamBean = new SiteParamBean();
			if (!Format.isNullOrEmpty(site.getSiteParam())) {
				siteParamBean = (SiteParamBean) Utility.joxUnMarshall(SiteParamBean.class, site.getSiteParam());
			}
			
	    	sb.append("client_id=" + URLEncoder.encode(siteParamBean.getBingClientId(), "UTF-8") + "&");
	    	sb.append("client_secret=" + URLEncoder.encode(siteParamBean.getBingClientSecert(), "UTF-8") + "&");
	    	sb.append("scope=" + URLEncoder.encode("http://api.microsofttranslator.com", "UTF-8") + "&");
	    	sb.append("grant_type=" + "client_credentials");
	    	
	    	URL url = new URL(tokenRequestUrl);
			connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(tokenRequestConnectionTimeout);
			connection.setReadTimeout(tokenRequestConnectionTimeout);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.write(sb.toString().getBytes());
			out.flush();
			out.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			line = in.readLine();
			in.close();
	    }
	    catch (Exception e) {
	    	BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
	    	String errorMessage = "";
			try {
				errorMessage = in.readLine();
				in.close();
			} catch (IOException e1) {
			}
			logger.error(e);
			logger.error("request = " + sb.toString());
			logger.error("response = " + line);
			logger.error("errorMessage = " + errorMessage);
	    }
	    
	    JSONObject result = new JSONObject(line);
	    accessToken = result.getString("access_token");
	    String expires = result.getString("expires_in");
	    expireOn = System.currentTimeMillis() + Long.valueOf(expires) * 1000 - 30000;
	    
	    cache = new BingCache();
	    cache.setAccessToken(accessToken);
	    cache.setExpireOn(expireOn);
	    CacheDAO.setCacheValue(site, com.jada.util.Constants.CACHE_BING_TRANSLATE, com.jada.util.Constants.CACHE_TYPE_CODE_TRANSIENT, cache);
		return accessToken;
	}
	
	public static void main(String[] args) {
	}
}
