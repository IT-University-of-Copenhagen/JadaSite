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

public class Transaction {
	private String authorized;
	private String reCo;
	private String rxDate;
	private String rxDateLocal;
	private String localTimeZone;
	private String merchantReference;
	private String cardName;
	private String retry;
	private String statusRequired;
	private String authCode;
	private String amountBalance;
	private String amount;
	private String currencyId;
	private String inputCurrencyId;
	private String inputCurrencyName;
	private String currencyRate;
	private String currencyName;
	private String cardHolderName;
	private String dateSettlement;
	private String txnType;
	private String cardNumber;
	private String txnMac;
	private String dateExpiry;
	private String productId;
	private String acquirerDate;
	private String acquirerTime;
	private String acquirerId;
	private String acquirer;
	private String acquirerReCo;
	private String acquirerResponseText;
	private String testMode;
	private String cardId;
	private String cardHolderResponseText;
	private String cardHolderHelpText;
	private String cardHolderResponseDescription;
	private String merchantResponseText;
	private String merchantHelpText;
	private String merchantResponseDescription;
	private String urlFail;
	private String urlSuccess;
	private String enablePostResponse;
	private String pxPayName;
	private String pxPayLogoSrc;
	private String pxPayUserId;
	private String pxPayXsl;
	private String pxPayBgColor;
	private String pxPayOptions;
	private String acquirerPort;
	private String acquirerTxnRef;
	private String groupAccount;
	private String dpsTxnRef;
	private String allowRetry;
	private String dpsBillingId;
	private String billingId;
	private String transactionId;
	private String pxHostId;

