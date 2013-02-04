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

package com.jada.admin.contactus;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminListingAction;
import com.jada.admin.AdminListingActionForm;
import com.jada.dao.ContactUsDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.ContactUs;
import com.jada.jpa.entity.Site;
import com.jada.util.Format;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;


import javax.persistence.Query;

import java.util.List;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class ContactUsListingAction
    extends AdminListingAction {
	
    public ActionForward resequence(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {

    	Site site = getAdminBean(request).getSite();
    	ContactUsListingActionForm form = (ContactUsListingActionForm) actionForm;

        ContactUsDisplayForm contactUsForms[] = form.getContactUsForms();
        for (int i = 0; i < contactUsForms.length; i++) {
        	int seqNum = Format.getInt(contactUsForms[i].getSeqNum());
    		ContactUs contactUs = ContactUsDAO.load(site.getSiteId(), Format.getLong(contactUsForms[i].getContactUsId()));
    		contactUs.setSeqNum(seqNum);
    		// em.update(contactUs);
        }
        return list(actionMapping, actionForm, request, response);
    }
 
    public void extract(AdminListingActionForm actionForm,
                        HttpServletRequest request)
        throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        ContactUsListingActionForm form = (ContactUsListingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        this.initSiteProfiles(form, site);

        Query query = null;
        
        if (form.getSrPageNo().length() == 0) {
        	form.setSrPageNo("1");
        }
        String sql = "select contactUs from ContactUs contactUs where siteId = :siteId ";
        if (form.getSrContactUsName().length() > 0) {
        	sql += "and contactUs.contactUsLanguage.contactUsName like :contactUsName ";
        }
        if (form.getSrActive() != null && !form.getSrActive().equals("*")) {
        	sql += "and active = :active ";
        }
        sql += "order by seq_num";

        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        if (form.getSrContactUsName().length() > 0) {
        	query.setParameter("contactUsName", "%" + form.getSrContactUsName() + "%");
        }
        if (!form.getSrActive().equals("*")) {
        	query.setParameter("active", form.getSrActive());
        }
        List<?> list = query.getResultList();
        int pageNo = Integer.parseInt(form.getSrPageNo());
        calcPage(adminBean, form, list, pageNo);
        Vector<ContactUsDisplayForm> vector = new Vector<ContactUsDisplayForm>();
        int startRecord = (form.getPageNo() - 1) * adminBean.getListingPageSize();
        int endRecord = startRecord + adminBean.getListingPageSize();
        for (int i = startRecord; i < list.size() && i < endRecord; i++) {
        	ContactUs contactUs = (ContactUs) list.get(i);
        	ContactUsDisplayForm contactUsDisplay = new ContactUsDisplayForm();
        	contactUsDisplay.setContactUsId(Format.getLong(contactUs.getContactUsId()));
        	contactUsDisplay.setContactUsName(contactUs.getContactUsLanguage().getContactUsName());
        	contactUsDisplay.setContactUsEmail(contactUs.getContactUsEmail());
        	contactUsDisplay.setContactUsPhone(contactUs.getContactUsPhone());
        	contactUsDisplay.setActive(String.valueOf(contactUs.getActive()));
        	contactUsDisplay.setSeqNum(Format.getInt(contactUs.getSeqNum()));
            vector.add(contactUsDisplay);
        }
        ContactUsDisplayForm contactUsDisplayForm[] = new ContactUsDisplayForm[vector.size()];
        vector.copyInto(contactUsDisplayForm);
        form.setContactUsForms(contactUsDisplayForm);
    }
    
    public void initSearchInfo(AdminListingActionForm actionForm, String siteId) throws Exception {
    	ContactUsListingActionForm form = (ContactUsListingActionForm) actionForm;
    	if (form.getSrActive() == null) {
    		form.setSrActive("*");
    	}
    }
    
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Site site = getAdminBean(request).getSite();
        ContactUsListingActionForm form = (ContactUsListingActionForm) actionForm;
        ContactUsDisplayForm contactUsForms[] = form.getContactUsForms();
        
        for (int i = 0; i < contactUsForms.length; i++) {
        	if (!contactUsForms[i].isRemove()) {
        		continue;
        	}
            ContactUs contactUs = new ContactUs();
            contactUs = ContactUsDAO.load(site.getSiteId(), Format.getLong(contactUsForms[i].getContactUsId()));
            em.remove(contactUs);
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
        map.put("resequence", "resequence");
        map.put("back", "back");
        map.put("search", "search");
        return map;
    }
   
	public void initForm(AdminListingActionForm actionForm) {
		ContactUsListingActionForm form = (ContactUsListingActionForm) actionForm;
		form.setContactUsForms(null);
	}
}