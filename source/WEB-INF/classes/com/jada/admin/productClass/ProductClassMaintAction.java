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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.admin.productClass.ProductClassMaintActionForm;
import com.jada.dao.ProductClassDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.ProductClass;
import com.jada.jpa.entity.Site;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

public class ProductClassMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse)
        throws Throwable {

        ProductClassMaintActionForm form = (ProductClassMaintActionForm) actionForm;
        form.setMode("C");
        form.setSystemRecord("N");
        
        FormUtils.setFormDisplayMode(httpServletRequest, form, FormUtils.CREATE_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

    	Site site = getAdminBean(request).getSite();
        ProductClassMaintActionForm form = (ProductClassMaintActionForm) actionForm;
        if (form == null) {
            form = new ProductClassMaintActionForm();
        }
		String productClassId = request.getParameter("productClassId");
        ProductClass productClass = new ProductClass();
        productClass = ProductClassDAO.load(site.getSiteId(), Format.getLong(productClassId));
        form.setMode("U");
        copyProperties(form, productClass);
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
		
    	Site site = getAdminBean(request).getSite();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ProductClassMaintActionForm form = (ProductClassMaintActionForm) actionForm;
		try {
			ProductClass productClass = ProductClassDAO.load(site.getSiteId(), Format.getLong(form.getProductClassId()));
			em.remove(productClass);
			em.getTransaction().commit();
		}
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.productClass.constraint"));
				saveMessages(request, errors);
				return actionMapping.findForward("error");
			}
			throw e;
		}
		
		ActionForward actionForward = actionMapping.findForward("removeSuccess");
		return actionForward;
	}

	public ActionForward save(ActionMapping mapping, 
							  ActionForm actionForm,
							  HttpServletRequest request,
							  HttpServletResponse response) 
		throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		boolean insertMode = false;
		ProductClassMaintActionForm form = (ProductClassMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}

		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		ProductClass productClass = new ProductClass();
		if (!insertMode) {
			productClass = ProductClassDAO.load(site.getSiteId(), Format.getLong(form.getProductClassId()));
		}

		ActionMessages errors = validate(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return mapping.findForward("error");
		}

		if (insertMode) {
			productClass.setRecCreateBy(adminBean.getUser().getUserId());
			productClass.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			productClass.setSystemRecord(Constants.VALUE_NO);
		}
		productClass.setSite(site);
		productClass.setProductClassName(form.getProductClassName());
		productClass.setRecUpdateBy(adminBean.getUser().getUserId());
		productClass.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		if (insertMode) {
			em.persist(productClass);
		}
		else {
			// em.update(productClass);
		}
        form.setMode("U");
        form.setProductClassId(productClass.getProductClassId().toString());
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	private void copyProperties(ProductClassMaintActionForm form, ProductClass productClass) {
		form.setProductClassId(Format.getLong(productClass.getProductClassId()));
		form.setProductClassName(productClass.getProductClassName());
		form.setSystemRecord(String.valueOf(productClass.getSystemRecord()));
	}

    public ActionMessages validate(ProductClassMaintActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	
    	if (Format.isNullOrEmpty(form.getProductClassName())) {
    		errors.add("productClassName", new ActionMessage("error.string.required"));
    	}
    	return errors;
    }
	
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("save", "save");
        map.put("edit", "edit");
        map.put("create", "create");
        map.put("remove", "remove");
        return map;
    }
}
