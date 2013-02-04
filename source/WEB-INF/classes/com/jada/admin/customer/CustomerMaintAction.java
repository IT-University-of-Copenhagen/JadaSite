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

package com.jada.admin. customer;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.CreditCardDAO;
import com.jada.dao.CustomerDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.CreditCard;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.CustomerAddress;
import com.jada.jpa.entity.CustomerClass;
import com.jada.jpa.entity.CustomerCreditCard;
import com.jada.jpa.entity.OrderHeader;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.State;
import com.jada.order.document.OrderEngine;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

public class CustomerMaintAction
    extends AdminLookupDispatchAction {

    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        CustomerMaintActionForm form = (CustomerMaintActionForm) actionForm;
        if (form == null) {
            form = new CustomerMaintActionForm();
        }
		String custId = request.getParameter("custId");
        Customer customer = new Customer();
        customer = CustomerDAO.load(site.getSiteId(), Format.getLong(custId));
        
        copyProperties(form, customer);
        initSearchInfo(request, form, site.getSiteId(), customer);

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
		CustomerMaintActionForm form = (CustomerMaintActionForm) actionForm;
		Site site = getAdminBean(request).getSite();
        Customer customer = CustomerDAO.load(site.getSiteId(), Format.getLong(form.getCustId()));
		em.remove(customer);
		ActionForward actionForward = actionMapping.findForward("removeSuccess");
		return actionForward;
	}

	public ActionForward save(ActionMapping mapping, 
							  ActionForm actionForm,
							  HttpServletRequest request,
							  HttpServletResponse response) 
		throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		CustomerMaintActionForm form = (CustomerMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();

		Customer customer = CustomerDAO.load(site.getSiteId(), Format.getLong(form.getCustId()));

		ActionMessages errors = validate(form, site.getSiteId());
		if (errors.size() != 0) {
			saveMessages(request, errors);
	        initSearchInfo(request, form, site.getSiteId(), customer);
			return mapping.findForward("error");
		}
		customer.setCustEmail(form.getCustEmail());
		customer.setCustPublicName(form.getCustPublicName());
		customer.setCustComments(form.getCustComments());
		customer.setActive(Constants.VALUE_NO);
		if (!Format.isNullOrEmpty(form.getActive())) {
			customer.setActive(form.getActive().equals("Y") ? Constants.VALUE_YES : Constants.VALUE_NO);
		}
		
		CustomerAddress address = customer.getCustAddress();
		if (address == null) {
			address = new CustomerAddress();
			customer.setCustAddress(address);
			customer.getCustAddresses().add(address);
	        address.setRecCreateBy(adminBean.getUserId());
	        address.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		address.setCustUseAddress(Constants.CUST_ADDRESS_USE_OWN);
		address.setCustAddressType(Constants.CUSTOMER_ADDRESS_CUST);
    	address.setCustPrefix(form.getCustPrefix());
    	address.setCustLastName(form.getCustLastName());
    	address.setCustMiddleName(form.getCustMiddleName());
    	address.setCustFirstName(form.getCustFirstName());
    	address.setCustSuffix(form.getCustSuffix());
    	address.setCustPhoneNum(form.getCustPhoneNum());
    	address.setCustFaxNum(form.getCustFaxNum());
    	address.setCustAddressLine1(form.getCustAddressLine1());
    	address.setCustAddressLine2(form.getCustAddressLine2());
    	address.setCustStateCode(form.getCustStateCode());
    	address.setCustStateName("");
    	if (Format.isNullOrEmpty(form.getCustStateCode())) {
    		address.setCustStateName(Utility.getStateName(adminBean.getSiteId(), form.getCustStateCode()));
    	}
    	address.setCustCountryCode(form.getCustCountryCode());
    	address.setCustCountryName("");
    	if (Format.isNullOrEmpty(form.getCustCountryCode())) {
    		address.setCustCountryName(Utility.getCountryName(adminBean.getSiteId(), form.getCustCountryCode()));
    	}
    	address.setCustZipCode(form.getCustZipCode());
        address.setRecUpdateBy(adminBean.getUserId());
        address.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        em.persist(address);

		address = null;
		for (CustomerAddress a : customer.getCustAddresses()) {
			if (a.getCustAddressType().equals(Constants.CUSTOMER_ADDRESS_BILLING)) {
				address = a;
			}
		}
		if (address == null) {
			address = new CustomerAddress();
			customer.getCustAddresses().add(address);
	        address.setRecCreateBy(adminBean.getUserId());
	        address.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		address.setCustUseAddress(form.getBillingCustUseAddress());
		address.setCustAddressType(Constants.CUSTOMER_ADDRESS_BILLING);
    	address.setCustPrefix(form.getBillingCustPrefix());
    	address.setCustLastName(form.getBillingCustLastName());
    	address.setCustMiddleName(form.getBillingCustMiddleName());
    	address.setCustFirstName(form.getBillingCustFirstName());
    	address.setCustSuffix(form.getBillingCustSuffix());
    	address.setCustPhoneNum(form.getBillingCustPhoneNum());
    	address.setCustFaxNum(form.getBillingCustFaxNum());
    	address.setCustAddressLine1(form.getBillingCustAddressLine1());
    	address.setCustAddressLine2(form.getBillingCustAddressLine2());
    	address.setCustStateCode(form.getBillingCustStateCode());
    	address.setCustStateName("");
    	if (!Format.isNullOrEmpty(form.getBillingCustStateCode())) {
    		address.setCustStateName(Utility.getStateName(adminBean.getSiteId(), form.getBillingCustStateCode()));
    	}
    	address.setCustCountryCode(form.getBillingCustCountryCode());
    	address.setCustCountryName("");
    	if (!Format.isNullOrEmpty(form.getBillingCustCountryCode())) {
    		address.setCustCountryName(Utility.getCountryName(adminBean.getSiteId(), form.getBillingCustCountryCode()));
    	}
    	address.setCustZipCode(form.getBillingCustZipCode());
        address.setRecUpdateBy(adminBean.getUserId());
        address.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        em.persist(address);
		
		address = null;
		for (CustomerAddress a : customer.getCustAddresses()) {
			if (a.getCustAddressType().equals(Constants.CUSTOMER_ADDRESS_SHIPPING)) {
				address = a;
			}
		}
		if (address == null) {
			address = new CustomerAddress();
			customer.getCustAddresses().add(address);
	        address.setRecCreateBy(adminBean.getUserId());
	        address.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		address.setCustUseAddress(form.getShippingCustUseAddress());
		address.setCustAddressType(Constants.CUSTOMER_ADDRESS_SHIPPING);
    	address.setCustPrefix(form.getShippingCustPrefix());
    	address.setCustLastName(form.getShippingCustLastName());
    	address.setCustMiddleName(form.getShippingCustMiddleName());
    	address.setCustFirstName(form.getShippingCustFirstName());
    	address.setCustSuffix(form.getShippingCustSuffix());
    	address.setCustPhoneNum(form.getShippingCustPhoneNum());
    	address.setCustFaxNum(form.getShippingCustFaxNum());
    	address.setCustAddressLine1(form.getShippingCustAddressLine1());
    	address.setCustAddressLine2(form.getShippingCustAddressLine2());
    	address.setCustStateCode(form.getShippingCustStateCode());
    	address.setCustStateName("");
    	if (!Format.isNullOrEmpty(form.getShippingCustStateCode())) {
    		address.setCustStateName(Utility.getStateName(adminBean.getSiteId(), form.getShippingCustStateCode()));
    	}
    	address.setCustCountryCode(form.getShippingCustCountryCode());
    	address.setCustCountryName("");
    	if (!Format.isNullOrEmpty(form.getShippingCustCountryCode())) {
    		address.setCustCountryName(Utility.getCountryName(adminBean.getSiteId(), form.getShippingCustCountryCode()));
    	}
    	address.setCustZipCode(form.getShippingCustZipCode());
        address.setRecUpdateBy(adminBean.getUserId());
        address.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        em.persist(address);

		customer.setRecUpdateBy(adminBean.getUser().getUserId());
		customer.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		if (!Format.isNullOrEmpty(form.getCustPassword())) {
			customer.setCustPassword(AESEncoder.getInstance().encode(form.getCustPassword()));
		}
		if (!Format.isNullOrEmpty(form.getCustClassId())) {
			CustomerClass customerClass = (CustomerClass) em.find(CustomerClass.class, Format.getLong(form.getCustClassId()));
			if (customerClass != null) {
				customer.setCustomerClass(customerClass);
			}
		}
		else {
			customer.setCustomerClass(null);
		}
		em.persist(customer);
		
		CustomerCreditCard customerCreditCard = null;
		Iterator<?> iterator = customer.getCustCreditCards().iterator();
		if (iterator.hasNext()) {
			customerCreditCard = (CustomerCreditCard) iterator.next();
		}
		if (customerCreditCard == null) {
			customerCreditCard = new CustomerCreditCard();
			customerCreditCard.setRecCreateBy(adminBean.getUser().getUserId());
			customerCreditCard.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		customerCreditCard.setCustCreditCardFullName(form.getCustCreditCardFullName());
		customerCreditCard.setCustCreditCardNum(AESEncoder.getInstance().encode(form.getCustCreditCardNum()));
		customerCreditCard.setCustCreditCardExpiryMonth(form.getCustCreditCardExpiryMonth());
		customerCreditCard.setCustCreditCardExpiryYear(form.getCustCreditCardExpiryYear());
		customerCreditCard.setCustCreditCardVerNum(form.getCustCreditCardVerNum());
		customerCreditCard.setRecUpdateBy(adminBean.getUser().getUserId());
		customerCreditCard.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		CreditCard creditCard = CreditCardDAO.load(site.getSiteId(), Long.valueOf(form.getCreditCardId()));
		customerCreditCard.setCreditCard(creditCard);
		customer.getCustCreditCards().add(customerCreditCard);
		em.persist(customerCreditCard);

		
        initSearchInfo(request, form, site.getSiteId(), customer);
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	private void copyProperties(CustomerMaintActionForm form, Customer customer) throws Exception {
		form.setSiteName(customer.getSiteDomain().getSiteDomainLanguage().getSiteName());
		form.setCustId(Format.getLong(customer.getCustId()));
		form.setCustEmail(customer.getCustEmail());
		form.setCustPublicName(customer.getCustPublicName());
		form.setCustSource(customer.getCustSource());
		form.setCustSourceRef(customer.getCustSourceRef());
		form.setCustComments(customer.getCustComments());
		form.setActive(String.valueOf(customer.getActive()));
		
		CustomerAddress address = customer.getCustAddress();
		form.setCustPrefix(address.getCustPrefix());
		form.setCustFirstName(address.getCustFirstName());
		form.setCustMiddleName(address.getCustMiddleName());
		form.setCustLastName(address.getCustLastName());
		form.setCustSuffix(address.getCustSuffix());
		form.setCustAddressLine1(address.getCustAddressLine1());
		form.setCustAddressLine2(address.getCustAddressLine2());
		form.setCustCityName(address.getCustCityName());
		form.setCustStateCode(address.getCustStateCode());
		form.setCustCountryCode(address.getCustCountryCode());
		form.setCustZipCode(address.getCustZipCode());
		form.setCustPhoneNum(address.getCustPhoneNum());
		form.setCustFaxNum(address.getCustFaxNum());
		
		address = null;
		for (CustomerAddress a : customer.getCustAddresses()) {
			if(a.getCustAddressType().equals(Constants.CUSTOMER_ADDRESS_BILLING)) {
				address = a;
			}
		}
		if (address != null) {
			form.setBillingCustUseAddress(address.getCustUseAddress());
			form.setBillingCustPrefix(address.getCustPrefix());
			form.setBillingCustFirstName(address.getCustFirstName());
			form.setBillingCustMiddleName(address.getCustMiddleName());
			form.setBillingCustLastName(address.getCustLastName());
			form.setBillingCustSuffix(address.getCustSuffix());
			form.setBillingCustAddressLine1(address.getCustAddressLine1());
			form.setBillingCustAddressLine2(address.getCustAddressLine2());
			form.setBillingCustCityName(address.getCustCityName());
			form.setBillingCustStateCode(address.getCustStateCode());
			form.setBillingCustCountryCode(address.getCustCountryCode());
			form.setBillingCustZipCode(address.getCustZipCode());
			form.setBillingCustPhoneNum(address.getCustPhoneNum());
			form.setBillingCustFaxNum(address.getCustFaxNum());
		}
		
		address = null;
		for (CustomerAddress a : customer.getCustAddresses()) {
			if(a.getCustAddressType().equals(Constants.CUSTOMER_ADDRESS_SHIPPING)) {
				address = a;
			}
		}
		if (address != null) {
			form.setShippingCustUseAddress(address.getCustUseAddress());
			form.setShippingCustPrefix(address.getCustPrefix());
			form.setShippingCustFirstName(address.getCustFirstName());
			form.setShippingCustMiddleName(address.getCustMiddleName());
			form.setShippingCustLastName(address.getCustLastName());
			form.setShippingCustSuffix(address.getCustSuffix());
			form.setShippingCustAddressLine1(address.getCustAddressLine1());
			form.setShippingCustAddressLine2(address.getCustAddressLine2());
			form.setShippingCustCityName(address.getCustCityName());
			form.setShippingCustStateCode(address.getCustStateCode());
			form.setShippingCustCountryCode(address.getCustCountryCode());
			form.setShippingCustZipCode(address.getCustZipCode());
			form.setShippingCustPhoneNum(address.getCustPhoneNum());
			form.setShippingCustFaxNum(address.getCustFaxNum());
		}

		CustomerCreditCard customerCreditCard = null;
		Iterator<?> iterator = customer.getCustCreditCards().iterator();
		if (iterator.hasNext()) {
			customerCreditCard = (CustomerCreditCard) iterator.next();
		}
		if (customerCreditCard != null) {
			form.setCreditCardId(String.valueOf(customerCreditCard.getCreditCard().getCreditCardId()));
			form.setCustCreditCardFullName(customerCreditCard.getCustCreditCardFullName());
			form.setCustCreditCardNum(AESEncoder.getInstance().decode(customerCreditCard.getCustCreditCardNum()));
			form.setCustCreditCardExpiryMonth(customerCreditCard.getCustCreditCardExpiryMonth());
			form.setCustCreditCardExpiryYear(customerCreditCard.getCustCreditCardExpiryYear());
			form.setCustCreditCardVerNum(customerCreditCard.getCustCreditCardVerNum());
		}
		
		CustomerClass customerClass = customer.getCustomerClass();
		form.setCustClassId("");
		if (customerClass != null) {
			form.setCustClassId(customerClass.getCustClassId().toString());
		}
	}
	
    public void initSearchInfo(HttpServletRequest request, CustomerMaintActionForm form, String siteId, Customer customer) throws Exception {
		MessageResources resources = this.getResources(request);

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

     	String sql = "select    state " +
     				 "from		State state " +
              		 "left	    join state.country country " +
              		 "where		country.siteId = :siteId " +
              		 "order		by country.countryId, state.stateName";
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
     	
     	iterator = customer.getOrderHeaders().iterator();
     	Vector<OrderDisplayForm> orderVector = new Vector<OrderDisplayForm>();
     	while (iterator.hasNext()) {
     		OrderHeader orderHeader = (OrderHeader) iterator.next();
     		OrderDisplayForm orderForm = new OrderDisplayForm();
     		OrderEngine orderEngine = new OrderEngine(orderHeader, null);
     		orderForm.setOrderHeaderId(Format.getLong(orderHeader.getOrderHeaderId()));
     		orderForm.setOrderDate(Format.getFullDatetime(orderHeader.getOrderDate()));
     		orderForm.setOrderNum(orderHeader.getOrderNum());
     		orderForm.setOrderStatusDesc(resources.getMessage("order.status." + orderHeader.getOrderStatus()));
     		orderForm.setShippingTotal(Format.getDouble(orderHeader.getShippingTotal()));
     		orderForm.setOrderTaxTotal(Format.getDouble(orderEngine.getOrderTaxTotal()));
     		orderForm.setOrderSubTotal(Format.getDouble(orderEngine.getOrderSubTotal()));
    		orderForm.setOrderTotal(Format.getDouble(orderHeader.getOrderTotal()));
    		orderVector.add(orderForm);
     	}
     	OrderDisplayForm orders[] = new OrderDisplayForm[orderVector.size()];
     	orderVector.copyInto(orders);
     	form.setOrders(orders);
     	
    	LabelValueBean expiryMonths[] = {new LabelValueBean("01", "01"),
				 new LabelValueBean("02", "02"),
				 new LabelValueBean("03", "03"),
				 new LabelValueBean("04", "04"),
				 new LabelValueBean("05", "05"),
				 new LabelValueBean("06", "06"),
				 new LabelValueBean("07", "07"),
				 new LabelValueBean("08", "08"),
				 new LabelValueBean("09", "09"),
				 new LabelValueBean("10", "10"),
				 new LabelValueBean("11", "11"),
				 new LabelValueBean("12", "12")};
		LabelValueBean expiryYears[] = {new LabelValueBean("2010", "2010"),
			    new LabelValueBean("2011", "2011"),
			    new LabelValueBean("2012", "2012"),
			    new LabelValueBean("2013", "2013"),
			    new LabelValueBean("2014", "2014"),
			    new LabelValueBean("2015", "2015"),
			    new LabelValueBean("2016", "2016"),
			    new LabelValueBean("2017", "2017"),
			    new LabelValueBean("2018", "2018"),
			    new LabelValueBean("2019", "2019"),
			    new LabelValueBean("2020", "2020"),
			    new LabelValueBean("2021", "2021"),
			    new LabelValueBean("2022", "2022"),
			    new LabelValueBean("2023", "2023"),
			    new LabelValueBean("2024", "2024"),
			    new LabelValueBean("2025", "2025"),
			    new LabelValueBean("2026", "2026"),
			    new LabelValueBean("2027", "2027"),
			    new LabelValueBean("2028", "2028"),
			    new LabelValueBean("2029", "2029"),
			    };
		form.setExpiryMonths(expiryMonths);
		form.setExpiryYears(expiryYears);
		
		sql = "select   customerClass " +
		 	  "from     CustomerClass customerClass " +
		 	  "left     join customerClass.site site " +
		 	  "where    site.siteId = :siteId " +
		 	  "order    by customerClass.custClassName ";	 
		query = em.createQuery(sql);
		query.setParameter("siteId", siteId);
		iterator = query.getResultList().iterator();
		Vector<LabelValueBean> custClassList = new Vector<LabelValueBean>();
		while (iterator.hasNext()) {
			CustomerClass customerClass = (CustomerClass) iterator.next();
			LabelValueBean bean = new LabelValueBean(customerClass.getCustClassName(), customerClass.getCustClassId().toString());;
			custClassList.add(bean);
		}
		LabelValueBean custClasses[] = new LabelValueBean[custClassList.size()];
		custClassList.copyInto(custClasses);
		form.setCustClasses(custClasses);
		
    	sql = "from CreditCard credit_card where siteId = :siteId order by seqNum";
     	query = em.createQuery(sql);
    	query.setParameter("siteId", siteId);
     	iterator = query.getResultList().iterator();
     	vector = new Vector<LabelValueBean>();
     	while (iterator.hasNext()) {
     		CreditCard creditCard = (CreditCard) iterator.next();
     		LabelValueBean bean = new LabelValueBean(creditCard.getCreditCardDesc(), creditCard.getCreditCardId().toString());
     		vector.add(bean);
     	}
     	LabelValueBean creditCards[] = new LabelValueBean[vector.size()];
     	vector.copyInto(creditCards);
     	form.setCreditCards(creditCards);
    }
    
    public ActionMessages validate(CustomerMaintActionForm form, String siteId) throws Exception { 
    	ActionMessages errors = new ActionMessages();
    	if (form.getCustPassword().length() > 0) {
        	if (!form.getCustPassword().equals(form.getCustPassword1())) {
        		errors.add("custPassword", new ActionMessage("error.password.nomatch"));
        	}
        	if (!Utility.isValidPassword(form.getCustPassword())) {
        		errors.add("custPassword", new ActionMessage("error.password.invalidRule"));
        	}
    	}
    	return errors;
    }
	
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("save", "save");
        map.put("edit", "edit");
        map.put("create", "create");
        map.put("remove", "remove");
        map.put("cancel", "cancel");
        return map;
    }
}
