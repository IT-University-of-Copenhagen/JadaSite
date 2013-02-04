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

package com.jada.admin.item;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminListingAction;
import com.jada.admin.AdminListingActionForm;
import com.jada.dao.ItemDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.Site;
import com.jada.util.Format;
import com.jada.util.Utility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;


import javax.persistence.Query;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import java.util.Map;
import java.util.HashMap;

public class ItemListingAction
    extends AdminListingAction {

    public void extract(AdminListingActionForm actionForm, HttpServletRequest request) throws Throwable {
    	ItemListingActionForm form = (ItemListingActionForm) actionForm;
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        AdminBean adminBean = getAdminBean(request);
        Site site = adminBean.getSite();

        ActionMessages errors = validate(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
			return;
		}

        Query query = null;
        String selectedCategories[] = form.getSrSelectedCategories();

        String sql = "select   distinct item " +
        			 "from     Item item ";
        if (selectedCategories != null) {
        	sql += "left   join item.categories category ";
        }
        sql += "where   item.siteId = :siteId ";
        if (form.getSrItemNum() != null && form.getSrItemNum().length() > 0) {
        	sql += "and item.itemNum = :itemNum ";
        }
        if (form.getSrItemSkuCd().length() > 0) {
        	sql += "and item.itemSkuCd = :itemSkuCd ";
        }
        if (form.getSrItemUpcCd().length() > 0) {
        	sql += "and item.itemUpcCd = :itemUpcCd ";
        }
        if (form.getSrItemShortDesc().length() > 0) {
        	sql += "and item.itemLanguage.itemShortDesc like :itemShortDesc ";
        }
        if (!form.getSrPublished().equals("*")) {
        	sql += "and item.published = :published ";
        }
        sql += "and item.itemTypeCd in (";
        if (form.isSrItemTypeRegular()) {
        	sql += "'01',";
        }
        if (form.isSrItemTypeTemplate()) {
        	sql += "'02',";
        }
        if (form.isSrItemTypeSku()) {
        	sql += "'03',";
        }
        if (form.isSrItemTypeStaticBundle()) {
        	sql += "'04',";
        }
        if (form.isSrItemTypeRecommandBundle()) {
        	sql += "'05',";
        }
        sql += "'') ";
        sql += "and item.itemPublishOn between :itemPublishOnStart and :itemPublishOnEnd ";
        sql += "and item.itemExpireOn between :itemExpireOnStart and :itemExpireOnEnd ";
        if (!form.getSrUpdateBy().equals("All")) {
        	sql += "and item.recUpdateBy = :recUpdateBy ";
        }
        if (!form.getSrCreateBy().equals("All")) {
        	sql += "and item.recCreateBy = :recCreateBy ";
        }
        
        if (selectedCategories != null) {
        	sql += "and category.catId in (";
        	int index = 0;
        	for (int i = 0; i < selectedCategories.length; i++) {
        		Long catIds[] = Utility.getCatIdTreeList(site.getSiteId(), Format.getLong(selectedCategories[i]));
        		for (int j = 0; j < catIds.length; j++) {
	        		if (index > 0) {
	        			sql += ",";
	        		}
	        		sql += ":selectedCategory" + index++;
        		}
         	}
           	sql += ") ";
        }

        query = em.createQuery(sql);
        Date date = null;
        query.setParameter("siteId", site.getSiteId());
        if (form.getSrItemSkuCd().length() > 0) {
        	query.setParameter("itemSkuCd", form.getSrItemSkuCd());
        }
        if (form.getSrItemNum().length() > 0) {
        	query.setParameter("itemNum", form.getSrItemNum());
        }
        if (form.getSrItemUpcCd().length() > 0) {
        	query.setParameter("itemUpcCd", form.getSrItemUpcCd());
        }
        if (form.getSrItemShortDesc().length() > 0) {
        	query.setParameter("itemShortDesc", "%" + form.getSrItemShortDesc() + "%");
        }
        if (!form.getSrPublished().equals("*")) {
        	query.setParameter("published", form.getSrPublished());
        }
        if (form.getSrItemPublishOnStart().length() > 0) {
        	date = Format.getDate(form.getSrItemPublishOnStart());
        	query.setParameter("itemPublishOnStart", date);
        }
        else {
        	query.setParameter("itemPublishOnStart", Format.LOWDATE);
        }
        if (form.getSrItemPublishOnEnd().length() > 0) {
        	date = Format.getDate(form.getSrItemPublishOnEnd());
        	query.setParameter("itemPublishOnEnd", date);
        }
        else {
        	query.setParameter("itemPublishOnEnd", Format.HIGHDATE);
        }
        if (form.getSrItemExpireOnStart().length() > 0) {
        	date = Format.getDate(form.getSrItemExpireOnStart());
        	query.setParameter("itemExpireOnStart", date);
        }
        else {
        	query.setParameter("itemExpireOnStart", Format.LOWDATE);
        }
        if (form.getSrItemExpireOnEnd().length() > 0) {
        	date = Format.getDate(form.getSrItemExpireOnEnd());
        	query.setParameter("itemExpireOnEnd", date);
        }
        else {
        	query.setParameter("itemExpireOnEnd", Format.HIGHDATE);
        }
        if (!form.getSrUpdateBy().equals("All")) {
        	query.setParameter("recUpdateBy", form.getSrUpdateBy());
        }
        if (!form.getSrCreateBy().equals("All")) {
        	query.setParameter("recCreateBy", form.getSrCreateBy());
        }
        if (selectedCategories != null) {
        	int index = 0;
        	for (int i = 0; i < selectedCategories.length; i++) {
        		Long catIds[] = Utility.getCatIdTreeList(site.getSiteId(), Format.getLong(selectedCategories[i]));
        		for (int j = 0; j < catIds.length; j++) {
        			query.setParameter("selectedCategory" + index++, catIds[j].longValue());
        		}
        	}
        }

        List<?> list = query.getResultList();
        if (Format.isNullOrEmpty(form.getSrPageNo())) {
        	form.setSrPageNo("1");
        }
        int pageNo = Integer.parseInt(form.getSrPageNo());
        calcPage(adminBean, form, list, pageNo);
        Vector<ItemDisplayForm> vector = new Vector<ItemDisplayForm>();
        int startRecord = (form.getPageNo() - 1) * adminBean.getListingPageSize();
        int endRecord = startRecord + adminBean.getListingPageSize();
        for (int i = startRecord; i < list.size() && i < endRecord; i++) {
        	Item item = (Item) list.get(i);
       	
        	ItemDisplayForm itemDisplay = new ItemDisplayForm();
        	itemDisplay.setItemId(Format.getLong(item.getItemId()));
        	itemDisplay.setItemSkuCd(item.getItemSkuCd());
        	itemDisplay.setItemNum(item.getItemNum());
        	itemDisplay.setItemShortDesc(item.getItemLanguage().getItemShortDesc());
        	itemDisplay.setPublished(String.valueOf(item.getPublished()));
        	itemDisplay.setItemPublishOn(Format.getFullDate(item.getItemPublishOn()));
        	itemDisplay.setItemExpireOn(Format.getFullDate(item.getItemExpireOn()));
            vector.add(itemDisplay);
        }
        ItemDisplayForm items[] = new ItemDisplayForm[vector.size()];
        vector.copyInto(items);
        form.setItems(items);
    }

    public void initSearchInfo(AdminListingActionForm actionForm, String siteId) throws Exception {
    	ItemListingActionForm form = (ItemListingActionForm) actionForm;
    	if (form.getSrPublished() == null) {
    		form.setSrPublished("*");
    	}
     	Vector<String> userIdVector = Utility.getUserIdsForSite(siteId);
    	String srSelectUsers[] = new String[userIdVector.size()];
    	userIdVector.copyInto(srSelectUsers);
    	form.setSrSelectUsers(srSelectUsers);
//    	form.setSrCategoryTree(Utility.makeCategoryTree(siteId, site.getSiteProfileClassDefault().getSiteProfileClassId()));
        String jsonCategoryTree = Utility.makeJSONCategoryTree(siteId, form.getSiteProfileClassId(), form.isSiteProfileClassDefault()).toHtmlString();
        form.setJsonCategoryTree(jsonCategoryTree);
    }

    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
    		throws Throwable {
    	
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	Site site = getAdminBean(request).getSite();
        ItemListingActionForm form = (ItemListingActionForm) actionForm;
        String itemIds[] = form.getItemIds();
        
        try {
	        if (form.getItems() != null) {
	        	ItemDisplayForm items[] = form.getItems();
		        for (int i = 0; i < items.length; i++) {
		        	if (items[i].getRemove() == null) {
		        		continue;
		        	}
		        	if (!items[i].getRemove().equals("Y")) {
		        		continue;
		        	}
		            Item item = ItemDAO.load(site.getSiteId(), Format.getLong(items[i].getItemId()));
		            ItemDAO.remove(site.getSiteId(), item);
		            em.remove(item);
		        }
		        em.getTransaction().commit();
	        }
        }
		catch (Exception e) {
			if (Utility.isConstraintViolation(e)) {
				ActionMessages errors = new ActionMessages();
				errors.add("error", new ActionMessage("error.remove.items.constraint"));
				saveMessages(request, errors);
		        ActionForward forward = actionMapping.findForward("removeError") ;
		        return forward;
			}
			throw e;
       }
        
        if (itemIds != null) {
	        for (int i = 0; i < itemIds.length; i++) {
	            Item item = new Item();
	            item = ItemDAO.load(site.getSiteId(), Format.getLong(itemIds[i]));
	            em.remove(item);
	        }
        }

        ActionForward forward = actionMapping.findForward("removed") ;
        forward = new ActionForward(forward.getPath() + "?process=list&srPageNo=" + form.getPageNo(), forward.getRedirect());
        return forward;
    }
    
    public ActionMessages validate(ItemListingActionForm form) throws Exception {
    	ActionMessages errors = new ActionMessages();
    	
    	if (!Format.isNullOrEmpty(form.getSrItemPublishOnStart()) && !Format.isDate(form.getSrItemPublishOnStart())) {
    		errors.add("srItemPublishOnStart", new ActionMessage("error.date.invalid"));
    	}
    	if (!Format.isNullOrEmpty(form.getSrItemPublishOnEnd()) && !Format.isDate(form.getSrItemPublishOnEnd())) {
    		errors.add("srItemPublishOnEnd", new ActionMessage("error.date.invalid"));
    	}
    	if (!Format.isNullOrEmpty(form.getSrItemExpireOnStart()) && !Format.isDate(form.getSrItemExpireOnStart())) {
    		errors.add("srItemExpireOnStart", new ActionMessage("error.date.invalid"));
    	}
    	if (!Format.isNullOrEmpty(form.getSrItemExpireOnEnd()) && !Format.isDate(form.getSrItemExpireOnEnd())) {
    		errors.add("srItemExpireOnEnd", new ActionMessage("error.date.invalid"));
    	}
    	return errors;
    }
    
    public void initForm(AdminListingActionForm actionForm) {
        ItemListingActionForm form = (ItemListingActionForm) actionForm;
    	form.setItems(null);
    	form.setSrItemTypeRegular(true);
    	form.setSrItemTypeTemplate(true);
    	form.setSrItemTypeSku(true);
    	form.setSrItemTypeStaticBundle(true);
    	form.setSrItemTypeRecommandBundle(true);
    }

    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("remove", "remove");
        map.put("search", "search");
        map.put("start", "start");
        map.put("back", "back");
        map.put("list", "list");
        return map;
    }
}