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

import java.util.Vector;

public class HomeInfo extends DataInfo {
	Object[] homePageDatas;
	DataInfo homePageFeatureData;
	Vector<?> topRatedContents;
	Vector<?> mostViewedContents;
	String pageTitle;
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public Object[] getHomePageDatas() {
		return homePageDatas;
	}
	public void setHomePageDatas(Object[] homePageDatas) {
		this.homePageDatas = homePageDatas;
	}
	public DataInfo getHomePageFeatureData() {
		return homePageFeatureData;
	}
	public void setHomePageFeatureData(DataInfo homePageFeatureData) {
		this.homePageFeatureData = homePageFeatureData;
	}
	public Vector<?> getTopRatedContents() {
		return topRatedContents;
	}
	public void setTopRatedContents(Vector<?> topRatedContents) {
		this.topRatedContents = topRatedContents;
	}
	public Vector<?> getMostViewedContents() {
		return mostViewedContents;
	}
	public void setMostViewedContents(Vector<?> mostViewedContents) {
		this.mostViewedContents = mostViewedContents;
	}
}
