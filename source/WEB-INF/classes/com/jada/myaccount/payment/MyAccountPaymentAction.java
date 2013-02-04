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

package com.jada.myaccount.payment;

import com.jada.content.ContentAction;
import com.jada.content.ContentBean;
import com.jada.dao.CustomerDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.CreditCard;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.CustomerAddress;
import com.jada.jpa.entity.CustomerCreditCard;
import com.jada.jpa.entity.Language;
import com.jada.myaccount.payment.MyAccountPaymentActionForm;
import com.jada.myaccount.portal.MyAccountPortalAction;
import com.jada.system.Languages;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;

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

public class MyAccountPaymentAction extends MyAccountPortalAction {
    Logger logger = Logger.getLogger(MyAccountPaymentAction.class);

    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	MyAccountPaymentActionForm form = (MyAccountPaymentActionForm) actionForm;
    	init(request, form);
    	ActionForward actionForward = actionMapping.findForward("success");
    	Customer customer = getCustomer(request);
		String siteId = getContentBean(request).getContentSessionKey().getSiteId();

		CustomerCreditCard custCreditCard = null;
    	customer = CustomerDAO.load(siteId, customer.getCustId());
    	Iterator<?> iterator = customer.getCustCreditCards().iterator();
    	if (iterator.hasNext()) {
    		custCreditCard = (CustomerCreditCard) iterator.next();
    	}
    	
    	if (custCreditCard != null) {
    		form.setCustCreditCardFullName(custCreditCard.getCustCreditCardFullName());
    		form.setCustCreditCardExpiryMonth(custCreditCard.getCustCreditCardExpiryMonth());
    		form.setCustCreditCardExpiryYear(custCreditCard.getCustCreditCardExpiryYear());
    		form.setCustCreditCardNum(AESEncoder.getInstance().decode(custCreditCard.getCustCreditCardNum()));
    		form.setCustCreditCardVerNum(custCreditCard.getCustCreditCardVerNum());
    		CreditCard creditCard = custCreditCard.getCreditCard();
    		if (creditCard != null) {
    			form.setCreditCardId(creditCard.getCreditCardId().toString());
    		}
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
    	
		ContentBean contentBean = getContentBean(request);
    	Language language = contentBean.getContentSessionBean().getSiteProfile().getSiteProfileClass().getLanguage();
		createEmptySecureTemplateInfo(request);
		String siteId = ContentAction.getContentBean(request).getContentSessionKey().getSiteId();
		MyAccountPaymentActionForm form = (MyAccountPaymentActionForm) actionForm;
    	init(request, form);
	
    	validate(form, language.getLangId());
    	if (form.hasMessage()) {
        	initSearchInfo(form, siteId);
            ActionForward actionForward = actionMapping.findForward("error");
            return actionForward;
    	}
    	
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Customer customer = CustomerDAO.load(siteId, getCustomer(request).getCustId());
		CustomerCreditCard customerCreditCard = null;
    	Iterator<?> iterator = customer.getCustCreditCards().iterator();
		boolean found = false;
		if (iterator.hasNext()) {
			found = true;
			customerCreditCard = (CustomerCreditCard) iterator.next();
			customerCreditCard.setRecCreateBy(Constants.USERNAME_SYSTEM);
			customerCreditCard.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		else {
			customerCreditCard = new CustomerCreditCard();
			customerCreditCard.setRecUpdateBy(Constants.USERNAME_SYSTEM);
			customerCreditCard.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			customerCreditCard.setRecCreateBy(Constants.USERNAME_SYSTEM);
			customerCreditCard.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		customerCreditCard.setCustCreditCardFullName(form.getCustCreditCardFullName());
		customerCreditCard.setCustCreditCardNum(AESEncoder.getInstance().encode(form.getCustCreditCardNum()));
		customerCreditCard.setCustCreditCardExpiryMonth(form.getCustCreditCardExpiryMonth());
		customerCreditCard.setCustCreditCardExpiryYear(form.getCustCreditCardExpiryYear());
		customerCreditCard.setCustCreditCardVerNum(form.getCustCreditCardVerNum());
		String creditCardId = form.getCreditCardId();
		CreditCard creditCard = (CreditCard) em.find(CreditCard.class, Format.getLong(creditCardId));
		customerCreditCard.setCreditCard(creditCard);
		if (customerCreditCard.getCustCreditCardId() == null) {
			em.persist(customerCreditCard);
		}
		if (!found) {
			customer.getCustCreditCards().add(customerCreditCard);
		}

    	initSearchInfo(form, siteId);
    	form.addMessage("message", Languages.getLangTranValue(language.getLangId(), "content.text.information.updated"));

        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public CustomerAddress getShippingAddress(Customer customer) {
    	Iterator<?> iterator = customer.getCustAddresses().iterator();
    	CustomerAddress address = null;
    	while (iterator.hasNext()) {
    		address = (CustomerAddress) iterator.next();
    		if (!address.getCustAddressType().equals(Constants.CUSTOMER_ADDRESS_SHIPPING)) {
    			continue;
    		}
    	}
    	return address;
    }

    public void validate(MyAccountPaymentActionForm form, Long langId) { 
    	if (Format.isNullOrEmpty(form.getCustCreditCardFullName())) {
    		form.addMessage("custCreditCardFullName", Languages.getLangTranValue(langId, "content.error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getCustCreditCardNum())) {
    		form.addMessage("custCreditCardNum", Languages.getLangTranValue(langId, "content.error.string.required"));
    	}
    }
    
    public void initSearchInfo(MyAccountPaymentActionForm form, String siteId) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String sql = "from CreditCard credit_card where siteId = :siteId order by seqNum";
     	Query query = em.createQuery(sql);
    	query.setParameter("siteId", siteId);
     	Iterator<?> iterator = query.getResultList().iterator();
     	Vector<LabelValueBean> vector = new Vector<LabelValueBean>();
     	while (iterator.hasNext()) {
     		CreditCard creditCard = (CreditCard) iterator.next();
     		LabelValueBean bean = new LabelValueBean(creditCard.getCreditCardDesc(), creditCard.getCreditCardId().toString());
     		vector.add(bean);
     	}
     	LabelValueBean creditCards[] = new LabelValueBean[vector.size()];
     	vector.copyInto(creditCards);
     	form.setCreditCards(creditCards);

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
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        map.put("update", "update");
        return map;
    }
}