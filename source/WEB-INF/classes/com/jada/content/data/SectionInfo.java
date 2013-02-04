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

public class SectionInfo {
	String sectionNaturalKey;
	String topSectionNaturalKey;
	String sectionId;
	String topSectionId;
	String sectionShortTitle;
	String sectionTitle;
	String sectionDesc;
	String sectionBasicUrl;
	String sectionUrl;
	String filterUrl;
	long dataCount;
	int pageNum;
	int pageStart;
	int pageEnd;
	int pageTotal;
	String sortBy;
	Object sectionDatas[];
	SectionInfo titleSectionInfos[];
	SectionInfo childSectionInfos[];
	SectionCustomAttributeInfo sectionCustomAttributeInfos[];
	int childCount;

	public int getChildCount() {
		return childCount;
	}
	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}
	public int getPageEnd() {
		return pageEnd;
	}
	public void setPageEnd(int pageEnd) {
		this.pageEnd = pageEnd;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageStart() {
		return pageStart;
	}
	public void setPageStart(int pageStart) {
		this.pageStart = pageStart;
	}
	public int getPageTotal() {
		return pageTotal;
	}
	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}
	public Object[] getSectionDatas() {
		return sectionDatas;
	}
	public void setSectionDatas(Object[] sectionDatas) {
		this.sectionDatas = sectionDatas;
	}
	public String getSectionId() {
		return sectionId;
	}
	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}
	public String getSectionShortTitle() {
		return sectionShortTitle;
	}
	public void setSectionShortTitle(String sectionShortTitle) {
		this.sectionShortTitle = sectionShortTitle;
	}
	public String getSectionTitle() {
		return sectionTitle;
	}
	public void setSectionTitle(String sectionTitle) {
		this.sectionTitle = sectionTitle;
	}
	public Object getSectionDatas(int index) {
		return sectionDatas[index];
	}
	public String getSectionDesc() {
		return sectionDesc;
	}
	public void setSectionDesc(String sectionDesc) {
		this.sectionDesc = sectionDesc;
	}
	public SectionInfo[] getChildSectionInfos() {
		return childSectionInfos;
	}
	public void setChildSectionInfos(SectionInfo[] childSectionInfos) {
		this.childSectionInfos = childSectionInfos;
	}
	public String getSectionUrl() {
		return sectionUrl;
	}
	public void setSectionUrl(String sectionUrl) {
		this.sectionUrl = sectionUrl;
	}
	public SectionInfo[] getTitleSectionInfos() {
		return titleSectionInfos;
	}
	public void setTitleSectionInfos(SectionInfo[] titleSectionInfos) {
		this.titleSectionInfos = titleSectionInfos;
	}
	public String getTopSectionId() {
		return topSectionId;
	}
	public void setTopSectionId(String topSectionId) {
		this.topSectionId = topSectionId;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public String getSectionNaturalKey() {
		return sectionNaturalKey;
	}
	public void setSectionNaturalKey(String sectionNaturalKey) {
		this.sectionNaturalKey = sectionNaturalKey;
	}
	public String getTopSectionNaturalKey() {
		return topSectionNaturalKey;
	}
	public void setTopSectionNaturalKey(String topSectionNaturalKey) {
		this.topSectionNaturalKey = topSectionNaturalKey;
	}
	public long getDataCount() {
		return dataCount;
	}
	public void setDataCount(long dataCount) {
		this.dataCount = dataCount;
	}
	public SectionCustomAttributeInfo[] getSectionCustomAttributeInfos() {
		return sectionCustomAttributeInfos;
	}
	public void setSectionCustomAttributeInfos(
			SectionCustomAttributeInfo[] sectionCustomAttributeInfos) {
		this.sectionCustomAttributeInfos = sectionCustomAttributeInfos;
	}
	public String getFilterUrl() {
		return filterUrl;
	}
	public void setFilterUrl(String filterUrl) {
		this.filterUrl = filterUrl;
	}
	public String getSectionBasicUrl() {
		return sectionBasicUrl;
	}
	public void setSectionBasicUrl(String sectionBasicUrl) {
		this.sectionBasicUrl = sectionBasicUrl;
	}
}
