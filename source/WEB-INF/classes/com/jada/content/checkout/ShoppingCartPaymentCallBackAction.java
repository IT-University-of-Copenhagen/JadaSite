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
import com.jada.content.ContentLookupDispatchAction;
import com.jada.order.payment.PaymentCustomerException;
import com.jada.order.payment.PaymentEngine;
import com.jada.order.payment.PaymentException;
import com.jada.order.cart.ShoppingCart;

public class ShoppingCartPaymentCallBackAction extends ContentLookupDispatchAction  {	
    Logger logger = Logger.getLogger(ShoppingCartPaymentCallBackAction.class);

    public ActionForward list(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

    	ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
    	PaymentEngine paymentEngine = shoppingCart.getPaymentEngine();
    	try {
	    	ContentBean contentBean = getContentBean(request);
	    	if (paymentEngine != null) {
		    	paymentEngine.callBack(request, shoppingCart, contentBean);
		    	if (paymentEngine.isProvideCustomer()) {
		    	}
	    	}
	    	shoppingCart.recalculate(contentBean);
    	}
    	catch (PaymentException e) {
    		logger.error(e);
    		paymentEngine.abort();
    		shoppingCart.cancelTransaction();
    		createEmptySecureTemplateInfo(request);
    		return actionMapping.findForward("paymentError");
    	}
    	catch (PaymentCustomerException e) {
    		paymentEngine.abort();
    		shoppingCart.cancelTransaction();
    		createEmptySecureTemplateInfo(request);
    		return actionMapping.findForward("paymentCustomerError");
    	}
    	
		this.saveToken(request);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("list", "list");
        return map;
    }
}
