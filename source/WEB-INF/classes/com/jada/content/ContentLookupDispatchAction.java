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

package com.jada.content;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import org.apache.struts.action.*;

import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.util.MessageResources;
import javax.persistence.EntityManager;

import com.jada.content.data.EmptyTemplateInfo;
import com.jada.content.template.TemplateEngine;
import com.jada.dao.SiteDomainDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Customer;
import com.jada.jpa.entity.SiteDomain;
import com.jada.order.cart.ShoppingCart;
import com.jada.system.ApplicationGlobal;
import com.jada.util.Constants;
import com.jada.util.Format;

public abstract class ContentLookupDispatchAction extends LookupDispatchAction {
    Logger logger = Logger.getLogger(ContentLookupDispatchAction.class);
    
    /*
     * Landing method for LookupDispatchAction
     */ 
    protected  ActionForward dispatchMethod(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, String name) {
    	return process(actionMapping, actionForm, request, response, name);
    }
    
    /*
     * Common processing 
     */
    protected ActionForward process(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, String name) {
        ActionForward forward = null;
        setRequestId(request, UUID.randomUUID().toString());
        ContentActionMapping mapping = (ContentActionMapping) actionMapping;
        logger.debug("URI > " + request.getRequestURI());
        logger.debug("Query string = " + request.getQueryString());
        
        if (ApplicationGlobal.isRequireInstall() && !ApplicationGlobal.isInstallCompleted()) {
        	ActionMessages messages = new ActionMessages();
        	messages.add("message", new ActionMessage("error.install.notCompleted"));
        	this.saveMessages(request, messages);
        	return actionMapping.findForward("message");
        }

        EntityManager em = null;
        try {
            em = JpaConnection.getInstance().getCurrentEntityManager();
            em.getTransaction().begin();
            ContentBean contentBean = new ContentBean();
	        contentBean.init(request);
	        setContentBean(contentBean, request);
	        ShoppingCart shoppingCart = ShoppingCart.getSessionInstance(request, false);
	        if (shoppingCart != null) {
	        	synchronized (this) {
		        	shoppingCart.setSiteCurrency(contentBean.getContentSessionBean().getSiteCurrency());
		        	shoppingCart.recalculate(contentBean);
	        	}
	        }
            
        	String enforceProtocol = mapping.getEnforceProtocol();
        	if (Format.isNullOrEmpty(enforceProtocol)) {
        		enforceProtocol = String.valueOf(Constants.VALUE_NO);
        	}
        	
        	// Enforce sign-in process when required.
        	String authentication = mapping.getAuthentication();
        	if (authentication != null && authentication.equals(String.valueOf(Constants.VALUE_YES))) {
                if (!isCustomerSession(request)) {
                	forward = new ActionForward();
                	ActionForward actionForward = actionMapping.findForward("customerLogin");
                	String path = actionForward.getPath();
                	if (path.compareTo("?") != -1) {
                		path += "&";
                	}
                	else {
                		path += "?";
                	}
                	path += "prefix=" + contentBean.getSiteDomain().getSiteDomainPrefix() + "&";
                	path += "langName=" + contentBean.getContentSessionKey().getSiteProfileClassName();
                	forward.setPath(path);
                	forward.setRedirect(actionForward.getRedirect());
        			return forward;
                }
        	}
        	
        	// Enforce the use of SSL when required.
        	SiteDomain siteDomain = contentBean.getContentSessionBean().getSiteDomain();
        	String ssl = mapping.getSsl();
        	if (Format.isNullOrEmpty(ssl)) {
        		ssl = String.valueOf(Constants.VALUE_NO);
        	}
        	if (enforceProtocol.equals(String.valueOf(Constants.VALUE_YES)) && siteDomain.getSiteSslEnabled() == Constants.VALUE_YES) {
		    	if (ssl.equals(String.valueOf(Constants.VALUE_YES))) {
		    		if (!request.isSecure()) {
		    			String url = SiteDomainDAO.getSecureURLPrefix(siteDomain) + request.getRequestURI();
		    			if (!Format.isNullOrEmpty(request.getQueryString())) {
		    				url += "?" + request.getQueryString();
		    			}
		    			response.sendRedirect(url);
		    			return null;
		    		}
		        } 
		    	else {
		    		if (request.isSecure()) {
		    			String url = SiteDomainDAO.getPublicURLPrefix(siteDomain) + request.getRequestURI();
		    			if (!Format.isNullOrEmpty(request.getQueryString())) {
		    				url += "?" + request.getQueryString();
		    			}
		    			response.sendRedirect(url);
		    			return null;
		    		}
		    	}
        	}
            forward = customProcess(actionMapping, actionForm, request, response, name);
            
            if (em.getTransaction().getRollbackOnly() == true) {
            	em.getTransaction().rollback();
            }
            else {
            	em.getTransaction().commit();
            }
        }
        catch (ContentSiteNotFoundException e) {
    		logger.error("Unable to find site definition.");
    		logger.error("ServerName = " + request.getServerName() + ", ServerPort = " + request.getServerPort());
    		logger.error("URI = " + request.getRequestURI());
        	ActionMessages errors = new ActionMessages();
        	errors.add("message", new ActionMessage("error.setup.site"));
        	saveMessages(request, errors);
        	forward = actionMapping.findForward("message");
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
        
        forward = getActionForward(forward, request);
        return forward;
    }
    
    protected ActionForward getActionForward(ActionForward forward, HttpServletRequest request) {
    	ContentBean contentBean = getContentBean(request);
    	if (forward == null) {
    		return null;
    	}
    	if (!forward.getRedirect()) {
    		return forward;
    	}
    	
    	String path = forward.getPath();
    	if (path.indexOf('?') >= 0) {
    		path += "&";
    	}
    	else {
    		path += "?";
    	}
    	path += "prefix=" + contentBean.getSiteDomain().getSiteDomainPrefix() + 
    			"&langName=" + contentBean.getContentSessionKey().getLangName(); 
    	ActionForward realForward = new ActionForward(path, forward.getRedirect());
    	return realForward;
    }
    
    /*
     * Final dispatch routine
     */
    protected ActionForward customProcess(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, String name) throws Throwable {
    	return super.dispatchMethod(actionMapping, actionForm, request, response, name);
    }
    
	static public String getCategory(HttpServletRequest request) {
		String category = request.getRequestURI();
		String prefix = "/" + ApplicationGlobal.getContextPath() + Constants.FRONTEND_URL_PREFIX;
		if (!category.matches(prefix + ".*")) {
			return null;
		}
		category = category.substring(prefix.length());
		
		// skip siteDomainPrefix
		int index = category.indexOf('/', 1);
		if (index == -1) {
			return null;
		}
		
		// skip currency
		if (category.length() < index + 1) {
			return null;
		}
		index = category.indexOf('/', index + 1);
		if (index == -1) {
			return null;
		}
		category = category.substring(index);
		
		return category;
	}
	
	static public String getCategoryName(HttpServletRequest request) {
		String category = getCategory(request);
		if (category == null) {
			return null; 
		}
		String tokens[] = category.split("/");
		return tokens[1];
	}
	
	static public String getSiteCurrencyClassName(HttpServletRequest request) {
		String url = request.getRequestURI();
		String sitePrefix = "/" + ApplicationGlobal.getContextPath() + Constants.FRONTEND_URL_PREFIX;
		if (!url.startsWith(sitePrefix)) {
			return null;
		}
		String token[] = url.split("/");
		if (!token.equals(Constants.FRONTEND_URL_ITEM)) {
			return null;
		}
		if (token.length < Constants.WEB_FRONTEND_POS_SITECURRENCYCLASSNAME) {
			return null;
		}
		return token[Constants.WEB_FRONTEND_POS_SITECURRENCYCLASSNAME];
	}
	
	static public String getSiteDomainPrefix(HttpServletRequest request) {
		String prefix = request.getParameter("prefix");
		if (prefix != null) {
			return prefix;
		}
		
		String url = request.getRequestURI();
		String sitePrefix = "/" + ApplicationGlobal.getContextPath() + Constants.FRONTEND_URL_PREFIX;
		if (url.startsWith(sitePrefix)) {
			String token[] = url.split("/");
			return token[Constants.WEB_FRONTEND_POS_PREFIX];
		}
		return null;
	}
	
	static public String getSiteProfileClassName(HttpServletRequest request) {
		String url = request.getRequestURI();
		String sitePrefix = "/" + ApplicationGlobal.getContextPath() + Constants.FRONTEND_URL_PREFIX;
		if (!url.startsWith(sitePrefix)) {
			return null;
		}
		String token[] = url.split("/");
		return token[Constants.WEB_FRONTEND_POS_SITEPROFILECLASSNAME];
	}
	
	/*
	 * Session information handling
	 */
	
    public void removeSession(HttpServletRequest request) {
    	HttpSession em = request.getSession();
    	if (em != null) {
    		em.invalidate();
    	}
    }
    
    static public ContentBean getContentBean(HttpServletRequest request) {
        return (ContentBean) request.getAttribute(Constants.SESSION_CONTENT_SESSION_CONTENTBEAN);
    }

    static public void setContentBean(ContentBean contentBean, HttpServletRequest request) {
        request.setAttribute(Constants.SESSION_CONTENT_SESSION_CONTENTBEAN, contentBean);
    }
    
    static public void setCustId(HttpServletRequest request, Long custId) throws SecurityException, Exception {
    	request.getSession(true).setAttribute(Constants.SESSION_CONTENT_SESSION_CUSTOMER, custId);
    }
    
	@SuppressWarnings("unchecked")
	static public void putContentSessionkey(Long siteDomainId, ContentSessionKey contentSessionKey, HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		Hashtable<Long, ContentSessionKey> contentSessionKeys = (Hashtable<Long, ContentSessionKey>) httpSession.getAttribute(Constants.SESSION_CONTENT_SESSION_KEYS);
		if (contentSessionKeys == null) {
			synchronized (httpSession) {
				contentSessionKeys = (Hashtable<Long, ContentSessionKey>) httpSession.getAttribute(Constants.SESSION_CONTENT_SESSION_KEYS);
				if (contentSessionKeys == null) {
					contentSessionKeys = new Hashtable<Long, ContentSessionKey>();
					httpSession.setAttribute(Constants.SESSION_CONTENT_SESSION_KEYS, contentSessionKeys);
				}
			}
		}
		
		ContentSessionKey key = (ContentSessionKey) contentSessionKeys.get(siteDomainId);
		if (key == null) {
			synchronized (httpSession) {
				key = (ContentSessionKey) contentSessionKeys.get(siteDomainId);
				if (key == null) {
					contentSessionKeys.put(siteDomainId, contentSessionKey);
				}
			}
		}
		contentSessionKeys.put(siteDomainId, contentSessionKey);
	}
	
	@SuppressWarnings("unchecked")
	static public ContentSessionKey getContentSessionKey(Long siteDomainId, HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		Hashtable<Long, ContentSessionKey> contentSessionKeys = (Hashtable<Long, ContentSessionKey>) httpSession.getAttribute(Constants.SESSION_CONTENT_SESSION_KEYS);
		if (contentSessionKeys == null) {
			return null;
		}
		return contentSessionKeys.get(siteDomainId);
	}
    
    static public String getRequestId(HttpServletRequest request) {
        return (String) request.getAttribute("requestId");
    }
    
    static public void setRequestId(HttpServletRequest request, String requestId) {
    	request.setAttribute("requestId", requestId);
    }
    
    static public Long getCustId(HttpServletRequest request) {
    	Long custId = (Long) request.getSession(true).getAttribute(Constants.SESSION_CONTENT_SESSION_CUSTOMER);
        return custId;
    }
    
    static public Customer getCustomer(HttpServletRequest request) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		Long custId = getCustId(request);
		if (custId == null) {
			return null;
		}
		
		Customer customer = (Customer) em.find(Customer.class, custId);
        return customer;
    }
    
