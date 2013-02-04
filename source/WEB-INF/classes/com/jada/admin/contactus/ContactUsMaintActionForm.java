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

package com.jada.admin.contactus;

import org.apache.struts.util.LabelValueBean;

import com.jada.admin.AdminMaintActionForm;

public class ContactUsMaintActionForm
    extends AdminMaintActionForm {
	private static final long serialVersionUID = -7098142239723101083L;
	String mode;
    String contactUsId;
    String contactUsName;
    boolean contactUsNameLangFlag;
    String contactUsNameLang;
    String contactUsEmail;
    String contactUsPhone;
	String contactUsAddressLine1;
	String contactUsAddressLine2;
	String contactUsCityName;
	String contactUsStateCode;
	String contactUsCountryCode;
	String contactUsZipCode;
	String contactUsDesc;
	boolean contactUsDescLangFlag;
	String contactUsDescLang;
    String seqNum;
    String active;
	LabelValueBean states[];
	LabelValueBean countries[];
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getContactUsAddressLine1() {
		return contactUsAddressLine1;
	}
	public void setContactUsAddressLine1(String contactUsAddressLine1) {
		this.contactUsAddressLine1 = contactUsAddressLine1;
	}
	public String getContactUsAddressLine2() {
		return contactUsAddressLine2;
	}
	public void setContactUsAddressLine2(String contactUsAddressLine2) {
		this.contactUsAddressLine2 = contactUsAddressLine2;
	}
	public String getContactUsCityName() {
		return contactUsCityName;
	}
	public void setContactUsCityName(String contactUsCityName) {
		this.contactUsCityName = contactUsCityName;
	}
	public String getContactUsCountryCode() {
		return contactUsCountryCode;
	}
	public void setContactUsCountryCode(String contactUsCountryCode) {
		this.contactUsCountryCode = contactUsCountryCode;
	}
	public String getContactUsEmail() {
		return contactUsEmail;
	}
	public void setContactUsEmail(String contactUsEmail) {
		this.contactUsEmail = contactUsEmail;
	}
	public String getContactUsId() {
		return contactUsId;
	}
	public void setContactUsId(String contactUsId) {
		this.contactUsId = contactUsId;
	}
	public String getContactUsName() {
		return contactUsName;
	}
	public void setContactUsName(String contactUsName) {
		this.contactUsName = contactUsName;
	}
	public String getContactUsPhone() {
		return contactUsPhone;
	}
	public void setContactUsPhone(String contactUsPhone) {
		this.contactUsPhone = contactUsPhone;
	}
	public String getContactUsStateCode() {
		return contactUsStateCode;
	}
	public void setContactUsStateCode(String contactUsStateCode) {
		this.contactUsStateCode = contactUsStateCode;
	}
	public String getContactUsZipCode() {
		return contactUsZipCode;
	}
	public void setContactUsZipCode(String contactUsZipCode) {
		this.contactUsZipCode = contactUsZipCode;
	}
	public LabelValueBean[] getCountries() {
		return countries;
	}
	public void setCountries(LabelValueBean[] countries) {
		this.countries = countries;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}
	public LabelValueBean[] getStates() {
		return states;
	}
	public void setStates(LabelValueBean[] states) {
		this.states = states;
	}
	public String getContactUsDesc() {
		return contactUsDesc;
	}
	public void setContactUsDesc(String contactUsDesc) {
		this.contactUsDesc = contactUsDesc;
	}
	public boolean isContactUsNameLangFlag() {
		return contactUsNameLangFlag;
	}
	public void setContactUsNameLangFlag(boolean contactUsNameLangFlag) {
		this.contactUsNameLangFlag = contactUsNameLangFlag;
	}
	public String getContactUsNameLang() {
		return contactUsNameLang;
	}
	public void setContactUsNameLang(String contactUsNameLang) {
		this.contactUsNameLang = contactUsNameLang;
	}
	public boolean isContactUsDescLangFlag() {
		return contactUsDescLangFlag;
	}
	public void setContactUsDescLangFlag(boolean contactUsDescLangFlag) {
		this.contactUsDescLangFlag = contactUsDescLangFlag;
	}
	public String getContactUsDescLang() {
		return contactUsDescLang;
	}
	public void setContactUsDescLang(String contactUsDescLang) {
		this.contactUsDescLang = contactUsDescLang;
	}
}