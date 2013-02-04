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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.UserDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.State;
import com.jada.jpa.entity.User;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

public class UserMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest request,
                             HttpServletResponse response)
        throws Throwable {

        AdminBean adminBean = getAdminBean(request);
        User signinUser = adminBean.getUser();
        String siteId = adminBean.getSite().getSiteId();
        UserMaintActionForm form = (UserMaintActionForm) actionForm;
        form.setMode("C");
        form.setUserType(Constants.USERTYPE_REGULAR);
        form.setActive(String.valueOf(Constants.ACTIVE_YES));
        initSearchInfo(form, siteId, signinUser);
        
        FormUtils.setFormDisplayMode(request, form, FormUtils.CREATE_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

        AdminBean adminBean = getAdminBean(request);
        String siteId = adminBean.getSite().getSiteId();
        UserMaintActionForm form = (UserMaintActionForm) actionForm;
        if (form == null) {
            form = new UserMaintActionForm();
        }
		String userId = request.getParameter("userId");
        User user = new User();
        user = UserDAO.load(userId, adminBean.getUser());
        
        Vector<String> vector = new Vector<String>();
        Iterator<?> iterator = user.getSites().iterator();
        while (iterator.hasNext()) {
        	Site site = (Site) iterator.next();
        	vector.add(site.getSiteId());
        }
        String selectedSiteIds[] = new String[vector.size()];
        vector.copyInto(selectedSiteIds);
        form.setSelectedSiteIds(selectedSiteIds);
        
        form.setMode("U");
        copyProperties(form, user);
        initSearchInfo(form, siteId, adminBean.getUser());

        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		UserMaintActionForm form = (UserMaintActionForm) actionForm;
		User signinUser = getAdminBean(request).getUser();
        User user = UserDAO.load(form.getUserId(), signinUser);
		em.remove(user);
		ActionForward actionForward = actionMapping.findForward("removeSuccess");
		return actionForward;
	}

	public ActionForward save(ActionMapping mapping, 
							  ActionForm actionForm,
							  HttpServletRequest httpServletRequest,
							  HttpServletResponse httpServletResponse) 
		throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		boolean insertMode = false;
		UserMaintActionForm form = (UserMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}

		AdminBean adminBean = getAdminBean(httpServletRequest);
		String siteId = adminBean.getSite().getSiteId();

		User user = new User();
		if (!insertMode) {
			user = UserDAO.load(form.getUserId(), adminBean.getUser());
		}

		ActionMessages errors = validate(form, siteId);
		if (errors.size() != 0) {
			form.setUserPassword("");
			form.setVerifyPassword("");
			saveMessages(httpServletRequest, errors);
	        initSearchInfo(form, siteId, adminBean.getUser());
			return mapping.findForward("error");
		}

		if (insertMode) {
			user.setUserId(form.getUserId());
			user.setUserPassword(AESEncoder.getInstance().encode(form.getUserPassword()));
			user.setRecCreateBy(adminBean.getUser().getUserId());
			user.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		if (form.getUserPassword().length() != 0) {
			user.setUserPassword(AESEncoder.getInstance().encode(form.getUserPassword()));
		}
		user.setUserName(form.getUserName());
		user.setUserEmail(form.getUserEmail());
		user.setUserPhone(form.getUserPhone());
		user.setUserType(form.getUserType());
		user.setUserAddressLine1(form.getUserAddressLine1());
		user.setUserAddressLine2(form.getUserAddressLine2());
		user.setUserCityName(form.getUserCityName());
		user.setUserStateCode(form.getUserStateCode());
		user.setUserStateName(Utility.getStateName(siteId, form.getUserStateCode()));
		user.setUserCountryCode(form.getUserCountryCode());
		user.setUserCountryName(Utility.getCountryName(siteId, form.getUserCountryCode()));
		user.setUserZipCode(form.getUserZipCode());
		user.setActive(Constants.VALUE_NO);
		if (form.getActive() != null && form.getActive().equals("Y")) {
			user.setActive(Constants.VALUE_YES);
		}
		user.setRecUpdateBy(adminBean.getUser().getUserId());
		user.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		
		if (!user.getUserType().equals(Constants.USERTYPE_SUPER) && !user.getUserType().equals(Constants.USERTYPE_ADMIN)) {
			user.getSites().clear();
			if (form.getSelectedSiteIds() != null) {
				for (int i = 0; i < form.getSelectedSiteIds().length; i++) {
					String s = form.getSelectedSiteIds()[i];
					Site site = (Site) em.find(Site.class, s);
					user.getSites().add(site);
				}
			}
		}
		
		if (insertMode) {
			em.persist(user);
		}

		form.setMode("U");
		form.setUserPassword("");
		form.setVerifyPassword("");
        initSearchInfo(form, siteId, adminBean.getUser());
        FormUtils.setFormDisplayMode(httpServletRequest, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	private void copyProperties(UserMaintActionForm form, User user) throws Exception {
		form.setUserId(user.getUserId());
		form.setUserName(user.getUserName());
		form.setUserEmail(user.getUserEmail());
		form.setUserAddressLine1(user.getUserAddressLine1());
		form.setUserAddressLine2(user.getUserAddressLine2());
		form.setUserCityName(user.getUserCityName());
		form.setUserStateCode(user.getUserStateCode());
		form.setUserCountryCode(user.getUserCountryCode());
		form.setUserZipCode(user.getUserZipCode());
		form.setUserPhone(user.getUserPhone());
		form.setUserType(user.getUserType());
		form.setActive(String.valueOf(user.getActive()));
	
		Vector<String> vector = new Vector<String>();
		if (user.getUserType().equals(Constants.USERTYPE_SUPER) || user.getUserType().equals(Constants.USERTYPE_ADMIN)) {
	    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
	     	String sql = "from Site where siteId != '_system' order by siteId";
	     	Query query = em.createQuery(sql);
	     	Iterator<?> iterator = query.getResultList().iterator();
	     	while (iterator.hasNext()) {
	     		Site site = (Site) iterator.next();
	     		vector.add(site.getSiteId());
	     	}
		}
		else {
			Iterator<?> iterator = user.getSites().iterator();
			while (iterator.hasNext()) {
				Site site = (Site) iterator.next();
				vector.add(site.getSiteId());
			}
		}
		String selectedSiteIds[] = new String[vector.size()];
		vector.copyInto(selectedSiteIds);
		form.setSelectedSiteIds(selectedSiteIds);
	}
	
    public void initSearchInfo(UserMaintActionForm form, String siteId, User signinUser) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Query query = em.createQuery("from country in class Country where country.siteId = :siteId");
    	query.setParameter("siteId", siteId);
     	Iterator<?> iterator = query.getResultList().iterator();
     	Vector<LabelValueBean> vector = new Vector<LabelValueBean>();
     	while (iterator.hasNext()) {
     		Country country = (Country) iterator.next();
     		LabelValueBean bean = new LabelValueBean(country.getCountryName(), country.getCountryCode());
     		vector.add(bean);
     	}
     	LabelValueBean countries[] = new LabelValueBean[vector.size()];
     	vector.copyInto(countries);
     	form.setCountries(countries);

     	String sql = "";
     	sql = "from		State state " +
              "left	join fetch state.country country " +
              "where	country.siteId = :siteId " +
              "order	by country.countryId, state.stateName";
     	query = em.createQuery(sql);
    	query.setParameter("siteId", siteId);
     	iterator = query.getResultList().iterator();
     	vector = new Vector<LabelValueBean>();
     	while (iterator.hasNext()) {
     		State state = (State) iterator.next();
     		LabelValueBean bean = new LabelValueBean(state.getStateName(), state.getStateCode());
     		vector.add(bean);
     	}
     	LabelValueBean states[] = new LabelValueBean[vector.size()];
     	vector.copyInto(states);
     	form.setStates(states);
     	
     	sql = "from Site where siteId != '_system' order by siteId";
     	query = em.createQuery(sql);
     	iterator = query.getResultList().iterator();
     	Vector<String> vector1 = new Vector<String>();
     	while (iterator.hasNext()) {
     		Site site = (Site) iterator.next();
     		vector1.add(site.getSiteId());
     	}
     	String siteIds[] = new String[vector1.size()];
     	vector1.copyInto(siteIds);
     	form.setSiteIds(siteIds);
     	
 		form.setHasAdministrator(false);
 		form.setHasSuperUser(false);
 		if (signinUser.getUserType().equals(Constants.USERTYPE_SUPER)) {
     		form.setHasAdministrator(true);
     		form.setHasSuperUser(true);
     	}
 		if (signinUser.getUserType().equals(Constants.USERTYPE_ADMIN)) {
     		form.setHasAdministrator(true);
     	}
    }
    
    public ActionMessages validate(UserMaintActionForm form, String siteId) throws Exception { 
    	ActionMessages errors = new ActionMessages();
    	
    	if (Format.isNullOrEmpty(form.getUserId())) {
    		errors.add("userId", new ActionMessage("error.string.required"));
    	}
    	if (Format.isNullOrEmpty(form.getUserName())) {
    		errors.add("userName", new ActionMessage("error.string.required"));
    	}    	
		if (form.getMode().equals("C")) {
        	if (Format.isNullOrEmpty(form.getUserPassword())) {
        		errors.add("userPassword", new ActionMessage("error.string.required"));
        	}
        	
	    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
	    	Query query = em.createQuery("from 	User " +
	    									  "where	userId = :userId");
	    	query.setParameter("userId", form.getUserId());
	    	if (query.getResultList().iterator().hasNext()) {
				errors.add("userId", new ActionMessage("error.user.duplicate"));
			}
		}
    	if (form.getUserPassword().length() > 0) {
        	if (!form.getUserPassword().equals(form.getVerifyPassword())) {
        		errors.add("userPassword", new ActionMessage("error.password.nomatch"));
        	}
        	if (!Utility.isValidPassword(form.getUserPassword())) {
        		errors.add("userPassword", new ActionMessage("error.password.invalidRule"));
        	}
    	}

    	return errors;
    }
	
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("save", "save");
        map.put("edit", "edit");
        map.put("create", "create");
        map.put("remove", "remove");
        return map;
    }
}
