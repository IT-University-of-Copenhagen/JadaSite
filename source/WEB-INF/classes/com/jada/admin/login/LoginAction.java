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

package com.jada.admin.login;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.jada.admin.AdminBean;
import com.jada.content.frontend.FrontendAction;
import com.jada.dao.SiteDAO;
import com.jada.install.process.Installer;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.User;
import com.jada.system.ApplicationGlobal;
import com.jada.util.AESEncoder;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.IdSecurity;

import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.persistence.Query;

import java.util.Date;
import java.util.List;


public class LoginAction extends Action {
	
    Logger logger = Logger.getLogger(FrontendAction.class);
    static int SIGNIN_SLEEP_TIME = 3000;
    
    public ActionForward execute(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) {
    	
    	LoginActionForm form = (LoginActionForm) actionForm;
    	if (form.getUserId() == null) {
    		String reason = (String) request.getParameter("reason");
			ActionMessages messages = new ActionMessages();
    		if (reason != null) {
    			if (reason.equals("noem")) {
	    			messages.add("error", new ActionMessage("error.login.noem"));
    			}
    			else if (reason.equals("sessionexpire")){
	    			messages.add("error", new ActionMessage("error.login.sessionexpire"));
    			}
    			else if (reason.equals("signout")) {
	    			messages.add("message", new ActionMessage("message.logout.successful"));
    			}
    		}
    		if (ApplicationGlobal.isRequireInstall()) {
    			Installer installer = Installer.getInstance();
    			if (!installer.isInstallCompleted()) {
    				messages.add("install", new ActionMessage("error.install.notCompleted"));
    			}
    		}
			saveMessages(request, messages);
            ActionForward actionForward = actionMapping.findForward("login");
            return actionForward;
    	}
    	EntityManager em = null;
    	User user = null;
    	try {
    		ActionMessages errors = new ActionMessages();
    		
    		em = JpaConnection.getInstance().getCurrentEntityManager();
    		em.getTransaction().begin();
    		
    		Query query = em.createQuery("from Site site where systemRecord = 'Y' and siteId != '_system'");
    		Site siteDefault = (Site) query.getSingleResult();
    		IdSecurity idSecurity = new IdSecurity(siteDefault, form.getUserId(), true);
    		if (idSecurity.isSuspened()) {
    			errors.add("error", new ActionMessage("content.error.login.suspended.temporary", Constants.ID_SUSPEND_TIME / 1000 / 60));
				saveMessages(request, errors);
				form.setUserPassword("");
				idSecurity.fail();
				return actionMapping.findForward("error");
    		}
	        
	        query = em.createQuery("from User user where userId = :userId");
			query.setParameter("userId", form.getUserId());
			List<?> list = query.getResultList();
			if (list.size() == 0) {
				errors.add("error", new ActionMessage("error.login.invalid"));
				saveMessages(request, errors);
				form.setUserPassword("");
				idSecurity.fail();
				em.getTransaction().commit();
				return actionMapping.findForward("error");
			}
			user = (User) list.get(0);
			if (user.getActive() != Constants.VALUE_YES) {
				errors.add("error", new ActionMessage("error.login.invalid"));
				saveMessages(request, errors);
				form.setUserPassword("");
				idSecurity.fail();
				em.getTransaction().commit();
				return actionMapping.findForward("error");
			}
			String userPassword = AESEncoder.getInstance().decode(user.getUserPassword());
			if (!userPassword.equals(form.getUserPassword())) {
				errors.add("error", new ActionMessage("error.login.invalid"));
				saveMessages(request, errors);
				form.setUserPassword("");
				idSecurity.fail();
				em.getTransaction().commit();
				return actionMapping.findForward("error");
			}
			Site site = null;
			if (!Format.isNullOrEmpty(user.getUserLastVisitSiteId())) {
				site = SiteDAO.load(user.getUserLastVisitSiteId());
			}
			if (site == null) {
				site = SiteDAO.getDefaultSite(user);
			}
			if (site == null) {
				errors.add("error", new ActionMessage("error.login.access"));
				saveMessages(request, errors);
				form.setUserPassword("");
				return actionMapping.findForward("error");
			}
			user.setUserLastVisitSiteId(site.getSiteId());

	    	HttpSession httpSession = request.getSession();
	    	AdminBean adminBean = new AdminBean();
	    	adminBean.init(user.getUserId(), site.getSiteId());
	    	httpSession.setAttribute("adminBean", adminBean);
	    	Cookie cookie = new Cookie("user", user.getUserName());
	    	cookie.setMaxAge(-1);
	    	response.addCookie(cookie);
	    	httpSession.setAttribute("userLastLoginDatetime", user.getUserLastLoginDatetime());
	    	user.setUserLastLoginDatetime(new Date());
	    	idSecurity.reset();
	    	em.getTransaction().commit();
    	}
	    catch (Throwable e) {
	        logger.error(e);
	        return actionMapping.findForward("exception");
	    }
	    finally {
	    	if (em != null && em.getTransaction().isActive()) {
	    		em.getTransaction().rollback();
	    	}
	    }
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
}