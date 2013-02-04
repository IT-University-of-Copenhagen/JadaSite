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

package com.jada.myaccount.address;

import com.jada.content.ContentBean;
import com.jada.content.ContentLookupDispatchAction;
import com.jada.dao.CountryDAO;
import com.jada.dao.CustomerDAO;
import com.jada.dao.StateDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.CustomerAddress;
import com.jada.jpa.entity.Language;
import com.jada.jpa.entity.State;
import com.jada.myaccount.portal.MyAccountPortalAction;
import com.jada.system.Languages;
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
import org.apache.struts.util.LabelValueBean;
import javax.persistence.Query;
import javax.persistence.EntityManager;

public class MyAccountAddressAction extends MyAccountPortalAction {
    Logger logger = Logger.getLogger(MyAccountAddressAction.class);

    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	MyAccountAddressActionForm form = (MyAccountAddressActionForm) actionForm;
    	init(request, form);
    	ActionForward actionForward = actionMapping.findForward("success");
    	Customer customer = getCustomer(request);
		String siteId = getContentBean(request).getContentSessionKey().getSiteId();
    	customer = CustomerDAO.load(siteId, customer.getCustId());
    	
		CustomerAddress custAddress = customer.getCustAddress();
		if (custAddress != null) {
			form.setCustFirstName(custAddress.getCustFirstName());
			form.setCustMiddleName(custAddress.getCustMiddleName());
			form.setCustLastName(custAddress.getCustLastName());
			form.setCustAddressLine1(custAddress.getCustAddressLine1());
			form.setCustAddressLine2(custAddress.getCustAddressLine2());
			form.setCustCityName(custAddress.getCustCityName());
			form.setCustStateCode(custAddress.getCustStateCode());
			form.setCustStateName(custAddress.getCustStateName());
			form.setCustCountryCode(custAddress.getCustCountryCode());
			form.setCustCountryName(custAddress.getCustCountryName());
			form.setCustZipCode(custAddress.getCustZipCode());
			form.setCustPhoneNum(custAddress.getCustPhoneNum());
			form.setCustFaxNum(custAddress.getCustFaxNum());
			if (!Format.isNullOrEmpty(custAddress.getCustStateCode())) {
				form.setCustStateName(Utility.getStateName(siteId, custAddress.getCustStateCode()));
			}
			else {
				form.setCustStateName(custAddress.getCustStateName());
			}
			if (!Format.isNullOrEmpty(custAddress.getCustCountryCode())) {
				form.setCustCountryName(Utility.getCountryName(siteId, custAddress.getCustCountryCode()));
			}
		}
		
		CustomerAddress billingAddress = null;
		for (CustomerAddress address : customer.getCustAddresses()) {
			if (address.getCustAddressType().equals(Constants.CUSTOMER_ADDRESS_BILLING)) {
				billingAddress = address;
				break;
			}
		}
		if (billingAddress != null) {
			form.setBillingCustAddressId(Format.getLong(billingAddress.getCustAddressId()));
			form.setBillingCustFirstName(billingAddress.getCustFirstName());
			form.setBillingCustMiddleName(billingAddress.getCustMiddleName());
			form.setBillingCustLastName(billingAddress.getCustLastName());
			form.setBillingCustAddressLine1(billingAddress.getCustAddressLine1());
			form.setBillingCustAddressLine2(billingAddress.getCustAddressLine2());
			form.setBillingCustCityName(billingAddress.getCustCityName());
			form.setBillingCustStateCode(billingAddress.getCustStateCode());
			form.setBillingCustCountryCode(billingAddress.getCustCountryCode());
			form.setBillingCustZipCode(billingAddress.getCustZipCode());
			form.setBillingCustPhoneNum(billingAddress.getCustPhoneNum());
			form.setBillingCustFaxNum(billingAddress.getCustFaxNum());
			form.setBillingUseAddress(billingAddress.getCustUseAddress());
			if (!Format.isNullOrEmpty(billingAddress.getCustStateCode())) {
				form.setBillingCustStateName(Utility.getStateName(siteId, billingAddress.getCustStateCode()));
			}
			else {
				form.setBillingCustStateName(billingAddress.getCustStateName());
			}
			if (!Format.isNullOrEmpty(billingAddress.getCustCountryCode())) {
				form.setBillingCustCountryName(Utility.getCountryName(siteId, billingAddress.getCustCountryCode()));
			}
		}
		else {
			form.setBillingUseAddress(Constants.CUST_ADDRESS_USE_CUST);
		}
		
