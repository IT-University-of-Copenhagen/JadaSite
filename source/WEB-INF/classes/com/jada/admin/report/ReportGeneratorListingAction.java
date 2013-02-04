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

package com.jada.admin.report;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Report;
import com.jada.jpa.entity.Site;
import com.jada.util.Format;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;


import javax.persistence.Query;

import java.util.Iterator;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class ReportGeneratorListingAction
    extends AdminLookupDispatchAction {
	
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        ReportGeneratorListingActionForm form = (ReportGeneratorListingActionForm) actionForm;
        form.setReports(null);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward list(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	ReportGeneratorListingActionForm form = (ReportGeneratorListingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        initSiteProfiles(form, site);

        Query query = null;
        String sql = "select report " +
        			 "from   Report report " + 
        			 "left   join report.site site " +
        			 "where  site.siteId = :siteId ";
        if (!Format.isNullOrEmpty(form.getSrReportName())) {
        	sql += "and report.reportName like :reportName ";
        }
        sql += "order by report.reportName";
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        if (form.getSrReportName() != null && form.getSrReportName().length() > 0) {
        	query.setParameter("reportName", "%" + form.getSrReportName() + "%");
        }
        Iterator<?> iterator = query.getResultList().iterator();
        Vector<ReportDisplayForm> vector = new Vector<ReportDisplayForm>();
        while (iterator.hasNext()) {
        	Report report = (Report) iterator.next();
    		ReportDisplayForm reportDisplay = new ReportDisplayForm();
    		reportDisplay.setReportId(Format.getLong(report.getReportId()));
    		reportDisplay.setReportName(report.getReportName());
    		reportDisplay.setReportDesc(report.getReportDesc());
    		if (report.getLastRunBy() != null) {
    			reportDisplay.setLastRunBy(report.getLastRunBy());
    		}
    		if (report.getLastRunDatetime() != null) {
    			reportDisplay.setLastRunDatetime(Format.getFullDatetime(report.getLastRunDatetime()));
    		}
    		reportDisplay.setSystemRecord(String.valueOf(report.getSystemRecord()));
    		vector.add(reportDisplay);
        }
        form.setReports(vector);
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward back(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	ReportGeneratorListingActionForm form = (ReportGeneratorListingActionForm) actionForm;
        if (form.getReports() != null) {
        	return list(actionMapping, actionForm, request, response);
        }
        else {
        	return start(actionMapping, actionForm, request, response);
        }
    }

    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("list", "list");
        map.put("start", "start");
        map.put("back", "back");
        return map;
    }
}