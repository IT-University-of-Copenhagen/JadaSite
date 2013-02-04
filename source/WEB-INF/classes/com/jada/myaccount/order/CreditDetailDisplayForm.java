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

package com.jada.myaccount.order;

public class CreditDetailDisplayForm {
	String creditNum;
	String creditTotal;
	String creditStatusDesc;
	String creditDatetime;
	public String getCreditNum() {
		return creditNum;
	}
	public void setCreditNum(String creditNum) {
		this.creditNum = creditNum;
	}
	public String getCreditTotal() {
		return creditTotal;
	}
	public void setCreditTotal(String creditTotal) {
		this.creditTotal = creditTotal;
	}
	public String getCreditStatusDesc() {
		return creditStatusDesc;
	}
	public void setCreditStatusDesc(String creditStatusDesc) {
		this.creditStatusDesc = creditStatusDesc;
	}
	public String getCreditDatetime() {
		return creditDatetime;
	}
	public void setCreditDatetime(String creditDatetime) {
		this.creditDatetime = creditDatetime;
	}
}
