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

package com.jada.admin.shipping;

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
import com.jada.dao.ShippingTypeDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.ShippingMethodRegionType;
import com.jada.jpa.entity.ShippingType;
import com.jada.jpa.entity.Site;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

public class ShippingTypeMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse)
        throws Throwable {

        ShippingTypeMaintActionForm form = (ShippingTypeMaintActionForm) actionForm;
        form.setSystemRecord("N");
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

    	Site site = getAdminBean(request).getSite();
        ShippingTypeMaintActionForm form = (ShippingTypeMaintActionForm) actionForm;
        if (form == null) {
            form = new ShippingTypeMaintActionForm();
        }
		String shippingTypeId = request.getParameter("shippingTypeId");
        ShippingType shippingType = new ShippingType();
        shippingType = ShippingTypeDAO.load(site.getSiteId(), Format.getLong(shippingTypeId));
        form.setMode("U");
        copyProperties(form, shippingType);
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
		ShippingTypeMaintActionForm form = (ShippingTypeMaintActionForm) actionForm;
		try {
			ShippingType shippingType = ShippingTypeDAO.load(site.getSiteId(), Format.getLong(form.getShippingTypeId()));
			Iterator<?> iterator = shippingType.getShippingMethodRegionTypes().iterator();
			while (iterator.hasNext()) {
				ShippingMethodRegionType shippingMethodRegionType = (ShippingMethodRegionType) iterator.next();
				em.remove(shippingMethodRegionType.getShippingRate());
				em.remove(shippingMethodRegionType);
			}
			em.remove(shippingType);
			em.getTransaction().commit();
		}
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.shippingType.constraint"));
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
		ShippingTypeMaintActionForm form = (ShippingTypeMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}

		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		ShippingType shippingType = new ShippingType();
		if (!insertMode) {
			shippingType = ShippingTypeDAO.load(site.getSiteId(), Format.getLong(form.getShippingTypeId()));
		}

		ActionMessages errors = validate(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return mapping.findForward("error");
		}

		if (insertMode) {
			shippingType.setRecCreateBy(adminBean.getUser().getUserId());
			shippingType.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			shippingType.setSystemRecord(Constants.VALUE_NO);
		}
		shippingType.setSite(site);
		shippingType.setShippingTypeName(form.getShippingTypeName());
		shippingType.setRecUpdateBy(adminBean.getUser().getUserId());
		shippingType.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		if (insertMode) {
			em.persist(shippingType);
		}
		else {
			// em.update(shippingType);
		}
        form.setMode("U");
        form.setShippingTypeId(shippingType.getShippingTypeId().toString());
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	private void copyProperties(ShippingTypeMaintActionForm form, ShippingType shippingType) {
		form.setShippingTypeId(Format.getLong(shippingType.getShippingTypeId()));
		form.setShippingTypeName(shippingType.getShippingTypeName());
		form.setSystemRecord(String.valueOf(shippingType.getSystemRecord()));
	}

    public ActionMessages validate(ShippingTypeMaintActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	
    	if (Format.isNullOrEmpty(form.getShippingTypeName())) {
    		errors.add("shippingTypeName", new ActionMessage("error.string.required"));
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
