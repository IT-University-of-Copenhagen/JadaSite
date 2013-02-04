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

package com.jada.admin.inventory;

import java.util.Vector;

import org.apache.struts.util.LabelValueBean;

import com.jada.admin.AdminListingActionForm;

public class OrderListingActionForm extends AdminListingActionForm {
	private static final long serialVersionUID = 6100743249236728017L;
	String srOrderNum;
	String srCustFirstName;
	String srCustLastName;
	String srCustEmail;
	String srCustCityName;
	String srCustStateCode;
	String srCustCountryCode;
	String srOrderStatus;
	String srOrderCreatedOnStart;
	String srOrderCreatedOnEnd;
	String orderAbundantLoc;
	Vector<?> orders;
	String orderHeaderIds[];
	LabelValueBean states[];
	LabelValueBean countries[];
	LabelValueBean orderStatuses[];
	LabelValueBean orderAbundantLocs[];
	public String getSrOrderNum() {
		return srOrderNum;
	}
	public void setSrOrderNum(String srOrderNum) {
		this.srOrderNum = srOrderNum;
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
	public String getSrCustEmail() {
		return srCustEmail;
	}
	public void setSrCustEmail(String srCustEmail) {
		this.srCustEmail = srCustEmail;
	}
	public String getSrCustCityName() {
		return srCustCityName;
	}
	public void setSrCustCityName(String srCustCityName) {
		this.srCustCityName = srCustCityName;
	}
	public String getSrCustStateCode() {
		return srCustStateCode;
	}
	public void setSrCustStateCode(String srCustStateCode) {
		this.srCustStateCode = srCustStateCode;
	}
	public String getSrCustCountryCode() {
		return srCustCountryCode;
	}
	public void setSrCustCountryCode(String srCustCountryCode) {
		this.srCustCountryCode = srCustCountryCode;
	}
	public String getSrOrderStatus() {
		return srOrderStatus;
	}
	public void setSrOrderStatus(String srOrderStatus) {
		this.srOrderStatus = srOrderStatus;
	}
	public String getSrOrderCreatedOnStart() {
		return srOrderCreatedOnStart;
	}
	public void setSrOrderCreatedOnStart(String srOrderCreatedOnStart) {
		this.srOrderCreatedOnStart = srOrderCreatedOnStart;
	}
	public String getSrOrderCreatedOnEnd() {
		return srOrderCreatedOnEnd;
	}
	public void setSrOrderCreatedOnEnd(String srOrderCreatedOnEnd) {
		this.srOrderCreatedOnEnd = srOrderCreatedOnEnd;
	}
	public Vector<?> getOrders() {
		return orders;
	}
	public void setOrders(Vector<?> orders) {
		this.orders = orders;
	}
	public String[] getOrderHeaderIds() {
		return orderHeaderIds;
	}
	public void setOrderHeaderIds(String[] orderHeaderIds) {
		this.orderHeaderIds = orderHeaderIds;
	}
	public LabelValueBean[] getStates() {
		return states;
	}
	public void setStates(LabelValueBean[] states) {
		this.states = states;
	}
	public LabelValueBean[] getCountries() {
		return countries;
	}
	public void setCountries(LabelValueBean[] countries) {
		this.countries = countries;
	}
	public LabelValueBean[] getOrderStatuses() {
		return orderStatuses;
	}
	public void setOrderStatuses(LabelValueBean[] orderStatuses) {
		this.orderStatuses = orderStatuses;
	}
	public String getOrderAbundantLoc() {
		return orderAbundantLoc;
	}
	public void setOrderAbundantLoc(String orderAbundantLoc) {
		this.orderAbundantLoc = orderAbundantLoc;
	}
	public LabelValueBean[] getOrderAbundantLocs() {
		return orderAbundantLocs;
	}
	public void setOrderAbundantLocs(LabelValueBean[] orderAbundantLocs) {
		this.orderAbundantLocs = orderAbundantLocs;
	}
}
