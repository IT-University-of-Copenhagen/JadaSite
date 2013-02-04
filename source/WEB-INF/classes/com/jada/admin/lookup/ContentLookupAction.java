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

package com.jada.admin.lookup;

import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import com.jada.util.JSONEscapeObject;

import com.jada.admin.AdminAction;
import com.jada.admin.AdminBean;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Content;
import com.jada.jpa.entity.ContentLanguage;
import com.jada.util.Constants;

public class ContentLookupAction extends AdminAction {
    public ActionForward performAction(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        AdminBean adminBean = getAdminBean(request);
        String contentTitle = (String) request.getParameter("contentTitle");
        Long siteProfileClassDefaultId = adminBean.getSite().getSiteProfileClassDefault().getSiteProfileClassId();
        
        String sql = "from Content content where content.site.siteId = :siteId ";
        if (contentTitle != null && contentTitle.length() > 0) {
        	sql += "and content.contentLanguage.contentTitle like :contentTitle ";
        }
        Query query = em.createQuery(sql);
        query.setParameter("siteId", adminBean.getSite().getSiteId());
        if (contentTitle != null && contentTitle.length() > 0) {
        	query.setParameter("contentTitle", "%" + contentTitle + "%");
        }
        Iterator<?> iterator = query.getResultList().iterator();
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("contentTitle", contentTitle);
    	int counter = 0;
    	Vector<JSONEscapeObject> vector = new Vector<JSONEscapeObject>();
        while (iterator.hasNext()) {
        	Content content = (Content) iterator.next();
			ContentLanguage contentLanguage = null;
			for (ContentLanguage language : content.getContentLanguages()) {
				if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassDefaultId)) {
					contentLanguage = language;
				}
			}
        	JSONEscapeObject jsonContent = new JSONEscapeObject();
        	jsonContent.put("contentId", content.getContentId());
        	jsonContent.put("contentTitle", contentLanguage.getContentTitle());
        	vector.add(jsonContent);
        	counter++;
        	if (counter == Constants.ADMIN_SEARCH_MAXCOUNT) {
            	MessageResources resources = this.getResources(request);
        		jsonResult.put("message", resources.getMessage("error.lookup.tooManyRecord"));
        		break;
        	}
        }
        jsonResult.put("contents", vector);
        String jsonString = jsonResult.toHtmlString();
        this.streamWebService(response, jsonString);
        return null;
    }
}
