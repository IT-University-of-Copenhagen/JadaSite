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

package com.jada.content.content;

import com.jada.content.ContentBean;
import com.jada.content.ContentLookupDispatchAction;
import com.jada.content.ContentSessionBean;
import com.jada.content.data.ContentApi;
import com.jada.content.data.ItemCompareInfo;
import com.jada.content.data.ItemInfo;
import com.jada.content.template.TemplateEngine;
import com.jada.dao.ContentDAO;
import com.jada.dao.CountryDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Comment;
import com.jada.jpa.entity.Content;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.CustomerAddress;
import com.jada.jpa.entity.CustomerClass;
import com.jada.jpa.entity.Language;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.State;
import com.jada.system.Languages;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import com.jada.util.JSONEscapeObject;

public class ContentAction extends ContentLookupDispatchAction {
    Logger logger = Logger.getLogger(ContentAction.class);

    public ActionForward alert(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	String commentId = request.getParameter("commentId"); 
    	String alertType = request.getParameter("alertType");
    	ContentBean contentBean = getContentBean(request);
		Customer customer = getCustomer(request);
		if (customer == null) {
	    	Language language = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getLanguage();
	    	JSONEscapeObject jsonResult = new JSONEscapeObject();
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_SIGNIN);
			jsonResult.put("message", Languages.getLangTranValue(language.getLangId(), "content.text.comment.signin"));
			String jsonString = jsonResult.toHtmlString();
			streamWebService(response, jsonString);
			return null;
		}
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Comment comment = (Comment) em.find(Comment.class, Format.getLong(commentId));

