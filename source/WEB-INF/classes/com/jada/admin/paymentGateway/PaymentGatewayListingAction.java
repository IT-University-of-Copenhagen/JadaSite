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

package com.jada.admin.paymentGateway;

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
import com.jada.dao.PaymentGatewayDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.PaymentGateway;
import com.jada.util.Format;
import com.jada.util.Utility;

public class PaymentGatewayListingAction extends AdminListingAction {

	public void extract(AdminListingActionForm actionForm, HttpServletRequest request) throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        PaymentGatewayListingActionForm form = (PaymentGatewayListingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        String siteId = adminBean.getSite().getSiteId();

        Query query = null;
        String sql = "from PaymentGateway paymentGateway where siteId = :siteId ";
        if (form.getSrPaymentGatewayName().length() > 0) {
        	sql += "and paymentGatewayName like :paymentGatewayName ";
        }
        sql += "order by paymentGatewayName";

        query = em.createQuery(sql);
        query.setParameter("siteId", siteId);
        
        if (form.getSrPaymentGatewayName().length() > 0) {
        	query.setParameter("paymentGatewayName", "%" + form.getSrPaymentGatewayName() + "%");
        }

        List<?> list = query.getResultList();
        if (Format.isNullOrEmpty(form.getSrPageNo())) {
        	form.setSrPageNo("1");
        }
        int pageNo = Integer.parseInt(form.getSrPageNo());
        calcPage(adminBean, form, list, pageNo);
        Vector<PaymentGatewayDisplayForm> vector = new Vector<PaymentGatewayDisplayForm>();
        int startRecord = (form.getPageNo() - 1) * adminBean.getListingPageSize();
        int endRecord = startRecord + adminBean.getListingPageSize();
        for (int i = startRecord; i < list.size() && i < endRecord; i++) {
        	PaymentGateway paymentGateway = (PaymentGateway) list.get(i);
        	PaymentGatewayDisplayForm paymentGatewayDisplay = new PaymentGatewayDisplayForm();
        	paymentGatewayDisplay.setPaymentGatewayName(paymentGateway.getPaymentGatewayName());
        	paymentGatewayDisplay.setPaymentGatewayId(paymentGateway.getPaymentGatewayId().toString());
            vector.add(paymentGatewayDisplay);
        }
        PaymentGatewayDisplayForm paymentGateways[] = new PaymentGatewayDisplayForm[vector.size()];
        vector.copyInto(paymentGateways);
        form.setPaymentGateways(paymentGateways);
    }
	
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        PaymentGatewayListingActionForm form = (PaymentGatewayListingActionForm) actionForm;

        try {
	        if (form.getPaymentGateways() != null) {
	        	PaymentGatewayDisplayForm paymentGateways[] = form.getPaymentGateways();
		        for (int i = 0; i < paymentGateways.length; i++) {
		        	if (!paymentGateways[i].isRemove()) {
		        		continue;
		        	}
		            PaymentGateway paymentGateway = new PaymentGateway();
		            paymentGateway = PaymentGatewayDAO.load(getAdminBean(request).getSite().getSiteId(), Format.getLong(paymentGateways[i].getPaymentGatewayId()));
		            em.remove(paymentGateway);
		        }
		        em.getTransaction().commit();
	        }
        }
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.paymentGateways.constraint"));
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
		PaymentGatewayListingActionForm form = (PaymentGatewayListingActionForm) actionForm;
		form.setPaymentGateways(null);
	}

	public void initSearchInfo(AdminListingActionForm actionForm, String siteId) throws Exception {
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
