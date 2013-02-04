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

package com.jada.content.checkout;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.jada.content.ContentBean;
import com.jada.dao.SiteDomainDAO;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomainLanguage;
import com.jada.xml.site.SiteDomainParamBean;

public class ShoppingCartConfirmationAction extends ShoppingCartBaseAction {	
    public ActionForward confirm(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	init(request);
    	String orderNum = (String) request.getAttribute("shoppingCart.orderNum");
    	if (orderNum == null) {
    		orderNum = request.getParameter("shoppingCart.orderNum");
    	}
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;

		ContentBean contentBean = getContentBean(request);
		Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
		this.initFromOrder(orderNum, form, site, request, null);
		
    	SiteDomainLanguage siteDomainLanguage = null;
    	for (SiteDomainLanguage language : contentBean.getContentSessionBean().getSiteDomain().getSiteDomainLanguages()) {
    		if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
    			siteDomainLanguage = language;
    			break;
    		}
    	}
    	SiteDomainParamBean siteDomainParamBean = SiteDomainDAO.getSiteDomainParamBean(contentBean.getContentSessionBean().getSiteDomain().getSiteDomainLanguage(), siteDomainLanguage);
    	form.setShoppingCartMessage(siteDomainParamBean.getCheckoutShoppingCartMessage());
    	if (form.isPrint()) {
    		form.setPrintTemplate(true);
    	}
    	return actionMapping.findForward("success");
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("confirm", "confirm");
        return map;
    }
}
