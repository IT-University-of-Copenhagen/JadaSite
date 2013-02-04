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

package com.jada.admin.customerClass;

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
import com.jada.admin.customerClass.CustomerClassMaintActionForm;
import com.jada.dao.CustomerClassDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.CustomerClass;
import com.jada.jpa.entity.Site;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

public class CustomerClassMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse)
        throws Throwable {

        CustomerClassMaintActionForm form = (CustomerClassMaintActionForm) actionForm;
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
        CustomerClassMaintActionForm form = (CustomerClassMaintActionForm) actionForm;
        if (form == null) {
            form = new CustomerClassMaintActionForm();
        }
		String custClassId = request.getParameter("custClassId");
        CustomerClass customerClass = new CustomerClass();
        customerClass = CustomerClassDAO.load(site.getSiteId(), Format.getLong(custClassId));
        form.setMode("U");
        copyProperties(form, customerClass);
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
		CustomerClassMaintActionForm form = (CustomerClassMaintActionForm) actionForm;
		try {
			CustomerClass customerClass = CustomerClassDAO.load(site.getSiteId(), Format.getLong(form.getCustClassId()));
			em.remove(customerClass);
			em.getTransaction().commit();
		}
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.customerClass.constraint"));
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
		CustomerClassMaintActionForm form = (CustomerClassMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}

		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		CustomerClass customerClass = new CustomerClass();
		if (!insertMode) {
			customerClass = CustomerClassDAO.load(site.getSiteId(), Format.getLong(form.getCustClassId()));
		}

		ActionMessages errors = validate(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return mapping.findForward("error");
		}

		if (insertMode) {
			customerClass.setRecCreateBy(adminBean.getUser().getUserId());
			customerClass.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			customerClass.setSystemRecord(Constants.VALUE_NO);
		}
		customerClass.setSite(site);
		customerClass.setCustClassName(form.getCustClassName());
		customerClass.setRecUpdateBy(adminBean.getUser().getUserId());
		customerClass.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		if (insertMode) {
			em.persist(customerClass);
		}
		else {
			// em.update(customerClass);
		}
        form.setMode("U");
        form.setCustClassId(customerClass.getCustClassId().toString());
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	private void copyProperties(CustomerClassMaintActionForm form, CustomerClass customerClass) {
		form.setCustClassId(Format.getLong(customerClass.getCustClassId()));
		form.setCustClassName(customerClass.getCustClassName());
		form.setSystemRecord(String.valueOf(customerClass.getSystemRecord()));
	}

    public ActionMessages validate(CustomerClassMaintActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	
    	if (Format.isNullOrEmpty(form.getCustClassName())) {
    		errors.add("customerClassName", new ActionMessage("error.string.required"));
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
