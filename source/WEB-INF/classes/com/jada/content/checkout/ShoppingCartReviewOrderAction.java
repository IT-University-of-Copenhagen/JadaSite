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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.jada.api.DataApi;
import com.jada.content.ContentBean;
import com.jada.content.checkout.ShoppingCartActionForm;
import com.jada.content.checkout.ShoppingCartBaseAction;
import com.jada.content.checkout.ShoppingCartItemBean;
import com.jada.dao.CouponDAO;
import com.jada.dao.ShippingMethodDAO;
import com.jada.jpa.entity.Coupon;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.Language;
import com.jada.jpa.entity.ShippingMethod;
import com.jada.jpa.entity.Site;
import com.jada.order.cart.CouponNotApplicableException;
import com.jada.order.cart.CouponUserNotRegisterException;
import com.jada.order.cart.ShoppingCart;
import com.jada.system.Languages;
import com.jada.util.Constants;
import com.jada.util.Format;

public class ShoppingCartReviewOrderAction extends ShoppingCartBaseAction {
    Logger logger = Logger.getLogger(ShoppingCartReviewOrderAction.class);
    
    public ActionForward list(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	init(request);
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();

		ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
		ActionMessages messages = new ActionMessages();
		this.initAddressInfo(form, site, shoppingCart, request, messages);
		this.initSearchInfo(form, site.getSiteId(), messages);
		this.initCartInfo(form, site, shoppingCart, request, messages);
		
		if (shoppingCart.isShippingPickUp()) {
			form.setShippingMethodId(Constants.SHOPPING_CART_SHIPPING_PICKUP);
		}
		
		if (shoppingCart.isShippingQuoteLock()) {
			this.saveOpenOrder(shoppingCart, Constants.ORDER_STEP_QUOTE_REVIEWPURCHASE);
		}
		else {
			this.saveOpenOrder(shoppingCart, Constants.ORDER_STEP_REVIEWPURCHASE);
		}

    	createEmptySecureTemplateInfo(request);
        saveMessages(request, messages);
        createEmptySecureTemplateInfo(request);
		this.saveToken(request);

    	return actionMapping.findForward("success");
    }
    
    public ActionForward recalculate(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	init(request);
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	
    	ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
    	
    	String shippingMethodId = form.getShippingMethodId();
    	if (shippingMethodId.equals(Constants.SHOPPING_CART_SHIPPING_PICKUP)) {
    		shoppingCart.setShippingPickUp(true);
    		shoppingCart.setShippingMethod(null);
    	}
    	else {
    		shoppingCart.setShippingPickUp(false);
        	ShippingMethod shippingMethod = null;
        	if (shippingMethodId != null) {
        		shippingMethod = ShippingMethodDAO.load(site.getSiteId(), Format.getLong(shippingMethodId));
        	}
        	shoppingCart.setShippingMethod(shippingMethod);
    	}
    	shoppingCart.recalculate(contentBean);

    	ActionMessages messages = new ActionMessages();
		this.initAddressInfo(form, site, shoppingCart, request, messages);
    	this.initCartInfo(form, site, shoppingCart, request, messages);
        saveMessages(request, messages);
        
		if (shoppingCart.isShippingQuoteLock()) {
			this.saveOpenOrder(shoppingCart, Constants.ORDER_STEP_QUOTE_REVIEWPURCHASE);
		}
		else {
			this.saveOpenOrder(shoppingCart, Constants.ORDER_STEP_REVIEWPURCHASE);
		}
		
        createEmptySecureTemplateInfo(request);
		this.saveToken(request);

    	return actionMapping.findForward("success");
    }
    
