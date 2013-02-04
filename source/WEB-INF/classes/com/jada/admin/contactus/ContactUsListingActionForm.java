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

package com.jada.admin.contactus;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminListingActionForm;

public class ContactUsListingActionForm extends AdminListingActionForm {
	private static final long serialVersionUID = -4557553084064600562L;
	String srContactUsName;
	String srActive;
	String srPageNo;
    ContactUsDisplayForm contactUsForms[];
    public ContactUsDisplayForm getContactUs(int index) {
    	return contactUsForms[index];
    }
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		String USER = "contactUs.*contactUsId";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(USER)) {
				count++;
			}
		}
		contactUsForms = new ContactUsDisplayForm[count];
		for (int i = 0; i < contactUsForms.length; i++) {
			contactUsForms[i] = new ContactUsDisplayForm();
		}
	}
	public String getSrActive() {
		return srActive;
	}
	public void setSrActive(String srActive) {
		this.srActive = srActive;
	}
	public String getSrPageNo() {
		return srPageNo;
	}
	public void setSrPageNo(String srPageNo) {
		this.srPageNo = srPageNo;
	}
	public String getSrContactUsName() {
		return srContactUsName;
	}
	public void setSrContactUsName(String srContactUsName) {
		this.srContactUsName = srContactUsName;
	}
	public ContactUsDisplayForm[] getContactUsForms() {
		return contactUsForms;
	}
	public void setContactUsForms(ContactUsDisplayForm[] contactUsForms) {
		this.contactUsForms = contactUsForms;
	}
}