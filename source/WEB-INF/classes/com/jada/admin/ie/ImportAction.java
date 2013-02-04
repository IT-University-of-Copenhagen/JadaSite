package com.jada.admin.ie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import com.jada.admin.AdminLookupDispatchAction;
import com.jada.ie.ItemSimpleCsvTransformation;
import com.jada.ie.ItemSimpleTransformation;
import com.jada.ie.ItemSimpleXmlTransformation;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.IeProfileHeader;
import com.jada.jpa.entity.Site;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;
import com.jada.xml.ie.ItemSaveException;
import com.jada.xml.ie.ItemSimple;

public class ImportAction extends AdminLookupDispatchAction {
    Logger logger = Logger.getLogger(ImportAction.class);
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	ImportActionForm form = (ImportActionForm) actionForm;
    	createAdditionalInfo(request, form);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward importFile(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();

		ImportActionForm form = (ImportActionForm) actionForm;
    	createAdditionalInfo(request, form);
        ItemSimpleTransformation transformation = null;
        if (form.getImportType().equals(Constants.IE_EXPORT_TYPE_CSV)) {
        	transformation = new ItemSimpleCsvTransformation(Long.valueOf(form.getIeProfileHeaderId()), getAdminBean(request).getSite());
        }
        else {
        	transformation = new ItemSimpleXmlTransformation();
        }
       
		ActionMessages errors = validate(form, request);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return actionMapping.findForward("error");
		}
		
		try {
			InputStream inputStream = null;
			if (form.getImportLocation().equals(Constants.IE_EXPORT_LOCATION_LOCAL)) {
				inputStream = form.getLocalFile().getInputStream();
			}
			else {
				inputStream = new FileInputStream(new File(form.getHostFileName()));
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Constants.SYSTEM_ENCODING));
			int lineNumber = 0;
			if (form.isSkipFirstLine()) {
				reader.readLine();
				lineNumber++;
			}
			while (true) {
				String input = reader.readLine();
				lineNumber++;
				if (input == null) {
					break;
				}
				System.out.println(input);
				if (Format.isNullOrEmpty(input)) {
					break;
				}
				
				try {
					String messages[] = transformation.validate(input);
					if (messages.length > 0) {
						String output = formatHtmlMessage(lineNumber, input, messages);
						form.setMessageText(output);
						return actionMapping.findForward("error");
					}
					
					ItemSimple itemSimple = transformation.fromImport(input);
					messages = itemSimple.validate();
					if (messages.length > 0) {
						String output = formatHtmlMessage(lineNumber, input, messages);
						form.setMessageText(output);
						return actionMapping.findForward("error");
					}
					
					itemSimple.save(getAdminBean(request));
				}
				catch (ItemSaveException e) {
					String output = formatHtmlMessage(lineNumber, StringEscapeUtils.escapeHtml(input), e.getMessage());
					form.setMessageText(output);
					em.getTransaction().rollback();
					return actionMapping.findForward("error");
				}
				catch (Exception e) {
					String output = formatHtmlMessage(lineNumber, StringEscapeUtils.escapeHtml(input), Utility.getHtmlStackTrace(e));
					form.setMessageText(output);
					em.getTransaction().rollback();
					return actionMapping.findForward("error");
				}
			}
		}
		catch (FileNotFoundException e) {
			errors.add("fileError", new ActionMessage("error.file.create.error"));
			saveMessages(request, errors);
			return actionMapping.findForward("error");
		}
		catch (IOException e1) {
			errors.add("fileError", new ActionMessage("error.file.write.error"));
			saveMessages(request, errors);
			logger.error(e1);
			return actionMapping.findForward("error");
		}
		
		ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public void createAdditionalInfo(HttpServletRequest request, ImportActionForm form) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        Site site = getAdminBean(request).getSite();
		String sql = "from  IeProfileHeader ieProfileHeader " +
					 "where ieProfileHeader.site.siteId = :siteId " +
					 "and   ieProfileHeader.ieProfileType = 'I' " + 
					 "order by systemRecord desc, ieProfileHeaderName";
		Query query = em.createQuery(sql);
		query.setParameter("siteId", site.getSiteId());
		Iterator<?> iterator = query.getResultList().iterator();
		Vector<LabelValueBean> vector = new Vector<LabelValueBean>();
		while (iterator.hasNext()) {
			IeProfileHeader ieProfileHeader = (IeProfileHeader) iterator.next();
			vector.add(new LabelValueBean(ieProfileHeader.getIeProfileHeaderName(), ieProfileHeader.getIeProfileHeaderId().toString()));
		}
		LabelValueBean headers[] = new LabelValueBean[vector.size()];
		vector.copyInto(headers);
		form.setIeProfileHeaderList(headers);
    }
    
    public ActionMessages validate(ImportActionForm form, HttpServletRequest request) throws Exception {
    	ActionMessages errors = new ActionMessages();
    	return errors;
    }

    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        map.put("importFile", "importFile");
        return map;
    }
    
	private String formatHtmlMessage(int lineNumber, String record, String message) {
		String output = "Error import record " + lineNumber + "<br>";
		output += record + "<br><br>";
		output += message;
		return output;
	}
	
	private String formatHtmlMessage(int lineNumber, String record, String messages[]) {
		String message = "";
		int errorCount = 0;
		for (String m : messages) {
			if (++errorCount > 3) {
				message += "...<br>";
				break;
			}
			message += m + "<br>";
		}
		return formatHtmlMessage(lineNumber, record, message);
	}
}
