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

package com.jada.admin.template;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.TemplateDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomain;
import com.jada.jpa.entity.Template;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;


import javax.persistence.Query;

import java.util.Iterator;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class TemplateListingAction
    extends AdminLookupDispatchAction {
	
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        TemplateListingActionForm form = (TemplateListingActionForm) actionForm;
        form.setTemplates(null);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward list(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	TemplateListingActionForm form = (TemplateListingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
/*
        SiteDomain siteDomain = (SiteDomain) site.getDomains().iterator().next();
        String domainAndPort = siteDomain.getSiteDomainName();
        if (!Format.isNullOrEmpty(siteDomain.getSitePublicPortNum())) {
        	domainAndPort += ":" + siteDomain.getSitePublicPortNum();
        }
        form.setDomainAndPort(domainAndPort);
*/
        
        if (form.getSrTemplateName() == null) {
        	return start(actionMapping, actionForm, request, response);
        }
        
        Query query = null;
        String sql = null;
        
        Vector<TemplateDisplayForm> vector = new Vector<TemplateDisplayForm>();
/*
        Template template = TemplateDAO.load(siteId, Constants.TEMPLATE_BASIC);
    	TemplateDisplayForm systemTemplate = new TemplateDisplayForm();
    	systemTemplate.setServletResource(true);
    	systemTemplate.setTemplateId(Format.getLong(template.getTemplateId()));
    	systemTemplate.setTemplateName(template.getTemplateName());
    	systemTemplate.setTemplateDesc(template.getTemplateDesc());
		vector.add(systemTemplate);
*/
        sql = "from    Template template " + 
              "where   siteId = :siteId ";
        if (form.getSrTemplateName().length() > 0) {
        	sql += "and templateName like :templateName ";
        }
        if (form.getSrTemplateDesc().length() > 0) {
        	sql += "and templateDesc like :templateDesc ";
        }
        sql += "order by templateName";
        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
//        query.setParameter("systemTemplateName", Constants.TEMPLATE_BASIC);
        
        if (form.getSrTemplateName().length() > 0) {
        	query.setParameter("templateName", "%" + form.getSrTemplateName() + "%");
        }
        if (form.getSrTemplateDesc().length() > 0) {
        	query.setParameter("templateDesc", "%" + form.getSrTemplateDesc() + "%");
        }
        Iterator<?> iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	Template template = (Template) iterator.next();
        	TemplateDisplayForm display = new TemplateDisplayForm();
        	if (template.getTemplateName().equals(Constants.TEMPLATE_BASIC)) {
        		display.setServletResource(true);
        	}
        	else {
        		display.setServletResource(false);
        	}
        	display.setTemplateId(Format.getLong(template.getTemplateId()));
        	display.setTemplateName(template.getTemplateName());
        	display.setTemplateDesc(template.getTemplateDesc());
        	String previewURL = "";
        	SiteDomain siteDomain = site.getSiteDomainDefault();
        	previewURL += "http://" + siteDomain.getSiteDomainName();
        	if (!Format.isNullOrEmpty(siteDomain.getSitePublicPortNum())) {
        		previewURL += ":" + siteDomain.getSitePublicPortNum();
        	}
        	previewURL += "/" + adminBean.getContextPath() + "/web/fe/";
        	previewURL += siteDomain.getSiteDomainPrefix() + "/";
        	previewURL += siteDomain.getSiteProfileDefault().getSiteProfileClass().getSiteProfileClassName() + "/";
        	previewURL += "home";
        	previewURL += "?templateName=" + template.getTemplateName();
        	display.setPreviewURL(previewURL);
    		vector.add(display);
        }
        form.setTemplates(vector);
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward back(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        TemplateListingActionForm form = (TemplateListingActionForm) actionForm;
        if (form.getTemplates() != null) {
        	return list(actionMapping, actionForm, request, response);
        }
        else {
        	return start(actionMapping, actionForm, request, response);
        }
    }
    
	public ActionForward remove(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {
	
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		TemplateListingActionForm form = (TemplateListingActionForm) actionForm;
		
		String templateNames[] = form.getTemplateNames();
		
		for (int i = 0; i < templateNames.length; i++) {
			Template template = TemplateDAO.load(site.getSiteId(), templateNames[i]);
	    	String prefix = Utility.getTemplatePrefix(site, templateNames[i]);
	    	if (!Utility.removeFile(prefix)) {
	            ActionMessages errors = new ActionMessages();
	       		errors.add("msg", new ActionMessage("error.template.remove", templateNames[i]));
            	saveMessages(request, errors);
    			return list(mapping, actionForm, request, response);
	    	}
			
			em.remove(template);
		}
        ActionForward forward = new ActionForward();
        forward = new ActionForward(request.getServletPath() + "?process=list", true);
        return forward;
	}

	public ActionForward preview(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
		throws Throwable {

        AdminBean adminBean = getAdminBean(request);
        String siteId = adminBean.getSite().getSiteId();
		String templateName = request.getParameter("templateName");
		ActionForward actionForward = mapping.findForward("preview");
		String path = actionForward.getPath() + "?preview=true&templateName=" + templateName + "&siteId=" + siteId;
		actionForward = new ActionForward(path);
		actionForward.setRedirect(true);
		return actionForward;
	}
	
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("remove", "remove");
        map.put("list", "list");
        map.put("start", "start");
        map.put("back", "back");
        map.put("preview", "preview");
        return map;
    }
}