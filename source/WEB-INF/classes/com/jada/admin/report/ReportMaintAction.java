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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.ReportDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Report;
import com.jada.jpa.entity.Site;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

public class ReportMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse)
        throws Throwable {

        ReportMaintActionForm form = (ReportMaintActionForm) actionForm;
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
        ReportMaintActionForm form = (ReportMaintActionForm) actionForm;
        if (form == null) {
            form = new ReportMaintActionForm();
        }
		String reportId = request.getParameter("reportId");
        Report report = new Report();
        report = ReportDAO.load(site.getSiteId(), Format.getLong(reportId));
        form.setMode("U");
        copyProperties(form, report);
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
		ReportMaintActionForm form = (ReportMaintActionForm) actionForm;
		try {
			Report Report = ReportDAO.load(site.getSiteId(), Format.getLong(form.getReportId()));
			em.remove(Report);
			em.getTransaction().commit();
		}
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.Report.constraint"));
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
		ReportMaintActionForm form = (ReportMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}

		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		Report report = new Report();
		if (!insertMode) {
			report = ReportDAO.load(site.getSiteId(), Format.getLong(form.getReportId()));
		}

		ActionMessages errors = validate(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return mapping.findForward("error");
		}

		if (insertMode) {
			report.setRecCreateBy(adminBean.getUser().getUserId());
			report.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			report.setSystemRecord(Constants.VALUE_NO);
		}
		report.setSite(site);
		report.setReportName(form.getReportName());
		report.setReportDesc(form.getReportDesc());
		report.setReportText(form.getReportText());
		report.setRecUpdateBy(adminBean.getUser().getUserId());
		report.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		if (insertMode) {
			em.persist(report);
		}
        form.setMode("U");
        form.setReportId(report.getReportId().toString());
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	private void copyProperties(ReportMaintActionForm form, Report report) {
		form.setReportId(Format.getLong(report.getReportId()));
		form.setReportName(report.getReportName());
		form.setReportDesc(report.getReportDesc());
		form.setReportText(report.getReportText());
		form.setSystemRecord(String.valueOf(report.getSystemRecord()));
	}

    public ActionMessages validate(ReportMaintActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	
    	if (Format.isNullOrEmpty(form.getReportName())) {
    		errors.add("reportName", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getReportDesc())) {
    		errors.add("reportDesc", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getReportText())) {
    		errors.add("reportText", new ActionMessage("error.string.required"));
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
