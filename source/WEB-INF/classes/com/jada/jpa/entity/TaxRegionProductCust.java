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

package com.jada.jpa.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TaxRegionProductCust generated by hbm2java
 */
@Entity
@Table(name = "tax_region_product_cust")
public class TaxRegionProductCust implements java.io.Serializable {

	private static final long serialVersionUID = -4119960571709209636L;
	private Long taxRegionProductCustId;
	private String recUpdateBy;
	private Date recUpdateDatetime;
	private String recCreateBy;
	private Date recCreateDatetime;
	private TaxRegionProduct taxRegionProduct;
	private CustomerClass customerClass;
	private Set<TaxRegionProductCustTax> taxes = new HashSet<TaxRegionProductCustTax>(
			0);

	public TaxRegionProductCust() {
	}

	public TaxRegionProductCust(String recUpdateBy, Date recUpdateDatetime,
			String recCreateBy, Date recCreateDatetime) {
		this.recUpdateBy = recUpdateBy;
		this.recUpdateDatetime = recUpdateDatetime;
		this.recCreateBy = recCreateBy;
		this.recCreateDatetime = recCreateDatetime;
	}

	public TaxRegionProductCust(String recUpdateBy, Date recUpdateDatetime,
			String recCreateBy, Date recCreateDatetime,
			TaxRegionProduct taxRegionProduct, CustomerClass customerClass,
			Set<TaxRegionProductCustTax> taxes) {
		this.recUpdateBy = recUpdateBy;
		this.recUpdateDatetime = recUpdateDatetime;
		this.recCreateBy = recCreateBy;
		this.recCreateDatetime = recCreateDatetime;
		this.taxRegionProduct = taxRegionProduct;
		this.customerClass = customerClass;
		this.taxes = taxes;
	}

	@Id
	@GeneratedValue
	@Column(name = "tax_region_product_cust_id", nullable = false)
	public Long getTaxRegionProductCustId() {
		return this.taxRegionProductCustId;
	}

	public void setTaxRegionProductCustId(Long taxRegionProductCustId) {
		this.taxRegionProductCustId = taxRegionProductCustId;
	}

	@Column(name = "rec_update_by", nullable = false, length = 20)
	public String getRecUpdateBy() {
		return this.recUpdateBy;
	}

	public void setRecUpdateBy(String recUpdateBy) {
		this.recUpdateBy = recUpdateBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "rec_update_datetime", nullable = false)
	public Date getRecUpdateDatetime() {
		return this.recUpdateDatetime;
	}

	public void setRecUpdateDatetime(Date recUpdateDatetime) {
		this.recUpdateDatetime = recUpdateDatetime;
	}

	@Column(name = "rec_create_by", nullable = false, length = 20)
	public String getRecCreateBy() {
		return this.recCreateBy;
	}

	public void setRecCreateBy(String recCreateBy) {
		this.recCreateBy = recCreateBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "rec_create_datetime", nullable = false)
	public Date getRecCreateDatetime() {
		return this.recCreateDatetime;
	}

	public void setRecCreateDatetime(Date recCreateDatetime) {
		this.recCreateDatetime = recCreateDatetime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_region_product_id")
	public TaxRegionProduct getTaxRegionProduct() {
		return this.taxRegionProduct;
	}

	public void setTaxRegionProduct(TaxRegionProduct taxRegionProduct) {
		this.taxRegionProduct = taxRegionProduct;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_class_id")
	public CustomerClass getCustomerClass() {
		return this.customerClass;
	}

	public void setCustomerClass(CustomerClass customerClass) {
		this.customerClass = customerClass;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy(value="taxRegionProductCustTaxId")
	@JoinColumn(name = "tax_region_product_cust_id", updatable = false)
	public Set<TaxRegionProductCustTax> getTaxes() {
		return this.taxes;
	}

	public void setTaxes(Set<TaxRegionProductCustTax> taxes) {
		this.taxes = taxes;
	}

}
