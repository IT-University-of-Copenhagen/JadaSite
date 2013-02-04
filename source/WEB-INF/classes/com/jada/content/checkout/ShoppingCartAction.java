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

import com.jada.api.DataApi;
import com.jada.content.ContentBean;
import com.jada.dao.CouponDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Coupon;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.CustomerAddress;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.ItemAttributeDetail;
import com.jada.jpa.entity.Language;
import com.jada.jpa.entity.ShippingMethod;
import com.jada.jpa.entity.Site;
import com.jada.order.cart.CouponNotApplicableException;
import com.jada.order.cart.CouponUserNotRegisterException;
import com.jada.order.cart.ItemAttributeInfo;
import com.jada.order.cart.ShoppingCart;
import com.jada.system.Languages;
import com.jada.util.Constants;
import com.jada.util.Format;

import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ShoppingCartAction extends ShoppingCartBaseAction {
    Logger logger = Logger.getLogger(ShoppingCartAction.class);

    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	init(request);
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
    	ContentBean contentBean = getContentBean(request);
    	Language language = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getLanguage();
    	Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
		ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
		maintainOpenOrder(request, shoppingCart);
    	ActionMessages messages = new ActionMessages();
    	CustomerAddress customerAddress = shoppingCart.getEstimateAddress();
    	if (customerAddress != null) {
    		form.setEstimateCountryCode(customerAddress.getCustCountryCode());
    		form.setEstimateStateCode(customerAddress.getCustStateCode());
    		form.setEstimateZipCode(customerAddress.getCustZipCode());
    	}
    	
		this.initSearchInfo(form, site.getSiteId(), messages);
		this.initCartInfo(form, site, shoppingCart, request, messages);
		if (customerAddress != null) {
        	if (!shoppingCart.isShippingValid()) {
        		form.addMessage("estimateShippingLocation", Languages.getLangTranValue(language.getLangId(), "content.error.shippingLocation.unsupported"));
        	}
		}
		saveMessages(request, messages);
        String sequenceInterrupt = request.getParameter("sequenceInterrupt");
        if (sequenceInterrupt != null && sequenceInterrupt.equals(String.valueOf(Constants.VALUE_YES))) {
        	form.setSequenceInterrupt(true);
        }
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward buy(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	init(request);
		ContentBean contentBean = getContentBean(request);
    	Language language = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getLanguage();
		Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
		ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
		ActionMessages messages = new ActionMessages();
		
    	try {
			String itemAttribDetailIds[] = form.getItemAttribDetailIds();
			Vector<ItemAttributeInfo> itemAttributeInfos = new Vector<ItemAttributeInfo>();
			if (itemAttribDetailIds != null) {
				String customAttribValues[] = form.getCustomAttribValues();
				String customAttribTypeCodes[] = form.getCustomAttribTypeCodes();
				for (int i = 0; i < itemAttribDetailIds.length; i++) {
					ItemAttributeInfo itemAttributeInfo = new ItemAttributeInfo();
					itemAttributeInfo.setCustomAttribTypeCode(customAttribTypeCodes[i]);
					itemAttributeInfo.setItemAttribDetailId(Format.getLong(itemAttribDetailIds[i]));
					if (customAttribTypeCodes[i].equals(String.valueOf(Constants.CUSTOM_ATTRIBUTE_TYPE_CUST_INPUT))) {
						itemAttributeInfo.setItemAttribDetailValue(customAttribValues[i]);
					}
					else {
						itemAttributeInfo.setCustomAttribOptionId(Format.getLong(customAttribValues[i]));
					}
					itemAttributeInfos.add(itemAttributeInfo);
				}
			}
			
        	String itemNauralKeys[] = form.getItemNaturalKeys();
        	if (itemNauralKeys != null) {
	        	for (int i = 0; i < itemNauralKeys.length; i++) {
	        		Item item = DataApi.getInstance().getItem(site.getSiteId(), itemNauralKeys[i]);
	        		try {
		    			if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_RECOMMAND_BUNDLE)) {
		    				for (Item child : item.getChildren()) {
		    					shoppingCart.setItemQty(child, 1, itemAttributeInfos, contentBean, false);
		    				}
		    			}
		    			else if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_TEMPLATE)) {
		    				item = getItem(form, site.getSiteId());
		    				shoppingCart.setItemQty(item, 1, itemAttributeInfos, contentBean, false);
		    			}
		    			else {
		    				shoppingCart.setItemQty(item, 1, itemAttributeInfos, contentBean, false);
		    			}
		        	} catch (ItemNotAvailiableException itemNotAvailiableException) {
		        		String value = Languages.getLangTranValue(language.getLangId(), "content.text.itemQuatityNotAvailable");
			    		ShoppingCartItemBean itemInfo = (ShoppingCartItemBean) form.getShoppingCartItemInfos().elementAt(i);
			    		itemInfo.setItemQtyError(value);
		        	}
	        	}
        	}
        	else {
	        	/*
	        	 * Only intended to be used with older version of templates.
	        	 */
	        	String itemIds[] = form.getItemIds();
	        	if (itemIds != null) {
		        	for (int i = 0; i < itemIds.length; i++) {
		        		Item item = DataApi.getInstance().getItem(site.getSiteId(), Format.getLong(itemIds[i]));
		    			if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_RECOMMAND_BUNDLE)) {
		    				for (Item child : item.getChildren()) {
		    					shoppingCart.setItemQty(child, 1, itemAttributeInfos, contentBean, false);
		    				}
		    			}
		    			else if (item.getItemTypeCd().equals(Constants.ITEM_TYPE_TEMPLATE)) {
		    				item = getItem(form, site.getSiteId());
		    				shoppingCart.setItemQty(item, 1, itemAttributeInfos, contentBean, false);
		    			}
		    			else {
		    				shoppingCart.setItemQty(item, 1, itemAttributeInfos, contentBean, false);
		    			}
		        	}
	        	}
        	}
    	} catch (ItemNotAvailiableException itemNotAvailiableException) {
    		form.addMessage("message", Languages.getLangTranValue(language.getLangId(), "content.text.itemQuatityNotAvailable"));
    	}
    	
		maintainOpenOrder(request, shoppingCart);
		
		this.initSearchInfo(form, site.getSiteId(), messages);
		this.initCartInfo(form, site, shoppingCart, request, messages);
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
		shoppingCart.removeItem(form.getItemNaturalKey(), getContentBean(request));
		
		maintainOpenOrder(request, shoppingCart);

		this.initSearchInfo(form, site.getSiteId(), messages);
		this.initCartInfo(form, site, shoppingCart, request, messages);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward cancelShippingQuote(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	init(request);
    	ContentBean contentBean = getContentBean(request);
		Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
    	ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true); 
    	shoppingCart.setShippingQuoteLock(false);
		this.uplockShippingQuote(shoppingCart, Constants.ORDER_STEP_REVIEWPURCHASE);
    	ActionMessages messages = new ActionMessages();
		this.initSearchInfo(form, site.getSiteId(), messages);
		this.initCartInfo(form, site, shoppingCart, request, messages);
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
		shoppingCart.removeCoupon(Format.getLong(form.getCouponId()), getContentBean(request));
		
		maintainOpenOrder(request, shoppingCart);
		
		this.initSearchInfo(form, site.getSiteId(), messages);
		this.initCartInfo(form, site, shoppingCart, request, messages);
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
    	this.initCartInfo(form, site, shoppingCart, request, messages);
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
						shoppingCart.setItemQty(item, qty, null, getContentBean(request), true);
					}
					catch (ItemNotAvailiableException e) {
			     		messages.add("message", new ActionMessage("content.text.itemQuatityNotAvailable"));
					}
		        }
	    	}
	        this.initCartInfo(form, site, shoppingCart, request, messages);
    	}
    	
		maintainOpenOrder(request, shoppingCart);
		
		this.initSearchInfo(form, site.getSiteId(), messages);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward applyCoupon(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	ContentBean contentBean = getContentBean(request);
    	Language language = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getLanguage();
		Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
    	ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true); 
    	String couponCode = form.getCouponCode().toUpperCase();
		createEmptySecureTemplateInfo(request);
    	ActionMessages messages = new ActionMessages();
    	this.initSearchInfo(form, site.getSiteId(), messages);
    	if (Format.isNullOrEmpty(couponCode)) {
    		this.initCartInfo(form, site, shoppingCart, request, messages);
    		form.addMessage("couponCode", Languages.getLangTranValue(language.getLangId(), "content.error.string.required"));
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
    	}
    	
        Coupon coupon = CouponDAO.loadByCouponCode(site.getSiteId(), couponCode);
        if (coupon == null) {
        	this.initCartInfo(form, site, shoppingCart, request, messages);
        	form.addMessage("couponCode", Languages.getLangTranValue(language.getLangId(), "content.error.coupon.invalid"));
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
        }
        
		try {
			shoppingCart.addCoupon(coupon, getContentBean(request));
		}
		catch (CouponNotApplicableException e1) {
			this.initCartInfo(form, site, shoppingCart, request, messages);
			form.addMessage("couponCode", Languages.getLangTranValue(language.getLangId(), "content.error.coupon.notApplicable"));
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
		}
		catch (CouponUserNotRegisterException e2) {
			this.initCartInfo(form, site, shoppingCart, request, messages);
			form.addMessage("couponCode", Languages.getLangTranValue(language.getLangId(), "content.error.coupon.notRegister"));
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
		}
		
		maintainOpenOrder(request, shoppingCart);

		form.setCouponCode("");
		this.initSearchInfo(form, site.getSiteId(), messages);
		this.initCartInfo(form, site, shoppingCart, request, messages);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward estimate(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	ContentBean contentBean = getContentBean(request);
    	Language language = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getLanguage();
		Site site = contentBean.getContentSessionBean().getSiteDomain().getSite();
    	ShoppingCartActionForm form = (ShoppingCartActionForm) actionForm;
    	ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, true);
    	shoppingCart.setEstimateAddress(request, form.getEstimateCountryCode(), form.getEstimateStateCode(), form.getEstimateZipCode(), form.isEstimatePickUp());
    	if (!Format.isNullOrEmpty(form.getShippingMethodId())) {
    		ShippingMethod shippingMethod = (ShippingMethod) em.find(ShippingMethod.class, Format.getLong(form.getShippingMethodId()));
    		shoppingCart.setShippingMethod(shippingMethod);
    	}
    	shoppingCart.recalculate(contentBean);
    	
    	ActionMessages messages = new ActionMessages();
    	this.initSearchInfo(form, site.getSiteId(), messages);
		this.initCartInfo(form, site, shoppingCart, request, messages);
    	if (!shoppingCart.isShippingValid() && !form.isEstimatePickUp()) {
    		form.addMessage("estimateShippingLocation", Languages.getLangTranValue(language.getLangId(), "content.error.shippingLocation.unsupported"));
    	}
        ActionForward actionForward = actionMapping.findForward("success");
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
    
    private Item getItem(ShoppingCartActionForm form, String siteId) throws SecurityException, Exception {
    	String itemNaturalKeys[] = form.getItemNaturalKeys();
    	if (itemNaturalKeys.length != 1) {
    		return null;
    	}
    	
    	Item item = DataApi.getInstance().getItem(siteId, itemNaturalKeys[0]);
    	String customAttribValues[] = form.getCustomAttribValues();
    	
    	for (Item child : item.getItemSkus()) {
    		boolean found = true;
    		for (ItemAttributeDetail itemAttributeDetail : child.getItemAttributeDetails()) {
    			if (itemAttributeDetail.getCustomAttributeDetail().getCustomAttribute().getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP) {
    				String optionId = itemAttributeDetail.getCustomAttributeOption().getCustomAttribOptionId().toString();
    				boolean exist = false;
    				for (String customAttribOptionId : customAttribValues) {
    					if (optionId.equals(customAttribOptionId)) {
    						exist = true;
    						break;
    					}
    				}
    				if (!exist) {
    					found = false;
    					break;
    				}
    			}
    		}
    		if (found) {
    			return child;
    		}
    	}
    	return null;
    }
    
    public void maintainOpenOrder(HttpServletRequest request, ShoppingCart shoppingCart) throws Exception {
		Customer customer = getCustomer(request);
		if (customer == null) {
			return;
		}
		
		if (shoppingCart.getShoppingCartItems().size() == 0) {
			this.removeOpenOrder(shoppingCart);
			return;
		}
		
		if (shoppingCart.isShippingQuoteLock()) {
			this.saveOpenOrder(shoppingCart, Constants.ORDER_STEP_QUOTE_CART);
		}
		else {
			this.saveOpenOrder(shoppingCart, Constants.ORDER_STEP_CART);
		}
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        map.put("buy", "buy");
        map.put("updateQty", "updateQty");
        map.put("applyCoupon", "applyCoupon");
        map.put("removeItem", "removeItem");
        map.put("removeCoupon", "removeCoupon");
        map.put("checkAvailability", "checkAvailability");
        map.put("cancelShippingQuote", "cancelShippingQuote");
        map.put("estimate", "estimate");
        return map;
    }
}