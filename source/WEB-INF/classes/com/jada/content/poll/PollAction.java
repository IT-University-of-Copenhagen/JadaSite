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

package com.jada.content.poll;

import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.persistence.EntityManager;

import com.jada.util.JSONEscapeObject;

import com.jada.content.ContentAction;
import com.jada.content.ContentBean;
import com.jada.dao.PollDetailDAO;
import com.jada.dao.PollHeaderDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.PollDetail;
import com.jada.jpa.entity.PollHeader;
import com.jada.jpa.entity.Site;
import com.jada.util.Format;

public class PollAction extends ContentAction {
    Logger logger = Logger.getLogger(PollAction.class);

    public ActionForward performAction(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	String process = request.getParameter("process");
    	String jsonString = null;
    	if (process.equals("get")) {
    		jsonString = getActivePoll(request);
    	}
    	if (process.equals("update")) {
    		jsonString = updatePoll(request);
    	}
    	streamWebService(response, jsonString);
    	return null;
    }
    
    public String getActivePoll(HttpServletRequest request) throws Exception {
        String siteId = getContentBean(request).getSiteDomain().getSite().getSiteId();
        PollHeader pollHeader = PollHeaderDAO.getActivePoll(siteId);
        return getPollInfo(pollHeader);
    }
    
    public String getPollInfo(PollHeader pollHeader) throws Exception {
    	String jsonString = "";
        JSONEscapeObject json = new JSONEscapeObject();
        if (pollHeader != null) {
        	json.put("pollHeaderId", Format.getLong(pollHeader.getPollHeaderId()));
        	json.put("pollTopic", pollHeader.getPollTopic());
        	Iterator<?> iterator = pollHeader.getPollDetails().iterator();
        	int total = 0;
        	while (iterator.hasNext()) {
        		PollDetail pollDetail = (PollDetail) iterator.next();
        		total += pollDetail.getPollVoteCount().intValue();
        	}
        	
        	Vector<JSONEscapeObject> vector = new Vector<JSONEscapeObject>();
        	iterator = pollHeader.getPollDetails().iterator();
        	while (iterator.hasNext()) {
        		PollDetail pollDetail = (PollDetail) iterator.next();
            	JSONEscapeObject jsonDetails = new JSONEscapeObject();
        		jsonDetails.put("pollDetailId", pollDetail.getPollDetailId());
        		jsonDetails.put("pollOption", pollDetail.getPollOption());
        		String percentage = "";
        		if (total > 0) {
        			percentage = String.valueOf(pollDetail.getPollVoteCount().intValue() * 100 / total) + "%";
        		}
        		jsonDetails.put("percentage", percentage);
        		vector.add(jsonDetails);
        	}
        	json.put("pollDetails", vector);
        }
        jsonString = json.toHtmlString();

    	return jsonString;
    }
    
    public String updatePoll(HttpServletRequest request) throws Exception {
        ContentBean contentBean = getContentBean(request);
        Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();

        String pollHeaderId = request.getParameter("pollHeaderId");
    	String pollDetailId = request.getParameter("pollDetailId");
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        if (!Format.isNullOrEmpty(pollDetailId)) {
	        PollDetail pollDetail = PollDetailDAO.load(Format.getLong(pollDetailId));
	        pollDetail.setPollVoteCount(new Integer(pollDetail.getPollVoteCount().intValue() + 1));
	    	em.persist(pollDetail);
	    	em.flush();
        }
    	
    	PollHeader pollHeader = PollHeaderDAO.load(site.getSiteId(), Format.getLong(pollHeaderId));
    	
    	return getPollInfo(pollHeader);
    }
}
