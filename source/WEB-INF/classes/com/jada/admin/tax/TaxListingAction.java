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

package com.jada.admin.tax;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.admin.tax.TaxListingActionForm;
import com.jada.dao.TaxDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.Tax;
import com.jada.jpa.entity.TaxLanguage;
import com.jada.util.Format;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;


import javax.persistence.Query;

import java.util.Iterator;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class TaxListingAction
    extends AdminLookupDispatchAction {
	
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        TaxListingActionForm form = (TaxListingActionForm) actionForm;
        form.setTaxes(null);
    	if (form.getSrPublished() == null) {
    		form.setSrPublished("*");
    	}
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward list(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        TaxListingActionForm form = (TaxListingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        initSiteProfiles(form, site);
        
        if (form.getSrTaxCode() == null) {
        	return start(actionMapping, actionForm, request, response);
        }

        Query query = null;
        String sql = "select  tax " + 
        			 "from    Tax tax " + 
        			 "where   siteId = :siteId ";
        if (form.getSrTaxCode().length() > 0) {
        	sql += "and tax.taxLanguage.taxCode like :taxCode ";
        }
        if (!form.getSrPublished().equals("*")) {
        	sql += "and published = :published ";
        }
        sql += "order by taxId";
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        if (form.getSrTaxCode().length() > 0) {
        	query.setParameter("taxCode", "%" + form.getSrTaxCode() + "%");
        }
        if (!form.getSrPublished().equals("*")) {
        	query.setParameter("published", form.getSrPublished());
        }
        Iterator<?> iterator = query.getResultList().iterator();
        Vector<TaxDisplayForm> vector = new Vector<TaxDisplayForm>();
        while (iterator.hasNext()) {
        	Tax tax = (Tax) iterator.next();
        	TaxLanguage taxLanguage = null;
        	for (TaxLanguage language : tax.getTaxLanguages()) {
        		if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassDefaultId())) {
        			taxLanguage = language;
        		}
        	}
    		TaxDisplayForm taxDisplay = new TaxDisplayForm();
    		taxDisplay.setTaxId(Format.getLong(tax.getTaxId()));
    		taxDisplay.setTaxCode(taxLanguage.getTaxCode());
    		taxDisplay.setTaxName(taxLanguage.getTaxName());
    		taxDisplay.setTaxRate(Format.getFloatObj4(tax.getTaxRate()));
    		taxDisplay.setPublished(String.valueOf(tax.getPublished()));
    		vector.add(taxDisplay);
        }
        form.setTaxes(vector);
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward back(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        TaxListingActionForm form = (TaxListingActionForm) actionForm;
        if (form.getTaxes() != null) {
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
        TaxListingActionForm form = (TaxListingActionForm) actionForm;
        String taxIds[] = form.getTaxIds();
        
        if (taxIds != null) {
	        for (int i = 0; i < taxIds.length; i++) {
	            Tax tax = new Tax();
	            tax = TaxDAO.load(getAdminBean(request).getSite().getSiteId(), Format.getLong(taxIds[i]));
	            em.remove(tax);
	        }
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