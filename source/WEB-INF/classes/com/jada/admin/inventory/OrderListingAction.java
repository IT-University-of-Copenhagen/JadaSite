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

package com.jada.admin.inventory;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminListingAction;
import com.jada.admin.AdminListingActionForm;
import com.jada.admin.inventory.OrderListingActionForm;
import com.jada.dao.OrderHeaderDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.OrderAddress;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.State;
import com.jada.util.Format;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class OrderListingAction
    extends AdminListingAction {
	
	public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
		ActionForward forward = super.start(actionMapping, actionForm, request, response);
		initInfo((AdminListingActionForm) actionForm, request);
		return forward;
	}
	
    public void extract(AdminListingActionForm actionForm, HttpServletRequest request)
        throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        OrderListingActionForm form = (OrderListingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();

        ActionMessages errors = validate(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return;
		}

        Query query = null;
        
        if (form.getSrPageNo().length() == 0) {
        	form.setSrPageNo("1");
        }
        String sql = "select orderHeader " +
        			 "from   OrderHeader orderHeader " +
        			 "left   join orderHeader.custAddress custAddress " +
        			 "where	 orderHeader.siteDomain.site.siteId = :siteId ";
        sql += "and orderHeader.orderDate between :orderCreatedOnStart and :orderCreatedOnEnd ";
        if (form.getSrOrderNum().length() > 0) {
        	sql += "and orderHeader.orderNum like :orderNum ";
        }
        if (form.getSrCustFirstName().length() > 0) {
        	sql += "and custAddress.custFirstName like :custFirstName ";
        }
        if (form.getSrCustLastName().length() > 0) {
        	sql += "and custAddress.custLastName like :custLastName ";
        }
        if (form.getSrCustEmail().length() > 0) {
        	sql += "and orderHeader.custEmail like :custEmail ";
        }
        if (form.getSrCustCityName().length() > 0) {
        	sql += "and custAddress.custCityName like :custCityName ";
        }
        if (!form.getSrCustStateCode().equals("All")) {
        	sql += "and custAddress.custStateCode like :custStateCode ";
        }
        if (!form.getSrCustCountryCode().equals("All")) {
        	sql += "and custAddress.custCountryCode like :custCountryCode ";
        }
        if (!form.getSrOrderStatus().equals("All")) {
        	sql += "and orderHeader.orderStatus = :orderStatus ";
        }
        if (!form.getOrderAbundantLoc().equals("All")) {
        	sql += "and orderHeader.orderAbundantLoc = :orderAbundantLoc ";
        }
        sql += "order by orderHeader.orderDate desc ";

        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        Date date = null;
        if (form.getSrOrderCreatedOnStart().length() > 0) {
        	date = Format.getDate(form.getSrOrderCreatedOnStart());
        	query.setParameter("orderCreatedOnStart", date);
        }
        else {
        	query.setParameter("orderCreatedOnStart", Format.LOWDATE);
        }
        if (form.getSrOrderCreatedOnEnd().length() > 0) {
        	date = Format.getDate(form.getSrOrderCreatedOnEnd());
        	Calendar calendar = Calendar.getInstance();
        	calendar.setTime(date);
        	calendar.add(Calendar.DATE, 1);
        	query.setParameter("orderCreatedOnEnd", calendar.getTime());
        }
        else {
        	query.setParameter("orderCreatedOnEnd", Format.HIGHDATE);
        }
        if (form.getSrOrderNum().length() > 0) {
        	query.setParameter("orderNum", form.getSrOrderNum());
        }
        if (form.getSrCustFirstName().length() > 0) {
        	query.setParameter("custFirstName", form.getSrCustFirstName() + "%");
        }
        if (form.getSrCustLastName().length() > 0) {
        	query.setParameter("custLastName", form.getSrCustLastName() + "%");
        }
        if (form.getSrCustEmail().length() > 0) {
        	query.setParameter("custEmail", form.getSrCustEmail() + "%");
        }
        if (form.getSrCustCityName().length() > 0) {
        	query.setParameter("custCityName", form.getSrCustCityName());
        }
        if (!form.getSrCustStateCode().equals("All")) {
        	query.setParameter("custStateCode", form.getSrCustStateCode());
        }
        if (!form.getSrCustCountryCode().equals("All")) {
        	query.setParameter("custCountryCode", form.getSrCustCountryCode());
        }
        if (!form.getSrOrderStatus().equals("All")) {
        	query.setParameter("orderStatus", form.getSrOrderStatus());
        }
        if (!form.getOrderAbundantLoc().equals("All")) {
        	query.setParameter("orderAbundantLoc", form.getOrderAbundantLoc());
        }
        List<?> list = query.getResultList();
        int pageNo = Integer.parseInt(form.getSrPageNo());
        calcPage(adminBean, form, list, pageNo);
        Vector<OrderListingDisplayForm> vector = new Vector<OrderListingDisplayForm>();
        int startRecord = (pageNo - 1) * adminBean.getListingPageSize();
        int endRecord = startRecord + adminBean.getListingPageSize();
        for (int i = startRecord; i < list.size() && i < endRecord; i++) {
        	OrderHeader orderHeader = (OrderHeader) list.get(i);
        	OrderAddress orderAddress = orderHeader.getCustAddress();
        	OrderListingDisplayForm orderDisplay = new OrderListingDisplayForm();
        	orderDisplay.setOrderHeaderId(Format.getLong(orderHeader.getOrderHeaderId()));
        	orderDisplay.setOrderNum(orderHeader.getOrderNum());
        	orderDisplay.setCustFirstName(orderAddress.getCustFirstName());
        	orderDisplay.setCustLastName(orderAddress.getCustLastName());
        	orderDisplay.setCustEmail(orderHeader.getCustEmail());
        	orderDisplay.setCustCityName(orderAddress.getCustCityName());
        	orderDisplay.setCustStateCode(orderAddress.getCustStateCode());
        	orderDisplay.setCustCountryCode(orderAddress.getCustCountryCode());
        	orderDisplay.setOrderTotal(Format.getFloatObj(orderHeader.getOrderTotal()));
        	orderDisplay.setOrderStatus(orderHeader.getOrderStatus());
        	orderDisplay.setOrderDate(Format.getFullDatetime(orderHeader.getOrderDate()));
            vector.add(orderDisplay);
        }
        form.setOrders(vector);
    }
    
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	Site site = getAdminBean(request).getSite();
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        OrderListingActionForm form = (OrderListingActionForm) actionForm;
        String orderHeaderIds[] = form.getOrderHeaderIds();
        
        if (orderHeaderIds != null) {
	        for (int i = 0; i < orderHeaderIds.length; i++) {
	            OrderHeader orderHeader = OrderHeaderDAO.load(site.getSiteId(), Format.getLong(orderHeaderIds[i]));
	            em.remove(orderHeader);
	        }
        }

        ActionForward forward = new ActionForward();
        forward = new ActionForward(request.getServletPath() + "?process=list", true);
        return forward;
    }
    
    public void initSearchInfo(AdminListingActionForm actionForm, String siteId) throws Exception {
    	OrderListingActionForm form = (OrderListingActionForm) actionForm;
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from country in class Country where country.siteId = :siteId");
    	query.setParameter("siteId", siteId);
     	Iterator<?> iterator = query.getResultList().iterator();
     	Vector<LabelValueBean> vector = new Vector<LabelValueBean>();
     	while (iterator.hasNext()) {
     		Country country = (Country) iterator.next();
     		LabelValueBean bean = new LabelValueBean(country.getCountryName(), country.getCountryCode());
     		vector.add(bean);
     	}
     	LabelValueBean countries[] = new LabelValueBean[vector.size()];
     	vector.copyInto(countries);
     	form.setCountries(countries);

     	String sql = "";
     	sql = "select   state " +
     		  "from		State state " +
              "left	    join state.country country " +
              "where	country.siteId = :siteId " +
              "order	by country.countryId, state.stateName";
     	query = em.createQuery(sql);
    	query.setParameter("siteId", siteId);
     	iterator = query.getResultList().iterator();
     	vector = new Vector<LabelValueBean>();
     	while (iterator.hasNext()) {
     		State state = (State) iterator.next();
     		LabelValueBean bean = new LabelValueBean(state.getStateName(), state.getStateCode());
     		vector.add(bean);
     	}
     	LabelValueBean states[] = new LabelValueBean[vector.size()];
     	vector.copyInto(states);
     	form.setStates(states);
     	
     	vector = new Vector<LabelValueBean>();
     	vector.add(new LabelValueBean("Open", "O"));
     	vector.add(new LabelValueBean("Closed", "C"));
     	LabelValueBean orderStatuses[] = new LabelValueBean[vector.size()];
     	vector.copyInto(orderStatuses);
     	form.setOrderStatuses(orderStatuses);
    }
    
    public void initInfo(AdminListingActionForm actionForm, HttpServletRequest request) throws Exception {
    	OrderListingActionForm form = (OrderListingActionForm) actionForm;
		MessageResources resources = this.getResources(request);
     	Vector<LabelValueBean> vector = new Vector<LabelValueBean>();
     	vector.add(new LabelValueBean(resources.getMessage("order.step.CT"), "CT"));
     	vector.add(new LabelValueBean(resources.getMessage("order.step.RA"), "RA"));
     	vector.add(new LabelValueBean(resources.getMessage("order.step.RP"), "RP"));
     	vector.add(new LabelValueBean(resources.getMessage("order.step.CC"), "CC"));
     	vector.add(new LabelValueBean(resources.getMessage("order.step.PP"), "PP"));
     	vector.add(new LabelValueBean(resources.getMessage("order.step.SQ"), "SQ"));
     	vector.add(new LabelValueBean(resources.getMessage("order.step.Q_CT"), "Q_CT"));
     	vector.add(new LabelValueBean(resources.getMessage("order.step.Q_RP"), "Q_RP"));
     	vector.add(new LabelValueBean(resources.getMessage("order.step.Q_CC"), "Q_CC"));
    	LabelValueBean orderAbundantLocs[] = new LabelValueBean[vector.size()];
     	vector.copyInto(orderAbundantLocs);
     	form.setOrderAbundantLocs(orderAbundantLocs);
    }

    public void initForm(AdminListingActionForm form) {
    	((OrderListingActionForm) form).setOrders(null);
    }
    
    public ActionMessages validate(OrderListingActionForm form) throws Exception {
    	ActionMessages errors = new ActionMessages();
    	
    	if (!Format.isNullOrEmpty(form.getSrOrderCreatedOnStart()) && !Format.isDate(form.getSrOrderCreatedOnStart())) {
    		errors.add("srOrderCreatedOnStart", new ActionMessage("error.date.invalid"));
    	}
    	if (!Format.isNullOrEmpty(form.getSrOrderCreatedOnEnd()) && !Format.isDate(form.getSrOrderCreatedOnEnd())) {
    		errors.add("srOrderCreatedOnEnd", new ActionMessage("error.date.invalid"));
    	}
    	return errors;
    }

    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("remove", "remove");
        map.put("search", "search");
        map.put("start", "start");
        map.put("remove", "remove");
        map.put("list", "list");
        map.put("back", "back");
        return map;
    }
}