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

public class SiteProfileForm {
	boolean locked;
	boolean remove;
	String siteProfileId;
	String siteProfileClassId;
	String siteProfileClassIdError;
	String seqNum;
	String seqNumError;
	boolean active;
	public boolean isRemove() {
		return remove;
	}
	public void setRemove(boolean remove) {
		this.remove = remove;
	}
	public String getSiteProfileId() {
		return siteProfileId;
	}
	public void setSiteProfileId(String siteProfileId) {
		this.siteProfileId = siteProfileId;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getSiteProfileClassId() {
		return siteProfileClassId;
	}
	public void setSiteProfileClassId(String siteProfileClassId) {
		this.siteProfileClassId = siteProfileClassId;
	}
	public String getSiteProfileClassIdError() {
		return siteProfileClassIdError;
	}
	public void setSiteProfileClassIdError(String siteProfileClassIdError) {
		this.siteProfileClassIdError = siteProfileClassIdError;
	}
	public String getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}
	public String getSeqNumError() {
		return seqNumError;
	}
	public void setSeqNumError(String seqNumError) {
		this.seqNumError = seqNumError;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
}
