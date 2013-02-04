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

package com.jada.content.checkout.cash;

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
import com.jada.order.payment.AuthorizationException;
import com.jada.order.payment.PaymentException;

public class ShoppingCartCashFinalizeAction extends ShoppingCartBaseAction  {
    Logger logger = Logger.getLogger(ShoppingCartCashFinalizeAction.class);

    public ActionForward finalize(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	ContentBean contentBean = getContentBean(request);
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
		ActionMessages messages = new ActionMessages();
    	createEmptySecureTemplateInfo(request);
		try {
			this.finalizeOrder(form, site, shoppingCart, request, messages);
    	}
		catch (AuthorizationException e) {
			logger.error(e);
			this.ignoreToken(request, true);
			return actionMapping.findForward("authorizationException");
		}
		catch (PaymentException e) {
			logger.error(e);
			return actionMapping.findForward("paymentError");
		}

    	request.setAttribute("shoppingCart.orderNum", shoppingCart.getOrderNum());
    	ShoppingCart.remove(request);
    	ActionForward forward = actionMapping.findForward("success");
        forward = new ActionForward(forward.getPath() + 
        							"&prefix=" + contentBean.getSiteDomain().getSiteDomainPrefix() + 
        							"&langName=" + contentBean.getContentSessionKey().getLangName() + 
        							"&shoppingCart.orderNum=" + shoppingCart.getOrderNum(), forward.getRedirect());
        return forward;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("finalize", "finalize");
        return map;
    }
}
