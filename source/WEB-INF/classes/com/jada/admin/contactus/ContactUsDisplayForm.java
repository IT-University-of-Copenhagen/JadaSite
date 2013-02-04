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

public class ContactUsDisplayForm {
	boolean remove;
	String contactUsId;
	String contactUsName;
	String contactUsEmail;
	String contactUsPhone;
	String seqNum;
	String active;
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
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
	public boolean isRemove() {
		return remove;
	}
	public void setRemove(boolean remove) {
		this.remove = remove;
	}
	public String getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}
}
