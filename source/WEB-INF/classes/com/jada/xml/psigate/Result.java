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

package com.jada.xml.psigate;

public class Result {
	String transTime;
	String orderID;
	String approved;
	String returnCode;
	String errMsg;
	String taxTotal;
	String shipTotal;
	String subTotal;
	String fullTotal;
	String paymentType;
	String cardNumber;
	String cardExpMonth;
	String cardExpYear;
	String transRefNumber;
	String cardIDResult;
	String aVSResult;
	String cardAuthNumber;
	String cardRefNumber;
	String cardType;
	String iPResult;
	String iPCountry;
	String iPRegion;
	String iPCity;
	public String getApproved() {
		return approved;
	}
	public void setApproved(String approved) {
		this.approved = approved;
	}
	public String getAVSResult() {
		return aVSResult;
	}
	public void setAVSResult(String result) {
		aVSResult = result;
	}
	public String getCardAuthNumber() {
		return cardAuthNumber;
	}
	public void setCardAuthNumber(String cardAuthNumber) {
		this.cardAuthNumber = cardAuthNumber;
	}
	public String getCardExpMonth() {
		return cardExpMonth;
	}
	public void setCardExpMonth(String cardExpMonth) {
		this.cardExpMonth = cardExpMonth;
	}
	public String getCardExpYear() {
		return cardExpYear;
	}
	public void setCardExpYear(String cardExpYear) {
		this.cardExpYear = cardExpYear;
	}
	public String getCardIDResult() {
		return cardIDResult;
	}
	public void setCardIDResult(String cardIDResult) {
		this.cardIDResult = cardIDResult;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCardRefNumber() {
		return cardRefNumber;
	}
	public void setCardRefNumber(String cardRefNumber) {
		this.cardRefNumber = cardRefNumber;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getFullTotal() {
		return fullTotal;
	}
	public void setFullTotal(String fullTotal) {
		this.fullTotal = fullTotal;
	}
	public String getIPCity() {
		return iPCity;
	}
	public void setIPCity(String city) {
		iPCity = city;
	}
	public String getIPCountry() {
		return iPCountry;
	}
	public void setIPCountry(String country) {
		iPCountry = country;
	}
	public String getIPRegion() {
		return iPRegion;
	}
	public void setIPRegion(String region) {
		iPRegion = region;
	}
	public String getIPResult() {
		return iPResult;
	}
	public void setIPResult(String result) {
		iPResult = result;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getShipTotal() {
		return shipTotal;
	}
	public void setShipTotal(String shipTotal) {
		this.shipTotal = shipTotal;
	}
	public String getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}
	public String getTaxTotal() {
		return taxTotal;
	}
	public void setTaxTotal(String taxTotal) {
		this.taxTotal = taxTotal;
	}
	public String getTransRefNumber() {
		return transRefNumber;
	}
	public void setTransRefNumber(String transRefNumber) {
		this.transRefNumber = transRefNumber;
	}
	public String getTransTime() {
		return transTime;
	}
	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}
}
