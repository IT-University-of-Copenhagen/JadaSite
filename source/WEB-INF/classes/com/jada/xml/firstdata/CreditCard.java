package com.jada.xml.firstdata;

public class CreditCard {
	String cardNumber;
	String cardExpMonth;
	String cardExpYear;
	String cvmValue;
	String cvmIndicator;
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
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
	public String getCvmValue() {
		return cvmValue;
	}
	public void setCvmValue(String cvmValue) {
		this.cvmValue = cvmValue;
	}
	public String getCvmIndicator() {
		return cvmIndicator;
	}
	public void setCvmIndicator(String cvmIndicator) {
		this.cvmIndicator = cvmIndicator;
	}
}
