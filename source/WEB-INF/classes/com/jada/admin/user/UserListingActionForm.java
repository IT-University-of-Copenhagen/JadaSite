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

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminListingActionForm;

public class UserListingActionForm
    extends AdminListingActionForm {
	private static final long serialVersionUID = 1340464966623574047L;
	String srUserId;
	String srUserName;
	String srUserType;
	String srActive;
    UserDisplayForm users[];
    public UserDisplayForm getUser(int index) {
    	return users[index];
    }
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		String USER = "user.*userId";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(USER)) {
				count++;
			}
		}
		users = new UserDisplayForm[count];
		for (int i = 0; i < users.length; i++) {
			users[i] = new UserDisplayForm();
		}
	}
	public String getSrActive() {
		return srActive;
	}
	public void setSrActive(String srActive) {
		this.srActive = srActive;
	}
	public String getSrUserId() {
		return srUserId;
	}
	public void setSrUserId(String srUserId) {
		this.srUserId = srUserId;
	}
	public String getSrUserName() {
		return srUserName;
	}
	public void setSrUserName(String srUserName) {
		this.srUserName = srUserName;
	}
	public String getSrUserType() {
		return srUserType;
	}
	public void setSrUserType(String srUserType) {
		this.srUserType = srUserType;
	}
	public UserDisplayForm[] getUsers() {
		return users;
	}
	public void setUsers(UserDisplayForm[] users) {
		this.users = users;
	}
}