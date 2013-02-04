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

package com.jada.admin.homePage;

public class HomePageDisplayForm {
	String homePageId;
	String dataType;
	String description;
	String categoryName;
	String featureData;
	String seqNum;
	String published;
	String dataPublishOn;
	String dataExpireOn;
	public String getDataExpireOn() {
		return dataExpireOn;
	}
	public void setDataExpireOn(String dataExpireOn) {
		this.dataExpireOn = dataExpireOn;
	}
	public String getDataPublishOn() {
		return dataPublishOn;
	}
	public void setDataPublishOn(String dataPublishOn) {
		this.dataPublishOn = dataPublishOn;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPublished() {
		return published;
	}
	public void setPublished(String published) {
		this.published = published;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}
	public String getHomePageId() {
		return homePageId;
	}
	public void setHomePageId(String homePageId) {
		this.homePageId = homePageId;
	}
	public String getFeatureData() {
		return featureData;
	}
	public void setFeatureData(String featureData) {
		this.featureData = featureData;
	}
}
