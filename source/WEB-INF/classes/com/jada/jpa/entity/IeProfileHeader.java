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

package com.jada.jpa.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ie_profile_header")
public class IeProfileHeader implements java.io.Serializable {

	private static final long serialVersionUID = -3977646793900314159L;
	private Long ieProfileHeaderId;
	private String ieProfileHeaderName;
	private char ieProfileType;
	private char systemRecord;
	private String recUpdateBy;
	private Date recUpdateDatetime;
	private String recCreateBy;
	private Date recCreateDatetime;
	private Set<IeProfileDetail> ieProfileDetails = new HashSet<IeProfileDetail>(0);
	private Site site;

	public IeProfileHeader() {
	}

	@Id
	@GeneratedValue
	@Column(name = "ie_profile_header_id", nullable = false)
	public Long getIeProfileHeaderId() {
		return this.ieProfileHeaderId;
	}

	public void setIeProfileHeaderId(Long ieProfileHeaderId) {
		this.ieProfileHeaderId = ieProfileHeaderId;
	}

	@Column(name = "ie_profile_header_name", nullable = false, length = 80)
	public String getIeProfileHeaderName() {
		return this.ieProfileHeaderName;
	}

	public void setIeProfileHeaderName(String ieProfileHeaderName) {
		this.ieProfileHeaderName = ieProfileHeaderName;
	}

	@Column(name = "ie_profile_type", nullable = false, length = 1)
	public char getIeProfileType() {
		return this.ieProfileType;
	}

	public void setIeProfileType(char ieProfileType) {
		this.ieProfileType = ieProfileType;
	}

	@Column(name = "system_record", nullable = false, length = 1)
	public char getSystemRecord() {
		return this.systemRecord;
	}

	public void setSystemRecord(char systemRecord) {
		this.systemRecord = systemRecord;
	}

	@Column(name = "rec_update_by", nullable = false, length = 20)
	public String getRecUpdateBy() {
		return this.recUpdateBy;
	}

	public void setRecUpdateBy(String recUpdateBy) {
		this.recUpdateBy = recUpdateBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "rec_update_datetime", nullable = false)
	public Date getRecUpdateDatetime() {
		return this.recUpdateDatetime;
	}

	public void setRecUpdateDatetime(Date recUpdateDatetime) {
		this.recUpdateDatetime = recUpdateDatetime;
	}

	@Column(name = "rec_create_by", nullable = false, length = 20)
	public String getRecCreateBy() {
		return this.recCreateBy;
	}

	public void setRecCreateBy(String recCreateBy) {
		this.recCreateBy = recCreateBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "rec_create_datetime", nullable = false)
	public Date getRecCreateDatetime() {
		return this.recCreateDatetime;
	}

	public void setRecCreateDatetime(Date recCreateDatetime) {
		this.recCreateDatetime = recCreateDatetime;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "ie_profile_header_id", updatable = false)
	@OrderBy(value="seqNum")
	public Set<IeProfileDetail> getIeProfileDetails() {
		return this.ieProfileDetails;
	}

	public void setIeProfileDetails(Set<IeProfileDetail> ieProfileDetails) {
		this.ieProfileDetails = ieProfileDetails;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id", nullable = false)
	public Site getSite() {
		return this.site;
	}

	public void setSite(Site site) {
		this.site = site;
	}
}
