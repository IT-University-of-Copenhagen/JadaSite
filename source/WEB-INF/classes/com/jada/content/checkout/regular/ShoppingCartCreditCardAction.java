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

package com.jada.content.checkout.regular;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.jada.content.ContentBean;
import com.jada.content.checkout.ShoppingCartActionForm;
import com.jada.content.checkout.ShoppingCartBaseAction;
import com.jada.jpa.entity.Site;
import com.jada.order.cart.ShoppingCart;
import com.jada.util.Constants;

public class ShoppingCartCreditCardAction extends ShoppingCartBaseAction {
    Logger logger = Logger.getLogger(ShoppingCartCreditCardAction.class);
    
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	ActionForward actionForward = actionMapping.findForward("success");
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
		
		ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
		ActionMessages messages = new ActionMessages();
		this.initCartInfo(form, site, shoppingCart, request, messages);
		this.initCreditCardInfo(form, site, shoppingCart, request, messages);
		this.initSearchInfo(form, site.getSiteId(), messages);
		
		if (form.getPaymentError() != null && form.getPaymentError().equalsIgnoreCase("true")) {
			form.setPaymentMessage(shoppingCart.getPaymentEngine().getPaymentMessage());
		}
		
		if (shoppingCart.isShippingQuoteLock()) {
			this.saveOpenOrder(shoppingCart, Constants.ORDER_STEP_QUOTE_CREDITCARD);
		}
		else {
			this.saveOpenOrder(shoppingCart, Constants.ORDER_STEP_CREDITCARD);
		}
		
    	createEmptySecureTemplateInfo(request);
    	this.saveToken(request);
        return actionForward;
    }
    
    public ActionForward update(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	createEmptySecureTemplateInfo(request);
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;

    	ActionMessages messages = this.validateCreditCard(form);
    	if (messages.size() > 0) {
			saveMessages(request, messages);
	    	initSearchInfo(form, site.getSiteId(), messages);
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
    	}
		ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
    	this.saveCreditCard(form, site, shoppingCart, contentBean);

    	initSearchInfo(form, site.getSiteId(), messages);

        ActionForward actionForward = actionMapping.findForward("updateSuccess");
        return actionForward;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        map.put("update", "update");
        return map;
    }
}
