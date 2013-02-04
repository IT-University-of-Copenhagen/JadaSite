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

package com.jada.xml.info;

import java.util.Date;

public class IndexerInfo {
	String indexerStatus;
	Date indexerStartTime;
	Date indexerLastUpdateTime;
	String removeStatus;
	int removeCount;
	String itemUpdateStatus;
	int itemUpdateCount;
	int itemTotalCount;
	String contentUpdateStatus;
	int contentUpdateCount;
	int contentTotalCount;
	public String getIndexerStatus() {
		return indexerStatus;
	}
	public void setIndexerStatus(String indexerStatus) {
		this.indexerStatus = indexerStatus;
	}
	public Date getIndexerStartTime() {
		return indexerStartTime;
	}
	public void setIndexerStartTime(Date indexerStartTime) {
		this.indexerStartTime = indexerStartTime;
	}
	public Date getIndexerLastUpdateTime() {
		return indexerLastUpdateTime;
	}
	public void setIndexerLastUpdateTime(Date indexerLastUpdateTime) {
		this.indexerLastUpdateTime = indexerLastUpdateTime;
	}
	public String getRemoveStatus() {
		return removeStatus;
	}
	public void setRemoveStatus(String removeStatus) {
		this.removeStatus = removeStatus;
	}
	public int getRemoveCount() {
		return removeCount;
	}
	public void setRemoveCount(int removeCount) {
		this.removeCount = removeCount;
	}
	public int getItemUpdateCount() {
		return itemUpdateCount;
	}
	public void setItemUpdateCount(int itemUpdateCount) {
		this.itemUpdateCount = itemUpdateCount;
	}
	public String getContentUpdateStatus() {
		return contentUpdateStatus;
	}
	public void setContentUpdateStatus(String contentUpdateStatus) {
		this.contentUpdateStatus = contentUpdateStatus;
	}
	public int getContentUpdateCount() {
		return contentUpdateCount;
	}
	public void setContentUpdateCount(int contentUpdateCount) {
		this.contentUpdateCount = contentUpdateCount;
	}
	public String getItemUpdateStatus() {
		return itemUpdateStatus;
	}
	public void setItemUpdateStatus(String itemUpdateStatus) {
		this.itemUpdateStatus = itemUpdateStatus;
	}
	public int getItemTotalCount() {
		return itemTotalCount;
	}
	public void setItemTotalCount(int itemTotalCount) {
		this.itemTotalCount = itemTotalCount;
	}
	public int getContentTotalCount() {
		return contentTotalCount;
	}
	public void setContentTotalCount(int contentTotalCount) {
		this.contentTotalCount = contentTotalCount;
	}
}