		CustomerAddress shippingAddress = null;
		for (CustomerAddress address : customer.getCustAddresses()) {
			if (address.getCustAddressType().equals(Constants.CUSTOMER_ADDRESS_SHIPPING)) {
				shippingAddress = address;
				break;
			}
		}
		if (shippingAddress != null) {
			form.setShippingCustAddressId(Format.getLong(shippingAddress.getCustAddressId()));
			form.setShippingCustFirstName(shippingAddress.getCustFirstName());
			form.setShippingCustMiddleName(shippingAddress.getCustMiddleName());
			form.setShippingCustLastName(shippingAddress.getCustLastName());
			form.setShippingCustAddressLine1(shippingAddress.getCustAddressLine1());
			form.setShippingCustAddressLine2(shippingAddress.getCustAddressLine2());
			form.setShippingCustCityName(shippingAddress.getCustCityName());
			form.setShippingCustStateCode(shippingAddress.getCustStateCode());
			form.setShippingCustCountryCode(shippingAddress.getCustCountryCode());
			form.setShippingCustZipCode(shippingAddress.getCustZipCode());
			form.setShippingCustPhoneNum(shippingAddress.getCustPhoneNum());
			form.setShippingCustFaxNum(shippingAddress.getCustFaxNum());
			form.setShippingUseAddress(shippingAddress.getCustUseAddress());
			if (!Format.isNullOrEmpty(shippingAddress.getCustStateCode())) {
				form.setShippingCustStateName(Utility.getStateName(siteId, shippingAddress.getCustStateCode()));
			}
			else {
				form.setShippingCustStateName(shippingAddress.getCustStateName());
			}
			if (!Format.isNullOrEmpty(shippingAddress.getCustCountryCode())) {
				form.setShippingCustCountryName(Utility.getCountryName(siteId, shippingAddress.getCustCountryCode()));
			}
		}
		else {
			form.setShippingUseAddress(Constants.CUST_ADDRESS_USE_CUST);
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
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		ContentBean contentBean = getContentBean(request);
		String siteId = contentBean.getContentSessionKey().getSiteId();
    	Language language = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getLanguage();
		createEmptySecureTemplateInfo(request);
		MyAccountAddressActionForm form = (MyAccountAddressActionForm) actionForm;
    	init(request, form);
    	validate(siteId, form, language.getLangId());
    	if (form.hasMessage()) {
			initSearchInfo(form, siteId);
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
    	}
    	
		Customer customer = CustomerDAO.load(siteId, getCustomer(request).getCustId());
        
        CustomerAddress custAddress = customer.getCustAddress();
		if (custAddress == null) {
			custAddress = new CustomerAddress();
			customer.setCustAddress(custAddress);
			custAddress.setCustAddressType(Constants.CUSTOMER_ADDRESS_CUST);
			customer.getCustAddresses().add(custAddress);
			custAddress.setRecCreateBy(Constants.USERNAME_CUSTOMER);
			custAddress.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		custAddress.setCustUseAddress(Constants.CUST_ADDRESS_USE_OWN);
		custAddress.setCustFirstName(form.getCustFirstName());
		custAddress.setCustMiddleName(form.getCustMiddleName());
		custAddress.setCustLastName(form.getCustLastName());
		custAddress.setCustSuffix(form.getCustSuffix());
		custAddress.setCustAddressLine1(form.getCustAddressLine1());
		custAddress.setCustAddressLine2(form.getCustAddressLine2());
		custAddress.setCustCityName(form.getCustCityName());
		if (Format.isNullOrEmpty(form.getCustStateName())) {
			custAddress.setCustStateCode(form.getCustStateCode());
			custAddress.setCustStateName(Utility.getStateName(siteId, form.getCustStateCode()));
		}
		else {
			custAddress.setCustStateCode("");
			custAddress.setCustStateName(form.getCustStateName());
		}
		custAddress.setCustCountryCode(form.getCustCountryCode());
		custAddress.setCustCountryName(Utility.getCountryName(siteId, form.getCustCountryCode()));
		custAddress.setCustZipCode(form.getCustZipCode());
		custAddress.setCustPhoneNum(form.getCustPhoneNum());
		custAddress.setCustFaxNum(form.getCustFaxNum());
		custAddress.setRecUpdateBy(Constants.USERNAME_CUSTOMER);
		custAddress.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		custAddress.setState(StateDAO.loadByStateCode(siteId, form.getCustStateCode()));
		custAddress.setCountry(CountryDAO.loadByCountryCode(siteId, form.getCustCountryCode()));
		custAddress.setRecCreateBy(Constants.USERNAME_CUSTOMER);
		custAddress.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		if (custAddress.getCustAddressId() == null) {
			em.persist(custAddress);
		}
		
		CustomerAddress billingAddress = null;
		for (CustomerAddress address : customer.getCustAddresses()) {
			if (address.getCustAddressType().equals(Constants.CUSTOMER_ADDRESS_BILLING)) {
				billingAddress = address;
				break;
			}
		}
		if (billingAddress == null) {
			billingAddress = new CustomerAddress();
			billingAddress.setCustAddressType(Constants.CUSTOMER_ADDRESS_BILLING);
			customer.getCustAddresses().add(billingAddress);
			billingAddress.setRecCreateBy(Constants.USERNAME_CUSTOMER);
			billingAddress.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		billingAddress.setCustUseAddress(form.getBillingUseAddress());
		if (form.billingUseAddress.equals(Constants.CUST_ADDRESS_USE_OWN)) {
			billingAddress.setCustUseAddress(Constants.CUST_ADDRESS_USE_OWN);
			billingAddress.setCustPrefix(form.getBillingCustPrefix());
			billingAddress.setCustFirstName(form.getBillingCustFirstName());
			billingAddress.setCustMiddleName(form.getBillingCustMiddleName());
			billingAddress.setCustLastName(form.getBillingCustLastName());
			billingAddress.setCustSuffix(form.getBillingCustSuffix());
			billingAddress.setCustAddressLine1(form.getBillingCustAddressLine1());
			billingAddress.setCustAddressLine2(form.getBillingCustAddressLine2());
			billingAddress.setCustCityName(form.getBillingCustCityName());
			if (Format.isNullOrEmpty(form.getBillingCustStateName())) {
				billingAddress.setCustStateCode(form.getBillingCustStateCode());
				billingAddress.setCustStateName(Utility.getStateName(siteId, form.getBillingCustStateCode()));
			}
			else {
				billingAddress.setCustStateCode("");
				billingAddress.setCustStateName(form.getBillingCustStateName());
			}
			billingAddress.setCustCountryCode(form.getBillingCustCountryCode());
			billingAddress.setCustCountryName(Utility.getCountryName(siteId, form.getBillingCustCountryCode()));
			billingAddress.setCustZipCode(form.getBillingCustZipCode());
			billingAddress.setCustPhoneNum(form.getBillingCustPhoneNum());
			billingAddress.setCustFaxNum(form.getBillingCustFaxNum());
			billingAddress.setRecUpdateBy(Constants.USERNAME_CUSTOMER);
			billingAddress.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			billingAddress.setState(StateDAO.loadByStateCode(siteId, form.getBillingCustStateCode()));
			billingAddress.setCountry(CountryDAO.loadByCountryCode(siteId, form.getBillingCustCountryCode()));
		}
		billingAddress.setRecUpdateBy(Constants.USERNAME_CUSTOMER);
		billingAddress.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		if (billingAddress.getCustAddressId() == null) {
			em.persist(billingAddress);
		}
		
		CustomerAddress shippingAddress = null;
		for (CustomerAddress address : customer.getCustAddresses()) {
			if (address.getCustAddressType().equals(Constants.CUSTOMER_ADDRESS_SHIPPING)) {
				shippingAddress = address;
				break;
			}
		}
		if (shippingAddress == null) {
			shippingAddress = new CustomerAddress();
			shippingAddress.setCustAddressType(Constants.CUSTOMER_ADDRESS_SHIPPING);
			customer.getCustAddresses().add(shippingAddress);
			shippingAddress.setRecCreateBy(Constants.USERNAME_CUSTOMER);
			shippingAddress.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		shippingAddress.setCustUseAddress(form.getShippingUseAddress());
		if (form.shippingUseAddress.equals(Constants.CUST_ADDRESS_USE_OWN)) {
			shippingAddress.setCustUseAddress(Constants.CUST_ADDRESS_USE_OWN);
			shippingAddress.setCustPrefix(form.getShippingCustPrefix());
			shippingAddress.setCustFirstName(form.getShippingCustFirstName());
			shippingAddress.setCustMiddleName(form.getShippingCustMiddleName());
			shippingAddress.setCustLastName(form.getShippingCustLastName());
			shippingAddress.setCustSuffix(form.getShippingCustSuffix());
			shippingAddress.setCustAddressLine1(form.getShippingCustAddressLine1());
			shippingAddress.setCustAddressLine2(form.getShippingCustAddressLine2());
			shippingAddress.setCustCityName(form.getShippingCustCityName());
			if (Format.isNullOrEmpty(form.getShippingCustStateName())) {
				shippingAddress.setCustStateCode(form.getShippingCustStateCode());
				shippingAddress.setCustStateName(Utility.getStateName(siteId, form.getShippingCustStateCode()));
			}
			else {
				shippingAddress.setCustStateCode("");
				shippingAddress.setCustStateName(form.getShippingCustStateName());
			}
			shippingAddress.setCustCountryCode(form.getShippingCustCountryCode());
			shippingAddress.setCustCountryName(Utility.getCountryName(siteId, form.getShippingCustCountryCode()));
			shippingAddress.setCustZipCode(form.getShippingCustZipCode());
			shippingAddress.setCustPhoneNum(form.getShippingCustPhoneNum());
			shippingAddress.setCustFaxNum(form.getShippingCustFaxNum());
			shippingAddress.setRecUpdateBy(Constants.USERNAME_CUSTOMER);
			shippingAddress.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			shippingAddress.setState(StateDAO.loadByStateCode(siteId, form.getShippingCustStateCode()));
			shippingAddress.setCountry(CountryDAO.loadByCountryCode(siteId, form.getShippingCustCountryCode()));
		}
		shippingAddress.setRecUpdateBy(Constants.USERNAME_CUSTOMER);
		shippingAddress.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		if (shippingAddress.getCustAddressId() == null) {
			em.persist(shippingAddress);
		}
		
        customer.setRecUpdateBy(Constants.USERNAME_CUSTOMER);
        customer.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        em.persist(customer);
        ContentLookupDispatchAction.setCustId(request, customer.getCustId());
		form.addMessage("message", Languages.getLangTranValue(language.getLangId(), "content.text.information.updated"));

    	init(request, form);
		initSearchInfo(form, siteId);
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
 
    protected void initSearchInfo(MyAccountAddressActionForm form, String siteId) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from country in class Country where country.siteId = :siteId order by country.countryName");
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
     	
		Country custCountry = CountryDAO.loadByCountryCode(siteId, form.getCustCountryCode());
		Vector<LabelValueBean> stateVector = new Vector<LabelValueBean>();
		if (custCountry != null) {
			for (State state : custCountry.getStates()) {
				LabelValueBean bean = new LabelValueBean(state.getStateName(), state.getStateCode());
				stateVector.add(bean);
			}
			LabelValueBean states[] = new LabelValueBean[stateVector.size()];
			stateVector.copyInto(states);
	     	form.setCustStates(states);
		}
		else {
			LabelValueBean states[] = {};
	     	form.setCustStates(states);
		}
		
		custCountry = CountryDAO.loadByCountryCode(siteId, form.getBillingCustCountryCode());
		stateVector = new Vector<LabelValueBean>();
		if (custCountry != null) {
			for (State state : custCountry.getStates()) {
				LabelValueBean bean = new LabelValueBean(state.getStateName(), state.getStateCode());
				stateVector.add(bean);
			}
			LabelValueBean states[] = new LabelValueBean[stateVector.size()];
			stateVector.copyInto(states);
	     	form.setBillingCustStates(states);
		}
		else {
			LabelValueBean states[] = {};
	     	form.setBillingCustStates(states);
		}
		
		custCountry = CountryDAO.loadByCountryCode(siteId, form.getShippingCustCountryCode());
		stateVector = new Vector<LabelValueBean>();
		if (custCountry != null) {
			for (State state : custCountry.getStates()) {
				LabelValueBean bean = new LabelValueBean(state.getStateName(), state.getStateCode());
				stateVector.add(bean);
			}
			LabelValueBean states[] = new LabelValueBean[stateVector.size()];
			stateVector.copyInto(states);
	     	form.setShippingCustStates(states);
		}
		else {
			LabelValueBean states[] = {};
	     	form.setShippingCustStates(states);
		}
    }
    
    public void validate(String siteId, MyAccountAddressActionForm form, Long langId) throws SecurityException, Exception { 
     	if (Format.isNullOrEmpty(form.getCustFirstName())) {
    		form.addMessage("custFirstName", Languages.getLangTranValue(langId, "content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustLastName())) {
    		form.addMessage("custLastName", Languages.getLangTranValue(langId, "content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustAddressLine1())) {
    		form.addMessage("custAddressLine1", Languages.getLangTranValue(langId, "content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustCityName())) {
    		form.addMessage("custCityName", Languages.getLangTranValue(langId, "content.error.string.required"));
    	}
    	if (isStateCodeRequired(siteId, form.getCustCountryCode())) {
	    	if (Format.isNullOrEmpty(form.getCustStateCode())) {
	    		form.addMessage("custStateCode", Languages.getLangTranValue(langId, "content.error.string.required"));
	    	}
    	}
    	if (Format.isNullOrEmpty(form.getCustCountryCode())) {
    		form.addMessage("custCountryCode", Languages.getLangTranValue(langId, "content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustZipCode())) {
    		form.addMessage("custZipCode", Languages.getLangTranValue(langId, "content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustPhoneNum())) {
    		form.addMessage("custPhoneNum", Languages.getLangTranValue(langId, "content.error.string.required"));
    	}
    	
    	if (form.getBillingUseAddress().equals(Constants.CUST_ADDRESS_USE_OWN)) {
	    	if (Format.isNullOrEmpty(form.getBillingCustFirstName())) {
	    		form.addMessage("billingCustFirstName", Languages.getLangTranValue(langId, "content.error.string.required"));
	    	}
	    	if (Format.isNullOrEmpty(form.getBillingCustLastName())) {
	    		form.addMessage("billingCustLastName", Languages.getLangTranValue(langId, "content.error.string.required"));
	    	}
	    	if (Format.isNullOrEmpty(form.getBillingCustAddressLine1())) {
	    		form.addMessage("billingCustAddressLine1", Languages.getLangTranValue(langId, "content.error.string.required"));
	    	}
	    	if (Format.isNullOrEmpty(form.getBillingCustCityName())) {
	    		form.addMessage("billingCustCityName", Languages.getLangTranValue(langId, "content.error.string.required"));
	    	}
	    	if (isStateCodeRequired(siteId, form.getBillingCustCountryCode())) {
		    	if (Format.isNullOrEmpty(form.getBillingCustStateCode())) {
		    		form.addMessage("billingCustStateCode", Languages.getLangTranValue(langId, "content.error.string.required"));
		    	}
	    	}
	    	if (Format.isNullOrEmpty(form.getBillingCustCountryCode())) {
	    		form.addMessage("billingCustCountryCode", Languages.getLangTranValue(langId, "content.error.string.required"));
	    	}
	    	if (Format.isNullOrEmpty(form.getBillingCustZipCode())) {
	    		form.addMessage("billingCustZipCode", Languages.getLangTranValue(langId, "content.error.string.required"));
	    	}
	    	if (Format.isNullOrEmpty(form.getBillingCustPhoneNum())) {
	    		form.addMessage("billingCustPhoneNum", Languages.getLangTranValue(langId, "content.error.string.required"));
	    	}
    	}
    	if (form.getShippingUseAddress().equals(Constants.CUST_ADDRESS_USE_OWN)) {
        	if (Format.isNullOrEmpty(form.getShippingCustFirstName())) {
        		form.addMessage("shippingCustFirstName", Languages.getLangTranValue(langId, "content.error.string.required"));
        	}
        	if (Format.isNullOrEmpty(form.getShippingCustLastName())) {
        		form.addMessage("shippingCustLastName", Languages.getLangTranValue(langId, "content.error.string.required"));
        	}
        	if (Format.isNullOrEmpty(form.getShippingCustAddressLine1())) {
        		form.addMessage("shippingCustAddressLine1", Languages.getLangTranValue(langId, "content.error.string.required"));
        	}
        	if (Format.isNullOrEmpty(form.getShippingCustCityName())) {
        		form.addMessage("shippingCustCityName", Languages.getLangTranValue(langId, "content.error.string.required"));
        	}
	    	if (isStateCodeRequired(siteId, form.getShippingCustCountryCode())) {
		    	if (Format.isNullOrEmpty(form.getShippingCustStateCode())) {
		    		form.addMessage("shippingCustStateCode", Languages.getLangTranValue(langId, "content.error.string.required"));
		    	}
	    	}
        	if (Format.isNullOrEmpty(form.getShippingCustCountryCode())) {
        		form.addMessage("shippingCustCountryCode", Languages.getLangTranValue(langId, "content.error.string.required"));
        	}
        	if (Format.isNullOrEmpty(form.getShippingCustZipCode())) {
        		form.addMessage("shippingCustZipCode", Languages.getLangTranValue(langId, "content.error.string.required"));
        	}
        	if (Format.isNullOrEmpty(form.getShippingCustPhoneNum())) {
        		form.addMessage("shippingCustPhoneNum", Languages.getLangTranValue(langId, "content.error.string.required"));
        	}
    	}
    }
    
    protected boolean isStateCodeRequired(String siteId, String countryCode) throws SecurityException, Exception {
    	if (Format.isNullOrEmpty(countryCode)) {
    		return false;
    	}
    	
    	Country country = CountryDAO.loadByCountryCode(siteId, countryCode);
    	if (country.getStates().size() > 0) {
    		return true;
    	}
    	return false;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        map.put("update", "update");
        return map;
    }
}