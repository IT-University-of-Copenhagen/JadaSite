package com.jada.admin.ie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import com.jada.admin.AdminLookupDispatchAction;
import com.jada.ie.ItemApi;
import com.jada.ie.ItemSimpleCsvTransformation;
import com.jada.ie.ItemSimpleTransformation;
import com.jada.ie.ItemSimpleXmlTransformation;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.IeProfileHeader;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.SiteCurrencyClass;
import com.jada.jpa.entity.SiteProfileClass;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;
import com.jada.xml.ie.ItemSimple;

public class ExportAction extends AdminLookupDispatchAction {
    public ActionForward start(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		ExportActionForm form = (ExportActionForm) actionForm;
        Site site = getAdminBean(request).getSite();
        createAdditionalInfo(form, site);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward validateInput(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	ExportActionForm form = (ExportActionForm) actionForm;
        Site site = getAdminBean(request).getSite();
        createAdditionalInfo(form, site);
		ActionMessages errors = validate(form, request);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return actionMapping.findForward("error");
		}
		form.setGenerate(true);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    @SuppressWarnings("unchecked")
	public ActionForward generate(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	ExportActionForm form = (ExportActionForm) actionForm;
    	form.setGenerate(false);
        Site site = getAdminBean(request).getSite();
        createAdditionalInfo(form, site);
        ItemSimpleTransformation transformation = null;
        if (form.getExportType().equals(Constants.IE_EXPORT_TYPE_CSV)) {
        	transformation = new ItemSimpleCsvTransformation(Long.valueOf(form.getIeProfileHeaderId()), site, form.getExportLocation());
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
	    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();

	    	String sql = "from SiteProfileClass siteProfileClass where siteProfileClass.site.siteId = :siteId";
			Query query = em.createQuery(sql);
			query.setParameter("siteId", site.getSiteId());
			SiteProfileClass siteProfileClasses[] = new SiteProfileClass[query.getResultList().size()];
			query.getResultList().toArray(siteProfileClasses);
			
			sql = "from SiteCurrencyClass siteCurrencyClass where siteCurrencyClass.site.siteId = :siteId";
			query = em.createQuery(sql);
			query.setParameter("siteId", site.getSiteId());
			SiteCurrencyClass siteCurrencyClasses[] = new SiteCurrencyClass[query.getResultList().size()];
			query.getResultList().toArray(siteCurrencyClasses);
			
			OutputStream outputStream = null;
			if (form.getExportLocation().equals(Constants.IE_EXPORT_LOCATION_LOCAL)) {
				if (form.getExportType().equals(Constants.IE_EXPORT_TYPE_CSV)) {
					response.setContentType("text/csv");
					response.setHeader("Content-disposition", "attachment; filename=\"" +"jadaexport" + "_" + Format.getSortDate(new Date()) + ".csv" +"\"");
				}
				else {
					response.setContentType("text/plain");
					response.setHeader("Content-disposition", "attachment; filename=\"" +"jadaexport" + "_" + Format.getSortDate(new Date()) + ".txt" +"\"");
				}
				response.setHeader("Content-Transfer-Encoding", "binary");
				outputStream = response.getOutputStream();
			}
			else {
				outputStream = new FileOutputStream(new File(form.getHostFileName()));
			}
	
			String header = transformation.getHeader();
			if (header != null) {
				outputStream.write(header.getBytes());
			}
			
			ItemApi itemApi = new ItemApi(site);
			
			sql = "from Item item where item.site.siteId = :siteId";
			query = em.createQuery(sql);
			query.setParameter("siteId", site.getSiteId());
			Iterator<?> iterator = query.getResultList().iterator();
			boolean error = false;
			while (iterator.hasNext()) {
				Item item = (Item) iterator.next();
				for (SiteProfileClass siteProfileClass : siteProfileClasses) {
					for (SiteCurrencyClass siteCurrencyClass : siteCurrencyClasses) {
						String result = "";
						try {
							com.jada.xml.ie.Item i = itemApi.export(item);
							ItemSimple itemSimpleXml = new ItemSimple(i,
																	  siteProfileClass.getSiteProfileClassId(),
																	  siteProfileClass.getSiteProfileClassName(),
																	  siteCurrencyClass.getSiteCurrencyClassId(),
																	  siteCurrencyClass.getSiteCurrencyClassName());
							result = transformation.toExport(itemSimpleXml);
						}
						catch (Exception e) {
							result = "Unable to export item with sku code " + item.getItemSkuCd() + System.getProperty("line.separator");
							result += Utility.getStackTrace(e);
							error = true;
							outputStream.write(result.getBytes(Constants.SYSTEM_ENCODING));
							break;
						}
						outputStream.write(result.getBytes(Constants.SYSTEM_ENCODING));
					}
					if (error) {
						break;
					}
				}
				if (error) {
					break;
				}
			}
			
			String footer = transformation.getFooter();
			if (footer != null) {
				outputStream.write(footer.getBytes());
			}
			
			if (form.getExportLocation().equals(Constants.IE_EXPORT_LOCATION_SERVER)) {
				outputStream.close();
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
			return actionMapping.findForward("error");
		}
		
		if (form.getExportLocation().equals(Constants.IE_EXPORT_LOCATION_LOCAL)) {
			return null;
		}
		else {
			ActionForward actionForward = actionMapping.findForward("success");
	        return actionForward;
		}
    }
    
    public ActionMessages validate(ExportActionForm form, HttpServletRequest request) throws Exception {
    	ActionMessages errors = new ActionMessages();
		if (form.getExportLocation().equals(Constants.IE_EXPORT_LOCATION_SERVER)) {
			if (Format.isNullOrEmpty(form.getHostFileName())) {
    			errors.add("hostFileName", new ActionMessage("error.string.required"));
    		}
		}
    	return errors;
    }
    
    public void createAdditionalInfo(ExportActionForm form, Site site) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String sql = "from  IeProfileHeader ieProfileHeader " +
					 "where ieProfileHeader.site.siteId = :siteId " +
					 "and   ieProfileHeader.ieProfileType = 'E' " + 
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

    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "start");
        map.put("generate", "generate");
        map.put("validateInput", "validateInput");
        return map;
    }
}
