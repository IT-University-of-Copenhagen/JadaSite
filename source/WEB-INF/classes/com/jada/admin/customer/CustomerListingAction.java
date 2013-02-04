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

package com.jada.admin.customer;

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
import com.jada.dao.CustomerDAO;
import com.jada.dao.SiteDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.CustomerAddress;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomain;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

public class CustomerListingAction extends AdminListingAction {

	public void extract(AdminListingActionForm actionForm, HttpServletRequest request) throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        CustomerListingActionForm form = (CustomerListingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();
        
        Query query = null;
        String sql = "select customer " +
        			 "from   Customer customer " + 
        			 "inner  join customer.custAddresses customerAddress " +
        			 "where  customer.site.siteId = :siteId " +
        			 "and    customerAddress.custAddressType = 'C' ";
        SubSiteDisplayForm subSites[] = form.getSubSites();
    	sql += "and    customer.siteDomain.siteDomainId in (null";
    	int index = 0;
    	for (SubSiteDisplayForm subSite : subSites) {
    		if (subSite.getSelect() == null) {
    			continue;
    		}
    		sql += "," + subSite.getSiteDomainId();
    		index++;
    	}
    	sql += ")";
    
        if (form.getSrCustFirstName().length() > 0) {
        	sql += "and customerAddress.custFirstName like :custFirstName ";
        }
        if (form.getSrCustLastName().length() > 0) {
        	sql += "and customerAddress.custLastName like :custLastName ";
        }
        if (form.getSrCustEmail().length() > 0) {
        	sql += "and customer.custEmail like :custEmail ";
        }
        if (!form.getSrActive().equals("*")) {
        	sql += "and customer.active = :active ";
        }
        sql += "order by customerAddress.custLastName";

        query = em.createQuery(sql);
        query.setParameter("siteId", site.getSiteId());
        if (form.getSrCustFirstName().length() > 0) {
        	query.setParameter("custFirstName", "%" + form.getSrCustFirstName() + "%");
        }
        if (form.getSrCustLastName().length() > 0) {
        	query.setParameter("custLastName", "%" + form.getSrCustLastName() + "%");
        }
        if (form.getSrCustEmail().length() > 0) {
        	query.setParameter("custEmail", "%" + form.getSrCustEmail() + "%");
        }
        if (!form.getSrActive().equals("*")) {
        	query.setParameter("active", form.getSrActive());
        }
        List<?> list = query.getResultList();
        if (Format.isNullOrEmpty(form.getSrPageNo())) {
        	form.setSrPageNo("1");
        }
        int pageNo = Integer.parseInt(form.getSrPageNo());
        calcPage(adminBean, form, list, pageNo);
        Vector<CustomerDisplayForm> vector = new Vector<CustomerDisplayForm>();
        int startRecord = (form.getPageNo() - 1) * adminBean.getListingPageSize();
        int endRecord = startRecord + adminBean.getListingPageSize();
        for (int i = startRecord; i < list.size() && i < endRecord; i++) {
        	Customer customer = (Customer) list.get(i);
        	CustomerAddress customerAddress = null;
        	for (CustomerAddress address : customer.getCustAddresses()) {
        		if (address.getCustAddressType().equals(Constants.CUSTOMER_ADDRESS_CUST)) {
        			customerAddress = address;
        			break;
        		}
        	}
        	CustomerDisplayForm customerDisplay = new CustomerDisplayForm();
        	customerDisplay.setSiteName(customer.getSiteDomain().getSiteDomainLanguage().getSiteName());
        	customerDisplay.setCustId(Format.getLong(customer.getCustId()));
        	customerDisplay.setCustEmail(customer.getCustEmail());
        	customerDisplay.setCustFirstName(customerAddress.getCustFirstName());
        	customerDisplay.setCustLastName(customerAddress.getCustLastName());
        	customerDisplay.setActive(String.valueOf(customer.getActive()));
            vector.add(customerDisplay);
        }
        CustomerDisplayForm customers[] = new CustomerDisplayForm[vector.size()];
        vector.copyInto(customers);
        form.setCustomers(customers);
    }
	
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        CustomerListingActionForm form = (CustomerListingActionForm) actionForm;
        Site site = getAdminBean(request).getSite();

        try {
	        if (form.getCustomers() != null) {
	        	CustomerDisplayForm customers[] = form.getCustomers();
		        for (int i = 0; i < customers.length; i++) {
		        	if (customers[i].getRemove() == null) {
		        		continue;
		        	}
		        	if (!customers[i].getRemove().equals("Y")) {
		        		continue;
		        	}
		            Customer customer = new Customer();
		            customer = CustomerDAO.load(site.getSiteId(), Format.getLong(customers[i].getCustId()));
		            em.remove(customer);
		        }
		        em.getTransaction().commit();
	        }
        }
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.customers.constraint"));
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
		CustomerListingActionForm form = (CustomerListingActionForm) actionForm;
		form.setCustomers(null);
		form.setSrActive("*");
	}

	public void initSearchInfo(AdminListingActionForm actionForm, String siteId) throws Exception {
    	CustomerListingActionForm form = (CustomerListingActionForm) actionForm;
    	if (form.getSrActive() == null) {
    		form.setSrActive("*");
    	}
    	if (form.getSubSites() == null || form.getSubSites().length == 0) {
	        Vector<SubSiteDisplayForm> vector = new Vector<SubSiteDisplayForm>();
	        Site site = SiteDAO.load(siteId);
	        for (SiteDomain siteDomain : site.getSiteDomains()) {
	        	SubSiteDisplayForm bean = new SubSiteDisplayForm();
	        	bean.setSelect("Y");
	        	bean.setSiteDomainId(siteDomain.getSiteDomainId().toString());
	        	bean.setSiteName(siteDomain.getSiteDomainLanguage().getSiteName());
	        	vector.add(bean);
	        }
	        SubSiteDisplayForm subSites[] = new SubSiteDisplayForm[vector.size()];
	        vector.copyInto(subSites);
	        form.setSubSites(subSites);
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
