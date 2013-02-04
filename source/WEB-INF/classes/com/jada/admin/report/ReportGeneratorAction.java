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
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.ReportDAO;
import com.jada.jpa.entity.Report;
import com.jada.jpa.entity.Site;
import com.jada.util.Constants;
import com.jada.util.Format;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class ReportGeneratorAction
    extends AdminLookupDispatchAction {
    public ActionForward input(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	ReportGeneratorActionForm form = (ReportGeneratorActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        
        Report report = ReportDAO.load(site.getSiteId(), Format.getLong(form.getReportId()));
        form.setReportName(report.getReportName());
        form.setReportDesc(report.getReportDesc());

        ReportEngine engine = new ReportEngine(site.getSiteId(), 
				   Long.valueOf(form.getReportId()),
				   this.getServlet().getServletConfig().getServletContext());
        Collection<?> collection = engine.getReportParameters();
        Iterator<?> iterator = collection.iterator();
        Vector<ReportParamDisplayForm> vector = new Vector<ReportParamDisplayForm>();
        while (iterator.hasNext()) {
        	IParameterDefnBase definition = (IParameterDefnBase) iterator.next();
        	ScalarParameterHandle handle = (ScalarParameterHandle) definition.getHandle();
        	if (definition.getName().equals("siteId")) {
        		continue;
        	}
        	ReportParamDisplayForm reportParamDisplayForm = new ReportParamDisplayForm();
        	reportParamDisplayForm.setName(definition.getName());
        	reportParamDisplayForm.setDisplayName(definition.getPromptText());
        	reportParamDisplayForm.setType(handle.getDataType());
        	reportParamDisplayForm.setRequired("Y");
        	
        	if (handle.getControlType().equals(DesignChoiceConstants.PARAM_CONTROL_LIST_BOX)) {
        		LabelValueBean values[] = engine.getReportParameterOptions(definition);
        		if (handle.distinct()) {
        			Vector<LabelValueBean> valueVector = new Vector<LabelValueBean>();
        			for (LabelValueBean value : values) {
	        			boolean found = false;
	        			Enumeration<LabelValueBean> enumeration = valueVector.elements();
	        			while (enumeration.hasMoreElements()) {
	        				LabelValueBean bean = (LabelValueBean) enumeration.nextElement();
	        				if (bean.getLabel().equals(value.getLabel())) {
	        					found = true;
	        				}
	        			}
	        			if (!found) {
	        				valueVector.add(value);
	        			}
        			}
        			values = new LabelValueBean[valueVector.size()];
        			valueVector.copyInto(values);
        		}
        		reportParamDisplayForm.setValues(values);
        	}
        	
        	vector.add(reportParamDisplayForm);
        }
        ReportParamDisplayForm reportParameters[] = new ReportParamDisplayForm[vector.size()];
        vector.copyInto(reportParameters);
        form.setReportParameters(reportParameters);

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    private void generateOptions(ReportGeneratorActionForm form, ReportEngine engine, String siteId) throws NumberFormatException, Exception {
		Collection<?> collection = engine.getReportParameters();
		Iterator<?> iterator = collection.iterator();
		while (iterator.hasNext()) {
			IParameterDefnBase definition = (IParameterDefnBase) iterator.next();
			ScalarParameterHandle handle = (ScalarParameterHandle) definition.getHandle();
			if (definition.getName().equals("siteId")) {
				continue;
			}
			if (handle.getControlType().equals(DesignChoiceConstants.PARAM_CONTROL_LIST_BOX)) {
				LabelValueBean values[] = engine.getReportParameterOptions(definition);
				ReportParamDisplayForm reportParamDisplayForm = null;
				for (ReportParamDisplayForm f : form.getReportParameters()) {
					if (f.getName().equals(definition.getName())) {
						reportParamDisplayForm = f;
						break;
					}
				}
				reportParamDisplayForm.setValues(values);
			}
		}
    }
    
    public ActionForward validateInput(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
    	ReportGeneratorActionForm form = (ReportGeneratorActionForm) actionForm;
        
        ReportEngine engine = new ReportEngine(site.getSiteId(), 
											   Long.valueOf(form.getReportId()),
											   getServlet().getServletContext());
        generateOptions(form, engine, site.getSiteId());
        
		ActionMessages errors = validate(form, request);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return actionMapping.findForward("error");
		}
		form.setGenerate(true);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward generate(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	ReportGeneratorActionForm form = (ReportGeneratorActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        ReportEngine engine = new ReportEngine(site.getSiteId(), 
											   Long.valueOf(form.getReportId()),
											   getServlet().getServletContext());
		generateOptions(form, engine, site.getSiteId());
        
		ActionMessages errors = validate(form, request);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return actionMapping.findForward("error");
		}
		
		engine.setOutputFormat(form.getReportOutputMode());
        
        Report report = ReportDAO.load(site.getSiteId(), Format.getLong(form.getReportId()));
        Collection<?> collection = engine.getReportParameters();
        Iterator<?> iterator = collection.iterator();
        while (iterator.hasNext()) {
        	IParameterDefnBase definition = (IParameterDefnBase) iterator.next();
        	if (definition.getName().equals("siteId")) {
        		engine.setReportParameter("siteId", adminBean.getSiteId(), String.valueOf(definition.getParameterType()));
        	}
        }
        
        ReportParamDisplayForm reportParameters[] = form.getReportParameters();
        for (ReportParamDisplayForm reportParameter : reportParameters) {
        	engine.setReportParameter(reportParameter.getName(), reportParameter.getValue(), reportParameter.getType());
        }
        
        engine.generate(response);
        report.setLastRunBy(adminBean.getUserId());
        report.setLastRunDatetime(new Date());
        return null;
    }
    
    public ActionMessages validate(ReportGeneratorActionForm form, HttpServletRequest request) throws Exception {
    	ActionMessages errors = new ActionMessages();
		MessageResources resources = this.getResources(request);
    	for (ReportParamDisplayForm parameter : form.getReportParameters()) {
    		if (Format.isNullOrEmpty(parameter.getValue())) {
    			parameter.setMessage(resources.getMessage("error.string.required"));
    			errors.add("dummy", new ActionMessage("error.string.required"));
    			continue;
    		}
    		if (parameter.getType().equals(Constants.REPORT_BIRT_PARAM_TYPE_DATE)) {
    			if (!Format.isDate(parameter.getValue())) {
    				parameter.setMessage(resources.getMessage("error.date.invalid"));
        			errors.add("dummy", new ActionMessage("error.string.required"));
        			continue;
    			}
    		}
    	}
    	return errors;
    }

    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("input", "input");
        map.put("generate", "generate");
        map.put("validateInput", "validateInput");
        return map;
    }
}