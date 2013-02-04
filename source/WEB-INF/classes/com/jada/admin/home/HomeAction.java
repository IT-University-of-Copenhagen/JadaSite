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

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
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
import com.jada.statistics.Statistics;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

public class HomeAction extends AdminLookupDispatchAction {
    Logger logger = Logger.getLogger(HomeAction.class);

    public ActionForward list(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	HomeActionForm form = (HomeActionForm) actionForm;
    	AdminBean adminBean = getAdminBean(request);
    	User user = adminBean.getUser();
    	Site site = adminBean.getSite();
    	initInfo(form, request, site, user);
    	form.setUserId(user.getUserId());
    	form.setUserName(user.getUserName());
		form.setUserName(user.getUserName());
		form.setUserEmail(user.getUserEmail());
		form.setUserAddressLine1(user.getUserAddressLine1());
		form.setUserAddressLine2(user.getUserAddressLine2());
		form.setUserCityName(user.getUserCityName());
		form.setUserStateCode(user.getUserStateCode());
		form.setUserCountryCode(user.getUserCountryCode());
		form.setUserZipCode(user.getUserZipCode());
		form.setUserPhone(user.getUserPhone());
		form.setTabName("");
    	return actionMapping.findForward("success");
    }
    
    public ActionForward password(ActionMapping mapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	HomeActionForm form = (HomeActionForm) actionForm;
    	
    	AdminBean adminBean = getAdminBean(request);
    	User user = adminBean.getUser();
    	Site site = adminBean.getSite();
    	initInfo(form, request, site, user);
 	
    	ActionMessages errors = new ActionMessages();
    	if (Format.isNullOrEmpty(form.getUserPassword())) {
    		errors.add("userPassword", new ActionMessage("error.string.required"));
    	}
       	if (!form.getUserPassword().equals(form.getUserVerifyPassword())) {
    		errors.add("userPassword", new ActionMessage("error.password.nomatch"));
    	}
    	if (!Utility.isValidPassword(form.getUserPassword())) {
    		errors.add("userPassword", new ActionMessage("error.password.invalidRule"));
    	}
		if (errors.size() != 0) {
			saveMessages(request, errors);
			form.setTabName("password");
			return mapping.findForward("error");
		}
		user = (User) em.find(User.class, user.getUserId());
		user.setUserPassword(AESEncoder.getInstance().encode(form.getUserPassword()));
		em.persist(user);
		
		return mapping.findForward("success");
    }
    
    public ActionForward update(ActionMapping mapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	HomeActionForm form = (HomeActionForm) actionForm;
    	AdminBean adminBean = getAdminBean(request);
    	User user = adminBean.getUser();
    	Site site = adminBean.getSite();
    	initInfo(form, request, site, user);

    	ActionMessages errors = new ActionMessages();
    	if (Format.isNullOrEmpty(form.getUserName())) {
    		errors.add("userName", new ActionMessage("error.string.required"));
    	}
		if (errors.size() != 0) {
			saveMessages(request, errors);
			form.setTabName("profile");
			return mapping.findForward("error");
		}
		user = (User) em.find(User.class, user.getUserId());
		user.setUserName(form.getUserName());
		user.setUserEmail(form.getUserEmail());
		user.setUserPhone(form.getUserPhone());
		user.setUserAddressLine1(form.getUserAddressLine1());
		user.setUserAddressLine2(form.getUserAddressLine2());
		user.setUserCityName(form.getUserCityName());
		user.setUserStateCode(form.getUserStateCode());
		user.setUserStateName(Utility.getStateName(site.getSiteId(), form.getUserStateCode()));
		user.setUserCountryCode(form.getUserCountryCode());
		user.setUserCountryName(Utility.getCountryName(site.getSiteId(), form.getUserCountryCode()));
		user.setUserZipCode(form.getUserZipCode());
		user.setRecUpdateBy(user.getUserId());
		user.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		em.persist(user);
		adminBean.setUser(user);
    	return mapping.findForward("success");
    }
    
