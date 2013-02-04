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

package com.jada.admin.user;

import org.apache.struts.util.LabelValueBean;

import com.jada.admin.AdminMaintActionForm;

public class UserMaintActionForm
    extends AdminMaintActionForm {
	private static final long serialVersionUID = -3878215914658417037L;
	String mode;
    String userId;
    String userName;
    String userPassword;
    String verifyPassword;
    String userEmail;
    String userPhone;
    String userType;
	String userAddressLine1;
	String userAddressLine2;
	String userCityName;
	String userStateCode;
	String userCountryCode;
	String userZipCode;
    String active;
	LabelValueBean states[];
	LabelValueBean countries[];
	String siteIds[];
	String selectedSiteIds[];
	boolean hasAdministrator;
	boolean hasSuperUser;
	public boolean isHasAdministrator() {
		return hasAdministrator;
	}
	public void setHasAdministrator(boolean hasAdministrator) {
		this.hasAdministrator = hasAdministrator;
	}
	public boolean isHasSuperUser() {
		return hasSuperUser;
	}
	public void setHasSuperUser(boolean hasSuperUser) {
		this.hasSuperUser = hasSuperUser;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public LabelValueBean[] getCountries() {
		return countries;
	}
	public void setCountries(LabelValueBean[] countries) {
		this.countries = countries;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserZipCode() {
		return userZipCode;
	}
	public void setUserZipCode(String userZipCode) {
		this.userZipCode = userZipCode;
	}
	public String getVerifyPassword() {
		return verifyPassword;
	}
	public void setVerifyPassword(String verifyPassword) {
		this.verifyPassword = verifyPassword;
	}
	public String[] getSelectedSiteIds() {
		return selectedSiteIds;
	}
	public void setSelectedSiteIds(String[] selectedSiteIds) {
		this.selectedSiteIds = selectedSiteIds;
	}
	public String[] getSiteIds() {
		return siteIds;
	}
	public void setSiteIds(String[] siteIds) {
		this.siteIds = siteIds;
	}
}