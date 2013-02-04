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

package com.jada.myaccount.shipinfo;

import com.jada.content.ContentLookupDispatchAction;
import com.jada.dao.CustomerDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.CustomerAddress;
import com.jada.jpa.entity.State;
import com.jada.myaccount.shipinfo.MyAccountShipInfoActionForm;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import javax.persistence.Query;
import javax.persistence.EntityManager;

public class MyAccountShipInfoAction extends ContentLookupDispatchAction {
    Logger logger = Logger.getLogger(MyAccountShipInfoAction.class);

    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	MyAccountShipInfoActionForm form = (MyAccountShipInfoActionForm) actionForm;
    	ActionForward actionForward = actionMapping.findForward("success");
    	Customer customer = getCustomer(request);
		String siteId = getContentBean(request).getContentSessionKey().getSiteId();

    	customer = CustomerDAO.load(siteId, customer.getCustId());
    	CustomerAddress billingAddress = getBillingAddress(customer);
    	if (billingAddress != null) {
    		form.setBillingCustPrefix(billingAddress.getCustPrefix());
    		form.setBillingCustFirstName(billingAddress.getCustFirstName());
    		form.setBillingCustMiddleName(billingAddress.getCustMiddleName());
    		form.setBillingCustLastName(billingAddress.getCustLastName());
    		form.setBillingCustSuffix(billingAddress.getCustSuffix());
    		form.setBillingCustAddressLine1(billingAddress.getCustAddressLine1());
    		form.setBillingCustAddressLine2(billingAddress.getCustAddressLine2());
    		form.setBillingCustCityName(billingAddress.getCustCityName());
    		form.setBillingCustStateCode(billingAddress.getCustStateCode());
    		form.setBillingCustCountryCode(billingAddress.getCustCountryCode());
    		form.setBillingCustZipCode(billingAddress.getCustZipCode());
    		form.setBillingCustPhoneNum(billingAddress.getCustPhoneNum());
    		form.setBillingCustFaxNum(billingAddress.getCustFaxNum());
    	}
    	CustomerAddress shippingAddress = getShippingAddress(customer);
    	if (shippingAddress != null) {
    		form.setShippingCustPrefix(shippingAddress.getCustPrefix());
    		form.setShippingCustFirstName(shippingAddress.getCustFirstName());
    		form.setShippingCustMiddleName(shippingAddress.getCustMiddleName());
    		form.setShippingCustLastName(shippingAddress.getCustLastName());
    		form.setShippingCustSuffix(shippingAddress.getCustSuffix());
    		form.setShippingCustAddressLine1(shippingAddress.getCustAddressLine1());
    		form.setShippingCustAddressLine2(shippingAddress.getCustAddressLine2());
    		form.setShippingCustCityName(shippingAddress.getCustCityName());
    		form.setShippingCustStateCode(shippingAddress.getCustStateCode());
    		form.setShippingCustCountryCode(shippingAddress.getCustCountryCode());
    		form.setShippingCustZipCode(shippingAddress.getCustZipCode());
    		form.setShippingCustPhoneNum(shippingAddress.getCustPhoneNum());
    		form.setShippingCustFaxNum(shippingAddress.getCustFaxNum());
    	}
    	else {
    		form.setUseBilling(true);
    	}
    	initSearchInfo(form, siteId);
    	createEmptySecureTemplateInfo(request);
        return actionForward;
    }

    public ActionForward update(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
		createEmptySecureTemplateInfo(request);
		String siteId = getContentBean(request).getContentSessionKey().getSiteId();
		MyAccountShipInfoActionForm form = (MyAccountShipInfoActionForm) actionForm;

    	ActionMessages messages = validate(form);
    	if (messages.size() > 0) {
        	initSearchInfo(form, siteId);
			saveMessages(request, messages);
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
    	}
    	
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Customer customer = CustomerDAO.load(siteId, getCustomer(request).getCustId());
		
		customer.setRecUpdateBy(Constants.USERNAME_CUSTOMER);
		customer.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(customer);
		
    	CustomerAddress billingAddress = getBillingAddress(customer);
    	if (billingAddress == null) {
    		billingAddress = new CustomerAddress();
    		billingAddress.setRecCreateBy(Constants.USERNAME_CUSTOMER);
    		billingAddress.setRecCreateDatetime(new Date(System.currentTimeMillis()));
    		customer.getCustAddresses().add(billingAddress);
    	}
    	billingAddress.setCustPrefix(form.getBillingCustPrefix());
    	billingAddress.setCustFirstName(form.getBillingCustFirstName());
    	billingAddress.setCustMiddleName(form.getBillingCustMiddleName());
    	billingAddress.setCustLastName(form.getBillingCustLastName());
    	billingAddress.setCustSuffix(form.getBillingCustSuffix());
    	billingAddress.setCustAddressType(Constants.CUSTOMER_ADDRESS_BILLING);
    	billingAddress.setCustAddressLine1(form.getBillingCustAddressLine1());
    	billingAddress.setCustAddressLine2(form.getBillingCustAddressLine2());
    	billingAddress.setCustCityName(form.getBillingCustCityName());
    	billingAddress.setCustStateCode(form.getBillingCustStateCode());
    	billingAddress.setCustStateName(Utility.getStateName(siteId, form.getBillingCustStateCode()));
    	billingAddress.setCustCountryCode(form.getBillingCustCountryCode());
    	billingAddress.setCustCountryName(Utility.getCountryName(siteId, form.getBillingCustCountryCode()));
    	billingAddress.setCustZipCode(form.getBillingCustZipCode());
    	billingAddress.setCustPhoneNum(form.getBillingCustPhoneNum());
		billingAddress.setCustFaxNum(form.getBillingCustPhoneNum());
		billingAddress.setRecUpdateBy(Constants.USERNAME_CUSTOMER);
		billingAddress.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		if (billingAddress.getCustAddressId() == null) {
			em.persist(billingAddress);
		}
		
    	CustomerAddress shippingAddress = getShippingAddress(customer);
    	if (form.isUseBilling()) {
    		if (shippingAddress != null) {
    			customer.getCustAddresses().remove(shippingAddress);
    			em.remove(shippingAddress);
    		}
    	}
    	else {
        	if (shippingAddress == null) {
        		shippingAddress = new CustomerAddress();
        		shippingAddress.setRecCreateBy(Constants.USERNAME_CUSTOMER);
        		shippingAddress.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        		customer.getCustAddresses().add(shippingAddress);
        	}
        	shippingAddress.setCustPrefix(form.getShippingCustPrefix());
        	shippingAddress.setCustFirstName(form.getShippingCustFirstName());
        	shippingAddress.setCustMiddleName(form.getShippingCustMiddleName());
        	shippingAddress.setCustLastName(form.getShippingCustLastName());
        	shippingAddress.setCustSuffix(form.getShippingCustSuffix());
        	shippingAddress.setCustAddressType(Constants.CUSTOMER_ADDRESS_BILLING);
        	shippingAddress.setCustAddressLine1(form.getShippingCustAddressLine1());
        	shippingAddress.setCustAddressLine2(form.getShippingCustAddressLine2());
        	shippingAddress.setCustCityName(form.getShippingCustCityName());
        	shippingAddress.setCustStateCode(form.getShippingCustStateCode());
        	shippingAddress.setCustStateName(Utility.getStateName(siteId, form.getShippingCustStateCode()));
        	shippingAddress.setCustCountryCode(form.getShippingCustCountryCode());
        	shippingAddress.setCustCountryName(Utility.getCountryName(siteId, form.getShippingCustCountryCode()));
        	shippingAddress.setCustZipCode(form.getShippingCustZipCode());
        	shippingAddress.setCustPhoneNum(form.getShippingCustPhoneNum());
    		shippingAddress.setCustFaxNum(form.getShippingCustPhoneNum());
    		shippingAddress.setRecUpdateBy(Constants.USERNAME_CUSTOMER);
    		shippingAddress.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
    		if (shippingAddress.getCustAddressId() == null) {
    			em.persist(shippingAddress);
    		}
    	}
    	initSearchInfo(form, siteId);
		messages.add("message", new ActionMessage("message.information.updated"));
		saveMessages(request, messages);  

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public CustomerAddress getShippingAddress(Customer customer) {
    	for (CustomerAddress address : customer.getCustAddresses()) {
    		if (address.getCustAddressType().equals(Constants.CUSTOMER_ADDRESS_SHIPPING)) {
    			return address;
    		}
    	}
    	return null;
    }
    
    
    public CustomerAddress getBillingAddress(Customer customer) {
    	for (CustomerAddress address : customer.getCustAddresses()) {
    		if (address.getCustAddressType().equals(Constants.CUSTOMER_ADDRESS_BILLING)) {
    			return address;
    		}
    	}
    	return null;
    }

    public ActionMessages validate(MyAccountShipInfoActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	if (Format.isNullOrEmpty(form.getBillingCustFirstName())) {
    		errors.add("billingCustFirstName", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getBillingCustLastName())) {
    		errors.add("billingCustLastName", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getBillingCustAddressLine1())) {
    		errors.add("billingCustAddressLine1", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getBillingCustCityName())) {
    		errors.add("billingCustCityName", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getBillingCustStateCode())) {
    		errors.add("billingCustStateCode", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getBillingCustCountryCode())) {
    		errors.add("billingCustCountryCode", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getBillingCustZipCode())) {
    		errors.add("billingCustZipCode", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getBillingCustPhoneNum())) {
    		errors.add("billingCustPhoneNum", new ActionMessage("error.string.required"));
    	}
    	if (!form.isUseBilling()) {
        	if (Format.isNullOrEmpty(form.getShippingCustFirstName())) {
        		errors.add("shippingCustFirstName", new ActionMessage("error.string.required"));
        	}
        	if (Format.isNullOrEmpty(form.getShippingCustLastName())) {
        		errors.add("shippingCustLastName", new ActionMessage("error.string.required"));
        	}
        	if (Format.isNullOrEmpty(form.getShippingCustAddressLine1())) {
        		errors.add("shippingCustAddressLine1", new ActionMessage("error.string.required"));
        	}
        	if (Format.isNullOrEmpty(form.getShippingCustCityName())) {
        		errors.add("shippingCustCityName", new ActionMessage("error.string.required"));
        	}
        	if (Format.isNullOrEmpty(form.getShippingCustStateCode())) {
        		errors.add("shippingCustStateCode", new ActionMessage("error.string.required"));
        	}
        	if (Format.isNullOrEmpty(form.getShippingCustCountryCode())) {
        		errors.add("shippingCustCountryCode", new ActionMessage("error.string.required"));
        	}
        	if (Format.isNullOrEmpty(form.getShippingCustZipCode())) {
        		errors.add("shippingCustZipCode", new ActionMessage("error.string.required"));
        	}
        	if (Format.isNullOrEmpty(form.getShippingCustPhoneNum())) {
        		errors.add("shippingCustPhoneNum", new ActionMessage("error.string.required"));
        	}

    	}
    	return errors;
    }
    
    public void initSearchInfo(MyAccountShipInfoActionForm form, String siteId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from country in class Country where country.siteId = :siteId order by countryName");
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
     	sql = "from		State state " +
     		  "where    state.country.siteId = :siteId " + 
     		  "order	by state.country.countryId, state.stateName";
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
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        map.put("update", "update");
        return map;
    }
}