    public void initInfo(HomeActionForm form, HttpServletRequest request, Site site, User user) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	HttpSession httpSession = request.getSession();
    	Date userLastLoginDatetime = (Date) httpSession.getAttribute("userLastLoginDatetime");
    	if (userLastLoginDatetime != null) {
    		form.setUserLastLoginDatetime(Format.getFullDatetime(userLastLoginDatetime));
    	}
    	// Pull statistics
    	Statistics statistics = new Statistics();
    	form.setServerStats(statistics.getServerStats());
    	form.setThreadStats(statistics.getThreadStats());
    	form.setJvmStats(statistics.getJvmStats());
    	
    	form.setSiteId(site.getSiteId());
    	Vector<LabelValueBean> vector = new Vector<LabelValueBean>();
    	Iterator<?> iterator = null;
    	if (user.getUserType().equals(Constants.USERTYPE_SUPER) || user.getUserType().equals(Constants.USERTYPE_ADMIN)) {
	     	String sql = "from Site where siteId not like '\\_%' order by siteId";
	     	Query query = em.createQuery(sql);
	     	iterator = query.getResultList().iterator();
    	}
    	else {
	    	iterator = user.getSites().iterator();
    	}
    	while (iterator.hasNext()) {
    		Site userSite = (Site) iterator.next();
    		vector.add(new LabelValueBean(userSite.getSiteId(), userSite.getSiteId()));
    	}
    	LabelValueBean siteIds[] = new LabelValueBean[vector.size()];
    	vector.copyInto(siteIds);
    	form.setSiteIds(siteIds);
    	
     	String sql = "";
     	sql = "from Country country where country.siteId = :siteId order by countryName";
    	Query query = em.createQuery(sql);
    	query.setParameter("siteId", site.getSiteId());
     	iterator = query.getResultList().iterator();
     	vector = new Vector<LabelValueBean>();
     	while (iterator.hasNext()) {
     		Country country = (Country) iterator.next();
     		LabelValueBean bean = new LabelValueBean(country.getCountryName(), country.getCountryCode());
     		vector.add(bean);
     	}
     	LabelValueBean countries[] = new LabelValueBean[vector.size()];
     	vector.copyInto(countries);
     	form.setCountries(countries);

     	sql = "from		State state " +
              "left	join fetch state.country country " +
              "where	country.siteId = :siteId " +
              "order	by country.countryId, state.stateName";
     	query = em.createQuery(sql);
    	query.setParameter("siteId", site.getSiteId());
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
    }
    
    public ActionForward toggle(ActionMapping mapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	HomeActionForm form = (HomeActionForm) actionForm;
    	AdminBean adminBean = getAdminBean(request);
    	String siteId = form.getSiteId();
    	if (adminBean.getSite().getSiteId().equals(siteId)) {
        	initInfo(form, request, adminBean.getSite(), adminBean.getUser());
        	return mapping.findForward("success");
    	}
    	User user = adminBean.getUser();
    	if (!user.getUserType().equals(Constants.USERTYPE_SUPER)) {
    		Iterator<?> iterator = user.getSites().iterator();
    		boolean found = false;
    		while (iterator.hasNext()) {
    			Site site = (Site) iterator.next();
    			if (site.getSiteId().equals(form.getSiteId())) {
    				found = true;
    				break;
    			}
    		}
    		if (!found) {
    			logger.equals("Security violated - unable to switch site: userId = " + user.getUserId() + ", siteId = " + siteId);
        		throw new com.jada.admin.SecurityException("Unable to switch site: userId = " + user.getUserId() + ", siteId = " + siteId);
    		}
    	}
    	adminBean.init(user.getUserId(), siteId);    	
    	user = UserDAO.load(user.getUserId(), user);
    	user.setUserLastVisitSiteId(siteId);
    	// em.update(user);
    	initInfo(form, request, adminBean.getSite(), user);

    	return mapping.findForward("success");
    }
	
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("list", "list");
        map.put("update", "update");
        map.put("password", "password");
        map.put("toggle", "toggle");
        return map;
    }
}
