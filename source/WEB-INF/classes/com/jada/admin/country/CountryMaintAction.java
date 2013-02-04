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

package com.jada.admin.country;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import com.jada.util.JSONEscapeObject;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.CountryDAO;
import com.jada.dao.StateDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.State;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

public class CountryMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse)
        throws Throwable {

        CountryMaintActionForm form = (CountryMaintActionForm) actionForm;
        if (form == null) {
            form = new CountryMaintActionForm();
        }
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
        CountryMaintActionForm form = (CountryMaintActionForm) actionForm;
        if (form == null) {
            form = new CountryMaintActionForm();
        }
		String countryId = request.getParameter("countryId");
        Country country = new Country();
        country = CountryDAO.load(site.getSiteId(), Format.getLong(countryId));
        form.setMode("U");
		form.setCountryId(Format.getLong(country.getCountryId()));
		form.setCountryCode(country.getCountryCode());
		form.setCountryName(country.getCountryName());
		
		createAdditionalInfo(form, country);

        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Site site = getAdminBean(request).getSite();
		CountryMaintActionForm form = (CountryMaintActionForm) actionForm;
		Country country = CountryDAO.load(site.getSiteId(), Format.getLong(form.getCountryId()));
		try {
			em.remove(country);
			em.getTransaction().commit();
		}
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.country.constraint"));
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
		CountryMaintActionForm form = (CountryMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}
	
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
	
		Country country = new Country();
		if (!insertMode) {
			country = CountryDAO.load(site.getSiteId(), Format.getLong(form.getCountryId()));
		}
	
		ActionMessages errors = validate(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return mapping.findForward("error");
		}
	
		String sql = "from    Country country " +
		     		 "where   country.site.siteId = :siteId " +
		     		 "and     countryCode = :countryCode ";
		if (!Format.isNullOrEmpty(form.getCountryId())) {
			sql += "and    countryId != :countryId";
		}
		Query query = em.createQuery(sql);
		query.setParameter("siteId", site.getSiteId());
		query.setParameter("countryCode", form.getCountryCode());
		if (!Format.isNullOrEmpty(form.getCountryId())) {
			query.setParameter("countryId", Long.valueOf(form.getCountryId()));
		}
		if (query.getResultList().iterator().hasNext()) {
			errors.add("countryCode", new ActionMessage("error.countryCode.duplicate"));
			saveMessages(request, errors);
			return mapping.findForward("error");
		}
	
		if (insertMode) {
			country.setRecCreateBy(adminBean.getUser().getUserId());
			country.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		country.setSite(site);
		country.setCountryCode(form.getCountryCode());
		country.setCountryName(form.getCountryName());
		country.setRecUpdateBy(adminBean.getUser().getUserId());
		country.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		if (insertMode) {
			em.persist(country);
		}
		else {
		// em.update(country);
		}
		form.setCountryId(Format.getLong(country.getCountryId()));
		form.setMode("U");
	
		FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	public ActionForward removeState(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Site site = getAdminBean(request).getSite();
		CountryMaintActionForm form = (CountryMaintActionForm) actionForm;
		Country country = CountryDAO.load(site.getSiteId(), Format.getLong(form.getCountryId()));
		
		StateDisplayForm states[] = form.getStates();
		for (int i = 0; i < states.length; i++) {
			StateDisplayForm stateForm = states[i];
			if (stateForm.getRemove() == null) {
				continue;
			}
			if (stateForm.getRemove().equals(String.valueOf(Constants.VALUE_YES))) {
				State state = StateDAO.load(site.getSiteId(), Format.getLong(stateForm.getStateId()));
				country.getStates().remove(state);
				em.remove(state);
			}
		}
		
		createAdditionalInfo(form, country);

        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("processed");
	}
	
	public ActionForward getState(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
        String stateId = (String) request.getParameter("stateId");
          
        State state = StateDAO.load(getAdminBean(request).getSite().getSiteId(), Format.getLong(stateId));
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("stateId", state.getStateId());
    	jsonResult.put("stateCode", state.getStateCode());
    	jsonResult.put("stateName", state.getStateName());
        String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
        return null;
	}
	
	public void createAdditionalInfo(CountryMaintActionForm form, Country country) {
		Vector<StateDisplayForm> vector = new Vector<StateDisplayForm>();
		Iterator<?> iterator = country.getStates().iterator();
		while (iterator.hasNext()) {
			State state = (State) iterator.next();
			StateDisplayForm stateForm = new StateDisplayForm();
			stateForm.setStateId(Format.getLong(state.getStateId()));
			stateForm.setStateCode(state.getStateCode());
			stateForm.setStateName(state.getStateName());
			vector.add(stateForm);
		}
		StateDisplayForm states[] = new StateDisplayForm[vector.size()];
		vector.copyInto(states);
		form.setStates(states);
	}
	
	public ActionForward updateState(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
		
	   	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();

		String stateId = (String) request.getParameter("stateId");
		String countryId = (String) request.getParameter("countryId");
        String stateCode = (String) request.getParameter("stateCode");
        String stateName = (String) request.getParameter("stateName");
    	MessageResources resources = this.getResources(request);
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	
    	Vector<JSONEscapeObject> vector = new Vector<JSONEscapeObject>();

    	boolean error = false;
    	if (Format.isNullOrEmpty(stateCode)) {
    		JSONEscapeObject object = new JSONEscapeObject();
       		object.put("message", resources.getMessage("error.stateCode.required"));	
       		vector.add(object);
       		error = true;
    	}
    	if (Format.isNullOrEmpty(stateName)) {
    		JSONEscapeObject object = new JSONEscapeObject();
       		object.put("message", resources.getMessage("error.stateName.required"));	
       		vector.add(object);
       		error = true;
    	}
		if (!Format.isNullOrEmpty(stateId)) {
	        String sql = "from	State state " + 
	        			 "left	outer join state.country country " +
	        			 "where	country.site.siteId = :siteId " +
	        			 "and 	state.stateCode = :stateCode " +
	        			 "and	state.stateId != :stateId ";
	        Query query = em.createQuery(sql);
	        query.setParameter("siteId", site.getSiteId());
	        query.setParameter("stateCode", stateCode);
	        query.setParameter("stateId", Format.getLong(stateId));
	        Iterator<?> iterator = query.getResultList().iterator();
	        if (iterator.hasNext()) {
	        	error = true;
	    		JSONEscapeObject object = new JSONEscapeObject();
	       		object.put("message", resources.getMessage("error.stateCode.duplicate"));	
	       		vector.add(object);
	        }
		}
		jsonResult.put("messages", vector);
		if (!error) {
			boolean exist = false;
			State state = null;
			if (!Format.isNullOrEmpty(stateId)) {
				state = StateDAO.load( site.getSiteId(), Format.getLong(stateId));
				exist = true;
			}
			else {
				state = new State();
				state.setRecCreateBy(adminBean.getUser().getUserId());
				state.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			}
			state.setStateCode(stateCode);
			state.setStateName(stateName);
			
			Country country = CountryDAO.load( site.getSiteId(), Format.getLong(countryId));
			state.setCountry(country);
			state.setRecUpdateBy(adminBean.getUser().getUserId());
			state.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			if (!exist) {
				em.persist(state);
			}
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		}
		else {
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
		}
        String jsonString = jsonResult.toHtmlString();
		streamWebService(response, jsonString);
        return null;
	}
	
    public ActionMessages validate(CountryMaintActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	
    	if (Format.isNullOrEmpty(form.getCountryCode())) {
    		errors.add("countryCode", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCountryName())) {
    		errors.add("countryName", new ActionMessage("error.string.required"));
    	}
    	return errors;
    }
	
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("save", "save");
        map.put("edit", "edit");
        map.put("create", "create");
        map.put("remove", "remove");
        map.put("addCountryState", "addCountryState");
        map.put("removeCountryState", "removeCountryState");
        map.put("updateState", "updateState");
        map.put("getState", "getState");
        map.put("removeState", "removeState");
        return map;
    }
    
    
}