	public void setAuthorized(String authorized) {
		this.authorized = authorized;
	}
	public String getAuthorized() {
		return authorized;
	}
	public void setReCo(String reCo) {
		this.reCo = reCo;
	}
	public String getReCo() {
		return reCo;
	}
	public void setRxDate(String rxDate) {
		this.rxDate = rxDate;
	}
	public String getRxDate() {
		return rxDate;
	}
	public void setRxDateLocal(String rxDateLocal) {
		this.rxDateLocal = rxDateLocal;
	}
	public String getRxDateLocal() {
		return rxDateLocal;
	}
	public void setLocalTimeZone(String localTimeZone) {
		this.localTimeZone = localTimeZone;
	}
	public String getLocalTimeZone() {
		return localTimeZone;
	}
	public void setMerchantReference(String merchantReference) {
		this.merchantReference = merchantReference;
	}
	public String getMerchantReference() {
		return merchantReference;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getCardName() {
		return cardName;
	}
	public void setRetry(String retry) {
		this.retry = retry;
	}
	public String getRetry() {
		return retry;
	}
	public void setStatusRequired(String statusRequired) {
		this.statusRequired = statusRequired;
	}
	public String getStatusRequired() {
		return statusRequired;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAmountBalance(String amountBalance) {
		this.amountBalance = amountBalance;
	}
	public String getAmountBalance() {
		return amountBalance;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAmount() {
		return amount;
	}
	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}
	public String getCurrencyId() {
		return currencyId;
	}
	public void setInputCurrencyId(String inputCurrencyId) {
		this.inputCurrencyId = inputCurrencyId;
	}
	public String getInputCurrencyId() {
		return inputCurrencyId;
	}
	public void setInputCurrencyName(String inputCurrencyName) {
		this.inputCurrencyName = inputCurrencyName;
	}
	public String getInputCurrencyName() {
		return inputCurrencyName;
	}
	public void setCurrencyRate(String currencyRate) {
		this.currencyRate = currencyRate;
	}
	public String getCurrencyRate() {
		return currencyRate;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}
	public String getCardHolderName() {
		return cardHolderName;
	}
	public void setDateSettlement(String dateSettlement) {
		this.dateSettlement = dateSettlement;
	}
	public String getDateSettlement() {
		return dateSettlement;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setTxnMac(String txnMac) {
		this.txnMac = txnMac;
	}
	public String getTxnMac() {
		return txnMac;
	}
	public void setDateExpiry(String dateExpiry) {
		this.dateExpiry = dateExpiry;
	}
	public String getDateExpiry() {
		return dateExpiry;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductId() {
		return productId;
	}
	public void setAcquirerDate(String acquirerDate) {
		this.acquirerDate = acquirerDate;
	}
	public String getAcquirerDate() {
		return acquirerDate;
	}
	public void setAcquirerTime(String acquirerTime) {
		this.acquirerTime = acquirerTime;
	}
	public String getAcquirerTime() {
		return acquirerTime;
	}
	public void setAcquirerId(String acquirerId) {
		this.acquirerId = acquirerId;
	}
	public String getAcquirerId() {
		return acquirerId;
	}
	public void setAcquirer(String acquirer) {
		this.acquirer = acquirer;
	}
	public String getAcquirer() {
		return acquirer;
	}
	public void setAcquirerReCo(String acquirerReCo) {
		this.acquirerReCo = acquirerReCo;
	}
	public String getAcquirerReCo() {
		return acquirerReCo;
	}
	public void setAcquirerResponseText(String acquirerResponseText) {
		this.acquirerResponseText = acquirerResponseText;
	}
	public String getAcquirerResponseText() {
		return acquirerResponseText;
	}
	public void setTestMode(String testMode) {
		this.testMode = testMode;
	}
	public String getTestMode() {
		return testMode;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardHolderResponseText(String cardHolderResponseText) {
		this.cardHolderResponseText = cardHolderResponseText;
	}
	public String getCardHolderResponseText() {
		return cardHolderResponseText;
	}
	public void setCardHolderHelpText(String cardHolderHelpText) {
		this.cardHolderHelpText = cardHolderHelpText;
	}
	public String getCardHolderHelpText() {
		return cardHolderHelpText;
	}
	public void setCardHolderResponseDescription(String cardHolderResponseDescription) {
		this.cardHolderResponseDescription = cardHolderResponseDescription;
	}
	public String getCardHolderResponseDescription() {
		return cardHolderResponseDescription;
	}
	public void setMerchantResponseText(String merchantResponseText) {
		this.merchantResponseText = merchantResponseText;
	}
	public String getMerchantResponseText() {
		return merchantResponseText;
	}
	public void setMerchantHelpText(String merchantHelpText) {
		this.merchantHelpText = merchantHelpText;
	}
	public String getMerchantHelpText() {
		return merchantHelpText;
	}
	public void setMerchantResponseDescription(String merchantResponseDescription) {
		this.merchantResponseDescription = merchantResponseDescription;
	}
	public String getMerchantResponseDescription() {
		return merchantResponseDescription;
	}
	public void setUrlFail(String urlFail) {
		this.urlFail = urlFail;
	}
	public String getUrlFail() {
		return urlFail;
	}
	public void setUrlSuccess(String urlSuccess) {
		this.urlSuccess = urlSuccess;
	}
	public String getUrlSuccess() {
		return urlSuccess;
	}
	public void setEnablePostResponse(String enablePostResponse) {
		this.enablePostResponse = enablePostResponse;
	}
	public String getEnablePostResponse() {
		return enablePostResponse;
	}
	public void setPxPayName(String pxPayName) {
		this.pxPayName = pxPayName;
	}
	public String getPxPayName() {
		return pxPayName;
	}
	public void setPxPayLogoSrc(String pxPayLogoSrc) {
		this.pxPayLogoSrc = pxPayLogoSrc;
	}
	public String getPxPayLogoSrc() {
		return pxPayLogoSrc;
	}
	public void setPxPayUserId(String pxPayUserId) {
		this.pxPayUserId = pxPayUserId;
	}
	public String getPxPayUserId() {
		return pxPayUserId;
	}
	public void setPxPayXsl(String pxPayXsl) {
		this.pxPayXsl = pxPayXsl;
	}
	public String getPxPayXsl() {
		return pxPayXsl;
	}
	public void setPxPayBgColor(String pxPayBgColor) {
		this.pxPayBgColor = pxPayBgColor;
	}
	public String getPxPayBgColor() {
		return pxPayBgColor;
	}
	public void setPxPayOptions(String pxPayOptions) {
		this.pxPayOptions = pxPayOptions;
	}
	public String getPxPayOptions() {
		return pxPayOptions;
	}
	public void setAcquirerPort(String acquirerPort) {
		this.acquirerPort = acquirerPort;
	}
	public String getAcquirerPort() {
		return acquirerPort;
	}
	public void setAcquirerTxnRef(String acquirerTxnRef) {
		this.acquirerTxnRef = acquirerTxnRef;
	}
	public String getAcquirerTxnRef() {
		return acquirerTxnRef;
	}
	public void setGroupAccount(String groupAccount) {
		this.groupAccount = groupAccount;
	}
	public String getGroupAccount() {
		return groupAccount;
	}
	public void setDpsTxnRef(String dpsTxnRef) {
		this.dpsTxnRef = dpsTxnRef;
	}
	public String getDpsTxnRef() {
		return dpsTxnRef;
	}
	public void setAllowRetry(String allowRetry) {
		this.allowRetry = allowRetry;
	}
	public String getAllowRetry() {
		return allowRetry;
	}
	public void setDpsBillingId(String dpsBillingId) {
		this.dpsBillingId = dpsBillingId;
	}
	public String getDpsBillingId() {
		return dpsBillingId;
	}
	public void setBillingId(String billingId) {
		this.billingId = billingId;
	}
	public String getBillingId() {
		return billingId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setPxHostId(String pxHostId) {
		this.pxHostId = pxHostId;
	}
	public String getPxHostId() {
		return pxHostId;
	}	
}