    public ActionForward applyCoupon(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
    	String couponCode = form.getCouponCode();
		createEmptySecureTemplateInfo(request);
    	ActionMessages messages = new ActionMessages();
		ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
    	if (Format.isNullOrEmpty(couponCode)) {
    		this.initAddressInfo(form, site, shoppingCart, request, messages);
    		this.initCartInfo(form, site, shoppingCart, request, messages);
    		messages.add("couponCode", new ActionMessage("content.error.string.required"));
			saveMessages(request, messages);
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
    	}
    	
        Coupon coupon = CouponDAO.loadByCouponCode(site.getSiteId(), couponCode);
        if (coupon == null) {
    		this.initAddressInfo(form, site, shoppingCart, request, messages);
        	this.initCartInfo(form, site, shoppingCart, request, messages);
    		messages.add("couponCode", new ActionMessage("content.error.coupon.invalid"));
			saveMessages(request, messages);
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
        }
        
		try {
			shoppingCart.addCoupon(coupon, contentBean);
		}
		catch (CouponNotApplicableException e1) {
			this.initAddressInfo(form, site, shoppingCart, request, messages);
			this.initCartInfo(form, site, shoppingCart, request, messages);
    		messages.add("couponCode", new ActionMessage("content.error.coupon.notApplicable"));
			saveMessages(request, messages);
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
		}
		catch (CouponUserNotRegisterException e2) {
			this.initAddressInfo(form, site, shoppingCart, request, messages);
			this.initCartInfo(form, site, shoppingCart, request, messages);
    		messages.add("couponCode", new ActionMessage("content.error.coupon.notRegister"));
			saveMessages(request, messages);
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
		}

		form.setCouponCode("");
		this.initCartInfo(form, site, shoppingCart, request, messages);
		
		if (shoppingCart.isShippingQuoteLock()) {
			this.saveOpenOrder(shoppingCart, Constants.ORDER_STEP_QUOTE_REVIEWPURCHASE);
		}
		else {
			this.saveOpenOrder(shoppingCart, Constants.ORDER_STEP_REVIEWPURCHASE);
		}
		
		createEmptySecureTemplateInfo(request);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward removeItem(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	init(request);
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
    	ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true); 
    	ActionMessages messages = new ActionMessages();
		shoppingCart.removeItem(form.getItemNaturalKey(), contentBean);
		this.initAddressInfo(form, site, shoppingCart, request, messages);
		this.initCartInfo(form, site, shoppingCart, request, messages);
		
		if (shoppingCart.isShippingQuoteLock()) {
			this.saveOpenOrder(shoppingCart, Constants.ORDER_STEP_QUOTE_REVIEWPURCHASE);
		}
		else {
			this.saveOpenOrder(shoppingCart, Constants.ORDER_STEP_REVIEWPURCHASE);
		}
		
		createEmptySecureTemplateInfo(request);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward removeCoupon(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	init(request);
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
    	ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true); 
    	ActionMessages messages = new ActionMessages();
		this.initAddressInfo(form, site, shoppingCart, request, messages);
    	this.initCartInfo(form, site, shoppingCart, request, messages);
		shoppingCart.removeCoupon(Format.getLong(form.getCouponId()), contentBean);
		
		if (shoppingCart.isShippingQuoteLock()) {
			this.saveOpenOrder(shoppingCart, Constants.ORDER_STEP_QUOTE_REVIEWPURCHASE);
		}
		else {
			this.saveOpenOrder(shoppingCart, Constants.ORDER_STEP_REVIEWPURCHASE);
		}
		
		this.initCartInfo(form, site, shoppingCart, request, messages);
		createEmptySecureTemplateInfo(request);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward updateQty(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	init(request);
    	ContentBean contentBean = getContentBean(request);
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
    	ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true); 
    	ActionMessages messages = new ActionMessages();
		this.initAddressInfo(form, site, shoppingCart, request, messages);
    	this.initCartInfo(form, site, shoppingCart, request, messages);
    	Language language = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getLanguage();
    	boolean hasError = validateUpdateQty(form, getContentBean(request));
    	if (hasError) {
	        String itemNaturalKeys[] = form.getItemNaturalKeys();
	        String itemQtys[] = form.getItemQtys();
    		for (int i = 0; i < itemNaturalKeys.length; i++) {
    			ShoppingCartItemBean bean = (ShoppingCartItemBean) form.getShoppingCartItemInfos().elementAt(i);
    			bean.setItemQty(itemQtys[i]);
    		}
    	}
    	else {
	        String itemNaturalKeys[] = form.getItemNaturalKeys();
	        String itemQtys[] = form.getItemQtys();
	        if (itemNaturalKeys != null) {
		        for (int i = 0; i < itemNaturalKeys.length; i++) {
		        	int qty = 0;
		        	if (itemQtys[i].trim().length() != 0) {
		        		qty = Format.getInt(itemQtys[i]);
		        	}
					Item item = DataApi.getInstance().getItem(site.getSiteId(), itemNaturalKeys[i]);
					try {
						shoppingCart.setItemQty(item, qty, null, contentBean, true);
			    	} catch (ItemNotAvailiableException itemNotAvailiableException) {
			    		String value = Languages.getLangTranValue(language.getLangId(), "content.text.itemQuatityNotAvailable");
			    		ShoppingCartItemBean itemInfo = (ShoppingCartItemBean) form.getShoppingCartItemInfos().elementAt(i);
			    		itemInfo.setItemQtyError(value);
			    		hasError = true;
			    	}
		        }
	    	}
	        if (!hasError) {
	        	this.initCartInfo(form, site, shoppingCart, request, messages);
	        }
    	}

		if (shoppingCart.isShippingQuoteLock()) {
			this.saveOpenOrder(shoppingCart, Constants.ORDER_STEP_QUOTE_REVIEWPURCHASE);
		}
		else {
			this.saveOpenOrder(shoppingCart, Constants.ORDER_STEP_REVIEWPURCHASE);
		}
		
		createEmptySecureTemplateInfo(request);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward shippingQuote(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	init(request);
    	ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true); 
    	this.saveOpenOrder(shoppingCart, Constants.ORDER_STEP_SHIPPINGQUOTE);
		ShoppingCart.remove(request);
		createEmptySecureTemplateInfo(request);
        ActionForward actionForward = actionMapping.findForward("quoteConfirmation");
        return actionForward;
    }
    
    public boolean validateUpdateQty(ShoppingCartActionForm form, ContentBean contentBean) throws Exception {
    	boolean hasError = false;
    	String itemQtys[] = form.getItemQtys();
    	Language language = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getLanguage();
    	if (itemQtys != null) {
	    	for (int i = 0; i < itemQtys.length; i++) {
	    		ShoppingCartItemBean itemInfo = (ShoppingCartItemBean) form.getShoppingCartItemInfos().elementAt(i);
	    		if (itemQtys[i].trim().length() == 0) {
	    			continue;
	    		}
	    		if (!Format.isInt(itemQtys[i])) {
	    			hasError = true;
	    			String value = Languages.getLangTranValue(language.getLangId(), "content.error.int.invalid");
	    			itemInfo.setItemQtyError(value);
	    		}
	    		else {
		    		int intValue = Format.getInt(itemQtys[i]);
		    		if (intValue < 0) {
		    			hasError = true;
		    			String value = Languages.getLangTranValue(language.getLangId(), "content.error.int.invalid");
		    			itemInfo.setItemQtyError(value);
		    		}
	    		}
	    	}
    	}
    	return hasError;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("list", "list");
        map.put("applyCoupon", "applyCoupon");
        map.put("recalculate", "recalculate");
        map.put("removeCoupon", "removeCoupon");
        map.put("removeItem", "removeItem");
        map.put("updateQty", "updateQty");
        map.put("shippingQuote", "shippingQuote");
        return map;
    }
}
