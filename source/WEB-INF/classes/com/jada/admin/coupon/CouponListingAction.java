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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminListingAction;
import com.jada.admin.AdminListingActionForm;
import com.jada.dao.CouponDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Coupon;
import com.jada.jpa.entity.CouponLanguage;
import com.jada.util.Format;
import com.jada.util.Utility;

public class CouponListingAction extends AdminListingAction {

	public void extract(AdminListingActionForm actionForm, HttpServletRequest request) throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        CouponListingActionForm form = (CouponListingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        String siteId = adminBean.getSite().getSiteId();

        Query query = null;
        String sql = "from Coupon coupon where siteId = :siteId ";
        if (form.getSrCouponCode().length() > 0) {
        	sql += "and couponCode like :couponCode ";
        }
        if (form.getSrCouponName().length() > 0) {
        	sql += "and coupon.couponLanguage.couponName like :couponName ";
        }
        if (!form.getSrPublished().equals("*")) {
        	sql += "and published = :published ";
        }
        if (form.getSrCouponPublishDate().length() > 0) {
        	sql += "and :couponPublishedDate between couponStartDate and couponEndDate ";
        }
        sql += "order by couponCode";

        query = em.createQuery(sql);
        query.setParameter("siteId", siteId);
        
        Date date = null;
        if (form.getSrCouponCode().length() > 0) {
        	query.setParameter("couponCode", "%" + form.getSrCouponCode() + "%");
        }
        if (form.getSrCouponName().length() > 0) {
        	query.setParameter("couponName", "%" + form.getSrCouponName() + "%");
        }
        if (!form.getSrPublished().equals("*")) {
        	query.setParameter("published", form.getSrPublished());
        }
        if (form.getSrCouponPublishDate().length() > 0) {
        	date = Format.getDate(form.getSrCouponPublishDate());
        	query.setParameter("couponPublishedDate", date);
        }

        List<?> list = query.getResultList();
        if (Format.isNullOrEmpty(form.getSrPageNo())) {
        	form.setSrPageNo("1");
        }
        int pageNo = Integer.parseInt(form.getSrPageNo());
        calcPage(adminBean, form, list, pageNo);
        Vector<CouponDisplayForm> vector = new Vector<CouponDisplayForm>();
        int startRecord = (form.getPageNo() - 1) * adminBean.getListingPageSize();
        int endRecord = startRecord + adminBean.getListingPageSize();
        for (int i = startRecord; i < list.size() && i < endRecord; i++) {
        	Coupon coupon = (Coupon) list.get(i);
        	CouponLanguage couponLanguage = null;
        	for (CouponLanguage language : coupon.getCouponLanguages()) {
        		if (language.getSiteProfileClass().getSiteProfileClassId().equals(form.getSiteProfileClassDefaultId())) {
        			couponLanguage = language;
        		}
        	}
        	CouponDisplayForm couponDisplay = new CouponDisplayForm();
        	couponDisplay.setCouponCode(coupon.getCouponCode());
        	couponDisplay.setCouponName(couponLanguage.getCouponName());
        	couponDisplay.setCouponId(Format.getLong(coupon.getCouponId()));
        	couponDisplay.setCouponStartDate(Format.getFullDate(coupon.getCouponStartDate()));
        	couponDisplay.setCouponEndDate(Format.getFullDate(coupon.getCouponEndDate()));
        	couponDisplay.setPublished(String.valueOf(coupon.getPublished()));
            vector.add(couponDisplay);
        }
        CouponDisplayForm coupons[] = new CouponDisplayForm[vector.size()];
        vector.copyInto(coupons);
        form.setCoupons(coupons);
    }
	
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        CouponListingActionForm form = (CouponListingActionForm) actionForm;

        try {
	        if (form.getCoupons() != null) {
	        	CouponDisplayForm coupons[] = form.getCoupons();
		        for (int i = 0; i < coupons.length; i++) {
		        	if (coupons[i].getRemove() == null) {
		        		continue;
		        	}
		        	if (!coupons[i].getRemove().equals("Y")) {
		        		continue;
		        	}
		            Coupon coupon = new Coupon();
		            coupon = CouponDAO.load(getAdminBean(request).getSite().getSiteId(), Format.getLong(coupons[i].getCouponId()));
		            em.remove(coupon);
		        }
		        em.getTransaction().commit();
	        }
        }
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.coupons.constraint"));
				saveMessages(request, errors);
		        ActionForward forward = actionMapping.findForward("removeError") ;
		        return forward;
			}
			throw e;
        }

        ActionForward forward = actionMapping.findForward("removed") ;
        forward = new ActionForward(forward.getPath() + "?process=list&srPageNo=" + form.getPageNo(), forward.getRedirect());
        return forward;
    }


	public void initForm(AdminListingActionForm actionForm) {
		CouponListingActionForm form = (CouponListingActionForm) actionForm;
		form.setCoupons(null);
		form.setSrPublished("*");
	}

	public void initSearchInfo(AdminListingActionForm actionForm, String siteId) throws Exception {
    	CouponListingActionForm form = (CouponListingActionForm) actionForm;
    	if (form.getSrPublished() == null) {
    		form.setSrPublished("*");
    	}
	}

    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("remove", "remove");
        map.put("list", "list");
        map.put("start", "start");
        map.put("back", "back");
        map.put("search", "search");
        map.put("cancel", "cancel");
        return map;
    }
}
