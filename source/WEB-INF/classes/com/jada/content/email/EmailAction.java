package com.jada.content.email;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.jada.content.ContentAction;
import com.jada.content.ContentBean;
import com.jada.content.ContentSessionBean;
import com.jada.dao.SiteDomainDAO;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteDomainLanguage;
import com.jada.util.Mailer;
import com.jada.xml.site.SiteDomainParamBean;

public class EmailAction extends ContentAction {
    Logger logger = Logger.getLogger(EmailAction.class);

    public ActionForward performAction(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	Site site = getContentBean(request).getContentSessionBean().getSiteDomain().getSite();
    	Enumeration<?> enumeration = request.getParameterNames();
    	String email = request.getParameter("contactUsEmail");
    	String content = "IP address: " + request.getRemoteAddr() + "\n";
    	while (enumeration.hasMoreElements()) {
    		String name = (String) enumeration.nextElement();
    		if (name.equals("contactUsEmail")) {
    			continue;
    		}
    		if (name.equals("prefix")) {
    			continue;
    		}
    		if (name.equals("langName")) {
    			continue;
    		}
    		if (name.equals("process")) {
    			continue;
    		}

    		String value = (String) request.getParameter(name);
    		content += name + ": " + value + "\n";
    	}
    	
    	MessageResources resources = this.getResources(request);
        Mailer mailer = new Mailer(site);
        ContentBean contentBean = getContentBean(request);
    	SiteDomainLanguage siteDomainLanguage = null;
    	for (SiteDomainLanguage language : contentBean.getContentSessionBean().getSiteDomain().getSiteDomainLanguages()) {
    		if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentBean.getContentSessionKey().getSiteProfileClassId())) {
    			siteDomainLanguage = language;
    			break;
    		}
    	}
    	SiteDomainParamBean siteDomainParamBean = SiteDomainDAO.getSiteDomainParamBean(contentBean.getContentSessionBean().getSiteDomain().getSiteDomainLanguage(), siteDomainLanguage);
        String mailFrom = siteDomainParamBean.getMailFromContactUs();
		ContentSessionBean contentSessionBean = contentBean.getContentSessionBean();
        if (email == null) {
        	logger.error("email address value is null and email may not be sent");
            ActionForward forward = actionMapping.findForward("error") ;
            forward = new ActionForward(forward.getPath() + 
    									contentSessionBean.getSiteDomain().getSiteDomainPrefix() + "/" +
            							contentSessionBean.getSiteProfile().getSiteProfileClass().getSiteProfileClassName() + "/" +
            							"contactus?message=content.text.contactUs.error", 
            							forward.getRedirect());
            return forward;
        }
        String subject = resources.getMessage("content.text.contactUs");
logger.error("mailFrom > " + mailFrom);
logger.error("email > " + email);
logger.error("subject > " + subject);
logger.error("content > " + content);
        mailer.sendMail(mailFrom, email, subject, content);
        
        ActionForward forward = actionMapping.findForward("success") ;
        forward = new ActionForward(forward.getPath() + 
									contentSessionBean.getSiteDomain().getSiteDomainPrefix() + "/" +
        							contentSessionBean.getSiteProfile().getSiteProfileClass().getSiteProfileClassName() + "/" +
        							"contactus?message=content.text.contactUs.success", 
        							forward.getRedirect());
        return forward;
    }
}
