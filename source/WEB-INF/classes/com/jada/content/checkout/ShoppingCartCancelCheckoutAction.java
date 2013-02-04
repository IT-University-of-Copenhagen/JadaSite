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

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.jada.content.ContentBean;
import com.jada.order.payment.PaymentEngine;
import com.jada.order.cart.ShoppingCart;

public class ShoppingCartCancelCheckoutAction extends ShoppingCartBaseAction  {	
    Logger logger = Logger.getLogger(ShoppingCartCancelCheckoutAction.class);

    public ActionForward cancel(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
    	shoppingCart.cancelTransaction();
    	PaymentEngine paymentEngine = shoppingCart.getPaymentEngine();
    	if (paymentEngine != null) {
    		paymentEngine.abort();
    	}
		createEmptySecureTemplateInfo(request);
    	ContentBean contentBean = getContentBean(request);
    	String siteProfileClassName = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getSiteProfileClassName();
    	ActionForward forward = new ActionForward("/web/fe/" + contentBean.getSiteDomain().getSiteDomainPrefix() + "/" + siteProfileClassName + "/home", true);
    	return forward;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("cancel", "cancel");
        return map;
    }
}
