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

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminMaintActionForm;

public class SiteMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = 221537019829152052L;
	String mode;
	String siteId;
	String siteDesc;
	boolean shareInventory;
	boolean manageInventory;
	boolean singleCheckout;
	String listingPageSize;
	String mailSmtpHost;
	String mailSmtpPort;
	String mailSmtpAccount;
	String mailSmtpPassword;
	String systemRecord;
	boolean enableCaptcha;
	String captchaPrivateKey;
	String captchaPublicKey;
	boolean storeCreditCard;
	String bingClientId;
	String bingClientSecert;
	boolean active;
	String siteDomainIds[];
	SiteDomainDisplayForm siteDomains[];
    Logger logger = Logger.getLogger(SiteMaintActionForm.class);
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		shareInventory = false;
		manageInventory = false;
		singleCheckout = false;
		enableCaptcha = false;
		storeCreditCard = false;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getSiteDesc() {
		return siteDesc;
	}
	public void setSiteDesc(String siteDesc) {
		this.siteDesc = siteDesc;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public SiteDomainDisplayForm[] getSiteDomains() {
		return siteDomains;
	}
	public void setSiteDomains(SiteDomainDisplayForm[] siteDomains) {
		this.siteDomains = siteDomains;
	}
	public boolean isShareInventory() {
		return shareInventory;
	}
	public void setShareInventory(boolean shareInventory) {
		this.shareInventory = shareInventory;
	}
	public String[] getSiteDomainIds() {
		return siteDomainIds;
	}
	public void setSiteDomainIds(String[] siteDomainIds) {
		this.siteDomainIds = siteDomainIds;
	}
	public String getListingPageSize() {
		return listingPageSize;
	}
	public void setListingPageSize(String listingPageSize) {
		this.listingPageSize = listingPageSize;
	}
	public String getMailSmtpHost() {
		return mailSmtpHost;
	}
	public void setMailSmtpHost(String mailSmtpHost) {
		this.mailSmtpHost = mailSmtpHost;
	}
	public String getMailSmtpPort() {
		return mailSmtpPort;
	}
	public void setMailSmtpPort(String mailSmtpPort) {
		this.mailSmtpPort = mailSmtpPort;
	}
	public String getMailSmtpAccount() {
		return mailSmtpAccount;
	}
	public void setMailSmtpAccount(String mailSmtpAccount) {
		this.mailSmtpAccount = mailSmtpAccount;
	}
	public String getMailSmtpPassword() {
		return mailSmtpPassword;
	}
	public void setMailSmtpPassword(String mailSmtpPassword) {
		this.mailSmtpPassword = mailSmtpPassword;
	}
	public boolean isManageInventory() {
		return manageInventory;
	}
	public void setManageInventory(boolean manageInventory) {
		this.manageInventory = manageInventory;
	}
	public String getSystemRecord() {
		return systemRecord;
	}
	public void setSystemRecord(String systemRecord) {
		this.systemRecord = systemRecord;
	}
	public boolean isSingleCheckout() {
		return singleCheckout;
	}
	public void setSingleCheckout(boolean singleCheckout) {
		this.singleCheckout = singleCheckout;
	}
	public boolean isEnableCaptcha() {
		return enableCaptcha;
	}
	public void setEnableCaptcha(boolean enableCaptcha) {
		this.enableCaptcha = enableCaptcha;
	}
	public String getCaptchaPrivateKey() {
		return captchaPrivateKey;
	}
	public void setCaptchaPrivateKey(String captchaPrivateKey) {
		this.captchaPrivateKey = captchaPrivateKey;
	}
	public String getCaptchaPublicKey() {
		return captchaPublicKey;
	}
	public void setCaptchaPublicKey(String captchaPublicKey) {
		this.captchaPublicKey = captchaPublicKey;
	}
	public boolean isStoreCreditCard() {
		return storeCreditCard;
	}
	public void setStoreCreditCard(boolean storeCreditCard) {
		this.storeCreditCard = storeCreditCard;
	}
	public String getBingClientId() {
		return bingClientId;
	}
	public void setBingClientId(String bingClientId) {
		this.bingClientId = bingClientId;
	}
	public String getBingClientSecert() {
		return bingClientSecert;
	}
	public void setBingClientSecert(String bingClientSecert) {
		this.bingClientSecert = bingClientSecert;
	}
}
