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

package com.jada.admin.tax;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import com.jada.util.JSONEscapeObject;

import com.jada.admin.AdminBean;
import com.jada.admin.AdminLookupDispatchAction;
import com.jada.dao.TaxRegionDAO;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.CustomerClass;
import com.jada.jpa.entity.ProductClass;
import com.jada.jpa.entity.Site;
import com.jada.jpa.entity.State;
import com.jada.jpa.entity.Tax;
import com.jada.jpa.entity.TaxLanguage;
import com.jada.jpa.entity.TaxRegion;
import com.jada.jpa.entity.TaxRegionProduct;
import com.jada.jpa.entity.TaxRegionProductCust;
import com.jada.jpa.entity.TaxRegionProductCustTax;
import com.jada.jpa.entity.TaxRegionZip;
import com.jada.util.Constants;
import com.jada.util.Format;

import fr.improve.struts.taglib.layout.util.FormUtils;

import java.net.URLEncoder;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

public class TaxRegionMaintAction
    extends AdminLookupDispatchAction {
	
    public ActionForward create(ActionMapping actionMapping,
                             ActionForm actionForm,
                             HttpServletRequest request,
                             HttpServletResponse response)
        throws Throwable {

        Site site = getAdminBean(request).getSite();
        TaxRegionMaintActionForm form = (TaxRegionMaintActionForm) actionForm;
        if (form == null) {
            form = new TaxRegionMaintActionForm();
        }
        form.setTaxRegionId("0");
        form.setPublished(true);
        form.setMode("C");
        createAdditionalInfo(form, site, null);
        
        FormUtils.setFormDisplayMode(request, form, FormUtils.CREATE_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }

    public ActionForward edit(ActionMapping actionMapping,
                              ActionForm actionForm,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Throwable {

        TaxRegionMaintActionForm form = (TaxRegionMaintActionForm) actionForm;
        Site site = getAdminBean(request).getSite();
        if (form == null) {
            form = new TaxRegionMaintActionForm();
        }
		String taxRegionId = request.getParameter("taxRegionId");
        TaxRegion taxRegion = TaxRegionDAO.load(site.getSiteId(), Format.getLong(taxRegionId));
        form.setMode("U");
		form.setTaxRegionId(Format.getLong(taxRegion.getTaxRegionId()));
		form.setTaxRegionDesc(taxRegion.getTaxRegionDesc());
		if (taxRegion.getPublished() == Constants.VALUE_YES) {
			form.setPublished(true);
		}
		ProductClass productClass = taxRegion.getShippingProductClass();
		if (productClass != null) {
			form.setShippingProductClassId(productClass.getProductClassId().toString());
		}
		
        createAdditionalInfo(form, site, taxRegion);
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    public ActionForward remove(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse response)
		throws Throwable {
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        Site site = getAdminBean(request).getSite();
		TaxRegionMaintActionForm form = (TaxRegionMaintActionForm) actionForm;
		TaxRegion taxRegion = TaxRegionDAO.load(site.getSiteId(), Format.getLong(form.getTaxRegionId()));
		em.remove(taxRegion);
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
		TaxRegionMaintActionForm form = (TaxRegionMaintActionForm) actionForm;
		if (form.getMode().equals("C")) {
			insertMode = true;
		}

		AdminBean adminBean = getAdminBean(request);
		Site site = adminBean.getSite();
		String siteId = site.getSiteId();

		TaxRegion taxRegion = new TaxRegion();
		if (!insertMode) {
			taxRegion = TaxRegionDAO.load(siteId, Format.getLong(form.getTaxRegionId()));
		}

		ActionMessages errors = validate(form);
		if (errors.size() != 0) {
			saveMessages(request, errors);
	        if (insertMode) {
	            FormUtils.setFormDisplayMode(request, form, FormUtils.CREATE_MODE);
	        }
	        else {
	            FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
	        }
			return mapping.findForward("error");
		}

		if (insertMode) {
			taxRegion.setRecCreateBy(adminBean.getUser().getUserId());
			taxRegion.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		}
		taxRegion.setSite(site);
		taxRegion.setTaxRegionDesc(form.getTaxRegionDesc());
		if (!Format.isNullOrEmpty(form.getShippingProductClassId())) {
			ProductClass productClass = (ProductClass) em.find(ProductClass.class, Format.getLong(form.getShippingProductClassId()));
			taxRegion.setShippingProductClass(productClass);
		}
		else {
			taxRegion.setShippingProductClass(null);
		}
		taxRegion.setPublished(Constants.VALUE_NO);
		if (form.isPublished()) {
			taxRegion.setPublished(Constants.VALUE_YES);
		}
		taxRegion.setRecUpdateBy(adminBean.getUser().getUserId());
		taxRegion.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		if (insertMode) {
			em.persist(taxRegion);
		}
		else {
			// em.update(taxRegion);
		}
        form.setTaxRegionId(Format.getLong(taxRegion.getTaxRegionId()));
		form.setMode("U");
        createAdditionalInfo(form, site, taxRegion);
        FormUtils.setFormDisplayMode(request, form, FormUtils.EDIT_MODE);
		return mapping.findForward("success");
	}
	
	private String getJSONCountriesAndStatesList(TaxRegion taxRegion) throws Exception  {
    	String result = null;
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
    	Iterator<Country> iterator = taxRegion.getCountries().iterator();
    	Vector<JSONEscapeObject> countries = new Vector<JSONEscapeObject>();
    	while (iterator.hasNext()) {
    		Country country = (Country) iterator.next();
    		JSONEscapeObject object = new JSONEscapeObject();
    		object.put("countryId", country.getCountryId());
    		object.put("countryCode", country.getCountryCode());
    		object.put("countryName", country.getCountryName());
    		countries.add(object);
    	}
    	jsonResult.put("countries", countries);
    	Iterator<State> iterator1 = taxRegion.getStates().iterator();
    	Vector<JSONEscapeObject> states = new Vector<JSONEscapeObject>();
    	while (iterator1.hasNext()) {
    		State state = (State) iterator1.next();
    		JSONEscapeObject object = new JSONEscapeObject();
    		object.put("stateId", state.getStateId());
    		object.put("stateCode", state.getStateCode());
    		object.put("stateName", state.getStateName());
    		states.add(object);
    	}
    	jsonResult.put("states", states);
    	result = jsonResult.toHtmlString();
    	return result;
	}
	
	private String getJSONZipCodeList(TaxRegion taxRegion) throws Exception {
    	String result = null;
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
    	Iterator<TaxRegionZip> iterator = taxRegion.getZipCodes().iterator();
    	Vector<JSONEscapeObject> zipCodes = new Vector<JSONEscapeObject>();
    	while (iterator.hasNext()) {
    		TaxRegionZip taxRegionZip = (TaxRegionZip) iterator.next();
    		JSONEscapeObject object = new JSONEscapeObject();
    		object.put("taxRegionZipId", taxRegionZip.getTaxRegionZipId());
    		object.put("zipCodeStart", taxRegionZip.getZipCodeStart());
    		object.put("zipCodeEnd", taxRegionZip.getZipCodeEnd());
    		zipCodes.add(object);
    	}
    	jsonResult.put("zipCodes", zipCodes);
    	result = jsonResult.toHtmlString();
    	return result;
	}

	private String getJSONProduct(HttpServletRequest request, TaxRegionProduct trProduct) throws Exception {
		AdminBean adminBean = getAdminBean(request);
		Long siteProfileClassDefaultId = adminBean.getSite().getSiteProfileClassDefault().getSiteProfileClassId();
		
    	String result = null;
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
    	jsonResult.put("taxRegionProductId", trProduct.getTaxRegionProductId());
    	jsonResult.put("productClassId", trProduct.getProductClass().getProductClassId());
    	jsonResult.put("productClassName", trProduct.getProductClass().getProductClassName());
    	
    	Vector<JSONEscapeObject> customers = new Vector<JSONEscapeObject>();
    	Iterator<?> custIterator = trProduct.getCustomerClasses().iterator();
    	while (custIterator.hasNext()) {
    		TaxRegionProductCust cust = (TaxRegionProductCust) custIterator.next();
    		JSONEscapeObject customer = new JSONEscapeObject();
    		customer.put("taxRegionProductCustId", cust.getTaxRegionProductCustId());
    		customer.put("custClassId", cust.getCustomerClass().getCustClassId());
    		customer.put("custClassName", cust.getCustomerClass().getCustClassName());
    		
    		Iterator<?> taxesIterator = cust.getTaxes().iterator();
    		Vector<JSONEscapeObject> taxesVector = new Vector<JSONEscapeObject>();
    		while (taxesIterator.hasNext()) {
    			TaxRegionProductCustTax trTax = (TaxRegionProductCustTax) taxesIterator.next();
    			Tax tax = trTax.getTax();
				TaxLanguage taxLanguage = null;
				for (TaxLanguage language : tax.getTaxLanguages()) {
					if (language.getSiteProfileClass().getSiteProfileClassId().equals(siteProfileClassDefaultId)) {
						taxLanguage = language;
					}
				}
    			JSONEscapeObject taxObj = new JSONEscapeObject();
    			taxObj.put("taxRegionProductCustTaxId", trTax.getTaxRegionProductCustTaxId());
    			taxObj.put("taxId", tax.getTaxId());
    			taxObj.put("seqNum", trTax.getSeqNum());
    			taxObj.put("taxName", taxLanguage.getTaxName());
    			taxObj.put("taxRate", tax.getTaxRate());
    			taxesVector.add(taxObj);
    		}
    		customer.put("taxes", taxesVector);
    		customers.add(customer);
    	}
/*
    	String sql = "";
    	sql = "select    trProductCust " +
    		  "from      TaxRegionProduct trProduct " +
    		  "left      join trProduct.customerClasses trProductCust " +
    		  "left      join trProductCust.customerClass customerClass " +
    		  "where     trProduct.taxRegionProductId = :taxRegionProductId " +
    		  "order     by customerClass.custClassName";
    	Query query = em.createQuery(sql);
    	query.setParameter("taxRegionProductId", trProduct.getTaxRegionProductId());
    	Iterator<TaxRegionProductCust> iterator = query.getResultList().iterator();
    	Vector<?> tmpCustList = new Vector();
    	while (iterator.hasNext()) {
    		tmpCustList.add(iterator.next());
    	}
    	
    	Vector<JSONEscapeObject> customers = new Vector<JSONEscapeObject>();
    	Iterator<?> custIterator<?> = tmpCustList.iterator();
    	while (custIterator.hasNext()) {
    		TaxRegionProductCust cust = (TaxRegionProductCust) custIterator.next();
    		JSONEscapeObject customer = new JSONEscapeObject();
    		customer.put("taxRegionProductCustId", cust.getTaxRegionProductCustId());
    		customer.put("custClassId", cust.getCustomerClass().getCustClassId());
    		customer.put("custClassName", cust.getCustomerClass().getCustClassName());
    		
    		sql = "select    trProductCustTax, trProductCust, tax " +
    			  "from      TaxRegionProductCust trProductCust " +
			      "left      join trProductCust.taxes trProductCustTax " +
			      "left      join trProductCustTax.tax tax  " +
			      "where     trProductCust.taxRegionProductCustId = :taxRegionProductCustId " +
			      "order     by trProductCustTax.seqNum, tax.taxName";
    		query = em.createQuery(sql);
    		query.setParameter("taxRegionProductCustId", cust.getTaxRegionProductCustId());
    		
    		Iterator<?> taxes = query.getResultList().iterator();
    		Vector<JSONEscapeObject> taxesVector = new Vector<JSONEscapeObject>();
    		while (taxes.hasNext()) {
    			Object objects[] = (Object[]) taxes.next();
    			TaxRegionProductCustTax trTax = (TaxRegionProductCustTax) objects[0];
    			Tax tax = trTax.getTax();
    			JSONEscapeObject taxObj = new JSONEscapeObject();
    			taxObj.put("taxRegionProductCustTaxId", trTax.getTaxRegionProductCustTaxId());
    			taxObj.put("taxId", tax.getTaxId());
    			taxObj.put("seqNum", trTax.getSeqNum());
    			taxObj.put("taxName", tax.getTaxName());
    			taxObj.put("taxRate", tax.getTaxRate());
    			taxesVector.add(taxObj);
    		}
    		customer.put("taxes", taxesVector);
    		customers.add(customer);
    	}
*/
    	jsonResult.put("customers", customers);
    	result = jsonResult.toHtmlString();
    	return result;
	}
	
	public ActionForward getRegionList(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		TaxRegionMaintActionForm form = (TaxRegionMaintActionForm) actionForm;
        Long taxRegionId = Format.getLong(form.getTaxRegionId());
        TaxRegion taxRegion = (TaxRegion) em.find(TaxRegion.class, taxRegionId);
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		String result = getJSONCountriesAndStatesList(taxRegion);
		streamWebService(response, result);
		return null;
	}
	
	public ActionForward getZipCodeList(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		TaxRegionMaintActionForm form = (TaxRegionMaintActionForm) actionForm;
        Long taxRegionId = Format.getLong(form.getTaxRegionId());
        TaxRegion taxRegion = (TaxRegion) em.find(TaxRegion.class, taxRegionId);
		JSONEscapeObject jsonResult = new JSONEscapeObject();
		jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
		String result = getJSONZipCodeList(taxRegion);
		streamWebService(response, result);
		return null;
	}

	public ActionForward removeProductClass(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        TaxRegionMaintActionForm form = (TaxRegionMaintActionForm) actionForm;
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
        Long taxRegionProductId = Format.getLong(form.getTaxRegionProductId());
        TaxRegionProduct product = (TaxRegionProduct) em.find(TaxRegionProduct.class, taxRegionProductId);
/*
        Iterator<?> customers = product.getCustomerClasses().iterator();
        while (customers.hasNext()) {
        	TaxRegionProductCust customer = (TaxRegionProductCust) customers.next();
        	Iterator<?> taxes = customer.getTaxes().iterator();
        	while (taxes.hasNext()) {
        		TaxRegionProductCustTax tax = (TaxRegionProductCustTax) taxes.next();
        		em.remove(tax);
        	}
        	em.remove(customer);
        }
*/
        em.remove(product);
    	String result = jsonResult.toHtmlString();
    	streamWebService(response, result);
    	return null;
	}
	
	public ActionForward addProductClass(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        TaxRegionMaintActionForm form = (TaxRegionMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
    	Long taxRegionId = Format.getLong(form.getTaxRegionId());
        Long productClassId = Format.getLong(form.getProductClassId());
        TaxRegion taxRegion = (TaxRegion) em.find(TaxRegion.class, taxRegionId);
        ProductClass productClass = (ProductClass) em.find(ProductClass.class, productClassId);
        
        Iterator<?> iterator = taxRegion.getProductClasses().iterator();
        while (iterator.hasNext()) {
        	TaxRegionProduct p = (TaxRegionProduct) iterator.next();
        	if (p.getProductClass().getProductClassId().equals(productClass.getProductClassId())) {
            	jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
            	jsonResult.put("message", "Product class already exist");
            	streamWebService(response, jsonResult.toHtmlString());
        		return null;
        	}
        }
        
        TaxRegionProduct trProduct = new TaxRegionProduct();
        trProduct.setTaxRegion(taxRegion);
        trProduct.setProductClass(productClass);
        trProduct.setRecUpdateBy(adminBean.getUser().getUserId());
		trProduct.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
		trProduct.setRecCreateBy(adminBean.getUser().getUserId());
		trProduct.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		taxRegion.getProductClasses().add(trProduct);
		
		em.persist(trProduct);
		
		String result = getJSONProduct(request, trProduct);
		streamWebService(response, result);
		return null;
	}
	
	public ActionForward getProduct(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		TaxRegionMaintActionForm form = (TaxRegionMaintActionForm) actionForm;
        Long taxRegionProductId = Format.getLong(form.getTaxRegionProductId());
        TaxRegionProduct taxRegionProduct = (TaxRegionProduct) em.find(TaxRegionProduct.class, taxRegionProductId);
		String result = getJSONProduct(request, taxRegionProduct);
		streamWebService(response, result);
		return null;
	}

	public ActionForward removeCustomerClass(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        TaxRegionMaintActionForm form = (TaxRegionMaintActionForm) actionForm;
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
        Long taxRegionProductCustId = Format.getLong(form.getTaxRegionProductCustId());
        TaxRegionProductCust customer = (TaxRegionProductCust) em.find(TaxRegionProductCust.class, taxRegionProductCustId);
        customer.getTaxRegionProduct().getCustomerClasses().remove(customer);
/*
    	Iterator<?> taxes = customer.getTaxes().iterator();
    	while (taxes.hasNext()) {
    		TaxRegionProductCustTax tax = (TaxRegionProductCustTax) taxes.next();
    		em.remove(tax);
    	}
*/
    	em.remove(customer);
		String result = getJSONProduct(request, customer.getTaxRegionProduct());
		streamWebService(response, result);
    	return null;
	}
	
	public ActionForward addCustomerClass(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        TaxRegionMaintActionForm form = (TaxRegionMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
        Long trProductId = Format.getLong(form.getTaxRegionProductId());
        Long custClassId = Format.getLong(form.getCustClassId());
        TaxRegionProduct taxRegionProduct = (TaxRegionProduct) em.find(TaxRegionProduct.class, trProductId);
        CustomerClass customerClass = (CustomerClass) em.find(CustomerClass.class, custClassId);
        
        Iterator<?> iterator = taxRegionProduct.getCustomerClasses().iterator();
        while (iterator.hasNext()) {
        	TaxRegionProductCust c = (TaxRegionProductCust) iterator.next();
        	if (c.getCustomerClass().getCustClassId().equals(customerClass.getCustClassId())) {
            	jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
            	jsonResult.put("message", "Customer class already exist");
            	streamWebService(response, jsonResult.toHtmlString());
        		return null;
        	}
        }
        
        TaxRegionProductCust trProductCust = new TaxRegionProductCust();
        trProductCust.setTaxRegionProduct(taxRegionProduct);
        trProductCust.setCustomerClass(customerClass);
        trProductCust.setRecUpdateBy(adminBean.getUser().getUserId());
        trProductCust.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        trProductCust.setRecCreateBy(adminBean.getUser().getUserId());
        trProductCust.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        taxRegionProduct.getCustomerClasses().add(trProductCust);
		
		em.persist(trProductCust);
		
		String result = getJSONProduct(request, taxRegionProduct);
		streamWebService(response, result);
		return null;
	}
	
	public ActionForward addTax(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        TaxRegionMaintActionForm form = (TaxRegionMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
    	
        Long taxId = Format.getLong(form.getTaxId());
        Long trProductCustId = Format.getLong(form.getTaxRegionProductCustId());
        TaxRegionProductCust taxRegionProductCust = (TaxRegionProductCust) em.find(TaxRegionProductCust.class, trProductCustId);
        Tax tax = (Tax) em.find(Tax.class, taxId);
        
        Iterator<?> iterator = taxRegionProductCust.getTaxes().iterator();
        while (iterator.hasNext()) {
        	TaxRegionProductCustTax t = (TaxRegionProductCustTax) iterator.next();
        	if (t.getTax().getTaxId().equals(tax.getTaxId())) {
            	jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
            	jsonResult.put("message", "Tax code already exist");
            	streamWebService(response, jsonResult.toHtmlString());
        		return null;
        	}
        }
        
        TaxRegionProductCustTax trProductCustTax = new TaxRegionProductCustTax();
        trProductCustTax.setTaxRegionProductCust(taxRegionProductCust);
        trProductCustTax.setTax(tax);
        trProductCustTax.setRecUpdateBy(adminBean.getUser().getUserId());
        trProductCustTax.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        trProductCustTax.setRecCreateBy(adminBean.getUser().getUserId());
        trProductCustTax.setRecCreateDatetime(new Date(System.currentTimeMillis()));
		
		em.persist(trProductCustTax);
		
		String result = getJSONProduct(request, taxRegionProductCust.getTaxRegionProduct());
		streamWebService(response, result);
		return null;
	}

	public ActionForward removeTaxes(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        TaxRegionMaintActionForm form = (TaxRegionMaintActionForm) actionForm;
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
    	
        Long trProductCustId = Format.getLong(form.getTaxRegionProductCustId());
        TaxRegionProductCust taxRegionProductCust = (TaxRegionProductCust) em.find(TaxRegionProductCust.class, trProductCustId);
   	
        String trProductCustTaxIds[] = form.getTaxRegionProductCustTaxIds();      
        for (int i = 0; i < trProductCustTaxIds.length; i++) {
        	Long id = Format.getLong(trProductCustTaxIds[i]);
        	TaxRegionProductCustTax tax = (TaxRegionProductCustTax) em.find(TaxRegionProductCustTax.class, id);
        	tax.getTaxRegionProductCust().getTaxes().remove(tax);
        	em.remove(tax);
        }
		String result = getJSONProduct(request, taxRegionProductCust.getTaxRegionProduct());
		streamWebService(response, result);
		return null;
	}

	public ActionForward sequenceTaxes(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        TaxRegionMaintActionForm form = (TaxRegionMaintActionForm) actionForm;
        Long trProductCustId = Format.getLong(form.getTaxRegionProductCustId());
        TaxRegionProductCust taxRegionProductCust = (TaxRegionProductCust) em.find(TaxRegionProductCust.class, trProductCustId);
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
    	
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        int i = 0;
        while (true) {
        	String seqNum = request.getParameter("seqNum_" + Integer.valueOf(i).toString());
        	if (seqNum == null) {
        		break;
        	}
        	if (!Format.isInt(seqNum)) {
        		MessageResources resources = this.getResources(request);
            	jsonResult.put("status", Constants.WEBSERVICE_STATUS_FAILED);
            	jsonResult.put("message", resources.getMessage("error.int.invalid"));
            	streamWebService(response, jsonResult.toHtmlString());
        		return null;
        	}
        	hashtable.put("seqNum_" + Integer.valueOf(i).toString(), seqNum);
         	String taxRegionProductCustTaxId = request.getParameter("taxRegionProductCustTaxId_" + Integer.valueOf(i).toString());
        	hashtable.put("tax_" + Integer.valueOf(i).toString(), taxRegionProductCustTaxId);
        	i++;
        }
        i = 0;
        while (true) {
        	String taxRegionProductCustTaxId = (String) hashtable.get("tax_" + Integer.valueOf(i).toString());
        	if (taxRegionProductCustTaxId == null) {
        		break;
        	}
        	TaxRegionProductCustTax trProductCustTax = (TaxRegionProductCustTax) em.find(TaxRegionProductCustTax.class, Format.getLong(taxRegionProductCustTaxId));
        	String seqNum = (String) hashtable.get("seqNum_" + Integer.valueOf(i).toString());
        	trProductCustTax.setSeqNum(Format.getInt(seqNum));
        	em.persist(trProductCustTax);
        	i++;
        }
		String result = getJSONProduct(request, taxRegionProductCust.getTaxRegionProduct());
		streamWebService(response, result);
		return null;
	}
	
	public ActionForward addRegion(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        TaxRegionMaintActionForm form = (TaxRegionMaintActionForm) actionForm;
     	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
    	
        Long taxRegionId = Format.getLong(form.getTaxRegionId());
        TaxRegion taxRegion = (TaxRegion) em.find(TaxRegion.class, taxRegionId);

    	String countryIds[] = form.getCountryIds();
    	if (countryIds != null) {
	    	for (int i = 0; i < countryIds.length; i++) {
	    		Long countryId = Format.getLong(countryIds[i]);
	            Country country = (Country) em.find(Country.class, countryId);
	            Iterator<?> countries = taxRegion.getCountries().iterator();
	            boolean found = false;
	            while (countries.hasNext()) {
	            	Country c = (Country) countries.next();
	            	if (c.getCountryId().equals(countryId)) {
	            		found = true;
	            		break;
	            	}
	            }
	            if (!found) {
	            	taxRegion.getCountries().add(country);
	            }
	    	}
    	}
    	
    	String stateIds[] = form.getStateIds();
    	if (stateIds != null) {
	    	for (int i = 0; i < stateIds.length; i++) {
	    		Long stateId = Format.getLong(stateIds[i]);
	            State state = (State) em.find(State.class, stateId);
	            Iterator<?> states = taxRegion.getStates().iterator();
	            boolean found = false;
	            while (states.hasNext()) {
	            	State s = (State) states.next();
	            	if (s.getStateId().equals(stateId)) {
	            		found = true;
	            		break;
	            	}
	            }
	            if (!found) {
	            	taxRegion.getStates().add(state);
	            }
	    	}
    	}
		em.persist(taxRegion);
		
		String result = getJSONCountriesAndStatesList(taxRegion);
		streamWebService(response, result);
		return null;
	}
	
	public ActionForward removeRegion(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        TaxRegionMaintActionForm form = (TaxRegionMaintActionForm) actionForm;
     	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
    	
        Long taxRegionId = Format.getLong(form.getTaxRegionId());
        TaxRegion taxRegion = (TaxRegion) em.find(TaxRegion.class, taxRegionId);
        
        boolean modified = false;
    	String countryIds[] = form.getCountryIds();
    	if (countryIds != null) {
	    	for (int i = 0; i < countryIds.length; i++) {
	    		Long countryId = Format.getLong(countryIds[i]);
	            Iterator<?> countries = taxRegion.getCountries().iterator();
	            while (countries.hasNext()) {
	            	Country c = (Country) countries.next();
	            	if (c.getCountryId().equals(countryId)) {
	            		countries.remove();
	            		modified = true;
	            		break;
	            	}
	            }
	    	}
    	}
    	
    	String stateIds[] = form.getStateIds();
    	if (stateIds != null) {
	    	for (int i = 0; i < stateIds.length; i++) {
	    		Long stateId = Format.getLong(stateIds[i]);
	            Iterator<?> states = taxRegion.getStates().iterator();
	            while (states.hasNext()) {
	            	State s = (State) states.next();
	            	if (s.getStateId().equals(stateId)) {
	            		states.remove();
	            		modified = true;
	            		break;
	            	}
	            }
	    	}
    	}

    	if (modified) {
    		em.persist(taxRegion);
    	}
		
		String result = getJSONCountriesAndStatesList(taxRegion);
		streamWebService(response, result);
		return null;
	}

	public ActionForward addZipCode(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        TaxRegionMaintActionForm form = (TaxRegionMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
    	
        Long taxRegionId = Format.getLong(form.getTaxRegionId());
        TaxRegion taxRegion = (TaxRegion) em.find(TaxRegion.class, taxRegionId);
        
        TaxRegionZip taxRegionZip = new TaxRegionZip();
        taxRegionZip.setZipCodeStart(form.getZipCodeStart());
        taxRegionZip.setZipCodeEnd(form.getZipCodeEnd());
        taxRegionZip.setRecUpdateBy(adminBean.getUser().getUserId());
        taxRegionZip.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        taxRegionZip.setRecCreateBy(adminBean.getUser().getUserId());
        taxRegionZip.setRecCreateDatetime(new Date(System.currentTimeMillis()));
        taxRegionZip.setTaxRegion(taxRegion);
		
		em.persist(taxRegionZip);
		
		String result = getJSONZipCodeList(taxRegion);
		streamWebService(response, result);
		return null;
	}
	
	public ActionForward modifyZipCode(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        TaxRegionMaintActionForm form = (TaxRegionMaintActionForm) actionForm;
        AdminBean adminBean = getAdminBean(request);
    	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
    	
        Long taxRegionId = Format.getLong(form.getTaxRegionId());
        TaxRegion taxRegion = (TaxRegion) em.find(TaxRegion.class, taxRegionId);
        Long taxRegionZipId = Format.getLong(form.getTaxRegionZipId());
        TaxRegionZip taxRegionZip = (TaxRegionZip) em.find(TaxRegionZip.class, taxRegionZipId);
       
        taxRegionZip.setZipCodeStart(form.getZipCodeStart());
        taxRegionZip.setZipCodeEnd(form.getZipCodeEnd());
        taxRegionZip.setRecUpdateBy(adminBean.getUser().getUserId());
        taxRegionZip.setRecUpdateDatetime(new Date(System.currentTimeMillis()));
        taxRegionZip.setTaxRegion(taxRegion);
		
		em.persist(taxRegionZip);
		
		String result = getJSONZipCodeList(taxRegion);
		streamWebService(response, result);
		return null;
	}
	
	public ActionForward removeZipCode(ActionMapping mapping, 
			  ActionForm actionForm,
			  HttpServletRequest request,
			  HttpServletResponse response) throws Exception {
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
        TaxRegionMaintActionForm form = (TaxRegionMaintActionForm) actionForm;
     	JSONEscapeObject jsonResult = new JSONEscapeObject();
    	jsonResult.put("status", Constants.WEBSERVICE_STATUS_SUCCESS);
    	
        Long taxRegionId = Format.getLong(form.getTaxRegionId());
        String taxRegionZipIds[] = form.getTaxRegionZipIds();
        TaxRegion taxRegion = (TaxRegion) em.find(TaxRegion.class, taxRegionId);
        if (taxRegionZipIds != null) {
	        for (int i = 0; i < taxRegionZipIds.length; i++) {
	        	Long taxRegionZipId = Format.getLong(taxRegionZipIds[i]);
	            TaxRegionZip taxRegionZip = (TaxRegionZip) em.find(TaxRegionZip.class, taxRegionZipId);
	            taxRegion.getZipCodes().remove(taxRegionZip);
	            em.remove(taxRegionZip);
	        }
        }
		String result = getJSONZipCodeList(taxRegion);
		streamWebService(response, result);
		return null;
	}
	
	public void createAdditionalInfo(TaxRegionMaintActionForm form, Site site, TaxRegion taxRegion) throws Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
    	String sql = "select productClass " +
    		         "from   ProductClass productClass " +
    	             "left   join productClass.site site " +
    	             "where  site.siteId = :siteId " +
    	             "order  by productClass.productClassName";
		Query query = em.createQuery(sql);
		query.setParameter("siteId", site.getSiteId());
		Iterator<?> iterator = query.getResultList().iterator();
		Vector<LabelValueBean> vector = new Vector<LabelValueBean>();
		vector.add(new LabelValueBean("", ""));
		while (iterator.hasNext()) {
			ProductClass productClass = (ProductClass) iterator.next();
			LabelValueBean bean = new LabelValueBean(productClass.getProductClassName(), productClass.getProductClassId().toString());
			vector.add(bean);
		}
		LabelValueBean productClassList[] = new LabelValueBean[vector.size()];
		vector.copyInto(productClassList);
		form.setProductClassList(productClassList);
		
		if (taxRegion != null) {
			JSONEscapeObject jsonProductClasses = new JSONEscapeObject();
	    	sql = "select    trProductClasses " +
	    		  "from      TaxRegion tr " +
	    		  "inner join tr.productClasses trProductClasses " +
	    		  "left      join trProductClasses.productClass productClass " +
	    		  "where     tr.taxRegionId = :taxRegionId " +
	    		  "order     by productClass.productClassName";
	    	query = em.createQuery(sql);
	    	query.setParameter("taxRegionId", taxRegion.getTaxRegionId());
			iterator = query.getResultList().iterator();
			Vector<JSONEscapeObject> jsonVector = new Vector<JSONEscapeObject>();
			while (iterator.hasNext()) {
				TaxRegionProduct trProduct = (TaxRegionProduct) iterator.next();
				JSONEscapeObject object = new JSONEscapeObject();
				object.put("taxRegionProductId", trProduct.getTaxRegionProductId());
				object.put("productClassId", trProduct.getProductClass().getProductClassId());
				object.put("productClassName", trProduct.getProductClass().getProductClassName());
				jsonVector.add(object);
			}
			jsonProductClasses.put("taxRegionProductClasses", jsonVector);
			// This string is to be converted into javascript variable via jstl
			form.setJsonProductClasses(URLEncoder.encode(jsonProductClasses.toHtmlString(), "UTF-8"));
		}
	}

    public ActionMessages validate(TaxRegionMaintActionForm form) { 
    	ActionMessages errors = new ActionMessages();
    	
    	if (Format.isNullOrEmpty(form.getTaxRegionDesc())) {
    		errors.add("taxRegionDesc", new ActionMessage("error.string.required"));
    	}
    	return errors;
    }
	
    protected java.util.Map<String, String> getKeyMethodMap()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("save", "save");
        map.put("edit", "edit");
        map.put("create", "create");
        map.put("remove", "remove");
        map.put("removeProductClass", "removeProductClass");
        map.put("addProductClass", "addProductClass");
        map.put("removeCustomerClass", "removeCustomerClass");
        map.put("addCustomerClass", "addCustomerClass");
        map.put("addTax", "addTax");
        map.put("removeTaxes", "removeTaxes");
        map.put("sequenceTaxes", "sequenceTaxes");
        map.put("getRegionList", "getRegionList");
        map.put("addRegion", "addRegion");
        map.put("removeRegion", "removeRegion");
        map.put("addZipCode", "addZipCode");
        map.put("getZipCodeList", "getZipCodeList");
        map.put("removeZipCode", "removeZipCode");
        map.put("addZipCode", "addZipCode");
        map.put("getZipCodeList", "getZipCodeList");
        map.put("removeZipCode", "removeZipCode");
        map.put("getProduct", "getProduct");
       return map;
    }
}
