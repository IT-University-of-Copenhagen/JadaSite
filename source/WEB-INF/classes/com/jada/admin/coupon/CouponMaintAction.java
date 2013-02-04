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

package com.jada.admin.coupon;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;
import com.jada.util.JSONEscapeObject;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.CouponDAO;
import com.jada.dao.ItemDAO;
import com.jada.dao.CategoryDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Category;
import com.jada.jpa.entity.Coupon;
import com.jada.jpa.entity.CouponCurrency;
import com.jada.jpa.entity.CouponLanguage;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteCurrencyClass;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.jpa.entity.User;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.translator.BingTranslate;
import com.jada.util.Utility;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

public class CouponMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest request,
                             HttpServletResponse response)
        throws Throwable {

		CouponMaintActionForm form = (CouponMaintActionForm) actionForm;
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
        form.setCouponId("0");
        form.setPublished(true);
        form.setMode("C");
        form.setCouponType(Constants.COUPONTYPE_DISCOUNT_PERCENT);
        
        FormUtils.setFormDisplayMode(request, form, FormUtils.CREATE_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward translate(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
	
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		CouponMaintActionForm form = (CouponMaintActionForm) actionForm;
		if (form == null) {
			form = new CouponMaintActionForm();
		}
		
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
		
		Coupon coupon = (Coupon) em.find(Coupon.class, Format.getLong(form.getCouponId()));
		copyProperties(form, coupon);
		
	    BingTranslate translator = new BingTranslate(form.getFromLocale(), form.getToLocale(), site);
	    form.setCouponNameLangFlag(true);
	    form.setCouponNameLang(translator.translate(coupon.getCouponLanguage().getCouponName()));
	
		FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}
    
    public ActionForward language(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
	
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		CouponMaintActionForm form = (CouponMaintActionForm) actionForm;
		if (form == null) {
			form = new CouponMaintActionForm();
		}
		
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
		
		Coupon coupon = (Coupon) em.find(Coupon.class, Format.getLong(form.getCouponId()));
		copyProperties(form, coupon);
		
		FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		ActionForward actionForward = actionMapping.findForward("success");
		return actionForward;
	}

    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

        CouponMaintActionForm form = (CouponMaintActionForm) actionForm;
        Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
        if (form == null) {
            form = new CouponMaintActionForm();
        }
		String couponId = request.getParameter("couponId");
        Coupon coupon = new Coupon();
        coupon = CouponDAO.load(getAdminBean(request).getSite().getSiteId(), Format.getLong(couponId));
        form.setMode("U");
        copyProperties(form, coupon);
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		CouponMaintActionForm form = (CouponMaintActionForm) actionForm;
        Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
	
		try {
			Coupon coupon = CouponDAO.load(getAdminBean(request).getSite().getSiteId(), Format.getLong(form.getCouponId()));
			em.remove(coupon);
			em.getTransaction().commit();
		}
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.coupon.constraint"));
				saveMessages(request, errors);
				return actionMapping.findForward("error");
			}
			throw e;
		}
		
		ActionForward actionForward = actionMapping.findForward("removeSuccess");
		return actionForward;
	}

	public ActionForward save(ActionMapping mapping, 
							  ActionForm actionForm,
							  HttpServletRequest request,
							  HttpServletResponse response) 
		throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		boolean insertMode = false;
		CouponMaintActionForm form = (CouponMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}

		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		String siteId = site.getSiteId();
 		initSiteProfiles(form, site);

		Coupon coupon = CouponDAO.load(siteId, Format.getLong(form.getCouponId()));

		form.setCouponCode(form.getCouponCode().toUpperCase());
		ActionMessages errors = validate(form, insertMode, site.getSiteId());
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return mapping.findForward("error");
		}

		if (insertMode) {
			coupon = new Coupon();
			coupon.setSite(site);
			coupon.setCouponTotalUsed(Integer.valueOf(0));
			coupon.setRecCreateBy(adminBean.getUser().getUserId());
			coupon.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		if (form.isSiteProfileClassDefault() && form.isSiteCurrencyClassDefault()) {
			saveDefault(coupon, form, adminBean);
		}
		else {
			if (!form.isSiteProfileClassDefault()) {
				saveLanguage(coupon, form, adminBean);
			}
			if (!form.isSiteCurrencyClassDefault()) {
				saveCurrency(coupon, form, adminBean);
			}
		}
		if (insertMode) {
			em.persist(coupon);
		}
		form.setCouponTotalUsed(Format.getInt(coupon.getCouponTotalUsed()));
		form.setMode("U");
		form.setCouponId(coupon.getCouponId().toString());
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	private void saveDefault(Coupon coupon, CouponMaintActionForm form, AdminBean adminBean) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		coupon.setCouponCode(form.getCouponCode());
		coupon.setCouponStartDate(Format.getDate(form.getCouponStartDate()));
		coupon.setCouponEndDate(Format.getDate(form.getCouponEndDate()));
		coupon.setCouponScope(form.getCouponScope().charAt(0));
		coupon.setCouponAutoApply(form.isCouponAutoApply() ? Constants.VALUE_YES : Constants.VALUE_NO);
		coupon.setCouponApplyAll(form.isCouponApplyAll() ? Constants.VALUE_YES : Constants.VALUE_NO);
		coupon.setCouponType(form.getCouponType().charAt(0));
		coupon.setCouponDiscountPercent(null);
		if (form.getCouponType().equals(Constants.COUPONTYPE_DISCOUNT_PERCENT)) {
			coupon.setCouponDiscountPercent(Format.getFloat(form.getCouponDiscountPercent()));
		}
		if (!Format.isNullOrEmpty(form.getCouponMaxCustUse())) {
			coupon.setCouponMaxCustUse(Format.getInt(form.getCouponMaxCustUse()));
		}
		if (!Format.isNullOrEmpty(form.getCouponMaxUse())) {
			coupon.setCouponMaxUse(Format.getInt(form.getCouponMaxUse()));
		}
		coupon.setCouponPriority(Integer.valueOf(form.getCouponPriority()));
		coupon.setPublished(form.isPublished() ? Constants.PUBLISHED_YES : Constants.PUBLISHED_NO);
		coupon.setRecUpdateBy(adminBean.getUser().getUserId());
		coupon.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		CouponLanguage couponLanguage = coupon.getCouponLanguage();
		if (couponLanguage == null) {
			couponLanguage = new CouponLanguage();
			couponLanguage.setCoupon(coupon);
			coupon.getCouponLanguages().add(couponLanguage);
    		SiteProfileClass siteProfileClass = (SiteProfileClass) em.find(SiteProfileClass.class, form.getSiteProfileClassDefaultId());
    		couponLanguage.setSiteProfileClass(siteProfileClass);
    		couponLanguage.setRecCreateBy(adminBean.getUser().getUserId());
    		couponLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		coupon.setCouponLanguage(couponLanguage);
		}
		
		CouponCurrency couponCurrency = coupon.getCouponCurrency();
		if (couponCurrency == null) {
			couponCurrency = new CouponCurrency();
			couponCurrency.setCoupon(coupon);
			coupon.getCouponCurrencies().add(couponCurrency);
    		SiteCurrencyClass siteCurrencyClass = (SiteCurrencyClass) em.find(SiteCurrencyClass.class, form.getSiteCurrencyClassDefaultId());
    		couponCurrency.setSiteCurrencyClass(siteCurrencyClass);
    		couponCurrency.setRecCreateBy(adminBean.getUser().getUserId());
    		couponCurrency.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		coupon.setCouponCurrency(couponCurrency);
		}
		
		couponLanguage.setCouponName(form.getCouponName());
		couponLanguage.setRecUpdateBy(adminBean.getUser().getUserId());
		couponLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(couponLanguage);
		
		couponCurrency.setCouponDiscountAmount(null);
		couponCurrency.setCouponOrderAmount(null);
		if (form.getCouponType().equals(Constants.COUPONTYPE_DISCOUNT_AMOUNT)) {
			couponCurrency.setCouponDiscountAmount(Format.getFloat(form.getCouponDiscountAmount()));
		}
		if (form.getCouponType().equals(Constants.COUPONTYPE_DISCOUNT_OVER_AMOUNT)) {
			couponCurrency.setCouponDiscountAmount(Format.getFloat(form.getCouponDiscountAmount()));
			couponCurrency.setCouponOrderAmount(Format.getFloatObj(form.getCouponOrderAmount()));
		}
		couponCurrency.setRecUpdateBy(adminBean.getUser().getUserId());
		couponCurrency.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(couponCurrency);
	}
	
	private void saveLanguage(Coupon coupon, CouponMaintActionForm form, AdminBean adminBean) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		CouponLanguage couponLanguage = null;
		User user = adminBean.getUser();
    	Long siteProfileClassId = form.getSiteProfileClassId();
		boolean found = false;
		Iterator<?> iterator = coupon.getCouponLanguages().iterator();
		while (iterator.hasNext()) {
			couponLanguage = (CouponLanguage) iterator.next();
			if (couponLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
				found = true;
				break;
			}
		}
		if (!found) {
			couponLanguage = new CouponLanguage();
			couponLanguage.setRecCreateBy(user.getUserId());
			couponLanguage.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		SiteProfileClass siteProfileClass = (SiteProfileClass) em.find(SiteProfileClass.class, siteProfileClassId);
    		couponLanguage.setSiteProfileClass(siteProfileClass);
    		couponLanguage.setCoupon(coupon);
    		coupon.getCouponLanguages().add(couponLanguage);
		}
		if (form.isCouponNameLangFlag()) {
			couponLanguage.setCouponName(form.getCouponNameLang());
		}
		else {
			couponLanguage.setCouponName(null);
		}
		couponLanguage.setRecUpdateBy(user.getUserId());
		couponLanguage.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(couponLanguage);
	}
	
	private void saveCurrency(Coupon coupon, CouponMaintActionForm form, AdminBean adminBean) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		CouponCurrency couponCurrency = null;
		User user = adminBean.getUser();
    	Long siteCurrencyClassId = form.getSiteCurrencyClassId();
		boolean found = false;
		Iterator<?> iterator = coupon.getCouponCurrencies().iterator();
		while (iterator.hasNext()) {
			couponCurrency = (CouponCurrency) iterator.next();
			if (couponCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteCurrencyClassId)) {
				found = true;
				break;
			}
		}
		if (!found) {
			couponCurrency = new CouponCurrency();
			couponCurrency.setRecCreateBy(user.getUserId());
			couponCurrency.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		SiteCurrencyClass siteCurrencyClass = (SiteCurrencyClass) em.find(SiteCurrencyClass.class, siteCurrencyClassId);
    		couponCurrency.setSiteCurrencyClass(siteCurrencyClass);
    		couponCurrency.setCoupon(coupon);
    		coupon.getCouponCurrencies().add(couponCurrency);
		}
		couponCurrency.setCouponDiscountAmount(null);
		couponCurrency.setCouponOrderAmount(null);
		if (form.isCouponDiscountAmountCurrFlag()) {
			couponCurrency.setCouponDiscountAmount(Format.getFloat(form.getCouponDiscountAmountCurr()));
		}
		if (form.isCouponOrderAmountCurrFlag()) {
			couponCurrency.setCouponOrderAmount(Format.getFloat(form.getCouponOrderAmountCurr()));
		}
		couponCurrency.setRecUpdateBy(user.getUserId());
		couponCurrency.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(couponCurrency);
	}
	
	private void copyProperties(CouponMaintActionForm form, Coupon coupon) {
		form.setCouponId(Format.getLong(coupon.getCouponId()));
		form.setCouponCode(coupon.getCouponCode());
		form.setCouponName(coupon.getCouponLanguage().getCouponName());
		form.setCouponStartDate(Format.getDate(coupon.getCouponStartDate()));
		form.setCouponEndDate(Format.getDate(coupon.getCouponEndDate()));
		if (coupon.getCouponAutoApply() == Constants.VALUE_YES) {
			form.setCouponAutoApply(true);
		}
		if (coupon.getCouponApplyAll() == Constants.VALUE_YES) {
			form.setCouponApplyAll(true);
		}
		form.setCouponType(String.valueOf(coupon.getCouponType()));
		if (form.getCouponType().equals(Constants.COUPONTYPE_DISCOUNT_AMOUNT)) {
			form.setCouponDiscountAmount(Format.getFloat(coupon.getCouponCurrency().getCouponDiscountAmount()));
		}
		if (form.getCouponType().equals(Constants.COUPONTYPE_DISCOUNT_PERCENT)) {
			form.setCouponDiscountPercent(Format.getFloat(coupon.getCouponDiscountPercent()));
		}
		if (form.getCouponType().equals(Constants.COUPONTYPE_DISCOUNT_OVER_AMOUNT)) {
			form.setCouponOrderAmount(Format.getFloat(coupon.getCouponCurrency().getCouponOrderAmount()));
			form.setCouponDiscountAmount(Format.getFloat(coupon.getCouponCurrency().getCouponDiscountAmount()));
		}
		form.setCouponTotalUsed(Format.getInt(coupon.getCouponTotalUsed()));
		if (coupon.getCouponMaxUse() != null) {
			if (coupon.getCouponMaxUse().intValue() > 0) {
				form.setCouponMaxUse(Format.getInt(coupon.getCouponMaxUse()));
			}
		}
		if (coupon.getCouponMaxCustUse() != null) {
			if (coupon.getCouponMaxCustUse().intValue() > 0) {
				form.setCouponMaxCustUse(Format.getInt(coupon.getCouponMaxCustUse()));
			}
		}
		form.setCouponScope(String.valueOf(coupon.getCouponScope()));
		form.setPublished(coupon.getPublished() == Constants.PUBLISHED_YES);
		form.setCouponPriority(coupon.getCouponPriority().toString());
		form.setRecUpdateBy(coupon.getRecUpdateBy());
		form.setRecUpdateDatetime(Format.getFullDate(coupon.getRecUpdateDatetime()));
		form.setRecCreateBy(coupon.getRecCreateBy());
		form.setRecCreateDatetime(Format.getFullDate(coupon.getRecCreateDatetime()));
		
		form.setCouponNameLangFlag(false);
		form.setCouponNameLang(coupon.getCouponLanguage().getCouponName());
		if (!form.isSiteProfileClassDefault()) {
	    	Iterator<?> iterator = coupon.getCouponLanguages().iterator();
	    	Long siteProfileClassId = form.getSiteProfileClassId();
	    	while (iterator.hasNext()) {
	    		CouponLanguage CouponLanguage = (CouponLanguage) iterator.next();
	    		if (CouponLanguage.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassId)) {
	    			if (CouponLanguage.getCouponName() != null) {
	    				form.setCouponNameLangFlag(true);
	    				form.setCouponNameLang(CouponLanguage.getCouponName());
	    			}
	    			break;
	    		}
	    	}
		}
		form.setCouponDiscountAmountCurrFlag(false);
		form.setCouponDiscountAmountCurr("");
		if (coupon.getCouponCurrency().getCouponDiscountAmount() != null) {
			form.setCouponDiscountAmountCurr(Format.getFloat(coupon.getCouponCurrency().getCouponDiscountAmount()));
		}
		form.setCouponOrderAmountCurrFlag(false);
		form.setCouponOrderAmountCurr("");
		if (coupon.getCouponCurrency().getCouponOrderAmount() != null) {
			form.setCouponOrderAmountCurr(Format.getFloat(coupon.getCouponCurrency().getCouponOrderAmount()));
		}
		if (!form.isSiteCurrencyClassDefault()) {
			Long siteCurrencyClassId = form.getSiteCurrencyClassId();
			for (CouponCurrency couponCurrency : coupon.getCouponCurrencies()) {
				if (couponCurrency.getSiteCurrencyClass().getSiteCurrencyClassId().equals(siteCurrencyClassId)) {
					if (couponCurrency.getCouponDiscountAmount() != null) {
						form.setCouponDiscountAmountCurrFlag(true);
						form.setCouponDiscountAmountCurr(Format.getFloat(couponCurrency.getCouponDiscountAmount()));
					}
					if (couponCurrency.getCouponOrderAmount() != null) {
						form.setCouponOrderAmountCurrFlag(true);
						form.setCouponOrderAmountCurr(Format.getFloat(couponCurrency.getCouponOrderAmount()));
					}
				}
			}
		}
	}

    public ActionMessages validate(CouponMaintActionForm form, boolean insertMode, String siteId) throws SecurityException, Exception { 
    	ActionMessages errors = new ActionMessages();
    	
    	Coupon coupon = CouponDAO.loadByCouponCode(siteId, form.getCouponCode());
    	if (coupon != null) {
    		if (!form.getCouponId().equals(coupon.getCouponId().toString())) {
    			errors.add("couponCode", new ActionMessage("error.record.duplicate"));
    		}
    	}
    	if (Format.isNullOrEmpty(form.getCouponCode())) {
    		errors.add("couponCode", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCouponName())) {
    		errors.add("couponName", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCouponStartDate())) {
    		errors.add("couponStartDate", new ActionMessage("error.string.required"));
    	}
    	else {
        	if (!Format.isDate(form.getCouponStartDate())) {
        		errors.add("couponStartDate", new ActionMessage("error.date.invalid"));
        	}
    	}
    	if (Format.isNullOrEmpty(form.getCouponEndDate())) {
    		errors.add("couponEndDate", new ActionMessage("error.string.required"));
    	}
    	else {
        	if (!Format.isDate(form.getCouponEndDate())) {
        		errors.add("couponEndDate", new ActionMessage("error.date.invalid"));
        	}
    	}
    	if (form.getCouponType().equals(Constants.COUPONTYPE_DISCOUNT_AMOUNT)) {
    		if (Format.isNullOrEmpty(form.getCouponDiscountAmount())) {
    			errors.add("couponDiscountAmount", new ActionMessage("error.string.required"));
    		}
    		else {
    			if (!Format.isFloat(form.getCouponDiscountAmount())) {
    				errors.add("couponDiscountAmount", new ActionMessage("error.float.invalid"));
    			}
    		}
        	if (!form.isSiteCurrencyClassDefault()) {
        		if (Format.isNullOrEmpty(form.getCouponDiscountAmountCurr())) {
        			errors.add("couponDiscountAmount", new ActionMessage("error.string.required"));
        		}
        		else {
        			if (!Format.isFloat(form.getCouponDiscountAmountCurr())) {
        				errors.add("couponDiscountAmount", new ActionMessage("error.float.invalid"));
        			}
        		}
        	}
    	}
    	if (form.getCouponType().equals(Constants.COUPONTYPE_DISCOUNT_PERCENT)) {
    		if (Format.isNullOrEmpty(form.getCouponDiscountPercent())) {
    			errors.add("couponDiscountPercent", new ActionMessage("error.string.required"));
    		}
    		else {
    			if (!Format.isFloat(form.getCouponDiscountPercent())) {
    				errors.add("couponDiscountPercent", new ActionMessage("error.float.invalid"));
    			}
    		}
    	}
    	if (form.getCouponType().equals(Constants.COUPONTYPE_DISCOUNT_OVER_AMOUNT)) {
    		if (Format.isNullOrEmpty(form.getCouponDiscountAmount())) {
    			errors.add("couponDiscountAmount", new ActionMessage("error.string.required"));
    		}
    		else {
    			if (!Format.isFloat(form.getCouponDiscountAmount())) {
    				errors.add("couponDiscountAmount", new ActionMessage("error.float.invalid"));
    			}
    		}
    		if (Format.isNullOrEmpty(form.getCouponOrderAmount())) {
    			errors.add("couponOrderAmount", new ActionMessage("error.string.required"));
    		}
    		else {
    			if (!Format.isFloat(form.getCouponOrderAmount())) {
    				errors.add("couponOrderAmount", new ActionMessage("error.float.invalid"));
    			}
    		}
        	if (!form.isSiteCurrencyClassDefault()) {
        		if (Format.isNullOrEmpty(form.getCouponDiscountAmountCurr())) {
        			errors.add("couponDiscountAmountCurr", new ActionMessage("error.string.required"));
        		}
        		else {
        			if (!Format.isFloat(form.getCouponDiscountAmountCurr())) {
        				errors.add("couponDiscountAmountCurr", new ActionMessage("error.float.invalid"));
        			}
        		}
        		if (Format.isNullOrEmpty(form.getCouponOrderAmountCurr())) {
        			errors.add("couponOrderAmountCurr", new ActionMessage("error.string.required"));
        		}
        		else {
        			if (!Format.isFloat(form.getCouponOrderAmountCurr())) {
        				errors.add("couponOrderAmountCurr", new ActionMessage("error.float.invalid"));
        			}
        		}
        	}
    	}
    	if (!Format.isNullOrEmpty(form.getCouponMaxUse())) {
    		if (!Format.isInt(form.getCouponMaxUse())) {
    			errors.add("couponMaxUse", new ActionMessage("error.int.invalid"));
    		}
    	}
    	if (!Format.isNullOrEmpty(form.getCouponMaxCustUse())) {
    		if (!Format.isInt(form.getCouponMaxCustUse())) {
    			errors.add("couponMaxCustUse", new ActionMessage("error.int.invalid"));
    		}
    	}
    	return errors;
    }
    
    private String getJSONItemList(Coupon coupon, CouponMaintActionForm form) throws Exception {
    	String result = null;
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
    	java.util.Iterator<Item> iterator = coupon.getItems().iterator();
    	Vector<JSONEscapeObject> items = new Vector<JSONEscapeObject>();
    	while (iterator.hasNext()) {
    		Item item = (Item) iterator.next();
    		JSONEscapeObject object = new JSONEscapeObject();
    		object.put("itemId", item.getItemId());
    		object.put("itemNum", item.getItemNum());
    		object.put("itemShortDesc", item.getItemLanguage().getItemShortDesc());
    		object.put("itemDesc", item.getItemLanguage().getItemDesc());
    		items.add(object);
    	}
    	jsonResult.put("items", items);
    	result = jsonResult.toHtmlString();
    	return result;
    }
	
    private String getJSONCategoryList(Coupon coupon) throws Exception {
    	String result = null;
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
    	java.util.Iterator<Category> iterator = coupon.getCategories().iterator();
    	Vector<JSONEscapeObject> categories = new Vector<JSONEscapeObject>();
    	while (iterator.hasNext()) {
    		Category category = (Category) iterator.next();
    		JSONEscapeObject object = new JSONEscapeObject();
    		object.put("catId", category.getCatId());
    		object.put("catShortTitle", category.getCategoryLanguage().getCatShortTitle());
    		categories.add(object);
    	}
    	jsonResult.put("categories", categories);
    	result = jsonResult.toHtmlString();
    	return result;
    }
    
	public ActionForward listItem(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
			throws Throwable {
		CouponMaintActionForm form = (CouponMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		String siteId = adminBean.getSite().getSiteId();
		
		Coupon coupon = CouponDAO.load(siteId, Format.getLong(form.getCouponId()));
		String result = getJSONItemList(coupon, form);
		streamWebService(response, result);
		return null;
	}
    
	public ActionForward listCategory(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
			throws Throwable {
		CouponMaintActionForm form = (CouponMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		String siteId = adminBean.getSite().getSiteId();
		
		Coupon coupon = CouponDAO.load(siteId, Format.getLong(form.getCouponId()));
		String result = getJSONCategoryList(coupon);
		streamWebService(response, result);
		return null;
	}
	
	public ActionForward removeItems(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
			throws Throwable {
		CouponMaintActionForm form = (CouponMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		String siteId = adminBean.getSite().getSiteId();
		
		Coupon coupon = CouponDAO.load(siteId, Format.getLong(form.getCouponId()));
		Vector<Item> removeList = new Vector<Item>();
		if (form.getItemIds() != null) {
			for (Item item : coupon.getItems()) {
				for (String itemId : form.getItemIds()) {
					if (item.getItemId().toString().equals(itemId)) {
						removeList.add(item);
					}
				}
			}
		}
		Iterator<?> iterator = removeList.iterator();
		while (iterator.hasNext()) {
			Item item = (Item) iterator.next();
			coupon.getItems().remove(item);
		}
		String result = getJSONItemList(coupon, form);
		streamWebService(response, result);
		return null;
	}
	
	public ActionForward removeCategories(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
			throws Throwable {
		CouponMaintActionForm form = (CouponMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		String siteId = adminBean.getSite().getSiteId();
		
		Coupon coupon = CouponDAO.load(siteId, Format.getLong(form.getCouponId()));
		Vector<Category> removeList = new Vector<Category>();
		if (form.getCatIds() != null) {
			for (Category category : coupon.getCategories()) {
				for (String catId : form.getCatIds()) {
					if (category.getCatId().toString().equals(catId)) {
						removeList.add(category);
					}
				}
			}
		}
		Iterator<?> iterator = removeList.iterator();
		while (iterator.hasNext()) {
			Category category = (Category) iterator.next();
			coupon.getCategories().remove(category);
		}
		String result = getJSONCategoryList(coupon);
		streamWebService(response, result);
		return null;
	}
	
	public ActionForward addItem(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
			throws Throwable {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		CouponMaintActionForm form = (CouponMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		String siteId = adminBean.getSite().getSiteId();
		
		MessageResources resources = this.getResources(request);
		Coupon coupon = CouponDAO.load(siteId, Format.getLong(form.getCouponId()));
		boolean found = false;
		java.util.Iterator<Item> iterator = coupon.getItems().iterator();
		while (iterator.hasNext()) {
			Item item = (Item) iterator.next();
			if (item.getItemId().equals(Format.getLong(form.getItemId()))) {
				found = true;
				break;
			}
		}
		if (found) {
			JSONEscapeObject jsonResult = new JSONEscapeObject();
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			jsonResult.put("message", resources.getMessage("error.item.exist"));
			streamWebService(response, jsonResult.toHtmlString());
		}
		Item item = ItemDAO.load(siteId, Format.getLong(form.getItemId()));
		if (item == null) {
			JSONEscapeObject jsonResult = new JSONEscapeObject();
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			jsonResult.put("message", resources.getMessage("error.item.notexist"));
			streamWebService(response, jsonResult.toHtmlString());
		}
		coupon.getItems().add(item);
		em.persist(coupon);
		String result = getJSONItemList(coupon, form);
		streamWebService(response, result);
		return null;
	}
	
	public ActionForward addCategory(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) 
			throws Throwable {
		CouponMaintActionForm form = (CouponMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		String siteId = adminBean.getSite().getSiteId();
		
		MessageResources resources = this.getResources(request);
		Coupon coupon = CouponDAO.load(siteId, Format.getLong(form.getCouponId()));
		boolean found = false;
		java.util.Iterator<Category> iterator = coupon.getCategories().iterator();
		while (iterator.hasNext()) {
			Category category = (Category) iterator.next();
			if (category.getCatId().equals(Format.getLong(form.getCatId()))) {
				found = true;
				break;
			}
		}
		if (found) {
			JSONEscapeObject jsonResult = new JSONEscapeObject();
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			jsonResult.put("message", resources.getMessage("error.record.duplicate"));
			streamWebService(response, jsonResult.toHtmlString());
		}
		Category category = CategoryDAO.load(siteId, Format.getLong(form.getCatId()));
		if (category == null) {
			JSONEscapeObject jsonResult = new JSONEscapeObject();
			jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
			jsonResult.put("message", resources.getMessage("error.category.notexist"));
			streamWebService(response, jsonResult.toHtmlString());
		}
		coupon.getCategories().add(category);
		String result = getJSONCategoryList(coupon);
		streamWebService(response, result);
		return null;
	}
	
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("save", "save");
        map.put("edit", "edit");
        map.put("create", "create");
        map.put("remove", "remove");
        map.put("listItem", "listItem");
        map.put("listCategory", "listCategory");
        map.put("addItem", "addItem");
        map.put("removeItems", "removeItems");
        map.put("addCategory", "addCategory");
        map.put("removeCategories", "removeCategories");
        map.put("language", "language");
        map.put("translate", "translate");
        return map;
    }
}
