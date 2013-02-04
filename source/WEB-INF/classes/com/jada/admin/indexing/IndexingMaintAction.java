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

package com.jada.admin.indexing;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessages;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.jpa.entity.Site;
import com.jada.search.Indexer;
import com.jada.search.IndexerWorker;
import com.jada.util.Constants;
import com.jada.xml.info.IndexerInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.HashMap;

public class IndexingMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward start(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

        IndexingActionForm form = (IndexingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        initInfo(form, site, request);

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward index(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {

        IndexingActionForm form = (IndexingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        
        IndexerWorker worker = new IndexerWorker(site.getSiteId());
        worker.start();
        // Make sure it has a chance to start before returning to screen
        Thread.sleep(2000);
        
        initInfo(form, site, request);

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public void initInfo(IndexingActionForm form, Site site, HttpServletRequest request) throws Exception {
    	ActionMessages messages = new ActionMessages();
    	Indexer indexer = Indexer.getInstance(site.getSiteId());
    	IndexerInfo indexerInfo = indexer.getIndexerInfo();
    	form.setIndexerInfo(indexerInfo);
    	
    	if (indexerInfo != null && indexerInfo.getIndexerStatus().equals(Constants.INDEXER_PROCESSING)) {
    		form.setInProgress(true);
    	}
    	this.saveMessages(request, messages);
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        map.put("index", "index");
        return map;
    }
}