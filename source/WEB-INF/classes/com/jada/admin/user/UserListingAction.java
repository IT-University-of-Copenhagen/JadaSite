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

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.util.MessageResources;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminListingAction;
import com.jada.admin.AdminListingActionForm;
import com.jada.dao.UserDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.User;
import com.jada.util.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;


import javax.persistence.Query;

import java.util.List;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class UserListingAction
    extends AdminListingAction {
	 
    public void extract(AdminListingActionForm actionForm, HttpServletRequest request) throws Throwable {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        UserListingActionForm form = (UserListingActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
		MessageResources resources = this.getResources(request);
		
		User signinUser = getAdminBean(request).getUser();
		if (!signinUser.getUserType().equals(Constants.USERTYPE_ADMIN) && !signinUser.getUserType().equals(Constants.USERTYPE_SUPER)) {
			form.setUsers(null);
			return;
		}

        Query query = null;
        
        if (form.getSrPageNo().length() == 0) {
        	form.setSrPageNo("1");
        }
        String sql = "select user from User user where 1 = 1";
        if (form.getSrUserId().length() > 0) {
        	sql += "and userId like :userId ";
        }
        if (form.getSrUserName().length() > 0) {
        	sql += "and userName like :userName ";
        }
        if (form.getSrUserType() != null && !form.getSrUserType().equals("*")) {
        	sql += "and userType = :userType ";
        }
        if (!form.getSrActive().equals("*")) {
        	sql += "and active = :active ";
        }
        sql += "order by user_id";

        query = em.createQuery(sql);
        if (form.getSrUserId().length() > 0) {
        	query.setParameter("userId", "%" + form.getSrUserId() + "%");
        }
        if (form.getSrUserName().length() > 0) {
        	query.setParameter("userName", "%" + form.getSrUserName() + "%");
        }
        if (form.getSrUserType() != null && !form.getSrUserType().equals("*")) {
        	query.setParameter("userType", form.getSrUserType());
        }
        if (!form.getSrActive().equals("*")) {
        	query.setParameter("active", form.getSrActive());
        }
        List<?> list = query.getResultList();
        int pageNo = Integer.parseInt(form.getSrPageNo());
        calcPage(adminBean, form, list, pageNo);
        Vector<UserDisplayForm> vector = new Vector<UserDisplayForm>();
        int startRecord = (form.getPageNo() - 1) * adminBean.getListingPageSize();
        int endRecord = startRecord + adminBean.getListingPageSize();
        for (int i = startRecord; i < list.size() && i < endRecord; i++) {
        	User user = (User) list.get(i);
        	if (!UserDAO.hasAccess(signinUser, user)) {
        		continue;
        	}
        	UserDisplayForm userDisplay = new UserDisplayForm();
        	userDisplay.setUserId(user.getUserId());
        	userDisplay.setUserName(user.getUserName());
        	userDisplay.setUserEmail(user.getUserEmail());
        	userDisplay.setUserPhone(user.getUserPhone());
        	userDisplay.setUserType(resources.getMessage("user.type." + user.getUserType()));
        	userDisplay.setActive(String.valueOf(user.getActive()));
            vector.add(userDisplay);
        }
        UserDisplayForm userDisplayForm[] = new UserDisplayForm[vector.size()];
        vector.copyInto(userDisplayForm);
        form.setUsers(userDisplayForm);
    }
    
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	User signinUser = getAdminBean(request).getUser();
        UserListingActionForm form = (UserListingActionForm) actionForm;
        UserDisplayForm users[] = form.getUsers();
        
        for (int i = 0; i < users.length; i++) {
        	if (!users[i].isRemove()) {
        		continue;
        	}
            User user = new User();
            user = UserDAO.load(users[i].getUserId(), signinUser);
            em.remove(user);
        }

        ActionForward forward = new ActionForward();
        forward = new ActionForward(request.getServletPath() + "?process=list", true);
        return forward;
    }
    
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("remove", "remove");
        map.put("list", "list");
        map.put("start", "start");
        map.put("search", "search");
        map.put("back", "back");
        return map;
    }

	public void initForm(AdminListingActionForm actionForm) {
        UserListingActionForm form = (UserListingActionForm) actionForm;
        form.setSrUserType("*");
        form.setSrActive("*");
        form.setUsers(null);
	}

	public void initSearchInfo(AdminListingActionForm form, String siteId) throws Exception {
	}
}