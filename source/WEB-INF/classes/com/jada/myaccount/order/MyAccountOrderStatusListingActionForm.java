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

import java.util.Vector;

import com.jada.myaccount.portal.MyAccountPortalActionForm;

public class MyAccountOrderStatusListingActionForm extends MyAccountPortalActionForm {
	private static final long serialVersionUID = -7967620082561820170L;
	String srPageNo;
    int pageCount;
    int startPage;
    int endPage;
    int pageNo;
	Vector<?> orders;
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public Vector<?> getOrders() {
		return orders;
	}
	public void setOrders(Vector<?> orders) {
		this.orders = orders;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public String getSrPageNo() {
		return srPageNo;
	}
	public void setSrPageNo(String srPageNo) {
		this.srPageNo = srPageNo;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
}
