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

package com.jada.admin.customAttribute;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import com.jada.util.JSONEscapeObject;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.CustomAttributeGroupDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.CustomAttribute;
import com.jada.jpa.entity.CustomAttributeDetail;
import com.jada.jpa.entity.CustomAttributeGroup;
import com.jada.jpa.entity.Site;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.util.Utility;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

public class CustomAttributeGroupMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest request,
                             HttpServletResponse response)
        throws Throwable {

        CustomAttributeGroupMaintActionForm form = (CustomAttributeGroupMaintActionForm) actionForm;
		Site site = getAdminBean(request).getSite();
		initSiteProfiles(form, site);
        CustomAttributeGroup customAttributeGroup = new CustomAttributeGroup();
        PropertyUtils.copyProperties(form, customAttributeGroup);
        form.setMode("C");
        
        FormUtils.setFormDisplayMode(request, form, FormUtils.CREATE_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

    	Site site = getAdminBean(request).getSite();
        CustomAttributeGroupMaintActionForm form = (CustomAttributeGroupMaintActionForm) actionForm;
		initSiteProfiles(form, site);
		String customAttribGroupId = request.getParameter("customAttribGroupId");
        CustomAttributeGroup customAttributeGroup = new CustomAttributeGroup();
        customAttributeGroup = CustomAttributeGroupDAO.load(site.getSiteId(), Format.getLong(customAttribGroupId));
        form.setMode("U");
        copyProperties(form, customAttributeGroup);
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
		
    	Site site = getAdminBean(request).getSite();
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		CustomAttributeGroupMaintActionForm form = (CustomAttributeGroupMaintActionForm) actionForm;
		try {
			CustomAttributeGroup customAttributeGroup = CustomAttributeGroupDAO.load(site.getSiteId(), Format.getLong(form.getCustomAttribGroupId()));
			em.remove(customAttributeGroup);
			em.getTransaction().commit();
		}
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.customAttributeGroup.constraint"));
				saveMessages(request, errors);
				return actionMapping.findForward("error");
			}
			throw e;
		}
		
		ActionForward actionForward = actionMapping.findForward("removeSuccess");
		return actionForward;
	}

	public ActionForward save(ActionMapping mapping, 
							  ActionForm actionForm,
							  HttpServletRequest request,
							  HttpServletResponse response) 
		throws Throwable {

    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		boolean insertMode = false;
		CustomAttributeGroupMaintActionForm form = (CustomAttributeGroupMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}

		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		initSiteProfiles(form, site);
		CustomAttributeGroup customAttributeGroup = new CustomAttributeGroup();
		if (!insertMode) {
			customAttributeGroup = CustomAttributeGroupDAO.load(site.getSiteId(), Format.getLong(form.getCustomAttribGroupId()));
		}

		ActionMessages errors = validate(form, site.getSiteId());
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return mapping.findForward("error");
		}

		if (insertMode) {
			customAttributeGroup.setRecCreateBy(adminBean.getUser().getUserId());
			customAttributeGroup.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		customAttributeGroup.setCustomAttribGroupName(form.getCustomAttribGroupName());
		customAttributeGroup.setSite(site);
		customAttributeGroup.setRecUpdateBy(adminBean.getUser().getUserId());
		customAttributeGroup.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		
		if (insertMode) {
			em.persist(customAttributeGroup);
		}
		else {
			// em.update(customAttributeGroup);
		}
        form.setMode("U");
        form.setCustomAttribGroupId(customAttributeGroup.getCustomAttribGroupId().toString());
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	private String getJSONCustomAttributeDetails(CustomAttributeGroup customAttributeGroup) throws Exception {
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		jsonResult.put("customAttributeGroupId", customAttributeGroup.getCustomAttribGroupId());
		jsonResult.put("customAttributeGroupName", customAttributeGroup.getCustomAttribGroupName());
		
		Vector<JSONEscapeObject> vector = new Vector<JSONEscapeObject>();
		Iterator<?> iterator = customAttributeGroup.getCustomAttributeDetails().iterator();
		while (iterator.hasNext()) {
			CustomAttributeDetail customAttributeDetail = (CustomAttributeDetail) iterator.next();
			JSONEscapeObject detail = new JSONEscapeObject();
			detail.put("customAttribDetailId", customAttributeDetail.getCustomAttribDetailId());
			detail.put("seqNum", customAttributeDetail.getSeqNum().toString());
			detail.put("customAttribName", customAttributeDetail.getCustomAttribute().getCustomAttribName());
			vector.add(detail);
		}
		jsonResult.put("customAttributeDetails", vector);
		return jsonResult.toHtmlString();
	}
	
	public ActionForward getCustomAttributeDetails(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Throwable {
		CustomAttributeGroupMaintActionForm form = (CustomAttributeGroupMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		Long customAttribGroupId = Format.getLong(form.getCustomAttribGroupId());
		CustomAttributeGroup customAttributeGroup = CustomAttributeGroupDAO.load(site.getSiteId(), customAttribGroupId);
		streamWebService(response, getJSONCustomAttributeDetails(customAttributeGroup));
		return null;
	}
	
	public ActionForward removeCustomAttributeDetails(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Throwable {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		CustomAttributeGroupMaintActionForm form = (CustomAttributeGroupMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);

		Long customAttribGroupId = Format.getLong(form.getCustomAttribGroupId());
		CustomAttributeGroup customAttributeGroup = CustomAttributeGroupDAO.load(site.getSiteId(), customAttribGroupId);
		String customAttribDetailIds[] = form.getCustomAttribDetailIds();
		if (customAttribDetailIds != null) {
			for (int i = 0; i < customAttribDetailIds.length; i++) {
				CustomAttributeDetail customAttributeDetail = (CustomAttributeDetail) em.find(CustomAttributeDetail.class, Format.getLong(customAttribDetailIds[i]));
				if (customAttributeDetail.getCustomAttribute().getCustomAttribTypeCode() == Constants.CUSTOM_ATTRIBUTE_TYPE_SKU_MAKEUP) {
					String sql = "select  count(*) " +
								 "from	  ItemAttributeDetail itemAttributeDetail " +
								 "where	  itemAttributeDetail.customAttributeDetail = :customAttributeDetail " +
								 "and     itemAttributeDetail.item.itemTypeCd = :itemTypeCd";
					Query query = em.createQuery(sql);
					query.setParameter("customAttributeDetail", customAttributeDetail);
					query.setParameter("itemTypeCd", Constants.ITEM_TYPE_SKU);
					Long count = (Long) query.getSingleResult();
					if (count.intValue() > 0) {	
						jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
						jsonResult.put("reason", Constants.WEBSERVICE_REASON_INUSE);
						streamWebService(response, jsonResult.toHtmlString());
						return null;
					}
				}

				String sql = "delete " +
							 "from   ItemAttributeDetail itemAttributeDetail " +
							 "where  itemAttributeDetail.customAttributeDetail = :customAttributeDetail ";
				Query query = em.createQuery(sql);
				query.setParameter("customAttributeDetail", customAttributeDetail);
				query.executeUpdate();

				customAttributeGroup.getCustomAttributeDetails().remove(customAttributeDetail);
				em.remove(customAttributeDetail);
			}
		}
		streamWebService(response, jsonResult.toHtmlString());
		return null;
	}
	
	public ActionForward addCustomAttributeDetail(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Throwable {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		CustomAttributeGroupMaintActionForm form = (CustomAttributeGroupMaintActionForm) actionForm;
		AdminBean adminBean = getAdminBean(request);
		
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		
		Long customAttribId = Format.getLong(form.getCustomAttribId());
		CustomAttribute customAttribute = (CustomAttribute) em.find(CustomAttribute.class, customAttribId);
		Long customAttribGroupId = Format.getLong(form.getCustomAttribGroupId());
		CustomAttributeGroup customAttributeGroup = (CustomAttributeGroup) em.find(CustomAttributeGroup.class, customAttribGroupId);
		int seqNum = 0;
		Iterator<?> iterator = customAttributeGroup.getCustomAttributeDetails().iterator();
		boolean found = false;
		while (iterator.hasNext()) {
			CustomAttributeDetail customAttributeDetail = (CustomAttributeDetail) iterator.next();
			if (customAttributeDetail.getCustomAttribute().getCustomAttribId().equals(customAttribute.getCustomAttribId())) {
				found = true;
			}
			if (customAttributeDetail.getSeqNum().intValue() >= seqNum) {
				seqNum = customAttributeDetail.getSeqNum() + 1;
			}
		}
		
		if (!found) {
			CustomAttributeDetail customAttributeDetail = new CustomAttributeDetail();
			customAttributeDetail.setCustomAttribute(customAttribute);
			customAttributeDetail.setSeqNum(seqNum);
			customAttributeDetail.setRecUpdateBy(adminBean.getUser().getUserId());
			customAttributeDetail.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
			customAttributeDetail.setRecCreateBy(adminBean.getUser().getUserId());
			customAttributeDetail.setRecCreateDatetime(new Date(System.currentTimeMillis()));
			customAttributeGroup.getCustomAttributeDetails().add(customAttributeDetail);
			em.persist(customAttributeDetail);
		}

		streamWebService(response, jsonResult.toHtmlString());
		return null;
	}
	
	public ActionForward resequence(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Throwable {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		CustomAttributeGroupMaintActionForm form = (CustomAttributeGroupMaintActionForm) actionForm;
		
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		
		MessageResources resources = this.getResources(request);
		Vector<JSONEscapeObject> errors = new Vector<JSONEscapeObject>();
		String seqNums[] = form.getSeqNums();
		if (seqNums != null) {
			String customAttribDetailIds[] = form.getCustomAttribDetailIds();
			for (int i = 0; i < seqNums.length; i++) {
				if (!Format.isInt(seqNums[i])) {
					JSONEscapeObject object = new JSONEscapeObject();
					object.put("customAttribDetailId", customAttribDetailIds[i]);
					object.put("error", resources.getMessage("error.int.invalid"));
					errors.add(object);
				}
			}
			if (errors.size() > 0) {
				jsonResult.put("validations", errors);
				streamWebService(response, jsonResult.toHtmlString());
				return null;
			}
			
			for (int i = 0; i < customAttribDetailIds.length; i++) {
				CustomAttributeDetail customAttributeDetail = (CustomAttributeDetail) em.find(CustomAttributeDetail.class, Format.getLong(customAttribDetailIds[i]));
				customAttributeDetail.setSeqNum(Format.getInt(seqNums[i]));
				em.persist(customAttributeDetail);
			}
		}

		streamWebService(response, jsonResult.toHtmlString());
		return null;
	}
	
	private void copyProperties(CustomAttributeGroupMaintActionForm form, CustomAttributeGroup customAttributeGroup) {
		form.setCustomAttribGroupId(Format.getLong(customAttributeGroup.getCustomAttribGroupId()));
		form.setCustomAttribGroupName(customAttributeGroup.getCustomAttribGroupName());
	}

    public ActionMessages validate(CustomAttributeGroupMaintActionForm form, String siteId) throws Exception { 
    	ActionMessages errors = new ActionMessages();
    	
    	boolean insertMode = false;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}
    	
    	if (Format.isNullOrEmpty(form.getCustomAttribGroupName())) {
    		errors.add("customAttribName", new ActionMessage("error.string.required"));
    	}
    	
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		String sql = "from   CustomAttributeGroup " +
					 "where  siteId = :siteId " +
					 "and    customAttribGroupName = :customAttribGroupName ";
		if (!insertMode) {
			sql += "and   customAttribGroupId != :customAttribGroupId";
		}
		Query query = em.createQuery(sql);
		query.setParameter("siteId", siteId);
		query.setParameter("customAttribGroupName", form.getCustomAttribGroupName());
		if (!insertMode) {
			query.setParameter("customAttribGroupId", Long.valueOf(form.getCustomAttribGroupId()));
		}
		if (query.getResultList().size() > 0) {
			errors.add("customAttribGroupName", new ActionMessage("error.customAttributeGroup.exist"));
		}
		
    	return errors;
    }
	
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("save", "save");
        map.put("edit", "edit");
        map.put("create", "create");
        map.put("remove", "remove");
        map.put("language", "language");
        map.put("translate", "translate");
        map.put("addCustomAttributeDetail", "addCustomAttributeDetail");
        map.put("removeCustomAttributeDetails", "removeCustomAttributeDetails");
        map.put("getCustomAttributeDetails", "getCustomAttributeDetails");
        map.put("resequence", "resequence");
        return map;
    }
}