    	if (alertType.equals(Constants.ALERT_TYPE_MODERATOR)) {
    		comment.setModeration(Constants.VALUE_YES);
    		em.persist(comment);
    	}
    	else {
    		Iterator<?> iterator = comment.getAgreeCustomers().iterator();
    		while (iterator.hasNext()) {
    			Customer c = (Customer) iterator.next();
    			if (c.getCustId().equals(customer.getCustId())) {
    				iterator.remove();
    				break;
    			}
    		}
    		iterator = comment.getDisagreeCustomers().iterator();
    		while (iterator.hasNext()) {
    			Customer c = (Customer) iterator.next();
    			if (c.getCustId().equals(customer.getCustId())) {
    				iterator.remove();
    				break;
    			}
    		}
    		if (alertType.equals(Constants.ALERT_TYPE_AGREE)) {
    			comment.getAgreeCustomers().add(customer);
    		}
    		else { 
    			comment.getDisagreeCustomers().add(customer);
    		}
    	}
    	
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("agreeCount", comment.getAgreeCustomers().size());
		jsonResult.put("disagreeCount", comment.getDisagreeCustomers().size());
		jsonResult.put("moderation", String.valueOf(comment.getModeration()));
		streamWebService(response, jsonResult.toHtmlString());
    	return null;
    }
    
    public ActionForward signinStatus(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
    	Customer customer = getCustomer(request);
    	if (customer == null) {
    		jsonResult.put("signinStatus", false);
    	}
    	else {
    		jsonResult.put("signinStatus", true);
    	}
    	streamWebService(response, jsonResult.toHtmlString());
    	return null;
    }
    
    public ActionForward signin(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	String custEmail = request.getParameter("custEmail"); 
    	String custPassword = request.getParameter("custPassword");
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	ContentBean contentBean = getContentBean(request);
    	
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String sql = "from  Customer customer " +
					 "where customer.site.siteId = :siteId " +
					 "and   custEmail = :custEmail ";
		Query query = em.createQuery(sql);
		query.setParameter("siteId", contentBean.getContentSessionKey().getSiteId());
		query.setParameter("custEmail", custEmail);
    	Language language = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getLanguage();
    	
		Customer customer = null;
		try {
			customer = (Customer) query.getSingleResult();
		}
		catch (javax.persistence.NoResultException e) {}
		if (customer == null) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			jsonResult.put("custPasswordError", Languages.getLangTranValue(language.getLangId(), "content.error.login.invalid"));
			this.streamWebService(response, jsonResult.toHtmlString());
			return null;
		}
		if (!AESEncoder.getInstance().decode(customer.getCustPassword()).equals(custPassword)) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			jsonResult.put("custPasswordError", Languages.getLangTranValue(language.getLangId(), "content.error.login.invalid"));
			this.streamWebService(response, jsonResult.toHtmlString());
			return null;
		}
    	setCustId(request, customer.getCustId());
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		this.streamWebService(response, jsonResult.toHtmlString());
    	return null;
    }

    
    public ActionForward registration(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	String custPublicName = request.getParameter("custPublicName"); 
    	String custEmail = request.getParameter("custEmail"); 
    	String custPassword = request.getParameter("custPassword");
    	String custPassword1 = request.getParameter("custPassword1");

		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ContentBean contentBean = getContentBean(request);
    	Long defaultSiteDomainId = contentBean.getSiteDomain().getSite().getSiteDomainDefault().getSiteDomainId();
    	Language language = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getLanguage();
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	if (Format.isNullOrEmpty(custPublicName)) {
    		jsonResult.put("custPublicNameError", Languages.getLangTranValue(language.getLangId(), "content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(custEmail)) {
    		jsonResult.put("custEmailError", Languages.getLangTranValue(language.getLangId(), "content.error.string.required"));
    	}    	
    	if (Format.isNullOrEmpty(custPassword)) {
    		jsonResult.put("custPasswordError", Languages.getLangTranValue(language.getLangId(), "content.error.string.required"));
    	}    	
    	if (Format.isNullOrEmpty(custPassword1)) {
    		jsonResult.put("custPassword1Error", Languages.getLangTranValue(language.getLangId(), "content.error.string.required"));
    	} 
    	if (!Format.isNullOrEmpty(custPassword) && !Format.isNullOrEmpty(custPassword1)) {
    		if (!custPassword.equals(custPassword1)) {
    			jsonResult.put("custPasswordMatch", Languages.getLangTranValue(language.getLangId(), "content.error.password.nomatch"));
    		}
    	}
    	if (jsonResult.length() > 0) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			this.streamWebService(response, jsonResult.toHtmlString());
			return null;
    	}

    	char singleCheckout = contentBean.getSiteDomain().getSite().getSingleCheckout();
    	String sql = "from   Customer customer " +
    				 "where  customer.siteDomain.siteDomainId = :siteDomainId " +
    				 "and    custPublicName = :custPublicName ";
    	Query query = em.createQuery(sql);
    	query.setParameter("custPublicName", custPublicName);
        if (singleCheckout == Constants.VALUE_YES) {
            query.setParameter("siteDomainId", defaultSiteDomainId);
        }
        else {
        	query.setParameter("siteDomainId", contentBean.getSiteDomain().getSiteDomainId());
        }
		Customer customer = null;
		try {
			customer = (Customer) query.getSingleResult();
		}
		catch (javax.persistence.NoResultException e) {}
    	if (customer != null) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			jsonResult.put("custPublicNameError", Languages.getLangTranValue(language.getLangId(), "content.error.publicName.duplicate"));
			this.streamWebService(response, jsonResult.toHtmlString());
			return null;
    	}
    	
    	sql = "from CustomerClass customerClass where siteId = :siteId and customerClass.systemRecord = 'Y'";
    	query = em.createQuery(sql);
    	query.setParameter("siteId", contentBean.getSiteDomain().getSite().getSiteId());
    	CustomerClass customerClass = (CustomerClass) query.getSingleResult();
    	
    	sql = "from   Customer customer " +
			  "where  customer.siteDomain.siteDomainId = :siteDomainId " +
			  "and    custEmail = :custEmail ";
		query = em.createQuery(sql);
		query.setParameter("custEmail", custEmail);
		if (singleCheckout == Constants.VALUE_YES) {
			query.setParameter("siteDomainId", defaultSiteDomainId);
		}
		else {
			query.setParameter("siteDomainId", contentBean.getSiteDomain().getSiteDomainId());
		}
		try {
			customer = (Customer) query.getSingleResult();
		}
		catch (javax.persistence.NoResultException e) {}
		if (customer != null) {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			jsonResult.put("custEmailError", Languages.getLangTranValue(language.getLangId(), "content.error.email.duplicate"));
			this.streamWebService(response, jsonResult.toHtmlString());
			return null;
		}
		
    	
    	customer = new Customer();
    	customer.setCustPublicName(custPublicName);
    	customer.setCustEmail(custEmail);;
    	customer.setCustPassword(AESEncoder.getInstance().encode(custPassword));
    	customer.setCustSource(Constants.CUSTOMER_SOURCE_REGISTER);
    	customer.setCustSourceRef("");
    	customer.setActive(Constants.VALUE_YES);
    	customer.setRecUpdateBy(Constants.USERNAME_SYSTEM);
    	customer.setRecUpdateDatetime(new Date());
    	customer.setRecCreateBy(Constants.USERNAME_SYSTEM);
    	customer.setRecCreateDatetime(new Date());
    	customer.setCustomerClass(customerClass);
    	customer.setSite(contentBean.getContentSessionBean().getSiteDomain().getSite());
		if (singleCheckout == Constants.VALUE_YES) {
			SiteDomain siteDomain = (SiteDomain) em.find(SiteDomain.class, defaultSiteDomainId);
			customer.setSiteDomain(siteDomain);
			query.setParameter("siteDomainId", defaultSiteDomainId);
		}
		else {
			customer.setSiteDomain(contentBean.getSiteDomain());
		}
    	
        CustomerAddress customerAddress = new CustomerAddress();
        customer.setCustAddress(customerAddress);
        customer.getCustAddresses().add(customerAddress);
        customerAddress.setCustUseAddress(Constants.CUST_ADDRESS_USE_OWN);
        customerAddress.setCustAddressType(Constants.CUSTOMER_ADDRESS_CUST);
        customerAddress.setRecUpdateBy(Constants.USERNAME_SYSTEM);
        customerAddress.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        customerAddress.setRecCreateBy(Constants.USERNAME_SYSTEM);
        customerAddress.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        em.persist(customerAddress);
        
    	em.persist(customer);
    	
    	setCustId(request, customer.getCustId());
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		this.streamWebService(response, jsonResult.toHtmlString());
    	return null;
    }
    
    public ActionForward comment(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
		String contentNaturalKey = (String) request.getParameter("contentNaturalKey");
		Customer customer = getCustomer(request);
		if (customer == null) {
			return null;
		}
		
		ContentSessionBean contentSessionBean = getContentBean(request).getContentSessionBean();
		Site site = contentSessionBean.getSiteDomain().getSite();
		String commentTitle = request.getParameter("commentTitle");
		commentTitle = Utility.escapeStrictHTML(commentTitle);
		String commentLine = request.getParameter("comment");
		commentLine = Utility.escapeStrictHTML(commentLine);
		if (!Format.isNullOrEmpty(commentTitle) ||  !Format.isNullOrEmpty(commentLine)) {		
			EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
			Content content = ContentDAO.loadNatural(site.getSiteId(), Utility.encode(contentNaturalKey));
			Comment comment = new Comment();
			comment.setCommentTitle(commentTitle);
			comment.setComment(commentLine);
			comment.setActive(Constants.VALUE_YES);
			String custName = customer.getCustEmail();
			if (custName.length() > 20) {
				custName = custName.substring(0, 19);
			}
			comment.setRecCreateBy(custName);
			comment.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			comment.setRecUpdateBy(custName);
			comment.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			comment.setCustomer(customer);
			comment.setCommentRating(0);
			content.getComments().add(comment);
			em.persist(comment);
		}
    
        ActionForward forward = actionMapping.findForward("commentSuccess") ;
        forward = new ActionForward(forward.getPath() + 
									contentSessionBean.getSiteDomain().getSiteDomainPrefix() + "/" +
        							contentSessionBean.getSiteProfile().getSiteProfileClass().getSiteProfileClassName() + "/" +
        							"contentComment/" + 
        							contentNaturalKey, 
        							forward.getRedirect());
        return forward;
    }
    	
    public ActionForward rate(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
		String contentNaturalKey = (String) request.getParameter("contentNaturalKey");
		String value = (String) request.getParameter("rate");
		ContentSessionBean contentSessionBean = getContentBean(request).getContentSessionBean();
		Site site = contentSessionBean.getSiteDomain().getSite();
		String siteId = site.getSiteId();
		if (value != null) {
			EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
			Content content = ContentDAO.loadNatural(siteId, contentNaturalKey);
			int contentRatingCount = content.getContentRatingCount().intValue();
			float contentRating = content.getContentRating().floatValue();
			
			int rate = Integer.parseInt(value);
			if (contentRatingCount != 0) {
				contentRating = ((contentRating * contentRatingCount) + rate) / (contentRatingCount + 1);
				contentRatingCount += 1;
			}
			else {
				contentRatingCount = 1;
				contentRating = rate;
			}
			content.setContentRating(new Float(contentRating));
			content.setContentRatingCount(new Integer(contentRatingCount));
			em.persist(content);
		}
        ActionForward forward = actionMapping.findForward("success") ;
        forward = new ActionForward(forward.getPath() + 
									contentSessionBean.getSiteDomain().getSiteDomainPrefix() + "/" +
									contentSessionBean.getSiteProfile().getSiteProfileClass().getSiteProfileClassName() + "/" +
        							"/contentComment/" + 
									contentNaturalKey, forward.getRedirect());
        return forward;
    }
    
    public ActionForward itemCompareAdd(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	Vector<String> itemCompareList = ContentLookupDispatchAction.getItemCompareList(request);
    	String itemId = request.getParameter("itemId");
    	Iterator<?> iterator = itemCompareList.iterator();
    	boolean found = false;
    	while (iterator.hasNext()) {
    		String id = (String) iterator.next();
    		if (id.equals(itemId)) {
    			found = true;
    		}
    	}
    	if (!found) {
    		itemCompareList.add(itemId);
    	}
    	
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		streamWebService(response, jsonResult.toHtmlString());
    	return null;
    }
    
    public ActionForward itemCompareRemove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	String itemIds[] = request.getParameterValues("itemId");
    	Iterator<?> iterator = ContentLookupDispatchAction.getItemCompareList(request).iterator();
    	while (iterator.hasNext()) {
    		String itemId = (String) iterator.next();
    		for (int i = 0; i < itemIds.length; i++) {
    			if (itemId.equals(itemIds[i])) {
    				iterator.remove();
    			}
    		}
    	}
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		streamWebService(response, jsonResult.toHtmlString());
    	return null;
    }
    
    public ActionForward itemCompareList(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	ContentApi api = new ContentApi(request);
    	Vector<ItemInfo> dataList = new Vector<ItemInfo>();
    	Iterator<?> iterator = ContentLookupDispatchAction.getItemCompareList(request).iterator();
    	while (iterator.hasNext()) {
    		String itemId = (String) iterator.next();
    		ItemInfo itemInfo = api.getItem(Format.getLong(itemId), false);
    		dataList.add(itemInfo);
    	}
    	
    	// merge with template and stream.
		TemplateEngine engine = TemplateEngine.getInstance();
		engine.init(request, this.getServlet().getServletContext(), true);
		ItemCompareInfo itemCompareInfo = new ItemCompareInfo();
		itemCompareInfo.setItemCompareList(dataList);
		String body = engine.mergeData("components/compare/compareItemList.vm", "itemCompareInfo", itemCompareInfo);
		streamWebService(response, body);
    	return null;
    }
    
    public ActionForward getStates(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	
    	Country country = CountryDAO.loadByCountryCode(site.getSiteId(), request.getParameter("countryCode"));
    	Vector<JSONEscapeObject> jsonStates = new Vector<JSONEscapeObject>();
    	
    	if (country != null) {
    		for (State state : country.getStates()) {
    			JSONEscapeObject jsonState = new JSONEscapeObject();
    			jsonState.put("stateCode", state.getStateCode());
    			jsonState.put("stateName", state.getStateName());
    			jsonStates.add(jsonState);
    		}
    	}
    	JSONEscapeObject object = new JSONEscapeObject();
    	object.put("states", jsonStates);
    	this.streamWebService(response, object.toString());
    	
    	return null;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("rate", "rate");
        map.put("comment", "comment");
        map.put("alert", "alert");
        map.put("itemCompareAdd", "itemCompareAdd");
        map.put("itemCompareRemove", "itemCompareRemove");
        map.put("itemCompareList", "itemCompareList");
        map.put("registration", "registration");
        map.put("signin", "signin");
        map.put("signinStatus", "signinStatus");
        map.put("getStates", "getStates");
        return map;
    }
}