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

import java.util.Enumeration;

import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminListingActionForm;

public class CustomerListingActionForm extends AdminListingActionForm {
	private static final long serialVersionUID = -5100597844649560050L;
	String srCustFirstName;
	String srCustLastName;
	String srCustEmail;
	String srActive;
	CustomerDisplayForm customers[];
	SubSiteDisplayForm subSites[];
	
    public CustomerDisplayForm getCustomer(int index) {
    	return customers[index];
    }
    public SubSiteDisplayForm getSubSite(int index) {
    	return subSites[index];
    }
	public void reset(ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
		String CUSTDETAIL = "customer.*custId";
		String SUBSITEDETAIL = "subSite.*siteDomainId";
		int count = 0;
		int subSiteCount = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(CUSTDETAIL)) {
				count++;
			}
			if (name.matches(SUBSITEDETAIL)) {
				subSiteCount++;
			}
		}
		customers = new CustomerDisplayForm[count];
		for (int i = 0; i < customers.length; i++) {
			customers[i] = new CustomerDisplayForm();
		}
		String process = request.getParameter("process");
		if (process.equals("search")) {
			subSites = new SubSiteDisplayForm[subSiteCount];
			for (int i = 0; i < subSites.length; i++) {
				subSites[i] = new SubSiteDisplayForm();
			}
		}
	}
	public CustomerDisplayForm[] getCustomers() {
		return customers;
	}
	public void setCustomers(CustomerDisplayForm[] customers) {
		this.customers = customers;
	}
	public String getSrActive() {
		return srActive;
	}
	public void setSrActive(String srActive) {
		this.srActive = srActive;
	}
	public String getSrCustEmail() {
		return srCustEmail;
	}
	public void setSrCustEmail(String srCustEmail) {
		this.srCustEmail = srCustEmail;
	}
	public String getSrCustFirstName() {
		return srCustFirstName;
	}
	public void setSrCustFirstName(String srCustFirstName) {
		this.srCustFirstName = srCustFirstName;
	}
	public String getSrCustLastName() {
		return srCustLastName;
	}
	public void setSrCustLastName(String srCustLastName) {
		this.srCustLastName = srCustLastName;
	}
	public SubSiteDisplayForm[] getSubSites() {
		return subSites;
	}
	public void setSubSites(SubSiteDisplayForm[] subSites) {
		this.subSites = subSites;
	}
}
