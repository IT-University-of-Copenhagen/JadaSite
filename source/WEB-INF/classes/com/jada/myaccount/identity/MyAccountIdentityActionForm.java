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

package com.jada.myaccount.identity;

import com.jada.myaccount.portal.MyAccountPortalActionForm;

public class MyAccountIdentityActionForm extends MyAccountPortalActionForm {
	private static final long serialVersionUID = -3205634948955132216L;
	String custEmail;
	String custPublicName;
	String custPassword;
	String custPassword1;
	public String getCustEmail() {
		return custEmail;
	}
	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}
	public String getCustPublicName() {
		return custPublicName;
	}
	public void setCustPublicName(String custPublicName) {
		this.custPublicName = custPublicName;
	}
	public String getCustPassword() {
		return custPassword;
	}
	public void setCustPassword(String custPassword) {
		this.custPassword = custPassword;
	}
	public String getCustPassword1() {
		return custPassword1;
	}
	public void setCustPassword1(String custPassword1) {
		this.custPassword1 = custPassword1;
	}
}
