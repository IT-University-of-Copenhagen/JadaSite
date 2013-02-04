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

import com.jada.xml.paymentexpress.Transaction;

public class TxnResult {
	Transaction transaction[];
	private String reCo;
	private String responseText;
	private String helpText;
	private String success;
	private String dpsTxnRef;
	private String txnRef;

	public Transaction[] getTransaction() {
		return transaction;
	}
	public void setTransaction(Transaction[] transaction) {
		this.transaction = transaction;
	}
	public void setReCo(String reCo) {
		this.reCo = reCo;
	}
	public String getReCo() {
		return reCo;
	}
	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}
	public String getResponseText() {
		return responseText;
	}
	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}
	public String getHelpText() {
		return helpText;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getSuccess() {
		return success;
	}
	public void setDpsTxnRef(String dpsTxnRef) {
		this.dpsTxnRef = dpsTxnRef;
	}
	public String getDpsTxnRef() {
		return dpsTxnRef;
	}
	public void setTxnRef(String txnRef) {
		this.txnRef = txnRef;
	}
	public String getTxnRef() {
		return txnRef;
	}
}
