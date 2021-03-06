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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CustomerCreditCard generated by hbm2java
 */
@Entity
@Table(name = "customer_credit_card")
public class CustomerCreditCard implements java.io.Serializable {

	private static final long serialVersionUID = 6008534316905698L;
	private Long custCreditCardId;
	private String custCreditCardFullName;
	private String custCreditCardNum;
	private String custCreditCardExpiryMonth;
	private String custCreditCardExpiryYear;
	private String custCreditCardVerNum;
	private String recUpdateBy;
	private Date recUpdateDatetime;
	private String recCreateBy;
	private Date recCreateDatetime;
	private CreditCard creditCard;

	public CustomerCreditCard() {
	}

	public CustomerCreditCard(String custCreditCardFullName,
			String custCreditCardNum, String custCreditCardExpiryMonth,
			String custCreditCardExpiryYear, String recUpdateBy,
			Date recUpdateDatetime, String recCreateBy, Date recCreateDatetime) {
		this.custCreditCardFullName = custCreditCardFullName;
		this.custCreditCardNum = custCreditCardNum;
		this.custCreditCardExpiryMonth = custCreditCardExpiryMonth;
		this.custCreditCardExpiryYear = custCreditCardExpiryYear;
		this.recUpdateBy = recUpdateBy;
		this.recUpdateDatetime = recUpdateDatetime;
		this.recCreateBy = recCreateBy;
		this.recCreateDatetime = recCreateDatetime;
	}

	public CustomerCreditCard(String custCreditCardFullName,
			String custCreditCardNum, String custCreditCardExpiryMonth,
			String custCreditCardExpiryYear, String custCreditCardVerNum,
			String recUpdateBy, Date recUpdateDatetime, String recCreateBy,
			Date recCreateDatetime, CreditCard creditCard) {
		this.custCreditCardFullName = custCreditCardFullName;
		this.custCreditCardNum = custCreditCardNum;
		this.custCreditCardExpiryMonth = custCreditCardExpiryMonth;
		this.custCreditCardExpiryYear = custCreditCardExpiryYear;
		this.custCreditCardVerNum = custCreditCardVerNum;
		this.recUpdateBy = recUpdateBy;
		this.recUpdateDatetime = recUpdateDatetime;
		this.recCreateBy = recCreateBy;
		this.recCreateDatetime = recCreateDatetime;
		this.creditCard = creditCard;
	}

	@Id
	@GeneratedValue
	@Column(name = "cust_creditcard_id", nullable = false)
	public Long getCustCreditCardId() {
		return this.custCreditCardId;
	}

	public void setCustCreditCardId(Long custCreditCardId) {
		this.custCreditCardId = custCreditCardId;
	}

	@Column(name = "cust_creditcard_full_name", nullable = false, length = 40)
	public String getCustCreditCardFullName() {
		return this.custCreditCardFullName;
	}

	public void setCustCreditCardFullName(String custCreditCardFullName) {
		this.custCreditCardFullName = custCreditCardFullName;
	}

	@Column(name = "cust_creditcard_num", nullable = false, length = 192)
	public String getCustCreditCardNum() {
		return this.custCreditCardNum;
	}

	public void setCustCreditCardNum(String custCreditCardNum) {
		this.custCreditCardNum = custCreditCardNum;
	}

	@Column(name = "cust_creditcard_expiry_month", nullable = false, length = 2)
	public String getCustCreditCardExpiryMonth() {
		return this.custCreditCardExpiryMonth;
	}

	public void setCustCreditCardExpiryMonth(String custCreditCardExpiryMonth) {
		this.custCreditCardExpiryMonth = custCreditCardExpiryMonth;
	}

	@Column(name = "cust_creditcard_expiry_year", nullable = false, length = 4)
	public String getCustCreditCardExpiryYear() {
		return this.custCreditCardExpiryYear;
	}

	public void setCustCreditCardExpiryYear(String custCreditCardExpiryYear) {
		this.custCreditCardExpiryYear = custCreditCardExpiryYear;
	}

	@Column(name = "cust_creditcard_ver_num", length = 40)
	public String getCustCreditCardVerNum() {
		return this.custCreditCardVerNum;
	}

	public void setCustCreditCardVerNum(String custCreditCardVerNum) {
		this.custCreditCardVerNum = custCreditCardVerNum;
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

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "credit_card_id")
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

}
