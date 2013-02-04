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

package com.jada.myaccount.portal;

import com.jada.content.frontend.FrontendBaseForm;

public class MyAccountPortalActionForm extends FrontendBaseForm {
	private static final long serialVersionUID = -7266354579024056663L;
	String displayFirstName;
	String displayLastName;
	boolean storeCreditCard;
	public String getDisplayFirstName() {
		return displayFirstName;
	}
	public void setDisplayFirstName(String displayFirstName) {
		this.displayFirstName = displayFirstName;
	}
	public String getDisplayLastName() {
		return displayLastName;
	}
	public void setDisplayLastName(String displayLastName) {
		this.displayLastName = displayLastName;
	}
	public boolean isStoreCreditCard() {
		return storeCreditCard;
	}
	public void setStoreCreditCard(boolean storeCreditCard) {
		this.storeCreditCard = storeCreditCard;
	}
}
