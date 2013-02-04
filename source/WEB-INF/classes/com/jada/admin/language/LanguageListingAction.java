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

package com.jada.admin.language;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminListingAction;
import com.jada.admin.AdminListingActionForm;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Language;
import com.jada.jpa.entity.LanguageTranslation;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;

import javax.persistence.Query;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class LanguageListingAction
    extends AdminListingAction {

    public void extract(AdminListingActionForm actionForm, HttpServletRequest request) throws Throwable {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        LanguageListingActionForm form = (LanguageListingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        String siteId = adminBean.getSite().getSiteId();

        Query query = null;
        String sql = "from Language where 1 = 1 ";
        if (form.getSrLangName().length() > 0) {
        	sql += "and langName like :langName ";
        }
        sql += "order by langName";

        query = em.createQuery(sql);
        if (form.getSrLangName().length() > 0) {
        	query.setParameter("langName", "%" + form.getSrLangName() + "%");
        }
        List<?> list = query.getResultList();
        if (Format.isNullOrEmpty(form.getSrPageNo())) {
        	form.setSrPageNo("1");
        }
        int pageNo = Integer.parseInt(form.getSrPageNo());
        calcPage(adminBean, form, list, pageNo);
        Vector<LanguageDisplayForm> vector = new Vector<LanguageDisplayForm>();
        int startRecord = (form.getPageNo() - 1) * adminBean.getListingPageSize();
        int endRecord = startRecord + adminBean.getListingPageSize();
        for (int i = startRecord; i < list.size() && i < endRecord; i++) {
        	Language language = (Language) list.get(i);
        	LanguageDisplayForm languageDisplay = new LanguageDisplayForm();
        	languageDisplay.setLangId(Format.getLong(language.getLangId()));
        	languageDisplay.setLangName(language.getLangName());
        	languageDisplay.setSystemRecord(String.valueOf(language.getSystemRecord()));
            vector.add(languageDisplay);
        }
        form.setLanguages(vector);
        
        initSearchInfo(form, siteId);
    }
    
    public void initSearchInfo(AdminListingActionForm actionForm, String siteId) throws Exception {
    }

    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        LanguageListingActionForm form = (LanguageListingActionForm) actionForm;
        String langIds[] = form.getLangIds();
        try {
	        for (int i = 0; i < langIds.length; i++) { 
	            Language language = (Language) em.find(Language.class, Format.getLong(langIds[i]));
	            if (language.getSystemRecord() == Constants.VALUE_YES) {
	            	continue;
	            }
	            Iterator<?> iterator = language.getTranslations().iterator();
	            while (iterator.hasNext()) {
	            	LanguageTranslation translation = (LanguageTranslation) iterator.next();
	            	em.remove(translation);
	            }
	            em.remove(language);
		        em.getTransaction().commit();
	        }
        }
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.languages.constraint"));
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
		((LanguageListingActionForm) form).setLanguages(null);
	}
}