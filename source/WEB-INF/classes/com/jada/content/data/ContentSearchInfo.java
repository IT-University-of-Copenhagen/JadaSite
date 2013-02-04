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

public class ContentSearchInfo extends DataInfo {
	String infoType = "content";
	String contentId;
	String contentTitle;
	String contentShortDesc;
	String contentDesc;
	String contentUrl;
	String contentUpdateName;
	String recUpdateBy;
	String recUpdateDatetime;
	String recCreateBy;
	String recCreateDatetime;
	public String getInfoType() {
		return infoType;
	}
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getContentTitle() {
		return contentTitle;
	}
	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}
	public String getContentShortDesc() {
		return contentShortDesc;
	}
	public void setContentShortDesc(String contentShortDesc) {
		this.contentShortDesc = contentShortDesc;
	}
	public String getContentDesc() {
		return contentDesc;
	}
	public void setContentDesc(String contentDesc) {
		this.contentDesc = contentDesc;
	}
	public String getContentUrl() {
		return contentUrl;
	}
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	public String getContentUpdateName() {
		return contentUpdateName;
	}
	public void setContentUpdateName(String contentUpdateName) {
		this.contentUpdateName = contentUpdateName;
	}
	public String getRecUpdateBy() {
		return recUpdateBy;
	}
	public void setRecUpdateBy(String recUpdateBy) {
		this.recUpdateBy = recUpdateBy;
	}
	public String getRecUpdateDatetime() {
		return recUpdateDatetime;
	}
	public void setRecUpdateDatetime(String recUpdateDatetime) {
		this.recUpdateDatetime = recUpdateDatetime;
	}
	public String getRecCreateBy() {
		return recCreateBy;
	}
	public void setRecCreateBy(String recCreateBy) {
		this.recCreateBy = recCreateBy;
	}
	public String getRecCreateDatetime() {
		return recCreateDatetime;
	}
	public void setRecCreateDatetime(String recCreateDatetime) {
		this.recCreateDatetime = recCreateDatetime;
	}
}
