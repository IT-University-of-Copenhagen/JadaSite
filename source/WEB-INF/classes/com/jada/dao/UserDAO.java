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

package com.jada.dao;

import javax.persistence.EntityManager;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.User;
import com.jada.util.Constants;

public class UserDAO extends User {
	private static final long serialVersionUID = -2897427861774598948L;

	public static User load(String userId, User signinUser) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String userType = signinUser.getUserType();
    	if (!userType.equals(Constants.USERTYPE_ADMIN) && !userType.equals(Constants.USERTYPE_SUPER)) {
    		if (!signinUser.getUserId().equals(userId)) {
    			throw new SecurityException();
    		}
    	}
    	
		User user = (User) em.find(User.class, userId);
		if (!hasAccess(signinUser, user)) {
			throw new SecurityException();
		}
		return user;
	}
	
	public static boolean hasAccess(User self, User user) {
		if (getRank(self) >= getRank(user)) {
			return true;
		}
		return false;
	}
	
	public static int getRank(User user) {
		int rank = 0;
		String userType = user.getUserType();
		if (userType.equals(Constants.USERTYPE_SUPER)) {
			rank = 3;
		}
		if (userType.equals(Constants.USERTYPE_ADMIN)) {
			rank = 2;
		}
		if (userType.equals(Constants.USERTYPE_REGULAR)) {
			rank = 1;
		}
		return rank;
	}
}