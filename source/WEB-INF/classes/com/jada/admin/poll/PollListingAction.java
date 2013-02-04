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

package com.jada.admin.poll;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminListingAction;
import com.jada.admin.AdminListingActionForm;
import com.jada.dao.PollHeaderDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.PollHeader;
import com.jada.jpa.entity.Site;
import com.jada.util.Format;
import com.jada.util.Utility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;


import javax.persistence.Query;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class PollListingAction
    extends AdminListingAction {

    public void extract(AdminListingActionForm actionForm, HttpServletRequest request) throws Throwable {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        PollListingActionForm form = (PollListingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();

        ActionMessages errors = validate(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return;
		}

        Query query = null;
        String sql = "from PollHeader where siteId = :siteId ";
        if (form.getSrPollTopic().length() > 0) {
        	sql += "and pollTopic like :pollTopic ";
        }
        if (!form.getSrPublished().equals("*")) {
        	sql += "and published = :published ";
        }
        sql += "and pollPublishOn between :pollPublishOnStart and :pollPublishOnEnd ";
        sql += "and pollExpireOn between :pollExpireOnStart and :pollExpireOnEnd ";
        if (!form.getSrUpdateBy().equals("All")) {
        	sql += "and recUpdateBy = :recUpdateBy ";
        }
        if (!form.getSrCreateBy().equals("All")) {
        	sql += "and recCreateBy = :recCreateBy ";
        }

        query = em.createQuery(sql);
        Date date = null;
        query.setParameter("siteId", site.getSiteId());
        if (form.getSrPollTopic().length() > 0) {
        	query.setParameter("pollTopic", "%" + form.getSrPollTopic() + "%");
        }
        if (!form.getSrPublished().equals("*")) {
        	query.setParameter("published", form.getSrPublished());
        }
        if (form.getSrPollPublishOnStart().length() > 0) {
        	date = Format.getDate(form.getSrPollPublishOnStart());
        	query.setParameter("pollPublishOnStart", date);
        }
        else {
        	query.setParameter("pollPublishOnStart", Format.LOWDATE);
        }
        if (form.getSrPollPublishOnEnd().length() > 0) {
        	date = Format.getDate(form.getSrPollPublishOnEnd());
        	query.setParameter("pollPublishOnEnd", date);
        }
        else {
        	query.setParameter("pollPublishOnEnd", Format.HIGHDATE);
        }
        if (form.getSrPollExpireOnStart().length() > 0) {
        	date = Format.getDate(form.getSrPollExpireOnStart());
        	query.setParameter("pollExpireOnStart", date);
        }
        else {
        	query.setParameter("pollExpireOnStart", Format.LOWDATE);
        }
        if (form.getSrPollExpireOnEnd().length() > 0) {
        	date = Format.getDate(form.getSrPollExpireOnEnd());
        	query.setParameter("pollExpireOnEnd", date);
        }
        else {
        	query.setParameter("pollExpireOnEnd", Format.HIGHDATE);
        }
        if (!form.getSrUpdateBy().equals("All")) {
        	query.setParameter("recUpdateBy", form.getSrUpdateBy());
        }
        if (!form.getSrCreateBy().equals("All")) {
        	query.setParameter("recCreateBy", form.getSrCreateBy());
        }
        List<?> list = query.getResultList();
        if (Format.isNullOrEmpty(form.getSrPageNo())) {
        	form.setSrPageNo("1");
        }
        int pageNo = Integer.parseInt(form.getSrPageNo());
        calcPage(adminBean, form, list, pageNo);
        Vector<PollDisplayForm> vector = new Vector<PollDisplayForm>();
        int startRecord = (form.getPageNo() - 1) * adminBean.getListingPageSize();
        int endRecord = startRecord + adminBean.getListingPageSize();
        for (int i = startRecord; i < list.size() && i < endRecord; i++) {
        	PollHeader pollHeader = (PollHeader) list.get(i);
        	PollDisplayForm pollDisplay = new PollDisplayForm();
        	pollDisplay.setPollHeaderId(Format.getLong(pollHeader.getPollHeaderId()));
        	pollDisplay.setPollTopic(pollHeader.getPollTopic());
        	pollDisplay.setPublished(String.valueOf(pollHeader.getPublished()));
        	pollDisplay.setPollPublishOn(Format.getFullDate(pollHeader.getPollPublishOn()));
        	pollDisplay.setPollExpireOn(Format.getFullDate(pollHeader.getPollExpireOn()));
            vector.add(pollDisplay);
        }
        form.setPolls(vector);
        
        initSearchInfo(form, site.getSiteId());
    }
    
    public void initSearchInfo(AdminListingActionForm actionForm, String siteId) throws Exception {
    	PollListingActionForm form = (PollListingActionForm) actionForm;
    	if (form.getSrPublished() == null) {
    		form.setSrPublished("*");
    	}
     	Vector<String> userIdVector = Utility.getUserIdsForSite(siteId);
    	String srSelectUsers[] = new String[userIdVector.size()];
    	userIdVector.copyInto(srSelectUsers);
    	form.setSrSelectUsers(srSelectUsers);
    }

    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        PollListingActionForm form = (PollListingActionForm) actionForm;
        String pollHeaderIds[] = form.getPollHeaderIds();
        
        if (pollHeaderIds != null) {
	        for (int i = 0; i < pollHeaderIds.length; i++) {
	            PollHeader pollHeader = new PollHeader();
	            pollHeader = PollHeaderDAO.load(getAdminBean(request).getSite().getSiteId(), Format.getLong(pollHeaderIds[i]));
	            em.remove(pollHeader);
	        }
        }

        ActionForward forward = actionMapping.findForward("removed") ;
        forward = new ActionForward(forward.getPath() + "?process=list&srPageNo=" + form.getPageNo(), forward.getRedirect());
        return forward;
    }
    
    public ActionMessages validate(PollListingActionForm form) throws Exception {
    	ActionMessages errors = new ActionMessages();
    	
    	if (!Format.isNullOrEmpty(form.getSrPollPublishOnStart()) && !Format.isDate(form.getSrPollPublishOnStart())) {
    		errors.add("srPollPublishOnStart", new ActionMessage("error.date.invalid"));
    	}
    	if (!Format.isNullOrEmpty(form.getSrPollPublishOnEnd()) && !Format.isDate(form.getSrPollPublishOnEnd())) {
    		errors.add("srPollPublishOnEnd", new ActionMessage("error.date.invalid"));
    	}
    	if (!Format.isNullOrEmpty(form.getSrPollExpireOnStart()) && !Format.isDate(form.getSrPollExpireOnStart())) {
    		errors.add("srPollExpireOnStart", new ActionMessage("error.date.invalid"));
    	}
    	if (!Format.isNullOrEmpty(form.getSrPollExpireOnEnd()) && !Format.isDate(form.getSrPollExpireOnEnd())) {
    		errors.add("srPollExpireOnEnd", new ActionMessage("error.date.invalid"));
    	}
    	return errors;
    }

    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("remove", "remove");
        map.put("list", "list");
        map.put("start", "start");
        map.put("back", "back");
        map.put("search", "search");
        return map;
    }

	public void initForm(AdminListingActionForm form) {
		((PollListingActionForm) form).setPolls(null);
	}
}