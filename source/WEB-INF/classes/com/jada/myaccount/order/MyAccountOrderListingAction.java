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

package com.jada.myaccount.order;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.content.ContentBean;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.PaymentTran;
import com.jada.jpa.entity.ShipHeader;
import com.jada.myaccount.portal.MyAccountPortalAction;
import com.jada.system.Languages;
import com.jada.util.Constants;
import com.jada.util.Format;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MyAccountOrderListingAction
    extends MyAccountPortalAction {
	
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
        MyAccountOrderStatusListingActionForm form = (MyAccountOrderStatusListingActionForm) actionForm;
        init(request, form);
        form.setOrders(null);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward list(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        MyAccountOrderStatusListingActionForm form = (MyAccountOrderStatusListingActionForm) actionForm;
        init(request, form);
        ContentBean contentBean = getContentBean(request);
        String siteId = contentBean.getContentSessionKey().getSiteId();
        
        Query query = null;
        if (form.getSrPageNo() == null || form.getSrPageNo().length() == 0) {
        	form.setSrPageNo("1");
        }
        String sql = "select   orderHeader " +
        			 "from     OrderHeader orderHeader " +
        			 "left     outer join orderHeader.siteDomain siteDomain " +
        			 "where    siteDomain.site.siteId = :siteId " +
        			 "and      orderHeader.customer.custId = :custId "  +
        			 "and 	   orderHeader.orderStatus != '" + Constants.ORDERSTATUS_CANCELLED + "' "  +
        			 "and 	   orderHeader.orderStatus != '" + Constants.ORDERSTATUS_OPEN + "' ";
        sql += "order by orderHeader.orderDate desc";

        query = em.createQuery(sql);
        query.setParameter("siteId", siteId);
        query.setParameter("custId", getCustId(request));
        List<?> list = query.getResultList();
        int pageNo = Integer.parseInt(form.getSrPageNo());
        calcPage(form, list, pageNo);
        Vector<OrderDisplayForm> vector = new Vector<OrderDisplayForm>();
        int startRecord = (pageNo - 1) * Constants.MYACCOUNT_LISTING_PAGE_SIZE;
        int endRecord = startRecord + Constants.MYACCOUNT_LISTING_PAGE_SIZE;
        for (int i = startRecord; i < list.size() && i < endRecord; i++) {
        	OrderHeader orderHeader = (OrderHeader) list.get(i);
        	OrderDisplayForm orderDisplay = new OrderDisplayForm();
        	orderDisplay.setOrderHeaderId(Format.getLong(orderHeader.getOrderHeaderId()));
        	orderDisplay.setOrderNum(orderHeader.getOrderNum());
        	orderDisplay.setCustFirstName(orderHeader.getCustomer().getCustAddress().getCustFirstName());
        	orderDisplay.setCustLastName(orderHeader.getCustomer().getCustAddress().getCustLastName());
        	orderDisplay.setCustEmail(orderHeader.getCustEmail());
        	orderDisplay.setCustCityName("");
        	orderDisplay.setCustStateCode("");
        	orderDisplay.setCustCountryCode("");
        	orderDisplay.setPriceTotal("");
        	orderDisplay.setShippingTotal(Format.getFloatObj(orderHeader.getShippingTotal()));
        	orderDisplay.setTaxTotal("");
        	orderDisplay.setOrderTotal(contentBean.getFormatter().formatCurrency(orderHeader.getOrderTotal()));
        	String orderStatus = Languages.getLangTranValue(contentBean.getContentSessionKey().getLangId(), "content.text.order.status." + orderHeader.getOrderStatus());
        	orderDisplay.setOrderStatus(orderStatus);
        	orderDisplay.setOrderDatetime(Format.getDate(orderHeader.getOrderDate()));
        	PaymentTran payment = orderHeader.getPaymentTran();
        	if (payment != null) {
        		orderDisplay.setPaymentReference1(payment.getPaymentReference1());
        	}
        	for (ShipHeader shipHeader : orderHeader.getShipHeaders()) {
        		orderDisplay.setShipDatetime(Format.getDate(shipHeader.getShipDate()));
        	}
            vector.add(orderDisplay);
        }
        form.setOrders(vector);
        
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    void calcPage(MyAccountOrderStatusListingActionForm form, List<?> list, int pageNo) {
        form.setPageNo(pageNo);
        
        int listingPageSize = Constants.MYACCOUNT_LISTING_PAGE_SIZE;

        /* Calc Page Count */
        int pageCount = (list.size() - list.size() % listingPageSize) / listingPageSize;
        if (list.size() % listingPageSize > 0) {
            pageCount++;
        }
        form.setPageCount(pageCount);
        
        int half = Constants.MYACCOUNT_LISTING_PAGE_COUNT / 2;

        /* Calc Start Page */
        int startPage = pageNo - half + 1;
        if (startPage < 1) {
        	startPage = 1;
        }
        form.setStartPage(startPage);

        /* Calc End Page */
        /* Trying to make sure the maximum number of navigation is visible */
        int endPage = startPage + Constants.MYACCOUNT_LISTING_PAGE_COUNT - 1;
        while (endPage > pageCount && startPage > 1) {
        	endPage--;
        	startPage--;
        }
        /* Still not possible.  Trimming navigation. */
        if (endPage > pageCount) {
            if (pageCount == 0) {
                endPage = 1;
            }
            else {
                endPage = pageCount;
            }
        }
        form.setStartPage(startPage);
        form.setEndPage(endPage);
    }

    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("list", "list");
        map.put("start", "start");
        return map;
    }
}