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
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.State;

public class RegionLookupAction extends AdminAction {
    public ActionForward performAction(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        Site site = getAdminBean(request).getSite();

    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	Vector<JSONEscapeObject> countries = new Vector<JSONEscapeObject>();
        Query query = em.createQuery("from Country country where siteId = :siteId order by countryName");
		query.setParameter("siteId", site.getSiteId());
		Iterator<?> iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			Country country = (Country) iterator.next();
			JSONEscapeObject jsonCountry = new JSONEscapeObject();
			jsonCountry.put("countryId", country.getCountryId());
			jsonCountry.put("countryCode", country.getCountryCode());
			jsonCountry.put("countryName", country.getCountryName());
			Iterator<?> iterator1 = country.getStates().iterator();
			Vector<JSONEscapeObject> states = new Vector<JSONEscapeObject>();
			while (iterator1.hasNext()) {
				State state = (State) iterator1.next();
				JSONEscapeObject jsonState = new JSONEscapeObject();
				jsonState.put("stateId", state.getStateId());
				jsonState.put("stateCode", state.getStateCode());
				jsonState.put("stateName", state.getStateName());
				states.add(jsonState);
			}
			jsonCountry.put("states", states);
			countries.add(jsonCountry);
		}
        jsonResult.put("countries", countries);
        String jsonString = jsonResult.toHtmlString();
        streamWebService(response, jsonString);
        return null;
    }
}