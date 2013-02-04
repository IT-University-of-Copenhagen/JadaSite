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

package com.jada.admin.home;

import org.apache.struts.util.LabelValueBean;

import com.jada.admin.AdminMaintActionForm;

public class HomeActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = 1440487780953389510L;
	String tabName;
	String userId;
	String userName;
	String userLastLoginDatetime;
	String userPassword;
	String userVerifyPassword;
	String userAddressLine1;
	String userAddressLine2;
	String userCityName;
	String userStateName;
	String userStateCode;
	String userCountryName;
	String userCountryCode;
	String userZipCode;
	String userPhone;
	String userEmail;
	LabelValueBean states[];
	LabelValueBean countries[];
	LabelValueBean serverStats[];
	LabelValueBean threadStats[];
	LabelValueBean jvmStats[];
	String siteId;
	LabelValueBean siteIds[];
	public LabelValueBean[] getSiteIds() {
		return siteIds;
	}
	public void setSiteIds(LabelValueBean[] siteIds) {
		this.siteIds = siteIds;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public LabelValueBean[] getJvmStats() {
		return jvmStats;
	}
	public void setJvmStats(LabelValueBean[] jvmStats) {
		this.jvmStats = jvmStats;
	}
	public LabelValueBean[] getServerStats() {
		return serverStats;
	}
	public void setServerStats(LabelValueBean[] serverStats) {
		this.serverStats = serverStats;
	}
	public LabelValueBean[] getThreadStats() {
		return threadStats;
	}
	public void setThreadStats(LabelValueBean[] threadStats) {
		this.threadStats = threadStats;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserLastLoginDatetime() {
		return userLastLoginDatetime;
	}
	public void setUserLastLoginDatetime(String userLastLoginDatetime) {
		this.userLastLoginDatetime = userLastLoginDatetime;
	}
	public LabelValueBean[] getCountries() {
		return countries;
	}
	public void setCountries(LabelValueBean[] countries) {
		this.countries = countries;
	}
	public LabelValueBean[] getStates() {
		return states;
	}
	public void setStates(LabelValueBean[] states) {
		this.states = states;
	}
	public String getUserAddressLine1() {
		return userAddressLine1;
	}
	public void setUserAddressLine1(String userAddressLine1) {
		this.userAddressLine1 = userAddressLine1;
	}
	public String getUserAddressLine2() {
		return userAddressLine2;
	}
	public void setUserAddressLine2(String userAddressLine2) {
		this.userAddressLine2 = userAddressLine2;
	}
	public String getUserCityName() {
		return userCityName;
	}
	public void setUserCityName(String userCityName) {
		this.userCityName = userCityName;
	}
	public String getUserCountryCode() {
		return userCountryCode;
	}
	public void setUserCountryCode(String userCountryCode) {
		this.userCountryCode = userCountryCode;
	}
	public String getUserCountryName() {
		return userCountryName;
	}
	public void setUserCountryName(String userCountryName) {
		this.userCountryName = userCountryName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getUserStateCode() {
		return userStateCode;
	}
	public void setUserStateCode(String userStateCode) {
		this.userStateCode = userStateCode;
	}
	public String getUserStateName() {
		return userStateName;
	}
	public void setUserStateName(String userStateName) {
		this.userStateName = userStateName;
	}
	public String getUserVerifyPassword() {
		return userVerifyPassword;
	}
	public void setUserVerifyPassword(String userVerifyPassword) {
		this.userVerifyPassword = userVerifyPassword;
	}
	public String getUserZipCode() {
		return userZipCode;
	}
	public void setUserZipCode(String userZipCode) {
		this.userZipCode = userZipCode;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTabName() {
		return tabName;
	}
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}
}
