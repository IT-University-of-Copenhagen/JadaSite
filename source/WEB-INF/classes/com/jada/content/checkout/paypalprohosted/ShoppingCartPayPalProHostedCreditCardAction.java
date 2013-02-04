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

package com.jada.content.checkout.paypalprohosted;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.jada.content.ContentBean;
import com.jada.content.checkout.ShoppingCartBaseAction;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.PaymentGateway;
import com.jada.order.cart.ShoppingCart;
import com.jada.order.document.OrderEngine;
import com.jada.order.payment.gateway.PayPalWebsitePaymentProHostedEngine;

public class ShoppingCartPayPalProHostedCreditCardAction extends ShoppingCartBaseAction {
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
    	ContentBean contentBean = getContentBean(request);
    	PaymentGateway paymentGateway = contentBean.getContentSessionBean().getSiteCurrency().getPaymentGateway();
    	PayPalWebsitePaymentProHostedEngine payPalEngine = new PayPalWebsitePaymentProHostedEngine(contentBean.getContentSessionBean().getSiteDomain().getSite(), paymentGateway.getPaymentGatewayId());
    	payPalEngine.setSiteDomain(contentBean.getContentSessionBean().getSiteDomain());
    	shoppingCart.setPaymentEngine(payPalEngine);
    	
		OrderEngine orderEngine = new OrderEngine(shoppingCart, null);
		OrderHeader orderHeader = orderEngine.getOrderHeader();
    	payPalEngine.payPalAuthorizeAndCapturePayment(orderHeader, request);
    	
		String emailLink = payPalEngine.getEmailLink();
    	ActionForward actionForward = new ActionForward();
    	actionForward.setPath(emailLink);
    	actionForward.setRedirect(true);
        return actionForward;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        return map;
    }
}
