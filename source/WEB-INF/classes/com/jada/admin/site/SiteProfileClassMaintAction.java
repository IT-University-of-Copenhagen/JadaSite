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

package com.jada.admin.site;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.SiteProfileClassDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Language;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.jpa.entity.Template;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.CategorySearchUtil;
import com.jada.util.Utility;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

public class SiteProfileClassMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest request,
                             HttpServletResponse response)
        throws Throwable {

		SiteProfileClassMaintActionForm form = (SiteProfileClassMaintActionForm) actionForm;
		Site site = getAdminBean(request).getSite();
        form.setMode("C");
        form.setSystemRecord(String.valueOf(Constants.VALUE_NO));
        createAdditionalInfo(form, site.getSiteId());
        FormUtils.setFormDisplayMode(request, form, FormUtils.CREATE_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
 
    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

        SiteProfileClassMaintActionForm form = (SiteProfileClassMaintActionForm) actionForm;
		Site site = getAdminBean(request).getSite();
        SiteProfileClass siteProfileClass = SiteProfileClassDAO.load(form.getSiteProfileClassId());
        form.setMode("U");
        copyProperties(form, siteProfileClass);
        createAdditionalInfo(form, site.getSiteId());
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
		SiteProfileClassMaintActionForm form = (SiteProfileClassMaintActionForm) actionForm;
        SiteProfileClass siteProfileClass = SiteProfileClassDAO.load(form.getSiteProfileClassId());

		try {
			CategorySearchUtil.removeSiteProfileClass(getAdminBean(request).getUserId(), siteProfileClass);
			em.remove(siteProfileClass);
			em.getTransaction().commit();
		}
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.siteProfileClass.constraint"));
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
		Site site = getAdminBean(request).getSite();
		boolean insertMode = false;
		SiteProfileClassMaintActionForm form = (SiteProfileClassMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}
        createAdditionalInfo(form, site.getSiteId());

		AdminBean adminBean = getAdminBean(request);

		SiteProfileClass siteProfileClass = new SiteProfileClass();
		if (!insertMode) {
	        siteProfileClass = SiteProfileClassDAO.load(form.getSiteProfileClassId());
		}

		ActionMessages errors = validate(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return mapping.findForward("error");
		}

		if (insertMode) {
			siteProfileClass.setSite(site);
			siteProfileClass.setSystemRecord(Constants.VALUE_NO);
			siteProfileClass.setRecCreateBy(adminBean.getUser().getUserId());
			siteProfileClass.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		siteProfileClass.setSiteProfileClassName(form.getSiteProfileClassName());
		siteProfileClass.setSiteProfileClassNativeName(form.getSiteProfileClassNativeName());
		siteProfileClass.setRecUpdateBy(adminBean.getUser().getUserId());
		siteProfileClass.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		
		Language language = (Language) em.find(Language.class, Format.getLong(form.getLangId()));
		siteProfileClass.setLanguage(language);
		
		if (insertMode) {
			em.persist(siteProfileClass);
			em.flush();
			CategorySearchUtil.createSiteProfileClass(site.getSiteProfileClassDefault(), siteProfileClass, site.getSiteId(), adminBean.getUserId());
		}
		else {
			// em.update(siteProfileClass);
		}
		form.setMode("U");
		form.setSiteProfileClassId(siteProfileClass.getSiteProfileClassId());
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	private void copyProperties(SiteProfileClassMaintActionForm form, SiteProfileClass siteProfileClass) {
		form.setSiteProfileClassId(siteProfileClass.getSiteProfileClassId());
		form.setSiteProfileClassName(siteProfileClass.getSiteProfileClassName());
		form.setSiteProfileClassNativeName(siteProfileClass.getSiteProfileClassNativeName());
		form.setLangId(siteProfileClass.getLanguage().getLangId().toString());
		form.setSystemRecord(String.valueOf(siteProfileClass.getSystemRecord()));
	}
	
	private void createAdditionalInfo(SiteProfileClassMaintActionForm form, String siteId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String sql = null;
    	
    	sql = "from   Language " +
    		  "order  by langName";
    	Query query = em.createQuery(sql);
    	Iterator<?> iterator = query.getResultList().iterator();
    	Vector<LabelValueBean> vector = new Vector<LabelValueBean>();
    	vector.add(new LabelValueBean("", ""));
    	while (iterator.hasNext()) {
    		Language language = (Language) iterator.next();
    		LabelValueBean bean = new LabelValueBean(language.getLangName(), language.getLangId().toString());
    		vector.add(bean);
    	}
    	LabelValueBean languages[] = new LabelValueBean[vector.size()];
    	vector.copyInto(languages);
    	form.setLanguages(languages);
    	
    	sql = "from   Template template " +
    	      "where  template.site.siteId = :siteId " +
		      "order  by templateName";
		query = em.createQuery(sql);
		query.setParameter("siteId", siteId);
		iterator = query.getResultList().iterator();
		vector = new Vector<LabelValueBean>();
    	vector.add(new LabelValueBean("", ""));
		while (iterator.hasNext()) {
			Template template = (Template) iterator.next();
			LabelValueBean bean = new LabelValueBean(template.getTemplateName(), template.getTemplateId().toString());
			vector.add(bean);
		}
		LabelValueBean templates[] = new LabelValueBean[vector.size()];
		vector.copyInto(templates);
		form.setTemplates(templates);
	}

    public ActionMessages validate(SiteProfileClassMaintActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	
    	if (Format.isNullOrEmpty(form.getSiteProfileClassName())) {
    		errors.add("siteProfileClassName", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getSiteProfileClassNativeName())) {
    		errors.add("siteProfileClassNativeName", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getLangId())) {
    		errors.add("langId", new ActionMessage("error.string.required"));
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
