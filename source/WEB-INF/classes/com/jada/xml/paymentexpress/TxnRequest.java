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

package com.jada.xml.paymentexpress;

public class TxnRequest {
	private String amount;
	private String avsAction;
	private String avsPostCode;
	private String avsStreetAddress;
	private String billingId;
	private String cardHolderName;
	private String cardNumber;
	private String cvc2;
	private String dateExpiry;
	private String dateStart;
	private String dpsBillingId;
	private String dpsTxnRef;
	private String enableAddBillCard;
	private String enableAvsData;
	private String inputCurrency;
	private String issueNumber;
	private String merchantReference;
	private String postPassword;
	private String postUsername;
	private String track2;
	private String txnData1;
	private String txnData2;
	private String txnData3;
	private String txnId;
	private String txnType;
	
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAvsAction() {
		return avsAction;
	}
	public void setAvsAction(String avsAction) {
		this.avsAction = avsAction;
	}
	public String getAvsPostCode() {
		return avsPostCode;
	}
	public void setAvsPostCode(String avsPostCode) {
		this.avsPostCode = avsPostCode;
	}
	public String getAvsStreetAddress() {
		return avsStreetAddress;
	}
	public void setAvsStreetAddress(String avsStreetAddress) {
		this.avsStreetAddress = avsStreetAddress;
	}
	public String getBillingId() {
		return billingId;
	}
	public void setBillingId(String billingId) {
		this.billingId = billingId;
	}
	public String getCardHolderName() {
		return cardHolderName;
	}
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCvc2() {
		return cvc2;
	}
	public void setCvc2(String cvc2) {
		this.cvc2 = cvc2;
	}
	public String getDateExpiry() {
		return dateExpiry;
	}
	public void setDateExpiry(String dateExpiry) {
		this.dateExpiry = dateExpiry;
	}
	public String getDateStart() {
		return dateStart;
	}
	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}
	public String getDpsBillingId() {
		return dpsBillingId;
	}
	public void setDpsBillingId(String dpsBillingId) {
		this.dpsBillingId = dpsBillingId;
	}
	public String getDpsTxnRef() {
		return dpsTxnRef;
	}
	public void setDpsTxnRef(String dpsTxnRef) {
		this.dpsTxnRef = dpsTxnRef;
	}
	public String getEnableAddBillCard() {
		return enableAddBillCard;
	}
	public void setEnableAddBillCard(String enableAddBillCard) {
		this.enableAddBillCard = enableAddBillCard;
	}
	public String getEnableAvsData() {
		return enableAvsData;
	}
	public void setEnableAvsData(String enableAvsData) {
		this.enableAvsData = enableAvsData;
	}
	public String getInputCurrency() {
		return inputCurrency;
	}
	public void setInputCurrency(String inputCurrency) {
		this.inputCurrency = inputCurrency;
	}
	public String getIssueNumber() {
		return issueNumber;
	}
	public void setIssueNumber(String issueNumber) {
		this.issueNumber = issueNumber;
	}
	public String getMerchantReference() {
		return merchantReference;
	}
	public void setMerchantReference(String merchantReference) {
		this.merchantReference = merchantReference;
	}
	public String getPostPassword() {
		return postPassword;
	}
	public void setPostPassword(String postPassword) {
		this.postPassword = postPassword;
	}
	public String getPostUsername() {
		return postUsername;
	}
	public void setPostUsername(String postUsername) {
		this.postUsername = postUsername;
	}
	public String getTrack2() {
		return track2;
	}
	public void setTrack2(String track2) {
		this.track2 = track2;
	}
	public String getTxnData1() {
		return txnData1;
	}
	public void setTxnData1(String txnData1) {
		this.txnData1 = txnData1;
	}
	public String getTxnData2() {
		return txnData2;
	}
	public void setTxnData2(String txnData2) {
		this.txnData2 = txnData2;
	}
	public String getTxnData3() {
		return txnData3;
	}
	public void setTxnData3(String txnData3) {
		this.txnData3 = txnData3;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
}
