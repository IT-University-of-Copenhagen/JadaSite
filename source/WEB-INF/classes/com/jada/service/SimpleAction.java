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

package com.jada.service;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.jada.jpa.connection.JpaConnection;

public abstract class SimpleAction extends Action {
    Logger logger = Logger.getLogger(SimpleAction.class);
    
    public ActionForward execute(ActionMapping actionMapping,
            					 ActionForm actionForm,
            					 HttpServletRequest request,
            					 HttpServletResponse response) {
    	ActionForward forward = null;
        EntityManager em = null;
        try {
            em = JpaConnection.getInstance().getCurrentEntityManager();
            em.getTransaction().begin();
            process(actionMapping, actionForm, request, response);
	        em.getTransaction().commit();
	    }
	    catch (Throwable ex) {
	        logger.error("Exception encountered in " + actionMapping.getName());
	        logger.error("Exception", ex);
	        forward = actionMapping.findForward("exception");
	    }
	    finally {
	        if (em.isOpen()) {
	        	if (em.getTransaction().isActive()) {
	        		em.getTransaction().rollback();
	        	}
	        }
	        em.close();
	    }
    	return forward;
    }
    
    abstract public ActionForward process(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception;
}
