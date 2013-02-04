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

package com.jada.content.data;

public class ContactUsInfo extends DataInfo {
	String contactUsName;
	String contactUsAddressLine1;
	String contactUsAddressLine2;
	String contactUsCityName;
	String contactUsStateName;
	String contactUsStateCode;
	String contactUsCountryName;
	String contactUsCountryCode;
	String contactUsZipCode;
	String contactUsDesc;
	String seqNum;
	String contactUsEmail;
	String contactUsPhone;
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
	public String getContactUsDesc() {
		return contactUsDesc;
	}
	public void setContactUsDesc(String contactUsDesc) {
		this.contactUsDesc = contactUsDesc;
	}
	public String getContactUsCountryCode() {
		return contactUsCountryCode;
	}
	public void setContactUsCountryCode(String contactUsCountryCode) {
		this.contactUsCountryCode = contactUsCountryCode;
	}
	public String getContactUsCountryName() {
		return contactUsCountryName;
	}
	public void setContactUsCountryName(String contactUsCountryName) {
		this.contactUsCountryName = contactUsCountryName;
	}
	public String getContactUsEmail() {
		return contactUsEmail;
	}
	public void setContactUsEmail(String contactUsEmail) {
		this.contactUsEmail = contactUsEmail;
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
	public String getContactUsStateName() {
		return contactUsStateName;
	}
	public void setContactUsStateName(String contactUsStateName) {
		this.contactUsStateName = contactUsStateName;
	}
	public String getContactUsZipCode() {
		return contactUsZipCode;
	}
	public void setContactUsZipCode(String contactUsZipCode) {
		this.contactUsZipCode = contactUsZipCode;
	}
	public String getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}
}
