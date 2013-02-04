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

package com.jada.order.cart;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import com.jada.content.ContentBean;
import com.jada.content.ContentSessionKey;
import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.Country;
import com.jada.jpa.entity.CustomerAddress;
import com.jada.jpa.entity.CustomerClass;
import com.jada.jpa.entity.Item;
import com.jada.jpa.entity.ProductClass;
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
import com.jada.util.Utility;

public class TaxCalculator {
	CustomerAddress shippingAddress = null;
	CustomerClass customerClass;
	TaxRegion taxRegionList[] = {};
//	TaxRegion taxRegion;
	ContentSessionKey contentSessionKey = null;
	
	public TaxCalculator(CustomerAddress shippingAddress, CustomerClass customerClass, ContentBean contentBean) throws Exception {
		this.shippingAddress = shippingAddress;
		this.customerClass = customerClass;
		init(contentBean);
	}
	
	public void init(ContentBean contentBean) throws Exception {
		if (shippingAddress == null) {
			return;
		}
		
		contentSessionKey = contentBean.getContentSessionKey();
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();        
		String sql = "select   taxRegion " +
					 "from     TaxRegion taxRegion " +
					 "left     join taxRegion.site site " +
					 "where    site.siteId = :siteId";
		Query query = em.createQuery(sql);
		query.setParameter("siteId", contentBean.getSiteDomain().getSite().getSiteId());
		boolean found = false;
		TaxRegion tr = null;
		Vector<TaxRegion> vector = new Vector<TaxRegion>();
		Iterator<?> iterator = query.getResultList().iterator();
		while (iterator.hasNext()) {
			tr = (TaxRegion) iterator.next();
			if (tr.getPublished() != Constants.VALUE_YES) {
				continue;
			}
        	Iterator<?> iteratorCountry = null;
        	iteratorCountry = tr.getCountries().iterator();
        	while (iteratorCountry.hasNext()) {
        		Country country = (Country) iteratorCountry.next();
        		if (country.getCountryId().equals(shippingAddress.getCountry().getCountryId())) {
        			vector.add(tr);
        			break;
        		}
        	}
        	
        	if (!found) {
        		if (shippingAddress.getState() != null) {
		        	Iterator<?> iteratorState = null;
		        	iteratorState = tr.getStates().iterator();
		        	while (iteratorState.hasNext()) {
		        		State state = (State) iteratorState.next();
		        		if (state.getStateId().equals(shippingAddress.getState().getStateId())) {
		        			vector.add(tr);
		        			break;
		        		}
		        	}
        		}
        	}
        	String zipCode = shippingAddress.getCustZipCode();
        	if (!found && !Format.isNullOrEmpty(zipCode)) {
        		Iterator<?> iteratorZipCode = tr.getZipCodes().iterator();
        		while (iteratorZipCode.hasNext()) {
        			TaxRegionZip trZip = (TaxRegionZip) iteratorZipCode.next();
        			if (zipCode.compareTo(trZip.getZipCodeStart()) >= 0 &&
        				zipCode.compareTo(trZip.getZipCodeEnd()) <= 0) {
        				vector.add(tr);
        				break;
        			}
        		}
        	}
		}
		taxRegionList = new TaxRegion[vector.size()];
		vector.copyInto(taxRegionList);
	}
	
