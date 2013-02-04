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

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import com.jada.util.JSONEscapeObject;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.ShippingRegionDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.ShippingRegion;
import com.jada.jpa.entity.ShippingRegionZip;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.State;
import com.jada.util.Constants;
import com.jada.util.Format;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

public class ShippingRegionMaintAction
    extends AdminLookupDispatchAction {
    Logger logger = Logger.getLogger(ShippingRegionMaintAction.class);
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse)
        throws Throwable {

        ShippingRegionMaintActionForm form = (ShippingRegionMaintActionForm) actionForm;
        if (form == null) {
            form = new ShippingRegionMaintActionForm();
        }
        form.setPublished(true);
        form.setMode(Constants.MODE_CREATE);
        
        FormUtils.setFormDisplayMode(httpServletRequest, form, FormUtils.CREATE_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        
System.out.println(System.getProperty("user.dir"));
        return actionForward;
    }

    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

    	Site site = getAdminBean(request).getSite();
        ShippingRegionMaintActionForm form = (ShippingRegionMaintActionForm) actionForm;
        if (form == null) {
            form = new ShippingRegionMaintActionForm();
        }
		String shippingRegionId = request.getParameter("shippingRegionId");
        ShippingRegion shippingRegion = new ShippingRegion();
        shippingRegion = ShippingRegionDAO.load(site.getSiteId(), Format.getLong(shippingRegionId));
        form.setMode(Constants.MODE_UPDATE);
        copyProperties(form, shippingRegion);

        initListInfo(form, shippingRegion);
        initSearchInfo(form, request);

        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    private boolean isRootExist(Vector<?> vector, String value) {
    	Iterator<?> iterator = vector.iterator();
    	while (iterator.hasNext()) {
    		CountryStateTableBean bean = (CountryStateTableBean) iterator.next();
    		if (bean.getType().equals("R")) {
    			if (bean.getValue().equals(value)) {
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ShippingRegionMaintActionForm form = (ShippingRegionMaintActionForm) actionForm;
		ShippingRegion shippingRegion = ShippingRegionDAO.load(getAdminBean(request).getSite().getSiteId(), Format.getLong(form.getShippingRegionId()));
		em.remove(shippingRegion);
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
		ShippingRegionMaintActionForm form = (ShippingRegionMaintActionForm) actionForm;
		if (form.getMode().equals(Constants.MODE_CREATE)) {
			insertMode = true;
		}

		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();

		ShippingRegion shippingRegion = new ShippingRegion();
		if (!insertMode) {
			shippingRegion = ShippingRegionDAO.load(site.getSiteId(), Format.getLong(form.getShippingRegionId()));
		}

		ActionMessages errors = validate(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return mapping.findForward("error");
		}

		if (insertMode) {
			shippingRegion.setSystemRecord(Constants.VALUE_NO);
			shippingRegion.setRecCreateBy(adminBean.getUser().getUserId());
			shippingRegion.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		shippingRegion.setSite(site);
		shippingRegion.setShippingRegionName(form.getShippingRegionName());
		if (form.isPublished()) {
			shippingRegion.setPublished(Constants.PUBLISHED_YES);
		}
		else {
			shippingRegion.setPublished(Constants.PUBLISHED_NO);	
		}
		shippingRegion.setRecUpdateBy(adminBean.getUser().getUserId());
		shippingRegion.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		if (insertMode) {
			em.persist(shippingRegion);
		}
		else {
			// em.update(shippingRegion);
		}
		em.flush();
		form.setShippingRegionId(Format.getLong(shippingRegion.getShippingRegionId()));
		form.setMode(Constants.MODE_UPDATE);
		
        initListInfo(form, shippingRegion);
        initSearchInfo(form, request);
        
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	private String getJSONCountriesAndStatesList(ShippingRegion shippingRegion) throws Exception  {
    	String result = null;
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
    	Iterator<Country> iterator = shippingRegion.getCountries().iterator();
    	Vector<JSONEscapeObject> countries = new Vector<JSONEscapeObject>();
    	while (iterator.hasNext()) {
    		Country country = (Country) iterator.next();
    		JSONEscapeObject object = new JSONEscapeObject();
    		object.put("countryId", country.getCountryId());
    		object.put("countryCode", country.getCountryCode());
    		object.put("countryName", country.getCountryName());
    		countries.add(object);
    	}
    	jsonResult.put("countries", countries);
    	Iterator<State> iterator1 = shippingRegion.getStates().iterator();
    	Vector<JSONEscapeObject> states = new Vector<JSONEscapeObject>();
    	while (iterator1.hasNext()) {
    		State state = (State) iterator1.next();
    		JSONEscapeObject object = new JSONEscapeObject();
    		object.put("stateId", state.getStateId());
    		object.put("stateCode", state.getStateCode());
    		object.put("stateName", state.getStateName());
    		states.add(object);
    	}
    	jsonResult.put("states", states);
    	result = jsonResult.toHtmlString();
    	return result;
	}
	
	private String getJSONZipCodeList(ShippingRegion shippingRegion) throws Exception {
    	String result = null;
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
    	Iterator<ShippingRegionZip> iterator = shippingRegion.getZipCodes().iterator();
    	Vector<JSONEscapeObject> zipCodes = new Vector<JSONEscapeObject>();
    	while (iterator.hasNext()) {
    		ShippingRegionZip shippingRegionZip = (ShippingRegionZip) iterator.next();
    		JSONEscapeObject object = new JSONEscapeObject();
    		object.put("shippingRegionZipId", shippingRegionZip.getShippingRegionZipId());
    		object.put("zipCodeExpression", String.valueOf(shippingRegionZip.getZipCodeExpression()));
    		object.put("zipCodeStart", shippingRegionZip.getZipCodeStart());
    		object.put("zipCodeEnd", shippingRegionZip.getZipCodeEnd());
    		zipCodes.add(object);
    	}
    	jsonResult.put("zipCodes", zipCodes);
    	result = jsonResult.toHtmlString();
    	return result;
	}
	
	public ActionForward getRegionList(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ShippingRegionMaintActionForm form = (ShippingRegionMaintActionForm) actionForm;
		Long shippingRegionId = Format.getLong(form.getShippingRegionId());
		ShippingRegion shippingRegion = (ShippingRegion) em.find(ShippingRegion.class, shippingRegionId);
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		String result = getJSONCountriesAndStatesList(shippingRegion);
		this.streamWebService(response, result);
		return null;
	}
	
	public ActionForward addRegion(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ShippingRegionMaintActionForm form = (ShippingRegionMaintActionForm) actionForm;
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
  	
		Long shippingRegionId = Format.getLong(form.getShippingRegionId());
		ShippingRegion shippingRegion = (ShippingRegion) em.find(ShippingRegion.class, shippingRegionId);

	  	String countryIds[] = form.getCountryIds();
	  	if (countryIds != null) {
	    	for (int i = 0; i < countryIds.length; i++) {
	    		Long countryId = Format.getLong(countryIds[i]);
	            Country country = (Country) em.find(Country.class, countryId);
	            Iterator<?> countries = shippingRegion.getCountries().iterator();
	            boolean found = false;
	            while (countries.hasNext()) {
	            	Country c = (Country) countries.next();
	            	if (c.getCountryId().equals(countryId)) {
	            		found = true;
	            		break;
	            	}
	            }
	            if (!found) {
	            	shippingRegion.getCountries().add(country);
	            }
	    	}
	  	}
  	
	  	String stateIds[] = form.getStateIds();
	  	if (stateIds != null) {
	    	for (int i = 0; i < stateIds.length; i++) {
	    		Long stateId = Format.getLong(stateIds[i]);
	            State state = (State) em.find(State.class, stateId);
	            Iterator<?> states = shippingRegion.getStates().iterator();
	            boolean found = false;
	            while (states.hasNext()) {
	            	State s = (State) states.next();
	            	if (s.getStateId().equals(stateId)) {
	            		found = true;
	            		break;
	            	}
	            }
	            if (!found) {
	            	shippingRegion.getStates().add(state);
	            }
	    	}
	  	}
		em.persist(shippingRegion);
		
		String result = getJSONCountriesAndStatesList(shippingRegion);
		this.streamWebService(response, result);
		return null;
	}
	
	public ActionForward removeRegion(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ShippingRegionMaintActionForm form = (ShippingRegionMaintActionForm) actionForm;
	   	JSONEscapeObject jsonResult = new JSONEscapeObject();
	  	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
	  	
	  	Long shippingRegionId = Format.getLong(form.getShippingRegionId());
	  	ShippingRegion shippingRegion = (ShippingRegion) em.find(ShippingRegion.class, shippingRegionId);
      
      boolean modified = false;
	  	String countryIds[] = form.getCountryIds();
	  	if (countryIds != null) {
	    	for (int i = 0; i < countryIds.length; i++) {
	    		Long countryId = Format.getLong(countryIds[i]);
	            Iterator<?> countries = shippingRegion.getCountries().iterator();
	            while (countries.hasNext()) {
	            	Country c = (Country) countries.next();
	            	if (c.getCountryId().equals(countryId)) {
	            		countries.remove();
	            		modified = true;
	            		break;
	            	}
	            }
	    	}
	  	}
  	
	  	String stateIds[] = form.getStateIds();
	  	if (stateIds != null) {
	    	for (int i = 0; i < stateIds.length; i++) {
	    		Long stateId = Format.getLong(stateIds[i]);
	            Iterator<?> states = shippingRegion.getStates().iterator();
	            while (states.hasNext()) {
	            	State s = (State) states.next();
	            	if (s.getStateId().equals(stateId)) {
	            		states.remove();
	            		modified = true;
	            		break;
	            	}
	            }
	    	}
	  	}

	  	if (modified) {
	  		em.persist(shippingRegion);
	  	}
		
		String result = getJSONCountriesAndStatesList(shippingRegion);
		this.streamWebService(response, result);
		return null;
	}
	
	public ActionForward getZipCodeList(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ShippingRegionMaintActionForm form = (ShippingRegionMaintActionForm) actionForm;
		Long shippingRegionId = Format.getLong(form.getShippingRegionId());
		ShippingRegion shippingRegion = (ShippingRegion) em.find(ShippingRegion.class, shippingRegionId);
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		String result = getJSONZipCodeList(shippingRegion);
		streamWebService(response, result);
		return null;
	}
	
	public ActionForward addZipCode(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ShippingRegionMaintActionForm form = (ShippingRegionMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		
		Long shippingRegionId = Format.getLong(form.getShippingRegionId());
		ShippingRegion shippingRegion = (ShippingRegion) em.find(ShippingRegion.class, shippingRegionId);
		  
		ShippingRegionZip shippingRegionZip = new ShippingRegionZip();
		shippingRegionZip.setZipCodeStart(form.getZipCodeStart());
		shippingRegionZip.setZipCodeEnd(form.getZipCodeEnd());
		if (form.getZipCodeExpression().equalsIgnoreCase(String.valueOf(Constants.VALUE_YES))) {
			shippingRegionZip.setZipCodeExpression(Constants.VALUE_YES);
		}
		else {
			shippingRegionZip.setZipCodeExpression(Constants.VALUE_NO);
		}
		
		shippingRegionZip.setRecUpdateBy(adminBean.getUser().getUserId());
		shippingRegionZip.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		shippingRegionZip.setRecCreateBy(adminBean.getUser().getUserId());
		shippingRegionZip.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		shippingRegionZip.setShippingRegion(shippingRegion);
		
		em.persist(shippingRegionZip);
		
		String result = getJSONZipCodeList(shippingRegion);
		this.streamWebService(response, result);
		return null;
	}
/*
	public ActionForward modifyZipCode(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ShippingRegionMaintActionForm form = (ShippingRegionMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		
		Long shippingRegionId = Format.getLong(form.getShippingRegionId());
		ShippingRegion shippingRegion = (ShippingRegion) em.find(ShippingRegion.class, shippingRegionId);
		Long shippingRegionZipId = Format.getLong(form.getShippingRegionZipId());
		ShippingRegionZip shippingRegionZip = (ShippingRegionZip) em.find(ShippingRegionZip.class, shippingRegionZipId);
		
		if (form.getZipCodeExpression().equalsIgnoreCase(String.valueOf(Constants.VALUE_YES))) {
			shippingRegionZip.setZipCodeExpression(Constants.VALUE_YES);
		}
		else {
			shippingRegionZip.setZipCodeExpression(Constants.VALUE_NO);
		}
		shippingRegionZip.setZipCodeStart(form.getZipCodeStart());
		shippingRegionZip.setZipCodeEnd(form.getZipCodeEnd());
		shippingRegionZip.setRecUpdateBy(adminBean.getUser().getUserId());
		shippingRegionZip.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		shippingRegionZip.setShippingRegion(shippingRegion);
		
		em.persist(shippingRegionZip);
		
		String result = getJSONZipCodeList(shippingRegion);
		streamWebService(response, result);
		return null;
	}
*/
	public ActionForward removeZipCode(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ShippingRegionMaintActionForm form = (ShippingRegionMaintActionForm) actionForm;
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		
		Long shippingRegionId = Format.getLong(form.getShippingRegionId());
		String shippingRegionZipIds[] = form.getShippingRegionZipIds();
		ShippingRegion shippingRegion = (ShippingRegion) em.find(ShippingRegion.class, shippingRegionId);
		if (shippingRegionZipIds != null) {
			for (int i = 0; i < shippingRegionZipIds.length; i++) {
	        	Long shippingRegionZipId = Format.getLong(shippingRegionZipIds[i]);
	            ShippingRegionZip shippingRegionZip = (ShippingRegionZip) em.find(ShippingRegionZip.class, shippingRegionZipId);
	            shippingRegion.getZipCodes().remove(shippingRegionZip);
	            em.remove(shippingRegionZip);
	        }
		}
		String result = getJSONZipCodeList(shippingRegion);
		streamWebService(response, result);
		return null;
	}
    
    public void initSearchInfo(ShippingRegionMaintActionForm form, HttpServletRequest request) throws Exception {
        AdminBean adminBean = getAdminBean(request);
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        Query query = em.createQuery("from Country country where siteId = :siteId order by countryName");
		query.setParameter("siteId", adminBean.getSite().getSiteId());
		Iterator<?> iterator = query.getResultList().iterator();
		Vector<CountryStateTableBean> vector = new Vector<CountryStateTableBean>();
		while (iterator.hasNext()) {
			Country country = (Country) iterator.next();
			String prefix = "";
			if (country.getCountryName().length() > 0) {
				prefix = country.getCountryName().substring(0,1).toUpperCase();
			}
			if (!isRootExist(vector, prefix)) {
				CountryStateTableBean bean = new CountryStateTableBean();
				bean.setType("R");
				bean.setLabel(prefix);
				bean.setValue(prefix);
				bean.setParentValue("root");
				vector.add(bean);
			}
			
			CountryStateTableBean bean = new CountryStateTableBean();
			bean.setType("C");
			bean.setLabel(country.getCountryName());
			bean.setValue(country.getCountryId().toString());
			bean.setParentValue(prefix);
			vector.add(bean);
			
			Iterator<?> stateIterator = country.getStates().iterator();
			while (stateIterator.hasNext()) {
				State state = (State) stateIterator.next();
				bean = new CountryStateTableBean();
				bean.setType("S");
				bean.setLabel(state.getStateName());
				bean.setValue(state.getStateId().toString());
				bean.setParentValue(country.getCountryId().toString());
				vector.add(bean);
			}
		}
		CountryStateTableBean countryStateTable[] = new CountryStateTableBean[vector.size()];
        vector.copyInto(countryStateTable);
        form.setCountryStateTable(countryStateTable);
    }
    
    public void initListInfo(ShippingRegionMaintActionForm form, ShippingRegion shippingRegion) throws Exception {
    	form.setRemoveCountries(null);
    	form.setRemoveStates(null);
    	
        Iterator<?> iterator = shippingRegion.getCountries().iterator();
        Vector<CountryStateTableBean> vector = new Vector<CountryStateTableBean>();
        while (iterator.hasNext()) {
        	Country country = (Country) iterator.next();
        	CountryStateTableBean bean = new CountryStateTableBean();
        	bean.setType("C");
        	bean.setLabel(country.getCountryName());
        	bean.setValue(country.getCountryId().toString());
         	vector.add(bean);

        }
 
        iterator = shippingRegion.getStates().iterator();
        while (iterator.hasNext()) {
        	State state = (State) iterator.next();
        	CountryStateTableBean bean = new CountryStateTableBean();
        	bean.setType("S");
        	bean.setLabel(state.getCountry().getCountryName() + " - " + state.getStateName());
        	bean.setValue(state.getStateId().toString());
        	vector.add(bean);
        }
        
        CountryStateTableBean regionCountryStateTable[] = new CountryStateTableBean[vector.size()];
        vector.copyInto(regionCountryStateTable);
        form.setRegionCountryStateTable(regionCountryStateTable);
    }
    
	private void copyProperties(ShippingRegionMaintActionForm form, ShippingRegion shippingRegion) {
		form.setShippingRegionId(Format.getLong(shippingRegion.getShippingRegionId()));
		form.setShippingRegionName(shippingRegion.getShippingRegionName());
		form.setPublished(shippingRegion.getPublished() == Constants.PUBLISHED_YES);
	}
	
    public ActionMessages validate(ShippingRegionMaintActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	
    	if (Format.isNullOrEmpty(form.getShippingRegionName())) {
    		errors.add("shippingRegionName", new ActionMessage("error.string.required"));
    	}
    	return errors;
    }
	
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("save", "save");
        map.put("edit", "edit");
        map.put("create", "create");
        map.put("remove", "remove");
        map.put("getRegionList", "getRegionList");
        map.put("addRegion", "addRegion");
        map.put("addZipCode", "addZipCode");
        map.put("getZipCodeList", "getZipCodeList");
        map.put("removeZipCode", "removeZipCode");
        map.put("removeRegion", "removeRegion");
        return map;
    }
}
