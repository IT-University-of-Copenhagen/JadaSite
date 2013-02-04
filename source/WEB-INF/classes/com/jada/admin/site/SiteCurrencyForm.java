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

package com.jada.admin.site;

public class SiteCurrencyForm {
	boolean locked;
	boolean remove;
	boolean cashPayment;
	String siteCurrencyId;
	String siteCurrencyClassId;
	String siteCurrencyClassIdError;
	String seqNum;
	String seqNumError;
	String exchangeRate;
	String exchangeRateError;
	String paymentGatewayId;
	String payPalPaymentGatewayId;
	boolean active;
	public boolean isRemove() {
		return remove;
	}
	public void setRemove(boolean remove) {
		this.remove = remove;
	}
	public String getSiteCurrencyId() {
		return siteCurrencyId;
	}
	public void setSiteCurrencyId(String siteCurrencyId) {
		this.siteCurrencyId = siteCurrencyId;
	}
	public String getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public String getExchangeRateError() {
		return exchangeRateError;
	}
	public void setExchangeRateError(String exchangeRateError) {
		this.exchangeRateError = exchangeRateError;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getSiteCurrencyClassId() {
		return siteCurrencyClassId;
	}
	public void setSiteCurrencyClassId(String siteCurrencyClassId) {
		this.siteCurrencyClassId = siteCurrencyClassId;
	}
	public String getPaymentGatewayId() {
		return paymentGatewayId;
	}
	public void setPaymentGatewayId(String paymentGatewayId) {
		this.paymentGatewayId = paymentGatewayId;
	}
	public String getSiteCurrencyClassIdError() {
		return siteCurrencyClassIdError;
	}
	public void setSiteCurrencyClassIdError(String siteCurrencyClassIdError) {
		this.siteCurrencyClassIdError = siteCurrencyClassIdError;
	}
	public String getPayPalPaymentGatewayId() {
		return payPalPaymentGatewayId;
	}
	public void setPayPalPaymentGatewayId(String payPalPaymentGatewayId) {
		this.payPalPaymentGatewayId = payPalPaymentGatewayId;
	}
	public String getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}
	public String getSeqNumError() {
		return seqNumError;
	}
	public void setSeqNumError(String seqNumError) {
		this.seqNumError = seqNumError;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	public boolean isCashPayment() {
		return cashPayment;
	}
	public void setCashPayment(boolean cashPayment) {
		this.cashPayment = cashPayment;
	}
}