	public ItemTax[] calcTaxes(Long productClassId, TaxRegion trList[], float amount) {
		ItemTax itemTaxes[] = {};
		if (trList.length == 0) {
			return itemTaxes;
		}
		
		Vector<ItemTax> vector = new Vector<ItemTax>();
		for (TaxRegion taxRegion : trList) {
			boolean found = false;
			Iterator<?> iterator = null;
			iterator = taxRegion.getProductClasses().iterator();
			TaxRegionProduct txProduct = null;
			while (iterator.hasNext()) {
				txProduct = (TaxRegionProduct) iterator.next();
				if (txProduct.getProductClass().getProductClassId().equals(productClassId)) {
					found = true;
					break;
				}
			}
			if (!found) {
				 continue;
			}
			
			found = false;
			iterator = txProduct.getCustomerClasses().iterator();
			TaxRegionProductCust txProductCust = null;
			while (iterator.hasNext()) {
				txProductCust = (TaxRegionProductCust) iterator.next();
				if (txProductCust.getCustomerClass().getCustClassId().equals(customerClass.getCustClassId())) {
					found = true;
					break;
				}
			}
			if (!found) {
				continue;
			}
			
			Iterator<TaxRegionProductCustTax> txTaxes = txProductCust.getTaxes().iterator();
			int seqNum = -1;
			float total = amount;
			float taxAmount = 0;
			while (txTaxes.hasNext()) {
				TaxRegionProductCustTax txTax = (TaxRegionProductCustTax) txTaxes.next();
				Tax tax = txTax.getTax();
				if (tax.getPublished() != Constants.VALUE_YES) {
					continue;
				}
				if (txTax.getSeqNum() != seqNum) {
					total += taxAmount;
					taxAmount = 0;
				}
				ItemTax itemTax = new ItemTax();
				itemTax.setTax(tax);
				itemTax.setTaxRate(tax.getTaxRate().floatValue());
				itemTax.setTaxCode(tax.getTaxLanguage().getTaxCode());
				if (!contentSessionKey.isSiteProfileClassDefault()) {
					for (TaxLanguage language : tax.getTaxLanguages()) {
						if (language.getSiteProfileClass().getSiteProfileClassId().equals(contentSessionKey.getSiteProfileClassId())) {
							if (language.getTaxCode() != null) {
								itemTax.setTaxCode(language.getTaxCode());
							}
							break;
						}
					}
				}
				float value = Utility.round(tax.getTaxRate().floatValue() * total / 100, 2);
				itemTax.setTaxAmount(value);
				vector.add(itemTax);
				taxAmount += value;
				seqNum = txTax.getSeqNum();
			}
		}
		itemTaxes = new ItemTax[vector.size()];
		vector.copyInto(itemTaxes);
		return itemTaxes;
	}
	
	public ItemTax[] getShippingTaxes(float shippingAmount) {
		ItemTax itemTaxes[] = {};
		if (taxRegionList.length == 0) {
			return itemTaxes;
		}
		Vector<ItemTax> vector = new Vector<ItemTax>();
		for (TaxRegion taxRegion : taxRegionList) {
			ProductClass shippingProductClass = taxRegion.getShippingProductClass();
			if (shippingProductClass == null) {
				continue;
			}
			TaxRegion list[] = {taxRegion};
			ItemTax taxes[] = calcTaxes(shippingProductClass.getProductClassId(), list, shippingAmount);
			for (ItemTax itemTax : taxes) {
				Enumeration<ItemTax> enumeration = vector.elements();
				boolean found = false;
				while (enumeration.hasMoreElements()) {
					ItemTax masterTax = (ItemTax) enumeration.nextElement();
					if (itemTax.getTax().getTaxId().equals(masterTax.getTax().getTaxId())) {
						Float amount = masterTax.getTaxAmount() + itemTax.getTaxAmount();
						masterTax.setTaxAmount(amount);
						found = true;
						break;
					}
				}
				if (!found) {
					vector.add(itemTax);
				}
			}
		}
		
		itemTaxes = new ItemTax[vector.size()];
		vector.copyInto(itemTaxes);
		return itemTaxes;
	}
	
	public ItemTax[] getTaxes(Item item, float itemAmount) {
		ItemTax itemTaxes[] = {};
		if (taxRegionList.length == 0) {
			return itemTaxes;
		}
		
		Long productClassId = item.getProductClass().getProductClassId();
		return calcTaxes(productClassId, taxRegionList, itemAmount);
	}
}
