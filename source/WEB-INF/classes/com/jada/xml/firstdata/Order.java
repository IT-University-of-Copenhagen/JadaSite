package com.jada.xml.firstdata;

public class Order {
	MerchantInfo merchantInfo;
	OrderOptions orderOptions;
	Payment payment;
	CreditCard creditCard;
	TransactionDetails transactionDetails;
	public MerchantInfo getMerchantInfo() {
		return merchantInfo;
	}
	public void setMerchantInfo(MerchantInfo merchantInfo) {
		this.merchantInfo = merchantInfo;
	}
	public OrderOptions getOrderOptions() {
		return orderOptions;
	}
	public void setOrderOptions(OrderOptions orderOptions) {
		this.orderOptions = orderOptions;
	}
	public Payment getPayment() {
		return payment;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	public CreditCard getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}
	public TransactionDetails getTransactionDetails() {
		return transactionDetails;
	}
	public void setTransactionDetails(TransactionDetails transactionDetails) {
		this.transactionDetails = transactionDetails;
	}
}
