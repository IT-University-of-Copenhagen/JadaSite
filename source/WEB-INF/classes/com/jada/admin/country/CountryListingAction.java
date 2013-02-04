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

import com.jada.admin.AdminBean;
import com.jada.admin.AdminListingAction;
import com.jada.admin.AdminListingActionForm;
import com.jada.dao.CountryDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.Site;
import com.jada.util.Format;
import com.jada.util.Utility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;


import javax.persistence.Query;

import java.util.List;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class CountryListingAction
    extends AdminListingAction {
	
    public void extract(AdminListingActionForm actionForm, HttpServletRequest request) throws Throwable {
    	CountryListingActionForm form = (CountryListingActionForm) actionForm;

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        AdminBean adminBean = getAdminBean(request);
        String siteId = adminBean.getSite().getSiteId();

        Query query = null;
        String sql = "select country from Country country where siteId = :siteId ";
        if (form.getSrCountryCode().length() > 0) {
        	sql += "and countryCode like :countryCode ";
        }
        if (form.getSrCountryName().length() > 0) {
        	sql += "and countryName like :countryName ";
        }

        query = em.createQuery(sql);
        query.setParameter("siteId", siteId);
        if (form.getSrCountryCode().length() > 0) {
        	query.setParameter("countryCode", "%" + form.getSrCountryCode() + "%");
        }
        if (form.getSrCountryName().length() > 0) {
        	query.setParameter("countryName", "%" + form.getSrCountryName() + "%");
        }
        List<?> list = query.getResultList();
        if (Format.isNullOrEmpty(form.getSrPageNo())) {
        	form.setSrPageNo("1");
        }
        int pageNo = Integer.parseInt(form.getSrPageNo());
        calcPage(adminBean, form, list, pageNo);
        Vector<CountryDisplayForm> vector = new Vector<CountryDisplayForm>();
        int startRecord = (form.getPageNo() - 1) * adminBean.getListingPageSize();
        int endRecord = startRecord + adminBean.getListingPageSize();
        for (int i = startRecord; i < list.size() && i < endRecord; i++) {
        	Country country = (Country) list.get(i);
        	CountryDisplayForm countryDisplay = new CountryDisplayForm();
        	countryDisplay.setCountryId(Format.getLong(country.getCountryId()));
        	countryDisplay.setCountryCode(country.getCountryCode());
        	countryDisplay.setCountryName(country.getCountryName());
            vector.add(countryDisplay);
        }
        CountryDisplayForm countries[] = new CountryDisplayForm[vector.size()];
        vector.copyInto(countries);
        form.setCountries(countries);
    }
    
    public void initSearchInfo(AdminListingActionForm form, String siteId) throws Exception {
    }

    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Site site = getAdminBean(request).getSite();
        CountryListingActionForm form = (CountryListingActionForm) actionForm;

        try {
	        if (form.getCountries() != null) {
	        	CountryDisplayForm countries[] = form.getCountries();
		        for (int i = 0; i < countries.length; i++) {
		        	if (countries[i].getRemove() == null) {
		        		continue;
		        	}
		        	if (!countries[i].getRemove().equals("Y")) {
		        		continue;
		        	}
		            Country country = new Country();
		            country = CountryDAO.load(site.getSiteId(), Format.getLong(countries[i].getCountryId()));
		            em.remove(country);
		        }
		        em.getTransaction().commit();
	        }
        }
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.countries.constraint"));
				saveMessages(request, errors);
		        ActionForward forward = actionMapping.findForward("removeError") ;
		        return forward;
			}
			throw e;
        }

        ActionForward forward = actionMapping.findForward("removed") ;
        forward = new ActionForward(forward.getPath() + "?process=list&srPageNo=" + form.getPageNo(), forward.getRedirect());
        return forward;
    }

    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("remove", "remove");
        map.put("list", "list");
        map.put("start", "start");
        map.put("back", "back");
        map.put("search", "search");
        return map;
    }

	public void initForm(AdminListingActionForm form) {
    	((CountryListingActionForm) form).setCountries(null);
	}
}