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

package com.jada.admin.moderation;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import com.jada.util.JSONEscapeObject;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminListingAction;
import com.jada.admin.AdminListingActionForm;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Comment;
import com.jada.jpa.entity.Content;
import com.jada.jpa.entity.ContentLanguage;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.ItemLanguage;
import com.jada.jpa.entity.Site;
import com.jada.util.Constants;
import com.jada.util.Format;

public class ModerationMaintAction
    extends AdminListingAction {
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        ActionForward actionForward = actionMapping.findForward("success");
    	ModerationMaintActionForm form = (ModerationMaintActionForm) actionForm;
    	form.setSrNotModerated(true);
    	form.setSrFlagged(true);
        form.setComments(null);
        return actionForward;
    }
    
    
    public ActionForward approve(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		AdminBean adminBean = getAdminBean(request);
    	ModerationMaintActionForm form = (ModerationMaintActionForm) actionForm;
    	if (form.getComments() != null) {
    		CommentDisplayForm comments[] = form.getComments();
    		for (int i = 0; i < comments.length; i++) {
    			if (comments[i].getSelect() == null) {
    				continue;
    			}
    			Comment comment = (Comment) em.find(Comment.class, Format.getLong(comments[i].getCommentId()));
    			comment.setCommentApproved('Y');
    			comment.setRecUpdateBy(adminBean.getUser().getUserId());
    			comment.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    			em.persist(comment);
    		}
    	}
        ActionForward forward = actionMapping.findForward("approved") ;
        forward = new ActionForward(forward.getPath() + "?process=list&srPageNo=" + form.getPageNo(), forward.getRedirect());
        return forward;
    }

    public ActionForward reject(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		AdminBean adminBean = getAdminBean(request);
    	ModerationMaintActionForm form = (ModerationMaintActionForm) actionForm;
    	if (form.getComments() != null) {
    		CommentDisplayForm comments[] = form.getComments();
    		for (int i = 0; i < comments.length; i++) {
    			if (comments[i].getSelect() == null) {
    				continue;
    			}
    			Comment comment = (Comment) em.find(Comment.class, Format.getLong(comments[i].getCommentId()));
    			comment.setCommentApproved('N');
    			comment.setRecUpdateBy(adminBean.getUser().getUserId());
    			comment.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    			em.persist(comment);
    		}
    	}
        ActionForward forward = actionMapping.findForward("approved") ;
        forward = new ActionForward(forward.getPath() + "?process=list&srPageNo=" + form.getSrPageNo(), forward.getRedirect());
        return forward;
    }
    
    public ActionForward showContent(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	ModerationMaintActionForm form = (ModerationMaintActionForm) actionForm;
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        JSONEscapeObject json = new JSONEscapeObject();
        Iterator<?> iterator = null;
    	Content content = (Content) em.find(Content.class, Format.getLong(form.getContentId()));
		ContentLanguage contentLanguage = null;
		for (ContentLanguage language : content.getContentLanguages()) {
			if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassDefaultId())) {
				contentLanguage = language;
			}
		}
        json.put("contentTitle", contentLanguage.getContentTitle());
        json.put("contentShortDesc", contentLanguage.getContentShortDesc());
        json.put("contentDesc", contentLanguage.getContentDesc());
        iterator = content.getComments().iterator();
    	
       	Vector<JSONEscapeObject> comments = new Vector<JSONEscapeObject>();
       	while (iterator.hasNext()) {
       		Comment comment = (Comment) iterator.next();
       		JSONEscapeObject jsonComment = new JSONEscapeObject();
       		jsonComment.put("commentTitle", comment.getCommentTitle());
       		jsonComment.put("comment", comment.getComment());
       		jsonComment.put("agreeCount", comment.getAgreeCustomers().size());
       		jsonComment.put("disagreeCount", comment.getDisagreeCustomers().size());
       		jsonComment.put("moderation", String.valueOf(comment.getModeration()));
       		jsonComment.put("commentApproved", String.valueOf(comment.getCommentApproved()));
       		jsonComment.put("custEmail", comment.getCustomer().getCustEmail());
       		jsonComment.put("custPublicName", comment.getCustomer().getCustPublicName());
       		jsonComment.put("recCreateDatetime", Format.getFullDatetime(comment.getRecCreateDatetime()));
       		comments.add(jsonComment);
       	}
        json.put("comments", comments);
        String jsonString = json.toHtmlString();
        streamWebService(response, jsonString);
     	return null;
    }
    
    public ActionForward showItem(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	ModerationMaintActionForm form = (ModerationMaintActionForm) actionForm;
    	AdminBean adminBean = getAdminBean(request);
    	Site site = adminBean.getSite();
    	initSiteProfiles(form, site);
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        JSONEscapeObject json = new JSONEscapeObject();
        Iterator<?> iterator = null;
		Item item = (Item) em.find(Item.class, Format.getLong(form.getItemId()));
		ItemLanguage itemLanguage = null;
		for (ItemLanguage language : item.getItemLanguages()) {
			if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassDefaultId())) {
				itemLanguage = language;
			}
		}
		iterator = item.getComments().iterator();
		json.put("itemNum", item.getItemNum());
		json.put("itemUpcCd", item.getItemUpcCd());
		json.put("itemShortDesc", itemLanguage.getItemShortDesc());
		json.put("itemDesc", itemLanguage.getItemDesc());
    	
       	Vector<JSONEscapeObject> comments = new Vector<JSONEscapeObject>();
       	while (iterator.hasNext()) {
       		Comment comment = (Comment) iterator.next();
       		JSONEscapeObject jsonComment = new JSONEscapeObject();
       		jsonComment.put("commentTitle", comment.getCommentTitle());
       		jsonComment.put("comment", comment.getComment());
       		jsonComment.put("agreeCount", comment.getAgreeCustomers().size());
       		jsonComment.put("disagreeCount", comment.getDisagreeCustomers().size());
       		jsonComment.put("moderation", String.valueOf(comment.getModeration()));
       		jsonComment.put("commentApproved", String.valueOf(comment.getCommentApproved()));
       		jsonComment.put("custEmail", comment.getCustomer().getCustEmail());
       		jsonComment.put("custPublicName", comment.getCustomer().getCustPublicName());
       		jsonComment.put("recCreateDatetime", Format.getFullDatetime(comment.getRecCreateDatetime()));
       		comments.add(jsonComment);
       	}
        json.put("comments", comments);
        String jsonString = json.toHtmlString();
        streamWebService(response, jsonString);
     	return null;
    }

	public void extract(AdminListingActionForm actionForm, HttpServletRequest request) throws Throwable {
    	ModerationMaintActionForm form = (ModerationMaintActionForm) actionForm;
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();

        ActionMessages errors = validate(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return;
		}

        Query query = null;
        String sql = "select   comment " +
        			 "from     Comment comment " +
        			 "left     join comment.customer customer " +
        			 "where    customer.siteId = :siteId ";
        sql += "and comment.recCreateDatetime between :recCreateDatetimeStart and :recCreateDatetimeEnd ";
        sql += "and (1 = 2 ";
        if (form.isSrModerated()) {
        	sql += "or comment.commentApproved is not null ";
        }
        if (form.isSrNotModerated()) {
        	sql += "or comment.commentApproved is null ";
        }
        sql += ") ";
        sql += "and (1 = 2 ";
        if (form.isSrFlagged()) {
        	sql += "or comment.moderation = 'Y'";
        }
        if (form.isSrNotFlagged()) {
        	sql += "or comment.moderation = 'N' or comment.moderation is null";
        }
        sql += ") order by comment.recCreateDatetime";
        Date date = null;
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        if (form.getSrRecCreateDatetimeStart().length() > 0) {
        	date = Format.getDate(form.getSrRecCreateDatetimeStart());
        	query.setParameter("recCreateDatetimeStart", date);
        }
        else {
        	query.setParameter("recCreateDatetimeStart", Format.LOWDATE);
        }
        if (form.getSrRecCreateDatetimeEnd().length() > 0) {
        	date = Format.getDate(form.getSrRecCreateDatetimeEnd());
        	query.setParameter("recCreateDatetimeEnd", date);
        }
        else {
        	query.setParameter("recCreateDatetimeEnd", Format.HIGHDATE);
        }
        
        List<?> list = query.getResultList();
        if (Format.isNullOrEmpty(form.getSrPageNo())) {
        	form.setSrPageNo("1");
        }
        int pageNo = Integer.parseInt(form.getSrPageNo());
        calcPage(adminBean, form, list, pageNo);
        Vector<CommentDisplayForm> vector = new Vector<CommentDisplayForm>();
        int startRecord = (form.getPageNo() - 1) * adminBean.getListingPageSize();
        int endRecord = startRecord + adminBean.getListingPageSize();
        for (int i = startRecord; i < list.size() && i < endRecord; i++) {
        	Comment comment = (Comment) list.get(i);
        	CommentDisplayForm display = new CommentDisplayForm();
        	display.setCommentId(Format.getLong(comment.getCommentId()));
        	display.setCommentTitle(comment.getCommentTitle());
        	display.setComment(comment.getComment());
        	display.setCustEmail(comment.getCustomer().getCustEmail());
        	display.setCustPublicName(comment.getCustomer().getCustPublicName());
        	display.setAgreeCount(Format.getInt(comment.getAgreeCustomers().size()));
        	display.setDisagreeCount(Format.getInt(comment.getDisagreeCustomers().size()));
        	display.setRecUpdatedDatetime(Format.getFullDatetime(comment.getRecUpdateDatetime()));
        	if (comment.getContent() != null) {
        		display.setCommentSource("C");
        		Content content = comment.getContent();
        		display.setCommentSourceTitle(content.getContentLanguage().getContentTitle());
        		display.setCommentSourceId(Format.getLong(content.getContentId()));
        	}
        	else {
        		display.setCommentSource("I");
        		Item item = comment.getItem();
        		display.setCommentSourceTitle(item.getItemLanguage().getItemShortDesc());
        		display.setCommentSourceId(Format.getLong(comment.getItem().getItemId()));
        	}
        	if (comment.getModeration() != null && comment.getModeration() == Constants.VALUE_YES) {
        		display.setCommentModeration("Yes");
        	}
        	if (comment.getCommentApproved() != null && comment.getCommentApproved() == Constants.VALUE_YES) {
        		display.setCommentApproved("Approved");
        	}
        	if (comment.getCommentApproved() != null && comment.getCommentApproved() == Constants.VALUE_NO) {
        		display.setCommentApproved("Rejected");
        	}
        	vector.add(display);
        }
        CommentDisplayForm commentDisplayForms[] = new CommentDisplayForm[vector.size()];
        vector.copyInto(commentDisplayForms);
        form.setComments(commentDisplayForms);
	}

	public void initForm(AdminListingActionForm actionForm) {
    	ModerationMaintActionForm form = (ModerationMaintActionForm) actionForm;
    	form.setComments(null);
	}

	public void initSearchInfo(AdminListingActionForm form, String siteId) throws Exception {
	}
	
    public ActionMessages validate(ModerationMaintActionForm form) throws Exception {
    	ActionMessages errors = new ActionMessages();
    	
    	if (!Format.isNullOrEmpty(form.getSrRecCreateDatetimeStart()) && !Format.isDate(form.getSrRecCreateDatetimeStart())) {
    		errors.add("srRecCreateDatetimeStart", new ActionMessage("error.date.invalid"));
    	}
    	if (!Format.isNullOrEmpty(form.getSrRecCreateDatetimeEnd()) && !Format.isDate(form.getSrRecCreateDatetimeEnd())) {
    		errors.add("srRecCreateDatetimeEnd", new ActionMessage("error.date.invalid"));
    	}
    	return errors;
    }
	
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        map.put("search", "search");
        map.put("approve", "approve");
        map.put("reject", "reject");
        map.put("list", "list");
        map.put("showContent", "showContent");
        map.put("showItem", "showItem");
        return map;
    }
}
