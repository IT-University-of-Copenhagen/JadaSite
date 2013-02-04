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

package com.jada.admin.paymentGateway;

import org.apache.struts.util.LabelValueBean;

import com.jada.admin.AdminMaintActionForm;

public class PaymentGatewayMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = 852534914053416787L;
	String mode;
	String paymentGatewayId;
	String paymentGatewayName;
	String paymentGatewayProvider;
	String authorizeNetLoginId;
	String authorizeNetTranKey;
	String authorizeNetEnvironment;
	String psiGateStoreId;
	String psiGatePassPhrase;
	String psiGateEnvironment;
	String paymentPaypalApiUsername;
	String paymentPaypalApiPassword;
	String paymentPaypalSignature;
	String paymentPaypalEnvironment;
	String paymentPaypalExtraAmount;
	String paymentPaypalExtraPercentage;
	String paymentPaypalCustClassId;
	String paymentExpressPostUsername;
	String paymentExpressPostPassword;
	String paymentExpressEnvironment;
	String paypalPayFlowUser;
	String paypalPayFlowVendor;
	String paypalPayFlowPartner;
	String paypalPayFlowPassword;
	String paypalPayFlowEnvironment;
	String paypalWebsitePaymentProApiUsername;
	String paypalWebsitePaymentProApiPassword;
	String paypalWebsitePaymentProSignature;
	String paypalWebsitePaymentProEnvironment;
	String paypalWebsitePaymentProHostedBodyBgColor;
	String paypalWebsitePaymentProHostedBodyBgImg;
	String paypalWebsitePaymentProHostedFooterTextColor;
	String paypalWebsitePaymentProHostedHeaderBgColor;
	String paypalWebsitePaymentProHostedHeaderHeight;
	String paypalWebsitePaymentProHostedLogoFont;
	String paypalWebsitePaymentProHostedLogoFontColor;
	String paypalWebsitePaymentProHostedLogoFontSize;
	String paypalWebsitePaymentProHostedLogoImage;
	String paypalWebsitePaymentProHostedLogoImagePosition;
	String paypalWebsitePaymentProHostedApiUsername;
	String paypalWebsitePaymentProHostedApiPassword;
	String paypalWebsitePaymentProHostedSignature;
	String paypalWebsitePaymentProHostedEnvironment;
	String eWayCVNAustraliaCustomerId;
	String eWayCVNAustraliaEnvironment;
	String firstDataPassword;
	String firstDataStoreNum;
	String firstDataKeyFile;
	String firstDataHostName;
	String firstDataHostPort;
	LabelValueBean customerClasses[];
	LabelValueBean paymentGateways[];
	public String getPaymentGatewayId() {
		return paymentGatewayId;
	}
	public void setPaymentGatewayId(String paymentGatewayId) {
		this.paymentGatewayId = paymentGatewayId;
	}
	public String getPaymentGatewayName() {
		return paymentGatewayName;
	}
	public void setPaymentGatewayName(String paymentGatewayName) {
		this.paymentGatewayName = paymentGatewayName;
	}
	public String getPaymentGatewayProvider() {
		return paymentGatewayProvider;
	}
	public void setPaymentGatewayProvider(String paymentGatewayProvider) {
		this.paymentGatewayProvider = paymentGatewayProvider;
	}
	public String getAuthorizeNetLoginId() {
		return authorizeNetLoginId;
	}
	public void setAuthorizeNetLoginId(String authorizeNetLoginId) {
		this.authorizeNetLoginId = authorizeNetLoginId;
	}
	public String getAuthorizeNetEnvironment() {
		return authorizeNetEnvironment;
	}
	public void setAuthorizeNetEnvironment(String authorizeNetEnvironment) {
		this.authorizeNetEnvironment = authorizeNetEnvironment;
	}
	public String getPsiGateStoreId() {
		return psiGateStoreId;
	}
	public void setPsiGateStoreId(String psiGateStoreId) {
		this.psiGateStoreId = psiGateStoreId;
	}
	public String getPsiGatePassPhrase() {
		return psiGatePassPhrase;
	}
	public void setPsiGatePassPhrase(String psiGatePassPhrase) {
		this.psiGatePassPhrase = psiGatePassPhrase;
	}
	public String getPsiGateEnvironment() {
		return psiGateEnvironment;
	}
	public void setPsiGateEnvironment(String psiGateEnvironment) {
		this.psiGateEnvironment = psiGateEnvironment;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getAuthorizeNetTranKey() {
		return authorizeNetTranKey;
	}
	public void setAuthorizeNetTranKey(String authorizeNetTranKey) {
		this.authorizeNetTranKey = authorizeNetTranKey;
	}
	public LabelValueBean[] getPaymentGateways() {
		return paymentGateways;
	}
	public void setPaymentGateways(LabelValueBean[] paymentGateways) {
		this.paymentGateways = paymentGateways;
	}
	public String getPaymentPaypalApiUsername() {
		return paymentPaypalApiUsername;
	}
	public void setPaymentPaypalApiUsername(String paymentPaypalApiUsername) {
		this.paymentPaypalApiUsername = paymentPaypalApiUsername;
	}
	public String getPaymentPaypalApiPassword() {
		return paymentPaypalApiPassword;
	}
	public void setPaymentPaypalApiPassword(String paymentPaypalApiPassword) {
		this.paymentPaypalApiPassword = paymentPaypalApiPassword;
	}
	public String getPaymentPaypalSignature() {
		return paymentPaypalSignature;
	}
	public void setPaymentPaypalSignature(String paymentPaypalSignature) {
		this.paymentPaypalSignature = paymentPaypalSignature;
	}
	public String getPaymentPaypalEnvironment() {
		return paymentPaypalEnvironment;
	}
	public void setPaymentPaypalEnvironment(String paymentPaypalEnvironment) {
		this.paymentPaypalEnvironment = paymentPaypalEnvironment;
	}
	public String getPaymentPaypalExtraAmount() {
		return paymentPaypalExtraAmount;
	}
	public void setPaymentPaypalExtraAmount(String paymentPaypalExtraAmount) {
		this.paymentPaypalExtraAmount = paymentPaypalExtraAmount;
	}
	public String getPaymentPaypalExtraPercentage() {
		return paymentPaypalExtraPercentage;
	}
	public void setPaymentPaypalExtraPercentage(String paymentPaypalExtraPercentage) {
		this.paymentPaypalExtraPercentage = paymentPaypalExtraPercentage;
	}
	public String getPaymentPaypalCustClassId() {
		return paymentPaypalCustClassId;
	}
	public void setPaymentPaypalCustClassId(String paymentPaypalCustClassId) {
		this.paymentPaypalCustClassId = paymentPaypalCustClassId;
	}
	public LabelValueBean[] getCustomerClasses() {
		return customerClasses;
	}
	public void setCustomerClasses(LabelValueBean[] customerClasses) {
		this.customerClasses = customerClasses;
	}
	public void setPaymentExpressPostUsername(String paymentExpressPostUsername) {
		this.paymentExpressPostUsername = paymentExpressPostUsername;
	}
	public String getPaymentExpressPostUsername() {
		return paymentExpressPostUsername;
	}
	public void setPaymentExpressPostPassword(String paymentExpressPostPassword) {
		this.paymentExpressPostPassword = paymentExpressPostPassword;
	}
	public String getPaymentExpressPostPassword() {
		return paymentExpressPostPassword;
	}
	public void setPaymentExpressEnvironment(String paymentExpressEnvironment) {
		this.paymentExpressEnvironment = paymentExpressEnvironment;
	}
	public String getPaymentExpressEnvironment() {
		return paymentExpressEnvironment;
	}
	public String getPaypalPayFlowUser() {
		return paypalPayFlowUser;
	}
	public void setPaypalPayFlowUser(String paypalPayFlowUser) {
		this.paypalPayFlowUser = paypalPayFlowUser;
	}
	public String getPaypalPayFlowVendor() {
		return paypalPayFlowVendor;
	}
	public void setPaypalPayFlowVendor(String paypalPayFlowVendor) {
		this.paypalPayFlowVendor = paypalPayFlowVendor;
	}
	public String getPaypalPayFlowPartner() {
		return paypalPayFlowPartner;
	}
	public void setPaypalPayFlowPartner(String paypalPayFlowPartner) {
		this.paypalPayFlowPartner = paypalPayFlowPartner;
	}
	public String getPaypalPayFlowPassword() {
		return paypalPayFlowPassword;
	}
	public void setPaypalPayFlowPassword(String paypalPayFlowPassword) {
		this.paypalPayFlowPassword = paypalPayFlowPassword;
	}
	public String getPaypalPayFlowEnvironment() {
		return paypalPayFlowEnvironment;
	}
	public void setPaypalPayFlowEnvironment(String paypalPayFlowEnvironment) {
		this.paypalPayFlowEnvironment = paypalPayFlowEnvironment;
	}
	public String getPaypalWebsitePaymentProApiUsername() {
		return paypalWebsitePaymentProApiUsername;
	}
	public void setPaypalWebsitePaymentProApiUsername(
			String paypalWebsitePaymentProApiUsername) {
		this.paypalWebsitePaymentProApiUsername = paypalWebsitePaymentProApiUsername;
	}
	public String getPaypalWebsitePaymentProApiPassword() {
		return paypalWebsitePaymentProApiPassword;
	}
	public void setPaypalWebsitePaymentProApiPassword(
			String paypalWebsitePaymentProApiPassword) {
		this.paypalWebsitePaymentProApiPassword = paypalWebsitePaymentProApiPassword;
	}
	public String getPaypalWebsitePaymentProSignature() {
		return paypalWebsitePaymentProSignature;
	}
	public void setPaypalWebsitePaymentProSignature(
			String paypalWebsitePaymentProSignature) {
		this.paypalWebsitePaymentProSignature = paypalWebsitePaymentProSignature;
	}
	public String getPaypalWebsitePaymentProEnvironment() {
		return paypalWebsitePaymentProEnvironment;
	}
	public void setPaypalWebsitePaymentProEnvironment(
			String paypalWebsitePaymentProEnvironment) {
		this.paypalWebsitePaymentProEnvironment = paypalWebsitePaymentProEnvironment;
	}
	public String getEWayCVNAustraliaCustomerId() {
		return eWayCVNAustraliaCustomerId;
	}
	public void setEWayCVNAustraliaCustomerId(String eWayCVNAustraliaCustomerId) {
		this.eWayCVNAustraliaCustomerId = eWayCVNAustraliaCustomerId;
	}
	public String getEWayCVNAustraliaEnvironment() {
		return eWayCVNAustraliaEnvironment;
	}
	public void setEWayCVNAustraliaEnvironment(String eWayCVNAustraliaEnvironment) {
		this.eWayCVNAustraliaEnvironment = eWayCVNAustraliaEnvironment;
	}
	public String getFirstDataKeyFile() {
		return firstDataKeyFile;
	}
	public void setFirstDataKeyFile(String firstDataKeyFile) {
		this.firstDataKeyFile = firstDataKeyFile;
	}
	public String getFirstDataHostName() {
		return firstDataHostName;
	}
	public void setFirstDataHostName(String firstDataHostName) {
		this.firstDataHostName = firstDataHostName;
	}
	public String getFirstDataHostPort() {
		return firstDataHostPort;
	}
	public void setFirstDataHostPort(String firstDataHostPort) {
		this.firstDataHostPort = firstDataHostPort;
	}
	public String getFirstDataPassword() {
		return firstDataPassword;
	}
	public void setFirstDataPassword(String firstDataPassword) {
		this.firstDataPassword = firstDataPassword;
	}
	public String getFirstDataStoreNum() {
		return firstDataStoreNum;
	}
	public void setFirstDataStoreNum(String firstDataStoreNum) {
		this.firstDataStoreNum = firstDataStoreNum;
	}
	public String getPaypalWebsitePaymentProHostedApiUsername() {
		return paypalWebsitePaymentProHostedApiUsername;
	}
	public void setPaypalWebsitePaymentProHostedApiUsername(
			String paypalWebsitePaymentProHostedApiUsername) {
		this.paypalWebsitePaymentProHostedApiUsername = paypalWebsitePaymentProHostedApiUsername;
	}
	public String getPaypalWebsitePaymentProHostedApiPassword() {
		return paypalWebsitePaymentProHostedApiPassword;
	}
	public void setPaypalWebsitePaymentProHostedApiPassword(
			String paypalWebsitePaymentProHostedApiPassword) {
		this.paypalWebsitePaymentProHostedApiPassword = paypalWebsitePaymentProHostedApiPassword;
	}
	public String getPaypalWebsitePaymentProHostedSignature() {
		return paypalWebsitePaymentProHostedSignature;
	}
	public void setPaypalWebsitePaymentProHostedSignature(
			String paypalWebsitePaymentProHostedSignature) {
		this.paypalWebsitePaymentProHostedSignature = paypalWebsitePaymentProHostedSignature;
	}
	public String getPaypalWebsitePaymentProHostedEnvironment() {
		return paypalWebsitePaymentProHostedEnvironment;
	}
	public void setPaypalWebsitePaymentProHostedEnvironment(
			String paypalWebsitePaymentProHostedEnvironment) {
		this.paypalWebsitePaymentProHostedEnvironment = paypalWebsitePaymentProHostedEnvironment;
	}
	public String getPaypalWebsitePaymentProHostedLogoImage() {
		return paypalWebsitePaymentProHostedLogoImage;
	}
	public void setPaypalWebsitePaymentProHostedLogoImage(
			String paypalWebsitePaymentProHostedLogoImage) {
		this.paypalWebsitePaymentProHostedLogoImage = paypalWebsitePaymentProHostedLogoImage;
	}
	public String getPaypalWebsitePaymentProHostedLogoImagePosition() {
		return paypalWebsitePaymentProHostedLogoImagePosition;
	}
	public void setPaypalWebsitePaymentProHostedLogoImagePosition(
			String paypalWebsitePaymentProHostedLogoImagePosition) {
		this.paypalWebsitePaymentProHostedLogoImagePosition = paypalWebsitePaymentProHostedLogoImagePosition;
	}
	public String getPaypalWebsitePaymentProHostedBodyBgColor() {
		return paypalWebsitePaymentProHostedBodyBgColor;
	}
	public void setPaypalWebsitePaymentProHostedBodyBgColor(
			String paypalWebsitePaymentProHostedBodyBgColor) {
		this.paypalWebsitePaymentProHostedBodyBgColor = paypalWebsitePaymentProHostedBodyBgColor;
	}
	public String getPaypalWebsitePaymentProHostedBodyBgImg() {
		return paypalWebsitePaymentProHostedBodyBgImg;
	}
	public void setPaypalWebsitePaymentProHostedBodyBgImg(
			String paypalWebsitePaymentProHostedBodyBgImg) {
		this.paypalWebsitePaymentProHostedBodyBgImg = paypalWebsitePaymentProHostedBodyBgImg;
	}
	public String getPaypalWebsitePaymentProHostedFooterTextColor() {
		return paypalWebsitePaymentProHostedFooterTextColor;
	}
	public void setPaypalWebsitePaymentProHostedFooterTextColor(
			String paypalWebsitePaymentProHostedFooterTextColor) {
		this.paypalWebsitePaymentProHostedFooterTextColor = paypalWebsitePaymentProHostedFooterTextColor;
	}
	public String getPaypalWebsitePaymentProHostedHeaderBgColor() {
		return paypalWebsitePaymentProHostedHeaderBgColor;
	}
	public void setPaypalWebsitePaymentProHostedHeaderBgColor(
			String paypalWebsitePaymentProHostedHeaderBgColor) {
		this.paypalWebsitePaymentProHostedHeaderBgColor = paypalWebsitePaymentProHostedHeaderBgColor;
	}
	public String getPaypalWebsitePaymentProHostedHeaderHeight() {
		return paypalWebsitePaymentProHostedHeaderHeight;
	}
	public void setPaypalWebsitePaymentProHostedHeaderHeight(
			String paypalWebsitePaymentProHostedHeaderHeight) {
		this.paypalWebsitePaymentProHostedHeaderHeight = paypalWebsitePaymentProHostedHeaderHeight;
	}
	public String getPaypalWebsitePaymentProHostedLogoFont() {
		return paypalWebsitePaymentProHostedLogoFont;
	}
	public void setPaypalWebsitePaymentProHostedLogoFont(
			String paypalWebsitePaymentProHostedLogoFont) {
		this.paypalWebsitePaymentProHostedLogoFont = paypalWebsitePaymentProHostedLogoFont;
	}
	public String getPaypalWebsitePaymentProHostedLogoFontColor() {
		return paypalWebsitePaymentProHostedLogoFontColor;
	}
	public void setPaypalWebsitePaymentProHostedLogoFontColor(
			String paypalWebsitePaymentProHostedLogoFontColor) {
		this.paypalWebsitePaymentProHostedLogoFontColor = paypalWebsitePaymentProHostedLogoFontColor;
	}
	public String getPaypalWebsitePaymentProHostedLogoFontSize() {
		return paypalWebsitePaymentProHostedLogoFontSize;
	}
	public void setPaypalWebsitePaymentProHostedLogoFontSize(
			String paypalWebsitePaymentProHostedLogoFontSize) {
		this.paypalWebsitePaymentProHostedLogoFontSize = paypalWebsitePaymentProHostedLogoFontSize;
	}
}
