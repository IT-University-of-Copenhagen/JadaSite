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
import javax.persistence.Query;
import javax.persistence.EntityManager;
import com.jada.util.JSONEscapeObject;

import com.jada.admin.AdminAction;
import com.jada.admin.AdminBean;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.CustomAttribute;
import com.jada.util.Constants;

public class CustomAttributeLookupAction extends AdminAction {
	static String LOOKUP_TYPE_FOR_CUSTOMATTRIBUTEGROUP = "customAttributeGroup";
	static String LOOKUP_TYPE_FOR_CUSTOMATTRIBUTECOMPARE = "customAttributeCompare";
	
    public ActionForward performAction(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        AdminBean adminBean = getAdminBean(request);
        String type = request.getParameter("type");
    
        String sql = "select   customAttribute " +
        			 "from     CustomAttribute customAttribute " +
        			 "left     outer join customAttribute.customAttributeLanguage customAttributeLanguage " +
        			 "left     join customAttribute.site site " +
        			 "where    site.siteId = :siteId " +
        			 "and      customAttributeLanguage.siteProfileClass.siteProfileClassId = :siteProfileClassId " +
        			 "order    by customAttribute.customAttribName ";
        Query query = em.createQuery(sql);
        query.setParameter("siteId", adminBean.getSite().getSiteId());
        query.setParameter("siteProfileClassId", adminBean.getSite().getSiteProfileClassDefault().getSiteProfileClassId());
        Iterator<?> iterator = query.getResultList().iterator();
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	Vector<JSONEscapeObject> vector = new Vector<JSONEscapeObject>();
        while (iterator.hasNext()) {
        	CustomAttribute customAttribute = (CustomAttribute) iterator.next();
        	if (type != null && type.equals(LOOKUP_TYPE_FOR_CUSTOMATTRIBUTEGROUP)) {
/*
        		if (customAttribute.getSystemRecord() == Constants.VALUE_YES) {
        			continue;
        		}
 */
        	}
        	if (type != null && type.equals(LOOKUP_TYPE_FOR_CUSTOMATTRIBUTECOMPARE)) {
/*
        		if (customAttribute.getSystemRecord() == Constants.VALUE_YES) {
        			continue;
        		}
 */
        		if (customAttribute.getCustomAttribTypeCode() != Constants.CUSTOM_ATTRIBUTE_TYPE_USER_SELECT_DROPDOWN && 
        			customAttribute.getCustomAttribTypeCode() != Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP) {
        			continue;
        		}
        	}
        	JSONEscapeObject jsonItem = new JSONEscapeObject();
        	jsonItem.put("customAttribId", customAttribute.getCustomAttribId());
        	jsonItem.put("customAttribName", customAttribute.getCustomAttribName());
        	vector.add(jsonItem);
        }
        jsonResult.put("customAttributes", vector);
        String jsonString = jsonResult.toHtmlString();
        this.streamWebService(response, jsonString);
        return null;
    }
}