    static public boolean isCustomerSession(HttpServletRequest request) throws Exception {
    	if (request == null) {
    		return false;
    	}
    	
        Long custId = (Long) request.getSession(true).getAttribute(Constants.SESSION_CONTENT_SESSION_CUSTOMER);
        if (custId != null) {
        	return true;
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
	static public Vector<String> getItemCompareList(HttpServletRequest request) throws Exception {
        Vector<String> itemCompareList = (Vector<String>) request.getSession(true).getAttribute(Constants.SESSION_CONTENT_SESSION_ITEMCOMPARE);
        if (itemCompareList == null) {
        	itemCompareList = new Vector<String>();
        	request.getSession().setAttribute(Constants.SESSION_CONTENT_SESSION_ITEMCOMPARE, itemCompareList);
        }
        return itemCompareList;
    }
    
    /*
     * Session information handling ends.
     */
    
    public void ignoreToken(HttpServletRequest request, boolean value) {
    	HttpSession em = request.getSession();
    	em.setAttribute(Constants.SESSION_IGNORETOKEN, Boolean.valueOf(value));
    }
    
    public boolean isIgnoreToken(HttpServletRequest request) {
       	HttpSession em = request.getSession();
       	Boolean value = (Boolean) em.getAttribute(Constants.SESSION_IGNORETOKEN);
       	if (value == null) {
       		return false;
       	}
       	return value.booleanValue();
    }
    
    protected void createEmptyTemplateInfo(HttpServletRequest request) throws IOException, Exception {
    	boolean empty = true;
    	TemplateEngine engine = TemplateEngine.getInstance();
    	engine.init(request, getServlet().getServletConfig().getServletContext(), empty);
		EmptyTemplateInfo emptyTemplateInfo =  engine.getEmptyTemplateInfo();
		request.setAttribute("emptyTemplateInfo", emptyTemplateInfo);
    }

    protected void createEmptySecureTemplateInfo(HttpServletRequest request) throws IOException, Exception {
    	boolean empty = true;
    	TemplateEngine engine = TemplateEngine.getInstance();
		engine.init(request, getServlet().getServletConfig().getServletContext(), empty);
		EmptyTemplateInfo emptyTemplateInfo =  engine.getEmptySecureTemplateInfo();
		request.setAttribute("emptyTemplateInfo", emptyTemplateInfo);
    }
    
    protected void createEmptyPrintTemplateInfo(HttpServletRequest request) throws IOException, Exception {
    	boolean empty = true;
    	TemplateEngine engine = TemplateEngine.getInstance();
    	engine.init(request, getServlet().getServletConfig().getServletContext(), empty);
		EmptyTemplateInfo emptyTemplateInfo =  engine.getEmptyPrintTemplateInfo();
		request.setAttribute("emptyTemplateInfo", emptyTemplateInfo);
    }
    
    protected String getErrorMessage(HttpServletRequest request, ActionMessages errors, String key) {
    	MessageResources resources = this.getResources(request);
    	
    	Iterator<?> iterator = errors.get(key);
    	if (iterator.hasNext()) {
    		ActionMessage error = (ActionMessage) iterator.next();
    		String value = resources.getMessage(error.getKey());
    		return value;
    	}
    	return "";
    }
    
    protected void streamWebService(HttpServletResponse response, String data) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setContentLength(data.getBytes(Constants.SYSTEM_ENCODING).length);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(data.getBytes(Constants.SYSTEM_ENCODING));
        outputStream.flush();
    }
    
	protected Map<String, String> getKeyMethodMap() {
		return null;
	}
}