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

package com.jada.admin;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.struts.action.*;

import javax.servlet.http.*;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.util.LabelValueBean;
import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteCurrencyClass;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.jpa.entity.User;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

public abstract class AdminLookupDispatchAction
    extends LookupDispatchAction {
    Logger logger = Logger.getLogger(AdminLookupDispatchAction.class);
    
    /*
     * Landing method for LookupDispatchAction
     */ 
    protected  ActionForward dispatchMethod(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, String name) {
    	return process(actionMapping, actionForm, request, response, name);
    }
    
    protected ActionForward process(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, String name) {
        ActionForward forward = null;
        
        AdminBean adminBean = getAdminBean(request);
        if (adminBean == null) {
        	Cookie cookies[] = request.getCookies();
        	if (cookies == null) {
        		return actionMapping.findForward("sessionexpire");
        	}
        	for (int i = 0; i < cookies.length; i++) {
        		if (cookies[i].getName().equals(Constants.SESSION_COOKIE_USER)) {
        			return actionMapping.findForward("sessionexpire");
        		}
        	}
			return actionMapping.findForward("login");
        }
        
        EntityManager em = null;
        try {
            em = JpaConnection.getInstance().getCurrentEntityManager();
            em.getTransaction().begin();

            if (actionMapping instanceof AdminActionMapping) {
            	AdminActionMapping adminMapping = (AdminActionMapping) actionMapping;
            	String userTypes = adminMapping.getUserTypes();
            	String tokens[] = userTypes.split(",");
            	User user = adminBean.getUser();
            	String userType = user.getUserType();
            	boolean found = false;
            	for (int i = 0; i < tokens.length; i++) {
            		if (userType.equals(tokens[i])) {
            			found = true;
            			break;
            		}
            	}
            	if (!found) {
            		throw new com.jada.admin.SecurityException("user " + user.getUserId() + " is blocked from accessing " + actionMapping.getPath());
            	}
            }
            
        	logger.debug("RequestURL  > " + request.getRequestURL());
        	logger.debug("QueryString > " + request.getQueryString());
        	if (Utility.getLogLevel(logger).equals(Level.DEBUG)) {
        		logger.debug("Request Information ...");
        		Enumeration<?> enumeration = request.getParameterNames();
        		while (enumeration.hasMoreElements()) {
        			String key = (String) enumeration.nextElement();
        			String line = "";
        			line += "key=" + key + " value=";
        			String values[] = request.getParameterValues(key);
        			for (int i = 0; i < values.length; i++) {
        				if (i > 0) {
        					line += ",";
        				}
        				line += "[" + values[i] + "]";
        			}
        			logger.debug(line);
        		}
        	}
        	AdminMaintActionForm form = (AdminMaintActionForm) actionForm;
            forward = customProcess(actionMapping, form, request, response, name);
            
            em = JpaConnection.getInstance().getCurrentEntityManager();
            if (em.isOpen()) {
            	if (em.getTransaction().isActive()) {
            		em.getTransaction().commit();
            	}
            }
            if (form != null) {
	            if (form.isStream) {
	            	streamWebService(response, form.getStreamData());
	            }
	            else {
	            	encodeForm(form);
	            }
            }
            
        }
        catch (Throwable e) {
            logger.error("Exception encountered in " + actionMapping.getName());
            logger.error("Exception", e);
            if (e instanceof com.jada.admin.SecurityException) {
            	forward = actionMapping.findForward("securityException");
            }
            else {
            	forward = actionMapping.findForward("exception");
            }
        }
        finally {
            try {
                em = JpaConnection.getInstance().getCurrentEntityManager();
	            if (em.isOpen()) {
	            	if (em.getTransaction().isActive()) {
	            		em.getTransaction().rollback();
	            	}
	            }
	            em.close();
            } catch (Throwable e1) {
                logger.error("Could not rollback transaction after exception!", e1);
            }
        }
        return forward;
    }
    
    /*
     * Final dispatch routine
     */
    protected ActionForward customProcess(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, String name) throws Throwable {
    	return super.dispatchMethod(actionMapping, actionForm, request, response, name);
    }
    
    static public AdminBean getAdminBean(HttpServletRequest request) {
        return (AdminBean) request.getSession().getAttribute("adminBean");
    }
    
    static public void setAdminBean(HttpServletRequest request, AdminBean adminBean) {
    	request.getSession().setAttribute("adminBean", adminBean);
    }
    
	protected Map<String, String> getKeyMethodMap() {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected void encodeForm(ActionForm form) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		Method methods[] = form.getClass().getDeclaredMethods();
		
		String encodeFields[] = {};
		try {
			Field field = form.getClass().getField("encodeFields");
			encodeFields = (String[]) field.get(form);
		}
		catch (java.lang.NoSuchFieldException e) {}
		
		for (Method m : methods) {
			if (!m.getName().startsWith("get")) {
				continue;
			}
			if (m.getParameterTypes().length > 0) {
				continue;
			}
			Class type = m.getReturnType();
			
			if (type.isArray()) {
				Object objects[] = (Object[]) m.invoke(form);
				if (objects != null) {
					for (Object object : objects) {
						if (isActionForm(object.getClass())) {
							encodeForm((ActionForm) object);
						}
					}
				}
				continue;
			}

			if (isActionForm(type)) {
				ActionForm object = (ActionForm) m.invoke(form);
				encodeForm((ActionForm) object);
				continue;
			}
			
			if (type != String.class) {
				continue;
			}
			
			String fieldname = m.getName().substring(3, 4).toLowerCase() + m.getName().substring(4);
			boolean found = false;
			for (String f : encodeFields) {
				if (f.equals(fieldname)) {
					found = true;
					break;
				}
			}
			if (!found) {
				continue;
			}
			
			String value = (String) m.invoke(form);
			value = StringEscapeUtils.escapeHtml(value);
			
			String methodName = "set" + m.getName().substring(3);
			Class[] args = new Class[] {String.class};
			Method setMethod = form.getClass().getDeclaredMethod(methodName, args);
			setMethod.invoke(form, value);
		}

	}
	
	@SuppressWarnings("unchecked")
	protected boolean isActionForm(Class c) {
		while (c != null) {
			if (c == ActionForm.class) {
				return true;
			}
			c = c.getSuperclass();
		}
		return false;
	}
	
    protected void calcPage(AdminBean adminBean, AdminListingActionForm form, List<?> list, int pageNo) throws Exception {
        form.setPageNo(pageNo);
        
        int listingPageSize = adminBean.getListingPageSize();

        /* Calc Page Count */
        int pageCount = (list.size() - list.size() % listingPageSize) / listingPageSize;
        if (list.size() % listingPageSize > 0) {
            pageCount++;
        }
        form.setPageCount(pageCount);
        
        int half = adminBean.getListingPageCount() / 2;

        /* Calc Start Page */
        int startPage = pageNo - half + 1;
        if (startPage < 1) {
        	startPage = 1;
        }
        form.setStartPage(startPage);

        /* Calc End Page */
        /* Trying to make sure the maximum number of navigation is visible */
        int endPage = startPage + adminBean.getListingPageCount() - 1;
        while (endPage > pageCount && startPage > 1) {
        	endPage--;
        	startPage--;
        }
        /* Still not possible.  Trimming navigation. */
        if (endPage > pageCount) {
            if (pageCount == 0) {
                endPage = 1;
            }
            else {
                endPage = pageCount;
            }
        }
        form.setStartPage(startPage);
        form.setEndPage(endPage);
    }
    
    protected void initSiteProfiles(AdminMaintActionForm form, Site site) throws Exception {
        EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
	
        Vector<LabelValueBean> vector = new Vector<LabelValueBean>();
    	Long siteProfileClassDefaultId = null;
        SiteProfileClass siteProfileClassDefault = site.getSiteProfileClassDefault();
        if (siteProfileClassDefault != null) {
        	siteProfileClassDefaultId = siteProfileClassDefault.getSiteProfileClassId();
        	form.setSiteProfileClassDefaultId(siteProfileClassDefaultId);
        	vector.add(new LabelValueBean(siteProfileClassDefault.getSiteProfileClassName(), 
        			siteProfileClassDefault.getSiteProfileClassId().toString()));
        }
        
    	String sql = "from   SiteProfileClass siteProfileClass " +
    				 "where  siteProfileClass.site.siteId = :siteId " +
    				 "order  by siteProfileClassName ";
    	Query query = em.createQuery(sql);
    	query.setParameter("siteId", site.getSiteId());
        Iterator<?> iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	SiteProfileClass siteProfileClass = (SiteProfileClass) iterator.next();
        	if (siteProfileClassDefault != null) {
	        	if (siteProfileClass.getSiteProfileClassId().equals(siteProfileClassDefaultId)) {
	        		continue;
	        	}
        	}
        	vector.add(new LabelValueBean(siteProfileClass.getSiteProfileClassName(), 
        								  siteProfileClass.getSiteProfileClassId().toString()));
        }
        LabelValueBean siteProfileClassBeans[] = new LabelValueBean[vector.size()];
        vector.copyInto(siteProfileClassBeans);
        form.setSiteProfileClassBeans(siteProfileClassBeans);
        
        if (form.getSiteProfileClassId() != null) {
        	boolean found = false;
        	for (LabelValueBean bean : siteProfileClassBeans) {
        		if (bean.getValue().equals(form.getSiteProfileClassId().toString())) {
        			found = true;
        			break;
        		}
        	}
        	if (!found) {
        		form.setSiteProfileClassId(null);
        	}
        }
        
        if (form.getSiteProfileClassId() == null) {
        	if (siteProfileClassDefault != null) {
        		form.setSiteProfileClassId(siteProfileClassDefaultId);
        	}
        	form.setSiteProfileClassDefault(true);
        }
        else {
        	if (siteProfileClassDefault == null || form.getSiteProfileClassId().equals(siteProfileClassDefaultId)) {
        		form.setSiteProfileClassDefault(true);
        	}
        	else {
            	form.setSiteProfileClassDefault(false);
        	}
        	form.setTranslationEnable(false);
        	String fromLocale = "";
        	String toLocale = "";
        	fromLocale = siteProfileClassDefault.getLanguage().getGoogleTranslateLocale();
        	SiteProfileClass siteProfileClass = (SiteProfileClass) em.find(SiteProfileClass.class, form.getSiteProfileClassId());
        	toLocale = siteProfileClass.getLanguage().getGoogleTranslateLocale();
        	if (!Format.isNullOrEmpty(fromLocale) && !Format.isNullOrEmpty(toLocale) && !fromLocale.equals(toLocale)) {
        		form.setTranslationEnable(true);
        		form.setFromLocale(fromLocale);
        		form.setToLocale(toLocale);
        	}
        }
        
        vector = new Vector<LabelValueBean>();
    	Long siteCurrencyClassDefaultId = null;
        SiteCurrencyClass siteCurrencyClassDefault = site.getSiteCurrencyClassDefault();
        if (siteCurrencyClassDefault != null) {
        	siteCurrencyClassDefaultId = siteCurrencyClassDefault.getSiteCurrencyClassId();
        	form.setSiteCurrencyClassDefaultId(siteCurrencyClassDefaultId);
        	vector.add(new LabelValueBean(siteCurrencyClassDefault.getSiteCurrencyClassName(), 
        			siteCurrencyClassDefault.getSiteCurrencyClassId().toString()));
        }
        
    	sql = "from   SiteCurrencyClass siteCurrencyClass " +
    		  "where  siteCurrencyClass.site.siteId = :siteId " +
    		  "order  by siteCurrencyClassName ";
    	query = em.createQuery(sql);
    	query.setParameter("siteId", site.getSiteId());
        iterator = query.getResultList().iterator();
        while (iterator.hasNext()) {
        	SiteCurrencyClass siteCurrencyClass = (SiteCurrencyClass) iterator.next();
        	if (siteProfileClassDefault != null) {
	        	if (siteCurrencyClass.getSiteCurrencyClassId().equals(siteCurrencyClassDefaultId)) {
	        		continue;
	        	}
        	}
        	vector.add(new LabelValueBean(siteCurrencyClass.getSiteCurrencyClassName(), 
        			siteCurrencyClass.getSiteCurrencyClassId().toString()));
        }
        LabelValueBean siteCurrencyClassBeans[] = new LabelValueBean[vector.size()];
        vector.copyInto(siteCurrencyClassBeans);
        form.setSiteCurrencyClassBeans(siteCurrencyClassBeans);
        
        if (form.getSiteCurrencyClassId() == null) {
        	if (siteCurrencyClassDefault != null) {
        		form.setSiteCurrencyClassId(siteCurrencyClassDefaultId);
        	}
        	form.setSiteCurrencyClassDefault(true);
        }
        else {
        	if (siteCurrencyClassDefault == null || form.getSiteCurrencyClassId().equals(siteCurrencyClassDefaultId)) {
        		form.setSiteCurrencyClassDefault(true);
        	}
        	else {
            	form.setSiteCurrencyClassDefault(false);
        	}
        }
    }
    
    /*
     * This method should not be called directly from the action class.
     * Should be calling setStream() and setStreamData()
     */
    protected void streamWebService(HttpServletResponse response, String data) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setContentLength(data.getBytes(Constants.SYSTEM_ENCODING).length);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(data.getBytes(Constants.SYSTEM_ENCODING));
        outputStream.flush();
    }
    
    protected void commitAndRestartTransaction() throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		em.getTransaction().commit();
        em = JpaConnection.getInstance().getCurrentEntityManager();
		em.getTransaction().begin();
    }
}
