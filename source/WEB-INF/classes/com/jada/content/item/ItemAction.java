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

package com.jada.content.item;

import com.jada.content.ContentLookupDispatchAction;
import com.jada.content.ContentSessionBean;
import com.jada.dao.ItemDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Comment;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.Site;
import com.jada.util.Constants;
import com.jada.util.Format;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import javax.persistence.EntityManager;
import com.jada.util.JSONEscapeObject;

public class ItemAction extends ContentLookupDispatchAction {
    Logger logger = Logger.getLogger(ItemAction.class);

    public ActionForward alert(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	String commentId = request.getParameter("commentId"); 
    	String alertType = request.getParameter("alertType");
		Customer customer = getCustomer(request);
		if (customer == null) {
	    	JSONEscapeObject jsonResult = new JSONEscapeObject();
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			MessageResources resources = this.getResources(request);
			jsonResult.put("message", resources.getMessage("error.content.signin"));
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
		String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
    	return null;
    }
    
    public ActionForward rate(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
		String itemNaturalKey = (String) request.getParameter("itemNaturalKey");
		String value = (String) request.getParameter("rate");
		ContentSessionBean contentSessionBean = getContentBean(request).getContentSessionBean();
		Site site = contentSessionBean.getSiteDomain().getSite();
		String siteId = site.getSiteId();
		if (value != null) {
			EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
			Item item = ItemDAO.loadNatural(siteId, itemNaturalKey);
			int itemRatingCount = item.getItemRatingCount().intValue();
			float itemRating = item.getItemRating().floatValue();
			
			int rate = Integer.parseInt(value);
			if (itemRatingCount != 0) {
				itemRating = ((itemRating * itemRatingCount) + rate) / (itemRatingCount + 1);
				itemRatingCount += 1;
			}
			else {
				itemRatingCount = 1;
				itemRating = rate;
			}
			item.setItemRating(new Float(itemRating));
			item.setItemRatingCount(new Integer(itemRatingCount));
			em.persist(item);
		}
        ActionForward forward = actionMapping.findForward("success") ;
        forward = new ActionForward(forward.getPath() + 
									contentSessionBean.getSiteDomain().getSiteDomainPrefix() +
									"/" + contentSessionBean.getSiteProfile().getSiteProfileClass().getSiteProfileClassName() +
									"/item/" +
									itemNaturalKey, forward.getRedirect());
        return forward;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("rate", "rate");
        map.put("alert", "alert");
        return map;
    }
}