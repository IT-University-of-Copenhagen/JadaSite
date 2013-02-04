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
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.EntityManager;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.PollDetailDAO;
import com.jada.dao.PollHeaderDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.PollDetail;
import com.jada.jpa.entity.PollHeader;
import com.jada.jpa.entity.Site;
import com.jada.util.Constants;
import com.jada.util.Format;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

public class PollMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse)
        throws Throwable {

        PollMaintActionForm form = (PollMaintActionForm) actionForm;
        if (form == null) {
            form = new PollMaintActionForm();
        }
        form.setPublished(String.valueOf(Constants.PUBLISHED_YES));
        form.setMode("C");
        
        FormUtils.setFormDisplayMode(httpServletRequest, form, FormUtils.CREATE_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

        PollMaintActionForm form = (PollMaintActionForm) actionForm;
        if (form == null) {
            form = new PollMaintActionForm();
        }
		String pollHeaderId = request.getParameter("pollHeaderId");
        PollHeader pollHeader = new PollHeader();
        pollHeader = PollHeaderDAO.load(getAdminBean(request).getSite().getSiteId(), Format.getLong(pollHeaderId));
        form.setMode("U");
		form.setPollHeaderId(Format.getLong(pollHeader.getPollHeaderId()));
		form.setPollTopic(pollHeader.getPollTopic());
		form.setPollPublishOn(Format.getDate(pollHeader.getPollPublishOn()));
		form.setPollExpireOn(Format.getDate(pollHeader.getPollExpireOn()));
		form.setPublished(String.valueOf(pollHeader.getPublished()));
		
		refreshDetails(form, pollHeader);

        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public void refreshDetails(PollMaintActionForm form, PollHeader pollHeader) {
		Iterator<?> iterator = pollHeader.getPollDetails().iterator();
		int totalVoteCount = 0;
		while (iterator.hasNext()) {
			PollDetail pollDetail = (PollDetail) iterator.next();
			totalVoteCount += pollDetail.getPollVoteCount().intValue();
		}
		
		Vector<PollDetailForm> vector = new Vector<PollDetailForm>();
		iterator = pollHeader.getPollDetails().iterator();
		while (iterator.hasNext()) {
			PollDetail pollDetail = (PollDetail) iterator.next();
			PollDetailForm pollDetailForm = new PollDetailForm();
			pollDetailForm.setPollDetailId(Format.getLong(pollDetail.getPollDetailId()));
			pollDetailForm.setPollOption(pollDetail.getPollOption());
			pollDetailForm.setSeqNum(Format.getInt(pollDetail.getSeqNum()));
			pollDetailForm.setPollVoteCount(Format.getInt(pollDetail.getPollVoteCount()));
			if (totalVoteCount > 0) {
				pollDetailForm.setPollPercentage(Format.getInt(pollDetail.getPollVoteCount() * 100 / totalVoteCount) + "%");
			}
			vector.add(pollDetailForm);
		}
		PollDetailForm pollDetails[] = new PollDetailForm[vector.size()];
		vector.copyInto(pollDetails);
		form.setPollDetails(pollDetails);
    }
    
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		PollMaintActionForm form = (PollMaintActionForm) actionForm;
		PollHeader pollHeader = PollHeaderDAO.load(getAdminBean(request).getSite().getSiteId(), Format.getLong(form.getPollHeaderId()));
		em.remove(pollHeader);
		ActionForward actionForward = actionMapping.findForward("removeSuccess");
		return actionForward;
	}

	public ActionForward save(ActionMapping mapping, 
							  ActionForm actionForm,
							  HttpServletRequest request,
							  HttpServletResponse response) 
		throws Throwable {

    	MessageResources resources = this.getResources(request);
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		boolean insertMode = false;
		PollMaintActionForm form = (PollMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}

		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();

        boolean isError = false;
        for (int i = 0; i < form.getPollDetails().length; i++) {
        	PollDetailForm pollDetailForm = form.getPollDetails()[i];
        	pollDetailForm.setSeqNumError("");
        	pollDetailForm.setPollOptionError("");
        	if (!Format.isInt(pollDetailForm.getSeqNum())) {
        		pollDetailForm.setSeqNumError(resources.getMessage("error.int.invalid"));
        		isError = true;
        	}
        	if (Format.isNullOrEmpty(pollDetailForm.getPollOption())) {
        		pollDetailForm.setPollOptionError(resources.getMessage("error.string.required"));
        		isError = true;
        	}
        }
        if (isError) {
			return mapping.findForward("error");	
        }

		PollHeader pollHeader = new PollHeader();
		if (!insertMode) {
			pollHeader = PollHeaderDAO.load(site.getSiteId(), Format.getLong(form.getPollHeaderId()));
		}

		ActionMessages errors = validate(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return mapping.findForward("error");
		}

		if (insertMode) {
			pollHeader.setRecCreateBy(adminBean.getUser().getUserId());
			pollHeader.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		pollHeader.setSite(site);
		pollHeader.setPollTopic(form.getPollTopic());
		pollHeader.setPollPublishOn(Format.getDate(form.getPollPublishOn()));
		pollHeader.setPollExpireOn(Format.getDate(form.getPollExpireOn()));
		pollHeader.setPublished(Constants.PUBLISHED_NO);
		if (form.getPublished() != null && form.getPublished().equals(String.valueOf(Constants.PUBLISHED_YES))) {
			pollHeader.setPublished(Constants.PUBLISHED_YES);
		}
		pollHeader.setRecUpdateBy(adminBean.getUser().getUserId());
		pollHeader.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		if (insertMode) {
			em.persist(pollHeader);
		}
		else {
			// em.update(pollHeader);
		}
        form.setPollHeaderId(Format.getLong(pollHeader.getPollHeaderId()));
		form.setMode("U");
		
		for (int i = 0; i < form.getPollDetails().length; i++) {
        	PollDetailForm pollDetailForm = form.getPollDetails()[i];
        	PollDetail pollDetail = PollDetailDAO.load(Format.getLong(pollDetailForm.getPollDetailId()));
        	pollDetail.setSeqNum(Format.getIntObj(pollDetailForm.getSeqNum()));
        	pollDetail.setPollOption(pollDetailForm.getPollOption());
        	pollDetail.setRecUpdateBy(adminBean.getUser().getUserId());
        	pollDetail.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    		em.persist(pollDetail);
		}
		
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
    public ActionForward addPollDetail(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
        PollMaintActionForm form = (PollMaintActionForm) actionForm;
        
        String pollOption = form.getNewPollOption();
        if (Format.isNullOrEmpty(pollOption)) {
    		ActionMessages errors = new ActionMessages();
    		errors.add("newPollOption", new ActionMessage("error.string.required"));
			saveMessages(request, errors);
			return actionMapping.findForward("error");
        }
        
        Long pollHeaderId = Format.getLong(form.getPollHeaderId());
        PollHeader pollHeader = PollHeaderDAO.load(site.getSiteId(), pollHeaderId);
        int seqNum = -1;
        Iterator<?> iterator = pollHeader.getPollDetails().iterator();
        while (iterator.hasNext()) {
        	PollDetail pollDetail = (PollDetail) iterator.next();
        	if (pollDetail.getSeqNum().intValue() > seqNum) {
        		seqNum = pollDetail.getSeqNum().intValue();
        	}
        }
        
        PollDetail pollDetail = new PollDetail();
        pollDetail.setPollVoteCount(new Integer(0));
        pollDetail.setPollOption(pollOption);
        pollDetail.setPollVoteCount(new Integer(0));
        pollDetail.setSeqNum(new Integer(seqNum + 1));
        pollDetail.setRecUpdateBy(adminBean.getUser().getUserId());
        pollDetail.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        pollDetail.setRecCreateBy(adminBean.getUser().getUserId());
        pollDetail.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		pollHeader.setRecUpdateBy(adminBean.getUser().getUserId());
		pollHeader.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		pollHeader.getPollDetails().add(pollDetail);
		em.persist(pollDetail);

        em.flush();
        
		refreshDetails(form, pollHeader);
		form.setNewPollOption("");

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward removePollDetails(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        PollMaintActionForm form = (PollMaintActionForm) actionForm;
    	PollHeader pollHeader = PollHeaderDAO.load(getAdminBean(request).getSite().getSiteId(), Format.getLong(form.getPollHeaderId()));
    	Vector<PollDetail> removeDetails = new Vector<PollDetail>();
        for (int i = 0; i < form.getPollDetails().length; i++) {
        	PollDetailForm pollDetailForm = form.getPollDetails()[i];
        	if (!pollDetailForm.isRemove()) {
        		continue;
        	}
        	Iterator<?> iterator = pollHeader.getPollDetails().iterator();
        	while (iterator.hasNext()) {
        		PollDetail pollDetail = (PollDetail) iterator.next();
        		if (pollDetail.getPollDetailId().equals(Format.getLong(pollDetailForm.getPollDetailId()))) {
        			em.remove(pollDetail);
        			removeDetails.add(pollDetail);
        		}
        	}
        }
        
        Iterator<?> iterator = removeDetails.iterator();
        while (iterator.hasNext()) {
        	pollHeader.getPollDetails().remove(iterator.next());
        }
        
		refreshDetails(form, pollHeader);
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward resequence(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	MessageResources resources = this.getResources(request);
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		AdminBean adminBean = getAdminBean(request);
        PollMaintActionForm form = (PollMaintActionForm) actionForm;
        boolean isError = false;
        for (int i = 0; i < form.getPollDetails().length; i++) {
        	PollDetailForm pollDetailForm = form.getPollDetails()[i];
    		pollDetailForm.setSeqNumError("");
        	if (!Format.isInt(pollDetailForm.getSeqNum())) {
        		pollDetailForm.setSeqNumError(resources.getMessage("error.int.invalid"));
        		isError = true;
        	}
        }
        if (isError) {
			return actionMapping.findForward("error");	
        }
        
    	PollHeader pollHeader = PollHeaderDAO.load(adminBean.getSite().getSiteId(), Format.getLong(form.getPollHeaderId()));
        for (int i = 0; i < form.getPollDetails().length; i++) {
        	PollDetailForm pollDetailForm = form.getPollDetails()[i];
        	PollDetail pollDetail = PollDetailDAO.load(Format.getLong(pollDetailForm.getPollDetailId()));
        	pollDetail.setSeqNum(Format.getIntObj(pollDetailForm.getSeqNum()));
            pollDetail.setRecUpdateBy(adminBean.getUser().getUserId());
            pollDetail.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
            em.persist(pollDetail);
        }
        em.flush();
        em.refresh(pollHeader);
        
		refreshDetails(form, pollHeader);
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionMessages validate(PollMaintActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	
    	if (Format.isNullOrEmpty(form.getPollTopic())) {
    		errors.add("pollTopic", new ActionMessage("error.string.required"));
    	}
    	if (!Format.isDate(form.getPollPublishOn())) {
       		errors.add("pollPublishOn", new ActionMessage("error.date.invalid"));
    	}
    	if (!Format.isDate(form.getPollExpireOn())) {
       		errors.add("pollExpireOn", new ActionMessage("error.date.invalid"));
    	}
    	return errors;
    }
	
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("save", "save");
        map.put("edit", "edit");
        map.put("create", "create");
        map.put("remove", "remove");
        map.put("addPollDetail", "addPollDetail");
        map.put("removePollDetails", "removePollDetails");
        map.put("resequence", "resequence");
        return map;
    }
}
