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

package com.jada.admin.productClass;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.ProductClassDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.ProductClass;
import com.jada.jpa.entity.Site;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;

import javax.persistence.Query;

import java.util.Iterator;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class ProductClassListingAction
    extends AdminLookupDispatchAction {
	
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        ProductClassListingActionForm form = (ProductClassListingActionForm) actionForm;
        form.setProductClasses(null);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward list(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        ProductClassListingActionForm form = (ProductClassListingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();

        Query query = null;
        String sql = "select productClass " +
        			 "from   ProductClass productClass " + 
        			 "left   join productClass.site site " +
        			 "where  site.siteId = :siteId ";
        if (form.getSrProductClassName().length() > 0) {
        	sql += "and productClass.productClassName like :productClassName ";
        }
        sql += "order by productClass.productClassName";
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        if (form.getSrProductClassName().length() > 0) {
        	query.setParameter("productClassName", "%" + form.getSrProductClassName() + "%");
        }
        Iterator<?> iterator = query.getResultList().iterator();
        Vector<ProductClassDisplayForm> vector = new Vector<ProductClassDisplayForm>();
        while (iterator.hasNext()) {
        	ProductClass productClass = (ProductClass) iterator.next();
    		ProductClassDisplayForm productClassDisplay = new ProductClassDisplayForm();
    		productClassDisplay.setProductClassId(Format.getLong(productClass.getProductClassId()));
    		productClassDisplay.setProductClassName(productClass.getProductClassName());
    		productClassDisplay.setSystemRecord(String.valueOf(productClass.getSystemRecord()));
    		vector.add(productClassDisplay);
        }
        form.setProductClasses(vector);
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward back(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        ProductClassListingActionForm form = (ProductClassListingActionForm) actionForm;
        if (form.getProductClasses() != null) {
        	return list(actionMapping, actionForm, request, response);
        }
        else {
        	return start(actionMapping, actionForm, request, response);
        }
    }
    
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        ProductClassListingActionForm form = (ProductClassListingActionForm) actionForm;
        String productClassIds[] = form.getProductClassIds();
        
        try {
	        if (productClassIds != null) {
		        for (int i = 0; i < productClassIds.length; i++) {
		            ProductClass productClass = new ProductClass();
		            productClass = ProductClassDAO.load(getAdminBean(request).getSite().getSiteId(), Format.getLong(productClassIds[i]));
		            if (productClass.getSystemRecord() == Constants.VALUE_YES) {
		            	continue;
		            }
		            em.remove(productClass);
		        }
		        em.getTransaction().commit();
	        }
        }
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.productClasses.constraint"));
				saveMessages(request, errors);
		        ActionForward forward = actionMapping.findForward("removeError") ;
		        return forward;
			}
			throw e;
        }

        ActionForward forward = new ActionForward();
        forward = new ActionForward(request.getServletPath() + "?process=list", true);
        return forward;
    }

    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("remove", "remove");
        map.put("list", "list");
        map.put("start", "start");
        map.put("back", "back");
        return map;
    }
}