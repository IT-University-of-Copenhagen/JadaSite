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

package com.jada.admin.syndication;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.content.syndication.SyndReader;
import com.jada.dao.SyndicationDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.Syndication;
import com.jada.util.Constants;
import com.jada.util.Format;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;


import javax.persistence.Query;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class SyndicationMaintAction
    extends AdminLookupDispatchAction {
	
	static int SYNDICATION_COUNT = 5;

    public ActionForward start(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

        SyndicationMaintActionForm form = (SyndicationMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
    
        initListInfo(form, site.getSiteId());

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    

    private void initListInfo(SyndicationMaintActionForm form, String siteId) throws Exception {
        Query query = null;
        
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        String sql = "from Syndication syndication where siteId = :siteId order by seqNum";
        query = em.createQuery(sql);
        query.setParameter("siteId", siteId);
        Vector<SyndicationDisplayForm> vector = new Vector<SyndicationDisplayForm>();
        Iterator<?> iterator = query.getResultList().iterator();
        int count = 0;
        while (iterator.hasNext()) {
        	Syndication syndication = (Syndication) iterator.next();
        	SyndicationDisplayForm displayForm = new SyndicationDisplayForm();
        	displayForm.setIndex(Format.getInt(count));
        	displayForm.setSynId(Format.getLong(syndication.getSynId()));
        	displayForm.setSynUrl(syndication.getSynUrl());
        	displayForm.setActive(syndication.getActive().equals(Constants.VALUE_YES));
        	vector.add(displayForm);
        	count++;
        }
        for (int i = vector.size(); i < SYNDICATION_COUNT; i++) {
        	SyndicationDisplayForm displayForm = new SyndicationDisplayForm();
        	displayForm.setIndex(Format.getInt(i));
        	vector.add(displayForm);
        }
        
        SyndicationDisplayForm syns[] = new SyndicationDisplayForm[vector.size()];
        vector.copyInto(syns);
        form.setSyns(syns);
    }
    
    public ActionForward update(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	SyndicationMaintActionForm form = (SyndicationMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();

        SyndicationDisplayForm displayForms[] = form.getSyns();
        for (int i = 0; i < displayForms.length; i++) {
        	SyndicationDisplayForm displayForm = displayForms[i];
        	if (displayForm.getSynId().length() > 0) {
    			Syndication syndication = SyndicationDAO.load(site.getSiteId(), Format.getLong(displayForm.getSynId()));
        		if (displayForm.getSynUrl().trim().length() == 0) {
        			displayForm.setSynId("");
        			em.remove(syndication);
        		}
        		else {
        			syndication.setSeqNum(i);
        			syndication.setSynUrl(displayForm.getSynUrl().trim());
        			syndication.setActive(displayForm.isActive() ? Constants.ACTIVE_YES : Constants.ACTIVE_NO);
        			syndication.setRecUpdateBy(adminBean.getUser().getUserId());
        			syndication.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        			// em.update(syndication);
        		}
        	}
        	else {
        		if (displayForm.getSynUrl().trim().length() > 0) {
        			Syndication syndication = new Syndication();
        			syndication.setSite(site);
        			syndication.setSeqNum(i);
           			syndication.setSynUrl(displayForm.getSynUrl().trim());
        			syndication.setActive(displayForm.isActive() ? Constants.ACTIVE_YES : Constants.ACTIVE_NO);
        			syndication.setRecUpdateBy(adminBean.getUser().getUserId());
        			syndication.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        			syndication.setRecCreateBy(adminBean.getUser().getUserId());
        			syndication.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        			em.persist(syndication);
        		}
        	}
        }
        if (SyndReader.hasInstance(site.getSiteId())) {
        	SyndReader.getInstance(site.getSiteId()).reset();
        }
        initListInfo(form, site.getSiteId());

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        map.put("update", "update");
        return map;
    }
}