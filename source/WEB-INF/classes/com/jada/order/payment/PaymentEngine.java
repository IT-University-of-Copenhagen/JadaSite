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

package com.jada.order.payment;

import javax.servlet.http.HttpServletRequest;

import com.jada.content.ContentBean;
import com.jada.jpa.entity.CreditHeader;
import com.jada.jpa.entity.InvoiceHeader;
import com.jada.jpa.entity.OrderHeader;
import com.jada.order.cart.ShoppingCart;

public interface PaymentEngine {
	static public String CARDTYPE_VISA = "Visa";
	static public String CARDTYPE_AMERICAN_EXPRESS = "American Express";
	static public String CARDTYPE_MASTERCARD = "MasterCard";
	static public String CARDTYPE_DINERSCLUB = "Diners Club";
	static public String CARDTYPE_DISCOVER = "Discover";
	
	static public String MESSAGE_NOTIMPLEMENTED = "Not implemented";
	
	public boolean isProvideCustomer();
	public boolean isExtendedTransaction();
	public boolean isAllowAuthorizeOnly();
	public void callBack(HttpServletRequest request, ShoppingCart shoppingCart, ContentBean contentBean) throws PaymentException, Exception;
	public void cancelPayment(InvoiceHeader invoiceHeader) throws PaymentException, NotImplementedException, Exception;
	public void voidPayment(InvoiceHeader invoiceHeader) throws AuthorizationException, PaymentException, NotImplementedException, Exception;
	public void authorizePayment(OrderHeader orderHeader, HttpServletRequest request) throws PaymentException, NotImplementedException, Exception;
	public void authorizeAndCapturePayment(InvoiceHeader invoiceHeader, HttpServletRequest request) throws PaymentException, NotImplementedException, Exception;
	public void capturePayment(InvoiceHeader invoiceHeader) throws PaymentException, NotImplementedException, Exception;
	public void creditPayment(CreditHeader creditHeader) throws PaymentException, NotImplementedException, Exception;
	public void voidCredit(CreditHeader creditHeader) throws AuthorizationException, PaymentException, NotImplementedException, Exception;
	public void abort();
	public String getPaymentMessage();
	public String getPaymentType();
	public String getAuthCode();
	public String getPaymentReference1();
	public String getPaymentReference2();
	public String getPaymentReference3();
	public String getPaymentReference4();
	public String getPaymentReference5();
	public CreditCardInfo getCreditCardInfo();
	public void setCreditCardInfo(CreditCardInfo creditCardInfo);
	public boolean isBookQty();
}
