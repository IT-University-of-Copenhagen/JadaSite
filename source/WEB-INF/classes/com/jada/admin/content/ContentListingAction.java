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

package com.jada.admin.content;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminListingAction;
import com.jada.admin.AdminListingActionForm;
import com.jada.dao.ContentDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Content;
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

public class ContentListingAction
    extends AdminListingAction {
	
    public void extract(AdminListingActionForm actionForm,
                        HttpServletRequest request)
        throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        ContentListingActionForm form = (ContentListingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        
        ActionMessages errors = validate(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return;
		}

        Query query = null;
        String selectedCategories[] = form.getSrSelectedCategories();
        String sql = "select   distinct content " + 
        			 "from     Content content ";
        if (selectedCategories != null) {
        	sql += "left   join content.categories category ";
        }
        sql += "where    content.siteId = :siteId ";
        if (form.getSrContentTitle().length() > 0) {
        	sql += "and content.contentLanguage.contentTitle like :contentTitle ";
        }
        if (!form.getSrPublished().equals("*")) {
        	sql += "and content.published = :published ";
        }
        sql += "and content.contentPublishOn between :contentPublishOnStart and :contentPublishOnEnd ";
        sql += "and content.contentExpireOn between :contentExpireOnStart and :contentExpireOnEnd ";
        if (!form.getSrUpdateBy().equals("All")) {
        	sql += "and content.recUpdateBy = :recUpdateBy ";
        }
        if (!form.getSrCreateBy().equals("All")) {
        	sql += "and content.recCreateBy = :recCreateBy ";
        }
        
        if (selectedCategories != null) {
        	sql += "and category.catId in (";
        	int index = 0;
        	for (int i = 0; i < selectedCategories.length; i++) {
        		Long catIds[] = Utility.getCatIdTreeList(site.getSiteId(), Format.getLong(selectedCategories[i]));
        		for (int j = 0; j < catIds.length; j++) {
	        		if (index > 0) {
	        			sql += ",";
	        		}
	        		sql += ":selectedCategory" + index++;
        		}
            	sql += ") ";
        	}
        }
        sql += "order by content.contentLanguage.contentTitle ";

        query = em.createQuery(sql);
        Date date = null;
        query.setParameter("siteId", site.getSiteId());
        if (form.getSrContentTitle().length() > 0) {
        	query.setParameter("contentTitle", "%" + form.getSrContentTitle() + "%");
        }
        if (!form.getSrPublished().equals("*")) {
        	query.setParameter("published", form.getSrPublished());
        }
        if (form.getSrContentPublishOnStart().length() > 0) {
        	date = Format.getDate(form.getSrContentPublishOnStart());
        	query.setParameter("contentPublishOnStart", date);
        }
        else {
        	query.setParameter("contentPublishOnStart", Format.LOWDATE);
        }
        if (form.getSrContentPublishOnEnd().length() > 0) {
        	date = Format.getDate(form.getSrContentPublishOnEnd());
        	query.setParameter("contentPublishOnEnd", date);
        }
        else {
        	query.setParameter("contentPublishOnEnd", Format.HIGHDATE);
        }
        if (form.getSrContentExpireOnStart().length() > 0) {
        	date = Format.getDate(form.getSrContentExpireOnStart());
        	query.setParameter("contentExpireOnStart", date);
        }
        else {
        	query.setParameter("contentExpireOnStart", Format.LOWDATE);
        }
        if (form.getSrContentExpireOnEnd().length() > 0) {
        	date = Format.getDate(form.getSrContentExpireOnEnd());
        	query.setParameter("contentExpireOnEnd", date);
        }
        else {
        	query.setParameter("contentExpireOnEnd", Format.HIGHDATE);
        }
        if (!form.getSrUpdateBy().equals("All")) {
        	query.setParameter("recUpdateBy", form.getSrUpdateBy());
        }
        if (!form.getSrCreateBy().equals("All")) {
        	query.setParameter("recCreateBy", form.getSrCreateBy());
        }
        if (selectedCategories != null) {
        	int index = 0;
        	for (int i = 0; i < selectedCategories.length; i++) {
        		Long catIds[] = Utility.getCatIdTreeList(site.getSiteId(), Format.getLong(selectedCategories[i]));
        		for (int j = 0; j < catIds.length; j++) {
        			query.setParameter("selectedCategory" + index++, catIds[j].longValue());
        		}
        	}
        }
        List<?> list = query.getResultList();
        if (Format.isNullOrEmpty(form.getSrPageNo())) {
        	form.setSrPageNo("1");
        }
        int pageNo = Integer.parseInt(form.getSrPageNo());
        calcPage(adminBean, form, list, pageNo);
        Vector<ContentDisplayForm> vector = new Vector<ContentDisplayForm>();
        int startRecord = (form.getPageNo() - 1) * adminBean.getListingPageSize();
        int endRecord = startRecord + adminBean.getListingPageSize();
        for (int i = startRecord; i < list.size() && i < endRecord; i++) {
        	Content content = (Content) list.get(i);        	
        	ContentDisplayForm contentDisplay = new ContentDisplayForm();
        	contentDisplay.setContentId(Format.getLong(content.getContentId()));
        	contentDisplay.setContentTitle(content.getContentLanguage().getContentTitle());
        	contentDisplay.setPublished(String.valueOf(content.getPublished()));
        	contentDisplay.setContentPublishOn(Format.getFullDate(content.getContentPublishOn()));
        	contentDisplay.setContentExpireOn(Format.getFullDate(content.getContentExpireOn()));
            vector.add(contentDisplay);
        }
        ContentDisplayForm contents[] = new ContentDisplayForm[vector.size()];
        vector.copyInto(contents);
        form.setContents(contents);
    }
    
    public void initSearchInfo(AdminListingActionForm actionForm, String siteId) throws Exception {
    	ContentListingActionForm form = (ContentListingActionForm) actionForm;
    	if (form.getSrPublished() == null) {
    		form.setSrPublished("*");
    	}
       	Vector<String> userIdVector = Utility.getUserIdsForSite(siteId);
    	String srSelectUsers[] = new String[userIdVector.size()];
    	userIdVector.copyInto(srSelectUsers);
    	form.setSrSelectUsers(srSelectUsers);
        String jsonCategoryTree = Utility.makeJSONCategoryTree(siteId, form.getSiteProfileClassId(), form.isSiteProfileClassDefault()).toHtmlString();
        form.setJsonCategoryTree(jsonCategoryTree);
    }

    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Site site = getAdminBean(request).getSite();
        ContentListingActionForm form = (ContentListingActionForm) actionForm;

        try {
	        if (form.getContents() != null) {
	        	ContentDisplayForm contents[] = form.getContents();
		        for (int i = 0; i < contents.length; i++) {
		        	if (contents[i].getRemove() == null) {
		        		continue;
		        	}
		        	if (!contents[i].getRemove().equals("Y")) {
		        		continue;
		        	}
		            Content content = new Content();
		            content = ContentDAO.load(site.getSiteId(), Format.getLong(contents[i].getContentId()));
		            ContentDAO.remove(site.getSiteId(), content);
		            em.remove(content);
		        }
		        em.getTransaction().commit();
	        }
        }
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.contents.constraint"));
				saveMessages(request, errors);
		        ActionForward forward = actionMapping.findForward("removeError") ;
		        return forward;
			}
			throw e;
        }

        ActionForward forward = actionMapping.findForward("removed") ;
        forward = new ActionForward(forward.getPath() + "?process=list&srPageNo=" + form.getPageNo(), forward.getRedirect());
        return forward;
    }
    
    public ActionMessages validate(ContentListingActionForm form) throws Exception {
    	ActionMessages errors = new ActionMessages();
    	
    	if (!Format.isNullOrEmpty(form.getSrContentPublishOnStart()) && !Format.isDate(form.getSrContentPublishOnStart())) {
    		errors.add("srContentPublishOnStart", new ActionMessage("error.date.invalid"));
    	}
    	if (!Format.isNullOrEmpty(form.getSrContentPublishOnEnd()) && !Format.isDate(form.getSrContentPublishOnEnd())) {
    		errors.add("srContentPublishOnEnd", new ActionMessage("error.date.invalid"));
    	}
    	if (!Format.isNullOrEmpty(form.getSrContentExpireOnStart()) && !Format.isDate(form.getSrContentExpireOnStart())) {
    		errors.add("srContentExpireOnStart", new ActionMessage("error.date.invalid"));
    	}
    	if (!Format.isNullOrEmpty(form.getSrContentExpireOnEnd()) && !Format.isDate(form.getSrContentExpireOnEnd())) {
    		errors.add("srContentExpireOnEnd", new ActionMessage("error.date.invalid"));
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
    	((ContentListingActionForm) form).setContents(null);
	}
}