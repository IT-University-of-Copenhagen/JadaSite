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

package com.jada.content.checkout;

import org.apache.struts.action.ActionForm;

public class ShoppingCartNewUserActionForm extends ActionForm {
	private static final long serialVersionUID = 5813010971810216054L;
	String custEmail;
	String custEmail1;
	String custPassword;
	String custPassword1;
	String custPublicName;
	public String getCustEmail() {
		return custEmail;
	}
	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}
	public String getCustEmail1() {
		return custEmail1;
	}
	public void setCustEmail1(String custEmail1) {
		this.custEmail1 = custEmail1;
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
	public String getCustPublicName() {
		return custPublicName;
	}
	public void setCustPublicName(String custPublicName) {
		this.custPublicName = custPublicName;
	}
}
