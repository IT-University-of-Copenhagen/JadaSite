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

package com.jada.dao;

import java.util.Date;

import javax.persistence.EntityManager;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.CreditCard;

public class CreditCardDAO extends CreditCard {
	private static final long serialVersionUID = -3561923472800607825L;
	private Long creditCardId;
	private String siteId;
	private int seqNum;
	private String creditCardDesc;
	private String recUpdateBy;
	private Date recUpdateDatetime;
	private String recCreateBy;
	private Date recCreateDatetime;

	public static CreditCard load(String siteId, Long creditCardId) throws SecurityException, Exception {
    	EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		CreditCard creditcard = (CreditCard) em.find(CreditCard.class, creditCardId);
		if (!creditcard.getSiteId().equals(siteId)) {
			throw new SecurityException();
		}
		return creditcard;
	}
	
	public CreditCardDAO(CreditCard creditCard) {
		this.creditCardId = creditCard.getCreditCardId();
		this.siteId = creditCard.getSiteId();
		this.seqNum = creditCard.getSeqNum();
		this.creditCardDesc = creditCard.getCreditCardDesc();
		this.recUpdateBy = creditCard.getRecUpdateBy();
		this.recUpdateDatetime = creditCard.getRecUpdateDatetime();
		this.recCreateBy = creditCard.getRecCreateBy();
		this.recCreateDatetime = creditCard.getRecCreateDatetime();
	}

	public String getCreditCardDesc() {
		return creditCardDesc;
	}
	public void setCreditCardDesc(String creditCardDesc) {
		this.creditCardDesc = creditCardDesc;
	}
	public Long getCreditCardId() {
		return creditCardId;
	}
	public void setCreditCardId(Long creditCardId) {
		this.creditCardId = creditCardId;
	}
	public String getRecCreateBy() {
		return recCreateBy;
	}
	public void setRecCreateBy(String recCreateBy) {
		this.recCreateBy = recCreateBy;
	}
	public Date getRecCreateDatetime() {
		return recCreateDatetime;
	}
	public void setRecCreateDatetime(Date recCreateDatetime) {
		this.recCreateDatetime = recCreateDatetime;
	}
	public String getRecUpdateBy() {
		return recUpdateBy;
	}
	public void setRecUpdateBy(String recUpdateBy) {
		this.recUpdateBy = recUpdateBy;
	}
	public Date getRecUpdateDatetime() {
		return recUpdateDatetime;
	}
	public void setRecUpdateDatetime(Date recUpdateDatetime) {
		this.recUpdateDatetime = recUpdateDatetime;
	}
	public int getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
}
