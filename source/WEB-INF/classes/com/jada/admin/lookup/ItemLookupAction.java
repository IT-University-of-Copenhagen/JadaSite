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
import com.jada.jpa.entity.Item;
import com.jada.util.Constants;

public class ItemLookupAction extends AdminAction {
    public ActionForward performAction(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        AdminBean adminBean = getAdminBean(request);
        String itemNum = (String) request.getParameter("itemNum");
        String itemUpcCd = (String) request.getParameter("itemUpcCd");
        String itemShortDesc = (String) request.getParameter("itemShortDesc");
  
        String sql = "select item from Item item where siteId = :siteId ";
        if (itemNum != null && itemNum.length() > 0) {
        	sql += "and itemNum like :itemNum ";
        }
        if (itemUpcCd != null && itemUpcCd.length() > 0) {
        	sql += "and itemUpcCd like :itemUpcCd ";
        }
        if (itemShortDesc != null && itemShortDesc.length() > 0) {
        	sql += "and item.itemLanguage.itemShortDesc like :itemShortDesc ";
        }
        Query query = em.createQuery(sql);
        query.setParameter("siteId", adminBean.getSite().getSiteId());
        if (itemNum != null && itemNum.length() > 0) {
        	query.setParameter("itemNum", itemNum);
        }
        if (itemUpcCd != null && itemUpcCd.length() > 0) {
        	query.setParameter("itemUpcCd", itemUpcCd);
        }
        if (itemShortDesc != null && itemShortDesc.length() > 0) {
        	query.setParameter("itemShortDesc", "%" + itemShortDesc + "%");
        }
        Iterator<?> iterator = query.getResultList().iterator();
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("itemNum", itemNum);
    	jsonResult.put("itemUpcCd", itemUpcCd);
    	jsonResult.put("itemShortDesc", itemShortDesc);
    	int counter = 0;
    	Vector<JSONEscapeObject> vector = new Vector<JSONEscapeObject>();
        while (iterator.hasNext()) {
        	Item item = (Item) iterator.next();
        	JSONEscapeObject jsonItem = new JSONEscapeObject();
        	jsonItem.put("itemId", item.getItemId());
        	jsonItem.put("itemNum", item.getItemNum());
        	jsonItem.put("itemUpcCd", item.getItemUpcCd());
        	jsonItem.put("itemShortDesc", item.getItemLanguage().getItemShortDesc());
        	vector.add(jsonItem);
        	counter++;
        	if (counter == Constants.ADMIN_SEARCH_MAXCOUNT) {
            	MessageResources resources = this.getResources(request);
        		jsonResult.put("message", resources.getMessage("error.lookup.tooManyRecord"));
        		break;
        	}
        }
        jsonResult.put("items", vector);
        String jsonString = jsonResult.toHtmlString();
        streamWebService(response, jsonString);
        return null;
    }
}
