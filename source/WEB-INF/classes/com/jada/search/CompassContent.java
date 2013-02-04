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

package com.jada.search;

import java.util.Date;

public class CompassContent {
	private String siteId;
	private Long siteProfileClassId;
	private String contentNaturalKey;
	private Long contentId;
	private char published;
	private String recUpdateBy;
	private Date recUpdateDatetime;
	private String recCreateBy;
	private Date recCreateDatetime;
	private String userId;
	public Long getContentId() {
		return contentId;
	}
	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	public char getPublished() {
		return published;
	}
	public void setPublished(char published) {
		this.published = published;
	}
	public String getRecUpdateBy() {
		return recUpdateBy;
	}
	public void setRecUpdateBy(String recUpdateBy) {
		this.recUpdateBy = recUpdateBy;
	}
	public Date getRecUpdateDatetime() {
		return recUpdateDatetime;
	}
	public void setRecUpdateDatetime(Date recUpdateDatetime) {
		this.recUpdateDatetime = recUpdateDatetime;
	}
	public String getRecCreateBy() {
		return recCreateBy;
	}
	public void setRecCreateBy(String recCreateBy) {
		this.recCreateBy = recCreateBy;
	}
	public Date getRecCreateDatetime() {
		return recCreateDatetime;
	}
	public void setRecCreateDatetime(Date recCreateDatetime) {
		this.recCreateDatetime = recCreateDatetime;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getContentNaturalKey() {
		return contentNaturalKey;
	}
	public void setContentNaturalKey(String contentNaturalKey) {
		this.contentNaturalKey = contentNaturalKey;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Long getSiteProfileClassId() {
		return siteProfileClassId;
	}
	public void setSiteProfileClassId(Long siteProfileClassId) {
		this.siteProfileClassId = siteProfileClassId;
	}
}
