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

public class CategoryInfo {
	String catNaturalKey;
	String topCatNaturalKey;
	String catId;
	String topCatId;
	String catShortTitle;
	String catTitle;
	String catDesc;
	String categoryBasicUrl;
	String categoryUrl;
	String filterUrl;
	long dataCount;
	int pageNum;
	int pageStart;
	int pageEnd;
	int pageTotal;
	String sortBy;
	Object categoryDatas[];
	CategoryInfo titleCategoryInfos[];
	CategoryInfo childCategoryInfos[];
	CategoryCustomAttributeInfo categoryCustomAttributeInfos[];
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
	public Object[] getCategoryDatas() {
		return categoryDatas;
	}
	public void setCategoryDatas(Object[] categoryDatas) {
		this.categoryDatas = categoryDatas;
	}
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public String getCatShortTitle() {
		return catShortTitle;
	}
	public void setCatShortTitle(String catShortTitle) {
		this.catShortTitle = catShortTitle;
	}
	public String getCatTitle() {
		return catTitle;
	}
	public void setCatTitle(String catTitle) {
		this.catTitle = catTitle;
	}
	public Object getCategoryDatas(int index) {
		return categoryDatas[index];
	}
	public String getCatDesc() {
		return catDesc;
	}
	public void setCatDesc(String catDesc) {
		this.catDesc = catDesc;
	}
	public CategoryInfo[] getChildCategoryInfos() {
		return childCategoryInfos;
	}
	public void setChildCategoryInfos(CategoryInfo[] childCategoryInfos) {
		this.childCategoryInfos = childCategoryInfos;
	}
	public String getCategoryUrl() {
		return categoryUrl;
	}
	public void setCategoryUrl(String categoryUrl) {
		this.categoryUrl = categoryUrl;
	}
	public CategoryInfo[] getTitleCategoryInfos() {
		return titleCategoryInfos;
	}
	public void setTitleCategoryInfos(CategoryInfo[] titleCategoryInfos) {
		this.titleCategoryInfos = titleCategoryInfos;
	}
	public String getTopCatId() {
		return topCatId;
	}
	public void setTopCatId(String topCatId) {
		this.topCatId = topCatId;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public String getCatNaturalKey() {
		return catNaturalKey;
	}
	public void setCatNaturalKey(String catNaturalKey) {
		this.catNaturalKey = catNaturalKey;
	}
	public String getTopCatNaturalKey() {
		return topCatNaturalKey;
	}
	public void setTopCatNaturalKey(String topCatNaturalKey) {
		this.topCatNaturalKey = topCatNaturalKey;
	}
	public long getDataCount() {
		return dataCount;
	}
	public void setDataCount(long dataCount) {
		this.dataCount = dataCount;
	}
	public CategoryCustomAttributeInfo[] getCategoryCustomAttributeInfos() {
		return categoryCustomAttributeInfos;
	}
	public void setCategoryCustomAttributeInfos(
			CategoryCustomAttributeInfo[] categoryCustomAttributeInfos) {
		this.categoryCustomAttributeInfos = categoryCustomAttributeInfos;
	}
	public String getFilterUrl() {
		return filterUrl;
	}
	public void setFilterUrl(String filterUrl) {
		this.filterUrl = filterUrl;
	}
	public String getCategoryBasicUrl() {
		return categoryBasicUrl;
	}
	public void setCategoryBasicUrl(String categoryBasicUrl) {
		this.categoryBasicUrl = categoryBasicUrl;
	}
}
